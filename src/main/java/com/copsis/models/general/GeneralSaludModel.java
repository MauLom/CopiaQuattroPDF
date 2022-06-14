package com.copsis.models.general;
import java.util.ArrayList;
import java.util.List;
import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraAseguradosModel;
import com.copsis.models.EstructuraCoberturasModel;
import com.copsis.models.EstructuraJsonModel;

public class GeneralSaludModel {		    //tipo

	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
	
	public EstructuraJsonModel procesar(String contenido) {
		StringBuilder newcontenido = new StringBuilder();
		int inicio = 0;		    //tipo
		int fin = 0;
		contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());
		   contenido = contenido.replaceAll("NOMBRE Y DIRECCIÓ N DEL CONTRATANTE", "NOMBRE Y DIRECCIÓN DEL CONTRATANTE")
	                .replace("DATOS DEL C O NTRATANTE", "NOMBRE Y DIRECCIÓN DEL CONTRATANTE")
	                .replace("COBERTURA S Y LÍMITES", "COBERTURAS Y LÍMITES")
	                .replace("Nombre:", "NOMBRE:")
	                .replace("Dirección", "DIRECCIÓN:")
	                .replace("Póliza", "PÓLIZA")
	                .replace("Emisión", "EMISIÓN")
	                .replace("Vigencia:", "VIGENCIA:")
	                .replace("anterior", "ANTERIOR")                
	                .replace("Moneda", "MONEDA")
	                .replace("Forma de Pago", "FORMA DE PAGO")
	                .replace("Prima Neta", "PRIMA NETA")
	                .replace("Fraccionado", "FRACCIONADO")
	                .replace("Prima Total", "PRIMA TOTAL")
	                .replace("Recibo", "RECIBO")
	                .replace("ASEGU R ADOS", "ASEGURADOS")
	                .replace("Plan", "PLAN");
		
