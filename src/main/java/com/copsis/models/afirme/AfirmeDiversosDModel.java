package com.copsis.models.afirme;

import java.util.ArrayList;
import java.util.List;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraCoberturasModel;
import com.copsis.models.EstructuraJsonModel;
import com.copsis.models.EstructuraUbicacionesModel;

public class AfirmeDiversosDModel {

    private DataToolsModel fn = new DataToolsModel();
    private EstructuraJsonModel modelo = new EstructuraJsonModel();
    
    public EstructuraJsonModel procesar(String contenido) {        
        StringBuilder newcontenido = new StringBuilder();
        contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());
        int inicio = 0;
        int fin = 0;
        try {
            modelo.setTipo(7);            
            modelo.setCia(31);
         
            inicio = contenido.indexOf("Póliza");
            fin =contenido.indexOf("Ubicación:");  
            newcontenido.append(fn.extracted(inicio, fin, contenido));
            
            for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
               
                if(newcontenido.toString().split("\n")[i].contains("Póliza:")) {
                    modelo.setPoliza(newcontenido.toString().split("\n")[i].split("Póliza:")[1].replace("###", "").trim());
                }
                if(newcontenido.toString().split("\n")[i].contains("Moneda:")) {
                    modelo.setMoneda(fn.buscaMonedaEnTexto(newcontenido.toString().split("\n")[i]));
                }
                
                if(newcontenido.toString().split("\n")[i].contains("Vigencia") && newcontenido.toString().split("\n")[i].contains("Desde")
                      &&  newcontenido.toString().split("\n")[i].contains("Hasta")) {
                    modelo.setVigenciaDe(fn.formatDateMonthCadena(fn.obtenVigePoliza(newcontenido.toString().split("\n")[i+1]).get(0)));
                    modelo.setVigenciaA(fn.formatDateMonthCadena(fn.obtenVigePoliza(newcontenido.toString().split("\n")[i+1]).get(1)));      
                    modelo.setFechaEmision(modelo.getVigenciaDe());
                }
                
                if(newcontenido.toString().split("\n")[i].contains(" domicilio en:") && newcontenido.toString().split("\n")[i].contains("C.P.")) {
                    modelo.setCteDireccion(newcontenido.toString().split("\n")[i].split("domicilio en:")[1].trim());
                    modelo.setCp(newcontenido.toString().split("\n")[i].split("C.P.")[1].trim().replace("###", "").substring(0, 5));
                }
            }
            
            inicio = contenido.indexOf("Ubicación");
            fin =contenido.indexOf("Suma Asegurada");
         
            newcontenido = new StringBuilder();
            newcontenido.append(fn.extracted(inicio, fin, contenido));
            
            
            List<EstructuraUbicacionesModel> ubicaciones = new ArrayList<>();
            EstructuraUbicacionesModel ubicacion = new EstructuraUbicacionesModel();

    
            if(newcontenido.toString().length()> 0) {
            for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
                
                    if(newcontenido.toString().split("\n")[i].contains("Ubicación")) {
                        ubicacion.setNombre(modelo.getCteNombre());
                        ubicacion.setCalle(newcontenido.toString().split("\n")[i].split("Ubicación")[1].replace(":", "").replace("###", "").trim().substring(0,30));    
                    }
                   if(newcontenido.toString().split("\n")[i].contains("Giro Asegurado")) {
                        ubicacion.setGiro(newcontenido.toString().split("\n")[i].split("Giro Asegurado")[1].replace(":", "").replace("###", "").trim());
                    }
                   if(newcontenido.toString().split("\n")[i].contains("Giro o Actividad:")) {
                    ubicacion.setGiro(newcontenido.toString().split("\n")[i].split("Giro o Actividad:")[1].replace(":", "").replace("###", "").trim());
                }
                   if(newcontenido.toString().split("\n")[i].contains("Número de pisos:")) {
                    ubicacion.setNiveles(fn.castInteger(fn.obtenerListNumeros2(newcontenido.toString().split("\n")[i].split("Número de pisos:")[1].replace(":", "").replace("###", "").trim()).get(0)));;
                }
                }
            ubicaciones.add(ubicacion);
            }
            
            modelo.setUbicaciones(ubicaciones);
            
            String cobertura ="";
            for (int i = 0; i < contenido.split("Suma Asegurada").length; i++) {
                if(i> 0) {
//                    System.out.println(contenido.split("Suma Asegurada")[i] +"\n----------------");
                    if(contenido.split("Suma Asegurada")[i].contains("Advertencia")) {
                        cobertura = contenido.split("Suma Asegurada")[i].split("Advertencia")[0];
                    }
                    if(contenido.split("Suma Asegurada")[i].contains("Prima Neta")) {
                        cobertura += contenido.split("Suma Asegurada")[i].split("Prima Neta")[0];
                    }
                  
                }
            }
            
            System.out.println(cobertura);
            
//            
//            inicio = contenido.indexOf("Suma Asegurada");
//            fin =contenido.indexOf("Advertencia");
//            System.out.println( inicio +"---> "+ fin);
//            newcontenido = new StringBuilder();
//            newcontenido.append(fn.extracted(inicio, fin, contenido));
//            List<EstructuraCoberturasModel> coberturas = new ArrayList<>();
//            if( newcontenido.toString().split("\n").length> 5) {                
//                for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {  
//                    
//                    EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();
//                    if(!newcontenido.toString().split("\n")[i].contains("Suma Asegurada")) {            
//                        int sp  = newcontenido.toString().split("\n")[i].split("###").length;
//                       switch (sp) {
//                    case 1:
//                        cobertura.setNombre(newcontenido.toString().split("\n")[i].split("###")[0]);
//                        coberturas.add(cobertura);
//                        break;
//                    case 3:
//                        cobertura.setNombre(newcontenido.toString().split("\n")[i].split("###")[0]);
//                        cobertura.setSa(newcontenido.toString().split("\n")[i].split("###")[1]);
//                        coberturas.add(cobertura);
//                        break;
//                    default:
//                        break;
//                    }
//                    }
//                }
//                modelo.setCoberturas(coberturas);
//            }
            
//            System.out.println(contenido);
            
            return modelo;
        } catch (Exception ex) {
            modelo.setError(AfirmeDiversosDModel.this.getClass().getTypeName() + " - catch:" + ex.getMessage() + " | " + ex.getCause());
            return modelo;
        }
    }
    
}
