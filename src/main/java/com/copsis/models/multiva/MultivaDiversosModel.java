package com.copsis.models.multiva;

import java.util.ArrayList;
import java.util.List;

import com.copsis.constants.ConstantsValue;
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
	private int inicio = 0;
	private int fin = 0;
	
	public MultivaDiversosModel(String contenido) {
		this.contenido = contenido;
	}

	public EstructuraJsonModel procesar() {
		String[] arrContenido;
		String lineaTexto = "";
		
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
            	arrContenido = newcontenido.split("\n");
            	for (int i = 0; i < arrContenido.length; i++) {
            		lineaTexto = arrContenido[i];
            		if(lineaTexto.contains("CONTRATANTE") &&lineaTexto.contains(ConstantsValue.POLIZA_ACENT2)) {
						modelo.setCteNombre(arrContenido[i+1].split("Agente")[0].replace("###", "").trim());
						modelo.setPoliza(lineaTexto.split(ConstantsValue.POLIZA_ACENT2)[1].replace("###", "").replace("-", "").trim());
						modelo.setPolizaGuion(lineaTexto.split(ConstantsValue.POLIZA_ACENT2)[1].replace("###", "").trim());
					}
            		if(lineaTexto.contains("Agente") && lineaTexto.contains("Oficina") && lineaTexto.contains("Moneda")) {
						modelo.setCveAgente(arrContenido[i+1].split("###")[0]);
						modelo.setMoneda(fn.moneda(arrContenido[i+1].split("###")[2]));
					}
            		if(lineaTexto.contains("Vigencia") && lineaTexto.contains("Pago")) {					
						modelo.setFormaPago(fn.formaPago(arrContenido[i+1].split("###")[1].replace("###", "")));
						modelo.setCteDireccion(lineaTexto.split("###")[0] +" "+ arrContenido[i+2].split("###")[0]);
					}
            		if(lineaTexto.split("-").length  > 4) {
            			modelo.setVigenciaDe(fn.formatDateMonthCadena(lineaTexto.split("###")[lineaTexto.split("###").length -2].trim()));
            			modelo.setVigenciaA(fn.formatDateMonthCadena(lineaTexto.split("###")[lineaTexto.split("###").length -1].trim()));            		
            		}
            		if(lineaTexto.contains("C.P:")) {
            			modelo.setCp(lineaTexto.split("C.P:")[1].split("###")[1]);
            		}
            		if(lineaTexto.contains("R.F.C:")) {
            			modelo.setRfc(lineaTexto.split("R.F.C:")[1].split("###")[1]);
            		}
            		if(lineaTexto.contains("Asegurado:")) {
            			modelo.setIdCliente(lineaTexto.split("Asegurado:")[1].split("###")[1]);
            		}
            		if(lineaTexto.contains("Emisión")) {
            			modelo.setFechaEmision(fn.formatDateMonthCadena(lineaTexto.split("Emisión")[1].split("###")[1]));
            		}
            	}            	
            }
            
            //PRIMAS
            leerPrimas();			
            obetenerCoberturas();
			
			
			return modelo;
		} catch (Exception ex) {
			modelo.setError(
					MultivaDiversosModel.this.getClass().getTypeName() + " - catch:" + ex.getMessage() + " | " + ex.getCause());
			return modelo;
		}
		
	}

	private void leerPrimas() {
        inicio = contenido.indexOf("Observaciones");
        fin = contenido.indexOf("En testimonio de");
        String[] arrNewContenido;
        String lineaTexto;
        
        if(inicio > 0 &&  fin >  0 && inicio < fin) {
        	newcontenido = contenido.substring(inicio,fin).replace("\r", "").replace("@@@", "");
        	arrNewContenido = newcontenido.split("\n");
        	
        	for (int i = 0; i < arrNewContenido.length; i++) {
        		lineaTexto = arrNewContenido[i];
				if (lineaTexto.contains("Prima Neta")) {
					modelo.setPrimaneta(fn.castBigDecimal(
							fn.castDouble(lineaTexto.split("###")[lineaTexto.split("###").length - 1])));
				} else if (lineaTexto.contains("Recargo")) {
					modelo.setRecargo(fn.castBigDecimal(
							fn.castDouble(lineaTexto.split("###")[lineaTexto.split("###").length - 1])));
				} else if (lineaTexto.contains("Derechos")) {
					modelo.setDerecho(fn.castBigDecimal(
							fn.castDouble(lineaTexto.split("###")[lineaTexto.split("###").length - 1])));
				} else if (lineaTexto.contains("I.V.A")) {
					modelo.setIva(fn.castBigDecimal(
							fn.castDouble(lineaTexto.split("###")[lineaTexto.split("###").length - 1])));
				} else if (lineaTexto.contains("Prima Total")) {
					modelo.setPrimaTotal(fn.castBigDecimal(
							fn.castDouble(lineaTexto.split("###")[lineaTexto.split("###").length - 1])));
				}
			}
        }
	}
	
	private void obetenerCoberturas() {
        inicio = contenido.indexOf("DESGLOSE DE COBERTURAS");
        fin = contenido.indexOf("Observaciones");
        String[] arrNewContenido;
        String lineaTexto = "";
        
        if(inicio > 0 &&  fin >  0 && inicio < fin) {
        	List<EstructuraCoberturasModel> coberturas = new ArrayList<>();
        	newcontenido = contenido.substring(inicio,fin).replace("\r", "").replace("@@@", "").replace(" *###", "").replace("###### ", "###");
        	arrNewContenido = newcontenido.split("\n");
        	for (int i = 0; i < arrNewContenido.length; i++) {	
        		EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();
        		lineaTexto = arrNewContenido[i];
        		if(!lineaTexto.contains("DESGLOSE") || !lineaTexto.contains("Sección")) {            			
        			cobertura.setSeccion(lineaTexto.split("###")[0]);
        		    cobertura.setNombre(lineaTexto.split("###")[1]);
        		    cobertura.setSa(lineaTexto.split("###")[2]);
        		    cobertura.setDeducible(lineaTexto.split("###")[3]);
        		    coberturas.add(cobertura);	        
        		}
        	}
        	modelo.setCoberturas(coberturas);
        }
	}

}
