package com.copsis.models.chubb;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraCoberturasModel;
import com.copsis.models.EstructuraJsonModel;
import com.copsis.models.EstructuraRecibosModel;
import com.copsis.models.EstructuraUbicacionesModel;

public class ChubbDiversosModel {
	// Clases
	DataToolsModel fn = new DataToolsModel();
	EstructuraJsonModel modelo = new EstructuraJsonModel();

	// Variables
	private String contenido;
	private String recibos = "";
	private int inicio = 0;
	private int fin = 0;
	private String newcontenido = "";
	private String resultado = "";
	private BigDecimal restoPrimaTotal = BigDecimal.ZERO;
	private BigDecimal restoDerecho = BigDecimal.ZERO;
	private BigDecimal restoIva = BigDecimal.ZERO;
	private BigDecimal restoRecargo = BigDecimal.ZERO;
	private BigDecimal restoPrimaNeta = BigDecimal.ZERO;
	private BigDecimal restoAjusteUno = BigDecimal.ZERO;
	private BigDecimal restoAjusteDos = BigDecimal.ZERO;
	private BigDecimal restoCargoExtra = BigDecimal.ZERO;
	private List<String> conceptos;
	private String separador = "###";
	private String saltolinea = "\r\n";
	

	// constructor
	public ChubbDiversosModel(String contenido,String recibos ) {
		this.contenido = contenido;
		this.recibos= recibos;

	}

