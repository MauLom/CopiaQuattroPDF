package com.copsis.models.sisnova;

import java.util.ArrayList;
import java.util.List;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraAseguradosModel;
import com.copsis.models.EstructuraCoberturasModel;
import com.copsis.models.EstructuraJsonModel;
import com.copsis.models.atlas.AtlasSaludModel;

public class SisnovaSaludModel {
	
	// Clases
	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
	// Variables
	private String contenido = "";
	private String newcontenido = "";
	private String resultado = "";
	private boolean direccion = false;
	private int donde = 0;
	private int inicio = 0;
	private int fin = 0;

	
	public SisnovaSaludModel(String contenido) {
		this.contenido = contenido;
		
	}
	
	public EstructuraJsonModel procesar() {
		contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());
		contenido = contenido.replace("DATOS DE LOS ASEGURADOS", "Datos de los asegurados")
				.replace("PÓLIZA NO.", "PÓLIZA NO.").replace("FECHA DE EMISIÓN", "Fecha de emisión")
				.replace("NOMBRE Y DOMICILIO DEL CONTRATANTE", "Nombre y domicilio del contratante")
				.replace("DESCRIPCIÓN DE LA PÓLIZA", "Descripción de la póliza")
				.replace("VIGENCIA", "Vigencia").replace("PLAN", "Plan").replace("PRIMA", "Prima")
				.replace("MONEDA", "Moneda").replace("TOTAL", "Total").replace("NETA", "Neta")
				.replace("COBERTURAS AMPARADAS", "coberturas amparadas");
		

