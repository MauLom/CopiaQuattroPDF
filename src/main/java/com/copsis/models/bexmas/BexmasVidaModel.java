package com.copsis.models.bexmas;

import java.util.ArrayList;
import java.util.List;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraAseguradosModel;
import com.copsis.models.EstructuraCoberturasModel;
import com.copsis.models.EstructuraJsonModel;
public class BexmasVidaModel {
	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
	
	
	public EstructuraJsonModel procesar (String contenido ) {
		int inicio =0;
		int fin =0;
		StringBuilder newcontenido = new StringBuilder();
		StringBuilder newdire = new StringBuilder();
		contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());
		try {
			
			modelo.setTipo(5);
			modelo.setCia(98);

			inicio = contenido.indexOf("Contratante");
			fin = contenido.indexOf("Seguros Ve por Más, S. A.");
			
			
			newcontenido.append( fn.extracted(inicio, fin, contenido));
			for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {

				if(newcontenido.toString().split("\n")[i].contains("Contratante") && newcontenido.toString().split("\n")[i].contains("Póliza")) {
					modelo.setPoliza(newcontenido.toString().split("\n")[i].split("Póliza:")[1]);
					modelo.setCteNombre(newcontenido.toString().split("\n")[i+1].split("###")[0]);
				}
				if(newcontenido.toString().split("\n")[i].contains("Moneda") &&  newcontenido.toString().split("\n")[i].contains("Pago") ) {
					modelo.setFormaPago(fn.formaPagoSring(newcontenido.toString().split("\n")[i]));
					if(newcontenido.toString().split("\n")[i].split("Moneda:")[1].split("Forma")[0].contains("M.N.")) {
						modelo.setMoneda(1);
					}else {
						modelo.setMoneda(fn.moneda(newcontenido.toString().split("\n")[i].split("Moneda:")[1].split("Forma")[0]));
					}
					
				} 
				
				if(newcontenido.toString().split("\n")[i].contains("Domicilio") && newcontenido.toString().split("\n")[i].contains("Contratante:") &&  
						newcontenido.toString().split("\n")[i].contains("UEN")  &&  newcontenido.toString().split("\n")[i].contains("Agente") ) {
					newdire.append(newcontenido.toString().split("\n")[i].split("Contratante:")[1].split("UEN")[0] );
					modelo.setCveAgente(newcontenido.toString().split("\n")[i].split("Agente:")[1]);;
				}
				
				if(newcontenido.toString().split("\n")[i].contains("Nombre del Agente:")) {
					newdire.append(newcontenido.toString().split("\n")[i].split("Nombre")[0] );
					modelo.setAgente(newcontenido.toString().split("\n")[i].split("Agente:")[1].replace("###", ""));
				}
				if(newcontenido.toString().split("\n")[i].contains("Estado:") && newcontenido.toString().split("\n")[i].contains("Teléfono:")) {
					newdire.append(newcontenido.toString().split("\n")[i].split("Estado:")[1].split("Teléfono")[0] );
				}
				
				if(newcontenido.toString().split("\n")[i].contains("Plan:") && newcontenido.toString().split("\n")[i].contains("Período")) {
					modelo.setPlan( newcontenido.toString().split("\n")[i].split("Plan:")[1].split("Período")[0].replace("###", ""));
				}
				if(newcontenido.toString().split("\n")[i].contains("Plazo del plan:") && newcontenido.toString().split("\n")[i].contains("Desde")) {
			
				modelo.setVigenciaDe(fn.formatDateMonthCadena(fn.obtenVigePoliza2(newcontenido.toString().split("\n")[i+1]).get(0)));
				modelo.setFechaEmision(fn.formatDateMonthCadena(fn.obtenVigePoliza2(newcontenido.toString().split("\n")[i+1]).get(0)));
				}
				if(newcontenido.toString().split("\n")[i].contains("C.P.")) {
					modelo.setCp(newcontenido.toString().split("\n")[i].split("C.P.")[1].trim().substring(0,5));
				}
				
			}
			
			if(modelo.getFormaPago() == 1) {
				modelo.setVigenciaA(fn.calcvigenciaA(modelo.getVigenciaDe(), 12));
			}
			
			if(newdire.length()> 0) {
				modelo.setCteDireccion(newdire.toString().replace("###"," "));
			}
			

			inicio = contenido.indexOf("Datos del Asegurado");
			fin = contenido.indexOf("Coberturas");
			newcontenido = new StringBuilder();			
			newcontenido.append( fn.extracted(inicio, fin, contenido));
			List<EstructuraAseguradosModel> asegurados = new ArrayList<>();
			for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
				EstructuraAseguradosModel asegurado = new EstructuraAseguradosModel();
				if(newcontenido.toString().split("\n")[i].split("-").length >2) {					
					asegurado.setNombre(newcontenido.toString().split("\n")[i].split("###")[0]);
					asegurado.setNacimiento(fn.formatDateMonthCadena(newcontenido.toString().split("\n")[i].split("###")[1]));
					asegurado.setSexo(fn.sexo(newcontenido.toString().split("\n")[i].split("###")[2]) ? 1:0);
					asegurado.setEdad(fn.castInteger(newcontenido.toString().split("\n")[i].split("###")[3]));
					asegurados.add(asegurado);
				}
			}
			
			modelo.setAsegurados(asegurados);
			

			inicio = contenido.indexOf("Coberturas");
			fin = contenido.indexOf("Designación de Beneficiarios");
			newcontenido = new StringBuilder();			
			newcontenido.append( fn.extracted(inicio, fin, contenido));
			List<EstructuraCoberturasModel> coberturas = new ArrayList<>();
			for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
				if(!newcontenido.toString().split("\n")[i].contains("Coberturas")) {
					EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();					
					switch (newcontenido.toString().split("\n")[i].split("###").length) {
					case 5:
						cobertura.setNombre(newcontenido.toString().split("\n")[i].split("###")[0]);
						cobertura.setSa(newcontenido.toString().split("\n")[i].split("###")[4]);		
						coberturas.add(cobertura);
						break;
					}
				}
			}
			
			modelo.setCoberturas(coberturas);
			

			inicio = contenido.indexOf("Total de Primas");
			fin = contenido.indexOf("En cumplimiento");
			newcontenido = new StringBuilder();			
			newcontenido.append( fn.extracted(inicio, fin, contenido));
			for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
				System.out.println(newcontenido.toString().split("\n")[i]);
				
				if(newcontenido.toString().split("\n")[i].contains("Total de Primas")) {
					 modelo.setPrimaneta(fn.castBigDecimal( fn.castDouble(newcontenido.toString().split("\n")[i].split("###")[1])));
				}
				if(newcontenido.toString().split("\n")[i].contains("Prima Total")) {
                  	 modelo.setPrimaTotal(fn.castBigDecimal( fn.castDouble(newcontenido.toString().split("\n")[i].split("Prima Total")[1].split("###")[1])));
				}
			}
			
			return modelo;
		} catch (Exception ex) {
			modelo.setError(BexmasVidaModel.this.getClass().getTypeName() + " - catch:" + ex.getMessage() + " | "
					+ ex.getCause());
			return modelo;
		}
		
	}
}
