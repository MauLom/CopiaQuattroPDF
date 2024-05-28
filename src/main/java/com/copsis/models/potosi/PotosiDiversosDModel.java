package com.copsis.models.potosi;

import java.util.ArrayList;
import java.util.List;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraCoberturasModel;
import com.copsis.models.EstructuraJsonModel;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class PotosiDiversosDModel {

	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();

	public EstructuraJsonModel procesar(String contenido) {
		int inicio = 0;
		int fin = 0;
		StringBuilder newcontenido = new StringBuilder();
		contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());

		try {
			modelo.setTipo(7);
			modelo.setCia(37);

			inicio = contenido.indexOf("Número de Póliza:");
			fin = contenido.indexOf("Información del Asegurado Titular");
			if (inicio == -1) {
				inicio = contenido.indexOf("POLIZA DE SEGUROS DE RESPONSABILIDAD CIVIL GENERAL");
				fin = contenido.indexOf("Seguros El Potosí, S. A.");
			}

			newcontenido.append(fn.extracted(inicio, fin, contenido));
			String[] lines = newcontenido.toString().split("\n");

			for (int i = 0; i < lines.length; i++) {
				if (lines[i].contains("Póliza")) {
					Pattern patron = Pattern.compile("RCGE\\s\\d+");
					Matcher matcher = patron.matcher(lines[i]);
					if (matcher.find()) {
						String poliza = matcher.group();
						modelo.setPoliza(poliza);

						if (i + 6 < lines.length && lines[i + 6].split("###").length > 0) {
							String[] line6Parts = lines[i + 6].split("###");
							String[] line5Parts = lines[i + 5].split("###");
							if (line5Parts.length > 0) {
								modelo.setCteDireccion(line6Parts[line5Parts.length - 1]);
								modelo.setCteNombre(line5Parts[line5Parts.length - 1]);
								modelo.setRfc(lines[i + 7].split("###")[line5Parts.length - 1]);
								modelo.setVigenciaDe(lines[i].split("###")[lines[i + 2].split("###").length - 1]);
								modelo.setVigenciaA(lines[i].split("###")[lines[i + 8].split("###").length - 1]);

								modelo.setIdCliente(lines[i + 2].split("###")[lines[i + 2].split("###").length - 1]);

								modelo.setMoneda(
										fn.moneda(lines[i + 1].split("###")[lines[i + 3].split("###").length - 1]));

								String lineaCP = lines[i + 6];
								Pattern patronCP = Pattern.compile("\\b\\d{5}\\b");
								Matcher matcherCP = patronCP.matcher(lineaCP);
								if (matcherCP.find()) {
									String cp = matcherCP.group();
									modelo.setCp(cp);
								}
							}
						}

					}
				}
				if (i + 2 < lines.length && lines[i + 2].contains("Plan de Pago")) {
					modelo.setFormaPago(fn.formaPagoSring(lines[i + 4]));
				}
				if (lines[i].contains("Vigencia")) {
					List<String> vigencias = fn.obtenVigePoliza(lines[i + 1]);
					if (vigencias.size() >= 2) {
						modelo.setVigenciaDe(fn.formatDateMonthCadena(vigencias.get(0)));
						modelo.setVigenciaA(fn.formatDateMonthCadena(vigencias.get(1)));
					}
				}
			}

			inicio = contenido.indexOf("CÉDULA R.C. AGENTES");
			fin = contenido.indexOf("Seguros El Potosí, S. A.");

			newcontenido = new StringBuilder();
			newcontenido.append(fn.extracted(inicio, fin, contenido));
			lines = newcontenido.toString().split("\n");

			for (int i = 0; i < lines.length; i++) {
				if (lines[i].contains("AGENTE") && lines[i].contains("Tipo de agente:")) {
					String[] agenteParts = lines[i].split("AGENTE:");
					if (agenteParts.length > 2) {
						modelo.setAgente(agenteParts[2].split("FECHA")[0].replace("###", "").trim());
					}
				}

				if (i + 1 < lines.length && lines[i].contains("Contratante") && lines[i + 1].contains("NOMBRE")) {
					String[] nombreParts = lines[i + 1].split("AZÓN SOCIAL:");
					if (nombreParts.length > 1) {
						modelo.setCteNombre(nombreParts[1].trim());
					}
				}

				if (lines[i].contains("RFC:") && lines[i].contains("TELÉFONO:")) {
					String[] rfcParts = lines[i].split("RFC:");
					if (rfcParts.length > 1) {
						modelo.setRfc(rfcParts[1].split("TELÉFONO")[0].replace("###", "").trim());
					}
				}

				if (lines[i].contains("DOMICILIO FISCAL:") && lines[i].contains("CÓDIGO POSTAL")) {
					String[] direccionParts = lines[i].split("DOMICILIO FISCAL:");
					if (direccionParts.length > 1) {
						modelo.setCteDireccion(direccionParts[1].split("CÓDIGO POSTAL")[0].replace("###", "").trim());
					}
				}

				if (i + 1 < lines.length && lines[i].contains("Domicilio del Contratante")) {
					modelo.setCteDireccion(lines[i + 1]);
				}

				if (lines[i].contains("MONEDA")) {
					modelo.setMoneda(fn.buscaMonedaEnTexto(lines[i]));
				}

				if (lines[i].contains("PLAN DE PAGO:")) {
					modelo.setFormaPago(fn.formaPagoSring(lines[i]));
				}

				if (lines[i].contains("CLAVE DEL AGENTE:") && lines[i].contains("TIPO DE OPERACIÓN:")) {
					String[] agenteClaveParts = lines[i].split("AGENTE:");
					if (agenteClaveParts.length > 1) {
						modelo.setCveAgente(agenteClaveParts[1].split("TIPO")[0].replace("###", "").trim());
					}
				}
			}

			inicio = contenido.indexOf("COBERTURAS AMPARADA");
			fin = contenido.indexOf("Información de Prima");
			newcontenido = new StringBuilder();
			newcontenido.append(fn.extracted(inicio, fin, contenido));
			lines = newcontenido.toString().split("\n");
			List<EstructuraCoberturasModel> coberturas = new ArrayList<>();

			for (int i = 0; i < lines.length; i++) {
				if (!lines[i].contains("COBERTURAS")) {
					String[] parts = lines[i].split("###");
					if (parts.length > 2) {
						EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();
						cobertura.setNombre(parts[0].trim());
						cobertura.setSa(parts[1].trim());
						cobertura.setDeducible(parts[2].trim());
						coberturas.add(cobertura);
					}
				}
			}

			modelo.setCoberturas(coberturas);

			inicio = contenido.indexOf("Información de Prima");
			fin = contenido.indexOf("Seguros ###el ###Potosí");
			fin = fin == -1 ? contenido.indexOf("Seguros ###El ###Potosí,") : fin;

			newcontenido = new StringBuilder();
			newcontenido.append(fn.extracted(inicio, fin, contenido));
			lines = newcontenido.toString().split("\n");

			for (int i = 0; i < lines.length; i++) {
				if (lines[i].contains("PRIMA NETA")) {
					List<String> valores = fn.obtenerListNumeros(lines[i + 1]);
					if (valores.size() > 7) {
						modelo.setPrimaneta(fn.castBigDecimal(fn.castDouble(valores.get(0))));
						modelo.setDerecho(fn.castBigDecimal(fn.castDouble(valores.get(1))));
						modelo.setRecargo(fn.castBigDecimal(fn.castDouble(valores.get(2))));
						modelo.setIva(fn.castBigDecimal(fn.castDouble(valores.get(4))));
						modelo.setPrimaTotal(fn.castBigDecimal(fn.castDouble(valores.get(7))));
					}
				}
			}

			return modelo;
		} catch (Exception ex) {
			modelo.setError(PotosiDiversosDModel.this.getClass().getTypeName() + " | " + ex.getMessage() + " | "
					+ ex.getCause());
			return modelo;
		}
	}
}
