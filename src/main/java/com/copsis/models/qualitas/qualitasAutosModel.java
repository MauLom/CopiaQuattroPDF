package com.copsis.models.qualitas;

import java.math.BigDecimal;

import org.json.JSONArray;
import org.json.JSONObject;

import com.copsis.models.DatosToolsModel;
import com.copsis.models.EstructuraCoberturasModel;
import com.copsis.models.EstructuraJsonModel;
import com.copsis.models.EstructuraRecibosModel;

public class qualitasAutosModel {

	    private DatosToolsModel fn = new DatosToolsModel();
	    private EstructuraJsonModel modelo = new EstructuraJsonModel();
	    public String contenido = "";

	    public qualitasAutosModel(String contenido) {
	        this.contenido = contenido;
	    }
	    
	    private int donde = 0, index = 0;
	    private int inicio = 0, inicioaux = 0, fin = 0;
	    private String texto = "", subtxt = "";
	    private float restoPrimaTotal = 0, restoDerecho = 0, restoIva = 0, restoRecargo = 0, restoPrimaNeta = 0, restoAjusteUno = 0, restoAjusteDos = 0, restoCargoExtra = 0;

	    
	    public JSONObject procesar() {
	        String newcontenido = "";
	        JSONObject jsonObject = new JSONObject();
	        JSONArray jsonArrayCob = new JSONArray();
	        JSONArray jsonArrayRec = new JSONArray();
	        contenido = fn.fixContenido(contenido);
	        contenido = contenido
	                .replace("Hasta  las", "Hasta las")
	                .replace("IMPORTE TOTAL.", "IMPORTE TOTAL")
	                .replace("RENUEVA  A :", "RENUEVA A:")
	                .replace("Inicial :", "Inicial:")
	                .replace("DEL  ASEGURADO", "DEL ASEGURADO")
	                .replace("RREENNUUEEVVAA", "RENUEVA")
	                .replace("MEsutnaidciop i:o:", "Municipio:")
	                .replace("Domicilio :", "Domicilio:")
	                .replace("Expedición.", "Expedición");

	        try {
	            //cia
	            modelo.setCia(29);

	            //tipo
	            modelo.setTipo(1);

	            //fecha_emision
	            inicio = contenido.indexOf("IMPORTE TOTAL");
	            fin = contenido.lastIndexOf("www.qualitas.com.mx");
	            if (inicio > -1 && fin > inicio) {
	                newcontenido = contenido.substring(inicio, fin);
	                for (String x : newcontenido.split("\r\n")) {

	                    if (x.contains(" DE ")) {

	                        if (x.split(" DE ").length == 3) {

	                            if (x.contains("A ")) {

	                                x = x.split("A ")[1];
	                                x = x.replace(" DE ", "/");
	                                if (x.split("/").length > 2) {
	                                    modelo.setFecha_emision(fn.formatDate_MonthCadena(x));
	                                }

	                            }
	                        }
	                    }
	                }
	            }

	            //poliza
	            //endoso
	            //inciso
	            inicio = contenido.lastIndexOf("ENDOSO###INCISO");
	            fin = contenido.lastIndexOf("INFORMACIÓN DEL ASEGURADO");

	            if (inicio > 0 & fin > 0 & inicio < fin) {
	                newcontenido = "";
	                newcontenido = contenido.substring(inicio, fin).replace("@@@", "").replace("\r", "");
	                for (int i = 0; i < newcontenido.split("\n").length; i++) {
	                  
	                    if (newcontenido.split("\n")[i].contains("AUTOMÓVILES")) {
	                        if ((i + 1) == newcontenido.split("\n").length) {
	                            modelo.setPoliza(newcontenido.split("\n")[i].split("###")[1]);
	                            modelo.setEndoso(newcontenido.split("\n")[i].split("###")[2]);
	                            if (fn.isNumeric(newcontenido.split("\n")[i].split("###")[3].trim())) {
	                                modelo.setInciso(Integer.parseInt(newcontenido.split("\n")[i].split("###")[3].trim()));
	                            }

	                        } else {
	                            modelo.setPoliza(newcontenido.split("\n")[i + 1].split("###")[0]);
	                            modelo.setEndoso(newcontenido.split("\n")[i + 1].split("###")[1]);
	                            if (fn.isNumeric(newcontenido.split("\n")[i + 1].split("###")[2].trim())) {
	                                modelo.setInciso(Integer.parseInt(newcontenido.split("\n")[i + 1].split("###")[2].trim()));
	                            }

	                        }

	                    } else {
	                        if (newcontenido.split("\n")[i].contains("ENDOSO") && newcontenido.split("\n")[i].contains("INCISO")) {
	                            modelo.setPoliza(newcontenido.split("\n")[i + 1].split("###")[0]);
	                            modelo.setEndoso(newcontenido.split("\n")[i + 1].split("###")[1]);
	                            if (fn.isNumeric(newcontenido.split("\n")[i + 1].split("###")[2].trim())) {
	                                modelo.setInciso(Integer.parseInt(newcontenido.split("\n")[i + 1].split("###")[2].trim()));
	                            }
	                        }

	                    }

	                }
	            }

	            //cte_nombre
	            inicio = contenido.lastIndexOf("DEL ASEGURADO");
	            fin = contenido.lastIndexOf("Domicilio");

	            if (inicio > -1 && fin > inicio) {
	                newcontenido = contenido.substring(inicio + 13, fin);

	                if (newcontenido.contains("RENUEVA")) {
	                    modelo.setCte_nombre(newcontenido.split("RENUEVA")[0].replace("@@@", "").replace("###", "").trim());
	                } else {
	                    if (newcontenido.split("\r\n")[1].length() > 0) {
	                        modelo.setCte_nombre(newcontenido.split("\r\n")[1].replace("@@@", ""));
	                    }
	                }
	                if (newcontenido.contains("RENUEVA")) {
	                    modelo.setCte_nomina(newcontenido.split("RENUEVA")[0].replace("@@@", "").replace("###", "").trim());
	                }
	            }

	            //cte_direccion
	            inicio = contenido.lastIndexOf("Domicilio:");
	            fin = contenido.lastIndexOf("DESCRIPCIÓN DEL VEHÍCULO");
	            if (inicio > -1 && fin > inicio) {
	                newcontenido = contenido.substring(inicio, fin).trim();
	                //calle
	                inicio = newcontenido.indexOf("Domicilio:");
	                inicioaux = newcontenido.indexOf("Número:");
	                if (inicioaux == -1) {
	                    inicioaux = newcontenido.indexOf("No. EXT");
	                }
	                if (inicio > -1 && inicioaux > -1) {
	                    texto = newcontenido.substring(inicio + 10, inicioaux).replace("###", "").trim();
	                    /**
	                     * *********************************
	                     */
	                    //numero exterior
	                    subtxt = newcontenido.split("\r\n")[0];
	                    inicio = subtxt.indexOf("Número:");
	                    index = 7;
	                    if (inicio == -1) {
	                        inicio = subtxt.indexOf("No. EXT.");
	                        index = 8;
	                    }
	                    if (inicio == -1) {
	                        inicio = subtxt.indexOf("No. EXT");
	                        index = 7;
	                    }
	                    fin = subtxt.indexOf("Interior:");
	                    if (fin == -1) {
	                        fin = subtxt.indexOf("No. INT.");
	                    }
	                    if (fin == -1) {
	                        fin = subtxt.indexOf("Colonia:");
	                    }
	                    if (fin == -1) {
	                        fin = subtxt.indexOf("R.F.C.");
	                    }
	                    if (fin > -1) {
	                        texto += ", " + subtxt.substring(inicio + index, fin).replace("###", "").trim();
	                    }

	                    //numero interior
	                    inicio = subtxt.indexOf("Interior:");
	                    index = 9;
	                    if (inicio == -1) {
	                        inicio = subtxt.indexOf("No. INT.");
	                        index = 8;
	                    }
	                    if (inicio == -1) {
	                        inicio = subtxt.indexOf("Colonia:");
	                        index = 8;
	                    }
	                    if (inicio == -1) {
	                        inicio = subtxt.indexOf("R.F.C.");
	                        index = 6;
	                    }
	                    fin = subtxt.indexOf("R.F.C.");
	                    if (fin == -1) {
	                        fin = subtxt.indexOf("COL.");
	                    }
	                    if (inicio > -1 && fin > inicio) {
	                        if (subtxt.substring(inicio + index, fin).replace("###", "").trim().length() > 0) {
	                            texto += ", " + subtxt.substring(inicio + index, fin).split("\r\n")[0].replace("###", "").trim();
	                        }
	                    }

	                    //colonia
	                    inicio = newcontenido.indexOf("Colonia:");
	                    index = 8;
	                    if (inicio == -1) {
	                        inicio = newcontenido.indexOf("COL.");
	                        index = 4;
	                    }
	                    if (inicio > -1) {
	                        texto += ", " + newcontenido.substring(inicio + index, newcontenido.indexOf("\r\n", inicio)).replace("###", "").trim();
	                    }

	                    //municipio
	                    for (String x : newcontenido.split("\r\n")) {
	                        if (x.contains("Municipio:")) {
	                            inicio = x.indexOf("Municipio:");
	                            index = 10;
	                            fin = x.indexOf("Estado:");
	                            if (inicio > -1 && fin > inicio) {
	                                texto += ", " + x.substring(inicio + index, fin).replace("###", "").trim();
	                            }
	                        }
	                    }

	                    //estado
	                    inicio = newcontenido.indexOf("Estado:");
	                    index = 7;
	                    if (inicio > -1) {
	                        subtxt = newcontenido.substring(inicio + index, newcontenido.indexOf("\r\n", inicio + index));
	                        if (subtxt.contains("Colonia")) {
	                            texto += ", " + subtxt.split("Colonia")[0].replace("###", "").trim();
	                        } else if (subtxt.contains("R.F.C.")) {
	                            texto += ", " + subtxt.split("R.F.C.")[0].replace("###", "").trim();
	                        }
	                    }

	                    inicio = newcontenido.indexOf("C.P.");
	                    fin = newcontenido.indexOf("RFC");
	                    if (inicio > -1 && fin > inicio) {
	                        subtxt = fn.gatos(newcontenido.substring(inicio + 4, fin));
	                        if (subtxt.split("###").length == 2 && subtxt.split("\r\n").length == 1) {
	                            texto += ", " + subtxt.split("###")[1].trim();
	                        }
	                    }

	                }
	            }
	            modelo.setCte_direccion(texto);

	            //rfc
	            inicio = contenido.indexOf("R.F.C.:");
	            index = 7;
	            if (inicio == -1) {
	                inicio = contenido.lastIndexOf("RFC");
	                index = 3;
	            }
	            if (inicio > 0) {

	            } else {
	                inicio = contenido.lastIndexOf("R.F.C.");
	                index = 6;
	            }

	            if (inicio > -1) {
	                newcontenido = contenido.substring(inicio + index, contenido.indexOf("\r\n", inicio + index)).replace("###", "").replace("-", "").trim();
	                modelo.setRfc(newcontenido);
	            }

	            //moneda
	            inicio = contenido.indexOf("MONEDA");
	            if (inicio > -1) {
	                newcontenido = contenido.substring(inicio + 6, contenido.indexOf("\r\n", inicio + 6)).replace("###", "").trim();
	                modelo.setMoneda(fn.moneda(newcontenido));
	            }

	            //prima_neta
	            inicio = contenido.indexOf("Prima Neta");
	            if (inicio > -1) {
	                newcontenido = contenido.substring(inicio + 10, contenido.indexOf("\r\n", inicio + 10)).replace("###", "").replace(",", "").trim();
	                if (fn.isNumeric(newcontenido)) {
	                    modelo.setPrima_neta(Float.parseFloat(newcontenido));
	                }
	            }

	            //recargo
	            inicio = contenido.indexOf("Tasa Financiamiento");
	            if (inicio > -1) {
	                newcontenido = contenido.substring(inicio + 19, contenido.indexOf("\r\n", inicio + 19)).replace("###", "").replace(",", "").trim();
	                if (fn.isNumeric(newcontenido)) {
	                    modelo.setRecargo(Float.parseFloat(newcontenido));
	                }
	            }

	            //derecho
	            inicio = contenido.indexOf("por Expedición");//Gastos por Expedición.
	            if (inicio > -1) {
	                newcontenido = contenido.substring(inicio + 14, contenido.indexOf("\r\n", inicio + 14)).replace("###", "").replace(",", "").trim();
	                if (newcontenido.contains("Pa")) {
	                    newcontenido = newcontenido.split("Pa")[0].trim();
	                }
	                if (fn.isNumeric(newcontenido)) {
	                    modelo.setDerecho(Float.parseFloat(newcontenido));
	                }
	            }

	            //iva
	            inicio = contenido.indexOf("I.V.A.");
	            if (inicio > -1) {
	                newcontenido = contenido.substring(inicio, contenido.indexOf("\r\n", inicio));
	                if (newcontenido.contains("%")) {
	                    newcontenido = newcontenido.split("%")[1].replace("###", "").replace(",", "").trim();
	                    if (newcontenido.contains("Bitli")) {
	                        newcontenido = newcontenido.split("Bitli")[0].trim();
	                    }
	                    if (fn.isNumeric(newcontenido)) {
	                        modelo.setIva(Float.parseFloat(newcontenido));
	                    }
	                }
	            }

	            //prima_total
	            inicio = contenido.indexOf("IMPORTE TOTAL");
	            if (inicio > -1) {
	                newcontenido = contenido.substring(inicio + 13, contenido.indexOf("\r\n", inicio + 13)).replace("###", "").replace(",", "").trim();
	                if (fn.isNumeric(newcontenido)) {
	                    modelo.setPrima_total(Float.parseFloat(newcontenido));
	                }
	            }

	            //agente
	            //cve_agente
	            donde = 0;
	            donde = fn.recorreContenido(contenido, "Agente");
	            if (donde > 0) {
	                for (String dato : contenido.split("@@@")[donde].split("\r\n")) {
	                    if (dato.contains("Agente")) {
	                        switch (dato.split("###").length) {
	                            case 1:
	                                if (dato.contains("Agente:")) {
	                                    modelo.setCve_agente(dato.split("te:")[1].trim().split(" ")[0].trim());
	                                }
	                                if (modelo.getCve_agente().length() > 0) {
	                                    modelo.setAgente(dato.split(modelo.getCve_agente())[1].trim());
	                                }
	                                break;
	                            case 2:
	                                if (dato.split("###")[0].trim().equals("Agente:")) {
	                                    modelo.setCve_agente(dato.split("###")[1].trim().split(" ")[0].trim());
	                                    modelo.setAgente(dato.split("###")[1].trim().split(modelo.getCve_agente())[1].trim());
	                                }
	                                break;
	                            case 3:
	                                modelo.setCve_agente(dato.split("###")[1].trim());
	                                modelo.setAgente(dato.split("###")[2].trim());
	                                break;
	                        }
	                    }
	                }
	            }
	            if (modelo.getAgente().contains("ASEASOONR")) {
	                donde = 0;
	                donde = fn.recorreContenido(contenido, "IMPORTE TOTAL");
	                if (donde > 0) {
	                    for (String dato : contenido.split("@@@")[donde].split("\r\n")) {
	                        if (dato.contains("Agente")) {
	                            if (dato.split("###").length == 3) {
	                                if (dato.split("###")[2].contains("IMPORTE")) {
	                                    modelo.setAgente(dato.split("###")[1].trim());
	                                }
	                            }
	                        } else if (dato.contains("Clave")) {
	                            if (dato.split("###").length == 4) {
	                                if (dato.split("###")[2].contains("Teléfono:")) {
	                                    modelo.setCve_agente(dato.split("###")[1].trim());
	                                }
	                            }
	                        }
	                    }
	                }
	            }
	            //plan
	            inicio = contenido.indexOf("PLAN:");
	            if (inicio > -1) {
	                newcontenido = contenido.substring(inicio + 5, contenido.indexOf("\r\n", inicio + 5)).replace("###", "").trim();
	                modelo.setPlan(newcontenido);
	            }

	            //renovacion
	            inicio = contenido.indexOf("RENUEVA A:");
	            if (inicio > -1) {
	                newcontenido = contenido.substring(inicio + 10, contenido.indexOf("\r\n", inicio + 10)).replace("###", "").replace("-", "").trim();
	                modelo.setRenovacion(newcontenido);
	            }

	            //forma_pago
	            inicio = contenido.indexOf("Tasa Financiamiento");

	            if (inicio > -1) {
	                newcontenido = contenido.substring(inicio, inicio + 200).split("\r\n")[1];

	                if (newcontenido.contains("Gastos por")) {
	                    newcontenido = newcontenido.split("Gastos por")[0].replace("###", "").trim();
	                    if (newcontenido.contains("Forma de:")) {
	                        newcontenido = newcontenido.split("Forma de:")[1];
	                    }
	                    modelo.setForma_pago(fn.fPago(newcontenido));
	                } else if (newcontenido.contains("Forma de:")) {

	                    newcontenido = newcontenido.split("Forma de:")[1].replace("###", "").trim();

	                    if (newcontenido.contains("Primer pago")) {
	                        newcontenido = contenido.substring(inicio, inicio + 300).split("\r\n")[2];
	                        if (newcontenido.contains("Gastos por Expedición")) {
	                            modelo.setForma_pago(fn.fPago(newcontenido.split("###")[0].trim()));
	                        }

	                    } else {
	                        modelo.setForma_pago(fn.fPago(newcontenido));
	                    }

	                } else if (newcontenido.contains("Forma de Pago:")) {
	                    inicio = contenido.indexOf("Forma de Pago:");
	                    newcontenido = contenido.substring(inicio, inicio + 600).split("\r\n")[1];
	                    newcontenido = newcontenido.split("Forma de Pago:")[0].split("###")[0].trim();

	                    modelo.setForma_pago(fn.fPago(newcontenido));
	                }
	            }

	            /**
	             * **********para cuando trae para primer recibo y
	             * subsecuente*************
	             */
	            fin = contenido.indexOf("Pagos Subsecuentes");
	            if (fin > -1) {
	                index = contenido.substring(fin - 150, fin).split("\r\n").length - 1;
	                newcontenido = contenido.substring(fin - 150, fin).split("\r\n")[index].trim().split(" ")[0].replace("###", "").replace("@@@", "").trim();
	                modelo.setForma_pago(fn.fPago(newcontenido));
	            }

	            if (modelo.getForma_pago() == 0) {
	                inicio = contenido.lastIndexOf("Pago:");

	                if (inicio > -1) {
	                    newcontenido = contenido.substring(inicio + 5, inicio + 150).split("\r\n")[0].replace("###", "").trim();
	                    if (newcontenido.contains("12")) {
	                        modelo.setForma_pago(fn.fPago(newcontenido.split("12")[0].trim()));
	                    } else {
	                        modelo.setForma_pago(fn.fPago(newcontenido));
	                    }

	                }
	            }

	            //primer_prima_total
	            inicio = contenido.indexOf("Pago Inicial:");
	            index = 13;
	            if (inicio == -1) {
	                inicio = contenido.indexOf("Primer pago");
	                index = 11;
	            }
	            if (inicio > -1) {
	                newcontenido = fn.cleanString(contenido.substring(inicio + index, contenido.indexOf("\r\n", inicio + index)).replace("###", ""));
	                if (newcontenido.contains("Tasa Financiamiento")) {
	                    newcontenido = newcontenido.split("Tasa Financiamiento")[0].trim();
	                }
	                if (fn.isNumeric(newcontenido)) {
	                    modelo.setPrimer_prima_total(Float.parseFloat(newcontenido));
	                }
	            }

	            //sub_prima_total
	            inicio = contenido.indexOf("Pagos Subsecuentes:");
	            index = 19;
	            if (inicio == -1) {
	                inicio = contenido.indexOf("Pago(s) Subsecuente(s)");
	                index = 22;
	            }
	            if (inicio > -1) {
	                newcontenido = fn.cleanString(contenido.substring(inicio + index, contenido.indexOf("\r\n", inicio + 19)).replace("###", ""));
	                if (newcontenido.contains("Gastos por")) {
	                    newcontenido = newcontenido.split("Gastos por")[0].trim();
	                }
	                if (fn.isNumeric(newcontenido)) {
	                    modelo.setSub_prima_total(Float.parseFloat(newcontenido));
	                }
	            }
	            /**
	             * ********************************************
	             */

	            //vigencia_a
	            inicio = contenido.lastIndexOf("Hasta las");
	            if (inicio > -1) {
	                newcontenido = contenido.substring(inicio, contenido.indexOf("\r\n", inicio)).replace("del:", "del");
	                if (newcontenido.contains("Plazo")) {
	                    newcontenido = newcontenido.split("Plazo")[0].split("del")[1].replace("###", "").trim();
	                    if (newcontenido.length() == 11) {
	                        modelo.setVigencia_a(fn.formatDate_MonthCadena(newcontenido));
	                    }
	                }
	            }

	            //vigencia_de
	            inicio = contenido.lastIndexOf("Desde las");
	            if (inicio > -1) {
	                newcontenido = contenido.substring(inicio + 9, contenido.indexOf("\r\n", inicio + 9)).replace("del:", "del").replace("Servic  i o  :", "Servicio:");

	                if (newcontenido.contains("Servicio")) {
	                    newcontenido = fn.gatos(newcontenido.split("Servicio")[0].split("del")[1].trim());
	                    if (newcontenido.split("###").length == 2) {
	                        newcontenido = fn.formatDate_MonthCadena(newcontenido.split("###")[0].trim());
	                        if (newcontenido.length() == 10) {
	                            modelo.setVigencia_de(newcontenido);
	                        }
	                    }
	                } else {
	                    if (newcontenido.contains("Hasta las")) {
	                        newcontenido = newcontenido.split("Hasta las")[0].split("del")[1].replace("###", "").trim();
	                        modelo.setVigencia_de(fn.formatDate_MonthCadena(newcontenido));
	                        if (modelo.getVigencia_a().length() > 0) {
	                        } else {
	                            newcontenido = "";
	                            newcontenido = contenido.substring(inicio + 9, contenido.indexOf("\r\n", inicio + 9)).replace("del:", "del").replace("Servic  i o  :", "Servicio:");
	                            modelo.setVigencia_a(fn.formatDate_MonthCadena(newcontenido.split("\r\n")[0].split("Hasta las")[1].split("del")[1].replace("###", "").trim()));
	                        }
	                    }
	                }
	            }

	            //cp
	            inicio = contenido.lastIndexOf("C.P");
	            index = 3;
	            if (inicio > -1) {
	                newcontenido = contenido.substring(inicio + index, contenido.indexOf("\r\n", inicio + index)).replace(".:", "").replace(".", "").replace(":", "");
	                if (newcontenido.contains("Municipio")) {
	                    newcontenido = newcontenido.split("Municipio")[0].replace("###", "").trim();
	                    modelo.setCp(newcontenido);
	                } else if (newcontenido.split("###").length > 2) {
	                    newcontenido = fn.gatos(newcontenido);
	                    if (fn.isNumeric(newcontenido.split("###")[0].trim())) {
	                        modelo.setCp(newcontenido.split("###")[0].trim());
	                    }
	                } else {
	                    modelo.setCp(newcontenido);
	                }
	            }

	            //clave
	            inicio = contenido.indexOf("VEHÍCULO ASEGURADO\r\n");
	            index = 20;
	            if (inicio > -1) {
	                newcontenido = contenido.substring(inicio + index, contenido.indexOf("\r\n", inicio + index)).replace("@@@", "");
	                if (newcontenido.contains("(")) {
	                    newcontenido = newcontenido.replace("(", "&&&");
	                    newcontenido = newcontenido.split("&&&")[0].replace("###", "").trim();
	                    modelo.setClave(newcontenido);
	                } else if (newcontenido.split("###").length == 2) {
	                    modelo.setClave(newcontenido.split("###")[0].trim());
	                }
	            }

	            //marca
	            //descripcion
	            inicio = contenido.indexOf("VEHÍCULO ASEGURADO\r\n");
	            index = 20;
	            if (inicio > -1) {
	                newcontenido = contenido.substring(inicio + index, contenido.indexOf("\r\n", inicio + index));
	                if (newcontenido.contains("(")) {
	                    newcontenido = newcontenido.replace(")", "&&&");
	                    newcontenido = newcontenido.split("&&&")[1].replace("###", "").trim();
	                    modelo.setMarca(newcontenido.split(" ")[0].trim());
	                    modelo.setDescripcion(newcontenido);
	                } else if (newcontenido.split("###").length == 2) {
	                    newcontenido = newcontenido.split("###")[1].trim();
	                    modelo.setMarca(newcontenido.split(" ")[0].trim());
	                    modelo.setDescripcion(newcontenido);
	                }
	            }

	            //modelo
	            inicio = contenido.indexOf("Modelo:");
	            if (inicio > -1) {
	                newcontenido = contenido.substring(inicio + 7, contenido.indexOf("\r\n", inicio + 7));
	                if (newcontenido.contains("Color")) {
	                    newcontenido = newcontenido.split("Color")[0].replace("###", "").trim();
	                    if (fn.isNumeric(newcontenido)) {
	                        modelo.setModelo(Integer.parseInt(newcontenido));
	                    }
	                }
	            }

	            //serie
	            inicio = contenido.indexOf("Serie:");
	            fin = contenido.indexOf("Motor:");
	       
	            if (inicio > 0 & fin > 0 & inicio < fin) {
	                newcontenido = contenido.substring(inicio, fin).split("Serie:")[1].replace("###", "").trim();
	                modelo.setSerie(newcontenido);
	              
	            }

	            //motor
	            inicio = contenido.indexOf("Motor:");
	            if (inicio > -1) {
	                newcontenido = contenido.substring(inicio + 6, contenido.indexOf("\r\n", inicio + 6));
	                if (newcontenido.contains("Placas")) {
	                    if (newcontenido.contains("REPUVE")) {
	                        newcontenido = newcontenido.split("REPUVE")[0];
	                    }
	                    newcontenido = newcontenido.split("Placas")[0].replace("###", "").trim();
	                    modelo.setMotor(newcontenido);
	                }
	            }

	            //placas
	            inicio = contenido.indexOf("Placas:");
	            if (inicio > -1) {
	                newcontenido = contenido.substring(inicio + 7, contenido.indexOf("\r\n", inicio + 7)).replace("###", "").replace("-", "").replace(" ", "").trim();
	                modelo.setPlacas(newcontenido);
	            }

	            //coberturas
	            inicio = contenido.indexOf("PRIMAS");
	            if (inicio > 0) {

	            } else {
	                inicio = contenido.indexOf("PRIMA");
	            }
	            fin = contenido.indexOf("MONEDA");

	            if (inicio > -1 && fin > inicio) {
	                newcontenido = contenido.substring(inicio, fin).replace("@@@", "").trim();
	                if (newcontenido.contains("La Unidad de Medida")) {
	                    newcontenido = newcontenido.split("La Unidad de Medida")[0].trim();
	                }
	                if (newcontenido.contains("Servicios de Asistencia Vial")) {
	                    newcontenido = newcontenido.split("Servicios de Asistencia Vial")[0].trim();
	                }

	                for (String x : newcontenido.split("\r\n")) {
	                    EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();
	                    switch (x.split("###").length) {
	                        case 4:
	                            cobertura.setNombre(x.split("###")[0].trim());
	                            cobertura.setSa(fn.formatoTexto(x.split("###")[1].trim()));
	                            cobertura.setDeducible(x.split("###")[2].trim());
	                            jsonArrayCob.put(new JSONObject(cobertura));
	                            break;
	                        case 3:
	                            cobertura.setNombre(x.split("###")[0].trim());
	                            cobertura.setSa(fn.formatoTexto(x.split("###")[1].trim()));
	                            jsonArrayCob.put(new JSONObject(cobertura));
	                            break;
	                    }
	                }
	            }
	            if (jsonArrayCob.length() > 0) {
	                for (int i = 0; i < jsonArrayCob.length(); i++) {

	                    JSONObject datos = new JSONObject();
	                    datos = jsonArrayCob.getJSONObject(i);
	                    if (datos.has("idx")) {
	                        datos.put("idx", i);
	                    }

	                }
	            }
	            modelo.setCoberturas(jsonArrayCob);
	            //CALCULO RESTO DE RECIBOS
	            switch (modelo.getForma_pago()) {
	                case 1:
	                    if (jsonArrayRec.length() == 0) {
	                        EstructuraRecibosModel recibo = new EstructuraRecibosModel();
	                        recibo.setRecibo_id("");
	                        recibo.setSerie("1/1");
	                        recibo.setVigencia_de(modelo.getVigencia_de());
	                        recibo.setVigencia_a(modelo.getVigencia_a());
	                        if (recibo.getVigencia_de().length() > 0) {
	                            recibo.setVencimiento(fn.dateAdd(recibo.getVigencia_de(), 30, 1));
	                        }
	                        recibo.setPrima_neta(new BigDecimal(modelo.getPrima_neta()).setScale(2, BigDecimal.ROUND_HALF_EVEN).floatValue());
	                        recibo.setDerecho(new BigDecimal(modelo.getDerecho()).setScale(2, BigDecimal.ROUND_HALF_EVEN).floatValue());
	                        recibo.setRecargo(new BigDecimal(modelo.getRecargo()).setScale(2, BigDecimal.ROUND_HALF_EVEN).floatValue());
	                        recibo.setIva(new BigDecimal(modelo.getIva()).setScale(2, BigDecimal.ROUND_HALF_EVEN).floatValue());
	                        recibo.setPrima_total(new BigDecimal(modelo.getPrima_total()).setScale(2, BigDecimal.ROUND_HALF_EVEN).floatValue());
	                        recibo.setAjuste_uno(new BigDecimal(modelo.getAjuste_uno()).setScale(2, BigDecimal.ROUND_HALF_EVEN).floatValue());
	                        recibo.setAjuste_dos(new BigDecimal(modelo.getAjuste_dos()).setScale(2, BigDecimal.ROUND_HALF_EVEN).floatValue());
	                        recibo.setCargo_extra(new BigDecimal(modelo.getCargo_extra()).setScale(2, BigDecimal.ROUND_HALF_EVEN).floatValue());
	                        jsonArrayRec.put(new JSONObject(recibo));
	                    }
	                    break;
	                case 2:
	                    if (jsonArrayRec.length() == 1) {
	                        EstructuraRecibosModel recibo = new EstructuraRecibosModel();
	                        recibo.setRecibo_id("");
	                        recibo.setSerie("2/2");
	                        recibo.setVigencia_de(jsonArrayRec.getJSONObject(0).getString("vigencia_a"));
	                        recibo.setVigencia_a(modelo.getVigencia_a());
	                        recibo.setVencimiento("");
	                        recibo.setPrima_neta(new BigDecimal(restoPrimaNeta).setScale(2, BigDecimal.ROUND_HALF_EVEN).floatValue());
	                        recibo.setPrima_total(new BigDecimal(restoPrimaTotal).setScale(2, BigDecimal.ROUND_HALF_EVEN).floatValue());
	                        recibo.setRecargo(new BigDecimal(restoRecargo).setScale(2, BigDecimal.ROUND_HALF_EVEN).floatValue());
	                        recibo.setDerecho(new BigDecimal(restoDerecho).setScale(2, BigDecimal.ROUND_HALF_EVEN).floatValue());
	                        recibo.setIva(new BigDecimal(restoIva).setScale(2, BigDecimal.ROUND_HALF_EVEN).floatValue());
	                        recibo.setAjuste_uno(new BigDecimal(restoAjusteUno).setScale(2, BigDecimal.ROUND_HALF_EVEN).floatValue());
	                        recibo.setAjuste_dos(new BigDecimal(restoAjusteDos).setScale(2, BigDecimal.ROUND_HALF_EVEN).floatValue());
	                        recibo.setCargo_extra(new BigDecimal(restoCargoExtra).setScale(2, BigDecimal.ROUND_HALF_EVEN).floatValue());
	                        jsonArrayRec.put(new JSONObject(recibo));
	                    }
	                    break;
	                case 3:
	                case 4:
	                    if (jsonArrayRec.length() >= 1) {
	                        int restoRec = (fn.getTotalRec(modelo.getForma_pago()) - jsonArrayRec.length());
	                        int totalRec = fn.getTotalRec(modelo.getForma_pago());
	                        int meses = fn.monthAdd(modelo.getForma_pago());//MESES A AGREGAR POR RECIBO
	                        for (int i = jsonArrayRec.length(); i <= restoRec; i++) {
	                            EstructuraRecibosModel recibo = new EstructuraRecibosModel();
	                            recibo.setRecibo_id("");
	                            recibo.setSerie(i + 1 + "/" + totalRec);
	                            recibo.setVigencia_de(jsonArrayRec.getJSONObject(i - 1).getString("vigencia_a"));
	                            if (jsonArrayRec.getJSONObject(i - 1).getString("vigencia_a").length() == 10) {
	                                recibo.setVigencia_a(fn.dateAdd(jsonArrayRec.getJSONObject(i - 1).getString("vigencia_a"), meses, 2));
	                            }
	                            recibo.setVencimiento("");
	                            recibo.setPrima_neta(new BigDecimal(restoPrimaNeta / (restoRec)).setScale(2, BigDecimal.ROUND_HALF_EVEN).floatValue());
	                            recibo.setPrima_total(new BigDecimal(restoPrimaTotal / (restoRec)).setScale(2, BigDecimal.ROUND_HALF_EVEN).floatValue());
	                            recibo.setRecargo(new BigDecimal((restoRecargo / (restoRec))).setScale(2, BigDecimal.ROUND_HALF_EVEN).floatValue());
	                            recibo.setDerecho(new BigDecimal((restoDerecho / (restoRec))).setScale(2, BigDecimal.ROUND_HALF_EVEN).floatValue());
	                            recibo.setIva(new BigDecimal((restoIva / (restoRec))).setScale(2, BigDecimal.ROUND_HALF_EVEN).floatValue());
	                            recibo.setAjuste_uno(new BigDecimal(restoAjusteUno / (restoRec)).setScale(2, BigDecimal.ROUND_HALF_EVEN).floatValue());
	                            recibo.setAjuste_dos(new BigDecimal(restoAjusteDos / (restoRec)).setScale(2, BigDecimal.ROUND_HALF_EVEN).floatValue());
	                            recibo.setCargo_extra(new BigDecimal(restoCargoExtra / (restoRec)).setScale(2, BigDecimal.ROUND_HALF_EVEN).floatValue());
	                            jsonArrayRec.put(new JSONObject(recibo));
	                        }
	                    }
	                    break;
	                case 5:
	                case 6://QUINCENAL, SEMANAL  NINGUN PDF DE FORMA DE PAGO SE QUEDA PENDIENTE ESTE CASO
	                    if (jsonArrayRec.length() >= 1) {
	                        int restoRec = (fn.getTotalRec(modelo.getForma_pago()) - jsonArrayRec.length());
	                        int totalRec = fn.getTotalRec(modelo.getForma_pago());
	                        for (int i = jsonArrayRec.length(); i <= restoRec; i++) {
	                            EstructuraRecibosModel recibo = new EstructuraRecibosModel();
	                            recibo.setSerie(i + 1 + "/" + totalRec);
	                            recibo.setRecibo_id("");
	                            recibo.setVigencia_de(jsonArrayRec.getJSONObject(i - 1).getString("vigencia_a"));
	                            recibo.setVigencia_a("");
	                            recibo.setVencimiento("");
	                            recibo.setPrima_neta(new BigDecimal(restoPrimaNeta / (restoRec)).setScale(2, BigDecimal.ROUND_HALF_EVEN).floatValue());
	                            recibo.setPrima_total(new BigDecimal(restoPrimaTotal / (restoRec)).setScale(2, BigDecimal.ROUND_HALF_EVEN).floatValue());
	                            recibo.setRecargo(new BigDecimal(restoRecargo / (restoRec)).setScale(2, BigDecimal.ROUND_HALF_EVEN).floatValue());
	                            recibo.setDerecho(new BigDecimal((restoDerecho / (restoRec))).setScale(2, BigDecimal.ROUND_HALF_EVEN).floatValue());
	                            recibo.setIva(new BigDecimal(restoIva / (restoRec)).setScale(2, BigDecimal.ROUND_HALF_EVEN).floatValue());
	                            recibo.setAjuste_uno(new BigDecimal(restoAjusteUno / (restoRec)).setScale(2, BigDecimal.ROUND_HALF_EVEN).floatValue());
	                            recibo.setAjuste_dos(new BigDecimal((restoAjusteDos / (restoRec))).setScale(2, BigDecimal.ROUND_HALF_EVEN).floatValue());
	                            recibo.setCargo_extra(new BigDecimal(restoCargoExtra / (restoRec)).setScale(2, BigDecimal.ROUND_HALF_EVEN).floatValue());
	                            jsonArrayRec.put(new JSONObject(recibo));
	                        }
	                    }
	                    break;
	            }
	            modelo.setRecibos(jsonArrayRec);
	            return jsonObject = new JSONObject(modelo);
	        } catch (Exception ex) {
	            jsonObject = new JSONObject(modelo);
	            jsonObject.put("error", "DatosQualitasAutos.procesar: " + ex.getMessage() + " | " + ex.getCause());
	            return jsonObject;
	        }
	    }
	    

}
