package com.copsis.models.metlife;

import java.util.ArrayList;
import java.util.List;

import com.copsis.constants.ConstantsValue;
import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraAseguradosModel;
import com.copsis.models.EstructuraCoberturasModel;
import com.copsis.models.EstructuraJsonModel;

public class MetlifeSaludModel {

	// Clases
	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
	// Varaibles
	private String contenido = "";


	public MetlifeSaludModel(String contenido) {
		this.contenido = contenido;	
	}

	
	public EstructuraJsonModel procesar() {
		 String newcontenido = "";
		 String resultado = "";
		 int inicio = 0;
		 int fin = 0;

		
		contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());
		contenido = contenido.replace("FEM.", "###FEM.###").replace("TIT.", "###TIT###")
				.replace("CONY.", "###CONY.###").replace("MASC.", "###MASC.###")
				
				.replace("COBERTURA TERRITORIO NACIONAL", "COBERTURA TERRITORIO NACIONAL###")
				.replace("SIN-LIMITE ", "SIN-LIMITE ###")
				.replace("EMERGENCIA EN EL EXTRANJERO ", "EMERGENCIA EN EL EXTRANJERO###")
				.replace("METDENTAL", "METDENTAL###")
				.replace("INCLUIDA", "INCLUIDA###")
				.replace("ASISTENCIA EN VIAJES IND.", "ASISTENCIA EN VIAJES IND.###")
				.replace("ENFERMEDADES CATASTROFICAS EX", "ENFERMEDADES CATASTROFICAS EX###")
				.replace("REDUCCION POR ACCIDENTE ", "REDUCCION POR ACCIDENTE###")
				.replace("SEM.C-REC.", "SEM.")
				.replace("HIJO", "###HIJO###");
		         
