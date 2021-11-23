package com.copsis.models.banorte;

import java.util.ArrayList;
import java.util.List;

import com.copsis.constants.ConstantsValue;
import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraAseguradosModel;
import com.copsis.models.EstructuraJsonModel;

public class BanorteSaludModel {
	// Clases
	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
	// Varaibles
	private String contenido = "";
	private String newcontenido = "";
	private String recibosText = "";
	private String coberturas;

	public BanorteSaludModel(String contenido, String recibos, String coberturas) {
		this.contenido = contenido;
		this.recibosText = recibos;
		this.coberturas = coberturas;
	}

	public EstructuraJsonModel procesar() {
		contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());
		contenido = contenido.replace("I.V.A.", ConstantsValue.IVA3)
				.replace("N o m b r e y A p e l l i d o C o m p l e t o", "Nombre y Apellido Completo")
				.replace("Si### el### contenido", "Si el contenido");
		StringBuilder resultado = new StringBuilder();
		int inicio = 0;
		int fin = 0;

		try {
			// tipo
			modelo.setTipo(3);

			// cia
			modelo.setCia(35);

			// Datos Generales
			inicio = contenido.indexOf("GASTOS MÉDICOS MAYORES");
			fin = contenido.indexOf("NOMBRE DEL ASEGURADO");

			if (inicio > 0 && fin > 0 && inicio < fin) {
				newcontenido = contenido.substring(inicio, fin).replace("\r", "").replace("@", "");
				for (int i = 0; i < newcontenido.split("\n").length; i++) {
					if (newcontenido.split("\n")[i].contains("CONTRATANTE")
							&& newcontenido.split("\n")[i].contains("PÓLIZA")) {
						if (newcontenido.split("\n")[i + 1].contains("apellido")) {
							modelo.setPoliza(newcontenido.split("\n")[i + 1]
									.split("###")[newcontenido.split("\n")[i + 1].split("###").length - 1].trim());
							modelo.setCteNombre(newcontenido.split("\n")[i + 2]
									.split("###")[newcontenido.split("\n")[i + 2].split("###").length - 4]
											.replace("10", "").trim());
						} else if (newcontenido.split("\n")[i + 1].contains("Apellido")) {
							modelo.setPoliza(newcontenido.split("\n")[i + 1]
									.split("###")[newcontenido.split("\n")[i + 1].split("###").length - 1].trim());
							String x = newcontenido.split("\n")[i + 1]
									.split("###")[newcontenido.split("\n")[i + 1].split("###").length - 4]
											.replace("Nombre y Apellido Completo :", "").trim();
							modelo.setCteNombre(x);
						} else {
							modelo.setPoliza(newcontenido.split("\n")[i + 1]
									.split("###")[newcontenido.split("\n")[i + 1].split("###").length - 1].trim());

							if (newcontenido.split("\n")[i + 1]
									.split("###")[newcontenido.split("\n")[i + 1].split("###").length - 3]
											.length() > 20) {
								modelo.setCteNombre(newcontenido.split("\n")[i + 1]
										.split("###")[newcontenido.split("\n")[i + 1].split("###").length - 3]
												.replace("10", "").trim());
							} else {
								modelo.setCteNombre(newcontenido.split("\n")[i + 1]
										.split("###")[newcontenido.split("\n")[i + 1].split("###").length - 4]
												.replace("10", "").trim());

							}

						}
					}

					if (newcontenido.split("\n")[i].contains("AGENTE")
							&& newcontenido.split("\n")[i + 1].contains(ConstantsValue.DOMICILIO)) {

						if (newcontenido.split("\n")[i + 1].split("###").length > 2) {
							modelo.setCveAgente(newcontenido.split("\n")[i + 1].split(ConstantsValue.DOMICILIO)[1]
									.split("MATRIZ")[0].replace("###", ""));
						} else {
							modelo.setCveAgente(newcontenido.split("\n")[i + 2].split("###")[1].replace("###", ""));
						}

					}

					if (newcontenido.split("\n")[i].contains("C.P.")) {
						if (newcontenido.split("\n")[i].split("C.P.")[1].split("###").length > 1) {
							modelo.setCp(newcontenido.split("\n")[i].split("C.P.")[1].split("###")[0]);
						} else {
							modelo.setCp(newcontenido.split("\n")[i].split("C.P.")[1].split(",")[0]);
						}

					}
					if (newcontenido.split("\n")[i].contains("EMISIÓN") && modelo.getCp().length() == 0
							&& fn.isvalidCp(newcontenido.split("\n")[i + 1].split("###")[0]).booleanValue()) {

						modelo.setCp(newcontenido.split("\n")[i + 1].split("###")[0]);
					}
					if (newcontenido.split("\n")[i].contains("PLAN")) {

						if (newcontenido.split("\n")[i + 1].contains(ConstantsValue.PESOS_MAYUS)
								|| newcontenido.split("\n")[i + 1].contains(ConstantsValue.NACIONAL_MAYUS)) {
							modelo.setPlan(newcontenido.split("\n")[i + 1]
									.split("###")[newcontenido.split("\n")[i + 1].split("###").length - 1]);
						} else if (newcontenido.split("\n")[i + 2].contains(ConstantsValue.NACIONAL_MAYUS)) {
							String valor = newcontenido.split("\n")[i + 2].replace(" ", "###");
							modelo.setPlan(valor.split("###")[valor.split("###").length - 1]);
						}

					}

					if (newcontenido.split("\n")[i].contains(ConstantsValue.DOMICILIO)) {

						if (newcontenido.split("\n")[i + 1].contains(ConstantsValue.MONEDA_MAYUS)) {
							resultado.append(newcontenido.split("\n")[i + 1].split(ConstantsValue.MONEDA_MAYUS)[0]
									.replace("###", ""));
						} else {
							resultado.append(newcontenido.split("\n")[i + 1].split("###")[0]);
						}
						if (newcontenido.split("\n")[i + 2].contains("C.P.")) {
							resultado.append(" ").append(newcontenido.split("\n")[i + 2].split("C.P.")[1]);

						} else if (newcontenido.split("\n")[i + 2].contains(ConstantsValue.MONEDA_MAYUS)) {
							resultado.append(" ").append(newcontenido.split("\n")[i + 2].split(ConstantsValue.MONEDA_MAYUS)[0]
									.replace("###", ""));
							
						} else {
							resultado.append(" ").append(newcontenido.split("\n")[i + 2]);

						}

						if (newcontenido.split("\n")[i + 3].contains(ConstantsValue.NACIONAL_MAYUS)) {
							resultado.append(newcontenido.split("\n")[i + 3].split(ConstantsValue.NACIONAL_MAYUS)[0]) ;
							
							modelo.setMoneda(1);

							if (newcontenido.split("\n")[i + 3].split(ConstantsValue.NACIONAL_MAYUS)[1]
									.replace(ConstantsValue.TOTAL_MAYUS , "").trim().split("###").length > 2) {
								modelo.setFormaPago(fn.formaPago(
										newcontenido.split("\n")[i + 3].split(ConstantsValue.NACIONAL_MAYUS)[1]
												.split("###")[1].replace(ConstantsValue.TOTAL_MAYUS, "").trim()));
							} else {
								modelo.setFormaPago(fn.formaPago(
										newcontenido.split("\n")[i + 3].split(ConstantsValue.NACIONAL_MAYUS)[1]
												.replace(ConstantsValue.TOTAL_MAYUS, "").trim()));
							}

						}
						if (newcontenido.split("\n")[i + 3].contains(ConstantsValue.PESOS_MAYUS)) {
							resultado.append(" ").append(newcontenido.split("\n")[i + 3].split(ConstantsValue.PESOS_MAYUS)[0].replace("###", ""));
							modelo.setMoneda(1);
							modelo.setFormaPago(fn.formaPago(
									newcontenido.split("\n")[i + 3].split(ConstantsValue.PESOS_MAYUS)[1].split("###")[1]
											.replace("###", "").trim()));
						}
						modelo.setCteDireccion(resultado.toString().replace("###", "").trim());
					}

					if (newcontenido.split("\n")[i].contains(ConstantsValue.RFC)
							&& newcontenido.split("\n")[i].contains("VIGENCIA")) {
						modelo.setRfc(newcontenido.split("\n")[i].split(ConstantsValue.RFC)[1].split("VIGENCIA")[0]
								.replace("###", "").trim());

					}

					if (newcontenido.split("\n")[i].contains(ConstantsValue.RFC)
							&& newcontenido.split("\n")[i].contains("DESDE")) {
						modelo.setRfc(newcontenido.split("\n")[i].split(ConstantsValue.RFC)[1].split("DESDE")[0]
								.replace("###", "").trim());
					}

					if (newcontenido.split("\n")[i].contains("Electrónico:")
							&& newcontenido.split("\n")[i].split("-").length > 1) {
						resultado = new StringBuilder();
						resultado.append(newcontenido.split("\n")[i].split("Electrónico:")[1].strip().trim().replace(" ",
								"###"));
						if (resultado.toString().split("###")[0].contains("-")) {
							modelo.setVigenciaDe(fn.formatDateMonthCadena(resultado.toString().split("###")[0]));
							modelo.setVigenciaA(fn.formatDateMonthCadena(resultado.toString().split("###")[1]));
						} else {
							modelo.setVigenciaDe(fn.formatDateMonthCadena(resultado.toString().split("###")[1]));
							modelo.setVigenciaA(fn.formatDateMonthCadena(resultado.toString().split("###")[2]));
						}

					}
					if (newcontenido.split("\n")[i].contains(ConstantsValue.RFC)
							&& newcontenido.split("\n")[i].split("-").length > 3) {
						if (newcontenido.split("\n")[i].split("###")[1].contains("-")) {
							modelo.setRfc(
									newcontenido.split("\n")[i].split("###")[0].replace(ConstantsValue.RFC, "").trim());
							resultado = new StringBuilder();
							resultado.append(newcontenido.split("\n")[i].split("###")[1].trim().replace(" ", "###"));
							modelo.setVigenciaDe(fn.formatDateMonthCadena(resultado.toString().split("###")[0]));
							modelo.setVigenciaA(fn.formatDateMonthCadena(resultado.toString().split("###")[1]));
						} else {
							modelo.setRfc(newcontenido.split("\n")[i].split("###")[1]);
							modelo.setVigenciaDe(fn.formatDateMonthCadena(newcontenido.split("\n")[i].split("###")[2]));
							modelo.setVigenciaA(fn.formatDateMonthCadena(newcontenido.split("\n")[i].split("###")[3]));
						}
					}

					if (modelo.getVigenciaA().length() == 0 && modelo.getVigenciaDe().length() == 0
							&& newcontenido.split("\n")[i].split("-").length > 3) {

						String x = newcontenido.split("\n")[i].trim().replace(" ", "###");
						modelo.setVigenciaDe(fn.formatDateMonthCadena(x.split("###")[0]));
						modelo.setVigenciaA(fn.formatDateMonthCadena(x.split("###")[1]));

					}
				}

			}

			inicio = contenido.indexOf("Clave del Agente:");
			fin = contenido.indexOf("En testimonio de");
			if (inicio > 0 && fin > 0 && inicio < fin) {
				newcontenido = contenido.substring(inicio, fin).replace("\r", "").replace("@", "");
				for (int i = 0; i < newcontenido.split("\n").length; i++) {
					if (newcontenido.split("\n")[i].contains("Clave del Agente:")) {
						modelo.setAgente(newcontenido.split("\n")[i + 1].split(modelo.getCveAgente())[0].trim());
					}
				}
			}

			inicio = contenido.indexOf("Prima Neta");
			fin = contenido.indexOf("Si el contenido");

			if (inicio > 0 && fin > 0 && inicio < fin) {
				newcontenido = contenido.substring(inicio, fin).replace("\r", "").replace("@", "");
				for (int i = 0; i < newcontenido.split("\n").length; i++) {
					if (newcontenido.split("\n")[i].contains("Prima Neta")) {
						modelo.setPrimaneta(fn.castBigDecimal(fn.castDouble(
								newcontenido.split("\n")[i].split("Prima Neta Anual")[1].split("###")[1].trim())));
					}
					if (newcontenido.split("\n")[i].contains(ConstantsValue.FRACCIONADO)
							&& newcontenido.split("\n")[i].contains("Especificaciones")) {
						modelo.setRecargo(fn.castBigDecimal(fn.castDouble(
								newcontenido.split("\n")[i].split(ConstantsValue.FRACCIONADO)[1].replace("###", "").trim())));
					}
					if (newcontenido.split("\n")[i].contains("Financiamiento")
							&& newcontenido.split("\n")[i].contains("Especificaciones")) {
						modelo.setRecargo(fn.castBigDecimal(fn.castDouble(newcontenido.split("\n")[i].split("Pago")[1]
								.replace(ConstantsValue.FRACCIONADO, "").replace("###", "").trim())));
					}

					if (newcontenido.split("\n")[i].contains("Generales")
							&& newcontenido.split("\n")[i].contains("Expedición")) {
						modelo.setDerecho(fn.castBigDecimal(fn
								.castDouble(newcontenido.split("\n")[i].split("Póliza")[1].replace("###", "").trim())));
					}
					if (newcontenido.split("\n")[i].contains("Identificación")
							&& newcontenido.split("\n")[i].contains("Expedición")) {
						modelo.setDerecho(fn.castBigDecimal(fn
								.castDouble(newcontenido.split("\n")[i].split("Póliza")[1].replace("###", "").trim())));
					}

					if (newcontenido.split("\n")[i].contains("Pago")
							&& newcontenido.split("\n")[i].contains(ConstantsValue.IVA3)) {
						modelo.setIva(fn.castBigDecimal(fn.castDouble(
								newcontenido.split("\n")[i].split(ConstantsValue.IVA3)[1].replace("###", "").trim())));
					}

					if ((newcontenido.split("\n")[i].contains("Endosos")
							&& newcontenido.split("\n")[i].contains(ConstantsValue.TOTAL2))
							|| newcontenido.split("\n")[i].contains(ConstantsValue.TOTAL2)) {

						modelo.setPrimaTotal(fn.castBigDecimal(
								fn.castDouble(newcontenido.split("\n")[i].split(ConstantsValue.TOTAL2)[1]
										.replace("###", "").trim())));

					}
				}

			}

			if (modelo.getVigenciaDe().length() > 0) {
				modelo.setFechaEmision(modelo.getVigenciaDe());
			}

			// PROCESO DE ASEGURADOS
			inicio = contenido.indexOf("NOMBRE DEL ASEGURADO");
			fin = contenido.indexOf("seguro de gastos médicos,");

			if (inicio > 0 && fin > 0 && inicio < fin) {
				List<EstructuraAseguradosModel> asegurados = new ArrayList<>();
				newcontenido = contenido.substring(inicio, fin).replace("\r", "").replace("@", "");
				for (int i = 0; i < newcontenido.split("\n").length; i++) {
					EstructuraAseguradosModel asegurado = new EstructuraAseguradosModel();
					if (newcontenido.split("\n")[i].split("-").length > 2) {
						if (newcontenido.split("\n")[i].split("###")[1].contains("-")) {
							asegurado.setNacimiento(
									fn.formatDateMonthCadena(newcontenido.split("\n")[i].split("###")[1]));
							asegurado.setNombre(newcontenido.split("\n")[i].split("###")[0]);
						} else {
							String x = newcontenido.split("\n")[i].split("###")[0].replace(" ", "###");
							asegurado
									.setNacimiento(fn.formatDateMonthCadena(x.split("###")[x.split("###").length - 1]));
							asegurado.setNombre(newcontenido.split("\n")[i].split("###")[0]
									.split(x.split("###")[x.split("###").length - 1])[0]);
						}
						asegurados.add(asegurado);
					}
				}
				modelo.setAsegurados(asegurados);
			}

			inicio = newcontenido.indexOf("");
			fin = newcontenido.indexOf("");

			return modelo;
		} catch (Exception ex) {
			modelo.setError(BanorteSaludModel.this.getClass().getTypeName() + " - catch:" + ex.getMessage() + " | "
					+ ex.getCause());
			return modelo;
		}

	}

}
