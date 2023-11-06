package com.copsis.models.recibos;

import java.util.List;
import java.util.stream.Collectors;

import com.copsis.constants.ConstantsValue;
import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructurarReciboModel;

public class ReciboCfeModel {
 private DataToolsModel fn = new DataToolsModel();
   
    public EstructurarReciboModel procesar(String contenido){
         EstructurarReciboModel recibo = new EstructurarReciboModel();
        contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());
        
        String[] lines = contenido.split("\n");
        String nombre = "";

        for (int x = 0; x < lines.length; x++) {
            if (lines[x].contains("TOTAL A PAGAR:") && lines[x].split("###").length < 2) {
                nombre = lines[x - 1].replace("###", "").replace("@@@", "").trim();
            }
            if (lines[x].contains("TOTAL A PAGAR:")) {
                if (lines[x].split("###").length > 1) {
                    nombre = lines[x].split(ConstantsValue.TOTAL_MAYUS)[0].replace("###", "").replace("@@@", "").trim();
                }

                StringBuilder direccion = new StringBuilder();
                direccion.append(lines[x + 1]);
                direccion.append(lines[x + 2].split("###")[0] + " ");
                direccion.append(lines[x + 3].split("###")[0] + " ");
                direccion.append(lines[x + 4].split("###")[0]);

                recibo.setNombre(nombre);
                recibo.setCteDireccion(direccion.toString().replace("@@@", "").replace("\r", " "));

                List<String> valores = fn.obtenerListNumeros2(recibo.getCteDireccion().split("C.P.")[1]);
                recibo.setCp(valores.stream()
                        .filter(numero -> String.valueOf(numero).length() >= 4)
                        .collect(Collectors.toList()).get(0));
            }
        }

        return recibo;

    }
}
