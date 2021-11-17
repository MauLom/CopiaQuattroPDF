package com.copsis.models.aba;

import java.util.ArrayList;
import java.util.List;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraCoberturasModel;
import com.copsis.models.EstructuraJsonModel;

public class AbaAutosModel {
	
	// Clases
	DataToolsModel fn = new DataToolsModel();
	EstructuraJsonModel modelo = new EstructuraJsonModel();

	// Variables
	private String contenido;

	// constructor
	public AbaAutosModel(String contenido ) {
		this.contenido = contenido;

	}
	
	public EstructuraJsonModel procesar() {
		
		int inicio = 0;
		int fin = 0;
		String newcontenido = "";
		
		contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());
		contenido = contenido.replace("vehículo*:", "vehículo###:");
		try {
			//tipo
			modelo.setTipo(1);
			
			//cia
			modelo.setCia(1);

			//Datos d ela poliza
			inicio = contenido.indexOf("PÓLIZA DE SEGURO");
			fin = contenido.indexOf("Coberturas amparadas");
			
			if(inicio > 0 && fin > 0 && inicio < fin ) {
				newcontenido = contenido.substring(inicio,fin);
				for (int i = 0; i < newcontenido.split("\n").length; i++) {	
					if(newcontenido.split("\n")[i].contains("Póliza") && newcontenido.split("\n")[i].contains("Vigencia")) {
						modelo.setPoliza( newcontenido.split("\n")[i].split("Póliza")[1].split("Vigencia")[0].replace("###", "").trim());
						modelo.setVigenciaDe(fn.formatDateMonthCadena(newcontenido.split("\n")[i].split("Vigencia:")[1].split("horas")[0].replace("###", "").replace("12:00", "").replace("Del", "").trim()));
            			modelo.setVigenciaA(fn.formatDateMonthCadena(newcontenido.split("\n")[i].split("horas")[1].split("horas")[0].replace("###", "").replace("12:00", "").replace("al", "").trim()));
					}
					if(newcontenido.split("\n")[i].contains("Asegurado") && newcontenido.split("\n")[i].contains("Endoso:")) {
						modelo.setEndoso( newcontenido.split("\n")[i].split("Endoso:")[1].replace(":", "").replace("\r","").replace("###", "").replace("", "".trim()));						
					}
					if(newcontenido.split("\n")[i].contains("Inciso")) {
						modelo.setInciso(Integer.parseInt(newcontenido.split("\n")[i].split("Inciso")[1].replace("###", "").replace("\r", "").trim()));
					}
					if(newcontenido.split("\n")[i].contains("Paquete:")) {
						modelo.setPlan(newcontenido.split("\n")[i].split("Paquete:")[1].replace("###", "").replace("\r", "").trim());
					}
					if(newcontenido.split("\n")[i].contains("Asegurado:")) {
						modelo.setCteNombre(newcontenido.split("\n")[i].split("Asegurado:")[1].replace("###", "").replace("\r", "").trim());
					}
					if(newcontenido.split("\n")[i].contains("C.P:")) {
						modelo.setCp(newcontenido.split("\n")[i].split("C.P:")[1].replace("###", "").replace("\r", "").trim());
					}
					if(newcontenido.split("\n")[i].contains("R.F.C:")) {
						modelo.setRfc(newcontenido.split("\n")[i].split("R.F.C:")[1].replace("###", "").replace("\r", "").trim());
					}
					if(newcontenido.split("\n")[i].contains("Domicilio:")) {
						modelo.setCteNombre((newcontenido.split("\n")[i].split("Domicilio:")[1].split("C.P:")[0] +" " + newcontenido.split("\n")[i+1].split("Teléfono:")[0] +" " + newcontenido.split("\n")[i+2].split("R.F.C:")[0] ).replace("@@@", "").replace("###", "").replace("\r", "").trim());
					}
					if(newcontenido.split("\n")[i].contains("Moneda:")  && newcontenido.split("\n")[i].contains("pago")) {
            			modelo.setMoneda(fn.moneda(newcontenido.split("\n")[i].split("Moneda:")[1].split("Forma")[0].replace("###", "").trim()));
            			modelo.setFormaPago(fn.formaPago(newcontenido.split("\n")[i].split("pago:")[1].split("###")[0].replace("\r", "").replace("###", "").trim()));	            		
            		}
					if(newcontenido.split("\n")[i].contains("agente:") && newcontenido.split("\n")[i].contains("- 0 -")) {
            			modelo.setCveAgente(newcontenido.split("\n")[i].split("agente:")[1].split("- 0 - ")[0].replace("\r", "").replace("###", "").trim());
            		    modelo.setAgente(newcontenido.split("\n")[i].split("- 0 - ")[1].replace("\r", "").replace("###", "").trim());
					}								
					if(newcontenido.split("\n")[i].contains("vehículo###:")) {
						modelo.setDescripcion(newcontenido.split("\n")[i].split("vehículo###:")[1].replace("###", "").replace("\r", "").trim());
					}
					if(newcontenido.split("\n")[i].contains("Modelo:") && newcontenido.split("\n")[i].contains("Serie:")) {
						modelo.setModelo(Integer.parseInt(newcontenido.split("\n")[i].split("Modelo:")[1].split("Serie")[0].replace("###", "").replace("\r", "").trim()));
						modelo.setSerie(newcontenido.split("\n")[i].split("Serie:")[1].replace("###", "").replace("\r", "").trim());
					}
					if(newcontenido.split("\n")[i].contains("Marca:") && newcontenido.split("\n")[i].contains("Capacidad")) {
						modelo.setMarca(newcontenido.split("\n")[i].split("Marca:")[1].split("Capacidad")[0].replace("###", "").replace("\r", "").trim());
					}
					
					if(newcontenido.split("\n")[i].contains("Clave vehicular:")) {
						modelo.setClave(newcontenido.split("\n")[i].split("Clave vehicular:")[1].split("Servicio")[0].replace("###", "").replace("\r", "").trim());
						modelo.setPlacas(newcontenido.split("\n")[i].split("Placas:")[1].replace("###", "").replace("\r", "").trim());
					}
				}				
			}
			
			
			//PRIMAS
			inicio = contenido.indexOf("Prima neta");
			fin = contenido.indexOf("Página 1");
			
			if(inicio > 0 && fin > 0 && inicio < fin ) {
				newcontenido = contenido.substring(inicio,fin);
				for (int i = 0; i < newcontenido.split("\n").length; i++) {	
					if(newcontenido.split("\n")[i].contains("Prima neta")) {
						 modelo.setPrimaneta(fn.castBigDecimal(fn.castDouble(newcontenido.split("\n")[i].split("Prima neta")[1].replace("###", "").replace("\r", "").trim())));
					}
					
					if(newcontenido.split("\n")[i].contains("fraccionado")) {
						 modelo.setRecargo(fn.castBigDecimal(fn.castDouble(newcontenido.split("\n")[i].split("fraccionado")[1].replace("###", "").replace("\r", "").trim())));
					}
					if(newcontenido.split("\n")[i].contains("expedición")) {
						 modelo.setDerecho(fn.castBigDecimal(fn.castDouble(newcontenido.split("\n")[i].split("expedición")[1].replace("###", "").replace("\r", "").trim())));
					}
					
					if(newcontenido.split("\n")[i].contains("I.V.A.")) {
						 modelo.setIva(fn.castBigDecimal(fn.castDouble(newcontenido.split("\n")[i].split("I.V.A.")[1].replace("###", "").replace("\r", "").trim())));
					}
					
					if(newcontenido.split("\n")[i].contains("total")) {
						 modelo.setPrimaTotal(fn.castBigDecimal(fn.castDouble(newcontenido.split("\n")[i].split("total")[1].replace("###", "").replace("\r", "").trim())));
					}
				}
			}
			
			

			//COBERTURAS
			List<EstructuraCoberturasModel> coberturas = new ArrayList<>();
			
			inicio = contenido.indexOf("Suma asegurada###Deducible###Prima") + 34;
			fin = contenido.indexOf("@@@Prima neta###");
		       newcontenido = contenido.substring(inicio, fin).replace("@@@", "");
			for(String dato:newcontenido.split("\n")) {
				if(dato.split("###").length >= 3) {
					EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();
					cobertura.setNombre(dato.split("###")[0].trim());
					cobertura.setSa(dato.split("###")[1].replace("D)", "").replace("A)", "").replace("B)", "").replace("C)", "").trim());
					cobertura.setDeducible(dato.split("###")[2].replace("D)", "").replace("A)", "").replace("B)", "").replace("C)", "").trim());
					 coberturas.add(cobertura);
				}
			}
			  modelo.setCoberturas(coberturas);
			

			
			return modelo;
		} catch (Exception ex) {
			modelo.setError(
					AbaAutosModel.this.getClass().getTypeName() + " | " + ex.getMessage() + " | " + ex.getCause());
			return modelo;
		}
	}

}
