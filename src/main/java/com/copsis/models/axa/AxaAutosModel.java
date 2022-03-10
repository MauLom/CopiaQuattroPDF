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

		contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales()).replace("R.F.C:", "R.F.C.:")
				.replace("DATOS DEL ASEGURADO", ConstantsValue.DATOS_ASEGURADO)
				.replace("Nombre: ", ConstantsValue.NOMBRE_HASH)
				.replace("PRIMA NETA",ConstantsValue.PRIMA_NETA2)
				.replace("Financiamiento","financiamiento")
				.replace("Expedición", "expedición")
				.replace("PRECIO TOTAL", "Precio Total")
				.replace("CONDUCTORES", "Conductores")
				.replace("No. de Cliente", "No. de cliente")
				.replace("DATOS DEL VEHÍCULO", "Datos del vehículo")
				.replace("DATOS ADICIONALES", ConstantsValue.DATOS_ADICIONALES)
				.replace("COBERTURAS AMPARADAS", "Coberturas amparadas")
				.replace("COBERTURAS", "Coberturas");
		
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
						if(!dato.split("###")[4].trim().contains("Anterior")) {
							modelo.setEndoso(dato.split("###")[4].trim());
						}
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
			
			// poliza
			// cte_nombre
			donde = 0;
			donde = fn.recorreContenido(contenido, ConstantsValue.DATOS_ASEGURADO);
			if (donde > 0 && (modelo.getCteNombre().length() == 0 || modelo.getPoliza().length() == 0)) {
				for (String dato : contenido.split("@@@")[donde].split("\n")) {
					if (dato.contains("Nombre:###")) {
						modelo.setCteNombre(dato.split("Nombre:###")[1].replace("###", " ").trim());
					} else if (!dato.contains("Vigencia") && dato.contains(ConstantsValue.DOMICILIO2) && modelo.getPoliza().split(" ").length > 1) {
						String[] valores = dato.split("###");
						modelo.setPoliza(valores[valores.length -1].trim());
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
					        	modelo.setVigenciaDe(fn.formatDateMonthCadena(x.split("\n")[i].split("Desde:")[1].replace("###", "").trim()));
					        }
					if(x.split("\n")[i].contains("Hasta:")) {
						modelo.setVigenciaA(fn.formatDateMonthCadena(x.split("\n")[i].split("Hasta:")[1].replace("###", "").trim()));				        
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
						if(!dato.split("###")[4].trim().contains("Anterior")) {
							modelo.setEndoso(dato.split("###")[4].trim());
						}
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

			//Direccion, CP
			if(modelo.getCteDireccion().length() == 0) {
				inicio = contenido.indexOf(ConstantsValue.DOMICILIO);
				fin = contenido.indexOf("R.F.C");
				
				if(inicio > -1 && inicio < fin) {
					StringBuilder domicilio = new StringBuilder();
					String texto = contenido.substring(inicio + 10,fin).replace("\r", "");
					for(String textoRenglon: texto.split("\n")) {
						textoRenglon = fn.gatos(textoRenglon.trim());
						domicilio.append(" ").append(textoRenglon.split("###")[0]);
					}
					
					if(domicilio.length() > 0) {
						if(domicilio.toString().contains("C.P")) {
							String cp = fn.numTx(domicilio.toString().split("C.P")[1].trim());
							if(fn.isNumeric(cp)) {
								modelo.setCp(cp);
							}
						}
						modelo.setCteDireccion(fn.eliminaSpacios(domicilio.toString().trim()));
					}
				}
				
			}
			
			//RFC
			if(modelo.getRfc().length() == 0 && contenido.contains("R.F.C")) {
				String texto = fn.gatos(contenido.split("R.F.C.:")[1].split("\n")[0].trim());
				if(texto.split("###")[0].trim().length() == 12 || texto.split("###")[0].trim().length() == 13) {
					modelo.setRfc(texto.split("###")[0].trim());
				}
				
			}
			
			//motor,modelo, placas,fecha emisión
			inicio = contenido.indexOf("Datos del vehículo");
			fin = contenido.indexOf(ConstantsValue.DATOS_ADICIONALES);

			if(inicio > - 1 && inicio < fin) {
				newcontenido = new StringBuilder();
				newcontenido.append(
						contenido.substring(inicio + 18, fin).replace("\r", "").replace("Motor: ###", "Motor:###").replace("Modelo: ###", "Modelo:###"));
				String[] arrContenido = newcontenido.toString().split("\n");
				for (int i = 0; i < arrContenido.length; i++) {
					if (arrContenido[i].contains("Vehículo:") && modelo.getDescripcion().length() == 0) {
						modelo.setDescripcion(
								fn.gatos(arrContenido[i].split("Vehículo:")[1].trim()).split("###")[0].trim());
					}
					if (arrContenido[i].contains("Motor:###") && modelo.getMotor().length() == 0) {
						modelo.setMotor(arrContenido[i].split("Motor:###")[1].split("###")[0].trim());
					}
					if (arrContenido[i].contains("Modelo:###") && modelo.getModelo() == 0) {
						modelo.setModelo(fn.castInteger(arrContenido[i].split("Modelo:###")[1].split("###")[0].trim()));
					}
					if (arrContenido[i].contains(ConstantsValue.PLACAS)) {
						modelo.setPlacas(
								fn.gatos(arrContenido[i].split(ConstantsValue.PLACAS)[1].trim()).split("###")[0]
										.trim());
					}
					if (modelo.getFechaEmision().length() == 0 && arrContenido[i].contains("Emisión")) {
						String fecha = "";
						if ((i + 1) < arrContenido.length || (i + 2) < arrContenido.length) {
							if (arrContenido[i + 1].split("-").length == 3) {
								fecha = arrContenido[i + 1].split("###")[arrContenido[i + 1].split("###").length - 1]
										.trim();
								modelo.setFechaEmision(fn.formatDateMonthCadena(fecha));
							} else if (arrContenido[i + 2].split("-").length == 3) {
								fecha = arrContenido[i + 2].split("###")[arrContenido[i + 2].split("###").length - 1]
										.trim();
								modelo.setFechaEmision(fn.formatDateMonthCadena(fecha));
							}
						}
					}

				}
			}
			
			//cveAgente, agente
			if(modelo.getCveAgente().length() == 0  && modelo.getAgente().length() == 0  && contenido.split("Agente:").length > 1) {
				String textoAgente = fn.gatos(contenido.split("Agente:")[1].split("\n")[0].trim());
				//valor en el mismo renglón
				textoAgente = textoAgente.split("###")[0].trim();
				if(textoAgente.split(" ").length > 1) {
					String cveAgente = textoAgente.split(" ")[0];
					if(cveAgente.contains("INTERPROTECCION")) {
						cveAgente = cveAgente.split("INTERPROTECCION")[0];
					}
					
					if(cveAgente.length() > 0) {
						modelo.setCveAgente(cveAgente);
						modelo.setAgente(textoAgente.split(cveAgente)[1].trim());
					}
				}
			}
			//endoso
			if(contenido.split("Endoso:").length > 1 && modelo.getEndoso().length() == 0) {
				String texto =  contenido.split("Endoso:")[1];
				String[] textoRenglones = texto.split("\n");
				String textoOtroRenglon = textoRenglones[1];
				
				if(textoOtroRenglon.split("###").length > 1 || (textoOtroRenglon.split("###").length == 1 && !textoOtroRenglon.contains("Datos adicionales"))) {
					modelo.setEndoso(textoOtroRenglon.split("###")[textoOtroRenglon.split("###").length -1].trim());
				}else  if(textoRenglones.length > 1){
					textoOtroRenglon = textoRenglones[2];
					if(textoOtroRenglon.contains("Agente") &&  textoOtroRenglon.split("###").length>2) {
						modelo.setEndoso(textoOtroRenglon.split("###")[textoOtroRenglon.split("###").length -1].trim());
					}
				}
			}
			
			//moneda
			contenido = contenido.replace("\r", "");
			if(contenido.split("Moneda:").length>1 && (modelo.getMoneda() == 0 || modelo.getMoneda() == 5)) {
				String textoMoneda =  contenido.split("Moneda:")[1];
				if(textoMoneda.contains("Conductores")) {
					textoMoneda = textoMoneda.split("Conductores")[0].replace("@@@", "");
				}
				String[] textoRenglones = textoMoneda.split("\n");
				String textoRenglon = fn.gatos(textoRenglones[0].trim());
				
				textoRenglon = textoRenglon.split("###")[0].trim();
				int moneda = fn.moneda(textoRenglon);
				if(moneda != 5 ){
					modelo.setMoneda(fn.moneda(textoRenglon));
				}else if(textoRenglones.length > 1) {
					textoRenglon = fn.gatos(textoRenglones[1].trim());
					moneda =  fn.moneda(textoRenglon);
					modelo.setMoneda(moneda != 5 ? moneda : 0);
				}
			}
			
			//ID cliente
			if(contenido.contains("No. de cliente") && modelo.getIdCliente().length() == 0) {
				String textoNoCliente =  contenido.split("No. de cliente")[1];
				if(textoNoCliente.contains("Conductores")) {
					textoNoCliente = textoNoCliente.split("Conductores")[0].replace("@@@", "").replace("Forma de Pago", "Forma de pago");
				}
				String[] textoRenglones = textoNoCliente.split("\n");
				if(textoRenglones.length >1) {
					String textoRenglon = fn.gatos(textoRenglones[2].trim());
					if(textoRenglon.contains("Forma de pago")) {
						modelo.setIdCliente(textoRenglon.split("###")[textoRenglon.split("###").length -1]);
					}
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

		
			if(modelo.getCoberturas().isEmpty()) {
				int inicioIndex = contenido.indexOf("Coberturas amparadas");
				int finIndex = contenido.indexOf("Prima neta");
				
				if(inicioIndex > -1 && inicioIndex < finIndex) {
					newcontenido = new StringBuilder();
					newcontenido.append(contenido.substring(inicioIndex,finIndex).replace("@@@", "").replace("\r", "").replace("PRIMA","Prima").replace("DEDUCIBLE", "Deducible"));					
					String[] arrContenido = newcontenido.toString().split("\n");
					boolean tieneTituloPrima = newcontenido.toString().contains("Prima");
					int numValores;
					String deducible = "";
					
					for(int i=0; i<arrContenido.length;i++) {
						arrContenido[i] = fn.gatos(arrContenido[i].trim());
						numValores = arrContenido[i].split("###").length;

						if(!arrContenido[i].contains("Coberturas amparadas") && numValores > 1) {
							EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();

							arrContenido[i] = fn.gatos(arrContenido[i].trim());
							cobertura.setNombre(arrContenido[i].split("###")[0].trim());
							if (numValores == 2) {
								cobertura.setSa(arrContenido[i].split("###")[1].trim());
								coberturas.add(cobertura);
							}else if (numValores == 3 ||numValores == 4) {
								cobertura.setSa(arrContenido[i].split("###")[1].trim());
                                deducible = arrContenido[i].split("###")[2].trim();
                                //se verifica que el valor no sea prima neta
                                if(arrContenido[i].split("###").length == 3 && tieneTituloPrima && !deducible.contains("%")) {
                                  deducible = "";
                                }
								cobertura.setDeducible(deducible);
								coberturas.add(cobertura);
							}
						}
					}
				}

				
			}

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
