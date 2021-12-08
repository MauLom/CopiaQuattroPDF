package com.copsis.models.axa.salud;

import java.util.ArrayList;
import java.util.List;

import com.copsis.constants.ConstantsValue;
import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraAseguradosModel;
import com.copsis.models.EstructuraCoberturasModel;
import com.copsis.models.EstructuraJsonModel;
import com.copsis.models.EstructuraRecibosModel;

public class AxaSaludModel {

	// Clases
	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
	// Varaibles
	private String contenido = "";

	public AxaSaludModel(String contenido) {
		this.contenido = contenido;
	}

	public EstructuraJsonModel procesar() {
		
		String newcontenido = "";
		String newcontenidoEx = "";
		int inicio = 0;
		int fin = 0;
		
		contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());
		contenido = contenido.replace("Datos de la Póliza", ConstantsValue.DATOS_POLIZA)
				.replace("Coberturas Amparadas", ConstantsValue.COBERTURAS_AMPARADAS)
				.replace("Familia Asegurada", ConstantsValue.FAMILIA_ASEGURADA).replace("En cumplimiento", ConstantsValue.EN_CUMPLUMIENTO)
				.replace("A B R ", "ABR ").replace("TITULAR M", "###TITULAR###M###")
				.replace("###ESPOSA F ###", "###ESPOSA###F###")
				.replace("T I T ULAR F", ConstantsValue.TITULAR_HASH)
				.replace("T ITULAR F",ConstantsValue.TITULAR_HASH)
				.replace("T I TULAR F",ConstantsValue.TITULAR_HASH)
				.replace("TITULAR F", ConstantsValue.TITULAR_HASH)
				.replace("E ###SPOSA F ","###ESPOSA###F###" )
				.replace("###HIJA F", "###HIJA###F###")
				.replace("###HIJO M", "###HIJO###M###")
				.replace(" ######TITULAR", " ###TITULAR")
				.replace("1 ###961", "1961")
				.replace("PROTECCION DENTAL SIN COSTO", "PROTECCION DENTAL###SIN COSTO").replace("T###el:", "Tel:")
				.replace("N###om###bre:", ConstantsValue.NOMBRE2).replace("D###om###icilio:", ConstantsValue.DOMICILIO).replace("C.P.", "C.P:")
				.replace("C###oberturas###Am###paradas", ConstantsValue.COBERTURAS_AMPARADAS).replace("M###oneda:", ConstantsValue.MONEDA2)
				.replace("V###igencia###:", ConstantsValue.VIGENCIA).replace("I.V###.A###:", ConstantsValue.IVA2)
				.replace("#Prim###as:", ConstantsValue.PRIMAS).replace("T###otal:", ConstantsValue.TOTAL).replace("C###.P:", "C.P:")
				.replace("A###gente:", ConstantsValue.AGENTE).replace("U###tilidad:", "Utilidad:")
				.replace("N O R T E T R E S", "NORTE TRES")
				.replace(" N U E V O P A R Q U E I N D U S T", "NUEVO PARQUE INDUSTRIAL")
				.replace("Endosos contenidos en la Póliza", ConstantsValue.ENDOSO_CONTENIDOS_POLIZA);

