package com.copsis.models.axa;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraCoberturasModel;
import com.copsis.models.EstructuraJsonModel;

public class AxaDiversos4Model {
    private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
    public EstructuraJsonModel procesar(String contenido) {
        int inicio = 0;
        int fin = 0;
        StringBuilder newcontenido = new StringBuilder();
        contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());
        try {

            modelo.setTipo(7);
            modelo.setCia(20);
          

            inicio = contenido.indexOf("Carátula de póliza");
            fin = contenido.indexOf("Costo del seguro");
            newcontenido.append(fn.extracted(inicio, fin, contenido));

            for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {                
                if(newcontenido.toString().split("\n")[i].contains("Datos del contratante") && newcontenido.toString().split("\n")[i].contains("Póliza")
                && newcontenido.toString().split("\n")[i+1].contains("Nombre:")){
                    modelo.setCteNombre(newcontenido.toString().split("\n")[i+1].split("Nombre:")[1].split("###")[1].trim());
                    modelo.setPoliza(newcontenido.toString().split("\n")[i+1].split("###")[2]);

                }
                if( newcontenido.toString().split("\n")[i].contains("RFC:")){
                   modelo.setRfc( newcontenido.toString().split("\n")[i].split("RFC:")[1].replace("###", "").trim());
                }
                if( newcontenido.toString().split("\n")[i].contains("Desde:")){
						modelo.setVigenciaDe(fn.formatDateMonthCadena(newcontenido.toString().split("\n")[i].split("Desde:")[1].replace("###", "").trim()));
						modelo.setFechaEmision(modelo.getVigenciaDe());
                        List<String> valores = fn.obtenerListNumeros2(newcontenido.toString().split("\n")[i]);
                    if(!valores.isEmpty()){
                        modelo.setCp(valores.stream()
                            .filter(numero -> String.valueOf(numero).length() >= 4)
                            .collect(Collectors.toList()).get(0));
                    } 
                }
                if( newcontenido.toString().split("\n")[i].contains("Hasta:")){
                    modelo.setVigenciaA(fn.formatDateMonthCadena(newcontenido.toString().split("\n")[i].split("Hasta:")[1].replace("###", "").trim()));
                }
                if(newcontenido.toString().split("\n")[i].contains("Domicilio:") && newcontenido.toString().split("\n")[i].contains("Vigencia")){
                modelo.setCteDireccion(newcontenido.toString().split("\n")[i].split("Domicilio:")[1].split("Vigencia")[0].replace("###", "").trim());
                }
              
                if( newcontenido.toString().split("\n")[i].contains("Moneda:")){
                  modelo.setMoneda(fn.buscaMonedaEnTexto(newcontenido.toString().split("\n")[i]));
                }
                if(newcontenido.toString().split("\n")[i].contains("Nombre del agente:") && newcontenido.toString().split("\n")[i].contains("No. cliente ")){
                   modelo.setAgente(newcontenido.toString().split("\n")[i].split("agente")[1].split("No.")[0].replace("###","").trim());
                }
                if(newcontenido.toString().split("\n")[i].contains("No. del agente:")){
                    List<String> valores = fn.obtenerListNumeros2(newcontenido.toString().split("\n")[i]);
                    modelo.setCveAgente(valores.get(0));
                }
                if( newcontenido.toString().split("\n")[i].contains("Forma de pago")){
                   modelo.setFormaPago(fn.formaPagoSring(newcontenido.toString().split("\n")[i+1]));
                }


            }
           


            inicio = contenido.indexOf("Costo del seguro");
            fin = contenido.indexOf("Datos del asegurado");
            newcontenido = new StringBuilder();
            newcontenido.append(fn.extracted(inicio, fin, contenido));

            for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {  
               
                if(newcontenido.toString().split("\n")[i].length() > 5){                   
                if (newcontenido.toString().split("\n")[i].contains("Prima neta:")) {
                    List<String> valores = fn.obtenerListNumeros2(newcontenido.toString().split("\n")[i].replace(",", ""));
                    if(!valores.isEmpty()){
                        modelo.setPrimaneta(fn.castBigDecimal(fn.castDouble(valores.get(0))));
                    }						
                }
                if (newcontenido.toString().split("\n")[i].contains("Gastos de expedición:")) {
                    List<String> valores = fn.obtenerListNumeros2(newcontenido.toString().split("\n")[i].replace(",", ""));
                    if(!valores.isEmpty()){
                        modelo.setDerecho(fn.castBigDecimal(fn.castDouble(valores.get(0))));
                    }						
                }

                if (newcontenido.toString().split("\n")[i].contains("IVA:")) {
                    List<String> valores = fn.obtenerListNumeros2(newcontenido.toString().split("\n")[i].replace(",", ""));
                    if(!valores.isEmpty()){
                        modelo.setIva(fn.castBigDecimal(fn.castDouble(valores.get(0))));
                    }						
                }

              
                if (newcontenido.toString().split("\n")[i].contains("Prima total:")) {
                    List<String> valores = fn.obtenerListNumeros2(newcontenido.toString().split("\n")[i].replace(",", ""));
                    if(!valores.isEmpty()){
                        modelo.setPrimaTotal(fn.castBigDecimal(fn.castDouble(valores.get(0))));
                    }						
                }
            }

            }

            inicio = contenido.indexOf("Suma asegurada");
            fin = contenido.indexOf("Formas de aseguramiento");
            newcontenido = new StringBuilder();
            newcontenido.append(fn.extracted(inicio, fin, contenido));
             List<EstructuraCoberturasModel> coberturas = new ArrayList<>();
            for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {                       
                if(!newcontenido.toString().split("\n")[i].contains("Suma asegurada")
                   && !newcontenido.toString().split("\n")[i].contains("I. Daños Materiales")
                   && !newcontenido.toString().split("\n")[i].contains("II. Responsabilidad")){
                    EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();
					int sp  = newcontenido.toString().split("\n")[i].split("###").length;
                   
                    if(sp == 2 || sp == 3 || sp == 4){
                        cobertura.setNombre(newcontenido.toString().split("\n")[i].split("###")[0]);
                        cobertura.setSa(newcontenido.toString().split("\n")[i].split("###")[1]);
                        coberturas.add(cobertura);
                    }
                   
                 }
            } 
            modelo.setCoberturas(coberturas);       

            return modelo;
        } catch (Exception ex) {
            modelo.setError(AxaDiversos4Model.this.getClass().getTypeName() + " | " + ex.getMessage() + " | " + ex.getCause());
        return modelo;
        }
    }
}
