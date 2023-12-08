package com.copsis.models.planSeguro;

import java.util.ArrayList;
import java.util.List;

import com.copsis.constants.ConstantsValue;
import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraAseguradosModel;
import com.copsis.models.EstructuraCoberturasModel;
import com.copsis.models.EstructuraJsonModel;

public class PlanSeguroSaludBModel {
	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();	
	
	 public EstructuraJsonModel procesar(String contenido) {
			int inicio = 0;
			int fin = 0;
			StringBuilder newcontenido = new StringBuilder();
			StringBuilder newdire = new StringBuilder();
			contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());
			try {
				modelo.setTipo(3);
				modelo.setCia(25);

				inicio = contenido.indexOf("Poliza:###");
				inicio = inicio ==-1 ? contenido.indexOf("Póliza"):inicio;
				fin = contenido.indexOf(ConstantsValue.COBERTURAS);
				
				newcontenido.append( fn.extracted(inicio, fin, contenido));
				
				for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
					
					if(newcontenido.toString().split("\n")[i].contains(ConstantsValue.POLIZA_ACENT2) && newcontenido.toString().split("\n")[i].contains("Certif")) {
						modelo.setPoliza(newcontenido.toString().split("\n")[i].split(ConstantsValue.POLIZA_ACENT2)[1].split("Certif")[0].replace("###", "").trim());
					}
					if(modelo.getPoliza().isEmpty() && newcontenido.toString().split("\n")[i].contains("Póliza") && newcontenido.toString().split("\n")[i].contains("Certif")) {
					modelo.setPoliza(newcontenido.toString().split("\n")[i+2].split("###")[0]);
					}
					
					if(modelo.getPoliza().isEmpty() && newcontenido.toString().split("\n")[i].contains(ConstantsValue.POLIZA_ACENT2)) {
						modelo.setPoliza(newcontenido.toString().split("\n")[i].split(ConstantsValue.POLIZA_ACENT2)[1].replace("###", ""));
					}
					if(modelo.getPoliza().isEmpty() && newcontenido.toString().split("\n")[i].contains(ConstantsValue.POLIZA)) {
						modelo.setPoliza(newcontenido.toString().split("\n")[i].split(ConstantsValue.POLIZA)[1].replace("###", ""));
					}
					
					if(newcontenido.toString().split("\n")[i].contains("Nombre:") && newcontenido.toString().split("\n")[i].contains("Inicio")) {
						modelo.setCteNombre(newcontenido.toString().split("\n")[i].split("Nombre:")[1].split("Inicio")[0].replace("###", "").trim());
					}
					if(newcontenido.toString().split("\n")[i].contains(fn.palabraRgx(newcontenido.toString().split("\n")[i], ConstantsValue.DIRECCION2))) {
						newdire.append(newcontenido.toString().split("\n")[i].split(ConstantsValue.DIRECCIOMN)[1].split("###")[1] +" ");
						newdire.append(newcontenido.toString().split("\n")[i+1].split("###")[0]);
						newdire.append(newcontenido.toString().split("\n")[i+2].split("###")[0]);
					}
					if( newcontenido.toString().split("\n")[i].contains("Inicio") && newcontenido.toString().split("\n")[i].contains("Fin")		) {
						
						modelo.setVigenciaDe(fn.formatDateMonthCadena(fn.obtenVigePoliza(newcontenido.toString().split("\n")[i+1]).get(0)));
						modelo.setVigenciaA(fn.formatDateMonthCadena(fn.obtenVigePoliza(newcontenido.toString().split("\n")[i+1]).get(1)));
						if(modelo.getVigenciaDe().length() >  0) {
							modelo.setFechaEmision(modelo.getVigenciaDe());
						}
					}
					
					if(newcontenido.toString().split("\n")[i].split("-").length > 3 && modelo.getVigenciaDe().length() == 0 &&  modelo.getVigenciaA().length() == 0) {
	
						List<String> valores = fn.obtenVigePoliza(newcontenido.toString().split("\n")[i]);				
						if(valores.size() ==2){
							modelo.setVigenciaDe(fn.formatDateMonthCadena(fn.obtenVigePoliza(newcontenido.toString().split("\n")[i]).get(0)));
							modelo.setVigenciaA(fn.formatDateMonthCadena(fn.obtenVigePoliza(newcontenido.toString().split("\n")[i]).get(1)));
						}
						if(valores.size() ==3){
							modelo.setVigenciaDe(fn.formatDateMonthCadena(fn.obtenVigePoliza(newcontenido.toString().split("\n")[i]).get(1)));
							modelo.setVigenciaA(fn.formatDateMonthCadena(fn.obtenVigePoliza(newcontenido.toString().split("\n")[i]).get(2)));
						}
						
						if(modelo.getVigenciaDe().length() >  0) {
							modelo.setFechaEmision(modelo.getVigenciaDe());
						}
					}
					if(newcontenido.toString().split("\n")[i].contains("RFC:") && newcontenido.toString().split("\n")[i].contains("desde")) {
						modelo.setRfc(newcontenido.toString().split("\n")[i].split("RFC:")[1].split("desde")[0].replace("###", "").trim());
					}
					if(newcontenido.toString().split("\n")[i].contains("C.P.")){
					modelo.setCp(newcontenido.toString().split("\n")[i].split("C.P.")[1].split("###")[1]);
					} 
				
					if(newcontenido.toString().split("\n")[i].contains("Moneda") && newcontenido.toString().split("\n")[i].contains("Pago") && newcontenido.toString().split("\n")[i].contains("Plan")){
					 modelo.setMoneda(fn.buscaMonedaEnTexto( newcontenido.toString().split("\n")[i+1]));
					 modelo.setFormaPago(fn.formaPagoSring( newcontenido.toString().split("\n")[i+1]));
					 if(newcontenido.toString().split("\n")[i].split("###").length == 10) {
						 modelo.setPlan(newcontenido.toString().split("\n")[i+1].split("###")[7]);
					 }

					}
					if(modelo.getFormaPago() == 0 && newcontenido.toString().split("\n")[i].contains("Plan Seguro")){
						modelo.setMoneda(fn.buscaMonedaEnTexto( newcontenido.toString().split("\n")[i+1]));
						modelo.setFormaPago(fn.formaPagoSring( newcontenido.toString().split("\n")[i+1]));
						modelo.setPlan(newcontenido.toString().split("\n")[i+1].split("###")[1]);
						
					}
							
				}
				modelo.setCteDireccion(newdire.toString());
				if(modelo.getVigenciaDe().equals(modelo.getVigenciaA())){
					modelo.setVigenciaA(fn.calcvigenciaA(modelo.getVigenciaDe(), 12));
				}
				

				
				inicio = contenido.indexOf(ConstantsValue.COBERTURAS);
				fin = contenido.indexOf("Primas");
				fin = fin == -1? contenido.indexOf(ConstantsValue.CLAVE_DE_AGENTENM):fin;
				
