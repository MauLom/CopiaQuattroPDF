package com.copsis.models.atlas;

import java.util.ArrayList;
import java.util.List;

import com.copsis.constants.ConstantsValue;
import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraCoberturasModel;
import com.copsis.models.EstructuraJsonModel;

public class AtlasAutosModel {
	// Clases
	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
	// Varaibles
	private String contenido = "";
	private String recibosText = "";

	public AtlasAutosModel(String contenido, String recibos) {
		this.contenido = contenido;
		this.recibosText = recibos;
	}

	public EstructuraJsonModel procesar() {
		String newcontenido = "";
		String resultado = "";
		int inicio = 0;
		int fin = 0;
		contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());
		contenido = contenido.replace("las 12:00 Hrs.del", "").replace("POLIZA", "PÓLIZA")
		.replace("Prima###Neta:", "Prima Neta:");
		try {
	
			modelo.setTipo(1);	
			modelo.setCia(33);

			inicio = contenido.indexOf("PÓLIZA");
			fin = contenido.indexOf("Coberturas Contratadas");

			if (inicio > 0 && fin > 0 && inicio < fin) {
				newcontenido = contenido.substring(inicio, fin).replace("\r", "").replace("@@@", "").trim();
				for (int i = 0; i < newcontenido.split("\n").length; i++) {
					if (newcontenido.split("\n")[i].contains("Póliza")) {
						modelo.setPolizaGuion(newcontenido.split("\n")[i].split("Póliza")[1].replace(":", "").replace("###", ""));
						modelo.setPoliza(newcontenido.split("\n")[i].split("Póliza:")[1].replace("###", "").replace(" ", ""));
					}
					if (newcontenido.split("\n")[i].contains("desde:") && newcontenido.split("\n")[i].contains(ConstantsValue.HASTA)) {
					
						modelo.setVigenciaDe(fn.formatDateMonthCadena(fn.obtenVigePoliza(newcontenido.split("\n")[i]).get(0)));
						modelo.setVigenciaA(fn.formatDateMonthCadena(fn.obtenVigePoliza(newcontenido.split("\n")[i]).get(1)));
						modelo.setFechaEmision(fn.formatDateMonthCadena(fn.obtenVigePoliza(newcontenido.split("\n")[i]).get(2)));
				
					}
					if (newcontenido.split("\n")[i].contains("Contratante")
							&& newcontenido.split("\n")[i].contains("Domicilio")
							&& newcontenido.split("\n")[i].contains("RFC:")) {
						modelo.setCteNombre(newcontenido.split("\n")[i + 1]);
						modelo.setRfc(newcontenido.split("\n")[i].split("RFC:")[1].replace("###", ""));
						resultado = newcontenido.split("\n")[i + 2] + " " + newcontenido.split("\n")[i + 3];
						modelo.setCteDireccion(resultado.replace("###", " "));
					}
					if (newcontenido.split("\n")[i].contains("Producto:")
							&& newcontenido.split("\n")[i].contains(ConstantsValue.AGENTE)
							&& newcontenido.split("\n")[i].contains("Orden:")) {
						modelo.setPlan(
								newcontenido.split("\n")[i].split("Producto:")[1].split("Orden")[0].replace("###", ""));
						modelo.setCveAgente(newcontenido.split("\n")[i].split(ConstantsValue.AGENTE)[1].replace("###", ""));
					}
					if (newcontenido.split("\n")[i].contains("Moneda:")
							&& newcontenido.split("\n")[i].contains("Prima Neta:")) {
						modelo.setMoneda(fn.moneda(
								newcontenido.split("\n")[i].split("Moneda:")[1].split("Prima")[0].replace("###", "")));

								List<String> valores = fn.obtenerListNumeros(newcontenido.toString().split("\n")[i]);
								modelo.setPrimaneta(fn.castBigDecimal(fn.castDouble(valores.get(0))));

					}
			
					if (newcontenido.split("\n")[i].contains("Forma Pago:")
							&& newcontenido.split("\n")[i].contains("Fraccionado")) {
						modelo.setFormaPago(fn.formaPago(
								newcontenido.split("\n")[i].split("Pago:")[1].split("Recargo")[0].replace("###", "")));
						modelo.setRecargo(fn.castBigDecimal(fn
								.castDouble(newcontenido.split("\n")[i].split("Fraccionado")[1].replace("###", "").replace(":", ""))));
					}
					if (newcontenido.split("\n")[i].contains("Recibo:")
							&& newcontenido.split("\n")[i].contains("Expedición:")) {
						modelo.setDerecho(fn.castBigDecimal(
								fn.castDouble(newcontenido.split("\n")[i].split("Expedición:")[1].replace("###", ""))));
					}
					if (newcontenido.split("\n")[i].contains("IVA:")) {
						modelo.setIva(fn.castBigDecimal(
								fn.castDouble(newcontenido.split("\n")[i].split("IVA:")[1].replace("###", ""))));
					}
					if (newcontenido.split("\n")[i].contains("CP")) {
						modelo.setCp(newcontenido.split("\n")[i].split("CP")[1].replace(":", "").replace("###", ""));
					}

					if (newcontenido.split("\n")[i].contains("Total a pagar:")) {
						modelo.setPrimaTotal(fn.castBigDecimal(fn.castDouble(
								newcontenido.split("\n")[i].split("Total a pagar:")[1].replace("###", ""))));
					}
					if (newcontenido.split("\n")[i].contains("Clave:")
							|| newcontenido.split("\n")[i].contains(ConstantsValue.NO_MOTOR)) {
						modelo.setClave(
								newcontenido.split("\n")[i].split("Clave:")[1].split(ConstantsValue.NO_MOTOR)[0].replace("###", ""));						
								if(newcontenido.split("\n")[i].split("Moto")[1].length() > 5){
									modelo.setMotor(newcontenido.split("\n")[i].split(ConstantsValue.NO_MOTOR)[1].replace("###", ""));
						}					    					 	
					}

					if (newcontenido.split("\n")[i].contains("Descripción:")) {
						modelo.setDescripcion(newcontenido.split("\n")[i].split("Descripción:")[1].replace("###", ""));
					}
					if (newcontenido.split("\n")[i].contains("Modelo:")
							&& newcontenido.split("\n")[i].contains("Circula")) {
						modelo.setModelo(fn.castInteger(
								newcontenido.split("\n")[i].split("Modelo:")[1].replace("###", "").split("Circula")[0]
										.replace("###", "")));
					}
					if (newcontenido.split("\n")[i].contains("Serie:") && newcontenido.split("\n")[i].contains("NCI")) {
						modelo.setSerie(
								newcontenido.split("\n")[i].split("Serie:")[1].split("NCI")[0].replace("###", ""));
					}
					if (newcontenido.split("\n")[i].contains("Placas:")) {
						modelo.setPlacas(newcontenido.split("\n")[i].split("Placas:")[1].replace("###", ""));
					}
				}
			}

			// COBERTURAS

			inicio = contenido.indexOf("Coberturas Contratadas");
			fin = contenido.indexOf("Forman parte de esta Póliza");

			if (inicio > 0 && fin > 0 && inicio < fin) {
				List<EstructuraCoberturasModel> coberturas = new ArrayList<>();
				newcontenido = contenido.substring(inicio, fin).replace("\r", "").replace("@@@", "").trim();
				for (int i = 0; i < newcontenido.split("\n").length; i++) {
					EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();

					if (!newcontenido.split("\n")[i].contains("Coberturas")
							&& !newcontenido.split("\n")[i].contains("Deducible")
							&& !newcontenido.split("\n")[i].contains("Prima")
							) {
						int sp = newcontenido.split("\n")[i].split("###").length;
					

						cobertura.setNombre(newcontenido.split("\n")[i].split("###")[0].trim());
						if (sp > 2) {
						cobertura.setSa(newcontenido.split("\n")[i].split("###")[1].trim());
						}
						if (sp > 3) {
							cobertura.setDeducible(newcontenido.split("\n")[i].split("###")[3].trim());
						}
						coberturas.add(cobertura);
					}
				}
				modelo.setCoberturas(coberturas);
			}

			inicio = recibosText.indexOf("Agente");
			fin = recibosText.indexOf("Forma de pago");
			if (inicio > -1 && fin > -1 && inicio < fin) {
				newcontenido = recibosText.substring(inicio, fin).replace("\r", "").replace("@@@", "").trim();
				for (int i = 0; i < newcontenido.split("\n").length; i++) {

					if (newcontenido.split("\n")[i].contains(ConstantsValue.AGENTE)) {
						modelo.setAgente(newcontenido.split("\n")[i].split(ConstantsValue.AGENTE)[1].split("###")[2]);
					}
				}
			}
			if (modelo.getAgente().length() >= 0) {
				inicio = contenido.indexOf("Agente");
				fin = contenido.indexOf("En cumplimiento");
				if (inicio > -1 && fin > -1 && inicio < fin) {
					newcontenido = contenido.substring(inicio, fin).replace("\r", "").replace("@@@", "").trim();
					for (int i = 0; i < newcontenido.split("\n").length; i++) {
						if (newcontenido.split("\n")[i].contains(ConstantsValue.AGENTE)
								&& newcontenido.split("\n")[i].split("###").length > 2) {
							modelo.setAgente(newcontenido.split("\n")[i].split(ConstantsValue.AGENTE)[1].split("###")[1]);

						}
					}
				}
			}

			return modelo;
		} catch (Exception ex) {
			modelo.setError(AtlasAutosModel.this.getClass().getTypeName() + " - catch:" + ex.getMessage() + " | "
					+ ex.getCause());
			return modelo;
		}

	}

}
