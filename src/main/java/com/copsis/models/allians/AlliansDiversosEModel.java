package com.copsis.models.allians;

import java.util.List;
import java.util.stream.Collectors;

import com.copsis.constants.ConstantsValue;
import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraJsonModel;

public class AlliansDiversosEModel {
      private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();

    public  EstructuraJsonModel procesar(String contenido ) {
        StringBuilder newcontenido = new StringBuilder();
	
		int inicio = 0;
		int fin = 0;		
		contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());
		try {
			modelo.setTipo(7);		
			modelo.setCia(4);

			
			inicio = contenido.indexOf("PÓLIZA DE SEGURO");
			fin = contenido.indexOf("Coberturas###Deducibles");			
			newcontenido.append( fn.extracted(inicio, fin, contenido));
			for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {			
				if(newcontenido.toString().split("\n")[i].contains("Residencial")){
					modelo.setCteNombre(newcontenido.toString().split("\n")[i].split("Residencial")[0].replace("###", ""));
					modelo.setCteDireccion((newcontenido.toString().split("\n")[i+2]
					+" "+ newcontenido.toString().split("\n")[i+3]).replace("###", " "));
									
				}
				if(newcontenido.toString().split("\n")[i].contains("DESDE")
				 && newcontenido.toString().split("\n")[i].contains("HASTA")
				 && newcontenido.toString().split("\n")[i].contains("FECHA DE EMISIÓN")){
					modelo.setPoliza(newcontenido.toString().split("\n")[i+1].split("###")[0].replace("###", ""));			
                 List<String> valores = fn.obtenVigePoliza(newcontenido.toString().split("\n")[i+1]);				
					if(valores.size() == 3){
                      modelo.setVigenciaDe(fn.formatDateMonthCadena(valores.get(0)));
					  modelo.setVigenciaA(fn.formatDateMonthCadena(valores.get(1)));
					  modelo.setFechaEmision(fn.formatDateMonthCadena(valores.get(2)));
					}				 
				}
            }
			if(!modelo.getCteDireccion().isEmpty()){
                 List<String> valores = fn.obtenerListNumeros2(modelo.getCteDireccion().split(ConstantsValue.CODIGO_POSTALPT)[1]);
                if(!valores.isEmpty()){
                    modelo.setCp(valores.stream()
                        .filter(numero -> String.valueOf(numero).length() >= 4)
                        .collect(Collectors.toList()).get(0));
                }
			}

			inicio = contenido.indexOf("Coberturas");
			fin = contenido.indexOf(ConstantsValue.PRIMA_NETA_MAYUS2);	
			newcontenido = new StringBuilder();		
			newcontenido.append( fn.extracted(inicio, fin, contenido));
			
			
			for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
				if( newcontenido.toString().split("\n")[i].contains("AGENTE")){
                List<String> valores = fn.obtenerListNumeros2(newcontenido.toString().split("\n")[i]);	
				  if(!valores.isEmpty()){
					 modelo.setCveAgente(valores.get(0));
					 modelo.setAgente(newcontenido.toString().split("\n")[i-1] +" "+ newcontenido.toString().split("\n")[i].split(modelo.getCveAgente())[1].replace("###", "").replace("AGENTE", "").trim());
				  }
				}
				if( newcontenido.toString().split("\n")[i].contains("MONEDA")
				&&  newcontenido.toString().split("\n")[i].contains("FORMA DE PAGO")){
                   modelo.setFormaPago(fn.formaPagoSring(newcontenido.toString().split("\n")[i]));
				    modelo.setMoneda(fn.buscaMonedaEnTexto(newcontenido.toString().split("\n")[i]));
				}

			}	
	
			inicio = contenido.indexOf(ConstantsValue.PRIMA_NETA_MAYUS2);
			fin = contenido.indexOf("En caso de que");	
			newcontenido = new StringBuilder();		
			newcontenido.append( fn.extracted(inicio, fin, contenido));
			for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
				if( newcontenido.toString().split("\n")[i].contains(ConstantsValue.PRIMA_NETA_MAYUS2)){
                List<String> valores = fn.obtenerListNumeros2(newcontenido.toString().split("\n")[i+1].replace(",", ""));	
				  modelo.setPrimaneta(fn.castBigDecimal(fn.castDouble(valores.get(0))));
				  modelo.setRecargo(fn.castBigDecimal(fn.castDouble(valores.get(1))));
				  modelo.setDerecho(fn.castBigDecimal(fn.castDouble(valores.get(2))));
				  modelo.setIva(fn.castBigDecimal(fn.castDouble(valores.get(3))));
				  modelo.setPrimaTotal(fn.castBigDecimal(fn.castDouble(valores.get(4))));                    				
			    }

			}
            
            return modelo;            
        } catch (Exception ex) {
          
            modelo.setError(AlliansDiversosEModel.this.getClass().getTypeName() + " | " + ex.getMessage() + " | " + ex.getCause());
             return modelo;
        }
    }
    
}
