package com.copsis.models.mapfre;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraCoberturasModel;
import com.copsis.models.EstructuraJsonModel;
import com.copsis.models.EstructuraRecibosModel;

public class MapfreAutosModel {
	// Clases
	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
	// Variables
	private String contenido = "", newcontenido = "",txtAux="",txt="";
	private int inicio = 0, fin = 0,index;
	// constructor
	public MapfreAutosModel(String contenido) {
		this.contenido = contenido;
	}

	public EstructuraJsonModel procesar() {
		contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());
		try {
			 //tipo
            modelo.setTipo(1);
            //cia
            modelo.setCia(22);

            //Poliza-Endoso
            inicio = contenido.indexOf("PÓLIZA-ENDOSO");
            fin = contenido.indexOf("FECHA DE EMISIÓN");
            
            if( inicio > 0 && fin > 0  && inicio < fin) {
            	 newcontenido = contenido.substring(inicio,fin);
            	 for (int i = 0; i < newcontenido.split("\n").length; i++) {
					if(newcontenido.split("\n")[i].contains("PÓLIZA-ENDOSO")) {
						modelo.setEndoso(newcontenido.split("ENDOSO")[1].split("-")[1].replace("\r\n", "").replace("@@@", "").trim());
						modelo.setPoliza(newcontenido.split("PÓLIZA-ENDOSO")[1].split("-")[0].replace("###", "").trim());
					}
					
				}
            }
            
            


//            cte_nombre
            inicio = contenido.indexOf("CONTRATANTE:");
            fin = contenido.indexOf("DOMICILIO:");

            if (inicio > -1) {
            	newcontenido = fn.gatos(contenido.substring(inicio + 12, fin).replace("@@@", "").trim());
                txtAux = "";
                for (String x : newcontenido.split("\r\n")) {
                    if (index >= 0 && index <= 2) {
                        x = fn.gatos(x);
                        fin = x.indexOf("CONDUCTOR");
                        if (fin > -1) {
                            txtAux += fn.gatos(x.substring(0, fin)) + " ";
                        } else if (x.split("###").length == 2) {
                            txtAux += x.split("###")[0] + " ";
                        }
                    }
                    index++;
                }
                if(txtAux.contains(",")) {
                	 modelo.setCteNombre((txtAux.trim().split(",")[1] +" "+ txtAux.trim().split(",")[0]).trim());
                }else {
                	 modelo.setCteNombre(txtAux.trim());
                }
                
               
            }
            if (modelo.getCteNombre().length() == 0) {//PARA OTRA VERSION
                inicio = contenido.indexOf("NOMBRE:");
                if (inicio > -1) {
                	newcontenido = fn.gatos(contenido.substring(inicio + 7, inicio + 150).split("\r\n")[0]);
                    modelo.setCteNombre(newcontenido);
                }
            }

            // cte_direccion
            inicio = contenido.indexOf("DOMICILIO:");
            fin = contenido.indexOf("FOLIO");
            if (inicio > -1 && fin > inicio) {
                txtAux = "";
                newcontenido = contenido.substring(inicio + 10, fin);
                index = 0;
                for (String x : newcontenido.split("\r\n")) {
                    if (index >= 0 && index <= 2) {
                        fin = x.indexOf("DOMICILIO");
                        if (fin > -1) {
                            txtAux += fn.gatos(x.substring(0, fin)) + " ";
                        } else if (x.split("###").length == 2) {
                            txtAux += x.split("###")[0].trim() + " ";
                        }
                    }
                    index++;
                }
                modelo.setCteDireccion(txtAux.trim());
            }
            if (modelo.getCteDireccion().length() == 0) {//PARA OTRA VERSION
                inicio = contenido.indexOf("DIRECCIÓN:");
                if (inicio > -1) {
                    newcontenido = fn.gatos(contenido.substring(inicio + 10, inicio + 150).split("\r\n")[0]);
                    modelo.setCteDireccion(newcontenido);
                }
            }

            // rfc
            int iniciorfcveri = contenido.indexOf("R.F.C:###");
            if (iniciorfcveri > 0) {
                inicio = contenido.indexOf("R.F.C:###") + 10;
            } else {
                inicio = contenido.indexOf("R.F.C:") + 8;
            }
            modelo.setRfc(contenido.substring(inicio, inicio + 13).trim().replace("-", "").replace("###", ""));

            // inciso
            modelo.setInciso(1);

            // prima neta
            inicio = contenido.indexOf("###PRIMA###NETA:###") + 19;
            newcontenido = contenido.substring(inicio, inicio + 20).split("\r\n")[0];
            modelo.setPrimaneta(fn.castFloat(newcontenido));

            // prima_total
            inicio = contenido.indexOf("PRIMA###TOTAL:");
            if (inicio > -1) {
            	newcontenido = contenido.substring(inicio + 14, inicio + 80).split("\r\n")[0].replace("###", "").replace(",", "");

                if (fn.isNumeric(newcontenido)) {
                    modelo.setPrimaTotal(fn.castFloat(newcontenido));
                }
            }

            // iva
            inicio = contenido.lastIndexOf("I.V.A:");
            if (inicio > -1) {
            	newcontenido = fn.gatos(contenido.substring(inicio + 7, inicio + 150).split("\r\n")[0].replace("##", "").replace(",", "").trim());
        System.out.println(newcontenido);
                if (fn.isNumeric(newcontenido)) {
                    modelo.setIva(fn.castFloat(newcontenido));
                }
            }

            // agente
            int inicioagentd = contenido.indexOf("@@@AGENTE:  ");
            if (inicioagentd > 0) {
                inicio = contenido.indexOf("@@@AGENTE:  ") + 12;
                newcontenido = contenido.substring(inicio, inicio + 90).split("\r\n")[0];
            } else {
                inicio = contenido.indexOf("@@@AGENTE:") + 11;
                newcontenido = contenido.substring(inicio, inicio + 90).split("\r\n")[0];
            }
            modelo.setAgente(newcontenido.trim());

            // cve_agente
            int inicioagente = contenido.indexOf("CLAVE DE AGENTE: ");
            if (inicioagente > 0) {
                inicio = contenido.indexOf("CLAVE DE AGENTE: ") + 17;
            } else {
                inicio = contenido.indexOf("@@@CLAVE DE AGENTE:") + 17;
            }
            newcontenido = contenido.substring(inicio, inicio + 10).split("\r\n")[0];
            modelo.setCveAgente(newcontenido.replace("E:###", "").trim());

            // vigencia_de
            inicio = contenido.indexOf("@@@VIGENCIA###DESDE###LAS###12:00###HRS.###DEL:###") + 50;
            newcontenido = contenido.substring(inicio, inicio + 10);
            modelo.setVigenciaDe(fn.formatDate(newcontenido,"dd-MM-yy"));

            // vigencia_a
            inicio = contenido.indexOf("@@@HASTA###LAS###12:00###HRS.###DEL:###") + 39;
            newcontenido = contenido.substring(inicio, inicio + 10);
            modelo.setVigenciaA(fn.formatDate(newcontenido,"dd-MM-yy"));


            // cp
            inicio = contenido.indexOf("INFORMACIÓN###GENERAL");
            fin = contenido.indexOf("CONCEPTOS###ECONÓMICOS");
   
            if (inicio > 0 & fin > 0 & inicio < fin) {
                newcontenido = contenido.substring(inicio, fin);
                for (String x : newcontenido.split("\n")) {
                    if (x.contains("CÓDIGO POSTAL")) {
                        modelo.setCp(x.split("CÓDIGO POSTAL")[1].split("###")[0].replace(":", "").trim());
                    }
                    if (x.contains("C.P")) {
                        modelo.setCp(x.split("C.P")[1].split("###")[1].replace(":", "").replace("\r", "").trim());
                    }

                }
            }



            // recargo
            inicio = contenido.indexOf("RECARGO###PAGO###FRACCIONADO:");
           
            if (inicio > -1) {
            	newcontenido = fn.gatos(contenido.substring(inicio + 29, inicio + 150).split("\r\n")[0].split("PRIMA")[0]);
                if (fn.isNumeric(newcontenido)) {
                    modelo.setRecargo(fn.castFloat(newcontenido));
                }
            }

            // derecho
            inicio = contenido.indexOf("DE###EXPEDICIÓN:");
            if (inicio > -1) {
            	newcontenido = fn.gatos(contenido.substring(inicio + 16, inicio + 150).split("\r\n")[0]);
                if (fn.isNumeric(newcontenido)) {
                    modelo.setDerecho(fn.castFloat(newcontenido));
                }
            }

            //descripcion
            inicio = contenido.indexOf("DESCRIPCIÓN:");
            fin = contenido.indexOf("MARCA");
            if (inicio > -1 && fin > inicio) {
            	newcontenido = contenido.substring(inicio + 12, fin).replace("@@@a", "").replace("@@@", "").trim();
                txt = "";
                for (String x : newcontenido.split("\r\n")) {
                    if (x.trim().length() > 0 && x.trim().equals("a") == false) {
                        if (x.contains("REMOLQUE")) {
                            txt += fn.gatos(x.split("REMOLQUE")[0]) + " ";
                        } else {
                            txt += fn.gatos(x);
                        }
                    }
                }
                modelo.setDescripcion(txt.trim());
            }

            inicio = contenido.indexOf("MARCA:");
            if (inicio > -1) {
            	newcontenido = fn.gatos(contenido.substring(inicio + 6, inicio + 150).split("\r\n")[0]);
                modelo.setMarca(newcontenido);
            }

            // clave
            inicio = contenido.indexOf("CLAVE###MAPFRE:");
            if (inicio > -1) {
            	newcontenido = fn.gatos(contenido.substring(inicio + 15, inicio + 150).split("\r\n")[0]);
                modelo.setClave(newcontenido.trim());
            }

            // modelo
            inicio = contenido.indexOf("FABRICACIÓN:");
            if (inicio > -1) {
            	newcontenido = fn.gatos(contenido.substring(inicio + 12, inicio + 150).split("\r\n")[0]);
                if (fn.isNumeric(newcontenido)) {
                    modelo.setModelo(fn.castInteger(newcontenido));
                }
            }

            // serie
            inicio = contenido.indexOf("SERIE:");
            if (inicio > -1) {
                txt = fn.gatos(contenido.substring(inicio + 6, inicio + 150).split("\r\n")[0]);
                if (txt.contains("PLACAS")) {
                    txt = fn.gatos(txt.split("###")[0]).trim();
                }
                if (txt.split("###").length == 1) {
                    modelo.setSerie(txt);
                }
            }

            // motor
            int inimoto = contenido.indexOf("NÚMERO###DE###MOTOR:###");
            if (inimoto > 0) {
                inicio = contenido.indexOf("NÚMERO###DE###MOTOR:###") ;
                fin = contenido.indexOf("###AÑO###DE###FABRICACIÓN:###");
                if(inicio >  0 &&  fin > 0  &&  inicio  < fin) {                	
                    newcontenido = contenido.substring(inicio+ 23, fin);
                    modelo.setMotor(newcontenido.trim());
                }
                
            
            }

            //placas
            inicio = contenido.indexOf("PLACAS");
            if (inicio > -1) {
                txt = fn.gatos(contenido.substring(inicio + 6, inicio + 180).replace(":", "").split("\r\n")[0]);
                modelo.setPlacas(txt);
            }

            // forma_pago
            inicio = contenido.indexOf("DE###PAGO:###");
            fin = contenido.indexOf("###PRIMA###NETA:");
            if(inicio >  0 &&  fin > 0  &&  inicio  < fin) {                	
                modelo.setFormaPago(fn.formaPago(contenido.substring(inicio + 13, fin)));
            }

            //conductor
            inicio = contenido.indexOf("CONDUCTOR###HABITUAL:");
            fin = contenido.indexOf("DOMICILIO:");
            if (inicio > -1) {
                txt = fn.gatos(contenido.substring(inicio + 21, fin).replace("@@@", "").trim());
                txtAux = "";
                index = 0;
                for (String x : txt.split("\r\n")) {
                    if (index >= 0 && index <= 2) {
                        x = fn.gatos(x);
                        if (x.split("###").length == 2) {
                            txtAux += x.split("###")[1].trim() + " ";
                        } else if (x.trim().equals("a") == false) {
                            txtAux += x.trim() + " ";
                        }
                    }
                    index++;
                }
                modelo.setConductor(txtAux.trim());
            }
            if (modelo.getConductor().length() == 0) {//PARA OTRA VERSION
                inicio = contenido.indexOf("CONDUCTOR###HABITUAL");
                if (inicio > -1) {
                    txtAux = fn.gatos(contenido.substring(inicio + 20, contenido.length()));
                    inicio = txtAux.indexOf("NOMBRE:");
                    if (inicio > -1) {
                        txt = fn.gatos(txtAux.substring(inicio + 7, inicio + 150).split("\r\n")[0]);
                        modelo.setConductor(txt);
                    }
                }
            }

            // moneda
            inicio = contenido.indexOf("MONEDA:");
            fin = contenido.indexOf("GASTOS");
            if(inicio >  0 &&  fin > 0  &&  inicio  < fin) {                
                txt = fn.gatos(contenido.substring(inicio + 7, fin).replace("$", "")).trim();
                modelo.setMoneda(fn.moneda(txt));
            }

            //id_cliente
            inicio = contenido.indexOf("CLIENTE###MAPFRE:");
            if (inicio > -1) {
                txt = fn.gatos(contenido.substring(inicio + 17, inicio + 150).split("\r\n")[0]);
                modelo.setIdCliente(txt);
            }

            //fecha_emision
            modelo.setFechaEmision(fn.formatDate(contenido.split("FECHA DE EMISIÓN")[1].split("\r\n")[0].replace("###", "").trim(),"dd-MM-yy"));

            // COBERTURAS
            
            inicio = contenido.indexOf("###DEDUCIBLE");
            fin = contenido.indexOf("@@@INFORMACIÓN###ADICIONAL");
            List<EstructuraCoberturasModel> coberturas = new ArrayList<>();
            if(inicio >  0 &&  fin > 0  &&  inicio  < fin) { 
            	
            	newcontenido = contenido.substring(inicio+ 12, fin).replace("@@@", "").replace("\r", "");
            	
            	for (String dato : newcontenido.split("\n")) {
            		EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();
                    if (dato.split("###").length == 3) {
                       
                        cobertura.setNombre(dato.split("###")[0]);
                        cobertura.setDeducible(dato.split("###")[2]);
                        cobertura.setSa(dato.split("###")[1]);
                        coberturas.add(cobertura);
                    }
                    if (dato.split("###").length == 4) {
                     
                        cobertura.setNombre(dato.split("###")[0]);
                        cobertura.setDeducible(dato.split("###")[3]);
                        cobertura.setSa(dato.split("###")[2]);
                        coberturas.add(cobertura);
                    }
                }            
                modelo.setCoberturas(coberturas);
            }
            

            //**************************************RECIBOS
            List<EstructuraRecibosModel> recibos = new ArrayList<>();
			EstructuraRecibosModel recibo = new EstructuraRecibosModel();
			switch (modelo.getFormaPago()) {
			case 1:
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
				break;
			}
			modelo.setRecibos(recibos);

			return modelo;
		} catch (Exception ex) {
			modelo.setError(
					MapfreAutosModel.this.getClass().getTypeName() + " | " + ex.getMessage() + " | " + ex.getCause());
			return modelo;
		}
	}

}
