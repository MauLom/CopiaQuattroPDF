package com.copsis.models.gnp;

import java.util.ArrayList;
import java.util.List;

import com.copsis.constants.ConstantsValue;
import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraAseguradosModel;
import com.copsis.models.EstructuraBeneficiariosModel;
import com.copsis.models.EstructuraCoberturasModel;
import com.copsis.models.EstructuraJsonModel;
import com.copsis.models.EstructuraRecibosModel;

public class GnpVidaModel {

	// Clases
	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
	// Varaibles
	private String contenido = "";

	public GnpVidaModel(String contenido) {
		this.contenido = contenido;
	}

	public EstructuraJsonModel procesar() {
		StringBuilder newcontenido = new StringBuilder();
		StringBuilder resultado = new StringBuilder();
		int donde = 0;
		int cuantos = 0;
		int inicio = 0;
		int fin = 0;
		contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());
		try {

			modelo.setTipo(5);
			modelo.setCia(18);
			if (contenido.split("@@@").length >= 3) {
				newcontenido.append(fn.filtroPorRango(contenido, 3));
				donde = fn.recorreContenido(newcontenido.toString(), ConstantsValue.POLIZA_NO);
				for (String dato : newcontenido.toString().split("@@@")[donde].split("\r\n")) {
					if (dato.contains(ConstantsValue.POLIZA_NO)) {
						if (dato.split("###").length == 2) {
							modelo.setPoliza(dato.split("###")[1].trim());
							if (dato.split("###")[0].contains("Vida")) {
								if (dato.split("###")[0].split("Vida").length == 3
										&& dato.split("###")[0].split("Vida")[2]
												.contains(ConstantsValue.POLIZA_ACENT)) {
									modelo.setPlan("Vida " + dato.split("###")[0].split("Vida")[2]
											.split(ConstantsValue.POLIZA_ACENT)[0].trim());
								}
								if (dato.split("###")[0].split("Vida")[1].contains(ConstantsValue.POLIZA_ACENT)) {
									modelo.setPlan(
											dato.split("###")[0].split("Vida")[1].split(ConstantsValue.POLIZA_ACENT)[0]
													.trim());
								}
							}
						} else if (dato.split("###").length == 3
								&& dato.split("###")[1].contains(ConstantsValue.POLIZA_NO)) {
							modelo.setPoliza(dato.split("###")[2].trim());
							if (dato.split("###")[1].trim().split(ConstantsValue.POLIZA_NO).length == 1) {
								modelo.setPlan(dato.split("###")[1].trim().split(ConstantsValue.POLIZA_NO)[0].trim());
							}
						}
					}
				}
			}

			donde = 0;
			newcontenido = new StringBuilder();
			newcontenido.append(fn.filtroPorRango(contenido, 6));
			donde = fn.recorreContenido(newcontenido.toString(), ConstantsValue.CONTRATANTE_HASH) + 1;
			if (donde > 1) {
				if (newcontenido.toString().split("@@@")[donde].split("\r\n")[0].split("###").length == 2) {
					modelo.setCteNombre(newcontenido.toString().split("@@@")[donde].split("\r\n")[0].split("###")[0].trim());
				} else if (newcontenido.toString().split("@@@")[donde].split("\r\n")[0].split("###").length == 1) {
					if (newcontenido.toString().split("@@@")[donde].split("\r\n")[0].contains("Grupo Nacional Provincial")) {// CONDICIONES
						for (int i = 0; i < newcontenido.toString().split("@@@")[(donde + 1)].split("\r\n").length; i++) {
							if (newcontenido.toString().split("@@@")[(donde + 1)].split("\r\n")[i].contains("C ontratante")
									&& (i + 2) < newcontenido.toString().split("@@@")[(donde + 1)].split("\r\n").length
									&& newcontenido.toString().split("@@@")[(donde + 1)].split("\r\n")[(i + 2)].trim()
											.split("###").length == 1) {

								modelo.setCteNombre(newcontenido.toString().split("@@@")[(donde + 1)].split("\r\n")[(i + 2)].trim()
										.replace("###", "").trim());

							}
						}
					} else {
						modelo.setCteNombre(newcontenido.toString().split("@@@")[donde].split("\r\n")[0].trim());
					}
				} else {

					if (newcontenido.toString().split("@@@")[donde - 1].contains(ConstantsValue.CONTRATANTE_HASH)) {
						for (int i = 0; i < newcontenido.toString().split("@@@")[donde - 1].split("\r\n").length; i++) {
							if (newcontenido.toString().split("@@@")[donde - 1].split("\r\n")[i]
									.contains(ConstantsValue.CONTRATANTE_HASH)) {
								if (newcontenido.toString().split("@@@")[donde - 1].split("\r\n")[(i + 2)]
										.split("###").length == 2) {
									modelo.setCteNombre(
											newcontenido.toString().split("@@@")[donde - 1].split("\r\n")[(i + 2)].split("###")[0]
													.trim());
								} else {
									if (newcontenido.toString().split("@@@")[donde - 1].split("\r\n")[(i + 2)]
											.split("###").length == 1) {
										modelo.setCteNombre(
												newcontenido.toString().split("@@@")[donde - 1].split("\r\n")[(i + 2)].trim());
									}
								}

							}
						}
					}
				}
			}

			donde = 0;
			newcontenido = new StringBuilder();
			newcontenido.append(fn.filtroPorRango(contenido, 8));

			if (fn.recorreContenido(newcontenido.toString(), ConstantsValue.CONTRATANTE_HASH) > 0) {
				donde = fn.recorreContenido(newcontenido.toString(), ConstantsValue.CONTRATANTE_HASH) + 1;
			} else if (fn.recorreContenido(newcontenido.toString(), "C ontratante###") > 0) {
				donde = fn.recorreContenido(newcontenido.toString(), "C ontratante###") + 1;
			}

			if (donde > 1) {
				if (newcontenido.toString().split("@@@")[donde].split("\r\n").length == 5
						|| newcontenido.toString().split("@@@")[donde].split("\r\n").length == 10) {
					for (int i = 0; i < newcontenido.toString().split("@@@")[donde].split("\r\n").length; i++) {
						if (i == 1 && newcontenido.toString().split("@@@")[donde].split("\r\n")[i].contains("Vigencia")
								&& newcontenido.toString().split("@@@")[donde].split("\r\n")[i].split("###").length == 2
								&& newcontenido.toString().split("@@@")[donde].split("\r\n")[i].split("###")[0].contains("C.P")) {
							resultado.append(newcontenido.toString().split("@@@")[donde].split("\r\n")[i].split("###")[0].trim()
									.split("C.P")[0].trim());
							
							modelo.setCp(newcontenido.toString().split("@@@")[donde].split("\r\n")[i].split("###")[0].trim()
									.split("C.P")[1].trim());

						}
						if (i == 2 && newcontenido.toString().split("@@@")[donde].split("\r\n")[i].split("###").length == 4) {
							resultado.append(" ").append(newcontenido.toString().split("@@@")[donde].split("\r\n")[i].split("###")[0].trim());
							modelo.setCteDireccion(resultado.toString());
						}
						if (i == 3 && newcontenido.toString().split("@@@")[donde].split("\r\n")[i].split("###").length == 5
								&& newcontenido.toString().split("@@@")[donde].split("\r\n")[i].contains(ConstantsValue.RFC)) {

							if (newcontenido.toString().split("@@@")[donde].split("\r\n")[i].split("###")[0]
									.contains(ConstantsValue.RFC)) {
								modelo.setRfc(
										newcontenido.toString().split("@@@")[donde].split("\r\n")[i].split("###")[0].split(":")[1]
												.trim());
							}

							if (newcontenido.toString().split("@@@")[donde].split("\r\n")[i].split("###")[1].equals(" Desde el")) {
								modelo.setVigenciaDe(fn.formatDate(
										newcontenido.toString().split("@@@")[donde].split("\r\n")[i].split("###")[2].trim() + "-"
												+ newcontenido.toString().split("@@@")[donde].split("\r\n")[i].split("###")[3]
														.trim()
												+ "-"
												+ newcontenido.toString().split("@@@")[donde].split("\r\n")[i].split("###")[4]
														.trim(),
										ConstantsValue.FORMATO_FECHA));

							}

						}
						if (i == 4 && newcontenido.toString().split("@@@")[donde].split("\r\n")[i].split("###").length == 6
								&& newcontenido.toString().split("@@@")[donde].split("\r\n")[i]
										.contains(ConstantsValue.HASTA_EL)) {
							resultado = new StringBuilder();
							
							resultado.append(newcontenido.toString().split("@@@")[donde].split("\r\n")[i].split("###")[3].trim()).append("/")
							.append(newcontenido.toString().split("@@@")[donde].split("\r\n")[i].split("###")[4].trim()).append("/")
							.append(newcontenido.toString().split("@@@")[donde].split("\r\n")[i].split("###")[5].trim());
							
							if (resultado.toString().contains("el")) {

								modelo.setVigenciaA(fn.formatDate(resultado.toString().split("el")[1].replace("/", "-").trim(),
										ConstantsValue.FORMATO_FECHA));
							}
						}
					}
				} else if (newcontenido.toString().split("@@@")[(donde - 1)].split("\r\n").length == 16) { // SEGUNDO CASO
					for (int i = 0; i < newcontenido.toString().split("@@@")[(donde - 1)].split("\r\n").length; i++) {
						if (newcontenido.toString().split("@@@")[(donde - 1)].split("\r\n")[i].split("###").length == 2
								&& newcontenido.toString().split("@@@")[(donde - 1)].split("\r\n")[i].split("###")[0]
										.contains("C.P ")) {
							resultado = new StringBuilder();
							resultado.append(newcontenido.toString().split("@@@")[(donde - 1)].split("\r\n")[i].split("###")[0]
									.split("C.P ")[0].trim());
							modelo.setCp(newcontenido.toString().split("@@@")[(donde - 1)].split("\r\n")[i].split("###")[0]
									.split("C.P ")[1].trim());
						}

						if (newcontenido.toString().split("@@@")[(donde - 1)].split("\r\n")[i].split("###").length == 4) {
							resultado.append(" ").append(newcontenido.toString().split("@@@")[(donde - 1)].split("\r\n")[i].split("###")[0].trim());
							modelo.setCteDireccion(resultado.toString());
						}

						if (newcontenido.toString().split("@@@")[(donde - 1)].split("\r\n")[i].split("###").length == 5
								&& newcontenido.toString().split("@@@")[(donde - 1)].split("\r\n")[i]
										.contains(ConstantsValue.RFC)) {
							if (newcontenido.toString().split("@@@")[(donde - 1)].split("\r\n")[i].split("###")[0]
									.contains(ConstantsValue.RFC)) {
								modelo.setRfc(newcontenido.toString().split("@@@")[(donde - 1)].split("\r\n")[i].split("###")[0]
										.split(":")[1].trim());
							}
							if (newcontenido.toString().split("@@@")[(donde - 1)].split("\r\n")[i].split("###")[1]
									.equals(" Desde el")) {

								modelo.setVigenciaDe(fn.formatDate(
										newcontenido.toString().split("@@@")[(donde - 1)].split("\r\n")[i].split("###")[2].trim()
												+ "-"
												+ newcontenido.toString().split("@@@")[(donde - 1)].split("\r\n")[i]
														.split("###")[3].trim()
												+ "-" + newcontenido.toString().split("@@@")[(donde - 1)].split("\r\n")[i]
														.split("###")[4].trim(),
										ConstantsValue.FORMATO_FECHA));
							}
						}

						if (newcontenido.toString().split("@@@")[(donde - 1)].split("\r\n")[i].split("###").length == 6
								&& newcontenido.toString().split("@@@")[(donde - 1)].split("\r\n")[i]
										.contains(ConstantsValue.HASTA_EL)) {
							
							
							resultado = new StringBuilder();
							resultado.append(newcontenido.toString().split("@@@")[(donde - 1)].split("\r\n")[i].split("###")[3].trim())
							.append("/")
							.append(newcontenido.toString().split("@@@")[(donde - 1)].split("\r\n")[i].split("###")[4].trim())
							.append("/")
							.append(newcontenido.toString().split("@@@")[(donde - 1)].split("\r\n")[i].split("###")[5].trim());
							if (resultado.toString().contains("el")) {

								modelo.setVigenciaA(fn.formatDate(resultado.toString().split("el")[1].replace("/", "-").trim(),
										ConstantsValue.FORMATO_FECHA));
							}
						}

					}
				}
			}

			donde = 0;
			donde = fn.recorreContenido(contenido, "Día###Mes###Año###");
			cuantos = 0;
			if (donde > 0) {
				if (contenido.split("@@@")[donde].split("\r\n").length == 3
						|| contenido.split("@@@")[donde].split("\r\n").length == 11
						|| contenido.split("@@@")[donde].split("\r\n").length == 16) {
					for (String dato : contenido.split("@@@")[donde].split("\r\n")) {
						if (dato.contains("Forma de pago") && dato.split("###").length == 8) {

							if (dato.split("###")[4].contains("pago")) {
								modelo.setFormaPago(fn.formaPago(dato.split("###")[5].trim()));
							}
							if (dato.split("###")[6].contains("Neta")) {
								modelo.setPrimaneta(fn.castBigDecimal(fn.preparaPrimas(dato.split("###")[7].trim())));
							}
							if (dato.split("###")[6].contains("Derecho de")) {
								modelo.setDerecho(fn.castBigDecimal(fn.preparaPrimas(dato.split("###")[7].trim())));
							}

						}

						if (dato.split("###").length == 2 && dato.split("###")[0].contains("Prima Neta")) {
							modelo.setPrimaneta(fn.castBigDecimal(fn.preparaPrimas(dato.split("###")[1].trim())));
						}

						if (dato.contains("Moneda###") && dato.split("###").length == 3) {

							if (dato.split("###")[0].contains(ConstantsValue.MONEDA)) {
								modelo.setMoneda(fn.moneda(dato.split("###")[1].trim()));
								cuantos += 1;
							}
							if (dato.split("###")[2].contains("I.V.A")) {
								modelo.setIva(fn.castBigDecimal(fn.preparaPrimas(dato.split("###")[3].trim())));
							}
						}
					}
				}
				modelo.setModelo(donde);

				if (cuantos == 0) {
					if (contenido.split("@@@")[(donde + 1)].split("\r\n")[0].split("###").length == 4) {
						if (contenido.split("@@@")[(donde + 1)].split("\r\n")[0].split("###")[0]
								.contains(ConstantsValue.MONEDA)) {
							modelo.setMoneda(fn.moneda(
									contenido.split("@@@")[(donde + 1)].split("\r\n")[0].split("###")[1].trim()));
						}
						if (contenido.split("@@@")[(donde + 1)].split("\r\n")[0].split("###")[0]
								.contains(ConstantsValue.MONEDA)) {
							modelo.setMoneda(fn.moneda(
									contenido.split("@@@")[(donde + 1)].split("\r\n")[0].split("###")[1].trim()));
						}
						if (contenido.split("@@@")[(donde + 1)].split("\r\n")[0].split("###")[2].contains("I.V.A.")) {
							modelo.setIva(fn.castBigDecimal(fn.preparaPrimas(
									contenido.split("@@@")[(donde + 1)].split("\r\n")[0].split("###")[3].trim())));
						}
					}

					if (contenido.split("@@@")[(donde + 2)].split("\r\n")[0].split("###").length == 2
							&& contenido.split("@@@")[(donde + 2)].split("\r\n")[0].split("###")[0]
									.contains("Importe a pagar")) {

						modelo.setPrimaTotal(fn.castBigDecimal(fn.preparaPrimas(
								contenido.split("@@@")[(donde + 2)].split("\r\n")[0].split("###")[1].trim())));
					}

				}
			}

			donde = 0;
			if (contenido.split("@@@").length >= 9) {
				newcontenido = new StringBuilder();
				newcontenido.append(fn.filtroPorRango(contenido, 8));
				donde = fn.recorreContenido(newcontenido.toString(), ConstantsValue.FRACCIONADO);
				if (donde > 0) {
					for (String dato : newcontenido.toString().split("@@@")[donde].split("\r\n")) {

						if (dato.contains(ConstantsValue.FRACCIONADO) && dato.split("###").length == 7
								&& dato.split("###")[5].contains(ConstantsValue.FRACCIONADO)) {

							modelo.setRecargo(fn.castBigDecimal(fn.preparaPrimas(dato.split("###")[6].trim())));
						}
						if (dato.split("###").length == 2 && dato.split("###")[0].contains(ConstantsValue.FRACCIONADO)) {
							modelo.setRecargo(fn.castBigDecimal(fn.preparaPrimas(dato.split("###")[1].trim())));
						}
						if (dato.split("###").length == 2 && dato.split("###")[0].contains("Importe a pagar")) {

							modelo.setPrimaTotal(fn.castBigDecimal(fn.preparaPrimas(dato.split("###")[1].trim())));
						}
					}
				}
			}

			donde = 0;
			donde = fn.searchTwoTexts(contenido, "Clave###", "Agente###");
			if (donde > 0) {
				for (String dato : contenido.split("@@@")[donde].split("\r\n")) {
					if (dato.contains(ConstantsValue.AGENTE2) && dato.split("###").length == 5) {
						if (dato.split("###")[0].equals(ConstantsValue.AGENTE2)) {
							modelo.setAgente(dato.split("###")[1].trim());
						}
						if (dato.split("###")[2].contains("Clave")) {
							modelo.setCveAgente(dato.split("###")[3].trim());
						}
					} else if (dato.contains(ConstantsValue.AGENTE2) && dato.split("###").length == 4) {
						if (dato.split("###")[0].equals(ConstantsValue.AGENTE2)) {
							modelo.setAgente(dato.split("###")[1].trim());
						}
						if (dato.split("###")[2].contains("Clave")) {
							modelo.setCveAgente(dato.split("###")[3].trim().replace(" ", "###").split("###")[0].trim());
						}
					}
				}
			}

			donde = fn.recorreContenido(contenido, "Asegurado s");

			if (donde > 0) {
				List<EstructuraAseguradosModel> asegurados = new ArrayList<>();
				for (int i = 0; i < contenido.split("@@@")[donde].split("\r\n").length; i++) {
					EstructuraAseguradosModel asegurado = new EstructuraAseguradosModel();
					// ****************************************************CUANDO 1 ASEGURADO
					if (contenido.split("@@@")[donde].split("\r\n")[i].contains("Asegurado 1")
							&& !contenido.split("@@@")[donde].split("\r\n")[i].contains("Asegurado  2")) {
						if (contenido.split("@@@")[donde].split("\r\n")[i + 1].split("###").length == 1) {
							asegurado.setNombre(contenido.split("@@@")[donde].split("\r\n")[i + 1]);
							asegurados.add(asegurado);
						}
						if (contenido.split("@@@")[donde].split("\r\n")[i + 1].split("###").length == 2) {

							asegurado.setNombre(
									contenido.split("@@@")[donde].split("\r\n")[i + 1].split("###")[0].trim());
							asegurados.add(asegurado);
						}
					}

					if (contenido.split("@@@")[donde].split("\r\n")[i].split("###").length >= 2
							&& contenido.split("@@@")[donde].split("\r\n")[i].split("###")[0].contains("Nacimiento")) {

						asegurado.setNacimiento(
								fn.formatDate(contenido.split("@@@")[donde].split("\r\n")[i].split("###")[1].trim(),
										ConstantsValue.FORMATO_FECHA));
						asegurados.add(asegurado);

					}
					// ****************************************************CUANDO 2 ASEGURADOS
					if (contenido.split("@@@")[donde].split("\r\n")[i].contains("Asegurado  1")
							&& contenido.split("@@@")[donde].split("\r\n")[i].contains("Asegurado  2")
							&& contenido.split("@@@")[donde].split("\r\n")[i + 1].split("###").length == 3) {

						asegurado.setNombre(contenido.split("@@@")[donde].split("\r\n")[i + 1].split("###")[0].trim());
						asegurados.add(asegurado);
						asegurado.setNombre(contenido.split("@@@")[donde].split("\r\n")[i + 1].split("###")[1].trim());
						asegurados.add(asegurado);
					}

				}

				modelo.setAsegurados(asegurados);
			}

			List<EstructuraCoberturasModel> coberturas = new ArrayList<>();
			// coberturas{nombre sa deducible coaseguro}
			newcontenido = new StringBuilder();
			inicio = -1;
			fin = -1;
			inicio = contenido.indexOf("Coberturas");
			fin = contenido.indexOf("@@@Agente");
			if (inicio == -1 && fin == -1 || inicio > -1 && fin == -1 || inicio == -1 && fin > -1) {
				inicio = -1;
				fin = -1;
				inicio = contenido.indexOf("Coberturas");
				fin = contenido.indexOf("@@@  \r\n" + ConstantsValue.AGENTE2);
			}
			if (inicio > -1 && fin > -1) {
				for (String dato : contenido.substring(inicio, fin).trim().split("\r\n")) {
					if (dato.split("###").length >= 2) {

						if (dato.split("###")[1].trim().equals("Amparada")) {
							newcontenido.append("\r\n").append(dato.trim());
						} else {
							if (Double.parseDouble(dato.split("###")[1].replace(".", "").replace(",", "").trim()) >= 0
									&& !dato.split("###")[0].contains("Hasta")
									&& !dato.split("###")[0].trim().equals("Movimiento")
									&& !dato.split("###")[0].trim().equals("Actual")
									&& (!dato.split("###")[0].contains("Importe")
											&& !dato.split("###")[0].contains("Total"))
									&& !dato.split("###")[0].contains("Desde")
									&& !dato.split("###")[0].trim().equals("Anterior")) {

								newcontenido.append("\r\n").append(dato.trim());

							}
						}
					}
				}

				if (newcontenido.length() > 0) {
					for (String dato : newcontenido.toString().split("\n")) {
						EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();
						if (dato.split("###").length >= 2) {
							cobertura.setNombre(dato.split("###")[0].replace("@@@", "").trim());
							cobertura.setSa(dato.split("###")[1].replace("@@@", "").trim());
							coberturas.add(cobertura);
						}
					}
				}
			}
			modelo.setCoberturas(coberturas);

			String a = contenido.split("Fecha de expedición")[1].split("Forma de pago")[0];
			modelo.setFechaEmision(fn.formatDate(fn.gatos(a).replace("###", "-"), ConstantsValue.FORMATO_FECHA));
			/////////////////////////////////////////////////////////////////////////////////////////////////////////
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
				recibo.setPrimaneta(fn.castBigDecimal(modelo.getPrimaneta(), 2));
				recibo.setDerecho(fn.castBigDecimal(modelo.getDerecho(), 2));
				recibo.setRecargo(fn.castBigDecimal(modelo.getRecargo(), 2));
				recibo.setIva(fn.castBigDecimal(modelo.getDerecho(), 2));

				recibo.setPrimaTotal(fn.castBigDecimal(modelo.getPrimaTotal(), 2));
				recibo.setAjusteUno(fn.castBigDecimal(modelo.getAjusteUno(), 2));
				recibo.setAjusteDos(fn.castBigDecimal(modelo.getAjusteDos(), 2));
				recibo.setCargoExtra(fn.castBigDecimal(modelo.getCargoExtra(), 2));
				recibos.add(recibo);
			}
			modelo.setRecibos(recibos);

