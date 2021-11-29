package com.copsis.models.gnp.autos;

import java.util.ArrayList;
import java.util.List;

import com.copsis.constants.ConstantsValue;
import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraCoberturasModel;
import com.copsis.models.EstructuraJsonModel;
import com.copsis.models.EstructuraRecibosModel;

public class GnpAutos2Model {
	// Clases
	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
	// Varaibles
	private String contenido = "";

	public GnpAutos2Model(String contenido) {
		this.contenido = contenido;
	}

	public EstructuraJsonModel procesar() {
		StringBuilder newcontenido1 = new StringBuilder();
		StringBuilder newcontenido = new StringBuilder();
		int inicio = 0;
		int fin = 0;
		int donde = 0;

		contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());

		try {

			// tipo
			modelo.setTipo(1);
			// cia
			modelo.setCia(18);

			// poliza
			donde = 0;
			donde = fn.recorreContenido(contenido, ConstantsValue.POLIZA_NO);
			if (donde > 0) {
				for (String dato : contenido.split("@@@")[donde].split("\r\n")) {
					if (dato.contains(ConstantsValue.POLIZA_NO) && dato.split("###").length == 4
							|| dato.split("###").length == 5) {
						if (dato.split("###")[2].trim().equals(ConstantsValue.POLIZA_NO)) {
							modelo.setPoliza(dato.split("###")[3].trim());
						}
						if (dato.split("###")[3].trim().equals(ConstantsValue.POLIZA_NO)) {
							modelo.setPoliza(dato.split("###")[4].trim());
						}
					}
				}
			}

			// renovacion
			// cte_nombre
			donde = 0;
			donde = fn.searchTwoTexts(contenido, ConstantsValue.RENOVACION, "Versión");
			if (donde > 0) {
				for (String dato : contenido.split("@@@")[donde].split("\r\n")) {
					if (dato.contains(ConstantsValue.RENOVACION) && dato.split("###").length == 3
							&& dato.split("###")[2].trim().contains(ConstantsValue.RENOVACION)
							&& dato.split("###")[2].trim().length() > 10) {
						modelo.setRenovacion(dato.split("###")[2].trim().split(ConstantsValue.RENOVACION)[1].trim());
					}
					if (dato.contains(ConstantsValue.CONTRATO)) {
						switch (dato.trim().split("###").length) {
						case 1:
							modelo.setCteNombre(dato.trim().split("Contrato")[0].trim());
							break;
						case 2:
							if (dato.split("###")[1].contains("Contrato")) {
								modelo.setCteNombre(dato.split("###")[0].trim());
							}
							break;
						default:
							break;
						}
					}
				}
			}

			// id_cliente
			inicio = contenido.indexOf("Código Cliente");
			if (inicio > -1) {
				newcontenido.append(contenido.substring(inicio + 14, inicio + 150).trim().split("\r\n")[0]);
				if (newcontenido.toString().contains("Duraci")) {
					modelo.setIdCliente(fn.gatos(newcontenido.toString().split("Duraci")[0]).trim());
				}
			}

			// cp
			// cte_direccion
			// rfc
			// vigencia_de
			// vigencia_a
			donde = 0;
			donde = fn.recorreContenido(contenido, "CP");
			if (donde > 0) {
				newcontenido = new StringBuilder();
				for (String dato : contenido.split("@@@")[donde].split("\r\n")) {
					if (dato.contains("Día") && dato.split("###").length == 4
							&& dato.split("###")[1].trim().equals("Día")) {
						newcontenido.append(dato.split("###")[0].trim());
					}
					if (dato.contains("CP") && dato.split("###").length == 5 && dato.split("###")[0].contains("CP")) {
						newcontenido.append(" ").append(dato.split("###")[0].split("CP")[0].trim());
						modelo.setCp(fn.cleanString(dato.split("###")[0].split("CP")[1].trim()));
						if (dato.split("###")[1].contains("hrs.")) {
							modelo.setVigenciaDe(dato.split("###")[4].trim() + "-" + dato.split("###")[3].trim() + "-"
									+ dato.split("###")[2].trim());
						}

					}
					if (dato.contains("R.F.C:") && dato.split("###").length == 5
							&& dato.split("###")[0].contains("R.F.C:") && dato.split("###")[1].contains("Hasta")) {
						modelo.setRfc(dato.split("###")[0].split(":")[1].trim());
						if (dato.split("###")[1].contains("hrs.")) {
							modelo.setVigenciaA(dato.split("###")[4].trim() + "-" + dato.split("###")[3].trim() + "-"
									+ dato.split("###")[2].trim());
						}
					}
				}
				modelo.setCteDireccion(newcontenido.toString());
			}

			// prima_neta
			// forma_pago
			// moneda
			// recargo
			// derecho
			// iva
			// prima_total
			donde = 0;
			donde = fn.recorreContenido(contenido, ConstantsValue.PRIMA_NETA);
			if (donde > 0) {
				for (String dato : contenido.split("@@@")[donde].split("\r\n")) {
					if (dato.contains(ConstantsValue.PRIMA_NETA) && dato.split("###").length == 5
							&& dato.split("###")[3].trim().equals(ConstantsValue.PRIMA_NETA)) {
						modelo.setPrimaneta(fn.castBigDecimal(fn.preparaPrimas(dato.split("###")[4].trim())));
					} else if (dato.contains("Forma de pago")) {
						if (dato.trim().split("###").length == 3
								&& dato.trim().split("###")[0].contains("Forma de pago")) {
							modelo.setFormaPago(fn.formaPago(dato.trim().split("###")[1].trim()));
						}
					} else if (dato.contains("Moneda")) {
						if (dato.split("###").length == 5 && dato.split("###")[1].trim().equals("Moneda")
								&& dato.split("###")[3].trim().equals("Fraccionado")) {
							modelo.setMoneda(fn.moneda(dato.split("###")[2].trim()));
							modelo.setRecargo(fn.castBigDecimal(fn.preparaPrimas(dato.split("###")[4].trim())));

						}
					} else if (dato.contains("de Póliza")) {
						if (dato.split("###").length == 5 && dato.split("###")[3].contains("de Póliza")) {
							modelo.setDerecho(fn.castBigDecimal(fn.preparaPrimas(dato.split("###")[4].trim())));
						}

					} else if (dato.contains(ConstantsValue.IVA)) {
						if (dato.split("###").length == 5 && dato.split("###")[4].contains(ConstantsValue.IVA)) {
							modelo.setIva(fn
									.castBigDecimal(fn.preparaPrimas(dato.split("###")[4].split(ConstantsValue.IVA)[1].trim())));
						}
					} else if (dato.contains("a Pagar") && dato.split("###").length == 2
							&& dato.split("###")[0].contains("a Pagar")) {
						modelo.setPrimaTotal(fn.castBigDecimal(fn.preparaPrimas(dato.split("###")[1].trim())));
					}
				}
			}

			// descripcion (vehiculo)
			// modelo
			// serie
			// motor
			// placas
			donde = 0;
			donde = fn.recorreContenido(contenido, "Marca:");
			if (donde > 0) {
				for (String dato : contenido.split("@@@")[donde].split("\r\n")) {
					if (dato.contains("Marca:")) {
						if (dato.split("###").length == 3) {
							modelo.setDescripcion(dato.split("###")[1].trim());
						}
					} else if (dato.contains("Motor:")) {
						if (dato.split("###").length == 5 && dato.split("###")[0].trim().equals("Motor:")
								&& dato.split("###")[2].trim().equals("Uso:")) {
							modelo.setMotor(dato.split("###")[1].trim());
						}
					} else if (dato.contains("Serie:")) {
						if (dato.trim().split("###").length == 3 && dato.split("###")[0].trim().equals("Serie:")
								&& dato.split("###")[2].trim().equals("RFV:")) {
							modelo.setSerie(dato.trim().split("###")[1].trim());
						}
					} else if (dato.contains("Modelo:")) {
						switch (dato.trim().split("###").length) {
						case 3:
						case 4:
							if (dato.trim().split("###")[0].trim().equals("Modelo:")
									&& dato.trim().split("###")[2].contains("Tipo")) {
								modelo.setModelo(fn.castInteger(dato.trim().split("###")[1].trim()));
							}
							break;
						default:
							break;
						}
					} else if (dato.contains("Placas:") && dato.trim().split("###").length == 2) {
						modelo.setPlacas(dato.trim().split("###")[1].trim());
					}
				}
			}

			// plan
			donde = 0;
			donde = fn.recorreContenido(contenido, "Paquete");
			if (donde > 0) {
				for (String dato : contenido.split("@@@")[donde].split("\r\n")) {
					if (dato.contains("Paquete") && dato.trim().split("###").length == 3
							&& dato.split("###")[0].contains("bertura") && dato.split("###")[2].contains("Vigencia")) {
						modelo.setPlan(dato.split("###")[1].trim());
					}
				}
			}

			// coberturas{nombre, sa, deducible}
			inicio = 0;
			fin = 0;
			inicio = contenido.indexOf("Coberturas Amparadas###Suma Asegurada###Deducible###");
			fin = contenido.indexOf("Aplicados");
			if (inicio > 0 && fin > 0) {
				newcontenido = new StringBuilder();
				for (String dato : contenido.substring(inicio + 52, fin).trim().split("\n")) {
					if (!dato.contains("Día###Mes###Año") && !dato.contains("Descuentos")
							&& dato.trim().split("###").length > 1 && !dato.split("###")[0].trim().equals("Actual")) {

						newcontenido.append(dato.trim()).append("\r\n");

					}
				}
				if (newcontenido.length() > 0) {
					String[] puedenSer = { "Desde", "Hasta", "Duración###", "Importe", "Anterior", "Movimiento" }; // Partidores

					for (int i = 0; i < newcontenido.toString().split("\r\n").length; i++) {

						boolean paso = false;
						for (String dato : puedenSer) {
							if (newcontenido.toString().split("\r\n")[i].contains(dato)) {
								paso = true;

								newcontenido1.append(newcontenido.toString().split("\r\n")[i].trim().split(dato)[0].trim()).append("\r\n");

							}
						}
						if (!paso) {
							newcontenido1.append(newcontenido.toString().split("\r\n")[i].trim()).append("\r\n");
						}
					}

					if (newcontenido1.length() > 0) {
						List<EstructuraCoberturasModel> coberturas = new ArrayList<>();
						for (int i = 0; i < newcontenido1.toString().split("\r\n").length; i++) {

							if (newcontenido1.toString().split("\r\n")[i].trim().split("###").length == 2) {
								EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();
								cobertura.setNombre(
										newcontenido1.toString().split("\r\n")[i].split("###")[0].replace("@@@", "").trim());

								if (newcontenido1.toString().split("\r\n")[i].split("###")[1].trim().contains("%")) {

									if (newcontenido1.toString().split("\r\n")[i].split("###")[1].trim().split(" ").length == 3) {

										cobertura.setSa(
												newcontenido1.toString().split("\r\n")[i].split("###")[1].trim().split(" ")[0]
														.trim());
										cobertura.setDeducible(newcontenido1.toString().split("\r\n")[i].split("###")[1].trim()
												.split(cobertura.getSa())[1].trim());

									} else if (newcontenido1.toString().split("\r\n")[i].split("###")[1].trim()
											.split(" ").length == 4
											&& newcontenido1.toString().split("\r\n")[i].split("###")[1]
													.contains("Valor Comercial")) {

										cobertura.setSa("Valor Comercial");
										cobertura.setDeducible(newcontenido1.toString().split("\r\n")[i].split("###")[1].trim()
												.split("Comercial")[1].trim());

									}

								} else if (newcontenido1.toString().split("\r\n")[i].split("###")[1].trim()
										.contains("No aplica")) {
									cobertura.setSa(
											newcontenido1.toString().split("\r\n")[i].split("###")[1].split("No")[0].trim());
									cobertura.setDeducible("No aplica");
								} else {
									cobertura.setSa(newcontenido1.toString().split("\r\n")[i].split("###")[1].trim());
								}
								coberturas.add(cobertura);
							}
						}
						modelo.setCoberturas(coberturas);
					}
				}
			}

			// agente
			inicio = contenido.indexOf("Agente");
			if (inicio > -1) {
				String auxStr = "";
				newcontenido =  new StringBuilder();
				newcontenido.append(contenido.substring(inicio + 6, inicio + 150).split("\r\n")[0]);
				if (newcontenido.toString().contains(ConstantsValue.CLAVE2)) {
					auxStr = newcontenido.toString();
					newcontenido =  new StringBuilder();
					newcontenido.append(auxStr.split(ConstantsValue.CLAVE2)[0]);
				}
				auxStr = newcontenido.toString();
				newcontenido.append(fn.gatos(auxStr));
				if (newcontenido.toString().split("###").length == 1) {
					modelo.setAgente(newcontenido.toString());
				}
			}

			// cve_agente
			inicio = contenido.indexOf("Agente");
			if (inicio > -1) {
				String auxStr = "";
				newcontenido = new StringBuilder();
				newcontenido.append(contenido.substring(inicio, inicio + 150).split("\r\n")[0]);
				if (newcontenido.toString().contains(ConstantsValue.CLAVE2)) {
					auxStr = newcontenido.toString();
					newcontenido = new StringBuilder();
					newcontenido.append(auxStr.split(ConstantsValue.CLAVE2)[1]);
				}
				auxStr = newcontenido.toString();
				newcontenido = new StringBuilder();
				newcontenido.append(fn.gatos(auxStr));
				if (newcontenido.toString().split("###").length == 2) {

					modelo.setCveAgente(newcontenido.toString().split("###")[0].trim());
				}

			}

			modelo.setFechaEmision(modelo.getVigenciaA());
			// inciso
			modelo.setInciso(1);

			List<EstructuraRecibosModel> recibos = new ArrayList<>();
			EstructuraRecibosModel recibo = new EstructuraRecibosModel();

			if (modelo.getFormaPago() == 1) {
				recibo.setReciboId("");
				recibo.setSerie("1/1");
				recibo.setVigenciaDe(modelo.getVigenciaDe());
				recibo.setVigenciaA(modelo.getVigenciaA());
				if (recibo.getVigenciaDe().length() > 0) {
					recibo.setVencimiento(fn.dateAdd(recibo.getVigenciaDe(), 30, 1));
				}
				recibo.setPrimaneta(fn.castBigDecimal(modelo.getPrimaneta(), 2));
				recibo.setDerecho(fn.castBigDecimal(modelo.getDerecho(), 2));
				recibo.setRecargo(fn.castBigDecimal(modelo.getRecargo(), 2));
				recibo.setIva(fn.castBigDecimal(modelo.getDerecho(), 2));

				recibo.setPrimaTotal(fn.castBigDecimal(modelo.getPrimaTotal(), 2));
				recibo.setAjusteUno(fn.castBigDecimal(modelo.getAjusteUno(), 2));
				recibo.setAjusteDos(fn.castBigDecimal(modelo.getAjusteDos(), 2));
				recibo.setCargoExtra(fn.castBigDecimal(modelo.getCargoExtra(), 2));
				recibos.add(recibo);

			}

			modelo.setRecibos(recibos);
			return modelo;

		} catch (Exception ex) {
			modelo.setError(
					GnpAutos2Model.this.getClass().getTypeName() + " | " + ex.getMessage() + " | " + ex.getCause());
			return modelo;
		}
	}

}
