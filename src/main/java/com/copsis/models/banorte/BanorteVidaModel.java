package com.copsis.models.banorte;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraAseguradosModel;
import com.copsis.models.EstructuraBeneficiariosModel;
import com.copsis.models.EstructuraCoberturasModel;
import com.copsis.models.EstructuraJsonModel;
import com.copsis.models.EstructuraRecibosModel;

public class BanorteVidaModel {
	// Clases
	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
	// Varaibles
	private String contenido = "";
	private String newcontenido = "";
	private String recibosText = "";
	private String resultado = "";
	private int inicio = 0;
	private int fin = 0;
	private int donde = 0;
	private BigDecimal restoPrimaTotal = BigDecimal.ZERO;
	private BigDecimal restoDerecho = BigDecimal.ZERO;
	private BigDecimal restoIva = BigDecimal.ZERO;
	private BigDecimal restoRecargo = BigDecimal.ZERO;
	private BigDecimal restoPrimaNeta = BigDecimal.ZERO;
	private BigDecimal restoAjusteUno = BigDecimal.ZERO;
	private BigDecimal restoAjusteDos = BigDecimal.ZERO;
	private BigDecimal restoCargoExtra = BigDecimal.ZERO;
	
	public BanorteVidaModel(String contenido,String recibos) {
		this.contenido = contenido;
		this.recibosText = recibos;
	}

