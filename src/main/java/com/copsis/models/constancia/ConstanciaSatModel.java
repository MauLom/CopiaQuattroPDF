package com.copsis.models.constancia;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraConstanciaSatModel;

public class ConstanciaSatModel {

	private DataToolsModel dataToolsModel = new DataToolsModel();
	private EstructuraConstanciaSatModel constancia = new EstructuraConstanciaSatModel();
	private String contenido = "";

	public ConstanciaSatModel(String contenido) {
		this.contenido = contenido;
	}

	public EstructuraConstanciaSatModel procesar() {
		int inicio = 0;
		int fin = 0;
		boolean nombre =false;
		StringBuilder newcontenido = new StringBuilder();
		
		contenido = dataToolsModel.remplazarMultiple(contenido, dataToolsModel.remplazosGenerales());
		

		try {
			
			inicio = contenido.indexOf("Identificación del Contribuyente");
			fin = contenido.indexOf("Datos del domicilio registrado");
			newcontenido.append( dataToolsModel.extracted(inicio, fin, contenido));
			
			for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
			
				if(newcontenido.toString().split("\n")[i].contains("RFC:")) {
					constancia.setRfc(newcontenido.toString().split("\n")[i].split("RFC:")[1].replace("###", "").trim());
				}
				
				if(newcontenido.toString().split("\n")[i].contains("CURP:")) {
					constancia.setCurp(newcontenido.toString().split("\n")[i].split("CURP:")[1].replace("###", "").trim());
				}
				
				if(newcontenido.toString().split("\n")[i].contains("Nombre") && nombre == false) {
					constancia.setNombre(newcontenido.toString().split("\n")[i].split("Nombre")[1].replace("###", " ").replace("s:", "").trim());
					nombre = true;
				}
				
				if(newcontenido.toString().split("\n")[i].contains("Primer") && newcontenido.toString().split("\n")[i].contains("Apellido:")) {
					constancia.setApellidoP(newcontenido.toString().split("\n")[i].split("Apellido:")[1].replace("###", "").trim());
				}
				
				if(newcontenido.toString().split("\n")[i].contains("Segundo") && newcontenido.toString().split("\n")[i].contains("Apellido:")) {
					constancia.setApellidoM(newcontenido.toString().split("\n")[i].split("Apellido:")[1].replace("###", "").trim());
				}
				
				if(newcontenido.toString().split("\n")[i].contains("Fecha") && newcontenido.toString().split("\n")[i].contains("operaciones:")) {
					constancia.setFechaOperaciones(newcontenido.toString().split("\n")[i].split("operaciones:")[1].replace("###", " ").trim());
				}
				
				
				if(newcontenido.toString().split("\n")[i].contains("Segundo") && newcontenido.toString().split("\n")[i].contains("Apellido:")) {
					constancia.setApellidoM(newcontenido.toString().split("\n")[i].split("Apellido:")[1].replace("###", "").trim());
				}
				
				
				if(newcontenido.toString().split("\n")[i].contains("Estatus") && newcontenido.toString().split("\n")[i].contains("padrón:")) {
					constancia.setStatusPadron(newcontenido.toString().split("\n")[i].split("padrón:")[1].replace("###", "").trim());
				}
				
				if(newcontenido.toString().split("\n")[i].contains("Fecha") && newcontenido.toString().split("\n")[i].contains("estado:")) {
					constancia.setFechaEstado(newcontenido.toString().split("\n")[i].split("estado:")[1].replace("###", " ").trim());
				}
				
				if(newcontenido.toString().split("\n")[i].contains("Nombre") && newcontenido.toString().split("\n")[i].contains("Comercial:") && newcontenido.toString().split("\n")[i].split("Comercial")[1].length() > 4) {
					constancia.setNombreComercial(newcontenido.toString().split("\n")[i].split("Comercial:")[1].replace("###", "").trim());
				}				
			}

			
			inicio = contenido.indexOf("Datos del domicilio registrado");
			fin = contenido.indexOf("Actividad Económica");
			newcontenido = new StringBuilder();
			newcontenido.append( dataToolsModel.extracted(inicio, fin, contenido));
			
			for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
			
				if(newcontenido.toString().split("\n")[i].contains("Código") && newcontenido.toString().split("\n")[i].contains("Postal:") &&
						newcontenido.toString().split("\n")[i].contains("Tipo") && newcontenido.toString().split("\n")[i].contains("Vialidad:")		) {
					constancia.setCp(newcontenido.toString().split("\n")[i].split("Postal:")[1].split("Tipo")[0].replace("###", "").trim());
					constancia.setTipoVialidad(newcontenido.toString().split("\n")[i].split("Vialidad:")[1].replace("###", " ").trim());
				}
				
				if(newcontenido.toString().split("\n")[i].contains("Número") && newcontenido.toString().split("\n")[i].contains("Exterior:") &&
						newcontenido.toString().split("\n")[i].contains("Nombre") && newcontenido.toString().split("\n")[i].contains("Vialidad:")		) {
					constancia.setNombreVialidad(newcontenido.toString().split("\n")[i].split("Vialidad:")[1].split("Número")[0].replace("###", "").trim());
					constancia.setNumeroExterior(newcontenido.toString().split("\n")[i].split("Exterior:")[1].replace("###", " ").trim());
				}
				
				if(newcontenido.toString().split("\n")[i].contains("Número") && newcontenido.toString().split("\n")[i].contains("Interior:") &&
						newcontenido.toString().split("\n")[i].contains("Nombre") && newcontenido.toString().split("\n")[i].contains("Colonia:")		) {
					constancia.setNumeroInterior(newcontenido.toString().split("\n")[i].split("Interior:")[1].split("Nombre")[0].replace("###", "").trim());
					constancia.setColonia(newcontenido.toString().split("\n")[i].split("Colonia:")[1].replace("###", " ").trim());
				}
				
				if(newcontenido.toString().split("\n")[i].contains("Nombre") && newcontenido.toString().split("\n")[i].contains("Localidad:") &&
						newcontenido.toString().split("\n")[i].contains("Nombre###del") && newcontenido.toString().split("\n")[i].contains("Territorial:")		) {
					constancia.setNumeroInterior(newcontenido.toString().split("\n")[i].split("Localidad:")[1].split("Nombre###del")[0].replace("###", "").trim());
					constancia.setMunicipio(newcontenido.toString().split("\n")[i].split("Territorial:")[1].replace("###", " ").trim());
				}
				
				if(newcontenido.toString().split("\n")[i].contains("Entidad") && newcontenido.toString().split("\n")[i].contains("Federativa:") &&
						newcontenido.toString().split("\n")[i].contains("Entre") && newcontenido.toString().split("\n")[i].contains("Calle:")		) {
					constancia.setEstado(newcontenido.toString().split("\n")[i].split("Federativa:")[1].split("Calle")[0].replace("###", "").trim());
					constancia.setEntreCalle(newcontenido.toString().split("\n")[i].split("Calle:")[1].replace("###", " ").trim());
				}
				
				if(newcontenido.toString().split("\n")[i].contains("Entidad") && newcontenido.toString().split("\n")[i].contains("Federativa:") &&
						newcontenido.toString().split("\n")[i].contains("Entre") && newcontenido.toString().split("\n")[i].contains("Calle:")		) {
					constancia.setEstado(newcontenido.toString().split("\n")[i].split("Federativa:")[1].split("Calle")[0].replace("###", "").trim());
					constancia.setEntreCalle(newcontenido.toString().split("\n")[i].split("Calle:")[1].replace("###", " ").trim());
				}
				
				if(newcontenido.toString().split("\n")[i].contains("Correo") && newcontenido.toString().split("\n")[i].contains("Electrónico:") &&
						newcontenido.toString().split("\n")[i].contains("Calle")		) {
					constancia.setYCalle(newcontenido.toString().split("\n")[i].split("Calle:")[1].split("Correo")[0].replace("###", " ").trim());
					constancia.setCorreo(newcontenido.toString().split("\n")[i].split("Electrónico:")[1].replace("###", " ").trim());
				}
				
				if(newcontenido.toString().split("\n")[i].contains("Fijo") && newcontenido.toString().split("\n")[i].contains("Lada:") &&
						newcontenido.toString().split("\n")[i].contains("Número:") ) {
					constancia.setTelefonoLada(newcontenido.toString().split("\n")[i].split("Lada:")[1].split("Número:")[0].replace("###", "").trim());
					constancia.setTelefonoFijo(newcontenido.toString().split("\n")[i].split("Número:")[1].replace("###", " ").trim());
				}
				
				if(newcontenido.toString().split("\n")[i].contains("Móvil") && newcontenido.toString().split("\n")[i].contains("Lada:") &&
						newcontenido.toString().split("\n")[i].contains("Número:") ) {
					constancia.setMovilLada(newcontenido.toString().split("\n")[i].split("Lada:")[1].split("Número:")[0].replace("###", "").trim());
					constancia.setTelefonoMovil(newcontenido.toString().split("\n")[i].split("Número:")[1].replace("###", " ").trim());
				}
				
				
				if(newcontenido.toString().split("\n")[i].contains("Estado###del###domicilio:")  && newcontenido.toString().split("\n")[i+1].contains("contribuyente")  ) {
					constancia.setEstadoDomicilio(newcontenido.toString().split("\n")[i+1].split("Estado")[0].replace("###", " ").trim());
					constancia.setEstadoContribuyente(newcontenido.toString().split("\n")[i+2].replace("###", " ").trim());
				}
				
				
				
				
			}
			
			
			return constancia;
		} catch (Exception ex) {
			ex.printStackTrace();
			constancia.setError(ConstanciaSatModel.this.getClass().getTypeName() + " | " + ex.getMessage() + " | "
					+ ex.getCause());
			return constancia;
		}
	}

}