			String plazo = "";
			if (contenido.contains(ConstantsValue.ESPECIFICACIONES_PLAN)) {
				plazo = contenido.split(ConstantsValue.ESPECIFICACIONES_PLAN)[1].split(ConstantsValue.AGENTE2)[0].replace("@@@", "")
						.replace("\r\n", "").replace("Observaciones", "").replace("###", "").substring(0, 24)
						.replace("9 m", "").trim();

				if (plazo.contains("Exención de Pago de Prim")) {
					plazo = contenido.split(ConstantsValue.ESPECIFICACIONES_PLAN)[1].split(ConstantsValue.PLAZO)[1]
							.split(ConstantsValue.AGENTE2)[0].replace("@@@", "").replace("\r\n", "");

				} else if (plazo.contains("LaprotecciónContratadase")) {
					plazo = contenido.split(ConstantsValue.ESPECIFICACIONES_PLAN)[1].split(ConstantsValue.PLAZO)[1]
							.split(ConstantsValue.AGENTE2)[0].split("Plan con incrementos")[0].replace("@@@", "").replace("\r\n", "");
				} else {
					plazo = contenido.split(ConstantsValue.ESPECIFICACIONES_PLAN)[1].split(ConstantsValue.AGENTE2)[0]
							.replace("@@@", "").replace("\r\n", "").replace("Observaciones", "").replace("###", "")
							.substring(0, 24).replace("9 m", "").trim();
				}

			} else {
				plazo = contenido.split("E specificaciones del Plan")[1].split("La###protección###Contratada")[0]
						.replace("@@@", "").substring(0, 28).trim();
			}

