package com.copsis.models.general;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.copsis.constants.ConstantsValue;
import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraCoberturasModel;
import com.copsis.models.EstructuraJsonModel;

import lombok.val;

public class GneralDiversosBModel {
    private DataToolsModel fn = new DataToolsModel();
    private EstructuraJsonModel modelo = new EstructuraJsonModel();
    public EstructuraJsonModel procesar(String contenido) {
        int inicio = 0;		    
        int fin = 0;
        StringBuilder newcontenido = new StringBuilder();        
        contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());
        try {
     
            inicio = contenido.indexOf("Oficina Matriz");
            fin = contenido.indexOf("PRIMER RECIBO");	
            newcontenido.append( fn.extracted(inicio, fin, contenido));
   
            for (int i = 0; i < newcontenido.toString().split("\n").length; i++) { 
               
            if( newcontenido.toString().split("\n")[i].contains("Póliza")){
               modelo.setPoliza( newcontenido.toString().split("\n")[i].split("Póliza")[1].replace("###", ""));
            }                
             if( newcontenido.toString().split("\n")[i].contains("Fecha de emisión:")){
                List<String> valores = fn.obtenVigePoliza(newcontenido.toString().split("\n")[i]);
                modelo.setFechaEmision(fn.formatDateMonthCadena( valores.get(0)));
             }
             if( newcontenido.toString().split("\n")[i].contains("DESDE LAS ")){
                List<String> valores = fn.obtenVigePoliza(newcontenido.toString().split("\n")[i]);
                modelo.setVigenciaDe(fn.formatDateMonthCadena( valores.get(0)));
             }
             if( newcontenido.toString().split("\n")[i].contains("HASTA LAS")){
                List<String> valores = fn.obtenVigePoliza(newcontenido.toString().split("\n")[i]);
                modelo.setVigenciaA(fn.formatDateMonthCadena( valores.get(0)));
             }
             if( newcontenido.toString().split("\n")[i].contains("R.F.C:")){
                modelo.setRfc(newcontenido.toString().split("\n")[i].split("R.F.C:")[1].replace("###", "").trim());
             }
             if( newcontenido.toString().split("\n")[i].contains("NOMBRE:")){
                modelo.setCteNombre(newcontenido.toString().split("\n")[i].split("NOMBRE:")[1].replace("###", "").trim());
             }
             if( newcontenido.toString().split("\n")[i].contains("DIRECCIÓN:")){
                modelo.setCteDireccion(newcontenido.toString().split("\n")[i].split("DIRECCIÓN:")[1].replace("###", "").trim());
                  List<String> valores = fn.obtenerListNumeros2(modelo.getCteDireccion().split("C.P:")[1]);
                if(!valores.isEmpty()){
                    modelo.setCp(valores.stream()
                        .filter(numero -> String.valueOf(numero).length() >= 4)
                        .collect(Collectors.toList()).get(0));
                }
             }
             if( newcontenido.toString().split("\n")[i].contains("MONEDA") 
             &&  newcontenido.toString().split("\n")[i].contains("FORMA DE PAGO")){
                modelo.setFormaPago(fn.formaPagoSring( newcontenido.toString().split("\n")[i+1]));
                modelo.setMoneda(fn.buscaMonedaEnTexto( newcontenido.toString().split("\n")[i+1]));
             }
             if( newcontenido.toString().split("\n")[i].contains("PRIMA NETA") 
             &&  newcontenido.toString().split("\n")[i].contains("I.V.A")){
                List<String> valores = fn.obtenerListNumeros2(newcontenido.toString().split("\n")[i+1].replace(",", ""));
                if(!valores.isEmpty()){
                    modelo.setPrimaneta(fn.castBigDecimal(fn.castDouble(valores.get(1))));
                    modelo.setIva(fn.castBigDecimal(fn.castDouble(valores.get(3))));
                }	
             }
            }

            inicio = contenido.indexOf("FORMA DE PAGO");
            fin = contenido.indexOf("GENERAL DE SEGUROS S.A. ");	
            newcontenido =new StringBuilder();
            newcontenido.append( fn.extracted(inicio, fin, contenido));
            for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
              
                if( newcontenido.toString().split("\n")[i].contains("RECARGO") 
             &&  newcontenido.toString().split("\n")[i].contains("TOTAL A PAGAR")){
                List<String> valores = fn.obtenerListNumeros2(newcontenido.toString().split("\n")[i+1].replace(",", ""));
                if(!valores.isEmpty()){
                    modelo.setPrimerPrimatotal(fn.castBigDecimal(fn.castDouble(valores.get(0))));
                    modelo.setDerecho(fn.castBigDecimal(fn.castDouble(valores.get(3))));
                    modelo.setPrimaTotal(fn.castBigDecimal(fn.castDouble(valores.get(4))));
                }	
               }     

             }
          

             inicio = contenido.indexOf("SUMA ASEGURADA");
             fin = contenido.indexOf("BIENES ASEGURADOS");	
             newcontenido =new StringBuilder();
             newcontenido.append( fn.extracted(inicio, fin, contenido));
              List<EstructuraCoberturasModel> coberturas = new ArrayList<>();
             for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
                EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();
                if(!newcontenido.toString().split("\n")[i].contains("SUMA ASEGURADA") && (newcontenido.toString().split("\n")[i].split("###").length == 2)){
                        cobertura.setNombre(newcontenido.toString().split("\n")[i].split("###")[0]);
                        cobertura.setSa(newcontenido.toString().split("\n")[i].split("###")[1]);
                        coberturas.add(cobertura);
                    
                }            
             }
             modelo.setCoberturas(coberturas);
            
            return modelo;
        } catch (Exception ex) {
           modelo.setError(GneralDiversosBModel.this.getClass().getTypeName() + " - catch:" + ex.getMessage() + " | "+ ex.getCause());
			return modelo;
        }
    }
}
