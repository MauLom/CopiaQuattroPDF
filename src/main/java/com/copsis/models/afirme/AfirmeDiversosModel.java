package com.copsis.models.afirme;

import java.util.ArrayList;
import java.util.List;

import com.copsis.constants.ConstantsValue;
import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraCoberturasModel;
import com.copsis.models.EstructuraJsonModel;
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
            		modelo.setCteNombre(newcontenido.toString().split("\n")[i].split("Asegurado")[1].replace("\"", "").replace("###", "").replace(":", "").trim());
            	}
            	if(newcontenido.toString().split("\n")[i].contains("Domicilio en:")) {
            		modelo.setCteDireccion(newcontenido.toString().split("\n")[i].split("Domicilio en:")[1].replace("###", "").trim());
            	}
            	if(newcontenido.toString().split("\n")[i].contains("C.P.") && newcontenido.toString().split("\n")[i].split("C.P.")[1].length() > 4 ) {            		
            		modelo.setCp(newcontenido.toString().split("\n")[i].split("C.P.")[1].trim());
            	}
            	
			}
            if(modelo.getVigenciaDe().length() > 0) {
            	modelo.setFechaEmision(modelo.getVigenciaDe());
            }
        
            inicio = contenido.indexOf("Ubicación");
            fin =contenido.indexOf("Coberturas###Suma Asegurada");
            newcontenido = new StringBuilder();
            newcontenido.append(fn.extracted(inicio, fin, contenido));
        	List<EstructuraUbicacionesModel> ubicaciones = new ArrayList<>();
        	EstructuraUbicacionesModel ubicacion = new EstructuraUbicacionesModel();
            for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {            
            	if(newcontenido.toString().split("\n")[i].contains("Ubicación")) {
            		ubicacion.setCalle(newcontenido.toString().split("\n")[i].split("Ubicación")[1].replace(":", "").replace("###", "").trim().substring(0,30));	
            	}
               if(newcontenido.toString().split("\n")[i].contains("Giro Asegurado")) {
            		ubicacion.setGiro(newcontenido.toString().split("\n")[i].split("Giro Asegurado")[1].replace(":", "").replace("###", "").trim());
            	}
            }
            ubicaciones.add(ubicacion);
            modelo.setUbicaciones(ubicaciones);
           
            
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
            for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {          
            	EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();
            	if(!newcontenido.toString().split("\n")[i].contains("Suma Asegurada")) {            
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
			
			return modelo;
		} catch (Exception ex) {
			modelo.setError(AfirmeDiversosModel.this.getClass().getTypeName() + " - catch:" + ex.getMessage() + " | "
					+ ex.getCause());
			return modelo;
		}
		
	}
	
	
}
