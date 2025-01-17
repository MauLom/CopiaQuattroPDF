package com.copsis.models.afirme;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.copsis.constants.ConstantsValue;
import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraCoberturasModel;
import com.copsis.models.EstructuraJsonModel;
import com.copsis.models.EstructuraRecibosAfirmeModel;

public class AfirmeAutosBModel {
	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
	private String contenido = "";
    private String recibos = "";
    private static final String CLAVE_REGEX = "(Clave\\s*:\\s*'+(\\w+)'+)"; 
    private static final String PLACAS_REGEX = "(Placas\\s*:\\s*'+(\\w+)'+)"; 

    
	public AfirmeAutosBModel(String contenido,String recibos) {
		this.contenido = contenido;
        this.recibos = recibos;
	}
	
	public EstructuraJsonModel procesar() {
		 String newcontenido = "";		
         String nombreCte="";
		 int inicio = 0;
		 int fin = 0;
		 boolean recargo= false;

		try {
			contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());
			contenido = contenido.replace("I.V.A:", ConstantsValue.IVA2).replace("DATOS ###DEL ASEGURADO", ConstantsValue.DATOS_ASEGURADOMY);

      
            modelo.setTipo(1);
            modelo.setCia(31);
            
            inicio = contenido.indexOf("Póliza");
            fin = contenido.indexOf(ConstantsValue.DATOS_ASEGURADOMY);

            if (inicio > 0 && fin > 0 && inicio < fin) {
                newcontenido = contenido.substring(inicio, fin).replaceAll("12:00 HRS.", "").replace("12:00 Hrs.", "");
                for (int i = 0; i < newcontenido.split("\n").length; i++) {

                    if (newcontenido.split("\n")[i].contains("Póliza") && newcontenido.split("\n")[i].contains(ConstantsValue.INCISO)) {
                        modelo.setPoliza(newcontenido.split("\n")[i].split("###")[1].split(ConstantsValue.INCISO)[0]);
                        modelo.setInciso(Integer.parseInt(newcontenido.split("\n")[i].split(ConstantsValue.INCISO)[1].replace("###", "").trim()));
                    }

                   
                    if (newcontenido.split("\n")[i].contains("desde")) {
                        modelo.setVigenciaDe(fn.formatDateMonthCadena(fn.obtenVigePoliza(newcontenido.split("\n")[i]).get(0)));
                        if(modelo.getVigenciaDe().length() == 0) {
                        modelo.setVigenciaDe(fn.formatDateMonthCadena(newcontenido.split("\n")[i].split("###")[1].trim()));
                        }
                    }
                    if (newcontenido.split("\n")[i].contains("Hasta")) {
                        modelo.setVigenciaA(fn.formatDateMonthCadena(fn.obtenVigePoliza(newcontenido.split("\n")[i]).get(0)));
                        if(modelo.getVigenciaA().length() == 0) {
                        modelo.setVigenciaA(fn.formatDateMonthCadena(newcontenido.split("\n")[i].split("###")[1].trim()));
                        }
                    }
                    if (newcontenido.split("\n")[i].contains("Emisión")) {
                        modelo.setFechaEmision(fn.formatDateMonthCadena(fn.obtenVigePoliza(newcontenido.split("\n")[i]).get(0)));
                        if(modelo.getFechaEmision().length() == 0) {
                        modelo.setFechaEmision(fn.formatDateMonthCadena(newcontenido.split("\n")[i].split("###")[1].trim()));
                        }
                    }
                }
            }

            if (modelo.getVigenciaDe().length() > 0) {
                modelo.setFechaEmision(modelo.getVigenciaA());
            }
            
            inicio = contenido.indexOf("DATOS DEL ASEGURADO");
            fin = contenido.indexOf("DATOS DEL VEHÍCULO");
            StringBuilder newcontenidosp = new StringBuilder();
            if (inicio > 0 && fin > 0 && inicio < fin) {
                newcontenido = contenido.substring(inicio, fin).replace("@@@", "").replace("###", " ");
                for (int i = 0; i < newcontenido.split("\n").length; i++) {

                    if (newcontenido.split("\n")[i].contains("Domicilio:") && newcontenido.split("\n")[i].contains(ConstantsValue.RFC)) {
                        newcontenidosp.append( newcontenido.split("\n")[i].split("Domicilio:")[1].split(ConstantsValue.RFC)[0]); 
                        

                        if(!newcontenido.split("\n")[i+1].contains("Contratante")) {                        	
                        
                        	 newcontenidosp.append( " " + newcontenido.split("\n")[i + 1]);
                        }	                        		                       
                        modelo.setCteDireccion(newcontenidosp.toString().replace("\r", "").trim());
                        modelo.setRfc(newcontenido.split("\n")[i].split(ConstantsValue.RFC)[1].replace("\r", "").trim());
                    }

                    if (newcontenido.split("\n")[i].contains(ConstantsValue.CONTRATANTE) && newcontenido.split("\n")[i].contains("C.P:")) {
                        if(newcontenido.split("\n")[i].split(ConstantsValue.CONTRATANTE)[1].split("C.P:")[0].trim().contains(",") &&
                        newcontenido.split("\n")[i].split(ConstantsValue.CONTRATANTE)[1].split(",")[1].length() > 20) {
                 
                            modelo.setCteNombre(
                          		(	newcontenido.split("\n")[i].split(ConstantsValue.CONTRATANTE)[1].split("C.P:")[0].trim().split(",")[1] +" "
                          			+ newcontenido.split("\n")[i].split(ConstantsValue.CONTRATANTE)[1].split("C.P:")[0].trim().split(",")[0]).trim()
                          			);
                        }else {
                          	modelo.setCteNombre(newcontenido.split("\n")[i].split(ConstantsValue.CONTRATANTE)[1].split("C.P:")[0].trim());
                        }
                 
                        modelo.setCp(newcontenido.split("\n")[i].split("C.P:")[1].replace("\r", "").trim());
                        
                        if(modelo.getCp().length() == 4) {
                            modelo.setCp("0" + modelo.getCp());
                        }
                   
                    }


                    if(modelo.getCteNombre().length() == 0 && newcontenido.split("\n")[i].contains("Nombre")) {
                        nombreCte =  newcontenido.split("\n")[i].split("Nombre:")[1].trim();
                       
                    }

                }

            }
            
            if(modelo.getCteNombre().length()   <  10) {
              modelo.setCteNombre(nombreCte);
            }

            /*poliza*/
            inicio = contenido.indexOf("DATOS DEL VEHÍCULO");
            fin = contenido.indexOf("COBERTURAS ");
            if (inicio > 0 && fin > 0 && inicio < fin) {
                newcontenido = contenido.substring(inicio, fin).replace("@@@", "");
                for (int i = 0; i < newcontenido.split("\n").length; i++) {

                    if (newcontenido.split("\n")[i].contains("Marca") && newcontenido.split("\n")[i].contains(ConstantsValue.MODELO) && newcontenido.split("\n")[i].contains("Tipo:")) {
                        modelo.setMarca(newcontenido.split("\n")[i].split("Marca:")[1].split(ConstantsValue.MODELO)[0].replace("###", "").trim());
                        modelo.setModelo(Integer.parseInt(newcontenido.split("\n")[i].split(ConstantsValue.MODELO)[1].split("Tipo:")[0].replace("###", "").trim()));
                    }
                    if (newcontenido.split("\n")[i].contains(ConstantsValue.VERSIONPT) && newcontenido.split("\n")[i].contains("Serie:")) {
                        modelo.setDescripcion(newcontenido.split("\n")[i].split(ConstantsValue.VERSIONPT)[1].split(ConstantsValue.NUMERO)[0].replace("###", "").trim());
                        modelo.setSerie(newcontenido.split("\n")[i].split("Serie:")[1].replace("###", "").trim());

                    }else if(modelo.getPlacas().length() == 0 && newcontenido.split("\n")[i].contains("Placas:") && (i-2)>0 && (newcontenido.split("\n")[i-2].contains("Versión") && newcontenido.split("\n")[i-1].contains("###") && (newcontenido.split("\n")[i-1].split("###").length == 2))) {
                    			modelo.setPlacas(newcontenido.split("\n")[i-1].split("###")[0]);
                    		
                    	
                    }

                }
            }
            //clave
            inicio = contenido.indexOf("Uso:");                
            if(inicio>-1) {
            	newcontenido = contenido.substring(inicio,inicio+100);
            	modelo.setClave(obtenerValoresPorRegex(CLAVE_REGEX,newcontenido));
            }
            //placas
            if(modelo.getPlacas().length() == 0) {
                modelo.setPlacas(obtenerValoresPorRegex(PLACAS_REGEX ,contenido));
            }
            //
            inicio = contenido.indexOf("COBERTURAS");
            fin = contenido.indexOf("Prima Neta");

            if (inicio > 0 && fin > 0 && inicio < fin) {
          	  List<EstructuraCoberturasModel> coberturas = new ArrayList<>();
                newcontenido = contenido.substring(inicio, fin).replace("@@@", "");

                for (String split : newcontenido.split("\n")) {
                    EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();
                    int sp = split.split("###").length;
                    if (!split.contains("COBERTURAS") && !split.contains("Clave de Agente:") && !split.contains("Pagina") && !split.contains("CARATULA DE AUTOMÓVILES RESIDENTES")
                            && !split.contains("Vigencia") && !split.contains("Hasta") && !split.contains("Fecha") 
                            && !split.contains("PRIMA")
                            && !split.contains("Número de Póliza")     
                            && !split.contains("###RESPONSABILIDAD")) {                                
                    	if (sp == 4 || sp == 5) {
                    		cobertura.setNombre(split.split("###")[0]);
                    		cobertura.setSa(split.split("###")[1]);
                    		cobertura.setDeducible(split.split("###")[2].trim());
                    		coberturas.add(cobertura);
                    	}
                    	if (sp == 3) {
                    		cobertura.setNombre(split.split("###")[0]);
                    		cobertura.setSa(split.split("###")[1]);
                    		coberturas.add(cobertura);
                    	}
                    	if (sp == 2) {
                    		cobertura.setNombre(split.split("###")[0]);
                    		coberturas.add(cobertura);
                    	}
                    }
                }
            	modelo.setCoberturas(coberturas);
            }
            
            
            inicio = contenido.indexOf("Agente:");
            fin = contenido.indexOf("Prima Total:");
            
            if (inicio > 0 && fin > 0 && inicio < fin) {
                newcontenido = contenido.substring(inicio, fin + 50).replace("@@@", "").replace("###", " ");
                for (int i = 0; i < newcontenido.split("\n").length; i++) {
   
                    if (newcontenido.split("\n")[i].contains("Agente") && newcontenido.split("\n")[i].contains("Prima")) {
                        modelo.setCveAgente(newcontenido.split("\n")[i].split("Agente")[1].split("Prima")[0].replace("###", "").replace(":", "").trim());
                        modelo.setPrimaneta(fn.castBigDecimal(fn.castDouble(newcontenido.split("\n")[i].split("Neta:")[1].replace("###", ""))));
                    }
                    if (newcontenido.split("\n")[i].contains("Nombre") && newcontenido.split("\n")[i].contains("Financiamiento")) {
                       newcontenidosp = new StringBuilder();
                        newcontenidosp.append( newcontenido.split("\n")[i].split("Nombre:")[1].split("Financiamiento")[0]);
                        modelo.setAgente(newcontenidosp.toString().replace("\r", "").replace("###", "").trim().replace(",", " "));
                        modelo.setRecargo(fn.castBigDecimal(fn.castDouble(newcontenido.split("\n")[i].split("Financiamiento:")[1].replace("###", ""))));
                      
                    }
                   
                    if (newcontenido.split("\n")[i].contains("pago") && newcontenido.split("\n")[i].contains(ConstantsValue.EXPEDICION3)) {
                        modelo.setFormaPago(fn.formaPago(newcontenido.split("\n")[i].split("pago:")[1].split(ConstantsValue.GASTOS)[0].trim()));
                       
                        modelo.setDerecho(fn.castBigDecimal(fn.castDouble(newcontenido.split("\n")[i].split(ConstantsValue.EXPEDICION2)[1].replace("###", ""))));
                    }

                    if (newcontenido.split("\n")[i].contains(ConstantsValue.GASTOS) && newcontenido.split("\n")[i].contains(ConstantsValue.EXPEDICION2)) {
                        modelo.setDerecho(fn.castBigDecimal(fn.castDouble(newcontenido.split("\n")[i].split(ConstantsValue.EXPEDICION2)[1].replace("###", ""))));
                    }

                    if (modelo.getFormaPago() ==0 && newcontenido.split("\n")[i].contains("Forma de Pago")){
                        modelo.setFormaPago(fn.formaPagoSring(newcontenido.split("\n")[i]));
                          
                    }

                    if (newcontenido.split("\n")[i].contains("Moneda") && newcontenido.split("\n")[i].contains(ConstantsValue.EXPEDICION)) {
                        modelo.setMoneda(fn.moneda(newcontenido.split("\n")[i].split("Moneda:")[1].split(ConstantsValue.GASTOS)[0].trim()));
                        modelo.setCargoExtra(fn.castBigDecimal(fn.castDouble(newcontenido.split("\n")[i].split(ConstantsValue.EXPEDICION2)[1].replace("$", ""))));
                    }


                    if (modelo.getMoneda() ==0 && newcontenido.split("\n")[i].contains("Moneda")){
                        modelo.setMoneda(fn.buscaMonedaEnTexto(newcontenido.split("\n")[i]));
                          
                    }
                    if (newcontenido.split("\n")[i].contains(ConstantsValue.IVA2)) {
                        modelo.setIva(fn.castBigDecimal(fn.castDouble(newcontenido.split("\n")[i].split(ConstantsValue.IVA2)[1].replace("###", "").trim())));
                    }
                    if (newcontenido.split("\n")[i].contains("Prima Total:")) {
                        modelo.setPrimaTotal(fn.castBigDecimal(fn.castDouble(newcontenido.split("\n")[i].split(ConstantsValue.TOTAL)[1].replace("###", ""))));
                    }
                }
            }
            
         
            if(modelo.getMoneda() == 5) {
            	modelo.setMoneda(1);
            }
            

            
            for (int i = 0; i < recibos.split(ConstantsValue.RECIBO_PRIMAS).length; i++) {
                if (recibos.split(ConstantsValue.RECIBO_PRIMAS)[i].contains("PARA USO EXCLUSIVO DEL BANCO")) {
                    newcontenido = recibos.split(ConstantsValue.RECIBO_PRIMAS)[i].split("PARA USO EXCLUSIVO DEL BANCO")[0];
                }
            }
            
            newcontenido = newcontenido.replace("@@@", "").replace("\r", "");
            List<EstructuraRecibosAfirmeModel> recibosList = new ArrayList<>();

            
            EstructuraRecibosAfirmeModel  recibo = new EstructuraRecibosAfirmeModel();
        
            
            if (modelo.getFormaPago() == 1) {
				recibo.setSerie(1);
				recibo.setTotalSerie(1);
				recibo.setVigenciaDe(modelo.getVigenciaDe());
			    recibo.setVigenciaA(modelo.getVigenciaA());
			 	recibo.setVence(modelo.getVigenciaDe());
			 	recibo.setDerecho(modelo.getDerecho());
			 	recibo.setFinanciamiento(modelo.getRecargo());
			 	recibo.setCargoExtra(modelo.getCargoExtra());
			 	recibo.setTotal(modelo.getPrimaTotal());
			 	recibo.setIva(modelo.getIva());
			 	
			}
       
            for (int i = 0; i < newcontenido.split("\n").length; i++) {
      
           	
                if (newcontenido.split("\n")[i].contains("Cubre el Periodo:") && newcontenido.split("\n")[i].contains("Del") && newcontenido.split("\n")[i].contains("Inciso")) {                           	
                    recibo.setVigenciaDe( fn.formatDateMonthCadena(newcontenido.split("\n")[i].split("Del")[1].split("Inciso")[0].replace(":", "").replace("###", "").replace("/", "-").trim()));
               
                
                    if(newcontenido.split("\n")[i+1].contains("Al")){
                        
                    	recibo.setVigenciaA( fn.formatDateMonthCadena(newcontenido.split("\n")[i+1].split("Al")[1].replace(":", "").replace("###", "").replace("/", "-").trim()));                    
                    	recibo.setVence(modelo.getVigenciaDe());
                    }                
                }
                
                 if (newcontenido.split("\n")[i].contains("Prima Neta:")) {                
                     recibo.setPrimaNeta(fn.castBigDecimal(fn.castDouble(fn.cleanString( newcontenido.split("\n")[i].split("Prima Neta:")[1].replace("###", "").trim()))));
                     if (newcontenido.split("\n")[i+1].contains("$")) {
                    	 recibo.setFinanciamiento(BigDecimal.ZERO);
                    	 recargo =true;
                     }
                 }

                 if (newcontenido.split("\n")[i].contains("Financiamiento:") && !recargo && !newcontenido.split("\n")[i].contains("$") && newcontenido.split("\n")[i + 1].contains("$")) {
                     recibo.setFinanciamiento(fn.castBigDecimal(fn.castDouble(fn.cleanString(newcontenido.split("\n")[i + 1].replace("$", "").trim()))));
                 }
       
                 if (newcontenido.split("\n")[i].contains(ConstantsValue.EXPEDICION2 ) && newcontenido.split("\n")[i].contains("$")) {
                     recibo.setDerecho(fn.castBigDecimal(fn.castDouble(fn.cleanString( newcontenido.split("\n")[i].split(ConstantsValue.EXPEDICION2)[1].replace("###", "").trim()))));
                 }
                 if (newcontenido.split("\n")[i].contains("expedición") && newcontenido.split("\n")[i].contains("Exentos")  && newcontenido.split("\n")[i].contains("IVA")){
                  recibo.setCargoExtra(fn.castBigDecimal(fn.castDouble(fn.cleanString(newcontenido.split("\n")[i].split("IVA")[1].replace(":", "").replace("###", "")))));
                 }
                 if (newcontenido.split("\n")[i].contains("tasa de 16") && newcontenido.split("\n")[i].contains("%")) {                	 
                     recibo.setIva(fn.castBigDecimal(fn.castDouble(fn.cleanString(newcontenido.split("\n")[i].split("%")[1].replace("###", "").replace(":", "").replace("$", "").trim()))));
                 }
                 if (newcontenido.split("\n")[i].contains(ConstantsValue.TOTAL) && newcontenido.split("\n")[i].contains("$")) {
                     recibo.setTotal(fn.castBigDecimal(fn.castDouble(fn.cleanString(newcontenido.split("\n")[i].split(ConstantsValue.TOTAL)[1].replace("###", "").trim()))));
                 }
             }
            recibosList.add(recibo);
            modelo.setRecibosAfirme(recibosList);
                                        
            List<EstructuraRecibosAfirmeModel> recibosvacio = new ArrayList<>();
            List<EstructuraRecibosAfirmeModel> recibosLi = new ArrayList<>();
      

            if (modelo.getFormaPago() != 1) {

            	int serietota =fn.getTotalRec(modelo.getFormaPago());
            	int c = 0;
            	 for (int i = 0; i < fn.getTotalRec(modelo.getFormaPago()); i++) {
            			c++;
          		   EstructuraRecibosAfirmeModel  recibo2 = new EstructuraRecibosAfirmeModel();  
          		   if(i == 0) {
          			   recibo2.setVigenciaDe(modelo.getRecibosAfirme().get(0).getVigenciaDe());
          			  recibo2.setVigenciaA(modelo.getRecibosAfirme().get(0).getVigenciaDe());
          		   }
          		   recibo2.setSerie(c);
          		   recibo2.setTotalSerie(serietota);
                   recibo2.setPrimaNeta(new BigDecimal(modelo.getPrimaneta().doubleValue()/serietota).setScale(2, BigDecimal.ROUND_HALF_EVEN));
                   recibo2.setCargoExtra(modelo.getRecibosAfirme().get(0).getCargoExtra());
                   recibo2.setFinanciamiento(new BigDecimal(modelo.getRecargo().doubleValue()/serietota).setScale(2, BigDecimal.ROUND_HALF_EVEN));
                  
                   
                   if(modelo.getRecibosAfirme().get(0).getFinanciamiento() != new BigDecimal(0) && i !=0) {
                  		recibo2.setDerecho(new BigDecimal(0));
                  	}else {
                  		recibo2.setDerecho(modelo.getRecibosAfirme().get(0).getDerecho());
                  	}
                  	
                	if(modelo.getRecibosAfirme().get(0).getFinanciamiento() != new BigDecimal(0) && i !=0) {
          
                	recibo2.setIva(new BigDecimal(((recibo2.getPrimaNeta().doubleValue() + recibo2.getFinanciamiento().doubleValue()))*.16).setScale(2, BigDecimal.ROUND_HALF_EVEN) );
                	}else {
                		recibo2.setIva(modelo.getRecibosAfirme().get(0).getIva());
                	}
                  	
                	if(modelo.getRecibosAfirme().get(0).getFinanciamiento() != new BigDecimal(0) && i !=0) {                  	
                  	recibo2.setTotal(new BigDecimal(((recibo2.getPrimaNeta().doubleValue() + recibo2.getFinanciamiento().doubleValue()  + recibo2.getIva().doubleValue() + recibo2.getCargoExtra().doubleValue()))).setScale(2, BigDecimal.ROUND_HALF_EVEN) );
                	}else {
                		recibo2.setTotal(modelo.getRecibosAfirme().get(0).getTotal());
                	}
                 
                  	recibosLi.add(recibo2);
                  
          	   }  
          	   modelo.setRecibosAfirme(recibosvacio);
               modelo.setRecibosAfirme(recibosLi);
            }

            
            
			
			return modelo;
		} catch (Exception ex) {
			modelo.setError(AfirmeAutosBModel.this.getClass().getTypeName() + " - catch:" + ex.getMessage() + " | "
					+ ex.getCause());
			return modelo;
		}
	}
	
	private String obtenerValoresPorRegex(String regex,String texto) {
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(texto.replace("#","'"));
		return matcher.find() ? matcher.group(2) : "";
	}

}
