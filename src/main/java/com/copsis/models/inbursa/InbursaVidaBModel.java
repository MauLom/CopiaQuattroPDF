package com.copsis.models.inbursa;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraAseguradosModel;
import com.copsis.models.EstructuraCoberturasModel;
import com.copsis.models.EstructuraJsonModel;

public class InbursaVidaBModel {
	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
	
	
	public EstructuraJsonModel procesar(String contenido) {

	
		StringBuilder newcontenido = new StringBuilder();
		StringBuilder newdire = new StringBuilder();		
		int inicio = 0;
		int fin = 0;

		contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());
		contenido = contenido.replace("DOLARES", "DÓLARES").replace("R.F.C.", "R.F.C").replace("I.V.A.", "IVA")
				.replace("COBERTURAS CONTRATADAS", "SECCION###COBERTURAS#");
		try {
			// tipo
			modelo.setTipo(5);

			// cia
			modelo.setCia(38);

	
			
			
			inicio = contenido.indexOf("PÓLIZA DE SEGUROS");
			fin = contenido.indexOf("COBERTURAS");
			

			newcontenido.append(fn.extracted(inicio, fin, contenido).replace("R.F.C:", "R.F.C."));
			
			
			for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
				
		     if(newcontenido.toString().split("\n")[i].contains("PÓLIZA") &&  newcontenido.toString().split("\n")[i].contains("CIS")) {
		    	modelo.setPoliza(newcontenido.toString().split("\n")[i-1].split("###")[1].trim()); 
		     }

			  if(newcontenido.toString().split("\n")[i].contains("Póliza") &&  newcontenido.toString().split("\n")[i].contains("CIS")) {
		    	modelo.setPoliza(newcontenido.toString().split("\n")[i].split("Póliza")[1].split("CIS")[0].replace("###","").trim()); 
		     }
		     
		     if(newcontenido.toString().split("\n")[i].contains("NOMBRE") && newcontenido.toString().split("\n")[i].contains("AGRUPACIÓN") &&  newcontenido.toString().split("\n")[i].contains("PRIMA NETA")) {
		    	 modelo.setCteNombre(newcontenido.toString().split("\n")[i+1].split("###")[0]);
		    		modelo.setPrimaneta(fn.castBigDecimal(fn.castDouble(newcontenido.toString().split("\n")[i + 1].split("###")[2])));		    	
		     }
			  if(newcontenido.toString().split("\n")[i].contains("Nombre") && newcontenido.toString().split("\n")[i].contains("Agrupación") 
			  &&  newcontenido.toString().split("\n")[i].contains("Prima neta")) {
		    	 modelo.setCteNombre(newcontenido.toString().split("\n")[i+1].split("###")[0]);
		    		modelo.setPrimaneta(fn.castBigDecimal(fn.castDouble(newcontenido.toString().split("\n")[i + 1].split("###")[2])));		    	
		     }
		     
		     if(newcontenido.toString().split("\n")[i].contains("FINANCIAMIENTO") && newcontenido.toString().split("\n")[i+1].contains("DIRECCIÓN")){
		    	 modelo.setRecargo(fn.castBigDecimal(fn.castDouble(newcontenido.toString().split("\n")[i + 2].split("###")[0])));	
		     } 
		    		 
		     
		     	if(newcontenido.toString().split("\n")[i].contains("R.F.C.") && newcontenido.toString().split("\n")[i].contains("IMPORTE")) {	
		     		 newdire.append(newcontenido.toString().split("\n")[i-1]);
				    	 newdire.append(newcontenido.toString().split("\n")[i].split("R.F.C.")[0].replace("###", ""));
				    	 if(newcontenido.toString().split("\n")[i].contains("C.P.")) {
				    		 modelo.setCp(newcontenido.toString().split("\n")[i].split("C.P.")[1].trim().substring(0,5));
				    	 }
				    	 else if(newcontenido.toString().split("\n")[i+1].contains("C.P.")) {
				    		 newdire.append(newcontenido.toString().split("\n")[i+1].split("C.P.")[0].replace("###", ""));
				    		 modelo.setCp(newcontenido.toString().split("\n")[i+1].split("C.P.")[1].trim().substring(0,5));
				    	 }			    	 
				    	modelo.setRfc(newcontenido.toString().split("\n")[i+1].split("###")[1]);
				    	modelo.setPrimaTotal(fn.castBigDecimal(fn.castDouble(newcontenido.toString().split("\n")[i + 1].split("###")[2])));		    	
			     }
			 if(modelo.getCteDireccion().length() == 0 && newcontenido.toString().split("\n")[i].contains("COL.") && newcontenido.toString().split("\n")[i].contains("C.P.")){
					newdire.append(newcontenido.toString().split("\n")[i].split("C.P.")[0].replace("###", " "));
			 }

			 if( newcontenido.toString().split("\n")[i].contains("C.P.")){
                 List<String> valores = fn.obtenerListNumeros2(newcontenido.toString().split("\n")[i]);
				if(!valores.isEmpty()){
                  modelo.setCp(valores.get(0));
				}
			 }
			
			 
			 if(newcontenido.toString().split("\n")[i].contains("MONEDA")) {
				 modelo.setMoneda(fn.buscaMonedaEnTexto(newcontenido.toString().split("\n")[i+1]));
			 }
			 if(newcontenido.toString().split("\n")[i].contains("FORMA DE PAGO")) {
				 modelo.setFormaPago(fn.formaPagoSring(newcontenido.toString().split("\n")[i+1]));
			 }
		
			 if(newcontenido.toString().split("\n")[i].contains("VIGENCIA") && newcontenido.toString().split("\n")[i+1].split("-").length > 3) {				
				modelo.setVigenciaDe(fn.formatDateMonthCadena(fn.obtenVigePoliza2(newcontenido.toString().split("\n")[i+1]).get(0)));
				modelo.setVigenciaA(fn.formatDateMonthCadena(fn.obtenVigePoliza2(newcontenido.toString().split("\n")[i+1]).get(1)));
			 }

			  if(newcontenido.toString().split("\n")[i].contains("Desde") &&newcontenido.toString().split("\n")[i].contains("hasta") && newcontenido.toString().split("\n")[i].split("-").length > 3) {				
				modelo.setVigenciaDe(fn.formatDateMonthCadena(fn.obtenVigePoliza2(newcontenido.toString().split("\n")[i]).get(0)));
				modelo.setVigenciaA(fn.formatDateMonthCadena(fn.obtenVigePoliza2(newcontenido.toString().split("\n")[i]).get(1)));
			 }

			}

