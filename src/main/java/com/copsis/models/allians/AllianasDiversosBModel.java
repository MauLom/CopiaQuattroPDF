package com.copsis.models.allians;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraCoberturasModel;
import com.copsis.models.EstructuraJsonModel;
import com.copsis.models.EstructuraUbicacionesModel;

public class AllianasDiversosBModel {
	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
	private String contenido = "";
	
	public  EstructuraJsonModel procesar(String contenido) {
		StringBuilder direccion = new StringBuilder();
		StringBuilder newcontenido = new StringBuilder();
		StringBuilder newcbo = new StringBuilder();
		int inicio = 0;
		int fin = 0;		
		contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());
		try {
			modelo.setTipo(7);		
			modelo.setCia(4);

			
			inicio = contenido.indexOf("Razon Social Asegurado");
			fin = contenido.indexOf("Datos de la ubicación Asegurada");
			newcontenido.append( fn.extracted(inicio, fin, contenido));
			for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
			
				if(newcontenido.toString().split("\n")[i].contains("Razon Social")) {
					modelo.setCteNombre(newcontenido.toString().split("\n")[i+1].split("###")[0]);	
				}
				if(newcontenido.toString().split("\n")[i].split("-").length > 3) {
					modelo.setPoliza(newcontenido.toString().split("\n")[i].split("###")[0]);
					modelo.setMoneda(fn.buscaMonedaEnTexto(newcontenido.toString().split("\n")[i]));
					modelo.setFormaPago(fn.formaPagoSring(newcontenido.toString().split("\n")[i]));
					modelo.setFechaEmision(fn.formatDateMonthCadena(fn.obtenVigePoliza(newcontenido.toString().split("\n")[i]).get(0)));
					modelo.setVigenciaDe(fn.formatDateMonthCadena(fn.obtenVigePoliza(newcontenido.toString().split("\n")[i]).get(1)));
					modelo.setVigenciaA(fn.formatDateMonthCadena(fn.obtenVigePoliza(newcontenido.toString().split("\n")[i]).get(2)));					
				}
				if(newcontenido.toString().split("\n")[i].contains("Agente:")) {					
					modelo.setCveAgente(fn.numTx(newcontenido.toString().split("\n")[i].split("Agente:")[1].replace("###", "")));
					modelo.setAgente(newcontenido.toString().split("\n")[i].split("Agente:")[1].split(modelo.getCveAgente())[1].replace("###", "").replace(" . ,", "").trim());
				}
				if(newcontenido.toString().split("\n")[i].contains("RFC")) {
					modelo.setRfc(newcontenido.toString().split("\n")[i].split("RFC:")[1].replace("###", "").trim());
				}
				if(newcontenido.toString().split("\n")[i].contains("Calle")) {
					direccion.append(newcontenido.toString().split("\n")[i].split("Calle:")[1].replace("###", "").trim());
				}
				if(newcontenido.toString().split("\n")[i].contains("Colonia")) {
					direccion.append(newcontenido.toString().split("\n")[i].split("Colonia:")[1].replace("###", "").trim());
				}
				if(newcontenido.toString().split("\n")[i].contains("Población")) {
					direccion.append("  " +newcontenido.toString().split("\n")[i].split("Población:")[1].replace("###", "").trim() +" ");
				}				
				if(newcontenido.toString().split("\n")[i].contains("País")) {
	        		direccion.append(newcontenido.toString().split("\n")[i].split("País:")[1].replace("###", "").trim());
				}
				if(newcontenido.toString().split("\n")[i].contains("Estado")) {
					direccion.append(newcontenido.toString().split("\n")[i].split("Estado:")[1].replace("###", "").trim());				
				}				
				if(newcontenido.toString().split("\n")[i].contains("C.P:")) {
	        		modelo.setCp(newcontenido.toString().split("\n")[i].split("C.P:")[1].replace("###", "").trim());
				}				
			}			
			modelo.setCteDireccion(direccion.toString());
			
			
	
