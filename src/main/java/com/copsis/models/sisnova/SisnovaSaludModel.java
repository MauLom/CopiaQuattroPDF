package com.copsis.models.sisnova;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.copsis.constants.ConstantsValue;
import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraAseguradosModel;
import com.copsis.models.EstructuraCoberturasModel;
import com.copsis.models.EstructuraJsonModel;

public class SisnovaSaludModel {
	
	// Clases
	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
	// Variables
	private String contenido = "";
	
	private String resultado = "";


	
	public SisnovaSaludModel(String contenido) {
		this.contenido = contenido;
		
	}
	
	public EstructuraJsonModel procesar() {
		 String newcontenido = "";
		 boolean direccion = true;
		 boolean calle = true;
		 int inicio;
		 int fin ;
		
		contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());
		contenido = contenido.replace("DATOS DE LOS ASEGURADOS", "Datos de los asegurados")
				.replace("PÓLIZA NO.", "PÓLIZA NO.").replace("FECHA DE EMISIÓN", "Fecha de emisión")
				.replace("NOMBRE Y DOMICILIO DEL CONTRATANTE", "Nombre y domicilio del contratante")
				.replace("DESCRIPCIÓN DE LA PÓLIZA", "Descripción de la póliza")
				.replace("VIGENCIA", "Vigencia").replace("PLAN", "Plan").replace("PRIMA", "Prima")
				.replace("MONEDA", "Moneda").replace("TOTAL", "Total").replace("NETA", "Neta")
				.replace("COBERTURAS AMPARADAS", "coberturas amparadas");
		

