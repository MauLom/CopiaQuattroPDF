package com.copsis.models.axa;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraCoberturasModel;
import com.copsis.models.EstructuraJsonModel;
import com.copsis.models.EstructuraRecibosModel;

public class AxaAutosModel {
	// Clases
	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
	// Varaibles
	private String contenido = "";
	private String newcontenido = "";
	private String recibosText = "";
	private String resultado = "";
	private int inicio = 0;
	private int fin = 0;
	private int donde = 0;
	private BigDecimal restoPrimaTotal = BigDecimal.ZERO;
	private BigDecimal restoDerecho = BigDecimal.ZERO;
	private BigDecimal restoIva = BigDecimal.ZERO;
	private BigDecimal restoRecargo = BigDecimal.ZERO;
	private BigDecimal restoPrimaNeta = BigDecimal.ZERO;
	private BigDecimal restoAjusteUno = BigDecimal.ZERO;
	private BigDecimal restoAjusteDos = BigDecimal.ZERO;
	private BigDecimal restoCargoExtra = BigDecimal.ZERO;
	
	public AxaAutosModel(String contenido, String recibos) {
		this.contenido = contenido;
		this.recibosText = recibos;
	}

	public EstructuraJsonModel procesar() {

		contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());
		try {
			// tipo
			modelo.setTipo(1);
			// cia
			modelo.setCia(20);

			// poliza
			// cte_nombre
			donde = 0;
			donde = fn.recorreContenido(contenido, "Datos del asegurado");
			if (donde > 0) {
				for (String dato : contenido.split("@@@")[donde + 1].split("\r\n")) {
					if (dato.contains("Nombre:###")) {
						modelo.setCteNombre(dato.split("Nombre:###")[1].replace("###", " ").trim());
					} else {
						modelo.setPoliza(dato.trim());
					}
				}
			}

			// cp
			// cte_direccion
			donde = 0;
			donde = fn.recorreContenido(contenido, "Domicilio:###");
			if (donde > 0 && contenido.split("@@@")[donde].split("\r\n").length == 2) {

					if (contenido.split("@@@")[donde].split("\r\n")[0].contains("Domicilio:###")) {
						if (contenido.split("@@@")[donde].split("\r\n")[0].split("###")[0].trim()
								.equals("Domicilio:") == false) {
							newcontenido = contenido.split("@@@")[donde].split("\r\n")[0].split("###")[0].trim();
						} else {
							if (contenido.split("@@@")[donde].split("\r\n")[0].contains("Vigencia")
									&& contenido.split("@@@")[donde].split("\r\n")[0].split("###")[0].trim()
											.equals("Domicilio:")) {
								newcontenido += contenido.split("@@@")[donde].split("\r\n")[0].split("Domicilio:")[1]
										.split("Vigencia")[0].replace("###", " ").trim();
							}
						}
						if (contenido.split("@@@")[donde].split("\r\n")[1].contains("###Desde")) {
							newcontenido = contenido.split("@@@")[donde].split("\r\n")[1].split("Desde")[0].trim();
							for (int i = 0; i < newcontenido.split("###").length; i++) {
								if (newcontenido.split("###")[i].trim().equals("C.P.") == false) {
									if ((i + 1) < newcontenido.split("###").length && i > 0 || i >= 1) {
										if (newcontenido.split("###")[i - 1].trim().equals("C.P.") == false) {
											newcontenido += " " + newcontenido.split("###")[i].trim();
										} else {
											modelo.setCp(newcontenido.split("###")[i].trim());
										}
									} else {
										newcontenido += " " + newcontenido.split("###")[i].trim();
									}
								}
							}
						}
						modelo.setCteDireccion(newcontenido);
					}
				
			}

			// rfc
			// moneda
			donde = 0;
			donde = fn.recorreContenido(contenido, "R.F.C.:###");
			if (donde > 0) {
				if (contenido.split("@@@")[donde].split("\r\n").length == 2) {
					if (contenido.split("@@@")[donde].split("\r\n")[0].contains("R.F.C.:###")) {
						modelo.setRfc(contenido.split("@@@")[donde].split("\r\n")[0].split("###")[1].trim());
					}
					if (contenido.split("@@@")[donde].split("\r\n")[1].contains("###Moneda:###")
							&& contenido.split("@@@")[donde].split("\r\n")[1].split("###").length == 3) {
						modelo.setMoneda(
								fn.moneda(contenido.split("@@@")[donde].split("\r\n")[1].split("###")[2].trim()));
					}
				}
			}

			// vigencia_a
			// vigencia_de
			donde = 0;
			donde = fn.recorreContenido(contenido, "Desde:");
			if (donde > 0) {
				if (contenido.split("@@@")[donde].split("\r\n").length == 2) {
					if (contenido.split("@@@")[donde].split("\r\n")[1].split("Desde")[1].trim().split("/").length == 3
							&& contenido.split("@@@")[donde].split("\r\n")[1].split("Desde")[1]
									.split("###").length == 2) {
						modelo.setVigenciaDe(fn.formatDateMonthCadena(
								contenido.split("@@@")[donde].split("\r\n")[1].split("Desde")[1].split("###")[1]
										.trim()));
					}
				}
				if (contenido.split("@@@")[donde + 1].split("\r\n").length == 1
						&& contenido.split("@@@")[donde + 1].contains("Hasta:###")) {
					modelo.setVigenciaA(
							fn.formatDateMonthCadena(contenido.split("@@@")[donde + 1].split("###")[1].trim()));
				}
			}

			// descripcion
			donde = 0;
			donde = fn.recorreContenido(contenido, "Vehículo:###");
			if (donde > 0) {
				for (String dato : contenido.split("@@@")[donde].split("\r\n")) {
					if (dato.contains("Vehículo:###")) {
						modelo.setDescripcion(dato.split("Vehículo:###")[1].replace("###", " ").trim());
					}
				}
			}

			// motor
			// modelo
			// serie
			// endoso
			// placas
			donde = 0;
			donde = fn.recorreContenido(contenido, "Motor:");
			if (donde > 0) {
				for (String dato : contenido.split("@@@")[donde].split("\r\n")) {
					if (dato.contains("Motor:") && dato.contains("Modelo:") && dato.split("###").length == 4) {
						modelo.setMotor(dato.split("###")[1].trim());
						modelo.setModelo(Integer.parseInt(dato.split("###")[3].trim()));
					}
					if (dato.contains("Serie:") && dato.split("###").length == 5) {
						modelo.setSerie(dato.split("###")[1].trim());
						modelo.setEndoso(dato.split("###")[4].trim());
					}
					if (dato.contains("Placas:") && dato.split("###").length == 2) {
						modelo.setPlacas(dato.split("###")[1].trim());
					} else if (dato.contains("Placas:") && dato.split("###").length == 4) {
						modelo.setPlacas(dato.split("###")[1].trim());
					}
				}
			}

			// forma_pago
			donde = 0;
			donde = fn.recorreContenido(contenido, "Datos adicionales");
			if (donde > 0) {
				for (String dato : contenido.split("@@@")[donde].split("\r\n")) {
					if (dato.contains("Datos adicionales") && dato.split("###").length == 3) {
						if (dato.split("###")[1].split(" ").length == 2) {
							modelo.setFormaPago(fn.formaPago(dato.split("###")[1].split(" ")[0].trim()));
						} else {
							modelo.setFormaPago(fn.formaPago(dato.split("###")[1].replace("", "").trim()));
						}
					}
				}
			}

			// agente
			// cve_agente

			donde = 0;
			donde = fn.recorreContenido(contenido, "Agente:###");
			if (donde > 0) {
				if (contenido.split("@@@")[donde].split("\r\n").length == 2) {
					for (String dato : contenido.split("@@@")[donde].split("\r\n")) {
						if (dato.contains("Agente:###")) {
							modelo.setCveAgente(dato.split("###")[1].trim());
							newcontenido = dato.split("###")[2].trim();
						} 
						if (dato.split("###").length == 1) {
							newcontenido += " " + dato.trim();
						}
					}
					modelo.setAgente(newcontenido);
				} else if (contenido.split("@@@")[donde].split("\r\n").length == 1) {
					if (contenido.split("@@@")[donde].contains("Agente:###")
							&& contenido.split("@@@")[donde].contains("No.")
							&& contenido.split("@@@")[donde].split("###").length == 6) {
						modelo.setCveAgente(contenido.split("@@@")[donde].split("###")[1].trim());
						modelo.setAgente(contenido.split("@@@")[donde].split(modelo.getCveAgente())[1].split("No.")[0]
								.replace("###", " ").trim());
					}
				}
			}

			// conductor
			donde = 0;
			donde = fn.recorreContenido(contenido, "Nombre:###Edad:###Sexo:");
			if (donde > 0) {
				for (String dato : contenido.split("@@@")[donde].split("\r\n")) {
					if (dato.contains("Nombre:###Edad:###Sexo:") == false) {
						inicio = dato.split("###").length;
						if (dato.split("###").length >= 5) {
							modelo.setConductor(
									dato.split(dato.split("###")[inicio - 2])[0].replace("###", " ").trim());
						}
					}
				}
			}

			// derecho
			// recargo
			// prima_neta
			donde = 0;
			donde = fn.recorreContenido(contenido, "Prima neta");
			if (donde > 0) {
				for (String dato : contenido.split("@@@")[donde].split("\r\n")) {
					if (dato.split("###").length == 2 && dato.contains("Prima neta")) {

						modelo.setPrimaneta(fn.castBigDecimal(fn.preparaPrimas(dato.split("###")[1].trim())));

					}
					if (dato.split("###").length == 2 && dato.contains("financiamiento")) {
						modelo.setRecargo(fn.castBigDecimal(fn.cleanString(dato.split("###")[1].trim())));
					}
					if (dato.split("###").length == 2 && dato.contains("expedición")) {
						modelo.setDerecho(fn.castBigDecimal(fn.cleanString(dato.split("###")[1].trim())));
					}
				}
			}

			// plan
			inicio = contenido.indexOf("Carátula de Póliza");
			fin = contenido.indexOf("Datos del asegurado");
			if (inicio > -1 && fin > inicio) {
				newcontenido = contenido.substring(inicio + 18, fin).replace("@@@", "").trim();
				if (newcontenido.split("\r\n").length == 1) {
					donde = newcontenido.split("/").length;
					if (donde == 3) {
						modelo.setPlan(newcontenido.split("-")[donde - 2].trim());
					}
				}
			}

			// iva
			// prima_total
			donde = 0;
			donde = fn.recorreContenido(contenido, "I.V.A.");
			if (donde > 0) {
				for (String dato : contenido.split("@@@")[donde].split("\r\n")) {
					if (dato.split("###").length == 2 && dato.contains("I.V.A.")) {
						modelo.setIva(fn.castBigDecimal(fn.cleanString(dato.split("###")[1].trim())));
					}
					if (dato.split("###").length == 2 && dato.contains("Precio Total")) {
						modelo.setPrimaTotal(fn.castBigDecimal(fn.cleanString(dato.split("###")[1].trim())));
					}
				}
			}

			// inciso
			modelo.setInciso(1);

			// marca
			if (modelo.getDescripcion().length() > 0)
				newcontenido = modelo.getDescripcion();
			inicio = newcontenido.trim().split(" ").length;
			if (inicio > 1) {
				newcontenido = newcontenido.split(" ")[0];
				modelo.setMarca(newcontenido);
			}

			// fecha_emision//tipo
			modelo.setTipo(1);
			// cia
			modelo.setCia(20);

			// poliza
			// cte_nombre
			donde = 0;
			donde = fn.recorreContenido(contenido, "Datos del asegurado");
			if (donde > 0) {
				for (String dato : contenido.split("@@@")[donde + 1].split("\r\n")) {
					if (dato.contains("Nombre:###")) {
						modelo.setCteNombre(dato.split("Nombre:###")[1].replace("###", " ").trim());
					} else {
						modelo.setPoliza(dato.trim());
					}
				}
			}

			// cp
			// cte_direccion
			donde = 0;
			donde = fn.recorreContenido(contenido, "Domicilio:###");
			if (donde > 0) {
				if (contenido.split("@@@")[donde].split("\r\n").length == 2) {
					if (contenido.split("@@@")[donde].split("\r\n")[0].contains("Domicilio:###")) {
						if (contenido.split("@@@")[donde].split("\r\n")[0].split("###")[0].trim()
								.equals("Domicilio:") == false) {
							resultado = contenido.split("@@@")[donde].split("\r\n")[0].split("###")[0].trim();
						} else {
							if (contenido.split("@@@")[donde].split("\r\n")[0].contains("Vigencia")
									&& contenido.split("@@@")[donde].split("\r\n")[0].split("###")[0].trim()
											.equals("Domicilio:")) {
								resultado += contenido.split("@@@")[donde].split("\r\n")[0].split("Domicilio:")[1]
										.split("Vigencia")[0].replace("###", " ").trim();
							}
						}
						if (contenido.split("@@@")[donde].split("\r\n")[1].contains("###Desde")) {
							newcontenido = contenido.split("@@@")[donde].split("\r\n")[1].split("Desde")[0].trim();
							for (int i = 0; i < newcontenido.split("###").length; i++) {
								if (newcontenido.split("###")[i].trim().equals("C.P.") == false) {
									if ((i + 1) < newcontenido.split("###").length && i > 0 || i >= 1) {
										if (newcontenido.split("###")[i - 1].trim().equals("C.P.") == false) {
											resultado += " " + newcontenido.split("###")[i].trim();
										} else {
											modelo.setCp(newcontenido.split("###")[i].trim());
										}
									} else {
										resultado += " " + newcontenido.split("###")[i].trim();
									}
								}
							}
						}
						modelo.setCteDireccion(resultado.replace("###", " "));
					}
				}
			}

			// rfc
			// moneda
			donde = 0;
			donde = fn.recorreContenido(contenido, "R.F.C:###");
			if (donde > 0) {
				if (contenido.split("@@@")[donde].split("\r\n").length == 2) {
					if (contenido.split("@@@")[donde].split("\r\n")[0].contains("R.F.C.:###")) {
						modelo.setRfc(contenido.split("@@@")[donde].split("\r\n")[0].split("###")[1].trim());
					}
					if (contenido.split("@@@")[donde].split("\r\n")[1].contains("###Moneda:###")
							&& contenido.split("@@@")[donde].split("\r\n")[1].split("###").length == 3) {
						modelo.setMoneda(
								fn.moneda(contenido.split("@@@")[donde].split("\r\n")[1].split("###")[2].trim()));
					}
				}
			}
	

			// vigencia_a
			// vigencia_de
			donde = 0;
			donde = fn.recorreContenido(contenido, "Desde:");
			if (donde > 0) {
				if (contenido.split("@@@")[donde].split("\r\n").length == 2) {
					if (contenido.split("@@@")[donde].split("\r\n")[1].split("Desde")[1].trim().split("-").length == 3
							&& contenido.split("@@@")[donde].split("\r\n")[1].split("Desde")[1]
									.split("###").length == 2) {
						
					
						modelo.setVigenciaDe(fn.formatDateMonthCadena(
								contenido.split("@@@")[donde].split("\r\n")[1].split("Desde")[1].split("###")[1]
										.trim()));
					}
				}
				if (contenido.split("@@@")[donde + 1].split("\r\n").length == 1
						&& contenido.split("@@@")[donde + 1].contains("Hasta:###")) {
					modelo.setVigenciaA(
							fn.formatDateMonthCadena(contenido.split("@@@")[donde + 1].split("###")[1].trim()));
				}
			}

			// descripcion
			donde = 0;
			donde = fn.recorreContenido(contenido, "Vehículo:###");
			if (donde > 0) {
				for (String dato : contenido.split("@@@")[donde].split("\r\n")) {
					if (dato.contains("Vehículo:###")) {
						modelo.setDescripcion(dato.split("Vehículo:###")[1].replace("###", " ").trim());
					}
				}
			}

			// motor
			// modelo
			// serie
			// endoso
			// placas
			donde = 0;
			donde = fn.recorreContenido(contenido, "Motor:");
			if (donde > 0) {
				for (String dato : contenido.split("@@@")[donde].split("\r\n")) {
					if (dato.contains("Motor:") && dato.contains("Modelo:") && dato.split("###").length == 4) {
						modelo.setMotor(dato.split("###")[1].trim());
						modelo.setModelo(Integer.parseInt(dato.split("###")[3].trim()));
					}
					if (dato.contains("Serie:") && dato.split("###").length == 5) {
						modelo.setSerie(dato.split("###")[1].trim());
						modelo.setEndoso(dato.split("###")[4].trim());
					}
					if (dato.contains("Placas:") && dato.split("###").length == 2) {
						modelo.setPlacas(dato.split("###")[1].trim());
					} else if (dato.contains("Placas:") && dato.split("###").length == 4) {
						modelo.setPlacas(dato.split("###")[1].trim());
					}
				}
			}

			// forma_pago
			donde = 0;
			donde = fn.recorreContenido(contenido, "Datos adicionales");
			if (donde > 0) {
				for (String dato : contenido.split("@@@")[donde].split("\r\n")) {
					if (dato.contains("Datos adicionales") && dato.split("###").length == 3) {
						if (dato.split("###")[1].split(" ").length == 2) {
							modelo.setFormaPago(fn.formaPago(dato.split("###")[1].split(" ")[0].trim()));
						} else {
							modelo.setFormaPago(fn.formaPago(dato.split("###")[1].replace("", "").trim()));
						}
					}
				}
			}

			// agente
			// cve_agente

			donde = 0;
			donde = fn.recorreContenido(contenido, "Agente:###");
			if (donde > 0) {
				if (contenido.split("@@@")[donde].split("\r\n").length == 2) {
					for (String dato : contenido.split("@@@")[donde].split("\r\n")) {
						if (dato.contains("Agente:###") && dato.contains("OT:") && dato.split("###").length == 6) {
							modelo.setCveAgente(dato.split("###")[1].trim());
							newcontenido = dato.split("###")[2].trim();
						} else if (dato.contains("Agente:###") && dato.contains("No.")
								&& dato.split("###").length == 4) {
							modelo.setCveAgente(dato.split("###")[1].trim());
							newcontenido = dato.split("###")[2].trim();
						}
						if (dato.split("###").length == 1) {
							newcontenido += " " + dato.trim();
						}
					}
					modelo.setAgente(newcontenido);
				} else if (contenido.split("@@@")[donde].split("\r\n").length == 1) {
					if (contenido.split("@@@")[donde].contains("Agente:###")
							&& contenido.split("@@@")[donde].contains("No.")
							&& contenido.split("@@@")[donde].split("###").length == 6) {
						modelo.setCveAgente(contenido.split("@@@")[donde].split("###")[1].trim());
						modelo.setAgente(contenido.split("@@@")[donde].split(modelo.getCveAgente())[1].split("No.")[0]
								.replace("###", " ").trim());
					}
				}
			}

			// conductor
			donde = 0;
			donde = fn.recorreContenido(contenido, "Nombre:###Edad:###Sexo:");
			if (donde > 0) {
				for (String dato : contenido.split("@@@")[donde].split("\r\n")) {
					if (dato.contains("Nombre:###Edad:###Sexo:") == false) {
						donde = dato.split("###").length;
						if (dato.split("###").length >= 5) {
							modelo.setConductor(dato.split(dato.split("###")[donde - 2])[0].replace("###", " ").trim());
						}
					}
				}
			}

			// derecho
			// recargo
			// prima_neta
			donde = 0;
			donde = fn.recorreContenido(contenido, "Prima neta");
			if (donde > 0) {
				for (String dato : contenido.split("@@@")[donde].split("\r\n")) {
					if (dato.split("###").length == 2 && dato.contains("Prima neta")) {
						modelo.setPrimaneta(fn.castBigDecimal(fn.cleanString(dato.split("###")[1].trim())));
					}
					if (dato.split("###").length == 2 && dato.contains("financiamiento")) {
						modelo.setRecargo(fn.castBigDecimal(fn.cleanString(dato.split("###")[1].trim())));
					}
					if (dato.split("###").length == 2 && dato.contains("expedición")) {
						modelo.setDerecho(fn.castBigDecimal(fn.cleanString(dato.split("###")[1].trim())));
					}
				}
			}

			// plan
			inicio = contenido.indexOf("Carátula de Póliza");
			fin = contenido.indexOf("Datos del asegurado");
			if (inicio > -1 && fin > inicio) {
				newcontenido = contenido.substring(inicio + 18, fin).replace("@@@", "").trim();
				if (newcontenido.split("\r\n").length == 1) {
					inicio = newcontenido.split("-").length;
					if (inicio == 3) {
						modelo.setPlan(newcontenido.split("-")[inicio - 2].trim());
					}
				}
			}

			// iva
			// prima_total
			donde = 0;
			donde = fn.recorreContenido(contenido, "I.V.A.");
			if (donde > 0) {
				for (String dato : contenido.split("@@@")[donde].split("\r\n")) {
					if (dato.split("###").length == 2 && dato.contains("I.V.A.")) {
						modelo.setIva(fn.castBigDecimal(fn.cleanString(dato.split("###")[1].trim())));
					}
					if (dato.split("###").length == 2 && dato.contains("Precio Total")) {
						modelo.setPrimaTotal(fn.castBigDecimal(fn.cleanString(dato.split("###")[1].trim())));
					}
				}
			}

			// inciso
			modelo.setInciso(1);

			// marca
			if (modelo.getDescripcion().length() > 0)
				newcontenido = modelo.getDescripcion();
			inicio = newcontenido.trim().split(" ").length;
			if (inicio > 1) {
				newcontenido = newcontenido.split(" ")[0];
				modelo.setMarca(newcontenido);
			}

			// fecha_emision
			inicio = contenido.indexOf("Emisión:");
			if (inicio > -1) {
				newcontenido = fn.gatos(contenido.substring(inicio + 8, inicio + 100).split("\n")[0].trim());
				modelo.setFechaEmision(fn.formatDateMonthCadena(newcontenido));
			}

			// id_cliente
			inicio = contenido.indexOf("No. de cliente");
			fin = contenido.indexOf("Conductores");
			if (fin == -1) {
				fin = contenido.indexOf("Coberturas");
			}
			if (inicio > -1 && fin > inicio) {
				newcontenido = contenido.substring(inicio + 14, fin).replace("@@@", "").trim();
				if (newcontenido.split("\n").length == 2) {
					if (newcontenido.split("\n")[1].trim().split(" ").length == 1) {
						modelo.setIdCliente(newcontenido.split("\n")[1].trim());
					}
				} else if (newcontenido.split(" ").length == 1) {
					modelo.setIdCliente(newcontenido);
				}
			}

			List<EstructuraCoberturasModel> coberturas = new ArrayList<>();
			// coberturas{nombre, sa, deducible}
			inicio = 0;
			fin = 0;
			inicio = contenido.indexOf("Coberturas amparadas###Suma asegurada###Deducible###Prima");
			fin = contenido.indexOf("Prima neta###");
			if (inicio > 0 && fin > 0) {
				for (String dato : contenido.substring(inicio + 57, fin).replace(".....", "").trim().split("\n")) {
					EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();
					if (dato.split("###").length == 4) {

						cobertura.setNombre(dato.split("###")[0].replace("@@@", "").trim());
						cobertura.setSa(dato.split("###")[1]);
						cobertura.setDeducible(dato.split("###")[2].trim());
						coberturas.add(cobertura);
					} else if (dato.split("###").length == 3) {

						cobertura.setNombre(dato.split("###")[0].replace("@@@", "").trim());
						cobertura.setSa(dato.split("###")[1].trim());
						coberturas.add(cobertura);
					}
				}
			}
			modelo.setCoberturas(coberturas);

			// if(recibos.length() > 0) jsonArrayRec = getRecibos();

			List<EstructuraRecibosModel> recibosList = new ArrayList<>();
			

		
			if (recibosText.length() > 0) {
				recibosList = recibosExtract();
				
				
			}

			// **************************************RECIBOS
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
					EstructuraRecibosModel recibo = new EstructuraRecibosModel();
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
					int meses = fn.monthAdd(modelo.getFormaPago());// MESES A AGREGAR POR RECIBOs
					int ct =recibosList.size();
					int x=0;
					for (int i = 0; i < restoRec.intValue(); i++) {
						x++;
						EstructuraRecibosModel recibo = new EstructuraRecibosModel();						
						recibo.setSerie((x+ct) + "/" + totalRec);
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
					
					int t = 0;
					for (int i = 0; i < recibosList.size(); i++) {
						t++;
						if (i >= ct) {
							recibosList.get(i).setVigenciaDe(recibosList.get(i - 1).getVigenciaA());
							if (recibosList.get(i - 1).getVigenciaA().length() == 10) {
								recibosList.get(i)
										.setVigenciaA(fn.dateAdd(recibosList.get(i - 1).getVigenciaA(), meses, 2));
							}
						}
					}
					

						
					
				}

				break;
			case 5:
			case 6:// QUINCENAL, SEMANAL NINGUN PDF DE FORMA DE PAGO SE QUEDA PENDIENTE ESTE CASO
				if (recibosList.size() >= 1) {
					BigDecimal restoRec = fn.castBigDecimal(fn.getTotalRec(modelo.getFormaPago()) - recibosList.size());
					int totalRec = fn.getTotalRec(modelo.getFormaPago());
					for (int i = recibosList.size(); i <= restoRec.intValue(); i++) {
						EstructuraRecibosModel recibo = new EstructuraRecibosModel();
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
			modelo.setError(
					AxaAutosModel.this.getClass().getTypeName() + " | " + ex.getMessage() + " | " + ex.getCause());
			return modelo;
		}
	}

	private ArrayList<EstructuraRecibosModel> recibosExtract() {
		recibosText = fn.fixContenido(recibosText);
		List<EstructuraRecibosModel> recibosLis = new ArrayList<>();

		try {
			int index = 0;
			int totalRec = fn.getTotalRec(modelo.getFormaPago());
			int recibosSerie = 1;
			
			for (String a : recibosText.split("RECIBO PROVISIONAL DE PAGO")) {
				EstructuraRecibosModel recibo = new EstructuraRecibosModel();
				if (index > 0) {				
					// serie
					recibo.setSerie(recibosSerie + "/" + totalRec);

					// recibo_id

					inicio = a.indexOf("Identificador:");
					fin = a.indexOf("Gastos por");
					if (inicio > -1 && fin > inicio) {
						newcontenido = fn.gatos(a.substring(inicio + 14, fin)).trim();
						recibo.setReciboId(newcontenido);
					}

					// vigencia_de

					// vigencia_a

					inicio = a.indexOf("Periodo cubierto:");
					if (inicio > -1) {
						newcontenido = a.substring(inicio + 17, inicio + 200);
						if (newcontenido.contains("Concepto")) {
							newcontenido = fn.gatos(newcontenido.split("Concepto")[0].trim()).replace("\r\n", "")
									.replace("al@@@", "").replace("Del###", "").replace("al###", "")
									.replace(" de ", "/").replace("@@@", "").trim();
						
							switch (newcontenido.split("###").length) {
							case 2:
								resultado = newcontenido.split("###")[0].trim();							
								if (resultado.split("/").length == 3)
									recibo.setVigenciaDe(fn.formatDateMonthCadena(resultado.replace("/", "-")));
								
								
								resultado = newcontenido.split("###")[1].trim();
								if (resultado.split("/").length == 3 && resultado.length() > 10)
									recibo.setVigenciaA(fn.formatDateMonthCadena(resultado.replace("/", "-")));
								break;
							}
						}
					}

					// prima_total

					inicio = a.indexOf("Total a Pagar");
					if (inicio > -1) {
						newcontenido = fn
								.cleanString(fn.gatos(a.substring(inicio + 13, inicio + 60).split("\n")[0].trim()));
						if (fn.isNumeric(newcontenido)) {
							recibo.setPrimaTotal(fn.castBigDecimal(fn.preparaPrimas(newcontenido)));
							restoPrimaTotal = modelo.getPrimaTotal().subtract(recibo.getPrimaTotal());

						}
					}

					// derecho

					inicio = a.indexOf("por Expedición");
					if (inicio > -1)
						newcontenido = fn.gatos(a.substring(inicio + 14, inicio + 100).split("\n")[0].trim());
					newcontenido = fn.cleanString(newcontenido);
					if (fn.isNumeric(newcontenido))

					recibo.setDerecho(fn.castBigDecimal(fn.preparaPrimas(newcontenido)));
					restoDerecho = modelo.getDerecho().subtract(recibo.getDerecho());

					// iva
			
					inicio = a.indexOf("I.V.A.");
					if (inicio > -1) {
						newcontenido = fn
								.cleanString(fn.gatos(a.substring(inicio + 6, inicio + 100).split("\n")[0].trim()));
						switch (newcontenido.split("###").length) {
						case 2:
							newcontenido = newcontenido.split("###")[1].trim();
							if (fn.isNumeric(newcontenido)) {								
								recibo.setIva(fn.castBigDecimal(fn.preparaPrimas(newcontenido)));
								restoIva = modelo.getIva().subtract(recibo.getIva());														
							}
							break;
						}
					}

					// recargo
				
					inicio = a.indexOf("de Financiamiento");
					if (inicio > -1)
						newcontenido = fn.gatos(a.substring(inicio + 17, inicio + 100).split("\n")[0].trim());
					newcontenido = fn.cleanString(newcontenido);
					if (fn.isNumeric(newcontenido))
						
						recibo.setRecargo(fn.castBigDecimal(fn.preparaPrimas(newcontenido)));
					restoRecargo = modelo.getRecargo().subtract(recibo.getRecargo());
						
				

					// prima_neta				
					inicio = a.indexOf("Prima Neta");
					if (inicio > -1)
						newcontenido = fn.gatos(a.substring(inicio + 10, inicio + 100).split("\n")[0].trim());
					newcontenido = fn.cleanString(newcontenido);
					if (fn.isNumeric(newcontenido))
					recibo.setPrimaneta(fn.castBigDecimal(fn.preparaPrimas(newcontenido)));
					restoPrimaNeta = modelo.getPrimaneta().subtract(recibo.getPrimaneta());		
					recibosLis.add(recibo);
					recibosSerie++;
					
				}
				index++;
			
			}
			
			return (ArrayList<EstructuraRecibosModel>) recibosLis;
		} catch (Exception ex) {
			modelo.setError(
					AxaAutosModel.this.getClass().getTypeName() + " | " + ex.getMessage() + " | " + ex.getCause());

			return (ArrayList<EstructuraRecibosModel>) recibosLis;
		}
	}

}
