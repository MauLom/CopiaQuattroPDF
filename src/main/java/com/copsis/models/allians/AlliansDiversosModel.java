package com.copsis.models.allians;

import java.util.ArrayList;
import java.util.List;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraCoberturasModel;
import com.copsis.models.EstructuraJsonModel;
import com.copsis.models.EstructuraUbicacionesModel;

public class AlliansDiversosModel {
	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
	private String contenido = "";
	
	
	public AlliansDiversosModel(String contenido) {
		this.contenido = contenido;
	}
	public  EstructuraJsonModel procesar() {
		StringBuilder newcontenido = new StringBuilder();
		int inicio = 0;
		int fin = 0;
		
		contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());
		try {
			modelo.setTipo(7);		
			modelo.setCia(4);

			inicio = contenido.indexOf("PÓLIZA DE SEGURO");
			fin = contenido.indexOf("Coberturas Aseguradas");
			newcontenido.append( fn.extracted(inicio, fin, contenido));
			List<EstructuraUbicacionesModel> ubicaciones = new ArrayList<>();
			EstructuraUbicacionesModel ubicacion = new EstructuraUbicacionesModel();
			
			for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
			
				if(newcontenido.toString().split("\n")[i].contains("NOMBRE") && newcontenido.toString().split("\n")[i].contains("DIRECCIÓN FISCAL")
						&& newcontenido.toString().split("\n")[i+1].contains("ASEGURADO")	&&  newcontenido.toString().split("\n")[i+3].length() >20	) {
				modelo.setCteNombre(newcontenido.toString().split("\n")[i+3].split("###")[0].replace("###", "").trim());
				modelo.setCteDireccion(newcontenido.toString().split("\n")[i+5].split("###")[0].replace("###", "").trim()  +" "+newcontenido.toString().split("\n")[i+6].split("###")[0].replace("###", "").trim());
				}
				if(newcontenido.toString().split("\n")[i].contains("C.P.")) {
					modelo.setCp(newcontenido.toString().split("\n")[i].split("C.P.")[1].trim().substring(0,5));	
				}
				
				if(newcontenido.toString().split("\n")[i].contains("FECHA DE EMISIÓN") && newcontenido.toString().split("\n")[i].contains("MONEDA")
				 && newcontenido.toString().split("\n")[i].contains("HASTA") && newcontenido.toString().split("\n")[i].contains("DESDE") &&
				 fn.obtenVigePoliza(newcontenido.toString().split("\n")[i+1]).size() == 3) {
					modelo.setPoliza(newcontenido.toString().split("\n")[i+1].split("###")[0]);
                 modelo.setVigenciaDe(fn.formatDateMonthCadena(fn.obtenVigePoliza(newcontenido.toString().split("\n")[i+1]).get(0)));
                 modelo.setVigenciaA(fn.formatDateMonthCadena(fn.obtenVigePoliza(newcontenido.toString().split("\n")[i+1]).get(1)));                
                 modelo.setFechaEmision(fn.formatDateMonthCadena(fn.obtenVigePoliza(newcontenido.toString().split("\n")[i+1]).get(2)));
                 modelo.setMoneda( fn.buscaMonedaEnTexto(newcontenido.toString().split("\n")[i+1]));
				}
				if(newcontenido.toString().split("\n")[i].contains("Ubicación")) {
					ubicacion.setNombre(modelo.getCteNombre());
					ubicacion.setCalle(modelo.getCteDireccion());
					ubicacion.setCp(modelo.getCp());
					
				}
				if(newcontenido.toString().split("\n")[i].contains("Giro") && newcontenido.toString().split("\n")[i].contains("Número de Pisos:") ) {
					ubicacion.setGiro(newcontenido.toString().split("\n")[i].split("Giro:")[1].split("Número")[0].replace("###", "").trim());
					ubicacion.setNiveles(fn.castInteger(newcontenido.toString().split("\n")[i].split("Número de Pisos:")[1].replace("###", "").trim()));
				}
				
			}
			ubicaciones.add(ubicacion);
			modelo.setUbicaciones(ubicaciones);
			
			

			
			inicio = contenido.indexOf("Coberturas Aseguradas");
			fin = contenido.indexOf("No. AGENTE");	
			newcontenido = new StringBuilder();
			newcontenido.append( fn.extracted(inicio, fin, contenido));
			List<EstructuraCoberturasModel> coberturas = new ArrayList<>();
			for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
				EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();
				if(!newcontenido.toString().split("\n")[i].contains("Coberturas Aseguradas") 
					&& !newcontenido.toString().split("\n")[i].contains("BLVD. MANUEL AVILA")
					&& !newcontenido.toString().split("\n")[i].contains("com.mx")
					&& !newcontenido.toString().split("\n")[i].contains("Deducibles")
					&& !newcontenido.toString().split("\n")[i].contains("Coaseguros")) {			                                                                                         
					if(newcontenido.toString().split("\n")[i].split("###").length == 3) {						
						cobertura.setNombre(newcontenido.toString().split("\n")[i].split("###")[0]);
						cobertura.setSa(newcontenido.toString().split("\n")[i].split("###")[2]);						
						coberturas.add(cobertura);
					}
					
				}
			}
			modelo.setCoberturas(coberturas);
			
			inicio = contenido.indexOf("No. AGENTE");
			fin = contenido.indexOf("En caso de que esta");
				
			newcontenido = new StringBuilder();
			newcontenido.append( fn.extracted(inicio, fin, contenido));
			for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
				if(newcontenido.toString().split("\n")[i].contains("No. AGENTE") && newcontenido.toString().split("\n")[i].contains("AGENTE")) {
					modelo.setCveAgente(newcontenido.toString().split("\n")[i].split("No. AGENTE")[1].split("AGENTE")[0].replace("###", ""));
					modelo.setAgente(newcontenido.toString().split("\n")[i].split("No. AGENTE")[1].split("AGENTE")[1].replace("###", ""));
				}
				if(newcontenido.toString().split("\n")[i].contains("PRIMA NETA") && newcontenido.toString().split("\n")[i].contains("RECARGO") && newcontenido.toString().split("\n")[i].contains("DERECHO")
						&& newcontenido.toString().split("\n")[i].contains("I.V.A.")) {
					modelo.setPrimaneta(fn.castBigDecimal(fn.cleanString(
							newcontenido.toString().split("\n")[i+1].split("###")[0].trim())));
					modelo.setRecargo(fn.castBigDecimal(fn.cleanString(
							newcontenido.toString().split("\n")[i+1].split("###")[1].trim())));														
					modelo.setDerecho(fn.castBigDecimal(fn.cleanString(
							newcontenido.toString().split("\n")[i+1].split("###")[2].trim())));												
					modelo.setIva(fn.castBigDecimal(fn.cleanString(
							newcontenido.toString().split("\n")[i+1].split("###")[3].trim())));							
					modelo.setPrimaTotal(fn.castBigDecimal(fn.cleanString(
							newcontenido.toString().split("\n")[i+1].split("###")[4].trim())));
				}
				if(newcontenido.toString().split("\n")[i].contains("FORMA DE PAGO")) {
					modelo.setFormaPago(fn.formaPagoSring(newcontenido.toString().split("\n")[i].replace("###", "")));
				}
			}
			
System.out.println(contenido);
			
			
			return modelo;
		} catch (Exception ex) {
			
			modelo.setError(AlliansDiversosModel.this.getClass().getTypeName() + " | " + ex.getMessage() + " | "
					+ ex.getCause());
			return modelo;
		}
		
	}
	
}
