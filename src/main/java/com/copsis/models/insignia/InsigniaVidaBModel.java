package com.copsis.models.insignia;

import java.util.ArrayList;
import java.util.List;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraJsonModel;

public class InsigniaVidaBModel {
    private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();

      public EstructuraJsonModel procesar(String contenido) {
    	int inicio = 0;
		int fin = 0;
		List<String> arrayvigencias = new ArrayList<>();
		StringBuilder newcontenido = new StringBuilder();
		contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());
        try {
			modelo.setTipo(5);
			modelo.setCia(77);
			
			inicio = contenido.indexOf("PÓLIZA DE SEGURO");
			fin  = contenido.indexOf("INFORMACIÓN DEL ASEGURADO");	
			newcontenido = new StringBuilder();
			newcontenido.append(fn.extracted(inicio, fin, contenido));
			for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
			}
			return modelo;
		} catch (Exception ex) {
				modelo.setError(InsigniaVidaBModel.this.getClass().getTypeName() + " | " + ex.getMessage() + " | " + ex.getCause());
			return modelo;
		}
		
     
    }
    
}
