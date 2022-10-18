package com.copsis.models.chubb.autos;

import java.util.ArrayList;
import java.util.List;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraCoberturasModel;
import com.copsis.models.EstructuraJsonModel;

public class ChubbAutosBModel {
	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
	
	public EstructuraJsonModel procesar(String contenido) {
		int inicio = 0;
		int fin = 0;
		StringBuilder newcontenido = new StringBuilder();	
		contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());
		
		try {
			modelo.setTipo(1);
			modelo.setCia(1);

			
			
			inicio = contenido.indexOf("Policy No.");
			fin = contenido.indexOf("Datos del asegurado");			
			newcontenido.append( fn.extracted(inicio, fin, contenido));
			for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
			
				if(newcontenido.toString().split("\n")[i].contains("Policy No.") && newcontenido.toString().split("\n")[i].contains("Endoso")) {
					modelo.setPoliza(newcontenido.toString().split("\n")[i].split("Policy No.")[1].split("Endoso")[0].replace("###", "").trim());
		
				}
				if(newcontenido.toString().split("\n")[i].contains("Vigencia de la Póliza") && newcontenido.toString().split("\n")[i].contains("Días")) {
				    modelo.setVigenciaDe(fn.obtenVigePolizaUS(newcontenido.toString().split("\n")[i]).get(0));
				    modelo.setVigenciaA(fn.obtenVigePolizaUS(newcontenido.toString().split("\n")[i]).get(1));
					
				}
			}
			
			
			inicio = contenido.indexOf("Datos del asegurado");
			fin = contenido.indexOf("Descripción del Vehículo");
			newcontenido = new StringBuilder();
			newcontenido.append( fn.extracted(inicio, fin, contenido).replace("día-mes-año: ", ""));			
			for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
				
				if(newcontenido.toString().split("\n")[i].contains("Name:") && newcontenido.toString().split("\n")[i].contains("Fecha")) {
					modelo.setCteNombre(newcontenido.toString().split("\n")[i].split("Name:")[1].split("Fecha")[0].replace("###", "").trim());
				}
				if(newcontenido.toString().split("\n")[i].contains("Address:") && newcontenido.toString().split("\n")[i].contains("Teléfono")) {
					modelo.setCteDireccion(newcontenido.toString().split("\n")[i].split("Address:")[1].split("Teléfono")[0].replace("###", "").trim());
				}
				if(newcontenido.toString().split("\n")[i].contains("Code:") && newcontenido.toString().split("\n")[i].contains("Ciudad")) {
					modelo.setCp(newcontenido.toString().split("\n")[i].split("Code:")[1].split("Ciudad")[0].replace("###", "").trim());
				}
				if(newcontenido.toString().split("\n")[i].contains("emisión") && newcontenido.toString().split("\n")[i].contains("date")  
						&& newcontenido.toString().split("\n")[i].contains("Clave") && newcontenido.toString().split("\n")[i].contains("Code")) {
				 modelo.setFechaEmision(fn.obtenVigePolizaUS(newcontenido.toString().split("\n")[i].split("date:")[1]).get(0));
				 modelo.setCveAgente(newcontenido.toString().split("\n")[i].split("Code:")[1].replace("###", "").trim());
				}
				