		try {		
            modelo.setTipo(3);
            modelo.setCia(15);

            inicio = contenido.indexOf("NOMBRE Y DIRECCIÓN DEL CONTRATANTE");
            fin = contenido.indexOf("COBERTURAS Y LÍMITES");	
			newcontenido.append( fn.extracted(inicio, fin, contenido));
			
			for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {				
				  if (newcontenido.toString().split("\n")[i].contains("CONTRATANTE")) {
                      modelo.setCteNombre(newcontenido.toString().split("\n")[i + 1].split("NOMBRE:")[1].replace("###", "").trim());
                  }				  
				  if (newcontenido.toString().split("\n")[i].contains("DIRECCIÓN:")) {                     
                      modelo.setCteDireccion((newcontenido.toString().split("\n")[i].split("DIRECCIÓN:")[1] + " " + newcontenido.toString().split("\n")[i + 1] + " " + newcontenido.toString().split("\n")[i + 2].replace("###", "").trim()).replace(":", "").replace("###", ""));
                  }
				  
                  if (newcontenido.toString().split("\n")[i].contains("C.P:")) {
                      modelo.setCp(newcontenido.toString().split("\n")[i].split("C.P:")[1].trim());
                  }                  
                  if (newcontenido.toString().split("\n")[i].contains("PÓLIZA") && newcontenido.toString().split("\n")[i].contains("ANTERIOR") && newcontenido.toString().split("\n")[i].contains("EMISIÓN") && newcontenido.toString().split("\n")[i].contains("VIGENCIA:")) {
                     
                      if(newcontenido.toString().split("\n")[i+1].split("###").length == 3 ){
                      modelo.setPoliza(newcontenido.toString().split("\n")[i + 1].split("###")[0]);

                      
                      modelo.setFechaEmision(fn.formatDateMonthCadena(fn.obtenVigePoliza( newcontenido.toString().split("\n")[i + 1]).get(0).toString()));
                      modelo.setVigenciaDe(fn.formatDateMonthCadena(fn.obtenVigePoliza( newcontenido.toString().split("\n")[i + 1]).get(1).toString()));
                      modelo.setVigenciaA(fn.formatDateMonthCadena(fn.obtenVigePoliza( newcontenido.toString().split("\n")[i + 1]).get(2).toString()));
                     
                      }else{
                      modelo.setPoliza(newcontenido.toString().split("\n")[i + 1].split("###")[0]);
                      modelo.setFechaEmision(fn.formatDateMonthCadena(fn.obtenVigePoliza( newcontenido.toString().split("\n")[i + 1]).get(0).toString()));
                      modelo.setVigenciaDe(fn.formatDateMonthCadena(fn.obtenVigePoliza( newcontenido.toString().split("\n")[i + 1]).get(1).toString()));
                      modelo.setVigenciaA(fn.formatDateMonthCadena(fn.obtenVigePoliza( newcontenido.toString().split("\n")[i + 1]).get(2).toString()));
                                     
                      }

                  }
                  
                  if (newcontenido.toString().split("\n")[i].contains("MONEDA") && newcontenido.toString().split("\n")[i].contains("FORMA DE PAGO")) {
                    	  modelo.setMoneda(fn.buscaMonedaEnTexto(newcontenido.toString().split("\n")[i + 1]));
                    	  modelo.setFormaPago(fn.formaPagoSring(newcontenido.toString().split("\n")[i + 1]));                                               
                  }
                  
                  if (newcontenido.toString().split("\n")[i].contains("FRACCIONADO") && newcontenido.toString().split("\n")[i].contains("Expedición")) {
                	  List<String> valores = fn.obtenerListNumeros(newcontenido.toString().split("\n")[i+1]);
			             		 modelo.setPrimaneta(fn.castBigDecimal(fn.castDouble(valores.get(0))));
								 modelo.setRecargo(fn.castBigDecimal(fn.castDouble(valores.get(1))));
								 modelo.setDerecho(fn.castBigDecimal(fn.castDouble(valores.get(2))));
								 modelo.setIva(fn.castBigDecimal(fn.castDouble(valores.get(3))));
								 modelo.setPrimaTotal(fn.castBigDecimal(fn.castDouble(valores.get(4))));
								 modelo.setPrimerPrimatotal(fn.castBigDecimal(fn.castDouble(valores.get(5))));                	
                  }
                  
               
				  
			}
			
			
			inicio = contenido.indexOf("ASEGURADOS");
            fin = contenido.indexOf("MONEDA");
            newcontenido = new StringBuilder();
        	newcontenido.append( fn.extracted(inicio, fin, contenido));
        	List<EstructuraAseguradosModel> asegurados = new ArrayList<>();
			for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
				EstructuraAseguradosModel asegurado = new  EstructuraAseguradosModel();
				 if (newcontenido.toString().split("\n")[i].split("-").length > 3) {
		              asegurado.setNombre( newcontenido.toString().split("\n")[i].split("###")[0]);
                      asegurado.setParentesco(fn.buscaParentesco( newcontenido.toString().split("\n")[i].split("###")[1]));
                      asegurado.setSexo(fn.sexo( newcontenido.toString().split("###")[2]) ? 1:0);                      
                      asegurado.setNacimiento(fn.formatDateMonthCadena( newcontenido.toString().split("\n")[i].split("###")[3])); 
                      asegurado.setAntiguedad(fn.formatDateMonthCadena( newcontenido.toString().split("\n")[i].split("###")[4])); 
                      asegurados.add(asegurado);
				 }
			}
            modelo.setAsegurados(asegurados);
            
           
            inicio = contenido.indexOf("COBERTURAS Y LÍMITES");
            fin = contenido.lastIndexOf("COBERTURAS###COBERTURAS");
            newcontenido = new StringBuilder();
           	
           
            if (inicio > 0 & fin > 0 & inicio < fin) {
            	newcontenido.append( fn.extracted(inicio, fin, contenido));    			
                for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
                    if (newcontenido.toString().split("\n")[i].contains("PLAN")) {
                        modelo.setPlan(newcontenido.toString().split("\n")[i + 1].split("###")[0]);
                    }
                }

            }
            
            inicio = contenido.indexOf("COBERTURAS###COBERTURAS");
            fin = contenido.lastIndexOf("Pagina: 1");
           
            newcontenido = new StringBuilder();
        	newcontenido.append( fn.extracted(inicio, fin, contenido));   
			List<EstructuraCoberturasModel> coberturas = new ArrayList<>();
			for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
				EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();
				if(!newcontenido.toString().split("\n")[i].contains("COBERTURAS") && !newcontenido.toString().split("\n")[i].contains("Advertencia")
						&& !newcontenido.toString().split("\n")[i].contains("financiero") && !newcontenido.toString().split("\n")[i].contains("incrementos")
						&& !newcontenido.toString().split("\n")[i].contains("reclamaciones") && newcontenido.toString().split("\n")[i].split("###").length > 1
						) {										
					if(newcontenido.toString().split("\n")[i].split("###")[0].contains("MANTENIMIENTO DE LA SALUD") 
							|| newcontenido.toString().split("\n")[i].split("###")[0].contains("SERVICIOS ODONTOLÓGICOS")
							|| newcontenido.toString().split("\n")[i].split("###")[0].contains("AYUDA DE MATERNIDAD")
							|| newcontenido.toString().split("\n")[i].split("###")[0].contains("SERVICIOS AUXILIARES")							
							) {						
						cobertura.setNombre(newcontenido.toString().split("\n")[i].split("###")[0].trim());
						cobertura.setSa(newcontenido.toString().split("\n")[i].split("###")[1].trim());
						coberturas.add(cobertura);
						
					}
				
				}
			}
			modelo.setCoberturas(coberturas);
			
			return modelo;
		} catch (Exception ex) {
			modelo.setError(GeneralSaludModel.this.getClass().getTypeName() + " - catch:" + ex.getMessage() + " | "
					+ ex.getCause());
			return modelo;
		}
		
	}
	
}
