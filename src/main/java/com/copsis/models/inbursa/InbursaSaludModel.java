package com.copsis.models.inbursa;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraAseguradosModel;
import com.copsis.models.EstructuraJsonModel;

public class InbursaSaludModel {
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
	
	public InbursaSaludModel(String contenido) {
		this.contenido = contenido;	
	}
	
	public EstructuraJsonModel procesar() {
		
		contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());
		try {
			 // tipo
	        modelo.setTipo(3);

	        // cia
	        modelo.setCia(35);
//	    	System.out.println(contenido);
	        
	         //Datos del Contratante
            inicio = contenido.indexOf("PÓLIZA DE SEGUROS");
            fin = contenido.indexOf("En caso de siniestro");
            if(inicio > 0 &&  fin >  0 && inicio < fin) {
            	newcontenido = contenido.substring(inicio, fin).replace("\r","").replace("@", "").replace("las 12:00 hrs. del", "");        
            	for (int i = 0; i < newcontenido.split("\n").length; i++) {   
            		if(newcontenido.split("\n")[i].contains("Cliente") && newcontenido.split("\n")[i].contains("PÓLIZA") && newcontenido.split("\n")[i].contains("CIS") ) {
            			modelo.setPoliza(newcontenido.split("\n")[i-1].split("###")[0]);
            		}
            		
            		if(newcontenido.split("\n")[i].contains("NOMBRE:") && newcontenido.split("\n")[i].contains("PRIMA")){
            			modelo.setCteNombre(newcontenido.split("\n")[i+1].split("###")[0]);            		
            			modelo.setPrimaneta(fn.castBigDecimal(fn.castDouble(newcontenido.split("\n")[i+1].split("###")[newcontenido.split("\n")[i+1].split("###").length -1])));
            		}
            		
            		if(newcontenido.split("\n")[i].contains("DIRECCIÓN:")) {
            			resultado =newcontenido.split("\n")[i+1].split("###")[0];            			
            			resultado += newcontenido.split("\n")[i+2].split("R.F.C:")[0];
            			resultado += " "+ newcontenido.split("\n")[i+3].split("###")[0];
            			modelo.setCteDireccion(resultado.replace("###", ""));
            		}
            		
            		
            		if(newcontenido.split("\n")[i].contains("R.F.C:")) {
            			modelo.setRfc(newcontenido.split("\n")[i+1].split("###")[newcontenido.split("\n")[i+1].split("###").length -2]);
            		}
            		
            		if(newcontenido.split("\n")[i].contains("FINANCIAMIENTO")) {
            			modelo.setCp(newcontenido.split("\n")[i+1].split("C.P.")[1].split("###")[0].trim());
            			modelo.setDerecho(fn.castBigDecimal(fn.castDouble(newcontenido.split("\n")[i+1].split("###")[newcontenido.split("\n")[i+1].split("###").length -1])));
            		}
            		if(newcontenido.split("\n")[i].contains("MONEDA:") && newcontenido.split("\n")[i].contains("GASTOS EXPEDICIÓN:")) {
            			modelo.setMoneda(fn.moneda( newcontenido.split("\n")[i+1].split("###")[0]));
            			modelo.setRecargo(fn.castBigDecimal(fn.castDouble(newcontenido.split("\n")[i+1].split("###")[newcontenido.split("\n")[i+1].split("###").length -1])));
            		}
            		if(newcontenido.split("\n")[i].contains("PAGO:") && newcontenido.split("\n")[i].contains("IVA")) {
            			modelo.setFormaPago(fn.formaPago( newcontenido.split("\n")[i+1].split("###")[0]));
            			modelo.setIva(fn.castBigDecimal(fn.castDouble(newcontenido.split("\n")[i+1].split("###")[newcontenido.split("\n")[i+1].split("###").length -1])));
            		}
            		
            		if(newcontenido.split("\n")[i].contains("DOCUMENTO:") && newcontenido.split("\n")[i].contains("PRIMA")) {
            			modelo.setPrimaTotal(fn.castBigDecimal(fn.castDouble(newcontenido.split("\n")[i+1].split("###")[newcontenido.split("\n")[i+1].split("###").length -1])));
            		}
	        		 if(newcontenido.split("\n")[i].contains("Desde") && newcontenido.split("\n")[i].contains("Hasta") && newcontenido.split("\n")[i].contains("-")) { 
	          			modelo.setVigenciaDe(fn.formatDate_MonthCadena(newcontenido.split("\n")[i].split("Desde")[1].split("Hasta")[0].replace("###", "").trim()));
	          			modelo.setVigenciaA(fn.formatDate_MonthCadena(newcontenido.split("\n")[i].split("Hasta")[1].split("###")[1].replace("###", "").trim()));
	          		 }
	        		 if(newcontenido.split("\n")[i].contains("PRODUCTO")){            			
	        			 modelo.setPlan(newcontenido.split("\n")[i+1].split("###")[0].replace("###", "").trim());
	        		 } 
            	}
            }
            
            
            inicio = contenido.indexOf("NOMBRE DEL AGENTE");
            
            if(inicio > 0) {
            	newcontenido = contenido.split("NOMBRE DEL AGENTE")[0];
            	if(newcontenido.length() > 200) {
            		newcontenido = newcontenido.substring(newcontenido.length()-200,newcontenido.length()).replace("@@@", "").replace("\r", "");
                    for (int j = 0; j < newcontenido.split("\n").length; j++) {    
   
                    	if(newcontenido.split("\n")[j].contains("CLAVE")) {                   
                    		modelo.setCveAgente(fn.extraerNumeros( newcontenido.split("\n")[j-2]));
                    		if(modelo.getCveAgente().length() > 0) {
                    			String A = newcontenido.split("\n")[j-1].replace(" ", "###").split("###")[0].trim();
                    			 if(A.contains("@")) {
                    				 A ="";
                    			 }
                    		  modelo.setAgente((newcontenido.split("\n")[j-2].split( modelo.getCveAgente() )[1] +""+ A).trim());	
                    		}                                        			
                    	}
					} 
            	}            	             
            }
            
            inicio = contenido.indexOf("ASEGURADOS");
            fin = contenido.indexOf("COBERTURAS");
    
            if(inicio > 0 &&  fin >  0 && inicio < fin) {
            	List<EstructuraAseguradosModel> asegurados = new ArrayList<>();
            	newcontenido = contenido.substring(inicio, fin).replace("\r","").replace("@", "");
            	EstructuraAseguradosModel asegurado = new EstructuraAseguradosModel();
            	for (int i = 0; i < newcontenido.split("\n").length; i++) {  
            		if(newcontenido.split("\n")[i].contains("Nombre:")) {
            			asegurado.setNombre(newcontenido.split("\n")[i].split("Nombre:")[1].split("###")[1]);            		
            		}
            		if(newcontenido.split("\n")[i].contains("nacimiento:")) {
            			asegurado.setNacimiento(fn.formatDate_MonthCadena( newcontenido.split("\n")[i].split("nacimiento:")[1].split("###")[1]));            			
            		}
            		if(newcontenido.split("\n")[i].contains("Género:") && newcontenido.split("\n")[i].contains("Fecha:")) {
            			asegurado.setSexo(fn.sexo( newcontenido.split("\n")[i].split("Género:")[1].split("###")[1]) ? 1 : 0);
            		}
            		
            		if(newcontenido.split("\n")[i].contains("Parentesco:") && newcontenido.split("\n")[i].contains("Tope")) {
            			asegurado.setParentesco(fn.parentesco( newcontenido.split("\n")[i].split("Parentesco:")[1].split("###")[1]));            		
            		}
            	}
            	asegurados.add(asegurado);
            	modelo.setAsegurados(asegurados);
            	
            	

            }
	        
	        
	        
	        return modelo;
		} catch (Exception ex) {
			modelo.setError(
					InbursaSaludModel.this.getClass().getTypeName() + " - catch:" + ex.getMessage() + " | " + ex.getCause());
			return modelo;
		}
        
		
	}
		

}
