package com.copsis.models;

public class ImportacionValidacionModel {

	public String isValidImportacion(EstructuraJsonModel poliza) {
		String respuesta = "";
		try {

			if(poliza.getPoliza().length() == 0) {
				respuesta ="La poliza no puede estar vacia\n";
			}
			if(poliza.getCp().length() == 0) {
				respuesta +="El cp no puede estar vacia\n";
			}
			if(poliza.getPlan().length() == 0) {
				respuesta +="El plan no puede estar vacion\n";
			}
			if(poliza.getCoberturas().size() == 0) {
				respuesta +="La poliza debe contener coberturas\n";
			}
			
			if(poliza.getVigenciaA().length() == 0  || poliza.getVigenciaDe().length() == 0  || poliza.getFechaEmision().length() == 0) {
				respuesta +="El campo fecha no puede estar vacio\n";
			}
			if(poliza.getFormaPago() == 0){
				respuesta +="La forma de pago no puede estar vacia \n";
			}
			if(poliza.getMoneda() == 0){
				respuesta +="La moneda no esta definida\n";
			}
			
			
			
			
			
			
		

			return respuesta;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return respuesta;
		}
	
	}
}
