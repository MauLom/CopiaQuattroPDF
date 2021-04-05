package com.copsis.models.mapfre;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraCoberturasModel;
import com.copsis.models.EstructuraJsonModel;
import com.copsis.models.EstructuraRecibosModel;
import com.copsis.models.EstructuraUbicacionesModel;

public class MapfreDiversosModel {

	// Clases
	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
	// Variables
	private String inicontenido = "";
	private String contenido = "";
	private String newcontenido = "";
	private String seccion = "";
	private String ubicacionesTex = "";
	private String resultado = "";
	private String recibosText;
	private int inicio = 0;
	private int fin = 0;
	private int index = 0;
	private BigDecimal restoPrimaTotal = BigDecimal.ZERO;
	private BigDecimal restoDerecho = BigDecimal.ZERO;
	private BigDecimal restoIva = BigDecimal.ZERO;
	private BigDecimal restoRecargo = BigDecimal.ZERO;
	private BigDecimal restoPrimaNeta = BigDecimal.ZERO;
	private BigDecimal restoAjusteUno = BigDecimal.ZERO;
	private BigDecimal restoAjusteDos = BigDecimal.ZERO;
	private BigDecimal restoCargoExtra = BigDecimal.ZERO;

	// constructor
	public MapfreDiversosModel(String contenido, String recibos, String Ubicaiones) {
		this.contenido = contenido;
		this.recibosText = recibos;
		this.ubicacionesTex = Ubicaiones;
	}

	public EstructuraJsonModel procesar() {
		inicontenido = fn.fixContenido(contenido);
		contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());

		contenido = contenido.replace("Póliza Número    :", "Póliza Número:")
				.replace("Póliza número    :", "Póliza número:")
				.replace("EMPRESARIAL       Endoso", "EMPRESARIAL       ###Endoso")
				.replace(", MULTIPLE", ",###MULTIPLE").replace("Vig encia", "Vigencia")
				.replace("Clave de", "###Clave de").replace("T e l . :", "Tel.:")
				.replace(" C O P S E ,  A G E NTE", "COPSE AGENTE").replace("Cliente MAPFRE   :", "Cliente MAPFRE:")
				.replace("Cliente Mapfre###:", "Cliente Mapfre:").replace("Cobro:", "cobro:")// Cobro:
				.replace("Map fre Tepeyac", "Mapfre Tepeyac");

