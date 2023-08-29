package com.copsis.models.allians;

import java.util.ArrayList;
import java.util.List;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraCoberturasModel;
import com.copsis.models.EstructuraJsonModel;

public class AlliansDiversosCModel {

    private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();

    public  EstructuraJsonModel procesar(String contenido ) {
        contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());
        StringBuilder newcontenido = new StringBuilder();		
		int inicio = 0;
		int fin = 0;	

        try {
            modelo.setTipo(7);		
			modelo.setCia(4);


			inicio = contenido.indexOf("PÓLIZA DE SEGURO");
			fin = contenido.indexOf("En testimonio de lo");
            newcontenido.append( fn.extracted(inicio, fin, contenido));

			for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {

                if(newcontenido.toString().split("\n")[i].contains("RAZÓN SOCIAL")){
                  modelo.setCteNombre(newcontenido.toString().split("\n")[i+1].split("###")[0]);
                }
                 if(newcontenido.toString().split("\n")[i].contains("PÓLIZA") && newcontenido.toString().split("\n")[i].contains("DESDE")
                 && newcontenido.toString().split("\n")[i].contains("HASTA")){
                    modelo.setPoliza( newcontenido.toString().split("\n")[i+1].split("###")[0] );
                    List<String> valores = fn.obtenVigePoliza(newcontenido.toString().split("\n")[i+1]);
                    if(!valores.isEmpty()){
                     modelo.setVigenciaDe(fn.formatDateMonthCadena(valores.get(1)));
                     modelo.setFechaEmision(modelo.getVigenciaDe());
                      modelo.setVigenciaA(fn.formatDateMonthCadena(valores.get(2)));
                    }
                modelo.setMoneda(fn.buscaMonedaEnTexto(newcontenido.toString().split("\n")[i+1]));
                 
                }                 
                if(newcontenido.toString().split("\n")[i].contains("DIRECCIÓN")){
                  modelo.setCteDireccion(newcontenido.toString().split("\n")[i+1] +" "+
                  newcontenido.toString().split("\n")[i+2] +" " +
                  newcontenido.toString().split("\n")[i+3]);  
                  
                  if(!modelo.getCteDireccion().isEmpty() && modelo.getCteDireccion().contains("C.P.")){
                    List<String> valores = fn.obtenerListNumeros2(modelo.getCteDireccion().split("C.P")[1]);
                     if(!valores.isEmpty()){
                      modelo.setCp(valores.get(0));
                     }
                  }
                }
                if(newcontenido.toString().split("\n")[i].contains("AGENTE")){
                      List<String> valores = fn.obtenerListNumeros2(newcontenido.toString().split("\n")[i+1]);
                    if(!valores.isEmpty()){
                      modelo.setCveAgente(valores.get(0));
                      modelo.setAgente(newcontenido.toString().split("\n")[i+1].split(modelo.getCveAgente())[1].trim());
                    }                     
                }
            }

          
			inicio = contenido.indexOf("SUMAS ASEGURADAS");
			fin = contenido.indexOf("ESPECIFICACIÓN DE LOS");

            newcontenido = new StringBuilder();
            newcontenido.append( fn.extracted(inicio, fin, contenido).replace(":", "###"));
             List<EstructuraCoberturasModel> coberturas = new ArrayList<>();
            for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
                EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();                
                if(!newcontenido.toString().split("\n")[i].contains("SUMAS")
                 && !newcontenido.toString().split("\n")[i].contains("CONDICIONES")){
                    cobertura.setNombre(newcontenido.toString().split("\n")[i].split("###")[0].trim());
                    cobertura.setSa(newcontenido.toString().split("\n")[i].split("###")[1].trim());
                    coberturas.add(cobertura);
                }               
            }
            modelo.setCoberturas(coberturas);

            inicio = contenido.indexOf("RESUMEN DE COSTOS");
			fin = contenido.lastIndexOf("Para cualquier");
          
            newcontenido = new StringBuilder();
            newcontenido.append( fn.extracted(inicio, fin, contenido).replace(":", "###"));
            for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
                
                if(newcontenido.toString().split("\n")[i].contains("")){

                }
                if(newcontenido.toString().split("\n")[i].contains("Forma de Pago")){
                   modelo.setFormaPago(fn.formaPagoSring(newcontenido.toString().split("\n")[i]));   
                 
                }
                if(newcontenido.toString().split("\n")[i].contains("Prima Neta")){
                    List<String> valores = fn.obtenerListNumeros2(newcontenido.toString().split("\n")[i].replace(",", ""));
                    if(!valores.isEmpty()){
                        modelo.setPrimaneta(fn.castBigDecimal(fn.castDouble(valores.get(0))));
                    }
                }
                if(newcontenido.toString().split("\n")[i].contains("Financiamiento")){
                     List<String> valores = fn.obtenerListNumeros2(newcontenido.toString().split("\n")[i].replace(",", ""));
                    if(!valores.isEmpty()){
                        modelo.setRecargo(fn.castBigDecimal(fn.castDouble(valores.get(0))));
                    } 
                }
                if(newcontenido.toString().split("\n")[i].contains("Derecho")){
                    List<String> valores = fn.obtenerListNumeros2(newcontenido.toString().split("\n")[i].replace(",", ""));
                    if(!valores.isEmpty()){
                        modelo.setDerecho(fn.castBigDecimal(fn.castDouble(valores.get(0))));
                    }  
                }
                if(newcontenido.toString().split("\n")[i].contains("Impuesto")){
                      List<String> valores = fn.obtenerListNumeros2(newcontenido.toString().split("\n")[i].replace(",", ""));
                    if(!valores.isEmpty()){
                        modelo.setIva(fn.castBigDecimal(fn.castDouble(valores.get(0))));
                    }
                }
                if(newcontenido.toString().split("\n")[i].contains("Prima Total")){
                      List<String> valores = fn.obtenerListNumeros2(newcontenido.toString().split("\n")[i].replace(",", ""));
                    if(!valores.isEmpty()){
                        modelo.setPrimaTotal(fn.castBigDecimal(fn.castDouble(valores.get(0))));
                    }
                }
            
            }



            return modelo;
            
        } catch (Exception ex) {
            ex.printStackTrace();
            modelo.setError(AlliansDiversosCModel.this.getClass().getTypeName() + " | " + ex.getMessage() + " | " + ex.getCause());
             return modelo;
        }
    
    }
    
}
