package com.copsis.models.inbursa;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraCoberturasModel;
import com.copsis.models.EstructuraJsonModel;
import com.copsis.models.banorte.BanorteAutosModel;

public class InbursaAutosModel {
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
	private BigDecimal restoPrimaTotal = BigDecimal.ZERO;
	private BigDecimal restoDerecho = BigDecimal.ZERO;
	private BigDecimal restoIva = BigDecimal.ZERO;
	private BigDecimal restoRecargo = BigDecimal.ZERO;
	private BigDecimal restoPrimaNeta = BigDecimal.ZERO;
	private BigDecimal restoAjusteUno = BigDecimal.ZERO;
	private BigDecimal restoAjusteDos = BigDecimal.ZERO;
	private BigDecimal restoCargoExtra = BigDecimal.ZERO;
	
	public InbursaAutosModel(String contenido, String recibos) {
		this.contenido = contenido;
		this.recibosText = recibos;
	}
	public EstructuraJsonModel procesar() {
		contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());
		    contenido = contenido.replace("IVA", "I.V.A:")
		    		.replace("ACTUAL", "RENOVACION");
		try {
			 // tipo
            modelo.setTipo(1);

            // cia
            modelo.setCia(38);
            

            //Datos del Contratante
            inicio = contenido.indexOf("PÓLIZA DE SEGUROS");
            fin = contenido.indexOf("COBERTURAS CONTRATADAS");
            if(inicio > 0 &&  fin >  0 && inicio < fin) {
            	newcontenido = contenido.substring(inicio, fin).replace("\r","").replace("@", "").replace("las 12:00 horas", "");        
            	for (int i = 0; i < newcontenido.split("\n").length; i++) {
            		
            		if(newcontenido.split("\n")[i].contains("DATOS DEL CONTRATANTE")) {
            			if(newcontenido.split("\n")[i+1].contains("NOMBRE:")) {
            				modelo.setCteNombre(newcontenido.split("\n")[i+2].split("###")[0]);
            			}else {            				
            				modelo.setCteNombre(newcontenido.split("\n")[i+1].split("###")[0]);		
            			}            		
            		}
            		if(newcontenido.split("\n")[i].contains("PÓLIZA") && newcontenido.split("\n")[i].contains("CIS")  && newcontenido.split("\n")[i].contains("ID CLIENTE")) {
            			modelo.setPoliza(newcontenido.split("\n")[i-1].split("###")[1]);
            		}
            		else if(newcontenido.split("\n")[i].contains("PÓLIZA") && newcontenido.split("\n")[i].contains("CIS")  && newcontenido.split("\n")[i].contains("Cliente Inbursa")) {
            			modelo.setPoliza(newcontenido.split("\n")[i-1].split("###")[1]);
            		}  else if(newcontenido.split("\n")[i].contains("PÓLIZA") && newcontenido.split("\n")[i].contains("FAMILIA")) {
            			modelo.setPoliza(newcontenido.split("\n")[i+1].split("###")[0]);
            		}
            		//proceso direccion
            		if(newcontenido.split("\n")[i].contains("R.F.C.")) {
            			resultado =newcontenido.split("\n")[i].split("R.F.C.")[0];
            		}
            		if(newcontenido.split("\n")[i].contains("PRIMA") && newcontenido.split("\n")[i].contains("NETA") && newcontenido.split("\n")[i].contains("AGRUPACIÓN")) {
            			//debe entra para no colocar nada
            		}else if(newcontenido.split("\n")[i].contains("PRIMA") && newcontenido.split("\n")[i].contains("NETA")) {
            			resultado += " " + newcontenido.split("\n")[i].split("PRIMA")[0] +" "+  newcontenido.split("\n")[i+1] ;
            			modelo.setCteDireccion(resultado.replace("###", "").trim() );
            		} 
            		
            		if(newcontenido.split("\n")[i].contains("DIRECCIÓN:")) {
            			String A="",B="",C="";
            			 if(newcontenido.split("\n")[i+2].trim() .contains(".00")) {
            				 A= newcontenido.split("\n")[i+1].split("SUMA")[0].trim();
            			 }else {
            				A= newcontenido.split("\n")[i+2].trim();
            			 }
            			 if(newcontenido.split("\n")[i+3].trim() .contains("R.F.C:")) {  
            				B = newcontenido.split("\n")[i+3].split("R.F.C:")[0].trim();
            			 }else {
            				B = newcontenido.split("\n")[i+3].trim();
            			 }
            			 if(newcontenido.split("\n")[i+6].trim() .contains(".00")) {
            				 C =newcontenido.split("\n")[i+4].split("C.P.")[0].trim();
            			 }else {
            				 C =newcontenido.split("\n")[i+6].trim();
            			 }            			 
            			String x = A +" "+ B +" "+  C;
            			modelo.setCteDireccion(x.replace("###", ""));
            		}
            		           		
            		if(newcontenido.split("\n")[i].contains("C.P.")) {
            			if(newcontenido.split("\n")[i].split("C.P.")[1].split("###").length > 0) {
            				modelo.setCp(newcontenido.split("\n")[i].split("C.P.")[1].split("###")[0].trim());
            			}else {
            				modelo.setCp(newcontenido.split("\n")[i].split("C.P.")[1].trim());
            			}            		
            		}            	
            		else  if(newcontenido.split("\n")[i].contains("MONEDA")) {
            			if(newcontenido.split("\n")[i+1].contains("###")) {
            				modelo.setMoneda(fn.moneda(newcontenido.split("\n")[i+1].split("###")[0].replace("###", "").trim()));
            			}else {
            				modelo.setMoneda(fn.moneda(newcontenido.split("\n")[i+1].replace("###", "").trim()));	
            			}            		
            		}
            		//primas
            		 if(newcontenido.split("\n")[i].contains("PRIMA NETA:") && newcontenido.split("\n")[i].contains("AGRUPACIÓN")) {  
            			 modelo.setPrimaneta(fn.castBigDecimal(fn.castDouble( newcontenido.split("\n")[i+1].split("###")[1])));
            		 }
            		 else if(newcontenido.split("\n")[i].contains("PRIMA NETA:")) {  
            			 modelo.setPrimaneta(fn.castBigDecimal(fn.castDouble(fn.extraerNumeros(newcontenido.split("\n")[i].split("PRIMA NETA:")[1].replace("###", "").substring(0,13)))));
            		 }
            		 if(newcontenido.split("\n")[i].contains("FINANCIAMIENTO:") && newcontenido.split("\n")[i].contains("R.F.C:")) {  
            			 modelo.setDerecho(fn.castBigDecimal(fn.castDouble(newcontenido.split("\n")[i+2].split("###")[1].replace("###", ""))));
            		 }
            		 else if(newcontenido.split("\n")[i].contains("FINANCIAMIENTO:")) {  
            			 modelo.setDerecho(fn.castBigDecimal(fn.castDouble(newcontenido.split("\n")[i].split("FINANCIAMIENTO:")[1].replace("###", ""))));
            		 }
            		 if(newcontenido.split("\n")[i].contains("EXPEDICIÓN:") && newcontenido.split("\n")[i].contains("MONEDA:")) {  
            			 modelo.setRecargo(fn.castBigDecimal(fn.castDouble(newcontenido.split("\n")[i+1].split("###")[1].replace("###", ""))));
            		 }
            		 else if(newcontenido.split("\n")[i].contains("EXPEDICIÓN:")) {  
            			 modelo.setRecargo(fn.castBigDecimal(fn.castDouble(newcontenido.split("\n")[i].split("EXPEDICIÓN:")[1].replace("###", ""))));
            		 }
             		 if(newcontenido.split("\n")[i].contains("I.V.A:") && newcontenido.split("\n")[i].contains("PAGO")) {  
            			 modelo.setIva(fn.castBigDecimal(fn.castDouble(newcontenido.split("\n")[i+1].split("###")[1].replace("###", ""))));
            				modelo.setFormaPago(fn.formaPago(newcontenido.split("\n")[i+1].split("###")[0].trim()));
            		 }
             		 else if(newcontenido.split("\n")[i].contains("I.V.A:")) {  
            			 modelo.setIva(fn.castBigDecimal(fn.castDouble(newcontenido.split("\n")[i].split("I.V.A:")[1].replace("###", ""))));
            		 }
             		if(newcontenido.split("\n")[i].contains("PRIMA TOTAL:") && newcontenido.split("\n")[i].contains("DOCUMENTO")) {  
           			 modelo.setPrimaTotal(fn.castBigDecimal(fn.castDouble(newcontenido.split("\n")[i+1].split("###")[newcontenido.split("\n")[i+1].split("###").length -1].replace("###", ""))));
           		    }
             		else if(newcontenido.split("\n")[i].contains("PRIMA TOTAL:")) {  
            			 modelo.setPrimaTotal(fn.castBigDecimal(fn.castDouble(newcontenido.split("\n")[i].split("PRIMA TOTAL:")[1].replace("###", ""))));
            		 }
             		 if(newcontenido.split("\n")[i].contains("Desde") && newcontenido.split("\n")[i].contains("Hasta") && newcontenido.split("\n")[i].contains("-")) { 
             			modelo.setVigenciaDe(fn.formatDateMonthCadena(newcontenido.split("\n")[i].split("Desde")[1].split("Hasta")[0].replace("###", "").trim()));
             			modelo.setVigenciaA(fn.formatDateMonthCadena(newcontenido.split("\n")[i].split("Hasta")[1].split("RENOVACION")[0].replace("###", "").trim()));
             		 }
             		 else if(newcontenido.split("\n")[i].contains("Desde") && newcontenido.split("\n")[i].contains("Hasta")) {  
            			modelo.setVigenciaDe(fn.formatDateMonthCadena(newcontenido.split("\n")[i+1].split("###")[0]));
            			modelo.setVigenciaA(fn.formatDateMonthCadena(newcontenido.split("\n")[i+1].split("###")[1]));
            			modelo.setFormaPago(fn.formaPago(newcontenido.split("\n")[i+1].split("###")[2].trim()));
            		 }            		 
            		 if(newcontenido.split("\n")[i].contains("CLAVE") && newcontenido.split("\n")[i].contains("ISIS:")) {
            			 modelo.setClave(newcontenido.split("\n")[i].split("ISIS:")[1].split(" ")[0].replace("MARCA:", "").trim().replace("###", "") );
            			 if(modelo.getClave().length() > 1) {
                  			modelo.setDescripcion(newcontenido.split("\n")[i].split(modelo.getClave())[1].trim());            				 
            			 }
             		 }
            		 if(newcontenido.split("\n")[i].contains("MODELO:") && newcontenido.split("\n")[i].contains("PLACAS:")){
            			 modelo.setModelo(fn.castInteger(newcontenido.split("\n")[i].split("MODELO:")[1].split("PLACAS")[0].replace("###", "").trim()));
            		 }
            		 else if(newcontenido.split("\n")[i].contains("MODELO:")){            			
            			 modelo.setModelo(fn.castInteger(newcontenido.split("\n")[i].split("MODELO:")[1].replace("###", "").trim()));
            		 }
            		 if(newcontenido.split("\n")[i].contains("PLACAS:")){
            			 if(newcontenido.split("\n")[i].split("PLACAS")[1].length() > 2) {
            				 modelo.setPlacas(newcontenido.split("\n")[i].split("PLACAS:")[1].replace("###", "").trim());	 
            			 }            			 
            		 }
            		 if(newcontenido.split("\n")[i].contains("SERIE:") && newcontenido.split("\n")[i].contains("NÚMERO")){            			
            			 modelo.setSerie(newcontenido.split("\n")[i].split("SERIE:")[1].split("NÚMERO")[0].replace("###", "").trim());
            		 }
            		 else if(newcontenido.split("\n")[i].contains("SERIE:")){            			
            			 modelo.setSerie(newcontenido.split("\n")[i].split("SERIE:")[1].replace("###", "").trim());
            		 }
            		 if(newcontenido.split("\n")[i].contains("MOTOR:")){
            			 if(newcontenido.split("\n")[i].split("MOTOR")[1].length() > 2) {
            				 modelo.setMotor(newcontenido.split("\n")[i].split("MOTOR:")[1].replace("###", "").trim());	 
            			 }            			 
            		 }            		 
            		 if(newcontenido.split("\n")[i].contains("PROPIETARIO:")){            			
            			 modelo.setConductor(newcontenido.split("\n")[i].split("PROPIETARIO:")[1].replace("###", "").trim());
            		 }  
            		 if(newcontenido.split("\n")[i].contains("PRODUCTO:") && newcontenido.split("\n")[i].contains("PAGO")){            			
            			 modelo.setPlan(newcontenido.split("\n")[i].split("PRODUCTO:")[1].split("FORMA")[0].replace("###", "").trim());
            		 } 
            	      else if(newcontenido.split("\n")[i].contains("PRODUCTO")){            			
            			 modelo.setPlan(newcontenido.split("\n")[i+1].split("###")[0].replace("###", "").trim());
            		 } 
            	}
            }

            
            
            /*Agente y Cve*/

            inicio = contenido.indexOf("NOMBRE DEL AGENTE");
            
            if(inicio > 0) {
            	newcontenido = contenido.split("NOMBRE DEL AGENTE")[0];
            	if(newcontenido.length() > 200) {
            		newcontenido = newcontenido.substring(newcontenido.length()-200,newcontenido.length()).replace("@@@", "").replace("\r", "");
                    for (int j = 0; j < newcontenido.split("\n").length; j++) {    
   
                    	if(newcontenido.split("\n")[j].contains("CLAVE")) {                   
                    		modelo.setCveAgente(fn.extraerNumeros( newcontenido.split("\n")[j-2]));
                    		if(modelo.getCveAgente().length() > 0) {
                    			String A = newcontenido.split("\n")[j-1].replace(" ", "###").split("###")[0].trim();
                    			 if(A.contains("@")) {
                    				 A ="";
                    			 }
                    		  modelo.setAgente((newcontenido.split("\n")[j-2].split( modelo.getCveAgente() )[1] +""+ A).trim());	
                    		}                                        			
                    	}
					} 
            	}            	             
            }
            
            
            
            
            inicio = contenido.indexOf("COBERTURAS CONTRATADAS");
            fin = contenido.indexOf("En caso de Siniestro");
            if(fin == -1) {
            	  fin = contenido.indexOf("AVISO IMPORTANTE");
            	   if(fin == -1) {
            		   fin = contenido.indexOf("OPERAN COMO");
            	   }
            }
 
          
            if(inicio > 0 &&  fin >  0 && inicio < fin) {
            	List<EstructuraCoberturasModel> coberturas = new ArrayList<>();
            	newcontenido = contenido.substring(inicio, fin).replace("\r","").replace("@", "").replace("las 12:00 horas", "");
            	for (int i = 0; i < newcontenido.split("\n").length; i++) {            		
          		EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();
            	  if(newcontenido.split("\n")[i].contains("COBERTURAS CONTRATADAS") || newcontenido.split("\n")[i].contains("MÍNIMO")
            		|| newcontenido.split("\n")[i].contains("Cobertura") || newcontenido.split("\n")[i].contains("Deducible") || newcontenido.split("\n")[i].contains("**")
            		|| newcontenido.split("\n")[i].contains("UMA") || newcontenido.split("\n")[i].contains("OPERAN")) {
            	  }else {
            		 int sp =newcontenido.split("\n")[i].split("###").length;     
            		 if(newcontenido.split("\n")[i].split("###")[0].length() > 3) {
            				cobertura.setNombre(newcontenido.split("\n")[i].split("###")[0]);
    						if (sp > 1) {
    							cobertura.setSa(newcontenido.split("\n")[i].split("###")[1]);
    						}
    						if (sp > 3) {
    							cobertura.setDeducible(newcontenido.split("\n")[i].split("###")[2]);
    						}
    						coberturas.add(cobertura);            				
                	  }
            		 }				
            	}
            	modelo.setCoberturas(coberturas);
            }
			
			
			return modelo;
		} catch (Exception ex) {
			modelo.setError(
					InbursaAutosModel.this.getClass().getTypeName() + " - catch:" + ex.getMessage() + " | " + ex.getCause());
			return modelo;
		}
	}

}
