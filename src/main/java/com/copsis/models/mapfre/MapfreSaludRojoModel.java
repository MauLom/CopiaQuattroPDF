package com.copsis.models.mapfre;

import java.util.ArrayList;
import java.util.List;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraAseguradosModel;
import com.copsis.models.EstructuraCoberturasModel;
import com.copsis.models.EstructuraJsonModel;
import com.copsis.models.EstructuraRecibosModel;

public class MapfreSaludRojoModel {
	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
	private String contenido = "";

	public MapfreSaludRojoModel(String contenido) {
		this.contenido = contenido;
	}
	public EstructuraJsonModel procesar() {
		contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());
		contenido = contenido.replace("COBERTURAS###Y###SERVICIOS", "COBERTURAS Y SERVICIOS")
				.replace("FECHA###DE###EMISIÓN", "FECHA DE EMISIÓN")
				.replace("LAS###12:00###HRS.###DEL:", "")
				.replace("PLAN###CONTRATADO:", "PLAN CONTRATADO:")
				.replace("ZONA###DE###CONTRATACIÓN", "ZONA DE CONTRATACIÓN")
				.replace("FORMA###DE###PAGO:", "FORMA DE PAGO:")
				.replace("PRIMA###NETA", "PRIMA NETA")
				.replace("PRIMA###TOTAL:", "PRIMA TOTAL:")
				.replace("EMPLEADOS EN SERVICIO ACTIVO DEL ","")
				.replace("CONTRATANTE, QUE CUENTEN CON","")
				.replace("RELACIÓN LABORAL DIRECTA Y","")
				.replace("COMPROBABLE, ASÍ COMO SUS","")
				.replace("DEPENDIENTES ECONÓMICOS, SIENDO","")
				.replace("ÉSTOS LOS SIGUIENTES: EL-LA CÓNYUGE E","")
			    .replace("HIJOS SOLTEROS MENORES DE 25 AÑOS DE","");		
		String newcontenido = "";
		int inicio = 0;
		int fin = 0;
		try {
		
			
			modelo.setTipo(3);
			modelo.setCia(22);

			inicio = contenido.indexOf("PROTECCION MEDICA");
			fin = contenido.indexOf("COBERTURAS Y SERVICIOS");
			if(inicio == -1 && fin == -1) {
				inicio = contenido.indexOf("GASTOS MÉDICOS MAYORES");
				fin = contenido.indexOf("ASEGURADO###TITULAR");
			}
			
			
			if(inicio == -1) {
				inicio = contenido.indexOf("PÓLIZA-ENDOSO");
			}

			if (inicio > -1 && fin > -1 && inicio < fin) {
				newcontenido = contenido.substring(inicio, fin).replace("@@@", "").replace("\r", "").replace("######","###").replace("COLECTIVIDAD###ASEGURABLE:", "");
				for (int i = 0; i < newcontenido.split("\n").length; i++) {
					
					if(newcontenido.split("\n")[i].contains("PÓLIZA-ENDOSO")  && newcontenido.split("\n")[i].split("PÓLIZA-ENDOSO")[1].replace("###", "").trim().contains("-")) {
						
							 modelo.setPoliza(newcontenido.split("\n")[i].split("PÓLIZA-ENDOSO")[1].replace("###", "").trim().replace("-", "/"));
							 if(modelo.getPoliza().contains("/")) {
								 modelo.setEndoso(modelo.getPoliza().split("/")[1]); 
							 }										
					}
					if(newcontenido.split("\n")[i].contains("FECHA DE EMISIÓN")) {
						modelo.setFechaEmision(fn.formatDateMonthCadena(newcontenido.split("\n")[i].split("FECHA DE EMISIÓN")[1].replace("###", "").trim()));
						
						if(newcontenido.split("\n")[i+1].contains("AGENTE:")) {						    
						    if(newcontenido.split("\n")[i+1].split("AGENTE:")[1].replace("###", "").trim().contains(",")) {
						        modelo.setAgente((newcontenido.split("\n")[i+1].split("AGENTE:")[1].replace("###", "").trim().split(",")[1] +" " + newcontenido.split("\n")[i+1].split("AGENTE:")[1].replace("###", "").trim().split(",")[0]).trim());    
						    }else {
						        modelo.setAgente(newcontenido.split("\n")[i+1].split("AGENTE:")[1].replace("###", "").trim());
						    }
                            
                        }
						if(newcontenido.split("\n")[i+2].contains("CLAVE DE AGENTE:")) {
							modelo.setCveAgente(newcontenido.split("\n")[i+2].split("AGENTE:")[1].replace("###", "").trim());
						}
					}
					if(newcontenido.split("\n")[i].contains("DESDE") && newcontenido.split("\n")[i].contains("TIPO")) {
						modelo.setVigenciaDe(fn.formatDateMonthCadena(newcontenido.split("\n")[i].split("DESDE")[1].split("TIPO")[0].replace(":", "").replace("###", "")));
					}
					if(newcontenido.split("\n")[i].contains("HASTA") && newcontenido.split("\n")[i].contains("CLIENTE")) {
						modelo.setVigenciaA(fn.formatDateMonthCadena(newcontenido.split("\n")[i].replace("HASTA","HASTA:").replace("::", ":").split("HASTA:")[1].split("CLIENTE")[0].replace("###", "")));
					}
					if(newcontenido.split("\n")[i].contains("PLAN CONTRATADO:")){
						modelo.setPlan((newcontenido.split("\n")[i].split("PLAN CONTRATADO:")[1] +" "+ newcontenido.split("\n")[i+1]).replace("###", "").trim().replace("H.F. DIAMANTE NACIONAL a", "H.F. DIAMANTE NACIONAL"));
					}
					if(newcontenido.split("\n")[i].contains("CONTRATANTE:") &&  newcontenido.split("\n")[i].contains("ZONA DE CONTRATACIÓN:")){
						modelo.setCteNombre(newcontenido.split("\n")[i].split("CONTRATANTE:")[1].split("ZONA")[0].replace("###", "").trim());
					}
					if(modelo.getCteNombre().isEmpty() && newcontenido.split("\n")[i].contains("CONTRATANTE:") &&  newcontenido.split("\n")[i+1].contains("DOMICILIO:")) {
						modelo.setCteNombre(newcontenido.split("\n")[i].split("CONTRATANTE:")[1].replace("###", "").trim());
					}
					if(modelo.getCteNombre().isEmpty() && newcontenido.split("\n")[i].contains("CONTRATANTE:") &&  newcontenido.split("\n")[i+3].contains("DOMICILIO:")) {
						modelo.setCteNombre(newcontenido.split("\n")[i].split("CONTRATANTE:")[1].replace("###", "").trim());
					}
					if(newcontenido.split("\n")[i].contains("DOMICILIO:") &&  newcontenido.split("\n")[i].split("DOMICILI").length >20){
			
						modelo.setCteDireccion((newcontenido.split("\n")[i].split("DOMICILIO:")[1] +" "+newcontenido.split("\n")[i+1]).replace("###", "").trim());
						if(!newcontenido.split("\n")[i+2].contains("RFC")){
							modelo.setCteDireccion((modelo.getCteDireccion()+ " "+  newcontenido.split("\n")[i+2].replace("###", "").split("R.F.C:")[0]).trim());
						}
						if(newcontenido.split("\n")[i+1].contains("R.F.C:")){
							modelo.setCteDireccion(modelo.getCteDireccion().split("R.F.C:")[0]);
						}
					}
					if(newcontenido.split("\n")[i].contains("R.F.C:")){
						modelo.setRfc(newcontenido.split("\n")[i].split("R.F.C:")[1].replace("###", "").trim());
					}
					if(newcontenido.split("\n")[i].contains("C.P:")){
						modelo.setCp(newcontenido.split("\n")[i].split("C.P:")[1].replace("###", "").trim());
					}
							
					
				}
			}

			if(modelo.getPlan().length() == 0) {
				if(contenido.contains("ASISTENCIAS QUE COMPONEN LA COBERTURA")) {
					String textoPlan = contenido.split("ASISTENCIAS QUE COMPONEN LA COBERTURA")[1].split("\n")[0].trim();
					if(textoPlan.length()> 0) {
						modelo.setPlan(textoPlan);
					}
				}
			}
	
			inicio = contenido.indexOf("FORMA DE PAGO:");
			fin = contenido.indexOf("PRÁCTICA DE DEPORTE");
			if(fin == -1) {
				fin = contenido.indexOf("ENDOSO DEL FACTOR");
			}
			if(fin == -1) {
				fin = contenido.indexOf("Av.###Revolución");
			}
			
			if(fin < inicio) {
				fin = contenido.lastIndexOf("Av.###Revolución");
			}


			if (inicio > -1 && fin > -1 && inicio < fin) {
				newcontenido = contenido.substring(inicio, fin).replace("@@@", "").replace("\r", "");
				modelo.setFormaPago(fn.formaPagoSring(newcontenido));
				for (int i = 0; i < newcontenido.split("\n").length; i++) {
					if(newcontenido.split("\n")[i].contains("PRIMA NETA:")) {
						modelo.setPrimaneta(fn.castBigDecimal(fn.preparaPrimas(newcontenido.split("\n")[i].split("PRIMA NETA:")[1].replace("###", "").trim())));
					}
					if(newcontenido.split("\n")[i].contains("EXPEDICIÓN")) {
						modelo.setDerecho(fn.castBigDecimal(fn.preparaPrimas(newcontenido.split("\n")[i].split("EXPEDICIÓN:")[1].replace("###", "").trim())));
					}
					if(newcontenido.split("\n")[i].contains("FRACCIONADO") && newcontenido.split("\n")[i].contains("I.V.A.")) {
						modelo.setIva(fn.castBigDecimal(fn.preparaPrimas(newcontenido.split("\n")[i].split("I.V.A.")[1].replace("###", "").trim())));
					}
					if((newcontenido.split("\n")[i].contains("RENOVACIÓN") || newcontenido.split("\n")[i].contains("DIAS")) && newcontenido.split("\n")[i].contains("MONTO") && newcontenido.split("\n")[i].contains("I.V.A.")) {
						modelo.setIva(fn.castBigDecimal(fn.preparaPrimas(newcontenido.split("\n")[i].split("I.V.A.")[1].replace("###", "").trim())));
					}
					if(newcontenido.split("\n")[i].contains("FRACCIONADO:") && newcontenido.split("\n")[i].contains("PRIMA TOTAL:")) {		
						if(newcontenido.split("\n")[i].split("FRACCIONADO:")[1].split("PRIMA")[0].replace("###", "").trim().contains("|")) {						
							modelo.setRecargo(fn.castBigDecimal(fn.preparaPrimas(newcontenido.split("\n")[i].split("FRACCIONADO:")[1].split("PRIMA")[0].replace("|", "###").split("###")[2].trim())));	
						}else {
							modelo.setRecargo(fn.castBigDecimal(fn.preparaPrimas(newcontenido.split("\n")[i].split("FRACCIONADO:")[1].split("PRIMA")[0].replace("###", "").trim())));
						}											
						modelo.setPrimaTotal(fn.castBigDecimal(fn.preparaPrimas(newcontenido.split("\n")[i].split("TOTAL:")[1].replace("###", "").trim())));
					}
					if(newcontenido.split("\n")[i].contains("MONEDA:###")) {
						modelo.setMoneda(fn.moneda(newcontenido.split("\n")[i].split("MONEDA:###")[1].split("###")[0].trim()));
					}
				}
			}
			
			if(modelo.getMoneda() == 0) {
				modelo.setMoneda(1);
			}
			
			 if (contenido.contains("NOMBRE###EDAD###SEXO###EXTRAPRIMA###ANTIGÜEDAD###ANTIGÜEDAD") || contenido.contains("NOMBRE###EDAD###SEXO###EXTRAPRIMA###ANTIGÜEDAD###ANTIGÜEDAD") && contenido.contains("Av.###Revolución")) {
	                newcontenido = contenido.split("NOMBRE###EDAD###SEXO###EXTRAPRIMA###ANTIGÜEDAD###ANTIGÜEDAD")[1].split("Av.###Revolución#")[0];
	                List<EstructuraAseguradosModel> asegurados = new ArrayList<>();
	                String fecha;
	                for (String a : newcontenido.split("\r\n")) {
	                    EstructuraAseguradosModel asegurado = new EstructuraAseguradosModel();
	                    int sp = a.split("###").length;
	        
	                  
	                    if (sp == 6) {
	                
	                    	asegurado.setNombre(a.split("###")[0].replace("@@@", "").trim());
	                    	asegurado.setSexo(fn.sexo(a.split("###")[2]) ? 1:0);
	                    	fecha = a.split("###")[a.split("###").length-1].trim();
	                    	if(fecha.length() == 8) {	                    		
	                              asegurado.setAntiguedad(fecha.substring(4,8) +"-"+fecha.substring(2,4) +"-"+fecha.substring(0,2));		
	                    	}
	                    	asegurados.add(asegurado);
	                       
	                    }
	                    if (sp == 5) {
	                   
	                    	asegurado.setNombre(a.split("###")[0].replace("@@@", "").trim());
	                    	asegurado.setSexo(fn.sexo(a.split("###")[2]) ? 1 : 0);
	                    	fecha = a.split("###")[a.split("###").length-1].trim();
	                    	if(fecha.length() == 8) {	                    		
	                              asegurado.setAntiguedad(fecha.substring(4,8) +"-"+fecha.substring(2,4) +"-"+fecha.substring(0,2));		
	                    	}
	                     
	                        asegurados.add(asegurado);
	                    }
	                }
	                modelo.setAsegurados(asegurados);
	            }

			 if (modelo.getAsegurados().isEmpty()
					&& contenido.contains("RIESGO###NOMBRES###SEXO###EDAD###PARENTESCO###FECHA DE###ANTIGÜEDAD")
					&& contenido.contains("FECHAS###DE###ANTIGÜEDAD")) {
				
				 newcontenido = contenido.split("RIESGO###NOMBRES###SEXO###EDAD###PARENTESCO###FECHA DE###ANTIGÜEDAD")[1].split("FECHAS###DE###ANTIGÜEDAD")[0];

				 String[] arrNewContenido = newcontenido.split("\r\n");
				 StringBuilder nombre;
				 List<EstructuraAseguradosModel> asegurados = new ArrayList<>();
				 
				 for (int i=0; i< arrNewContenido.length;i++) {
	                    EstructuraAseguradosModel asegurado = new EstructuraAseguradosModel();
	                    if(arrNewContenido[i].split("-").length > 3) {
	                    	 int sp = arrNewContenido[i].split("###").length;
	                         if (sp == 7) {	         	  
	                        	nombre = new StringBuilder();
	 	                    	nombre.append(arrNewContenido[i].split("###")[1].replace("@@@", "").trim());
	 	                    	if(!arrNewContenido[i+1].contains("#")) {
	 	                    		nombre.append(" ");
	 	                    		nombre.append(arrNewContenido[i+1].replace("@@@","").replace("\r", "").trim());
	 	                    	}
	 	                    	asegurado.setNombre(nombre.toString());
	 	                    	asegurado.setSexo(fn.sexo(arrNewContenido[i].split("###")[2]) ? 1:0);
	 	                    	asegurado.setParentesco(fn.parentesco(arrNewContenido[i].split("###")[4]));
	 	                    	asegurado.setNacimiento(fn.formatDateMonthCadena(arrNewContenido[i].split("###")[5]));
	 	                    	asegurado.setFechaAlta(fn.formatDateMonthCadena(arrNewContenido[i].split("###")[6].replace("\r", "")));
	 	                    	
	 	                    	asegurados.add(asegurado);
	 	                       
	 	                    }

	                    }
	                    
				 }
				 modelo.setAsegurados(asegurados);
			 }

			if (modelo.getAsegurados().isEmpty() &&contenido.contains("FAMILIA###EMPLEADO###ASEGURADO###PARENTESCO###NACIMIENTO###EDAD###SEXO###F.###ANTIG###F.###INTERN###PRIMA NETA")
		    		&& contenido.contains("DESGLOSE DE POBLACIÓN Y PRIMAS")) {
					obtenerAseguradosPolizaColectiva(contenido);
			}
			
			if(modelo.getAsegurados().isEmpty()) {
				int inicioIndex = contenido.indexOf("ASEGURADO###TITULAR");
				int finIndex = contenido.indexOf("CONCEPTOS###ECONÓMICOS");
				String texto = fn.extracted(inicioIndex, finIndex, contenido);
				List<EstructuraAseguradosModel> asegurados = new ArrayList<>();
				
				if(texto.split("ASEGURADO:").length > 1 ) {
					EstructuraAseguradosModel asegurado = new EstructuraAseguradosModel();
					asegurado.setNombre(texto.split("ASEGURADO:")[1].split("\n")[0].replace("###", "").trim());
					asegurado.setParentesco(1);
					asegurados.add(asegurado);
					modelo.setAsegurados(asegurados);
				}
				
			}
			
			 newcontenido ="";
			 for (int i = 0; i < contenido.split("COBERTURAS Y SERVICIOS").length; i++) {
		
	                if (contenido.split("COBERTURAS Y SERVICIOS")[i].contains("VER ANEXOS")) {
	                    newcontenido += contenido.split("COBERTURAS Y SERVICIOS")[i].split("VER ANEXOS")[0];
	                }
	                if (contenido.split("COBERTURAS Y SERVICIOS")[i].contains("CONCEPTOS###ECONÃ“MICOS")) {
	                    newcontenido += contenido.split("COBERTURAS Y SERVICIOS")[i].split("CONCEPTOS###ECONÃ“MICOS")[0];
	                }
	                if (contenido.split("COBERTURAS Y SERVICIOS")[i].contains("CONCEPTOS###ECONÓMICOS")) {
	                    newcontenido += contenido.split("COBERTURAS Y SERVICIOS")[i].split("CONCEPTOS###ECONÓMICOS")[0];
	                }
	                
	            }
			 
			 if(!contenido.contains("COBERTURAS Y SERVICIOS")) {
				 inicio = contenido.indexOf("COBERTURAS");
				 fin = contenido.indexOf("PLAN");
				 if(inicio > -1 && inicio < fin) {
					 newcontenido = contenido.substring(inicio,fin);
				 }
				 inicio = contenido.indexOf("Asistencia que componen la cobertura");
				 fin = contenido.indexOf("AVISO DE PRIVACIDAD");
				 
				 
	
				 if(inicio > -1 && inicio < fin) {
					 newcontenido += contenido.substring(inicio+36,fin);	 
				 }
				 
				 newcontenido = newcontenido.replace("noches por###hasta 150 Euros por \r\r\nnoche" ,"noches por hasta 150 Euros por noche");
			 }
			 
	            newcontenido = fn.gatos(newcontenido).replace("### ### ### ", "").replace("@@@", "").replace("EndosoElemental", "Elemental")
	            		.replace("EndosoAsistencia", "Asistencia")
	            		.replace("ReducciÃ³n de deducible por", "ReducciÃ³n de deducible por accidente");
	          	            
	            if(newcontenido.contains("R.F.C:") && newcontenido.contains("SEXO") &&  newcontenido.contains("EDAD")) {
	            	newcontenido ="";
	            	inicio = contenido.indexOf("COBERTURAS###SUMA###DEDUCIBLE###COAS");
	            	fin = contenido.indexOf("LAS###ANTERIORES");
	            	if(inicio  > -1 && fin > -1 && inicio < fin ) {
	            		newcontenido = contenido.substring(inicio,fin).replace("@@@COBERTURA", "COBERTURA")
		            			.replace("INCREMENTO DE", "INCREMENTO DE HON-QUIRÚRGICOS")
		            			.replace("ELIM.DE DED.", "ELIM.DE DED. ACCIDENTE Cob. Nal.");		
	            	}	                        	
	            }
	            

	            List<EstructuraCoberturasModel> coberturas = new ArrayList<>();
	            String[] arrContenido = newcontenido.split("\r\n");
	            int index = 0;

	            for (String x : arrContenido) {
	               
	                
	                if (!x.contains("ASEGURADA") && !x.contains("COBERTURAS") && !x.contains("COASEGURO") && !x.contains("a###a") && !x.contains("ContinuaciÃ³n")
	                		&& !x.contains("EXTRANJERO###URO") 	&& !x.contains("Continuación") && !x.contains("Dental") && !x.contains("Visión")
	                		&& !x.contains("Tabulador") && !x.contains("Segurviaje###Suma Asegurada")
	                		) {	                

	                	 EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();
	                	
	                	x = completaTextoCobertura(arrContenido, index);
	                	int sp = x.split("###").length;
						
	                	
	                    if (sp == 7 || sp == 6 || sp ==5) {
	                    	
	                    	if(!x.contains("-") &&  !x.contains("ANTIGÜEDAD") && !x.contains("RECONOCIMIENTO") && !x.contains("ASEGURADOS")) {
	                        cobertura.setNombre(x.split("###")[0].trim());
	                        cobertura.setSa(x.split("###")[1].replace("\r", ""));
	                        cobertura.setDeducible(x.split("###")[2]);
	                        cobertura.setCoaseguro(x.split("###")[3]);
	                        coberturas.add(cobertura);	
	                    	}
	                    }
	                    if (sp == 3 || sp == 2 ) {
	                  
	                    	if(!x.contains("-") && !x.contains("URO")  && !x.contains("ASEGURADOS") && !x.contains("NACIMIENTO") && !x.contains("ANTIGÜEDAD") && !x.contains("ANEXOS")) {
	                            cobertura.setNombre(x.split("###")[0].trim());
		                        cobertura.setSa(x.split("###")[1].replace("\r", ""));
		                        coberturas.add(cobertura);	
	                    	}
	            
	                    }
	                }
	                index++;
	            }
	            modelo.setCoberturas(coberturas);
			
			
	            buildRecibos(modelo);
	            
	            
	            if(modelo.getFormaPago() == 1 && fn.diferenciaDias(modelo.getVigenciaDe(), modelo.getVigenciaA()) < 30  ) {
	                
	                modelo.setVigenciaA(fn.calcvigenciaA(modelo.getVigenciaDe(), 12));
	            }
			
			return modelo;
		} catch (Exception e) {		 
			modelo.setError(MapfreSaludRojoModel.this.getClass().getTypeName()+ " - catch:"+ e.getMessage() + " | "+e.getCause());
			return modelo;
		}
	}
	
	private String completaTextoCobertura(String[] arrTexto, int i) {
		String texto = arrTexto[i];
		if(i+1 < arrTexto.length) {
			if(texto.contains("Prótesis y aparatos")) {
				texto = completaTextoActualConLineaSiguiente(arrTexto, i, "Prótesis y aparatos", "ortopédicos");
			}else if(texto.contains("Tratamientos Reconstructivos")) {
				texto = completaTextoActualConLineaSiguiente(arrTexto, i, "Tratamientos Reconstructivos", "y estéticos");
			}else if(texto.contains("Complicaciones de gastos no")) {
				texto = completaTextoActualConLineaSiguiente(arrTexto, i, "Complicaciones de gastos no", "cubiertos");
			}else if(texto.contains("Padecimientos preexistentes")) {
				texto = completaTextoActualConLineaSiguiente(arrTexto, i, "Padecimientos preexistentes", "no declarados");
				if(!texto.contains("Padecimientos preexistentes no declarados")) {
					texto = completaTextoActualConLineaSiguiente(arrTexto, i, "Padecimientos preexistentes", "declarados");
				}
			}else if(texto.contains("Reducción de deducible por")) {
				texto = completaTextoActualConLineaSiguiente(arrTexto, i, "Reducción de deducible por", "accidente");
			}else if(texto.contains("AYUDA DE")) {
				texto = completaTextoActualConLineaSiguiente(arrTexto, i, "AYUDA DE", "MATERNIDAD");
			}else if(texto.contains("EMERGENCIA EN EL")) {
				texto = completaTextoActualConLineaSiguiente(arrTexto, i, "EMERGENCIA EN EL", "EXTRANJERO");
			}
			else if(texto.contains("INCREMENTO DE")) {
				texto = completaTextoActualConLineaSiguiente(arrTexto, i, "INCREMENTO DE", "HON-QUIRÚRGICOS");
			}
			else if(texto.contains("Retorno anticipado del asegurado por fallecimiento")) {
				texto = completaTextoActualConLineaSiguiente(arrTexto, i, 
				"Retorno anticipado del asegurado por fallecimiento de un familiar", "directo###Incluido");
			}

			else if(texto.contains("Retorno anticipado del asegurado por hospitalización de un familiar")) {
				texto = completaTextoActualConLineaSiguiente(arrTexto, i, 
				"Retorno anticipado del asegurado por hospitalización de un familiar", "directo###Incluido");
			}
			

		}
		
		return texto;
	}
	
	private String completaTextoActualConLineaSiguiente(String[] arrTexto, int i, String textoActual, String textoSiguiente) {
		String texto = arrTexto[i];
		if(!texto.contains(textoSiguiente) && arrTexto[i+1].contains(textoSiguiente)) {
			texto = texto.replace(textoActual, textoActual + " " + textoSiguiente);
			arrTexto[i+1] = arrTexto[i+1].replace(textoSiguiente,"");
		}
		return texto;
	}
	
	private void obtenerAseguradosPolizaColectiva(final String contenido) {
		String newcontenido = contenido.split(
				"FAMILIA###EMPLEADO###ASEGURADO###PARENTESCO###NACIMIENTO###EDAD###SEXO###F.###ANTIG###F.###INTERN###PRIMA NETA")[1]
						.split("DESGLOSE DE POBLACIÓN Y PRIMAS")[0].replace("@@@", "").replace("PUG a","hola");

		String[] arrNewContenido = newcontenido.split("\n");
		List<EstructuraAseguradosModel> asegurados = new ArrayList<>();

		for (int i = 0; i < arrNewContenido.length; i++) {
			EstructuraAseguradosModel asegurado = new EstructuraAseguradosModel();
			if (arrNewContenido[i].split("-").length > 3) {
				String[] aseguradosDetalle= arrNewContenido[i].split("###");
				int sp = aseguradosDetalle.length;

				if (sp == 8 && (i+3)<arrNewContenido.length) {
					StringBuilder nombre = new StringBuilder();
					nombre.append(aseguradosDetalle[1].replace("@@@", "").trim());
					if (arrNewContenido[i + 1].contains("###")) {
						if(arrNewContenido[i+1].split("###").length == 2) {
							nombre.append(" ");
							nombre.append(arrNewContenido[i + 1].split("###")[1].replace("\r", "").trim());
						}
					}
					
					if(!arrNewContenido[i+2].contains("###")) {
						nombre.append(" ").append(arrNewContenido[i+2].replace("\r","").trim());
					}
					
					if(!arrNewContenido[i+3].contains("###")) {
						nombre.append(" ").append(arrNewContenido[i+3].replace("\r","").trim());
					}
					
					asegurado.setNombre(nombre.toString().replace("PUG a", "PUG"));
					asegurado.setParentesco(fn.parentesco(aseguradosDetalle[2]));
					asegurado.setNacimiento(fn.formatDateMonthCadena(aseguradosDetalle[3]));
					asegurado.setSexo(fn.sexo(aseguradosDetalle[5]) ? 1 : 0);
					asegurados.add(asegurado);

				}

			}

		}
		modelo.setAsegurados(asegurados);
	}
	
	private void buildRecibos(EstructuraJsonModel model) {
		if(model.getFormaPago() == 1 ) {
            List<EstructuraRecibosModel> listRecibos = new ArrayList<>();
			EstructuraRecibosModel recibo = new EstructuraRecibosModel();
			recibo.setReciboId("");
			recibo.setSerie("1/1");
			recibo.setVigenciaDe(model.getVigenciaDe());
			recibo.setVigenciaA(model.getVigenciaA());
			if (recibo.getVigenciaDe().length() > 0) {
				recibo.setVencimiento(fn.dateAdd(recibo.getVigenciaDe(), 30, 1));
			}			
			recibo.setPrimaneta(model.getPrimaneta());
			recibo.setDerecho(model.getDerecho());
			recibo.setRecargo(model.getRecargo());
			recibo.setIva(model.getIva());
			recibo.setPrimaTotal(model.getPrimaTotal());
			recibo.setAjusteUno(model.getAjusteUno());
			recibo.setAjusteDos(model.getAjusteDos());
			recibo.setCargoExtra(model.getCargoExtra());
			listRecibos.add(recibo);
			modelo.setRecibos(listRecibos);
		}
	}

}
