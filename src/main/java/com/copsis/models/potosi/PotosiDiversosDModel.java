package com.copsis.models.potosi;

import java.util.ArrayList;
import java.util.List;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraCoberturasModel;
import com.copsis.models.EstructuraJsonModel;

public class PotosiDiversosDModel {

	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();	
	
	public EstructuraJsonModel procesar(String contenido) {
		int inicio = 0;
		int fin = 0;
		StringBuilder newcontenido = new StringBuilder();
		contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());
		try {
			
			modelo.setTipo(7);
			modelo.setCia(37);

			inicio = contenido.indexOf("Número de Póliza:");
			fin = contenido.indexOf("Información del Asegurado Titular");

			newcontenido.append( fn.extracted(inicio, fin, contenido));
			
			for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
				if(newcontenido.toString().split("\n")[i].contains("Número de Póliza:")) {
					modelo.setPoliza(newcontenido.toString().split("\n")[i].split("Número de Póliza:")[1].replace("###", "").trim());
				}
				if(newcontenido.toString().split("\n")[i].contains("Vigencia")) {				
					modelo.setVigenciaDe(fn.formatDateMonthCadena(fn.obtenVigePoliza(newcontenido.toString().split("\n")[i+1]).get(0)));
					modelo.setVigenciaA(fn.formatDateMonthCadena(fn.obtenVigePoliza(newcontenido.toString().split("\n")[i+1]).get(1)));
					
				}
			}
			if(modelo.getVigenciaDe().length()> 0) {
				modelo.setFechaEmision(modelo.getVigenciaDe());
			}
   
			inicio = contenido.indexOf("Información del Asegurado");
			fin = contenido.indexOf("Información Adicional");
			 newcontenido = new StringBuilder();
			newcontenido.append( fn.extracted(inicio, fin, contenido));
			
			for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
			
				if(newcontenido.toString().split("\n")[i].contains("Contratante") && newcontenido.toString().split("\n")[i+1].contains("NOMBRE")){
					modelo.setCteNombre(newcontenido.toString().split("\n")[i+1].split("AZÓN SOCIAL:")[1].trim());
				}
				if(newcontenido.toString().split("\n")[i].contains("RFC:") && newcontenido.toString().split("\n")[i].contains("TELÉFONO:")) {
					modelo.setRfc(newcontenido.toString().split("\n")[i].split("RFC:")[1].split("TELÉFONO")[0].replace("###", "").trim());
				}
				if(newcontenido.toString().split("\n")[i].contains("DOMICILIO FISCAL:") && newcontenido.toString().split("\n")[i].contains("CÓDIGO POSTAL")) {
					modelo.setCteDireccion(newcontenido.toString().split("\n")[i].split("DOMICILIO FISCAL:")[1].split("CÓDIGO POSTAL")[0].replace("###", "").trim());
				}
				if(newcontenido.toString().split("\n")[i].contains("MONEDA")) {
				 modelo.setMoneda(fn.buscaMonedaEnTexto( newcontenido.toString().split("\n")[i]));	
				}
				if(newcontenido.toString().split("\n")[i].contains("PLAN DE PAGO:")) {
					 modelo.setFormaPago(fn.formaPagoSring( newcontenido.toString().split("\n")[i]));	
				}
				if(newcontenido.toString().split("\n")[i].contains("CLAVE DEL AGENTE:") && newcontenido.toString().split("\n")[i].contains("TIPO DE OPERACIÓN:")) {
					 modelo.setCveAgente(newcontenido.toString().split("\n")[i].split("AGENTE:")[1].split("TIPO")[0].replace("###", "").trim());	
				}
				if(newcontenido.toString().split("\n")[i].contains("AGENTE") && newcontenido.toString().split("\n")[i].contains("FECHA DE OPERACIÓN")) {
					 modelo.setAgente(newcontenido.toString().split("\n")[i].split("AGENTE:")[1].split("FECHA")[0].replace("###", "").trim());	
				}
			}
			

			inicio = contenido.indexOf("COBERTURAS AMPARADA");
			fin = contenido.indexOf("Información de Prima");
            newcontenido = new StringBuilder();
			newcontenido.append( fn.extracted(inicio, fin, contenido));
			List<EstructuraCoberturasModel> coberturas = new ArrayList<>();
			for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
				EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();
				if(!newcontenido.toString().split("\n")[i].contains("COBERTURAS")) {				
					if(newcontenido.toString().split("\n")[i].split("###").length > 1) {
						  cobertura.setNombre(newcontenido.toString().split("\n")[i].split("###")[0].trim());
						  cobertura.setSa(newcontenido.toString().split("\n")[i].split("###")[1].trim());
						  cobertura.setDeducible(newcontenido.toString().split("\n")[i].split("###")[2].trim());
						  coberturas.add(cobertura);
					}
				}
			}
			
			modelo.setCoberturas(coberturas);

		
			inicio = contenido.indexOf("Información de Prima");
			fin = contenido.indexOf("Seguros ###el ###Potosí");
            newcontenido = new StringBuilder();
			newcontenido.append( fn.extracted(inicio, fin, contenido));
			for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
				
				if(newcontenido.toString().split("\n")[i].contains("PRIMA NETA")) {
					 List<String> valores = fn.obtenerListNumeros(newcontenido.toString().split("\n")[i+1]);
					 modelo.setPrimaneta(fn.castBigDecimal(fn.castDouble(valores.get(0))));
					 modelo.setDerecho(fn.castBigDecimal(fn.castDouble(valores.get(1))));
					 modelo.setRecargo(fn.castBigDecimal(fn.castDouble(valores.get(2))));
					 modelo.setIva(fn.castBigDecimal(fn.castDouble(valores.get(4))));					 
					 modelo.setPrimaTotal(fn.castBigDecimal(fn.castDouble(valores.get(7))));
				}
			}
			
			
			return modelo;
		} catch (Exception ex) {
			modelo.setError(PotosiDiversosDModel.this.getClass().getTypeName() + " | " + ex.getMessage() + " | " + ex.getCause());
			return modelo;
		}
		
		
	}
}