				if(newcontenido.toString().split("\n")[i].contains("pago") && newcontenido.toString().split("\n")[i].contains("Payment")) {
					modelo.setFormaPago(1);
					modelo.setMoneda(1);
				}
				
			}
			if(modelo.getFormaPago() == 1 && (fn.diferenciaDias(modelo.getVigenciaDe(), modelo.getVigenciaA()) <30)) {
				modelo.setVigenciaA(fn.calcvigenciaA(modelo.getVigenciaDe(), 12));
			}
			
			inicio = contenido.indexOf("Descripción del Vehículo");
			fin = contenido.indexOf("Otros Bienes Asegurados");
			newcontenido = new StringBuilder();
			newcontenido.append( fn.extracted(inicio, fin, contenido));			
			for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
				
				if(newcontenido.toString().split("\n")[i].contains("Year:") && newcontenido.toString().split("\n")[i].contains("Marca") ) {
					
					modelo.setModelo(fn.castInteger(newcontenido.toString().split("\n")[i].split("Year:")[1].split("Marca")[0].replace("###", "").trim()));
				}
				if( newcontenido.toString().split("\n")[i].contains("Make:") &&  newcontenido.toString().split("\n")[i].contains("Model:")) {
				
					modelo.setMarca(newcontenido.toString().split("\n")[i].split("Make:")[1].split("Modelo")[0].replace("###", "").trim());
					modelo.setDescripcion(newcontenido.toString().split("\n")[i].split("Model:")[1].replace("###", "").trim());
				}
				if( newcontenido.toString().split("\n")[i].contains("VIN:") &&  newcontenido.toString().split("\n")[i].contains("Placas")
				  && newcontenido.toString().split("\n")[i].contains("Plates:") &&  newcontenido.toString().split("\n")[i].contains("Uso") ) {
					modelo.setSerie(newcontenido.toString().split("\n")[i].split("VIN:")[1].split("Placas")[0].replace("###", "").trim());
					modelo.setPlacas(newcontenido.toString().split("\n")[i].split("Plates:")[1].split("Uso")[0].replace("###", "").trim());
				}
			}
			
			
			
			inicio = contenido.indexOf("Coberturas");
			fin = contenido.indexOf("Prima Neta");
			newcontenido = new StringBuilder();
			newcontenido.append( fn.extracted(inicio, fin, contenido));	
			List<EstructuraCoberturasModel> coberturas = new ArrayList<>();
			for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
				EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();
				if(!newcontenido.toString().split("\n")[i].contains("Coberturas") && !newcontenido.toString().split("\n")[i].contains("Suma Asegurada")) {					
					int sp = newcontenido.toString().split("\n")[i].split("###").length;
					switch (sp) {
					case 3:
						cobertura.setNombre(newcontenido.toString().split("\n")[i].split("###")[0]);
						cobertura.setDeducible(newcontenido.toString().split("\n")[i].split("###")[1]);
						cobertura.setSa(newcontenido.toString().split("\n")[i].split("###")[2]);
						coberturas.add(cobertura);
						break;	

					default:
						break;
					}
				}
			}
			modelo.setCoberturas(coberturas);
			
			List<String> valores = new ArrayList<String>();
					inicio = contenido.indexOf("Prima Neta");
			fin = contenido.indexOf("Números de Contacto");
			newcontenido = new StringBuilder();
			newcontenido.append( fn.extracted(inicio, fin, contenido));
			for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {				
				if(newcontenido.toString().split("\n")[i].contains("Prima Neta")) {
					valores = fn.obtenerListNumeros(newcontenido.toString().split("\n")[i]);
					 modelo.setPrimaneta(fn.castBigDecimal(fn.castDouble(valores.get(0))));
				}
				if(newcontenido.toString().split("\n")[i].contains("expedición")) {
					valores = fn.obtenerListNumeros(newcontenido.toString().split("\n")[i]);
					 modelo.setDerecho(fn.castBigDecimal(fn.castDouble(valores.get(0))));
				}
				if(newcontenido.toString().split("\n")[i].contains("I.V.A.")) {
					valores = fn.obtenerListNumeros(newcontenido.toString().split("\n")[i]);
					 modelo.setIva(fn.castBigDecimal(fn.castDouble(valores.get(0))));
				}
				if(newcontenido.toString().split("\n")[i].contains("Prima Total")) {
					valores = fn.obtenerListNumeros(newcontenido.toString().split("\n")[i]);
					 modelo.setPrimaTotal(fn.castBigDecimal(fn.castDouble(valores.get(0))));
				}
			}
			
			modelo.setPolizaGuion(modelo.getPoliza());
			
			return modelo;
		} catch (Exception ex) {
			modelo.setError(ChubbAutosBModel.this.getClass().getTypeName() + " | " + ex.getMessage() + " | " + ex.getCause());
			return modelo;
		}
	}
}
