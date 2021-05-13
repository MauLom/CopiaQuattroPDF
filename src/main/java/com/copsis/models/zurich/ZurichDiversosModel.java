package com.copsis.models.zurich;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraJsonModel;

public class ZurichDiversosModel {
	// Clases
	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
	// Varaibles
	private String contenido = "";
	private String newcontenido = "";	
	private String  newcontenidoDire=""; 
	
	private int inicio = 0;
	private int fin = 0;
	
	public ZurichDiversosModel(String contenido) {
		this.contenido = contenido;
	}
	

}
