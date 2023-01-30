package com.copsis.models.potosi;

import java.util.ArrayList;
import java.util.List;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraBeneficiariosModel;
import com.copsis.models.EstructuraCoberturasModel;
import com.copsis.models.EstructuraJsonModel;
import com.copsis.models.EstructuraRecibosModel;

public class PotosiVidaModel {
	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();	
	
	public EstructuraJsonModel procesar(String contenido) {
		int inicio = 0;
		int fin = 0;
		
		StringBuilder newcontenido = new StringBuilder();
		contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());
		try {
			modelo.setTipo(5);
			modelo.setCia(37);

			inicio = contenido.indexOf("POLIZA DE SEGURO");
			fin = contenido.indexOf("ASEGURADOS");
			
			newcontenido.append( fn.extracted(inicio, fin, contenido));
			for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
				if( newcontenido.toString().split("\n")[i].contains("Póliza") && newcontenido.toString().split("\n")[i].contains("Vigencia Desde") ) {
					if(newcontenido.toString().split("\n")[i].split("Póliza")[1].split("###")[0].length() > 2) {
						modelo.setPoliza(newcontenido.toString().split("\n")[i].split("Póliza")[1].split("###")[0]);	
					}else {
						modelo.setPoliza(newcontenido.toString().split("\n")[i].split("Póliza")[1].split("###")[1]);
					}					
					modelo.setVigenciaDe(fn.formatDateMonthCadena(fn.obtenVigePoliza( newcontenido.toString().split("\n")[i]).get(0)));					
				}
				if( newcontenido.toString().split("\n")[i].contains("Moneda") && newcontenido.toString().split("\n")[i].contains("Plan de Pago")) {
					modelo.setMoneda(fn.buscaMonedaEnTexto( newcontenido.toString().split("\n")[i]));
					modelo.setFormaPago(fn.formaPagoSring( newcontenido.toString().split("\n")[i]));
				}
				if( newcontenido.toString().split("\n")[i].contains("Sucursal") && newcontenido.toString().split("\n")[i].contains("Moneda")) {
					modelo.setMoneda(fn.buscaMonedaEnTexto( newcontenido.toString().split("\n")[i]));
				}
				if( newcontenido.toString().split("\n")[i].contains("Plan de Pago") && newcontenido.toString().split("\n")[i].contains("Num.Cliente")) {
					
					modelo.setFormaPago(fn.formaPagoSring( newcontenido.toString().split("\n")[i]));
				}
				if( newcontenido.toString().split("\n")[i].contains("Razón Social") && newcontenido.toString().split("\n")[i].contains("RFC")) {
					modelo.setCteNombre(newcontenido.toString().split("\n")[i].split("Social")[1].split("RFC")[0].replace("###", ""));
					modelo.setRfc(newcontenido.toString().split("\n")[i].split("RFC")[1].replace("###", ""));
				}
				if( newcontenido.toString().split("\n")[i].contains("Domicilio del Contratante") &&  newcontenido.toString().split("\n")[i].contains("Tels")
						&&  newcontenido.toString().split("\n")[i].contains("CP")	) {
					modelo.setCteDireccion(newcontenido.toString().split("\n")[i].split("Contratante")[1].split("Tels")[0].replace("###", ""));
					modelo.setCp(newcontenido.toString().split("\n")[i].split("CP")[1].trim().substring(0,5));
				}
			}
			
			
			if(modelo.getFormaPago() == 1) {
				modelo.setVigenciaA(fn.calcvigenciaA(modelo.getVigenciaDe(), 12));
				modelo.setFechaEmision(modelo.getVigenciaDe());
			}
			
			

