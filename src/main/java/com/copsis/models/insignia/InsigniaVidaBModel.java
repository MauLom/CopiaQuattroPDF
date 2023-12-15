package com.copsis.models.insignia;

import java.util.ArrayList;
import java.util.List;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraAseguradosModel;
import com.copsis.models.EstructuraCoberturasModel;
import com.copsis.models.EstructuraJsonModel;

public class InsigniaVidaBModel {
    private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();

      public EstructuraJsonModel procesar(String contenido) {
    	int inicio = 0;
		int fin = 0;
		List<String> arrayvigencias = new ArrayList<>();
		StringBuilder newcontenido = new StringBuilder();
		contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());
        try {
			modelo.setTipo(5);
			modelo.setCia(77);
				
			inicio = contenido.indexOf("PÓLIZA:");
			fin  = contenido.indexOf("DATOS DE LA PÓLIZA");	
		
			newcontenido = new StringBuilder();
			newcontenido.append(fn.extracted(inicio, fin, contenido));
			for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
		
				if(newcontenido.toString().split("\n")[i].contains("PÓLIZA:")){
					modelo.setPoliza(newcontenido.toString().split("\n")[i].split("PÓLIZA:")[1].replace("###", "").trim());
				}
				if(newcontenido.toString().split("\n")[i].contains("Desde") && newcontenido.toString().split("\n")[i].contains("Hasta")){
					List<String> valores = fn.obtenVigePoliza(newcontenido.toString().split("\n")[i + 1]);
                    if (!valores.isEmpty()) {
                        modelo.setVigenciaDe(fn.formatDateMonthCadena(valores.get(0)));
                        modelo.setVigenciaA(fn.formatDateMonthCadena(valores.get(1)));
                    }
				}
			}


			inicio = contenido.indexOf("DATOS DE LA PÓLIZA");
			fin  = contenido.indexOf("DATOS DEL CONTRATANTE");	

			newcontenido = new StringBuilder();
			newcontenido.append(fn.extracted(inicio, fin, contenido));
			for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
				

				if(newcontenido.toString().split("\n")[i].contains("Producto:") && newcontenido.toString().split("\n")[i].contains("Plazo de pago")){
                 modelo.setPlan(newcontenido.toString().split("\n")[i].split("Producto:")[1].split("Plazo de pago")[0].replace("###", "").trim());
				}