			if(modelo.getPrimaTotal() == BigDecimal.ZERO){
            modelo.setPrimaTotal(modelo.getPrimaneta());
			}
			
	     modelo.setCteDireccion(newdire.toString());
			
			
			inicio = contenido.indexOf("Asegurado");
			fin = contenido.indexOf("ESPECIFICACIÓN DE SECCION");
			fin = fin == -1? contenido.indexOf("@COBERTURAS"):fin;

			
			newcontenido = new StringBuilder();
			newcontenido.append(fn.extracted(inicio, fin, contenido));

			List<EstructuraAseguradosModel> asegurados = new ArrayList<>();
			for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {		
				 if(!newcontenido.toString().split("\n")[i].contains("nacimiento") && !newcontenido.toString().split("\n")[i].contains("Asegurado")
						 && !newcontenido.toString().split("\n")[i].contains("fumador") 
						 && !newcontenido.toString().split("\n")[i].contains("Nacimiento") 
						 ) {
							if(newcontenido.toString().split("\n")[i].split("-").length > 2){
							EstructuraAseguradosModel asegurado = new EstructuraAseguradosModel();   
							asegurado.setNombre(newcontenido.toString().split("\n")[i].split("###")[0]);							
							asegurado.setParentesco(fn.parentesco(newcontenido.toString().split("\n")[i].split("###")[2]));
							 asegurado.setNacimiento(fn.formatDateMonthCadena(fn.obtenVigePoliza2(newcontenido.toString().split("\n")[i]).get(0)));
							asegurados.add(asegurado);
					}				
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
						if(fn.obtenerListNumeros2(newcontenido.toString().split("\n")[i].split("###")[0]).size()> 0) {
							modelo.setCveAgente(fn.obtenerListNumeros2(newcontenido.toString().split("\n")[i].split("###")[0]).get(0).toString());
							if(modelo.getCveAgente().length() >  0) {
								modelo.setAgente(newcontenido.toString().split("\n")[i].split("###")[0].split(modelo.getCveAgente())[1].trim());
							}
						}else {
							modelo.setCveAgente(newcontenido.toString().split("\n")[i-1].split(" ")[0]);
							modelo.setAgente(newcontenido.toString().split("\n")[i-1].split(modelo.getCveAgente())[1].trim() );
						}
						
					}
				}
			}
			inicio = contenido.indexOf("Opción de pago");
			fin = contenido.indexOf("LA PRESENTE PÓLIZA QUEDA");
            newcontenido = new StringBuilder();
			newcontenido.append(fn.extracted(inicio, fin, contenido));
			for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {				
				if(newcontenido.toString().split("\n")[i].contains("sola exhibición")){
					 modelo.setFormaPago(1);
					 modelo.setMoneda(1);
				}
		       
			}
			
             
			
	

			return modelo;
		} catch (Exception ex) {			
			modelo.setError(InbursaVidaBModel.this.getClass().getTypeName() + " - catch:" + ex.getMessage() + " | "
					+ ex.getCause());
			return modelo;
		}
	}
}
