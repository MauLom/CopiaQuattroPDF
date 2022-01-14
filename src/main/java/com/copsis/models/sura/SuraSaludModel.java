package com.copsis.models.sura;

import java.util.ArrayList;
import java.util.List;

import com.copsis.constants.ConstantsValue;
import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraAseguradosModel;
import com.copsis.models.EstructuraCoberturasModel;
import com.copsis.models.EstructuraJsonModel;

public class SuraSaludModel {
	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
	private String contenido;
	private String newcontenido;
	private int inicio;
	private int fin;

	public SuraSaludModel(String contenidox) {
		this.contenido = contenidox;
	}

	public EstructuraJsonModel procesar() {
		try {
			contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());
			modelo.setTipo(3);
			modelo.setCia(88);

			inicio = contenido.indexOf("GASTOS MÉDICOS MAYORES");
			fin = contenido.indexOf(ConstantsValue.COBERTURAS_CONTRATADAS2);
			if (inicio > -1 && fin > -1 && inicio < fin) {
				newcontenido = contenido.substring(inicio, fin).replace("@@@", "").replace("\r", "")
						.replace("12hrs. de", "").replace("|", "-");
				for (int i = 0; i < newcontenido.split("\n").length; i++) {

					if (newcontenido.split("\n")[i].contains("Póliza no.")) {
						modelo.setPoliza(newcontenido.split("\n")[i + 1].split("###")[3]);
					}
					if (newcontenido.split("\n")[i].contains("Desde las")) {
						modelo.setVigenciaDe(
								fn.formatDateMonthCadena(newcontenido.split("\n")[i + 1].replace(" ", "").trim()));
						if (modelo.getVigenciaDe().length() > 0) {
							modelo.setFechaEmision(modelo.getVigenciaDe());
						}
					}
					if (newcontenido.split("\n")[i].contains("Hasta las")) {
						modelo.setVigenciaA(
								fn.formatDateMonthCadena(newcontenido.split("\n")[i + 1].replace(" ", "").trim()));
					}
					if (newcontenido.split("\n")[i].contains("Datos del contratante")) {
	

						int sp = newcontenido.split("\n")[i + 1].replace(" ", "###").split("###").length;
						String rx = newcontenido.split("\n")[i + 1].replace(" ", "###");
						if (sp == 8) {
							modelo.setCteNombre(
									rx.split("###")[0] + " " + rx.split("###")[1] + " " + rx.split("###")[3]);
							modelo.setRfc(rx.split("###")[4]);
						}
						
						String direccion = newcontenido.split("\n")[i + 2] + " "
								+ newcontenido.split("\n")[i + 3].split("C.P.")[0];
						modelo.setCteDireccion(direccion);
					}

				}
			}

			inicio = contenido.indexOf("Prima Neta###Tasa de");
			fin = contenido.indexOf("Pag. 1");

			if (inicio > -1 && fin > -1 && inicio < fin) {
				newcontenido = contenido.substring(inicio, fin).replace("@@@", "").replace("\r", "");
				for (int i = 0; i < newcontenido.split("\n").length; i++) {
					if (newcontenido.split("\n")[i].contains("Prima Neta")) {
						int sp = newcontenido.split("\n")[i + 1].split("###").length;
						if(sp == 5) {
							modelo.setPrimaneta(fn
									.castBigDecimal(fn.preparaPrimas(newcontenido.split("\n")[i + 1].split("###")[0])));
							modelo.setIva(fn
									.castBigDecimal(fn.preparaPrimas(newcontenido.split("\n")[i + 1].split("###")[3])));
							modelo.setPrimaTotal(fn
									.castBigDecimal(fn.preparaPrimas(newcontenido.split("\n")[i + 1].split("###")[4])));
						}
					}

				}
			}

			inicio = contenido.indexOf("Asegurado Titular");
			fin = contenido.indexOf(ConstantsValue.COBERTURAS_CONTRATADAS2);

			if (inicio > -1 && fin > -1 && inicio < fin) {
				List<EstructuraAseguradosModel> asegurados = new ArrayList<>();
				newcontenido = contenido.substring(inicio, fin).replace("@@@", "").replace("\r", "").replace(",",
						"###");
				for (int i = 0; i < newcontenido.split("\n").length; i++) {
					EstructuraAseguradosModel asegurado = new EstructuraAseguradosModel();
					if (newcontenido.split("\n")[i].split("-").length > 3) {
						asegurado.setNombre(newcontenido.split("\n")[i].split("###")[0]);
						asegurado.setNacimiento(
								fn.formatDateMonthCadena(newcontenido.split("\n")[i].split("###")[1].trim()));
						asegurado.setSexo(
								fn.sexo(newcontenido.split("\n")[i].split("###")[2].toUpperCase().trim()) ? 1 : 0);
						asegurado.setParentesco(
								fn.parentesco(newcontenido.split("\n")[i].split("###")[3].toUpperCase().trim()));
						asegurado.setAntiguedad(
								fn.formatDateMonthCadena(newcontenido.split("\n")[i].split("###")[4].trim()));
						asegurados.add(asegurado);
					}
				}
				modelo.setAsegurados(asegurados);
			}

			inicio = contenido.indexOf(ConstantsValue.COBERTURAS_CONTRATADAS2);
			fin = contenido.indexOf("Prima Neta###Tasa de");
			if (inicio > -1 && fin > -1 && inicio < fin) {
				List<EstructuraCoberturasModel> coberturas = new ArrayList<>();
				newcontenido = contenido.substring(inicio, fin).replace("@@@", "").replace("\r", "");
				for (int i = 0; i < newcontenido.split("\n").length; i++) {
					EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();
					int sp = newcontenido.split("\n")[i].split("###").length;
					if (!newcontenido.split("\n")[i].contains("Deducible") && sp == 4) {
						cobertura.setNombre(newcontenido.split("\n")[i].split("###")[0]);
						cobertura.setSa(newcontenido.split("\n")[i].split("###")[1]);
						cobertura.setCoaseguro(newcontenido.split("\n")[i].split("###")[2]);
						coberturas.add(cobertura);
					}
				}
			}

			return modelo;
		} catch (Exception e) {
			return modelo;
		}
	}

}
