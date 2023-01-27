package com.copsis.models.gnp;

import java.util.ArrayList;
import java.util.List;

import com.copsis.constants.ConstantsValue;
import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraAseguradosModel;
import com.copsis.models.EstructuraBeneficiariosModel;
import com.copsis.models.EstructuraCoberturasModel;
import com.copsis.models.EstructuraJsonModel;

public class GnpVIdaModel2 {
	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
	private String contenido = "";

	public GnpVIdaModel2(String contenido) {
		this.contenido = contenido;
	}

	public EstructuraJsonModel procesar() {
		StringBuilder newcontenido = new StringBuilder();
		StringBuilder newcontenidodire = new StringBuilder();
		StringBuilder newAseguradosi = new StringBuilder();
		int inicio = 0;
		int fin = 0;


	
		contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());
		contenido = contenido.replace("### ### ###", "###").replace("### ### ", "###").replace("######", "###")
				.replace("E ###specificaciones del Plan", ConstantsValue.ESPECIFICACIONES_PLAN)
				.replace("Periodicidad de Pago", ConstantsValue.FORMA_PAGO2)
				.replace("Importe a Pagar", "Importe a pagar")
				.replace("C ###ontratante", "Contratante")
				.replace("E ###ste documento no", "Este documento no");
		        

		try {
			modelo.setTipo(5);
			modelo.setCia(18);
		
			
			inicio =contenido.indexOf("Vigencia Versión");
			fin  = contenido.indexOf("Moneda");
			if(inicio == -1){
				inicio =contenido.indexOf("Vigencia Póliza");
			}

			if (inicio > -1 && fin > -1 && inicio < fin) {
		
				newcontenido.append(contenido.substring(inicio, fin).replace("@@@", "").replace("\r", ""));
			
				for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
					
					if(newcontenido.toString().split("\n")[i].contains("Desde el")) {						
                      modelo.setVigenciaDe(fn.formatDateMonthCadena(fn.elimgatos(newcontenido.toString().split("\n")[i].split("Desde el")[1]).trim().replace("###", "-")));				
					}
					
					if(newcontenido.toString().split("\n")[i].contains("Hasta el")) {					
                      modelo.setVigenciaA(fn.formatDateMonthCadena(fn.elimgatos(newcontenido.toString().split("\n")[i].split("Hasta el")[1]).trim().replace("###", "-")));				
					}
				}
			}

