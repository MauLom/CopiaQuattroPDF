package com.copsis.models.gnp;

import java.util.ArrayList;
import java.util.List;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraAseguradosModel;
import com.copsis.models.EstructuraCoberturasModel;
import com.copsis.models.EstructuraJsonModel;
import com.copsis.models.EstructuraRecibosModel;
import com.copsis.panAmerican.PanAmericanSaludModel;

public class GnpVidaModel4 {

	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
	
	public EstructuraJsonModel procesar(String contenido) {
		int inicio = 0;
		int fin = 0;
		StringBuilder newcontenido = new StringBuilder();
		StringBuilder newdire = new StringBuilder();
		contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());
		try {
			modelo.setTipo(5);
			modelo.setCia(18);

			inicio = contenido.indexOf("Domicilio del Asegurado");
			fin = contenido.indexOf("Beneficios");			
			newcontenido.append( fn.extracted(inicio, fin, contenido));
			EstructuraAseguradosModel asegurado = new EstructuraAseguradosModel();
			List<EstructuraAseguradosModel> asegurados = new ArrayList<>();
			for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {						
				if(newcontenido.toString().split("\n")[i].contains("Asegurado")) {
					modelo.setCteNombre(newcontenido.toString().split("\n")[i+1].split("###")[0]);
					asegurado.setNombre(modelo.getCteNombre());
				}
				if(newcontenido.toString().split("\n")[i].contains("Domicilio")) {
					newdire.append(newcontenido.toString().split("\n")[i+3].split("###")[0] );
					newdire.append(newcontenido.toString().split("\n")[i+4].split("###")[0]+" ");
					newdire.append(newcontenido.toString().split("\n")[i+5].split("###")[0] );
				}
				if(newcontenido.toString().split("\n")[i].contains("Número de la Póliza")) {
					modelo.setPoliza(newcontenido.toString().split("\n")[i+2].split("###")[1]);
				}
				if(newcontenido.toString().split("\n")[i].contains("R.F.C.") && newcontenido.toString().split("\n")[i].contains("CURP")) {
					modelo.setRfc(newcontenido.toString().split("\n")[i].split("R.F.C.")[1].split("CURP")[0].replace("###", "").trim());
				}
				if(newcontenido.toString().split("\n")[i].contains("R.F.C.") &&  modelo.getRfc().length() == 0) {
					modelo.setRfc(newcontenido.toString().split("\n")[i].split("R.F.C.")[1].trim());
				}
				if(newcontenido.toString().split("\n")[i].contains("Nacimiento") && newcontenido.toString().split("\n")[i].contains("Plan") && newcontenido.toString().split("\n")[i].contains("Moneda")) {
				modelo.setMoneda(fn.buscaMonedaEnTexto(newcontenido.toString().split("\n")[i+1]));	
				modelo.setPlan(newcontenido.toString().split("\n")[i+1].split("###")[1].trim());
				asegurado.setNacimiento(fn.formatDateMonthCadena(fn.obtenVigePoliza2(newcontenido.toString().split("\n")[i+1]).get(0)));
				asegurado.setParentesco(1);
				}
			}
			asegurados.add(asegurado);
			modelo.setAsegurados(asegurados);		
			if(newdire.length() > 0) {
				modelo.setCteDireccion(newdire.toString());
				modelo.setCp(fn.obtenerCPRegex2(modelo.getCteDireccion()));
			}

			inicio = contenido.indexOf("Fallecimiento");
			fin = contenido.indexOf("Fecha de Inicio");			
			newcontenido = new  StringBuilder();
			newcontenido.append( fn.extracted(inicio, fin, contenido));
			List<EstructuraCoberturasModel> coberturas = new ArrayList<>();
			for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {				
				EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();
				
				if(!newcontenido.toString().split("\n")[i].contains("Fallecimiento") && !newcontenido.toString().split("\n")[i].contains("Invalidez") ) {
					
					if(newcontenido.toString().split("\n")[i].contains("PAGO ÚNICO")) {
							modelo.setFormaPago(1);
						}
					
					int sp = newcontenido.toString().split("\n")[i].split("###").length;
					switch (sp) {
					case 2:
						cobertura.setNombre(newcontenido.toString().split("\n")[i].split("###")[0]);
						cobertura.setSa(newcontenido.toString().split("\n")[i].split("###")[1]);
						coberturas.add(cobertura);
						break;
					case 3:
						cobertura.setNombre(newcontenido.toString().split("\n")[i].split("###")[0]);
						cobertura.setSa(newcontenido.toString().split("\n")[i].split("###")[1]);
						coberturas.add(cobertura);
						break;	

					default:
						break;
					}
				}
			}
			modelo.setCoberturas(coberturas);
			
			
			
			inicio = contenido.indexOf("Fecha de Inicio");
			fin = contenido.indexOf("Beneficiarios");
			
			newcontenido = new  StringBuilder();
		
			newcontenido.append( fn.extracted(inicio, fin, contenido));
			
			for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
				
				if(newcontenido.toString().split("\n")[i].contains("Vigencia")){
					modelo.setFechaEmision(fn.formatDateMonthCadena(fn.obtenVigePoliza2(newcontenido.toString().split("\n")[i+1]).get(0)));
					modelo.setVigenciaDe(fn.formatDateMonthCadena(fn.obtenVigePoliza2(newcontenido.toString().split("\n")[i+1]).get(0)));
					modelo.setVigenciaA(fn.formatDateMonthCadena(fn.obtenVigePoliza2(newcontenido.toString().split("\n")[i+1]).get(1)));
					
					 List<String> valores = fn.obtenerListNumeros(newcontenido.toString().split("\n")[i+1]);
					 modelo.setPrimaneta(fn.castBigDecimal(fn.castDouble(valores.get(0))));
					 modelo.setPrimaTotal(fn.castBigDecimal(fn.castDouble(valores.get(0))));
				}
				if(newcontenido.toString().split("\n")[i].contains("ANUALMENTE")){
					modelo.setFormaPago(1);
				}
				
			}
			
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
			
			
			if(modelo.getVigenciaDe().length() > 0 && modelo.getVigenciaA().length() > 0  ) {		
				int cantida= fn.diferencia(modelo.getVigenciaDe(),modelo.getVigenciaA());
				if(cantida > 1) {
					modelo.setVigenciaA(fn.calcvigenciaA(modelo.getVigenciaDe(), 12));
				}
			}
			
			
			
			
			return modelo;
		} catch (Exception ex) {
			modelo.setError(GnpVidaModel4.this.getClass().getTypeName() + " | " + ex.getMessage() + " | " + ex.getCause());
			return modelo;
		}
	}
	
}
