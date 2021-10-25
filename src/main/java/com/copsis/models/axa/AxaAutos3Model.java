package com.copsis.models.axa;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraJsonModel;

public class AxaAutos3Model {
	// Clases
		private DataToolsModel fn = new DataToolsModel();
		private EstructuraJsonModel modelo = new EstructuraJsonModel();
		
		private String contenido;
	
		

		
		public AxaAutos3Model(String contenido) {
			this.contenido = contenido;		
		}
		public EstructuraJsonModel procesar() {
			 int inicio;
			 int fin ;
			 String newcontenido;
			contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());
			try {
				// tipo
				modelo.setTipo(1);
				// cia
				modelo.setCia(20);
				
				inicio = contenido.indexOf("CARÁTULA DE PÓLIZA");
				fin = contenido.indexOf("Datos del Vehículo");
				System.out.println(contenido);
				
				if(inicio > 0 && fin > 0 && inicio < fin) {
					newcontenido = contenido.substring(inicio,fin).replace("@@@", "").replace("\r", "");
					for (int i = 0; i < newcontenido.split("\n").length; i++) {	
						System.out.println("===> "+ newcontenido.split("\n")[i]);
					}
				}				
				return modelo;
			} catch (Exception e) {
				return modelo;
			}
		}

}