			inicio = contenido.indexOf("Póliza de Seguro de Vida");
			fin = contenido.indexOf(ConstantsValue.COBERTURAS);
			if (inicio > -1 || fin > -1 || inicio < fin) {
				newcontenido = new StringBuilder();
				newcontenido.append(contenido.substring(inicio, fin).replace("@@@", "").replace("\r", "").replace("−", "-"));
				for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {

					if (newcontenido.toString().split("\n")[i].contains("Póliza No.")) {
						modelo.setPoliza(newcontenido.toString().split("\n")[i].split("Póliza No.")[1].replace("###", ""));
					}
					if (newcontenido.toString().split("\n")[i].contains("Contratante")
							&& newcontenido.toString().split("\n")[i + 1].length() < 20) {
						modelo.setCteNombre((newcontenido.toString().split("\n")[i + 2].replace("###", "")).trim());
					
					}else if(newcontenido.toString().split("\n")[i].contains("Contratante") && newcontenido.toString().split("\n")[i+2].contains("Edad")){
						modelo.setCteNombre((newcontenido.toString().split("\n")[i + 1].split("Edad")[0].replace("###", "")).trim());
					}
					if (newcontenido.toString().split("\n")[i].contains("Contratante") && newcontenido.toString().split("\n")[i+1].contains("Vigencia")) {
						modelo.setCteNombre((newcontenido.toString().split("\n")[i + 1].split("Vigencia")[0].replace("###", "")).trim());
						newcontenidodire.append(newcontenido.toString().split("\n")[i + 2].split("Día")[0].replace("###", ""));						
					}
					
					if (newcontenido.toString().split("\n")[i].contains("R.F.C:") && newcontenido.toString().split("\n")[i].contains(ConstantsValue.DESDE)) {
						modelo.setRfc(newcontenido.toString().split("\n")[i].split("R.F.C:")[1].split("Desde")[0].replace("###", "").trim());
					}
					
				    if (newcontenido.toString().split("\n")[i].contains("R.F.C:") && newcontenido.toString().split("\n")[i].contains("Hasta") ) {
				    	modelo.setRfc(newcontenido.toString().split("\n")[i].split("R.F.C:")[1].split("Hasta")[0].replace("###", "").trim());
				    }
					
					
					if (newcontenido.toString().split("\n")[i].contains("C.P")) {									
						modelo.setCp(newcontenido.toString().split("\n")[i].replace("C.P", "C/P").split("C/P")[1].split("###")[0].replace(".", "").trim());
						 if((newcontenido.toString().split("\n")[i + 1].split("Día")[0]).replace("###", "").contains("R.F.C:")) {
							 modelo.setCteDireccion(newcontenidodire +" "+newcontenido.toString().split("\n")[i].replace("C.P", "C/P").split("C/P")[0]);
						 }else {
							 modelo.setCteDireccion((newcontenido.toString().split("\n")[i].split("C.P")[0] + " "+ newcontenido.toString().split("\n")[i + 1].split("Día")[0]).replace("###", ""));	 
						 }
						 
						 if(modelo.getCp().length() == 0 && fn.isvalidCp(newcontenido.toString().split("\n")[i+1].substring(0,5))) {
							 modelo.setCp(newcontenido.toString().split("\n")[i+1].substring(0,5));							 
						 }
						
					}
					if (newcontenido.toString().split("\n")[i].contains("Desde el") && modelo.getVigenciaDe().length() == 0) {

						modelo.setVigenciaDe(fn.formatDateMonthCadena(
								fn.elimgatos(newcontenido.toString().split("\n")[i].split("Desde el")[1]).replace("###", "-")));
					}
					if (newcontenido.toString().split("\n")[i].contains(ConstantsValue.HASTA_EL) && modelo.getVigenciaA().length() == 0) {

						modelo.setVigenciaA(fn.formatDateMonthCadena(
								fn.elimgatos(newcontenido.toString().split("\n")[i].split("Hasta el")[1]).replace("###", "-")));
					}
					if (newcontenido.toString().split("\n")[i].contains("Fecha de expedición")) {
						modelo.setFechaEmision(
								fn.formatDateMonthCadena(fn
										.elimgatos(newcontenido.toString().split("\n")[i].split("Fecha de expedición")[1]
												.split("Forma de pago")[0].replace("### ###", "###"))
										.replace("###", "-")));
					}
					if(modelo.getFechaEmision().isEmpty()) {
						modelo.setFechaEmision(modelo.getVigenciaDe());
					}
					
					if (newcontenido.toString().split("\n")[i].contains("Forma de pago")) {
						if (newcontenido.toString().split("\n")[i].contains("Anual")) {
							modelo.setFormaPago(1);
						}
						if (modelo.getFormaPago() == 0) {
							modelo.setFormaPago(fn.formaPagoSring(newcontenido.toString().replace("Única", "Contado").split("\n")[i]));
						}
						

					}
					if (newcontenido.toString().split("\n")[i].contains("Prima Neta")) {
						modelo.setPrimaneta(fn.castBigDecimal(fn
								.preparaPrimas(newcontenido.toString().split("\n")[i].split("Prima Neta")[1].replace("###", ""))));
					}

					if (newcontenido.toString().split("\n")[i].contains("Fraccionado")) {
						modelo.setRecargo(fn.castBigDecimal(fn.preparaPrimas(
								newcontenido.toString().split("\n")[i].split("Fraccionado")[1].replace("###", ""))));
					}
					if (newcontenido.toString().split("\n")[i].contains("Importe a pagar")) {
						modelo.setPrimaTotal(fn.castBigDecimal(fn.preparaPrimas(
								newcontenido.toString().split("\n")[i].split("Importe a pagar")[1].replace("###", ""))));
					}

				}
				
				//moneda
				if (newcontenido.toString().contains("Dólares")) {
					modelo.setMoneda(2);

				} else {
					modelo.setMoneda(1);

				}
			}

			inicio = contenido.lastIndexOf(ConstantsValue.AGENTE2);
			fin = contenido.lastIndexOf("Para mayor información contáctenos:");
		
