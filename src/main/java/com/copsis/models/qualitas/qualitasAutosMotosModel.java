package com.copsis.models.qualitas;

import java.util.ArrayList;
import java.util.List;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraCoberturasModel;
import com.copsis.models.EstructuraJsonModel;
import com.copsis.models.EstructuraRecibosModel;

public class qualitasAutosMotosModel {
	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
	private String contenido = "";
	private String agente = "";
	private String newDireccion = "";

	public qualitasAutosMotosModel(String contenido,String agente) {
		this.contenido = contenido;
		this.agente = agente;
	}

	
	private int inicio;

	private int fin = 0;


	public EstructuraJsonModel procesar() {
		String newcontenido = "";
		String[] arrNewContenido;
		contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());
		agente = fn.remplazarMultiple(agente, fn.remplazosGenerales())
				.replace("En cumplimiento", "En###cumplimiento");
		contenido = contenido.replace("IMPORTE TOTAL.", "IMPORTE TOTAL").replace("RREENNUUEEVVAA", "RENUEVA")
				.replace("MEsutnaidciop i:o:", "Municipio:").replace("Expedición.", "Expedición")
				.replace("Dom i c il i o ","Domicilio ")
				.replace("C.P:", "C.P.");
		
		try {
			// cia
			modelo.setCia(29);		

			// tipo
			modelo.setTipo(1);

			// ramo
			modelo.setRamo("Autos");


			inicio = contenido.indexOf("SEGURO DE AUTOMÓVILES");
			fin  = contenido.indexOf("COBERTURAS CONTRATADAS");
			
			if(inicio >  -1 && fin > -1 && inicio < fin) {
				newcontenido = contenido.substring(inicio, fin).replace("@@@", "").replace("\r", "");
				for (int i = 0; i < newcontenido.split("\n").length; i++) {

					obtenerPolizaEndosoInciso(newcontenido.split("\n"), i);
					
					if(newcontenido.split("\n")[i].contains("INFORMACIÓN DEL ASEGURADO")) {
						modelo.setCteNombre(newcontenido.split("\n")[i+1].split("###")[0]);
					}
					if(newcontenido.split("\n")[i].contains("R.F.C:")) {
						modelo.setRfc(newcontenido.split("\n")[i].split("R.F.C:")[1].replace("###", "").replace("-", "").trim());
					}
					if(newcontenido.split("\n")[i].contains("Domicilio")) {
						newDireccion =  newcontenido.split("\n")[i].split("Domicilio")[1].replace(":", " ").replace("### ", "")
								.replace(modelo.getRfc(), "");
					}
					
					if(newcontenido.split("\n")[i].contains("C.P.")) {
						newDireccion  += newcontenido.split("\n")[i].split("C.P.")[1].replace(":", " ").replace("###", " ").trim();
						modelo.setCteDireccion(newDireccion.replace("###", "").trim());
					
					}
				
					
					if(newcontenido.split("\n")[i].contains("C.P.")) {
						if(newcontenido.split("C.P.")[1].split("###")[0].length() > 3) {
							modelo.setCp(newcontenido.split("C.P.")[1].split("###")[0].trim());
						}else {
							modelo.setCp(newcontenido.split("C.P.")[1].split("###")[1].trim());
						}
						
					
					}
					if(newcontenido.split("\n")[i].contains("DESCRIPCIÓN DEL VEHÍCULO ASEGURADO")) {
						modelo.setDescripcion( newcontenido.split("\n")[i+1].replace("###", "").trim());
						
					}
					if(newcontenido.split("\n")[i].contains("Modelo")) {
						if(newcontenido.split("\n")[i].split("Modelo:")[1].split("###")[0].length() > 3) {
							modelo.setModelo(fn.castInteger(newcontenido.split("\n")[i].split("Modelo:")[1].split("###")[0].trim()));
						}else {
							modelo.setModelo(fn.castInteger(newcontenido.split("\n")[i].split("Modelo:")[1].split("###")[1].trim()));
						}
					
					}
					if(newcontenido.split("\n")[i].contains("Placas") && newcontenido.split("\n")[i].split("Placas")[1].length()  > 3) {
						
					
							modelo.setPlacas(newcontenido.split("\n")[i].split("Placas:")[1].replace("###", "").trim());
										
					}
					if(newcontenido.split("\n")[i].contains("Serie:") && newcontenido.split("\n")[i].contains("Motor:")) {
						modelo.setSerie(newcontenido.split("\n")[i].split("Serie:")[1].split("Motor:")[0].replace("###", "").trim());
						if(newcontenido.split("\n")[i].split("Motor:")[1].contains("REPUVE")) {
							modelo.setMotor(newcontenido.split("\n")[i].split("Motor:")[1].split("REPUVE")[0].replace("###", "").trim());
						}else if(newcontenido.split("\n")[i].split("Motor:")[1].contains("Placas:")){
							modelo.setMotor(newcontenido.split("\n")[i].split("Motor:")[1].split("Placas:")[0].replace("###", "").trim());
						}
						
					}
					if(newcontenido.split("\n")[i].contains("Desde las 12:00")) {
						modelo.setVigenciaDe(fn.formatDateMonthCadena(newcontenido.split("\n")[i].split("###")[1]));
						modelo.setFechaEmision(modelo.getVigenciaDe());
					}
					
					if(newcontenido.split("\n")[i].contains("Hasta las 12:00")) {
						modelo.setVigenciaA(fn.formatDateMonthCadena(newcontenido.split("\n")[i].split("###")[1]));
					}

					
				}
				
			}
			
			modelo.setMoneda(1);
			
			inicio = agente.indexOf("Agente");
			fin  = agente.indexOf("En###cumplimiento");
			if(inicio >  -1 && fin > -1 && inicio < fin) {
				newcontenido = agente.substring(inicio, fin).replace("@@@", "").replace("\r", "");
				for (int i = 0; i < newcontenido.split("\n").length; i++) {

					if(newcontenido.split("\n")[i].split("Agente:")[1].split("###").length > 2) {
						modelo.setCveAgente(newcontenido.split("\n")[i].split("Agente:")[1].split("###")[1].replace("###","").trim());
					}else {
						modelo.setCveAgente(newcontenido.split("\n")[i].split("Agente:")[1].split(" ")[0].replace("###","").trim());
					}
					
					
					if(modelo.getCveAgente().length() > 0) {
						modelo.setAgente(newcontenido.split("\n")[i].split(modelo.getCveAgente())[1].replace("###", ""));
					}
					
				}
			}
			
			inicio = agente.indexOf("PLAN");
			fin  = agente.indexOf("R.F.C:");
			if(inicio >  -1 && fin > -1 && inicio < fin) {
				newcontenido = agente.substring(inicio, fin).replace("@@@", "").replace("\r", "");
				for (int i = 0; i < newcontenido.split("\n").length; i++) {				
					if(newcontenido.split("\n")[i].contains("PLAN:")) {
						modelo.setPlan(newcontenido.split("\n")[i].split("PLAN:")[1].replace("###", "").trim());
					}
				}
			}
			
			
			newcontenido ="";
			
			inicio = contenido.indexOf("MONEDA");
			fin  = contenido.indexOf("Tarifa Aplicada");
	
			if(inicio >  -1 && fin > -1 && inicio < fin) {
				newcontenido = contenido.substring(inicio, fin).replace("@@@", "").replace("\r", "");
				modelo.setFormaPago(fn.formaPagoSring(newcontenido));
				for (int i = 0; i < newcontenido.split("\n").length; i++) {
	
					if(newcontenido.split("\n")[i].contains("Anual")  || newcontenido.split("\n")[i].contains("CONTADO")) {
						modelo.setFormaPago(1);
					}
				
					if(newcontenido.split("\n")[i].contains("Prima Neta")) {		
						modelo.setPrimaneta(fn.castBigDecimal(fn.preparaPrimas(newcontenido.split("\n")[i].split("Prima Neta")[1].replace("###", ""))));						
					}
					if(newcontenido.split("\n")[i].contains("Financiamiento")) {						
					  
						modelo.setRecargo(fn.castBigDecimal(fn.preparaPrimas(newcontenido.split("\n")[i].split("Financiamiento")[1].replace("###", ""))));						
					}
					if(newcontenido.split("\n")[i].contains("Expedición")) {												 
						modelo.setDerecho(fn.castBigDecimal(fn.preparaPrimas(newcontenido.split("\n")[i].split("Expedición")[1].replace("###", "").trim())));						
					}
					if(newcontenido.split("\n")[i].contains("I.V.A.")) {	
						arrNewContenido = newcontenido.split("\n")[i].split("I.V.A.")[1].split("###");
						modelo.setIva(fn.castBigDecimal(fn.preparaPrimas(arrNewContenido[arrNewContenido.length-1].replace("###", "").trim())));						
					}
					if(newcontenido.split("\n")[i].contains("TOTAL")) {	
						modelo.setPrimaTotal(fn.castBigDecimal(fn.preparaPrimas(newcontenido.split("\n")[i].split("TOTAL")[1].replace("###", "").trim())));						
					}
					
				}
			}
			

			// coberturas
			inicio = contenido.indexOf("PRIMAS");
			if (inicio > -1) {

			} else {
				inicio = contenido.indexOf("PRIMA");
			}
			fin = contenido.indexOf("MONEDA");

			if (inicio > -1 && fin > inicio) {
				newcontenido = contenido.substring(inicio, fin).replace("@@@", "").trim();
				if (newcontenido.contains("La Unidad de Medida")) {
					newcontenido = newcontenido.split("La Unidad de Medida")[0].trim();
				}
				if (newcontenido.contains("Servicios de Asistencia Vial")) {
					newcontenido = newcontenido.split("Servicios de Asistencia Vial")[0].trim();
				}

				List<EstructuraCoberturasModel> coberturas = new ArrayList<>();

				for (String x : newcontenido.split("\r\n")) {
					EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();
					switch (x.split("###").length) {
					case 4:
						cobertura.setNombre(x.split("###")[0].trim());
						cobertura.setSa(fn.eliminaSpacios(x.split("###")[1].trim(), ' ', ""));
						cobertura.setDeducible(x.split("###")[2].trim());
						coberturas.add(cobertura);
						break;
					case 3:
						cobertura.setNombre(x.split("###")[0].trim());
						cobertura.setSa(fn.eliminaSpacios(x.split("###")[1].trim(), ' ', ""));
						coberturas.add(cobertura);
						break;
					default:
						break;
					}

				}

				modelo.setCoberturas(coberturas);
			}

			List<EstructuraRecibosModel> recibos = new ArrayList<>();

			EstructuraRecibosModel recibo = new EstructuraRecibosModel();

			switch (modelo.getFormaPago()) {
			case 1:
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
				break;
			case 2:
				break;

			case 3:
			case 4:
				break;
			default:
				break;

			}
			modelo.setRecibos(recibos);
			return modelo;
		} catch (Exception ex) {
			modelo.setError(
					qualitasAutosMotosModel.this.getClass().getTypeName() + " | " + ex.getMessage() + " | " + ex.getCause());
			return modelo;
		}
	}
	

	private void obtenerPolizaEndosoInciso(String[] arrContenido, int i) {
		if(arrContenido[i].contains("SEGURO DE AUTOMÓVILES") && arrContenido[i].contains("INCISO") && arrContenido[i].contains("PÓLIZA")) {
			modelo.setPoliza(arrContenido[i+1].split("###")[0].trim());
			modelo.setEndoso(arrContenido[i+1].split("###")[1].trim());
			modelo.setInciso(Integer.parseInt(arrContenido[i+1].split("###")[2].trim()));
		} else if (arrContenido[i].contains("SEGURO DE AUTOMÓVILES") && !arrContenido[i].contains("PÓLIZA")
				&& arrContenido[i].split("###").length == 4) {
			modelo.setPoliza(arrContenido[i].split("###")[1].trim());
			modelo.setEndoso(arrContenido[i].split("###")[2].trim());
			modelo.setInciso(Integer.parseInt(arrContenido[i].split("###")[3].trim()));
		}
	}

}
