package com.copsis.models.mapfre;

import java.util.ArrayList;
import java.util.List;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraAseguradosModel;
import com.copsis.models.EstructuraCoberturasModel;
import com.copsis.models.EstructuraJsonModel;

public class MapfreSaludBModel {

	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
	private String contenido = "";

	public MapfreSaludBModel(String contenido) {
		this.contenido = contenido;
	}
	public EstructuraJsonModel procesar() {
		contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());
		contenido = contenido.replace("Póliza Número :", "Póliza Número:")
				.replace("las 12:00 hrs. de:", "")
				.replace("SIN LIMITE", "###SIN LIMITE###")
				.replace("AYUDA DE MATERNIDAD", "AYUDA DE MATERNIDAD###")
				.replace("AMPARADA", "###AMPARADA###")
				.replace("BÁSICO", "###BÁSICO###");
		String newcontenido = "";
		int inicio = 0;
		int fin = 0;
		try {
			modelo.setTipo(3);
			modelo.setCia(22);

			inicio = contenido.indexOf("GASTOS MÉDICOS MAYORES");
			fin = contenido.indexOf("COBERTURAS SUMA ASEGURADA");
			
			if(inicio ==  -1) {
				inicio = contenido.indexOf("PLAN SERVICIOS");
			}
			if( fin == -1) {
				fin = contenido.indexOf("Plan de Seguro:");
				
			}
	
			if(inicio  == -1  && fin == -1  ) {
				inicio = contenido.indexOf("Tipo de Documento:");
				fin = contenido.indexOf("PAQUETE");				
			}
				

			if (inicio > -1 && fin > -1 && inicio < fin) {
				newcontenido = contenido.substring(inicio, fin).replace("@@@", "").replace("\r", "");
				for (int i = 0; i < newcontenido.split("\n").length; i++) {
					if(newcontenido.split("\n")[i].contains("Póliza Número:")) {
						modelo.setPoliza(newcontenido.split("\n")[i].split("Póliza Número:")[1].replace("###", "").trim());
					}
					if(newcontenido.split("\n")[i].contains("Contratante:") && newcontenido.split("\n")[i].contains("R.F.C:")) {
						modelo.setCteNombre(newcontenido.split("\n")[i].split("Contratante:")[1].split("R.F.C:")[0].replace("C.U.R.P:", "").replace("###", "").trim());
						modelo.setRfc(newcontenido.split("\n")[i].split("R.F.C:")[1].replace("###", "").trim());
					}
					if(newcontenido.split("\n")[i].contains("Domicilio:") && newcontenido.split("\n")[i].contains("Tel:")) {
						modelo.setCteDireccion(newcontenido.split("\n")[i].split("Domicilio:")[1].split("Tel:")[0].replace("###", "").trim());
					}
					if(newcontenido.split("\n")[i].contains("Desde") && newcontenido.split("\n")[i].contains("Clave de Agente:")) {
						modelo.setVigenciaDe(fn.formatDateMonthCadena( newcontenido.split("\n")[i].split("Desde")[1].split("Clave de Agente:")[0].replace("###", "").trim()));
						modelo.setCveAgente(newcontenido.split("\n")[i+1].split("###")[2].replace("###", "").trim());
						modelo.setAgente(newcontenido.split("\n")[i+1].split("###")[3].replace("###", "").trim());
					}
					if(newcontenido.split("\n")[i].contains("Hasta") ) {
						modelo.setVigenciaA(fn.formatDateMonthCadena(newcontenido.split("\n")[i].split("Hasta")[1].split("###")[1].replace("###", "").trim()));
					}
					if(newcontenido.split("\n")[i].contains("Fecha de Emisión") && newcontenido.split("\n")[i].contains("Forma de Pago:") && newcontenido.split("\n")[i].contains("Moneda") ) {
						modelo.setFechaEmision(fn.formatDateMonthCadena(newcontenido.split("\n")[i+1].split("###")[0].replace("###", "").replace(" ", "")).trim() );					
						modelo.setFormaPago(fn.formaPago( newcontenido.split("\n")[i+1].split("###")[2].replace("###", "").trim()));
						if(modelo.getFormaPago() == 0) {
							modelo.setFormaPago(fn.formaPagoSring(newcontenido.split("\n")[i+1].split("###")[2].replace("###", "").trim()));
						}
						modelo.setMoneda(1);
						
					}
					if(newcontenido.split("\n")[i].contains("Prima Neta:") && newcontenido.split("\n")[i].contains("Expedición") && newcontenido.split("\n")[i].contains("Prima Total:")  && newcontenido.split("\n")[i+1].split("###").length == 7) {
						modelo.setPrimaneta(fn.castBigDecimal( fn.preparaPrimas(newcontenido.split("\n")[i + 1].split("###")[0])));
						modelo.setRecargo(fn.castBigDecimal(fn.preparaPrimas(newcontenido.split("\n")[i + 1].split("###")[2])));
						modelo.setDerecho(fn.castBigDecimal(fn.preparaPrimas(newcontenido.split("\n")[i + 1].split("###")[3])));
						modelo.setIva(fn.castBigDecimal(fn.preparaPrimas(newcontenido.split("\n")[i + 1].split("###")[5])));
						modelo.setPrimaTotal(fn.castBigDecimal(fn.preparaPrimas(newcontenido.split("\n")[i + 1].split("###")[6])));						
					}
					if(newcontenido.split("\n")[i].contains("C.P:")) {
						modelo.setCp(newcontenido.split("\n")[i].split("C.P:")[1].replace("###", "").trim());
					} 
				}
			}
			
			
			
			inicio = contenido.indexOf("Plan de Seguro:");
			fin = contenido.indexOf("Asegurados que");

			
			if (inicio > -1 && fin > -1 && inicio < fin) {
				newcontenido = contenido.substring(inicio, fin).replace("@@@", "").replace("\r", "");
				for (int i = 0; i < newcontenido.split("\n").length; i++) {                     
                       if(newcontenido.split("\n")[i].contains("Plan de Seguro:")) {
                    	   modelo.setPlan(newcontenido.split("\n")[i].split("Plan de Seguro:")[1].trim());
                       }
				}
			}

			
			inicio = contenido.indexOf("COBERTURAS SUMA ASEGURADA");
			fin = contenido.indexOf("LAS ANTERIORES COBERTURAS");	
			
			
			if(inicio  == -1  && fin == -1  ) {
				inicio = contenido.indexOf("COBERTURAS BÁSICAS");
				fin = contenido.indexOf("LISTA DE ASEGURADOS");				
			}
				
System.out.println(inicio +"---->" + fin);
			if (inicio > -1 && fin > -1 && inicio < fin) {
				 List<EstructuraCoberturasModel> coberturas = new ArrayList<>();
				newcontenido = contenido.substring(inicio, fin).replace("@@@", "").replace("\r", "")
						.replace("10%", "###10%###")
						.replace("Usd", "###Usd###")
						.replace("30%", "###30%###")
						.replace("VISIÓN", "VISIÓN###")
						.replace("MUERTE ACCIDENTAL", "MUERTE ACCIDENTAL###")
						.replace("PERDIDAS ORGANICAS", "PERDIDAS ORGANICAS###")
						.replace("REEMBOLSO DE GASTOS MEDICOS", "REEMBOLSO DE GASTOS MEDICOS###");
				System.out.println(newcontenido);
				for (int i = 0; i < newcontenido.split("\n").length; i++) {
					EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();

					if(newcontenido.split("\n")[i].contains("DEDUCIBLE")) {						
					}else {
						int sp =newcontenido.split("\n")[i].split("###").length;
						switch (sp) {
						case  3:
							   cobertura.setNombre(newcontenido.split("\n")[i].split("###")[0].trim());
	                              cobertura.setSa(newcontenido.split("\n")[i].split("###")[1].trim());	                           
	                              coberturas.add(cobertura);
							break;
						case  4: 	case  5:
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
				
				
//				inicio = contenido.indexOf("LISTA DE ASEGURADOS:");
//				fin = contenido.indexOf("FECHAS DE ANTIGÜEDAD: ");	
//
//				
//			
//		
//				
//				if (inicio > -1 && fin > -1 && inicio < fin) {
//				       List<EstructuraAseguradosModel> asegurados = new ArrayList<>();
//					newcontenido = contenido.substring(inicio, fin).replace("@@@", "").replace("\r", "")
//							.replace(" F ", "###F###")
//							.replace("TITULAR", "###TITULAR###")
//							.replace("HIJO-A", "###HIJO-A###")						
//							.replace(" M ", "###M###")
//							.replace("CONYUGE", "###CONYUGE###");
//	
//					for (int i = 0; i < newcontenido.split("\n").length; i++) {
//						EstructuraAseguradosModel asegurado = new EstructuraAseguradosModel();
//						if(!newcontenido.split("\n")[i].contains("PARENTESCO")  && !newcontenido.split("\n")[i].contains("LISTA DE ASEGURADOS")) {						
//						
//							switch (newcontenido.split("\n")[i].split("###").length) {
//							case  5:
//								asegurado.setNombre(newcontenido.split("\n")[i].split("###")[0].split(newcontenido.split("\n")[i].split("###")[0].split(" ")[1])[1].trim());
//								asegurado.setSexo(fn.sexo(newcontenido.split("\n")[i].split("###")[1]) ? 1 : 0);
//								asegurado.setEdad(Integer.parseInt(newcontenido.split("\n")[i].split("###")[2].trim()));
//								asegurado.setParentesco(fn.parentesco(newcontenido.split("\n")[i].split("###")[3].trim()));
//								asegurado.setAntiguedad(fn.formatDateMonthCadena(newcontenido.split("\n")[i].split("###")[4].trim().split(" ")[1]));
//								asegurados.add(asegurado);
//								break;
//					
//							}
//						}
//					}
//					modelo.setAsegurados(asegurados);
//				}				
//
//					if(modelo.getAsegurados().isEmpty()) {
//						   inicio = contenido.indexOf("Asegurados que ampara");				
//						   fin = contenido.indexOf("EL CONTRATANTE GOZARÁ");
//						   List<EstructuraAseguradosModel> asegurados = new ArrayList<>();						  				
//										newcontenido = contenido.substring(inicio, fin).replace("@@@", "").replace("\r", "")
//						.replace(" F ", "###F###")
//						.replace("TITULAR", "###TITULAR###")
//						.replace("HIJO-A", "###HIJO-A###")
//						.replace("HIJOA", "###HIJOA###")
//						.replace(" M ", "###M###")
//						.replace("CONYUGE", "###CONYUGE###");
//						
//						for (int i = 0; i < newcontenido.split("\n").length; i++) {
//						EstructuraAseguradosModel asegurado = new EstructuraAseguradosModel();
//						if(!newcontenido.split("\n")[i].contains("Asegurados") ||  newcontenido.split("\n")[i].split("###").length == 2) {	
//								asegurado.setParentesco(fn.parentesco(newcontenido.split("\n")[i].split("###")[1].trim()));
//								asegurado.setEdad(Integer.parseInt(newcontenido.split("\n")[i].split("###")[0].split(" ")[newcontenido.split("\n")[i].split("###")[0].split(" ").length-1]));
//								asegurado.setNacimiento(fn.formatDateMonthCadena(newcontenido.split("\n")[i].split("###")[0].split(" ")[newcontenido.split("\n")[i].split("###")[0].split(" ").length-2]));
//								asegurado.setNombre(newcontenido.split("\n")[i].split(newcontenido.split("\n")[i].split("###")[0].split(" ")[newcontenido.split("\n")[i].split("###")[0].split(" ").length-2])[0].trim());
//								asegurados.add(asegurado);											
//					        }
//							
//						 }
//						modelo.setAsegurados(asegurados);
//					}
//			
//			
			return modelo;
		} catch (Exception e) {
			e.printStackTrace();
			return modelo;
		}
		
	}

}
