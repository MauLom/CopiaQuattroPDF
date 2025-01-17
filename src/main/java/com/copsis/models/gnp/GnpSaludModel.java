package com.copsis.models.gnp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.copsis.constants.ConstantsValue;
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
	private String contenido = "";
	private String certificados = "";
	private String txtasegurados = "";
	private String fechAsegurados = "";

	public GnpSaludModel(String contenido, String certificados, String asegurados, String fechAsegurados) {
		this.contenido = contenido;
		this.certificados = certificados;
		this.txtasegurados = asegurados;
		this.fechAsegurados = fechAsegurados;
	}

	public EstructuraJsonModel procesar() {

		contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales()).replace("###−###", "");
		txtasegurados = fn.remplazarMultiple(txtasegurados, fn.remplazosGenerales());
		fechAsegurados = fn.remplazarMultiple(fechAsegurados, fn.remplazosGenerales());

		StringBuilder newcontenido = new StringBuilder();

		String filtrado = "";
		String contenidoCoberturas = "";
		int inicio = 0;
		int fin = 0;
		String newcontenido1 = "";
		String newcontenido2 = "";

		int longitudSplit = 0;
		int longitudTexto = 0;
		int donde = 0;
		int dondeAux = 0;
		String parte = "";
		StringBuilder agente = new StringBuilder();
		boolean encontroNal = false;
		boolean encontroExt = false;
		int encontroNacional = 0;
		int encontroExtranjero = 0;

		try {

			System.out.println("contenido" + contenido);
			modelo.setTipo(3);
			modelo.setCia(18);

			inicio = contenido.indexOf("Póliza No.");
			if (inicio > -1) {
				modelo.setPoliza(contenido.substring(inicio + 10, contenido.indexOf("\r\n", inicio + 10))
						.replace("###", "").replace("-", "").trim());
			}
			donde = 0;
			donde = fn.searchTwoTexts(contenido, ConstantsValue.RENOVACION, "Versión");
			if (donde > 0) {
				for (String dato : contenido.split("@@@")[donde].split("\r\n")) {

					if (dato.contains(ConstantsValue.RENOVACION)) {
						switch (dato.split("###").length) {
							case 3:
								if (dato.split("###")[2].trim().contains(ConstantsValue.RENOVACION)
										&& dato.split("###")[2].trim().length() > 10) {
									modelo.setRenovacion(
											dato.split("###")[2].trim().split(ConstantsValue.RENOVACION)[1].trim());
								}
								break;
							case 4:
								if (dato.split("###")[2].trim().equals(ConstantsValue.RENOVACION)) {
									modelo.setRenovacion(dato.split("###")[3].trim());
								}
								break;
							default:
								break;
						}
					}
					if (dato.contains(ConstantsValue.VIGENCIA2)) {

						switch (dato.trim().split("###").length) {
							case 1:
								modelo.setCteNombre(dato.trim().split(ConstantsValue.VIGENCIA2)[0]);
								break;
							case 2:
								if (dato.split("###")[1].contains(ConstantsValue.VIGENCIA2)) {
									modelo.setCteNombre(dato.split("###")[0].trim());
								}
								break;
							default:
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
						if (dato.split("###").length == 4 && dato.split("###")[1].trim().equals("Día")) {
							newcontenido.append(dato.split("###")[0].trim());
						}
					} else if (dato.contains("C.P")) {
						if (dato.split("###").length == 5 && dato.split("###")[0].contains("C.P")) {
							dato = dato.replace("C.P", "C/P");
							newcontenido.append(" ").append(dato.split("###")[0].split("C/P")[0].trim());

							if (newcontenido.substring(newcontenido.length() - 1).equals(",")) {
								String auxiliar = newcontenido.substring(0, newcontenido.length() - 1);
								newcontenido = new StringBuilder();
								newcontenido.append(auxiliar);
							}

							if (Boolean.TRUE.equals(fn.isvalidCp(
									dato.split("###")[0].split("C/P")[1].replace(":", "").replace(".", "").trim()))) {
								modelo.setCp(
										dato.split("###")[0].split("C/P")[1].replace(":", "").replace(".", "").trim());
							}
							if (dato.split("###")[1].contains("Desde las 12 hrs de")) {
								modelo.setVigenciaDe(dato.split("###")[4].trim() + "-" + dato.split("###")[3].trim()
										+ "-" + dato.split("###")[2].trim());
							}

						}
						if (dato.split("###").length == 6) {
							String res = dato.replace(ConstantsValue.HASH, "###");
							dato = res;
							if (dato.split("###")[0].contains("C.P")) {
								newcontenido.append(" ").append(dato.split("###")[0].split("C.P")[0].trim());
								modelo.setCp(dato.split("C.P")[1].split("###")[0].trim());
								if (dato.split("###")[1].contains("hrs.")) {
									modelo.setVigenciaDe(dato.split("###")[4].trim() + "-" + dato.split("###")[3].trim()
											+ "-" + dato.split("###")[2].trim());
								}
							}
						}
					} else if (dato.contains("R.F.C:") || dato.contains("R.F.C. :")) {

						dato = dato.replace(ConstantsValue.HASH, "###");

						if (dato.split("###").length == 5
								&& (dato.split("###")[0].contains("R.F.C:") && dato.split("###")[1].contains("del"))
								|| (dato.split("###")[0].contains("R.F.C. :")
										&& dato.split("###")[1].contains("del"))) {
							if (dato.split("###")[0].split(":").length > 1) {
								modelo.setRfc(dato.split("###")[0].split(":")[1].replace("-", "").trim());
							}
							if (dato.split("###")[1].contains("hrs.") || dato.contains("Hasta las 12 hrs del")) {
								modelo.setVigenciaA(dato.split("###")[4].trim() + "-" + dato.split("###")[3].trim()
										+ "-" + dato.split("###")[2].trim());
							}

						}
					}
				}

				modelo.setCteDireccion(newcontenido.toString());
			}

			// prima_neta
			// forma_pago
			// moneda
			// recargo
			// derecho
			// iva
			donde = 0;
			donde = fn.recorreContenido(contenido, ConstantsValue.PRIMA_NETA);
			if (donde > 0) {
				for (String dato : contenido.split("@@@")[donde].split("\r\n")) {

					if (dato.contains(ConstantsValue.PRIMA_NETA)) {

						dato = dato.replace(ConstantsValue.HASH, "###").trim();
						switch (dato.split("###").length) {
							case 4:
								if (dato.split("###")[2].trim().equals(ConstantsValue.PRIMA_NETA)) {
									modelo.setPrimaneta(
											fn.castBigDecimal(fn.preparaPrimas(dato.split("###")[3].trim())));
								}
								break;
							case 5:
								if (dato.contains(ConstantsValue.PRIMA_NETA)) {
									modelo.setPrimaneta(
											fn.castBigDecimal(fn.preparaPrimas(dato.split("###")[4].trim())));
								}
								break;
							default:
								break;
						}
					}

					if (dato.contains(ConstantsValue.FORMA_PAGO2)) {

						switch (dato.trim().split("###").length) {
							case 8:
								if (dato.split("###")[6].contains("de Póliza") || dato.contains("Derecho de Póliza")) {
									modelo.setDerecho(fn.castBigDecimal(fn.preparaPrimas(dato.split("###")[7].trim())));
								}

								if (dato.split("###")[6].contains("Fraccionado")) {
									if (dato.split("###")[7].trim().contains("−")) {
										modelo.setRecargo(fn
												.castBigDecimal(
														fn.preparaPrimas(dato.split("###")[7].replace("−", "").trim()))
												.negate());
									} else {
										modelo.setRecargo(
												fn.castBigDecimal(fn.preparaPrimas(dato.split("###")[7].trim())));
									}
								}
								break;
							case 7:

								if (dato.contains(ConstantsValue.FORMA_PAGO2)|| dato.contains("Derecho de Póliza")) {
									modelo.setFormaPago(fn.formaPago(
											dato.trim().split(ConstantsValue.FORMA_PAGO2)[1].split("###")[1].trim()));
								}

								if (dato.split("###")[5].contains("de Póliza") || dato.contains("Derecho de Póliza###")) {
									modelo.setDerecho(fn.castBigDecimal(fn.preparaPrimas(dato.split("###")[6].trim())));
								}
								break;
							case 6:
								if (dato.split("###")[3].trim().equals(ConstantsValue.FORMA_PAGO2)
										&& dato.split("###")[5].trim().equals(ConstantsValue.RECARGO_PAGO)) {
									modelo.setFormaPago(fn.formaPago(dato.split("###")[4].trim()));
								}
								break;
							case 9:

								if (dato.contains(ConstantsValue.FORMA_PAGO2) && dato.contains("Recargo por Pago")) {
									modelo.setFormaPago(fn.formaPago(
											dato.split(ConstantsValue.FORMA_PAGO2)[1]
													.split(ConstantsValue.RECARGO_PAGO)[0]
													.replace("###", "").trim()));

								}
								break;
							default:
								break;
						}

						if (modelo.getFormaPago() == 0) {
							modelo.setFormaPago(fn.formaPagoSring(dato));
						}
						if (modelo.getFormaPago() == 0 && dato.contains("Unica")) {
							modelo.setFormaPago(1);
						}

					}

					if (dato.contains(ConstantsValue.MONEDA)) {

						switch (dato.split("###").length) {
							case 7:
								if (dato.split("###")[4].trim().equals(ConstantsValue.MONEDA)
										&& dato.split("###")[6].contains(ConstantsValue.IVA)) {
									modelo.setMoneda(fn.moneda(dato.split("###")[5].trim()));
									newcontenido.append(
											fn.cleanString(dato.split("###")[6].split(ConstantsValue.IVA)[1].trim()));

									if (fn.isNumeric(newcontenido.toString())) {
										modelo.setIva(fn.castBigDecimal(fn.preparaPrimas(newcontenido.toString())));
									}
								}
								break;
							case 8:
								if (dato.split("###")[4].trim().equals(ConstantsValue.MONEDA)
										&& dato.split("###")[6].contains(ConstantsValue.FRACCIONADO)) {
									modelo.setMoneda(fn.moneda(dato.split("###")[5].trim()));

								}
								if (dato.contains("I.V.A.") && dato.contains("%")) {

									modelo.setIva(fn.castBigDecimal(
											fn.preparaPrimas(dato.toString().split("I.V.A.")[1].split("%")[1]
													.replace("###", "").trim())));
								} else if (dato.contains("I.V.A.")) {

									modelo.setIva(fn.castBigDecimal(fn.preparaPrimas(
											dato.toString().split("I.V.A.")[1].replace("###", "").trim())));

								}

								break;
							case 10:
								if (dato.contains(ConstantsValue.MONEDA) && dato.contains(ConstantsValue.FRACCIONADO)) {
									modelo.setMoneda(fn.moneda(
											dato.split(ConstantsValue.MONEDA)[1].split(ConstantsValue.FRACCIONADO)[0]
													.replace("###", "").trim()));

								}
								break;
							default:
								break;
						}

						if (modelo.getMoneda() == 0) {
							modelo.setMoneda(fn.buscaMonedaEnTexto(dato));
						}
					}

					if (dato.contains("de Póliza")) {
						//System.out.println("entro");
						dato = dato.replace("######### ###", "###").trim();

						if (dato.split("###").length == 2 && dato.split("###")[0].contains("Póliza")) {
							modelo.setDerecho(fn.castBigDecimal(fn.preparaPrimas(dato.split("###")[1].trim())));
						}
						if (dato.split("###").length == 3 && dato.split("###")[1].contains("Póliza")) {
							modelo.setDerecho(fn.castBigDecimal(fn.preparaPrimas(dato.split("###")[2].trim())));
						}
						if (dato.split("###").length == 8 && dato.split("###")[6].contains("Póliza")) {
							modelo.setDerecho(fn.castBigDecimal(fn.preparaPrimas(dato.split("###")[7].trim())));
						}
						if (dato.split("###").length == 2 && dato.split("###")[6].contains("Derecho de Póliza")) {
							modelo.setDerecho(fn.castBigDecimal(fn.preparaPrimas(dato.split("###")[7].trim())));
						}
						if (dato.contains("Derecho de Póliza")) {
							// System.out.println("dato: " + dato);
							// System.out.println("entro");
							
							String[] parts = dato.split("###");
							
							for (int i = 0; i < parts.length; i++) {
								if (parts[i].trim().equals("Derecho de Póliza")) {
									//System.out.println("entro1");
									if (i + 1 < parts.length) {
										String derechoDePoliza = parts[i + 1].trim();
										//System.out.println("Derecho de Póliza encontrado: " + derechoDePoliza);
										modelo.setDerecho(fn.castBigDecimal(fn.preparaPrimas(derechoDePoliza)));
									}
									break;
								}

							}
						}
					}

					if (dato.contains(ConstantsValue.IVA)) {
						;
						switch (dato.split("###").length) {
							case 1:
								if (dato.trim().split(" ").length == 2 && Double.parseDouble(
										dato.trim().split(" ")[1].replace(".", "").replace(",", "").trim()) > 0) {
									modelo.setIva(
											fn.castBigDecimal(fn.preparaPrimas(dato.trim().split(" ")[1].trim())));

								}
								break;
							case 2:
							case 3:
								newcontenido = new StringBuilder();
								if (dato.contains("%")) {
									newcontenido.append(fn.cleanString(dato.split("%")[1].replace("###", "")));
								} else {
									newcontenido.append(fn.cleanString(dato.split("###")[2]));
								}

								if (fn.isNumeric(newcontenido.toString())) {
									modelo.setIva(fn.castBigDecimal(fn.preparaPrimas(newcontenido.toString())));
								}
								break;
							case 4:

								List<String> valores = fn.obtenerListNumeros(dato);
								modelo.setIva(fn.castBigDecimal(fn.castDouble(valores.get(0))));
								break;
							default:
								break;
						}
					}

					if (dato.contains(ConstantsValue.FRACCIONADO) && dato.split("###").length == 8
							&& dato.split("###")[6].trim().equals(ConstantsValue.FRACCIONADO)) {
						modelo.setRecargo(fn.castBigDecimal(fn.preparaPrimas(dato.split("###")[7].trim())));
					}

					if (dato.contains("Cesión de Comisión")) {
						if (dato.contains("0.00.")) {
							modelo.setAjusteUno(fn.castBigDecimal(fn.preparaPrimas(
									dato.split("Cesión de Comisión")[1].replace("0.00.", "00.").replace("−", "")
											.replace("###", ""))));
						} else {
							List<String> valores = fn.obtenerListNumeros(
									dato.split("Cesión de Comisión")[1].replace("−", "-").replace("###", ""));
							if (!valores.isEmpty()) {
								modelo.setAjusteUno(fn.castBigDecimal(fn.castDouble(valores.get(0))));
							}
						}

					}
				}
			}

			// prima_total
			donde = 0;
			donde = fn.recorreContenido(contenido, "Grupo###Nacional");
			// donde = fn.recorreContenido(contenido, "Pagar###");
			if (donde > 0) {
				for (String dato : contenido.split("@@@")[donde].split("\r\n")) {
					 //System.out.println("entro");
					if (dato.contains(ConstantsValue.PAGAR) && dato.split("###").length == 2
							&& dato.split("###")[0].contains(ConstantsValue.PAGAR)) {
						modelo.setPrimaTotal(fn.castBigDecimal(fn.preparaPrimas(dato.split("###")[1].trim())));
					}

					if (modelo.getPrimaTotal().doubleValue() == 0 && dato.contains("Total a Pagar")) {
						modelo.setPrimaTotal(fn.castBigDecimal(
								fn.preparaPrimas(dato.split("Total a Pagar")[1].replace("###", "").trim())));
					}
					
					 if (dato.contains(ConstantsValue.PAGAR)||dato.contains("Pagar###") &&
					 dato.split("###").length == 2
					 && dato.split("###")[0].contains(ConstantsValue.PAGAR)) {
					System.out.println("prima");
					 modelo.setPrimaTotal(fn.castBigDecimal(fn.preparaPrimas(dato.split("###")[0].trim())));
					 }

				}
			}

			// for (String dato : contenido.split("@@@")[donde].split("\r\n")) {
			// System.out.println("entro");
			// if (dato.contains(ConstantsValue.PAGAR)||dato.contains("Pagar") &&
			// dato.split("###").length == 2
			// && dato.split("###")[0].contains(ConstantsValue.PAGAR)) {
			// System.out.println("prima");
			// modelo.setPrimaTotal(fn.castBigDecimal(fn.preparaPrimas(dato.split("###")[6].trim())));
			// }
			// }

			if (modelo.getPrimaTotal().floatValue() == 0) {
				donde = 0;
				donde = fn.recorreContenido(contenido, ConstantsValue.PAGAR);
				if (donde > 0) {
					for (String dato : contenido.split("@@@")[donde].split("\r\n")) {

						if (dato.contains(ConstantsValue.PAGAR)) {
							switch (dato.split("###").length) {
								case 2:
									if (dato.split("###")[0].trim().equals(ConstantsValue.PAGAR)) {

										modelo.setPrimaTotal(
												fn.castBigDecimal(fn.preparaPrimas(dato.split("###")[1].trim())));
									}
									break;
								case 3:
									if (dato.split("###")[1].trim().equals(ConstantsValue.PAGAR)) {
										modelo.setPrimaTotal(
												fn.castBigDecimal(fn.preparaPrimas(dato.split("###")[2].trim())));
									}
									break;
								case 4:
									if (dato.split("###")[2].trim().equals(ConstantsValue.PAGAR)) {
										modelo.setPrimaTotal(
												fn.castBigDecimal(fn.preparaPrimas(dato.split("###")[3].trim())));
									}
									break;
								default:
									break;
							}
						}
					}
				}
			}

			 donde = fn.recorreContenido(contenido, "Pagar###");
System.out.println("Índice encontrado: " + donde);
if (donde >= 0) {
    String[] lineas = contenido.split("\r\n");
    for (String linea : lineas) {
        System.out.println("entro");
        System.out.println("dato " + linea);
        if (linea.contains("Pagar###")) {
            String[] partes = linea.split("###");
            System.out.println("Partes: " + Arrays.toString(partes));
            if (partes.length == 5 && partes[3].contains("Pagar")) {
                String valorPagar = partes[4].trim();
                System.out.println("Valor Pagar antes de conversión: " + valorPagar);
                modelo.setPrimaTotal(fn.castBigDecimal(fn.preparaPrimas(valorPagar)));
                System.out.println("Valor Pagar: " + valorPagar);
            }
        }
    }
}

			
			
			// hola@@@mundo@@@prima de la poliza@@@vidal

			int posInicio = contenido.lastIndexOf("Prima de la Póliza");
			int posFin = contenido.indexOf("Descripción del Movimiento");

			// Depuración de las posiciones
			// System.out.println("Posición inicio: " + posInicio);
			// System.out.println("Posición fin: " + posFin);

			if (posInicio > -1 && posFin > posInicio) {
				
				// Ajustar la posición de inicio correctamente
				posInicio += "Prima de la Póliza".length();

				// Extraer el contenido entre inicio y fin
				String extractedContent = contenido.substring(posInicio, posFin).replace("@@@", "").trim();
				newcontenido.append(extractedContent);

				// Imprimir para depuración
				 //System.out.println("Contenido extraído: " + extractedContent);
				 //System.out.println("newcontenido: " + newcontenido.toString());

				// Procesar cada línea del nuevo contenido para buscar "prima total"
				for (String x : newcontenido.toString().split("\r\n")) {
					x = fn.gatos(x);

					// Imprimir cada línea para depuración
					 //System.out.println("Línea procesada: " + x);

					if (x.trim().length() > 0) {
						 //System.out.println("entro");
						if (x.contains("Pagar###")) {
							// Extraer y procesar la prima total
							String primaTotal = x.split("Pagar###")[1].trim();
							//System.out.println("Prima total encontrada: " + primaTotal);
							// Puedes almacenar o procesar la prima total como desees
							break; // Salir del bucle si ya encontraste la prima total
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
							case 3:
								String plan = fn.gatos(dato.split("Plan")[1].trim());
								modelo.setPlan(plan.split("###")[0].trim());
								break;
							case 4:
								if (dato.split("###")[0].trim().equals("Plan")
										&& dato.split("###")[1].contains("Día")) {
									modelo.setPlan(dato.split("###")[1].split("Día")[0].trim());
								}
								break;
							case 2:
								if (dato.split("###")[0].trim().equals("Plan")
										&& dato.split("###")[1].contains(ConstantsValue.VIGENCIA2)) {
									modelo.setPlan(dato.split("###")[1].split(ConstantsValue.VIGENCIA2)[0].trim());
								} else if (dato.split("###")[0].trim().equals("Plan")
										&& !dato.split("###")[1].contains(ConstantsValue.VIGENCIA2)) {

									modelo.setPlan(dato.split("###")[1].trim());
								}
								break;
							case 6:
								if (dato.split("###")[2].contains("Desde las")) {
									modelo.setPlan(dato.split("###")[1].trim());
								}
								break;
							default:
								break;
						}
					}
				}
			}

			inicio = contenido.indexOf(ConstantsValue.CLAVE2);
			if (inicio > 0) {
				String cont = contenido.split(ConstantsValue.CLAVE2)[1].split("\r\n")[0].replace("### ###", "###");

				if (cont.split("###").length > 2) {
					modelo.setCveAgente(cont.split("###")[1]);
				} else {
					modelo.setCveAgente(fn.gatos(contenido.split(ConstantsValue.CLAVE2)[1].split("\r\n")[0]));
				}

			}

			// cve_agente

			// agente
			inicio = contenido.lastIndexOf(ConstantsValue.AGENTE2);
			fin = contenido.indexOf("dls=");
			if (inicio > -1 && fin > inicio) {
				newcontenido = new StringBuilder();
				newcontenido.append(contenido.substring(inicio + 6, fin).replace("@@@", "").trim());
				for (String x : newcontenido.toString().split("\r\n")) {
					x = fn.gatos(x);
					if (x.trim().length() > 0) {
						if (x.contains(ConstantsValue.CLAVE2)) {
							agente.append(fn.gatos(x.split(ConstantsValue.CLAVE2)[0])).append(" ");
						} else {
							agente.append(x);
						}
					}
				}
				if (agente.length() > 0) {
					modelo.setAgente((agente.toString().replace("### ###", "")).replace("###", "").trim());
				}
			}

			// sa
			// deducible
			// coaseguro_tope (coaseguro maximo,tope coaseguro, tope coaseguro)
			// coaseguro
			// deducible_ext
			String coaseguros = contenido.split(ConstantsValue.COASASEGURO)[1].split(ConstantsValue.AGENTE2)[0]
					.replace(ConstantsValue.SIN_LIMITE, "Sin Límite###").replace("dls ", "dls###")
					.replace("pesos", "###pesos###").replace(ConstantsValue.HASH2, "###").replace("SMGM", "###SMGM###");

			int a1 = coaseguros.indexOf(ConstantsValue.NACIONAL);
			int b2 = coaseguros.indexOf(ConstantsValue.EXTRANJERO);

			String dtcoberturas = " ";
			if (b2 > 0 && a1 > 0) {
				dtcoberturas = coaseguros.substring(a1, a1 + 100) + "\n" + coaseguros.substring(b2, b2 + 70);
			} else {

				if (a1 > -1) {
					dtcoberturas = coaseguros.substring(a1, a1 + 100);
				} else {
					if (b2 > -1) {
						dtcoberturas = coaseguros.substring(b2, b2 + 100);
					}

				}
			}

			if (dtcoberturas.length() > 10) {
				for (String A : dtcoberturas.split("\n")) {

					if (A.contains(ConstantsValue.EXTRANJERO)) {
						int n = A.split("###").length;

						if (n == 7) {
							modelo.setDeducibleExt(A.split("###")[3].trim());
						}
						if (n == 5 || n == 6) {
							modelo.setDeducibleExt(A.split("###")[2].trim());
						}
					}
					if (A.contains(ConstantsValue.NACIONAL) || (A.contains(ConstantsValue.NACIONAL))) {
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
							modelo.setDeducible((A.split("###")[3].trim().length() > 10 ? A.split("###")[1].trim()
									: A.split("###")[4].replace("\r", "").trim()));
							modelo.setCoaseguro((A.split("###")[5].trim().length() > 10 ? A.split("###")[1].trim()
									: A.split("###")[7].replace("\r", "").trim()));
						} else if (n == 7 || n == 9 || n == 8) {
							modelo.setSa(A.split("###")[1].trim());
							A = A.replace("###pesos", "pesos");
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

			}

			String a = contenido.split("Fecha de Expedición")[1].split("Moneda")[0];
			modelo.setFechaEmision(a.split("###")[3] + "-" + a.split("###")[2] + "-" + a.split("###")[1]);

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

				newcontenido = new StringBuilder();
				filtrado = contenido.substring(inicio, fin).replace("@@@", "").trim();

				if (filtrado.contains("Ver listado de Asegurados") && txtasegurados.length() > 0) {
					// AL NO TRAER ASEGURADOS TRABAJAMOS CON LA VARIABLE QUE LLEGA

					inicio = txtasegurados.indexOf("Nacional###Extranjero");
					if (inicio == -1) {
						inicio = txtasegurados.indexOf("Nombre###Nacional");
					}
					fin = txtasegurados.indexOf("Para mayor información");
					if (inicio > -1 && fin > inicio) {
						filtrado = fn.gatos(txtasegurados.substring(inicio + 21, fin).replace("@@@", "")).trim();
					}
				}

				for (String dato : filtrado.split("\r\n")) {
					if (dato.trim().split("-").length == 3 || dato.trim().split("-").length == 5) {
						if (dato.contains("CARTERA")) {
							newcontenido
									.append(dato.split("CARTERA")[0].replace("###", "   ").trim().replace("   ", "###"))
									.append("\r\n");
						} else if (dato.contains("Renovación")) {
							newcontenido.append(
									dato.split("Renovación")[0].replace("###", "   ").trim().replace("   ", "###"))
									.append("\r\n");
						} else if (dato.contains("Nacional")) {
							newcontenido.append(
									dato.split("Nacional")[1].replace("###", "   ").trim().replace("   ", "###"))
									.append("\r\n");
						} else if (dato.contains("Descripción")) {
							newcontenido.append(
									dato.split("Descripción")[0].replace("###", "   ").trim().replace("   ", "###"))
									.append("\r\n");
						} else if (dato.contains("D escripción")) {
							newcontenido.append(
									dato.split("D escripción")[0].replace("###", "   ").trim().replace("   ", "###"))
									.append("\r\n");
						} else if (dato.contains("Cambio")) {
							newcontenido
									.append(dato.split("Cambio")[0].replace("###", "   ").trim().replace("   ", "###"))
									.append("\r\n");
						} else if (dato.contains("Sustitución")) {
							newcontenido.append(
									dato.split("Sustitución")[0].replace("###", "   ").trim().replace("   ", "###"))
									.append("\r\n");
						} else if (dato.contains("###D ###escripción")) {
							newcontenido.append(
									dato.split("###D ###escripción")[0].replace("###", "   ").trim().replace("   ",
											"###"))
									.append("\r\n");
						} else {
							newcontenido.append(dato.replace("###", "   ").trim().replace("   ", "###")).append("\r\n");
						}
					}
				}

				if (newcontenido.length() > 0) {

					for (String dato : newcontenido.toString().split("\r\n")) {
						EstructuraAseguradosModel asegurado = new EstructuraAseguradosModel();
						switch (dato.split("###").length) {
							case 2:
								if (dato.split("###")[1].trim().length() > 15) {
									longitudSplit = dato.split("###")[1].split(" ").length - 1;
									asegurado.setAntiguedad(
											fn.formatDate(dato.split("###")[1].trim().split(" ")[longitudSplit].trim(),
													ConstantsValue.FORMATO_FECHA));
									asegurado.setNombre(dato.split("###")[1]
											.split(dato.split("###")[1].trim().split(" ")[longitudSplit])[0].trim());
									asegurado.setSexo(1);
									asegurado.setParentesco(asegurados.isEmpty() ? 1 : 4);
									asegurados.add(asegurado);

								}
								break;
							case 3:
							case 4:
								asegurado.setAntiguedad(
										fn.formatDate(dato.split("###")[2].trim(), ConstantsValue.FORMATO_FECHA));
								asegurado.setNombre(dato.split("###")[1].trim());
								asegurado.setSexo(1);
								asegurado.setParentesco(asegurados.isEmpty() ? 1 : 4);
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

			String[] coberturasTxt = { "Nacional", "B ###ásica", "E mergencia de gastos médico s",
					"Emergencia de gastos médico s", "E ###mergencia de gastos médico s",
					"Emergencia en el Extranjero", "− Extranjero", "E mergencia en el Extranjero",
					"Catastróficas Nacional",
					"C atastróficas Nacional", "Enfermedades Catastróficas", "n el Extranjero", "Asistencia en Viajes",
					"A sistencia en Viajes", "Membresía Médica Móvil", "M embresía Médica Móvil",
					"Cero Deducible por Accidente", "C ero Deducible por Accidente", "ero Deducible por Accidente",
					"R espaldo por Fallecimiento", "Atención Hospitales", "Respaldo Hospitalario",
					"Reducción de Deducible" };

			donde = fn.recorreContenido(contenido, ConstantsValue.COASASEGURO);
			dondeAux = fn.recorreContenido(contenido, ConstantsValue.AGENTE2);
			dondeAux = dondeAux == 0 ? fn.recorreContenido(contenido, "En caso de requerir mayor") : dondeAux;

			String coberturasext = "";

			if (donde > 0 && dondeAux > 0) {
				inicio = contenido.indexOf(contenido.split("@@@")[donde]);
				fin = contenido.indexOf(contenido.split("@@@")[dondeAux]);
				filtrado = contenido.substring(inicio, fin).replace("@@@", "").replace("A ###sistencia", "Asistencia")
						.replace("E ###mergencia", "Emergencia").trim();
				coberturasext = filtrado;
				filtrado = "";
				for (int i = 0; i < coberturasext.split("\n").length; i++) {
					if (coberturasext.split("\n")[i].contains("− Nacional")
							&& coberturasext.split("\n")[i].contains("Desde")) {
					} else {

						filtrado += coberturasext.split("\n")[i] + "\n";
					}
				}

				if (filtrado.contains(ConstantsValue.COASASEGURO) && filtrado.contains("Coberturas")) {
					newcontenido = new StringBuilder();
					int inicioCob = 0;
					for (String cobertura : coberturasTxt) {
						newcontenido1 = "";
						contenidoCoberturas = filtrado.split(ConstantsValue.COASASEGURO)[1].split("Coberturas")[0]
								.trim();
						inicioCob = contenidoCoberturas.indexOf(cobertura);
						if (inicioCob > -1) {

							if (cobertura.equals("− Nacional")) { // CUANDO VIENE DOS VESES NACIONAL
								if (encontroNal) {
									inicioCob = 0;
									inicioCob = contenidoCoberturas.lastIndexOf(cobertura);
								}

								if (encontroNal) {
									encontroNacional = inicioCob;
									newcontenido1 = contenidoCoberturas
											.substring(inicioCob, contenidoCoberturas.indexOf("\r\n", inicioCob))
											.trim();
									encontroNal = true;
								} else if (encontroNal && encontroNacional != inicioCob && contenidoCoberturas
										.substring(inicioCob, inicioCob + 80).split("\n").length == 2) {

									newcontenido1 = contenidoCoberturas.substring(inicioCob, (inicioCob + 90))
											.split("\r\n")[0].trim();
								}

							} else if (cobertura.equals(ConstantsValue.EXTRANJERO)) { // CUANDO VIENE DOS VESES
																						// EXTRANGERO
								if (encontroExt) {
									inicioCob = 0;
									inicioCob = contenidoCoberturas.lastIndexOf(cobertura);
								}
								if (!encontroExt) {
									encontroExtranjero = inicioCob;
									newcontenido1 = contenidoCoberturas
											.substring(inicioCob, contenidoCoberturas.indexOf("\r\n", inicioCob))
											.trim();
									encontroExt = true;
								} else if (encontroExt && encontroExtranjero != inicioCob && contenidoCoberturas
										.substring(inicioCob, inicioCob + 80).split("\n").length == 2) {
									newcontenido1 = contenidoCoberturas.substring(inicioCob, (inicioCob + 90))
											.split("\r\n")[0].trim();
								}
							} else if (cobertura.equals("n el Extranjero")) {// CUANDO TRAE "ENFERMEDADES CATASTRÓFICAS
																				// EN EL EXTRANJERO"
								if (contenidoCoberturas.substring(inicioCob - 12, inicioCob)
										.contains(ConstantsValue.ERGEN)) {// Emergencias
									inicioCob = 0;
									inicioCob = contenidoCoberturas.lastIndexOf(cobertura); // BUSCA EL ULTIMO "n el
																							// Extranjero"
									if (inicioCob > 0 && !contenidoCoberturas
											.substring(inicioCob - 12, contenidoCoberturas.indexOf("\r\n", inicioCob))
											.contains("ergen")) {
										newcontenido1 = "e" + contenidoCoberturas
												.substring(inicioCob, contenidoCoberturas.indexOf("\r\n", inicioCob))
												.trim();
									}
								}
							} else if (cobertura.equals("B ###ásica")) {
								StringBuilder strbCobertura = new StringBuilder();
								inicio = contenidoCoberturas.indexOf("B ###ásica");
								fin = contenidoCoberturas.indexOf("%");
								if (fin == -1) {
									fin = contenidoCoberturas.indexOf("Desde las");
								} else {
									fin = fin + 4;
								}
								if (inicio > -1 && fin > -1 && inicio < fin) {
									String textoFiltrado = contenidoCoberturas.substring(inicio, fin);
									String[] detalleCoberturas = textoFiltrado.split("\n");
									if (detalleCoberturas.length > 0) {
										if (detalleCoberturas[1].contains("acional")) {
											strbCobertura.append("Básica");
											;
											strbCobertura.append(detalleCoberturas[1]);
											newcontenido1 = strbCobertura.toString().replace("\r", "")
													.replace("###−", "-").trim();
										}
									}
								}
							} else if (cobertura.equals("E ###mergencia de gastos médico s")) {
								StringBuilder strbCobertura = new StringBuilder();
								String textoFiltrado = contenidoCoberturas
										.split("E ###mergencia de gastos médico s")[1];
								String[] detalleCoberturas = textoFiltrado.split("\n");

								if (detalleCoberturas.length > 3
										&& detalleCoberturas[1].contains("mayores no cubiertos")) {
									strbCobertura.append("Emergencia de gastos médicos mayores no cubiertos");
									strbCobertura.append(detalleCoberturas[2]);
									newcontenido1 = strbCobertura.toString().replace("\r", "").replace("###−", "-")
											.trim();

								}

							} else {// TODAS LAS DEMAS COBETURAS
								if (!newcontenido.toString().contains(cobertura)) {

									newcontenido1 = contenidoCoberturas
											.substring(inicioCob, contenidoCoberturas.indexOf("\r\n", inicioCob))
											.trim();
								}
							}

							if (newcontenido1.length() > 0) {
								if (newcontenido1.contains(ConstantsValue.PRIMA)) { // SI TRAE MAS CONTENIDO LA LINEA,
																					// SE REMUEVEN
									// EXTRAS
									newcontenido1 = newcontenido1.split(ConstantsValue.PRIMA)[0].trim();
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
								} else if (newcontenido1.contains(ConstantsValue.IMPORTE)) {
									newcontenido1 = newcontenido1.split(ConstantsValue.IMPORTE)[0].trim();
								} else if (newcontenido1.contains("Pagar")) {
									newcontenido1 = newcontenido1.split("Pagar")[0].trim();
								} else if (newcontenido1.contains("Derecho de")) {
									newcontenido1 = newcontenido1.split("Derecho de")[0].trim();
								} else if (newcontenido1.contains("I.V.A.")) {
									newcontenido1 = newcontenido1.split("I.V.A.")[0].trim();
								} else if (newcontenido1.contains("Fraccionado")) {
									newcontenido1 = newcontenido1.split("Fraccionado")[0].trim();
								} else if (newcontenido1.contains(ConstantsValue.DURACION)) {
									newcontenido1 = newcontenido1.split(ConstantsValue.DURACION)[0].trim();
								} else if (newcontenido1.contains("Resumen")) {
									newcontenido1 = newcontenido1.split("Resumen")[0].trim();
								} else if (newcontenido1.contains("Deducible por")
										&& !newcontenido1.contains(ConstantsValue.AMPARADA)) {
									newcontenido1 = newcontenido1 + "###" + "Amparada ###";
								} else if (newcontenido1.contains("en el Extranjero")
										&& newcontenido1.substring(0, 10).contains("en el E")) {
									newcontenido1 = "Enfermedades Catastróficas " + newcontenido1;
								}

								///////////////////////////////////////////////// SI LOS ULTIMOS 3 CARACTERES ES
								///////////////////////////////////////////////// ### LOS QUITA

								// longitudTexto = 0;
								longitudTexto = newcontenido1.length();
								if (newcontenido1.substring(longitudTexto - 3, longitudTexto).equals("###")) {
									newcontenido.append(newcontenido1.substring(0, longitudTexto - 3).trim()
											.replace(ConstantsValue.HASH2, "###")).append("\r\n");
								} else {
									newcontenido.append(contenidoCoberturas
											.substring(inicioCob, contenidoCoberturas.indexOf("\r\n", inicioCob))
											.replace(ConstantsValue.HASH2, "###")).append("\r\n");
								}
							}
						}
					}

					StringBuilder datotexto;
					for (String dato : newcontenido.toString().split("\r\n")) {
						EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();
						datotexto = new StringBuilder();
						datotexto.append(dato.replace("−", "").trim());
						String auxStr = datotexto.toString();
						if (datotexto.toString().contains("E mergencia de gastos médico s")
								|| datotexto.toString().contains("Emergencia de gastos médico s")) {

							datotexto = new StringBuilder();
							datotexto.append("Emergencia de gastos médicos mayores no cubiertos");

						} else if (datotexto.toString().contains("Catastróficas")
								&& !datotexto.toString().contains("Enfermedades")
								|| datotexto.toString().contains("C atastróficas")
										&& !datotexto.toString().contains("Enfermedades")) {
							datotexto = new StringBuilder();
							datotexto.append("Enfermedades ").append(auxStr);

						} else if (datotexto.toString().contains("n el Extranjero")
								&& !datotexto.toString().contains("ergen")
								&& !datotexto.toString().contains("Enfermedades Catastróficas en el Extranjero")) {
							datotexto = new StringBuilder();
							datotexto.append("Enfermedades Catastróficas ").append(auxStr);
						}

						switch (datotexto.toString().split("###").length) {
							case 4:

								cobertura.setNombre(datotexto.toString().split("###")[0].trim());
								cobertura.setSa(datotexto.toString().split("###")[1].trim());
								cobertura.setDeducible(datotexto.toString().split("###")[2].trim());
								cobertura.setCoaseguro(datotexto.toString().split("###")[3].trim());
								coberturas.add(cobertura);
								break;
							case 3:

								if (datotexto.toString().split("###")[2].length() == 3
										&& datotexto.toString().split("###")[2].contains("%")) {
									cobertura.setCoaseguro(datotexto.toString().split("###")[2].trim());
								}

								if (datotexto.toString().split("###")[1].trim().split(" ").length == 2) {
									cobertura.setDeducible(datotexto.toString().split("###")[1].trim());
								}

								if (datotexto.toString().split("###")[0].contains(ConstantsValue.SIN_LIMITE)) {
									cobertura.setNombre(
											datotexto.toString().split("###")[0].split(ConstantsValue.SIN_LIMITE)[0]
													.trim());
									cobertura.setSa(datotexto.toString().split("###")[0].split(cobertura.getNombre())[1]
											.trim());

								} else if (datotexto.toString().split("###")[0].split(" ").length >= 3) {
									parte = extraigoPipe(datotexto.toString().split("###")[0].trim());
									cobertura.setNombre(
											datotexto.toString().split("###")[0].trim().split(parte)[0].trim());
									cobertura
											.setSa(datotexto.toString().split("###")[0].trim()
													.split(cobertura.getNombre())[1].trim());

								}
								coberturas.add(cobertura);
								break;
							case 2:
								if (datotexto.toString().split("###")[1].trim().equals(ConstantsValue.AMPARADA)) {

									cobertura.setNombre(datotexto.toString().split("###")[0].trim());
									cobertura.setSa(datotexto.toString().split("###")[1].trim());

									coberturas.add(cobertura);
								} else {
									if (datotexto.toString().contains("por Fallecimie")) {
										cobertura.setNombre(datotexto.toString().split("###")[0].trim());
										cobertura.setSa(datotexto.toString().split("###")[1].trim());
										coberturas.add(cobertura);

									} else {
										newcontenido2 = datotexto.toString().split("###")[1];
										if (newcontenido2.split(" ").length == 5) {
											cobertura.setNombre(datotexto.toString().split("###")[0].trim());
											cobertura.setSa(datotexto.toString().split("###")[1].trim());
											cobertura.setDeducible(newcontenido2.split(" ")[1].trim());
											cobertura.setCoaseguro(newcontenido2.split(" ")[1].trim());
											coberturas.add(cobertura);
											if (newcontenido2.split(" ").length == 2) {
												parte = extraigoPipe(datotexto.toString().split("###")[0].trim());
												cobertura
														.setNombre(datotexto.toString().split("###")[0].trim()
																.split(parte)[0].trim());
												cobertura.setSa(
														datotexto.toString().split("###")[0].trim()
																.split(cobertura.getNombre())[1]
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
								if (datotexto.toString().contains("Emergencia de gastos")) {
									cobertura.setNombre(datotexto.toString());
									coberturas.add(cobertura);
								} else if (datotexto.toString().contains(ConstantsValue.AMPARADA)) {
									cobertura.setNombre(datotexto.toString().split(ConstantsValue.AMPARADA)[0].trim());
									cobertura.setSa(datotexto.toString().split(cobertura.getSa())[1].trim());
									coberturas.add(cobertura);
								}
								break;
							default:
								break;
						}
					}
				}
			}
			modelo.setCoberturas(coberturas);

			modelo.setIdCliente(
					contenido.split("Código Cliente")[1].split(ConstantsValue.DURACION)[0].replace("###", "").trim());

			if (modelo.getCteNombre().length() == 0 && modelo.getRfc().length() == 0
					&& modelo.getVigenciaDe().length() == 0 && modelo.getVigenciaA().length() == 0
					&& modelo.getCp().length() == 0) {
				newcontenido = new StringBuilder();
				inicio = contenido.indexOf(ConstantsValue.RENOVACION);
				fin = contenido.indexOf("Código Cliente");

				newcontenido.append(fn.extracted(inicio, fin, contenido).replace("las 12 hrs. del", ""));
				for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {

					if (newcontenido.toString().split("\n")[i].contains("Contratante")
							&& newcontenido.toString().split("\n")[i + 1].length() > 10) {

						modelo.setCteNombre(newcontenido.toString().split("\n")[i + 1].replace("###", ""));
					}
					if (newcontenido.toString().split("\n")[i].contains("Desde")
							&& newcontenido.toString().split("\n")[i].contains("C.P")) {
						modelo.setCp(newcontenido.toString().split("\n")[i].split("C.P")[1].split("Desde")[0]
								.replace("###", "").trim());
						modelo.setVigenciaDe(fn.formatDateMonthCadena(
								fn.elimgatos(newcontenido.toString().split("\n")[i].split("Desde")[1]).replace("###",
										"-")));
					}
					if (newcontenido.toString().split("\n")[i].contains("Hasta")
							&& newcontenido.toString().split("\n")[i].contains("R.F.C:")) {
						modelo.setRfc(newcontenido.toString().split("\n")[i].split("R.F.C:")[1].split("Hasta")[0]
								.replace("###", "").trim());
						modelo.setVigenciaA(fn.formatDateMonthCadena(
								fn.elimgatos(newcontenido.toString().split("\n")[i].split("Hasta")[1]).replace("###",
										"-")));
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
				default:
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
						newcontenido = new StringBuilder();
						newcontenido.append(x.substring(inicio + 10, inicio + 150).split("\n")[0]);

						if (newcontenido.toString().contains(ConstantsValue.IMPORTE)) {

							nacimiento = fn.formatDate(
									fn.gatos(newcontenido.toString().split(ConstantsValue.IMPORTE)[0].trim())
											.replace("###", "-"),
									ConstantsValue.FORMATO_FECHA);

						}
					}

					inicio = x.indexOf("digo Cliente");
					if (inicio == 0) {
						inicio = x.indexOf("Código Cliente");
					}

					if (inicio > -1) {
						newcontenido = new StringBuilder();
						newcontenido.append(x.substring(inicio + 12, x.indexOf("\n", inicio + 12)));

						if (newcontenido.toString().contains("Prima")) {
							certificado = fn.gatos(newcontenido.toString().split("Prima")[0]).replace("###", "").trim();
						}
					}
					inicio = x.indexOf("Sexo:");

					if (inicio > -1) {
						newcontenido = new StringBuilder();
						newcontenido.append(fn.gatos(x.substring(inicio + 5, inicio + 150).split("\n")[0]).trim());
						sexo = (fn.sexo(newcontenido.toString().replace("###", "").trim()).booleanValue()) ? 1 : 0;

					}
					if (nombre.length() > 0) {

						for (int i = 0; i < asegurados.size(); i++) {
							if (asegurados.get(i).getNombre().equals(nombre)) {
								EstructuraAseguradosModel asegurado = asegurados.get(i);
								asegurado.setNacimiento(nacimiento);
								asegurado.setSexo(sexo);
								asegurado.setCertificado(certificado);
							}
						}
					}
				}
			}

			if (modelo.getDeducible().contains("Desde")) {
				modelo.setDeducible("");
			}

			if (modelo.getAsegurados().isEmpty() && txtasegurados.length() > 100) {
				inicio = txtasegurados.indexOf("Asegurado s");
				fin = txtasegurados.indexOf("En caso de requerir mayor");

				filtrado = txtasegurados.substring(inicio, fin).replace("@@@", "").trim();
				if (filtrado.length() > 0) {
					for (int i = 0; i < filtrado.split("\n").length; i++) {
						EstructuraAseguradosModel asegurado = new EstructuraAseguradosModel();
						if (filtrado.split("\n")[i].split("-").length > 2) {
							asegurado.setNombre(filtrado.split("\n")[i].split("###")[1]);
							List<String> valores = fn.obtenVigePoliza(filtrado.toString().split("\n")[i]);
							asegurado.setAntiguedad(fn.formatDateMonthCadena(valores.get(0)));
							asegurados.add(asegurado);
						}
					}

				}
			}
			StringBuilder fechas = new StringBuilder();
			for (int i = 0; i < fechAsegurados.split("Fecha de Nacimiento").length; i++) {

				if (i > 0) {
					fechas.append(fechAsegurados.split("Fecha de Nacimiento")[i].split("Importe Total")[0]
							.replace("###", "-"));
				}
			}
			if (!fechas.isEmpty() && fn.obtenVigePoliza(fechas.toString()).size() == modelo.getAsegurados().size()) {
				List<EstructuraAseguradosModel> aseg = modelo.getAsegurados();
				List<EstructuraAseguradosModel> aseguradosex = new ArrayList<>();

				for (int i = 0; i < aseg.size(); i++) {
					EstructuraAseguradosModel asegurado = new EstructuraAseguradosModel();
					asegurado.setNombre(aseg.get(i).getNombre());
					asegurado.setAntiguedad(aseg.get(i).getAntiguedad());

					asegurado.setNacimiento(fn.formatDateMonthCadena(fn.obtenVigePoliza(fechas.toString()).get(i)));
					aseguradosex.add(asegurado);

				}

				modelo.setAsegurados(new ArrayList<>());
				modelo.setAsegurados(aseguradosex);

			}

			return modelo;
		} catch (Exception ex) {
			modelo.setError(
					GnpSaludModel.this.getClass().getTypeName() + " | " + ex.getMessage() + " | " + ex.getCause());
			return modelo;
		}
	}

	private String extraigoPipe(String txtfull) {// EXTRAE LOS 2 PRIMEROS NUMEROS QUE ENCUENTRA DE
		StringBuilder texto = new StringBuilder();
		int contador = 0;
		for (int i = 0; i < txtfull.trim().length(); i++) {
			try {
				if (Double.parseDouble(Character.toString(txtfull.charAt(i))) >= 0) {
					texto.append(txtfull.charAt(i));
					contador++;
					if (contador == 2) {
						break;
					}
				}
			} catch (java.lang.NumberFormatException e) {
				if (contador > 0) {
					texto.append(txtfull.charAt(i));
					break;
				}

			}
		}

		return texto.toString();
	}

}
