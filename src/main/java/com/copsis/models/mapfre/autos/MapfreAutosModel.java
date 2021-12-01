package com.copsis.models.mapfre.autos;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraCoberturasModel;
import com.copsis.models.EstructuraJsonModel;
import com.copsis.models.EstructuraRecibosModel;

public class MapfreAutosModel {
	// Clases
	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
	// Variables
	private String inicontenido = "";
	private String contenido = "";
	private String newcontenido = "";
	private String txtAux = "";
	private String txt = "";
	private String recibos = "";
	private String resultado = "";
	private int inicio = 0;
	private int fin = 0;
	private int index;
	private BigDecimal restoPrimaTotal =  BigDecimal.ZERO;
	private BigDecimal restoDerecho = BigDecimal.ZERO;
	private BigDecimal restoIva = BigDecimal.ZERO;
	private BigDecimal restoRecargo = BigDecimal.ZERO;
	private BigDecimal restoPrimaNeta = BigDecimal.ZERO;
	private BigDecimal restoAjusteUno = BigDecimal.ZERO;
	private BigDecimal restoAjusteDos = BigDecimal.ZERO;
	private BigDecimal restoCargoExtra = BigDecimal.ZERO;

	// constructor
	public MapfreAutosModel(String contenido, String recibos) {
		this.contenido = contenido;
		this.recibos = recibos;
	}