			if (fn.isNumeric(plazo.replace(ConstantsValue.PLAZO, "").replace("años", "").trim())) {
				modelo.setPlazo(fn.castInteger((plazo.replace(ConstantsValue.PLAZO, "").replace("años", "").trim())));
			}

			if (contenido.contains("Plazo: Edad Alcanzada")) {

				String plazapago = contenido.split("Plazo: Edad Alcanzada")[1].split("Cobertura:s")[0]
						.replace("años", "").replace("\r\n", "").replace(".", "").substring(0, 7);
				modelo.setPlazoPago(fn.castInteger(plazapago.trim()));
				modelo.setRetiro(fn.castInteger(plazapago.trim()));
			}

			if (modelo.getRetiro() > 0) {
				modelo.setTipovida(1);
			} else if (contenido.contains("Supervivencia")) {
				modelo.setTipovida(3);
			} else {
				modelo.setTipovida(2);
			}
			if (contenido.contains("Supervivencia")) {
				modelo.setAportacion(1);
			}
			modelo.setIdCliente(
					contenido.split("Código Cliente")[1].split(ConstantsValue.HASTA_EL)[0].replace("###", "").trim());
			String beneficiarios1 = "";
			inicio = contenido.indexOf("Beneficiarios de Ahorro Garantizado:");
			fin = contenido.lastIndexOf("Para mayor información contáctenos:");
			if (inicio > 0 && fin > 0 && inicio < fin) {
				newcontenido = new StringBuilder();
				newcontenido.append(contenido.substring(inicio, fin + 10).replace("@@@", "").trim());
				if (newcontenido.toString().contains("Protección Contratada") && newcontenido.toString().contains("Para mayor")) {
					beneficiarios1 = newcontenido.toString().split("Protección Contratada")[1].split("Para mayor")[0];
				}
			}

