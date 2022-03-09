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
				newcontenido = contenido.substring(inicio, fin).replace("\r", "").replace("@@@", "")
						.trim();

				for (int i = 0; i < newcontenido.split("\n").length; i++) {
					System.out.println( newcontenido.split("\n")[i]);
					if (newcontenido.split("\n")[i].contains("NÚMERO")  && newcontenido.split("\n")[i].contains("PÓLIZA")) {
						modelo.setPoliza(newcontenido.split("\n")[i + 2].split("###")[3]);
					}
					if (newcontenido.split("\n")[i].contains("PÓLIZA")  && newcontenido.split("\n")[i].contains("ENDOSO") && newcontenido.split("\n")[i].contains("RENOVACIÓN")) {
						modelo.setPoliza(newcontenido.split("\n")[i + 1].split("###")[3].trim());
					}
					
					if (newcontenido.split("\n")[i].contains("Contratante") && newcontenido.split("\n")[i].contains("Contratante") ) {
						modelo.setCteNombre(newcontenido.split("\n")[i].split("Contratante")[1].split("RFC")[0].replace("###", "").trim());
						modelo.setRfc(newcontenido.split("\n")[i].split("RFC")[1].replace("###", "").trim());
					}
					if (newcontenido.split("\n")[i].contains("Contratante") && modelo.getCteNombre().length() == 0) {
						modelo.setCteNombre(newcontenido.split("\n")[i].split("Contratante")[1].replace("###", " "));
					}
					
					
					
					
					if (newcontenido.split("\n")[i].contains("Domicilio") && newcontenido.split("\n")[i].contains("Domicilio###de###Cobro")) {
						modelo.setCteDireccion((newcontenido.split("\n")[i].split("Domicilio###de###Cobro")[1] + " "
								+ newcontenido.split("\n")[i + 1]).replace("###", " "));
					}
					if (newcontenido.split("\n")[i].contains("Domicilio") && newcontenido.split("\n")[i].contains("Fecha de Nacimiento") && modelo.getCteDireccion().length() == 0 ) {
						modelo.setCteDireccion(newcontenido.split("\n")[i].split("Domicilio")[1].split("Fecha de Nacimiento")[0].replace("###", "").trim()
								+"  " + newcontenido.split("\n")[i+1]	
								);
					}

					if(newcontenido.split("\n")[i].contains("Agente") && newcontenido.split("\n")[i].contains("-")){
						modelo.setCveAgente(newcontenido.split("\n")[i].split("Agente")[1].split("-")[0].replace("###", "").trim());
						modelo.setAgente(newcontenido.split("\n")[i].split("Agente")[1].split("-")[1].replace("###", "").trim());
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
