package com.copsis.models.segurosMty;

import java.util.ArrayList;
import java.util.List;

import com.copsis.constants.ConstantsValue;
import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraAseguradosModel;
import com.copsis.models.EstructuraCoberturasModel;
import com.copsis.models.EstructuraJsonModel;
import com.itextpdf.text.pdf.PdfStructTreeController.returnType;

public class SegurosMtySalud {

	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
	// Varaibles
	private String contenido = "";
	private String newcontenido = "";
	StringBuilder newcon = new StringBuilder();
	private int inicio = 0;
	private int fin = 0;
	
	public SegurosMtySalud(String contenido) {
		this.contenido = contenido;
	}
	public EstructuraJsonModel procesar() {
		contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());
		contenido = contenido.replace("###NÚMERO ###DE ###PÓLIZA", "NÚMERO DE PÓLIZA")
				.replace("CARÁTULA ###DE ###PÓLIZA", "CARÁTULA DE PÓLIZA")
				.replace("CARÁTULA ###DE PÓLIZA", "CARÁTULA DE PÓLIZA")
				.replace("COBERTURA ###BÁSICA", "COBERTURA BÁSICA")
				.replace("CONTRA###TA###NTE", "CONTRATANTE")
				.replace("COBERTURAS ###OPCIONALES ###CON ###COSTO ", "COBERTURAS OPCIONALES CON COSTO")
				.replace("PLAN ###SUMA ###ASEGURADA", "PLAN ###SUMA ASEGURADA")
				.replace("EN ###TESTIMONIO ###DE ###LO ###CUAL", "EN TESTIMONIO DE LO CUAL")
				.replace("DERECHO ###DE PÓLIZA", "DERECHO DE PÓLIZA")
				.replace("CANAL ###DE ###VENTA", "CANAL DE VENTA")
				.replace("EN CUMPLIMIENTO A LO DISPUESTO", "EN ###CUMPLIMIENTO ###A ###LO ###DISPUESTO")
				.replace("ASEGURA###DO ###F IGURA ###G ###ÉNERO ### ###EDAD", "ASEGURADO ###TIPO DE ###GÉNERO ###EDAD")
				.replace("ASEGURA###DO ###F IGURA ###G ###ÉNERO ###EDAD", "ASEGURADO ###TIPO DE ###GÉNERO ###EDAD")
				;
			
