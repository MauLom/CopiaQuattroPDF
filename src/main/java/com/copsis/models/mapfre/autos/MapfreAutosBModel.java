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
				;
		String newcontenido = "";
		int inicio = 0;
		int fin = 0;
		try {
			modelo.setTipo(1);
			modelo.setCia(22);

//			System.out.println(contenido);

			inicio = contenido.indexOf("SEGURO DE AUTOMÓVILES");
			if(inicio == -1) {
				inicio = contenido.indexOf("SEGURO DE MOTOCICLETAS");
			}
			fin = contenido.indexOf("Coberturas Amparadas");
			
			if(fin == -1) {
				fin = contenido.indexOf("APFRE###MÉXICO,###S.A.");
			}


			if (inicio > -1 & fin > -1 & inicio < fin) {
				newcontenido = contenido.substring(inicio, fin).replace("@@@", "").replace("\r", "");
				

				for (int i = 0; i < newcontenido.split("\n").length; i++) {
					System.out.println(newcontenido.split("\n")[i]);
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
                          modelo.setRfc(contenido.split("\n")[i].split("R.F.C:")[1].split("Sexo")[0].replace("###", ""));
                      }else {
                    	  modelo.setRfc(contenido.split("\n")[i].split("R.F.C:")[1].trim());
                      }
                    
					}
					
		
					if (newcontenido.split("\n")[i].contains("DOMICILIO:") &&  newcontenido.split("\n")[i].contains("Tel:")) {
                        modelo.setCteDireccion(newcontenido.split("\n")[i].split("DOMICILIO:")[1].split("Tel:")[0].replace("###", "").trim());
					}
					
					if (newcontenido.split("\n")[i].contains("Desde  de:") && newcontenido.split("\n")[i].contains("Clave de agente:") && newcontenido.split("\n")[i].contains("Nombre del agente:")  ) {
						modelo.setVigenciaDe(fn.formatDate_MonthCadena( newcontenido.split("\n")[i].split("Vigencia Desde  de:")[1].split("Clave de agente:")[0].replace("###", "")));
                        modelo.setFechaEmision(modelo.getVigenciaDe());
                        modelo.setAgente(newcontenido.split("\n")[i+1].split("###")[3].replace("###", "").trim());
					}
					if (newcontenido.split("\n")[i].contains("Hasta  de:")) {
						modelo.setVigenciaA(fn.formatDate_MonthCadena( newcontenido.split("\n")[i].split("Hasta  de:")[1].split("###")[1].replace("###", "")));

					}
					if (newcontenido.split("\n")[i].contains("Forma de pago:")  && newcontenido.split("\n")[i].contains("Moneda")) {
						modelo.setFormaPago(fn.formaPago(newcontenido.split("\n")[i+1].split("###")[1].trim()));
						modelo.setMoneda(1);
					}
					if (newcontenido.split("\n")[i].contains("AGENTE")) {
						modelo.setCveAgente(newcontenido.split("\n")[i].split("AGENTE")[1].replace("###", "").trim());
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
						modelo.setConductor(newcontenido.split("\n")[i].split("CONDUCTOR HABITUAL:")[1].split("Sexo")[0].replace("###", "").trim());
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
			

			inicio = contenido.indexOf("Coberturas Amparadas");
			fin = contenido.indexOf("Prima Neta:");

			if (inicio > -1 & fin > -1 & inicio < fin) {
				newcontenido = contenido.substring(inicio, fin).replace("@@@", "").replace("\r", "").replace("### ###", "###");
		         List<EstructuraCoberturasModel> coberturas = new ArrayList<>();
				for (int i = 0; i < newcontenido.split("\n").length; i++) {
					EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();
					if(newcontenido.split("\n")[i].contains("Coberturas Amparadas") || newcontenido.split("\n")[i].split("-").length > 3){
						
					}else {

						int sp =newcontenido.split("\n")[i].split("###").length;
						switch (sp) {
						case  3:
                              cobertura.setNombre(newcontenido.split("\n")[i].split("###")[0].trim());
                              cobertura.setSa(newcontenido.split("\n")[i].split("###")[1].trim());
                              cobertura.setDeducible(newcontenido.split("\n")[i].split("###")[2].trim());
                              coberturas.add(cobertura);
							break;

						}
					}
				}
				modelo.setCoberturas(coberturas);
			}

			return modelo;
		} catch (Exception e) {
			modelo.setError(e.getMessage());
			return modelo;
		}

	}

}