	public EstructuraJsonModel procesar() {
		contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());
		contenido = contenido.replace("PÓLIZA DE SEGURO", "POLIZA DE SEGURO");
	     recibos = fn.remplazarMultiple(recibos, fn.remplazosGenerales());
		try {

			    modelo.setTipo(7);
	            modelo.setCia(1);
	            modelo.setRamo("Daños");
	            
	            inicio =contenido.indexOf("POLIZA DE SEGURO");
	            fin = contenido.indexOf("Características del riesgo");
	         

	            if(inicio > 0 && fin >  0 && inicio < fin) {
	            	newcontenido = contenido.substring( inicio,fin);
	            	for (int i = 0; i < newcontenido.split("\n").length; i++) {
	            		if(newcontenido.split("\n")[i].contains("Póliza:")  && newcontenido.split("\n")[i].contains("Vigencia")) {
	            			modelo.setPoliza(newcontenido.split("\n")[i].split("Póliza:")[1].split("Vigencia:")[0].replace("###", ""));
	            			modelo.setVigenciaDe(fn.formatDateMonthCadena(newcontenido.split("\n")[i].split("Vigencia:")[1].split("horas")[0].replace("###", "").replace("12:00", "").replace("Del", "").trim()));
	            			modelo.setVigenciaA(fn.formatDateMonthCadena(newcontenido.split("\n")[i].split("horas")[1].split("horas")[0].replace("###", "").replace("12:00", "").replace("al", "").trim()));
	            		}
	            		if(newcontenido.split("\n")[i].contains("Asegurado:")  && newcontenido.split("\n")[i].contains("C.P:")) {
	            			modelo.setCteNombre((newcontenido.split("\n")[i].split("Asegurado:")[1].split("C.P:")[0].replace("###", " ")).trim());
	            			modelo.setCp(newcontenido.split("\n")[i].split("C.P:")[1].replace("\r", "").replace("###", ""));
	            		}
	            		if(newcontenido.split("\n")[i].contains("Domicilio:")  && newcontenido.split("\n")[i].contains("Teléfono")) {
	            			modelo.setCteDireccion((newcontenido.split("\n")[i].split("Domicilio:")[1].split("Teléfono")[0] +" " 
	            		      +newcontenido.split("\n")[i+1].split("RFC:")[0]).replace("###", " ").replace("\r", "").trim());
	            		}
	            		if(newcontenido.split("\n")[i].contains("RFC:")) {
	            			modelo.setRfc(newcontenido.split("\n")[i].split("RFC:")[1].replace("\r", "").replace("###", "").trim());
	            		} 
	            		if(newcontenido.split("\n")[i].contains("Moneda:")  && newcontenido.split("\n")[i].contains("pago")) {
	            			modelo.setMoneda(fn.moneda(newcontenido.split("\n")[i].split("Moneda:")[1].split("Forma")[0].replace("###", "").trim()));
	            			modelo.setFormaPago(fn.formaPago(newcontenido.split("\n")[i].split("pago:")[1].replace("\r", "").replace("###", "").trim()));	            		
	            		}
	            		if(newcontenido.split("\n")[i].contains("emisión:") && newcontenido.split("\n")[i].contains("Descuento")) {
	            			String x = newcontenido.split("\n")[i].split("emisión:")[1].split("Descuento")[0].replace("###", "").trim().replace(" ", "###");
	            			x = x.split("###")[0] +"-"+x.split("###")[1] +"-"+ x.split("###")[2];
	            			modelo.setFechaEmision(fn.formatDateMonthCadena(x));
	            		}
	            		if(newcontenido.split("\n")[i].contains("Paquete:")) {
	            			modelo.setPlan(newcontenido.split("\n")[i].split("Paquete:")[1].replace("\r", "").replace("###", "").trim());
	            		} 
	            		if(newcontenido.split("\n")[i].contains("agente:")) {
	            			modelo.setCveAgente(newcontenido.split("\n")[i].split("agente:")[1].replace("\r", "").replace("###", "").trim());
	            		}
					}	            
	            }
	            
	            //PRIMAS
	            inicio = contenido.indexOf("o especificación");
	            fin = contenido.indexOf("Notas del riesgo");
	            if (inicio > -1 && fin > inicio) {
					newcontenido = contenido.substring( inicio,fin).replace("@@@", "");
					for (int i = 0; i < newcontenido.split("\n").length; i++) {					
						if(newcontenido.split("\n")[i].contains("Neta")) {
							modelo.setPrimaneta(fn.castBigDecimal( fn.castDouble(newcontenido.split("\n")[i].split("Neta")[1].replace("###", "").replace("\r", "").trim())));
						}
						if(newcontenido.split("\n")[i].contains("Financiamiento") && newcontenido.split("\n")[i].contains("expedición")) {
							modelo.setRecargo(fn.castBigDecimal( fn.castDouble(newcontenido.split("\n")[i].split("fraccionado")[1].split("Gastos")[0].replace("###", "").replace("\r", "").trim())));
							modelo.setDerecho(fn.castBigDecimal( fn.castDouble(newcontenido.split("\n")[i].split("expedición")[1].replace("###", "").replace("\r", "").trim())));
						}
						if(newcontenido.split("\n")[i].contains("I.V.A.")) {
							modelo.setIva(fn.castBigDecimal( fn.castDouble(newcontenido.split("\n")[i].split("I.V.A.")[1].replace("###", "").replace("\r", "").trim())));
						}						
						if(newcontenido.split("\n")[i].contains("Total:")) {
							modelo.setPrimaTotal(fn.castBigDecimal( fn.castDouble(newcontenido.split("\n")[i].split("Total:")[1].replace("###", "").replace("\r", "").trim())));
						}
					
					}
	            }
	            
	            modelo.setAgente(recibos.split("Clave interna del agente:")[1].split("Desglose de pago")[0].replace("###", "").replace("\r\n", "").trim());


	         // UBICACIONES
				inicio = contenido.indexOf("Características del riesgo");
				fin = contenido.indexOf("Prima");
				List<EstructuraUbicacionesModel> ubicaciones = new ArrayList<>();

				if (inicio > -1 && fin > inicio) {
					newcontenido = contenido.substring( inicio,fin);
					EstructuraUbicacionesModel ubicacion = new EstructuraUbicacionesModel();
	            	for (int i = 0; i < newcontenido.split("\n").length; i++) {
	            		if(newcontenido.split("\n")[i].contains("Dirección:")) {
	            			ubicacion.setCalle(newcontenido.split("\n")[i].split("Dirección:")[1].replace("###", "").replace("\r", ""));
	            		}
	            		if(newcontenido.split("\n")[i].contains("Techo:")) {
	            			ubicacion.setTechos(fn.material(newcontenido.split("\n")[i].split("Techo:")[1].split("Tipo")[0].replace("###", "")));
	            			ubicacion.setMuros(fn.material(newcontenido.split("\n")[i].split("Techo:")[1].split("Tipo")[0].replace("###", "")));
	            		}
	            		
	            		if(newcontenido.split("\n")[i].contains("Niveles:")) {
	            			ubicacion.setNiveles(Integer.parseInt( newcontenido.split("\n")[i].split("Niveles:")[1].split("En qué piso")[0].replace("###", "").replace("\r", "")));
	            		}
	          
	            	}
	            	ubicaciones.add(ubicacion);
	            	
	            	modelo.setUbicaciones(ubicaciones);
				}

				List<EstructuraCoberturasModel> coberturas = new ArrayList<>();
				//Cobertutas
				
				 inicio = contenido.indexOf("Tipo Vivienda");
		            fin = contenido.indexOf("Prima Neta");

		            String  nombre="";
		            String  deducible="";
		
		            if (inicio > -1 && fin > -1 && inicio < fin) {
		                newcontenido = contenido.substring(inicio, fin).replace("@@@", "").replace("###Prima", "").trim();
		                for (String x : newcontenido.split("\r\n")) {
		                    if (x.contains("Tipo Vivienda") == false
		                            && x.contains("Coberturas###Suma") == false
		                            && x.contains("página#") == false) {
		                        resultado += x.trim() + "\r\n";
		                    }
		                }
		                if (resultado.split("\r\n").length > 1) {
		                    String seccion = "";

		                    for (int i = 0; i < resultado.split("\r\n").length; i++) {
		            			EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();
		                        String a = "";
		                        String b = "";
		                        String c = "";
		                        a = resultado.split("\r\n")[i];
		                        if (a.contains("SECCION")) {
		                            seccion = a;
		                        } else {
		                            if (i + 1 < resultado.split("\r\n").length) {
		                                b = resultado.split("\r\n")[i + 1].trim();
		                            }
		                            if (i + 2 < resultado.split("\r\n").length) {
		                                c = resultado.split("\r\n")[i + 2].trim();
		                            }
		                            switch (a.split("###").length) {
		                                case 3:
		                                    nombre = a.split("###")[0].trim();
		                                    deducible = a.split("###")[2].trim();
		                                    if (deducible.contains("de la pérdida") || deducible.contains("del eq. dañado")) {
		                                        switch (b.split("###").length) {
		                                            case 1:
		                                                if (b.contains("SECCION") == false) {
		                                                    deducible += " " + b;
		                                                }
		                                                break;
		                                        }
		                                        switch (c.split("###").length) {
		                                            case 1:
		                                                if (b.split("###").length == 1 && c.contains("SECCION") == false) {
		                                                    deducible += " " + c;
		                                                }
		                                                break;
		                                        }

		                                    } else {
		                                    	cobertura.setDeducible(deducible);
		                                    }
		                                    cobertura.setSeccion(seccion.replace("SECCION", "").trim());
		                                    cobertura.setNombre(nombre);
		                                    cobertura.setSa(a.split("###")[1].trim());
		                                    cobertura.setDeducible(deducible);

		                                    coberturas.add(cobertura);
		                                    break;
		                            }
		                        }
		                    }
		                }
		                modelo.setCoberturas(coberturas);
		            }
				
		            List<EstructuraRecibosModel> recibosList = new ArrayList<>();
					EstructuraRecibosModel recibo = new EstructuraRecibosModel();
		            
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
		        	}
		        	modelo.setRecibos(recibosList);
	       
			
			return modelo;
		} catch (Exception ex) {
			modelo.setError(
					ChubbDiversosModel.this.getClass().getTypeName() + " | " + ex.getMessage() + " | " + ex.getCause());
			return modelo;
		}
	}
}
