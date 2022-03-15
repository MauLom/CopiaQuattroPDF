package com.copsis.models.insignia;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraJsonModel;

public class InsigniaVidaModel {
	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
	private String contenido = "";
	
    public  InsigniaVidaModel(String contenido){
    	this.contenido = contenido;
    }
    
    public EstructuraJsonModel procesar() {
    	int inicio = 0;
		int fin = 0;
		StringBuilder newcontenido = new StringBuilder();
		contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());
		
		try {
			
			modelo.setTipo(5);
			modelo.setCia(77);
			
			return modelo;
		} catch (Exception ex) {
			modelo.setError(InsigniaVidaModel.this.getClass().getTypeName() + " | " + ex.getMessage() + " | " + ex.getCause());
			return modelo;
		}
    }
}
