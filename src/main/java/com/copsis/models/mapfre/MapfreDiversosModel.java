package com.copsis.models.mapfre;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.copsis.constants.ConstantsValue;
import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraCoberturasModel;
import com.copsis.models.EstructuraJsonModel;
import com.copsis.models.EstructuraRecibosModel;
import com.copsis.models.EstructuraUbicacionesModel;

public class MapfreDiversosModel {

	// Clases
	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
	// Variables
	private String inicontenido = "";
	private String contenido = "";
	private String newcontenido = "";
	private String seccion = "";
	private String ubicacionesTex = "";
	private String resultado = "";
	private String recibosText;
	private int inicio = 0;
	private int fin = 0;
	private int index = 0;
	private BigDecimal restoPrimaTotal = BigDecimal.ZERO;
	private BigDecimal restoDerecho = BigDecimal.ZERO;
	private BigDecimal restoIva = BigDecimal.ZERO;
	private BigDecimal restoRecargo = BigDecimal.ZERO;
	private BigDecimal restoPrimaNeta = BigDecimal.ZERO;
	private BigDecimal restoAjusteUno = BigDecimal.ZERO;
	private BigDecimal restoAjusteDos = BigDecimal.ZERO;
	private BigDecimal restoCargoExtra = BigDecimal.ZERO;
	private String cp, calle, muro, techo, piso = "";

	// constructor
	public MapfreDiversosModel(String contenido, String recibos, String Ubicaiones) {
		this.contenido = contenido;
		this.recibosText = recibos;
		this.ubicacionesTex = Ubicaiones;
	}

	public EstructuraJsonModel procesar() {
		StringBuilder ubicacionest = new StringBuilder();
		boolean isVersionMayusculas = false;
		inicontenido = fn.fixContenido(contenido);
		contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());
		contenido = contenido.replace("Póliza Número    :", "Póliza Número:")
				.replace("Póliza número    :", "Póliza número:")
				.replace("EMPRESARIAL       Endoso", "EMPRESARIAL       ###Endoso")
				.replace(", MULTIPLE", ",###MULTIPLE").replace("Vig encia", "Vigencia")
				.replace("Clave de", "###Clave de").replace("T e l . :", "Tel.:")
				.replace(" C O P S E ,  A G E NTE", "COPSE AGENTE").replace("Cliente MAPFRE   :", "Cliente MAPFRE:")
				.replace("Cliente Mapfre###:", "Cliente Mapfre:").replace("Cobro:", "cobro:")// Cobro:
				.replace("Map fre Tepeyac", "Mapfre Tepeyac")
				.replace("Map fre México S.A. denominada", "Mapfre México S.A. denominada")
				.replaceFirst("R . F . C", "R.F.C").replaceFirst("Con tratante:", "Contratante:")
				.replace("C . P . ", "C.P.").replace("C . P :", "C.P:")
				.replace("T e l :", "Tel:").replace("VIGENCIA", "Vigencia")
				.replace("HASTA###LAS###12:00", "hasta las 12:00").replace("CLIENTE###MAPFRE", "CLIENTE MAPFRE")
				.replace("FORMA###DE###PAGO", "FORMA DE PAGO").replace("óliza número ###:", "óliza número:")
				.replace("Póliza Número :", "Póliza Número:")
				.replace("Prima Neta:", "Prima neta:")
				.replace("D O LARES U.S.A.", "DOLARES U.S.A.");

