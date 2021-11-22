package com.copsis.models.axa;

import java.util.ArrayList;
import java.util.List;

import com.copsis.constants.ConstantsValue;
import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraCoberturasModel;
import com.copsis.models.EstructuraJsonModel;

public class AxaDiversos2Model {
	// Clases
	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
	private String contenido = "";
	
	
	public AxaDiversos2Model(String contenidox) {
	 this.contenido = fn.remplazarMultiple(contenidox, fn.remplazosGenerales());
	 contenido = contenido.replace("C o b e r t u r a s", "Coberturas");
	}

	public EstructuraJsonModel procesar() {
		String newcontenido = "";
		int inicio = 0;
		int fin = 0;
		//Responsabilidad Civil, Comercio
	
		try {
			modelo.setTipo(7);
			modelo.setCia(20);
			

			inicio = contenido.indexOf("Datos de la Póliza");
			fin = contenido.indexOf("Datos Adicionales");
			if(inicio > -1 && fin > -1 && inicio < fin) {
				newcontenido = contenido.substring(inicio,fin).replace("@@@", "").replace("\r", "");				
				for (int i = 0; i < newcontenido.split("\n").length; i++) {	
		
					if(newcontenido.split("\n")[i].contains("Póliza No.") && newcontenido.split("\n")[i].contains("Ramo")) {
						modelo.setPoliza(newcontenido.split("\n")[i].split("Póliza No.")[1].split("Ramo")[0].replace("###", "").trim());
						
					}
					if(newcontenido.split("\n")[i].contains("Fecha de Emisión") && newcontenido.split("\n")[i].contains("Expediente")) {
						modelo.setFechaEmision(fn.formatDateMonthCadena( newcontenido.split("\n")[i].split("Emisión")[1].split("Expediente")[0].replace("###", "").trim()));
					}
					if(newcontenido.split("\n")[i].contains("Vigencia") ) {
						String x = newcontenido.split("\n")[i+1].replace("a las 12 Hrs.", "");
						modelo.setVigenciaDe(fn.formatDateMonthCadena(x.split("###")[1].trim()));
						modelo.setVigenciaA(fn.formatDateMonthCadena(x.split("###")[3].trim()));
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
			

			inicio = contenido.indexOf("Datos Adicionales");
			fin = contenido.indexOf("Suma Asegurada Prima Neta");
			if(inicio > -1 && fin > -1 && inicio < fin) {

				newcontenido = contenido.substring(inicio,fin).replace("@@@", "").replace("\r", "").replace("\u00a0", "");				
				for (int i = 0; i < newcontenido.split("\n").length; i++) {
					
					if(newcontenido.split("\n")[i].contains("Moneda") && newcontenido.split("\n")[i].contains("Suma Asegurada")) {
						modelo.setMoneda(fn.moneda(newcontenido.split("\n")[i].split("Moneda")[1].split("Suma Asegurada")[0].replace("###", "").trim()));	
					}
					if(newcontenido.split("\n")[i].contains("Forma de Pago") && newcontenido.split("\n")[i].contains(ConstantsValue.PRIMA_NETA)) {
						modelo.setFormaPago(fn.formaPago(newcontenido.split("\n")[i].split("Forma de Pago")[1].split(ConstantsValue.PRIMA_NETA)[0].replace("###", "").trim()));
						
					}
                    if(newcontenido.split("\n")[i].contains("Nombre del Agente")) {
                    	modelo.setAgente(newcontenido.split("\n")[i].split("Nombre del Agente")[1].replace("###", "").trim());

					}
                    if(newcontenido.split("\n")[i].contains("Número de Agente")&& newcontenido.split("\n")[i].contains(ConstantsValue.GASTOS_EXPEDICION)  ) {
                    	modelo.setCveAgente(newcontenido.split("\n")[i].split("Número de Agente")[1].split(ConstantsValue.GASTOS_EXPEDICION)[0].replace("###", "").trim());
                       
					}
                    if(newcontenido.split("\n")[i].contains("I.V.A.")) {
                    	modelo.setIva(fn.castBigDecimal(fn.cleanString(newcontenido.split("\n")[i].split("I.V.A.")[1].split("###")[1].trim())));
					
					}
                    if(newcontenido.split("\n")[i].contains("Prima Neta")) {
                    	modelo.setPrimaneta(fn.castBigDecimal(fn.cleanString(newcontenido.split("\n")[i].split("Prima Neta")[1].split("###")[1].trim())));
					
                    }
                    if(newcontenido.split("\n")[i].contains(ConstantsValue.GASTOS_EXPEDICION)) {
                    	modelo.setDerecho(fn.castBigDecimal(fn.cleanString(newcontenido.split("\n")[i].split(ConstantsValue.GASTOS_EXPEDICION)[1].split("###")[1].trim())));
					
					}
                    if(newcontenido.split("\n")[i].contains("Prima Total")) {
                    	modelo.setPrimaTotal(fn.castBigDecimal(fn.cleanString(newcontenido.split("\n")[i].split("Prima Total")[1].replace("###","").trim())));
                   
					}
				}
			}
			
			

			
			/*Proceoso para las  coberturas*/
			inicio = contenido.indexOf("Coberturas");
			fin = contenido.indexOf("Giro del Negocio");
			if(inicio > -1 && fin > -1 && inicio < fin) {
				List<EstructuraCoberturasModel> coberturas = new ArrayList<>();				
				newcontenido = contenido.substring(inicio,fin);				
				for (int i = 0; i < newcontenido.split("\n").length; i++) {
					EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();
				    int x = newcontenido.split("\n")[i].split("###").length;
				    if(newcontenido.split("\n")[i].length() > 20) {
				    	if(x == 2) {
					    	cobertura.setNombre(newcontenido.split("\n")[i].split("###")[0]);
					    	cobertura.setSa(newcontenido.split("\n")[i].split("###")[1].replace("\r", ""));
					    	coberturas.add(cobertura);				
					    }
					    if(x == 3) {
					    	cobertura.setNombre(newcontenido.split("\n")[i].split("###")[0]);
					    	cobertura.setSa(newcontenido.split("\n")[i].split("###")[1]);					    
					    	coberturas.add(cobertura);
					    }
					    if(x == 4) {
					    	cobertura.setNombre(newcontenido.split("\n")[i].split("###")[0]);
					    	cobertura.setSa(newcontenido.split("\n")[i].split("###")[1]);
					    	cobertura.setDeducible(newcontenido.split("\n")[i].split("###")[2].replace("\r", ""));
					    	coberturas.add(cobertura);
					    }
				    }
				    
				}
				modelo.setCoberturas(coberturas);
			}
		
			
			
			
			return modelo;
		} catch (Exception ex) {
			modelo.setError(
					AxaDiversos2Model.this.getClass().getTypeName() + " | " + ex.getMessage() + " | " + ex.getCause());
			return modelo;
	
		}
		
	}
	

}