	public EstructuraJsonModel procesar() {
		inicontenido = fn.fixContenido(contenido);
		contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales()).replace("R.F.C.:", "R.F.C:").replace("R.F.C:###", "R.F.C:");
		this.recibos = fn.remplazarMultiple(recibos, fn.remplazosGenerales());
		try {
			// tipo
			modelo.setTipo(1);
			// cia
			modelo.setCia(22);

			// Poliza-Endoso
			inicio = contenido.indexOf("PÓLIZA-ENDOSO");
			fin = contenido.indexOf("FECHA DE EMISIÓN");

			if (inicio > 0 && fin > 0 && inicio < fin) {
				newcontenido = contenido.substring(inicio, fin);
				for (int i = 0; i < newcontenido.split("\n").length; i++) {
					if (newcontenido.split("\n")[i].contains("PÓLIZA-ENDOSO")) {
						modelo.setEndoso(newcontenido.split("ENDOSO")[1].split("-")[1].replace("\r\n", "")
								.replace("@@@", "").trim());
						modelo.setPoliza(
								newcontenido.split("PÓLIZA-ENDOSO")[1].split("-")[0].replace("###", "").trim());
					}

				}
			} else {// para version2-pdf

				inicio = contenido.indexOf("PÓLIZA NÚMERO:");
				if (inicio > -1) {
					newcontenido = fn.gatos(contenido.substring(inicio + 14, inicio + 150).split("\r\n")[0]);
					modelo.setPoliza(newcontenido);
				}
			}

//            cte_nombre
			inicio = contenido.indexOf("CONTRATANTE:");
			fin = contenido.indexOf("DOMICILIO:");

			if (inicio > -1) {
				newcontenido = fn.gatos(contenido.substring(inicio + 12, fin).replace("@@@", "").trim());
				txtAux = "";
				for (String x : newcontenido.split("\r\n")) {
					if (index >= 0 && index <= 2) {
						x = fn.gatos(x);
						fin = x.indexOf("CONDUCTOR");
						if (fin > -1) {
							txtAux += fn.gatos(x.substring(0, fin)) + " ";
						} else if (x.split("###").length == 2) {
							txtAux += x.split("###")[0] + " ";
						}
					}
					index++;
				}
				if (txtAux.contains(",")) {
					modelo.setCteNombre((txtAux.trim().split(",")[1] + " " + txtAux.trim().split(",")[0]).trim());
				} else {
					modelo.setCteNombre(txtAux.trim());
				}

			}
			if (modelo.getCteNombre().length() == 0) {// PARA OTRA VERSION
				inicio = contenido.indexOf("NOMBRE:");
				if (inicio > -1) {
					newcontenido = fn.gatos(contenido.substring(inicio + 7, inicio + 150).split("\r\n")[0]);
					modelo.setCteNombre(newcontenido);
				}
			}
			
			// cte_direccion
			inicio = contenido.indexOf("DOMICILIO:");
			fin = contenido.indexOf("FOLIO");
			if (inicio > -1 && fin > inicio) {
				txtAux = "";
				newcontenido = contenido.substring(inicio + 10, fin);
				index = 0;
				for (String x : newcontenido.split("\r\n")) {
					if (index >= 0 && index <= 2) {
						fin = x.indexOf("DOMICILIO");
						if (fin > -1) {
							txtAux += fn.gatos(x.substring(0, fin)) + " ";
						} else if (x.split("###").length == 2) {
							txtAux += x.split("###")[0].trim() + " ";
						}
					}
					index++;
				}
				modelo.setCteDireccion(txtAux.trim());
			}
			if (modelo.getCteDireccion().length() == 0) {// PARA OTRA VERSION
				inicio = contenido.indexOf("DIRECCIÓN:");
				if (inicio > -1) {
					newcontenido = fn.gatos(contenido.substring(inicio + 10, inicio + 150).split("\r\n")[0]);
					modelo.setCteDireccion(newcontenido);
				}
			}

			// inciso
			modelo.setInciso(1);

			// prima_neta
			// recargo
			// derecho
			// iva
			// prima_total
			inicio = contenido.indexOf("Prima total:");
			if (inicio > -1) {
				txt = contenido.substring(inicio + 12, inicio + 200).trim().split("\r\n")[0].replace("@@@", "").trim();
				
				if (txt.split("###").length == 6) {
					modelo.setPrimaneta(fn.castBigDecimal(fn.preparaPrimas(txt.split("###")[0])));
					modelo.setRecargo(fn.castBigDecimal(fn.preparaPrimas(txt.split("###")[2])));
					modelo.setDerecho(fn.castBigDecimal(fn.preparaPrimas(txt.split("###")[3])));
					modelo.setIva(fn.castBigDecimal(fn.preparaPrimas(txt.split("###")[4])));
					modelo.setPrimaTotal(fn.castBigDecimal(fn.preparaPrimas(txt.split("###")[5])));
				}
				// rfc
				inicio = contenido.indexOf("R.F.C:");
				if (inicio > -1) {
					modelo.setRfc(contenido.substring(inicio + 7, inicio + 150).split("\r\n")[0].trim());
				}


				if (modelo.getCteNombre().length() == 0 && modelo.getRfc().length() == 0) {
					inicio = contenido.indexOf("Contratante");
					fin = contenido.indexOf("Sexo");

					if (inicio > 0 && fin > 0 && inicio < fin) {
						newcontenido = contenido.substring(inicio, fin);
						for (int i = 0; i < newcontenido.split("\n").length; i++) {

							if (newcontenido.split("\n")[i].contains("Contratante")
									&& newcontenido.split("\n")[i].contains("R.F.C:")) {
								if (newcontenido.split("\n")[i].split("Contratante:")[1].split("R.F.C:")[0]
										.contains(",")) {
									modelo.setCteNombre(newcontenido.split("\n")[i].split("Contratante:")[1]
											.split("R.F.C:")[0].split(",")[1] + ""
											+ newcontenido.split("\n")[i].split("Contratante:")[1].split("R.F.C:")[0]
													.split(",")[0].trim());
								}
								modelo.setRfc(newcontenido.split("\n")[i].split("R.F.C:")[1].split("Sexo")[0]
										.replace("###", "").trim());
							}

						}
					}
				}

				if (contenido.contains("CONTRATANTE:")) {
					txt = contenido.split("CONTRATANTE:")[1];
					inicio = txt.indexOf("DOMICILIO:");
					fin = txt.indexOf("Tel:");
					if (inicio > -1 && fin > inicio) {
						newcontenido = fn.gatos(txt.substring(inicio + 10, fin));
						modelo.setCteDireccion(newcontenido);
					}
				} else {
					if (contenido.contains("Contratante:")) {
						txt = contenido.split("Contratante:")[1];
						inicio = txt.indexOf("Domicilio:");
						fin = txt.indexOf("Tel:");
						if (inicio > -1 && fin > inicio) {
							newcontenido = fn.gatos(txt.substring(inicio + 10, fin));
							modelo.setCteDireccion(newcontenido);
						}

					}
				}

			} else {				
				// rfc
				if (contenido.indexOf("R.F.C:") > -1) {
					inicio = contenido.indexOf("R.F.C:") + 6;
					modelo.setRfc(contenido.substring(inicio, inicio + 14).trim().replace("-", "").replace("###", ""));
				} 
     
				

				// prima neta
				inicio = contenido.indexOf("###PRIMA###NETA:###") + 19;
				newcontenido = contenido.substring(inicio, inicio + 20).split("\r\n")[0];
				if (fn.isNumeric(fn.cleanString( newcontenido))) {
					modelo.setPrimaneta(fn.castBigDecimal(fn.preparaPrimas(newcontenido)));
				}

				// prima_total
				inicio = contenido.indexOf("PRIMA###TOTAL:");
				if (inicio > -1) {
					newcontenido = contenido.substring(inicio + 14, inicio + 80).split("\r\n")[0].replace("###", "")
							.replace(",", "");
					if (fn.isNumeric(newcontenido)) {
						modelo.setPrimaTotal(fn.castBigDecimal(fn.preparaPrimas(newcontenido)));
					}
				}

				// iva
				inicio = contenido.lastIndexOf("I.V.A:");
				if (inicio > -1) {
					newcontenido = fn.gatos(contenido.substring(inicio + 7, inicio + 150).split("\r\n")[0]
							.replace("##", "").replace(",", "").trim());
					if (fn.isNumeric(newcontenido)) {
						modelo.setIva(fn.castBigDecimal(fn.preparaPrimas(newcontenido)));
					}
				}

				// recargo
				inicio = contenido.indexOf("RECARGO###PAGO###FRACCIONADO:");

				if (inicio > -1) {
					newcontenido = fn
							.gatos(contenido.substring(inicio + 29, inicio + 150).split("\r\n")[0].split("PRIMA")[0]).trim();
					if (fn.isNumeric(newcontenido)) {
						modelo.setRecargo(fn.castBigDecimal(fn.preparaPrimas(newcontenido)));
					}
				}

				// derecho
				inicio = contenido.indexOf("DE###EXPEDICIÓN:");
				if (inicio > -1) {
					newcontenido = fn.gatos(contenido.substring(inicio + 16, inicio + 150).split("\r\n")[0]).trim();
					if (fn.isNumeric(newcontenido)) {
						modelo.setDerecho(fn.castBigDecimal(fn.preparaPrimas(newcontenido)));
					}
				}

			}

			// agente
			int inicioagentd = contenido.indexOf("@@@AGENTE:  ");
			if (inicioagentd > 0) {
				inicio = contenido.indexOf("@@@AGENTE:  ") + 12;
				newcontenido = contenido.substring(inicio, inicio + 90).split("\r\n")[0];
			} else {
				inicio = contenido.indexOf("@@@AGENTE:") + 11;
				newcontenido = contenido.substring(inicio, inicio + 90).split("\r\n")[0];
			}
			modelo.setAgente(newcontenido.trim());

			// cve_agente
			int inicioagente = contenido.indexOf("CLAVE DE AGENTE: ");
			if (inicioagente > 0) {
				inicio = contenido.indexOf("CLAVE DE AGENTE: ") + 17;
			} else {
				inicio = contenido.indexOf("@@@CLAVE DE AGENTE:") + 17;
			}
			newcontenido = contenido.substring(inicio, inicio + 10).split("\r\n")[0];
			modelo.setCveAgente(newcontenido.replace("E:###", "").trim());

			if (contenido.indexOf("Desde las 12:00 hrs. de:") > 0) {
				// vigencia_de
				inicio = contenido.indexOf("Desde las 12:00 hrs. de:");
				if (inicio > -1) {
					txt = fn.gatos(contenido.substring(inicio + 24, inicio + 150).split("\r\n")[0].split("Clave")[0]);
					modelo.setVigenciaDe(fn.formatDate(txt, "dd-MM-yy"));
				}

				// vigencia_a
				inicio = contenido.indexOf("ta las 12:00 hrs. de:");
				if (inicio > -1) {
					txt = fn.gatos(contenido.substring(inicio + 21, inicio + 150).split("\r\n")[0]);
					modelo.setVigenciaA(fn.formatDate(txt.substring(0, 10), "dd-MM-yy"));
				}

				inicio = contenido.indexOf("ta las 12:00 hrs. de:");
				if (inicio > -1) {
					txt = fn.gatos(contenido.substring(inicio + 21, inicio + 180).split("\r\n")[0]);
					if (txt.split("###").length == 3) {
						modelo.setAgente(txt.split("###")[2].trim());
						modelo.setCveAgente(txt.split("###")[1].trim());
					}
				}

			} else {// aplica para version2 pdf
					// vigencia_de
				inicio = contenido.indexOf("@@@VIGENCIA###DESDE###LAS###12:00###HRS.###DEL:###") + 50;
				newcontenido = contenido.substring(inicio, inicio + 10);
				modelo.setVigenciaDe(fn.formatDate(newcontenido, "dd-MM-yy"));

				// vigencia_a
				inicio = contenido.indexOf("@@@HASTA###LAS###12:00###HRS.###DEL:###") + 39;
				newcontenido = contenido.substring(inicio, inicio + 10);
				modelo.setVigenciaA(fn.formatDate(newcontenido, "dd-MM-yy"));
			}

			// cp
			inicio = contenido.indexOf("INFORMACIÓN###GENERAL");
			fin = contenido.indexOf("CONCEPTOS###ECONÓMICOS");

			if (inicio > 0 && fin > 0 && inicio < fin) {
				newcontenido = contenido.substring(inicio, fin);
				for (String x : newcontenido.split("\n")) {
					if (x.contains("CÓDIGO POSTAL")) {
						modelo.setCp(x.split("CÓDIGO POSTAL")[1].split("###")[0].replace(":", "").trim());
					}
					if (x.contains("C.P")) {
						modelo.setCp(x.split("C.P")[1].split("###")[1].replace(":", "").replace("\r", "").trim());
					}

				}
			}

			// descripcion
			inicio = contenido.indexOf("DESCRIPCIÓN:");
			fin = contenido.indexOf("MARCA");
			if (inicio > -1 && fin > inicio) {
				newcontenido = contenido.substring(inicio + 12, fin).replace("@@@a", "").replace("@@@", "").trim();
				txt = "";
				for (String x : newcontenido.split("\r\n")) {
					if (x.trim().length() > 0 && x.trim().equals("a") == false) {
						if (x.contains("REMOLQUE")) {
							txt += fn.gatos(x.split("REMOLQUE")[0]) + " ";
						} else {
							txt += fn.gatos(x);
						}
					}
				}
				modelo.setDescripcion(txt.trim());
			}

			inicio = contenido.indexOf("MARCA:");
			if (inicio > -1) {
				newcontenido = fn.gatos(contenido.substring(inicio + 6, inicio + 150).split("\r\n")[0]);
				modelo.setMarca(newcontenido);
			} else {

				// marca
				inicio = contenido.indexOf("Marca:");
				fin = contenido.indexOf("Clase");
				if (inicio > -1 && fin > inicio) {
					txt = fn.gatos(contenido.substring(inicio + 6, fin));
					modelo.setMarca(txt);
				}
			}

			// clave
			inicio = contenido.indexOf("CLAVE###MAPFRE:");
			if (inicio > -1) {
				newcontenido = fn.gatos(contenido.substring(inicio + 15, inicio + 150).split("\r\n")[0]);
				modelo.setClave(newcontenido.trim());
			} else {

				// clave
				inicio = contenido.indexOf("Clave MAPFRE:");
				if (fin > -1) {
					txt = fn.gatos(contenido.substring(inicio + 13, inicio + 150).split("\r\n")[0]);
					modelo.setClave(txt);
				}
			}

			// modelo
			inicio = contenido.indexOf("FABRICACIÓN:");
			if (inicio > -1) {
				newcontenido = fn.gatos(contenido.substring(inicio + 12, inicio + 150).split("\r\n")[0]);
				if (fn.isNumeric(newcontenido)) {
					modelo.setModelo(fn.castInteger(newcontenido));
				}
			} else {

				// modelo
				inicio = contenido.indexOf("###Año") + 22;
				fin = contenido.indexOf("@@@Número");
				if (inicio > -1 && fin > inicio) {
					txt = fn.gatos(contenido.substring(inicio, fin));
					if (fn.isNumeric(txt)) {
						modelo.setModelo(fn.castInteger(txt));
					}
				}

			}

			// serie
			inicio = contenido.indexOf("SERIE:");
			if (inicio > -1) {
				txt = fn.gatos(contenido.substring(inicio + 6, inicio + 150).split("\r\n")[0]);
				if (txt.contains("PLACAS")) {
					txt = fn.gatos(txt.split("###")[0]).trim();
				}
				if (txt.split("###").length == 1) {
					modelo.setSerie(txt);
				}
			} else {
				// serie
				inicio = contenido.indexOf("de Serie:");
				fin = contenido.indexOf("Remolque:");
				if (inicio > -1 && fin > inicio) {
					txt = fn.gatos(contenido.substring(inicio + 9, fin));
					modelo.setSerie(txt);
				}
			}

			// motor
			int inimoto = contenido.indexOf("NÚMERO###DE###MOTOR:###");
			if (inimoto > 0) {
				inicio = contenido.indexOf("NÚMERO###DE###MOTOR:###");
				fin = contenido.indexOf("###AÑO###DE###FABRICACIÓN:###");
				if (inicio > 0 && fin > 0 && inicio < fin) {
					newcontenido = contenido.substring(inicio + 23, fin);
					modelo.setMotor(newcontenido.trim());
				}

			}

			// placas
			inicio = contenido.indexOf("PLACAS");
			if (inicio > -1) {
				txt = fn.gatos(contenido.substring(inicio + 6, inicio + 180).replace(":", "").split("\r\n")[0]);
				modelo.setPlacas(txt);
			} else {
				// placas
				inicio = contenido.indexOf("Placas:");
				if (inicio > -1) {
					txt = contenido.substring(inicio + 7, inicio + 150).split("\r\n")[0].trim();
					modelo.setPlacas(txt);
				}

			}

			// forma_pago
			inicio = contenido.indexOf("DE###PAGO:###");
			fin = contenido.indexOf("###PRIMA###NETA:");
			if (inicio > 0 && fin > 0 && inicio < fin) {
				modelo.setFormaPago(fn.formaPago(contenido.substring(inicio + 13, fin)));
			} else {
				inicio = contenido.indexOf("Gestor de cobro:");
				if (inicio > -1) {
					txt = contenido.substring(inicio + 16, inicio + 200).trim().split("\r\n")[0].replace("@@@", "")
							.trim();
					if (txt.split("###").length == 5 || txt.split("###").length == 4) {
						modelo.setFormaPago(fn.formaPago(txt.split("###")[1].trim()));
						modelo.setMoneda(fn.moneda(txt.split("###")[2].replace("$", "").trim()));
					}
				}
			}

			// conductor
			inicio = contenido.indexOf("CONDUCTOR###HABITUAL:");
			fin = contenido.indexOf("DOMICILIO:");
			if (inicio > -1) {
				txt = fn.gatos(contenido.substring(inicio + 21, fin).replace("@@@", "").trim());
				txtAux = "";
				index = 0;
				for (String x : txt.split("\r\n")) {
					if (index >= 0 && index <= 2) {
						x = fn.gatos(x);
						if (x.split("###").length == 2) {
							txtAux += x.split("###")[1].trim() + " ";
						} else if (x.trim().equals("a") == false) {
							txtAux += x.trim() + " ";
						}
					}
					index++;
				}
				modelo.setConductor(txtAux.trim());
			}
			if (modelo.getConductor().length() == 0) {// PARA OTRA VERSION
				inicio = contenido.indexOf("CONDUCTOR###HABITUAL");
				if (inicio > -1) {
					txtAux = fn.gatos(contenido.substring(inicio + 20, contenido.length()));
					inicio = txtAux.indexOf("NOMBRE:");
					if (inicio > -1) {
						txt = fn.gatos(txtAux.substring(inicio + 7, inicio + 150).split("\r\n")[0]);
						modelo.setConductor(txt);
					}
				}
			}

			// moneda
			inicio = contenido.indexOf("MONEDA:");
			fin = contenido.indexOf("GASTOS");
			if (inicio > 0 && fin > 0 && inicio < fin) {
				txt = fn.gatos(contenido.substring(inicio + 7, fin).replace("$", "")).trim();
				modelo.setMoneda(fn.moneda(txt));
			}

			// id_cliente
			inicio = contenido.indexOf("CLIENTE###MAPFRE:");
			if (inicio > -1) {
				txt = fn.gatos(contenido.substring(inicio + 17, inicio + 150).split("\r\n")[0]);
				modelo.setIdCliente(txt);
			}

			// fecha_emision
			modelo.setFechaEmision(modelo.getVigenciaDe());
			// COBERTURAS

			inicio = contenido.indexOf("###DEDUCIBLE");
			fin = contenido.indexOf("@@@INFORMACIÓN###ADICIONAL");
			List<EstructuraCoberturasModel> coberturas = new ArrayList<>();

			if (inicio > 0 && fin > 0 && inicio < fin) {

				newcontenido = contenido.substring(inicio + 12, fin).replace("@@@", "").replace("\r", "");

				for (String dato : newcontenido.split("\n")) {
					EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();
					if (dato.split("###").length == 3) {

						cobertura.setNombre(dato.split("###")[0]);
						cobertura.setDeducible(dato.split("###")[2]);
						cobertura.setSa(dato.split("###")[1]);
						coberturas.add(cobertura);
					}
					if (dato.split("###").length == 4) {

						cobertura.setNombre(dato.split("###")[0]);
						cobertura.setDeducible(dato.split("###")[3]);
						cobertura.setSa(dato.split("###")[2]);
						coberturas.add(cobertura);
					}
				}
				modelo.setCoberturas(coberturas);
			} else {
				inicio = inicontenido.indexOf("Deducible") + 110;
				fin = inicontenido.indexOf("Neta:");

				if (inicio > 0 && fin > 0 && inicio < fin) {

					newcontenido = fixGatos(inicontenido.substring(inicio, fin).replace("@@@", ""));
					for (String x : fixGatos(newcontenido).split("\n")) {
						EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();

						if (x.split("###").length == 3) {

							cobertura.setNombre(x.split("###")[0].trim());
							cobertura.setDeducible(x.split("###")[2].replace("\r", "").replace("\r\n", ""));
							cobertura.setSa(x.split("###")[2].replace("\r", "").replace("\r\n", ""));
							coberturas.add(cobertura);
						} else if (x.split("###").length == 4) {

							cobertura.setNombre(x.split("###")[0].trim());
							cobertura.setDeducible(x.split("###")[3].replace("\r", "").replace("\r\n", ""));
							cobertura.setSa(x.split("###")[2].trim());
							coberturas.add(cobertura);
						}
					}
				}
				modelo.setCoberturas(coberturas);
			}

			// **************************************RECIBOS
			List<EstructuraRecibosModel> recibosList = new ArrayList<>();
			EstructuraRecibosModel recibo = new EstructuraRecibosModel();
			if (recibos.length() > 0) {
				recibosList = recibosExtract();
			}

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
				recibo.setIva(modelo.getDerecho());
				recibo.setPrimaTotal(modelo.getPrimaTotal());
				recibo.setAjusteUno(modelo.getAjusteUno());
				recibo.setAjusteDos(modelo.getAjusteDos());
				recibo.setCargoExtra(modelo.getCargoExtra());
				recibosList.add(recibo);
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
			}
			modelo.setRecibos(recibosList);

