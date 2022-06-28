package com.copsis.models.atlas;

import java.util.ArrayList;
import java.util.List;

import com.copsis.constants.ConstantsValue;
import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraCoberturasModel;
import com.copsis.models.EstructuraJsonModel;

public class AtlasDiversosModel {
	// Clases
	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
	// Variables
	private String contenido = "";
	

	public AtlasDiversosModel(String contenido) {
		this.contenido = contenido;
	}

	public EstructuraJsonModel procesar() {
		String newcontenido = "";
		String resultado = "";
		int inicio = 0;
		int fin = 0;
		contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());
		contenido = contenido.replace("las 12:00 Hrs.del", "");
		try {
			// tipo
			modelo.setTipo(7);

			// cia
			modelo.setCia(33);

			// Datos del Contrantante
			inicio = contenido.indexOf("PÓLIZA");
			fin = contenido.indexOf(ConstantsValue.SECCIONES);

			if (inicio > 0 && fin > 0 && inicio < fin) {
				newcontenido = contenido.substring(inicio, fin).replace("\r", "").replace("@@@", "").trim();
				for (int i = 0; i < newcontenido.split("\n").length; i++) {
					if (newcontenido.split("\n")[i].contains("Póliza")) {
						modelo.setPolizaGuion(newcontenido.split("\n")[i].split("Póliza")[1].replace("###", "").replace(":", "").trim());
						modelo.setPoliza(newcontenido.split("\n")[i].split("Póliza:")[1].replace("###", "")
								.replace("-", "").replace(" ", ""));
					}
					if (newcontenido.split("\n")[i].contains("desde:") && newcontenido.split("\n")[i].contains(ConstantsValue.HASTA)) {
						modelo.setVigenciaDe(
								fn.formatDateMonthCadena(newcontenido.split("\n")[i].split("desde:")[1].split(ConstantsValue.HASTA)[0]
										.replace("###", "").trim()));
						modelo.setVigenciaA(
								fn.formatDateMonthCadena(newcontenido.split("\n")[i].split(ConstantsValue.HASTA)[1].split("Fecha")[0]
										.replace("###", "").trim()));
						modelo.setFechaEmision(fn.formatDateMonthCadena(
								newcontenido.split("\n")[i].split("expedición:")[1].replace("###", "").trim()));
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

						modelo.setCveAgente(newcontenido.split("\n")[i].split(ConstantsValue.AGENTE)[1].replace("###", ""));
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
								fn.castDouble(newcontenido.split("\n")[i].split("Expedición:")[1].replace("###", ""))));
					}
					if (newcontenido.split("\n")[i].contains("IVA:")) {
						modelo.setIva(fn.castBigDecimal(
								fn.castDouble(newcontenido.split("\n")[i].split("IVA:")[1].replace("###", ""))));
					}
					if (newcontenido.split("\n")[i].contains("PLAN:")) {
						modelo.setPlan(newcontenido.split("\n")[i].split("PLAN:")[1].replace("###", ""));
					}
					if (newcontenido.split("\n")[i].contains("CP")) {
						modelo.setCp(
								newcontenido.split("\n")[i].split("CP")[1].replace(":", "").replace("###", "").trim());
					}
					if (newcontenido.split("\n")[i].contains("Total a pagar:")) {
						modelo.setPrimaTotal(fn.castBigDecimal(fn.castDouble(
								newcontenido.split("\n")[i].split("Total a pagar:")[1].replace("###", ""))));
					}
				}
			}
			inicio = contenido.indexOf("Renueva a:");
			fin = contenido.indexOf("Seguros Atlas");
			if (inicio > 0 && fin > 0 && inicio < fin) {
				newcontenido = contenido.substring(inicio, fin);
				inicio = newcontenido.indexOf(ConstantsValue.AGENTE);
				if (inicio > 0) {
					modelo.setAgente(newcontenido.split(ConstantsValue.AGENTE)[1].replace("###", "").trim());
				}
			}

			/**
			 * Coberturas
			 */
			inicio = contenido.indexOf(ConstantsValue.SECCIONES);
			fin = contenido.indexOf("Idaseg");
	
			if (inicio > 0 && fin > 0 && inicio < fin) {
				List<EstructuraCoberturasModel> coberturas = new ArrayList<>();
				newcontenido = contenido.substring(inicio, fin);
				

				for (String txt : newcontenido.split("\r\n")) {			
					EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();
					int sp = txt.split("###").length;
					if (!txt.contains(ConstantsValue.SECCIONES)) {	
						if(sp ==2 || sp == 3) {
							cobertura.setNombre(txt.split("###")[0].replace("@@@", ""));
							cobertura.setSa(txt.split("###")[1]);
							coberturas.add(cobertura);
						}
					}
				}
				modelo.setCoberturas(coberturas);
			}

			return modelo;
		} catch (

		Exception ex) {
			modelo.setError(AtlasDiversosModel.this.getClass().getTypeName() + " - catch:" + ex.getMessage() + " | "
					+ ex.getCause());
			return modelo;
		}

	}

}
