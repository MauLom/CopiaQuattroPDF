package com.copsis.models.ana;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.copsis.constants.ConstantsValue;
import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraCoberturasModel;
import com.copsis.models.EstructuraJsonModel;

public class AnaAutosModelRoja {

	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
	private String contenido = "";
	private static final String FORMATDATE="yyyy-MM-dd";


	public AnaAutosModelRoja(String contenido) {
		this.contenido = contenido;
	}

	public EstructuraJsonModel procesar() {
		String newcontenido = "";
		StringBuilder direccion = new StringBuilder();
		String vigencias = "";
		int inicio = 0;
		int fin = 0;
		boolean cp = true;

		contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());
		contenido = contenido.replace("Fecha de Exp.", "Fecha de Expedición")
				.replace("Mens. prorrat", "Mensual")
				.replace("Pagos Subsec", "Pagos Subsecuentes").replace("For. de Pago", "Forma de pago")
                .replace("Forma de Pago:", "Forma de pago:")
				.replace("Subsec:", ConstantsValue.SUBSECUENTE).replace("Prima Net a :",ConstantsValue.PRIMA_NETA3)
				.replace("Prima Tota l:", ConstantsValue.PRIMA_TOTAL );

		try {
			modelo.setTipo(1);
			// cia
			modelo.setCia(7);

			inicio = contenido.indexOf("PÓLIZA DE SEGURO AUTOMÓVILES");
			fin = contenido.indexOf("Coberturas Amparada");

			if (inicio > -1 && fin > -1 && inicio < fin) {
				newcontenido = contenido.substring(inicio, fin).replace("@@@", "").replace("\r", "");
				for (int i = 0; i < newcontenido.split("\n").length; i++) {

					if (newcontenido.split("\n")[i].contains("Póliza") && newcontenido.split("\n")[i].contains("Inciso")
							&& newcontenido.split("\n")[i].contains("Endoso")) {
						modelo.setPoliza(newcontenido.split("\n")[i].split("Póliza:")[1].split("Inciso")[0]
								.replace("###", "").trim());
						modelo.setEndoso(newcontenido.split("\n")[i].split("Endoso:")[1].replace("###", "").trim());
					}
					if (newcontenido.split("\n")[i].contains(ConstantsValue.RFC) && newcontenido.split("\n")[i].contains(ConstantsValue.NO_CLIENTE)
							&& newcontenido.split("\n")[i].contains("Pague")) {
						modelo.setRfc(newcontenido.split("\n")[i].split(ConstantsValue.RFC)[1].split("No.Cliente")[0]
								.replace("###", "").trim());
						modelo.setIdCliente(newcontenido.split("\n")[i].split(ConstantsValue.NO_CLIENTE)[1].split("Pague")[0]
								.replace("###", "").trim());
					}

					if (newcontenido.split("\n")[i].contains(ConstantsValue.RFC) && newcontenido.split("\n")[i].contains(ConstantsValue.NO_CLIENTE)
							&& newcontenido.split("\n")[i].contains("Clave Agente")) {
						modelo.setRfc(newcontenido.split("\n")[i].split(ConstantsValue.RFC)[1].split("No.Cliente")[0]
								.replace("###", "").trim());
						modelo.setIdCliente(newcontenido.split("\n")[i].split(ConstantsValue.NO_CLIENTE)[1].split("Clave Agente")[0]
								.replace("###", "").trim());
					}

					if (newcontenido.split("\n")[i].contains("Nombre")
							&& newcontenido.split("\n")[i].contains("Contratante")
							&& newcontenido.split("\n")[i].contains("Dirección")) {
						modelo.setCteNombre(newcontenido.split("\n")[i + 1].replace("###", "").trim());

						if (newcontenido.split("\n")[i + 3].contains("Duración:")) {
							direccion.append(newcontenido.split("\n")[i + 4]);

							if (newcontenido.split("\n")[i + 5].contains("Fecha de Expedición")) {
								direccion.append(newcontenido.split("\n")[i + 4].split("Fecha")[0]);
							}
							modelo.setCteDireccion(direccion.toString().replace("###", ""));
						}
					} else {
						if (newcontenido.split("\n")[i].contains("Nombre")
								&& newcontenido.split("\n")[i].contains("Dirección")) {
							modelo.setCteNombre(newcontenido.split("\n")[i + 1].replace("###", "").trim());

							if (newcontenido.split("\n")[i + 2].contains("Duración:")) {
								direccion.append(newcontenido.split("\n")[i + 3].split("Fecha")[0]);

								if (newcontenido.split("\n")[i + 4].contains("C.P.")) {
									direccion.append(" " + newcontenido.split("\n")[i + 4].split("###")[0] + "  "
											+ newcontenido.split("\n")[i + 4].split("###")[1]);
								}
								modelo.setCteDireccion(direccion.toString().replace("###", ""));
							}
						}

					}

					if (newcontenido.split("\n")[i].contains("C.P.") && fn.isNumeric(newcontenido.split("\n")[i].replace("C.P.", "C.P.").split("C.P.")[1].substring(0, 5).trim()) && cp) {												
							modelo.setCp(newcontenido.split("\n")[i].replace("C.P.", "C.P.").split("C.P.")[1].substring(0, 5));		
							cp =false;
					}
					
					if (newcontenido.split("\n")[i].contains("Expedición")
							&& newcontenido.split("\n")[i].contains("Desde")
							&& newcontenido.split("\n")[i].contains("Hasta")) {
						vigencias = fn.gatos(newcontenido.split("\n")[i + 1].replace("###", "").replace("D", "###")
								.replace("M", "###").replace("A", "###"));

						int to = 0;
						int sp = vigencias.split("###").length;
						to = sp - 9;

						if (sp == 12) {
							vigencias = fn.gatos(vigencias.split(vigencias.split("###")[2])[1]);
							sp = vigencias.split("###").length;
						} else {
							if (sp > 9) {
								vigencias = fn.gatos(
										vigencias.split(vigencias.split("###")[to - 1])[1].replace(" ", "").trim());
								sp = vigencias.split("###").length;
							}

						}

						if (sp == 9) {
							modelo.setVigenciaA(vigencias.split("###")[8] + "-" + vigencias.split("###")[7] + "-"
									+ vigencias.split("###")[6]);
							modelo.setVigenciaDe(vigencias.split("###")[5] + "-" + vigencias.split("###")[4] + "-"
									+ vigencias.split("###")[3]);
							modelo.setFechaEmision(vigencias.split("###")[2] + "-" + vigencias.split("###")[1] + "-"
									+ vigencias.split("###")[0]);
						}
					}

					if (newcontenido.split("\n")[i].contains("Cobertura:")
							&& newcontenido.split("\n")[i].contains("Pagos Subsecuentes")
							&& newcontenido.split("\n")[i].contains("Recargos")) {
						modelo.setPlan(newcontenido.split("\n")[i].split("Cobertura:")[1].split("Pagos")[0]
								.replace("###", "").trim());
						modelo.setRecargo(fn.castBigDecimal(fn
								.preparaPrimas(newcontenido.split("\n")[i].split("Recargos:")[1].replace("###", ""))));
					}
					if (newcontenido.split("\n")[i].contains("Moneda:")
							&& newcontenido.split("\n")[i].contains("Inicial:")
							&& newcontenido.split("\n")[i].contains("Gastos:")) {
						modelo.setDerecho(fn.castBigDecimal(
								fn.preparaPrimas(newcontenido.split("\n")[i].split("Gastos:")[1].replace("###", ""))));

					}
						
					if (newcontenido.split("\n")[i].contains("pago:")
							&& newcontenido.split("\n")[i].contains(ConstantsValue.SUBSECUENTE)
							&& newcontenido.split("\n")[i].contains("I.V.A:")) {						
						modelo.setFormaPago(fn
								.formaPago(newcontenido.split("\n")[i].split("Forma de pago:")[1].split(ConstantsValue.SUBSECUENTE)[0]
										.replace("###", "").replace(".", "").trim()));
						modelo.setIva(fn.castBigDecimal(
								fn.preparaPrimas(newcontenido.split("\n")[i].split("I.V.A:")[1].replace("###", ""))));
					}

					if (newcontenido.split("\n")[i].contains("Prima Neta:")
							&& newcontenido.split("\n")[i].contains(ConstantsValue.PRIMA_TOTAL)) {
						modelo.setPrimaneta(fn.castBigDecimal(fn
								.preparaPrimas(newcontenido.split("\n")[i].split("Prima Neta:")[1].split(ConstantsValue.PRIMA_TOTAL)[0]
										.replace("###", ""))));
						modelo.setPrimaTotal(fn.castBigDecimal(
								fn.preparaPrimas(newcontenido.split("\n")[i].split(ConstantsValue.PRIMA_TOTAL)[1].replace("###", ""))));
					}
					if (newcontenido.split("\n")[i].contains("Descripción:")) {
						modelo.setDescripcion(
								newcontenido.split("\n")[i].split("Descripción:")[1].replace("###", "").trim());
					}
					if (newcontenido.split("\n")[i].contains("No.Motor:")
							&& newcontenido.split("\n")[i].contains("Capacidad:")
							&& newcontenido.split("\n")[i].contains("Modelo:")) {
						modelo.setMotor(newcontenido.split("\n")[i].split("No.Motor:")[1].split("Capacidad")[0]
								.replace("###", ""));
						modelo.setModelo(Integer
								.parseInt(newcontenido.split("\n")[i].split("Modelo:")[1].replace("###", "").trim()));
					}

					if (newcontenido.split("\n")[i].contains("Serie:")
							&& newcontenido.split("\n")[i].contains(ConstantsValue.PLACAS)) {
						modelo.setSerie(
								newcontenido.split("\n")[i].split("Serie:")[1].split(ConstantsValue.PLACAS)[0].replace("###", ""));

						if (newcontenido.split("\n")[i].split("Placa")[1].length() > 7) {
							modelo.setPlacas(newcontenido.split("\n")[i].split(ConstantsValue.PLACAS)[1].replace("###", "").trim());
						}

					}

				}
			}

			modelo.setMoneda(1);

			if(modelo.getFormaPago() == 0 && modelo.getVigenciaDe().length() == 10 && modelo.getVigenciaA().length() == 10 && contenido.contains(ConstantsValue.FORMA_PAGO2) ) {
				long diasVigencia = calculaDiasVigencia(modelo.getVigenciaDe(), modelo.getVigenciaA());
				String textoFormaPago = fn.gatos(contenido.split(ConstantsValue.FORMA_PAGO2)[1].replace(":", "").trim());
				textoFormaPago = textoFormaPago.split("###")[0].trim();
			
				if(diasVigencia>27 && diasVigencia<32 && textoFormaPago.equalsIgnoreCase("Domiciliada")) {
					modelo.setFormaPago(fn.formaPago("CONTADO"));
				}
			}
			
			inicio = contenido.indexOf("Canal de Venta, Agente:");
			fin = contenido.indexOf("Tel");

			if (inicio > -1 && fin > -1 && inicio < fin) {
				newcontenido = contenido.substring(inicio, fin).replace("@@@", "").replace("\r", "");

				for (int i = 0; i < newcontenido.split("\n").length; i++) {

					if (newcontenido.split("\n")[i].contains(ConstantsValue.AGENTE)) {
						if (newcontenido.split("\n")[i].split(ConstantsValue.AGENTE)[1].split("###")[1].length() > 10) {
							modelo.setCveAgente(newcontenido.split("\n")[i].split(ConstantsValue.AGENTE)[1].split("###")[1]
									.replace("###", "").substring(0, 5));
							modelo.setAgente(newcontenido.split("\n")[i].split(modelo.getCveAgente())[1]
									.replace("###", "").trim());
						} else {
							modelo.setCveAgente(newcontenido.split("\n")[i].split(ConstantsValue.AGENTE)[1].split("###")[1]);
							modelo.setAgente(newcontenido.split("\n")[i].split(ConstantsValue.AGENTE)[1].split("###")[2]);
						}

					}

				}
			}

			inicio = contenido.indexOf("Coberturas Amparada");
			fin = contenido.indexOf("A.N.A. Compañía de Seguros");

			if (inicio > -1 && fin > -1 && inicio < fin) {
				List<EstructuraCoberturasModel> coberturas = new ArrayList<>();
				newcontenido = contenido.substring(inicio, fin).replace("@@@", "").replace("\r", "");
				for (int i = 0; i < newcontenido.split("\n").length; i++) {
					EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();

					if (!newcontenido.split("\n")[i].contains("Unidad de Medida") &&
						!newcontenido.split("\n")[i].contains("www.anaseguros") &&
						!newcontenido.split("\n")[i].contains("Valor Comercial") && 
						!newcontenido.split("\n")[i].contains("Las sumas aseguradas de RC") && 
						!newcontenido.split("\n")[i].contains("Aplican condiciones generales") && 
						!newcontenido.split("\n")[i].contains("Coberturas Amparadas")) {
						
						int sp = newcontenido.split("\n")[i].split("###").length;
						switch (sp) {
						case 2:case 3:
							cobertura.setNombre(eliminaNumeroDeNombreCobertura(newcontenido.split("\n")[i].split("###")[0]));
							cobertura.setSa(newcontenido.split("\n")[i].split("###")[1]);
							coberturas.add(cobertura);
							break;
						case 4:
							cobertura.setNombre(eliminaNumeroDeNombreCobertura(newcontenido.split("\n")[i].split("###")[0]));
							cobertura.setDeducible(newcontenido.split("\n")[i].split("###")[1].trim());
							cobertura.setSa(newcontenido.split("\n")[i].split("###")[2]);
							coberturas.add(cobertura);
							break;
						default:
							break;
						}
					} 
				}
				modelo.setCoberturas(coberturas);

			}

			return modelo;
		} catch (Exception e) {
			return modelo;
		}

	}
	
	private long calculaDiasVigencia(String vigenciaDe, String vigenciaA) {
		SimpleDateFormat sdf = new SimpleDateFormat(FORMATDATE);
        Date dateVigenciaDe;
        Date dateVigenciaA;
        long diferencia = 0;
		try {
			dateVigenciaDe = sdf.parse(vigenciaDe);
			dateVigenciaA = sdf.parse(vigenciaA);

			long diferenciaMilli = Math.abs(dateVigenciaA.getTime() - dateVigenciaDe.getTime());
		     diferencia = TimeUnit.DAYS.convert(diferenciaMilli, TimeUnit.MILLISECONDS);
		} catch (ParseException e) {}
		return diferencia;
	}
	
	private String eliminaNumeroDeNombreCobertura(String nombre) {
		String resultado = nombre.trim();
		if(fn.isNumeric(resultado.split(" ")[0])){
			resultado = resultado.replace(resultado.split(" ")[0], "");
		}
		return resultado;
	}
}
