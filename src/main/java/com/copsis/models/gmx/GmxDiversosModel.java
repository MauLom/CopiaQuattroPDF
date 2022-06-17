package com.copsis.models.gmx;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraCoberturasModel;
import com.copsis.models.EstructuraJsonModel;
import com.copsis.models.EstructuraRecibosModel;

public class GmxDiversosModel {

	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
	private String contenido = "";

	public GmxDiversosModel(String contenido) {
		this.contenido = contenido;
	}

	public EstructuraJsonModel procesar() {
		String newcontenido = "";
		int inicio = 0;
		int fin = 0;
		int  fechavenci =0;
		contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());
		try {
			// tipo
			modelo.setTipo(7);

			// cia
			modelo.setCia(17);

			inicio = contenido.indexOf("POLIZA NUEVA");
			fin = contenido.indexOf("Descripción de Bienes");
			if (inicio > 0 && fin > 0 && inicio < fin) {

				newcontenido = contenido.substring(inicio, fin).replace("\r", "").replace("@@@", "").trim();
				for (int i = 0; i < newcontenido.split("\n").length; i++) {

			
					if (newcontenido.split("\n")[i].contains("PÓLIZA") && newcontenido.split("\n")[i].contains("RENOVACIÓN")) {
						modelo.setPoliza(newcontenido.split("\n")[i + 1].split("###")[3].trim());
					}
					
					if (newcontenido.split("\n")[i].contains("Contratante") && newcontenido.split("\n")[i].contains("RFC")) {
						modelo.setCteNombre(newcontenido.split("\n")[i].split("Contratante")[1].split("RFC")[0].replace("###", "").trim());
					  modelo.setRfc(newcontenido.split("\n")[i].split("RFC")[1].replace("###", "").trim());
					}
					
					if (newcontenido.split("\n")[i].contains("Contratante") && modelo.getCteNombre().length() == 0) {
						modelo.setCteNombre(newcontenido.split("\n")[i].split("Contratante")[1].replace("###", " ").trim());
					}

					if (newcontenido.split("\n")[i].contains("Domicilio") && newcontenido.split("\n")[i].contains("Fecha de Nacimiento")) {
						modelo.setCteDireccion(newcontenido.split("\n")[i].split("Domicilio")[1].split("Fecha de Nacimiento")[0].replace("###", "").trim());
					}
					
					if (newcontenido.split("\n")[i].contains("C.P.") ) {
						modelo.setCp(obtenerCP(newcontenido.split("\n")[i].split("###")[1])); 
					}
					
					if (newcontenido.split("\n")[i].contains("Agente") && newcontenido.split("\n")[i].contains("-") ) {
						modelo.setCveAgente(newcontenido.split("\n")[i].split("Agente")[1].split("-")[0].replace("###", ""));
						modelo.setAgente(newcontenido.split("\n")[i].split("Agente")[1].split("-")[1].replace("###", ""));
					
					}
					
					
					if (newcontenido.split("\n")[i].contains("Desde") && newcontenido.split("\n")[i].contains("horas de la ")) {
						modelo.setVigenciaDe(fn.formatDateMonthCadena(newcontenido.split("\n")[i].split("Desde")[1].split("horas")[0].replace("12:00 ", "").replace("###", "").trim().replace(" " , "-")));
					}
					
					
					if(modelo.getVigenciaDe().length() == 0 && newcontenido.split("\n")[i].contains("Desde") && newcontenido.split("\n")[i+1].contains("horas de la")) {
		
					modelo.setVigenciaDe(fn.formatDateMonthCadena(newcontenido.split("\n")[i+1].split("horas")[0].replace("12:00 ", "").replace("###", "").trim().replace(" " , "-")));
					}
		
					if (newcontenido.split("\n")[i].contains("Hasta") && newcontenido.split("\n")[i].contains("horas de la ")) {
						modelo.setVigenciaA(fn.formatDateMonthCadena(newcontenido.split("\n")[i].split("Hasta")[1].split("horas")[0].replace("12:00 ", "").replace("###", "").trim().replace(" " , "-")));

					}
					
					if (modelo.getVigenciaA().length() == 0 && newcontenido.split("\n")[i].contains("Hasta") && newcontenido.split("\n")[i+1].contains("horas de la ")) {
						modelo.setVigenciaA(fn.formatDateMonthCadena(newcontenido.split("\n")[i+1].split("horas")[0].replace("12:00 ", "").replace("###", "").trim().replace(" " , "-")));

					}
					if (newcontenido.split("\n")[i].contains("Domicilio") && newcontenido.split("\n")[i].contains("Fecha de Nacimiento") && modelo.getCteDireccion().length() == 0 ) {
						modelo.setCteDireccion((newcontenido.split("\n")[i].split("Domicilio")[1].split("Fecha de Nacimiento")[0].replace("###", "").trim()
								+"  " + newcontenido.split("\n")[i+1]).trim()	
								);
					}


					if(newcontenido.split("\n")[i].contains("Agente") && newcontenido.split("\n")[i].contains("-")){
						modelo.setCveAgente(newcontenido.split("\n")[i].split("Agente")[1].split("-")[0].replace("###", "").trim());
						modelo.setAgente(newcontenido.split("\n")[i].split("Agente")[1].split("-")[1].replace("###", "").trim());
					}

					if (newcontenido.split("\n")[i].contains("Fecha Emisión") && newcontenido.split("\n")[i].contains("Vigencia") &&  newcontenido.split("\n")[i].contains("Días")){
						 modelo.setFechaEmision(fn.formatDateMonthCadena(newcontenido.split("\n")[i].split("Emisión")[1].replace("###", "").trim().replace(" " , "-")));

						 fechavenci =fn.castInteger(newcontenido.split("\n")[i].split("Vigencia")[1].split("Fecha Emisión ")[0].replace("###", "").replace("Días", "").trim());
						}
						

					if (newcontenido.split("\n")[i].contains("Moneda") ) {
						modelo.setMoneda(fn.buscaMonedaEnTexto((newcontenido.split("\n")[i]))); 
					}
					
					if (newcontenido.split("\n")[i].contains("Forma de Pago") ) {
						modelo.setFormaPago(fn.formaPagoSring((newcontenido.split("\n")[i]))); 
					}
					

				}

			}
			if(modelo.getFechaEmision().length() == 0) {
				modelo.setFechaEmision(modelo.getVigenciaDe());
			}
//			if(modelo.getVigenciaDe().length() >  0  && modelo.getVigenciaDe().length() >  0  ){
//				modelo.setFechaVence(fn.calcfechavence(modelo.getVigenciaDe(), fechavenci));
//			}
//			

			inicio = contenido.indexOf("Prima Neta");
			fin = contenido.indexOf("En cumplimiento ");
			if(fin == -1) {
				fin = contenido.indexOf("Artículo. ###25");
			}
			
			if (inicio > 0 && fin > 0 && inicio < fin) {
				newcontenido = "";
				newcontenido = contenido.substring(inicio, fin).replace("\r", "").replace("@@@", "").trim();
				for (int i = 0; i < newcontenido.split("\n").length; i++) {
					if(newcontenido.split("\n")[i].contains("Prima Neta") && newcontenido.split("\n")[i].contains("Derecho") 						
					&& newcontenido.split("\n")[i+1].length() > 30) {
						int sp = newcontenido.split("\n")[i].split("###")[1].length();
		
						if(sp == 8) {
							modelo.setPrimaneta(fn.castBigDecimal(fn.cleanString(
									newcontenido.split("\n")[i+1].split("###")[1].trim())));
							modelo.setRecargo(fn.castBigDecimal(fn.cleanString(
									newcontenido.split("\n")[i+1].split("###")[2].trim())));														
							modelo.setDerecho(fn.castBigDecimal(fn.cleanString(
									newcontenido.split("\n")[i+1].split("###")[3].trim())));												
							modelo.setIva(fn.castBigDecimal(fn.cleanString(
									newcontenido.split("\n")[i+1].split("###")[4].trim())));							
							modelo.setPrimaTotal(fn.castBigDecimal(fn.cleanString(
									newcontenido.split("\n")[i+1].split("###")[5].trim())));
						}
					}
				}
			}
			
		
			if(modelo.getFormaPago() == 1 &&  fn.diferenciaDias(modelo.getVigenciaDe(), modelo.getVigenciaA()) <30) {
				List<EstructuraRecibosModel> recibos = new ArrayList<>();
				EstructuraRecibosModel recibo = new EstructuraRecibosModel();
				recibo.setReciboId("");
				recibo.setSerie("1/1");
				recibo.setVigenciaDe(modelo.getVigenciaDe());
				recibo.setVigenciaA(modelo.getVigenciaA());				
			    recibo.setVencimiento(modelo.getVigenciaDe());				
				recibo.setPrimaneta(fn.castBigDecimal(modelo.getPrimaneta(), 2));
				recibo.setDerecho(fn.castBigDecimal(modelo.getDerecho(), 2));
				recibo.setRecargo(fn.castBigDecimal(modelo.getRecargo(), 2));
				recibo.setIva(fn.castBigDecimal(modelo.getDerecho(), 2));

				recibo.setPrimaTotal(fn.castBigDecimal(modelo.getPrimaTotal(), 2));
				recibo.setAjusteUno(fn.castBigDecimal(modelo.getAjusteUno(), 2));
				recibo.setAjusteDos(fn.castBigDecimal(modelo.getAjusteDos(), 2));
				recibo.setCargoExtra(fn.castBigDecimal(modelo.getCargoExtra(), 2));
				recibos.add(recibo);
				modelo.setRecibos(recibos);
			}
			
			

			
			inicio = contenido.indexOf("Riesgos Cubiertos ");
			fin = contenido.indexOf("Para Mercancías usada");
			

			if (inicio > 0 && fin > 0 && inicio < fin) {
				List<EstructuraCoberturasModel> coberturas = new ArrayList<>();
				newcontenido = contenido.substring(inicio, fin).replace("\r", "").replace("@@@", "").replace("","").replace("", "").trim();
				for (String x : newcontenido.split("\n")) {
					EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();
				  if(x.length() > 4 && x.split("###").length > 1 && !x.contains("Riesgos Cubiertos")  && !x.contains("Riesgos Ordinarios")
						  && !x.contains("Embarques terrestres") && !x.contains("Colisión o volcadura del")
						  && !x.contains("puentes o hundimiento ")
						  && !x.contains("tránsito terrestre")
						  && !x.contains("ESPECIFICACIÓN")
						  && !x.contains("Averías particulares")
						  && !x.contains("Para mercancías nuevas")
						  ) {
					  cobertura.setNombre(x.replace("###", ""));
					  coberturas.add(cobertura);
					  System.out.println("----> " + x);
				  }	
				}
				
				modelo.setCoberturas(coberturas);
			}
			

			return modelo;
		} catch (Exception ex) {
			modelo.setError(GmxDiversosModel.this.getClass().getTypeName() + " - catch:" + ex.getMessage() + " | "
					+ ex.getCause());
			return modelo;
		}
	}
	
	public String obtenerCP(String cp) {

		char[] ch = cp.toCharArray();
		StringBuilder strbuild = new StringBuilder();
		for(char c : ch){
			if(Character.isDigit(c)){
			strbuild.append(c);
			}
		}
		if(strbuild.length() == 5 || strbuild.length() == 4) {
			return strbuild.toString();
		}else {
			strbuild = new StringBuilder();
		}
		
		return strbuild.toString();
	}
}
