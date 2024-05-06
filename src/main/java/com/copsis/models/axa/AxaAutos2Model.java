package com.copsis.models.axa;

import java.util.ArrayList;
import java.util.List;

import com.copsis.constants.ConstantsValue;
import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraCoberturasModel;
import com.copsis.models.EstructuraJsonModel;
import com.copsis.models.EstructuraRecibosModel;

public class AxaAutos2Model {
	// Clases
	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();

	private String contenido = "";

	public AxaAutos2Model(String contenido) {
		this.contenido = contenido;
	}

	public EstructuraJsonModel procesar() {
		String newcontenido = "";
		int inicio;
		int fin;
		contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales())
		.replace("Datos del vehículo", ConstantsValue.DATOS_VEHICULO);
		try {
			
			modelo.setTipo(1);			
			modelo.setCia(20);


			inicio = contenido.indexOf("Carátula de póliza");
			fin = contenido.indexOf("Datos del asegurado");
			if (inicio > -1 && fin > -1 && inicio < fin) {
				newcontenido = contenido.substring(inicio, fin).replace("@@@", "").replace("\r", "");
				for (int i = 0; i < newcontenido.split("\n").length; i++) {
					if(newcontenido.split("\n")[i].contains("-")) {
                     modelo.setPlan(newcontenido.split("\n")[i].split("###")[1].split("-")[1]);		
                     
					}
					if(newcontenido.split("\n")[i].contains("-") && newcontenido.split("\n")[i].split("###")[2].contains("-")) {
						modelo.setPlan(newcontenido.split("\n")[i].split("###")[2].split("-")[1]);	
					}
					if(modelo.getPlan().length()  == 0 && newcontenido.split("\n")[i].length() == 2) {
						
							modelo.setPlan(newcontenido.split("\n")[i].split("###")[1]);
					
					}
				}			
			}
			
			

			inicio = contenido.indexOf("Datos del asegurado");
			fin = contenido.indexOf(ConstantsValue.DATOS_VEHICULO);

			
			
			if (inicio > -1 && fin > -1 && inicio < fin) {
				newcontenido = contenido.substring(inicio, fin).replace("@@@", "").replace("\r", "");
				for (int i = 0; i < newcontenido.split("\n").length; i++) {
					if (newcontenido.split("\n")[i].contains("Name:")) {
						modelo.setPoliza(newcontenido.split("\n")[i]
								.split("###")[newcontenido.split("\n")[i].split("###").length - 1]);
						modelo.setCteNombre((newcontenido.split("\n")[i].split(modelo.getPoliza())[0].split("Name:")[1]
								.replace("###", " ")).trim());
					}
					if (newcontenido.split("\n")[i].contains("Address:")
							&& newcontenido.split("\n")[i].contains("Desde")) {
						modelo.setCteDireccion(
								newcontenido.split("\n")[i].split("Address:")[1].split("Desde")[0].replace("###", " ").trim());
						modelo.setVigenciaDe(fn.formatDateMonthCadena(
								newcontenido.split("\n")[i].split("From:")[1].replace("###", "")));
						modelo.setFechaEmision(modelo.getVigenciaDe());
					}
					if (newcontenido.split("\n")[i].contains("Hasta")) {
						modelo.setVigenciaA(fn
								.formatDateMonthCadena(newcontenido.split("\n")[i].split("To:")[1].replace("###", "")));
					}
				}
			}

			inicio = contenido.indexOf(ConstantsValue.DATOS_VEHICULO);
			fin = contenido.indexOf("Datos adicionales");
			if (inicio > 0 && fin > 0 && inicio < fin) {
				newcontenido = contenido.substring(inicio, fin).replace("@@@", "").replace("\r", "");
				for (int i = 0; i < newcontenido.split("\n").length; i++) {
					if (newcontenido.split("\n")[i].contains(ConstantsValue.DATOS_VEHICULO)
							&& newcontenido.split("\n")[i].contains("Term")) {
						modelo.setDescripcion(newcontenido.split("\n")[i].split(ConstantsValue.DATOS_VEHICULO)[1].split("Term:")[0]
								.replace("###", "").trim());
					}else if(newcontenido.split("\n")[i].contains("Vehicle")&& newcontenido.split("\n")[i].contains("Term") ) {
						modelo.setDescripcion(newcontenido.split("\n")[i].split("Vehicle")[1].split("Term:")[0].replace(":", "").replace("###", " ").trim());
					}
					if (newcontenido.split("\n")[i].contains("Modelo")) {
						modelo.setModelo(
								fn.castInteger(newcontenido.split("\n")[i].split("Modelo - Year:")[1].split("###")[1]));
					}
					if (newcontenido.split("\n")[i].contains("Currency")) {
						modelo.setMoneda(2);
					}
					
					if(newcontenido.split("\n")[i].contains("No. de Serie") &&  newcontenido.split("\n")[i].contains(ConstantsValue.OCUPANTES) ) {						 
				     	modelo.setSerie(newcontenido.split("\n")[i].split("de Serie")[1].split(ConstantsValue.OCUPANTES)[0].replace("###", "").replace("-", "").trim());				     
			     	}
					
					if (newcontenido.split("\n")[i].contains(ConstantsValue.VEHICLE_ID_NUMBER) && modelo.getSerie().length() == 0) {
					
						if (newcontenido.split("\n")[i].contains(ConstantsValue.OCUPANTES)) {
							modelo.setSerie(newcontenido.split("\n")[i].split(ConstantsValue.VEHICLE_ID_NUMBER)[1]
									.split(ConstantsValue.OCUPANTES)[0].replace("###", ""));
						} else {
							modelo.setSerie(newcontenido.split("\n")[i].split(ConstantsValue.VEHICLE_ID_NUMBER)[1]
									.replace("###", ""));
						}
					}
					

				
					
										
					
					
					if (newcontenido.split("\n")[i].contains("Placas")
							&& newcontenido.split("\n")[i].contains("Plate No:")) {
						modelo.setPlacas(newcontenido.split("\n")[i].split("Plate No:")[1].replace("###", ""));
					}
					if (newcontenido.split("\n")[i].contains("Contado")) {
						modelo.setFormaPago(1);
					}
				}
			}

			inicio = contenido.indexOf("Coberturas - Coverages");
			fin = contenido.indexOf("Notas importantes");
			if (inicio > 0 && fin > 0 && inicio < fin) {
				newcontenido = contenido.substring(inicio, fin).replace("@@@", "").replace("\r", "");
				for (int i = 0; i < newcontenido.split("\n").length; i++) {

					if (newcontenido.split("\n")[i].contains("Net premium:")) {
						modelo.setPrimaneta(fn.castBigDecimal(fn.cleanString(
								newcontenido.split("\n")[i].split("Net premium:")[1].split("###")[1].trim())));
					}
					if (newcontenido.split("\n")[i].contains("Policy fee:")) {
						modelo.setDerecho(fn.castBigDecimal(fn.cleanString(
								newcontenido.split("\n")[i].split("Policy fee:")[1].split("###")[1].trim())));
					}
					if (newcontenido.split("\n")[i].contains("I.V.A.")) {
						modelo.setIva(fn.castBigDecimal(
								fn.cleanString(newcontenido.split("\n")[i].split("V.A.T:")[1].split("###")[1].trim())));
					}
					if (newcontenido.split("\n")[i].contains("Total Premium:")) {
						modelo.setPrimaTotal(fn.castBigDecimal(fn.cleanString(
								newcontenido.split("\n")[i].split("Total Premium:")[1].split("###")[1].trim())));
					}

				}
			}

			inicio = contenido.indexOf("Nombre del agente");
			fin = contenido.indexOf("Notas importantes");
			if (inicio > 0 && fin > 0 && inicio < fin) {
				newcontenido = contenido.substring(inicio, fin).replace("@@@", "").replace("\r", "");
				for (int i = 0; i < newcontenido.split("\n").length; i++) {
					if (newcontenido.split("\n")[i].contains("agente ")) {
						modelo.setCveAgente(
								newcontenido.split("\n")[i].split("agente")[1].split("###")[0].replace("-", "").trim());
						modelo.setAgente(newcontenido.split("\n")[i].split("agente")[1].split("###")[1]);
					}
				}
			}

			inicio = contenido.indexOf(ConstantsValue.RFC2);
			fin = contenido.indexOf("Domicilio:");
			if (inicio > 0 && fin > 0 && inicio < fin) {
				newcontenido = contenido.substring(inicio, fin).replace("@@@", "").replace("\r", "");
				for (int i = 0; i < newcontenido.split("\n").length; i++) {
					if (newcontenido.split("\n")[i].contains(ConstantsValue.RFC2)) {
						modelo.setRfc(newcontenido.split("\n")[i].split(ConstantsValue.RFC2)[1].split("###")[1]);
					}
				}
			}

			inicio = contenido.indexOf("Domicilio:");
			fin = contenido.indexOf("Emisión:");
			if (inicio > 0 && fin > 0 && inicio < fin) {
				newcontenido = contenido.substring(inicio, fin).replace("@@@", "").replace("\r", "");
				for (int i = 0; i < newcontenido.split("\n").length; i++) {
					if (newcontenido.split("\n")[i].contains("Marca")) {
						modelo.setMarca(newcontenido.split("\n")[i].split("Marca")[1].split("###")[1]);
					}
				}
			}

			inicio = contenido.indexOf("Coberturas - Coverages");
			fin = contenido.indexOf("Gastos por expedición");
			if (inicio > 0 && fin > 0 && inicio < fin) {
				List<EstructuraCoberturasModel> coberturas = new ArrayList<>();
				newcontenido = contenido.substring(inicio, fin).replace("@@@", "").replace("\r", "");
				for (int i = 0; i < newcontenido.split("\n").length; i++) {
					EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();
					int sp = newcontenido.split("\n")[i].split("###").length;

					switch (sp) {
					case 3:
						if (!newcontenido.split("\n")[i].contains("Coberturas")
								&& !newcontenido.split("\n")[i].contains("Suma asegurada")
								&& !newcontenido.split("\n")[i].contains("Deductible")) {
							cobertura.setNombre(newcontenido.split("\n")[i].split("###")[0]);
							cobertura.setSa(newcontenido.split("\n")[i].split("###")[1]);
							cobertura.setDeducible(newcontenido.split("\n")[i].split("###")[2]);
							coberturas.add(cobertura);
						}
						break;
					case 4:
						cobertura.setNombre(newcontenido.split("\n")[i].split("###")[0]);
						cobertura.setSa(newcontenido.split("\n")[i].split("###")[1]);
						cobertura.setDeducible(newcontenido.split("\n")[i].split("###")[2]);
						coberturas.add(cobertura);
						break;
					default:
						break;
					}
				}
				modelo.setCoberturas(coberturas);
			}

			List<EstructuraRecibosModel> recibosList = new ArrayList<>();

			if (modelo.getFormaPago() == 1 && recibosList.isEmpty()) {

				EstructuraRecibosModel recibo = new EstructuraRecibosModel();
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
				recibosList.add(recibo);

			}

			modelo.setRecibos(recibosList);

			return modelo;
		} catch (Exception ex) {
			ex.printStackTrace();
			modelo.setError(
					AxaAutos2Model.this.getClass().getTypeName() + " | " + ex.getMessage() + " | " + ex.getCause());
			return modelo;
		}

	}

}
