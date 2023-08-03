package com.copsis.models.continental;

import java.util.ArrayList;
import java.util.List;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraCoberturasModel;
import com.copsis.models.EstructuraJsonModel;

public class ContinentalAssitSalud {
    private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
    	public EstructuraJsonModel procesar(String contenido) {
            int inicio = 0;		    
            int fin = 0;
            StringBuilder newcontenido = new StringBuilder();        
            contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());

            try {
                modelo.setTipo(3);
                modelo.setCia(113);
  
                inicio = contenido.indexOf("Información General");
                fin = contenido.indexOf("Contacto de Emergencia");	
                newcontenido.append( fn.extracted(inicio, fin, contenido));
                for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
               
                  if(newcontenido.toString().split("\n")[i].contains("Fecha de emisión")){
                     List<String> valores = fn.obtenVigePoliza(newcontenido.toString().split("\n")[i]);
                   modelo.setFechaEmision(fn.formatDateMonthCadena(valores.get(0)));
                  }
                  if(newcontenido.toString().split("\n")[i].contains("Fecha Inicio")){
                   List<String> valores = fn.obtenVigePoliza(newcontenido.toString().split("\n")[i]);
                   modelo.setVigenciaDe(fn.formatDateMonthCadena(valores.get(0)));
                      modelo.setVigenciaA(fn.formatDateMonthCadena(valores.get(1)));
                  }
                  if(newcontenido.toString().split("\n")[i].contains("Precio Total")){
                    List<String> valores = fn.obtenerListNumeros2(newcontenido.toString().split("\n")[i]);
                    modelo.setPrimaneta(fn.castBigDecimal(fn.castDouble(valores.get(0))));
                    modelo.setPrimaTotal(fn.castBigDecimal(fn.castDouble(valores.get(0))));
                  }
                    if(newcontenido.toString().split("\n")[i].contains("Plan")){
                        modelo.setPlan(newcontenido.toString().split("\n")[i].split("Plan")[1].replace("###", "").trim());
                    }
                   if(newcontenido.toString().split("\n")[i].contains("Titular") && newcontenido.toString().split("\n")[i+1].contains("Nombre")
                    && newcontenido.toString().split("\n")[i+1].contains("Apellido")){
                    String x =newcontenido.toString().split("\n")[i+1].replace("Nombre", "").replace("###Apellido", "");
                 
                    modelo.setCteNombre(x.replace("###", " ").trim());
                  }
                 
                }
                modelo.setFormaPago(1);
                modelo.setMoneda(2);

                


                 inicio = contenido.indexOf("Datos de la Agencia");
                 fin = contenido.indexOf("Números Telefonicos");	
                 newcontenido = new StringBuilder();
                 newcontenido.append( fn.extracted(inicio, fin, contenido));
                
                  
                for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
                    if(newcontenido.toString().split("\n")[i].contains("Agente")){
                     modelo.setAgente(newcontenido.toString().split("\n")[i].split("Agente")[1].replace("###", ""));
                    }
                }


                
                 inicio = contenido.indexOf("Beneficio");
                 fin = contenido.indexOf("INSTRUCCIONES");	
                 newcontenido = new StringBuilder();
                 newcontenido.append( fn.extracted(inicio, fin, contenido));
                   List<EstructuraCoberturasModel> coberturas = new ArrayList<>();
                  for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
                    EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();
                    
                    int sp  = newcontenido.toString().split("\n")[i].split("###").length; 
                    if(!newcontenido.toString().split("\n")[i].contains("Beneficio")){                        
                        if(sp == 2){
                            cobertura.setNombre(newcontenido.toString().split("\n")[i].split("###")[0]);
                            cobertura.setSa(newcontenido.toString().split("\n")[i].split("###")[1]);
                            coberturas.add(cobertura);
                        }
                    }
                  }
                  modelo.setCoberturas(coberturas);


                return modelo;
            } catch (Exception ex) {
             modelo.setError(ContinentalAssitSalud.this.getClass().getTypeName() + " - catch:" + ex.getMessage() + " | "+ ex.getCause());
			return modelo;
            }
        
        }
}
