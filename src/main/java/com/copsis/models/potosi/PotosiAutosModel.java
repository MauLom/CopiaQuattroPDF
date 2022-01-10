package com.copsis.models.potosi;

import java.util.ArrayList;
import java.util.List;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraCoberturasModel;
import com.copsis.models.EstructuraJsonModel;

public class PotosiAutosModel {
	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();	
	private String contenido = "";
	
	public PotosiAutosModel(String contenido) {
		this.contenido = contenido;
	}
	public EstructuraJsonModel procesar() {
		int inicio = 0;
		int fin = 0;
		boolean  cp = true;
		boolean  decVehiculo = true;
		StringBuilder newcontenido = new StringBuilder();
		contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());
		try {
			modelo.setTipo(1);
			modelo.setCia(37);
			
		
			
			inicio = contenido.indexOf("SEGURO DE AUTOMÓVILES");
			fin = contenido.indexOf("DATOS DEL RIESGO ASEGURADO");
			if(inicio > -1 && fin > -1   && inicio  < fin ) {
				newcontenido.append(contenido.substring(inicio,fin).replace("@@@", "").replace("\r", "").replace("12:00###hrs.", "").replace("ANUAL", "CONTADO"));
				for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
					if(newcontenido.toString().split("\n")[i].contains("Póliza") && newcontenido.toString().split("\n")[i].contains("Certificado")) {
						modelo.setPoliza(newcontenido.toString().split("\n")[i+1].split("###")[0]);
					}
					if(newcontenido.toString().split("\n")[i].contains("Desde") && newcontenido.toString().split("\n")[i].contains("Nombre")) {
						modelo.setVigenciaDe(fn.formatDate(newcontenido.toString().split("\n")[i].split("Desde:")[1].split("Nombre")[0].replace("###", "").trim()));
						if(newcontenido.toString().split("\n")[i+1].contains("Hasta")) {
							modelo.setVigenciaA(fn.formatDate(newcontenido.toString().split("\n")[i+1].split("###")[1].trim()));
							modelo.setCteNombre(newcontenido.toString().split("\n")[i+1].split("###")[2].trim());
						}
						modelo.setFechaEmision(modelo.getVigenciaDe());					
					}
					if(newcontenido.toString().split("\n")[i].contains("CP") && cp) {
						modelo.setCp(newcontenido.toString().split("\n")[i].split("CP")[1].trim().substring(0,5));
						cp= false;
					}
					if(newcontenido.toString().split("\n")[i].contains("R.F.C:") && newcontenido.toString().split("\n")[i].contains("Teléfono")) {
						modelo.setRfc(newcontenido.toString().split("\n")[i].split("R.F.C:")[1].split("Teléfono")[0].replace("###", "").replace("-", "").trim());
					}
					if(newcontenido.toString().split("\n")[i].contains("Dirección:") && newcontenido.toString().split("\n")[i+1].contains("operación")) {						
						modelo.setCteDireccion(newcontenido.toString().split("\n")[i+1].split("###")[2].replace("###", "").trim());
					}
				
					if(newcontenido.toString().split("\n")[i].contains("Plan de pago") && newcontenido.toString().split("\n")[i].contains("Domicilio")) {
						modelo.setFormaPago(fn.formaPagoSring(newcontenido.toString().split("\n")[i]));
					}
					if(modelo.getFormaPago() == 0 && newcontenido.toString().split("\n")[i].contains("Plan de pago")) {
						modelo.setFormaPago(fn.formaPagoSring(newcontenido.toString().split("\n")[i]));
					}
					if(newcontenido.toString().split("\n")[i].contains("Moneda")) {
						modelo.setMoneda(fn.buscaMonedaEnTexto(newcontenido.toString().split("\n")[i]));
					}
					
				}
				
			}
			
			

