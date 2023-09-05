package com.copsis.models.sura;

import java.util.ArrayList;
import java.util.List;

import com.copsis.constants.ConstantsValue;
import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraCoberturasModel;
import com.copsis.models.EstructuraJsonModel;
import com.copsis.models.EstructuraUbicacionesModel;

public class SuraDiversosModel {

	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
	private String contenido;

	public SuraDiversosModel(String contenidox) {
		this.contenido = contenidox;
	}

	public EstructuraJsonModel procesar() {
		int inicio;
		int fin;
		StringBuilder newcontenido = new StringBuilder();
		contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());

		try {

			modelo.setTipo(7);
			modelo.setCia(88);

			inicio = contenido.indexOf("Seguro Múltiple Familiar");
			fin = contenido.indexOf("Ubicación de los bienes asegurados");
			if(inicio == -1) {
				inicio = contenido.indexOf("Múltiple Empresarial Riesgos");
			}
		
			newcontenido.append(fn.extracted(inicio, fin, contenido));

			for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {             
				if (newcontenido.toString().split("\n")[i].contains("Datos del asegurado")) {
					modelo.setCteNombre(newcontenido.toString().split("\n")[i + 1].split("###")[0]);
				}
				if (newcontenido.toString().split("\n")[i].contains("Moneda")) {
					modelo.setMoneda(fn.buscaMonedaEnTexto(newcontenido.toString().split("\n")[i + 1]));
				}
				if (newcontenido.toString().split("\n")[i].contains("Póliza no.") && newcontenido.toString().split("\n")[i + 1].contains("C.P")) {
					modelo.setPoliza(newcontenido.toString().split("\n")[i + 1].split("###")[newcontenido.toString().split("\n")[i + 1].split("###").length - 1]);
					modelo.setCp(newcontenido.toString().split("\n")[i + 1].split("C.P.")[1].trim().substring(0, 5));
				}				
				if (modelo.getPoliza().length() == 0 && newcontenido.toString().split("\n")[i].contains("Póliza no.")) {
					modelo.setPoliza(newcontenido.toString().split("\n")[i + 2].split("###")[newcontenido.toString().split("\n")[i + 2].split("###").length - 1]);
					modelo.setCp(newcontenido.toString().split("\n")[i + 2].split("C.P.")[1].trim().substring(0, 5));
				}

				if (newcontenido.toString().split("\n")[i].contains("Forma de pago")
						&& newcontenido.toString().split("\n")[i].contains("Fecha de emisión")) {
					modelo.setFormaPago(fn.formaPagoSring(newcontenido.toString().split("\n")[i + 1]));
					modelo.setFechaEmision(fn.formatDateMonthCadena(fn.obtenVigePoliza(newcontenido.toString().split("\n")[i + 1]).get(0)));
				}
				if(modelo.getFormaPago() == 0 && newcontenido.toString().split("\n")[i].contains("Forma de pago") &&  newcontenido.toString().split("\n")[i].contains("Expedición")) {
					modelo.setFormaPago(fn.formaPagoSring(newcontenido.toString().split("\n")[i + 1]));
					modelo.setFechaEmision(fn.formatDateMonthCadena(fn.obtenVigePoliza(newcontenido.toString().split("\n")[i + 1]).get(0)));
				}

				if (newcontenido.toString().split("\n")[i].contains("Vigencia desde")) {
					modelo.setVigenciaDe(fn.formatDateMonthCadena(fn.obtenVigePoliza(newcontenido.toString().split("\n")[i]).get(0)));
				}
				if (newcontenido.toString().split("\n")[i].contains("Hasta las")) {
					modelo.setVigenciaA(fn.formatDateMonthCadena(fn.obtenVigePoliza(newcontenido.toString().split("\n")[i]).get(0)));
				}
				
				if (newcontenido.toString().split("\n")[i].contains("R.F.C.")) {
					modelo.setRfc(newcontenido.toString().split("\n")[i].split("R.F.C.")[1].trim());
				}
				if (newcontenido.toString().split("\n")[i].contains("R.F.C:") && newcontenido.toString().split("\n")[i].contains("Vigencia")) {
					modelo.setRfc(newcontenido.toString().split("\n")[i].split("R.F.C:")[1].split("Vigencia")[0].replace("###", "").trim());
				}
			}

			inicio = contenido.indexOf("Ubicación de los bienes asegurados");
			fin = contenido.indexOf("Secciones contratadas");
			if(fin == -1 ) {
				fin = contenido.indexOf("Coberturas contratadas");
			}


			List<EstructuraUbicacionesModel> ubicaciones = new ArrayList<>();

			EstructuraUbicacionesModel ubicacion = new EstructuraUbicacionesModel();
			newcontenido = new StringBuilder();
			newcontenido.append(fn.extracted(inicio, fin, contenido));
			for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
			
				if (newcontenido.toString().split("\n")[i].contains("Ubicación de los bienes")) {
					ubicacion.setNombre(newcontenido.toString().split("\n")[i + 1].split("###")[0]);
				}
				if (newcontenido.toString().split("\n")[i].contains("C.P.")) {
					List<String> valores = fn.obtenerListNumeros2(newcontenido.toString().split("\n")[i]);
					if(valores.isEmpty()){
						valores = fn.obtenerListNumeros2(newcontenido.toString().split("\n")[i+1]);
					}
					if(!valores.isEmpty()){
						modelo.setCp(valores.get(0));
					}
					
				}
				if (newcontenido.toString().split("\n")[i].contains("CONSTRUCCIÓN")) {
					ubicacion.setMuros(1);
				}
				if (newcontenido.toString().split("\n")[i].contains("Pisos:") && fn.isNumeric(newcontenido.toString().split("\n")[i].split("Pisos:")[1].split("###")[1])) {
			
					ubicacion.setNiveles(
							fn.castInteger(newcontenido.toString().split("\n")[i].split("Pisos:")[1].split("###")[1]));
				}
			}
			ubicaciones.add(ubicacion);
			modelo.setUbicaciones(ubicaciones);

			
			List<EstructuraCoberturasModel> coberturas = new ArrayList<>();
			
			
			inicio = contenido.indexOf("Suma asegurada");
			if(inicio == -1 ) {
				inicio = contenido.indexOf("Coberturas contratadas");
			}			
			fin = contenido.indexOf("Prima neta");

