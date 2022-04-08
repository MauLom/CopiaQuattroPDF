package com.copsis.models.latino;

import java.util.ArrayList;
import java.util.List;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraCoberturasModel;
import com.copsis.models.EstructuraJsonModel;
import com.copsis.models.planSeguro.PlanSeguroSaludModel;

public class LatinoSeguroAutoModel {
	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();	
	private String contenido = "";
	
    public  LatinoSeguroAutoModel(String contenido){
    	this.contenido = contenido;
    }
    
    public EstructuraJsonModel procesar() {
    	int inicio = 0;
		int fin = 0;
		StringBuilder newcontenido = new StringBuilder();
		String direccion = "";
		contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());
		
		try {
			modelo.setTipo(1);
			modelo.setCia(21);
			inicio = contenido.indexOf("DATOS DE LA PÓLIZA");
			fin = contenido.indexOf("DESCRIPCIÓN VEHÍCULO");
			
			newcontenido.append( fn.extracted(inicio, fin, contenido).replace("día-mes-año: ", ""));
			
			for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
		
				if(newcontenido.toString().split("\n")[i].contains("Vigencia") && fn.obtenVigePoliza( newcontenido.toString().split("\n")[i]).size() == 2) {
					
					modelo.setVigenciaDe(fn.formatDateMonthCadena(fn.obtenVigePoliza( newcontenido.toString().split("\n")[i]).get(0)));
					modelo.setVigenciaA(fn.formatDateMonthCadena(fn.obtenVigePoliza( newcontenido.toString().split("\n")[i]).get(1)));
				}
				
				if(newcontenido.toString().split("\n")[i].contains("Póliza:") && newcontenido.toString().split("\n")[i].contains("Inciso:")  && newcontenido.toString().split("\n")[i].contains("Endoso:")){
					modelo.setPoliza(newcontenido.toString().split("\n")[i].split("Póliza:")[1].split("Inciso:")[0].replace("###", "").trim());
					modelo.setInciso(Integer.parseInt(newcontenido.toString().split("\n")[i].split("Inciso:")[1].split("Endoso:")[0].replace("###", "").trim()));
				}
				
				if(newcontenido.toString().split("\n")[i].contains("CONTRATANTE") && newcontenido.toString().split("\n")[i+1].contains("Nombre:")){
					modelo.setCteNombre(newcontenido.toString().split("\n")[i+1].split("Nombre:")[1].trim());
				}
				if(newcontenido.toString().split("\n")[i].contains("Calle y número:")) {
					direccion =  newcontenido.toString().split("\n")[i].split("Calle y número:")[1].replace("###", " ").trim();
				}
				if(newcontenido.toString().split("\n")[i].contains("Población")) {
					direccion += " "+  newcontenido.toString().split("\n")[i].split("Población:")[1].replace("###", " ").trim();
					modelo.setCteDireccion(direccion);
				}
				if(newcontenido.toString().split("\n")[i].contains("Código Postal:")) {
					modelo.setCp(newcontenido.toString().split("\n")[i].split("Código Postal:")[1].replace("###", " ").trim());
				}
				if(newcontenido.toString().split("\n")[i].contains("R.F.C:") && newcontenido.toString().split("\n")[i].contains("Número")) {
					modelo.setRfc(newcontenido.toString().split("\n")[i].split("R.F.C:")[1].split("Número")[0].replace("###", " ").trim());
				}
				
			}

			
			inicio = contenido.indexOf("DESCRIPCIÓN VEHÍCULO");
			fin = contenido.indexOf("COBERTURA AMPLIA");
			newcontenido = new StringBuilder();
			newcontenido.append( fn.extracted(inicio, fin, contenido));
			
			for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
		
