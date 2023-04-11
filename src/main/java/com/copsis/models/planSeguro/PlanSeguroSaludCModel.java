package com.copsis.models.planSeguro;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraJsonModel;

public class PlanSeguroSaludCModel {
    private DataToolsModel fn = new DataToolsModel();
    private EstructuraJsonModel modelo = new EstructuraJsonModel();

    public EstructuraJsonModel procesar(String contenido) {
        int inicio = 0;
        int fin = 0;
        StringBuilder newcontenido = new StringBuilder();
        contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());
        try {
            modelo.setTipo(3);
            modelo.setCia(25);

            inicio = contenido.indexOf("Número de Póliza");
            fin = contenido.indexOf("Lista de Asegurados");        
            newcontenido.append(fn.extracted(inicio, fin, contenido));
            
            for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {  
                //System.out.println(newcontenido.toString().split("\n")[i]);
             if(newcontenido.toString().split("\n")[i].contains("Número de Póliza") && newcontenido.toString().split("\n")[i+1].contains("Desde")){                            
                modelo.setVigenciaDe(fn.formatDateMonthCadena(fn.obtenVigePoliza(newcontenido.toString().split("\n")[i+1]).get(0)));
                modelo.setPoliza(newcontenido.toString().split("\n")[i+2].split("###")[0]);                  
                modelo.setVigenciaA(fn.formatDateMonthCadena(fn.obtenVigePoliza(newcontenido.toString().split("\n")[i+2]).get(0)));
             }

             if(newcontenido.toString().split("\n")[i].contains("Datos del Contratante") && newcontenido.toString().split("\n")[i+1].contains("Nombre:")
             &&newcontenido.toString().split("\n")[i+1].contains("RFC:")){
                System.out.println(newcontenido.toString().split("\n")[i+1]);

             }

            }


            return modelo;
        } catch (Exception ex) {
            modelo.setError(PlanSeguroSaludCModel.this.getClass().getTypeName() + " | " + ex.getMessage() + " | "
                    + ex.getCause());
            return modelo;
        }
    }
}
