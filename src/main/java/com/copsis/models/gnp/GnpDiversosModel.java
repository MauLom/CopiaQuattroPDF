package com.copsis.models.gnp;

import java.util.ArrayList;
import java.util.List;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraCoberturasModel;
import com.copsis.models.EstructuraJsonModel;
import com.copsis.models.EstructuraRecibosModel;
import com.copsis.models.EstructuraUbicacionesModel;

public class GnpDiversosModel {
	// Clases
	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
	// Variables
	private String  contenido = "", newcontenido = "",resultado = "", contenidolower ="",nombre="",seccion="", ubicacionesT="";
	private int inicio = 0, fin = 0,index =0,tipo;
	private Boolean esverdad;	
    //constructor
	public GnpDiversosModel(String contenido,String ubicaciones, int tipo) {
		this.contenido = contenido;
		this.ubicacionesT = ubicaciones;
		this.tipo = tipo;
	}
	
	public EstructuraJsonModel procesar() {
		contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());
		contenidolower= contenido.toLowerCase();
		try {
			modelo.setTipo(7);
			// cia
			modelo.setCia(18);
			
			// poliza
			inicio = contenido.indexOf("liza No.");
			if(inicio > -1) {
				resultado = fn.gatos(contenido.substring(inicio + 8, inicio + 150).split("\r\n")[0].trim()).trim();
				modelo.setPoliza(resultado);
			}
		
			// renovacion
			inicio = contenido.indexOf("Renovación");
			if(inicio > -1) {
				resultado = fn.gatos(contenido.substring(inicio + 10, inicio + 150).split("\r\n")[0].trim()).trim();
				modelo.setRenovacion(resultado);				
			}
			
			//rfc
			inicio = contenido.indexOf("R.F.C:");
			if(inicio > -1) {
				resultado = contenido.substring(inicio + 8, inicio + 150).trim().split("\r\n")[0].trim();
				if(resultado.contains("Desde las")) {
					resultado = fn.gatos(resultado.split("Desde las")[0].trim());
					modelo.setRfc(resultado);
				}else if(resultado.contains("Hasta las")) {
					resultado = fn.gatos(resultado.split("Hasta las")[0].trim());
					modelo.setRfc(resultado);
				}else if(resultado.contains("Vigencia")) {
					resultado = fn.gatos(resultado.split("Vigencia")[0].trim());
					modelo.setRfc(resultado);
				}
			}
			
			
			List<String> search = new ArrayList<String>();
			search.add("Clave");
			search.add("En caso");
			search.add("Para mayor");
			search.add("Grupo Nacional");
			
			// cve_agente
			inicio = contenido.indexOf("Clave");
			if(inicio > -1) {
				newcontenido = "";
				newcontenido = fn.gatos(contenido.substring(inicio + 5, inicio + 150).split("\r\n")[0].trim());
				resultado = "";
				for(String x: newcontenido.split("\r\n")) {
					esverdad = false;
					for(String a: search) {
						fin = x.indexOf(a);
						if(fin > -1) {
							resultado += x.substring(0, fin).trim();
							esverdad = true;
							break;
						}
					}
					if(esverdad == false) resultado += x.trim() + " ";
				}
				modelo.setCveAgente(resultado.replace("###", " ").trim());
			}
			
			
			// agente
			inicio = contenido.indexOf("Agente");
			if(inicio > -1) {
				newcontenido = "";
				newcontenido = fn.gatos(contenido.substring(inicio + 6, inicio + 180)).replace("@@@", "").trim();
				resultado = "";
				for(String x: newcontenido.split("\r\n")) {
					if(x.trim().length() > 0) {
						esverdad = false;
						for(String a: search) {
							fin = x.indexOf(a);
							if(fin > -1) {
								resultado += x.substring(0, fin).trim();
								esverdad = true;
								break;
							}
						}
						if(esverdad == false) resultado += x.trim() + " ";
					}
				}
				modelo.setAgente(resultado.replace("###", " ").trim());
			}
			
			//id_cliente
			inicio = contenido.indexOf("Código Cliente");
			if(inicio > -1) {
				resultado = contenido.substring(inicio + 14, inicio + 150).trim().split("\r\n")[0];
				if(resultado.contains("Duraci")) {
					resultado = fn.gatos(resultado.split("Duraci")[0]).trim();
					modelo.setIdCliente(resultado);	
				}
			}
			
			//plan
			fin = contenido.indexOf("Póliza No.");
			if(fin > -1) {
				resultado = fn.gatos(contenido.substring(0, fin).trim());
				inicio = resultado.indexOf("Daños");
				if(inicio > -1) {
					resultado = fn.gatos(resultado.substring(inicio + 5, resultado.length()));
					modelo.setPlan(resultado);
				}
			}
			
			
			// cte_nombre
			inicio = contenido.indexOf("Contratante");
			if(inicio > -1) {
				newcontenido = "";
				resultado = contenido.substring(inicio + 11, inicio + 200).trim();
				int index = 0;
				for(String x: resultado.split("\n")) {
					if(index == 1) {
						if(x.contains("Número")) {
							x = fn.gatos(x.split("Número")[0].trim());
						}
						newcontenido += fn.gatos(x.trim());
					}else if(index == 2 ){
						if((x.contains("CALLE") || x.contains("Vigencia") || x.contains("AVENIDA") || x.contains("BOULEVARD")|| x.contains("CARRETERA") || x.contains("Número")) == false) {
							newcontenido += " " + fn.gatos(x.trim());
						}
					}
					index++;
				}
				modelo.setCteNombre(newcontenido);
			}
			
			// cte_direccion
			newcontenido = "";
			inicio = contenido.indexOf(modelo.getCteNombre());
			if(inicio > -1) {
				int longtxt = modelo.getCteNombre().length();
				if(longtxt > 0) {
					resultado = fn.gatos(contenido.substring(inicio + longtxt, inicio + 350).trim()).trim();
					for(String x: resultado.split("\r\n")) {
						if(x.contains("Número") == false) {
							fin = x.indexOf("Vigencia");
							if(fin > -1) {
								newcontenido += fn.gatos(x.substring(0, fin).trim()) + " ";
							}else {
								newcontenido += x.trim() + "\r\n";
							}	
						}
					}
					if(newcontenido.contains("C.P")) {
						newcontenido = newcontenido.split("C.P")[0].trim();
						modelo.setCteDireccion(newcontenido.replace("###", " ").replace("\r\n", ""));
					}
				}
			}
			
			// cp
			inicio = contenido.indexOf(", C.P");
			if (inicio > -1) {
				resultado = fn.gatos(contenido.substring(inicio + 5, inicio + 250).replace(".", "").split("\r\n")[0].trim());
				if(resultado.contains("Vigencia")) {
					modelo.setCp(fn.gatos(resultado.split("Vigencia")[0].trim()));
				}else if(resultado.contains("Desde las")) {
					modelo.setCp(fn.gatos(resultado.split("Desde las")[0].trim()));
				}else if(fn.isNumeric(resultado)) {
					modelo.setCp(resultado);					
				}
			}

			// moneda
			inicio = contenido.indexOf("Moneda");
			if(inicio > -1) {
				resultado = (contenido.substring(inicio + 6, inicio + 150).trim().split("\r\n")[0].trim());
				if(resultado.contains("Derecho de P")) {
					resultado = fn.gatos(resultado.split("Derecho de P")[0].trim());
					modelo.setMoneda(fn.moneda(resultado));			
				}
			}
			
			// forma_pago
			inicio = contenido.indexOf("Forma de pago");
			if(inicio > -1) {
				resultado = fn.gatos(contenido.substring(inicio + 13, inicio + 150).trim().split("\r\n")[0].trim());
				if(resultado.contains("Recargo Pago")) {
					resultado = fn.gatos(resultado.split("Recargo Pago")[0].trim());
					modelo.setFormaPago(fn.formaPago(resultado));
				}
			}

			// vigencia_de
			inicio = contenido.indexOf("Desde las 12 hrs");
			if(inicio > -1) {
				resultado = fn.gatos(contenido.substring(inicio + 16, inicio + 150).trim().split("\r\n")[0].trim());
				resultado = resultado.replace("###", "-");
				if(resultado.split("-").length == 3) {
					modelo.setVigenciaDe(fn.formatDate(resultado,"dd-MM-yy"));
				}
			}
			
			// vigencia_a
			inicio = contenido.indexOf("Hasta las 12 hrs");
			if(inicio > -1) {
				resultado = fn.gatos(contenido.substring(inicio + 16, inicio + 150).trim().split("\r\n")[0].trim());
				resultado = resultado.replace("###", "-");
				if(resultado.split("-").length == 3) {
					modelo.setVigenciaA(fn.formatDate(resultado,"dd-MM-yy"));
				}
			}
			
			//fecha_emision
			inicio = contenido.indexOf("Fecha de expedición");
			if(inicio > -1) {
				resultado = contenido.substring(inicio + 19, inicio + 150).split("\r\n")[0];
				if(resultado.contains("Tipo de")) {
					resultado = fn.gatos(resultado.split("Tipo de")[0].trim()).replace("###", "-");
				}else if(resultado.contains("I.V.A.")) {
					resultado = fn.gatos(resultado.split("I.V.A.")[0].trim()).replace("###", "-");
				}
				if(resultado.split("-").length == 3 && resultado.length() == 10) {
					modelo.setFechaEmision(fn.formatDate(resultado,"dd-MM-yy"));
				}
			}

			if(modelo.getFechaEmision().length() == 0) {
				inicio = contenido.lastIndexOf("Derecho de Póliza");
				fin = contenido.indexOf("I.V.A.");
				if(inicio > -1 && fin > inicio) {
					newcontenido = contenido.substring(inicio + 17, fin).trim();
					if(newcontenido.split("\r\n").length == 2) {
						newcontenido = fn.gatos(newcontenido.split("\r\n")[1].trim()).replace("###", "-");
						if(newcontenido.split("-").length == 3 && newcontenido.length() == 10) {
							modelo.setFechaEmision(fn.formatDate(newcontenido,"dd-MM-yy"));
						}
					}
				}
			}
			
			// prima_neta
			inicio = contenido.indexOf("Prima Neta");
			if(inicio > -1) {
				resultado = fn.gatos(contenido.substring(inicio + 10, inicio + 150).trim().split("\r\n")[0].trim());
				modelo.setPrimaneta(fn.castFloat(resultado));
			}
			
			// recargo
			inicio = contenido.indexOf("Pago Fraccionado");
			if(inicio > -1) {
				resultado = fn.gatos(contenido.substring(inicio + 16, inicio + 150).trim().split("\r\n")[0].trim());
				modelo.setRecargo(fn.castFloat(resultado));
			}
			
			// derecho
			inicio = contenido.indexOf("Derecho de Póliza");
			if(inicio > -1) {
				resultado = fn.gatos(contenido.substring(inicio + 17, inicio + 150).trim().split("\r\n")[0].trim());
				modelo.setDerecho(fn.castFloat(resultado));
			}
			
			// iva
			inicio = contenido.indexOf("I.V.A.");
			if(inicio > -1) {
				resultado = fn.gatos(contenido.substring(inicio + 6, inicio + 150).trim().split("\r\n")[0].trim());
				inicio = resultado.indexOf("%");
				if(inicio > -1) {
					resultado = fn.gatos(resultado.substring(inicio + 1, resultado.length()));
				}
				modelo.setIva(fn.castFloat(resultado));
			}
			
			// prima_total
			inicio = contenidolower.indexOf("importe a pagar");
			if(inicio > -1) {
				resultado = fn.gatos(contenido.substring(inicio + 15, inicio + 150).trim().split("\r\n")[0].trim());
				modelo.setPrimaTotal(fn.castFloat(resultado));
			}
			
			//LAS COBERTURAS(TITULOS O TUTULOS Y DESGLOCE)
			//TUTULOS 1
			newcontenido = "";
			inicio = contenido.indexOf("Riesgos###Prima Neta###");
			fin = contenido.indexOf("Agente");
			if(inicio > -1 && fin > inicio) {
				newcontenido = contenido.substring(inicio + 23, fin).trim();
				List<String> buscar = new ArrayList<String>();
				buscar.add("Agente");
				buscar.add("Grupo Nacional");
				for(String x: buscar) {
					fin = newcontenido.indexOf(x);
					if(fin > -1) {
						resultado = newcontenido.substring(0, fin).replace("@@@", "").trim();
					}
				}
				newcontenido = clearCoberturas(resultado);
			}
			
			//***************************************************************************
			//TITULO 2			
			inicio = contenido.indexOf("Renovación póliza");
			if(inicio > -1) {
				newcontenido = contenido.substring(inicio + 17, contenido.length()).trim();
				fin = newcontenido.indexOf("Grupo Nacional");
				if(fin > -1) {
					resultado = newcontenido.substring(0, fin).replace("@@@", "").trim();
					newcontenido = clearCoberturas(resultado);
				}
			}
			
			//***************************************************************************
			//TITULO 3
			inicio = contenido.indexOf("secciones contratadas");
			fin = contenido.indexOf("Grupo###Nacional###Provincial");
			if(inicio > -1 && fin > inicio) {
				newcontenido = "";
				newcontenido = fn.gatos(contenido.substring(inicio + 21, fin).replace("@@@", "")).trim().replace(" Amparado", "###Amparado");
				resultado = "";
				for(String x: newcontenido.split("\r\n")) {
					if(x.trim().length() > 0) {
						resultado += fn.gatos(x.trim()) + "\r\n";
					}
				}
				newcontenido = resultado;
			}
			
			List<EstructuraCoberturasModel> coberturas = new ArrayList<>();
			//SE RECORRE LOS DOS CASOS ANTERIORES CON ESTE FOR (TITULO 1 O TUTULO 2)
			newcontenido = newcontenido.replace(" Amparado", "###Amparado").replace("######", "###");
			if(newcontenido.contains("Grupo###Nacional")) {
				newcontenido = newcontenido.split("Grupo###Nacional")[0].toString();
			}
			if(newcontenido.length() > 0) {
				for(int i = 0; i < newcontenido.split("\r\n").length; i++) {
					EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();
					
					String x = "", y = "";
					x = newcontenido.split("\r\n")[i];
					if(x.contains("Especificación de Bienes") == false) {
						if(i + 1 < newcontenido.split("\r\n").length) {
							if(newcontenido.split("\r\n")[i+1].split("###").length == 1) {
								y = newcontenido.split("\r\n")[i + 1];	
							}
						}
						switch (x.split("###").length) {
						case 3:
							nombre = x.split("###")[1].trim();
							if(y.length() > 0) {
								nombre += " " + y.trim();
							}
							
							cobertura.setSeccion(x.split("###")[0].trim());
							cobertura.setNombre(nombre);
							if(x.split("###")[2].trim().equals("Amparado")) {
								cobertura.setSa(x.split("###")[2].trim());
							}
							   coberturas.add(cobertura);
							break;
						}	
					}
				}
			}
			
			//***************************************************************************
			//COBERTURAS TUTULOS Y DESGLOCE
			extctCoberturas(coberturas, tipo);
			modelo.setCoberturas(coberturas);
			
			//UBICACIONES
			List<EstructuraUbicacionesModel> ubicaciones = new ArrayList<>();
			inicio = contenido.indexOf("Descripción del Movimiento");
			fin = contenido.indexOf("Ubicado a Más");
			if(inicio > -1 && fin > inicio) {
				
				resultado = "";
				newcontenido = contenido.substring(inicio + 26,  fin).trim();
				
				index = 0;
				EstructuraUbicacionesModel ubicacion = new EstructuraUbicacionesModel();
				for(String x : newcontenido.split("\r\n")) {
				
					if(x.split("###").length == 2) resultado += x.split("###")[0].trim() + "\r\n";				
					else resultado += x.trim() + "\r\n";
					if(index == 0) {
						int len = x.split(",").length;
						if(len > 1) {
							ubicacion.setCalle(x.split(",")[0].trim());
							boolean encontro = false;
							for(int i = 0; i < x.split(",")[0].trim().split(" ").length; i++) {
								String a = x.split(",")[0].trim().split(" ")[i];
								if(i == x.split(",")[0].trim().split(" ").length-1) {
									if(fn.isNumeric(a) && encontro == false) {
										ubicacion.setNoExterno(a);
										ubicacion.setCalle(x.split(",")[0].trim().split(ubicacion.getNoExterno())[0].trim());
									}
								}else {
									if(fn.isNumeric(a)) {
										encontro = true;	
									}
								}
							}
							ubicacion.setColonia(x.split(",")[1].trim());	
						}
					}
					if(x.contains("C.P")) {
						if(x.split("###").length == 2) ubicacion.setCp(x.split("###")[0].split("C.P.")[1].trim());
					}
					index++;
				}
				ubicaciones.add(ubicacion);
				modelo.setUbicaciones(ubicaciones);
			}else {
			
				inicio = contenido.indexOf("Descripción del Movimiento");
				fin = contenido.lastIndexOf("Asegurado###");
				
				resultado = "";
				if(inicio > -1 && fin > inicio) {
			
					EstructuraUbicacionesModel ubicacion = new EstructuraUbicacionesModel();
					newcontenido = contenido.substring(inicio + 26, fin).replace("@@@", "").trim();
					for(String x : newcontenido.split("\r\n")) {
						if(x.contains("###CARTERA")) resultado += x.split("###CARTERA")[0].trim() + "\r\n";
						else resultado += x + "\r\n";
					}

					//giro
					//nombre
					inicio = resultado.indexOf("Giro");
					fin = resultado.indexOf("Techos###");
					if(inicio > -1 && fin > inicio) {
						ubicacion.setGiro(fn.gatos(resultado.substring(inicio + 4, fin)).replace("###", "").replace("\r\n", "").trim());
						ubicacion.setNombre(ubicacion.getGiro());
					}
					
					//techos
					inicio = resultado.indexOf("Techos");
					fin = resultado.indexOf("Muros");
		
					if(inicio > -1 && fin > inicio) {
						newcontenido = resultado.substring(inicio + 6, fin).replace("###", "").replace("\r\n", " ").trim();
					
						if(newcontenido.contains("MATERIALES INCOMBUSTIBLES")) {
							newcontenido = newcontenido.replace("MATERIALES INCOMBUSTIBLES NO MACIZOS", "").replace("(", "").replace(")", "").trim();
				
						}
						ubicacion.setTechos(fn.material(newcontenido));
					}
					
					//muros
					inicio = resultado.indexOf("Muros");
					fin = resultado.indexOf("Ubicado");
					if(inicio > -1 && fin > inicio) {
						newcontenido = resultado.substring(inicio + 5, fin).replace("###", "").replace("\r\n", "").trim();
						ubicacion.setMuros(fn.material(newcontenido));
					}
					
					//niveles
					inicio = resultado.indexOf("No. de Pisos del edificio");
					if(inicio > -1) {
						newcontenido = resultado.substring(inicio + 25, resultado.length()).replace("###", "").replace("\r\n", "").trim();
						if(newcontenido.contains("(")) {
							newcontenido = newcontenido.replace("(", "&&&&");
							ubicacion.setNiveles(Integer.parseInt(newcontenido.split("&&&&")[0].trim()));
						}
					}
                if(ubicacion.getNombre().length() > 0)
					ubicaciones.add(ubicacion);
					if(ubicaciones.size() > 0) {
						modelo.setUbicaciones(ubicaciones);
					}
				
				}else {
	
					inicio = ubicacionesT.indexOf("Descripción del Movimiento");
					fin = ubicacionesT.lastIndexOf("Resumen de secciones contratada");
					
					resultado = "";
					if(inicio > -1 && fin > inicio) {
	
						EstructuraUbicacionesModel ubicacion = new EstructuraUbicacionesModel();
						newcontenido = ubicacionesT.substring(inicio + 26, fin).replace("@@@", "").trim();
						for(String x : newcontenido.split("\r\n")) {
							if(x.contains("###CARTERA")) resultado += x.split("###CARTERA")[0].trim() + "\r\n";
							else resultado += x + "\r\n";
						}

						//giro
						//nombre
						inicio = resultado.indexOf("Giro");
						fin = resultado.indexOf("Descripción del Giro");
						if(inicio > -1 && fin > inicio) {
							ubicacion.setGiro(fn.gatos(resultado.substring(inicio + 4, fin)).replace("###", "").replace("\r\n", "").trim());
							ubicacion.setNombre(ubicacion.getGiro());
						}
						
						//techos
						inicio = resultado.indexOf("Techos");
						fin = resultado.indexOf("Muros");
					
						if(inicio > -1 && fin > inicio) {
							newcontenido = resultado.substring(inicio + 6, fin).replace("###", "").replace("\r\n", " ").trim();
						
							if(newcontenido.contains("MATERIALES INCOMBUSTIBLES")) {
								newcontenido = newcontenido.replace("MATERIALES INCOMBUSTIBLES NO MACIZOS", "").replace("(", "").replace(")", "").trim();
							
							}

							ubicacion.setTechos(fn.material(newcontenido));
						}
						
						//muros
						inicio = resultado.indexOf("Muros");
						fin = resultado.indexOf("Año de Construcción");
						if(inicio > -1 && fin > inicio) {
							newcontenido = resultado.substring(inicio + 5, fin).replace("###", "").replace("\r\n", "").trim();
					;
							ubicacion.setMuros(fn.material(newcontenido));
						}
						
						//niveles
						inicio = resultado.indexOf("Número de pisos");
						fin = resultado.indexOf("Techos");
						if(inicio > -1) {
							newcontenido = resultado.substring(inicio+15 , fin).replace("###", "").replace("\r\n", "").trim();
						
							if(fn.isNumeric(newcontenido)) {
							
								ubicacion.setNiveles(Integer.parseInt(newcontenido));
							}
						}
						  if(ubicacion.getNombre().length() > 0)
						if(ubicaciones.size() > 0) {
							modelo.setUbicaciones(ubicaciones);
						}
					
					
					}
					
				}
			}
			
		
			//**************************************RECIBOS
            List<EstructuraRecibosModel> recibos = new ArrayList<>();
			EstructuraRecibosModel recibo = new EstructuraRecibosModel();
			switch (modelo.getFormaPago()) {
			case 1:
				recibo.setReciboId("");
				recibo.setSerie("1/1");
				recibo.setVigenciaDe(modelo.getVigenciaDe());
				recibo.setVigenciaA(modelo.getVigenciaA());
				if (recibo.getVigenciaDe().length() > 0) {
					recibo.setVencimiento(fn.dateAdd(recibo.getVigenciaDe(), 30, 1));
				}
				recibo.setPrimaneta(fn.castBigDecimal(modelo.getPrimaneta(), 2));
				recibo.setDerecho(fn.castBigDecimal(modelo.getDerecho(), 2));
				recibo.setRecargo(fn.castBigDecimal(modelo.getRecargo(), 2));
				recibo.setIva(fn.castBigDecimal(modelo.getDerecho(), 2));

				recibo.setPrimaTotal(fn.castBigDecimal(modelo.getPrimaTotal(), 2));
				recibo.setAjusteUno(fn.castBigDecimal(modelo.getAjusteUno(), 2));
				recibo.setAjusteDos(fn.castBigDecimal(modelo.getAjusteDos(), 2));
				recibo.setCargoExtra(fn.castBigDecimal(modelo.getCargoExtra(), 2));
				recibos.add(recibo);
				break;
			}
			modelo.setRecibos(recibos);
		
			return modelo;
		} catch (Exception ex) {
			modelo.setError(
					GnpDiversosModel.this.getClass().getTypeName() + " | " + ex.getMessage() + " | " + ex.getCause());
			return modelo;
		}
		
	}

	public void extctCoberturas(List<EstructuraCoberturasModel> coberturas,Integer tipo) {
		  if(tipo == 1) {//esto es para estraer coberutas de 1* version
				newcontenido = "";
				inicio = contenido.indexOf("###Coaseguro###Primas");
				fin = contenido.indexOf("Total Coberturas");
				if(inicio > -1 && fin > inicio) {
					resultado = contenido.substring(inicio + 21, fin).replace("@@@", "").trim();
					resultado = resultado.replace("C ris tales", "Cristales").replace("A rt ículos", "Artículos").replace("Res ponsabilidad", "Responsabilidad")
							.replace("p ro pietario", "propietario").replace("G as tos", "Gastos").replace("Rem oción", "Remoción").replace("Rie sgos", "Riesgos")
							.replace("E qu ipo", "Equipo").replace("Dañ os", "Daños").replace("Rie sgos", "Riesgos").replace("V  ", "V###").replace("Me naje", "Menaje")
							.replace("No Aplica  ", "No Aplica###").replace("T er remoto", "Terremoto").replace("v olc ánica", "volcánica").replace("M e naje", "Menaje")
							.replace("D añ os", "Daños").replace("Cris tales", "Cristales").replace("Gas tos", "Gastos").replace("Equ ipo", "Equipo").replace("R ie sgos", "Riesgos")
							.replace("R em oción", "Remoción").replace("volc ánica", "volcánica").replace("I Rie", "Rie").replace("No Aplica ", "No Aplica###")
							.replace("V l###", "Vl###").replace("l ll###", "lll###");
					newcontenido = clearCoberturas(resultado);
					for(int i = 0; i < newcontenido.split("\r\n").length; i++) {
						EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();
						esverdad = false;
						String x = "", y = "", z = "";
						x = newcontenido.split("\r\n")[i];
						if(i + 1 < newcontenido.split("\r\n").length) {
							if(newcontenido.split("\r\n")[i+1].split("###").length == 1 /*&& (x.split("###").length == 4 ) || x.split("###").length == 5*/) {
								y = fn.cleanString(newcontenido.split("\r\n")[i+1]);
								if(fn.isNumeric(y)) {
									esverdad = true;
									y = "";
								}else if(newcontenido.split("\r\n")[i+1].contains("Daños a") == false) {
									y = newcontenido.split("\r\n")[i+1].trim();
									esverdad = true;
								}else {
									y = "";
								}
							}else {
								y = "";
							}
						}else {
							y = "";
						}
						if(i + 2 < newcontenido.split("\r\n").length) {
							if(newcontenido.split("\r\n")[i+2].split("###").length == 1 && esverdad) {
								z = fn.cleanString(newcontenido.split("\r\n")[i+2].trim());
								if(fn.isNumeric(z)) {
									z = "";
								}else if(newcontenido.split("\r\n")[i+2].contains("Daños a") == false) {
									z = newcontenido.split("\r\n")[i+2].trim();
								}else {
									z = "";
								}
							}else {
								z = "";
							}
						}else {
							z = "";
						}
						if (x.split("###").length == 2) {
							seccion = x.split("###")[0].trim();
							cobertura.setNombre(x.split("###")[1].trim());
							cobertura.setSeccion(seccion);
							coberturas.add(cobertura);
						}else if((x.contains("Daños al inmueble") || x.contains("Daños a los")) && x.split("###").length == 1){
							cobertura.setNombre(x.split("###")[0].trim());
							cobertura.setSeccion(seccion);
							coberturas.add(cobertura);
						}else {
							switch (x.split("###").length) {
							case 5:case 4:
								nombre = "";
								nombre = x.split("###")[0].trim();
								if(y.length() > 0) {
									nombre += " " + y;
								}
								if(z.length() > 0) {
									nombre += " " + z;
								}
								cobertura.setNombre(nombre);
								cobertura.setSa(x.split("###")[1].trim());
								cobertura.setDeducible(x.split("###")[2].trim());
								cobertura.setCoaseguro(x.split("###")[3].trim());
								cobertura.setSeccion(seccion);
								coberturas.add(cobertura);
								break;
							}
						}
					}	
				}  
		  }
		  
		  if(tipo == 2) {
			   for (int i = 0; i < contenido.split("Suma Asegurada").length; i++) {
	                if (contenido.split("Suma Asegurada")[i].contains("Cuando###se###refieran")) {
	                    newcontenido = contenido.split("Suma Asegurada")[i].split("Cuando###se###refieran")[0];
	                } else if (contenido.split("Suma Asegurada")[i].contains("Agente")) {
	                    if (contenido.split("Suma Asegurada")[i].contains("Coaseguro")) {
	                    	newcontenido += contenido.split("Suma Asegurada")[i].split("Agente")[0].split("SERVICIOS DE ASISTENCIAS AMPARADAS")[0];
	                    }
	                }
	            }
			   
			
	            for (String co : newcontenido.split("\n")) {
	            	EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();
	                int x = co.split("###").length;
	                if (x > 1) {
	                    if (co.contains("Deducible")) {
	                    } else {
	                        if (x == 3) {
	                            cobertura.setNombre(co.split("###")[0]);
	                            cobertura.setSa(co.split("###")[1]);
	                            cobertura.setDeducible(co.split("###")[2].replace("\r", ""));
	                            cobertura.setCoaseguro(co.split("###")[2].replace("\r", ""));
	                            coberturas.add(cobertura);
	                        }
	                        if (x == 4) {	                           
	                            cobertura.setNombre(co.split("###")[0]);
	                            cobertura.setSa(co.split("###")[1]);
	                            cobertura.setDeducible(co.split("###")[2].replace("\r", ""));
	                            cobertura.setCoaseguro(co.split("###")[3].replace("\r", ""));
	                            coberturas.add(cobertura);
	                        }
	                    }

	                }
	            }

	  
		  }
	}
	
	private String clearCoberturas(String resultado) {
		String a = "";
		try {
			for(String x: resultado.split("\r\n")) {
				x = x.trim();
				if(x.length() > 0 && (x.contains("las 12") || x.contains("Agosto")|| x.contains("Mes###Año")) == false) {
					boolean cobertura = false;
					fin = x.indexOf("Duración");
					if(fin > -1) {
						a += fn.gatos(x.substring(0, fin).trim()).trim() + "\r\n";
						cobertura = true;
					}
					fin = x.indexOf("Importe");
					if(fin > -1) {
						a += fn.gatos(x.substring(0, fin).trim()).trim() + "\r\n";
						cobertura = true;
					}else {
						fin = x.indexOf("mporte Total");
						if(fin > -1) {
							a += fn.gatos(x.substring(0, fin).trim()).trim() + "\r\n";
							cobertura = true;
						}
					}
					fin = x.indexOf("Movimiento");
					if(fin > -1) {
						a += fn.gatos(x.substring(0, fin).trim()).trim() + "\r\n";
						cobertura = true;
					}
					fin = x.indexOf("Descuentos");
					if(fin > -1) {
						a += fn.gatos(x.substring(0, fin).trim()).trim() + "\r\n";
						cobertura = true;
					}
					fin = x.indexOf("Aplican Condiciones");
					if(fin > -1) {
						a += fn.gatos(x.substring(0, fin).trim()).trim() + "\r\n";
						cobertura = true;
					}
					fin = x.indexOf("Póliza paquetes");
					if(fin > -1) {
						a += fn.gatos(x.substring(0, fin).trim()).trim() + "\r\n";
						cobertura = true;
					}
					fin = x.indexOf("Vigencia");
					if(fin > -1) {
						a += fn.gatos(x.substring(0, fin).trim()).trim() + "\r\n";
						cobertura = true;
					}
					if(cobertura == false) {
						a += fn.gatos(x.trim()) + "\r\n";
					}
				}
			}
			return a;	
		} catch (Exception ex) {
			System.out.println("error.DatosGnpDanos.clearCoberturas: " + ex.getMessage() + " | " + ex.getCause());
			return a;
		}
	}

}
