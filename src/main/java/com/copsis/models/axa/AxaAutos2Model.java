package com.copsis.models.axa;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraJsonModel;

public class AxaAutos2Model {
	// Clases
	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
	
	private String contenido = "";
	private String newcontenido = "";
	
	private int inicio = 0;
	private int fin = 0;
	
	public AxaAutos2Model(String contenido) {
		this.contenido = contenido;		
	}
	public EstructuraJsonModel procesar() {
		contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());
		try {
			// tipo
			modelo.setTipo(1);
			// cia
			modelo.setCia(20);
			
//			System.out.println(contenido);
			
			inicio = contenido.indexOf("Datos del asegurado");
			fin =contenido.indexOf("Datos del vehículo");
//			System.out.println(inicio +"----> " + fin);
			if(inicio > 0 && fin > 0 && inicio < fin) {
				newcontenido = contenido.substring(inicio,fin).replace("@@@", "").replace("\r", "");
				for (int i = 0; i < newcontenido.split("\n").length; i++) {
//				System.out.println("---> " + newcontenido.split("\n")[i]);
				if(newcontenido.split("\n")[i].contains("Name:")) {
					modelo.setPoliza(newcontenido.split("\n")[i].split("###")[newcontenido.split("\n")[i].split("###").length -1]);
					modelo.setCteNombre((newcontenido.split("\n")[i].split(modelo.getPoliza())[0].split("Name:")[1].replace("###", " ")).trim());
				}
				}
			}
			
			return modelo;
		} catch (Exception ex) {
			modelo.setError(
					AxaAutos2Model.this.getClass().getTypeName() + " | " + ex.getMessage() + " | " + ex.getCause());
			return modelo;
		}
		
	}

   
}
