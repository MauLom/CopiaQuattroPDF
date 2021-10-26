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
	                            modelo.setVigenciaDe(fn.formatDate_MonthCadena(newcontenido.split("\n")[i + 1].split("###")[1].trim()));
	                            modelo.setVigenciaA(fn.formatDate_MonthCadena(newcontenido.split("\n")[i + 1].split("###")[3].trim()));
	                            modelo.setFechaEmision(modelo.getVigenciaDe());
	                        } else {
	                            modelo.setVigenciaDe(fn.formatDate_MonthCadena(newcontenido.split("\n")[i + 1].split("###")[2].trim()));
	                            modelo.setVigenciaA(fn.formatDate_MonthCadena(newcontenido.split("\n")[i + 1].split("###")[4].trim()));
	                            modelo.setFechaEmision(modelo.getVigenciaDe());
	                        }
	                    }
	                    if (newcontenido.split("\n")[i].contains("Clave") && newcontenido.split("\n")[i].contains("Marca")) {
	                        modelo.setClave(newcontenido.split("\n")[i + 1].split("###")[0]);
	                        modelo.setMarca(newcontenido.split("\n")[i + 1].split("###")[1].split("-")[0]);
	                        modelo.setDescripcion(newcontenido.split("\n")[i + 1].split("###")[1].split("-")[1]);
	                        modelo.setModelo(Integer.parseInt(newcontenido.split("\n")[i + 1].split("###")[2]));
	                    }
	                    if (newcontenido.split("\n")[i].contains("Placas") && newcontenido.split("\n")[i].contains("Serie")) {

	                        if (newcontenido.split("\n")[i + 1].split("###").length == 4) {

	                            modelo.setSerie(newcontenido.split("\n")[i + 1].split("###")[1]);
	                        } else {
	                            modelo.setPlacas(newcontenido.split("\n")[i + 1].split("###")[0]);
	                            modelo.setSerie(newcontenido.split("\n")[i + 1].split("###")[2]);
	                        }

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
	                      }
	                  }
	            }
	            
	            
	            inicio = contenido.indexOf("Prima Neta");
	            fin = contenido.indexOf("EN CASO DE SINIESTRO");
	            
	            if (inicio > 0 && fin > 0 && inicio < fin) {
	                newcontenido = contenido.substring(inicio, fin).replace("@@@", "").replace("\r", "");
	                for (int i = 0; i < newcontenido.split("\n").length; i++) {                  
	                    if (newcontenido.split("\n")[i].contains("Prima Neta###Financiamiento###Gastos de Expedición###Subtotal###IVA###Total")) {
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
	            
	            
	            //Coberturas
	            inicio = contenido.indexOf("Coberturas Amparadas");
	            fin = contenido.indexOf("Prima Neta");

	            if (inicio > 0 && fin > 0 && inicio < fin) {
	            	List<EstructuraCoberturasModel> coberturas = new ArrayList<>();
	                newcontenido = contenido.substring(inicio, fin).replace("@@@", "").replace("\r", "");
	                for (int i = 0; i < newcontenido.split("\n").length; i++) {

	                    if (newcontenido.split("\n")[i].split("###")[0].contains("Coberturas Amparadas") || newcontenido.split("\n")[i].split("###")[0].contains("Coberturas")) {
	                    } else {
	                        if (newcontenido.split("\n")[i].split("###").length > 1) {
	                            EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();
	                            cobertura.setNombre(newcontenido.split("\n")[i].split("###")[0].trim());
	                            cobertura.setSa(newcontenido.split("\n")[i].split("###")[1].trim());
	                            if (newcontenido.split("\n")[i].split("###").length == 4) {
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
