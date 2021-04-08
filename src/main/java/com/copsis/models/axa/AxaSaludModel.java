package com.copsis.models.axa;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraAseguradosModel;
import com.copsis.models.EstructuraCoberturasModel;
import com.copsis.models.EstructuraJsonModel;

public class AxaSaludModel {

	// Clases
	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
	// Varaibles
	private String inicontenido = "";
	private String contenido = "";
	private String newcontenido = "";
	private String newcontenidoEx = "";
	private String recibosText = "";
	private String resultado = "";
	private int inicio = 0;
	private int fin = 0;
	private int donde = 0;
	private int index = 0;
    private int longAux = 0;
    private int longitudTexto = 0;
    private boolean segda_line_agente = false;
	private BigDecimal restoPrimaTotal = BigDecimal.ZERO;
	private BigDecimal restoDerecho = BigDecimal.ZERO;
	private BigDecimal restoIva = BigDecimal.ZERO;
	private BigDecimal restoRecargo = BigDecimal.ZERO;
	private BigDecimal restoPrimaNeta = BigDecimal.ZERO;
	private BigDecimal restoAjusteUno = BigDecimal.ZERO;
	private BigDecimal restoAjusteDos = BigDecimal.ZERO;
	private BigDecimal restoCargoExtra = BigDecimal.ZERO;
	
	public AxaSaludModel(String contenido) {
		this.contenido = contenido;
	}
	