		try {
		    //mocia
            modelo.setCia(11);
            //cia
            modelo.setTipo(3);
            //

            
            //Datos Generales
            inicio = contenido.indexOf("PÓLIZA NO.");
            fin = contenido.indexOf("Datos de los asegurados");
            if (inicio > 0 && fin > 0 && inicio < fin) {
				newcontenido = contenido.substring(inicio, fin).replace("\r", "").replace("@", "")
						.trim().replaceAll(" +", " ").replaceAll("   ", " ").replaceAll("  ", " ").replaceAll("   ", " ");
				for (int i = 0; i < newcontenido.split("\n").length; i++) {

					if(newcontenido.split("\n")[i].contains("PÓLIZA NO.")) {
						modelo.setPoliza(newcontenido.split("\n")[i].split("PÓLIZA NO.")[1].trim().replace(" ", ""));
					}
					if(newcontenido.split("\n")[i].contains("Fecha de emisión")) {
						modelo.setFechaEmision(newcontenido.split("\n")[i].split("Fecha de emisión")[1].replace("###", "").trim().replace(" ", ""));
					}
					
					if(newcontenido.split("\n")[i].contains("Nombre") && newcontenido.split("\n")[i].contains("domicilio") && newcontenido.split("\n")[i].contains("RFC")) {
						modelo.setCteNombre(newcontenido.split("\n")[i+1].replace("###", ""));
						if(newcontenido.split("\n")[i+2].split("###").length == 2) {
							modelo.setRfc(newcontenido.split("\n")[i+2].split("###")[1].replace("###", "").trim().replace(" ", ""));	
						} if(newcontenido.split("\n")[i+4].split("###").length == 2) {
							modelo.setRfc(newcontenido.split("\n")[i+4].split("###")[1].replace("###", "").trim().replace(" ", ""));	
						}
						
					}
					if(newcontenido.split("\n")[i].contains("Calle:")) {
						resultado = newcontenido.split("\n")[i].split("Calle:")[1].split("###")[0];
					}
	                if(newcontenido.split("\n")[i].contains("Colonia:") && direccion == false) {
	                	resultado += " "+ newcontenido.split("\n")[i].split("Colonia:")[1];
	                	modelo.setCteDireccion(resultado);
	                	direccion = true;
					}else if(newcontenido.split("\n")[i].contains("domicilio") && direccion == false) {
	                		resultado =  newcontenido.split("\n")[i+2] +" " + newcontenido.split("\n")[i+3];
	                		modelo.setCteDireccion(resultado);
	               }	                  		               
				}
            }
    
            

         
            //ASEGURADOS           
            inicio = contenido.indexOf("Datos de los asegurados");
            fin = contenido.indexOf("Descripción de la póliza");
   
            if (inicio > 0 && fin > 0 && inicio < fin) {
            	List<EstructuraAseguradosModel> asegurados = new ArrayList<>();
				newcontenido = contenido.substring(inicio, fin).replace("\r", "").replace("@", "")
						.replaceAll("### ###", "###").replaceAll("  +", "").replace("  ", " ");
				for (int i = 0; i < newcontenido.split("\n").length; i++) {
					EstructuraAseguradosModel asegurado = new EstructuraAseguradosModel();
					if(newcontenido.split("\n")[i].split("-").length > 4) {					
						asegurado.setNombre(newcontenido.split("\n")[i].split("###")[1]);
						asegurado.setNacimiento(fn.formatDate_MonthCadena(newcontenido.split("\n")[i].split("###")[2].replace(" ", "").trim()));
						asegurado.setAntiguedad(fn.formatDate_MonthCadena(newcontenido.split("\n")[i].split("###")[3].trim()));
						asegurados.add(asegurado);
					}
				}
				modelo.setAsegurados(asegurados);
            }
            
           
            
            inicio = 0;
            fin = 0;
            //Primas           
            inicio = contenido.indexOf("Descripción de la póliza");
            fin = contenido.indexOf("Servicios Integrales de Salud");
	
          
            if (inicio > 0 && fin > 0 && inicio < fin) {
            	newcontenido = contenido.substring(inicio, fin).replace("\r", "").replaceAll("@@@", "").replaceAll("### ###", "###")
            		    .trim().replaceAll("\u00A0", " ")	.replaceAll(" +", "###").replaceAll("######", "###").replace("######", "###")
            			.replace("00:00", "").replace("hrs.", "").replace("#########", "###");
            	        //El caracter unicode
            	
            	for (int i = 0; i < newcontenido.split("\n").length; i++) {   
            		if(newcontenido.split("\n")[i].contains("Vigencia") && newcontenido.split("\n")[i].contains("Desde:") && newcontenido.split("\n")[i].contains("Hasta:")) {            		
            			modelo.setVigenciaDe(fn.formatDate_MonthCadena(newcontenido.split("\n")[i].split("Desde:")[1].split("Hasta:")[0].replace("###", "").replace(" ", "").trim()));
            			modelo.setVigenciaA(fn.formatDate_MonthCadena(newcontenido.split("\n")[i].split("Hasta:")[1].replace("###", "").replace(" ", "").trim()));
            		}
            		if(newcontenido.split("\n")[i].contains("Plan:") && newcontenido.split("\n")[i].contains("Asegurada:") ) {
            			modelo.setPlan(newcontenido.split("\n")[i].split("Plan:")[1].split("###")[1]);
            		} else if(newcontenido.split("\n")[i].contains("Plan:") ) {
            			modelo.setPlan(newcontenido.split("\n")[i].split("Plan:")[1].replace("###", " "));
            		} 
            		if(newcontenido.split("\n")[i].contains("Prima") && newcontenido.split("\n")[i].contains("Neta") ) {
            			if(newcontenido.split("\n")[i+2].split("###").length == 4) {
            				modelo.setPrimaneta(fn.castBigDecimal(fn.castDouble(newcontenido.split("\n")[i+2].split("###")[3])));
                			modelo.setRecargo(fn.castBigDecimal(0.00));
                			modelo.setDerecho(fn.castBigDecimal(fn.castDouble(newcontenido.split("\n")[i+4].split("###")[2])));
                			modelo.setIva(fn.castBigDecimal(fn.castDouble(newcontenido.split("\n")[i+4].split("###")[3])));
            			}
            			if(newcontenido.split("\n")[i+2].split("###").length == 9) {
            				modelo.setPrimaneta(fn.castBigDecimal(fn.castDouble(newcontenido.split("\n")[i+2].split("###")[4])));
            				modelo.setRecargo(fn.castBigDecimal(0.00));
            				modelo.setDerecho(fn.castBigDecimal(fn.castDouble(newcontenido.split("\n")[i+2].split("###")[6])));
            				modelo.setIva(fn.castBigDecimal(fn.castDouble(newcontenido.split("\n")[i+2].split("###")[7])));
            			}
            			
            		}
            		if(newcontenido.split("\n")[i].contains("Prima") && newcontenido.split("\n")[i].contains("Total") && newcontenido.split("\n")[i].contains("Moneda") ) {
            			if(newcontenido.split("\n")[i+1].split("###").length == 6) {
            				modelo.setMoneda( fn.moneda( newcontenido.split("\n")[i+1].split("###")[1]));
                			modelo.setFormaPago( fn.formaPago( newcontenido.split("\n")[i+1].split("###")[2]));
                			modelo.setPrimaTotal(fn.castBigDecimal(fn.castDouble(newcontenido.split("\n")[i+1].split("###")[3])));
            			}
            			if(newcontenido.split("\n")[i+1].split("###").length == 5) {
            				modelo.setMoneda( fn.moneda( newcontenido.split("\n")[i+1].split("###")[0]));
                			modelo.setFormaPago( fn.formaPago( newcontenido.split("\n")[i+1].split("###")[1]));
                			modelo.setPrimaTotal(fn.castBigDecimal(fn.castDouble(newcontenido.split("\n")[i+1].split("###")[2])));
            			}
            			
            		}
            	}            	
            }
            
    		List<EstructuraCoberturasModel> coberturas = new ArrayList<>();
            inicio = contenido.indexOf("coberturas amparadas");
            fin = contenido.indexOf("PROMOCIONES");
            if(fin == -1) {
            	   fin = contenido.lastIndexOf("Servicios Integrales");
            }

            if (inicio > 0 & fin > 0 & inicio < fin) {
                newcontenido = "";
                newcontenido = fn.elimina_spacios(contenido.substring(inicio, fin).replace("     ", "").replace("   ", "").replace("@@@", "")).replace("\r", "");

                for (String x : newcontenido.split("\n")) {
                    int sp = x.split("###").length;
                    if (x.contains("Plan")) {
                    } else {
                        if (sp == 2) {
                        	EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();
                            cobertura.setNombre(x.split("###")[0]);
                            cobertura.setDeducible(x.split("###")[1]);
                            coberturas.add(cobertura);
                        }
                    }
                }
            	modelo.setCoberturas(coberturas);
            }
			
			
			
			return modelo;
		} catch (Exception ex) {
			modelo.setError(
					SisnovaSaludModel.this.getClass().getTypeName() + " - catch:" + ex.getMessage() + " | " + ex.getCause());
			return modelo;
		}
	}
	

}
