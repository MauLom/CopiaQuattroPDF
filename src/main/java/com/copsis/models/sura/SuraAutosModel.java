package com.copsis.models.sura;

import java.util.ArrayList;
import java.util.List;

import com.copsis.constants.ConstantsValue;
import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraCoberturasModel;
import com.copsis.models.EstructuraJsonModel;

public class SuraAutosModel {

	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
	private String contenido;


	public SuraAutosModel(String contenidox) {
		this.contenido = contenidox;
	}

	public EstructuraJsonModel procesar() {
		try {
			
			String newcontenido;
			int inicio;
			int fin;
			contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());
			contenido = contenido.replace("R. F. C:", ConstantsValue.RFC)
			        .replace("Prima Neta###Descuento", ConstantsValue.PRIMANETADESCUETOHASH);
					
			modelo.setTipo(1);
			modelo.setCia(88);
		
			inicio=-1;
			String[] searchTerms = {"Seguro de Automóviles","SEGUROS AUTOS RESIDENTES", "SEGUROS MOTOR TECHNICAL", "Seguro de Movilidad", "Seguro de Auto", "Póliza no."};
			for (String term : searchTerms) {
				inicio = contenido.indexOf(term);
				if (inicio != -1) {		
					break;
				}
			}
			fin = contenido.indexOf(ConstantsValue.COBERTURASCONTRATADAS);
			if(fin < inicio){
				inicio = contenido.indexOf("Ramo");
				fin = contenido.indexOf(ConstantsValue.COBERTURASCONTRATADAS);
		
			}
			
			

			if (inicio > -1 && fin > -1 && inicio < fin) {
				newcontenido = contenido.substring(inicio, fin).replace("@@@", "").replace("\r", "");

				boolean cpEncontrado = false;
				for (int i = 0; i < newcontenido.split("\n").length; i++) {
					if (newcontenido.split("\n")[i].contains("Póliza no.")) {
					    if (newcontenido.split("\n")[i + 1].contains("C.P.")) {
                            modelo.setPolizaGuion(newcontenido.split("\n")[i + 1].split("###")[3]);
                            modelo.setPoliza(newcontenido.split("\n")[i + 1].split("###")[3].replace("-", "")
                                    .replace(" ", "").trim());
                        }
					    
						cpEncontrado = getcp(newcontenido, cpEncontrado, i);
					}
                 
					if (newcontenido.split("\n")[i].contains("Moneda")
							&& newcontenido.split("\n")[i].contains("Emisión")) {
						if (newcontenido.split("\n")[i + 1].contains("NACIONAL")
								|| newcontenido.split("\n")[i + 1].contains("Pesos")) {
							modelo.setMoneda(1);
						}
						int sp = newcontenido.split("\n")[i + 1].split("###").length;
						if (sp == 7) {
							modelo.setFormaPago(fn.formaPago(newcontenido.split("\n")[i + 1].split("###")[6]));
							modelo.setFechaEmision(
									fn.formatDateMonthCadena(newcontenido.split("\n")[i + 1].split("###")[5]));
						}
					}
					if (newcontenido.split("\n")[i].contains(ConstantsValue.MONEDA)){					
						modelo.setMoneda(fn.buscaMonedaEnTexto(newcontenido.split("\n")[i + 1]));

					}
					if (newcontenido.split("\n")[i].contains(ConstantsValue.FORMA_PAGO2)){					
						modelo.setFormaPago(fn.formaPagoSring(newcontenido.split("\n")[i + 1]));

					}
				
					if (newcontenido.split("\n")[i].contains("Vigencia desde")) {
						
						List<String> valores = fn.obtenVigePoliza(newcontenido.split("\n")[i] );
						
						modelo.setVigenciaDe(fn.formatDateMonthCadena(valores.get(0)));

					
					}
					if (newcontenido.split("\n")[i].contains(ConstantsValue.HASTA_LAS)
							&& newcontenido.split("\n")[i].contains("C.P.")) {
				
						modelo.setVigenciaA(
								fn.formatDateMonthCadena(newcontenido.split(ConstantsValue.HASTA_LAS)[1].split("###")[1]));
						modelo.setCp(newcontenido.split("\n")[i].split("C.P.")[1].split("###")[0].trim());
					}else if(newcontenido.split("\n")[i].contains(ConstantsValue.HASTA_LAS)) {
						String texto = fn.gatos(newcontenido.split("\n")[i].split(ConstantsValue.HASTA_LAS)[1]);
						if(texto.split("###")[0].split("-").length == 3) {
							modelo.setVigenciaA(fn.formatDateMonthCadena(texto.split("###")[0].trim()));
						}
					}
					

					if (newcontenido.split("\n")[i].contains(ConstantsValue.RFC)) {
						modelo.setRfc(newcontenido.split("\n")[i].split(ConstantsValue.RFC)[1].split("###")[0]);
					}
					if (newcontenido.split("\n")[i].contains("Datos del contratante") || newcontenido.split("\n")[i].contains("Datos del asegurado")) {

						modelo.setCteNombre(newcontenido.split("\n")[i + 1].split("###")[0].replace("###", "").trim());
						String direccion = newcontenido.split("\n")[i + 2] + " "
								+ newcontenido.split("\n")[i + 3].split("C.P.")[0].split("Hasta")[0].replace("###", "").trim();
						modelo.setCteDireccion(direccion.replace("###", " ").replace("Fecha Forma de pago", ""));
						
						if(newcontenido.split("\n")[i + 4].contains("C.P.")) {
							direccion = modelo.getCteDireccion() +" "+newcontenido.split("\n")[i + 4].replace("C.P.", "C/P");
							modelo.setCteDireccion(direccion.split("C/P")[0].trim().replace("###", " "));
						}
					}
					if(!cpEncontrado && newcontenido.split("\n")[i].contains("C.P")) {
						String texto = newcontenido.split("\n")[i].replace("C.P","C/P").trim();
						if(texto.split("C/P").length>1) {
							texto = fn.elimgatos(texto.split("C/P")[1].trim()).split("###")[0].replace(".", "").replace(":", "").trim();
							if(fn.isNumeric(texto)) {
								modelo.setCp(texto);
								cpEncontrado = true;
							}
						}
					}
					if (newcontenido.split("\n")[i].contains("Descripción")
							&& newcontenido.split("\n")[i].contains("Modelo")) {
						modelo.setDescripcion(newcontenido.split("\n")[i + 1].split("###")[0]);
						modelo.setModelo(fn.castInteger(newcontenido.split("\n")[i + 1].split("###")[1]));
					}

					if (newcontenido.split("\n")[i].contains("Motor") && newcontenido.split("\n")[i].contains("Serie")) {

						int sp = newcontenido.split("\n")[i + 1].split("###").length;
						if (sp == 3) {
							modelo.setSerie(newcontenido.split("\n")[i + 1].split("###")[0]);
						} else if (sp == 4) {
							modelo.setMotor(newcontenido.split("\n")[i + 1].split("###")[0]);
							modelo.setSerie(newcontenido.split("\n")[i + 1].split("###")[1]);
						}	
					}
					  if (newcontenido.split("\n")[i].contains("Cve. Vehículo:")) {
						if(newcontenido.split("\n")[i].split(fn.palabraRgx(newcontenido.split("\n")[i], ConstantsValue.VEHICULO2))[1].trim().split("###").length > 1){
                            modelo.setClave(newcontenido.split("\n")[i].split(fn.palabraRgx(newcontenido.split("\n")[i], ConstantsValue.VEHICULO2))[1].split("###")[0].trim());
						}else{
							 modelo.setClave(newcontenido.split("\n")[i].split(fn.palabraRgx(newcontenido.split("\n")[i], ConstantsValue.VEHICULO2))[1].trim());
						}
                          
                      }
					if (newcontenido.split("\n")[i].contains("Cve") && modelo.getClave().length() == 0 ) {
						 modelo.setClave(newcontenido.split("\n")[i].split(fn.palabraRgx(newcontenido.split("\n")[i], ConstantsValue.VEHICULO2))[1].split("###")[0].trim());
					}
					 
				}
			}
			
			if(modelo.getCp().length() == 4) {
				modelo.setCp("0"+modelo.getCp());
			}
			if(!modelo.getVigenciaDe().isEmpty()){
            modelo.setFechaEmision(modelo.getVigenciaDe());
			}
			

			inicio = contenido.indexOf(ConstantsValue.PRIMANETADESCUETOHASH);
			fin = contenido.indexOf("Pág. 1");
			fin = inicio > fin ?  contenido.lastIndexOf("Pág. 1"):fin;
			fin = inicio > fin ?  contenido.lastIndexOf("Pag. 1 de 3"):fin;

           
			if (inicio > -1 && fin > -1 && inicio < fin) {
				newcontenido = contenido.substring(inicio, fin).replace("@@@", "").replace("\r", "");
				for (int i = 0; i < newcontenido.split("\n").length; i++) {			
					
					if(newcontenido.split("\n")[i].contains(ConstantsValue.PRIMA_NETA2)) {
						int sp = newcontenido.split("\n")[i+1].split("###").length;
						if(sp== 6) {
                            modelo.setPrimaneta(fn.castBigDecimal(fn.preparaPrimas( newcontenido.split("\n")[i+1].split("###")[0])));
                            modelo.setRecargo(fn.castBigDecimal(fn.preparaPrimas( newcontenido.split("\n")[i+1].split("###")[2])));
                            modelo.setDerecho(fn.castBigDecimal(fn.preparaPrimas( newcontenido.split("\n")[i+1].split("###")[3])));
                            modelo.setIva(fn.castBigDecimal(fn.preparaPrimas( newcontenido.split("\n")[i+1].split("###")[4])));
                            modelo.setPrimaTotal(fn.castBigDecimal(fn.preparaPrimas( newcontenido.split("\n")[i+1].split("###")[5])));
						}
					}
					
				}
				
			}

			if(modelo.getPrimaneta().intValue() == 0){
			
					inicio = contenido.lastIndexOf(ConstantsValue.PRIMANETADESCUETOHASH);
				     fin = contenido.lastIndexOf("Pág. 1 de 4");
					 if(inicio >  -1&& fin >  -1){
						newcontenido = contenido.substring(inicio, fin).replace("@@@", "").replace("\r", "");
						for (int i = 0; i < newcontenido.split("\n").length; i++) {				
						   if(newcontenido.split("\n")[i].contains(ConstantsValue.PRIMA_NETA2)) {
							   int sp = newcontenido.split("\n")[i+1].split("###").length;
							   if(sp== 6) {
								   modelo.setPrimaneta(fn.castBigDecimal(fn.preparaPrimas( newcontenido.split("\n")[i+1].split("###")[0])));
								   modelo.setRecargo(fn.castBigDecimal(fn.preparaPrimas( newcontenido.split("\n")[i+1].split("###")[2])));
								   modelo.setDerecho(fn.castBigDecimal(fn.preparaPrimas( newcontenido.split("\n")[i+1].split("###")[3])));
								   modelo.setIva(fn.castBigDecimal(fn.preparaPrimas( newcontenido.split("\n")[i+1].split("###")[4])));
								   modelo.setPrimaTotal(fn.castBigDecimal(fn.preparaPrimas( newcontenido.split("\n")[i+1].split("###")[5])));
							   }
						   }
					   }
				  }									
			}

			
			inicio = contenido.indexOf(ConstantsValue.COBERTURASCONTRATADAS);
			fin = contenido.indexOf(ConstantsValue.PRIMANETADESCUETOHASH);

			int	in = contenido.lastIndexOf(ConstantsValue.COBERTURASCONTRATADAS);
			int fi = contenido.lastIndexOf(ConstantsValue.PRIMANETADESCUETOHASH);
			String xcon = "";
			if (in > -1 && fi > -1 && in < fi) {
				xcon=	contenido.substring(in, fi).replace("@@@", "").replace("\r", "");
			}

			if (inicio > -1 && fin > -1 && inicio < fin) {
				List<EstructuraCoberturasModel> coberturas = new ArrayList<>();
				newcontenido = contenido.substring(inicio, fin).replace("@@@", "").replace("\r", "")+"" +xcon;
				for (int i = 0; i < newcontenido.split("\n").length; i++) {
					EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();
					if(!newcontenido.split("\n")[i].contains("contratadas") || !newcontenido.split("\n")[i].contains(ConstantsValue.PRIMA_NETA2)) {
						int sp = newcontenido.split("\n")[i].split("###").length;
					
						if(sp == 3 ) {
							cobertura.setNombre(newcontenido.split("\n")[i].split("###")[0]);
							cobertura.setSa(newcontenido.split("\n")[i].split("###")[1]);	
							coberturas.add(cobertura);
						}else if( sp == 4) {
							cobertura.setNombre(newcontenido.split("\n")[i].split("###")[0]);
							cobertura.setSa(newcontenido.split("\n")[i].split("###")[1]);
							cobertura.setDeducible(newcontenido.split("\n")[i].split("###")[3]);
							coberturas.add(cobertura);
						}						
					}
				}
				modelo.setCoberturas(coberturas);
			}
			
			obtenerDatosAgente(contenido, modelo);
			return modelo;
		} catch (Exception e) {
			e.printStackTrace();
			modelo.setError(SuraAutosModel.this.getClass().getTypeName() + " | " + e.getMessage() + " | "
					+ e.getCause());
			return modelo;
		}
	}

	private boolean getcp(String newcontenido, boolean cpEncontrado, int i) {
		if (newcontenido.split("\n")[i + 2].contains("C.P.")) {
			modelo.setPolizaGuion(newcontenido.split("\n")[i + 2].split("###")[3]);
			modelo.setPoliza(newcontenido.split("\n")[i + 2].split("###")[3].replace("-", "")
					.replace(" ", "").trim());
					 cpEncontrado = true;
		}
		if (newcontenido.split("\n")[i + 1].contains("C.P.") && !cpEncontrado) {
		    modelo.setCp(newcontenido.split("\n")[i + 1].split("C.P.")[1].split("###")[0].substring(0, 5).trim());
		    cpEncontrado = true;
		}
		if (newcontenido.split("\n")[i + 1].contains("C.P.") && !cpEncontrado) {
			modelo.setCp(newcontenido.split("\n")[i + 2].split("C.P.")[1].split("###")[0].trim());
			cpEncontrado = true;
		}
		return cpEncontrado;
	}
	
	private void obtenerDatosAgente(String texto, EstructuraJsonModel model) {
		
		if(texto.split("Agente:").length > 1) {
   
			model.setCveAgente(texto.split("Agente:")[1].split("\n")[0].trim());
		}
		
		int inicioIndex = texto.indexOf("Nombre Agente");
		int finIndex = texto.indexOf("Datos del Asegurado");
		String newContenido = fn.extracted(inicioIndex, finIndex, texto);
		if(newContenido.length() > 0 && model.getCveAgente().length() > 0) {
			String[] arrContenido = newContenido.split("\n");
			String agente = "";
			
			if(arrContenido[1].split("###")[0].contains(model.getCveAgente())) {
				agente = arrContenido[1].split(model.getCveAgente())[1];
				agente = fn.elimgatos(agente.trim()).split("###")[0];
				model.setAgente(agente.trim());
			}else if(arrContenido[1].contains("Agente###") && arrContenido.length>3) {
				agente = arrContenido[2].split("###")[0].trim();
				
				if(agente.contains("Y DE FIANZAS, S.") && (arrContenido[3].contains("A. DE C.V.") || arrContenido[4].contains("A. DE C.V."))) {
					model.setAgente(agente + " A. DE C.V.");
				}
				
			}
		}
				
	}

}