		try {

			// tipo
			modelo.setTipo(7);
			// cia
			modelo.setCia(22);

			// poliza
			inicio = contenido.indexOf("Póliza Número:");
			if (inicio == -1) {
				inicio = contenido.indexOf("Póliza número:");
				if (inicio == -1) {
					inicio = contenido.indexOf("Póliza número");
				}
			}

			if (inicio > -1) {
				modelo.setPoliza(contenido.substring(inicio + 14, inicio + 60).split("\r\n")[0].replace(":", "")
						.replace(" ", "").trim());
			}

			// endoso
			inicio = contenido.indexOf("Endoso Número");
			if (inicio == -1) {
				inicio = contenido.indexOf("so número###:");
			}
			if (inicio > -1) {
				modelo.setEndoso(
						contenido.substring(inicio + 13, inicio + 100).split("\r\n")[0].replace(":", "").trim());
			}

			// cte_nombre
			inicio = contenido.indexOf("Contratante:");
		;
			if (inicio > -1) {
				newcontenido = contenido.substring(inicio + 12, inicio + 150).split("\r\n")[0];
				if (newcontenido.contains("R.F.C")) {
					modelo.setCteNombre(fn.gatos(
							contenido.substring(inicio + 12, inicio + 150).split("\r\n")[0].split("R.F.C")[0].trim()));
				}
			} else {
				inicio = contenido.indexOf("R.F.C:");
				if (inicio > -1) {
					modelo.setCteNombre(fn.gatos(
							contenido.substring(inicio + 7, inicio + 150).split("\r\n")[0].split("R.F.C:")[0].trim()));
				}
			}

			// rfc
			inicio = contenido.indexOf("Contratante");
			fin = contenido.indexOf("Domicilio");
			if (fin == -1) {
				fin = contenido.indexOf("icilio");
			}
			if (inicio > -1 && fin > -1) {
				newcontenido = contenido.substring(inicio, fin);
				inicio = newcontenido.indexOf("R.F.C.");
				if (inicio > -1) {
					resultado = fn.gatos(newcontenido.substring(inicio + 6, newcontenido.indexOf("\r\n", inicio + 6))
							.replace(":", "").replace(" ", ""));
					modelo.setRfc(resultado);
				}
			}

			// cp
			inicio = contenido.indexOf("C.P.:");
			if (inicio > -1) {
				newcontenido = contenido.substring(inicio + 5, inicio + 30).split("\r\n")[0].replace(" ", "").trim();
				modelo.setCp(newcontenido);
				;
			}

			// cte_direccion
			inicio = contenido.indexOf("icilio:");
			if (inicio > -1) {
				newcontenido = contenido.substring(inicio + 7, inicio + 150);
				if (newcontenido.contains("Tel")) {
					modelo.setCteDireccion(fn.gatos(newcontenido.split("Tel")[0].replace(".", "").trim()));
				}
			}

			// vigencia_de
			inicio = contenido.indexOf("Vigencia");
			if (inicio > -1) {
				newcontenido = contenido.substring(inicio + 8, inicio + 200).split("\r\n")[0];
				switch (newcontenido.split("###").length) {
				case 2:
				case 3:
					if (newcontenido.split("###")[0].contains("de:")) {					
						modelo.setVigenciaDe(
								fn.formatDate(newcontenido.split("###")[0].split("de:")[1].trim(), "dd-MM-YY"));
					}
					break;
				}
			}

			// agente
			// cve_agente
			// vigencia_a
			inicio = contenido.indexOf("Hasta las 12:00");
			if (inicio == -1) {
				inicio = contenido.indexOf("hasta las 12:00");
			}
			fin = contenido.indexOf("Fecha de emisión");
			if (inicio > -1 && fin > inicio) {
				newcontenido = contenido.substring(inicio + 15, fin).replace("@@@", "");
				if (newcontenido.contains("de:")) {
					newcontenido = fn.RemplazaGrupoSpace(newcontenido.split("de:")[1].trim()).replace("######", "###");
					switch (newcontenido.split("###").length) {
					case 3:
						modelo.setVigenciaA(fn.formatDate_MonthCadena(newcontenido.split("###")[0].trim()));
						modelo.setCveAgente(newcontenido.split("###")[1].trim());
						modelo.setAgente(newcontenido.split("###")[2].trim());
						break;
					}
				}
			}

			// moneda
			// forma_pago
			inicio = contenido.indexOf("Cobro:");
			if (inicio == -1) {
				inicio = contenido.indexOf("cobro:");
			}
			fin = contenido.indexOf("Prima neta:");
			if (inicio > -1 && fin > inicio) {
				newcontenido = fn
						.RemplazaGrupoSpace(
								contenido.substring(inicio + 6, fin).replace("@@@", "").trim().split("\r\n")[0])
						.replace("######", "###");
				switch (newcontenido.split("###").length) {
				case 5:
					modelo.setFormaPago(fn.formaPago(newcontenido.split("###")[1].trim()));
					modelo.setMoneda(fn.moneda(newcontenido.split("###")[2].replace("$", "").trim()));
					break;
				}
			}

			// fecha_emision
			inicio = contenido.indexOf("Gestor de cobro:");
			if (inicio > -1) {
				newcontenido = contenido.substring(inicio + 16, inicio + 150).trim().split("\r\n")[0];
				fin = newcontenido.lastIndexOf("/");
				if (fin > -1) {
					modelo.setFechaEmision(fn.formatDate(newcontenido.substring(0, fin + 5).trim(), "dd-MMM-YY"));
				}
			}

			// id_cliente
			inicio = contenido.indexOf("Cliente MAPFRE:");
			if (inicio == -1) {
				inicio = contenido.indexOf("Cliente Mapfre:");
			}
			if (inicio > -1) {
				modelo.setIdCliente(
						fn.gatos(contenido.substring(inicio + 15, contenido.indexOf("\r\n", inicio)).trim()));
			}

			// prima_neta
			// derecho
			// recargo
			// iva
			// prima_total
			inicio = contenido.indexOf("Prima neta:");
			fin = contenido.indexOf("Mapfre Tepeyac");
			if (inicio > -1 && fin > inicio) {
				newcontenido = contenido.substring(inicio, fin);
				if (newcontenido.contains("otal:")) {
					newcontenido = fn.RemplazaGrupoSpace(newcontenido.split("otal:")[1].replace("@@@", "").trim())
							.replace("######", "###");
					switch (newcontenido.split("###").length) {
					case 7:
						modelo.setPrimaneta(fn.castBigDecimal(fn.preparaPrimas(newcontenido.split("###")[0].trim())));
						modelo.setRecargo(fn.castBigDecimal(fn.preparaPrimas(newcontenido.split("###")[2].trim())));
						modelo.setDerecho(fn.castBigDecimal(fn.preparaPrimas(newcontenido.split("###")[3].trim())));;
						modelo.setIva(fn.castBigDecimal(fn.preparaPrimas(newcontenido.split("###")[5].trim())));
						modelo.setPrimaTotal(fn.castBigDecimal(fn.preparaPrimas(newcontenido.split("###")[6].trim())));
						break;
					}
				}
			}

			// plan
			inicio = contenido.indexOf("Póliza Número:");
			fin = contenido.indexOf("###Endoso Número");
			if (inicio > -1 && fin > -1 && inicio < fin) {
				newcontenido = contenido.substring(inicio + 14, fin);
				if (newcontenido.split("\r\n").length == 2) {
					newcontenido = newcontenido.split("\r\n")[1];
					switch (newcontenido.split("###").length) {
					case 2:
						modelo.setPlan(newcontenido.split("###")[1].replace("\"", "").trim());
						break;
					}
				}
			} else {
				inicio = contenido.indexOf("ocumento:");
				fin = contenido.indexOf("óliza número:");
				if (inicio > -1 && fin > -1 && inicio < fin) {
					newcontenido = contenido.substring(inicio + 9, fin);
					if (newcontenido.split("\r\n").length == 2) {
						switch (newcontenido.split("\r\n")[1].split("###").length) {
						case 2:
							modelo.setPlan(newcontenido.split("\r\n")[1].split("###")[0].replace("\"", "").trim());
							break;
						}
					}
				}
			}

			// renovacion
			inicio = inicontenido.indexOf("SUMA ASEGURADA DEDUCIBLE");
			fin = inicontenido.indexOf("ABREVIATURAS");

			List<EstructuraCoberturasModel> coberturas = new ArrayList<>();
			if (inicio > -1 && fin > inicio) {
				newcontenido = inicontenido.substring(inicio, fin).replace("@@@", "").trim();
				resultado = "";
				for (String x : newcontenido.split("\r\n")) {
					if (x.contains("BIENES Y RIESGOS CUBIERTOS") == false && x.contains("---") == false
							&& x.contains("DEDUCIBLE") == false) {
						resultado = x.trim();
						resultado = fn.RemplazaGrupoSpace(resultado.trim());
						resultado = resultado.replace("A.###MIN", "A. MIN").replace("###DSMG", " DSMG")
								.replace(".###MIN", ". MIN").replace(".###MAX", ". MAX");
						EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();
						switch (resultado.split("###").length) {
						case 2:
							cobertura.setNombre(resultado.split("###")[0].trim());
							cobertura.setSa(resultado.split("###")[1].trim());
							coberturas.add(cobertura);
							break;
						case 3:
							cobertura.setNombre(resultado.split("###")[0].trim());
							cobertura.setSa(resultado.split("###")[1].trim());
							if (resultado.split("###")[2].trim().length() == 3) {
								cobertura.setCoaseguro(resultado.split("###")[2].trim());
							} else {
								cobertura.setDeducible(resultado.split("###")[2].trim());
							}
							coberturas.add(cobertura);
							break;
						case 4:
							cobertura.setNombre(resultado.split("###")[0].trim());
							cobertura.setSa(resultado.split("###")[1].trim());
							cobertura.setDeducible(resultado.split("###")[2].trim());
							cobertura.setCoaseguro(resultado.split("###")[3].trim());
							coberturas.add(cobertura);
							break;
						}
					}
				}
			} else {// SEGUNDO CASO
				inicio = inicontenido.indexOf("BIENES Y RIESGOS");
				fin = inicontenido.indexOf("ABREVIATURAS");

				if (fin == -1) {
					fin = inicontenido.indexOf("RENOVACION AUTOMATICA");
				}

				if (inicio > -1 && fin > inicio) {
					newcontenido = inicontenido.substring(inicio, fin).replace("@@@", "").trim();

					if (newcontenido.contains("RENOVACIÓN AUTOMÁTICA:")) {
						newcontenido = newcontenido.split("RENOVACIÓN AUTOMÁTICA")[0];
					}
					resultado = "";
					for (String x : newcontenido.split("\r\n")) {

						if (x.contains("SUMA ASEGURADA") == false && x.contains("---") == false) {

							
							EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();
							x = fn.gatos( x.replaceAll("  +", "###").trim());
							x = x.replace("###$###", "###$ ");
							switch (x.split("###").length) {
							case 2:
								switch (x.split("###")[0].trim()) {
								case "I":
								case "II":
								case "III":
								case "IV":
								case "V":
								case "VI":
								case "VII":
								case "VIII":
								case "IX":
								case "X":
								case "XI":
								case "XII":
								case "XIII":
								case "XIV":
								case "XV":
									seccion = x.split("###")[0].trim();
									break;
								default:
									
									cobertura.setNombre(x.split("###")[0].trim());
									cobertura.setSeccion(seccion);
									cobertura.setSa(x.split("###")[1].trim());
									coberturas.add(cobertura);
									break;
								}
								break;
							case 3:
								switch (x.split("###")[0].trim()) {
								case "I":
								case "II":
								case "III":
								case "IV":
								case "V":
								case "VI":
								case "VII":
								case "VIII":
								case "IX":
								case "X":
								case "XI":
								case "XII":
								case "XIII":
								case "XIV":
								case "XV":
								
									seccion = x.split("###")[0].trim();
									cobertura.setSeccion(seccion);
									cobertura.setNombre(x.split("###")[1].trim());
									cobertura.setSa(x.split("###")[2].trim());
									coberturas.add(cobertura);
									break;
								default:
									cobertura.setSeccion(seccion);
									cobertura.setNombre(x.split("###")[1].trim());
									cobertura.setSa(x.split("###")[2].trim());
									coberturas.add(cobertura);
									break;
								}
								break;
							}
						}
					}
				}
			}

			modelo.setCoberturas(coberturas);

			// UBICACIONES
			inicio = contenido.indexOf("RIESGOS ASEGURADOS");
			fin = contenido.indexOf("MEDIDAS DE SEGURIDAD");
			List<EstructuraUbicacionesModel> ubicaciones = new ArrayList<>();

			if (inicio > -1 && fin > inicio) {
				EstructuraUbicacionesModel ubicacion = new EstructuraUbicacionesModel();
				newcontenido = contenido.substring(inicio + 18, fin).replace("@@@", "").trim();
				resultado = newcontenido.split(":")[1].split("CLASIFICACIÓN")[0].trim();

				// cp
				if (resultado.contains(", C.P.")) {

					ubicacion.setCp(resultado.split("\r\n")[0].split(", C.P.")[1].replace(".", "").trim());
				}

				// calle
				// colonia
				// no_externo
				// no_interno
				index = 0;
				for (String xx : resultado.split(",")) {
					if (index == 0) {
						ubicacion.setCalle(xx.trim());
					}
					if (xx.contains("Col.")) {
						ubicacion.setColonia(xx.trim());
					}
					if (xx.contains("No.")) {
						ubicacion.setNoExterno(xx.trim());
					}
					if (xx.contains("Int.")) {
						ubicacion.setNoInterno(xx.trim());
					}
					index++;
				}

				// nombre
				// giro
				inicio = contenido.indexOf("GIRO ASEGURADO");
				if (inicio > -1) {
					newcontenido = contenido.substring(inicio, contenido.indexOf("\r\n", inicio)).split(":")[1]
							.replace("  .", ".").trim();
					ubicacion.setNombre(newcontenido);
					ubicacion.setGiro(newcontenido);
				}

				// muros
				// techos
				inicio = contenido.indexOf("CARACTERISTICAS");
				if (inicio == -1) {
					inicio = contenido.indexOf("CARACTERÍSTICAS");
				}
				if (inicio > -1) {
					newcontenido = contenido.substring(inicio, contenido.indexOf(".", inicio)).split(":")[1]
							.replace("\r\n", "").replace("CONSTRUCCION", "CONSTRUCCIÓN").trim();
					fin = newcontenido.lastIndexOf(" Y ");
					if (fin > -1) {

						resultado = newcontenido.substring(0, fin).split("CONSTRUCCIÓN MUROS")[1].trim();
						ubicacion.setMuros(fn.material(resultado));

						resultado = newcontenido.substring(fin + 3, newcontenido.length()).replace("TECHO DE", "")
								.trim();

						ubicacion.setTechos(fn.material(resultado));
					}
				}

				// niveles
				for (String x : newcontenido.split(",")) {
					if (x.contains("PISOS ALTOS")) {
						if (x.contains(" Y ")) {
							resultado = x.split(" Y ")[0].replace("PISOS ALTOS", "").trim();
							if (fn.isNumeric(resultado)) {
								ubicacion.setNiveles(Integer.parseInt(resultado));
							}
							resultado = x.split(" Y ")[1].replace("SOTANOS", "").trim();
							if (fn.isNumeric(resultado)) {
								ubicacion.setSotanos(Integer.parseInt(resultado));
							}
						}
					}
				}

				if (ubicacion.getNiveles() == 0) {
					ubicacion.setNiveles(1);
				}
				ubicaciones.add(ubicacion);
			}

			modelo.setUbicaciones(ubicaciones);

			List<EstructuraRecibosModel> recibosList = new ArrayList<>();
			EstructuraRecibosModel recibo = new EstructuraRecibosModel();

			if (recibosText.length() > 0) {
				recibosList = recibosExtract();
			}

			// CALCULO RESTO DE RECIBOS
			switch (modelo.getFormaPago()) {
			case 1:
				if(recibosList.size() ==  0) {
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
			
				break;
			case 2:
				if (recibosList.size() == 1) {
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
				if (recibosList.size() >= 1) {
					BigDecimal restoRec = fn.castBigDecimal(fn.getTotalRec(modelo.getFormaPago()) - recibosList.size());
					int totalRec = fn.getTotalRec(modelo.getFormaPago());
					int meses = fn.monthAdd(modelo.getFormaPago());// MESES A AGREGAR POR RECIBO

					for (int i = recibosList.size(); i <= restoRec.intValue(); i++) {

						recibo.setSerie(i + 1 + "/" + totalRec);
						recibo.setVigenciaDe(recibosList.get(i - 1).getVigenciaA());
						if (recibosList.get(i - 1).getVigenciaA().length() == 10) {
							recibo.setVigenciaA(fn.dateAdd(recibosList.get(i - 1).getVigenciaA(), meses, i));
						}
						recibo.setVencimiento("");
						recibo.setPrimaneta(restoPrimaNeta.divide(restoRec, 2,RoundingMode.HALF_EVEN));
						recibo.setPrimaTotal(restoPrimaTotal.divide(restoRec,2,RoundingMode.HALF_EVEN));
						recibo.setRecargo(restoRecargo.divide(restoRec,2,RoundingMode.HALF_EVEN));
						recibo.setDerecho(restoDerecho.divide(restoRec,2,RoundingMode.HALF_EVEN));
						recibo.setIva(restoIva.divide(restoRec,2,RoundingMode.HALF_EVEN));
						recibo.setAjusteUno(restoAjusteUno.divide(restoRec,2,RoundingMode.HALF_EVEN));
						recibo.setAjusteDos(restoAjusteDos.divide(restoRec,2,RoundingMode.HALF_DOWN));
						recibo.setCargoExtra(restoCargoExtra.divide(restoRec,2,RoundingMode.HALF_EVEN));
						recibosList.add(recibo);
					}
				}

				break;
			case 5:
			case 6:// QUINCENAL, SEMANAL NINGUN PDF DE FORMA DE PAGO SE QUEDA PENDIENTE ESTE CASO
				if (recibosList.size() >= 1) {
					BigDecimal restoRec = fn.castBigDecimal(fn.getTotalRec(modelo.getFormaPago()) - recibosList.size());
					int totalRec = fn.getTotalRec(modelo.getFormaPago());
					for (int i = recibosList.size(); i <= restoRec.intValue(); i++) {

						recibo.setSerie(i + 1 + "/" + totalRec);
						recibo.setPrimaneta(restoPrimaNeta.divide(fn.castBigDecimal(restoRec), 2,RoundingMode.HALF_EVEN));
						recibo.setPrimaTotal(restoPrimaTotal.divide(restoRec,2,RoundingMode.HALF_EVEN));
						recibo.setRecargo(restoRecargo.divide(restoRec, 2,RoundingMode.HALF_EVEN));
						recibo.setDerecho(restoDerecho.divide(restoRec,2,RoundingMode.HALF_EVEN));
						recibo.setIva(restoIva.divide(restoRec,2,RoundingMode.HALF_EVEN));
						recibo.setAjusteUno(restoAjusteUno.divide(restoRec,2,RoundingMode.HALF_EVEN));
						recibo.setAjusteDos(restoAjusteDos.divide(restoRec,2,RoundingMode.HALF_DOWN));
						recibo.setCargoExtra(restoCargoExtra.divide(restoRec,2,RoundingMode.HALF_EVEN));
						recibosList.add(recibo);
					}
				}
				break;
			}
			modelo.setRecibos(recibosList);

			return modelo;
		} catch (Exception ex) {
			modelo.setError(MapfreDiversosModel.this.getClass().getTypeName() + " | " + ex.getMessage() + " | "
					+ ex.getCause());
			return modelo;
		}

	}

	private ArrayList<EstructuraRecibosModel> recibosExtract() {
		recibosText = fn.fixContenido(recibosText);
		List<EstructuraRecibosModel> recibosLis = new ArrayList<>();

		try {
			EstructuraRecibosModel recibo = new EstructuraRecibosModel();
			// prima_neta
			inicio = recibosText.indexOf("Prima neta###");
			if (inicio > -1) {
				newcontenido = recibosText.substring(inicio + 13, recibosText.indexOf("\r\n", inicio));
				if (newcontenido.split("###").length == 2) {
					recibo.setPrimaneta(fn.castBigDecimal(fn.preparaPrimas(newcontenido.split("###")[1])));
					restoPrimaNeta = modelo.getPrimaneta().subtract(recibo.getPrimaneta());
				}
			}

			// derecho
			inicio = recibosText.indexOf("Gasto de expedición");
			if (inicio > -1) {
				newcontenido = fn.gatos(recibosText.substring(inicio + 19, inicio + 150).split("\r\n")[0]);
				if (newcontenido.split("###").length == 2) {
					recibo.setDerecho(fn.castBigDecimal(fn.preparaPrimas(newcontenido.split("###")[1])));
					restoDerecho = modelo.getDerecho().subtract(recibo.getDerecho());
				}
			}

			// recargo
			inicio = recibosText.indexOf("Financ. pago fracc.");
			if (inicio > -1) {
				newcontenido = fn.gatos(recibosText.substring(inicio + 19, recibosText.indexOf("\r\n", inicio)));
				if (newcontenido.split("###").length == 2) {
					recibo.setRecargo(fn.castBigDecimal(fn.preparaPrimas(newcontenido.split("###")[1])));
					restoRecargo = modelo.getRecargo().subtract(recibo.getRecargo());
				}
			}

			// iva
			inicio = recibosText.indexOf("IVA 16.00%");
			if (inicio > -1) {
				newcontenido = recibosText.substring(inicio, recibosText.indexOf("\r\n", inicio));
				if (newcontenido.split("###").length == 3) {
				
					recibo.setIva(fn.castBigDecimal(fn.preparaPrimas(newcontenido.split("###")[2])));
					restoIva = modelo.getIva().subtract(recibo.getIva());
				}
			}

			// prima_total
			inicio = recibosText.indexOf("Total a pagar:");
			if (inicio > -1) {
				newcontenido = recibosText.substring(inicio, recibosText.indexOf("\r\n", inicio));
				if (newcontenido.split("###").length == 3) {
					recibo.setPrimaTotal(fn.castBigDecimal(fn.preparaPrimas(newcontenido.split("###")[2])));
					restoPrimaTotal = modelo.getPrimaTotal().subtract(recibo.getPrimaTotal());
				}
			}

			// vigencia_de
			// vigencia_a
			inicio = recibosText.indexOf("horas del ");
			if (inicio > -1) {
				newcontenido = recibosText.substring(inicio + 10, recibosText.indexOf("\r\n", inicio + 10))
						.split("Gasto de ")[0];
				if (newcontenido.contains("Hasta: 12:00 horas del")) {
					resultado = fn.gatos(newcontenido.split("Hasta: 12:00 horas del")[0]).trim();

					recibo.setVigenciaDe(resultado);
					String resultado2 = fn.gatos(newcontenido.split("Hasta: 12:00 horas del")[1]).trim();
					recibo.setVigenciaA(resultado2);
	
				}
			}

			// vencimiento
			if (recibo.getVigenciaDe().length() > 0) {
				recibo.setVencimiento(fn.dateAdd(recibo.getVigenciaDe(), 30, 1));
			}

			// serie
			inicio = recibosText.indexOf("Serie de recibo:");
			if (inicio > -1) {
				newcontenido = fn.gatos(recibosText.substring(inicio + 16, recibosText.indexOf("Fecha", inicio + 16)));
				newcontenido = newcontenido.replace("01", "1").replace("02", "2").replace("03", "3").replace("04", "4")
						.replace("05", "5").replace("06", "6").replace("07", "7").replace("08", "8").replace("09", "9");
				recibo.setSerie(newcontenido);
			}

			// recibo_id
			inicio = recibosText.indexOf("serie P No.");
	
			if (inicio > -1) {
				newcontenido = fn.gatos(recibosText.substring(inicio + 11, recibosText.indexOf("\r\n", inicio + 11)));

				recibo.setReciboId(newcontenido);
			}

			return (ArrayList<EstructuraRecibosModel>) recibosLis;
		} catch (Exception ex) {
			modelo.setError(MapfreDiversosModel.this.getClass().getTypeName() + " | " + ex.getMessage() + " | "
					+ ex.getCause());

			return (ArrayList<EstructuraRecibosModel>) recibosLis;
		}
	}

}
