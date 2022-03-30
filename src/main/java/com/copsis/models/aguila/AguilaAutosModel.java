package com.copsis.models.aguila;

import java.util.ArrayList;
import java.util.List;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraCoberturasModel;
import com.copsis.models.EstructuraJsonModel;

public class AguilaAutosModel {
	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
	private String contenido = "";
	
	public AguilaAutosModel(String contenido) {
		this.contenido = contenido;
	}
	
	public EstructuraJsonModel procesar() {
		try {
			int inicio = 0;
			int fin = 0;
			StringBuilder newcontenido = new StringBuilder();
			contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());
			 modelo.setTipo(1);
			 modelo.setCia(13);
			
			 
			 inicio = contenido.indexOf("DATOS DEL CONTRATANTE");
			 fin = contenido.indexOf("PRIMA SEGÚN VIGENCIA");
			 
			 newcontenido.append( fn.extracted(inicio, fin, contenido));
			 
			 for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
				 
				 if(newcontenido.toString().split("\n")[i].split("-").length  > 3 && fn.obtenVigePoliza(newcontenido.toString().split("\n")[i]).size() == 3) {					
					 modelo.setFechaEmision(fn.formatDateMonthCadena(fn.obtenVigePoliza(newcontenido.toString().split("\n")[i]).get(0)));
					 modelo.setVigenciaDe(fn.formatDateMonthCadena(fn.obtenVigePoliza(newcontenido.toString().split("\n")[i]).get(1)));
					 modelo.setVigenciaA(fn.formatDateMonthCadena(fn.obtenVigePoliza(newcontenido.toString().split("\n")[i]).get(2)));
					 modelo.setCteNombre(newcontenido.toString().split("\n")[i].split("###")[0].trim());
					 modelo.setPoliza(newcontenido.toString().split("\n")[i].split("###")[5].trim());
					 
				 }
				 if(newcontenido.toString().split("\n")[i].contains("Plan Contratado") && newcontenido.toString().split("\n")[i].contains("Forma de Pago") && newcontenido.toString().split("\n")[i].contains("Agente")) {
					 modelo.setPlan(newcontenido.toString().split("\n")[i+1].split("###")[0]);
					 modelo.setFormaPago(fn.formaPagoSring(newcontenido.toString().split("\n")[i+1]));
					 modelo.setCveAgente(newcontenido.toString().split("\n")[i+1].split("###")[newcontenido.toString().split("\n")[i+1].split("###").length-1].trim());
				 }
				 if(newcontenido.toString().split("\n")[i].contains("Calle") && newcontenido.toString().split("\n")[i].contains("Colonia")) {
					 modelo.setCteDireccion(newcontenido.toString().split("\n")[i+1].replace("###", " ").trim());
				 }
				 if(newcontenido.toString().split("\n")[i].contains("CP")) {
					 modelo.setCp(newcontenido.toString().split("\n")[i+1].split("###")[1]);
				 }
			 }
			 
			 

			 
			 inicio = contenido.indexOf("PRIMA SEGÚN VIGENCIA");
			 fin = contenido.indexOf("El ###Águila");
			 newcontenido = new StringBuilder();
			 newcontenido.append( fn.extracted(inicio, fin, contenido));
			 
			 for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {			
					if(newcontenido.toString().split("\n")[i].contains("Prima Neta")) {
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
							
			 }
			 

			 
			 inicio = contenido.indexOf("INFORMACIÓN DEL VEHÍCULO ASEGURADO");
			 fin = contenido.indexOf("INFORMACIÓN DEL CONDUCTOR");
			 newcontenido = new StringBuilder();
			 newcontenido.append( fn.extracted(inicio, fin, contenido));
			 
			 for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
				
				 
				 if(newcontenido.toString().split("\n")[i].contains("Serie") && newcontenido.toString().split("\n")[i].contains("Placas") && newcontenido.toString().split("\n")[i].contains("Año")) {
					 int sp =newcontenido.toString().split("\n")[i+1].split("###").length;
				    if(sp == 7) {
				    	modelo.setPlacas(newcontenido.toString().split("\n")[i+1].split("###")[6]);
				    	modelo.setSerie(newcontenido.toString().split("\n")[i+1].split("###")[5]);
				    	modelo.setModelo(fn.castInteger(newcontenido.toString().split("\n")[i+1].split("###")[4].trim()));
				    	modelo.setDescripcion(newcontenido.toString().split("\n")[i+1].split("###")[3]);
				    	modelo.setMarca(newcontenido.toString().split("\n")[i+1].split("###")[2]);
				    }
				 }
			 }
			 
			
			 inicio = contenido.indexOf("INFORMACIÓN DEL CONDUCTOR");
			 fin = contenido.indexOf("RESTRICCIÓN GENERAL");
			 newcontenido = new StringBuilder();
			 newcontenido.append( fn.extracted(inicio, fin, contenido));

			 if(newcontenido.toString().split("\n").length > 1) {
				 modelo.setConductor(newcontenido.toString().split("\n")[2].split("###")[0]);
			 }
			 
			 
			 inicio = contenido.indexOf("COBERTURAS BÁSICAS");
			 fin = contenido.indexOf("Hoja 1");
			 newcontenido = new StringBuilder();
			 newcontenido.append( fn.extracted(inicio, fin, contenido)
					 .replace("Responsabilidad ###Civil ###por ###Daños ###a", "Responsabilidad Civil por Daños a terceros en sus bienes o sus ###1,000,000.00")
					 .replace("Equipo Especial, Adaptaciones y", "Equipo Especial, Adaptaciones y Conversiones###EXCLUIDO")
					 .replace("Gastos Médicos a Ocupantes", "Gastos Médicos a Ocupantes###220,000.00")
					 .replace("Responsabilidad Civil Catastrófica", "Responsabilidad Civil Catastrófica###3,000,000.00"));
			 
			 List<EstructuraCoberturasModel> coberturas = new ArrayList<>();
			 for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
				 EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();
				
				 if(!newcontenido.toString().split("\n")[i].contains("COBERTURAS BÁSICAS") && !newcontenido.toString().split("\n")[i].contains("Cobertura")) {
					 
					 
					 
					 switch (newcontenido.toString().split("\n")[i].split("###").length) {
					 case 2:
							cobertura.setNombre(newcontenido.toString().split("\n")[i].split("###")[0]);
							cobertura.setSa(newcontenido.toString().split("\n")[i].split("###")[1]);							
							coberturas.add(cobertura);
							
							break;
					 case 3:
						cobertura.setNombre(newcontenido.toString().split("\n")[i].split("###")[0]);
						cobertura.setSa(newcontenido.toString().split("\n")[i].split("###")[1]);
						cobertura.setDeducible(newcontenido.toString().split("\n")[i].split("###")[2]);
						coberturas.add(cobertura);
						
						break;
				
					default:
						break;
					}
				 }
			 }
			    modelo.setMoneda(1);
				modelo.setCoberturas(coberturas);
			return modelo;
		} catch (Exception ex) {
			modelo.setError(AguilaAutosModel.this.getClass().getTypeName() + " - catch:" + ex.getMessage() + " | "
					+ ex.getCause());
			return modelo;
		}
	}
}
