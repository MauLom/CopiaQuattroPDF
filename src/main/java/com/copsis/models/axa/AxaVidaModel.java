package com.copsis.models.axa;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraBeneficiariosModel;
import com.copsis.models.EstructuraCoberturasModel;
import com.copsis.models.EstructuraJsonModel;


public class AxaVidaModel {

	// Clases
	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
	// Varaibles
	private String contenido = "";

	public AxaVidaModel(String contenido) {
		this.contenido = contenido;
	}

	public EstructuraJsonModel procesar() {
		StringBuilder resultado = new StringBuilder();
	
		String newcontenido = "";
		int inicio = 0;
		int fin = 0;


		String inicontenido = fn.fixContenido(contenido);
		contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales()).replace("Carátula de Póliza ", "CARATULA DE POLIZA")
				.replace("Coberturas amparadas", "Coberturas Amparadas");
		try {

			modelo.setTipo(5);
			modelo.setCia(20);

		 inicio = contenido.indexOf("CARATULA DE POLIZA");
		 fin = contenido.indexOf("Coberturas Amparadas");
			if (inicio > -1 && fin > -1 && inicio < fin) {
			 newcontenido = contenido.substring(inicio,fin).replace("@@@", "").replace("\r", "");
			 
			 for (int i = 0; i < newcontenido.split("\n").length; i++) {
				
				 if(newcontenido.split("\n")[i].contains("Contratante") && newcontenido.split("\n")[i].contains("Póliza")) {					
					 modelo.setPoliza(newcontenido.split("\n")[i].split("Póliza")[1].split("###")[1]);
					
				 }
	
				 
				 				        
				 if(newcontenido.split("\n")[i].contains("Nombre") && newcontenido.split("\n")[i].contains("RFC:")) {
					 modelo.setCteNombre(newcontenido.split("\n")[i].split("Nombre")[1].split("RFC:")[0].replace("###", "").trim());
		
					 modelo.setRfc(newcontenido.split("\n")[i].split("RFC:")[1].replace("###", "").trim());
				 }
				 if(newcontenido.split("\n")[i].contains("Domicilio")) {
					 modelo.setCteDireccion(newcontenido.split("\n")[i].split("Domicilio")[1].replace("###", " ").trim() +" "+ newcontenido.split("\n")[i+1].replace("###", " ").trim());
				 }
				 if(newcontenido.split("\n")[i].contains("C.P.")) {
					 modelo.setCp(newcontenido.split("\n")[i].split("C.P.")[1].replace("###", "").substring(0,6).trim());
				 }
				 if(newcontenido.split("\n")[i].contains("Inicio de Vigencia") && newcontenido.split("\n")[i].contains("Prima")) {
					 modelo.setVigenciaDe(fn.formatDateMonthCadena(newcontenido.split("\n")[i].split("###")[1]));
					 modelo.setFormaPago( fn.formaPagoSring(newcontenido.split("\n")[i]));
					 if(modelo.getVigenciaDe().length() > 0 && modelo.getVigenciaDe().contains("-")) {
						 modelo.setFechaEmision(modelo.getVigenciaDe());
						 SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd"); 
						 Date fecha = formato.parse(modelo.getVigenciaDe());
						 modelo.setVigenciaA(sumar(fecha,12));
					 }
					 
					 modelo.setPrimaneta(fn.castBigDecimal( fn.castDouble(newcontenido.split("\n")[i].split("###")[3])));
				 }
				 if(newcontenido.split("\n")[i].contains("Prima Total")) {					 
					 modelo.setPrimaTotal(fn.castBigDecimal( fn.castDouble(newcontenido.split("\n")[i].split("###")[3])));
				 } 
				
				 
			}
		 }
			modelo.setMoneda(1);

			List<EstructuraBeneficiariosModel> beneficiarios = new ArrayList<>();
			inicio = inicontenido.indexOf("Beneficiarios");
			fin = inicontenido.indexOf("ADVERTENCIA");
			
			if (inicio > 0 && fin > 0 && inicio < fin) {
				newcontenido = inicontenido.split("Beneficiarios")[1].split("ADVERTENCIA")[0].replace("@@@", "")
						.replace(")", "###").replace("(", "###").trim();

				for (String bene : newcontenido.split("\n")) {
					EstructuraBeneficiariosModel beneficiario = new EstructuraBeneficiariosModel();
					int n = bene.split("###").length;
					
			
					
                    if (bene.contains("Nombres") && n == 4) {				    	
						beneficiario.setNombre(bene.split("###")[1].trim());
						beneficiario.setParentesco(fn.parentesco(bene.split("###")[2]));
						beneficiario.setTipo(11);
						beneficiario.setPorcentaje(fn.castDouble(bene.split("###")[3].trim().replace("%", "")).intValue());
						beneficiarios.add(beneficiario);

					}
					
					if (bene.contains("Nombres") && n == 3) {
				    	
						beneficiario.setNombre(bene.split("###")[0].replace("Nombres:", "").trim());
						beneficiario.setParentesco(fn.parentesco(bene.split("###")[1]));
						beneficiario.setTipo(11);
						beneficiario
								.setPorcentaje(fn.castDouble(bene.split("###")[2].trim().replace("%", "")).intValue());
						beneficiarios.add(beneficiario);

					}
					if (bene.contains("FALLECIMIENTO")) {
						String fall = newcontenido.split("FALLECIMIENTO")[1].split("\n")[1];
						if (fall.split("###").length == 3) {
							beneficiario.setNombre(fall.split("###")[0].trim());
							beneficiario.setPorcentaje(
									fn.castDouble(fall.split("###")[2].trim().replace("%", "")).intValue());
							beneficiario.setParentesco(fn.parentesco(fall.split("###")[1].trim()));
							beneficiario.setTipo(12);
							beneficiarios.add(beneficiario);
						}
					}

				}

				modelo.setBeneficiarios(beneficiarios);

			}
			
			
			 inicio = contenido.indexOf("Coberturas Amparadas");
			 fin = contenido.indexOf("AXA Seguros, S.A. de C.V.");

		
				if (inicio > -1 && fin > -1 && inicio < fin) {
					List<EstructuraCoberturasModel> coberturas = new ArrayList<>();
				     newcontenido = contenido.substring(inicio,fin).replace("@@@", "").replace("\r", "");
				     for (int i = 0; i < newcontenido.split("\n").length; i++) {
				    	 EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();
				    	 if(!newcontenido.split("\n")[i].contains("Coberturas Amparadas") && !newcontenido.split("\n")[i].contains("Asegurada")
				    			 && !newcontenido.split("\n")[i].contains("Prima") && !newcontenido.split("\n")[i].contains("R E S U M E N")
				    			 && !newcontenido.split("\n")[i].contains("VIDA")  &&  newcontenido.split("\n")[i].split("###").length == 6) {			
				    		 
				    			 cobertura.setNombre(newcontenido.split("\n")[i].split("###")[0]);
				    			 cobertura.setSa(newcontenido.split("\n")[i].split("###")[1]);
				    				coberturas.add(cobertura);
				    		
				    		
				    	 }
				    	
				     }
				     modelo.setCoberturas(coberturas);
				}

			return modelo;
		} catch (Exception ex) {
		ex.printStackTrace();
			modelo.setError(
					AxaVidaModel.this.getClass().getTypeName() + " | " + ex.getMessage() + " | " + ex.getCause());
			return modelo;
		}

	}
	public String sumar(Date fecha,int meses) {

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(fecha);
		calendar.add(Calendar.MONTH,meses);
		
		 SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd"); 
		return formato.format( calendar.getTime());
	}

}
