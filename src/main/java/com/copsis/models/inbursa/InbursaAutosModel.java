package com.copsis.models.inbursa;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.copsis.constants.ConstantsValue;
import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraCoberturasModel;
import com.copsis.models.EstructuraJsonModel;

public class InbursaAutosModel {
	// Clases
	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
	// Varaibles
	private String contenido = "";

	private String recibosText = "";
	private static final String POLIZA_REGEX = "(PÓLIZA \\s*(\\w{5} \\w{8}))";

	public InbursaAutosModel(String contenido, String recibos) {
		this.contenido = contenido;
		this.recibosText = recibos;
	}

	public EstructuraJsonModel procesar() {
		
		StringBuilder resultado = new StringBuilder();

		String newcontenido = "";
		int inicio = 0;
		int fin = 0;

		contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());
		contenido = contenido.replace("IVA", ConstantsValue.IVA2).replace("ACTUAL", "RENOVACION");
		try {
			// tipo
			modelo.setTipo(1);

			// cia
			modelo.setCia(38);

			// Datos del Contratante
			inicio = contenido.indexOf("PÓLIZA DE SEGUROS");
			fin = contenido.indexOf(ConstantsValue.COBERTURAS_CONTRATADAS);
			if (inicio > 0 && fin > 0 && inicio < fin) {
				newcontenido = contenido.substring(inicio, fin).replace("\r", "").replace("@", "")
						.replace("las 12:00 horas", "");
				
				obtenerPolizaRegex();
				
				for (int i = 0; i < newcontenido.split("\n").length; i++) {

					if (newcontenido.split("\n")[i].contains("DATOS DEL CONTRATANTE")) {
						if (newcontenido.split("\n")[i + 1].contains("NOMBRE:")) {
							modelo.setCteNombre(newcontenido.split("\n")[i + 2].split("###")[0]);
						} else {
							modelo.setCteNombre(newcontenido.split("\n")[i + 1].split("###")[0]);
						}
					}
					
					if(modelo.getPoliza().isEmpty()) {
						obtenerPolizaOtraUbicacion(newcontenido.split("\n"), i);
					}
					// proceso direccion
					if (newcontenido.split("\n")[i].contains("R.F.C.")) {
						resultado.append(newcontenido.split("\n")[i].split("R.F.C.")[0]);
					}
					if (newcontenido.split("\n")[i].contains(ConstantsValue.PRIMA_MAYUS)
							&& newcontenido.split("\n")[i].contains("NETA")
							&& newcontenido.split("\n")[i].contains("AGRUPACIÓN")) {
						// debe entra para no colocar nada
					} else if (newcontenido.split("\n")[i].contains(ConstantsValue.PRIMA_MAYUS)
							&& newcontenido.split("\n")[i].contains("NETA")) {
						resultado.append(" ").append(newcontenido.split("\n")[i].split(ConstantsValue.PRIMA_MAYUS)[0]).append(" ").append(newcontenido.split("\n")[i + 1]);
						modelo.setCteDireccion(resultado.toString().replace("###", "").trim());
					}

					if (newcontenido.split("\n")[i].contains("DIRECCIÓN:")) {
						String a = "";
						String b = "";
						String c = "";
						if (newcontenido.split("\n")[i + 2].trim().contains(".00")) {
							a = newcontenido.split("\n")[i + 1].split("SUMA")[0].trim();
						} else {
							a = newcontenido.split("\n")[i + 2].trim();
						}
						if (newcontenido.split("\n")[i + 3].trim().contains(ConstantsValue.RFC)) {
							b = newcontenido.split("\n")[i + 3].split(ConstantsValue.RFC)[0].trim();
						} else {
							b = newcontenido.split("\n")[i + 3].trim();
						}
						if (newcontenido.split("\n")[i + 6].trim().contains(".00")) {
							c = newcontenido.split("\n")[i + 4].split("C.P.")[0].trim();
						} else {
							c = newcontenido.split("\n")[i + 6].trim();
						}
						String x = a + " " + b + " " + c;
						modelo.setCteDireccion(x.replace("###", ""));
					}

					if (newcontenido.split("\n")[i].contains("C.P.")) {
						if (newcontenido.split("\n")[i].split("C.P.")[1].split("###").length > 0) {
							modelo.setCp(newcontenido.split("\n")[i].split("C.P.")[1].split("###")[0].trim());
						} else {
							modelo.setCp(newcontenido.split("\n")[i].split("C.P.")[1].trim());
						}
					}
					
					if (newcontenido.split("\n")[i].contains("MONEDA")) {
						modelo.setMoneda(fn.buscaMonedaEnTexto(newcontenido.split("\n")[i + 1]));						
					}
					// primas
					if (newcontenido.split("\n")[i].contains(ConstantsValue.PRIMA_NETA_MAYUS)
							&& newcontenido.split("\n")[i].contains("AGRUPACIÓN")) {
						modelo.setPrimaneta(
								fn.castBigDecimal(fn.castDouble(newcontenido.split("\n")[i + 1].split("###")[1])));
					} else if (newcontenido.split("\n")[i].contains(ConstantsValue.PRIMA_NETA_MAYUS)) {
						modelo.setPrimaneta(fn.castBigDecimal(fn.castDouble(
								fn.extraerNumeros(newcontenido.split("\n")[i].split(ConstantsValue.PRIMA_NETA_MAYUS)[1]
										.replace("###", "").substring(0, 13)))));
					}
					if (newcontenido.split("\n")[i].contains(ConstantsValue.FINANCIAMIENTO_MAYUS)
							&& newcontenido.split("\n")[i].contains(ConstantsValue.RFC)) {
						modelo.setDerecho(fn.castBigDecimal(
								fn.castDouble(newcontenido.split("\n")[i + 2].split("###")[1].replace("###", ""))));
					} else if (newcontenido.split("\n")[i].contains(ConstantsValue.FINANCIAMIENTO_MAYUS)) {
						modelo.setDerecho(fn.castBigDecimal(
								fn.castDouble(newcontenido.split("\n")[i].split(ConstantsValue.FINANCIAMIENTO_MAYUS)[1]
										.replace("###", ""))));
					}
					if (newcontenido.split("\n")[i].contains(ConstantsValue.EXPEDICION_MAYUS)
							&& newcontenido.split("\n")[i].contains("MONEDA:")) {
						modelo.setRecargo(fn.castBigDecimal(
								fn.castDouble(newcontenido.split("\n")[i + 1].split("###")[1].replace("###", ""))));
					} else if (newcontenido.split("\n")[i].contains(ConstantsValue.EXPEDICION_MAYUS)) {
						modelo.setRecargo(fn.castBigDecimal(
								fn.castDouble(newcontenido.split("\n")[i].split(ConstantsValue.EXPEDICION_MAYUS)[1]
										.replace("###", ""))));
					}
					if (newcontenido.split("\n")[i].contains(ConstantsValue.IVA2)
							&& newcontenido.split("\n")[i].contains("PAGO")) {
						modelo.setIva(fn.castBigDecimal(
								fn.castDouble(newcontenido.split("\n")[i + 1].split("###")[1].replace("###", ""))));
						modelo.setFormaPago(fn.formaPago(newcontenido.split("\n")[i + 1].split("###")[0].trim()));
					} else if (newcontenido.split("\n")[i].contains(ConstantsValue.IVA2)) {
						modelo.setIva(fn.castBigDecimal(fn.castDouble(
								newcontenido.split("\n")[i].split(ConstantsValue.IVA2)[1].replace("###", ""))));
					}
					if (newcontenido.split("\n")[i].contains(ConstantsValue.PRIMA_TOTAL_MAYUS)
							&& newcontenido.split("\n")[i].contains("DOCUMENTO")) {
						modelo.setPrimaTotal(fn.castBigDecimal(fn.castDouble(newcontenido.split("\n")[i + 1]
								.split("###")[newcontenido.split("\n")[i + 1].split("###").length - 1].replace("###",
										""))));
					} else if (newcontenido.split("\n")[i].contains(ConstantsValue.PRIMA_TOTAL_MAYUS)) {
						modelo.setPrimaTotal(fn.castBigDecimal(
								fn.castDouble(newcontenido.split("\n")[i].split(ConstantsValue.PRIMA_TOTAL_MAYUS)[1]
										.replace("###", ""))));
					}
					if (newcontenido.split("\n")[i].contains(ConstantsValue.DESDE)
							&& newcontenido.split("\n")[i].contains(ConstantsValue.HASTA2)
							&& newcontenido.split("\n")[i].contains("-")) {
						modelo.setVigenciaDe(
								fn.formatDateMonthCadena(newcontenido.split("\n")[i].split(ConstantsValue.DESDE)[1]
										.split(ConstantsValue.HASTA2)[0].replace("###", "").trim()));
						modelo.setVigenciaA(fn.formatDateMonthCadena(
								newcontenido.split("\n")[i].split(ConstantsValue.HASTA2)[1].split("RENOVACION")[0]
										.replace("###", "").trim()));
					} else if (newcontenido.split("\n")[i].contains(ConstantsValue.DESDE)
							&& newcontenido.split("\n")[i].contains(ConstantsValue.HASTA2)) {
						modelo.setVigenciaDe(fn.formatDateMonthCadena(newcontenido.split("\n")[i + 1].split("###")[0]));
						modelo.setVigenciaA(fn.formatDateMonthCadena(newcontenido.split("\n")[i + 1].split("###")[1]));
						modelo.setFormaPago(fn.formaPago(newcontenido.split("\n")[i + 1].split("###")[2].trim()));
					}
					if (modelo.getClave().isEmpty()) {
						obtenerClaveYDescripcionAuto(newcontenido.split("\n")[i]);
					}
	
					if (newcontenido.split("\n")[i].contains(ConstantsValue.MODELO_MAYUS)
							&& newcontenido.split("\n")[i].contains(ConstantsValue.PLACAS_MAYUS)) {
						modelo.setModelo(fn.castInteger(
								newcontenido.split("\n")[i].split(ConstantsValue.MODELO_MAYUS)[1].split("PLACAS")[0]
										.replace("###", "").trim()));
					} else if (newcontenido.split("\n")[i].contains(ConstantsValue.MODELO_MAYUS)) {
						modelo.setModelo(
								fn.castInteger(newcontenido.split("\n")[i].split(ConstantsValue.MODELO_MAYUS)[1]
										.replace("###", "").trim()));
					}
					if (newcontenido.split("\n")[i].contains(ConstantsValue.PLACAS_MAYUS)
							&& newcontenido.split("\n")[i].split("PLACAS")[1].length() > 2) {
						modelo.setPlacas(newcontenido.split("\n")[i].split("PLACAS:")[1].replace("###", "").trim());
					}
					if (newcontenido.split("\n")[i].contains(ConstantsValue.SERIE_MAYUS)
							&& newcontenido.split("\n")[i].contains("NÚMERO")) {
						modelo.setSerie(
								newcontenido.split("\n")[i].split(ConstantsValue.SERIE_MAYUS)[1].split("NÚMERO")[0]
										.replace("###", "").trim());
					} else if (newcontenido.split("\n")[i].contains(ConstantsValue.SERIE_MAYUS)) {
						modelo.setSerie(newcontenido.split("\n")[i].split(ConstantsValue.SERIE_MAYUS)[1]
								.replace("###", "").trim());
					}
					if (newcontenido.split("\n")[i].contains("MOTOR:")
							&& newcontenido.split("\n")[i].split("MOTOR")[1].length() > 2) {

						modelo.setMotor(newcontenido.split("\n")[i].split("MOTOR:")[1].replace("###", "").trim());
					}
					if (newcontenido.split("\n")[i].contains("PROPIETARIO:")) {
						modelo.setConductor(
								newcontenido.split("\n")[i].split("PROPIETARIO:")[1].replace("###", "").trim());
					}
					if (newcontenido.split("\n")[i].contains("PRODUCTO:")
							&& newcontenido.split("\n")[i].contains("PAGO")) {
						modelo.setPlan(newcontenido.split("\n")[i].split("PRODUCTO:")[1].split("FORMA")[0]
								.replace("###", "").trim());
					} else if (newcontenido.split("\n")[i].contains("PRODUCTO")) {
						modelo.setPlan(newcontenido.split("\n")[i + 1].split("###")[0].replace("###", "").trim());
					}
				}
			}

			/* Agente y Cve */

			inicio = contenido.indexOf("NOMBRE DEL AGENTE");

			if (inicio > 0) {
				newcontenido = contenido.split("NOMBRE DEL AGENTE")[0];
				if (newcontenido.length() > 200) {
					newcontenido = newcontenido.substring(newcontenido.length() - 200, newcontenido.length())
							.replace("@@@", "").replace("\r", "");
					for (int j = 0; j < newcontenido.split("\n").length; j++) {

						if (newcontenido.split("\n")[j].contains("CLAVE")) {
							modelo.setCveAgente(fn.extraerNumeros(newcontenido.split("\n")[j - 2]));
							if (modelo.getCveAgente().length() > 0) {
								String a = newcontenido.split("\n")[j - 1].replace(" ", "###").split("###")[0].trim();
								if (a.contains("@")) {
									a = "";
								}
								modelo.setAgente(
										(newcontenido.split("\n")[j - 2].split(modelo.getCveAgente())[1] + "" + a).trim());
							}
						}
					}
				}
			}

			inicio = contenido.indexOf(ConstantsValue.COBERTURAS_CONTRATADAS);
			fin = contenido.indexOf("En caso de Siniestro");
			if (fin == -1) {
				fin = contenido.indexOf("AVISO IMPORTANTE");
				if (fin == -1) {
					fin = contenido.indexOf("OPERAN COMO");
				}
			}

			if (inicio > 0 && fin > 0 && inicio < fin) {
				List<EstructuraCoberturasModel> coberturas = new ArrayList<>();
				newcontenido = contenido.substring(inicio, fin).replace("\r", "").replace("@", "")
						.replace("las 12:00 horas", "");
				for (int i = 0; i < newcontenido.split("\n").length; i++) {
					EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();
					if (!newcontenido.split("\n")[i].contains(ConstantsValue.COBERTURAS_CONTRATADAS)
							&& !newcontenido.split("\n")[i].contains("MÍNIMO")
							&& !newcontenido.split("\n")[i].contains("Cobertura")
							&& !newcontenido.split("\n")[i].contains("Deducible")
							&& !newcontenido.split("\n")[i].contains("**")
							&& !newcontenido.split("\n")[i].contains("UMA")
							&& !newcontenido.split("\n")[i].contains("OPERAN")) {

						int sp = newcontenido.split("\n")[i].split("###").length;
						if (newcontenido.split("\n")[i].split("###")[0].length() > 3) {
							cobertura.setNombre(newcontenido.split("\n")[i].split("###")[0]);
							if (sp > 1) {
								cobertura.setSa(newcontenido.split("\n")[i].split("###")[1]);
							}
							if (sp > 3) {
								cobertura.setDeducible(newcontenido.split("\n")[i].split("###")[2]);
							}
							coberturas.add(cobertura);

						}
					}
				}
				modelo.setCoberturas(coberturas);
			}

			return modelo;
		} catch (Exception ex) {
			modelo.setError(InbursaAutosModel.this.getClass().getTypeName() + " - catch:" + ex.getMessage() + " | "
					+ ex.getCause());
			return modelo;
		}
	}
	
	private void obtenerPolizaRegex() {
		Pattern pattern = Pattern.compile(POLIZA_REGEX);
		Matcher matcher = pattern.matcher(contenido);
		modelo.setPoliza(matcher.find() ? matcher.group(2) : "");
	}
	
	private void obtenerPolizaOtraUbicacion(String[] arrContenido,int i) {
		if (arrContenido[i].contains(ConstantsValue.POLIZA_MAYUS)
				&& arrContenido[i].contains("CIS")
				&& arrContenido[i].contains("ID CLIENTE")) {
			modelo.setPoliza(arrContenido[i - 1].split("###")[1]);
		} else if (arrContenido[i].contains(ConstantsValue.POLIZA_MAYUS)
				&& arrContenido[i].contains("CIS")
				&& arrContenido[i].contains("Cliente Inbursa")) {
			modelo.setPoliza(arrContenido[i - 1].split("###")[1]);
		} else if (arrContenido[i].contains(ConstantsValue.POLIZA_MAYUS)
				&& arrContenido[i].contains("FAMILIA")) {
			modelo.setPoliza(arrContenido[i + 1].split("###")[0]);
		}
	}
	
	private void obtenerClaveYDescripcionAuto(String texto) {
		if (texto.contains("CLAVE")) {
			if (texto.contains("ISIS:")) {
				modelo.setClave(texto.split("ISIS:")[1].split(" ")[0].replace("MARCA:", "").trim().replace("###", ""));
			} else if (texto.contains("VEHICULAR")) {
				modelo.setClave(
						texto.split("VEHICULAR:")[1].split(" ")[0].replace("MARCA:", "").trim().replace("###", ""));
			}

		}

		if (modelo.getClave().length() > 1) {
			modelo.setDescripcion(texto.split(modelo.getClave())[1].trim());
		}
		
	}
}
