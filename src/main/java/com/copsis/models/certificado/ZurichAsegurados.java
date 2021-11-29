package com.copsis.models.certificado;

import java.util.ArrayList;
import java.util.List;

import com.copsis.constants.ConstantsValue;
import com.copsis.exceptions.GeneralServiceException;
import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraAseguradosModel;
import com.copsis.models.EstructuraJsonModel;

public class ZurichAsegurados {
	// Clases
	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
	// variables
	private String contenido = "";

	public ZurichAsegurados(String contenido) {
		this.contenido = contenido;
	}

	public EstructuraJsonModel procesar() {
		String subgrupo = "";
		String categoria = "";
		int inicio = 0;
		int fin = 0;
		StringBuilder newcontenido = new StringBuilder();

		contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());
		try {
			inicio = contenido.indexOf(ConstantsValue.CONTRATANTE2);
			fin = contenido.indexOf("Certificado");

			if (inicio > 0 && fin > 0 && inicio < fin) {
				newcontenido.append(contenido.substring(inicio, fin).replace("@@@", "").replace("\r", "").trim());

				for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
					if (newcontenido.toString().split("\n")[i].contains(ConstantsValue.CONTRATANTE2)
							&& newcontenido.toString().split("\n")[i].contains("Subgrupo")
							&& newcontenido.toString().split("\n")[i].contains("Categoría")) {
						if (newcontenido.toString().split("\n")[i + 1].length() > 5) {
							modelo.setContratante(newcontenido.toString().split("\n")[i + 1].split("###")[0].trim());
						} else {
							modelo.setContratante(newcontenido.toString().split("\n")[i + 2].split("###")[0].trim());
						}
					}
				}
			}

			newcontenido = new StringBuilder();
			for (int i = 0; i < contenido.split(ConstantsValue.CONTRATANTE2).length; i++) {

				if (contenido.split(ConstantsValue.CONTRATANTE2)[i].contains("Total de Asegurados")) {
					newcontenido.append(contenido.split(ConstantsValue.CONTRATANTE2)[i].replace("@@@", "")
							.replace("\r", "").split("Total de Asegurados")[0].replace("TITU", "TITULAR")
									.replace("CONY", "CONYUGUE").replace("Alta-Baja", ""));
				}
			}

			List<EstructuraAseguradosModel> asegurados = new ArrayList<>();
			for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {

				if (newcontenido.toString().split("\n")[i].contains("Subgrupo")) {
					subgrupo = newcontenido.toString().split("\n")[i + 1].split("###")[1];
					categoria = newcontenido.toString().split("\n")[i + 1].split("###")[2];
				}
				EstructuraAseguradosModel asegurado = new EstructuraAseguradosModel();
				if (newcontenido.toString().split("\n")[i].split("-").length > 3) {
					asegurado.setPrimaneta(fn.castBigDecimal(fn.preparaPrimas(newcontenido.toString().split("\n")[i]
							.split("###")[newcontenido.toString().split("\n")[i].split("###").length - 1]
									.replace("Pesos", "").trim())));
					asegurado.setSa(newcontenido.toString().split("\n")[i]
							.split("###")[newcontenido.toString().split("\n")[i].split("###").length - 2]);
					asegurado.setCobertura(newcontenido.toString().split("\n")[i]
							.split("###")[newcontenido.toString().split("\n")[i].split("###").length - 3]);
					asegurado.setFechaAlta(fn.formatDateMonthCadena(newcontenido.toString().split("\n")[i]
							.split("###")[newcontenido.toString().split("\n")[i].split("###").length - 4]));
					asegurado.setSexo(fn
							.sexo(newcontenido.toString().split("\n")[i]
									.split("###")[newcontenido.toString().split("\n")[i].split("###").length - 5])
							.booleanValue() ? 1 : 0);
					asegurado.setEdad(Integer.parseInt(newcontenido.toString().split("\n")[i]
							.split("###")[newcontenido.toString().split("\n")[i].split("###").length - 6]));
					asegurado.setAntiguedad(fn.formatDateMonthCadena(newcontenido.toString().split("\n")[i]
							.split("###")[newcontenido.toString().split("\n")[i].split("###").length - 7]));

					if (newcontenido.toString().split("\n")[i + 1].split("-").length == 1) {
						asegurado.setNombre((newcontenido.toString().split("\n")[i]
								.split("###")[newcontenido.toString().split("\n")[i].split("###").length - 8] + " "
								+ newcontenido.toString().split("\n")[i + 1]).replace("+", "").replace("  ", " ")
										.trim());
					} else {
						asegurado.setNombre(newcontenido.toString().split("\n")[i]
								.split("###")[newcontenido.toString().split("\n")[i].split("###").length - 8]
										.replace("+", "").replace("  ", " ").trim());
					}

					asegurado.setParentesco(fn.parentesco(newcontenido.toString().split("\n")[i]
							.split("###")[newcontenido.toString().split("\n")[i].split("###").length - 9].trim()));
					asegurado.setCertificado(newcontenido.toString().split("\n")[i]
							.split("###")[newcontenido.toString().split("\n")[i].split("###").length - 10].trim());

					asegurado.setSubgrupo(subgrupo.trim());
					asegurado.setCategoria(categoria.trim());
					asegurados.add(asegurado);
				}

			}
			modelo.setAsegurados(asegurados);
			return modelo;
		} catch (Exception ex) {
			throw new GeneralServiceException("00001",
					"Ocurrio un error en el servicio ImpresionInter: " + ex.getMessage());
		}
	}
}
