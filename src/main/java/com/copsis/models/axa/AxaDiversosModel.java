package com.copsis.models.axa;

import java.util.ArrayList;
import java.util.List;

import com.copsis.constants.ConstantsValue;
import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraCoberturasModel;
import com.copsis.models.EstructuraJsonModel;
import com.copsis.models.EstructuraUbicacionesModel;



public class AxaDiversosModel {
	// Clases
	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
	private static String contenido = "";
	
	
	public AxaDiversosModel(String contenido) {
		AxaDiversosModel.contenido = contenido;
	}

	public EstructuraJsonModel procesar() {
		String textbusq = "";
		int inicio = 0;
		int fin = 0;
		
		String newcontenido = "";
		contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());
		contenido = contenido.replace("D omicilio:", ConstantsValue.DOMICILIO).replace("F RESNOS", "FRESNOS")
				.replace("P r i m a T o t a l", ConstantsValue.PRIMA_TOTAL2).replace("Prima###neta:", "Prima Neta:").replace("Prima neta", "Prima Neta")
				.replace("expedición", ConstantsValue.EXPEDICION3).replace("C .P:", "C.P:").replace("R .F.C: ", "R.F.C: ")
				.replace("@@@I.V.A:", ConstantsValue.IVA).replace("I.V.A", ConstantsValue.IVA).replace("I.V.A..", ConstantsValue.IVA).replace("Prima###Total", ConstantsValue.PRIMA_TOTAL2).replace("financiamiento", ConstantsValue.FINANCIAMIENTO)
				.replace("Datos del Contratante","Datos del contratante")
				.replace("Costo del Seguro", "Costo del seguro")
				.replace("R ###.F.C", ConstantsValue.RFC).replace("Forma de Pago", "Forma de pago");
		
