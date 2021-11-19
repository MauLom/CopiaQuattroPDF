package com.copsis.models.aig;

import java.util.ArrayList;
import java.util.List;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraCoberturasModel;
import com.copsis.models.EstructuraJsonModel;

public class AigDiversosModel {

	// Clases
	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
	// Varaibles
	private String contenido = "";
	private static final String MONEDA = "MONEDA";
	private static final String SECCIONESCOBERTURAS = "SECCIONES Y COBERTURAS";

	

	public AigDiversosModel(String contenido) {
		this.contenido = contenido;
	}

	public EstructuraJsonModel procesar() {
		int inicio = 0;
		int fin = 0;
		String newcontenido = "";
		String newresultado = "";
		StringBuilder newcoberturas = new StringBuilder();
		
		contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());
		contenido = contenido.replace("MENSUAL", "###MENSUAL###").replace("EDIFICIO", "EDIFICIO###")
				.replace("CONTENIDOS", "CONTENIDOS###").replace("REMOCION DE ESCOMBROS", "REMOCION DE ESCOMBROS###")
				.replace("GASTOS EXTRAORDINARIOS", "GASTOS EXTRAORDINARIOS###")
				.replace("RESPONSABILIDAD CIVIL", "RESPONSABILIDAD CIVIL###").replace("CRISTALES", "CRISTALES###")
				.replace("EQUIPO ELECTRONICO Y-O ELECTRODOMESTICO", "EQUIPO ELECTRONICO Y-O ELECTRODOMESTICO###")
				.replace("ROBO CON VIOLENCIA Y-O ASALTO", "ROBO CON VIOLENCIA Y-O ASALTO###")
				.replace("SERVICIOS ADICIONALES", "SERVICIOS ADICIONALES###");
		try {
			// tipo
			modelo.setTipo(7);
			// cia
			modelo.setCia(3);
			// Datos del Contractante
			inicio = contenido.indexOf("PAQUETE");
			fin = contenido.indexOf(MONEDA);
			if (inicio > 0 && fin > 0 && inicio < fin) {
				newcontenido = contenido.substring(inicio, fin).replace("@@@", "").replace("\r", "").replace("  ",
						"###");
				for (int i = 0; i < newcontenido.split("\n").length; i++) {
					if (newcontenido.split("\n")[i].contains("NÚMERO DE PÓLIZA")) {
						modelo.setPoliza(newcontenido.split("\n")[i + 1].split(" ")[3]);
					}
					if (newcontenido.split("\n")[i].contains("NOMBRE:")
							&& newcontenido.split("\n")[i].contains("R.F.C:")) {
						modelo.setCteNombre(newcontenido.split("\n")[i].split("NOMBRE:")[1].split("R.F.C:")[0].trim());
					}
					if (newcontenido.split("\n")[i].contains("DIRECCIÓN:")) {
						newresultado = newcontenido.split("\n")[i].split("DIRECCIÓN:")[1] + " "
								+ newcontenido.split("\n")[i + 1];
						modelo.setCteDireccion(newresultado.trim());
					}
					if (newcontenido.split("\n")[i].contains("CÓDIGO POSTAL:")) {
						modelo.setCp(newcontenido.split("\n")[i].split("CÓDIGO POSTAL:")[1].trim());
					}
					if (newcontenido.split("\n")[i].contains("PAGO")) {
						modelo.setFormaPago(fn.formaPago(newcontenido.split("\n")[i + 1].split("###")[1]));
						newresultado = newcontenido.split("\n")[i + 2].replace("DÍAS", "").trim().replace(" ", "/");
						String a = newresultado.split("/")[0] + "-" + newresultado.split("/")[1] + "-"
								+ newresultado.split("/")[2];
						String b = newresultado.split("/")[3] + "-" + newresultado.split("/")[4] + "-"
								+ newresultado.split("/")[5];
						modelo.setVigenciaDe(fn.formatDateMonthCadena(a));
						modelo.setVigenciaA(fn.formatDateMonthCadena(b));
						modelo.setFechaEmision(modelo.getVigenciaDe());
					}
				}
			}

			inicio = contenido.indexOf(MONEDA);
			fin = contenido.indexOf("FECHA DE EXPEDICIÓN:");
			if (inicio > 0 && fin > 0 && inicio < fin) {
				newcontenido = contenido.substring(inicio, fin).replace("@@@", "").replace("\r", "").replace(" ",
						"###");
				for (int i = 0; i < newcontenido.split("\n").length; i++) {
					if (newcontenido.split("\n")[i].contains(MONEDA)
							&& newcontenido.split("\n")[i].contains("PRIMA")) {
						modelo.setMoneda(fn.moneda(newcontenido.split("\n")[i + 1].split("###")[0].trim()));
						modelo.setPrimaneta(fn.castBigDecimal(fn.castDouble(newcontenido.split("\n")[i + 1]
								.split("###")[newcontenido.split("\n")[i + 1].split("###").length - 1])));
					}
					if (newcontenido.split("\n")[i].contains("FRACCIONADO")) {
						modelo.setRecargo(fn.castBigDecimal(fn.castDouble(newcontenido.split("\n")[i]
								.split("###")[newcontenido.split("\n")[i].split("###").length - 1])));
					}
					if (newcontenido.split("\n")[i].contains("EXPEDICIÓN")) {
						modelo.setDerecho(fn.castBigDecimal(fn.castDouble(newcontenido.split("\n")[i]
								.split("###")[newcontenido.split("\n")[i].split("###").length - 1])));
					}
					if (newcontenido.split("\n")[i].contains("IVA")) {
						modelo.setIva(fn.castBigDecimal(fn.castDouble(newcontenido.split("\n")[i]
								.split("###")[newcontenido.split("\n")[i].split("###").length - 1])));
					}
					if (newcontenido.split("\n")[i].contains("TOTAL")
							&& newcontenido.split("\n")[i].contains("PAGAR")) {
						modelo.setIva(fn.castBigDecimal(fn.castDouble(newcontenido.split("\n")[i]
								.split("###")[newcontenido.split("\n")[i].split("###").length - 1])));
					}

				}
			}
			boolean cbo = false;
			for (int i = 0; i < contenido.split(SECCIONESCOBERTURAS).length; i++) {
				if (contenido.split(SECCIONESCOBERTURAS)[i].contains(MONEDA)) {
					newcoberturas.append(contenido.split(SECCIONESCOBERTURAS)[i].split(MONEDA)[0]);
				} else if (contenido.split(SECCIONESCOBERTURAS)[i].contains("En cumplimiento") && !cbo) {
					newcoberturas.append(contenido.split(SECCIONESCOBERTURAS)[i].split("En cumplimiento")[0]);
					cbo = true;
				}
			}

			if (newcoberturas.length() > 0) {
				List<EstructuraCoberturasModel> coberturas = new ArrayList<>();
				String auxStr = newcoberturas.toString();
				newcoberturas = new StringBuilder();
				newcoberturas.append(auxStr.replace("@@@", "").replace("\r", "").trim());
				for (int i = 0; i < newcoberturas.toString().split("\n").length; i++) {
					EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();
					if (newcoberturas.toString().split("\n")[i].split("###").length > 1) {
						cobertura.setNombre(newcoberturas.toString().split("\n")[i].split("###")[0].trim());
						cobertura.setSa(newcoberturas.toString().split("\n")[i].split("###")[1].trim());
						coberturas.add(cobertura);
					}
				}
				modelo.setCoberturas(coberturas);
			}

			return modelo;
		} catch (Exception ex) {
			modelo.setError(AigDiversosModel.this.getClass().getTypeName() + " - catch:" + ex.getMessage() + " | "
					+ ex.getCause());
			return modelo;
		}

	}

}
