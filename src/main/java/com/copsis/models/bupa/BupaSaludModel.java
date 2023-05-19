package com.copsis.models.bupa;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraAseguradosModel;
import com.copsis.models.EstructuraCoberturasModel;
import com.copsis.models.EstructuraJsonModel;

import java.util.ArrayList;
import java.util.List;

public class BupaSaludModel {
	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();

	public EstructuraJsonModel procesar(String contenido,String conteniext,String recibo) {
		int inicio = 0;
		int fin = 0;
		StringBuilder newcontenido = new StringBuilder();
		contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());
		try {
			modelo.setTipo(3);
			modelo.setCia(87);
	
		
			inicio = contenido.indexOf("Contratante");
			fin = contenido.indexOf("Advertencia");
			newcontenido.append( fn.extracted(inicio, fin, contenido).replace("día-mes-año: ", ""));
			List<EstructuraAseguradosModel> asegurados = new ArrayList<>();
			EstructuraAseguradosModel asegurado = new EstructuraAseguradosModel();
			List<EstructuraCoberturasModel> coberturas = new ArrayList<>();
			EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();
			for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {	
			
				if(newcontenido.toString().split("\n")[i].contains("Contratante") && newcontenido.toString().split("\n")[i+1].contains("Dirección")) {				
					
					modelo.setCteNombre(newcontenido.toString().split("\n")[i].split("Contratante")[1].replace("###", "").trim());
				}
				if(newcontenido.toString().split("\n")[i].contains("Dirección")) {
					modelo.setCteDireccion(newcontenido.toString().split("\n")[i].split("Dirección")[1].replace("###", "").trim());					
					modelo.setCp(fn.obtenerCPRegex2(newcontenido.toString().split("\n")[i+1]));
					if(modelo.getCp().length() == 0) {
					    modelo.setCp(fn.obtenerCPRegex2(newcontenido.toString().split("\n")[i+2]));
					}
				}
			
				if(newcontenido.toString().split("\n")[i].contains("Número de Póliza")) {
					modelo.setPoliza(newcontenido.toString().split("\n")[i].split("Póliza")[1].replace("###", "").trim());
				}
				if(newcontenido.toString().split("\n")[i].contains("Número de")&& newcontenido.toString().split("\n")[i+1].contains("Póliza") ) {
					modelo.setPoliza(newcontenido.toString().split("\n")[i].split("Número de")[1].replace("###", "").trim());
				}
				if(newcontenido.toString().split("\n")[i].contains("Vigencia")  && newcontenido.toString().split("\n")[i].contains("Vigencia")) {
					modelo.setVigenciaDe(fn.formatDateMonthCadena(fn.obtenVigePoliza(newcontenido.toString().split("\n")[i]).get(0)));
				}
				if(newcontenido.toString().split("\n")[i].contains("Hasta")) {
					modelo.setVigenciaA(fn.formatDateMonthCadena(fn.obtenVigePoliza(newcontenido.toString().split("\n")[i]).get(0)));
				}
				if( newcontenido.toString().split("\n")[i].contains("Nacimiento") && newcontenido.toString().split("\n")[i].contains("Prima")) {
					  List<String> valores = fn.obtenerListNumeros(newcontenido.toString().split("\n")[i+1]);
					  modelo.setPrimaneta(fn.castBigDecimal(fn.castDouble(valores.get(0))));
				}
				
				if(newcontenido.toString().split("\n")[i].contains("Derecho de Póliza")) {
					  List<String> valores = fn.obtenerListNumeros(newcontenido.toString().split("\n")[i]);
					  modelo.setDerecho(fn.castBigDecimal(fn.castDouble(valores.get(0))));
				}
				if(newcontenido.toString().split("\n")[i].contains("Fraccionado")) {
					  List<String> valores = fn.obtenerListNumeros(newcontenido.toString().split("\n")[i]);
				if(valores.size() > 0) {
					  modelo.setRecargo(fn.castBigDecimal(fn.castDouble(valores.get(0))));
				}
				}
				if(newcontenido.toString().split("\n")[i].contains("IVA")) {
					  List<String> valores = fn.obtenerListNumeros(newcontenido.toString().split("\n")[i]);
					  modelo.setIva(fn.castBigDecimal(fn.castDouble(valores.get(0))));
				}
				if(newcontenido.toString().split("\n")[i].contains("Total")) {
					  List<String> valores = fn.obtenerListNumeros(newcontenido.toString().split("\n")[i]);
					  modelo.setPrimaTotal(fn.castBigDecimal(fn.castDouble(valores.get(0))));
				}
				
				if(newcontenido.toString().split("\n")[i].contains("Asegurado Principal") || newcontenido.toString().split("\n")[i].contains("Asegurado Titular")) {				
				    
				    if(fn.obtenVigePoliza(newcontenido.toString().split("\n")[i].split("###")[1]).get(0).length() > 0) {
					asegurado.setNombre(newcontenido.toString().split("\n")[i].split("###")[1].split( fn.obtenVigePoliza(newcontenido.toString().split("\n")[i].split("###")[1]).get(0) )[0].trim());
					asegurado.setNacimiento(fn.formatDateMonthCadena(fn.obtenVigePoliza(newcontenido.toString().split("\n")[i].split("###")[1]).get(0)));
				    asegurado.setParentesco(fn.parentesco(newcontenido.toString().split("\n")[i+1] ));
					}else {
						asegurado.setNombre(newcontenido.toString().split("\n")[i].split("###")[1]);
						asegurado.setParentesco(fn.parentesco(newcontenido.toString().split("\n")[i+1] ));
					}	
					  
				}
				
				if(newcontenido.toString().split("\n")[i].contains("Deducibles") && newcontenido.toString().split("\n")[i].contains("Dentro")) {
					cobertura.setDeducible(newcontenido.toString().split("\n")[i].split("Dentro")[1].replace("###", ""));
				}
				if(newcontenido.toString().split("\n")[i].contains("Suma Asegurada") ) {
					cobertura.setSa(newcontenido.toString().split("\n")[i+1].substring(0, 30).split("###")[0].replace("###", ""));
				}
				
			}
			
			if(modelo.getVigenciaDe().length() > 0) {
				modelo.setFechaEmision(modelo.getVigenciaDe());
			}
			
			
			
			 asegurados.add(asegurado);
			modelo.setAsegurados(asegurados);
			
			coberturas.add(cobertura);
			modelo.setCoberturas(coberturas);
			
			
	
			inicio = conteniext.indexOf("Forma de Pago");
			fin = conteniext.indexOf("Vigencia###Prima Neta");
			if(fin == -1) {
			    fin = conteniext.indexOf("Hasta###Prima Neta");
			}
		

			newcontenido.append( fn.extracted(inicio, fin, conteniext));
			for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {			
				
				if(newcontenido.toString().split("\n")[i].contains("Forma de Pago")) {
				  modelo.setFormaPago( fn.formaPagoSring(newcontenido.toString().split("\n")[i+1] ));
				}
				if(newcontenido.toString().split("\n")[i].contains("Moneda")) {
					if(newcontenido.toString().split("\n")[i+1].contains("MXP")) {
						modelo.setMoneda(1);
					}
				}
					
			}
			if(modelo.getFormaPago() == 0) {		
				inicio = recibo.indexOf("Forma de Pago");
				fin = recibo.indexOf("Prima Neta");
				newcontenido.append(fn.extracted(inicio, fin, recibo));
				for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
					if (newcontenido.toString().split("\n")[i].contains("Forma de Pago")) {
						modelo.setFormaPago(fn.formaPagoSring(newcontenido.toString().split("\n")[i + 1]));
					}
					if (newcontenido.toString().split("\n")[i].contains("Moneda")) {
						if (newcontenido.toString().split("\n")[i + 1].contains("MXP")) {
							modelo.setMoneda(1);
						}
					}
				}
			}
			
	
			
			return modelo;
		} catch (Exception ex) {
			modelo.setError(BupaSaludModel.this.getClass().getTypeName() + " | " + ex.getMessage() + " | " + ex.getCause());
			return modelo;
		}
	}
}
