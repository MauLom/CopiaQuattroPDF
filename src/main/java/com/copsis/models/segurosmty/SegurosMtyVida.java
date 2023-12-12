package com.copsis.models.segurosmty;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.copsis.constants.ConstantsValue;
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
			contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales()).replace("FORMA ###DE PAGO", "FORMA DE PAGO")
			.replace("DESIGNACIÓN ###DE BENEFICIARIOS", ConstantsValue.DESIGNACION_DE_BENEFICIARIOS);
			
			try {
				modelo.setTipo(5);
				modelo.setCia(39);
				
			
				inicio = contenido.indexOf("CARÁTULA DE LA PÓLIZA");
				fin = contenido.indexOf( ConstantsValue.BENEFICIOS);				
				newcontenido.append( fn.extracted(inicio, fin, contenido));
	
				for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {

					if(newcontenido.toString().split("\n")[i].contains(ConstantsValue.PLAN_BASICO) &&  newcontenido.toString().split("\n")[i].contains(ConstantsValue.POLIZA_MAYUS)) {
						modelo.setPlan(newcontenido.toString().split("\n")[i].split(ConstantsValue.PLAN_BASICO)[1].split(ConstantsValue.POLIZA_MAYUS)[0].replace("###", "").trim());
						modelo.setPoliza(newcontenido.toString().split("\n")[i].split(ConstantsValue.POLIZA_MAYUS)[1].replace("No.", "").replace("###", "").trim());
					}
					if(newcontenido.toString().split("\n")[i].contains("CONTRATANTE") && newcontenido.toString().split("\n")[i].contains(ConstantsValue.TIPO_DE_POLIZAMY) ) {
						modelo.setCteNombre(newcontenido.toString().split("\n")[i].split("CONTRATANTE")[1].split(ConstantsValue.TIPO_DE_POLIZAMY)[0].replace("###", "").trim());						
					}
					
					if(newcontenido.toString().split("\n")[i].contains("EMISIÓN")) {			
						modelo.setFechaEmision(fn.formatDateMonthCadena(fn.obtenerMes(newcontenido.toString().split("\n")[i].toUpperCase())));
						modelo.setVigenciaDe(modelo.getFechaEmision());

						if(modelo.getVigenciaDe().split("-").length == 3  && fn.isNumeric(modelo.getVigenciaDe().split("-")[2])) {
							Date date = new Date();
					        Calendar calendar = Calendar.getInstance();
					        calendar.setTime(date);
					        int year = calendar.get(Calendar.YEAR);				        
							if(Integer.parseInt(modelo.getVigenciaDe().split("-")[2]) < year) {
								modelo.setVigenciaDe(year + "-" + modelo.getVigenciaDe().split("-")[1] + "-" + modelo.getVigenciaDe().split("-")[2]);
							}
						}
						
						modelo.setVigenciaA(fn.calcvigenciaA(modelo.getVigenciaDe(), 12));
						
					}
					
					if(newcontenido.toString().split("\n")[i].contains("FORMA DE PAGO")) {
						modelo.setFormaPago( fn.formaPagoSring(newcontenido.toString().split("\n")[i]));
					}
					if(newcontenido.toString().split("\n")[i].contains(ConstantsValue.MONEDA_MAYUS)) {
						modelo.setMoneda( fn.buscaMonedaEnTexto(newcontenido.toString().split("\n")[i]));
					}
					if(newcontenido.toString().split("\n")[i].contains("C.P.") &&  newcontenido.toString().split("\n")[i].split("C.P.")[1].replace("###", "").trim().length() == 5) {
						modelo.setCp(newcontenido.toString().split("\n")[i].split("C.P.")[1].replace("###", "").trim());
					}
					
					if(modelo.getCp().length() == 0 && newcontenido.toString().split("\n")[i].contains("C.P.")  && newcontenido.toString().split("\n")[i].split("###").length == 5 ) {						
						modelo.setCp(newcontenido.toString().split("\n")[i].split("###")[1].trim());
					}
					if(newcontenido.toString().split("\n")[i].contains("RESIDENCIA")) {
						modelo.setCteDireccion(newcontenido.toString().split("\n")[i].split("RESIDENCIA")[1].split("FECHA")[0].replace("###","").trim());
					}

				}
			
				
				
				inicio = contenido.indexOf("CARÁTULA DE LA PÓLIZA");
				fin = contenido.indexOf("BENEFICIOS");
				newcontenido = new StringBuilder();
				newcontenido.append( fn.extracted(inicio, fin, contenido).replace("FECHA ###DE EMISIÓN", ConstantsValue.FECHA_DE_EMISION));
				List<EstructuraAseguradosModel> asegurados = new ArrayList<>();
				EstructuraAseguradosModel asegurado = new EstructuraAseguradosModel();
				EstructuraAseguradosModel asegurado2 = new EstructuraAseguradosModel();
				for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {	
												
					if(newcontenido.toString().split("\n")[i].contains(ConstantsValue.ASEGURADOMY)) {
					
                        asegurado.setNombre(newcontenido.toString().split("\n")[i].split(ConstantsValue.ASEGURADOMY)[1].replace("###", "").trim());
                        if(asegurado.getNombre().contains(ConstantsValue.FECHA_DE_EMISION)) {
                        	asegurado.setNombre(asegurado.getNombre().split(ConstantsValue.FECHA_DE_EMISION)[0].trim());
                        }
                        if(newcontenido.toString().split("\n")[i+1].contains("###") && newcontenido.toString().split("\n")[i+1].trim().length()>1) {
                            asegurado2.setNombre(newcontenido.toString().split("\n")[i+1].replace("###", "").trim());
                        }
					}
					if(newcontenido.toString().split("\n")[i].contains("SEXO") && newcontenido.toString().split("\n")[i].contains("EDAD")  && newcontenido.toString().split("\n")[i].contains(ConstantsValue.MONEDA_MAYUS)) {
						asegurado.setNacimiento(fn.formatDateMonthCadena(fn.obtenerMes(newcontenido.toString().split("\n")[i])));
					    asegurado.setSexo(fn.sexo(newcontenido.toString().split("\n")[i].split("SEXO")[1].split(ConstantsValue.MONEDA_MAYUS)[0].replace("###", "").trim()).booleanValue() ? 1: 0);
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
						
				fin = fin ==-1 ? contenido.indexOf(ConstantsValue.DESIGNACION_DE_BENEFICIARIOS): fin;
				


				newcontenido = new StringBuilder();
				
			
				if( fin < inicio &&  contenido.indexOf("BENEFICIOS ###ASEGURADA") > -1) {
					String contenidoxt = contenido.split("BENEFICIOS ###ASEGURADA")[1];					
					fin = contenidoxt.indexOf(ConstantsValue.ASEGURADOMY);
					if(fin  > -1) {
						newcontenido.append(contenidoxt.substring(0, fin).replace("NO FUMADOR ###C ###","NO FUMADOR C###").replace("@@@", ""));	
					}					
				}
				if(fin < inicio){
					inicio = contenido.indexOf("BENEFICIOS ###SUMA ###ANEXO");
					fin = contenido.indexOf("ESIGNACIÓN DE BENEFICIARIOS:");
					newcontenido = new StringBuilder();
					newcontenido.append(contenido.substring(inicio, fin).replace("@@@", ""));	
				}

				
				newcontenido.append(contenido.substring(inicio, fin).replace("@@@", ""));	
		
				List<EstructuraCoberturasModel> coberturas = new ArrayList<>();	
				  Double suma = 0.00;
				for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {	
					EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();
				
					
					if(!newcontenido.toString().split("\n")[i].contains("SUMA ASEGURADA") && 
					  !newcontenido.toString().split("\n")[i].contains("EFECTIVIDAD") 
							&& !newcontenido.toString().split("\n")[i].contains("TITULAR")
							&& !newcontenido.toString().split("\n")[i].contains("BENEFICIOS")
							&& !newcontenido.toString().split("\n")[i].contains("ASEGURADA")
						
							&& !newcontenido.toString().split("\n")[i].contains("AÑOS")
							&& newcontenido.toString().split("\n")[i].length() > 10
							) {		
												
						int sp = newcontenido.toString().split("\n")[i].split("###").length;
							
						if(sp == 7 || sp == 8) {
							
							if(newcontenido.toString().split("\n")[i].contains("NP 65")){
								List<String> valores = fn.obtenerListNumeros(newcontenido.toString().split("\n")[i]);
								
								modelo.setPrimaneta(fn.castBigDecimal(fn.castDouble(valores.get(0))));
								modelo.setPrimaTotal(modelo.getPrimaneta());
							
							}
							if(modelo.getPrimaneta().intValue() == 0 && !fn.obtenerListNumeros(newcontenido.toString().split("\n")[i]).isEmpty()) {
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
				
				
				newcontenido = new StringBuilder();
				for (int i = 0; i < contenido.split(ConstantsValue.DESIGNACION_DE_BENEFICIARIOS).length; i++) {
				
					if(i> 0 && contenido.split(ConstantsValue.DESIGNACION_DE_BENEFICIARIOS)[i].contains(ConstantsValue.LA_CAMPANIA)) {	
						
					    newcontenido.append(contenido.split(ConstantsValue.DESIGNACION_DE_BENEFICIARIOS)[i].split(ConstantsValue.LA_CAMPANIA)[0].replace("@@@", ""));						
					}					
				}

		
				if( newcontenido.length() > 0) {
					List<EstructuraBeneficiariosModel> beneficiarios = new ArrayList<>();
					for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {	
						EstructuraBeneficiariosModel beneficiario = new EstructuraBeneficiariosModel();		
									
						if(newcontenido.toString().split("\n")[i].contains("BENEFICIARIOS") 
						 && (newcontenido.toString().split("\n")[i+1].contains("%"))
						 && !newcontenido.toString().split("\n")[i].contains("NINGUNO")) {
					
							if(newcontenido.toString().split("\n")[i+1].contains(ConstantsValue.ESPOSO)) {
								beneficiario.setNombre(newcontenido.toString().split("\n")[i+1].split(ConstantsValue.ESPOSO)[0].replace("###", "").trim());
							}else {
									
								if(newcontenido.toString().split("\n")[i+1].split("###")[0].trim().length() > 25){
									beneficiario.setNombre(newcontenido.toString().split("\n")[i+1].split("###")[0].trim());
								}else{
								
										beneficiario.setNombre(newcontenido.toString().split("\n")[i+1].split("###")[0].trim()
										+" "+newcontenido.toString().split("\n")[i+1].split("###")[1].trim()
										+" "+ newcontenido.toString().split("\n")[i+1].split("###")[2].trim());
									
								
								}
								
							}					
								beneficiario.setPorcentaje(Integer.parseInt(newcontenido.toString().split("\n")[i+1].split("###")[newcontenido.toString().split("\n")[i+1].split("###").length -1].replace("%", "").trim()));
												
							if(newcontenido.toString().split("\n")[i+1].contains(ConstantsValue.ESPOSO)) {
								beneficiario.setParentesco(1);
							}else {
								beneficiario.setParentesco(fn.buscaParentesco(newcontenido.toString().split("\n")[i+1]));	
							}
					
							beneficiarios.add(beneficiario);
						}
						
						
					}
					modelo.setBeneficiarios(beneficiarios);
				}
				

				 if(!modelo.getBeneficiarios().isEmpty()){
                   List<EstructuraBeneficiariosModel> bene = modelo.getBeneficiarios();				 
				  modelo.setBeneficiarios(bene.stream().filter(x -> !x.getNombre().contains("NINGUNO"))
                            .collect(Collectors.toList()));
				 }
				
				 
				 
				
				return modelo;
			} catch (Exception ex) {				
				modelo.setError(SegurosMtyVida.this.getClass().getTypeName() + " | " + ex.getMessage() + " | " + ex.getCause());
				 return modelo;
			}
		 
	 }

}
