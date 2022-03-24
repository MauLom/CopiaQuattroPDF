package com.copsis.models.hdi;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraJsonModel;

public class HdiDiversosModel {
	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
	private String contenido = "";

	public HdiDiversosModel(String contenido) {
		this.contenido = contenido;
	}

	public EstructuraJsonModel procesar() {
		int inicio = 0;
		int fin = 0;
		StringBuilder newcontenido = new StringBuilder();
		contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());

		try {
			
			modelo.setTipo(1);
			modelo.setCia(14);
			
			System.out.println(contenido);
			
			inicio = contenido.indexOf("SEGURO DE DAÑOS");
			fin = contenido.indexOf("El asegurado es:");			
			newcontenido.append( fn.extracted(inicio, fin, contenido).toString());

			for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
				System.out.println("===> " +newcontenido.toString().split("\n")[i]);
				if(newcontenido.toString().split("\n")[i].contains("Póliza") && newcontenido.toString().split("\n")[i].contains("Inciso") ) {
					modelo.setPoliza(newcontenido.toString().split("\n")[i].split("Póliza:")[1].split("Inciso")[0].trim());
				}
				if(newcontenido.toString().split("\n")[i].contains("emisión:")){
					modelo.setFechaEmision(fn.formatDateMonthCadena((newcontenido.toString().split("\n")[i].split("emisión:")[1])));					
				}
				if(newcontenido.toString().split("\n")[i].contains("Vigencia:") && newcontenido.toString().split("\n")[i].contains("Desde") && newcontenido.toString().split("\n")[i].contains("Hasta")){
					
				}
			}

			return modelo;
		} catch (Exception ex) {
			modelo.setError(HdiDiversosModel.this.getClass().getTypeName() + " | " + ex.getMessage() + " | " + ex.getCause());
			return modelo;
		}

	}
}
