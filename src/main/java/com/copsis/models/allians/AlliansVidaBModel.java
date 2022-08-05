package com.copsis.models.allians;

import java.util.ArrayList;
import java.util.List;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraBeneficiariosModel;
import com.copsis.models.EstructuraCoberturasModel;
import com.copsis.models.EstructuraJsonModel;

public class AlliansVidaBModel {
	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
	
	public EstructuraJsonModel procesar( String contenido) {
		StringBuilder newcontenido = new StringBuilder();
		StringBuilder newdirec = new StringBuilder();
		int inicio = 0;
		int fin = 0;
		contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales())
				.replace("B E N E F I C I A R I O S", "BENEFICIARIOS");
		
		try {
			modelo.setTipo(5);		
			modelo.setCia(4);
			modelo.setMoneda(1);

			inicio = contenido.indexOf("Asegurado");
			fin  = contenido.indexOf("BENEFICIARIOS");	
			newcontenido = new StringBuilder();
			newcontenido.append(fn.extracted(inicio, fin, contenido));
			for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
				
				if(newcontenido.toString().split("\n")[i].contains("Asegurado") && newcontenido.toString().split("\n")[i].contains("Póliza")) {
					modelo.setCteNombre(newcontenido.toString().split("\n")[i+1].split("###")[0]);
					modelo.setPoliza(newcontenido.toString().split("\n")[i+1].split("###")[1]);
				}
				if(newcontenido.toString().split("\n")[i].contains("Domicilio")) {
					newdirec.append(newcontenido.toString().split("\n")[i+1]+" ");
					newdirec.append(newcontenido.toString().split("\n")[i+2].split("###")[0] +" ");
					newdirec.append(newcontenido.toString().split("\n")[i+3]);
				}
				if(newcontenido.toString().split("\n")[i].contains("C.P.")) {
					modelo.setCp(newcontenido.toString().split("\n")[i].split("C.P.")[1].trim().substring(0,5));
					if(newcontenido.toString().split("\n")[i].split("C.P.")[1].split("###").length > 2) {
						modelo.setRfc(newcontenido.toString().split("\n")[i].split("C.P.")[1].split("###")[1].replace(" ", ""));
					}
				}
				
				if(newcontenido.toString().split("\n")[i].contains("Fecha de Inicio") && newcontenido.toString().split("\n")[i].contains("Fin de Vigencia")
				&& newcontenido.toString().split("\n")[i+1].contains("Edad Actual")		) {                                  
                   if(newcontenido.toString().split("\n")[i+2].split("###").length == 5) {
                	   modelo.setVigenciaDe(fn.formatDateMonthCadena(newcontenido.toString().split("\n")[i+2].split("###")[3].replace(" ", "-")));
                	   modelo.setVigenciaA(fn.formatDateMonthCadena(newcontenido.toString().split("\n")[i+2].split("###")[4].replace(" ", "-")));
                    modelo.setFechaEmision(modelo.getVigenciaDe());
                   }
                    
				}
				
				
				if(newcontenido.toString().split("\n")[i].contains("Forma de Pago")) {
					modelo.setFormaPago(fn.formaPagoSring(newcontenido.toString().split("\n")[i+1]));
				}
			}
			
			modelo.setCteDireccion(newdirec.toString());
			
			
			if(modelo.getVigenciaDe().length()> 0 && modelo.getVigenciaA().length()> 0) {
				if(fn.diferencia(modelo.getVigenciaDe(), modelo.getVigenciaA()) > 3) {
					modelo.setVigenciaA(fn.calcvigenciaA(modelo.getVigenciaDe(), 12));
				}
			}
			
			
			inicio = contenido.indexOf("COBERTURAS CONTRATADAS");
			fin  = contenido.indexOf("Beneficio por Fallecimiento");	
			newcontenido = new StringBuilder();
			newcontenido.append(fn.extracted(inicio, fin, contenido));
			List<EstructuraCoberturasModel> coberturas = new ArrayList<>();
			for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {	
				EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();
				if(!newcontenido.toString().split("\n")[i].contains("COBERTURAS") && !newcontenido.toString().split("\n")[i].contains("Beneficio Básico")) {			
					cobertura.setNombre(newcontenido.toString().split("\n")[i].split("###")[0].trim());
					cobertura.setNombre(newcontenido.toString().split("\n")[i].split("###")[1].trim());
					coberturas.add(cobertura);
				}
			}
			modelo.setCoberturas(coberturas);
			
			
			
			inicio = contenido.indexOf("BENEFICIARIOS");
			fin  = contenido.indexOf("Advertencias");	
			newcontenido = new StringBuilder();
			newcontenido.append(fn.extracted(inicio, fin, contenido));
			List<EstructuraBeneficiariosModel> beneficiarios = new ArrayList<>();
			for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {	
				EstructuraBeneficiariosModel beneficiario = new EstructuraBeneficiariosModel();
				if(!newcontenido.toString().split("\n")[i].contains("Nombre") && !newcontenido.toString().split("\n")[i].contains("BENEFICIARIOS")
					&&	!newcontenido.toString().split("\n")[i].contains("Fallecimiento") 		) {						
					beneficiario.setNombre(newcontenido.toString().split("\n")[i].split("###")[0]);
					beneficiario.setParentesco(fn.parentesco(newcontenido.toString().split("\n")[i].split("###")[1]));
					beneficiario.setPorcentaje(fn.castInteger(newcontenido.toString().split("\n")[i].split("###")[2].replaceFirst("%", "").trim()));
					beneficiarios.add(beneficiario);
				}
			}
			modelo.setBeneficiarios(beneficiarios);
			
			return modelo;
		} catch (Exception ex) {
			modelo.setError(AlliansVidaBModel.this.getClass().getTypeName() + " | " + ex.getMessage() + " | "+ ex.getCause());
			return modelo;
		}
		
	}
}
