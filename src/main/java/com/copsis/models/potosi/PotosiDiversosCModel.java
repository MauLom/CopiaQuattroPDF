package com.copsis.models.potosi;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraJsonModel;

public class PotosiDiversosCModel {
	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();	
	private String contenido = "";
	
	public PotosiDiversosCModel(String contenido) {
		this.contenido = contenido;
	}
	
	public EstructuraJsonModel procesar() {
		int inicio = 0;
		int fin = 0;
		StringBuilder newcontenido = new StringBuilder();
		contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());
		try {
			modelo.setTipo(7);
			modelo.setCia(37);
			
			inicio = contenido.indexOf("POLIZA DE SEGURO DE R ESPONSABILIDAD ");
			fin = contenido.indexOf("DETALLE DEL MOVIMIENTO");
			newcontenido.append(fn.extracted(inicio, fin, contenido));

			
		 return modelo;	
		} catch (Exception e) {
			 return modelo;	
		}
	}
}
