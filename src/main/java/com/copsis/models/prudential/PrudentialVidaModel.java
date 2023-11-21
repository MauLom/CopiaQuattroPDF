package com.copsis.models.prudential;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraAseguradosModel;
import com.copsis.models.EstructuraBeneficiariosModel;
import com.copsis.models.EstructuraCoberturasModel;
import com.copsis.models.EstructuraJsonModel;

public class PrudentialVidaModel {
	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
	private String contenido = "";

	public PrudentialVidaModel(String contenido) {
		this.contenido = contenido;
	}

	public EstructuraJsonModel procesar() {
		int inicio = 0;
		int fin = 0;
		StringBuilder newcontenido = new StringBuilder();
		contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales())
		.replace("domicilio ###del ASEGURADO", "domicilio del ASEGURADO");

		try {
			modelo.setTipo(5);
			modelo.setCia(28);

			inicio = contenido.indexOf("CONTRATANTE");
			fin = contenido.indexOf("domicilio del ASEGURADO");
			newcontenido.append(fn.extracted(inicio, fin, contenido));
			
			for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
                
				if(newcontenido.toString().split("\n")[i].contains("CONTRATANTE") && newcontenido.toString().split("\n")[i].contains("Póliza No.")) {
					modelo.setCteNombre(newcontenido.toString().split("\n")[i+1].replace("###", "").trim());
				modelo.setPoliza(newcontenido.toString().split("\n")[i].split("Póliza No.")[1].replace("###", "").trim());
				}


				if(newcontenido.toString().split("\n")[i].contains("CONTRATANTE") 
				&& newcontenido.toString().split("\n")[i].contains("PÓLIZA No.")) {	
							String x=newcontenido.toString().split("\n")[i+2].replace("###", "").trim();
						modelo.setCteNombre(x.replace("\u00a0","").trim());
						modelo.setPoliza(newcontenido.toString().split("\n")[i].split("PÓLIZA No.")[1].replace("###", "").replace("\u00a0","").trim());							
				}
				if(newcontenido.toString().split("\n")[i].contains("RFC")) {
				
					if(newcontenido.toString().split("\n")[i+1].split("###")[1].replace("###", "").trim().length() <15){
						modelo.setRfc(newcontenido.toString().split("\n")[i+1].split("###")[1].replace("###", "").trim());
					}

					if(newcontenido.toString().split("\n")[i+1].split("###").length ==3 && newcontenido.toString().split("\n")[i+1].split("###")[2].replace("###", "").trim().length() <15){
						modelo.setRfc(newcontenido.toString().split("\n")[i+1].split("###")[2].replace("###", "").trim());
					}
					if(newcontenido.toString().split("\n")[i+1].split("###").length==2 && newcontenido.toString().split("\n")[i+1].split("###")[1].replace("###", "").trim().length() <15){
						modelo.setRfc(newcontenido.toString().split("\n")[i+1].split("###")[1].replace("###", "").trim());
					}
					
					
					modelo.setCteDireccion((newcontenido.toString().split("\n")[i].split("###")[0]
							+" "+ newcontenido.toString().split("\n")[i+1].split("###")[0]
									+" "+ newcontenido.toString().split("\n")[i+2].split("###")[0]).replace("RFC", "").replace("\u00a0",""));
				}
				
			}
			
			if(contenido.contains("CURP")){
				for(String linea: contenido.substring((contenido.indexOf("CURP")-80),contenido.indexOf("CURP")).split("\n")) {
					for(String palabra: linea.split(" ")) {
						//busca 5 digitos seguidos + al final de la palabra una coma
						if(palabra.matches("^[\\d{5}]+[,]")) {
							modelo.setCp(palabra.replace(",", ""));
						}
					}				
				}
			}
			
			if(contenido.contains("del ASEGURADO")) {
				for(String linea: contenido.substring((contenido.indexOf("del ASEGURADO")-80),contenido.indexOf("del ASEGURADO")).split("\n")) {
					for(String palabra: linea.split(" ")) {
						 palabra =palabra.replace("domicilio", "")
						 .replace("Nombre", "").replace("###", "");
						
						if(palabra.matches("^[A-Z]{1}[AEIOU]{1}[A-Z]{2}[0-9]{2}(0[1-9]|1[0-2])(0[1-9]|1[0-9]|2[0-9]|3[0-1])[HM]{1}(AS|BC|BS|CC|CS|CH|CL|CM|DF|DG|GT|GR|HG|JC|MC|MN|MS|NT|NL|OC|PL|QT|QR|SP|SL|SR|TC|TS|TL|VZ|YN|ZS|NE)[B-DF-HJ-NP-TV-Z]{3}[0-9A-Z]{1}[0-9]{1}$")) {
							modelo.setCurp(palabra);	
						}	
					}
				}
			}
			
