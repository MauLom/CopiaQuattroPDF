package com.copsis.models.atlas;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraAseguradosModel;
import com.copsis.models.EstructuraBeneficiariosModel;
import com.copsis.models.EstructuraJsonModel;

public class AtlasVidaModel {
	// Clases
		private DataToolsModel fn = new DataToolsModel();
		private EstructuraJsonModel modelo = new EstructuraJsonModel();
		// Varaibles
		private String antcontenido = "";
		private String contenido = "";
		private String newcontenido = "";
		private String recibosText = "";
		private String resultado = "";
		private int donde = 0;
		private int inicio = 0;
		private int fin = 0;

		
		public AtlasVidaModel(String contenido) {
			this.contenido = contenido;
			
		}
		public EstructuraJsonModel procesar() {
			antcontenido = fn.fixContenido(contenido);
			contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());
			contenido =contenido.replace("las 12:00 Hrs.del", "");
			try {
				//tipo
				modelo.setTipo(5);

				//cia
				modelo.setCia(33);
				
				//Datos del Contrantante
				inicio = contenido.indexOf("PÓLIZA");
	            fin = contenido.indexOf("Asegurado");

	            
	            if(inicio > 0 && fin > 0 && inicio < fin) {
	            	newcontenido = contenido.substring(inicio,  fin).replace("\r", "").replace("@@@", "").trim();
	            	for (int i = 0; i < newcontenido.split("\n").length; i++) {
	            		if(newcontenido.split("\n")[i].contains("Póliza")) {
	            			modelo.setPolizaGuion(newcontenido.split("\n")[i].split("Póliza")[1].replace("###", ""));
	            		  modelo.setPoliza(newcontenido.split("\n")[i].split("Póliza:")[1].replace("###", "").replace("-", "").replace(" ", ""));	
	            		}             		
	            		if(newcontenido.split("\n")[i].contains("desde:") && newcontenido.split("\n")[i].contains("Hasta:")) {
	            			modelo.setVigenciaDe(fn.formatDate_MonthCadena(newcontenido.split("\n")[i].split("desde:")[1].split("Hasta:")[0].replace("###", "").trim()));
	            			modelo.setVigenciaA(fn.formatDate_MonthCadena(newcontenido.split("\n")[i].split("Hasta:")[1].split("Fecha")[0].replace("###", "").trim()));
	            			modelo.setFechaEmision(fn.formatDate_MonthCadena(newcontenido.split("\n")[i].split("expedición:")[1].replace("###", "").trim()));
	            		}            		
	            		if(newcontenido.split("\n")[i].contains("Contratante") && newcontenido.split("\n")[i].contains("Domicilio") &&  newcontenido.split("\n")[i].contains("RFC:")) {
							modelo.setCteNombre(newcontenido.split("\n")[i+1]);
							modelo.setRfc( newcontenido.split("\n")[i].split("RFC:")[1].replace("###", ""));
							resultado =newcontenido.split("\n")[i+2] +" " + newcontenido.split("\n")[i+3]; 
							modelo.setCteDireccion(resultado.replace("###", " "));
						}
	            		if(newcontenido.split("\n")[i].contains("Producto:") && newcontenido.split("\n")[i].contains("Agente:") && newcontenido.split("\n")[i].contains("Orden:")) {
							
						    modelo.setCveAgente( newcontenido.split("\n")[i].split("Agente:")[1].replace("###", ""));
						}
	            		if(newcontenido.split("\n")[i].contains("Moneda:") && newcontenido.split("\n")[i].contains("Prima Neta:")) {
							modelo.setMoneda(fn.moneda(newcontenido.split("\n")[i].split("Moneda:")[1].split("Prima")[0].replace("###", "")));
							modelo.setPrimaneta(fn.castBigDecimal(fn.castDouble(newcontenido.split("\n")[i].split("Neta:")[1].replace("###", ""))));
						} 
	            		if(newcontenido.split("\n")[i].contains("Forma Pago:") && newcontenido.split("\n")[i].contains("Fraccionado:")) {
							modelo.setFormaPago(fn.formaPago(newcontenido.split("\n")[i].split("Pago:")[1].split("Recargo")[0].replace("###", "")));
							modelo.setRecargo(fn.castBigDecimal(fn.castDouble(newcontenido.split("\n")[i].split("Fraccionado:")[1].replace("###", ""))));
						}            		
	            		if(newcontenido.split("\n")[i].contains("Recibo:") && newcontenido.split("\n")[i].contains("Expedición:")) {
							modelo.setDerecho(fn.castBigDecimal(fn.castDouble(newcontenido.split("\n")[i].split("Expedición:")[1].replace("###", ""))));
						}
						if(newcontenido.split("\n")[i].contains("IVA:")) {
							modelo.setIva(fn.castBigDecimal(fn.castDouble(newcontenido.split("\n")[i].split("IVA:")[1].replace("###", ""))));
						}					
						if(newcontenido.split("\n")[i].contains("PLAN:")) {
							modelo.setPlan(newcontenido.split("\n")[i].split("PLAN:")[1].replace("###", ""));
						}
						if(newcontenido.split("\n")[i].contains("CP")) {
							modelo.setCp(newcontenido.split("\n")[i].split("CP")[1].replace("###", ""));
						}
						if(newcontenido.split("\n")[i].contains("Total a pagar:")) {
							modelo.setPrimaTotal(fn.castBigDecimal(fn.castDouble(newcontenido.split("\n")[i].split("Total a pagar:")[1].replace("###", ""))));
						}
	            	}
	            }
	            
	            inicio = contenido.indexOf("Hasta:");
	           
				if(inicio > -1) {
					newcontenido = contenido.substring(inicio, contenido.indexOf("Fecha expedi", inicio));
					if(newcontenido.contains("Hasta:")) {
						if(modelo.getFechaEmision().length() > 0) {
							modelo.setVigenciaA(fn.formatDate_MonthCadena(newcontenido.split("Hasta:")[1].replace("###", "").trim()));
							LocalDate date = LocalDate.parse(modelo.getFechaEmision());
							modelo.setVigenciaA(modelo.getVigenciaA().replace(modelo.getVigenciaA().split("-")[0], String.valueOf(date.getYear() +1)));	
						}
					}
				}
				
				/*****/
				//plazo_pago
				inicio = contenido.indexOf("Plazo pago primas años");
				if(inicio > -1) {
					newcontenido = contenido.substring(inicio +22, contenido.indexOf("Plazo del seguro", inicio + 22)).replace("###", "").trim();
					if(fn.isNumeric(newcontenido)) modelo.setPlazoPago(Integer.parseInt(newcontenido));
				}
				
				//plazo
				inicio = contenido.indexOf("Plazo del seguro en años");
				if(inicio > -1) {
					newcontenido = contenido.substring(inicio +24, contenido.indexOf("\r\n", inicio + 24)).replace("###", "").trim();
					if(fn.isNumeric(newcontenido)) modelo.setPlazo(Integer.parseInt(newcontenido));
				}
				
				//tipo_vida
				int tipo_vida = 0;
				inicio = contenido.indexOf("PREVER ");
				if(inicio > -1) {
					newcontenido = contenido.substring(inicio + 7, contenido.indexOf("Plazo pa", inicio)).trim();
					if(newcontenido.split(" ").length > 1) {
						switch (newcontenido.split(" ")[0]) {
						case "ORDINARIO":
							tipo_vida = 1;
							break;
						case "DOTALES":
							tipo_vida = 3;
							break;
						}
					}
				}
				modelo.setTipovida(tipo_vida);
				
				//aportacion VITALICIO / AHORRO
				if(tipo_vida > 0) {
					if(tipo_vida == 1 || tipo_vida == 3) modelo.setAportacion(1);
				}

				//retiro
				if(tipo_vida == 3) modelo.setRetiro(1);
				
				
				
				//******************************BENEFICIARIOS***********************************
				inicio = antcontenido.indexOf("Beneficiarios###Clausulas Adicionales");
				fin = antcontenido.indexOf("Pago inmediato parcial");
				List<EstructuraBeneficiariosModel> beneficiarios = new ArrayList<>();
				if(inicio > 0 && fin  > 0 && inicio < fin) {
		
					newcontenido = antcontenido.substring(inicio + 37, fin).trim();
					newcontenido = newcontenido.split("\r\n")[0].replace("(", "@@@").replace(")", "@@@");
					
					switch (newcontenido.split("@@@").length) {
					case 3:
						EstructuraBeneficiariosModel beneficiario = new EstructuraBeneficiariosModel();
						beneficiario.setNombre(newcontenido.split("@@@")[0].trim());
						beneficiario.setParentesco(fn.parentesco(newcontenido.split("@@@")[1].toLowerCase()));
						switch (tipo_vida) {
						case 1:
							beneficiario.setTipo(11);	
							break;
						case 3:
							beneficiario.setTipo(11);
							break;
						}
						beneficiario.setPorcentaje(Integer.parseInt(newcontenido.split("@@@")[2].replace("_", "").replace("%", "").trim()));
						beneficiarios.add(beneficiario);
						break;
					}
					
				}
				
				
				inicio = antcontenido.indexOf("DE FALLECIMIENTO PAGAR A :");
				fin = antcontenido.indexOf("PARTES IGUALES");
				if(inicio > -1) {
	
					newcontenido = antcontenido.substring(inicio + 26, fin).trim();
					resultado = "";
					for(String x: newcontenido.split("\r\n")) {
						if(x.split("###").length == 2) resultado += x.split("###")[0] + "\r\n";
						else resultado += x + "\r\n";
					}
					if(resultado.contains("(HIJOS)")) resultado = resultado.split("HI")[0].replace("(", ")");
					if(resultado.contains(" Y ")) {
						EstructuraBeneficiariosModel beneficiario = new EstructuraBeneficiariosModel();
						EstructuraBeneficiariosModel beneficiario1 = new EstructuraBeneficiariosModel();
						beneficiario.setNombre(resultado.split(" Y ")[0].replace(")", "").trim());
						beneficiario.setPorcentaje(50);
						beneficiario.setParentesco(3);
						beneficiario.setTipo(12);
						beneficiarios.add(beneficiario);
						
						beneficiario1.setNombre(resultado.split(" Y ")[1].replace(")", "").replace("\r\n", " ").trim());
						beneficiario1.setPorcentaje(50);
						beneficiario1.setParentesco(3);
						beneficiario1.setTipo(12);
						beneficiarios.add(beneficiario1);
					}
					
				}
				modelo.setBeneficiarios(beneficiarios);
				
				
				//****************************asegurados*************************************
				donde = 0;
				donde = fn.recorreContenido(contenido, "Asegurado:");
				List<EstructuraAseguradosModel> asegurados = new ArrayList<>();				
				if(donde > 0){
					for(String dato:contenido.split("@@@")[donde].split("\r\n")) {
						if(dato.contains("Asegurado:") &&  dato.contains("Nacimiento") && dato.split("###").length == 6) {
							EstructuraAseguradosModel asegurado = new EstructuraAseguradosModel();
							asegurado.setNombre(dato.split("###")[1].trim());
							asegurado.setNacimiento(fn.formatDate_MonthCadena(dato.split("###")[3].trim()));
							asegurado.setSexo(1); //DEFAULT
							asegurado.setParentesco(1); //DEFAULT
							asegurados.add(asegurado);
						}
					}
				}
				modelo.setAsegurados(asegurados);
				
				inicio = contenido.lastIndexOf("Agente:");
				fin = contenido.lastIndexOf("En cumplimiento");
				if(inicio > -1 && fin > inicio) {
					newcontenido = fn.gatos(contenido.substring(inicio + 7, fin).replace("@@@", "").replace("\r\n", "").trim());
					if(newcontenido.contains("Seguros Atlas,")) modelo.setAgente(newcontenido.split("Seguros Atlas,")[0].trim());
					else modelo.setAgente(newcontenido);
				}
				
				


				
	        
				
				return modelo;
			} catch (Exception ex) {
				modelo.setError(
						AtlasVidaModel.this.getClass().getTypeName() + " - catch:" + ex.getMessage() + " | " + ex.getCause());
				return modelo;
			}
			
		}

}
