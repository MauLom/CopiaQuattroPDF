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
			// Datos Generales
			inicio = contenido.indexOf("GASTOS MÉDICOS MAYORES");
			fin = contenido.indexOf("NOMBRE DEL ASEGURADO");
			if(fin == -1){
				fin = contenido.indexOf("CUADRO DE ESPECIFICACIONES");
			}
		

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
							if(newcontenido.split("\n")[i].split("C.P.")[1].split("###")[0].trim().length() > 5){
								modelo.setCp(newcontenido.split("\n")[i].split("C.P.")[1].split("###")[0].trim().substring(0,5));
							}else{
								modelo.setCp(newcontenido.split("\n")[i].split("C.P.")[1].split("###")[0].trim());
							}
							
						} else {
							modelo.setCp(newcontenido.split("\n")[i].split("C.P.")[1].split(",")[0].trim());
						}

					}
					if (newcontenido.split("\n")[i].contains("EMISIÓN") && modelo.getCp().length() == 0
							&& fn.isvalidCp(newcontenido.split("\n")[i + 1].split("###")[0]).booleanValue()) {

						modelo.setCp(newcontenido.split("\n")[i + 1].split("###")[0].trim());
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
					
							 modelo.setFormaPago(fn.formaPagoSring(
							 		newcontenido.split("\n")[i + 3].split(ConstantsValue.PESOS_MAYUS)[1]
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
							asegurado.setNombre(newcontenido.split("\n")[i].split("###")[0].trim());
						} else {
							String x = newcontenido.split("\n")[i].split("###")[0].replace(" ", "###");
							asegurado
									.setNacimiento(fn.formatDateMonthCadena(x.split("###")[x.split("###").length - 1]));
							asegurado.setNombre(newcontenido.split("\n")[i].split("###")[0]
									.split(x.split("###")[x.split("###").length - 1])[0].trim());
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

			if(coberturasApartado.trim().length() > 0) {
				contenidoCoberturas.append(" ").append(coberturasApartado);
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
			if(arrContenido[i].split("###").length > 1) {
				arrContenido[i] = uneValorDeducible(arrContenido[i]);
			}
		}

		for(int i=0;i< arrContenido.length;i++) {
			valores = arrContenido[i].split("###").length;
			EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();
			if(valores == 2 && arrContenido[i].split("###")[0].length() >0) {
				cobertura.setNombre(arrContenido[i].split("###")[0].trim());
				cobertura.setSa(arrContenido[i].split("###")[1].trim());
				listCoberturas.add(cobertura);
			}
		}
		
		if(!listCoberturas.isEmpty()) {
			modelo.setCoberturas(listCoberturas);
		}
		
	}
	
	private List<ReplaceModel> reemplazoCoberturas(){
		List<ReplaceModel> reemplazoDeA = new ArrayList<>();

		reemplazoDeA.add(new ReplaceModel("@@@",""));
		reemplazoDeA.add(new ReplaceModel("\r",""));

		reemplazoDeA.add(new ReplaceModel("-###EMERGENC###I###A###EN###EL###EXTRANJERO", "EMERGENCIA EN EL EXTRANJERO"));
		reemplazoDeA.add(new ReplaceModel("-###COBERTURA###TOTAL###EXTRANJERO", "COBERTURA TOTAL EXTRANJERO"));
		reemplazoDeA.add(new ReplaceModel("-###COBERTURA###DE###EL###IMI###NAC###IÓN###DE###DEDUC###I###BLE###POR###ACC###I###DENTE", "COBERTURA DE ELIMINACIÓN DE DEDUCIBLE POR ACCIDENTE"));
		reemplazoDeA.add(new ReplaceModel("-###COBERTURA###I###NDEMN###I###ZAC###IÓN###POR###ENFERMEDAD###GRAVE", "COBERTURA DE INDEMNIZACIÓN POR ENEFERMEDAD GRAVE"));
		reemplazoDeA.add(new ReplaceModel("-###COBERTURA###DE###V###I###S###IÓN###I###NCREMENTAL###OPERADOR###DENTEGRA", "COBERTURA DE VISIÓN INCREMENTAL OPERADOR DENTEGRA"));
		
		reemplazoDeA.add(new ReplaceModel("-###I###NCREMENTO###AL###CATÁLOGO","INCREMENTO AL CATÁLOGO"));
		reemplazoDeA.add(new ReplaceModel("-###BASE###DE###HONORAR###IOS###QU###I###RÚRGI###COS###ZONA###1", "BASE DE HONORARIOS QUIRÚRGICOS ZONA 1"));
		reemplazoDeA.add(new ReplaceModel("ZONA###2", "ZONA 2"));
		reemplazoDeA.add(new ReplaceModel("ZONA###3", "ZONA 3"));
		reemplazoDeA.add(new ReplaceModel("ZONA###4", "ZONA 4"));
		reemplazoDeA.add(new ReplaceModel("ZONA###5", "ZONA 5"));

		reemplazoDeA.add(new ReplaceModel("-###GASTOS###POR###PARTO###O###CESÁREA###DEL###TITULAR###,\nCÓNYUGE###O###HIJA##ASEGURADA###,###CON###", "GASTOS POR PARTO O CESÁREA DEL TITULAR, CÓNYUGE O HIJA ASEGURADA, CON###"));
		reemplazoDeA.add(new ReplaceModel("ESPERA###DE###10###MESES###SIN###DEDUCIBLE###N###I###COASEGURO###DE###ACUERDO###A###ENDOSO","ESPERA DE 10 MESES SIN DEDUCIBLE NI COASEGURO###DE ACUERDO A ENDOSO"));
		reemplazoDeA.add(new ReplaceModel("-###T###I###PO###DE###HOSP###I###TAL###TODOS###LOS###HOSP###I###TALES", "TIPO DE HOSPITAL###TODOS LOS HOSPITALES"));
		reemplazoDeA.add(new ReplaceModel("-###HONORAR###IOS###POR###V###I###S###I###TA###I###NTRAHOSP###I###TALAR###I###A###CATÁLOGO", "HONORARIOS POR VISITA INTRAHOSPITALARIA###CATÁLOGO"));
		reemplazoDeA.add(new ReplaceModel("-###HONORAR###IOS###POR###CONSULTA###CATÁLOGO", "HONORARIOS POR CONSULTA###CATÁLOGO"));
		reemplazoDeA.add(new ReplaceModel("-###HONORAR###IOS###POR###ENFERMERA###MÁX###IMO###3###TURNOS\nPOR###D###Í###A###DURANTE###30###D###Í###AS###CATÁLOGO", "HONORARIOS POR ENFERMERA MÁXIMO 3 TURNOS POR DÍA DURANTE 30 DÍAS###CATÁLOGO"));
		reemplazoDeA.add(new ReplaceModel("-###HAB###I###TAC###IÓN###HOSP###I###TAL###PR###I###VADO###ESTÁNDAR", "HABITACIÓN HOSPITAL###PRIVADO ESTÁNDAR"));

		reemplazoDeA.add(new ReplaceModel("-###SALA###DE###OPERAC###IÓN###Y###RECUPERAC###IÓN###,###MED###I###CAMENTO,###I###NCLU###I###DO", "SALA DE OPERACIÓN Y RECUPERACIÓN, MEDICAMENTO,###INCLUIDO"));
		reemplazoDeA.add(new ReplaceModel("CONSUMO###DE###OX###ÍGENO,###SERV###I###C###IO###DE###TERAP###I###A###,", "CONSUMO DE OXÍGENO, SERVICIO DE TERAPIA,"));
		reemplazoDeA.add(new ReplaceModel("TRATAMI###ENTO###DE###D###I###ÁL###I###S###I###S###Y###TRANSFUS###IONES###DE###SANGRE", "TRATAMIENTO DE DIÁLISIS Y TRANSFUSIONES DE SANGRE"));
		reemplazoDeA.add(new ReplaceModel("-###RENTA###DE###APARATOS###ORTOPED###I###COS###Y###PRÓTES###I###S###I###NCLU###I###DO", "RENTA DE APARATOS ORTOPEDICOS Y PRÓTESIS###INCLUIDO"));
		reemplazoDeA.add(new ReplaceModel("-###AMBULANC###I###A###AÉREA###I###NCLU###I###DO", "AMBULANCIA AÉREA###INCLUIDO"));
		reemplazoDeA.add(new ReplaceModel("-###AMBULANC###I###A###TERRESTRE###I###NCLU###I###DO", "AMBULANCIA TERRESTRE###INCLUIDO"));
		reemplazoDeA.add(new ReplaceModel("-###RE###I###NSTALAC###IÓN###AUTOMÁT###I###CA###DE###SUMA###ASEGURADA###I###NCLU###I###DO", "REINSTALACIÓN AUTOMÁTICA DE SUMA ASEGURADA###INCLUIDO"));
		reemplazoDeA.add(new ReplaceModel("-###GASTOS###POR###DONAC###IÓN###DE###ÓRGANOS", "GASTOS POR DONACIÓN DE ÓRGANOS"));
		reemplazoDeA.add(new ReplaceModel("-###RENTA###D###I###AR###I###A###POR###HOSP###I###TAL###I###ZAC###IÓN###EN###UN###CENTRO###DE", "RENTA DIARIA POR HOSPITALIZACIÓN EN UN CENTRO DE"));
		reemplazoDeA.add(new ReplaceModel("AS###I###STENC###I###A###SOC###I###AL###MÁX###IMO###30###D###Í###AS###POR###D###Í###A", "ASISTENCIA SOCIAL MÁXIMO 30 DÍAS###POR DÍA"));
		reemplazoDeA.add(new ReplaceModel("-###COMPL###I###CAC###IONES###DEL###EMBARAZO###Y###DEL###PARTO###DEL###T###I###TU-###CATÁLOGO", "COMPLICACIONES DEL EMBARAZO Y DEL PARTO DEL TITU-###CATÁLOGO"));
		reemplazoDeA.add(new ReplaceModel("LAR###,###CÓNYUGE###O###HIJA##ASEGURADA###CON###PER###IODO###DE###ES-", "LAR, CÓNYUGE O HIJA ASEGURADA CON PERIODO DE ES-"));
		reemplazoDeA.add(new ReplaceModel("PERA###DE###10###MESES###CON###DEDUC###I###BLE###Y###COASEGURO", "PERA DE 10 MESES CON DEDUCIBLE Y COASEGURO"));
		reemplazoDeA.add(new ReplaceModel("-###PREMATUREZ###S###I###EMPRE###QUE###EL###NAC###IMI###ENTO###OCURRA###SUMA###ASEGURADA", "PREMATUREZ SIEMPRE QUE EL NACIMIENTO OCURRA###SUMA ASEGURADA"));
		reemplazoDeA.add(new ReplaceModel("DESPUÉS###DE###10###MESES###DE###ALTA###DE###LA###MADRE###BÁS###I###CA###POR###EVENTO", "DESPUÉS DE 10 MESES DE ALTA DE LA MADRE###BÁSICA POR EVENTO"));
		reemplazoDeA.add(new ReplaceModel("-###PREEX###I###STENC###I###A###DECLARADA###CON###PER###IODO###DE###ESPE-###I###NCLU###I###DA", "PREEXISTENCIA DECLARADA CON PERIODO DE ESPE-###INCLUIDA"));
		reemplazoDeA.add(new ReplaceModel("RA###DE###2###AÑOS###,###NO###APL###I###CA###RECONOC###IMI###ENTO###DE", "RA DE 2 AÑOS, NO APLICA RECONOCIMIENTO DE"));
		reemplazoDeA.add(new ReplaceModel("ANT###IGÜEDAD", "ANTIGÜEDAD"));
		reemplazoDeA.add(new ReplaceModel("-###C###I###RCUNC###I###S###IÓN###CON###PER###IODO###DE###ESPERA###DE###1###AÑO###I###NCLU###I###DO", "CIRCUNCISIÓN CON PERIODO DE ESPERA DE 1 AÑO###INCLUIDO"));
		reemplazoDeA.add(new ReplaceModel("NO###APL###I###CA###RECONOC###IMI###ENTO###DE###ANTIGÜEDAD", "NO APLICA RECONOCIMIENTO DE ANTIGÜEDAD"));
		reemplazoDeA.add(new ReplaceModel("-###PADEC###IMI###ENTOS###CONGÉN###I###TOS###PARA###NAC###I###DOS###DENTRO###I###NCLU###I###DO", "PADECIMIENTOS CONGÉNITOS PARA NACIDOS DENTRO###INCLUIDO"));
		reemplazoDeA.add(new ReplaceModel("DE###LA###V###IGENC###I###A###,###DADOS###DE###ALTA###DENTRO###DE###LOS", "DE LA VIGENCIA , DADOS DE ALTA DENTRO DE LOS"));
		reemplazoDeA.add(new ReplaceModel("30###D###Í###AS###POSTER###IORES###AL###NAC###IMI###ENTO###Y###QUE###LA", "30 DÍAS POSTERIORES AL NACIMIENTO Y QUE LA"));
		reemplazoDeA.add(new ReplaceModel("MADRE###ASEGURADA###CUMPLA###CON###EL###PER###IODO###DE", "MADRE ASEGURADA CUMPLA CON EL PERIODO DE"));
		reemplazoDeA.add(new ReplaceModel("ESPERA###DE###10###MESES.", "ESPERA DE 10 MESES"));
		reemplazoDeA.add(new ReplaceModel("-###PADEC###IMI###ENTOS###CONGÉN###I###TOS###PARA###LOS###NAC###I###DOS###I###NCLU###I###DO", "PADECIMIENTOS CONGÉNITOS PARA LOS NACIDOS###INCLUIDO"));
		reemplazoDeA.add(new ReplaceModel("FUERA###DE###LA###V###IGENC###I###A###CON###PER###IODO###DE###ESPERA", "FUERA DE LA VIGENCIA CON PERIODO DE ESPERA"));
		reemplazoDeA.add(new ReplaceModel("DE###12###MESES", "DE 12 MESES"));

		reemplazoDeA.add(new ReplaceModel("-###S###I###DA###,###S###I###EMPRE###QUE###EL###ASEGURADO###SE###ENCUENTRE###CUB###I###ER-###I###NCLU###I###DO", "SIDA,SIEMPRE QUE EL ASEGURADO SE ENCUENTRE CUBIER-###INCLUIDO"));
		reemplazoDeA.add(new ReplaceModel("TO###EN###SEGUROS###BANORTE###AL###MENOS###4###AÑOS###I###N###I###NTERRUM-\nP###I###DOS", "TO EN SEGUROS BANORTE AL MENOS 4 AÑOS ININTERRUMPIDOS"));
		reemplazoDeA.add(new ReplaceModel("-###X###I###FOS###I###S###,###LORDOS###I###S###Y###ESCOL###IOS###I###S###CON###PER###IODO###DE###ESPE-###I###NCLU###I###DO", "XIFOSIS,LORDOSIS Y ESCOLIOSIS CON PERIODO DE ESPE-###INCLUIDO"));
		reemplazoDeA.add(new ReplaceModel("RA DE 2 AÑOS, NO APLICA RECONOCIMIENTO DE###ANT###IGÜE-\nDAD###.", "RA DE 2 AÑOS, NO APLICA RECONOCIMIENTO DE ANTIGÜEDAD"));
		reemplazoDeA.add(new ReplaceModel("-###TERAP###I###A###PS###I###COLÓGI###CA###Y###PS###IQU###I###ÁTR###I###CA###I###NCLU###I###DO", "TERAPIA PSICOLÓGICA Y PSIQUIÁTRICA###INCLUIDO"));
		reemplazoDeA.add(new ReplaceModel("-###RECONOC###IMI###ENTO###DE###ANTIGÜEDAD###,###PREV###I###A###COMPROBAC###IÓN###I###NCLU###I###DO", "RECONOCIMIENTO DE ANTIGÜEDAD, PREVIA COMPROBACIÓN###INCLUIDO"));
		reemplazoDeA.add(new ReplaceModel("-###TRANSTORNOS###V###I###SUALES###,###CON###PER###IODO###DE###ESPERA###DE###18", "TRANSTORNOS VISUALES, CON PERIODO DE ESPERA DE 18"));
		reemplazoDeA.add(new ReplaceModel("MESES###.", "MESES."));
		reemplazoDeA.add(new ReplaceModel("-###LENTE###I###NTRAOCULAR###CON###PER###IODO###DE###ESPERA###DE 12 MESES",  "LENTE INTRAOCULAR CON PERIODO DE ESPERA DE 12 MESES"));
		reemplazoDeA.add(new ReplaceModel("-###COBERTURA###S###I###N###L###ÍMI###TE###DE###EDAD###DE###ACUERDO###CON###LA###I###NCLU###I###DO", "COBERTURA SIN LÍMITE DE EDAD DE ACUERDO CON LA###INCLUIDO"));
		reemplazoDeA.add(new ReplaceModel("CLÁUSULA###DE###EDAD", "CLÁUSULA DE EDAD"));
		reemplazoDeA.add(new ReplaceModel("-###LEGRADO", "LEGRADO"));
		reemplazoDeA.add(new ReplaceModel("-###I###NCREMENTO###EN###CATÁLOGO###DE###HONORAR###IOS###PARA###I###NCLU###I###DO", "INCREMENTO EN CATÁLOGO DE HONORARIOS PARA###INCLUIDO"));
		reemplazoDeA.add(new ReplaceModel("ENFERMEDADES###CATASTRÓF###I###CAS", "ENFERMEDADES CATASTRÓFICAS"));
		reemplazoDeA.add(new ReplaceModel("-###SERV###I###C###IOS###DE###AS###I###STENC###I###A###CUB###I###ERTO###DE###ACUERDO###I###NCLU###I###DO", "SERVICIOS DE ASISTENCIA CUBIERTO DE ACUERDO###INCLUIDO"));
		reemplazoDeA.add(new ReplaceModel("A###FOLLETO###ANEXO,###OPERADO###MÉX###I###CO###AS###I###STENC###I###AS", "A FOLLETO ANEXO, OPERADO MÉXICO ASISTENCIAS"));
		reemplazoDeA.add(new ReplaceModel("Y###MONTOL###I###N", "Y MONTOLIN"));
		reemplazoDeA.add(new ReplaceModel("-###COBERTURA###I###NTEGRAL###DENTAL###Y###V###I###S###IÓN###CUB###I###ERTO###I###NCLU###I###DO", "COBERTURA INTEGRAL DENTAL Y VISIÓN CUBIERTO###INCLUIDO"));
		reemplazoDeA.add(new ReplaceModel("DE###ACUERDO###A###FOLLETO###ANEXO,###OPERADO###POR\nDENTEGRA###", "DE ACUERDO A FOLLETO ANEXO, OPERADO POR DENTEGRA"));

		reemplazoDeA.add(new ReplaceModel("#I###NTRAHOSP###I###TALAR###I###A", "INTRAHOSPITALARIA"));
		reemplazoDeA.add(new ReplaceModel("HONORAR###IOS", "HONORARIOS"));
		reemplazoDeA.add(new ReplaceModel("###DEDUC###I###BLE", "###DEDUCIBLE"));
		reemplazoDeA.add(new ReplaceModel("EL###IMI###NAC###IÓN", "ELIMINACIÓN"));
		reemplazoDeA.add(new ReplaceModel("T###I###PO", "TIPO"));
		reemplazoDeA.add(new ReplaceModel("HOSP###I###TALES", "HOSPITALES"));
		reemplazoDeA.add(new ReplaceModel("HOSP###I###TAL", "HOSPITAL"));
		reemplazoDeA.add(new ReplaceModel("V###I###S###I###TA", "VISITA"));
		reemplazoDeA.add(new ReplaceModel("EXCLU###I###DO", "EXCLUIDO"));
		reemplazoDeA.add(new ReplaceModel("EMERGENC###I###A", "EMERGENCIA"));
		reemplazoDeA.add(new ReplaceModel("I###NCLU###I###DO", "INCLUIDO"));
		reemplazoDeA.add(new ReplaceModel("ACC###I###DENTE", "ACCIDENTE"));
		reemplazoDeA.add(new ReplaceModel("I###NDEMN###I###ZAC###IÓN", "INDEMNIZACIÓN"));
		reemplazoDeA.add(new ReplaceModel("V###I###S###IÓN", "VISIÓN"));
		reemplazoDeA.add(new ReplaceModel("COBERTURA###BÁS###I###CA", "COBERTURA BÁSICA"));
		reemplazoDeA.add(new ReplaceModel("I###NCREMENTO", "INCREMENTO"));
		reemplazoDeA.add(new ReplaceModel("I###NCREMENTAL", "INCREMENTAL"));
		reemplazoDeA.add(new ReplaceModel("M.###N###.", "M.N."));
		reemplazoDeA.add(new ReplaceModel("QU###I###RÚRGI###COS", "QUIRÚRGICOS"));
		reemplazoDeA.add(new ReplaceModel("PER###IODO", "PERIODO"));
		reemplazoDeA.add(new ReplaceModel("TRATAMI###ENTO", "TRATAMIENTO"));
		reemplazoDeA.add(new ReplaceModel("#OPERAC###IÓN", "OPERACIÓN"));
		reemplazoDeA.add(new ReplaceModel("RECUPERAC###IÓN", "RECUPERACIÓN"));
		reemplazoDeA.add(new ReplaceModel("MED###I###CAMENTO", "MEDICAMENTO"));
		reemplazoDeA.add(new ReplaceModel("OX###ÍGENO", "OXÍGENO"));
		reemplazoDeA.add(new ReplaceModel("SERV###I###C###IO", "SERVICIO"));
		reemplazoDeA.add(new ReplaceModel("TERAP###I###A", "TERAPIA"));
		reemplazoDeA.add(new ReplaceModel("D###Í###AS", "DÍAS"));
		reemplazoDeA.add(new ReplaceModel("PADEC###IMI###ENTOS", "PADECIMIENTOS"));
		reemplazoDeA.add(new ReplaceModel("CONGÉN###I###TOS", "CONGÉNITOS"));
		reemplazoDeA.add(new ReplaceModel("NAC###I###DOS###", "NACIDOS###"));
		reemplazoDeA.add(new ReplaceModel("V###IGENC###I###A", "VIGENCIA"));
		reemplazoDeA.add(new ReplaceModel("###X###I###FOS###I###S###,###LORDOS###I###S###Y###ESCOL###IOS###I###S", "-XIFOSIS, LORDOSIS Y ESCOLIOSIS"));
		reemplazoDeA.add(new ReplaceModel("APL###I###CA", "APLICA"));
		reemplazoDeA.add(new ReplaceModel("SIN###DEDUCIBLE###N###I", "SIN NI"));
		reemplazoDeA.add(new ReplaceModel("S###I###N###", "SIN###"));
		reemplazoDeA.add(new ReplaceModel("###PLAN###MÉD###I###CO###TOTAL", ""));

		return reemplazoDeA;
	}
	
	private String completaTextoCobertura(String[] arrTexto,int i) {
		String texto = arrTexto[i];
		if(texto.contains("SALA DE OPERACIÓN Y RECUPERACIÓN, MEDICAMENTO,") && (i+1) < arrTexto.length) {
			texto = completaTextoActualConLineaSiguiente(arrTexto, i, "SALA DE OPERACIÓN Y RECUPERACIÓN, MEDICAMENTO,", "CONSUMO DE OXÍGENO, SERVICIO DE TERAPIA,");
			if(!texto.contains("TRATAMIENTO DE DIÁLISIS Y TRANSFUSIONES DE SANGRE") && (i+2) < arrTexto.length) {
				if(arrTexto[i+2].contains("TRATAMIENTO DE DIÁLISIS Y TRANSFUSIONES DE SANGRE")) {
					String aux = texto.split("###")[0];
					texto = texto.replace(aux, aux+" TRATAMIENTO DE DIÁLISIS Y TRANSFUSIONES DE SANGRE");
					arrTexto[i+2] = "";
				}
			}
		}else if(texto.contains("RENTA DIARIA POR HOSPITALIZACIÓN EN UN CENTRO DE")) {
			texto = completaTextoActualConLineaSiguiente(arrTexto, i, "RENTA DIARIA POR HOSPITALIZACIÓN EN UN CENTRO DE", "ASISTENCIA SOCIAL MÁXIMO 30 DÍAS###POR DÍA");
			if(texto.contains("###M.N.")){
				texto = texto.replace("POR DÍA", "");
				texto+= " POR DÍA";
			}
		}else if(texto.contains("COMPLICACIONES DEL EMBARAZO Y DEL PARTO DEL TITU-")) {
			texto = completaTextoActualConLineaSiguiente(arrTexto, i,"COMPLICACIONES DEL EMBARAZO Y DEL PARTO DEL TITU-", "LAR, CÓNYUGE O HIJA ASEGURADA CON PERIODO DE ES-");
			if(!texto.contains("PERA DE 10 MESES CON DEDUCIBLE Y COASEGURO") && (i+2) < arrTexto.length) {
				String aux = texto.split("###")[0];
				texto = texto.replace(aux, aux+"PERA DE 10 MESES CON DEDUCIBLE Y COASEGURO").replace("TITU- LAR", "TITULAR").replace("ES-PERA", "ESPERA");
			}
		}else if(texto.contains("PREMATUREZ SIEMPRE QUE EL NACIMIENTO OCURRA###SUMA ASEGURADA") && (i+1) < arrTexto.length) {
			if(!texto.contains("DESPUÉS DE 10 MESES DE ALTA DE LA MADRE###BÁSICA POR EVENTO") && arrTexto[i+1].contains("DESPUÉS DE 10 MESES DE ALTA DE LA MADRE###BÁSICA POR EVENTO")) {
				arrTexto[i+1] = "";
				texto = "PREMATUREZ SIEMPRE QUE EL NACIMIENTO OCURRA DESPUÉS DE 10 MESES DE ALTA DE LA MADRE###SUMA ASEGURADA BÁSICA POR EVENTO";
			}
		}else if(texto.contains("PREEXISTENCIA DECLARADA CON PERIODO DE ESPE-")) {
			if(i+2 <  arrTexto.length) {
				if(arrTexto[i+2].contains("ANTIGÜEDAD") && !arrTexto[i+2].contains("###")) {
					texto = completaTextoActualConLineaSiguiente(arrTexto, i,"PREEXISTENCIA DECLARADA CON PERIODO DE ESPE-", "RA DE 2 AÑOS, NO APLICA RECONOCIMIENTO DE");
					String aux = texto.split("###")[0];
					texto = texto.replace(aux, aux + " ANTIGÜEDAD").replace("ESPE- RA", "ESPERA");
				}
			}
		}else if(texto.contains("CIRCUNCISIÓN CON PERIODO DE ESPERA DE 1 AÑO")) {
			texto = completaTextoActualConLineaSiguiente(arrTexto, i,"CIRCUNCISIÓN CON PERIODO DE ESPERA DE 1 AÑO", "NO APLICA RECONOCIMIENTO DE ANTIGÜEDAD");
		}else if(texto.contains("PADECIMIENTOS CONGÉNITOS PARA NACIDOS DENTRO")) {
			texto = completaTextoActualConLineaSiguiente(arrTexto, i, "PADECIMIENTOS CONGÉNITOS PARA NACIDOS DENTRO", "DE LA VIGENCIA , DADOS DE ALTA DENTRO DE LOS");
			if(!texto.contains("30 DÍAS POSTERIORES AL NACIMIENTO Y QUE LA")&& (i+2) < arrTexto.length) {
				String textoSiguienteLinea = "30 DÍAS POSTERIORES AL NACIMIENTO Y QUE LA";
				if(arrTexto[i+2].contains(textoSiguienteLinea)) {
					texto = texto.replace(texto.split("###")[0], texto.split("###")[0]+" "+textoSiguienteLinea);
					textoSiguienteLinea = "MADRE ASEGURADA CUMPLA CON EL PERIODO DE";
					if(!texto.contains(textoSiguienteLinea)&& (i+3) < arrTexto.length) {
                        texto = texto.replace(texto.split("###")[0], texto.split("###")[0]+" "+textoSiguienteLinea);
                        textoSiguienteLinea = "ESPERA DE 10 MESES.";
                        if(!texto.contains(textoSiguienteLinea)&& (i+4) < arrTexto.length) {
                            texto = texto.replace(texto.split("###")[0], texto.split("###")[0]+" "+textoSiguienteLinea);
    					}
					}
				}
			}
		}else if(texto.contains("PADECIMIENTOS CONGÉNITOS PARA LOS NACIDOS")) {
			texto = completaTextoActualConLineaSiguiente(arrTexto, i, "PADECIMIENTOS CONGÉNITOS PARA LOS NACIDOS", "FUERA DE LA VIGENCIA CON PERIODO DE ESPERA");
			if(!texto.contains("DE 12 MESES.") && (i+2)< arrTexto.length) {
				if(arrTexto[i+2].contains("DE 12 MESES.")) {
					texto = texto.replace(texto.split("###")[0], texto.split("###")[0]+" DE 12 MESES");
					arrTexto[i+2] = "";
				}
			}
		}else if(texto.contains("SIDA,SIEMPRE QUE EL ASEGURADO SE ENCUENTRE CUBIER-")) {
			texto = completaTextoActualConLineaSiguiente(arrTexto, i, "SIDA,SIEMPRE QUE EL ASEGURADO SE ENCUENTRE CUBIER-", "TO EN SEGUROS BANORTE AL MENOS 4 AÑOS ININTERRUMPIDOS").replace("CUBIER- TO", "CUBIERTO");
		}else if(texto.contains("XIFOSIS,LORDOSIS Y ESCOLIOSIS CON PERIODO DE ESPE-")) {
			texto = completaTextoActualConLineaSiguiente(arrTexto, i, "XIFOSIS,LORDOSIS Y ESCOLIOSIS CON PERIODO DE ESPE-", "RA DE 2 AÑOS, NO APLICA RECONOCIMIENTO DE ANTIGÜEDAD");
			texto = texto.replace("ESPE- RA", "ESPERA");
		}else if(texto.contains("TRANSTORNOS VISUALES, CON PERIODO DE ESPERA DE 18")){
			texto = completaTextoActualConLineaSiguiente(arrTexto, i, "TRANSTORNOS VISUALES, CON PERIODO DE ESPERA DE 18", "MESES.");
		}else if(texto.contains("COBERTURA SIN LÍMITE DE EDAD DE ACUERDO CON LA")) {
			texto = completaTextoActualConLineaSiguiente(arrTexto, i, "COBERTURA SIN LÍMITE DE EDAD DE ACUERDO CON LA", "CLÁUSULA DE EDAD");
		}else if(texto.contains("INCREMENTO EN CATÁLOGO DE HONORARIOS PARA")) {
			texto = completaTextoActualConLineaSiguiente(arrTexto, i, "INCREMENTO EN CATÁLOGO DE HONORARIOS PARA", "ENFERMEDADES CATASTRÓFICAS");
		}else if(texto.contains("SERVICIOS DE ASISTENCIA CUBIERTO DE ACUERDO")) {
			texto = completaTextoActualConLineaSiguiente(arrTexto, i, "SERVICIOS DE ASISTENCIA CUBIERTO DE ACUERDO", "A FOLLETO ANEXO, OPERADO MÉXICO ASISTENCIAS");
			if(!texto.contains("Y MONTOLIN") && (i+2)< arrTexto.length) {
				if(arrTexto[i+2].contains("Y MONTOLIN")) {
					texto = texto.replace(texto.split("###")[0], texto.split("###")[0]+" Y MONTOLIN");
					arrTexto[i+2] = "";
				}
			}
		}else if(texto.contains("COBERTURA INTEGRAL DENTAL Y VISIÓN CUBIERTO")) {
			texto = completaTextoActualConLineaSiguiente(arrTexto, i, "COBERTURA INTEGRAL DENTAL Y VISIÓN CUBIERTO", "DE ACUERDO A FOLLETO ANEXO, OPERADO POR DENTEGRA");
		}else if(texto.contains("EMERGENCIA EN EL EXTRANJERO###CON###SUMA###ASEGURADA###DE")) {
			texto = completaTextoActualConLineaSiguiente(arrTexto, i, "EMERGENCIA EN EL EXTRANJERO###CON###SUMA###ASEGURADA###DE", "50###,###000###DLLS###,###DEDUCIBLE###DE###50###DLLS###Y###COASEGURO###0%###INCLUIDO");
			texto = texto.replace("###", " ").replace("INCLUIDO", "###INCLUIDO").replace("50 , 000", "50,000");			
		}
		else if(texto.contains("GASTOS POR PARTO O CESÁREA DEL TITULAR, CÓNYUGE O HIJA ASEGURADA, CON###PERIODO###DE")) {
			texto = completaTextoActualConLineaSiguiente(arrTexto, i, "GASTOS POR PARTO O CESÁREA DEL TITULAR, CÓNYUGE O HIJA ASEGURADA, CON###PERIODO###DE", "ESPERA###DE###10###MESES###SIN###DEDUCIBLE###N###I###COASEGURO###DE###ACUERDO###A###ENDOSO");
			texto = texto.replace("SIN###DEDUCIBLE###N###I", "SIN DEDUCIBLE NI").replace("###", " ").replace("DE ACUERDO A ENDOSO", "###DE ACUERDO A ENDOSO");
		}else if(texto.contains("###GASTOS###POR###PARTO###O###CESÁREA###DEL###TITULAR###,")) {
			texto = completaTextoActualConLineaSiguiente(arrTexto, i, "###GASTOS###POR###PARTO###O###CESÁREA###DEL###TITULAR###,", "CÓNYUGE###O###HIJA##ASEGURADA###,###CON###PERIODO###DE");
			if(!texto.contains("ESPERA###DE###10###MESES###SIN###DEDUCIBLE###N###I###COASEGURO###DE###ACUERDO###A###ENDOSO") && (i+2)<arrTexto.length) {
				if(arrTexto[i+2].contains("ESPERA###DE###10###MESES###SIN###DEDUCIBLE###N###I###COASEGURO###DE###ACUERDO###A###ENDOSO")) {
					texto = texto.replace("###", " ");
					texto += "ESPERA DE 1O MESES SIN DEDUCIBLE NI COASEGURO###DE ACUERDO A ENDOSO";
					arrTexto[i+2] = "";
				}
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
	
	private String uneValorDeducible(String cobertura) {
		String aux = "";
		String nombre = "";
		String resultado = cobertura;
		String deducible = "";
		if(cobertura.split("###").length > 2) {
			nombre = cobertura.split("###")[0];
			deducible = cobertura.split(nombre)[1];
			deducible = deducible.replace("###", "");
			aux = deducible.replace("M.N.", "M/N/").replace("M.", "M/").replace(".", "@@").replace(",", "");

			//se valida si no hay  "@@" o si trae  mas de @@, antes de validar si es numérico
			if(aux.split("@@").length < 3 && aux.contains("M/")) {
				if(aux.split("@@").length == 2 ) {
					if(aux.split("@@")[1].length() > 2 ) {
					  aux =  "";
					}
				}
				aux = aux.split("M/")[0].trim();
			}

			if(fn.isNumeric(aux)) {
				resultado = nombre +"###"+deducible;
			}
		}

		return resultado;
	}
}
