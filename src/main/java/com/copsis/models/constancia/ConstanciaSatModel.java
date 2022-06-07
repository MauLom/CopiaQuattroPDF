package com.copsis.models.constancia;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.copsis.controllers.forms.PdfForm;
import com.copsis.models.CardSettings;
import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraConstanciaSatModel;
import com.copsis.models.RegimenFiscalPropsDto;
import com.copsis.services.RegimenFiscalService;
import com.copsis.services.WebhookService;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ConstanciaSatModel {	
	@Autowired
	private RegimenFiscalService regimenFiscalService;
	
	@Autowired
	private WebhookService webhookService;
	
	private DataToolsModel dataToolsModel = new DataToolsModel();

	public EstructuraConstanciaSatModel procesar(String contenido, PdfForm pdfForm) {
		EstructuraConstanciaSatModel constancia = new EstructuraConstanciaSatModel();
		int beginIndex = 0;
		int endIndex = 0;
		boolean nombre =false;
		StringBuilder newcontenido = new StringBuilder();
		
		contenido = dataToolsModel.remplazarMultiple(contenido, dataToolsModel.remplazosGeneralesV2());
		

		try {
			String identificacionDelContribuyente = "Identificación del Contribuyente";
			String datosDelDomicilioRegistrado = "Datos del domicilio registrado";
			String datosDeUbiacion = "Datos de Ubicación";
			String personaMoral = "Moral";
			String personaFisica = "Física";
			
			beginIndex = contenido.indexOf(identificacionDelContribuyente);
			endIndex = contenido.indexOf(datosDelDomicilioRegistrado);
			if(endIndex == -1) {
				endIndex = contenido.indexOf(datosDeUbiacion);
			}
			
			// Extracción regresa ### en lugar de espacios
			if(beginIndex == -1 || endIndex == -1) {
				beginIndex = contenido.indexOf(identificacionDelContribuyente.replace(" ", "###"));
				endIndex = contenido.indexOf(datosDelDomicilioRegistrado.replace(" ", "###"));
				if(endIndex == -1) {
					endIndex = contenido.indexOf(datosDeUbiacion.replace(" ", "###"));
				}
			}			
			
			newcontenido.append( dataToolsModel.extracted(beginIndex, endIndex, contenido));
			
			constancia.setRegimenDeCapital("NO APLICA");
			
			for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
			
				if(newcontenido.toString().split("\n")[i].contains("RFC:")) {
					String rfc = newcontenido.toString().split("\n")[i].split("RFC:")[1].replace("###", "").trim();
					constancia.setRfc(rfc);					
					constancia.setTipoPersona(rfc.length() == 12 ? personaMoral : personaFisica);
				}
				
				if(newcontenido.toString().split("\n")[i].contains("Denominación-Razón###Social:")) {
					constancia.setRazonSocial(newcontenido.toString().split("\n")[i].split("Social:")[1].replace("###", " ").trim());
				}
				
				if(newcontenido.toString().split("\n")[i].contains("Régimen") && newcontenido.toString().split("\n")[i].contains("Capital:")) {
					constancia.setRegimenDeCapital(newcontenido.toString().split("\n")[i].split("Capital:")[1].replace("###", " ").trim());
				}
				
				if(newcontenido.toString().split("\n")[i].contains("CURP:")) {
					constancia.setCurp(newcontenido.toString().split("\n")[i].split("CURP:")[1].replace("###", "").trim());
				}
				
				if(newcontenido.toString().split("\n")[i].contains("Nombre:") && nombre == false) {
					constancia.setNombre(newcontenido.toString().split("\n")[i].split("Nombre:")[1].replace("###", " ").replace("s:", "").trim());
					nombre = true;
				} else if(newcontenido.toString().split("\n")[i].contains("Nombre (s):") && nombre == false) {
					constancia.setNombre(newcontenido.toString().split("\n")[i].split("Nombre \\(s\\):")[1].replace("###", " ").trim());
					nombre = true;
				} else if(newcontenido.toString().split("\n")[i].contains("Nombre###(s):") && nombre == false) {					
					String[] arrNombre = newcontenido.toString().split("\n")[i].split("Nombre###\\(s\\):");					
					constancia.setNombre(arrNombre[1].replace("###", " ").trim());
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
					constancia.setNombreComercial(newcontenido.toString().split("\n")[i].split("Comercial:")[1].replace("###", " ").trim());
				}				
			}
			
			beginIndex = contenido.indexOf(datosDelDomicilioRegistrado);
			if(beginIndex == -1) {
				beginIndex = contenido.indexOf(datosDeUbiacion);
			}
			endIndex = contenido.indexOf("Actividad Económica");
			if(endIndex == -1) {
				endIndex = contenido.indexOf("Regímenes:");
			}
			
			// Extracción regresa ### en lugar de espacios
			if(beginIndex == -1 || endIndex == -1) {
				beginIndex = contenido.indexOf(datosDelDomicilioRegistrado.replace(" ", "###"));
				if(beginIndex == -1) {
					beginIndex = contenido.indexOf(datosDeUbiacion.replace(" ", "###"));
				}
				endIndex = contenido.indexOf("Actividad Económica".replace(" ", "###"));
				if(endIndex == -1) {
					endIndex = contenido.indexOf("Regímenes:");
				}
			}			
			
			newcontenido = new StringBuilder();
			newcontenido.append( dataToolsModel.extracted(beginIndex, endIndex, contenido));
			
			for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
				//log.info("LINEA: {}", newcontenido.toString().split("\n")[i]);
				if(newcontenido.toString().split("\n")[i].contains("Código") && newcontenido.toString().split("\n")[i].contains("Postal:") &&
						newcontenido.toString().split("\n")[i].contains("Tipo") && newcontenido.toString().split("\n")[i].contains("Vialidad:")		) {
					constancia.setCp(newcontenido.toString().split("\n")[i].split("Postal:")[1].split("Tipo")[0].replace("###", "").trim());
					String[] vialidadArr = newcontenido.toString().split("\n")[i].split("Vialidad:");
					constancia.setTipoVialidad(vialidadArr.length > 1 ? vialidadArr[1].replace("###", " ").trim() : "");
				}
				
				if(newcontenido.toString().split("\n")[i].contains("Número") && newcontenido.toString().split("\n")[i].contains("Exterior:") &&
						newcontenido.toString().split("\n")[i].contains("Nombre") && newcontenido.toString().split("\n")[i].contains("Vialidad:")		) {
					constancia.setNombreVialidad(newcontenido.toString().split("\n")[i].split("Vialidad:")[1].split("Número")[0].replace("###", " ").trim());
					String[] noExteriorArr = newcontenido.toString().split("\n")[i].split("Exterior:");
					constancia.setNumeroExterior(noExteriorArr.length > 1 ? noExteriorArr[1].replace("###", " ").trim() : "");
				}
				
				if(newcontenido.toString().split("\n")[i].contains("Número") && newcontenido.toString().split("\n")[i].contains("Interior:") &&
						newcontenido.toString().split("\n")[i].contains("Nombre") && newcontenido.toString().split("\n")[i].contains("Colonia:")		) {					
					constancia.setNumeroInterior(newcontenido.toString().split("\n")[i].split("Interior:")[1].split("Nombre")[0].replace("###", " ").trim());
					String[] nombreColoniaArr = newcontenido.toString().split("\n")[i].split("Colonia:");
					constancia.setColonia(nombreColoniaArr.length > 1 ? nombreColoniaArr[1].replace("###", " ").trim() : "");
				}
				
				if(newcontenido.toString().split("\n")[i].contains("Nombre") && newcontenido.toString().split("\n")[i].contains("Localidad:") &&
						newcontenido.toString().split("\n")[i].contains("Nombre###del") && newcontenido.toString().split("\n")[i].contains("Territorial:")		) {					
					constancia.setLocalidad(newcontenido.toString().split("\n")[i].split("Localidad:")[1].split("Nombre###del")[0].replace("###", " ").trim());
					//constancia.setMunicipio(newcontenido.toString().split("\n")[i].split("Territorial:")[1].replace("###", " ").trim());
					if(constancia.getLocalidad().length()  ==  0 && newcontenido.toString().split("\n")[i+1].length() >  0 ) {
						StringBuilder municipio = new StringBuilder();
						municipio.append(newcontenido.toString().split("\n")[i].split("Territorial:")[1].replace("###", " ").trim());
						// validar que la siguiente línea no sea entidad federativa, entonces se considerará parte del municipio						
						if(!newcontenido.toString().split("\n")[i + 1].contains("Entidad") && !newcontenido.toString().split("\n")[i + 1].contains("Federativa:") &&
						!newcontenido.toString().split("\n")[i + 1].contains("Entre") && !newcontenido.toString().split("\n")[i + 1].contains("Calle:")) {
							municipio.append(" ");
							municipio.append(newcontenido.toString().split("\n")[i+1].replace("###", " ").trim());
						}
                        constancia.setMunicipio(municipio.toString());                                                
                    }else {
                        //constancia.setMunicipio(newcontenido.toString().split("\n")[i].split("Territorial:")[1].replace("###", " ").trim());
                        StringBuilder municipio = new StringBuilder();
						municipio.append(newcontenido.toString().split("\n")[i].split("Territorial:")[1].replace("###", " ").trim());
						// validar que la siguiente línea no sea entidad federativa, entonces se considerará parte del municipio						
						if(!newcontenido.toString().split("\n")[i + 1].contains("Entidad") && !newcontenido.toString().split("\n")[i + 1].contains("Federativa:") &&
						!newcontenido.toString().split("\n")[i + 1].contains("Entre") && !newcontenido.toString().split("\n")[i + 1].contains("Calle:")) {
							municipio.append(" ");
							municipio.append(newcontenido.toString().split("\n")[i+1].replace("###", " ").trim());
						}
                        constancia.setMunicipio(municipio.toString());
                    }
				}
				
				if(newcontenido.toString().split("\n")[i].contains("Entidad") && newcontenido.toString().split("\n")[i].contains("Federativa:") &&
						newcontenido.toString().split("\n")[i].contains("Entre") && newcontenido.toString().split("\n")[i].contains("Calle:")		) {
					constancia.setEstado(newcontenido.toString().split("\n")[i].split("Federativa:")[1].split("Entre###Calle")[0].replace("###", " ").trim());
					String[] entreCalleArr = newcontenido.toString().split("\n")[i].split("Calle:");
					constancia.setEntreCalle(entreCalleArr.length > 1 ? entreCalleArr[1].replace("###", " ").trim() : "");
				}
				
				/*if(newcontenido.toString().split("\n")[i].contains("Entidad") && newcontenido.toString().split("\n")[i].contains("Federativa:") &&
						newcontenido.toString().split("\n")[i].contains("Entre") && newcontenido.toString().split("\n")[i].contains("Calle:")		) {
					constancia.setEstado(newcontenido.toString().split("\n")[i].split("Federativa:")[1].split("Entre###Calle")[0].replace("###", " ").trim());
					constancia.setEntreCalle(newcontenido.toString().split("\n")[i].split("Calle:")[1].replace("###", " ").trim());
				}*/
				
				if(newcontenido.toString().split("\n")[i].contains("Correo") && newcontenido.toString().split("\n")[i].contains("Electrónico:") &&
						newcontenido.toString().split("\n")[i].contains("Calle")		) {
					constancia.setYCalle(newcontenido.toString().split("\n")[i].split("Calle:")[1].split("Correo")[0].replace("###", " ").trim());
					String[] correoArr = newcontenido.toString().split("\n")[i].split("Electrónico:");
					constancia.setCorreo(correoArr.length > 1 ? correoArr[1].replace("###", " ").trim() : "");
				}
				
				if(newcontenido.toString().split("\n")[i].contains("Fijo") && newcontenido.toString().split("\n")[i].contains("Lada:") &&
						newcontenido.toString().split("\n")[i].contains("Número:") ) {
					constancia.setTelefonoLada(newcontenido.toString().split("\n")[i].split("Lada:")[1].split("Número:")[0].replace("###", "").trim());
					String[] telefonoArr = newcontenido.toString().split("\n")[i].split("Número:");
					constancia.setTelefonoFijo(telefonoArr.length > 1 ? telefonoArr[1].replace("###", " ").trim() : "");
				}
				
				if(newcontenido.toString().split("\n")[i].contains("Móvil") && newcontenido.toString().split("\n")[i].contains("Lada:") &&
						newcontenido.toString().split("\n")[i].contains("Número:") ) {
					constancia.setMovilLada(newcontenido.toString().split("\n")[i].split("Lada:")[1].split("Número:")[0].replace("###", "").trim());
					String[] telefonoMovilArr = newcontenido.toString().split("\n")[i].split("Número:");
					constancia.setTelefonoMovil(telefonoMovilArr.length > 1 ? telefonoMovilArr[1].replace("###", " ").trim() : "");
				}
				
				if(newcontenido.toString().split("\n")[i].contains("Estado###del###domicilio:")  && newcontenido.toString().split("\n")[i].contains("contribuyente")  ) {
					constancia.setEstadoDomicilio(newcontenido.toString().split("\n")[i].split("###del###domicilio:")[1].split("Estado")[0].replace("###", " ").trim());
					
					String[] estadoContribuyenteArr = newcontenido.toString().split("\n")[i].split("###del###domicilio:")[1].split("domicilio:");
					StringBuilder estadoContribuyente = new StringBuilder();
					estadoContribuyente.append(estadoContribuyenteArr.length > 1 ? estadoContribuyenteArr[1].replace("###", " ").trim() : "");
					// validar si la siguiente línea no es actividades económicas, entonces se considerará parte del estado del contribuyente
					if( (newcontenido.toString().split("\n").length - 1) > i ) {
						if(!newcontenido.toString().split("\n")[i+1].contains("Actividades") && !newcontenido.toString().split("\n")[i+1].contains("Económicas")
								&& !newcontenido.toString().split("\n")[i+1].contains("Regímenes:")) {
							estadoContribuyente.append(" ");
							estadoContribuyente.append(newcontenido.toString().split("\n")[i+1].replace("###", " ").trim());
						}
					}					
					constancia.setEstadoContribuyente(estadoContribuyente.toString());
				}
				//log.info("row: {}", newcontenido.toString().split("\n")[i]);
			}
			
			if(constancia.getRfc().equals("")) {
				String publicErrorMessage = "No se encontraron datos en el rango de búsqueda";
				String privateErrorMessage = String.format(publicErrorMessage.concat(": '%s' y '%s'"), identificacionDelContribuyente, datosDelDomicilioRegistrado);
				sendWebhookMessage(pdfForm, privateErrorMessage);
				constancia.setError(publicErrorMessage);
				return constancia;
			}

			if(constancia.getTipoPersona().equals(personaFisica)) {
				// validaciones persona física
				if(constancia.getNombre().equals("") || constancia.getApellidoP().equals("") || constancia.getApellidoM().equals("") || constancia.getCp().equals("")) {
					String publicErrorMessage = "No fué posible leer alguno de los datos: nombre(s), apellído paterno, apellído materno, Código postal";
					sendWebhookMessage(pdfForm, publicErrorMessage);
					constancia.setError(publicErrorMessage);
					return constancia;
				}
			} else {
				// validaciones persona moral
				if(constancia.getRazonSocial().equals("") || constancia.getRegimenDeCapital().equals("") || constancia.getCp().equals("")) {
					String publicErrorMessage = "No fué posible leer alguno de los datos: Razón Social, Régimen Capital, Código postal";
					sendWebhookMessage(pdfForm, publicErrorMessage);
					constancia.setError(publicErrorMessage);
					return constancia;
				}
			}
			
			
			beginIndex = contenido.indexOf("Regímenes:");
			//endIndex = contenido.length();
			endIndex = contenido.indexOf("Obligaciones:");
			if(endIndex == -1) {
				endIndex = contenido.indexOf("Sus datos personales son incorporados y protegidos en los sistemas del SAT".replace(" ", "###"));
			}
			
			String regimenes = dataToolsModel.extracted(beginIndex, endIndex, contenido);
			List<RegimenFiscalPropsDto> regimenesList = new ArrayList<>();
			
			for (int i = 0; i < regimenes.split("\n").length; i++) {
				if(!regimenes.split("\n")[i].contains("Regímenes:") 
						&& !regimenes.split("\n")[i].contains("Fecha")
						&& !regimenes.split("\n")[i].contains("Inicio")
						&& !regimenes.split("\n")[i].contains("Fin")
						&& !regimenes.split("\n")[i].contains("Página [")
						) {
					String row = regimenes.split("\n")[i];
					String[] resultPattern = row.split("[0-9]{2,2}-[0-9]{2,2}-[0-9]{4,4}");
					String searchTerm = "";
					if(resultPattern.length > 0) {
						searchTerm = resultPattern[0].replace("###", " ").trim();
					}					
					RegimenFiscalPropsDto regimen = regimenFiscalService.get(searchTerm);
					//log.info("regimen: {}", regimen);	
					if(regimen.getDescripcion() != null && !regimen.getDescripcion().equals("")) { 
						RegimenFiscalPropsDto regimenDto = new RegimenFiscalPropsDto();
						regimenDto.setClave(regimen.getClave());
						regimenDto.setDescripcion(regimen.getDescripcion());
						regimenesList.add(regimenDto);						
					}
				}				
			}
			
			if(regimenesList.isEmpty()) {
				sendWebhookMessage(pdfForm, "No se logro extraer informacion de los regímenes");
				constancia.setError(ConstanciaSatModel.this.getClass().getTypeName() + " | " + "No se logro extraer informacion de los regímenes" + " | ");
				return constancia;
			} else {
				constancia.setRegimenFiscal(regimenesList);
			}
			
			return constancia;
		} catch (Exception ex) {
			sendWebhookMessage(pdfForm, ex.getMessage());
			constancia.setError(ConstanciaSatModel.this.getClass().getTypeName() + " | " + ex.getMessage() + " | " + ex.getCause());
			return constancia;
		}
	}

	private void sendWebhookMessage(PdfForm pdfForm, String errorMessage) {
		try  {
			CardSettings cardSettings = CardSettings.builder()
					.fileUrl(pdfForm.getUrl())
					.sourceClass(ConstanciaSatModel.class.getName())
					.exceptionMessage(errorMessage)
					.build();
			webhookService.send(cardSettings);
		} catch(Exception e) {
			// do nothing
		}
	}
}
