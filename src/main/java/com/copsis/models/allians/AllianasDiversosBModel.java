package com.copsis.models.allians;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraJsonModel;

public class AllianasDiversosBModel {
	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
	private String contenido = "";
	
	public  EstructuraJsonModel procesar(String contenido) {
		StringBuilder newcontenido = new StringBuilder();
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
					System.out.println("=========> " +newcontenido.toString().split("\n")[i]);
					System.out.println(fn.obtenVigePoliza(newcontenido.toString().split("\n")[i]));
					modelo.setFechaEmision(fn.formatDateMonthCadena(fn.obtenVigePoliza(newcontenido.toString().split("\n")[i]).get(0)));
					modelo.setVigenciaDe(fn.formatDateMonthCadena(fn.obtenVigePoliza(newcontenido.toString().split("\n")[i]).get(1)));
					modelo.setVigenciaA(fn.formatDateMonthCadena(fn.obtenVigePoliza(newcontenido.toString().split("\n")[i]).get(2)));
					
				}
				if(newcontenido.toString().split("\n")[i].contains("Razon Social")) {
					
				}
				if(newcontenido.toString().split("\n")[i].contains("Razon Social")) {
					
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
