package com.copsis.models.hdi;

import java.util.ArrayList;
import java.util.List;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraCoberturasModel;
import com.copsis.models.EstructuraJsonModel;
import com.copsis.models.EstructuraRecibosModel;

public class HdiAutosBModel {
	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();

	public EstructuraJsonModel procesar(String  contenido) {
		int inicio = 0;
		int fin = 0;
		StringBuilder newcontenido = new StringBuilder();
		contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());
		
		try {
			
			modelo.setTipo(1);
			modelo.setCia(14);

			inicio = contenido.indexOf("Ramo:");
			fin = contenido.indexOf("Descripción###Límite de Responsabilidad");
			newcontenido.append( fn.extracted(inicio, fin, contenido));
			
			for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {			
				if(newcontenido.toString().split("\n")[i].contains("responsabilidad máxima.")){
					modelo.setCteNombre(newcontenido.toString().split("\n")[i+1]);
					modelo.setCteDireccion(newcontenido.toString().split("\n")[i+2]);				
					if(fn.obtenerListNumeros2(modelo.getCteDireccion()).get(1).length()  == 5) {
						modelo.setCp(fn.obtenerListNumeros2(modelo.getCteDireccion()).get(1));
					}
				 }
				if(newcontenido.toString().split("\n")[i].contains("Póliza:") && newcontenido.toString().split("\n")[i].contains("Licencia:") && newcontenido.toString().split("\n")[i].contains("expedición de licencia")) {					
					modelo.setPoliza(newcontenido.toString().split("\n")[i].split("Póliza:")[1].split("Licencia:")[0].replace("###", "").trim());
				}
				if(newcontenido.toString().split("\n")[i].contains("Agente:") && newcontenido.toString().split("\n")[i].contains("Vigencia")) {			
					modelo.setCveAgente(fn.obtenerListNumeros2(newcontenido.toString().split("\n")[i].split("Agente:")[1].split("Vigencia:")[0].replace("###","").trim()).get(0));
					if(modelo.getCveAgente().length() >  0) {
						modelo.setAgente(newcontenido.toString().split("\n")[i].split(modelo.getCveAgente())[1].split("Vigencia:")[0].replace("###", "").trim());
					}					
					String d  = newcontenido.toString().split("\n")[i].split("Vigencia:")[1].split("al")[0].replace(",", "").trim().replace(" ","-");
					modelo.setVigenciaDe(fn.formatDateMonthCadena( d.split("-")[1] +"-"+ d.split("-")[0] +"-"+ d.split("-")[2]));
					
					String a  = newcontenido.toString().split("\n")[i].split("al")[1].replace(",", "").trim().replace(" ","-");
					modelo.setVigenciaA(fn.formatDateMonthCadena( a.split("-")[1] +"-"+ a.split("-")[0] +"-"+ a.split("-")[2]));		
				}
				if(newcontenido.toString().split("\n")[i].contains("RFC:") && newcontenido.toString().split("\n")[i].contains("Fecha de Emisión:")) {	
					modelo.setRfc(newcontenido.toString().split("\n")[i].split("RFC:")[1].split("Fecha")[0].replace("###", "").trim());
				}
				if(newcontenido.toString().split("\n")[i].contains("Serie:") && newcontenido.toString().split("\n")[i].contains("Placas:") && newcontenido.toString().split("\n")[i].contains("Remolque:")) {	
					modelo.setSerie(newcontenido.toString().split("\n")[i].split("Serie:")[1].split("Placas")[0].replace("###", "").trim());	
					modelo.setPlacas(newcontenido.toString().split("\n")[i].split("Placas:")[1].split("Remolque")[0].replace("###", "").trim());	
				}
				if(newcontenido.toString().split("\n")[i].contains("Paquete:")) {
					modelo.setPlan( newcontenido.toString().split("\n")[i].split("Paquete:")[1].split("###")[0].trim());
				}

			}
			
			modelo.setFormaPago(1);
			modelo.setMoneda(1);
			
			if(modelo.getVigenciaDe().length() > 0) {
				modelo.setFechaEmision(modelo.getVigenciaDe());
			}
			
			
			inicio  = contenido.indexOf("Descripción###Límite de Responsabilidad");
			fin = contenido.indexOf("Prima Neta");
		
			newcontenido = new StringBuilder(); 
			newcontenido.append( fn.extracted(inicio, fin, contenido));
			List<EstructuraCoberturasModel> coberturas = new ArrayList<>();
		
			
			for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {		
				EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();
				if(!newcontenido.toString().split("\n")[i].contains("Descripción")) {
				
					int sp =newcontenido.toString().split("\n")[i].split("###").length;
					switch(sp) {
					case  2:
						cobertura.setNombre(newcontenido.toString().split("\n")[i].split("###")[0]);
						cobertura.setSa(newcontenido.toString().split("\n")[i].split("###")[1]);
						coberturas.add(cobertura);
						break;
					case  3:
						cobertura.setNombre(newcontenido.toString().split("\n")[i].split("###")[0]);
						cobertura.setSa(newcontenido.toString().split("\n")[i].split("###")[1]);
						cobertura.setDeducible(newcontenido.toString().split("\n")[i].split("###")[2]);
						coberturas.add(cobertura);
						break;
					}
				}
			}
			
			modelo.setCoberturas(coberturas);
			
			inicio = contenido.indexOf("Prima Neta");
			fin  = contenido.indexOf("Póliza Extranjera");
		
		
			newcontenido = new StringBuilder(); 
			newcontenido.append( fn.extracted(inicio, fin, contenido));
			for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {		
				if(newcontenido.toString().split("\n")[i].contains("Prima Neta")) {
					List<String> valores = fn.obtenerListNumeros(newcontenido.toString().split("\n")[i+1]);
            		 modelo.setPrimaneta(fn.castBigDecimal(fn.castDouble(valores.get(0))));
					 modelo.setDerecho(fn.castBigDecimal(fn.castDouble(valores.get(1))));
					 modelo.setIva(fn.castBigDecimal(fn.castDouble(valores.get(2))));
					 modelo.setPrimaTotal(fn.castBigDecimal(fn.castDouble(valores.get(3))));
				}
			}
	
	
			if(modelo.getFormaPago() == 1 && (fn.diferenciaDias(modelo.getVigenciaDe(), modelo.getVigenciaA()) <30)) {
				List<EstructuraRecibosModel> recibos = new ArrayList<>();
				EstructuraRecibosModel recibo = new EstructuraRecibosModel();
				recibo.setReciboId("");
				recibo.setSerie("1/1");
				recibo.setVigenciaDe(modelo.getVigenciaDe());
				recibo.setVigenciaA(modelo.getVigenciaA());				
			    recibo.setVencimiento(modelo.getVigenciaDe());				
				recibo.setPrimaneta(fn.castBigDecimal(modelo.getPrimaneta(), 2));
				recibo.setDerecho(fn.castBigDecimal(modelo.getDerecho(), 2));
				recibo.setRecargo(fn.castBigDecimal(modelo.getRecargo(), 2));
				recibo.setIva(fn.castBigDecimal(modelo.getDerecho(), 2));
				recibo.setPrimaTotal(fn.castBigDecimal(modelo.getPrimaTotal(), 2));
				recibo.setAjusteUno(fn.castBigDecimal(modelo.getAjusteUno(), 2));
				recibo.setAjusteDos(fn.castBigDecimal(modelo.getAjusteDos(), 2));
				recibo.setCargoExtra(fn.castBigDecimal(modelo.getCargoExtra(), 2));
				recibos.add(recibo);
				modelo.setRecibos(recibos);
			}
	
			return modelo;
		} catch (Exception ex) {
			modelo.setError(HdiAutosBModel.this.getClass().getTypeName() + " | " + ex.getMessage() + " | " + ex.getCause());
			 return modelo;
		}

	}

}
