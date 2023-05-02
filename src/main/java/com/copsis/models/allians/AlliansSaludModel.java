package com.copsis.models.allians;

import java.util.ArrayList;
import java.util.List;

import com.copsis.constants.ConstantsValue;
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
			 fin = contenido.indexOf(ConstantsValue.COBERTURAS_BASICA);
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
			 
			 newcont = new StringBuilder();
			 inicio =  contenido.indexOf(ConstantsValue.COBERTURAS_BASICA);
			 fin = contenido.indexOf("Beneficios y Coberturas");
			
	
			 if (inicio > -1 && fin > -1 && inicio < fin) {
				 newcont = new StringBuilder();
				 newcont.append(contenido.substring(inicio, fin).replace("@@@", "").replace("\r", ""));
			 }
			

			 for (int i = 0; i < newcont.toString().split("\n").length; i++) {
			
				if(newcont.toString().split("\n")[i].contains("Agente") && newcont.toString().split("\n")[i].contains("Contratada")){
					List<String> valores = fn.obtenerListNumeros2(newcont.toString().split("\n")[i+1]);					
					if(valores.size() ==1){
						modelo.setCveAgente(valores.get(0));
					}
					if(valores.size() ==8){
						modelo.setCveAgente(valores.get(7));
						modelo.setAgente(newcont.toString().split("\n")[i+1].split(modelo.getCveAgente())[1]
						+"" + newcont.toString().split("\n")[i+2].split("###")[0]);
					}				
					if(newcont.toString().split("\n")[i+2].contains("%") && newcont.toString().split("\n")[i+2].split("###").length == 7){
						modelo.setAgente(
							(newcont.toString().split("\n")[i+1].split(modelo.getCveAgente())[1] +" "+
						newcont.toString().split("\n")[i+2].split("###")[4] +" "+
						newcont.toString().split("\n")[i+2].split("###")[5]).trim()
						);
					}
					
				}
			 }
			
			 newcont = new StringBuilder();
			 inicio =  contenido.indexOf("Otras Coberturas");
			 fin = contenido.indexOf("En caso de contratarse");
			 fin = fin == -1 ? contenido.indexOf("Cláusulas que se anexan") : fin;
			
			 if (inicio > -1 && fin > -1 && inicio < fin) {
				
				 newcont.append(contenido.substring(inicio, fin).replace("@@@", "").replace("\r", "")
				 .replace("ELIMINACION DE DEDUCIBLE", "ELIMINACION DE DEDUCIBLE POR ACCIDENTE")
				 .replace("Actividades###elExtranjero###", "Actividades elExtranjero###")				 );
			 }
			 
			 if(newcont.toString().length() > -1) {
				String sa="";
					List<EstructuraCoberturasModel> coberturas = new ArrayList<>();
				 for (int i = 0; i < newcont.toString().split("\n").length; i++) {
				
						EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();
					if(newcont.toString().split("\n")[i].contains("%")  || newcont.toString().split("\n")[i].contains("Amparado")
					   || (newcont.toString().split("\n")[i].contains("Asegurada") && newcont.toString().split("\n")[i].contains("Maternidad"))
					) {					
						int sp = newcont.toString().split("\n")[i].split("###").length;
						
						  if (sp == 3) {
							sa = newcont.toString().split("\n")[i].split("###")[2];
							if(newcont.toString().split("\n")[i+1].length() > 10 && newcont.toString().split("\n")[i+1].split("###").length >4){
								sa += newcont.toString().split("\n")[i+1].split("###")[4];
							}
							 cobertura.setNombre( newcont.toString().split("\n")[i].split("###")[1]);
							  cobertura.setSa( sa.replace("AmparadoPOR ACCIDENTE", "Amparado") );
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
			 
			 if(modelo.getCoberturas().isEmpty()){
				inicio =  contenido.indexOf("Cobertura Básica");
				fin = contenido.indexOf("En cumplimiento a lo dispuesto");
				 if (inicio > -1 && fin > -1 && inicio < fin) {
					 newcont = new StringBuilder();
					 newcont.append(contenido.substring(inicio, fin).replace("@@@", "").replace("\r", ""));
				 }
				 List<EstructuraCoberturasModel> coberturas = new ArrayList<>();
				 for (int i = 0; i < newcont.toString().split("\n").length; i++) {
						EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();
						
						if(!newcont.toString().split("\n")[i].contains("Básica")
						&& !newcont.toString().split("\n")[i].contains("Coaseguro")
						&& !newcont.toString().split("\n")[i].contains("AGENTE")
						&& !newcont.toString().split("\n")[i].contains("Contratada") &&
						newcont.toString().split("\n")[i].length() >10 ){
							cobertura.setNombre( "Básica");
							cobertura.setSa( newcont.toString().split("\n")[i].split("###")[0]);
							cobertura.setDeducible( newcont.toString().split("\n")[i].split("###")[1]);
							cobertura.setCoaseguro( newcont.toString().split("\n")[i].split("###")[2]);
							coberturas.add(cobertura); 
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
