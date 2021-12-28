package com.copsis.models.axa;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import com.copsis.constants.ConstantsValue;
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
	private String recibosText = "";
	private int inicio = 0;
	private int fin = 0;

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
		int donde = 0;
		StringBuilder newcontenido = new StringBuilder();
		StringBuilder resultado = new StringBuilder();

		contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());
		try {
			// tipo
			modelo.setTipo(1);
			// cia
			modelo.setCia(20);

			// poliza
			// cte_nombre
			donde = 0;
			donde = fn.recorreContenido(contenido, ConstantsValue.DATOS_ASEGURADO);
			if (donde > 0) {
				for (String dato : contenido.split("@@@")[donde + 1].split("\r\n")) {
					if (dato.contains(ConstantsValue.NOMBRE_HASH)) {
						modelo.setCteNombre(dato.split(ConstantsValue.NOMBRE_HASH)[1].replace("###", " ").trim());
					} else {
						modelo.setPoliza(dato.trim());
					}
				}
			}

			// cp
			// cte_direccion
			donde = 0;
			donde = fn.recorreContenido(contenido, ConstantsValue.DOMICILIO_HASH);
			if (donde > 0 && contenido.split("@@@")[donde].split("\r\n").length == 2
					&& contenido.split("@@@")[donde].split("\r\n")[0].contains(ConstantsValue.DOMICILIO_HASH)) {

				if (!contenido.split("@@@")[donde].split("\r\n")[0].split("###")[0].trim()
						.equals(ConstantsValue.DOMICILIO)) {
					newcontenido.append(contenido.split("@@@")[donde].split("\r\n")[0].split("###")[0].trim());
				} else {
					if (contenido.split("@@@")[donde].split("\r\n")[0].contains(ConstantsValue.VIGENCIA2)
							&& contenido.split("@@@")[donde].split("\r\n")[0].split("###")[0].trim()
									.equals(ConstantsValue.DOMICILIO)) {
						newcontenido.append(
								contenido.split("@@@")[donde].split("\r\n")[0].split(ConstantsValue.DOMICILIO)[1]
										.split(ConstantsValue.VIGENCIA2)[0].replace("###", " ").trim());
					}
				}
				if (contenido.split("@@@")[donde].split("\r\n")[1].contains("###Desde")) {
					newcontenido = new StringBuilder();
					newcontenido.append(
							contenido.split("@@@")[donde].split("\r\n")[1].split(ConstantsValue.DESDE)[0].trim());
					for (int i = 0; i < newcontenido.toString().split("###").length; i++) {
						if (!newcontenido.toString().split("###")[i].trim().equals("C.P.")) {
							if ((i + 1) < newcontenido.toString().split("###").length && i > 0 || i >= 1) {
								if (!newcontenido.toString().split("###")[i - 1].trim().equals("C.P.")) {
									newcontenido.append(" " + newcontenido.toString().split("###")[i].trim());
								} else {
									modelo.setCp(newcontenido.toString().split("###")[i].trim());
								}
							} else {
								newcontenido.append(" " + newcontenido.toString().split("###")[i].trim());
							}
						}
					}
				}
				modelo.setCteDireccion(newcontenido.toString());

			}

			// rfc
			// moneda
			donde = 0;
			donde = fn.recorreContenido(contenido, ConstantsValue.RFC_HASH);
			if (donde > 0 && contenido.split("@@@")[donde].split("\r\n").length == 2) {
				if (contenido.split("@@@")[donde].split("\r\n")[0].contains("R.F.C.:###")) {
					modelo.setRfc(contenido.split("@@@")[donde].split("\r\n")[0].split("###")[1].trim());
				}
				if (contenido.split("@@@")[donde].split("\r\n")[1].contains("###Moneda:###")
						&& contenido.split("@@@")[donde].split("\r\n")[1].split("###").length == 3) {
					modelo.setMoneda(fn.moneda(contenido.split("@@@")[donde].split("\r\n")[1].split("###")[2].trim()));
				}

			}

			// vigencia_a
			// vigencia_de
			donde = 0;
			donde = fn.recorreContenido(contenido, "Desde:");
			if (donde > 0) {
				if (contenido.split("@@@")[donde].split("\r\n").length == 2
						&& contenido.split("@@@")[donde].split("\r\n")[1].split(ConstantsValue.DESDE)[1].trim()
								.split("/").length == 3
						&& contenido.split("@@@")[donde].split("\r\n")[1].split(ConstantsValue.DESDE)[1]
								.split("###").length == 2) {
					modelo.setVigenciaDe(fn.formatDateMonthCadena(
							contenido.split("@@@")[donde].split("\r\n")[1].split(ConstantsValue.DESDE)[1]
									.split("###")[1].trim()));

				}
				if (contenido.split("@@@")[donde + 1].split("\r\n").length == 1
						&& contenido.split("@@@")[donde + 1].contains("Hasta:###")) {
					modelo.setVigenciaA(
							fn.formatDateMonthCadena(contenido.split("@@@")[donde + 1].split("###")[1].trim()));
				}
			}

			// descripcion
			donde = 0;
			donde = fn.recorreContenido(contenido, ConstantsValue.VEHICULO_HASH);
			if (donde > 0) {
				for (String dato : contenido.split("@@@")[donde].split("\r\n")) {
					if (dato.contains(ConstantsValue.VEHICULO_HASH)) {
						modelo.setDescripcion(dato.split(ConstantsValue.VEHICULO_HASH)[1].replace("###", " ").trim());
					}
				}
			}

			// motor
			// modelo
			// serie
			// endoso
			// placas
			donde = 0;
			donde = fn.recorreContenido(contenido, ConstantsValue.MOTOR);
			if (donde > 0) {
				for (String dato : contenido.split("@@@")[donde].split("\r\n")) {
					if (dato.contains(ConstantsValue.MOTOR) && dato.contains(ConstantsValue.MODELO)
							&& dato.split("###").length == 4) {
						modelo.setMotor(dato.split("###")[1].trim());
						modelo.setModelo(Integer.parseInt(dato.split("###")[3].trim()));
					}
					if (dato.contains("Serie:") && dato.split("###").length == 5) {
						modelo.setSerie(dato.split("###")[1].trim());
						modelo.setEndoso(dato.split("###")[4].trim());
					}
					if (dato.contains(ConstantsValue.PLACAS) && dato.split("###").length == 2
							|| dato.contains(ConstantsValue.PLACAS) && dato.split("###").length == 4) {
						modelo.setPlacas(dato.split("###")[1].trim());
					}
				}
			}

			// forma_pago
			modelo.setFormaPago(fn.formaPagoSring(recibosText));
			// agente
			// cve_agente

			donde = 0;
			donde = fn.recorreContenido(contenido, ConstantsValue.AGENTE_HASH);
			if (donde > 0) {
				if (contenido.split("@@@")[donde].split("\r\n").length == 2) {
					for (String dato : contenido.split("@@@")[donde].split("\r\n")) {
						if (dato.contains(ConstantsValue.AGENTE_HASH)) {
							modelo.setCveAgente(dato.split("###")[1].trim());
							newcontenido = new StringBuilder();
							newcontenido.append(dato.split("###")[2].trim());
						}
						if (dato.split("###").length == 1) {
							newcontenido.append(" ").append(dato.trim());
						}
					}
					modelo.setAgente(newcontenido.toString());
				} else if (contenido.split("@@@")[donde].split("\r\n").length == 1
						|| (contenido.split("@@@")[donde].contains(ConstantsValue.AGENTE)
								&& contenido.split("@@@")[donde].contains("No.")
								&& contenido.split("@@@")[donde].split("###").length == 6)) {

					modelo.setCveAgente(contenido.split("@@@")[donde].split("###")[1].trim());
					modelo.setAgente(contenido.split("@@@")[donde].split(modelo.getCveAgente())[1].split("No.")[0]
							.replace("###", " ").trim());

				}
			}

			// conductor
			donde = 0;
			donde = fn.recorreContenido(contenido, ConstantsValue.NOMBRE_EDAD_SEXO_HASH);
			if (donde > 0) {
				for (String dato : contenido.split("@@@")[donde].split("\r\n")) {
					if (!dato.contains(ConstantsValue.NOMBRE_EDAD_SEXO_HASH)) {
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
			donde = fn.recorreContenido(contenido, ConstantsValue.PRIMA_NETA2);
			if (donde > 0) {
				for (String dato : contenido.split("@@@")[donde].split("\r\n")) {
					if (dato.split("###").length == 2 && dato.contains(ConstantsValue.PRIMA_NETA2)) {

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
			fin = contenido.indexOf(ConstantsValue.DATOS_ASEGURADO);
			if (inicio > -1 && fin > inicio) {
				newcontenido = new StringBuilder();
				newcontenido.append(contenido.substring(inicio + 18, fin).replace("@@@", "").trim());
				if (newcontenido.toString().split("\r\n").length == 1) {
					donde = newcontenido.toString().split("/").length;
					if (donde == 3) {
						modelo.setPlan(newcontenido.toString().split("-")[donde - 2].trim());
					}
				}
			}

			// iva
			// prima_total
			donde = 0;
			donde = fn.recorreContenido(contenido, ConstantsValue.IVA);
			if (donde > 0) {
				for (String dato : contenido.split("@@@")[donde].split("\r\n")) {
					if (dato.split("###").length == 2 && dato.contains(ConstantsValue.IVA)) {
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
			if (modelo.getDescripcion().length() > 0) {
				newcontenido = new StringBuilder();
				newcontenido.append(modelo.getDescripcion());
				inicio = newcontenido.toString().trim().split(" ").length;
			}
			if (inicio > 1) {
				String auxStr = newcontenido.toString();
				newcontenido = new StringBuilder();
				newcontenido.append(auxStr.split(" ")[0]);
				modelo.setMarca(newcontenido.toString());
			}

	
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
			if (donde > 0 && contenido.split("@@@")[donde].split("\r\n").length == 2
					&& contenido.split("@@@")[donde].split("\r\n")[0].contains("Domicilio:###")) {

				if (!contenido.split("@@@")[donde].split("\r\n")[0].split("###")[0].trim()
						.equals(ConstantsValue.DOMICILIO)) {
					resultado.append(contenido.split("@@@")[donde].split("\r\n")[0].split("###")[0].trim());
				} else {
					if (contenido.split("@@@")[donde].split("\r\n")[0].contains("Vigencia")
							&& contenido.split("@@@")[donde].split("\r\n")[0].split("###")[0].trim()
									.equals("Domicilio:")) {
						resultado.append(contenido.split("@@@")[donde].split("\r\n")[0].split("Domicilio:")[1]
								.split("Vigencia")[0].replace("###", " ").trim());
					}
				}
				if (contenido.split("@@@")[donde].split("\r\n")[1].contains("###Desde")) {
					newcontenido = new StringBuilder();
					newcontenido.append(
							contenido.split("@@@")[donde].split("\r\n")[1].split(ConstantsValue.DESDE)[0].trim());
					for (int i = 0; i < newcontenido.toString().split("###").length; i++) {
						if (!newcontenido.toString().split("###")[i].trim().equals("C.P.")) {
							if ((i + 1) < newcontenido.toString().split("###").length && i > 0 || i >= 1) {
								if (!newcontenido.toString().split("###")[i - 1].trim().equals("C.P.")) {
									resultado.append(" ").append(newcontenido.toString().split("###")[i].trim());
								} else {
									modelo.setCp(newcontenido.toString().split("###")[i].trim());
								}
							} else {
								resultado.append(" ").append(newcontenido.toString().split("###")[i].trim());
							}
						}
					}
				}
				modelo.setCteDireccion(resultado.toString().replace("###", " "));

			}

			// rfc
			// moneda
			donde = 0;
			donde = fn.recorreContenido(contenido, "R.F.C:###");
			if (donde > 0 && contenido.split("@@@")[donde].split("\r\n").length == 2) {

				if (contenido.split("@@@")[donde].split("\r\n")[0].contains("R.F.C.:###")) {
					modelo.setRfc(contenido.split("@@@")[donde].split("\r\n")[0].split("###")[1].trim());
				}
				if (contenido.split("@@@")[donde].split("\r\n")[1].contains("###Moneda:###")
						&& contenido.split("@@@")[donde].split("\r\n")[1].split("###").length == 3) {
					modelo.setMoneda(fn.moneda(contenido.split("@@@")[donde].split("\r\n")[1].split("###")[2].trim()));
				}

			}

			if(modelo.getVigenciaDe().length() == 0  || modelo.getVigenciaA().length() == 0) {

				   inicio = contenido.indexOf("Domicilio");
				   fin = contenido.indexOf("Datos del vehículo");
				   if(inicio > -1 &&  fin > -1 && inicio < fin) {
					 String x = contenido.substring(inicio ,fin).replace("@@@", "").replace("\r", "");
					 	for (int i = 0; i < x.split("\n").length; i++) {
					        if(x.split("\n")[i].contains("Desde:")) {
					        	modelo.setVigenciaDe(fn.formatDateMonthCadena(x.split("\n")[i].split("Desde:")[1].replace("###", "")));
					        }
					if(x.split("\n")[i].contains("Hasta:")) {
						modelo.setVigenciaA(fn.formatDateMonthCadena(x.split("\n")[i].split("Hasta:")[1].replace("###", "")));				        
						       }
						  }
					}
				}


			// descripcion
			donde = 0;
			donde = fn.recorreContenido(contenido, ConstantsValue.VEHICULO_HASH);
			if (donde > 0) {
				for (String dato : contenido.split("@@@")[donde].split("\r\n")) {
					if (dato.contains("Vehículo:###")) {
						modelo.setDescripcion(dato.split(ConstantsValue.VEHICULO_HASH)[1].replace("###", " ").trim());
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
					if ((dato.contains("Placas:") && dato.split("###").length == 2)
							|| (dato.contains("Placas:") && dato.split("###").length == 4)) {
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
			donde = fn.recorreContenido(contenido, ConstantsValue.AGENTE_HASH);
			if (donde > 0) {
				if (contenido.split("@@@")[donde].split("\r\n").length == 2) {
					for (String dato : contenido.split("@@@")[donde].split("\r\n")) {
						if ((dato.contains(ConstantsValue.AGENTE_HASH) && dato.contains("OT:")
								&& dato.split("###").length == 6)
								|| (dato.contains("Agente:###") && dato.contains("No.")
										&& dato.split("###").length == 4)) {
							modelo.setCveAgente(dato.split("###")[1].trim());
							newcontenido = new StringBuilder();
							newcontenido.append(dato.split("###")[2].trim());
						}
						if (dato.split("###").length == 1) {
							newcontenido.append(" ").append(dato.trim());
						}
					}
					modelo.setAgente(newcontenido.toString());
				} else if (contenido.split("@@@")[donde].split("\r\n").length == 1
						|| (contenido.split("@@@")[donde].contains("Agente:###")
								&& contenido.split("@@@")[donde].contains("No.")
								&& contenido.split("@@@")[donde].split("###").length == 6)) {
					modelo.setCveAgente(contenido.split("@@@")[donde].split("###")[1].trim());
					modelo.setAgente(contenido.split("@@@")[donde].split(modelo.getCveAgente())[1].split("No.")[0]
							.replace("###", " ").trim());

				}
			}

			// conductor
			donde = 0;
			donde = fn.recorreContenido(contenido, "Nombre:###Edad:###Sexo:");
			if (donde > 0) {
				for (String dato : contenido.split("@@@")[donde].split("\r\n")) {
					if (!dato.contains("Nombre:###Edad:###Sexo:")) {
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
				newcontenido = new StringBuilder();
				newcontenido.append(contenido.substring(inicio + 18, fin).replace("@@@", "").trim());
				if (newcontenido.toString().split("\r\n").length == 1) {
					inicio = newcontenido.toString().split("-").length;
					if (inicio == 3) {
						modelo.setPlan(newcontenido.toString().split("-")[inicio - 2].trim());
					}
				}
			}

			// iva
			// prima_total
			donde = 0;
			donde = fn.recorreContenido(contenido, ConstantsValue.IVA);
			if (donde > 0) {
				for (String dato : contenido.split("@@@")[donde].split("\r\n")) {
					if (dato.split("###").length == 2 && dato.contains("comisión")) {
						modelo.setCargoExtra(fn.castBigDecimal(fn.cleanString(dato.split("###")[1].trim())));
					}
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
			if (modelo.getDescripcion().length() > 0) {
				newcontenido = new StringBuilder();
				newcontenido.append(modelo.getDescripcion());
				inicio = newcontenido.toString().trim().split(" ").length;
			}
			if (inicio > 1) {
				String auxStr = newcontenido.toString();
				newcontenido = new StringBuilder();
				newcontenido.append(auxStr.split(" ")[0]);
				modelo.setMarca(newcontenido.toString());
			}

			// fecha_emision
			inicio = contenido.indexOf("Emisión:");
			if (inicio > -1) {
				newcontenido = new StringBuilder();
				newcontenido.append(fn.gatos(contenido.substring(inicio + 8, inicio + 100).split("\n")[0].trim()));
				modelo.setFechaEmision(fn.formatDateMonthCadena(newcontenido.toString()));
			}

			// id_cliente
			inicio = contenido.indexOf("No. de cliente");
			fin = contenido.indexOf("Conductores");
			if (fin == -1) {
				fin = contenido.indexOf("Coberturas");
			}
			if (inicio > -1 && fin > inicio) {
				newcontenido = new StringBuilder();
				newcontenido.append(contenido.substring(inicio + 14, fin).replace("@@@", "").trim());
				if (newcontenido.toString().split("\n").length == 2) {
					if (newcontenido.toString().split("\n")[1].trim().split(" ").length == 1) {
						modelo.setIdCliente(newcontenido.toString().split("\n")[1].trim());
					}
				} else if (newcontenido.toString().split(" ").length == 1) {
					modelo.setIdCliente(newcontenido.toString());
				}
			}

			if(modelo.getMoneda() == 0) {
				modelo.setMoneda(1);
			}
			List<EstructuraCoberturasModel> coberturas = new ArrayList<>();
			inicio = 0;
			fin = 0;
			inicio = contenido.indexOf("Coberturas amparadas###Suma asegurada###Deducible###Prima");
			fin = contenido.indexOf("Prima neta###");
			if(fin == -1)fin = contenido.indexOf("Félix Cuevas 366");
	
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

		

			return modelo;
		} catch (

		Exception ex) {
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
			StringBuilder newcontenido = new StringBuilder();
			StringBuilder resultado = new StringBuilder();

			for (String a : recibosText.split("RECIBO PROVISIONAL DE PAGO")) {
				EstructuraRecibosModel recibo = new EstructuraRecibosModel();
				if (index > 0) {
					// serie
					recibo.setSerie(recibosSerie + "/" + totalRec);

					// recibo_id

					inicio = a.indexOf("Identificador:");
					fin = a.indexOf("Gastos por");
					if (inicio > -1 && fin > inicio) {
						newcontenido.append(fn.gatos(a.substring(inicio + 14, fin)).trim());
						recibo.setReciboId(newcontenido.toString());
					}

					// vigencia_de

					// vigencia_a

					inicio = a.indexOf("Periodo cubierto:");
					if (inicio > -1) {
						newcontenido = new StringBuilder();
						newcontenido.append(a.substring(inicio + 17, inicio + 200));
						if (newcontenido.toString().contains("Concepto")) {
							String auxStr = newcontenido.toString();
							newcontenido = new StringBuilder();
							newcontenido.append(fn.gatos(auxStr.split("Concepto")[0].trim()).replace("\r\n", "")
									.replace("al@@@", "").replace("Del###", "").replace("al###", "")
									.replace(" de ", "/").replace("@@@", "").trim());

							if (newcontenido.toString().split("###").length == 2) {
								resultado.append(newcontenido.toString().split("###")[0].trim());
								if (resultado.toString().split("/").length == 3)
									recibo.setVigenciaDe(fn.formatDateMonthCadena(resultado.toString().replace("/", "-")));
								resultado = new StringBuilder();
								resultado.append(newcontenido.toString().split("###")[1].trim());
								if (resultado.toString().split("/").length == 3 && resultado.length() > 10)
									recibo.setVigenciaA(fn.formatDateMonthCadena(resultado.toString().replace("/", "-")));

							}

						}
					}

					// prima_total

					inicio = a.indexOf("Total a Pagar");
					if (inicio > -1) {
						newcontenido = new StringBuilder();
						newcontenido.append(
								fn.cleanString(fn.gatos(a.substring(inicio + 13, inicio + 60).split("\n")[0].trim())));
						if (fn.isNumeric(newcontenido.toString())) {
							recibo.setPrimaTotal(fn.castBigDecimal(fn.preparaPrimas(newcontenido.toString())));
							restoPrimaTotal = modelo.getPrimaTotal().subtract(recibo.getPrimaTotal());

						}
					}

					// derecho

					inicio = a.indexOf("por Expedición");
					if (inicio > -1) {
						newcontenido = new StringBuilder();
						newcontenido.append(fn.gatos(a.substring(inicio + 14, inicio + 100).split("\n")[0].trim()));
					}
					newcontenido.append(fn.cleanString(newcontenido.toString()));
					if (fn.isNumeric(newcontenido.toString()))

						recibo.setDerecho(fn.castBigDecimal(fn.preparaPrimas(newcontenido.toString())));
					restoDerecho = modelo.getDerecho().subtract(recibo.getDerecho());

					// iva

					inicio = a.indexOf("I.V.A.");
					if (inicio > -1) {
						newcontenido = new StringBuilder();
						newcontenido.append(
								fn.cleanString(fn.gatos(a.substring(inicio + 6, inicio + 100).split("\n")[0].trim())));

						if (newcontenido.toString().split("###").length == 2) {
							String auxStr = newcontenido.toString();
							newcontenido = new StringBuilder();
							newcontenido.append(auxStr.split("###")[1].trim());
							if (fn.isNumeric(newcontenido.toString())) {
								recibo.setIva(fn.castBigDecimal(fn.preparaPrimas(newcontenido.toString())));
								restoIva = modelo.getIva().subtract(recibo.getIva());
							}
						}
					}

					// recargo

					inicio = a.indexOf("de Financiamiento");
					if (inicio > -1) {
						newcontenido = new StringBuilder();
						newcontenido.append(fn.gatos(a.substring(inicio + 17, inicio + 100).split("\n")[0].trim()));
					}
					String auxStr = newcontenido.toString();
					newcontenido = new StringBuilder();
					newcontenido.append(fn.cleanString(auxStr));
					if (fn.isNumeric(newcontenido.toString())) {
						recibo.setRecargo(fn.castBigDecimal(fn.preparaPrimas(newcontenido.toString())));

					}
					restoRecargo = modelo.getRecargo().subtract(recibo.getRecargo());
					// prima_neta
					inicio = a.indexOf("Prima Neta");
					if (inicio > -1) {
						newcontenido = new StringBuilder();
						newcontenido.append(fn.gatos(a.substring(inicio + 10, inicio + 100).split("\n")[0].trim()));
					}
					auxStr = newcontenido.toString();
					newcontenido = new StringBuilder();
					newcontenido.append(fn.cleanString(auxStr));
					if (fn.isNumeric(newcontenido.toString())) {
						recibo.setPrimaneta(fn.castBigDecimal(fn.preparaPrimas(newcontenido.toString())));
					}
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
