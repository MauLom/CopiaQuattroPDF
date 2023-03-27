package com.copsis.models.afirme;

import java.util.ArrayList;
import java.util.List;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraCoberturasModel;
import com.copsis.models.EstructuraJsonModel;
import com.copsis.models.EstructuraRecibosModel;
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
            
            String coberturaex ="";
            for (int i = 0; i < contenido.split("Suma Asegurada").length; i++) {
                if(i> 0) {                 
                    if(contenido.split("Suma Asegurada")[i].contains("Advertencia")) {
                        coberturaex = contenido.split("Suma Asegurada")[i].split("Advertencia")[0].replace("@@@", "");
                    }
                    if(contenido.split("Suma Asegurada")[i].contains("Prima Neta")) {
                        coberturaex += contenido.split("Suma Asegurada")[i].split("Prima Neta")[0].replace("@@@", "");
                    }                  
                }
            }
            
     
            

   
            newcontenido = new StringBuilder();
            newcontenido.append( coberturaex.replace("\r", "").replace("Responsabilidad Civil por", "Responsabilidad Civil por Gastos Médicos y-o Muerte de Trabajadores Domésticos"));
            List<EstructuraCoberturasModel> coberturas = new ArrayList<>();
            if( newcontenido.toString().split("\n").length> 5) {                
                for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {                      
                    EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();
                    if(!newcontenido.toString().split("\n")[i].contains("Suma Asegurada") 
                     && !newcontenido.toString().split("\n")[i].contains("Deducibles") 
                     && !newcontenido.toString().split("\n")[i].contains("DAÑOS MATERIALES AL INMUEBLE") 
                     && !newcontenido.toString().split("\n")[i].contains("DAÑOS MATERIALES A CONTENIDOS") 
                     && !newcontenido.toString().split("\n")[i].contains("ASISTENCIA EN EL HOGAR")
                     && !newcontenido.toString().split("\n")[i].contains("RESPONSABILIDAD CIVIL") 
                 
                   
                     && newcontenido.toString().split("\n")[i].length() > 20) {         
                                     
                        int sp  = newcontenido.toString().split("\n")[i].split("###").length; 
                    
                                  
                       switch (sp) {
                    case 2:
                        cobertura.setNombre(newcontenido.toString().split("\n")[i].split("###")[0]);
                        cobertura.setSa(newcontenido.toString().split("\n")[i].split("###")[1]);
                        coberturas.add(cobertura);
                        break;
             
                    case 3:
                        cobertura.setNombre(newcontenido.toString().split("\n")[i].split("###")[0]);
                        cobertura.setSa(newcontenido.toString().split("\n")[i].split("###")[1]);
                        coberturas.add(cobertura);
                        break;
                    case 4:
                        cobertura.setNombre(newcontenido.toString().split("\n")[i].split("###")[0]);
                        cobertura.setSa(newcontenido.toString().split("\n")[i].split("###")[1]);
                        cobertura.setDeducible(newcontenido.toString().split("\n")[i].split("###")[2]);
                        cobertura.setCoaseguro(newcontenido.toString().split("\n")[i].split("###")[3].replace("\n", ""));
                        coberturas.add(cobertura);
                        break;    
                    default:
                        break;
                    }
                    }
                }
                modelo.setCoberturas(coberturas);
            }
            
            inicio = contenido.indexOf("Prima Neta");
            fin = contenido.indexOf("Artículo 25");
            newcontenido = new StringBuilder();
            newcontenido.append(fn.extracted(inicio, fin, contenido));
              
            modelo.setFormaPago(fn.formaPagoSring(newcontenido.toString()));
            
            for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
                if(newcontenido.toString().split("\n")[i].contains("Cantidad")) {
                    List<String> valores = fn.obtenerListNumeros(newcontenido.toString().split("\n")[i]);
                    modelo.setPrimaneta(fn.castBigDecimal(fn.castDouble(valores.get(0))));
                    modelo.setDerecho(fn.castBigDecimal(fn.castDouble(valores.get(2))));
                    modelo.setRecargo(fn.castBigDecimal(fn.castDouble(valores.get(1))));
                    modelo.setIva(fn.castBigDecimal(fn.castDouble(valores.get(3))));                    
                    modelo.setPrimaTotal(fn.castBigDecimal(fn.castDouble(valores.get(4))));
                }
            }
            
            List<EstructuraRecibosModel> recibos = new ArrayList<>();
            EstructuraRecibosModel recibo = new EstructuraRecibosModel();

            if (modelo.getFormaPago() == 1) {
                recibo.setReciboId("");
                recibo.setSerie("1/1");
                recibo.setVigenciaDe(modelo.getVigenciaDe());
                recibo.setVigenciaA(modelo.getVigenciaA());
                if (recibo.getVigenciaDe().length() > 0) {
                    recibo.setVencimiento(fn.dateAdd(recibo.getVigenciaDe(), 30, 1));
                }
                recibo.setPrimaneta(modelo.getPrimaneta());
                recibo.setDerecho(modelo.getDerecho());
                recibo.setRecargo(modelo.getRecargo());
                recibo.setIva(modelo.getDerecho());

                recibo.setPrimaTotal(modelo.getPrimaTotal());
                recibo.setAjusteUno(modelo.getAjusteUno());
                recibo.setAjusteDos(modelo.getAjusteDos());
                recibo.setCargoExtra(modelo.getCargoExtra());
                recibos.add(recibo);
            }

            modelo.setRecibos(recibos);
            
            return modelo;
        } catch (Exception ex) {
            modelo.setError(AfirmeDiversosDModel.this.getClass().getTypeName() + " - catch:" + ex.getMessage() + " | " + ex.getCause());
            return modelo;
        }
    }
    
}