		try {
			  // tipo
            modelo.setTipo(3);
            // cia
            modelo.setCia(23);
            // poliza

            
            inicio = contenido.indexOf("Nombre y Domicilio");
            fin = contenido.indexOf("ASEGURADOS DE LA POLIZA");
            
            if(inicio > -1 && fin > -1 && inicio < fin) {
            	newcontenido = contenido.substring(inicio,  fin).replace("\r", "").replace("@@@", "").trim();
            	for (int i = 0; i < newcontenido.split("\n").length; i++) { 
            			if( newcontenido.split("\n")[i].contains("Contratante") && newcontenido.split("\n")[i].contains("Póliza")) {
            				if(newcontenido.split("\n")[i+2].length() > 20) {
            					modelo.setPoliza(newcontenido.split("\n")[i+1].split("###")[1].trim());
                    			modelo.setCteNombre(newcontenido.split("\n")[i+1].split("###")[0].replace("SR.", "").trim());
            				}else {
            					modelo.setPoliza(newcontenido.split("\n")[i+2]);
                    			modelo.setCteNombre(newcontenido.split("\n")[i+3].replace("SR.", "").trim());
            				}
                			
                		}
            			if(modelo.getPoliza().contains("Sucursal")) {
            				modelo.setPoliza(newcontenido.split("\n")[i+1].split("###")[1].trim());
                			modelo.setCteNombre(newcontenido.split("\n")[i+1].split("###")[0].replace("SR.", "").trim());
            			}
            			if( newcontenido.split("\n")[i].contains("Sucursal")){
            				resultado =newcontenido.split("\n")[i+1].split("###")[0] 
            						+" "+ newcontenido.split("\n")[i+2].split("###")[0] 
            					    +" " + newcontenido.split("\n")[i+3].split("C.P.")[0];
            				modelo.setCteDireccion(resultado.replace("###Vigencia de la Póliza", "").replace("Desde###Hasta", "").trim());
            			}
            			
            			if( newcontenido.split("\n")[i].contains("C.P.")){

            				if(newcontenido.split("\n")[i].split("C.P.")[1].length() > 7) {
            					modelo.setCp(newcontenido.split("\n")[i].split("C.P.")[1].substring(0,5));
            				}else {
            					modelo.setCp(newcontenido.split("\n")[i].split("C.P.")[1]);
            				}
            			
            			}
            			
            			if( newcontenido.split("\n")[i].contains("Día") &&  newcontenido.split("\n")[i].contains("Mes") && newcontenido.split("\n")[i+1].split("###").length ==  6){            				            				
            					modelo.setVigenciaDe(newcontenido.split("\n")[i+1].split("###")[2] +"-"+newcontenido.split("\n")[i+1].split("###")[1] +"-"+newcontenido.split("\n")[i+1].split("###")[0]);
            					modelo.setVigenciaA(newcontenido.split("\n")[i+1].split("###")[5] +"-"+newcontenido.split("\n")[i+1].split("###")[4] +"-"+newcontenido.split("\n")[i+1].split("###")[3]);            												
						}
					}
				}
            

            inicio = contenido.indexOf("Forma de Pago");
            fin = contenido.indexOf("MetLife México, S.A. pagará los beneficios convenidos");
           

            if (inicio > -1  && fin > -1 && inicio < fin) {            	
            	newcontenido = contenido.substring(inicio, fin).replace("@@@", "").replace("MEN.S", "Mensual").replace("SEM.S-REC.", "Semestral").replace("TRIM.S-REC", "Trimestral");
            	modelo.setFormaPago(fn.formaPagoSring(newcontenido));
                for (int i = 0; i < newcontenido.split("\n").length; i++) {
                    if (newcontenido.split("\n")[i].contains("Agente")) { 
                    	if(modelo.getFormaPago() == 0) {
                    		  modelo.setFormaPago(fn.formaPagoSring(fn.cleanString(newcontenido.split("\n")[i + 1].split("###")[0].trim())));
                    	}
                      
                        modelo.setCveAgente(newcontenido.split("\n")[i + 1].split("###")[1].trim());
                        modelo.setMoneda(1);
                    }                    
                    if (newcontenido.split("\n")[i].contains("Prima")) {
						if (newcontenido.split("\n")[i + 1].split("###").length == 5 ) {
							modelo.setPrimaneta(fn.castBigDecimal(fn.castDouble(newcontenido.split("\n")[i + 1].split("###")[0])));
							modelo.setIva(fn.castBigDecimal(fn.castDouble(  newcontenido.split("\n")[i + 1].split("###")[3])));
							modelo.setPrimaTotal(fn.castBigDecimal(fn.castDouble( newcontenido.split("\n")[i + 1].split("###")[4])));
							modelo.setRecargo(fn.castBigDecimal(fn.castDouble( newcontenido.split("\n")[i + 2].split("###")[0])));
                            modelo.setDerecho(fn.castBigDecimal(fn.castDouble( newcontenido.split("\n")[i + 2].split("###")[1])));
						}
                       else  if (newcontenido.split("\n")[i + 2].split("###").length == 3) {                     
               		    	modelo.setPrimaneta(fn.castBigDecimal(fn.castDouble(newcontenido.split("\n")[i + 2].split("###")[0])));
                            modelo.setIva(fn.castBigDecimal(fn.castDouble( newcontenido.split("\n")[i + 2].split("###")[1])));
                            modelo.setPrimaTotal(fn.castBigDecimal(fn.castDouble( newcontenido.split("\n")[i + 2].split("###")[2])));
                            modelo.setRecargo(fn.castBigDecimal(fn.castDouble( newcontenido.split("\n")[i + 3].split("###")[0])));
                            modelo.setDerecho(fn.castBigDecimal(fn.castDouble( newcontenido.split("\n")[i + 3].split("###")[1])));
                        } else {                                      
                            modelo.setPrimaneta(fn.castBigDecimal(fn.castDouble( newcontenido.split("\n")[i + 2].split("###")[0])));
                            modelo.setRecargo(fn.castBigDecimal(fn.castDouble( newcontenido.split("\n")[i + 2].split("###")[1])));
                            modelo.setDerecho(fn.castBigDecimal(fn.castDouble( newcontenido.split("\n")[i + 2].split("###")[2])));
                            modelo.setIva(fn.castBigDecimal(fn.castDouble(  newcontenido.split("\n")[i + 2].split("###")[3])));
                            modelo.setPrimaTotal(fn.castBigDecimal(fn.castDouble( newcontenido.split("\n")[i + 2].split("###")[4])));
                        }
                    }
                }
            }

            /*plan**/
            inicio = contenido.indexOf("PLAN");
            fin = contenido.indexOf("TIPO CONDUCTO");
            if(fin == -1) {
            	fin = contenido.lastIndexOf("MetLife México");
            }
            
            if (inicio > -1 && fin > -1 && inicio < fin) {
            	newcontenido = contenido.substring(inicio, fin).split("\n")[0].replace("@@@", "").replace(":", "").trim();  
   
                modelo.setPlan(newcontenido.trim());
            }
            if(modelo.getVigenciaDe().length() >0) {
            	modelo.setFechaEmision(modelo.getVigenciaDe());
            }            
            inicio = contenido.indexOf("ASEGURADOS DE LA POLIZA");
            fin = contenido.indexOf(ConstantsValue.COBERTURAS.toUpperCase());

            if(inicio > -1 && fin > -1 && inicio < fin) {
            	List<EstructuraAseguradosModel> asegurados = new ArrayList<>();
            	newcontenido = contenido.substring(inicio,  fin).replace("\r", "").replace("@@@", "").replace("ASC.", "###PADRE###").trim();
            	for (int i = 0; i < newcontenido.split("\n").length; i++) {          
            		EstructuraAseguradosModel asegurado = new EstructuraAseguradosModel();
            		if(newcontenido.split("\n")[i].split("-").length >  3 && newcontenido.split("\n")[i].split("-").length < 6) {            		
            			asegurado.setNombre(newcontenido.split("\n")[i].split("###")[0].replace("00", "").replace("01", "").trim());
            			asegurado.setParentesco(fn.parentesco( newcontenido.split("\n")[i].split("###")[1]));            			
            			asegurado.setSexo(fn.sexo( newcontenido.split("\n")[i].split("###")[3].trim()) ? 1 : 0);
            			String x = newcontenido.split("\n")[i].split("###")[newcontenido.split("\n")[i].split("###").length-1].trim().replace(" ", "###");
            			asegurado.setNacimiento( fn.formatDateMonthCadena(x.split("###")[0]));
            			asegurado.setAntiguedad( fn.formatDateMonthCadena(x.split("###")[1]));
            			asegurados.add(asegurado);
            		}
            	}
            	modelo.setAsegurados(asegurados);
            }

            inicio = contenido.indexOf(ConstantsValue.COBERTURAS.toUpperCase());
            fin = contenido.indexOf("PLAN");
			if(fin <= inicio){
				fin = contenido.indexOf("MetLife México S.A");
			}
		
            if(inicio > -1 && fin > -1 && inicio < fin) {
            	List<EstructuraCoberturasModel> coberturas = new ArrayList<>();
            	newcontenido = contenido.substring(inicio,  fin).replace("\r", "").replace("@@@", "").replace("-", "").replace("M.N.", "M.N.###").trim();
            	for (int i = 0; i < newcontenido.split("\n").length; i++) { 
            		EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();
            		if((!newcontenido.split("\n")[i].contains(ConstantsValue.COBERTURAS.toUpperCase()) || !newcontenido.split("\n")[i].contains("Nombre")) && ( newcontenido.split("\n")[i].length() > 4 && newcontenido.split("\n")[i].split("###").length > 1)) {            			            		
            				cobertura.setNombre(newcontenido.split("\n")[i].split("###")[0]);
            				cobertura.setSa(newcontenido.split("\n")[i].split("###")[1]);
            				if(newcontenido.split("\n")[i].split("###").length ==  4) {
            					cobertura.setDeducible(newcontenido.split("\n")[i].split("###")[2]);
            					cobertura.setCoaseguro(newcontenido.split("\n")[i].split("###")[3]);
            				}
            				coberturas.add(cobertura);            			            			
                	}            		
            	}
            	modelo.setCoberturas(coberturas);
            }
			return modelo;
		} catch (Exception ex) {	
			modelo.setError(
					MetlifeSaludModel.this.getClass().getTypeName() + " - catch:" + ex.getMessage() + " | " + ex.getCause());
			return modelo;
		}
		
	}

}
