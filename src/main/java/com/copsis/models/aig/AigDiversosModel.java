package com.copsis.models.aig;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraJsonModel;

public class AigDiversosModel {
	
	// Clases
		private DataToolsModel fn = new DataToolsModel();
		private EstructuraJsonModel modelo = new EstructuraJsonModel();
		// Varaibles
		private String contenido = "";
		private String newcontenido = "";
		private String newresultado = "";

		private int inicio = 0;
		private int fin = 0;

		
		public AigDiversosModel(String contenido) {
			this.contenido = contenido;
		}
		
		public EstructuraJsonModel procesar() {
			contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());
			System.out.println("------> " + contenido);
			try {
				//tipo
				modelo.setTipo(7);
				//cia
				modelo.setCia(3);
				//Datos del Contractante
				inicio = contenido.indexOf("PAQUETE");
				fin = contenido.indexOf("MONEDA");
				System.out.println(inicio +"-->" +fin);
				if (inicio > 0 && fin > 0 && inicio < fin) {
					newcontenido = contenido.substring(inicio, fin).replace("@@@", "").replace("\r", "").replace("  ", "###");
					for (int i = 0; i < newcontenido.split("\n").length; i++) {
						System.out.println("======> " + newcontenido.split("\n")[i]);
						if(newcontenido.split("\n")[i].contains("NÚMERO DE PÓLIZA")) {
							System.out.println("==°_°====> " + newcontenido.split("\n")[i+1].split(" ")[3]);
							modelo.setPoliza(newcontenido.split("\n")[i+1].split(" ")[3]);
						}
						if(newcontenido.split("\n")[i].contains("NOMBRE:") && newcontenido.split("\n")[i].contains("R.F.C:")) {
							modelo.setCteNombre(newcontenido.split("\n")[i].split("NOMBRE:")[1].split("R.F.C:")[0].trim());
						}
						if(newcontenido.split("\n")[i].contains("DIRECCIÓN:")) {
						  newresultado =  newcontenido.split("\n")[i].split("DIRECCIÓN:")[1] + " " +newcontenido.split("\n")[i+1] ;
						  modelo.setCteDireccion(newresultado.trim());
						}
					}
				}
				
				
				return modelo;
			} catch (Exception ex) {
				modelo.setError(AigDiversosModel.this.getClass().getTypeName() + " - catch:" + ex.getMessage() + " | "
						+ ex.getCause());
				return modelo;
			}
			
		}

}
