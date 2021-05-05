package com.copsis.models.metlife;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraAseguradosModel;
import com.copsis.models.EstructuraCoberturasModel;
import com.copsis.models.EstructuraJsonModel;
import com.copsis.models.inbursa.InbursaSaludModel;

public class MetlifeSaludModel {

	// Clases
	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
	// Varaibles
	private String contenido = "";
	private String newcontenido = "";
	private String recibosText = "";
	private String resultado = "";
	private int inicio = 0;
	private int fin = 0;
	private int donde = 0;
	private BigDecimal restoPrimaTotal = BigDecimal.ZERO;
	private BigDecimal restoDerecho = BigDecimal.ZERO;
	private BigDecimal restoIva = BigDecimal.ZERO;
	private BigDecimal restoRecargo = BigDecimal.ZERO;
	private BigDecimal restoPrimaNeta = BigDecimal.ZERO;
	private BigDecimal restoAjusteUno = BigDecimal.ZERO;
	private BigDecimal restoAjusteDos = BigDecimal.ZERO;
	private BigDecimal restoCargoExtra = BigDecimal.ZERO;
	
	
	public MetlifeSaludModel(String contenido) {
		this.contenido = contenido;	
	}

	
	public EstructuraJsonModel procesar() {
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
				.replace("HIJO", "###HIJO###")
				;
		         
		try {
			  // tipo
            modelo.setTipo(3);
            // cia
            modelo.setCia(23);
            // poliza
//            System.out.println("----------->" +contenido);
            
            inicio = contenido.indexOf("Nombre y Domicilio");
            fin = contenido.indexOf("ASEGURADOS DE LA POLIZA");
            
            if(inicio > 0 && fin > 0 && inicio < fin) {
            	newcontenido = contenido.substring(inicio,  fin).replace("\r", "").replace("@@@", "").trim();
            	for (int i = 0; i < newcontenido.split("\n").length; i++) {            		
            			if( newcontenido.split("\n")[i].contains("Contratante") && newcontenido.split("\n")[i].contains("Póliza")) {
                			modelo.setPoliza(newcontenido.split("\n")[i+2]);
                			modelo.setCteNombre(newcontenido.split("\n")[i+3].replace("SR.", "").trim());
                		}
            			if( newcontenido.split("\n")[i].contains("Sucursal")){
            				resultado =newcontenido.split("\n")[i+1].split("###")[0] 
            						+" "+ newcontenido.split("\n")[i+2].split("###")[0] 
            					    +" " + newcontenido.split("\n")[i+3].split("C.P.")[0];
            				modelo.setCteDireccion(resultado.trim());
            			}
            			
            			if( newcontenido.split("\n")[i].contains("C.P.")){
            				modelo.setCp(newcontenido.split("\n")[i].split("C.P.")[1]);
            			}
            			
            			if( newcontenido.split("\n")[i].contains("Día") &&  newcontenido.split("\n")[i].contains("Mes")){            				
            				if(newcontenido.split("\n")[i+1].split("###").length ==  6) {
            					String A = newcontenido.split("\n")[i+1].split("###")[2] +"-"+newcontenido.split("\n")[i+1].split("###")[1] +"-"+newcontenido.split("\n")[i+1].split("###")[0];
            					String B = newcontenido.split("\n")[i+1].split("###")[5] +"-"+newcontenido.split("\n")[i+1].split("###")[4] +"-"+newcontenido.split("\n")[i+1].split("###")[3];
            					modelo.setVigenciaDe(A);
            					modelo.setVigenciaA(B);            					
							}
						}
					}
				}
            
            /*primas*/
            inicio = contenido.indexOf("Forma de Pago");
            fin = contenido.indexOf("MetLife México, S.A. pagará los beneficios convenidos");

            if (inicio > 0 & fin > 0 & inicio < fin) {
            	newcontenido = contenido.substring(inicio, fin).replace("@@@", "");
                for (int i = 0; i < newcontenido.split("\n").length; i++) {
                    if (newcontenido.split("\n")[i].contains("Agente")) {                    	
                        modelo.setFormaPago(fn.formaPago(newcontenido.split("\n")[i + 1].split("###")[0].trim()));
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
            if (inicio > 0 & fin > 0 & inicio < fin) {
            	newcontenido = contenido.substring(inicio, fin).replace("@@@", "").replace(":", "").trim();
                modelo.setPlan(newcontenido.split("PLAN")[1].trim());

            }
            if(modelo.getVigenciaDe().length() >0) {
            	modelo.setFechaEmision(modelo.getVigenciaDe());
            }
            
            inicio = contenido.indexOf("ASEGURADOS DE LA POLIZA");
            fin = contenido.indexOf("COBERTURAS");

            if(inicio > 0 && fin > 0 && inicio < fin) {
            	List<EstructuraAseguradosModel> asegurados = new ArrayList<>();
            	newcontenido = contenido.substring(inicio,  fin).replace("\r", "").replace("@@@", "").trim();
            	for (int i = 0; i < newcontenido.split("\n").length; i++) {          
            		EstructuraAseguradosModel asegurado = new EstructuraAseguradosModel();
            		if(newcontenido.split("\n")[i].split("-").length >  3 && newcontenido.split("\n")[i].split("-").length < 6) {
            			asegurado.setNombre(newcontenido.split("\n")[i].split("###")[0]);
            			asegurado.setParentesco(fn.parentesco( newcontenido.split("\n")[i].split("###")[1]));
            			asegurado.setSexo(fn.sexo( newcontenido.split("\n")[i].split("###")[3]) ? 1:0);
            			String x = newcontenido.split("\n")[i].split("###")[newcontenido.split("\n")[i].split("###").length-1].trim().replace(" ", "###");
            			asegurado.setNacimiento( fn.formatDate_MonthCadena(x.split("###")[0]));
            			asegurado.setAntiguedad( fn.formatDate_MonthCadena(x.split("###")[1]));
            			asegurados.add(asegurado);
            		}
            	}
            	modelo.setAsegurados(asegurados);
            }

            inicio = contenido.indexOf("COBERTURAS");
            fin = contenido.indexOf("PLAN");
        
            if(inicio > 0 && fin > 0 && inicio < fin) {
            	List<EstructuraCoberturasModel> coberturas = new ArrayList<>();
            	newcontenido = contenido.substring(inicio,  fin).replace("\r", "").replace("@@@", "").replaceAll("-", "").replace("M.N.", "M.N.###").trim();
            	for (int i = 0; i < newcontenido.split("\n").length; i++) { 
            		EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();
            		if(newcontenido.split("\n")[i].contains("COBERTURAS") || newcontenido.split("\n")[i].contains("Nombre")) {            			
            		}else {
            			if(newcontenido.split("\n")[i].length() > 4) {
            				cobertura.setNombre(newcontenido.split("\n")[i].split("###")[0]);
            				cobertura.setSa(newcontenido.split("\n")[i].split("###")[1]);
            				if(newcontenido.split("\n")[i].split("###").length ==  4) {
            					cobertura.setDeducible(newcontenido.split("\n")[i].split("###")[2]);
            					cobertura.setCoaseguro(newcontenido.split("\n")[i].split("###")[3]);
            				}
            				coberturas.add(cobertura);
            			
            			}
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
