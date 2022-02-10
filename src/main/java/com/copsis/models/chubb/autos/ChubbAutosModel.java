package com.copsis.models.chubb.autos;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.math.NumberUtils;

import com.copsis.constants.ConstantsValue;
import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraCoberturasModel;
import com.copsis.models.EstructuraJsonModel;
import com.copsis.models.EstructuraRecibosModel;

import lombok.Data;

@Data
public class ChubbAutosModel {
	// Clases
	DataToolsModel fn = new DataToolsModel();
	EstructuraJsonModel modelo = new EstructuraJsonModel();

	// Variables
	private String contenido;
	private String recibos = "";
	private int inicio = 0;
	private int fin = 0;
	private String newcontenido = "";
	private BigDecimal restoPrimaTotal = BigDecimal.ZERO;
	private BigDecimal restoDerecho = BigDecimal.ZERO;
	private BigDecimal restoIva = BigDecimal.ZERO;
	private BigDecimal restoRecargo = BigDecimal.ZERO;
	private BigDecimal restoPrimaNeta = BigDecimal.ZERO;
	private BigDecimal restoAjusteUno = BigDecimal.ZERO;
	private BigDecimal restoAjusteDos = BigDecimal.ZERO;
	private BigDecimal restoCargoExtra = BigDecimal.ZERO;
	private List<String> conceptos;
	private String separador = "###";
	private String saltolinea = "\r\n";

