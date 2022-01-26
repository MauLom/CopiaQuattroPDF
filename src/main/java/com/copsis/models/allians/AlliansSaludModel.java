package com.copsis.models.allians;

import java.util.ArrayList;
import java.util.List;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraAseguradosModel;
import com.copsis.models.EstructuraCoberturasModel;
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
							modelo.setPlan(newcont.toString().split("\n")[i].split("PLAN")[1].replace("###", "").trim());
						}
						
						if(modelo.getPlan().length() == 0  && newcont.toString().split("\n")[i].contains("PLAN")) {
							modelo.setPlan(newcont.toString().split("\n")[i].split("PLAN")[1].replace("###", "").trim());
						}
						
						if(newcont.toString().split("\n")[i].contains("C.P:")) {
							modelo.setCp(newcont.toString().split("\n")[i].split("###")[1]);
						}
						
						if(newcont.toString().split("\n")[i].contains("-") && newcont.toString().split("\n")[i].split("-").length > 4) {
							modelo.setFormaPago(fn.formaPagoSring(newcont.toString().split("\n")[i]));
							modelo.setMoneda(fn.buscaMonedaEnTexto(newcont.toString().split("\n")[i]));
							modelo.setPoliza(newcont.toString().split("\n")[i].split("###")[0]);
							modelo.setFechaEmision(fn.formatDateMonthCadena(newcont.toString().split("\n")[i].split("###")[1]));
							modelo.setVigenciaDe(fn.formatDateMonthCadena(newcont.toString().split("\n")[i].split("###")[2]));
							modelo.setVigenciaA(fn.formatDateMonthCadena( newcont.toString().split("\n")[i].split("###")[3]));
						}
					}				
			   }		
			 
			 

			 inicio =  contenido.indexOf("Asegurados");
			 fin = contenido.indexOf("Derecho");
			 if (inicio > -1 && fin > -1 && inicio < fin) {
				  List<EstructuraAseguradosModel> asegurados = new ArrayList<>();
				 newcont = new StringBuilder();
				 newcont.append(contenido.substring(inicio, fin).replace("@@@", "").replace("\r", ""));
				 for (int i = 0; i < newcont.toString().split("\n").length; i++) {
					 EstructuraAseguradosModel asegurado = new EstructuraAseguradosModel();
	
					 if(newcont.toString().split("\n")[i].contains("-")){				

						 if(newcont.toString().split("\n")[i].split("###")[0].split(" ").length == 2) {
							 asegurado.setNombre(newcont.toString().split("\n")[i].split("###")[0] +" " + newcont.toString().split("\n")[i+1].split("###")[0]);
						 }else {
							 asegurado.setNombre(newcont.toString().split("\n")[i].split("###")[0]);
						 }
							asegurado.setParentesco(fn.parentesco(newcont.toString().split("\n")[i].split("###")[1]));							
							asegurado.setSexo(fn.sexo(newcont.toString().split("\n")[i].split("###")[2]) ? 1:0);
							asegurado.setFechaAlta(fn.formatDateMonthCadena(newcont.toString().split("\n")[i].split("###")[3]));
							asegurado.setAntiguedad(fn.formatDateMonthCadena(newcont.toString().split("\n")[i].split("###")[4]));
							asegurado.setNacimiento(fn.formatDateMonthCadena(newcont.toString().split("\n")[i].split("###")[5]));																		
							asegurados.add(asegurado);
					 }
				 }
				 modelo.setAsegurados(asegurados);
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
			 fin = contenido.indexOf("Beneficios y Coberturas");
			 if (inicio > -1 && fin > -1 && inicio < fin) {
				 newcont = new StringBuilder();
				 newcont.append(contenido.substring(inicio, fin).replace("@@@", "").replace("\r", ""));
			 }
			 
			 
			 inicio =  contenido.indexOf("Otras Coberturas");
			 fin = contenido.indexOf("En caso de contratarse");
			 if (inicio > -1 && fin > -1 && inicio < fin) {
				
				 newcont.append(contenido.substring(inicio, fin).replace("@@@", "").replace("\r", ""));
			 }
			 
			 if(newcont.toString().length() > -1) {
					List<EstructuraCoberturasModel> coberturas = new ArrayList<>();
				 for (int i = 0; i < newcont.toString().split("\n").length; i++) {
						EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();
					if(newcont.toString().split("\n")[i].contains("%")  || newcont.toString().split("\n")[i].contains("Amparado")
					   || (newcont.toString().split("\n")[i].contains("Asegurada") && newcont.toString().split("\n")[i].contains("Maternidad"))
					) {
						int sp = newcont.toString().split("\n")[i].split("###").length;
						  if (sp == 3) {
							     cobertura.setNombre( newcont.toString().split("\n")[i].split("###")[1]);
							  cobertura.setSa( newcont.toString().split("\n")[i].split("###")[2] +" "+  newcont.toString().split("\n")[i+1].split("###")[4]);
							  coberturas.add(cobertura); 
						  }
						  if (sp == 6) {
							    
	                            cobertura.setSa( newcont.toString().split("\n")[i].split("###")[0]);
	                            cobertura.setDeducible( newcont.toString().split("\n")[i].split("###")[1]);
	                            cobertura.setCoaseguro( newcont.toString().split("\n")[i].split("###")[2]);
	                            coberturas.add(cobertura); 
						  }
						 
		
					}
				}
				 modelo.setCoberturas(coberturas);
			 }
			 
			 
		
			return modelo;
		} catch (Exception e) {
			return modelo;
		}
	}
	

}
