package com.copsis.models.primero;

import java.util.ArrayList;
import java.util.List;

import com.copsis.constants.ConstantsValue;
import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraCoberturasModel;
import com.copsis.models.EstructuraJsonModel;
import com.copsis.models.EstructuraRecibosModel;
import com.copsis.models.EstructuraUbicacionesModel;

public class PrimeroDiversosModel {
	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
	private String contenido = "";


	public PrimeroDiversosModel(String contenido) {
		this.contenido = contenido;
	}
	
	public EstructuraJsonModel procesar() {
		contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());
		 String newcontenido = "";
		 int inicio;
		 int fin;
		
		try {
			modelo.setCia(49);
			modelo.setTipo(7);

			inicio = contenido.indexOf("PÓLIZA DE SEGURO PARA DAÑOS");
			fin = contenido.indexOf(ConstantsValue.COBERTURAS);

    
			   if (inicio > 0 && fin > 0 && inicio < fin) {
	                newcontenido = contenido.substring(inicio, fin).replace("@@@", "").replace("\r", "").replace("12:00", "").replace("12 Hrs", "");
	                for (int i = 0; i < newcontenido.split("\n").length; i++) {
	               
	                	if(newcontenido.split("\n")[i].contains(ConstantsValue.SEGURO_PARA_DANOS)) {	 
	                		if(newcontenido.split("\n")[i].contains("###")) {
	                			modelo.setPoliza(newcontenido.split("\n")[i].split(ConstantsValue.SEGURO_PARA_DANOS)[1].split("###")[1].replace("-", "").trim());
		                		modelo.setPolizaGuion(newcontenido.split("\n")[i].split(ConstantsValue.SEGURO_PARA_DANOS)[1].split("###")[1]);
	                		}else {
	                			modelo.setPoliza(newcontenido.split("\n")[i+1].replace("-", "").trim());
		                		modelo.setPolizaGuion(newcontenido.split("\n")[i+1].trim());	                			
	                		}	                		
	                	}
	                	if(modelo.getPoliza().length() == 0 && newcontenido.split("\n")[i].contains("SEGURO PARA DAÑOS")) {	                			   
	                			modelo.setPoliza(newcontenido.split("\n")[i+1].replace("-", "").trim());
		                		modelo.setPolizaGuion(newcontenido.split("\n")[i+1].trim());	                		
	                	}
	                	
	                	if(newcontenido.split("\n")[i].contains("Fecha de Emisión") && newcontenido.split("\n")[i+1].split(" ")[0].contains("-")) {
	                	   
	                		modelo.setFechaEmision(fn.formatDateMonthCadena(newcontenido.split("\n")[i+1].split(" ")[0]));
	                	}
	                	  if(newcontenido.split("\n")[i].contains("Fecha de Emisión") && modelo.getFechaEmision().length() == 0) {
	                	      modelo.setFechaEmision(fn.formatDateMonthCadena(newcontenido.split("\n")[i+1].split("###")[1]));  
	                	  }
	                	if(newcontenido.split("\n")[i].contains("Asegurado:")) {
	                		modelo.setCteNombre(newcontenido.split("\n")[i].split("Asegurado:")[1].replace(":", "").replace("###", ""));
	                	}
						
	                	if(newcontenido.split("\n")[i].contains("Domicilio:")) {
	                		modelo.setCteDireccion(newcontenido.split("\n")[i].split("Domicilio:")[1].replace(":", "").replace("###", ""));
	                	}
	                	if (newcontenido.split("\n")[i].contains("Cp.")  && newcontenido.split("\n")[i].contains("Domicilio:")) {
							List<String> valores = fn.obtenerListNumeros2(newcontenido.toString().split("\n")[i+1]);
							if( modelo.getCp().length() == 0 && valores.size() > 0){
                              modelo.setCp(valores.get(0));
							}
							if( modelo.getCp().length() ==0){
								modelo.setCp(newcontenido.split("\n")[i].split("Cp.")[1].split(",")[0].trim());
							}
							
						
						}
	                	if(newcontenido.split("\n")[i].contains("RFC:") || newcontenido.split("\n")[i].contains("Teléfono:")) {
	                		modelo.setRfc(newcontenido.split("\n")[i].split("RFC:")[1].split("Teléfono:")[0].replace("###", ""));
	                	}
	                	if(newcontenido.split("\n")[i].contains("Vigencia")  && newcontenido.split("\n")[i+1].split("-").length > 3 && newcontenido.split("\n")[i+1].split("###").length > 4) {	                	
	                			modelo.setPlan(newcontenido.split("\n")[i+1].split("###")[0]);
	                			modelo.setVigenciaDe(fn.formatDateMonthCadena(newcontenido.split("\n")[i+1].split("###")[2].trim()));
	                			modelo.setVigenciaA(fn.formatDateMonthCadena(newcontenido.split("\n")[i+1].split("###")[4].trim()));	                		
	                	}
	                }
			   }
			   
			   
	            inicio = contenido.indexOf("Prima Neta");
	            fin = contenido.indexOf("EN CASO DE SINIESTRO");
	            
                String contenidopr ="";
                for (int i = 0; i < contenido.split("Prima Neta").length; i++) {
                  if(i > 1 &&  contenido.split("Prima Neta")[i].split("EN CASO DE SINIESTRO")[0] .contains("Moneda")) {
                      contenidopr = contenido.split("Prima Neta")[i].split("EN CASO DE SINIESTRO")[0];
                  } 
                }
           
	          
	            if (inicio > 0 && fin > 0 && inicio < fin) {
	                if(contenidopr.length()> 0) {
	                    newcontenido = contenidopr.replace("@@@", "").replace("\r", "")
	                            .replace("###Financiamiento###Gastos de###Subtotal###IVA###Total","Prima Neta###Financiamiento###Gastos de###Subtotal###IVA###Total" );        
	                            
	                }else {
	                newcontenido = contenido.substring(inicio, fin).replace("@@@", "").replace("\r", "")
	                        .replace("Prima Neta###Financiamiento###Gastos Expedición###Subtotal###IVA###Total","Prima Neta###Financiamiento###Gastos de###Subtotal###IVA###Total" );
	                }
	                for (int i = 0; i < newcontenido.split("\n").length; i++) {    
	                    if (newcontenido.split("\n")[i].contains("Prima Neta###Financiamiento###Gastos de###Subtotal###IVA###Total")) {
	                        modelo.setPrimaneta(fn.castBigDecimal( fn.preparaPrimas(newcontenido.split("\n")[i + 1].split("###")[0])));
	                        modelo.setRecargo(fn.castBigDecimal(fn.preparaPrimas(newcontenido.split("\n")[i + 1].split("###")[1])));
	                        modelo.setDerecho(fn.castBigDecimal(fn.preparaPrimas(newcontenido.split("\n")[i + 1].split("###")[2])));
	                        modelo.setIva(fn.castBigDecimal(fn.preparaPrimas(newcontenido.split("\n")[i + 1].split("###")[4])));
	                        modelo.setPrimaTotal(fn.castBigDecimal(fn.preparaPrimas(newcontenido.split("\n")[i + 1].split("###")[5])));
	                    }
	                    
	                    if (newcontenido.split("\n")[i].contains("Moneda")) {
	                        modelo.setMoneda(fn.moneda(newcontenido.split("\n")[i + 1].split("###")[0]));
	                        modelo.setFormaPago(fn.formaPago(newcontenido.split("\n")[i + 1].split("###")[1]));
	                    }

	                }
	            }
	            obtenerAgente(contenido,modelo);
	            obtenerTextoDiversos(contenido,modelo);
			
	            inicio = contenido.indexOf("Datos del Riesgo");
				fin = contenido.indexOf("Coberturas");
				List<EstructuraUbicacionesModel> ubicaciones = new ArrayList<>();
				  if (inicio > -1 && fin > -1 && inicio < fin) {					 
		                newcontenido = contenido.substring(inicio, fin).replace("@@@", "").replace("\r", "");
		                EstructuraUbicacionesModel ubicacion = new EstructuraUbicacionesModel();
		                for (int i = 0; i < newcontenido.split("\n").length; i++) {

		                	if(newcontenido.split("\n")[i].contains("Giro") && newcontenido.split("\n")[i].contains("Ubicación del Riesgo")) {
		                		ubicacion.setGiro(newcontenido.split("\n")[i].split("Giro")[1].replace("###", ""));
		                	}
		                	if(newcontenido.split("\n")[i].contains("Muro:")) {
		                		ubicacion.setMuros(fn.material(newcontenido.split("\n")[i].split("Muro:")[1].replace(".", "###").split("###")[0].toLowerCase()));
		                	}
		                	if(newcontenido.split("\n")[i].contains("Techo:")) {
		                		ubicacion.setTechos(fn.material(newcontenido.split("\n")[i].split("Techo:")[1].replace(".", "###").split("###")[0].toLowerCase()));
		                	}
		                	if(newcontenido.split("\n")[i].contains("Cp.") && newcontenido.split("\n")[i].split("Cp")[1].length() >1) {
		                		ubicacion.setCp(newcontenido.split("\n")[i].split("Cp.")[1].replace(",", "###").split("###")[0].trim());
		                	}
		                   	if(newcontenido.split("\n")[i].contains("Niveles:")) {
		                		ubicacion.setNiveles(Integer.parseInt( newcontenido.split("\n")[i].split("Niveles:")[1].replace(".", "###").split("###")[0].strip()));
		                	}
		                   	if(newcontenido.split("\n")[i].contains("Ubicación del Riesgo.")) {
		                   	  ubicacion.setCalle( newcontenido.split("\n")[i+1]);
		                   	}		     		             
		                }
		                ubicaciones.add(ubicacion);
		                modelo.setUbicaciones(ubicaciones);
		    
				  }
				  
				  inicio = contenido.indexOf("Coberturas");
				  fin = contenido.indexOf("Prima Neta");
				  if(fin <  inicio) {
				      fin =contenido.lastIndexOf("Prima Neta");
				  }

				
				  if (inicio > -1 && fin > -1 && inicio < fin) {	
					  String secciont="";
					  List<EstructuraCoberturasModel> coberturas = new ArrayList<>();
					   newcontenido = contenido.substring(inicio, fin).replace("@@@", "").replace("\r", "");
		                for (int i = 0; i < newcontenido.split("\n").length; i++) {    
		                	EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();
		                	
		                	if(!newcontenido.split("\n")[i].contains("Coberturas Amparadas") || !newcontenido.split("\n")[i].contains("Deducible")
		                			|| !newcontenido.split("\n")[i].contains("Sección") || !newcontenido.split("\n")[i].contains("Coberturas")) {
		             
		                		
		                		int sp = newcontenido.split("\n")[i].split("###").length;
		                		if(sp == 4) {
		                			secciont =newcontenido.split("\n")[i].split("###")[0];	
		                		}
		                			                				        
		                		switch (sp) {
								case  4:
									cobertura.setSeccion(secciont);
									cobertura.setNombre( newcontenido.split("\n")[i].split("###")[1]);
									cobertura.setSa( newcontenido.split("\n")[i].split("###")[2]);
									cobertura.setDeducible(newcontenido.split("\n")[i].split("###")[3]);
									coberturas.add(cobertura);
									break;

								case 3:
										cobertura.setSeccion(secciont);
									cobertura.setNombre( newcontenido.split("\n")[i].split("###")[0]);
									cobertura.setSa( newcontenido.split("\n")[i].split("###")[1]);
									cobertura.setDeducible(newcontenido.split("\n")[i].split("###")[2]);
									coberturas.add(cobertura);
									break;
									default:
										break;
								}
//		                		
		                	}
		                	
		                }
		                modelo.setCoberturas(coberturas);						
				  
				  }
				  buildRecibos();
	            
			
			return modelo;
		} catch (Exception e) {
			modelo.setError(PrimeroDiversosModel.this.getClass().getTypeName()+" - catch:"+e.getMessage()+" | "+ e.getCause());
		  return modelo;
		}
	}
	
	private void obtenerAgente(String texto,EstructuraJsonModel model) {
        int inicio = texto.indexOf("AVISO DE PRIVACIDAD");
        int fin = texto.indexOf("Nombre del Agente");
        String newContenido = "";
        if (inicio > 0 && fin > 0 && inicio < fin) {
        	newContenido = texto.substring(inicio,fin).replace("@@@", "");
        	if(newContenido.split("\n").length>1) {
        		model.setAgente(newContenido.split("\n")[newContenido.split("\n").length-1].replace("\r", ""));
        	}
        }
	}
	
	private void obtenerTextoDiversos(String contenidoTexto, EstructuraJsonModel model) {
		int inicio = contenido.indexOf("Datos del Embarque");
		int fin = contenido.indexOf("Coberturas Amparadas");
		
		if(inicio > -1  && inicio < fin) {
			String[] arrContenido = contenidoTexto.substring(inicio,fin).replace("@@@", "").replace("\r","").replace("DPeasncerl iRpacdióiandor","Descripción\n ###Panel Radiador").split("\n");
			StringBuilder texto = new StringBuilder();
			StringBuilder mercancia = new StringBuilder();
			String factura = "";
			String empaque = "";
			StringBuilder trayecto = new StringBuilder();
			StringBuilder descripcion = new StringBuilder();
			int renglonDescripcion = -1;

			for(int i=0;i<arrContenido.length;i++) {
				if(arrContenido[i].split("Mercancía###").length>1 && (i+1)<arrContenido.length){
					texto.append("Datos del Embarque,");
					mercancia.append("Mercancía:");
					mercancia.append(fn.elimgatos(arrContenido[i].split("Mercancía###")[1].trim()).split("###")[0]);
					texto.append(mercancia);
					if(arrContenido[i+1].contains("Factura###")) {
						texto.append(" ");
						texto.append(arrContenido[i+1].split("Factura###")[0].replace("###",""));
						texto.append(",");
						if(arrContenido[i+1].split("Factura###").length>1) {
							factura ="Factura:"+ arrContenido[i+1].split("Factura")[1].replace("###", "").trim();
							texto.append(factura).append(",");
						}
					}
				}
				
				if(arrContenido[i].split("Tipo de Mercancía###").length>1) {
					texto.append("Tipo de Mercancía:").append(arrContenido[i].split("###")[arrContenido[i].split("###").length-1]);
					texto.append(",");
				}
				
				if(arrContenido[i].split("Empaque###").length > 1) {
					empaque = fn.elimgatos(arrContenido[i].split("Empaque###")[1].trim()).split("###")[0];
					texto.append("Empaque:").append(empaque).append(",");
				}
				
				if(arrContenido[i].contains("Descripción") && (i+1)<arrContenido.length) {
					descripcion.append(arrContenido[i+1].split("###")[arrContenido[i+1].split("###").length-1]);
					renglonDescripcion = i+1;
				}

				if(descripcion.length()>0 && renglonDescripcion < i  && (arrContenido[i].split("###").length == 1 || arrContenido[i].split("###").length >2)) {
					descripcion.append(" ");
					descripcion.append(arrContenido[i].split("###")[arrContenido[i].split("###").length-1]);
				}
				
				
				if(arrContenido[i].split("Trayecto###Origen###").length>1 && (i+1)<arrContenido.length && arrContenido[i].contains("###")) {
					trayecto.append("Trayecto:Origen:");
					trayecto.append(fn.elimgatos(arrContenido[i].split("Trayecto###Origen###")[1].trim()).split("###")[0]);
					trayecto.append(",");
				}
				
				if(arrContenido[i].contains("Destin###")) {
					trayecto.append("Destino: ");
					trayecto.append(fn.elimgatos(arrContenido[i].split("Destin###")[1].trim()).split("###")[0]);
					texto.append(trayecto).append(",");
				}
			}

			texto.append("Descripción:").append(descripcion).append(",");
			model.setTextoDiversos(texto.toString().substring(0,texto.length()-1));
		}
		
	}
	
	private void buildRecibos() {
		if(modelo.getFormaPago() == 1 ) {
            List<EstructuraRecibosModel> listRecibos = new ArrayList<>();
			EstructuraRecibosModel recibo = new EstructuraRecibosModel();
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
			listRecibos.add(recibo);
			modelo.setRecibos(listRecibos);
		}
	}
}