	public EstructuraJsonModel procesar() {
		try {

			// Variables
			/*
			 * String F_DEPAGO; BigDecimal r_prima_neta; BigDecimal r_recargo; BigDecimal
			 * r_derecho; BigDecimal r_iva; BigDecimal r_prima_total; BigDecimal r_ajuste2;
			 * BigDecimal r_ajuste1; int numero_recibo;
			 */

			/* int donde = 0; */
			// String resultado = "";
			/*
			 * float restoPrimaTotal = 0; float restoDerecho = 0; float restoIva = 0; float
			 * restoRecargo = 0; float restoPrimaNeta = 0; float restoAjusteUno = 0; float
			 * restoAjusteDos = 0; float restoCargoExtra = 0;
			 */
			List<String> conceptosFin;

			contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());
			contenido = contenido.replace("Prima neta", ConstantsValue.PRIMA_NETA).replace("Prima total",
					ConstantsValue.PRIMA_TOTAL2);

			// tipo
			modelo.setTipo(1);
			// aseguradora
			modelo.setCia(1);

			// Ramo
			modelo.setRamo("Autos");

			// Moneda
			modelo.setMoneda(fn.moneda(
					contenido.split("Moneda:")[1].split(ConstantsValue.FORMA_PAGO)[0].replace("###", "").trim()));

			// Renovacion
			conceptos = Arrays.asList("Póliza anterior:###");
			for (String x : conceptos) {
				inicio = contenido.indexOf(x);
				if (inicio > -1 && x.equals("Póliza anterior:###")) {
					inicio = inicio + 19;
					newcontenido = contenido.substring(inicio, (inicio + 100));
					modelo.setRenovacion(newcontenido.split("Moneda")[0].replace("###", "").trim());
				}
			}

			// Plan
			conceptos = Arrays.asList("Paquete:###");
			for (String x : conceptos) {
				inicio = contenido.indexOf(x);
				if (inicio > -1 && x.equals("Paquete:###")) {
					inicio = inicio + 11;
					newcontenido = contenido.substring(inicio, (inicio + 100));
					modelo.setPlan(newcontenido.split(saltolinea)[0].trim());
				}
			}

			// FormaPago
			conceptos = Arrays.asList("Forma de pago:");
			for (String x : conceptos) {
				inicio = contenido.indexOf(x);
				if (inicio > -1 && x.equals("Forma de pago:")) {
					inicio = inicio + 14;
					newcontenido = fn.gatos(contenido.substring(inicio, (inicio + 100)).split(saltolinea)[0])
							.split(separador)[0];
					modelo.setFormaPago(fn.formaPago(newcontenido));
				}
			}
			if (modelo.getFormaPago() == 0) {

				modelo.setFormaPago(fn.formaPagoSring(newcontenido));
			}

			// poliza
			conceptos = Arrays.asList("Póliza:###");
			for (String x : conceptos) {
				inicio = contenido.indexOf(x);
				if (inicio > -1 && x.equals("Póliza:###")) {
					inicio = inicio + 10;
					newcontenido = contenido.substring(inicio, (inicio + 100));
					modelo.setPoliza(
							newcontenido.split(separador)[0].trim() + "" + newcontenido.split(separador)[1].trim());
				}
			}

			// endoso
			conceptos = Arrays.asList("Endoso:###");
			for (String x : conceptos) {
				inicio = contenido.indexOf(x);
				if (inicio > -1 && x.equals("Endoso:###")) {
					inicio = inicio + 10;
					newcontenido = contenido.substring(inicio, (inicio + 100));
					modelo.setEndoso(newcontenido.split(separador)[0].trim());

				}
			}

			// Contratante
			conceptos = Arrays.asList("Propietario-Contratante:###");
			for (String x : conceptos) {
				inicio = contenido.indexOf(x);
				if (inicio > -1 && x.equals("Propietario-Contratante:###")) {
					inicio = inicio + 27;
					newcontenido = contenido.substring(inicio, (inicio + 150));
					modelo.setContratante(newcontenido.split(saltolinea)[0].trim());
				}
			}

			// CteNombre
			conceptos = Arrays.asList("Datos del asegurado y-o propietario");
			for (String x : conceptos) {
				inicio = contenido.indexOf(x);
				if (inicio > -1 && x.equals("Datos del asegurado y-o propietario")) {
					inicio = inicio + 35;
					newcontenido = contenido.substring(inicio, (inicio + 150));
					modelo.setCteNombre(newcontenido.split(saltolinea)[1].split(separador)[1].trim());
				}
			}

			// CteDireccion
			conceptos = Arrays.asList("Domicilio:###");
			for (String x : conceptos) {
				inicio = contenido.indexOf(x);
				if (inicio > -1 && x.equals("Domicilio:###")) {
					inicio = inicio + 13;
					newcontenido = contenido.substring(inicio, (inicio + 100));
					modelo.setCteDireccion(newcontenido.split(separador)[0].trim());
				}
			}

			// rfc
			conceptos = Arrays.asList("R.F.C.:", "RFC:###","R.F.C:###");
			for (String x : conceptos) {
				inicio = contenido.indexOf(x);
				if (inicio > -1) {
					switch (x) {
					case "R.F.C.:":
					case "RFC:###":
					case "R.F.C:###":
						inicio = inicio + 7;
						fin = (inicio + 100 )< contenido.length() ? (inicio + 100 ) : (inicio +50);
						newcontenido = contenido.substring(inicio, (inicio + 100)).split(saltolinea)[0];
						modelo.setRfc(newcontenido.split(separador)[0].replace("#", "").trim());
						break;
					default:
						break;
					}
				}
			}

			// Inciso
			conceptos = Arrays.asList("Inciso:###");
			for (String x : conceptos) {
				inicio = contenido.indexOf(x);
				if (inicio > -1 && x.equals("Inciso:###")) {
					inicio = inicio + 10;
					newcontenido = contenido.substring(inicio, (inicio + 100));
					if (NumberUtils.isParsable(newcontenido.split(separador)[0].trim())) {
						modelo.setInciso(Integer.parseInt(newcontenido.split(separador)[0].trim()));
					}
				}
			}

			// Primaneta
			conceptos = Arrays.asList("Prima Neta###");
			for (String x : conceptos) {
				inicio = contenido.indexOf(x);
				if (inicio > -1 && x.equals("Prima Neta###")) {
					inicio = inicio + 13;
					newcontenido = contenido.substring(inicio, (inicio + 100));
					if (NumberUtils.isParsable(fn.preparaPrimas(newcontenido.split(saltolinea)[0].trim()))) {
						modelo.setPrimaneta(
								fn.castBigDecimal(fn.preparaPrimas(newcontenido.split(saltolinea)[0].trim())));
					}
				}
			}

			// PrimaTotal
			conceptos = Arrays.asList("Prima Total");
			for (String x : conceptos) {
				inicio = contenido.indexOf(x);
				if (inicio > -1 && x.equals("Prima Total")) {
					inicio = inicio + 11;
					fin = (inicio + 100) < contenido.length() ? (inicio + 100) : (inicio + 20);
					newcontenido = fn.gatos(contenido.substring(inicio, fin).replace(":", ""));
					if (NumberUtils.isParsable(fn.preparaPrimas(newcontenido.split(saltolinea)[0].trim()))) {
						modelo.setPrimaTotal(
								fn.castBigDecimal(fn.preparaPrimas(newcontenido.split(saltolinea)[0].trim())));
					}
				}
			}

			// Iva
			conceptos = Arrays.asList(ConstantsValue.IVA);
			for (String x : conceptos) {
				inicio = contenido.indexOf(x);
				if (inicio > -1 && x.equals(ConstantsValue.IVA)) {
					inicio = inicio + 6;
					fin = (inicio + 100) < contenido.length() ? (inicio + 100) : (inicio + 20);
					newcontenido = fn.gatos(contenido.substring(inicio, fin));
					if (NumberUtils.isParsable(fn.preparaPrimas(newcontenido.split(saltolinea)[0].trim()))) {
						modelo.setIva(fn.castBigDecimal(fn.preparaPrimas(newcontenido.split(saltolinea)[0].trim())));
					}
				}
			}

			// CveAgente
			conceptos = Arrays.asList(ConstantsValue.CLAVE_INTERNA_AGENTE);
			for (String x : conceptos) {
				inicio = contenido.indexOf(x);
				if (inicio > -1 && x.equals(ConstantsValue.CLAVE_INTERNA_AGENTE)) {
					inicio = inicio + 25;
					fin = (inicio + 150) < contenido.length() ? (inicio + 150) : (inicio + 30);
					newcontenido = fn.gatos(contenido.substring(inicio, fin));
					modelo.setCveAgente(newcontenido.contains("-")
							? newcontenido.split("-")[0].replace("###Conducto:###0", "").replace("###", "").trim()
							: "");
				}
			}

			// Agente
			conceptos = Arrays.asList(ConstantsValue.CLAVE_INTERNA_AGENTE);
			for (String x : conceptos) {
				inicio = contenido.indexOf(x);
				fin = contenido.indexOf("Descripción del vehículo") > -1 ? contenido.indexOf("Descripción del vehículo") : (inicio + 150);
				if (inicio > -1 && x.equals(ConstantsValue.CLAVE_INTERNA_AGENTE)) {
					inicio = inicio + 25;
					newcontenido = fn.gatos(contenido.substring(inicio, fin));
					modelo.setAgente(
							newcontenido.split(saltolinea)[0].contains("-")
									? fn.eliminaSpacios(newcontenido.split("-")[newcontenido.split("-").length-1].replace("###", " ").replace("\r", " ").replace("\n", " ").replace("@", " ")).trim()//.replace("\r\r\n@@@"," ")
									: "");
				}
			}

			// VigenciaDe
			conceptos = Arrays.asList("Vigencia:###Del###");
			for (String x : conceptos) {
				inicio = contenido.indexOf(x);
				if (inicio > -1 && x.equals("Vigencia:###Del###")) {
					inicio = inicio + 18;
					fin = (inicio + 150) < contenido.length() ? (inicio + 150): (inicio + 100);
					newcontenido = contenido.substring(inicio, fin);
					modelo.setVigenciaDe(fn.formatDateMonthCadena(newcontenido.split(separador)[0]));
				}
			}

			// VigenciaA
			conceptos = Arrays.asList("horas al###");
			for (String x : conceptos) {
				inicio = contenido.indexOf(x);
				if (inicio > -1 && x.equals("horas al###")) {
					inicio = inicio + 11;
					fin = (inicio + 150) < contenido.length() ? (inicio + 150): (inicio + 100);
					newcontenido = contenido.substring(inicio, fin);
					modelo.setVigenciaA(fn.formatDateMonthCadena(newcontenido.split(separador)[0].trim()));
				}
			}
			// Cp
			conceptos = Arrays.asList("C.P:###");
			for (String x : conceptos) {
				inicio = contenido.indexOf(x);
				if (inicio > -1 && x.equals("C.P:###")) {
					inicio = inicio + 7;
					fin = (inicio + 150) < contenido.length() ? (inicio + 150): (inicio + 100);
					newcontenido = contenido.substring(inicio, fin);
					modelo.setCp(newcontenido.split(saltolinea)[0].trim());
				}
			}

			// Recargo
			conceptos = Arrays.asList("por pago fraccionado###");
			for (String x : conceptos) {
				inicio = contenido.indexOf(x);
				if (inicio > -1 && x.equals("por pago fraccionado###")) {
					inicio = inicio + 23;
					fin = (inicio + 150) < contenido.length() ? (inicio + 150) : (inicio + 130);
					newcontenido = contenido.substring(inicio, fin);
					if (NumberUtils.isParsable(fn.preparaPrimas(newcontenido.split(saltolinea)[0].trim()))) {
						modelo.setRecargo(
								fn.castBigDecimal(fn.preparaPrimas(newcontenido.split(saltolinea)[0].trim())));
					}
				}
			}

			// Derecho
			conceptos = Arrays.asList(ConstantsValue.GASTOS_DE_EXPEDICION);
			for (String x : conceptos) {
				inicio = contenido.indexOf(x);
				if (inicio > -1 && x.equals(ConstantsValue.GASTOS_DE_EXPEDICION)) {
					inicio = inicio + 20;
					int finIndex = (inicio+150) < contenido.length() ? (inicio+150) : (inicio+100);
					
					newcontenido = fn.gatos(contenido.substring(inicio, finIndex).split(saltolinea)[0])
							.split(separador)[0].split(ConstantsValue.IVA)[0];
					if (NumberUtils.isParsable(fn.preparaPrimas(newcontenido.split(saltolinea)[0].trim()))) {
						modelo.setDerecho(
								fn.castBigDecimal(fn.preparaPrimas(newcontenido.split(saltolinea)[0].trim())));
					}
				}
			}

			// Descripcion
			conceptos = Arrays.asList("Descripción del vehículo*:");
			for (String x : conceptos) {
				inicio = contenido.indexOf(x);
				if (inicio > -1 && x.equals("Descripción del vehículo*:")) {
					inicio = inicio + 26;
					fin = (inicio + 150) < contenido.length() ? (inicio + 150) : (inicio + 130);
					newcontenido = fn.gatos(contenido.substring(inicio, fin));
					modelo.setDescripcion(newcontenido.split(saltolinea)[0].trim().replace("###", " "));
				}
			}

			// Clave
			conceptos = Arrays.asList("Clave vehicular:");
			for (String x : conceptos) {
				inicio = contenido.indexOf(x);
				if (inicio > -1 && x.equals("Clave vehicular:")) {
					inicio = inicio + 16;
					fin = (inicio + 150) < contenido.length() ? (inicio + 150) : (inicio + 130);
					newcontenido = fn.gatos(contenido.substring(inicio, fin));
					modelo.setClave(newcontenido.split(separador)[0].trim());
				}
			}

			// Modelo
			conceptos = Arrays.asList("Modelo:");
			for (String x : conceptos) {
				inicio = contenido.indexOf(x);
				if (inicio > -1 && x.equals("Modelo:")) {
					inicio = inicio + 7;
					fin = (inicio + 150) < contenido.length() ? (inicio + 150) : (inicio + 130);
					newcontenido = fn.gatos(contenido.substring(inicio, fin));
					if (NumberUtils.isParsable(newcontenido.split(separador)[0].trim())) {
						modelo.setModelo(Integer.parseInt(newcontenido.split(separador)[0].trim()));
					}
				}
			}

			// Serie
			conceptos = Arrays.asList("Serie:");
			for (String x : conceptos) {
				inicio = contenido.indexOf(x);
				if (inicio > -1 && x.equals("Serie:")) {
					inicio = inicio + 6;
					fin = (inicio + 150) < contenido.length() ? (inicio + 150): (inicio + 100);
					newcontenido = fn.gatos(contenido.substring(inicio, fin));
					modelo.setSerie(newcontenido.split(saltolinea)[0].trim());
				}
			}

			// Motor
			conceptos = Arrays.asList("Motor:");
			for (String x : conceptos) {
				inicio = contenido.indexOf(x);
				if (inicio > -1 && x.equals("Motor:")) {
					inicio = inicio + 6;
					fin = (inicio + 150) < contenido.length() ? (inicio + 150): (inicio + 100);
					newcontenido = fn.gatos(contenido.substring(inicio, fin).split(saltolinea)[0]);
					modelo.setMotor(newcontenido);
				}
			}

			// Placas
			conceptos = Arrays.asList("Placas:");
			for (String x : conceptos) {
				inicio = contenido.indexOf(x);
				if (inicio > -1 && x.equals("Placas:")) {
					inicio = inicio + 7;
					fin = (inicio + 150) < contenido.length() ? (inicio + 150): (inicio + 100);
					newcontenido = fn.gatos(contenido.substring(inicio, fin).split(saltolinea)[0]);
					modelo.setPlacas(newcontenido);
				}
			}

			// FechaEmision
			conceptos = Arrays.asList("Fecha de emisión:###");
			for (String x : conceptos) {
				inicio = contenido.indexOf(x);
				if (inicio > -1 && x.equals("Fecha de emisión:###")) {
					inicio = inicio + 20;
					fin = (inicio + 150) < contenido.length() ? (inicio + 150): (inicio + 100);
					newcontenido = contenido.substring(inicio, fin);
					newcontenido = newcontenido.split(separador)[0].trim().replace(" DE ", "-");
					modelo.setFechaEmision(fn.formatDateMonthCadena(newcontenido));
				}
			}

			// Marca
			conceptos = Arrays.asList("Marca:");
			for (String x : conceptos) {
				inicio = contenido.indexOf(x);
				if (inicio > -1 && x.equals("Marca:")) {
					inicio = inicio + 6;
					newcontenido = fn.gatos(contenido.substring(inicio, (inicio + 150)));
					modelo.setMarca(newcontenido.split(separador)[0].trim());
				}
			}

			// IdCliente
			conceptos = Arrays.asList("Asegurado:###");
			for (String x : conceptos) {
				inicio = contenido.indexOf(x);
				if (inicio > -1 && x.equals("Asegurado:###")) {
					inicio = inicio + 13;
					newcontenido = contenido.substring(inicio, (inicio + 150));
					modelo.setIdCliente(newcontenido.split(separador)[0].trim());
				}
			}

			// AjusteUno
			conceptos = Arrays.asList("Otros descuentos###");
			for (String x : conceptos) {
				inicio = contenido.indexOf(x);
				if (inicio > -1 && x.equals("Otros descuentos###")) {
					inicio = inicio + 19;
					fin = (inicio + 150) < contenido.length() ? (inicio + 150): (inicio + 50);
					newcontenido = contenido.substring(inicio, fin));
					if (NumberUtils.isParsable(fn.preparaPrimas(newcontenido.split(saltolinea)[0].trim()))) {
						modelo.setAjusteUno(
								fn.castBigDecimal(fn.preparaPrimas(newcontenido.split(saltolinea)[0].trim())));
					}
				}
			}

