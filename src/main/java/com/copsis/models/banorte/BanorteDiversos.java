package com.copsis.models.banorte;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraCoberturasModel;
import com.copsis.models.EstructuraJsonModel;
import com.copsis.models.EstructuraRecibosModel;

public class BanorteDiversos {
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
		
		public BanorteDiversos(String contenido,String recibos) {
			this.contenido = contenido;
			this.recibosText = recibos;
		}
		public EstructuraJsonModel procesar() {
			contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());
			contenido = contenido.replace("R ESPONSABILIDAD CIVIL GENERAL", " RESPONSABILIDAD CIVIL GENERAL")
					.replace("C RISTALES", "CRISTALES").replace("R OBO DE BIENES", "ROBO DE BIENES")
					.replace("D INERO Y VALORES", "DINERO Y VALORES").replace("EQ UIPO ELECTRONICO", "EQUIPO ELECTRONICO")
			        .replace("SE RVICIOS DE ASISTENCIA","SERVICIOS DE ASISTENCIA")
			        .replace("R IESGOS HIDROMETEOROLOGICOS COASEGURO SEGUN ZONA"," RIESGOS HIDROMETEOROLOGICOS COASEGURO SEGUN ZONA")
			        .replace("S.E.A ", "###S.E.A ")
			        .replace("Fraccionado", "fraccionado")
			        .replace("hasta a las 12 hrs:", "Hasta las 12 hrs:")
			        .replace("Expreso","###Expreso")
			        .replace("Dentro","Dentro###")
			        .replace("SUBLIMITE DEL ", "SUBLIMITE DEL###")
			        .replace("Fuera SUBLIMITE", "Fuera SUBLIMITE###")
			        .replace("Asalto", "Asalto###")
			        
			        ;
			recibosText = fn.remplazarMultiple(recibosText, fn.remplazosGenerales());
			
