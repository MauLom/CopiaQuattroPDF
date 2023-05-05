package com.copsis.models.sura;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraJsonModel;

public class SuraVidaModel {
    private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
    public EstructuraJsonModel procesar(String contenido) {
		int inicio;
		int fin;
		StringBuilder newcontenido = new StringBuilder();
		contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());
      
        try {
            modelo.setTipo(5);
			modelo.setCia(88);
			
		
			inicio = contenido.indexOf("Oficina###Ramo###Póliza");
			fin = contenido.indexOf("Regla para determinar la suma asegurada por cobertura");			
			newcontenido.append( fn.extracted(inicio, fin, contenido));
		
			for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
                System.out.println(newcontenido.toString().split("\n")[i]);

                if(newcontenido.toString().split("\n")[i].contains("Póliza")){
                    
                }
            }
            
            return modelo;
        } catch (Exception ex) {
            modelo.setError(
                SuraVidaModel.this.getClass().getTypeName() + " | " + ex.getMessage() + " | " + ex.getCause());
            return modelo;
        }
    }
}
