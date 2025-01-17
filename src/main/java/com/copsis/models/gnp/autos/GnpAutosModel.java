package com.copsis.models.gnp.autos;


import java.util.ArrayList;
import java.util.List;

import com.copsis.constants.ConstantsValue;
import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraCoberturasModel;
import com.copsis.models.EstructuraJsonModel;
import com.copsis.models.EstructuraRecibosModel;

public class GnpAutosModel {
	// Clases
	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
	// Variables
	private String contenido = "";

	// constructor
	public GnpAutosModel(String contenido) {
		this.contenido = contenido;
	}

	public EstructuraJsonModel procesar() {

		int inicio = 0;
		int fin = 0;
		int donde = 0;
		int longitudTexto = 0;

		StringBuilder newcontenido = new StringBuilder();

		contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());
		contenido = contenido.replace("Importe###por###Pagar", "Importe por Pagar").replace("Derecho###de###Póliza",
				ConstantsValue.DERECHO_POLIZA)
				.replace("Desde###las###12###hrs###del", "Desde las 12 hrs del")
				.replace("Hasta###las###12###hrs###del", "Hasta las 12 hrs del")
		        .replace("I.V.A.","I.V.A.");

		try {

		
			modelo.setTipo(1);
			modelo.setCia(18);
			
			donde = 0;
			donde = fn.recorreContenido(contenido, ConstantsValue.NO_POLIZA);
			if (donde > 0) {
				for (String dato : contenido.split("@@@")[donde].split("\r\n")) {
					if (dato.contains(ConstantsValue.NO_POLIZA) && dato.split("###").length == 1) {
						modelo.setPoliza(dato.split("Póliza")[1].trim());
						break;
					}
				}
			}

			// cte_nombre
			// renovacion
			donde = 0;
			donde = fn.recorreContenido(contenido, "Código###de###Cliente###Nombre###Versión###Renovación");
			if (donde > 0 && contenido.split("@@@")[donde].split("\r\n").length == 1
					&& contenido.split("@@@")[donde + 1].split("\r\n").length > 1) {

				for (String dato : contenido.split("@@@")[donde + 1].split("\r\n")) {
					if (!dato.contains("Vigencia") && dato.split("###").length == 4) {
						modelo.setCteNombre(dato.split("###")[1].trim());
						modelo.setRenovacion(dato.split("###")[3].trim());
					}
				}
			}
			//cteNombre
			//idCte
			if(modelo.getCteNombre().length() == 0) {
				if(contenido.contains("Código###de###Cliente###Nombre###")) {
					String textoSiguienteLinea = contenido.split("Código###de###Cliente###Nombre###")[1].split("\n")[1];
					if(textoSiguienteLinea.split("###").length>1) {
						modelo.setIdCliente(textoSiguienteLinea.split("###")[0].trim());
						modelo.setCteNombre(textoSiguienteLinea.split("###")[1].trim());
					}
				}
			}

			// vigencia_de
			donde = 0;
			donde = fn.recorreContenido(contenido, "R.F.C.###Dirección###Desde");
			if (donde > 0 && contenido.split("@@@")[donde].split("\r\n").length == 1
					&& contenido.split("@@@")[donde].split("###").length == 3
					&& contenido.split("@@@")[donde].split("###")[2].contains("del")
					&& contenido.split("@@@")[donde].split("###")[2].split("-").length == 3) {

				modelo.setVigenciaDe(fn.formatDate(contenido.split("@@@")[donde].split("###")[2].split("del")[1].trim(),
						"dd-MM-yy"));

			}

			//Otro formato
			if(modelo.getVigenciaDe().length() == 0 && contenido.contains("Desde las 12 hrs del")) {
				String fecha = "";
				for(String texto:contenido.split("Desde las 12 hrs del")) {

					if(texto.split("\n")[0].split("-").length == 3) {
						fecha =  texto.split("\n")[0].replace("###", "").trim();
				
						if(fecha.split("-")[2].length() > 4 && fecha.split("-")[2].contains("Importe")) {
							fecha = fecha.split("Importe")[0];
						}
												
						modelo.setVigenciaDe(fn.formatDateMonthCadena(fecha));
					}
				}
			}
			
			inicio = contenido.indexOf("ersión###Renovación");
			if (inicio > -1 && modelo.getIdCliente().length() == 0) {
				newcontenido.append(contenido.substring(inicio + 19, inicio + 150).trim().split("\r\n")[0]
						.replace("@@@", "").trim());
				if (newcontenido.toString().split("###").length == 4) {
					modelo.setIdCliente(newcontenido.toString().split("###")[0].trim());
				}
			}

			// vigencia_a
			// cte_direccion
			// rfc
			// cp
			donde = 0;
			donde = fn.recorreContenido(contenido, "###Hasta las 12 hrs del");
			StringBuilder direccionCte = new StringBuilder();
			if (donde > 0 && contenido.split("@@@")[donde].split("\r\n").length > 1 || contenido.split("@@@")[donde].split("\n").length > 1) {
				String separador = contenido.split("@@@")[donde].split("\r\n").length > 1 ? "\r\n" : "\n";
				
				newcontenido = new StringBuilder();
				for (String dato : contenido.split("@@@")[donde].split(separador)) {

					if (dato.split("###").length == 3) {
						if (dato.split("###")[2].contains("del") && dato.split("###")[2].split("-").length == 3) {
							modelo.setVigenciaA(fn.formatDate(dato.split("###")[2].split("del")[1].trim(), "dd-MM-yy"));
							modelo.setRfc(dato.split("###")[0].trim());
							newcontenido.append(dato.split("###")[1].trim());
						}else if(dato.split("###")[1].contains("del") && dato.split("###")[2].split("-").length == 3) {
							modelo.setVigenciaA(fn.formatDate(dato.split("###")[2].trim(), "dd-MM-yy"));
							direccionCte.append(dato.split("###")[0]);
						}
					} else if (dato.split("###").length == 2) {
						if (dato.split("###")[1].contains("Duración:") && dato.split("###")[0].contains("C.P.")) {
							if (!dato.contains("C.P.###Duración")) {
								modelo.setCp(dato.split("###")[0].split("C.P.")[1].trim());
								newcontenido.append(" ").append(dato.split("###")[0].split("C.P.")[0].trim());
							}
						} else if (dato.split("###").length == 2 && dato.split("###")[0].trim().length() == 5
								&& fn.isNumeric(dato.split("###")[0].trim())) {
							modelo.setCp(dato.split("###")[0].trim());
						}else if(dato.contains(", C.P.###Duración") && direccionCte.length()>0){
							direccionCte.append(" ").append(dato.split(", C.P.###Duración")[0].trim());
						}
					}
				}
				modelo.setCteDireccion(newcontenido.toString().replace(", C.P.", "").trim());
				if(modelo.getCteDireccion().length() == 0 && direccionCte.length()>0) {
					modelo.setCteDireccion(direccionCte.toString());
				}

			}else {
				inicio = contenido.indexOf("R.F.C.###Dirección");
				fin = contenido.indexOf("VEHÍCULO###ASEGURADO");
				if(inicio > -1 && inicio < fin) {
					newcontenido = new StringBuilder();
					newcontenido.append(contenido.substring(inicio + 15,fin).replace("@@@", "").replace("C.P", "C/P"));

					String[] arrlineasTexto = newcontenido.toString().split("\n");
					int indexValorDireccion = 0;
					boolean encontroTextpCP = false;
					String direccion = "";
					for(int i=1;i<arrlineasTexto.length;i++) {
						if(arrlineasTexto[i].split("-").length == 3 && arrlineasTexto[i].split("###").length == 2) {
							if(tieneLongitudRFC(arrlineasTexto[i].split("###")[0].trim())) {
								modelo.setRfc(arrlineasTexto[i].split("###")[0].trim());
							}
							modelo.setVigenciaA(fn.formatDateMonthCadena(arrlineasTexto[i].split("###")[1].trim()));
						}else {
							if(arrlineasTexto[i].split("###").length < 4 && arrlineasTexto[i].split("###").length > 1 && !encontroTextpCP ){
								indexValorDireccion = arrlineasTexto[i].split("###").length -2;
								direccion = arrlineasTexto[i].split("###")[indexValorDireccion];
								if(direccion.contains(", C/P")) {
									String aux = direccion;
									direccion = direccion.split(", C/P")[0].trim();
									encontroTextpCP = true;
									if(fn.isNumeric(aux.split("C/P")[1].replace(".", "").trim())){
										modelo.setCp(aux.split("C/P")[1].replace(".", "").trim());
									}
								}
								direccionCte.append(" ").append(direccion);
							}
						}
					}
					
					modelo.setCteDireccion(direccionCte.toString().trim());					
				}
			}
			
			
			//Otro formato vigencia_A
			if(modelo.getVigenciaA().length() == 0 && contenido.contains("Hasta las 12 hrs del")) {
				String fecha = "";
				for(String texto:contenido.split("Hasta las 12 hrs del")) {
					if(texto.split("\n")[0].split("-").length == 3) {						
						fecha =  texto.split("\n")[0].replace("###", "").trim();
						if(fecha.split("-")[2].length() > 4 && fecha.split("-")[2].contains("Importe")) {
							fecha = fecha.split("Importe")[0];
						}
						modelo.setVigenciaA(fn.formatDateMonthCadena(fecha));
						
					}
				}
			}
			

			//rfc 
			if(modelo.getRfc().length() == 0  && contenido.contains(ConstantsValue.RFC2)) {
				String[] texto = contenido.split(ConstantsValue.RFC2)[1].split("\n");
				if(texto.length > 1) {
					String aux = texto[2].split("###")[0].replace("@@@", "").trim();
					
					if(tieneLongitudRFC(aux)) {
						modelo.setRfc(aux);
					}
					
					if(texto.length > 1 && modelo.getRfc().length() == 0) {
						String aux2 = texto[1].split("###")[0].replace("@@@", "").trim();
						if(tieneLongitudRFC(aux2)) {
							modelo.setRfc(aux2);
						}
					}
				}
			}
			
			
			if (modelo.getCp().length() == 0) {
				inicio = contenido.indexOf(ConstantsValue.REFERENCIA);
				fin = contenido.indexOf("Descripción");
				if (inicio > 0 && fin > 0 && inicio < fin) {
					newcontenido = new StringBuilder();
					newcontenido.append(contenido.substring(inicio, fin));
					if (fn.isNumeric(
							newcontenido.toString().split(ConstantsValue.REFERENCIA)[1].replace("###", "").trim())) {
						modelo.setCp(
								newcontenido.toString().split(ConstantsValue.REFERENCIA)[1].replace("###", "").trim());
					}else if( newcontenido.toString().contains("C.P.")){
						String cp = newcontenido.toString().split("C.P.")[1].replace("###", "").trim();
						if(cp.contains("Duración")) {
							cp = cp.split("Duración")[0];
						}
						if(fn.isNumeric(cp))
						{
							modelo.setCp(cp);
						}else if(newcontenido.toString().split("C.P.")[1].split("\n").length >1){
							String textoOtraLinea = fn.gatos(newcontenido.toString().split("C.P.")[1].split("\n")[1]);
							String[] aux = textoOtraLinea.split("###");
							if (fn.isNumeric(aux[aux.length - 1].trim())) {
								modelo.setCp(aux[aux.length - 1].trim());
							}
							
						}
					}
				}

			}
			
		
	
			if(modelo.getCp().length() == 0 &&  contenido.contains("C.P.") ) {
				inicio = contenido.indexOf("Dirección");
				fin = contenido.indexOf("Giro");
				if(fin == -1) {
					fin = contenido.indexOf("VEHÍCULO###ASEGURADO");
				}
				if(inicio > -1 && inicio < fin) {
					newcontenido.append(contenido.substring(inicio, fin).replace("@@@", "").replace("/r", ""));
					modelo.setCp(newcontenido.toString().split("C.P.")[1].trim().substring(0, 5).replace("#", "").replace("#", "").replace("###", "").trim());
				}
			} 
			if(modelo.getCp().length() == 4 && fn.isNumeric(modelo.getCp())) {
				String cp = modelo.getCp();
				modelo.setCp("0"+cp);
			}
			if(modelo.getCteDireccion().length() < 15) {
				modelo.setCteDireccion("");
			}
			
		

			if(modelo.getCteDireccion().length() == 0 && contenido.contains("Dirección")) {
				newcontenido = new StringBuilder();
			
				direccionCte =new StringBuilder();
				inicio = contenido.indexOf("Dirección");
				fin = contenido.indexOf("Giro");
				if(fin == -1) {
					fin = contenido.indexOf("VEHÍCULO###ASEGURADO");
				}
				if(inicio > -1 && inicio < fin) {
				newcontenido.append(contenido.substring(inicio, fin).replace("@@@", "").replace("\r", ""));
		
				for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {					
				
					if(newcontenido.toString().split("\n")[i].contains("Dirección") && newcontenido.toString().split("\n")[i+1].contains("Hasta") && newcontenido.toString().split("\n")[i+2].contains("Duración")) {
						modelo.setCteDireccion((newcontenido.toString().split("\n")[i+1].split("###")[1].split("Hasta")[0]
								+ " " + newcontenido.toString().split("\n")[i+2].split("###")[0]).trim());
					}
					
					if(newcontenido.toString().split("\n")[i].contains("Dirección") && newcontenido.toString().split("\n")[i+1].contains("Hasta") && newcontenido.toString().split("\n")[i+3].contains("Duración")) {
						modelo.setCteDireccion((newcontenido.toString().split("\n")[i+1].split("###")[1].split("Hasta")[0]
								+ " " + newcontenido.toString().split("\n")[i+2].split("###")[0]).trim());
					}
				  }
				}
				newcontenido = new StringBuilder();
				
			}
			
			
			
			
			// descripcion (vehiculo)
			// serie
			// prima_neta
			donde = 0;
			donde = fn.recorreContenido(contenido, "Descripción###Serie###MONTO###A###PAGAR");
			if (donde > 0 && contenido.split("@@@")[donde].split("\r\n").length == 1
					&& contenido.split("@@@")[donde + 1].split("\r\n").length == 1
					&& contenido.split("@@@")[donde + 1].split("###").length == 5) {
				modelo.setDescripcion(contenido.split("@@@")[donde + 1].split("###")[0].trim());
				modelo.setSerie(contenido.split("@@@")[donde + 1].split("###")[1].trim());
				if (contenido.split("@@@")[donde + 1].split("###")[3].trim().equals("Neta")) {
					modelo.setPrimaneta(fn.castBigDecimal(
							fn.preparaPrimas(contenido.split("@@@")[donde + 1].split("###")[4].trim())));
				}

			}

			inicio = contenido.indexOf("Modelo###Placas###Motor");
			fin = contenido.indexOf("Importe por Pagar");

			if (inicio > -1 && fin > -1 && inicio < fin) {
				newcontenido.append(contenido.substring(inicio, fin).replace("@@@", "").replace("\r", ""));
				for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {

					if (newcontenido.toString().split("\n")[i].contains(ConstantsValue.MODELO2)
							&& newcontenido.toString().split("\n")[i].contains("Placas")
							&& newcontenido.toString().split("\n")[i].contains("Motor")
							&& newcontenido.toString().split("\n")[i].contains("Derecho")) {
						if (newcontenido.toString().split("\n")[i].contains(ConstantsValue.DERECHO_POLIZA)) {
							modelo.setDerecho(fn.castBigDecimal(fn.preparaPrimas(
									newcontenido.toString().split("\n")[i].split(ConstantsValue.DERECHO_POLIZA)[1]
											.replace("###", "").trim())));
						}

						if (newcontenido.toString().split("\n")[i + 1].contains(ConstantsValue.IVA)) {

							modelo.setModelo(
									fn.castInteger(newcontenido.toString().split("\n")[i + 1].split("###")[0].trim()));

							if (newcontenido.toString().split("\n")[i + 1].split("###")[1].length() > 10) {
									modelo.setMotor(newcontenido.toString().split("\n")[i + 1].split("###")[1]);
								
							
							} else {
								if(newcontenido.toString().split("\n")[i + 1].split("###")[1].equals("I.V.A.")) {
									
								}else {
									modelo.setPlacas(newcontenido.toString().split("\n")[i + 1].split("###")[1]);	
								}
								
							}

							
							modelo.setIva(fn.castBigDecimal(fn.preparaPrimas(
									newcontenido.toString().split("\n")[i + 1].split(ConstantsValue.IVA)[1]
											.replace("###", "").trim())));
						}
					}

					if (newcontenido.toString().split("\n")[i].contains(ConstantsValue.MODELO2)
							&& newcontenido.toString().split("\n")[i].contains("Placas")
							&& newcontenido.toString().split("\n")[i].contains("Motor")
							&& newcontenido.toString().split("\n")[i].contains(ConstantsValue.FRACCIONADO)) {

						if (newcontenido.toString().split("\n")[i].contains(ConstantsValue.FRACCIONADO)) {
							modelo.setRecargo(fn.castBigDecimal(fn.preparaPrimas(
									newcontenido.toString().split("\n")[i].split(ConstantsValue.FRACCIONADO)[1]
											.replace("###", "").trim())));
						}
						if (newcontenido.toString().split("\n")[i + 1].contains(ConstantsValue.DERECHO_POLIZA)) {
							int sp = newcontenido.toString().split("\n")[i + 1].trim().split("###").length;

							switch (sp) {
							case 4:
								modelo.setModelo(
										fn.castInteger(newcontenido.toString().split("\n")[i + 1].split("###")[0]));
								if (newcontenido.toString().split("\n")[i + 1].split("###")[1].length() > 10) {
									modelo.setMotor(newcontenido.toString().split("\n")[i + 1].split("###")[1]);
								} else {
									modelo.setPlacas(newcontenido.toString().split("\n")[i + 1].split("###")[1]);
								}

								modelo.setDerecho(
										fn.castBigDecimal(fn.preparaPrimas(newcontenido.toString().split("\n")[i + 1]
												.split(ConstantsValue.DERECHO_POLIZA)[1].replace("###", "").trim())));
								break;
							case 5:
								modelo.setModelo(
										fn.castInteger(newcontenido.toString().split("\n")[i + 1].split("###")[0]));
								modelo.setPlacas(newcontenido.toString().split("\n")[i + 1].split("###")[1]);
								modelo.setMotor(newcontenido.toString().split("\n")[i + 1].split("###")[2]);
								modelo.setDerecho(
										fn.castBigDecimal(fn.preparaPrimas(newcontenido.toString().split("\n")[i + 1]
												.split(ConstantsValue.DERECHO_POLIZA)[1].replace("###", "").trim())));
								break;
							default:
								break;
							}
						}
					}
				}
			}

			inicio = contenido.indexOf("I.V.A.");
			if (inicio > 0) {
				newcontenido = new StringBuilder();
				newcontenido.append(contenido.split("I.V.A.")[1]);

				if(newcontenido.length() < 10) {
					modelo.setIva(fn.castBigDecimal(
							fn.preparaPrimas(fn.remplazarMultiple(newcontenido.toString(), fn.remplazosGenerales()))));				
				}
			}
			

			if(modelo.getIva().doubleValue() ==  0) {
				inicio = contenido.indexOf("Motor");
				fin = contenido.indexOf("Importe");				
				if(inicio > -1 && fin > -1 && inicio <  fin) {
					newcontenido = new StringBuilder();
					newcontenido.append(contenido.substring(inicio, fin).replace("@@@", "").replace("\r", ""));					
					for (int j = 0; j < newcontenido.toString().split("\n").length; j++) {				
						if(newcontenido.toString().split("\n")[j].contains("Motor") && newcontenido.toString().split("\n")[j].contains("I.V.A.") ) {
							
							modelo.setIva(fn.castBigDecimal(
									fn.preparaPrimas(fn.remplazarMultiple( newcontenido.toString().split("\n")[j].split("Motor")[1].replace("I.V.A.", "").replace("###", ""),fn.remplazosGenerales()))));
						}
					}
					
				}
			}
			
			
			
			
			
			/**
			 * otro formato de gnp
			 */
			if (modelo.getSerie().length() == 0 && modelo.getDescripcion().length() == 0) {
				inicio = contenido.indexOf("VEHÍCULO###ASEGURADO");
				fin = contenido.indexOf("DESGLOSE");
				if (inicio > 0 && fin > 0 && inicio < fin) {
					newcontenido = new StringBuilder();
					newcontenido.append(contenido.substring(inicio, fin).replace("@@@", ""));
					for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {

						if (newcontenido.toString().split("\n")[i].contains("Descripción") && newcontenido.toString().split("\n")[i].contains("Neta")) {
							modelo.setPrimaneta(fn.castBigDecimal(fn.preparaPrimas(fn.remplazarMultiple(
									newcontenido.toString().split("\n")[i].split("Neta")[1].replace("###", ""),
									fn.remplazosGenerales()))));
							modelo.setDescripcion(newcontenido.toString().split("\n")[i + 1].split("###")[0]);
							modelo.setSerie(newcontenido.toString().split("\n")[i + 1].split("###")[1]);
						}else if(newcontenido.toString().split("\n")[i].contains("Neta")) {
							modelo.setPrimaneta(fn.castBigDecimal(fn.castDouble(newcontenido.toString().split("\n")[i].split("Neta")[1].replace("###", "").trim() )));
						}
						if (modelo.getDescripcion().length() == 0 && modelo.getSerie().length() == 0
								&& newcontenido.toString().split("\n")[i].contains("Descripción###Serie###")
								&& (i + 2) < newcontenido.toString().split("\n")[i].length()) {
							String[] texto = newcontenido.toString().split("\n")[i+1].split("###");
							if(texto.length > 1) {
								String descripcion = texto[0];
								if(!descripcion.contains("STD")) {
									descripcion += " STD";
									modelo.setDescripcion(descripcion.trim());
								}
								modelo.setSerie(texto[1].trim());
							}
							
						}
						if (newcontenido.toString().split("\n")[i].contains("Modelo") && newcontenido.toString().split("\n")[i].contains("Póliza")) {
							modelo.setDerecho(fn.castBigDecimal(fn.preparaPrimas(fn.remplazarMultiple(
									newcontenido.toString().split("\n")[i].split("Póliza")[1].replace("###", ""),
									fn.remplazosGenerales()))));

						}else if(newcontenido.toString().split("\n")[i].contains("Póliza")) {
							modelo.setDerecho(fn.castBigDecimal(fn.castDouble(newcontenido.toString().split("\n")[i].split("Póliza")[1].replace("###", "").trim())));
						}
						
						if(modelo.getModelo() == 0 && newcontenido.toString().split("\n")[i].contains("Modelo###Placas##") && (i+1)<newcontenido.toString().split("\n")[i].length() ) {
							if(fn.isNumeric(newcontenido.toString().split("\n")[i+1].split("###")[0].trim())) {
								modelo.setModelo(fn.castInteger(newcontenido.toString().split("\n")[i+1].split("###")[0].trim()));
							}
							
							if(newcontenido.toString().split("\n")[i].contains("Modelo###Placas###Motor")) {					
								if (newcontenido.toString().split("\n")[i + 1].split("###").length >2 && newcontenido.toString().split("\n")[i + 1].split("###")[1].length() > 10) {
		
									if(newcontenido.toString().split("\n")[i + 1].split("###")[1].trim().equals("Importe por Pagar")) {
										
									}else {
										modelo.setMotor(newcontenido.toString().split("\n")[i + 1].split("###")[1].trim());	
									}
									
									
								} else {
								
									if(newcontenido.toString().split("\n")[i + 1].contains("###") ) {
								
										modelo.setPlacas(newcontenido.toString().split("\n")[i + 1].split("###")[1].trim());	
									}
									
								}
							}
						}
						
						if (newcontenido.toString().split("\n")[i].contains("Importe")) {
							modelo.setPrimaTotal(fn.castBigDecimal(fn.preparaPrimas(fn.remplazarMultiple(
									newcontenido.toString().split("\n")[i].split("Pagar")[1].replace("###", ""),
									fn.remplazosGenerales()))));

						}
						
						if(newcontenido.toString().split("\n")[i].contains("Fraccionado")) {
							modelo.setRecargo(fn.castBigDecimal(fn.castDouble(newcontenido.toString().split("\n")[i].split("Fraccionado")[1].replace("###", "").trim())));
						}

					}
				}
			}

			// iva
			donde = 0;

			// prima_total
			donde = 0;
			donde = fn.recorreContenido(contenido, "Importe###por###Pagar");
			if (donde > 0 && contenido.split("@@@")[donde].split("\r\n").length == 1
					&& contenido.split("@@@")[donde].split("###").length == 7
					&& contenido.split("@@@")[donde].split("###")[5].trim().equals("Pagar")) {

				modelo.setPrimaTotal(fn.castBigDecimal(fn.preparaPrimas(fn.remplazarMultiple(
						contenido.split("@@@")[donde].split("###")[6].trim(), fn.remplazosGenerales()))));
			}

			// forma_pago
			// moneda
			donde = 0;
			donde = fn.recorreContenido(contenido, "Forma###de###Pago###Moneda");
			if (donde > 0 && contenido.split("@@@")[donde].split("\r\n").length == 2) {

				for (int i = 0; i < contenido.split("@@@")[donde].split("\r\n").length; i++) {
					if ((i == 0 || (i+1)< contenido.split("@@@")[donde].split("\r\n").length) 
							&& contenido.split("@@@")[donde].split("\r\n")[i].contains("Forma###de###Pago###Moneda")
							&& (contenido.split("@@@")[donde].split("\r\n")[i + 1].split("###").length == 4 || contenido.split("@@@")[donde].split("\r\n")[i + 1].split("###").length == 3)
							&& contenido.split("@@@")[donde].split("\r\n")[i + 1].split("###")[1].trim()
									.split(" ").length == 1
							&& contenido.split("@@@")[donde].split("\r\n")[i + 1].split("###")[2].trim()
									.split(" ").length == 1) {

						modelo.setFormaPago(fn
								.formaPago(contenido.split("@@@")[donde].split("\r\n")[i + 1].split("###")[1].trim()));
						modelo.setMoneda(
								fn.moneda(contenido.split("@@@")[donde].split("\r\n")[i + 1].split("###")[2].trim()));

					}
				}
			}

			// agente
			// cve_agente
			donde = 0;
			donde = fn.recorreContenido(contenido, "Clave###Agente");

			if (donde > 0 ) {
				for (int i = 0; i < contenido.split("@@@")[donde].split("\n").length; i++) {
					if (contenido.split("@@@")[donde].split("\n")[i].contains("Clave")
							&& contenido.split("@@@")[donde].split("\n")[i].contains("Agente") && contenido.split("@@@")[donde].split("\n").length > 3) {						
							modelo.setCveAgente(contenido.split("@@@")[donde].split("\n")[i+1].split("###")[0].trim());
							modelo.setAgente(contenido.split("@@@")[donde].split("\n")[i+1].split("###")[1].trim());								
					}
				}
				if(modelo.getAgente().length() == 0 && modelo.getCveAgente().length() == 0 && contenido.split("Clave###Agente")[1].split("\n").length > 2 && contenido.split("Clave###Agente")[1].split("\n")[1].contains("-")) {					
					modelo.setCveAgente(contenido.split("Clave###Agente")[1].split("\n")[1].split("###")[0].replace("@@@", "").trim());
			        newcontenido = new StringBuilder();
					newcontenido.append(contenido.split("Clave###Agente")[1].split("\n")[1].split("###")[contenido.split("Clave###Agente")[1].split("\n")[1].split("###").length - 1]);
					modelo.setAgente(contenido.split("Clave###Agente")[1].split("\n")[1].split(modelo.getCveAgente())[1].split(newcontenido.toString())[0].replace("###", " ").trim());
					
				}

			}
			
			
			

			// plan

			for (int j = 0; j < contenido.split("@@@").length; j++) {
				if (contenido.split("@@@")[j].contains(ConstantsValue.NO_POLIZA)
						&& contenido.split("@@@")[j].contains("Regular Autos")) {
					inicio = contenido.split("@@@")[j].trim().indexOf("Regular Autos");
					fin = contenido.split("@@@")[j].trim().indexOf(ConstantsValue.NO_POLIZA);

					if (inicio > -1 && fin > inicio) {				
						modelo.setPlan(contenido.split("@@@")[j].trim().substring((inicio + 13), fin).trim());
					}
				}
			}
			
			if(modelo.getPlan().length() == 0 ) {				
				if(newcontenido.append(contenido.split("CONTRATANTE")[0]).toString().contains("Amplia")) {
					modelo.setPlan("Amplia");				
				}else if(newcontenido.append(contenido.split("CONTRATANTE")[0]).toString().contains("AMPLIA")) {
					modelo.setPlan("AMPLIA");				
				}else if(contenido.contains(ConstantsValue.NO_POLIZA))	{
					String texto = contenido.split(ConstantsValue.NO_POLIZA)[0];
					if(texto.split("-").length == 2) {
						modelo.setPlan(texto.split("-")[1].trim());
					}else if(texto.split("\n").length > 1){
						modelo.setPlan(texto.split("\n")[1].trim());
					}
				}
			}

			// conductor
			donde = 0;
			donde = fn.recorreContenido(contenido, "Cliente###Conductor###Habitual###Edad###Sexo");
			if (donde > 0 && contenido.split("@@@")[donde].split("\r\n").length == 2
					&& contenido.split("@@@")[donde].split("\r\n")[1].split("###").length == 4
					&& !contenido.split("@@@")[donde].split("\r\n")[1].contains("Edad###Sexo")) {
				modelo.setConductor(contenido.split("@@@")[donde].split("\r\n")[1].split("###")[1].trim());

			}

			List<EstructuraCoberturasModel> coberturas = new ArrayList<>();
			// coberturas{nombre, sa, deducible}
			inicio = 0;
			fin = 0;
			inicio = contenido.indexOf("Descripción###Suma###Asegurada###Deducible");
			if(inicio == -1) {
				inicio = contenido.indexOf("Coberturas###Suma###Asegurada###Deducible");
			}
			fin = contenido.indexOf("Total###Coberturas");
			longitudTexto = 42;
			if (inicio > -1 && fin > inicio) {
				for (String x : contenido.substring((inicio + longitudTexto), fin).trim().split("\r\n")) {
					EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();
					if (x.split("###").length == 3) {

						cobertura.setNombre(x.split("###")[0].trim());
						cobertura.setSa(fn.eliminaSpacios(x.split("###")[1].trim(), ' ', ""));
						cobertura.setDeducible(x.split("###")[2].trim());
						coberturas.add(cobertura);

					}else if(x.split("###").length == 2) {
						cobertura.setNombre(x.split("###")[0].trim());
						if(fn.numTx(x.split("###")[1]).length() >0){
							cobertura.setSa(x.split("###")[1].trim());
							coberturas.add(cobertura);
						}
					}
				}
			}

			modelo.setCoberturas(coberturas);

			modelo.setFechaEmision(modelo.getVigenciaA());	
		
			List<EstructuraRecibosModel> recibos = new ArrayList<>();
			EstructuraRecibosModel recibo = new EstructuraRecibosModel();

			if (modelo.getFormaPago() == 1) {
				recibo.setReciboId("");
				recibo.setSerie("1/1");
				recibo.setVigenciaDe(modelo.getVigenciaDe());
				recibo.setVigenciaA(modelo.getVigenciaA());
				if (recibo.getVigenciaDe().length() > 0) {
					recibo.setVencimiento(fn.dateAdd(recibo.getVigenciaDe(), 30, 1));
				}
				recibo.setPrimaneta(modelo.getPrimaneta());
				recibo.setDerecho(modelo.getDerecho());
				recibo.setRecargo(modelo.getRecargo());
				recibo.setIva(modelo.getDerecho());

				recibo.setPrimaTotal(modelo.getPrimaTotal());
				recibo.setAjusteUno(modelo.getAjusteUno());
				recibo.setAjusteDos(modelo.getAjusteDos());
				recibo.setCargoExtra(modelo.getCargoExtra());
				recibos.add(recibo);
			}

			modelo.setRecibos(recibos);
			if(modelo.getFormaPago() ==0 && modelo.getMoneda() ==0 ){
				modelo.setMoneda(1);
				modelo.setFormaPago(1);
			}
		
			newcontenido = new StringBuilder();
			return modelo;
		} catch (Exception ex) {
			modelo.setError(
					GnpAutosModel.this.getClass().getTypeName() + " | " + ex.getMessage() + " | " + ex.getCause());
			return modelo;
		}

	}
	
	private boolean tieneLongitudRFC(String rfc) {
		return (rfc.length() == 12 || rfc.length() == 13);
	}
}
