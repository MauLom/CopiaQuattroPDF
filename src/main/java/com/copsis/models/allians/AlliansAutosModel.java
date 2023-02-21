package com.copsis.models.allians;

import java.util.ArrayList;
import java.util.List;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraCoberturasModel;
import com.copsis.models.EstructuraJsonModel;

public class AlliansAutosModel {
    private DataToolsModel fn = new DataToolsModel();
    private EstructuraJsonModel modelo = new EstructuraJsonModel();

    public EstructuraJsonModel procesar(String contenido) {
        int inicio = 0;
        int fin = 0;
        StringBuilder newcontenido = new StringBuilder();
        StringBuilder newDireccion = new StringBuilder();
        contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());
        try {
            modelo.setTipo(1);		
			modelo.setCia(4);

          
			inicio = contenido.indexOf("Endoso");
			fin = contenido.indexOf("Suma Asegurada");		
            newcontenido.append( fn.extracted(inicio, fin, contenido));

            for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {    
                    
                if(newcontenido.toString().split("\n")[i].contains("emisión") &&newcontenido.toString().split("\n")[i].contains("Moneda") 
                && newcontenido.toString().split("\n")[i].contains("pago")){                  
                    modelo.setPoliza(newcontenido.toString().split("\n")[i+2].split("###")[0]);
                    modelo.setFormaPago(fn.formaPagoSring(newcontenido.toString().split("\n")[i+2]));
                    modelo.setVigenciaDe(fn.formatDateMonthCadena(fn.obtenVigePoliza(newcontenido.toString().split("\n")[i+2]).get(1)));
                    modelo.setVigenciaA(fn.formatDateMonthCadena(fn.obtenVigePoliza(newcontenido.toString().split("\n")[i+2]).get(2)));
                    modelo.setFechaEmision(modelo.getVigenciaDe());                  
                    modelo.setMoneda(fn.buscaMonedaEnTexto(newcontenido.toString().split("\n")[i+2]));
                }
                if(newcontenido.toString().split("\n")[i].contains("Marca:") && newcontenido.toString().split("\n")[i].contains("Número")
                && newcontenido.toString().split("\n")[i].contains("Motor:")){
                   modelo.setMarca(newcontenido.toString().split("\n")[i].split("Marca:")[1].split("Número")[0].replace("###", "").trim());
                   modelo.setMotor(newcontenido.toString().split("\n")[i].split("Motor")[1].replace(":", "").replace("###", "").trim());
                }
                if(newcontenido.toString().split("\n")[i].contains("Serie")){
                    modelo.setSerie(newcontenido.toString().split("\n")[i].split("Serie")[1].replace(":", "").replace("###", "").trim());
                }
                if(newcontenido.toString().split("\n")[i].contains("Año") && !fn.obtenerListNumeros2(newcontenido.toString().split("\n")[i]).isEmpty()){
                 modelo.setModelo(fn.castInteger(fn.obtenerListNumeros2(newcontenido.toString().split("\n")[i]).get(0)));
                }

                if(newcontenido.toString().split("\n")[i].contains("R.F.C:") && newcontenido.toString().split("\n")[i].contains("Tipo")){
                  modelo.setRfc(newcontenido.toString().split("\n")[i].split("R.F.C:")[1].split("Tipo")[0].replace("###", ""));   
                }

                if(newcontenido.toString().split("\n")[i].contains("Calle") && newcontenido.toString().split("\n")[i].contains("Número")){
                    newDireccion.append(newcontenido.toString().split("\n")[i].split("Calle")[1].split("Número")[0].replace(":", "").replace("###", "") +" ");
                }
                if(newcontenido.toString().split("\n")[i].contains("Colonia") && newcontenido.toString().split("\n")[i].contains("Número")){
                    newDireccion.append(newcontenido.toString().split("\n")[i].split("Colonia")[1].split("Número")[0].replace(":", "").replace("###", "") +" ");
                }
                if(newcontenido.toString().split("\n")[i].contains("Municipio:")){
                    newDireccion.append( newcontenido.toString().split("\n")[i].split("Municipio:")[1].replace("###", ""));
                }
                if(newcontenido.toString().split("\n")[i].contains("Conductor") && newcontenido.toString().split("\n")[i].contains("habitual")
              ){
                modelo.setConductor(newcontenido.toString().split("\n")[i].split("habitual")[1].split("C.P:")[0].replace(":", "").replace("###", "").trim());
                }
              
            }
           modelo.setCteDireccion(newDireccion.toString());
           if(modelo.getCteDireccion().length()>0 && modelo.getCteDireccion().contains("C.P:")){
             modelo.setCp(modelo.getCteDireccion().split("C.P:")[1].trim().substring(0,5));
           }

            inicio = contenido.indexOf("Suma Asegurada");
			fin = contenido.indexOf("financiamiento");
          
            newcontenido = new StringBuilder();		
            newcontenido.append( fn.extracted(inicio, fin, contenido).replace("###-### ", "###"));
            List<EstructuraCoberturasModel> coberturas = new ArrayList<>();
            for (int i = 0; i < newcontenido.toString().split("\n").length; i++) { 
              
                EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();
                if(!newcontenido.toString().split("\n")[i].contains("Suma Asegurada") 
                && !newcontenido.toString().split("\n")[i].contains("Tasa de")) {
                    
						int sp = newcontenido.toString().split("\n")[i].split("###").length;
                        if (sp == 3) {
                            cobertura.setNombre(newcontenido.toString().split("\n")[i].split("###")[0]);
                            cobertura.setSa(newcontenido.toString().split("\n")[i].split("###")[1]);
                            coberturas.add(cobertura);
                        }

                        if (sp == 4) {
                            cobertura.setNombre(newcontenido.toString().split("\n")[i].split("###")[0]);
                            cobertura.setSa(newcontenido.toString().split("\n")[i].split("###")[1]);
                            cobertura.setCoaseguro(newcontenido.toString().split("\n")[i].split("###")[2]);
                            coberturas.add(cobertura);
                        }						 		
					}
            }
            modelo.setCoberturas(coberturas);

            inicio = contenido.indexOf("financiamiento");
			fin = contenido.indexOf("Agente");
         
            newcontenido = new StringBuilder();		
            newcontenido.append( fn.extracted(inicio, fin, contenido));
            for (int i = 0; i < newcontenido.toString().split("\n").length; i++) { 
                if(newcontenido.toString().split("\n")[i].contains("financiamiento")) {
                    List<String> valores = fn.obtenerListNumeros(newcontenido.toString().split("\n")[i+1]);
                    modelo.setPrimaneta(fn.castBigDecimal(fn.castDouble(valores.get(0))));
                    modelo.setDerecho(fn.castBigDecimal(fn.castDouble(valores.get(2))));
                    modelo.setRecargo(fn.castBigDecimal(fn.castDouble(valores.get(1))));
                    modelo.setIva(fn.castBigDecimal(fn.castDouble(valores.get(3))));                    
                    modelo.setPrimaTotal(fn.castBigDecimal(fn.castDouble(valores.get(4))));
                }
            }



            return modelo;
        } catch (Exception ex) {
			modelo.setError(AlliansAutosModel.this.getClass().getTypeName() + " - catch:" + ex.getMessage() + " | "
					+ ex.getCause());
            return modelo;
        }
        
    }

}
