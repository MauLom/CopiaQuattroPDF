package com.copsis.models.gnp;

import java.util.ArrayList;
import java.util.List;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraAseguradosModel;
import com.copsis.models.EstructuraCoberturasModel;
import com.copsis.models.EstructuraJsonModel;
import com.copsis.models.EstructuraRecibosModel;

public class GnpSaludModel {

	// Clases
	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
	// Varaibles
	private String contenido = "", newcontenido = "", newcontenido1 = "", newcontenido2 = "", certificados = "",
			txtasegurados = "", filtrado = "", contenidoCoberturas = "";
	private int inicio = 0, fin = 0;

	public GnpSaludModel(String contenido, String certificados, String asegurados) {
		this.contenido = contenido;
		this.certificados = certificados;
		this.txtasegurados = asegurados;
	}

	public EstructuraJsonModel procesar() {

		contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());
		txtasegurados = fn.remplazarMultiple(txtasegurados, fn.remplazosGenerales());

		int longitud_split = 0;
		int longitud_texto = 0;
		int donde = 0;
		int dondeAux = 0;
		String parte = "", agente = "";
		boolean encontroNal = false;
		boolean encontroExt = false;
		int encontroNacional = 0;
		int encontroExtranjero = 0;

		try {

			// tipo
			modelo.setTipo(3);
			// cia
			modelo.setCia(18);

			// poliza
			inicio = contenido.indexOf("Póliza No.");
			if (inicio > -1) {
				modelo.setPoliza(contenido.substring(inicio + 10, contenido.indexOf("\r\n", inicio + 10))
						.replace("###", "").replace("-", "").trim());
			}

			// renovacion
			// cte_nombre
			donde = 0;
			donde = fn.searchTwoTexts(contenido, "Renovación", "Versión");
			if (donde > 0) {
				for (String dato : contenido.split("@@@")[donde].split("\r\n")) {
					if (dato.contains("Renovación")) {
						switch (dato.split("###").length) {
						case 3:
							if (dato.split("###")[2].trim().contains("Renovación")
									&& dato.split("###")[2].trim().length() > 10) {
								modelo.setRenovacion(dato.split("###")[2].trim().split("Renovación")[1].trim());
							}
							break;
						case 4:
							if (dato.split("###")[2].trim().equals("Renovación")) {
								modelo.setRenovacion(dato.split("###")[3].trim());
							}
							break;
						}
					}
					if (dato.contains("Vigencia")) {
						switch (dato.trim().split("###").length) {
						case 1:
							modelo.setCteNombre(dato.trim().split("Vigencia")[0].trim());
							break;
						case 2:
							if (dato.split("###")[1].contains("Vigencia")) {
								modelo.setCteNombre(dato.split("###")[0].trim());
							}
							break;
						}
					}
				}
			}

			// cp
			// cte_direccion
			// rfc
			// vigencia_de
			// vigencia_a
			donde = 0;
			donde = fn.recorreContenido(contenido, "Contratante");
			if (donde > 0) {
				for (String dato : contenido.split("@@@")[donde].split("\r\n")) {
					if (dato.contains("Día")) {
						if (dato.split("###").length == 4) {
							if (dato.split("###")[1].trim().equals("Día")) {
								newcontenido = dato.split("###")[0].trim();
							}
						}
					} else if (dato.contains("C.P")) {
						if (dato.split("###").length == 5) {
							if (dato.split("###")[0].contains("C.P")) {
								newcontenido += " " + dato.split("###")[0].split("C.P")[0].trim();
								modelo.setCp(dato.split("###")[0].split("C.P")[1].trim());
								if (dato.split("###")[1].contains("hrs.")) {
									modelo.setVigenciaDe(dato.split("###")[4].trim() + "-" + dato.split("###")[3].trim()
											+ "-" + dato.split("###")[2].trim());
								}
							}
						}
					} else if (dato.contains("R.F.C:") || dato.contains("R.F.C. :")) {
						
						if (dato.split("###").length == 5) {
							if ((dato.split("###")[0].contains("R.F.C:") && dato.split("###")[1].contains("del"))
									|| (dato.split("###")[0].contains("R.F.C. :") && dato.split("###")[1].contains("del"))) {
								modelo.setRfc(dato.split("###")[0].split(":")[1].trim());
								if (dato.split("###")[1].contains("hrs.")) {
								
									modelo.setVigenciaA(dato.split("###")[4].trim() + "-" + dato.split("###")[3].trim()
											+ "-" + dato.split("###")[2].trim());
								}
							}
						}
					}
				}

				modelo.setCteDireccion(newcontenido);
			}

			// prima_neta
			// forma_pago
			// moneda
			// recargo
			// derecho
			// iva
			donde = 0;
			donde = fn.recorreContenido(contenido, "Prima Neta");
			if (donde > 0) {
				for (String dato : contenido.split("@@@")[donde].split("\r\n")) {
					if (dato.contains("Prima Neta")) {
						switch (dato.split("###").length) {
						case 4:
							if (dato.split("###")[2].trim().equals("Prima Neta")) {
								modelo.setPrimaneta(fn.castFloat(dato.split("###")[3].trim()));
							}
							break;
						}
					}

					if (dato.contains("Forma de pago")) {
						switch (dato.trim().split("###").length) {
						case 7:
							if (dato.trim().split("###")[3].trim().equals("Forma de pago")) {
								modelo.setFormaPago(fn.formaPago(dato.trim().split("###")[4].trim()));
							}
							if (dato.split("###")[5].contains("de Póliza")) {
								modelo.setDerecho(fn.castFloat(dato.split("###")[6].trim()));
							}
							break;
						case 6:
							if (dato.split("###")[3].trim().equals("Forma de pago")
									&& dato.split("###")[5].trim().equals("Recargo por Pago")) {
								modelo.setFormaPago(fn.formaPago(dato.split("###")[4].trim()));
							}
							break;
						}
					}

					if (dato.contains("Moneda")) {
						switch (dato.split("###").length) {
						case 7:
							if (dato.split("###")[4].trim().equals("Moneda")
									&& dato.split("###")[6].contains("I.V.A.")) {
								modelo.setMoneda(fn.moneda(dato.split("###")[5].trim()));
								newcontenido = fn.cleanString(dato.split("###")[6].split("I.V.A.")[1].trim());

								if (fn.isNumeric(newcontenido)) {
									modelo.setIva(fn.castFloat(newcontenido));
								}
							}
							break;
						case 8:
							if (dato.split("###")[4].trim().equals("Moneda")
									&& dato.split("###")[6].contains("Fraccionado")) {
								modelo.setMoneda(fn.moneda(dato.split("###")[5].trim()));

							}
							break;
						}
					}

					if (dato.contains("de Póliza")) {
						if (dato.split("###").length == 2) {
							if (dato.split("###")[0].contains("Póliza")) {
								modelo.setDerecho(fn.castFloat(dato.split("###")[1].trim()));
							}
						}
					}

					if (dato.contains("I.V.A.")) {

						switch (dato.split("###").length) {

						case 1:
							try {
								if (dato.trim().split(" ").length == 2) {
									if (Double.parseDouble(
											dato.trim().split(" ")[1].replace(".", "").replace(",", "").trim()) > 0) {
										modelo.setIva(fn.castFloat(dato.trim().split(" ")[1].trim()));
									}
								}
							} catch (Exception e) {
								// TODO: handle exception
							}
							break;
						case 2:
							newcontenido = fn.cleanString(dato.split("%")[1].replace("###", ""));

							if (fn.isNumeric(newcontenido)) {

								modelo.setIva(fn.castFloat(newcontenido));
							}

							break;
						}                	
					}

					if (dato.contains("Fraccionado")) {
						switch (dato.split("###").length) {
						case 8:
							if (dato.split("###")[6].trim().equals("Fraccionado")) {
								modelo.setRecargo(fn.castFloat(dato.split("###")[7].trim()));
							}
							break;
						}
					}

					if (dato.contains("Cesión de Comisión")) {
							modelo.setAjusteUno(fn.castFloat(dato.split("Cesión de Comisión")[1].replace("−", "").replace("###", "")));
					}
				}
			}

			// prima_total
			donde = 0;
			donde = fn.recorreContenido(contenido, "Grupo###Nacional");
			if (donde > 0) {
				for (String dato : contenido.split("@@@")[donde].split("\r\n")) {
					if (dato.contains("Pagar")) {
						if (dato.split("###").length == 2) {
							if (dato.split("###")[0].contains("Pagar")) {
								modelo.setPrimaTotal(fn.castFloat(dato.split("###")[1].trim()));
							}
						}
					}
				}
			}
			if (modelo.getPrimaTotal() == 0) {
				donde = 0;
				donde = fn.recorreContenido(contenido, "Pagar");
				if (donde > 0) {
					for (String dato : contenido.split("@@@")[donde].split("\r\n")) {
						if (dato.contains("Pagar")) {
							switch (dato.split("###").length) {
							case 2:
								if (dato.split("###")[0].trim().equals("Pagar")) {
									modelo.setPrimaTotal(fn.castFloat(dato.split("###")[1].trim()));
								}
								break;
							case 4:
								if (dato.split("###")[2].trim().equals("Pagar")) {
									modelo.setPrimaTotal(fn.castFloat(dato.split("###")[3].trim()));
								}
								break;
							}
						}
					}
				}
			}

			// plan
			donde = 0;
			donde = fn.recorreContenido(contenido, "Plan");
			if (donde > 0) {
				for (String dato : contenido.split("@@@")[donde].split("\r\n")) {
					if (dato.contains("Plan")) {
						switch (dato.trim().split("###").length) {
						case 4:
							if (dato.split("###")[0].trim().equals("Plan") && dato.split("###")[1].contains("Día")) {
								modelo.setPlan(dato.split("###")[1].split("Día")[0].trim());
							}
							break;
						case 2:
							if (dato.split("###")[0].trim().equals("Plan")
									&& dato.split("###")[1].contains("Vigencia")) {
								modelo.setPlan(dato.split("###")[1].split("Vigencia")[0].trim());
							} else if (dato.split("###")[0].trim().equals("Plan")
									&& dato.split("###")[1].contains("Vigencia") == false) {

								modelo.setPlan(dato.split("###")[1].trim());
							}
							break;
						}
					}
				}
			}

			// cve_agente
			modelo.setCveAgente(fn.gatos(contenido.split("Clave")[1].split("\r\n")[0]));

			// agente
			inicio = contenido.lastIndexOf("Agente");
			fin = contenido.indexOf("dls=");
			if (inicio > -1 && fin > inicio) {
				newcontenido = contenido.substring(inicio + 6, fin).replace("@@@", "").trim();
				for (String x : newcontenido.split("\r\n")) {
					x = fn.gatos(x);
					if (x.trim().length() > 0) {
						if (x.contains("Clave")) {
							agente += fn.gatos(x.split("Clave")[0]) + " ";
						} else {
							agente += x;
						}
					}
				}
				if (agente.length() > 0) {
					modelo.setAgente(agente);
				}
			}

			// sa
			// deducible
			// coaseguro_tope (coaseguro maximo,tope coaseguro, tope coaseguro)
			// coaseguro
			// deducible_ext
			String coaseguros = contenido.split("Coaseguro")[1].split("Agente")[0]
					.replace("Sin Límite", "Sin Límite###").replace("dls ", "dls###").replace("pesos", "###pesos###")
					.replace("######", "###").replace("SMGM", "###SMGM###");
			int A1 = coaseguros.indexOf("− Nacional");
			int B2 = coaseguros.indexOf("− Extranjero");

			String dtcoberturas = " ";
			if (B2 > 0 && A1 > 0) {
				dtcoberturas = coaseguros.substring(A1, A1 + 100) + "\n" + coaseguros.substring(B2, B2 + 70);
			} else {
				if (A1 > 0) {
					dtcoberturas = coaseguros.substring(A1, A1 + 100);
				} else {
					dtcoberturas = coaseguros.substring(B2, B2 + 100);
				}
			}

			for (String A : dtcoberturas.split("\n")) {
				
				if (A.contains("− Extranjero")) {
					int n = A.split("###").length;
					if (n == 7) {
						modelo.setDeducibleExt(A.split("###")[3].trim());
					}if (n == 6) {
						modelo.setDeducibleExt(A.split("###")[2].trim());
					}
					else if (n == 5) {
						modelo.setDeducibleExt(A.split("###")[2].trim());
					} else {

					}
				}
				if (A.contains("− Nacional") || (A.contains("− Nacional"))) {
					int n = A.split("###").length;
					if (n == 4) {
						modelo.setSa(A.split("###")[1].trim());
						modelo.setDeducible(A.split("###")[2].replace("\r", "").trim());
						modelo.setCoaseguro(A.split("###")[3].replace("\r", "").trim());
					} else if (n == 5) {
						modelo.setSa(A.split("###")[1].trim());
						modelo.setDeducible(A.split("###")[2].replace("\r", "").trim());
						modelo.setCoaseguro(A.split("###")[4].replace("\r", "").trim());
					} else if (n == 6) {
						modelo.setSa(A.split("###")[1].trim());
						if (A.split("###")[3].replace("\r", "").trim().equalsIgnoreCase("pesos")) {
							modelo.setDeducible(A.split("###")[2].replace("\r", "").trim());
							modelo.setCoaseguro(A.split("###")[4].replace("\r", "").trim());
						} else {
							if (A.split("###")[5].replace("\r", "").contains("%")) {
								modelo.setDeducible(A.split("###")[3].replace("\r", "").trim());
								modelo.setCoaseguro(A.split("###")[5].replace("\r", "").trim());
							} else {
								modelo.setDeducible(A.split("###")[2].replace("\r", "").trim());
								modelo.setCoaseguro(A.split("###")[3].replace("\r", "").trim());
							}
						}
					} else if (n == 10) {
						modelo.setSa(A.split("###")[1].trim());
						modelo.setDeducible(A.split("###")[3].replace("\r", "").trim());
						modelo.setCoaseguro(A.split("###")[5].replace("\r", "").trim());
					} else if (n == 7 || n == 9 || n == 8) {
						modelo.setSa(A.split("###")[1].trim());
						if (A.split("###")[5].contains("%")) {
							modelo.setDeducible(A.split("###")[3].trim());
							modelo.setCoaseguro(A.split("###")[5].trim());
						} else {
							modelo.setDeducible(A.split("###")[2].trim());
							modelo.setCoaseguro(A.split("###")[4].trim());
						}
					}
				}
			}
			String a = contenido.split("Fecha de Expedición")[1].split("Moneda")[0];
			modelo.setFechaEmision(a.split("###")[3] + "-" + a.split("###")[2] + "-" + a.split("###")[1]);

			// asegurados { nombre nacimiento antiguedad sexo parentesco }
			// inicio = contenido.indexOf("Asegurado (s)");
			donde = 0;
			donde = fn.recorreContenido(contenido, "segurado s");
			dondeAux = fn.recorreContenido(contenido, "Coberturas###Suma");
			if (dondeAux == 0) {
				dondeAux = fn.recorreContenido(contenido, "Suma Asegurada");
			}
			if (dondeAux == 0) {
				dondeAux = fn.recorreContenido(contenido, "Plan###");
			}
			List<EstructuraAseguradosModel> asegurados = new ArrayList<>();
			
	
			if (donde > 0 && dondeAux > 0) {
				
				inicio = contenido.indexOf(contenido.split("@@@")[donde]);
				fin = contenido.indexOf(contenido.split("@@@")[dondeAux]);
				
				
				newcontenido = "";
				filtrado = contenido.substring(inicio, fin).replace("@@@", "").trim();
				
			
				if (filtrado.contains("Ver listado de Asegurados")) {// AL NO TRAER ASEGURADOS TRABAJAMOS CON LA
																		// VARIABLE QUE LLEGA
					if (txtasegurados.length() > 0) {
						inicio = txtasegurados.indexOf("Nacional###Extranjero");
						if (inicio == -1) {
							inicio = txtasegurados.indexOf("Nombre###Nacional");
						}
						fin = txtasegurados.indexOf("Para mayor información");
						if (inicio > -1 && fin > inicio) {
							filtrado = fn.gatos(txtasegurados.substring(inicio + 21, fin).replace("@@@", "")).trim();
						}
					}
				}
				
				for (String dato : filtrado.split("\r\n")) {
					if (dato.trim().split("-").length == 3 || dato.trim().split("-").length == 5) {
						if (dato.contains("CARTERA")) {
							newcontenido += dato.split("CARTERA")[0].replace("###", "   ").trim().replace("   ", "###")
									+ "\r\n";
						} else if (dato.contains("Renovación")) {
							newcontenido += dato.split("Renovación")[0].replace("###", "   ").trim().replace("   ",
									"###") + "\r\n";
						} else if (dato.contains("Nacional")) {
							newcontenido += dato.split("Nacional")[1].replace("###", "   ").trim().replace("   ", "###")
									+ "\r\n";
						} else if (dato.contains("Descripción")) {
							newcontenido += dato.split("Descripción")[0].replace("###", "   ").trim().replace("   ",
									"###") + "\r\n";
						} else if (dato.contains("D escripción")) {
							newcontenido += dato.split("D escripción")[0].replace("###", "   ").trim().replace("   ",
									"###") + "\r\n";
						} else if (dato.contains("Cambio")) {
							newcontenido += dato.split("Cambio")[0].replace("###", "   ").trim().replace("   ", "###")
									+ "\r\n";
						} else if (dato.contains("Sustitución")) {
							newcontenido += dato.split("Sustitución")[0].replace("###", "   ").trim().replace("   ",
									"###") + "\r\n";
						} else {
							newcontenido += dato.replace("###", "   ").trim().replace("   ", "###") + "\r\n";
						}
					}
				}

				if (newcontenido.length() > 0) {
				
					for (String dato : newcontenido.split("\r\n")) {
						EstructuraAseguradosModel asegurado = new EstructuraAseguradosModel();
						switch (dato.split("###").length) {
						case 2:
							if (dato.split("###")[1].trim().length() > 15) {
								longitud_split = dato.split("###")[1].split(" ").length - 1;
								asegurado.setAntiguedad(fn.formatDate(
										dato.split("###")[1].trim().split(" ")[longitud_split].trim(), "dd-MM-yy"));
								asegurado.setNombre(dato.split("###")[1]
										.split(dato.split("###")[1].trim().split(" ")[longitud_split])[0].trim());
								asegurado.setSexo(1);
								asegurado.setParentesco(1);
								asegurados.add(asegurado);

							}
							break;
						case 3:
							asegurado.setAntiguedad(fn.formatDate(dato.split("###")[2].trim(), "dd-MM-yy"));
							asegurado.setNombre(dato.split("###")[1].trim());
							asegurado.setSexo(1);
							asegurado.setParentesco(1);
							asegurados.add(asegurado);
							break;
						case 4:
							asegurado.setAntiguedad(fn.formatDate(dato.split("###")[2].trim(), "dd-MM-yy"));
							asegurado.setNombre(dato.split("###")[1].trim());
							asegurado.setSexo(1);
							asegurado.setParentesco(1);
							asegurados.add(asegurado);
							break;
						default:

							break;
						}
					}
				}
			}
			modelo.setAsegurados(asegurados);

			List<EstructuraCoberturasModel> coberturas = new ArrayList<>();
			// coberturas{nombre sa deducible coaseguro}
			String[] coberturasTxt = { "E mergencia de gastos médico s", "Emergencia de gastos médico s",
					"Emergencia en el Extranjero", "E mergencia en el Extranjero", "Catastróficas Nacional",
					"C atastróficas Nacional", "Enfermedades Catastróficas", "n el Extranjero", "Asistencia en Viajes",
					"A sistencia en Viajes", "Membresía Médica Móvil", "M embresía Médica Móvil",
					"Cero Deducible por Accidente", "C ero Deducible por Accidente", "ero Deducible por Accidente",
					"R espaldo por Fallecimiento" };
			donde = 0;
			donde = fn.recorreContenido(contenido, "Coaseguro");
			dondeAux = fn.recorreContenido(contenido, "Agente");
			if (donde > 0 && dondeAux > 0) {
				inicio = contenido.indexOf(contenido.split("@@@")[donde]);
				fin = contenido.indexOf(contenido.split("@@@")[dondeAux]);
				filtrado = contenido.substring(inicio, fin).replace("@@@", "").trim();
				if (filtrado.contains("Coaseguro") && filtrado.contains("Coberturas")) {
					newcontenido = "";
					int inicioCob = 0;
					for (String cobertura : coberturasTxt) {
						newcontenido1 = "";
						contenidoCoberturas = filtrado.split("Coaseguro")[1].split("Coberturas")[0].trim();
						inicioCob = contenidoCoberturas.indexOf(cobertura);
						if (inicioCob > -1) {

							if (cobertura.equals("− Nacional")) { // CUANDO VIENE DOS VESES NACIONAL
								if (encontroNal == true) {
									inicioCob = 0;
									inicioCob = contenidoCoberturas.lastIndexOf(cobertura);
								}

								if (encontroNal == false) {
									encontroNacional = inicioCob;
									newcontenido1 = contenidoCoberturas
											.substring(inicioCob, contenidoCoberturas.indexOf("\r\n", inicioCob))
											.trim();
									encontroNal = true;
								} else if (encontroNal == true && encontroNacional != inicioCob) {
									if (contenidoCoberturas.substring(inicioCob, inicioCob + 80)
											.split("\n").length == 2) {
										newcontenido1 = contenidoCoberturas.substring(inicioCob, (inicioCob + 90))
												.split("\r\n")[0].trim();
									}
								}

							} else if (cobertura.equals("− Extranjero")) { // CUANDO VIENE DOS VESES EXTRANGERO
								if (encontroExt == true) {
									inicioCob = 0;
									inicioCob = contenidoCoberturas.lastIndexOf(cobertura);
								}
								if (encontroExt == false) {
									encontroExtranjero = inicioCob;
									newcontenido1 = contenidoCoberturas
											.substring(inicioCob, contenidoCoberturas.indexOf("\r\n", inicioCob))
											.trim();
									encontroExt = true;
								} else if (encontroExt == true && encontroExtranjero != inicioCob) {
									if (contenidoCoberturas.substring(inicioCob, inicioCob + 80)
											.split("\n").length == 2) {
										newcontenido1 = contenidoCoberturas.substring(inicioCob, (inicioCob + 90))
												.split("\r\n")[0].trim();
									}
								}
							} else if (cobertura.equals("n el Extranjero")) {// CUANDO TRAE "ENFERMEDADES CATASTRÓFICAS
																				// EN EL EXTRANJERO"
								if (contenidoCoberturas.substring(inicioCob - 12, inicioCob).contains("ergen")) {// Emergencias
									inicioCob = 0;
									inicioCob = contenidoCoberturas.lastIndexOf(cobertura); // BUSCA EL ULTIMO "n el
																							// Extranjero"
									if (inicioCob > 0) {
										if (contenidoCoberturas
												.substring(inicioCob - 12,
														contenidoCoberturas.indexOf("\r\n", inicioCob))
												.contains("ergen") == false) {
											newcontenido1 = "e"
													+ contenidoCoberturas
															.substring(inicioCob,
																	contenidoCoberturas.indexOf("\r\n", inicioCob))
															.trim();
										}
									}
								}
							} else {// TODAS LAS DEMAS COBETURAS
								if (newcontenido.contains(cobertura) == false) {
									newcontenido1 = contenidoCoberturas
											.substring(inicioCob, contenidoCoberturas.indexOf("\r\n", inicioCob))
											.trim();
								}
							}

							if (newcontenido1.length() > 0) {
								if (newcontenido1.contains("Prima")) { // SI TRAE MAS CONTENIDO LA LINEA, SE REMUEVEN
																		// EXTRAS
									newcontenido1 = newcontenido1.split("Prima")[0].trim();
								} else if (newcontenido1.contains("P rima")) {
									newcontenido1 = newcontenido1.split("P rima")[0].trim();
								} else if (newcontenido1.contains("P  rima")) {
									newcontenido1 = newcontenido1.split("P  rima")[0].trim();
								} else if (newcontenido1.contains("P   rima")) {
									newcontenido1 = newcontenido1.split("P   rima")[0].trim();
								} else if (newcontenido1.contains("Recargo")) {
									newcontenido1 = newcontenido1.split("Recargo")[0].trim();
								} else if (newcontenido1.contains("Hasta las")) {
									newcontenido1 = newcontenido1.split("Hasta las")[0].trim();
								} else if (newcontenido1.contains("Importe")) {
									newcontenido1 = newcontenido1.split("Importe")[0].trim();
								} else if (newcontenido1.contains("Pagar")) {
									newcontenido1 = newcontenido1.split("Pagar")[0].trim();
								} else if (newcontenido1.contains("Derecho de")) {
									newcontenido1 = newcontenido1.split("Derecho de")[0].trim();
								} else if (newcontenido1.contains("I.V.A.")) {
									newcontenido1 = newcontenido1.split("I.V.A.")[0].trim();
								} else if (newcontenido1.contains("Fraccionado")) {
									newcontenido1 = newcontenido1.split("Fraccionado")[0].trim();
								} else if (newcontenido1.contains("Duración")) {
									newcontenido1 = newcontenido1.split("Duración")[0].trim();
								} else if (newcontenido1.contains("Resumen")) {
									newcontenido1 = newcontenido1.split("Resumen")[0].trim();
								} else if (newcontenido1.contains("Deducible por")
										&& newcontenido1.contains("Amparada") == false) {
									newcontenido1 = newcontenido1 + "###" + "Amparada ###";
								} else if (newcontenido1.contains("en el Extranjero")) {
									if (newcontenido1.substring(0, 10).contains("en el E")) {
										newcontenido1 = "Enfermedades Catastróficas " + newcontenido1;
									}
								}

								///////////////////////////////////////////////// SI LOS ULTIMOS 3 CARACTERES ES
								///////////////////////////////////////////////// ### LOS QUITA
								longitud_texto = 0;
								longitud_texto = newcontenido1.length();
								if (newcontenido1.substring(longitud_texto - 3, longitud_texto).equals("###")) {
									newcontenido += newcontenido1.substring(0, longitud_texto - 3).trim()
											.replace("######", "###") + "\r\n";
								} else {
									newcontenido += contenidoCoberturas
											.substring(inicioCob, contenidoCoberturas.indexOf("\r\n", inicioCob))
											.replace("######", "###") + "\r\n";
								}
							}
						}
					}

					String datotexto = "";
					for (String dato : newcontenido.split("\r\n")) {
						EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();
						datotexto = dato.replace("−", "").trim();
						if (datotexto.contains("E mergencia de gastos médico s")
								|| datotexto.contains("Emergencia de gastos médico s")) {
							datotexto = "Emergencia de gastos médicos mayores no cubiertos";
						} else if (datotexto.contains("Catastróficas") && datotexto.contains("Enfermedades") == false
								|| datotexto.contains("C atastróficas")
										&& datotexto.contains("Enfermedades") == false) {

							datotexto = "Enfermedades " + datotexto;
						} else if (datotexto.contains("n el Extranjero") && datotexto.contains("ergen") == false) {
							datotexto = "Enfermedades Catastróficas " + datotexto;
						}

						switch (datotexto.split("###").length) {
						case 4:

							cobertura.setNombre(datotexto.split("###")[0].trim());
							cobertura.setSa(datotexto.split("###")[1].trim());
							cobertura.setDeducible(datotexto.split("###")[2].trim());
							cobertura.setCoaseguro(datotexto.split("###")[3].trim());
							coberturas.add(cobertura);
							break;
						case 3:

							if (datotexto.split("###")[2].length() == 3 && datotexto.split("###")[2].contains("%")) {
								cobertura.setCoaseguro(datotexto.split("###")[2].trim());
							}

							if (datotexto.split("###")[1].trim().split(" ").length == 2) {
								cobertura.setDeducible(datotexto.split("###")[1].trim());
							}

							if (datotexto.split("###")[0].contains("Sin Límite")) {
								cobertura.setNombre(datotexto.split("###")[0].split("Sin Límite")[0].trim());
								cobertura.setSa(datotexto.split("###")[0].split(cobertura.getNombre())[1].trim());

							} else if (datotexto.split("###")[0].split(" ").length >= 3) {
								parte = extraigoPipe(datotexto.split("###")[0].trim());
								cobertura.setNombre(datotexto.split("###")[0].trim().split(parte)[0].trim());
								cobertura
										.setSa(datotexto.split("###")[0].trim().split(cobertura.getNombre())[1].trim());

							}
							coberturas.add(cobertura);
							break;
						case 2:
							if (datotexto.split("###")[1].trim().equals("Amparada")) {

								cobertura.setNombre(datotexto.split("###")[0].trim());
								cobertura.setSa(datotexto.split("###")[1].trim());

								coberturas.add(cobertura);
							} else {
								if (datotexto.contains("por Fallecimie")) {
									cobertura.setNombre(datotexto.split("###")[0].trim());
									cobertura.setSa(datotexto.split("###")[1].trim());
									coberturas.add(cobertura);

								} else {
									newcontenido2 = datotexto.split("###")[1];
									if (newcontenido2.split(" ").length == 5) {
										cobertura.setNombre(datotexto.split("###")[0].trim());
										cobertura.setSa(datotexto.split("###")[1].trim());
										cobertura.setDeducible(newcontenido2.split(" ")[1].trim());
										cobertura.setCoaseguro(newcontenido2.split(" ")[1].trim());
										coberturas.add(cobertura);
										if (newcontenido2.split(" ").length == 2) {
											parte = extraigoPipe(datotexto.split("###")[0].trim());
											cobertura
													.setNombre(datotexto.split("###")[0].trim().split(parte)[0].trim());
											cobertura.setSa(
													datotexto.split("###")[0].trim().split(cobertura.getNombre())[1]
															.trim());
											cobertura.setDeducible(newcontenido2.split(" ")[0].trim());
											cobertura.setCoaseguro(newcontenido2.split(" ")[1].trim());
											coberturas.add(cobertura);

										}
									}
								}
							}
							break;
						case 1:
							if (datotexto.contains("Emergencia de gastos")) {
								cobertura.setNombre(datotexto);
								coberturas.add(cobertura);
							} else if (datotexto.contains("Amparada")) {
								cobertura.setNombre(datotexto.split("Amparada")[0].trim());
								cobertura.setSa(datotexto.split(cobertura.getSa())[1].trim());
								coberturas.add(cobertura);
							}
							break;
						}
					}
				}
			}
			modelo.setCoberturas(coberturas);

			modelo.setIdCliente(contenido.split("Código Cliente")[1].split("Duración")[0].replace("###", "").trim());

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
			case 2: // NINGUN PDF TRAE RECIBOS SE QUEDA PENDIENTE ESTE CASO
				if (recibos.size() == 1) {

					recibo.setReciboId("");
					recibo.setSerie("2/2");
					recibo.setPrimaneta(fn.castBigDecimal(0.00, 2));
					recibo.setDerecho(fn.castBigDecimal(0.00, 2));
					recibo.setRecargo(fn.castBigDecimal(0.00, 2));
					recibo.setIva(fn.castBigDecimal(0.00, 2));
					recibo.setPrimaTotal(fn.castBigDecimal(0.00, 2));
					recibo.setAjusteUno(fn.castBigDecimal(0.00, 2));
					recibo.setAjusteDos(fn.castBigDecimal(0.00, 2));
					recibo.setCargoExtra(fn.castBigDecimal(0.00, 2));
					recibos.add(recibo);
				}
				break;
			case 3:
			case 4:
			case 5:
			case 6: // NINGUN PDF TRAE RECIBOS SE QUEDA PENDIENTE ESTE CASO

				break;
			}
			modelo.setRecibos(recibos);
			if (certificados.length() > 0) {
				for (String x : certificados.split("ERTIFICADO DE COBERTURA POR ASEGURADO")) {

					String nombre = "";
					String nacimiento = "";
					String certificado = "";
					int sexo = 1;
					inicio = x.indexOf("Prima del Asegurado");
					fin = x.indexOf("Código");
					if (inicio > -1) {
						nombre = fn.gatos(x.substring(inicio + 19, fin).trim());
					}
					inicio = x.indexOf("Nacimiento");
					
					if (inicio > -1) {
						newcontenido = x.substring(inicio + 10, inicio + 150).split("\n")[0];
						
						if (newcontenido.contains("Importe")) {
							
							nacimiento = fn.formatDate(fn.gatos( newcontenido.split("Importe")[0].trim()).replace("###", "-"), "dd-MM-yy");
									
						}
					}
				
					inicio = x.indexOf("digo Cliente");
					if(inicio == 0) {
						inicio = x.indexOf("Código Cliente");
					}
					
					if (inicio > -1) {
						
						newcontenido = x.substring(inicio + 12, x.indexOf("\n", inicio + 12));
				
						if (newcontenido.contains("Prima")) {
							certificado = fn.gatos(newcontenido.split("Prima")[0]);
						}
					}
					inicio = x.indexOf("Sexo:");
					
					if (inicio > -1) {
						newcontenido = fn.gatos(x.substring(inicio + 5, inicio + 150).split("\n")[0]).trim();
		
						sexo = (fn.sexo(newcontenido)) ? 1 : 0;
					
					}
					if (nombre.length() > 0) {
						
						for (int i = 0; i < asegurados.size(); i++) {
						if(asegurados.get(i).getNombre().equals(nombre)) {
							EstructuraAseguradosModel asegurado = asegurados.get(i);
							asegurado.setNacimiento(nacimiento);
							asegurado.setSexo(sexo);
							asegurado.setCertificado(certificado);
						}
						}
					}
				}
			}

			return modelo;
		} catch (Exception ex) {
			modelo.setError(
					GnpSaludModel.this.getClass().getTypeName() + " | " + ex.getMessage() + " | " + ex.getCause());
			return modelo;
		}
	}

	private String extraigoPipe(String txtfull) {// EXTRAE LOS 2 PRIMEROS NUMEROS QUE ENCUENTRA DE
		String texto = "";
		int contador = 0;
		for (int i = 0; i < txtfull.trim().length(); i++) {
			try {
				if (Double.parseDouble(Character.toString(txtfull.charAt(i))) >= 0) {
					texto += txtfull.charAt(i);
					contador++;
					if (contador == 2) {
						break;
					}
				}
			} catch (java.lang.NumberFormatException e) {
				if (contador > 0) {
					texto += txtfull.charAt(i);
					break;
				}

			}
		}

		return texto;
	}

}
