package com.copsis.models.sura;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraJsonModel;

public class SuraAutosModel {

	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
	private String contenido;
	private String newcontenido;
	private int  inicio;
	private int fin;

	public SuraAutosModel(String contenidox) {
		this.contenido = contenidox;
	}
	public EstructuraJsonModel procesar() {
		try {
			contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());
			modelo.setTipo(1);
			modelo.setCia(23);
			
			System.out.println(contenido);
			
			return modelo;
		} catch (Exception e) {
			return modelo;
	
			// TODO: handle exception
		}
	}
	
}
