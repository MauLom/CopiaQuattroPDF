package com.copsis.models.sura;

import java.util.ArrayList;
import java.util.List;

import com.copsis.constants.ConstantsValue;
import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraAseguradosModel;
import com.copsis.models.EstructuraCoberturasModel;
import com.copsis.models.EstructuraJsonModel;

public class SuraSaludBModel {
	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
	private String contenido;
	
	

	public SuraSaludBModel(String contenidox) {
		this.contenido = contenidox;
	}
	
	public EstructuraJsonModel procesar() {
		int inicio = 0;
		int fin = 0;
		StringBuilder newCont = new StringBuilder();
		contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());
		
		try {
			
			modelo.setTipo(3);
			modelo.setCia(88);
		
			inicio = contenido.indexOf("GASTOS MÉDICOS MAYORES");
			fin = contenido.indexOf(ConstantsValue.COBERTURAS_CONTRATADAS2);
			if(inicio > -1 && fin > -1   && inicio  < fin ) {
				newCont.append(contenido.substring(inicio,fin).replace("@@@", "").replace("\r", "").trim());
				modelo.setFormaPago(fn.formaPagoSring(newCont.toString()));
				modelo.setMoneda(fn.buscaMonedaEnTexto(newCont.toString()));

				for (int i = 0; i < newCont.toString().split("\n").length; i++) {
					if(newCont.toString().split("\n")[i].contains("Póliza no.")) {
						modelo.setPoliza(newCont.toString().split("\n")[i+1].split("###")[newCont.toString().split("\n")[i+1].split("###").length-1]);
					}
				
					
					if(newCont.toString().split("\n")[i].contains("Vigencia") && newCont.toString().split("\n")[i].split("-").length > 2 && newCont.toString().split("\n")[i].contains("Importes"))
					{
					modelo.setVigenciaDe(fn.formatDateMonthCadena(newCont.toString().split("\n")[i].split("Vigencia")[1].split("Importes")[0].replace("desde", "").replace("###", "").trim()));
					obtenerDireccion(newCont.toString().split("\n"),i);
						
					}
					if(newCont.toString().split("\n")[i].contains("Emisión") && (i+2)<newCont.toString().split("\n")[i].length() && modelo.getFechaEmision().length() == 0) {
						if(newCont.toString().split("\n")[i+2].contains("###") && newCont.toString().split("\n")[i+2].contains("-")) {
							if(newCont.toString().split("\n")[i+2].split("###")[0].length() == 10) {
								modelo.setFechaEmision(fn.formatDateMonthCadena(newCont.toString().split("\n")[i+2].split("###")[0]));
							}
						}
					}
					if(newCont.toString().split("\n")[i].contains("Hasta las") && newCont.toString().split("\n")[i].split("-").length > 2 && newCont.toString().split("\n")[i].contains("SUB-SEC."))
					{
					modelo.setVigenciaA(fn.formatDateMonthCadena(newCont.toString().split("\n")[i].split("Hasta las")[1].split("SUB-SEC.")[0].replace("###", "").trim()));	
						
					}
					if(newCont.toString().split("\n")[i].contains("Datos del contratante")) {
						modelo.setCteNombre(newCont.toString().split("\n")[i+1]);
					}
					if(newCont.toString().split("\n")[i].contains("R.F.C.")) {
						modelo.setRfc(newCont.toString().split("\n")[i].split("R.F.C.")[1].split("###")[0].trim());
					}
				
				}
				
				
			}
			
			inicio = contenido.indexOf("Asegurado");
			fin = contenido.indexOf(ConstantsValue.COBERTURAS_CONTRATADAS2);
			if(inicio > -1 && fin > -1   && inicio  < fin ) {
			 	List<EstructuraAseguradosModel> asegurados = new ArrayList<>();
				newCont = new StringBuilder();
				newCont.append(contenido.substring(inicio,fin).replace("@@@", "").replace(" ", "###").replace("\r", "").trim());
				for (int i = 0; i < newCont.toString().split("\n").length; i++) {
					EstructuraAseguradosModel asegurado = new EstructuraAseguradosModel();   
					if(newCont.toString().split("\n")[i].split("###")[newCont.toString().split("\n")[i].split("###").length-1].contains("-")) {
						asegurado.setAntiguedad(fn.formatDateMonthCadena(newCont.toString().split("\n")[i].split("###")[newCont.toString().split("\n")[i].split("###").length-1]));
						asegurado.setParentesco(fn.parentesco( newCont.toString().split("\n")[i].split("###")[newCont.toString().split("\n")[i].split("###").length-2]));
						asegurado.setSexo(fn.sexo( newCont.toString().split("\n")[i].split("###")[newCont.toString().split("\n")[i].split("###").length-3]).booleanValue() ? 1:0);
						asegurado.setNacimiento(fn.formatDateMonthCadena(newCont.toString().split("\n")[i].split("###")[newCont.toString().split("\n")[i].split("###").length-4]));
						asegurado.setNombre(newCont.toString().split("\n")[i].split(newCont.toString().split("\n")[i].split("###")[newCont.toString().split("\n")[i].split("###").length-4])[0].replace("###", " ").trim());
						asegurados.add(asegurado);
					}
			
				}
				modelo.setAsegurados(asegurados);
			}

			
			inicio = contenido.indexOf(ConstantsValue.COBERTURAS_CONTRATADAS2);
			fin = contenido.indexOf("Gastos de expedición");
			if(inicio > -1 && fin > -1   && inicio  < fin ) {
				 List<EstructuraCoberturasModel> coberturas = new ArrayList<>();
				newCont = new StringBuilder();
				newCont.append(contenido.substring(inicio,fin).replace("@@@", "").replace("\r", "").trim());
				for (int i = 0; i < newCont.toString().split("\n").length; i++) {
					EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();
					if(!newCont.toString().split("\n")[i].contains("Prima") && !newCont.toString().split("\n")[i].contains("total")
							 && !newCont.toString().split("\n")[i].contains("Costo")  && !newCont.toString().split("\n")[i].contains("financiamiento")
							 && !newCont.toString().split("\n")[i].contains("Coberturas")) {
					
						 cobertura.setNombre(newCont.toString().split("\n")[i].split("###")[0]);
						 cobertura.setSa(newCont.toString().split("\n")[i].split("###")[1]);
						  coberturas.add(cobertura);	        
					}
				}
				modelo.setCoberturas(coberturas);
			}

			inicio = contenido.indexOf("Gastos de expedición");
			fin = contenido.indexOf("Pág.");
			if(inicio > -1 && fin > -1   && inicio  < fin ) {
				newCont = new StringBuilder();
				newCont.append(contenido.substring(inicio,fin).replace("@@@", "").replace("\r", "").trim());
				for (int i = 0; i < newCont.toString().split("\n").length; i++) {
					
					
					if(!newCont.toString().split("\n")[i].contains("expedición") || newCont.toString().split("\n")[i].split("###").length == 6) {
					    modelo.setPrimaneta(fn.castBigDecimal( fn.preparaPrimas(newCont.toString().split("\n")[i ].split("###")[0])));
                        modelo.setRecargo(fn.castBigDecimal(fn.preparaPrimas(newCont.toString().split("\n")[i ].split("###")[1])));
                        modelo.setDerecho(fn.castBigDecimal(fn.preparaPrimas(newCont.toString().split("\n")[i].split("###")[2])));
                        modelo.setIva(fn.castBigDecimal(fn.preparaPrimas(newCont.toString().split("\n")[i ].split("###")[4])));
                        modelo.setPrimaTotal(fn.castBigDecimal(fn.preparaPrimas(newCont.toString().split("\n")[i].split("###")[5])));
					}
				}
				
			}
			
			return modelo;
			
		} catch (Exception ex) {
			return modelo;
		}
	
	}
	
	private void obtenerDireccion(String[] arrContenido, int index) {
		StringBuilder strbDireccion = new StringBuilder();
		if(arrContenido[index].split("Vigencia")[0].toUpperCase().contains("COL")) {
			strbDireccion.append(arrContenido[index].split("Vigencia")[0]);
		}
		
		if(arrContenido[index+1].contains("C.P") && arrContenido[index+1].contains("las 12hrs.")) {
			strbDireccion.append(" ");
			strbDireccion.append(arrContenido[index+1].split("las 12hrs.")[0]);
		}
		modelo.setCteDireccion(strbDireccion.toString().replace("@@@","").replace("###", "").trim());
		
	}
}
