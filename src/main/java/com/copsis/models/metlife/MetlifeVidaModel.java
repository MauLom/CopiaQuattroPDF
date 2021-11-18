package com.copsis.models.metlife;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraAseguradosModel;
import com.copsis.models.EstructuraBeneficiariosModel;
import com.copsis.models.EstructuraCoberturasModel;
import com.copsis.models.EstructuraJsonModel;

public class MetlifeVidaModel {
	
	// Clases
	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
	// Varaibles
	private String contenido = "";
	private String newcontenido = "";
	private String recibosText = "";
	private String resultado = "";
	private int inicio = 0;
	private int fin = 0;
	int longitud_split = 0;
	int donde = 0;
	int yy = 0;
	String y ="";
	String date2 ="";

	
	
	public MetlifeVidaModel(String contenido) {
		this.contenido = contenido;	
	}
	public EstructuraJsonModel procesar() {
		contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());
		contenido =contenido.replace("R. F. C:", "RFC:");
		try {
			  // tipo
            modelo.setTipo(5);
            // cia
            modelo.setCia(23);
            // poliza
            
            
            inicio = contenido.indexOf("contratante");
            fin = contenido.indexOf("COBERTURAS");
            if(inicio > 0 && fin > 0 && inicio < fin) {
            	newcontenido = contenido.substring(inicio,  fin).replace("\r", "").replace("@@@", "").trim();
            	for (int i = 0; i < newcontenido.split("\n").length; i++) {            	
            		if(newcontenido.split("\n")[i].contains("contratante") && newcontenido.split("\n")[i].contains("Póliza")) {
            			modelo.setCteNombre(newcontenido.split("\n")[i+1].split("###")[0].trim());
            			modelo.setPoliza(newcontenido.split("\n")[i+1].split("###")[1].trim());
            		}
            		if(newcontenido.split("\n")[i].contains("domicilio") && newcontenido.split("\n")[i].contains("nacimiento")) {
            			resultado =newcontenido.split("\n")[i +2].split("###")[0] +" "+ newcontenido.split("\n")[i +3].split("###")[0] 
            			+" "+newcontenido.split("\n")[i +4].split("###")[0];
            			modelo.setCteDireccion(resultado);
            		}
            		if(newcontenido.split("\n")[i].contains("Moneda") && newcontenido.split("\n")[i].contains("Periodicidad")) {         
            			modelo.setFormaPago(fn.formaPago(newcontenido.split("\n")[i+2].split("###")[0].trim()));
            			modelo.setMoneda(fn.moneda(newcontenido.split("\n")[i+2].split("###")[1].trim()));            		
            		}
            	}
            }
            
          //aportaciones
			if(fn.recorreContenido(contenido, "TEMPOLIFE POLIZA DE") > -1) {
				modelo.setAportacion(0);
			}else {
				modelo.setAportacion(1);
			}
            
            //Aseguros
            donde = fn.recorreContenido(contenido, "Nombre y domicilio");
			if(donde > 0) {
			 	List<EstructuraAseguradosModel> asegurados = new ArrayList<>();
		   		EstructuraAseguradosModel asegurado = new EstructuraAseguradosModel();
				if(contenido.split("@@@")[donde].split("\r\n").length > 1) {
					if(contenido.split("@@@")[donde].split("\r\n")[1].split("###").length > 1) {
						for(int i = 0; i < contenido.split("@@@")[donde].split("\r\n").length; i++){
							if(contenido.split("@@@")[donde].split("\r\n")[i].trim().contains("Nombre y domicilio")){
								if((i+1) < contenido.split("@@@")[donde].split("\r\n").length) {
									if(contenido.split("@@@")[donde].split("\r\n")[i+1].trim().contains("Día")) {
										if((i+2) < contenido.split("@@@")[donde].split("\r\n").length ) {
											if(contenido.split("@@@")[donde].split("\r\n")[i+2].split("###").length == 5) {
												resultado = fn.formatDateMonthCadena(contenido.split("@@@")[donde].split("\r\n")[i+2].split("###")[1].trim() + "-" + contenido.split("@@@")[donde].split("\r\n")[i+2].split("###")[2].trim() + "-" + contenido.split("@@@")[donde].split("\r\n")[i+2].split("###")[3].trim()); 
											}else {
												resultado = "";
											}
										}
									}
									
									asegurado.setNombre(contenido.split("@@@")[donde].split("\r\n")[i+1].trim().split("###")[0].trim());
									asegurado.setNacimiento(resultado);
									asegurados.add(asegurado);
									
									
								}
							}
						}		
					}else {
						if(contenido.split("@@@")[donde +1].split("\r\n").length == 2) {
							if(contenido.split("@@@")[donde +1].split("\r\n")[0].split("###").length == 4) {
								resultado = contenido.split("@@@")[donde +1].split("\r\n")[0].split("###")[0].trim();
								
							}else if (contenido.split("@@@")[donde +1].split("\r\n")[0].split("###").length == 6) {
								resultado = contenido.split("@@@")[donde +1].split("\r\n")[0].split("###")[0].trim();
							}
						}
						if(contenido.split("@@@")[donde +2].split("\r\n").length == 1) {
							 longitud_split = contenido.split("@@@")[donde +2].split("\r\n")[0].split("###").length-1;
							 if(contenido.split("@@@")[donde +2].split("\r\n")[0].split("###")[longitud_split].trim().length() == 4) {
								 newcontenido = contenido.split("@@@")[donde +2].split("\r\n")[0].split("###")[longitud_split].trim();
							 }
							 if(contenido.split("@@@")[donde +2].split("\r\n")[0].split("###")[longitud_split-1].trim().length() == 2) {
								 newcontenido += "-" + contenido.split("@@@")[donde +2].split("\r\n")[0].split("###")[longitud_split-1].trim();
							 }
							 if(contenido.split("@@@")[donde +2].split("\r\n")[0].split("###")[longitud_split-2].trim().length() == 2) {
								 newcontenido += "-" + contenido.split("@@@")[donde +2].split("\r\n")[0].split("###")[longitud_split-2].trim();
							 }
							 if(newcontenido.split("-").length == 3) {	
									asegurado.setNombre(resultado);
									asegurado.setNacimiento(newcontenido);
									asegurados.add(asegurado);
		
							 }
						}
					}
				}
				modelo.setAsegurados(asegurados);
			}
		
            
            inicio = contenido.indexOf("RFC:");
            fin = contenido.indexOf("SOLICITUD");
            if(inicio > -1 &  fin  > -1  & inicio < fin)                      {
            	newcontenido =contenido.substring(inicio, fin).replace("@@@", "").replace("\r", "");
            	for (int i = 0; i < newcontenido.split("\n").length; i++) {  
            		if(newcontenido.split("\n")[i].contains("RFC:")) {
            		modelo.setRfc(newcontenido.split("\n")[i].split("RFC:")[1]);	
            		}
            	}            	
            }
            
            
            
            
            
            
            
            
            
            if(contenido.contains("Lugar y Fecha")) {
				int index = 0;
				for(String x: contenido.split("Lugar y Fecha")) {				
					if(x.split("\r\n")[0].trim().length() > 0 && index > 0) {							
						longitud_split = x.split("\r\n")[0].trim().split(" ").length-1;	
						y = x.split("\r\n")[0].trim().replace("MEXICO, D.F. A ", "").replace(" DE ", "-").replace(" DEL ","-").replace("###", "");						
						modelo.setFechaEmision(fn.formatDateMonthCadena(y));
						y = x.split("\r\n")[0].trim().split(" ")[longitud_split]; 
						break;
					}
					index ++;
				}
			}
            
          //coberturas
			donde = 0;
			newcontenido = "";
			donde = fn.searchTwoTexts(contenido, "COBERTURAS", "Suma");
			if(donde > 0) {
				List<EstructuraCoberturasModel> coberturas = new ArrayList<>();
				newcontenido = contenido.split("@@@")[donde + 1].trim();
				if(newcontenido.split("\r\n").length > 1) {
					for(String x: newcontenido.split("\r\n")) {
						EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();
						if(x.split("###").length > 3) {
							cobertura.setNombre( x.split("###")[0].trim());
							cobertura.setSa( x.split("###")[1].trim());
							cobertura.setNombre( x.split("###")[0].trim());
							coberturas.add(cobertura);
							if(x.contains("TEMPORAL")) {
								if(x.split("###").length == 6) {
									if(x.split("###")[2].trim().split(" ").length == 3){
										String date = fn.formatDateMonthCadena(x.split("###")[2].trim().replace(" ", "-"));
										date2 = date;
										date = date.replace(date.split("-")[0], y);
										modelo.setVigenciaDe(date);
									}
									if(x.split("###")[3].trim().split(" ").length == 3) {
										String date = fn.formatDateMonthCadena(x.split("###")[3].trim().replace(" ", "-"));	
										date2 = date;
									    yy = new Integer(y); 
									    yy =yy+1;
										date = date.replace(date.split("-")[0], Integer.toString(yy));	
										modelo.setVigenciaA(date);
									}
								}
							}
						}
					}
				}
				modelo.setCoberturas(coberturas);
			}
			
			//plazo
			date2 = date2.split("-")[0];
			newcontenido= modelo.getVigenciaDe().split("-")[0];
			modelo.setPlazo(new Integer(date2)-new Integer(newcontenido));
			//plazo_pago
			modelo.setPlazoPago(modelo.getPlazo()-1);	
			//beneficiarios
//			
//			inicio = contenido.indexOf("BENEFICIARIOS:");
//			List<EstructuraBeneficiariosModel> beneficiarios = new ArrayList<>();
//			if(inicio > -1) {
//				newcontenido = contenido.substring(inicio+14,contenido.length());
//				fin= newcontenido.indexOf("Lugar y Fecha");
//				if(fin > -1) {
//					
//					
//					
//					newcontenido = newcontenido.substring(0,fin).replace("@@@","").trim();
//					
//					for(int i = 0;i < newcontenido.split("\n").length; i++) {
//						EstructuraBeneficiariosModel beneficiario = new EstructuraBeneficiariosModel();
//						String a = newcontenido.split("\n")[i].replace("%","").trim();
//						String b = "";
//							if(i+1 < newcontenido.split("\n").length) {
//								b = newcontenido.split("\n")[i+1].replace("%","").trim();
//							}
//														
//							if(i == 0) {
//								longitud_split = a.split(" ").length;
//								beneficiario.setPorcentaje( new Double(a.split(" ")[longitud_split-1]).intValue());
//								beneficiario.setParentesco(fn.parentesco(a.split(" ")[longitud_split-2].toLowerCase()));
//								beneficiario.setNombre(a.split(a.split(" ")[longitud_split-2])[0].trim());
//								beneficiario.setTipo(11);
//								beneficiarios.add(beneficiario);
//
//							}
//							else if(a.contains("ASEGURADO DEPENDIENTE:")) {
//								longitud_split = b.split(" ").length;
//								
//								beneficiario.setPorcentaje(0);
//								beneficiario.setParentesco(1);
//								beneficiario.setNombre(b.trim());
//								beneficiario.setTipo(11);
//								beneficiarios.add(beneficiario);
//							
//							}else if(a.contains("BENEFICIARIOS:")) {
//								longitud_split = b.split(" ").length;
//								
//								beneficiario.setPorcentaje(new Double(b.split(" ")[longitud_split-1]).intValue());
//								beneficiario.setParentesco(fn.parentesco(b.split(" ")[longitud_split-2].toLowerCase()));
//								beneficiario.setNombre(b.split(b.split(" ")[longitud_split-2])[0].trim());
//								beneficiario.setTipo(12);
//								beneficiarios.add(beneficiario);
//								
//				
//							}else if(a.contains("EN CASO DE FALLECIMIENTO DEL PRIMER BENEFICIARIO EL SEGUNDO SER")) {
//								b = b.replace("(", " ").replace(")", "");
//								longitud_split = b.split(" ").length;
//								beneficiario.setPorcentaje(0);
//								beneficiario.setParentesco(fn.parentesco(b.split(" ")[longitud_split-2].toLowerCase()));
//								beneficiario.setNombre(b.split(b.split(" ")[longitud_split-2])[0].trim());
//								beneficiario.setTipo(12);
//								beneficiarios.add(beneficiario);
//								
//								
//								
//							}							
//						}
//					}	
//				}else {
//					inicio = contenido.indexOf("y porcentaje de participación.");
//					fin = contenido.indexOf("@@@Para cualquier duda o aclaración");
//					if(inicio>-1 && fin>inicio)  {
//							newcontenido=contenido.substring(inicio+30, fin).trim();							
//							for( String dato: newcontenido.split("\n")) {
//								EstructuraBeneficiariosModel beneficiario = new EstructuraBeneficiariosModel();
//								
//								beneficiario.setPorcentaje(new Integer(dato.split("###")[2].replace("%","").trim()));
//								beneficiario.setParentesco(fn.parentesco(dato.split("###")[1].trim().toLowerCase()));
//								beneficiario.setNombre(dato.split("###")[0]);
//								beneficiario.setTipo(12);
//								beneficiarios.add(beneficiario);
//								
//								
//							
//							}
//					}
//				}
//			modelo.setBeneficiarios(beneficiarios);
			
            
			
			
			return modelo;
		} catch (Exception ex) {
			modelo.setError(
					MetlifeVidaModel.this.getClass().getTypeName() + " - catch:" + ex.getMessage() + " | " + ex.getCause());
			return modelo;
		}
	}

}
