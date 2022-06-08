package com.copsis.models.gmx;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraJsonModel;

public class GmxAutosModel {
	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
	private String contenido = "";

	public GmxAutosModel(String contenido) {
		this.contenido = contenido;
	}

	public EstructuraJsonModel procesar() {
		int inicio = 0;
		int fin = 0;
		StringBuilder newcontenido = new StringBuilder();
		contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());
		try {
			
			modelo.setTipo(7);
			modelo.setCia(17);
			
			inicio = contenido.indexOf("PÓLIZA DE SALUD");
			fin = contenido.indexOf("Asegurados");
			newcontenido.append( fn.extracted(inicio, fin, contenido));
			
			for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
				
			}
			
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}
}
