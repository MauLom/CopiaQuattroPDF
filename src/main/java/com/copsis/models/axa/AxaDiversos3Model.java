package com.copsis.models.axa;

import java.util.ArrayList;
import java.util.List;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraCoberturasModel;
import com.copsis.models.EstructuraJsonModel;

public class AxaDiversos3Model {
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

            inicio = contenido.indexOf("Insured's data");
            fin = contenido.indexOf("Package contracted:");// se usa para version 3
            newcontenido.append(fn.extracted(inicio, fin, contenido));

            for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {               
               
              if(newcontenido.toString().split("\n")[i].contains("Policy") && newcontenido.toString().split("\n")[i+1].contains("Name")){
              
                   modelo.setCteNombre(newcontenido.toString().split("\n")[i+1].split("Name")[1].split("###")[1].trim());
                   modelo.setPoliza(newcontenido.toString().split("\n")[i+1].split("Name")[1].split("###")[2].trim());
              }
                if(newcontenido.toString().split("\n")[i].contains("Address")){              
                    modelo.setCteDireccion(newcontenido.toString().split("\n")[i].split("Address")[1].split("###")[1].trim());
                }
                if(newcontenido.toString().split("\n")[i].contains("From")){              
                    modelo.setVigenciaDe(fn.formatDateMonthCadena(fn.obtenVigePoliza2(newcontenido.toString().split("\n")[i]).get(0)));
                    modelo.setFechaEmision(modelo.getVigenciaDe());
                }
                if(newcontenido.toString().split("\n")[i].contains("Contracting party:") 
                  && newcontenido.toString().split("\n")[i].contains("To")){              
                    modelo.setVigenciaA(fn.formatDateMonthCadena(fn.obtenVigePoliza2(newcontenido.toString().split("\n")[i]).get(0)));;
                }
                if(newcontenido.toString().split("\n")[i].contains("Z.C:")) {
                modelo.setCp(newcontenido.toString().split("\n")[i].split("Z.C:")[1].trim().substring(0,5));
                }
                if(newcontenido.toString().split("\n")[i].contains("Made of payment")) {
                    modelo.setFormaPago(fn.formaPagoSring(newcontenido.toString().split("\n")[i+1]));
                }
            }

           
            inicio = contenido.indexOf("Package contracted:");
            fin = contenido.indexOf("Net Premium");// se usa para version 3
            newcontenido = new StringBuilder();
            newcontenido.append(fn.extracted(inicio, fin, contenido));
            List<EstructuraCoberturasModel> coberturas = new ArrayList<>();				
            for (int i = 0; i < newcontenido.toString().split("\n").length; i++) { 
               
                EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();
                if(!newcontenido.toString().split("\n")[i].contains("Best Package")
                 && !newcontenido.toString().split("\n")[i].contains("Coverages")
                 && !newcontenido.toString().split("\n")[i].contains("PROPERTY DAMAGE")
                 && !newcontenido.toString().split("\n")[i].contains("adjustment")
               ){
                  
                  int x = newcontenido.toString().split("\n")[i].split("###").length;
                  switch(x){
                    case 3:
                    cobertura.setNombre(newcontenido.toString().split("\n")[i].split("###")[0]);
                    cobertura.setSa(newcontenido.toString().split("\n")[i].split("###")[1].replace("\r", ""));
                    cobertura.setDeducible(newcontenido.toString().split("\n")[i].split("###")[2].replace("\r", ""));
                    coberturas.add(cobertura);	                   
                    break;
                    case 4:
                    cobertura.setNombre(newcontenido.toString().split("\n")[i].split("###")[0]);
                    cobertura.setSa(newcontenido.toString().split("\n")[i].split("###")[1].replace("\r", ""));
                    cobertura.setDeducible(newcontenido.toString().split("\n")[i].split("###")[2].replace("\r", ""));
                    cobertura.setCoaseguro(newcontenido.toString().split("\n")[i].split("###")[3].replace("\r", ""));
                    coberturas.add(cobertura);	                  
                    break;
                    default:
                    break;
                  }
                }
            }
            modelo.setCoberturas(coberturas);

            inicio = contenido.indexOf("Net Premium");
            fin = contenido.lastIndexOf("AXA Seguros, S.A. de C.V.");// se usa para version 3
           
            newcontenido = new StringBuilder();
            newcontenido.append(fn.extracted(inicio, fin, contenido));

            for (int i = 0; i < newcontenido.toString().split("\n").length; i++) { 
               
                if(newcontenido.toString().split("\n")[i].contains("Net Premium")){
                    List<String> valores = fn.obtenerListNumeros(newcontenido.toString().split("\n")[i]);
                    modelo.setPrimaneta(fn.castBigDecimal(fn.castDouble(valores.get(0))));
                }
                if(newcontenido.toString().split("\n")[i].contains("V.A.T.")){
                    List<String> valores = fn.obtenerListNumeros(newcontenido.toString().split("\n")[i]);
                    modelo.setIva(fn.castBigDecimal(fn.castDouble(valores.get(0))));
                }
                if(newcontenido.toString().split("\n")[i].contains("T o t a l")){
                    List<String> valores = fn.obtenerListNumeros(newcontenido.toString().split("\n")[i]);
                    modelo.setPrimaTotal(fn.castBigDecimal(fn.castDouble(valores.get(0))));
                }
            }
            modelo.setMoneda(2);
            
            return modelo;
        } catch (Exception ex) {
            modelo.setError(
                    AxaDiversos3Model.this.getClass().getTypeName() + " | " + ex.getMessage() + " | " + ex.getCause());
            return modelo;
        }
    }

}
