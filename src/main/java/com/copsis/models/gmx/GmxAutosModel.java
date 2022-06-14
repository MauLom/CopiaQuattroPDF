package com.copsis.models.gmx;

import java.util.ArrayList;
import java.util.List;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraCoberturasModel;
import com.copsis.models.EstructuraJsonModel;
import com.copsis.models.planSeguro.PlanSeguroSaludModel;

public class GmxAutosModel {
	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
	private String contenido = "";

	public GmxAutosModel(String contenido) {
		this.contenido = contenido;
	}

	public EstructuraJsonModel procesar() {
		int inicio = 0;
		int fin = 0;
		StringBuilder newcontenido = new StringBuilder();
		StringBuilder newdireccion = new StringBuilder();
		contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());
		try {
			
			modelo.setTipo(1);
			modelo.setCia(17);

			inicio = contenido.indexOf("DATOS DEL ASEGURADO");
			fin = contenido.indexOf("COBERTURAS CONTRATADAS");
			
			
			newcontenido.append( fn.extracted(inicio, fin, contenido));
			
			for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
		
				 if(newcontenido.toString().split("\n")[i].contains("DATOS DEL ASEGURADO") && newcontenido.toString().split("\n")[i].contains("R.F.C.") && newcontenido.toString().split("\n")[i].contains("Póliza")) {
					 modelo.setRfc(newcontenido.toString().split("\n")[i].split("R.F.C.")[1].split("Póliza")[0].replace("###", ""));
				  modelo.setCteNombre(newcontenido.toString().split("\n")[i+1].split("###")[0]);
				  modelo.setPoliza(newcontenido.toString().split("\n")[i+1].split("###")[1]);
				 }
				 if(newcontenido.toString().split("\n")[i].contains("Desde") ) {
					 newdireccion.append(newcontenido.toString().split("\n")[i].split("Desde")[0]);
					 String vigencia =newcontenido.toString().split("\n")[i].split("Desde")[1].replace("###", "").trim().replace(" ", "-");					
					 if(vigencia.split("-").length == 3) {					
						 modelo.setVigenciaDe(fn.formatDateMonthCadena(vigencia));
					 }					 			
				 }
				 if(newcontenido.toString().split("\n")[i].contains("C.P.") ) {
					 newdireccion.append( " "+newcontenido.toString().split("\n")[i].split("C.P.")[0]);
					 modelo.setCp(newcontenido.toString().split("\n")[i].split("C.P.")[1].trim().substring(0,5));
				 }
				 if(newdireccion.length() >  0) {
					 modelo.setCteDireccion(newdireccion.toString().replace("###", ""));
				 }
				 if(newcontenido.toString().split("\n")[i].contains("Hasta") ) {
					 String vigencia =newcontenido.toString().split("\n")[i].split("Hasta")[1].replace("###", "").trim().replace(" ", "-");
					 if(vigencia.split("-").length == 3) {						 
						 modelo.setVigenciaA(fn.formatDateMonthCadena(vigencia));
					 }					
				 }
				 
				 if(newcontenido.toString().split("\n")[i].contains("MARCA:")  && newcontenido.toString().split("\n")[i].contains("TIPO")) {
					 modelo.setMarca(newcontenido.toString().split("\n")[i].split("MARCA:")[1].split("TIPO")[0].replace("###", "").trim());
				 }
				 if(newcontenido.toString().split("\n")[i].contains("VERSIÓN:")  && newcontenido.toString().split("\n")[i].contains("No. de Cliente")) {
					 modelo.setDescripcion(newcontenido.toString().split("\n")[i].split("VERSIÓN:")[1].split("No. de Cliente")[0].replace("###", "").trim());
				 }
				 
				 if(newcontenido.toString().split("\n")[i].contains("MODELO:")  && newcontenido.toString().split("\n")[i].contains("SERIE:")
						 && newcontenido.toString().split("\n")[i].contains("MOTOR:")	 ) {
					 modelo.setModelo(fn.castInteger(newcontenido.toString().split("\n")[i].split("MODELO:")[1].split("SERIE:")[0].replace("###", "").trim()));
					 modelo.setSerie(newcontenido.toString().split("\n")[i].split("SERIE:")[1].split("MOTOR:")[0].replace("###", "").trim());
					 modelo.setMotor(newcontenido.toString().split("\n")[i].split("MOTOR:")[1].split("###")[1].replace("###", "").trim());
				 }
				 
				 if(newcontenido.toString().split("\n")[i].contains("PLACAS:")  && newcontenido.toString().split("\n")[i].contains("Producto")) {
					 modelo.setPlacas(newcontenido.toString().split("\n")[i].split("PLACAS:")[1].split("Producto")[0].replace("###", "").trim());
				 }
				 
				 if(newcontenido.toString().split("\n")[i].contains("CLAVE:")  && newcontenido.toString().split("\n")[i].contains("MARCA")) {
					 modelo.setClave(newcontenido.toString().split("\n")[i].split("CLAVE:")[1].split("MARCA:")[0].replace("###", "").trim());
				 }
				 
				 if(newcontenido.toString().split("\n")[i].contains("Paquete")  && newcontenido.toString().split("\n")[i].contains("Moneda")) {
					 modelo.setPlan(newcontenido.toString().split("\n")[i+1].split("###")[0].replace("###", "").trim());
					 modelo.setMoneda(fn.buscaMonedaEnTexto(newcontenido.toString().split("\n")[i+1]));
				 }
				 
			}
			
