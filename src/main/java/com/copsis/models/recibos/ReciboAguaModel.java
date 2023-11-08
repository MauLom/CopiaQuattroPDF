package com.copsis.models.recibos;

import java.util.List;
import java.util.stream.Collectors;

import com.copsis.constants.ConstantsValue;
import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructurarReciboModel;

public class ReciboAguaModel {
     private DataToolsModel fn = new DataToolsModel();
      public EstructurarReciboModel procesar(String contenido){
         EstructurarReciboModel recibo = new EstructurarReciboModel();
         
        contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales())
        .replace("U S U A RIO", " USUARIO")
        .replace("D I R E C C I Ó N D E", "DIRECCIÓN DE")
        .replace("C O N S U M O", "CONSUMO")
        .replace("VENCIM I E N T O", ConstantsValue.VENCIMIENTO).replace("FAC T U R A D O", "FACTURADO");
        
        String[] lines = contenido.split("\n");
      

        for (int x = 0; x < lines.length; x++) {
            if (lines[x].contains("USUARIO") && !lines[x].contains("DATOS DEL")){            
             recibo.setNombre(lines[x].split("USUARIO")[1].replace("@@@", "").replace("###", " ").replace("\r", "").trim());
            }
             if (lines[x].contains("DIRECCIÓN") && lines[x].contains(ConstantsValue.ENVIO) &&  lines[x].contains(ConstantsValue.VENCIMIENTO)){
               recibo.setCalle(lines[x].split(ConstantsValue.ENVIO)[1].split(ConstantsValue.VENCIMIENTO)[0].replace("###", " ").replace("\r", "").trim());
       
             StringBuilder direccion = new StringBuilder();
                if(lines[x + 1].contains("MES") ){
                    direccion.append(lines[x + 1].split("MES")[0].replace("###", " ").replace("\r", "").trim());
                }
                 direccion.append(" "+lines[x + 2].replace("###", " ").replace("\r", "").trim());
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
