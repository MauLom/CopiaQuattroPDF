package com.copsis.models.bexmas;

import java.util.ArrayList;
import java.util.List;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraCoberturasModel;
import com.copsis.models.EstructuraJsonModel;


public class BexmasAutosModel {

	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();

	private String contenido = "";

	public BexmasAutosModel(String contenido) {
		this.contenido = contenido;
	}

	public EstructuraJsonModel procesar() {
		int inicio = 0;
		int fin = 0;
		StringBuilder newcont = new StringBuilder();
		String contenidocbo="";
		contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales())
		.replace("Seguros ###Ve ###Por ###Mas", "Seguros Ve por Más")
		.replace("Prima Neta", "Prima neta");

		try {
			modelo.setTipo(1);
			modelo.setCia(98);

			String[] palabras = {"AUTOMÓVILES Y CAMIONES INDIVIDUAL"};

			modelo.setPlan(!plan(palabras).isEmpty() ?plan(palabras):"");


			inicio = contenido.indexOf("AUTOMÓVILES Y CAMIONES");
			fin = contenido.indexOf("Seguros Ve por Más");

			if (inicio > -1 && fin > -1 && inicio < fin) {
				newcont.append(contenido.substring(inicio, fin).replace("@@@", "").replace("\r", ""));
				
				for (int i = 0; i < newcont.toString().split("\n").length; i++) {									
					if (newcont.toString().split("\n")[i].contains("Póliza")
							&& newcont.toString().split("\n")[i].contains("Endoso")
							&& newcont.toString().split("\n")[i + 1].contains("ASEGURADO")) {
						modelo.setPoliza(newcont.toString().split("\n")[i + 2].split("###")[0]);
					}
					
					if(modelo.getPoliza().length() == 0 && newcont.toString().split("\n")[i].contains("Póliza")
							&& newcont.toString().split("\n")[i].contains("Endoso")
							&& newcont.toString().split("\n")[i].contains("ASEGURADO")) {
						
						modelo.setCteNombre(newcont.toString().split("\n")[i+1].split("###")[1]);
						modelo.setPoliza(newcont.toString().split("\n")[i+1].split("###")[2]);
					}
					
			
					if (newcont.toString().split("\n")[i].contains("Nombre:")
							&& newcont.toString().split("\n")[i].contains("Agente")
							&& newcont.toString().split("\n")[i].contains("Moneda")) {
						modelo.setCteNombre(newcont.toString().split("\n")[i].split("Nombre:")[1].split("Agente")[0]
								.replace("###", "").trim());
						modelo.setMoneda(fn.buscaMonedaEnTexto(newcont.toString().split("\n")[i + 1]));						
						if(newcont.toString().split("\n")[i + 1].split("###").length == 4){
							List<String> valores = fn.obtenerListNumeros2(newcont.toString().split("\n")[i+1].split("###")[1]);
							if(!valores.isEmpty()){
								modelo.setCveAgente(valores.get(0));
							}
							
						}
						
					}
					
					if(modelo.getMoneda() ==0 && newcont.toString().split("\n")[i].contains("Moneda")) {
						modelo.setMoneda(fn.buscaMonedaEnTexto(newcont.toString().split("\n")[i + 1]));
					}
				
				
					
					if (newcont.toString().split("\n")[i].contains("Dirección:")) {
					
						getCtedirecciones(newcont, i);		
					}
				
					if (newcont.toString().split("\n")[i].contains("Formas de Pago")) {
						modelo.setFormaPago(fn.formaPagoSring(newcont.toString().split("\n")[i + 1]));
					}
					
					if (modelo.getFormaPago() == 0 && newcont.toString().split("\n")[i].contains("Formas de Pago")) {
						modelo.setFormaPago(fn.formaPagoSring(newcont.toString().split("\n")[i + 2]));
					}
					if (newcont.toString().split("\n")[i].contains("C.P:") && newcont.toString().split("\n")[i].split("C.P:")[1].split("###")[0].trim().length() > 3) {

						modelo.setCp(newcont.toString().split("\n")[i].split("C.P:")[1].split("###")[0].trim());
					}
					if (newcont.toString().split("\n")[i].contains("C.P:") && newcont.toString().split("\n")[i].split("C.P:")[1].split("###")[1].trim().length() > 3) {

						modelo.setCp(newcont.toString().split("\n")[i].split("C.P:")[1].split("###")[1].trim());
					}
					
					if(modelo.getCp().equalsIgnoreCase("Desde")) {
						modelo.setCp(newcont.toString().split("\n")[i].split("C.P:")[1].trim().substring(0,5));
					}

					if (newcont.toString().split("\n")[i].contains("R.F.C:") && newcont.toString().split("\n")[i].split("R.F.C:")[1].split("###")[0].trim().length() > 0
						&&  newcont.toString().split("\n")[i].split("R.F.C:")[1].split("###").length > 1	
							) {
						modelo.setRfc(newcont.toString().split("\n")[i].split("R.F.C:")[1].split("###")[0].trim());
					}
					
					if (newcont.toString().split("\n")[i].contains("R.F.C:")
						&&  newcont.toString().split("\n")[i].split("R.F.C:")[1].split("###").length == 1	) {
						modelo.setRfc(newcont.toString().split("\n")[i].split("R.F.C:")[1].replace("-", "").trim());
					}
					
					
					if (newcont.toString().split("\n")[i].split("-").length > 3 && newcont.toString().split("\n")[i].contains("12:00 Horas")) {
					
						List<String> valores = fn.obtenVigePoliza(newcont.toString().split("\n")[i]);
						if(!valores.isEmpty() & valores.size() > 2){
							modelo.setVigenciaDe(fn.formatDateMonthCadena(valores.get(0)));
							modelo.setVigenciaA(fn.formatDateMonthCadena(valores.get(1)));
							modelo.setFechaEmision(modelo.getVigenciaDe());
						}
			
						if(modelo.getVigenciaDe().isEmpty() && modelo.getVigenciaA().isEmpty()){
							modelo.setVigenciaDe(fn.formatDate(fn.formatDateMonthCadena(
								newcont.toString().split("\n")[i].split("###")[0].replace("12:00 Horas", "").trim())));
						modelo.setVigenciaA(fn.formatDate(fn.formatDateMonthCadena(
								newcont.toString().split("\n")[i].split("###")[1].replace("12:00 Horas", "").trim())));
						if (modelo.getVigenciaDe().length() > 0) {
							modelo.setFechaEmision(modelo.getVigenciaDe());
					   	}
					  }						
					}
					if (newcont.toString().split("\n")[i].split("-").length > 3 && newcont.toString().split("\n")[i].contains("12 Hrs.") && fn.obtenVigePoliza(newcont.toString().split("\n")[i]).size() ==2) {
					
						modelo.setVigenciaDe(fn.formatDateMonthCadena(fn.obtenVigePoliza(newcont.toString().split("\n")[i]).get(0)));
						modelo.setVigenciaA(fn.formatDateMonthCadena(fn.obtenVigePoliza(newcont.toString().split("\n")[i]).get(1)));
						
						if (modelo.getVigenciaDe().length() > 0) {
							modelo.setFechaEmision(modelo.getVigenciaDe());
						}
					}
				}
			}

			inicio = contenido.indexOf("DESCRIPCIÓN DEL VEHÍCULO");
			fin = contenido.indexOf("DESGLOSE DE COBERTURAS");
			if (inicio > -1 && fin > -1 && inicio < fin) {
				newcont = new StringBuilder();
				newcont.append(contenido.substring(inicio, fin).replace("@@@", "").replace("\r", ""));
				for (int i = 0; i < newcont.toString().split("\n").length; i++) {

					if (newcont.toString().split("\n")[i].contains("Clave")
							&& newcont.toString().split("\n")[i].contains("Marca")
							&& newcont.toString().split("\n")[i].contains("Zona")) {
						if (newcont.toString().split("\n")[i].split("Marca:")[1].split("Zona")[0].trim()
								.replace("###", "").length() > 0) {
							modelo.setMarca(newcont.toString().split("\n")[i].split("Marca:")[1].split("Zona")[0].trim()
									.replace("###", "").split(" ")[1]);
							modelo.setClave(newcont.toString().split("\n")[i].split("Marca:")[1].split("Zona")[0].trim()
									.replace("###", "").split(" ")[0]);
						}
					}
					if (newcont.toString().split("\n")[i].contains("Descripción:")) {
						modelo.setDescripcion(
								newcont.toString().split("\n")[i].split("Descripción:")[1].replace("###", "").trim());
					}
					if (newcont.toString().split("\n")[i].contains("Placa:") && newcont.toString().split("\n")[i].contains("NCI:")) {
						modelo.setPlacas(
								newcont.toString().split("\n")[i].split("Placa:")[1].split("NCI:")[0].replace("*", "").replace("###", "").trim());
					}
					
					if (newcont.toString().split("\n")[i].contains("SERIE:") && newcont.toString().split("\n")[i].contains("Motor")) {
						modelo.setSerie(newcont.toString().split("\n")[i].split("SERIE:")[1].split("Motor")[0].replace("*", "").replace("###", "").trim());
					}
				}
			}
			


			inicio = contenido.indexOf("Prima neta:");
			fin = contenido.indexOf("En testimonio de lo cual la institución");
			
			if(inicio == -1 && fin ==-1) {
				inicio = contenido.indexOf("Prima neta");
				fin = contenido.lastIndexOf("En testimonio de lo cual");
			}

			if (inicio > -1 && fin > -1 && inicio < fin) {

				newcont = new StringBuilder();
				newcont.append(contenido.substring(inicio, fin).replace("@@@", "").replace("\r", ""));
				for (int i = 0; i < newcont.toString().split("\n").length; i++) {
					
					if(newcont.toString().split("\n")[i].contains("Prima neta:") || newcont.toString().split("\n")[i].contains("Prima neta")) {						
						List<String> valores = fn.obtenerListNumeros(newcont.toString().split("\n")[i]);				
						if(!valores.isEmpty()){
						modelo.setPrimaneta(fn.castBigDecimal(fn.castDouble(valores.get(0))));
						}
					}
				
					if(newcont.toString().split("\n")[i].contains("Recargos:") || newcont.toString().split("\n")[i].contains("Recargos")) {					
						List<String> valores = fn.obtenerListNumeros(newcont.toString().split("\n")[i]);				
						if(!valores.isEmpty()){
						modelo.setRecargo(fn.castBigDecimal(fn.castDouble(valores.get(0))));
						}
					}
				
					if(newcont.toString().split("\n")[i].contains("Derechos:") || newcont.toString().split("\n")[i].contains("Derechos")){
						List<String> valores = fn.obtenerListNumeros(newcont.toString().split("\n")[i]);
						if(!valores.isEmpty()){
							modelo.setDerecho(fn.castBigDecimal(fn.castDouble(valores.get(0))));					
						}				
							
					}
					if(modelo.getDerecho().intValue() == 0 && newcont.toString().split("\n")[i].contains("daños a terceros")) {
						List<String> valores = fn.obtenerListNumeros(newcont.toString().split("\n")[i]);
						if(!valores.isEmpty()){
							modelo.setDerecho(fn.castBigDecimal(fn.castDouble(valores.get(0))));					
						}				
							
					}
				
					
					if(newcont.toString().split("\n")[i].contains("I.V.A:")){		
						modelo.setIva(fn.castBigDecimal(fn.castDouble(newcont.toString().split("\n")[i].split("I.V.A:")[1].replace("###", "").trim())));
					}
					
					if(newcont.toString().split("\n")[i].contains("I.V.A.")&& modelo.getIva().intValue() == 0){
						modelo.setIva(fn.castBigDecimal(fn.castDouble(newcont.toString().split("\n")[i].split("I.V.A.")[1].replace("###", "").trim())));
					}
					
					if(newcont.toString().split("\n")[i].contains("Prima total:")){		
						modelo.setPrimaTotal(fn.castBigDecimal(fn.castDouble(newcont.toString().split("\n")[i].split("Prima total:")[1].replace("###", "").trim())));
					}else if(newcont.toString().split("\n")[i].contains("Prima total")){		
						modelo.setPrimaTotal(fn.castBigDecimal(fn.castDouble(newcont.toString().split("\n")[i].split("Prima total")[1].replace("###", "").trim())));
					}
					
					if(newcont.toString().split("\n")[i].contains("1er Recibo")){		
						List<String> valores = fn.obtenerListNumeros(newcont.toString().split("\n")[i]);
						if(!valores.isEmpty()){
							modelo.setPrimerPrimatotal(fn.castBigDecimal(fn.castDouble(valores.get(0))));					
						}						
					}
					if(newcont.toString().split("\n")[i].contains("Subsecuentes:")){		
						modelo.setSubPrimatotal(fn.castBigDecimal(fn.castDouble(newcont.toString().split("\n")[i].split("Subsecuentes:")[1].replace("###", "").trim())));
					}

					if(modelo.getPrimaTotal().intValue() == 0 && newcont.toString().split("\n")[i].contains("Prima Total:")) {
						List<String> valores = fn.obtenerListNumeros(newcont.toString().split("\n")[i]);
						if(!valores.isEmpty()){
							modelo.setPrimaTotal(fn.castBigDecimal(fn.castDouble(valores.get(0))));					
						}				
					}
				}
			}
			
			int conpos =0;
			for(int i = 0; i < contenido.split("DESGLOSE DE COBERTURAS").length;i++) {
				
				if( i!=0) {
					conpos++;
					contenidocbo +=  contenido.split("DESGLOSE DE COBERTURAS")[i].split("Prima neta:")[0];
				}
			} 
			
	
			
		

			inicio = contenido.indexOf("DESGLOSE DE COBERTURAS");
			fin = contenido.indexOf("Prima neta:");
			if(fin == -1) {
				fin = contenido.indexOf("En testimonio de lo cual la Institución firma");
			}
			
			if (inicio > -1 && fin > -1 && inicio < fin) {
				List<EstructuraCoberturasModel> coberturas = new ArrayList<>();
				newcont = new StringBuilder();
				newcont.append(contenido.substring(inicio, fin).replace("@@@", "").replace("\r", ""));
				if(conpos >1) {
					newcont = new StringBuilder();
					newcont.append(contenidocbo.replace("@@@", "").replace("\r", ""));
				}
				
				for (int i = 0; i < newcont.toString().split("\n").length; i++) {
					EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();
					if (!newcont.toString().split("\n")[i].contains("COBERTURAS")
							&& !newcont.toString().split("\n")[i].contains("Responsabilidad")
							&& !newcont.toString().split("\n")[i].contains("Observaciones")
							&& !newcont.toString().split("\n")[i].contains("Coberturas Amparada")) {
						switch (newcont.toString().split("\n")[i].split("###").length) {
						case 2:
							cobertura.setNombre(newcont.toString().split("\n")[i].split("###")[0]);
							cobertura.setSa(newcont.toString().split("\n")[i].split("###")[1]);						
							coberturas.add(cobertura);
							break;
						case 3:case 4:
							cobertura.setNombre(newcont.toString().split("\n")[i].split("###")[0]);
							cobertura.setSa(newcont.toString().split("\n")[i].split("###")[1]);
							if(newcont.toString().split("\n")[i].split("###")[2].contains("%")) {
								cobertura.setDeducible(newcont.toString().split("\n")[i].split("###")[2]);
							}			
							coberturas.add(cobertura);
							
							break;						
						default:
							break;
						}
					}
				}
				modelo.setCoberturas(coberturas);
			}
			return modelo;
		} catch (Exception ex) {	
			modelo.setError(BexmasAutosModel.this.getClass().getTypeName() + " - catch:" + ex.getMessage() + " | " + ex.getCause());
			return modelo;
		}

	}

	private  String  plan (String[] palabras) {
		String palabraEcontrada="";
		for (String palabra : palabras) {
			if (contenido.contains(palabra)){
				 palabraEcontrada = palabra;
			} 
		}

		return palabraEcontrada;
	}

	private void getCtedirecciones(StringBuilder newcont, int i) {
	
		if (newcont.toString().split("\n")[i].split("Dirección:")[1].split("###").length > 15) {
			modelo.setCteDireccion(
					newcont.toString().split("\n")[i].split("Dirección:")[1].split("###")[1].trim());
		} else {
			modelo.setCteDireccion(
					newcont.toString().split("\n")[i].split("Dirección:")[1].split("###")[0].trim());
		}

		if (modelo.getCteDireccion().isEmpty()) {
			modelo.setCteDireccion(newcont.toString().split("\n")[i + 1].trim());
		}
	}

}
