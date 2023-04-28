package com.copsis.models.zurich;

import java.util.ArrayList;
import java.util.List;

import com.copsis.constants.ConstantsValue;
import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraCoberturasModel;
import com.copsis.models.EstructuraJsonModel;


public class ZurichAutosModel {
	// Clases
	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
	// Varaibles
	private String contenido = "";
	private static final String CLAVE2 = "laDvEe";

	
	public ZurichAutosModel(String contenido) {
		this.contenido = contenido;
	}
	
	public EstructuraJsonModel procesar() {
		contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales()).replace("PÓLIZA No.", ConstantsValue.POLIZA_NO)
				.replace("Datos del Asegurado", "Datos del asegurado");
		try {
			
			 String newcontenido = "";	
			 String  newcontenidoDire=""; 
			
			 int inicio = 0;
			 int fin = 0;
			  modelo.setTipo(1);
			    modelo.setCia(44);

	            inicio = contenido.indexOf(ConstantsValue.POLIZA_NO);
	            fin = contenido.indexOf(ConstantsValue.DATOS_ASEGURADO);
	            if (inicio > -1 && fin > -1 && inicio < fin) {
	                newcontenido = fn.eliminaSpacios(contenido.substring(inicio, fin).replace("@@@", ""));
	                
	                modelo.setPoliza(newcontenido.split(ConstantsValue.POLIZA_NO)[1].split("###")[1]);
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
	            
	            if (inicio > -1 && fin > -1 && inicio < fin) {
	                newcontenido = contenido.substring(inicio, fin).replace("\r\n", "");
	                modelo.setCp(newcontenido.split("###")[1]);
	            }

	          
	            /**
	             * DATOS DEL CONTRATANTE*
	             */
	            inicio = contenido.indexOf(ConstantsValue.DATOS_ASEGURADO);
	            fin = contenido.indexOf("Descripción del Vehículo");
	            if (inicio > -1 && fin > -1 && inicio < fin) {
	                newcontenido = fn.eliminaSpacios(contenido.substring(inicio, fin).replace("@@@", "").replace("12:00hrs", ""));
	                for (int i = 0; i < newcontenido.split("\n").length; i++) {
	                    if (newcontenido.split("\n")[i].contains(ConstantsValue.DATOS_ASEGURADO)) {
	                        modelo.setCteNombre(newcontenido.split("\n")[i + 1].split("###")[0]);
							List<String> valores = fn.obtenVigePoliza(newcontenido.split("\n")[i + 1].replace(":", "").trim());
							System.out.println(valores);
	                        modelo.setVigenciaDe(fn.formatDateMonthCadena(valores.get(0)));
	                    }
	                    if (newcontenido.split("\n")[i].contains("Hasta")) {
	                        modelo.setVigenciaA(fn.formatDateMonthCadena(newcontenido.split("\n")[i].split("###")[1].replace(":", "").trim()));
	                    }
	                    if (newcontenido.split("\n")[i].contains("Emisión")) {
	                        modelo.setFechaEmision(fn.formatDateMonthCadena(newcontenido.split("\n")[i].split("Emisión")[1].replace("###", "").replace(":", "").trim()));
	                        newcontenidoDire = newcontenido.split("\n")[i].split(ConstantsValue.DURACION)[0] + " " + newcontenido.split("\n")[i + 1] + " "
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
	            obtenerCveAgente();

	            /*DESCRIPCION DEL VEHICULO*/
	            inicio = contenido.indexOf("Descripción del Vehículo");
	            fin = contenido.indexOf("Resumen de Valores");
	            if (inicio > -1 && fin > -1 && inicio < fin) {
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
	                        modelo.setDescripcion(x.split("###")[1].replace("\r", ""));
	                    }
	                    if(x.contains("Clave:")) {
	                        modelo.setClave(x.split("###")[5].replace("\r", ""));
	                    }
	                }
	            }

	            /*PRIMAS**/
	            inicio = contenido.indexOf("Resumen de Valores");
	            fin = contenido.indexOf("Coberturas Amparadas");
	            if (inicio > -1 && fin > -1 && inicio < fin) {
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
	                        modelo.setPrimerPrimatotal(fn.castBigDecimal(fn.castFloat(newcontenido.split("\n")[i].split("###")[3])));
	                        modelo.setSubPrimatotal(fn.castBigDecimal(fn.castFloat(newcontenido.split("\n")[i].split("###")[5])));
	                    }
	                    
	                }
	            }
	            
	             inicio = contenido.indexOf("Primas");
	             fin = contenido.indexOf("@@@*LUC:Límite");
	             if (fin == -1) {
	            	fin = contenido.indexOf("@@@Observaciones:");
	             } 
	            
	            newcontenido = contenido.substring(inicio, fin);
	            List<EstructuraCoberturasModel> coberturas = new ArrayList<>();
	            for (String dato : newcontenido.split("\n")) {
	            	EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();
	                Integer num = dato.split("###").length;	   
	                if (num == 4 || num == 3) {
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
	
	private void obtenerCveAgente() {
		int inicio = contenido.indexOf("Nombre del Agente");
        int fin = contenido.indexOf("Folio");
        if (inicio > -1 && fin > -1 && inicio < fin) {
            String newcontenido = fn.eliminaSpacios(contenido.substring(inicio, fin).replace("@@@", "")).replace("\r\n", "").replace("FIA SCAl aDve", "FISCAL Clave");
            modelo.setAgente(newcontenido.split("Nombre del Agente")[1].split("###")[1].replace("Clave:", ""));

            if(newcontenido.contains(ConstantsValue.CLAVE)) {
                int sp = newcontenido.split(ConstantsValue.CLAVE)[1].split("###").length;
                if(sp == 3) {
                	  modelo.setCveAgente(newcontenido.split(ConstantsValue.CLAVE)[1].split("###")[2]);
                }
                if(sp == 2) {
                	  modelo.setCveAgente(newcontenido.split(ConstantsValue.CLAVE)[1].split("###")[1]);
                }
            }
            
            if(newcontenido.contains(CLAVE2)){
            	String[] arrContenido = newcontenido.split(CLAVE2)[1].split("###");
            	newcontenido = arrContenido.length>0? arrContenido[arrContenido.length-1]:"";
            	newcontenido = newcontenido.replace("\r","");
            	modelo.setCveAgente(newcontenido );
            	modelo.setAgente(modelo.getAgente().split(CLAVE2)[0].trim());
            
            }                           	               
        }
	}

}
