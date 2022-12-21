package com.copsis.models.axa;

import java.util.ArrayList;
import java.util.List;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraAseguradosModel;
import com.copsis.models.EstructuraBeneficiariosModel;
import com.copsis.models.EstructuraCoberturasModel;
import com.copsis.models.EstructuraJsonModel;



public class AxaVida2Model {

	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
	private String contenido = "";

	public AxaVida2Model(String contenido) {
		this.contenido = contenido;
	}

	public EstructuraJsonModel procesar() {
		StringBuilder resultado = new StringBuilder();
		StringBuilder newcontenido = new StringBuilder();
		int inicio = 0;
		int fin = 0;


		contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales())
				.replace("Carátula de Póliza ", "CARATULA DE POLIZA")
				.replace("CARÁTULA DE LA PÓLIZA", "CARATULA DE POLIZA")
				.replace("Carátula de póliza ", "CARATULA DE POLIZA")
				.replace("Contratante", "contratante")
				.replace("JESU S", "JESUS")
				.replace("Coberturas amparadas", "Coberturas Amparadas").replace("Beneficiarios nombre", "Beneficiarios Nombre");
		try {
			modelo.setTipo(5);
			modelo.setCia(20);

			inicio = contenido.indexOf("CARATULA DE POLIZA");
			fin = contenido.indexOf("Coberturas Amparadas");

			List<EstructuraAseguradosModel> asegurados = new ArrayList<>();
			EstructuraAseguradosModel asegurado = new EstructuraAseguradosModel();
			
			if (inicio > -1 && fin > -1 && inicio < fin) {
				resultado.append( contenido.substring(inicio,fin).replace("@@@", "").replace("\r", ""));
				 for (int i = 0; i < resultado.toString().split("\n").length; i++) {	

				    if(resultado.toString().split("\n")[i].contains("contratante") && resultado.toString().split("\n")[i].contains("Póliza") && resultado.toString().split("\n")[i+1].contains(",")) {
					 		modelo.setCteNombre((resultado.toString().split("\n")[i+1].split("###")[1].split(",")[1] +" "+resultado.toString().split("\n")[i+1].split("###")[1].split(",")[0]).trim());
					        modelo.setPoliza(resultado.toString().split("\n")[i+1].split("###")[2]); 
				    }
				    
				    if(modelo.getPoliza().length()  == 0 && modelo.getCteNombre().length() == 0) {
				    	if(resultado.toString().split("\n")[i].contains("contratante") && resultado.toString().split("\n")[i].contains("Póliza")) {				    	
				    		modelo.setCteNombre(resultado.toString().split("\n")[i+1].split("###")[1]);
					        modelo.setPoliza(resultado.toString().split("\n")[i+1].split("###")[2]); 
				    	}
				    }
					if(modelo.getPoliza().length()  == 0 ){
						if(resultado.toString().split("\n")[i].contains("Póliza")){
                      		 modelo.setPoliza(resultado.toString().split("\n")[i].split("###")[1]);
							
						}
					}
					if(modelo.getCteNombre().length()  == 0 ){
						if(resultado.toString().split("\n")[i].contains("Nombre")){
							String nombre = resultado.toString().split("\n")[i].split("###")[1];
							if(nombre.split(",").length > 0){
								modelo.setCteNombre((nombre.split(",")[1]+" "+ nombre.split(",")[0]).trim());
							}else{
								modelo.setCteNombre(nombre);
							}
                      	
						}
					}
				    
				    if(resultado.toString().split("\n")[i].contains("Domicilio:") && resultado.toString().split("\n")[i].contains("Tipo de Plan")) {
				    	modelo.setPlan(resultado.toString().split("\n")[i+1].split("###")[resultado.toString().split("\n")[i+1].split("###").length -2]);
				    	newcontenido.append( resultado.toString().split("\n")[i].split("Domicilio:")[1].split("Tipo de Plan")[0]);
				    	if(modelo.getPlan().length() > 0) {				    		
				    		newcontenido.append( resultado.toString().split("\n")[i+1].split(modelo.getPlan())[0]);
				    	}				    	
				    	modelo.setCteDireccion(newcontenido.toString().replace("###", ""));
				    }

					if(modelo.getCteDireccion().length()  == 0 ){
						if(resultado.toString().split("\n")[i].contains("Domicilio") && resultado.toString().split("\n")[i].contains("C.P.")){
							StringBuilder direccion = new StringBuilder();
							direccion.append( resultado.toString().split("\n")[i].split("###")[1]);
							direccion.append(resultado.toString().split("\n")[i+1].replace("###", ""));
							direccion.append(resultado.toString().split("\n")[i+2].split("###")[0].replace("###", ""));
					
						modelo.setCteDireccion(direccion.toString());
                      	
						}
					}
					if(modelo.getCteDireccion().length()  == 0 ){
						if(resultado.toString().split("\n")[i].contains("Domicilio") ){
							StringBuilder direccion2 = new StringBuilder();
							direccion2.append( resultado.toString().split("\n")[i].split("###")[0]);
							direccion2.append(" "+ resultado.toString().split("\n")[i+1].split("###")[0].replace("###", ""));
							direccion2.append( " "+resultado.toString().split("\n")[i+2].split("###")[0].split("###")[0].replace("###", ""));
							direccion2.append( " "+resultado.toString().split("\n")[i+3].split("###")[0].split("###")[0].replace("###", ""));
							modelo.setCteDireccion(direccion2.toString().replace("Domicilio:","").trim());
							if(modelo.getCteDireccion().length() > 50){
								modelo.setCp(fn.obtenerCPRegex2(direccion2.toString()));
							}
						}
					}
				    
				    if(resultado.toString().split("\n")[i].contains("Moneda")) {
				    	
				    	modelo.setMoneda(fn.moneda( resultado.toString().split("\n")[i].split("Moneda")[1].replace("###", "").trim()));
				    }
				    if(resultado.toString().split("\n")[i].contains("Agente")) {
				    	modelo.setAgente(resultado.toString().split("\n")[i].split("###")[2]);
				    	modelo.setCveAgente(resultado.toString().split("\n")[i].split("###")[1]);
				    }
				    if(resultado.toString().split("\n")[i].contains("Forma de pago") && resultado.toString().split("\n")[i].contains("CARGO")) {
				    	modelo.setFormaPago(4);
				    }
				    if(resultado.toString().split("\n")[i].contains("Forma de pago") && resultado.toString().split("\n")[i].contains("AGENTE")) {
				    	modelo.setFormaPago(1);
				    }
					if(modelo.getFormaPago() == 0){
						modelo.setFormaPago(fn.formaPagoSring(resultado.toString().split("\n")[i+1]));								
					}

				    if(resultado.toString().split("\n")[i].contains("Fecha de inicio") && resultado.toString().split("\n")[i].split("inicio")[1].length() > 10) {
				    	modelo.setVigenciaDe(fn.formatDateMonthCadena( resultado.toString().split("\n")[i].split("inicio")[1].replace("###", "").trim()));
				        modelo.setFechaEmision(modelo.getVigenciaDe());
				    }

					if(resultado.toString().split("\n")[i].contains("Inicio de Vigencia")) {
				    	modelo.setVigenciaDe(fn.formatDateMonthCadena( resultado.toString().split("\n")[i].split("Vigencia")[1].replace("###", "").trim()));
				        modelo.setFechaEmision(modelo.getVigenciaDe());
				    }
				    if(resultado.toString().split("\n")[i].contains("Fecha de fin") && resultado.toString().split("\n")[i].contains("R.F.C:") && resultado.toString().split("\n")[i].contains("Teléfono")) {
				    	modelo.setRfc(resultado.toString().split("\n")[i].split("R.F.C:")[1].split("Teléfono")[0].replace("###", "") );
				    	modelo.setVigenciaA(fn.formatDateMonthCadena( resultado.toString().split("\n")[i].split("Fecha de fin")[1].replace("###", "").trim()));
				    }
					
					if(modelo.getVigenciaDe().length() == 0  && resultado.toString().split("\n")[i].contains("Fecha de inicio de") && resultado.toString().split("\n")[i+1].contains("vigencia")  ){
						
						modelo.setVigenciaDe(fn.formatDateMonthCadena( resultado.toString().split("\n")[i+1].split("vigencia")[1].replace("###", "").trim()));
						modelo.setFechaEmision(modelo.getVigenciaDe());
					}

					if(resultado.toString().split("\n")[i].contains("Tipo de plan") && resultado.toString().split("\n")[i+1].contains("Domicilio") && resultado.toString().split("\n")[i+1].split("###").length == 3 ){

					modelo.setPlan(resultado.toString().split("\n")[i+1].split("###")[1]);
					}

				
	

					if(resultado.toString().split("\n")[i].contains("Prima") ) {
						List<String> valores = fn.obtenerListNumeros(resultado.toString().split("\n")[i]);
						if(valores.size() > 0){
							modelo.setPrimaneta(fn.castBigDecimal(fn.castDouble(valores.get(0))));
							modelo.setPrimaTotal(modelo.getPrimaneta());
						}
					
					}
					if(resultado.toString().split("\n")[i].contains("C.P") ) {
					modelo.setCp(resultado.toString().split("\n")[i].split("C.P.")[1].replace("###", "").substring(0,5));
					}
		
					if(resultado.toString().split("\n")[i].contains("R.F.C.") ) {
						modelo.setRfc(resultado.toString().split("\n")[i].split("R.F.C.")[1].replace("###", ""));
						}

						if(resultado.toString().split("\n")[i].contains("R.F.C:") ) {
							modelo.setRfc(resultado.toString().split("\n")[i].split("R.F.C:")[1].split("###")[1].replace("###", ""));
					}	
					
				    if(resultado.toString().split("\n")[i].contains("Promotor") && resultado.toString().split("\n")[i].contains("Prima")) {
				    	modelo.setPrimaneta(fn.castBigDecimal(fn.castDouble( resultado.toString().split("\n")[i].split("Prima")[1].split("###")[1])));
				    }
				    if(resultado.toString().split("\n")[i].contains("fraccionado")) {
				    	modelo.setRecargo(fn.castBigDecimal(fn.castDouble( resultado.toString().split("\n")[i].split("fraccionado")[1].split("###")[1])));
				    }
				    
				    if(resultado.toString().split("\n")[i].contains("Prima") && resultado.toString().split("\n")[i].contains("mensual") && resultado.toString().split("\n")[i].contains("total")) {				    	
				    	modelo.setPrimaTotal(fn.castBigDecimal(fn.castDouble( resultado.toString().split("\n")[i].split("Prima")[1].split("###")[1])));
				    }
				    if(resultado.toString().split("\n")[i].contains("Prima") && resultado.toString().split("\n")[i].contains("anual") && resultado.toString().split("\n")[i].contains("total")) {				    	
				    	modelo.setPrimaTotal(fn.castBigDecimal(fn.castDouble( resultado.toString().split("\n")[i].split("Prima")[1].split("###")[1])));
				    }
					if(resultado.toString().split("\n")[i].contains("Prima") && resultado.toString().split("\n")[i].contains("ANUAL") && resultado.toString().split("\n")[i].contains("total")) {				    	
				    	modelo.setPrimaTotal(fn.castBigDecimal(fn.castDouble( resultado.toString().split("\n")[i].split("Prima")[1].split("###")[1])));
				    }
			
				    if( resultado.toString().split("\n")[i].contains("asegurado") && resultado.toString().split("\n")[i].contains("emisión")
				       &&  resultado.toString().split("\n")[i+1].contains("Nombre:") && resultado.toString().split("\n")[i+1].contains("Moneda")     ) {
				       String str = resultado.toString().split("\n")[i+1].split("Nombre:")[1].split("Moneda")[0].replace("###", "").trim();
				       if(str.contains(",")) {
				           asegurado.setNombre(str.split(",")[1] +" " +str.split(",")[0]);
				       }else {
				           asegurado.setNombre(resultado.toString().split("\n")[i+1].split("Nombre:")[1].split("Moneda")[0]);    
				       }
				       

				    }

					if( resultado.toString().split("\n")[i].contains("asegurado") && resultado.toString().split("\n")[i].contains("Moneda")
					&&  resultado.toString().split("\n")[i+2].contains("Nombre:") && resultado.toString().split("\n")[i+2].contains("Plazo")     ) {
					
					String str = resultado.toString().split("\n")[i+2].split("Nombre:")[1].split("Plazo")[0].replace("###", "").trim();
					if(str.contains(",")) {
						asegurado.setNombre(str.split(",")[1] +" " +str.split(",")[0]);
					}else {
						asegurado.setNombre(resultado.toString().split("\n")[i+2].split("Nombre:")[1].split("Plazo")[0].replace("###", "").trim());    
					}
					

				 }
				    if( resultado.toString().split("\n")[i].contains("Fecha de nacimiento:")) {
				        String fechacmineto=resultado.toString().split("\n")[i].split("nacimiento:")[1].replace("###", "").trim().replace("DE", "").replace("  ", "-");				      

				       asegurado.setNacimiento(fn.formatDateMonthCadena(fechacmineto));
				    }

					if( resultado.toString().split("\n")[i].contains("nacimiento") && resultado.toString().split("\n")[i].contains("Incremento")) {
				        String fechacmineto=resultado.toString().split("\n")[i].split("nacimiento:")[1].split("Incremento")[0].replace("###", "").trim().replace("de", "").replace("  ", "-");				      

				       asegurado.setNacimiento(fn.formatDateMonthCadena(fechacmineto));

					   if( resultado.toString().split("\n")[i+1].contains("Edad:") ) {
						asegurado.setEdad(fn.castInteger(resultado.toString().split("\n")[i+1].split("Edad:")[1].split("###")[1].replace("###", "").trim()));
					   }
				    }

				    if( resultado.toString().split("\n")[i].contains("Edad:") && resultado.toString().split("\n")[i].contains("Plazo")) {
				     asegurado.setEdad(fn.castInteger(resultado.toString().split("\n")[i].split("Edad:")[1].split("Plazo")[0].replace("###", "").trim()));
				    }

					
					   
				    		
				 }

				 
			}

			asegurados.add(asegurado);
			modelo.setAsegurados(asegurados);
			
			resultado  = new StringBuilder();
			for (int i = 0; i < contenido.split("Beneficiarios Nombre").length; i++) {	
				if(i> 0) {
					resultado.append(contenido.split("Beneficiarios Nombre")[i].split("Advertencia")[0].replace("CONYUGE", "###CONYUGE###").replace("@@@", "")
							.replace("ESPOSA", "###ESPOSA###").replace("MADRE", "###MADRE###").replace("CONCUBINA", "###CONCUBINA###")); 
				}										
			}

			if(resultado.length() == 0){
				inicio = contenido.indexOf("Beneficiarios");
				fin = contenido.indexOf("Advertencia");
				newcontenido.append( fn.extracted(inicio, fin, contenido).replace("MADRE", "###MADRE###")
				.replace("PADRE", "###PADRE###"));
				resultado = newcontenido;
			}
			
	
			if(resultado.length() >  0) {
				List<EstructuraBeneficiariosModel> beneficiarios = new ArrayList<>();
				for (int i = 0; i < resultado.toString().split("\n").length; i++) {
					EstructuraBeneficiariosModel beneficiario = new EstructuraBeneficiariosModel();							
					if(resultado.toString().split("\n")[i].contains("%")) {
						beneficiario.setNombre(resultado.toString().split("\n")[i].split("###")[0].trim());
						beneficiario.setParentesco(fn.parentesco( resultado.toString().split("\n")[i].split("###")[1]));
						beneficiarios.add(beneficiario);
					}
				}
				modelo.setBeneficiarios(beneficiarios);
			}
			
			
			inicio = contenido.indexOf("Coberturas Amparadas");
			fin = contenido.indexOf("Beneficios incluidos ###Suma asegurada");
		

			if(fin == -1){				
				fin = contenido.indexOf("Beneficiarios");
			}
			
			if(fin == -1){				
				fin = contenido.indexOf("AXA Seguros,");
			}
			String cob = contenido.substring(inicio,fin).replace("@@@", "").replace("\r", "");


	
			inicio = contenido.indexOf("Beneficios incluidos");
			fin = contenido.indexOf("AXA Seguros, S.A. de C.V.");

			String contex = fn.extracted(inicio, fin, contenido);


			
			if (inicio > -1 && fin > -1 && inicio < fin) {
				List<EstructuraCoberturasModel> coberturas = new ArrayList<>();
				List<EstructuraCoberturasModel> coberturas2 = new ArrayList<>();
			
		
				if(cob.length()> 0){
					cob +=contex ;
				}
				resultado  = new StringBuilder();
				resultado.append(cob);
				for (int i = 0; i < resultado.toString().split("\n").length; i++) {
					EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();	
					if(!resultado.toString().split("\n")[i].contains("Plazo de seguro") 
					&& !resultado.toString().split("\n")[i].contains("Coberturas") && !resultado.toString().split("\n")[i].contains("Beneficios")
					&& !resultado.toString().split("\n")[i].contains("AXA Seguros")
					&& !resultado.toString().split("\n")[i].contains("Piso")
					&& !resultado.toString().split("\n")[i].contains("axa")
					&& !resultado.toString().split("\n")[i].contains("México")
					) {
						
						
						if(resultado.toString().split("\n")[i].split("###").length == 1) {
							cobertura.setNombre(resultado.toString().split("\n")[i].split("###")[0].trim());
							
							coberturas.add(cobertura);
						}
						if(resultado.toString().split("\n")[i].split("###").length == 2) {
							cobertura.setNombre(resultado.toString().split("\n")[i].split("###")[0].trim());
							cobertura.setSa(resultado.toString().split("\n")[i].split("###")[1].trim());
							coberturas.add(cobertura);
						}
					
						if(resultado.toString().split("\n")[i].split("###").length == 5) {
							cobertura.setNombre(resultado.toString().split("\n")[i].split("###")[0].trim());
							cobertura.setSa(resultado.toString().split("\n")[i].split("###")[2].trim());
							coberturas.add(cobertura);
						}
						if(resultado.toString().split("\n")[i].split("###").length == 6) {
							cobertura.setNombre(resultado.toString().split("\n")[i].split("###")[0].trim());
							cobertura.setSa(resultado.toString().split("\n")[i].split("###")[2].trim());
							coberturas.add(cobertura);
						}

					}
					
				}
				modelo.setCoberturas(coberturas);
				
			
				
			}
			
			if(modelo.getVigenciaDe().length() > 0 && modelo.getVigenciaA().length() == 0){
				modelo.setVigenciaA(fn.calcvigenciaA(modelo.getVigenciaDe(), 12));
				
			}
		
			if(modelo.getVigenciaDe().length() > 0 && fn.diferencia(modelo.getVigenciaDe(), modelo.getVigenciaA()) > 1) {
			    modelo.setVigenciaA(fn.calcvigenciaA(modelo.getVigenciaDe(), 12));
			}
			

			return modelo;
		} catch (Exception ex) {
			ex.printStackTrace();
			modelo.setError(AxaVida2Model.this.getClass().getTypeName() + " - catch:" + ex.getMessage() + " | "
					+ ex.getCause());
			return modelo;

		}
	}

}
