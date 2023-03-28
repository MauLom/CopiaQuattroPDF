package com.copsis.models.axa.salud;

import java.util.ArrayList;
import java.util.List;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraAseguradosModel;
import com.copsis.models.EstructuraCoberturasModel;
import com.copsis.models.EstructuraJsonModel;

public class AxaSaludFaModel {
    private DataToolsModel fn = new DataToolsModel();
    private EstructuraJsonModel modelo = new EstructuraJsonModel();

    public EstructuraJsonModel procesar(String contenido) {
        int inicio = 0;
        int fin = 0;
        StringBuilder newcontenido = new StringBuilder();

        contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales())
        .replaceAll("(\\t)", " ");
      
        try {
            modelo.setTipo(3);
            modelo.setCia(20);

            inicio = contenido.indexOf("Póliza");
            fin = contenido.indexOf("Coberturas");
          

            newcontenido.append(fn.extracted(inicio, fin, contenido));
      
            for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
                
                
                if(newcontenido.toString().split("\n")[i].contains("Póliza:")){
                 modelo.setPoliza(newcontenido.toString().split("\n")[i].split("Póliza:")[1].trim());
                }
                if(newcontenido.toString().split("\n")[i].contains("razón") && newcontenido.toString().split("\n")[i].contains("contratante")){
                 modelo.setCteNombre(newcontenido.toString().split("\n")[i].split("razón")[1].replace("###", ""));
                }
                if(newcontenido.toString().split("\n")[i].contains("RFC:")){
                    modelo.setRfc(newcontenido.toString().split("\n")[i].split("RFC:")[1].replace("###", "").trim());
                }
                if(newcontenido.toString().split("\n")[i].contains("Domicilio") && newcontenido.toString().split("\n")[i].contains("contratante")){
                 modelo.setCteDireccion(newcontenido.toString().split("\n")[i].split("contratante:")[1].replace("###", "").trim());
                }
                if(newcontenido.toString().split("\n")[i].contains("C.P:")){
                    modelo.setCp(newcontenido.toString().split("\n")[i].split("C.P:")[1].replace("###", "").trim().substring(0,5));
                }

                if(newcontenido.toString().split("\n")[i].contains("Producto") && newcontenido.toString().split("\n")[i].contains("Moneda")){
                    modelo.setPlan(newcontenido.toString().split("\n")[i].split("Producto:")[1].split("Moneda")[0].replace("###", "").trim());
                    modelo.setMoneda(fn.buscaMonedaEnTexto(newcontenido.toString().split("\n")[i]));
                }
                if(newcontenido.toString().split("\n")[i].contains("Clave de agente:") && newcontenido.toString().split("\n")[i].contains("Forma de pago:")){
                    modelo.setCveAgente(newcontenido.toString().split("\n")[i].split("agente:")[1].split("Forma")[0].replace("###", "").trim());
                    modelo.setFormaPago(fn.formaPagoSring(newcontenido.toString().split("\n")[i]));
                }
                if(newcontenido.toString().split("\n")[i].contains("Clave de agente:")){

                }

                if(newcontenido.toString().split("\n")[i].contains("Vigencia") && newcontenido.toString().split("\n")[i].contains("hasta")){ 
                   String x = newcontenido.toString().split("\n")[i].split("del")[1].replace("###", "").replace(" ", "")
                   .replace("hasta", "").trim();
                   String y = newcontenido.toString().split("\n")[i+1].split("del")[1].replace("###", "").replace(" ", "")
                   .replace("hasta", "").trim();
                
                   modelo.setVigenciaDe(fn.formatDateMonthCadena(fn.obtenVigePoliza(x).get(0)));
                   modelo.setVigenciaA(fn.formatDateMonthCadena(fn.obtenVigePoliza(y).get(0)));
                   modelo.setFechaEmision(modelo.getVigenciaDe());

                }
                if(newcontenido.toString().split("\n")[i].contains("Prima inicial:") ){
                    List<String> valores = fn.obtenerListNumeros(newcontenido.toString().split("\n")[i]);
                    modelo.setPrimerPrimatotal(fn.castBigDecimal(fn.castDouble(valores.get(0))));
                }
                if(newcontenido.toString().split("\n")[i].contains("Descuento:") ){
                    List<String> valores = fn.obtenerListNumeros(newcontenido.toString().split("\n")[i]);
                    modelo.setCargoExtra(fn.castBigDecimal(fn.castDouble(valores.get(0))));
                }
                if(newcontenido.toString().split("\n")[i].contains("Prima neta:") ){
                    List<String> valores = fn.obtenerListNumeros(newcontenido.toString().split("\n")[i]);
                    modelo.setPrimaneta(fn.castBigDecimal(fn.castDouble(valores.get(0))));
                }
                if(newcontenido.toString().split("\n")[i].contains("Derecho") ){
                    List<String> valores = fn.obtenerListNumeros(newcontenido.toString().split("\n")[i]);
                    modelo.setDerecho(fn.castBigDecimal(fn.castDouble(valores.get(0))));
                }
                
                if(newcontenido.toString().split("\n")[i].contains("I.V.A:") ){
                    List<String> valores = fn.obtenerListNumeros(newcontenido.toString().split("\n")[i]);
                    modelo.setIva(fn.castBigDecimal(fn.castDouble(valores.get(0))));
                }
                
                if(newcontenido.toString().split("\n")[i].contains("Total:") ){
                    List<String> valores = fn.obtenerListNumeros(newcontenido.toString().split("\n")[i]);
                    modelo.setPrimaTotal(fn.castBigDecimal(fn.castDouble(valores.get(0))));
                }
  
            }

