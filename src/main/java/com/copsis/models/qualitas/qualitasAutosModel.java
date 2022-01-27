package com.copsis.models.qualitas;

import java.util.ArrayList;
import java.util.List;

import com.copsis.constants.ConstantsValue;
import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraCoberturasModel;
import com.copsis.models.EstructuraJsonModel;
import com.copsis.models.EstructuraRecibosModel;


public class qualitasAutosModel {

	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
	private String contenido = "";
	private String cbo = "";

	public qualitasAutosModel(String contenido,String coberturas) {
		this.contenido = contenido;
		this.cbo = coberturas;
	}



	public EstructuraJsonModel procesar() {
		
		 int donde = 0;
		 int index = 0;
		 int inicio = 0;
		 int inicioaux = 0;
		 int fin = 0;
		 String texto = "";
		 String subtxt = "";
		 String newcontenido = "";
		 String[] arrNewContenido;
	

		contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());
		contenido = contenido.replace("IMPORTE TOTAL.", "IMPORTE TOTAL").replace("RREENNUUEEVVAA", "RENUEVA")
				.replace("MEsutnaidciop i:o:", "Municipio:").replace("Expedición.", "Expedición")
				.replace("Servic i o :", "Servicio:")
				.replace("Dom i c il i o ", "Domicilio");