 			String b = "";
			if (!beneficiarios1.contains(" Primas Anuales")) {
				b = beneficiarios1;
			}
			int tip = 0;
			if (modelo.getPlan().equals("Profesional") || modelo.getPlan().equals("Dotal")) {
				tip = 10;

			} else {
				tip = 11;

			}

			List<EstructuraBeneficiariosModel> beneficiarios = new ArrayList<>();
			for (String beneficiariod : b.split("\n")) {
				EstructuraBeneficiariosModel beneficiario = new EstructuraBeneficiariosModel();
				int aseg = beneficiariod.split("###").length;
				if (aseg == 4 && !beneficiariod.contains("Nombre")) {

					beneficiario.setNombre(beneficiariod.split("###")[0].replace("@@@", "").trim());
					beneficiario
							.setNacimiento(fn.formatDate(beneficiariod.split("###")[1], ConstantsValue.FORMATO_FECHA));
					beneficiario.setParentesco(fn.parentesco(beneficiariod.split("###")[2].toLowerCase()));
					beneficiario
							.setPorcentaje(fn.castDouble(beneficiariod.split("###")[3].replace("\r", "")).intValue());
					beneficiario.setTipo(tip);
					beneficiarios.add(beneficiario);
				}
			}

			modelo.setBeneficiarios(beneficiarios);
			return modelo;
		} catch (Exception ex) {
			modelo.setError(
					GnpVidaModel.this.getClass().getTypeName() + " | " + ex.getMessage() + " | " + ex.getCause());
			return modelo;
		}

	}

}
