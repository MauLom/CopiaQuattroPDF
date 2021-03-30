package com.copsis.models.chubb.autos;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.math.NumberUtils;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraCoberturasModel;
import com.copsis.models.EstructuraJsonModel;

import lombok.Data;

@Data
public class ChubbAutosModel {
	// Clases
	DataToolsModel fn = new DataToolsModel();

	// Variables
	private String contenido;
	private String recibos = "";

	public EstructuraJsonModel procesar() {
		// Clases
		EstructuraJsonModel modelo = new EstructuraJsonModel();
		try {
			// Clases
			EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();

			// Variables
			String F_DEPAGO;
			BigDecimal r_prima_neta;
			BigDecimal r_recargo;
			BigDecimal r_derecho;
			BigDecimal r_iva;
			BigDecimal r_prima_total;
			BigDecimal r_ajuste2;
			BigDecimal r_ajuste1;
			int numero_recibo;
			int inicio = 0;
			int fin = 0;
			int donde = 0;
			String resultado = "";
			String newcontenido = "";
			String separador = "###";
			String saltolinea = "\r\n";
			float restoPrimaTotal = 0;
			float restoDerecho = 0;
			float restoIva = 0;
			float restoRecargo = 0;
			float restoPrimaNeta = 0;
			float restoAjusteUno = 0;
			float restoAjusteDos = 0;
			float restoCargoExtra = 0;
			List<String> conceptos;

			contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());
			System.out.println(contenido);

			// tipo
			modelo.setTipo(1);

			// aseguradora
			modelo.setCia(1);

			// Ramo
			modelo.setRamo("Autos");

			// Moneda
			modelo.setMoneda(
					fn.moneda(contenido.split("Moneda:")[1].split("Forma de pago:")[0].replace("###", "").trim()));

			// Renovacion
			donde = 0;
			donde = fn.recorreContenido(contenido, "Póliza anterior:");
			System.out.println(donde);
			if (donde > 0) {
				for (String dato : contenido.split("@@@")[donde].split("\r\n")) {
					if (dato.split("###").length == 1) {
						if (dato.split("###")[0].contains("anterior:")) {
							modelo.setRenovacion(dato.split("###")[1].trim());
						}
					}
				}

			}

			// Plan
			donde = 0;
			donde = fn.recorreContenido(contenido, "Paquete:");
			if (donde > 0) {
				for (String dato : contenido.split("@@@")[donde].split("\r\n")) {
					if (dato.split("###").length == 2) {
						if (dato.split("###")[0].equals("Paquete:")) {
							modelo.setPlan(dato.split("###")[1].trim());
						}
					} else if (dato.split("###").length == 7) {
						if (dato.split("###")[5].equals("Paquete:")) {
							modelo.setPlan(dato.split("###")[6].trim());
						}
					} else if (dato.split("###").length == 8) {
						if (dato.split("###")[6].equals("Paquete:")) {
							modelo.setPlan(dato.split("###")[7].trim());
						}
					}
				}
			}

			// FormaPago
				conceptos = Arrays.asList("Forma de pago:");
				for (String x : conceptos) {
					inicio = contenido.indexOf(x);
					if (inicio > -1) {
						switch (x) {
						case "Forma de pago:":
							inicio = inicio + 17;
							newcontenido = contenido.substring(inicio, (inicio + 100));
							System.out.println(newcontenido);
							modelo.setFormaPago(fn.formaPago(newcontenido.split(saltolinea)[0].trim()));
							break;
						}
					}
				}
			
//			inicio = contenido.indexOf("Forma de pago:");
//			fin = contenido.indexOf("Fecha de emisión:");
//			if (inicio > 0 && fin > 0 && inicio < fin) {
//				String PAGO = contenido.split("Forma de pago:")[1].split("Fecha de emisión:")[0].replace(".", "")
//						.replace("###", "").trim();
//				System.out.println("---< " + PAGO);
//				modelo.setFormaPago(fn.formaPago(PAGO.trim()));
//			}

			// poliza
				conceptos = Arrays.asList("Póliza:###");
			for (String x : conceptos) {
				inicio = contenido.indexOf(x);
				if (inicio > -1) {
					switch (x) {
					case "Póliza:###":
						inicio = inicio + 10;
						newcontenido = contenido.substring(inicio, (inicio + 100));
						modelo.setPoliza(newcontenido.split(separador)[0].trim() + " " + newcontenido.split(separador)[1].trim());
						break;
					}
				}
			}

			// endoso
			conceptos = Arrays.asList("Endoso:###");
			for (String x : conceptos) {
				inicio = contenido.indexOf(x);
				if (inicio > -1) {
					switch (x) {
					case "Endoso:###":
						inicio = inicio + 10;
						newcontenido = contenido.substring(inicio, (inicio + 100));
						modelo.setEndoso(newcontenido.split(separador)[0].trim());
						break;
					}
				}
			}

			//Contratante
			conceptos = Arrays.asList("Propietario-Contratante:###");
			for (String x : conceptos) {
				inicio = contenido.indexOf(x);
				if (inicio > -1) {
					switch (x) {
					case "Propietario-Contratante:###":
						inicio = inicio + 27;
						newcontenido = contenido.substring(inicio, (inicio + 150));
						modelo.setContratante(newcontenido.split(saltolinea)[0].trim());
						break;
					}
				}
			}
			
			//CteNombre
			conceptos = Arrays.asList("Datos del asegurado y-o propietario");
			for (String x : conceptos) {
				inicio = contenido.indexOf(x);
				if (inicio > -1) {
					switch (x) {
					case "Datos del asegurado y-o propietario":
						inicio = inicio + 35;
						newcontenido = contenido.substring(inicio, (inicio + 150));
						modelo.setCteNombre(newcontenido.split(saltolinea)[1].split(separador)[1].trim());
						break;
					}
				}
			}

			// cte_direccion
			conceptos = Arrays.asList("Domicilio:###");
			for (String x : conceptos) {
				inicio = contenido.indexOf(x);
				if (inicio > -1) {
					switch (x) {
					case "Domicilio:###":
						inicio = inicio + 13;
						newcontenido = contenido.substring(inicio, (inicio + 100));
						modelo.setCteDireccion(newcontenido.split(separador)[0].trim());
						break;
					}
				}
			}

			// rfc
			conceptos = Arrays.asList("R.F.C.:", "RFC:###");
			for (String x : conceptos) {
				inicio = contenido.indexOf(x);
				if (inicio > -1) {
					switch (x) {
					case "R.F.C.:":
					case "RFC:###":
						inicio = inicio + 7;
						newcontenido = contenido.substring(inicio, (inicio + 100));
						modelo.setRfc(newcontenido.split(separador)[0].trim());
						break;
					}
				}
			}

			// Inciso
			conceptos = Arrays.asList("Inciso:###");
			for (String x : conceptos) {
				inicio = contenido.indexOf(x);
				if (inicio > -1) {
					switch (x) {
					case "Inciso:###":
						inicio = inicio + 10;
						newcontenido = contenido.substring(inicio, (inicio + 100));
						if(NumberUtils.isParsable(newcontenido.split(separador)[0].trim())) {
							modelo.setInciso(Integer.parseInt(newcontenido.split(separador)[0].trim()));	
						}
						break;
					}
				}
			}
			
//            String a;
//            if (contenido.indexOf("@@@Prima neta###") > 0) {
//                a = "@@@Prima neta###";
//            } else {
//                a = "@@@Prima Neta###";
//            }
//
//            //prima_neta
//            modelo.setPrima_neta(Float.parseFloat(fn.cleanString(contenido.split(a)[1].split("Otros descuentos")[0].replace("###", "").trim())));
//
//            //prima_total
//            inicio = contenido.indexOf("###Prima total###");
//            fin = contenido.indexOf("@@@Página 1 de 2");
//
//            if (fin > 0) {
//                inicio = contenido.indexOf("###Prima total###");
//                if (inicio > 0) {
//                    inicio = contenido.indexOf("###Prima total###") + 17;
//                    fin = contenido.indexOf("@@@Página 1 de 2");
//                    
//                    if (inicio > 0 && fin > 0 && inicio < fin) {
//                        modelo.setPrima_total(Float.parseFloat(fn.cleanString(contenido.substring(inicio, fin).replace("###", "").trim())));
//                    }
//
//                }
//
//            } else {
//                inicio = contenido.indexOf("Prima Total:###");
//                if (inicio > -1) {
//                    inicio = contenido.indexOf("Prima Total:###") + 12;
//                    fin = contenido.indexOf("@@@página 1 de 2");
//
//                    if (inicio > 0 && fin > 0 && inicio < fin) {
//                        String conte_prima = contenido.substring(inicio, fin).replace("###", "").trim();
//                        if (conte_prima.contains("Encumplimientoalo")) {
//                            modelo.setPrima_total(Float.parseFloat(fn.cleanString(conte_prima.split("\n")[0].trim())));
//                        } else {
//                            modelo.setPrima_total(Float.parseFloat(fn.cleanString(contenido.substring(inicio, fin).replace("###", "").trim())));
//                        }
//
//                    }
//                }
//
//            }
//
//            this.limpio(inicio, fin);
//            //iva
//            inicio = contenido.indexOf("I.V.A.###");
//            if (inicio > 0) {
//                inicio = contenido.indexOf("I.V.A.###") + 9;
//                modelo.setIva(Float.parseFloat(fn.cleanString(contenido.substring(inicio, inicio + 30).split("\r\n")[0])));
//            }
//
//            inicio = 0;
//
//            inicio = contenido.indexOf("Clave interna del agente:");
//            fin = contenido.indexOf("Descripción del vehículo");
//
//            if (inicio > 0 && fin > 0 && inicio < fin) {
//                newcontenido = contenido.substring(inicio, fin);
//                String ag = "";
//                if (newcontenido.contains("- 0 -") == true) {
//                    ag = "- 0 -";
//                } else if (newcontenido.contains("###0 -") == true) {
//                    ag = "###0 -";
//                } else if (newcontenido.contains("- 1 -") == true) {
//                    ag = "- 1 -";
//                } else if (newcontenido.contains("- 2 -") == true) {
//                    ag = "- 2 -";
//                } else if (newcontenido.contains("- 3 -") == true) {
//                    ag = "- 3 -";
//                } else if (newcontenido.contains("###0###-") == true) {
//                    ag = "###0###-";
//                }
//
//                inicio = contenido.indexOf("Clave interna del agente:");
//
//                if (inicio > 0) {
//                    String clav_agente = contenido.split("Clave interna del agente:")[1];
//
//                    if (clav_agente.contains("Descripción del vehículo") == true) {
//                        String agente = contenido.split("Clave interna del agente:")[1].split("Descripción del vehículo")[0]
//                                .split(ag)[1];
//                        String cv_agente = contenido.split("Clave interna del agente:")[1].split("Descripción del vehículo")[0]
//                                .split(ag)[0].replace("###", "");
//                        // agente
//                        modelo.setAgente(agente.replace("\r\n", "").replace("###", " ").replace("@@@", "").trim());
//                      
//                        modelo.setCve_agente(cv_agente.replace("Conducto:", "").replace("191748Conducto:", "").trim());
//
//                    } else {
//
//                    }
//                }
//
//            }
//
//            String Vigenciaspolizas = contenido.split("Vigencia")[1].split("Inciso")[0].replace("Del", "").replace("al", "").replace("12:00 horas", "").replace(":", "").replace("######", "");
//
//            if (Vigenciaspolizas.split("###").length == 4) {
//                //vigencia_de 
//                modelo.setVigencia_de(fn.formatDate_MonthCadena(Vigenciaspolizas.split("###")[0]));
//                //vigencia_a 
//                modelo.setVigencia_a(fn.formatDate_MonthCadena(Vigenciaspolizas.split("###")[2].trim()));
//            } else {
//                //vigencia_de
//                modelo.setVigencia_de(fn.formatDate_MonthCadena(Vigenciaspolizas.split("###")[0]));
//                modelo.setVigencia_a(fn.formatDate_MonthCadena(Vigenciaspolizas.split("###")[1].trim()));
//            }
//
//            //cp
//            inicio = contenido.indexOf("###C.P.:###");
//           if (inicio > -1) {
//                newcontenido = contenido.substring(inicio + 11, inicio + 16);
//                modelo.setCp(newcontenido.replace("\n\r", ""));
//            }
//
//            //recargo = financiamiento pago fraccionado
//            String recargo = contenido.split("Financiamiento por pago fraccionado")[1].split("Gastos de expedición")[0].split("\n")[0].replace("###", "");
//
//            modelo.setRecargo(Float.parseFloat(fn.cleanString(recargo.trim())));
//            r_recargo = fn.cleanStringv2(recargo.trim());
//
//            inicio = contenido.indexOf("Gastos de expedición###");
//            if (inicio > 0) {
//                //derecho = gastos de expedicion
//                inicio = contenido.indexOf("Gastos de expedición###") + 23;
//                fin = contenido.indexOf("I.V.A.###");
//                if (inicio > 0 && fin > 0 && inicio < fin) {
//                    modelo.setDerecho(Float.parseFloat(fn.cleanString(contenido.substring(inicio, fin).trim())));
//                }
//
//            }
//
//            this.limpio(inicio, fin);
//
//            inicio = contenido.indexOf("Modelo:");
//
//            if (inicio > 0) {
//                String descriauto = contenido.split("Modelo:")[0];
//
//                if (descriauto.split("Clave interna del agente:")[1].split("\n")[2].split("###").length > 1) {
//                    modelo.setDescripcion(descriauto.split("Clave interna del agente:")[1].split("\n")[2].split("###")[1].replace("\r", ""));
//                } else {
//
//                    inicio = contenido.indexOf("Descripción del vehículo*:");
//                    fin = contenido.indexOf("Modelo:");
//                    if (inicio > 0 && fin > 0 && inicio < fin) {
//                        newcontenido = contenido.substring(inicio, fin);
//                        modelo.setDescripcion(newcontenido.replace("Descripción del vehículo*:", "").trim());
//                    } else {
//                        modelo.setDescripcion(descriauto.split("Clave interna del agente:")[1].split("\n")[2].split(":")[1].replace("\r", ""));
//                    }
//
//                }
//
//            }
//
//            //clave
//            donde = 0;
//            donde = fn.recorreContenido(contenido, "Clave vehicular:");
//
//            if (donde > 0) {
//                for (String dato : contenido.split("@@@")[donde].split("\r\n")) {
//                    if (dato.contains("Clave vehicular:")) {
//                        if (dato.split("###").length == 4) {
//                            if (dato.split("###")[0].contains("Clave vehicular:")) {
//                                modelo.setClave(dato.split("###")[0].split("vehicular:")[1].trim());
//                            }
//                        } else if (dato.split("###").length == 3) {
//                            if (dato.split("###")[0].contains("Clave vehicular:")) {
//                                modelo.setClave(dato.split("###")[0].split("vehicular:")[1].trim());
//                            }
//                        } else {
//                            if (dato.split("###").length == 6) {
//                                if (dato.split("###")[0].contains("Clave vehicular:")) {
//                                    modelo.setClave(dato.split("###")[1].trim());
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//            modelo.setModelo(Integer.parseInt(fn.cleanString(contenido.split("Modelo:")[1].split("Serie:")[0].replace("###", ""))));
//            //serie
//            modelo.setSerie(contenido.split("Serie:")[1].split("Marca:")[0].replace("###", "").replace("\r\n", "").replace("@@@", "").trim());
//            //motor
//            modelo.setMotor(contenido.split("Motor:")[1].split("Descripción de abreviaturas")[0].split("\n")[0].replace("###", "").replace("\r", ""));
//            //placas
//
//            String placas = contenido.split("Placas:")[1].split("Desglose de coberturas")[0].replace("###", "").replace("\r\n", "").replace("@@@", "");
//
//            inicio = placas.indexOf("*Descripción de abreviaturas");
//
//            if (inicio > 0) {
//                int infb= placas.indexOf("Uso");
//                 if(infb  > 0){
//                 modelo.setPlacas(placas.split("Uso")[0].replace("*", "").trim());
//                 }else{
//                 modelo.setPlacas(placas.split("Descripción de abreviaturas")[0].replace("*", "").trim());
//                 }
//                
//            } else {
//
//                if (placas.split("###").length > 1) {
//                    
//                    modelo.setPlacas(placas);
//                } else {
//                    if (placas.length() > 20) {
//                    } else {
//                   
//                        modelo.setPlacas(placas.trim());
//                    }                  
//                }
//            }
//            if(modelo.getPlacas().length()> 10){
//             modelo.setPlacas(modelo.getPlacas().substring(0, 9));
//            }
//
//            modelo.setFecha_emision(fn.formatDate_MonthCadena(contenido.split("Fecha de emisión:")[1].split("Referencia:")[0].replace("###", "").replace("DE", "/").replace(" ", "")));
//            modelo.setMarca(contenido.split("Marca:")[1].split("Capacidad:")[0].replace("###", "").trim());
//
//            modelo.setId_cliente(contenido.split("Asegurado:")[1].split("Paquete:")[0].replace("###", "").trim());

			return modelo;
		} catch (Exception ex) {
			modelo.setError(
					ChubbAutosModel.this.getClass().getTypeName() + " | " + ex.getMessage() + " | " + ex.getCause());
			return modelo;
		}
	}
}