			if(fin < inicio	){
				fin = contenido.lastIndexOf("Prima neta");
			}

			newcontenido = new StringBuilder();
			newcontenido.append(fn.extracted(inicio, fin, contenido));
			for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {				
				EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();
				if (!newcontenido.toString().split("\n")[i].contains("Suma asegurada")
						&& !newcontenido.toString().split("\n")[i].contains("SECCIÓN")
						&& !newcontenido.toString().split("\n")[i].contains("excluida")
						&& !newcontenido.toString().split("\n")[i].contains("asegurada")
						) {
	
					switch (newcontenido.toString().split("\n")[i].split("###").length) {
					case 2:
						cobertura.setNombre(newcontenido.toString().split("\n")[i].split("###")[0]);
						
						coberturas.add(cobertura);						
						break;
					case 3:
						if(newcontenido.toString().split("\n")[i].split("###")[0].length() < 5) {
							cobertura.setNombre(newcontenido.toString().split("\n")[i].split("###")[1]);	
						}else {
							cobertura.setNombre(newcontenido.toString().split("\n")[i].split("###")[0]);
						}
						cobertura.setSa(newcontenido.toString().split("\n")[i].split("###")[2]);
						coberturas.add(cobertura);
						break;
					case 4:
						cobertura.setNombre(newcontenido.toString().split("\n")[i].split("###")[0] +" " + newcontenido.toString().split("\n")[i].split("###")[1]);
						cobertura.setSa(newcontenido.toString().split("\n")[i].split("###")[2]);
						coberturas.add(cobertura);
						break;
					default:
						break;
					}
					
				}

			}
			modelo.setCoberturas(coberturas);
			
		
			inicio = contenido.indexOf("Prima neta");
			fin = contenido.indexOf("Pág. 2");
			newcontenido = new StringBuilder();
			newcontenido.append(fn.extracted(inicio, fin, contenido));			
			for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {	
				
				if(newcontenido.toString().split("\n")[i].contains("Prima neta") && newcontenido.toString().split("\n")[i].contains("financiamiento")) {						
					List<String> valores = fn.obtenerListNumeros(newcontenido.toString().split("\n")[i+1]);					
                    modelo.setPrimaneta(fn.castBigDecimal(fn.castDouble(valores.get(0))));
					   modelo.setDerecho(fn.castBigDecimal(fn.castDouble(valores.get(2))));
                    modelo.setRecargo(fn.castBigDecimal(fn.castDouble(valores.get(1))));
                    modelo.setIva(fn.castBigDecimal(fn.castDouble(valores.get(3))));                    
                    modelo.setPrimaTotal(fn.castBigDecimal(fn.castDouble(valores.get(4))));
                				
				}				
			}
			
			
			

			inicio = contenido.indexOf(ConstantsValue.AGENTE);			
			fin = contenido.indexOf("En cumplimiento ");
			
			newcontenido = new StringBuilder();
			newcontenido.append(fn.extracted(inicio, fin, contenido));
			for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {				
				if(newcontenido.toString().split("\n")[i].contains("Agente:")){
					modelo.setCveAgente(newcontenido.toString().split("\n")[i].split("Agente:")[1].trim().split(" ")[0]);
					modelo.setAgente(newcontenido.toString().split("\n")[i].split(modelo.getCveAgente())[1].trim());
				}
			}
			

			return modelo;
		} catch (Exception ex) {	
			ex.printStackTrace();	
			modelo.setError(
					SuraDiversosModel.this.getClass().getTypeName() + " | " + ex.getMessage() + " | " + ex.getCause());
			return modelo;
		}
	}

}
