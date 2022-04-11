package com.copsis.models.gnp.autos;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraCoberturasModel;
import com.copsis.models.EstructuraJsonModel;

public class GnpAutos3Model {
	
	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
	private String contenido = "";
	
	public GnpAutos3Model(String contenido) {
		this.contenido = contenido;
	}
	
	public EstructuraJsonModel procesar() {
		StringBuilder newcontenido = new StringBuilder();
		int inicio = 0;
		int fin = 0;
		
		contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());
		try {
			modelo.setTipo(1);
			modelo.setCia(18);
			

			inicio = contenido.indexOf("Policy No.");
			fin = contenido.indexOf("COVERAGE AND SERVICES PROVIDED");
			
			newcontenido.append( fn.extracted(inicio, fin, contenido));
			
			for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
				
				if(newcontenido.toString().split("\n")[i].contains("Policy No.")) {
					modelo.setPoliza( newcontenido.toString().split("\n")[i].split("Policy No.")[1].trim());
				}
				if(newcontenido.toString().split("\n")[i].contains("R.F.C.")) {
					modelo.setRfc( newcontenido.toString().split("\n")[i].split("R.F.C.")[1].trim());
				}

				if(newcontenido.toString().split("\n")[i].contains("Last Name")) {
					modelo.setCteNombre(newcontenido.toString().split("\n")[i+1].trim());
				}
				if(newcontenido.toString().split("\n")[i].contains("Address Street") && newcontenido.toString().split("\n")[i].contains("ZIP") ) {
					modelo.setCteDireccion(newcontenido.toString().split("\n")[i+1].split("###")[0]);
				}
				
				if(newcontenido.toString().split("\n")[i].contains("date") && newcontenido.toString().split("\n")[i].contains("From 12:00")) {
					modelo.setVigenciaDe(fn.formatDateMonthCadena(fn.obtenVigePoliza(newcontenido.toString().split("\n")[i]).get(0)));
				}
				
				if(newcontenido.toString().split("\n")[i].contains("date") && newcontenido.toString().split("\n")[i].contains("To 12:00")) {
					modelo.setVigenciaA(fn.formatDateMonthCadena(fn.obtenVigePoliza(newcontenido.toString().split("\n")[i]).get(0)));
				}
				
				if(newcontenido.toString().split("\n")[i].contains("Description") && newcontenido.toString().split("\n")[i].contains("License Plate") && newcontenido.toString().split("\n")[i+1].contains("Net Premium")) {
					modelo.setDescripcion(newcontenido.toString().split("\n")[i+1].split("###")[0]);
					modelo.setPlacas(newcontenido.toString().split("\n")[i+1].split("###")[1]);
					
					modelo.setPrimaneta(fn.castBigDecimal(fn.cleanString(
							newcontenido.toString().split("\n")[i+1].split("###")[3].trim().replace("USD", "").trim())));
				}
				
				if(newcontenido.toString().split("\n")[i].contains("Policy Fee")){
					modelo.setDerecho(fn.castBigDecimal(fn.cleanString(newcontenido.toString().split("\n")[i].split("###")[1].trim().replace("USD", "").trim())));
				}
				
				if(newcontenido.toString().split("\n")[i].contains("Model Year") && newcontenido.toString().split("\n")[i].contains("Vehicle Type") &&  newcontenido.toString().split("\n")[i].contains("Tax")){
				  modelo.setModelo(fn.castInteger(newcontenido.toString().split("\n")[i+1].split("###")[0]));
				  modelo.setSerie(newcontenido.toString().split("\n")[i+1].split("###")[2]);
				  modelo.setIva(fn.castBigDecimal(fn.cleanString(newcontenido.toString().split("\n")[i].split("Tax")[1].trim().replace("USD", "").replace("###", "").trim())));
				}

				if(newcontenido.toString().split("\n")[i].contains("Amount to pay")){
					modelo.setPrimaTotal(fn.castBigDecimal(fn.cleanString(newcontenido.toString().split("\n")[i].split("Amount to pay")[1].replace("###", "").replace("USD", "").trim())));
				}

			}

			modelo.setMoneda(2);
			modelo.setFormaPago(1);
			if(modelo.getVigenciaDe().length() >  0) {
				modelo.setFechaEmision(modelo.getVigenciaDe());
			}
			
			inicio = contenido.indexOf("COBERTURAS Y SERVICIOS AMPARADOS");
			fin = contenido.indexOf("Observaciones");
			newcontenido = new StringBuilder();
			newcontenido.append( fn.extracted(inicio, fin, contenido));
			List<EstructuraCoberturasModel> coberturas = new ArrayList<>();
			for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
				EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();
				if(!newcontenido.toString().split("\n")[i].contains("Coberturas") && !newcontenido.toString().split("\n")[i].contains("Sum Insured")
				 && !newcontenido.toString().split("\n")[i].contains("Endoso de extensió")) {				
					switch (newcontenido.toString().split("\n")[i].split("###").length) {
					case 2:
						cobertura.setNombre(newcontenido.toString().split("\n")[i].split("###")[0]);
						cobertura.setSa(newcontenido.toString().split("\n")[i].split("###")[1]);
						coberturas.add(cobertura);
						break;
					case 3:
						cobertura.setNombre(newcontenido.toString().split("\n")[i].split("###")[0]);
						cobertura.setSa(newcontenido.toString().split("\n")[i].split("###")[1]);
						cobertura.setSa(newcontenido.toString().split("\n")[i].split("###")[2]);
						coberturas.add(cobertura);
						break;

					default:
						break;
					}
					
				}
			}			
			modelo.setCoberturas(coberturas);
			inicio = contenido.indexOf("Clave del Agente");
			if(inicio > 1 &&  contenido.split("Clave del Agente")[1].split("\n").length > 0) {				
				modelo.setCveAgente(contenido.split("Clave del Agente")[1].split("\n")[1].split("###")[0]);
				modelo.setAgente(contenido.split("Clave del Agente")[1].split("\n")[1].split("###")[1].replace("\r", ""));
			}
			
			return modelo;
		} catch (Exception ex) {
			modelo.setError(
					GnpAutos3Model.this.getClass().getTypeName() + " | " + ex.getMessage() + " | " + ex.getCause());
			return modelo;
		}
		
		
	}

}
