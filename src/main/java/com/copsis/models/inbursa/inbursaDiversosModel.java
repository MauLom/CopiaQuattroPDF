package com.copsis.models.inbursa;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraCoberturasModel;
import com.copsis.models.EstructuraJsonModel;

public class inbursaDiversosModel {
	// Clases
	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
	// Varaibles
	private String contenido = "";
	private String resultado = "";

	public inbursaDiversosModel(String contenido) {
		this.contenido = contenido;
	}

	public EstructuraJsonModel procesar() {
		boolean iva = false;
		StringBuilder newcontenido = new StringBuilder();
		int inicio = 0;
		int fin = 0;

		contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());
		contenido = contenido.replace("DOLARES", "DÓLARES").replace("R.F.C.", "R.F.C").replace("I.V.A.", "IVA")
				.replace("COBERTURAS CONTRATADAS", "SECCION###COBERTURAS#").replace("hasta ", "Hasta ");
				
		try {
			// tipo
			modelo.setTipo(7);

			// cia
			modelo.setCia(35);

			inicio = contenido.indexOf("PÓLIZA DE SEGUROS");
			fin = contenido.indexOf("COBERTURAS");
			
			if (inicio > 0 && fin > 0 && inicio < fin) {
				newcontenido.append(contenido.substring(inicio, fin).replace("\r", "").replace("@", "")
						.replace("las 12:00 hrs. del", ""));
				for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
					
					if (newcontenido.toString().split("\n")[i].contains("PÓLIZA")
							&& newcontenido.toString().split("\n")[i].contains("CIS")
							&& newcontenido.toString().split("\n")[i].contains("ID CLIENTE")) {
						modelo.setPoliza(newcontenido.toString().split("\n")[i - 1].split("###")[1]);
					} else if (newcontenido.toString().split("\n")[i].contains("AGRUPACIÓN")
							&& newcontenido.toString().split("\n")[i].contains("PÓLIZA")
							&& newcontenido.toString().split("\n")[i].contains("CIS")) {
						modelo.setPoliza(newcontenido.toString().split("\n")[i + 1].split("###")[2]);
						resultado = newcontenido.toString().split("\n")[i + 2];
					}else if(newcontenido.toString().split("\n")[i].contains("PÓLIZA")
							&& newcontenido.toString().split("\n")[i].contains("CIS")
							&& newcontenido.toString().split("\n")[i].contains("CLIENTE INBURSA")) {
						modelo.setPoliza(newcontenido.toString().split("\n")[i - 1].split("###")[1]);
					}else if (newcontenido.toString().split("\n")[i].contains("Póliza")
					&& newcontenido.toString().split("\n")[i].contains("CIS")
					&& newcontenido.toString().split("\n")[i].contains("Cliente Inbursa")) {
							modelo.setPoliza(newcontenido.toString().split("\n")[i].split("Póliza")[1].split("CIS")[0].replace("###", ""));
					}
					if (newcontenido.toString().split("\n")[i].contains("C.P.")
							&& newcontenido.toString().split("\n")[i].contains("R.F.C")) {
						modelo.setCp(newcontenido.toString().split("\n")[i].split("C.P.")[1].split("###")[0].trim());
						if (newcontenido.toString().split("\n")[i + 1].contains("PRIMA NETA")) {
							modelo.setRfc(newcontenido.toString().split("\n")[i + 1].split("###")[0]);
						} else {
							modelo.setRfc(newcontenido.toString().split("\n")[i + 1].split("###")[1]);
						}
						resultado += " " + newcontenido.toString().split("\n")[i].split("R.F.C")[0];
					}

					if (newcontenido.toString().split("\n")[i].contains("PRIMA")
							&& newcontenido.toString().split("\n")[i].contains("NETA")
							&& newcontenido.toString().split("\n")[i].contains("AGRUPACIÓN")) {
							
						modelo.setPrimaneta(fn.castBigDecimal(fn.castDouble(newcontenido.toString().split("\n")[i + 1]
								.split("###")[newcontenido.toString().split("\n")[i + 1].split("###").length - 1])));								
								if(modelo.getPrimaneta() == null){
									List<String> valores = fn.obtenerListNumeros2(newcontenido.toString().split("\n")[i+2].replace(",", ""));								                 
									modelo.setPrimaneta(fn.castBigDecimal(fn.castDouble(valores.get(1).replace(",", ""))));
								}

					} else if (newcontenido.toString().split("\n")[i].contains("PRIMA NETA")) {
						if (newcontenido.toString().split("\n")[i].split("###").length > 2) {
							resultado += " " + newcontenido.toString().split("\n")[i].split("PRIMA NETA")[1].split(fn
									.extraerNumeros(newcontenido.toString().split("\n")[i].split("PRIMA NETA")[1]))[1];
							modelo.setPrimaneta(fn.castBigDecimal(fn.castDouble(
									fn.extraerNumeros(newcontenido.toString().split("\n")[i].split("PRIMA NETA")[1]))));
						} else {
							modelo.setPrimaneta(fn.castBigDecimal(fn.castDouble(
									newcontenido.toString().split("\n")[i].split("PRIMA NETA")[1].replace("###", ""))));
						}
						modelo.setCteDireccion(resultado.replace("###", ""));
					}
					// Datos de la direccion alternativa
					if (newcontenido.toString().split("\n")[i].contains("DIRECCIÓN:")) {
						String A = "", B = "", C = "";
						if (newcontenido.toString().split("\n")[i + 1].trim().contains(".00")) {
							A = newcontenido.toString().split("\n")[i + 1].split("SUMA")[0].trim();
						} else {
							A = newcontenido.toString().split("\n")[i + 1].trim();
						}
						if (newcontenido.toString().split("\n")[i + 2].trim().contains("R.F.C:")) {
							B = newcontenido.toString().split("\n")[i + 2].split("R.F.C:")[0].trim();
						} else {
							B = newcontenido.toString().split("\n")[i + 2].trim();
						}
						if (newcontenido.toString().split("\n")[i + 3].trim().contains(".00")) {
							if (newcontenido.toString().split("\n")[i + 3].split("###").length > 2) {
								C = newcontenido.toString().split("\n")[i + 3].split("###")[0].trim();
							} else {
								C = newcontenido.toString().split("\n")[i + 3].split("C.P.")[0].trim();
							}

						} else {
							C = newcontenido.toString().split("\n")[i + 3].split("###")[0].trim();
						}
						String x = A + " " + B + " " + C;
						modelo.setCteDireccion(x.replace("###", "").replaceAll(modelo.getRfc(), ""));
					}

					if (newcontenido.toString().split("\n")[i].contains("MONEDA")
							&& newcontenido.toString().split("\n")[i].contains("EXPEDICIÓN")) {
						modelo.setMoneda(fn.moneda(newcontenido.toString().split("\n")[i + 1].split("###")[0]));
					} else if (newcontenido.toString().split("\n")[i].contains("MONEDA")) {
						modelo.setMoneda(fn.moneda(newcontenido.toString().split("\n")[i + 1]));
					}

					if (newcontenido.toString().split("\n")[i].contains("FINANCIAMIENTO")
							&& newcontenido.toString().split("\n")[i].contains("R.F.C:")) {
						modelo.setRecargo(fn.castBigDecimal(fn.castDouble(newcontenido.toString().split("\n")[i + 1]
								.split("###")[newcontenido.toString().split("\n")[i + 1].split("###").length - 1]
										.replace("###", ""))));
					} else if (newcontenido.toString().split("\n")[i].contains("FINANCIAMIENTO")) {
						modelo.setRecargo(fn.castBigDecimal(fn.castDouble(
								newcontenido.toString().split("\n")[i].split("FINANCIAMIENTO")[1].replace("###", ""))));
					}

					if (newcontenido.toString().split("\n")[i].contains("DERECHO DE PÓLIZA")) {
						modelo.setDerecho(fn.castBigDecimal(
								fn.castDouble(newcontenido.toString().split("\n")[i].split("DERECHO DE PÓLIZA")[1]
										.replace("###", ""))));
					}
					if (newcontenido.toString().split("\n")[i].contains("GASTO DE EXPEDICIÓN")) {
						modelo.setDerecho(fn.castBigDecimal(fn.castDouble(
								newcontenido.toString().split("\n")[i + 1].split("###")[1].replace("###", ""))));
					}
					if (newcontenido.toString().split("\n")[i].contains("IVA")
							&& newcontenido.toString().split("\n")[i].contains("FORMA DE PAGO") && !iva) {
						modelo.setIva(fn.castBigDecimal(fn.castDouble(
								newcontenido.toString().split("\n")[i].split("IVA")[1].split("FORMA DE PAGO")[0]
										.replace("###", ""))));
						if (newcontenido.toString().split("\n")[i + 1].split("###").length > 4) {
							if (newcontenido.toString().split("\n")[i + 1].contains("PRIMA TOTAL")
									&& newcontenido.toString().split("\n")[i + 1].contains("Horas")) {
								String x = newcontenido.toString().split("\n")[i + 1].split("PRIMA TOTAL")[0];
								modelo.setFormaPago(fn.formaPago(x.split("###")[x.split("###").length - 1]));
							}
						} else {
							modelo.setFormaPago(fn.formaPago(newcontenido.toString().split("\n")[i + 2]
									.split("###")[newcontenido.toString().split("\n")[i + 2].split("###").length - 1]));
						}
						iva = true;
					} else if (newcontenido.toString().split("\n")[i].split("IVA").length > 4 && !iva) {
				
						modelo.setIva(fn.castBigDecimal(
								fn.castDouble(newcontenido.toString().split("\n")[i].split("IVA")[1].split("###")[1]
										.replace("###", ""))));
						iva = true;
					}else if((i+1) < newcontenido.toString().split("\n").length && newcontenido.toString().split("\n")[i].contains("IVA") && !iva) {
						String ivaTexto = newcontenido.toString().split("\n")[i+1];
						ivaTexto = ivaTexto.split("###")[ivaTexto.split("###").length -1];
						modelo.setIva(fn.castBigDecimal(fn.castDouble(fn.preparaPrimas(ivaTexto.trim()))));
					}
					if (modelo.getFormaPago() == 0
							&& newcontenido.toString().split("\n")[i].contains("FORMA DE PAGO")) {

						modelo.setFormaPago(fn.formaPago(newcontenido.toString().split("\n")[i + 1].split("###")[0]));

					}

					if (newcontenido.toString().split("\n")[i].contains("PRIMA TOTAL")
							&& newcontenido.toString().split("\n")[i].contains("Desde")) {
						if (newcontenido.toString().split("\n")[i].split("-").length > 2) {
							modelo.setPrimaTotal(fn.castBigDecimal(fn.castDouble(fn.extraerNumeros(
									newcontenido.toString().split("\n")[i].split("PRIMA TOTAL")[1].split("###")[1])
									.replace("###", ""))));
							if (modelo.getPrimaTotal().toString().length() > 0) {
								String x = newcontenido.toString().split("\n")[i].split(fn.extraerNumeros(
										newcontenido.toString().split("\n")[i].split("PRIMA TOTAL")[1].split("###")[1])
										.replace("###", ""))[1];
								modelo.setVigenciaDe(fn.formatDateMonthCadena(x.split("###")[0]));
								modelo.setVigenciaA(fn.formatDateMonthCadena(x.split("###")[1]));
							}
						} else {
							modelo.setPrimaTotal(fn.castBigDecimal(fn.castDouble(
									newcontenido.toString().split("\n")[i].split("PRIMA TOTAL")[1].split("###")[1])));
						}

					} else if (newcontenido.toString().split("\n")[i].contains("PRIMA TOTAL")
							&& newcontenido.toString().split("\n")[i].contains("SUMA ASEGURADA")) {
						modelo.setPrimaTotal(fn.castBigDecimal(fn.castDouble(newcontenido.toString().split("\n")[i + 1]
								.split("###")[newcontenido.toString().split("\n")[i + 1].split("###").length - 1]
										.replace("###", ""))));
					} else if (newcontenido.toString().split("\n")[i].contains("PRIMA TOTAL")) {
						modelo.setPrimaTotal(fn.castBigDecimal(fn.castDouble(
								newcontenido.toString().split("\n")[i].split("PRIMA TOTAL")[1].replace("###", ""))));
					}

					if (newcontenido.toString().split("\n")[i].contains("Desde")
							&& newcontenido.toString().split("\n")[i].contains("Hasta")) {
							
						if (newcontenido.toString().split("\n")[i].split("Desde")[1].contains("-")) {					
							modelo.setVigenciaA(fn.formatDateMonthCadena(fn.obtenVigePoliza2(newcontenido.toString().split("\n")[i].toUpperCase()).get(1)));
							modelo.setVigenciaDe(fn.formatDateMonthCadena(fn.obtenVigePoliza2(newcontenido.toString().split("\n")[i].toUpperCase()).get(0)));
							modelo.setFechaEmision(modelo.getVigenciaDe());
						}						
						if (modelo.getVigenciaA().length() ==0  && modelo.getVigenciaDe().length() == 0 && newcontenido.toString().split("\n")[i + 1].split("###")[0].contains("-")) {
							modelo.setVigenciaDe(fn
									.formatDateMonthCadena(newcontenido.toString().split("\n")[i + 1].split("###")[0]));
							modelo.setVigenciaA(fn
									.formatDateMonthCadena(newcontenido.toString().split("\n")[i + 1].split("###")[1]));
						} 
					}

					if (newcontenido.toString().split("\n")[i].contains("PRODUCTO")
							&& newcontenido.toString().split("\n")[i].contains("TIPO")) {
						modelo.setPlan(newcontenido.toString().split("\n")[i].split("###")[1].replace("###", ""));
						if(modelo.getPlan().contains("TIPO DE DOCUMENTO:") && newcontenido.toString().split("\n")[i].contains("PRODUCTO:###TIPO DE DOCUMENTO:")) {
							modelo.setPlan(newcontenido.toString().split("\n")[i + 1].split("###")[0].replace("###", "").trim());
						}
					} else if (newcontenido.toString().split("\n")[i].contains("PRODUCTO")) {
						modelo.setPlan(newcontenido.toString().split("\n")[i + 1].split("###")[0].replace("###", ""));
					}
					
					if (newcontenido.toString().split("\n")[i].contains("DATOS DEL CONTRATANTE") && newcontenido.toString().split("\n")[i + 1].contains("NOMBRE:")) {
						modelo.setCteNombre(newcontenido.toString().split("\n")[i + 2].split("###")[0].trim());
					}
				}
			}
			if(modelo.getCteDireccion().length() > 0 &&  modelo.getCteDireccion().indexOf("C.P.") > -1){
              modelo.setCp(modelo.getCteDireccion().split("C.P.")[1].trim().substring(0,5));
			}

