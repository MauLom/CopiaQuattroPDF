package com.copsis.models.segurosMty;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraAseguradosModel;
import com.copsis.models.EstructuraBeneficiariosModel;
import com.copsis.models.EstructuraCoberturasModel;
import com.copsis.models.EstructuraJsonModel;

public class SegurosMtyVida {
	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();	
	private String contenido = "";
	
	public  SegurosMtyVida(String contenido){
    	this.contenido = contenido;
    }
	
	 public EstructuraJsonModel procesar() {
			int inicio = 0;
			int fin = 0;
			StringBuilder newcontenido = new StringBuilder();
			contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales()).replace("FORMA ###DE PAGO", "FORMA DE PAGO");
			
			try {
				modelo.setTipo(5);
				modelo.setCia(25);
				
			
				inicio = contenido.indexOf("CARÁTULA DE LA PÓLIZA");
				fin = contenido.indexOf("BENEFICIOS");				
				newcontenido.append( fn.extracted(inicio, fin, contenido));
	
				for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {

					if(newcontenido.toString().split("\n")[i].contains("PLAN BÁSICO") &&  newcontenido.toString().split("\n")[i].contains("PÓLIZA")) {
						modelo.setPlan(newcontenido.toString().split("\n")[i].split("PLAN BÁSICO")[1].split("PÓLIZA")[0].replace("###", "").trim());
						modelo.setPoliza(newcontenido.toString().split("\n")[i].split("PÓLIZA")[1].replace("No.", "").replace("###", "").trim());
					}
					if(newcontenido.toString().split("\n")[i].contains("CONTRATANTE") && newcontenido.toString().split("\n")[i].contains("TIPO DE PÓLIZA") ) {
						modelo.setCteNombre(newcontenido.toString().split("\n")[i].split("CONTRATANTE")[1].split("TIPO DE PÓLIZA")[0].replace("###", "").trim());						
					}
					
					if(newcontenido.toString().split("\n")[i].contains("EMISIÓN")) {			
						modelo.setFechaEmision(fn.formatDateMonthCadena(fn.obtenerMes(newcontenido.toString().split("\n")[i].toUpperCase())));
						modelo.setVigenciaDe(modelo.getFechaEmision());
						modelo.setVigenciaA(fn.calcvigenciaA(modelo.getVigenciaDe(), 12));
						
					}
					
					if(newcontenido.toString().split("\n")[i].contains("FORMA DE PAGO")) {
						modelo.setFormaPago( fn.formaPagoSring(newcontenido.toString().split("\n")[i]));
					}
					if(newcontenido.toString().split("\n")[i].contains("MONEDA")) {
						modelo.setMoneda( fn.buscaMonedaEnTexto(newcontenido.toString().split("\n")[i]));
					}
					if(newcontenido.toString().split("\n")[i].contains("C.P.") &&  newcontenido.toString().split("\n")[i].split("C.P.")[1].replace("###", "").trim().length() == 5) {
						modelo.setCp(newcontenido.toString().split("\n")[i].split("C.P.")[1].replace("###", "").trim());
					}
					
					if(modelo.getCp().length() == 0 && newcontenido.toString().split("\n")[i].contains("C.P.")  && newcontenido.toString().split("\n")[i].split("###").length == 5 ) {						
						modelo.setCp(newcontenido.toString().split("\n")[i].split("###")[1]);
					}
					if(newcontenido.toString().split("\n")[i].contains("RESIDENCIA")) {
						modelo.setCteDireccion(newcontenido.toString().split("\n")[i].split("RESIDENCIA")[1].split("FECHA")[0].replace("###","").trim());
					}

				}
			
				
				
				inicio = contenido.indexOf("CARÁTULA DE LA PÓLIZA");
				fin = contenido.indexOf("BENEFICIOS");
				newcontenido = new StringBuilder();
				newcontenido.append( fn.extracted(inicio, fin, contenido).replace("FECHA ###DE EMISIÓN", "FECHA DE EMISIÓN"));
				List<EstructuraAseguradosModel> asegurados = new ArrayList<>();
				EstructuraAseguradosModel asegurado = new EstructuraAseguradosModel();
				EstructuraAseguradosModel asegurado2 = new EstructuraAseguradosModel();
				for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {									
					if(newcontenido.toString().split("\n")[i].contains("ASEGURADO") && newcontenido.toString().split("\n")[i].contains("ASEGURADO")) {
                        asegurado.setNombre(newcontenido.toString().split("\n")[i].split("ASEGURADO")[1].replace("###", "").trim());
                        if(asegurado.getNombre().contains("FECHA DE EMISIÓN")) {
                        	asegurado.setNombre(asegurado.getNombre().split("FECHA DE EMISIÓN")[0].trim());
                        }
                        if(newcontenido.toString().split("\n")[i+1].contains("###") && newcontenido.toString().split("\n")[i+1].trim().length()>1) {
                            asegurado2.setNombre(newcontenido.toString().split("\n")[i+1].replace("###", "").trim());
                        }
					}
					if(newcontenido.toString().split("\n")[i].contains("SEXO") && newcontenido.toString().split("\n")[i].contains("EDAD")  && newcontenido.toString().split("\n")[i].contains("MONEDA")) {
						asegurado.setNacimiento(fn.formatDateMonthCadena(fn.obtenerMes(newcontenido.toString().split("\n")[i])));
					    asegurado.setSexo(fn.sexo(newcontenido.toString().split("\n")[i].split("SEXO")[1].split("MONEDA")[0].replace("###", "").trim()).booleanValue() ? 1: 0);
					    asegurado.setEdad(Integer.parseInt(newcontenido.toString().split("\n")[i].split("EDAD")[1].split("SEXO")[0].replace("###", "").trim()));					    
						asegurado2.setSexo((newcontenido.toString().split("\n")[i+1].contains("MASCULINO") ? 1: 0));
						if(newcontenido.toString().split("\n")[i+1].split("-").length == 3){
						    asegurado2.setNacimiento(fn.formatDateMonthCadena(fn.obtenerMes(newcontenido.toString().split("\n")[i+1])));
						}
						
					}
				}
				asegurados.add(asegurado);
				if(asegurado2.getNombre().length() > 0) {
					asegurados.add(asegurado2);
				}
				modelo.setAsegurados(asegurados);
				
				
				inicio = contenido.indexOf("SUMA ASEGURADA");
				fin = contenido.indexOf("MANCOMUNADO");
				if(fin == -1) {
					fin = contenido.indexOf("DESIGNACIÓN ###DE BENEFICIARIOS");
				}
		

				newcontenido = new StringBuilder();
				
				
				if( fin < inicio) {
					String contenidoxt = contenido.split("BENEFICIOS ###ASEGURADA")[1];
					fin = contenidoxt.indexOf("ASEGURADO");
					if(fin  > -1) {
						newcontenido.append(contenidoxt.substring(0, fin).replace("NO FUMADOR ###C ###","NO FUMADOR C###").replace("@@@", ""));	
					}					
				}
				else {
					newcontenido.append(fn.extracted(inicio, fin, contenido).replace("NO FUMADOR ###C ###","NO FUMADOR C###"));
				}
			
		
				List<EstructuraCoberturasModel> coberturas = new ArrayList<>();	
				  Double suma = 0.00;
				for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {	
					EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();
				
					
					if(!newcontenido.toString().split("\n")[i].contains("SUMA ASEGURADA") && !newcontenido.toString().split("\n")[i].contains("EFECTIVIDAD") 
							&& !newcontenido.toString().split("\n")[i].contains("TITULAR")) {										
						int sp = newcontenido.toString().split("\n")[i].split("###").length;
						if(sp == 7 || sp == 8) {
							if(fn.obtenerListNumeros(newcontenido.toString().split("\n")[i]).size() > 0) {
							   Double dato = fn.castDouble( fn.obtenerListNumeros(newcontenido.toString().split("\n")[i]).get(0));
							   suma +=dato;					              
							   modelo.setPrimaneta(new BigDecimal(suma).setScale(2, RoundingMode.HALF_UP));
							   modelo.setPrimaTotal(modelo.getPrimaneta());
							}
							
							cobertura.setNombre(newcontenido.toString().split("\n")[i].split("###")[0].trim());
							if(!newcontenido.toString().split("\n")[i].split("###")[1].contains("---")) {
								cobertura.setSa(newcontenido.toString().split("\n")[i].split("###")[1].trim());
							}
							
							coberturas.add(cobertura);	
						}
					}
				}
				modelo.setCoberturas(coberturas);
				
				
				for (int i = 0; i < contenido.split("DESIGNACIÓN DE BENEFICIARIOS").length; i++) {
					if(i> 0 && contenido.split("DESIGNACIÓN DE BENEFICIARIOS")[i].contains("LA COMPAÑÍA")) {											
					    newcontenido.append(contenido.split("DESIGNACIÓN DE BENEFICIARIOS")[i].split("LA COMPAÑÍA")[0].replace("@@@", ""));						
					}					
				}
				
				if( newcontenido.length() > 0) {
					List<EstructuraBeneficiariosModel> beneficiarios = new ArrayList<>();
					for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {	
						EstructuraBeneficiariosModel beneficiario = new EstructuraBeneficiariosModel();						
						if(newcontenido.toString().split("\n")[i].contains("BENEFICIARIOS")) {
							if(newcontenido.toString().split("\n")[i+1].contains("ESPOSO")) {
								beneficiario.setNombre(newcontenido.toString().split("\n")[i+1].split("ESPOSO")[0].replace("###", "").trim());
							}else {
								beneficiario.setNombre(newcontenido.toString().split("\n")[i+1].split("###")[0].trim());
							}
							beneficiario.setPorcentaje(Integer.parseInt(newcontenido.toString().split("\n")[i+1].split("###")[newcontenido.toString().split("\n")[i+1].split("###").length -1].replace("%", "").trim()));
							if(newcontenido.toString().split("\n")[i+1].contains("ESPOSO")) {
								beneficiario.setParentesco(1);
							}else {
								beneficiario.setParentesco(2);	
							}													
							beneficiarios.add(beneficiario);
						}
					}
					modelo.setBeneficiarios(beneficiarios);
				}
				
				

				
				return modelo;
			} catch (Exception ex) {
				modelo.setError(SegurosMtyVida.this.getClass().getTypeName() + " | " + ex.getMessage() + " | " + ex.getCause());
				 return modelo;
			}
		 
	 }

}