            inicio = contenido.indexOf("Suma contratada");
            fin = contenido.indexOf("Datos de familia asegurada");         
            newcontenido = new StringBuilder();
            newcontenido.append(fn.extracted(inicio, fin, contenido).replace("especialidad","Consultas de atención de especialidad")
            .replace("referencia médica", "Terapias físicas###10 sesiones por cada referencia médica")
            .replace("Nutrición", "Consultas de Apoyo Psicología y Nutrición"));
            
            List<EstructuraCoberturasModel> coberturas = new ArrayList<>();
            for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
                EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();                               
                if(!newcontenido.toString().split("\n")[i].contains("Beneficio Incluido")){
                    
                    switch (newcontenido.toString().split("\n")[i].split("###").length) {
                        case 3:                        
                        cobertura.setNombre(newcontenido.toString().split("\n")[i].split("###")[0]);
                        cobertura.setSa(newcontenido.toString().split("\n")[i].split("###")[1]);
                        cobertura.setCopago(newcontenido.toString().split("\n")[i].split("###")[2]);        
                        coberturas.add(cobertura);               
                         break;
                                           
                
                    }
                    modelo.setCoberturas(coberturas);
                   
                }
            }

            List<EstructuraAseguradosModel> asegurados = new ArrayList<>();
            EstructuraAseguradosModel asegurado = new EstructuraAseguradosModel();
            inicio = contenido.indexOf("Datos de familia asegurada");
            String xcon="";
            if(inicio > -1){
                xcon = contenido.split("Datos de familia asegurada")[1];
            }

            if(xcon.length() > -1){
                 xcon = xcon.split("AXA Salud")[0];
            }
            newcontenido = new StringBuilder();
            newcontenido.append(xcon.replace(" - ", "-"));

            for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {           
                if(newcontenido.toString().split("\n")[i].split("-").length > 2){                  
                    asegurado.setNombre(newcontenido.toString().split("\n")[i].split("###")[0].replace("@@@", ""));
                    asegurado.setParentesco(fn.buscaParentesco(newcontenido.toString().split("\n")[i]));
                    asegurado.setSexo(fn.sexo(newcontenido.toString().split("\n")[i].split("###")[5]) ? 1:0);
                    asegurado.setNacimiento(fn.formatDateMonthCadena(fn.obtenVigePoliza(newcontenido.toString().split("\n")[i]).get(0)));
                    asegurado.setAntiguedad(fn.formatDateMonthCadena(fn.obtenVigePoliza(newcontenido.toString().split("\n")[i]).get(1)));
                    asegurados.add(asegurado);
                }
            }
            modelo.setAsegurados(asegurados);
           

            return modelo;
        } catch (Exception e) {
         
            modelo.setError(AxaSaludFaModel.this.getClass().getTypeName() + " - catch:" + e.getMessage() + " | "
                    + e.getCause());
            return modelo;
        }

    }

}
