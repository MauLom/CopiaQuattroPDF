package com.copsis.models.multiva;

import java.util.ArrayList;
import java.util.List;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraAseguradosModel;
import com.copsis.models.EstructuraCoberturasModel;
import com.copsis.models.EstructuraJsonModel;

public class MultivaSaludModel {
	// Clases
	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
	// Varaibles
	private String contenido = "";
	private String newcontenido = "";	

	
	public MultivaSaludModel(String contenido) {
		this.contenido = contenido;
	}
	public EstructuraJsonModel procesar() {
		contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());
		try {
			// tipo
			modelo.setTipo(3);
			// cia
			modelo.setCia(65);		
			//Datos del contrante

			
	        int inicio = contenido.indexOf("Póliza de Seguro");
            int fin = contenido.indexOf("Datos de los Asegurados");

            if(inicio > 0 &&  fin >  0 && inicio < fin) {
            	newcontenido = contenido.substring(inicio,fin).replace("\r", "").replace("@@@", "");
                String[] arrNewContenido = newcontenido.split("\n");
                String lineaTexto = "";
                
            	for (int i = 0; i < arrNewContenido.length; i++) {	
            		lineaTexto = arrNewContenido[i];
					if(lineaTexto.contains("Contratante") && lineaTexto.contains("Póliza:")) {
						modelo.setCteNombre(arrNewContenido[i+1].trim());
						modelo.setPoliza(lineaTexto.split("Póliza:")[1].replace("###", "").trim());
					}
					if(lineaTexto.contains("Agente:") && lineaTexto.contains("Oficina:") && lineaTexto.contains("Póliza")) {
						modelo.setCveAgente(lineaTexto.split("Agente:")[1].split("Oficina:")[0].replace("###", ""));
					}
					if(lineaTexto.contains("Domicilio") && lineaTexto.contains("Moneda:") && lineaTexto.contains("Pago:")) {
						modelo.setMoneda(fn.moneda(lineaTexto.split("Moneda:")[1].split("Forma")[0].replace("###", "")));
						modelo.setFormaPago(fn.formaPago(lineaTexto.split("Pago:")[1].replace("###", "")));
						modelo.setCteDireccion(arrNewContenido[i+1] +" "+ arrNewContenido[i+2].split("###")[0]);
					}
					if(lineaTexto.contains("Plan:")) {
						modelo.setPlan(newcontenido.split("Plan:")[1].split("###")[1]);
					}
					
					if(lineaTexto.contains("-")) {
						if(lineaTexto.split("-").length  > 3) {
						 leerVigencia(lineaTexto);
						}
					}				
				}
            }
            
			
            obtenerAsegurados();
            obtenerDetalleSeguro();
            //PRIMAS
            obtenerPrimas();
            obtenerCoberturas();
            
			
			return modelo;
		} catch (Exception ex) {
			modelo.setError(
					MultivaSaludModel.this.getClass().getTypeName() + " - catch:" + ex.getMessage() + " | " + ex.getCause());
			return modelo;
		}
	}
	
	private void obtenerAsegurados() {
        int inicio = contenido.indexOf("Datos de los Asegurados");
        int fin = contenido.indexOf("Detalle del Seguro");
        String[] arrContenido;
        String lineaTexto;
        
        if(inicio > 0 &&  fin >  0 && inicio < fin) {
        	List<EstructuraAseguradosModel> asegurados = new ArrayList<>();
        	newcontenido = contenido.substring(inicio,fin).replace("\r", "").replace("@@@", "");
        	arrContenido = newcontenido.split("\n");
        	
        	int indexNacimiento = newcontenido.toUpperCase().indexOf("NACIMIENTO");
        	int indexAntiguedad = newcontenido.toUpperCase().indexOf("ANTIG");
        	
        	//para ubicar si la fecha de nacimiento viene antes de la fecha de antiguedad
        	if(indexNacimiento>0 && indexNacimiento<indexAntiguedad) {
        		indexNacimiento = 2;
        		indexAntiguedad = 3;
        	}else {
        		indexAntiguedad = 2;
        		indexNacimiento = 3;
        	}
        	
        	for (int i = 0; i <arrContenido.length; i++) {
        		EstructuraAseguradosModel asegurado = new EstructuraAseguradosModel();    
        		lineaTexto = arrContenido[i];

        		if(lineaTexto.split("-").length > 3) {
        			String[] arrDettalleAsegurado = lineaTexto.split("###");     
            		asegurado.setNombre(arrDettalleAsegurado[0]);
            		asegurado.setAntiguedad(fn.formatDateMonthCadena(arrDettalleAsegurado[indexAntiguedad]));
            		asegurado.setNacimiento(fn.formatDateMonthCadena(arrDettalleAsegurado[indexNacimiento]));
            		asegurado.setParentesco( fn.parentesco(arrDettalleAsegurado[4]));
            		asegurado.setSexo(fn.sexo( arrDettalleAsegurado[6]) ? 1:0);
            		asegurados.add(asegurado);
        		}
        	} 
        	modelo.setAsegurados(asegurados);
        }
	}
	
	private void obtenerDetalleSeguro() {
		int inicio = contenido.indexOf("Detalle del Seguro");
		int fin = contenido.indexOf("Coberturas###Alcance");

		if (inicio > 0 && fin > 0 && inicio < fin) {
			newcontenido = contenido.substring(inicio, fin).replace("\r", "").replace("@@@", "");
			String[] arrContenido = newcontenido.split("\n");
			String lineaTexto = "";
			String[] arrDetalle;

			for (int i = 0; i < arrContenido.length; i++) {
				lineaTexto = arrContenido[i];
				if (lineaTexto.contains("Suma Asegurada") && lineaTexto.contains("Deducible")
						&& lineaTexto.contains("Coaseguro")) {
					arrDetalle = arrContenido[i + 2].split("###");
					modelo.setSa(arrDetalle[1]);
					modelo.setDeducible(arrDetalle[2]);
					modelo.setCoaseguro(arrDetalle[3]);
				}
				if (lineaTexto.contains("Tope Coaseguro")) {
					modelo.setCoaseguroTope(lineaTexto.split("###")[1]);
				}

			}
		}
	}
	
	private void leerVigencia(String texto) {
		String[] arrContenido = texto.split("###");
		if (arrContenido[0].contains("-") && modelo.getVigenciaDe().length() == 0) {
			modelo.setVigenciaDe(fn.formatDateMonthCadena(arrContenido[0]));
			if(arrContenido[2].contains("-")) {
				if(arrContenido[2].split("-").length == 3 ) {
					modelo.setVigenciaA(fn.formatDateMonthCadena(arrContenido[2]));
				}
			}
		} else if (arrContenido[2].contains("-")) {
			String b = arrContenido[2].split("-")[2].substring(4, 6) + "-" + arrContenido[2].split("-")[3] + "-"
					+ arrContenido[2].split("-")[4];
			modelo.setVigenciaDe(fn.formatDateMonthCadena(b));
			modelo.setVigenciaA(fn.formatDateMonthCadena(arrContenido[4]));
		}
		
	}
	
	private void obtenerPrimas() {
		int inicio = contenido.indexOf("Prima Neta");
		int fin = contenido.indexOf("En cumplimiento ");

		if (inicio > 0 && fin > 0 && inicio < fin) {
			newcontenido = contenido.substring(inicio, fin).replace("\r", "").replace("@@@", "");
			String[] arrContenido = newcontenido.split("\n");
			String lineaTexto = "";
			String[] arrDetalle;

			for (int i = 0; i < arrContenido.length; i++) {
				lineaTexto = arrContenido[i];
				arrDetalle = lineaTexto.split("###");
				if (lineaTexto.contains("Prima Neta")) {
					modelo.setPrimaneta(fn.castBigDecimal(fn.castDouble(arrDetalle[arrDetalle.length - 1])));
				}
				if (lineaTexto.contains("Recargo")) {
					modelo.setRecargo(fn.castBigDecimal(fn.castDouble(arrDetalle[arrDetalle.length - 1])));
				}

				if (lineaTexto.contains("Expedición")) {
					modelo.setDerecho(fn.castBigDecimal(fn.castDouble(arrDetalle[arrDetalle.length - 1])));
				}
				if (lineaTexto.contains("I.V.A")) {
					modelo.setIva(fn.castBigDecimal(fn.castDouble(arrDetalle[arrDetalle.length - 1])));
				}
				if (lineaTexto.contains("Prima Total")) {
					modelo.setPrimaTotal(fn.castBigDecimal(fn.castDouble(arrDetalle[arrDetalle.length - 1])));
				}
			}
		}
	}

	
	private void obtenerCoberturas() {
		int inicio = contenido.indexOf("Coberturas###Alcance");
		int fin = contenido.indexOf("Esta póliza queda");

		if (inicio > 0 && fin > 0 && inicio < fin) {
			List<EstructuraCoberturasModel> coberturas = new ArrayList<>();
			newcontenido = contenido.substring(inicio, fin).replace("\r", "").replace("@@@", "");
			String[] arrContenido = newcontenido.split("\n");
			String[] arrCoberturaDetalle;
			
			for (int i = 0; i < arrContenido.length; i++) {
				EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();
				if (!arrContenido[i].contains("Coberturas")) {
					arrCoberturaDetalle = arrContenido[i].split("###");
					int sp = arrCoberturaDetalle.length;
					cobertura.setNombre(arrCoberturaDetalle[0]);
					cobertura.setDeducible(arrCoberturaDetalle[1]);
					if (sp > 2) {
						cobertura.setSa(arrCoberturaDetalle[2]);
					}
					coberturas.add(cobertura);
				}
			}
			modelo.setCoberturas(coberturas);
		}
	}

}
