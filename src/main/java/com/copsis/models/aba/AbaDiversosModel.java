package com.copsis.models.aba;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraJsonModel;

public class AbaDiversosModel {
	
	// Clases
	DataToolsModel fn = new DataToolsModel();
	EstructuraJsonModel modelo = new EstructuraJsonModel();

	// Variables
	private String contenido;
	private int inicio = 0;
	private int fin = 0;
	private String newcontenido = "";

	

	// constructor
	public AbaDiversosModel(String contenido ) {
		this.contenido = contenido;

	}
	
	public EstructuraJsonModel procesar() {
		contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());
		try {
			//tipo
			modelo.setTipo(7);			
			//cia
			modelo.setCia(1);
			System.out.println(contenido);

			//Datos de la poliza
			inicio = contenido.indexOf("Póliza");
			fin = contenido.indexOf("Coberturas amparadas");
			
			
			
			
			
			return modelo;
			
		} catch (Exception ex) {
			modelo.setError(
					AbaDiversosModel.this.getClass().getTypeName() + " | " + ex.getMessage() + " | " + ex.getCause());
			return modelo;
		}
	}
	

}
