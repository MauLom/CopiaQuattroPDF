package com.copsis.models.recibos;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructurarReciboModel;

public class ReciboAguaModel {
     private DataToolsModel fn = new DataToolsModel();
      public EstructurarReciboModel procesar(String contenido){
         EstructurarReciboModel recibo = new EstructurarReciboModel();
         //System.out.println( fn.remplazarMultiple(contenido, fn.remplazosGenerales()));
        contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales())
        .replace("U S U A RIO", " USUARIO")
        .replace("D I R E C C I Ó N D E", "DIRECCIÓN DE")
        .replace("C O N S U M O", "CONSUMO")
        .replace("VENCIM I E N T O", "VENCIMIENTO").replace("FAC T U R A D O", "FACTURADO");
        System.out.println(contenido);
        String[] lines = contenido.split("\n");
        String nombre = "";

        for (int x = 0; x < lines.length; x++) {
            //System.out.println(lines[x]);
            recibo.setNombre(lines[0].replace("@@@", ""));

        }

        return recibo;
      }
}
