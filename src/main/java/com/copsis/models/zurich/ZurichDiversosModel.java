package com.copsis.models.zurich;

import java.util.ArrayList;
import java.util.List;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraCoberturasModel;
import com.copsis.models.EstructuraJsonModel;
import com.copsis.models.EstructuraUbicacionesModel;


public class ZurichDiversosModel {
	// Clases
	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
	// Varaibles
	private String contenido = "";

	

	
	public ZurichDiversosModel(String contenido) {
		this.contenido = contenido;
	}
	
	public EstructuraJsonModel procesar() {
		int inicio =0;
		int fin =0;
		boolean contratancte = true;
		StringBuilder newcont = new StringBuilder();
		contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());
		
		try {
			  modelo.setTipo(7);
			  modelo.setCia(44);
			
			inicio = contenido.indexOf("Producto");
			fin = contenido.indexOf("Coberturas Amparadas");

			if(inicio  > -1 && fin > -1 && inicio < fin) {
				newcont.append(contenido.substring(inicio,fin).replace("@@@", "").replace("\r", "").trim());
				for (int i = 0; i < newcont.toString().split("\n").length; i++) {		
					if( newcont.toString().split("\n")[i].contains("Moneda")) {						
						modelo.setMoneda(fn.buscaMonedaEnTexto(newcont.toString().split("\n")[i]));
					}
					if( newcont.toString().split("\n")[i].contains("Agente")) {
						modelo.setAgente( newcont.toString().split("\n")[i].split("Agente")[1].replace("###", "").replace(":", "").trim());
					}
					if( newcont.toString().split("\n")[i].contains("Póliza") && newcont.toString().split("\n")[i].contains("Documento")) {
						modelo.setPoliza( newcont.toString().split("\n")[i].split("Póliza")[1].split("Documento")[0].replace("###", "").replace(":", "").trim());
					}
					if( newcont.toString().split("\n")[i].contains("Vigencia")&& newcont.toString().split("\n")[i].contains("Desde") && newcont.toString().split("\n")[i].contains("Fecha")) {
						modelo.setVigenciaDe(fn.formatDateMonthCadena( newcont.toString().split("\n")[i].split("Vigencia")[1].split("Desde")[1].split("Fecha")[0].replace("###", "").replace(":", "").replace("el", "").replace("de", "-").replace(" ", "").trim().toUpperCase()));
					}
					
					if( newcont.toString().split("\n")[i].contains("Hasta")&& newcont.toString().split("\n")[i].contains("Folio") ) {
						modelo.setVigenciaA(fn.formatDateMonthCadena( newcont.toString().split("\n")[i].split("Hasta")[1].split("Folio")[0].replace("###", "").replace(":", "").replace("el", "").replace("de", "-").replace(" ", "").trim().toUpperCase()));
					}
					if( newcont.toString().split("\n")[i].contains("Asegura") && contratancte) {
						modelo.setCteNombre( newcont.toString().split("\n")[i+1]);
						contratancte=false;
						
					}
					if( newcont.toString().split("\n")[i].contains("R.F.C.")) {
						modelo.setRfc( newcont.toString().split("\n")[i].split("R.F.C.")[1].replace("###", "").replace("-", "").trim());
						modelo.setCteDireccion(newcont.toString().split("\n")[i+1]);
					}
				}
			}
			inicio = contenido.indexOf("Producto");
			fin = contenido.indexOf("Coberturas Amparadas");

			if(inicio  > -1 && fin > -1 && inicio < fin) {
				newcont = new StringBuilder();
				newcont.append(contenido.substring(inicio,fin).replace("@@@", "").replace("\r", "").trim());
				List<EstructuraUbicacionesModel> ubicaciones = new ArrayList<>();
				 EstructuraUbicacionesModel ubicacion = new EstructuraUbicacionesModel();
				for (int i = 0; i < newcont.toString().split("\n").length; i++) {	
					

					 if(newcont.toString().split("\n")[i].contains("Colonia")) {
						 ubicacion.setColonia(newcont.toString().split("\n")[i]);
					 }
					 if(newcont.toString().split("\n")[i].contains("Pisos") && newcont.toString().split("\n")[i].contains("Estructura")) {
						ubicacion.setNiveles(fn.castInteger(newcont.toString().split("\n")[i].split("Pisos")[1].split("Estructura")[0].replace(":", "").replace("###", "").trim())); 
					 }
					 if(newcont.toString().split("\n")[i].contains("Muros") && newcont.toString().split("\n")[i].contains("Entrepiso")) {
						 ubicacion.setMuros(fn.material(newcont.toString().split("\n")[i].split("Muros")[1].split("Entrepiso")[0].replace(":", "").replace("###", "").trim().toLowerCase()));
					 }
					 if(newcont.toString().split("\n")[i].contains("Uso") && newcont.toString().split("\n")[i].contains("Zona Alfa")) {
						 ubicacion.setGiro(newcont.toString().split("\n")[i].split("Uso")[1].split("Zona")[0].replace(":", "").replace("###", "").trim());
					 }
					 
				}
				ubicaciones.add(ubicacion);
				modelo.setUbicaciones(ubicaciones);
			}
			

			inicio = contenido.indexOf("Coberturas Amparadas");
			fin = contenido.indexOf("Prima neta");

			if(inicio  > -1 && fin > -1 && inicio < fin) {
				newcont = new StringBuilder();
				newcont.append(contenido.substring(inicio,fin).replace("@@@", "").replace("\r", "").trim());
				 List<EstructuraCoberturasModel> coberturas = new ArrayList<>();
				for (int i = 0; i < newcont.toString().split("\n").length; i++) {
					EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();
					if(!newcont.toString().split("\n")[i].contains("Coberturas")) {
						if(newcont.toString().split("\n")[i].split("###").length == 3) {
							cobertura.setNombre(newcont.toString().split("\n")[i].split("###")[0]);
							cobertura.setSa(newcont.toString().split("\n")[i].split("###")[1]);
							coberturas.add(cobertura);
						
						}
					}
					
				}
				modelo.setCoberturas(coberturas);
			}
			
	
			
			inicio = contenido.indexOf("Prima neta");
			fin = contenido.indexOf("Si el contenido");

			if(inicio  > -1 && fin > -1 && inicio < fin) {
				newcont = new StringBuilder();
				newcont.append(contenido.substring(inicio,fin).replace("@@@", "").replace("\r", "").trim());
				for (int i = 0; i < newcont.toString().split("\n").length; i++) {
					if(newcont.toString().split("\n")[i].contains("Prima neta")) {
						modelo.setPrimaneta( fn.castBigDecimal(fn.castDouble(newcont.toString().split("\n")[i].split("neta")[1].replace("###", "").trim())));
					 }
					if(newcont.toString().split("\n")[i].contains("expedición")) {
						modelo.setDerecho( fn.castBigDecimal(fn.castDouble(newcont.toString().split("\n")[i].split("expedición")[1].replace("###", "").trim())));
					 }
					if(newcont.toString().split("\n")[i].contains("I. V. A.")) {
						modelo.setIva( fn.castBigDecimal(fn.castDouble(newcont.toString().split("\n")[i].split("I. V. A.")[1].replace("###", "").trim())));
					 }
					if(newcont.toString().split("\n")[i].contains("Prima total")) {
						modelo.setPrimaTotal( fn.castBigDecimal(fn.castDouble(newcont.toString().split("\n")[i].split("total")[1].replace("###", "").trim())));
					 }
					
				}
			}

			inicio =  contenido.indexOf("Forma de pago:");
			if(inicio > -1) {
				modelo.setFormaPago(fn.formaPagoSring(contenido.split("Forma de pago:")[1]));
			}
			
			return modelo;
		} catch (Exception e) {
			 return modelo;
		}
	}
	

}
