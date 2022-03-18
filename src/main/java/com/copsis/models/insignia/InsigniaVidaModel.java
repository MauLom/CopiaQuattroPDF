package com.copsis.models.insignia;

import java.util.ArrayList;
import java.util.List;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraAseguradosModel;
import com.copsis.models.EstructuraBeneficiariosModel;
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
			
				if(newcontenido.toString().split("\n")[i].contains("Póliza:")) {
					modelo.setPoliza(newcontenido.toString().split("\n")[i].split("Póliza:")[1].replace("###", ""));
				}
				
				if(newcontenido.toString().split("\n")[i].contains("Forma de pago") && newcontenido.toString().split("\n")[i].contains("Moneda") 
					&& newcontenido.toString().split("\n")[i].contains("Producto")	
						) {
					modelo.setPlan(newcontenido.toString().split("\n")[i].trim().split("###")[3]);
					modelo.setFormaPago(fn.formaPagoSring(newcontenido.toString().split("\n")[i+1].trim()));
					String textoMoneda = newcontenido.toString().split("\n")[i+1];
					if(textoMoneda.contains("MXN")) {
						textoMoneda = "PESO MEXICANO";
					}
					modelo.setMoneda(fn.buscaMonedaEnTexto(textoMoneda));
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
			
		
					inicio = contenido.indexOf("ASEGURADO TITULAR");
					fin  = contenido.indexOf("Suma asegurada");
					newcontenido = new StringBuilder();
					newcontenido.append(fn.extracted(inicio, fin, contenido));
					List<EstructuraAseguradosModel> asegurados = new ArrayList<>();	
					EstructuraAseguradosModel asegurado = new EstructuraAseguradosModel();
					for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
						if(newcontenido.toString().split("\n")[i].contains("NOMBRE:")) {
							asegurado.setNombre(newcontenido.toString().split("\n")[i].split("NOMBRE:")[1].replace("###", "").trim());
						}
						if(newcontenido.toString().split("\n")[i].contains("NACIMIENTO:") && newcontenido.toString().split("\n")[i].contains("EDAD:")
						 && newcontenido.toString().split("\n")[i].contains("SEXO:")	) {
							
							asegurado.setNacimiento(fn.formatDateMonthCadena(newcontenido.toString().split("\n")[i].split("NACIMIENTO:")[1].split("EDAD")[0].replace("###", "").trim()));
							asegurado.setEdad(fn.castInteger(newcontenido.toString().split("\n")[i].split("EDAD:")[1].split("SEXO")[0].replace("###", "").trim()));
							asegurado.setSexo(fn.sexo((newcontenido.toString().split("\n")[i].split("SEXO:")[1].replace("###", "").trim())) ? 1: 0);
						}
					}
					asegurados.add(asegurado);
					  modelo.setAsegurados(asegurados); 
			         
			        
			
			        
			    	inicio = contenido.lastIndexOf("INFORMACIÓN DEL ASEGURADO");
					fin  = contenido.indexOf("BENEFICIARIOS SECUNDARIOS");
					newcontenido = new StringBuilder();
					newcontenido.append(fn.extracted(inicio, fin, contenido).replace("CONYUGE", "###CONYUGE###"));
					List<EstructuraBeneficiariosModel> beneficiarios = new ArrayList<>();
					
					for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
						EstructuraBeneficiariosModel beneficiario = new EstructuraBeneficiariosModel();				
						if(newcontenido.toString().split("\n")[i].contains("CONYUGE")) {
							beneficiario.setNombre(newcontenido.toString().split("\n")[i].split("###")[0].trim());
							beneficiario.setParentesco(fn.parentesco(newcontenido.toString().split("\n")[i].split("###")[1].trim()));							
							beneficiario.setPorcentaje(fn.castInteger(newcontenido.toString().split("\n")[i].split("###")[2].replace("%", "").trim()));
							beneficiarios.add(beneficiario);
						}
					}
				
					modelo.setBeneficiarios(beneficiarios);
					
					   

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
					
					inicio = contenido.indexOf("Agente");
					fin = contenido.indexOf("En cumplimiento");
					newcontenido = new StringBuilder();									
					newcontenido.append(fn.extracted(inicio, fin, contenido));
					
					if(newcontenido.length() > 50) {
						modelo.setAgente(newcontenido.toString().split("\n")[0].split("Agente:")[1].split("Clave")[0].replace("###", "").trim());
						modelo.setCveAgente(newcontenido.toString().split("\n")[0].split("Clave:")[1].replace("###", "").trim());
					}
				
					
			
			return modelo;
		} catch (Exception ex) {
			modelo.setError(InsigniaVidaModel.this.getClass().getTypeName() + " | " + ex.getMessage() + " | " + ex.getCause());
			return modelo;
		}
    }
}
