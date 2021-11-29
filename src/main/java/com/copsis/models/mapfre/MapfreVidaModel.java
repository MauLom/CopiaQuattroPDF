package com.copsis.models.mapfre;

import java.util.ArrayList;
import java.util.List;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraAseguradosModel;
import com.copsis.models.EstructuraBeneficiariosModel;
import com.copsis.models.EstructuraCoberturasModel;
import com.copsis.models.EstructuraJsonModel;
import com.copsis.models.EstructuraRecibosModel;

public class MapfreVidaModel {
	// Clases
	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
	// Variables
	private String inicontenido;
	private String contenido;
	private String newcontenido;
	private String resultado;
	private int inicio;
	private int fin;
	private int index;
	private int donde;
	private int longitud_split;

	// constructor
	public MapfreVidaModel(String contenido) {
		this.contenido = contenido;
	}

	public EstructuraJsonModel procesar() {
		inicontenido = fn.fixContenido(contenido);
		contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());

		try {
			modelo.setTipo(5);
			modelo.setCia(22);
			String aed = "";
			if (contenido.contains("Endoso Número")) {
				aed = "Endoso Número";
			} else {
				aed = "Endoso";
			}

			modelo.setEndoso(contenido.split(aed)[1].split("\r\n")[0].replace(":", "").replace("###", "").trim());

			// poliza
			donde = 0;
			donde = fn.recorreContenido(contenido, "Nú m er o     :");
			if (donde == 0) {
				donde = fn.recorreContenido(contenido, "Póliza Número    :");
			}
			if (donde == 0) {
				donde = fn.recorreContenido(contenido, "Póliza Número    :");
			}
			if (donde == 0) {
				donde = fn.recorreContenido(contenido, "N ú  me  ro      :");
			}
			if (donde > 0) {
				for (String dato : contenido.split("@@@")[donde].split("\r\n")) {

					if (dato.contains("Nú m er o     :")) {
						modelo.setPoliza(dato.split("Nú m er o     :")[1].trim().replace("###", ""));
					} else if (dato.split("###").length == 2 && dato.contains("Póliza")) {
						if (dato.contains("Número    :")) {
							modelo.setPoliza(dato.split("Número    :")[1].trim().replace("###", ""));
						}
						if (dato.contains("N ú  me  ro")) {
							if (dato.split("###").length == 4) {
								modelo.setPoliza(dato.split("###")[3].trim().replace("###", ""));
							}
						}
					} else if (dato.split("###").length == 3 && dato.contains("Póliza")) {
						if (dato.split("###")[1].trim().equals("Póliza Número    :")) {

							modelo.setPoliza(dato.split("###")[2].trim().replace("###", ""));
						}
					} else if (dato.split("###").length == 4) {
						if (dato.split("###")[2].trim().equals("Póliza Número    :")) {
							modelo.setPoliza(dato.split("###")[3].trim());
						} else if (dato.split("###")[2].contains("N ú  me  ro      :")) {
							modelo.setPoliza(dato.split("###")[3].trim().replace("###", ""));
						}
					}
				}
			}

			if (modelo.getPoliza().toString().length() == 0) {
				inicio = contenido.indexOf("México.*");
				fin = contenido.indexOf("*###Original");
				if (inicio > -1 && fin > -1) {
					modelo.setPoliza(contenido.substring(inicio + 8, fin).replace("###", ""));
				}
			}
			String st = "";
			if (contenido.indexOf("Cliente Mapfre") > 0) {
				st = "Cliente Mapfre";
			} else {
				st = "Cliente MAPFRE";
			}
			String dcl = "";
			if (contenido.split(st)[1].split("Contratante")[0].contains("Tel.")) {
				dcl = "Tel.";
			} else {
				dcl = "Contratante";
			}
			newcontenido = contenido.split(st)[1].split(dcl)[0].replace("@@@", "").replace(":", "").replace("###", "")
					.replace("\r\nPóliza Grupo", "").trim();
			if (newcontenido.length() > 100) {
				modelo.setIdCliente(newcontenido.split("Tel")[0].replace("\r\n", ""));
			} else {
				modelo.setIdCliente(newcontenido);
			}

			// cte_nombre
			donde = 0;
			donde = fn.searchTwoTexts(contenido, "Contratante:", "R.F.C");
			if (donde == 0) {
				donde = fn.searchTwoTexts(contenido, "tratante:", "R.F.C");
			}
			if (donde == 0) {
				donde = fn.searchTwoTexts(contenido, "tratante:", "R.F.C:");
			}
			if (donde > 0) {
				for (String dato : contenido.split("@@@")[donde].split("\r\n")) {
					if (dato.split("###").length == 2) {
						if (dato.split("###")[0].contains("Contratante:")) {
							if (dato.split("###")[0].contains("CURP")) {
								modelo.setCteNombre(
										dato.split("###")[0].trim().split("tante:")[1].trim().split("CURP")[0].trim());
							} else {
								modelo.setCteNombre(dato.split("###")[0].trim().split("tante:")[1].trim());
							}
						}
					} else if (dato.contains("Contratante:") && dato.contains("CURP")) {
						modelo.setCteNombre(dato.split("Contratante:")[1].trim().split("CURP")[0].trim());
					} else if (dato.split("###").length == 3) {
						if (dato.split("###")[0].contains("Contratante:") && dato.split("###")[0].length() > 20) {
							modelo.setCteNombre(dato.split("###")[0].split("tante:")[1].trim());
						}
					} else if (dato.contains("Contratante:") && dato.contains("C.U.R.P")) {
						modelo.setCteNombre(
								dato.split("tante:")[1].trim().split("C.U.")[0].trim().replace("###", " ").trim());
					} else if (dato.contains("tratante:") && dato.split("###").length == 6) {
						modelo.setCteNombre(dato.split("tratante")[1].split("###")[0].trim());
					}
				}
			}
			if (modelo.getCteNombre().toString().length() == 0) {
				donde = 0;
				donde = fn.searchTwoTexts(contenido, "Contratante:", "C.U.R.P");
				if (donde > 0) {
					if (contenido.split("@@@")[donde].split("\r\n").length == 0) {
						if (contenido.split("@@@")[donde].trim().split("###").length == 2) {
							if (contenido.split("@@@")[donde].trim().split("###")[0].indexOf("te:") > -1) {
								modelo.setCteNombre(contenido.split("@@@")[donde].trim().split("###")[0].split("te:")[1]
										.replace(":", "").trim());
							}
						}
					}
				}
			}

			// cte_direccion
			donde = 0;
			donde = fn.searchTwoTexts(contenido, "Domicilio:", "Tel");
			if (donde == 0) {
				donde = fn.searchTwoTexts(contenido, "Dom.:", "Tel");
			}
			if (donde == 0) {
				donde = fn.searchTwoTexts(contenido, "tratante:", "Domicilio:");
			}
			if (donde == 0) {
				donde = fn.searchTwoTexts(contenido, "cilio:", "CT.e");
			}
			if (donde > 0) {
				for (String dato : contenido.split("@@@")[donde].split("\r\n")) {
					if (dato.contains("Domicilio:")) {
						switch (dato.split("###").length) {
						case 2:
							if (dato.split("###")[0].contains("Domicilio:")) {
								modelo.setCteDireccion(dato.split("###")[0].split("Domicilio:")[1].trim());
							} else if (dato.contains("Dom.:")) {
								if (dato.split("###").length == 2) {
									if (dato.split("###")[0].contains("Dom.:")) {
										modelo.setCteDireccion(dato.split("###")[0].split("Dom.:")[1].trim());
									}
								}
							}
							break;
						case 3:
							if (dato.split("###")[0].contains("Domicilio:")) {
								modelo.setCteDireccion(dato.split("###")[0].split("cilio:")[1].trim());
							}
							break;
						case 4:
							if (dato.split("###")[0].equals("Domicilio:")) {
								modelo.setCteDireccion(dato.split("###")[1].trim());
							}
							break;
						case 5:
							if (dato.split("###").length > 3) {
								if (dato.split("###")[0].trim().equals("Domicilio:")) {
									modelo.setCteDireccion(dato.split("###")[1].trim());
								}
							}
							break;
						}
					} else if (dato.contains("Dom.:")) {
						if (dato.split("###").length == 2) {
							if (dato.split("###")[0].contains("Dom.:")) {

								modelo.setCteDireccion(dato.split("###")[0].split("Dom.:")[1].trim());
							}
						}
					} else if (dato.split("###").length == 6) {
						if (dato.split("###")[3].contains("cilio:")) {
							modelo.setCteDireccion(dato.split("###")[4].trim());
						}
					}
				}
			}

			// rfc
			donde = 0;
			donde = fn.recorreContenido(contenido, "Contratante:");
			if (donde > 0) {
				for (String dato : contenido.split("@@@")[donde].split("\r\n")) {
					if (dato.contains("R.F.C.:")) {
						switch (dato.split("###").length) {
						case 2:
							if (dato.split("###")[0].contains("R.F.C.:")) {
								modelo.setRfc(dato.split("###")[0].split("R.F.C.:")[1].trim().replace("-", ""));
							}
							break;
						}
					} else if (dato.contains("R.F.C:")) {
						switch (dato.split("###").length) {
						case 3:
							if (dato.split("###")[dato.split("###").length - 1].contains("R.F.C:")) {
								modelo.setRfc(dato.split("###")[dato.split("###").length - 1].split("C:")[1].trim()
										.replace("-", ""));
							}
							break;
						case 2:
							if (dato.contains("R.F.C:")) {
								modelo.setRfc(dato.split(".C:")[1].trim());
							}
							break;
						}
					}
				}
			}

			if (modelo.getRfc().toString().length() == 0) {
				donde = 0;
				donde = fn.searchTwoTexts(contenido, "Asegurado:###", "###R.F.C:###");
				if (donde == 0) {
					donde = fn.searchTwoTexts(contenido, "Contratante:", "R.F.C:");
				}
				if (donde > 0) {
					if (contenido.split("@@@")[donde].split("\r\n").length == 4) {
						if (contenido.split("@@@")[donde].split("\r\n")[1].contains("###R.F.C:")
								&& contenido.split("@@@")[donde].split("\r\n")[1].split("###").length == 6) {
							if (contenido.split("@@@")[donde].split("\r\n")[1].split("###")[2].trim()
									.equals("R.F.C:")) {
								modelo.setRfc(contenido.split("@@@")[donde].split("\r\n")[1].split("###")[3].trim()
										.replace("-", ""));
							}
						}
					} else if (contenido.split("@@@")[donde].split("\r\n").length == 3) {

						if (contenido.split("@@@")[donde].split("\n")[0].split("###").length == 6) {
							modelo.setRfc(contenido.split("@@@")[donde].split("\n")[0].split("###")[3]);
						}
					} else if (contenido.split("@@@")[donde].split("\r\n").length == 1) {
						if (contenido.split("@@@")[donde].split("###").length == 6) {
							modelo.setRfc(contenido.split("@@@")[donde].split("###")[3].trim().replace("-", ""));
						} else if (contenido.split("@@@")[donde].split("###").length == 2) {
							if (contenido.split("@@@")[donde].split("###")[1].contains("R.F.C:")) {
								modelo.setRfc(contenido.split("@@@")[donde].split("###")[1].split("R.F.C:")[1].trim()
										.replace("-", ""));
							}
						} else {
							for (String dato : contenido.split("@@@")[donde].split("\r\n")) {
								if (dato.contains("R.F.C:") && dato.split("###").length == 7) {
									modelo.setRfc(dato.split("###")[6].trim().replace("-", ""));
								}
							}
						}
					}
				}
			}

			// cp
			donde = 0;
			if (fn.recorreContenido(contenido, "C.P:") > 0) {
				donde = fn.recorreContenido(contenido, "C.P:");
			} else if (fn.recorreContenido(contenido, "C.P. :") > 0) {
				donde = fn.recorreContenido(contenido, "C.P. :");
			}
			if (donde > 0) {
				for (String dato : contenido.split("@@@")[donde].split("\r\n")) {
					if (dato.contains("C.P:") && dato.split("###").length == 2) {
						if (dato.split("###")[1].contains("C.P:")) {
							resultado = dato.split("###")[1].split("C.P:")[1].trim();
							if (fn.isNumeric(resultado)) {
								modelo.setCp(resultado);
							}
						}
					} else if (dato.contains("C.P. :") && dato.split("###").length == 2) {
						if (dato.split("###")[1].contains("C.P. :")) {
							resultado = dato.split("###")[1].split("C.P. :")[1].trim();
							if (fn.isNumeric(resultado)) {
								modelo.setCp(resultado);
							}
						}
					}
				}
			}

			// vigencia_de
			donde = 0;
			donde = fn.recorreContenido(contenido, "Asegurados       ");
			if (donde == 0) {
				donde = fn.recorreContenido(contenido, "Vigencia###Desde");
			}
			if (donde == 0) {
				donde = fn.recorreContenido(contenido, "Desde las 12:00 hrs. de:");
			}
			if (donde > 0) {
				for (String dato : contenido.split("@@@")[donde].split("\r\n")) {
					if (dato.contains("Vigencia Desde")) {
						switch (dato.split("###").length) {
						case 3:
							if (dato.split("###")[0].split("-").length == 3 && dato.split("###")[0].contains("de:")) {
								modelo.setVigenciaDe(
										fn.formatDate(dato.split("###")[0].split("de:")[1].trim(), "dd-MM-yy"));
							} else if (dato.split("###")[1].split("-").length == 3
									&& dato.split("###")[1].trim().length() == 10) {
								modelo.setVigenciaDe(fn.formatDate(dato.split("###")[1].trim(), "dd-MM-yy"));
							}
							break;
						}
					} else {
						if (modelo.getVigenciaDe().length() == 0) {
							for (int i = 0; i < dato.split("###").length; i++) {
								if (dato.split("###")[i].split("-").length == 3) {
									if (dato.split("###")[i].trim().contains("de:")) {
										modelo.setVigenciaDe(
												fn.formatDate(dato.split("###")[i].split("de:")[1].trim(), "dd-MM-yy"));
									} else {
										modelo.setVigenciaDe(fn.formatDate(dato.split("###")[i].trim(), "dd-MM-yy"));
									}
								} else {
									if (dato.split("###").length == 1 && dato.contains("Desde:")
											&& dato.contains("Hasta")) {
									}
								}
							}
						} else {
							break;
						}
					}
				}
			}

			// vigencia_a
			donde = 0;
			if (fn.recorreContenido(contenido, "Hasta las") > 0) {
				donde = fn.recorreContenido(contenido, "Hasta las");
			} else if (fn.recorreContenido(contenido, "Hasta###las") > 0) {
				donde = fn.recorreContenido(contenido, "Hasta###las");
			}
			if (donde > 0) {
				for (String dato : contenido.split("@@@")[donde].split("\r\n")) {
					if (dato.contains("Hasta las") && dato.split("###").length == 3) {
						if (dato.split("###")[0].split("-").length == 3 && dato.split("###")[0].contains("de:")) {
							modelo.setVigenciaA(fn.formatDate(dato.split("###")[0].split("de:")[1].trim(), "dd-MM-yy"));
						}
					} else if (dato.contains("Hasta las") && dato.split("###").length == 4) {
						if (dato.split("###")[0].split("-").length == 3 && dato.split("###")[0].contains("de:")) {
							modelo.setVigenciaA(fn.formatDate(dato.split("###")[0].split("de:")[1].trim(), "dd-MM-yy"));
						} else if (dato.split("###")[1].split("-").length == 3
								&& dato.split("###")[1].trim().length() == 10) {
							modelo.setVigenciaA(fn.formatDate(dato.split("###")[1].trim(), "dd-MM-yy"));
						}
					} else {
						for (int i = 0; i < dato.split("###").length; i++) {
							if (dato.split("###")[i].split("-").length == 3) {
								modelo.setVigenciaA(fn.formatDate(dato.split("###")[i].trim(), "dd-MM-yy"));
							}
						}
					}
				}
			}

			// moneda
			// forma_pago
			donde = 0;
			if (fn.recorreContenido(contenido, "Forma de Pago:") > 0) {
				donde = fn.recorreContenido(contenido, "Forma de Pago:");
			}
			if (donde > 0) {
				if (contenido.split("@@@")[donde].split("\r\n").length == 1) {
					if (contenido.split("@@@")[donde].contains("Forma de Pago:")) {

						switch (contenido.split("@@@")[donde].split("###").length) {
						case 3:
						case 4:

							if (contenido.split("@@@")[donde + 1].split("###").length == 4
									|| contenido.split("@@@")[donde + 1].split("###").length == 5) {

								if (contenido.split("@@@")[donde + 1].split("###")[1].trim().length() > 0) {

									if (contenido.split("@@@")[donde + 1].split("###")[2].contains("PESOS")) {
										modelo.setFormaPago(
												fn.formaPago(contenido.split("@@@")[donde + 1].split("###")[1].trim()));
										modelo.setMoneda(fn.moneda(contenido.split("@@@")[donde + 1].split("###")[2]
												.replace("$", "").trim()));
									} else {
										modelo.setFormaPago(
												fn.formaPago(contenido.split("@@@")[donde + 1].split("###")[2].trim()));
										modelo.setMoneda(fn.moneda(contenido.split("@@@")[donde + 1].split("###")[3]
												.replace("$", "").trim()));
									}

								} else {
									modelo.setFormaPago(
											fn.formaPago(contenido.split("@@@")[donde + 1].split("###")[1].trim()));
									modelo.setMoneda(fn.moneda(
											contenido.split("@@@")[donde + 1].split("###")[2].replace("$", "").trim()));
								}

							}
							break;
						case 5:
							if (contenido.split("@@@")[donde + 1].split("###").length == 4
									|| contenido.split("@@@")[donde + 1].split("###").length == 5) {
								modelo.setFormaPago(
										fn.formaPago(contenido.split("@@@")[donde + 1].split("###")[1].trim()));
								modelo.setMoneda(fn.moneda(
										contenido.split("@@@")[donde + 1].split("###")[2].replace("$", "").trim()));
							}
							break;
						}
					}
				} else {
					switch (contenido.split("@@@")[donde + 1].split("\r\n")[0].split("###").length) {
					case 5:
						if (contenido.split("@@@")[donde + 1].split("\r\n")[0].split("###").length == 5) {
							modelo.setFormaPago(fn.formaPago(
									contenido.split("@@@")[donde + 1].split("\r\n")[0].split("###")[1].trim()));
							modelo.setMoneda(fn
									.moneda(contenido.split("@@@")[donde + 1].split("\r\n")[0].split("###")[1].trim()));
						}
						break;
					}
				}
			}

			inicio = contenido.indexOf("Prima Total:");
			if (inicio > -1) {
				newcontenido = contenido.substring(inicio + 15, inicio + 200).trim().split("\r\n")[0].replace("@@", "")
						.replace("@@@", "").trim();

				switch (newcontenido.split("###").length) {
				case 7:
					resultado = fn.cleanString(newcontenido.split("###")[0].replace("@@@", "").trim());
					if (fn.isNumeric(resultado)) {
						modelo.setPrimaneta(fn.castBigDecimal(resultado));
					}
					resultado = fn.cleanString(newcontenido.split("###")[2]);
					if (fn.isNumeric(resultado)) {
						modelo.setRecargo(fn.castBigDecimal(resultado));
					}
					resultado = fn.cleanString(newcontenido.split("###")[3]);
					if (fn.isNumeric(resultado)) {
						modelo.setDerecho(fn.castBigDecimal(resultado));
					}
					resultado = fn.cleanString(newcontenido.split("###")[5]);
					if (fn.isNumeric(resultado)) {
						modelo.setIva(fn.castBigDecimal(resultado));
					}
					resultado = fn.cleanString(newcontenido.split("###")[6]);
					if (fn.isNumeric(resultado)) {
						modelo.setPrimaTotal(fn.castBigDecimal(resultado));
					}
					break;

				case 5:
					resultado = fn.cleanString(newcontenido.split("###")[0].replace("@@@", "").trim());
					if (fn.isNumeric(resultado)) {
						modelo.setPrimaneta(fn.castBigDecimal(resultado));
					}

					resultado = newcontenido.split("###")[1].trim();
					if (resultado.split("###").length == 2) {
						resultado = fn.cleanString(resultado.split("###")[1]);
						if (fn.isNumeric(resultado)) {
							modelo.setRecargo(fn.castBigDecimal(resultado));
						}
					}
					resultado = fn.cleanString(newcontenido.split("###")[2].trim());
					if (fn.isNumeric(resultado)) {
						modelo.setDerecho(fn.castBigDecimal(resultado));
					}
					resultado = newcontenido.split("###")[3].trim();
					if (resultado.split("###").length == 2) {
						resultado = fn.cleanString(resultado.split("###")[1]);
						if (fn.isNumeric(resultado)) {
							modelo.setIva(fn.castBigDecimal(resultado));
						}
					}
					resultado = fn.cleanString(newcontenido.split("###")[4].trim());
					if (fn.isNumeric(resultado)) {
						modelo.setPrimaTotal(fn.castBigDecimal(resultado));
					}
					break;
				}
			}

			// plan
			donde = 0;
			donde = fn.recorreContenido(contenido, "PLAN DE SEGURO:");
			if (donde == 0) {
				donde = fn.recorreContenido(contenido, "Plan de Seguro:");
			}
			if (donde > 0) {
				if (contenido.split("@@@")[donde].split("\r\n").length == 1) {
					if (contenido.split("@@@")[donde].contains("PLAN DE SEGURO:")) {
						modelo.setPlan(contenido.split("@@@")[donde].split("SEGURO:")[1].trim());
					} else if (contenido.split("@@@")[donde].contains("Plan de Seguro:")) {
						modelo.setPlan(contenido.split("@@@")[donde].split("Seguro:")[1].trim());
					}
				} else {
					if (contenido.split("@@@")[donde].split("\r\n").length == 2) {
						for (String reglon : contenido.split("@@@")[donde].split("\r\n")) {
							if (reglon.split("###").length == 2) {
								if (reglon.split("###")[1].contains("Póliza Número")) {
									modelo.setPlan(reglon.split("###")[0].trim());
								}
							} else if (reglon.split("###").length == 1) {
								if (reglon.contains("P ól i za")) {
									modelo.setPlan(reglon.split("###")[0].trim());
								}
							}
						}
					}

					if (modelo.getPlan().toString().length() == 0) {
						for (String dato : contenido.split("@@@")[donde].split("\r\n")) {
							if (dato.contains("Plan de Seguro:")) {
								modelo.setPlan(dato.split("Seguro:")[1].trim());
							}
						}
					}

				}
			}
			// agente
			// cve_agente
			donde = 0;
			if (fn.recorreContenido(contenido, "Clave de Agente:###") > 0) {
				donde = fn.recorreContenido(contenido, "Clave de Agente:###");
			} else if (fn.recorreContenido(contenido, "Clave de Agente:") > 0) {
				donde = fn.recorreContenido(contenido, "Clave de Agente:");
			}
			if (donde > 0) {
				if (contenido.split("@@@")[donde].split("\r\n").length == 1) {
					if (contenido.split("@@@")[donde].contains("Clave de Agente:")
							&& contenido.split("@@@")[donde].split("###").length == 3) {
						if (contenido.split("@@@")[donde + 1].split("\r\n").length == 1) {
							if (contenido.split("@@@")[donde + 1].split("###").length == 3) {
								modelo.setAgente(contenido.split("@@@")[donde + 1].split("###")[2].trim());
								;
								modelo.setCveAgente(contenido.split("@@@")[donde + 1].split("###")[1].trim());
							} else if (contenido.split("@@@")[donde + 1].split("###").length == 4) {
								modelo.setAgente(contenido.split("@@@")[donde + 1].split("###")[3].trim());
								modelo.setCveAgente(contenido.split("@@@")[donde + 1].split("###")[2].trim());
							}
						}
					} else {
						if (contenido.split("@@@")[donde].contains("Clave de Agente:")) {
							for (int i = 0; i < contenido.split("@@@")[donde + 1].split("###").length; i++) {
								if (contenido.split("@@@")[donde + 1].split("###")[i].split("/").length == 3) {
									if ((i + 1) < contenido.split("@@@")[donde + 1].split("###").length) {
										modelo.setCveAgente(
												contenido.split("@@@")[donde + 1].split("###")[i + 1].trim());
										modelo.setAgente(contenido.split("@@@")[donde + 1].split("###")[i + 2].trim());
									}
								}
							}
						}
					}
				} else {
					for (int i = 0; i < contenido.split("@@@")[donde].split("\r\n").length; i++) {
						if (i == 1) {
							if (contenido.split("@@@")[donde].split("\r\n")[i].contains("Asegurado:") == false) {
								modelo.setCveAgente(
										contenido.split("@@@")[donde].split("\r\n")[i].split("###")[1].trim());
								modelo.setAgente(contenido.split("@@@")[donde].split("\r\n")[i].split("###")[2].trim());
							}
						}
					}

					longitud_split = contenido.split("@@@")[donde].split("\r\n").length - 1;
					if (contenido.split("@@@")[donde].split("\r\n")[longitud_split].contains("Nombre del Agente")
							&& contenido.split("@@@")[donde].split("\r\n")[longitud_split]
									.contains("Clave de Agente:")) {
						if (contenido.split("@@@")[donde + 1].split("\r\n")[0].split("###").length == 4) {
							modelo.setCveAgente(
									contenido.split("@@@")[donde + 1].split("\r\n")[0].split("###")[2].trim());
							modelo.setAgente(contenido.split("@@@")[donde + 1].split("\r\n")[0].split("###")[3].trim());
						}
					}
				}
			}

			// fecha_emision
			inicio = contenido.indexOf("Gestor de Cobro:");
			if (inicio > -1) {
				newcontenido = contenido.substring(inicio + 16, inicio + 200).trim().split("\r\n")[0].replace("@@@", "")
						.trim();
				switch (newcontenido.split("###").length) {
				case 4:
				case 5:
					modelo.setFechaEmision(fn.formatDate(newcontenido.split("###")[0].trim(), "dd-MM-yy"));
					break;
				}
			}

			// curp
			inicio = contenido.indexOf("C.U.R.P.:");
			if (inicio > -1) {
				newcontenido = contenido.substring(inicio + 9, inicio + 150).trim().split("\r\n")[0];
				if (newcontenido.contains("R.F.C")) {
					newcontenido = fn.gatos(newcontenido.split("R.F.C")[0]);
					modelo.setCurp(newcontenido);
				}
			}

			if (contenido.contains("TRADICIONAL")) {
				modelo.setTipovida(2);
			}

			// asegurados { nombre nacimiento antiguedad sexo }
			inicio = contenido.indexOf("Edad     Parentesco");

			index = 19;
			fin = contenido.lastIndexOf("Prima Neta");

			List<EstructuraAseguradosModel> asegurados = new ArrayList<>();
			if (inicio > -1 && fin > inicio) {
				newcontenido = contenido.substring(inicio + index, fin).replace("@@@", "").replace("@@", "").trim();
				for (String a : newcontenido.split("\n")) {

					EstructuraAseguradosModel asegurado = new EstructuraAseguradosModel();
					switch (a.split("###").length) {
					case 4:

						asegurado.setNacimiento(fn.formatDate(a.split("###")[1].trim(), "dd-MM-yy"));
						asegurado.setNombre(a.split("###")[0].trim().replace("###", ""));
						asegurado.setSexo(1);
						asegurado.setParentesco(
								fn.parentesco(a.split("###")[3].replace("(A)", "").trim().toLowerCase()));
						asegurados.add(asegurado);
						break;
					}
				}
				modelo.setAsegurados(asegurados);
			}

			if (modelo.getAsegurados().size() == 0) {
				if (fn.recorreContenido(contenido, "Asegurado:") > 0) {
					donde = fn.recorreContenido(contenido, "Asegurado:");
				}
				if (donde > 0) {
					for (String dato : contenido.split("@@@")[donde].split("\r\n")) {
						EstructuraAseguradosModel asegurado = new EstructuraAseguradosModel();
						if (dato.contains("Asegurado:") && dato.contains("R.F.C:")) {
							asegurado.setNombre(
									dato.split("Asegurado:")[1].trim().split("R.F.C:")[0].replace("###", "").trim());
							if (dato.split("###").length == 2) {
								if (dato.split("###")[1].contains("Nac:")) {
									asegurado.setNacimiento(
											fn.formatDate(dato.split("###")[1].split("Nac:")[1].trim(), "dd-MM-yy"));
								}
							}
							if (dato.split("###").length == 6) {
								if (dato.contains("Nacimiento:")) {
									asegurado.setNacimiento(fn.formatDate(
											dato.split("Nacimiento:")[1].replace("###", "").trim(), "dd-MM-yy"));
								}
							}
							asegurado.setSexo(1);
							asegurado.setParentesco(1);
							asegurados.add(asegurado);
						} else if (dato.contains("Asegurado:") && dato.contains("R.F.C:")
								&& dato.split("###").length == 6) {
							asegurado.setNombre(dato.split("###")[1].trim());
							if (dato.split("###")[dato.split("###").length - 1].trim().split("/").length == 3) {
								asegurado.setNacimiento(fn.formatDate(
										dato.split("###")[dato.split("###").length - 1].trim(), "dd-MM-yy"));
							}
							asegurado.setSexo(1);
							asegurado.setParentesco(0);
							asegurados.add(asegurado);
						}
					}
				}
			}

			if (asegurados.size() == 0) {
				inicio = contenido.indexOf("PARENTESCO  EDAD");
				fin = contenido.indexOf("LA DOCUMENTACIÓN CONTRACTUAL");
				if (inicio > 0 && fin > 0 && inicio < fin) {
					newcontenido = contenido.substring(inicio, fin);
					for (String a : newcontenido.split("\n")) {
						EstructuraAseguradosModel asegurado = new EstructuraAseguradosModel();
						if (!a.contains("NOMBRE")) {
							switch (a.split("###").length) {
							case 5:
								asegurado.setNombre(a.split("###")[0].replace("###", "").trim());
								asegurado.setParentesco(
										fn.parentesco(a.split("###")[1].replace("(A)", "").trim().toLowerCase()));
								asegurado.setSexo(1);
								asegurados.add(asegurado);
								break;
							}
						}
					}
				}
			}
			modelo.setAsegurados(asegurados);
			List<EstructuraBeneficiariosModel> beneficiarios = new ArrayList<>();
			/*
			 * Esto aplico para el caso de plan de funerario de un poliza de vida de mapfre
			 */
			if (modelo.getAsegurados().size() > 1) {
				for (int i = 0; i < modelo.getAsegurados().size(); i++) {
					EstructuraBeneficiariosModel beneficiario = new EstructuraBeneficiariosModel();
					if (modelo.getAsegurados().get(i).getParentesco() != 1) {
						beneficiario.setNombre(modelo.getAsegurados().get(i).getNombre());
						beneficiario.setParentesco(modelo.getAsegurados().get(i).getParentesco());
						beneficiario.setNacimiento(modelo.getAsegurados().get(i).getNacimiento());
						beneficiarios.add(beneficiario);
					} else {
						asegurados.add(modelo.getAsegurados().get(i));
					}
				}
				modelo.setBeneficiarios(beneficiarios);
				modelo.setAsegurados(asegurados);
			}

			// coberturas{nombre sa deducible coaseguro}
			donde = 0;
			donde = fn.recorreContenido(inicontenido, "DESCRIPCION DE COBERTURAS");
			if (donde == 0) {
				donde = fn.recorreContenido(inicontenido, "DESCRIPCIÓN DE COBERTURAS");
			}
			List<EstructuraCoberturasModel> coberturas = new ArrayList<>();

			if (donde > 0) {

				for (String dato : inicontenido.split("@@@")[donde + 1].split("\r\n")) {
					EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();
					newcontenido = remplazaGrupoSpace(dato.trim());
					if (newcontenido.split("###").length == 5) {
						cobertura.setNombre(newcontenido.split("###")[0].replace("*", "").trim());
						cobertura.setSa(newcontenido.split("###")[1].trim());
						coberturas.add(cobertura);
						modelo.setCoberturas(coberturas);
					}
				}
			} else if (fn.searchTwoTexts(contenido, "COBERTURAS", "SUMA ASEGURADA") > 0) {
				donde = fn.searchTwoTexts(contenido, "COBERTURAS", "SUMA ASEGURADA");
				for (String dato : contenido.split("@@@")[donde].split("\r\n")) {
					EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();
					newcontenido = dato.trim();
					if (newcontenido.contains("COBERTURAS###SUMA") == false) {
						if (newcontenido.split("###").length >= 3) {

							cobertura.setNombre(newcontenido.split("###")[0].replace("*", "").trim());
							cobertura.setSa(newcontenido.split("###")[1].trim());
							coberturas.add(cobertura);

						}
					}
				}
				modelo.setCoberturas(coberturas);
			}

			// CUANDO SERVICIOS VIENE INDEPENDIENTE
			donde = 0;
			newcontenido = "";
			donde = fn.recorreContenido(contenido, "SERVICIOS");
			if (donde > 0) {
				if (contenido.split("@@@")[donde].split("\r\n")[0].trim().equals("SERVICIOS")) {
					for (int i = 0; i < contenido.split("@@@")[donde].split("\r\n").length; i++) {
						EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();
						if (i > 0) {
							newcontenido = contenido.split("@@@")[donde].split("\r\n")[i].trim();
							if (newcontenido.split("###").length == 2) {
								cobertura.setNombre(newcontenido.split("###")[0].replace("*", "").trim());
								cobertura.setSa(newcontenido.split("###")[1].trim());
								coberturas.add(cobertura);
							}
						}
					}
					modelo.setCoberturas(coberturas);
				}
			}
			;

			if (contenido.indexOf("DESIGNACION DE LOS BENEFICIARIOS") > 0) {
				String beneFi = remplazaGrupoSpace(
						inicontenido.split("DESIGNACION DE LOS BENEFICIARIOS")[1].split("PARTICIPACIÓN")[1]
								.split("En testimonio ")[0].replace("@@@", ""));
				for (String ben : beneFi.split("\r\n")) {
					EstructuraBeneficiariosModel beneficiario = new EstructuraBeneficiariosModel();
					int tmben = ben.split("###").length;

					if (tmben == 4) {
						beneficiario.setNombre(ben.split("###")[1]);
						beneficiario.setParentesco(fn.parentesco(ben.split("###")[2]));
						beneficiario.setTipo(11);
						beneficiario.setPorcentaje(Integer.parseInt(ben.split("###")[3]));
						beneficiarios.add(beneficiario);
					}
				}
				modelo.setBeneficiarios(beneficiarios);

			} else {
				if (modelo.getBeneficiarios().size() == 0) {
					if (inicontenido.contains("BENEFICIARIOS")
							&& inicontenido.contains("LA DOCUMENTACION CONTRACTUAL")) {
						String beng = remplazaGrupoSpace(inicontenido.split("BENEFICIARIOS")[1].split("PORCENTAJE")[1]
								.split("LA DOCUMENTACION CONTRACTUAL")[0]);
						for (String ben : beng.split("\r\n")) {
							EstructuraBeneficiariosModel beneficiario = new EstructuraBeneficiariosModel();
							int tmben = ben.split("###").length;

							if (tmben == 4) {
								beneficiario.setNombre(ben.split("###")[1]);
								beneficiario.setParentesco(fn.parentesco(ben.split("###")[2]));
								beneficiario
										.setPorcentaje(Integer.parseInt(ben.split("###")[3].replace("%", "").trim()));
								beneficiario.setTipo(11);
								beneficiarios.add(beneficiario);
							}
						}
						modelo.setBeneficiarios(beneficiarios);
					} else {

					}
				}

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
					MapfreVidaModel.this.getClass().getTypeName() + " | " + ex.getMessage() + " | " + ex.getCause());
			return modelo;
		}
	}

	public String remplazaGrupoSpace(String dato) { // RETORNA UNA CADENA, EN DONDE TENGA MAS DE 2 ESPACIOS PONE ###
		boolean encontro_grupo = false;
		int par = 0;
		String newdato = "";
		for (int i = 0; i < dato.length(); i++) {
			if (dato.charAt(i) == ' ') {
				if (encontro_grupo == false) {
					par = par + 1;
					if (par == 2) {
						encontro_grupo = true;
						newdato = newdato.trim();
						newdato += "###";
					} else {
						newdato += Character.toString(dato.charAt(i));
					}
				}
			} else {
				par = 0;
				encontro_grupo = false;
				newdato += Character.toString(dato.charAt(i));
			}
		}
		return newdato;
	}

}