			if (inicio > -1 || fin > -1 || inicio < fin) {
				newcontenido = new StringBuilder();
				newcontenido.append(contenido.substring(inicio, fin).replace("@@@", "").replace("\r", ""));
				if (!newcontenido.toString().contains(ConstantsValue.AGENTE2) && !newcontenido.toString().contains(ConstantsValue.CLAVE2)) {
					newcontenido = new StringBuilder();
					inicio = contenido.indexOf("Especificaciones del Plan");
					fin = contenido.indexOf("visite gnp.com.mx");
					if (inicio > -1 || fin > -1 || inicio < fin) {
						inicio = contenido.indexOf("Especificaciones del Plan");
						fin = contenido.indexOf("visite gnp.com.mx");
					}

				}
			}
			

			if (inicio > -1 || fin > -1 || inicio < fin) {
				newcontenido.append(contenido.substring(inicio, fin).replace("@@@", "").replace("\r", "").replace("### ###",
						"###"));
				for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
					if (newcontenido.toString().split("\n")[i].contains(ConstantsValue.AGENTE2)
							&& newcontenido.toString().split("\n")[i].contains(ConstantsValue.CLAVE2)) {
						modelo.setAgente(newcontenido.toString().split("\n")[i].split(ConstantsValue.AGENTE2)[1]
								.split(ConstantsValue.CLAVE2)[0].replace("###", "").trim());
						modelo.setCveAgente(
								newcontenido.toString().split("\n")[i].split(ConstantsValue.CLAVE2)[1].split("###")[1]);
					}
				}
			}
			

			if(modelo.getAgente().isEmpty() && modelo.getCveAgente().isEmpty()) {
				inicio = contenido.indexOf(ConstantsValue.AGENTE2);
				fin = contenido.indexOf("Para mayor información contáctenos:");
			
				if (inicio > -1 || fin > -1 || inicio < fin) {
					newcontenido = new StringBuilder();
					newcontenido.append(contenido.substring(inicio, fin).replace("@@@", "").replace("\r", ""));
					for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {			
						
						if (newcontenido.toString().split("\n")[i].contains(ConstantsValue.AGENTE2)){ 
							modelo.setAgente(newcontenido.toString().split("\n")[i].split(ConstantsValue.AGENTE2)[1].replace("###", ""));
						}
						if (newcontenido.toString().split("\n")[i].contains(ConstantsValue.AGENTE2) && newcontenido.toString().split("\n")[i].contains("Clave")){ 
							modelo.setAgente(newcontenido.toString().split("\n")[i].split(ConstantsValue.AGENTE2)[1].split("Clave")[0].replace("###", ""));
						}
					
						if (newcontenido.toString().split("\n")[i].contains(ConstantsValue.CLAVE2) && !newcontenido.toString().split("\n")[i].contains("Hombre") && modelo.getCveAgente().length() == 0){							
							modelo.setCveAgente(newcontenido.toString().split("\n")[i].split(ConstantsValue.CLAVE2)[1].split("###")[1].replace(" ", "").trim());
							
						}
						
						
					}
					
				}
			}

			inicio = contenido.indexOf("Asegurado s");
			fin = contenido.indexOf(ConstantsValue.COBERTURAS);

			if (inicio > -1 || fin > -1 || inicio < fin) {
				List<EstructuraAseguradosModel> asegurados = new ArrayList<>();

				newcontenido = new StringBuilder();
				newcontenido.append(contenido.substring(inicio, fin).replace("@@@", "").replace("\r", ""));
				
				if(newcontenido.toString().contains("Asegurado del Ahorro") && newcontenido.toString().contains("Supervivencia")) {
					newcontenido = new StringBuilder();
					inicio = contenido.indexOf("Asegurado s");
					fin = contenido.indexOf("Asegurado del Ahorro");
					newcontenido.append(contenido.substring(inicio, fin).replace("@@@", "").replace("\r", ""));
									
					inicio = contenido.indexOf("Asegurado del Ahorro");
					fin = contenido.indexOf(ConstantsValue.COBERTURAS);
					
					newAseguradosi.append(contenido.substring(inicio, fin).replace("@@@", "").replace("\r", ""));
				}
				
				
			
				
			
			
				if(newcontenido.length() > 100) {
					
					if(newcontenido.toString().contains("Asegurado 1") && newcontenido.toString().contains("Asegurado 2")) {
						
						for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
							if(newcontenido.toString().split("\n")[i].contains("Asegurado 1") ) {
								EstructuraAseguradosModel asegurado = new EstructuraAseguradosModel();
								asegurado.setNombre(newcontenido.toString().split("\n")[i + 1].split("###")[0]);
								if(newcontenido.toString().split("\n")[i+2].contains("Nacimiento:")) {
									asegurado.setNacimiento(fn.formatDateMonthCadena(newcontenido.toString().split("\n")[i+2].split("###")[1].trim()));
								}
								if(newcontenido.toString().split("\n")[i+3].contains("Nacimiento:")) {
									asegurado.setNacimiento(fn.formatDateMonthCadena(newcontenido.toString().split("\n")[i+3].split("###")[1]));
								}
								
								if (newcontenido.toString().split("\n")[i+4].contains("Edad Contratación")) {
									
									asegurado.setEdad(fn.castInteger(fn.numTx(newcontenido.toString().split("\n")[i+4].split("###")[1].replace("años", "").trim())));
								}else if(newcontenido.toString().split("\n")[i+2].contains("Edad Contratación")) {
									asegurado.setEdad(fn.castInteger(fn.numTx(newcontenido.toString().split("\n")[i+2].split("###")[1].replace("años", "").trim())));
								}
								asegurados.add(asegurado);
							}
							
							if(newcontenido.toString().split("\n")[i].contains("Asegurado 2")) {
								EstructuraAseguradosModel asegurado = new EstructuraAseguradosModel();
								asegurado.setNombre(newcontenido.toString().split("\n")[i + 1].split("###")[1] + " " + newcontenido.toString().split("\n")[i + 2].trim());
								if(asegurado.getNombre().contains("Fecha de Nacimiento")) {
									String auxiliar = asegurado.getNombre();
									asegurado.setNombre(auxiliar.split("Fecha de Nacimiento")[0].replace("###", "").trim());
								}
								if(newcontenido.toString().split("\n")[i+2].contains("Nacimiento:")) {
									asegurado.setNacimiento(fn.formatDateMonthCadena(newcontenido.toString().split("\n")[i+2].split("###")[3]));
								}
								if(newcontenido.toString().split("\n")[i+3].contains("Nacimiento:")) {
									asegurado.setNacimiento(fn.formatDateMonthCadena(newcontenido.toString().split("\n")[i+3].split("###")[3]));
								}
								if (newcontenido.toString().split("\n")[i+4].contains("Edad Contratación")) {
									
									asegurado.setEdad(fn.castInteger(fn.numTx(newcontenido.toString().split("\n")[i+4].split("###")[3].replace("años", "").trim())));
								}else if(newcontenido.toString().split("\n")[i+2].contains("Edad Contratación")) {
									asegurado.setEdad(fn.castInteger(fn.numTx(newcontenido.toString().split("\n")[i+2].split("###")[3].replace("años", "").trim())));
									String nombre = asegurado.getNombre().split("Edad Contratación")[0].trim();
									asegurado.setNombre(nombre);
								}
								
								asegurados.add(asegurado);
							}
							
							if(newcontenido.toString().split("\n")[i].contains("Nombre del menor")) {
								EstructuraAseguradosModel asegurado = new EstructuraAseguradosModel();
							if(fn.isNumeric(newcontenido.toString().split("\n")[i + 2].split("###")[0].trim())) {
								asegurado.setNombre(newcontenido.toString().split("\n")[i + 1].split("###")[0].trim());
							}else {
								asegurado.setNombre(newcontenido.toString().split("\n")[i + 1].split("###")[0].trim()
										+" "+ newcontenido.toString().split("\n")[i + 2].split("###")[0].trim()
										);
							}
								
								
								if(newcontenido.toString().split("\n")[i+1].contains("Nacimiento:")) {
								
							    String nacimiento =  newcontenido.toString().split("\n")[i+1].split("Nacimiento:")[1].replace("de", "-").replace("###", "").replace(" ", "").trim();
							
									if(nacimiento.split("-").length == 2) {
										String viga = nacimiento.substring( nacimiento.length()-4,nacimiento.length());
										
									
									
										if(fn.isNumeric(viga)) {
											   nacimiento = nacimiento.replace(viga, "") +"-" + viga;
										}
										else {
											nacimiento = newcontenido.toString().split("\n")[i+1].split("Nacimiento:")[1].split("###")[0].replace("de", "-").replace("###", "").replace(" ", "").trim();
												
									     if(newcontenido.toString().split("\n")[i+2].split("###").length == 2 ) {
									    	 if(fn.isNumeric(newcontenido.toString().split("\n")[i+2].split("###")[1])) {
									    		nacimiento += "-" + newcontenido.toString().split("\n")[i+2].split("###")[1]; 
									    	 }
									     }
										 if(newcontenido.toString().split("\n")[i+2].split("###").length == 1 ) {
									    	 if(fn.isNumeric(newcontenido.toString().split("\n")[i+2].split("###")[0])) {
									    		nacimiento += "-" + newcontenido.toString().split("\n")[i+2].split("###")[0]; 
									    	 }
									     }
										 
										 if(fn.isNumeric(newcontenido.toString().split("\n")[i+2].split("###")[0]) && newcontenido.toString().split("\n")[i+2].contains("Día")) {
												nacimiento += "-" + newcontenido.toString().split("\n")[i+2].split("###")[0]; 
										 }
								     
									
										}
										
								
									}
									
									if(nacimiento.split("-").length == 3) {
										  asegurado.setNacimiento(fn.formatDateMonthCadena(nacimiento));
									}
									
							    
								}
								
								asegurados.add(asegurado);
							}
							
						}
						
					}else {
					EstructuraAseguradosModel asegurado = new EstructuraAseguradosModel();
					for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
					
						if (newcontenido.toString().split("\n")[i].contains("Asegurado 1")) {
							asegurado.setNombre(newcontenido.toString().split("\n")[i + 1].split("###")[0]);
						}
						if (newcontenido.toString().split("\n")[i].contains("Nacimiento:") && asegurado.getNacimiento().length()==0) {
							
							asegurado.setNacimiento(fn.formatDateMonthCadena(newcontenido.toString().split("\n")[i].split("###")[1]));
						}
											
						if (newcontenido.toString().split("\n")[i].contains("Edad Emisión")) {
							asegurado.setEdad(fn.castInteger(newcontenido.toString().split("\n")[i].split("Edad Emisión:")[1]
									.replace("###", "").replace("años", "").trim()));
						}
					
						if (newcontenido.toString().split("\n")[i].contains("Edad Contratación")) {
							if(asegurado.getNombre().length() == 0){
								asegurado.setNombre(newcontenido.toString().split("\n")[i -1].split("###")[0]);
							}
							asegurado.setEdad(fn.castInteger(fn.numTx(newcontenido.toString().split("\n")[i])));
						}
					}			
					asegurados.add(asegurado);
					}
					
				}
				if(newAseguradosi.toString().contains("Asegurado del Ahorro") && newAseguradosi.toString().contains("Supervivencia")) {
					EstructuraAseguradosModel asegurado = new EstructuraAseguradosModel();
					for (int i = 0; i < newAseguradosi.toString().split("\n").length; i++) {

						if (newAseguradosi.toString().split("\n")[i].contains("Asegurado del Ahorro")) {
							asegurado.setNombre(newAseguradosi.toString().split("\n")[i + 1].split("###")[0].trim());
						}
						if (newAseguradosi.toString().split("\n")[i].contains("Nacimiento:")) {
							StringBuilder fechaNacimiento = new StringBuilder();
							fechaNacimiento.append(newAseguradosi.toString().split("\n")[i].split("Nacimiento:")[1].split("###")[0].replace("de", "-").replace("###", "").replace(" ", "").trim());

							if(newAseguradosi.toString().split("\n")[i+1].split("###").length == 1 && fechaNacimiento.toString().split("-").length == 2) {
						    	 if(fn.isNumeric(newAseguradosi.toString().split("\n")[i+1].split("###")[0].replace("de", "").trim())) {
						    		fechaNacimiento.append("-").append(newAseguradosi.toString().split("\n")[i+1].split("###")[0].replace("de","").replace("###", "").replace(" ", ""));
						    	 }
						     }
							
							
							if(fechaNacimiento.toString().split("-").length == 2 && newAseguradosi.toString().split("\n")[i+1].contains("Vigencia Póliza")) {
								
								
								if(fn.isNumeric(newAseguradosi.toString().split("\n")[i+1].split("###")[0].replace("de", "").trim())) {
			
									 fechaNacimiento.append("").append(newAseguradosi.toString().split("\n")[i+1].split("###")[0].replace("de","").replace("###", "").replace(" ", "")); 
								 }
							}
						
							if(fechaNacimiento.toString().split("-").length == 3) {
									asegurado.setNacimiento(fn.formatDateMonthCadena(fechaNacimiento.toString().trim()));
							}
						
							
						}
					}
					
					asegurados.add(asegurado);
				}

				modelo.setAsegurados(asegurados);
			}

			String plazo = "";
			if (contenido.contains("Especificaciones del Plan")) {
				plazo = contenido.split("Especificaciones del Plan")[1].split(ConstantsValue.AGENTE2)[0]
						.replace("@@@", "").replace("\r\n", "").replace("Observaciones", "").replace("###", "")
						.substring(0, 24).replace("9 m", "").trim();

				if (plazo.contains("Exención de Pago de Prim")) {
					plazo = contenido.split("Especificaciones del Plan")[1].split(ConstantsValue.PLAZO)[1]
							.split(ConstantsValue.AGENTE2)[0].replace("@@@", "").replace("\r\n", "");

				} else if (plazo.contains("LaprotecciónContratadase")) {
					plazo = contenido.split("Especificaciones del Plan")[1].split(ConstantsValue.PLAZO)[1]
							.split(ConstantsValue.AGENTE2)[0].split("Plan con incrementos")[0].replace("@@@", "")
									.replace("\r\n", "");
				} else {
					plazo = contenido.split("Especificaciones del Plan")[1].split(ConstantsValue.AGENTE2)[0]
							.replace("@@@", "").replace("\r\n", "").replace("Observaciones", "").replace("###", "")
							.substring(0, 24).replace("9 m", "").trim();
				}

			} else {
				plazo = contenido.split("E specificaciones del Plan")[1].split("La###protección###Contratada")[0]
						.replace("@@@", "").substring(0, 28).trim();
			}

			if (fn.isNumeric(plazo.replace("Plazo", "").replace("años", "").trim())
					&& !plazo.contains(ConstantsValue.PLAZO_EDAD_ALCANZADA)
					&& !contenido.contains("Plazo edad alcanzada")) {
                     modelo.setPlazo(fn.castInteger((plazo.replace("Plazo", "").replace("años", "").trim())));

			}


			if (contenido.contains("Plazo edad alcanzada")) {
			
				newcontenido = new StringBuilder();

				if(contenido.split("Plazo edad")[1].split("\n")[0].contains("Cobertura:s")) {
					newcontenido.append(contenido.split("Plazo edad")[1].split("Cobertura:s")[0]
							.replace("años", "").replace("\r\n", "").replace(".", "").replace("alcanzada", "").substring(0, 7));
				}else {
					if(contenido.split("Plazo edad")[1].contains("años")) {
						newcontenido.append(contenido.split("Plazo edad")[1].split("años")[0].replace("alcanzada", "").trim());
					}
				}

				modelo.setPlazoPago(fn.castInteger(newcontenido.toString()));
				modelo.setRetiro(fn.castInteger(newcontenido.toString()));
			}

			if (modelo.getRetiro() > 0) {
				modelo.setTipovida(1);
			} else if (contenido.contains("Supervivencia")) {
				modelo.setTipovida(3);
			} else {
				modelo.setTipovida(2);
			}
			if (contenido.contains("Supervivencia")) {
				modelo.setAportacion(1);
			}
			
			if(contenido.split(ConstantsValue.CODIGO_CLIENTE)[1].split("Hasta el")[0].length() > 100) {
				if(contenido.split(ConstantsValue.CODIGO_CLIENTE)[1].split("Prima del Movimiento")[0].length()> 50) {
					modelo.setIdCliente(contenido.split(ConstantsValue.CODIGO_CLIENTE)[1].split("Prima del Movimiento")[0].substring(0,12).replace("\r\n", "").replace("@@@", "").replace("###", "").trim());
				}else{
					modelo.setIdCliente(contenido.split(ConstantsValue.CODIGO_CLIENTE)[1].split("Prima del Movimiento")[0].replace("\r\n", "").replace("@@@", "").replace("###", "").trim());
				}
				
			}else {
				modelo.setIdCliente(contenido.split(ConstantsValue.CODIGO_CLIENTE)[1].split("Hasta el")[0].replace("###", "").trim());	
			}
			
			
			/*Proceso unico cuando las  primas de las vienen cero*/
			  if(modelo.getDerecho().doubleValue() == 0  || modelo.getRecargo().doubleValue() == 0 || modelo.getDerecho().doubleValue() == 0 
					  &&  modelo.getIva().doubleValue() == 0 &&  modelo.getPrimaTotal().doubleValue() == 0) {
				  
				  
					inicio = contenido.indexOf("Coberturas");
					fin = contenido.indexOf("Agente");
					if(fin < inicio ) {
						fin = contenido.indexOf("Este documento no");
					}
				
				
					if (inicio > 0 && fin > 0 && inicio < fin) {
						newcontenido = new StringBuilder();
						newcontenido.append(contenido.substring(inicio,fin).replace("−", "-"));
						 for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {

							if(newcontenido.toString().split("\n")[i].contains("Importe Total") 
							&& newcontenido.toString().split("\n")[i+1].contains("Actual")&& newcontenido.toString().split("\n")[i+1].split("Actual")[1].length() > 5) {															
								modelo.setPrimaTotal(fn.castBigDecimal(fn.preparaPrimas(
										newcontenido.toString().split("\n")[i+1].split("Actual")[1].replace("###", "").trim())));
								modelo.setPrimaneta(modelo.getPrimaTotal());
							}
						}
					}
			  }
			
			
			
			
			String beneficiarios1 = "";
			inicio = contenido.indexOf("Beneficiarios de Ahorro Garantizado:");
			fin = contenido.lastIndexOf("Para mayor información contáctenos:");
			if (inicio > 0 && fin > 0 && inicio < fin) {
				newcontenido = new StringBuilder();
				newcontenido.append(contenido.substring(inicio, fin + 10).replace("@@@", "").trim());
				if (newcontenido.toString().contains("Protección Contratada") && newcontenido.toString().contains("Para mayor")) {
					beneficiarios1 = newcontenido.toString().split("Protección Contratada")[1].split("Para mayor")[0];
				}
			}

			String b = "";
			if (!beneficiarios1.contains(" Primas Anuales")) {
				b = beneficiarios1;
			}
			int tip = 0;
			if (modelo.getPlan().equals("Profesional") || modelo.getPlan().equals("Dotal")) {
				tip = 10;

			} else {
				tip = 11;

			}

			if (b.length() == 0) {
				inicio = contenido.indexOf("Beneficiarios");
				fin = contenido.lastIndexOf("Para mayor información");

				if (inicio > fin) {
					fin = inicio + (contenido.split("Beneficiarios")[1].length() - 1);
				}
				if (inicio > -1 && fin > -1 && inicio < fin) {
					b = contenido.substring(inicio, fin);
				}
			}
			
			if( b.split("-").length > 2) {
				inicio = contenido.indexOf("Beneficiarios:");
				fin = contenido.lastIndexOf("Especificaciones especiales");
				if (inicio > -1 && fin > -1 && inicio < fin) {
					String primer =contenido.substring(inicio, fin + 10).replace("@@@", "").trim();
					for (String bene : primer.split("\n")) {
						if( bene.split("-").length > 2) {
						b = b+bene;
						}
					}
				
				
				}
				
			}else {
				
				inicio = contenido.indexOf("Beneficiarios:");
				fin = contenido.lastIndexOf("Especificaciones especiales");
				
				if (inicio > -1 && fin > -1 && inicio < fin) {
					b =contenido.substring(inicio, fin + 10).replace("@@@", "").trim();					
				}
			}
	
			
			inicio = contenido.indexOf("Beneficiarios");
			fin = contenido.lastIndexOf("Beneficiarios de Ahorro Garantizado");
			if (inicio > -1 && fin > -1 && inicio < fin) {
				String primer =contenido.substring(inicio, fin + 10).replace("@@@", "").trim();
				for (String bene : primer.split("\n")) {
					if( bene.split("-").length > 2) {
					b = b+bene;
					}
				}
			}
			
		
			
			
		
		
			
		
			if (b.length() > 0) {
				List<EstructuraBeneficiariosModel> beneficiarios = new ArrayList<>();

				for (String beneficiariod : b.split("\n")) {
					EstructuraBeneficiariosModel beneficiario = new EstructuraBeneficiariosModel();
					int aseg = beneficiariod.split("###").length;
				
					if (aseg == 4 && !beneficiariod.contains("Nombre") && beneficiariod.split("-").length > 2) {

						beneficiario.setNombre(beneficiariod.split("###")[0].replace("@@@", "").trim());
						beneficiario.setNacimiento(fn.formatDate(beneficiariod.split("###")[1], "dd-MM-yy"));
						beneficiario.setParentesco(fn.parentesco(beneficiariod.split("###")[2].toLowerCase()));
						beneficiario.setPorcentaje(
								fn.castDouble(beneficiariod.split("###")[3].replace("\r", "")).intValue());
						beneficiario.setTipo(tip);
						beneficiarios.add(beneficiario);
					}
				}

				modelo.setBeneficiarios(beneficiarios);
			}

			List<EstructuraCoberturasModel> coberturas = new ArrayList<>();

			newcontenido = new StringBuilder();
			inicio = -1;
			fin = -1;
			inicio = contenido.indexOf("Coberturas");
			fin = contenido.indexOf("@@@Agente");
			if (fin == -1) {
				fin = contenido.lastIndexOf("Agente");
			}

			if (inicio == -1 && fin == -1 || inicio > -1 && fin == -1 || inicio == -1 && fin > -1) {
				inicio = -1;
				fin = -1;
				inicio = contenido.indexOf("Coberturas");
				fin = contenido.indexOf("@@@  \r\n" + "Agente");
			}
			if (inicio > -1 && fin > -1) {
				for (String dato : contenido.substring(inicio, fin).trim().split("\r\n")) {
					if (dato.split("###").length >= 2) {

						if (dato.split("###")[1].trim().equals("Amparada")) {
							newcontenido.append("\r\n").append(dato.trim());
						} else {
		

							if (fn.isNumeric(dato.split("###")[1].replace(".", "").replace(",", "").trim()) && Double.parseDouble(dato.split("###")[1].replace(".", "").replace(",", "").trim()) >= 0
									&& !dato.split("###")[0].contains("Hasta")
									&& !dato.split("###")[0].trim().equals("Movimiento")
									&& !dato.split("###")[0].trim().equals("Actual")
									&& (!dato.split("###")[0].contains("Importe")
											&& !dato.split("###")[0].contains("Total"))
									&& !dato.split("###")[0].contains("Desde")
									&& !dato.split("###")[0].trim().equals("Anterior")) {

								newcontenido.append("\r\n").append(dato.trim());
							}
						}
					}
				}

				if (newcontenido.length() > 0) {
					for (String dato : newcontenido.toString().split("\n")) {
						EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();
						if (dato.split("###").length >= 2) {
							cobertura.setNombre(dato.split("###")[0].replace("@@@", "").trim());
							cobertura.setSa(dato.split("###")[1].replace("@@@", "").trim());
							coberturas.add(cobertura);
						}
					}
				}
			}
			modelo.setCoberturas(coberturas);
			
			inicio = contenido.indexOf("Póliza de Seguro de Vida");
			fin = contenido.indexOf("Póliza No.");
			newcontenido = new StringBuilder();
			if(inicio > -1 && fin > -1 && inicio < fin) {
				newcontenido.append(contenido.substring(inicio,fin));
				for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
					if(newcontenido.toString().split("\n")[i].contains("Seguro de Vida")) {
						modelo.setPlan(newcontenido.toString().split("\n")[i].split("Seguro de Vida")[1].replace("###", "").trim());
					}
				}
				
			}

			if(modelo.getFechaEmision().length()> 0 && modelo.getVigenciaDe().length() == 0 && modelo.getVigenciaA().length() == 0){
             modelo.setVigenciaDe(modelo.getFechaEmision());
			 modelo.setVigenciaA(fn.calcvigenciaA(modelo.getVigenciaDe(), 12));
			}

			return modelo;
		} catch (Exception ex) {

			modelo.setError(
					GnpVIdaModel2.this.getClass().getTypeName() + " | " + ex.getMessage() + " | " + ex.getCause());
		
			return modelo;
		}
	}

}
