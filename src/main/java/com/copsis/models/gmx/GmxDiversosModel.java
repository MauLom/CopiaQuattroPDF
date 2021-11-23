package com.copsis.models.gmx;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraJsonModel;

public class GmxDiversosModel {

	// Clases
	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
	// Variables
	private String contenido = "";

	public GmxDiversosModel(String contenido) {
		this.contenido = contenido;
	}

	public EstructuraJsonModel procesar() {
		String newcontenido = "";
		int inicio = 0;
		int fin = 0;
		contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());
		try {
			// tipo
			modelo.setTipo(7);

			// cia
			modelo.setCia(17);

			// Datos del contratante

			inicio = contenido.indexOf("POLIZA NUEVA");
			fin = contenido.indexOf("Descripción de Bienes");
			if (inicio > 0 && fin > 0 && inicio < fin) {
				newcontenido = contenido.substring(inicio, fin).replace("\r", "").replace("@@@", "").replace(" ", "###")
						.trim();

				for (int i = 0; i < newcontenido.split("\n").length; i++) {

					if (newcontenido.split("\n")[i].contains("NÚMERO")
							&& newcontenido.split("\n")[i].contains("PÓLIZA")) {
						modelo.setPoliza(newcontenido.split("\n")[i + 2].split("###")[3]);
					}
					if (newcontenido.split("\n")[i].contains("Contratante")) {
						modelo.setCteNombre(newcontenido.split("\n")[i].split("Contratante")[1].replace("###", " "));
					}
					if (newcontenido.split("\n")[i].contains("Domicilio")) {
						modelo.setCteDireccion((newcontenido.split("\n")[i].split("Domicilio###de###Cobro")[1] + " "
								+ newcontenido.split("\n")[i + 1]).replace("###", " "));
					}

					if (newcontenido.split("\n")[i].contains("RFC:")) {
						modelo.setRfc(newcontenido.split("\n")[i].split("RFC:")[1]);
					}
				}

			}

			return modelo;
		} catch (Exception ex) {
			modelo.setError(GmxDiversosModel.this.getClass().getTypeName() + " - catch:" + ex.getMessage() + " | "
					+ ex.getCause());
			return modelo;
		}
	}
}
