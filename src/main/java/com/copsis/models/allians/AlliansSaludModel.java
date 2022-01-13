package com.copsis.models.allians;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraJsonModel;

public class AlliansSaludModel {
	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
	private String contenido = "";
	public AlliansSaludModel(String contenido) {
		this.contenido = contenido;
	}
	
	public EstructuraJsonModel procesar() {
		StringBuilder newcont = new StringBuilder();
		int inicio = 0;
		int fin = 0;
		
		contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());
		try {
			modelo.setTipo(3);		
			modelo.setCia(4);
			

			
			 inicio =  contenido.indexOf("Contratante");
			 fin = contenido.indexOf("Parentesco");
			 if (inicio > -1 && fin > -1 && inicio < fin) {
				
				 newcont.append(contenido.substring(inicio, fin).replace("@@@", "").replace("\r", ""));
					for (int i = 0; i < newcont.toString().split("\n").length; i++) {
						
						if(newcont.toString().split("\n")[i].contains("Contratante")) {
							modelo.setCteNombre(newcont.toString().split("\n")[i+1].split("###")[0].trim());
						}
						if(newcont.toString().split("\n")[i].contains("NUM. EXT") && newcont.toString().split("\n")[i].contains("PLAN")) {
							modelo.setCteDireccion(newcont.toString().split("\n")[i].split("PLAN")[0].replace("###", " ")
									+" " +newcont.toString().split("\n")[i+1].replace("###", " ")
									+" " +newcont.toString().split("\n")[i+2].replace("###", " ")
									);
						}
						if(newcont.toString().split("\n")[i].contains("C.P:")) {
							modelo.setCp(newcont.toString().split("\n")[i].split("###")[1]);
						}
						
						if(newcont.toString().split("\n")[i].contains("-") && newcont.toString().split("\n")[i].split("-").length > 4) {
							modelo.setFormaPago(fn.formaPagoSring(newcont.toString().split("\n")[i]));
							modelo.setMoneda(fn.buscaMonedaEnTexto(newcont.toString().split("\n")[i]));
							modelo.setFechaEmision(fn.formatDateMonthCadena(newcont.toString().split("\n")[i].split("###")[1]));
							modelo.setVigenciaDe(fn.formatDateMonthCadena(newcont.toString().split("\n")[i].split("###")[2]));
							modelo.setVigenciaA(fn.formatDateMonthCadena( newcont.toString().split("\n")[i].split("###")[3]));
						}
					}				
			   }			
			
			 inicio =  contenido.indexOf("Prima Neta");
			 fin = contenido.indexOf("Cobertura Básica");
			 if (inicio > -1 && fin > -1 && inicio < fin) {
				 newcont = new StringBuilder();
				 newcont.append(contenido.substring(inicio, fin).replace("@@@", "").replace("\r", ""));
					for (int i = 0; i < newcont.toString().split("\n").length; i++) {
					  
						if(newcont.toString().split("\n")[i].contains("Fraccionado") && newcont.toString().split("\n")[i+1].split("###").length ==  9) {
						    modelo.setPrimaneta(fn.castBigDecimal(fn.preparaPrimas(newcont.toString().split("\n")[i+1].split("###")[0])));
							modelo.setDerecho(fn.castBigDecimal(fn.preparaPrimas(newcont.toString().split("\n")[i+1].split("###")[2])));
							modelo.setRecargo(fn.castBigDecimal(fn.preparaPrimas(newcont.toString().split("\n")[i+1].split("###")[3])));							
							modelo.setIva(fn.castBigDecimal(fn.preparaPrimas(newcont.toString().split("\n")[i+1].split("###")[5])));
							modelo.setPrimaTotal(fn.castBigDecimal(fn.preparaPrimas(newcont.toString().split("\n")[i+1].split("###")[6])));
						}				   		
					}
			 }
			 
			 inicio =  contenido.indexOf("Cobertura Básica");
			 fin = contenido.indexOf("Cobertura Básica");
			 if (inicio > -1 && fin > -1 && inicio < fin) {
				 newcont = new StringBuilder();
				 newcont.append(contenido.substring(inicio, fin).replace("@@@", "").replace("\r", ""));
			 }
			 
		
			return modelo;
		} catch (Exception e) {
			return modelo;
		}
	}
	

}
