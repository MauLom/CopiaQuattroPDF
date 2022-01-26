package com.copsis.models.chubb;

import java.util.ArrayList;
import java.util.List;

import com.copsis.constants.ConstantsValue;
import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraCoberturasModel;
import com.copsis.models.EstructuraJsonModel;
import com.copsis.models.EstructuraRecibosModel;
import com.copsis.models.EstructuraUbicacionesModel;

public class ChubbDiversosModel {
	// Clases
	DataToolsModel fn = new DataToolsModel();
	EstructuraJsonModel modelo = new EstructuraJsonModel();
	// Variables
	private String contenido;
	private String recibos = "";

	// constructor
	public ChubbDiversosModel(String contenido, String recibos) {
		this.contenido = contenido;
		this.recibos = recibos;

	}

	public EstructuraJsonModel procesar() {


		StringBuilder resultado = new StringBuilder();
		int inicio = 0;
		int fin = 0;
		String newcontenido = "";

		contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());
		contenido = contenido.replace("PÓLIZA DE SEGURO", "POLIZA DE SEGURO");
		recibos = fn.remplazarMultiple(recibos, fn.remplazosGenerales());
		try {

			modelo.setTipo(7);
			modelo.setCia(1);
			modelo.setRamo("Daños");

			inicio = contenido.indexOf("POLIZA DE SEGURO");
			fin = contenido.indexOf("Características del riesgo");

			if (inicio > 0 && fin > 0 && inicio < fin) {
				newcontenido = contenido.substring(inicio, fin);
				obtenerEndoso(newcontenido);
				for (int i = 0; i < newcontenido.split("\n").length; i++) {
					if (newcontenido.split("\n")[i].contains("Póliza:")
							&& newcontenido.split("\n")[i].contains("Vigencia")) {
						modelo.setPoliza(newcontenido.split("\n")[i].split(ConstantsValue.POLIZA_ACENT2)[1].split("Vigencia:")[0]
								.replace("###", ""));
						modelo.setVigenciaDe(fn.formatDateMonthCadena(
								newcontenido.split("\n")[i].split("Vigencia:")[1].split(ConstantsValue.HORAS)[0]
										.replace("###", "").replace("12:00", "").replace("Del", "").trim()));
						modelo.setVigenciaA(
								fn.formatDateMonthCadena(newcontenido.split("\n")[i].split(ConstantsValue.HORAS)[1]
										.split(ConstantsValue.HORAS)[0].replace("###", "").replace("12:00", "")
												.replace("al", "").trim()));
					}
					if (newcontenido.split("\n")[i].contains("Asegurado:")
							&& newcontenido.split("\n")[i].contains("C.P:")) {
						modelo.setCteNombre((newcontenido.split("\n")[i].split("Asegurado:")[1].split("C.P:")[0]
								.replace("###", " ")).trim());
						modelo.setCp(newcontenido.split("\n")[i].split("C.P:")[1].replace("\r", "").replace("###", ""));
					}
					if (newcontenido.split("\n")[i].contains("Domicilio:")
							&& newcontenido.split("\n")[i].contains("Teléfono")) {
						modelo.setCteDireccion((newcontenido.split("\n")[i].split("Domicilio:")[1].split("Teléfono")[0]
								+ " " + newcontenido.split("\n")[i + 1].split("RFC:")[0]).replace("###", " ")
										.replace("\r", "").trim());
					}
					if(modelo.getCp().length() == 0 && newcontenido.split("\n")[i].contains("C.P:")) {
						modelo.setCp(newcontenido.split("\n")[i].split("C.P:")[1].replace("###", "").trim());
					}
					
					if (newcontenido.split("\n")[i].contains("RFC:")) {
						modelo.setRfc(newcontenido.split("\n")[i].split("RFC:")[1].replace("\r", "").replace("###", "")
								.trim());
					}
					if (newcontenido.split("\n")[i].contains("Moneda:")
							&& newcontenido.split("\n")[i].contains("pago")) {
						modelo.setMoneda(fn.moneda(newcontenido.split("\n")[i].split("Moneda:")[1].split("Forma")[0]
								.replace("###", "").trim()));
						modelo.setFormaPago(fn.formaPago(newcontenido.split("\n")[i].split("pago:")[1].replace("\r", "")
								.replace("###", "").trim()));
					}
					if (newcontenido.split("\n")[i].contains("emisión:")
							&& newcontenido.split("\n")[i].contains("Descuento")) {
						String x = newcontenido.split("\n")[i].split("emisión:")[1].split("Descuento")[0]
								.replace("###", "").trim().replace(" ", "###");
						x = x.split("###")[0] + "-" + x.split("###")[1] + "-" + x.split("###")[2];
						modelo.setFechaEmision(fn.formatDateMonthCadena(x));
					}
					if (newcontenido.split("\n")[i].contains("Paquete:")) {
						modelo.setPlan(newcontenido.split("\n")[i].split("Paquete:")[1].replace("\r", "")
								.replace("###", "").trim());
					}
					if (newcontenido.split("\n")[i].contains("agente:")) {
						modelo.setCveAgente(newcontenido.split("\n")[i].split("agente:")[1].replace("\r", "")
								.replace("###", "").trim());
					}
				}
			}


			// PRIMAS
			inicio = contenido.indexOf("o especificación");
			if(inicio == -1) {
				inicio = contenido.indexOf("Prima Neta");
			}
			fin = contenido.indexOf("Notas del riesgo");
			if(fin == -1) {
				fin = contenido.indexOf("Artículo");
			}

			if (inicio > -1 && fin > inicio) {
				newcontenido = contenido.substring(inicio, fin).replace("@@@", "");
				for (int i = 0; i < newcontenido.split("\n").length; i++) {
					if (newcontenido.split("\n")[i].contains("Neta")) {
						modelo.setPrimaneta(fn.castBigDecimal(fn.castDouble(newcontenido.split("\n")[i].split("Neta")[1]
								.replace("###", "").replace("\r", "").trim())));
					}
				
					if (newcontenido.split("\n")[i].contains("Financiamiento")
							&& newcontenido.split("\n")[i].contains("expedición")) {
						
						modelo.setRecargo(fn.castBigDecimal(
								fn.castDouble(newcontenido.split("\n")[i].split("fraccionado")[1].split("Gastos")[0]
										.replace("###", "").replace("\r", "").trim())));
						modelo.setDerecho(
								fn.castBigDecimal(fn.castDouble(newcontenido.split("\n")[i].split("expedición")[1]
										.replace("###", "").replace("\r", "").trim())));
					}
					
					if(modelo.getRecargo().intValue() == 0 && newcontenido.split("\n")[i].contains("fraccionado") ) {
						modelo.setRecargo(
								fn.castBigDecimal(fn.castDouble(newcontenido.split("\n")[i].split("fraccionado")[1]
										.replace("###", "").replace("\r", "").trim())));
					}
					
					if(modelo.getDerecho().intValue() == 0 && newcontenido.split("\n")[i].contains("expedición") ) {
						modelo.setDerecho(
								fn.castBigDecimal(fn.castDouble(newcontenido.split("\n")[i].split("expedición")[1]
										.replace("###", "").replace("\r", "").trim())));
					}
					
					if (newcontenido.split("\n")[i].contains("I.V.A.")) {
						modelo.setIva(fn.castBigDecimal(fn.castDouble(newcontenido.split("\n")[i].split("I.V.A.")[1]
								.replace("###", "").replace("\r", "").trim())));
					}
					if (newcontenido.split("\n")[i].contains("Total:")) {
						modelo.setPrimaTotal(
								fn.castBigDecimal(fn.castDouble(newcontenido.split("\n")[i].split("Total:")[1]
										.replace("###", "").replace("\r", "").trim())));
					}

				}
			}

			if(recibos.contains("Desglose de pago")){
				modelo.setAgente(recibos.split("Clave interna del agente:")[1].split("Desglose de pago")[0]
						.replace("###", "").replace("\r\n", "").trim());
			}

			// UBICACIONES
			inicio = contenido.indexOf("Características del riesgo");
			fin = contenido.indexOf("Prima");
			List<EstructuraUbicacionesModel> ubicaciones = new ArrayList<>();

			if (inicio > -1 && fin > inicio) {
				newcontenido = contenido.substring(inicio, fin);
				EstructuraUbicacionesModel ubicacion = new EstructuraUbicacionesModel();
				for (int i = 0; i < newcontenido.split("\n").length; i++) {
					if (newcontenido.split("\n")[i].contains("Dirección:")) {
						ubicacion.setCalle(newcontenido.split("\n")[i].split("Dirección:")[1].replace("###", "")
								.replace("\r", ""));
					}
					if (newcontenido.split("\n")[i].contains(ConstantsValue.TECHO)) {
						ubicacion.setTechos(fn.material(newcontenido.split("\n")[i].split(ConstantsValue.TECHO)[1].split("Tipo")[0].toUpperCase().replace("###", "")));
						ubicacion.setMuros(fn.material(newcontenido.split("\n")[i].split(ConstantsValue.TECHO)[1].split("Tipo")[0].toUpperCase().replace("###", "")));
					}
					if (newcontenido.split("\n")[i].contains(ConstantsValue.TECHOS)) {				
						ubicacion.setTechos(fn.material(newcontenido.split("\n")[i].split(ConstantsValue.TECHOS)[1].split("Incendio:")[1].toUpperCase().replace("###", "")));						
						ubicacion.setMuros(fn.material(newcontenido.split("\n")[i].split(ConstantsValue.MUROS)[1].split(ConstantsValue.TECHOS)[0].toUpperCase().replace("###", "").trim()));
					}
					
					if (newcontenido.split("\n")[i].contains(ConstantsValue.NIVELES)) {						
						if(newcontenido.split("\n")[i].split(ConstantsValue.NIVELES)[1].contains("No. Sótanos:")) {
							ubicacion.setNiveles(Integer.parseInt(newcontenido.split("\n")[i].split(ConstantsValue.NIVELES)[1].split("No. Sótanos:")[0].replace("###", "").replace("\r", "")));
						}else {
							ubicacion.setNiveles(Integer.parseInt(newcontenido.split("\n")[i].split(ConstantsValue.NIVELES)[1].split("En qué piso")[0].replace("###", "").replace("\r", "")));	
						}
						
					}
					if (newcontenido.split("\n")[i].contains("Núm. pisos incendio:")) {
						ubicacion.setNiveles(Integer.parseInt(newcontenido.split("\n")[i].split("Núm. pisos")[1].split("incendio:")[1].replace("###", "").replace("\r", "")));
					}
					if (newcontenido.split("\n")[i].contains("Giro:")) {
						ubicacion.setGiro(newcontenido.split("\n")[i].split("Giro:")[1].replace("###", "").trim());
					}

				}
				ubicaciones.add(ubicacion);

				modelo.setUbicaciones(ubicaciones);
			}

			List<EstructuraCoberturasModel> coberturas = new ArrayList<>();
			// Cobertutas


			inicio = contenido.indexOf("Tipo Vivienda");
			if(inicio == -1) {
				inicio = contenido.indexOf("Muro de Contención:");
			}
			
			if(inicio == -1) {
				inicio = contenido.indexOf("Tipo Techo");
			}
			
			if(inicio == -1) {
				inicio = contenido.indexOf("Uso Camión:");
			}
			if(inicio == -1) {
				inicio = contenido.indexOf("Secciones amparadas");
			}
			
		    fin = contenido.indexOf("Prima Neta");
		    

	
			String nombre = "";
			StringBuilder deducible = new StringBuilder();

			if (inicio > -1 && fin > -1 && inicio < fin) {
				newcontenido = contenido.substring(inicio, fin).replace("@@@", "").replace("###Prima", "").trim();
				boolean existeTituloCoaseguro = newcontenido.toUpperCase().contains("COASEGURO");
				int index = 0;
				StringBuilder coberturasNombreIncompleto = new StringBuilder();
				
				for (String x : newcontenido.split("\r\n")) {
					if (!x.contains("Tipo Vivienda") && !x.contains("Coberturas###Suma") && !x.contains("página#") && !x.contains("No. Sótanos") && !x.contains("Tipo Techo")) {
						x = completaTextoCoberturas(newcontenido.split("\r\n"),index,coberturasNombreIncompleto);
						resultado.append(x.trim()).append("\r\n");
					}
					index++;
				}
				if (resultado.toString().split("\r\n").length > 1) {
					String seccion = "";
					StringBuilder sumaAsegurada = new StringBuilder();
					String coaseguro = "";
					String auxiliar = "";
					String[] arrResultado = resultado.toString().split("\r\n");
					for (int i = 0; i < arrResultado.length; i++) {
						EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();
						String a = "";
						String b = "";
						String c = "";
						a = arrResultado[i];
						if (a.contains(ConstantsValue.SECCION)) {
							seccion = a;
						} else {
							if (i + 1 < arrResultado.length) {
								b = arrResultado[i + 1].trim();
							}
							if (i + 2 < arrResultado.length) {
								c = arrResultado[i + 2].trim();
							}

							if ((a.split("###").length == 3 || a.split("###").length == 4) && fn.castDouble(a.split("###")[0]) == null) {
								nombre = a.split("###")[0].trim();
												
								coaseguro = "";
								//Suma asegurada
								sumaAsegurada = new StringBuilder();
								sumaAsegurada.append(a.split("###")[1].trim());
								if(a.split("###")[1].equalsIgnoreCase("Sublimite de") && b.split("###").length>0) {
									auxiliar = b.split("###")[0];
									if(coberturasNombreIncompleto.toString().contains(auxiliar)) {
										auxiliar = b.split("###")[1];
										nombre = b.split("###")[0];
										b = b.replace(nombre+"###", "");
										arrResultado[i+1] = arrResultado[i+1].replace(nombre+"###", "");
									}
									sumaAsegurada.append(" ").append(auxiliar);
									b = b.replace(auxiliar+"###", "");
								}
								
								//Coaseguro
								if(existeTituloCoaseguro) {
									if(a.split("###").length == 4) {
										coaseguro = a.split("###")[3];
									}else if(b.split("###").length>0){
										coaseguro = b.split("###")[1];
									}
								}
							
								
								deducible.append(a.split("###")[2].trim());
								if (deducible.toString().contains("de la pérdida")
										|| deducible.toString().contains("del eq. dañado")
										|| deducible.toString().contains("sobre el monto")
										|| deducible.toString().contains("de reposición")
										|| deducible.toString().contains("de reposicion")
										|| deducible.toString().contains("suma asegurada")) {
									if ((b.split("###").length == 1 || b.split("###").length == 2)&& !b.contains(ConstantsValue.SECCION)) {
										if( b.split("###").length == 2){
											deducible.append(" ").append(b.split("###")[0]);
										}else {
											deducible.append(" ").append(b);
										}

									}

									if (c.split("###").length == 1 && (b.split("###").length == 1 ||  b.split("###").length == 2)
											&& !c.contains(ConstantsValue.SECCION) && !coberturasNombreIncompleto.toString().contains(c)) {
										deducible.append(" ").append(c);
									}
									

								} else {
									cobertura.setDeducible(deducible.toString());
								}
								cobertura.setSeccion(seccion.replace("SECCION", "").trim());
								cobertura.setNombre(nombre);
								cobertura.setSa(sumaAsegurada.toString().trim());
								cobertura.setDeducible(deducible.toString().trim());
								cobertura.setCoaseguro(coaseguro);
								deducible = new StringBuilder();
								coberturas.add(cobertura);

							}

						}
					}

				}
				modelo.setCoberturas(coberturas);
			}

			List<EstructuraRecibosModel> recibosList = new ArrayList<>();
			EstructuraRecibosModel recibo = new EstructuraRecibosModel();

			if (modelo.getFormaPago() == 1 && recibosList.isEmpty()) {

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
				recibo.setIva(modelo.getIva());
				recibo.setPrimaTotal(modelo.getPrimaTotal());
				recibo.setAjusteUno(modelo.getAjusteUno());
				recibo.setAjusteDos(modelo.getAjusteDos());
				recibo.setCargoExtra(modelo.getCargoExtra());
				recibosList.add(recibo);

			}

			modelo.setRecibos(recibosList);

			return modelo;
		} catch (Exception ex) {
			modelo.setError(
					ChubbDiversosModel.this.getClass().getTypeName() + " | " + ex.getMessage() + " | " + ex.getCause());
			return modelo;
		}
	}
	
	private void obtenerEndoso(String newContenido) {
		int inicio = newContenido.indexOf("Endoso:");
		int fin = newContenido.indexOf("Tipo de endoso");
		
		if(inicio>-1 && inicio<fin) {
			String text = newContenido.split("Endoso:")[1].split("Tipo de endoso")[0];
			modelo.setEndoso(text.replace("###","").replace("\n", "").trim());
		}
		
	}
	
	private String completaTextoCoberturas(String[] arrTexto,int i, StringBuilder coberturasNombreIncompleto) {
		String texto =  arrTexto[i];
		if(texto.contains("Según condición Fenómenos") && !texto.contains("Hidrometeorológicos")) {
			texto =texto.replace("Según condición Fenómenos", "Según condición Fenómenos Hidrometeorológicos");
		}else
		if(texto.contains("###") && (i-1)>-1) {
			switch (texto.split("###")[0]) {
			case "sublimite":
				texto = completaTextoConLineaSuperior(arrTexto, i,coberturasNombreIncompleto, "sublimite","Robo de bienes y Valores de Empleados y Clientes");
				break;
			case "CONTEN":
				texto = completaTextoConLineaSuperior(arrTexto, i, coberturasNombreIncompleto, "CONTEN", "COBERTURA AMPLIA DE INCENDIO PARA");
				break;
			case "SUB":
				texto = completaTextoConLineaSuperior(arrTexto, i,coberturasNombreIncompleto, "SUB","REMOCION DE ESCOMBROS CONTENIDOS");
				break;
			case "MOVIL DENTRO Y FUERA DEL PREDIO":
				texto = completaTextoConLineaSuperior(arrTexto, i, coberturasNombreIncompleto, "MOVIL DENTRO Y FUERA DEL PREDIO", "ROBO CON VIOLENCIA Y-O ASALTO EQUIPO");
				break;
			case "FIJO DENTRO DEL PREDIO":
				if (texto.split("###").length == 4 && arrTexto[i - 1].contains("ROBO CON VIOLENCIA Y-O ASALTO DE EQUIPO") && arrTexto[i - 1].contains("###")) {
					String[] auxiliar = arrTexto[i - 1].split("###");
					if (auxiliar[auxiliar.length - 1].contains("del valor de reposición")) {
						String deducible = texto.split("###")[2];
						String inicioDeducible = auxiliar[auxiliar.length - 1].replace("\r", "");

						texto.split("###")[2] = texto.split("###")[2].concat(" del valor de reposición");
						texto = texto
								.replace("FIJO DENTRO DEL PREDIO",
										"ROBO CON VIOLENCIA Y-O ASALTO DE EQUIPO FIJO DENTRO DEL PREDIO")
								.replace(deducible, inicioDeducible + " " + deducible);
					}

				}
				break;
			default:
				break;
			}

		}
		return texto;
	}
	
	private String completaTextoConLineaSuperior(String[] arrTexto,int i,StringBuilder coberturasNombreIncompleto, String textoOld, String textSuperiorAcompletar) {
		String texto = arrTexto[i];
		if (arrTexto[i - 1].contains(textSuperiorAcompletar)) {
				texto = texto.replace(textoOld, textSuperiorAcompletar + " " + textoOld);
				coberturasNombreIncompleto.append(textSuperiorAcompletar + " " + textoOld).append(",");
		}

		return texto;
	}
	
}
