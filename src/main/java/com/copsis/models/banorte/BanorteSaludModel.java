package com.copsis.models.banorte;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraAseguradosModel;
import com.copsis.models.EstructuraJsonModel;

public class BanorteSaludModel {
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
	
	public BanorteSaludModel(String contenido, String recibos) {
		this.contenido = contenido;
		this.recibosText = recibos;
	}
	
	public EstructuraJsonModel procesar() {
		contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());
		contenido = contenido.replace("I.V.A.", "I.V.A")
				.replace("N o m b r e y A p e l l i d o C o m p l e t o", "Nombre y Apellido Completo")
				.replace("Si### el### contenido", "Si el contenido");
		try {
			 // tipo
            modelo.setTipo(3);

            // cia
            modelo.setCia(35);
            

           
           //Datos Generales
           inicio = contenido.indexOf("GASTOS MÉDICOS MAYORES");
           fin = contenido.indexOf("NOMBRE DEL ASEGURADO");

			if (inicio > 0 && fin > 0 && inicio < fin) {
				newcontenido = contenido.substring(inicio, fin).replace("\r", "").replace("@", "");
				for (int i = 0; i < newcontenido.split("\n").length; i++) {
                    if(newcontenido.split("\n")[i].contains("CONTRATANTE") && newcontenido.split("\n")[i].contains("PÓLIZA")) {                   
                    	if(newcontenido.split("\n")[i+1].contains("apellido")) {
                    		modelo.setPoliza( newcontenido.split("\n")[i+1].split("###")[newcontenido.split("\n")[i+1].split("###").length -1].trim());
                           	modelo.setCteNombre(newcontenido.split("\n")[i+2].split("###")[newcontenido.split("\n")[i+2].split("###").length -4].replace("10", "").trim());              
                    	}else if(newcontenido.split("\n")[i+1].contains("Apellido")) {
                    		modelo.setPoliza( newcontenido.split("\n")[i+1].split("###")[newcontenido.split("\n")[i+1].split("###").length -1].trim());
                           	String x =newcontenido.split("\n")[i+1].split("###")[newcontenido.split("\n")[i+1].split("###").length -4].replace("Nombre y Apellido Completo :", "").trim();
                    		modelo.setCteNombre(x);                             
                    	}
                    	else {                    	
                    	    modelo.setPoliza( newcontenido.split("\n")[i+1].split("###")[newcontenido.split("\n")[i+1].split("###").length -1].trim());
                            

                    	    if(newcontenido.split("\n")[i+1].split("###")[newcontenido.split("\n")[i+1].split("###").length -3].length() > 20) {
                    	    	modelo.setCteNombre(newcontenido.split("\n")[i+1].split("###")[newcontenido.split("\n")[i+1].split("###").length -3].replace("10", "").trim());	
                    	    }else {
                    	    	modelo.setCteNombre(newcontenido.split("\n")[i+1].split("###")[newcontenido.split("\n")[i+1].split("###").length -4].replace("10", "").trim());
                    	    	
                    	    }
                    	                  	
                        }
                   }
                    
                    if(newcontenido.split("\n")[i].contains("AGENTE")){                    	
                    	if(newcontenido.split("\n")[i+1].contains("Domicilio:")) {  
                    		if(newcontenido.split("\n")[i+1].split("###").length > 2 ) {
                    			modelo.setCveAgente( newcontenido.split("\n")[i+1].split("Domicilio:")[1].split("MATRIZ")[0].replace("###", ""));                    		
                    		}else {
                    			modelo.setCveAgente( newcontenido.split("\n")[i+2].split("###")[1].replace("###", ""));
                    		}                    		                    		
                    	}                    	
                    }
                    
                    if(newcontenido.split("\n")[i].contains("C.P.")){                    	
                    	if(newcontenido.split("\n")[i].split("C.P.")[1].split("###").length > 1) {                    	
                    		modelo.setCp(newcontenido.split("\n")[i].split("C.P.")[1].split("###")[0]);
                    	}else {
                    		modelo.setCp(newcontenido.split("\n")[i].split("C.P.")[1].split(",")[0]);
                    	}
                    	 
                    }
                    if(newcontenido.split("\n")[i].contains("EMISIÓN")){  
                    	if(modelo.getCp().length() == 0) {
                    		if(fn.isvalidCp(newcontenido.split("\n")[i+1].split("###")[0])) {
                    			modelo.setCp(newcontenido.split("\n")[i+1].split("###")[0]);	
                    		}
                    		
                    	}
                    }
                    if(newcontenido.split("\n")[i].contains("PLAN")){ 
                 
                    	if( newcontenido.split("\n")[i+1].contains("PESOS") ||  newcontenido.split("\n")[i+1].contains("NACIONAL")) {
                    		modelo.setPlan( newcontenido.split("\n")[i+1].split("###")[newcontenido.split("\n")[i+1].split("###").length-1]);
                    	}else if( newcontenido.split("\n")[i+2].contains("NACIONAL")) {
                    		String valor=newcontenido.split("\n")[i+2].replace(" ", "###");
                    		modelo.setPlan( valor.split("###")[valor.split("###").length-1]);
                    	}
                    	
                    }
                   
                    if(newcontenido.split("\n")[i].contains("Domicilio:")){
                    
                    	 if(newcontenido.split("\n")[i+1].contains("MONEDA")) {
                    		 resultado = newcontenido.split("\n")[i+1].split("MONEDA")[0].replace("###", "");	 
                    	 }else {                    		 
                    		 resultado = newcontenido.split("\n")[i+1].split("###")[0];	 
                    	 } 
                    	 if(newcontenido.split("\n")[i+2].contains("C.P.")) {
                    		
                    		 resultado += " "+ newcontenido.split("\n")[i+2].split("C.P.")[1];	 
                    	
                    	 } else if(newcontenido.split("\n")[i+2].contains("MONEDA")) {
                    		resultado += " "+ newcontenido.split("\n")[i+2].split("MONEDA")[0].replace("###", "");
                    	 }else {
                    		 resultado += " "+ newcontenido.split("\n")[i+2];
                    		 
                    	 }
                    	 
                    	 
                    	 if(newcontenido.split("\n")[i+3].contains("NACIONAL")) {
                    		 resultado += newcontenido.split("\n")[i+3].split("NACIONAL")[0];  
                    		 modelo.setMoneda(1);
                    	
                    		 if(newcontenido.split("\n")[i+3].split("NACIONAL")[1].replace("TOTAL", "").trim().split("###").length > 2) {
                    			 modelo.setFormaPago(fn.formaPago(newcontenido.split("\n")[i+3].split("NACIONAL")[1].split("###")[1].replace("TOTAL", "").trim() ));
                    		 }else {
                    			 modelo.setFormaPago(fn.formaPago(newcontenido.split("\n")[i+3].split("NACIONAL")[1].replace("TOTAL", "").trim() ));	 
                    		 }
                    		 
                    	 }
                    	 if(newcontenido.split("\n")[i+3].contains("PESOS")) {
                    		 resultado += " "+ newcontenido.split("\n")[i+3].split("PESOS")[0].replace("###", "");  
                    		 modelo.setMoneda(1);
                    		 modelo.setFormaPago(fn.formaPago(newcontenido.split("\n")[i+3].split("PESOS")[1].split("###")[1].replace("###", "").trim() ));
                    	 }
                    	 modelo.setCteDireccion(resultado.replace("###", "").trim());                    	
                    }
                    

                    if(newcontenido.split("\n")[i].contains("R.F.C:") && newcontenido.split("\n")[i].contains("VIGENCIA")){
                    	modelo.setRfc(newcontenido.split("\n")[i].split("R.F.C:")[1].split("VIGENCIA")[0].replace("###", "").trim());;
                    }
                    
                    if(newcontenido.split("\n")[i].contains("R.F.C:") && newcontenido.split("\n")[i].contains("DESDE")){
                    	modelo.setRfc(newcontenido.split("\n")[i].split("R.F.C:")[1].split("DESDE")[0].replace("###", "").trim());;
                    }
                    
                    if(newcontenido.split("\n")[i].contains("Electrónico:")){                    
                    	if(newcontenido.split("\n")[i].split("-").length > 1) {
                    	 resultado = newcontenido.split("\n")[i].split("Electrónico:")[1].strip().trim().replace(" ", "###");
                    	 if(resultado.split("###")[0].contains("-")) {
                    		 modelo.setVigenciaDe(fn.formatDate_MonthCadena( resultado.split("###")[0]));
                        	 modelo.setVigenciaA(fn.formatDate_MonthCadena( resultado.split("###")[1])); 
                    	 }else {
                    		 modelo.setVigenciaDe(fn.formatDate_MonthCadena( resultado.split("###")[1]));
                        	 modelo.setVigenciaA(fn.formatDate_MonthCadena( resultado.split("###")[2]));
                    	 }
                    	
                    	}
                    }
                    if(newcontenido.split("\n")[i].contains("R.F.C:") && newcontenido.split("\n")[i].split("-").length > 3){
						if (newcontenido.split("\n")[i].split("###")[1].contains("-")) {
						     modelo.setRfc(newcontenido.split("\n")[i].split("###")[0].replace("R.F.C:", "").trim());							
							 resultado = newcontenido.split("\n")[i].split("###")[1].trim().replace(" ", "###");
							 modelo.setVigenciaDe(fn.formatDate_MonthCadena( resultado.split("###")[0]));
	                    	 modelo.setVigenciaA(fn.formatDate_MonthCadena( resultado.split("###")[1]));
						}else {
							modelo.setRfc(newcontenido.split("\n")[i].split("###")[1]);
							 modelo.setVigenciaDe(fn.formatDate_MonthCadena( newcontenido.split("\n")[i].split("###")[2]));
	                    	 modelo.setVigenciaA(fn.formatDate_MonthCadena( newcontenido.split("\n")[i].split("###")[3]));  
						}                    	                     	           
                    }    
                    
                    if(modelo.getVigenciaA().length() == 0 && modelo.getVigenciaDe().length() == 0 ) {
    					if (newcontenido.split("\n")[i].split("-").length > 3) {
    	                     String x = newcontenido.split("\n")[i].trim().replace(" ", "###");
    						 modelo.setVigenciaDe(fn.formatDate_MonthCadena( x.split("###")[0]));
	                    	 modelo.setVigenciaA(fn.formatDate_MonthCadena( x.split("###")[1]));
    					}
    				}
				}
				
			}
			

			inicio = contenido.indexOf("Clave del Agente:");
			fin = contenido.indexOf("En testimonio de");
			if (inicio > 0 && fin > 0 && inicio < fin) {
				newcontenido = contenido.substring(inicio, fin).replace("\r", "").replace("@", "");
				for (int i = 0; i < newcontenido.split("\n").length; i++) {
					if(newcontenido.split("\n")[i].contains("Clave del Agente:")) {
						modelo.setAgente(newcontenido.split("\n")[i+1].split(modelo.getCveAgente())[0].trim() );
					}
				}
			}
			

			inicio = contenido.indexOf("Prima Neta");
			fin = contenido.indexOf("Si el contenido");
		

			if (inicio > 0 && fin > 0 && inicio < fin) {
				newcontenido = contenido.substring(inicio, fin).replace("\r", "").replace("@", "");
				for (int i = 0; i < newcontenido.split("\n").length; i++) {
				  if(newcontenido.split("\n")[i].contains("Prima Neta")) {
					  modelo.setPrimaneta(fn.castBigDecimal(fn.castDouble(newcontenido.split("\n")[i].split("Prima Neta Anual")[1].split("###")[1].trim())));
				  }
				  if(newcontenido.split("\n")[i].contains("Fraccionado") && newcontenido.split("\n")[i].contains("Especificaciones")) {
					  modelo.setRecargo(fn.castBigDecimal(fn.castDouble(newcontenido.split("\n")[i].split("Fraccionado")[1].replace("###", "").trim())));
				  }
				  if(newcontenido.split("\n")[i].contains("Financiamiento") && newcontenido.split("\n")[i].contains("Especificaciones")) {
					  modelo.setRecargo(fn.castBigDecimal(fn.castDouble(newcontenido.split("\n")[i].split("Pago")[1].replace("Fraccionado", "").replace("###", "").trim())));
				  }
				  
				  
				  
				  if(newcontenido.split("\n")[i].contains("Generales") && newcontenido.split("\n")[i].contains("Expedición")) {
					  modelo.setDerecho(fn.castBigDecimal(fn.castDouble(newcontenido.split("\n")[i].split("Póliza")[1].replace("###", "").trim())));
				  }
				  if(newcontenido.split("\n")[i].contains("Identificación") && newcontenido.split("\n")[i].contains("Expedición")) {
					  modelo.setDerecho(fn.castBigDecimal(fn.castDouble(newcontenido.split("\n")[i].split("Póliza")[1].replace("###", "").trim())));
				  }
				  
				  if(newcontenido.split("\n")[i].contains("Pago") && newcontenido.split("\n")[i].contains("I.V.A")) {
					  modelo.setIva(fn.castBigDecimal(fn.castDouble(newcontenido.split("\n")[i].split("I.V.A")[1].replace("###", "").trim())));
				  }
				  
				  if(newcontenido.split("\n")[i].contains("Endosos") && newcontenido.split("\n")[i].contains("Total")) {					
					  modelo.setPrimaTotal(fn.castBigDecimal(fn.castDouble(newcontenido.split("\n")[i].split("Total")[1].replace("###", "").trim())));
				  }	else   if(newcontenido.split("\n")[i].contains("Total")) {					
					  modelo.setPrimaTotal(fn.castBigDecimal(fn.castDouble(newcontenido.split("\n")[i].split("Total")[1].replace("###", "").trim())));
				  }
				}

			}
	          
			if(modelo.getVigenciaDe().length() >  0) {
				modelo.setFechaEmision(modelo.getVigenciaDe());
			}
			
			//PROCESO DE ASEGURADOS
		     inicio = contenido.indexOf("NOMBRE DEL ASEGURADO");
	           fin = contenido.indexOf("seguro de gastos médicos,");

				if (inicio > 0 && fin > 0 && inicio < fin) {
					List<EstructuraAseguradosModel> asegurados = new ArrayList<>();
					newcontenido = contenido.substring(inicio, fin).replace("\r", "").replace("@", "");
					for (int i = 0; i < newcontenido.split("\n").length; i++) {
						EstructuraAseguradosModel asegurado = new EstructuraAseguradosModel();
						if(newcontenido.split("\n")[i].split("-").length > 2 ) {							
							if(newcontenido.split("\n")[i].split("###")[1].contains("-")) {
								asegurado.setNacimiento(fn.formatDate_MonthCadena( newcontenido.split("\n")[i].split("###")[1]));
								asegurado.setNombre(newcontenido.split("\n")[i].split("###")[0]);
							}else {
								String x=newcontenido.split("\n")[i].split("###")[0].replace(" ", "###");
								asegurado.setNacimiento(fn.formatDate_MonthCadena( x.split("###")[x.split("###").length -1]));
								asegurado.setNombre(newcontenido.split("\n")[i].split("###")[0].split(x.split("###")[x.split("###").length -1])[0]);
							}							
							asegurados.add(asegurado);												
						}						
					}
					modelo.setAsegurados(asegurados);
				}
						
           
            return modelo;
		} catch (Exception ex) {
			modelo.setError(
					BanorteSaludModel.this.getClass().getTypeName() + " - catch:" + ex.getMessage() + " | " + ex.getCause());
			return modelo;
		}
		
	}
	
}
