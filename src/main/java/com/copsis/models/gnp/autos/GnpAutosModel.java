package com.copsis.models.gnp.autos;

import java.util.ArrayList;
import java.util.List;

import com.copsis.constants.ConstantsValue;
import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraCoberturasModel;
import com.copsis.models.EstructuraJsonModel;
import com.copsis.models.EstructuraRecibosModel;

public class GnpAutosModel {
	// Clases
	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
	// Variables
	private String contenido = "";

	// constructor
	public GnpAutosModel(String contenido) {
		this.contenido = contenido;
	}

	public EstructuraJsonModel procesar() {

		int inicio = 0;
		int fin = 0;
		int donde = 0;
		int longitudTexto = 0;

		StringBuilder newcontenido = new StringBuilder();

		contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());
		contenido = contenido.replace("Importe###por###Pagar", "Importe por Pagar").replace("Derecho###de###Póliza",
				ConstantsValue.DERECHO_POLIZA);

		try {

			// tipo
			modelo.setTipo(1);
			// cia
			modelo.setCia(18);

			// poliza
			donde = 0;
			donde = fn.recorreContenido(contenido, ConstantsValue.NO_POLIZA);
			if (donde > 0) {
				for (String dato : contenido.split("@@@")[donde].split("\r\n")) {
					if (dato.contains(ConstantsValue.NO_POLIZA) && dato.split("###").length == 1) {
						modelo.setPoliza(dato.split("Póliza")[1].trim());
						break;
					}
				}
			}

			// cte_nombre
			// renovacion
			donde = 0;
			donde = fn.recorreContenido(contenido, "Código###de###Cliente###Nombre###Versión###Renovación");
			if (donde > 0 && contenido.split("@@@")[donde].split("\r\n").length == 1
					&& contenido.split("@@@")[donde + 1].split("\r\n").length > 1) {

				for (String dato : contenido.split("@@@")[donde + 1].split("\r\n")) {
					if (!dato.contains("Vigencia") && dato.split("###").length == 4) {
						modelo.setCteNombre(dato.split("###")[1].trim());
						modelo.setRenovacion(dato.split("###")[3].trim());
					}
				}
			}

			// vigencia_de
			donde = 0;
			donde = fn.recorreContenido(contenido, "R.F.C.###Dirección###Desde");
			if (donde > 0 && contenido.split("@@@")[donde].split("\r\n").length == 1
					&& contenido.split("@@@")[donde].split("###").length == 3
					&& contenido.split("@@@")[donde].split("###")[2].contains("del")
					&& contenido.split("@@@")[donde].split("###")[2].split("-").length == 3) {

				modelo.setVigenciaDe(fn.formatDate(contenido.split("@@@")[donde].split("###")[2].split("del")[1].trim(),
						"dd-MM-yy"));

			}

			inicio = contenido.indexOf("ersión###Renovación");
			if (inicio > -1) {
				newcontenido.append(contenido.substring(inicio + 19, inicio + 150).trim().split("\r\n")[0]
						.replace("@@@", "").trim());
				if (newcontenido.toString().split("###").length == 4) {
					modelo.setIdCliente(newcontenido.toString().split("###")[0].trim());
				}
			}

			// vigencia_a
			// cte_direccion
			// rfc
			// cp
			donde = 0;
			donde = fn.recorreContenido(contenido, "###Hasta las 12 hrs del");

			if (donde > 0 && contenido.split("@@@")[donde].split("\r\n").length > 1) {

				newcontenido = new StringBuilder();
				for (String dato : contenido.split("@@@")[donde].split("\r\n")) {

					if (dato.split("###").length == 3) {
						if (dato.split("###")[2].contains("del") && dato.split("###")[2].split("-").length == 3) {
							modelo.setVigenciaA(fn.formatDate(dato.split("###")[2].split("del")[1].trim(), "dd-MM-yy"));
							modelo.setRfc(dato.split("###")[0].trim());
							newcontenido.append(dato.split("###")[1].trim());
						}
					} else if (dato.split("###").length == 2) {
						if (dato.split("###")[1].contains("Duración:") && dato.split("###")[0].contains("C.P.")) {
							if (!dato.contains("C.P.###Duración")) {
								modelo.setCp(dato.split("###")[0].split("C.P.")[1].trim());
								newcontenido.append(" ").append(dato.split("###")[0].split("C.P.")[0].trim());
							}
						} else if (dato.split("###").length == 2 && dato.split("###")[0].trim().length() == 5
								&& fn.isNumeric(dato.split("###")[0].trim())) {
							modelo.setCp(dato.split("###")[0].trim());
						}
					}
				}
				modelo.setCteDireccion(newcontenido.toString().replace(", C.P.", "").trim());

			}

			if (modelo.getCp().length() == 0) {
				inicio = contenido.indexOf(ConstantsValue.REFERENCIA);
				fin = contenido.indexOf("Descripción");
				if (inicio > 0 && fin > 0 && inicio < fin) {
					newcontenido = new StringBuilder();
					newcontenido.append(contenido.substring(inicio, fin));
					if (fn.isNumeric(
							newcontenido.toString().split(ConstantsValue.REFERENCIA)[1].replace("###", "").trim()))
						modelo.setCp(
								newcontenido.toString().split(ConstantsValue.REFERENCIA)[1].replace("###", "").trim());
				}

			}

			// descripcion (vehiculo)
			// serie
			// prima_neta
			donde = 0;
			donde = fn.recorreContenido(contenido, "Descripción###Serie###MONTO###A###PAGAR");
			if (donde > 0 && contenido.split("@@@")[donde].split("\r\n").length == 1
					&& contenido.split("@@@")[donde + 1].split("\r\n").length == 1
					&& contenido.split("@@@")[donde + 1].split("###").length == 5) {
				modelo.setDescripcion(contenido.split("@@@")[donde + 1].split("###")[0].trim());
				modelo.setSerie(contenido.split("@@@")[donde + 1].split("###")[1].trim());
				if (contenido.split("@@@")[donde + 1].split("###")[3].trim().equals("Neta")) {
					modelo.setPrimaneta(fn.castBigDecimal(
							fn.preparaPrimas(contenido.split("@@@")[donde + 1].split("###")[4].trim())));
				}

			}

			inicio = contenido.indexOf("Modelo###Placas###Motor");
			fin = contenido.indexOf("Importe por Pagar");

			if (inicio > -1 && fin > -1 && inicio < fin) {
				newcontenido.append(contenido.substring(inicio, fin).replace("@@@", "").replace("\r", ""));
				for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {

					if (newcontenido.toString().split("\n")[i].contains(ConstantsValue.MODELO2)
							&& newcontenido.toString().split("\n")[i].contains("Placas")
							&& newcontenido.toString().split("\n")[i].contains("Motor")
							&& newcontenido.toString().split("\n")[i].contains("Derecho")) {
						if (newcontenido.toString().split("\n")[i].contains(ConstantsValue.DERECHO_POLIZA)) {
							modelo.setDerecho(fn.castBigDecimal(fn.preparaPrimas(
									newcontenido.toString().split("\n")[i].split(ConstantsValue.DERECHO_POLIZA)[1]
											.replace("###", "").trim())));
						}

						if (newcontenido.toString().split("\n")[i + 1].contains(ConstantsValue.IVA)) {
							modelo.setModelo(
									fn.castInteger(newcontenido.toString().split("\n")[i + 1].split("###")[0]));
							modelo.setPlacas(newcontenido.toString().split("\n")[i + 1].split("###")[1]);
							modelo.setMotor(newcontenido.toString().split("\n")[i + 1].split("###")[2]);
							modelo.setIva(fn.castBigDecimal(fn.preparaPrimas(
									newcontenido.toString().split("\n")[i + 1].split(ConstantsValue.IVA)[1]
											.replace("###", "").trim())));
						}
					}

					if (newcontenido.toString().split("\n")[i].contains(ConstantsValue.MODELO2)
							&& newcontenido.toString().split("\n")[i].contains("Placas")
							&& newcontenido.toString().split("\n")[i].contains("Motor")
							&& newcontenido.toString().split("\n")[i].contains(ConstantsValue.FRACCIONADO)) {

						if (newcontenido.toString().split("\n")[i].contains(ConstantsValue.FRACCIONADO)) {
							modelo.setRecargo(fn.castBigDecimal(fn.preparaPrimas(
									newcontenido.toString().split("\n")[i].split(ConstantsValue.FRACCIONADO)[1]
											.replace("###", "").trim())));
						}
						if (newcontenido.toString().split("\n")[i + 1].contains(ConstantsValue.DERECHO_POLIZA)) {
							int sp = newcontenido.toString().split("\n")[i + 1].trim().split("###").length;

							switch (sp) {
							case 4:
								modelo.setModelo(
										fn.castInteger(newcontenido.toString().split("\n")[i + 1].split("###")[0]));
								if (newcontenido.toString().split("\n")[i + 1].split("###")[1].length() > 10) {
									modelo.setMotor(newcontenido.toString().split("\n")[i + 1].split("###")[1]);
								} else {
									modelo.setPlacas(newcontenido.toString().split("\n")[i + 1].split("###")[1]);
								}

								modelo.setDerecho(
										fn.castBigDecimal(fn.preparaPrimas(newcontenido.toString().split("\n")[i + 1]
												.split(ConstantsValue.DERECHO_POLIZA)[1].replace("###", "").trim())));
								break;
							case 5:
								modelo.setModelo(
										fn.castInteger(newcontenido.toString().split("\n")[i + 1].split("###")[0]));
								modelo.setPlacas(newcontenido.toString().split("\n")[i + 1].split("###")[1]);
								modelo.setMotor(newcontenido.toString().split("\n")[i + 1].split("###")[2]);
								modelo.setDerecho(
										fn.castBigDecimal(fn.preparaPrimas(newcontenido.toString().split("\n")[i + 1]
												.split(ConstantsValue.DERECHO_POLIZA)[1].replace("###", "").trim())));
								break;
							default:
								break;
							}
						}
					}
				}
			}

			inicio = contenido.indexOf("I.V.A.");
			if (inicio > 0) {
				newcontenido = new StringBuilder();
				newcontenido.append(contenido.split("I.V.A.")[1].split("\n")[0].replace("###", "").replace("$", "")
						.replace(",", "").trim());
				modelo.setIva(fn.castBigDecimal(
						fn.preparaPrimas(fn.remplazarMultiple(newcontenido.toString(), fn.remplazosGenerales()))));

			}

			/**
			 * otro formato de gnp
			 */
			if (modelo.getSerie().length() == 0 && modelo.getDescripcion().length() == 0) {
				inicio = contenido.indexOf("VEHÍCULO###ASEGURADO");
				fin = contenido.indexOf("DESGLOSE");
				if (inicio > 0 && fin > 0 && inicio < fin) {
					newcontenido = new StringBuilder();
					newcontenido.append(contenido.substring(inicio, fin).replace("@@@", ""));
					for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {

						if (newcontenido.toString().split("\n")[i].contains("Descripción")) {
							modelo.setPrimaneta(fn.castBigDecimal(fn.preparaPrimas(fn.remplazarMultiple(
									newcontenido.toString().split("\n")[i].split("Neta")[1].replace("###", ""),
									fn.remplazosGenerales()))));
							modelo.setDescripcion(newcontenido.toString().split("\n")[i + 1].split("###")[0]);
							modelo.setSerie(newcontenido.toString().split("\n")[i + 1].split("###")[1]);
						}
						if (newcontenido.toString().split("\n")[i].contains("Modelo")) {
							modelo.setDerecho(fn.castBigDecimal(fn.preparaPrimas(fn.remplazarMultiple(
									newcontenido.toString().split("\n")[i].split("Póliza")[1].replace("###", ""),
									fn.remplazosGenerales()))));

						}

						if (newcontenido.toString().split("\n")[i].contains("Importe")) {
							modelo.setPrimaTotal(fn.castBigDecimal(fn.preparaPrimas(fn.remplazarMultiple(
									newcontenido.toString().split("\n")[i].split("Pagar")[1].replace("###", ""),
									fn.remplazosGenerales()))));

						}

					}
				}
			}

			// iva
			donde = 0;

			// prima_total
			donde = 0;
			donde = fn.recorreContenido(contenido, "Importe###por###Pagar");
			if (donde > 0 && contenido.split("@@@")[donde].split("\r\n").length == 1
					&& contenido.split("@@@")[donde].split("###").length == 7
					&& contenido.split("@@@")[donde].split("###")[5].trim().equals("Pagar")) {

				modelo.setPrimaTotal(fn.castBigDecimal(fn.preparaPrimas(fn.remplazarMultiple(
						contenido.split("@@@")[donde].split("###")[6].trim(), fn.remplazosGenerales()))));
			}

			// forma_pago
			// moneda
			donde = 0;
			donde = fn.recorreContenido(contenido, "Forma###de###Pago###Moneda");
			if (donde > 0 && contenido.split("@@@")[donde].split("\r\n").length == 2) {

				for (int i = 0; i < contenido.split("@@@")[donde].split("\r\n").length; i++) {
					if (i == 0 && contenido.split("@@@")[donde].split("\r\n")[i].contains("Forma###de###Pago###Moneda")
							&& contenido.split("@@@")[donde].split("\r\n")[i + 1].split("###").length == 4
							&& contenido.split("@@@")[donde].split("\r\n")[i + 1].split("###")[1].trim()
									.split(" ").length == 1
							&& contenido.split("@@@")[donde].split("\r\n")[i + 1].split("###")[2].trim()
									.split(" ").length == 1) {

						modelo.setFormaPago(fn
								.formaPago(contenido.split("@@@")[donde].split("\r\n")[i + 1].split("###")[1].trim()));
						modelo.setMoneda(
								fn.moneda(contenido.split("@@@")[donde].split("\r\n")[i + 1].split("###")[2].trim()));

					}
				}
			}

			// agente
			// cve_agente
			donde = 0;
			donde = fn.recorreContenido(contenido, "Clave###Agente");

			if (donde > 0 ) {
				for (int i = 0; i < contenido.split("@@@")[donde].split("\n").length; i++) {
					if (contenido.split("@@@")[donde].split("\n")[i].contains("Clave")
							&& contenido.split("@@@")[donde].split("\n")[i].contains("Agente") && contenido.split("@@@")[donde].split("\n").length > 3) {						
							modelo.setCveAgente(contenido.split("@@@")[donde].split("\n")[i+1].split("###")[0].trim());
							modelo.setAgente(contenido.split("@@@")[donde].split("\n")[i+1].split("###")[1].trim());								
					}
				}
				if(modelo.getAgente().length() == 0 && modelo.getCveAgente().length() == 0 && contenido.split("Clave###Agente")[1].split("\n").length > 2 && contenido.split("Clave###Agente")[1].split("\n")[1].contains("-")) {					
					modelo.setCveAgente(contenido.split("Clave###Agente")[1].split("\n")[1].split("###")[0].replace("@@@", "").trim());
			        newcontenido = new StringBuilder();
					newcontenido.append(contenido.split("Clave###Agente")[1].split("\n")[1].split("###")[contenido.split("Clave###Agente")[1].split("\n")[1].split("###").length - 1]);
					modelo.setAgente(contenido.split("Clave###Agente")[1].split("\n")[1].split(modelo.getCveAgente())[1].split(newcontenido.toString())[0].replace("###", " ").trim());
					
				}

			}
			
			
			

			// plan

			for (int j = 0; j < contenido.split("@@@").length; j++) {
				if (contenido.split("@@@")[j].contains(ConstantsValue.NO_POLIZA)
						&& contenido.split("@@@")[j].contains("Regular Autos")) {
					inicio = contenido.split("@@@")[j].trim().indexOf("Regular Autos");
					fin = contenido.split("@@@")[j].trim().indexOf(ConstantsValue.NO_POLIZA);

					if (inicio > -1 && fin > inicio) {				
						modelo.setPlan(contenido.split("@@@")[j].trim().substring((inicio + 13), fin).trim());
					}
				}
			}
			
			if(modelo.getPlan().length() == 0 && newcontenido.append(contenido.split("CONTRATANTE")[0]).toString().contains("Amplia")) {				
					modelo.setPlan("Amplia");				
			}

			// conductor
			donde = 0;
			donde = fn.recorreContenido(contenido, "Cliente###Conductor###Habitual###Edad###Sexo");
			if (donde > 0 && contenido.split("@@@")[donde].split("\r\n").length == 2
					&& contenido.split("@@@")[donde].split("\r\n")[1].split("###").length == 4
					&& !contenido.split("@@@")[donde].split("\r\n")[1].contains("Edad###Sexo")) {
				modelo.setConductor(contenido.split("@@@")[donde].split("\r\n")[1].split("###")[1].trim());

			}

			List<EstructuraCoberturasModel> coberturas = new ArrayList<>();
			// coberturas{nombre, sa, deducible}
			inicio = 0;
			fin = 0;
			inicio = contenido.indexOf("Descripción###Suma###Asegurada###Deducible");
			fin = contenido.indexOf("Total###Coberturas");
			longitudTexto = 42;
			if (inicio > -1 && fin > inicio) {
				for (String x : contenido.substring((inicio + longitudTexto), fin).trim().split("\r\n")) {
					EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();
					if (x.split("###").length == 3) {

						cobertura.setNombre(x.split("###")[0].trim());
						cobertura.setSa(fn.eliminaSpacios(x.split("###")[1].trim(), ' ', ""));
						cobertura.setDeducible(x.split("###")[2].trim());
						coberturas.add(cobertura);

					}
				}
			}

			modelo.setCoberturas(coberturas);

			modelo.setFechaEmision(modelo.getVigenciaA());
			List<EstructuraRecibosModel> recibos = new ArrayList<>();
			EstructuraRecibosModel recibo = new EstructuraRecibosModel();

			if (modelo.getFormaPago() == 1) {
				recibo.setReciboId("");
				recibo.setSerie("1/1");
				recibo.setVigenciaDe(modelo.getVigenciaDe());
				recibo.setVigenciaA(modelo.getVigenciaA());
				if (recibo.getVigenciaDe().length() > 0) {
					recibo.setVencimiento(fn.dateAdd(recibo.getVigenciaDe(), 30, 1));
				}
				recibo.setPrimaneta(modelo.getPrimaneta());
				recibo.setDerecho(modelo.getDerecho());
				recibo.setRecargo(modelo.getRecargo());
				recibo.setIva(modelo.getDerecho());

				recibo.setPrimaTotal(modelo.getPrimaTotal());
				recibo.setAjusteUno(modelo.getAjusteUno());
				recibo.setAjusteDos(modelo.getAjusteDos());
				recibo.setCargoExtra(modelo.getCargoExtra());
				recibos.add(recibo);
			}

			modelo.setRecibos(recibos);
		
			newcontenido = new StringBuilder();
			return modelo;
		} catch (Exception ex) {
			modelo.setError(
					GnpAutosModel.this.getClass().getTypeName() + " | " + ex.getMessage() + " | " + ex.getCause());
			return modelo;
		}

	}
}
