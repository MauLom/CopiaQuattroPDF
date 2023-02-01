package com.copsis.models.general;

import java.util.ArrayList;
import java.util.List;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraCoberturasModel;
import com.copsis.models.EstructuraJsonModel;
import com.copsis.models.EstructuraUbicacionesModel;

public class GeneralDiversosAModel {
    private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
    
    public EstructuraJsonModel procesar(String contenido) {
		int inicio = 0;
		int fin = 0;
		StringBuilder newcontenido = new StringBuilder();
		StringBuilder newdireccion = new StringBuilder();
		contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());
        try {
            modelo.setTipo(7);
            modelo.setCia(16);

            inicio = contenido.indexOf("POLIZA DEL SEGURO DEL RAMO");
            fin = contenido.indexOf("COBERTURAS CONTRATADAS");

            newcontenido.append( fn.extracted(inicio, fin, contenido));
            for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
         
                if(newcontenido.toString().split("\n")[i].contains("POLIZA")){
                    
                    if(newcontenido.toString().split("\n")[i+2].split("###").length == 5){
                     modelo.setCteDireccion((newcontenido.toString().split("\n")[i+1]  +" " +newcontenido.toString().split("\n")[i+2].split("###")[0]).replace("###", " "));
                     modelo.setPoliza(newcontenido.toString().split("\n")[i+2].split("###")[newcontenido.toString().split("\n")[i+2].split("###").length -1]);
                    }
                }
                if(newcontenido.toString().split("\n")[i].contains("RFC:")){
                   modelo.setRfc(newcontenido.toString().split("\n")[i].split("RFC:")[1].replace("###", "").trim());
                   modelo.setCteNombre(newcontenido.toString().split("\n")[i].split("RFC:")[0].replace("###",""));
                }
                if(newcontenido.toString().split("\n")[i].contains("C.P:")){
                    modelo.setCp(newcontenido.toString().split("\n")[i].split("C.P:")[1].trim().substring(0,5));
                }
                if(newcontenido.toString().split("\n")[i].contains("DESDE LAS") && newcontenido.toString().split("\n")[i].contains("HASTA LAS")){
          
                 if(fn.obtenVigePoliza(newcontenido.toString().split("\n")[i+1]).size() > 0 && fn.obtenVigePoliza(newcontenido.toString().split("\n")[i+1]).size() ==3){
                  modelo.setVigenciaDe(fn.formatDateMonthCadena(fn.obtenVigePoliza(newcontenido.toString().split("\n")[i+1]).get(0)));
                  modelo.setVigenciaA(fn.formatDateMonthCadena(fn.obtenVigePoliza(newcontenido.toString().split("\n")[i+1]).get(1)));
                  modelo.setFechaEmision(fn.formatDateMonthCadena(fn.obtenVigePoliza(newcontenido.toString().split("\n")[i+1]).get(1)));
                modelo.setMoneda(1);
                }
               
                  modelo.setFormaPago(fn.formaPagoSring(newcontenido.toString().split("\n")[i+1]));
                }

                if(newcontenido.toString().split("\n")[i].contains("PRIMA NETA") && newcontenido.toString().split("\n")[i].contains("RECARGO")
                && newcontenido.toString().split("\n")[i+1].contains("I.V.A.")){
                    List<String> valores = fn.obtenerListNumeros(newcontenido.toString().split("\n")[i+2]);                  
                    modelo.setPrimaneta(fn.castBigDecimal(fn.castDouble(valores.get(0))));
                    modelo.setDerecho(fn.castBigDecimal(fn.castDouble(valores.get(3))));
                    modelo.setDerecho(fn.castBigDecimal(fn.castDouble(valores.get(4))));
                    modelo.setIva(fn.castBigDecimal(fn.castDouble(valores.get(5))));
                    modelo.setPrimaTotal(fn.castBigDecimal(fn.castDouble(valores.get(6))));
                }
            }


            inicio = contenido.indexOf("COBERTURAS CONTRATADAS");
            fin = contenido.indexOf("BIENES ASEGURADOS");

            newcontenido = new StringBuilder();
            newcontenido.append(fn.extracted(inicio, fin, contenido));
            List<EstructuraCoberturasModel> coberturas = new ArrayList<>();
            for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
                EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();

                int sp  = newcontenido.toString().split("\n")[i].split("###").length;
             
                if(!newcontenido.toString().split("\n")[i].contains("COBERTURAS CONTRATADAS")              
                && !newcontenido.toString().split("\n")[i].contains("DEDUCIBLE")){
                 
                switch (sp) {
                    case 2:
                        cobertura.setNombre(newcontenido.toString().split("\n")[i].split("###")[0]);
                        cobertura.setSa(newcontenido.toString().split("\n")[i].split("###")[1]);
                        coberturas.add(cobertura);                
                    break;
                    case 3:
                        cobertura.setNombre(newcontenido.toString().split("\n")[i].split("###")[1]);
                        cobertura.setSa(newcontenido.toString().split("\n")[i].split("###")[2]);                    
                        coberturas.add(cobertura);
                    break;
                    case 5:
                    case 6:
                        cobertura.setNombre(newcontenido.toString().split("\n")[i].split("###")[0]);
                        cobertura.setSa(newcontenido.toString().split("\n")[i].split("###")[2]);
                        cobertura.setDeducible(newcontenido.toString().split("\n")[i].split("###")[3]);
                        cobertura.setCoaseguro(newcontenido.toString().split("\n")[i].split("###")[4].replace("\n", ""));
                        coberturas.add(cobertura);
                    break; 

                }
            }
            }
            modelo.setCoberturas(coberturas);

            inicio = contenido.indexOf("BIENES ASEGURADOS");
            fin =contenido.indexOf("OTRAS CONDICIONES");
         
            newcontenido = new StringBuilder();
            newcontenido.append(fn.extracted(inicio, fin, contenido));

            List<EstructuraUbicacionesModel> ubicaciones = new ArrayList<>();
            EstructuraUbicacionesModel ubicacion = new EstructuraUbicacionesModel();
            if(newcontenido.toString().length()> 0) {
                for (int i = 0; i < newcontenido.toString().split("\n").length; i++) { 
                    if(newcontenido.toString().split("\n")[i].contains("GIRO")) {
                        ubicacion.setGiro(newcontenido.toString().split("\n")[i].split("GIRO")[1].replace(":", "").replace("###", "").trim());
                    }
                    if(newcontenido.toString().split("\n")[i].contains("MUROS") && newcontenido.toString().split("\n")[i].contains("TECHOS")) {
                        ubicacion.setMuros(fn.material(newcontenido.toString().split("\n")[i].split("MUROS")[1].split("TECHOS")[0]));
                        ubicacion.setTechos(fn.material(newcontenido.toString().split("\n")[i].split("TECHOS")[1]));
                    }
                }
                ubicaciones.add(ubicacion);
            
            }
            modelo.setUbicaciones(ubicaciones);               


            return modelo;
        } catch (Exception ex) {
         
            modelo.setError(GeneralDiversosAModel.this.getClass().getTypeName() + " - catch:" + ex.getMessage() + " | "+ ex.getCause());
            return modelo;
        }
    }
}
