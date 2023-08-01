package com.copsis.models.atlas;

import java.util.ArrayList;
import java.util.List;

import com.copsis.constants.ConstantsValue;
import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraCoberturasModel;
import com.copsis.models.EstructuraJsonModel;

public class AtlasSaludModel {

	// Clases
	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
	// Variables
	private String contenido = "";

	public AtlasSaludModel(String contenido) {
		this.contenido = contenido;

	}

	public EstructuraJsonModel procesar() {
		String newcontenido = "";
		String resultado = "";
		int donde = 0;
		int inicio = 0;
		int fin = 0;

		contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());
		contenido = contenido.replace("las 12:00 Hrs.del", "");
		try {
			// tipo
			modelo.setTipo(3);

			// cia
			modelo.setCia(33);

			// Datos del Contrantante
			inicio = contenido.indexOf("PÓLIZA");
			fin = contenido.indexOf("ASEGURADOS");

			if (inicio > 0 && fin > 0 && inicio < fin) {
				newcontenido = contenido.substring(inicio, fin).replace("\r", "").replace("@@@", "").trim();
				for (int i = 0; i < newcontenido.split("\n").length; i++) {
					if (newcontenido.split("\n")[i].contains("Póliza")) {
						modelo.setPolizaGuion(newcontenido.split("\n")[i].split(ConstantsValue.POLIZA_ACENT2)[1].replace("###", ""));
						modelo.setPoliza(newcontenido.split("\n")[i].split(ConstantsValue.POLIZA_ACENT2)[1].replace("###", "")
								.replace("-", "").replace(" ", ""));
					}
					if (newcontenido.split("\n")[i].contains("desde:")
							&& newcontenido.split("\n")[i].contains(ConstantsValue.HASTA)) {
						modelo.setVigenciaDe(fn
								.formatDateMonthCadena(newcontenido.split("\n")[i].split("desde:")[1].split("Hasta:")[0]
										.replace("###", "").trim()));
						modelo.setVigenciaA(fn
								.formatDateMonthCadena(newcontenido.split("\n")[i].split("Hasta:")[1].split("Fecha")[0]
										.replace("###", "").trim()));
						modelo.setFechaEmision(fn.formatDateMonthCadena(
								newcontenido.split("\n")[i].split(ConstantsValue.EXPEDICION)[1].replace("###", "").replace(":", "").trim()));
					}
					if (newcontenido.split("\n")[i].contains("Contratante")
							&& newcontenido.split("\n")[i].contains("Domicilio")
							&& newcontenido.split("\n")[i].contains("RFC:")) {
						modelo.setCteNombre(newcontenido.split("\n")[i + 1]);
						modelo.setRfc(newcontenido.split("\n")[i].split("RFC:")[1].replace("###", ""));
						resultado = newcontenido.split("\n")[i + 2] + " " + newcontenido.split("\n")[i + 3];
						modelo.setCteDireccion(resultado.replace("###", " "));
					}
					if (newcontenido.split("\n")[i].contains("Producto:")
							&& newcontenido.split("\n")[i].contains(ConstantsValue.AGENTE)
							&& newcontenido.split("\n")[i].contains("Orden:")) {

						modelo.setCveAgente(newcontenido.split("\n")[i].split("Agente:")[1].replace("###", ""));
					}
					if (newcontenido.split("\n")[i].contains("Moneda:")
							&& newcontenido.split("\n")[i].contains("Prima Neta:")) {
						modelo.setMoneda(fn.moneda(
								newcontenido.split("\n")[i].split("Moneda:")[1].split("Prima")[0].replace("###", "")));
						modelo.setPrimaneta(fn.castBigDecimal(
								fn.castDouble(newcontenido.split("\n")[i].split("Neta:")[1].replace("###", ""))));
					}
					if (newcontenido.split("\n")[i].contains("Forma Pago:")
							&& newcontenido.split("\n")[i].contains("Fraccionado:")) {
						modelo.setFormaPago(fn.formaPago(
								newcontenido.split("\n")[i].split("Pago:")[1].split("Recargo")[0].replace("###", "")));
						modelo.setRecargo(fn.castBigDecimal(fn
								.castDouble(newcontenido.split("\n")[i].split("Fraccionado:")[1].replace("###", ""))));
					}
					if (newcontenido.split("\n")[i].contains("Recibo:")
							&& newcontenido.split("\n")[i].contains("Expedición:")) {
						modelo.setDerecho(fn.castBigDecimal(
								fn.castDouble(newcontenido.split("\n")[i].split(ConstantsValue.EXPEDICION2)[1].replace("###", ""))));
					}
					if (newcontenido.split("\n")[i].contains("IVA:")) {
						modelo.setIva(fn.castBigDecimal(
								fn.castDouble(newcontenido.split("\n")[i].split("IVA:")[1].replace("###", ""))));
					}

					if (newcontenido.split("\n")[i].contains("PLAN:")) {
						modelo.setPlan(newcontenido.split("\n")[i].split("PLAN:")[1].replace("###", ""));
					}
					if (newcontenido.split("\n")[i].contains("CP")) {
						modelo.setCp(newcontenido.split("\n")[i].split("CP")[1].replace("###", ""));
					}
					if (newcontenido.split("\n")[i].contains("Total a pagar:")) {
						modelo.setPrimaTotal(fn.castBigDecimal(fn.castDouble(
								newcontenido.split("\n")[i].split("Total a pagar:")[1].replace("###", ""))));
					}
				}
			}

			// sa
			// deducible
			// coaseguro
			// coaseguro_tope
			donde = fn.recorreContenido(contenido, "Límites de cobertura");
			if (donde > 0 && contenido.trim().split("@@@")[donde].split("\r\n").length > 0) {
				for (String dato : contenido.trim().split("@@@")[donde].split("\r\n")) {
					if (dato.split("###").length == 2) {
						if (dato.contains("asegurada")) {
							modelo.setSa(dato.trim().split("###")[1].trim());
						}
						if (dato.contains("Deducible por enfermedad")) {
							modelo.setDeducible(dato.trim().split("###")[1].trim());
						}
						if (dato.contains("Coaseguro por enfermedad")) {
							modelo.setCoaseguro(dato.trim().split("###")[1].trim());
						}
						if (dato.contains("Tope coaseguro")) {
							modelo.setCoaseguroTope(dato.split("###")[1].trim());
						}
					}
				}

			}

			// agente
			donde = 0;
			donde = fn.recorreContenido(contenido, "Control Admon:###");
			if (donde == 0) {
				donde = fn.searchTwoTexts(contenido, "Agente:", "Renueva");
			}
			if (donde > 0) {
				for (String dato : contenido.split("@@@")[donde].split("\r\n")) {
					if (dato.trim().contains("gente:")) {
						modelo.setAgente(dato.substring(dato.trim().indexOf("gente:") + 6, dato.trim().length())
								.replace("###", " ").trim());
					}
				}
			}

			// 1er_prima_total
			inicio = contenido.indexOf("1er.Recibo:");
			if (inicio > -1) {
				newcontenido = fn.gatos(contenido.substring(inicio + 11, inicio + 100).split("\n")[0].trim());
				if (newcontenido.contains("Gastos")) {
					newcontenido = fn.cleanString(fn.gatos(newcontenido.split("Gastos")[0].trim()));
					if (fn.isNumeric(newcontenido)) {
						modelo.setPrimerPrimatotal(fn.castBigDecimal(fn.castDouble(newcontenido)));
					}
				}
			}

			// sub_prima_total
			inicio = contenido.indexOf("Recibo Subsecuente:");
			if (inicio > -1) {
				newcontenido = fn.gatos(contenido.substring(inicio + 19, inicio + 100).split("\n")[0].trim());
				if (newcontenido.split("###").length == 1) {
					newcontenido = fn.cleanString(newcontenido);
					if (fn.isNumeric(newcontenido)) {
						modelo.setSubPrimatotal(fn.castBigDecimal(fn.castDouble(newcontenido)));
					}
				}
			}
			
		
				inicio = contenido.indexOf("Límites de cobertura");
				fin = contenido.indexOf("Base de catálogo");

				fin = fin == -1 ? contenido.indexOf("Seguros Atlas, S.A"): fin;

			
				if(inicio > -1 && fin >  -1 && inicio < fin ) {	
					List<EstructuraCoberturasModel> coberturas = new ArrayList<>();
					 newcontenido =contenido.substring(inicio, fin).replace("@@@", "").replace("\r", "");
					 for (int i = 0; i < newcontenido.split("\n").length; i++) {
						 EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();
						if(newcontenido.split("\n")[i].contains("Gastos médico")) {
								cobertura.setNombre(newcontenido.split("\n")[i].split("###")[0]);
								cobertura.setSa(newcontenido.split("\n")[i].split("###")[1].replace("según anexo", "").trim());
								coberturas.add(cobertura);
														
						}
						if(newcontenido.split("\n")[i].contains("GASTOS MÉDICOS")) {
								cobertura.setNombre(newcontenido.split("\n")[i].split("###")[0]);
								cobertura.setSa(newcontenido.split("\n")[i].split("###")[1].replace("según anexo", "").trim());
								coberturas.add(cobertura);
														
						}
						if(!newcontenido.split("\n")[i].contains("Límites") && !newcontenido.split("\n")[i].contains("Gastos médicos") && newcontenido.split("\n")[i].contains("Suma asegurada")) {                       
                            		cobertura.setNombre("Basica");
                            		cobertura.setSa(newcontenido.split("\n")[i].split("###")[1]);
                            		cobertura.setDeducible(newcontenido.split("\n")[i+1].split("###")[1]);
                            		cobertura.setCoaseguro(newcontenido.split("\n")[i+3].split("###")[1]);
                            		coberturas.add(cobertura);
                              
						}
					}	
				 modelo.setCoberturas(coberturas);
			}			
			return modelo;
		} catch (Exception ex) {
			modelo.setError(AtlasSaludModel.this.getClass().getTypeName() + " - catch:" + ex.getMessage() + " | "
					+ ex.getCause());
			return modelo;
		}

	}

}
