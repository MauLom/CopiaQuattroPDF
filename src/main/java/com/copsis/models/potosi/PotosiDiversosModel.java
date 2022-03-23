package com.copsis.models.potosi;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraJsonModel;

public class PotosiDiversosModel {
	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();	
	private String contenido = "";
	
	public PotosiDiversosModel(String contenido) {
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
			
			  return modelo;
		} catch (Exception e) {
	   return modelo;
		}
	}
}
