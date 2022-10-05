package com.copsis.models.afirme;

import java.util.ArrayList;
import java.util.List;

import com.copsis.constants.ConstantsValue;
import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraCoberturasModel;
import com.copsis.models.EstructuraJsonModel;


public class AfirmeAutosModel {
	// Clases
	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
	// Varaibles
	private String contenido = "";
	

	public AfirmeAutosModel(String contenido) {
		this.contenido = contenido;
	}

	public EstructuraJsonModel procesar() {
		try {
			int splitxt = 0;
			StringBuilder newcontenidotx= new StringBuilder();
			StringBuilder newdireccion = new StringBuilder();
			String newcontenido = "";
			int inicio = 0;
			int fin = 0;
			
			contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());
			contenido = contenido.replace("Prima neta", ConstantsValue.PRIMA_NETA )
					.replace("Póliza:", ConstantsValue.POLIZA);

            modelo.setTipo(1);
            modelo.setCia(31);


            //Datos Generales
            inicio = contenido.indexOf("PÓLIZA DE SEGURO");
            fin = contenido.indexOf("DESGLOSE DE COBERTURAS");
          
            if (inicio > 0 && fin > 0 && inicio < fin) {
				newcontenido = contenido.substring(inicio, fin).replace("\r", "").replace("@@@", "");
				for (int i = 0; i < newcontenido.split("\n").length; i++) {			
					if(newcontenido.split("\n")[i].contains(ConstantsValue.POLIZA) && newcontenido.split("\n")[i].contains(ConstantsValue.INCISO)) {
						modelo.setPoliza(newcontenido.split("\n")[i].split(ConstantsValue.POLIZA)[1].split(ConstantsValue.INCISO)[0].replace("-", "").replace("###", "").trim());
						modelo.setPolizaGuion(newcontenido.split("\n")[i].split(ConstantsValue.POLIZA)[1].split(ConstantsValue.INCISO)[0].replace("###", "").trim());
					}
					if(newcontenido.split("\n")[i].contains("Desde:") && newcontenido.split("\n")[i].contains("Hasta:") && newcontenido.split("\n")[i+1].split("-").length > 2) {						
						modelo.setVigenciaDe(fn.formatDateMonthCadena(newcontenido.split("\n")[i+1].split("###")[1].replace("###", "").trim()));
						modelo.setFechaEmision(modelo.getVigenciaDe());
						modelo.setVigenciaA(fn.formatDateMonthCadena(newcontenido.split("\n")[i+1].split("###")[3].replace("###", "").trim()));
						
					}
					if(newcontenido.split("\n")[i].contains("Marca") && newcontenido.split("\n")[i].contains(ConstantsValue.CLAVE)) {
						modelo.setMarca( newcontenido.split("\n")[i].split(ConstantsValue.CLAVE)[1]);
						modelo.setClave( fn.numTx(newcontenido.split("\n")[i].split(ConstantsValue.CLAVE)[1]));
					}
					if(newcontenido.split("\n")[i].contains("ASEGURADO") && newcontenido.split("\n")[i+2].contains("Y-O")){
						modelo.setCteNombre(newcontenido.split("\n")[i+2].split("Y-O")[0].trim());							
					}
					
					if(modelo.getCteNombre().length() == 0 && newcontenido.split("\n")[i].contains("Tipo y Clase:")) {		

						modelo.setCteNombre( newcontenido.split("\n")[i].split("Tipo y Clase:")[0].replace("###", "").trim());
					}
					
					if(newcontenido.split("\n")[i].contains("Modelo")){
						modelo.setModelo(Integer.parseInt(newcontenido.split("\n")[i].split("Modelo:")[1].split("###")[1]));
						newdireccion.append(newcontenido.split("\n")[i].split("Modelo:")[0].replace("###", ""));
					}
					if(newcontenido.split("\n")[i].contains("Motor")){
						newdireccion.append(newcontenido.split("\n")[i].split("Motor")[0].replace("###", ""));
						modelo.setCteDireccion(newdireccion.toString().trim());
					}
					if(newcontenido.split("\n")[i].contains("Serie:")){
						modelo.setSerie(newcontenido.split("\n")[i].split("Serie:")[1].replace("###", "").trim());
					}
					if(newcontenido.split("\n")[i].contains("C.P.")){
						modelo.setCp(newcontenido.split("\n")[i].split("C.P.")[1].split("###")[1].replace("###", "").trim());
					}
					if(newcontenido.split("\n")[i].contains("R.F.C.")){
						modelo.setRfc(newcontenido.split("\n")[i].split("R.F.C.")[1].split("###")[1].replace("###", "").trim());
					}
					if(newcontenido.split("\n")[i].contains("ID.")){
						modelo.setIdCliente(newcontenido.split("\n")[i].split("ID.")[1].split("###")[1].replace("###", "").trim());
					}
					
				}
            }
            
            /*Agente y Clave*/
   
            inicio = contenido.indexOf(ConstantsValue.CLAVE_CONDUCTO);
            fin = contenido.indexOf("Artículo 20 de");
       
            if (inicio > 0 && fin > 0 && inicio < fin) {
            	newcontenido = contenido.substring(inicio, fin).replace("\r", "").replace("@@@", "");
				for (int i = 0; i < newcontenido.split("\n").length; i++) {
					if (newcontenido.split("\n")[i].contains(ConstantsValue.CLAVE_CONDUCTO)) {			
						String x= newcontenido.split("\n")[i].split(ConstantsValue.CLAVE_CONDUCTO)[1].replace(" ", "###");
						modelo.setCveAgente(x.split("###")[1].trim());
						modelo.setAgente(x.split(modelo.getCveAgente())[1].replace("###", " "));
						
					}
				}
            	
            }
            
            
        
            if(contenido.contains("INFORMACIÓN GENERAL DE LA PÓLIZA") && contenido.contains("DESGLOSE DE COBERTURAS") ) {
                inicio = contenido.indexOf("INFORMACIÓN GENERAL DE LA PÓLIZA");
                fin = contenido.indexOf("ESGLOSE DE COBERTURAS");                
                newcontenidotx.append(fn.extracted(inicio, fin, contenido));
    			for(int i =0; i < newcontenidotx.toString().split("\n").length ; i++) {    			
    				if(newcontenidotx.toString().split("\n")[i].contains("Y-O")){  
    
    					String x = newcontenidotx.toString().split("\n")[i].replace(" ", "###");
    				
    					
    					switch(x.split("###").length) {
    					case 1:
    						   modelo.setNomina(x.split("###")[1]);
      					     if(modelo.getNomina().contains( newcontenidotx.toString().split("\n")[i])) {
      	                        	modelo.setCteNomina( newcontenidotx.toString().split("\n")[i].split(modelo.getNomina())[1].split("###")[0].trim());	
      	                        }
    						break;
    					case 2:
    					    modelo.setNomina(x.split("###")[2]);
    					     if(modelo.getNomina().contains( newcontenidotx.toString().split("\n")[i])) {
    	                        	modelo.setCteNomina( newcontenidotx.toString().split("\n")[i].split(modelo.getNomina())[1].split("###")[0].trim());	
    	                        }
    						break;
    						
    					case 9:
    					    modelo.setNomina(x.split("###")[7]);
    					     if(modelo.getNomina().contains(x.split("###")[7])) {    					       					    
    	                        	modelo.setCteNomina(newcontenidotx.toString().split("\n")[i].split(modelo.getNomina())[0].replace("NO.", "").replace("Y-O", "").trim());	
    	                        }
    						break;
    						
    					}
           
                                                                   
                    }
    				if(newcontenidotx.toString().split("\n")[i].contains("Placas")){
    					modelo.setPlacas(newcontenidotx.toString().split("\n")[i].split("Placas")[1].replace("###", ""));
    				}    				
    			}
            	
            }
            

      
            //Primas
            inicio = contenido.indexOf(ConstantsValue.PRIMA_NETA);
            fin = contenido.indexOf("Artículo 25 de");
       
            if (inicio > 0 && fin > 0 && inicio < fin) {
				newcontenido = contenido.substring(inicio, fin).replace("\r", "").replace("@@@", "");
				for (int i = 0; i < newcontenido.split("\n").length; i++) {
					if (newcontenido.split("\n")[i].contains(ConstantsValue.PRIMA_NETA)) {
						modelo.setPrimaneta( fn.castBigDecimal(fn.castDouble(newcontenido.split("\n")[i].split(ConstantsValue.PRIMA_NETA)[1].split("###")[1])));
					}
					if (newcontenido.split("\n")[i].contains("Financiamiento")) {						
						modelo.setRecargo( fn.castBigDecimal(fn.castDouble(newcontenido.split("\n")[i].split("Financiamiento")[1].split("###")[1])));
					}
					
					if (newcontenido.split("\n")[i].contains("Gastos Expedición")) {
						modelo.setDerecho( fn.castBigDecimal(fn.castDouble(newcontenido.split("\n")[i].split("Gastos Expedición")[1].split("###")[1])));
					}
					if (newcontenido.split("\n")[i].contains("I.V.A.")) {
						modelo.setIva( fn.castBigDecimal(fn.castDouble(newcontenido.split("\n")[i].split("I.V.A.")[1].split("###")[1])));
					}
					if (newcontenido.split("\n")[i].contains("Prima total")) {
						modelo.setPrimaTotal( fn.castBigDecimal(fn.castDouble(newcontenido.split("\n")[i].split("Prima total")[1].split("###")[1])));
					}
					
					if (newcontenido.split("\n")[i].contains("Moneda")) {
					
						modelo.setMoneda(fn.moneda(newcontenido.split("\n")[i].split("Moneda")[1].split("###")[1] ));
					}
					if (newcontenido.split("\n")[i].contains("Forma de pago")) {
						modelo.setFormaPago(fn.formaPago(newcontenido.split("\n")[i].split("Forma de pago")[1].split("###")[1] ));
					}
					if(newcontenido.split("\n")[i].contains("Forma de pago") && modelo.getFormaPago() == 0) {					
						modelo.setFormaPago(fn.formaPagoSring(newcontenido.split("\n")[i].split("Forma de pago")[1].split("###")[1] ));
					}
				}
            }
            
          
            //Coberturas
            inicio = contenido.indexOf("DESGLOSE DE COBERTURAS");
            fin = contenido.indexOf(ConstantsValue.PRIMA_NETA);
            if (inicio > 0 && fin > 0 && inicio < fin) {
            	  List<EstructuraCoberturasModel> coberturas = new ArrayList<>();
				newcontenido = contenido.substring(inicio, fin).replace("\r", "").replace("@@@", "");
				for (int i = 0; i < newcontenido.split("\n").length; i++) {
					EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();
					int sp  = newcontenido.split("\n")[i].split("###").length;
					if(!newcontenido.split("\n")[i].contains("Deducible")) {						
						switch (sp) {
						case 4:
							cobertura.setNombre(newcontenido.split("\n")[i].split("###")[1]);
							cobertura.setSa(newcontenido.split("\n")[i].split("###")[2]);
							coberturas.add(cobertura);
							break;
						case 5:
							cobertura.setNombre(newcontenido.split("\n")[i].split("###")[1]);
							cobertura.setSa(newcontenido.split("\n")[i].split("###")[2]);
							cobertura.setDeducible(newcontenido.split("\n")[i].split("###")[3]);
							coberturas.add(cobertura);
							break;
							
						default:
							break;
						}
					}
				}
				modelo.setCoberturas(coberturas);
            }
            modelo.setCteNomina(modelo.getCteNombre());
            inicio =     contenido.indexOf("PÓLIZA DE SEGURO PARA");
            fin = contenido.lastIndexOf("DESGLOSE DE COBERTURAS");
           

            if (inicio > -1 & fin > -1 & inicio < fin) {
                newcontenido = contenido.substring(inicio, fin).replace("@@@", "").replace("\r", "");
                for (int i = 0; i < newcontenido.split("\n").length; i++) {
                	
                	 if (newcontenido.split("\n")[i].contains("PÓLIZA DE SEGURO PARA") & newcontenido.split("\n")[i].contains("Poliza:")) {
                		
                		 switch (newcontenido.split("\n")[i].split("Poliza:")[1].split("###").length){
						case 3:
							 modelo.setInciso(fn.castInteger(newcontenido.split("\n")[i].split("Poliza:")[1].split("###")[2]));
							break;
							  
							case 4:
							   modelo.setInciso(fn.castInteger(newcontenido.split("\n")[i].split("Poliza:")[1].split("###")[3]));
							break;

						default:
							break;
						}
                		
                	 }
                }
            }
			return modelo;
		} catch (Exception ex) {
			modelo.setError(AfirmeAutosModel.this.getClass().getTypeName() + " - catch:" + ex.getMessage() + " | "
					+ ex.getCause());
			return modelo;
		}
	}

}
