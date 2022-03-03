package com.copsis.models.banorte;

import java.util.ArrayList;
import java.util.List;

import com.copsis.constants.ConstantsValue;
import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraAseguradosModel;
import com.copsis.models.EstructuraCoberturasModel;
import com.copsis.models.EstructuraJsonModel;
import com.copsis.models.ReplaceModel;

public class BanorteSaludModel {
	// Clases
	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
	// Varaibles
	private String contenido = "";
	private String newcontenido = "";
	private String recibosText = "";
	private String coberturas;

	public BanorteSaludModel(String contenido, String recibos, String coberturas) {
		this.contenido = contenido;
		this.recibosText = recibos;
		this.coberturas = coberturas;
	}

	public EstructuraJsonModel procesar() {
		contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());
		contenido = contenido.replace("I.V.A.", ConstantsValue.IVA3)
				.replace("N o m b r e y A p e l l i d o C o m p l e t o", "Nombre y Apellido Completo")
				.replace("Si### el### contenido", "Si el contenido")
				.replace("CUADRO###DE###ESPEC###I###F###I###CAC###IONES", "CUADRO DE ESPECIFICACIONES")
				.replace("A r t í c u l o", "Artículo")
				.replace("SE###COBRARÁ###DOBLE###DEDUC###I###BLE###Y###COASEGURO", "SE COBRARÁ DOBLE DEDUCIBLE Y COASEGURO")
				.replace("LA###LEGI###SLAC###IÓN###C###I###TADA", "LA LEGISLACIÓN CITADA")
				.replace("EN###CUMPL###IMI###ENTO###A###LO###D###I###SPUESTO", "EN CUMPLIMIENTO A LO DISPUESTO")
				.replace("H###I###JA#", "HIJA")
				.replace("T###I###TULAR", "TITULAR");
		StringBuilder resultado = new StringBuilder();
		int inicio = 0;
		int fin = 0;

