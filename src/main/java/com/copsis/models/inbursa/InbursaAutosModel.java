package com.copsis.models.inbursa;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.copsis.constants.ConstantsValue;
import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraCoberturasModel;
import com.copsis.models.EstructuraJsonModel;

public class InbursaAutosModel {
	// Clases
	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
	// Varaibles
	private String contenido = "";

	private String recibosText = "";
	private static final String POLIZA_REGEX = "(PÓLIZA \\s*(\\w{5} \\w{8}))";

	public InbursaAutosModel(String contenido, String recibos) {
		this.contenido = contenido;
		this.recibosText = recibos;
	}

	public EstructuraJsonModel procesar() {
		
		StringBuilder resultado = new StringBuilder();

		String newcontenido = "";
		String textoAux = "";
		int inicio = 0;
		int fin = 0;

		contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());
		contenido = contenido.replace("IVA", ConstantsValue.IVA2).replace("ACTUAL", "RENOVACION");

		try {
			// tipo
			modelo.setTipo(1);

			// cia
			modelo.setCia(38);

			// Datos del Contratante
			inicio = contenido.indexOf("PÓLIZA DE SEGUROS");
			fin = contenido.indexOf(ConstantsValue.COBERTURAS_CONTRATADAS);
			if (inicio > 0 && fin > 0 && inicio < fin) {
				newcontenido = contenido.substring(inicio, fin).replace("\r", "").replace("@", "")
						.replace("las 12:00 horas", "");
				
				obtenerPolizaRegex();
				
				for (int i = 0; i < newcontenido.split("\n").length; i++) {

					if (newcontenido.split("\n")[i].contains("DATOS DEL CONTRATANTE")) {
						if (newcontenido.split("\n")[i + 1].contains("NOMBRE:")) {
							modelo.setCteNombre(newcontenido.split("\n")[i + 2].split("###")[0]);
						} else {
							modelo.setCteNombre(newcontenido.split("\n")[i + 1].split("###")[0]);
						}
					}
					
					if(modelo.getPoliza().isEmpty()) {
						obtenerPolizaOtraUbicacion(newcontenido.split("\n"), i);
					}
					// proceso direccion
					if (newcontenido.split("\n")[i].contains("R.F.C.")) {
						resultado.append(newcontenido.split("\n")[i].split("R.F.C.")[0]);
					}
					if (newcontenido.split("\n")[i].contains(ConstantsValue.PRIMA_MAYUS)
							&& newcontenido.split("\n")[i].contains("NETA")
							&& newcontenido.split("\n")[i].contains("AGRUPACIÓN")) {
						// debe entra para no colocar nada
					} else if (newcontenido.split("\n")[i].contains(ConstantsValue.PRIMA_MAYUS)
							&& newcontenido.split("\n")[i].contains("NETA")) {
						resultado.append(" ").append(newcontenido.split("\n")[i].split(ConstantsValue.PRIMA_MAYUS)[0]).append(" ").append(newcontenido.split("\n")[i + 1]);
						modelo.setCteDireccion(resultado.toString().replace("###", "").trim());
					}

					if (newcontenido.split("\n")[i].contains("DIRECCIÓN:")) {
						String a = "";
						String b = "";
						String c = "";
						if (newcontenido.split("\n")[i + 2].trim().contains(".00")) {
							a = newcontenido.split("\n")[i + 1].split("SUMA")[0].trim();
						} else if(!newcontenido.split("\n")[i + 1].isBlank()){
			
							a = newcontenido.split("\n")[i + 1].trim() +" "+ newcontenido.split("\n")[i + 2].trim();
						}else {
							a =  newcontenido.split("\n")[i + 2].trim();
						}
						
						if (newcontenido.split("\n")[i + 3].trim().contains(ConstantsValue.RFC) && b.isEmpty()) {
							b = newcontenido.split("\n")[i + 3].split(ConstantsValue.RFC)[0].trim();
						} else {
							b = newcontenido.split("\n")[i + 3].trim().isBlank()? newcontenido.split("\n")[i + 4].trim() :newcontenido.split("\n")[i + 3].trim();
						}
						
						if (newcontenido.split("\n")[i + 6].trim().contains(".00")) {
							c = newcontenido.split("\n")[i + 4].split("C.P.")[0].trim();
						} else {
							c = newcontenido.split("\n")[i + 6].trim();
						}

						String x = a + " " + b + " " + c;
						modelo.setCteDireccion(x.replace("###", ""));
					}

					if (newcontenido.split("\n")[i].contains("C.P.")) {
						if (newcontenido.split("\n")[i].split("C.P.")[1].split("###").length > 0) {
							modelo.setCp(newcontenido.split("\n")[i].split("C.P.")[1].split("###")[0].trim());
						} else {
							modelo.setCp(newcontenido.split("\n")[i].split("C.P.")[1].trim());
						}
						
						//rfc 
						if(newcontenido.split("\n")[i-1].contains("R.F.C") && newcontenido.split("\n")[i].split("###").length>2) {
							String aux = newcontenido.split("\n")[i].split("###")[newcontenido.split("\n")[i].split("###").length-2];
							if(aux.trim().length()>11) {
								modelo.setRfc(aux.trim());
							}
						}
					}
					
					
					if (newcontenido.split("\n")[i].contains("MONEDA")) {
						modelo.setMoneda(fn.buscaMonedaEnTexto(newcontenido.split("\n")[i + 1]));						
					}
					// primas
					if (newcontenido.split("\n")[i].contains(ConstantsValue.PRIMA_NETA_MAYUS)
							&& newcontenido.split("\n")[i].contains("AGRUPACIÓN")) {
						if(newcontenido.split("\n")[i].contains("NOMBRE") && newcontenido.split("\n")[i + 2].trim().split("###").length>1) {
							modelo.setPrimaneta(
									fn.castBigDecimal(fn.castDouble(newcontenido.split("\n")[i + 2].split("###")[1].trim())));
						}else {
							modelo.setPrimaneta(
									fn.castBigDecimal(fn.castDouble(newcontenido.split("\n")[i + 1].split("###")[newcontenido.split("\n")[i + 1].split("###").length-1].trim())));
						}

					} else if (newcontenido.split("\n")[i].contains(ConstantsValue.PRIMA_NETA_MAYUS)) {
						modelo.setPrimaneta(fn.castBigDecimal(fn.castDouble(
								fn.extraerNumeros(newcontenido.split("\n")[i].split(ConstantsValue.PRIMA_NETA_MAYUS)[1]
										.replace("###", "").substring(0, 13)))));
					}
					if (newcontenido.split("\n")[i].contains(ConstantsValue.FINANCIAMIENTO_MAYUS)
							&& newcontenido.split("\n")[i].contains(ConstantsValue.RFC)) {
						if(!fn.extraerNumeros(newcontenido.split("\n")[i+1]).isEmpty()) {
							modelo.setRecargo(fn.castBigDecimal(
									fn.castDouble(newcontenido.split("\n")[i + 1].split("###")[newcontenido.split("\n")[i + 1].split("###").length-1].replace("###", ""))));
						}else {
							modelo.setRecargo(fn.castBigDecimal(
									fn.castDouble(newcontenido.split("\n")[i + 2].split("###")[newcontenido.split("\n")[i + 2].split("###").length-1].replace("###", ""))));
						}

					} else if (newcontenido.split("\n")[i].contains(ConstantsValue.FINANCIAMIENTO_MAYUS)) {
						modelo.setRecargo(fn.castBigDecimal(
								fn.castDouble(newcontenido.split("\n")[i].split(ConstantsValue.FINANCIAMIENTO_MAYUS)[1]
										.replace("###", ""))));
					}
					if (newcontenido.split("\n")[i].contains(ConstantsValue.EXPEDICION_MAYUS)
							&& newcontenido.split("\n")[i].contains("MONEDA:")) {
						modelo.setDerecho(fn.castBigDecimal(
								fn.castDouble(fn.extraerNumeros(newcontenido.split("\n")[i + 1]))));
					} else if (newcontenido.split("\n")[i].contains(ConstantsValue.EXPEDICION_MAYUS)) {
						modelo.setDerecho(fn.castBigDecimal(
								fn.castDouble(newcontenido.split("\n")[i].split(ConstantsValue.EXPEDICION_MAYUS)[1]
										.replace("###", ""))));
					}
					if (newcontenido.split("\n")[i].contains(ConstantsValue.IVA2)
							&& newcontenido.split("\n")[i].contains("PAGO")) {
						modelo.setIva(fn.castBigDecimal(
								fn.castDouble(fn.extraerNumeros(newcontenido.split("\n")[i + 1].replace("###", "")))));
						obtenerFormaDePago(newcontenido.split("\n")[i + 1].trim());
						
					} else if (newcontenido.split("\n")[i].contains(ConstantsValue.IVA2)) {
						modelo.setIva(fn.castBigDecimal(fn.castDouble(
								newcontenido.split("\n")[i].split(ConstantsValue.IVA2)[1].replace("###", ""))));
					}
					if (newcontenido.split("\n")[i].contains(ConstantsValue.PRIMA_TOTAL_MAYUS)
							&& newcontenido.split("\n")[i].contains("DOCUMENTO")) {
						if(!fn.extraerNumeros(newcontenido.split("\n")[i + 1]).isEmpty()) {
							modelo.setPrimaTotal(fn.castBigDecimal(fn.castDouble(newcontenido.split("\n")[i + 1]
									.split("###")[newcontenido.split("\n")[i + 1].split("###").length - 1].replace("###",
											""))));
						}else {
							modelo.setPrimaTotal(fn.castBigDecimal(fn.castDouble(fn.extraerNumeros(newcontenido.split("\n")[i+2]))));
						}

					} else if (newcontenido.split("\n")[i].contains(ConstantsValue.PRIMA_TOTAL_MAYUS)) {
						modelo.setPrimaTotal(fn.castBigDecimal(
								fn.castDouble(newcontenido.split("\n")[i].split(ConstantsValue.PRIMA_TOTAL_MAYUS)[1]
										.replace("###", ""))));
					}

					if (newcontenido.split("\n")[i].contains(ConstantsValue.DESDE)
							&& Pattern.compile(ConstantsValue.HASTA2,Pattern.CASE_INSENSITIVE).matcher(newcontenido.split("\n")[i]).find()
							&& newcontenido.split("\n")[i].contains("-")) {
						textoAux = newcontenido.split("\n")[i].toUpperCase();
						modelo.setVigenciaDe(
								fn.formatDateMonthCadena(textoAux.split("DESDE")[1]
										.split("HASTA")[0].replace("###", "").trim()));
						modelo.setVigenciaA(fn.formatDateMonthCadena(
								textoAux.split("HASTA")[1].split("RENOVACION")[0]
										.replace("###", "").trim()));
					} else if (newcontenido.split("\n")[i].contains(ConstantsValue.DESDE)
							&& newcontenido.split("\n")[i].contains(ConstantsValue.HASTA2)) {
						modelo.setVigenciaDe(fn.formatDateMonthCadena(newcontenido.split("\n")[i + 1].split("###")[0]));
						modelo.setVigenciaA(fn.formatDateMonthCadena(newcontenido.split("\n")[i + 1].split("###")[1]));
						
						modelo.setFormaPago(fn.formaPago(newcontenido.split("\n")[i + 1].split("###")[2].trim()));
					}
					if (modelo.getClave().isEmpty()) {
						obtenerClaveYDescripcionAuto(newcontenido.split("\n")[i]);
					}
	
					if (newcontenido.split("\n")[i].contains(ConstantsValue.MODELO_MAYUS)
							&& newcontenido.split("\n")[i].contains(ConstantsValue.PLACAS_MAYUS)) {
						modelo.setModelo(fn.castInteger(
								newcontenido.split("\n")[i].split(ConstantsValue.MODELO_MAYUS)[1].split("PLACAS")[0]
										.replace("###", "").trim()));
					} else if (newcontenido.split("\n")[i].contains(ConstantsValue.MODELO_MAYUS)) {
						modelo.setModelo(
								fn.castInteger(newcontenido.split("\n")[i].split(ConstantsValue.MODELO_MAYUS)[1]
										.replace("###", "").trim()));
					}
					if (newcontenido.split("\n")[i].contains(ConstantsValue.PLACAS_MAYUS)
							&& newcontenido.split("\n")[i].split("PLACAS")[1].length() > 2) {
						modelo.setPlacas(newcontenido.split("\n")[i].split("PLACAS:")[1].replace("###", "").trim());
					}
					if (newcontenido.split("\n")[i].contains(ConstantsValue.SERIE_MAYUS)
							&& newcontenido.split("\n")[i].contains("NÚMERO")) {
						modelo.setSerie(
								newcontenido.split("\n")[i].split(ConstantsValue.SERIE_MAYUS)[1].split("NÚMERO")[0]
										.replace("###", "").trim());
					} else if (newcontenido.split("\n")[i].contains(ConstantsValue.SERIE_MAYUS)) {
						modelo.setSerie(newcontenido.split("\n")[i].split(ConstantsValue.SERIE_MAYUS)[1]
								.replace("###", "").trim());
					}
					if (newcontenido.split("\n")[i].contains("MOTOR:")
							&& newcontenido.split("\n")[i].split("MOTOR")[1].length() > 2) {

						modelo.setMotor(newcontenido.split("\n")[i].split("MOTOR:")[1].replace("###", "").trim());
					}
					if (newcontenido.split("\n")[i].contains("PROPIETARIO:")) {
						modelo.setConductor(
								newcontenido.split("\n")[i].split("PROPIETARIO:")[1].replace("###", "").trim());
					}
					if (newcontenido.split("\n")[i].contains(ConstantsValue.PRODUCTO)
							&& newcontenido.split("\n")[i].contains("PAGO")) {
						modelo.setPlan(newcontenido.split("\n")[i].split(ConstantsValue.PRODUCTO)[1].split(ConstantsValue.FORMA)[0]
								.replace("###", "").trim());
					} else if (newcontenido.split("\n")[i].contains(ConstantsValue.FORMA)) {
						modelo.setPlan(newcontenido.split("\n")[i].split(ConstantsValue.FORMA)[0].replace("###", "").replace(ConstantsValue.PRODUCTO, "").replace("@", "").trim());
					}
				}
			}

			if(!modelo.getVigenciaDe().isEmpty()) {
				modelo.setFechaEmision(modelo.getVigenciaDe());
			}
			limpiarCteDireccion();
			
			/* Agente y Cve */

			inicio = contenido.indexOf("NOMBRE DEL AGENTE");
			int renglonesARetroceder = -1;
			if (inicio > 0) {
				newcontenido = contenido.split("NOMBRE DEL AGENTE")[0];
				if (newcontenido.length() > 200) {
					newcontenido = newcontenido.substring(newcontenido.length() - 200, newcontenido.length())
							.replace("@@@", "").replace("\r", "");
					for (int j = 0; j < newcontenido.split("\n").length; j++) {

						if (newcontenido.split("\n")[j].contains("CLAVE")) {
							
							if(!fn.numTx(newcontenido.split("\n")[j - 2]).contains("-")) {
								renglonesARetroceder = 2;
							}else if(fn.isNumeric(fn.numTx(newcontenido.split("\n")[j - 3]))){
								renglonesARetroceder = 3;
							}

							modelo.setCveAgente(fn.numTx(newcontenido.split("\n")[j - renglonesARetroceder]));

							if (modelo.getCveAgente().length() > 0) {
								String a = newcontenido.split("\n")[j - 1].replace(" ", "###").split("###")[0].trim();
								if (a.contains("@")) {
									a = "";
								}
								modelo.setAgente(
										(fn.eliminaSpacios(newcontenido.split("\n")[j-renglonesARetroceder].split(modelo.getCveAgente())[1] + " " + a)));
							}
						}
					}
				}
			}

			inicio = contenido.indexOf(ConstantsValue.COBERTURAS_CONTRATADAS);
			fin = limiteFinalCoberturas();

			if (inicio > 0 && fin > 0 && inicio < fin) {
				List<EstructuraCoberturasModel> coberturas = new ArrayList<>();
				newcontenido = contenido.substring(inicio, fin).replace("\r", "").replace("@", "")
						.replace("las 12:00 horas", "");

				if(!modelo.getPoliza().isEmpty()) {
					//descarta texto que esta en el lateral izquierdo del pdf 
					newcontenido =newcontenido.replace("###"+modelo.getPoliza()+"###", "").replace(modelo.getPoliza(), "");
				}

				String[] arrContenido = newcontenido.split("\n");
				for(int i=0; i< arrContenido.length;i++) {
					arrContenido[i] = completaTextoCobertura(arrContenido, i);
				}

				for (int i = 0; i <arrContenido.length ; i++) {
					EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();

					if (!arrContenido[i].contains(ConstantsValue.COBERTURAS_CONTRATADAS)
							&& !arrContenido[i].contains("MÍNIMO")
							&& !arrContenido[i].contains("Cobertura")
							&& !arrContenido[i].contains("**")
							&& !arrContenido[i].contains("UMA")
							&& !arrContenido[i].contains("OPERAN")
							&& !arrContenido[i].contains("CLIENTE INBURSA")
							&& !arrContenido[i].contains("Cláusula adicional de asistencia:")
							&& !arrContenido[i].contains("CLAUSULA ADICIONAL DE ASISTENCIA:")) {
						
					
						int sp = arrContenido[i].split("###").length;

						if (arrContenido[i].split("###")[0].length() > 3) {
							cobertura.setNombre(arrContenido[i].split("###")[0].trim());
							if (sp == 2 ) {
								cobertura.setSa(arrContenido[i].split("###")[1]);
								coberturas.add(cobertura);
							}
							if (sp > 2) {
								cobertura.setSa(arrContenido[i].split("###")[1]);
								cobertura.setDeducible(!arrContenido[i].split("###")[2].contains(".00")?arrContenido[i].split("###")[2]:"");
								coberturas.add(cobertura);
							}
						}
					}
				}
				modelo.setCoberturas(coberturas);
			}
			
			
			
			fin = contenido.indexOf("CLAVE Y NOMBRE DEL AGENTE");
			if(inicio > -1 && modelo.getAgente().isEmpty() && modelo.getCveAgente().isEmpty()) {
				newcontenido = (contenido.split("CLAVE Y NOMBRE DEL AGENTE")[0].length() > 200 ? contenido.substring(fin-199,fin)  :  contenido.substring(fin-100,fin));
				newcontenido = newcontenido.replace("@@@", "").replace("\r", "");		
				for (int i = 0; i < newcontenido.split("\n").length; i++) {
			        if(newcontenido.split("\n")[i].split("-").length >  2) {
			        	modelo.setCveAgente(newcontenido.split("\n")[i].split(" ")[0]);
			        	modelo.setAgente(newcontenido.split("\n")[i].split("###")[0].split(modelo.getCveAgente())[1]);
			        }
				}
				
			}

			return modelo;
		} catch (Exception ex) {
			modelo.setError(InbursaAutosModel.this.getClass().getTypeName() + " - catch:" + ex.getMessage() + " | "
					+ ex.getCause());
			return modelo;
		}
	}
	
	private void obtenerPolizaRegex() {
		Pattern pattern = Pattern.compile(POLIZA_REGEX);
		Matcher matcher = pattern.matcher(contenido);
		modelo.setPoliza(matcher.find() ? matcher.group(2) : "");
	}
	
	private void obtenerPolizaOtraUbicacion(String[] arrContenido,int i) {
		if (arrContenido[i].contains(ConstantsValue.POLIZA_MAYUS)
				&& arrContenido[i].contains("CIS")
				&& arrContenido[i].contains("ID CLIENTE")) {
			modelo.setPoliza(arrContenido[i - 1].split("###")[1]);
		} else if (arrContenido[i].contains(ConstantsValue.POLIZA_MAYUS)
				&& arrContenido[i].contains("CIS")
				&& (arrContenido[i].contains("Cliente Inbursa") || arrContenido[i].contains("CLIENTE INBURSA"))) {
			modelo.setPoliza(arrContenido[i - 1].split("###")[1]);
		} else if (arrContenido[i].contains(ConstantsValue.POLIZA_MAYUS)
				&& arrContenido[i].contains("FAMILIA")) {
			modelo.setPoliza(arrContenido[i + 1].split("###")[0]);
		}
	}
	
	private void obtenerClaveYDescripcionAuto(String texto) {
		if (texto.contains("CLAVE")) {
			if (texto.contains("ISIS:")) {
				modelo.setClave(texto.split("ISIS:")[1].split(" ")[0].replace("MARCA:", "").trim().replace("###", ""));
			} else if (texto.contains("VEHICULAR")) {
				modelo.setClave(
						texto.split("VEHICULAR:")[1].split(" ")[0].replace("MARCA:", "").trim().replace("###", ""));
			}

		}

		if (modelo.getClave().length() > 1) {
			modelo.setDescripcion(texto.split(modelo.getClave())[1].trim());
		}
		
	}
	
	private void obtenerFormaDePago(String lineaTexto) {
		for(String palabra:Arrays.asList(lineaTexto.split("###"))) {
			if(fn.formaPago(palabra) > 0) {
				modelo.setFormaPago(fn.formaPago(palabra));
			}
		}
	}
	
	private int limiteFinalCoberturas() {
		int indexTemporal = contenido.length();
		List<String> listPalabras = Arrays.asList("En caso de Siniestro","AVISO IMPORTANTE","**AVISO ###IMPORTANTE","OPERAN COMO","* Para hacer válida");
		//Se toma el indice inmediato donde finaliza la sección de coberturas
		for(String palabra:listPalabras) {
			if(contenido.indexOf(palabra)>-1 && contenido.indexOf(palabra)<indexTemporal) {
				indexTemporal = contenido.indexOf(palabra);
			}
		}
		return indexTemporal != contenido.length() ? indexTemporal : -1;
	}
	
	private void limpiarCteDireccion() {
		String direccion = fn.cleanString(modelo.getCteDireccion().replace("@", " "));
		if(direccion.indexOf("C.P")>-1) {
			modelo.setCteDireccion(direccion.substring(0,direccion.indexOf("C.P")).trim());
		}
	}
	
	private String completaTextoCobertura(String[] arrTexto,int i) {
		String texto = arrTexto[i];
		if(texto.split("###")[0].trim().equals("M Responsabilidad civil por daños a Terceros en su") && (i+1) < arrTexto.length ) {
			texto = completaTextoActualConLineaSiguiente(arrTexto, i, "M Responsabilidad civil por daños a Terceros en su", "persona");
		}else if(texto.contains("bienes")) {
			texto = completaTextoActualConLineaAnterior(arrTexto, i, "bienes", "M Responsabilidad civil por daños a Terceros en sus");
		}else if(texto.contains("CANADÁ *")) {
			texto = completaTextoActualConLineaAnterior(arrTexto, i, "CANADÁ *", "RESPONSABILIDAD CIVIL EN ESTADOS UNIDOS Y");
		}
		return texto;
	}
	
	private String completaTextoActualConLineaSiguiente(String[] arrTexto, int i, String textoActual, String textoSiguiente) {
		String texto = arrTexto[i];
		if(!texto.contains(textoSiguiente) && arrTexto[i+1].contains(textoSiguiente)) {
			texto = texto.replace(textoActual, textoActual + " " + textoSiguiente);
			arrTexto[i+1] = arrTexto[i+1].replace(textoSiguiente+"###", "").replace(textoSiguiente, "");
		}
		return texto;
	}
	
	private String completaTextoActualConLineaAnterior(String[] arrTexto, int i, String textoActual, String textoLineaAnterior) {
		String texto = arrTexto[i];
		if((i-1)< arrTexto.length) {
			if(!texto.contains(textoLineaAnterior) && arrTexto[i-1].contains(textoLineaAnterior)) {
				texto = texto.replace(textoActual, textoLineaAnterior + " "+textoActual);
				arrTexto[i-1] = arrTexto[i-1].replace(textoLineaAnterior+"###", "").replace(textoLineaAnterior, "");
			}
		}
		return texto;
	}
}
