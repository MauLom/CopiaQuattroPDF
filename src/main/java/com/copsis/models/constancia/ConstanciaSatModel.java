package com.copsis.models.constancia;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraConstanciaSatModel;
import com.copsis.models.RegimenFiscalAndTipoPersona;
import com.copsis.services.RegimenFiscalService;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ConstanciaSatModel {	
	@Autowired
	private RegimenFiscalService regimenFiscalService;
	
	private DataToolsModel dataToolsModel = new DataToolsModel();
	private EstructuraConstanciaSatModel constancia = new EstructuraConstanciaSatModel();
	/*private String contenido = "";

	public ConstanciaSatModel(String contenido) {
		this.contenido = contenido;
	}*/

	public EstructuraConstanciaSatModel procesar(String contenido) {
		int beginIndex = 0;
		int endIndex = 0;
		boolean nombre =false;
		StringBuilder newcontenido = new StringBuilder();
		
		contenido = dataToolsModel.remplazarMultiple(contenido, dataToolsModel.remplazosGeneralesV2());
		

		try {
			
			beginIndex = contenido.indexOf("Identificación del Contribuyente");
			endIndex = contenido.indexOf("Datos del domicilio registrado");
			newcontenido.append( dataToolsModel.extracted(beginIndex, endIndex, contenido));
			
			constancia.setRegimenDeCapital("NO APLICA");
			
			for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
			
				if(newcontenido.toString().split("\n")[i].contains("RFC:")) {
					constancia.setRfc(newcontenido.toString().split("\n")[i].split("RFC:")[1].replace("###", "").trim());
				}
				
				if(newcontenido.toString().split("\n")[i].contains("Régimen") && newcontenido.toString().split("\n")[i].contains("Capital:")) {
					constancia.setRegimenDeCapital(newcontenido.toString().split("\n")[i].split("Capital:")[1].replace("###", " ").trim());
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

			
			beginIndex = contenido.indexOf("Datos del domicilio registrado");
			endIndex = contenido.indexOf("Actividad Económica");
			newcontenido = new StringBuilder();
			newcontenido.append( dataToolsModel.extracted(beginIndex, endIndex, contenido));
			
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
			
			beginIndex = contenido.indexOf("Regímenes:");
			//endIndex = contenido.length();
			endIndex = contenido.indexOf("Obligaciones:");
			if(endIndex == -1) {
				endIndex = contenido.indexOf("Sus datos personales son incorporados y protegidos en los sistemas del SAT".replace(" ", "###"));
			}
			
			String regimenes = dataToolsModel.extracted(beginIndex, endIndex, contenido);
			List<String> regimenesList = new ArrayList<>();
			
			for (int i = 0; i < regimenes.split("\n").length; i++) {
				if(!regimenes.split("\n")[i].contains("Regímenes:") 
						&& !regimenes.split("\n")[i].contains("Fecha")
						&& !regimenes.split("\n")[i].contains("Inicio")
						&& !regimenes.split("\n")[i].contains("Fin")) {
					String row = regimenes.split("\n")[i];
					String strRegimen = row.split(dataToolsModel.obtenVigePoliza(row).get(0))[0].replace("###", "").trim();					
					RegimenFiscalAndTipoPersona regimen = regimenFiscalService.get(strRegimen);
					//log.info("regimen: {}", regimen);	
					if(!regimen.getRegimenFiscal().equals("")) { 
						regimenesList.add(regimen.getRegimenFiscal());
						// se sobreescribe el último tip ode persona
						constancia.setTipoPersona(regimen.getTipo());
					}
				}				
			}
			
			if(!regimenesList.isEmpty()) {
				constancia.setRegimenFiscal(regimenesList);
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
