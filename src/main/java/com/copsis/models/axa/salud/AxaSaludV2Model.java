package com.copsis.models.axa.salud;

import java.util.ArrayList;
import java.util.List;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraAseguradosModel;
import com.copsis.models.EstructuraCoberturasModel;
import com.copsis.models.EstructuraJsonModel;
import com.copsis.models.EstructuraRecibosModel;

public class AxaSaludV2Model {
	// Clases
	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
	// Varaibles
	public String inicontenido = "";
	private String contenido = "";
	public String newcontenido = "";
	private String resultado = "";
	private int inicio = 0;
	private int fin = 0;
	private boolean cp = false;
	private boolean vigenciaD = false;
	


	public AxaSaludV2Model(String contenido) {
		this.contenido = contenido;
	}
	
	public EstructuraJsonModel procesar() {
		inicontenido = fn.fixContenido(contenido);
		contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());
	try {
		modelo.setTipo(3);
		modelo.setCia(20);
		

		inicio = contenido.indexOf("Datos###del###contratante###Póliza");
		if(inicio == -1) {
			inicio = contenido.indexOf("Datos del contratante###Póliza");
		}
		fin = contenido.indexOf("Coberturas-Servicios");
		

		if (inicio > 0 && fin > 0 && inicio < fin) {
			newcontenido = contenido.substring(inicio, fin);
			for (int i = 0; i < newcontenido.split("\n").length; i++) {
				if(newcontenido.split("\n")[i].contains("contratante") || newcontenido.split("\n")[i].contains("Póliza")) {
					if( newcontenido.split("\n")[i+1].contains("Nombre")) {					
						String contratante= newcontenido.split("\n")[i+1].split("Nombre")[1].split("###")[newcontenido.split("\n")[i+1].split("###").length-2].replace(":", "");	
						if(contratante.contains(",")) {
							modelo.setCteNombre((contratante.split(",")[1] +" " + contratante.split(",")[0]).trim());
						}else {
							modelo.setCteNombre(contratante);
						}										
					}
					modelo.setPoliza(newcontenido.split("\n")[i+1].split("Nombre")[1].split("###")[newcontenido.split("\n")[i+1].split("###").length-1].replace("\r", ""));			
				}
				if (newcontenido.split("\n")[i].contains("plan") && newcontenido.split("\n")[i].contains("SolicitudDomicilio")) {
					modelo.setPlan(newcontenido.split("\n")[i ].split("SolicitudDomicilio")[1].split("###")[newcontenido.split("\n")[i ].split("SolicitudDomicilio")[1].split("###").length -3] );
				}else if (newcontenido.split("\n")[i].contains("plan") && newcontenido.split("\n")[i].contains("Solicitud")) {

					if(newcontenido.split("\n")[i + 1].split("###").length == 2) {
						modelo.setPlan(newcontenido.split("\n")[i + 1].split("###")[0].replace("@@@", ""));
					}else {
						modelo.setPlan(newcontenido.split("\n")[i + 1].split("###")[1].replace("@@@", ""));
					}
			
				}
				else if (newcontenido.split("\n")[i].contains("plan")) {
					modelo.setPlan(newcontenido.split("\n")[i + 1].split("###")[0].replace("@@@", ""));
				}

			
				if (newcontenido.split("\n")[i].contains("C.P.") && newcontenido.split("\n")[i].contains("Fecha") &&  newcontenido.split("\n")[i].contains("vigencia")) {//C.P./fecha/vigencia
				
					modelo.setCp(newcontenido.split("\n")[i].split("C.P.")[1].split("Ciudad")[0].replace("###", "").trim());
					   if(modelo.getCp().length() > 5) {
						      int sp = newcontenido.split("\n")[i].split("C.P.")[1].split("###").length;
						      if(sp > 0) {
						    	  modelo.setCp(newcontenido.split("\n")[i].split("C.P.")[1].split("###")[0]);
						      }
						    }
					
					resultado = newcontenido.split("\n")[i].split("Fecha")[0].replace(modelo.getCp(), "")
							.replace("C.P", "").replace("###", " ".replace("\r", ""));
					
					//vigencias
					modelo.setVigenciaDe(fn.formatDateMonthCadena(newcontenido.split("\n")[i].split("vigencia")[1].replace("###", "").replace("\r", "")));
                   cp=true;
                   vigenciaD = true;
				}	else if (newcontenido.split("\n")[i].contains("C.P.") && newcontenido.split("\n")[i].contains("vigencia")){
					modelo.setVigenciaDe(fn.formatDateMonthCadena(newcontenido.split("\n")[i].split("vigencia")[1].replace("###", "").trim().replace("\r", "")));
					vigenciaD = true;
				}
				
				if (newcontenido.split("\n")[i].contains("C.P.") && cp == false) {
					
					modelo.setCp(newcontenido.split("\n")[i].split("C.P.")[1].split("Ciudad")[0].replace("###", "").trim());
				    if(modelo.getCp().length() > 5) {
				      int sp = newcontenido.split("\n")[i].split("C.P.")[1].split("###").length;
				      if(sp > 0) {
				    	  modelo.setCp(newcontenido.split("\n")[i].split("C.P.")[1].split("###")[0]);
				      }
				    }
					
				}
				if(newcontenido.split("\n")[i].contains("inicio de vigencia") && vigenciaD == false){
					modelo.setVigenciaDe(fn.formatDateMonthCadena(newcontenido.split("\n")[i].split("inicio de vigencia")[1].replace("###", "").trim().replace("\r", "")));					
				}
				
				
				
				if (newcontenido.split("\n")[i].contains("R.F.C.") && newcontenido.split("\n")[i].contains("vigencia")){
					modelo.setVigenciaA(fn.formatDateMonthCadena(newcontenido.split("\n")[i].split("vigencia")[1].replace("###", "").trim().replace("\r", "")));					
				}
				else if (newcontenido.split("\n")[i].contains("Fecha de fin de vigencia") ){
			
					modelo.setVigenciaA(fn.formatDateMonthCadena(newcontenido.split("\n")[i].split("Fecha de fin de vigencia")[1].replace("###", "").trim().replace("\r", "")));					
				}
				
				if (newcontenido.split("\n")[i].contains("emisión")){
					modelo.setFechaEmision(fn.formatDateMonthCadena(newcontenido.split("\n")[i].split("emisión")[1].replace("###", "").trim().replace("\r", "")));					
				}				
				if (newcontenido.split("\n")[i].contains("Domicilio")) {
					modelo.setCteDireccion(newcontenido.split("\n")[i].split("Domicilio")[1].replaceAll("###", "")
							.replace(":", "").replace(",", "").replace("\r", "") + " " + resultado);
				}				
				if (newcontenido.split("\n")[i].contains("Agente:")){
					modelo.setCveAgente(newcontenido.split("\n")[i].split("Agente:")[1].split("###")[1]);
					if(newcontenido.split("\n")[i+1].contains("Periodo")) {
						modelo.setAgente((newcontenido.split("\n")[i].split(modelo.getCveAgente())[1].replace("###", " ") +" " +newcontenido.split("\n")[i+1].split("Periodo")[0].trim()).trim().replace("@@@", "").replace("\r", "").replace("###", ""));
					}else {
						modelo.setAgente((newcontenido.split("\n")[i].split(modelo.getCveAgente())[1].split("Periodo")[0].replace("###", " ") ).replace("\r", ""));
					}					
				}
				if (newcontenido.split("\n")[i].contains("pago") && newcontenido.split("\n")[i].contains("Frecuencia")){					
					modelo.setFormaPago(fn.formaPago(newcontenido.split("\n")[i].split("pago")[1].replace("###", "").replace("\r", "").trim()));
				}
				
			}
		}

		modelo.setMoneda(1);
		inicio = contenido.indexOf("Prima###Neta");
		if(inicio ==  -1) {
			inicio = contenido.indexOf("Prima Neta");
		}
		fin = contenido.indexOf("HOJA 1 ");
		if (inicio > 0 && fin > 0 && inicio < fin) {
			newcontenido = contenido.substring(inicio, fin);
			for (int i = 0; i < newcontenido.split("\n").length; i++) {
				if(newcontenido.split("\n")[i].contains("Neta")) {
					modelo.setPrimaneta(fn.castBigDecimal(fn.castDouble( newcontenido.split("\n")[i].split("Neta")[1].replace(",","").replace("###", ""))));
					
				}
				if(newcontenido.split("\n")[i].contains("Recargo")) {
					modelo.setRecargo(fn.castBigDecimal(fn.castDouble( newcontenido.split("\n")[i].split("fraccionado")[1].replace("###", ""))));
					
				}
	
				if(newcontenido.split("\n")[i].contains("Derecho")) {
					modelo.setDerecho(fn.castBigDecimal(fn.castDouble( newcontenido.split("\n")[i].split("póliza")[1].replace("###", ""))));
					
				}
				
				if(newcontenido.split("\n")[i].contains("I.V.A.")) {
					modelo.setIva(fn.castBigDecimal(fn.castDouble( newcontenido.split("\n")[i].split("I.V.A.")[1].replace("###", ""))));
					
				}
				if(newcontenido.split("\n")[i].contains("Prima") &&  newcontenido.split("\n")[i].contains("total")) {
					modelo.setPrimaTotal(fn.castBigDecimal(fn.castDouble( newcontenido.split("\n")[i].split("total")[1].replace("###", ""))));
				}
			}
		}
		
		//Sa,deducibel,coaseguro

		inicio = contenido.indexOf("Condiciones Contratadas");		
		fin = contenido.indexOf("Coberturas-Servicios ");
		if (inicio > 0 && fin > 0 && inicio < fin) {
			newcontenido = contenido.substring(inicio, fin);
			for (int i = 0; i < newcontenido.split("\n").length; i++) {
				if(newcontenido.split("\n")[i].contains("SumaAsegurada")) {
					modelo.setSa(newcontenido.split("\n")[i].split("SumaAsegurada")[1].replace("###", "").replace("\r", ""));
				}
				if(newcontenido.split("\n")[i].contains("Deducible")) {
					modelo.setDeducible(newcontenido.split("\n")[i].split("Deducible")[1].replace("###", "").replace("\r", ""));
				}
				if(newcontenido.split("\n")[i].contains("Coaseguro")) {
					modelo.setCoaseguro(newcontenido.split("\n")[i].split("Coaseguro")[1].replace("###", "").replace("\r", ""));
				}
			}
		}
		
		
		
		// proceso Asegurados		
		List<EstructuraAseguradosModel> asegurados = new ArrayList<>();
		inicio = contenido.indexOf("Relación###de###Asegurados");		
		fin = contenido.indexOf("AXA###Seguros,###S.A.");
		if (inicio > 0 && fin > 0 && inicio < fin) {
			newcontenido = contenido.substring(inicio, fin);
			for (int i = 0; i < newcontenido.split("\n").length; i++) {
				EstructuraAseguradosModel asegurado = new EstructuraAseguradosModel();
				if (newcontenido.split("\n")[i].split("-").length > 5) {
					String x = newcontenido.split("\n")[i].split("###")[0].replace("@@@", "").trim();
					asegurado.setNombre(x.split(",")[1] +" "+ x.split(",")[0]);
					asegurado.setNacimiento(fn.formatDateMonthCadena(newcontenido.split("\n")[i].split("###")[4]));
					asegurado.setAntiguedad(fn.formatDateMonthCadena(newcontenido.split("\n")[i].split("###")[5]));
					asegurado.setParentesco(fn.parentesco(newcontenido.split("\n")[i].split("###")[3]));
					asegurado.setSexo(fn.sexo(newcontenido.split("\n")[i].split("###")[3]) ? 1:0);
					asegurados.add(asegurado);
				}
			
			}
			modelo.setAsegurados(asegurados);
		}
		
		if (modelo.getAsegurados().size() == 0) {
			String nombre ="";
			String fecha_n ="";
			String fecha_a ="";
		
			
			inicio = inicontenido.indexOf("Relación de Asegurados");
			fin = inicontenido.indexOf("AXA Seguros, S.A. de C.V.");
			if (inicio > 0 && fin > 0 && inicio < fin) {
				newcontenido = inicontenido.substring(inicio, fin).replace("T   it u  l ar", "Titular")
						.replace("C   ó  n  y  uge", "Conyuge").replace("H   i j o", "Hijo").replaceAll("  +", "###").replace("/","-")
						.replace("######", "###");
				for (int i = 0; i < newcontenido.split("\n").length; i++) {
					EstructuraAseguradosModel asegurado = new EstructuraAseguradosModel();
					int x = newcontenido.split("\n")[i].split("###").length;
					
					if (newcontenido.split("\n")[i].split("-").length > 5) {
						if(x == 13) {
							nombre = newcontenido.split("\n")[i].split("###")[0].replace("@@@", "").trim();
							asegurado.setNombre(nombre.split(",")[1] +" "+ newcontenido.split("\n")[i+1].split("###")[0] +" "+ nombre.split(",")[0]);
							fecha_n =newcontenido.split("\n")[i].split("###")[5] +""+newcontenido.split("\n")[i].split("###")[6].replace(" ", "");
							asegurado.setNacimiento(fn.formatDateMonthCadena(fecha_n));
							fecha_a =newcontenido.split("\n")[i].split("###")[7].replace(" ", "");														
							asegurado.setAntiguedad(fn.formatDateMonthCadena(fecha_a));
							asegurado.setParentesco(fn.parentesco(newcontenido.split("\n")[i].split("###")[4]));
							asegurado.setSexo(fn.sexo(newcontenido.split("\n")[i].split("###")[1]) ? 1:0);
							asegurados.add(asegurado);
						}
						if(x == 12) {
							nombre = newcontenido.split("\n")[i].split("###")[0].replace("@@@", "").trim();
							asegurado.setNombre(nombre.split(",")[1]  +" "+ nombre.split(",")[0]);
				
							fecha_n =newcontenido.split("\n")[i].split("###")[4] +""+newcontenido.split("\n")[i].split("###")[5].replace(" ", "");
							asegurado.setNacimiento(fn.formatDateMonthCadena(fecha_n));
							fecha_a =newcontenido.split("\n")[i].split("###")[6].replace(" ", "");														
							asegurado.setAntiguedad(fn.formatDateMonthCadena(fecha_a));
							asegurado.setParentesco(fn.parentesco(newcontenido.split("\n")[i].split("###")[3]));
							asegurado.setSexo(fn.sexo(newcontenido.split("\n")[i].split("###")[1]) ? 1:0);
							asegurados.add(asegurado);
						}
						if(x == 14) {						
							nombre = newcontenido.split("\n")[i].split("###")[0].replace("@@@", "").trim();
							asegurado.setNombre(nombre.split(",")[1] +" "+ nombre.split(",")[0]);
							fecha_n = newcontenido.split("\n")[i].split("###")[5]+""+newcontenido.split("\n")[i].split("###")[6] +""+ newcontenido.split("\n")[i].split("###")[7].replace(" ", "");			
							asegurado.setNacimiento(fn.formatDateMonthCadena(fecha_n));
							fecha_a =newcontenido.split("\n")[i].split("###")[8].replace(" ", "");														
							asegurado.setAntiguedad(fn.formatDateMonthCadena(fecha_a));
							asegurado.setParentesco(fn.parentesco(newcontenido.split("\n")[i].split("###")[4]));
							asegurado.setSexo(fn.sexo(newcontenido.split("\n")[i].split("###")[1]) ? 1:0);
							asegurados.add(asegurado);
						}
						
						if(x == 15) {						
							nombre = newcontenido.split("\n")[i].replace("A ###", "A").split("###")[0].replace("@@@", "").trim();										
						    asegurado.setNombre((nombre.split(",")[1] +" "+ nombre.split(",")[0]).trim());
							fecha_n = newcontenido.split("\n")[i].split("###")[5]+""+newcontenido.split("\n")[i].split("###")[6] +""+ newcontenido.split("\n")[i].split("###")[7].replace(" ", "");										
							asegurado.setNacimiento(fn.formatDateMonthCadena(fecha_n));
							fecha_a =newcontenido.split("\n")[i].split("###")[8].replace(" ", "");														
							asegurado.setAntiguedad(fn.formatDateMonthCadena(fecha_a));
							asegurado.setParentesco(fn.parentesco(newcontenido.split("\n")[i].split("###")[4]));
							if(newcontenido.split("\n")[i].split("###")[1].length() >  3) {
								asegurado.setSexo(fn.sexo(newcontenido.split("\n")[i].split("###")[2]) ? 1:0);
							}else {
								asegurado.setSexo(fn.sexo(newcontenido.split("\n")[i].split("###")[1]) ? 1:0);	
							}
							
							asegurados.add(asegurado);
						}
					}
				
				}
				modelo.setAsegurados(asegurados);
			}
		}
		
		
		//proceso de coberturas
		
		List<EstructuraCoberturasModel> coberturas = new ArrayList<>();


		inicio = contenido.indexOf("Coberturas-Servicios");
		fin = contenido.indexOf("Costo por Servicio");


		if (inicio > 0 && fin > 0 && inicio < fin) {
			newcontenido = contenido.substring(inicio, fin).replace("###D ###e acuerdo", "###De acuerdo".replace("\r", ""));
			for (int i = 0; i < newcontenido.split("\n").length; i++) {
				EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();
				if(newcontenido.split("\n")[i].contains("Gama") || newcontenido.split("\n")[i].contains("Red") || newcontenido.split("\n")[i].contains("adicionales") || newcontenido.split("\n")[i].contains("Deducible") ) {					
				}else {
					
					int x = newcontenido.split("\n")[i].split("###").length;

					if(x == 3) {
						cobertura.setNombre(newcontenido.split("\n")[i].split("###")[0].replace("@@@", ""));
						cobertura.setSa(newcontenido.split("\n")[i].split("###")[1].replace("@@@", ""));
						cobertura.setDeducible(newcontenido.split("\n")[i].split("###")[2].replace("@@@", ""));

						coberturas.add(cobertura);
					}
					if(x == 4) {
						cobertura.setNombre(newcontenido.split("\n")[i].split("###")[0].replace("@@@", ""));
						cobertura.setSa(newcontenido.split("\n")[i].split("###")[1].replace("@@@", ""));
						cobertura.setDeducible(newcontenido.split("\n")[i].split("###")[2].replace("@@@", ""));
						cobertura.setCoaseguro(newcontenido.split("\n")[i].split("###")[3].replace("@@@", "").replace("\r", ""));
						coberturas.add(cobertura);
					}
					
					if(x == 4) {
						cobertura.setNombre(newcontenido.split("\n")[i].split("###")[0].replace("@@@", ""));
						cobertura.setSa(newcontenido.split("\n")[i].split("###")[1].replace("@@@", ""));
						cobertura.setDeducible(newcontenido.split("\n")[i].split("###")[2].replace("@@@", ""));
						cobertura.setCoaseguro(newcontenido.split("\n")[i].split("###")[3].replace("@@@", "").replace("\r", ""));
						coberturas.add(cobertura);
					}
					if(x == 7) {					
						cobertura.setNombre(newcontenido.split("\n")[i].split("###")[0].replace("@@@", ""));
						cobertura.setSa(newcontenido.split("\n")[i].split("###")[1].replace("@@@", ""));
						cobertura.setDeducible(newcontenido.split("\n")[i].split("###")[2].replace("@@@", ""));
						cobertura.setCoaseguro(newcontenido.split("\n")[i].split("###")[3].replace("@@@", ""));
						coberturas.add(cobertura);
					}
					if(x == 8) {
						cobertura.setNombre(newcontenido.split("\n")[i].split("###")[0].replace("@@@", ""));
						cobertura.setSa(newcontenido.split("\n")[i].split("###")[1].replace("@@@", ""));
						cobertura.setDeducible(newcontenido.split("\n")[i].split("###")[2].replace("@@@", ""));
						cobertura.setCoaseguro(newcontenido.split("\n")[i].split("###")[3].replace("@@@", ""));
						coberturas.add(cobertura);
					}
				}
				
			}
			modelo.setCoberturas(coberturas);
		}

		
		//proceso 2 para coberturas
		
		if(modelo.getCoberturas().size() == 0) {//version dos de coberturas si formato (### no tiene)
			inicio = inicontenido.indexOf("Coberturas/Servicios");
			fin = inicontenido.indexOf("Costo por Servicio");

					if (inicio > 0 && fin > 0 && inicio < fin) {
						newcontenido = inicontenido.substring(inicio,fin).replaceAll("  +", "###").replace("@@@", "").replace("Tipo de Red###Abierta", "").replace("###Gama Hospitalaria###Esmeralda", "");
						for (int i = 0; i < newcontenido.split("\n").length; i++) {
							EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();

							if(newcontenido.split("\n")[i].contains("Servicios Cobertura Básica") || newcontenido.split("\n")[i].contains("Tope de Coaseguro")
							 || newcontenido.split("\n")[i].contains("Tabulador") || newcontenido.split("\n")[i].contains("adicionales")
							  || newcontenido.split("\n")[i].contains("Servicios")
							  ) {} else {
								  int x = newcontenido.split("\n")[i].split("###").length;
	

								  if(newcontenido.split("\n")[i].contains("%")) {
									
										if (x == 5) {
											cobertura.setNombre(newcontenido.split("\n")[i].split("###")[0]);
											cobertura.setSa(newcontenido.split("\n")[i].split("###")[1]);
											cobertura.setDeducible(newcontenido.split("\n")[i].split("###")[2]);
											cobertura.setCoaseguro(newcontenido.split("\n")[i].split("###")[3]);
											coberturas.add(cobertura);
										}
										if (x == 6) {
											cobertura.setNombre(newcontenido.split("\n")[i].split("###")[0]);
											cobertura.setSa(newcontenido.split("\n")[i].split("###")[1]);
											cobertura.setDeducible(newcontenido.split("\n")[i].split("###")[3]);
											cobertura.setCoaseguro(newcontenido.split("\n")[i].split("###")[4]);
											coberturas.add(cobertura);
										}								  									 
								  }else {
										if (x == 5) {
											cobertura.setNombre(newcontenido.split("\n")[i].split("###")[0]);
											cobertura.setSa(newcontenido.split("\n")[i].split("###")[1]);
											cobertura.setDeducible(newcontenido.split("\n")[i].split("###")[2]);
											cobertura.setCoaseguro(newcontenido.split("\n")[i].split("###")[3]);
											coberturas.add(cobertura);
										}
									  if(x == 4) {
											cobertura.setNombre(newcontenido.split("\n")[i].split("###")[0].replace("@@@", ""));
											cobertura.setSa(newcontenido.split("\n")[i].split("###")[1].replace("@@@", ""));
											cobertura.setDeducible(newcontenido.split("\n")[i].split("###")[2].replace("@@@", ""));
											cobertura.setCoaseguro(newcontenido.split("\n")[i].split("###")[3].replace("@@@", "").replace("\r", ""));
											coberturas.add(cobertura);
									  }
									 
									
								  }								  								
							  }						
						}						
						modelo.setCoberturas(coberturas);
					}					
		}
		
		
		
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
			recibo.setPrimaneta(modelo.getPrimaneta());
			recibo.setDerecho(modelo.getDerecho());
			recibo.setRecargo(modelo.getRecargo());
			recibo.setIva(modelo.getDerecho());

			recibo.setPrimaTotal(modelo.getPrimaTotal());
			recibo.setAjusteUno(modelo.getAjusteUno());
			recibo.setAjusteDos(modelo.getAjusteDos());
			recibo.setCargoExtra(modelo.getCargoExtra());
			recibos.add(recibo);

			break;
		}
		modelo.setRecibos(recibos);
		
		
		
		
		
		return modelo;
	} catch (Exception ex) {
		modelo.setError(
				AxaSaludV2Model.this.getClass().getTypeName() + " | " + ex.getMessage() + " | " + ex.getCause());
		return modelo;
	}
	}

}