				if(newcontenido.toString().split("\n")[i].contains("Fecha de emisión:") && newcontenido.toString().split("\n")[i].contains("Forma de Pago:")){
					List<String> valores = fn.obtenVigePoliza(newcontenido.toString().split("\n")[i]);
					if (!valores.isEmpty()) {
					modelo.setFechaEmision(fn.formatDateMonthCadena(valores.get(0)));
					}
					modelo.setFormaPago(fn.formaPagoSring(newcontenido.toString().split("\n")[i]));
				}
				if(newcontenido.toString().split("\n")[i].contains("seguro:") && newcontenido.toString().split("\n")[i].contains("Moneda:")){
                  modelo.setMoneda(fn.buscaMonedaEnTexto(newcontenido.toString().split("\n")[i].split("Moneda:")[1]));
				}
			}


			inicio = contenido.indexOf("DATOS DEL ASEGURADO");
			fin  = contenido.indexOf("COBERTURAS AMPARADAS");	
		
			newcontenido = new StringBuilder();
			newcontenido.append(fn.extracted(inicio, fin, contenido));
			if(!newcontenido.isEmpty()){
			List<EstructuraAseguradosModel> asegurados = new ArrayList<>();
			EstructuraAseguradosModel asegurado = new EstructuraAseguradosModel();
			for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
	
				if(newcontenido.toString().split("\n")[i].contains("Nombre:")){
                  asegurado.setNombre(newcontenido.toString().split("\n")[i].split("Nombre:")[1].replace("###", "").trim());
				  modelo.setCteNombre(newcontenido.toString().split("\n")[i].split("Nombre:")[1].replace("###", "").trim());
				}
				if(newcontenido.toString().split("\n")[i].contains("Fecha de Nacimiento:")){
					List<String> valores = fn.obtenVigePoliza(newcontenido.toString().split("\n")[i]);
					if (!valores.isEmpty()) {
					  asegurado.setNacimiento(fn.formatDateMonthCadena(valores.get(0)));
					}
				}
				if(newcontenido.toString().split("\n")[i].contains("Edad:")){
					List<String> valores = fn.obtenerListNumeros2(newcontenido.toString().split("\n")[i]);
					if (!valores.isEmpty()) {                
                       asegurado.setEdad(fn.castInteger(valores.get(0)));
					}
					
				}
				if(newcontenido.toString().split("\n")[i].contains("Sexo:")){
					
				}
		
			}
			asegurados.add(asegurado);
			modelo.setAsegurados(asegurados);
		}


			inicio = contenido.indexOf("COBERTURAS AMPARADAS");
			fin  = contenido.indexOf("Prima Protección");	

		
			newcontenido = new StringBuilder();
			newcontenido.append(fn.extracted(inicio, fin, contenido));
			List<EstructuraCoberturasModel> coberturas = new ArrayList<>();
			if(!newcontenido.isEmpty()){

			for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
				EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();
			  if(!newcontenido.toString().split("\n")[i].contains("COBERTURAS AMPARADAS") && (newcontenido.toString().split("\n")[i].split("###").length == 3)){
					cobertura.setNombre(newcontenido.toString().split("\n")[i].split("###")[0].trim());
					cobertura.setSa(newcontenido.toString().split("\n")[i].split("###")[1].trim());
					coberturas.add(cobertura);	
				
			  }
			}
			modelo.setCoberturas(coberturas);
		}

		inicio = contenido.indexOf("Prima Protección");
		fin  = contenido.indexOf("Nombre del Agente:");	

		newcontenido = new StringBuilder();
		newcontenido.append(fn.extracted(inicio, fin, contenido));
		for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {

            if(newcontenido.toString().split("\n")[i].contains("Prima Protección")){
				List<String> valores = fn.obtenerListNumeros2(newcontenido.toString().split("\n")[i].replace(",", ""));
				if(!valores.isEmpty()){
					modelo.setPrimaneta(fn.castBigDecimal(fn.castDouble(valores.get(0))));
				}
			}
			if(newcontenido.toString().split("\n")[i].contains("Prima del Plan Anual")){
				List<String> valores = fn.obtenerListNumeros2(newcontenido.toString().split("\n")[i].replace(",", ""));
				if(!valores.isEmpty()){
					modelo.setPrimaneta(fn.castBigDecimal(fn.castDouble(valores.get(0))));
				}
			}
			

		}
		
		
		inicio = contenido.indexOf("Nombre del Agente:");
		fin  = contenido.lastIndexOf("PÓLIZA DE SEGURO DE VIDA");	

		newcontenido = new StringBuilder();
		newcontenido.append(fn.extracted(inicio, fin, contenido));
		for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
		  
		  if(newcontenido.toString().split("\n")[i].contains("Nombre del Agente:")){
            modelo.setAgente(newcontenido.toString().split("\n")[i].split("Nombre del Agente:")[1].split(" Clave del Agente:")[0].replace("###", "").trim());
			List<String> valores = fn.obtenerListNumeros2(newcontenido.toString().split("\n")[i].replace(",", ""));
				if(!valores.isEmpty()){
				 modelo.setCveAgente(valores.get(0));
				}  
		}

		
		}

			return modelo;
		} catch (Exception ex) {
				modelo.setError(InsigniaVidaBModel.this.getClass().getTypeName() + " | " + ex.getMessage() + " | " + ex.getCause());
			return modelo;
		}
		
     
    }
    
}
