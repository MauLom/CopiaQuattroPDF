package com.copsis.models.primero;

import java.util.ArrayList;
import java.util.List;

import com.copsis.constants.ConstantsValue;
import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraCoberturasModel;
import com.copsis.models.EstructuraJsonModel;
import com.copsis.models.EstructuraUbicacionesModel;

public class PrimeroDiversosModel {
	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
	private String contenido = "";


	public PrimeroDiversosModel(String contenido) {
		this.contenido = contenido;
	}
	
	public EstructuraJsonModel procesar() {
		contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());
		 String newcontenido = "";
		 int inicio;
		 int fin;
		
		try {
			modelo.setCia(49);
			modelo.setTipo(7);

			inicio = contenido.indexOf("PÓLIZA DE SEGURO PARA DAÑOS");
			fin = contenido.indexOf(ConstantsValue.COBERTURAS);
    
			   if (inicio > 0 && fin > 0 && inicio < fin) {
	                newcontenido = contenido.substring(inicio, fin).replace("@@@", "").replace("\r", "").replace("12:00", "").replace("12 Hrs", "");
	                for (int i = 0; i < newcontenido.split("\n").length; i++) {
	                	if(newcontenido.split("\n")[i].contains(ConstantsValue.SEGURO_PARA_DANOS)) {	 
	                		if(newcontenido.split("\n")[i].contains("###")) {
	                			modelo.setPoliza(newcontenido.split("\n")[i].split(ConstantsValue.SEGURO_PARA_DANOS)[1].split("###")[1].replace("-", "").trim());
		                		modelo.setPolizaGuion(newcontenido.split("\n")[i].split(ConstantsValue.SEGURO_PARA_DANOS)[1].split("###")[1]);
	                		}else {
	                			modelo.setPoliza(newcontenido.split("\n")[i+1].replace("-", "").trim());
		                		modelo.setPolizaGuion(newcontenido.split("\n")[i+1].trim());	                			
	                		}	                		
	                	}
	                	if(modelo.getPoliza().length() == 0 && newcontenido.split("\n")[i].contains("SEGURO PARA DAÑOS")) {	                			   
	                			modelo.setPoliza(newcontenido.split("\n")[i+1].replace("-", "").trim());
		                		modelo.setPolizaGuion(newcontenido.split("\n")[i+1].trim());	                		
	                	}
	                	
	                	if(newcontenido.split("\n")[i].contains("Fecha de Emisión")) {
	                		modelo.setFechaEmision(fn.formatDateMonthCadena(newcontenido.split("\n")[i+1].split(" ")[0]));
	                	}
	                	if(newcontenido.split("\n")[i].contains("Asegurado:")) {
	                		modelo.setCteNombre(newcontenido.split("\n")[i].split("Asegurado:")[1].replace(":", "").replace("###", ""));
	                	}
	                	if(newcontenido.split("\n")[i].contains("Domicilio:")) {
	                		modelo.setCteDireccion(newcontenido.split("\n")[i].split("Domicilio:")[1].replace(":", "").replace("###", ""));
	                	}
	                	if (newcontenido.split("\n")[i].contains("Cp.") && newcontenido.split("\n")[i].contains("Domicilio:")) {
	                		
							modelo.setCp(newcontenido.split("\n")[i].split("Cp.")[1].split(",")[0].trim());
						}
	                	if(newcontenido.split("\n")[i].contains("RFC:") || newcontenido.split("\n")[i].contains("Teléfono:")) {
	                		modelo.setRfc(newcontenido.split("\n")[i].split("RFC:")[1].split("Teléfono:")[0].replace("###", ""));
	                	}
	                	if(newcontenido.split("\n")[i].contains("Vigencia")  && newcontenido.split("\n")[i+1].split("-").length > 3) {	                	
	                			modelo.setPlan(newcontenido.split("\n")[i+1].split("###")[0]);
	                			modelo.setVigenciaDe(fn.formatDateMonthCadena(newcontenido.split("\n")[i+1].split("###")[2].trim()));
	                			modelo.setVigenciaA(fn.formatDateMonthCadena(newcontenido.split("\n")[i+1].split("###")[4].trim()));	                		
	                	}
	                }
			   }
			   
			   
	            inicio = contenido.indexOf("Prima Neta");
	            fin = contenido.indexOf("EN CASO DE SINIESTRO");
	            if (inicio > 0 && fin > 0 && inicio < fin) {
	                newcontenido = contenido.substring(inicio, fin).replace("@@@", "").replace("\r", "");
	                for (int i = 0; i < newcontenido.split("\n").length; i++) {    
	                    if (newcontenido.split("\n")[i].contains("Prima Neta###Financiamiento###Gastos de###Subtotal###IVA###Total")) {
	                        modelo.setPrimaneta(fn.castBigDecimal( fn.preparaPrimas(newcontenido.split("\n")[i + 1].split("###")[0])));
	                        modelo.setRecargo(fn.castBigDecimal(fn.preparaPrimas(newcontenido.split("\n")[i + 1].split("###")[1])));
	                        modelo.setDerecho(fn.castBigDecimal(fn.preparaPrimas(newcontenido.split("\n")[i + 1].split("###")[2])));
	                        modelo.setIva(fn.castBigDecimal(fn.preparaPrimas(newcontenido.split("\n")[i + 1].split("###")[4])));
	                        modelo.setPrimaTotal(fn.castBigDecimal(fn.preparaPrimas(newcontenido.split("\n")[i + 1].split("###")[5])));
	                    }
	                    if (newcontenido.split("\n")[i].contains("Moneda")) {
	                        modelo.setMoneda(fn.moneda(newcontenido.split("\n")[i + 1].split("###")[0]));
	                        modelo.setFormaPago(fn.formaPago(newcontenido.split("\n")[i + 1].split("###")[1]));
	                    }

	                }
	            }
	            obtenerAgente();
	            obtenerTextoDiversos();
			
	            inicio = contenido.indexOf("Datos del Riesgo");
				fin = contenido.indexOf("Coberturas");
				List<EstructuraUbicacionesModel> ubicaciones = new ArrayList<>();
				  if (inicio > -1 && fin > -1 && inicio < fin) {					 
		                newcontenido = contenido.substring(inicio, fin).replace("@@@", "").replace("\r", "");
		                EstructuraUbicacionesModel ubicacion = new EstructuraUbicacionesModel();
		                for (int i = 0; i < newcontenido.split("\n").length; i++) {

		                	if(newcontenido.split("\n")[i].contains("Giro") && newcontenido.split("\n")[i].contains("Ubicación del Riesgo")) {
		                		ubicacion.setGiro(newcontenido.split("\n")[i].split("Giro")[1].replace("###", ""));
		                	}
		                	if(newcontenido.split("\n")[i].contains("Muro:")) {
		                		ubicacion.setMuros(fn.material(newcontenido.split("\n")[i].split("Muro:")[1].replace(".", "###").split("###")[0].toLowerCase()));
		                	}
		                	if(newcontenido.split("\n")[i].contains("Techo:")) {
		                		ubicacion.setTechos(fn.material(newcontenido.split("\n")[i].split("Techo:")[1].replace(".", "###").split("###")[0].toLowerCase()));
		                	}
		                	if(newcontenido.split("\n")[i].contains("Cp.") && newcontenido.split("\n")[i].split("Cp")[1].length() >1) {
		                		ubicacion.setCp(newcontenido.split("\n")[i].split("Cp.")[1].replace(",", "###").split("###")[0].trim());
		                	}
		                   	if(newcontenido.split("\n")[i].contains("Niveles:")) {
		                		ubicacion.setNiveles(Integer.parseInt( newcontenido.split("\n")[i].split("Niveles:")[1].replace(".", "###").split("###")[0].strip()));
		                	}
		                   	if(newcontenido.split("\n")[i].contains("Ubicación del Riesgo.")) {
		                   	  ubicacion.setCalle( newcontenido.split("\n")[i+1]);
		                   	}		     		             
		                }
		                ubicaciones.add(ubicacion);
		                modelo.setUbicaciones(ubicaciones);
		    
				  }
				  
				  inicio = contenido.indexOf("Coberturas");
				  fin = contenido.indexOf("Prima Neta");

				  if (inicio > -1 && fin > -1 && inicio < fin) {	
					  String secciont="";
					  List<EstructuraCoberturasModel> coberturas = new ArrayList<>();
					   newcontenido = contenido.substring(inicio, fin).replace("@@@", "").replace("\r", "");
		                for (int i = 0; i < newcontenido.split("\n").length; i++) {    
		                	EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();
		                	
		                	if(!newcontenido.split("\n")[i].contains("Coberturas Amparadas") || !newcontenido.split("\n")[i].contains("Deducible")
		                			|| !newcontenido.split("\n")[i].contains("Sección") || !newcontenido.split("\n")[i].contains("Coberturas")) {
		             
		                		
		                		int sp = newcontenido.split("\n")[i].split("###").length;
		                		if(sp == 4) {
		                			secciont =newcontenido.split("\n")[i].split("###")[0];	
		                		}
		                			                				        
		                		switch (sp) {
								case  4:
									cobertura.setSeccion(secciont);
									cobertura.setNombre( newcontenido.split("\n")[i].split("###")[1]);
									cobertura.setSa( newcontenido.split("\n")[i].split("###")[2]);
									cobertura.setDeducible(newcontenido.split("\n")[i].split("###")[3]);
									coberturas.add(cobertura);
									break;

								case 3:
										cobertura.setSeccion(secciont);
									cobertura.setNombre( newcontenido.split("\n")[i].split("###")[0]);
									cobertura.setSa( newcontenido.split("\n")[i].split("###")[1]);
									cobertura.setDeducible(newcontenido.split("\n")[i].split("###")[2]);
									coberturas.add(cobertura);
									break;
									default:
										break;
								}
//		                		
		                	}
		                	
		                }
		                modelo.setCoberturas(coberturas);						
				  
				  }
	            
			
			return modelo;
		} catch (Exception e) {
		  return modelo;
		}
	}
	
	private void obtenerAgente() {
        int inicio = contenido.indexOf("AVISO DE PRIVACIDAD");
        int fin = contenido.indexOf("Nombre del Agente");
        String newContenido = "";
        if (inicio > 0 && fin > 0 && inicio < fin) {
        	newContenido = contenido.substring(inicio,fin).replace("@@@", "");
        	if(newContenido.split("\n").length>1) {
        		modelo.setAgente(newContenido.split("\n")[newContenido.split("\n").length-1].replace("\r", ""));
        	}
        }
	}
	
	private void obtenerTextoDiversos() {
		int inicio = contenido.indexOf("Datos del Embarque");
		int fin = contenido.indexOf("Coberturas Amparadas");
		String[] arrContenido = contenido.substring(inicio,fin).replace("@@@", "").replace("\r","").split("\n");
		StringBuilder texto = new StringBuilder();
		String textoEnOtroRenglon = "";

		for(int i=0;i<arrContenido.length;i++) {
				switch (arrContenido[i].split("###").length) {
				case 1:
					if(!arrContenido[i].isBlank()) {
						texto.append(arrContenido[i]);
					}
					break;
				case 2:
					texto.append(arrContenido[i].split("###")[0]);
					texto.append(":");
					texto.append(arrContenido[i].split("###")[1]);
					break;
				case 3:
					texto.append(arrContenido[i].split("###")[0]);
					texto.append(":");
					texto.append(arrContenido[i].split("###")[1]);
					if(arrContenido[i].split("###")[2].contains("Descripción") && arrContenido[i+1].contains("###")) {
						texto.append(",Descripción: ");
						textoEnOtroRenglon = arrContenido[i+1].split("###")[arrContenido[i+1].split("###").length-1];
						texto.append(textoEnOtroRenglon);
					}
					break;
				
				case 4:
					if(arrContenido[i].split("###")[0].contains("Trayecto")) {
						texto.append("Trayecto: Origen:");
						texto.append(arrContenido[i].split("###")[2]);
						texto.append(",Destino:");
						textoEnOtroRenglon = arrContenido[i+1].split("###")[1].trim();
						texto.append(textoEnOtroRenglon);
						arrContenido[i+1] = arrContenido[i+1].replace("Destin###"+textoEnOtroRenglon,"");
					}else {
						texto.append(arrContenido[i].split("###")[0]);
						texto.append(":");
						texto.append(arrContenido[i].split("###")[1].trim());
						texto.append(",");
						texto.append(arrContenido[i].split("###")[2]);
						texto.append(":");
						texto.append(arrContenido[i].split("###")[3].trim());
					}
					
					break;

				default:
					break;
				}
				if(!arrContenido[i].isBlank()) {
				    texto.append(",");
				}
	
		}
		modelo.setTextoDiversos(texto.toString().substring(0,texto.length()-1));
	}
}
