package com.copsis.models.allians;

import java.util.ArrayList;
import java.util.List;

import com.copsis.constants.ConstantsValue;
import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraAseguradosModel;
import com.copsis.models.EstructuraBeneficiariosModel;
import com.copsis.models.EstructuraCoberturasModel;
import com.copsis.models.EstructuraJsonModel;

public class AlliansVidaModel {
	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
	private String contenido = "";
	
	
	public AlliansVidaModel(String contenido ) {
		this.contenido = contenido;		
	}
	public EstructuraJsonModel procesar() {
		StringBuilder newcont = new StringBuilder();
		int inicio = 0;
		int fin = 0;
		
		contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());
		try {
			modelo.setTipo(5);		
			modelo.setCia(4);
			modelo.setMoneda(1);

			
			List<EstructuraAseguradosModel> asegurados = new ArrayList<>();
			EstructuraAseguradosModel asegurado = new EstructuraAseguradosModel();
			 inicio =  contenido.indexOf("Contratante");
			 fin = contenido.indexOf("Riesgos Cubiertos");
			 if (inicio > -1 && fin > -1 && inicio < fin) {
				 newcont.append(contenido.substring(inicio, fin).replace("@@@", "").replace("\r", ""));
				 
				for (int i = 0; i < newcont.toString().split("\n").length; i++) {			
					 if(newcont.toString().split("\n")[i].contains("Contratante")) {
						 modelo.setCteNombre(newcont.toString().split("\n")[i+1].split("###")[0].replace("@@@", "").trim());
						 modelo.setCteDireccion( newcont.toString().split("\n")[i+2].split("###")[0] +" " + newcont.toString().split("\n")[i+3].split("###")[0]
								 +" " + newcont.toString().split("\n")[i+4].split("###")[0]
								 );
						 
						 if(fn.isNumeric( newcont.toString().split("\n")[i+3].substring(0,5))) {
							 modelo.setCp(newcont.toString().split("\n")[i+3].substring(0,5));
						 }
						if(newcont.toString().split("\n")[i+1].split("###").length>1) {
							modelo.setPlan(newcont.toString().split("\n")[i+1].split("###")[newcont.toString().split("\n")[i+1].split("###").length-1].trim());
						}
					 }
					 if(newcont.toString().split("\n")[i].contains("RFC")) {
						modelo.setRfc(newcont.toString().split("\n")[i].split("###")[1].trim()); 
					 }
					 if(newcont.toString().split("\n")[i].contains("Número de Póliza") && newcont.toString().split("\n")[i].contains("Emisión") && newcont.toString().split("\n")[i].contains("Forma de Pago")) {
						 modelo.setPoliza(newcont.toString().split("\n")[i+1].split("###")[0].trim());
						 modelo.setVigenciaDe(fn.formatDateMonthCadena( newcont.toString().split("\n")[i+1].split("###")[2].trim().split(" ")[0]));
						 modelo.setVigenciaA(fn.formatDateMonthCadena( newcont.toString().split("\n")[i+1].split("###")[2].trim().split(" ")[1]));
						 modelo.setFormaPago(fn.formaPagoSring(newcont.toString().split("\n")[i+1].split("###")[3].trim()));
					   if(modelo.getVigenciaDe().length() > 0) {
						   modelo.setFechaEmision(modelo.getVigenciaDe());
					   }
					 }
					 if(newcont.toString().split("\n")[i].contains("Asegurado") && newcont.toString().split("\n")[i].contains("Fumador")) {
						asegurado.setNombre(newcont.toString().split("\n")[i+1].split("###")[0]);
						if((i+2)< newcont.toString().split("\n").length) {
							asegurado.setNombre( !newcont.toString().split("\n")[i+2].contains("###") ? asegurado.getNombre() + " "+ newcont.toString().split("\n")[i+2].trim(): asegurado.getNombre());
						}
                        asegurado.setNacimiento(fn.formatDateMonthCadena(newcont.toString().split("\n")[i+1].split("###")[1]));
                        asegurado.setEdad(fn.castInteger(newcont.toString().split("\n")[i+1].split("###")[2]));
                        asegurado.setSexo(Boolean.TRUE.equals(fn.sexo(newcont.toString().split("\n")[i+1].split("###")[2] ))? 1:0 );
                        asegurados.add(asegurado);
					 }
				}
				
				modelo.setAsegurados(asegurados);
			 }
			

			 
			 inicio =  contenido.indexOf("Riesgos Cubiertos");
			 fin = contenido.indexOf("Prima Neta");
			 
			 if (inicio > -1 && fin > -1 && inicio < fin) {
				 List<EstructuraCoberturasModel> coberturas = new ArrayList<>();
				 newcont  = new StringBuilder();
				 newcont.append(contenido.substring(inicio, fin).replace("@@@", "").replace("\r", "")
						 .replace("PERMANENTE", "EXENCION DE PAGO DE PRIMAS POR INVALIDEZ TOTAL YPERMANENTE"));
				
				 for (int i = 0; i < newcont.toString().split("\n").length; i++) {		
					 EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();
					 if(!newcont.toString().split("\n")[i].contains("Riesgos") && !newcont.toString().split("\n")[i].contains("Coberturas")  && (newcont.toString().split("\n")[i].split("###").length > 1)) {
						 cobertura.setNombre(newcont.toString().split("\n")[i].split("###")[0]);
						 cobertura.setSa(newcont.toString().split("\n")[i].split("###")[1]);
						 coberturas.add(cobertura);
						
					 }
				 }
				 modelo.setCoberturas(coberturas);
			 }
			 

			 
			 inicio =  contenido.indexOf("Prima Neta");
			 fin = contenido.indexOf(ConstantsValue.BENEFICIARIOSMN);
			 
			 if (inicio > -1 && fin > -1 && inicio < fin) {				
				 newcont  = new StringBuilder();
				 String newContenido = fn.extracted(inicio, fin, contenido);
				 newContenido = newContenido.replace("Prima Neta Anual###Derecho de Póliza###Recargo por Pago###Prima Total Anual###Importe Primer###Importe Recibos\n"
				 		+ "Fraccionado###Recibo###Subsecuentes","Prima Neta Anual###Derecho de Póliza###Recargo por Pago Fraccionado###Prima Total Anual###Importe Primer Recibo###Importe Recibos Subsecuentes");
				 newcont.append(newContenido);
				boolean primas= true;
				 for (int i = 0; i < newcont.toString().split("\n").length; i++) {	
					
					 if(newcont.toString().split("\n")[i].contains("Fraccionado")) {
						List<String> valores = fn.obtenerListNumeros(newcont.toString().split("\n")[i+1]);
						if(!valores.isEmpty()){
								getPrimas(valores);
								primas= false;
						}
						if(valores.isEmpty() && primas){
							valores = fn.obtenerListNumeros(newcont.toString().split("\n")[i+2]);
							if(!valores.isEmpty()){
								getPrimas(valores);
							}
						}
    						
					 }
				 }
			 }
			 
			 
				List<EstructuraBeneficiariosModel> beneficiarios = new ArrayList<>();
				
	
				 inicio =  contenido.indexOf(ConstantsValue.BENEFICIARIOSMN);
				 fin = contenido.indexOf("Nombre del Agente");
				 
				 if (inicio > -1 && fin > -1 && inicio < fin) {				
					 newcont  = new StringBuilder();
					 newcont.append(contenido.substring(inicio, fin).replace("@@@", "").replace("\r", "").replace("Principal", "###Principal"));
					 for (int i = 0; i < newcont.toString().split("\n").length; i++) {	
							EstructuraBeneficiariosModel beneficiario = new EstructuraBeneficiariosModel();
						 if(newcont.toString().split("\n")[i].contains(ConstantsValue.BENEFICIARIOSMN)) {					
							 int sp = newcont.toString().split("\n")[i+1].split("###").length;
							 if(sp == 3 ) {
								 beneficiario.setNombre(newcont.toString().split("\n")[i+1].split("###")[0].trim());
								 beneficiario.setParentesco(fn.parentesco(newcont.toString().split("\n")[i+1].split("###")[1].trim()));										
								 beneficiario.setPorcentaje(fn.castInteger(newcont.toString().split("\n")[i+1].split("###")[2].replace("%", "").trim()));
								 beneficiarios.add(beneficiario);
							 }else {
								 beneficiario.setNombre(newcont.toString().split("\n")[i+1].split("###")[0].trim());
								 beneficiario.setParentesco(fn.parentesco(newcont.toString().split("\n")[i+1].split("###")[2].trim()));										
								 beneficiario.setPorcentaje(fn.castInteger(newcont.toString().split("\n")[i+1].split("###")[3].replace("%", "").trim()));
								 beneficiarios.add(beneficiario);
							 }
						
						 }
					 }
					 modelo.setBeneficiarios(beneficiarios);
				 }
				 

				 inicio =  contenido.indexOf("Nombre del Agente");
				 fin = contenido.indexOf("En el caso de");
				 
				 if (inicio > -1 && fin > -1 && inicio < fin) {				
					 newcont  = new StringBuilder();
					 newcont.append(contenido.substring(inicio, fin).replace("@@@", "").replace("\r", ""));
					 for (int i = 0; i < newcont.toString().split("\n").length; i++) {	
						 if(newcont.toString().split("\n")[i].contains("Agente") && newcont.toString().split("\n")[i+1].contains("Tarjeta de Crédito")) {
                            modelo.setAgente(newcont.toString().split("\n")[i+2].split("###")[1].trim());
                            modelo.setCveAgente(newcont.toString().split("\n")[i+2].split("###")[0].trim());
						 }else if(newcont.toString().split("\n")[i].contains("Agente") && newcont.toString().split("\n")[i+1].split("###").length>1) {
							 modelo.setAgente(newcont.toString().split("\n")[i+1].split("###")[1].trim());
	                         modelo.setCveAgente(newcont.toString().split("\n")[i+1].split("###")[0].trim());
						 }
					 }					 
				 }							 
			 	
				 if(fn.diferencia(modelo.getVigenciaDe(), modelo.getVigenciaA()) > 5) {
				     modelo.setVigenciaA(fn.calcvigenciaA(modelo.getVigenciaDe(), 12));
				 }
				 
			return modelo;
		} catch (Exception ex) {	
			ex.printStackTrace();
			modelo.setError(AlliansVidaModel.this.getClass().getTypeName() + " | " + ex.getMessage() + " | "+ ex.getCause());
			return modelo;
		}
		
		
	}
	private void getPrimas(List<String> valores) {
		modelo.setPrimaneta(fn.castBigDecimal(fn.castDouble(valores.get(0))));
		modelo.setDerecho(fn.castBigDecimal(fn.castDouble(valores.get(1))));
		modelo.setRecargo(fn.castBigDecimal(fn.castDouble(valores.get(2))));						           
		modelo.setPrimaTotal(fn.castBigDecimal(fn.castDouble(valores.get(3))));
		modelo.setPrimerPrimatotal(fn.castBigDecimal(fn.castDouble(valores.get(4))));
		modelo.setSubPrimatotal(fn.castBigDecimal(fn.castDouble(valores.get(5))));
	}	
}
