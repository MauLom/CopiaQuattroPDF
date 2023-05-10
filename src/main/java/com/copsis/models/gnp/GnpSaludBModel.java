package com.copsis.models.gnp;

import java.util.ArrayList;
import java.util.List;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraAseguradosModel;
import com.copsis.models.EstructuraJsonModel;

public class GnpSaludBModel {
    private DataToolsModel fn = new DataToolsModel();
    private EstructuraJsonModel modelo = new EstructuraJsonModel();

    public EstructuraJsonModel procesar(String contenido) {
        int inicio = 0;
        int fin = 0;
        StringBuilder newcontenido = new StringBuilder();
        contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());
        try {
            modelo.setTipo(3);
            modelo.setCia(18);

            inicio = contenido.indexOf("Póliza No.");
            fin = contenido.indexOf("Descripción del Movimiento");
            newcontenido.append(fn.extracted(inicio, fin, contenido));
            for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
                if(newcontenido.toString().split("\n")[i].contains("Póliza No.")){
                 modelo.setPoliza(newcontenido.toString().split("\n")[i].split("Póliza No.")[1].replace("###", ""));
                }
              
                if(newcontenido.toString().split("\n")[i].contains("Contratante") 
                && newcontenido.toString().split("\n")[i+1].contains("Vigencia de la Póliza")){                  
                    modelo.setCteNombre(newcontenido.toString().split("\n")[i+1].split("###")[0].replace("###", ""));
                }
                if(newcontenido.toString().split("\n")[i].contains("Desde las 12")){
                    String x= fn.gatos(newcontenido.toString().split("\n")[i].split("Desde las 12")[1].replace("hrs. del", "")).replace("###", "-").trim();
                   modelo.setVigenciaDe(fn.formatDateMonthCadena(x));
                
                }
                if(newcontenido.toString().split("\n")[i].contains("Hasta las 12")){
                    String x= fn.gatos(newcontenido.toString().split("\n")[i].split("Hasta las 12")[1].replace("hrs. del", "")).replace("###", "-").trim();
                    modelo.setVigenciaA(fn.formatDateMonthCadena(x));
                }
               
                if(newcontenido.toString().split("\n")[i].contains("Prima Neta")){
                    List<String> valores = fn.obtenerListNumeros(newcontenido.toString().split("\n")[i]);
                    modelo.setPrimaneta(fn.castBigDecimal(fn.castDouble(valores.get(0))));                 
                }
                if(newcontenido.toString().split("\n")[i].contains("Fraccionado")){
                    List<String> valores = fn.obtenerListNumeros(newcontenido.toString().split("\n")[i]);
                    modelo.setRecargo(fn.castBigDecimal(fn.castDouble(valores.get(0))));                 
                }
                if(newcontenido.toString().split("\n")[i].contains("Derecho de Póliza")){
                    List<String> valores = fn.obtenerListNumeros(newcontenido.toString().split("\n")[i]);
                    modelo.setDerecho(fn.castBigDecimal(fn.castDouble(valores.get(0))));                 
                }
                if(newcontenido.toString().split("\n")[i].contains("I.V.A.")){
                    List<String> valores = fn.obtenerListNumeros(newcontenido.toString().split("\n")[i]);
                    modelo.setIva(fn.castBigDecimal(fn.castDouble(valores.get(0))));                 
                }
                if(newcontenido.toString().split("\n")[i].contains("Pagar")){
                    List<String> valores = fn.obtenerListNumeros(newcontenido.toString().split("\n")[i]);
                    modelo.setPrimaTotal(fn.castBigDecimal(fn.castDouble(valores.get(0))));                 
                }
                                 
            }

            inicio = contenido.indexOf("Asegurado s");
            fin = contenido.indexOf("Vigencia de la Versión");
            newcontenido = new StringBuilder();
            newcontenido.append(fn.extracted(inicio, fin, contenido));
           
            List<EstructuraAseguradosModel> asegurados = new ArrayList<>();

            for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
                EstructuraAseguradosModel asegurado = new EstructuraAseguradosModel();
                if( newcontenido.toString().split("\n")[i].split("-").length > 2){
                    asegurado.setNombre(newcontenido.toString().split("###")[1].trim());
                    List<String> valores = fn.obtenVigePoliza(newcontenido.toString().split("\n")[i]);
                    System.out.println(newcontenido.toString().split("\n")[i]);
                }
            }

            return modelo;
        } catch (Exception e) {
            return modelo;
        }
    }
}
