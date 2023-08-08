package com.copsis.models.afirme;

import java.util.ArrayList;
import java.util.List;

import com.copsis.constants.ConstantsValue;
import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraCoberturasModel;
import com.copsis.models.EstructuraJsonModel;
import com.copsis.models.EstructuraUbicacionesModel;

public class AfirmeDiversosBModel {
	
	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
	private String contenido = "";
	
	public AfirmeDiversosBModel(String contenido) {
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
            fin =contenido.indexOf("BIENES");
            newcontenido.append(fn.extracted(inicio, fin, contenido));
        	List<EstructuraUbicacionesModel> ubicaciones = new ArrayList<>();
        	EstructuraUbicacionesModel ubicacion = new EstructuraUbicacionesModel();
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
            	
            	if(newcontenido.toString().split("\n")[i].contains("El asegurado")) {
            		modelo.setCteNombre(newcontenido.toString().split("\n")[i].split("El asegurado")[1].replace("\"", "").replace("###", "").replace(":", "").trim());
            	}
            	if(newcontenido.toString().split("\n")[i].contains("Domicilio en:")) {
            		modelo.setCteDireccion(newcontenido.toString().split("\n")[i].split("Domicilio en:")[1].replace("###", "").trim());
            		ubicacion.setCalle(modelo.getCteDireccion());
            		
            	}
            	if(newcontenido.toString().split("\n")[i].contains("C.P.") && newcontenido.toString().split("\n")[i].split("C.P.")[1].length() > 4 ) {            		
            		modelo.setCp(newcontenido.toString().split("\n")[i].split("C.P.")[1].trim());
            		ubicacion.setCp(modelo.getCp());
            	}
            }
            if(modelo.getVigenciaDe().length() > 0) {
            	modelo.setFechaEmision(modelo.getVigenciaDe());
            }
            
            ubicaciones.add(ubicacion);
            modelo.setUbicaciones(ubicaciones);
            
            
            inicio = contenido.indexOf("Prima Neta");
            fin =contenido.indexOf("En testimonio de lo cua");

			fin = fin < inicio ? contenido.lastIndexOf("En testimonio de lo cual"):fin;
			
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
			
     
            inicio = contenido.indexOf("COBERTURAS INCISO1");			
            fin =contenido.indexOf("De acuerdo al Art.");

			String coberturasExt="";
			String cbx="";

			for( int i=0; i < contenido.split("Bienes y Riesgos").length;i++){				
				if(i > 0){
					coberturasExt +=contenido.split("Bienes y Riesgos")[i].split("En testimonio de")[0].replace("\r","").replace("@@@", "");
				}			
			}

		


			

			
            newcontenido = new StringBuilder();
            if(coberturasExt.isEmpty()){
				newcontenido.append(fn.extracted(inicio, fin, contenido));
			}else{
				newcontenido.append(coberturasExt);
			}
			

            List<EstructuraCoberturasModel> coberturas = new ArrayList<>();
            for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {            	
            	EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();
            	if(!newcontenido.toString().split("\n")[i].contains("Suma Asegurada")
            		&& !newcontenido.toString().split("\n")[i].contains("COBERTURAS INCISO")
					&& !newcontenido.toString().split("\n")[i].contains("EDIFICIO")
					&& !newcontenido.toString().split("\n")[i].contains("CONTENIDOS")
					&& !newcontenido.toString().split("\n")[i].contains("RESPONSABILIDAD CIVIL")
					&& !newcontenido.toString().split("\n")[i].contains("Prima")
					&& !newcontenido.toString().split("\n")[i].contains("Concepto")
					&& !newcontenido.toString().split("\n")[i].contains("Forma")
					&& !newcontenido.toString().split("\n")[i].contains("Cláusula")
					&& !newcontenido.toString().split("\n")[i].contains("CRISTALES")
					&& !newcontenido.toString().split("\n")[i].contains("ROTURA DE MAQUINARIA")
					&& !newcontenido.toString().split("\n")[i].contains("adjunta")
					&& !newcontenido.toString().split("\n")[i].contains("CONSECUENCIALES")
					&& !newcontenido.toString().split("\n")[i].contains("Hasta")
					&& !newcontenido.toString().split("\n")[i].contains("Hasta 1 Mes")
					&& !newcontenido.toString().split("\n")[i].contains("Período de")
					&& !newcontenido.toString().split("\n")[i].contains("Actividades")
					&& !newcontenido.toString().split("\n")[i].contains("Bardeado")
					&& !newcontenido.toString().split("\n")[i].contains("Control")
					&& !newcontenido.toString().split("\n")[i].contains("Número")
            			) {       
							
            		int sp  = newcontenido.toString().split("\n")[i].split("###").length;
            	   switch (sp) {
				case 1:
					if(newcontenido.toString().split("\n")[i].length() > 10) {
						cobertura.setNombre(newcontenido.toString().split("\n")[i].split("###")[0]);
						coberturas.add(cobertura);
					}				
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
			modelo.setError(AfirmeDiversosBModel.this.getClass().getTypeName() + " - catch:" + ex.getMessage() + " | "
					+ ex.getCause());
			return modelo;
		}
		
	}
}
