package com.copsis.models.aig;

import java.util.ArrayList;
import java.util.List;

import com.copsis.constants.ConstantsValue;
import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraCoberturasModel;
import com.copsis.models.EstructuraJsonModel;

public class AigAutosModel {

	// Clases
	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
	
	private static final String HORA_VIGENCIA = "23:59";

	
	public EstructuraJsonModel procesar(String contenido) {
		StringBuilder resultado = new StringBuilder();
		StringBuilder newCobertura = new StringBuilder();
		StringBuilder newcontenido = new StringBuilder();
		int inicio = 0;
		int fin = 0;
		contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());
		contenido = contenido.replace("DE LAS 12:00 HORAS del ", "").replace("A LAS 12:00 HORAS del", "###")
				.replace(" de ", "-")
				.replace("DAÑO MATERIAL", "DAÑO MATERIAL###")
				.replace("ROBO TOTAL", "ROBO TOTAL###")
				.replace("RESPONSABILIDAD CIVIL L.U.C.", "RESPONSABILIDAD CIVIL L.U.C.###")
				.replace("GASTOS MEDICOS OCUPANTES ", "GASTOS MEDICOS OCUPANTES ###")
				.replace("ASISTENCIA VIAL Y EN VIAJES", "ASISTENCIA VIAL Y EN VIAJES###")
				.replace("S.O.S. DEFENSA LEGAL Y FIANZ", " S.O.S. DEFENSA LEGAL Y FIANZ###")
				.replace("AUTO SUSTITUTO POR PERDIDA T", " AUTO SUSTITUTO POR PERDIDA T###")
				.replace("ELIMINACION DE DEDUCIBLE POR", " ELIMINACION DE DEDUCIBLE POR###")
				.replace("DEVOLUCION DE PRIMA POR P.T.", " DEVOLUCION DE PRIMA POR P.T.###");
		try {
			//tipo
			modelo.setTipo(1);

			//cia
			modelo.setCia(3);
			
			//Datos del contratante
			
			inicio  = contenido.indexOf("SEGURO DE AUTOMÓVILES");
			fin = contenido.indexOf("RIESGOS CUBIERTOS");
			
			if(fin == -1) {
				fin = contenido.indexOf("Coberturas");
			}

			if(inicio >  0 && fin > 0 && inicio < fin) {
				newcontenido.append(contenido.substring( inicio, fin).replace("@@@", "").replace("\r", "").replace("C.P", "C/P").replace("######", "###"));
				
				for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
			
					if(newcontenido.toString().split("\n")[i].contains("PÓLIZA:")) {					
						modelo.setPoliza(newcontenido.toString().split("\n")[i].split("PÓLIZA")[1].split("-")[2].trim());
					}else if(newcontenido.toString().split("\n")[i].contains("Póliza:") && newcontenido.toString().split("\n")[i].contains("Cobertura")) {
						modelo.setPoliza(newcontenido.toString().split("\n")[i].split("Póliza:")[1].split("Cobertura")[0].replace("###",""));
					}
					if(newcontenido.toString().split("\n")[i].contains("VIGENCIA DEL SEGURO")) {
						
						modelo.setVigenciaDe( fn.formatDateMonthCadena(newcontenido.toString().split("\n")[i+1].split("###")[0].trim()));
						 modelo.setVigenciaA( fn.formatDateMonthCadena(newcontenido.toString().split("\n")[i+1].split("###")[1].trim()));
					}else if(newcontenido.toString().split("\n")[i].contains("Vigencia") && newcontenido.toString().split("\n")[i].contains(HORA_VIGENCIA)) {
						String aux = "";
						if(newcontenido.toString().split("\n")[i].contains("desde:")) {
							aux = newcontenido.toString().split("\n")[i].split("Vigencia desde:")[1].split(HORA_VIGENCIA)[0].trim();
							modelo.setVigenciaDe(fn.formatDateMonthCadena(aux));
						}else if(newcontenido.toString().split("\n")[i].contains("hasta:")) {
							aux = newcontenido.toString().split("\n")[i].split("Vigencia hasta:")[1].split(HORA_VIGENCIA)[0].trim();
							modelo.setVigenciaA(fn.formatDateMonthCadena(aux));
						}
					}
					
					if(newcontenido.toString().split("\n")[i].contains("emisión:") && newcontenido.toString().split("\n")[i].contains("-") && newcontenido.toString().split("\n")[i].contains("Forma-pago")) {
						String fecha = newcontenido.toString().split("\n")[i].split("emisión:")[1].split("Forma-pago")[0].replace("###","").trim();
						if(fecha.split("-").length == 3) {
							modelo.setFechaEmision(fn.formatDateMonthCadena(fecha));
						}
					}
					if(modelo.getCteNombre().length() == 0 && newcontenido.toString().split("\n")[i].contains(ConstantsValue.DATOS_CONTRATANTE) && (i+1)<newcontenido.toString().split("\n").length) {
						if(newcontenido.toString().split("\n")[i+1].contains(ConstantsValue.NOMBRE2)) {
							modelo.setCteNombre(newcontenido.toString().split("\n")[i+1].split(ConstantsValue.NOMBRE2)[1].replace("###", "").trim());
						}
					}
					
					if(newcontenido.toString().split("\n")[i].contains(ConstantsValue.NOMBRE2) && newcontenido.toString().split("\n")[i].contains(ConstantsValue.RFC)) {
						modelo.setCteNombre(newcontenido.toString().split("\n")[i].split(ConstantsValue.NOMBRE2)[1].split(ConstantsValue.RFC)[0].replace("###", "").trim());
						modelo.setRfc(newcontenido.toString().split("\n")[i].split(ConstantsValue.RFC)[1].replace("###", ""));						
					}else if( newcontenido.toString().split("\n")[i].contains("RFC") &&  newcontenido.toString().split("\n")[i].contains("Tipo")){
						modelo.setRfc(newcontenido.toString().split("\n")[i].split("RFC")[1].split("Tipo")[0].replace(":","").replace("###", "").trim());
					}
					if(newcontenido.toString().split("\n")[i].contains("Código Postal:")) {
						modelo.setCp(newcontenido.toString().split("\n")[i].split("Código Postal:")[1].replace("###", "").trim());
					}else if(newcontenido.toString().split("\n")[i].contains("C/P") && newcontenido.toString().split("\n")[i].contains("###")) {
						String cp = newcontenido.toString().split("\n")[i].split("C/P")[1].split("###")[0].replace(":", "").replace(".","").trim();
						if(fn.isNumeric(cp)) {
							modelo.setCp(cp.replace("###", ""));
						}
					}
					
					if(newcontenido.toString().split("\n")[i].contains("Calle:")) {
						resultado = new StringBuilder();
						resultado.append(newcontenido.toString().split("\n")[i].split("Calle:")[1]);
					}
					
					if(newcontenido.toString().split("\n")[i].contains("Colonia:")) {
						resultado.append(newcontenido.toString().split("\n")[i].split("Colonia:")[1]);						
					}
                     if(newcontenido.toString().split("\n")[i].contains("Estado-Municipio:")) {
						modelo.setCteDireccion((resultado.append(newcontenido.toString().split("\n")[i]
						.split("Estado-Municipio:")[1]).toString().replace("###", " ").replace("Código-Asegurado:", "").trim()));
					}
                     
                    if(newcontenido.toString().split("\n")[i].contains("Domicilio") && modelo.getCteDireccion().length() == 0){
                    	int indexInicio = newcontenido.indexOf("Domicilio:");
                    	int indexFin = newcontenido.indexOf("C/P");
                    	if(indexInicio > -1 &&  indexInicio < indexFin) {
							String domiclio = newcontenido.substring(indexInicio + 10, indexFin).replace("Estado:", "")
									.replace("###", "").replace("\n", "").trim();
                    		modelo.setCteDireccion(domiclio.replace("###", " "));
                    	}
                    }
                     
                     if(newcontenido.toString().split("\n")[i].contains("Conductor Habitual:")) {
                    	 modelo.setConductor(newcontenido.toString().split("\n")[i].split("Habitual:")[1].replace("###", "").trim());
                    	 
                     }
					
					if (newcontenido.toString().split("\n")[i].contains("Marca:")) {
						String marca = newcontenido.toString().split("\n")[i].split("Marca:")[1];
						if(marca.contains("Modelo")) {
							marca = marca.split("Modelo")[0];
						}
						modelo.setMarca(marca.replace("###", "").trim());

					}
					if (newcontenido.toString().split("\n")[i].contains(ConstantsValue.MODELO) && newcontenido.toString().split("\n")[i].contains("Placa:")) {
						
						modelo.setModelo(fn.castInteger( newcontenido.toString().split("\n")[i].split(ConstantsValue.MODELO)[1].split("Placa:")[0].replace("###", "").trim()));
						modelo.setPlacas(newcontenido.toString().split("\n")[i].split("Placa:")[1].replace("###", "").trim());

					}
					// if(newcontenido.toString().split("\n")[i].contains(ConstantsValue.MODELO) && newcontenido.toString().split("\n")[i].contains("Tipo")) {
					// 	modelo.setModelo(fn.castInteger( newcontenido.toString().split("\n")[i].split(ConstantsValue.MODELO)[1].split("Tipo")[0].replace("###", "").trim()));
					// }
					// if (newcontenido.toString().split("\n")[i].contains("Motor:")) {
					// 	String texto = newcontenido.toString().split("\n")[i].split("Motor:")[1];
					// 	if(texto.contains("###Placas")) {
					// 		texto = texto.split("###Placas")[0];
					// 	}
                    //     modelo.setMotor(texto);
					// }
					// if (newcontenido.toString().split("\n")[i].contains("Serie:")) {
					// 	String serie = newcontenido.toString().split("\n")[i].split("Serie:")[1];
					// 	if(serie.contains("###Motor")) {
					// 		serie = serie.split("###Motor")[0].trim();
					// 	}
				    //      modelo.setSerie(serie);
					// }
					// if(modelo.getPlacas().length() == 0 && newcontenido.toString().split("\n")[i].contains("Placas") && newcontenido.toString().split("\n")[i].contains("Clave")) {
					// 	modelo.setPlacas(newcontenido.toString().split("\n")[i].split("Placas")[1].split("Clave")[0].replace("###","").replace(":","").trim());
					// }
					// if(newcontenido.toString().split("\n")[i].split("Descripción:").length> 1) {
					// 	modelo.setDescripcion(fn.eliminaSpacios(newcontenido.toString().split("\n")[i].split("Descripción:")[1].trim()).split("####")[0]);
					// }
				}				
			}
			
