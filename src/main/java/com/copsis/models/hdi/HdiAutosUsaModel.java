package com.copsis.models.hdi;

import java.util.ArrayList;
import java.util.List;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraCoberturasModel;
import com.copsis.models.EstructuraJsonModel;

public class HdiAutosUsaModel {
    private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
    public EstructuraJsonModel procesar(String  contenido) {
        int inicio = 0;
		int fin = 0;
		StringBuilder newcontenido = new StringBuilder();
		contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());

        try {
            modelo.setTipo(1);
			modelo.setCia(14);
          
			inicio = contenido.indexOf("AUTO TOURIST INSURANCE POLICY");
			fin = contenido.indexOf("Liability###Deductible");	
			newcontenido.append( fn.extracted(inicio, fin, contenido));

            for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
               
                if(newcontenido.toString().split("\n")[i].contains("Liability specified below.")){
                  modelo.setCteNombre(newcontenido.toString().split("\n")[i+1].replace("###", "").trim());
                  modelo.setCteDireccion(newcontenido.toString().split("\n")[i+2].replace("###", "").trim());
                }
                if(newcontenido.toString().split("\n")[i].contains("Policy Number:")
                && newcontenido.toString().split("\n")[i].contains("Driver License:")){
                    modelo.setPoliza( newcontenido.toString().split("\n")[i].split("Policy Number:")[1].split("Driver")[0].replace("###", "").trim());
                }
                if(newcontenido.toString().split("\n")[i].contains("Agent:") 
                && newcontenido.toString().split("\n")[i].contains("From:")){
                    String x= newcontenido.toString().split("\n")[i].split("Agent:")[1].split("From:")[0];
                  
                    List<String> valores = fn.obtenerListNumeros2(x);
                    if(!valores.isEmpty()){
                     modelo.setCveAgente(valores.get(0));
                     if(!modelo.getCveAgente().isEmpty()){
                       modelo.setAgente(x.split(modelo.getCveAgente())[1].replace("###", "").trim() );
                     }
                    }
                }
                if(newcontenido.toString().split("\n")[i].contains("RFC:") && 
                newcontenido.toString().split("\n")[i].contains("Issue Date:")){                                                 
                    modelo.setRfc( newcontenido.toString().split("\n")[i].split("RFC:")[1].split("Issue")[0].replace("###", "").trim()); 
                    String x = newcontenido.toString().split("\n")[i]
                     .split("Date:")[1].trim().replace(",","" ).replace(" ", "-");
                     modelo.setVigenciaDe(fn.formatDateMonthCadena((x.split("-")[1] +"-"+x.split("-")[0] +"-"+ x.split("-")[2])));
                     modelo.setVigenciaA(fn.calcvigenciaA(modelo.getVigenciaDe(), 12));
                     modelo.setFechaEmision(modelo.getVigenciaDe());

                }
                 if(newcontenido.toString().split("\n")[i].contains("Type of Use:") 
                 && newcontenido.toString().split("\n")[i].contains("VIN:")
                 && newcontenido.toString().split("\n")[i].contains("Plates:")){                     
                    modelo.setSerie(newcontenido.toString().split("\n")[i].split("VIN:")[1].split("Plates")[0].replace("###", "").trim());
                 }
               

                 if(newcontenido.toString().split("\n")[i].contains("Policy Cash Value: ") 
                 && newcontenido.toString().split("\n")[i].contains("Currency:")){
                    modelo.setMoneda(fn.buscaMonedaEnTexto(newcontenido.toString().split("\n")[i].split("Currency:")[1].toUpperCase().replace(" ","" )));                    
                }
            }


            inicio = contenido.indexOf("Liability###Deductible");
			fin = contenido.indexOf("Net Premium");	
            newcontenido = new StringBuilder();
			newcontenido.append( fn.extracted(inicio, fin, contenido));

            List<EstructuraCoberturasModel> coberturas = new ArrayList<>();

		

            for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
              
               if(!newcontenido.toString().split("\n")[i].contains("Deductible")
               && !newcontenido.toString().split("\n")[i].contains("Per Accident")){
                 
                  EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();
                  switch (newcontenido.toString().split("\n")[i].split("###").length) {
                    case 2:  
                        cobertura.setNombre(newcontenido.toString().split("\n")[i].split("###")[0]);
                        cobertura.setSa(newcontenido.toString().split("\n")[i].split("###")[1]);
                        coberturas.add(cobertura);               
                        break;
                    case 3:   
                       cobertura.setNombre(newcontenido.toString().split("\n")[i].split("###")[0]);
                        cobertura.setSa(newcontenido.toString().split("\n")[i].split("###")[1]);                            
                        cobertura.setDeducible(newcontenido.toString().split("\n")[i].split("###")[2]);
                        coberturas.add(cobertura);                          
                        break; 
                    default:
                        break;
                  }
                  
               }
            }
            modelo.setCoberturas(coberturas);


            inicio = contenido.indexOf("Net Premium");
			fin = contenido.indexOf("Insurance Company:");	
            newcontenido = new StringBuilder();
			newcontenido.append( fn.extracted(inicio, fin, contenido));
            for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {          
              if(newcontenido.toString().split("\n")[i].contains("Net Premium")){
                List<String> valores = fn.obtenerListNumeros(newcontenido.toString().split("\n")[i+1]);
                
                modelo.setPrimaneta(fn.castBigDecimal(fn.castDouble(valores.get(0))));
                modelo.setDerecho(fn.castBigDecimal(fn.castDouble(valores.get(1))));
                modelo.setIva(fn.castBigDecimal(fn.castDouble(valores.get(2))));
                modelo.setPrimaTotal(fn.castBigDecimal(fn.castDouble(valores.get(3))));         
              }
             }
             modelo.setFormaPago(1);

			
            return modelo;
        } catch (Exception ex) {
            modelo.setError(HdiAutosUsaModel.this.getClass().getTypeName() + " | " + ex.getMessage() + " | " + ex.getCause());
            return modelo;
        }

    }
}
