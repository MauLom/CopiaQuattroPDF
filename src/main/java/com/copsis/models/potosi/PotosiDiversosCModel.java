package com.copsis.models.potosi;

import java.util.ArrayList;
import java.util.List;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraCoberturasModel;
import com.copsis.models.EstructuraJsonModel;
import com.copsis.models.EstructuraRecibosModel;
import com.copsis.models.EstructuraUbicacionesModel;

public class PotosiDiversosCModel {
	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
	private String contenido = "";

	public PotosiDiversosCModel(String contenido) {
		this.contenido = contenido;
	}

	public EstructuraJsonModel procesar() {
		int inicio = 0;
		int fin = 0;
		int splitxt = 0;
		StringBuilder newcontenido = new StringBuilder();
		contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());
		try {
			modelo.setTipo(7);
			modelo.setCia(37);

			inicio = contenido.indexOf("MONEDA");
			fin = contenido.indexOf("PRIMAS");

			newcontenido.append(fn.extracted(inicio, fin, contenido));
			for(int i =0; i <newcontenido.toString().split("\n").length ; i++) {
				if(newcontenido.toString().split("\n")[i].contains("MONEDA:") && newcontenido.toString().split("\n")[i].contains("NÚM. PÓLIZA:")) {
                 modelo.setMoneda(fn.buscaMonedaEnTexto(newcontenido.toString().split("\n")[i]));
                 modelo.setPoliza(newcontenido.toString().split("\n")[i].split("NÚM. PÓLIZA:")[1].trim());
				}
				if(newcontenido.toString().split("\n")[i].contains("FORMA DE PAGO:")) {
					  modelo.setFormaPago(fn.formaPagoSring(newcontenido.toString().split("\n")[i]));
				}
				if(newcontenido.toString().split("\n")[i].contains("VIGENCIA")) {
				
				 modelo.setVigenciaDe(fn.formatDateMonthCadena(fn.obtenVigePoliza(newcontenido.toString().split("\n")[i]).get(0)));
				 modelo.setVigenciaA(fn.formatDateMonthCadena(fn.obtenVigePoliza(newcontenido.toString().split("\n")[i]).get(1)));
				 modelo.setFechaEmision(fn.formatDateMonthCadena(fn.obtenVigePoliza(newcontenido.toString().split("\n")[i]).get(2)));
				}
				if(newcontenido.toString().split("\n")[i].contains("DATOS DEL ASEGURADO") && newcontenido.toString().split("\n")[i+1].contains("NOMBRE Y-O RAZÓN SOCIAL:")
						&& newcontenido.toString().split("\n")[i+1].contains("RFC")		) {
					modelo.setCteNombre(newcontenido.toString().split("\n")[i+1].split("RAZÓN SOCIAL:")[1].split("RFC")[0].replace("###","").trim());
					modelo.setRfc(newcontenido.toString().split("\n")[i+1].split("RFC:")[1].replace("###","").replace("-", "").trim());
				}
				
				if(newcontenido.toString().split("\n")[i].contains("DIRECCIÓN:") && newcontenido.toString().split("\n")[i].contains("GIRO")) {
					  modelo.setCteDireccion(newcontenido.toString().split("\n")[i].split("DIRECCIÓN:")[1].split("GIRO")[0].replace("###","").trim());
				}
				if(newcontenido.toString().split("\n")[i].contains("C.P.")) {
				 modelo.setCp(newcontenido.toString().split("\n")[i].split("C.P.")[1].trim().substring(0,5));
				}
			}
			
			inicio = contenido.indexOf("PRIMAS");
			fin = contenido.indexOf("MODULO SECCIÓN");
			newcontenido = new StringBuilder();
			newcontenido.append(fn.extracted(inicio, fin, contenido));
			for(int i =0; i <newcontenido.toString().split("\n").length ; i++) {
				if(newcontenido.toString().split("\n")[i].contains("FRACCIONADO")) {
					if(newcontenido.toString().split("\n")[i+1].split("###").length == 8){
						modelo.setPrimaneta(fn.castBigDecimal(fn.castFloat(newcontenido.toString().split("\n")[i+1].split("###")[0])));
						modelo.setRecargo(fn.castBigDecimal(fn.castFloat(newcontenido.toString().split("\n")[i+1].split("###")[1])));
						modelo.setDerecho(fn.castBigDecimal(fn.castFloat(newcontenido.toString().split("\n")[i+1].split("###")[2])));
						modelo.setIva(fn.castBigDecimal(fn.castFloat(newcontenido.toString().split("\n")[i+1].split("###")[4])));
						modelo.setPrimaTotal(fn.castBigDecimal(fn.castFloat(newcontenido.toString().split("\n")[i+1].split("###")[7])));
					}
					if(newcontenido.toString().split("\n")[i+1].split("###").length == 10){
						modelo.setPrimaneta(fn.castBigDecimal(fn.castFloat(newcontenido.toString().split("\n")[i+1].split("###")[2])));
						modelo.setRecargo(fn.castBigDecimal(fn.castFloat(newcontenido.toString().split("\n")[i+1].split("###")[3])));
						modelo.setDerecho(fn.castBigDecimal(fn.castFloat(newcontenido.toString().split("\n")[i+1].split("###")[4])));
						modelo.setIva(fn.castBigDecimal(fn.castFloat(newcontenido.toString().split("\n")[i+1].split("###")[6])));
						modelo.setPrimaTotal(fn.castBigDecimal(fn.castFloat(newcontenido.toString().split("\n")[i+1].split("###")[9])));
					}
				
				}
			}
			
		
			inicio = contenido.indexOf("MODULO SECCIÓN");
			fin = contenido.indexOf("DOMICILIO DE RIESGO");
			newcontenido = new StringBuilder();
			newcontenido.append(fn.extracted(inicio, fin, contenido));
			List<EstructuraCoberturasModel> coberturas = new ArrayList<>();
			for(int i =0; i < newcontenido.toString().split("\n").length ; i++) {	
				EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();
				if(!newcontenido.toString().split("\n")[i].contains("-") && !newcontenido.toString().split("\n")[i].contains("SECCIÓN")) {
					if(newcontenido.toString().split("\n")[i].split("###")[0].length() > 10 && newcontenido.toString().split("\n")[i].split("###").length > 1) {
						splitxt = newcontenido.toString().split("\n")[i].split("###").length;
					  if(splitxt == 3) {
						  cobertura.setNombre(newcontenido.toString().split("\n")[i].split("###")[0]);
						  cobertura.setSa(newcontenido.toString().split("\n")[i].split("###")[1]);
						  cobertura.setDeducible(newcontenido.toString().split("\n")[i].split("###")[2]);
						  coberturas.add(cobertura);
					  }
					}
					
				}
			}
			modelo.setCoberturas(coberturas);
			
			
			
			inicio = contenido.indexOf("DOMICILIO DE RIESGO");
			fin = contenido.indexOf("Clave del Agente");
			List<EstructuraUbicacionesModel> ubicaciones = new ArrayList<>();
			newcontenido = new StringBuilder();
			newcontenido.append(fn.extracted(inicio, fin, contenido));
			modelo.setUbicaciones(ubicaciones);
			EstructuraUbicacionesModel ubicacion = new EstructuraUbicacionesModel();
			for(int i =0; i < newcontenido.toString().split("\n").length ; i++) {
			
				 
				 if(newcontenido.toString().split("\n")[i].contains("DIRECCIÓN:") && newcontenido.toString().split("\n")[i].contains("CP")) {
					ubicacion.setCalle(newcontenido.toString().split("\n")[i].split("DIRECCIÓN:")[1].split("CP")[0]);
				 }
				 
				 if(newcontenido.toString().split("\n")[i].contains("Material de los Muros") 
						 &&   newcontenido.toString().split("\n")[i].contains("Material de los Techos:")
						 &&   newcontenido.toString().split("\n")[i].contains("Número de Niveles")
						 &&   newcontenido.toString().split("\n")[i].contains("Zona TEV")
						 
						 ) {
				  ubicacion.setMuros(fn.material(newcontenido.toString().split("\n")[i].split("Material de los Muros")[1].split("Material de los Techos:")[0])); 
				  ubicacion.setTechos(fn.material(newcontenido.toString().split("\n")[i].split("Material de los Techos")[1].split("Número de Niveles")[0]));				
				  ubicacion.setNiveles(fn.castInteger(newcontenido.toString().split("\n")[i].split("Número de Niveles")[1].split("Zona TEV")[0].replace("###", "").trim()));
				 }
			}
			
			ubicaciones.add(ubicacion);
			modelo.setUbicaciones(ubicaciones);
			
			
		
			inicio = contenido.indexOf("Clave del Agente");
			fin = contenido.indexOf("Registro:");
		
			newcontenido = new StringBuilder();
			newcontenido.append(fn.extracted(inicio, fin, contenido));
			for(int i =0; i < newcontenido.toString().split("\n").length ; i++) {
				if(newcontenido.toString().split("\n")[i].contains("Clave del Agente")) {
					modelo.setCveAgente(newcontenido.toString().split("\n")[i+1].split("###")[0]);
					modelo.setAgente(newcontenido.toString().split("\n")[i+1].split("###")[1]);
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

			
			
			return modelo;
		} catch (Exception ex) {
			modelo.setError(PotosiDiversosCModel.this.getClass().getTypeName() + " | " + ex.getMessage() + " | " + ex.getCause());
			return modelo;
		}

	}
}
