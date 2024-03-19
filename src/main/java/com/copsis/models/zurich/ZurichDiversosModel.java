package com.copsis.models.zurich;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.copsis.constants.ConstantsValue;
import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraCoberturasModel;
import com.copsis.models.EstructuraJsonModel;
import com.copsis.models.EstructuraUbicacionesModel;


public class ZurichDiversosModel {
	// Clases
	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
	// Varaibles
	private String contenido = "";

	

	
	public ZurichDiversosModel(String contenido) {
		this.contenido = contenido;
	}
	
	public EstructuraJsonModel procesar() {
		int inicio =0;
		int fin =0;
		boolean contratancte = true;
		boolean agente = true;
		StringBuilder newcont = new StringBuilder();
		contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());
		
		try {
			  modelo.setTipo(7);
			  modelo.setCia(44);
			
			inicio = contenido.indexOf(ConstantsValue.PRODUCTOMN);
			fin = contenido.indexOf(ConstantsValue.COBERTURAS_AMPARADAS2);

			if(inicio  > -1 && fin > -1 && inicio < fin) {
				newcont.append(contenido.substring(inicio,fin).replace("@@@", "").replace("\r", "").trim());
				for (int i = 0; i < newcont.toString().split("\n").length; i++) {                    
					if( newcont.toString().split("\n")[i].contains("Moneda")) {						
						modelo.setMoneda(fn.buscaMonedaEnTexto(newcont.toString().split("\n")[i]));
					}
					if( newcont.toString().split("\n")[i].contains(ConstantsValue.AGENTE2)) {	
						modelo.setAgente( newcont.toString().split("\n")[i].split("Agente###:")[1].trim());
						agente=false;
					}
					if( newcont.toString().split("\n")[i].contains(ConstantsValue.AGENTE2) && agente  ) {	
						modelo.setAgente( newcont.toString().split("\n")[i].split(ConstantsValue.AGENTE2)[1].replace("###", "").replace(":", "").trim());
						
					}
				
					if( newcont.toString().split("\n")[i].contains(ConstantsValue.POLIZA_ACENT) && newcont.toString().split("\n")[i].contains("Documento")) {
						modelo.setPoliza( newcont.toString().split("\n")[i].split(ConstantsValue.POLIZA_ACENT)[1].split("Documento")[0].replace("###", "").replace(":", "").trim());
					}
					if( newcont.toString().split("\n")[i].contains("Vigencia")&& newcont.toString().split("\n")[i].contains("Desde") && newcont.toString().split("\n")[i].contains("Fecha")) {
						modelo.setVigenciaDe(fn.formatDateMonthCadena( newcont.toString().split("\n")[i].split("Vigencia")[1].split("Desde")[1].split("Fecha")[0].replace("###", "").replace(":", "").replace("el", "").replace("de", "-").replace(" ", "").trim().toUpperCase()));
					}
					
					if( newcont.toString().split("\n")[i].contains("Fecha Emisión")) {
						String[] valores = newcont.toString().split("\n")[i].split("###");
						modelo.setFechaEmision(fn.formatDateMonthCadena(valores[valores.length-1].replace("de", "-").replace(" ", "").trim()));
					}
					if( newcont.toString().split("\n")[i].contains("Hasta")&& newcont.toString().split("\n")[i].contains("Folio") ) {
						modelo.setVigenciaA(fn.formatDateMonthCadena( newcont.toString().split("\n")[i].split("Hasta")[1].split("Folio")[0].replace("###", "").replace(":", "").replace("el", "").replace("de", "-").replace(" ", "").trim().toUpperCase()));
					}
					if( newcont.toString().split("\n")[i].contains("Asegura") && contratancte) {
						modelo.setCteNombre( newcont.toString().split("\n")[i+1]);
						contratancte=false;
						
					}
					if( newcont.toString().split("\n")[i].contains("R.F.C.") && (i+1) < newcont.toString().split("\n").length) {
						modelo.setRfc(newcont.toString().split("\n")[i].split("R.F.C.")[1].replace("###", "").replace("-", "").replace(" ", "").trim());
						String textoOtroRenglon = newcont.toString().split("\n")[i+1];
						String direccion = textoOtroRenglon.split("###")[ textoOtroRenglon.split("###").length -1];
						 
						if((i+2) < newcont.toString().split("\n").length) {
							String textoAux = newcont.toString().split("\n")[i+2];
							if(!textoAux.contains("Zurich")) {
								direccion += " "+ textoAux.split("###")[ textoAux.split("###").length -1].replace("\n", "").trim();
							}
						}
						
						modelo.setCteDireccion(direccion);
					}
					if( newcont.toString().split("\n")[i].contains(ConstantsValue.PRODUCTOMN)) {						
						modelo.setPlan(newcont.toString().split("\n")[i].split(ConstantsValue.PRODUCTOMN)[1].replace("###", "").replace(":", "").trim());
					}	
					
				}
			}
			inicio = contenido.indexOf(ConstantsValue.PRODUCTOMN);
			fin = contenido.indexOf(ConstantsValue.COBERTURAS_AMPARADAS2);

			if(inicio  > -1 && fin > -1 && inicio < fin) {
				newcont = new StringBuilder();
				newcont.append(contenido.substring(inicio,fin).replace("@@@", "").replace("\r", "").trim());
				List<EstructuraUbicacionesModel> ubicaciones = new ArrayList<>();
				 EstructuraUbicacionesModel ubicacion = new EstructuraUbicacionesModel();
				for (int i = 0; i < newcont.toString().split("\n").length; i++) {	
		
					if(newcont.toString().split("\n")[i].contains("Ubicación del riesgo:")) {
						ubicacion.setNombre(newcont.toString().split("\n")[i+1]);
					}

					 if(newcont.toString().split("\n")[i].contains("Colonia")) {
						
						 ubicacion.setColonia(newcont.toString().split("\n")[i]);
					 }
					 if(newcont.toString().split("\n")[i].contains("Pisos") && newcont.toString().split("\n")[i].contains("Estructura")) {
						ubicacion.setNiveles(fn.castInteger(newcont.toString().split("\n")[i].split("Pisos")[1].split("Estructura")[0].replace(":", "").replace("###", "").trim())); 
					 }
					 if(newcont.toString().split("\n")[i].contains("Muros") && newcont.toString().split("\n")[i].contains("Entrepiso")) {
						 ubicacion.setMuros(fn.material(newcont.toString().split("\n")[i].split("Muros")[1].split("Entrepiso")[0].replace(":", "").replace("###", "").trim().toLowerCase()));
					 }
					 if(newcont.toString().split("\n")[i].contains("Uso") && newcont.toString().split("\n")[i].contains("Zona Alfa")) {
						 ubicacion.setGiro(newcont.toString().split("\n")[i].split("Uso")[1].split("Zona")[0].replace(":", "").replace("###", "").trim());
					 }
					 if(newcont.toString().split("\n")[i].contains("CP")){
					  List<String> valores = fn.obtenerListNumeros2("CP");
						if(!valores.isEmpty()){
							ubicacion.setCp(valores.stream()
								.filter(numero -> String.valueOf(numero).length() >= 4)
								.collect(Collectors.toList()).get(0));
						}
						}
					 
				}
				ubicaciones.add(ubicacion);
				modelo.setUbicaciones(ubicaciones);
			}
			

			inicio = contenido.indexOf(ConstantsValue.COBERTURAS_AMPARADAS2);
			fin = contenido.indexOf(ConstantsValue.PRIMA_NETA2);

			if(inicio  > -1 && fin > -1 && inicio < fin) {
				newcont = new StringBuilder();
				newcont.append(contenido.substring(inicio,fin).replace("@@@", "").replace("\r", "").trim());
				 List<EstructuraCoberturasModel> coberturas = new ArrayList<>();
				for (int i = 0; i < newcont.toString().split("\n").length; i++) {
					EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();
					if(!newcont.toString().split("\n")[i].contains("Coberturas") && (newcont.toString().split("\n")[i].split("###").length == 3 || newcont.toString().split("\n")[i].split("###").length == 2)) {
							cobertura.setNombre(newcont.toString().split("\n")[i].split("###")[0]);
							cobertura.setSa(newcont.toString().split("\n")[i].split("###")[1]);
							coberturas.add(cobertura);
						
						
						
					}
					
				}
				modelo.setCoberturas(coberturas);
			}
			
			if(modelo.getCoberturas().isEmpty()) {
				inicio = contenido.indexOf(ConstantsValue.COBERTURAS_AMPARADAS2);
				fin = contenido.indexOf("Si el contenido de");
				if(inicio > -1 && inicio < fin) {
					newcont = new StringBuilder();
					newcont.append(contenido.substring(inicio,fin).replace("@@@", "").replace("\r", "").trim());
					List<EstructuraCoberturasModel> coberturas = new ArrayList<>();
					for (int i = 0; i < newcont.toString().split("\n").length; i++) {
						EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();
						if(!newcont.toString().split("\n")[i].contains("Coberturas") &&
						   !newcont.toString().split("\n")[i].contains(ConstantsValue.PRIMA_NETA2) &&
						   !newcont.toString().split("\n")[i].contains("Gastos de expedición") &&
						   !newcont.toString().split("\n")[i].contains("I. V. A") &&
						   !newcont.toString().split("\n")[i].contains("C. I:") && (newcont.toString().split("\n")[i].split("###").length == 3 || newcont.toString().split("\n")[i].split("###").length == 2)) {
								cobertura.setNombre(newcont.toString().split("\n")[i].split("###")[0].trim());
								cobertura.setSa(newcont.toString().split("\n")[i].split("###")[1].trim());
								coberturas.add(cobertura);
							
							
						}
						
					}
					modelo.setCoberturas(coberturas);
				}
				
			}
	
			
			inicio = contenido.indexOf(ConstantsValue.PRIMA_NETA2);
			fin = contenido.indexOf("Si el contenido");

			if(inicio  > -1 && fin > -1 && inicio < fin) {
				newcont = new StringBuilder();
				newcont.append(contenido.substring(inicio,fin).replace("@@@", "").replace("\r", "").trim());
				for (int i = 0; i < newcont.toString().split("\n").length; i++) {
					if(newcont.toString().split("\n")[i].contains(ConstantsValue.PRIMA_NETA2) && newcont.toString().split("\n")[i].split(ConstantsValue.PRIMA_NETA2).length == 2) {
						modelo.setPrimaneta( fn.castBigDecimal(fn.castDouble(newcont.toString().split("\n")[i].split("neta")[1].replace("###", "").trim())));
					 }
					if(newcont.toString().split("\n")[i].contains(ConstantsValue.EXPEDICION)) {
						modelo.setDerecho( fn.castBigDecimal(fn.castDouble(newcont.toString().split("\n")[i].split(ConstantsValue.EXPEDICION)[1].replace("###", "").trim())));
					 }
					if(newcont.toString().split("\n")[i].contains("I. V. A.")) {
						modelo.setIva( fn.castBigDecimal(fn.castDouble(newcont.toString().split("\n")[i].split("I. V. A.")[1].replace("###", "").trim())));
					 }
					if(newcont.toString().split("\n")[i].contains("Prima total")) {
						modelo.setPrimaTotal( fn.castBigDecimal(fn.castDouble(newcont.toString().split("\n")[i].split("total")[1].replace("###", "").trim())));
					 }
					
				}
			}

			inicio =  contenido.indexOf("Forma de pago:");
			if(inicio > -1) {
				modelo.setFormaPago(fn.formaPagoSring(contenido.split("Forma de pago:")[1]));
			}
			if(modelo.getFormaPago() == 1 && fn.diferencia(modelo.getVigenciaDe(),modelo.getVigenciaA()) == 0){
				modelo.setVigenciaA(fn.calcvigenciaA(modelo.getVigenciaDe(), 12));
			  }
			
			inicio = contenido.indexOf("C.P.");
			if(modelo.getCp().length() == 0 && inicio > 0) {			
			 modelo.setCp(contenido.split("C.P.")[1].trim().substring(0, 5));
						
			}
			
			return modelo;
		} catch (Exception e) {
			modelo.setError(ZurichDiversosModel.this.getClass().getTypeName() + " - catch:" + e.getMessage() + " | "
					+ e.getCause());
			 return modelo;
		}
	}
	

}
