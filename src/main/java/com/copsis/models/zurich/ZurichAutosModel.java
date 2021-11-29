package com.copsis.models.zurich;

import java.util.ArrayList;
import java.util.List;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraCoberturasModel;
import com.copsis.models.EstructuraJsonModel;


public class ZurichAutosModel {
	// Clases
	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
	// Varaibles
	private String contenido = "";

	
	public ZurichAutosModel(String contenido) {
		this.contenido = contenido;
	}
	
	public EstructuraJsonModel procesar() {
		contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());
		try {
			
			 String newcontenido = "";	
			 String  newcontenidoDire=""; 
			
			 int inicio = 0;
			 int fin = 0;
			  modelo.setTipo(1);
			    modelo.setCia(44);
	            
	            inicio = contenido.indexOf("PÓLIZA No.");
	            fin = contenido.indexOf("Datos del Asegurado");
	            if (inicio > 0 & fin > 0 & inicio < fin) {
	                newcontenido = fn.eliminaSpacios(contenido.substring(inicio, fin).replace("@@@", ""));
	                
	                modelo.setPoliza(newcontenido.split("PÓLIZA No.")[1].split("###")[1]);
	                modelo.setEndoso(newcontenido.split("Endoso")[1].split("###")[1]);
	            }
	            
	            inicio = contenido.indexOf("@@@C.P.###");
	            if (inicio == -1) {
	                inicio = contenido.indexOf("###CP.###");
	            }
	            fin = contenido.indexOf("###No.");
	            if (fin == -1) {
	                fin = contenido.indexOf("RFC:###");
	            }
	            
	            if (inicio > 0 & fin > 0 & inicio < fin) {
	                newcontenido = contenido.substring(inicio, fin).replace("\r\n", "");
	                modelo.setCp(newcontenido.split("###")[1]);
	            }

	          
	            /**
	             * DATOS DEL CONTRATANTE*
	             */
	            inicio = contenido.indexOf("Datos del Asegurado");
	            fin = contenido.indexOf("Descripción del Vehículo");
	            if (inicio > 0 & fin > 0 & inicio < fin) {
	                newcontenido = fn.eliminaSpacios(contenido.substring(inicio, fin).replace("@@@", "").replace("12:00hrs", ""));
	                for (int i = 0; i < newcontenido.split("\n").length; i++) {
	                    if (newcontenido.split("\n")[i].contains("Datos del Asegurado")) {
	                        modelo.setCteNombre(newcontenido.split("\n")[i + 1].split("###")[0]);
	                        modelo.setVigenciaDe(fn.formatDateMonthCadena(newcontenido.split("\n")[i + 1].split("###")[2].replace(":", "").trim()));
	                    }
	                    if (newcontenido.split("\n")[i].contains("Hasta")) {
	                        modelo.setVigenciaA(fn.formatDateMonthCadena(newcontenido.split("\n")[i].split("###")[1].replace(":", "").trim()));
	                    }
	                    if (newcontenido.split("\n")[i].contains("Emisión")) {
	                        modelo.setFechaEmision(fn.formatDateMonthCadena(newcontenido.split("\n")[i].split("Emisión")[1].replace("###", "").replace(":", "").trim()));
	                        newcontenidoDire = newcontenido.split("\n")[i].split("Duración")[0] + " " + newcontenido.split("\n")[i + 1] + " "
	                                + newcontenido.split("\n")[i + 2].split("Moneda")[0] + " "
	                                + newcontenido.split("\n")[i + 3].split("Documento")[0].replace("###", " ");
	                        modelo.setCteDireccion(newcontenidoDire.replace("\r", "").replace("### ", " "));
	                    }
	                    
	                    if (newcontenido.split("\n")[i].contains("R.F.C.") && newcontenido.split("\n")[i].contains("Producto")) {
	                    	modelo.setRfc(newcontenido.split("\n")[i].split("R.F.C.")[1].split("Producto")[0].replace("-", "").replace("###", "").trim());
	                    	if(newcontenido.split("\n")[i].split("Producto")[1].length() >10) {
	                    		if(newcontenido.split("\n")[i].split("Producto")[1].contains("Tu Auto Seguro Más")) {
	                    			modelo.setPlan("Amplia");
	                    		}else {
	                    			if(newcontenido.split("\n")[i].split("Producto")[1].contains("Relax auto")) {
	                    				modelo.setPlan("Amplia");
		                    		}
	                    		}
	                    	}
	                    }
	                    
	                    
	                    if (newcontenido.split("\n")[i].contains("Moneda")) {
	                        modelo.setMoneda(1);
	                    }
	                    
	                    if (newcontenido.split("\n")[i].contains("Forma de pag")) {
	                        
	                        modelo.setFormaPago(fn.formaPago(newcontenido.split("Forma de pag")[1].split("###")[1].split("\n")[0].trim()));
	                        
	                    }
	                    
	                }
	            }

	            /*AGENTE*/
	            inicio = contenido.indexOf("Nombre del Agente");
	            fin = contenido.indexOf("Folio");
	            if (inicio > 0 & fin > 0 & inicio < fin) {
	                newcontenido = fn.eliminaSpacios(contenido.substring(inicio, fin).replace("@@@", "")).replace("\r\n", "");
	                modelo.setAgente(newcontenido.split("Nombre del Agente")[1].split("###")[1]);
	                int sp = newcontenido.split("Clave:")[1].split("###").length;
	                if(sp == 3) {
	                	  modelo.setCveAgente(newcontenido.split("Clave:")[1].split("###")[2]);
	                }
	                if(sp == 2) {
	                	  modelo.setCveAgente(newcontenido.split("Clave:")[1].split("###")[1]);
	                }
	                               	               
	            }


	            /*DESCRIPCION DEL VEHICULO*/
	            inicio = contenido.indexOf("Descripción del Vehículo");
	            fin = contenido.indexOf("Resumen de Valores");
	            if (inicio > 0 & fin > 0 & inicio < fin) {
	                newcontenido = fn.eliminaSpacios(contenido.substring(inicio, fin).replace("@@@", ""));
	                for (String x : newcontenido.split("\n")) {
	                    if (x.contains("Placas")) {
	                        modelo.setPlacas(x.split("###")[0].replace("Placas:", ""));
	                    }
	                    if (x.contains("No. de Serie")) {
	                        modelo.setSerie(x.split("###")[1]);
	                    }
	                    if (x.contains("Marca") && x.contains("Modelo")) {
	                        modelo.setMarca(x.split("###")[1]);
	                        modelo.setModelo(Integer.parseInt(fn.cleanString(x.split("###")[3])));
	                    }
	                    if (x.contains("Descripción:")) {
	                        modelo.setDescripcion(x.split("###")[1].replaceAll("\r", ""));
	                    }
	                }
	            }

	            /*PRIMAS**/
	            inicio = contenido.indexOf("Resumen de Valores");
	            fin = contenido.indexOf("Coberturas Amparadas");
	            if (inicio > 0 & fin > 0 & inicio < fin) {
	                newcontenido = fn.eliminaSpacios(contenido.substring(inicio, fin).replace("@@@", "").replace("###$", ""));
	                for (int i = 0; i < newcontenido.split("\n").length; i++) {
	                    if (newcontenido.split("\n")[i].contains("Prima Neta")) {	                    
	                        modelo.setPrimaneta(fn.castBigDecimal(fn.castDouble(newcontenido.split("\n")[i].split("###")[1].trim())));
	                        modelo.setCargoExtra(fn.castBigDecimal(fn.castDouble(newcontenido.split("\n")[i].split("###")[3])));	 	          
	                    }
	                    if (newcontenido.split("\n")[i].contains("Financiamiento")) {
	                    		modelo.setRecargo(fn.castBigDecimal(fn.castDouble(newcontenido.split("\n")[i].split("###")[1])));
		                        modelo.setDerecho(fn.castBigDecimal(fn.castDouble(newcontenido.split("\n")[i].split("###")[3])));
		                        modelo.setIva(fn.castBigDecimal(fn.castDouble(newcontenido.split("\n")[i].split("###")[5])));	                    		                     
	                    }
	                    if (newcontenido.split("\n")[i].contains("Prima Total")) {	                        
	                        modelo.setPrimaTotal(fn.castBigDecimal(fn.castFloat(newcontenido.split("\n")[i].split("###")[1])));
	                    }
	                    
	                }
	            }
	            
	            int iniciod = contenido.indexOf("Primas");
	            int finalda = contenido.indexOf("@@@*LUC:Límite");
	            if (finalda > 0) {
	                finalda = contenido.indexOf("@@@*LUC:Límite");
	            } else {
	                finalda = contenido.indexOf("@@@Observaciones:");
	            }
	            String datos = contenido.substring(iniciod, finalda);
	            List<EstructuraCoberturasModel> coberturas = new ArrayList<>();
	            for (String dato : datos.split("\n")) {
	            	EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();
	                Integer num = dato.split("###").length;	   
	                if (num == 4) {
	                	cobertura.setNombre(dato.split("###")[0].replace("@@@", ""));
	                	cobertura.setSa(dato.split("###")[1].trim().replace("    ", "").replace("   ", ""));
	                	cobertura.setDeducible(dato.split("###")[2].trim());
	                	  coberturas.add(cobertura);
	                }
	                if (num == 3) {
	                	cobertura.setNombre(dato.split("###")[0].replace("@@@", ""));
	                	cobertura.setSa(dato.split("###")[1].trim().replace("    ", "").replace("   ", ""));
	                	cobertura.setDeducible(dato.split("###")[2].trim());
	               	   coberturas.add(cobertura);	
	                }
	            }
	        	modelo.setCoberturas(coberturas);
	        	
	        	
	        	
	        	
			
			return modelo;
		} catch (Exception ex) {
			modelo.setError(
					ZurichAutosModel.this.getClass().getTypeName() + " - catch:" + ex.getMessage() + " | " + ex.getCause());
			return modelo;
		}
	}

}
