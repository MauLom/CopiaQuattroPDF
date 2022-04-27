package com.copsis.models.mapfre;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraAseguradosModel;
import com.copsis.models.EstructuraCoberturasModel;
import com.copsis.models.EstructuraJsonModel;
import com.copsis.models.EstructuraRecibosModel;

public class MapfreSaludBModel {

	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
	private String contenido = "";
	private String[] nombresCobertura = { "AYUDA DE MATERNIDAD", "EMERGENCIA EN EL EXTRANJERO", "MUERTE ACCIDENTAL",
			"GASTOS DE SEPELIO", "ENFER. CATASTRÓFICAS EN EL EXTRANJERO", "INCREMENTO DE HON-QUIRÚRGICOS",
			"PREVISIÓN MAPFRE TEPEYAC", "ELIM.DE DED. POR ACCIDENTE Cob. Nal.", "CENTRAL MEDICA", "ASISTENCIA EN VIAJE",
			"DENTAL", "VISIÓN" };
	private List<String> listNombresCobertura = Arrays.asList(nombresCobertura);
	
	public MapfreSaludBModel(String contenido) {
		this.contenido = contenido;
	}
	public EstructuraJsonModel procesar() {
		contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());
		contenido = contenido.replace("Póliza Número :", "Póliza Número:")
				.replace("las 12:00 hrs. de:", "")
				.replace("SIN LIMITE", "###SIN LIMITE###")
				.replace("AYUDA DE MATERNIDAD", "AYUDA DE MATERNIDAD###")
				.replace("AMPARADA", "###AMPARADA###")
				.replace("BÁSICO", "###BÁSICO###")
				.replace("Poliza Numero :", "Póliza Número:")
				.replace("Endoso Numero", "Endoso Número")
				.replace("Fecha de Emision", "Fecha de Emisión")
				.replace("Expedicion", "Expedición")
				.replace("GASTOS MEDICOS MAYORES", "GASTOS MÉDICOS MAYORES");
		
