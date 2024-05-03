package com.copsis.models.inbursa;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.copsis.constants.ConstantsValue;
import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraCoberturasModel;
import com.copsis.models.EstructuraJsonModel;

public class inbursaDiversosModel {
	// Clases
	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
	// Varaibles
	private String contenido = "";
	private StringBuilder resultado = new StringBuilder();

	public inbursaDiversosModel(String contenido) {
		this.contenido = contenido;
	}

	public EstructuraJsonModel procesar() {
		boolean iva = false;
		StringBuilder newcontenido = new StringBuilder();
		int inicio = 0;
		int fin = 0;

		contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());
		contenido = contenido.replace("DOLARES", "DÓLARES").replace("R.F.C.",ConstantsValue.RFC2).replace("I.V.A.", "IVA")
				.replace("COBERTURAS CONTRATADAS", "SECCION###COBERTURAS#").replace("hasta ", "Hasta ");

		try {
			// tipo
			modelo.setTipo(7);

			// cia
			modelo.setCia(38);

			inicio = contenido.indexOf("PÓLIZA DE SEGUROS");
			fin = contenido.indexOf(ConstantsValue.COBERTURAS.toUpperCase());
			fin =  fin == -1 ? contenido.indexOf("Seguros ###Inbursa") : fin;
			
		
	
			if (inicio > 0 && fin > 0 && inicio < fin) {
				newcontenido.append(contenido.substring(inicio, fin).replace("\r", "").replace("@", "")
						.replace("las 12:00 hrs. del", ""));
				for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
					
					if (newcontenido.toString().split("\n")[i].contains(ConstantsValue.POLIZA_MAYUS)
							&& newcontenido.toString().split("\n")[i].contains("CIS")
							&& newcontenido.toString().split("\n")[i].contains("ID CLIENTE")) {
						modelo.setPoliza(newcontenido.toString().split("\n")[i - 1].split("###")[1]);
					} else if (newcontenido.toString().split("\n")[i].contains("AGRUPACIÓN")
							&& newcontenido.toString().split("\n")[i].contains(ConstantsValue.POLIZA_MAYUS)
							&& newcontenido.toString().split("\n")[i].contains("CIS")) {
						modelo.setPoliza(newcontenido.toString().split("\n")[i + 1].split("###")[2]);
						resultado.append( newcontenido.toString().split("\n")[i + 2]);
					} else if (newcontenido.toString().split("\n")[i].contains(ConstantsValue.POLIZA_MAYUS)
							&& newcontenido.toString().split("\n")[i].contains("CIS")
							&& newcontenido.toString().split("\n")[i].contains("CLIENTE INBURSA")) {
						modelo.setPoliza(newcontenido.toString().split("\n")[i - 1].split("###")[1]);
					} else if (newcontenido.toString().split("\n")[i].contains(ConstantsValue.POLIZA_MAYUS)
							&& newcontenido.toString().split("\n")[i].contains("CIS")
							&& newcontenido.toString().split("\n")[i].contains("Cliente Inbursa")) {
						modelo.setPoliza(newcontenido.toString().split("\n")[i].split(ConstantsValue.POLIZA_MAYUS)[1].split("CIS")[0]
								.replace("###", ""));
					} else if (newcontenido.toString().split("\n")[i].contains("Póliza")
							&& newcontenido.toString().split("\n")[i].contains("CIS")
							&& newcontenido.toString().split("\n")[i].contains("Cliente Inbursa")) {
						modelo.setPoliza(newcontenido.toString().split("\n")[i].split("Póliza")[1].split("CIS")[0]
								.replace("###", "").trim());
					} else if(newcontenido.toString().split("\n")[i].contains(ConstantsValue.POLIZA_ACENT) && newcontenido.toString().split("\n")[i].contains("CIS")){
                          modelo.setPoliza(newcontenido.toString().split("\n")[i].split(ConstantsValue.POLIZA_ACENT)[1].split("CIS")[0].replace("###", "").trim());   
					}
					
					if (newcontenido.toString().split("\n")[i].contains("C.P.")
							&& newcontenido.toString().split("\n")[i].contains("R.F.C")) {
						modelo.setCp(newcontenido.toString().split("\n")[i].split("C.P.")[1].split("###")[0].trim());
						if (newcontenido.toString().split("\n")[i + 1].contains(ConstantsValue.PRIMA_NETA_MAYUS2)) {
							modelo.setRfc(newcontenido.toString().split("\n")[i + 1].split("###")[0]);
						} else {
							modelo.setRfc(newcontenido.toString().split("\n")[i + 1].split("###")[1]);
						}
						resultado.append(" " + newcontenido.toString().split("\n")[i].split("R.F.C")[0]);
					}

					if (newcontenido.toString().split("\n")[i].contains("PRIMA")
							&& newcontenido.toString().split("\n")[i].contains("NETA")
							&& newcontenido.toString().split("\n")[i].contains("AGRUPACIÓN")) {

						modelo.setPrimaneta(fn.castBigDecimal(fn.castDouble(newcontenido.toString().split("\n")[i + 1]
								.split("###")[newcontenido.toString().split("\n")[i + 1].split("###").length - 1])));
						if (modelo.getPrimaneta() == null) {
							List<String> valores = fn
									.obtenerListNumeros2(newcontenido.toString().split("\n")[i + 2].replace(",", ""));
							modelo.setPrimaneta(fn.castBigDecimal(fn.castDouble(valores.get(1).replace(",", ""))));
						}

					} else if (newcontenido.toString().split("\n")[i].contains(ConstantsValue.PRIMA_NETA_MAYUS2)) {
						if (newcontenido.toString().split("\n")[i].split("###").length > 2) {
							resultado.append( " " + newcontenido.toString().split("\n")[i].split(ConstantsValue.PRIMA_NETA_MAYUS2)[1].split(fn
									.extraerNumeros(newcontenido.toString().split("\n")[i].split(ConstantsValue.PRIMA_NETA_MAYUS2)[1]))[1]);
							modelo.setPrimaneta(fn.castBigDecimal(fn.castDouble(
									fn.extraerNumeros(newcontenido.toString().split("\n")[i].split(ConstantsValue.PRIMA_NETA_MAYUS2)[1]))));
						} else {
							modelo.setPrimaneta(fn.castBigDecimal(fn.castDouble(
									newcontenido.toString().split("\n")[i].split(ConstantsValue.PRIMA_NETA_MAYUS2)[1].replace("###", ""))));
						}
						modelo.setCteDireccion(resultado.toString().replace("###", ""));
					}
					// Datos de la direccion alternativa
					if (newcontenido.toString().split("\n")[i].contains("DIRECCIÓN:")) {
					
						String a = "";
						String b = "";
						String c = "";
						if (newcontenido.toString().split("\n")[i + 1].trim().contains(".00")) {
							a = newcontenido.toString().split("\n")[i + 1].split("SUMA")[0].trim();
						} else {
							a = newcontenido.toString().split("\n")[i + 1].trim();
						}
						if (newcontenido.toString().split("\n")[i + 2].trim().contains(ConstantsValue.RFC)) {
							b = newcontenido.toString().split("\n")[i + 2].split(ConstantsValue.RFC)[0].trim();
						} else {
							b = newcontenido.toString().split("\n")[i + 2].trim();
						}
						if (newcontenido.toString().split("\n")[i + 3].trim().contains(".00")) {
							if (newcontenido.toString().split("\n")[i + 3].split("###").length > 2) {
								c = newcontenido.toString().split("\n")[i + 3].split("###")[0].trim();
							} else {
								c = newcontenido.toString().split("\n")[i + 3].split("C.P.")[0].trim();
							}

						} else {
							c = newcontenido.toString().split("\n")[i + 3].split("###")[0].trim();
						}
						String x = a + " " + b + " " + c;
						modelo.setCteDireccion(x.replace("###", "").replaceAll(modelo.getRfc(), ""));
					}

					if (newcontenido.toString().split("\n")[i].contains("MONEDA")
							&& newcontenido.toString().split("\n")[i].contains("EXPEDICIÓN")) {
						modelo.setMoneda(fn.moneda(newcontenido.toString().split("\n")[i + 1].split("###")[0]));
					} else if (newcontenido.toString().split("\n")[i].contains("MONEDA")) {
						modelo.setMoneda(fn.moneda(newcontenido.toString().split("\n")[i + 1]));
					}

					if (newcontenido.toString().split("\n")[i].contains(ConstantsValue.FINANCIAMIENTO_MAYUS2)
							&& newcontenido.toString().split("\n")[i].contains("R.F.C:")) {
						modelo.setRecargo(fn.castBigDecimal(fn.castDouble(newcontenido.toString().split("\n")[i + 1]
								.split("###")[newcontenido.toString().split("\n")[i + 1].split("###").length - 1]
								.replace("###", ""))));
					} else if (newcontenido.toString().split("\n")[i].contains(ConstantsValue.FINANCIAMIENTO_MAYUS2)) {
						modelo.setRecargo(fn.castBigDecimal(fn.castDouble(
								newcontenido.toString().split("\n")[i].split(ConstantsValue.FINANCIAMIENTO_MAYUS2)[1].replace("###", ""))));
					}

					if (newcontenido.toString().split("\n")[i].contains(ConstantsValue.DERECHO_POLIZAMY)) {
						modelo.setDerecho(fn.castBigDecimal(
								fn.castDouble(newcontenido.toString().split("\n")[i].split(ConstantsValue.DERECHO_POLIZAMY)[1]
										.replace("###", ""))));
					}
					if (newcontenido.toString().split("\n")[i].contains("GASTO DE EXPEDICIÓN")) {
						modelo.setDerecho(fn.castBigDecimal(fn.castDouble(
								newcontenido.toString().split("\n")[i + 1].split("###")[1].replace("###", ""))));
					}
					if (newcontenido.toString().split("\n")[i].contains("IVA")
							&& newcontenido.toString().split("\n")[i].contains(ConstantsValue.FORMA_PAGO_MAYUS) && !iva) {
						modelo.setIva(fn.castBigDecimal(fn.castDouble(
								newcontenido.toString().split("\n")[i].split("IVA")[1].split(ConstantsValue.FORMA_PAGO_MAYUS)[0]
										.replace("###", ""))));
						if (newcontenido.toString().split("\n")[i + 1].split("###").length > 4) {
							if (newcontenido.toString().split("\n")[i + 1].contains(ConstantsValue.PRIMA_TOTAL_MAYUS2)
									&& newcontenido.toString().split("\n")[i + 1].contains("Horas")) {
								String x = newcontenido.toString().split("\n")[i + 1].split(ConstantsValue.PRIMA_TOTAL_MAYUS2)[0];
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
					} else if ((i + 1) < newcontenido.toString().split("\n").length
							&& newcontenido.toString().split("\n")[i].contains("IVA") && !iva) {
						String ivaTexto = newcontenido.toString().split("\n")[i + 1];
						ivaTexto = ivaTexto.split("###")[ivaTexto.split("###").length - 1];
						modelo.setIva(fn.castBigDecimal(fn.castDouble(fn.preparaPrimas(ivaTexto.trim()))));
					}
					if (modelo.getFormaPago() == 0
							&& newcontenido.toString().split("\n")[i].contains("FORMA DE PAGO")) {

						modelo.setFormaPago(fn.formaPago(newcontenido.toString().split("\n")[i + 1].split("###")[0]));

					}

					if (newcontenido.toString().split("\n")[i].contains(ConstantsValue.PRIMA_TOTAL_MAYUS2)
							&& newcontenido.toString().split("\n")[i].contains(ConstantsValue.DESDE)) {
						if (newcontenido.toString().split("\n")[i].split("-").length > 2) {
							modelo.setPrimaTotal(fn.castBigDecimal(fn.castDouble(fn.extraerNumeros(
									newcontenido.toString().split("\n")[i].split(ConstantsValue.PRIMA_TOTAL_MAYUS2)[1].split("###")[1])
									.replace("###", ""))));
							if (modelo.getPrimaTotal().toString().length() > 0) {
								String x = newcontenido.toString().split("\n")[i].split(fn.extraerNumeros(
										newcontenido.toString().split("\n")[i].split(ConstantsValue.PRIMA_TOTAL_MAYUS2)[1].split("###")[1])
										.replace("###", ""))[1];
								modelo.setVigenciaDe(fn.formatDateMonthCadena(x.split("###")[0]));
								modelo.setVigenciaA(fn.formatDateMonthCadena(x.split("###")[1]));
							}
						} else {
							modelo.setPrimaTotal(fn.castBigDecimal(fn.castDouble(
									newcontenido.toString().split("\n")[i].split(ConstantsValue.PRIMA_TOTAL_MAYUS2)[1].split("###")[1])));
						}

					} else if (newcontenido.toString().split("\n")[i].contains(ConstantsValue.PRIMA_TOTAL_MAYUS2)
							&& newcontenido.toString().split("\n")[i].contains("SUMA ASEGURADA")) {
						modelo.setPrimaTotal(fn.castBigDecimal(fn.castDouble(newcontenido.toString().split("\n")[i + 1]
								.split("###")[newcontenido.toString().split("\n")[i + 1].split("###").length - 1]
								.replace("###", ""))));
					} else if (newcontenido.toString().split("\n")[i].contains(ConstantsValue.PRIMA_TOTAL_MAYUS2)) {
						modelo.setPrimaTotal(fn.castBigDecimal(fn.castDouble(
								newcontenido.toString().split("\n")[i].split(ConstantsValue.PRIMA_TOTAL_MAYUS2)[1].replace("###", ""))));
					}

					if (newcontenido.toString().split("\n")[i].contains(ConstantsValue.DESDE)
							&& newcontenido.toString().split("\n")[i].contains("Hasta")) {

						if (newcontenido.toString().split("\n")[i].split(ConstantsValue.DESDE)[1].contains("-")) {
							modelo.setVigenciaA(fn.formatDateMonthCadena(
									fn.obtenVigePoliza2(newcontenido.toString().split("\n")[i].toUpperCase()).get(1)));
							modelo.setVigenciaDe(fn.formatDateMonthCadena(
									fn.obtenVigePoliza2(newcontenido.toString().split("\n")[i].toUpperCase()).get(0)));
							modelo.setFechaEmision(modelo.getVigenciaDe());
						}
						if (modelo.getVigenciaA().length() == 0 && modelo.getVigenciaDe().length() == 0
								&& newcontenido.toString().split("\n")[i + 1].split("###")[0].contains("-")) {
							modelo.setVigenciaDe(fn
									.formatDateMonthCadena(newcontenido.toString().split("\n")[i + 1].split("###")[0]));
							modelo.setVigenciaA(fn
									.formatDateMonthCadena(newcontenido.toString().split("\n")[i + 1].split("###")[1]));
						}
					}
					if (modelo.getVigenciaDe().length() == 0 
					&& newcontenido.toString().split("\n")[i].contains(ConstantsValue.DESDE)){
						List<String> valores = fn.obtenVigePoliza2(newcontenido.toString().split("\n")[i].toUpperCase());
						
						modelo.setVigenciaDe(fn.formatDateMonthCadena(valores.get(0)));
						modelo.setFechaEmision(modelo.getVigenciaDe());
					}

					if (modelo.getVigenciaA().length() == 0 && newcontenido.toString().split("\n")[i].contains("Hasta")){
						List<String> valoresA = fn.obtenVigePoliza2(newcontenido.toString().split("\n")[i].toUpperCase());											
						modelo.setVigenciaA(fn.formatDateMonthCadena(valoresA.get(0)));
					}

					if (newcontenido.toString().split("\n")[i].contains("PRODUCTO")
							&& newcontenido.toString().split("\n")[i].contains("TIPO")) {
						modelo.setPlan(newcontenido.toString().split("\n")[i].split("###")[1].replace("###", ""));
						if (modelo.getPlan().contains("TIPO DE DOCUMENTO:")
								&& newcontenido.toString().split("\n")[i].contains("PRODUCTO:###TIPO DE DOCUMENTO:")) {
							modelo.setPlan(newcontenido.toString().split("\n")[i + 1].split("###")[0].replace("###", "")
									.trim());
						}
					} else if (newcontenido.toString().split("\n")[i].contains("PRODUCTO")) {
						modelo.setPlan(newcontenido.toString().split("\n")[i + 1].split("###")[0].replace("###", ""));
					}

					if (newcontenido.toString().split("\n")[i].contains("DATOS DEL CONTRATANTE")
							&& newcontenido.toString().split("\n")[i + 1].contains("NOMBRE:")) {
						modelo.setCteNombre(newcontenido.toString().split("\n")[i + 2].split("###")[0].trim());
					}
				}
			}
			if (modelo.getCteDireccion().length() > 0 && modelo.getCteDireccion().indexOf("C.P.") > -1) {
				modelo.setCp(modelo.getCteDireccion().split("C.P.")[1].trim().substring(0, 5));
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
							&& !newcontenido.toString().split("\n")[i].contains(ConstantsValue.POLIZA_MAYUS)) {

						int sp = newcontenido.toString().split("\n")[i].split("###").length;

						if (sp > 2) {
							seccion = newcontenido.toString().split("\n")[i].split("###")[0].replace(ConstantsValue.SECCION2, "")
									.trim();
						}
						if (sp == 2) {
							if (newcontenido.toString().split("\n")[i].contains(ConstantsValue.SECCION2)) {
								seccion = newcontenido.toString().split("\n")[i].split("###")[0].replace(ConstantsValue.SECCION2, "");
								cobertura.setSeccion(seccion);
								cobertura.setNombre(newcontenido.toString().split("\n")[i].split("###")[1]);
								coberturas.add(cobertura);
							} else {
								cobertura.setSeccion(seccion);
								cobertura.setNombre(
										newcontenido.toString().split("\n")[i].split("###")[0].replace(ConstantsValue.SECCION2, ""));
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

			if (modelo.getCoberturas().isEmpty()) {
				obtenerCoberturasOtroFormato(contenido, modelo);
			}

			inicio = contenido.indexOf("COBERTURAS###SUMA ASEGURADA###PRIMA NETA");
			fin = contenido.indexOf("EXCL. ENFERMEDAD");

			newcontenido = new StringBuilder();
			newcontenido.append(fn.extracted(inicio, fin, contenido));

			if (modelo.getCoberturas().isEmpty()) {
				List<EstructuraCoberturasModel> coberturas = new ArrayList<>();
				for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
					EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();
					if (!newcontenido.toString().split("\n")[i].contains("COBERTURAS")
							&& !newcontenido.toString().split("\n")[i].contains("GR45")) {
						int sp = newcontenido.toString().split("\n")[i].split("###").length;
						switch (sp) {
							case 1:
							cobertura.setNombre(newcontenido.toString().split("\n")[i].split("###")[0]);
						
							coberturas.add(cobertura);
								break;
							case 3:
							cobertura.setNombre(newcontenido.toString().split("\n")[i].split("###")[0]);
							cobertura.setSa(newcontenido.toString().split("\n")[i].split("###")[1]);
							coberturas.add(cobertura);
								break;

							default:
								break;
						}
						
					}
					
					if(!coberturas.isEmpty() && coberturas.size() > 1 ){
 					   modelo.setCoberturas(coberturas);
					}
                 
				}

				
			}
		

			return modelo;
		} catch (Exception ex) {			
			modelo.setError(inbursaDiversosModel.this.getClass().getTypeName() + " - catch:" + ex.getMessage() + " | "
					+ ex.getCause());
			return modelo;
		}
	}

	private void obtenerDatosAgenteYFechaEmision(String textoContenido, EstructuraJsonModel model) {

		int indexInicio = 0;
	
		indexInicio = contenido.lastIndexOf("Cliente Inbursa");	
		indexInicio =  indexInicio== -1 ? contenido.lastIndexOf("Término máximo para el pago de segunda fracción"):indexInicio;
		
		

		int indexFin = contenido.indexOf("CLAVE Y NOMBRE DEL AGENTE");
		StringBuilder aux = new StringBuilder();
		if (indexInicio > -1 && indexFin > 0 && indexInicio < indexFin) {

			String newcontenido = textoContenido.substring(indexInicio, indexFin);
			
			
			newcontenido = newcontenido.replace("@@@", "").replace("\r", "");

			
			String[] arrContenido = newcontenido.split("\n");
			String fecha = "";

			for (int i = 0; i < arrContenido.length; i++) {

				if (arrContenido[i].trim().length() > 0 && !arrContenido[i].contains("Término ")) {
					aux.append(arrContenido[i].split("###")[0]);
				}
				if (arrContenido[i].split("-").length == 3 && arrContenido[i].split("-").length > 1) {

					fecha = arrContenido[i].split("###")[1].trim();
					if (fecha.split("-").length == 3) {

						model.setFechaEmision(fn.formatDateMonthCadena(fecha));
					}
				}
			}
			if (aux.length() > 10 && aux.toString().split(" ").length > 10) {

				if (aux.toString().contains("Definiciones:")) {
					aux.append( aux.toString().split("Definiciones:")[1]);
				}

				model.setCveAgente(aux.toString().split(" ")[0]);
				if (model.getCveAgente().trim().length() > 5) {
					fecha = !fn.obtenVigePoliza2(aux.toString()).isEmpty() ? fn.obtenVigePoliza2(aux.toString()).get(0) : "";

					model.setAgente(aux.toString().split(model.getCveAgente())[1].replace(fecha, " ").trim());
				}

			}

		}

		if(modelo.getAgente().length() > 100){
			modelo.setCveAgente("");
			modelo.setAgente("");
			 aux = new StringBuilder();		
			indexInicio = contenido.lastIndexOf("Término máximo");
			indexFin = contenido.indexOf("Página 3 de 5");
		
			indexFin = indexFin == -1 ?  contenido.indexOf("Página 3 de 6"): indexFin;
			
			if(indexInicio > -1 && indexFin > -1){			
			aux.append(contenido.substring(indexInicio,indexFin).replace("\r",""));
			for(int i =0; i < aux.toString().split("\n").length;i++){
					 if(aux.toString().split("\n")[i].contains("Término máximo")){
						List<String> valores = fn.obtenerListNumeros2(aux.toString().split("\n")[i+1]);
						if(!valores.isEmpty()){
					      modelo.setCveAgente(valores.get(0).trim());
						  modelo.setAgente(aux.toString().split("\n")[i+1].split(modelo.getCveAgente())[1].trim() );
						}
                                           
					 }
			 }
		  }
		}
	}

	private void obtenerCoberturasOtroFormato(String texto, EstructuraJsonModel model) {
		int indexInicio = texto.indexOf("COBERTURAS ADICIONALES CONTRATADAS");
		int indexFin = texto.indexOf("DEDUCIBLES");
		List<EstructuraCoberturasModel> coberturas = new ArrayList<>();

		if (indexInicio > -1 && indexInicio < indexFin) {
			String newContenido = texto.substring(indexInicio + 35, indexFin).replace("@@@", "").replace("\r", "");
			if (!newContenido.contains("SUMA ASEGURADA")) {
				// FORMATO donde en un apartado estan nombres de coberturas y el valor de
				// deducible se encuentra en otro apartado
				Map<String, String> mapDeducibles = obtenerApartadoDeducible(texto);
				String deducibleDemasCoberturas = mapDeducibles.containsKey("DEMÁS COBERTURAS")
						? mapDeducibles.get("DEMÁS COBERTURAS")
						: "";
				String nombre = "";
				String deducible = "";
				int sp = 0;

				StringBuilder strbCoberturas = new StringBuilder();
				if (texto.split(ConstantsValue.COBERTURAS_BASICA).length > 1) {
					String coberturaB = fn.elimgatos(texto.split(ConstantsValue.COBERTURAS_BASICA)[1].split("\n")[0].trim());
					coberturaB = "Cobertura básica###" + coberturaB.split("###")[0].replace(":", "") + "\n";
					newContenido = coberturaB + newContenido;
				}

				String[] arrContenido = newContenido.split("\n");
				for (int i = 0; i < arrContenido.length; i++) {
					arrContenido[i] = completaTextoCobertura(arrContenido, i);
					if (arrContenido[i].length() > 1 && !arrContenido[i].contains("Página")
							&& !arrContenido[i].contains("CARÁTULA") && arrContenido[i].split("-").length < 3
							&& !arrContenido[i].contains("CLIENTE INBURSA")) {

						nombre = arrContenido[i].split("###")[0].trim().toUpperCase();

						if (mapDeducibles.containsKey(nombre)) {
							deducible = mapDeducibles.get(nombre);
						} else if (nombre.equalsIgnoreCase("Cobertura Básica")) {
							deducible = mapDeducibles.containsKey("BÁSICA") ? mapDeducibles.get("BÁSICA")
									: deducibleDemasCoberturas;
						} else if (nombre.length() > 0) {
							deducible = deducibleDemasCoberturas;
						}
						sp = arrContenido[i].split("###").length;
						if (sp == 1) {
							strbCoberturas.append(arrContenido[i]).append("### ###").append(deducible);
							strbCoberturas.append("\n");
						} else if (sp == 2) {
							strbCoberturas.append(arrContenido[i]).append("###").append(deducible);
							strbCoberturas.append("\n");
						}
					}

				}

				arrContenido = strbCoberturas.toString().split("\n");
				for (String textoCobertura : arrContenido) {
					EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();
					sp = textoCobertura.split("###").length;
					if (textoCobertura.split("###").length > 1) {
						cobertura.setNombre(textoCobertura.split("###")[0].trim());
						cobertura.setSa(textoCobertura.split("###")[1].trim());
						if (sp == 3) {
							cobertura.setDeducible(textoCobertura.split("###")[2].trim());
						}
						coberturas.add(cobertura);
					}
				}

				if (!coberturas.isEmpty()) {
					model.setCoberturas(coberturas);
				}
			}
		}

	}

	private String completaTextoCobertura(String[] arrTexto, int i) {
		String texto = arrTexto[i];
		if (texto.contains("RESPONSABILIDAD CIVIL MANIOBRAS DE CARGA Y")) {
			texto = completaTextoActualConLineaSiguiente(arrTexto, i, "RESPONSABILIDAD CIVIL MANIOBRAS DE CARGA Y",
					"DESCARGA");
		} else if (texto.contains("RESPONSABILIDAD CIVIL INSTALACIONES")) {
			texto = completaTextoActualConLineaSiguiente(arrTexto, i, "RESPONSABILIDAD CIVIL INSTALACIONES",
					"SUBTERRANEAS");
		} else if (texto.contains("RESPONSABILIDAD CIVIL PRODUCTOS Y-O TRABAJOS")) {
			texto = completaTextoActualConLineaSiguiente(arrTexto, i, "RESPONSABILIDAD CIVIL PRODUCTOS Y-O TRABAJOS",
					"TERMINADOS");
		}

		return texto;
	}

	private String completaTextoActualConLineaSiguiente(String[] arrTexto, int i, String textoActual,
			String textoSiguiente) {
		String texto = arrTexto[i];
		if (!texto.contains(textoSiguiente) && arrTexto[i + 1].contains(textoSiguiente)) {
			texto = texto.replace(textoActual, textoActual + " " + textoSiguiente);
			arrTexto[i + 1] = arrTexto[i + 1].replace(textoSiguiente, "").replace(textoSiguiente + "###", "");
		}
		return texto;
	}

	private Map<String, String> obtenerApartadoDeducible(String texto) {
		int indexInicio = texto.indexOf("DEDUCIBLES");
		int indexFin = texto.indexOf("La presente póliza");
		Map<String, String> deducibles = new HashMap<>();

		if (indexInicio > -1 && indexInicio < indexFin) {
			String newContenido = texto.substring(indexInicio, indexFin).replace("@@@", "").replace("\r", "");
			String[] arrewContenido = newContenido.split("\n");
			String key = "";
			StringBuilder valor = new StringBuilder();
			for (String renglon : arrewContenido) {
				if (renglon.split("###").length == 2 && (renglon.contains("%") || renglon.contains("porciento"))) {
			
					valor = new StringBuilder();
					key = renglon.split("###")[0].toUpperCase().trim();
					valor.append(renglon.split("###")[1]);
				} else if (renglon.split("###").length == 1) {
					valor.append(" ").append(renglon);
				}

				if (key.length() > 0) {
					deducibles.put(key, fn.eliminaSpacios(valor.toString()).trim());
				}
			}
		}

		return deducibles;
	}
}
