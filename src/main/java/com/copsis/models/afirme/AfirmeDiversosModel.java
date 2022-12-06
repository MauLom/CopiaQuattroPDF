package com.copsis.models.afirme;

import java.util.ArrayList;
import java.util.List;

import com.copsis.constants.ConstantsValue;
import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraCoberturasModel;
import com.copsis.models.EstructuraJsonModel;
import com.copsis.models.EstructuraRecibosModel;
import com.copsis.models.EstructuraUbicacionesModel;

public class AfirmeDiversosModel {
	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
	private String contenido = "";
	
	public AfirmeDiversosModel(String contenido) {
		this.contenido = contenido;
	}
	
	public EstructuraJsonModel procesar() {
		
		StringBuilder newcontenido = new StringBuilder();
		contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales()).replace("PÓLIZA", "POLIZA");
		int inicio = 0;
		int fin = 0;
		try {
			
            modelo.setTipo(7);            
            modelo.setCia(31);

           
            inicio = contenido.indexOf("POLIZA");
            fin =contenido.indexOf("Ubicación");
  
            newcontenido.append(fn.extracted(inicio, fin, contenido));
         
            
            for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {

            	if(newcontenido.toString().split("\n")[i].contains("POLIZA:")) {
            		modelo.setPoliza(newcontenido.toString().split("\n")[i].split("POLIZA:")[1].replace("###", "").trim());
            	}

            	if(newcontenido.toString().split("\n")[i].contains("MONEDA:")) {
            		modelo.setMoneda(fn.buscaMonedaEnTexto(newcontenido.toString().split("\n")[i]));
            	}
            	if(newcontenido.toString().split("\n")[i].contains("Desde") && newcontenido.toString().split("\n")[i].contains("Hasta")
            	&& fn.obtenVigePoliza(newcontenido.toString().split("\n")[i+1]).size() == 2	) {											
					 modelo.setVigenciaDe(fn.formatDateMonthCadena(fn.obtenVigePoliza(newcontenido.toString().split("\n")[i+1]).get(0)));
					 modelo.setVigenciaA(fn.formatDateMonthCadena(fn.obtenVigePoliza(newcontenido.toString().split("\n")[i+1]).get(1)));				 
            	}
            	
            	if(newcontenido.toString().split("\n")[i].contains("Asegurado")) {
            	
            		if(newcontenido.toString().split("\n")[i].split("Asegurado")[1].length() < 30) {
            		modelo.setCteNombre(newcontenido.toString().split("\n")[i].split("Asegurado")[1].replace("\"", "").replace("###", "").replace(":", "").trim());
            		}
            	}
            	if(newcontenido.toString().split("\n")[i].contains("Domicilio en:")) {
            		modelo.setCteDireccion(newcontenido.toString().split("\n")[i].split("Domicilio en:")[1].replace("###", "").trim());
            		if(modelo.getCteNombre().length() == 0 && newcontenido.toString().split("\n")[i-1].contains("El asegurado")) {
            			modelo.setCteNombre(newcontenido.toString().split("\n")[i-1].split("asegurado\":")[1].replace("###", ""));
            		}
            	}
            	if(newcontenido.toString().split("\n")[i].contains("C.P.") && newcontenido.toString().split("\n")[i].split("C.P.")[1].length() > 4 ) {   
            		if(newcontenido.toString().split("\n")[i].split("C.P.")[1].trim().length()>  5) {
            			modelo.setCp("");
            		}else {
            			modelo.setCp(newcontenido.toString().split("\n")[i].split("C.P.")[1].trim());
            		}
            		
            	}
            	
			}
            if(modelo.getVigenciaDe().length() > 0) {
            	modelo.setFechaEmision(modelo.getVigenciaDe());
            }
            
            if(modelo.getCp().length() == 0 && modelo.getCteDireccion().length()> 0) {
            	modelo.setCp(modelo.getCteDireccion().split("C.P.")[1].trim());
           
            }
        
            inicio = contenido.indexOf("Ubicación");
            fin =contenido.indexOf("Coberturas###Suma Asegurada");
            newcontenido = new StringBuilder();
            newcontenido.append(fn.extracted(inicio, fin, contenido));
            if(! newcontenido.toString().contains("Ubicación") && !newcontenido.toString().contains("Giro Asegurado")) {
            
            	newcontenido = new StringBuilder(); 
            	inicio = contenido.indexOf("ESPECIFICACIÓN UBICACIÓ");
                fin =contenido.indexOf("Bienes y Riesgos###Suma Asegurada");
                newcontenido.append(fn.extracted(inicio, fin, contenido));
       
            }
            
        	List<EstructuraUbicacionesModel> ubicaciones = new ArrayList<>();
        	EstructuraUbicacionesModel ubicacion = new EstructuraUbicacionesModel();

    
        	if(newcontenido.toString().length()> 0) {
            for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {            
            	if(newcontenido.toString().split("\n")[i].contains("Ubicación")) {
            		ubicacion.setCalle(newcontenido.toString().split("\n")[i].split("Ubicación")[1].replace(":", "").replace("###", "").trim().substring(0,30));	
            	}
               if(newcontenido.toString().split("\n")[i].contains("Giro Asegurado")) {
            		ubicacion.setGiro(newcontenido.toString().split("\n")[i].split("Giro Asegurado")[1].replace(":", "").replace("###", "").trim());
            	}
               if(newcontenido.toString().split("\n")[i].contains("Giro o Actividad:")) {
           		ubicacion.setGiro(newcontenido.toString().split("\n")[i].split("Giro o Actividad:")[1].replace(":", "").replace("###", "").trim());
           	}
               if(newcontenido.toString().split("\n")[i].contains("Número de pisos:")) {
           		ubicacion.setNiveles(fn.castInteger(fn.obtenerListNumeros2(newcontenido.toString().split("\n")[i].split("Número de pisos:")[1].replace(":", "").replace("###", "").trim()).get(0)));;
           	}
            }
            ubicaciones.add(ubicacion);
            modelo.setUbicaciones(ubicaciones);
		}
           
            
            inicio = contenido.indexOf("Prima Neta");
            fin =contenido.indexOf("En testimonio de lo cua");
            newcontenido = new StringBuilder();
            newcontenido.append(fn.extracted(inicio, fin, contenido));
            for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {          
        		if (newcontenido.toString().split("\n")[i].contains(ConstantsValue.PRIMA_NETA)) {
					modelo.setPrimaneta( fn.castBigDecimal(fn.castDouble(newcontenido.toString().split("\n")[i+1].split("###")[1])));
					modelo.setRecargo( fn.castBigDecimal(fn.castDouble(newcontenido.toString().split("\n")[i+1].split("###")[2])));
					modelo.setDerecho( fn.castBigDecimal(fn.castDouble(newcontenido.toString().split("\n")[i+1].split("###")[3])));
					modelo.setIva( fn.castBigDecimal(fn.castDouble(newcontenido.toString().split("\n")[i+1].split("###")[4])));						
					modelo.setPrimaTotal( fn.castBigDecimal(fn.castDouble(newcontenido.toString().split("\n")[i+1].split("###")[5])));
				}
            	if(newcontenido.toString().split("\n")[i].contains("Forma de pago") && newcontenido.toString().split("\n")[i].contains("Agente")) {
            		modelo.setFormaPago(fn.formaPagoSring(newcontenido.toString().split("\n")[i]));
            		modelo.setCveAgente(newcontenido.toString().split("\n")[i].split("Agente")[1].split("###")[1]);
            		modelo.setAgente(newcontenido.toString().split("\n")[i].split("Agente")[1].split("###")[2]);
            	}
            }
            
           
            inicio = contenido.indexOf("Coberturas###Suma Asegurada");
            fin =contenido.indexOf("Concepto###Prima Neta");
           
            newcontenido = new StringBuilder();
            newcontenido.append(fn.extracted(inicio, fin, contenido));
         
            List<EstructuraCoberturasModel> coberturas = new ArrayList<>();
            if( newcontenido.toString().split("\n").length> 2) {
	            for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {  
	            	
	            	EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();
	            	if(!newcontenido.toString().split("\n")[i].contains("Suma Asegurada")
	            	        && !newcontenido.toString().split("\n")[i].contains("CONTRATISTA")) {            
	            		int sp  = newcontenido.toString().split("\n")[i].split("###").length;
	            	   switch (sp) {
					case 1:
						cobertura.setNombre(newcontenido.toString().split("\n")[i].split("###")[0]);
						coberturas.add(cobertura);
						break;
					case 3:
						cobertura.setNombre(newcontenido.toString().split("\n")[i].split("###")[0]);
						cobertura.setSa(newcontenido.toString().split("\n")[i].split("###")[1]);
						coberturas.add(cobertura);
						break;
					default:
						break;
					}
	            	}
	            }
	            modelo.setCoberturas(coberturas);
            }
            
            if(modelo.getCoberturas().isEmpty()) {
            	
            	 inicio = contenido.indexOf("I###EDIFICIO");
                 fin =contenido.indexOf("Página 4 de 46");
                 

                 newcontenido = new StringBuilder();
                 newcontenido.append(fn.extracted(inicio, fin, contenido));
                 
              
                 if( newcontenido.toString().split("\n").length> 5) {            	
     	            for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {  
     	            	
     	            	EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();
     	            	if(!newcontenido.toString().split("\n")[i].contains("Suma Asegurada") 
     	            	&& 	!newcontenido.toString().split("\n")[i].contains("I###EDIFICIO")
     	            	&& 	!newcontenido.toString().split("\n")[i].contains("II###CONTENIDOS")
     	            	&& 	!newcontenido.toString().split("\n")[i].contains("III###PÉRDIDAS")
     	            	&& 	!newcontenido.toString().split("\n")[i].contains("IV###RESPONSABILIDAD CIVIL")
     	            	&& 	!newcontenido.toString().split("\n")[i].contains("VII###ROBO CON VIOLENCIA")
     	            	&& 	!newcontenido.toString().split("\n")[i].contains("VIII###DINERO Y VALORES")
     	            	&& 	!newcontenido.toString().split("\n")[i].contains("S.E.A.")
     	           	&& 	!newcontenido.toString().split("\n")[i].contains("ProDteecscciróipnc")
     	            			) {  
     	            	
     	            		int sp  = newcontenido.toString().split("\n")[i].split("###").length;
     	            	   switch (sp) {
     					case 1:
     						cobertura.setNombre(newcontenido.toString().split("\n")[i].split("###")[0]);
     						coberturas.add(cobertura);
     						break;
     						
     					case 2:	case 3:
     						cobertura.setNombre(newcontenido.toString().split("\n")[i].split("###")[0]);
     						cobertura.setSa(newcontenido.toString().split("\n")[i].split("###")[1]);
     						coberturas.add(cobertura);
     						break;
     					default:
     						break;
     					}
     	            	}
     	            }
     	            modelo.setCoberturas(coberturas);
                 }
            }
			
            List<EstructuraRecibosModel> recibos = new ArrayList<>();
            EstructuraRecibosModel recibo = new EstructuraRecibosModel();

            if (modelo.getFormaPago() == 1) {
                recibo.setReciboId("");
                recibo.setSerie("1/1");
                recibo.setVigenciaDe(modelo.getVigenciaDe());
                recibo.setVigenciaA(modelo.getVigenciaA());
                if (recibo.getVigenciaDe().length() > 0) {
                    recibo.setVencimiento(fn.dateAdd(recibo.getVigenciaDe(), 30, 1));
                }
                recibo.setPrimaneta(modelo.getPrimaneta());
                recibo.setDerecho(modelo.getDerecho());
                recibo.setRecargo(modelo.getRecargo());
                recibo.setIva(modelo.getDerecho());

                recibo.setPrimaTotal(modelo.getPrimaTotal());
                recibo.setAjusteUno(modelo.getAjusteUno());
                recibo.setAjusteDos(modelo.getAjusteDos());
                recibo.setCargoExtra(modelo.getCargoExtra());
                recibos.add(recibo);
            }

            modelo.setRecibos(recibos);
            
			return modelo;
		} catch (Exception ex) {
			modelo.setError(AfirmeDiversosModel.this.getClass().getTypeName() + " - catch:" + ex.getMessage() + " | "
					+ ex.getCause());
			return modelo;
		}
		
	}
	
	
}
