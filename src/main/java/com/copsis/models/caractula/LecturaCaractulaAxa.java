package com.copsis.models.caractula;

import java.util.ArrayList;
import java.util.List;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraAseguradosModel;
import com.copsis.models.EstructuraCoberturasModel;
import com.copsis.models.EstructuraJsonModel;

public class LecturaCaractulaAxa {
    private DataToolsModel fn = new DataToolsModel();
    EstructuraJsonModel modelo = new EstructuraJsonModel();

    public EstructuraJsonModel procesar(String contenido) {
        contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());
       


        int inicio = 0;
        int fin = 0;
        StringBuilder newcontenido = new StringBuilder();

        try {

            inicio = contenido.indexOf("Póliza:");
            fin = contenido.indexOf("Datos del asegurado");
            newcontenido.append( fn.extracted(inicio, fin, contenido));

            for(int i=0;i< newcontenido.toString().split("\n").length ;i++){
              
                if(newcontenido.toString().split("\n")[i].contains("Póliza:") ){
                 modelo.setPoliza(newcontenido.toString().split("\n")[i].split("Póliza:")[1].replace("###", "").trim());
                }
                if(newcontenido.toString().split("\n")[i].contains("contratante") && newcontenido.toString().split("\n")[i].contains("razón") ){
                  modelo.setCteNombre(newcontenido.toString().split("\n")[i].split("razón")[1].replace("###", "").trim());
                }
                if(newcontenido.toString().split("\n")[i].contains("RFC:") ){
                     modelo.setRfc(newcontenido.toString().split("\n")[i].split("RFC:")[1].replace("###", "").trim());
                }
                if(newcontenido.toString().split("\n")[i].contains("Domicilio") && newcontenido.toString().split("\n")[i].contains("contratante:") ){
                    modelo.setCteDireccion(newcontenido.toString().split("\n")[i].split("contratante:")[1].replace("###", "").trim());
                    if(!modelo.getCteDireccion().isEmpty())	{
                      List<String> valores = fn.obtenerListNumeros2(modelo.getCteDireccion());
                      if(!valores.isEmpty()){
                         if(valores.get(0).length() == 4 || valores.get(0).length() ==  5){
                            modelo.setCp(valores.get(0));
                         }
                      }
                    }
                    
                }
                

            }

            
            inicio = contenido.indexOf("Datos de la póliza ");
            fin = contenido.indexOf("Prima inicial:");
            newcontenido = new StringBuilder();
            newcontenido.append( fn.extracted(inicio, fin, contenido));
            for(int i=0;i< newcontenido.toString().split("\n").length ;i++){               
                 if(newcontenido.toString().split("\n")[i].contains("Producto:") && newcontenido.toString().split("\n")[i].contains("Moneda:")){
                    modelo.setPlan(newcontenido.toString().split("\n")[i].split("Producto:")[1].split("Moneda")[0].replace("###", "").trim());
                    if(newcontenido.toString().split("\n")[i].split("Moneda")[1].contains("MXN")){
                        modelo.setMoneda(1);
                    }
                 } 
                 
                 if(newcontenido.toString().split("\n")[i].contains("agente:") && newcontenido.toString().split("\n")[i].contains("Forma de pago:")){
                  modelo.setCveAgente(newcontenido.toString().split("\n")[i].split("agente:")[1].split("Forma de")[0].replace("###", "").trim());
                  modelo.setFormaPago(fn.formaPagoSring(newcontenido.toString().split("\n")[i]));
                 } 
                 if(newcontenido.toString().split("\n")[i].contains("Fecha de emisión:") ){
                   String fechaEmisi = newcontenido.toString().split("\n")[i].split("emisión:")[1].replace(" ","").replace("###", "").trim();
                   if(!fechaEmisi.isEmpty()){
                   List<String> valores = fn.obtenVigePoliza(fechaEmisi);
                    if(!valores.isEmpty()){
                        modelo.setFechaEmision(fn.formatDateMonthCadena(valores.get(0)));
                    }                  
                   }            
                 }

                   if(newcontenido.toString().split("\n")[i].contains("Nombre de") && newcontenido.toString().split("\n")[i+1].contains("agente:") ){
                       modelo.setAgente(newcontenido.toString().split("\n")[i+1].split("agente:")[1].replace("###", "").trim());
                   }


                 if(newcontenido.toString().split("\n")[i].contains("Vigencia de") && newcontenido.toString().split("\n")[i+1].contains("la póliza:")){
                   String vigencias = newcontenido.toString().split("\n")[i+1].split("póliza:")[1]
                   .replace(" ","").replace("###", "").replace("al", " ").trim();                   
                    List<String> valores = fn.obtenVigePoliza(vigencias);    
                    if(!valores.isEmpty()){
                      modelo.setVigenciaDe(fn.formatDateMonthCadena(valores.get(0)));
                      modelo.setVigenciaA(fn.formatDateMonthCadena(valores.get(1)));  
                    }                                       
                 }
            }

            inicio = contenido.indexOf("Prima inicial:");
            fin = contenido.indexOf("AXA Salud, S.A.");
            newcontenido = new StringBuilder();
            newcontenido.append( fn.extracted(inicio, fin, contenido));
            for(int i=0;i< newcontenido.toString().split("\n").length ;i++){                              
                if(newcontenido.toString().split("\n")[i].contains("Prima neta:")){
                    List<String> valores = fn.obtenerListNumeros2(newcontenido.toString().split("\n")[i].replace(",", ""));
                    if(!valores.isEmpty()){
                        modelo.setPrimaneta(fn.castBigDecimal(fn.castDouble(valores.get(0))));
                    }	
                }
                if(newcontenido.toString().split("\n")[i].contains("Derecho de póliza:")){
                    List<String> valores = fn.obtenerListNumeros2(newcontenido.toString().split("\n")[i].replace(",", ""));
                    if(!valores.isEmpty()){
                        modelo.setDerecho(fn.castBigDecimal(fn.castDouble(valores.get(0))));
                    }	
                }
                if(newcontenido.toString().split("\n")[i].contains("I.V.A:")){
                    List<String> valores = fn.obtenerListNumeros2(newcontenido.toString().split("\n")[i].replace(",", ""));
                    if(!valores.isEmpty()){
                        modelo.setIva(fn.castBigDecimal(fn.castDouble(valores.get(0))));
                    }
                }
                if(newcontenido.toString().split("\n")[i].contains("Total:")){
                    List<String> valores = fn.obtenerListNumeros2(newcontenido.toString().split("\n")[i].replace(",", ""));
                    if(!valores.isEmpty()){
                        modelo.setPrimaTotal(fn.castBigDecimal(fn.castDouble(valores.get(0))));
                    }
                }

            }

           
            inicio = contenido.indexOf("Coberturas");
            fin = contenido.indexOf("Características especiales");
            newcontenido = new StringBuilder();
            newcontenido.append( fn.extracted(inicio, fin, contenido)
            .replace("Terapias ###físicas, ###inhaloterapia ###y", "Terapias físicas, inhaloterapia y oxigenoterapia")
            .replace("10 ###sesiones ###por ###cada", "10 sesiones por cada referencia médica###No aplica")
            .replace("Centro de Atención Médica", "Estudios de laboratorio en el Centro de Atención Médica")
            );
           
            List<EstructuraCoberturasModel> coberturas = new ArrayList<>();
            for(int i=0;i< newcontenido.toString().split("\n").length ;i++){ 
                EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();
          
             if(!newcontenido.toString().split("\n")[i].contains("Suma contratada")){
                if( newcontenido.toString().split("\n")[i].split("###").length == 3){
                     System.out.println(newcontenido.toString().split("\n")[i]);
                cobertura.setNombre(newcontenido.toString().split("\n")[i].split("###")[0]);
                cobertura.setSa(newcontenido.toString().split("\n")[i].split("###")[1].trim());
                cobertura.setCopago(newcontenido.toString().split("\n")[i].split("###")[2].trim());
                coberturas.add(cobertura);

                }
             }
            }
            modelo.setCoberturas(coberturas);


           
            inicio = contenido.indexOf("Datos de familia asegurada");
            fin = contenido.indexOf("Movimientos de disminución");
            newcontenido = new StringBuilder();
            newcontenido.append( fn.extracted(inicio, fin, contenido).replace("Hijoa", "Hijo"));
              List<EstructuraAseguradosModel> asegurados = new ArrayList<>();
            for(int i=0;i< newcontenido.toString().split("\n").length ;i++){ 
                EstructuraAseguradosModel asegurado  = new EstructuraAseguradosModel();  
                if(newcontenido.toString().split("\n")[i].split("-").length > 3){
                    asegurado.setNombre(newcontenido.toString().split("\n")[i].split("###")[0]);
                     List<String> valores = fn.obtenVigePoliza(newcontenido.toString().split("\n")[i].split("###")[1].replace(" ", "").trim());
                     asegurado.setNacimiento(fn.formatDateMonthCadena(valores.get(0)));                    
                     asegurado.setParentesco(fn.parentesco(newcontenido.toString().split("\n")[i].split("###")[2]));
                     asegurado.setSexo(fn.sexo(newcontenido.toString().split("\n")[i].split("###")[3]) ? 0:1);    
                     asegurados.add( asegurado);                
                }

            }
            modelo.setAsegurados(asegurados);


            return modelo;
        } catch (Exception e) {        
            return modelo;
        }

    }
}
