package com.copsis.models.inbursa;

import java.util.ArrayList;
import java.util.List;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraAseguradosModel;
import com.copsis.models.EstructuraCoberturasModel;
import com.copsis.models.EstructuraJsonModel;

public class InbursaVidaModel {
	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
	
	public EstructuraJsonModel procesar(String contenido) {
		contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());
		StringBuilder newcontenido = new StringBuilder();		
		StringBuilder newdire = new StringBuilder();		
		int inicio = 0;
		int fin = 0;
		try {
			inicio = contenido.indexOf("PÓLIZA DE SEGUROS");
			fin = contenido.indexOf("COBERTURAS");
			

			newcontenido.append(fn.extracted(inicio, fin, contenido));
			
			for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
		    
		     if(newcontenido.toString().split("\n")[i].contains("Cliente Inbursa") &&  newcontenido.toString().split("\n")[i].contains("Póliza")) {
		    	modelo.setPoliza(newcontenido.toString().split("\n")[i].split("Póliza")[1].trim()); 
		     }
		     
		     if(newcontenido.toString().split("\n")[i].contains("Nombre") && newcontenido.toString().split("\n")[i].contains("Agrupación") &&  newcontenido.toString().split("\n")[i].contains("Prima neta")) {
		    	 modelo.setCteNombre(newcontenido.toString().split("\n")[i+1].split("###")[0]);
		    		modelo.setPrimaneta(fn.castBigDecimal(fn.castDouble(newcontenido.toString().split("\n")[i + 1].split("###")[2])));		    	
		     }
		     
		     if(newcontenido.toString().split("\n")[i].contains("Financiamiento") && newcontenido.toString().split("\n")[i+1].contains("Dirección")){
		    	 modelo.setRecargo(fn.castBigDecimal(fn.castDouble(newcontenido.toString().split("\n")[i + 2].split("###")[0])));	
		     } 
		    		 
		     
		     	if(newcontenido.toString().split("\n")[i].contains("R.F.C.") && newcontenido.toString().split("\n")[i].contains("Importe")) {		  
				    	 newdire.append(newcontenido.toString().split("\n")[i].split("R.F.C.")[0].replace("###", ""));
				    	 if(newcontenido.toString().split("\n")[i+1].contains("C.P.")) {
				    		 newdire.append(newcontenido.toString().split("\n")[i+1].split("C.P.")[0].replace("###", ""));
				    		 modelo.setCp(newcontenido.toString().split("\n")[i+1].split("C.P.")[1].trim().substring(0,5));
				    	 }			    	 
				    	modelo.setRfc(newcontenido.toString().split("\n")[i+1].split("###")[1]);
				    	modelo.setPrimaTotal(fn.castBigDecimal(fn.castDouble(newcontenido.toString().split("\n")[i + 1].split("###")[2])));		    	
			     }
			 
			 
			 if(newcontenido.toString().split("\n")[i].contains("Moneda")) {
				 modelo.setMoneda(fn.buscaMonedaEnTexto(newcontenido.toString().split("\n")[i+1]));
			 }
			 if(newcontenido.toString().split("\n")[i].contains("Forma de pago")) {
				 modelo.setFormaPago(fn.formaPagoSring(newcontenido.toString().split("\n")[i+1]));
			 }
			 if(newcontenido.toString().split("\n")[i].split("-").length > 3) {				
				modelo.setVigenciaDe(fn.formatDateMonthCadena(fn.obtenVigePoliza2(newcontenido.toString().split("\n")[i]).get(0)));
				modelo.setVigenciaA(fn.formatDateMonthCadena(fn.obtenVigePoliza2(newcontenido.toString().split("\n")[i]).get(1)));
			 }

			}					
			modelo.setCteDireccion(newdire.toString());
			
			
			inicio = contenido.indexOf("Asegurado");
			fin = contenido.indexOf("A partir del ###inicio");
			newcontenido = new StringBuilder();
			newcontenido.append(fn.extracted(inicio, fin, contenido));
			List<EstructuraAseguradosModel> asegurados = new ArrayList<>();
			for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
				
				 if(!newcontenido.toString().split("\n")[i].contains("Nacimiento")) {
					 EstructuraAseguradosModel asegurado = new EstructuraAseguradosModel();   
					 asegurado.setNombre(newcontenido.toString().split("\n")[i].split("###")[0]);
					 asegurado.setParentesco(fn.parentesco(newcontenido.toString().split("\n")[i].split("###")[2]));
					 asegurado.setNacimiento(fn.formatDateMonthCadena(fn.obtenVigePoliza2(newcontenido.toString().split("\n")[i]).get(0)));
					 asegurado.setEdad(fn.castInteger(newcontenido.toString().split("\n")[i].split("###")[5]));
					 asegurados.add(asegurado);
				 }
			}
			modelo.setAsegurados(asegurados);
			
		
			inicio = contenido.indexOf("COBERTURAS");
			fin = contenido.indexOf("LA PRESENTE PÓLIZA QUEDA");
			newcontenido = new StringBuilder();
			newcontenido.append(fn.extracted(inicio, fin, contenido)
					.replace("A###Renta mensual", "Renta mensual")
					.replace("Adelanto ###de ###suma ###asegurada ###por ###enfermedad", "Adelanto de suma asegurada por enfermedad")
					);
			
			List<EstructuraCoberturasModel> coberturas = new ArrayList<>();
			for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
				
				if( newcontenido.toString().split("\n")[i].split("-").length> 2) {
					if(!newcontenido.toString().split("\n")[i].contains("dd-mm-aaaa") && newcontenido.toString().split("\n")[i].split("###").length > 3 ) {
						EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();  
						cobertura.setNombre(newcontenido.toString().split("\n")[i].split("###")[0]);
						cobertura.setSa(newcontenido.toString().split("\n")[i].split("###")[1]);
						coberturas.add(cobertura);
							
					}
			
				}
				
			}
			
			modelo.setCoberturas(coberturas);
			

			
			inicio = contenido.indexOf("CLAVE Y NOMBRE DEL AGENTE");
			if(inicio >  0) {
				newcontenido = new StringBuilder();
				newcontenido.append(fn.extracted((inicio-100), inicio, contenido));			
				for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
					if(newcontenido.toString().split("\n")[i].split("-").length >2 ) {
						modelo.setFechaEmision(fn.formatDateMonthCadena(newcontenido.toString().split("\n")[i].split("###")[1]));
						modelo.setCveAgente(fn.obtenerListNumeros2(newcontenido.toString().split("\n")[i].split("###")[0]).get(0).toString());
						modelo.setAgente(newcontenido.toString().split("\n")[i].split("###")[0].split(modelo.getCveAgente())[1].trim());
					}
				}
			}
			
			
			return modelo;
		} catch (Exception ex) {
			modelo.setError(InbursaVidaModel.this.getClass().getTypeName() + " - catch:" + ex.getMessage() + " | "+ ex.getCause());
			return modelo;
		}
	}
}