				newcontenido = new StringBuilder();
				newcontenido.append( fn.extracted(inicio, fin, contenido));
				List<EstructuraCoberturasModel> coberturas = new ArrayList<>();
				for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {					
					if(!newcontenido.toString().split("\n")[i].contains("Tabla de Honorarios") && !newcontenido.toString().split("\n")[i].contains(ConstantsValue.COBERTURAS) && !newcontenido.toString().split("\n")[i].contains("COBERTURA BASICA") && !newcontenido.toString().split("\n")[i].contains("COBERTURAS ADICIONALES ")) {
						EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();
					    int sp = newcontenido.toString().split("\n")[i].split("###").length;
						if(sp >1) {							
							cobertura.setNombre(newcontenido.toString().split("\n")[i].split("###")[0]);
							cobertura.setSa(newcontenido.toString().split("\n")[i].split("###")[1]);
							coberturas.add(cobertura);
						}
					}
				}
				modelo.setCoberturas(coberturas);
				

				inicio = contenido.indexOf("Primas");
				inicio = inicio == -1 ?contenido.indexOf(ConstantsValue.PRIMA_NETA):fin;
				fin = contenido.indexOf(ConstantsValue.CLAVE_DE_AGENTENM);
				 
				
				newcontenido = new StringBuilder();
				newcontenido.append( fn.extracted(inicio, fin, contenido));
			
