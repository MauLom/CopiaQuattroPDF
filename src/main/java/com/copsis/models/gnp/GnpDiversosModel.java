package com.copsis.models.gnp;

import java.util.ArrayList;
import java.util.List;

import com.copsis.constants.ConstantsValue;
import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraCoberturasModel;
import com.copsis.models.EstructuraJsonModel;
import com.copsis.models.EstructuraRecibosModel;
import com.copsis.models.EstructuraUbicacionesModel;

public class GnpDiversosModel {
	// Clases
	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
	// Variables
	private String contenido = "";

	private String seccion = "";
	private String ubicacionesT = "";
	private int inicio = 0;
	private int fin = 0;
	private int tipo;
	private boolean esverdad;

	// constructor
	public GnpDiversosModel(String contenido, String ubicaciones, int tipo) {
		this.contenido = contenido;
		this.ubicacionesT = ubicaciones;
		this.tipo = tipo;
	}

	public EstructuraJsonModel procesar() {
		int index = 0;
		StringBuilder nombre = new StringBuilder();
		StringBuilder resultado = new StringBuilder();
		StringBuilder newcontenido = new StringBuilder();
		String contenidolower = "";
		contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());
		contenidolower = contenido.toLowerCase();
		try {
			modelo.setTipo(7);
			// cia
			modelo.setCia(18);
			// poliza
			inicio = contenido.indexOf("liza No.");
			if (inicio > -1) {
				modelo.setPoliza(
						fn.gatos(contenido.substring(inicio + 8, inicio + 150).split("\r\n")[0].trim()).trim());
			}

			// renovacion
			inicio = contenido.indexOf("Renovación");
			if (inicio > -1) {
				modelo.setRenovacion(
						fn.gatos(contenido.substring(inicio + 10, inicio + 150).split("\r\n")[0].trim()).trim());
			}

			// rfc
			inicio = contenido.indexOf("R.F.C:");
			if (inicio > -1) {
				resultado.append(contenido.substring(inicio + 8, inicio + 150).trim().split("\r\n")[0].trim());
				if (resultado.toString().contains(ConstantsValue.DESDE_LAS)) {
					modelo.setRfc(fn.gatos(resultado.toString().split(ConstantsValue.DESDE_LAS)[0].trim()));
				} else if (resultado.toString().contains("Hasta las")) {
					modelo.setRfc(fn.gatos(resultado.toString().split("Hasta las")[0].trim()));
				} else if (resultado.toString().contains(ConstantsValue.VIGENCIA2)) {
					modelo.setRfc(fn.gatos(resultado.toString().split(ConstantsValue.VIGENCIA2)[0].trim()));
				}
			}

			List<String> search = new ArrayList<>();
			search.add("Clave");
			search.add("C lave");
			search.add("En caso");
			search.add("Para mayor");
			search.add(ConstantsValue.GRUPO_NACIONAL);

			// cve_agente
			inicio = contenido.indexOf("Clave");
			if (inicio > -1) {
				newcontenido = new StringBuilder();
				newcontenido.append(fn.gatos(contenido.substring(inicio + 5, inicio + 150).split("\r\n")[0].trim()));
				resultado = new StringBuilder();
				for (String x : newcontenido.toString().split("\r\n")) {
					esverdad = false;
					for (String a : search) {
						fin = x.indexOf(a);
						if (fin > -1) {
							resultado.append(x.substring(0, fin).trim());
							esverdad = true;
							break;
						}
					}
					if (!esverdad)
						resultado.append(x.trim()).append(" ");
				}
				modelo.setCveAgente(resultado.toString().replace("###", " ").trim());
			}

			// agente
			inicio = contenido.indexOf(ConstantsValue.AGENTE2);
			if (inicio > -1) {
				newcontenido = new StringBuilder();
				newcontenido.append(fn.gatos(contenido.substring(inicio + 6, inicio + 180)).replace("@@@", "").trim());
				resultado = new StringBuilder();
				esverdad = false;
				for (String x : newcontenido.toString().split("\r\n")) {
					if (x.trim().length() > 0) {
						
						for (String a : search) {
							fin = x.indexOf(a);
							if (fin > -1) {
								resultado.append(x.substring(0, fin).trim());
								esverdad = true;
								break;
							}
						}
						if (!esverdad)
							resultado.append(x.trim()).append(" ");
					}
				}
				modelo.setAgente(resultado.toString().replace("###", " ").trim());
			}

			// id_cliente
			inicio = contenido.indexOf("Código Cliente");
			if (inicio > -1) {
				resultado = new StringBuilder();
				resultado.append(contenido.substring(inicio + 14, inicio + 150).trim().split("\r\n")[0]);
				if (resultado.toString().contains("Duraci")) {
					modelo.setIdCliente(fn.gatos(resultado.toString().split("Duraci")[0]).trim());
				}
			}

			// plan
			fin = contenido.indexOf("Póliza No.");
			if (fin > -1) {
				resultado = new StringBuilder();
				resultado.append(fn.gatos(contenido.substring(0, fin).trim()));
				inicio = resultado.indexOf(ConstantsValue.DANIOS);
				if (inicio > -1) {
					modelo.setPlan(fn.gatos(resultado.substring(inicio + 5, resultado.length())));
				}
			}

			// cte_nombre
			inicio = contenido.indexOf("Contratante");
			if (inicio > -1) {
				newcontenido = new StringBuilder();
				resultado = new StringBuilder();
				resultado.append(contenido.substring(inicio + 11, inicio + 200).trim());
				int idx = 0;
				for (String x : resultado.toString().split("\n")) {
					if (idx == 1) {
						if (x.contains(ConstantsValue.NUMERO)) {
							x = fn.gatos(x.split(ConstantsValue.NUMERO)[0].trim());
						}
						newcontenido.append(fn.gatos(x.trim()));
					} else if (idx == 2  && !(x.contains("CALLE") || x.contains("Vigencia") || x.contains("AVENIDA") || x.contains("BOULEVARD")|| x.contains("CARRETERA") || x.contains("Número"))) {					
							newcontenido.append(fn.gatos(x.trim()));
					}
					idx++;
				}
				modelo.setCteNombre(newcontenido.toString());
			}

			// cte_direccion
			newcontenido = new StringBuilder();
			inicio = contenido.indexOf(modelo.getCteNombre());
			if (inicio > -1) {
				int longtxt = modelo.getCteNombre().length();
				if (longtxt > 0) {
					resultado = new StringBuilder();
					resultado.append(fn.gatos(contenido.substring(inicio + longtxt, inicio + 350).trim()).trim());
					for (String x : resultado.toString().split("\r\n")) {
						if (!x.contains("Número")) {
							fin = x.indexOf(ConstantsValue.VIGENCIA2);
							if (fin > -1) {
								newcontenido.append(fn.gatos(x.substring(0, fin).trim())).append(" ");
							} else {
								newcontenido.append(x.trim()).append("\r\n");
							}
						}
					}
					if (newcontenido.toString().contains("C.P")) {
						modelo.setCteDireccion(
								newcontenido.toString().split("C.P")[0].trim().replace("###", " ").replace("\r\n", ""));
					}
				}
			}

			// cp
			inicio = contenido.indexOf(", C.P");
			if (inicio > -1) {
				resultado = new StringBuilder();
				resultado.append(fn
						.gatos(contenido.substring(inicio + 5, inicio + 250).replace(".", "").split("\r\n")[0].trim()));
				if (resultado.toString().contains(ConstantsValue.VIGENCIA2)) {
					modelo.setCp(fn.gatos(resultado.toString().split(ConstantsValue.VIGENCIA2)[0].trim()));
				} else if (resultado.toString().contains(ConstantsValue.DESDE_LAS)) {
					modelo.setCp(fn.gatos(resultado.toString().split(ConstantsValue.DESDE_LAS)[0].trim()));
				} else if (fn.isNumeric(resultado.toString())) {
					modelo.setCp(resultado.toString());
				}
			}

			// moneda
			inicio = contenido.indexOf("Moneda");
			if (inicio > -1) {
				resultado = new StringBuilder();
				resultado.append((contenido.substring(inicio + 6, inicio + 150).trim().split("\r\n")[0].trim()));
				if (resultado.toString().contains("Derecho de P")) {
					modelo.setMoneda(fn.moneda(fn.gatos(resultado.toString().split("Derecho de P")[0].trim())));
				}
			}

			// forma_pago
			inicio = contenido.indexOf("Forma de pago");
			if (inicio > -1) {
				resultado = new StringBuilder();
				resultado.append(
						fn.gatos(contenido.substring(inicio + 13, inicio + 150).trim().split("\r\n")[0].trim()));
				if (resultado.toString().contains("Recargo Pago")) {
					modelo.setFormaPago(fn.formaPago(fn.gatos(resultado.toString().split("Recargo Pago")[0].trim())));
				}
			}

			if(modelo.getFormaPago() == 0) {
				modelo.setFormaPago(fn.formaPagoSring(contenido));
			}
			// vigencia_de
			inicio = contenido.indexOf("Desde las 12 hrs");
			if (inicio > -1) {
				resultado = new StringBuilder();
				resultado.append(fn.gatos(contenido.substring(inicio + 16, inicio + 150).replace("del", "").trim().split("\r\n")[0].trim())
						.replace("###", "-"));
				if (resultado.toString().split("-").length == 3) {
					modelo.setVigenciaDe(fn.formatDate(resultado.toString(), ConstantsValue.FORMATO_FECHA));
				}
			}

			// vigencia_a
			inicio = contenido.indexOf("Hasta las 12 hrs");
			if (inicio > -1) {
				resultado = new StringBuilder();
				resultado.append(fn.gatos(contenido.substring(inicio + 16, inicio + 150).replace("del", "").trim().split("\r\n")[0].trim())
						.replace("###", "-"));
				if (resultado.toString().split("-").length == 3) {
					modelo.setVigenciaA(fn.formatDate(resultado.toString(), ConstantsValue.FORMATO_FECHA));
				}
			}

			// fecha_emision
			inicio = contenido.indexOf("Fecha de expedición");
			if (inicio > -1) {
				String auxStr = "";
				resultado = new StringBuilder();
				resultado.append(contenido.substring(inicio + 19, inicio + 150).split("\r\n")[0]);
				auxStr = resultado.toString();
				if (resultado.toString().contains("Tipo de")) {
					resultado = new StringBuilder();
					resultado.append(fn.gatos(auxStr.split("Tipo de")[0].trim()).replace("###", "-"));
				} else if (resultado.toString().contains(ConstantsValue.IVA)) {
					resultado = new StringBuilder();
					resultado.append(fn.gatos(auxStr.split(ConstantsValue.IVA)[0].trim()).replace("###", "-"));
				}

				if (resultado.toString().split("-").length == 3 && resultado.length() == 10) {
					modelo.setFechaEmision(fn.formatDate(resultado.toString(), "dd-MM-yy"));
				}
			}

			if (modelo.getFechaEmision().length() == 0) {
				inicio = contenido.lastIndexOf("Derecho de Póliza");
				fin = contenido.indexOf(ConstantsValue.IVA);
				if (inicio > -1 && fin > inicio) {
					newcontenido = new StringBuilder();
					newcontenido.append(contenido.substring(inicio + 17, fin).trim());
					if (newcontenido.toString().split("\r\n").length == 2) {
						String auxStr = newcontenido.toString();
						newcontenido = new StringBuilder();
						newcontenido.append(fn.gatos(auxStr.split("\r\n")[1].trim()).replace("###", "-"));
						if (newcontenido.toString().split("-").length == 3 && newcontenido.length() == 10) {
							modelo.setFechaEmision(fn.formatDate(newcontenido.toString(), "dd-MM-yy"));
						}
					}
				}
			}

			if (modelo.getFechaEmision().length() == 0) {
				modelo.setFechaEmision(modelo.getVigenciaDe());
			}

			// prima_neta
			inicio = contenido.indexOf("Prima Neta");
			if (inicio > -1) {
			//
				modelo.setPrimaneta(fn.castBigDecimal(fn.preparaPrimas(
						fn.gatos(contenido.substring(inicio + 10, inicio + 150).trim().split("\r\n")[0].trim()))));
			}

			// recargo
			inicio = contenido.indexOf("Pago Fraccionado");
			if (inicio > -1) {
				modelo.setRecargo(fn.castBigDecimal(fn.preparaPrimas(
						fn.gatos(contenido.substring(inicio + 16, inicio + 150).trim().split("\r\n")[0].trim()))));
			}

			// derecho
			inicio = contenido.indexOf("Derecho de Póliza");
			if (inicio > -1) {			
				modelo.setDerecho(fn.castBigDecimal(fn.preparaPrimas(
						fn.gatos(contenido.substring(inicio + 17, inicio + 150).trim().split("\r\n")[0].trim()))));
			}

			// iva
			inicio = contenido.indexOf("I.V.A.");
			if (inicio > -1) {
				resultado = new StringBuilder();			
				resultado.append(fn.gatos(contenido.substring(inicio + 6, inicio + 150).trim().split("\r\n")[0].trim()));
				inicio = resultado.indexOf("%");
				if (inicio > -1) {					
					String auxStr = resultado.toString();
					resultado = new StringBuilder();
					resultado.append(fn.gatos(auxStr.substring(inicio + 1, auxStr.length())));
				}	
				modelo.setIva(fn.castBigDecimal(fn.preparaPrimas(resultado.toString())));
			}

			// prima_total
			inicio = contenidolower.indexOf("importe a pagar");
			if (inicio > -1) {
				modelo.setPrimaTotal(fn.castBigDecimal(fn.preparaPrimas(
						fn.gatos(contenido.substring(inicio + 15, inicio + 150).trim().split("\r\n")[0].trim()))));
			}

			// LAS COBERTURAS(TITULOS O TUTULOS Y DESGLOCE)
			// TUTULOS 1
			newcontenido = new StringBuilder();
			inicio = contenido.indexOf("Riesgos###Prima Neta###");
			fin = contenido.indexOf(ConstantsValue.AGENTE2);
			if (inicio > -1 && fin > inicio) {
				newcontenido.append(contenido.substring(inicio + 23, fin).trim());
				List<String> buscar = new ArrayList<>();
				buscar.add(ConstantsValue.AGENTE2);
				buscar.add("Grupo Nacional");
				for (String x : buscar) {
					fin = newcontenido.indexOf(x);
					if (fin > -1) {
						resultado = new StringBuilder();
						resultado.append(newcontenido.substring(0, fin).replace("@@@", "").trim());
					}
				}
				newcontenido.append(clearCoberturas(resultado.toString()));
			}

			// ***************************************************************************
			// TITULO 2
			inicio = contenido.indexOf("Renovación póliza");
			if (inicio > -1) {
				newcontenido = new StringBuilder();
				newcontenido.append(contenido.substring(inicio + 17, contenido.length()).trim());
				fin = newcontenido.indexOf("Grupo Nacional");
				if (fin > -1) {
					resultado = new StringBuilder();
					resultado.append(newcontenido.substring(0, fin).replace("@@@", "").trim());
					newcontenido.append(clearCoberturas(resultado.toString()));
				}
			}

			// ***************************************************************************
			// TITULO 3
			inicio = contenido.indexOf("secciones contratadas");
			fin = contenido.indexOf("Grupo###Nacional###Provincial");

			if (inicio > -1 && fin > inicio) {
				newcontenido = new StringBuilder();
				newcontenido.append(fn.gatos(contenido.substring(inicio + 21, fin).replace("@@@", "")).trim()
						.replace(" Amparado", "###Amparado"));
				resultado = new StringBuilder();
				for (String x : newcontenido.toString().split("\r\n")) {
					if (x.trim().length() > 0) {
						resultado.append(fn.gatos(x.trim()) + "\r\n");
					}
				}
				newcontenido.append(resultado.toString());
			}

			List<EstructuraCoberturasModel> coberturas = new ArrayList<>();
			// SE RECORRE LOS DOS CASOS ANTERIORES CON ESTE FOR (TITULO 1 O TUTULO 2)
			String auxStr = newcontenido.toString();
			newcontenido = new StringBuilder();
			newcontenido.append(auxStr.replace(" Amparado", "###Amparado").replace("######", "###"));
			if (newcontenido.toString().contains("Grupo###Nacional")) {
				auxStr = newcontenido.toString();
				newcontenido = new StringBuilder();
				newcontenido.append(auxStr.split("Grupo###Nacional")[0]);
			}
			if (newcontenido.length() > 0) {
				for (int i = 0; i < newcontenido.toString().split("\r\n").length; i++) {
					EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();

					String x = "";
					String y = "";
					x = newcontenido.toString().split("\r\n")[i];
					if (!x.contains("Especificación de Bienes")) {
						if (i + 1 < newcontenido.toString().split("\r\n").length
								&& newcontenido.toString().split("\r\n")[i + 1].split("###").length == 1) {
							y = newcontenido.toString().split("\r\n")[i + 1];

						}

						if (x.split("###").length == 3) {
							nombre = new StringBuilder();
							nombre.append(x.split("###")[1].trim());
							if (y.length() > 0) {
								nombre.append(" ").append(y.trim());
							}

							cobertura.setSeccion(x.split("###")[0].trim());
							cobertura.setNombre(nombre.toString());
							if (x.split("###")[2].trim().equals("Amparado")) {
								cobertura.setSa(x.split("###")[2].trim());
							}
							coberturas.add(cobertura);

						}

					}
				}
			}

			// ***************************************************************************
			// COBERTURAS 
			extctCoberturas(coberturas, tipo);
			modelo.setCoberturas(coberturas);
			if(modelo.getCoberturas().isEmpty()) {
				inicio = contenido.indexOf("Coberturas Contratadas");
				fin = contenido.indexOf("Vigencia Póliza");
				if (inicio > -1 && fin > inicio) {					
					newcontenido = new StringBuilder();
					newcontenido.append(contenido.substring(inicio + 21, fin).replace("@@@", "").replace("\r", ""));
					for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {					
						EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();
						if(!newcontenido.toString().split("\n")[i].contains("Suma Asegurada") && newcontenido.toString().split("\n")[i].split("###").length == 3) {
							cobertura.setNombre(newcontenido.toString().split("\n")[i].split("###")[0]);
							cobertura.setSa(newcontenido.toString().split("\n")[i].split("###")[1]);						
							coberturas.add(cobertura);
						}
					}
					modelo.setCoberturas(coberturas);
				}				
			}
			
			if(modelo.getCoberturas().isEmpty()) {
				inicio = contenido.indexOf("secciones contratadas");
				fin = contenido.lastIndexOf("Para mayor información contáctenos:");
				if (inicio > -1 && fin > inicio) {	
					newcontenido = new StringBuilder();
					newcontenido.append(contenido.substring(inicio + 21, fin).replace("@@@", "").replace("\r", "")
							.replace("Importe Total Actualizado###", "")
							.replace("Importe Total Anterior###",""));
					
					for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {					
						EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();
						if(!newcontenido.toString().split("\n")[i].contains("Vigencia") &&
							!newcontenido.toString().split("\n")[i].contains("Importe Total") &&
							!newcontenido.toString().split("\n")[i].contains("Suma Asegurada") &&
							!newcontenido.toString().split("\n")[i].contains("Descripción del Movimiento") &&
							!newcontenido.toString().split("\n")[i].contains("Especificación de Bienes") && 
							!newcontenido.toString().split("\n")[i].contains("Petición del Asegurado") &&
							!newcontenido.toString().split("\n")[i].contains("Duración") &&
							!newcontenido.toString().split("\n")[i].contains("po Nacional Pr")&&
							!newcontenido.toString().split("\n")[i].contains("Agente")) {
							
							if(newcontenido.toString().split("\n")[i].trim().split("###").length == 3 || 
							    newcontenido.toString().split("\n")[i].contains("Equipo Electrónico")) {
								cobertura.setNombre(newcontenido.toString().split("\n")[i].split("###")[1]);			
								coberturas.add(cobertura);
							}else if(newcontenido.toString().split("\n")[i].contains("Presión") && (i-1)>0){
								if(newcontenido.toString().split("\n")[i-1].contains("Calderas y Recipientes Sujetos a")) {
									String textAuxiliar = "Calderas y Recipientes Sujetos a Presión";
									cobertura.setNombre(newcontenido.toString().split("\n")[i-1].contains("No contratada")? textAuxiliar+" No contratada": textAuxiliar);
									coberturas.add(cobertura);
								}
							}
						}
					}
					modelo.setCoberturas(coberturas);
					
				}
			}
			

			// UBICACIONES
			List<EstructuraUbicacionesModel> ubicaciones = new ArrayList<>();
			inicio = contenido.indexOf(ConstantsValue.DESCRIPCION_MOVIMIENTO);
			fin = contenido.indexOf("Ubicado a Más");

			if (inicio > -1 && fin > inicio) {

				resultado = new StringBuilder();
				newcontenido = new StringBuilder();
				newcontenido.append(contenido.substring(inicio + 26, fin).trim());

				index = 0;
				EstructuraUbicacionesModel ubicacion = new EstructuraUbicacionesModel();
				for (String x : newcontenido.toString().split("\r\n")) {

					if (x.split("###").length == 2)
						resultado.append(x.split("###")[0].trim()).append("\r\n");
					else
						resultado.append(x.trim()).append("\r\n");
					if (index == 0) {
						int len = x.split(",").length;
						if (len > 1) {
							ubicacion.setCalle(x.split(",")[0].trim());
							boolean encontro = false;
							for (int i = 0; i < x.split(",")[0].trim().split(" ").length; i++) {
								String a = x.split(",")[0].trim().split(" ")[i];
								if (i == x.split(",")[0].trim().split(" ").length - 1) {
									if (fn.isNumeric(a) && !encontro) {
										ubicacion.setNoExterno(a);
										ubicacion.setCalle(
												x.split(",")[0].trim().split(ubicacion.getNoExterno())[0].trim());
									}
								} else {
									if (fn.isNumeric(a)) {
										encontro = true;
									}
								}
							}
							ubicacion.setColonia(x.split(",")[1].trim());
						}
					}
					if (x.contains("C.P") && x.split("###").length == 2) {

						ubicacion.setCp(x.split("###")[0].split("C.P.")[1].trim());
					}
					index++;
				}
				ubicaciones.add(ubicacion);
				modelo.setUbicaciones(ubicaciones);
			} else {

				inicio = contenido.indexOf("Descripción del Movimiento");
				fin = contenido.lastIndexOf("Asegurado###");

				resultado = new StringBuilder();
				newcontenido = new StringBuilder();
				if (inicio > -1 && fin > inicio) {

					EstructuraUbicacionesModel ubicacion = new EstructuraUbicacionesModel();
					newcontenido.append(contenido.substring(inicio + 26, fin).replace("@@@", "").trim());
					for (String x : newcontenido.toString().split("\r\n")) {
						if (x.contains(ConstantsValue.CARTERA_HASH))
							resultado.append(x.split(ConstantsValue.CARTERA_HASH)[0].trim()).append("\r\n");
						else
							resultado.append(x).append("\r\n");
					}

					// giro
					// nombre
					inicio = resultado.indexOf("Giro");
					fin = resultado.indexOf("Techos###");
					if (inicio > -1 && fin > inicio) {
						ubicacion.setGiro(fn.gatos(resultado.substring(inicio + 4, fin)).replace("###", "")
								.replace("\r\n", "").trim());
						ubicacion.setNombre(ubicacion.getGiro());
					}

					// techos
					inicio = resultado.indexOf(ConstantsValue.TECHOS);
					fin = resultado.indexOf(ConstantsValue.MUROS);

					if (inicio > -1 && fin > inicio) {
						newcontenido = new StringBuilder();
						newcontenido.append(
								resultado.substring(inicio + 6, fin).replace("###", "").replace("\r\n", " ").trim());

						if (newcontenido.toString().contains("MATERIALES INCOMBUSTIBLES")) {
							auxStr = newcontenido.toString();
							newcontenido = new StringBuilder();
							newcontenido.append(auxStr.replace("MATERIALES INCOMBUSTIBLES NO MACIZOS", "")
									.replace("(", "").replace(")", "").trim());

						}
						ubicacion.setTechos(fn.material(newcontenido.toString()));
					}

					// muros
					inicio = resultado.indexOf(ConstantsValue.MUROS);
					fin = resultado.indexOf("Ubicado");
					if (inicio > -1 && fin > inicio) {
						newcontenido = new StringBuilder();
						newcontenido.append(
								resultado.substring(inicio + 5, fin).replace("###", "").replace("\r\n", "").trim());
						ubicacion.setMuros(fn.material(newcontenido.toString()));
					}

					// niveles
					inicio = resultado.indexOf("No. de Pisos del edificio");
					if (inicio > -1) {
						newcontenido = new StringBuilder();
						newcontenido.append(resultado.substring(inicio + 25, resultado.length()).replace("###", "")
								.replace("\r\n", "").trim());
						if (newcontenido.toString().contains("(")) {
							auxStr = newcontenido.toString();
							newcontenido = new StringBuilder();
							newcontenido.append(auxStr.replace("(", "&&&&"));
							ubicacion.setNiveles(Integer.parseInt(newcontenido.toString().split("&&&&")[0].trim()));
						}
					}
					if (ubicacion.getNombre().length() > 0)
						ubicaciones.add(ubicacion);
					if (!ubicaciones.isEmpty()) {
						modelo.setUbicaciones(ubicaciones);
					}

				} else {

					inicio = ubicacionesT.indexOf("Descripción del Movimiento");
					fin = ubicacionesT.lastIndexOf("Resumen de secciones contratada");

					resultado = new StringBuilder();
					newcontenido = new StringBuilder();
					if (inicio > -1 && fin > inicio) {
                        System.err.println("UBICACIONEST primer caso");

						EstructuraUbicacionesModel ubicacion = new EstructuraUbicacionesModel();
						newcontenido.append(ubicacionesT.substring(inicio + 26, fin).replace("@@@", "").trim());
						System.out.println(newcontenido);
						for (String x : newcontenido.toString().split("\r\n")) {
							if (x.contains("###CARTERA"))
								resultado.append(x.split("###CARTERA")[0].trim()).append("\r\n");
							else
								resultado.append(x).append("\r\n");
						}

						// giro
						// nombre
						inicio = resultado.indexOf("Giro");
						fin = resultado.indexOf("Descripción del Giro");
						if (inicio > -1 && fin > inicio) {
							ubicacion.setGiro(fn.gatos(resultado.substring(inicio + 4, fin)).replace("###", "")
									.replace("\r\n", "").trim());
							ubicacion.setNombre(ubicacion.getGiro());
						}

						// techos
						inicio = resultado.indexOf("Techos");
						fin = resultado.indexOf("Muros");

						if (inicio > -1 && fin > inicio) {
							newcontenido = new StringBuilder();
							newcontenido.append(resultado.substring(inicio + 6, fin).replace("###", "")
									.replace("\r\n", " ").trim());

							System.out.println(newcontenido);
							if (newcontenido.toString().contains("MATERIALES INCOMBUSTIBLES")) {
								auxStr = newcontenido.toString();
								newcontenido = new StringBuilder();
								newcontenido.append(auxStr.replace("MATERIALES INCOMBUSTIBLES NO MACIZOS", "")
										.replace("(", "").replace(")", "").trim());
							}
							System.out.println("="+fn.material(newcontenido.toString())+"=");
							ubicacion.setTechos(fn.material(newcontenido.toString()));
						}

						// muros
						inicio = resultado.indexOf("Muros");
						fin = resultado.indexOf("Año de Construcción");
						if (inicio > -1 && fin > inicio) {
							newcontenido = new StringBuilder();
							newcontenido.append(
									resultado.substring(inicio + 5, fin).replace("###", "").replace("\r\n", "").trim());
							System.out.println(newcontenido);
							ubicacion.setMuros(fn.material(newcontenido.toString()));
						}

						// niveles
						inicio = resultado.indexOf("Número de pisos");
						fin = resultado.indexOf("Techos");
						if (inicio > -1) {
							newcontenido = new StringBuilder();
							newcontenido.append(resultado.substring(inicio + 15, fin).replace("###", "")
									.replace("\r\n", "").trim());

							if (fn.isNumeric(newcontenido.toString())) {

								ubicacion.setNiveles(Integer.parseInt(newcontenido.toString()));
							}
						}
						
						if(ubicacion.getNombre().length() > 0) {
							ubicaciones.add(ubicacion);
						}
						if (!ubicaciones.isEmpty())
							modelo.setUbicaciones(ubicaciones);
					}

				}
			}
			
			if(modelo.getUbicaciones().isEmpty()) {
			
				
				inicio = contenido.indexOf("Asegurado y ubicación");
				fin = contenido.indexOf("Prima del movimiento");
				
				newcontenido = new StringBuilder();
				if(inicio > -1 && fin >  -1 && inicio < fin ) {
					newcontenido.append(contenido.substring(inicio, fin).replace("@@@", "").trim());
					
					EstructuraUbicacionesModel ubicacion = new EstructuraUbicacionesModel();
					for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {									
		
						if(newcontenido.toString().split("\n")[i].contains("CALLE")) {				
							ubicacion.setCalle(newcontenido.toString().split("\n")[i].split(" ")[1]);
							ubicacion.setNoInterno(newcontenido.toString().split("\n")[i].split(" ")[2]);
							ubicacion.setCp(newcontenido.toString().split("\n")[i].split(" ")[3].replace(",", "").trim());						
						}
						if(newcontenido.toString().split("\n")[i].contains("Tipo constructivo")) {
				           ubicacion.setMuros(fn.material(newcontenido.toString().split("\n")[i].split("###")[1]));
						}
									
					}
					ubicaciones.add(ubicacion);
					modelo.setUbicaciones(ubicaciones);
				}
								
				
			}
			
			if(modelo.getUbicaciones().isEmpty() && ubicacionesT.contains("Ubicación")) {
				newcontenido = new StringBuilder();
				newcontenido.append(ubicacionesT.split("Ubicación")[1].replace("@@@", "").replace("Descripción del movimiento","").replace("PRODUCCION NUEVA", ""));
				
				String[] arrContenido = newcontenido.toString().split("\r\n");
				EstructuraUbicacionesModel ubicacion = new EstructuraUbicacionesModel();
				String textAuxiliar = "";
                System.out.println("Si es aqui ubicaciones****************");

				for(int i=0;i<arrContenido.length;i++) {
					if(arrContenido[i].contains("Asegurado") && ubicacion.getCalle().length() == 0) {
						String[] arrDetalle = arrContenido[i+2].split(",");
						String calle = arrDetalle[0];
						ubicacion.setNoExterno(fn.numTx(calle));
						ubicacion.setCalle(calle.split(ubicacion.getNoExterno())[0].replace("###", "").trim());
						
						if(arrDetalle.length >1) {
							//No. interno
							if(fn.isNumeric(arrDetalle[1])) {
								ubicacion.setNoInterno(arrDetalle[1]);
							}else if(fn.numTx(arrDetalle[1]).length() > 0) {
								ubicacion.setNoInterno(fn.numTx(arrDetalle[1]));
							}else{
								ubicacion.setColonia(arrDetalle[1].replace("###", "").trim());
							}
							
							//colonia 
							if(ubicacion.getColonia().length() == 0) {
								ubicacion.setColonia(arrDetalle[2].replace("###Día###Mes###Año", "").replace("###","").trim());
							}
						}

					}else if(arrContenido[i].contains("C.P.") && arrContenido[i].contains("###")) {
						ubicacion.setCp(arrContenido[i].split("C.P.")[1].split("###")[0].trim());
					}
					
					
					if (arrContenido[i].contains("Giro / Actividad") &&  newcontenido.toString().contains("Número de Equipos a Asegurar")) {
						inicio = newcontenido.toString().indexOf("Giro / Actividad");
						fin = newcontenido.toString().indexOf("Número de Equipos a Asegurar");
						if(inicio<fin) {
							ubicacion.setGiro(fn.gatos(newcontenido.substring(inicio + 16, fin)).replace("###", "")
									.replace("\r", "").replace("\n", "").trim());
							ubicacion.setNombre(ubicacion.getGiro());
						}
						
					} else if (arrContenido[i].contains("Giro") && ubicacion.getGiro().length() == 0) {
						ubicacion.setGiro(fn.gatos(arrContenido[i].split("Giro")[1]).replace("###", "").replace("\r\n","").trim());
						ubicacion.setNombre(ubicacion.getGiro());
					}

					//niveles
					if(arrContenido[i].contains("pisos")) {
						textAuxiliar = arrContenido[i].split("pisos")[1].replace("Vigencia Póliza", "").replace("###", "").trim();
						ubicacion.setNiveles(fn.castInteger(textAuxiliar));
					}
					
					//techos
					if(arrContenido[i].contains("Techos")) {
						ubicacion.setTechos(fn.material(arrContenido[i].split("Techos")[1]));
					}
					//muros
					if(arrContenido[i].contains("Muros")) {
						System.out.println(arrContenido[i].split("Muros")[1]);
						ubicacion.setMuros(fn.material(arrContenido[i].split("Muros")[1].replace("###", "").replace("\r\n", "").trim()));
					}
				}
				if(ubicacion.getCalle().length() >0 ) {
					ubicaciones.add(ubicacion);
					modelo.setUbicaciones(ubicaciones);
				}
			
			}

			// **************************************RECIBOS
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
				recibo.setIva(fn.castBigDecimal(modelo.getIva(), 2));

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
					GnpDiversosModel.this.getClass().getTypeName() + " | " + ex.getMessage() + " | " + ex.getCause());
			return modelo;
		}

	}

	public void extctCoberturas(List<EstructuraCoberturasModel> coberturas, Integer tipo) {
		StringBuilder newcontenido = new StringBuilder();
		StringBuilder resultado = new StringBuilder();
		if (tipo == 1) {// esto es para estraer coberutas de 1* version

			inicio = contenido.indexOf("###Coaseguro###Primas");
			fin = contenido.indexOf("Total Coberturas");
			StringBuilder nombre = new StringBuilder();
			if (inicio > -1 && fin > inicio) {
				resultado.append(contenido.substring(inicio + 21, fin).replace("@@@", "").trim());
				String auxStr = resultado.toString();
				resultado = new StringBuilder();
				resultado.append(auxStr.replace("C ris tales", "Cristales").replace("A rt ículos", "Artículos")
						.replace("Res ponsabilidad", "Responsabilidad").replace("p ro pietario", "propietario")
						.replace("G as tos", "Gastos").replace("Rem oción", "Remoción")
						.replace("Rie sgos", ConstantsValue.RIESGOS).replace("E qu ipo", "Equipo")
						.replace("Dañ os", "Daños").replace("Rie sgos", ConstantsValue.RIESGOS).replace("V  ", "V###")
						.replace("Me naje", "Menaje").replace("No Aplica  ", "No Aplica###")
						.replace("T er remoto", "Terremoto").replace("v olc ánica", "volcánica")
						.replace("M e naje", "Menaje").replace("D añ os", "Daños").replace("Cris tales", "Cristales")
						.replace("Gas tos", "Gastos").replace("Equ ipo", "Equipo")
						.replace("R ie sgos", ConstantsValue.RIESGOS).replace("R em oción", "Remoción")
						.replace("volc ánica", "volcánica").replace("I Rie", "Rie")
						.replace("No Aplica ", "No Aplica###").replace("V l###", "Vl###").replace("l ll###", "lll###"));
				newcontenido.append(clearCoberturas(resultado.toString()));
				for (int i = 0; i < newcontenido.toString().split("\r\n").length; i++) {
					EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();
					esverdad = false;
					String x = "";
					String y = "";
					String z = "";
					x = newcontenido.toString().split("\r\n")[i];
					if (i + 1 < newcontenido.toString().split("\r\n").length) {
						if (newcontenido.toString().split("\r\n")[i + 1].split("###").length == 1) {
							y = fn.cleanString(newcontenido.toString().split("\r\n")[i + 1]);
							if (fn.isNumeric(y)) {
								esverdad = true;
								y = "";
							} else if (!newcontenido.toString().split("\r\n")[i + 1].contains("Daños a")) {
								y = newcontenido.toString().split("\r\n")[i + 1].trim();
								esverdad = true;
							} else {
								y = "";
							}
						} else {
							y = "";
						}
					} else {
						y = "";
					}
					if (i + 2 < newcontenido.toString().split("\r\n").length) {
						if (newcontenido.toString().split("\r\n")[i + 2].split("###").length == 1 && esverdad) {
							z = fn.cleanString(newcontenido.toString().split("\r\n")[i + 2].trim());
							if (fn.isNumeric(z)) {
								z = "";
							} else if (!newcontenido.toString().split("\r\n")[i + 2].contains("Daños a")) {
								z = newcontenido.toString().split("\r\n")[i + 2].trim();
							} else {
								z = "";
							}
						} else {
							z = "";
						}
					} else {
						z = "";
					}
					if (x.split("###").length == 2) {
						seccion = x.split("###")[0].trim();
						cobertura.setNombre(x.split("###")[1].trim());
						cobertura.setSeccion(seccion);
						coberturas.add(cobertura);
					} else if ((x.contains("Daños al inmueble") || x.contains("Daños a los"))
							&& x.split("###").length == 1) {
						cobertura.setNombre(x.split("###")[0].trim());
						cobertura.setSeccion(seccion);
						coberturas.add(cobertura);
					} else {
						switch (x.split("###").length) {
						case 5:
						case 4:
							nombre = new StringBuilder();
							nombre.append(x.split("###")[0].trim());
							if (y.length() > 0) {
								nombre.append(" ").append(y);
							}
							if (z.length() > 0) {
								nombre.append(" ").append(z);
							}
							cobertura.setNombre(nombre.toString());
							cobertura.setSa(x.split("###")[1].trim());
							cobertura.setDeducible(x.split("###")[2].trim());
							cobertura.setCoaseguro(x.split("###")[3].trim());
							cobertura.setSeccion(seccion);
							coberturas.add(cobertura);
							break;
						default:
							break;
						}
					}
				}
			}
		}

		if (tipo == 2) {
			for (int i = 0; i < contenido.split(ConstantsValue.SUMA_ASEGURADA).length; i++) {
				if (contenido.split(ConstantsValue.SUMA_ASEGURADA)[i].contains("Cuando###se###refieran")) {
					newcontenido = new StringBuilder();
					newcontenido.append(contenido.split(ConstantsValue.SUMA_ASEGURADA)[i].split("Cuando###se###refieran")[0]);
				} else if (contenido.split(ConstantsValue.SUMA_ASEGURADA)[i].contains("Agente")
						&& contenido.split(ConstantsValue.SUMA_ASEGURADA)[i].contains("Coaseguro")) {
					newcontenido.append(contenido.split(ConstantsValue.SUMA_ASEGURADA)[i].split("Agente")[0]
							.split("SERVICIOS DE ASISTENCIAS AMPARADAS")[0]);
				}
			}

			for (String co : newcontenido.toString().split("\n")) {
				EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();
				int x = co.split("###").length;
				if (x > 1 && !co.contains("Deducible")) {

					if (x == 3) {
						cobertura.setNombre(co.split("###")[0]);
						cobertura.setSa(co.split("###")[1]);
						cobertura.setDeducible(co.split("###")[2].replace("\r", ""));
						cobertura.setCoaseguro(co.split("###")[2].replace("\r", ""));
						coberturas.add(cobertura);
					}
					if (x == 4) {
						cobertura.setNombre(co.split("###")[0]);
						cobertura.setSa(co.split("###")[1]);
						cobertura.setDeducible(co.split("###")[2].replace("\r", ""));
						cobertura.setCoaseguro(co.split("###")[3].replace("\r", ""));
						coberturas.add(cobertura);
					}

				}
			}

		}
	}

	private String clearCoberturas(String resultado) {
		StringBuilder a = new StringBuilder();
		try {
			for (String x : resultado.split("\r\n")) {
				x = x.trim();
				if (x.length() > 0 && (x.contains("las 12") || x.contains("Agosto") || !x.contains("Mes###Año"))) {
					boolean cobertura = false;
					fin = x.indexOf("Duración");
					if (fin > -1) {
						a.append(fn.gatos(x.substring(0, fin).trim()).trim()).append("\r\n");
						cobertura = true;
					}
					
					fin = x.indexOf("Importe");
					if (fin > -1) {
						a.append(fn.gatos(x.substring(0, fin).trim()).trim()).append("\r\n");
						cobertura = true;
					} else {
						fin = x.indexOf("mporte Total");
						if (fin > -1) {
							a.append(fn.gatos(x.substring(0, fin).trim()).trim()).append("\r\n");
							cobertura = true;
						}
					}
					fin = x.indexOf("Movimiento");
					if (fin > -1) {
						a.append(fn.gatos(x.substring(0, fin).trim()).trim()).append("\r\n");
						cobertura = true;
					}
					fin = x.indexOf("Descuentos");
					if (fin > -1) {
						a.append(fn.gatos(x.substring(0, fin).trim()).trim()).append("\r\n");
						cobertura = true;
					}
					fin = x.indexOf("Aplican Condiciones");
					if (fin > -1) {
						a.append(fn.gatos(x.substring(0, fin).trim()).trim()).append("\r\n");
						cobertura = true;
					}
					fin = x.indexOf("Póliza paquetes");
					if (fin > -1) {
						a.append(fn.gatos(x.substring(0, fin).trim()).trim()).append("\r\n");
						cobertura = true;
					}
					fin = x.indexOf("Vigencia");
					if (fin > -1) {
						a.append(fn.gatos(x.substring(0, fin).trim()).trim()).append("\r\n");
						cobertura = true;
					}
					if (!cobertura) {
						a.append(fn.gatos(x.trim())).append("\r\n");
					}
				}
			}
			return a.toString();
		} catch (Exception ex) {
			return a.toString();
		}
	}

}
