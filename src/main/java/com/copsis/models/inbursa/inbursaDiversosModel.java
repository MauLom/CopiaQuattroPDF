package com.copsis.models.inbursa;

import java.util.ArrayList;
import java.util.List;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraCoberturasModel;
import com.copsis.models.EstructuraJsonModel;

public class inbursaDiversosModel {
	// Clases
	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
	// Varaibles
	private String contenido = "";
	private String resultado = "";

	public inbursaDiversosModel(String contenido) {
		this.contenido = contenido;
	}

	public EstructuraJsonModel procesar() {
		boolean iva = false;
		StringBuilder newcontenido = new StringBuilder();
		int inicio = 0;
		int fin = 0;

		contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());
		contenido = contenido.replace("DOLARES", "DÓLARES").replace("R.F.C.", "R.F.C").replace("I.V.A.", "IVA")
				.replace("COBERTURAS CONTRATADAS", "SECCION###COBERTURAS#");
		try {
			// tipo
			modelo.setTipo(7);

			// cia
			modelo.setCia(35);

			inicio = contenido.indexOf("PÓLIZA DE SEGUROS");
			fin = contenido.indexOf("COBERTURAS");
			if (inicio > 0 && fin > 0 && inicio < fin) {
				newcontenido.append(contenido.substring(inicio, fin).replace("\r", "").replace("@", "")
						.replace("las 12:00 hrs. del", ""));
				for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
					if (newcontenido.toString().split("\n")[i].contains("PÓLIZA")
							&& newcontenido.toString().split("\n")[i].contains("CIS")
							&& newcontenido.toString().split("\n")[i].contains("ID CLIENTE")) {
						modelo.setPoliza(newcontenido.toString().split("\n")[i - 1].split("###")[1]);
					} else if (newcontenido.toString().split("\n")[i].contains("AGRUPACIÓN")
							&& newcontenido.toString().split("\n")[i].contains("PÓLIZA")
							&& newcontenido.toString().split("\n")[i].contains("CIS")) {
						modelo.setPoliza(newcontenido.toString().split("\n")[i + 1].split("###")[2]);
						resultado = newcontenido.toString().split("\n")[i + 2];
					}
					if (newcontenido.toString().split("\n")[i].contains("C.P.")
							&& newcontenido.toString().split("\n")[i].contains("R.F.C")) {
						modelo.setCp(newcontenido.toString().split("\n")[i].split("C.P.")[1].split("###")[0].trim());
						if (newcontenido.toString().split("\n")[i + 1].contains("PRIMA NETA")) {
							modelo.setRfc(newcontenido.toString().split("\n")[i + 1].split("###")[0]);
						} else {
							modelo.setRfc(newcontenido.toString().split("\n")[i + 1].split("###")[1]);
						}
						resultado += " " + newcontenido.toString().split("\n")[i].split("R.F.C")[0];
					}

					if (newcontenido.toString().split("\n")[i].contains("PRIMA")
							&& newcontenido.toString().split("\n")[i].contains("NETA")
							&& newcontenido.toString().split("\n")[i].contains("AGRUPACIÓN")) {
						modelo.setPrimaneta(fn.castBigDecimal(fn.castDouble(newcontenido.toString().split("\n")[i + 1]
								.split("###")[newcontenido.toString().split("\n")[i + 1].split("###").length - 1])));

					} else if (newcontenido.toString().split("\n")[i].contains("PRIMA NETA")) {
						if (newcontenido.toString().split("\n")[i].split("###").length > 2) {
							resultado += " " + newcontenido.toString().split("\n")[i].split("PRIMA NETA")[1].split(fn
									.extraerNumeros(newcontenido.toString().split("\n")[i].split("PRIMA NETA")[1]))[1];
							modelo.setPrimaneta(fn.castBigDecimal(fn.castDouble(
									fn.extraerNumeros(newcontenido.toString().split("\n")[i].split("PRIMA NETA")[1]))));
						} else {
							modelo.setPrimaneta(fn.castBigDecimal(fn.castDouble(
									newcontenido.toString().split("\n")[i].split("PRIMA NETA")[1].replace("###", ""))));
						}
						modelo.setCteDireccion(resultado.replace("###", ""));
					}
					// Datos de la direccion alternativa
					if (newcontenido.toString().split("\n")[i].contains("DIRECCIÓN:")) {
						String A = "", B = "", C = "";
						if (newcontenido.toString().split("\n")[i + 1].trim().contains(".00")) {
							A = newcontenido.toString().split("\n")[i + 1].split("SUMA")[0].trim();
						} else {
							A = newcontenido.toString().split("\n")[i + 1].trim();
						}
						if (newcontenido.toString().split("\n")[i + 2].trim().contains("R.F.C:")) {
							B = newcontenido.toString().split("\n")[i + 2].split("R.F.C:")[0].trim();
						} else {
							B = newcontenido.toString().split("\n")[i + 2].trim();
						}
						if (newcontenido.toString().split("\n")[i + 3].trim().contains(".00")) {
							if (newcontenido.toString().split("\n")[i + 3].split("###").length > 2) {
								C = newcontenido.toString().split("\n")[i + 3].split("###")[0].trim();
							} else {
								C = newcontenido.toString().split("\n")[i + 3].split("C.P.")[0].trim();
							}

						} else {
							C = newcontenido.toString().split("\n")[i + 3].split("###")[0].trim();
						}
						String x = A + " " + B + " " + C;
						modelo.setCteDireccion(x.replace("###", "").replaceAll(modelo.getRfc(), ""));
					}

					if (newcontenido.toString().split("\n")[i].contains("MONEDA")
							&& newcontenido.toString().split("\n")[i].contains("EXPEDICIÓN")) {
						modelo.setMoneda(fn.moneda(newcontenido.toString().split("\n")[i + 1].split("###")[0]));
					} else if (newcontenido.toString().split("\n")[i].contains("MONEDA")) {
						modelo.setMoneda(fn.moneda(newcontenido.toString().split("\n")[i + 1]));
					}

					if (newcontenido.toString().split("\n")[i].contains("FINANCIAMIENTO")
							&& newcontenido.toString().split("\n")[i].contains("R.F.C:")) {
						modelo.setRecargo(fn.castBigDecimal(fn.castDouble(newcontenido.toString().split("\n")[i + 1]
								.split("###")[newcontenido.toString().split("\n")[i + 1].split("###").length - 1]
										.replace("###", ""))));
					} else if (newcontenido.toString().split("\n")[i].contains("FINANCIAMIENTO")) {
						modelo.setRecargo(fn.castBigDecimal(fn.castDouble(
								newcontenido.toString().split("\n")[i].split("FINANCIAMIENTO")[1].replace("###", ""))));
					}

					if (newcontenido.toString().split("\n")[i].contains("DERECHO DE PÓLIZA")) {
						modelo.setDerecho(fn.castBigDecimal(
								fn.castDouble(newcontenido.toString().split("\n")[i].split("DERECHO DE PÓLIZA")[1]
										.replace("###", ""))));
					}
					if (newcontenido.toString().split("\n")[i].contains("GASTO DE EXPEDICIÓN")) {
						modelo.setDerecho(fn.castBigDecimal(fn.castDouble(
								newcontenido.toString().split("\n")[i + 1].split("###")[1].replace("###", ""))));
					}
					if (newcontenido.toString().split("\n")[i].contains("IVA")
							&& newcontenido.toString().split("\n")[i].contains("FORMA DE PAGO") && !iva) {
						modelo.setIva(fn.castBigDecimal(fn.castDouble(
								newcontenido.toString().split("\n")[i].split("IVA")[1].split("FORMA DE PAGO")[0]
										.replace("###", ""))));
						if (newcontenido.toString().split("\n")[i + 1].split("###").length > 4) {
							if (newcontenido.toString().split("\n")[i + 1].contains("PRIMA TOTAL")
									&& newcontenido.toString().split("\n")[i + 1].contains("Horas")) {
								String x = newcontenido.toString().split("\n")[i + 1].split("PRIMA TOTAL")[0];
								modelo.setFormaPago(fn.formaPago(x.split("###")[x.split("###").length - 1]));
							}
						} else {
							modelo.setFormaPago(fn.formaPago(newcontenido.toString().split("\n")[i + 2]
									.split("###")[newcontenido.toString().split("\n")[i + 2].split("###").length - 1]));
						}
						iva = true;
					} else if (newcontenido.toString().split("\n")[i].contains("IVA") && !iva) {
						modelo.setIva(fn.castBigDecimal(
								fn.castDouble(newcontenido.toString().split("\n")[i].split("IVA")[1].split("###")[1]
										.replace("###", ""))));
						iva = true;
					}
					if (modelo.getFormaPago() == 0
							&& newcontenido.toString().split("\n")[i].contains("FORMA DE PAGO")) {

						modelo.setFormaPago(fn.formaPago(newcontenido.toString().split("\n")[i + 1].split("###")[0]));

					}

					if (newcontenido.toString().split("\n")[i].contains("PRIMA TOTAL")
							&& newcontenido.toString().split("\n")[i].contains("Desde")) {
						if (newcontenido.toString().split("\n")[i].split("-").length > 2) {
							modelo.setPrimaTotal(fn.castBigDecimal(fn.castDouble(fn.extraerNumeros(
									newcontenido.toString().split("\n")[i].split("PRIMA TOTAL")[1].split("###")[1])
									.replace("###", ""))));
							if (modelo.getPrimaTotal().toString().length() > 0) {
								String x = newcontenido.toString().split("\n")[i].split(fn.extraerNumeros(
										newcontenido.toString().split("\n")[i].split("PRIMA TOTAL")[1].split("###")[1])
										.replace("###", ""))[1];
								modelo.setVigenciaDe(fn.formatDateMonthCadena(x.split("###")[0]));
								modelo.setVigenciaA(fn.formatDateMonthCadena(x.split("###")[1]));
							}
						} else {
							modelo.setPrimaTotal(fn.castBigDecimal(fn.castDouble(
									newcontenido.toString().split("\n")[i].split("PRIMA TOTAL")[1].split("###")[1])));
						}

					} else if (newcontenido.toString().split("\n")[i].contains("PRIMA TOTAL")
							&& newcontenido.toString().split("\n")[i].contains("SUMA ASEGURADA")) {
						modelo.setPrimaTotal(fn.castBigDecimal(fn.castDouble(newcontenido.toString().split("\n")[i + 1]
								.split("###")[newcontenido.toString().split("\n")[i + 1].split("###").length - 1]
										.replace("###", ""))));
					} else if (newcontenido.toString().split("\n")[i].contains("PRIMA TOTAL")) {
						modelo.setPrimaTotal(fn.castBigDecimal(fn.castDouble(
								newcontenido.toString().split("\n")[i].split("PRIMA TOTAL")[1].replace("###", ""))));
					}
					if (newcontenido.toString().split("\n")[i].contains("Desde")
							&& newcontenido.toString().split("\n")[i].contains("Hasta")) {
						if (newcontenido.toString().split("\n")[i + 1].split("###")[0].contains("-")) {
							modelo.setVigenciaDe(fn
									.formatDateMonthCadena(newcontenido.toString().split("\n")[i + 1].split("###")[0]));
							modelo.setVigenciaA(fn
									.formatDateMonthCadena(newcontenido.toString().split("\n")[i + 1].split("###")[1]));
						} else {
							if (newcontenido.toString().split("\n")[i].split("Horas")[1].split("Hasta")[0]
									.contains("-")) {
								modelo.setVigenciaDe(fn.formatDateMonthCadena(
										newcontenido.toString().split("\n")[i].split("Horas")[1].split("Hasta")[0]
												.replace("###", "")));
								modelo.setVigenciaA(fn.formatDateMonthCadena(
										newcontenido.toString().split("\n")[i].split("Hasta")[1].split("Horas")[1]
												.replace("###", "").trim()));
							}

						}

					}

					if (newcontenido.toString().split("\n")[i].contains("PRODUCTO")
							&& newcontenido.toString().split("\n")[i].contains("TIPO")) {
						modelo.setPlan(newcontenido.toString().split("\n")[i].split("###")[1].replace("###", ""));
					} else if (newcontenido.toString().split("\n")[i].contains("PRODUCTO")) {
						modelo.setPlan(newcontenido.toString().split("\n")[i + 1].split("###")[0].replace("###", ""));
					}
				}
			}