		try {


			modelo.setTipo(7);
			modelo.setCia(22);

			
			inicio = contenido.indexOf("Póliza Número:");
			if (inicio == -1) {
				inicio = contenido.indexOf("Póliza número:");
				if (inicio == -1) {
					inicio = contenido.indexOf("Póliza número");
				}
			}

			if (inicio > -1) {
				modelo.setPoliza(contenido.substring(inicio + 14, inicio + 60).split("\r\n")[0].replace(":", "")
						.replace(" ", "").trim().replace("###", ""));
			}

			if (modelo.getPoliza().length() == 0) {
				inicio = contenido.indexOf("PÓLIZA-ENDOSO");
				fin = contenido.indexOf("FECHA DE EMISIÓN");

				if ((inicio > -1 && inicio < fin)) {
					String newcontenido = contenido.split("PÓLIZA-ENDOSO")[1].split("FECHA DE EMISIÓN")[0]
							.replace("@@@", "").trim();
					isVersionMayusculas = true;
					if (newcontenido.contains("###") && newcontenido.contains("-")) {
						newcontenido = newcontenido.split("###")[1];
						modelo.setPoliza(newcontenido.split("-")[0].trim().replace("###", ""));
						modelo.setEndoso(newcontenido.split("-")[1].trim().replace("###", ""));
					}
				}
			}

			if (modelo.getPoliza().length() == 0) {
				modelo.setPoliza(fn.obtenerPolizaRegex(contenido, 13).replace("###", ""));
			}

			// endoso
			inicio = contenido.indexOf("Endoso Número");
			if (inicio == -1) {
				inicio = contenido.indexOf("so número###:");
			}
			if (inicio > -1) {
				modelo.setEndoso(
						contenido.substring(inicio + 13, inicio + 100).split("\r\n")[0].replace(":", "").replace("###", "").trim());
			}

			// cte_nombre
			inicio = contenido.indexOf("Contratante:");
			if (inicio == -1) {
				inicio = contenido.indexOf("CONTRATANTE:");
			}

			if (inicio > -1) {
				newcontenido = contenido.substring(inicio + 12, inicio + 150).split("\r\n")[0];

				if (newcontenido.contains("R.F.C")) {
					modelo.setCteNombre(fn.gatos(
							contenido.substring(inicio + 12, inicio + 150).split("\r\n")[0].split("R.F.C")[0].trim()));
				} else if (newcontenido.contains("DOMICILIO")) {
					modelo.setCteNombre(newcontenido.split("DOMICILIO")[0].replace("###", "").replace(":", "").trim());
				}

			} else {
				inicio = contenido.indexOf("R.F.C");
				if (inicio > -1) {
					modelo.setCteNombre(fn.gatos(
							contenido.substring(inicio + 7, inicio + 150).split("\r\n")[0].split("R.F.C:")[0].trim()));
				}
			}

			// rfc
			inicio = contenido.indexOf("Contratante");
			fin = contenido.indexOf("Domicilio");
			if (fin == -1) {
				fin = contenido.indexOf("icilio");
			}
			if (inicio > -1 && fin > -1 && inicio < fin) {
			
				newcontenido = contenido.substring(inicio, fin);
				inicio = newcontenido.indexOf("R.F.C");

				if (inicio > -1) {
					resultado = fn.gatos(newcontenido.substring(inicio + 6, newcontenido.indexOf("\r\n", inicio + 6))
							.replace(":", "").replace(" ", ""));
					modelo.setRfc(resultado.replace("\r\n", ""));
				}
			}

			if (modelo.getRfc().length() == 0) {
				inicio = contenido.indexOf("R.F.C");
				fin = contenido.indexOf("C.P");

				if (inicio > -1 && inicio < fin) {
					String texto = contenido.substring(inicio, fin).replace("C.P", "C/P");
					modelo.setRfc(texto.split("R.F.C")[1].split("C/P")[0].replace("###", "").replace(":", "").replace("@@@", "").replace("\r\n", "").trim());
				}
			}

			// cp
			modelo.setCp(fn.obtenerCPRegex(contenido));
			if (modelo.getCp().length() == 0) {
				inicio = contenido.indexOf("C.P");
				
				String texto = contenido.substring(inicio).replace("C.P", "C/P");
			
				
				texto = texto.split("C/P")[1].split("\n")[0].replace(":", "").replace(".", "").replace(",", "").replace("###", "").trim();
				
				if (Boolean.TRUE.equals(fn.isvalidCp(texto))) {
					modelo.setCp(texto);
				}
			}
	
			if(modelo.getCp().length() == 0 && newcontenido.indexOf("C.P") > -1) {
		
			    modelo.setCp(newcontenido.split("C.P:")[1].replace(" ", "").trim().substring(0,5));
			}
			if(modelo.getCp().length() == 0 ){
				String codepostal = contenido.split("Cliente MAPFRE")[1];
				 modelo.setCp(codepostal.split("C.P:")[1].replace(" ", "").trim().substring(0,5));
				
			}

			// cte_direccion
			inicio = contenido.indexOf("icilio:");
			if (inicio == -1) {
				inicio = contenido.indexOf("ICILIO:");
			}
			if (inicio > -1) {
				newcontenido = contenido.substring(inicio + 7, inicio + 150);
				if (newcontenido.contains("Tel")) {
					modelo.setCteDireccion(fn.gatos(newcontenido.split("Tel")[0].replace(".", "").trim()));
				} else if (newcontenido.contains("R.F.C")) {
					modelo.setCteDireccion(fn.eliminaSpacios(fn.gatos(
							newcontenido.split("R.F.C")[0].replace("\r", " ").replace("\n", "").replace("@@@", ""))));
				}
			}

			// vigencia_de
			inicio = contenido.indexOf("Vigencia");
			if (inicio > -1) {
				newcontenido = contenido.substring(inicio + 8, inicio + 200).split("\r\n")[0];
				modelo.setVigenciaDe(fn.formatDateMonthCadena(fn.obtenerFecha(newcontenido)));
			}

			// agente
			// cve_agente
			// vigencia_a
			inicio = contenido.indexOf("Hasta las 12:00");
			inicio =  inicio == -1 ? inicio = contenido.indexOf("hasta las 12:00"): inicio;
			
			fin = contenido.indexOf("Fecha de emisión");
		    fin = fin == -1  ? fin = contenido.indexOf("CLIENTE MAPFRE"):fin;
			fin = fin == -1  ? fin = contenido.indexOf("Si Usted realiza el"):fin;
	

			if (inicio > -1 && fin > inicio) {
				newcontenido = contenido.substring(inicio + 15, fin).replace("@@@", "");

				if (newcontenido.contains("de:")) {
					newcontenido = fn.remplazaGrupoSpace(newcontenido.split("de:")[1].trim()).replace("######", "###");
					modelo.setVigenciaA(fn.formatDateMonthCadena(fn.obtenerFecha(newcontenido)));

					if (newcontenido.split("###").length == 3) {
						modelo.setCveAgente(newcontenido.split("###")[1].trim());
						modelo.setAgente(newcontenido.split("###")[2].trim());
					}
				} else if (newcontenido.contains("DEL:")) {
					newcontenido = newcontenido.split("DEL:")[1].replace("###", "");
					if (newcontenido.split("-").length == 3) {
						modelo.setVigenciaA(fn.formatDateMonthCadena(fn.obtenerFecha(newcontenido.trim())));
					}
				}
			}

			if (modelo.getAgente().length() == 0 && modelo.getCveAgente().length() == 0) {
				if (contenido.contains("AGENTE:") && contenido.contains("CLAVE DE AGENTE:")) {
					modelo.setAgente(contenido.split("AGENTE:")[1].split("\n")[0].trim());
					modelo.setCveAgente(contenido.split("CLAVE DE AGENTE:")[1].split("\n")[0].trim());
				}
			}
			// moneda
			// forma_pago
			inicio = contenido.indexOf("Cobro:");
			if (inicio == -1) {
				inicio = contenido.indexOf("cobro:");
			}
			fin = contenido.indexOf("Prima neta:");
		
			if (inicio > -1 && fin > inicio) {
				newcontenido = fn
						.remplazaGrupoSpace(
								contenido.substring(inicio + 6, fin).replace("@@@", "").trim().split("\r\n")[0])
						.replace("#", "");
						
				if (newcontenido.split(" ").length == 5) {
					modelo.setFormaPago(fn.formaPago(newcontenido.split(" ")[1].trim()));
					modelo.setMoneda(fn.moneda(newcontenido.split(" ")[2].replace("$", "").trim()));
				}
				if(modelo.getFormaPago() == 0){
					modelo.setFormaPago(fn.formaPagoSring(newcontenido.split(" ")[1].trim()));
				}
				if(modelo.getMoneda() == 0){
					modelo.setMoneda(fn.buscaMonedaEnTexto(newcontenido.split(" ")[2].replace("$", "").trim()));
				}

			}

			
			if (modelo.getFormaPago() == 0 && contenido.contains("FORMA DE PAGO:")) {
				String aux = fn.gatos(contenido.split("FORMA DE PAGO:")[1].split("\n")[0]);
				if (aux.contains("###")) {
					aux = aux.split("###")[0];
					modelo.setFormaPago(fn.formaPago(aux));
				}
			}

			if (modelo.getMoneda() == 0 && contenido.contains("MONEDA:")) {
				String aux = fn.gatos(contenido.split("MONEDA:")[1].split("\n")[0]);
				if (aux.contains("###")) {
					aux = aux.split("###")[0];
					modelo.setMoneda(fn.moneda(aux.trim()));
				}

			}

			// fecha_emision
			inicio = contenido.indexOf("Gestor de cobro:");
			if (inicio == -1) {
				inicio = contenido.indexOf("FECHA DE EMISIÓN");
			}
			if (inicio > -1) {
				newcontenido = contenido.substring(inicio + 16, inicio + 150).trim().split("\r\n")[0];
			 if(newcontenido.indexOf("-") > -1){
				modelo.setFechaEmision(fn.formatDateMonthCadena(fn.obtenerFecha(newcontenido)));
			 }
		    
				
			}

			// id_cliente
			obtenerIdCte();
			// prima_neta
			// derecho
			// recargo
			// iva
			// prima_total
			inicio = contenido.indexOf("Prima neta:");
			fin = contenido.indexOf("Mapfre Tepeyac");

		
			if (fin == -1) {
				fin = contenido.indexOf("Mapfre México S.A. denominada");
			}
			if (fin == -1) {
				fin = contenido.indexOf("Mapfre México, S. A. denominada");
			}
			if (fin == -1) {
                fin = contenido.indexOf("MAPFRE MÉXICO, S.A., denominada");
            }

			
		

			if (inicio > -1 && fin > inicio) {
				newcontenido = contenido.substring(inicio, fin);
				
				if (newcontenido.contains("otal:")) {
					List<String> listValores = fn.obtenerListNumeros(newcontenido);
					if (listValores.size() == 6) {
						modelo.setPrimaneta(fn.castBigDecimal(fn.preparaPrimas(listValores.get(0).trim())));
						modelo.setRecargo(fn.castBigDecimal(fn.preparaPrimas(listValores.get(2).trim())));
						modelo.setDerecho(fn.castBigDecimal(fn.preparaPrimas(listValores.get(3).trim())));
						modelo.setIva(fn.castBigDecimal(fn.preparaPrimas(listValores.get(4).trim())));
						modelo.setPrimaTotal(fn.castBigDecimal(fn.preparaPrimas(listValores.get(5).trim())));
					}
					if (listValores.size() == 5) {
                        modelo.setPrimaneta(fn.castBigDecimal(fn.preparaPrimas(listValores.get(0).trim())));
                        modelo.setRecargo(fn.castBigDecimal(fn.preparaPrimas(listValores.get(1).trim())));
                        modelo.setDerecho(fn.castBigDecimal(fn.preparaPrimas(listValores.get(2).trim())));
                        modelo.setIva(fn.castBigDecimal(fn.preparaPrimas(listValores.get(3).trim())));
                        modelo.setPrimaTotal(fn.castBigDecimal(fn.preparaPrimas(listValores.get(4).trim())));
                    }
				}
			   
			}
			if (isVersionMayusculas) {
				obtenerPrimas(contenido, modelo);
			}

			// plan
			inicio = contenido.indexOf("Póliza Número:");
			fin = contenido.indexOf("###Endoso Número");
			if (inicio > -1 && fin > -1 && inicio < fin) {
				newcontenido = contenido.substring(inicio + 14, fin);
				if (newcontenido.split("\r\n").length == 2) {
					newcontenido = newcontenido.split("\r\n")[1];
					if (newcontenido.split("###").length == 2) {
						modelo.setPlan(newcontenido.split("###")[1].replace("\"", "").replace("@@@", "").replace("\r\n", "").trim());
					}
				}
			} else {
				inicio = contenido.indexOf("ocumento:");
				fin = contenido.indexOf("óliza número:");

				if (inicio > -1 && fin > -1 && inicio < fin) {
					newcontenido = contenido.substring(inicio + 9, fin);
					if (newcontenido.split("\r\n").length == 2
							&& newcontenido.split("\r\n")[1].split("###").length == 2) {

						modelo.setPlan(newcontenido.split("\r\n")[1].split("###")[0].replace("\"", "")
								.replace("@@@", "").trim());

					}
				}
			}

			if (modelo.getPlan().length() == 0 && contenido.contains("PÓLIZA-ENDOSO")) {
				String texto = contenido.split("PÓLIZA-ENDOSO")[0].replace("@@@", "").trim();
				if (texto.length() > 0 && texto.split("\n").length > 0) {
					modelo.setPlan(texto.split("\n")[0].replace("\r", ""));
				}
			}

			// renovacion
			inicio = inicontenido.indexOf("SUMA ASEGURADA  DEDUCIBLE");
			fin = inicontenido.indexOf("ABREVIATURAS");

			if(inicio == -1) {
			    inicio = inicontenido.indexOf("SUMA ASEGURADA    LIMITE MAXIMO");
			}
			if(inicio == -1) {
			    inicio = inicontenido.indexOf("SUMA ASEGURADA### DEDUCIBLES");
			}

			if(inicio == -1) {
			    inicio = inicontenido.indexOf("AMPARADAS");
			}
			if(fin == -1) {
			    fin = inicontenido.indexOf("VALOR PARA SEGURO");
			}
			
			if(fin == -1) {
			    fin = inicontenido.indexOf("Las condiciones generales");
			}
			
		

			List<EstructuraCoberturasModel> coberturas = new ArrayList<>();
			if (inicio > -1 && fin > inicio) {
				newcontenido = inicontenido.substring(inicio, fin).replace("@@@", "").trim();
			
				resultado = "";
				for (String x : newcontenido.split("\r\n")) {
					if (!x.contains("BIENES Y RIESGOS CUBIERTOS") && !x.contains("---") && !x.contains("DEDUCIBLE")) {
						resultado = fn.gatos(x.replaceAll("  +", "###").trim());
						resultado = resultado.replace("A.###MIN", "A. MIN").replace("###DSMG", " DSMG")
								.replace(".###MIN", ". MIN").replace(".###MAX", ". MAX")
								.replace("###$###", "###$");
						EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();

						switch (resultado.split("###").length) {
						case 2:
							cobertura.setNombre(resultado.split("###")[0].trim());
							cobertura.setSa(resultado.split("###")[1].trim());
							coberturas.add(cobertura);
							break;
						case 3:
							cobertura.setNombre(resultado.split("###")[0].trim());
							cobertura.setSa(resultado.split("###")[1].trim());
							if (resultado.split("###")[2].trim().length() == 3) {
								cobertura.setCoaseguro(resultado.split("###")[2].trim());
							} else {
								cobertura.setDeducible(resultado.split("###")[2].trim());
							}
							coberturas.add(cobertura);
							break;
						case 4:
						
							cobertura.setNombre(resultado.split("###")[0].trim());
							cobertura.setSa(resultado.split("###")[1].trim());
							cobertura.setDeducible(resultado.split("###")[2].trim());
							cobertura.setCoaseguro(resultado.split("###")[3].trim());
							coberturas.add(cobertura);
							break;
						default:
							break;
						}
					}
				}
			} else {// SEGUNDO CASO
				inicio = inicontenido.indexOf("SECCIÓN   BIENES Y RIESGOS");
				fin = inicontenido.indexOf("ABREVIATURAS");

				if (inicio == -1) {
					inicio = inicontenido.indexOf("BIENES Y RIESGOS AMPARADOS");
				}
				if (fin == -1) {
					fin = inicontenido.indexOf("RENOVACION AUTOMATICA");
				}


				if (inicio > -1 && fin > inicio) {
					newcontenido = inicontenido.substring(inicio, fin).replace("@@@", "").trim();
					if (newcontenido.contains("DE CONFORMIDAD CON LOS ARTÍCULOS")) {
						newcontenido = newcontenido.split("DE CONFORMIDAD CON LOS ARTÍCULOS")[0];
					} else if (newcontenido.contains(ConstantsValue.RENOVACION_AUTOM)) {
						newcontenido = newcontenido.split(ConstantsValue.RENOVACION_AUTOM)[0];
					}

					resultado = "";
					for (String x : newcontenido.split("\r\n")) {
						if (!x.contains("SUMA ASEGURADA") && !x.contains("---")) {
							EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();
							x = fn.gatos(x.replaceAll("  +", "###").trim());
							x = x.replace("###$###", "###$ ");
							switch (x.split("###").length) {
							case 2:
								switch (x.split("###")[0].trim()) {
								case "I":
								case "II":
								case "III":
								case "IV":
								case "V":
								case "VI":
								case "VII":
								case "VIII":
								case "IX":
								case "X":
								case "XI":
								case "XII":
								case "XIII":
								case "XIV":
								case "XV":
									seccion = x.split("###")[0].trim();
									break;
								default:

									cobertura.setNombre(x.split("###")[0].trim());
									cobertura.setSeccion(seccion);
									cobertura.setSa(x.split("###")[1].trim());
									coberturas.add(cobertura);
									break;
								}
								break;
							case 3:
								switch (x.split("###")[0].trim()) {
								case "I":
								case "II":
								case "III":
								case "IV":
								case "V":
								case "VI":
								case "VII":
								case "VIII":
								case "IX":
								case "X":
								case "XI":
								case "XII":
								case "XIII":
								case "XIV":
								case "XV":

									seccion = x.split("###")[0].trim();
									cobertura.setSeccion(seccion);
									cobertura.setNombre(x.split("###")[1].trim());
									cobertura.setSa(x.split("###")[2].trim());
									coberturas.add(cobertura);
									break;
								default:
									cobertura.setSeccion(seccion);
									cobertura.setNombre(x.split("###")[1].trim());
									cobertura.setSa(x.split("###")[2].trim());
									coberturas.add(cobertura);
									break;
								}
								break;
							default:
								break;
							}
						}
					}
				}
			}

			modelo.setCoberturas(coberturas);

			if(modelo.getCoberturas().isEmpty()){
				inicio = contenido.indexOf("SUMA ASEGURADA DEDUCIBLES");
				fin = contenido.indexOf("EXTENSION DE Vigencia:");
			
				String cbo = fn.extracted(inicio, fin, contenido)
				.replace("ORDINARIOS DE TRANSITO 4,500,000.00 3 %", "ORDINARIOS DE TRANSITO### 4,500,000.00###3 %")
				.replace("AMPARADO", "###AMPARADO###");
		
				for (int i = 0; i < cbo.split("\n").length; i++) {
					EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();
					if(cbo.split("\n")[i].contains("%")){
						
						if(cbo.split("\n")[i].split("###").length ==3){
	
							cobertura.setNombre(cbo.split("\n")[i].split("###")[0].trim());
							cobertura.setSa(cbo.split("\n")[i].split("###")[1].trim());
							cobertura.setDeducible(cbo.split("\n")[i].split("###")[2].trim());						
							coberturas.add(cobertura);
						}
					}
				
				}
				modelo.setCoberturas(coberturas);
			}

			// UBICACIONES
			inicio = contenido.indexOf("RIESGOS ASEGURADOS");
			fin = contenido.indexOf("MEDIDAS DE SEGURIDAD");
			
		
			List<EstructuraUbicacionesModel> ubicaciones = new ArrayList<>();

			if (inicio > -1 && fin > inicio) {
				EstructuraUbicacionesModel ubicacion = new EstructuraUbicacionesModel();
				newcontenido = contenido.substring(inicio + 18, fin).replace("@@@", "").trim();
				resultado = newcontenido.split(":")[1].split("CLASIFICACIÓN")[0].trim();
			

				// cp
				if (resultado.contains(", C.P.")) {
					ubicacion.setCp(resultado.split(", C.P.")[1].replace(".", "").split("\r\n")[0].trim());
				}
				// calle
				// colonia
				// no_externo
				// no_interno
				index = 0;
				for (String xx : resultado.split(",")) {
		
					if (index == 0) {
						ubicacion.setCalle(xx.trim());
					}
					if (xx.contains("Col.")) {
						ubicacion.setColonia(xx.trim());
					}
					if (xx.contains("No.")) {
						ubicacion.setNoExterno(xx.trim());
					}
					if (xx.contains("Int.")) {
						ubicacion.setNoInterno(xx.trim());
					}
					index++;
				}

				// nombre
				// giro
				inicio = contenido.indexOf("GIRO ASEGURADO");
				if (inicio > -1) {
					newcontenido = contenido.substring(inicio, contenido.indexOf("\r\n", inicio)).split(":")[1]
							.replace("  .", ".").trim();
					ubicacion.setNombre(newcontenido);
					ubicacion.setGiro(newcontenido);
				}

				// muros
				// techos
				inicio = contenido.indexOf("CARACTERISTICAS");
				if (inicio == -1) {
					inicio = contenido.indexOf("CARACTERÍSTICAS");
				}

				if (inicio > -1) {
					newcontenido = contenido.substring(inicio, contenido.indexOf(".", inicio)).split(":")[1]
							.replace("\r\n", "").replace("CONSTRUCCION", "CONSTRUCCIÓN").trim();
					fin = newcontenido.lastIndexOf(" Y");
					if (fin > -1) {
						resultado = newcontenido.substring(0, fin).split("CONSTRUCCIÓN MURO")[1].trim();
						ubicacion.setMuros(fn.material(resultado));

						resultado = newcontenido.substring(fin + 3, newcontenido.length()).replace("TECHO DE", "")
								.trim();
						ubicacion.setTechos(fn.material(resultado));
					}
				}

				// niveles
				for (String x : newcontenido.split(",")) {
					if (x.contains("PISOS ALTOS")) {
						if (x.contains(" Y ")) {
							resultado = x.split(" Y ")[0].replace("PISOS ALTOS", "").trim();
							if (fn.isNumeric(resultado)) {
								ubicacion.setNiveles(Integer.parseInt(resultado));
							}
							resultado = x.split(" Y ")[1].replace("SOTANOS", "").trim();
							if (fn.isNumeric(resultado)) {
								ubicacion.setSotanos(Integer.parseInt(resultado));
							}
						}
					}
				}

				if (ubicacion.getNiveles() == 0) {
					ubicacion.setNiveles(1);
				}
				if(ubicacion.getNombre().length() > 10){
					ubicaciones.add(ubicacion);
					modelo.setUbicaciones(ubicaciones);
				}
				
				
			}

			

			if (modelo.getUbicaciones().isEmpty()) {
				obtenerUbicacionVersionMayusculas(contenido, modelo);
			}

			List<EstructuraRecibosModel> recibosList = new ArrayList<>();
			EstructuraRecibosModel recibo = new EstructuraRecibosModel();

			if (recibosText.length() > 0) {
				recibosList = recibosExtract();
			}

			// CALCULO RESTO DE RECIBOS
			switch (modelo.getFormaPago()) {
			case 1:
				if (recibosList.size() == 0) {
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

				break;
			case 2:
				if (recibosList.size() == 1) {
					recibo.setSerie("2/2");
					recibo.setVigenciaDe(recibosList.get(0).getVigenciaA());
					recibo.setVigenciaA(modelo.getVigenciaA());
					recibo.setVencimiento("");
					recibo.setPrimaneta(restoPrimaNeta);
					recibo.setPrimaTotal(restoPrimaTotal);
					recibo.setRecargo(restoRecargo);
					recibo.setDerecho(restoDerecho);
					recibo.setIva(restoIva);
					recibo.setAjusteUno(restoAjusteUno);
					recibo.setAjusteDos(restoAjusteDos);
					recibo.setCargoExtra(restoCargoExtra);
					recibosList.add(recibo);

				}
				break;
			case 3:
			case 4:
				if (recibosList.size() >= 1) {
					BigDecimal restoRec = fn.castBigDecimal(fn.getTotalRec(modelo.getFormaPago()) - recibosList.size());
					int totalRec = fn.getTotalRec(modelo.getFormaPago());
					int meses = fn.monthAdd(modelo.getFormaPago());// MESES A AGREGAR POR RECIBO

					for (int i = recibosList.size(); i <= restoRec.intValue(); i++) {

						recibo.setSerie(i + 1 + "/" + totalRec);
						recibo.setVigenciaDe(recibosList.get(i - 1).getVigenciaA());
						if (recibosList.get(i - 1).getVigenciaA().length() == 10) {
							recibo.setVigenciaA(fn.dateAdd(recibosList.get(i - 1).getVigenciaA(), meses, i));
						}
						recibo.setVencimiento("");
						recibo.setPrimaneta(restoPrimaNeta.divide(restoRec, 2, RoundingMode.HALF_EVEN));
						recibo.setPrimaTotal(restoPrimaTotal.divide(restoRec, 2, RoundingMode.HALF_EVEN));
						recibo.setRecargo(restoRecargo.divide(restoRec, 2, RoundingMode.HALF_EVEN));
						recibo.setDerecho(restoDerecho.divide(restoRec, 2, RoundingMode.HALF_EVEN));
						recibo.setIva(restoIva.divide(restoRec, 2, RoundingMode.HALF_EVEN));
						recibo.setAjusteUno(restoAjusteUno.divide(restoRec, 2, RoundingMode.HALF_EVEN));
						recibo.setAjusteDos(restoAjusteDos.divide(restoRec, 2, RoundingMode.HALF_DOWN));
						recibo.setCargoExtra(restoCargoExtra.divide(restoRec, 2, RoundingMode.HALF_EVEN));
						recibosList.add(recibo);
					}
				}

				break;
			case 5:
			case 6:// QUINCENAL, SEMANAL NINGUN PDF DE FORMA DE PAGO SE QUEDA PENDIENTE ESTE CASO
				if (recibosList.size() >= 1) {
					BigDecimal restoRec = fn.castBigDecimal(fn.getTotalRec(modelo.getFormaPago()) - recibosList.size());
					int totalRec = fn.getTotalRec(modelo.getFormaPago());
					for (int i = recibosList.size(); i <= restoRec.intValue(); i++) {

						recibo.setSerie(i + 1 + "/" + totalRec);
						recibo.setPrimaneta(
								restoPrimaNeta.divide(fn.castBigDecimal(restoRec), 2, RoundingMode.HALF_EVEN));
						recibo.setPrimaTotal(restoPrimaTotal.divide(restoRec, 2, RoundingMode.HALF_EVEN));
						recibo.setRecargo(restoRecargo.divide(restoRec, 2, RoundingMode.HALF_EVEN));
						recibo.setDerecho(restoDerecho.divide(restoRec, 2, RoundingMode.HALF_EVEN));
						recibo.setIva(restoIva.divide(restoRec, 2, RoundingMode.HALF_EVEN));
						recibo.setAjusteUno(restoAjusteUno.divide(restoRec, 2, RoundingMode.HALF_EVEN));
						recibo.setAjusteDos(restoAjusteDos.divide(restoRec, 2, RoundingMode.HALF_DOWN));
						recibo.setCargoExtra(restoCargoExtra.divide(restoRec, 2, RoundingMode.HALF_EVEN));
						recibosList.add(recibo);
					}
				}
				break;
			default:
				break;
			}
			modelo.setRecibos(recibosList);

			if (ubicaciones.isEmpty()) {

				for (int i = 0; i < contenido.split("ESPECIFICACION DE BIENES CUBIERTOS").length; i++) {
					if (i > 0) {

						ubicacionest.append(contenido.split("ESPECIFICACION DE BIENES CUBIERTOS")[i]
								.split("BIENES Y RIESGOS CUBIERTOS SUMA ASEGURADA")[0]);
					}
				}

				EstructuraUbicacionesModel ubicacion = new EstructuraUbicacionesModel();

				for (int i = 0; i < ubicacionest.toString().split("\n").length; i++) {
				    
					

					if (ubicacionest.toString().split("\n")[i].contains("C.P.") && ubicacionest.toString().split("\n")[i].split("C.")[1].length() > 8) {
						cp = (ubicacionest.toString().split("\n")[i].split("C.P.")[1].trim().substring(0, 5));											
					}
					if (ubicacionest.toString().split("\n")[i].contains("UBICACION") ) {
						calle = (ubicacionest.toString().split("\n")[i].substring(0,50)).replace("@@@", "");											
					}
					if (ubicacionest.toString().split("\n")[i].contains("MUROS") && ubicacionest.toString().split("\n")[i].split("TEC.")[1].length() > 8) {
						muro = (ubicacionest.toString().split("\n")[i].split("MUROS")[1].split("TECHO.")[0]);											
					}

				   if(cp !=null && calle  !=null && muro !=null) {
					 ubicacion.setCp(cp);
					 ubicacion.setCalle(calle);
					 ubicacion.setMuros(fn.material(muro));
					
				   }

				}
				if(ubicacion.getNombre().length() > 0) {
					ubicaciones.add(ubicacion);
					modelo.setUbicaciones(ubicaciones);
				}
			

			}
			if(modelo.getFechaEmision().length() == 0){
				modelo.setFechaEmision(modelo.getVigenciaDe());
			}

			return modelo;
		} catch (Exception ex) {		
			modelo.setError(MapfreDiversosModel.this.getClass().getTypeName() + " | " + ex.getMessage() + " | "
					+ ex.getCause());
			return modelo;
		}

	}