			//Coberturas
			inicio = contenido.indexOf("Sumas Aseguradas y Sublímites");
			fin = contenido.indexOf("Otros");
			newcontenido = new StringBuilder();
			newcontenido.append( fn.extracted(inicio, fin, contenido));
		   for (int i = 0; i < newcontenido.toString().split("Cobertura###Suma Asegurada").length; i++) {
			   if(i > 0 ) {
				  
					   newcbo.append(newcontenido.toString().split("Cobertura###Suma Asegurada")[i]);
				  
			   }
			
		}
		
			List<EstructuraCoberturasModel> coberturas = new ArrayList<>();
			for (int i = 0; i < newcbo.toString().split("\n").length; i++) {	
				EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();
				
				if(!newcbo.toString().split("\n")[i].contains("Cobertura") &&
				   !newcbo.toString().split("\n")[i].contains("Daño Físico")
				  && !newcbo.toString().split("\n")[i].contains("C.P.")
				   &&  newcbo.toString().split("\n")[i].split("###").length > 1) {
					cobertura.setNombre(newcbo.toString().split("\n")[i].split("###")[0]);
					cobertura.setSa(newcbo.toString().split("\n")[i].split("###")[1]);
					coberturas.add(cobertura);
				}
			}
			modelo.setCoberturas(coberturas);
			
			

			inicio = contenido.indexOf("Datos de la ubicación Asegurada");
			fin = contenido.indexOf("Año Construcción");
			newcontenido = new StringBuilder();
			newcontenido.append( fn.extracted(inicio, fin, contenido));
			List<EstructuraUbicacionesModel> ubicaciones = new ArrayList<>();
			EstructuraUbicacionesModel ubicacion = new EstructuraUbicacionesModel();
			
			for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {				
				if(newcontenido.toString().split("\n")[i].contains("Calle:")) {
					ubicacion.setCalle(newcontenido.toString().split("\n")[i].split("Calle:")[1].replace("###", "").trim());	
				}				
				if(newcontenido.toString().split("\n")[i].contains("Tipo Muros:") &&  newcontenido.toString().split("\n")[i].contains("Tipo Muros:")) {			
					ubicacion.setTechos(fn.material(newcontenido.toString().split("\n")[i].split("Tipo Techo:")[1].replace("###", "")));					
					ubicacion.setMuros(fn.material(newcontenido.toString().split("\n")[i].split("Muros:")[1].split("Tipo Techo:")[0].replace("###", "")));
				}
				if(newcontenido.toString().split("\n")[i].contains("Nivel del Suelo:")) {
					if(newcontenido.toString().split("\n")[i].contains("5")) {
						ubicacion.setNiveles(5);
					}
				}
				
			}
			
			ubicaciones.add(ubicacion);
			modelo.setUbicaciones(ubicaciones);
			
			
			
			
			inicio = contenido.indexOf("Prima Neta");
			fin = contenido.indexOf("En caso de que");
			newcontenido = new StringBuilder();
			newcontenido.append( fn.extracted(inicio, fin, contenido));
			for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {					
				if(newcontenido.toString().split("\n")[i].contains("Prima Neta")) {
					 List<String> valores = fn.obtenerListNumeros(newcontenido.toString().split("\n")[i+1]);
					  modelo.setPrimaneta(fn.castBigDecimal(fn.castDouble(valores.get(0))));
		       		  modelo.setRecargo(fn.castBigDecimal(fn.castDouble(valores.get(1))));
		       		  modelo.setDerecho(fn.castBigDecimal(fn.castDouble(valores.get(2))));
		       		  modelo.setIva(fn.castBigDecimal(fn.castDouble(valores.get(3))));		       		  
		       		  modelo.setPrimaTotal(fn.castBigDecimal(fn.castDouble(valores.get(4))));   
				}				
			}
			

			
			
		
			return modelo;
		} catch (Exception ex) {
			modelo.setError(AllianasDiversosBModel.this.getClass().getTypeName() + " | " + ex.getMessage() + " | "
					+ ex.getCause());
			return modelo;
		}
	}
}
