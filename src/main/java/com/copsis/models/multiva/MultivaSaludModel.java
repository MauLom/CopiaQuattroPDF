package com.copsis.models.multiva;

import java.util.ArrayList;
import java.util.List;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraAseguradosModel;
import com.copsis.models.EstructuraCoberturasModel;
import com.copsis.models.EstructuraJsonModel;

public class MultivaSaludModel {
	// Clases
	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
	// Varaibles
	private String contenido = "";
	private String newcontenido = "";	
	private int inicio = 0;
	private int fin = 0;
	
	public MultivaSaludModel(String contenido) {
		this.contenido = contenido;
	}
	public EstructuraJsonModel procesar() {
		contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());
		try {
			// tipo
			modelo.setTipo(3);
			// cia
			modelo.setCia(65);		
			//Datos del contrante

			
	        inicio = contenido.indexOf("Póliza de Seguro");
            fin = contenido.indexOf("Datos de los Asegurados");

            if(inicio > 0 &&  fin >  0 && inicio < fin) {
            	newcontenido = contenido.substring(inicio,fin).replace("\r", "").replace("@@@", "");
            	for (int i = 0; i < newcontenido.split("\n").length; i++) {					
					if(newcontenido.split("\n")[i].contains("Contratante") && newcontenido.split("\n")[i].contains("Póliza:")) {
						modelo.setCteNombre(newcontenido.split("\n")[i+1].trim());
						modelo.setPoliza(newcontenido.split("\n")[i].split("Póliza:")[1].replace("###", "").trim());
					}
					if(newcontenido.split("\n")[i].contains("Agente:") && newcontenido.split("\n")[i].contains("Oficina:") && newcontenido.split("\n")[i].contains("Póliza")) {
						modelo.setCveAgente(newcontenido.split("\n")[i].split("Agente:")[1].split("Oficina:")[0].replace("###", ""));
					}
					if(newcontenido.split("\n")[i].contains("Domicilio") && newcontenido.split("\n")[i].contains("Moneda:") && newcontenido.split("\n")[i].contains("Pago:")) {
						modelo.setMoneda(fn.moneda(newcontenido.split("\n")[i].split("Moneda:")[1].split("Forma")[0].replace("###", "")));
						modelo.setFormaPago(fn.formaPago(newcontenido.split("\n")[i].split("Pago:")[1].replace("###", "")));
						modelo.setCteDireccion(newcontenido.split("\n")[i+1] +" "+ newcontenido.split("\n")[i+2].split("###")[0]);
					}
					if(newcontenido.split("\n")[i].contains("Plan:")) {
						modelo.setPlan(newcontenido.split("Plan:")[1].split("###")[1]);
					}
					if(newcontenido.split("\n")[i].split("-").length  > 3) {
						if(newcontenido.split("\n")[i].split("###")[2].contains("-")) {							 
					     String b =newcontenido.split("\n")[i].split("###")[2].split("-")[2].substring(4,6) +"-" +newcontenido.split("\n")[i].split("###")[2].split("-")[3] +"-"+newcontenido.split("\n")[i].split("###")[2].split("-")[4];
						 modelo.setVigenciaDe(fn.formatDate_MonthCadena(b));
						 modelo.setVigenciaA(fn.formatDate_MonthCadena(newcontenido.split("\n")[i].split("###")[4]));						
						}						
					}					
				}
            }
            
			
	        inicio = contenido.indexOf("Datos de los Asegurados");
            fin = contenido.indexOf("Detalle del Seguro");

            if(inicio > 0 &&  fin >  0 && inicio < fin) {
            	List<EstructuraAseguradosModel> asegurados = new ArrayList<>();
            	newcontenido = contenido.substring(inicio,fin).replace("\r", "").replace("@@@", "");
            	for (int i = 0; i < newcontenido.split("\n").length; i++) {
            		EstructuraAseguradosModel asegurado = new EstructuraAseguradosModel();            		
            		if(newcontenido.split("\n")[i].split("-").length > 3) {
                		asegurado.setNombre(newcontenido.split("\n")[i].split("###")[0]);
                		asegurado.setAntiguedad(fn.formatDate_MonthCadena( newcontenido.split("\n")[i].split("###")[2]));
                		asegurado.setNacimiento(fn.formatDate_MonthCadena(newcontenido.split("\n")[i].split("###")[3]));
                		asegurado.setParentesco( fn.parentesco( newcontenido.split("\n")[i].split("###")[4]));
                		asegurado.setSexo(fn.sexo( newcontenido.split("\n")[i].split("###")[6]) ? 1:0);
                		asegurados.add(asegurado);
            		}
            	} 
            	modelo.setAsegurados(asegurados);
            }
            
        	
	        inicio = contenido.indexOf("Detalle del Seguro");
            fin = contenido.indexOf("Coberturas###Alcance");

            if(inicio > 0 &&  fin >  0 && inicio < fin) {
            	newcontenido = contenido.substring(inicio,fin).replace("\r", "").replace("@@@", "");
            	for (int i = 0; i < newcontenido.split("\n").length; i++) {	
            		if(newcontenido.split("\n")[i].contains("Suma Asegurada") && newcontenido.split("\n")[i].contains("Deducible") && newcontenido.split("\n")[i].contains("Coaseguro")) {
            			modelo.setSa(newcontenido.split("\n")[i+2].split("###")[1]);
            			modelo.setDeducible(newcontenido.split("\n")[i+2].split("###")[2]);
            			modelo.setCoaseguro(newcontenido.split("\n")[i+2].split("###")[3]);
            		}
            		if(newcontenido.split("\n")[i].contains("Tope Coaseguro")){
            			modelo.setCoaseguroTope(newcontenido.split("\n")[i].split("###")[1]);
            		}
            		
            	}
            }
            
            
            //PRIMAS
            inicio = contenido.indexOf("Prima Neta");
            fin = contenido.indexOf("En cumplimiento ");

            if(inicio > 0 &&  fin >  0 && inicio < fin) {
            	newcontenido = contenido.substring(inicio,fin).replace("\r", "").replace("@@@", "");
            	for (int i = 0; i < newcontenido.split("\n").length; i++) {	;
            		if( newcontenido.split("\n")[i].contains("Prima Neta")) {
						modelo.setPrimaneta( fn.castBigDecimal(fn.castDouble(newcontenido.split("\n")[i].split("###")[newcontenido.split("\n")[i].split("###").length -1])));
					}
            		if(newcontenido.split("\n")[i].contains("Recargo")) {
						modelo.setRecargo(fn.castBigDecimal(fn.castDouble(newcontenido.split("\n")[i].split("###")[newcontenido.split("\n")[i].split("###").length -1])));
					}
            		
            		if(newcontenido.split("\n")[i].contains("Expedición")) {
                    	modelo.setDerecho( fn.castBigDecimal(fn.castDouble(newcontenido.split("\n")[i].split("###")[newcontenido.split("\n")[i].split("###").length -1])));
					}
            		if(newcontenido.split("\n")[i].contains("I.V.A")) {
  	            	   modelo.setIva( fn.castBigDecimal(fn.castDouble(newcontenido.split("\n")[i].split("###")[newcontenido.split("\n")[i].split("###").length -1])));
  					}
            		if(newcontenido.split("\n")[i].contains("Prima Total")) {
   	            	   modelo.setPrimaTotal( fn.castBigDecimal(fn.castDouble(newcontenido.split("\n")[i].split("###")[newcontenido.split("\n")[i].split("###").length -1])));
   					}            		
            	}
            }
            
            inicio = contenido.indexOf("Coberturas###Alcance");
            fin = contenido.indexOf("Esta póliza queda");

            if(inicio > 0 &&  fin >  0 && inicio < fin) {
            	   List<EstructuraCoberturasModel> coberturas = new ArrayList<>();
            	newcontenido = contenido.substring(inicio,fin).replace("\r", "").replace("@@@", "");
            	for (int i = 0; i < newcontenido.split("\n").length; i++) {	
            		EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();
	                  if(newcontenido.split("\n")[i].contains("Coberturas")) {	                	 
	                  }else {
	                	  int sp = newcontenido.split("\n")[i].split("###").length;
	                	   cobertura.setNombre(newcontenido.split("\n")[i].split("###")[0]);
	                	   cobertura.setDeducible(newcontenido.split("\n")[i].split("###")[1]);
	                	   if(sp > 2) {
	                		   cobertura.setSa(newcontenido.split("\n")[i].split("###")[2]);
	                	   }
	                	   coberturas.add(cobertura);	        
	                  }
            	}
            	modelo.setCoberturas(coberturas);
            }
            
			
			return modelo;
		} catch (Exception ex) {
			modelo.setError(
					MultivaSaludModel.this.getClass().getTypeName() + " - catch:" + ex.getMessage() + " | " + ex.getCause());
			return modelo;
		}
	}
	
	
}
