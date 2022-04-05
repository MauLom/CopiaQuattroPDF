package com.copsis.models.planSeguro;

import java.util.ArrayList;
import java.util.List;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraAseguradosModel;
import com.copsis.models.EstructuraCoberturasModel;
import com.copsis.models.EstructuraJsonModel;

public class PlanSeguroSaludModel {

	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();	
	private String contenido = "";
	
    public  PlanSeguroSaludModel(String contenido){
    	this.contenido = contenido;
    }
    
    public EstructuraJsonModel procesar() {
		int inicio = 0;
		int fin = 0;
		StringBuilder newcontenido = new StringBuilder();
		contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());
		
		try {
			modelo.setTipo(3);
			modelo.setCia(25);
			
			inicio = contenido.indexOf("PÓLIZA DE SALUD");
			if(inicio == -1) {
				inicio = contenido.indexOf("SALUD INDIVIDUAL");
			}
			fin = contenido.indexOf("Asegurados");
	
			
			newcontenido.append( fn.extracted(inicio, fin, contenido));
			
			for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
				
				if(newcontenido.toString().split("\n")[i].contains("Poliza:")) {
					modelo.setPoliza(newcontenido.toString().split("\n")[i].split("Poliza:")[1].replace("###", "").trim());
				}
				if(newcontenido.toString().split("\n")[i].contains("Nombre:") && newcontenido.toString().split("\n")[i].contains("Inicio")) {
					modelo.setCteNombre(newcontenido.toString().split("\n")[i].split("Nombre:")[1].split("Inicio")[0].replace("###", "").trim());
				}
				if(newcontenido.toString().split("\n")[i].split("-").length > 3 ) {
					if(newcontenido.toString().split("\n")[i].split("###")[newcontenido.toString().split("\n")[i].split("###").length -1 ].contains("-")) {
						modelo.setVigenciaDe(fn.formatDateMonthCadena (newcontenido.toString().split("\n")[i].split("###")[newcontenido.toString().split("\n")[i].split("###").length -2 ]));
						modelo.setVigenciaA(fn.formatDateMonthCadena (newcontenido.toString().split("\n")[i].split("###")[newcontenido.toString().split("\n")[i].split("###").length -1 ]));
						
						if(modelo.getVigenciaA().length() > 0) {
							modelo.setFechaEmision(modelo.getVigenciaDe());
						}
					}

				}
				
				if(newcontenido.toString().split("\n")[i].contains("Dirección:") && newcontenido.toString().split("\n")[i].contains("Teléfonos") ){
					modelo.setCteDireccion(newcontenido.toString().split("\n")[i].split("Dirección:")[1].split("Teléfonos")[0].replace("###", "").trim()
				     + " " + newcontenido.toString().split("\n")[i+1].split("RFC")[0].replace("###", "").trim()
				     + " " + newcontenido.toString().split("\n")[i+2].split("C.P.")[0].replace("###", "").trim()
							
							);
				}
				
				if(newcontenido.toString().split("\n")[i].contains("RFC:") && newcontenido.toString().split("\n")[i].contains("desde")) {
					modelo.setRfc(newcontenido.toString().split("\n")[i].split("RFC:")[1].split("desde")[0].replace("###", "").trim());
				}
				
				if(newcontenido.toString().split("\n")[i].contains("C.P.")) {
					modelo.setCp(newcontenido.toString().split("\n")[i].split("C.P")[1].split("###")[1]);
				}
			
			}
			

			inicio = contenido.indexOf("Condiciones de la Póliza");
			fin = contenido.indexOf("COBERTURA BASICA");

			newcontenido = new StringBuilder();
			newcontenido.append( fn.extracted(inicio, fin, contenido));
			for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
				
