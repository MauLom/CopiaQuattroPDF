package com.copsis.models;

public class ImportacionValidacionModel {

	public String isValidImportacion(EstructuraJsonModel poliza) {
		String respuesta = "";
		try {

			if(poliza.getPoliza().length() == 0) {
				respuesta ="La poliza esta vacia\n";
			}
			if(poliza.getCp().length() == 0) {
				respuesta +="El cp esta vacio\n";
			}
			if(poliza.getCteNombre().length() == 0) {
				respuesta +="El campo  getCteNombre no debe estar vacio\n";
			}
			if(poliza.getCoberturas().isEmpty()) {
				respuesta +="La poliza debe contener coberturas\n";
			}
			
			if(poliza.getVigenciaA().length() == 0  || poliza.getVigenciaDe().length() == 0  || poliza.getFechaEmision().length() == 0) {
				respuesta +="El campo fecha no puede estar vacio\n";
			}
			if(poliza.getFormaPago() == 0){
				respuesta +="La forma de pago esta vacio \n";
			}
			if(poliza.getMoneda() == 0){
				respuesta +="La moneda no esta definida\n";
			}
			
			if(poliza.getPrimaneta().doubleValue() == 0 || poliza.getPrimaTotal().doubleValue() == 0  ) {
				respuesta +="El campo  prima o prima total no pueden estar cero\n";
			
			}
			
			if(poliza.getCteDireccion().length() == 0 &&  poliza.getCteDireccion().length() > 200 ) {
				respuesta +="La direccion no puede estar vacia o  tener mas de 200 caracteres\n";
			}
			
			if(poliza.getAgente() .length() == 0 || poliza.getCveAgente().length() == 0) {
				respuesta +="El campo  agente o cveAgente no pueden estar vacios\n";
			}
			if(poliza.getPlan().length() == 0 ) {
				respuesta +="Alerta solo revisar si trae plan la poliza\n";			
			}
			
			if(poliza.getTipo() == 1) {//autos
				if(poliza.getModelo() == 0) {
					respuesta +="Alerta solo revisar si trae *modelo la poliza\n";			
				}
				if(poliza.getMarca().length() == 0) {
					respuesta +="Alerta solo revisar si trae *marca la poliza\n";			
				}
				if(poliza.getSerie().length() == 0) {
					respuesta +="Alerta solo revisar si trae *serie la poliza\n";			
				}
				if(poliza.getMotor().length() == 0) {
					respuesta +="Alerta solo revisar si trae *motor la poliza\n";			
				}
				if(poliza.getPlacas().length() == 0) {
					respuesta +="Alerta solo revisar si trae *placas la poliza\n";			
				}
				if(poliza.getDescripcion().length() == 0) {
					respuesta +="Alerta solo revisar si trae descripcion del auto la poliza\n";			
				}
			}
			
			if(poliza.getTipo() == 3 && poliza.getAsegurados().isEmpty()) {//salud		
					respuesta +="Alerta solo revisar si trae *Asegurados la poliza\n";						
			}
			if(poliza.getTipo() == 3 && !poliza.getAsegurados().isEmpty()) {
				for (int i = 0; i <  poliza.getAsegurados().size(); i++) {
				  if(poliza.getAsegurados().get(i).getNacimiento().isEmpty()) {
					  respuesta +="La fecha nacimiento esta vacia\n";
					  break;
				  }
				}
			}
			
			if(poliza.getTipo() == 5) {
				  respuesta +="Solo revisar,hay casos donde solo se tiene la prima total\n";
				  respuesta +="Alerta solo revisar si trae *Benficiarios la poliza\n";	
			}
			
			if(poliza.getTipo() == 5) {
				
			}
			
			
			
			
			 
			respuesta +="Clase==> "+  poliza.getError() +"\n" ;		
			

			return respuesta;
		} catch (Exception e) {
			return respuesta;
		}
	
	}
}
