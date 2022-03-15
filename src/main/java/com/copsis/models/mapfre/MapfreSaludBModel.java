package com.copsis.models.mapfre;

import java.util.ArrayList;
import java.util.List;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraAseguradosModel;
import com.copsis.models.EstructuraCoberturasModel;
import com.copsis.models.EstructuraJsonModel;
import com.copsis.models.EstructuraRecibosModel;
import com.copsis.models.inbursa.inbursaDiversosModel;

public class MapfreSaludBModel {

	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
	private String contenido = "";

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
			if( fin == -1) {
				fin = contenido.indexOf("Plan de Seguro:");
			}
			
	
			if (inicio > -1 && fin > -1 && inicio < fin) {
				newcontenido = contenido.substring(inicio, fin).replace("@@@", "").replace("\r", "");
				for (int i = 0; i < newcontenido.split("\n").length; i++) {
					if(newcontenido.split("\n")[i].contains("Póliza Número:")) {
						modelo.setPoliza(newcontenido.split("\n")[i].split("Póliza Número:")[1].replace("###", "").strip());
					}
					if(newcontenido.split("\n")[i].contains("Endoso Número")) {
						modelo.setEndoso(newcontenido.split("\n")[i].split("Endoso Número")[1].replace("###", "").replace(":", "").strip());
					}					
					if(newcontenido.split("\n")[i].contains("Contratante:") && newcontenido.split("\n")[i].contains("R.F.C:")) {
						modelo.setCteNombre(newcontenido.split("\n")[i].split("Contratante:")[1].split("R.F.C:")[0].replace("C.U.R.P:", "").replace("###", "").strip());
						modelo.setRfc(newcontenido.split("\n")[i].split("R.F.C:")[1].replace("###", "").strip());
					}
					if(newcontenido.split("\n")[i].contains("Domicilio:") && newcontenido.split("\n")[i].contains("Tel:")) {
						modelo.setCteDireccion(newcontenido.split("\n")[i].split("Domicilio:")[1].split("Tel:")[0].replace("###", "").strip());
					}
					if(newcontenido.split("\n")[i].contains("Clave de Agente:")) {
						if(newcontenido.split("\n")[i].contains("Desde")) {
							modelo.setVigenciaDe(fn.formatDateMonthCadena( newcontenido.split("\n")[i].split("Desde")[1].split("Clave de Agente:")[0].replace("###", "").strip()));
						}else if(newcontenido.split("\n")[i].contains("Vigencia de:")) {
							modelo.setVigenciaDe(fn.formatDateMonthCadena( newcontenido.split("\n")[i].split("Vigencia de:")[1].split("Clave de Agente:")[0].replace("###", "").strip()));
						}
						if(newcontenido.split("\n")[i+1].split("###").length > 2) {
							modelo.setCveAgente(newcontenido.split("\n")[i+1].split("###")[2].replace("###", "").strip());
							modelo.setAgente(newcontenido.split("\n")[i+1].split("###")[3].replace("###", "").strip());
						}
					}
					if(newcontenido.split("\n")[i].contains("Hasta") ) {
						modelo.setVigenciaA(fn.formatDateMonthCadena(newcontenido.split("\n")[i].split("Hasta")[1].split("###")[1].replace("###", "").strip()));
					}
					if(newcontenido.split("\n")[i].contains("Fecha de Emisión") && newcontenido.split("\n")[i].contains("Forma de Pago:") && newcontenido.split("\n")[i].contains("Moneda") ) {
						modelo.setFechaEmision(fn.formatDateMonthCadena(newcontenido.split("\n")[i+1].split("###")[0].replace("###", "").replace(" ", "")).strip() );					
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

			if(modelo.getPlan().length() == 0 && contenido.contains("Tipo de Documento")) {
				String texto = contenido.split("Tipo de Documento")[1];
				if(texto.contains("GASTOS MÉDICOS MAYORES")) {
					texto = texto.split("GASTOS MÉDICOS MAYORES")[1].split("\n")[1].split("###")[0].replace("@@@", "").trim();
					if(!texto.startsWith("Endoso") && !texto.startsWith("Av")) {
						modelo.setPlan(texto);
					}
				}else {
					texto = texto.split("\n")[1].split("###")[0].replace("@@@", "").trim();
					System.out.println("="+texto+"=");
					if(!texto.startsWith("Endoso") && !texto.startsWith("Av")) {
						modelo.setPlan(texto);
					}
				}
				
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
						.replace("COBERTURA BÁSICA ", "COBERTURA BÁSICA###");
				//System.out.println(newcontenido);
				for (int i = 0; i < newcontenido.split("\n").length; i++) {
					EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();

					if(newcontenido.split("\n")[i].contains("DEDUCIBLE")) {						
					}else {
						int sp =newcontenido.split("\n")[i].split("###").length;
						switch (sp) {
						case  3:
							   cobertura.setNombre(newcontenido.split("\n")[i].split("###")[0].strip());
	                              cobertura.setSa(newcontenido.split("\n")[i].split("###")[1].strip());	                           
	                              coberturas.add(cobertura);
							break;
						case  4: 	case  5:
							  cobertura.setNombre(newcontenido.split("\n")[i].split("###")[0].strip());
                              cobertura.setSa(newcontenido.split("\n")[i].split("###")[1].strip());
                              cobertura.setDeducible(newcontenido.split("\n")[i].split("###")[2].strip());
                              coberturas.add(cobertura);
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
								asegurado.setNombre(newcontenido.split("\n")[i].split("###")[0].split(newcontenido.split("\n")[i].split("###")[0].split(" ")[1])[1].strip());
								asegurado.setSexo(fn.sexo(newcontenido.split("\n")[i].split("###")[1]) ? 1 : 0);
								asegurado.setEdad(Integer.parseInt(newcontenido.split("\n")[i].split("###")[2].strip()));
								asegurado.setParentesco(fn.parentesco(newcontenido.split("\n")[i].split("###")[3].strip()));
								asegurado.setAntiguedad(fn.formatDateMonthCadena(newcontenido.split("\n")[i].split("###")[4].strip().split(" ")[1]));
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
								asegurado.setParentesco(fn.parentesco(newcontenido.split("\n")[i].split("###")[1].strip()));
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
}