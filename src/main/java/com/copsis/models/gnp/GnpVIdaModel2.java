package com.copsis.models.gnp;

import java.util.ArrayList;
import java.util.List;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraAseguradosModel;
import com.copsis.models.EstructuraBeneficiariosModel;
import com.copsis.models.EstructuraCoberturasModel;
import com.copsis.models.EstructuraJsonModel;

public class GnpVIdaModel2 {
		private DataToolsModel fn = new DataToolsModel();
		private EstructuraJsonModel modelo = new EstructuraJsonModel();
		private String contenido = "";

	
	
		
		public GnpVIdaModel2(String contenido){
			this.contenido = contenido;
		}
		
		public EstructuraJsonModel procesar() {
			 String newcontenido;
			 int inicio = 0;
			 int fin = 0;

			contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());
			contenido = contenido.replace("### ### ###", "###").replace("### ### ", "###").replace("######", "###")
					.replace("E ###specificaciones del Plan", "Especificaciones del Plan");
				
		
			try {
				modelo.setTipo(5);
				modelo.setCia(18);

				inicio = contenido.indexOf("Póliza de Seguro de Vida");
				fin = contenido.indexOf("Coberturas");
				
				if(inicio > -1 || fin > -1 || inicio < fin) {
					newcontenido =contenido.substring(inicio,fin).replace("@@@", "").replace("\r", "");
					for (int i = 0; i < newcontenido.split("\n").length; i++) {
						
						if(newcontenido.split("\n")[i].contains("Póliza No.")) {
							modelo.setPoliza(newcontenido.split("\n")[i].split("Póliza No.")[1].replace("###", ""));
						}
						if(newcontenido.split("\n")[i].contains("Contratante")) {
							if(newcontenido.split("\n")[i+1].length() > 20) {
								
							}else {
								modelo.setCteNombre(newcontenido.split("\n")[i+2].replace("###", ""));
							}
						}
						if(newcontenido.split("\n")[i].contains("R.F.C:") && newcontenido.split("\n")[i].contains("Desde")) {
							modelo.setRfc(newcontenido.split("\n")[i].split("R.F.C:")[1].split("Desde")[0].replace("###", "").trim());
						}
						if(newcontenido.split("\n")[i].contains("C.P")) {
							modelo.setCp(newcontenido.split("\n")[i].split("C.P")[1].split("###")[0].trim());
							modelo.setCteDireccion((newcontenido.split("\n")[i].split("C.P")[0]
									+" " + newcontenido.split("\n")[i+1].split("Día")[0]).replace("###", ""));				
						}
						
						if(newcontenido.split("\n")[i].contains("Desde el")) {
						
							modelo.setVigenciaDe(fn.formatDate_MonthCadena(fn.elimgatos( newcontenido.split("\n")[i].split("Desde el")[1]).replace("###", "-")));
						}
						if(newcontenido.split("\n")[i].contains("Hasta el")) {
							
							modelo.setVigenciaA(fn.formatDate_MonthCadena(fn.elimgatos( newcontenido.split("\n")[i].split("Hasta el")[1]).replace("###", "-")));
						}
						if(newcontenido.split("\n")[i].contains("Fecha de expedición")) {
							modelo.setFechaEmision(fn.formatDate_MonthCadena(fn.elimgatos(newcontenido.split("\n")[i].split("Fecha de expedición")[1].split("Forma de pago")[0].replace("### ###", "###")).replace("###", "-")));
						}
						if(newcontenido.split("\n")[i].contains("Forma de pago")) {
								if(newcontenido.split("\n")[i].contains("Anual")) {
									modelo.setFormaPago(1);
								}
								if(modelo.getFormaPago() ==0 ) {
								 modelo.setFormaPago(fn.formaPagoSring(newcontenido.split("\n")[i]));	
								}
								
						}
						if(newcontenido.split("\n")[i].contains("Prima Neta")) {							
							modelo.setPrimaneta(fn.castBigDecimal(fn.preparaPrimas(newcontenido.split("\n")[i].split("Prima Neta")[1].replace("###", ""))));
						}
						
						if(newcontenido.split("\n")[i].contains("Fraccionado")) {							
							modelo.setRecargo(fn.castBigDecimal(fn.preparaPrimas(newcontenido.split("\n")[i].split("Fraccionado")[1].replace("###", ""))));
						}
						if(newcontenido.split("\n")[i].contains("Importe a pagar")) {							
							modelo.setPrimaTotal(fn.castBigDecimal(fn.preparaPrimas(newcontenido.split("\n")[i].split("Importe a pagar")[1].replace("###", ""))));
						}
						
						if(newcontenido.split("\n")[i].contains("Dólares")) {
							modelo.setMoneda(2);
							
						}else {
							modelo.setMoneda(1);
							
						}
					}					
				}
		
				inicio = contenido.lastIndexOf("Agente");
				fin = contenido.lastIndexOf("Para mayor información contáctenos:");		
				if(inicio > -1 || fin > -1 || inicio < fin) {
					newcontenido =contenido.substring(inicio,fin).replace("@@@", "").replace("\r", "");
					if(newcontenido.contains("Agente") && newcontenido.contains("Clave")) {
						
					}else {
						newcontenido ="";
						inicio = contenido.indexOf("Especificaciones del Plan");
						fin = contenido.indexOf("visite gnp.com.mx");
						
						
						if(inicio > -1 || fin > -1 || inicio < fin) {
							inicio = contenido.indexOf("Especificaciones del Plan");
							fin = contenido.indexOf("visite gnp.com.mx");
						}
				
					}
				}

				if(inicio > -1 || fin > -1 || inicio < fin) {
					newcontenido =contenido.substring(inicio,fin).replace("@@@", "").replace("\r", "").replace("### ###", "###");					
					for (int i = 0; i < newcontenido.split("\n").length; i++) {
						if(newcontenido.split("\n")[i].contains("Agente") & newcontenido.split("\n")[i].contains("Clave")) {
							modelo.setAgente(newcontenido.split("\n")[i].split("Agente")[1].split("Clave")[0].replace("###", "").trim());	
							modelo.setCveAgente(newcontenido.split("\n")[i].split("Clave")[1].split("###")[1]);
						}					
					}					
				}
				
				inicio = contenido.indexOf("Asegurado s");
				fin = contenido.indexOf("Coberturas");
				
				if(inicio > -1 || fin > -1 || inicio < fin) {
					List<EstructuraAseguradosModel> asegurados = new ArrayList<>();

					newcontenido =contenido.substring(inicio,fin).replace("@@@", "").replace("\r", "");
					EstructuraAseguradosModel asegurado = new EstructuraAseguradosModel();
					for (int i = 0; i < newcontenido.split("\n").length; i++) { 
						if(newcontenido.split("\n")[i].contains("Asegurado 1")) {
							asegurado.setNombre(newcontenido.split("\n")[i+1].split("###")[0]);
							
										
						}
						if(newcontenido.split("\n")[i].contains("Edad Emisión")) {
							asegurado.setEdad(fn.castInteger(newcontenido.split("\n")[i].split("Edad Emisión:")[1].replace("###", "").replace("años", "").trim()));
						}
						
					}
					asegurados.add(asegurado);		
					
					modelo.setAsegurados(asegurados);
				}
				
				
				
				
				String plazo = "";
				if (contenido.contains("Especificaciones del Plan") == true) {
					plazo = contenido.split("Especificaciones del Plan")[1].split("Agente")[0].replace("@@@", "")
							.replace("\r\n", "").replace("Observaciones", "").replace("###", "").substring(0, 24)
							.replace("9 m", "").trim();

					if (plazo.contains("Exención de Pago de Prim") == true) {
						plazo = contenido.split("Especificaciones del Plan")[1].split("Plazo")[1].split("Agente")[0]
								.replace("@@@", "").replace("\r\n", "");

					} else if (plazo.contains("LaprotecciónContratadase") == true) {
						plazo = contenido.split("Especificaciones del Plan")[1].split("Plazo")[1].split("Agente")[0]
								.split("Plan con incrementos")[0].replace("@@@", "").replace("\r\n", "");
					} else {
						plazo = contenido.split("Especificaciones del Plan")[1].split("Agente")[0].replace("@@@", "")
								.replace("\r\n", "").replace("Observaciones", "").replace("###", "").substring(0, 24)
								.replace("9 m", "").trim();
					}

				} else {
					plazo = contenido.split("E specificaciones del Plan")[1].split("La###protección###Contratada")[0]
							.replace("@@@", "").substring(0, 28).trim();
				}

				if (plazo.contains("Plazo: Edad Alcanzada") == true || contenido.contains("Plazo edad alcanzada") == true) {
				} else {
					if (fn.isNumeric(plazo.replace("Plazo", "").replace("años", "").trim())) {
						modelo.setPlazo(fn.castInteger((plazo.replace("Plazo", "").replace("años", "").trim())));
					}
				}

				if (contenido.contains("Plazo edad alcanzada") == true) {
				} else if (contenido.contains("Plazo: Edad Alcanzada") == true) {

					String plazapago = contenido.split("Plazo: Edad Alcanzada")[1].split("Cobertura:s")[0]
							.replace("años", "").replace("\r\n", "").replace(".", "").substring(0, 7);
					modelo.setPlazoPago(fn.castInteger(plazapago.trim()));
					modelo.setRetiro(fn.castInteger(plazapago.trim()));
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
				modelo.setIdCliente(contenido.split("Código Cliente")[1].split("Hasta el")[0].replace("###", "").trim());
				String beneficiarios1 = "";
				inicio = contenido.indexOf("Beneficiarios de Ahorro Garantizado:");
				fin = contenido.lastIndexOf("Para mayor información contáctenos:");
				if (inicio > 0 && fin > 0 && inicio < fin) {
					newcontenido = contenido.substring(inicio, fin + 10).replace("@@@", "").trim();
					if (newcontenido.contains("Protección Contratada") && newcontenido.contains("Para mayor")) {
						beneficiarios1 = newcontenido.split("Protección Contratada")[1].split("Para mayor")[0];
					}
				}

				String b = "";
				if (beneficiarios1.contains(" Primas Anuales") == true) {
				} else {
					b = beneficiarios1;
				}
				int tip = 0;
				if (modelo.getPlan().equals("Profesional") || modelo.getPlan().equals("Dotal")) {
					tip = 10;

				} else {
					tip = 11;

				}
				

			
				if(b.length() == 0) {
					inicio = contenido.indexOf("Beneficiarios");
					fin = contenido.lastIndexOf("Para mayor información");

					if(inicio > fin) {					
						fin = inicio +(contenido.split("Beneficiarios")[1].length()-1) ;
					}
					if(inicio > -1 && fin > -1 && inicio < fin) {
						b = contenido.substring(inicio,fin);						
					}
				}
				
				if(b.length() > 0) {
				List<EstructuraBeneficiariosModel> beneficiarios = new ArrayList<>();

				for (String beneficiariod : b.split("\n")) {
					EstructuraBeneficiariosModel beneficiario = new EstructuraBeneficiariosModel();
					int aseg = beneficiariod.split("###").length;
					if (aseg == 4) {
						if (beneficiariod.contains("Nombre")) {
						} else if (beneficiariod.contains("@@@")) {
							if(beneficiariod.split("-").length >  2) {
								beneficiario.setNombre(beneficiariod.split("###")[0].replace("@@@", "").trim());
								beneficiario.setNacimiento(fn.formatDate(beneficiariod.split("###")[1], "dd-MM-yy"));
								beneficiario.setParentesco(fn.parentesco(beneficiariod.split("###")[2].toLowerCase()));					
								beneficiario.setPorcentaje(
										fn.castDouble(beneficiariod.split("###")[3].replace("\r", "")).intValue());
								beneficiario.setTipo(tip);
								beneficiarios.add(beneficiario);
							}
					
						} else {
							if(beneficiariod.split("-").length >  2) {
								beneficiario.setNombre(beneficiariod.split("###")[0].replace("@@@", "").trim());
								beneficiario.setNacimiento(fn.formatDate(beneficiariod.split("###")[1], "dd-MM-yy"));
								beneficiario.setParentesco(fn.parentesco(beneficiariod.split("###")[2].toLowerCase()));
								beneficiario.setPorcentaje(
										fn.castDouble(beneficiariod.split("###")[3].replace("\r", "")).intValue());
								beneficiario.setTipo(tip);
								beneficiarios.add(beneficiario);	
							}
					
						}
					}
				}

				modelo.setBeneficiarios(beneficiarios);
				}


				List<EstructuraCoberturasModel> coberturas = new ArrayList<>();

				newcontenido = "";
				inicio = -1;
				fin = -1;
				inicio = contenido.indexOf("Coberturas");
				fin = contenido.indexOf("@@@Agente");
				if (inicio == -1 && fin == -1 || inicio > -1 && fin == -1 || inicio == -1 && fin > -1) {
					inicio = -1;
					fin = -1;
					inicio = contenido.indexOf("Coberturas");
					fin = contenido.indexOf("@@@  \r\n" + "Agente");
				}
				if (inicio > -1 && fin > -1) {
					for (String dato : contenido.substring(inicio, fin).trim().split("\r\n")) {
						if (dato.split("###").length >= 2) {
							try {
								if (dato.split("###")[1].trim().equals("Amparada")) {
									newcontenido += "\r\n" + dato.trim();// + "\r\n";
								} else {
									if (Double.parseDouble(
											dato.split("###")[1].replace(".", "").replace(",", "").trim()) >= 0) {
										if (dato.split("###")[0].contains("Hasta") == false
												&& dato.split("###")[0].trim().equals("Movimiento") == false
												&& dato.split("###")[0].trim().equals("Actual") == false
												&& (dato.split("###")[0].contains("Importe") == false
														&& dato.split("###")[0].contains("Total") == false)
												&& dato.split("###")[0].contains("Desde") == false
												&& dato.split("###")[0].trim().equals("Anterior") == false) {
											newcontenido += "\r\n" + dato.trim();
										}
									}
								}
							} catch (Exception ex) {
							}
						}
					}

					if (newcontenido.length() > 0) {
						for (String dato : newcontenido.split("\n")) {
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


				
				
				return modelo;
			} catch (Exception ex) {
				modelo.setError(
						GnpVIdaModel2.this.getClass().getTypeName() + " | " + ex.getMessage() + " | " + ex.getCause());
				return modelo;
			}
		}
		
}
