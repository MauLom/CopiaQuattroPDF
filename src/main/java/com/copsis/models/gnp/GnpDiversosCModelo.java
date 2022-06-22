package com.copsis.models.gnp;

import java.util.ArrayList;
import java.util.List;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraCoberturasModel;
import com.copsis.models.EstructuraJsonModel;
import com.copsis.models.EstructuraUbicacionesModel;

public class GnpDiversosCModelo {

	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
	
	private String contenido = "";

	
	public GnpDiversosCModelo(String contenido) {
		this.contenido = contenido;
		//this.ubicaciociones = ubicaciociones;
	}
	
	public  EstructuraJsonModel procesar (){
		 int inicio = 0;
		 int fin = 0;
		 
		 StringBuilder newcontenido = new StringBuilder();
		 contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales()).replace("INFORMACIÓN###ADICIONAL", "INFORMACIÓN ADICIONAL");
		 
		 try {
				modelo.setTipo(7);
				modelo.setCia(18);
		
				inicio = contenido.indexOf("CONTRATANTE");
				fin = contenido.indexOf("INFORMACIÓN ADICIONAL");
				newcontenido.append(fn.extracted(inicio, fin, contenido));
				for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {

					if(newcontenido.toString().split("\n")[i].contains("Cliente") && newcontenido.toString().split("\n")[i].contains("Nombre")) {
						modelo.setCteNombre(newcontenido.toString().split("\n")[i+1].split("###")[1].replace("###", "").trim());
					}
					if(newcontenido.toString().split("\n")[i].contains("R.F.C.") && newcontenido.toString().split("\n")[i+1].contains("Hasta")) {						
						modelo.setRfc(newcontenido.toString().split("\n")[i+2].split("###")[0]);
						modelo.setCteDireccion(newcontenido.toString().split("\n")[i+2].split("###")[1] +" "+ newcontenido.toString().split("\n")[i+3]);
					}
				
					if(newcontenido.toString().split("\n")[i].contains("RFC") && newcontenido.toString().split("\n")[i+1].contains("Hasta")) {						
						modelo.setRfc(newcontenido.toString().split("\n")[i+2].split("###")[0]);
						modelo.setCteDireccion(newcontenido.toString().split("\n")[i+2].split("###")[1] +" "+ newcontenido.toString().split("\n")[i+3]);
					}
					
							
					if(newcontenido.toString().split("\n")[i].contains("C.P.") && newcontenido.toString().split("\n")[i].split("C.P")[1].length() > 4 ) {
						modelo.setCp(newcontenido.toString().split("\n")[i].split("C.P.")[1].substring(0, 6).trim());
					}
					
					if(newcontenido.toString().split("\n")[i].contains("C.P.") && newcontenido.toString().split("\n")[i+1].contains("Descripción") ) {
						modelo.setCp(newcontenido.toString().split("\n")[i+1].split("Descripción")[0].replace("###", "").trim());
					}
					
					if(newcontenido.toString().split("\n")[i].contains("Desde") && newcontenido.toString().split("\n")[i].contains("del") ) {
						modelo.setVigenciaDe(fn.formatDateMonthCadena(newcontenido.toString().split("\n")[i].split("del")[1].replace("###", "").trim()));
					
					}
					if(newcontenido.toString().split("\n")[i].contains("Hasta") && newcontenido.toString().split("\n")[i].contains("del") ) {
						modelo.setVigenciaA(fn.formatDateMonthCadena(newcontenido.toString().split("\n")[i].split("del")[1].replace("###", "").trim()));
					}
					
					if(newcontenido.toString().split("\n")[i].contains("Prima###Neta") ) {	
						modelo.setPrimaneta(fn.castBigDecimal(fn.preparaPrimas(newcontenido.toString().split("\n")[i].split("Prima###Neta")[1].replace("###", "").trim())));
					}
					if(newcontenido.toString().split("\n")[i].contains("Fraccionado") ) {	
						modelo.setRecargo(fn.castBigDecimal(fn.preparaPrimas(newcontenido.toString().split("\n")[i].split("Fraccionado")[1].replace("###", "").trim())));
					}
					
					if(newcontenido.toString().split("\n")[i].contains("Derecho") ) {	
						modelo.setDerecho(fn.castBigDecimal(fn.preparaPrimas(newcontenido.toString().split("\n")[i].split("Póliza")[1].replace("###", "").trim())));
					}

					if(newcontenido.toString().split("\n")[i].contains("I.V.A.16%") ) {	
						modelo.setIva(fn.castBigDecimal(fn.preparaPrimas(newcontenido.toString().split("\n")[i].split("I.V.A.16%")[1].replace("###", "").trim())));
					}
					if(newcontenido.toString().split("\n")[i].contains("IVA 16%") ) {	
						modelo.setIva(fn.castBigDecimal(fn.preparaPrimas(newcontenido.toString().split("\n")[i].split("IVA 16%")[1].replace("###", "").trim())));
					}
					
					if(newcontenido.toString().split("\n")[i].contains("Importe") &&  newcontenido.toString().split("\n")[i].contains("Pagar") ) {	
						modelo.setPrimaTotal(fn.castBigDecimal(fn.preparaPrimas(newcontenido.toString().split("\n")[i].split("Pagar")[1].replace("###", "").trim())));
					}

				}
				
				if(modelo.getVigenciaDe().length() > 0) {
					modelo.setFechaEmision(modelo.getVigenciaDe());
				}
				
				
				
				if (contenido.split("RESUMEN###DE###LA###PÓLIZA")[0].length() > 0) {
					modelo.setPoliza(contenido.split("RESUMEN###DE###LA###PÓLIZA")[0].split("\n")[0].replace("@@@","").replace("\r", ""));
				}
				
				inicio = contenido.indexOf("INFORMACIÓN ADICIONAL");
				fin = contenido.indexOf("ste documento no");
				newcontenido = new StringBuilder();
				newcontenido.append(fn.extracted(inicio, fin, contenido));
				
				for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
					
					if(newcontenido.toString().split("\n")[i].contains("Pago") && newcontenido.toString().split("\n")[i].contains("Moneda")) {
						modelo.setMoneda(fn.buscaMonedaEnTexto((newcontenido.toString().split("\n")[i+1])));
						modelo.setFormaPago(fn.formaPagoSring((newcontenido.toString().split("\n")[i+1])));
					}
				}
				

