package com.copsis.models.primero;

import java.util.ArrayList;
import java.util.List;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraCoberturasModel;
import com.copsis.models.EstructuraJsonModel;

public class PrimeroAutosBModel {

	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
	private String contenido = "";

	public PrimeroAutosBModel(String contenido) {
		this.contenido = contenido;
	}

	public EstructuraJsonModel procesar() {
		String newcontenido = "";
		int inicio = 0;
		int fin = 0;
		
		contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());
		try {
			modelo.setCia(49);
			modelo.setTipo(1);
			modelo.setInciso(1);

			inicio = contenido.indexOf("PÓLIZA DE SEGURO");
			fin = contenido.indexOf("COBERTURAS");
			if (inicio > -1 && fin > -1 && inicio < fin) {
				newcontenido = contenido.substring(inicio, fin).replace("@@@", "").replace("\r", "");
				for (int i = 0; i < newcontenido.split("\n").length; i++) {
					if (newcontenido.split("\n")[i].contains("NO. DE PÓLIZA")) {
						modelo.setPoliza(newcontenido.split("\n")[i + 1]);
					}
					if (newcontenido.split("\n")[i].contains("ASEGURADO:")) {
						modelo.setCteNombre(newcontenido.split("\n")[i].split("ASEGURADO:")[1].replace("###", ""));
					}
					if (newcontenido.split("\n")[i].contains("DOMICILIO:")) {
						modelo.setCteDireccion(newcontenido.split("\n")[i].split("DOMICILIO:")[1].replace("###", ""));
					}
					if (newcontenido.split("\n")[i].contains("CP.")) {
						modelo.setCp(newcontenido.split("\n")[i].split("CP.")[1].split(",")[0].strip());
					}
					if (newcontenido.split("\n")[i].contains("RFC:")
							&& newcontenido.split("\n")[i].contains("TELÉFONO:")) {
						modelo.setRfc(
								newcontenido.split("\n")[i].split("RFC:")[1].split("TELÉFONO")[0].replace("###", ""));
					}

					if (newcontenido.split("\n")[i].contains("PRODUCTO")
							|| newcontenido.split("\n")[i].contains("MONEDA")
							|| newcontenido.split("\n")[i].contains("FECHA DEMONEDA")) {
						int sp = newcontenido.split("\n")[i + 1].split("###").length;
						switch (sp) {
						case 7:
							modelo.setPlan(newcontenido.split("\n")[i + 1].split("###")[0]);
							modelo.setMoneda(fn.moneda(newcontenido.split("\n")[i + 1].split("###")[2]));
							modelo.setFechaEmision(fn.formatDate_MonthCadena(
									fn.elimgatos(newcontenido.split("\n")[i + 1].split("EXPEDICIÓN")[1]).replace("###",
											"-")));
							break;
						}

					}

					if (newcontenido.split("\n")[i].contains("VIGENCIA")
							|| newcontenido.split("\n")[i].contains("FORMA DE PAGO")) {
						if (newcontenido.split("\n")[i + 1].split("-").length > 0) {
							modelo.setVigenciaDe(
									fn.formatDate_MonthCadena(newcontenido.split("\n")[i + 1].split("###")[1]));
							modelo.setVigenciaA(
									fn.formatDate_MonthCadena(newcontenido.split("\n")[i + 1].split("###")[3]));
							modelo.setFormaPago(fn.formaPago(newcontenido.split("\n")[i + 1].split("###")[4]));
						}
					}

					if (newcontenido.split("\n")[i].contains("CLAVE") || newcontenido.split("\n")[i].contains("MARCA")
							|| newcontenido.split("\n")[i].contains("PLACAS")) {

						modelo.setClave(newcontenido.split("\n")[i + 1].split("###")[0]);
						if (newcontenido.split("\n")[i + 1].split("###")[1].split("-").length > 0) {
							int sp = newcontenido.split("\n")[i + 1].split("###").length;

							switch (sp) {
							case 4:
								modelo.setMarca(newcontenido.split("\n")[i + 1].split("###")[1].split("-")[0]);
								modelo.setDescripcion(newcontenido.split("\n")[i + 1].split("###")[1]);
								modelo.setModelo(
										fn.castInteger(newcontenido.split("\n")[i + 1].split("###")[2].trim()));
								break;
							}

						}
					}

					if (newcontenido.split("\n")[i].contains("NO. DE SERIE")
							|| newcontenido.split("\n")[i].contains("NO. DE MOTOR")) {
						int sp = newcontenido.split("\n")[i + 1].split("###").length;
						switch (sp) {
						case 4:
							modelo.setMotor(newcontenido.split("\n")[i + 1].split("###")[0]);
							modelo.setSerie(newcontenido.split("\n")[i + 1].split("###")[1]);

							break;
						}

					}

				}
			}

			inicio = contenido.indexOf("PRIMA NETA");
			fin = contenido.indexOf("EN CASO DE SINIESTRO");
			if (inicio > -1 && fin > -1 && inicio < fin) {
				newcontenido = contenido.substring(inicio, fin).replace("@@@", "").replace("\r", "");
				for (int i = 0; i < newcontenido.split("\n").length; i++) {
					if (newcontenido.split("\n")[i].contains("PRIMA NETA")) {
						int sp = newcontenido.split("\n")[i + 1].split("###").length;

						switch (sp) {
						case 6:
							modelo.setPrimaneta(fn
									.castBigDecimal(fn.preparaPrimas(newcontenido.split("\n")[i + 1].split("###")[0])));
							modelo.setRecargo(fn
									.castBigDecimal(fn.preparaPrimas(newcontenido.split("\n")[i + 1].split("###")[1])));
							modelo.setDerecho(fn
									.castBigDecimal(fn.preparaPrimas(newcontenido.split("\n")[i + 1].split("###")[2])));
							modelo.setIva(fn
									.castBigDecimal(fn.preparaPrimas(newcontenido.split("\n")[i + 1].split("###")[4])));
							break;
						}
					}

				}
			}

			inicio = contenido.indexOf("AVISO DE PRIVACIDAD");
			fin = contenido.indexOf("FIRMA AUTORIZADA");
			if (inicio > -1 && fin > -1 && inicio < fin) {
				newcontenido = contenido.substring(inicio, fin).replace("@@@", "").replace("\r", "");
				for (int i = 0; i < newcontenido.split("\n").length; i++) {
					if (newcontenido.split("\n")[i].contains("CLAVES DE PROMOTOR")) {
						modelo.setCveAgente(newcontenido.split("\n")[i - 1].split("-")[1].split("###")[0].trim());
						modelo.setAgente(newcontenido.split("\n")[i - 1].split("###")[1]);
					}
				}

			}

			inicio = contenido.indexOf("COBERTURAS");
			fin = contenido.indexOf("PRIMA NETA");
			if (inicio > -1 && fin > -1 && inicio < fin) {
				List<EstructuraCoberturasModel> coberturas = new ArrayList<>();
				newcontenido = contenido.substring(inicio, fin).replace("@@@", "").replace("\r", "");
				for (int i = 0; i < newcontenido.split("\n").length; i++) {
					EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();
					if (newcontenido.split("\n")[i].contains("COBERTURAS")
							|| newcontenido.split("\n")[i].contains("RESPONSABILIDAD")) {

					} else {
						int sp = newcontenido.split("\n")[i].split("###").length;
						switch (sp) {
						case 3:
							cobertura.setNombre(newcontenido.split("\n")[i].split("###")[0]);
							cobertura.setSa(newcontenido.split("\n")[i].split("###")[1]);
							coberturas.add(cobertura);
							break;
						case 4:
							cobertura.setNombre(newcontenido.split("\n")[i].split("###")[0]);
							cobertura.setSa(newcontenido.split("\n")[i].split("###")[1]);
							cobertura.setDeducible(newcontenido.split("\n")[i].split("###")[2]);
							coberturas.add(cobertura);
							break;
						}

					}

				}
				modelo.setCoberturas(coberturas);
			}

			return modelo;
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
			modelo.setError(ex.getMessage());
			return modelo;
		}

	}

}