			inicio = contenido.indexOf("domicilio del ASEGURADO");
			fin = contenido.indexOf("Coberturas");
			
			newcontenido = new StringBuilder();
			newcontenido.append(fn.extracted(inicio, fin, contenido));
		 	List<EstructuraAseguradosModel> asegurados = new ArrayList<>();
		 	EstructuraAseguradosModel asegurado = new EstructuraAseguradosModel();
			for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
			
				if(newcontenido.toString().split("\n")[i].contains("ASEGURADO")) {
					
					if(newcontenido.toString().split("\n")[i+1].split("###").length==6) {
						asegurado.setNombre((newcontenido.toString().split("\n")[i+1].split("###")[0]).concat(newcontenido.toString().split("\n")[i+1].split("###")[1].trim()));	
					}else if(newcontenido.toString().split("\n")[i+1].split("###").length==5) {
						asegurado.setNombre((newcontenido.toString().split("\n")[i+1].split("###")[0]).concat(newcontenido.toString().split("\n")[i+1].split("###")[1].trim()));	
					}
					
					else {
						asegurado.setNombre(newcontenido.toString().split("\n")[i+1].split("###")[0].trim());	
					}
				}
				if(newcontenido.toString().split("\n")[i].contains("Edad") && newcontenido.toString().split("\n")[i].contains("Sexo")
				 && newcontenido.toString().split("\n")[i].contains("nacimiento")) {
					
					if(newcontenido.toString().split("\n")[i+1].contains("Fem")){
						asegurado.setEdad(Integer.parseInt(fn.obtenerListNumeros2(newcontenido.toString().split("\n")[i+1]).get(0)));
					}else{
					  if(fn.obtenerListNumeros2( newcontenido.toString().split("\n")[i+1].split("###")[1]).size() > 0){
						asegurado.setEdad(Integer.parseInt(fn.obtenerListNumeros2( newcontenido.toString().split("\n")[i+1].split("###")[1]).get(0)));
					  }
						
					}									
					asegurado.setSexo(fn.sexo(newcontenido.toString().split("\n")[i+1].split("###")[2].trim()) ? 1:0);
				
					asegurado.setNacimiento(fn.formatDateMonthCadena(fn.obtenVigePoliza(newcontenido.toString().split("\n")[i+1]).get(0)));
					
				}
				if(newcontenido.toString().split("\n")[i].contains("Forma de pago") && newcontenido.toString().split("\n")[i].contains("Moneda")
						&& newcontenido.toString().split("\n")[i].contains("vigencia")			
						){
						
					modelo.setMoneda(fn.buscaMonedaEnTexto(newcontenido.toString().split("\n")[i+1]));
				 modelo.setFormaPago(fn.formaPagoSring( newcontenido.toString().split("\n")[i+1]));

				 if(modelo.getFormaPago()==0  && modelo.getMoneda() == 0){
					modelo.setMoneda(fn.buscaMonedaEnTexto(newcontenido.toString().split("\n")[i+2]));
				 modelo.setFormaPago(fn.formaPagoSring( newcontenido.toString().split("\n")[i+2]));
				 }
				 
					if(newcontenido.toString().split("\n")[i+1].split("###").length> 2 && fn.obtenVigePoliza2(newcontenido.toString().split("\n")[i+1].split("###")[2].trim()).size() >1){
						modelo.setVigenciaDe(fn.formatDateMonthCadena(
							fn.obtenVigePoliza2(newcontenido.toString().split("\n")[i+1].split("###")[2].trim()).get(0)));
					}
					
					
					
					
					if(fn.obtenVigePoliza(newcontenido.toString().split("\n")[i+1].trim()).size() >0){
					 modelo.setVigenciaDe(fn.formatDateMonthCadena(fn.obtenVigePoliza(newcontenido.toString().split("\n")[i+1].trim()).get(0)));
					}
					if(fn.obtenVigePoliza(newcontenido.toString().split("\n")[i+2].trim()).size() >0){
						modelo.setVigenciaDe(fn.formatDateMonthCadena(fn.obtenVigePoliza(newcontenido.toString().split("\n")[i+2].trim()).get(0)));
					   }
					
					
				}
			}
			asegurados.add(asegurado);
			modelo.setAsegurados(asegurados);
			
		
			
			if(modelo.getVigenciaDe().length() > 0) {
				modelo.setFechaEmision(modelo.getVigenciaDe());
				 SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd"); 
				 Date fecha = formato.parse(modelo.getVigenciaDe());
				 modelo.setVigenciaA(fn.sumar(fecha,12));
			}
			

			inicio = contenido.indexOf("Coberturas");
			fin = contenido.indexOf("BENEFICIARIO");
			newcontenido = new StringBuilder();
			newcontenido.append(fn.extracted(inicio, fin, contenido).replace("Retiro Prudential con", 
		   "Retiro Prudential con Beneficio Fisca LISR Prima para el Retiro deducible en los términos del Artículo"));
			List<EstructuraCoberturasModel> coberturas = new ArrayList<>();
			for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
				EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();
				if(!newcontenido.toString().split("\n")[i].contains("Coberturas") && !newcontenido.toString().split("\n")[i].contains("Vigencia")
						&& !newcontenido.toString().split("\n")[i].contains("La cobertura") && !newcontenido.toString().split("\n")[i].contains("primas años ")
						&& !newcontenido.toString().split("\n")[i].contains("Nombre completo")) {

					if(newcontenido.toString().split("\n")[i].split("###").length == 7) {
						cobertura.setNombre(newcontenido.toString().split("\n")[i].split("###")[0].trim());
						cobertura.setSa(newcontenido.toString().split("\n")[i].split("###")[1].trim());
						coberturas.add(cobertura);
					}

					if(!newcontenido.toString().split("\n")[i].contains("Invalidez Total") ){
					if(newcontenido.toString().split("\n")[i].contains("Total") && newcontenido.toString().split("\n")[i].length() > 10 ){
				
						List<String> valores = fn.obtenerListNumeros(newcontenido.toString().split("\n")[i]);
					
						modelo.setPrimaTotal(fn.castBigDecimal(fn.castDouble(valores.get(0))));   
						modelo.setPrimaneta(modelo.getPrimaTotal());
					}
				} 

				}
			}
			modelo.setCoberturas(coberturas);
			
			
			inicio = contenido.indexOf("BENEFICIARIO");
			fin = contenido.indexOf("Advertencia");
			newcontenido = new StringBuilder();
			newcontenido.append(fn.extracted(inicio, fin, contenido));
			List<EstructuraBeneficiariosModel> beneficiarios = new ArrayList<>();				
			for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
				EstructuraBeneficiariosModel beneficiario = new EstructuraBeneficiariosModel();
				
				if(newcontenido.toString().split("\n")[i].length() > 20 && !newcontenido.toString().split("\n")[i].contains("Parentesco") && !newcontenido.toString().split("\n")[i].contains("identificación")
						) {		
						
						if(newcontenido.toString().split("\n")[i].split("###").length == 5){
							beneficiario.setNombre(newcontenido.toString().split("\n")[i].split("###")[0].trim() +" "+newcontenido.toString().split("\n")[i].split("###")[1].trim() +" "+ newcontenido.toString().split("\n")[i].split("###")[2].trim() );				
						}
						else if(newcontenido.toString().split("\n")[i].split("###").length == 6){
							beneficiario.setNombre(newcontenido.toString().split("\n")[i].split("###")[0].trim() +" "
							+newcontenido.toString().split("\n")[i].split("###")[1].trim() +" "+ 
							newcontenido.toString().split("\n")[i].split("###")[2].trim()
							+" "+ newcontenido.toString().split("\n")[i].split("###")[3].trim() );				
						}
						else if(newcontenido.toString().split("\n")[i].split("###")[0].trim().length() >10){
							beneficiario.setNombre(newcontenido.toString().split("\n")[i].split("###")[0].trim());				
						}else {
							beneficiario.setNombre(newcontenido.toString().split("\n")[i].split("###")[1].trim());				
						}				
					
				
					if(fn.castInteger(newcontenido.toString().split("\n")[i].split("###")[2].trim()) == null){
						beneficiario.setParentesco(fn.parentesco(newcontenido.toString().split("\n")[i].split("###")[2].trim()));
					
						if(fn.obtenerListNumeros(newcontenido.toString().split("\n")[i]).size() > 0){
							List<String> valores = fn.obtenerListNumeros(newcontenido.toString().split("\n")[i]);
							beneficiario.setPorcentaje(fn.castDouble(valores.get(0)).intValue());   
						}
						
						
					}else{
						beneficiario.setParentesco(fn.parentesco(newcontenido.toString().split("\n")[i].split("###")[1].trim()));

						beneficiario.setPorcentaje(Integer.parseInt(newcontenido.toString().split("\n")[i].split("###")[2].trim()));
					}
				
					 beneficiarios.add(beneficiario);					
				}
			}
			modelo.setBeneficiarios(beneficiarios);

			return modelo;
		} catch (Exception ex) {		
			modelo.setError(PrudentialVidaModel.this.getClass().getTypeName() + " - catch:" + ex.getMessage() + " | "+ ex.getCause());
				return modelo;
		}
	}

}
