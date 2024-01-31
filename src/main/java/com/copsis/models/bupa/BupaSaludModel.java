package com.copsis.models.bupa;

import com.copsis.constants.ConstantsValue;
import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraAseguradosModel;
import com.copsis.models.EstructuraCoberturasModel;
import com.copsis.models.EstructuraJsonModel;

import java.util.ArrayList;
import java.util.List;

public class BupaSaludModel {
	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();

	public EstructuraJsonModel procesar(String contenido,String conteniext,String recibo) {
		int inicio = 0;
		int fin = 0;
		StringBuilder newcontenido = new StringBuilder();
		contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());
		try {
			modelo.setTipo(3);
			modelo.setCia(87);
	
			
		
			inicio = contenido.indexOf(ConstantsValue.CONTRATANTE2);
			fin = contenido.indexOf("Advertencia");
			fin = fin == -1 ? contenido.indexOf("Área de Cobertur"):fin;
	
			newcontenido.append( fn.extracted(inicio, fin, contenido).replace("día-mes-año: ", ""));
			List<EstructuraAseguradosModel> asegurados = new ArrayList<>();
			EstructuraAseguradosModel asegurado = new EstructuraAseguradosModel();
			List<EstructuraCoberturasModel> coberturas = new ArrayList<>();
			EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();
			for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {	
			
				if(newcontenido.toString().split("\n")[i].contains(ConstantsValue.CONTRATANTE2) && newcontenido.toString().split("\n")[i+1].contains("Dirección")) {				
					
					modelo.setCteNombre(newcontenido.toString().split("\n")[i].split(ConstantsValue.CONTRATANTE2)[1].replace("###", "").trim());
				}
				if(newcontenido.toString().split("\n")[i].contains(ConstantsValue.DIRECCIONMC)) {
					modelo.setCteDireccion(newcontenido.toString().split("\n")[i].split(ConstantsValue.DIRECCIONMC)[1].replace("###", "").trim());					
					modelo.setCp(fn.obtenerCPRegex2(newcontenido.toString().split("\n")[i+1]));
					if(modelo.getCp().length() == 0) {
					    modelo.setCp(fn.obtenerCPRegex2(newcontenido.toString().split("\n")[i+2]));
					}
				}
			
				if(newcontenido.toString().split("\n")[i].contains("Número de Póliza")) {
					modelo.setPoliza(newcontenido.toString().split("\n")[i].split(ConstantsValue.POLIZA_ACENT)[1].replace("###", "").trim());
				}
			
				if(newcontenido.toString().split("\n")[i].contains(ConstantsValue.NUMERODE)&& newcontenido.toString().split("\n")[i+1].contains(ConstantsValue.POLIZA_ACENT) ) {
					modelo.setPoliza(newcontenido.toString().split("\n")[i].split(ConstantsValue.NUMERODE)[1].replace("###", "").trim());
				}
				if(newcontenido.toString().split("\n")[i].contains("Vigencia")) {
					modelo.setVigenciaDe(fn.formatDateMonthCadena(fn.obtenVigePoliza(newcontenido.toString().split("\n")[i]).get(0)));
				}
				if(newcontenido.toString().split("\n")[i].contains("Hasta")) {
					modelo.setVigenciaA(fn.formatDateMonthCadena(fn.obtenVigePoliza(newcontenido.toString().split("\n")[i]).get(0)));
				}
				if( newcontenido.toString().split("\n")[i].contains("Nacimiento") && newcontenido.toString().split("\n")[i].contains("Prima")) {
					  List<String> valores = fn.obtenerListNumeros(newcontenido.toString().split("\n")[i+1]);
					  modelo.setPrimaneta(fn.castBigDecimal(fn.castDouble(valores.get(0))));
				}
				
				if(newcontenido.toString().split("\n")[i].contains("Derecho de Póliza")) {
					  List<String> valores = fn.obtenerListNumeros(newcontenido.toString().split("\n")[i]);
					  modelo.setDerecho(fn.castBigDecimal(fn.castDouble(valores.get(0))));
				}
				if(newcontenido.toString().split("\n")[i].contains("Fraccionado")) {
					  List<String> valores = fn.obtenerListNumeros(newcontenido.toString().split("\n")[i]);
					if(!valores.isEmpty()) {
					  modelo.setRecargo(fn.castBigDecimal(fn.castDouble(valores.get(0))));
					}
				}
				if(newcontenido.toString().split("\n")[i].contains("IVA")) {
					  List<String> valores = fn.obtenerListNumeros(newcontenido.toString().split("\n")[i]);
					  modelo.setIva(fn.castBigDecimal(fn.castDouble(valores.get(0))));
				}
				if(newcontenido.toString().split("\n")[i].contains("Total")) {
					  List<String> valores = fn.obtenerListNumeros(newcontenido.toString().split("\n")[i]);
					  modelo.setPrimaTotal(fn.castBigDecimal(fn.castDouble(valores.get(0))));
				}
				
				if(newcontenido.toString().split("\n")[i].contains("Dependientes")) {					
					  List<String> valores = fn.obtenerListNumeros(newcontenido.toString().split("\n")[i]);
					  modelo.setCargoExtra(fn.castBigDecimal(fn.castDouble(valores.get(0))));
					  
				}
				
				
				
				if(newcontenido.toString().split("\n")[i].contains("based discount")) {					
					  List<String> valores = fn.obtenerListNumeros(newcontenido.toString().split("\n")[i]);
					  modelo.setAjusteUno(fn.castBigDecimal(fn.castDouble(valores.get(0))));

				}
				
				if(newcontenido.toString().split("\n")[i].contains("Asegurado Principal") || newcontenido.toString().split("\n")[i].contains("Asegurado Titular")) {				
				    
				    if(fn.obtenVigePoliza(newcontenido.toString().split("\n")[i].split("###")[1]).get(0).length() > 0) {
					asegurado.setNombre(newcontenido.toString().split("\n")[i].split("###")[1].split( fn.obtenVigePoliza(newcontenido.toString().split("\n")[i].split("###")[1]).get(0) )[0].trim());
					asegurado.setNacimiento(fn.formatDateMonthCadena(fn.obtenVigePoliza(newcontenido.toString().split("\n")[i].split("###")[1]).get(0)));
				    asegurado.setParentesco(fn.parentesco(newcontenido.toString().split("\n")[i+1] ));
					}else {
						asegurado.setNombre(newcontenido.toString().split("\n")[i].split("###")[1]);
						asegurado.setParentesco(fn.parentesco(newcontenido.toString().split("\n")[i+1] ));
					}	
					  
				}
				
				if(newcontenido.toString().split("\n")[i].contains("Deducibles") && newcontenido.toString().split("\n")[i].contains("Dentro")) {
					cobertura.setDeducible(newcontenido.toString().split("\n")[i].split("Dentro")[1].replace("###", ""));
				}
				if(newcontenido.toString().split("\n")[i].contains("Suma Asegurada") ) {
					cobertura.setSa(newcontenido.toString().split("\n")[i+1].substring(0, 30).split("###")[0].replace("###", ""));
				}
				
			}
			
			if(modelo.getVigenciaDe().length() > 0) {
				modelo.setFechaEmision(modelo.getVigenciaDe());
			}
			
			
			
			 asegurados.add(asegurado);
			modelo.setAsegurados(asegurados);
			
			coberturas.add(cobertura);
			modelo.setCoberturas(coberturas);
			
			
	
			inicio = conteniext.indexOf(ConstantsValue.FORMA_PAGO2);
			fin = conteniext.indexOf("Vigencia###Prima Neta");
			
			if(fin == -1) {
			    fin = conteniext.indexOf("Hasta###Prima Neta");
			}
			if(fin == -1) {
			    fin = conteniext.indexOf("Número de Recibo");
			}
		

			newcontenido.append( fn.extracted(inicio, fin, conteniext));
			
			for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {			
			
			if(newcontenido.toString().split("\n")[i].contains(ConstantsValue.FORMA_PAGO2) ) {
				
				  modelo.setFormaPago( fn.formaPagoSring(newcontenido.toString().split("\n")[i] ));
				}		
				if(modelo.getFormaPago() == 0 && newcontenido.toString().split("\n")[i].contains(ConstantsValue.FORMA_PAGO2)) {
				  modelo.setFormaPago( fn.formaPagoSring(newcontenido.toString().split("\n")[i+1] ));
				}
				
				if(newcontenido.toString().split("\n")[i].contains("Moneda") && newcontenido.toString().split("\n")[i+1].contains("MXP")) {
				
						modelo.setMoneda(1);
					
				}
					
			}
			if(modelo.getFormaPago() == 0) {		
				recibo = recibo.replace("Forma de Pago", ConstantsValue.FORMA_PAGO_MAYUS);
				inicio = recibo.indexOf(ConstantsValue.FORMA_PAGO_MAYUS);
	
				fin = recibo.indexOf("Prima Neta");
	
				newcontenido = new StringBuilder();
				newcontenido.append(fn.extracted(inicio, fin, recibo));
			
				
				for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
					if (newcontenido.toString().split("\n")[i].contains(ConstantsValue.FORMA_PAGO_MAYUS)) {
						modelo.setFormaPago(fn.formaPagoSring(newcontenido.toString().split("\n")[i + 1]));
					}
					if (newcontenido.toString().split("\n")[i].contains(ConstantsValue.FORMA_PAGO_MAYUS)) {
						modelo.setFormaPago(fn.formaPagoSring(newcontenido.toString().split("\n")[i]));
					}
					if (newcontenido.toString().split("\n")[i].contains("Moneda") && newcontenido.toString().split("\n")[i + 1].contains("MXP")) {						
							modelo.setMoneda(1);
						
					}
				}
			}

			if(modelo.getMoneda() == 0 && modelo.getFormaPago() == 0){
				modelo.setMoneda(1);
				modelo.setFormaPago(1);
			}

			if(modelo.getMoneda() == 0){
				modelo.setMoneda(1);
			}
			
			for (int i = 0; i < recibo.split("\n").length; i++) {
				
				if(recibo.split("\n")[i].contains("Clave del Agente")){
					List<String> valores = fn.obtenerListNumeros2(recibo.split("\n")[i]);
					modelo.setCveAgente(valores.get(0));
				}
				if(recibo.split("\n")[i].contains("Número de Póliza") && recibo.split("\n")[i].contains("Plan")){				
                  modelo.setPlan(recibo.split("\n")[i].split("Plan")[1].replace("###", "").trim());
				}
			}
	
			
			return modelo;
		} catch (Exception ex) {	
			
			modelo.setError(BupaSaludModel.this.getClass().getTypeName() + " | " + ex.getMessage() + " | " + ex.getCause());
			return modelo;
		}
	}
}
