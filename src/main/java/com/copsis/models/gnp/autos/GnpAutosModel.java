package com.copsis.models.gnp.autos;

import java.util.ArrayList;
import java.util.List;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraCoberturasModel;
import com.copsis.models.EstructuraJsonModel;
import com.copsis.models.EstructuraRecibosModel;

public class GnpAutosModel {
	// Clases
	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
	// Variables
	private String contenido = "", newcontenido = "";
	private int inicio = 0, fin = 0, donde = 0, longitud_texto = 0;

	// constructor
	public GnpAutosModel(String contenido) {
		this.contenido = contenido;
	}

	public EstructuraJsonModel procesar() {

		contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());

		try {

			// tipo
			modelo.setTipo(1);
			// cia
			modelo.setCia(18);
		
			// poliza
			donde = 0;
			donde = fn.recorreContenido(contenido, "No. Póliza");
			if (donde > 0) {
				for (String dato : contenido.split("@@@")[donde].split("\r\n")) {
					if (dato.contains("No. Póliza")) {
						switch (dato.split("###").length) {
						case 1:
							modelo.setPoliza(dato.split("Póliza")[1].trim());
							break;
						}
					}
				}
			}

			// cte_nombre
			// renovacion
			donde = 0;
			donde = fn.recorreContenido(contenido, "Código###de###Cliente###Nombre###Versión###Renovación");
			if (donde > 0) {
				if (contenido.split("@@@")[donde].split("\r\n").length == 1) {
					if (contenido.split("@@@")[donde + 1].split("\r\n").length > 1) {
						for (String dato : contenido.split("@@@")[donde + 1].split("\r\n")) {
							if (dato.contains("Vigencia") == false && dato.split("###").length == 4) {
								modelo.setCteNombre(dato.split("###")[1].trim());
								modelo.setRenovacion(dato.split("###")[3].trim());
							}
						}
					}
				}
			}

			// vigencia_de
			donde = 0;
			donde = fn.recorreContenido(contenido, "R.F.C.###Dirección###Desde");
			if (donde > 0) {
				if (contenido.split("@@@")[donde].split("\r\n").length == 1) {
					if (contenido.split("@@@")[donde].split("###").length == 3) {
						if (contenido.split("@@@")[donde].split("###")[2].contains("del")
								&& contenido.split("@@@")[donde].split("###")[2].split("-").length == 3) {
							modelo.setVigenciaDe(fn.formatDate(
									contenido.split("@@@")[donde].split("###")[2].split("del")[1].trim(), "dd-MM-yy"));
						}
					}
				}
			}

			inicio = contenido.indexOf("ersión###Renovación");
			if (inicio > -1) {
				newcontenido = contenido.substring(inicio + 19, inicio + 150).trim().split("\r\n")[0]
						.replaceAll("@@@", "").trim();
				if (newcontenido.split("###").length == 4) {
					modelo.setIdCliente(newcontenido.split("###")[0].trim());
				}
			}

			// vigencia_a
			// cte_direccion
			// rfc
			// cp
			donde = 0;
			donde = fn.recorreContenido(contenido, "###Hasta las 12 hrs del");
			if (donde > 0) {
				if (contenido.split("@@@")[donde].split("\r\n").length > 1) {
					newcontenido = "";
					for (String dato : contenido.split("@@@")[donde].split("\r\n")) {
						if (dato.split("###").length == 3) {
							if (dato.split("###")[2].contains("del") && dato.split("###")[2].split("-").length == 3) {
								modelo.setVigenciaA(
										fn.formatDate(dato.split("###")[2].split("del")[1].trim(), "dd-MM-yy"));
								modelo.setRfc(dato.split("###")[0].trim());
								newcontenido = dato.split("###")[1].trim();
							}
						} else if (dato.split("###").length == 2) {
							if (dato.split("###")[1].contains("Duración:") && dato.split("###")[0].contains("C.P.")) {
								modelo.setCp(dato.split("###")[0].split("C.P.")[1].trim());
								newcontenido += " " + dato.split("###")[0].split("C.P.")[0].trim();
							} else if (dato.split("###").length == 2) {
								if (dato.split("###")[0].trim().length() == 5) {
									if (fn.isNumeric(dato.split("###")[0].trim())) {
										modelo.setCp(dato.split("###")[0].trim());
									}
								}
							}
						}
					}
					modelo.setCteDireccion(newcontenido.replace(", C.P.", "").trim());
				}
			}

			// descripcion (vehiculo)
			// serie
			// prima_neta
			donde = 0;
			donde = fn.recorreContenido(contenido, "Descripción###Serie###MONTO###A###PAGAR");
			if (donde > 0) {
				if (contenido.split("@@@")[donde].split("\r\n").length == 1) {
					if (contenido.split("@@@")[donde + 1].split("\r\n").length == 1) {
						if (contenido.split("@@@")[donde + 1].split("###").length == 5) {
							modelo.setDescripcion(contenido.split("@@@")[donde + 1].split("###")[0].trim());
							modelo.setSerie(contenido.split("@@@")[donde + 1].split("###")[1].trim());
							if (contenido.split("@@@")[donde + 1].split("###")[3].trim().equals("Neta")) {
								modelo.setPrimaneta(
										fn.castFloat(contenido.split("@@@")[donde + 1].split("###")[4].trim()));
							}
						}
					}
				}
			}

			// modelo
			// placas
			// derecho
			// recargo
			// motor
			donde = 0;
			donde = fn.recorreContenido(contenido, "Modelo###Placas###Motor###Fraccionado###");
			if (donde > 0) {
				if (contenido.split("@@@")[donde].split("\r\n").length == 1) {
					if (contenido.split("@@@")[donde].split("\r\n")[0].split("###").length == 5) {
						if (contenido.split("@@@")[donde].split("###")[3].trim().equals("Fraccionado")) {
							modelo.setRecargo(fn.castFloat(contenido.split("@@@")[donde].split("###")[4].trim()));

						}
					}
					if (contenido.split("@@@")[donde + 1].split("\r\n").length == 1) {
						switch (contenido.split("@@@")[donde + 1].split("###").length) {
						case 6:
							modelo.setModelo(fn.castInteger(
									fn.remplazarMultiple(contenido.split("@@@")[donde + 1].split("###")[0].trim(),
											fn.remplazosGenerales())));
							if (contenido.split("@@@")[donde + 1].split("###")[2].trim().equals("Derecho")) {
								if (contenido.split("@@@")[donde + 1].split("###")[1].contains(" ")) {
									modelo.setMotor(contenido.split("@@@")[donde + 1].split("###")[1].trim());
								} else {
									modelo.setPlacas(contenido.split("@@@")[donde + 1].split("###")[1].trim());
								}
							} else {
								modelo.setMotor(contenido.split("@@@")[donde + 1].split("###")[1].trim());
							}
							if (contenido.split("@@@")[donde + 1].split("###")[4].trim().equals("Póliza")) {
								modelo.setDerecho(
										fn.castFloat(contenido.split("@@@")[donde + 1].split("###")[5].trim()));

							}
							break;
						case 7:
							modelo.setModelo(fn.castInteger(
									fn.remplazarMultiple(contenido.split("@@@")[donde + 1].split("###")[0].trim(),
											fn.remplazosGenerales())));
							modelo.setPlacas(contenido.split("@@@")[donde + 1].split("###")[1].trim());
							modelo.setMotor(contenido.split("@@@")[donde + 1].split("###")[2].trim());
							if (contenido.split("@@@")[donde + 1].split("###")[5].trim().equals("Póliza")) {
								modelo.setDerecho(fn.castFloat(
										fn.remplazarMultiple(contenido.split("@@@")[donde + 1].split("###")[6].trim(),
												fn.remplazosGenerales())));
							}
							break;
						}
					}
				}
			}

			inicio = contenido.indexOf("I.V.A.");
			if (inicio > 0) {

				newcontenido = contenido.split("I.V.A.")[1].split("\n")[0].replace("###", "").replace("$", "")
						.replace(",", "").trim();
				modelo.setIva(fn.castFloat(fn.remplazarMultiple(newcontenido, fn.remplazosGenerales())));

			}

			/**
			 * otro formato de gnp
			 */
			if (modelo.getSerie().length() == 0 && modelo.getDescripcion().length() == 0) {
				inicio = contenido.indexOf("VEHÍCULO###ASEGURADO");
				fin = contenido.indexOf("DESGLOSE");
				if (inicio > 0 & fin > 0 & inicio < fin) {
					newcontenido = contenido.substring(inicio, fin).replace("@@@", "");
					for (int i = 0; i < newcontenido.split("\n").length; i++) {

						if (newcontenido.split("\n")[i].contains("Descripción")) {
							modelo.setPrimaneta(fn.castFloat(fn.remplazarMultiple(
									newcontenido.split("\n")[i].split("Neta")[1].replace("###", ""),
									fn.remplazosGenerales())));
							modelo.setDescripcion(newcontenido.split("\n")[i + 1].split("###")[0]);
							modelo.setSerie(newcontenido.split("\n")[i + 1].split("###")[1]);
						}
						if (newcontenido.split("\n")[i].contains("Modelo")) {
							modelo.setDerecho(fn.castFloat(fn.remplazarMultiple(
									newcontenido.split("\n")[i].split("Póliza")[1].replace("###", ""),
									fn.remplazosGenerales())));
							modelo.setModelo(fn.castInteger(fn.remplazarMultiple(
									newcontenido.split("\n")[i + 1].split("###")[0], fn.remplazosGenerales())));
							modelo.setMotor(newcontenido.split("\n")[i + 1].split("###")[1]);
						}

						if (newcontenido.split("\n")[i].contains("Importe")) {
							modelo.setPrimaTotal(fn.castFloat(fn.remplazarMultiple(
									newcontenido.split("\n")[i].split("Pagar")[1].replace("###", ""),
									fn.remplazosGenerales())));

						}

					}
				}
			}

			// iva
			donde = 0;

			// prima_total
			donde = 0;
			donde = fn.recorreContenido(contenido, "Importe###por###Pagar");
			if (donde > 0) {
				if (contenido.split("@@@")[donde].split("\r\n").length == 1) {
					if (contenido.split("@@@")[donde].split("###").length == 7) {
						if (contenido.split("@@@")[donde].split("###")[5].trim().equals("Pagar")) {

							modelo.setPrimaTotal(fn.castFloat(fn.remplazarMultiple(
									contenido.split("@@@")[donde].split("###")[6].trim(), fn.remplazosGenerales())));
						}
					}
				}
			}

			// forma_pago
			// moneda
			donde = 0;
			donde = fn.recorreContenido(contenido, "Forma###de###Pago###Moneda");
			if (donde > 0) {
				if (contenido.split("@@@")[donde].split("\r\n").length == 2) {
					for (int i = 0; i < contenido.split("@@@")[donde].split("\r\n").length; i++) {
						if (i == 0) {
							if (contenido.split("@@@")[donde].split("\r\n")[i].contains("Forma###de###Pago###Moneda")) {
								if (contenido.split("@@@")[donde].split("\r\n")[i + 1].split("###").length == 4) {
									if (contenido.split("@@@")[donde].split("\r\n")[i + 1].split("###")[1].trim()
											.split(" ").length == 1
											&& contenido.split("@@@")[donde].split("\r\n")[i + 1].split("###")[2].trim()
													.split(" ").length == 1) {
										modelo.setFormaPago(fn.formaPago(
												contenido.split("@@@")[donde].split("\r\n")[i + 1].split("###")[1]
														.trim()));
										modelo.setMoneda(fn.moneda(
												contenido.split("@@@")[donde].split("\r\n")[i + 1].split("###")[2]
														.trim()));
									}
								}
							}
						}
					}
				}
			}

			// agente
			// cve_agente
			donde = 0;
			donde = fn.recorreContenido(contenido, "Clave###Agente");

			if (donde > 0) {
				if (contenido.split("@@@")[donde].split("\r\n").length == 2) {

					if (contenido.split("@@@")[donde].split("\r\n")[1].contains("Clave###Agente")) {

						int split = contenido.split("@@@")[donde + 1].split("\r\n").length;

						switch (split) {
						case 1:
							if (contenido.split("@@@")[donde + 1].split("\r\n")[0].split("###").length == 3) {
								modelo.setCveAgente(
										contenido.split("@@@")[donde + 1].split("\r\n")[0].split("###")[0].trim());
								modelo.setAgente(
										contenido.split("@@@")[donde + 1].split("\r\n")[0].split("###")[1].trim());
							}
							break;
						case 2:
							if (contenido.split("@@@")[donde + 1].split("\r\n")[0].split("###").length == 3) {
								modelo.setCveAgente(
										contenido.split("@@@")[donde + 1].split("\r\n")[0].split("###")[0].trim());
								modelo.setAgente(
										contenido.split("@@@")[donde + 1].split("\r\n")[0].split("###")[1].trim());
							}
							break;

						}

					}
				}
			}

			// plan
			if (contenido.split("@@@")[1].contains("No. Póliza")
					&& contenido.split("@@@")[1].contains("Regular Autos")) {
				inicio = -1;
				fin = -1;
				inicio = contenido.split("@@@")[1].trim().indexOf("Regular Autos");
				fin = contenido.split("@@@")[1].trim().indexOf("No. Póliza");
				if (inicio > -1 && fin > inicio) {
					modelo.setPlan(contenido.split("@@@")[1].trim().substring((inicio + 13), fin).trim());
				}
			}

			// conductor
			donde = 0;
			donde = fn.recorreContenido(contenido, "Cliente###Conductor###Habitual###Edad###Sexo");
			if (donde > 0) {
				if (contenido.split("@@@")[donde].split("\r\n").length == 2) {
					if (contenido.split("@@@")[donde].split("\r\n")[1].split("###").length == 4
							&& contenido.split("@@@")[donde].split("\r\n")[1].contains("Edad###Sexo") == false) {
						modelo.setConductor(contenido.split("@@@")[donde].split("\r\n")[1].split("###")[1].trim());
					}
				}
			}

			List<EstructuraCoberturasModel> coberturas = new ArrayList<>();
			// coberturas{nombre, sa, deducible}
			inicio = 0;
			fin = 0;
			inicio = contenido.indexOf("Descripción###Suma###Asegurada###Deducible");
			fin = contenido.indexOf("Total###Coberturas");
			longitud_texto = 42;
			if (inicio > -1 && fin > inicio) {
				for (String x : contenido.substring((inicio + longitud_texto), fin).trim().split("\r\n")) {
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

			switch (modelo.getFormaPago()) {
			case 1:
				recibo.setReciboId("");
				recibo.setSerie("1/1");
				recibo.setVigenciaDe(modelo.getVigenciaDe());
				recibo.setVigenciaA(modelo.getVigenciaA());
				if (recibo.getVigenciaDe().length() > 0) {
					recibo.setVencimiento(fn.dateAdd(recibo.getVigenciaDe(), 30, 1));
				}
				recibo.setPrimaneta(fn.castBigDecimal(modelo.getPrimaneta(), 2));
				recibo.setDerecho(fn.castBigDecimal(modelo.getDerecho(), 2));
				recibo.setRecargo(fn.castBigDecimal(modelo.getRecargo(), 2));
				recibo.setIva(fn.castBigDecimal(modelo.getDerecho(), 2));

				recibo.setPrimaTotal(fn.castBigDecimal(modelo.getPrimaTotal(), 2));
				recibo.setAjusteUno(fn.castBigDecimal(modelo.getAjusteUno(), 2));
				recibo.setAjusteDos(fn.castBigDecimal(modelo.getAjusteDos(), 2));
				recibo.setCargoExtra(fn.castBigDecimal(modelo.getCargoExtra(), 2));
				recibos.add(recibo);
				break;
			}
			modelo.setRecibos(recibos);

			return modelo;
		} catch (Exception ex) {
			modelo.setError(
					GnpAutosModel.this.getClass().getTypeName() + " | " + ex.getMessage() + " | " + ex.getCause());
			return modelo;
		}

	}
}
