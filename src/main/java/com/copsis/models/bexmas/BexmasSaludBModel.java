package com.copsis.models.bexmas;

import java.util.ArrayList;
import java.util.List;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraAseguradosModel;
import com.copsis.models.EstructuraCoberturasModel;
import com.copsis.models.EstructuraJsonModel;

public class BexmasSaludBModel {
	// Clases
	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
	// Varaibles
	private String contenido = "";

	public BexmasSaludBModel(String contenido) {
		this.contenido = contenido;
	}

	public EstructuraJsonModel procesar() {
		int inicio = 0;
		int fin = 0;
		boolean sumaasegura = true;
		StringBuilder newcont = new StringBuilder();
		StringBuilder newcoberturas = new StringBuilder();
		StringBuilder newcob = new StringBuilder();
		contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());
		try {
			
			modelo.setTipo(3);
			modelo.setCia(98);
			
			inicio = contenido.indexOf("Nombre del Contratante:");
			fin = contenido.indexOf("Datos de los Asegurados");
			
			if(inicio >  -1 && fin > -1  &&  inicio < fin) {
				newcont.append(contenido.substring(inicio,fin).replace("@@@", "").replace("\r", "").replace("12:00 HORAS", ""));
				
				for (int i = 0; i < newcont.toString().split("\n").length; i++) {
					
					if(newcont.toString().split("\n")[i].contains("Contratante") && newcont.toString().split("\n")[i].contains("Póliza")) {
						modelo.setCteNombre(newcont.toString().split("\n")[i+1].split("###")[0]);
						modelo.setPoliza(newcont.toString().split("\n")[i].split("###")[newcont.toString().split("\n")[i].split("###").length-1].replace("Póliza:", "").trim());
					}
					if(newcont.toString().split("\n")[i].contains("Agente")) {
						modelo.setCveAgente( newcont.toString().split("\n")[i].split("Agente:")[1].split("###")[0] );
					}
					if(newcont.toString().split("\n")[i].contains("R.F.C:")) {
						modelo.setRfc(newcont.toString().split("\n")[i].split("R.F.C:")[1].split("###")[1] );
					}
					if(newcont.toString().split("\n")[i].contains("Moneda") && newcont.toString().split("\n")[i].contains("Forma de Pago")  && newcont.toString().split("\n")[i].contains("Domicilio") ) {
						modelo.setFormaPago(fn.formaPagoSring(newcont.toString().split("\n")[i]));
						modelo.setMoneda(fn.buscaMonedaEnTexto(newcont.toString().split("\n")[i]));
					}
					if(newcont.toString().split("\n")[i].contains("Domicilio")) {
						modelo.setCteDireccion(newcont.toString().split("\n")[i+1]);
					
					}
					if(newcont.toString().split("\n")[i].contains("Plan:")) {
						modelo.setPlan(newcont.toString().split("\n")[i].split("Plan:")[1].split("###")[0]);
					}
					if(newcont.toString().split("\n")[i].split("-").length > 4 && newcont.toString().split("\n")[i].split("###").length == 4) {						
						modelo.setVigenciaDe(fn.formatDateMonthCadena(newcont.toString().split("\n")[i].split("###")[0].trim()));
						modelo.setVigenciaA(fn.formatDateMonthCadena(newcont.toString().split("\n")[i].split("###")[2].trim()));
						modelo.setFechaEmision(modelo.getVigenciaDe());
					}
					
					

				}
			}
			

			List<EstructuraAseguradosModel> asegurados = new ArrayList<>();
			inicio = contenido.indexOf("Datos de los Asegurados");
			fin = contenido.indexOf("Detalle del Seguro");
			if(inicio >  -1 && fin > -1  &&  inicio < fin) {
				newcont = new StringBuilder();
				newcont.append(contenido.substring(inicio,fin).replace("@@@", "").replace("\r", ""));
				
				for (int i = 0; i < newcont.toString().split("\n").length; i++) {
					EstructuraAseguradosModel asegurado = new EstructuraAseguradosModel();
				
					if(newcont.toString().split("\n")[i].split("-").length > 3) {	
						
					    asegurado.setNombre(newcont.toString().split("\n")[i].split("###")[0]);
						asegurado.setNacimiento(fn.formatDateMonthCadena(newcont.toString().split("\n")[i].split("###")[1]));
						asegurado.setAntiguedad(fn.formatDateMonthCadena(newcont.toString().split("\n")[i].split("###")[2]));
						asegurado.setParentesco(fn.parentesco(newcont.toString().split("\n")[i].split("###")[3]));
						asegurado.setEdad(fn.castInteger(newcont.toString().split("\n")[i].split("###")[4]));
						asegurado.setSexo(fn.sexo(newcont.toString().split("\n")[i].split("###")[5])  ? 1 : 0);
						asegurados.add(asegurado);
					}								
				}
				
				modelo.setAsegurados(asegurados);
			}
			

			
			inicio = contenido.indexOf("Detalle del Seguro");
			fin = contenido.indexOf("Coberturas");
			if(inicio >  -1 && fin > -1  &&  inicio < fin) {
				newcont = new StringBuilder();
				newcont.append(contenido.substring(inicio,fin).replace("@@@", "").replace("\r", ""));
				for (int i = 0; i < newcont.toString().split("\n").length; i++) {				
					if(newcont.toString().split("\n")[i].contains("%") &&  sumaasegura) {
						modelo.setSa(newcont.toString().split("\n")[i].split("###")[1]);
						modelo.setDeducible(newcont.toString().split("\n")[i].split("###")[2]);
						modelo.setCoaseguro(newcont.toString().split("\n")[i].split("###")[3]);	
						sumaasegura = false;
					}
				}
			}
			

			inicio = contenido.indexOf("Coberturas");
			fin = contenido.indexOf("Esta póliza queda");
			if(inicio >  -1 && fin > -1  &&  inicio < fin) {
				newcont = new StringBuilder();
				newcont.append(contenido.substring(inicio,fin).replace("@@@", "").replace("\r", ""));
		
			}
			
			inicio = contenido.indexOf("Coberturas Adicionales");
			fin = contenido.lastIndexOf("Art. 41");
			if(inicio >  -1 && fin > -1  &&  inicio < fin) {
				newcoberturas = new StringBuilder();
				newcoberturas.append(contenido.substring(inicio,fin).replace("@@@", "").replace("\r", "").split("En testimonio de")[0]);
		
			}
			
			newcob.append(newcont +" "+ newcoberturas);
			
			List<EstructuraCoberturasModel> coberturas = new ArrayList<>();
			if(newcob.toString().length() > 50) {
				for (int i = 0; i < newcob.toString().split("\n").length; i++) {
					if(!newcob.toString().split("\n")[i].contains("Cobertura Básica") && !newcob.toString().split("\n")[i].contains("Coberturas Adicionales")
				 && !newcob.toString().split("\n")[i].contains("coaseguro###") && !newcob.toString().split("\n")[i].contains("Recargo")
				 && !newcob.toString().split("\n")[i].contains("Expedición###")   		 && !newcob.toString().split("\n")[i].contains("Prima Neta")  
				 && !newcob.toString().split("\n")[i].contains("Alcance")
							) {	
						EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();
		
						switch (newcob.toString().split("\n")[i].split("###").length) {
						case 1:	case 2:
							if(!newcob.toString().split("\n")[i].contains("Subtotal") && newcob.toString().split("\n")[i].length() > 10) {
								cobertura.setNombre(newcob.toString().split("\n")[i].split("###")[0]);						
								coberturas.add(cobertura);
							}											
							break;
						case 3:
							
							if(newcob.toString().split("\n")[i].contains("Total") || newcob.toString().split("\n")[i].contains("Descuento") || newcob.toString().split("\n")[i].contains("Subtotal") ){
								cobertura.setNombre(newcob.toString().split("\n")[i].split("###")[0]);						
								coberturas.add(cobertura);	
							}else {
								cobertura.setNombre(newcob.toString().split("\n")[i].split("###")[0]);
								cobertura.setSa(newcob.toString().split("\n")[i].split("###")[2]);		
								coberturas.add(cobertura);
							}
							break;
						default:
							break;
						}
						
					}
					
				}
				modelo.setCoberturas(coberturas);
			}
			

			inicio = contenido.indexOf("Prima Neta");
			fin = contenido.indexOf("Prima Total");
			if(inicio >  -1 && fin > -1  &&  inicio < fin) {
				newcont = new StringBuilder();
				newcont.append(contenido.substring(inicio,fin).replace("@@@", "").replace("\r", ""));
				for (int i = 0; i < newcont.toString().split("\n").length; i++) {

					if(newcont.toString().split("\n")[i].contains("Prima Neta")) {
						 modelo.setPrimaneta(fn.castBigDecimal( fn.castDouble(newcont.toString().split("\n")[i].split("###")[1])));
					}
					if(newcont.toString().split("\n")[i].contains("Fraccionado") || newcont.toString().split("\n")[i].contains("Fraccionario")) {
						 modelo.setRecargo(fn.castBigDecimal( fn.castDouble(newcont.toString().split("\n")[i].split("###")[1])));
					}
					if(newcont.toString().split("\n")[i].contains("Expedición")) {
						 modelo.setDerecho(fn.castBigDecimal( fn.castDouble(newcont.toString().split("\n")[i].split("###")[1])));
					}
                   if(newcont.toString().split("\n")[i].contains("I.V.A.")) {
                   	 modelo.setIva(fn.castBigDecimal( fn.castDouble(newcont.toString().split("\n")[i].split("I.V.A.")[1].replace("###", ""))));
					}
                   if(newcont.toString().split("\n")[i].contains("reciba la") && fn.isNumeric(fn.cleanString(newcont.toString().split("\n")[i].split("reciba la")[1].replace("###","").trim()))) {
                     	 modelo.setPrimaTotal(fn.castBigDecimal( fn.castDouble(newcont.toString().split("\n")[i].split("reciba la")[1].replace("###", ""))));
  					}
                   
				}
			}
			
			

			return modelo;
		} catch (Exception ex) {
			modelo.setError(BexmasSaludBModel.this.getClass().getTypeName() + " - catch:" + ex.getMessage() + " | " + ex.getCause());
			return modelo;
		}
	}

}
