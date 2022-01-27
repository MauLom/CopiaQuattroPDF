package com.copsis.models.gnp;

import java.util.ArrayList;
import java.util.List;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraCoberturasModel;
import com.copsis.models.EstructuraJsonModel;

public class GnpDiversosBModelo {
	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
	
	private String contenido = "";
	private String ubicaciociones = "";

	public GnpDiversosBModelo(String contenido,String ubicaciociones) {
		this.contenido = contenido;
		this.ubicaciociones=ubicaciociones;
	}
	public  EstructuraJsonModel procesar (){
		 int inicio = 0;
		 int fin = 0;
		 Boolean primas =true;
			StringBuilder newcontenido = new StringBuilder();
			contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales())
					;
			
		try {
			modelo.setTipo(7);
			modelo.setCia(18);
			
		
			
			inicio = contenido.indexOf("Póliza de Seguro de Daños");
			fin = contenido.indexOf("Prima Neta del Seguro");
			if(inicio > -1 && fin > -1   && inicio  < fin ) {
				newcontenido.append(contenido.substring(inicio,fin).replace("@@@", "").replace("\r", ""));
				modelo.setFormaPago(fn.formaPagoSring(newcontenido.toString()));
				modelo.setMoneda(fn.buscaMonedaEnTexto(newcontenido.toString()));
				for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
		         
		            if(newcontenido.toString().split("\n")[i].contains("Clave") && newcontenido.toString().split("\n")[i].contains("Agente") && newcontenido.toString().split("\n")[i].contains("Póliza No."))
		            {
		            	modelo.setAgente(newcontenido.toString().split("\n")[i+1].split("###")[1]);
		            	modelo.setCveAgente(newcontenido.toString().split("\n")[i+1].split("###")[0]);
		            	modelo.setPoliza(newcontenido.toString().split("\n")[i+1].split("###")[2]);		            	
		            }
		            if(newcontenido.toString().split("\n")[i].contains("Nombre") && newcontenido.toString().split("\n")[i].contains("Contratante")  && newcontenido.toString().split("\n")[i].contains("Contratante"))
		            {
		            	modelo.setCteNombre(newcontenido.toString().split("\n")[i+1].replace("###", "").trim());
		            	modelo.setCteDireccion(newcontenido.toString().split("\n")[i+2].replace("###", ""));
		            }
		            if(newcontenido.toString().split("\n")[i].contains("C.P.") && newcontenido.toString().split("\n")[i].contains("RFC:")) {
		            	modelo.setCp(newcontenido.toString().split("\n")[i].split("C.P.")[1].substring(0, 5));
		            	modelo.setRfc(newcontenido.toString().split("\n")[i].split("RFC:")[1].replace("###", "").trim());
		            }
		            if(newcontenido.toString().split("\n")[i].contains("Día Mes Año") && newcontenido.toString().split("\n")[i].length() < 20) {
	                   String  x =newcontenido.toString().split("\n")[i+1];
	            
		             if(x.split("###").length == 11 ) {
		            	   modelo.setVigenciaDe(fn.formatDateMonthCadena(x.split("###")[5]+"-" + x.split("###")[6]+"-" + x.split("###")[7]));
		                   modelo.setVigenciaA(fn.formatDateMonthCadena(x.split("###")[8]+"-" + x.split("###")[9]+"-" + x.split("###")[10]));
		                    modelo.setFechaEmision(modelo.getVigenciaDe());
		             }
		            }
				}
			}


			inicio = contenido.indexOf("Recargos");
			fin = contenido.indexOf("Grupo###Nacional###Provincial");

			if(inicio > -1 && fin > -1   && inicio  < fin ) {
				newcontenido = new StringBuilder();
				newcontenido.append(contenido.substring(inicio,fin).replace("@@@", "").replace("\r", ""));
				for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
				
			
					if(newcontenido.toString().split("\n")[i].contains("Recargos") && newcontenido.toString().split("\n")[i].contains("Derechos") && primas) {			
						modelo.setPrimaneta(fn.castBigDecimal(fn.castDouble(newcontenido.toString().split("\n")[i+1].split("###")[0])));
						modelo.setRecargo(fn.castBigDecimal(fn.castDouble(newcontenido.toString().split("\n")[i+1].split("###")[1])));
						modelo.setDerecho(fn.castBigDecimal(fn.castDouble(newcontenido.toString().split("\n")[i+1].split("###")[2])));
						modelo.setIva(fn.castBigDecimal(fn.castDouble(newcontenido.toString().split("\n")[i+1].split("###")[4])));
						modelo.setPrimaTotal(fn.castBigDecimal(fn.castDouble(newcontenido.toString().split("\n")[i+1].split("###")[5])));
						
						primas = false;
					}
				}
			}
			
			inicio = contenido.indexOf("DETALLE DE COBERTURAS");
			fin = contenido.lastIndexOf("Para mayor información");
	
			
			if(inicio > -1 && fin > -1   && inicio  < fin ) {
				List<EstructuraCoberturasModel> coberturas = new ArrayList<>();
				newcontenido = new StringBuilder();
				newcontenido.append(contenido.substring(inicio,fin).replace("@@@", "").replace("\r", "").contains( "Para mayor información") ? contenido.substring(inicio,fin).replace("@@@", "").replace("\r", "").split("Para mayor información")[0] : contenido.substring(inicio,fin).replace("@@@", "").replace("\r", ""));
				for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
					EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();
					if(!newcontenido.toString().split("\n")[i].contains("DETALLE DE COBERTURAS") && !newcontenido.toString().split("\n")[i].contains("SECCION")
							&& !newcontenido.toString().split("\n")[i].contains("OBLIGACIONES") && !newcontenido.toString().split("\n")[i].contains("COBERTURAS AMPARADAS")
							&& !newcontenido.toString().split("\n")[i].contains("VALORES TOTALES DECLARADOS") && !newcontenido.toString().split("\n")[i].contains("INCLUIDO EN CONTENIDOS")
							&& !newcontenido.toString().split("\n")[i].contains("SUMA ASEGURADA") && !newcontenido.toString().split("\n")[i].contains("INCLUIDO EN CONTENIDOS")
							) {
						
						if (newcontenido.toString().split("\n")[i].length() > 10) {
							cobertura.setNombre(newcontenido.toString().split("\n")[i].split("###")[0]);
							if (newcontenido.toString().split("\n")[i].split("###").length >1) {
								cobertura.setSa(newcontenido.toString().split("\n")[i].split("###")[1]);
							}
							coberturas.add(cobertura);
							
						}  
						
					}
				}
				modelo.setCoberturas(coberturas);
			}

			
			return modelo;
		} catch (Exception e) {
			 return modelo;
		}
		
	}
	

}