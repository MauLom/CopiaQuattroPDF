package com.copsis.models.qualitas;

import java.util.ArrayList;
import java.util.List;

import com.copsis.constants.ConstantsValue;
import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraCoberturasModel;
import com.copsis.models.EstructuraJsonModel;
import com.copsis.models.EstructuraRecibosModel;

public class qualitasAutosMotosModel {
	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
	private String contenido = "";
	private String agente = "";
	

	public qualitasAutosMotosModel(String contenido,String agente) {
		this.contenido = contenido;
		this.agente = agente;
	}

	



	public EstructuraJsonModel procesar() {
	 int inicio=0;
	 int fin = 0;
	 StringBuilder newDireccion = new StringBuilder();
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
		
			modelo.setCia(29);		
			modelo.setTipo(1);
			modelo.setRamo("Autos");

			inicio = contenido.indexOf(ConstantsValue.SEGURO_DE_AUTOMOVILES);
			fin  = contenido.indexOf(ConstantsValue.COBERTURAS_CONTRATADAS);

		
			if(inicio >  -1 && fin > -1 && inicio < fin) {
				newcontenido = contenido.substring(inicio, fin).replace("@@@", "").replace("\r", "");
				for (int i = 0; i < newcontenido.split("\n").length; i++) {

					obtenerPolizaEndosoInciso(newcontenido.split("\n"), i);
					
					if(newcontenido.split("\n")[i].contains("INFORMACIÓN DEL ASEGURADO")) {
						modelo.setCteNombre(newcontenido.split("\n")[i+1].split("###")[0]);
					}
					if(newcontenido.split("\n")[i].contains(ConstantsValue.RFC)) {
						modelo.setRfc(newcontenido.split("\n")[i].split(ConstantsValue.RFC)[1].replace("###", "").replace("-", "").trim());
					}
					if(newcontenido.split("\n")[i].contains(ConstantsValue.DOMICILIO2)) {
						if(newcontenido.split("\n")[i].split(ConstantsValue.DOMICILIO2)[1].replace(":", " ").replace("### ", "").contains("R.F.C")) {
							newDireccion.append(  newcontenido.split("\n")[i].split(ConstantsValue.DOMICILIO2)[1].replace(":", " ").replace("### ", "")
									.split("R.F.C")[0].replace(modelo.getRfc(), ""));
						}else {
							newDireccion.append( newcontenido.split("\n")[i].split(ConstantsValue.DOMICILIO2)[1].replace(":", " ").replace("### ", "")
									.replace(modelo.getRfc(), ""));
						}
					
					}
					
					if(newcontenido.split("\n")[i].contains("C.P.")) {
						newDireccion.append( " "+newcontenido.split("\n")[i].split("C.P.")[1].replace(":", " ").replace("###", " ").trim());
						modelo.setCteDireccion(newDireccion.toString().replace("###", "").trim());
					
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
					if(newcontenido.split("\n")[i].contains(ConstantsValue.MODELO)) {
						if(newcontenido.split("\n")[i].split(ConstantsValue.MODELO)[1].split("###")[0].length() > 3) {
							modelo.setModelo(fn.castInteger(newcontenido.split("\n")[i].split(ConstantsValue.MODELO)[1].split("###")[0].trim()));
						}else {
							modelo.setModelo(fn.castInteger(newcontenido.split("\n")[i].split(ConstantsValue.MODELO)[1].split("###")[1].trim()));
						}
					
					}
					
					if(newcontenido.split("\n")[i].contains(ConstantsValue.PLACAS) && newcontenido.split("\n")[i].split("Placa")[1].length()  > 3) {
						
					
							modelo.setPlacas(newcontenido.split("\n")[i].split(ConstantsValue.PLACAS)[1].replace("###", "").trim());
										
					}
					if(newcontenido.split("\n")[i].contains("Serie:") && newcontenido.split("\n")[i].contains(ConstantsValue.MOTOR)) {
						modelo.setSerie(newcontenido.split("\n")[i].split("Serie:")[1].split(ConstantsValue.MOTOR)[0].replace("###", "").trim());
						if(newcontenido.split("\n")[i].split(ConstantsValue.MOTOR)[1].contains("REPUVE")) {
							modelo.setMotor(newcontenido.split("\n")[i].split(ConstantsValue.MOTOR)[1].split("REPUVE")[0].replace("###", "").trim());
						}else if(newcontenido.split("\n")[i].split(ConstantsValue.MOTOR)[1].contains(ConstantsValue.PLACAS)){
							String con = newcontenido.split("\n")[i].split(ConstantsValue.MOTOR)[1].split(ConstantsValue.PLACAS)[0].replace("###", "").trim();
							if(newcontenido.contains("Color:")){
								con = con.split("Color")[0];
							   } 
							modelo.setMotor(con);
						}
						
					}
					if(newcontenido.split("\n")[i].contains("Desde las 12:00") && fn.obtenVigePoliza2( newcontenido.split("\n")[i]).size() == 2) {						
						modelo.setVigenciaDe(fn.formatDateMonthCadena(fn.obtenVigePoliza2( newcontenido.split("\n")[i]).get(0)));
						modelo.setFechaEmision(fn.formatDateMonthCadena(fn.obtenVigePoliza2( newcontenido.split("\n")[i]).get(1)));					    
					}
					if(newcontenido.split("\n")[i].contains("Desde las 12:00") && fn.obtenVigePoliza2( newcontenido.split("\n")[i]).size() == 1) {
						modelo.setVigenciaDe(fn.formatDateMonthCadena(newcontenido.split("\n")[i].split("###")[1]));
						modelo.setFechaEmision(modelo.getVigenciaDe());
					}
					
					if(newcontenido.split("\n")[i].contains("Hasta las 12:00")) {
						List<String> valores = fn.obtenVigePoliza2(newcontenido.split("\n")[i]);	
						if(!valores.isEmpty()){
                          modelo.setVigenciaA(fn.formatDateMonthCadena(valores.get(0)));
						}		
						
					}

					
				}
				
			}
		
			modelo.setMoneda(1);
			
			inicio = agente.indexOf(ConstantsValue.AGENTE2);
			fin  = agente.indexOf("En###cumplimiento");
			if(inicio >  -1 && fin > -1 && inicio < fin) {
				newcontenido = agente.substring(inicio, fin).replace("@@@", "").replace("\r", "");
				for (int i = 0; i < newcontenido.split("\n").length; i++) {

					if(newcontenido.split("\n")[i].split(ConstantsValue.AGENTE)[1].split("###").length > 2) {
						modelo.setCveAgente(newcontenido.split("\n")[i].split(ConstantsValue.AGENTE)[1].split("###")[1].replace("###","").trim());
					}else {
						modelo.setCveAgente(newcontenido.split("\n")[i].split(ConstantsValue.AGENTE)[1].split(" ")[0].replace("###","").trim());
					}
					
					
					if(modelo.getCveAgente().length() > 0) {
						modelo.setAgente(newcontenido.split("\n")[i].split(modelo.getCveAgente())[1].replace("###", ""));
					}
					
				}
			}
			
			inicio = agente.indexOf("PLAN");
			fin  = agente.indexOf(ConstantsValue.RFC);
			if(inicio >  -1 && fin > -1 && inicio < fin) {
				newcontenido = agente.substring(inicio, fin).replace("@@@", "").replace("\r", "");
				for (int i = 0; i < newcontenido.split("\n").length; i++) {				
					if(newcontenido.split("\n")[i].contains("PLAN:")) {
						modelo.setPlan(newcontenido.split("\n")[i].split("PLAN:")[1].replace("###", "").trim());
					}
				}
			}
			
			
			
			
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
					if(newcontenido.split("\n")[i].contains("Financiamiento") && newcontenido.split("\n")[i].split("Financiamient")[1].length() > 5) {						
						modelo.setRecargo(fn.castBigDecimal(fn.preparaPrimas(newcontenido.split("\n")[i].split("Financiamiento")[1].replace("###", ""))));						
					}
					if(newcontenido.split("\n")[i].contains(ConstantsValue.EXPEDICION)) {												 
						modelo.setDerecho(fn.castBigDecimal(fn.preparaPrimas(newcontenido.split("\n")[i].split(ConstantsValue.EXPEDICION)[1].replace("###", "").trim())));						
					}
					if(newcontenido.split("\n")[i].contains(ConstantsValue.EXPEDICION3)) {												 
						modelo.setDerecho(fn.castBigDecimal(fn.preparaPrimas(newcontenido.split("\n")[i].split(ConstantsValue.EXPEDICION3)[1].replace("###", "").trim())));						
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
			if (inicio ==-1) {			
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

            
            if(modelo.getFormaPago() == 1 &&  fn.diferencia(modelo.getVigenciaDe(), modelo.getVigenciaA())> 1) {             
                modelo.setVigenciaA(fn.calcvigenciaA(modelo.getVigenciaDe(), 12));
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
				recibo.setPrimaneta(modelo.getPrimaneta());
				recibo.setDerecho(modelo.getDerecho());
				recibo.setRecargo(modelo.getRecargo());
				recibo.setIva(modelo.getIva());

				recibo.setPrimaTotal(modelo.getPrimaTotal());
				recibo.setAjusteUno(modelo.getAjusteUno());
				recibo.setAjusteDos(modelo.getAjusteDos());
				recibo.setCargoExtra(modelo.getCargoExtra());
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
