package com.copsis.models.gnp;

import java.util.List;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraJsonModel;

public class GnpDiversosDModelo {
    private DataToolsModel fn = new DataToolsModel();
    private EstructuraJsonModel modelo = new EstructuraJsonModel();
    public EstructuraJsonModel procesar(String contenido) {
        int inicio = 0;
        int fin = 0;
        
        StringBuilder newcontenido = new StringBuilder();
        contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales())
        .replace("RESUMEN###DE###LA###PÓLIZA", "RESUMEN DE LA PÓLIZA")
        .replace("Prima###del###movimiento", "Prima del movimiento")
        .replace("INFORMACIÓN###ADICIONAL", "INFORMACIÓN ADICIONAL");
        try {
            modelo.setTipo(7);			
			modelo.setCia(18);
            inicio = contenido.indexOf("RESUMEN DE LA PÓLIZA");
			fin = contenido.indexOf("Prima del movimiento");		
           
			newcontenido.append( fn.extracted(inicio, fin, contenido));
			
			for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
                if(newcontenido.toString().split("\n")[i].contains("Cliente") && newcontenido.toString().split("\n")[i].contains("Nombre")){
                 modelo.setCteNombre(newcontenido.toString().split("\n")[i+1].split("###")[1]);
                }
                if(newcontenido.toString().split("\n")[i].contains("Vigencia") && newcontenido.toString().split("\n")[i+1].contains("Desde")){                 
                    modelo.setVigenciaDe(fn.formatDateMonthCadena(fn.obtenVigePoliza2(newcontenido.toString().split("\n")[i+1].toUpperCase()).get(0)));
                    modelo.setFechaEmision(modelo.getVigenciaDe());
                }
                if(newcontenido.toString().split("\n")[i].contains("RFC") && newcontenido.toString().split("\n")[i].contains("Hasta")){
                    modelo.setVigenciaA(fn.formatDateMonthCadena(fn.obtenVigePoliza2(newcontenido.toString().split("\n")[i].toUpperCase()).get(0)));
                }
                if(newcontenido.toString().split("\n")[i].contains("C.P") ){
                 modelo.setCp(newcontenido.toString().split("\n")[i].split("C.P")[1].trim().substring(0, 5));
                }
                
            }

           
            inicio = contenido.indexOf("Prima del movimiento");
			fin = contenido.indexOf("INFORMACIÓN ADICIONAL");		
            newcontenido =new StringBuilder()           ;
			newcontenido.append( fn.extracted(inicio, fin, contenido));

            for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
                if(newcontenido.toString().split("\n")[i].contains("Prima###Neta")){
                    List<String> valores = fn.obtenerListNumeros(newcontenido.toString().split("\n")[i]);
                    modelo.setPrimaneta(fn.castBigDecimal(fn.castDouble(valores.get(0))));
                }
                if(newcontenido.toString().split("\n")[i].contains("Fraccionado")){
                    List<String> valores = fn.obtenerListNumeros(newcontenido.toString().split("\n")[i]);
                    modelo.setRecargo(fn.castBigDecimal(fn.castDouble(valores.get(0))));
                }

                if(newcontenido.toString().split("\n")[i].contains("Derecho")){
                    List<String> valores = fn.obtenerListNumeros(newcontenido.toString().split("\n")[i]);
                    modelo.setDerecho(fn.castBigDecimal(fn.castDouble(valores.get(0))));
                }
                if(newcontenido.toString().split("\n")[i].contains("IVA")){
                    List<String> valores = fn.obtenerListNumeros(newcontenido.toString().split("\n")[i]);
                    modelo.setIva(fn.castBigDecimal(fn.castDouble(valores.get(0))));             
                }
                if(newcontenido.toString().split("\n")[i].contains("Pagar")){
                    List<String> valores = fn.obtenerListNumeros(newcontenido.toString().split("\n")[i]);
                    modelo.setPrimaTotal(fn.castBigDecimal(fn.castDouble(valores.get(0))));
                }
            }
            inicio = contenido.indexOf("Forma###de###Pago");
			fin = contenido.indexOf("CARACTERÍSTICAS###DE###LA###MASCOTA");		
            newcontenido =new StringBuilder();
			newcontenido.append( fn.extracted(inicio, fin, contenido));

            for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
                if(newcontenido.toString().split("\n")[i].contains("Forma###de###Pago") && newcontenido.toString().split("\n")[i].contains("Moneda")){                
                  modelo.setFormaPago(fn.formaPagoSring(newcontenido.toString().split("\n")[i+1]));
                  modelo.setMoneda(fn.buscaMonedaEnTexto(newcontenido.toString().split("\n")[i+1]));
                }             
            }
            
            
            return modelo;
        } catch (Exception ex) {modelo.setError(
            GnpDiversosDModelo.this.getClass().getTypeName() + " | " + ex.getMessage() + " | " + ex.getCause());
            return modelo;
        }
    }
    
}
