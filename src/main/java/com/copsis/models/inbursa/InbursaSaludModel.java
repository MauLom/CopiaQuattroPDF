package com.copsis.models.inbursa;

import java.util.ArrayList;
import java.util.List;

import com.copsis.constants.ConstantsValue;
import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraAseguradosModel;
import com.copsis.models.EstructuraJsonModel;

public class InbursaSaludModel {
	// Clases
		private DataToolsModel fn = new DataToolsModel();
		private EstructuraJsonModel modelo = new EstructuraJsonModel();
		// Varaibles
		private String contenido = "";
	
	public InbursaSaludModel(String contenido) {
		this.contenido = contenido;	
	}

	public EstructuraJsonModel procesar() {
	String newcontenido = "";
	StringBuilder resultado = new StringBuilder();
	int inicio = 0;
	int fin = 0;
	String[] arrNewContenido;
	String renglon = "";
	
	contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());
	try {
		 // tipo
        modelo.setTipo(3);

        // cia
        modelo.setCia(35);

        
         //Datos del Contratante
        inicio = contenido.indexOf("PÓLIZA DE SEGUROS");
        fin = contenido.indexOf("En caso de siniestro");
        if(inicio > 0 &&  fin >  0 && inicio < fin) {
        	newcontenido = contenido.substring(inicio, fin).replace("\r","").replace("@", "").replace("las 12:00 hrs. del", "");        
        	arrNewContenido = newcontenido.split("\n");
        	
        	for (int i = 0; i < arrNewContenido.length; i++) {  
        		renglon = arrNewContenido[i];
        		readPoliza(arrNewContenido, renglon, i);
        		leerNombreCteyPrima(renglon, arrNewContenido, i);
        		leerDireccion(arrNewContenido,renglon, resultado, i);
        		leerRFC(arrNewContenido, renglon, i);
        		leerDerechoyCP(arrNewContenido, renglon, i);
        		leerMonedaYRecargo(arrNewContenido, renglon, i);
        		leerFormaPagoIva(arrNewContenido, renglon, i);
        		leerPrimaTotal(arrNewContenido, renglon, i);
        		leerVigencia(arrNewContenido, renglon, i);
        		leerProducto(arrNewContenido, renglon, i);
        	}
        }
        
        
        inicio = contenido.indexOf("NOMBRE DEL AGENTE");
        
        leerInformacionAgente(inicio);           	             
        leerAsegurados();      
        
        
        return modelo;
	} catch (Exception ex) {
		modelo.setError(
				InbursaSaludModel.this.getClass().getTypeName() + " - catch:" + ex.getMessage() + " | " + ex.getCause());
		return modelo;
	}
    
	
	}
		
	private void readPoliza(String[] arrNewContenido, String renglon, int i) {
		
		if (renglon.contains("Cliente") && renglon.contains("PÓLIZA") && renglon.contains("CIS")) {
			modelo.setPoliza(arrNewContenido[i - 1].split("###")[0]);
		}

	}
	
	private void leerNombreCteyPrima(String renglon,String[] arrNewContenido, int i) {
		if (renglon.contains("NOMBRE:") && renglon.contains("PRIMA")) {
			modelo.setCteNombre(arrNewContenido[i + 1].split("###")[0]);
			modelo.setPrimaneta(fn.castBigDecimal(fn
					.castDouble(arrNewContenido[i + 1].split("###")[arrNewContenido[i + 1].split("###").length - 1])));
		}

	}
	
	private void leerDireccion(String[] arrNewContenido,String renglon, StringBuilder resultado, int i ) {

		if (renglon.contains("DIRECCIÓN:")) {
			resultado.append(arrNewContenido[i + 1].split("###")[0]);
			resultado.append(arrNewContenido[i + 2].split("R.F.C:")[0]);
			resultado.append(" ");
			resultado.append(arrNewContenido[i + 3].split("###")[0]);
			modelo.setCteDireccion(resultado.toString().replace("###", ""));
		}
		
	}
	
	private void leerRFC(String[] arrNewContenido,String renglon, int i) {
		if(renglon.contains("R.F.C:")) {
			modelo.setRfc(arrNewContenido[i+1].split("###")[arrNewContenido[i+1].split("###").length -2]);
		}
	}
	
	private void leerDerechoyCP(String[] arrNewContenido,String renglon, int i) {
		if(renglon.contains("FINANCIAMIENTO")) {
			modelo.setCp(arrNewContenido[i+1].split("C.P.")[1].split("###")[0].trim());
			modelo.setDerecho(fn.castBigDecimal(fn.castDouble(arrNewContenido[i+1].split("###")[arrNewContenido[i+1].split("###").length -1])));
		}
	}
	
	private void leerMonedaYRecargo(String[] arrNewContenido,String renglon, int i) {
		if(renglon.contains("MONEDA:") && renglon.contains("GASTOS EXPEDICIÓN:")) {
			modelo.setMoneda(fn.moneda( arrNewContenido[i+1].split("###")[0]));
			modelo.setRecargo(fn.castBigDecimal(fn.castDouble(arrNewContenido[i+1].split("###")[arrNewContenido[i+1].split("###").length -1])));
		}
	}
	
	private void leerFormaPagoIva(String[] arrNewContenido,String renglon, int i) {
		if(renglon.contains("PAGO:") && renglon.contains("IVA")) {
			modelo.setFormaPago(fn.formaPago( arrNewContenido[i+1].split("###")[0]));
			modelo.setIva(fn.castBigDecimal(fn.castDouble(arrNewContenido[i+1].split("###")[arrNewContenido[i+1].split("###").length -1])));
		}
	}
	
	private void leerPrimaTotal(String[] arrNewContenido,String renglon, int i) {
		if(renglon.contains("DOCUMENTO:") && renglon.contains("PRIMA")) {
			modelo.setPrimaTotal(fn.castBigDecimal(fn.castDouble(arrNewContenido[i+1].split("###")[arrNewContenido[i+1].split("###").length -1])));
		}
	}
	
	private void leerVigencia(String[] arrNewContenido,String renglon, int i) {
		 if(renglon.contains("Desde") && renglon.contains(ConstantsValue.HASTA2) && renglon.contains("-")) { 
   			modelo.setVigenciaDe(fn.formatDateMonthCadena(arrNewContenido[i].split("Desde")[1].split(ConstantsValue.HASTA2)[0].replace("###", "").trim()));
   			modelo.setVigenciaA(fn.formatDateMonthCadena(arrNewContenido[i].split(ConstantsValue.HASTA2)[1].split("###")[1].replace("###", "").trim()));
   		 }
	}
	
	private void leerProducto(String[] arrNewContenido,String renglon, int i) {
		 if(renglon.contains("PRODUCTO")){            			
			 modelo.setPlan(arrNewContenido[i+1].split("###")[0].replace("###", "").trim());
		 } 
	}

	private void leerInformacionAgente(int inicio) {
		String newcontenido = contenido.split("NOMBRE DEL AGENTE")[0];
		if(inicio > 0 && newcontenido.length() > 200) {
		        String renglon = "";
	    		newcontenido = newcontenido.substring(newcontenido.length()-200,newcontenido.length()).replace("@@@", "").replace("\r", "");
	    		String[] arrNewContenido = newcontenido.split("\n");
	    		for (int j = 0; j < arrNewContenido.length; j++) {    
	    			renglon = arrNewContenido[j];
	            	if(renglon.contains("CLAVE") && fn.extraerNumeros( arrNewContenido[j-2]).length() > 0) {   
	            		modelo.setCveAgente(fn.extraerNumeros( arrNewContenido[j-2]));
	            		String a = arrNewContenido[j-1].replace(" ", "###").split("###")[0].trim();
	            		if(a.contains("@")) {
           				 a ="";
	            		}
	            		modelo.setAgente((arrNewContenido[j-2].split( modelo.getCveAgente() )[1] +""+ a).trim());                                  			
	            	}
	    		}
		}   	             
		
	}

	private void leerAsegurados() {
        int inicio = contenido.indexOf("ASEGURADOS");
        int fin = contenido.indexOf("COBERTURAS");
        
        if(inicio > 0 &&  fin >  0 && inicio < fin) {
        	List<EstructuraAseguradosModel> asegurados = new ArrayList<>();
            String newcontenido = contenido.substring(inicio, fin).replace("\r","").replace("@", "");
            String[] arrNewContenido = newcontenido.split("\n");
        	EstructuraAseguradosModel asegurado = new EstructuraAseguradosModel();
        	String renglon = "";
        	for (int i = 0; i < arrNewContenido.length; i++) {  
        		renglon = arrNewContenido[i];
        		
        		if(renglon.contains("Nombre:")) {
        			asegurado.setNombre(renglon.split("Nombre:")[1].split("###")[1]);            		
        		}
        		if(renglon.contains("nacimiento:")) {
        			asegurado.setNacimiento(fn.formatDateMonthCadena( renglon.split("nacimiento:")[1].split("###")[1]));            			
        		}
        		
        		leerGeneroYParentesco(renglon,asegurado);
        	}
        	asegurados.add(asegurado);
        	modelo.setAsegurados(asegurados);
        	
        	

        }
	}
	
	private void leerGeneroYParentesco(String renglon,EstructuraAseguradosModel asegurado) {
		if(renglon.contains("Género:") && renglon.contains("Fecha:")) {
			asegurado.setSexo(fn.sexo( renglon.split("Género:")[1].split("###")[1]).booleanValue() ? 1 : 0);
		}
		
		if(renglon.contains("Parentesco:") && renglon.contains("Tope")) {
			asegurado.setParentesco(fn.parentesco( renglon.split("Parentesco:")[1].split("###")[1]));            		
		}
	}
}