				inicio = contenido.indexOf("Clave###Agente");
				fin = contenido.lastIndexOf("RESUMEN###DE###LA###PÓLIZA");
				
				newcontenido = new StringBuilder();									
				newcontenido.append(fn.extracted(inicio, fin, contenido));

				if(newcontenido.length() > 50) {
					modelo.setCveAgente(newcontenido.toString().split("\n")[1].split("###")[0].replace("###", "").trim());
					modelo.setAgente(newcontenido.toString().split("\n")[1].split("###")[1].replace("###", "").trim());
				}
			
				

				inicio = contenido.indexOf("DESGLOSE###DE###COBERTURAS");
				fin = contenido.indexOf("El###monto###de#");
				
				if(inicio == -1 && fin == -1) {
					inicio = contenido.indexOf("SECCIONES###CONTRATADAS");
					fin = contenido.indexOf("Este documento no acredita");
				}

				newcontenido = new StringBuilder();
				newcontenido.append(fn.extracted(inicio, fin, contenido));
				
				
				List<EstructuraCoberturasModel> coberturas = new ArrayList<>();
				for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
					
					EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();
					if(!newcontenido.toString().split("\n")[i].contains("DESGLOSE")
					&& !newcontenido.toString().split("\n")[i].contains("Coberturas")
					&& !newcontenido.toString().split("\n")[i].contains("Del Hogar")
					&& !newcontenido.toString().split("\n")[i].contains("SERVICIOS")
					&& !newcontenido.toString().split("\n")[i].contains("Salud")
					&& !newcontenido.toString().split("\n")[i].contains("Familiares")
					&& !newcontenido.toString().split("\n")[i].contains("Mascotas")
					&& !newcontenido.toString().split("\n")[i].contains("Notas")
					&& !newcontenido.toString().split("\n")[i].contains("Ver Cláusulas")
					&& !newcontenido.toString().split("\n")[i].contains("SECCIONES")
					&& !newcontenido.toString().split("\n")[i].contains("Especificación")
					&& !newcontenido.toString().split("\n")[i].contains("LC TRC")
					
					&& newcontenido.toString().split("\n")[i].length() > 3
					){
						
						
						switch (newcontenido.toString().split("\n")[i].split("###").length) {
						case 2:
							cobertura.setNombre(newcontenido.toString().split("\n")[i].split("###")[0]);
							cobertura.setSa(newcontenido.toString().split("\n")[i].split("###")[1]);
							coberturas.add(cobertura);
							break;
						case 3:
							cobertura.setNombre(newcontenido.toString().split("\n")[i].split("###")[1]);							
							coberturas.add(cobertura);
							break;	
						case 4:
							cobertura.setNombre(newcontenido.toString().split("\n")[i].split("###")[0]);
							cobertura.setSa(newcontenido.toString().split("\n")[i].split("###")[1]);
							cobertura.setDeducible(newcontenido.toString().split("\n")[i].split("###")[2]);
							cobertura.setCoaseguro(newcontenido.toString().split("\n")[i].split("###")[3]);
							coberturas.add(cobertura);
							break;	

						default:
							break;
						}
						
						
					}
				}
				modelo.setCoberturas(coberturas);
				inicio = contenido.indexOf("CARACTERÍSTICAS###DEL###RIESGO");
				fin = contenido.indexOf("indemnización");				
				newcontenido = new StringBuilder();
				newcontenido.append(fn.extracted(inicio, fin, contenido));
				List<EstructuraUbicacionesModel> ubicaciones = new ArrayList<>();
				EstructuraUbicacionesModel ubicacion = new EstructuraUbicacionesModel();
				for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
					if(newcontenido.toString().split("\n")[i].contains("Giro")) {
						ubicacion.setGiro(newcontenido.toString().split("\n")[i +1].replace("###", ""));
					}
					if(newcontenido.toString().split("\n")[i].contains("Número###de###pisos") && newcontenido.toString().split("\n")[i].contains("Techos")
					  && newcontenido.toString().split("\n")[i].contains("Muros")) {
						ubicacion.setNiveles(1);
						ubicacion.setTechos(1);
						ubicacion.setMuros(1);
					}
				}
				ubicaciones.add(ubicacion);
				modelo.setUbicaciones(ubicaciones);
				
				ubicaciones.add(ubicacion);
				modelo.setUbicaciones(ubicaciones);
	
			 return modelo;
		} catch (Exception e) {
	
			modelo.setError(GnpDiversosCModelo.this.getClass().getTypeName()+ " | "+e.getMessage()+" | "+e.getCause());
			return modelo; 
		}
		
	}

}