			return modelo;
		} catch (Exception ex) {
			modelo.setError(
					MapfreAutosModel.this.getClass().getTypeName() + " | " + ex.getMessage() + " | " + ex.getCause());
			return modelo;
		}
	}

	private String fixGatos(String dato) { // RETORNA UNA CADENA, EN DONDE TENGA MAS DE 2 ESPACIOS PONE ###
		boolean encontro_grupo = false;
		int par = 0;
		String newdato = "";
		for (int i = 0; i < dato.length(); i++) {
			if (dato.charAt(i) == ' ') {
				if (encontro_grupo == false) {
					par = par + 1;
					if (par == 2) {
						encontro_grupo = true;
						newdato = newdato.trim();
						newdato += "###";
					} else {
						newdato += Character.toString(dato.charAt(i));
					}
				}
			} else {
				par = 0;
				encontro_grupo = false;
				newdato += Character.toString(dato.charAt(i));
			}
		}
		return newdato;
	}

	private ArrayList<EstructuraRecibosModel> recibosExtract() {
		recibos = fn.fixContenido(recibos);
		List<EstructuraRecibosModel> recibosLis = new ArrayList<>();
		try {

			ArrayList<String> series = new ArrayList<String>();
			String actualRec = "";
			index = 0;
			for (String x : recibos.split("FACTURA ELECTRÓNICA")) {
				EstructuraRecibosModel recibo = new EstructuraRecibosModel();
				// recibo_id
				inicio = x.indexOf("serie P No.");
				if (inicio > -1) {
					newcontenido = fn.gatos(x.substring(inicio + 11, x.indexOf("\r\n", inicio + 11)));
					actualRec = newcontenido;
				}
				if (index == 1 && actualRec.length() > 0
						|| (series.contains(actualRec) == false && actualRec.length() > 0)) {

					recibo.setReciboId(actualRec);

					// prima_neta
					inicio = x.indexOf("Prima neta###");
					if (inicio > -1) {
						newcontenido = x.substring(inicio + 13, x.indexOf("\r\n", inicio));
						if (newcontenido.split("###").length == 2) {
							recibo.setPrimaneta(fn.castBigDecimal(
									newcontenido.split("###")[1].replace(",", "").replace("MXN", "").trim(), 2));
							restoPrimaNeta = modelo.getPrimaneta().subtract(recibo.getPrimaneta());
						}
					}

					// derecho
					inicio = x.indexOf("Gasto de expedición");
					if (inicio > -1) {
						newcontenido = fn.gatos(x.substring(inicio + 19, inicio + 150).split("\r\n")[0]);
						if (newcontenido.split("###").length == 2) {
							recibo.setDerecho(fn.castBigDecimal(
									newcontenido.split("###")[1].replace(",", "").replace("MXN", "").trim(), 2));
							restoDerecho = modelo.getDerecho().subtract(recibo.getDerecho());
						}
					}

					// recargo
					inicio = x.indexOf("Financ. pago fracc.");
					if (inicio > -1) {
						newcontenido = fn.gatos(x.substring(inicio + 19, x.indexOf("\r\n", inicio)));
						if (newcontenido.split("###").length == 2) {
							recibo.setRecargo(fn.castBigDecimal(
									newcontenido.split("###")[1].replace(",", "").replace("MXN", "").trim(), 2));
							restoRecargo = modelo.getRecargo().subtract(recibo.getRecargo());
						}
					}

					// iva
					inicio = x.indexOf("IVA 16.00%");
					if (inicio > -1) {
						newcontenido = x.substring(inicio, x.indexOf("\r\n", inicio));
						if (newcontenido.split("###").length == 3) {
							recibo.setIva(fn.castBigDecimal(
									newcontenido.split("###")[2].replace(",", "").replace("MXN", "").trim(), 2));
							restoIva = modelo.getIva().subtract(recibo.getIva());
						}
					}

					// prima_total
					inicio = x.indexOf("Total a pagar:");
					if (inicio > -1) {
						newcontenido = x.substring(inicio, x.indexOf("\r\n", inicio));
						if (newcontenido.split("###").length == 3) {
							recibo.setPrimaTotal(fn.castBigDecimal(
									newcontenido.split("###")[2].replace(",", "").replace("MXN", "").trim(), 2));
							restoPrimaTotal = modelo.getPrimaTotal().subtract(recibo.getPrimaTotal());

						}
					}

					// vigencia_de
					// vigencia_a
					inicio = x.indexOf("horas del ");
					if (inicio > -1) {
						newcontenido = x.substring(inicio + 10, x.indexOf("\r\n", inicio + 10)).split("Gasto de ")[0];
						if (newcontenido.contains("Hasta: 12:00 horas del")) {
							resultado = fn.gatos(newcontenido.split("Hasta: 12:00 horas del")[0]).trim();
							if (resultado.length() == 9) {
								recibo.setVigenciaDe("20" + resultado.split("-")[0] + "-"
										+ fn.mes(resultado.split("-")[1]) + "-" + resultado.split("-")[2]);
							} else {
								recibo.setVigenciaDe(resultado);
							}

							resultado = fn.gatos(newcontenido.split("Hasta: 12:00 horas del")[1]).trim();
							if (resultado.length() == 9) {
								recibo.setVigenciaA("20" + resultado.split("-")[0] + "-"
										+ fn.mes(resultado.split("-")[1]) + "-" + resultado.split("-")[2]);
							} else {
								recibo.setVigenciaA(resultado);
							}
						}
					}

					// vencimiento
					if (recibo.getVigenciaDe().length() > 0) {
						recibo.setVencimiento(fn.dateAdd(recibo.getVigenciaDe(), 30, 1));
					}

					// serie
					inicio = x.indexOf("Serie de recibo:");
					if (inicio > -1) {
						newcontenido = fn.gatos(x.substring(inicio + 16, x.indexOf("Fecha", inicio + 16)));
						newcontenido = newcontenido.replace("01", "1").replace("02", "2").replace("03", "3")
								.replace("04", "4").replace("05", "5").replace("06", "6").replace("07", "7")
								.replace("08", "8").replace("09", "9");
						recibo.setSerie(newcontenido);
					}
					recibosLis.add(recibo);
					series.add(actualRec);
				}
				index++;
			}

			return (ArrayList<EstructuraRecibosModel>) recibosLis;

		} catch (Exception ex) {
			modelo.setError(
					MapfreAutosModel.this.getClass().getTypeName() + " | " + ex.getMessage() + " | " + ex.getCause());
			return (ArrayList<EstructuraRecibosModel>) recibosLis;
		}

	}

}