	private ArrayList<EstructuraRecibosModel> recibosExtract() {
		recibosText = fn.fixContenido(recibosText);
		List<EstructuraRecibosModel> recibosLis = new ArrayList<>();

		try {
			EstructuraRecibosModel recibo = new EstructuraRecibosModel();
			// prima_neta
			inicio = recibosText.indexOf("Prima neta###");
			if (inicio > -1) {
				newcontenido = recibosText.substring(inicio + 13, recibosText.indexOf("\r\n", inicio));
				if (newcontenido.split("###").length == 2) {
					recibo.setPrimaneta(fn.castBigDecimal(fn.preparaPrimas(newcontenido.split("###")[1])));
					restoPrimaNeta = modelo.getPrimaneta().subtract(recibo.getPrimaneta());
				}
			}

			// derecho
			inicio = recibosText.indexOf("Gasto de expedición");
			if (inicio > -1) {
				newcontenido = fn.gatos(recibosText.substring(inicio + 19, inicio + 150).split("\r\n")[0]);
				if (newcontenido.split("###").length == 2) {
					recibo.setDerecho(fn.castBigDecimal(fn.preparaPrimas(newcontenido.split("###")[1])));
					restoDerecho = modelo.getDerecho().subtract(recibo.getDerecho());
				}
			}

			// recargo
			inicio = recibosText.indexOf("Financ. pago fracc.");
			if (inicio > -1) {
				newcontenido = fn.gatos(recibosText.substring(inicio + 19, recibosText.indexOf("\r\n", inicio)));
				if (newcontenido.split("###").length == 2) {
					recibo.setRecargo(fn.castBigDecimal(fn.preparaPrimas(newcontenido.split("###")[1])));
					restoRecargo = modelo.getRecargo().subtract(recibo.getRecargo());
				}
			}

			// iva
			inicio = recibosText.indexOf("IVA 16.00%");
			if (inicio > -1) {
				newcontenido = recibosText.substring(inicio, recibosText.indexOf("\r\n", inicio));
				if (newcontenido.split("###").length == 3) {

					recibo.setIva(fn.castBigDecimal(fn.preparaPrimas(newcontenido.split("###")[2])));
					restoIva = modelo.getIva().subtract(recibo.getIva());
				}
			}

			// prima_total
			inicio = recibosText.indexOf("Total a pagar:");
			if (inicio > -1) {
				newcontenido = recibosText.substring(inicio, recibosText.indexOf("\r\n", inicio));
				if (newcontenido.split("###").length == 3) {
					recibo.setPrimaTotal(fn.castBigDecimal(fn.preparaPrimas(newcontenido.split("###")[2])));
					restoPrimaTotal = modelo.getPrimaTotal().subtract(recibo.getPrimaTotal());
				}
			}

			// vigencia_de
			// vigencia_a
			inicio = recibosText.indexOf("horas del ");
			if (inicio > -1) {
				newcontenido = recibosText.substring(inicio + 10, recibosText.indexOf("\r\n", inicio + 10))
						.split("Gasto de ")[0];
				if (newcontenido.contains("Hasta: 12:00 horas del")) {
					resultado = fn.gatos(newcontenido.split("Hasta: 12:00 horas del")[0]).trim();

					recibo.setVigenciaDe(resultado);
					String resultado2 = fn.gatos(newcontenido.split("Hasta: 12:00 horas del")[1]).trim();
					recibo.setVigenciaA(resultado2);

				}
			}

			// vencimiento
			if (recibo.getVigenciaDe().length() > 0) {
				recibo.setVencimiento(fn.dateAdd(recibo.getVigenciaDe(), 30, 1));
			}

			// serie
			inicio = recibosText.indexOf("Serie de recibo:");
			if (inicio > -1) {
				newcontenido = fn.gatos(recibosText.substring(inicio + 16, recibosText.indexOf("Fecha", inicio + 16)));
				newcontenido = newcontenido.replace("01", "1").replace("02", "2").replace("03", "3").replace("04", "4")
						.replace("05", "5").replace("06", "6").replace("07", "7").replace("08", "8").replace("09", "9");
				recibo.setSerie(newcontenido);
			}

			// recibo_id
			inicio = recibosText.indexOf("serie P No.");

			if (inicio > -1) {
				newcontenido = fn.gatos(recibosText.substring(inicio + 11, recibosText.indexOf("\r\n", inicio + 11)));

				recibo.setReciboId(newcontenido);
			}
			recibosLis.add(recibo);
			return (ArrayList<EstructuraRecibosModel>) recibosLis;
		} catch (Exception ex) {		
			modelo.setError(MapfreDiversosModel.this.getClass().getTypeName() + " | " + ex.getMessage() + " | "
					+ ex.getCause());

			return (ArrayList<EstructuraRecibosModel>) recibosLis;
		}
	}

