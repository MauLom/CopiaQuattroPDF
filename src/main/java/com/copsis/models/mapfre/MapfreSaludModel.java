package com.copsis.models.mapfre;

import java.util.ArrayList;
import java.util.List;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraAseguradosModel;
import com.copsis.models.EstructuraBeneficiariosModel;
import com.copsis.models.EstructuraCoberturasModel;
import com.copsis.models.EstructuraJsonModel;
import com.copsis.models.EstructuraRecibosModel;

public class MapfreSaludModel {
	// Clases
	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
	// Variables
	private String inicontenido = "";
	private String contenido = "";
	private String newcontenido = "";
	private String fecha = "";
	private String resultado = "";
	private int inicio = 0;
	private int fin = 0;
	//private int index = 0;
	private int donde = 0;
	//private int longitud_split = 0;
	private int longitud_texto = 0;
	private Boolean encontro = false;

	// constructor
	public MapfreSaludModel(String contenido) {
		this.contenido = contenido;
	}

	public EstructuraJsonModel procesar() {
		inicontenido = fn.fixContenido(contenido);
		contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());
		try {

			modelo.setTipo(3);

			modelo.setCia(22);
			String strb2 = "Póliza Número    :###";
			// poliza
			donde = fn.recorreContenido(contenido, "Póliza Número    :###");
			if (donde == 0) {
				donde = fn.recorreContenido(contenido, "Poliza Numero    :###");
				if (donde > 0) {
					strb2 = "Poliza Numero    :###";
				}
			}

			if (donde > 0) {
				encontro = false;
				for (String dato : contenido.split("@@@")[donde].split("\r\n")) {
					if (dato.contains(strb2)) {
						if (dato.split("###").length >= 2) {
							if (dato.split("###")[1].contains("Endoso")) {

								modelo.setPoliza(dato.split("###")[1].split("Endoso")[0].trim());
								encontro = true;
							}
							if (encontro == false) {
								for (int a = 0; a < dato.trim().split("###").length; a++) {
									if (dato.trim().split("###")[a].contains("Endoso")) {

										modelo.setPoliza(dato.trim().split("###")[a].split("Endoso")[0].trim());
										encontro = true;
									}
									if (encontro == false) {
										if (dato.trim().split("###")[a].contains(strb2) == true) {
											if (a < dato.trim().split("###").length - 1) {

												modelo.setPoliza(dato.trim().split("###")[a + 1].trim());
											}
										}

									}
								}
							}
						}
					}
				}
			} else {
				if (contenido.contains("PÓLIZA/ENDOSO")) {
					modelo.setPoliza(contenido.split("PÓLIZA/ENDOSO")[1].split("###")[1].split("/")[0]);
				}

			}

			if (fn.filtroPorRango(contenido, 6).indexOf("Contratante: ") > -1) {

				donde = fn.recorreContenido(fn.filtroPorRango(contenido, 6), "Contratante: ");
				if (donde > 0) {
					for (String dato : contenido.split("@@@")[donde].split("\r\n")) {

						if (dato.contains("Contratante: ")) {
							// cte_nombre
							inicio = dato.indexOf("Contratante: ") + 13;
							fin = dato.indexOf("R.F.C:");

							modelo.setCteNombre(dato.trim().substring(inicio, fin).trim().replace("###", ""));
							// rfc
							inicio = dato.trim().indexOf("R.F.C:") + 6;
							fin = dato.trim().length();

							modelo.setRfc(dato.substring(inicio, fin).trim().replace("-", ""));
						}
						if (dato.contains("###C.P:")) {
							// cp
							inicio = dato.trim().indexOf("###C.P:") + 8;
							fin = dato.trim().length();

							modelo.setCp(dato.substring(inicio, fin).trim());
						} else if (contenido.split("@@@")[4].split("\r\n").length > 0) {
							for (String linea : contenido.split("@@@")[4].split("\r\n")) {
								if (linea.contains("###C.P:")) {
									if (linea.split("###").length == 2) {

										modelo.setCp(linea.split("###")[1].split(":")[1].trim());
									}
								}
							}
						}
					}
				}
			} else {

				if (contenido.split("@@@").length >= 30) {

					if (fn.filtroPorRango(contenido, 30).indexOf("Contratante: ") > -1) {
						donde = fn.recorreContenido(fn.filtroPorRango(contenido, 30), "Contratante: ");
						if (donde > 0) {
							for (String dato : contenido.split("@@@")[donde].split("\r\n")) {

								if (dato.contains("Contratante: ")) {
									// cte_nombre
									inicio = dato.indexOf("Contratante: ") + 13;
									fin = dato.indexOf("R.F.C:");

									modelo.setCteNombre(dato.trim().substring(inicio, fin).trim().replace("###", ""));

									// rfc
									inicio = dato.trim().indexOf("R.F.C:") + 6;
									fin = dato.trim().length();
									modelo.setRfc(dato.substring(inicio, fin).trim().replace("-", ""));
								}
								if (dato.contains("###C.P. :")) {
									// cp
									inicio = dato.trim().indexOf("###C.P. :") + 9;
									fin = dato.trim().length();
									modelo.setCp(dato.substring(inicio, fin).trim());
								} else if (contenido.split("@@@")[4].split("\r\n").length > 0) {
									for (String linea : contenido.split("@@@")[4].split("\r\n")) {
										if (linea.contains("###C.P. :")) {
											if (linea.split("###").length == 2) {
												modelo.setCp(linea.split("###")[1].split(":")[1].trim());
											}
										}
									}
								}
							}
						}
					} else {

						if (contenido.contains("CONTRATANTE")) {
							int ind = contenido.indexOf("CONTRATANTE");
							String info = contenido.substring(ind, ind + 400);

							for (String a : info.split("\r\n")) {
								//int ga = info.split("###").length;

								if (a.contains("@@@CONTRATANTE:")) {
									modelo.setCteNombre(a.split("###")[1]);

								}
								if (a.contains("R.F.C.")) {
									modelo.setRfc(a.split("###")[1]);
								}
								if (a.contains("C.P.")) {
									modelo.setCp(a.split("###")[1]);
								}

							}

							if (info.contains("DOMICILIO") && info.contains("R.F.C.:") && info.contains("ESTADO")
									&& info.contains("POBLACIÓN")) {
								String dato = "DOMICILIO";
								if (info.indexOf("DOMICILIO:") > 0) {
									dato = "DOMICILIO:";
								} else {
									dato = "DOMICILIO";
								}
								String direccion = info.split(dato)[1].split("R.F.C.:")[0];

								String cte_direccion = direccion.split("###")[1] + " "
										+ direccion.split("ESTADO")[1].split("POBLACIÓN")[0];
								modelo.setCteDireccion(
										cte_direccion.replace("@@@", "").replace("###", "").replace("\r\n", ""));

							} else if (info.contains("DOMICILIO:") && info.contains("R.F.C.:")) {
								String direccion = info.split("DOMICILIO")[1].split("R.F.C.:")[0];
								String cte_direccion = direccion.split("###")[1];
								modelo.setCteDireccion(
										cte_direccion.replace("@@@", "").replace("###", "").replace("\r\n", ""));
							}
						}
					}

				}

			}

			// domicilio
			if (contenido.split("@@@")[4].contains("Domicilio: ")) {
				donde = contenido.split("@@@")[4].trim().split("\r\n").length - 1;
				if (contenido.split("@@@")[4].trim().split("\r\n")[donde].trim().contains("Domicilio: ")
						&& contenido.split("@@@")[4].trim().split("\r\n")[donde].trim().contains("###Tel. :")) {
					inicio = contenido.split("@@@")[4].trim().split("\r\n")[donde].trim().indexOf("Domicilio: ") + 11;
					fin = contenido.split("@@@")[4].trim().split("\r\n")[donde].trim().indexOf("###Tel. :");

					modelo.setCteDireccion(
							contenido.split("@@@")[4].trim().split("\r\n")[donde].trim().substring(inicio, fin));
				}
			} else if (contenido.split("@@@")[5].contains("Domicilio:")) {
				if (contenido.split("@@@")[5].split("\r\n").length == 1) {
					inicio = contenido.split("@@@")[5].trim().split("###")[0].trim().indexOf("Domicilio:") + 10;
					longitud_texto = contenido.split("@@@")[5].trim().split("###")[0].trim().length();
					modelo.setCteDireccion(contenido.split("@@@")[5].trim().split("###")[0].trim()
							.substring(inicio, longitud_texto).trim());
				}
			} else {
				donde = fn.recorreContenido(contenido, "Domicilio: ");
				if (donde > 0) {
					if (contenido.split("@@@")[donde].trim().contains("Domicilio: ")
							&& contenido.split("@@@")[donde].trim().contains("###Tel. :")) {
						inicio = contenido.split("@@@")[donde].trim().indexOf("Domicilio: ") + 11;
						fin = contenido.split("@@@")[donde].trim().indexOf("###Tel. :");

						modelo.setCteDireccion(contenido.split("@@@")[donde].trim().substring(inicio, fin));
					}
				}
			}

			donde = fn.recorreContenido(contenido, "Vigencia Desde las");

			if (donde > 0) {

				if (contenido.split("@@@")[donde].split("\r\n").length == 1) {
					if (contenido.split("@@@")[donde].split("\r\n")[0].split("###").length >= 2) {

						// vigencia_de
						modelo.setVigenciaDe(fn.formatDate(
								contenido.split("@@@")[donde].split("\r\n")[0].split("###")[1].trim(), "dd-MM-yy"));
						if (contenido.split("@@@")[donde + 1].split("\r\n")[0].split("###").length == 4) {
							// vigencia_a
							modelo.setVigenciaA(fn.formatDate(
									contenido.split("@@@")[donde + 1].split("\r\n")[0].split("###")[1].trim(),
									"dd-MM-yy"));
							// agente
							modelo.setAgente(contenido.split("@@@")[donde + 1].split("\r\n")[0].split("###")[3].trim());
							// cve_agente
							modelo.setCveAgente(
									contenido.split("@@@")[donde + 1].split("\r\n")[0].split("###")[2].trim());
						}
					}
				}
			}
			if (modelo.getVigenciaA().length() >= 0) {
				if (contenido.contains("VIGENCIA###HASTA:")) {
					modelo.setVigenciaA(fn.formatDate(
							contenido.split("VIGENCIA###HASTA:")[1].split("CLIENTE")[0].replace("###", ""),
							"dd-MM-yy"));
				} else if (contenido.contains("VIGENCIA###HASTA")) {
					modelo.setVigenciaA(
							fn.formatDate(contenido.split("VIGENCIA###HASTA")[1].split("DEL")[1].split("CLIENTE")[0]
									.replace("###", "").replace(":", ""), "dd-MM-yy"));
				}

			}
			if (modelo.getVigenciaDe().length() >= 0) {
				if (contenido.contains("VIGENCIA###DESDE###LAS")) {
					modelo.setVigenciaDe(
							fn.formatDate(contenido.split("VIGENCIA###DESDE###LAS")[1].split("DEL")[1].split("TIPO")[0]
									.replace("###", "").replace(":", ""), "dd-MM-yy"));
				}

			}

			if (modelo.getAgente().length() >= 0) {

				if (contenido.contains("Nombre del Agente")) {
					modelo.setAgente(contenido.split("Nombre del Agente:")[1].split("\r\n")[1].split("###")[3].trim());
				} else {
					modelo.setAgente(contenido.split("AGENTE")[1].replace(":", "").split("\r\n")[0].trim());
				}

			}
			if (modelo.getCveAgente().length() >= 0) {
				if (contenido.contains("CLAVE DE AGENTE")) {
					modelo.setCveAgente(contenido.split("CLAVE DE AGENTE")[1].replace(":", "").split("\r\n")[0].trim());
				}
			}

			donde = fn.recorreContenido(contenido, "Fecha de Emisión:###No. de Renovación:###");
			if (donde == 0) {
				donde = fn.recorreContenido(contenido, "Fecha de Emision:###No. de Renovacion:###");
			}
			if (donde > 0) {

				if (contenido.split("@@@")[donde + 1].split("\r\n").length == 1) {
					if (contenido.split("@@@")[donde + 1].split("\r\n")[0].split("###").length == 5) {

						modelo.setRenovacion(contenido.split("@@@")[donde + 1].split("\r\n")[0].split("###")[1].trim());

						modelo.setFormaPago(fn
								.formaPago(contenido.split("@@@")[donde + 1].split("\r\n")[0].split("###")[2].trim()));

						modelo.setMoneda(fn.moneda(contenido.split("@@@")[donde + 1].split("\r\n")[0].split("###")[3]
								.replace("$", "").trim()));
					}
				}
			} else {
				donde = fn.recorreContenido(contenido, "Fecha de Emisión:###Forma de Pago:");

				if (donde > 0) {

					modelo.setFormaPago(fn.formaPago(
							contenido.split("@@@")[donde + 1].trim().split("\r\n")[0].trim().split("###")[1].trim()));
					modelo.setMoneda(fn.moneda(contenido.split("@@@")[donde + 1].split("\r\n")[0].split("###")[2]
							.replace("$", "").trim()));
				} else {

				}
			}
			if (modelo.getFormaPago() >= 0) {

				if (contenido.contains("FORMA###DE###PAGO")) {

					modelo.setFormaPago(fn.formaPago(contenido.split("PAGO:")[1].split("###")[1].split("\r\n")[0]));

				}

			}
			if (modelo.getMoneda() >= 0) {
				if (contenido.contains("MONEDA")) {

					modelo.setMoneda(fn.moneda(
							contenido.split("MONEDA:")[1].split("###")[1].split("\r\n")[0].replace("$", "").trim()));
				}
			}
			String primas = "";
			if (contenido.contains("Total:")) {

				primas = contenido.split("Total:")[1].split("Mapfre Tepeyac, S.A")[0].replace("@@@", "").trim();
				for (String m : primas.split("\n")) {
					int tm = m.split("###").length;
					if (tm == 7) {
						modelo.setPrimaneta((fn.castBigDecimal(fn.preparaPrimas(m.split("###")[0]))));
						modelo.setDerecho((fn.castBigDecimal(fn.preparaPrimas(m.split("###")[3]))));
						modelo.setRecargo((fn.castBigDecimal(fn.preparaPrimas(m.split("###")[2]))));
						modelo.setIva((fn.castBigDecimal(fn.preparaPrimas(m.split("###")[5]))));
						modelo.setPrimaTotal((fn.castBigDecimal(fn.preparaPrimas(m.split("###")[6]))));

					}

				}
			} else {

				primas = contenido.split("CONCEPTOS###ECONÓMICOS")[1].split("PADECIMIENTOS EXCLUIDOS")[0]
						.replace("@@@", "").trim();
				for (String m : primas.split("\n")) {
					int tm = m.split("###").length;

					if (tm == 7) {
						if (m.contains("NETA")) {
							modelo.setPrimaneta((fn.castBigDecimal(fn.preparaPrimas(m.split("###")[6]))));
						}
						if (m.contains("I.V.A")) {
							modelo.setIva((fn.castBigDecimal(fn.preparaPrimas(m.split("###")[6].replace("%", "")))));
						}
						if (m.contains("TOTAL")) {

							modelo.setPrimaTotal((fn.castBigDecimal(fn.preparaPrimas(m.split("###")[6].replace("$", "").trim()))));
							if (m.split("###")[3].replace("$", "").trim().contains("|")) {
								String valor = m.split("###")[3].replace("$", "").replace("|", "###");

								modelo.setRecargo(fn.castBigDecimal(fn.preparaPrimas(valor.split("###")[1].trim())));
							} else {
								modelo.setRecargo(
										fn.castBigDecimal(fn.preparaPrimas(m.split("###")[3].replace("$", "").replace("0% |", "").trim())));
							}

						}
					}
					if (tm == 6) {
						if (m.contains("EXPEDICIÓN")) {
							modelo.setDerecho(fn.castBigDecimal(fn.preparaPrimas(m.split("###")[5])));
						}
					}
				}
			}
			// plan

			if (contenido.split("@@@")[1].trim().split("\r\n").length == 2) {
				if (contenido.split("@@@")[1].trim().split("\r\n")[1].trim().split("###")[0].trim().length() > 0) {
					if (contenido.split("@@@")[1].trim().split("\r\n")[1].trim().split("###")[0].trim()
							.contains("Póliza") == false) {
						resultado = contenido.split("@@@")[1].trim().split("\r\n")[1].trim().split("###")[0].trim();
						if (contenido.split("@@@")[2].trim().split("\r\n")[0].trim().split("###")[0].trim()
								.contains("pi n o s ,")) {
							inicio = contenido.split("@@@")[2].trim().split("\r\n")[0].trim().split("###")[0].trim()
									.indexOf("pi n o s ,") + 10;
							longitud_texto = contenido.split("@@@")[2].trim().split("\r\n")[0].trim().split("###")[0]
									.trim().length();
							if ((inicio < contenido.split("@@@")[2].trim().split("\r\n")[0].trim().split("###")[0]
									.trim().length()) && (inicio > -1)) {
							}
						} else if (contenido.split("@@@")[2].trim().split("\r\n")[0].trim().split("###")[0].trim()
								.contains("pinos,")) {
							inicio = contenido.split("@@@")[2].trim().split("\r\n")[0].trim().split("###")[0].trim()
									.indexOf("pinos,") + 6;
							longitud_texto = contenido.split("@@@")[2].trim().split("\r\n")[0].trim().split("###")[0]
									.trim().length();
							if ((inicio < contenido.split("@@@")[2].trim().split("\r\n")[0].trim().split("###")[0]
									.trim().length()) && (inicio > -1)) {
								resultado += " "
										+ contenido.split("@@@")[2].trim().split("\r\n")[0].trim().split("###")[0]
												.trim().substring(inicio, longitud_texto).trim();
							}
						}

						if (resultado.equalsIgnoreCase("GASTOS MÉDICOS HOSPITALARIOS")) {
							modelo.setPlan("GASTOS MÉDICOS HOSPITALARIOS PLUS");
						} else {
							modelo.setPlan(resultado);
						}

					} else {

						if (contenido.split("@@@")[1].trim().split("\r\n")[1].trim().split("###")[0]
								.contains("Póliza Número")) {

							modelo.setPlan("MAPFRE-OCA-DOCTORS");
						}
					}

				}
			} else if (fn.recorreContenido(contenido, "###Folio") > 0) {
				donde = fn.recorreContenido(contenido, "###Folio");

				if (contenido.split("@@@")[donde].split("\r\n")[0].trim().split("###").length == 3) {
					if (contenido.split("@@@")[donde].split("\r\n")[0].contains("Folio")) {
						modelo.setPlan(contenido.split("@@@")[donde].split("\r\n")[0].trim().split("###")[0].trim());
					}
				} else if (contenido.split("@@@")[donde].split("\r\n")[1].trim().split("###").length == 3) {
					if (contenido.split("@@@")[donde].split("\r\n")[1].contains("Folio")) {
						modelo.setPlan(contenido.split("@@@")[donde].split("\r\n")[1].trim().split("###")[0].trim());
					}
				}
			}

			if (modelo.getPlan().length() >= 0) {

				if (contenido.contains("PLAN###CONTRATADO")) {
					modelo.setPlan(contenido.split("PLAN")[1].split("###")[2].split("\r\n")[0] + " "
							+ contenido.split("PLAN")[1].split("###")[2].split("\r\n")[1].replaceAll("@@@", ""));
				}

			}
			List<EstructuraBeneficiariosModel> beneficiarios = new ArrayList<>();

			if (inicontenido.indexOf("ANEXO DE BENEFICIARIOS:") > 0) {

				newcontenido = fn.eliminaSpacios(inicontenido.split("ANEXO DE BENEFICIARIOS:")[1].split("@@@ANEXO ")[0],
						' ', "doble").replace("######", "###");
				for (String ben : newcontenido.split("\n")) {
					EstructuraBeneficiariosModel beneficiario = new EstructuraBeneficiariosModel();
					if (ben.contains("%")) {
						if (ben.contains("BENEFICIARIO")) {
						} else {
							beneficiario.setNombre(ben.split("###")[1]);
							beneficiario.setPorcentaje(Integer.parseInt(ben.split("###")[2].replace("%", "").trim()));
							beneficiario.setTipo(10);
							beneficiarios.add(beneficiario);
						}
					}
				}
				modelo.setBeneficiarios(beneficiarios);
			}

			// asegurados { nombre nacimiento antiguedad sexo }
			donde = fn.recorreContenido(contenido, "RIESGO   NOMBRES");
			List<EstructuraAseguradosModel> asegurados = new ArrayList<>();

			if (donde > 0) {
				for (int i = 0; i < contenido.split("@@@")[donde].split("\r\n").length; i++) {

					if (i > 0) {
						EstructuraAseguradosModel asegurado = new EstructuraAseguradosModel();
						resultado = contenido.split("@@@")[donde].split("\r\n")[i].trim();

						if (resultado.split("###").length == 7) {
							asegurado.setNombre(resultado.split("###")[1]);
							asegurado.setParentesco(fn.parentesco(resultado.split("###")[4].toLowerCase()));
							asegurado.setNacimiento(fn.formatDate_MonthCadena(resultado.split("###")[5]));
							asegurado.setAntiguedad(fn.formatDate_MonthCadena(resultado.split("###")[6]));
							asegurado.setSexo(fn.sexo(resultado.split("###")[2]) ? 1 : 0);
							asegurados.add(asegurado);

						}
					}
				}
				modelo.setAsegurados(asegurados);
			} else if (contenido.split("@@@")[17].split("\r\n").length > 0) {

				if (contenido.split("@@@")[17].split("\r\n")[0].contains("a###a###a###a###a###a###a")) {
					donde = contenido.indexOf("NOMBRE###EDAD###SEXO###EXTRAPRIMA###ANTIGÜEDAD###ANTIGÜEDAD");
					if (donde > 0) {

						String datos = contenido.substring(donde, donde + 400);
						for (String x : datos.split("\r\n")) {
							EstructuraAseguradosModel asegurado = new EstructuraAseguradosModel();
							switch (x.split("###").length) {

							case 5:
								if (fn.isNumeric(x.split("###")[4].trim())) {
									if (x.split("###")[4].trim().length() == 8) {
										fecha = x.split("###")[4].substring(4, 8) + "-"
												+ x.split("###")[4].substring(2, 4) + "-"
												+ x.split("###")[4].substring(0, 2);
									}

									if (fecha.contains("-")) {
										asegurado.setAntiguedad(fecha);
									}
								}

								asegurado.setNombre(x.split("###")[0].replace("@@@", "").trim());
								asegurado.setSexo(fn.sexo(x.split("###")[2].trim().toLowerCase()) ? 1 : 0);

								if (asegurado.getNombre().length() > 10) {
									asegurados.add(asegurado);
									modelo.setAsegurados(asegurados);
								}

								break;

							case 4:
								if (fn.isNumeric(x.split("###")[3].trim())) {
									if (x.split("###")[3].trim().length() == 8) {
										fecha = x.split("###")[4].substring(4, 8) + "-"
												+ x.split("###")[4].substring(2, 4) + "-"
												+ x.split("###")[4].substring(0, 2);
									}

									if (fecha.contains("-")) {
										asegurado.setAntiguedad(fecha);
									}
								}

								asegurado.setNombre(x.split("###")[0].replace("@@@", "").replace("38", "")
										.replace("13", "").trim());
								asegurado.setSexo(fn.sexo(x.split("###")[2].trim()) ? 1 : 0);

								if (asegurado.getNombre().length() > 10) {
									asegurados.add(asegurado);
									modelo.setAsegurados(asegurados);
								}

								break;

							}

						}

					}

				} else {
					String conten = contenido.split("@@@")[17];

					if (conten.contains("%") || conten.contains("OBERTURAS")) {

						donde = contenido.indexOf("NOMBRE###EDAD###SEXO###EXTRAPRIMA###ANTIGÜEDAD###ANTIGÜEDAD");
						if (donde > 0) {
							String datos = contenido.substring(donde, donde + 300);

							for (String x : datos.split("\r\n")) {
								EstructuraAseguradosModel asegurado = new EstructuraAseguradosModel();
								switch (x.split("###").length) {

								case 5:
									if (fn.isNumeric(x.split("###")[4].trim())) {
										if (x.split("###")[4].trim().length() == 8) {
											fecha = x.split("###")[4].substring(4, 8) + "-"
													+ x.split("###")[4].substring(2, 4) + "-"
													+ x.split("###")[4].substring(0, 2);
										}

										if (fecha.contains("-")) {
											asegurado.setAntiguedad(fecha);
										}
									}

									asegurado.setNombre(x.split("###")[0].replace("@@@", "").trim());
									asegurado.setSexo(fn.sexo(x.split("###")[2].trim()) ? 1 : 0);
									asegurados.add(asegurado);
									modelo.setAsegurados(asegurados);
									break;

								case 6:
									if (x.contains("NOMBRE")) {

									} else {
										if (fn.isNumeric(x.split("###")[4].trim())) {
											if (x.split("###")[4].trim().length() == 8) {
												fecha = x.split("###")[4].substring(4, 8) + "-"
														+ x.split("###")[4].substring(2, 4) + "-"
														+ x.split("###")[4].substring(0, 2);
											}
											if (fecha.contains("-")) {
												asegurado.setAntiguedad(fecha);
											}
										}
										asegurado.setNombre(x.split("###")[0].replace("@@@", "").trim());
										asegurado.setSexo(fn.sexo(x.split("###")[2].trim().toLowerCase()) ? 1 : 0);
										asegurados.add(asegurado);
										modelo.setAsegurados(asegurados);
									}

									break;

								}

							}

						}

					} else {

						inicio = contenido.indexOf("ASEGURADO###TITULAR");
						fin = contenido.indexOf("Si###el###contenido###de###la###póliza");

						if (inicio > 0 && fin > 0 && inicio < fin) {
							EstructuraAseguradosModel asegurado = new EstructuraAseguradosModel();
							newcontenido = contenido.substring(inicio, fin).replace("ASEGURADO###TITULAR", "").trim();
							if (newcontenido.split("\r\n")[1].contains("ASEGURADO:")
									&& newcontenido.split("\r\n")[2].contains("SEXO:")) {
								asegurado.setNombre(newcontenido.split("ASEGURADO:")[1].split("SEXO")[0]
										.replace("###", " ").trim());
							}
							if (newcontenido.split("\r\n")[3].contains("NACIMIENTO:")
									&& newcontenido.split("\r\n")[4].contains("EDAD:")) {
								asegurado
										.setNacimiento(fn.formatDate(
												newcontenido.split("NACIMIENTO:")[1].split("EDAD")[0]
														.replace("###", " ").replace("DE", "/").replace(" ", "").trim(),
												"dd-MM-yy"));
							}
							asegurados.add(asegurado);
							modelo.setAsegurados(asegurados);

						}

					}

				}

			}

			if (modelo.getAsegurados().size() == 0) {
				inicio = contenido.indexOf("ASEGURADOS###(Continuación)");
				fin = contenido.lastIndexOf("Av.###Revolució");

				if (inicio > 0 && fin > 0 && inicio < fin) {
					newcontenido = contenido.substring(inicio, fin).replace("ASEGURADOS###(Continuación", "");
					for (String x : newcontenido.split("\n")) {
						EstructuraAseguradosModel asegurado = new EstructuraAseguradosModel();
						int nsp = x.split("###").length;
						if (nsp == 5) {

							asegurado.setNombre(x.split("###")[0].replace("@@@", ""));
							asegurado.setSexo(fn.sexo(x.split("###")[2]) ? 1 : 0);
							asegurados.add(asegurado);
							modelo.setAsegurados(asegurados);
						}

					}

				}

			}

			if (modelo.getAsegurados().size() == 0) {
				inicio = inicontenido.indexOf("ASEGURADOS");
				fin = inicontenido.lastIndexOf("MATERNIDAD");

				if (inicio > 0 && fin > 0 && inicio < fin) {
					newcontenido = inicontenido.substring(inicio, fin).replace("ASEGURADOS", "")
							.replaceAll("  +", "###").trim();
					donde = newcontenido.indexOf("NOMBRE###EDAD###SEXO###EXTRAPRIMA###ANTIGÜEDAD###ANTIGÜEDAD");
					if (donde > 0) {
						newcontenido = newcontenido.substring(donde, newcontenido.indexOf("NETA"));
					}
					for (String x : newcontenido.split("\n")) {
						EstructuraAseguradosModel asegurado = new EstructuraAseguradosModel();
						int nsp = x.split("###").length;
						if (x.contains("FECHA") && x.contains("Av.")) {
						} else {
							if (nsp == 9) {
								asegurado.setNombre(x.split("###")[2]);
								asegurado.setNacimiento(fn.formatDate(x.split("###")[6].replace("/", "-"), "dd-mm-yy"));
								asegurado.setAntiguedad(fn.formatDate(x.split("###")[7].replace("/", "-"), "dd-mm-yy"));
								asegurado.setSexo(fn.sexo(x.split("###")[3]) ? 1 : 0);
								asegurado.setParentesco(fn.parentesco(x.split("###")[5]));
								asegurados.add(asegurado);
								modelo.setAsegurados(asegurados);
							}
						}
					}
				}
			}

			// sa deducible coaseguro, coaseguro_tope
			donde = fn.recorreContenido(contenido, "DEDUCIBLE  COAS         TOPE COAS ZONA HOSPITAL MÍNIMO"); // VALORES
																												// DE
			// NIVEL
			// RAIZ
			if (donde > 0) {

				if (contenido.split("@@@")[donde].trim().split("\r\n").length > 2) {
					if (contenido.split("@@@")[donde].trim().split("\r\n")[2].trim().contains("$")) {
						resultado = contenido.split("@@@")[donde].trim().split("\r\n")[2].trim().trim().replace("$",
								"");

						if (resultado.split("###").length >= 1) {
							modelo.setSa(resultado.split("###")[1]);
							modelo.setDeducible(resultado.split("###")[2]);
							modelo.setCoaseguro(resultado.split("###")[3]);
							modelo.setCoaseguroTope(resultado.split("###")[4]);
						}
					} else if (contenido.split("@@@")[donde].trim().split("\r\n").length >= 4) { // valore a nivel raiz
						resultado = contenido.split("@@@")[donde].trim().split("\r\n")[3].trim().replace("$", "");
						if (resultado.split("###").length >= 1) {
							modelo.setSa(resultado.split("###")[1]);
							modelo.setDeducible(resultado.split("###")[2]);
							modelo.setCoaseguro(resultado.split("###")[3]);
							modelo.setCoaseguroTope(resultado.split("###")[4]);
						}
					}
				}
			} else {

			}

			int inicoberturas = inicontenido.indexOf("MÍNIMO") + 8;
			int fincoberturas = inicontenido.indexOf("ANTERIORES") - 10;

			List<EstructuraCoberturasModel> coberturas = new ArrayList<>();
			if (inicoberturas > -1 && fincoberturas > -1 && inicoberturas < fincoberturas) {

				newcontenido = inicontenido.substring(inicoberturas, fincoberturas).replaceAll("  +", "###").trim();
				for (String datod : newcontenido.split("\n")) {
					EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();
					Integer num = datod.split("###").length;

					if (num == 10) {

						if (datod.split("###")[0].length() == 16) {
							cobertura.setNombre(datod.split("###")[0].replace("\r\n", "").replace("@@@", ""));
							cobertura.setSa(datod.split("###")[1].replace("\r", "").replace("@@@", ""));
							cobertura.setDeducible(datod.split("###")[2] + " " + datod.split("###")[3]);
							cobertura.setCoaseguro(datod.split("###")[4]);
							coberturas.add(cobertura);
						} else if (datod.split("###")[0].length() == 23) {

							cobertura.setNombre(datod.split("###")[0].replace("\r\n", "").replace("@@@", ""));
							cobertura.setSa(datod.split("###")[1].replace("\r", "").replace("@@@", "") + " "
									+ datod.split("###")[2].replace("\r\n", "").replace("@@@", ""));
							cobertura.setDeducible(datod.split("###")[3] + " " + datod.split("###")[4]);
							cobertura.setCoaseguro(datod.split("###")[5]);
							coberturas.add(cobertura);
						} else {

							cobertura.setNombre(datod.split("###")[0].replace("\r\n", "").replace("@@@", ""));
							cobertura.setSa(datod.split("###")[1].replace("\r", "").replace("@@@", ""));
							cobertura.setDeducible(datod.split("###")[3] + " " + datod.split("###")[4]);
							cobertura.setCoaseguro(datod.split("###")[5]);
							coberturas.add(cobertura);

						}

					}
					if (num == 9) {

						if (datod.split("###")[0].length() == 16) {
							cobertura.setNombre(datod.split("###")[0].replace("\r\n", "").replace("@@@", ""));
							cobertura.setSa(datod.split("###")[1].replace("\r", "").replace("@@@", ""));
							cobertura.setDeducible(datod.split("###")[3] + " " + datod.split("###")[4]);
							cobertura.setCoaseguro(datod.split("###")[4]);
							coberturas.add(cobertura);

						} else if (datod.split("###")[0].length() == 27) {
							cobertura.setNombre(datod.split("###")[0].replace("\r\n", "").replace("@@@", ""));
							cobertura.setSa(datod.split("###")[1].replace("\r", "").replace("@@@", ""));
							cobertura.setDeducible(datod.split("###")[3] + " " + datod.split("###")[4]);
							cobertura.setCoaseguro(datod.split("###")[5]);
							coberturas.add(cobertura);
						} else {
							cobertura.setNombre(datod.split("###")[0].replace("\r\n", "").replace("@@@", ""));
							cobertura.setSa(datod.split("###")[1].replace("\r", "").replace("@@@", ""));
							cobertura.setDeducible(datod.split("###")[3] + " " + datod.split("###")[4]);
							cobertura.setCoaseguro(datod.split("###")[5]);
							coberturas.add(cobertura);
						}

					}

					if (num == 8) {

						cobertura.setNombre(datod.split("###")[0].replace("\r\n", "").replace("@@@", ""));
						cobertura.setSa(datod.split("###")[1].replace("\r", "").replace("@@@", ""));
						cobertura.setDeducible(datod.split("###")[2] + " " + datod.split("###")[3]);
						cobertura.setCoaseguro(datod.split("###")[4]);
						coberturas.add(cobertura);
					}
					if (num == 4) {
						cobertura.setNombre(datod.split("###")[0].replace("\r\n", "").replace("@@@", ""));
						cobertura.setSa(datod.split("###")[1].replace("\r", "").replace("@@@", "") + " "
								+ datod.split("###")[2].replace("\r\n", "").replace("@@@", ""));
						coberturas.add(cobertura);
					}
					if (num == 3) {
						cobertura.setNombre(datod.split("###")[0].replace("\r\n", "").replace("@@@", ""));
						cobertura.setSa(datod.split("###")[1].replace("\r", "").replace("@@@", ""));
						coberturas.add(cobertura);
					}
					if (num == 2) {
						cobertura.setNombre(datod.split("###")[0].replace("\r\n", "").replace("@@@", ""));
						cobertura.setSa(datod.split("###")[1].replace("\r", "").replace("@@@", ""));
						coberturas.add(cobertura);
					}
				}
				modelo.setCoberturas(coberturas);
			} else {
				donde = fn.recorreContenido(contenido, "COBERTURAS###Y###SERVICIOS");

				for (int i = 0; i < contenido.split("COBERTURAS###Y###SERVICIOS").length; i++) {
					if (contenido.split("COBERTURAS###Y###SERVICIOS")[i].contains("CONCEPTOS###ECONÓMICOS")) {
						newcontenido += contenido.split("COBERTURAS###Y###SERVICIOS")[i]
								.split("CONCEPTOS###ECONÓMICOS")[0];
					}
					if (contenido.split("COBERTURAS###Y###SERVICIOS")[i].contains("@@@VER ANEXOS:")) {
						newcontenido += contenido.split("COBERTURAS###Y###SERVICIOS")[i].split("@@@VER ANEXOS:")[0];
					}
				}
				for (String x : newcontenido.split("\r\n")) {
					EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();
					int numgatos = x.split("###").length;

					if (x.contains("a###a###a###a###a###a") || x.contains("@@@COBERTURAS###SUMA###DEDUCIBLE")
							|| x.contains("AMPARADAS###ASEGURADA###COASEGURO") || x.contains("(Continuación)")) {
					} else {

						switch (numgatos) {
						case 2:
							cobertura.setNombre(x.split("###")[0].replace("@@@", ""));
							cobertura.setSa(x.split("###")[1].replace("\r\n", "").replace("@@@", ""));
							coberturas.add(cobertura);
							break;
						case 3:
							cobertura.setNombre(x.split("###")[0].replace("@@@", ""));
							cobertura.setSa(x.split("###")[1].replace("\r\n", "").replace("@@@", ""));
							coberturas.add(cobertura);
							break;

						case 5:
							cobertura.setNombre(x.split("###")[0].replace("@@@", ""));
							cobertura.setSa(x.split("###")[1].replace("\r\n", "").replace("@@@", ""));
							cobertura.setDeducible(x.split("###")[2]);
							cobertura.setCoaseguro(x.split("###")[3]);
							coberturas.add(cobertura);
							break;
						case 6:
							cobertura.setNombre(x.split("###")[0].replace("@@@", ""));
							cobertura.setSa(x.split("###")[1].replace("\r\n", "").replace("@@@", ""));
							cobertura.setDeducible(x.split("###")[2]);
							cobertura.setCoaseguro(x.split("###")[3]);
							coberturas.add(cobertura);
							break;
						}

					}
				}
				modelo.setCoberturas(coberturas);

			}

			inicio = contenido.indexOf("Emision");
			String strb = "Emision";
			if (inicio == -1) {
				inicio = contenido.indexOf("Emision:");
				if (inicio > 0) {
					strb = "";
				}
			}

			fin = contenido.indexOf("Prima Neta");
			if (inicio > 0 && fin > 0 && inicio < fin) {
				newcontenido = contenido.split(strb)[1].split("Prima Neta")[0].split("\n")[1].replace("@@@", "###")
						.trim();
				modelo.setFechaEmision(fn.formatDate(newcontenido.split("###")[1].trim(), "dd-MM-yy"));

			}
			if (modelo.getFechaEmision().length() == 0) {
				modelo.setFechaEmision(modelo.getVigenciaDe());
			}

			if (contenido.contains("Cliente MAPFRE")) {
				modelo.setIdCliente(contenido.split("Cliente MAPFRE")[1].split("\r\n")[0].trim());
				modelo.setRenovacion(contenido.split("Moneda")[1].split("###")[1].trim());
			} else {
				modelo.setIdCliente(contenido.split("CLIENTE###MAPFRE")[1].split("\r\n")[0].replace(":###", "").trim());

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
					MapfreSaludModel.this.getClass().getTypeName() + " | " + ex.getMessage() + " | " + ex.getCause());
			return modelo;
		}
	}
}