	public EstructuraJsonModel procesar() {
		contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());
		try {
			modelo.setTipo(3);
            modelo.setCia(20);
            
            inicio = contenido.indexOf("Póliza");
            fin = contenido.indexOf("Solicitud");
            
            if(inicio > 0 & fin > 0 & inicio < fin) {
            	newcontenido = contenido.split("Póliza:")[1].split("Solicitud")[0].replace("\r\n", "").replace("###","");
                modelo.setPoliza(newcontenido);
            }
            
            inicio = contenido.indexOf("Contratante");
            fin = contenido.indexOf("Domicilio");
            if(inicio > 0 & fin > 0 & inicio < fin) {
            	newcontenido = contenido.substring(inicio,fin);
            	for (int i = 0; i < newcontenido.split("\n").length; i++) {					
					if(newcontenido.split("\n")[i].contains("Nombre:") & newcontenido.split("\n")[i].contains("RFC")) {
						modelo.setCteNombre(newcontenido.split("\n")[i].split("Nombre:")[1].split("RFC")[0].replace("\r", "").replace("###",""));
						modelo.setRfc(newcontenido.split("\n")[i].split("RFC")[1].replace("\r", "").replace("###",""));
					}
				}            	
            }
            
            
            inicio = contenido.indexOf("Datos###de###la###Póliza");
            fin = contenido.indexOf("Coberturas###Amparadas");
            if(inicio > 0 & fin > 0 & inicio < fin) {
            	newcontenido = contenido.substring(inicio,fin);
            	for (int i = 0; i < newcontenido.split("\n").length; i++) {	
            		System.out.println(newcontenido.split("\n")[i] +"");
            		if(newcontenido.split("\n")[i].contains("Plan") & newcontenido.split("\n")[i].contains("Póliza") & newcontenido.split("\n")[i].contains("Prima")) {
            			modelo.setPlan(newcontenido.split("\n")[i].split("Póliza:")[1].split("Prima")[0].replace("###", ""));
                        modelo.setPrimaneta((fn.castBigDecimal( fn.castDouble(newcontenido.split("\n")[i].split("Neta:")[1].replace("###", "").replace(",", "")))));            	
            		}
            		
            		
            		if(newcontenido.split("\n")[i].contains("Moneda:") & newcontenido.split("\n")[i].contains("Financiamiento") ) {
            			modelo.setMoneda(fn.moneda(newcontenido.split("\n")[i].split("Moneda:")[1].split("Financiamiento")[0].replace("###", "")));
            			  modelo.setRecargo((fn.castBigDecimal( fn.castDouble(newcontenido.split("\n")[i].split("Financiamiento:")[1].replace("###", "").replace(",", "")))));
            		}else if(newcontenido.split("\n")[i].contains("Moneda:")) {            			
            			modelo.setMoneda(fn.moneda(newcontenido.split("\n")[i].split("Moneda:")[1].replace("###", "").trim()));                		
            		}

            		if(newcontenido.split("\n")[i].contains("Vigencia") & newcontenido.split("\n")[i].contains("Expedición") ) {
            			if(newcontenido.split("\n")[i].split("Vigencia:")[1].split("Gastos")[0].replace("###","").split("-").length == 6) {
            				modelo.setVigenciaDe(fn.formatDate_MonthCadena( newcontenido.split("\n")[i].split("Vigencia:")[1].split("Gastos")[0].replace("###","").replace(" - ", "###").split("###")[0]));
            				modelo.setVigenciaA(fn.formatDate_MonthCadena(newcontenido.split("\n")[i].split("Vigencia:")[1].split("Gastos")[0].replace("###","").replace(" - ", "###").split("###")[1]));
            				 modelo.setDerecho((fn.castBigDecimal( fn.castDouble(newcontenido.split("\n")[i].split("Expedición:")[1].replace("###", "").replace(",", "")))));
            			}            			
            		}else if(newcontenido.split("\n")[i].contains("Vigencia") & newcontenido.split("\n")[i].contains("Financiamiento") ) {
            			String x =newcontenido.split("\n")[i].split("Vigencia:")[1].split("Financiamiento")[0].replace("###","").split("-")[2];
            			String x2 =newcontenido.split("\n")[i].split("Vigencia:")[1].split("Financiamiento")[0].replace("###","").split(x)[1];

            			modelo.setVigenciaDe(fn.formatDate_MonthCadena(newcontenido.split("\n")[i].split("Vigencia:")[1].split("Financiamiento")[0].replace("###","").split(x)[0] +"" +x));
            		    modelo.setVigenciaA(fn.formatDate_MonthCadena(x2.substring(1,x2.length())));
            		}
            		else if(newcontenido.split("\n")[i].contains("Vigencia")){
            			modelo.setVigenciaDe(fn.formatDate_MonthCadena( newcontenido.split("\n")[i].split("Vigencia:")[1].replace("###","").replace(" - ", "###").trim().split("###")[0]));
            			modelo.setVigenciaA(fn.formatDate_MonthCadena(newcontenido.split("\n")[i].split("Vigencia:")[1].replace("\r", "").replace("###", "").replace("- ", "###").split("###")[1]));
            		}
            		
            		
            		
            		if(newcontenido.split("\n")[i].contains("Pago") & newcontenido.split("\n")[i].contains("I.V.A:") ) {
            			modelo.setFormaPago(fn.formaPago(newcontenido.split("\n")[i].split("Primas:")[1].split("Prima")[0].replace("###", "")));
            			modelo.setIva((fn.castBigDecimal( fn.castDouble(newcontenido.split("\n")[i+1].split("I.V.A:")[1].replace("###", "").replace(",", "")))));
            		}else if(newcontenido.split("\n")[i].contains("Pago") & newcontenido.split("\n")[i].contains("Expedición") ) {
            			modelo.setFormaPago(fn.formaPago(newcontenido.split("\n")[i].split("Primas:")[1].split("Gastos")[0].replace("###", "")));
            			modelo.setIva((fn.castBigDecimal( fn.castDouble(newcontenido.split("\n")[i+2].split("I.V.A:")[1].replace("###", "").replace(",", "")))));
            		}
            		
            		
            		if(newcontenido.split("\n")[i].contains("Total:")){
            			modelo.setPrimaTotal((fn.castBigDecimal( fn.castDouble(newcontenido.split("\n")[i].split("Total:")[1].replace("###", "").replace(",", "")))));
            		}
            	}            	
            }
            
            
            
            
            
           
           //Proceso de Asegurados 
            inicio = contenido.indexOf("Datos###del###Asegurado");
            fin = contenido.indexOf("Datos###de###la###Póliza");
    		List<EstructuraAseguradosModel> asegurados = new ArrayList<>();
    		
            if(inicio > 0 & fin > 0 & inicio < fin) {
          		EstructuraAseguradosModel asegurado = new EstructuraAseguradosModel();
            	newcontenido = contenido.substring(inicio,fin);
            	for (int i = 0; i < newcontenido.split("\n").length; i++) {	
            		if(newcontenido.split("\n")[i].contains("Nombre:") & newcontenido.split("\n")[i].contains("Parentesco:")) {
            			newcontenidoEx = newcontenido.split("\n")[i].split("Nombre:")[1].split("Parentesco")[0];
                         if(newcontenido.contains(",")) {
                           newcontenidoEx =	( newcontenidoEx.split(",")[1] +" "+  newcontenidoEx.split(",")[0]).replace("###", "");
                           asegurado.setNombre(newcontenidoEx);
                         }else {
                        	 newcontenidoEx	= newcontenidoEx.replace("###", "");
                        	 asegurado.setNombre(newcontenidoEx);
                         }
                        asegurado.setParentesco(fn.parentesco(newcontenido.split("Parentesco:")[1]));                         
            		} 
            		if(newcontenido.split("\n")[i].contains("Nacimiento:") & newcontenido.split("\n")[i].contains("Edad:")) {
            			newcontenidoEx = newcontenido.split("\n")[i].split("Nacimiento:")[1].split("Edad:")[0].replace("###", "");
            			asegurado.setNacimiento(fn.formatDate_MonthCadena(newcontenidoEx));
            		}
            	}
            	asegurados.add(asegurado);
            	modelo.setAsegurados(asegurados);	            	
            }
            
 
			return modelo;
		} catch (Exception ex) {
			modelo.setError(
					AxaSaludModel.this.getClass().getTypeName() + " | " + ex.getMessage() + " | " + ex.getCause());
			return modelo;
		}
	}
	
    private String filter(String contenido, String caracteres, int quienes_no) { // FILTRA POR MEDIANTE UNA CONDICION CON ITERADOR
        String result = "";
        for (int i = 0; i < contenido.split(caracteres).length; i++) {
            if (i > quienes_no) {
                if (contenido.split(caracteres).length == i) {
                    result += contenido.split(caracteres)[i].trim();
                } else {
                    result += contenido.split(caracteres)[i].trim() + " ";
                }
            }
        }
        return result;
    }

}