	private void obtenerIdCte() {
		Pattern pattern = Pattern.compile(ConstantsValue.REGEX_IDCLIENTE_MAPFRE);
		Matcher matcher = pattern.matcher(contenido);
		modelo.setIdCliente(matcher.find() ? matcher.group(3).replace("###", "") : "");
	}

	private void obtenerPrimas(String contenido, EstructuraJsonModel model) {
		int indexInicio = contenido.indexOf("CONCEPTOS###ECONÓMICOS");
		int indexFin = contenido.indexOf("MAPFRE###MÉXICO");

		if (indexInicio > -1 && indexInicio < indexFin) {
			String newContenido = contenido.substring(indexInicio + 20, indexFin).replace("@@@", "")
					.replace("PRIMA###NETA", "PRIMA NETA").replace("PRIMA###TOTAL", "PRIMA TOTAL").replace("\r", "");

			String[] arrContenido = newContenido.split("\n");
			for (int i = 0; i < arrContenido.length; i++) {
				if (arrContenido[i].contains("PRIMA NETA")) {
					String aux = arrContenido[i].split("PRIMA NETA")[1].replace("###", "").replace(":", "").trim();
					model.setPrimaneta(fn.castBigDecimal(fn.preparaPrimas(aux)));
				} else if (arrContenido[i].contains("EXPEDICIÓN")) {
					String aux = arrContenido[i].split("EXPEDICIÓN")[1].replace("###", "").replace(":", "").trim();
					model.setDerecho(fn.castBigDecimal(fn.preparaPrimas(aux)));
				} else if (arrContenido[i].contains("I.V.A") && arrContenido[i].contains("FRACCIONADO")) {
					String aux = arrContenido[i].split("I.V.A")[1].replace("###", "").replace(":", "").trim();
					model.setIva(fn.castBigDecimal(fn.preparaPrimas(aux)));
				} else if (arrContenido[i].contains("FRACCIONADO") && arrContenido[i].contains("PRIMA TOTAL")) {
			

							List<String> valores = fn.obtenerListNumeros( arrContenido[i]);	
							modelo.setRecargo(fn.castBigDecimal(fn.cleanString(valores.get(0))));
							modelo.setPrimaTotal(fn.castBigDecimal(fn.cleanString(valores.get(1))));
				}
			}
		}
	}

