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
        
            if (x== 1 && lines[x].contains("TOTAL A PAGAR:") && lines[x].split("###").length < 2) {
                nombre = lines[x - 1].replace("###", "").replace("@@@", "").trim();
            }
            if (lines[x].contains("TOTAL A PAGAR:")) {
                if (lines[x].split("###").length > 1) {
                    nombre = lines[x].split(ConstantsValue.TOTAL_MAYUS)[0].replace("###", "").replace("@@@", "").trim();
                }

                StringBuilder direccion = new StringBuilder();
                recibo.setCalle(lines[x + 1].replace("###", "").replace("@@@", "").trim());
                direccion.append(lines[x + 2].split("###")[0] + " ");
                direccion.append(lines[x + 3].split("###")[0] + " ");
                direccion.append(lines[x + 4].split("###")[0]);
                if(lines[x + 5].contains(ConstantsValue.CODIGO_POSTALPT)){
                    direccion.append(lines[x + 5]);
                }

                 recibo.setNombre(nombre);
                 recibo.setCteDireccion(direccion.toString().replace("@@@", "").replace("\r", " "));

                getCodigoPostal(recibo);
               
               
            }
        }

        return recibo;

    }

    private void getCodigoPostal(EstructurarReciboModel recibo) {
        if(recibo.getCteDireccion().contains(ConstantsValue.CODIGO_POSTALPT)){
        List<String> valores = fn.obtenerListNumeros2(recibo.getCteDireccion().split(ConstantsValue.CODIGO_POSTALPT)[1]);
                if(!valores.isEmpty()){
                    recibo.setCp(valores.stream()
                        .filter(numero -> String.valueOf(numero).length() >= 4)
                        .collect(Collectors.toList()).get(0));
                }
        }
    }
}
