package com.copsis.models.mapfre;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraJsonModel;

public class MapfreSaludRojoModel {
	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
	private String contenido = "";

	public MapfreSaludRojoModel(String contenido) {
		this.contenido = contenido;
	}
	public EstructuraJsonModel procesar() {
		contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());
		contenido = contenido.replace("COBERTURAS###Y###SERVICIOS", "COBERTURAS Y SERVICIOS")
				.replace("FECHA###DE###EMISIÓN", "FECHA DE EMISIÓN")
				.replace("LAS###12:00###HRS.###DEL:", "")
				.replace("PLAN###CONTRATADO:", "PLAN CONTRATADO:")
				.replace("ZONA###DE###CONTRATACIÓN", "ZONA DE CONTRATACIÓN")
				.replace("FORMA###DE###PAGO:", "FORMA DE PAGO:")
				.replace("PRIMA###NETA", "PRIMA NETA")
				.replace("PRIMA###TOTAL:", "PRIMA TOTAL:");
		String newcontenido = "";
		int inicio = 0;
		int fin = 0;
		try {
			modelo.setTipo(32323);
			modelo.setCia(22);

			inicio = contenido.indexOf("PROTECCION MEDICA");
			fin = contenido.indexOf("COBERTURAS Y SERVICIOS");
			

			if (inicio > -1 & fin > -1 & inicio < fin) {
				newcontenido = contenido.substring(inicio, fin).replace("@@@", "").replace("\r", "");
				for (int i = 0; i < newcontenido.split("\n").length; i++) {
					if(newcontenido.split("\n")[i].contains("PÓLIZA-ENDOSO")) {
						if(newcontenido.split("\n")[i].split("PÓLIZA-ENDOSO")[1].replace("###", "").trim().contains("-")) {
							 modelo.setPoliza(newcontenido.split("\n")[i].split("PÓLIZA-ENDOSO")[1].replace("###", "").trim().split("-")[0]);							 
						}				
					}
					if(newcontenido.split("\n")[i].contains("FECHA DE EMISIÓN")) {
						modelo.setFechaEmision(fn.formatDate_MonthCadena(newcontenido.split("\n")[i].split("FECHA DE EMISIÓN")[1].replace("###", "").trim()));
						if(newcontenido.split("\n")[i+1].contains("AGENTE:")) {
							modelo.setAgente(newcontenido.split("\n")[i+1].split("AGENTE:")[1].replace("###", "").trim());
						}
						if(newcontenido.split("\n")[i+2].contains("CLAVE DE AGENTE:")) {
							modelo.setCveAgente(newcontenido.split("\n")[i+2].split("AGENTE:")[1].replace("###", "").trim());
						}
					}
					if(newcontenido.split("\n")[i].contains("DESDE") && newcontenido.split("\n")[i].contains("TIPO")) {
						modelo.setVigenciaDe(fn.formatDate_MonthCadena(newcontenido.split("\n")[i].split("DESDE")[1].split("TIPO")[0].replace("###", "")));
					}
					if(newcontenido.split("\n")[i].contains("HASTA") && newcontenido.split("\n")[i].contains("CLIENTE")) {
						modelo.setVigenciaA(fn.formatDate_MonthCadena(newcontenido.split("\n")[i].split("HASTA:")[1].split("CLIENTE")[0].replace("###", "")));
					}
					if(newcontenido.split("\n")[i].contains("PLAN CONTRATADO:")){
						modelo.setPlan((newcontenido.split("\n")[i].split("PLAN CONTRATADO:")[1] +" "+ newcontenido.split("\n")[i+1]).replace("###", "").trim());
					}
					if(newcontenido.split("\n")[i].contains("CONTRATANTE:") &&  newcontenido.split("\n")[i].contains("ZONA DE CONTRATACIÓN:")){
						modelo.setCteNombre(newcontenido.split("\n")[i].split("CONTRATANTE:")[1].split("ZONA")[0].replace("###", "").trim());
					}
					if(newcontenido.split("\n")[i].contains("DOMICILIO:")){
						modelo.setCteDireccion((newcontenido.split("\n")[i].split("DOMICILIO:")[1] +" "+newcontenido.split("\n")[i+1]).replace("###", "").trim());
					}
					if(newcontenido.split("\n")[i].contains("R.F.C:")){
						modelo.setRfc(newcontenido.split("\n")[i].split("R.F.C:")[1].replace("###", "").trim());
					}
					if(newcontenido.split("\n")[i].contains("C.P:")){
						modelo.setCp(newcontenido.split("\n")[i].split("C.P:")[1].replace("###", "").trim());
					}
							
					
				}
			}
			
//			System.out.println(contenido);
			inicio = contenido.indexOf("FORMA DE PAGO:");
			fin = contenido.indexOf("PRÁCTICA DE DEPORTE");
			System.out.println(inicio +"---> "+ fin);
			if (inicio > -1 & fin > -1 & inicio < fin) {
				newcontenido = contenido.substring(inicio, fin).replace("@@@", "").replace("\r", "");
				modelo.setFormaPago(fn.formaPagoSring(newcontenido));
				for (int i = 0; i < newcontenido.split("\n").length; i++) {						
					System.out.println( newcontenido.split("\n")[i]);
				}
			}
			
			return modelo;
		} catch (Exception e) {
			return modelo;
		}
	}

}
