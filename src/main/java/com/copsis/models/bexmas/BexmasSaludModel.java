package com.copsis.models.bexmas;

import java.util.ArrayList;
import java.util.List;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraAseguradosModel;
import com.copsis.models.EstructuraCoberturasModel;
import com.copsis.models.EstructuraJsonModel;
import com.copsis.models.EstructuraRecibosModel;


public class BexmasSaludModel {
	// Clases
	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
	// Varaibles
	private String contenido = "";
	
	public  BexmasSaludModel(String contenido) {
		this.contenido = contenido;
	}
	
	
	public EstructuraJsonModel procesar() {
		int inicio = 0;
		int fin = 0;
		boolean cp= true;
		StringBuilder newcont = new StringBuilder();
		StringBuilder newcoberturas = new StringBuilder();
		StringBuilder newcob = new StringBuilder();
		contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());
		
		try {
			// tipo
			modelo.setTipo(3);
			// cia
			modelo.setCia(98);
			
	
			inicio = contenido.indexOf("Nombre del Contratante:");
			fin = contenido.indexOf("Datos de los Asegurados");
			
			if(inicio >  -1 && fin > -1  &&  inicio < fin) {
				newcont.append(contenido.substring(inicio,fin).replace("@@@", "").replace("\r", "").replace("12:00 HORAS", "").replace("ANUAL", "CONTADO"));
				
				for (int i = 0; i < newcont.toString().split("\n").length; i++) {
					
					if(newcont.toString().split("\n")[i].contains("Contratante") && newcont.toString().split("\n")[i].contains("Póliza Inicial")) {
						modelo.setCteNombre(newcont.toString().split("\n")[i+1].split("###")[0]);
						modelo.setPoliza(newcont.toString().split("\n")[i+1].split("###")[newcont.toString().split("\n")[i+1].split("###").length-1]);
					}
					if(modelo.getPoliza().length() == 0  && newcont.toString().split("\n")[i].contains("Póliza Inicial:")) {
						modelo.setPoliza(newcont.toString().split("\n")[i].split("Póliza Inicial:")[1].replace("###", "").trim());
					}
					if(modelo.getCteNombre().length() == 0 && newcont.toString().split("\n")[i].contains("Contratante") ){
						modelo.setCteNombre(newcont.toString().split("\n")[i+1].replace("###", "").trim());
					}
					
					if(newcont.toString().split("\n")[i].contains("Moneda") && newcont.toString().split("\n")[i].contains("Forma de Pago")  && newcont.toString().split("\n")[i+1].contains("Dirección") && cp) {						
						modelo.setFormaPago(fn.formaPagoSring(newcont.toString().split("\n")[i+1]));
						modelo.setMoneda(fn.buscaMonedaEnTexto(newcont.toString().split("\n")[i+1]));
						cp = false;
					}
					
					if(newcont.toString().split("\n")[i].contains("Moneda") && newcont.toString().split("\n")[i].contains("Forma de Pago") && cp) {
						modelo.setFormaPago(fn.formaPagoSring(newcont.toString().split("\n")[i]));
						modelo.setMoneda(fn.buscaMonedaEnTexto(newcont.toString().split("\n")[i]));
						cp = false;
					}
					
					
					if(newcont.toString().split("\n")[i].contains("Dirección")) {
						modelo.setCteDireccion((newcont.toString().split("\n")[i+1].length() > 1  ? newcont.toString().split("\n")[i+1].split("###")[0] : newcont.toString().split("\n")[i+1]) 
						 +" " + newcont.toString().split("\n")[i+2]);
					}
					if(newcont.toString().split("\n")[i].contains("Desde") && newcont.toString().split("\n")[i].contains("Hasta") && newcont.toString().split("\n")[i+1].contains("-")) {											
						modelo.setVigenciaDe(fn.formatDateMonthCadena(newcont.toString().split("\n")[i+1].split("###")[0].trim()));
						modelo.setVigenciaA(fn.formatDateMonthCadena(newcont.toString().split("\n")[i+1].split("###")[1].trim()));
						modelo.setFechaEmision(modelo.getVigenciaDe());
					}
					if(newcont.toString().split("\n")[i].contains("C.P:")) {
						modelo.setCp(newcont.toString().split("\n")[i].split("C.P:")[1].split("###")[1] );
					}
					if(newcont.toString().split("\n")[i].contains("R.F.C:")) {
						modelo.setRfc(newcont.toString().split("\n")[i].split("R.F.C:")[1].split("###")[1] );
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

						if(newcont.toString().split("\n")[i].split("###")[0].length() > 20) {
							asegurado.setNombre(newcont.toString().split("\n")[i].split("###")[0]);
						}else {
							asegurado.setNombre(newcont.toString().split("\n")[i-1]  +" " +newcont.toString().split("\n")[i].split("###")[0]);
						}
						asegurado.setNacimiento(fn.formatDateMonthCadena(newcont.toString().split("\n")[i].split("###")[2]));
						asegurado.setAntiguedad(fn.formatDateMonthCadena(newcont.toString().split("\n")[i].split("###")[3]));
						asegurado.setParentesco(fn.parentesco(newcont.toString().split("\n")[i].split("###")[4]));
						asegurado.setEdad(fn.castInteger(newcont.toString().split("\n")[i].split("###")[5]));
						asegurado.setSexo(fn.sexo(newcont.toString().split("\n")[i].split("###")[6])  ? 1 : 0);
						asegurados.add(asegurado);
					}								
				}
				
				modelo.setAsegurados(asegurados);
			}
			
			
			
			inicio = contenido.indexOf("Detalle del Seguro");
			fin = contenido.indexOf("Cobertura Básica");
			if(inicio >  -1 && fin > -1  &&  inicio < fin) {
				newcont = new StringBuilder();
				newcont.append(contenido.substring(inicio,fin).replace("@@@", "").replace("\r", ""));
				for (int i = 0; i < newcont.toString().split("\n").length; i++) {
					
					if(newcont.toString().split("\n")[i].contains("%")) {
						modelo.setSa(newcont.toString().split("\n")[i].split("###")[1]);
						modelo.setDeducible(newcont.toString().split("\n")[i].split("###")[2]);
						modelo.setCoaseguro(newcont.toString().split("\n")[i].split("###")[3]);			
					}
				}
			}
			

			inicio = contenido.indexOf("Cobertura Básica");
			fin = contenido.indexOf("En testimonio");
			if(inicio >  -1 && fin > -1  &&  inicio < fin) {
				newcont = new StringBuilder();
				newcont.append(contenido.substring(inicio,fin).replace("@@@", "").replace("\r", ""));
		
			}
			
	
			
			inicio = contenido.indexOf("Coberturas Adicionales");
			fin = contenido.lastIndexOf("En testimonio de");
			if(inicio >  -1 && fin > -1  &&  inicio < fin) {
				newcoberturas = new StringBuilder();
				newcoberturas.append(contenido.substring(inicio,fin).replace("@@@", "").replace("\r", "").split("En testimonio de")[0]);
		
			}
			
			newcob.append(newcont +" "+ newcoberturas);
			
			List<EstructuraCoberturasModel> coberturas = new ArrayList<>();
			if(newcob.toString().length() > 50) {
				for (int i = 0; i < newcob.toString().split("\n").length; i++) {
				
					if(!newcob.toString().split("\n")[i].contains("Cobertura Básica") && !newcob.toString().split("\n")[i].contains("Coberturas Adicionales")
							&& !newcob.toString().split("\n")[i].contains("coaseguro###")
							) {		
						EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();
						switch (newcob.toString().split("\n")[i].split("###").length) {
						case 2:
							cobertura.setNombre(newcob.toString().split("\n")[i].split("###")[0]);						
							coberturas.add(cobertura);
							break;
						case 3:
							cobertura.setNombre(newcob.toString().split("\n")[i].split("###")[0]);
							cobertura.setSa(newcob.toString().split("\n")[i].split("###")[2]);		
							coberturas.add(cobertura);
							break;								
						default:
							break;
						}											
					}
				}
				modelo.setCoberturas(coberturas);
			}
			
			
			
			
			List<EstructuraRecibosModel> recibos = new ArrayList<>();
			EstructuraRecibosModel recibo = new EstructuraRecibosModel();
			if(modelo.getFormaPago() == 1) {
				recibo.setReciboId("");
				recibo.setSerie("1/1");
				recibo.setVigenciaDe(modelo.getVigenciaDe());
				recibo.setVigenciaA(modelo.getVigenciaA());
				if (recibo.getVigenciaDe().length() > 0) {
					recibo.setVencimiento(fn.dateAdd(recibo.getVigenciaDe(), 30, 1));
				}
				recibo.setPrimaneta(modelo.getPrimaneta());
				recibo.setDerecho(modelo.getDerecho());
				recibo.setRecargo(modelo.getRecargo());
				recibo.setIva(modelo.getDerecho());

				recibo.setPrimaTotal(modelo.getPrimaTotal());
				recibo.setAjusteUno(modelo.getAjusteUno());
				recibo.setAjusteDos(modelo.getAjusteDos());
				recibo.setCargoExtra(modelo.getCargoExtra());
				recibos.add(recibo);

			}
		
			inicio = contenido.indexOf("Prima Neta");
			fin = contenido.indexOf("La presente carátula");
			if(inicio >  -1 && fin > -1  &&  inicio < fin) {
				newcont = new StringBuilder();
				newcont.append(contenido.substring(inicio,fin).replace("@@@", "").replace("\r", ""));
				
				for (int i = 0; i < newcont.toString().split("\n").length; i++) {
				
					if(newcont.toString().split("\n")[i].contains("Prima Neta")) {
						 modelo.setPrimaneta(fn.castBigDecimal( fn.castDouble(newcont.toString().split("\n")[i].split("###")[1])));
					}
					if(newcont.toString().split("\n")[i].contains("Fraccionado")) {
						 modelo.setRecargo(fn.castBigDecimal( fn.castDouble(newcont.toString().split("\n")[i].split("###")[1])));
					}
					if(newcont.toString().split("\n")[i].contains("Expedición")) {
						 modelo.setDerecho(fn.castBigDecimal( fn.castDouble(newcont.toString().split("\n")[i].split("###")[1])));
					}
                    if(newcont.toString().split("\n")[i].contains("I.V.A.")) {
                    	 modelo.setIva(fn.castBigDecimal( fn.castDouble(newcont.toString().split("\n")[i].split("###")[1])));
					}
                    if(newcont.toString().split("\n")[i].contains("Prima Total")) {
                    	 modelo.setPrimaTotal(fn.castBigDecimal( fn.castDouble(newcont.toString().split("\n")[i].split("###")[1])));
					}
                    if(newcont.toString().split("\n")[i].contains("Agente")) {
						modelo.setCveAgente( newcont.toString().split("\n")[i].split("###")[1]);
					}
				}
			}
			
			
			
			return modelo;
		} catch (Exception ex) {
			modelo.setError(BexmasSaludModel.this.getClass().getTypeName() + " - catch:" + ex.getMessage() + " | "
					+ ex.getCause());
			return modelo;
		}
	}


}