			inicio = contenido.indexOf("Beneficiarios");
			fin = contenido.indexOf("Coberturas");
			newcontenido = new StringBuilder();
			newcontenido.append( fn.extracted(inicio, fin, contenido));
			List<EstructuraBeneficiariosModel> beneficiarios = new ArrayList<>();			
			for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
				EstructuraBeneficiariosModel beneficiario = new EstructuraBeneficiariosModel();
				if(!newcontenido.toString().split("\n")[i].contains("Ramo") && !newcontenido.toString().split("\n")[i].contains("Beneficiarios")) {
					beneficiario.setNombre(newcontenido.toString().split("\n")[i].split("###")[0]);
					beneficiario.setParentesco(fn.buscaParentesco(newcontenido.toString().split("\n")[i]));
					beneficiario.setPorcentaje(fn.castDouble(newcontenido.toString().split("\n")[i].split("###")[2].trim().replace("%", "")).intValue() );
					beneficiarios.add(beneficiario);
				}
			}
			modelo.setBeneficiarios(beneficiarios);
			
			
			
			
			
			inicio = contenido.indexOf("TASA DE FINANCIAMIENTO");
			fin = contenido.indexOf("Seguros el Potosí");
		   
	            
			newcontenido = new StringBuilder();
			newcontenido.append( fn.extracted(inicio, fin, contenido));
		
			for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
				if(newcontenido.toString().split("\n")[i].contains("FRACCIONADO")) {
		        	  List<String> valores = fn.obtenerListNumeros(newcontenido.toString().split("\n")[i+1]);

		        	  if(valores.size() == 8) {
		        		  modelo.setPrimaneta(fn.castBigDecimal(fn.castDouble(valores.get(0))));
		        		  modelo.setPrimaTotal(fn.castBigDecimal(fn.castDouble(valores.get(7))));
		        	  }
		        		

				}
			}
			
			
			
			inicio = contenido.indexOf("Suma Asegurada");
			fin = contenido.indexOf("Las primas de protección");
			if(fin == -1){
				 fin = contenido.indexOf("Seguros el Potosí, S.A.");
			}

			newcontenido = new StringBuilder();
			newcontenido.append( fn.extracted(inicio, fin, contenido).replace("PAGO DE LA SUMA ASEGURADA POR INVALIDEZ", "PAGO DE LA SUMA ASEGURADA POR INVALIDEZ TOTAL Y PERMANENTE")
			.replace("EXENCIÓN DE PAGO DE PRIMAS POR", "EXENCIÓN DE PAGO DE PRIMAS POR INVALIDEZ TOTAL Y PERMANENTE"));
			List<EstructuraCoberturasModel> coberturas = new ArrayList<>();			
			for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
				EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();
				
				if(!newcontenido.toString().split("\n")[i].contains("Deducible")) {
					int sp = newcontenido.toString().split("\n")[i].split("###").length;
					switch (sp) {
					case 3:
						cobertura.setNombre(newcontenido.toString().split("\n")[i].split("###")[0]);
						cobertura.setSa(newcontenido.toString().split("\n")[i].split("###")[1].trim());
						coberturas.add(cobertura);
						break;
					case 4:
						cobertura.setNombre(newcontenido.toString().split("\n")[i].split("###")[0]);
						cobertura.setSa(newcontenido.toString().split("\n")[i].split("###")[1].trim());
						coberturas.add(cobertura);
						break;	

					default:
						break;
					}
		
				}
			}
			modelo.setCoberturas(coberturas);
			List<EstructuraRecibosModel> recibos = new ArrayList<>();
			EstructuraRecibosModel recibo = new EstructuraRecibosModel();
			if (modelo.getFormaPago() == 1) {
				recibo.setReciboId("");
				recibo.setSerie("1/1");
				recibo.setVigenciaDe(modelo.getVigenciaDe());
				recibo.setVigenciaA(modelo.getVigenciaA());
				if (recibo.getVigenciaDe().length() > 0) {
					recibo.setVencimiento(fn.dateAdd(recibo.getVigenciaDe(), 30, 1));
				}
				recibo.setPrimaneta(fn.castBigDecimal(modelo.getPrimaneta(), 2));
				recibo.setDerecho(fn.castBigDecimal(modelo.getDerecho(), 2));
				recibo.setRecargo(fn.castBigDecimal(modelo.getRecargo(), 2));
				recibo.setIva(fn.castBigDecimal(modelo.getDerecho(), 2));

				recibo.setPrimaTotal(fn.castBigDecimal(modelo.getPrimaTotal(), 2));
				recibo.setAjusteUno(fn.castBigDecimal(modelo.getAjusteUno(), 2));
				recibo.setAjusteDos(fn.castBigDecimal(modelo.getAjusteDos(), 2));
				recibo.setCargoExtra(fn.castBigDecimal(modelo.getCargoExtra(), 2));
				recibos.add(recibo);
			}
			modelo.setRecibos(recibos);
			
			return modelo;
		} catch (Exception ex) {
			modelo.setError(PotosiVidaModel.this.getClass().getTypeName() + " | " + ex.getMessage() + " | " + ex.getCause());
			 return modelo;
		}
	}

}