				if(newcontenido.toString().split("\n")[i].contains("Deducible") && newcontenido.toString().split("\n")[i].contains("Coaseguro")
						
						&& newcontenido.toString().split("\n")[i].contains("Plan Avanzado") && newcontenido.toString().split("\n")[i+1].split("###").length > 7		) {
					
					modelo.setFormaPago(fn.formaPagoSring(newcontenido.toString().split("\n")[i+1]));
					modelo.setMoneda(fn.buscaMonedaEnTexto(newcontenido.toString().split("\n")[i+1]));
					modelo.setPlan(newcontenido.toString().split("\n")[i+1].split("###")[7]);
				}
				if(newcontenido.toString().split("\n")[i].contains("Plan Avanzado") && newcontenido.toString().split("\n")[i].contains("Moneda") && newcontenido.toString().split("\n")[i].contains("Forma de Pago") 
						&& newcontenido.toString().split("\n")[i+2].split("###").length > 6) {			
					modelo.setFormaPago(fn.formaPagoSring(newcontenido.toString().split("\n")[i+2]));
					modelo.setMoneda(fn.buscaMonedaEnTexto(newcontenido.toString().split("\n")[i+2]));
					modelo.setPlan(newcontenido.toString().split("\n")[i+1]);
				}
				
			}
			
			inicio = contenido.indexOf("Asegurados");
			fin = contenido.indexOf("COBERTURA BASICA");
			
			newcontenido = new StringBuilder();
			newcontenido.append( fn.extracted(inicio, fin, contenido));
			List<EstructuraAseguradosModel> asegurados = new ArrayList<>();
			for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
				EstructuraAseguradosModel asegurado = new EstructuraAseguradosModel();
				if(newcontenido.toString().split("\n")[i].split("-").length > 3 ) {
					
					if(newcontenido.toString().split("\n")[i].split("###").length == 6) {
						asegurado.setNombre(newcontenido.toString().split("\n")[i].split("###")[0]);
						asegurado.setSexo( fn.sexo(newcontenido.toString().split("\n")[i].split("###")[1]).booleanValue() ? 0 :1) ;
						asegurado.setNacimiento(fn.formatDateMonthCadena(newcontenido.toString().split("\n")[i].split("###")[2]));
						asegurado.setAntiguedad(fn.formatDateMonthCadena(newcontenido.toString().split("\n")[i].split("###")[3]));						
						asegurados.add(asegurado);
					}
				}
			}
			modelo.setAsegurados(asegurados);
			
			
			
		
			
			inicio = contenido.indexOf("Primas");
			fin = contenido.indexOf("Agente");

			newcontenido = new StringBuilder();
			newcontenido.append( fn.extracted(inicio, fin, contenido));
			for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
				if(newcontenido.toString().split("\n")[i].contains("Prima Neta")) {
					modelo.setPrimaneta(fn.castBigDecimal(fn.cleanString(
							newcontenido.toString().split("\n")[i+1].split("###")[0].trim())));
					modelo.setRecargo(fn.castBigDecimal(fn.cleanString(
							newcontenido.toString().split("\n")[i+1].split("###")[3].trim())));														
					modelo.setDerecho(fn.castBigDecimal(fn.cleanString(
							newcontenido.toString().split("\n")[i+1].split("###")[4].trim())));												
					modelo.setIva(fn.castBigDecimal(fn.cleanString(
							newcontenido.toString().split("\n")[i+1].split("###")[5].trim())));							
					modelo.setPrimaTotal(fn.castBigDecimal(fn.cleanString(
							newcontenido.toString().split("\n")[i+1].split("###")[6].trim())));
				}
			}
			
			inicio = contenido.indexOf("Agente");
			fin = contenido.indexOf("En###cumplimiento");
			
			newcontenido = new StringBuilder();
			newcontenido.append( fn.extracted(inicio, fin, contenido));
			
		 if(newcontenido.toString().split("\n").length > 1) {
			 modelo.setCveAgente(newcontenido.toString().split("\n")[1].split("###")[0]);
			 modelo.setAgente(newcontenido.toString().split("\n")[1].split("###")[1].replace(".", "").trim());
		 }


		    inicio = contenido.indexOf("COBERTURA BASICA");
			fin = contenido.indexOf("Financiamiento");

			newcontenido = new StringBuilder();
			newcontenido.append( fn.extracted(inicio, fin, contenido));
			List<EstructuraCoberturasModel> coberturas = new ArrayList<>();
			for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
				EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();
			
				if(!newcontenido.toString().split("\n")[i].contains("Son las que se describen") 
				 && !newcontenido.toString().split("\n")[i].contains("COBERTURAS ADICIONALES")
				 && !newcontenido.toString().split("\n")[i].contains("Primas")
				 && !newcontenido.toString().split("\n")[i].contains("Prima Neta")) {
					if(newcontenido.toString().split("\n")[i].contains("COBERTURA BASICA")) {
						cobertura.setNombre("COBERTURA BASICA");
						coberturas.add(cobertura);
					}else {
						if(newcontenido.toString().split("\n")[i].split("###").length == 2) {
						cobertura.setNombre(newcontenido.toString().split("\n")[i].split("###")[0]);
						cobertura.setSa(newcontenido.toString().split("\n")[i].split("###")[1]);
						coberturas.add(cobertura);
						}
					}
					
				}				
			}
			modelo.setCoberturas(coberturas);
			return modelo;
		} catch (Exception ex) {
			modelo.setError(PlanSeguroSaludModel.this.getClass().getTypeName() + " | " + ex.getMessage() + " | " + ex.getCause());
			 return modelo;
			
		}
    }
}
