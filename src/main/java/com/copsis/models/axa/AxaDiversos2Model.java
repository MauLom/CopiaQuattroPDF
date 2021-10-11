package com.copsis.models.axa;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraJsonModel;

public class AxaDiversos2Model {
	// Clases
	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
	private static String contenido = "";
	private static String newcontenido = "";
	
	private static  String textbusq = "";
	private int inicio = 0;
	private int fin = 0;

	
	public AxaDiversos2Model(String contenido) {
	 this.contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());
	}

	public EstructuraJsonModel procesar() {
		//modelo.setTipo(7);
		//Responsabilidad Civil, Comercio
	
		try {
			modelo.setTipo(712);
			modelo.setCia(20);
			
//		   System.out.println(contenido);
			inicio = contenido.indexOf("Datos de la Póliza");
			fin = contenido.indexOf("Datos Adicionales");
			if(inicio > -1 && fin > -1 && inicio < fin) {
				newcontenido = contenido.substring(inicio,fin).replace("@@@", "").replace("\r", "");				
				for (int i = 0; i < newcontenido.split("\n").length; i++) {	
		
					if(newcontenido.split("\n")[i].contains("Póliza No.") && newcontenido.split("\n")[i].contains("Ramo")) {
						modelo.setPoliza(newcontenido.split("\n")[i].split("Póliza No.")[1].split("Ramo")[0].replace("###", "").trim());
						
					}
					if(newcontenido.split("\n")[i].contains("Fecha de Emisión") && newcontenido.split("\n")[i].contains("Expediente")) {
						modelo.setFechaEmision(fn.formatDate_MonthCadena( newcontenido.split("\n")[i].split("Emisión")[1].split("Expediente")[0].replace("###", "").trim()));
					}
					if(newcontenido.split("\n")[i].contains("Vigencia") ) {
						String x = newcontenido.split("\n")[i+1].replace("a las 12 Hrs.", "");
						modelo.setVigenciaDe(fn.formatDate_MonthCadena(x.split("###")[1].trim()));
						modelo.setVigenciaA(fn.formatDate_MonthCadena(x.split("###")[3].trim()));
					}
					
					if(newcontenido.split("\n")[i].contains("Datos Generales del Asegurado") && newcontenido.split("\n")[i+1].contains("RFC:") ) {
						modelo.setCteNombre(newcontenido.split("\n")[i+1].split("Nombre")[1].split("RFC:")[0].replace(":", "").replace("###", "").trim());
						modelo.setRfc(newcontenido.split("\n")[i+1].split("RFC:")[1].trim());
					}
					if(newcontenido.split("\n")[i].contains("Domicilio")) {
						modelo.setCteDireccion(  newcontenido.split("\n")[i].split("Domicilio")[1].replace("###", ""));
					}
					if(newcontenido.split("\n")[i].contains("C.P:") && newcontenido.split("\n")[i].contains("Tel:")) {
						modelo.setCp(  newcontenido.split("\n")[i].split("C.P:")[1].split("Tel:")[0].trim());
					}
					
				}
			}
			
//			   System.out.println(contenido);
			inicio = contenido.indexOf("Datos Adicionales");
			fin = contenido.indexOf("Suma Asegurada Prima Neta");
			if(inicio > -1 && fin > -1 && inicio < fin) {

				newcontenido = contenido.substring(inicio,fin).replace("@@@", "").replace("\r", "").replaceAll("\u00a0", "");				
				for (int i = 0; i < newcontenido.split("\n").length; i++) {
					
					if(newcontenido.split("\n")[i].contains("Moneda") && newcontenido.split("\n")[i].contains("Suma Asegurada")) {
						modelo.setMoneda(fn.moneda(newcontenido.split("\n")[i].split("Moneda")[1].split("Suma Asegurada")[0].replace("###", "").trim()));	
					}
					if(newcontenido.split("\n")[i].contains("Forma de Pago") && newcontenido.split("\n")[i].contains("Prima Neta")) {
						System.out.println("===>"+newcontenido.split("\n")[i].split("Forma de Pago")[1].split("Prima Neta")[0].replace("###", "").trim());
					}
                    if(newcontenido.split("\n")[i].contains("Nombre del Agente")) {
						
					}
                    if(newcontenido.split("\n")[i].contains("Número de Agente ")&& newcontenido.split("\n")[i].contains("Gastos por Expedición")  ) {
						
					}
                    if(newcontenido.split("\n")[i].contains("I.V.A.")) {
						
					}
                    if(newcontenido.split("\n")[i].contains("Prima Total")) {
						
					}
				}
			}
			
			
			
			
			return modelo;
		} catch (Exception ex) {
			modelo.setError(
					AxaDiversos2Model.this.getClass().getTypeName() + " | " + ex.getMessage() + " | " + ex.getCause());
			return modelo;
	
		}
		
	}
	

}
