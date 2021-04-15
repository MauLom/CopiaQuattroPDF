package com.copsis.models.axa;


import java.util.ArrayList;
import java.util.List;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraAseguradosModel;
import com.copsis.models.EstructuraBeneficiariosModel;
import com.copsis.models.EstructuraCoberturasModel;
import com.copsis.models.EstructuraJsonModel;
import com.copsis.models.EstructuraRecibosModel;

public class AxaVidaModel {

	// Clases
	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
	// Varaibles
	public String inicontenido = "";
	private String contenido = "";
	public String newcontenido = "";
	private String resultado = "";
	private int inicio = 0;
	private int fin = 0;
	private int donde = 0;


	public AxaVidaModel(String contenido) {
		this.contenido = contenido;
	}

	public EstructuraJsonModel procesar() {
		inicontenido = fn.fixContenido(contenido);
		contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());
		try {

			modelo.setTipo(5);
			modelo.setCia(20);
			newcontenido = fn.filtroPorRango(contenido, 5);
			donde = fn.recorreContenido(newcontenido, "Contratante Póliza");
			if (donde > 0) {
				for (String dato : newcontenido.split("@@@")[donde].split("\r\n")) {
					if (dato.contains("Contratante Póliza") && dato.split("###").length == 1) {
						modelo.setPoliza(dato.split("Póliza")[1].trim());
					}
				}
			}

			donde = 0;
			newcontenido = fn.filtroPorRango(contenido, 5);
			if (fn.recorreContenido(newcontenido, "###RFC:###") > 0) {
				donde = fn.recorreContenido(newcontenido, "###RFC:###");
			} else if (fn.recorreContenido(newcontenido, "Contratante Póliza") > 0) {
				donde = fn.recorreContenido(newcontenido, "Contratante Póliza");
			}

			if (donde > 0) {
				for (String dato : newcontenido.split("@@@")[donde].split("\r\n")) {
					if (dato.contains("###RFC:###") && dato.split("###").length == 3) {
						modelo.setCteNombre(dato.split("###")[0].split("bre")[1].trim());
					}
					if (dato.contains("Nombre") && dato.split("###").length == 2) {
						inicio = dato.split("###")[0].trim().indexOf("Nombre") + 6;
						fin = dato.split("###")[0].trim().length();
						modelo.setCteNombre(dato.split("###")[0].substring(inicio, fin).trim());
					}
				}
			}

			donde = 0;
			newcontenido = fn.filtroPorRango(contenido, 6);
			donde = fn.recorreContenido(newcontenido, "Domicilio");
			if (donde > 0) {
				for (int i = 0; i < newcontenido.split("@@@")[donde].split("\r\n").length; i++) {
					if (newcontenido.split("@@@")[donde].split("\r\n")[i].contains("Domicilio")) {
						switch (newcontenido.split("@@@")[donde].split("\r\n")[i].split("###").length) {
						case 2:
							if (newcontenido.split("@@@")[donde].split("\r\n")[i].split("###")[0]
									.contains("Domicilio")) {
								resultado = newcontenido.split("@@@")[donde].split("\r\n")[i].split("###")[0]
										.split("cilio")[1].trim();
							}
							if ((i + 2) < newcontenido.split("@@@")[donde].split("\r\n").length) {
								if (newcontenido.split("@@@")[donde].split("\r\n")[i + 2].split("###").length == 2) {
									resultado += " "
											+ newcontenido.split("@@@")[donde].split("\r\n")[i + 2].split("###")[0]
													.trim();
								}
							}
							if ((i + 1) < newcontenido.split("@@@")[donde].split("\r\n").length) {
								if (newcontenido.split("@@@")[donde].split("\r\n")[i + 1].split("###").length == 2) {
									resultado += " "
											+ newcontenido.split("@@@")[donde].split("\r\n")[i + 1].split("###")[0]
													.trim();
								}
								if (newcontenido.split("@@@")[donde].split("\r\n")[i + 1].split("###")[1]
										.contains("Edo.")) {
									resultado += ", "
											+ newcontenido.split("@@@")[donde].split("\r\n")[i + 1].split("###")[1]
													.split("Edo.")[1].trim();
								}
							}
							modelo.setCteDireccion(resultado);
							break;
						}
					}

					if (newcontenido.split("@@@")[donde].split("\r\n")[i].contains("###C.P.")) {
						switch (newcontenido.split("@@@")[donde].split("\r\n")[i].split("###").length) {
						case 2:
							if (newcontenido.split("@@@")[donde].split("\r\n")[i].split("###")[1].contains("C.P.")) {
								modelo.setCp(newcontenido.split("@@@")[donde].split("\r\n")[i].split("###")[1]
										.split("C.P.")[1].trim());
							}
							break;
						}
					}
				}
			}
			donde = 0;
			newcontenido = fn.filtroPorRango(contenido, 6);
			donde = fn.recorreContenido(newcontenido, "###RFC:###");
			if (donde > 0) {
				for (String dato : newcontenido.split("@@@")[donde].split("\r\n")) {
					if (dato.contains("###RFC:###") && dato.split("###").length == 3) {
						modelo.setRfc(dato.split("###")[2].trim());
					}
				}
			}

			donde = 0;
			if (fn.recorreContenido(contenido, "Inicio de Vigencia") > 0) {
				donde = fn.recorreContenido(contenido, "Inicio de Vigencia");
			} else if (fn.recorreContenido(contenido, "Prima semestral") > 0) {
				donde = fn.recorreContenido(contenido, "Prima semestral");
			}
			if (donde > 0) {
				for (String dato : contenido.split("@@@")[donde].split("\r\n")) {
					if (dato.contains("Inicio de Vigencia") && dato.split("###").length == 3) {
						if (dato.split("###")[0].contains("Vigencia")) {
							modelo.setVigenciaDe(
									fn.formatDate_MonthCadena(dato.split("###")[0].split("Vigencia")[1].trim()));
						}
					}
					if (dato.contains("Moneda")) {
						switch (dato.split("###").length) {
						case 1:
							modelo.setMoneda(fn.moneda(dato.split("neda")[1].trim()));
							break;
						}
					}
					if (dato.contains("Frecuencia de Pago")) {
						switch (dato.split("###").length) {
						case 3:
							if (dato.split("###")[0].contains("Primas")) {
								modelo.setFormaPago(
										fn.formaPago(dato.split("###")[0].split("Primas")[1].trim().toUpperCase()));
							}
							break;
						}
					}
				}
			}

			donde = 0;
			donde = fn.recorreContenido(contenido, "Prima Anual");
			if (donde > 0) {
				for (String dato : contenido.split("@@@")[donde].split("\r\n")) {
					if (dato.contains("Anual") && dato.split("###").length == 1) {

						modelo.setPrimaTotal(fn.castBigDecimal(fn.cleanString(dato.split("Anual")[1].trim())));
					}
				}
			}
			modelo.setPlan(contenido.split("CARATULA DE POLIZA")[1].split("\n")[2].replace("@@@", ""));
			modelo.setPlazo(Integer.parseInt(fn.cleanString(contenido.split("Anual")[1].split("\n")[1].split("###")[3]
					.replace("Años", "").replace("Año", "").trim())));
			modelo.setPlazoPago(
					Integer.parseInt(fn.cleanString(contenido.split("Anual")[1].split("\n")[1].split("###")[3]
							.replace("Años", "").replace("Año", "").trim())));
			modelo.setAportacion(1);
			modelo.setFechaEmision(fn.formatDate_MonthCadena(
					contenido.split("Emisión")[1].split("Moneda")[0].replace("\r\n", "").trim()));

			if (modelo.getFechaEmision().split("-").length == 3 && modelo.getFechaEmision().length() == 10
					&& modelo.getVigenciaDe().length() == 10 && modelo.getVigenciaA().length() == 0) {
				resultado = modelo.getFechaEmision().split("-")[0] + "-" + modelo.getVigenciaDe().split("-")[1] + "-"
						+ modelo.getVigenciaDe().split("-")[2];
				modelo.setVigenciaDe(modelo.getFechaEmision().split("-")[0] + "-" + modelo.getVigenciaDe().split("-")[1]
						+ "-" + modelo.getVigenciaDe().split("-")[2]);
				modelo.setVigenciaA(Integer.parseInt(modelo.getFechaEmision().split("-")[0]) + 1 + "-"
						+ modelo.getVigenciaDe().split("-")[1] + "-" + modelo.getVigenciaDe().split("-")[2]);
			}

			if (contenido.split("@@@")[2].split("\r\n").length == 1) {
				if (contenido.split("@@@")[2].split("\r\n")[0].split("###").length == 1) {
					modelo.setPlan(contenido.split("@@@")[2].trim());
				}
			}

			if (fn.recorreContenido(contenido, "###Centro de utilidad") > 0) {
				donde = fn.recorreContenido(contenido, "###Centro de utilidad");
				for (String dato : contenido.split("@@@")[donde].split("\r\n")) {
					if (dato.contains("Agente:") && dato.split("\r\n").length == 1) {
						if (dato.split("###").length == 3) {
							if (dato.split("###")[0].contains("Agente:")) {
								modelo.setAgente(dato.split("###")[0].split("Agente:")[1].trim());
							}
						}
					}
				}
			}

			donde = 0;
			if (30 < contenido.split("@@@").length) {
				newcontenido = fn.filtroPorRango(contenido, 30);
				donde = fn.recorreContenido(contenido, "Agente:");
				if (donde > 0) {
					if (newcontenido.split("@@@")[donde].split("\r\n").length == 1) {
						if (newcontenido.split("@@@")[donde].split("\r\n")[0].split("###").length == 3) {
							if (newcontenido.split("@@@")[donde].split("\r\n")[0].split("###")[0].contains("Agente:")) {
								modelo.setCveAgente(newcontenido.split("@@@")[donde].split("\r\n")[0].split("###")[0]
										.split("Agente:")[1].trim());
							}
						}
					}
				}
			}

			if (fn.recorreContenido(contenido, "VIDA###") > 0) {
				donde = fn.recorreContenido(contenido, "VIDA###");
				if (contenido.split("@@@")[donde].split("\r\n").length == 1) {
					if (contenido.split("@@@")[donde].split("###").length == 3) {
						modelo.setSa(contenido.split("@@@")[donde].split("###")[1].trim());
					}
				}
			}
			List<EstructuraAseguradosModel> asegurados = new ArrayList<>();
			EstructuraAseguradosModel asegurado = new EstructuraAseguradosModel();


			donde = 0;
			newcontenido = fn.filtroPorRango(contenido, 8);
			donde = fn.recorreContenido(newcontenido, "Datos del Asegurado");

			if (donde > 0) {
				if (newcontenido.split("@@@")[donde].contains("Nombre")
						&& newcontenido.split("@@@")[donde].split("\r\n").length == 2) {
					if (newcontenido.split("@@@")[donde].split("\r\n")[1].contains("Nombre")
							&& newcontenido.split("@@@")[donde].split("\r\n")[1].split("###").length == 2) {
						if (newcontenido.split("@@@")[donde].split("\r\n")[1].split("###")[0].contains("Nombre")) {
							asegurado.setNombre(
									newcontenido.split("@@@")[donde].split("\r\n")[1].split("###")[0].split("Nombre")[1]
											.trim());
						}
					}
				}
			}

			inicio = contenido.indexOf("Fecha de Nacimiento");
			fin = contenido.indexOf("Datos de la Póliza");

			if (inicio > 0 && fin > 0 && inicio < fin) {
				newcontenido = contenido.substring(inicio, fin);
				for (int i = 0; i < newcontenido.split("\n").length; i++) {
					if (newcontenido.split("\n")[i].contains("Fecha de Nacimiento")
							&& newcontenido.split("\n")[i].contains("Edad")) {
						asegurado.setNacimiento(fn.formatDate_MonthCadena(
								newcontenido.split("\n")[i].split("Nacimiento")[1].split("Edad")[0].replace("###", "")
										.replace("de", "").replaceAll("  +", "-").trim()));
					}
				}
			}
			asegurados.add(asegurado);
			modelo.setAsegurados(asegurados);
			List<EstructuraCoberturasModel> coberturas = new ArrayList<>();

			donde = 0;
			if (15 < contenido.split("@@@").length) {
				newcontenido = fn.filtroPorRango(contenido, 15);
				donde = fn.recorreContenido(newcontenido, "Coberturas Amparadas") + 1;
				if (donde > 1) {
					if (newcontenido.split("@@@")[donde].split("\r\n").length == 1) {
						if (newcontenido.split("@@@")[donde].split("\r\n")[0].split("###").length == 6) {

							EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();
							cobertura.setNombre(newcontenido.split("@@@")[donde].split("\r\n")[0].split("###")[0]
									.replace("-", "").trim());
							cobertura.setSa(newcontenido.split("@@@")[donde].split("\r\n")[0].split("###")[1].trim());
							coberturas.add(cobertura);
						}
					} else if (newcontenido.split("@@@")[donde].split("\r\n").length > 1) {
						for (String dato : newcontenido.split("@@@")[donde].split("\r\n")) {
							EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();
							if (dato.split("###").length == 6) {

								cobertura.setNombre(dato.split("###")[0].replace("-", "").trim());
								cobertura.setSa(dato.split("###")[1].trim());
								coberturas.add(cobertura);

							} else if (dato.split("###").length == 5) {

								if (dato.split("###")[0].split(" ").length >= 2) {
									if (dato.split("###")[0].split(dato.split("###")[0]
											.split(" ")[dato.split("###")[0].split(" ").length - 1]).length == 1) {
										cobertura.setNombre(dato.split("###")[0].split(dato.split("###")[0]
												.split(" ")[dato.split("###")[0].split(" ").length - 1])[0].replace("-",
														""));
									}
									cobertura.setSa(dato.split("###")[0]
											.split(" ")[dato.split("###")[0].split(" ").length - 1]);
								}
								coberturas.add(cobertura);
							}
						}
					}
				}
				modelo.setCoberturas(coberturas);
			}

			List<EstructuraBeneficiariosModel> beneficiarios = new ArrayList<>();
			inicio = inicontenido.indexOf("Beneficiarios");
			fin = inicontenido.indexOf("ADVERTENCIA");
	

			if (inicio > 0 && fin > 0 && inicio < fin) {
				newcontenido = inicontenido.split("Beneficiarios")[1].split("ADVERTENCIA")[0].replace("@@@", "")
						.replace(")", "###").replace("(", "###").trim();
				

				for (String bene : newcontenido.split("\n")) {
					EstructuraBeneficiariosModel beneficiario = new EstructuraBeneficiariosModel();
					int n = bene.split("###").length;
					if (bene.contains("Nombres")) {
						if (n == 3) {

							beneficiario.setNombre(bene.split("###")[0].replace("Nombres:", "").trim());
							beneficiario.setParentesco(fn.parentesco(bene.split("###")[1]));
							beneficiario.setTipo(11);
							beneficiario.setPorcentaje(
									fn.castDouble(bene.split("###")[2].trim().replace("%", "")).intValue());
							beneficiarios.add(beneficiario);
						}
					}
					if (bene.contains("FALLECIMIENTO")) {
						String fall = newcontenido.split("FALLECIMIENTO")[1].split("\n")[1];
						if (fall.split("###").length == 3) {
							beneficiario.setNombre(fall.split("###")[0].trim());
							beneficiario.setPorcentaje(
									fn.castDouble(fall.split("###")[2].trim().replace("%", "")).intValue());
							beneficiario.setParentesco(fn.parentesco(fall.split("###")[1].trim()));
							beneficiario.setTipo(12);
							beneficiarios.add(beneficiario);
						}
					}

				}

				modelo.setBeneficiarios(beneficiarios);

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

			if (modelo.getCoberturas().size() > 0) {
				String tipo_vida = modelo.getCoberturas().get(0).getNombre().toLowerCase();
				if (tipo_vida.contains("dotal")) {
					modelo.setTipovida(3);
				} else if (tipo_vida.contains("temporal")) {
					modelo.setTipovida(2);
				}
			}
			
			return modelo;
		} catch (Exception ex) {
			modelo.setError(
					AxaVidaModel.this.getClass().getTypeName() + " | " + ex.getMessage() + " | " + ex.getCause());
			return modelo;
		}

	}

}