		try {

            modelo.setCia(11);
            modelo.setTipo(3);

               							   
            inicio = contenido.indexOf(ConstantsValue.POLIZA_NOM);
            fin = contenido.indexOf("Datos de los asegurados");
            if(inicio > fin) {
            	   inicio = contenido.indexOf("PÓLIZA NO:");
            }
    
            if (inicio > -1 && fin > -1 && inicio < fin) {
				newcontenido = contenido.substring(inicio, fin).replace("\r", "").replace("@", "")
						.trim().replaceAll(" +", " ").replaceAll("   ", " ").replaceAll("  ", " ").replaceAll("   ", " ");
				for (int i = 0; i < newcontenido.split("\n").length; i++) {
					if(newcontenido.split("\n")[i].contains(ConstantsValue.POLIZA_NOM)) {
						modelo.setPoliza(newcontenido.split("\n")[i].split(ConstantsValue.POLIZA_NOM)[1].trim().replace(" ", ""));
						modelo.setPolizaGuion(newcontenido.split("\n")[i].split(ConstantsValue.POLIZA_NOM)[1].trim().replace(" ", ""));
					}
					
					if(newcontenido.split("\n")[i].contains(ConstantsValue.POLIZA_NOPUNTOS)) {
						modelo.setPolizaGuion(newcontenido.split("\n")[i].split(ConstantsValue.POLIZA_NOPUNTOS)[1].trim().replace(" ", ""));
						modelo.setPoliza(modelo.getPolizaGuion().replace("-", ""));
					}

					if(newcontenido.split("\n")[i].contains("CP:")) {
						modelo.setCp(newcontenido.split("CP:")[1].substring(0,6).trim());
					}
					if(newcontenido.split("\n")[i].contains("Fecha de emisión")) {	
						
						if(newcontenido.split("\n")[i].split("Fecha de emisión")[1].replace("###", "").trim().replace(" ", "").replace("\u00A0","").split("-")[0].length() == 4 ) {
							modelo.setFechaEmision(newcontenido.split("\n")[i].split("Fecha de emisión")[1].replace("###", "").trim().replace(" ", "").replace("\u00A0",""));
						}else {
						modelo.setFechaEmision(fn.formatDateMonthCadena( newcontenido.split("\n")[i].split("Fecha de emisión")[1].replace("###", "").trim().replace(" ", "").replace("\u00A0","")));
						}
					}
					
					if(newcontenido.split("\n")[i].contains("Nombre") && newcontenido.split("\n")[i].contains("domicilio") && newcontenido.split("\n")[i].contains("RFC")) {
						modelo.setCteNombre(newcontenido.split("\n")[i+1].replace("###", "").replace("\u00A0","").trim());
						if(newcontenido.split("\n")[i+2].split("###").length == 2) {
							modelo.setRfc(newcontenido.split("\n")[i+2].split("###")[1].replace("###", "").trim().replace(" ", ""));	
						} 
						else if(newcontenido.split("\n")[i+4].split("###").length == 2) {
							modelo.setRfc(newcontenido.split("\n")[i+4].split("###")[1].replace("###", "").trim().replace(" ", ""));	
						}						
					}
					if(newcontenido.split("\n")[i].contains("Calle:") && calle) {
						resultado = newcontenido.split("\n")[i].split("Calle:")[1].split("###")[0];
						calle = false;
						modelo.setRfc(newcontenido.split("\n")[i].split("###")[newcontenido.split("\n")[i].split("###").length-1].replace("\u00A0","").trim());
					}
	                if(newcontenido.split("\n")[i].contains("Colonia:") && direccion) {
	                	resultado += " "+ newcontenido.split("\n")[i].split("Colonia:")[1];
	                	modelo.setCteDireccion(resultado.trim());
	                	direccion = false;
					}else if(newcontenido.split("\n")[i].contains("domicilio") && direccion) {
	                		resultado =  newcontenido.split("\n")[i+2] +" " + newcontenido.split("\n")[i+3];
	                		modelo.setCteDireccion(resultado.replace("LLEEOGNH8109053V9 NOMBRE Y DOMICILIO DEL TITULAR###RAMO", "").trim());
	               }	                  		               
				}
            }
    
            

         
            //ASEGURADOS           
            inicio = contenido.indexOf("Datos de los asegurados");
            fin = contenido.indexOf("Descripción de la póliza");
   
            if (inicio > 0 && fin > 0 && inicio < fin) {
            	List<EstructuraAseguradosModel> asegurados = new ArrayList<>();
				newcontenido = contenido.substring(inicio, fin).replace("\r", "").replace("@", "")
						.replaceAll("### ###", "###").replaceAll("  +", "").replace("  ", " ");
				for (int i = 0; i < newcontenido.split("\n").length; i++) {
					EstructuraAseguradosModel asegurado = new EstructuraAseguradosModel();
					if(newcontenido.split("\n")[i].split("-").length > 4) {					
						asegurado.setNombre(newcontenido.split("\n")[i].split("###")[1]);
						asegurado.setNacimiento(fn.formatDateMonthCadena(newcontenido.split("\n")[i].split("###")[2].replace(" ", "").trim()));
						asegurado.setAntiguedad(fn.formatDateMonthCadena(newcontenido.split("\n")[i].split("###")[3].trim()));
						asegurados.add(asegurado);
					}
				}
				modelo.setAsegurados(asegurados);
            }
            
           
            
            inicio = 0;
            fin = 0;
            //Primas           
            inicio = contenido.indexOf("Descripción de la póliza");
            fin = contenido.indexOf("Servicios Integrales de Salud");
	
       
            if (inicio > -1 && fin > -1 && inicio < fin) {
            	newcontenido = contenido.substring(inicio, fin).replace("\r", "").replaceAll("@@@", "").replaceAll("### ###", "###")
            		    .trim().replaceAll("\u00A0", " ")	.replaceAll(" +", "###").replaceAll("######", "###").replace("######", "###")
            			.replace("00:00", "").replace("hrs.", "").replace("#########", "###").replace("Desde", "Desde:").replace("las###12###Hrs###del###día", "").replace("Hasta", "Hasta:")
            			.replace("las###12###Hrs.###del###día###", "").replace("::", ":");
            	        //El caracter unicode
            	  
            	
            	for (int i = 0; i < newcontenido.split("\n").length; i++) {   
            		if(newcontenido.split("\n")[i].contains("Vigencia") && newcontenido.split("\n")[i].contains("Desde:") && newcontenido.split("\n")[i].contains("Hasta:")) {            		
            	
            			if(newcontenido.split("\n")[i].split("Desde:")[1].split("Hasta:")[0].replace("###", "").replace(" ", "").trim().split("-")[0].length() == 4) {
            				modelo.setVigenciaDe(newcontenido.split("\n")[i].split("Desde:")[1].split("Hasta:")[0].replace("###", "").replace(" ", "").trim());
                			modelo.setVigenciaA(newcontenido.split("\n")[i].split("Hasta:")[1].replace("###", "").replace(" ", "").trim());
            			}else {
            			modelo.setVigenciaDe(fn.formatDateMonthCadena(newcontenido.split("\n")[i].split("Desde:")[1].split("Hasta:")[0].replace("###", "").replace(" ", "").trim()));
            			modelo.setVigenciaA(fn.formatDateMonthCadena(newcontenido.split("\n")[i].split("Hasta:")[1].replace("###", "").replace(" ", "").trim()));
            			}
            		}
            		if(newcontenido.split("\n")[i].contains("Plan:") && newcontenido.split("\n")[i].contains("Asegurada:") ) {
            			modelo.setPlan(newcontenido.split("\n")[i].split("Plan:")[1].split("###")[1]);
            		} else if(newcontenido.split("\n")[i].contains("Plan:") ) {
            			modelo.setPlan(newcontenido.split("\n")[i].split("Plan:")[1].replace("###", " ").trim());
            		}
            		

            		if(newcontenido.split("\n")[i].contains("Prima") && newcontenido.split("\n")[i].contains("Neta") ) {
            			if(newcontenido.split("\n")[i+2].split("###").length == 4) {            	
            				modelo.setPrimaneta(fn.castBigDecimal(fn.castDouble(newcontenido.split("\n")[i+2].split("###")[3])));
                			modelo.setRecargo(fn.castBigDecimal(0.00));
                			modelo.setDerecho(fn.castBigDecimal(fn.castDouble(newcontenido.split("\n")[i+4].split("###")[2])));
                			modelo.setIva(fn.castBigDecimal(fn.castDouble(newcontenido.split("\n")[i+4].split("###")[3])));
            			}
            			if(newcontenido.split("\n")[i+2].split("###").length == 9) {
            				modelo.setPrimaneta(fn.castBigDecimal(fn.castDouble(newcontenido.split("\n")[i+2].split("###")[4])));
            				modelo.setRecargo(fn.castBigDecimal(fn.castDouble(newcontenido.split("\n")[i+2].split("###")[5])));
            				modelo.setDerecho(fn.castBigDecimal(fn.castDouble(newcontenido.split("\n")[i+2].split("###")[6])));
            				modelo.setIva(fn.castBigDecimal(fn.castDouble(newcontenido.split("\n")[i+2].split("###")[7])));
            			}
            			
            		}
            		if(newcontenido.split("\n")[i].contains("Prima") && newcontenido.split("\n")[i].contains("Total") && newcontenido.split("\n")[i].contains("Moneda") ) {
            			if(newcontenido.split("\n")[i+1].split("###").length == 6) {
            				modelo.setMoneda( fn.moneda( newcontenido.split("\n")[i+1].split("###")[1]));
                			modelo.setFormaPago( fn.formaPago( newcontenido.split("\n")[i+1].split("###")[2]));
                			modelo.setPrimaTotal(fn.castBigDecimal(fn.castDouble(newcontenido.split("\n")[i+1].split("###")[3])));
            			}
            			if(newcontenido.split("\n")[i+1].split("###").length == 5) {
            				modelo.setMoneda( fn.moneda( newcontenido.split("\n")[i+1].split("###")[0]));
                			modelo.setFormaPago( fn.formaPago( newcontenido.split("\n")[i+1].split("###")[1]));
                			modelo.setPrimaTotal(fn.castBigDecimal(fn.castDouble(newcontenido.split("\n")[i+1].split("###")[2])));
                			modelo.setPrimerPrimatotal(fn.castBigDecimal(fn.castDouble(newcontenido.split("\n")[i+1].split("###")[3])));
                			modelo.setSubPrimatotal(fn.castBigDecimal(fn.castDouble(newcontenido.split("\n")[i+1].split("###")[4])));
            			}
            			
            		}
            	}            	
            }
            
            if(modelo.getPrimaneta() == BigDecimal.ZERO && modelo.getRecargo() == BigDecimal.ZERO && modelo.getDerecho() == BigDecimal.ZERO) {
            	 inicio = contenido.indexOf("Prima básica");
                 fin = contenido.indexOf("Advertencia");
                
                 if (inicio > -1 && fin > -1 && inicio < fin) {
                	 newcontenido = contenido.substring(inicio, fin).replace("\r", "").replace("\u00A0","").replaceAll("@@@", "").replace("######", "###");
                		for (int i = 0; i < newcontenido.split("\n").length; i++) {   
                			if(newcontenido.split("\n")[i].contains("Prima") && newcontenido.split("\n")[i].contains("Neta") ) {                			                			                				
                				if(newcontenido.split("\n")[i+1].split("###").length == 8) {                			
                    				modelo.setPrimaneta(fn.castBigDecimal(fn.castDouble(newcontenido.split("\n")[i+1].split("###")[0])));
                    				modelo.setRecargo(fn.castBigDecimal(fn.castDouble(newcontenido.split("\n")[i+1].split("###")[4])));
                    				modelo.setDerecho(fn.castBigDecimal(fn.castDouble(newcontenido.split("\n")[i+1].split("###")[5])));
                    				modelo.setIva(fn.castBigDecimal(fn.castDouble(newcontenido.split("\n")[i+1].split("###")[6])));
                    			}
                				if(newcontenido.split("\n")[i+3].split("###").length == 5) {
                    				modelo.setMoneda( fn.moneda( newcontenido.split("\n")[i+3].split("###")[0]));
                        			modelo.setFormaPago( fn.formaPago( newcontenido.split("\n")[i+3].split("###")[1]));
                        			modelo.setPrimaTotal(fn.castBigDecimal(fn.castDouble(newcontenido.split("\n")[i+3].split("###")[2])));
                        			modelo.setPrimerPrimatotal(fn.castBigDecimal(fn.castDouble(newcontenido.split("\n")[i+3].split("###")[3])));
                        			modelo.setSubPrimatotal(fn.castBigDecimal(fn.castDouble(newcontenido.split("\n")[i+3].split("###")[4])));
                    			}
                			}
                		}
                 }
            }
            
    		
            inicio = contenido.indexOf("coberturas amparadas");
            fin = contenido.indexOf("PROMOCIONES");
            if(fin == -1) {
            	   fin = contenido.lastIndexOf("Servicios Integrales");
            }

            if (inicio > -1 && fin > -1 && inicio < fin) {
            	List<EstructuraCoberturasModel> coberturas = new ArrayList<>();
                newcontenido = "";
                newcontenido =contenido.substring(inicio, fin) .replace("\r", "").replace("\u00A0"," ").replace("@@@", "").replace("######", "###");

                for (String x : newcontenido.split("\n")) {
                    int sp = x.split("###").length;
                    if (!x.contains("Plan") && sp == 2) {                                            
                        	EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();
                            cobertura.setNombre(x.split("###")[0].trim());
                            cobertura.setDeducible(x.split("###")[1].trim());
                            coberturas.add(cobertura);                                          
                    	}
                    }
            	modelo.setCoberturas(coberturas);
            }
			
            if(modelo.getCoberturas().isEmpty()) {            
            	 inicio = contenido.indexOf("Descripción de la póliza");
                 fin = contenido.indexOf("Servicios Integrales de Salud");
                 if (inicio > -1 && fin > -1 && inicio < fin) {
                	 List<EstructuraCoberturasModel> coberturas = new ArrayList<>();
                	 newcontenido = contenido.substring(inicio, fin).replace("\r", "").replace("\u00A0"," ").replace("@@@", "").replace("######", "###");
                	 for (int i = 0; i < newcontenido.split("\n").length; i++) {   
                		 EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();
                		 if(newcontenido.split("\n")[i].contains("Plan:")) {
                			 modelo.setPlan(newcontenido.split("\n")[i].split("Plan:")[1].replace("###", ""));
                		 }
                		 if(newcontenido.split("\n")[i].contains("Póliza:")) {
                			 cobertura.setSa(newcontenido.split("\n")[i+1].split("###")[1].trim());
                			 cobertura.setDeducible(newcontenido.split("\n")[i+2].split("###")[1].trim());
                			 cobertura.setCoaseguro(newcontenido.split("\n")[i+3].split("###")[1].trim());
                			 cobertura.setCoaseguroTope(newcontenido.split("\n")[i+4].split("###")[1].trim());
                			 coberturas.add(cobertura);
                		 }
                		 if(newcontenido.split("\n")[i].contains("contratadas:")) {
                			 cobertura.setNombre(newcontenido.split("\n")[i+1].split("###")[0].trim());
                			 cobertura.setSa(newcontenido.split("\n")[i+1].split("###")[1].trim()); 
                			 cobertura.setCoaseguro(newcontenido.split("\n")[i+1].split("###")[2].trim());
                			 cobertura.setCoaseguroTope(newcontenido.split("\n")[i+1].split("###")[3].trim());
                			 coberturas.add(cobertura);
                		 }
                	 }  
                     modelo.setCoberturas(coberturas);
                 }      
            }
            
			
			
			return modelo;
		} catch (Exception ex) {
			modelo.setError(
					SisnovaSaludModel.this.getClass().getTypeName() + " - catch:" + ex.getMessage() + " | " + ex.getCause());
			return modelo;
		}
	}
	

}
