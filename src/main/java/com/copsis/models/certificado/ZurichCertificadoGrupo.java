package com.copsis.models.certificado;

import java.util.ArrayList;
import java.util.List;

import com.copsis.constants.ConstantsValue;
import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraAseguradosModel;
import com.copsis.models.EstructuraJsonModel;

public class ZurichCertificadoGrupo {

	// Clases
	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
	// variables
	private String contenido = "";

	public ZurichCertificadoGrupo(String contenido) {
		this.contenido = contenido;
	}

	public EstructuraJsonModel procesar() {
		String valorsubgrupo = "";
		String categoria = "";
		int inicio = 0;
		StringBuilder newcontenido = new StringBuilder();

		contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());
		contenido = contenido.replace("Empleados ###y ###sus", ConstantsValue.EMPLEADOS_YSUS)
				.replace("NacionalSubgrupo:", "Subgrupo:");

		try {

			inicio = contenido.indexOf("Plan:");
			if (inicio > 0) {
				newcontenido.append(contenido.split("Plan:")[1].split("\n")[0].replace("@@@", "").replace("###", "")
						.replace("\r", "").trim());
				modelo.setPlan(newcontenido.toString());
			}

			for (int i = 0; i < contenido.split(ConstantsValue.CERTIFICADO_INDIVIDUAL).length; i++) {
				if (contenido.split(ConstantsValue.CERTIFICADO_INDIVIDUAL)[i].contains(ConstantsValue.EMPLEADOS_YSUS)) {
					newcontenido.append(contenido.split(ConstantsValue.CERTIFICADO_INDIVIDUAL)[i]
							.split(ConstantsValue.EMPLEADOS_YSUS)[0].replace("@@@", "").replace("ITULAR", "###TITULAR")
									.replace("ONYUGE", "###CONYUGE").replace("###T###", "###").replace("\r", ""));
				}
			}

			List<EstructuraAseguradosModel> asegurados = new ArrayList<>();

			for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {

				EstructuraAseguradosModel asegurado = new EstructuraAseguradosModel();

				if (newcontenido.toString().split("\n")[i].contains("Subgrupo")) {
					valorsubgrupo = newcontenido.toString().split("\n")[i].split("\n")[0].split("Subgrupo")[1];
				}
				if (newcontenido.toString().split("\n")[i].contains("Categoria")) {
					categoria = newcontenido.toString().split("\n")[i].split("\n")[0].split("Categoria")[1];
				}

				if (newcontenido.toString().split("\n")[i].split("-").length > 4) {

					asegurado.setSubgrupo(valorsubgrupo.trim());
					asegurado.setCategoria(categoria.trim());
					asegurado.setAntiguedad(fn.formatDateMonthCadena(newcontenido.toString().split("\n")[i]
							.split("###")[newcontenido.toString().split("\n")[i].split("###").length - 1]));
					asegurado.setFechaAlta(fn.formatDateMonthCadena(newcontenido.toString().split("\n")[i]
							.split("###")[newcontenido.toString().split("\n")[i].split("###").length - 2]));
					asegurado.setNacimiento(fn.formatDateMonthCadena(newcontenido.toString().split("\n")[i]
							.split("###")[newcontenido.toString().split("\n")[i].split("###").length - 3]));
					asegurado.setEdad(Integer.parseInt(newcontenido.toString().split("\n")[i]
							.split("###")[newcontenido.toString().split("\n")[i].split("###").length - 4].trim()));
					if (newcontenido.toString().split("\n")[i]
							.split("###")[newcontenido.toString().split("\n")[i].split("###").length - 5]
									.contains("TITULAR")) {
						asegurado.setParentesco(1);
						asegurado.setNombre(newcontenido.toString().split("\n")[i].split("TITULAR")[0]
								.replace("@@@", "").replace("###", " ").trim().replace("  ", ""));
					} else if (newcontenido.toString().split("\n")[i]
							.split("###")[newcontenido.toString().split("\n")[i].split("###").length - 5]
									.contains("CONYUGE")) {
						asegurado.setParentesco(2);
						asegurado.setNombre(newcontenido.toString().split("\n")[i].split("CONYUGE")[0]
								.replace("@@@", "").replace("###", " ").trim().replace("  ", ""));
					} else {
						asegurado.setParentesco(3);
						asegurado.setNombre(newcontenido.toString().split("\n")[i]
								.split(newcontenido.toString().split("\n")[i].split(
										"###")[newcontenido.toString().split("\n")[i].split("###").length - 4])[0]
												.replace("@@@", "").replace("###", " ").trim().replace("  ", ""));
					}

					asegurados.add(asegurado);
				}

			}
			modelo.setAsegurados(asegurados);

			return modelo;
		} catch (Exception ex) {

			modelo.setError(ZurichCertificadoGrupo.this.getClass().getTypeName() + " | " + ex.getMessage() + " | "
					+ ex.getCause());
			return modelo;
		}

	}

}