		try {
		// tipo
		modelo.setTipo(3);
		// cia
		modelo.setCia(39);		
		modelo.setMoneda(1);
		//Datos del contrante
	

		inicio = contenido.indexOf("CARÁTULA DE PÓLIZA");
		fin = contenido.indexOf("COBERTURA BÁSICA");
		

		
		if(inicio > 0 && fin > 0 && inicio <fin) {
			newcontenido = contenido.substring(inicio, fin).replace("\r", "").replace("@@@", "");
			for (int i = 0; i < newcontenido.split("\n").length; i++) {
				if(newcontenido.split("\n")[i].contains("NÚMERO DE PÓLIZA")) {
					if(newcontenido.split("\n")[i+1].length() > 2) {
						modelo.setPoliza(newcontenido.split("\n")[i+1].trim().replace("###", ""));
					}else {
						modelo.setPoliza(newcontenido.split("\n")[i+2].trim().replace("###", ""));
					}
				}
				if(newcontenido.split("\n")[i].contains("CONTRATANTE") && newcontenido.split("\n")[i].contains("PÓLIZA")) {
					if(newcontenido.split("\n")[i+1].contains(modelo.getPoliza())) {
						modelo.setCteNombre(newcontenido.split("\n")[i+1].split(modelo.getPoliza())[0].replace("###", "").trim());
					}else {
						modelo.setCteNombre(newcontenido.split("\n")[i+1].split("TERRITORIALIDAD")[0].replace("###", "").trim());
							
					}
					
				}
				if(newcontenido.split("\n")[i].contains("DOMICILIO")){
					modelo.setCteDireccion(newcontenido.split("\n")[i+1].split("###")[0].trim() 
							+" " + newcontenido.split("\n")[i+2].split("###")[0].trim()
							+" "+ newcontenido.split("\n")[i+3].split("###")[0].trim()
							);
				}
				if(newcontenido.split("\n")[i].contains(ConstantsValue.LAS_HORAS) && newcontenido.split("\n")[i].contains("INICIA")) {
					 if(newcontenido.split("\n")[i].split(ConstantsValue.LAS_HORAS)[1].contains("###")){
						 modelo.setVigenciaDe(fn.formatDateMonthCadena(newcontenido.split("\n")[i].split(ConstantsValue.LAS_HORAS)[1].split("###")[1].trim()));
					 }	else {
						 modelo.setVigenciaDe(fn.formatDateMonthCadena(newcontenido.split("\n")[i].split(ConstantsValue.LAS_HORAS)[1].trim()));
					 }			
				 modelo.setFechaEmision(modelo.getVigenciaDe());
				}
				if(newcontenido.split("\n")[i].contains(ConstantsValue.LAS_HORAS) && newcontenido.split("\n")[i].contains("TERMINA")) {
				   if(newcontenido.split("\n")[i].split(ConstantsValue.LAS_HORAS)[1].contains("###")){
						modelo.setVigenciaA(fn.formatDateMonthCadena(newcontenido.split("\n")[i].split(ConstantsValue.LAS_HORAS)[1].split("###")[1].trim()));		
				   }else {
						modelo.setVigenciaA(fn.formatDateMonthCadena(newcontenido.split("\n")[i].split(ConstantsValue.LAS_HORAS)[1].trim()));
				   }
				
				}
				if(newcontenido.split("\n")[i].split("-").length >  3 && newcontenido.split("\n")[i].contains("C.P.") ) {
					modelo.setCp(newcontenido.split("\n")[i].split("C.P.")[1].trim().split(" ")[0]);
					modelo.setVigenciaDe(fn.formatDateMonthCadena(newcontenido.split("\n")[i].split("###")[1].trim()));
					modelo.setVigenciaA(fn.formatDateMonthCadena(newcontenido.split("\n")[i].split("###")[2].trim()));	
					 modelo.setFechaEmision(modelo.getVigenciaDe());
				}
				if( newcontenido.split("\n")[i].contains("C.P.")) {
					modelo.setCp(newcontenido.split("\n")[i].split("C.P.")[1].trim().split(" ")[0]);
				}
			}
		}


		
		
		   inicio = contenido.indexOf("PLAN ###SUMA ASEGURADA");
	         fin = contenido.indexOf("COBERTURAS OPCIONALES CON COSTO");

	         if(inicio > -1 &&  fin >  -1 && inicio < fin) {
	        		newcontenido = contenido.substring(inicio,fin).replace("\r", "").replace("@@@", "").trim();
	        		for (int i = 0; i < newcontenido.split("\n").length; i++) {	 
	        			
	        			if(newcontenido.split("\n")[i].contains("PLAN")) {
	        	
	        				if(newcontenido.split("\n")[i+4].split("###").length > 3) {
	        					if( newcontenido.split("\n")[i+4].contains("COPAGO")) {
	        						modelo.setPlan(newcontenido.split("\n")[i+1].split("###")[0].trim() +" " + newcontenido.split("\n")[i+1].split("###")[1].trim());
			    					modelo.setSa(newcontenido.split("\n")[i+1].split("###")[2].trim());
	        					}else {
	        						modelo.setPlan(newcontenido.split("\n")[i+4].split("###")[0].trim());
			    					modelo.setSa(newcontenido.split("\n")[i+4].split("###")[1].trim());
			    					modelo.setDeducible(newcontenido.split("\n")[i+4].split("###")[2].trim());
			    					modelo.setCoaseguroTope(newcontenido.split("\n")[i+4].split("###")[3].trim());
	        					}
	        			
	        				}else {
	        					newcon.append(fn.gatos( newcontenido.split("\n")[i+1]));
	        			
	        					if(newcon.toString().split("###").length == 3) {
	        						modelo.setPlan(newcontenido.split("\n")[i+1].split("###")[0].trim() +" " + newcontenido.split("\n")[i+1].split("###")[1].trim());
	        						modelo.setSa(newcontenido.split("\n")[i+1].split("###")[2].trim());
	        					}
	        					
	        					if(newcon.toString().split("###").length == 2) {
	        						modelo.setPlan(newcontenido.split("\n")[i+1].split("###")[0].trim());
	        						modelo.setSa(newcontenido.split("\n")[i+1].split("###")[1].trim());
	        					}
	        		
	        				}
	    					
	    				}
	        		}
	         }

	         inicio = contenido.indexOf("PRIMAS");
	         fin = contenido.indexOf("EN TESTIMONIO DE LO CUAL");
	         
	      
	 
	         if(inicio > 0 &&  fin >  0 && inicio < fin) {
	        		newcontenido = contenido.substring(inicio,fin).replace("\r", "").replace("@@@", "").trim().replaceAll("### ###", "###");
	        		for (int i = 0; i < newcontenido.split("\n").length; i++) {
	        			if(newcontenido.split("\n")[i].contains("FORMA DE PAGO")) {	
	        				modelo.setFormaPago(fn.formaPago(newcontenido.split("\n")[i].split("###")[1].trim()));
	        				modelo.setPrimaneta(fn.castBigDecimal(fn.cleanString( newcontenido.split("\n")[i].split("###")[3].trim())));	        				
	        			}
	        			if(newcontenido.split("\n")[i].contains("FRACCIONADO")) {
	        				if(newcontenido.split("\n")[i].split("FRACCIONADO")[1].split("###").length > 1) {
	        					modelo.setRecargo(fn.castBigDecimal(fn.cleanString( newcontenido.split("\n")[i].split("FRACCIONADO")[1].split("###")[1].trim())));
	        				}else {
	        					modelo.setRecargo(fn.castBigDecimal(fn.cleanString( newcontenido.split("\n")[i-1].split("POR PAGO")[1].split("###")[1].trim())));
	        				}
	        				
	        			}
	        			if(newcontenido.split("\n")[i].contains("DERECHO")) {
	        				modelo.setDerecho(fn.castBigDecimal(fn.cleanString( newcontenido.split("\n")[i].split("DERECHO DE PÓLIZA")[1].split("###")[1].trim())));
	        			}
	        			if(newcontenido.split("\n")[i].contains("IVA")) {
	        				modelo.setIva(fn.castBigDecimal(fn.cleanString(newcontenido.split("\n")[i].split("IVA")[1].split("###")[1].trim())));
	        			}
	        			if(newcontenido.split("\n")[i].contains("TOTAL")) {
	        				modelo.setPrimaTotal(fn.castBigDecimal(fn.cleanString( newcontenido.split("\n")[i].split("TOTAL")[1].split("###")[1].trim())));
	        			}
	        			
	        		}
	         }
	  

	         inicio = contenido.indexOf("CANAL DE VENTA");
	         fin = contenido.indexOf("EN ###CUMPLIMIENTO ###A ###LO ###DISPUESTO");
	       
	         if(inicio > 0 &&  fin >  0 && inicio < fin) {
	        		newcontenido = contenido.substring(inicio,fin).replace("\r", "").replace("@@@", "").trim().replace("### ###", "###");
	        		for (int i = 0; i < newcontenido.split("\n").length; i++) {
	        		
	        			if(newcontenido.split("\n")[i].contains("CANAL DE VENTA") && newcontenido.split("\n")[i+1].split("###").length > 2) {	 

	        					modelo.setAgente(newcontenido.split("\n")[i+1].split("###")[1]);
	        					modelo.setCveAgente(newcontenido.split("\n")[i+1].split("###")[2]);	        						      	        					
	        			}
	        		}	         
	         }
		

		inicio = contenido.indexOf("ASEGURADO ###TIPO DE ###GÉNERO ###EDAD");
		if(inicio == -1) {
			inicio = contenido.indexOf("ASEGURADO ###FIGURA ###GÉNERO ###EDAD");
		}
		fin = contenido.indexOf("COBERTURA BÁSICA");

		if(inicio > -1 && fin > -1 && inicio <fin) {
			List<EstructuraAseguradosModel> asegurados = new ArrayList<>();
			newcontenido = contenido.substring(inicio, fin).replace("\r", "").replace("@@@", "").replace("1.","").replace("2.","").replace("3.","").replace("4.","");
			for (int i = 0; i < newcontenido.split("\n").length; i++) {
				EstructuraAseguradosModel asegurado = new EstructuraAseguradosModel(); 
				
				if(newcontenido.split("\n")[i].split("-").length >  3 ) {
					
					if(newcontenido.split("\n")[i].split("###").length == 8) {
						asegurado.setNombre(newcontenido.split("\n")[i].split("###")[0].trim() +" " + newcontenido.split("\n")[i].split("###")[1].trim() +" "+newcontenido.split("\n")[i].split("###")[2].trim());
						asegurado.setParentesco( fn.parentesco( newcontenido.split("\n")[i].split("###")[3]));					
						asegurado.setSexo(fn.sexo( newcontenido.split("\n")[i].split("###")[4]) ? 1:0);
						
					}else {
						asegurado.setNombre(newcontenido.split("\n")[i].split("###")[0].trim());
						asegurado.setParentesco( fn.parentesco( newcontenido.split("\n")[i].split("###")[1]));					
						asegurado.setSexo(fn.sexo( newcontenido.split("\n")[i].split("###")[2]) ? 1:0);
					}
					
					
				
					if(newcontenido.split("\n")[i].split("###")[4].trim().contains("-")) {
						asegurado.setEdad(Integer.parseInt(newcontenido.split("\n")[i].split("###")[3].trim()));
						asegurado.setNacimiento(fn.formatDateMonthCadena(newcontenido.split("\n")[i].split("###")[4].trim()));
	            		asegurado.setFechaAlta(fn.formatDateMonthCadena(newcontenido.split("\n")[i].split("###")[5].trim()));	            		
					}else {						
						
						asegurado.setEdad(Integer.parseInt(newcontenido.split("\n")[i].split("###")[newcontenido.split("\n")[i].split("###").length -3].trim()));
	            		asegurado.setNacimiento(fn.formatDateMonthCadena(newcontenido.split("\n")[i].split("###")[newcontenido.split("\n")[i].split("###").length -2].trim()));
	            		asegurado.setFechaAlta(fn.formatDateMonthCadena(newcontenido.split("\n")[i].split("###")[newcontenido.split("\n")[i].split("###").length -1].trim()));
					}
					
            		asegurados.add(asegurado);
				}
				
			}
			modelo.setAsegurados(asegurados);
		}

		newcontenido ="";
	     inicio = contenido.indexOf("ANEXOS");
         fin = contenido.indexOf("COBERTURA BÁSICA");
  
         if(inicio > 0 &&  fin >  0 && inicio < fin) {
        		newcontenido = contenido.substring(inicio,fin).replace("\r", "").replace("@@@", "").trim();
        		
         }
         inicio = contenido.indexOf("COBERTURAS OPCIONALES CON COSTO");
         fin = contenido.indexOf("ESTE DOCUMENTO NO ES VÁLIDO COMO RECIBO");
         if(inicio > 0 &&  fin >  0 && inicio < fin) {
        	 String coberturas="";
        		newcontenido += "\n"+ contenido.substring(inicio,fin).replace("\r", "").replace("@@@", "").trim()
        				.replace("COBERTURA DE ASISTENCIA EN EL EXTRANJEROCAE", "COBERTURA DE ASISTENCIA EN EL EXTRANJERO(CAE)  ### ### ###")
        				.replace("COBERTURA DE ENFERMEDADES CATASTRÓFICAS EN EL EXTRANJEROCEC", "COBERTURA DE ENFERMEDADES CATASTRÓFICAS EN EL EXTRANJERO(CEC)  ### ### ###")
        				.replace("COBERTURA DE ELIMINACIÓN DE DEDUCIBLE POR ACCIDENTECEDA ", "COBERTURA DE ELIMINACIÓN DE DEDUCIBLE POR ACCIDENTE(CEDA)  ### ### ###")
        				.replace("COBERTURA DE PROTECCION POR FALLECIMIENTO CPF", "COBERTURA DE PROTECCION POR FALLECIMIENTO (CPF)  ### ### ###")
        				.replace("### ###", "###")
        				.replace("COBERTURAS OPCIONALES CON COSTO ", "")
        				.replace("COBERTURA ###SUMA ###DEDUCIBLE ###COASEGURO ###ASEGURADO ###PRIMA", "")
        				.replace("ASEGURADA ###CUBIERTO ", "").replace("ANEXOS", "").replace("TOTAL", "").replace("PRIMA DE LAS COBERTURAS OPCIONALES ", "");
        		
        	  	for (int i = 0; i < newcontenido.split("\n").length; i++) {
            		if(newcontenido.split("\n")[i].length() > 7) {
            			coberturas += newcontenido.split("\n")[i] +"\n";	
            		}					
				}
        	  	newcontenido = coberturas;
        	
         }


         if(newcontenido.length() ==  0 ||  newcontenido.length() < 20) {
        	 newcontenido ="";
        	 inicio = contenido.indexOf("COBERTURA BÁSICA");
             fin = contenido.indexOf("COBERTURAS OPCIONALES CON COSTO");  
           
             newcontenido = getCoberturas (inicio,fin,contenido);
         }
           


         if(inicio > 0 &&  fin >  0 && inicio < fin) {
        	 List<EstructuraCoberturasModel> coberturas = new ArrayList<>();  
         	for (int i = 0; i < newcontenido.split("\n").length; i++) {	         		 
         		EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();         		
         		if(!newcontenido.split("\n")[i].contains("ANEXOS") && !newcontenido.split("\n")[i].contains("ASEGURADO")
         		  && !newcontenido.split("\n")[i].contains("ASEGURADA") && !newcontenido.split("\n")[i].contains("CAE")
         		  && !newcontenido.split("\n")[i].contains("TOTAL") && !newcontenido.split("\n")[i].contains("CEC")
         		 && !newcontenido.split("\n")[i].contains("SUMA") && !newcontenido.split("\n")[i].contains("ESTE DOCUMENTO")
         		&& !newcontenido.split("\n")[i].contains("Paseo de la") && !newcontenido.split("\n")[i].contains("CARÁTULA DE PÓLIZ")
         		&& !newcontenido.split("\n")[i].contains("NÚMERO DE PÓLIZA") && !newcontenido.split("\n")[i].contains("PRIMA DE LA")
         		&& !newcontenido.split("\n")[i].contains("MKT OP") && !newcontenido.split("\n")[i].contains("RV-")
         		&& !newcontenido.split("\n")[i].contains("Página")
         				) {   	         				         			         	
       		          			 
              		if((newcontenido.split("\n")[i].split("###").length > 1   && newcontenido.split("\n")[i].split("###").length < 6) && newcontenido.split("\n")[i].split("###")[0].length() > 6) {    

              			 cobertura.setNombre(newcontenido.split("\n")[i].split("###")[0].trim());
             			 if(newcontenido.split("\n")[i].split("###").length == 2) {
             				cobertura.setSa(newcontenido.split("\n")[i].split("###")[1].trim());	 
             			 }else {
             				 if(newcontenido.split("\n")[i].split("###").length == 3 ) {
             					cobertura.setSa(newcontenido.split("\n")[i].split("###")[2].trim());
             				 }
             				 if(newcontenido.split("\n")[i].split("###").length == 4 ) {
             					 if(newcontenido.split("\n")[i].split("###")[3].trim().length() > 7){
             						cobertura.setSa(newcontenido.split("\n")[i].split("###")[3].trim());
             					 }else {
             						cobertura.setSa(newcontenido.split("\n")[i].split("###")[2].trim());	 
             					 }
              					
              				 }             			 
             			 }             			 
             			coberturas.add(cobertura);	
              		}         	         		
         		}         	         	
         	}
         	modelo.setCoberturas(coberturas);
         }
			return modelo;
		} catch (Exception ex) {
			ex.printStackTrace();
			modelo.setError(
					SegurosMtySalud.this.getClass().getTypeName() + " - catch:" + ex.getMessage() + " | " + ex.getCause());
			return modelo;
		}
		
	
	}
	

	public String getCoberturas(int inicio,int fin,String cobertura) {

		String coberturastx ="";
            if(inicio > 0 &&  fin >  0 && inicio < fin) {
            	cobertura = cobertura.substring(inicio,fin).replace("\r", "").replace("@@@", "").replace("###1", "")
           				.replace("ASEGURA###DO", "").trim()
           				.replace("ASEGURA###DA", "ASEGURADA")
           				.replace("### ### ### ###", "")
           				.replace("### ###", "###")
           				.replace(" ###", "###");

            	
            	for (int i = 0; i < cobertura.split("\n").length; i++) {
            		if(cobertura.split("\n")[i].length() > 7) {
            			coberturastx += cobertura.split("\n")[i]+"\n";	
            		}
					
				}
            }
            return coberturastx;
      }
		
}

