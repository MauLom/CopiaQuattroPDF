package com.copsis.models.axa.salud;

import java.util.ArrayList;
import java.util.List;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraAseguradosModel;
import com.copsis.models.EstructuraCoberturasModel;
import com.copsis.models.EstructuraJsonModel;
import com.copsis.models.EstructuraRecibosModel;

public class AxaSaludModel {

	// Clases
	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
	// Varaibles
	private String contenido = "";
	private String newcontenido = "";
	private String newcontenidoEx = "";
	private int inicio = 0;
	private int fin = 0;

	public AxaSaludModel(String contenido) {
		this.contenido = contenido;
	}

	public EstructuraJsonModel procesar() {
		contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());
		contenido = contenido.replace("Datos de la Póliza", "Datos###de###la###Póliza")
				.replace("Coberturas Amparadas", "Coberturas###Amparadas")
				.replace("Familia Asegurada", "Familia###Asegurada").replace("En cumplimiento", "En###cumplimiento#")
				.replace("A B R ", "ABR ").replace("TITULAR M", "###TITULAR###M###")
				.replace("PROTECCION DENTAL SIN COSTO", "PROTECCION DENTAL###SIN COSTO").replace("T###el:", "Tel:")
				.replace("N###om###bre:", "Nombre:").replace("D###om###icilio:", "Domicilio:").replace("C.P.", "C.P:")
				.replace("C###oberturas###Am###paradas", "Coberturas###Amparadas").replace("M###oneda:", "Moneda:")
				.replace("V###igencia###:", "Vigencia:").replace("I.V###.A###:", "I.V.A:")
				.replace("#Prim###as:", "Primas:").replace("T###otal:", "Total:").replace("C###.P:", "C.P:")
				.replace("A###gente:", "Agente:").replace("U###tilidad:", "Utilidad:");
		try {
			modelo.setTipo(3);
			modelo.setCia(20);

			inicio = contenido.indexOf("Póliza");
			fin = contenido.indexOf("Solicitud");

			if (inicio > 0 && fin > 0 && inicio < fin) {
				newcontenido = contenido.split("Póliza:")[1].split("Solicitud")[0].replace("\r\n", "")
						.replace("@@@", "").replace("###", "");
				modelo.setPoliza(newcontenido);
			}

			inicio = contenido.indexOf("POLIZA");
			fin = contenido.indexOf("Contratante");
			if (inicio > 0 && fin > 0 && inicio < fin) {
				newcontenido = contenido.substring(inicio, fin).replace("@@@", "").replace("\r", "");
				for (int i = 0; i < newcontenido.split("\n").length; i++) {
					if (newcontenido.split("\n")[i].contains("Grupo")) {
						modelo.setError("La póliza corresponde al ramo de grupo");
					}
				}
			}

			inicio = contenido.indexOf("Contratante");
			fin = contenido.indexOf("Tel:");
			if (inicio > 0 && fin > 0 && inicio < fin) {
				newcontenido = contenido.substring(inicio, fin);
				for (int i = 0; i < newcontenido.split("\n").length; i++) {

					if (newcontenido.split("\n")[i].contains("Nombre:")
							&& newcontenido.split("\n")[i].contains("RFC")) {
						String x = newcontenido.split("\n")[i].split("Nombre:")[1].split("RFC")[0].replace("\r", "")
								.replace("###", "");
						if (x.contains(",")) {
							modelo.setCteNombre((x.split(",")[1] + " " + x.split(",")[0]).trim());
						} else {
							modelo.setCteNombre(x.trim());
						}
						modelo.setRfc(newcontenido.split("\n")[i].split("RFC")[1].replace(":", "").replace("\r", "")
								.replace("###", ""));
					}
					if (newcontenido.split("\n")[i].contains("Domicilio:")
							&& newcontenido.split("\n")[i].contains("C.P:")) {
						modelo.setCteDireccion((newcontenido.split("\n")[i].split("Domicilio:")[1].split("C.P:")[0]
								+ "  " + newcontenido.split("\n")[i + 1] + "  " + newcontenido.split("\n")[i + 2])
										.replaceAll("###", "").replace("\r", ""));
						modelo.setCp(newcontenido.split("\n")[i].split("C.P:")[1].replace("###", "").replace("\r", ""));
					} else {
						if (newcontenido.split("\n")[i].contains("Domicilio:")) {
							modelo.setCteDireccion((newcontenido.split("\n")[i].split("Domicilio:")[1] + "  "
									+ newcontenido.split("\n")[i + 2]).replaceAll("###", " ").replace("\r", "").trim());
						}
						if (newcontenido.split("\n")[i].contains("C.P:")) {
							modelo.setCp(newcontenido.split("\n")[i].split("C.P:")[1].replace("###", "")
									.replace("Edo:", "").replace("\r", ""));
						}

					}
				}
			}

			inicio = contenido.indexOf("Datos###de###la###Póliza");
			fin = contenido.indexOf("Coberturas###Amparadas");
			if (inicio > 0 && fin > 0 && inicio < fin) {
				newcontenido = contenido.substring(inicio, fin).replace("\r", "");
				for (int i = 0; i < newcontenido.split("\n").length; i++) {
					if (newcontenido.split("\n")[i].contains("Plan") && newcontenido.split("\n")[i].contains("Póliza")
							&& newcontenido.split("\n")[i].contains("Prima")) {
						modelo.setPlan(
								newcontenido.split("\n")[i].split("Póliza:")[1].split("Prima")[0].replace("###", ""));
						modelo.setPrimaneta((fn.castBigDecimal(fn.castDouble(
								newcontenido.split("\n")[i].split("Neta:")[1].replace("###", "").replace(",", "")))));
					}

					if (newcontenido.split("\n")[i].contains("Moneda:")
							&& newcontenido.split("\n")[i].contains("Financiamiento")) {
						modelo.setMoneda(
								fn.moneda(newcontenido.split("\n")[i].split("Moneda:")[1].split("Financiamiento")[0]
										.replace("###", "")));
						modelo.setRecargo(
								(fn.castBigDecimal(fn.castDouble(newcontenido.split("\n")[i].split("Financiamiento:")[1]
										.replace("###", "").replace(",", "")))));
					} else if (newcontenido.split("\n")[i].contains("Moneda:")) {
						modelo.setMoneda(
								fn.moneda(newcontenido.split("\n")[i].split("Moneda:")[1].replace("###", "").trim()));
					}

					if (newcontenido.split("\n")[i].contains("Vigencia")
							&& newcontenido.split("\n")[i].contains("Expedición")) {
						if (newcontenido.split("\n")[i].split("Vigencia:")[1].split("Gastos")[0].replace("###", "")
								.split("-").length == 6) {
							modelo.setVigenciaDe(fn.formatDate_MonthCadena(
									newcontenido.split("\n")[i].split("Vigencia:")[1].split("Gastos")[0]
											.replace("###", "").replace(" - ", "###").split("###")[0]));
							modelo.setVigenciaA(fn.formatDate_MonthCadena(
									newcontenido.split("\n")[i].split("Vigencia:")[1].split("Gastos")[0]
											.replace("###", "").replace(" - ", "###").split("###")[1]));
							modelo.setFechaEmision(modelo.getVigenciaDe());
							modelo.setDerecho(
									(fn.castBigDecimal(fn.castDouble(newcontenido.split("\n")[i].split("Expedición:")[1]
											.replace("###", "").replace(",", "")))));
						}
					} else if (newcontenido.split("\n")[i].contains("Vigencia")
							&& newcontenido.split("\n")[i].contains("Financiamiento")) {
						String x = newcontenido.split("\n")[i].split("Vigencia:")[1].split("Financiamiento")[0]
								.replace("###", "").split("-")[2];
						String x2 = newcontenido.split("\n")[i].split("Vigencia:")[1].split("Financiamiento")[0]
								.replace("###", "").split(x)[1].trim();
						modelo.setVigenciaDe(fn.formatDate_MonthCadena(
								newcontenido.split("\n")[i].split("Vigencia:")[1].split("Financiamiento")[0]
										.replace("###", "").split(x)[0].trim() + "" + x));
						modelo.setVigenciaA(fn.formatDate_MonthCadena(x2.substring(1, x2.length()).replace(" ", "")));
						modelo.setFechaEmision(modelo.getVigenciaDe());
						modelo.setRecargo(
								(fn.castBigDecimal(fn.castDouble(newcontenido.split("\n")[i].split("Financiamiento:")[1]
										.replace("###", "").replace(",", "")))));
					} else if (newcontenido.split("\n")[i].contains("Vigencia")) {
						modelo.setVigenciaDe(fn.formatDate_MonthCadena(newcontenido.split("\n")[i].split("Vigencia:")[1]
								.replace("###", "").replace(" - ", "###").trim().split("###")[0]));
						modelo.setVigenciaA(fn.formatDate_MonthCadena(newcontenido.split("\n")[i].split("Vigencia:")[1]
								.replace("\r", "").replace("###", "").replace("- ", "###").split("###")[1]));
						modelo.setFechaEmision(modelo.getVigenciaDe());
					}

					if (newcontenido.split("\n")[i].contains("Pago")
							&& newcontenido.split("\n")[i].contains("I.V.A:")) {
						modelo.setFormaPago(fn.formaPago(
								newcontenido.split("\n")[i].split("Primas:")[1].split("Prima")[0].replace("###", "")));
						modelo.setIva(
								(fn.castBigDecimal(fn.castDouble(newcontenido.split("\n")[i + 1].split("I.V.A:")[1]
										.replace("###", "").replace(",", "")))));
					} else if (newcontenido.split("\n")[i].contains("Pago")
							&& newcontenido.split("\n")[i].contains("Expedición")) {
						modelo.setFormaPago(fn.formaPago(
								newcontenido.split("\n")[i].split("Primas:")[1].split("Gastos")[0].replace("###", "")));
						modelo.setIva(
								(fn.castBigDecimal(fn.castDouble(newcontenido.split("\n")[i + 2].split("I.V.A:")[1]
										.replace("###", "").replace(",", "")))));
						modelo.setDerecho(
								(fn.castBigDecimal(fn.castDouble(newcontenido.split("\n")[i].split("Expedición:")[1]
										.replace("###", "").replace(",", "")))));
					}

					if (newcontenido.split("\n")[i].contains("Total:")) {
						modelo.setPrimaTotal((fn.castBigDecimal(fn.castDouble(
								newcontenido.split("\n")[i].split("Total:")[1].replace("###", "").replace(",", "")))));
					}

					if (newcontenido.split("\n")[i].contains("Suma Asegurada:")) {

						modelo.setSa(newcontenido.split("\n")[i].split("Suma Asegurada:")[1].replace("###", ""));
						
					}
					if (newcontenido.split("\n")[i].contains("Deducible:") && newcontenido.split("\n")[i].contains("Coaseguro:")) {
						modelo.setDeducible(newcontenido.split("\n")[i].split("Deducible:")[1].split("Coaseguro:")[0].replace("###", ""));
						modelo.setCoaseguro(newcontenido.split("\n")[i].split("Coaseguro:")[1].replace("###", ""));
					}
					
					
				}
			}

			inicio = contenido.indexOf("Agente:");
			fin = contenido.indexOf("Utilidad:");
			if (inicio > 0 && fin > 0 && inicio < fin) {
				newcontenido = contenido.substring(inicio, fin);
				for (int i = 0; i < newcontenido.split("\n").length; i++) {
					if (newcontenido.split("\n")[i].contains("Agente:")) {
						modelo.setCveAgente(newcontenido.split("\n")[i].split("###")[1]);
					}

				}

			}

			// Proceso de Asegurados
			inicio = contenido.indexOf("Datos###del###Asegurado");
			fin = contenido.indexOf("Datos###de###la###Póliza");

			List<EstructuraAseguradosModel> asegurados = new ArrayList<>();

			if (inicio > 0 && fin > 0 && inicio < fin) {
				EstructuraAseguradosModel asegurado = new EstructuraAseguradosModel();
				newcontenido = contenido.substring(inicio, fin);
				for (int i = 0; i < newcontenido.split("\n").length; i++) {

					if (newcontenido.split("\n")[i].contains("Nombre:")
							&& newcontenido.split("\n")[i].contains("Parentesco:")) {
						newcontenidoEx = newcontenido.split("\n")[i].split("Nombre:")[1].split("Parentesco")[0];
						if (newcontenido.contains(",")) {
							newcontenidoEx = (newcontenidoEx.split(",")[1] + " " + newcontenidoEx.split(",")[0])
									.replace("###", "");
							asegurado.setNombre(newcontenidoEx);
						} else {
							newcontenidoEx = newcontenidoEx.replace("###", "");
							asegurado.setNombre(newcontenidoEx);
						}
						asegurado.setParentesco(fn.parentesco(newcontenido.split("Parentesco:")[1]));
					}
					if (newcontenido.split("\n")[i].contains("Nacimiento:")
							&& newcontenido.split("\n")[i].contains("Edad:")) {
						newcontenidoEx = newcontenido.split("\n")[i].split("Nacimiento:")[1].split("Edad:")[0]
								.replace("###", "");
						asegurado.setNacimiento(fn.formatDate_MonthCadena(newcontenidoEx));
					}
				}
				asegurados.add(asegurado);
				modelo.setAsegurados(asegurados);
			}

			// proceso de Asegurados version 2
			if (modelo.getAsegurados().size() == 0) {
				inicio = contenido.indexOf("Familia###Asegurada");
				fin = contenido.indexOf("En###cumplimiento#");
				if (fin == -1) {
					fin = contenido.indexOf("Endosos###contenidos###en###la###Póliza");
				}
				if (inicio > 0 && fin > 0 && inicio < fin) {
					newcontenido = contenido.substring(inicio, fin).replace(" - ", "-");
					for (int i = 0; i < newcontenido.split("\n").length; i++) {
						EstructuraAseguradosModel asegurado = new EstructuraAseguradosModel();

						if (newcontenido.split("\n")[i].split("-").length > 2) {

							if (newcontenido.split("\n")[i].split("###")[newcontenido.split("\n")[i].split("###").length
									- 1].trim().split("-").length > 3) {
								String numero = newcontenido.split("\n")[i]
										.split("###")[newcontenido.split("\n")[i].split("###").length - 1].trim()
												.split("-")[2].split(" ")[0];
								asegurado.setNacimiento(fn.formatDate_MonthCadena(newcontenido.split("\n")[i]
										.split("###")[newcontenido.split("\n")[i].split("###").length - 1].trim()
												.split(numero)[0].replace(" ", "")
										+ numero));
								asegurado.setAntiguedad(fn.formatDate_MonthCadena(newcontenido.split("\n")[i]
										.split("###")[newcontenido.split("\n")[i].split("###").length - 1].trim()
												.split(" ")[4]));
							} else {

								asegurado.setAntiguedad(fn.formatDate_MonthCadena(newcontenido.split("\n")[i]
										.split("###")[newcontenido.split("\n")[i].split("###").length - 1].trim()));
								if (newcontenido.split("\n")[i]
										.split("###")[newcontenido.split("\n")[i].split("###").length - 3].trim()
												.contains("-")) {
									asegurado.setNacimiento(fn.formatDate_MonthCadena(newcontenido.split("\n")[i]
											.split("###")[newcontenido.split("\n")[i].split("###").length - 3].trim()));
								} else {
									asegurado.setNacimiento(fn.formatDate_MonthCadena(newcontenido.split("\n")[i]
											.split("###")[newcontenido.split("\n")[i].split("###").length - 4].trim()));
								}
//                                
							}

							if (newcontenido.split("\n")[i].split(newcontenido.split("\n")[i]
									.split("###")[newcontenido.split("\n")[i].split("###").length - 4])[0]
											.split("###")[0].contains(",")) {
								String x = newcontenido.split("\n")[i].split(newcontenido.split("\n")[i]
										.split("###")[newcontenido.split("\n")[i].split("###").length - 4])[0]
												.split("###")[0];
								asegurado.setNombre(x.split(",")[1] + " " + x.split(",")[0]);
								String x2 = newcontenido.split("\n")[i].split(newcontenido.trim().split("\n")[i]
										.split("###")[newcontenido.split("\n")[i].split("###").length - 4])[0].trim();

								if (x2.split("###").length > 1) {

									asegurado.setSexo(fn.sexo(x2.split("###")[1].split(" ")[1].trim()) ? 1 : 0);

									asegurado.setParentesco(fn.parentesco(x2.split("###")[1].trim()));
								} else {
									if (newcontenido.split("\n")[i].contains("TITULAR")) {
										asegurado.setParentesco(1);
										asegurado.setSexo(
												fn.sexo(newcontenido.split("\n")[i].split("###")[1].split(" ")[1].trim()
														.toLowerCase()) ? 1 : 0);

									}
								}

							} else {

								if (newcontenido.split("\n")[i].contains(",")) {
									asegurado.setNombre(newcontenido.split("\n")[i].split("###")[0].split(",")[1] + ""
											+ newcontenido.split("\n")[i].split("###")[0].split(",")[0]);
									asegurado.setParentesco(
											fn.parentesco(newcontenido.split("\n")[i].split("###")[1].trim()));
									asegurado.setSexo(
											fn.sexo(newcontenido.split("\n")[i].split("###")[2].trim()) ? 1 : 0);

								}
							}

							asegurados.add(asegurado);
						}

					}

					modelo.setAsegurados(asegurados);
				}

			}

			// proceso de Asegurados version 2
			if (modelo.getAsegurados().size() == 0) {
				inicio = contenido.indexOf("Familia###Asegurada");
				fin = contenido.indexOf("En###cumplimiento#");
				if (fin == -1) {
					fin = contenido.indexOf("Endosos###contenidos###en###la###Póliza");
				}
				if (inicio > 0 && fin > 0 && inicio < fin) {
					newcontenido = contenido.substring(inicio, fin);
					for (int i = 0; i < newcontenido.split("\n").length; i++) {
						EstructuraAseguradosModel asegurado = new EstructuraAseguradosModel();
						if (newcontenido.split("\n")[i].split("-").length > 2) {
							asegurado.setAntiguedad(fn.formatDate_MonthCadena(newcontenido.split("\n")[i]
									.split("###")[newcontenido.split("\n")[i].split("###").length - 1].trim()));
							asegurado.setNacimiento(fn.formatDate_MonthCadena(newcontenido.split("\n")[i]
									.split("###")[newcontenido.split("\n")[i].split("###").length - 4].trim()));
							if (newcontenido.split("\n")[i].split(newcontenido.split("\n")[i]
									.split("###")[newcontenido.split("\n")[i].split("###").length - 4])[0]
											.split("###")[0].contains(",")) {
								String x = newcontenido.split("\n")[i].split(newcontenido.split("\n")[i]
										.split("###")[newcontenido.split("\n")[i].split("###").length - 4])[0]
												.split("###")[0];
								asegurado.setNombre(x.split(",")[1] + " " + x.split(",")[0]);
								String x2 = newcontenido.split("\n")[i].split(newcontenido.trim().split("\n")[i]
										.split("###")[newcontenido.split("\n")[i].split("###").length - 4])[0].trim();
								if (x2.split("###").length > 1) {

									asegurado.setSexo(fn.sexo(x2.split("###")[1].split(" ")[1].trim()) ? 1 : 0);

									asegurado.setParentesco(fn.parentesco(x2.split("###")[1].trim()));
								}
							}
							asegurados.add(asegurado);
						}
					}
					asegurados.get(0).setNombre(modelo.getCteNombre());
					asegurados.get(0).setParentesco(1);
					modelo.setAsegurados(asegurados);
				}

			}

//			/**proceso para cober*/

			List<EstructuraCoberturasModel> coberturas = new ArrayList<>();

			inicio = contenido.indexOf("Coberturas###Amparadas");
			fin = contenido.indexOf("Advertencia:");
			if (fin == -1) {
				fin = contenido.indexOf("Se hace del conocimient");
			}

			if (inicio > 0 && fin > 0 && inicio < fin) {
				newcontenido = contenido.substring(inicio, fin).replace("@@@", "");
				for (int i = 0; i < newcontenido.split("\n").length; i++) {
					EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();
					if (newcontenido.split("\n")[i].contains("Coberturas###Amparadas")) {

					} else {
						int sp = newcontenido.split("\n")[i].split("###").length;
						if (sp == 2) {
							cobertura.setNombre(newcontenido.split("\n")[i].split("###")[0]);
							cobertura.setSa(newcontenido.split("\n")[i].split("###")[1].replace("\r", ""));
							coberturas.add(cobertura);
						}

					}
				}
				modelo.setCoberturas(coberturas);
			}

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
				recibo.setPrimaneta(modelo.getPrimaneta());
				recibo.setDerecho(modelo.getDerecho());
				recibo.setRecargo(modelo.getRecargo());
				recibo.setIva(modelo.getDerecho());

				recibo.setPrimaTotal(modelo.getPrimaTotal());
				recibo.setAjusteUno(modelo.getAjusteUno());
				recibo.setAjusteDos(modelo.getAjusteDos());
				recibo.setCargoExtra(modelo.getCargoExtra());
				recibos.add(recibo);

				break;
			}
			modelo.setRecibos(recibos);

			return modelo;
		} catch (Exception ex) {
			modelo.setError(
					AxaSaludModel.this.getClass().getTypeName() + " | " + ex.getMessage() + " | " + ex.getCause());
			return modelo;
		}
	}

	private String filter(String contenido, String caracteres, int quienes_no) { // FILTRA POR MEDIANTE UNA CONDICION
																					// CON ITERADOR
		String result = "";
		for (int i = 0; i < contenido.split(caracteres).length; i++) {
			if (i > quienes_no) {
				if (contenido.split(caracteres).length == i) {
					result += contenido.split(caracteres)[i].trim();
				} else {
					result += contenido.split(caracteres)[i].trim() + " ";
				}
			}
		}
		return result;
	}

}
