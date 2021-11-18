package com.copsis.models.inbursa;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraCoberturasModel;
import com.copsis.models.EstructuraJsonModel;

public class inbursaDiversosModel {
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
	private boolean iva = false;
	private BigDecimal restoPrimaTotal = BigDecimal.ZERO;
	private BigDecimal restoDerecho = BigDecimal.ZERO;
	private BigDecimal restoIva = BigDecimal.ZERO;
	private BigDecimal restoRecargo = BigDecimal.ZERO;
	private BigDecimal restoPrimaNeta = BigDecimal.ZERO;
	private BigDecimal restoAjusteUno = BigDecimal.ZERO;
	private BigDecimal restoAjusteDos = BigDecimal.ZERO;
	private BigDecimal restoCargoExtra = BigDecimal.ZERO;

	public inbursaDiversosModel(String contenido) {
		this.contenido = contenido;
	}

	public EstructuraJsonModel procesar() {
		contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());
		contenido = contenido.replace("DOLARES", "DÓLARES")
				.replace("R.F.C.", "R.F.C").replace("I.V.A.", "IVA")
				.replace("COBERTURAS CONTRATADAS", "SECCION###COBERTURAS#");
		try {
			 // tipo
	        modelo.setTipo(7);

	        // cia
	        modelo.setCia(35);
	        
		      inicio = contenido.indexOf("PÓLIZA DE SEGUROS");
	            fin = contenido.indexOf("COBERTURAS");
	            if(inicio > 0 &&  fin >  0 && inicio < fin) {
	            	newcontenido = contenido.substring(inicio, fin).replace("\r","").replace("@", "").replace("las 12:00 hrs. del", "");        
	            	for (int i = 0; i < newcontenido.split("\n").length; i++) {
						if(newcontenido.split("\n")[i].contains("PÓLIZA") && newcontenido.split("\n")[i].contains("CIS")  && newcontenido.split("\n")[i].contains("ID CLIENTE")) {
							modelo.setPoliza(newcontenido.split("\n")[i-1].split("###")[1]);
						}
						else if(newcontenido.split("\n")[i].contains("AGRUPACIÓN") && newcontenido.split("\n")[i].contains("PÓLIZA") && newcontenido.split("\n")[i].contains("CIS") ) {
	            			modelo.setPoliza(newcontenido.split("\n")[i+1].split("###")[2]);
	            			resultado = newcontenido.split("\n")[i+2]; 	            			
	            		}
	            		if(newcontenido.split("\n")[i].contains("C.P.") && newcontenido.split("\n")[i].contains("R.F.C")) {
	            			modelo.setCp(newcontenido.split("\n")[i].split("C.P.")[1].split("###")[0].trim());
	            			if(newcontenido.split("\n")[i+1].contains("PRIMA NETA")){
	            				modelo.setRfc(newcontenido.split("\n")[i+1].split("###")[0]);
	            			}else {
	            				modelo.setRfc(newcontenido.split("\n")[i+1].split("###")[1]);	
	            			}	            			
	            			resultado  += " " + newcontenido.split("\n")[i].split("R.F.C")[0]; 	   
	            		}
	            		
	            		if(newcontenido.split("\n")[i].contains("PRIMA") && newcontenido.split("\n")[i].contains("NETA") && newcontenido.split("\n")[i].contains("AGRUPACIÓN")) {	            			
	            			modelo.setPrimaneta(fn.castBigDecimal(fn.castDouble(  newcontenido.split("\n")[i+1].split("###")[newcontenido.split("\n")[i+1].split("###").length-1] )));
	            
	            		}	            		
	            		else if(newcontenido.split("\n")[i].contains("PRIMA NETA")) {
	            			if(newcontenido.split("\n")[i].split("###").length > 2) {
	            				resultado += " " + newcontenido.split("\n")[i].split("PRIMA NETA")[1].split(fn.extraerNumeros(newcontenido.split("\n")[i].split("PRIMA NETA")[1]))[1];	
	            				modelo.setPrimaneta(fn.castBigDecimal(fn.castDouble(fn.extraerNumeros(newcontenido.split("\n")[i].split("PRIMA NETA")[1]))));
	            			}else {	           
	            				modelo.setPrimaneta(fn.castBigDecimal(fn.castDouble(newcontenido.split("\n")[i].split("PRIMA NETA")[1].replace("###", ""))));
	            			}	            			            		
	            			modelo.setCteDireccion(resultado.replace("###", ""));	            		
	            		}
	            		//Datos de  la direccion alternativa
	            		if(newcontenido.split("\n")[i].contains("DIRECCIÓN:")) {
	            			String A="",B="",C="";
	            			 if(newcontenido.split("\n")[i+1].trim() .contains(".00")) {
	            				 A= newcontenido.split("\n")[i+1].split("SUMA")[0].trim();
	            			 }else {
	            				A= newcontenido.split("\n")[i+1].trim();
	            			 }
	            			 if(newcontenido.split("\n")[i+2].trim() .contains("R.F.C:")) {  
	            				B = newcontenido.split("\n")[i+2].split("R.F.C:")[0].trim();
	            			 }else {
	            				B = newcontenido.split("\n")[i+2].trim();
	            			 }
	            			 if(newcontenido.split("\n")[i+3].trim() .contains(".00")) {
	            				 if(newcontenido.split("\n")[i+3].split("###").length > 2) {
	            					 C =newcontenido.split("\n")[i+3].split("###")[0].trim();
	            				 }else {
	            					 C =newcontenido.split("\n")[i+3].split("C.P.")[0].trim();	 
	            				 }
	            				 
	            			 }else {
	            				 C =newcontenido.split("\n")[i+3].split("###")[0].trim();
	            			 }            			 
	            			String x = A +" "+ B +" "+  C;
	            			modelo.setCteDireccion(x.replace("###", "").replaceAll(modelo.getRfc(), ""));
	            		}
	            		
	            		if(newcontenido.split("\n")[i].contains("MONEDA") && newcontenido.split("\n")[i].contains("EXPEDICIÓN") ) {
	            			modelo.setMoneda( fn.moneda(newcontenido.split("\n")[i+1].split("###")[0] ));	            			
	            		} else if(newcontenido.split("\n")[i].contains("MONEDA")) {	     
	            			modelo.setMoneda( fn.moneda(newcontenido.split("\n")[i+1] ));
	            		}
	            		
	            		if(newcontenido.split("\n")[i].contains("FINANCIAMIENTO") && newcontenido.split("\n")[i].contains("R.F.C:") ) {
	               			modelo.setRecargo(fn.castBigDecimal(fn.castDouble(newcontenido.split("\n")[i+1].split("###")[newcontenido.split("\n")[i+1].split("###").length -1].replace("###", ""))));
	               		}
	            		else  if(newcontenido.split("\n")[i].contains("FINANCIAMIENTO")) {
	               			modelo.setRecargo(fn.castBigDecimal(fn.castDouble(newcontenido.split("\n")[i].split("FINANCIAMIENTO")[1].replace("###", ""))));
	               		}
	               		
	               		if(newcontenido.split("\n")[i].contains("DERECHO DE PÓLIZA")) {
	               			modelo.setDerecho(fn.castBigDecimal(fn.castDouble(newcontenido.split("\n")[i].split("DERECHO DE PÓLIZA")[1].replace("###", ""))));
	               		}
	               		if(newcontenido.split("\n")[i].contains("GASTO DE EXPEDICIÓN")) {
	               			modelo.setDerecho(fn.castBigDecimal(fn.castDouble(newcontenido.split("\n")[i+1].split("###")[1].replace("###", ""))));
	               		}
	               		if(newcontenido.split("\n")[i].contains("IVA") && newcontenido.split("\n")[i].contains("FORMA DE PAGO") && iva  == false) {
	               			modelo.setIva(fn.castBigDecimal(fn.castDouble(newcontenido.split("\n")[i].split("IVA")[1].split("FORMA DE PAGO")[0].replace("###", ""))));	               		
	               			if(newcontenido.split("\n")[i+1].split("###").length > 4) 
	               			{
	               				if(newcontenido.split("\n")[i+1].contains("PRIMA TOTAL") && newcontenido.split("\n")[i+1].contains("Horas") ) {
	               					String x = newcontenido.split("\n")[i+1].split("PRIMA TOTAL")[0];
	               					modelo.setFormaPago( fn.formaPago(x.split("###")[x.split("###").length -1]));
	               				}	               			
	               			}else {
	               				modelo.setFormaPago( fn.formaPago(newcontenido.split("\n")[i+2].split("###")[newcontenido.split("\n")[i+2].split("###").length -1]));	
	               			}
	               			iva =true;
	               		} else if(newcontenido.split("\n")[i].contains("IVA") && iva == false){
	               			modelo.setIva(fn.castBigDecimal(fn.castDouble(newcontenido.split("\n")[i].split("IVA")[1].split("###")[1].replace("###", ""))));	    
	               			iva = true;
	               		}
	               		if(modelo.getFormaPago() == 0) {
	               				if( newcontenido.split("\n")[i].contains("FORMA DE PAGO")) {
	               					modelo.setFormaPago( fn.formaPago(newcontenido.split("\n")[i+1].split("###")[0]));	
	               				}
	               		}
	               		
	               		if(newcontenido.split("\n")[i].contains("PRIMA TOTAL") && newcontenido.split("\n")[i].contains("Desde")) {
	               			if(newcontenido.split("\n")[i].split("-").length > 2) {
	               				modelo.setPrimaTotal(fn.castBigDecimal(fn.castDouble(fn.extraerNumeros(newcontenido.split("\n")[i].split("PRIMA TOTAL")[1].split("###")[1]).replace("###", ""))));
		               			if(modelo.getPrimaTotal().toString().length() > 0) {
		               				String x =newcontenido.split("\n")[i].split(fn.extraerNumeros(newcontenido.split("\n")[i].split("PRIMA TOTAL")[1].split("###")[1]).replace("###", ""))[1];
		               				modelo.setVigenciaDe(fn.formatDateMonthCadena(x.split("###")[0]));
			               			modelo.setVigenciaA(fn.formatDateMonthCadena(x.split("###")[1]));	               					                   		
		               			}
	               			}else {
	               				modelo.setPrimaTotal(fn.castBigDecimal(fn.castDouble(newcontenido.split("\n")[i].split("PRIMA TOTAL")[1].split("###")[1])));
	               			}
	               		
	               		} else   if(newcontenido.split("\n")[i].contains("PRIMA TOTAL") && newcontenido.split("\n")[i].contains("SUMA ASEGURADA")) {
	               			modelo.setPrimaTotal(fn.castBigDecimal(fn.castDouble(newcontenido.split("\n")[i+1].split("###")[newcontenido.split("\n")[i+1].split("###").length -1].replace("###", ""))));
	               		}
	               		else  if(newcontenido.split("\n")[i].contains("PRIMA TOTAL")) {
	               			modelo.setPrimaTotal(fn.castBigDecimal(fn.castDouble(newcontenido.split("\n")[i].split("PRIMA TOTAL")[1].replace("###", ""))));
	               		}	               		
	               		if(newcontenido.split("\n")[i].contains("Desde") && newcontenido.split("\n")[i].contains("Hasta")) {	               			
	               			if(newcontenido.split("\n")[i+1].split("###")[0].contains("-") ) {
	               				modelo.setVigenciaDe(fn.formatDateMonthCadena(newcontenido.split("\n")[i+1].split("###")[0]));
		               			modelo.setVigenciaA(fn.formatDateMonthCadena(newcontenido.split("\n")[i+1].split("###")[1]));
	               			}else {
	               				if(newcontenido.split("\n")[i].split("Horas")[1].split("Hasta")[0].contains("-")) {
	               					modelo.setVigenciaDe(fn.formatDateMonthCadena(newcontenido.split("\n")[i].split("Horas")[1].split("Hasta")[0].replace("###", "")));
		            				modelo.setVigenciaA(fn.formatDateMonthCadena(newcontenido.split("\n")[i].split("Hasta")[1].split("Horas")[1].replace("###", "").trim()));
	               				}
	            				
	               			}
	               			
	               		}
	               		
	               		if(newcontenido.split("\n")[i].contains("PRODUCTO") && newcontenido.split("\n")[i].contains("TIPO")) {
	               			modelo.setPlan(newcontenido.split("\n")[i].split("###")[1].replace("###", ""));
	               		}
	               		else if(newcontenido.split("\n")[i].contains("PRODUCTO")) {
	               			modelo.setPlan(newcontenido.split("\n")[i+1].split("###")[0].replace("###", ""));
	               		}
	            	}
	            }

	            
	            inicio = contenido.indexOf("SECCION###COBERTURAS#");
	            fin = contenido.indexOf("COBERTURAS###ADICIONALES");
	            if(fin == -1) {
	            	fin = contenido.indexOf("A###PARTIR");
	            	if(fin == -1) {
                            fin =contenido.indexOf("ZONA TERREMOTO");
                            if(fin == -1) {
                                fin =contenido.indexOf("ESPECIFICACIÓN DE CONDICIONES");
    	            	}
	            	}
	            }

	            String seccionALT="";
	            if(inicio > 0 &&  fin >  0 && inicio < fin) {
	            	List<EstructuraCoberturasModel> coberturas = new ArrayList<>();
	            	newcontenido = contenido.substring(inicio, fin).replace("\r","").replace("@", "").replace("las 12:00 hrs. del", "");        
	                String xcont = "";
	            	String seccion = "";
	            	for (int i = 0; i < newcontenido.split("\n").length; i++) {
	            		EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();
	            	
	            		if(newcontenido.split("\n")[i].contains("SECCION###COBERTURAS") || newcontenido.split("\n")[i].contains("Página")  
	            				|| newcontenido.split("\n")[i].contains(modelo.getPoliza()) ||  newcontenido.split("\n")[i].contains("PÓLIZA") 
	            				
	            				   ) {
	            			
	            		}else {
	            			int sp = newcontenido.split("\n")[i].split("###").length;
	            			
		            		if(sp >2) {
		            			seccion = newcontenido.split("\n")[i].split("###")[0].replace("SECCIÓN", "").replace("SECCIÓN", "").trim();
		            		}		             			             			             
							if (sp == 2) {								
								if(newcontenido.split("\n")[i].contains("SECCIÓN")) {
									seccion =newcontenido.split("\n")[i].split("###")[0].replace("SECCIÓN", "");
									cobertura.setSeccion(seccion);
									cobertura.setNombre(newcontenido.split("\n")[i].split("###")[1]);
									coberturas.add(cobertura);
								}else {
									cobertura.setSeccion(seccion);									
									cobertura.setNombre(newcontenido.split("\n")[i].split("###")[0].replace("SECCIÓN", ""));
									cobertura.setSa(newcontenido.split("\n")[i].split("###")[1]);
									coberturas.add(cobertura);
								}
								
							
							}
							if (sp == 3) {
								cobertura.setSeccion(seccion);
								cobertura.setNombre(newcontenido.split("\n")[i].split("###")[1]);
								cobertura.setSa(newcontenido.split("\n")[i].split("###")[2]);
								coberturas.add(cobertura);
							}
							if (sp == 4) {
								cobertura.setSeccion(seccion);
								cobertura.setNombre(newcontenido.split("\n")[i].split("###")[1]);
								cobertura.setSa(newcontenido.split("\n")[i].split("###")[2]);
								coberturas.add(cobertura);
							}
	            			
	            		}
	            		
	            		
	            	}
	            	 modelo.setCoberturas(coberturas);
	            }
	            
	        
	            
	            

			 return modelo;
		} catch (Exception ex) {
			modelo.setError(
					inbursaDiversosModel.this.getClass().getTypeName() + " - catch:" + ex.getMessage() + " | " + ex.getCause());
			return modelo;
		}
	}

}
