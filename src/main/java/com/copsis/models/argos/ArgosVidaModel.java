package com.copsis.models.argos;

import java.util.ArrayList;
import java.util.List;

import com.copsis.constants.ConstantsValue;
import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraCoberturasModel;
import com.copsis.models.EstructuraJsonModel;

public class ArgosVidaModel {

	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
	private String contenido = "";
	

	public ArgosVidaModel(String contenido) {
		this.contenido = contenido;
	}

	public EstructuraJsonModel procesar() {
		String newcontenido = "";
		int inicio = 0;
		int fin = 0;

		contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());
		try {
			// tipo
			modelo.setTipo(5);
			// cia
			modelo.setCia(33);

			inicio = contenido.indexOf("Póliza No.");
			fin = contenido.indexOf(ConstantsValue.COBERTURAS);

			if (inicio > 0 && fin > 0 && inicio < fin) {
				newcontenido = contenido.substring(inicio, fin).replace("\r", "").replace("@@@", "").trim();
				for (int i = 0; i < newcontenido.split("\n").length; i++) {
					if (newcontenido.split("\n")[i].contains("Póliza No.")) {
						modelo.setPoliza(newcontenido.split("\n")[i + 1].split("###")[1]);
					}
					if (newcontenido.split("\n")[i].contains("Contratante")
							&& newcontenido.split("\n")[i].contains("domicilio")) {
						modelo.setCteNombre(newcontenido.split("\n")[i + 1]);
						String x = newcontenido.split("\n")[i + 2];
						if (newcontenido.split("\n")[i + 3].contains("Fecha de Nacimiento")) {
							x += " " + newcontenido.split("\n")[i + 3].split("Fecha")[0];
						}
						if (newcontenido.split("\n")[i + 4].contains("Día")) {
							x += " " + newcontenido.split("\n")[i + 4].split("Día")[0];
						}
						if (newcontenido.split("\n")[i + 5].contains("C.P.")) {
							x += " " + newcontenido.split("\n")[i + 5].split("C.P.")[1].split("###")[0];
						}
						modelo.setCteDireccion(x.replace("###", ""));
					}
					if (newcontenido.split("\n")[i].contains("C.P.")) {
						modelo.setCp(newcontenido.split("\n")[i].split("C.P.")[1].trim().split(" ")[0]);
					}
					if (newcontenido.split("\n")[i].contains("ANUAL")) {
						modelo.setFormaPago(1);
					}

					if (newcontenido.split("\n")[i].contains("Prima")
							&& newcontenido.split("\n")[i + 1].contains("Día")) {

						modelo.setPrimaneta(fn.castBigDecimal(
								fn.cleanString(newcontenido.split("\n")[i + 2].split("###")[2].trim())));
						modelo.setPrimaTotal(modelo.getPrimaneta());
					}
					if (newcontenido.split("\n")[i].contains("Día###Mes###Año")) {

						String x = fn.cleanString(
								newcontenido.split("\n")[i + 1].split(modelo.getPrimaneta().toString())[1]);
						int sp = fn
								.cleanString(newcontenido.split("\n")[i + 1].split(modelo.getPrimaneta().toString())[1])
								.split("###").length;

						if (sp == 4) {
							modelo.setVigenciaDe(x.split("###")[3] + "-" + x.split("###")[2] + "-" + x.split("###")[1]);
						}

					}
				}
			}
			modelo.setMoneda(1);

			inicio = contenido.indexOf(ConstantsValue.COBERTURAS);
			fin = contenido.indexOf("Nombre completo de");

			if (inicio > 0 && fin > 0 && inicio < fin) {
				List<EstructuraCoberturasModel> coberturas = new ArrayList<>();
				newcontenido = contenido.substring(inicio, fin).replace("\r", "").replace("@@@", "").trim();
				for (int i = 0; i < newcontenido.split("\n").length; i++) {
					EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();
					if (!newcontenido.split("\n")[i].contains(ConstantsValue.COBERTURAS)) {

						int sp = newcontenido.split("\n")[i].split("###").length;
						if (sp == 4) {
							cobertura.setNombre(newcontenido.split("\n")[i].split("###")[0]);
							cobertura.setSa(newcontenido.split("\n")[i].split("###")[1]);
							coberturas.add(cobertura);

						}

					}
				}
				modelo.setCoberturas(coberturas);
			}

			inicio = contenido.indexOf("Nombre completo de los beneficiarios");
			fin = contenido.indexOf("Los siguientes endosos");

			if (inicio > 0 && fin > 0 && inicio < fin) {
				newcontenido = contenido.substring(inicio, fin).replace("\r", "").replace("@@@", "").trim();
			}

			return modelo;
		} catch (Exception ex) {
			modelo.setError(ArgosVidaModel.this.getClass().getTypeName() + " - catch:" + ex.getMessage() + " | "
					+ ex.getCause());
			return modelo;
		}

	}

}
