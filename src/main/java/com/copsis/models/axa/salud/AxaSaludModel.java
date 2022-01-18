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
				.replace("ESPOSA###F", "ESPOSA###F###")
				.replace("T I T ULAR F", ConstantsValue.TITULAR_HASH)
				.replace("T ITULAR F",ConstantsValue.TITULAR_HASH)
				.replace("T I TULAR F",ConstantsValue.TITULAR_HASH)
				.replace("TITULAR F", ConstantsValue.TITULAR_HASH)
				.replace("T ###ITULAR M", "###TITULAR###M###")
				.replace("E ###SPOSA F ","###ESPOSA###F###" )
				.replace("ESPOSA F", "ESPOSA###F###")
				.replace("E SPOSA F", "###ESPOSA###F###s")
				.replace("###HIJA F", "###HIJA###F###")
				.replace("###HIJO M", "###HIJO###M###")
				.replace(" ######TITULAR", " ###TITULAR")
				.replace("1 ###961", "1961")
				.replace("-1969 ", "-1969###").replace("-1943", "-1943###").replace("-1944 ", "-1944###e")
	             .replace("###OTRO F", "###OTRO###F###")
	             .replace("Familia###Asegurada", "Familia Asegurada")	             
	             .replace("Prima###Total###Asegurados:", "Prima Total Asegurados:")
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
						.replace("@@@", "").replace("###", "").replace(ConstantsValue.CONTRATANTE2, "").replace("  ", "")
						.replace("\r","");
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
						if(newcontenido.split("\n")[i].split("C.P:")[1].contains("Num")){
							modelo.setCp(newcontenido.split("\n")[i].split("C.P:")[1].split("Num")[0].replace("###", "").replace("\r", "").replace("\u00a0", "").trim());
						}else {
							modelo.setCp(newcontenido.split("\n")[i].split("C.P:")[1].replace("###", "").replace("\r", "").replace("\u00a0", "").trim());
						}
						
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
					}else if(newcontenido.split("\n")[i].contains("Deducible:") && newcontenido.split("\n")[i].contains("Coaseguro Máximo")) {
						modelo.setDeducible(newcontenido.split("\n")[i].split("Deducible:")[1].split("Coaseguro Máximo:")[0].replace("###", ""));
						modelo.setCoaseguro(newcontenido.split("\n")[i-1].split("Coaseguro:")[1].replace("###", ""));			
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
			
			inicio = contenido.indexOf("Familia Asegurada");
			fin = contenido.indexOf("Prima Total Asegurados:");
			int index = -1;
			String texto;
		
			
			if (inicio > 0 && fin > 0 && inicio < fin) {
				List<EstructuraAseguradosModel> asegurados = new ArrayList<>();
				newcontenido = contenido.substring(inicio, fin).replace("@@@", "").replace("######", "###").replace("### ###", "###");
				for (int i = 0; i < newcontenido.split("\n").length; i++) {
					EstructuraAseguradosModel asegurado = new EstructuraAseguradosModel();
				  if(newcontenido.split("\n")[i].contains("-")) {	

			  switch (newcontenido.split("\n")[i].split("###").length) {						 		
			        case 7: case 8:	
						asegurado.setNombre((newcontenido.split("\n")[i].split("###")[0].split(",")[1] +" " + newcontenido.split("\n")[i].split("###")[0].split(",")[0]).replace("  ", " ").trim());			
						asegurado.setParentesco(fn.parentesco(newcontenido.split("\n")[i].split("###")[1]));
						asegurado.setSexo(fn.sexo(newcontenido.split("\n")[i].split("###")[2].trim()).booleanValue() ? 1 : 0);
						asegurado.setNacimiento(fn.formatDateMonthCadena(newcontenido.split("\n")[i].split("###")[3].replace(" ", "")));
						asegurado.setEdad(fn.castInteger(newcontenido.split("\n")[i].split("###")[4].trim()) != null  ? fn.castInteger(newcontenido.split("\n")[i].split("###")[4].trim()) : 0 );

						if(newcontenido.split("\n")[i].split("###").length == 7 && asegurado.getNacimiento().split("-")[0].length()>4) {							
							texto = asegurado.getNacimiento().substring(4,6);//  se extrae los digitos que no corresponden al año 195863-03-17 = 63
							asegurado.setEdad(fn.castInteger(texto));
							asegurado.setNacimiento(asegurado.getNacimiento().replace(texto, ""));
						}
						index = fn.castDouble(newcontenido.split("\n")[i].split("###")[5]) != null ? 5 : 4;
						asegurado.setPrimaneta(fn.castBigDecimal(fn.castDouble(newcontenido.split("\n")[i].split("###")[index])));
						index = newcontenido.split("\n")[i].split("###")[6].contains("-") ? 6 : 5;
						asegurado.setAntiguedad(fn.formatDateMonthCadena(newcontenido.split("\n")[i].split("###")[index].replace(" ", "").replace("\r", "")));
						asegurados.add(asegurado);
						break;

					default:
						break;
					}
				  }	
				}
				modelo.setAsegurados(asegurados);
			}
			
		

			


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
