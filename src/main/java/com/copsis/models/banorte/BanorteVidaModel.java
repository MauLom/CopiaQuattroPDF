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
					   modelo.setFechaEmision(fn.formatDateMonthCadena(newcontenido.split("\n")[i+1].split("###")[1]));
					   modelo.setVigenciaDe(fn.formatDateMonthCadena(newcontenido.split("\n")[i+1].split("###")[2]));
					   modelo.setVigenciaA(fn.formatDateMonthCadena(newcontenido.split("\n")[i+1].split("###")[3]));
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
		
			if (inicio > -1 && fin > -1 && inicio < fin) {
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
        
            if(inicio > -1 &&  fin >  -1 && inicio < fin) {
        		List<EstructuraAseguradosModel> asegurados = new ArrayList<>();
            	newcontenido = contenido.substring(inicio, fin).replace("\r","").replace("@", "");
            	for (int i = 0; i < newcontenido.split("\n").length; i++) {
            		EstructuraAseguradosModel asegurado = new EstructuraAseguradosModel();

            		if(newcontenido.split("\n")[i].contains("Nombre")) {            			
            			asegurado.setNombre(newcontenido.split("\n")[i+1].split("###")[0]);
            			asegurado.setNacimiento(fn.formatDateMonthCadena(newcontenido.split("\n")[i+1].split("###")[1]));//              
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
            			
            			
            			if(sp == 2) {
            				cobertura.setNombre( newcontenido.split("\n")[i].split("###")[0]);
            				cobertura.setSa( newcontenido.split("\n")[i].split("###")[1]);
            				coberturas.add(cobertura);
            			} 
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
            if(inicio > -1 &&  fin >  -1 && inicio < fin) {
        		List<EstructuraBeneficiariosModel> beneficiarios = new ArrayList<>();
            	newcontenido = contenido.substring(inicio, fin).replace("\r","").replace("@", "").replace("  +", "###");
            	for (int i = 0; i < newcontenido.split("\n").length; i++) {
            		EstructuraBeneficiariosModel beneficiario = new EstructuraBeneficiariosModel();
            		if(newcontenido.split("\n")[i].contains("BENEFICIARIOS") || newcontenido.split("\n")[i].contains("NOMBRES")) {            			
            		}else {
            			int sp = newcontenido.split("\n")[i].split("###").length;
            			if(sp == 3) {
                			beneficiario.setNombre(newcontenido.split("\n")[i].split("###")[0]);
                			beneficiario.setParentesco(fn.parentesco( newcontenido.split("\n")[i].split("###")[1]));
                			beneficiario.setPorcentaje(fn.castInteger( newcontenido.split("\n")[i].split("###")[2]));
                			beneficiarios.add(beneficiario);
            			}

            		}
            	}
        		modelo.setBeneficiarios(beneficiarios);
            }
          
            //Recibos
            
            List<EstructuraRecibosModel> recibosList = new ArrayList<>();    	    
			EstructuraRecibosModel recibo = new EstructuraRecibosModel();

            switch (modelo.getFormaPago()) {
			case 1:		
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
				
	        	modelo.setRecibos(recibosList);
				break;
		
        	}
        
       
          
            
            
			return modelo;
		} catch (Exception ex) {
			modelo.setError(
					BanorteVidaModel.this.getClass().getTypeName() + " - catch:" + ex.getMessage() + " | " + ex.getCause());
			return modelo;
		}
	}
	
	
	
	
}
