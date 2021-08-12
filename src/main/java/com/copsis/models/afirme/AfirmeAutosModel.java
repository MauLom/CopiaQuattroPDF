package com.copsis.models.afirme;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraJsonModel;


public class AfirmeAutosModel {
	// Clases
	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
	// Varaibles
	private String contenido = "";
	private String newcontenido = "";
	private String newdireccion = "";
	private String resultado = "";
	private String resultadoCbo = "";
	private int inicio = 0;
	private int fin = 0;
	private int donde = 0;
	private Boolean primas = true;

	public AfirmeAutosModel(String contenido) {
		this.contenido = contenido;
	}

	public EstructuraJsonModel procesar() {
		try {
			contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());
			//tipo
            modelo.setTipo(1);
            //cia
            modelo.setCia(31);
            
           // System.out.println("----->" + contenido);
            //Datos Generales
            inicio = contenido.indexOf("PÓLIZA DE SEGURO");
            fin = contenido.indexOf("DESGLOSE DE COBERTURAS");
            if (inicio > 0 & fin > 0 & inicio < fin) {
				newcontenido = contenido.substring(inicio, fin).replace("\r", "").replace("@@@", "");
				for (int i = 0; i < newcontenido.split("\n").length; i++) {
					System.out.println("------> " + newcontenido.split("\n")[i]);
					if(newcontenido.split("\n")[i].contains("Poliza:") && newcontenido.split("\n")[i].contains("Inciso:")) {
						modelo.setPoliza(newcontenido.split("\n")[i].split("Poliza:")[1].split("Inciso:")[0].replace("-", "").replace("###", "").trim());
						modelo.setPolizaGuion(newcontenido.split("\n")[i].split("Poliza:")[1].split("Inciso:")[0].replace("###", "").trim());
					}
					if(newcontenido.split("\n")[i].contains("Desde:") && newcontenido.split("\n")[i].contains("Hasta:")) {						
						if(newcontenido.split("\n")[i+1].split("-").length > 2) {
							modelo.setVigenciaDe(fn.formatDate_MonthCadena(newcontenido.split("\n")[i+1].split("###")[1].replace("###", "").trim()));
							modelo.setFechaEmision(modelo.getVigenciaDe());
							modelo.setVigenciaA(fn.formatDate_MonthCadena(newcontenido.split("\n")[i+1].split("###")[3].replace("###", "").trim()));
						}
					}
					if(newcontenido.split("\n")[i].contains("Marca") && newcontenido.split("\n")[i].contains("Clave:")) {
						modelo.setMarca( newcontenido.split("\n")[i].split("Clave:")[1]);
						modelo.setClave( fn.numTx(newcontenido.split("\n")[i].split("Clave:")[1]));
					}
					if(newcontenido.split("\n")[i].contains("ASEGURADO")){
						if(newcontenido.split("\n")[i+2].contains("Y-O")) {
							modelo.setCteNombre(newcontenido.split("\n")[i+2].split("Y-O")[0].trim());
							
						}
					}
					if(newcontenido.split("\n")[i].contains("Modelo")){
						modelo.setModelo(Integer.parseInt(newcontenido.split("\n")[i].split("Modelo:")[1].split("###")[1]));
						newdireccion = newcontenido.split("\n")[i].split("Modelo:")[0];
					}
					
				}
            }
            
			
			return modelo;
		} catch (Exception ex) {
			modelo.setError(AfirmeAutosModel.this.getClass().getTypeName() + " - catch:" + ex.getMessage() + " | "
					+ ex.getCause());
			return modelo;
		}
	}

}
