package com.copsis.models.zurich;

import java.util.Iterator;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraJsonModel;

public class ZurichDiversosModel {
	// Clases
	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
	// Varaibles
	private String contenido = "";

	

	
	public ZurichDiversosModel(String contenido) {
		this.contenido = contenido;
	}
	
	public EstructuraJsonModel procesar() {
		int inicio =0;
		int fin =0;
		StringBuilder newcont = new StringBuilder();
		contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());
		
		try {
			inicio = contenido.indexOf("");
			fin = contenido.indexOf("");
			System.out.println(contenido);
			if(inicio  > -1 && fin > -1 && inicio < fin) {
				newcont.append(contenido.substring(inicio,fin).replace("@@@", "").replace("\r", "").trim());
				for (int i = 0; i < newcont.toString().split("\n").length; i++) {
					
					
				}
			}
			
			
			return modelo;
		} catch (Exception e) {
			 return modelo;
		}
	}
	

}
