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
			contenido = contenido.replace("R. F. C:", "R.F.C:");
			modelo.setTipo(1);
			modelo.setCia(23);
			
			inicio = contenido.indexOf("Seguro de Automóviles");
			fin = contenido.indexOf("Coberturas contratadas");
			
			if(inicio > -1 && fin > -1 &&  inicio < fin ) {
				newcontenido = contenido.substring(inicio ,fin).replace("@@@", "").replace("\r", "");
				for (int i = 0; i < newcontenido.split("\n").length; i++) {					
					if(newcontenido.split("\n")[i].contains("Póliza no.")) {
						if(newcontenido.split("\n")[i+2].contains("C.P.")) {
							modelo.setPolizaGuion(newcontenido.split("\n")[i+2].split("###")[3]);
					     modelo.setPoliza(newcontenido.split("\n")[i+2].split("###")[3].replace("-", "").replace(" ", "").trim());
						}
						if(newcontenido.split("\n")[i+1].contains("C.P.")) {
							modelo.setCp(newcontenido.split("\n")[i+2].split("C.P.")[1].split("###")[0].trim());
						}
					}
					if(newcontenido.split("\n")[i].contains("Moneda") && newcontenido.split("\n")[i].contains("Emisión")) {
						if(newcontenido.split("\n")[i+1].contains("NACIONAL") || newcontenido.split("\n")[i+1].contains("Pesos"))
						 {
							modelo.setMoneda(1);
						 }
						 int sp=newcontenido.split("\n")[i+1].split("###").length;
						 if(sp == 7) {
							 modelo.setFormaPago(fn.formaPago(newcontenido.split("\n")[i+1].split("###")[6]));
							 modelo.setFechaEmision(fn.formatDate_MonthCadena(newcontenido.split("\n")[i+1].split("###")[5]));
						 }
					}
					if(newcontenido.split("\n")[i].contains("Vigencia desde")) {						
						modelo.setVigenciaDe(fn.formatDate_MonthCadena(newcontenido.split("Vigencia desde")[1].split("###")[1]));
					}
					if(newcontenido.split("\n")[i].contains("Hasta las") && newcontenido.split("\n")[i].contains("C.P.")) {						
						modelo.setVigenciaA(fn.formatDate_MonthCadena(newcontenido.split("Hasta las")[1].split("###")[1]));
						modelo.setCp(newcontenido.split("\n")[i].split("C.P.")[1].split("###")[0].trim());
					} 
					if(newcontenido.split("\n")[i].contains("R.F.C:")) {
						modelo.setRfc(newcontenido.split("\n")[i].split("R.F.C:")[1].split("###")[0]);					
					}
					
				}	
			}			
			return modelo;
		} catch (Exception e) {
			return modelo;
		}
	}
	
}
