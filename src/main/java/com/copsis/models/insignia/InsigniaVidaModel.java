package com.copsis.models.insignia;

import java.util.ArrayList;
import java.util.List;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraCoberturasModel;
import com.copsis.models.EstructuraJsonModel;

public class InsigniaVidaModel {
	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
	private String contenido = "";
	
    public  InsigniaVidaModel(String contenido){
    	this.contenido = contenido;
    }
    
    public EstructuraJsonModel procesar() {
    	int inicio = 0;
		int fin = 0;
		List<String> arrayvigencias = new ArrayList<>();
		StringBuilder newcontenido = new StringBuilder();
		contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());
		
		try {
			

			modelo.setTipo(5);
			modelo.setCia(77);
			
			inicio = contenido.indexOf("PÓLIZA DE SEGURO");
			fin  = contenido.indexOf("INFORMACIÓN DEL ASEGURADO");	
			newcontenido = new StringBuilder();
			newcontenido.append(fn.extracted(inicio, fin, contenido));
			
			for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
				modelo.setFormaPago(fn.formaPagoSring(newcontenido.toString().split("\n")[i]));
				if(newcontenido.toString().split("\n")[i].contains("Póliza:")) {
					modelo.setPoliza(newcontenido.toString().split("\n")[i].split("Póliza:")[1].replace("###", ""));
				}
				
				if(newcontenido.toString().split("\n")[i].contains("Forma de pago") && newcontenido.toString().split("\n")[i].contains("Moneda")) {
					modelo.setFormaPago(fn.formaPagoSring(newcontenido.toString().split("\n")[i+1]));
					modelo.setMoneda(fn.buscaMonedaEnTexto(newcontenido.toString().split("\n")[i+1]));
				}
				if(newcontenido.toString().split("\n")[i].contains("CONTRATANTE") && newcontenido.toString().split("\n")[i+1].contains("RFC")) {
					modelo.setCteNombre(newcontenido.toString().split("\n")[i+1].split("RFC")[0].replace("###", "").trim());
					modelo.setRfc(newcontenido.toString().split("\n")[i+1].split("RFC")[1].replace("###", "").trim());
				 modelo.setCteDireccion(newcontenido.toString().split("\n")[i+2].replace("###", "").trim()
						 +" " + newcontenido.toString().split("\n")[i+3].replace("###", " ").trim());
				}				
				if(newcontenido.toString().split("\n")[i].contains("C.P.")) {				
					modelo.setCp(newcontenido.toString().split("\n")[i].split("C.P.")[1].replace("###", "").trim());
				}				
				if(newcontenido.toString().split("\n")[i].split("-").length > 3) {
					arrayvigencias = fn.obtenVigePoliza( newcontenido.toString().split("\n")[i]);					
					if(arrayvigencias.size() == 1 ) {
						modelo.setVigenciaDe(fn.formatDateMonthCadena( arrayvigencias.get(0)));
					}
                    if(arrayvigencias.size() == 2 ) {
                    	modelo.setVigenciaDe(fn.formatDateMonthCadena( arrayvigencias.get(0)));
                    	modelo.setVigenciaA(fn.formatDateMonthCadena( arrayvigencias.get(1)));                                     
                    }                 
                    modelo.setFechaEmision(modelo.getVigenciaDe());					
				}								
			}
			
			

			
			
		
					inicio = contenido.indexOf("Suma asegurada");
					fin  = contenido.indexOf("DESIGNACIÓN DE BENEFICIARIOS");
					
					newcontenido = new StringBuilder();
					
					
					newcontenido.append(fn.extracted(inicio, fin, contenido));
					List<EstructuraCoberturasModel> coberturas = new ArrayList<>();
					for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
						EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();						
						if(newcontenido.toString().split("\n")[i].split("-").length > 2) {
							int s = newcontenido.toString().split("\n")[i].split("###").length;							
							if(s == 6) {
								cobertura.setNombre(newcontenido.toString().split("\n")[i].split("###")[0]);
								cobertura.setSa(newcontenido.toString().split("\n")[i].split("###")[1]);
							    coberturas.add(cobertura);
							}
						}
					}
					modelo.setCoberturas(coberturas);
					
					
	
					
					inicio = contenido.indexOf("Prima total");
					fin = contenido.indexOf("contenido ###de ###la");
					newcontenido = new StringBuilder();
					
			
					newcontenido.append(fn.extracted(inicio, fin, contenido));
					
					for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
						if (newcontenido.toString().split("\n")[i].contains("Prima total")) {
							int sp = newcontenido.toString().split("\n")[i + 1].split("###").length;
							if (sp ==  5) {
						
								modelo.setPrimaneta(fn
										.castBigDecimal(fn.preparaPrimas(newcontenido.toString().split("\n")[i + 1].split("###")[0])));								
								modelo.setDerecho(fn
										.castBigDecimal(fn.preparaPrimas(newcontenido.toString().split("\n")[i + 1].split("###")[1])));								
								modelo.setRecargo(fn
										.castBigDecimal(fn.preparaPrimas(newcontenido.toString().split("\n")[i + 1].split("###")[2])));
								modelo.setPrimaTotal(fn
										.castBigDecimal(fn.preparaPrimas(newcontenido.toString().split("\n")[i + 1].split("###")[3])));								
								break;
							}
						}
						
					}
					
			
			return modelo;
		} catch (Exception ex) {
			modelo.setError(InsigniaVidaModel.this.getClass().getTypeName() + " | " + ex.getMessage() + " | " + ex.getCause());
			return modelo;
		}
    }
}