				for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
                  
					if(newcontenido.toString().split("\n")[i].contains("Financiamiento") && newcontenido.toString().split("\n")[i].contains(ConstantsValue.PRIMA_NETA) 
					&& newcontenido.toString().split("\n")[i+1].contains("Expedición") ) {
											
						 	List<String> valores  = fn.obtenerListNumeros(newcontenido.toString().split("\n")[i+2]);
						 modelo.setPrimaneta(fn.castBigDecimal(fn.castDouble(valores.get(0).replace(",", ""))));
						 modelo.setRecargo(fn.castBigDecimal(fn.castDouble(valores.get(3))));
						 modelo.setDerecho(fn.castBigDecimal(fn.castDouble(valores.get(4))));
						 modelo.setIva(fn.castBigDecimal(fn.castDouble(valores.get(5))));
						 modelo.setPrimaTotal(fn.castBigDecimal(fn.castDouble(valores.get(6).replace(",", ""))));
						
					}
				
					if( newcontenido.toString().split("\n")[i].contains(ConstantsValue.PRIMA_NETA)) {	
									
						List<String> valores = fn.obtenerListNumeros(newcontenido.toString().split("\n")[i+1]);					
						 if(!valores.isEmpty()){
 						 modelo.setPrimaneta(fn.castBigDecimal(fn.castDouble(valores.get(0).replace(",", ""))));
						 modelo.setRecargo(fn.castBigDecimal(fn.castDouble(valores.get(3))));
						 modelo.setDerecho(fn.castBigDecimal(fn.castDouble(valores.get(4))));
						 modelo.setIva(fn.castBigDecimal(fn.castDouble(valores.get(5))));
						 modelo.setPrimaTotal(fn.castBigDecimal(fn.castDouble(valores.get(6).replace(",", ""))));
						 }
						
					}
				}
				

				inicio = contenido.indexOf(ConstantsValue.CLAVE_DE_AGENTENM);
				fin = contenido.indexOf("En###cumplimiento");
				if(fin == -1) {
					fin = contenido.indexOf("En cumplimiento");
				}
				newcontenido = new StringBuilder();
				newcontenido.append( fn.extracted(inicio, fin, contenido));
				for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
				
					if(newcontenido.toString().split("\n")[i].contains("Agente")) {
						modelo.setCveAgente(newcontenido.toString().split("\n")[i+1].split("###")[0]);
						modelo.setAgente(newcontenido.toString().split("\n")[i+1].split("###")[1].replace(". .", "").trim());
					}
				}
				
				
				inicio = contenido.indexOf("NOMBRE###SEXO");
                fin = contenido.indexOf(ConstantsValue.COBERTURAS);
                newcontenido = new StringBuilder();
                newcontenido.append( fn.extracted(inicio, fin, contenido));
                List<EstructuraAseguradosModel> asegurados = new ArrayList<>();
                for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {                    
                    EstructuraAseguradosModel asegurado = new EstructuraAseguradosModel();
                    if(newcontenido.toString().split("\n")[i].split("-").length > 3) {
                        asegurado.setNombre(newcontenido.toString().split("\n")[i].split("###")[0]);
                        asegurado.setSexo(Boolean.TRUE.equals(fn.sexo(newcontenido.toString().split("\n")[i].split("###")[1])) ? 1: 0);
                        if(fn.obtenVigePoliza(newcontenido.toString().split("\n")[i]).size() > 2) {
                            asegurado.setNacimiento(fn.formatDateMonthCadena(fn.obtenVigePoliza(newcontenido.toString().split("\n")[i]).get(0)));
                            asegurado.setAntiguedad(fn.formatDateMonthCadena(fn.obtenVigePoliza(newcontenido.toString().split("\n")[i]).get(1)));
                        }
                       
                        asegurados.add(asegurado);
                    }
                }
                modelo.setAsegurados(asegurados);
                
                
                
				return modelo;
			} catch (Exception ex) {
				modelo.setError(PlanSeguroSaludBModel.this.getClass().getTypeName() + " | " + ex.getMessage() + " | " + ex.getCause());
				 return modelo;
			}
	 }
}