		try {
			// tipo
			modelo.setTipo(3);

			// cia
			modelo.setCia(35);
			System.err.println("Longitud coberturas "+coberturas.length());
			// Datos Generales
			inicio = contenido.indexOf("GASTOS MÉDICOS MAYORES");
			fin = contenido.indexOf("NOMBRE DEL ASEGURADO");

			if (inicio > 0 && fin > 0 && inicio < fin) {
				newcontenido = contenido.substring(inicio, fin).replace("\r", "").replace("@", "");
				for (int i = 0; i < newcontenido.split("\n").length; i++) {
					if (newcontenido.split("\n")[i].contains("CONTRATANTE")
							&& newcontenido.split("\n")[i].contains("PÓLIZA")) {
						if (newcontenido.split("\n")[i + 1].contains("apellido")) {
							modelo.setPoliza(newcontenido.split("\n")[i + 1]
									.split("###")[newcontenido.split("\n")[i + 1].split("###").length - 1].trim());
							modelo.setCteNombre(newcontenido.split("\n")[i + 2]
									.split("###")[newcontenido.split("\n")[i + 2].split("###").length - 4]
											.replace("10", "").trim());
						} else if (newcontenido.split("\n")[i + 1].contains("Apellido")) {
							modelo.setPoliza(newcontenido.split("\n")[i + 1]
									.split("###")[newcontenido.split("\n")[i + 1].split("###").length - 1].trim());
							String x = newcontenido.split("\n")[i + 1]
									.split("###")[newcontenido.split("\n")[i + 1].split("###").length - 4]
											.replace("Nombre y Apellido Completo :", "").trim();
							modelo.setCteNombre(x);
						} else {
							modelo.setPoliza(newcontenido.split("\n")[i + 1]
									.split("###")[newcontenido.split("\n")[i + 1].split("###").length - 1].trim());

							if (newcontenido.split("\n")[i + 1]
									.split("###")[newcontenido.split("\n")[i + 1].split("###").length - 3]
											.length() > 20) {
								modelo.setCteNombre(newcontenido.split("\n")[i + 1]
										.split("###")[newcontenido.split("\n")[i + 1].split("###").length - 3]
												.replace("10", "").trim());
							} else {
								modelo.setCteNombre(newcontenido.split("\n")[i + 1]
										.split("###")[newcontenido.split("\n")[i + 1].split("###").length - 4]
												.replace("10", "").trim());

							}

						}
					}

					if (newcontenido.split("\n")[i].contains("AGENTE")
							&& newcontenido.split("\n")[i + 1].contains(ConstantsValue.DOMICILIO)) {

						if (newcontenido.split("\n")[i + 1].split("###").length > 2) {
							modelo.setCveAgente(newcontenido.split("\n")[i + 1].split(ConstantsValue.DOMICILIO)[1]
									.split("MATRIZ")[0].replace("###", ""));
						} else {
							modelo.setCveAgente(newcontenido.split("\n")[i + 2].split("###")[1].replace("###", ""));
						}

					}

					if (newcontenido.split("\n")[i].contains("C.P.")) {
						if (newcontenido.split("\n")[i].split("C.P.")[1].split("###").length > 1) {
							modelo.setCp(newcontenido.split("\n")[i].split("C.P.")[1].split("###")[0]);
						} else {
							modelo.setCp(newcontenido.split("\n")[i].split("C.P.")[1].split(",")[0]);
						}

					}
					if (newcontenido.split("\n")[i].contains("EMISIÓN") && modelo.getCp().length() == 0
							&& fn.isvalidCp(newcontenido.split("\n")[i + 1].split("###")[0]).booleanValue()) {

						modelo.setCp(newcontenido.split("\n")[i + 1].split("###")[0]);
					}
					if (newcontenido.split("\n")[i].contains("PLAN")) {

						if (newcontenido.split("\n")[i + 1].contains(ConstantsValue.PESOS_MAYUS)
								|| newcontenido.split("\n")[i + 1].contains(ConstantsValue.NACIONAL_MAYUS)) {
							modelo.setPlan(newcontenido.split("\n")[i + 1]
									.split("###")[newcontenido.split("\n")[i + 1].split("###").length - 1]);
						} else if (newcontenido.split("\n")[i + 2].contains(ConstantsValue.NACIONAL_MAYUS)) {
							String valor = newcontenido.split("\n")[i + 2].replace(" ", "###");
							modelo.setPlan(valor.split("###")[valor.split("###").length - 1]);
						}

					}

					if (newcontenido.split("\n")[i].contains(ConstantsValue.DOMICILIO)) {

						if (newcontenido.split("\n")[i + 1].contains(ConstantsValue.MONEDA_MAYUS)) {
							resultado.append(newcontenido.split("\n")[i + 1].split(ConstantsValue.MONEDA_MAYUS)[0]
									.replace("###", ""));
						} else {
							resultado.append(newcontenido.split("\n")[i + 1].split("###")[0]);
						}
						if (newcontenido.split("\n")[i + 2].contains("C.P.")) {
							resultado.append(" ").append(newcontenido.split("\n")[i + 2].split("C.P.")[1]);

						} else if (newcontenido.split("\n")[i + 2].contains(ConstantsValue.MONEDA_MAYUS)) {
							resultado.append(" ").append(newcontenido.split("\n")[i + 2].split(ConstantsValue.MONEDA_MAYUS)[0]
									.replace("###", ""));
							
						} else {
							resultado.append(" ").append(newcontenido.split("\n")[i + 2]);

						}

						if (newcontenido.split("\n")[i + 3].contains(ConstantsValue.NACIONAL_MAYUS)) {
							resultado.append(newcontenido.split("\n")[i + 3].split(ConstantsValue.NACIONAL_MAYUS)[0]) ;
							
							modelo.setMoneda(1);

							if (newcontenido.split("\n")[i + 3].split(ConstantsValue.NACIONAL_MAYUS)[1]
									.replace(ConstantsValue.TOTAL_MAYUS , "").trim().split("###").length > 2) {
								modelo.setFormaPago(fn.formaPago(
										newcontenido.split("\n")[i + 3].split(ConstantsValue.NACIONAL_MAYUS)[1]
												.split("###")[1].replace(ConstantsValue.TOTAL_MAYUS, "").trim()));
							} else {
								modelo.setFormaPago(fn.formaPago(
										newcontenido.split("\n")[i + 3].split(ConstantsValue.NACIONAL_MAYUS)[1]
												.replace(ConstantsValue.TOTAL_MAYUS, "").trim()));
							}

						}
						if (newcontenido.split("\n")[i + 3].contains(ConstantsValue.PESOS_MAYUS)) {
							resultado.append(" ").append(newcontenido.split("\n")[i + 3].split(ConstantsValue.PESOS_MAYUS)[0].replace("###", ""));
							modelo.setMoneda(1);
							modelo.setFormaPago(fn.formaPago(
									newcontenido.split("\n")[i + 3].split(ConstantsValue.PESOS_MAYUS)[1].split("###")[1]
											.replace("###", "").trim()));
						}
						modelo.setCteDireccion(resultado.toString().replace("###", "").trim());
					}

					if (newcontenido.split("\n")[i].contains(ConstantsValue.RFC)
							&& newcontenido.split("\n")[i].contains("VIGENCIA")) {
						modelo.setRfc(newcontenido.split("\n")[i].split(ConstantsValue.RFC)[1].split("VIGENCIA")[0]
								.replace("###", "").trim());

					}

					if (newcontenido.split("\n")[i].contains(ConstantsValue.RFC)
							&& newcontenido.split("\n")[i].contains("DESDE")) {
						modelo.setRfc(newcontenido.split("\n")[i].split(ConstantsValue.RFC)[1].split("DESDE")[0]
								.replace("###", "").trim());
					}

					if (newcontenido.split("\n")[i].contains("Electrónico:")
							&& newcontenido.split("\n")[i].split("-").length > 1) {
						resultado = new StringBuilder();
						resultado.append(newcontenido.split("\n")[i].split("Electrónico:")[1].strip().trim().replace(" ",
								"###"));
						if (resultado.toString().split("###")[0].contains("-")) {
							modelo.setVigenciaDe(fn.formatDateMonthCadena(resultado.toString().split("###")[0]));
							modelo.setVigenciaA(fn.formatDateMonthCadena(resultado.toString().split("###")[1]));
						} else {
							modelo.setVigenciaDe(fn.formatDateMonthCadena(resultado.toString().split("###")[1]));
							modelo.setVigenciaA(fn.formatDateMonthCadena(resultado.toString().split("###")[2]));
						}

					}
					if (newcontenido.split("\n")[i].contains(ConstantsValue.RFC)
							&& newcontenido.split("\n")[i].split("-").length > 3) {
						if (newcontenido.split("\n")[i].split("###")[1].contains("-")) {
							modelo.setRfc(
									newcontenido.split("\n")[i].split("###")[0].replace(ConstantsValue.RFC, "").trim());
							resultado = new StringBuilder();
							resultado.append(newcontenido.split("\n")[i].split("###")[1].trim().replace(" ", "###"));
							modelo.setVigenciaDe(fn.formatDateMonthCadena(resultado.toString().split("###")[0]));
							modelo.setVigenciaA(fn.formatDateMonthCadena(resultado.toString().split("###")[1]));
						} else {
							modelo.setRfc(newcontenido.split("\n")[i].split("###")[1]);
							modelo.setVigenciaDe(fn.formatDateMonthCadena(newcontenido.split("\n")[i].split("###")[2]));
							modelo.setVigenciaA(fn.formatDateMonthCadena(newcontenido.split("\n")[i].split("###")[3]));
						}
					}

					if (modelo.getVigenciaA().length() == 0 && modelo.getVigenciaDe().length() == 0
							&& newcontenido.split("\n")[i].split("-").length > 3) {

						String x = newcontenido.split("\n")[i].trim().replace(" ", "###");
						modelo.setVigenciaDe(fn.formatDateMonthCadena(x.split("###")[0]));
						modelo.setVigenciaA(fn.formatDateMonthCadena(x.split("###")[1]));

					}
				}

			}

			inicio = contenido.indexOf("Clave del Agente:");
			fin = contenido.indexOf("En testimonio de");
			if (inicio > 0 && fin > 0 && inicio < fin) {
				newcontenido = contenido.substring(inicio, fin).replace("\r", "").replace("@", "");
				for (int i = 0; i < newcontenido.split("\n").length; i++) {
					if (newcontenido.split("\n")[i].contains("Clave del Agente:")) {
						modelo.setAgente(newcontenido.split("\n")[i + 1].split(modelo.getCveAgente())[0].trim());
					}
				}
			}

			inicio = contenido.indexOf("Prima Neta");
			fin = contenido.indexOf("Si el contenido");

			if (inicio > 0 && fin > 0 && inicio < fin) {
				newcontenido = contenido.substring(inicio, fin).replace("\r", "").replace("@", "");
				for (int i = 0; i < newcontenido.split("\n").length; i++) {
					if (newcontenido.split("\n")[i].contains("Prima Neta")) {
						modelo.setPrimaneta(fn.castBigDecimal(fn.castDouble(
								newcontenido.split("\n")[i].split("Prima Neta Anual")[1].split("###")[1].trim())));
					}
					if (newcontenido.split("\n")[i].contains(ConstantsValue.FRACCIONADO)
							&& newcontenido.split("\n")[i].contains("Especificaciones")) {
						modelo.setRecargo(fn.castBigDecimal(fn.castDouble(
								newcontenido.split("\n")[i].split(ConstantsValue.FRACCIONADO)[1].replace("###", "").trim())));
					}
					if (newcontenido.split("\n")[i].contains("Financiamiento")
							&& newcontenido.split("\n")[i].contains("Especificaciones")) {
						modelo.setRecargo(fn.castBigDecimal(fn.castDouble(newcontenido.split("\n")[i].split("Pago")[1]
								.replace(ConstantsValue.FRACCIONADO, "").replace("###", "").trim())));
					}

					if (newcontenido.split("\n")[i].contains("Generales")
							&& newcontenido.split("\n")[i].contains("Expedición")) {
						modelo.setDerecho(fn.castBigDecimal(fn
								.castDouble(newcontenido.split("\n")[i].split("Póliza")[1].replace("###", "").trim())));
					}
					if (newcontenido.split("\n")[i].contains("Identificación")
							&& newcontenido.split("\n")[i].contains("Expedición")) {
						modelo.setDerecho(fn.castBigDecimal(fn
								.castDouble(newcontenido.split("\n")[i].split("Póliza")[1].replace("###", "").trim())));
					}

					if (newcontenido.split("\n")[i].contains("Pago")
							&& newcontenido.split("\n")[i].contains(ConstantsValue.IVA3)) {
						modelo.setIva(fn.castBigDecimal(fn.castDouble(
								newcontenido.split("\n")[i].split(ConstantsValue.IVA3)[1].replace("###", "").trim())));
					}

					if ((newcontenido.split("\n")[i].contains("Endosos")
							&& newcontenido.split("\n")[i].contains(ConstantsValue.TOTAL2))
							|| newcontenido.split("\n")[i].contains(ConstantsValue.TOTAL2)) {

						modelo.setPrimaTotal(fn.castBigDecimal(
								fn.castDouble(newcontenido.split("\n")[i].split(ConstantsValue.TOTAL2)[1]
										.replace("###", "").trim())));

					}
				}

			}

			if (modelo.getVigenciaDe().length() > 0) {
				modelo.setFechaEmision(modelo.getVigenciaDe());
			}

			// PROCESO DE ASEGURADOS
			inicio = contenido.indexOf("NOMBRE DEL ASEGURADO");
			fin = contenido.indexOf("seguro de gastos médicos,");

			if (inicio > 0 && fin > 0 && inicio < fin) {
				List<EstructuraAseguradosModel> asegurados = new ArrayList<>();
				newcontenido = contenido.substring(inicio, fin).replace("\r", "").replace("@", "");
				for (int i = 0; i < newcontenido.split("\n").length; i++) {
					EstructuraAseguradosModel asegurado = new EstructuraAseguradosModel();
					if (newcontenido.split("\n")[i].split("-").length > 2) {
						if (newcontenido.split("\n")[i].split("###")[1].contains("-")) {
							asegurado.setNacimiento(
									fn.formatDateMonthCadena(newcontenido.split("\n")[i].split("###")[1]));
							asegurado.setNombre(newcontenido.split("\n")[i].split("###")[0]);
						} else {
							String x = newcontenido.split("\n")[i].split("###")[0].replace(" ", "###");
							asegurado
									.setNacimiento(fn.formatDateMonthCadena(x.split("###")[x.split("###").length - 1]));
							asegurado.setNombre(newcontenido.split("\n")[i].split("###")[0]
									.split(x.split("###")[x.split("###").length - 1])[0]);
						}
						asegurados.add(asegurado);
					}
				}
				modelo.setAsegurados(asegurados);
			}

			obtenerCoberturas(contenido, modelo);
			return modelo;
		} catch (Exception ex) {
			modelo.setError(BanorteSaludModel.this.getClass().getTypeName() + " - catch:" + ex.getMessage() + " | "
					+ ex.getCause());
			return modelo;
		}

	}
	
	private void obtenerCoberturas(String textoContenido,EstructuraJsonModel model) {
		textoContenido = fn.remplazarMultiple(textoContenido, reemplazoCoberturas());
		StringBuilder contenidoCoberturas = new StringBuilder();
		System.err.println(" hola ".trim()+ "         ".trim()+"?");
		System.out.println("Long "+ textoContenido.split("CUADRO DE ESPECIFICACIONES").length);
		String coberturasApartado = "";
		for(int i =1; i<textoContenido.split("CUADRO DE ESPECIFICACIONES").length;i++) {
			coberturasApartado = textoContenido.split("CUADRO DE ESPECIFICACIONES")[i];
			if(coberturasApartado.contains("LA LEGISLACIÓN CITADA")) {
				coberturasApartado = coberturasApartado.split("LA LEGISLACIÓN CITADA")[0];
			}else if(coberturasApartado.contains("SE COBRARÁ DOBLE DEDUCIBLE Y COASEGURO")) {
				coberturasApartado = coberturasApartado.split("SE COBRARÁ DOBLE DEDUCIBLE Y COASEGURO")[0];
			}else if(coberturasApartado.contains("EN CUMPLIMIENTO A LO DISPUESTO")) {
				coberturasApartado = coberturasApartado.split("EN CUMPLIMIENTO A LO DISPUESTO")[0];
			}else if(coberturasApartado.contains("Artículo")) {
				coberturasApartado = coberturasApartado.split("Artículo")[0];
			}
			System.out.println("Cober length "+coberturasApartado.length());

			if(coberturasApartado.trim().length() > 0) {
				contenidoCoberturas.append(" ").append(coberturasApartado);
				//System.out.println(coberturasApartado);
				System.err.println("********************");
			}
		}
		coberturasApartado = contenidoCoberturas.toString();
		contenidoCoberturas = new StringBuilder();
		contenidoCoberturas.append(coberturasApartado);
		String[] arrContenido = contenidoCoberturas.toString().split("\n");
		int valores = 0;
		List<EstructuraCoberturasModel> listCoberturas = new ArrayList<>();
		
		for(int i=0; i< arrContenido.length;i++) {
			arrContenido[i] = completaTextoCobertura(arrContenido, i);
			arrContenido[i] = fn.gatos(arrContenido[i]);
			
		}
		
		for(int i=0;i< arrContenido.length;i++) {
			valores = arrContenido[i].split("###").length;
			EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();
			if(valores == 2 && arrContenido[i].split("###")[0].length() >40) {
				cobertura.setNombre(arrContenido[i].split("###")[0]);
				cobertura.setSa(arrContenido[i].split("###")[1]);
				listCoberturas.add(cobertura);
			}
		}
		
		if(listCoberturas.size() > 0) {
			modelo.setCoberturas(listCoberturas);
		}
		System.err.println("====\n"+contenidoCoberturas+"\n==========");
		
	}
	
	private List<ReplaceModel> reemplazoCoberturas(){
		List<ReplaceModel> remplazoDeA = new ArrayList<>();
		remplazoDeA.add(new ReplaceModel("@@@",""));
		remplazoDeA.add(new ReplaceModel("\r",""));
		remplazoDeA.add(new ReplaceModel("-###EMERGENC###I###A###EN###EL###EXTRANJERO", "EMERGENCIA EN EL EXTRANJERO"));
		remplazoDeA.add(new ReplaceModel("-###COBERTURA###TOTAL###EXTRANJERO", "COBERTURA TOTAL EXTRANJERO"));
		remplazoDeA.add(new ReplaceModel("-###COBERTURA###DE###EL###IMI###NAC###IÓN###DE###DEDUC###I###BLE###POR###ACC###I###DENTE", "-COBERTURA DE ELIMINACIÓN DE DEDUCIBLE POR ACCIDENTE"));
		remplazoDeA.add(new ReplaceModel("-###COBERTURA###I###NDEMN###I###ZAC###IÓN###POR###ENFERMEDAD###GRAVE", "-COBERTURA DE INDEMNIZACIÓN POR ENEFERMEDAD GRAVE"));
		remplazoDeA.add(new ReplaceModel("-###COBERTURA###DE###V###I###S###IÓN###I###NCREMENTAL###OPERADOR###DENTEGRA", "-COBERTURA DE VISIÓN INCREMENTAL OPERADOR DENTEGRA"));
		
		remplazoDeA.add(new ReplaceModel("-###I###NCREMENTO###AL###CATÁLOGO","-INCREMENTO AL CATÁLOGO"));
		remplazoDeA.add(new ReplaceModel("-###BASE###DE###HONORAR###IOS###QU###I###RÚRGI###COS###ZONA###1", "-BASE DE HONORARIOS QUIRÚRGICOS ZONA 1"));
		remplazoDeA.add(new ReplaceModel("ZONA###2", "ZONA 2"));
		remplazoDeA.add(new ReplaceModel("ZONA###3", "ZONA 3"));
		remplazoDeA.add(new ReplaceModel("ZONA###4", "ZONA 4"));
		remplazoDeA.add(new ReplaceModel("ZONA###5", "ZONA 5"));

		remplazoDeA.add(new ReplaceModel("-###GASTOS###POR###PARTO###O###CESÁREA###DEL###TITULAR###,\n"
				+ "\r\n"
				+ "CÓNYUGE###O###HIJA##ASEGURADA###,###CON###PER###IODO###DE\r\n"
				+ "\r\n"
				+ "ESPERA###DE###10###MESES", "GASTOS POR PARTO O CESÁREA DEL TITULAR, CÓNYUGE O HIJA ASEGURADA, CON PERIODO DE ESPERA DE 10 MESES"));
		remplazoDeA.add(new ReplaceModel("-###T###I###PO###DE###HOSP###I###TAL###TODOS###LOS###HOSP###I###TALES", "-TIPO DE HOSPITAL###TODOS LOS HOSPITALES"
				+ ""));
		remplazoDeA.add(new ReplaceModel("-###HONORAR###IOS###POR###V###I###S###I###TA###I###NTRAHOSP###I###TALAR###I###A###CATÁLOGO", "-HONORARIOS POR VISITA INTRAHOSPITALARIA###CATÁLOGO"));
		remplazoDeA.add(new ReplaceModel("-###HONORAR###IOS###POR###CONSULTA###CATÁLOGO", "-HONORARIOS POR CONSULTA###CATÁLOGO"));
		remplazoDeA.add(new ReplaceModel("-###HONORAR###IOS###POR###ENFERMERA###MÁX###IMO###3###TURNOS", "-HONORARIOS POR ENFERMERA MÁXIMO 3 TURNOS"));
		remplazoDeA.add(new ReplaceModel("POR###D###Í###A###DURANTE###30###D###Í###AS###CATÁLOGO", "POR DÍA DURANTE 30 DÍAS###CATÁLOGO"));
		remplazoDeA.add(new ReplaceModel("-###HAB###I###TAC###IÓN###HOSP###I###TAL###PR###I###VADO###ESTÁNDAR", "HABITACIÓN HOSPITAL##PRIVADO ESTÁNDAR"));

		remplazoDeA.add(new ReplaceModel("-###SALA###DE###OPERAC###IÓN###Y###RECUPERAC###IÓN###,###MED###I###CAMENTO,###I###NCLU###I###DO", "-SALA DE OPERACIÓN Y RECUPERACIÓN, MEDICAMENTO,###INCLUIDO"));
		remplazoDeA.add(new ReplaceModel("CONSUMO###DE###OX###ÍGENO,###SERV###I###C###IO###DE###TERAP###I###A###,", "CONSUMO DE OXÍGENO, SERVICIO DE TERAPIA,"));
		remplazoDeA.add(new ReplaceModel("TRATAMI###ENTO###DE###D###I###ÁL###I###S###I###S###Y###TRANSFUS###IONES###DE###SANGRE", "TRATAMIENTO DE DIÁLISIS Y TRANSFUSIONES DE SANGRE"));
		remplazoDeA.add(new ReplaceModel("-###RENTA###DE###APARATOS###ORTOPED###I###COS###Y###PRÓTES###I###S###I###NCLU###I###DO", "-RENTA DE APARATOS ORTOPEDICOS Y PRÓTESIS###INCLUIDO"));
		remplazoDeA.add(new ReplaceModel("-###AMBULANC###I###A###AÉREA###I###NCLU###I###DO", "-AMBULANCIA AÉREA###INCLUIDO"));
		remplazoDeA.add(new ReplaceModel("-###AMBULANC###I###A###TERRESTRE###I###NCLU###I###DO", "-AMBULANCIA TERRESTRE###INCLUIDO"));
		remplazoDeA.add(new ReplaceModel("-###RE###I###NSTALAC###IÓN###AUTOMÁT###I###CA###DE###SUMA###ASEGURADA###I###NCLU###I###DO", "-REINSTALACIÓN AUTOMÁTICA DE SUMA ASEGURADA###INCLUIDO"));
		remplazoDeA.add(new ReplaceModel("-###GASTOS###POR###DONAC###IÓN###DE###ÓRGANOS", "-GASTOS POR DONACIÓN DE ÓRGANOS"));
		remplazoDeA.add(new ReplaceModel("-###RENTA###D###I###AR###I###A###POR###HOSP###I###TAL###I###ZAC###IÓN###EN###UN###CENTRO###DE", "RENTA DIARIA POR HOSPITALIZACIÓN EN UN CENTRO DE"));
		remplazoDeA.add(new ReplaceModel("AS###I###STENC###I###A###SOC###I###AL###MÁX###IMO###30###D###Í###AS###POR###D###Í###A", "ASISTENCIA SOCIAL MÁXIMO 30 DÍAS POR DÍA"));
		remplazoDeA.add(new ReplaceModel("-###COMPL###I###CAC###IONES###DEL###EMBARAZO###Y###DEL###PARTO###DEL###T###I###TU-###CATÁLOGO", "-COMPLICACIONES DEL EMBARAZO Y DEL PARTO DEL TITU-###CATÁLOGO"));
		remplazoDeA.add(new ReplaceModel("LAR###,###CÓNYUGE###O###HIJA##ASEGURADA###CON###PER###IODO###DE###ES-", "LAR, CÓNYUGE O HIJA ASEGURADA CON PERIODO DE ES-"));
		remplazoDeA.add(new ReplaceModel("PERA###DE###10###MESES###CON###DEDUC###I###BLE###Y###COASEGURO", "PERA DE 10 MESES CON DEDUCIBLE Y COASEGURO"));
		remplazoDeA.add(new ReplaceModel("-###PREMATUREZ###S###I###EMPRE###QUE###EL###NAC###IMI###ENTO###OCURRA###SUMA###ASEGURADA", "-PREMATUREZ SIEMPRE QUE EL NACIMIENTO OCURRA###SUMA ASEGURADA"));
		remplazoDeA.add(new ReplaceModel("DESPUÉS###DE###10###MESES###DE###ALTA###DE###LA###MADRE###BÁS###I###CA###POR###EVENTO", "DESPUÉS DE 10 MESES DE ALTA DE LA MADRE###BÁSICA POR EVENTO"));
		remplazoDeA.add(new ReplaceModel("-###PREEX###I###STENC###I###A###DECLARADA###CON###PER###IODO###DE###ESPE-###I###NCLU###I###DA", "-PREEXISTENCIA DECLARADA CON PERIODO DE ESPE-###INCLUIDA"));
		remplazoDeA.add(new ReplaceModel("RA###DE###2###AÑOS###,###NO###APL###I###CA###RECONOC###IMI###ENTO###DE", "RA DE 2 AÑOS, NO APLICA RECONOCIMIENTO DE"));
		remplazoDeA.add(new ReplaceModel("ANT###IGÜEDAD", "ANTIGÜEDAD"));
		remplazoDeA.add(new ReplaceModel("-###C###I###RCUNC###I###S###IÓN###CON###PER###IODO###DE###ESPERA###DE###1###AÑO###I###NCLU###I###DO", "-CIRCUNCISIÓN CON PERIODO DE ESPERA DE 1 AÑO###INCLUIDO"));
		remplazoDeA.add(new ReplaceModel("NO###APL###I###CA###RECONOC###IMI###ENTO###DE###ANTIGÜEDAD", "NO APLICA RECONOCIMIENTO DE ANTIGÜEDAD"));
		remplazoDeA.add(new ReplaceModel("-###PADEC###IMI###ENTOS###CONGÉN###I###TOS###PARA###NAC###I###DOS###DENTRO###I###NCLU###I###DO", "-PADECIMIENTOS CONGÉNITOS PARA NACIDOS DENTRO###INCLUIDO"));
		remplazoDeA.add(new ReplaceModel("DE###LA###V###IGENC###I###A###,###DADOS###DE###ALTA###DENTRO###DE###LOS", "DE LA VIGENCIA , DADOS DE ALTA DENTRO DE LOS"));
		remplazoDeA.add(new ReplaceModel("30###D###Í###AS###POSTER###IORES###AL###NAC###IMI###ENTO###Y###QUE###LA", "30 DÍAS POSTERIORES AL NACIMIENTO Y QUE LA"));
		remplazoDeA.add(new ReplaceModel("MADRE###ASEGURADA###CUMPLA###CON###EL###PER###IODO###DE", "MADRE ASEGURADA CUMPLA CON EL PERIODO DE"));
		remplazoDeA.add(new ReplaceModel("ESPERA###DE###10###MESES", "ESPERA DE 10 MESES."));
		remplazoDeA.add(new ReplaceModel("-###PADEC###IMI###ENTOS###CONGÉN###I###TOS###PARA###LOS###NAC###I###DOS###I###NCLU###I###DO", "-PADECIMIENTOS CONGÉNITOS PARA LOS NACIDOS###INCLUIDO"));
		remplazoDeA.add(new ReplaceModel("FUERA###DE###LA###V###IGENC###I###A###CON###PER###IODO###DE###ESPERA", "FUERA DE LA VIGENCI A CON PERIODO DE ESPERA"));
		remplazoDeA.add(new ReplaceModel("DE###12###MESES###", "DE 12 MESES."));

		remplazoDeA.add(new ReplaceModel("-###S###I###DA###,###S###I###EMPRE###QUE###EL###ASEGURADO###SE###ENCUENTRE###CUB###I###ER-###I###NCLU###I###DO", "-SIDA,SIEMPRE QUE EL ASEGURADO SE ENCUENTRE CUBIER-###INCLUIDO"));
		remplazoDeA.add(new ReplaceModel("TO###EN###SEGUROS###BANORTE###AL###MENOS###4###AÑOS###I###N###I###NTERRUM-\nP###I###DOS", "TO EN SEGUROS BANORTE AL MENOS 4 AÑOS ININTERRUMPIDOS"));
		remplazoDeA.add(new ReplaceModel("-###X###I###FOS###I###S###,###LORDOS###I###S###Y###ESCOL###IOS###I###S###CON###PER###IODO###DE###ESPE-###I###NCLU###I###DO", "-XIFOSIS,LORDOSIS Y ESCOLIOSIS CON PERIODO DE ESPE-###INCLUIDO"));
		remplazoDeA.add(new ReplaceModel("RA DE 2 AÑOS, NO APLICA RECONOCIMIENTO DE###ANT###IGÜE-\nDAD###.", "RA DE 2 AÑOS , NO APLICA RECONOCIMIENTO DE ANTIGÜEDAD"));
		remplazoDeA.add(new ReplaceModel("-###TERAP###I###A###PS###I###COLÓGI###CA###Y###PS###IQU###I###ÁTR###I###CA###I###NCLU###I###DO", "-TERAPIA PSICOLÓGICA Y PSIQUIÁTRICA###INCLUIDO"));
		remplazoDeA.add(new ReplaceModel("-###RECONOC###IMI###ENTO###DE###ANTIGÜEDAD###,###PREV###I###A###COMPROBAC###IÓN###I###NCLU###I###DO", "-RECONOCIMIENTO DE ANTIGÜEDAD, PREVI A COMPROBACIÓN##INCLUIDO"));
		remplazoDeA.add(new ReplaceModel("-###TRANSTORNOS###V###I###SUALES###,###CON###PER###IODO###DE###ESPERA###DE###18", "-TRANSTORNOS VISUALES, CON PERIODO DE ESPERA DE 18"));
		remplazoDeA.add(new ReplaceModel("MESES###.", "MESES."));
		remplazoDeA.add(new ReplaceModel("-###LENTE###I###NTRAOCULAR###CON###PER###IODO###DE###ESPERA###DE 12 MESES",  "-LENTE INTRAOCULAR CON PERIODO DE ESPERA DE 12 MESES"));
		remplazoDeA.add(new ReplaceModel("-###COBERTURA###S###I###N###L###ÍMI###TE###DE###EDAD###DE###ACUERDO###CON###LA###I###NCLU###I###DO", "-COBERTURA SIN LÍMITE DE EDAD DE ACUERDO CON LA###INCLUIDO"));
		remplazoDeA.add(new ReplaceModel("CLÁUSULA###DE###EDAD", "CLÁUSULA DE EDAD"));
		remplazoDeA.add(new ReplaceModel("-###LEGRADO", "-LEGRADO"));
		remplazoDeA.add(new ReplaceModel("-###I###NCREMENTO###EN###CATÁLOGO###DE###HONORAR###IOS###PARA###I###NCLU###I###DO", "-INCREMENTO EN CATÁLOGO DE HONORARIOS PARA###INCLUIDO"));
		remplazoDeA.add(new ReplaceModel("ENFERMEDADES###CATASTRÓF###I###CAS", "ENFERMEDADES CATASTRÓFICAS"));
		remplazoDeA.add(new ReplaceModel("-###SERV###I###C###IOS###DE###AS###I###STENC###I###A###CUB###I###ERTO###DE###ACUERDO###I###NCLU###I###DO", "-SERVICIOS DE ASISTENCIA CUBIERTO DE ACUERDO###INCLUIDO"));
		remplazoDeA.add(new ReplaceModel("A###FOLLETO###ANEXO,###OPERADO###MÉX###I###CO###AS###I###STENC###I###AS", "A FOLLETO ANEXO, OPERADO MÉXICO ASISTENCIAS"));
		remplazoDeA.add(new ReplaceModel("Y###MONTOL###I###N", "Y MONTOLIN"));
		remplazoDeA.add(new ReplaceModel("-###COBERTURA###I###NTEGRAL###DENTAL###Y###V###I###S###IÓN###CUB###I###ERTO###I###NCLU###I###DO", "-COBERTURA INTEGRAL DENTAL Y VISIÓN CUBIERTO###INCLUIDO"));
		remplazoDeA.add(new ReplaceModel("DE###ACUERDO###A###FOLLETO###ANEXO,###OPERADO###POR\nDENTEGRA###", "DE ACUERDO A FOLLETO ANEXO, OPERADO POR DENTEGRA"));


		
		remplazoDeA.add(new ReplaceModel("#I###NTRAHOSP###I###TALAR###I###A", "INTRAHOSPITALARIA"));
		remplazoDeA.add(new ReplaceModel("HONORAR###IOS", "HONORARIOS"));
		remplazoDeA.add(new ReplaceModel("###DEDUC###I###BLE", "###DEDUCIBLE"));
		remplazoDeA.add(new ReplaceModel("EL###IMI###NAC###IÓN", "ELIMINACIÓN"));
		remplazoDeA.add(new ReplaceModel("T###I###PO", "TIPO"));
		remplazoDeA.add(new ReplaceModel("HOSP###I###TALES", "HOSPITALES"));
		remplazoDeA.add(new ReplaceModel("HOSP###I###TAL", "HOSPITAL"));
		remplazoDeA.add(new ReplaceModel("V###I###S###I###TA", "VISITA"));
		remplazoDeA.add(new ReplaceModel("EXCLU###I###DO", "EXCLUIDO"));
		remplazoDeA.add(new ReplaceModel("EMERGENC###I###A", "EMERGENCIA"));
		remplazoDeA.add(new ReplaceModel("I###NCLU###I###DO", "INCLUIDO"));
		remplazoDeA.add(new ReplaceModel("ACC###I###DENTE", "ACCIDENTE"));
		remplazoDeA.add(new ReplaceModel("I###NDEMN###I###ZAC###IÓN", "INDEMNIZACIÓN"));
		remplazoDeA.add(new ReplaceModel("V###I###S###IÓN", "VISIÓN"));
		remplazoDeA.add(new ReplaceModel("COBERTURA###BÁS###I###CA", "COBERTURA BÁSICA"));
		remplazoDeA.add(new ReplaceModel("I###NCREMENTO", "INCREMENTO"));
		remplazoDeA.add(new ReplaceModel("I###NCREMENTAL", "INCREMENTAL"));
		remplazoDeA.add(new ReplaceModel("M.###N###.", "M.N."));
		remplazoDeA.add(new ReplaceModel("QU###I###RÚRGI###COS", "QUIRÚRGICOS"));
		remplazoDeA.add(new ReplaceModel("PER###IODO", "PERIODO"));
		remplazoDeA.add(new ReplaceModel("TRATAMI###ENTO", "TRATAMIENTO"));
		remplazoDeA.add(new ReplaceModel("#OPERAC###IÓN", "OPERACIÓN"));
		remplazoDeA.add(new ReplaceModel("RECUPERAC###IÓN", "RECUPERACIÓN"));
		remplazoDeA.add(new ReplaceModel("MED###I###CAMENTO", "MEDICAMENTO"));
		remplazoDeA.add(new ReplaceModel("OX###ÍGENO", "OXÍGENO"));
		remplazoDeA.add(new ReplaceModel("SERV###I###C###IO", "SERVICIO"));
		remplazoDeA.add(new ReplaceModel("TERAP###I###A", "TERAPIA"));
		remplazoDeA.add(new ReplaceModel("D###Í###AS", "DÍAS"));
		remplazoDeA.add(new ReplaceModel("PADEC###IMI###ENTOS", "PADECIMIENTOS"));
		remplazoDeA.add(new ReplaceModel("CONGÉN###I###TOS", "CONGÉNITOS"));
		remplazoDeA.add(new ReplaceModel("NAC###I###DOS###", "NACIDOS###"));
		remplazoDeA.add(new ReplaceModel("V###IGENC###I###A", "VIGENCIA"));
		remplazoDeA.add(new ReplaceModel("###X###I###FOS###I###S###,###LORDOS###I###S###Y###ESCOL###IOS###I###S", "-XIFOSIS, LORDOSIS Y ESCOLIOSIS"));
		remplazoDeA.add(new ReplaceModel("APL###I###CA", "APLICA"));
		remplazoDeA.add(new ReplaceModel("S###I###N###", "SIN###"));

		return remplazoDeA;
	}
	
	private String completaTextoCobertura(String[] arrTexto,int i) {
		String texto = arrTexto[i];
		if(texto.contains("SALA DE OPERACIÓN Y RECUPERACIÓN, MEDICAMENTO,") && (i+1) < arrTexto.length) {
			texto = completaTextoActualConLineaSiguiente(arrTexto, i, "SALA DE OPERACIÓN Y RECUPERACIÓN, MEDICAMENTO,", "CONSUMO DE OXÍGENO, SERVICIO DE TERAPIA");
			if(!texto.contains("TRATAMIENTO DE DIÁLISIS Y TRANSFUSIONES DE SANGRE") && (i+2) < arrTexto.length) {
				arrTexto[i+1] = texto;
				texto =  completaTextoActualConLineaSiguiente(arrTexto, i+1, "SALA DE OPERACIÓN Y RECUPERACIÓN, MEDICAMENTO,CONSUMO DE OXÍGENO, SERVICIO DE TERAPIA", "TRATAMIENTO DE DIÁLISIS Y TRANSFUSIONES DE SANGRE");
			}
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
}