			// coberturas
			List<EstructuraCoberturasModel> coberturas = new ArrayList<>();
			conceptos = Arrays.asList("Suma asegurada###Deducible###Prima");
			conceptosFin = Arrays.asList("@@@Prima Neta###");
			inicio = contenido.indexOf(conceptos.get(0));
			fin = contenido.indexOf(conceptosFin.get(0));
			if (inicio > -1 && fin > -1) {
				inicio = inicio + 34;
				newcontenido = contenido.substring(inicio, fin);
				int i = 0;
				for (String dato : newcontenido.split("\n")) {
					if (dato.split("###").length >= 3) {
						// Clases
						EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();
						cobertura.setNombre(dato.split("###")[0].trim());
						cobertura.setDeducible(dato.split("###")[2].trim());
						cobertura.setSa(dato.split("###")[1].trim());
						i++;
						cobertura.setIdx(i);
						coberturas.add(cobertura);
					}
				}

			}
			modelo.setCoberturas(coberturas);

			// recibos
			List<EstructuraRecibosModel> recibosList = new ArrayList<>();
	

			if (!recibos.equals("")) {
				recibosList = recibosExtract();
			}

			switch (modelo.getFormaPago()) {
			case 1:
				if (recibosList.isEmpty()) {
					EstructuraRecibosModel recibo = new EstructuraRecibosModel();
					recibo.setReciboId("");
					recibo.setSerie("1/1");
					recibo.setVigenciaDe(modelo.getVigenciaDe());
					recibo.setVigenciaA(modelo.getVigenciaA());
					if (recibo.getVigenciaDe().length() > 0) {
						recibo.setVencimiento(fn.dateAdd(recibo.getVigenciaDe(), 30, 1));
					}
					recibo.setPrimaneta(fn.castBigDecimal(modelo.getPrimaneta()));
					recibo.setDerecho(fn.castBigDecimal(modelo.getDerecho()));
					recibo.setRecargo(fn.castBigDecimal(modelo.getRecargo()));
					recibo.setIva(fn.castBigDecimal(modelo.getIva()));
					recibo.setPrimaTotal(fn.castBigDecimal(modelo.getPrimaTotal()));
					recibo.setAjusteUno(fn.castBigDecimal(modelo.getAjusteUno()));
					recibo.setAjusteDos(fn.castBigDecimal(modelo.getAjusteDos()));
					recibo.setCargoExtra(fn.castBigDecimal(modelo.getCargoExtra()));
					recibosList.add(recibo);
				}
				break;
			}
			modelo.setRecibos(recibosList);

