package com.copsis.models.chubb;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.copsis.constants.ConstantsValue;
import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraCoberturasModel;
import com.copsis.models.EstructuraJsonModel;
import com.copsis.models.EstructuraRecibosModel;
import com.copsis.models.EstructuraUbicacionesModel;

public class ChubbDiversosModel {
	// Clases
	DataToolsModel fn = new DataToolsModel();
	EstructuraJsonModel modelo = new EstructuraJsonModel();
	// Variables
	private String contenido;
	private String recibos = "";
	private String ubicacionesExt = "";
	private static final String ROBO_Y_O_ASALTO = "ROBO CON VIOLENCIA Y-O ASALTO DE EQUIPO";
	private static final String FIJO_DENTRO_DEL_PREDIO = "FIJO DENTRO DEL PREDIO";
	
	// constructor
	public ChubbDiversosModel(String contenido, String recibos,String ubicacionesExt) {
		this.contenido = contenido;
		this.recibos = recibos;
		this.ubicacionesExt=ubicacionesExt;

	}

	public EstructuraJsonModel procesar() {


		StringBuilder resultado = new StringBuilder();
		int inicio = 0;
		int fin = 0;
		String newcontenido = "";

		contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());
		contenido = contenido.replace("PÓLIZA DE SEGURO", "POLIZA DE SEGURO")
				.replace("Póliza###", "Póliza:###")
				.replace("Expedición", ConstantsValue.EXPEDICION)
				.replace("Fraccionado", ConstantsValue.FRACIONADO2)
				.replace("I.V.A:", "I.V.A.:");
		recibos = fn.remplazarMultiple(recibos, fn.remplazosGenerales());
		try {

			modelo.setTipo(7);
			modelo.setCia(1);
			modelo.setRamo("Daños");

			inicio = contenido.indexOf("POLIZA DE SEGURO");
			fin = contenido.indexOf(ConstantsValue.CARACTERISTICASRIESGO);				
			fin = fin == -1 ? contenido.indexOf("Desglose de coberturas"):fin;
			fin = fin ==-1? contenido.indexOf("Características del Riesgo"):fin;
			
		
			

			if (inicio > 0 && fin > 0 && inicio < fin) {
				newcontenido = contenido.substring(inicio, fin).replace("C.P", "C/P");
				obtenerEndoso(newcontenido);
				for (int i = 0; i < newcontenido.split("\n").length; i++) {				
					if (newcontenido.split("\n")[i].contains("Póliza:")
							&& newcontenido.split("\n")[i].contains("Vigencia")) {
						modelo.setPoliza(newcontenido.split("\n")[i].split(ConstantsValue.POLIZA_ACENT2)[1].split("Vigencia:")[0]
								.replace("###", "").trim());
						modelo.setVigenciaDe(fn.formatDateMonthCadena(
								newcontenido.split("\n")[i].split("Vigencia:")[1].split(ConstantsValue.HORAS)[0]
										.replace("###", "").replace("12:00", "").replace("Del", "").trim()));
						modelo.setVigenciaA(
								fn.formatDateMonthCadena(newcontenido.split("\n")[i].split(ConstantsValue.HORAS)[1]
										.split(ConstantsValue.HORAS)[0].replace("###", "").replace("12:00", "")
												.replace("al", "").trim()));
					}
					if (modelo.getPoliza().isEmpty() && newcontenido.split("\n")[i].contains("Póliza")
							&& newcontenido.split("\n")[i].contains("Vigencia")) {
								modelo.setPoliza(newcontenido.split("\n")[i].split("Póliza")[1].split("Vigencia")[0]
								.replace("###", "").trim());
						List<String> valores = fn.obtenVigePoliza(newcontenido.split("\n")[i]);
						if (valores.size() > 1) {
							modelo.setVigenciaDe(fn.formatDateMonthCadena(valores.get(0)));
							modelo.setVigenciaA(fn.formatDateMonthCadena(valores.get(1)));
							modelo.setFechaEmision(modelo.getVigenciaDe());
						}			

					}
					if (newcontenido.split("\n")[i].contains(ConstantsValue.ASEGURADO)
							&& newcontenido.split("\n")[i].contains("C.P:")) {
						modelo.setCteNombre((newcontenido.split("\n")[i].split(ConstantsValue.ASEGURADO)[1].split("C.P:")[0]
								.replace("###", " ")).trim());
						modelo.setCp(newcontenido.split("\n")[i].split("C.P:")[1].replace("\r", "").replace("###", ""));
					}
					if(modelo.getCteNombre().isEmpty() && newcontenido.split("\n")[i].contains("Contratante:")){
                        modelo.setCteNombre(newcontenido.split("\n")[i].split("Contratante:")[1].replace("\r", "").trim());
					}
				
					if (newcontenido.split("\n")[i].contains(ConstantsValue.DOMICILIO )
							&& newcontenido.split("\n")[i].contains(ConstantsValue.TELEFONOAC)) {
						modelo.setCteDireccion((newcontenido.split("\n")[i].split(ConstantsValue.DOMICILIO )[1].split(ConstantsValue.TELEFONOAC)[0]
								+ " " + newcontenido.split("\n")[i + 1].split("RFC:")[0]).replace("###", " ")
										.replace("\r", "").trim());
					}else if(newcontenido.split("\n")[i].trim().split(ConstantsValue.DOMICILIO ).length>1&& (i+1) < newcontenido.split("\n")[i].length() && !newcontenido.split("\n")[i].contains(ConstantsValue.RFC) && !newcontenido.split("\n")[i].contains("Teléfono")) {
						String direccion = newcontenido.split("\n")[i].split(ConstantsValue.DOMICILIO )[1].trim();
						direccion +=  " " +newcontenido.split("\n")[i+1];
						direccion = fn.eliminaSpacios(direccion.replace("###", " ").replace("@@@", "").split("C/P")[0].trim());
						modelo.setCteDireccion(direccion);
					}
					
					if (modelo.getCteNombre().length() == 0	&& newcontenido.split("\n")[i].contains("propietario") && (i+1)<newcontenido.split("\n").length
					&& newcontenido.split("\n")[i+1].split(ConstantsValue.ASEGURADO).length > 1) {
						
							modelo.setCteNombre(fn.elimgatos(newcontenido.split("\n")[i+1].split(ConstantsValue.ASEGURADO)[1].trim()).split("###")[0]);
												
					}
					
					if(modelo.getCp().length() == 0 && newcontenido.split("\n")[i].contains("C/P:")) {
					      List<String> valores = fn.obtenerListNumeros2(modelo.getCteDireccion());
							if(!valores.isEmpty()){
								modelo.setCp(valores.stream()
									.filter(numero -> String.valueOf(numero).length() >= 4)
									.collect(Collectors.toList()).get(0));
							}
					}
					
					if (newcontenido.split("\n")[i].contains("RFC:")) {
						modelo.setRfc(newcontenido.split("\n")[i].split("RFC:")[1].replace("\r", "").replace("###", "")
								.trim());
					}
					if (newcontenido.split("\n")[i].contains("Moneda:")
							&& newcontenido.split("\n")[i].contains("pago")) {
						modelo.setMoneda(fn.moneda(newcontenido.split("\n")[i].split("Moneda:")[1].split("Forma")[0]
								.replace("###", "").trim()));
						modelo.setFormaPago(fn.formaPagoSring(newcontenido.split("\n")[i].split("pago:")[1].replace("\r", "")
								.replace("###", "").trim()));
					}

					if(modelo.getMoneda() == 0 && newcontenido.split("\n")[i].contains("Moneda:") ){
						modelo.setMoneda(fn.buscaMonedaEnTexto(newcontenido.split("\n")[i]
								.replace("###", "").trim()));
					}

					if(modelo.getFormaPago() == 0 && newcontenido.split("\n")[i].contains("Forma de pago:") ){
						modelo.setFormaPago(fn.formaPagoSring(newcontenido.split("\n")[i].replace("\r", "")
								.replace("###", "").trim()));
					 }

					if (newcontenido.split("\n")[i].contains(ConstantsValue.EMISION2)
							&& newcontenido.split("\n")[i].contains("Descuento")) {
						String x = newcontenido.split("\n")[i].split(ConstantsValue.EMISION2)[1].split("Descuento")[0]
								.replace("###", "").trim().replace(" ", "###");
						x = x.split("###")[0] + "-" + x.split("###")[1] + "-" + x.split("###")[2];				
						modelo.setFechaEmision(fn.formatDateMonthCadena(x.replace(":", "")));
					}
					
					if(modelo.getFechaEmision().length() == 0 && newcontenido.split("\n")[i].contains(ConstantsValue.EMISION2)&& newcontenido.split("\n")[i].trim().split(ConstantsValue.EMISION2).length>1 && newcontenido.split("\n")[i].contains("Referencia")) {
						
							String x = newcontenido.split("\n")[i].split(ConstantsValue.EMISION2)[1].split("Referencia")[0].trim().replace("P. M.", "P/M").replace("P.M", "P/M");
							if(x.contains("P/M")) {
								x = x.split("P/M")[0].trim();
								x = fn.elimgatos(x).substring(0,x.lastIndexOf(" ")).replace(" ", "-");
								modelo.setFechaEmision(fn.formatDateMonthCadena(x.replace(":", "")));						
							}
						
						
					}
					if (newcontenido.split("\n")[i].contains("Paquete:")) {
						modelo.setPlan(newcontenido.split("\n")[i].split("Paquete:")[1].replace("\r", "")
								.replace("###", "").trim());
					}
					if (newcontenido.split("\n")[i].contains("agente:")) {
						modelo.setCveAgente(newcontenido.split("\n")[i].split("agente:")[1].replace("\r", "")
								.replace("###", "").trim());
						if(modelo.getCveAgente().split(" ").length > 1) {
							modelo.setCveAgente(modelo.getCveAgente().split(" ")[0]);
						}
					}
				}
			}


			// PRIMAS
			inicio = contenido.indexOf("o especificación");
			if(inicio == -1) {
				inicio = contenido.indexOf("Prima Neta");
			}
			fin = contenido.indexOf("Notas del riesgo");
			if(fin == -1) {
				fin = contenido.indexOf("Artículo");
			}
			
			if(fin == -1) {
				fin = contenido.indexOf("En cumplimiento a ");
			}

			if (inicio > -1 && fin > inicio) {
				newcontenido = contenido.substring(inicio, fin).replace("@@@", "");
				for (int i = 0; i < newcontenido.split("\n").length; i++) {
					
					if (newcontenido.split("\n")[i].contains("Neta")) {
						List<String> valores = fn.obtenerListNumeros2(newcontenido.split("\n")[i].replace(",", ""));
						if(!valores.isEmpty()){
							modelo.setPrimaneta(fn.castBigDecimal(fn.castDouble(valores.get(0))));
						}							
					}
				
					if (newcontenido.split("\n")[i].contains("Financiamiento")
							&& newcontenido.split("\n")[i].contains(ConstantsValue.EXPEDICION)) {
						
						modelo.setRecargo(fn.castBigDecimal(
								fn.castDouble(newcontenido.split("\n")[i].split(ConstantsValue.FRACIONADO2)[1].split("Gastos")[0]
										.replace("###", "").replace("\r", "").replace(":", "").trim())));
						modelo.setDerecho(
								fn.castBigDecimal(fn.castDouble(newcontenido.split("\n")[i].split(ConstantsValue.EXPEDICION)[1]
										.replace("###", "").replace("\r", "").replace(":", "").trim())));
					}
					
					if(modelo.getRecargo() == null && modelo.getRecargo().intValue() == 0 && newcontenido.split("\n")[i].contains(ConstantsValue.FRACIONADO2) ) {
						List<String> valores = fn.obtenerListNumeros2(newcontenido.split("\n")[i].replace(",", ""));
						if(!valores.isEmpty()){
							modelo.setRecargo(fn.castBigDecimal(fn.castDouble(valores.get(0))));
						}							
					
					}
					
					if(modelo.getDerecho() == null && modelo.getDerecho().intValue() == 0 && newcontenido.split("\n")[i].contains(ConstantsValue.EXPEDICION) ) {
						List<String> valores = fn.obtenerListNumeros2(newcontenido.split("\n")[i].replace(",", ""));
						if(!valores.isEmpty()){
							modelo.setDerecho(fn.castBigDecimal(fn.castDouble(valores.get(0))));
						}
					}
					
					if (newcontenido.split("\n")[i].contains("I.V.A.")) {
						List<String> valores = fn.obtenerListNumeros2(newcontenido.split("\n")[i].replace(",", ""));
						if(!valores.isEmpty()){
							modelo.setIva(fn.castBigDecimal(fn.castDouble(valores.get(0))));
						}							
					
					}
					if ( newcontenido.split("\n")[i].contains("Total:") && newcontenido.split("\n")[i].contains("MXN")) {
						List<String> valores = fn.obtenerListNumeros2(newcontenido.split("\n")[i].replace(",", ""));
						if(!valores.isEmpty()){
							modelo.setPrimaTotal(fn.castBigDecimal(fn.castDouble(valores.get(0))));
						}
					}

				}
			}

			if(recibos.contains("Desglose de pago")){
				modelo.setAgente(recibos.split("Clave interna del agente:")[1].split("Desglose de pago")[0]
						.replace("###", "").replace("\r\n", "").trim());
			}
	List<EstructuraUbicacionesModel> ubicaciones = new ArrayList<>();

	StringBuilder contenidoUb = new StringBuilder();
	   
		   for (int i = 0; i < ubicacionesExt.split(ConstantsValue.CARACTERISTICASRIESGO).length; i++) {
			  if(i >  0){
  					contenidoUb.append( ubicacionesExt.split(ConstantsValue.CARACTERISTICASRIESGO)[i].split("Coberturas")[0]+"\n");
			  }            			
		   }
					
					int tlineas =contenidoUb.toString().split("\n").length;
			for (int i = 0; i < contenidoUb.toString().split("\n").length; i++) {
				
				EstructuraUbicacionesModel ubicacion = new EstructuraUbicacionesModel();
				
				if(contenidoUb.toString().split("\n")[i].contains("Dirección")){
					ubicacion.setCalle(contenidoUb.toString().split("\n")[i].split(fn.palabraRgx(contenidoUb.toString().split("\n")[i], ConstantsValue.DIRECCION2))[1].replace("###", "").replace("\r", "").trim());
					if(tlineas > 4 && contenidoUb.toString().split("\n")[i+3].contains("Giro")){
						ubicacion.setGiro(contenidoUb.toString().split("\n")[i+3].split("Giro")[1]);
					}
					if (tlineas > 5 && contenidoUb.toString().split("\n")[i+4].contains(ConstantsValue.TECHOS)) {				
						ubicacion.setTechos(fn.material(contenidoUb.toString().split("\n")[i+4].split(ConstantsValue.TECHOS)[1].split( ConstantsValue.INCENDIOMYM )[1].toUpperCase().replace("###", "").trim()));						
						ubicacion.setMuros(fn.material(contenidoUb.toString().split("\n")[i+4].split(ConstantsValue.MUROS)[1].split(ConstantsValue.TECHOS)[0].toUpperCase().replace("###", "").trim()));
					}
					if (tlineas > 6 && contenidoUb.toString().split("\n")[i+5].contains(ConstantsValue.TECHOS)) {				
						ubicacion.setTechos(fn.material(contenidoUb.toString().split("\n")[i+5].split(ConstantsValue.TECHOS)[1].split(ConstantsValue.INCENDIOMYM)[1].toUpperCase().replace("###", "").trim()));						
						ubicacion.setMuros(fn.material(contenidoUb.toString().split("\n")[i+5].split(ConstantsValue.MUROS)[1].split(ConstantsValue.TECHOS)[0].toUpperCase().replace("###", "").trim()));
					}
					if (tlineas > 6 && contenidoUb.toString().split("\n")[i+5].contains(ConstantsValue.NUMPISOSINCENDIO)) {
						ubicacion.setNiveles(Integer.parseInt(contenidoUb.toString().split("\n")[i+5].split(ConstantsValue.INCENDIOMYMPT)[1].replace("###", "").replace("\r", "").trim()));
					}
					
					if (tlineas > 8 && contenidoUb.toString().split("\n")[i+6].contains(ConstantsValue.NUMPISOSINCENDIO)) {
						ubicacion.setNiveles(Integer.parseInt(contenidoUb.toString().split("\n")[i+6].split(ConstantsValue.INCENDIOMYMPT)[1].replace("###", "").replace("\r", "").trim()));
					}
					
					
					ubicaciones.add(ubicacion);									
				}

			  }
			  modelo.setUbicaciones(ubicaciones);


			// UBICACIONES
			inicio = contenido.indexOf("Características del riesgo");
			fin = contenido.indexOf("Prima");
	
			
		if(contenidoUb.toString().isEmpty() && inicio > -1 && fin > inicio){												
				EstructuraUbicacionesModel ubicacion = new EstructuraUbicacionesModel();
				for (int i = 0; i < newcontenido.split("\n").length; i++) {
					if (newcontenido.split("\n")[i].contains(fn.palabraRgx(newcontenido.split("\n")[i], ConstantsValue.DIRECCION2))) {
						ubicacion.setCalle(newcontenido.split("\n")[i].split(fn.palabraRgx(newcontenido.split("\n")[i], ConstantsValue.DIRECCION2))[1].replace("###", "")
								.replace("\r", "").trim());
						if((i+2)<newcontenido.split("\n").length && !newcontenido.split("\n")[i+1].contains("###") &&  fn.numTx(newcontenido.split("\n")[i+1].trim()).length()>0) {
						
								String cp = fn.numTx(newcontenido.split("\n")[i+1].trim());
								if(cp.length() == 4) {
									ubicacion.setCp("0"+cp);
								}else if(cp.length() == 5) {
									ubicacion.setCp(cp);
								}
							
						}
						
						String noExterno = fn.numTx(ubicacion.getCalle());
						if(noExterno.length()> 0) {
							String calle = ubicacion.getCalle();
							noExterno = calle.substring(calle.indexOf(noExterno),calle.length()-1);
							noExterno = noExterno.split(" ")[0];
							ubicacion.setNoExterno(noExterno);
						}
					}

					if(newcontenido.split("\n")[i].contains("Origen:") &&  newcontenido.split("\n")[i].contains("Destino")){
					    ubicacion.setCalle(newcontenido.split("\n")[i].split("Origen:")[1].split("Destino")[0].replace("###", "").trim());
					}
					
					if (newcontenido.split("\n")[i].contains(ConstantsValue.TECHO)) {
						ubicacion.setTechos(fn.material(newcontenido.split("\n")[i].split(ConstantsValue.TECHO)[1].split("Tipo")[0].toUpperCase().replace("###", "").trim()));
						ubicacion.setMuros(fn.material(newcontenido.split("\n")[i].split(ConstantsValue.TECHO)[1].split("Tipo")[0].toUpperCase().replace("###", "").trim()));
					}
					if (newcontenido.split("\n")[i].contains(ConstantsValue.TECHOS)) {				
						ubicacion.setTechos(fn.material(newcontenido.split("\n")[i].split(ConstantsValue.TECHOS)[1].split("Incendio:")[1].toUpperCase().replace("###", "").trim()));						
						ubicacion.setMuros(fn.material(newcontenido.split("\n")[i].split(ConstantsValue.MUROS)[1].split(ConstantsValue.TECHOS)[0].toUpperCase().replace("###", "").trim()));
					}
					
					if (newcontenido.split("\n")[i].contains(ConstantsValue.NIVELES)) {						
						if(newcontenido.split("\n")[i].split(ConstantsValue.NIVELES)[1].contains(fn.palabraRgx(newcontenido.split("\n")[i],ConstantsValue.NUMSOTANOS))) {
							ubicacion.setNiveles(Integer.parseInt(newcontenido.split("\n")[i].split(ConstantsValue.NIVELES)[1].split(fn.palabraRgx(newcontenido.split("\n")[i],ConstantsValue.NUMSOTANOS) )[0].replace("###", "").replace("\r", "").trim()));
						}else {
							ubicacion.setNiveles(Integer.parseInt(newcontenido.split("\n")[i].split(ConstantsValue.NIVELES)[1].split(fn.palabraRgx(newcontenido.split("\n")[i].split(ConstantsValue.NIVELES)[1], ConstantsValue.ENQUPISO))[0].replace("###", "").replace("\r", "").trim()));	
						}
						
					}
					if (newcontenido.split("\n")[i].contains(ConstantsValue.NUMPISOSINCENDIO)) {
						ubicacion.setNiveles(Integer.parseInt(newcontenido.split("\n")[i].split(ConstantsValue.INCENDIOMYMPT)[1].replace("###", "").replace("\r", "").trim()));
					}
					if (newcontenido.split("\n")[i].contains("Giro:")) {
						ubicacion.setGiro(newcontenido.split("\n")[i].split("Giro:")[1].replace("###", "").trim());
					}

				}
				ubicaciones.add(ubicacion);
				modelo.setUbicaciones(ubicaciones);
			
		}



			List<EstructuraCoberturasModel> coberturas = new ArrayList<>();
			// Cobertutas


			inicio = contenido.indexOf("Tipo Vivienda");
			if(inicio == -1) {
				inicio = contenido.indexOf("Muro de Contención:");
			}
			
			if(inicio == -1) {
				inicio = contenido.indexOf("Tipo Techo");
			}
			
			if(inicio == -1) {
				inicio = contenido.indexOf("Uso Camión:");
			}
			if(inicio == -1) {
				inicio = contenido.indexOf("Secciones amparadas");
			}
			if(inicio == -1) {
				inicio = contenido.indexOf("Coberturas");
			}		
			if(inicio == -1) {
				inicio = contenido.indexOf("Cobertura");
			}	
		    fin = contenido.indexOf("Prima Neta");
		
			String nombre = "";
			StringBuilder deducible = new StringBuilder();

			if (inicio > -1 && fin > -1 && inicio < fin) {
				newcontenido = contenido.substring(inicio, fin).replace("@@@", "").replace("###Prima", "").trim();
				boolean existeTituloCoaseguro = newcontenido.toUpperCase().contains("COASEGURO");
				int index = 0;
				StringBuilder coberturasNombreIncompleto = new StringBuilder();
				
				String[] arrNewContenido = newcontenido.split("\r\n");
			
				for (String x : arrNewContenido) {
					if (!x.contains("Tipo Vivienda") && !x.contains("Coberturas###Suma") && !x.contains("página#") && !x.contains("No. Sótanos") && !x.contains("Tipo Techo")
							&& !x.contains("Cobertura amparada") && !x.contains("No. Niveles:")) {
						x = completaTextoCoberturas(arrNewContenido,index,coberturasNombreIncompleto);
						resultado.append(x.trim()).append("\r\n");
					}
					index++;
				}
				
				if(arrNewContenido.length < 4 && newcontenido.contains("PERDIDAS O DAÑOS PARCIALES")) {
					arrNewContenido = resultado.toString().split("\n");
					String seccion = "";
					for (int i = 0; i < arrNewContenido.length; i++) {
						String[] valores = arrNewContenido[i].trim().split("###");
						if (valores.length > 1) {
							EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();
							if (fn.seccion(valores[0].trim()).length() > 0) {
								seccion = valores[0].trim();
								cobertura.setNombre(valores[1].trim());
							}else {
								cobertura.setNombre(valores[0].trim());
							}
							cobertura.setSeccion(seccion);
							cobertura.setSa(valores[2].trim());

							if(valores.length == 5) {
								cobertura.setDeducible(valores[3].trim());
								cobertura.setCoaseguro(valores[4].trim());
								coberturas.add(cobertura);
							}
							
						}
					}
					if(!coberturas.isEmpty()) {
						modelo.setCoberturas(coberturas);
						resultado = new StringBuilder();
					}
				}

				if (resultado.toString().split("\r\n").length > 1) {
					String seccion = "";
					StringBuilder sumaAsegurada;
					String coaseguro = "";
					String auxiliar = "";
					String[] arrResultado = resultado.toString().split("\r\n");
					List<String> nombresCoberturas = Arrays.asList("BIENES A LA INTEMPERIE EDIFICIO,".split(","));
					for (int i = 0; i < arrResultado.length; i++) {
						EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();
						String a = "";
						String b = "";
						String c = "";
						a = arrResultado[i];
						if (a.contains(ConstantsValue.SECCION)) {
							seccion = a;
						} else {
							if (i + 1 < arrResultado.length) {
								b = arrResultado[i + 1].trim();
							}
							if (i + 2 < arrResultado.length) {
								c = arrResultado[i + 2].trim();
							}

							if ((a.split("###").length == 3 || a.split("###").length == 4) && fn.castDouble(a.split("###")[0]) == null) {
								nombre = a.split("###")[0].trim();
												
								coaseguro = "";
								//Suma asegurada
								sumaAsegurada = new StringBuilder();
								sumaAsegurada.append(a.split("###")[1].trim());
								if(a.split("###")[1].equalsIgnoreCase("Sublimite de") && b.split("###").length>0) {
									auxiliar = b.split("###")[0];
									if(coberturasNombreIncompleto.toString().contains(auxiliar)) {
										auxiliar = b.split("###")[1];
										nombre = b.split("###")[0];
										b = b.replace(nombre+"###", "");
										arrResultado[i+1] = arrResultado[i+1].replace(nombre+"###", "");
									}
									sumaAsegurada.append(" ").append(auxiliar);
									b = b.replace(auxiliar+"###", "");
								}
								
								//Coaseguro
								if(existeTituloCoaseguro) {
									if(a.split("###").length == 4) {
										coaseguro = a.split("###")[3];
									}else if(b.split("###").length>1){
										coaseguro = b.split("###")[1];
									}
								}
							
								
								deducible.append(a.split("###")[2].trim());
								if (deducible.toString().contains("de la pérdida")
										|| deducible.toString().contains("del eq. dañado")
										|| deducible.toString().contains("sobre el monto")
										|| deducible.toString().contains("de reposición")
										|| deducible.toString().contains("de reposicion")
										|| deducible.toString().contains("suma asegurada")) {
									if ((b.split("###").length == 1 || b.split("###").length == 2)&& !b.contains(ConstantsValue.SECCION) && nombresCoberturas.indexOf(b.split("###")[0].trim().toUpperCase()) ==-1) {
										if( b.split("###").length == 2){
											deducible.append(" ").append(b.split("###")[0]);
										}else {
											deducible.append(" ").append(b);
										}

									}

									if (c.split("###").length == 1 && (b.split("###").length == 1 ||  b.split("###").length == 2)
											&& !c.contains(ConstantsValue.SECCION) && !coberturasNombreIncompleto.toString().contains(c)) {
										deducible.append(" ").append(c);
									}
									

								} else {
									cobertura.setDeducible(deducible.toString());
								}
								cobertura.setSeccion(seccion.replace("SECCION", "").trim());
								cobertura.setNombre(nombre);
								cobertura.setSa(sumaAsegurada.toString().trim());
								cobertura.setDeducible(deducible.toString().trim());
								cobertura.setCoaseguro(coaseguro);
								deducible = new StringBuilder();
								coberturas.add(cobertura);

							}else if(a.split("###").length == 2  && i+1<arrResultado.length && fn.numTx(arrResultado[i+1].split("###")[0]).length() >1 && a.trim().endsWith("Sublímite")
										&& nombresCoberturas.indexOf(a.split("###")[0].trim().toUpperCase()) > -1 ) {
								
									cobertura.setSeccion(seccion.replace("SECCION", "").trim());
									cobertura.setNombre(a.split("###")[0].trim());
									cobertura.setSa(a.split("###")[1]+" "+arrResultado[i+1].split("###")[0].trim());
									if(arrResultado[i+1].split("###").length>1) {
										cobertura.setDeducible(arrResultado[i+1].split("###")[1].contains("%") ? arrResultado[i+1].split("###")[1].trim():"");
									}
									coberturas.add(cobertura);
									deducible = new StringBuilder();
								
							}

						}
					}

				}
				modelo.setCoberturas(coberturas);
			}
      
			if(modelo.getCoberturas().isEmpty()){
				inicio = contenido.indexOf("Secciones###Suma asegurada ");
				fin = contenido.indexOf("Prestador de Servicios");
				newcontenido = contenido.substring(inicio, fin).replace("@@@", "").replace("\r", "").trim();
				for (int i = 0; i < newcontenido.split("\n").length; i++) {
					EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();
					if(!newcontenido.split("\n")[i].contains("horas cubiertas")
					&& !newcontenido.split("\n")[i].contains("Otros")
					&& !newcontenido.split("\n")[i].contains("CARÁTULA")
					&& !newcontenido.split("\n")[i].contains("POLIZA DE SEGURO")
					&& !newcontenido.split("\n")[i].contains("Civil")
				
					
					&& !newcontenido.split("\n")[i].contains("Suma asegurada") && ( newcontenido.split("\n")[i] .split("###").length ==1)){
						cobertura.setNombre(newcontenido.split("\n")[i].split("###")[0]);
						coberturas.add(cobertura);
					}

				}
             modelo.setCoberturas(coberturas);
          		 
			}

			List<EstructuraRecibosModel> recibosList = new ArrayList<>();
			EstructuraRecibosModel recibo = new EstructuraRecibosModel();

			if (modelo.getFormaPago() == 1 && recibosList.isEmpty()) {

				recibo.setReciboId("");
				recibo.setSerie("1/1");
				recibo.setVigenciaDe(modelo.getVigenciaDe());
				recibo.setVigenciaA(modelo.getVigenciaA());
				if (recibo.getVigenciaDe().length() > 0) {
					recibo.setVencimiento(fn.dateAdd(recibo.getVigenciaDe(), 30, 1));
				}
				recibo.setPrimaneta(modelo.getPrimaneta());
				recibo.setDerecho(modelo.getDerecho());
				recibo.setRecargo(modelo.getRecargo());
				recibo.setIva(modelo.getIva());
				recibo.setPrimaTotal(modelo.getPrimaTotal());
				recibo.setAjusteUno(modelo.getAjusteUno());
				recibo.setAjusteDos(modelo.getAjusteDos());
				recibo.setCargoExtra(modelo.getCargoExtra());
				recibosList.add(recibo);

			}

			modelo.setRecibos(recibosList);

			if(!modelo.getVigenciaDe().isEmpty() &&  fn.diferenciaDias(modelo.getVigenciaDe(), modelo.getVigenciaA()) < 30){
				modelo.setVigenciaA(fn.calcvigenciaA(modelo.getVigenciaDe(), 12));
			}

			return modelo;
		} catch (Exception ex) {				
			modelo.setError(
					ChubbDiversosModel.this.getClass().getTypeName() + " | " + ex.getMessage() + " | " + ex.getCause());
			return modelo;
		}
	}
	
	private void obtenerEndoso(String newContenido) {
		int inicio = newContenido.indexOf("Endoso:");
		int fin = newContenido.indexOf("Tipo de endoso");
		
		if(inicio>-1 && inicio<fin) {
			String text = newContenido.split("Endoso:")[1].split("Tipo de endoso")[0];
			modelo.setEndoso(text.replace("###","").replace("\n", "").trim());
		}
		
	}
	
	private String completaTextoCoberturas(String[] arrTexto,int i, StringBuilder coberturasNombreIncompleto) {
		String texto =  arrTexto[i];
		if(texto.contains("Según condición Fenómenos") && !texto.contains("Hidrometeorológicos")) {
			texto =texto.replace("Según condición Fenómenos", "Según condición Fenómenos Hidrometeorológicos");
		}else
		if(texto.contains("###") && (i-1)>-1) {
			if(texto.split("###")[0].trim().length() <2) {
				texto = completaTextoActualConLineaSiguiente(arrTexto, i, "PERDIDAS O DAÑOS PARCIALES", "AVERIA PART");
			}
			switch (texto.split("###")[0]) {
			case "sublimite":
				texto = completaTextoActualConLineaSuperior(arrTexto, i,coberturasNombreIncompleto, "sublimite","Robo de bienes y Valores de Empleados y Clientes");
				break;
			case "CONTEN":
				texto = completaTextoActualConLineaSuperior(arrTexto, i, coberturasNombreIncompleto, "CONTEN", "COBERTURA AMPLIA DE INCENDIO PARA");
				break;
			case "SUB":
				texto = completaTextoActualConLineaSuperior(arrTexto, i,coberturasNombreIncompleto, "SUB","REMOCION DE ESCOMBROS CONTENIDOS");
				break;
			case "MOVIL DENTRO Y FUERA DEL PREDIO":
				texto = completaTextoActualConLineaSuperior(arrTexto, i, coberturasNombreIncompleto, "MOVIL DENTRO Y FUERA DEL PREDIO", "ROBO CON VIOLENCIA Y-O ASALTO EQUIPO");
				break;
			case FIJO_DENTRO_DEL_PREDIO:
				if (texto.split("###").length == 4 && arrTexto[i - 1].contains(ROBO_Y_O_ASALTO) && arrTexto[i - 1].contains("###")) {
					String[] auxiliar = arrTexto[i - 1].split("###");
					if (auxiliar[auxiliar.length - 1].contains("del valor de reposición")) {
						String deducible = texto.split("###")[2];
						String inicioDeducible = auxiliar[auxiliar.length - 1].replace("\r", "");
						texto.split("###")[2] = texto.split("###")[2].concat(" del valor de reposición");
						texto = texto
								.replace(FIJO_DENTRO_DEL_PREDIO,
										"ROBO CON VIOLENCIA Y-O ASALTO DE EQUIPO FIJO DENTRO DEL PREDIO")
								.replace(deducible, inicioDeducible + " " + deducible);
					}

				}
				break;
			case ROBO_Y_O_ASALTO:
				if(i+1 < arrTexto.length &&  arrTexto[i+1].contains("###") && arrTexto[i+1].split("###")[0].equals(FIJO_DENTRO_DEL_PREDIO) && arrTexto[i+1].split("###").length == 3 && !texto.contains(FIJO_DENTRO_DEL_PREDIO)) {
					
						
							texto = texto.replace(ROBO_Y_O_ASALTO,"ROBO CON VIOLENCIA Y-O ASALTO DE EQUIPO FIJO DENTRO DEL PREDIO");
							arrTexto[i+1] = arrTexto[i+1].replace("FIJO DENTRO DEL PREDIO###","" );
						
						
					
				}
				break;
			case "R.C. ARRENDATARIO SUB", "R.C. TALLERES CRISTALES":
				//linea actual nombre,suma linea siguiente complemento de suma asegurada, deducible,coaseguro
				if(i+1 < arrTexto.length && texto.split("###").length == 2) {
					texto = texto.replace("\r","").replace("\n", "");
					if((texto.split("###")[1].equals("Sublimite de") || fn.numTx(texto.split("###")[1]).length() == 0) && arrTexto[i+1].contains("###")
					&& fn.isNumeric(arrTexto[i+1].split("###")[0].replace(",", ""))
					
					) {
						
							texto = texto.concat(" ").concat(arrTexto[i+1]).trim();
							arrTexto[i+1] = "";
						
					}
				}
				break;
			case "guerra":
				texto = completaTextoActualConLineaSuperior(arrTexto, i, coberturasNombreIncompleto, "guerra", "Apresamiento, secuestro o decomiso y actos de");
				break;
			case "labour":
				texto = completaTextoActualConLineaSuperior(arrTexto, i, coberturasNombreIncompleto, "labour", "Gastos de salvamento, remolque o auxilio, sue &");
				break;
			default:
				break;
			}

		}
		return texto;
	}
	
	private String completaTextoActualConLineaSuperior(String[] arrTexto,int i,StringBuilder coberturasNombreIncompleto, String textoActual, String textSuperiorAcompletar) {
		String texto = arrTexto[i];
		if (arrTexto[i - 1].contains(textSuperiorAcompletar) && !arrTexto[i].contains(textSuperiorAcompletar)) {
				texto = texto.replace(textoActual, textSuperiorAcompletar + " " + textoActual);
				coberturasNombreIncompleto.append(textSuperiorAcompletar + " " + textoActual).append(",");
		}

		return texto;
	}
	
	private String completaTextoActualConLineaSiguiente(String[] arrTexto, int i, String textoActual, String textoSiguiente) {
		String texto = arrTexto[i];
		if(!texto.contains(textoSiguiente) && arrTexto[i+1].contains(textoSiguiente)) {
			texto = texto.replace(textoActual, textoActual + " " + textoSiguiente);
			arrTexto[i+1] = arrTexto[i+1].replace(textoSiguiente, "").replace(textoSiguiente+"###", "");
		}
		return texto;
	}
}