	public EstructuraJsonModel procesar() {
		contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());
		recibosText = fn.remplazarMultiple(recibosText, fn.remplazosGenerales());
		try {
			 // tipo
            modelo.setTipo(5);
            // cia
            modelo.setCia(35);
            

            inicio = contenido.indexOf("PÓLIZA DE SEGURO");
            fin = contenido.indexOf("COBERTURAS Y SUMAS ASEGURADAS");
            if(inicio > 0 &&  fin >  0 && inicio < fin) {
            	newcontenido = contenido.substring(inicio, fin).replace("\r","").replace("@", "");
            	for (int i = 0; i < newcontenido.split("\n").length; i++) {

					if(newcontenido.split("\n")[i].contains("PÓLIZA") && newcontenido.split("\n")[i].contains("EMISIÓN") && newcontenido.split("\n")[i].contains("VIGENCIA")) {
					   modelo.setPoliza(newcontenido.split("\n")[i+1].split("###")[0]);
					   modelo.setFechaEmision(fn.formatDate_MonthCadena(newcontenido.split("\n")[i+1].split("###")[1]));
					   modelo.setVigenciaDe(fn.formatDate_MonthCadena(newcontenido.split("\n")[i+1].split("###")[2]));
					   modelo.setVigenciaA(fn.formatDate_MonthCadena(newcontenido.split("\n")[i+1].split("###")[3]));
					}
					if(newcontenido.split("\n")[i].contains("Nombre") && newcontenido.split("\n")[i].contains("RFC:"))
					{
						   modelo.setCteNombre(newcontenido.split("\n")[i+1].split("###")[0]);
						   modelo.setRfc(newcontenido.split("\n")[i+1].split("###")[1]);
					}
					if(newcontenido.split("\n")[i].contains("C.P:")) {
						modelo.setCp(newcontenido.split("\n")[i].split("C.P:")[1].replace("###", ""));
					}
					
					if(newcontenido.split("\n")[i].contains("Domicilio") && newcontenido.split("\n")[i].contains("Núm:")) {
						resultado += newcontenido.split("\n")[i].split("Núm:")[1];
					}
					if(newcontenido.split("\n")[i].contains("Colonia") && newcontenido.split("\n")[i].contains("localidad:")) {
						resultado +=  newcontenido.split("\n")[i].split("localidad:")[1];
					}
					if(newcontenido.split("\n")[i].contains("Municipio") && newcontenido.split("\n")[i].contains("Estado:")) {
						resultado +=  newcontenido.split("\n")[i].split("ciudad:")[1];					
						modelo.setCteDireccion(resultado.replace("###", " "));
						
					}
				}            	
            }
            
            
            //PROCESO PARA PRIMAS

            
			inicio = contenido.indexOf("PRIMA TOTAL");
			fin = contenido.indexOf("BENEFICIARIOS");
			if (inicio > 0 && fin > 0 && inicio < fin) {
				newcontenido = contenido.substring(inicio, fin).replace("\r", "").replace("@", "");
				for (int i = 0; i < newcontenido.split("\n").length; i++) {
				
					if (newcontenido.split("\n")[i].contains("TOTAL")) {
                       modelo.setPrimaneta(fn.castBigDecimal(fn.castDouble(newcontenido.split("\n")[i].split("TOTAL")[1].replace("ANUAL", "").replace("###", ""))));
					}
					if (newcontenido.split("\n")[i].contains("Moneda") && newcontenido.split("\n")[i].contains("fraccionado:")) {	
						modelo.setMoneda( fn.moneda(newcontenido.split("\n")[i].split("Moneda:")[1].split("Monto")[0].replace("###", "")));
						modelo.setRecargo(fn.castBigDecimal(fn.castDouble(newcontenido.split("\n")[i].split("fraccionado:")[1].replace("###", ""))));
					}
					if (newcontenido.split("\n")[i].contains("Pago") && newcontenido.split("\n")[i].contains("expedición")) {
						modelo.setFormaPago( fn.formaPago(newcontenido.split("\n")[i].split("Pago:")[1].split("Gastos")[0].replace("###", "")));
						modelo.setDerecho(fn.castBigDecimal(fn.castDouble(newcontenido.split("\n")[i].split("póliza:")[1].replace("###", ""))));
					}
				}
			}
            
            
            
            
            
            inicio = contenido.indexOf("DATOS DEL ASEGURADO");
            fin = contenido.indexOf("COBERTURAS Y SUMAS ASEGURADAS");
            if(inicio > 0 &&  fin >  0 && inicio < fin) {
        		List<EstructuraAseguradosModel> asegurados = new ArrayList<>();
            	newcontenido = contenido.substring(inicio, fin).replace("\r","").replace("@", "");
            	for (int i = 0; i < newcontenido.split("\n").length; i++) {
            		EstructuraAseguradosModel asegurado = new EstructuraAseguradosModel();

            		if(newcontenido.split("\n")[i].contains("Nombre")) {            			
            			asegurado.setNombre(newcontenido.split("\n")[i+1].split("###")[0]);
            			asegurado.setNacimiento(fn.formatDate_MonthCadena(newcontenido.split("\n")[i+1].split("###")[1]));//              
            			asegurados.add(asegurado);
            		}            	
            	}
            	modelo.setAsegurados(asegurados);
            }

            
            inicio = contenido.indexOf("COBERTURAS Y SUMAS ASEGURADAS");
            fin = contenido.indexOf("PRIMA TOTAL");
            if(inicio > 0 &&  fin >  0 && inicio < fin) {
        		List<EstructuraCoberturasModel> coberturas = new ArrayList<>();
            	newcontenido = contenido.substring(inicio, fin).replace("\r","").replace("@", "").replace("  +", "###");
            	for (int i = 0; i < newcontenido.split("\n").length; i++) {
            		EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();            		
            		if(newcontenido.split("\n")[i].contains("SUMAS") || newcontenido.split("\n")[i].contains("PRIMA")) {            			
            		}else {
            			int sp = newcontenido.split("\n")[i].split("###").length;
            			if(sp == 6) {
            				cobertura.setNombre( newcontenido.split("\n")[i].split("###")[0]);
            				cobertura.setSa( newcontenido.split("\n")[i].split("###")[1]);
            				coberturas.add(cobertura);
            			} 
            			if(sp == 1) {
            				if(newcontenido.split("\n")[i].contains("AMPARADA")) {
            					 cobertura.setSa("AMPARADA");
            					 cobertura.setNombre(newcontenido.split("\n")[i].split(cobertura.getSa())[0]+"ASEGURADO");
            					 coberturas.add(cobertura);
            				}
            			}            
            		}            		
            	}            	
        		modelo.setCoberturas(coberturas);            	
            }
        		
        		
            inicio = contenido.indexOf("BENEFICIARIOS");
            fin = contenido.indexOf("*Para efectos");
            if(inicio > 0 &&  fin >  0 && inicio < fin) {
        		List<EstructuraBeneficiariosModel> beneficiarios = new ArrayList<>();
            	newcontenido = contenido.substring(inicio, fin).replace("\r","").replace("@", "").replace("  +", "###");
            	for (int i = 0; i < newcontenido.split("\n").length; i++) {
            		EstructuraBeneficiariosModel beneficiario = new EstructuraBeneficiariosModel();
            		if(newcontenido.split("\n")[i].contains("BENEFICIARIOS") || newcontenido.split("\n")[i].contains("NOMBRES")) {            			
            		}else {
            			beneficiario.setNombre(newcontenido.split("\n")[i].split("###")[0]);
            			beneficiario.setParentesco(fn.parentesco( newcontenido.split("\n")[i].split("###")[1]));
            			beneficiario.setPorcentaje(fn.castInteger( newcontenido.split("\n")[i].split("###")[2]));
            			beneficiarios.add(beneficiario);
            		}
            	}
        		modelo.setBeneficiarios(beneficiarios);
            }
            
            inicio = recibosText.indexOf("Agente:");
            fin = recibosText.indexOf("Moneda:");
            if(inicio > 0 &&  fin >  0 && inicio < fin) {
            	newcontenido = recibosText.substring(inicio, fin);
            	for (int i = 0; i < newcontenido.split("\n").length; i++) {
                       System.out.println("===> " +newcontenido.split("\n")[i]);		
            	}
                                    
            }
            
            
            
            //Recibos
            
            List<EstructuraRecibosModel> recibosList = new ArrayList<>();    	    
			EstructuraRecibosModel recibo = new EstructuraRecibosModel();
			
			
			
			
            
            
            if (recibosText.length() > 0) {
				recibosList = recibosExtract();
				
			}
   
            switch (modelo.getFormaPago()) {
			case 1:
				if(recibosList.size() ==  0) {
					recibo.setReciboId("");
					recibo.setSerie("1/1");
					recibo.setVigenciaDe(modelo.getVigenciaDe());
					recibo.setVigenciaA(modelo.getVigenciaA());
					if (recibo.getVigenciaDe().length() > 0) {
						recibo.setVencimiento(fn.dateAdd(recibo.getVigenciaDe(), 30, 1));
					}
					recibo.setPrimaneta(modelo.getPrimaneta());
					recibo.setDerecho(modelo.getDerecho());
					recibo.setRecargo(modelo.getRecargo());
					recibo.setIva(modelo.getIva());
					recibo.setPrimaTotal(modelo.getPrimaTotal());
					recibo.setAjusteUno(modelo.getAjusteUno());
					recibo.setAjusteDos(modelo.getAjusteDos());
					recibo.setCargoExtra(modelo.getCargoExtra());
					recibosList.add(recibo);					
				}else {
					
					int tota_recibos =fn.getTotalRec(modelo.getFormaPago());
					if(recibosList.size() > tota_recibos) {
						for (int i = 0; i < recibosList.size(); i++) {					      
							if(i >= tota_recibos) {							
								recibosList.remove(i--);							
							}				
						}
					}				
					break;
	        	}
	        
	        	modelo.setRecibos(recibosList);
	          
				
			
				break;
			case 2:
				if (recibosList.size() == 1) {
					recibo.setSerie("2/2");
					recibo.setVigenciaDe(recibosList.get(0).getVigenciaA());
					recibo.setVigenciaA(modelo.getVigenciaA());
					recibo.setVencimiento("");
					recibo.setPrimaneta(restoPrimaNeta);
					recibo.setPrimaTotal(restoPrimaTotal);
					recibo.setRecargo(restoRecargo);
					recibo.setDerecho(restoDerecho);
					recibo.setIva(restoIva);
					recibo.setAjusteUno(restoAjusteUno);
					recibo.setAjusteDos(restoAjusteDos);
					recibo.setCargoExtra(restoCargoExtra);
					recibosList.add(recibo);

				}
				break;
			case 3:
			case 4:									
				int tota_recibos =fn.getTotalRec(modelo.getFormaPago());
				if(recibosList.size() > tota_recibos) {
					for (int i = 0; i < recibosList.size(); i++) {					      
						if(i >= tota_recibos) {							
							recibosList.remove(i--);							
						}				
					}
				}				
				break;
        	}
        
        	modelo.setRecibos(recibosList);
          
            
            
			return modelo;
		} catch (Exception ex) {
			modelo.setError(
					BanorteVidaModel.this.getClass().getTypeName() + " - catch:" + ex.getMessage() + " | " + ex.getCause());
			return modelo;
		}
	}
	
	
	private ArrayList<EstructuraRecibosModel> recibosExtract() {
		recibosText = fn.fixContenido(recibosText);
		List<EstructuraRecibosModel> recibosLis = new ArrayList<>();

		try {
			for (int i = 0; i < recibosText.split("AVISO DE COBRO").length; i++) {
		
				if(recibosText.split("AVISO DE COBRO")[i].contains("Serie:")) {

					EstructuraRecibosModel recibo = new EstructuraRecibosModel();
				String x = recibosText.split("AVISO DE COBRO")[i].split("Importe con Letra")[0];

				    if(x.contains("Prima Neta") && x.contains("Recargos")) {
				    	recibo.setPrimaneta(fn.castBigDecimal(fn.cleanString(x.split("Prima Neta")[1].split("Recargos")[0].replace(":", "").split("###")[1])));
				    
				    	recibo.setRecargo(fn.castBigDecimal(fn.cleanString(x.split("Recargos")[1].split("\n")[0].replace(":", "").replace("###", ""))));
				    	
				    	 recibosLis.add(recibo);
				    }
				    if(x.contains("Serie:")){
				    	recibo.setSerie(x.split("Serie:")[1].split("Folio")[0].replace("###", ""));
				    }
				    
				    if(x.contains("IVA")){					 
				    	recibo.setIva(fn.castBigDecimal(fn.cleanString(x.split("IVA")[1].replace("%", "").split(":")[1].split("\n")[0].replace("###", ""))));
				    } 
				    if(x.contains("Prima Total")){
				    	recibo.setDerecho(fn.castBigDecimal(fn.cleanString(x.split("Derecho Póliza")[1].split("Prima")[0].replace(":", "").split("\n")[0].replace("###", ""))));
				    	recibo.setPrimaTotal(fn.castBigDecimal(fn.cleanString(x.split("Prima Total")[1].split("\n")[0].replace(":", "").replace("###", ""))));
				    } 
				   
				}			
			}
			return (ArrayList<EstructuraRecibosModel>) recibosLis;
		} catch (Exception ex) {
			modelo.setError("recibosExtract ==> " +BanorteVidaModel.this.getClass().getTypeName() + " | " + ex.getMessage() + " | "
					+ ex.getCause());

			return (ArrayList<EstructuraRecibosModel>) recibosLis;
		}
	}
	
	
}
