package com.copsis.models.segurosMty;


import com.copsis.constants.ConstantsValue;
import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraJsonModel;

public class SegurosMtySalud2 {
    private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
	
	
	StringBuilder newcontenido = new StringBuilder();
    public EstructuraJsonModel procesar(String contenido) {
     int inicio = 0;
	 int fin = 0;
     contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());
         try {
            	modelo.setCia(39);			
			modelo.setTipo(3);
          
            inicio = contenido.indexOf(ConstantsValue.PLAN);
			fin = contenido.indexOf("COBERTURAS CONTRATADAS");
           
			newcontenido.append(fn.extracted(inicio, fin, contenido));
            return modelo;
         } catch (Exception ex) {
            modelo.setError(
                SegurosMtySalud2.this.getClass().getTypeName() + " - catch:" + ex.getMessage() + " | " + ex.getCause());
             return modelo;
         }
    }
}