			inicio = contenido.indexOf("DATOS DEL RIESGO ASEGURADO");
			fin = contenido.indexOf("CONDICIONES DEL ASEGURAMIENTO");
			if(inicio > -1 && fin > -1   && inicio  < fin ) {
				newcontenido = new StringBuilder();
				newcontenido.append(contenido.substring(inicio,fin).replace("@@@", "").replace("\r", ""));
				for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {							
					if(newcontenido.toString().split("\n")[i].contains("Vehículo:") && decVehiculo) {
						modelo.setDescripcion( newcontenido.toString().split("\n")[i].trim());
						decVehiculo = false;
					}					
					if(newcontenido.toString().split("\n")[i].contains("serie:") && newcontenido.toString().split("\n")[i].contains("No. de")) {
						modelo.setSerie(newcontenido.toString().split("\n")[i].split("serie:")[1].split("No. de")[0].replace("###",""));
					}
					if(newcontenido.toString().split("\n")[i].contains("motor:") && newcontenido.toString().split("\n")[i].split("moto")[1].length() > 10) {
						modelo.setMotor(newcontenido.toString().split("\n")[i].split("motor:")[1].replace("###", "").trim());
					}
					
					if(newcontenido.toString().split("\n")[i].contains("Mod.")) {
						modelo.setModelo(fn.castInteger( newcontenido.toString().split("\n")[i].split("Mod.")[1].trim().substring(0,4)));
					}
					if(newcontenido.toString().split("\n")[i].contains("No. PLACA:") && newcontenido.toString().split("\n")[i].split("No. PLAC")[1].length() > 6) {
					  modelo.setPlacas(newcontenido.toString().split("\n")[i].split("No. PLACA:")[1].trim());	
					}
				}
			}
			
			inicio = contenido.indexOf("DETALLES DE MOVIMIENTO");
			fin = contenido.indexOf("Fecha de expedición");

			if(inicio > -1 && fin > -1   && inicio  < fin ) {
				newcontenido = new StringBuilder();
				newcontenido.append(contenido.substring(inicio,fin).replace("@@@", "").replace("\r", ""));
				for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
					if(newcontenido.toString().split("\n")[i].contains("Prima")&& newcontenido.toString().split("\n")[i].split("###").length == 8) {
															
					    modelo.setPrimaneta(fn.castBigDecimal(fn.preparaPrimas(newcontenido.toString().split("\n")[i+1].split("###")[0])));
						modelo.setDerecho(fn.castBigDecimal(fn.preparaPrimas(newcontenido.toString().split("\n")[i+1].split("###")[1])));
						modelo.setRecargo(fn.castBigDecimal(fn.preparaPrimas(newcontenido.toString().split("\n")[i+1].split("###")[2])));
						modelo.setIva(fn.castBigDecimal(fn.preparaPrimas(newcontenido.toString().split("\n")[i+1].split("###")[4])));
						modelo.setPrimaTotal(fn.castBigDecimal(fn.preparaPrimas(newcontenido.toString().split("\n")[i+1].split("###")[7])));
						
					}
					
				}
			
			}
			
			     inicio = contenido.indexOf("AGENTE");
	            fin = contenido.indexOf("Fecha de expedición");

	   
	            if (inicio > -1 && fin > -1 && inicio < fin) {
	            	newcontenido = new StringBuilder();
	            	newcontenido.append(contenido.substring(inicio,fin).replace("@@@", "").replace("\r", ""));
	            	for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
	          
	            		if (newcontenido.toString().split("\n")[i].contains("Clave")) {
	            			modelo.setCveAgente(newcontenido.toString().split("\n")[i+1].split("###")[0]);
	   	                    modelo.setAgente(newcontenido.toString().split("\n")[i+1].split("###")[1]);
	            		}
	            	}
	            }
	      
	            inicio = contenido.indexOf("CONDICIONES DEL ASEGURAMIENTO");
	            fin = contenido.indexOf("1 de 2");
	            if (fin == -1) {
	                fin = contenido.indexOf("1 de 3");
	            }

	            if (inicio > -1 && fin > -1 && inicio < fin) {
	            	List<EstructuraCoberturasModel> coberturas = new ArrayList<>();
	                newcontenido .append(contenido.substring(inicio, fin).replace("@@@", ""));
	                for (String x : newcontenido.toString().split("\r\n")) {
	                  	EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();
	                    int sp = x.split("###").length;

	                    if (!x.contains("CONDICIONES DEL ASEGURAMIENTO") &&  !x.contains("Cobertura###Suma Asegurada###Prima###Deducible")) {	                   
	                        if (sp == 3) {	      
	                            cobertura.setNombre(x.split("###")[0]);
	                            cobertura.setSa(x.split("###")[1]);
	                            coberturas.add(cobertura);
	                        }
	                        if (sp == 4) {
	                          
	                            cobertura.setNombre(x.split("###")[0]);
	                            cobertura.setSa(x.split("###")[1]);
	                            cobertura.setDeducible(x.split("###")[3]);
	                            coberturas.add(cobertura);

	                        }
	                    }

	                }
	        		modelo.setCoberturas(coberturas);
	            }
			

			
			
			
			return modelo;
		} catch (Exception e) {			
			 return modelo;
		}
		
	}
	


}
