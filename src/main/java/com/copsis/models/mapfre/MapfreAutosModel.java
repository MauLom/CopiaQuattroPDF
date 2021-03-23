//package com.copsis.models.mapfre;
//
//import com.copsis.models.DataToolsModel;
//import com.copsis.models.EstructuraJsonModel;
//
//public class MapfreAutosModel {
//	// Clases
//	private DataToolsModel fn = new DataToolsModel();
//	private EstructuraJsonModel modelo = new EstructuraJsonModel();
//	// Variables
//	private String contenido = "", newcontenido = "",txtAux="";
//	private int inicio = 0, fin = 0, donde = 0, longitud_texto = 0,index;
//
//	// constructor
//	public MapfreAutosModel(String contenido) {
//		this.contenido = contenido;
//	}
//
//	public EstructuraJsonModel procesar() {
//
//		contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());
//
//		try {
//			 //tipo
//            modelo.setTipo(1);
//            //cia
//            modelo.setCia(22);
//
//            // poliza
//            inicio = contenido.indexOf("PÓLIZA/ENDOSO###") + 16;
//            modelo.setPoliza(contenido.substring(inicio, inicio + 17).split("/")[0].trim());
//
//            // endoso
//            modelo.setEndoso(contenido.substring(inicio, inicio + 17).split("/")[1].trim());
//
////            cte_nombre
//            inicio = contenido.indexOf("CONTRATANTE:");
//            fin = contenido.indexOf("DOMICILIO:");
//
//            if (inicio > -1) {
//            	newcontenido = fn.gatos(contenido.substring(inicio + 12, fin).replace("@@@", "").trim());
//                txtAux = "";
//                for (String x : newcontenido.split("\r\n")) {
//                    if (index >= 0 && index <= 2) {
//                        x = fn.gatos(x);
//                        fin = x.indexOf("CONDUCTOR");
//                        if (fin > -1) {
//                            txtAux += fn.gatos(x.substring(0, fin)) + " ";
//                        } else if (x.split("###").length == 2) {
//                            txtAux += x.split("###")[0] + " ";
//                        }
//                    }
//                    index++;
//                }
//                modelo.setCteNombre(txtAux.trim());
//            }
//            if (modelo.getCteNombre().length() == 0) {//PARA OTRA VERSION
//                inicio = contenido.indexOf("NOMBRE:");
//                if (inicio > -1) {
//                	newcontenido = fn.gatos(contenido.substring(inicio + 7, inicio + 150).split("\r\n")[0]);
//                    modelo.setCteNombre(newcontenido);
//                }
//            }
//
//            // cte_direccion
//            inicio = contenido.indexOf("DOMICILIO:");
//            fin = contenido.indexOf("FOLIO");
//            if (inicio > -1 && fin > inicio) {
//                txtAux = "";
//                newcontenido = contenido.substring(inicio + 10, fin);
//                index = 0;
//                for (String x : newcontenido.split("\r\n")) {
//                    if (index >= 0 && index <= 2) {
//                        fin = x.indexOf("DOMICILIO");
//                        if (fin > -1) {
//                            txtAux += fn.gatos(x.substring(0, fin)) + " ";
//                        } else if (x.split("###").length == 2) {
//                            txtAux += x.split("###")[0].trim() + " ";
//                        }
//                    }
//                    index++;
//                }
//                modelo.setCteDireccion(txtAux.trim());
//            }
//            if (modelo.getCteDireccion().length() == 0) {//PARA OTRA VERSION
//                inicio = contenido.indexOf("DIRECCIÓN:");
//                if (inicio > -1) {
//                    txt = fn.gatos(contenido.substring(inicio + 10, inicio + 150).split("\r\n")[0]);
//                    modelo.setCte_direccion(fn.formatoTexto(txt));
//                }
//            }
//
//            // rfc
//            int iniciorfcveri = contenido.indexOf("R.F.C.:###");
//            if (iniciorfcveri > 0) {
//                inicio = contenido.indexOf("R.F.C.:###") + 10;
//            } else {
//                inicio = contenido.indexOf("R.F.C.:") + 8;
//            }
//            modelo.setRfc(contenido.substring(inicio, inicio + 13).trim().replace("-", "").replace("###", ""));
//
//            // inciso
//            modelo.setInciso(1);
//
//            // prima neta
//            inicio = contenido.indexOf("###PRIMA###NETA:###") + 19;
//            contenidoAux = contenido.substring(inicio, inicio + 20).split("\r\n")[0];
//            modelo.setPrima_neta(new Float(fn.cleanString(contenidoAux.replace("$", "").trim())));
//
//            // prima_total
//            inicio = contenido.indexOf("PRIMA###TOTAL:");
//            if (inicio > -1) {
//                contenidoAux = contenido.substring(inicio + 14, inicio + 80).split("\r\n")[0].replace("###", "");
//                txt = fn.cleanString(contenidoAux);
//                if (fn.isNumeric(txt)) {
//                    modelo.setPrima_total(new Float(txt));
//                }
//            }
//
//            // iva
//            inicio = contenido.lastIndexOf("I.V.A.:");
//            if (inicio > -1) {
//                txt = fn.gatos(contenido.substring(inicio + 7, inicio + 150).split("\r\n")[0]);
//                txt = fn.cleanString(txt);
//                if (fn.isNumeric(txt)) {
//                    modelo.setIva(new Float(txt));
//                }
//            }
//
//            // agente
//            int inicioagentd = contenido.indexOf("@@@AGENTE:  ");
//            if (inicioagentd > 0) {
//                inicio = contenido.indexOf("@@@AGENTE:  ") + 12;
//                contenidoAux = contenido.substring(inicio, inicio + 90).split("\r\n")[0];
//            } else {
//                inicio = contenido.indexOf("@@@AGENTE:") + 11;
//                contenidoAux = contenido.substring(inicio, inicio + 90).split("\r\n")[0];
//            }
//            modelo.setAgente(contenidoAux.trim());
//
//            // cve_agente
//            int inicioagente = contenido.indexOf("CLAVE DE AGENTE: ");
//            if (inicioagente > 0) {
//                inicio = contenido.indexOf("CLAVE DE AGENTE: ") + 17;
//            } else {
//                inicio = contenido.indexOf("@@@CLAVE DE AGENTE:") + 17;
//            }
//            contenidoAux = contenido.substring(inicio, inicio + 10).split("\r\n")[0];
//            modelo.setCve_agente(contenidoAux.replace("E:###", "").trim());
//
//            // vigencia_de
//            inicio = contenido.indexOf("@@@VIGENCIA###DESDE###LAS###12:00###HRS.###DEL:###") + 50;
//            contenidoAux = contenido.substring(inicio, inicio + 10);
//            modelo.setVigencia_de(fn.formatDate(contenidoAux));
//
//            // vigencia_a
//            inicio = contenido.indexOf("@@@HASTA###LAS###12:00###HRS.###DEL:###") + 39;
//            contenidoAux = contenido.substring(inicio, inicio + 10);
//            modelo.setVigencia_a(fn.formatDate(contenidoAux));
//
//
//            // cp
//            inicio = contenido.indexOf("INFORMACIÓN###GENERAL");
//            fin = contenido.indexOf("CONCEPTOS###ECONÓMICOS");
//   
//            if (inicio > 0 & fin > 0 & inicio < fin) {
//                newcontenido = contenido.substring(inicio, fin);
//                for (String x : newcontenido.split("\n")) {
//                    if (x.contains("CÓDIGO POSTAL")) {
//                        modelo.setCp(x.split("CÓDIGO POSTAL")[1].split("###")[0].replace(":", "").trim());
//                    }
//                    if (x.contains("C.P.")) {
//                        modelo.setCp(x.split("C.P.")[1].split("###")[1].replace(":", "").replace("\r", "").trim());
//                    }
//
//                }
//            }
//
//
//
//            // recargo
//            inicio = contenido.indexOf("RECARGO###PAGO###FRACCIONADO:");
//            if (inicio > -1) {
//                contenidoAux = fn.gatos(contenido.substring(inicio + 29, inicio + 150).split("\r\n")[0].split("PRIMA")[0]);
//                contenidoAux = fn.cleanString(contenidoAux);
//                if (fn.isNumeric(contenidoAux)) {
//                    modelo.setRecargo(new Float(contenidoAux));
//                }
//            }
//
//            // derecho
//            inicio = contenido.indexOf("DE###EXPEDICIÓN:");
//            if (inicio > -1) {
//                contenidoAux = fn.gatos(contenido.substring(inicio + 16, inicio + 150).split("\r\n")[0]);
//                contenidoAux = fn.cleanString(contenidoAux);
//                if (fn.isNumeric(contenidoAux)) {
//                    modelo.setDerecho(new Float(contenidoAux));
//                }
//            }
//
//            //descripcion
//            inicio = contenido.indexOf("DESCRIPCIÓN:");
//            fin = contenido.indexOf("MARCA");
//            if (inicio > -1 && fin > inicio) {
//                contenidoAux = contenido.substring(inicio + 12, fin).replace("@@@a", "").replace("@@@", "").trim();
//                txt = "";
//                for (String x : contenidoAux.split("\r\n")) {
//                    if (x.trim().length() > 0 && x.trim().equals("a") == false) {
//                        if (x.contains("REMOLQUE")) {
//                            txt += fn.gatos(x.split("REMOLQUE")[0]) + " ";
//                        } else {
//                            txt += fn.gatos(x);
//                        }
//                    }
//                }
//                modelo.setDescripcion(txt.trim());
//            }
//
//            inicio = contenido.indexOf("MARCA:");
//            if (inicio > -1) {
//                contenidoAux = fn.gatos(contenido.substring(inicio + 6, inicio + 150).split("\r\n")[0]);
//                modelo.setMarca(contenidoAux);
//            }
//
//            // clave
//            inicio = contenido.indexOf("CLAVE###MAPFRE:");
//            if (inicio > -1) {
//                contenidoAux = fn.gatos(contenido.substring(inicio + 15, inicio + 150).split("\r\n")[0]);
//                modelo.setClave(contenidoAux.trim());
//            }
//
//            // modelo
//            inicio = contenido.indexOf("FABRICACIÓN:");
//            if (inicio > -1) {
//                contenidoAux = fn.gatos(contenido.substring(inicio + 12, inicio + 150).split("\r\n")[0]);
//                if (fn.isNumeric(contenidoAux)) {
//                    modelo.setModelo(new Integer(contenidoAux));
//                }
//            }
//
//            // serie
//            inicio = contenido.indexOf("SERIE:");
//            if (inicio > -1) {
//                txt = fn.gatos(contenido.substring(inicio + 6, inicio + 150).split("\r\n")[0]);
//                if (txt.contains("PLACAS")) {
//                    txt = fn.gatos(txt.split("###")[0]).trim();
//                }
//                if (txt.split("###").length == 1) {
//                    modelo.setSerie(txt);
//                }
//            }
//
//            // motor
//            int inimoto = contenido.indexOf("NÚMERO###DE###MOTOR:###");
//            if (inimoto > 0) {
//                inicio = contenido.indexOf("NÚMERO###DE###MOTOR:###") + 23;
//                fin = contenido.indexOf("###AÑO###DE###FABRICACIÓN:###");
//                contenidoAux = contenido.substring(inicio, fin);
//                modelo.setMotor(contenidoAux.trim());
//            }
//
//            //placas
//            inicio = contenido.indexOf("PLACAS");
//            if (inicio > -1) {
//                txt = fn.gatos(contenido.substring(inicio + 6, inicio + 180).replace(":", "").split("\r\n")[0]);
//                modelo.setPlacas(txt);
//            }
//
//            // forma_pago
//            inicio = contenido.indexOf("DE###PAGO:###");
//            fin = contenido.indexOf("###PRIMA###NETA:");
//            if (inicio > -1 && fin > inicio) {
//                modelo.setForma_pago(fn.fPago(contenido.substring(inicio + 13, fin)));
//            }
//
//            //conductor
//            inicio = contenido.indexOf("CONDUCTOR###HABITUAL:");
//            fin = contenido.indexOf("DOMICILIO:");
//            if (inicio > -1) {
//                txt = fn.gatos(contenido.substring(inicio + 21, fin).replace("@@@", "").trim());
//                txtAux = "";
//                index = 0;
//                for (String x : txt.split("\r\n")) {
//                    if (index >= 0 && index <= 2) {
//                        x = fn.gatos(x);
//                        if (x.split("###").length == 2) {
//                            txtAux += x.split("###")[1].trim() + " ";
//                        } else if (x.trim().equals("a") == false) {
//                            txtAux += x.trim() + " ";
//                        }
//                    }
//                    index++;
//                }
//                modelo.setConductor(txtAux.trim());
//            }
//            if (modelo.getConductor().length() == 0) {//PARA OTRA VERSION
//                inicio = contenido.indexOf("CONDUCTOR###HABITUAL");
//                if (inicio > -1) {
//                    txtAux = fn.gatos(contenido.substring(inicio + 20, contenido.length()));
//                    inicio = txtAux.indexOf("NOMBRE:");
//                    if (inicio > -1) {
//                        txt = fn.gatos(txtAux.substring(inicio + 7, inicio + 150).split("\r\n")[0]);
//                        modelo.setConductor(txt);
//                    }
//                }
//            }
//
//            // moneda
//            inicio = contenido.indexOf("MONEDA:");
//            fin = contenido.indexOf("GASTOS");
//            if (inicio > -1 && fin > inicio) {
//                txt = fn.gatos(contenido.substring(inicio + 7, fin).replace("$", "")).trim();
//                modelo.setMoneda(fn.moneda(txt));
//            }
//
//            //id_cliente
//            inicio = contenido.indexOf("CLIENTE###MAPFRE:");
//            if (inicio > -1) {
//                txt = fn.gatos(contenido.substring(inicio + 17, inicio + 150).split("\r\n")[0]);
//                modelo.setId_cliente(txt);
//            }
//
//            //fecha_emision
//            modelo.setFecha_emision(fn.formatDate(contenido.split("FECHA DE EMISIÓN")[1].split("\r\n")[0].replace("###", "").trim()));
//
//            // COBERTURAS
//            inicio = contenido.indexOf("###DEDUCIBLE") + 12;
//            fin = contenido.indexOf("@@@INFORMACIÓN###ADICIONAL");
//            String coberturas = contenido.substring(inicio, fin).replace("@@@", "").replace("\r", "");
//            for (String dato : coberturas.split("\n")) {
//                if (dato.split("###").length == 3) {
//                    EstructuraCoberturas cobertura = new EstructuraCoberturas();
//                    cobertura.setNombre(dato.split("###")[0]);
//                    cobertura.setDeducible(dato.split("###")[2]);
//                    cobertura.setSa(dato.split("###")[1]);
//                    jsonArrayCob.put(new JSONObject(cobertura));
//                }
//                if (dato.split("###").length == 4) {
//                    EstructuraCoberturas cobertura = new EstructuraCoberturas();
//                    cobertura.setNombre(dato.split("###")[0]);
//                    cobertura.setDeducible(dato.split("###")[3]);
//                    cobertura.setSa(dato.split("###")[2]);
//                    jsonArrayCob.put(new JSONObject(cobertura));
//                }
//            }
//            if (jsonArrayCob.length() > 0) {
//                for (int i = 0; i < jsonArrayCob.length(); i++) {
//
//                    JSONObject datos = new JSONObject();
//                    datos = jsonArrayCob.getJSONObject(i);
//                    if (datos.has("idx")) {
//                        datos.put("idx", i);
//                    }
//
//                }
//            }
//            modelo.setCoberturas(jsonArrayCob);
//
//            //**************************************RECIBOS
//            switch (modelo.getForma_pago()) {
//                case 1:
//                    if (jsonArrayRec.length() == 0) {
//                        EstructuraRecibos recibo = new EstructuraRecibos();
//                        recibo.setRecibo_id("");
//                        recibo.setSerie("1/1");
//                        recibo.setVigencia_de(modelo.getVigencia_de());
//                        recibo.setVigencia_a(modelo.getVigencia_a());
//                        if (recibo.getVigencia_de().length() > 0) {
//                            recibo.setVencimiento(fn.dateAdd(recibo.getVigencia_de(), 30, 1));
//                        }
//                        recibo.setPrima_neta(new BigDecimal(modelo.getPrima_neta()).setScale(2, BigDecimal.ROUND_HALF_EVEN).floatValue());
//                        recibo.setDerecho(new BigDecimal(modelo.getDerecho()).setScale(2, BigDecimal.ROUND_HALF_EVEN).floatValue());
//                        recibo.setRecargo(new BigDecimal(modelo.getRecargo()).setScale(2, BigDecimal.ROUND_HALF_EVEN).floatValue());
//                        recibo.setIva(new BigDecimal(modelo.getIva()).setScale(2, BigDecimal.ROUND_HALF_EVEN).floatValue());
//                        recibo.setPrima_total(new BigDecimal(modelo.getPrima_total()).setScale(2, BigDecimal.ROUND_HALF_EVEN).floatValue());
//                        recibo.setAjuste_uno(new BigDecimal(modelo.getAjuste_uno()).setScale(2, BigDecimal.ROUND_HALF_EVEN).floatValue());
//                        recibo.setAjuste_dos(new BigDecimal(modelo.getAjuste_dos()).setScale(2, BigDecimal.ROUND_HALF_EVEN).floatValue());
//                        recibo.setCargo_extra(new BigDecimal(modelo.getCargo_extra()).setScale(2, BigDecimal.ROUND_HALF_EVEN).floatValue());
//                        jsonArrayRec.put(new JSONObject(recibo));
//                    }
//                    break;
//            }
//            modelo.setRecibos(jsonArrayRec);
//            return jsonObject = new JSONObject(modelo);
//			
//			
//			
//			
//			return modelo;
//		} catch (Exception ex) {
//			modelo.setError(
//					MapfreAutosModel.this.getClass().getTypeName() + " | " + ex.getMessage() + " | " + ex.getCause());
//			return modelo;
//		}
//	}
//
//}
