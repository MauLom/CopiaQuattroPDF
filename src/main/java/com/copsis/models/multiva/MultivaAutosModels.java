package com.copsis.models.multiva;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraJsonModel;
import com.copsis.models.inbursa.InbursaAutosModel;

public class MultivaAutosModels {
	// Clases
	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
	// Varaibles
	private String contenido = "";
	private String newcontenido = "";
	private String recibosText = "";
	private String resultado = "";
	private int inicio = 0;
	private int fin = 0;
	private int donde = 0;
	
	public MultivaAutosModels(String contenido) {
		this.contenido = contenido;
	}
	
	public EstructuraJsonModel procesar() {
		contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());
		System.out.println(contenido);
		contenido = contenido.replace("12 Hrs.", "").replace("pago","Pago")
				.replace("Cllave y Marca:", "Clave y Marca:")
				.replace("MMooddeelloo::", "Modelo:")
				.replace("MMoototor","Motor").replace("12:00 Horas", "");
		try {
			// tipo
			modelo.setTipo(1);
			// cia
			modelo.setCia(65);		
			//Datos del contrante
			
			
			
	        inicio = contenido.indexOf("PÓLIZA DE SEGURO");
            fin = contenido.indexOf("DESGLOSE DE COBERTURAS");

            if(inicio > 0 &&  fin >  0 && inicio < fin) {
            	newcontenido = contenido.substring(inicio, fin).replace("\r","").replace("@@@", "").replace("######", "###").replace("###2003###", "###")
            			.replace("Noo::", "No:");
            	for (int i = 0; i < newcontenido.split("\n").length; i++) {
            		System.out.println("============> "+newcontenido.split("\n")[i]);
            		if(newcontenido.split("\n")[i].contains("CONTRATANTE") && newcontenido.split("\n")[i].contains("Póliza")  && newcontenido.split("\n")[i].contains("Inciso:")) {
            			modelo.setPoliza(newcontenido.split("\n")[i+1].split("###")[0].replace("-", ""));
            			modelo.setPolizaGuion(newcontenido.split("\n")[i+1].split("###")[0]);
            			modelo.setInciso(fn.castInteger(newcontenido.split("\n")[i+1].split("###")[1]));
            			modelo.setCteNombre(newcontenido.split("\n")[i+2].split("###")[0]);            		            	
            		}else
            		if(newcontenido.split("\n")[i].contains("CONTRATANTE") && newcontenido.split("\n")[i].contains("Póliza")  && newcontenido.split("\n")[i].contains("Inciso")) {
            			modelo.setCteNombre(newcontenido.split("\n")[i+1].split("Nombre:")[1].split("Agente")[0].replace("###", ""));            		            	
            			modelo.setPoliza(newcontenido.split("\n")[i].split("Endoso")[1].split("###")[0].replace("-", ""));
            			modelo.setInciso(fn.castInteger(newcontenido.split("\n")[i].split("Endoso")[1].split("###")[1].replace("-", "")));
            			modelo.setPolizaGuion(newcontenido.split("\n")[i].split("Endoso")[1].split("###")[0]);
            		}
            		
            		if(newcontenido.split("\n")[i].contains("Nombre:") && newcontenido.split("\n")[i].contains("Agente") && newcontenido.split("\n")[i].contains("Moneda")) {
            			//se usa otro proceso para extraer la direccion
            		}
            		else if(newcontenido.split("\n")[i].contains("Agente") && newcontenido.split("\n")[i].contains("Moneda")) {
            			modelo.setCveAgente(newcontenido.split("\n")[i+1].split("###")[0]);
            			modelo.setMoneda(fn.moneda(newcontenido.split("\n")[i+1].split("###")[2]));            			
            			resultado =newcontenido.split("\n")[i+2] +" " + newcontenido.split("\n")[i+5] +" "+ newcontenido.split("\n")[i+7];
            			modelo.setCteDireccion(resultado);
            		}
            		
					  if(newcontenido.split("\n")[i].contains("Dirección")) {  
							resultado = newcontenido.split("\n")[i].split("Dirección")[1].split("###")[0];
						}
					   if(newcontenido.split("\n")[i].contains("Delegación")) {  
						   resultado += newcontenido.split("\n")[i].split("Delegación")[1].split("###")[0];
						}
					   if(newcontenido.split("\n")[i].contains("Estado:")) {  
						   resultado += newcontenido.split("\n")[i].split("Estado:")[1].split("###")[0];
						   modelo.setCteDireccion(resultado.replace("Municipio:", ""));
					   }
						
            		
            		
            		
            		if(newcontenido.split("\n")[i].contains("Pago")) {                       		
            			modelo.setFormaPago( fn.formaPago( newcontenido.split("\n")[i+1].split("###")[ newcontenido.split("\n")[i+1].split("###").length-1]));
            		}
            		if(newcontenido.split("\n")[i].contains("Desde") && newcontenido.split("\n")[i].contains("Hasta")) {
            			
            			modelo.setVigenciaA(fn.formatDate_MonthCadena(newcontenido.split("\n")[i+1].split("###")[newcontenido.split("\n")[i+1].split("###").length -1].trim()));
            			modelo.setVigenciaDe(fn.formatDate_MonthCadena(newcontenido.split("\n")[i+1].split("###")[newcontenido.split("\n")[i+1].split("###").length -2].trim()));
            		}
            		if(newcontenido.split("\n")[i].contains("Emisión")) {
            			if(newcontenido.split("\n")[i].contains("-")) {
            				modelo.setFechaEmision(fn.formatDate_MonthCadena(newcontenido.split("\n")[i].split("Emisión")[1].replace("###", "")));	
            			}
            			if(newcontenido.split("\n")[i+1].contains("-")) {
            				modelo.setFechaEmision(fn.formatDate_MonthCadena(newcontenido.split("\n")[i+1].split("###")[1].replace("###", "")));	
            			}
            			
            		}
            	 
            		//Datos del vehiculo
            		if(newcontenido.split("\n")[i].contains("Clave y Marca:")) {
            			System.out.println("-----_--> " + newcontenido.split("\n")[i].split("Marca:")[1].trim().split(" ")[0]);
            			modelo.setClave(newcontenido.split("\n")[i].split("Marca:")[1].trim().split(" ")[0].replace("###", ""));
            			modelo.setMarca(newcontenido.split("\n")[i].split("Marca:")[1].split(" ")[1].replace("###", ""));
            		}
            		if(newcontenido.split("\n")[i].contains("Descripción:")) {
            			modelo.setDescripcion(newcontenido.split("\n")[i].split("Descripción:")[1].replace("###", ""));
            		}
            		if(newcontenido.split("\n")[i].contains("Modelo:") &&  newcontenido.split("\n")[i].contains("SERIE:") &&  newcontenido.split("\n")[i].contains("Motor") ) {
            			modelo.setModelo(fn.castInteger(newcontenido.split("\n")[i].split("Modelo:")[1].split("SERIE:")[0].trim().replace("###", "")));
            		    modelo.setSerie(newcontenido.split("\n")[i].split("SERIE:")[1].split("Motor")[0].replace("###",""));
            		    modelo.setMotor(newcontenido.split("\n")[i].split("No:")[1]);
            		}
            		if(newcontenido.split("\n")[i].contains("Placa:")) {
            			if( newcontenido.split("\n")[i].split("Placa:")[1].contains("*NCI")) {
            				modelo.setPlacas(newcontenido.split("\n")[i].split("Placa:")[1].split("###")[0].trim());
            			}else {
            				modelo.setPlacas(newcontenido.split("\n")[i].split("Placa:")[1].split("###")[1]);
            			}
            	
            		}            	
            	}
            }
			
			
			return modelo;
		} catch (Exception ex) {
			modelo.setError(
					MultivaAutosModels.this.getClass().getTypeName() + " - catch:" + ex.getMessage() + " | " + ex.getCause());
			return modelo;
		}
	}

}
