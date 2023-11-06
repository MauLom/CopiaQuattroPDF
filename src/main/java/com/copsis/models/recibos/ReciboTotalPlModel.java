package com.copsis.models.recibos;

import java.util.List;
import java.util.stream.Collectors;

import com.copsis.constants.ConstantsValue;
import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructurarReciboModel;

public class ReciboTotalPlModel {
    private DataToolsModel fn = new DataToolsModel();

    public EstructurarReciboModel procesar(String contenido) {
        EstructurarReciboModel recibo = new EstructurarReciboModel();
        contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());
        String[] lines = contenido.split("\n");

        StringBuilder nombre=  new StringBuilder();
        StringBuilder direcion=  new StringBuilder();
        for (int x = 0; x < lines.length; x++) {
       
            if(lines[x].contains("Referencia")){               
              nombre.append(lines[x+1].replace("@@@", ""));
            }
            if(lines[x].contains("No. de Cuenta")){                 
              nombre.append(lines[x].split("No. de")[0].replace("###", ""));
              direcion.append(lines[x+2].replace("###", ""));
            }
            if(lines[x].contains(ConstantsValue.TELEFONOSP)){                              
              direcion.append(lines[x].split(ConstantsValue.TELEFONOSP)[0].replace("###", ""));
              direcion.append(" "+lines[x+1].replace("###", ""));
            }
        }
        recibo.setNombre(nombre.toString().replace("\r", " ") );
        recibo.setCteDireccion(direcion.toString().replace("\r", " ") );

        if(!recibo.getCteDireccion().isEmpty()){
            List<String> valores = fn.obtenerListNumeros2(recibo.getCteDireccion());
                recibo.setCp(valores.stream()
                        .filter(numero -> String.valueOf(numero).length() >= 4)
                        .collect(Collectors.toList()).get(0));
        }

       
        return recibo;
    }

}
