package com.copsis.models.banorte;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraCoberturasModel;
import com.copsis.models.EstructuraJsonModel;
import com.copsis.models.EstructuraRecibosModel;

public class BanorteAutosModel {
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
	private BigDecimal restoPrimaTotal = BigDecimal.ZERO;
	private BigDecimal restoDerecho = BigDecimal.ZERO;
	private BigDecimal restoIva = BigDecimal.ZERO;
	private BigDecimal restoRecargo = BigDecimal.ZERO;
	private BigDecimal restoPrimaNeta = BigDecimal.ZERO;
	private BigDecimal restoAjusteUno = BigDecimal.ZERO;
	private BigDecimal restoAjusteDos = BigDecimal.ZERO;
	private BigDecimal restoCargoExtra = BigDecimal.ZERO;
	
	public BanorteAutosModel(String contenido, String recibos) {
		this.contenido = contenido;
		this.recibosText = recibos;
	}
		
		public EstructuraJsonModel procesar() {
			contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());
			contenido = contenido.replace("DATOS DEL VEHYÍ CDULO", "DATOS DEL VEHÍCULO").replace("Calle y Número", "Calle y No:")
					.replace("Tel:", "Teléfono:").replace("neta", "Neta")
					.replace("Inicio de vigencia", " Inicio Vigencia").replace("Fin de vigencia", "Fin Vigencia")
					.replace("Derecho de póliza", "Derecho de Póliza")
					.replace("Emisión", "emisión").replace("Moneda", "Moneda:").replace("IVA", "I.V.A:")
					.replace("N o m b r e d e l C o n t r a t a n t e :", "Nombre del Contratante:")
					.replace("12:00hrs", "")
					.replace("habitual:", "Habitual:")
					.replace("Prima total:", "Prima Total:");
			this.recibosText =  fn.remplazarMultiple(this.recibosText,fn.remplazosGenerales());
			try {
				
				 // tipo
	            modelo.setTipo(1);

	            // cia
	            modelo.setCia(35);
	            
	            //Poliza
	            inicio = contenido.indexOf("PÓLIZA DE SEGURO");
	            fin = contenido.indexOf("DATOS DEL VEHÍCULO");
	            if(inicio > 0 &&  fin >  0 && inicio < fin) {
	            	newcontenido = contenido.substring(inicio, fin).replace("\r","").replace("@", "");
	        
	            	for (int i = 0; i < newcontenido.split("\n").length; i++) {
	            		
						if(newcontenido.split("\n")[i].contains("Póliza") &&  newcontenido.split("\n")[i].contains("Subramo")) {
							int sp =newcontenido.split("\n")[i+1].split("###")[0].length();							
							if(sp == 4) {
								modelo.setPoliza(newcontenido.split("\n")[i+1].split("###")[1]);
							}else {
								modelo.setPoliza(newcontenido.split("\n")[i+1].split("###")[0]);	
							}							
							modelo.setInciso(fn.castInteger(newcontenido.split("\n")[i+1].split("###")[newcontenido.split("\n")[i].split("###").length -1]));						
						}
						
						if(newcontenido.split("\n")[i].contains("Contratante:") && newcontenido.split("\n")[i].contains("R.F.C:")){							
							modelo.setCteNombre(newcontenido.split("\n")[i].split("Contratante:")[1].split("R.F.C:")[0].replace("###", "").replace("_", "").replace("  ", ""));
							modelo.setRfc(newcontenido.split("\n")[i].split("R.F.C:")[1].replace("###", "").replace("_", "").replace("  ", "").trim());
						}
						
						if(newcontenido.split("\n")[i].contains("Calle")){
							resultado = newcontenido.split("\n")[i].split("Calle")[1].replace("y No:", "").replace("###", "");
						}
						if(newcontenido.split("\n")[i].contains("Colonia") && newcontenido.split("\n")[i].contains("Municipio")){
							resultado +=  " "+  (newcontenido.split("\n")[i].split("Colonia:")[1].split("Municipio:")[0].replace("Población-", "")
								+"  "+	newcontenido.split("\n")[i].split("Municipio:")[1]).replace("###", "");
							
						modelo.setCteDireccion( (resultado.replace("_", "").replace("  ", "").replace("y número:", "") +"  " +newcontenido.split("\n")[i+1].split("Estado:")[1].split("Teléfono:")[0].replace("###", "").replace("_", "").replace("  ", "")).trim());
						modelo.setCp(newcontenido.split("\n")[i+1].split("C.P:")[1].split("Estado")[0].replace("###", "").replace("_", "").replace("  ", "").trim());
						
						}						
						if(newcontenido.split("\n")[i].contains("Conductor")){
							modelo.setConductor( newcontenido.split("\n")[i].split("Habitual:")[1].replace("###", "").replace("_", "").replace("  ", "").trim());
						}
						//
						//primas
						if(newcontenido.split("\n")[i].contains("emisión:") && newcontenido.split("\n")[i].contains("Neta:")){
					
							modelo.setFechaEmision(fn.formatDateMonthCadena((newcontenido.split("\n")[i].split("emisión:")[1].split("Prima")[0]).replace("###", "").trim()));
						    modelo.setPrimaneta(fn.castBigDecimal(fn.castDouble(newcontenido.split("\n")[i].split("Neta:")[1].replace("###", ""))));
						}
					    if(newcontenido.split("\n")[i].contains("Inicio Vigencia:") && newcontenido.split("\n")[i].contains("Reducción:")){
					    	modelo.setVigenciaDe(fn.formatDateMonthCadena((newcontenido.split("\n")[i].split("Vigencia:")[1].split("Reducción")[0]).replace("###", "").replace("12:00 hrs", "").trim()));
						    				
						}
                        if(newcontenido.split("\n")[i].contains("Fin Vigencia:") && newcontenido.split("\n")[i].contains("Recargo:")){
                        	modelo.setVigenciaA(fn.formatDateMonthCadena((newcontenido.split("\n")[i].split("Vigencia:")[1].split("Recargo")[0]).replace("###", "").replace("12:00 hrs", "").trim()));
                        	modelo.setRecargo(fn.castBigDecimal(fn.castDouble(newcontenido.split("\n")[i].split("Recargo:")[1].split("###")[newcontenido.split("\n")[i].split("Recargo:")[1].split("###").length -1].replace("###", ""))));	
						}
                   
                        
                        if(newcontenido.split("\n")[i].contains("Moneda:") && newcontenido.split("\n")[i].contains("Derecho de Póliza:")){ 
							modelo.setMoneda(fn.moneda(newcontenido.split("\n")[i].split("Moneda:")[1].split("Derecho")[0].replace("###", "").replace(":", "").trim()));
							modelo.setDerecho(fn.castBigDecimal(fn.castDouble(newcontenido.split("\n")[i].split("Póliza:")[1].replace("###", ""))));
						}
                        
						if(newcontenido.split("\n")[i].contains("pago:") && newcontenido.split("\n")[i].contains("I.V.A:")){
							modelo.setFormaPago(fn.formaPago(newcontenido.split("\n")[i].split("pago:")[1].split("Impuesto")[0].replace("###", "").replace(":", "").trim()));
							modelo.setIva(fn.castBigDecimal(fn.castDouble(newcontenido.split("\n")[i].split("I.V.A:")[1].split("###")[newcontenido.split("\n")[i].split("I.V.A:")[1].split("###").length -1].replace("###", "").trim())));						
						}
						 if(newcontenido.split("\n")[i].contains("Prima Total:")) {
					
							 modelo.setPrimaTotal(fn.castBigDecimal(fn.castDouble(newcontenido.split("\n")[i].split("Prima Total:")[1].replace("###", ""))));
						 }
					}
	            }
	            

	            inicio = recibosText.indexOf("AVISO DE COBRO");
	            fin = recibosText.indexOf("Derecho Póliza");

	            if(inicio > 0 &&  fin >  0 && inicio < fin) {
	            	newcontenido = recibosText.substring(inicio, fin).replace("\r","").replace("@", "");
	            	for (int i = 0; i < newcontenido.split("\n").length; i++) {	
	            		if(newcontenido.split("\n")[i].contains("Contratante:") &&  newcontenido.split("\n")[i].contains("Agente:")) {
	            			modelo.setAgente(newcontenido.split("\n")[i].split("Agente:")[1].replace("###", "").trim());
	            		}
	            		if(newcontenido.split("\n")[i].contains("Clave del Agente:") &&  newcontenido.split("\n")[i].contains("Oficina:")) {
	            			modelo.setCveAgente(newcontenido.split("\n")[i].split("Clave del Agente:")[1].split("Oficina")[0].replace("###", "").trim());
	            		}
	            	}
	            }
	            
	            if (modelo.getAgente().length() == 0 && modelo.getCveAgente().length() == 0) {
	                inicio = contenido.indexOf("AVISO DE COBRO");
		            fin = contenido.indexOf("Derecho Póliza");
		            if(inicio > 0 &&  fin >  0 && inicio < fin) {
		            	newcontenido = contenido.substring(inicio, fin).replace("\r","").replace("@", "");
		            	for (int i = 0; i < newcontenido.split("\n").length; i++) {	
		            		if(newcontenido.split("\n")[i].contains("Contratante:") &&  newcontenido.split("\n")[i].contains("Agente:")) {
		            			modelo.setAgente(newcontenido.split("\n")[i].split("Agente:")[1].replace("###", "").trim());
		            		}
		            		if(newcontenido.split("\n")[i].contains("Clave del Agente:") &&  newcontenido.split("\n")[i].contains("Oficina:")) {
		            			modelo.setCveAgente(newcontenido.split("\n")[i].split("Clave del Agente:")[1].split("Oficina")[0].replace("###", "").trim());
		            		}
		            	}
		            }
				}
	            

	            //Informacion del vehiculo
	            inicio = contenido.indexOf("DATOS DEL VEHÍCULO");
	            fin = contenido.indexOf("No. Pedimento:");

	            if(inicio > 0 &&  fin >  0 && inicio < fin) {
	            	newcontenido = contenido.substring(inicio, fin).replace("\r","").replace("@", "");
	            	for (int i = 0; i < newcontenido.split("\n").length; i++) {	
	            		if(newcontenido.split("\n")[i].contains("Descripción:")) {
	            			modelo.setDescripcion(newcontenido.split("\n")[i].split("Descripción:")[1].replace("###", "").replace("_", "").replace("  ", "").trim());
	            		}
	            		if(newcontenido.split("\n")[i].contains("Clave") && newcontenido.split("\n")[i].contains("Marca:")&& newcontenido.split("\n")[i].contains("Modelo:")) {
		            		modelo.setClave(newcontenido.split("\n")[i].split("Clave")[1].split("Marca")[0].replace("SB:", "").replace("###", "").replace("_", "").replace("  ", "").trim());
		            		modelo.setMarca((newcontenido.split("\n")[i].split("Marca:")[1].split("Capacidad")[0]).replace("###", "").replace("_", "").replace("  ", "").trim());
		            		modelo.setModelo(fn.castInteger((newcontenido.split("\n")[i].split("Modelo:")[1].split("Transmisión")[0]).replace("_","").replace("###", "").trim()));
	            		}
	            		if(newcontenido.split("\n")[i].contains("Cilindros") && newcontenido.split("\n")[i].contains("Marca:")&& newcontenido.split("\n")[i].contains("Modelo:")) {
	            			modelo.setMarca((newcontenido.split("\n")[i].split("Marca:")[1].split("Capacidad")[0]).replace("###", ""));
		            		modelo.setModelo(fn.castInteger((newcontenido.split("\n")[i].split("Modelo:")[1].split("Marca")[0]).replace("###", "")));
	            		}
	            		
	            		if(newcontenido.split("\n")[i].contains("Categoría:") && newcontenido.split("\n")[i].contains("Marca:")&& newcontenido.split("\n")[i].contains("Modelo:")) {
	            	
	            			modelo.setMarca((newcontenido.split("\n")[i].split("Marca:")[1].split("Categoría:")[0]).replace("###", ""));
		            		modelo.setModelo(fn.castInteger((newcontenido.split("\n")[i].split("Modelo:")[1].split("Marca")[0]).replace("###", "")));
	            		}
	            			            		
	            		if(newcontenido.split("\n")[i].contains("Placas:") && newcontenido.split("\n")[i].contains("Serie:")){
	            			modelo.setPlacas(newcontenido.split("\n")[i].split("Placas:")[1].split("Serie")[0].replace("###", "").replace("_", "").replace("  ", "").trim());
	            			modelo.setSerie(newcontenido.split("\n")[i].split("Serie:")[1].replace("###", "").replace("_", "").replace("  ", "").trim());
	            		}	            		
	            		if(newcontenido.split("\n")[i].contains("Motor:") && newcontenido.split("\n")[i].contains("Serie:")){	            	
	            			modelo.setSerie(newcontenido.split("\n")[i].split("Serie:")[1].split("Motor")[0].replace("###", "").replace("_", "").replace("  ", "").trim());
	            		}
	            		if(newcontenido.split("\n")[i].contains("Carrocería:") && newcontenido.split("\n")[i].contains("Placas:")){	            	
	            			modelo.setPlacas(newcontenido.split("\n")[i].split("Placas:")[1].replace("###", ""));
	            		}
	            	}	            
	            }
//	            //COBERTURAS
	            inicio = contenido.indexOf("DETALLES DE COBERTURAS");
	            if(inicio == -1) {
	            	 inicio = contenido.indexOf("DETALLE COBERTURAS"); 	
	            }
	           
	            fin = contenido.indexOf("La Unidad de Medida ");
	            if(fin == -1){
	            	fin =contenido.indexOf("La Compañía podrá en cualquier");
	            }

	            if(inicio > 0 &&  fin >  0 && inicio < fin) {
	            	List<EstructuraCoberturasModel> coberturas = new ArrayList<>();
	            	newcontenido = contenido.substring(inicio, fin).replace("\r","").replace("@", "").trim();
	            	for (int i = 0; i < newcontenido.split("\n").length; i++) {	 
	            		EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();
	            		if(newcontenido.split("\n")[i].contains("DETALLES")  || newcontenido.split("\n")[i].contains("PAQUETE")  || newcontenido.split("\n")[i].contains("Coberturas") 
	            				|| newcontenido.split("\n")[i].contains("Primas") || newcontenido.split("\n")[i].contains("COBERTURAS")	) {	            			
	            		}else {	
	            			if( newcontenido.split("\n")[i].split("###").length > 1) {	            			
	            				cobertura.setNombre(newcontenido.split("\n")[i].split("###")[0]);
	            				if(newcontenido.split("\n")[i].split("###").length == 4) {
	            					cobertura.setDeducible(newcontenido.split("\n")[i].split("###")[newcontenido.split("\n")[i].split("###").length -2]);
		            				cobertura.setSa(newcontenido.split("\n")[i].split("###")[newcontenido.split("\n")[i].split("###").length -3]);	
	            				}else {
	            					cobertura.setDeducible(newcontenido.split("\n")[i].split("###")[newcontenido.split("\n")[i].split("###").length -1]);
		            				cobertura.setSa(newcontenido.split("\n")[i].split("###")[newcontenido.split("\n")[i].split("###").length -2]);	            					
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
					}
				
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
						BanorteAutosModel.this.getClass().getTypeName() + " - catch:" + ex.getMessage() + " | " + ex.getCause());
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
					    	recibo.setPrimaneta(fn.castBigDecimal(fn.preparaPrimas(x.split("Prima Neta")[1].split("Recargos")[0].split("###")[1])));
					    
					    	recibo.setRecargo(fn.castBigDecimal(fn.preparaPrimas(x.split("Recargos")[1].split("\n")[0].replace("###", ""))));
					    	
					    	 recibosLis.add(recibo);
					    }
					    if(x.contains("Serie:")){
					    	recibo.setSerie(x.split("Serie:")[1].split("Folio")[0].replace("###", ""));
					    }
					    
					    if(x.contains("IVA")){					 
					    	recibo.setIva(fn.castBigDecimal(fn.preparaPrimas(x.split("IVA")[1].split(":")[1].split("\n")[0].replace("###", ""))));
					    } 
					    if(x.contains("Prima Total")){
					    	recibo.setDerecho(fn.castBigDecimal(fn.preparaPrimas(x.split("Derecho Póliza")[1].split("Prima")[0].split("\n")[0].replace("###", ""))));
					    	recibo.setPrimaTotal(fn.castBigDecimal(fn.preparaPrimas(x.split("Prima Total")[1].split("\n")[0].replace("###", ""))));
					    } 
					   
					}			
				}
				return (ArrayList<EstructuraRecibosModel>) recibosLis;
			} catch (Exception ex) {
				modelo.setError(BanorteAutosModel.this.getClass().getTypeName() + " | " + ex.getMessage() + " | "
						+ ex.getCause());

				return (ArrayList<EstructuraRecibosModel>) recibosLis;
			}
		}
		
		

}