			inicio = contenido.indexOf("COBERTURAS CONTRATADAS");
			fin = contenido.indexOf("SERVICIOS DE ASISTENCIA");	
			newcontenido = new StringBuilder();
			newcontenido.append( fn.extracted(inicio, fin, contenido));		
		       List<EstructuraCoberturasModel> coberturas = new ArrayList<>();
			for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
				EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();
				if(!newcontenido.toString().split("\n")[i].contains("COBERTURAS CONTRATADAS")) {

					int sp =newcontenido.toString().split("\n")[i].split("###").length;
					if(sp == 3) {
						cobertura.setNombre(newcontenido.toString().split("\n")[i].split("###")[0]);
						cobertura.setSa(newcontenido.toString().split("\n")[i].split("###")[1]);
						cobertura.setDeducible(newcontenido.toString().split("\n")[i].split("###")[2]);
						coberturas.add(cobertura);
					}
					if(sp == 2) {
						cobertura.setNombre(newcontenido.toString().split("\n")[i].split("###")[0]);
						cobertura.setSa(newcontenido.toString().split("\n")[i].split("###")[1]);					
						coberturas.add(cobertura);
					}
				}
			}
			 modelo.setCoberturas(coberturas);
			 
			 
			 
		
				inicio = contenido.indexOf("SERVICIOS DE ASISTENCIA");
				fin = contenido.indexOf("General de Seguros");	
				newcontenido = new StringBuilder();
				newcontenido.append(fn.extracted(inicio, fin, contenido));	
		for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
				System.out.println(newcontenido.toString().split("\n")[i]);
					if(newcontenido.toString().split("\n")[i].contains("Fraccionado") && newcontenido.toString().split("\n")[i].contains("I.V.A.")) {
						 List<String> valores = fn.obtenerListNumeros(newcontenido.toString().split("\n")[i+1]);
						 modelo.setPrimaneta(fn.castBigDecimal(fn.castDouble(valores.get(0))));
						 modelo.setRecargo(fn.castBigDecimal(fn.castDouble(valores.get(1))));
						 modelo.setDerecho(fn.castBigDecimal(fn.castDouble(valores.get(2))));
						 modelo.setIva(fn.castBigDecimal(fn.castDouble(valores.get(4))));
						 modelo.setPrimaTotal(fn.castBigDecimal(fn.castDouble(valores.get(5).replace(",", ""))));						 						
					}
					if(newcontenido.toString().split("\n")[i].contains("Clave:")) {
						modelo.setCveAgente(newcontenido.toString().split("\n")[i].split("Clave:")[1].replace("###","" ).trim());
						modelo.setFormaPago( fn.formaPagoSring( newcontenido.toString().split("\n")[i+1]));				
					}
					if(newcontenido.toString().split("\n")[i].contains("Agente:")) {
						modelo.setAgente(newcontenido.toString().split("\n")[i].split("Agente:")[1].replace("###","" ).trim());
					}
				}
				
			
			return modelo;
		} catch (Exception ex) {
			modelo.setError(GmxAutosModel.this.getClass().getTypeName() + " | " + ex.getMessage() + " | " + ex.getCause());
			return modelo;
		}
		
	}
}
