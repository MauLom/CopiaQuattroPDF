//package com.copsis.models.axa;
//
//import java.math.BigDecimal;
//
//import com.copsis.models.DataToolsModel;
//import com.copsis.models.EstructuraJsonModel;
//
//public class AxaVidaModel {
//	
//	// Clases
//	private DataToolsModel fn = new DataToolsModel();
//	private EstructuraJsonModel modelo = new EstructuraJsonModel();
//	// Varaibles
//	private String contenido = "";
//	private String newcontenido = "";
//	private String recibosText = "";
//	private String resultado = "";
//	private int inicio = 0;
//	private int fin = 0;
//	private int donde = 0;
//	private BigDecimal restoPrimaTotal = BigDecimal.ZERO;
//	private BigDecimal restoDerecho = BigDecimal.ZERO;
//	private BigDecimal restoIva = BigDecimal.ZERO;
//	private BigDecimal restoRecargo = BigDecimal.ZERO;
//	private BigDecimal restoPrimaNeta = BigDecimal.ZERO;
//	private BigDecimal restoAjusteUno = BigDecimal.ZERO;
//	private BigDecimal restoAjusteDos = BigDecimal.ZERO;
//	private BigDecimal restoCargoExtra = BigDecimal.ZERO;
//	
//	public AxaVidaModel(String contenido, String recibos) {
//		this.contenido = contenido;
//		this.recibosText = recibos;
//	}
//	
//	public EstructuraJsonModel procesar() {
//
//		contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());
//		try {
//			
//			modelo.setTipo(5);
//			modelo.setCia(20);
//			newcontenido = fn.filtroPorRango(contenido, 5);
//			donde = fn.recorreContenido(newcontenido, "Contratante Póliza");
//			if(donde > 0){
//				for(String dato : newcontenido.split("@@@")[donde].split("\r\n")) {
//					if(dato.contains("Contratante Póliza") && dato.split("###").length == 1) {
//						modelo.setPoliza(dato.split("Póliza")[1].trim());
//					}
//				}
//			}
//			
//			donde = 0;
//			newcontenido = fn.filtroPorRango(contenido, 5);
//			if(fn.recorreContenido(newcontenido, "###RFC:###") > 0) {
//				donde = fn.recorreContenido(newcontenido, "###RFC:###");
//			}else if(fn.recorreContenido(newcontenido, "Contratante Póliza") > 0) {
//				donde = fn.recorreContenido(newcontenido, "Contratante Póliza");
//			}
//			
//			if(donde > 0){
//				for(String dato : newcontenido.split("@@@")[donde].split("\r\n")) {
//					if(dato.contains("###RFC:###") && dato.split("###").length == 3) {
//						modelo.setCteNombre(dato.split("###")[0].split("bre")[1].trim());
//					}
//					if(dato.contains("Nombre") && dato.split("###").length == 2) {
//						inicio = dato.split("###")[0].trim().indexOf("Nombre") +6;
//						fin = dato.split("###")[0].trim().length();
//						modelo.setCteNombre(dato.split("###")[0].substring(inicio, fin).trim());
//					}
//				}
//			}
//			
//			donde = 0;
//			newcontenido = fn.filtroPorRango(contenido, 6);
//			donde = fn.recorreContenido(newcontenido, "Domicilio");
//			if(donde > 0){
//				for(int i = 0; i < newcontenido.split("@@@")[donde].split("\r\n").length; i++) {
//					if(newcontenido.split("@@@")[donde].split("\r\n")[i].contains("Domicilio")) {
//						switch(newcontenido.split("@@@")[donde].split("\r\n")[i].split("###").length) {
//						case 2 :
//							if(newcontenido.split("@@@")[donde].split("\r\n")[i].split("###")[0].contains("Domicilio")){
//								resultado = newcontenido.split("@@@")[donde].split("\r\n")[i].split("###")[0].split("cilio")[1].trim();
//							}
//							if((i+2) < newcontenido.split("@@@")[donde].split("\r\n").length) {
//								if(newcontenido.split("@@@")[donde].split("\r\n")[i+2].split("###").length == 2) {
//								 	resultado += " " + newcontenido.split("@@@")[donde].split("\r\n")[i+2].split("###")[0].trim();
//								}
//							}
//							if((i+1) < newcontenido.split("@@@")[donde].split("\r\n").length) {
//								if(newcontenido.split("@@@")[donde].split("\r\n")[i+1].split("###").length == 2) {
//									resultado += " " +  newcontenido.split("@@@")[donde].split("\r\n")[i+1].split("###")[0].trim();
//								}
//								if(newcontenido.split("@@@")[donde].split("\r\n")[i+1].split("###")[1].contains("Edo.")) {
//									resultado += ", " + newcontenido.split("@@@")[donde].split("\r\n")[i+1].split("###")[1].split("Edo.")[1].trim();
//								}
//							}
//							modelo.setCteDireccion(resultado);
//							break;
//						}
//					}
//					
//					if(newcontenido.split("@@@")[donde].split("\r\n")[i].contains("###C.P.")) {
//						switch(newcontenido.split("@@@")[donde].split("\r\n")[i].split("###").length) {
//						case 2:
//							if(newcontenido.split("@@@")[donde].split("\r\n")[i].split("###")[1].contains("C.P.")){
//								modelo.setCp(newcontenido.split("@@@")[donde].split("\r\n")[i].split("###")[1].split("C.P.")[1].trim());
//							}
//							break;
//						}
//					}
//				}
//			}
//			donde = 0;
//			newcontenido = fn.filtroPorRango(contenido, 6);
//			donde = fn.recorreContenido(newcontenido, "###RFC:###");
//			if(donde > 0){
//				for(String dato : newcontenido.split("@@@")[donde].split("\r\n")) {
//					if(dato.contains("###RFC:###") && dato.split("###").length == 3) {
//						modelo.setRfc(dato.split("###")[2].trim());
//					}
//				}
//			}
//			
//			
//
//			donde = 0;
//			if(fn.recorreContenido(contenido, "Inicio de Vigencia") > 0) {
//				donde = fn.recorreContenido(contenido, "Inicio de Vigencia");	
//			}else if(fn.recorreContenido(contenido, "Prima semestral") > 0) {
//				donde = fn.recorreContenido(contenido, "Prima semestral");
//			}
//			if(donde > 0){
//				for(String dato : contenido.split("@@@")[donde].split("\r\n")) {
//					if(dato.contains("Inicio de Vigencia") && dato.split("###").length == 3) {
//						if(dato.split("###")[0].contains("Vigencia")) {
//							modelo.setVigenciaDe(fn.formatDate_MonthCadena(dato.split("###")[0].split("Vigencia")[1].trim()));
//						}
//					}
//					if(dato.contains("Moneda")) {
//						switch (dato.split("###").length) {
//						case 1:
//							modelo.setMoneda(fn.moneda(dato.split("neda")[1].trim()));
//							break;
//						}
//					}
//					if(dato.contains("Frecuencia de Pago")) {
//						switch (dato.split("###").length) {
//						case 3:
//							if(dato.split("###")[0].contains("Primas")) {
//								modelo.setFormaPago(fn.formaPago(dato.split("###")[0].split("Primas")[1].trim().toUpperCase()));
//							}
//							break;
//						}
//					}
//				}
//			}
//			
//			donde = 0;
//			donde = fn.recorreContenido(contenido, "Prima Anual");
//			if(donde > 0){
//				for(String dato : contenido.split("@@@")[donde].split("\r\n")) {
//					if(dato.contains("Anual") && dato.split("###").length == 1) {
//
//						modelo.setPrimaTotal(fn.castBigDecimal(fn.cleanString(dato.split("Anual")[1].trim())));
//					}
//				}
//			}
//			modelo.setPlan(contenido.split("CARATULA DE POLIZA")[1].split("\n")[2].replace("@@@",""));
//			modelo.setPlazo(Integer.parseInt(fn.cleanString(contenido.split("Anual")[1].split("\n")[1].split("###")[3].replace("Años","").replace("Año", "").trim())));
//		    modelo.setPlazoPago(Integer.parseInt(fn.cleanString(contenido.split("Anual")[1].split("\n")[1].split("###")[3].replace("Años","").replace("Año", "").trim())));
//			modelo.setAportacion(1);
//			modelo.setFechaEmision(fn.formatDate_MonthCadena(contenido.split("Emisión")[1].split("Moneda")[0].replace("\r\n", "").trim()));
//			
//			if(modelo.getFechaEmision().split("-").length == 3 
//			&& modelo.getFechaEmision().length() == 10 
//			&& modelo.getVigenciaDe().length() == 10
//			&& modelo.getVigenciaA().length() == 0) {
//				resultado = modelo.getFechaEmision().split("-")[0] + "-" + modelo.getVigencia_de().split("-")[1] + "-" + modelo.getVigencia_de().split("-")[2];
//				modelo.setVigencia_de(modelo.getFecha_emision().split("-")[0] + "-" + modelo.getVigencia_de().split("-")[1] + "-" + modelo.getVigencia_de().split("-")[2]);
//				modelo.setVigencia_a(Integer.parseInt(modelo.getFecha_emision().split("-")[0]) + 1 + "-" + modelo.getVigencia_de().split("-")[1] + "-" + modelo.getVigencia_de().split("-")[2]);
//			}
//		
//			
//			if(contenido.split("@@@")[2].split("\r\n").length == 1) {
//				if(contenido.split("@@@")[2].split("\r\n")[0].split("###").length == 1){
//					modelo.setPlan(contenido.split("@@@")[2].trim());
//				}
//			}
//			
//
//			if(fn.recorreContenido(contenido, "###Centro de utilidad") > 0) {
//				donde = fn.recorreContenido(contenido, "###Centro de utilidad");
//				for(String dato : contenido.split("@@@")[donde].split("\r\n")){
//					if(dato.contains("Agente:") && dato.split("\r\n").length == 1) {
//						if(dato.split("###").length == 3) {
//							if(dato.split("###")[0].contains("Agente:")) {
//								modelo.setAgente(dato.split("###")[0].split("Agente:")[1].trim());
//							}
//						}
//					}
//				}
//			}
//
//			donde = 0;
//			if(30 < contenido.split("@@@").length) {
//				newcontenido = fn.filtroPorRango(contenido, 30);
//				donde = fn.recorreContenido(contenido, "Agente:");
//				if(donde > 0){
//					if(newcontenido.split("@@@")[donde].split("\r\n").length == 1) {
//						if(newcontenido.split("@@@")[donde].split("\r\n")[0].split("###").length == 3){
//							if(newcontenido.split("@@@")[donde].split("\r\n")[0].split("###")[0].contains("Agente:")){
//								modelo.setCve_agente(newcontenido.split("@@@")[donde].split("\r\n")[0].split("###")[0].split("Agente:")[1].trim());
//							}
//						}
//					}
//				}
//			}
//			
//			
//
//			if(fn.recorreContenido(contenido, "VIDA###") > 0) {
//				donde = fn.recorreContenido(contenido, "VIDA###");
//				if(contenido.split("@@@")[donde].split("\r\n").length == 1) {
//					if(contenido.split("@@@")[donde].split("###").length == 3) {
//						modelo.setSa(contenido.split("@@@")[donde].split("###")[1].trim());
//					}
//				}
//			}
//
//			//asegurados { nombre nacimiento antiguedad sexo }
//			JSONObject jsonAseg = new JSONObject();
//			donde = 0;
//			newcontenido = fn.filtroPorRango(contenido, 8);
//			donde = fn.recorreContenido(newcontenido, "Datos del Asegurado");
//			
//			if(donde > 0) {
//				if(newcontenido.split("@@@")[donde].contains("Nombre") && newcontenido.split("@@@")[donde].split("\r\n").length == 2) {
//					if(newcontenido.split("@@@")[donde].split("\r\n")[1].contains("Nombre") && newcontenido.split("@@@")[donde].split("\r\n")[1].split("###").length == 2){
//						if(newcontenido.split("@@@")[donde].split("\r\n")[1].split("###")[0].contains("Nombre")){
//							jsonAseg.put("nombre", newcontenido.split("@@@")[donde].split("\r\n")[1].split("###")[0].split("Nombre")[1].trim());	
//						}
//					}
//				}
//			}
//			
//			donde = 0;
//			newcontenido = fn.filtroPorRango(contenido, 8);
//			donde = fn.recorreContenido(newcontenido, "Fecha de Nacimiento");
//			if(donde > 0) {
//				if(newcontenido.split("@@@")[donde].split("\r\n").length == 1) {
//					if(newcontenido.split("@@@")[donde].split("\r\n")[0].split("###").length == 3){
//						if(newcontenido.split("@@@")[donde].split("\r\n")[0].split("###")[0].contains("Nacimiento")){
//							if(fn.formatoTexto(newcontenido.split("@@@")[donde].split("\r\n")[0].split("###")[0].split("Nacimiento")[1].trim()).replace(" ", "###").split("###").length == 5) {
//								if(fn.formatoTexto(newcontenido.split("@@@")[donde].split("\r\n")[0].split("###")[0].split("Nacimiento")[1].trim()).replace(" ", "###").split("###")[1].trim().equals("de") &&  fn.formatoTexto(newcontenido.split("@@@")[donde].split("\r\n")[0].split("###")[0].split("Nacimiento")[1].trim()).replace(" ", "###").split("###")[3].trim().equals("de")) {
//									year = fn.formatoTexto(newcontenido.split("@@@")[donde].split("\r\n")[0].split("###")[0].split("Nacimiento")[1].trim()).replace(" ", "###").split("###")[4].trim();
//									month = fn.formatMonth(fn.formatoTexto(newcontenido.split("@@@")[donde].split("\r\n")[0].split("###")[0].split("Nacimiento")[1].trim()).replace(" ", "###").split("###")[2].trim());
//									day = fn.formatoTexto(newcontenido.split("@@@")[donde].split("\r\n")[0].split("###")[0].split("Nacimiento")[1].trim()).replace(" ", "###").split("###")[0].trim();
//									jsonAseg.put("nacimiento", year + "-" + month + "-" + day);
//								}
//							}
//						}
//					}
//				}
//			}
//			jsonAseg.put("sexo", 1);
//			jsonAseg.put("antiguedad", "");
//			jsonAseg.put("parentesco", 1);
//			jsonAseg.put("certificado", "");
//			jsonArrayAseg.put(jsonAseg);
//
//			modelo.setAsegurados(jsonArrayAseg);
//			
//
//			donde = 0;
//			if(15 < contenido.split("@@@").length) {
//				newcontenido = fn.filtroPorRango(contenido, 15);
//				donde = fn.recorreContenido(newcontenido, "Coberturas Amparadas") + 1;
//				if(donde > 1) {
//					if(newcontenido.split("@@@")[donde].split("\r\n").length == 1) {
//						if(newcontenido.split("@@@")[donde].split("\r\n")[0].split("###").length == 6){
//							JSONObject jsonCob = new JSONObject();
//							jsonCob.put("nombre", newcontenido.split("@@@")[donde].split("\r\n")[0].split("###")[0].replace("-", "").trim());
//							jsonCob.put("sa", newcontenido.split("@@@")[donde].split("\r\n")[0].split("###")[1].trim());
//							jsonCob.put("deducible", "");
//							jsonCob.put("coaseguro", "");
//							jsonCob.put("copago", "");
//							jsonCob.put("seccion", "");
//						 	jsonArray.put(jsonCob);
//						 	//jsonObject.put("coberturas", jsonArray);
//						}
//					}else if(newcontenido.split("@@@")[donde].split("\r\n").length > 1) {
//						for(String dato: newcontenido.split("@@@")[donde].split("\r\n")) {
//							if(dato.split("###").length == 6) {
//								JSONObject jsonCob = new JSONObject();
//								jsonCob.put("nombre", dato.split("###")[0].replace("-", "").trim());
//								jsonCob.put("sa", dato.split("###")[1].trim());
//								jsonCob.put("deducible", "");
//								jsonCob.put("coaseguro", "");
//								jsonCob.put("copago", "");
//								jsonCob.put("seccion", "");
//							 	jsonArray.put(jsonCob);
//							}else if(dato.split("###").length == 5) {
//								JSONObject jsonCob = new JSONObject();
//								if(dato.split("###")[0].split(" ").length >=2) {
//									if(dato.split("###")[0].split(dato.split("###")[0].split(" ")[dato.split("###")[0].split(" ").length-1]).length == 1){
//										jsonCob.put("nombre", dato.split("###")[0].split(dato.split("###")[0].split(" ")[dato.split("###")[0].split(" ").length-1])[0].replace("-", ""));
//									}
//									jsonCob.put("sa", dato.split("###")[0].split(" ")[dato.split("###")[0].split(" ").length-1]);
//								}
//								jsonCob.put("deducible", "");
//								jsonCob.put("coaseguro", "");
//								jsonCob.put("copago", "");
//								jsonCob.put("seccion", "");
//							 	jsonArray.put(jsonCob);
//							}
//						}
//					}
//				}
//			}
//
//			modelo.setCoberturas(jsonArray);
//				String  beneficiarios =  contenido.split("Beneficiarios")[1].split("ADVERTENCIA")[0].replace("@@@", "").replace(")", "###").replace("(", "###").trim();
//			for (String bene : beneficiarios.split("\n")) {
//				int n = bene.split("###").length;
//				
//				if(bene.contains("Nombres")) {
//
//					if(n ==  3) {
//						
//						JSONObject jsonBene = new JSONObject();
//						jsonBene.put("nombre", bene.split("###")[0].replace("Nombres:", "").trim());
//						jsonBene.put("nacimiento", "");
//						jsonBene.put("porcentaje",new Double(bene.split("###")[2].trim().replace("%", "")).intValue());
//						jsonBene.put("parentesco", fn.parent(bene.split("###")[1].toLowerCase()));
//						if (bene.split("###")[1].toLowerCase().contains("padre") || bene.split("###")[1].toLowerCase().contains("conyuge")) {
//							jsonBene.put("tipo", new Double(11));
//						}
//						jsonArrayBen.put(jsonBene);
//					}
//				}
//				if(bene.contains("FALLECIMIENTO")) {
//					String fall = beneficiarios.split("FALLECIMIENTO")[1].split("\n")[1];
//					if (fall.split("###").length == 3) {
//						JSONObject jsonBene = new JSONObject();
//						jsonBene.put("nombre", fall.split("###")[0].trim());
//						jsonBene.put("nacimiento", "");
//						jsonBene.put("porcentaje",new Double(fall.split("###")[2].replace("%","").trim()).intValue());
//						jsonBene.put("parentesco", fn.parent(fall.split("###")[1].toLowerCase().trim()));
//						if (fall.split("###")[1].toLowerCase().contains("madre")
//								|| fall.split("###")[1].toLowerCase().contains("suegra")) {
//							jsonBene.put("tipo", new Double(12));
//						}
//						jsonArrayBen.put(jsonBene);
//					}
//				}
//				
//			}
//
//			modelo.setBeneficiarios(jsonArrayBen);
//
//			switch (modelo.getForma_pago()) {
//			case 1:
//				if(jsonArrayRec.length() == 0) {
//					JSONObject jsonRec = new JSONObject();
//					jsonRec.put("recibo_id","") ;
//					jsonRec.put("vigencia_de", modelo.getVigencia_de());
//					jsonRec.put("vigencia_a", modelo.getVigencia_a());
//					jsonRec.put("vencimiento", "");
//					jsonRec.put("serie", "1/1");
//					jsonRec.put("prima_neta", fn.decimalF(modelo.getPrima_neta()).setScale(2, BigDecimal.ROUND_HALF_EVEN));
//					jsonRec.put("derecho", fn.decimalF(modelo.getDerecho()).setScale(2, BigDecimal.ROUND_HALF_EVEN));
//					jsonRec.put("recargo", fn.decimalF(modelo.getRecargo()).setScale(2, BigDecimal.ROUND_HALF_EVEN));
//					jsonRec.put("iva", fn.decimalF(modelo.getIva()).setScale(2, BigDecimal.ROUND_HALF_EVEN));
//					jsonRec.put("prima_total", fn.decimalF(modelo.getPrima_total()).setScale(2, BigDecimal.ROUND_HALF_EVEN));
//					jsonRec.put("ajuste_uno", modelo.getAjuste_uno());
//					jsonRec.put("ajuste_dos", modelo.getAjuste_dos());
//					jsonRec.put("cargo_extra", modelo.getCargo_extra());
//					jsonArrayRec.put(jsonRec);
//				}
//				break;
//				/*
//			case 2:
//				if(jsonArrayRec.length() == 1) {
//					JSONObject jsonRec = new JSONObject();
//					jsonRec.put("serie", "2/2");
//					jsonRec.put("vigencia_de", "");
//					jsonRec.put("vigencia_a", "");
//					jsonRec.put("vencimiento", "");
//					jsonRec.put("prima_neta", restoPrimaNeta);
//					jsonRec.put("prima_total", restoPrimaTotal);
//					jsonRec.put("recargo", restoRecargo);
//					jsonRec.put("derecho", restoDerecho);
//					jsonRec.put("iva", restoIva);
//					jsonRec.put("ajuste_uno", restoAjusteUno);
//					jsonRec.put("ajuste_dos", restoAjusteDos);
//					jsonRec.put("cargo_extra", restoCargoExtra);
//					jsonArrayRec.put(jsonRec);
//				}
//				break;
//			case 3: case 4: case 5: case 6: //NINGUN PDF TRAE RECIBOS SE QUEDA PENDIENTE ESTE CASO
//				if(jsonArrayRec.length() >= 1) {
//					int restoRec = (fn.getTotalRec(jsonObject.getInt("forma_pago")) - jsonArrayRec.length());
//					int totalRec = fn.getTotalRec(jsonObject.getInt("forma_pago"));
//					for(int i = jsonArrayRec.length(); i <= restoRec ; i++) {
//						JSONObject jsonRec = new JSONObject();
//						jsonRec.put("serie", i+1 + "/" + totalRec );
//						jsonRec.put("vigencia_de", "");
//						jsonRec.put("vigencia_a", "");
//						jsonRec.put("vencimiento", "");
//						jsonRec.put("prima_neta", (restoPrimaNeta / (restoRec)) );
//						jsonRec.put("prima_total", (restoPrimaTotal / (restoRec)));
//						jsonRec.put("recargo", (restoRecargo / (restoRec)));
//						jsonRec.put("derecho", (restoDerecho / (restoRec)));
//						jsonRec.put("iva", (restoIva / (restoRec)));
//						jsonRec.put("ajuste_uno", (restoAjusteUno / (restoRec)));
//						jsonRec.put("ajuste_dos", (restoAjusteDos / (restoRec)));
//						jsonRec.put("cargo_extra", (restoCargoExtra  / (restoRec)));
//						jsonArrayRec.put(jsonRec);
//					}	
//				}
//				break;
//				 */
//			}
//			//jsonObject.put("recibos", jsonArrayRec);
//			modelo.setRecibos(jsonArrayRec);
//			
//			 if(modelo.getCoberturas().length() > 0)
//	            {
//				 String tipo_vida = modelo.getCoberturas().getJSONObject(0).getString("nombre").toLowerCase();
//				 if(tipo_vida.contains("dotal")) {
//					 modelo.setTipovida(3);
//				 }else if(tipo_vida.contains("temporal")) {
//					 modelo.setTipovida(2);
//				 }
//	            }
//			jsonObject = new JSONObject(modelo);
//			
//			
//              return modelo;			
//		} catch (Exception ex) {
//			modelo.setError(
//					AxaVidaModel.this.getClass().getTypeName() + " | " + ex.getMessage() + " | " + ex.getCause());
//			return modelo;
//		}
//		
//		
//		
//		
//	}
//
//
//}