			obtenerDatosAgenteYFechaEmision(contenido, modelo);
			
			inicio = contenido.indexOf("SECCION###COBERTURAS#");
			fin = contenido.indexOf("COBERTURAS###ADICIONALES");
			if (fin == -1) {
				fin = contenido.indexOf("A###PARTIR");
				if (fin == -1) {
					fin = contenido.indexOf("ZONA TERREMOTO");
					if (fin == -1) {
						fin = contenido.indexOf("ESPECIFICACIÓN DE CONDICIONES");
					}
				}
			}
			
			if (inicio > 0 && fin > 0 && inicio < fin) {
				List<EstructuraCoberturasModel> coberturas = new ArrayList<>();
				newcontenido = new StringBuilder();
				newcontenido.append(contenido.substring(inicio, fin).replace("\r", "").replace("@", "")
						.replace("las 12:00 hrs. del", ""));

				String seccion = "";
				for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
					EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();

					if (!newcontenido.toString().split("\n")[i].contains("SECCION###COBERTURAS")
							&& !newcontenido.toString().split("\n")[i].contains("Página")
							&& !newcontenido.toString().split("\n")[i].contains("COBERTURAS")
							&& !newcontenido.toString().split("\n")[i].contains(modelo.getPoliza())
							&& !newcontenido.toString().split("\n")[i].contains("PÓLIZA")) {

						int sp = newcontenido.toString().split("\n")[i].split("###").length;
					

						if (sp > 2) {
							seccion = newcontenido.toString().split("\n")[i].split("###")[0].replace("SECCIÓN", "")
									.replace("SECCIÓN", "").trim();
						}
						if (sp == 2) {
							if (newcontenido.toString().split("\n")[i].contains("SECCIÓN")) {
								seccion = newcontenido.toString().split("\n")[i].split("###")[0].replace("SECCIÓN", "");
								cobertura.setSeccion(seccion);
								cobertura.setNombre(newcontenido.toString().split("\n")[i].split("###")[1]);
								coberturas.add(cobertura);
							} else {
								cobertura.setSeccion(seccion);
								cobertura.setNombre(
										newcontenido.toString().split("\n")[i].split("###")[0].replace("SECCIÓN", ""));
								cobertura.setSa(newcontenido.toString().split("\n")[i].split("###")[1]);
								coberturas.add(cobertura);
							}

						}
						if (sp == 3) {
							cobertura.setSeccion(seccion);
							cobertura.setNombre(newcontenido.toString().split("\n")[i].split("###")[1]);
							cobertura.setSa(newcontenido.toString().split("\n")[i].split("###")[2]);
							coberturas.add(cobertura);
						}
						if (sp == 4) {
							cobertura.setSeccion(seccion);
							cobertura.setNombre(newcontenido.toString().split("\n")[i].split("###")[1]);
							cobertura.setSa(newcontenido.toString().split("\n")[i].split("###")[2]);
							coberturas.add(cobertura);
						}
					}
				}
				modelo.setCoberturas(coberturas);
			}
			
			if(modelo.getCoberturas().isEmpty()) {
				obtenerCoberturasOtroFormato(contenido, modelo);
			}

			return modelo;
		} catch (Exception ex) {
			ex.printStackTrace();
			modelo.setError(inbursaDiversosModel.this.getClass().getTypeName() + " - catch:" + ex.getMessage() + " | "
					+ ex.getCause());
			return modelo;
		}
	}
	
	
	
	private void obtenerDatosAgenteYFechaEmision(String textoContenido, EstructuraJsonModel model) {
		//System.out.println(contenido);
		int indexInicio = 0;
		indexInicio= contenido.lastIndexOf("Cliente Inbursa");
		if(indexInicio == -1){
			indexInicio= contenido.indexOf("Término máximo para el pago de segunda fracción");
		}
		
		int indexFin = contenido.indexOf("CLAVE Y NOMBRE DEL AGENTE");

		if(indexInicio >  -1 && indexFin >  0 &&  indexInicio < indexFin){

		
		
		String newcontenido = textoContenido.substring(indexInicio,indexFin);
		
		newcontenido = newcontenido.replace("@@@", "").replace("\r", "");	
		
		StringBuilder aux = new StringBuilder();
		String[] arrContenido =  newcontenido.split("\n");
		String fecha = "";
		
		for (int i = 0; i < arrContenido.length; i++) {
			
	        if(arrContenido[i].trim().length() > 0 && !arrContenido[i].contains("Término ")) {
	        	aux.append(arrContenido[i].split("###")[0]).append(" ");
	        }
	        if(arrContenido[i].split("-").length == 3 && arrContenido[i].split("-").length > 1 ) {
	        	
	        	fecha = arrContenido[i].split("###")[1].trim();
	        	if(fecha.split("-").length == 3) {
					
	        		model.setFechaEmision(fn.formatDateMonthCadena(fecha));
	        	}
	        }
		}
	   if(aux.length() > 10 && aux.toString().split(" ").length > 10){
		model.setCveAgente(aux.toString().split(" ")[0]);
		if(model.getCveAgente().trim().length() >5){
			model.setAgente(aux.toString().split(model.getCveAgente())[1].trim());
		}
	
	   }
	
	 }
	}

	private void obtenerCoberturasOtroFormato(String texto, EstructuraJsonModel model) {
		int indexInicio = texto.indexOf("COBERTURAS ADICIONALES CONTRATADAS");
		int indexFin = texto.indexOf("DEDUCIBLES");
		List<EstructuraCoberturasModel> coberturas = new ArrayList<>();
		
		if(indexInicio > -1 && indexInicio < indexFin) {
			String newContenido = texto.substring(indexInicio +35, indexFin).replace("@@@", "").replace("\r", "");
			if(!newContenido.contains("SUMA ASEGURADA")) {
				//FORMATO donde en un apartado estan nombres de coberturas y el valor de deducible se encuentra en otro apartado 
				Map<String, String> mapDeducibles = obtenerApartadoDeducible(texto);
				String deducibleDemasCoberturas = mapDeducibles.containsKey("DEMÁS COBERTURAS") ? mapDeducibles.get("DEMÁS COBERTURAS") : "";
				String nombre = "";
				String deducible = "";
				int sp = 0;
				
				StringBuilder strbCoberturas = new StringBuilder();
				if(texto.split("Cobertura básica").length>1) {
					String coberturaB = fn.elimgatos(texto.split("Cobertura básica")[1].split("\n")[0].trim());
					coberturaB = "Cobertura básica###" +coberturaB.split("###")[0].replace(":", "")+ "\n";
					newContenido = coberturaB + newContenido;
				}

				String[] arrContenido = newContenido.split("\n");
				for(int i=0;i< arrContenido.length;i++) {
					arrContenido[i] = completaTextoCobertura(arrContenido, i);
					if (arrContenido[i].length() > 1 && !arrContenido[i].contains("Página")
							&& !arrContenido[i].contains("CARÁTULA") && arrContenido[i].split("-").length < 3
							&& !arrContenido[i].contains("CLIENTE INBURSA")) {

						nombre = arrContenido[i].split("###")[0].trim().toUpperCase();
						
						if(mapDeducibles.containsKey(nombre)) {
							deducible  = mapDeducibles.get(nombre);
						}else if(nombre.equalsIgnoreCase("Cobertura Básica")) {
							deducible = mapDeducibles.containsKey("BÁSICA") ? mapDeducibles.get("BÁSICA")
									: deducibleDemasCoberturas;
						}else if(nombre.length() >0){
							deducible = deducibleDemasCoberturas;						
						}
						sp = arrContenido[i].split("###").length;
						if(sp == 1) {
							strbCoberturas.append(arrContenido[i]).append("### ###").append(deducible);
							strbCoberturas.append("\n");
						}else if(sp == 2) {
							strbCoberturas.append(arrContenido[i]).append("###").append(deducible);
							strbCoberturas.append("\n");
						}
					}

			
				}
				
				arrContenido = strbCoberturas.toString().split("\n");
				for(String textoCobertura:arrContenido) {
					EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();
					sp = textoCobertura.split("###").length;
					if(textoCobertura.split("###").length> 1) {
						cobertura.setNombre(textoCobertura.split("###")[0].trim());
						cobertura.setSa(textoCobertura.split("###")[1].trim());
						if(sp == 3) {
							cobertura.setDeducible(textoCobertura.split("###")[2].trim());
						}						
						coberturas.add(cobertura);
					}
				}
				
				if(!coberturas.isEmpty()) {
					model.setCoberturas(coberturas);
				}
			}
		}
		
		
	}
	private String completaTextoCobertura(String[] arrTexto,int i) {
		String texto = arrTexto[i];
		if(texto.contains("RESPONSABILIDAD CIVIL MANIOBRAS DE CARGA Y")) {
			texto = completaTextoActualConLineaSiguiente(arrTexto, i, "RESPONSABILIDAD CIVIL MANIOBRAS DE CARGA Y", "DESCARGA");
		} else if (texto.contains("RESPONSABILIDAD CIVIL INSTALACIONES")) {
			texto = completaTextoActualConLineaSiguiente(arrTexto, i, "RESPONSABILIDAD CIVIL INSTALACIONES",
					"SUBTERRANEAS");
		} else if (texto.contains("RESPONSABILIDAD CIVIL PRODUCTOS Y-O TRABAJOS")) {
			texto = completaTextoActualConLineaSiguiente(arrTexto, i, "RESPONSABILIDAD CIVIL PRODUCTOS Y-O TRABAJOS",
					"TERMINADOS");
		}
		
		return texto;
	}
	
	private String completaTextoActualConLineaSiguiente(String[] arrTexto, int i, String textoActual, String textoSiguiente) {
		String texto = arrTexto[i];
		if(!texto.contains(textoSiguiente) && arrTexto[i+1].contains(textoSiguiente)) {
			texto = texto.replace(textoActual, textoActual + " " + textoSiguiente);
			arrTexto[i+1] = arrTexto[i+1].replace(textoSiguiente, "").replace(textoSiguiente+"###", "");
		}
		return texto;
	}
	
	private Map<String, String> obtenerApartadoDeducible(String texto) {
		int indexInicio = texto.indexOf("DEDUCIBLES");
		int indexFin = texto.indexOf("La presente póliza");	
		Map<String, String> deducibles = new HashMap<>();

		if(indexInicio > -1 && indexInicio < indexFin) {
			String newContenido = texto.substring(indexInicio, indexFin).replace("@@@", "").replace("\r", "");
			String[] arrewContenido = newContenido.split("\n");
			String key = "";
			StringBuilder valor = new StringBuilder();
			for(String renglon : arrewContenido) {
				if(renglon.split("###").length == 2 && (renglon.contains("%") || renglon.contains("porciento"))) {
					key = "";
					valor = new StringBuilder();
					key = renglon.split("###")[0].toUpperCase().trim();
					valor.append(renglon.split("###")[1]);
				}else if(renglon.split("###").length ==1) {
					valor.append(" ").append(renglon);
				}
				
				if(key.length() > 0) {
					deducibles.put(key, fn.eliminaSpacios(valor.toString()).trim());
				}
			}
		}

		return deducibles;
	}
}