	private void obtenerUbicacionVersionMayusculas(String texto, EstructuraJsonModel model) {
		texto = contenido.replace("CARACTERÍSTICAS###DEL###OBJETO###ASEGURADO", "CARACTERÍSTICAS DEL OBJETO ASEGURADO");

		if (texto.contains("CARACTERÍSTICAS DEL OBJETO ASEGURADO") && contenido.contains("Página")) {
			String textoUbicaciones = texto.split("CARACTERÍSTICAS DEL OBJETO ASEGURADO")[1].split("Página")[0]
					.replace("@@@", "");
			List<EstructuraUbicacionesModel> ubicaciones = new ArrayList<>();
			EstructuraUbicacionesModel ubicacion = new EstructuraUbicacionesModel();
			String[] arrContenido = textoUbicaciones.split(",");
			String aux = "";

			// cp
			if (textoUbicaciones.contains(", C.P.")) {
				ubicacion.setCp(textoUbicaciones.split(", C.P.")[1].replace(".", "").split("\r\n")[0].trim());
			}
			// domicilio calle,colonia, numero interno y externo
			for (int i = 0; i < arrContenido.length; i++) {
				if (arrContenido[i].contains("UBICACIÓN:")) {
					ubicacion.setCalle(arrContenido[i].split("UBICACIÓN:")[1].replace("###", "").trim());
				}
				if (ubicacion.getColonia().length() == 0 && arrContenido[i].contains("Col.")) {
					ubicacion.setColonia(arrContenido[i].trim());
				}
				if (ubicacion.getNoExterno().length() == 0 && arrContenido[i].contains("No.")) {
					ubicacion.setNoExterno(arrContenido[i].trim());
				}
				if (ubicacion.getNoInterno().length() == 0 && arrContenido[i].contains("Int.")) {
					ubicacion.setNoInterno(arrContenido[i].trim());
				}
				index++;
			}
			// Muros
			if (textoUbicaciones.contains("MUROS:")) {
				aux = textoUbicaciones.split("MUROS:")[1].split("\n")[0];
				if (aux.contains("###")) {
					aux = aux.split("###")[1].replace("MUROS", "").trim();
					ubicacion.setMuros(fn.material(aux));
				}
			}
			// Techos
			if (textoUbicaciones.contains("TECHOS:")) {
				aux = textoUbicaciones.split("TECHOS:")[1].split("\n")[0];
				if (aux.split("###").length > 1) {
					aux = aux.split("###")[1].replace("TECHO", "").trim();
					ubicacion.setTechos(fn.material(aux));
				}
			}
			// Niveles
			if (textoUbicaciones.contains("PISOS:")) {
				aux = textoUbicaciones.split("PISOS:")[1].split("\n")[0];
				if (aux.split("###").length > 1) {
					aux = aux.split("###")[1].trim();
					ubicacion.setNiveles(fn.castInteger(aux));
				}
			}
			// giro , nombre
			if (textoUbicaciones.contains("GIRO")) {
				aux = textoUbicaciones.split("GIRO")[1].split("\n")[0];
				if (aux.split("###").length > 1) {
					aux = aux.replace("###ASEGURADO", "").split("###")[1].trim();
					ubicacion.setGiro(aux);
					ubicacion.setNombre(ubicacion.getGiro());
				}
			}
			// sótanos
			if (textoUbicaciones.contains("SÓTANOS")) {
				aux = textoUbicaciones.split("SÓTANOS")[1].split("\n")[0];
				if (aux.split("###").length > 1) {
					aux = aux.split("###")[1].trim();
					ubicacion.setSotanos(fn.castInteger(aux));
				}
			}
			if (ubicacion.getCalle().length() > 0) {
				ubicaciones.add(ubicacion);
				ubicacion.setNiveles(ubicacion.getNiveles() == 0 ? 1 : ubicacion.getNiveles());
				model.setUbicaciones(ubicaciones);
			}
		}

	}

}
