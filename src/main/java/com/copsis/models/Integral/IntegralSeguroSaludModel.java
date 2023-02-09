package com.copsis.models.Integral;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraJsonModel;

public class IntegralSeguroSaludModel {
    private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
    public EstructuraJsonModel procesar(String contenido) {
        int inicio = 0;
		int fin = 0;
		
	    String  newcon = fn.remplazarMultiple(contenido, fn.remplazosGenerales());

        try {
            modelo.setTipo(3);
			modelo.setCia(83);
         
			// inicio = newcon.indexOf("PÓLIZA INDIVIDUAL");
			// fin = newcon.indexOf("DATOS DE LOS ASEGURADOS");		

			
			for (int i = 0; i < contenido.split("\n").length; i++) {
              System.out.print( contenido.split("\n")[i]);
                // if(newcontenido.toString().split("\n")[i].contains("PÓLIZA NO.") && newcontenido.toString().split("\n")[i].contains("AGENTE")){
                //     modelo.setPoliza(newcontenido.toString().split("\n")[i].split("PÓLIZA NO.")[1].split("AGENTE")[0].replace("###", "").trim());
                // }
                // if(newcontenido.toString().split("\n")[i].contains("NOMBRE:") && newcontenido.toString().split("\n")[i].contains("SOLICITUD NO.")){
                //     modelo.setCteNombre(newcontenido.toString().split("\n")[i].split("NOMBRE:")[1].replace("###", "").trim());
                // }
                // if(newcontenido.toString().split("\n")[i].contains("DOMICILIO")){
                //     modelo.setCteDireccion(newcontenido.toString().split("\n")[i+1]);
                //     if(modelo.getCteDireccion().length() > 0){
                //      modelo.setCp(modelo.getCteDireccion().split("CP")[1].trim().substring(0,5));
                //     }
                // } 
                // if(newcontenido.toString().split("\n")[i].contains("RFC")){
                //     modelo.setRfc(newcontenido.toString().split("\n")[i+1].split("###")[1]);
                // }
            }

            // inicio = contenido.indexOf("DATOS DE LOS ASEGURADOS");
			// fin = contenido.indexOf("ESCRIPCIÓN DE LA PÓLIZA");	
       
            // newcontenido = new StringBuilder();		
			// newcontenido.append( fn.extracted(inicio, fin, contenido));
            // System.out.print(newcontenido.toString().split("\n").length);
            // for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
              
            // }


            return modelo;
        } catch (Exception e) {
            e.printStackTrace();
            return modelo;
        }

    }
    
}