		try {
			// cia
			modelo.setCia(29);

			// tipo
			modelo.setTipo(1);

			// ramo
			modelo.setRamo("Autos");

			// fecha_emision
			inicio = contenido.indexOf("IMPORTE TOTAL");
			fin = contenido.lastIndexOf("www.qualitas.com.mx");
			if (inicio > -1 && fin > inicio) {
				newcontenido = contenido.substring(inicio, fin);
				for (String x : newcontenido.split("\r\n")) {

					if (x.contains(" DE ")) {

						if (x.split(" DE ").length == 3) {

							if (x.contains("A ")) {

								x = x.split("A ")[1];
								x = x.replace(" DE ", "-");
								if (x.split("-").length > 2) {
									modelo.setFechaEmision(fn.formatDate(x, ConstantsValue.FORMATO_FECHA));
								}

							}
						}
					}
				}
			}

			// poliza
			// endoso
			// inciso
			inicio = contenido.lastIndexOf("ENDOSO###INCISO");
			fin = contenido.lastIndexOf("INFORMACIÓN DEL ASEGURADO");

			if (inicio > 0 && fin > 0 && inicio < fin) {
				newcontenido = contenido.substring(inicio, fin).replace("@@@", "").replace("\r", "");
				arrNewContenido = newcontenido.split("\n");
				for (int i = 0; i < arrNewContenido.length; i++) {
System.out.println(newcontenido.split("\n")[i].contains("AUTOMÓVILES") && modelo.getPoliza().length()==0);
					if (newcontenido.split("\n")[i].contains("AUTOMÓVILES") && modelo.getPoliza().length()==0) {
						if ((i + 1) == arrNewContenido.length) {
							
							modelo.setPoliza(arrNewContenido[i].split("###")[1]);
							modelo.setEndoso(arrNewContenido[i].split("###")[2]);
							if (fn.isNumeric(arrNewContenido[i].split("###")[3].trim())) {
								modelo.setInciso(Integer.parseInt(arrNewContenido[i].split("###")[3].trim()));
							}

						} else {
							modelo.setPoliza(arrNewContenido[i + 1].split("###")[0]);
							modelo.setEndoso(arrNewContenido[i + 1].split("###")[1]);
							if (fn.isNumeric(arrNewContenido[i + 1].split("###")[2].trim())) {
								modelo.setInciso(
										Integer.parseInt(newcontenido.split("\n")[i + 1].split("###")[2].trim()));
							}

						}

					} else {
						if (newcontenido.split("\n")[i].contains("ENDOSO") && newcontenido.split("\n")[i].contains("INCISO")) {
							if(arrNewContenido[arrNewContenido.length-1].split("###")[0].length()  < 4){
								
								if(arrNewContenido[arrNewContenido.length-1].split("###")[0].contains("AUTOMÓVILES") ) {
									modelo.setPoliza(arrNewContenido[arrNewContenido.length-1].split("###")[1]);
									modelo.setEndoso(arrNewContenido[arrNewContenido.length-1].split("###")[2]);
									if (fn.isNumeric(arrNewContenido[arrNewContenido.length-1].split("###")[3].trim())) {
										modelo.setInciso(
												Integer.parseInt(arrNewContenido[arrNewContenido.length-1].split("###")[2].trim()));
									}
									
								}else {
									modelo.setPoliza(arrNewContenido[arrNewContenido.length-1].split("###")[0]);
									modelo.setEndoso(arrNewContenido[arrNewContenido.length-1].split("###")[1]);
									if (fn.isNumeric(arrNewContenido[arrNewContenido.length-1].split("###")[2].trim())) {
										modelo.setInciso(
												Integer.parseInt(arrNewContenido[arrNewContenido.length-1].split("###")[2].trim()));
									}
								}
							}
							
							
						}

					}

				}
			}

			
			
			
			
			// cte_nombre
			inicio = contenido.lastIndexOf("DEL ASEGURADO");
			fin = contenido.lastIndexOf("Domicilio");
			

			if (inicio > -1 && fin > inicio) {
				newcontenido = contenido.substring(inicio + 13, fin);
				if (newcontenido.contains("RENUEVA")) {
					modelo.setCteNombre(newcontenido.split("RENUEVA")[0].replace("@@@", "").replace("###", "").trim());
				} else {
					if (newcontenido.split("\r\n")[1].length() > 0) {
						modelo.setCteNombre(newcontenido.split("\r\n")[1].replace("@@@", "").replace("###", "").trim());
					}
				}
				if (newcontenido.contains("RENUEVA")) {
					modelo.setCteNomina(newcontenido.split("RENUEVA")[0].replace("@@@", ""));
				}
			}

			// cte_direccion
			inicio = contenido.lastIndexOf("Domicilio:");
			fin = contenido.lastIndexOf("DESCRIPCIÓN DEL VEHÍCULO");
			if (inicio > -1 && fin > inicio) {
				newcontenido = contenido.substring(inicio, fin).trim();
				// calle
				inicio = newcontenido.indexOf("Domicilio:");
				inicioaux = newcontenido.indexOf("Número:");
				if (inicioaux == -1) {
					inicioaux = newcontenido.indexOf("No. EXT");
				}
				if (inicio > -1 && inicioaux > -1) {
					texto = newcontenido.substring(inicio + 10, inicioaux).replace("###", "").trim();
					/**
					 * *********************************
					 */
					// numero exterior
					subtxt = newcontenido.split("\r\n")[0];
					inicio = subtxt.indexOf("Número:");
					index = 7;
					if (inicio == -1) {
						inicio = subtxt.indexOf("No. EXT.");
						index = 8;
					}
					if (inicio == -1) {
						inicio = subtxt.indexOf("No. EXT");
						index = 7;
					}
					fin = subtxt.indexOf("Interior:");
					if (fin == -1) {
						fin = subtxt.indexOf("No. INT.");
					}
					if (fin == -1) {
						fin = subtxt.indexOf("Colonia:");
					}
					if (fin == -1) {
						fin = subtxt.indexOf("R.F.C.");
					}
					if (fin > -1) {
						texto += ", " + subtxt.substring(inicio + index, fin).replace("###", "").trim();
					}

					// numero interior
					inicio = subtxt.indexOf("Interior:");
					index = 9;
					if (inicio == -1) {
						inicio = subtxt.indexOf("No. INT.");
						index = 8;
					}
					if (inicio == -1) {
						inicio = subtxt.indexOf("Colonia:");
						index = 8;
					}
					if (inicio == -1) {
						inicio = subtxt.indexOf("R.F.C.");
						index = 6;
					}
					fin = subtxt.indexOf("R.F.C.");
					if (fin == -1) {
						fin = subtxt.indexOf("COL.");
					}
					if (inicio > -1 && fin > inicio) {
						if (subtxt.substring(inicio + index, fin).replace("###", "").trim().length() > 0) {
							texto += ", "
									+ subtxt.substring(inicio + index, fin).split("\r\n")[0].replace("###", "").trim();
						}
					}

					// colonia
					inicio = newcontenido.indexOf("Colonia:");
					index = 8;
					if (inicio == -1) {
						inicio = newcontenido.indexOf("COL.");
						index = 4;
					}
					if (inicio > -1) {
						texto += ", " + newcontenido.substring(inicio + index, newcontenido.indexOf("\r\n", inicio))
								.replace("###", "").trim();
					}

					// municipio
					for (String x : newcontenido.split("\r\n")) {
						if (x.contains("Municipio:")) {
							inicio = x.indexOf("Municipio:");
							index = 10;
							fin = x.indexOf("Estado:") > -1 ? x.indexOf("Estado:") : x.indexOf("Colonia") ;
							if (inicio > -1 && fin > inicio) {
								texto += ", " + x.substring(inicio + index, fin).replace("###", "").trim();
							}
						}
					}

					// estado
					inicio = newcontenido.indexOf("Estado:");
					index = 7;
					if (inicio > -1) {
						subtxt = newcontenido.substring(inicio + index, newcontenido.indexOf("\r\n", inicio + index));
						if (subtxt.contains("Colonia")) {
							texto += ", " + subtxt.split("Colonia")[0].replace("###", "").trim();
						} else if (subtxt.contains("R.F.C.")) {
							texto += ", " + subtxt.split("R.F.C.")[0].replace("###", "").trim();
						}else {
							texto += ", "+subtxt.trim();
						}
					}

					inicio = newcontenido.indexOf("C.P.");
					fin = newcontenido.indexOf("RFC");
					if (inicio > -1 && fin > inicio) {
						subtxt = fn.gatos(newcontenido.substring(inicio + 4, fin));
						if (subtxt.split("###").length == 2 && subtxt.split("\r\n").length == 1) {
							texto += ", " + subtxt.split("###")[1].trim();
						}
					}

				}
			}
			modelo.setCteDireccion(texto.trim());

			// rfc
			inicio = contenido.indexOf("R.F.C.:");
			index = 7;
			if (inicio == -1) {
				inicio = contenido.lastIndexOf("RFC");
				index = 3;
			}
			if (inicio > 0) {

			} else {
				inicio = contenido.lastIndexOf("R.F.C.");
				index = 6;
			}

			if (inicio > -1) {
				newcontenido = contenido.substring(inicio + index, contenido.indexOf("\r\n", inicio + index))
						.replace("###", "").replace("-", "").trim();
				modelo.setRfc(newcontenido);
			}
		
			if (modelo.getRfc().length()  == 0) {
				inicio = contenido.indexOf("R.F.C:");
				fin = contenido.indexOf("VEHÍCULO ASEGURADO");
				if (inicio > 0 && fin > 0 && inicio < fin) {
					newcontenido = contenido.substring(inicio, fin).replace("@@@", "").replace("\r", "");

					for (int i = 0; i < newcontenido.split("\n").length; i++) {						
						if(newcontenido.split("\n")[i].split("R.F.C:").length>1) {
							modelo.setRfc(newcontenido.split("\n")[i].split("R.F.C:")[1].replace("###", "").replace("-", "").trim());
						}
					
					}
             
				}

			}

			// moneda
			inicio = contenido.indexOf("MONEDA");
			if (inicio > -1) {
				newcontenido = contenido.substring(inicio + 6, contenido.indexOf("\r\n", inicio + 6)).replace("###", "")
						.trim();
				modelo.setMoneda(fn.moneda(newcontenido));
			}

			// prima_neta
			inicio = contenido.indexOf("Prima Neta");
			if (inicio > -1) {
				newcontenido = contenido.substring(inicio + 10, contenido.indexOf("\r\n", inicio + 10))
						.replace("###", "").replace(",", "").trim();
				if (fn.isNumeric(newcontenido)) {
					modelo.setPrimaneta(fn.castBigDecimal(newcontenido));
				}
			}

			// recargo
			inicio = contenido.indexOf("Tasa Financiamiento");
			if (inicio > -1) {
				newcontenido = contenido.substring(inicio + 19, contenido.indexOf("\r\n", inicio + 19))
						.replace("###", "").replace(",", "").trim();
				if (fn.isNumeric(newcontenido)) {
					modelo.setRecargo(fn.castBigDecimal(newcontenido));
				}
			}

			// derecho
			inicio = contenido.indexOf("por Expedición");// Gastos por Expedición.
			if (inicio > -1) {
				newcontenido = contenido.substring(inicio + 14, contenido.indexOf("\r\n", inicio + 14))
						.replace("###", "").replace(",", "").trim();
				if (newcontenido.contains("Pa")) {
					newcontenido = newcontenido.split("Pa")[0].trim();
				}
				if (fn.isNumeric(newcontenido)) {
					modelo.setDerecho(fn.castBigDecimal(newcontenido));
				}
			}

			// iva
			inicio = contenido.indexOf("I.V.A.");
			if (inicio > -1) {
				newcontenido = contenido.substring(inicio, contenido.indexOf("\r\n", inicio));
				if (newcontenido.contains("%")) {
					newcontenido = newcontenido.split("%")[1].replace("###", "").replace(",", "").trim();
					if (newcontenido.contains("Bitli")) {
						newcontenido = newcontenido.split("Bitli")[0].trim();
					}
					if (fn.isNumeric(newcontenido)) {
						modelo.setIva(fn.castBigDecimal(newcontenido));
					}
				}
			}

			// prima_total
			inicio = contenido.indexOf("IMPORTE TOTAL");
			if (inicio > -1) {
				newcontenido = contenido.substring(inicio + 13, contenido.indexOf("\r\n", inicio + 13))
						.replace("###", "").replace(",", "").trim();
				if (fn.isNumeric(newcontenido)) {
					modelo.setPrimaTotal(fn.castBigDecimal(newcontenido));
				}
			}

			// agente
			// cve_agente
			donde = 0;
			donde = fn.recorreContenido(contenido, "Agente");
			if (donde > 0) {
				for (String dato : contenido.split("@@@")[donde].split("\r\n")) {
					if (dato.contains("Agente")) {
						switch (dato.split("###").length) {
						case 1:
							if (dato.contains("Agente:")) {
								modelo.setCveAgente(dato.split("te:")[1].trim().split(" ")[0].trim());
							}
							if (modelo.getCveAgente().length() > 0) {
								modelo.setAgente(dato.split(modelo.getCveAgente())[1].trim());
							}
							break;
						case 2:
							if (dato.split("###")[0].trim().equals("Agente:")) {
								modelo.setCveAgente(dato.split("###")[1].trim().split(" ")[0].trim());
								modelo.setAgente(dato.split("###")[1].trim().split(modelo.getCveAgente())[1].trim());
							}
							break;
						case 3:
							modelo.setCveAgente(dato.split("###")[1].trim());
							modelo.setAgente(dato.split("###")[2].trim());
							break;
						default:
							break;
						}
					}
				}
			}
			if (modelo.getAgente().contains("ASEASOONR")) {
				donde = 0;
				donde = fn.recorreContenido(contenido, "IMPORTE TOTAL");
				if (donde > 0) {
					for (String dato : contenido.split("@@@")[donde].split("\r\n")) {
						if (dato.contains("Agente")) {
							if (dato.split("###").length == 3) {
								if (dato.split("###")[2].contains("IMPORTE")) {
									modelo.setAgente(dato.split("###")[1].trim());
								}
							}
						} else if (dato.contains("Clave")) {
							if (dato.split("###").length == 4  && dato.split("###")[2].contains("Teléfono:")) {
								
									modelo.setCveAgente(dato.split("###")[1].trim());
								
							}
						}
					}
				}
			}
			// plan
			inicio = contenido.indexOf("PLAN:");
			if (inicio > -1) {
				newcontenido = contenido.substring(inicio + 5, contenido.indexOf("\r\n", inicio + 5)).replace("###", "")
						.trim();
				modelo.setPlan(newcontenido);
			}

			// renovacion
			inicio = contenido.indexOf("RENUEVA A");
			if (inicio > -1) {
				newcontenido = contenido.substring(inicio + 10, contenido.indexOf("\r\n", inicio + 10))
						.replace("###", "").replace("-", "").replace(":", "").trim();
				modelo.setRenovacion(newcontenido);
			}

			// forma_pago
			inicio = contenido.indexOf("Tasa Financiamiento");

			if (inicio > -1) {
				newcontenido = contenido.substring(inicio, inicio + 200).split("\r\n")[1];
			

				if (newcontenido.contains("Gastos por")) {
					newcontenido = newcontenido.split("Gastos por")[0].replace("###", "").trim();
					if (newcontenido.contains("Forma de:")) {
						newcontenido = newcontenido.split("Forma de:")[1];
					}
					modelo.setFormaPago(fn.formaPago(newcontenido));
				} else if (newcontenido.contains("Forma de:")) {

					newcontenido = newcontenido.split("Forma de:")[1].replace("###", "").trim();

					if (newcontenido.contains("Primer pago")) {
						newcontenido = contenido.substring(inicio, inicio + 300).split("\r\n")[2];
						if (newcontenido.contains("Gastos por Expedición")) {
							modelo.setFormaPago(fn.formaPago(newcontenido.split("###")[0].trim()));
						}

					} else {
						modelo.setFormaPago(fn.formaPago(newcontenido));
					}

				} else if (newcontenido.contains("Forma de Pago:")) {
					inicio = contenido.indexOf("Forma de Pago:");
					newcontenido = contenido.substring(inicio, inicio + 600).split("\r\n")[1];
					newcontenido = newcontenido.split("Forma de Pago:")[0].split("###")[0].trim();

					modelo.setFormaPago(fn.formaPago(newcontenido));
				}
			}

			
			/**
			 * **********para cuando trae para primer recibo y subsecuente*************
			 */
			fin = contenido.indexOf("Pagos Subsecuentes");
			if (fin > -1 && modelo.getFormaPago() == 0) {
				index = contenido.substring(fin - 150, fin).split("\r\n").length - 1;
				newcontenido = contenido.substring(fin - 150, fin).split("\r\n")[index].trim().split(" ")[0]
						.replace("###", "").replace("@@@", "").trim();
				modelo.setFormaPago(fn.formaPago(newcontenido));
			}
			


			if (modelo.getFormaPago() == 0) {
				inicio = contenido.lastIndexOf("Pago:");

				if (inicio > -1) {
					newcontenido = contenido.substring(inicio + 5, inicio + 150).split("\r\n")[0].replace("###", "")
							.trim();
			
			
					if (newcontenido.contains("12")) {
						modelo.setFormaPago(fn.formaPago(newcontenido.split("12")[0].trim()));
					} else {
						if(newcontenido.contains("Pagos")) {
							modelo.setFormaPago(fn.formaPago(newcontenido.split("Pagos")[0]));
						}else {
							
							if(newcontenido.contains("Gastos")) {
								modelo.setFormaPago(fn.formaPago(newcontenido.split("Gastos")[0]));
							}else {
								modelo.setFormaPago(fn.formaPago(newcontenido));
							}
							
						}
				
					}

				}
			}
			if (modelo.getFormaPago() == 0) {
				inicio = contenido.indexOf("MONEDA");
				fin = contenido.indexOf("IMPORTE TOTAL");
				if(inicio > -1 && fin > -1) {
					newcontenido = contenido.substring(inicio,fin).replace("@@@", "").replace("\r", "");
			
			  modelo.setFormaPago(fn.formaPagoSring(newcontenido));
					
				}
				
				
			}

			// primer_prima_total
			inicio = contenido.indexOf("Pago Inicial:");
			index = 13;
			if (inicio == -1) {
				inicio = contenido.indexOf("Primer pago");
				index = 11;
			}
			if (inicio > -1) {
				newcontenido = contenido.substring(inicio + index, contenido.indexOf("\r\n", inicio + index))
						.replace("###", "");
				if (newcontenido.contains("Tasa Financiamiento")) {
					newcontenido = newcontenido.split("Tasa Financiamiento")[0].trim();
				}
				if (fn.isNumeric(newcontenido)) {
					modelo.setPrimerPrimatotal(fn.castBigDecimal(fn.castDouble(newcontenido)));
				}
			}

			// sub_prima_total
			inicio = contenido.indexOf("Pagos Subsecuentes:");
			index = 19;
			if (inicio == -1) {
				inicio = contenido.indexOf("Pago(s) Subsecuente(s)");
				index = 22;
			}
			if (inicio > -1) {
				newcontenido = contenido.substring(inicio + index, contenido.indexOf("\r\n", inicio + 19))
						.replace("###", "");
				if (newcontenido.contains("Gastos por")) {
					newcontenido = newcontenido.split("Gastos por")[0].trim();
				}
				if (fn.isNumeric(newcontenido)) {
					modelo.setSubPrimatotal(fn.castBigDecimal(fn.castDouble(newcontenido)));
				}
			}
			/**
			 * ********************************************
			 */

			// vigencia_a
			inicio = contenido.lastIndexOf("Hasta las");
			if (inicio > -1) {
				newcontenido = contenido.substring(inicio, contenido.indexOf("\r\n", inicio)).replace("del:", "del");
				if (newcontenido.contains("Plazo")) {
					newcontenido = newcontenido.split("Plazo")[0].split("del")[1].replace("###", "").trim();
					if (newcontenido.length() == 11) {
						modelo.setVigenciaA(fn.formatDate(newcontenido, ConstantsValue.FORMATO_FECHA));
					}
				}else {
					newcontenido = newcontenido.split("###").length>2 ?newcontenido.split("###")[1] : "";
					modelo.setVigenciaA(fn.formatDate(newcontenido, ConstantsValue.FORMATO_FECHA));
				}
			}
			// vigencia_de
			inicio = contenido.lastIndexOf("Desde las");
			if (inicio > -1) {
				newcontenido = contenido.substring(inicio + 9, contenido.indexOf("\r\n", inicio + 9))
						.replace("del:", "del").replace("Servic  i o  :", "Servicio:").replace("Servic ###io:", "Servicio:");
	
				if (newcontenido.contains("Servicio")) {
					newcontenido = fn.gatos(newcontenido.split("Servicio")[0].split("del")[1].trim());
	
					if (newcontenido.split("###").length == 2 ||( newcontenido.split("###").length == 1 && newcontenido.contains("-") )) {
						newcontenido = fn.formatDate(newcontenido.split("###")[0].trim(), ConstantsValue.FORMATO_FECHA);
						if (newcontenido.length() == 10) {
							modelo.setVigenciaDe(newcontenido);
						}
					}
				} else {
					if (newcontenido.contains("Hasta las")) {
						newcontenido = newcontenido.split("Hasta las")[0].split("del")[1].replace("###", "").trim();
						modelo.setVigenciaDe(fn.formatDate(newcontenido, ConstantsValue.FORMATO_FECHA));
						if (modelo.getVigenciaA().length() > 0) {
						} else {

							newcontenido = contenido.substring(inicio + 9, contenido.indexOf("\r\n", inicio + 9))
									.replace("del:", "del").replace("Servic  i o  :", "Servicio:");
							modelo.setVigenciaA(
									fn.formatDate(newcontenido.split("\r\n")[0].split("Hasta las")[1].split("del")[1]
											.replace("###", "").trim(), ConstantsValue.FORMATO_FECHA));
						}
					}else if(newcontenido.contains("del") && newcontenido.split("###").length>1) {
						modelo.setVigenciaDe(fn.formatDate(newcontenido.split("###")[1].trim(),ConstantsValue.FORMATO_FECHA));
					}
				}
			}
			// cp
			inicio = contenido.lastIndexOf("C.P");
			index = 3;
			if (inicio > -1) {
				newcontenido = contenido.substring(inicio + index, contenido.indexOf("\r\n", inicio + index))
						.replace(".:", "").replace(".", "").replace(":", "");
				if (newcontenido.contains("Municipio")) {
					newcontenido = newcontenido.split("Municipio")[0].replace("###", "").trim();
					modelo.setCp(newcontenido);
				} else if (newcontenido.split("###").length > 2) {
					newcontenido = fn.gatos(newcontenido);
					if (fn.isNumeric(newcontenido.split("###")[0].trim())) {
						modelo.setCp(newcontenido.split("###")[0].trim());
					}
				} else {
					modelo.setCp(newcontenido);
				}
			}

			// clave
			inicio = contenido.indexOf("VEHÍCULO ASEGURADO\r\n");
			index = 20;
			if (inicio > -1) {
				newcontenido = contenido.substring(inicio + index, contenido.indexOf("\r\n", inicio + index))
						.replace("@@@", "");
				if (newcontenido.contains("(")) {
					newcontenido = newcontenido.replace("(", "&&&");
					newcontenido = newcontenido.split("&&&")[0].replace("###", "").trim();
					modelo.setClave(newcontenido);
				} else if (newcontenido.split("###").length == 2) {
					modelo.setClave(newcontenido.split("###")[0].trim());
				}
			}

			// marca
			// descripcion
			inicio = contenido.indexOf("VEHÍCULO ASEGURADO");
			index = 20;
			if (inicio > -1) {
				newcontenido = contenido.substring(inicio + index, contenido.indexOf("\r\n", inicio + index));
				if (newcontenido.contains("(")) {
					newcontenido = newcontenido.replace(")", "&&&");
					newcontenido = newcontenido.split("&&&")[1].replace("###", "").trim();
					modelo.setMarca(newcontenido.split(" ")[0].trim());
					modelo.setDescripcion(newcontenido);
				} else if (newcontenido.split("###").length == 2) {
					newcontenido = newcontenido.split("###")[1].trim();
					modelo.setMarca(newcontenido.split(" ")[0].trim());
					modelo.setDescripcion(newcontenido);
				}else if( !newcontenido.contains("#") && newcontenido.split(" ").length > 2) {
					modelo.setMarca(newcontenido.split(" ")[1].trim());
					modelo.setDescripcion(newcontenido.split(modelo.getMarca())[1].replace("\r", "").trim());
				}
			}

			// modelo
			inicio = contenido.indexOf("Modelo:");
			if (inicio > -1) {
				newcontenido = contenido.substring(inicio + 7, contenido.indexOf("\r\n", inicio + 7));
				if (newcontenido.contains("Color")) {
					newcontenido = newcontenido.split("Color")[0].replace("###", "").trim();
					if (fn.isNumeric(newcontenido)) {
						modelo.setModelo(Integer.parseInt(newcontenido));
					}
				}
			}

			// serie
			inicio = contenido.indexOf("Serie:");
			fin = contenido.indexOf("Motor:");

			if (inicio > 0 && fin > 0 && inicio < fin) {
				newcontenido = contenido.substring(inicio, fin).split("Serie:")[1].replace("###", "").trim();
				modelo.setSerie(newcontenido);

			}

			// motor
			inicio = contenido.indexOf("Motor:");
			if (inicio > -1) {
				newcontenido = contenido.substring(inicio + 6, contenido.indexOf("\r\n", inicio + 6));
				if (newcontenido.contains("Placas")) {
					if (newcontenido.contains("REPUVE")) {
						newcontenido = newcontenido.split("REPUVE")[0];
					}
					newcontenido = newcontenido.split("Placas")[0].replace("###", "").trim();
					modelo.setMotor(newcontenido);
				}
			}

			// placas
			inicio = contenido.indexOf("Placas:");
			if (inicio > -1) {
				newcontenido = contenido.substring(inicio + 7, contenido.indexOf("\r\n", inicio + 7)).replace("###", "")
						.replace("-", "").replace(" ", "").trim();
				modelo.setPlacas(newcontenido);
			}

			// coberturas
			inicio = contenido.indexOf("PRIMAS");
			if (inicio  == -1) {
				inicio = contenido.indexOf("PRIMA");
			}
			fin = contenido.indexOf("MONEDA");
	
			if (inicio > -1 && fin > inicio) {
				newcontenido = contenido.substring(inicio, fin).replace("@@@", "").trim();
		
				if (newcontenido.contains("La Unidad de Medida")) {
					newcontenido = newcontenido.split("La Unidad de Medida")[0].trim();
				}
				if (newcontenido.contains("Servicios de Asistencia Vial")) {
					newcontenido = newcontenido.split("Servicios de Asistencia Vial")[0].trim();
				}

				List<EstructuraCoberturasModel> coberturas = new ArrayList<>();

				for (String x : newcontenido.split("\r\n")) {
					EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();
					switch (x.split("###").length) {
					case 4:
						cobertura.setNombre(x.split("###")[0].trim());
						cobertura.setSa(fn.eliminaSpacios(x.split("###")[1].trim(), ' ', ""));
						cobertura.setDeducible(x.split("###")[2].trim());
						coberturas.add(cobertura);
						break;
					case 3:
						cobertura.setNombre(x.split("###")[0].trim());
						cobertura.setSa(fn.eliminaSpacios(x.split("###")[1].trim(), ' ', ""));
						coberturas.add(cobertura);
						break;
					default:
						break;
					}

				}

				modelo.setCoberturas(coberturas);
			}

			if(modelo.getCoberturas().isEmpty()) {
				newcontenido ="";
	
				inicio = cbo.indexOf("PRIMAS");
				if (inicio  == -1) {
					inicio = cbo.indexOf("PRIMA");
				}
				fin = cbo.indexOf("MONEDA");
		
			  if(cbo.indexOf("COBERTURAS CONTRATADAS") > -1 && cbo.indexOf("SUMA ASEGURADA") > -1 && cbo.indexOf("DEDUCIBLE") > -1) {
				 inicio = cbo.lastIndexOf("COBERTURAS CONTRATADAS");
				 fin = cbo.indexOf("Para RC en el extranjero");				
			  }
				
				if (inicio > -1 && fin > inicio) {
		
					newcontenido = cbo.substring(inicio, fin).replace("\u00A0","").replace("@@@", "").trim();				
					
					if (newcontenido.contains("Asistencia Vial Quálitas")) {
						newcontenido = newcontenido.split("Asistencia Vial Quálitas")[0].trim();
					}
					List<EstructuraCoberturasModel> coberturas = new ArrayList<>();

					for (String x : newcontenido.split("\n")) {
						EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();
	
						switch (x.split("###").length) {
						case 4:
							cobertura.setNombre(x.split("###")[0].trim());
							cobertura.setSa(fn.eliminaSpacios(x.split("###")[1].trim(), ' ', ""));
							cobertura.setDeducible(x.split("###")[2].trim());
							coberturas.add(cobertura);
							break;
						case 3:
							cobertura.setNombre(x.split("###")[0].trim());
							cobertura.setSa(fn.eliminaSpacios(x.split("###")[1].trim(), ' ', ""));
							coberturas.add(cobertura);
							break;
						default:
							break;
						}

					}

					modelo.setCoberturas(coberturas);
				}
				
			}
			
			
			List<EstructuraRecibosModel> recibos = new ArrayList<>();

			EstructuraRecibosModel recibo = new EstructuraRecibosModel();

			switch (modelo.getFormaPago()) {
			case 1:
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
				break;
			case 2:
				break;

			case 3:
			case 4:
				break;
			default:
				break;

			}
			modelo.setRecibos(recibos);
			return modelo;
		} catch (Exception ex) {
			modelo.setError(
					qualitasAutosModel.this.getClass().getTypeName() + " | " + ex.getMessage() + " | " + ex.getCause());
			return modelo;
		}
	}

}
