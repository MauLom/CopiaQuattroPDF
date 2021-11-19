package com.copsis.models.aig;

import java.util.ArrayList;
import java.util.List;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraAseguradosModel;
import com.copsis.models.EstructuraJsonModel;

public class AigSaludModel {
	// Clases
	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
	// Varaibles
	private String contenido = "";
	private static final String PLAN = "Plan:";
	public AigSaludModel(String contenido) {
		this.contenido = contenido;
	}
	
	public EstructuraJsonModel procesar() {
		contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());
		try {
			String newcontenido = "";
			int inicio = 0;
			int fin = 0;

			//tipo
			modelo.setTipo(3);
			//cia
			modelo.setCia(3);
			//Datos del Contractante
			
			inicio = contenido.indexOf("CARÁTULA DE LA PÓLIZA");
			fin = contenido.indexOf("Información de Asegurados Adicionales");
			if (inicio > 0 && fin > 0 && inicio < fin) {
				newcontenido = contenido.substring(inicio, fin).replace("@@@", "").replace("\r", "").replace("12:15", "");
				for (int i = 0; i < newcontenido.split("\n").length; i++) {
					if( newcontenido.split("\n")[i].contains("Póliza:") &&  newcontenido.split("\n")[i].contains("Producto")) {
						modelo.setPoliza(newcontenido.split("\n")[i].split("Póliza:")[1].split("Producto")[0].replace("###", "").trim());						
					}
					if( newcontenido.split("\n")[i].contains("Emisión:") && newcontenido.split("\n")[i].contains(PLAN)){
						modelo.setPlan(newcontenido.split("\n")[i].split(PLAN)[1].replace("###", ""));
						modelo.setFechaEmision(fn.formatDateMonthCadena(newcontenido.split("\n")[i].split("Emisión:")[1].split(PLAN)[0].replace("###", "").trim() ));
					}
					if( newcontenido.split("\n")[i].contains("Inicio de Viaje:") ) {
						modelo.setVigenciaDe(fn.formatDateMonthCadena(newcontenido.split("\n")[i].split("Viaje:")[1].replace("###", "")));
					}
					
					if (newcontenido.split("\n")[i].contains("Fin de Viaje:")) {
						modelo.setVigenciaA(fn.formatDateMonthCadena(newcontenido.split("\n")[i].split("Viaje:")[1].replace("###", "")));
					}
					if (newcontenido.split("\n")[i].contains("Nombre Completo:")) {
						modelo.setCteNombre(newcontenido.split("Nombre Completo:")[1].replace("###", "").split("Prima")[0]);
						modelo.setPrimaneta(fn.castBigDecimal(fn.castDouble(newcontenido.split("\n")[i].split("Prima:")[1].replace("###", ""))));
					}
					if (newcontenido.split("\n")[i].contains("Impuesto")) {
						modelo.setIva(fn.castBigDecimal(fn.castDouble(newcontenido.split("\n")[i].split("Impuesto:")[1].replace("###", ""))));
					}
					if (newcontenido.split("\n")[i].contains("Prima Total:")) {
						modelo.setIva(fn.castBigDecimal(fn.castDouble(newcontenido.split("\n")[i].split("Prima Total:")[1].replace("###", ""))));
					}
				
				}
			}
			
			inicio = contenido.indexOf("Asegurados Adicionales");
			fin = contenido.indexOf("Beneficiarios");
			if (inicio > 0 && fin > 0 && inicio < fin) {
				List<EstructuraAseguradosModel> asegurados = new ArrayList<>();
				newcontenido = contenido.substring(inicio, fin).replace("@@@", "").replace("\r", "").replace("12:15", "");
				for (int i = 0; i < newcontenido.split("\n").length; i++) {
					EstructuraAseguradosModel asegurado = new EstructuraAseguradosModel();
					if(newcontenido.split("\n")[i].split("-").length > 2) {				
						asegurado.setNombre( newcontenido.split("\n")[i].split("###")[1]);
						asegurado.setSexo( fn.sexo(newcontenido.split("\n")[i].split("###")[2]).booleanValue() ? 1:0);
						asegurado.setNacimiento( fn.formatDateMonthCadena( newcontenido.split("\n")[i].split("###")[3]));
						asegurados.add(asegurado);
					}
				}
				modelo.setAsegurados(asegurados);
			}
				
			return modelo;
		} catch (Exception ex) {
			modelo.setError(AigSaludModel.this.getClass().getTypeName() + " - catch:" + ex.getMessage() + " | "
					+ ex.getCause());
			return modelo;
		}

	}

}
