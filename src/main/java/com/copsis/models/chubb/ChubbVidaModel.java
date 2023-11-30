package com.copsis.models.chubb;

import java.util.List;
import java.util.stream.Collectors;

import com.copsis.constants.ConstantsValue;
import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraJsonModel;

public class ChubbVidaModel {
    DataToolsModel fn = new DataToolsModel();
    EstructuraJsonModel modelo = new EstructuraJsonModel();

    public EstructuraJsonModel procesar(String contenido) {
        StringBuilder direccion = new StringBuilder();
        int inicio = 0;
        int fin = 0;
        StringBuilder newcontenido = new StringBuilder();
        contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());
        try {
            modelo.setTipo(3);
			modelo.setCia(1);
            inicio = contenido.indexOf("PÓLIZA DE SEGURO VIDA");
			fin = contenido.indexOf("Coberturas###Suma asegurada");			
			newcontenido.append( fn.extracted(inicio, fin, contenido));

            for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {        
                 
                if(newcontenido.toString().split("\n")[i].contains(ConstantsValue.POLIZA_ACENT2) && newcontenido.toString().split("\n")[i].contains("Vigencia:")){
                 modelo.setPoliza(newcontenido.toString().split("\n")[i].split(ConstantsValue.POLIZA_ACENT2)[1].split("Vigencia")[0].replace("###", "").trim());
                 List<String> valores = fn.obtenVigePoliza(newcontenido.toString().split("\n")[i]);
                 if(valores.size() ==2){
                    modelo.setVigenciaDe(fn.formatDateMonthCadena(valores.get(0)));
                    modelo.setFechaEmision(modelo.getVigenciaDe());
                    modelo.setVigenciaA(fn.formatDateMonthCadena(valores.get(1)));
                 }
                 
                }
                if(newcontenido.toString().split("\n")[i].contains("Asegurado:") && newcontenido.toString().split("\n")[i].contains("C.P:")){
                   modelo.setCteNombre(newcontenido.toString().split("\n")[i].split("Asegurado:")[1].split("C.P:")[0].replace("###", ""));
                   List<String> valores = fn.obtenerListNumeros2(newcontenido.toString().split("\n")[i]);
                   if(valores.size() ==2){
                    modelo.setCp(valores.get(1));
                   }
                   
                }
                if(newcontenido.toString().split("\n")[i].contains("Domicilio:") && newcontenido.toString().split("\n")[i].contains(ConstantsValue.TELEFONO)){
                    direccion.append(newcontenido.toString().split("\n")[i].split("Domicilio:")[1].split(ConstantsValue.TELEFONO)[0].replace("###", ""));
                }
                if(newcontenido.toString().split("\n")[i].contains("RFC:")){
                    direccion.append(" "+newcontenido.toString().split("\n")[i].split("RFC")[0].replace("###", ""));
                    modelo.setRfc(newcontenido.toString().split("\n")[i].split("RFC:")[1].replace("###", "").trim());
                }

                if(newcontenido.toString().split("\n")[i].contains("Moneda:") && newcontenido.toString().split("\n")[i].contains("Forma de pago:")){
                  modelo.setMoneda(fn.buscaMonedaEnTexto(newcontenido.toString().split("\n")[i]));
                  modelo.setFormaPago(fn.formaPagoSring(newcontenido.toString().split("\n")[i]));
                }
                if(newcontenido.toString().split("\n")[i].contains("Clave interna del agente:")){
                    List<String> valores = fn.obtenerListNumeros2(newcontenido.toString().split("\n")[i]);
                    if(!valores.isEmpty()){
                       modelo.setCveAgente(valores.get(0));
                        modelo.setAgente(newcontenido.toString().split("\n")[i].split("agente:")[1].split(modelo.getCveAgente())[1].replace("###", "").trim());
                    }else{
                        modelo.setAgente(newcontenido.toString().split("\n")[i].split("agente:")[1].replace("###", "").trim());
                    }
               
                }

                if(newcontenido.toString().split("\n")[i].contains("C.P:")){
                    List<String> valores = fn.obtenerListNumeros2(newcontenido.toString().split("\n")[i]);
                    if(!valores.isEmpty()){
                        modelo.setCp(valores.stream()
                            .filter(numero -> String.valueOf(numero).length() >= 4)
                            .collect(Collectors.toList()).get(0));
                    }
                 }
            
            }
            modelo.setCteDireccion(direccion.toString());

            inicio = contenido.indexOf("Prima Neta");
			fin = contenido.indexOf("Artículo###25");
            newcontenido = new StringBuilder();			
			newcontenido.append( fn.extracted(inicio, fin, contenido));

            for (int i = 0; i < newcontenido.toString().split("\n").length; i++) { 
            
                if(newcontenido.toString().split("\n")[i].contains("Prima Neta")){
                    List<String> valores = fn.obtenerListNumeros(newcontenido.toString().split("\n")[i]);
                    modelo.setPrimaneta(fn.castBigDecimal(fn.castDouble(valores.get(0))));
                }
                if(newcontenido.toString().split("\n")[i].contains("fraccionado")){
                    List<String> valores = fn.obtenerListNumeros(newcontenido.toString().split("\n")[i]);
                    modelo.setRecargo(fn.castBigDecimal(fn.castDouble(valores.get(0))));
                }
                if(newcontenido.toString().split("\n")[i].contains("Gastos de expedición")){
                    List<String> valores = fn.obtenerListNumeros(newcontenido.toString().split("\n")[i]);
                    modelo.setDerecho(fn.castBigDecimal(fn.castDouble(valores.get(0))));
                }
                if(newcontenido.toString().split("\n")[i].contains("I.V.A.")){
                    List<String> valores = fn.obtenerListNumeros(newcontenido.toString().split("\n")[i]);
                    modelo.setIva(fn.castBigDecimal(fn.castDouble(valores.get(0))));                    
                }
                if(newcontenido.toString().split("\n")[i].contains("Prima Total:")){
                    List<String> valores = fn.obtenerListNumeros(newcontenido.toString().split("\n")[i]);
                    modelo.setPrimaTotal(fn.castBigDecimal(fn.castDouble(valores.get(0))));
                }

            }    

            return modelo;
        } catch (Exception ex) {
            modelo.setError(
                ChubbVidaModel.this.getClass().getTypeName() + " | " + ex.getMessage() + " | " + ex.getCause());
        return modelo;
        }
    }
}