		String newcontenido = "";
		int inicio = 0;
		int fin = 0;
		try {
			modelo.setTipo(3);
			modelo.setCia(22);

			inicio = contenido.indexOf("GASTOS MÉDICOS MAYORES");
			fin = contenido.indexOf("COBERTURAS SUMA ASEGURADA");
			
			if(inicio ==  -1) {
				inicio = contenido.indexOf("PLAN SERVICIOS");
			}
			if(inicio == -1) {
				inicio = contenido.indexOf("SEGURVIAJE");
			}
			if(inicio == -1) {
				inicio = contenido.indexOf("PROTECCIÓN SEGURA");
			}
			if( fin == -1) {
				fin = contenido.indexOf("Plan de Seguro:");
			}
			
	
			if (inicio > -1 && fin > -1 && inicio < fin) {
				newcontenido = contenido.substring(inicio, fin).replace("@@@", "").replace("\r", "");
				for (int i = 0; i < newcontenido.split("\n").length; i++) {
					if(newcontenido.split("\n")[i].contains("Póliza Número:")) {
						modelo.setPoliza(newcontenido.split("\n")[i].split("Póliza Número:")[1].replace("###", "").trim());
					}
					if(newcontenido.split("\n")[i].contains("Endoso Número")) {
						modelo.setEndoso(newcontenido.split("\n")[i].split("Endoso Número")[1].replace("###", "").replace(":", "").trim());
					}					
					if(newcontenido.split("\n")[i].contains("Contratante:") && newcontenido.split("\n")[i].contains("R.F.C:")) {
						modelo.setCteNombre(newcontenido.split("\n")[i].split("Contratante:")[1].split("R.F.C:")[0].replace("C.U.R.P:", "").replace("###", "").trim());
						modelo.setRfc(newcontenido.split("\n")[i].split("R.F.C:")[1].replace("###", "").trim());
					}
					if(newcontenido.split("\n")[i].contains("Domicilio:") && newcontenido.split("\n")[i].contains("Tel:")) {
						modelo.setCteDireccion(newcontenido.split("\n")[i].split("Domicilio:")[1].split("Tel:")[0].replace("###", "").trim());
					}
					if(newcontenido.split("\n")[i].contains("Clave de Agente:")) {
						if(newcontenido.split("\n")[i].contains("Desde")) {
							modelo.setVigenciaDe(fn.formatDateMonthCadena( newcontenido.split("\n")[i].split("Desde")[1].split("Clave de Agente:")[0].replace("###", "").trim()));
						}else if(newcontenido.split("\n")[i].contains("Vigencia de:")) {
							modelo.setVigenciaDe(fn.formatDateMonthCadena( newcontenido.split("\n")[i].split("Vigencia de:")[1].split("Clave de Agente:")[0].replace("###", "").trim()));
						}
						if(newcontenido.split("\n")[i+1].split("###").length > 2) {
							modelo.setCveAgente(newcontenido.split("\n")[i+1].split("###")[2].replace("###", "").trim());
							modelo.setAgente(newcontenido.split("\n")[i+1].split("###")[3].replace("###", "").trim());
						}
					}
					if(newcontenido.split("\n")[i].contains("Hasta") ) {
						modelo.setVigenciaA(fn.formatDateMonthCadena(newcontenido.split("\n")[i].split("Hasta")[1].split("###")[1].replace("###", "").trim()));
					}
					if(newcontenido.split("\n")[i].contains("Fecha de Emisión") && newcontenido.split("\n")[i].contains("Forma de Pago:") && newcontenido.split("\n")[i].contains("Moneda") ) {
						modelo.setFechaEmision(fn.formatDateMonthCadena(newcontenido.split("\n")[i+1].split("###")[0].replace("###", "").replace(" ", "")).trim() );					
						modelo.setFormaPago(fn.formaPago( newcontenido.split("\n")[i+1].split("###")[2].replace("###", "").trim()));

						if(modelo.getFormaPago() == 0) {
							modelo.setFormaPago(fn.formaPagoSring(newcontenido.split("\n")[i+1].split("###")[2].replace("###", "").trim()));
							if(modelo.getFormaPago() == 0) {
								modelo.setFormaPago(fn.formaPago(newcontenido.split("\n")[i+1].split("###")[1].replace("###", "").trim()));
							}
						}
						modelo.setMoneda(1);
						int moneda = fn.moneda(newcontenido.split("\n")[i+1].split("###")[2].replace("###", "").trim());
						if(moneda > 0 && moneda < 5) {
							modelo.setMoneda(moneda);
						}

						
					}
					if(newcontenido.split("\n")[i].contains("Prima Neta:") && newcontenido.split("\n")[i].contains("Expedición") && newcontenido.split("\n")[i].contains("Prima Total:")  && newcontenido.split("\n")[i+1].split("###").length == 7) {
						modelo.setPrimaneta(fn.castBigDecimal( fn.preparaPrimas(newcontenido.split("\n")[i + 1].split("###")[0])));
						modelo.setRecargo(fn.castBigDecimal(fn.preparaPrimas(newcontenido.split("\n")[i + 1].split("###")[2])));
						modelo.setDerecho(fn.castBigDecimal(fn.preparaPrimas(newcontenido.split("\n")[i + 1].split("###")[3])));
						modelo.setIva(fn.castBigDecimal(fn.preparaPrimas(newcontenido.split("\n")[i + 1].split("###")[5])));
						modelo.setPrimaTotal(fn.castBigDecimal(fn.preparaPrimas(newcontenido.split("\n")[i + 1].split("###")[6])));						
					}
					if(newcontenido.split("\n")[i].contains("C.P:")) {
						modelo.setCp(newcontenido.split("\n")[i].split("C.P:")[1].replace("###", "").trim());
					} 
				}
			}
			
			
			
			inicio = contenido.indexOf("Plan de Seguro:");
			fin = contenido.indexOf("Asegurados que");
			
			if (inicio > -1 && fin > -1 && inicio < fin) {
				newcontenido = contenido.substring(inicio, fin).replace("@@@", "").replace("\r", "");
				for (int i = 0; i < newcontenido.split("\n").length; i++) {                     
                       if(newcontenido.split("\n")[i].contains("Plan de Seguro:")) {
                    	   modelo.setPlan(newcontenido.split("\n")[i].split("Plan de Seguro:")[1].trim());
                       }
				}
			}
			
			if(modelo.getPlan().length() == 0 && contenido.contains("PLAN:###")) {
				modelo.setPlan(contenido.split("PLAN:###")[1].split("###")[0].trim());
			}

			if(modelo.getPlan().length() == 0 && contenido.contains("GASTOS MÉDICOS MAYORES")) {
				String texto = contenido.split("GASTOS MÉDICOS MAYORES")[1].split("\n")[1].split("###")[0].replace("@@@", "").trim();
				if (!texto.startsWith("Endoso") && !texto.startsWith("Av") && !texto.startsWith("Póliza Número")) {
					modelo.setPlan(texto);
				}
				
			}
			
			if(modelo.getCp().length() == 4) {
				modelo.setCp("0"+modelo.getCp());
			}
			
			inicio = contenido.indexOf("COBERTURAS SUMA ASEGURADA");
			fin = contenido.indexOf("LAS ANTERIORES COBERTURAS");	

			if (inicio > -1 && fin > -1 && inicio < fin) {
				 List<EstructuraCoberturasModel> coberturas = new ArrayList<>();
				newcontenido = contenido.substring(inicio, fin).replace("@@@", "").replace("\r", "")
						.replace("10%", "###10%###")
						.replace("Usd", "###Usd###")
						.replace("30%", "###30%###")
						.replace("VISIÓN", "VISIÓN###")
						.replace("COBERTURA BÁSICA ", "COBERTURA BÁSICA###")
						.replace("PREVISIÓN### MAPFRE TEPEYAC ###","PREVISIÓN MAPFRE TEPEYAC###");

				String coberturaTexto = ""; 
				boolean tieneTituloCoaseguro = newcontenido.contains(" COAS ");
				
				for (int i = 0; i < newcontenido.split("\n").length; i++) {
					EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();
					if(!newcontenido.split("\n")[i].contains("DEDUCIBLE") && !newcontenido.split("\n")[i].contains("COBERTURAS OPCIONALES")) {						
						int sp = newcontenido.split("\n")[i].split("###").length;
						coberturaTexto = newcontenido.split("\n")[i];
						String nombre = coberturaTexto.split("###")[0].trim();
						coberturaTexto = agregaSeparadorSumaAsegurada(nombre, coberturaTexto, true);
						if (sp > 1) {
							coberturaTexto = agregaSeparadorSumaAsegurada(coberturaTexto.split("###")[1].trim(),
									coberturaTexto, false);
						}
						coberturaTexto = coberturaTexto.replace("### ###", "###");
						sp = coberturaTexto.split("###").length;

						switch (sp) {
						case 2: 
							if(esValidoNombreCobertura(coberturaTexto.split("###")[0].trim())) {
								cobertura.setNombre(coberturaTexto.split("###")[0].trim());
								cobertura.setSa(coberturaTexto.split("###")[1].trim());
								coberturas.add(cobertura);
							}
							break;
						case  3:
							   cobertura.setNombre(coberturaTexto.split("###")[0].trim());
	                              cobertura.setSa(coberturaTexto.split("###")[1].trim());	                           
	                              coberturas.add(cobertura);
							break;
						case  4: 	case  5:
							  cobertura.setNombre(coberturaTexto.split("###")[0].trim());
                              cobertura.setSa(coberturaTexto.split("###")[1].trim());
                              cobertura.setDeducible(coberturaTexto.split("###")[2].trim());
                              if(tieneTituloCoaseguro) {
                            	  cobertura.setCoaseguro(coberturaTexto.split("###")[3].trim());
                              }
                              coberturas.add(cobertura);
							break;
						default:
							break;
							
						}
						
					}
				}
				modelo.setCoberturas(coberturas);
			}
		
			if(modelo.getCoberturas().isEmpty() && contenido.contains("COBERTURAS SUMA ASEGURADA") && contenido.contains("cobertura Segurviaje Suma Asegurada")) {
				StringBuilder contenidoCoberturas = new StringBuilder();
				contenidoCoberturas.append(contenido.split("COBERTURAS SUMA ASEGURADA")[1].split("PLAN")[0].replace("@@@", ""));
			    boolean existenValoresDeducibleCoasegurso = contenidoCoberturas.toString().contains("DEDUCIBLE COASEGURO");
								
				if(!contenidoCoberturas.toString().contains("cobertura Segurviaje Suma Asegurada")) {
					contenidoCoberturas.append(contenido.split("cobertura Segurviaje Suma Asegurada")[1].split("En testimonio")[0].replace("@@@", ""));
				}
				if(contenidoCoberturas.length() > 0) {
					List<EstructuraCoberturasModel> coberturas = new ArrayList<>();
					String[] arrContenido = contenidoCoberturas.toString().split("\n");
					for(int i=0;i<arrContenido.length;i++) {
						String texto = arrContenido[i];
						EstructuraCoberturasModel cobertura =  new EstructuraCoberturasModel();
						if(!texto.contains("###") && !existenValoresDeducibleCoasegurso) {
							String[] detalle = texto.replace(" USD", "").trim().split(" ");
							String valorSumaAsegurada = detalle[detalle.length-1];
							
							if (fn.numTx(valorSumaAsegurada).length() > 0 || valorSumaAsegurada.contains("Incluido")){
								texto =  texto.replace(valorSumaAsegurada, "###"+valorSumaAsegurada);
							}else if(valorSumaAsegurada.contains("días") && fn.numTx(valorSumaAsegurada).length() == 0 && (i-1)> detalle.length) {
								valorSumaAsegurada = detalle[detalle.length-2];
							    texto =  texto.replace(valorSumaAsegurada, "###"+valorSumaAsegurada);
							}else if(texto.contains("Hasta")) {
								texto =  texto.replace("Hasta", "###Hasta");
							}
						}
						if(texto.split("###").length > 1) {
							cobertura.setNombre(texto.split("###")[0].trim());
							cobertura.setSa(texto.split("###")[1].trim());
							coberturas.add(cobertura);
						}
					}
					modelo.setCoberturas(coberturas);
				}
				
			}
				
				inicio = contenido.indexOf("LISTA DE ASEGURADOS:");
				fin = contenido.indexOf("FECHAS DE ANTIGÜEDAD: ");	

				
			
		
				
				if (inicio > -1 && fin > -1 && inicio < fin) {
				       List<EstructuraAseguradosModel> asegurados = new ArrayList<>();
					newcontenido = contenido.substring(inicio, fin).replace("@@@", "").replace("\r", "")
							.replace(" F ", "###F###")
							.replace("TITULAR", "###TITULAR###")
							.replace("HIJO-A", "###HIJO-A###")						
							.replace(" M ", "###M###")
							.replace("CONYUGE", "###CONYUGE###");
	
					for (int i = 0; i < newcontenido.split("\n").length; i++) {
						EstructuraAseguradosModel asegurado = new EstructuraAseguradosModel();
						if(!newcontenido.split("\n")[i].contains("PARENTESCO")  && !newcontenido.split("\n")[i].contains("LISTA DE ASEGURADOS")) {						
						
							switch (newcontenido.split("\n")[i].split("###").length) {
							case  5:
								asegurado.setNombre(newcontenido.split("\n")[i].split("###")[0].split(newcontenido.split("\n")[i].split("###")[0].split(" ")[1])[1].trim());
								asegurado.setSexo(fn.sexo(newcontenido.split("\n")[i].split("###")[1]) ? 1 : 0);
								asegurado.setEdad(Integer.parseInt(newcontenido.split("\n")[i].split("###")[2].trim()));
								asegurado.setParentesco(fn.parentesco(newcontenido.split("\n")[i].split("###")[3].trim()));
								if(newcontenido.split("\n")[i].split("###")[4].trim().split(" ")[0].split("-").length == 3) {
									asegurado.setNacimiento(fn.formatDateMonthCadena(newcontenido.split("\n")[i].split("###")[4].trim().split(" ")[0]));
								}
								asegurado.setAntiguedad(fn.formatDateMonthCadena(newcontenido.split("\n")[i].split("###")[4].trim().split(" ")[1]));
								asegurados.add(asegurado);
								break;
					
							}
						}
					}
					modelo.setAsegurados(asegurados);
				}				
				   inicio = contenido.indexOf("Asegurados que ampara");				
				   fin = contenido.indexOf("EL CONTRATANTE GOZARÁ");
				   
					if(modelo.getAsegurados().isEmpty() && inicio >-1 && inicio < fin) {

						   List<EstructuraAseguradosModel> asegurados = new ArrayList<>();						  				
										newcontenido = contenido.substring(inicio, fin).replace("@@@", "").replace("\r", "")
						.replace(" F ", "###F###")
						.replace("TITULAR", "###TITULAR###")
						.replace("HIJO-A", "###HIJO-A###")
						.replace("HIJOA", "###HIJOA###")
						.replace(" M ", "###M###")
						.replace("CONYUGE", "###CONYUGE###");
						
						for (int i = 0; i < newcontenido.split("\n").length; i++) {
						EstructuraAseguradosModel asegurado = new EstructuraAseguradosModel();
						if(!newcontenido.split("\n")[i].contains("Asegurados") ||  newcontenido.split("\n")[i].split("###").length == 2) {	
								asegurado.setParentesco(fn.parentesco(newcontenido.split("\n")[i].split("###")[1].trim()));
								asegurado.setEdad(Integer.parseInt(newcontenido.split("\n")[i].split("###")[0].split(" ")[newcontenido.split("\n")[i].split("###")[0].split(" ").length-1]));
								asegurado.setNacimiento(fn.formatDateMonthCadena(newcontenido.split("\n")[i].split("###")[0].split(" ")[newcontenido.split("\n")[i].split("###")[0].split(" ").length-2]));
								asegurado.setNombre(newcontenido.split("\n")[i].split(newcontenido.split("\n")[i].split("###")[0].split(" ")[newcontenido.split("\n")[i].split("###")[0].split(" ").length-2])[0].trim());
								asegurados.add(asegurado);											
					        }
							
						 }
						modelo.setAsegurados(asegurados);
					}
			
					if(modelo.getAsegurados().isEmpty() && contenido.contains("Asegurado:")) {
						String texto = fn.gatos(contenido.split("Asegurado:")[1].split("\n")[0]);
						texto = texto.split("###")[0];
						List<EstructuraAseguradosModel> asegurados = new ArrayList<>();
						if(texto.length() > 0) {
							EstructuraAseguradosModel asegurado = new EstructuraAseguradosModel();
							asegurado.setNombre(texto);
							asegurados.add(asegurado);
							modelo.setAsegurados(asegurados);
						}
						
					}
					buildRecibos(modelo);
			
			return modelo;
		} catch (Exception ex) {
			modelo.setError(MapfreSaludBModel.this.getClass().getTypeName() + " - catch:" + ex.getMessage() + " | "
					+ ex.getCause());
			return modelo;
		}
		
	}
	
	private void buildRecibos(EstructuraJsonModel model) {
		if(model.getFormaPago() == 1 ) {
            List<EstructuraRecibosModel> listRecibos = new ArrayList<>();
			EstructuraRecibosModel recibo = new EstructuraRecibosModel();
			recibo.setReciboId("");
			recibo.setSerie("1/1");
			recibo.setVigenciaDe(model.getVigenciaDe());
			recibo.setVigenciaA(model.getVigenciaA());
			if (recibo.getVigenciaDe().length() > 0) {
				recibo.setVencimiento(fn.dateAdd(recibo.getVigenciaDe(), 30, 1));
			}			
			recibo.setPrimaneta(model.getPrimaneta());
			recibo.setDerecho(model.getDerecho());
			recibo.setRecargo(model.getRecargo());
			recibo.setIva(model.getIva());
			recibo.setPrimaTotal(model.getPrimaTotal());
			recibo.setAjusteUno(model.getAjusteUno());
			recibo.setAjusteDos(model.getAjusteDos());
			recibo.setCargoExtra(model.getCargoExtra());
			listRecibos.add(recibo);
			modelo.setRecibos(listRecibos);
		}
	}
		
	private String agregaSeparadorSumaAsegurada(String valoresPorSeparar,String renglonCobertura, boolean esUbicacionNombreCob) {
		String resultado = renglonCobertura;
		String sumaAseguradaAux =  valoresPorSeparar.trim().replace(".00", "@00");
		
		if((sumaAseguradaAux.split("@00").length == 2 || sumaAseguradaAux.contains("@00"))  && sumaAseguradaAux.split(" ").length == 2)  {
			sumaAseguradaAux = sumaAseguradaAux.replace("@00 ", ".00###").replace("@00", ".00");
			resultado = resultado.replace( valoresPorSeparar, sumaAseguradaAux);
		}else if(fn.numTx(valoresPorSeparar).length() > 0) {
			List<String> listNumTx = fn.obtenerListNumeros(valoresPorSeparar);
			sumaAseguradaAux = valoresPorSeparar.trim();
			if(listNumTx.size() < 2 && esUbicacionNombreCob) {
				sumaAseguradaAux = valoresPorSeparar.replace(fn.numTx(valoresPorSeparar), "###"+fn.numTx(valoresPorSeparar));
				resultado = resultado.replace(valoresPorSeparar, sumaAseguradaAux);
			}else if(listNumTx.size() > 1){
				for(String numTx: listNumTx) {
					if(esUbicacionNombreCob) {
						sumaAseguradaAux = sumaAseguradaAux.replace(numTx, "###"+numTx);
					}
				}
			}
			resultado = resultado.replace(valoresPorSeparar, sumaAseguradaAux);
		}
		
		resultado = agregarSeparadorSiEsCadena(sumaAseguradaAux, resultado);
		
		return resultado;
	}

	private String agregarSeparadorSiEsCadena(String valoresPorSeparar, String renglonCobertura ) {
		String resultado = renglonCobertura;
		valoresPorSeparar = valoresPorSeparar.trim();
		
		if(valoresPorSeparar.contains("AMPARADA") && valoresPorSeparar.split(" ").length > 1 ) {
			resultado = resultado.replace("AMPARADA", "###AMPARADA");
		}else if(valoresPorSeparar.contains("Amparada")&& valoresPorSeparar.split(" ").length > 1) {
			resultado = resultado.replace("Amparada", "###Amparada");
		}
		return resultado;
	}
	private boolean esValidoNombreCobertura(String nombre) {
		return listNombresCobertura.contains(nombre.toUpperCase().trim());
	}
}