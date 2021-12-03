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
				.replace("P r i m a T o t a l", ConstantsValue.PRIMA_TOTAL2).replace("Prima###neta:", "Prima Neta:").replace("Prima neta:", "Prima Neta:")
				.replace("expedición", ConstantsValue.EXPEDICION3).replace("C .P:", "C.P:").replace("R .F.C: ", "R.F.C: ")
				.replace("@@@I.V.A:", ConstantsValue.IVA).replace("I.V.A", ConstantsValue.IVA).replace("I.V.A..", ConstantsValue.IVA).replace("Prima###Total", ConstantsValue.PRIMA_TOTAL2).replace("financiamiento", ConstantsValue.FINANCIAMIENTO);
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
					if(newcontenido.split("\n")[i].contains("Póliza") && newcontenido.split("\n")[i].contains(textbusq)) {
				
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
						}
						if (newcontenido.split("\n")[i + 1].contains(ConstantsValue.VIGENCIA2)) {
							x = (newcontenido.split("\n")[i + 1].split(ConstantsValue.VIGENCIA2)[0]).replace("\r", "");
						}
						if (newcontenido.split("\n")[i + 1].contains("Vigencia a las")) {
							x = (newcontenido.split("\n")[i + 1].split("Vigencia a las")[1]).replace("\r", "").replace("1 2 hrs", "");
						}


						modelo.setCteDireccion((newcontenido.split("\n")[i].split("Domicilio:")[1] + x).replace("\r", "").replace("###", "").replace("@@@", "").replace("  ", "").trim());
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
						
						}						
					}
					if(newcontenido.split("\n")[i].contains(ConstantsValue.RFC)) {
						if(newcontenido.split("\n")[i].split(ConstantsValue.RFC)[1].contains(ConstantsValue.DESDE) && modelo.getRfc().length() == 0) {
							modelo.setRfc(newcontenido.split("\n")[i].split(ConstantsValue.RFC)[1].split(ConstantsValue.DESDE)[0].replace("###", "").trim());
						}
						if(newcontenido.split("\n")[i].split(ConstantsValue.RFC)[1].contains(ConstantsValue.HASTA2)&& modelo.getRfc().length() == 0) {
							modelo.setRfc(newcontenido.split("\n")[i].split("R.F.C:")[1].split(ConstantsValue.HASTA2)[0].replace("###", "").trim());
						}
						else {
							if( modelo.getRfc().length() == 0) {
								modelo.setRfc(newcontenido.split("\n")[i].split("R.F.C:")[1].replace("\r", "").replace("###", ""));
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
						modelo.setFechaEmision(fn.formatDateMonthCadena(newcontenido.split("\n")[i].split("Emisión")[1].replace("\r", "").replace(" ", "").replace("###", "")));
					}
					
					if(newcontenido.split("\n")[i].contains("Moneda")) {
					modelo.setMoneda(1);
					}
					
					if(newcontenido.split("\n")[i].contains("Nombre") && newcontenido.split("\n")[i].contains(ConstantsValue.AGENTE)) {
						
						modelo.setAgente((newcontenido.split("\n")[i].split(ConstantsValue.AGENTE)[1].split("###")[newcontenido.split("\n")[i].split(ConstantsValue.AGENTE)[1].split("###").length -2]).trim().replace("###", "").replace("  ", ""));
					}
				
					if(newcontenido.split("\n")[i].contains("Número") && newcontenido.split("\n")[i].contains(ConstantsValue.AGENTE)) {
						modelo.setCveAgente((newcontenido.split("\n")[i].split(ConstantsValue.AGENTE)[1].split("No.")[0]).trim().replace("###", "").replace("  ", ""));
					}
					if(newcontenido.split("\n")[i].contains("Subramo") && newcontenido.split("\n")[i].contains("pago")) {
						modelo.setFormaPago(fn.formaPago(newcontenido.split("\n")[i+1].split("Folio:")[1].replace("###", "").replace("\r", "").trim()));
					}
					if(newcontenido.split("\n")[i].contains("Uso:") && newcontenido.split("\n")[i].contains("pago")) {
						modelo.setFormaPago(fn.formaPago(newcontenido.split("\n")[i+1].split("Régimen:")[1].replace("Vivienda Sola", "").replace("###", "").replace("\r", "").trim()));
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
			

			
			//Proceso para ubicaciones
			inicio = contenido.indexOf("Descripción de la ");
			fin = contenido.indexOf("Paquete contratado:");
	
			if(inicio > 0 && fin > 0 && inicio < fin) {
				List<EstructuraUbicacionesModel> ubicaciones = new ArrayList<>();
				EstructuraUbicacionesModel ubicacion = new EstructuraUbicacionesModel();
				newcontenido = contenido.substring(inicio,fin);				
				for (int i = 0; i < newcontenido.split("\n").length; i++) {	
				if(contenido.contains("Hogar Integral") && newcontenido.split("\n")[i].contains("Uso:") && newcontenido.split("\n")[i].contains("Forma")) {										
						 ubicacion.setGiro(newcontenido.split("\n")[i].split("Uso:")[1].split("Forma")[0].replace("###", "").trim());
						
				   }
					
					if(newcontenido.split("\n")[i].contains("Muros:")) {
						ubicacion.setMuros(fn.material(newcontenido.split("\n")[i]));
					}
					if(newcontenido.split("\n")[i].contains("Niveles:")) {
						ubicacion.setNiveles(fn.castInteger(newcontenido.split("\n")[i].split("Niveles:")[1].replace("\r", "").replace("###","").trim()).intValue());
					}					
				}
				if(contenido.contains("Hogar Integral")) {
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
			
			return modelo;
		} catch (Exception ex) {
			modelo.setError(
					AxaDiversosModel.this.getClass().getTypeName() + " | " + ex.getMessage() + " | " + ex.getCause());
			return modelo;
		}
		
	}

}
