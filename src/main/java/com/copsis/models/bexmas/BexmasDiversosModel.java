package com.copsis.models.bexmas;

import java.util.ArrayList;
import java.util.List;

import com.copsis.constants.ConstantsValue;
import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraCoberturasModel;
import com.copsis.models.EstructuraJsonModel;

public class BexmasDiversosModel {
	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
	
	public EstructuraJsonModel procesar (String contenido) {
		int inicio = 0;
		int fin = 0;
		StringBuilder newcontenido = new StringBuilder();
		StringBuilder newdirec = new StringBuilder();
		contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());
		contenido = contenido.replace("Formas de Pago","Formas de pago")
		.replace("En ###testimonio", "En testimonio")
		.replace("I.V.A:", ConstantsValue.IVA);
		
		try {
			// tipo
			modelo.setTipo(7);
			// cia
			modelo.setCia(98);

			inicio = contenido.indexOf("CONTRATANTE");
			fin = contenido.indexOf("DESGLOSE DE COBERTURAS");

			newcontenido.append( fn.extracted(inicio, fin, contenido));
			
			for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
			
				if(newcontenido.toString().split("\n")[i].contains(ConstantsValue.POLIZA_ACENT2)) {
					modelo.setPoliza(newcontenido.toString().split("\n")[i].split(ConstantsValue.POLIZA_ACENT2)[1].replace("###", "").trim());
				}
				if(newcontenido.toString().split("\n")[i].contains("Agente") && newcontenido.toString().split("\n")[i].contains("Moneda")) {
					modelo.setCteNombre(newcontenido.toString().split("\n")[i+1].split("###")[0]);
					modelo.setCveAgente(newcontenido.toString().split("\n")[i+1].split("###")[1]);					
					modelo.setMoneda(fn.buscaMonedaEnTexto(newcontenido.toString().split("\n")[i+1]));
				}
	
				if(newcontenido.toString().split("\n")[i].contains("Días de Vigencia") && newcontenido.toString().split("\n")[i].contains("pago")) {
					newdirec.append(newcontenido.toString().split("\n")[i].split(ConstantsValue.DIASAC)[0]);
					if(newdirec.length() == 0){
						newdirec.append(newcontenido.toString().split("\n")[i+1].split("###")[0]);
					}
					
					modelo.setFormaPago(fn.formaPagoSring(newcontenido.toString().split("\n")[i+1]));
				}
				getVigencias(newcontenido, i);
				if(newcontenido.toString().split("\n")[i].contains("Emisión") && newcontenido.toString().split("\n")[i].contains("R.F.C:")) {
					
					modelo.setFechaEmision(fn.formatDateMonthCadena(fn.obtenVigePoliza(newcontenido.toString().split("\n")[i]).get(0)));
			         modelo.setRfc(newcontenido.toString().split("\n")[i].split("R.F.C:")[1].split("Fecha")[0].replace("###", "").trim());
				}

				if(newcontenido.toString().split("\n")[i].contains("C.P:")) {
					modelo.setCp(newcontenido.toString().split("\n")[i].split("C.P:")[1].replace("###", "").trim().substring(0,5));
					newdirec.append(newcontenido.toString().split("\n")[i].split("C.P:")[0]);
				}
			
				
			}
			
			if(!newdirec.isEmpty()) {
				modelo.setCteDireccion(newdirec.toString().replace("###", " ").trim());
			}
			
			

			inicio = contenido.indexOf("DESGLOSE DE COBERTURAS");
			fin = contenido.indexOf("Prima Neta");
            getCoberturas(contenido, inicio, fin);
			

			inicio = contenido.indexOf("Prima Neta");
			fin = contenido.indexOf("En testimonio");
	
            getPrimas(contenido, inicio, fin);
			
			return modelo;
		} catch (Exception ex) {
			modelo.setError(
					BexmasDiversosModel.this.getClass().getTypeName() + " | " + ex.getMessage() + " | " + ex.getCause());
			return modelo;
		}
		
	}

	private void getVigencias(StringBuilder newcontenido, int i) {
		if(newcontenido.toString().split("\n")[i].contains("Desde") && newcontenido.toString().split("\n")[i].contains("Hasta")) {	
		   if(newcontenido.toString().split("\n")[i+1].split("-").length >2){
			modelo.setVigenciaDe(fn.formatDateMonthCadena(fn.obtenVigePoliza(newcontenido.toString().split("\n")[i+1]).get(0)));
			modelo.setVigenciaA(fn.formatDateMonthCadena(fn.obtenVigePoliza(newcontenido.toString().split("\n")[i+1]).get(1)));
		   }
		   if(newcontenido.toString().split("\n")[i+2].split("-").length >2){
			modelo.setVigenciaDe(fn.formatDateMonthCadena(fn.obtenVigePoliza(newcontenido.toString().split("\n")[i+2]).get(0)));
			modelo.setVigenciaA(fn.formatDateMonthCadena(fn.obtenVigePoliza(newcontenido.toString().split("\n")[i+2]).get(1)));
		   }
		
		}
	}

	private void getCoberturas(String contenido, int inicio, int fin) {
		StringBuilder newcontenido;
		newcontenido = new StringBuilder();
		newcontenido.append( fn.extracted(inicio, fin, contenido));
		
		List<EstructuraCoberturasModel> coberturas = new ArrayList<>();
		for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
			EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();
			if(!newcontenido.toString().split("\n")[i].contains("DESGLOSE") && !newcontenido.toString().split("\n")[i].contains("Observaciones")
					&& !newcontenido.toString().split("\n")[i].contains("Sección")	) {			
				cobertura.setSeccion(newcontenido.toString().split("\n")[i].split("###")[0]);
				cobertura.setNombre(newcontenido.toString().split("\n")[i].split("###")[1]);
				cobertura.setSa(newcontenido.toString().split("\n")[i].split("###")[2]);
				cobertura.setDeducible(newcontenido.toString().split("\n")[i].split("###")[3]);
				coberturas.add(cobertura);
			}
		}
		modelo.setCoberturas(coberturas);
	}

	private void getPrimas(String contenido, int inicio, int fin) {
		StringBuilder newcontenido;
		newcontenido = new StringBuilder();
		newcontenido.append( fn.extracted(inicio, fin, contenido));
				
		for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {			
			if(newcontenido.toString().split("\n")[i].contains("Prima Neta:")) {
				modelo.setPrimaneta(fn.castBigDecimal( fn.preparaPrimas(newcontenido.toString().split("\n")[i].split("Prima Neta:")[1].replace("###", ""))));
			}
			if(newcontenido.toString().split("\n")[i].contains("Recargos:")) {
				modelo.setRecargo(fn.castBigDecimal( fn.preparaPrimas(newcontenido.toString().split("\n")[i].split("Recargos:")[1].replace("###", ""))));					
		    }
			if(newcontenido.toString().split("\n")[i].contains("Derechos:")) {
				modelo.setDerecho(fn.castBigDecimal( fn.preparaPrimas(newcontenido.toString().split("\n")[i].split("Derechos:")[1].replace("###", ""))));	
			}
			if(newcontenido.toString().split("\n")[i].contains(ConstantsValue.IVA)) {
				modelo.setIva(fn.castBigDecimal( fn.preparaPrimas(newcontenido.toString().split("\n")[i].split("I.V.A.")[1].replace("###", ""))));	
			}
			if(newcontenido.toString().split("\n")[i].contains("Prima Total:")) {
				modelo.setPrimaTotal(fn.castBigDecimal( fn.preparaPrimas(newcontenido.toString().split("\n")[i].split("Prima Total:")[1].replace("###", ""))));	
			}
		}
	}
}
