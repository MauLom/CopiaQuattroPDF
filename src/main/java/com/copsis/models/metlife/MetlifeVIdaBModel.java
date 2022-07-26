package com.copsis.models.metlife;

import java.util.ArrayList;
import java.util.List;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraBeneficiariosModel;
import com.copsis.models.EstructuraCoberturasModel;
import com.copsis.models.EstructuraJsonModel;

public class MetlifeVIdaBModel {
	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();

	public EstructuraJsonModel procesar(String contenido) {
		int inicio = 0;
		int fin = 0;
		StringBuilder newcontenido = new StringBuilder();
		StringBuilder direccion = new StringBuilder();
		contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());
		try {
			modelo.setTipo(5);
			modelo.setCia(23);
			inicio = contenido.indexOf("contratante");
			fin = contenido.indexOf("COBERTURAS");
			newcontenido.append( fn.extracted(inicio, fin, contenido));
			
			for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
				if(newcontenido.toString().split("\n")[i].contains("contratante") && newcontenido.toString().split("\n")[i].contains("Póliza")) {
					modelo.setPoliza(newcontenido.toString().split("\n")[i+1].split("###")[1]);
					modelo.setCteNombre(newcontenido.toString().split("\n")[i+1].split("###")[0]);
				}
				if(newcontenido.toString().split("\n")[i].contains("domicilio") && newcontenido.toString().split("\n")[i+1].contains("Día")) {
					direccion.append(newcontenido.toString().split("\n")[i+2].split("###")[0]);
				} 
				if(newcontenido.toString().split("\n")[i].contains("Moneda")) {
					modelo.setMoneda(1);
					modelo.setFormaPago(fn.formaPagoSring(newcontenido.toString().split("\n")[i+1]));
					direccion.append(newcontenido.toString().split("\n")[i].split("###")[0]);
					direccion.append(newcontenido.toString().split("\n")[i+1].split("###")[0]);
				}
				if(newcontenido.toString().split("\n")[i].contains("Clave del agente")) {
					modelo.setCveAgente(newcontenido.toString().split("\n")[i+1].split("AGENTE-1:")[1].split("Día")[0].replace("###", "").trim());
				}
			}
			modelo.setCteDireccion(direccion.toString());
			
			if(modelo.getCteDireccion().length() > 0) {
				modelo.setCp(fn.obtenerCPRegex2(modelo.getCteDireccion()));
			}
			
		
			inicio = contenido.indexOf("COBERTURAS");
			fin = contenido.indexOf("Recargo");
			newcontenido = new StringBuilder();
			newcontenido.append( fn.extracted(inicio, fin, contenido));
			List<EstructuraCoberturasModel> coberturas = new ArrayList<>();			
			for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
				EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();
				if(!newcontenido.toString().split("\n")[i].contains("COBERTURAS") && !newcontenido.toString().split("\n")[i].contains("Asegurada")
						&& !newcontenido.toString().split("\n")[i].contains("Prima anual")	) {
					cobertura.setNombre(newcontenido.toString().split("\n")[i].split("###")[0]);
					cobertura.setSa(newcontenido.toString().split("\n")[i].split("###")[1]);
					if(i == 3) {
						modelo.setVigenciaDe(fn.formatDateMonthCadena(newcontenido.toString().split("\n")[i].split("###")[2]));
					}
					coberturas.add(cobertura);
				}
			}
			if(modelo.getFormaPago() == 1) {
				modelo.setVigenciaA( fn.calcvigenciaA(modelo.getVigenciaDe(),12));
				modelo.setFechaEmision(modelo.getVigenciaDe());
			}
			
			modelo.setCoberturas(coberturas);			
			inicio = contenido.indexOf("Recargo");
			fin = contenido.indexOf("En el supuesto");
			newcontenido = new StringBuilder();
			newcontenido.append( fn.extracted(inicio, fin, contenido).replace("@@@", ""));
			for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
				if(newcontenido.toString().split("\n")[i].contains("forma de pago")) {
					 List<String> valores = fn.obtenerListNumeros(newcontenido.toString().split("\n")[i+1]);
					 modelo.setPrimaneta(fn.castBigDecimal(fn.castDouble(valores.get(0))));					 
					 modelo.setRecargo(fn.castBigDecimal(fn.castDouble(valores.get(1))));
					 modelo.setPrimaTotal(fn.castBigDecimal(fn.castDouble(valores.get(3).replace(",", ""))));
				}
			}
			
			inicio = contenido.indexOf("completo de los beneficiarios,");
			fin = contenido.indexOf("Para cualquier duda");
			newcontenido = new StringBuilder();
			newcontenido.append( fn.extracted(inicio, fin, contenido).replace("@@@", "").toUpperCase()
					.replace("CÓNYUGE", "###CÓNYUGE###")
					.replace("HERMANA", "###HERMANA###")
					);
			
			List<EstructuraBeneficiariosModel> beneficiarios = new ArrayList<>();			
			for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
				EstructuraBeneficiariosModel beneficiario = new EstructuraBeneficiariosModel();
				if(!newcontenido.toString().split("\n")[i].contains("BENEFICIARIOS")) {
				beneficiario.setParentesco(fn.buscaParentesco(newcontenido.toString().split("\n")[i]));
				beneficiario.setNombre(newcontenido.toString().split("\n")[i].split("###")[0].trim());
				beneficiario.setPorcentaje(fn.castInteger(newcontenido.toString().split("\n")[i].split("###")[2].replace("%", "").trim()));
				beneficiarios.add(beneficiario);				
				}
			}
			
			modelo.setBeneficiarios(beneficiarios);
			

			
			return modelo;
		} catch (Exception ex) {
			modelo.setError(MetlifeVIdaBModel.this.getClass().getTypeName() + " - catch:" + ex.getMessage() + " | "
					+ ex.getCause());
			return modelo;
		}
	}

}
