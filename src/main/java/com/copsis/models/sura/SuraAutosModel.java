package com.copsis.models.sura;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraJsonModel;

public class SuraAutosModel {

	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
	private String contenido;
	private String newcontenido;
	private int  inicio;
	private int fin;

	public SuraAutosModel(String contenidox) {
		this.contenido = contenidox;
	}
	public EstructuraJsonModel procesar() {
		try {
			contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());
			modelo.setTipo(1);
			modelo.setCia(23);
//			System.out.println(contenido);
			
			inicio = contenido.indexOf("Seguro de Automóviles");
			fin = contenido.indexOf("Coberturas contratadas");
			
			if(inicio > -1 && fin > -1 &&  inicio < fin ) {
				newcontenido = contenido.substring(inicio ,fin).replace("@@@", "").replace("\r", "");
				for (int i = 0; i < newcontenido.split("\n").length; i++) {
					System.out.println(newcontenido.split("\n")[i]);
					if(newcontenido.split("\n")[i].contains("Póliza no.")) {
						if(newcontenido.split("\n")[i+2].contains("C.P.")) {
							modelo.setPolizaGuion(newcontenido.split("\n")[i+2].split("###")[3]);
					     modelo.setPoliza(newcontenido.split("\n")[i+2].split("###")[3].replace("-", "").replace(" ", "").trim());
						}
						if(newcontenido.split("\n")[i+1].contains("C.P.")) {
							modelo.setCp(newcontenido.split("\n")[i+2].split("C.P.")[1].split("###")[0].trim());
						}
					}
					
					
				}
				
			}
			
			
			
			return modelo;
		} catch (Exception e) {
			return modelo;
	
			// TODO: handle exception
		}
	}
	
}
