package com.copsis.models.recibos;

import java.util.List;
import java.util.stream.Collectors;

import com.copsis.constants.ConstantsValue;
import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructurarReciboModel;

public class ReciboIzzyModel {
    private DataToolsModel fn = new DataToolsModel();

    public EstructurarReciboModel procesar(String contenido) {
        EstructurarReciboModel recibo = new EstructurarReciboModel();
        contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());

        String[] lines = contenido.split("\n");
  
        for (int x = 0; x < lines.length; x++) {
     
            if(lines[x].contains("Realiza") && lines[x].contains("pago")){
                recibo.setNombre(lines[x].split("Realiza")[0].replace("@@@", "").replace("###", " ").replace("\r", "").trim());
                recibo.setCalle(lines[x+1].replace("@@@", "").replaceAll("###|\\r", " ").trim());
                StringBuilder direccion = new StringBuilder();

                direccion.append(lines[x + 2].replace("###", " ").replace("\r", "").trim());
                if(lines[x + 4].split("###").length > 1){
                direccion.append(" "+lines[x + 3].split("###")[0].replaceAll("###|\\r", " ").trim());
                }
                
                if(lines[x + 4].split("###").length > 1){
                    direccion.append(" "+lines[x + 4].split("###")[0].replaceAll("###|\\r", " ").trim());
                }               
                recibo.setCteDireccion(direccion.toString().trim());
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
