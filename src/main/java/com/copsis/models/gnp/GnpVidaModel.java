package com.copsis.models.gnp;

import java.util.ArrayList;
import java.util.List;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraAseguradosModel;
import com.copsis.models.EstructuraBeneficiariosModel;
import com.copsis.models.EstructuraCoberturasModel;
import com.copsis.models.EstructuraJsonModel;
import com.copsis.models.EstructuraRecibosModel;

public class GnpVidaModel {

	// Clases
	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
	// Varaibles
	private String contenido = "", newcontenido = "",resultado = "";
	private int inicio = 0, fin = 0,donde =0, cuantos=0;

	public GnpVidaModel(String contenido) {
		this.contenido = contenido;
	}

	public EstructuraJsonModel procesar() {
		contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());
		try {
			
			modelo.setTipo(5);
            modelo.setCia(18);
            if (contenido.split("@@@").length >= 3) {
                newcontenido = fn.filtroPorRango(contenido, 3);
                donde = fn.recorreContenido(newcontenido, "Póliza No.");
                for (String dato : newcontenido.split("@@@")[donde].split("\r\n")) {
                    if (dato.contains("Póliza No.")) {
                        if (dato.split("###").length == 2) {
                            modelo.setPoliza(dato.split("###")[1].trim());
                            if (dato.split("###")[0].contains("Vida")) {
                                if (dato.split("###")[0].split("Vida").length == 3) {
                                    if (dato.split("###")[0].split("Vida")[2].contains("Póliza")) {
                                        modelo.setPlan("Vida " + dato.split("###")[0].split("Vida")[2].split("Póliza")[0].trim());
                                    }
                                }
                                if (dato.split("###")[0].split("Vida")[1].contains("Póliza")) {
                                    modelo.setPlan(dato.split("###")[0].split("Vida")[1].split("Póliza")[0].trim());
                                }
                            }
                        } else if (dato.split("###").length == 3) {
                            if (dato.split("###")[1].contains("Póliza No.")) {
                                modelo.setPoliza(dato.split("###")[2].trim());
                                if (dato.split("###")[1].trim().split("Póliza No.").length == 1) {
                                    modelo.setPlan(dato.split("###")[1].trim().split("Póliza No.")[0].trim());
                                }
                            }
                        }
                    }
                }
            }

            donde = 0;
            newcontenido = fn.filtroPorRango(contenido, 6);
            donde = fn.recorreContenido(newcontenido, "Contratante###") + 1;
            if (donde > 1) {
                if (newcontenido.split("@@@")[donde].split("\r\n")[0].split("###").length == 2) {
                    modelo.setCteNombre(newcontenido.split("@@@")[donde].split("\r\n")[0].split("###")[0].trim());
                } else if (newcontenido.split("@@@")[donde].split("\r\n")[0].split("###").length == 1) {
                    if (newcontenido.split("@@@")[donde].split("\r\n")[0].contains("Grupo Nacional Provincial")) {//CONDICIONES
                        for (int i = 0; i < newcontenido.split("@@@")[(donde + 1)].split("\r\n").length; i++) {
                            if (newcontenido.split("@@@")[(donde + 1)].split("\r\n")[i].contains("C ontratante")) {
                                if ((i + 2) < newcontenido.split("@@@")[(donde + 1)].split("\r\n").length && newcontenido.split("@@@")[(donde + 1)].split("\r\n")[(i + 2)].trim().split("###").length == 1) {
                                    modelo.setCteNombre(newcontenido.split("@@@")[(donde + 1)].split("\r\n")[(i + 2)].trim().replace("###", "").trim());
                                }
                            }
                        }
                    } else {
                        modelo.setCteNombre(newcontenido.split("@@@")[donde].split("\r\n")[0].trim());
                    }
                } else {

                    if (newcontenido.split("@@@")[donde - 1].contains("Contratante###")) {
                        for (int i = 0; i < newcontenido.split("@@@")[donde - 1].split("\r\n").length; i++) {
                            if (newcontenido.split("@@@")[donde - 1].split("\r\n")[i].contains("Contratante###")) {
                                if (newcontenido.split("@@@")[donde - 1].split("\r\n")[(i + 2)].split("###").length == 2) {
                                    modelo.setCteNombre(newcontenido.split("@@@")[donde - 1].split("\r\n")[(i + 2)].split("###")[0].trim());
                                } else {
                                    if (newcontenido.split("@@@")[donde - 1].split("\r\n")[(i + 2)].split("###").length == 1) {
                                        modelo.setCteNombre(newcontenido.split("@@@")[donde - 1].split("\r\n")[(i + 2)].trim());
                                    }
                                }

                            }
                        }
                    }
                }
            }

            donde = 0;
            newcontenido = fn.filtroPorRango(contenido, 8);
         
            if (fn.recorreContenido(newcontenido, "Contratante###") > 0) {
                donde = fn.recorreContenido(newcontenido, "Contratante###") + 1;
            } else if (fn.recorreContenido(newcontenido, "C ontratante###") > 0) {
                donde = fn.recorreContenido(newcontenido, "C ontratante###") + 1;
            }
    
            if (donde > 1) {
                if (newcontenido.split("@@@")[donde].split("\r\n").length == 5 || newcontenido.split("@@@")[donde].split("\r\n").length == 10) {
                    for (int i = 0; i < newcontenido.split("@@@")[donde].split("\r\n").length; i++) {
                        if (i == 1) {
                            if (newcontenido.split("@@@")[donde].split("\r\n")[i].contains("Vigencia") && newcontenido.split("@@@")[donde].split("\r\n")[i].split("###").length == 2) {
                            	if (newcontenido.split("@@@")[donde].split("\r\n")[i].split("###")[0].contains("C.P")) {
                                    resultado = newcontenido.split("@@@")[donde].split("\r\n")[i].split("###")[0].trim().split("C.P")[0].trim();
                                    modelo.setCp(newcontenido.split("@@@")[donde].split("\r\n")[i].split("###")[0].trim().split("C.P")[1].trim());
                                }
                            }
                        }
                        if (i == 2) {

                            if (newcontenido.split("@@@")[donde].split("\r\n")[i].split("###").length == 4) {
                            	resultado += " " + newcontenido.split("@@@")[donde].split("\r\n")[i].split("###")[0].trim();
                                modelo.setCteDireccion(resultado);
                            }
                        }
                        if (i == 3) {
                    
                            if (newcontenido.split("@@@")[donde].split("\r\n")[i].split("###").length == 5 && newcontenido.split("@@@")[donde].split("\r\n")[i].contains("R.F.C:")) {
                                if (newcontenido.split("@@@")[donde].split("\r\n")[i].split("###")[0].contains("R.F.C:")) {
                                    modelo.setRfc(newcontenido.split("@@@")[donde].split("\r\n")[i].split("###")[0].split(":")[1].trim());
                                }
                                
                                if (newcontenido.split("@@@")[donde].split("\r\n")[i].split("###")[1].equals(" Desde el")) {
                                    modelo.setVigenciaDe(fn.formatDate(newcontenido.split("@@@")[donde].split("\r\n")[i].split("###")[2].trim() + "-" + newcontenido.split("@@@")[donde].split("\r\n")[i].split("###")[3].trim() + "-" + newcontenido.split("@@@")[donde].split("\r\n")[i].split("###")[4].trim(),"dd-MM-yy"));;
                                }
                            }
                        }
                        if (i == 4) {
                            if (newcontenido.split("@@@")[donde].split("\r\n")[i].split("###").length == 6 && newcontenido.split("@@@")[donde].split("\r\n")[i].contains("Hasta el")) {
                                resultado = newcontenido.split("@@@")[donde].split("\r\n")[i].split("###")[3].trim() + "/" + newcontenido.split("@@@")[donde].split("\r\n")[i].split("###")[4].trim() + "/" + newcontenido.split("@@@")[donde].split("\r\n")[i].split("###")[5].trim();
                                if (resultado.contains("el")) {
                               
                                    modelo.setVigenciaA(fn.formatDate(resultado.split("el")[1].replace("/", "-").trim(),"dd-MM-yy"));
                                }
                            }
                        }
                    }
                } else if (newcontenido.split("@@@")[(donde - 1)].split("\r\n").length == 16) { //SEGUNDO CASO
                    for (int i = 0; i < newcontenido.split("@@@")[(donde - 1)].split("\r\n").length; i++) {
                      	System.out.println(newcontenido.split("@@@")[(donde - 1)].split("\r\n")[i].split("###").length);
                        if (newcontenido.split("@@@")[(donde - 1)].split("\r\n")[i].split("###").length == 2) {
                            if (newcontenido.split("@@@")[(donde - 1)].split("\r\n")[i].split("###")[0].contains("C.P ")) {
                                resultado = newcontenido.split("@@@")[(donde - 1)].split("\r\n")[i].split("###")[0].split("C.P ")[0].trim();
                                modelo.setCp(newcontenido.split("@@@")[(donde - 1)].split("\r\n")[i].split("###")[0].split("C.P ")[1].trim());
                            }
                        }

                        if (newcontenido.split("@@@")[(donde - 1)].split("\r\n")[i].split("###").length == 4) {
                            resultado += " " + newcontenido.split("@@@")[(donde - 1)].split("\r\n")[i].split("###")[0].trim();
                            modelo.setCteDireccion(resultado);
                        }

                        if (newcontenido.split("@@@")[(donde - 1)].split("\r\n")[i].split("###").length == 5 && newcontenido.split("@@@")[(donde - 1)].split("\r\n")[i].contains("R.F.C:")) {
                            if (newcontenido.split("@@@")[(donde - 1)].split("\r\n")[i].split("###")[0].contains("R.F.C:")) {
                                modelo.setRfc(newcontenido.split("@@@")[(donde - 1)].split("\r\n")[i].split("###")[0].split(":")[1].trim());
                            }
                            if (newcontenido.split("@@@")[(donde - 1)].split("\r\n")[i].split("###")[1].equals(" Desde el")) {
                             
                            	modelo.setVigenciaDe(fn.formatDate(newcontenido.split("@@@")[(donde - 1)].split("\r\n")[i].split("###")[2].trim() + "-" + newcontenido.split("@@@")[(donde - 1)].split("\r\n")[i].split("###")[3].trim() + "-" + newcontenido.split("@@@")[(donde - 1)].split("\r\n")[i].split("###")[4].trim(),"dd-MM-yy"));
                            }
                        }

                        if (newcontenido.split("@@@")[(donde - 1)].split("\r\n")[i].split("###").length == 6 && newcontenido.split("@@@")[(donde - 1)].split("\r\n")[i].contains("Hasta el")) {
                            resultado = newcontenido.split("@@@")[(donde - 1)].split("\r\n")[i].split("###")[3].trim() + "/" + newcontenido.split("@@@")[(donde - 1)].split("\r\n")[i].split("###")[4].trim() + "/" + newcontenido.split("@@@")[(donde - 1)].split("\r\n")[i].split("###")[5].trim();
                            if (resultado.contains("el")) {
                         
                                modelo.setVigenciaA(fn.formatDate(resultado.split("el")[1].replace("/", "-").trim(),"dd-MM-yy"));
                            }
                        }

                    }
                }
            }

            donde = 0;
            donde = fn.recorreContenido(contenido, "Día###Mes###Año###");
            cuantos = 0;
            if (donde > 0) {
                if (contenido.split("@@@")[donde].split("\r\n").length == 3 || contenido.split("@@@")[donde].split("\r\n").length == 11 || contenido.split("@@@")[donde].split("\r\n").length == 16) {
                    for (String dato : contenido.split("@@@")[donde].split("\r\n")) {
                        if (dato.contains("Forma de pago")) {
                            if (dato.split("###").length == 8) {
                                if (dato.split("###")[4].contains("pago")) {
                                    modelo.setFormaPago(fn.formaPago(dato.split("###")[5].trim()));
                                }
                                if (dato.split("###")[6].contains("Neta")) {
                                    modelo.setPrimaneta(fn.castFloat(dato.split("###")[7].trim()));
                                }
                                if (dato.split("###")[6].contains("Derecho de")) {
                                    modelo.setDerecho(fn.castFloat(dato.split("###")[7].trim()));
                                }
                            }

                        }

                        if (dato.split("###").length == 2) {
                            if (dato.split("###")[0].contains("Prima Neta")) {
                                modelo.setPrimaneta(fn.castFloat(dato.split("###")[1].trim()));
                            }
                        }

                        if (dato.contains("Moneda###")) {
                            if (dato.split("###").length == 3) {
                                if (dato.split("###")[0].contains("Moneda")) {
                                    modelo.setMoneda(fn.moneda(dato.split("###")[1].trim()));
                                    cuantos += 1;
                                }
                                if (dato.split("###")[2].contains("I.V.A")) {
                                    modelo.setIva(fn.castFloat(dato.split("###")[3].trim()));
                                }
                            }
                        }
                    }
                }
                modelo.setModelo(donde);

                if (cuantos == 0) {
                    if (contenido.split("@@@")[(donde + 1)].split("\r\n")[0].split("###").length == 4) {
                        if (contenido.split("@@@")[(donde + 1)].split("\r\n")[0].split("###")[0].contains("Moneda")) {
                            modelo.setMoneda(fn.moneda(contenido.split("@@@")[(donde + 1)].split("\r\n")[0].split("###")[1].trim()));
                        }
                        if (contenido.split("@@@")[(donde + 1)].split("\r\n")[0].split("###")[0].contains("Moneda")) {
                            modelo.setMoneda(fn.moneda(contenido.split("@@@")[(donde + 1)].split("\r\n")[0].split("###")[1].trim()));
                        }
                        if (contenido.split("@@@")[(donde + 1)].split("\r\n")[0].split("###")[2].contains("I.V.A.")) {
                            modelo.setIva(fn.castFloat(contenido.split("@@@")[(donde + 1)].split("\r\n")[0].split("###")[3].trim()));
                        }
                    }

                    if (contenido.split("@@@")[(donde + 2)].split("\r\n")[0].split("###").length == 2) {
                        if (contenido.split("@@@")[(donde + 2)].split("\r\n")[0].split("###")[0].contains("Importe a pagar")) {
                            modelo.setPrimaTotal(fn.castFloat(contenido.split("@@@")[(donde + 2)].split("\r\n")[0].split("###")[1].trim()));
                        }
                    }

                }
            }

            donde = 0;
            if (contenido.split("@@@").length >= 9) {
                newcontenido = fn.filtroPorRango(contenido, 8);
                donde = fn.recorreContenido(newcontenido, "Fraccionado");
                if (donde > 0) {
                    for (String dato : newcontenido.split("@@@")[donde].split("\r\n")) {

                        if (dato.contains("Fraccionado")) {
                            if (dato.split("###").length == 7) {
                                if (dato.split("###")[5].contains("Fraccionado")) {
                                    modelo.setRecargo(fn.castFloat(dato.split("###")[6].trim()));
                                }
                            }
                        }
                        if (dato.split("###").length == 2) {
                            if (dato.split("###")[0].contains("Fraccionado")) {
                                modelo.setRecargo(fn.castFloat(dato.split("###")[1].trim()));
                            }
                        }
                        if (dato.split("###").length == 2) {
                            if (dato.split("###")[0].contains("Importe a pagar")) {
                                modelo.setPrimaTotal(fn.castFloat(dato.split("###")[1].trim()));
                            }
                        }
                    }
                }
            }

            donde = 0;
            donde = fn.searchTwoTexts(contenido, "Clave###", "Agente###");
            if (donde > 0) {
                for (String dato : contenido.split("@@@")[donde].split("\r\n")) {
                    if (dato.contains("Agente") && dato.split("###").length == 5) {
                        if (dato.split("###")[0].equals("Agente")) {
                            modelo.setAgente(dato.split("###")[1].trim());
                        }
                        if (dato.split("###")[2].contains("Clave")) {
                            modelo.setCveAgente(dato.split("###")[3].trim());
                        }
                    } else if (dato.contains("Agente") && dato.split("###").length == 4) {
                        if (dato.split("###")[0].equals("Agente")) {
                            modelo.setAgente(dato.split("###")[1].trim());
                        }
                        if (dato.split("###")[2].contains("Clave")) {
                            modelo.setCveAgente(dato.split("###")[3].trim().replace(" ", "###").split("###")[0].trim());
                        }
                    }
                }
            }

           
            
            donde = fn.recorreContenido(contenido, "Asegurado s");
            if (donde > 0) {
            	 List<EstructuraAseguradosModel> asegurados = new ArrayList<>();
                for (int i = 0; i < contenido.split("@@@")[donde].split("\r\n").length; i++) {
                	EstructuraAseguradosModel asegurado = new EstructuraAseguradosModel();
                    //****************************************************CUANDO 1 ASEGURADO
                    if (contenido.split("@@@")[donde].split("\r\n")[i].contains("Asegurado 1") && contenido.split("@@@")[donde].split("\r\n")[i].contains("Asegurado  2") == false) {
                        if (contenido.split("@@@")[donde].split("\r\n")[i + 1].split("###").length == 1) {                           
                        	asegurado.setNombre(contenido.split("@@@")[donde].split("\r\n")[i + 1]);
                        	asegurados.add(asegurado);
                        }
                        if (contenido.split("@@@")[donde].split("\r\n")[i + 1].split("###").length == 2) {
                          
                            asegurado.setNombre(contenido.split("@@@")[donde].split("\r\n")[i + 1].split("###")[0].trim());
                            asegurados.add(asegurado);
                        }
                    }

                    if (contenido.split("@@@")[donde].split("\r\n")[i].split("###").length >= 2) {
                        if (contenido.split("@@@")[donde].split("\r\n")[i].split("###")[0].contains("Nacimiento")) {
                            asegurado.setNacimiento(fn.formatDate(contenido.split("@@@")[donde].split("\r\n")[i].split("###")[1].trim(),"dd-MM-yy"));
                            asegurados.add(asegurado);
                        }
                    }
                    //****************************************************CUANDO 2 ASEGURADOS
                    if (contenido.split("@@@")[donde].split("\r\n")[i].contains("Asegurado  1") && contenido.split("@@@")[donde].split("\r\n")[i].contains("Asegurado  2")) {
                        if (contenido.split("@@@")[donde].split("\r\n")[i + 1].split("###").length == 3) {
                          
                            asegurado.setNombre(contenido.split("@@@")[donde].split("\r\n")[i + 1].split("###")[0].trim());
                            asegurados.add(asegurado);
                            asegurado.setNombre(contenido.split("@@@")[donde].split("\r\n")[i + 1].split("###")[1].trim());
                            asegurados.add(asegurado);

                        }
                    }

                }
                
                modelo.setAsegurados(asegurados);
            }

			List<EstructuraCoberturasModel> coberturas = new ArrayList<>();
            //coberturas{nombre sa deducible coaseguro}
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
                                if (Double.parseDouble(dato.split("###")[1].replace(".", "").replace(",", "").trim()) >= 0) {
                                    if (dato.split("###")[0].contains("Hasta") == false
                                            && dato.split("###")[0].trim().equals("Movimiento") == false
                                            && dato.split("###")[0].trim().equals("Actual") == false
                                            && (dato.split("###")[0].contains("Importe") == false && dato.split("###")[0].contains("Total") == false)
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

            String a = contenido.split("Fecha de expedición")[1].split("Forma de pago")[0];
            modelo.setFechaEmision(fn.formatDate(fn.gatos(a).replace("###", "-"),"dd-MM-yy"));
            /////////////////////////////////////////////////////////////////////////////////////////////////////////
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
				recibo.setPrimaneta(fn.castBigDecimal(modelo.getPrimaneta(), 2));
				recibo.setDerecho(fn.castBigDecimal(modelo.getDerecho(), 2));
				recibo.setRecargo(fn.castBigDecimal(modelo.getRecargo(), 2));
				recibo.setIva(fn.castBigDecimal(modelo.getDerecho(), 2));

				recibo.setPrimaTotal(fn.castBigDecimal(modelo.getPrimaTotal(), 2));
				recibo.setAjusteUno(fn.castBigDecimal(modelo.getAjusteUno(), 2));
				recibo.setAjusteDos(fn.castBigDecimal(modelo.getAjusteDos(), 2));
				recibo.setCargoExtra(fn.castBigDecimal(modelo.getCargoExtra(), 2));
				recibos.add(recibo);
            }
        	modelo.setRecibos(recibos);

            String plazo = "";
            if (contenido.contains("Especificaciones del Plan") == true) {
                plazo = contenido.split("Especificaciones del Plan")[1].split("Agente")[0].replace("@@@", "")
                        .replace("\r\n", "").replace("Observaciones", "").replace("###", "").substring(0, 24)
                        .replace("9 m", "").trim();

                if (plazo.contains("Exención de Pago de Prim") == true) {
                    plazo = contenido.split("Especificaciones del Plan")[1].split("Plazo")[1].split("Agente")[0].replace("@@@", "").replace("\r\n", "");

                } else if (plazo.contains("LaprotecciónContratadase") == true) {
                    plazo = contenido.split("Especificaciones del Plan")[1].split("Plazo")[1].split("Agente")[0].split("Plan con incrementos")[0].replace("@@@", "").replace("\r\n", "");
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
            if (inicio > 0 & fin > 0 & inicio < fin) {
                newcontenido = contenido.substring(inicio, fin + 10).replace("@@@", "").trim();
                if (newcontenido.contains("Protección Contratada") & newcontenido.contains("Para mayor")) {
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
            List<EstructuraBeneficiariosModel> beneficiarios = new ArrayList<>();
            for (String beneficiariod : b.split("\n")) {
            	EstructuraBeneficiariosModel beneficiario = new EstructuraBeneficiariosModel();
                int aseg = beneficiariod.split("###").length;
                if (aseg == 4) {
                    if (beneficiariod.contains("Nombre")) {
                    } else if (beneficiariod.contains("@@@")) {
                    	beneficiario.setNombre(beneficiariod.split("###")[0].replace("@@@", "").trim());
                    	beneficiario.setNacimiento(fn.formatDate(beneficiariod.split("###")[1],"dd-MM-yy"));
                    	beneficiario.setParentesco(fn.parentesco(beneficiariod.split("###")[2].toLowerCase()));
                    	beneficiario.setPorcentaje(fn.castDouble(beneficiariod.split("###")[3].replace("\r", "")).intValue());
                    	beneficiario.setTipo(tip);
                    	beneficiarios.add(beneficiario);
                    } else {
                    	beneficiario.setNombre(beneficiariod.split("###")[0].replace("@@@", "").trim());
                    	beneficiario.setNacimiento(fn.formatDate(beneficiariod.split("###")[1],"dd-MM-yy"));
                    	beneficiario.setParentesco(fn.parentesco(beneficiariod.split("###")[2].toLowerCase()));
                    	beneficiario.setPorcentaje(fn.castDouble(beneficiariod.split("###")[3].replace("\r", "")).intValue());
                    	beneficiario.setTipo(tip);
                    	beneficiarios.add(beneficiario);
                    }
                }
            }

            modelo.setBeneficiarios(beneficiarios);
			return modelo;
		} catch (Exception ex) {
			modelo.setError(
					GnpVidaModel.this.getClass().getTypeName() + " | " + ex.getMessage() + " | " + ex.getCause());
			return modelo;
		}

	}

}
