package com.copsis.models.primero;

import java.util.ArrayList;
import java.util.List;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraCoberturasModel;
import com.copsis.models.EstructuraJsonModel;
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
		 int inicio = 0;
		 int fin = 0;
		
		try {
			modelo.setCia(49);
			modelo.setTipo(7);

			inicio = contenido.indexOf("PÓLIZA DE SEGURO PARA DAÑOS");
			fin = contenido.indexOf("Coberturas");
			   if (inicio > 0 && fin > 0 && inicio < fin) {
	                newcontenido = contenido.substring(inicio, fin).replace("@@@", "").replace("\r", "").replace("12:00", "").replace("12 Hrs", "");
	                for (int i = 0; i < newcontenido.split("\n").length; i++) {
	                	if(newcontenido.split("\n")[i].contains("SEGURO PARA DAÑOS")) {
	                		modelo.setPoliza(newcontenido.split("\n")[i].split("SEGURO PARA DAÑOS")[1].split("###")[1].replace("-", "").strip());
	                		modelo.setPolizaGuion(newcontenido.split("\n")[i].split("SEGURO PARA DAÑOS")[1].split("###")[1]);
	                	}
	                	if(newcontenido.split("\n")[i].contains("Fecha de Emisión")) {
	                		modelo.setFechaEmision(fn.formatDate_MonthCadena(newcontenido.split("\n")[i+1].split(" ")[0]));
	                	}
	                	if(newcontenido.split("\n")[i].contains("Asegurado:")) {
	                		modelo.setCteNombre(newcontenido.split("\n")[i].split("Asegurado:")[1].replace(":", "").replace("###", ""));
	                	}
	                	if(newcontenido.split("\n")[i].contains("Domicilio:")) {
	                		modelo.setCteDireccion(newcontenido.split("\n")[i].split("Domicilio:")[1].replace(":", "").replace("###", ""));
	                	}
	                	if (newcontenido.split("\n")[i].contains("Cp.")) {
							modelo.setCp(newcontenido.split("\n")[i].split("Cp.")[1].split(",")[0].strip());
						}
	                	if(newcontenido.split("\n")[i].contains("RFC:") || newcontenido.split("\n")[i].contains("Teléfono:")) {
	                		modelo.setRfc(newcontenido.split("\n")[i].split("RFC:")[1].split("Teléfono:")[0].replace("###", ""));
	                	}
	                	if(newcontenido.split("\n")[i].contains("Vigencia")) {
	                		if(newcontenido.split("\n")[i+1].split("-").length > 3) {
	                			modelo.setPlan(newcontenido.split("\n")[i+1].split("###")[0]);
	                			modelo.setVigenciaDe(fn.formatDate_MonthCadena(newcontenido.split("\n")[i+1].split("###")[2].strip()));
	                			modelo.setVigenciaA(fn.formatDate_MonthCadena(newcontenido.split("\n")[i+1].split("###")[4].strip()));
	                		}
	                	}
	                }
			   }
			   
			   
	            inicio = contenido.indexOf("Prima Neta");
	            fin = contenido.indexOf("EN CASO DE SINIESTRO");
	            
	            if (inicio > 0 && fin > 0 && inicio < fin) {
	                newcontenido = contenido.substring(inicio, fin).replace("@@@", "").replace("\r", "");
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

			
	            inicio = contenido.indexOf("Datos del Riesgo");
				fin = contenido.indexOf("Coberturas");
				List<EstructuraUbicacionesModel> ubicaciones = new ArrayList<>();
				  if (inicio > 0 && fin > 0 && inicio < fin) {					 
		                newcontenido = contenido.substring(inicio, fin).replace("@@@", "").replace("\r", "");
		                for (int i = 0; i < newcontenido.split("\n").length; i++) {    
		                	//System.out.println(newcontenido.split("\n")[i]);
		                }
				  }
	            
			
			return modelo;
		} catch (Exception e) {
		  return modelo;
		}
	}
}