			try {
				 // tipo
	            modelo.setTipo(7);

	            // cia
	            modelo.setCia(35);
	            

	            
	            //Poliza
	            inicio = contenido.indexOf("PÓLIZA DE SEGURO");
	            fin = contenido.indexOf("DATOS DE LAS COBERTURAS");
	            if(inicio > 0 &&  fin >  0 && inicio < fin) {
	            	newcontenido = contenido.substring(inicio, fin).replace("\r","").replace("@", "").replace("A las 12 hrs desde:", "")
	            			.replace("Hasta las 12 hrs:", "");	       
	            	for (int i = 0; i < newcontenido.split("\n").length; i++) {
	            	
	            		
	            		if(newcontenido.split("\n")[i].contains("PÓLIZA") && newcontenido.split("\n")[i].contains("OFICINA")) {
	            			modelo.setPoliza(newcontenido.split("\n")[i+1].split("###")[0].replace("-", ""));
	            			modelo.setPolizaGuion(newcontenido.split("\n")[i+1].split("###")[0]);
	            		}
	            		if(newcontenido.split("\n")[i].contains("Nombre") && newcontenido.split("\n")[i].contains("Social:") && newcontenido.split("\n")[i].contains("RFC:")) {
	            			modelo.setCteNombre(newcontenido.split("\n")[i].split("Social:")[1].split("RFC:")[0].replace("###", "").trim());
	            		}
	            		else if(newcontenido.split("\n")[i].contains("Nombre") && newcontenido.split("\n")[i].contains("Social:")) {
	            			modelo.setCteNombre(newcontenido.split("\n")[i].split("Social:")[1].trim());
	            		}
	            		
	            		if(newcontenido.split("\n")[i].contains("Calle") && newcontenido.split("\n")[i].contains("Número:")) {
	            			resultado = newcontenido.split("\n")[i].split("Número:")[1];
	            		}
	            		if(newcontenido.split("\n")[i].contains("Población") && newcontenido.split("\n")[i].contains("Municipio:")) {
	            			resultado += " "+ newcontenido.split("\n")[i].split("Municipio:")[1];
	            			
	            		}
                       if(newcontenido.split("\n")[i].contains("Colonia") && newcontenido.split("\n")[i].contains("RFC")) {
                    	   resultado += " "+ newcontenido.split("\n")[i].split("Colonia:")[1].split("RFC")[0];
                    	   modelo.setCteDireccion(resultado.replace("Estado:", "").replace(" Código Postal:", "").replace("###",""));
	            		}
                       if(newcontenido.split("\n")[i].contains("Emisión:") ) {
                    	   modelo.setFechaEmision(fn.formatDate_MonthCadena(newcontenido.split("\n")[i].split("Emisión:")[1].trim()));
                       }
                       if(newcontenido.split("\n")[i].contains("Moneda:") && newcontenido.split("\n")[i].contains("Vigencia") ) {
                    	   modelo.setMoneda(fn.moneda(newcontenido.split("\n")[i].split("Moneda:")[1].trim()));
                    	   modelo.setVigenciaDe(fn.formatDate_MonthCadena(newcontenido.split("\n")[i+1].split("###")[0].trim()));
                    	   modelo.setVigenciaA(fn.formatDate_MonthCadena(newcontenido.split("\n")[i+1].split("###")[1].trim()));
                    	   
                       }
                        if(newcontenido.split("\n")[i].contains("Pago:") ) {
                        	 modelo.setFormaPago(fn.formaPago(newcontenido.split("\n")[i].split("Pago:")[1].trim()));
                        
                       }
	                    if(newcontenido.split("\n")[i].contains("Código Postal:") ) {
	                    	modelo.setCp(newcontenido.split("\n")[i].split("Código Postal:")[1]);
	                    }
	                    if(newcontenido.split("\n")[i].contains("Prima Neta") ) {
	                    	if(newcontenido.split("\n")[i+1].contains("fraccionado")) {
	                    		String x = newcontenido.split("\n")[i+2];
	                    		modelo.setPrimaneta(fn.castBigDecimal(fn.castFloat(x.split("###")[0])));
	                    		modelo.setDerecho(fn.castBigDecimal(fn.castFloat(x.split("###")[1])));
	                    		modelo.setRecargo(fn.castBigDecimal(fn.castFloat(x.split("###")[2])));
	                    		modelo.setIva(fn.castBigDecimal(fn.castFloat(x.split("###")[3])));
	                    		modelo.setPrimaTotal(fn.castBigDecimal(fn.castFloat(x.split("###")[4])));
	                    	}
	                    	
	                    }
	            	}
	            }
	            

	            inicio = contenido.indexOf("nombre del Agente");
	            fin = contenido.indexOf("En cumplimiento");
	        
	            if(inicio > 0 &&  fin >  0 && inicio < fin) {
	            	newcontenido = contenido.substring(inicio, fin).replace("\r","").replace("@", "").trim();
	            	for (int i = 0; i < newcontenido.split("\n").length; i++) {
	            		if(newcontenido.split("\n")[i].contains("Agente:")) {	   
	            			if(newcontenido.split("\n")[i].contains("-")) {
                                    modelo.setCveAgente(newcontenido.split("\n")[i].split("Agente:")[1].split("-")[0].trim());
                                    modelo.setAgente(newcontenido.split("\n")[i].split("Agente:")[1].split("-")[1].trim());
	            			}
	            			
	            		}
	          
	            	}
	            	
	            }
	            
	            if(modelo.getVigenciaDe().length() > 0) {
	            	modelo.setFechaEmision(modelo.getVigenciaDe());
	            }
	            
	            
	            /**/
	            
	            inicio = contenido.indexOf("DATOS DE LAS COBERTURAS");
	            fin = contenido.indexOf("Seguros Banorte");
	            if(inicio > fin) {
	            	fin = contenido.indexOf("contrato de Seguros:");
	            }
	            
	            
	            if(inicio > 0 &&  fin >  0 && inicio < fin) {
	            	List<EstructuraCoberturasModel> coberturas = new ArrayList<>();
	            	newcontenido = contenido.substring(inicio, fin).replace("\r","").replace("@", "").trim();
	            	for (int i = 0; i < newcontenido.split("\n").length; i++) {
	            		EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();  	            		
	            		if(newcontenido.split("\n")[i].split("###").length > 1) {
	            			if(newcontenido.split("\n")[i].contains("IN CENDIO") || newcontenido.split("\n")[i].contains("SECCIÓN") ) {	            				
	            			}else {
	            				cobertura.setNombre( newcontenido.split("\n")[i].split("###")[0]);
	            				cobertura.setSa( newcontenido.split("\n")[i].split("###")[1]);
	            				if( newcontenido.split("\n")[i].split("###").length > 2) {
	            					cobertura.setDeducible( newcontenido.split("\n")[i].split("###")[2]);	
	            				}	            				
	            				coberturas.add(cobertura);
	            			}
	            			
	            		}
	            	}
	            	modelo.setCoberturas(coberturas); 
	             }
	            
	            List<EstructuraRecibosModel> recibosList = new ArrayList<>();    	    
				EstructuraRecibosModel recibo = new EstructuraRecibosModel();
				
				
				
				
	            
	            
	            if (recibosText.length() > 0) {
					recibosList = recibosExtract();
					
				}
	            switch (modelo.getFormaPago()) {
				case 1:
					if(recibosList.size() ==  0) {
						recibo.setReciboId("");
						recibo.setSerie("1/1");
						recibo.setVigenciaDe(modelo.getVigenciaDe());
						recibo.setVigenciaA(modelo.getVigenciaA());
						if (recibo.getVigenciaDe().length() > 0) {
							recibo.setVencimiento(fn.dateAdd(recibo.getVigenciaDe(), 30, 1));
						}
						recibo.setPrimaneta(modelo.getPrimaneta());
						recibo.setDerecho(modelo.getDerecho());
						recibo.setRecargo(modelo.getRecargo());
						recibo.setIva(modelo.getIva());
						recibo.setPrimaTotal(modelo.getPrimaTotal());
						recibo.setAjusteUno(modelo.getAjusteUno());
						recibo.setAjusteDos(modelo.getAjusteDos());
						recibo.setCargoExtra(modelo.getCargoExtra());
						recibosList.add(recibo);					
					}else {
						
						int tota_recibos =fn.getTotalRec(modelo.getFormaPago());
						if(recibosList.size() > tota_recibos) {
							for (int i = 0; i < recibosList.size(); i++) {					      
								if(i >= tota_recibos) {							
									recibosList.remove(i--);							
								}				
							}
						}				
						break;
		        	}
		        
		        	modelo.setRecibos(recibosList);
		          
					
				
					break;
				case 2:
					if (recibosList.size() == 1) {
						recibo.setSerie("2/2");
						recibo.setVigenciaDe(recibosList.get(0).getVigenciaA());
						recibo.setVigenciaA(modelo.getVigenciaA());
						recibo.setVencimiento("");
						recibo.setPrimaneta(restoPrimaNeta);
						recibo.setPrimaTotal(restoPrimaTotal);
						recibo.setRecargo(restoRecargo);
						recibo.setDerecho(restoDerecho);
						recibo.setIva(restoIva);
						recibo.setAjusteUno(restoAjusteUno);
						recibo.setAjusteDos(restoAjusteDos);
						recibo.setCargoExtra(restoCargoExtra);
						recibosList.add(recibo);

					}
					break;
				case 3:
				case 4:									
					int tota_recibos =fn.getTotalRec(modelo.getFormaPago());
					if(recibosList.size() > tota_recibos) {
						for (int i = 0; i < recibosList.size(); i++) {					      
							if(i >= tota_recibos) {							
								recibosList.remove(i--);							
							}				
						}
					}				
					break;
	        	}
	        
	        	modelo.setRecibos(recibosList);
	            			
	            
	            
				
				return modelo;
			} catch (Exception ex) {
				modelo.setError(
						BanorteDiversos.this.getClass().getTypeName() + " - catch:" + ex.getMessage() + " | " + ex.getCause());
				return modelo;
			}
			
		}
		
		private ArrayList<EstructuraRecibosModel> recibosExtract() {
			recibosText = fn.fixContenido(recibosText);
			List<EstructuraRecibosModel> recibosLis = new ArrayList<>();

			try {
				for (int i = 0; i < recibosText.split("AVISO DE COBRO").length; i++) {
			
					if(recibosText.split("AVISO DE COBRO")[i].contains("Serie:")) {

						EstructuraRecibosModel recibo = new EstructuraRecibosModel();
					String x = recibosText.split("AVISO DE COBRO")[i].split("Importe con Letra")[0];

					    if(x.contains("Prima Neta") && x.contains("Recargos")) {
					    	recibo.setPrimaneta(fn.castBigDecimal(fn.cleanString(x.split("Prima Neta")[1].split("Recargos")[0].replace(":", "").split("###")[1])));
					    
					    	recibo.setRecargo(fn.castBigDecimal(fn.cleanString(x.split("Recargos")[1].split("\n")[0].replace(":", "").replace("###", ""))));
					    	
					    	 recibosLis.add(recibo);
					    }
					    if(x.contains("Serie:")){
					    	recibo.setSerie(x.split("Serie:")[1].split("Folio")[0].replace("###", ""));
					    }
					    
					    if(x.contains("IVA")){					 
					    	recibo.setIva(fn.castBigDecimal(fn.cleanString(x.split("IVA")[1].replace("%", "").split(":")[1].split("\n")[0].replace("###", ""))));
					    } 
					    if(x.contains("Prima Total")){
					    	recibo.setDerecho(fn.castBigDecimal(fn.cleanString(x.split("Derecho Póliza")[1].split("Prima")[0].replace(":", "").split("\n")[0].replace("###", ""))));
					    	recibo.setPrimaTotal(fn.castBigDecimal(fn.cleanString(x.split("Prima Total")[1].split("\n")[0].replace(":", "").replace("###", ""))));
					    } 
					   
					}			
				}
				return (ArrayList<EstructuraRecibosModel>) recibosLis;
			} catch (Exception ex) {
				modelo.setError("recibosExtract ==> " +BanorteDiversos.this.getClass().getTypeName() + " | " + ex.getMessage() + " | "
						+ ex.getCause());

				return (ArrayList<EstructuraRecibosModel>) recibosLis;
			}
		}


}