		try {
			modelo.setTipo(3);
			modelo.setCia(20);

			inicio = contenido.indexOf("Póliza");
			fin = contenido.indexOf("Solicitud");

			if (inicio > 0 && fin > 0 && inicio < fin) {
				newcontenido = contenido.split("Póliza:")[1].split("Solicitud")[0].replace("\r\n", "")
						.replace("@@@", "").replace("###", "").replace(ConstantsValue.CONTRATANTE2, "").replace("  ", "");
				modelo.setPoliza(newcontenido);
			}

			inicio = contenido.indexOf("POLIZA");
			fin = contenido.indexOf("Contratante");
			if (inicio > 0 && fin > 0 && inicio < fin) {
				newcontenido = contenido.substring(inicio, fin).replace("@@@", "").replace("\r", "");
				for (int i = 0; i < newcontenido.split("\n").length; i++) {
					if (newcontenido.split("\n")[i].contains("Grupo")) {
						modelo.setError("La póliza corresponde al ramo de grupo");
					}
				}
			}

			inicio = contenido.indexOf("Contratante");
			fin = contenido.indexOf("Tel:");
			if (inicio > 0 && fin > 0 && inicio < fin) {
				newcontenido = contenido.substring(inicio, fin);
				for (int i = 0; i < newcontenido.split("\n").length; i++) {

					if (newcontenido.split("\n")[i].contains(ConstantsValue.NOMBRE2)
							&& newcontenido.split("\n")[i].contains("RFC")) {
				
						String x = newcontenido.split("\n")[i].split(ConstantsValue.NOMBRE2)[1].split("RFC")[0].replace("\r", "").replace("###", "");
						if (x.contains(",")) {
							modelo.setCteNombre((x.split(",")[1] + " " + x.split(",")[0]).trim().replace("  ", " "));
						} else {
				
							if(newcontenido.split("\n")[i+1].contains("SA DE CV")) {
								modelo.setCteNombre(x.trim() +" " + newcontenido.split("\n")[i+1].split("###")[0].replace("@@@", "").trim());
							}else {
								modelo.setCteNombre(x.trim());	
							}
							
						}
						modelo.setRfc(newcontenido.split("\n")[i].split("RFC")[1].replace(":", "").replace("\r", "")
								.replace("###", ""));
					}
					if (newcontenido.split("\n")[i].contains(ConstantsValue.DOMICILIO)
							&& newcontenido.split("\n")[i].contains("C.P:")) {
						modelo.setCteDireccion((newcontenido.split("\n")[i].split(ConstantsValue.DOMICILIO)[1].split("C.P:")[0]
								+ "  " + newcontenido.split("\n")[i + 1] + "  " + newcontenido.split("\n")[i + 2])
										.replace("###", "").replace("\r", ""));
						modelo.setCp(newcontenido.split("\n")[i].split("C.P:")[1].replace("###", "").replace("\r", "").replace("\u00a0", "").trim());
					} else {
						if (newcontenido.split("\n")[i].contains("Domicilio:")) {
					
							String valor = "";
							if( newcontenido.split("\n")[i + 2].length() > 15) {
							 valor =  newcontenido.split("\n")[i + 2];
							}else {
								valor =  newcontenido.split("\n")[i + 3];
							}
							modelo.setCteDireccion((newcontenido.split("\n")[i].split("Domicilio:")[1] + "  "
									+ valor).replace("###", " ").replace("\r", "").replace("@@@", "").replace("   ", " ").trim());
						}
						if (newcontenido.split("\n")[i].contains("C.P:")) {
							modelo.setCp(newcontenido.split("\n")[i].split("C.P:")[1].replace("###", "")
									.replace("Edo:", "").replace("\r", "").replace("   ", "").replace("\u00a0", "").trim());
						}

					}
				}
			}

			inicio = contenido.indexOf(ConstantsValue.DATOS_POLIZA );
			fin = contenido.indexOf(ConstantsValue.COBERTURAS_AMPARADAS);
			if (inicio > 0 && fin > 0 && inicio < fin) {
				newcontenido = contenido.substring(inicio, fin).replace("\r", "");
				for (int i = 0; i < newcontenido.split("\n").length; i++) {
					if (newcontenido.split("\n")[i].contains("Plan") && newcontenido.split("\n")[i].contains("Póliza")
							&& newcontenido.split("\n")[i].contains(ConstantsValue.PRIMA)) {
						modelo.setPlan(
								newcontenido.split("\n")[i].split("Póliza:")[1].split(ConstantsValue.PRIMA)[0].replace("###", ""));
						modelo.setPrimaneta((fn.castBigDecimal(fn.castDouble(
								newcontenido.split("\n")[i].split("Neta:")[1].replace("###", "").replace(",", "")))));
					}

					if (newcontenido.split("\n")[i].contains(ConstantsValue.MONEDA2)
							&& newcontenido.split("\n")[i].contains(ConstantsValue.FINANCIAMIENTO)) {
						modelo.setMoneda(
								fn.moneda(newcontenido.split("\n")[i].split(ConstantsValue.MONEDA2)[1].split(ConstantsValue.FINANCIAMIENTO)[0]
										.replace("###", "")));
						modelo.setRecargo(
								(fn.castBigDecimal(fn.castDouble(newcontenido.split("\n")[i].split("Financiamiento:")[1]
										.replace("###", "").replace(",", "")))));
					} else if (newcontenido.split("\n")[i].contains(ConstantsValue.MONEDA2)) {
					if(newcontenido.split("\n")[i].split(ConstantsValue.MONEDA2)[1].contains("NACIONAL")) {
					 modelo.setMoneda(1);
					}else {
						modelo.setMoneda(
								fn.moneda(newcontenido.split("\n")[i].split(ConstantsValue.MONEDA2)[1].replace("###", "").trim()));
					}
						
					}

					if (newcontenido.split("\n")[i].contains(ConstantsValue.VIGENCIA2)
							&& newcontenido.split("\n")[i].contains("Expedición")) {
				
						if (newcontenido.split("\n")[i].split(ConstantsValue.VIGENCIA)[1].split(ConstantsValue.GASTOS)[0].replace("###", "").split("-").length == 6) {
							modelo.setVigenciaDe(fn.formatDateMonthCadena(
									newcontenido.split("\n")[i].split(ConstantsValue.VIGENCIA)[1].split(ConstantsValue.GASTOS)[0].replace("###", "").replace(" - ", "###").split("###")[0]));
							modelo.setVigenciaA(fn.formatDateMonthCadena(
									newcontenido.split("\n")[i].split(ConstantsValue.VIGENCIA )[1].split(ConstantsValue.GASTOS)[0]
											.replace("###", "").replace(" - ", "###").split("###")[1]));
							modelo.setFechaEmision(modelo.getVigenciaDe());
							modelo.setDerecho(
									(fn.castBigDecimal(fn.castDouble(newcontenido.split("\n")[i].split(ConstantsValue.EXPEDICION2)[1]
											.replace("###", "").replace(",", "")))));
						}
						if (newcontenido.split("\n")[i].split(ConstantsValue.VIGENCIA)[1].split(ConstantsValue.GASTOS)[0].replace("###", "").split("-").length == 5) {
							modelo.setVigenciaDe(fn.formatDateMonthCadena(
									newcontenido.split("\n")[i].split(ConstantsValue.VIGENCIA)[1].split(ConstantsValue.GASTOS)[0].replace("###", "").replace("Del", "").replace("al", "###").replace(" - ", "###").split("###")[0].replace(" ", "")));
							modelo.setVigenciaA(fn.formatDateMonthCadena(
									newcontenido.split("\n")[i].split(ConstantsValue.VIGENCIA)[1].split(ConstantsValue.GASTOS)[0]
											.replace("###", "").replace("al", "###").replace(" - ", "###").split("###")[1].replace(" ", "")));
							modelo.setFechaEmision(modelo.getVigenciaDe());
							modelo.setDerecho(
									(fn.castBigDecimal(fn.castDouble(newcontenido.split("\n")[i].split(ConstantsValue.EXPEDICION2)[1]
											.replace("###", "").replace(",", "")))));
						}
						
					} else if (newcontenido.split("\n")[i].contains(ConstantsValue.VIGENCIA2)
							&& newcontenido.split("\n")[i].contains(ConstantsValue.FINANCIAMIENTO)) {
						String x = newcontenido.split("\n")[i].split(ConstantsValue.VIGENCIA)[1].split(ConstantsValue.FINANCIAMIENTO)[0]
								.replace("###", "").split("-")[2];
						String x2 = newcontenido.split("\n")[i].split(ConstantsValue.VIGENCIA)[1].split(ConstantsValue.FINANCIAMIENTO)[0]
								.replace("###", "").split(x)[1].trim();
						modelo.setVigenciaDe(fn.formatDateMonthCadena(
								newcontenido.split("\n")[i].split(ConstantsValue.VIGENCIA)[1].split(ConstantsValue.FINANCIAMIENTO)[0]
										.replace("###", "").split(x)[0].trim() + "" + x).replace(" ", ""));
						modelo.setVigenciaA(fn.formatDateMonthCadena(x2.substring(1, x2.length()).replace(" ", "")).replace(" ", ""));
						modelo.setFechaEmision(modelo.getVigenciaDe());
						modelo.setRecargo(
								(fn.castBigDecimal(fn.castDouble(newcontenido.split("\n")[i].split("Financiamiento:")[1]
										.replace("###", "").replace(",", "")))));
					} else if (newcontenido.split("\n")[i].contains(ConstantsValue.VIGENCIA2)) {

						modelo.setVigenciaDe(fn.formatDateMonthCadena(newcontenido.split("\n")[i].split(ConstantsValue.VIGENCIA)[1].replace("###", "").replace(" - ", "###").trim().split("###")[0].replace(" ", "")));			
						if(newcontenido.split("\n")[i].split(ConstantsValue.VIGENCIA)[1].replace("\r", "").replace("###", "").replace("- ", "###").split("###").length > 1) {
							modelo.setVigenciaA(fn.formatDateMonthCadena(newcontenido.split("\n")[i].split(ConstantsValue.VIGENCIA)[1].replace("\r", "").replace("###", "").replace("- ", "###").split("###")[1].replace(" ", "")));
							modelo.setFechaEmision(modelo.getVigenciaDe());	
						}
						if(modelo.getVigenciaDe().length() > 5) {
						String	x = (newcontenido.split("\n")[i].split(ConstantsValue.VIGENCIA)[1].split("-")[3] +"-"+ newcontenido.split("\n")[i].split(ConstantsValue.VIGENCIA)[1].split("-")[4] +"-"+ newcontenido.split("\n")[i].split(ConstantsValue.VIGENCIA )[1].split("-")[5]).replace("###", "");
							modelo.setVigenciaA(fn.formatDateMonthCadena(x.trim()));
							modelo.setFechaEmision(modelo.getVigenciaDe());	
						}
						
					}

					if (newcontenido.split("\n")[i].contains("Pago")
							&& newcontenido.split("\n")[i].contains(ConstantsValue.IVA2)) {
						modelo.setFormaPago(fn.formaPago(
								newcontenido.split("\n")[i].split("Primas:")[1].split("Prima")[0].replace("###", "")));
						modelo.setIva(
								(fn.castBigDecimal(fn.castDouble(newcontenido.split("\n")[i + 1].split("I.V.A:")[1]
										.replace("###", "").replace(",", "")))));
					} else if (newcontenido.split("\n")[i].contains("Pago")
							&& newcontenido.split("\n")[i].contains("Expedición")) {
						modelo.setFormaPago(fn.formaPago(
								newcontenido.split("\n")[i].split("Primas:")[1].split("Gastos")[0].replace("###", "")));
						modelo.setIva(
								(fn.castBigDecimal(fn.castDouble(newcontenido.split("\n")[i + 2].split("I.V.A:")[1]
										.replace("###", "").replace(",", "")))));
						modelo.setDerecho(
								(fn.castBigDecimal(fn.castDouble(newcontenido.split("\n")[i].split(ConstantsValue.EXPEDICION2)[1]
										.replace("###", "").replace(",", "")))));
					}

					if (newcontenido.split("\n")[i].contains("Total:")) {
						modelo.setPrimaTotal((fn.castBigDecimal(fn.castDouble(
								newcontenido.split("\n")[i].split("Total:")[1].replace("###", "").replace(",", "")))));
					}

					if (newcontenido.split("\n")[i].contains("Suma Asegurada:")) {

						modelo.setSa(newcontenido.split("\n")[i].split("Suma Asegurada:")[1].replace("###", ""));
						
					}
					if (newcontenido.split("\n")[i].contains("Deducible:") && newcontenido.split("\n")[i].contains(ConstantsValue.COASASEGURO2)) {
						modelo.setDeducible(newcontenido.split("\n")[i].split("Deducible:")[1].split("Coaseguro:")[0].replace("###", ""));
						modelo.setCoaseguro(newcontenido.split("\n")[i].split("Coaseguro:")[1].replace("###", ""));
					}
					
					
				}
			}

			inicio = contenido.indexOf("Agente:");
			fin = contenido.indexOf("Utilidad:");

			if (inicio > 0 && fin > 0 && inicio < fin) {
				newcontenido = contenido.substring(inicio, fin);
				for (int i = 0; i < newcontenido.split("\n").length; i++) {
					if (newcontenido.split("\n")[i].contains("Agente:")) {
						modelo.setCveAgente(newcontenido.split("\n")[i].split("###")[1]);
					}

				}

			}

			// Proceso de Asegurados
			inicio = contenido.indexOf("Datos###del###Asegurado");
			fin = contenido.indexOf(ConstantsValue.DATOS_POLIZA);

		
			List<EstructuraAseguradosModel> asegurados = new ArrayList<>();

			if (inicio > 0 && fin > 0 && inicio < fin) {
				EstructuraAseguradosModel asegurado = new EstructuraAseguradosModel();
				newcontenido = contenido.substring(inicio, fin);
				for (int i = 0; i < newcontenido.split("\n").length; i++) {

					if (newcontenido.split("\n")[i].contains("Nombre:")
							&& newcontenido.split("\n")[i].contains("Parentesco:")) {
						newcontenidoEx = newcontenido.split("\n")[i].split("Nombre:")[1].split("Parentesco")[0];
						if (newcontenido.contains(",")) {
							newcontenidoEx = (newcontenidoEx.split(",")[1] + " " + newcontenidoEx.split(",")[0])
									.replace("###", "");
							asegurado.setNombre(newcontenidoEx);
						} else {
							newcontenidoEx = newcontenidoEx.replace("###", "");
							asegurado.setNombre(newcontenidoEx);
						}
						asegurado.setParentesco(fn.parentesco(newcontenido.split("Parentesco:")[1]));
					}
					if (newcontenido.split("\n")[i].contains("Nacimiento:")
							&& newcontenido.split("\n")[i].contains("Edad:")) {
						newcontenidoEx = newcontenido.split("\n")[i].split("Nacimiento:")[1].split("Edad:")[0]
								.replace("###", "");
						asegurado.setNacimiento(fn.formatDateMonthCadena(newcontenidoEx));
					}
				}
				asegurados.add(asegurado);
				modelo.setAsegurados(asegurados);
			}

			// proceso de Asegurados version 2
			if (modelo.getAsegurados().isEmpty()) {
				inicio = contenido.indexOf(ConstantsValue.FAMILIA_ASEGURADA);
				fin = contenido.indexOf(ConstantsValue.EN_CUMPLUMIENTO);
		
				if (fin == -1) {
					fin = contenido.indexOf(ConstantsValue.ENDOSO_CONTENIDOS_POLIZA);
				}
			
				

				if (inicio > 0 && fin > 0 && inicio < fin) {
					newcontenido =  contenido.substring(inicio, fin).replace(" - ", "-").replace("### ###", "###");
		
					for (int i = 0; i < newcontenido.split("\n").length; i++) {
						EstructuraAseguradosModel asegurado = new EstructuraAseguradosModel();
						System.out.println( newcontenido.split("\n")[i]);
						if (newcontenido.split("\n")[i].split("-").length > 2) {
							
					                                                       								
							if (newcontenido.split("\n")[i].split("###")[newcontenido.split("\n")[i].split("###").length
									- 1].trim().split("-").length > 3) {
								String numero = newcontenido.split("\n")[i]
										.split("###")[newcontenido.split("\n")[i].split("###").length - 1].trim()
												.split("-")[2].split(" ")[0];
								asegurado.setNacimiento(fn.formatDateMonthCadena(newcontenido.split("\n")[i]
										.split("###")[newcontenido.split("\n")[i].split("###").length - 1].trim()
												.split(numero)[0].replace(" ", "")
										+ numero));
								asegurado.setAntiguedad(fn.formatDateMonthCadena(newcontenido.split("\n")[i]
										.split("###")[newcontenido.split("\n")[i].split("###").length - 1].trim()
												.split(" ")[4]));
							}
							else {
				
					
								if( newcontenido.split("\n")[i]
										.split("###")[newcontenido.split("\n")[i].split("###").length - 1].trim().length() > 5 ) {
									asegurado.setAntiguedad(fn.formatDateMonthCadena(newcontenido.split("\n")[i]
											.split("###")[newcontenido.split("\n")[i].split("###").length - 1].trim()));
									if (newcontenido.split("\n")[i]
											.split("###")[newcontenido.split("\n")[i].split("###").length - 3].trim()
													.contains("-")) {
										asegurado.setNacimiento(fn.formatDateMonthCadena(newcontenido.split("\n")[i]
												.split("###")[newcontenido.split("\n")[i].split("###").length - 3].trim()));
									} else {
										asegurado.setNacimiento(fn.formatDateMonthCadena(newcontenido.split("\n")[i]
												.split("###")[newcontenido.split("\n")[i].split("###").length - 4].trim()));
									}
									
								}
								else {
									asegurado.setAntiguedad(fn.formatDateMonthCadena(newcontenido.split("\n")[i]
											.split("###")[newcontenido.split("\n")[i].split("###").length - 2].trim()));

									if (newcontenido.split("\n")[i].split("###")[newcontenido.split("\n")[i].split("###").length - 4].trim().contains("-")) {

										if(newcontenido.split("\n")[i].split("###").length > 3) {
							
											if(newcontenido.split("\n")[i].split("###")[newcontenido.split("\n")[i].split("###").length - 4].contains("-")){
												String xpcon = newcontenido.split("\n")[i].split("###")[newcontenido.split("\n")[i].split("###").length - 4];
												asegurado.setNacimiento(fn.formatDateMonthCadena(xpcon.split(xpcon.split(" ")[xpcon.split(" ").length -1])[0].replace(" ", "")));
											}else {																			
													asegurado.setNacimiento(fn.formatDateMonthCadena(newcontenido.split("\n")[i].split("###")[newcontenido.split("\n")[i].split("###").length - 3].trim()));	
											}	
										
										}
										
									} 
									else {
										if(newcontenido.split("\n")[i]
												.split("###")[newcontenido.split("\n")[i].split("###").length - 4].trim().contains("-")) {
											asegurado.setNacimiento(fn.formatDateMonthCadena(newcontenido.split("\n")[i].split("###")[newcontenido.split("\n")[i].split("###").length - 4].trim()));
											
										}else {
											asegurado.setNacimiento(fn.formatDateMonthCadena(newcontenido.split("\n")[i]
													.split("###")[newcontenido.split("\n")[i].split("###").length - 5].replace(" ", "").trim()));
										}
										
									}
									
								}
					

							if (newcontenido.split("\n")[i].split(newcontenido.split("\n")[i]
									.split("###")[newcontenido.split("\n")[i].split("###").length - 4])[0]
											.split("###")[0].contains(",")) {
								String x = newcontenido.split("\n")[i].split(newcontenido.split("\n")[i]
										.split("###")[newcontenido.split("\n")[i].split("###").length - 4])[0]
												.split("###")[0];
								
							
								
								asegurado.setNombre((x.split(",")[1] + " " + x.split(",")[0]).replace("  ", " "));
								String x2 = newcontenido.split("\n")[i].split(newcontenido.trim().split("\n")[i]
										.split("###")[newcontenido.split("\n")[i].split("###").length - 4])[0].replace("######", "###").trim();
								


								if (x2.split("###").length > 1) {
						
									asegurado.setSexo(fn.sexo(x2.split("###")[2].trim()).booleanValue() ? 1 : 0);
									asegurado.setParentesco(fn.parentesco(x2.split("###")[1].trim()));
								} 
								else {
									if (newcontenido.split("\n")[i].contains("TITULAR")) {
										asegurado.setParentesco(1);
										asegurado.setSexo(
												fn.sexo(newcontenido.split("\n")[i].split("###")[1].split(" ")[1].trim()
														.toLowerCase()).booleanValue() ? 1 : 0);

									}
								}

							} else {

								if (newcontenido.split("\n")[i].contains(",")) {
									asegurado.setNombre(newcontenido.split("\n")[i].split("###")[0].split(",")[1] + ""
											+ newcontenido.split("\n")[i].split("###")[0].split(",")[0]);
									asegurado.setParentesco(
											fn.parentesco(newcontenido.split("\n")[i].split("###")[1].trim()));
									asegurado.setSexo(
											fn.sexo(newcontenido.split("\n")[i].split("###")[2].trim()).booleanValue() ? 1 : 0);

								}
							}
							}
							asegurados.add(asegurado);
						}

					}

					modelo.setAsegurados(asegurados);
				}

			}

			// proceso de Asegurados version 2
			if (modelo.getAsegurados().isEmpty()) {
				inicio = contenido.indexOf(ConstantsValue.FAMILIA_ASEGURADA);
				fin = contenido.indexOf(ConstantsValue.EN_CUMPLUMIENTO);
				if (fin == -1) {
					fin = contenido.indexOf(ConstantsValue.ENDOSO_CONTENIDOS_POLIZA);
				}
				if (inicio > 0 && fin > 0 && inicio < fin) {
					newcontenido = contenido.substring(inicio, fin);
					for (int i = 0; i < newcontenido.split("\n").length; i++) {
			
						EstructuraAseguradosModel asegurado = new EstructuraAseguradosModel();
	
						if (newcontenido.split("\n")[i].split("-").length > 2) {
							asegurado.setAntiguedad(fn.formatDateMonthCadena(newcontenido.split("\n")[i]
									.split("###")[newcontenido.split("\n")[i].split("###").length - 1].trim()));
							asegurado.setNacimiento(fn.formatDateMonthCadena(newcontenido.split("\n")[i]
									.split("###")[newcontenido.split("\n")[i].split("###").length - 4].trim()));
							if (newcontenido.split("\n")[i].split(newcontenido.split("\n")[i]
									.split("###")[newcontenido.split("\n")[i].split("###").length - 4])[0]
											.split("###")[0].contains(",")) {
								String x = newcontenido.split("\n")[i].split(newcontenido.split("\n")[i]
										.split("###")[newcontenido.split("\n")[i].split("###").length - 4])[0]
												.split("###")[0];
								asegurado.setNombre(x.split(",")[1] + " " + x.split(",")[0]);
								String x2 = newcontenido.split("\n")[i].split(newcontenido.trim().split("\n")[i]
										.split("###")[newcontenido.split("\n")[i].split("###").length - 4])[0].trim();
								if (x2.split("###").length > 1) {

									asegurado.setSexo(fn.sexo(x2.split("###")[1].split(" ")[1].trim()).booleanValue() ? 1 : 0);

									asegurado.setParentesco(fn.parentesco(x2.split("###")[1].trim()));
								}
							}
							asegurados.add(asegurado);
						}
					}
					asegurados.get(0).setNombre(modelo.getCteNombre());
					asegurados.get(0).setParentesco(1);
					modelo.setAsegurados(asegurados);
				}

			}

//			/**proceso para cober*/

			List<EstructuraCoberturasModel> coberturas = new ArrayList<>();

			inicio = contenido.indexOf("Coberturas###Amparadas");
			fin = contenido.indexOf("Advertencia:");
			if (fin == -1) {
				fin = contenido.indexOf("Se hace del conocimient");
			}

			if (inicio > 0 && fin > 0 && inicio < fin) {
				newcontenido = contenido.substring(inicio, fin).replace("@@@", "");
				for (int i = 0; i < newcontenido.split("\n").length; i++) {
					
					if (!newcontenido.split("\n")[i].contains("Coberturas###Amparadas")) {
						int sp = newcontenido.split("\n")[i].split("###").length;
						if (sp == 2 &&  newcontenido.split("\n")[i].length() > 20) {
						
							EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();
							cobertura.setNombre(newcontenido.split("\n")[i].split("###")[0]);
							cobertura.setSa(newcontenido.split("\n")[i].split("###")[1].replace("\r", ""));
							coberturas.add(cobertura);
					} 

					}
				}
				modelo.setCoberturas(coberturas);
			}

			List<EstructuraRecibosModel> recibos = new ArrayList<>();
			EstructuraRecibosModel recibo = new EstructuraRecibosModel();
			if(modelo.getFormaPago() == 1) {
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
				recibo.setIva(modelo.getDerecho());

				recibo.setPrimaTotal(modelo.getPrimaTotal());
				recibo.setAjusteUno(modelo.getAjusteUno());
				recibo.setAjusteDos(modelo.getAjusteDos());
				recibo.setCargoExtra(modelo.getCargoExtra());
				recibos.add(recibo);

			}
		
			modelo.setRecibos(recibos);

			return modelo;
		} catch (Exception ex) {
			modelo.setError(
					AxaSaludModel.this.getClass().getTypeName() + " | " + ex.getMessage() + " | " + ex.getCause());
			return modelo;
		}
	}



}
