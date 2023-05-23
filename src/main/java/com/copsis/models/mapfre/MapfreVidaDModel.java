package com.copsis.models.mapfre;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraJsonModel;

public class MapfreVidaDModel {
    private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
    public EstructuraJsonModel procesar(String contenido) {
        int inicio = 0;
		int fin = 0;
        contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());
        StringBuilder newcontenido = new StringBuilder();
        try {
            modelo.setTipo(5);
			modelo.setCia(22);

             
            inicio = contenido.indexOf("PLAN:");
			fin = contenido.indexOf("COBERTURAS CONTRATADAS");
			newcontenido.append(fn.extracted(inicio, fin, contenido).replace("las###12:00 P.M.", ""));
            for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
            
            }
            
            return modelo;
        } catch (Exception e) {
             return modelo;
        }
    }
}
