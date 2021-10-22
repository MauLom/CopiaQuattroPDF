package com.copsis.models.chubb.autos;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.math.NumberUtils;

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
			contenido = contenido.replace("Prima neta", "Prima Neta").replace("Prima total", "Prima Total");

			// tipo
			modelo.setTipo(1);

			// aseguradora
			modelo.setCia(1);

			// Ramo
			modelo.setRamo("Autos");

			// Moneda
			modelo.setMoneda(
					fn.moneda(contenido.split("Moneda:")[1].split("Forma de pago:")[0].replace("###", "").trim()));

			// Renovacion
			conceptos = Arrays.asList("Póliza anterior:###");
			for (String x : conceptos) {
				inicio = contenido.indexOf(x);
				if (inicio > -1) {
					switch (x) {
					case "Póliza anterior:###":
						inicio = inicio + 19;
						newcontenido = contenido.substring(inicio, (inicio + 100));
						modelo.setRenovacion(newcontenido.split("Moneda")[0].replace("###", "").trim());
						break;
					}
				}
			}

			// Plan
			conceptos = Arrays.asList("Paquete:###");
			for (String x : conceptos) {
				inicio = contenido.indexOf(x);
				if (inicio > -1) {
					switch (x) {
					case "Paquete:###":
						inicio = inicio + 11;
						newcontenido = contenido.substring(inicio, (inicio + 100));
						modelo.setPlan(newcontenido.split(saltolinea)[0].trim());
						break;
					}
				}
			}

			// FormaPago
			conceptos = Arrays.asList("Forma de pago:");
			for (String x : conceptos) {
				inicio = contenido.indexOf(x);
				if (inicio > -1) {
					switch (x) {
					case "Forma de pago:":
						inicio = inicio + 14;
						newcontenido = fn.gatos(contenido.substring(inicio, (inicio + 100)).split(saltolinea)[0]).split(separador)[0];
						modelo.setFormaPago(fn.formaPago(newcontenido));
						break;
					}
				}
			}

			// poliza
			conceptos = Arrays.asList("Póliza:###");
			for (String x : conceptos) {
				inicio = contenido.indexOf(x);
				if (inicio > -1) {
					switch (x) {
					case "Póliza:###":
						inicio = inicio + 10;
						newcontenido = contenido.substring(inicio, (inicio + 100));
						modelo.setPoliza(newcontenido.split(separador)[0].trim() + ""
								+ newcontenido.split(separador)[1].trim());
						break;
					}
				}
			}

			// endoso
			conceptos = Arrays.asList("Endoso:###");
			for (String x : conceptos) {
				inicio = contenido.indexOf(x);
				if (inicio > -1) {
					switch (x) {
					case "Endoso:###":
						inicio = inicio + 10;
						newcontenido = contenido.substring(inicio, (inicio + 100));
						modelo.setEndoso(newcontenido.split(separador)[0].trim());
						break;
					}
				}
			}

			// Contratante
			conceptos = Arrays.asList("Propietario-Contratante:###");
			for (String x : conceptos) {
				inicio = contenido.indexOf(x);
				if (inicio > -1) {
					switch (x) {
					case "Propietario-Contratante:###":
						inicio = inicio + 27;
						newcontenido = contenido.substring(inicio, (inicio + 150));
						modelo.setContratante(newcontenido.split(saltolinea)[0].trim());
						break;
					}
				}
			}

			// CteNombre
			conceptos = Arrays.asList("Datos del asegurado y-o propietario");
			for (String x : conceptos) {
				inicio = contenido.indexOf(x);
				if (inicio > -1) {
					switch (x) {
					case "Datos del asegurado y-o propietario":
						inicio = inicio + 35;
						newcontenido = contenido.substring(inicio, (inicio + 150));
						modelo.setCteNombre(newcontenido.split(saltolinea)[1].split(separador)[1].trim());
						break;
					}
				}
			}

			// CteDireccion
			conceptos = Arrays.asList("Domicilio:###");
			for (String x : conceptos) {
				inicio = contenido.indexOf(x);
				if (inicio > -1) {
					switch (x) {
					case "Domicilio:###":
						inicio = inicio + 13;
						newcontenido = contenido.substring(inicio, (inicio + 100));
						modelo.setCteDireccion(newcontenido.split(separador)[0].trim());
						break;
					}
				}
			}

			// rfc
			conceptos = Arrays.asList("R.F.C.:", "RFC:###");
			for (String x : conceptos) {
				inicio = contenido.indexOf(x);
				if (inicio > -1) {
					switch (x) {
					case "R.F.C.:":
					case "RFC:###":
						inicio = inicio + 7;
						newcontenido = contenido.substring(inicio, (inicio + 100)).split(saltolinea)[0];
						modelo.setRfc(newcontenido.split(separador)[0].trim());
						break;
					}
				}
			}

			// Inciso
			conceptos = Arrays.asList("Inciso:###");
			for (String x : conceptos) {
				inicio = contenido.indexOf(x);
				if (inicio > -1) {
					switch (x) {
					case "Inciso:###":
						inicio = inicio + 10;
						newcontenido = contenido.substring(inicio, (inicio + 100));
						if (NumberUtils.isParsable(newcontenido.split(separador)[0].trim())) {
							modelo.setInciso(Integer.parseInt(newcontenido.split(separador)[0].trim()));
						}
						break;
					}
				}
			}

			// Primaneta
			conceptos = Arrays.asList("Prima Neta###");
			for (String x : conceptos) {
				inicio = contenido.indexOf(x);
				if (inicio > -1) {
					switch (x) {
					case "Prima Neta###":
						inicio = inicio + 13;
						newcontenido = contenido.substring(inicio, (inicio + 100));
						if (NumberUtils.isParsable(fn.preparaPrimas(newcontenido.split(saltolinea)[0].trim()))) {
							modelo.setPrimaneta(
									fn.castBigDecimal(fn.preparaPrimas(newcontenido.split(saltolinea)[0].trim())));
						}
						break;
					}
				}
			}

			// PrimaTotal
			conceptos = Arrays.asList("Prima Total");
			for (String x : conceptos) {
				inicio = contenido.indexOf(x);
				if (inicio > -1) {
					switch (x) {
					case "Prima Total":
						inicio = inicio + 11;
						newcontenido = fn.gatos(contenido.substring(inicio, (inicio + 100)).replace(":", ""));
						if (NumberUtils.isParsable(fn.preparaPrimas(newcontenido.split(saltolinea)[0].trim()))) {
							modelo.setPrimaTotal(
									fn.castBigDecimal(fn.preparaPrimas(newcontenido.split(saltolinea)[0].trim())));
						}
						break;
					}
				}
			}

			// Iva
			conceptos = Arrays.asList("I.V.A.");
			for (String x : conceptos) {
				inicio = contenido.indexOf(x);
				if (inicio > -1) {
					switch (x) {
					case "I.V.A.":
						inicio = inicio + 6;
						newcontenido = fn.gatos(contenido.substring(inicio, (inicio + 100)));
						if (NumberUtils.isParsable(fn.preparaPrimas(newcontenido.split(saltolinea)[0].trim()))) {
							modelo.setIva(fn.castBigDecimal(fn.preparaPrimas(newcontenido.split(saltolinea)[0].trim())));
						}
						break;
					}
				}
			}

			// CveAgente
			conceptos = Arrays.asList("Clave interna del agente:");
			for (String x : conceptos) {
				inicio = contenido.indexOf(x);
				if (inicio > -1) {
					switch (x) {
					case "Clave interna del agente:":
						inicio = inicio + 25;
						newcontenido = fn.gatos(contenido.substring(inicio, (inicio + 150)));
						modelo.setCveAgente(newcontenido.contains("-") ? newcontenido.split("-")[0].replace("###Conducto:###0", "").replace("###", "").trim() : "");
						break;
					}
				}
			}

			// Agente
			conceptos = Arrays.asList("Clave interna del agente:");
			for (String x : conceptos) {
				inicio = contenido.indexOf(x);
				if (inicio > -1) {
				
					switch (x) {
					case "Clave interna del agente:":
						inicio = inicio + 25;
						newcontenido = fn.gatos(contenido.substring(inicio, (inicio + 150)));
		
						
						modelo.setAgente(newcontenido.split(saltolinea)[0].contains("-")
								? newcontenido.split(saltolinea)[0].split("-")[newcontenido.split(saltolinea)[0].split("-").length -1].trim().replace("###", " ")
								: "");
						break;

					}
				}
			}

			// VigenciaDe
			conceptos = Arrays.asList("Vigencia:###Del###");
			for (String x : conceptos) {
				inicio = contenido.indexOf(x);
				if (inicio > -1) {
					switch (x) {
					case "Vigencia:###Del###":
						inicio = inicio + 18;
						newcontenido = contenido.substring(inicio, (inicio + 150));
						modelo.setVigenciaDe(fn.formatDate_MonthCadena(newcontenido.split(separador)[0]));
						break;
					}
				}
			}

			// VigenciaA
			conceptos = Arrays.asList("horas al###");
			for (String x : conceptos) {
				inicio = contenido.indexOf(x);
				if (inicio > -1) {
					switch (x) {
					case "horas al###":
						inicio = inicio + 11;
						newcontenido = contenido.substring(inicio, (inicio + 150));
						modelo.setVigenciaA(fn.formatDate_MonthCadena(newcontenido.split(separador)[0].trim()));
						break;
					}
				}
			}
			// Cp
			conceptos = Arrays.asList("C.P:###");
			for (String x : conceptos) {
				inicio = contenido.indexOf(x);
				if (inicio > -1) {
					switch (x) {
					case "C.P:###":
						inicio = inicio + 7;
						newcontenido = contenido.substring(inicio, (inicio + 150));
						modelo.setCp(newcontenido.split(saltolinea)[0].trim());
						break;
					}
				}
			}

			// Recargo
			conceptos = Arrays.asList("por pago fraccionado###");
			for (String x : conceptos) {
				inicio = contenido.indexOf(x);
				if (inicio > -1) {
					switch (x) {
					case "por pago fraccionado###":
						inicio = inicio + 23;
						newcontenido = contenido.substring(inicio, (inicio + 150));
						if (NumberUtils.isParsable(fn.preparaPrimas(newcontenido.split(saltolinea)[0].trim()))) {
							modelo.setRecargo(
									fn.castBigDecimal(fn.preparaPrimas(newcontenido.split(saltolinea)[0].trim())));
						}
						break;
					}
				}
			}

			// Derecho
			conceptos = Arrays.asList("Gastos de expedición");
			for (String x : conceptos) {
				inicio = contenido.indexOf(x);
				if (inicio > -1) {
					switch (x) {
					case "Gastos de expedición":
						inicio = inicio + 20;
						newcontenido = fn.gatos(contenido.substring(inicio, (inicio + 150)).split(saltolinea)[0])
								.split(separador)[0].split("I.V.A.")[0];
						if (NumberUtils.isParsable(fn.preparaPrimas(newcontenido.split(saltolinea)[0].trim()))) {
							modelo.setDerecho(
									fn.castBigDecimal(fn.preparaPrimas(newcontenido.split(saltolinea)[0].trim())));
						}
						break;
					}
				}
			}

			// Descripcion
			conceptos = Arrays.asList("Descripción del vehículo*:");
			for (String x : conceptos) {
				inicio = contenido.indexOf(x);
				if (inicio > -1) {
					switch (x) {
					case "Descripción del vehículo*:":
						inicio = inicio + 26;
						newcontenido = fn.gatos(contenido.substring(inicio, (inicio + 150)));
						modelo.setDescripcion(newcontenido.split(saltolinea)[0].trim().replace("###", " "));
						break;
					}
				}
			}

			// Clave
			conceptos = Arrays.asList("Clave vehicular:");
			for (String x : conceptos) {
				inicio = contenido.indexOf(x);
				if (inicio > -1) {
					switch (x) {
					case "Clave vehicular:":
						inicio = inicio + 16;
						newcontenido = fn.gatos(contenido.substring(inicio, (inicio + 150)));
						modelo.setClave(newcontenido.split(separador)[0].trim());
						break;
					}
				}
			}

			// Modelo
			conceptos = Arrays.asList("Modelo:");
			for (String x : conceptos) {
				inicio = contenido.indexOf(x);
				if (inicio > -1) {
					switch (x) {
					case "Modelo:":
						inicio = inicio + 7;
						newcontenido = fn.gatos(contenido.substring(inicio, (inicio + 150)));
						if (NumberUtils.isParsable(newcontenido.split(separador)[0].trim())) {
							modelo.setModelo(Integer.parseInt(newcontenido.split(separador)[0].trim()));
						}
						break;
					}
				}
			}

			// Serie
			conceptos = Arrays.asList("Serie:");
			for (String x : conceptos) {
				inicio = contenido.indexOf(x);
				if (inicio > -1) {
					switch (x) {
					case "Serie:":
						inicio = inicio + 6;
						newcontenido = fn.gatos(contenido.substring(inicio, (inicio + 150)));
						modelo.setSerie(newcontenido.split(saltolinea)[0].trim());
						break;
					}
				}
			}

			// Motor
			conceptos = Arrays.asList("Motor:");
			for (String x : conceptos) {
				inicio = contenido.indexOf(x);
				if (inicio > -1) {
					switch (x) {
					case "Motor:":
						inicio = inicio + 6;
						newcontenido = fn.gatos(contenido.substring(inicio, (inicio + 150)).split(saltolinea)[0]);
						modelo.setMotor(newcontenido);
						break;
					}
				}
			}

			// Placas
			conceptos = Arrays.asList("Placas:");
			for (String x : conceptos) {
				inicio = contenido.indexOf(x);
				if (inicio > -1) {
					switch (x) {
					case "Placas:":
						inicio = inicio + 7;
						newcontenido = fn.gatos(contenido.substring(inicio, (inicio + 150)).split(saltolinea)[0]);
						modelo.setPlacas(newcontenido);
						break;
					}
				}
			}

			// FechaEmision
			conceptos = Arrays.asList("Fecha de emisión:###");
			for (String x : conceptos) {
				inicio = contenido.indexOf(x);
				if (inicio > -1) {
					switch (x) {
					case "Fecha de emisión:###":
						inicio = inicio + 20;
						newcontenido = contenido.substring(inicio, (inicio + 150));
						newcontenido = newcontenido.split(separador)[0].trim().replace(" DE ", "-");
						modelo.setFechaEmision(fn.formatDate_MonthCadena(newcontenido));
						break;
					}
				}
			}

			// Marca
			conceptos = Arrays.asList("Marca:");
			for (String x : conceptos) {
				inicio = contenido.indexOf(x);
				if (inicio > -1) {
					switch (x) {
					case "Marca:":
						inicio = inicio + 6;
						newcontenido = fn.gatos(contenido.substring(inicio, (inicio + 150)));
						modelo.setMarca(newcontenido.split(separador)[0].trim());
						break;
					}
				}
			}

			// IdCliente
			conceptos = Arrays.asList("Asegurado:###");
			for (String x : conceptos) {
				inicio = contenido.indexOf(x);
				if (inicio > -1) {
					switch (x) {
					case "Asegurado:###":
						inicio = inicio + 13;
						newcontenido = contenido.substring(inicio, (inicio + 150));
						modelo.setIdCliente(newcontenido.split(separador)[0].trim());
						break;
					}
				}
			}

			// AjusteUno
			conceptos = Arrays.asList("Otros descuentos###");
			for (String x : conceptos) {
				inicio = contenido.indexOf(x);
				if (inicio > -1) {
					switch (x) {
					case "Otros descuentos###":
						inicio = inicio + 19;
						newcontenido = contenido.substring(inicio, (inicio + 150));
						if (NumberUtils.isParsable(fn.preparaPrimas(newcontenido.split(saltolinea)[0].trim()))) {
							modelo.setAjusteUno(
									fn.castBigDecimal(fn.preparaPrimas(newcontenido.split(saltolinea)[0].trim())));
						}

						break;
					}
				}
			}

			// coberturas
			List<EstructuraCoberturasModel> coberturas = new ArrayList<EstructuraCoberturasModel>();
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
				if (recibosList.size() == 0) {
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
			case 2:
				if (recibosList.size() == 1) {
					EstructuraRecibosModel recibo = new EstructuraRecibosModel();
					recibo.setReciboId("");
					recibo.setSerie("2/2");
					recibo.setVigenciaDe(recibosList.get(0).getVigenciaA());
					recibo.setVigenciaA(modelo.getVigenciaA());
					recibo.setVencimiento("");
					recibo.setPrimaneta(restoPrimaNeta);
					recibo.setPrimaTotal(restoPrimaTotal);
					recibo.setRecargo(restoRecargo);
					recibo.setDerecho(restoDerecho);
					recibo.setIva(restoIva);
					recibo.setAjusteUno(restoAjusteUno);
					recibo.setAjusteDos(restoAjusteDos);
					recibo.setCargoExtra(restoCargoExtra);
					recibosList.add(recibo);
				}
				break;
			case 3:
			case 4:
			case 5:
			case 6: // NINGUN PDF TRAE RECIBOS SE QUEDA PENDIENTE ESTE CASO
				if (recibosList.size() >= 1) {
					BigDecimal restoRec = fn.castBigDecimal(fn.getTotalRec(modelo.getFormaPago()) - recibosList.size());
					int totalRec = fn.getTotalRec(modelo.getFormaPago());
					for (int i = recibosList.size(); i <= restoRec.intValue(); i++) {
						EstructuraRecibosModel recibo = new EstructuraRecibosModel();
						recibo.setSerie(i + 1 + "/" + totalRec);
						recibo.setReciboId("");
						recibo.setVigenciaDe(recibosList.get(i - 1).getVigenciaA());
						recibo.setVigenciaA("");
						recibo.setVencimiento("");
						recibo.setPrimaneta(restoPrimaNeta.divide(restoRec,2,RoundingMode.HALF_EVEN));
						recibo.setPrimaTotal(restoPrimaTotal.divide(restoRec,2,RoundingMode.HALF_EVEN));
						recibo.setRecargo(restoRecargo.divide(restoRec,2,RoundingMode.HALF_EVEN));
						recibo.setDerecho(restoDerecho.divide(restoRec,2,RoundingMode.HALF_EVEN));
						recibo.setIva(restoIva.divide(restoRec,2,RoundingMode.HALF_EVEN));
						recibo.setAjusteUno(restoAjusteUno.divide(restoRec,2,RoundingMode.HALF_EVEN));
						recibo.setAjusteDos(restoAjusteDos.divide(restoRec,2,RoundingMode.HALF_EVEN));
						recibo.setCargoExtra(restoCargoExtra.divide(restoRec,2,RoundingMode.HALF_EVEN));
						recibosList.add(recibo);
					}
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

			ArrayList<String> series = new ArrayList<String>();
			for (String a : recibos.split("AVISO DE COBRO")) {
				if (index > 0 && a.contains("recibo:")) {
					EstructuraRecibosModel recibo = new EstructuraRecibosModel();
					
					String actualSerie = "";
					conceptos = Arrays.asList("recibo:");
					for (String x : conceptos) {
						inicio = a.indexOf(x);
						if (inicio > -1) {
							switch (x) {
							case "recibo:":
								inicio = inicio + 7;
								newcontenido = fn.gatos(a.substring(inicio, (inicio + 150)).split(saltolinea)[0]).split(separador)[0];
								actualSerie = recibo.getReciboId();
								break;
							}
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
						
						conceptos = Arrays.asList("Vigencia:");
						for (String x : conceptos) {
							inicio = a.indexOf(x);
							if (inicio > -1) {
								switch (x) {
								case "Vigencia:":
									inicio = inicio + 9;
									newcontenido = fn.gatos(fn.gatos(a.substring(inicio, (inicio + 150)).split(saltolinea)[0])
											.split("12:")[0]);
									newcontenido =(newcontenido.split(separador).length>1)?newcontenido.split(separador)[1]:"";
									recibo.setVigenciaDe(fn.formatDate(newcontenido,"dd-mm-yyyy"));
									break;
								}
							}
						}

						//conceptos = Arrays.asList("Vigencia:");
						for (String x : conceptos) {
							inicio = a.lastIndexOf(x);
							if (inicio > -1) {
								switch (x) {
								case "Vigencia:":
									inicio = inicio + 9;
									newcontenido = fn.gatos(a.substring(inicio, (inicio + 150)).split(saltolinea)[0].split("al")[1].split("12:")[0]);
									recibo.setVigenciaA(fn.formatDate(newcontenido,"dd-mm-yyyy"));
									break;
								}
							}
						}
						
						conceptos = Arrays.asList("Total a pagar:");
						for (String x : conceptos) {
							inicio = a.indexOf(x);
							if (inicio > -1) {
								switch (x) {
								case "Total a pagar:":
									inicio = inicio + 14;
									newcontenido = fn.gatos(a.substring(inicio, (inicio + 150)).split(saltolinea)[0])
											.split(separador)[0];
									if (NumberUtils.isParsable(fn.preparaPrimas(newcontenido))) {
										recibo.setPrimaTotal(fn.castBigDecimal(fn.preparaPrimas(newcontenido)));
										restoPrimaTotal = fn.castBigDecimal(modelo.getPrimaTotal())
												.subtract(recibo.getPrimaTotal());
									}

									break;
								}
							}
						}
						
						conceptos = Arrays.asList("Gastos de expedición");
						for (String x : conceptos) {
							inicio = a.indexOf(x);
							if (inicio > -1) {
								switch (x) {
								case "Gastos de expedición":
									inicio = inicio + 20;
									newcontenido = fn.gatos(a.substring(inicio, (inicio + 150)).split(saltolinea)[0]).split(separador)[0];
									if (NumberUtils.isParsable(fn.preparaPrimas(newcontenido))) {
										recibo.setDerecho(fn.castBigDecimal(fn.preparaPrimas(newcontenido)));
										restoDerecho = fn.castBigDecimal(modelo.getDerecho()).subtract(recibo.getDerecho());
									}
									break;
								}
							}
						}
						
						conceptos = Arrays.asList("I.V.A.");
						for (String x : conceptos) {
							inicio = a.indexOf(x);
							if (inicio > -1) {
								switch (x) {
								case "I.V.A.":
									inicio = inicio + 6;
									newcontenido = fn.gatos(a.substring(inicio, (inicio + 150)).split(saltolinea)[0])
											.split(separador)[0].trim();
									if (NumberUtils.isParsable(fn.preparaPrimas(newcontenido))) {
										recibo.setIva(fn.castBigDecimal(fn.preparaPrimas(newcontenido)));
										restoIva = fn.castBigDecimal(modelo.getIva()).subtract(recibo.getIva());
									}
									break;
								}
							}
						}
						
						conceptos = Arrays.asList("Financiamiento por pago fraccionado");
						for (String x : conceptos) {
							inicio = a.indexOf(x);
							if (inicio > -1) {
								switch (x) {
								case "Financiamiento por pago fraccionado":
									inicio = inicio + 35;
									newcontenido = fn.gatos(a.substring(inicio, (inicio + 150)).split(saltolinea)[0])
											.split(separador)[0];
									if (NumberUtils.isParsable(fn.preparaPrimas(newcontenido))) {
										recibo.setRecargo(fn.castBigDecimal(fn.preparaPrimas(newcontenido)));
										restoRecargo = fn.castBigDecimal(modelo.getRecargo())
												.subtract(recibo.getRecargo());
									}
									break;
								}
							}
						}

						conceptos = Arrays.asList("Prima Neta");
						for (String x : conceptos) {
							inicio = a.indexOf(x);
							if (inicio > -1) {
								switch (x) {
								case "Prima Neta":
									inicio = inicio + 10;
									newcontenido = fn.gatos(a.substring(inicio, (inicio + 150)).split(saltolinea)[0])
											.split(separador)[0];
									if (NumberUtils.isParsable(fn.preparaPrimas(newcontenido))) {
										recibo.setPrimaneta(fn.castBigDecimal(fn.preparaPrimas(newcontenido)));
										restoPrimaNeta = fn.castBigDecimal(modelo.getPrimaneta())
												.subtract(recibo.getPrimaneta());
									}
									break;
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