				if(newcontenido.toString().split("\n")[i].contains("Marca:") && newcontenido.toString().split("\n")[i].contains("Descripción") ) {
					modelo.setMarca(newcontenido.toString().split("\n")[i].split("Marca:")[1].split("Descripción")[0].replace("###", " ").trim());
					modelo.setDescripcion(newcontenido.toString().split("\n")[i].split("Descripción:")[1].replace("###", " ").trim());
				}
				if(newcontenido.toString().split("\n")[i].contains("Clave:") && newcontenido.toString().split("\n")[i].contains("Modelo")
						&& newcontenido.toString().split("\n")[i].contains("Ocupantes:")
				 && newcontenido.toString().split("\n")[i].contains("Motor:") && newcontenido.toString().split("\n")[i].contains("Placas")) {
					modelo.setClave(newcontenido.toString().split("\n")[i].split("Clave:")[1].split("Modelo:")[0].replace("###", "").trim());
					modelo.setModelo( fn.castInteger(newcontenido.toString().split("\n")[i].split("Modelo:")[1].split("Ocupantes:")[0].replace("###", "").trim()));
					modelo.setMotor(newcontenido.toString().split("\n")[i].split("Motor:")[1].split("Placas:")[0].replace("###", "").trim());
					modelo.setPlacas(newcontenido.toString().split("\n")[i].split("Placas:")[1].replace("###", "").trim());
				}
				if(newcontenido.toString().split("\n")[i].contains("Serie:") && newcontenido.toString().split("\n")[i].contains("Tipo:")) {
					modelo.setSerie(newcontenido.toString().split("\n")[i].split("Serie:")[1].split("Tipo:")[0].replace("###", "").trim());
				}
			}
			

			inicio = contenido.indexOf("COBERTURA AMPLIA");
			fin = contenido.indexOf("PRIMA DEL SEGURO");
			newcontenido = new StringBuilder();
			newcontenido.append( fn.extracted(inicio, fin, contenido));
        	List<EstructuraCoberturasModel> coberturas = new ArrayList<>();
			for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
				EstructuraCoberturasModel  cobertura  = new EstructuraCoberturasModel();				
				if(!newcontenido.toString().split("\n")[i].contains("COBERTURA") && !newcontenido.toString().split("\n")[i].contains("Coberturas")) {					
					int sp = newcontenido.toString().split("\n")[i].split("###").length;
					  if( sp == 3 || sp == 4) {
					       cobertura.setNombre(newcontenido.toString().split("\n")[i].split("###")[0].trim());
	                       cobertura.setSa(newcontenido.toString().split("\n")[i].split("###")[1].trim());
	                       if(sp == 4) {
	                       cobertura.setDeducible(newcontenido.toString().split("\n")[i].split("###")[2].trim());
	                       }
	                       coberturas.add(cobertura);
					  }
				}
			}
			modelo.setCoberturas(coberturas);
					
			inicio = contenido.indexOf("PRIMA DEL SEGURO");
			fin = contenido.indexOf("Clave Promotor");
			newcontenido = new StringBuilder();
			newcontenido.append( fn.extracted(inicio, fin, contenido));
			
			for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {

				if(newcontenido.toString().split("\n")[i].contains("Fecha de emisión:") && newcontenido.toString().split("\n")[i].contains("Prima Neta:")
					&& newcontenido.toString().split("\n")[i].contains("I.V.A:")	) {					
					modelo.setFechaEmision(fn.formatDateMonthCadena(fn.obtenVigePoliza(newcontenido.toString().split("\n")[i]).get(0)));
					modelo.setPrimaneta(fn.castBigDecimal(fn.preparaPrimas(newcontenido.toString().split("\n")[i].split("Prima Neta:")[1].split("I.V.A:")[0].replace("###", "").trim())));
					modelo.setIva(fn.castBigDecimal(fn.preparaPrimas(newcontenido.toString().split("\n")[i].split("I.V.A:")[1].replace("###", "").trim())));
				}
				if(newcontenido.toString().split("\n")[i].contains("Moneda:") && newcontenido.toString().split("\n")[i].contains("Fraccionado:")
						&& newcontenido.toString().split("\n")[i].contains("Artículo")) {										
						modelo.setRecargo(fn.castBigDecimal(fn.preparaPrimas(newcontenido.toString().split("\n")[i].split("Fraccionado:")[1].split("Artículo")[0].replace("###", "").trim())));
					   modelo.setMoneda(1);
					}
				
				if(newcontenido.toString().split("\n")[i].contains("pago:") && newcontenido.toString().split("\n")[i].contains("Expedición:")
						&& newcontenido.toString().split("\n")[i].contains("I.V.A.")) {										
						modelo.setDerecho(fn.castBigDecimal(fn.preparaPrimas(newcontenido.toString().split("\n")[i].split("Expedición:")[1].split("I.V.A.")[0].replace("###", "").trim())));
					   modelo.setFormaPago(fn.formaPagoSring(newcontenido.toString().split("\n")[i]));
					}
				
				if(newcontenido.toString().split("\n")[i].contains("Primer Recibo:") && newcontenido.toString().split("\n")[i].contains("Total a pagar:")) {										
						modelo.setPrimerPrimatotal(fn.castBigDecimal(fn.preparaPrimas(newcontenido.toString().split("\n")[i].split("Recibo:")[1].split("Sub Total:")[0].replace("###", "").trim())));
						modelo.setPrimaTotal(fn.castBigDecimal(fn.preparaPrimas(newcontenido.toString().split("\n")[i].split("Total a pagar:")[1].replace("###", "").trim())));
						
					}
				
				if(newcontenido.toString().split("\n")[i].contains("Agente") && newcontenido.toString().split("\n")[i].contains("Clave")) {
					modelo.setAgente(newcontenido.toString().split("\n")[i].split("Agente")[1].split("Clave")[0].replace("###", "").trim());
					modelo.setCveAgente(newcontenido.toString().split("\n")[i].split("Clave")[1].replace("###", "").trim());
				}
			}
			
			
			
			
			
			return modelo;
		} catch (Exception ex) {
			modelo.setError(LatinoSeguroAutoModel.this.getClass().getTypeName() + " | " + ex.getMessage() + " | " + ex.getCause());
           return modelo;
		}
    	
    }
    
}