			inicio = contenido.indexOf("SECCION###COBERTURAS#");
			fin = contenido.indexOf("COBERTURAS###ADICIONALES");
			if (fin == -1) {
				fin = contenido.indexOf("A###PARTIR");
				if (fin == -1) {
					fin = contenido.indexOf("ZONA TERREMOTO");
					if (fin == -1) {
						fin = contenido.indexOf("ESPECIFICACIÓN DE CONDICIONES");
					}
				}
			}

			if (inicio > 0 && fin > 0 && inicio < fin) {
				List<EstructuraCoberturasModel> coberturas = new ArrayList<>();
				newcontenido.append(contenido.substring(inicio, fin).replace("\r", "").replace("@", "")
						.replace("las 12:00 hrs. del", ""));

				String seccion = "";
				for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
					EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();

					if (newcontenido.toString().split("\n")[i].contains("SECCION###COBERTURAS")
							&& !newcontenido.toString().split("\n")[i].contains("Página")
							&& !newcontenido.toString().split("\n")[i].contains(modelo.getPoliza())
							&& !newcontenido.toString().split("\n")[i].contains("PÓLIZA")) {

						int sp = newcontenido.toString().split("\n")[i].split("###").length;

						if (sp > 2) {
							seccion = newcontenido.toString().split("\n")[i].split("###")[0].replace("SECCIÓN", "")
									.replace("SECCIÓN", "").trim();
						}
						if (sp == 2) {
							if (newcontenido.toString().split("\n")[i].contains("SECCIÓN")) {
								seccion = newcontenido.toString().split("\n")[i].split("###")[0].replace("SECCIÓN", "");
								cobertura.setSeccion(seccion);
								cobertura.setNombre(newcontenido.toString().split("\n")[i].split("###")[1]);
								coberturas.add(cobertura);
							} else {
								cobertura.setSeccion(seccion);
								cobertura.setNombre(
										newcontenido.toString().split("\n")[i].split("###")[0].replace("SECCIÓN", ""));
								cobertura.setSa(newcontenido.toString().split("\n")[i].split("###")[1]);
								coberturas.add(cobertura);
							}

						}
						if (sp == 3) {
							cobertura.setSeccion(seccion);
							cobertura.setNombre(newcontenido.toString().split("\n")[i].split("###")[1]);
							cobertura.setSa(newcontenido.toString().split("\n")[i].split("###")[2]);
							coberturas.add(cobertura);
						}
						if (sp == 4) {
							cobertura.setSeccion(seccion);
							cobertura.setNombre(newcontenido.toString().split("\n")[i].split("###")[1]);
							cobertura.setSa(newcontenido.toString().split("\n")[i].split("###")[2]);
							coberturas.add(cobertura);
						}
					}
				}
				modelo.setCoberturas(coberturas);
			}

			return modelo;
		} catch (Exception ex) {
			modelo.setError(inbursaDiversosModel.this.getClass().getTypeName() + " - catch:" + ex.getMessage() + " | "
					+ ex.getCause());
			return modelo;
		}
	}

}