		try {
			modelo.setTipo(7);
			modelo.setCia(20);

			textbusq="Datos del contratante";
			inicio = contenido.indexOf("Datos del contratante");
			if(inicio == -1) {
				textbusq="Datos###del###contratante";
				inicio = contenido.indexOf("Datos###del###contratante");
				if(inicio == -1) {
					textbusq="Datos del asegurado###Póliza";
					inicio = contenido.indexOf("Datos del asegurado###Póliza");//se usa para version 2
				}
			}
			fin  = contenido.indexOf("Costo del seguro");
			if(fin == -1) {
				fin  = contenido.indexOf("Costo###del###seguro"); 
				if(fin == -1) {
					fin  = contenido.indexOf("Paquete contratado:");//se usa para version 2
				}
			}
			
			
			if(inicio > 0 && fin > 0 && inicio < fin) {
				newcontenido = contenido.substring(inicio,fin);

				for (int i = 0; i < newcontenido.split("\n").length; i++) {
					if((newcontenido.split("\n")[i].contains("Póliza") ||newcontenido.split("\n")[i].contains("PÓLIZA")) && newcontenido.split("\n")[i].contains(textbusq)) {
				
						if(newcontenido.split("\n")[i+1].split("###").length > 1) {
							String x = newcontenido.split("\n")[i+1].replace("Nombre:", "");
							modelo.setPoliza(x.split("###")[x.split("###").length -1].replace("\r", ""));
							modelo.setCteNombre((x.split(modelo.getPoliza())[0].replace("###", " ")).trim());							
						}else {							
							String x = newcontenido.split("\n")[i+1].replace(" ", "###").replace("Nombre:", "");
						
							modelo.setPoliza(x.split("###")[x.split("###").length -1].replace("\r", ""));
							modelo.setCteNombre((x.split(modelo.getPoliza())[0].replace("###", " ")).trim());
						}									
					}
					if (newcontenido.split("\n")[i].contains("Domicilio:")) {
						String x = "";
						if (newcontenido.split("\n")[i + 1].contains("C.P:")) {
							
							x = (newcontenido.split("\n")[i + 1].split("C.P:")[0]).replace("\r", "");
						}else if(newcontenido.split("\n")[i + 1].contains(ConstantsValue.DESDE) && !newcontenido.split("\n")[i + 1].contains(ConstantsValue.VIGENCIA2)) {
							x = newcontenido.split("\n")[i + 1].split(ConstantsValue.DESDE)[0];
						}
						if (newcontenido.split("\n")[i + 1].contains(ConstantsValue.VIGENCIA2)) {
							x = (newcontenido.split("\n")[i + 1].split(ConstantsValue.VIGENCIA2)[0]).replace("\r", "");
						}
						if (newcontenido.split("\n")[i + 1].contains("Vigencia a las")) {
							x = (newcontenido.split("\n")[i + 1].split("Vigencia a las")[1]).replace("\r", "").replace("1 2 hrs", "");
						}


						modelo.setCteDireccion((newcontenido.split("\n")[i].split("Domicilio:")[1].split(ConstantsValue.VIGENCIA2)[0] + x).replace("\r", "").replace("###", "").replace("@@@", "").replace("  ", "").replace("C ALLE", "CALLE").trim());
					}
					if(newcontenido.split("\n")[i].contains("C.P:")) {
						if(newcontenido.split("\n")[i].split("C.P:")[1].contains(ConstantsValue.VIGENCIA2)) {
							if(newcontenido.split("\n")[i].split("C.P:")[1].split(ConstantsValue.VIGENCIA2)[0].length() > 7) {
            					modelo.setCp(newcontenido.split("\n")[i].split("C.P.")[1].substring(0,5));
            				}else {
            					modelo.setCp(newcontenido.split("\n")[i].split("C.P:")[1].split("Vigencia")[0].trim());
            				}
							
						}
						if(newcontenido.split("\n")[i].split("C.P:")[1].contains(ConstantsValue.DESDE)) {
							if(newcontenido.split("\n")[i].split("C.P:")[1].split(ConstantsValue.DESDE)[0].replace("###", "").length() > 7) {
								modelo.setCp(newcontenido.split("\n")[i].split("C.P.")[1].substring(0,5));
							}else {
								modelo.setCp(newcontenido.split("\n")[i].split("C.P:")[1].split(ConstantsValue.DESDE)[0].replace("###", "").trim());
							}
						
						}else if(!newcontenido.split("\n")[i].split("C.P:")[1].contains("###") && fn.isNumeric(newcontenido.split("\n")[i].split("C.P:")[1].trim())){
							modelo.setCp(newcontenido.split("\n")[i].split("C.P:")[1].trim());
						}
						
					}
					if(newcontenido.split("\n")[i].contains(ConstantsValue.RFC)) {
						if(newcontenido.split("\n")[i].split(ConstantsValue.RFC)[1].contains(ConstantsValue.DESDE) && modelo.getRfc().length() == 0) {
							modelo.setRfc(newcontenido.split("\n")[i].split(ConstantsValue.RFC)[1].split(ConstantsValue.DESDE)[0].replace("###", "").replace(":", "").trim());
						}
						if(newcontenido.split("\n")[i].split(ConstantsValue.RFC)[1].contains(ConstantsValue.HASTA2)&& modelo.getRfc().length() == 0) {
							modelo.setRfc(newcontenido.split("\n")[i].split("R.F.C:")[1].split(ConstantsValue.HASTA2)[0].replace("###", "").replace(":", "").trim());
						}
						else {
							if( modelo.getRfc().length() == 0) {
								modelo.setRfc(newcontenido.split("\n")[i].split("R.F.C:")[1].replace("\r", "").replace("###", "").replace(":", "").trim());
							}
							
						}
						
					}
					if(newcontenido.split("\n")[i].contains("Desde")) {
						modelo.setVigenciaDe(fn.formatDateMonthCadena(newcontenido.split("\n")[i].split("Desde")[1].replace("\r", "").replace(" ", "").replace("###", "")));
					}
					if(newcontenido.split("\n")[i].contains("Hasta")) {
						modelo.setVigenciaA(fn.formatDateMonthCadena(newcontenido.split("\n")[i].split("Hasta")[1].replace("\r", "").replace(" ", "").replace("###", "")));
					}
					if(newcontenido.split("\n")[i].contains("Emisión")) {
						if(newcontenido.split("\n")[i].split("Emisión")[1].replace("\r", "").replace(" ", "").replace("###", "").split("-").length == 3) {
							modelo.setFechaEmision(fn.formatDateMonthCadena(newcontenido.split("\n")[i].split("Emisión")[1].replace("\r", "").replace(" ", "").replace("###", "")));
						}
						if(modelo.getFechaEmision().length() == 0){
							String textoSiguienteRenglon = newcontenido.split("\n")[i+1].replace("\r", "");
							String[] valores = textoSiguienteRenglon.split("###");
							if(valores[valores.length-1].trim().split("-").length == 3) {
								modelo.setFechaEmision(fn.formatDateMonthCadena(valores[valores.length-1]));
							}
						}
					}
					
					
					if(newcontenido.split("\n")[i].contains("Moneda")) {
						modelo.setMoneda(fn.moneda(fn.eliminaSpacios(newcontenido.split("\n")[i].split("Moneda")[1].split("\n")[0].replace(":", "").replace("###", "").replace("\r", "").replace("\u0020"," ").replace("\u00A0"," "))));
					}
					
					if(newcontenido.split("\n")[i].contains("Nombre") && newcontenido.split("\n")[i].contains(ConstantsValue.AGENTE)) {
						
						modelo.setAgente((newcontenido.split("\n")[i].split(ConstantsValue.AGENTE)[1].split("###")[newcontenido.split("\n")[i].split(ConstantsValue.AGENTE)[1].split("###").length -2]).trim().replace("###", "").replace("  ", ""));
					}
				
					if((newcontenido.split("\n")[i].contains("Número") || newcontenido.split("\n")[i].contains("No."))&& newcontenido.split("\n")[i].contains(ConstantsValue.AGENTE)) {
						modelo.setCveAgente((newcontenido.split("\n")[i].split(ConstantsValue.AGENTE)[1].split("No.")[0]).trim().replace("###", "").replace("  ", ""));
					}
				
					if(newcontenido.split("\n")[i].contains("Subramo") && newcontenido.split("\n")[i].contains("pago")) {
						if(newcontenido.split("\n")[i].contains("Folio:")) {
							modelo.setFormaPago(fn.formaPago(newcontenido.split("\n")[i+1].split("Folio:")[1].replace("###", "").replace("\r", "").trim()));
						}else {
							String[] valores = newcontenido.split("\n")[i+1].split("###");
							modelo.setFormaPago(fn.formaPago(valores[valores.length-1].replace("\r", "").trim()));
						}
					}
					if(newcontenido.split("\n")[i].contains("Uso:") && newcontenido.split("\n")[i].contains("pago")) {
						modelo.setFormaPago(fn.formaPagoSring(newcontenido.split("\n")[i+1].split("Régimen:")[1].replace("Vivienda Sola", "").replace("###", "").replace("\r", "").trim()));
					}
				
				}
			}
			
			
			//Primas de la poliza
			inicio = contenido.indexOf(ConstantsValue.PRIMA_NETA);
			fin = contenido.indexOf("Relación de bienes opcionales");

			if( fin == -1) {
				inicio = contenido.indexOf("Costo###del###seguro");
				fin = contenido.indexOf("AXA###Seguros");
				if(inicio ==-1 && fin == -1) {
					inicio = contenido.indexOf("Costo del seguro");
					fin = contenido.indexOf("AXA Seguros, S.A.");
				}
			}

			if(inicio > fin) {
				inicio = contenido.indexOf(ConstantsValue.PRIMA_NETA);
				fin = contenido.indexOf(ConstantsValue.PRIMA_TOTAL2);
				if(fin > -1) {
					fin =fin+100;
				}
			}
			if(inicio == -1){
				inicio = contenido.indexOf("Prima Neta");
			}
			
			

			if(inicio > 0 && fin > 0 && inicio < fin) {
				newcontenido = contenido.substring(inicio,fin);				
				for (int i = 0; i < newcontenido.split("\n").length; i++) {		
					if(newcontenido.split("\n")[i].contains(ConstantsValue.PRIMA_NETA)) {
						modelo.setPrimaneta(fn.castBigDecimal( fn.castDouble(newcontenido.split("\n")[i].split(ConstantsValue.PRIMA_NETA)[1].replace(":", "").replace("###", "").replace("\r", ""))));
					}
					if(newcontenido.split("\n")[i].contains("Financiamiento")) {
						if(newcontenido.split("\n")[i].contains("Financiamiento 0.0%")) {
							modelo.setDerecho(fn.castBigDecimal( fn.castDouble(newcontenido.split("\n")[i].split("Financiamiento 0.0%")[1].replace("###", "").replace("\r", ""))));	
						}else {							
							modelo.setDerecho(fn.castBigDecimal( fn.castDouble(newcontenido.split("\n")[i].split("Financiamiento")[1].split("###")[1].replace("###", "").replace("\r", ""))));
						}						
					}					
					if(newcontenido.split("\n")[i].contains(ConstantsValue.EXPEDICION3)) {
						modelo.setRecargo(fn.castBigDecimal( fn.castDouble(newcontenido.split("\n")[i].split(ConstantsValue.EXPEDICION3)[1].replace(":", "").replace("###", "").replace("\r", ""))));
					}					
					if(newcontenido.split("\n")[i].contains(ConstantsValue.IVA)) {
						if(newcontenido.split("\n")[i].split(ConstantsValue.IVA)[1].replace(":", "").split("###")[1].contains("%")) {
							modelo.setIva(fn.castBigDecimal( fn.castDouble(newcontenido.split("\n")[i].split(ConstantsValue.IVA)[1].replace(":", "").split("###")[0].replace("###", "").replace("\r", ""))));
						}else {
							modelo.setIva(fn.castBigDecimal( fn.castDouble(newcontenido.split("\n")[i].split(ConstantsValue.IVA)[1].replace(":", "").split("###")[1].replace("###", "").replace("\r", ""))));
						}													
					}					
					if(newcontenido.split("\n")[i].contains(ConstantsValue.PRIMA_TOTAL2)) {
						modelo.setPrimaTotal(fn.castBigDecimal( fn.castDouble(newcontenido.split("\n")[i].split(ConstantsValue.PRIMA_TOTAL2)[1].replace(":", "").replace("###", "").replace("\r", ""))));
					}
				}				
			}
			

			obtenerPlan(contenido,modelo);
			
			//Proceso para ubicaciones
			inicio = contenido.indexOf("Descripción de la ");
			fin = contenido.indexOf("Paquete contratado:");
			if(inicio == -1) {
				inicio = contenido.indexOf("Datos de la Ubicación");
			}
			if(fin == -1) {
				fin = contenido.indexOf("Módulo - Cobertura");
			}
	
			if(inicio > 0 && fin > 0 && inicio < fin) {
				List<EstructuraUbicacionesModel> ubicaciones = new ArrayList<>();
				EstructuraUbicacionesModel ubicacion = new EstructuraUbicacionesModel();
				newcontenido = contenido.substring(inicio,fin).replace("@@@","").replace("\r", "").replace("\u0020"," ").replace("\u00A0"," ").replace("s ###uministro", "suministro")
						.replace("e ###léctrica", "eléctrica");	
				for (int i = 0; i < newcontenido.split("\n").length; i++) {	
				if(contenido.contains("Hogar Integral") && newcontenido.split("\n")[i].contains("Uso:") && newcontenido.split("\n")[i].contains("Forma")) {										
						 ubicacion.setGiro(newcontenido.split("\n")[i].split("Uso:")[1].split("Forma")[0].replace("###", "").trim());
						
				   }else if(newcontenido.split("\n")[i].contains("Giro de Negocio:")) {
					   String valorNegocio = fn.gatos(newcontenido.split("\n")[i].split("Giro de Negocio:")[1].trim());
					   valorNegocio = valorNegocio.split("###")[0].trim();
					   ubicacion.setGiro(valorNegocio);
					   if(valorNegocio.equalsIgnoreCase("Construcción y supervisión de obras para el") && (i+2)<newcontenido.split("\n").length) {
						   StringBuilder negocio = new StringBuilder();
						   if(newcontenido.split("\n")[i+1].startsWith("suministro de") && newcontenido.split("\n")[i+2].startsWith("eléctrica")) {
							   negocio.append(valorNegocio);
							   negocio.append(" ").append(newcontenido.split("\n")[i+1].split("###")[0].trim());
							   negocio.append(" ").append(newcontenido.split("\n")[i+2].split("###")[0].trim());
							   ubicacion.setGiro(negocio.toString());
						   }
					   }
				   }
					
					if(newcontenido.split("\n")[i].contains("Muros:")) {
						String textoMuros = fn.gatos(fn.eliminaSpacios(newcontenido.split("\n")[i].trim().split("Muros:")[1]));
						ubicacion.setMuros(fn.material(textoMuros.split("###")[0]));
					}
					if(newcontenido.split("\n")[i].contains("Niveles:")) {
						String valorNiveles = newcontenido.split("\n")[i].split("Niveles:")[1].replace("\r", "").replace("###","").trim();
						if(fn.isNumeric(valorNiveles)) {
							ubicacion.setNiveles(fn.castInteger(valorNiveles).intValue());
						}else if(i+1 < newcontenido.split("\n").length) {
							valorNiveles = newcontenido.split("\n")[i+1].split("###")[0].trim();
							if(fn.isNumeric(valorNiveles)) {
								ubicacion.setNiveles(fn.castInteger(valorNiveles).intValue());
							}
						}
					}
					
					if(newcontenido.split("\n")[i].contains("Techos:")) {
						String textoMuros = fn.gatos(fn.eliminaSpacios(newcontenido.split("\n")[i].trim().split("Techos:")[1]));
						ubicacion.setTechos(fn.material(textoMuros.split("###")[0]));
					}
				}

				if(contenido.contains("Hogar Integral") || contenido.contains("PLANPROTEGE")) {
					ubicacion.setCalle(modelo.getCteDireccion());
					ubicacion.setCp(modelo.getCp());
				}
							
				ubicaciones.add(ubicacion);
				modelo.setUbicaciones(ubicaciones);
			}

			
//			/*Proceoso para las  coberturas*/
			inicio = contenido.indexOf("Paquete contratado");
			fin = contenido.indexOf("Prima Neta");
			if(inicio > fin ) {
				fin = contenido.lastIndexOf("Prima Neta");
			}

			if(inicio > 0 && fin > 0 && inicio < fin) {
				List<EstructuraCoberturasModel> coberturas = new ArrayList<>();				
				newcontenido = contenido.substring(inicio,fin);				
				for (int i = 0; i < newcontenido.split("\n").length; i++) {
					EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();
				    int x = newcontenido.split("\n")[i].split("###").length;
				    if(newcontenido.split("\n")[i].contains("Suma Asegurada")) {
				    	
				    }else {

				    	  if(x == 2) {
						    	cobertura.setNombre(newcontenido.split("\n")[i].split("###")[0]);
						    	cobertura.setSa(newcontenido.split("\n")[i].split("###")[1].replace("\r", ""));
						    	coberturas.add(cobertura);				
						    }
						    if(x == 3) {
						    	cobertura.setNombre(newcontenido.split("\n")[i].split("###")[0]);
						    	cobertura.setSa(newcontenido.split("\n")[i].split("###")[1]);
						    	cobertura.setDeducible(newcontenido.split("\n")[i].split("###")[2].replace("\r", ""));
						    	coberturas.add(cobertura);
						    }
						    if(x == 4) {
						    	cobertura.setNombre(newcontenido.split("\n")[i].split("###")[0]);
						    	cobertura.setSa(newcontenido.split("\n")[i].split("###")[1]);
						    	cobertura.setDeducible(newcontenido.split("\n")[i].split("###")[2].replace("\r", ""));
						    	coberturas.add(cobertura);
						    }
				    }
				  
				    
				}
				modelo.setCoberturas(coberturas);
			}
			
			if(modelo.getCoberturas().isEmpty()) {
				obtenerCoberturas(contenido, modelo);
			}
			return modelo;
		} catch (Exception ex) {
			modelo.setError(
					AxaDiversosModel.this.getClass().getTypeName() + " | " + ex.getMessage() + " | " + ex.getCause());
			return modelo;
		}
		
	}
		
	private void obtenerCoberturas(String texto, EstructuraJsonModel model) {
		StringBuilder contenidoCoberturas = new StringBuilder();
		String otraPalabraInicioRangoCoberturas = "Coberturas Catastróficas";
		contenidoCoberturas.append(extraerTextoCoberturas(texto, "Módulo - Cobertura", "AXA Seguros, S.A"));
		contenidoCoberturas.append(otraPalabraInicioRangoCoberturas+"\n").append(extraerTextoCoberturas(texto,otraPalabraInicioRangoCoberturas, "Medidas de Seguridad"));
		
		String strContenidoCoberturas = contenidoCoberturas.toString().replace("@@@", "");
		String[] arrContenido = strContenidoCoberturas.split("\n");
		int numValores = 0;
		
		List<EstructuraCoberturasModel> coberturas = new ArrayList<>();
		String seccion ="";
		String deducible =  "";
		String coaseguro = "";
		
		for (int i = 0; i < arrContenido.length; i++) {
			EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();
			arrContenido[i] = fn.gatos(arrContenido[i]);
			if(arrContenido[i].split("###")[0].contains(otraPalabraInicioRangoCoberturas) && otraPalabraInicioRangoCoberturas.length()> 0) {
				seccion = "";
			}else if(fn.seccion(arrContenido[i].split("###")[0].trim()).length() > 0 ) {
				seccion = arrContenido[i].split("###")[0].trim();
			}else if(fn.seccion(arrContenido[i].trim().split(" ")[0].replace(".", "").trim()).length()>0) {
				seccion = arrContenido[i].trim().split(" ")[0].replace(".", "").trim();
				arrContenido[i] = arrContenido[i].replace("###", "");
			}

			if (arrContenido[i].split("###").length > 1
					&& !arrContenido[i].contains("SECCIÓN")
					&& !arrContenido[i].contains(ConstantsValue.PRIMA_NETA)
					&& !arrContenido[i].contains("Las coberturas básicas por giro, se")
					&& !arrContenido[i].contains("Para las coberturas arriba")
					&& !arrContenido[i].startsWith("Subtotal")
					&& arrContenido[i].length() > 18) {
					deducible = "";
					coaseguro = "";				
					numValores = arrContenido[i].split("###").length;
					if (numValores > 2) {
						deducible = arrContenido[i].split("###")[2];
					}
					
					if(numValores > 3) {
						coaseguro = arrContenido[i].split("###")[3];
					}
					
					cobertura.setSeccion(seccion);
					cobertura.setNombre(arrContenido[i].split("###")[0].trim());
					cobertura.setSa(arrContenido[i].split("###")[1].trim());
					cobertura.setDeducible(deducible);
					cobertura.setCoaseguro(coaseguro);
					coberturas.add(cobertura);


			}
		}
		model.setCoberturas(coberturas);

	}

	private StringBuilder extraerTextoCoberturas(String texto,String palabraInicio, String palabraFinal) {
		StringBuilder contenidoCoberturas = new StringBuilder();
		for(int i =0; i<texto.split(palabraInicio).length;i++) {
			if(i> 0 && texto.split(palabraInicio)[i].contains(palabraFinal)) {
				contenidoCoberturas.append(" ").append(texto.split(palabraInicio)[i].split(palabraFinal)[0]);
			}
		}
		return contenidoCoberturas;
	}
	
	private void obtenerPlan(String texto, EstructuraJsonModel model) {
		if(texto.contains("Paquete contratado:")) {
			model.setPlan(texto.split("Paquete contratado:")[1].split("\n")[0].replace("###", "").trim());
		}else{
			int inicioIndex = contenido.indexOf("CARÁTULA DE PÓLIZA");
			int finIndex = contenido.indexOf("Datos del contratante");
			if(inicioIndex > -1 && inicioIndex < finIndex) {
				String text = contenido.substring(inicioIndex,finIndex).replace("@@@", "");
				if(text.split("\n").length > 0) {
					text = text.split("\n")[1];
					text = text.split("###")[text.split("###").length-1];
					model.setPlan(text.split("-")[0].trim());
					if(model.getPlan().equals("PLANPROTEGE")) {
						model.setPlan("Planprotege");
					}
				}
			}
		}
	}
}
