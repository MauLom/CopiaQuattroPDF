package com.copsis.models.sspins;

import java.util.List;
import java.util.stream.Collectors;

import com.copsis.constants.ConstantsValue;
import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraJsonModel;

public class SspInsDiversosModel {

    private DataToolsModel fn = new DataToolsModel();
    private EstructuraJsonModel modelo = new EstructuraJsonModel();
    public EstructuraJsonModel procesar(String contenido) {
        int inicio;
        int fin;
        StringBuilder newcontenido = new StringBuilder();
   
        contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());
        try {
          
            modelo.setTipo(7);
            modelo.setCia(150);

            inicio = contenido.indexOf("Carátula de Póliza:");
            fin = contenido.lastIndexOf("Descripción de los Bienes Asegurados");
            fin = fin == -1 ? contenido.indexOf("DESCRIPCIÓN DE LOS BIENES ASEGURADOS"):fin;
       
            newcontenido.append(fn.extracted(inicio, fin, contenido));
            getContratante(newcontenido);

            inicio = contenido.indexOf("Gastos de Expedición");
            inicio = inicio == -1 ? contenido.indexOf("I.V.A."):inicio;

            fin = contenido.lastIndexOf("Las Condiciones Generales");        
            fin = fin == -1 ? contenido.indexOf("El Asegurado y SPP"):fin;

            newcontenido = new StringBuilder();
            getPrimas(contenido, inicio, fin, newcontenido);

        
            return modelo;
        } catch (Exception ex) {
         modelo.setError(
            SspInsDiversosModel.this.getClass().getTypeName() + " - catch:" + ex.getMessage() + " | " + ex.getCause());
			return modelo;
        }

    }

    private void getPrimas(String contenido, int inicio, int fin, StringBuilder newcontenido) {
        newcontenido.append(fn.extracted(inicio, fin, contenido));
   
        for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
         
          if(newcontenido.toString().split("\n")[i].contains("Prima:")){
            List<String> valores = fn.obtenerListNumeros(newcontenido.toString().split("\n")[i]);
            modelo.setPrimaneta(fn.castBigDecimal(fn.castDouble(valores.get(0))));
            modelo.setDerecho(fn.castBigDecimal(fn.castDouble(valores.get(2))));
            modelo.setRecargo(fn.castBigDecimal(fn.castDouble(valores.get(1))));
            modelo.setIva(fn.castBigDecimal(fn.castDouble(valores.get(3))));                    
            modelo.setPrimaTotal(fn.castBigDecimal(fn.castDouble(valores.get(4))));
          }

          if(newcontenido.toString().split("\n")[i].contains("PRIMA")){
            List<String> valores = fn.obtenerListNumeros(newcontenido.toString().split("\n")[i]);
            modelo.setPrimaneta(fn.castBigDecimal(fn.castDouble(valores.get(0))));
            modelo.setDerecho(fn.castBigDecimal(fn.castDouble(valores.get(1))));
       
            modelo.setIva(fn.castBigDecimal(fn.castDouble(valores.get(2))));                    
            modelo.setPrimaTotal(fn.castBigDecimal(fn.castDouble(valores.get(3))));
          }
        }
    }

    private void getContratante(StringBuilder newcontenido) {

        for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
            
               if(newcontenido.toString().split("\n")[i].contains(ConstantsValue.POLIZA_ACENT2) && newcontenido.toString().split("\n")[i].contains("Versión:")
               && newcontenido.toString().split("\n")[i].contains("Emisión:")){
              
                modelo.setPoliza(newcontenido.toString().split("\n")[i].split(ConstantsValue.POLIZA_ACENT2)[1].split("Versión")[0].replace("###", "").trim());
                List<String> valores = fn.obtenVigePoliza(newcontenido.toString().split("\n")[i]);
						modelo.setFechaEmision(fn.formatDateMonthCadena(valores.get(0)));
               }

               if(modelo.getPoliza().isEmpty() && newcontenido.toString().split("\n")[i].contains("No. Póliza") 
                && newcontenido.toString().split("\n")[i].contains("Versión:")){
             
                modelo.setPoliza(newcontenido.toString().split("\n")[i].split("No. Póliza")[1].split("Versión")[0].replace("###", "").trim());
               
               }
             
               if(newcontenido.toString().split("\n")[i].contains("Moneda")){
                    modelo.setMoneda(fn.buscaMonedaEnTexto(newcontenido.toString().split("\n")[i]));
               }
               
               if(newcontenido.toString().split("\n")[i].contains("Forma de Pago")){
                modelo.setFormaPago(fn.formaPagoSring(newcontenido.toString().split("\n")[i]));
               }
               if(modelo.getFormaPago() == 0 && newcontenido.toString().split("\n")[i].contains("Forma de")){
                modelo.setFormaPago(fn.formaPagoSring(newcontenido.toString().split("\n")[i+1]));
               }
               getVigencias(newcontenido, i);
               getAgente(newcontenido, i);

            
               getDireccion(newcontenido, i);
        }

        String[] palabras = {"BLACK TOWER GROUP AGENTE DE SEGUROS Y DE FIANZAS, SA"};

        if(modelo.getAgente().isEmpty()){
            modelo.setAgente(!plan(palabras,newcontenido.toString()).isEmpty() ?plan(palabras,newcontenido.toString()):"");
        }
			

    }

    private void getAgente(StringBuilder newcontenido, int i) {
        if(newcontenido.toString().split("\n")[i].contains("Agente:") && newcontenido.toString().split("\n")[i].contains("Forma de Pago:")){
            modelo.setAgente(newcontenido.toString().split("\n")[i].split("Agente:")[1].split("Forma")[0].replace("###", "").trim());
           }
           if(newcontenido.toString().split("\n")[i].contains("Nombre:") && newcontenido.toString().split("\n")[i].contains("RFC:")){
            modelo.setCteNombre(newcontenido.toString().split("\n")[i].split("Nombre")[1].split("RFC")[0].replace("###", "").trim());
           }
    }

    private  String  plan (String[] palabras,String newcontenido) {
		String palabraEcontrada="";
		for (String palabra : palabras) {
			if (newcontenido.contains(palabra)){
				 palabraEcontrada = palabra;
			} 
		}

		return palabraEcontrada;
	}

    private void getDireccion(StringBuilder newcontenido, int i) {
        if(newcontenido.toString().split("\n")[i].contains("Domicilio:") && newcontenido.toString().split("\n")[i].contains("Colonia:")){
            modelo.setCteDireccion(newcontenido.toString().split("\n")[i].split("Domicilio")[1].replace("Colonia:", "").replace("###", "").trim());
           }
        if(newcontenido.toString().split("\n")[i].contains("C.P:")){
            
                List<String> valores = fn.obtenerListNumeros2(newcontenido.toString().split("\n")[i]);
                        if(!valores.isEmpty()){
                            modelo.setCp(valores.stream()
                                .filter(numero -> String.valueOf(numero).length() >= 4)
                                .collect(Collectors.toList()).get(0));
                        }
                
           }
    }

    private void getVigencias(StringBuilder newcontenido, int i) {
        if(newcontenido.toString().split("\n")[i].contains("Inicia a las")){
            List<String> valores = fn.obtenVigePolizaFmDE(newcontenido.toString().split("\n")[i]);
            modelo.setVigenciaDe(fn.formatDateMonthCadena(valores.get(0).replace("de", "-").replace(" ", "")));
            modelo.setFechaEmision(modelo.getVigenciaDe());
           }
           if(newcontenido.toString().split("\n")[i].contains("Termina a las")){
            List<String> valores = fn.obtenVigePolizaFmDE(newcontenido.toString().split("\n")[i]);
            modelo.setVigenciaA(fn.formatDateMonthCadena(valores.get(0).replace("de", "-").replace(" ", "")));
           }
    }
   
}
