package com.copsis.models.aba;

import java.util.ArrayList;
import java.util.List;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraCoberturasModel;
import com.copsis.models.EstructuraJsonModel;
import com.copsis.models.EstructuraUbicacionesModel;

public class AbaDiversosModel {
	
	// Clases
	DataToolsModel fn = new DataToolsModel();
	EstructuraJsonModel modelo = new EstructuraJsonModel();

	// Variables
	private String contenido;
	private int inicio = 0;
	private int fin = 0;
	private String newcontenido = "";
	private String deducible ="";
	private String nombre="";
	private String sa;
	

	// constructor
	public AbaDiversosModel(String contenido ) {
		this.contenido = contenido;

	}
	
	public EstructuraJsonModel procesar() {
		contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());
		try {
			//tipo
			modelo.setTipo(7);			
			//cia
			modelo.setCia(1);

			//Datos de la poliza
			inicio = contenido.indexOf("Póliza");
			fin = contenido.indexOf("Coaseguro");
			
			
			if(inicio > 0 &&  fin >  0 && inicio < fin ) {
				newcontenido = contenido.substring(inicio,fin);
				for (int i = 0; i < newcontenido.split("\n").length; i++) {		

					if(newcontenido.split("\n")[i].contains("Póliza") && newcontenido.split("\n")[i].contains("Vigencia")) {
						modelo.setPoliza(newcontenido.split("\n")[i].split("Póliza")[1].split("Vigencia")[0].replace("###", ""));
						modelo.setVigenciaDe(fn.formatDate_MonthCadena(newcontenido.split("\n")[i].split("DEL")[1].split("HORAS")[0].replace("12:00", "").replace("###", "").trim()));
						modelo.setVigenciaA(fn.formatDate_MonthCadena(newcontenido.split("\n")[i].split("HORAS AL")[1].split("HORAS")[0].replace("12:00", "").replace("###", "").trim()));					
					}					
					if(newcontenido.split("\n")[i].contains("Inciso") && newcontenido.split("\n")[i].contains("Endoso")) {
						modelo.setInciso(Integer.parseInt(newcontenido.split("\n")[i].split("Inciso")[1].split("Asegurado")[0].replace("###", "").trim()));
						modelo.setEndoso(newcontenido.split("\n")[i].split("Endoso:")[1].split("Tipo")[0].replace("###", ""));
					}
					if(newcontenido.split("\n")[i].contains("asegurado")){
						modelo.setCteNombre( newcontenido.split("\n")[i+1].replace("\r", ""));
					}
					if(newcontenido.split("\n")[i].contains("Domicilio") && newcontenido.split("\n")[i].contains("Teléfono") && newcontenido.split("\n")[i].contains("R.F.C:")){
						modelo.setCteDireccion((newcontenido.split("\n")[i].split("Domicilio:")[1].split("Teléfono")[0]
							+" "+ newcontenido.split("\n")[i+1].split("Colonia:")[1]
							+" "+ newcontenido.split("\n")[i+2]).replace("###", "").replace("\r", "") );
						
						modelo.setRfc(newcontenido.split("\n")[i].split("R.F.C:")[1].replace("\r", "").replace("###", ""));
					}					
					if(newcontenido.split("\n")[i].contains("C.P:")){
						modelo.setCp( newcontenido.split("\n")[i].split("C.P:")[1].split("###")[1]);
					}
					if(newcontenido.split("\n")[i].contains("Agente") && newcontenido.split("\n")[i].contains("Pago")){
						modelo.setCveAgente(newcontenido.split("\n")[i].split("Agente:")[1].split("Forma")[0].replace("###", ""));
						modelo.setFormaPago(fn.formaPago(newcontenido.split("\n")[i].split("Pago:")[1].replace("###", "").replace("\r", "")));
					}
					if(newcontenido.split("\n")[i].contains("emisión:")){
						String x=newcontenido.split("\n")[i].split("emisión:")[1].replace(" ", "###");
				
					 if( x.split("###")[0].length() > 1 ) {
							x = x.split("###")[0] +"-"+ x.split("###")[1] +"-"+ x.split("###")[2].trim();
					 }else {						
							x = x.split("###")[1] +"-"+ x.split("###")[2] +"-"+ x.split("###")[3].trim();							
					 }					
						modelo.setFechaEmision(fn.formatDate_MonthCadena(x));
						
						if(newcontenido.split("\n")[i].contains("Moneda")) {
							modelo.setMoneda(fn.moneda(newcontenido.split("\n")[i].split("Moneda:")[1].replace("###", "").replace("\r", "").trim()));
						}
												
					}
					if(newcontenido.split("\n")[i].contains("Moneda:") && modelo.getMoneda() == 0){
				
						modelo.setMoneda(fn.moneda(newcontenido.split("\n")[i].split("Moneda:")[1].split("###")[1].trim()));
					}
				}			
			}
			
			//PRIMAS
			inicio = contenido.indexOf("Prima Neta");
			fin = contenido.indexOf("Prima Total");
			
			if(inicio >  0 && fin >  0 && inicio < fin) {
				newcontenido = contenido.substring(inicio, fin+50);
				for (int i = 0; i < newcontenido.split("\n").length; i++) {
					
					if(newcontenido.split("\n")[i].contains("Neta")) {
						modelo.setPrimaneta(fn.castBigDecimal( fn.castDouble( newcontenido.split("\n")[i].split("Neta")[1].replace("###", "").trim())));
					}
					
					if(newcontenido.split("\n")[i].contains("fraccionado")) {
						modelo.setDerecho(fn.castBigDecimal( fn.castDouble( newcontenido.split("\n")[i].split("fraccionado")[1].replace("###", "").trim())));
					}
					if(newcontenido.split("\n")[i].contains("Expedición")) {
						modelo.setRecargo(fn.castBigDecimal( fn.castDouble( newcontenido.split("\n")[i].split("Expedición")[1].replace("###", "").trim())));
					}
					if(newcontenido.split("\n")[i].contains("I.V.A.")) {
						modelo.setIva(fn.castBigDecimal( fn.castDouble( newcontenido.split("\n")[i].split("I.V.A.")[1].replace("###", "").trim())));
					}
					if(newcontenido.split("\n")[i].contains("Total")) {
						modelo.setPrimaTotal(fn.castBigDecimal( fn.castDouble( newcontenido.split("\n")[i].split("Total")[1].replace("###", "").trim())));
					}
				}
				
			}
			
			/*nombre del  agente*/
		inicio = contenido.indexOf("Nombre del agente");
		fin = contenido.indexOf("AVISO###IMPORTANTE:");
		if(inicio >  0 && fin >  0 && inicio < fin) {
			newcontenido = contenido.substring(inicio, fin).replace("@@@", "").replace("\r", "");
			for (int i = 0; i < newcontenido.split("\n").length; i++) {
				if(newcontenido.split("\n")[i].contains("Nombre del agente")) {
					modelo.setAgente(newcontenido.split("\n")[i+1].split("###")[1]);
				}

			}
		}
			
			//proceso de ubicaciones

			
			List<EstructuraUbicacionesModel> ubicaciones = new ArrayList<>();
			inicio = contenido.indexOf("riesgo");
			fin = contenido.indexOf("Coaseguro");
			if(inicio >  0 && fin >  0 && inicio < fin) {
				newcontenido = contenido.substring(inicio, fin).replace("\r", "");
				EstructuraUbicacionesModel ubicacion = new EstructuraUbicacionesModel();
				for (int i = 0; i < newcontenido.split("\n").length; i++) {
	
					
					if(newcontenido.split("\n")[i].contains("C.P.")) {
						ubicacion.setCp(newcontenido.split("\n")[i].split("C.P.")[1]);
					}
					if(newcontenido.split("\n")[i].contains("Giro")) {
						ubicacion.setGiro(newcontenido.split("\n")[i].split("Giro")[1]);
					}					
					if(newcontenido.split("\n")[i].contains("Incendio")) {
						ubicacion.setTechos(fn.material(newcontenido.split("\n")[i].split("Incendio")[1]));
					}
					if(newcontenido.split("\n")[i].contains("pisos")) {
						ubicacion.setNiveles(Integer.parseInt(newcontenido.split("\n")[i].split("incendio:")[1].trim()));
					}			
				}
				ubicaciones.add(ubicacion);
				modelo.setUbicaciones(ubicaciones);
			}
			
			
			List<EstructuraCoberturasModel> coberturas = new ArrayList<>();
			String  result="";
			
			for (int i = 0; i < contenido.split("Secciones amparadas").length; i++) {
				if(contenido.split("Secciones amparadas")[i].contains("Coaseguro") ) {
					result +=contenido.split("Secciones amparadas")[i].split("Página")[0];
	   
				}
 			
								
			}
			
			String seccion="";

			result = result.replace("\r", "").replace("@@@", "");
			for (int i = 0; i < result.split("\n").length; i++) {
				

				if( result.split("\n")[i].contains("Secciones") || result.split("\n")[i].contains("Prima")) {
					
				}else {
					EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();
					if(result.split("\n")[i].contains("SECCION")) {
						seccion = result.split("\n")[i].split("SECCION")[1].split(":")[0];
					}
					int x= result.split("\n")[i].split("###").length;
					if(result.split("\n")[i].contains("SECCION") ) {
						
					}else {
						if(x  > 2){

							cobertura.setSeccion(seccion);
				
							if( x == 4) {
								cobertura.setNombre(result.split("\n")[i].split("###")[result.split("\n")[i].split("###").length-4]);
								cobertura.setSa(result.split("\n")[i].split("###")[result.split("\n")[i].split("###").length-1]);
								cobertura.setCoaseguro(result.split("\n")[i].split("###")[result.split("\n")[i].split("###").length-2]);
								cobertura.setDeducible(result.split("\n")[i].split("###")[result.split("\n")[i].split("###").length-3]);
							}
							if(x == 3) {
								cobertura.setNombre(result.split("\n")[i].split("###")[result.split("\n")[i].split("###").length-4]);
								cobertura.setSa(result.split("\n")[i].split("###")[result.split("\n")[i].split("###").length-1]);
								cobertura.setCoaseguro(result.split("\n")[i].split("###")[result.split("\n")[i].split("###").length-2]);
							}
						
							coberturas.add(cobertura);
						}
					}
					
					
				}						
			}
			
		
			modelo.setCoberturas(coberturas);
			
			
			
			return modelo;
			
		} catch (Exception ex) {
			modelo.setError(
					AbaDiversosModel.this.getClass().getTypeName() + " | " + ex.getMessage() + " | " + ex.getCause());
			return modelo;
		}
	}
	

}