			return modelo;
		} catch (Exception ex) {
			modelo.setError(
					ChubbAutosModel.this.getClass().getTypeName() + " | " + ex.getMessage() + " | " + ex.getCause());
			return modelo;
		}
	}

	private ArrayList<EstructuraRecibosModel> recibosExtract() {
		List<EstructuraRecibosModel> recibosLis = new ArrayList<>();
		try {
			recibos = fn.remplazarMultiple(recibos, fn.remplazosGenerales());

			int index = 0;
			int totalRec = fn.getTotalRec(modelo.getFormaPago());
			int recibosSerie = 1;

			ArrayList<String> series = new ArrayList<>();
			for (String a : recibos.split("AVISO DE COBRO")) {
				if (index > 0 && a.contains(ConstantsValue.RECIBO)) {
					EstructuraRecibosModel recibo = new EstructuraRecibosModel();

					String actualSerie = "";
					conceptos = Arrays.asList(ConstantsValue.RECIBO);
					for (String x : conceptos) {
						inicio = a.indexOf(x);
						if (inicio > -1 && x.equals(ConstantsValue.RECIBO)) {
							inicio = inicio + 7;
							newcontenido = fn.gatos(a.substring(inicio, (inicio + 150)).split(saltolinea)[0])
									.split(separador)[0];
							actualSerie = recibo.getReciboId();
						}
					}

					if (index == 1 || !series.contains(actualSerie)) {

						recibo.setSerie(actualSerie);
						recibo.setSerie(recibosSerie + "/" + totalRec);

						inicio = a.indexOf("No. De recibo:");
						fin = a.indexOf("Endoso:");
						if (inicio > -1 && fin > inicio) {
							newcontenido = fn.gatos(a.substring(inicio + 14, fin)).trim();

						}

						conceptos = Arrays.asList(ConstantsValue.VIGENCIA);
						for (String x : conceptos) {
							inicio = a.indexOf(x);
							if (inicio > -1 && x.equals(ConstantsValue.VIGENCIA)) {
								inicio = inicio + 9;
								newcontenido = fn
										.gatos(fn.gatos(a.substring(inicio, (inicio + 150)).split(saltolinea)[0])
												.split("12:")[0]);
								newcontenido = (newcontenido.split(separador).length > 1)
										? newcontenido.split(separador)[1]
										: "";
								recibo.setVigenciaDe(fn.formatDate(newcontenido, "dd-mm-yyyy"));
							}
						}

						for (String x : conceptos) {
							inicio = a.lastIndexOf(x);
							if (inicio > -1 && x.equals(ConstantsValue.VIGENCIA)) {
								inicio = inicio + 9;
								newcontenido = fn
										.gatos(a.substring(inicio, (inicio + 150)).split(saltolinea)[0].split("al")[1]
												.split("12:")[0]);
								recibo.setVigenciaA(fn.formatDate(newcontenido, "dd-mm-yyyy"));
							}
						}

						conceptos = Arrays.asList("Total a pagar:");
						for (String x : conceptos) {
							inicio = a.indexOf(x);
							if (inicio > -1 && x.equals("Total a pagar:")) {
								inicio = inicio + 14;
								newcontenido = fn.gatos(a.substring(inicio, (inicio + 150)).split(saltolinea)[0])
										.split(separador)[0];
								if (NumberUtils.isParsable(fn.preparaPrimas(newcontenido))) {
									recibo.setPrimaTotal(fn.castBigDecimal(fn.preparaPrimas(newcontenido)));
									restoPrimaTotal = fn.castBigDecimal(modelo.getPrimaTotal())
											.subtract(recibo.getPrimaTotal());
								}
							}
						}

						conceptos = Arrays.asList(ConstantsValue.GASTOS_DE_EXPEDICION);
						for (String x : conceptos) {
							inicio = a.indexOf(x);
							if (inicio > -1 && x.equals(ConstantsValue.GASTOS_DE_EXPEDICION)) {
								inicio = inicio + 20;
								newcontenido = fn.gatos(a.substring(inicio, (inicio + 150)).split(saltolinea)[0])
										.split(separador)[0];
								if (NumberUtils.isParsable(fn.preparaPrimas(newcontenido))) {
									recibo.setDerecho(fn.castBigDecimal(fn.preparaPrimas(newcontenido)));
									restoDerecho = fn.castBigDecimal(modelo.getDerecho()).subtract(recibo.getDerecho());
								}
							}
						}

						conceptos = Arrays.asList(ConstantsValue.IVA);
						for (String x : conceptos) {
							inicio = a.indexOf(x);
							if (inicio > -1 && x.equals(ConstantsValue.IVA)) {
								inicio = inicio + 6;
								newcontenido = fn.gatos(a.substring(inicio, (inicio + 150)).split(saltolinea)[0])
										.split(separador)[0].trim();
								if (NumberUtils.isParsable(fn.preparaPrimas(newcontenido))) {
									recibo.setIva(fn.castBigDecimal(fn.preparaPrimas(newcontenido)));
									restoIva = fn.castBigDecimal(modelo.getIva()).subtract(recibo.getIva());
								}
							}
						}

						conceptos = Arrays.asList("Financiamiento por pago fraccionado");
						for (String x : conceptos) {
							inicio = a.indexOf(x);
							if (inicio > -1 && x.equals("Financiamiento por pago fraccionado")) {
								inicio = inicio + 35;
								newcontenido = fn.gatos(a.substring(inicio, (inicio + 150)).split(saltolinea)[0])
										.split(separador)[0];
								if (NumberUtils.isParsable(fn.preparaPrimas(newcontenido))) {
									recibo.setRecargo(fn.castBigDecimal(fn.preparaPrimas(newcontenido)));
									restoRecargo = fn.castBigDecimal(modelo.getRecargo()).subtract(recibo.getRecargo());
								}
							}
						}

						conceptos = Arrays.asList("Prima Neta");
						for (String x : conceptos) {
							inicio = a.indexOf(x);
							if (inicio > -1 && x.equals("Prima Neta")) {
								inicio = inicio + 10;
								newcontenido = fn.gatos(a.substring(inicio, (inicio + 150)).split(saltolinea)[0])
										.split(separador)[0];
								if (NumberUtils.isParsable(fn.preparaPrimas(newcontenido))) {
									recibo.setPrimaneta(fn.castBigDecimal(fn.preparaPrimas(newcontenido)));
									restoPrimaNeta = fn.castBigDecimal(modelo.getPrimaneta())
											.subtract(recibo.getPrimaneta());
								}
							}
						}

						recibosLis.add(recibo);
						series.add(actualSerie);
					}
				}
				index++;
			}
			return (ArrayList<EstructuraRecibosModel>) recibosLis;
		} catch (Exception ex) {
			return (ArrayList<EstructuraRecibosModel>) recibosLis;
		}
	}
}
