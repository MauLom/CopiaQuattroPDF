package com.copsis.models.aig;

import java.util.ArrayList;
import java.util.List;

import com.copsis.constants.ConstantsValue;
import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraAseguradosModel;
import com.copsis.models.EstructuraCoberturasModel;
import com.copsis.models.EstructuraJsonModel;


public class AigSaludBModel {
	// Clases
	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
	// Varaibles
	private String contenido = "";
	
	public AigSaludBModel(String contenido) {
		this.contenido = contenido;
	}

	public EstructuraJsonModel procesar() {
		contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());
		contenido = contenido.replace("CARÁTULA DE PÓLIZA", "CARÁTULA DE LA PÓLIZA");
	

		String newcontenido = "";

		StringBuilder newdireccion = new StringBuilder();
		int inicio = 0;
		int fin = 0;

		try {

			// tipo
			modelo.setTipo(3);
			// cia
			modelo.setCia(3);
			// Datos del Contractante

			inicio = contenido.indexOf("CARÁTULA DE LA PÓLIZA");
			fin = contenido.indexOf("BENEFICIOS CUBIERTOS");
     
			if (inicio > 0 && fin > 0 && inicio < fin) {
				newcontenido = contenido.substring(inicio, fin).replace("@@@", "").replace("\r", "").replace(ConstantsValue.TIME,"");
				modelo.setFormaPago(fn.formaPagoSring(newcontenido));
				modelo.setMoneda(1);
				for (int i = 0; i < newcontenido.split("\n").length; i++) {
					if (newcontenido.split("\n")[i].contains("NÚMERO DE PÓLIZA")
							&& newcontenido.split("\n")[i].contains("PAQUETE")) {
						int sp = newcontenido.split("\n")[i + 1].split("###").length;
						if (sp == 7) {
							modelo.setPoliza(newcontenido.split("\n")[i + 1].split("###")[4].replace("###", "").trim());
						}
					}

					if (newcontenido.split("\n")[i].contains("NOMBRE") && 
						newcontenido.split("\n")[i].contains("CONTRATANTE") && 
						newcontenido.split("\n")[i + 1].contains("NOMBRE:") && 
						newcontenido.split("\n")[i + 1].contains("R.F.C:")) {
						
						modelo.setCteNombre(newcontenido.split("\n")[i + 1].split("NOMBRE:")[1].split("R.F.C:")[0].replace("###", "").trim());
					}
					if (newcontenido.split("\n")[i].contains("CALLE:")) {
						newdireccion.append(newcontenido.split("\n")[i].split("CALLE:")[1]);
					}
					if (newcontenido.split("\n")[i].contains("POBLACIÓN:")) {
						newdireccion.append(newcontenido.split("\n")[i].split("POBLACIÓN:")[1].replace("###", ""));
					}
					if (newcontenido.split("\n")[i].contains("ESTADO:")) {
						newdireccion.append(newcontenido.split("\n")[i].split("ESTADO:")[1].replace("###", ""));
						modelo.setCteDireccion(newdireccion.toString().replace("###", "").trim());
					}

					if (newcontenido.split("\n")[i].contains("C.P.")) {
						modelo.setCp(newcontenido.split("\n")[i].split("C.P.")[1].trim().substring(0, 5));
					}
					if (newcontenido.split("\n")[i].split("-").length > 3) {
						modelo.setVigenciaDe(
								fn.formatDateMonthCadena(newcontenido.split("\n")[i].split("###")[0].trim()));
						modelo.setVigenciaA(
								fn.formatDateMonthCadena(newcontenido.split("\n")[i].split("###")[1].trim()));
						modelo.setFechaEmision(modelo.getVigenciaDe());

					}
				
					if (newcontenido.split("\n")[i].contains("PRIMA NETA")
							&& newcontenido.split("\n")[i].contains("EXPEDICIÓN")
							&& newcontenido.split("\n")[i].contains("PRIMA TOTAL")) {
						int sp = newcontenido.split("\n")[i + 1].split("###").length;
						
						if (sp == 5) {
							modelo.setPrimaneta(fn.castBigDecimal(
									fn.castDouble(newcontenido.split("\n")[i + 1].split("###")[0].replace("###", ""))));
							modelo.setRecargo(fn.castBigDecimal(
									fn.castDouble(newcontenido.split("\n")[i + 1].split("###")[1].replace("###", ""))));
							modelo.setDerecho(fn.castBigDecimal(
									fn.castDouble(newcontenido.split("\n")[i + 1].split("###")[2].replace("###", ""))));
							modelo.setIva(fn.castBigDecimal(
									fn.castDouble(newcontenido.split("\n")[i + 1].split("###")[3].replace("###", ""))));
							modelo.setPrimaTotal(fn.castBigDecimal(
									fn.castDouble(newcontenido.split("\n")[i + 1].split("###")[4].replace("###", ""))));
						}
					}

					if (newcontenido.split("\n")[i].contains("FRACCIONAD") && newcontenido.split("\n")[i+1].contains("I.V.A.")){
						List<String> valores = fn.obtenerListNumeros(newcontenido.toString().split("\n")[i+2]);
						modelo.setPrimaneta(fn.castBigDecimal(fn.castDouble(valores.get(0))));
						modelo.setDerecho(fn.castBigDecimal(fn.castDouble(valores.get(2))));
						modelo.setRecargo(fn.castBigDecimal(fn.castDouble(valores.get(1))));
						modelo.setIva(fn.castBigDecimal(fn.castDouble(valores.get(3))));                    
						modelo.setPrimaTotal(fn.castBigDecimal(fn.castDouble(valores.get(4))));
					}

				}
			}

			StringBuilder coberturastxt = new StringBuilder();

			for (int j = 0; j < contenido.split(ConstantsValue.BENEFICIOS_CUBIERTOS).length; j++) {
				if (contenido.split(ConstantsValue.BENEFICIOS_CUBIERTOS)[j].contains("FECHA DE EXPEDICIÓN")) {
					coberturastxt.append(contenido.split(ConstantsValue.BENEFICIOS_CUBIERTOS)[j].split("FECHA DE EXPEDICIÓN")[0]);
				} else {
					if (contenido.split(ConstantsValue.BENEFICIOS_CUBIERTOS)[j].contains("Los datos personales serán")) {
						coberturastxt.append(
								contenido.split(ConstantsValue.BENEFICIOS_CUBIERTOS)[j].split("Los datos personales serán")[0]);
					}
				}
			}

			if (coberturastxt.length() > 0) {
				List<EstructuraCoberturasModel> coberturas = new ArrayList<>();

				newcontenido = coberturastxt.toString().replace("@@@", "").replace(ConstantsValue.TIME, "");
				for (String x : newcontenido.split("\r\n")) {
					int sp = x.split("###").length;
					EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();
				    if(sp == 2 && !x.contains("COBERTURAS") && !x.contains("SUMAS ASEGURADAS")) {
				    	cobertura.setNombre(x.split("###")[0].trim());
				    	cobertura.setSa(fn.eliminaSpacios(x.split("###")[1].trim(), ' ', ""));
				    	coberturas.add(cobertura);	
				    }				    			
				}
				modelo.setCoberturas(coberturas);
			}

			inicio = contenido.indexOf("Agente de Seguro:");
			fin = contenido.indexOf("Reducción en prima ");
			if (inicio > 0 && fin > 0 && inicio < fin) {
				newcontenido = contenido.substring(inicio, fin).replace("@@@", "").replace("\r", "").replace("###", "");
				for (int i = 0; i < newcontenido.split("\n").length; i++) {					
					if(newcontenido.split("\n")[i].contains("Seguro:")) {
						modelo.setCveAgente(newcontenido.split("\n")[i].split("Seguro:")[1].trim().split(" ")[0].trim());
						modelo.setAgente(newcontenido.split("\n")[i].split(modelo.getCveAgente())[1].trim());
					}									
				}
			}

			inicio = contenido.indexOf("Nombre del Asegurado:");
			fin = contenido.indexOf(ConstantsValue.BENEFICIOS_CUBIERTOS);

			if (inicio > 0 && fin > 0 && inicio < fin) {
				List<EstructuraAseguradosModel> asegurados = new ArrayList<>();
				newcontenido = contenido.substring(inicio, fin).replace("@@@", "").replace("\r", "").replace(ConstantsValue.TIME,"");				
				for (int i = 0; i < newcontenido.split("\n").length; i++) {
					EstructuraAseguradosModel asegurado = new EstructuraAseguradosModel();
					if (newcontenido.split("\n")[i].contains("Asegurado:")) {
						asegurado.setNombre(newcontenido.split("\n")[i].split("Asegurado:")[1].trim());				
						asegurados.add(asegurado);
					}
				}
				modelo.setAsegurados(asegurados);
			}

			return modelo;
		} catch (Exception ex) {
			modelo.setError(AigSaludBModel.this.getClass().getTypeName() + " - catch:" + ex.getMessage() + " | "
					+ ex.getCause());
			return modelo;
		}

	}

}
