package com.copsis.models.mediacces;

import java.util.ArrayList;
import java.util.List;

import com.copsis.constants.ConstantsValue;
import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraAseguradosModel;
import com.copsis.models.EstructuraCoberturasModel;
import com.copsis.models.EstructuraJsonModel;

public class MediaccesSaludModel {
    private DataToolsModel fn = new DataToolsModel();
    private EstructuraJsonModel modelo = new EstructuraJsonModel();

    public EstructuraJsonModel procesar(String contenido) {
        int inicio = 0;
        int fin = 0;
        StringBuilder newcontenido = new StringBuilder();

        contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales())
        .replace("En ###cumplimiento", "En cumplimiento");

        try {

            modelo.setTipo(3);
            modelo.setCia(84);

            inicio = contenido.indexOf("NOMBRE DEL CONTRATANTE:");
            fin = contenido.indexOf("DATOS DE LOS ASEGURADOS");
            newcontenido.append(fn.extracted(inicio, fin, contenido));
            for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {  
                if (newcontenido.toString().split("\n")[i].contains("CONTRATANTE")
                        && newcontenido.toString().split("\n")[i].contains("ACCIDENTES")) {
                    modelo.setCteNombre(newcontenido.toString().split("\n")[i + 1]);
                }
                if (newcontenido.toString().split("\n")[i].contains("RFC")
                        && newcontenido.toString().split("\n")[i].contains("AGENTE:")) {
                    modelo.setRfc(newcontenido.toString().split("\n")[i].split("RFC")[1].split("AGENTE")[0]
                            .replace("###", ""));
                    modelo.setCveAgente(newcontenido.toString().split("\n")[i].split("AGENTE:")[1].replace("###", ""));
                }
                if (newcontenido.toString().split("\n")[i].contains("DOMICILIO")
                        && newcontenido.toString().split("\n")[i].contains("NOMBRE AGENTE:")) {
                    modelo.setAgente(newcontenido.toString().split("\n")[i].split("AGENTE:")[1].replace("###", ""));
                }

                if (newcontenido.toString().split("\n")[i].contains("DOMICILIO")
                        && newcontenido.toString().split("\n")[i + 1].contains(ConstantsValue.POLIZA_MAYUS)) {
                    modelo.setCteDireccion(
                            newcontenido.toString().split("\n")[i + 1].split(ConstantsValue.POLIZA_MAYUS)[0].replace("###", ""));
                }

                if (newcontenido.toString().split("\n")[i].contains(ConstantsValue.POLIZA_MAYUS)
                        && newcontenido.toString().split("\n")[i].contains("MONEDA:")) {
                    modelo.setPoliza(newcontenido.toString().split("\n")[i].split("PÓLIZA:")[1].split("MONEDA")[0]
                            .replace("###", ""));
                    modelo.setMoneda(fn.buscaMonedaEnTexto(newcontenido.toString().split("\n")[i]));
                }
                if (newcontenido.toString().split("\n")[i].contains("CP")) {
                    
                    if(newcontenido.toString().split("\n")[i].split("CP")[1].replace("###", "").trim().substring(0, 5).length() ==5){
                        modelo.setCp(newcontenido.toString().split("\n")[i].split("CP")[1].replace("###", "").trim().substring(0, 5));
                    }
                    
                }
                if (newcontenido.toString().split("\n")[i].contains("FORMA PAGO:")) {
                    modelo.setFormaPago(fn.formaPagoSring(newcontenido.toString().split("\n")[i]));
                }
                if (newcontenido.toString().split("\n")[i].contains("HASTA")
                        && newcontenido.toString().split("\n")[i].contains("DESDE")) {
                    List<String> valores = fn.obtenVigePoliza(newcontenido.toString().split("\n")[i + 1]);
                    if (valores.size() > 2) {
                        modelo.setVigenciaDe(fn.formatDateMonthCadena(valores.get(0)));
                        modelo.setVigenciaA(fn.formatDateMonthCadena(valores.get(1)));
                        modelo.setFechaEmision(fn.formatDateMonthCadena(valores.get(2)));
                    }
                }
            }

         

            inicio = contenido.indexOf("DATOS DE LOS ASEGURADOS");
            fin = contenido.indexOf("COBERTURAS INCLUIDAS");
            newcontenido = new StringBuilder();
            newcontenido.append(fn.extracted(inicio, fin, contenido));
            List<EstructuraAseguradosModel> asegurados = new ArrayList<>();
            for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {               
                EstructuraAseguradosModel asegurado = new EstructuraAseguradosModel();
                if(newcontenido.toString().split("\n")[i].split("-").length >4){
                    List<String> x = fn.obtenVigePoliza(newcontenido.toString().split("\n")[i]);
                   asegurado.setNombre(newcontenido.toString().split("\n")[i].split("###")[1]);
                   asegurado.setNacimiento(fn.formatDateMonthCadena(x.get(0)));
                   asegurado.setAntiguedad(fn.formatDateMonthCadena(x.get(1)));
                   asegurado.setSexo(fn.sexo(newcontenido.toString().split("\n")[i].split("###")[2]) ? 1: 0);
                   asegurados.add(asegurado);
                }
            }
            modelo.setAsegurados(asegurados);

         

            inicio = contenido.indexOf("COBERTURAS INCLUIDAS");
            fin = contenido.indexOf("MEDI ACCESS SEGUROS DE SALUD");
            newcontenido = new StringBuilder();
            newcontenido.append(fn.extracted(inicio, fin, contenido));
            List<EstructuraCoberturasModel> coberturas = new ArrayList<>();
            for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {               
                EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();
              if(!newcontenido.toString().split("\n")[i].contains("SUMA ASEGURADA")){
               
                switch (newcontenido.toString().split("\n")[i].split("###").length){
                    case 3:
                    cobertura.setNombre(newcontenido.toString().split("\n")[i].split("###")[0]);
                    cobertura.setCopago(newcontenido.toString().split("\n")[i].split("###")[1]);
                    cobertura.setSa(newcontenido.toString().split("\n")[i].split("###")[2]);
                    coberturas.add(cobertura);
                    break;                                   
                }                
              }
            }
            modelo.setCoberturas(coberturas);

            inicio = contenido.indexOf("PRIMA BASICA");
            fin = contenido.indexOf("En cumplimiento");
            if(inicio > fin) {
                fin = contenido.indexOf("Le sugerimos consultar las");
            }
       
            newcontenido = new StringBuilder();
            newcontenido.append(fn.extracted(inicio, fin, contenido));
            for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {              

              if(newcontenido.toString().split("\n")[i].contains("PRIMA BASICA")){
                List<String> valores = fn.obtenerListNumeros(newcontenido.toString().split("\n")[i]);
                modelo.setPrimaneta(fn.castBigDecimal(fn.castDouble(valores.get(0))));
               
                
                                   
                
              }
              if(newcontenido.toString().split("\n")[i].contains("RECARGOS")){
                List<String> valores = fn.obtenerListNumeros(newcontenido.toString().split("\n")[i]);
                modelo.setRecargo(fn.castBigDecimal(fn.castDouble(valores.get(0))));
              }
              if(newcontenido.toString().split("\n")[i].contains("GASTOS DE EXPEDICION")){
                List<String> valores = fn.obtenerListNumeros(newcontenido.toString().split("\n")[i]);
                modelo.setDerecho(fn.castBigDecimal(fn.castDouble(valores.get(0))));
              }
              if(newcontenido.toString().split("\n")[i].contains("I.V.A.")){
                List<String> valores = fn.obtenerListNumeros(newcontenido.toString().split("\n")[i]);
                modelo.setIva(fn.castBigDecimal(fn.castDouble(valores.get(0))));  
              }
              if(newcontenido.toString().split("\n")[i].contains("PRIMA TOTAL ANUAL")){
                List<String> valores = fn.obtenerListNumeros(newcontenido.toString().split("\n")[i]);
                modelo.setPrimaTotal(fn.castBigDecimal(fn.castDouble(valores.get(0))));
              }
            }

            


            return modelo;
        } catch (Exception ex) {            
            modelo.setError(MediaccesSaludModel.this.getClass().getTypeName() + " | " + ex.getMessage() + " | "
                    + ex.getCause());
            return modelo;
        }
    }

}
