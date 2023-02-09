package com.copsis.panAmerican;

import java.util.ArrayList;
import java.util.List;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraAseguradosModel;
import com.copsis.models.EstructuraBeneficiariosModel;
import com.copsis.models.EstructuraCoberturasModel;
import com.copsis.models.EstructuraJsonModel;

public class PanAmericanSaludModel {
	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
	
	public EstructuraJsonModel procesar(String contenido) {
	   	int inicio = 0;
			int fin = 0;
			StringBuilder newcontenido = new StringBuilder();
	
			contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());
		try {
			
			modelo.setTipo(3);
			modelo.setCia(84);
			
			inicio = contenido.indexOf("SEGURO DE GASTOS MÉDICOS");
			fin = contenido.indexOf("NÚCLEOS ASEGURADOS");			
			newcontenido.append( fn.extracted(inicio, fin, contenido).replace("día-mes-año: ", ""));			
			for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
				
				if(newcontenido.toString().split("\n")[i].contains("NO. PÓLIZA")) {
					modelo.setPoliza(newcontenido.toString().split("\n")[i].split("PÓLIZA")[1].replace("###", ""));
				}
				
				if(newcontenido.toString().split("\n")[i].contains("CONTRATANTE")) {
					modelo.setCteNombre(newcontenido.toString().split("\n")[i].split("CONTRATANTE")[1].replace("###", ""));
				}
				if(newcontenido.toString().split("\n")[i].contains("DIRECCIÓN")) {
					modelo.setCteDireccion(newcontenido.toString().split("\n")[i].split("DIRECCIÓN")[1].replace("###", "") +" " + newcontenido.toString().split("\n")[i+1]);
				   modelo.setCp(fn.obtenerCPRegex2( newcontenido.toString().split("\n")[i+1]));
				}
				if(newcontenido.toString().split("\n")[i].contains("DESDE")) {
					modelo.setVigenciaDe(fn.formatDateMonthCadena(fn.obtenVigePoliza(newcontenido.toString().split("\n")[i]).get(0)));
				}
				if(newcontenido.toString().split("\n")[i].contains("HASTA")) {
					modelo.setVigenciaA(fn.formatDateMonthCadena(fn.obtenVigePoliza(newcontenido.toString().split("\n")[i]).get(0)));
				}
				
				if(newcontenido.toString().split("\n")[i].contains("EMISIÓN")) {
					modelo.setFechaEmision((fn.formatDateMonthCadena(fn.obtenVigePoliza(newcontenido.toString().split("\n")[i]).get(0))));
				}
				
				
			}

			
			inicio = contenido.indexOf("NÚCLEOS ASEGURADOS");
			fin = contenido.indexOf("BENEFICIARIOS");
			newcontenido = new StringBuilder(); 
			newcontenido.append( fn.extracted(inicio, fin, contenido));
			List<EstructuraAseguradosModel> asegurados = new ArrayList<>();
			EstructuraAseguradosModel asegurado = new EstructuraAseguradosModel();
			for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
				
			
				if(newcontenido.toString().split("\n")[i].contains("ASEGURADO PRINCIPAL") && newcontenido.toString().split("\n")[i+1].contains("NOMBRE")) {
					asegurado.setNombre(newcontenido.toString().split("\n")[i+1].split("NOMBRE")[1].replace("###", ""));
				}
				if(newcontenido.toString().split("\n")[i].contains("NACIMIENTO")) {
					asegurado.setNacimiento(fn.formatDateMonthCadena(fn.obtenVigePoliza(newcontenido.toString().split("\n")[i]).get(0)));
				}
			}
			asegurados.add(asegurado);
			modelo.setAsegurados(asegurados);
			
		
			inicio = contenido.indexOf("BENEFICIARIOS");
			fin = contenido.indexOf("En el caso de");
			newcontenido = new StringBuilder(); 
			newcontenido.append( fn.extracted(inicio, fin, contenido));
			List<EstructuraBeneficiariosModel> beneficiarios = new ArrayList<>();			
			EstructuraBeneficiariosModel beneficiario = new EstructuraBeneficiariosModel();
			for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {			
				if(newcontenido.toString().split("\n")[i].contains("BENEFICIARIO 1") && newcontenido.toString().split("\n")[i+1].contains("NOMBRE")) {
					beneficiario.setNombre(newcontenido.toString().split("\n")[i+1].split("NOMBRE")[1].replace("###", ""));
				}
			}
			beneficiarios.add(beneficiario);				
			modelo.setBeneficiarios(beneficiarios);
			
	

			inicio = contenido.indexOf("FORMA DE PAGO");
			fin = contenido.lastIndexOf("AGENTE");
		
			newcontenido = new StringBuilder(); 
			newcontenido.append( fn.extracted(inicio, fin, contenido));
			for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
				
				if(newcontenido.toString().split("\n")[i].contains("FORMA DE PAGO")) {
				
					modelo.setFormaPago(fn.formaPagoSring (newcontenido.toString().split("\n")[i]));
				}
				if(newcontenido.toString().split("\n")[i].contains("PRIMA NETA")) {
					  List<String> valores = fn.obtenerListNumeros(newcontenido.toString().split("\n")[i]);
					  modelo.setPrimaneta(fn.castBigDecimal(fn.castDouble(valores.get(0))));
				}
				if(newcontenido.toString().split("\n")[i].contains("FRACCIONADO")) {
					  List<String> valores = fn.obtenerListNumeros(newcontenido.toString().split("\n")[i]);
					  modelo.setRecargo(fn.castBigDecimal(fn.castDouble(valores.get(0))));
				}
				if(newcontenido.toString().split("\n")[i].contains("DERECHO DE PÓLIZA")) {
				  List<String> valores = fn.obtenerListNumeros(newcontenido.toString().split("\n")[i]);
				  modelo.setDerecho(fn.castBigDecimal(fn.castDouble(valores.get(0))));
				}
				if(newcontenido.toString().split("\n")[i].contains("IMPORTE TOTAL")) {
					  List<String> valores = fn.obtenerListNumeros(newcontenido.toString().split("\n")[i]);
					  modelo.setPrimaTotal(fn.castBigDecimal(fn.castDouble(valores.get(0))));
				}
								
			}
			modelo.setMoneda(1);

			
			inicio = contenido.lastIndexOf("NOMBRE-DENOMINACIÓN");
			fin = contenido.indexOf("DECLARACIONES DE LA COMPAÑÍA");
	
			newcontenido = new StringBuilder(); 
			newcontenido.append( fn.extracted(inicio, fin, contenido));
			for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {				
				if(newcontenido.toString().split("\n")[i].contains("RAZÓN SOCIAL")) {
					modelo.setAgente(newcontenido.toString().split("\n")[i].split("RAZÓN SOCIAL")[1].replace("###", "") +" " + newcontenido.toString().split("\n")[i+1]);
				}
				if(newcontenido.toString().split("\n")[i].contains("CLAVE")) {
					modelo.setCveAgente(newcontenido.toString().split("\n")[i].split("CLAVE")[1].replace("###", ""));
				}
			}


			 inicio = contenido.lastIndexOf("COBERTURA BÁSICA");
	         fin = contenido.indexOf("El seguro brinda cobertura");
	         
	         newcontenido = new StringBuilder(); 
	         newcontenido.append( fn.extracted(inicio, fin, contenido));
	         List<EstructuraCoberturasModel> coberturas = new ArrayList<>();
	         EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel(); 
	         for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {  	            
	             if(newcontenido.toString().split("\n")[i].contains("SUMA ASEGURADA") && newcontenido.toString().split("\n")[i].contains("POR AÑO PÓLIZA")) {
	                cobertura.setSa(newcontenido.toString().split("\n")[i].split("PÓLIZA")[1].replace("###", "").trim());
	             }
	             if(newcontenido.toString().split("\n")[i].contains("DEDUCIBLE") && newcontenido.toString().split("\n")[i].contains("MXN")) {
	                 cobertura.setDeducible(newcontenido.toString().split("\n")[i].split("MXN")[1]);
	             }
	             if(newcontenido.toString().split("\n")[i].contains("COBERTURA BÁSICA")) {
                     cobertura.setNombre("COBERTURA BÁSICA");
                 }	         
	         }
	         
	        coberturas.add(cobertura);
	      
	         
	         inicio = contenido.lastIndexOf("CUADRO DE BENEFICIOS CUBIERTOS");
             fin = contenido.indexOf("3 de 9");
             newcontenido = new StringBuilder(); 
             newcontenido.append( fn.extracted(inicio, fin, contenido)
             .replace("Hasta MXN 2,000.00 Cobertura", 
              "Hasta MXN 2,000.00 Cobertura máxima por noche hasta 30 días por Año Póliza por Asegurado")
             .replace("Trasplante de Órganos Incluye Etapa previa al Trasplante,"
             , "Trasplante de Órganos Incluye Etapa previa al Trasplante,Etapa del Trasplante y Etapa posterior al Trasplante")
             .replace("Hasta MXN 9,000,000.00 Suma", "Hasta MXN 9,000,000.00 Suma Asegurada Máxima por Beneficio por Asegurado, de por vida")
             .replace("Hasta MXN 500,000.00 Esta suma", "Hasta MXN 500,000.00 Esta suma se descontará del valor total de la Suma Asegurada Máxima por el Beneficio de Trasplante de Órganos y está destinada a cubrir los gastos derivados del proceso de Donación")
             .replace("Hasta 60 días naturales Por Año", "Hasta 60 días naturales Por Año Póliza y dentro de los cuales sólo 30 días de estancia en Unidad de Cuidados Intensivos"));
             for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {  
                 EstructuraCoberturasModel coberturax = new EstructuraCoberturasModel(); 
                 if(!newcontenido.toString().split("\n")[i].contains("CUADRO DE BENEFICIOS") 
                         && !newcontenido.toString().split("\n")[i].contains("HOSPITALIZACIÓN")
                         && !newcontenido.toString().split("\n")[i].contains("VALOR ASEGURADO")) {
                     switch (newcontenido.toString().split("\n")[i].split("###").length) {
                        case 2:
                            coberturax.setNombre(newcontenido.toString().split("\n")[i].split("###")[0]);
                            coberturax.setSa(newcontenido.toString().split("\n")[i].split("###")[1]);
                            coberturas.add(coberturax);
                            break;

                        default:
                            break;
                    }
                     
                 }
             }
	         modelo.setCoberturas(coberturas);
	         

			return modelo;
		} catch (Exception ex) {
			modelo.setError(PanAmericanSaludModel.this.getClass().getTypeName() + " | " + ex.getMessage() + " | " + ex.getCause());
			return modelo;
		}
	}
	

}
