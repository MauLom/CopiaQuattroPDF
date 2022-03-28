package com.copsis.models.axa;

import java.util.ArrayList;
import java.util.List;

import com.copsis.constants.ConstantsValue;
import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraCoberturasModel;
import com.copsis.models.EstructuraJsonModel;

public class AxaAutos3Model {
	// Clases
	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();

	private String contenido;

	public AxaAutos3Model(String contenido) {
		this.contenido = contenido;
	}

	public EstructuraJsonModel procesar() {
		int inicio;
		int fin;
		String newcontenido;
		contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());
		try {
			// tipo
			modelo.setTipo(1);
			// cia
			modelo.setCia(20);

			inicio = contenido.indexOf("CARÁTULA DE PÓLIZA");
			fin = contenido.indexOf("Datos del Vehículo");

			if (inicio > 0 && fin > 0 && inicio < fin) {
				newcontenido = contenido.substring(inicio, fin).replace("@@@", "").replace("\r", "");

				for (int i = 0; i < newcontenido.split("\n").length; i++) {

					if (newcontenido.split("\n")[i].contains("Moneda:")
							&& newcontenido.split("\n")[i].contains(ConstantsValue.POLIZA_ACENT2)) {
						modelo.setMoneda(fn.moneda(newcontenido.split("\n")[i].split("Moneda:")[1].split("Póliza:")[0]
								.replace("###", "").strip()));
						modelo.setPoliza(newcontenido.split("\n")[i].split("Póliza:")[1].replace("###", "").strip());
					}
					if (newcontenido.split("\n")[i].contains("Nombre:") && newcontenido.split("\n")[i].contains("Edad:")
							&& newcontenido.split("\n")[i].contains("R.F.C:")) {
						modelo.setCteNombre(newcontenido.split("\n")[i].split("Nombre:")[1].split("Edad:")[0]
								.replace("###", "").strip());
						modelo.setRfc(newcontenido.split("\n")[i].split("R.F.C:")[1].replace("###", "").strip());
					}else if(newcontenido.split("\n")[i].split("Nombre:").length>1) {
						modelo.setCteNombre(fn.elimgatos(newcontenido.split("\n")[i].split("Nombre:")[1].trim()).split("###")[0].strip());
					}
					if (newcontenido.split("\n")[i].contains("Domicilio:")) {
						String direccion = newcontenido.split("\n")[i].split("Domicilio:")[1].replace("###", "").strip() + " "
								+ newcontenido.split("\n")[i + 1].replace("###", "").strip();
						if(!newcontenido.split("\n")[i + 2].contains("R.Tel") &&  !newcontenido.split("\n")[i + 2].contains("Datos")) {
							direccion+= " "+newcontenido.split("\n")[i + 2];
						}
						direccion = fn.eliminaSpacios(direccion.replace("I.D:", "").replace("U.A:", "").replace("###", "")).strip();
						modelo.setCteDireccion(direccion);
						//.replace("I.D:", "").replace("U.A:", "")
						direccion = direccion.replace(" C P:", "C/P:").replace("C.P", "C/P");
						String cp = direccion.split("C/P")[1].replace(":", "").trim().split(" ")[0];
						if(fn.isNumeric(cp)) {
							modelo.setCp(cp);
						}
						
					}
				}
			}
			inicio = contenido.indexOf("Datos del Vehículo");
			fin = contenido.indexOf("Datos de la Póliza");

			if (inicio > 0 && fin > 0 && inicio < fin) {
				newcontenido = contenido.substring(inicio, fin).replace("@@@", "").replace("\r", "");
				for (int i = 0; i < newcontenido.split("\n").length; i++) {
					if (newcontenido.split("\n")[i].contains("Vehículo:")) {
						modelo.setDescripcion(
								newcontenido.split("\n")[i].split("Vehículo:")[1].replace("###", "").strip());
					}
					if (newcontenido.split("\n")[i].contains("Motor:")
							&& newcontenido.split("\n")[i].contains(ConstantsValue.MODELO)) {
						modelo.setMotor(newcontenido.split("\n")[i].split("Motor:")[1].split("Modelo:")[0]
								.replace("###", "").strip());
						modelo.setModelo(Integer
								.parseInt(newcontenido.split("\n")[i].split("Modelo:")[1].replace("###", "").strip()));
					}
					if (newcontenido.split("\n")[i].contains("Serie:")
							&& newcontenido.split("\n")[i].contains("Capacidad:")) {
						modelo.setSerie(newcontenido.split("\n")[i].split("Serie:")[1].split("Capacidad:")[0]
								.replace("###", "").strip());
					}
					if (newcontenido.split("\n")[i].contains("Placas:")
							&& newcontenido.split("\n")[i].contains("Carga:")) {
						modelo.setPlacas(newcontenido.split("\n")[i].split("Placas:")[1].split("Carga:")[0]
								.replace("###", "").strip());
					}

				}
			}

			inicio = contenido.indexOf("Datos Adicionales");
			fin = contenido.indexOf(ConstantsValue.PRIMA_TOTAL);

			if (inicio > 0 && fin > 0 && inicio < fin) {
				newcontenido = contenido.substring(inicio, fin).replace("@@@", "").replace("\r", "");
				for (int i = 0; i < newcontenido.split("\n").length; i++) {
					if (newcontenido.split("\n")[i].contains(ConstantsValue.AGENTE)
							&& newcontenido.split("\n")[i].contains(ConstantsValue.PRIMA_NETA3)) {
						modelo.setCveAgente(newcontenido.split("\n")[i].split("Agente:")[1].split("###")[1]
								.replace("###", "").strip());
						modelo.setAgente(newcontenido.split("\n")[i].split("Agente:")[1].split("###")[2]
								.replace("###", "").strip());
					}

				}
			}

			inicio = contenido.indexOf("Datos de la Póliza");
			fin = contenido.indexOf("Datos Adicionales");
			if (inicio > 0 && fin > 0 && inicio < fin) {
				newcontenido = contenido.substring(inicio, fin).replace("@@@", "").replace("\r", "")
						.replace("a las 12 hrs. del", "").replace("Vigencia :", ConstantsValue.VIGENCIA);
				for (int i = 0; i < newcontenido.split("\n").length; i++) {

					if (newcontenido.split("\n")[i].contains(ConstantsValue.VIGENCIA)
							&& newcontenido.split("\n")[i].contains("al:")) {
						modelo.setVigenciaDe(fn
								.formatDateMonthCadena(newcontenido.split("\n")[i].split("Vigencia:")[1].split("al:")[0]
										.replace("###", "").replace("  ", "").strip()));
						modelo.setVigenciaA(fn
								.formatDateMonthCadena(newcontenido.split("\n")[i].split("Vigencia:")[1].split("al:")[1]
										.replace("###", "").replace("  ", "").strip()));
					}
					if (newcontenido.split("\n")[i].contains("Forma de Pago:")) {
						modelo.setFormaPago(fn.formaPago(
								newcontenido.split("\n")[i].split("Forma de Pago:")[1].replace("###", "").strip()));
					}
					if (newcontenido.split("\n")[i].contains("Fecha de Emisión:")) {
						modelo.setFechaEmision(fn.formatDateMonthCadena(
								newcontenido.split("\n")[i].split("Fecha de Emisión:")[1].split("Póliza Ant:")[0]
										.replace("###", "").replace("  ", "").strip()));
					}
				}
			}

			inicio = contenido.indexOf(ConstantsValue.PRIMA_NETA3);
			fin = contenido.indexOf("AXA Seguros, S.A.");

			if (inicio > 0 && fin > 0 && inicio < fin) {
				newcontenido = contenido.substring(inicio, fin).replace("@@@", "").replace("\r", "");
				for (int i = 0; i < newcontenido.split("\n").length; i++) {

					if (newcontenido.split("\n")[i].contains("Prima Neta:")) {
						modelo.setPrimaneta(fn.castBigDecimal(fn.cleanString(
								newcontenido.split("\n")[i].split("Prima Neta:")[1].split("###")[1].trim())));
					}
					if (newcontenido.split("\n")[i].contains("Financiamiento")) {
						modelo.setRecargo(fn.castBigDecimal(fn.cleanString(
								newcontenido.split("\n")[i].split("Financiamiento")[1].split("###")[1].trim())));
					}
					if (newcontenido.split("\n")[i].contains("Expedición")) {
						modelo.setDerecho(fn.castBigDecimal(fn.cleanString(
								newcontenido.split("\n")[i].split("Expedición:")[1].split("###")[1].trim())));
					}
					if (newcontenido.split("\n")[i].contains("I.V.A:")) {
						modelo.setIva(fn.castBigDecimal(
								fn.cleanString(newcontenido.split("\n")[i].split("I.V.A:")[1].split("###")[1].trim())));
					}
					if (newcontenido.split("\n")[i].contains("Prima Total:")) {
						modelo.setPrimaTotal(fn.castBigDecimal(fn.cleanString(
								newcontenido.split("\n")[i].split("Prima Total:")[1].split("###")[1].trim())));
					}
				}
			}

			inicio = contenido.indexOf("Coberturas Amparadas");
			fin = contenido.indexOf("PRIMA NETA");

			if (inicio > 0 && fin > 0 && inicio < fin) {
				List<EstructuraCoberturasModel> coberturas = new ArrayList<>();
				newcontenido = contenido.substring(inicio, fin).replace("@@@", "").replace("\r", "");
				for (int i = 0; i < newcontenido.split("\n").length; i++) {
					EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();
					if (!newcontenido.split("\n")[i].contains("Deducible")) {
						int sp = newcontenido.split("\n")[i].split("###").length;
						if(sp == 4) {
							cobertura.setNombre(newcontenido.split("\n")[i].split("###")[0].trim());
							cobertura.setSa(newcontenido.split("\n")[i].split("###")[1].trim());
							if (newcontenido.split("\n")[i].split("###")[2].contains("%")) {
								cobertura.setDeducible(newcontenido.split("\n")[i].split("###")[2].trim());
							}
							coberturas.add(cobertura);
						}else if(sp==3) {
							cobertura.setNombre(newcontenido.split("\n")[i].split("###")[0].trim());
							cobertura.setSa(newcontenido.split("\n")[i].split("###")[1].trim());
							coberturas.add(cobertura);
						}
						
					} 				}
				modelo.setCoberturas(coberturas);
			}

			return modelo;
		} catch (Exception ex) {
			modelo.setError(
					AxaAutos3Model.this.getClass().getTypeName() + " | " + ex.getMessage() + " | " + ex.getCause());
			return modelo;
		}
	}

}
