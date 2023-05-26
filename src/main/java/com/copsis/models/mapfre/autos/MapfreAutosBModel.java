package com.copsis.models.mapfre.autos;

import java.util.ArrayList;
import java.util.List;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraCoberturasModel;
import com.copsis.models.EstructuraJsonModel;

public class MapfreAutosBModel {
	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
	private String contenido = "";

	public MapfreAutosBModel(String contenido) {
		this.contenido = contenido;
	}

	public EstructuraJsonModel procesar() {
		contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());
		contenido = contenido.replace("las 12:00 hrs.", "")
				.replace("VALOR COMERCIAL", "###VALOR COMERCIAL###")
				.replace("AMPARADA", "###AMPARADA###")
				.replace("RC2 A TERCEROS EN SUS BIENES", "RC2 A TERCEROS EN SUS BIENES###")
				.replace("RC2 A TERCEROS EN SUS PERSONAS", "RC2 A TERCEROS EN SUS PERSONAS###")
				.replace("DSMGVDF3", "###DSMGVDF3")
				.replace("GASTOS MÉDICOS", "GASTOS MÉDICOS###")
				.replace("POR EVENTO", "###POR EVENTO")
				.replace("ACCIDENTES AL CONDUCTOR", "ACCIDENTES AL CONDUCTOR###")
				.replace("NO APLICA", "###NO APLICA")
				.replace("RC CAT5 POR MUERTE ACCIDENTAL", "RC CAT5 POR MUERTE ACCIDENTAL###")
				.replace("AMPARADO", "###AMPARADO")
				.replace("RC* CATASTRÓFICA", "RC* CATASTRÓFICA###")
				.replace("RC* A TERCEROS EN SUS BIENES", "RC* A TERCEROS EN SUS BIENES###")
				.replace("RC* A TERCEROS EN SUS PERSONAS", "RC* A TERCEROS EN SUS PERSONAS###")
				.replace("DSMGVDF**", "###DSMGVDF**")
				.replace("SEGURO AUTOPLUS", "SEGURO DE AUTOMÓVILES")
				.replace("SEGURO MOTORÍZATE", "SEGURO DE AUTOMÓVILES")
				.replace("PÓLIZA-ENDOSO", "PÓLIZA NÚMERO:")
				.replace("Contratante:", "CONTRATANTE:")
				.replace("Domicilio:", "DOMICILIO:")
				.replace("Conductor Habitual", "CONDUCTOR HABITUAL")
				;
		String newcontenido = "";
		int inicio = 0;
		int fin = 0;
		try {
			modelo.setTipo(1);
			modelo.setCia(22);

 	        inicio = contenido.indexOf("TIPO DE DOCUMENTO:");
			if(inicio == -1) {
				inicio = contenido.indexOf("SEGURO DE MOTOCICLETAS");
			}
			 
			fin = contenido.indexOf("Coberturas Amparadas");
			
			if(fin == -1) {
				fin = contenido.indexOf("APFRE###MÉXICO,###S.A.");
			}


			if (inicio > -1 && fin > -1 && inicio < fin) {
				newcontenido = contenido.substring(inicio, fin).replace("@@@", "").replace("\r", "");
				for (int i = 0; i < newcontenido.split("\n").length; i++) {
					if (newcontenido.split("\n")[i].contains("PÓLIZA NÚMERO:")) {
						if(newcontenido.split("\n")[i].split("PÓLIZA NÚMERO:")[1].contains("-")) {
							modelo.setPoliza(newcontenido.split("\n")[i].split("PÓLIZA NÚMERO:")[1].split("-")[0].replace("###", "").trim());
						}else {
							modelo.setPoliza(newcontenido.split("\n")[i].split("PÓLIZA NÚMERO:")[1].replace("###", ""));	
						}
						
					}
				
					if (newcontenido.split("\n")[i].contains("CONTRATANTE:") && newcontenido.split("\n")[i].contains("R.F.C:")) {
                      modelo.setCteNombre(newcontenido.split("\n")[i].split("CONTRATANTE:")[1].split("R.F.C:")[0].replace("###", "").trim());
                    
					  if(newcontenido.split("\n")[i].contains("Sexo")) {
						
							modelo.setRfc(newcontenido.split("\n")[i].split("R.F.C:")[1].split("Sexo")[0].replace("###", "").trim());
						                       						
                      }else {						
                          modelo.setRfc(newcontenido.split("\n")[i].split("R.F.C:")[1].replace("###", "").trim());
                      }
                    
					}
					
		
					if (newcontenido.split("\n")[i].contains("DOMICILIO:") &&  newcontenido.split("\n")[i].contains("Tel:")) {
                        modelo.setCteDireccion(newcontenido.split("\n")[i].split("DOMICILIO:")[1].split("Tel:")[0].replace("###", "").trim());
					}
					
					if (newcontenido.split("\n")[i].contains("Desde  de:") && newcontenido.split("\n")[i].contains("Clave de agente:") && newcontenido.split("\n")[i].contains("Nombre del agente:")  ) {
						modelo.setVigenciaDe(fn.formatDateMonthCadena( newcontenido.split("\n")[i].split("Vigencia Desde  de:")[1].split("Clave de agente:")[0].replace("###", "")));
                        modelo.setAgente(newcontenido.split("\n")[i+1].split("###")[3].replace("###", "").trim());
					}
					if (newcontenido.split("\n")[i].contains("Hasta  de:")) {
						modelo.setVigenciaA(fn.formatDateMonthCadena( newcontenido.split("\n")[i].split("Hasta  de:")[1].split("###")[1].replace("###", "")));

					}
					if (newcontenido.split("\n")[i].contains("Fecha de emisión") && (i+1)<newcontenido.split("\n").length ) {
						String texto = newcontenido.split("\n")[i+1].split("###")[0].trim();
						if(texto.split("-").length == 3) {
							modelo.setFechaEmision(fn.formatDateMonthCadena(texto));
						}
					}
					if (newcontenido.split("\n")[i].contains("Forma de pago:")  && newcontenido.split("\n")[i].contains("Moneda")) {						
						modelo.setFormaPago(fn.formaPago(newcontenido.split("\n")[i+1].split("###")[1].trim()));
						if(modelo.getFormaPago() == 0){
							modelo.setFormaPago(fn.formaPagoSring(newcontenido.split("\n")[i+1].split("###")[1].trim()));
						}
						modelo.setMoneda(1);
					}
					if (newcontenido.split("\n")[i].contains("AGENTE") &&  !newcontenido.split("\n")[i].contains("CAMBIO AGENTE")) {
						String texto = newcontenido.split("\n")[i].split("AGENTE")[1].replace("###", "").trim();
						if(!texto.contains("SA DE CV")) {
							modelo.setCveAgente(newcontenido.split("\n")[i].split("AGENTE")[1].replace("###", "").trim());
						}
					}else if(newcontenido.split("\n")[i].contains("Clave de agente:") && (i+1)<newcontenido.split("\n").length) {
						if(newcontenido.split("\n")[i+1].contains(modelo.getAgente())) {
							String texto = fn.gatos(newcontenido.split("\n")[i+1].split(modelo.getAgente())[0]);
							String[] aux = texto.split("###");
							if(aux.length>0) {
								modelo.setCveAgente(aux[aux.length-1].trim());
							}
						}
					}
					if (newcontenido.split("\n")[i].contains("Descripción:") &&  newcontenido.split("\n")[i].contains("Uso:") &&  newcontenido.split("\n")[i].contains("Placas:")) {		
						modelo.setDescripcion(newcontenido.split("\n")[i].split("Descripción:")[1].split("Uso:")[0].replace("###", "").trim());
						modelo.setPlacas(newcontenido.split("\n")[i].split("Placas:")[1].replace("###", "").trim());
					}
					if (newcontenido.split("\n")[i].contains("Marca:") && newcontenido.split("\n")[i].contains("Clase:") && newcontenido.split("\n")[i].contains("Fabricación:")) {
						modelo.setMarca(newcontenido.split("\n")[i].split("Marca:")[1].split("Clase:")[0].replace("###", "").trim());
						modelo.setModelo(fn.castInteger(newcontenido.split("\n")[i].split("Fabricación:")[1].replace("###", "").trim()));
					}
					
					if (newcontenido.split("\n")[i].contains("Número de Serie:") && newcontenido.split("\n")[i].contains("Remolque:")) {
						modelo.setSerie(newcontenido.split("\n")[i].split("Número de Serie:")[1].split("Remolque:")[0].replace("###", "").trim());
						
					} 
					if(newcontenido.split("\n")[i].contains("C.P:")) {			
						if(newcontenido.split("\n")[i].split("C.P:")[1].length() > 6) {
							modelo.setCp(newcontenido.split("\n")[i].split("C.P:")[1].replace("###", "").trim().substring(0, 5));
						}else {
							modelo.setCp(newcontenido.split("\n")[i].split("C.P:")[1].replace("###", "").trim());	
						}
						
					}
					if(newcontenido.split("\n")[i].contains("CONDUCTOR HABITUAL:") && newcontenido.split("\n")[i].contains("Sexo")) {					
						modelo.setConductor(newcontenido.split("\n")[i].split("CONDUCTOR HABITUAL:")[1].split("Sexo")[0].split("R.F.C")[0].replace("###", "").trim());
						
					}				
					
					if (newcontenido.split("\n")[i].contains("Prima neta:") &&  newcontenido.split("\n")[i].contains("Prima total")) {					
                        modelo.setPrimaneta(fn.castBigDecimal( fn.preparaPrimas(newcontenido.split("\n")[i + 1].split("###")[0])));
                        modelo.setRecargo(fn.castBigDecimal(fn.preparaPrimas(newcontenido.split("\n")[i + 1].split("###")[2])));
                        modelo.setDerecho(fn.castBigDecimal(fn.preparaPrimas(newcontenido.split("\n")[i + 1].split("###")[3])));
                        modelo.setIva(fn.castBigDecimal(fn.preparaPrimas(newcontenido.split("\n")[i + 1].split("###")[4])));
                        modelo.setPrimaTotal(fn.castBigDecimal(fn.preparaPrimas(newcontenido.split("\n")[i + 1].split("###")[5])));
						
					}
				}
			}
			
			if(modelo.getFechaEmision().length() == 0) {
                modelo.setFechaEmision(modelo.getVigenciaDe());
			}

			inicio = contenido.indexOf("Coberturas Amparadas");
			fin = contenido.indexOf("Prima Neta:");

			if (inicio > -1 && fin > -1 && inicio < fin) {
				newcontenido = contenido.substring(inicio, fin).replace("@@@", "").replace("\r", "").replace("### ###", "###");
				String[] arrContenido = newcontenido.split("\n");
		         List<EstructuraCoberturasModel> coberturas = new ArrayList<>();
				for (int i = 0; i < arrContenido.length; i++) {
					EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();
					if(newcontenido.split("\n")[i].contains("###") && newcontenido.split("\n")[i].contains(newcontenido)) {
						
					}
					if(!arrContenido[i].contains("Coberturas Amparadas") || arrContenido[i].split("-").length < 3){
						arrContenido[i] = agregarSeparadorCobertura(arrContenido[i]);
						int sp =arrContenido[i].split("###").length;
						switch (sp) {
						case  3:
                              cobertura.setNombre(arrContenido[i].split("###")[0].trim());
                              cobertura.setSa(arrContenido[i].split("###")[1].trim());
                              cobertura.setDeducible(arrContenido[i].split("###")[2].trim());
                              coberturas.add(cobertura);
							break;

						}
					}
				}
				modelo.setCoberturas(coberturas);
			}

			return modelo;
		} catch (Exception e) {
			modelo.setError(MapfreAutosBModel.this.getClass().getTypeName() + " | "+ e.getMessage() + " | "+e.getCause());
			return modelo;
		}

	}

	private String agregarSeparadorCobertura(String texto) {
		if(!texto.contains("###") && texto.contains("INT. OCUP. VEHIC. PART")) {
			texto = texto.replace("INT. OCUP. VEHIC. PART", "INT. OCUP. VEHIC. PART###");
		}
		if(texto.split("###").length == 2) {
			if (texto.contains("UMA")) {
				String[] aux = texto.trim().split(" ");
				if(aux[aux.length-1].startsWith("UMA") && aux.length >2) {
					String deducible = aux[aux.length-2] +" " + aux[aux.length-1];
					texto = texto.replace(deducible, "###"+deducible);
				}
			}
		}
		return texto;
	}
}
