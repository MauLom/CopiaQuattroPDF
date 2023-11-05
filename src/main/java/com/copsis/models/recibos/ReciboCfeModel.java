package com.copsis.models.recibos;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructurarReciboModel;

public class ReciboCfeModel {
     private DataToolsModel fn = new DataToolsModel();
    EstructurarReciboModel recibo = new EstructurarReciboModel();
    public EstructurarReciboModel procesar(String contenido){
        contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());
        try {
       
            for(int x =0; x< contenido.split("\n").length;x++){
                 if(contenido.split("\n")[x].contains("TOTAL A PAGAR:")){
                   recibo.setNombre(contenido.split("\n")[x].split("TOTAL")[0].replace("###", "").replace("@@@", "").trim());
                 }
            }

            return recibo;
        } catch (Exception e) {
            return recibo;
        }

    }
}
