package com.copsis.models.multiva;

import java.util.ArrayList;
import java.util.List;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraCoberturasModel;
import com.copsis.models.EstructuraJsonModel;

public class MultivaDiversosModel {
	// Clases
	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
	// Varaibles
	private String contenido = "";
	private String newcontenido = "";	
	private String resultado = "";
	private String resultadoCbo = "";
	private int inicio = 0;
	private int fin = 0;
	private int donde = 0;
	
	public MultivaDiversosModel(String contenido) {
		this.contenido = contenido;
	}

	public EstructuraJsonModel procesar() {
		contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales())
				.replace("12:00 Horas", "").replace("EDIFICIO", "EDIFICIO###").replace("CONTENIDOS", "CONTENIDOS###")
		        .replace("CONTENIDOS", "CONTENIDOS###").replace("PÉRDIDAS CONSECUENCIALES", "PÉRDIDAS CONSECUENCIALES###")
		        .replace("RESPONSABILIDAD CIVIL GENERAL", "RESPONSABILIDAD CIVIL GENERAL###")
		        .replace("RESPONSABILIDAD CIVIL ARRENDATARIO", "RESPONSABILIDAD CIVIL ARRENDATARIO###")
		        .replace("ROBO DE MERCANCIAS", "ROBO DE MERCANCIAS###")
		        .replace("DINERO Y VALORES", "DINERO Y VALORES###").replace("ROTURA DE MAQUINARIA", "ROTURA DE MAQUINARIA###")
		        .replace("EQUIPO ELECTRONICO ", "EQUIPO ELECTRONICO###").replace("E n testimonio de", "En testimonio de");		
		try {
			// tipo
			modelo.setTipo(7);
			// cia
			modelo.setCia(65);		
			//Datos del contrante

			
	        inicio = contenido.indexOf("PÓLIZA DE SEGURO");
            fin = contenido.indexOf("DESGLOSE DE COBERTURAS");

            if(inicio > 0 &&  fin >  0 && inicio < fin) {
            	newcontenido = contenido.substring(inicio,fin).replace("\r", "").replace("@@@", "");
            	for (int i = 0; i < newcontenido.split("\n").length; i++) {	            	
            		if(newcontenido.split("\n")[i].contains("CONTRATANTE") && newcontenido.split("\n")[i].contains("Póliza:")) {
						modelo.setCteNombre(newcontenido.split("\n")[i+1].split("Agente")[0].replace("###", "").trim());
						modelo.setPoliza(newcontenido.split("\n")[i].split("Póliza:")[1].replace("###", "").replace("-", "").trim());
						modelo.setPolizaGuion(newcontenido.split("\n")[i].split("Póliza:")[1].replace("###", "").trim());
					}
            		if(newcontenido.split("\n")[i].contains("Agente") && newcontenido.split("\n")[i].contains("Oficina") && newcontenido.split("\n")[i].contains("Moneda")) {
						modelo.setCveAgente(newcontenido.split("\n")[i+1].split("###")[0]);
						modelo.setMoneda(fn.moneda(newcontenido.split("\n")[i+1].split("###")[2]));
					}
            		if(newcontenido.split("\n")[i].contains("Vigencia") && newcontenido.split("\n")[i].contains("Pago")) {					
						modelo.setFormaPago(fn.formaPago(newcontenido.split("\n")[i+1].split("###")[1].replace("###", "")));
						modelo.setCteDireccion(newcontenido.split("\n")[i].split("###")[0] +" "+ newcontenido.split("\n")[i+2].split("###")[0]);
					}
            		if(newcontenido.split("\n")[i].split("-").length  > 4) {
            			modelo.setVigenciaDe(fn.formatDateMonthCadena(newcontenido.split("\n")[i].split("###")[newcontenido.split("\n")[i].split("###").length -2].trim()));
            			modelo.setVigenciaA(fn.formatDateMonthCadena(newcontenido.split("\n")[i].split("###")[newcontenido.split("\n")[i].split("###").length -1].trim()));            		
            		}
            		if(newcontenido.split("\n")[i].contains("C.P:")) {
            			modelo.setCp(newcontenido.split("\n")[i].split("C.P:")[1].split("###")[1]);
            		}
            		if(newcontenido.split("\n")[i].contains("R.F.C:")) {
            			modelo.setRfc(newcontenido.split("\n")[i].split("R.F.C:")[1].split("###")[1]);
            		}
            		if(newcontenido.split("\n")[i].contains("Asegurado:")) {
            			modelo.setIdCliente(newcontenido.split("\n")[i].split("Asegurado:")[1].split("###")[1]);
            		}
            		if(newcontenido.split("\n")[i].contains("Emisión")) {
            			modelo.setFechaEmision(fn.formatDateMonthCadena(newcontenido.split("\n")[i].split("Emisión")[1].split("###")[1]));
            		}
            	}            	
            }
            
            //PRIMAS
            inicio = contenido.indexOf("Observaciones");
            fin = contenido.indexOf("En testimonio de");
            if(inicio > 0 &&  fin >  0 && inicio < fin) {
            	newcontenido = contenido.substring(inicio,fin).replace("\r", "").replace("@@@", "");
            	for (int i = 0; i < newcontenido.split("\n").length; i++) {
					if( newcontenido.split("\n")[i].contains("Prima Neta")) {
						modelo.setPrimaneta( fn.castBigDecimal(fn.castDouble(newcontenido.split("\n")[i].split("###")[newcontenido.split("\n")[i].split("###").length -1])));
					}
					if(newcontenido.split("\n")[i].contains("Recargo")) {
						modelo.setRecargo(fn.castBigDecimal(fn.castDouble(newcontenido.split("\n")[i].split("###")[newcontenido.split("\n")[i].split("###").length -1])));
					}
					if(newcontenido.split("\n")[i].contains("Derechos")) {
                    	modelo.setDerecho( fn.castBigDecimal(fn.castDouble(newcontenido.split("\n")[i].split("###")[newcontenido.split("\n")[i].split("###").length -1])));
					}
					if(newcontenido.split("\n")[i].contains("I.V.A")) {
	  	            	   modelo.setIva( fn.castBigDecimal(fn.castDouble(newcontenido.split("\n")[i].split("###")[newcontenido.split("\n")[i].split("###").length -1])));
	  					}
					if(newcontenido.split("\n")[i].contains("Prima Total")) {
	   	            	   modelo.setPrimaTotal( fn.castBigDecimal(fn.castDouble(newcontenido.split("\n")[i].split("###")[newcontenido.split("\n")[i].split("###").length -1])));
	   					} 
				}
            }
            
			
            inicio = contenido.indexOf("DESGLOSE DE COBERTURAS");
            fin = contenido.indexOf("Observaciones");
        
            if(inicio > 0 &&  fin >  0 && inicio < fin) {
            	List<EstructuraCoberturasModel> coberturas = new ArrayList<>();
            	newcontenido = contenido.substring(inicio,fin).replace("\r", "").replace("@@@", "").replace(" *###", "").replace("###### ", "###");
            	for (int i = 0; i < newcontenido.split("\n").length; i++) {	
            		EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();
            		if(newcontenido.split("\n")[i].contains("DESGLOSE") || newcontenido.split("\n")[i].contains("Sección")) {            			
            		}else  {
            			cobertura.setSeccion(newcontenido.split("\n")[i].split("###")[0]);
            		    cobertura.setNombre(newcontenido.split("\n")[i].split("###")[1]);
            		    cobertura.setSa(newcontenido.split("\n")[i].split("###")[2]);
            		    cobertura.setDeducible(newcontenido.split("\n")[i].split("###")[3]);
            		    coberturas.add(cobertura);	        
            		}
            	}
            	modelo.setCoberturas(coberturas);
            }
			
			
			
			return modelo;
		} catch (Exception ex) {
			modelo.setError(
					MultivaDiversosModel.this.getClass().getTypeName() + " - catch:" + ex.getMessage() + " | " + ex.getCause());
			return modelo;
		}
		
	}

}