			inicio  = contenido.indexOf(ConstantsValue.MONEDA);
			fin = contenido.indexOf("Si su vehículo" );
			if(fin == -1) {
				fin = contenido.length();
			}

			if(inicio >  0 && fin > 0 && inicio < fin) {
				newcontenido = new StringBuilder();
				newcontenido.append(contenido.substring(inicio, fin).replace("Prima neta", "Prima Neta")
						.replace("expedición", ConstantsValue.EXPEDICION3).replace("I.V.A", "IVA")
						.replace("Prima total", ConstantsValue.PRIMA_TOTAL2).replace("fraccionado", ConstantsValue.FRACCIONADO).replace("@@@", "")
						.replace("\r", ""));

				for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
					if(newcontenido.toString().split("\n")[i].contains("Moneda:") && newcontenido.toString().split("\n")[i].contains("Prima Neta:")) {
						modelo.setMoneda(fn.moneda(newcontenido.toString().split("\n")[i].split("Moneda:")[1].split("Total")[0].trim()));
						modelo.setPrimaneta(fn.castBigDecimal(fn.castDouble(newcontenido.toString().split("\n")[i].split("Neta:")[1])));
					}else if(newcontenido.toString().split("\n")[i].contains("Prima Neta")) {
						modelo.setPrimaneta(fn.castBigDecimal(fn.castDouble(fn.gatos(newcontenido.toString().split("\n")[i].split("Neta")[1]))));
					}
					if(newcontenido.toString().split("\n")[i].contains(ConstantsValue.FRACCIONADO)) {
						String aux = newcontenido.toString().split("\n")[i].split(ConstantsValue.FRACCIONADO)[1].replace(":","");
						if(aux.split("###").length > 1) {
							aux = aux.split("###")[aux.split("###").length -1];
						}else {
							aux = aux.replace("###","");
						}
						modelo.setRecargo(fn.castBigDecimal(fn.castDouble(aux)));
					}
					if(newcontenido.toString().split("\n")[i].contains("Pago:") && newcontenido.toString().split("\n")[i].contains("Expedición:")) {
						modelo.setFormaPago( fn.formaPagoSring(newcontenido.toString().split("\n")[i].split("Pago:")[1].split("Gastos")[0].trim()));
						modelo.setDerecho(fn.castBigDecimal(fn.castDouble(newcontenido.toString().split("\n")[i].split("Expedición:")[1])));
						
					}else if(newcontenido.toString().split("\n")[i].contains(ConstantsValue.EXPEDICION3)) {
						String aux = newcontenido.toString().split("\n")[i].split(ConstantsValue.EXPEDICION3)[1].replace(":", "").replace("###", "");
						modelo.setDerecho(fn.castBigDecimal(fn.castDouble(aux)));
					}
					if (newcontenido.toString().split("\n")[i].contains("IVA") && !newcontenido.toString().split("\n")[i].contains("PRIVACIDAD")) {
						String aux = newcontenido.toString().split("\n")[i].split("IVA")[1].replace(":","");
						if(aux.split("###").length > 1) {
							aux = aux.split("###")[aux.split("###").length -1];
						}else {
							aux = aux.replace("###","");
						}
						modelo.setIva(fn.castBigDecimal(fn.castDouble(aux)));
					}

					if (newcontenido.toString().split("\n")[i].contains(ConstantsValue.PRIMA_TOTAL2)) {
						String aux = newcontenido.toString().split("\n")[i].split(ConstantsValue.PRIMA_TOTAL2)[1].replace(":", "").replace("###", "");
						modelo.setPrimaTotal(fn.castBigDecimal(fn.castDouble(aux)));
					}
				}
			}
			//forma de pago
			if(modelo.getFormaPago() == 0) {
				inicio = contenido.indexOf("Forma-pago");
				fin = contenido.indexOf(ConstantsValue.DATOS_CONTRATANTE);
				if(inicio > -1 && inicio < fin) {
					modelo.setFormaPago(fn.formaPagoSring(contenido.substring(inicio,fin)));
				}
			}
			//moneda
			if(modelo.getMoneda() == 0) {
				inicio = contenido.indexOf("Moneda");
				fin = contenido.indexOf(ConstantsValue.DATOS_CONTRATANTE);
				modelo.setMoneda(fn.buscaMonedaEnTexto(contenido.substring(inicio, fin)));
			}
			//PROCESO PARA LAS COBERTURAS
			for (int i = 0; i < contenido.split(ConstantsValue.RIESGOS_CUBIERTOS).length; i++) {		
				if(contenido.split(ConstantsValue.RIESGOS_CUBIERTOS)[i].contains(ConstantsValue.MONEDA)) {
					newCobertura.append(contenido.split(ConstantsValue.RIESGOS_CUBIERTOS)[i].split(ConstantsValue.MONEDA)[0]);
				}else
					if(contenido.split(ConstantsValue.RIESGOS_CUBIERTOS)[i].contains("En cumplimiento")) {
            	   newCobertura.append(contenido.split(ConstantsValue.RIESGOS_CUBIERTOS)[i].split("En cumplimiento")[0]);
				}
			}
			
			if(!contenido.contains(ConstantsValue.RIESGOS_CUBIERTOS)) {
				inicio = contenido.indexOf("Coberturas");
				fin = contenido.indexOf("Prima neta");
				if(inicio > -1 &&  inicio < fin) {
					newCobertura = new StringBuilder();
					newCobertura.append(contenido.substring(inicio, fin));
				}
			}
			
			String auxStr = newCobertura.toString();
			newCobertura = new StringBuilder();
			newCobertura.append(auxStr.replace("@@@", "").replace("\r", "").trim()
					.replace("VALOR GUIA EBC", "VALOR GUIA EBC###")
					.replace("AMPARADA", "AMPARADA###")
					.replace(".00", ".00###")
					.replace("0.0", "0.0###")
					.replace(" % ", " %###")
					.replace("FIANZ###A","FIANZA")
					.replace("ROBO TOTAL######", "ROBO TOTAL###")
					.replace(".0###0",".00"));
			if(newCobertura.length() > 0)
			{
				List<EstructuraCoberturasModel> coberturas = new ArrayList<>();
				for (int i = 0; i < newCobertura.toString().split("\n").length; i++) {
					EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();
					if (!newCobertura.toString().split("\n")[i].contains("Coberturas###Límite Máximo-Responsabilidad")
							&& !newCobertura.toString().split("\n")[i].contains("C.R: Centro-reparto")) {
						int sp = newCobertura.toString().split("\n")[i].split("###").length;
						if(sp > 2) {
							cobertura.setNombre(newCobertura.toString().split("\n")[i].split("###")[0].trim());
							cobertura.setSa(newCobertura.toString().split("\n")[i].split("###")[1].trim());
							cobertura.setDeducible(newCobertura.toString().split("\n")[i].split("###")[2].trim());
							coberturas.add(cobertura);
						}
					}
				}
				modelo.setCoberturas(coberturas);
				
			}

				if(modelo.getFechaEmision().length()  == 0){
				 	modelo.setFechaEmision(modelo.getVigenciaDe());	
				}
		
			return modelo;
			
		} catch (Exception ex) {
		
			modelo.setError(
				AigAutosModel.this.getClass().getTypeName() + " | " + ex.getMessage() + " | " + ex.getCause());
		return modelo;
	
		}
	}
}
