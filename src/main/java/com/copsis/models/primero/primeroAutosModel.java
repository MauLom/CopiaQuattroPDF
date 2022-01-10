package com.copsis.models.primero;

import java.util.ArrayList;
import java.util.List;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraCoberturasModel;
import com.copsis.models.EstructuraJsonModel;

public class primeroAutosModel {

	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
	private String contenido = "";


	public primeroAutosModel(String contenido) {
		this.contenido = contenido;
	}

	public EstructuraJsonModel procesar() {
		
		 String newcontenido = "";
		 int inicio = 0;
		 int fin = 0;
		
		contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());
		contenido = contenido.replace("12 Hrs", "");
		try {

			modelo.setCia(49);
			modelo.setTipo(1);
			modelo.setInciso(1);
			//Fecha de Emisión
			inicio = contenido.indexOf("Fecha de Emisión");
			fin = contenido.indexOf("Datos generales");
			if(inicio>-1 && fin>-1 && inicio<fin) {
				 newcontenido = contenido.substring(inicio, fin).replace("@@@", "").replace("\r", "");
				 modelo.setFechaEmision(fn.formatDate(fn.obtenerFecha(newcontenido),"dd-MM-yy"));
			}
			inicio = contenido.indexOf("Datos generales");
			fin = contenido.indexOf("Coberturas Amparadas");
			
			
		      if (inicio > 0 && fin > 0 && inicio < fin) {
	                newcontenido = contenido.substring(inicio, fin).replace("@@@", "").replace("\r", "").replace("12:00", "");

	                for (int i = 0; i < newcontenido.split("\n").length; i++) {
	                    if (newcontenido.split("\n")[i].contains("Cp.")) {
	                        modelo.setCp(newcontenido.split("\n")[i].split("Cp.")[1].split(",")[0].trim());
	                    }
	                    if (newcontenido.split("\n")[i].contains("Asegurado")) {
	                        modelo.setCteNombre(newcontenido.split("\n")[i].split("Asegurado:")[1].replace("###", ""));
	                    }
	                    if (newcontenido.split("\n")[i].contains("Domicilio")) {
	                        modelo.setCteDireccion(newcontenido.split("\n")[i].split("Domicilio:")[1].replace("###", ""));
	                    }
	                    if (newcontenido.split("\n")[i].contains("RFC:") && newcontenido.split("\n")[i].contains("Teléfono:")) {
	                        modelo.setRfc(newcontenido.split("\n")[i].split("RFC:")[1].split("Teléfono")[0].replace("###", ""));
	                    }
	                    if (newcontenido.split("\n")[i].contains("Vigencia")) {

	                        modelo.setPlan(newcontenido.split("\n")[i + 1].split("###")[0]);
	                        if (newcontenido.split("\n")[i + 1].split("###")[1].length() > 2) {

	                        } else {
	                            modelo.setPoliza(newcontenido.split("\n")[i + 1].split("###")[1].replace("-", ""));
	                            modelo.setPolizaGuion(newcontenido.split("\n")[i + 1].split("###")[1]);
	                        }
	                        if (newcontenido.split("\n")[i + 1].split("###")[1].split("-").length > 2) {
	                            modelo.setVigenciaDe(fn.formatDateMonthCadena(newcontenido.split("\n")[i + 1].split("###")[1].trim()));
	                            modelo.setVigenciaA(fn.formatDateMonthCadena(newcontenido.split("\n")[i + 1].split("###")[3].trim()));
	                            modelo.setFechaEmision(modelo.getFechaEmision().isBlank()? modelo.getVigenciaDe():modelo.getFechaEmision());
	                        } else {
	                            modelo.setVigenciaDe(fn.formatDateMonthCadena(newcontenido.split("\n")[i + 1].split("###")[2].trim()));
	                            modelo.setVigenciaA(fn.formatDateMonthCadena(newcontenido.split("\n")[i + 1].split("###")[4].trim()));
	                            modelo.setFechaEmision(modelo.getFechaEmision().isBlank()? modelo.getVigenciaDe():modelo.getFechaEmision());
	                        }
	                    }
	                    if (newcontenido.split("\n")[i].contains("Clave") && newcontenido.split("\n")[i].contains("Marca")) {
	                        modelo.setClave(newcontenido.split("\n")[i + 1].split("###")[0]);
	                        modelo.setMarca(newcontenido.split("\n")[i + 1].split("###")[1].split("-")[0]);
	                        modelo.setDescripcion(newcontenido.split("\n")[i + 1].split("###")[1].split("-")[1]);
	                        modelo.setModelo(Integer.parseInt(newcontenido.split("\n")[i + 1].split("###")[2]));
	                    }else if(newcontenido.split("\n")[i].contains("Marca de Fábrica")&& newcontenido.split("\n")[i].contains("Descripción")) {
	                    	modelo.setMarca(newcontenido.split("\n")[i + 2].split("###")[1].split("-")[0].trim());
	                        modelo.setDescripcion(newcontenido.split("\n")[i + 2].split("###")[1].split("-")[1].trim());
	                    }
	                    
	                    if(modelo.getModelo() == 0 && newcontenido.split("\n")[i].contains("Modelo") && newcontenido.split("\n")[i].contains("###")) {
	                    	modelo.setModelo(Integer.parseInt(newcontenido.split("\n")[i].split("Modelo")[1].split("###")[1]));
	                    }
	                    
	                    if (newcontenido.split("\n")[i].contains("Placas") && newcontenido.split("\n")[i].contains("Serie")) {

	                        if (newcontenido.split("\n")[i + 1].split("###").length == 4) {

	                            modelo.setSerie(newcontenido.split("\n")[i + 1].split("###")[1]);
	                        } else {
	                            modelo.setPlacas(newcontenido.split("\n")[i + 1].split("###")[0]);
	                            modelo.setSerie(newcontenido.split("\n")[i + 1].split("###")[2]);
	                        }

	                    }else if(newcontenido.split("\n")[i].contains("Serie")) {
	                    	modelo.setSerie(newcontenido.split("\n")[i].split("Serie")[1].split("\n")[0].replace("###", ""));
	                    }
	                }
	            }
		      
		       inicio = contenido.indexOf("PÓLIZA DE SEGURO PARA");
	            fin = contenido.indexOf("Fecha de Emisión");
	            
	            if (inicio > 0 && fin > 0 && inicio < fin) {
	                newcontenido = contenido.substring(inicio, fin).replace("@@@", "").replace("\r", "");
	                  for (int i = 0; i < newcontenido.split("\n").length; i++) {
	                      if(newcontenido.split("\n")[i].contains("VEHICULOS")){
	                          modelo.setPoliza(newcontenido.split("\n")[i].split("VEHICULOS")[1].replace("###", "").replace("-", ""));
	                           modelo.setPolizaGuion(newcontenido.split("\n")[i].split("VEHICULOS")[1].replace("###", ""));
	                      }else if(newcontenido.split("\n")[i].contains("CUATRIMOTOS") && newcontenido.split("\n")[i-1].split("###").length>0) {
		                    	modelo.setPolizaGuion(newcontenido.split("\n")[i-1].split("###")[1]);
		                    	modelo.setPoliza(newcontenido.split("\n")[i-1].split("###")[1].replace("-", ""));
		                    }
	                  }
	            }
	            
	            
	            inicio = contenido.indexOf("Prima Neta");
	            fin = contenido.indexOf("EN CASO DE SINIESTRO");
	            
	            if (inicio > 0 && fin > 0 && inicio < fin) {
	                newcontenido = contenido.substring(inicio, fin).replace("@@@", "").replace("\r", "");
	                for (int i = 0; i < newcontenido.split("\n").length; i++) {  
	                    if (newcontenido.split("\n")[i].contains("Prima Neta###Financiamiento###Gastos de") &&
	                    	newcontenido.split("\n")[i].contains("Subtotal###IVA###Total")) {
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
	            
	            
	            inicio = contenido.indexOf("AGENTE");
	            fin = contenido.indexOf("NOTAS IMPORTANTES:");
	            if (inicio > 0 && fin > 0 && inicio < fin) {
	                newcontenido = contenido.substring(inicio, fin).replace("@@@", "").replace("\r", "");
	                for (int i = 0; i < newcontenido.split("\n").length; i++) {
	                    if (newcontenido.split("\n")[i].contains("AGENTE") && newcontenido.split("\n")[i].contains("CLAVE")) {
	                        modelo.setAgente(newcontenido.split("\n")[i].split("AGENTE")[1].split("CLAVE")[0].replace("###", ""));
	                        modelo.setCveAgente(newcontenido.split("\n")[i].split("CLAVE")[1].split("AGENTE")[1].replace("###", ""));
	                    }
	                }
	            }
	            
	            inicio = contenido.indexOf("AVISO DE PRIVACIDAD");
	            fin = contenido.indexOf("Nombre del Agente");

	            if (inicio > 0 && fin > 0 && inicio < fin) {
	            	newcontenido = contenido.substring(inicio,fin).replace("@@@", "");
	            	if(newcontenido.split("\n").length>1) {
	            		modelo.setAgente(newcontenido.split("\n")[newcontenido.split("\n").length-1].replace("\r", ""));
	            	}
	            }
	            
	            //Coberturas
	            inicio = contenido.indexOf("Coberturas Amparadas");
	            fin = contenido.indexOf("Prima Neta");

	            if (inicio > 0 && fin > 0 && inicio < fin) {
	            	List<EstructuraCoberturasModel> coberturas = new ArrayList<>();
	                newcontenido = contenido.substring(inicio, fin).replace("@@@", "").replace("\r", "");
	                for (int i = 0; i < newcontenido.split("\n").length; i++) {

						if (!newcontenido.split("\n")[i].split("###")[0].contains("Coberturas Amparadas")
								&& !newcontenido.split("\n")[i].split("###")[0].contains("Coberturas")
								&& !newcontenido.split("\n")[i].contains("Responsabilidad###Deducible")) {
	                    	if (newcontenido.split("\n")[i].split("###").length > 1) {
	                            EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();
	                            cobertura.setNombre(newcontenido.split("\n")[i].split("###")[0].trim());
	                            cobertura.setSa(newcontenido.split("\n")[i].split("###")[1].trim());
	                            if (newcontenido.split("\n")[i].split("###").length == 3 || newcontenido.split("\n")[i].split("###").length == 4) {
	                                cobertura.setDeducible(newcontenido.split("\n")[i].split("###")[2].trim());
	                            }
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
