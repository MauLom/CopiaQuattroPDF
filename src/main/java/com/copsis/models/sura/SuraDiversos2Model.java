package com.copsis.models.sura;

import java.util.ArrayList;
import java.util.List;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraCoberturasModel;
import com.copsis.models.EstructuraJsonModel;

public class SuraDiversos2Model {

    private DataToolsModel fn = new DataToolsModel();
    private EstructuraJsonModel modelo = new EstructuraJsonModel();

    public EstructuraJsonModel procesar(String contenido) {
        int inicio;
		int fin;
		StringBuilder newcontenido = new StringBuilder();
		contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());
        try {
            modelo.setTipo(7);
			modelo.setCia(88);

			inicio = contenido.indexOf("Póliza no.");
			fin = contenido.indexOf("Asegurado, bienes");			
     
			newcontenido.append(fn.extracted(inicio, fin, contenido));
            for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
            
              if(newcontenido.toString().split("\n")[i].contains("Póliza no")){
                modelo.setPoliza(newcontenido.toString().split("\n")[i+1].split("###")[newcontenido.toString().split("\n")[i+1].split("###").length -1]);
              }
              if(newcontenido.toString().split("\n")[i].contains("Datos del contratante")){
                modelo.setCteNombre(newcontenido.toString().split("\n")[i+1].split("###")[0]);
                modelo.setMoneda(fn.buscaMonedaEnTexto(newcontenido.toString().split("\n")[i+1]));
              
              }
                if(newcontenido.toString().split("\n")[i].contains("Emisión")
                && newcontenido.toString().split("\n")[i].contains("Forma de pago")){

                    List<String> valores = fn.obtenVigePoliza(newcontenido.toString().split("\n")[i+1]);
					modelo.setFechaEmision(fn.formatDateMonthCadena(valores.get(0)) );
                    modelo.setFormaPago(fn.formaPagoSring(newcontenido.toString().split("\n")[i+1]));
                }
                if( newcontenido.toString().split("\n")[i].contains("Vigencia desde")){
                    List<String> valores = fn.obtenVigePoliza(newcontenido.toString().split("\n")[i]);
                    modelo.setVigenciaDe(fn.formatDateMonthCadena(valores.get(0)) );
                    modelo.setCteDireccion(newcontenido.toString().split("\n")[i].split("Vigencia")[0].replace("###", ""));
                }
                if(newcontenido.toString().split("\n")[i].contains("C.P.")){
                   modelo.setCp(newcontenido.toString().split("\n")[i].split("C.P.")[1].split("Vigencia")[0].replace(" ", "").trim().substring(0, 5));
                }
                 if( newcontenido.toString().split("\n")[i].contains("Hasta las")){
                    List<String> valores = fn.obtenVigePoliza(newcontenido.toString().split("\n")[i]);
                    modelo.setVigenciaA(fn.formatDateMonthCadena(valores.get(0)) );
                 }
                 if(newcontenido.toString().split("\n")[i].contains("R.F.C.") && newcontenido.toString().split("\n")[i].contains("las 12hrs.")){
                    modelo.setRfc(newcontenido.toString().split("\n")[i].split("R.F.C.")[1].split("las 12hrs")[0].replace("###", ""));
                 }
            }

            inicio = contenido.indexOf("Coberturas contratadas");
			fin = contenido.indexOf("Suma asegurada");			
		    newcontenido = new StringBuilder();
			newcontenido.append(fn.extracted(inicio, fin, contenido));
              List<EstructuraCoberturasModel> coberturas = new ArrayList<>();
            for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
          
             EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();
                if(!newcontenido.toString().split("\n")[i].contains("Coberturas")
                 && !newcontenido.toString().split("\n")[i].contains("Sumas asegurada")){
                    cobertura.setNombre(newcontenido.toString().split("\n")[i].split("###")[0]);
                    cobertura.setSa(newcontenido.toString().split("\n")[i].split("###")[1]);
                    coberturas.add(cobertura);

                }                          
            }
            modelo.setCoberturas(coberturas);

             inicio = contenido.indexOf("Prima neta");
			fin = contenido.indexOf("Pág. 1");			
		    newcontenido = new StringBuilder();
			newcontenido.append(fn.extracted(inicio, fin, contenido).replace(",", ""));
             for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
                
                if( newcontenido.toString().split("\n")[i].contains("Prima neta") && newcontenido.toString().split("\n")[i].contains("financiamiento")){
                
                    List<String> valores = fn.obtenerListNumeros2(newcontenido.toString().split("\n")[i+1]);
                   
                    if(!valores.isEmpty()){
                        modelo.setPrimaneta( fn.castBigDecimal(fn.castDouble(valores.get(0))));
                          modelo.setDerecho( fn.castBigDecimal(fn.castDouble(valores.get(2))));
                        modelo.setIva( fn.castBigDecimal(fn.castDouble(valores.get(4))));
						modelo.setPrimaTotal( fn.castBigDecimal(fn.castDouble(valores.get(5))));
					}
						
                }
             
            }


            
            return modelo;
        } catch (Exception e) {
            return modelo;
        }
    }

}
