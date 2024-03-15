package com.copsis.models.hdi;

import java.util.ArrayList;
import java.util.List;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraCoberturasModel;
import com.copsis.models.EstructuraJsonModel;
import com.copsis.models.EstructuraRecibosModel;

public class HdiAutosBModel {
	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();

	public EstructuraJsonModel procesar(String  contenido) {
		int inicio = 0;
		int fin = 0;
		StringBuilder newcontenido = new StringBuilder();
		contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales())
		.replace("Descripción###Límite###de###Responsabilidad", "Descripción###Límite de Responsabilidad")
		.replace("responsabilidad###máxima", "responsabilidad máxima.")
		.replace("MESES###SIN###INTERESES", "MESES SIN INTERESES")
		.replace("SEMESTRAL EFECTIVO", "SEMESTRAL")
		.replace("Art.###25°###de###la###Ley", "Art.25° de la Ley")
		.replace("Art. ###25° ###de ###la ###Ley", "Art.25° de la Ley")
		.replace("POólOiz a:", "Póliza:");
		
		try {
			
			modelo.setTipo(1);
			modelo.setCia(14);
			inicio = contenido.indexOf("Ramo:");
			fin = contenido.indexOf("Descripción###Límite de Responsabilidad");
	
			newcontenido.append( fn.extracted(inicio, fin, contenido));
			
			
			for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {		
				if(newcontenido.toString().split("\n")[i].contains("responsabilidad máxima.")){
					modelo.setCteNombre(newcontenido.toString().split("\n")[i+1].replace("###", " "));
					if(newcontenido.toString().split("\n")[i+2].contains("RFC:")){
						modelo.setRfc(newcontenido.toString().split("\n")[i+2].split("RFC:")[1].replace("###", " ").trim());
						if(newcontenido.toString().split("\n")[i+5].contains("C.P")){
							modelo.setCteDireccion(newcontenido.toString().split("\n")[i+4].replace("###", " ")
							+" "+ newcontenido.toString().split("\n")[i+5].replace("###", " "));	
						}else{
							modelo.setCteDireccion(newcontenido.toString().split("\n")[i+4].replace("###", " "));	
						}
								
					}else {
						modelo.setCteDireccion(newcontenido.toString().split("\n")[i+2]);			
					}

					if(fn.obtenerListNumeros2(modelo.getCteDireccion()).get(1).length()  == 5) {
						modelo.setCp(fn.obtenerListNumeros2(modelo.getCteDireccion()).get(1));
					}
					if(fn.obtenerListNumeros2(modelo.getCteDireccion()).size()>5 && fn.obtenerListNumeros2(modelo.getCteDireccion()).get(5).length()  == 5) {
						modelo.setCp(fn.obtenerListNumeros2(modelo.getCteDireccion()).get(5));
					}
				 }
				if(newcontenido.toString().split("\n")[i].contains("Póliza:") && newcontenido.toString().split("\n")[i].contains("Licencia:") && newcontenido.toString().split("\n")[i].contains("expedición de licencia")) {					
					modelo.setPoliza(newcontenido.toString().split("\n")[i].split("Póliza:")[1].split("Licencia:")[0].replace("###", "").trim());
				}
				if(newcontenido.toString().split("\n")[i].contains("Póliza:") && newcontenido.toString().split("\n")[i].contains("Vigencia:") ) {					
					modelo.setPoliza(newcontenido.toString().split("\n")[i].split("Póliza:")[1].split("Vigencia:")[0].replace("###", "").trim());
					if(fn.obtenVigePoliza(newcontenido.toString().split("\n")[i]).size()> 1){
						modelo.setVigenciaDe(fn.formatDateMonthCadena(fn.obtenVigePoliza(newcontenido.toString().split("\n")[i]).get(0)));
						modelo.setVigenciaA(fn.formatDateMonthCadena(fn.obtenVigePoliza(newcontenido.toString().split("\n")[i]).get(1)));
					}
					
				}
				if(newcontenido.toString().split("\n")[i].contains("Agente:") && newcontenido.toString().split("\n")[i].contains("Vigencia")) {			
					modelo.setCveAgente(fn.obtenerListNumeros2(newcontenido.toString().split("\n")[i].split("Agente:")[1].split("Vigencia:")[0].replace("###","").trim()).get(0));
					if(modelo.getCveAgente().length() >  0) {
						modelo.setAgente(newcontenido.toString().split("\n")[i].split(modelo.getCveAgente())[1].split("Vigencia:")[0].replace("###", "").trim());
					}					
					String d  = newcontenido.toString().split("\n")[i].split("Vigencia:")[1].split("al")[0].replace(",", "").trim().replace(" ","-");
					modelo.setVigenciaDe(fn.formatDateMonthCadena( d.split("-")[1] +"-"+ d.split("-")[0] +"-"+ d.split("-")[2]));
					
					String a  = newcontenido.toString().split("\n")[i].split("al")[1].replace(",", "").trim().replace(" ","-");
					modelo.setVigenciaA(fn.formatDateMonthCadena( a.split("-")[1] +"-"+ a.split("-")[0] +"-"+ a.split("-")[2]));		
				}
				if(newcontenido.toString().split("\n")[i].contains("RFC:") && newcontenido.toString().split("\n")[i].contains("Fecha de Emisión:")) {	
					modelo.setRfc(newcontenido.toString().split("\n")[i].split("RFC:")[1].split("Fecha")[0].replace("###", "").trim());
				}
			
				if(newcontenido.toString().split("\n")[i].contains("Serie:") && newcontenido.toString().split("\n")[i].contains("Placas:") && newcontenido.toString().split("\n")[i].contains("Remolque:")) {	
					modelo.setSerie(newcontenido.toString().split("\n")[i].split("Serie:")[1].split("Placas")[0].replace("###", "").trim());	
					modelo.setPlacas(newcontenido.toString().split("\n")[i].split("Placas:")[1].split("Remolque")[0].replace("###", "").trim());	
				}
				if(newcontenido.toString().split("\n")[i].contains("Serie:") && newcontenido.toString().split("\n")[i].contains("Cilindros") 
				) {	
					modelo.setSerie(newcontenido.toString().split("\n")[i].split("Serie:")[1].split("Cilindros")[0].replace("###", "").trim());	
					
				}
				if(newcontenido.toString().split("\n")[i].contains("Paquete:")) {
					modelo.setPlan( newcontenido.toString().split("\n")[i].split("Paquete:")[1].split("###")[0].trim());
				}
			
				if(newcontenido.toString().split("\n")[i].contains("Clave:") && newcontenido.toString().split("\n")[i].contains("Puertas:")) {
					modelo.setDescripcion(newcontenido.toString().split("\n")[i].split("Clave:")[0].replace("###", "").trim());
					modelo.setClave(newcontenido.toString().split("\n")[i].split("Clave:")[1].split("Puertas:")[0].replace("###", "").trim());
				}
				if(newcontenido.toString().split("\n")[i].contains("Motor:") && newcontenido.toString().split("\n")[i].contains("Uso:")) {
					modelo.setMotor(newcontenido.toString().split("\n")[i].split("Motor:")[1].split("Uso:")[0].replace("###", "").trim());
				}
				if(newcontenido.toString().split("\n")[i].contains("C.P.")){
                    List<String> valores = fn.obtenerListNumeros2(newcontenido.toString().split("\n")[i].split("C.P.")[1]);
						if(!valores.isEmpty()){
                               modelo.setCp(valores.get(0));
                        }
                }
				if(modelo.getVigenciaDe().isBlank() && modelo.getVigenciaA().isEmpty() 
				&& newcontenido.toString().split("\n")[i].contains("Vigencia:")
				&& newcontenido.toString().split("\n")[i].contains("Desde")
				&& newcontenido.toString().split("\n")[i].contains("Hasta") 
				){
					List<String> valores = fn.obtenVigePoliza(newcontenido.toString().split("\n")[i]);
                    if (valores.size() > 1) {
                        modelo.setVigenciaDe(fn.formatDateMonthCadena(valores.get(0)));
                        modelo.setVigenciaA(fn.formatDateMonthCadena(valores.get(1)));
                    }
				}

			}
			
			
			if(modelo.getVigenciaDe().length() > 0) {
				modelo.setFechaEmision(modelo.getVigenciaDe());
			}
			
			
			inicio  = contenido.indexOf("Descripción###Límite de Responsabilidad");
			fin = contenido.indexOf("Prima Neta");
			if(fin == -1){
				fin = contenido.indexOf("MESES SIN INTERESES");
			}

		
			newcontenido = new StringBuilder(); 
			newcontenido.append( fn.extracted(inicio, fin, contenido)
			.replace("Daños###Materiales", "Daños Materiales")
			.replace("Robo###Total", "Robo Total")
			.replace("Gastos###Médicos###Ocupantes", "Gastos Médicos Ocupantes (Límite Único Combinado)")
			.replace("Accidentes###Automovilísticos###al###Conductor", "Accidentes Automovilísticos al Conductor")
			.replace("Responsabilidad###Civil###Límite###Único###y###Combinado", "Responsabilidad Civil (Límite Único y Combinado)")
			.replace("Responsabilidad###Civil###Exceso###por###Muerte###de###Terceros", "Responsabilidad Civil Exceso por Muerte de Terceros")
			.replace("Asistencia###Jurídica", "Asistencia Jurídica")
			.replace("Responsabilidad###Civil###Familiar", "Responsabilidad Civil Familiar")
			.replace("Asistencia###en###viajes", "Asistencia en viajes")
			.replace("Asistencia###Médica", "Asistencia Médica")
			.replace("Responsabilidad###Civil###por###daños###a###los###Ocupantes", "Responsabilidad Civil por daños a los Ocupantes")
			.replace("Asistencia###Funeraria", "Asistencia Funeraria"));
			List<EstructuraCoberturasModel> coberturas = new ArrayList<>();

			modelo.setFormaPago(fn.formaPagoSring(newcontenido.toString())  );
					
			for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {		
				EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();
				if(!newcontenido.toString().split("\n")[i].contains("Descripción")) {
					
					
					int sp =newcontenido.toString().split("\n")[i].split("###").length;
					switch(sp) {
					case  2:
						cobertura.setNombre(newcontenido.toString().split("\n")[i].split("###")[0]);
						cobertura.setSa(newcontenido.toString().split("\n")[i].split("###")[1]);
						coberturas.add(cobertura);
						break;
					case  3:
						cobertura.setNombre(newcontenido.toString().split("\n")[i].split("###")[0]);
						cobertura.setSa(newcontenido.toString().split("\n")[i].split("###")[1]);
						cobertura.setDeducible(newcontenido.toString().split("\n")[i].split("###")[2]);
						coberturas.add(cobertura);
						break;
					}
				}
			}
			
			modelo.setCoberturas(coberturas);
			
			
			inicio = contenido.indexOf("Prima Neta");
			if(inicio == -1){
				inicio = contenido.indexOf("MESES SIN INTERESES");
			}
			fin  = contenido.indexOf("Póliza Extranjera");

			if(fin == -1){
				fin = contenido.indexOf("Art.25° de la Ley");
			}


		
			newcontenido = new StringBuilder(); 
			newcontenido.append( fn.extracted(inicio, fin, contenido));
			
			for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {		
				if(newcontenido.toString().split("\n")[i].contains("Prima Neta")) {
				
					List<String> valores = fn.obtenerListNumeros(newcontenido.toString().split("\n")[i+1]);
				
					if(!valores.isEmpty()){
						modelo.setPrimaneta(fn.castBigDecimal(fn.castDouble(valores.get(0))));
					
						modelo.setDerecho(fn.castBigDecimal(fn.castDouble(valores.get(1))));
						modelo.setIva(fn.castBigDecimal(fn.castDouble(valores.get(2))));
						modelo.setPrimaTotal(fn.castBigDecimal(fn.castDouble(valores.get(3))));
					}
            	
				}
				
				if(newcontenido.toString().split("\n")[i].contains("Total")) {
					List<String> valores = fn.obtenerListNumeros(newcontenido.toString().split("\n")[i+1]);		
					
					if(!valores.isEmpty() && valores.size() != 4){												
							modelo.setPrimaneta(fn.castBigDecimal(fn.castDouble(valores.get(0))));
							modelo.setDerecho(fn.castBigDecimal(fn.castDouble(valores.get(5))));
							modelo.setIva(fn.castBigDecimal(fn.castDouble(valores.get(6))));
							modelo.setPrimaTotal(fn.castBigDecimal(fn.castDouble(valores.get(7))));
						
            		
					}
				}
				if(newcontenido.toString().split("\n")[i].contains("Fraccionado:")) {
					List<String> valores = fn.obtenerListNumeros(newcontenido.toString().split("\n")[i+1]);		
					if(!valores.isEmpty() && valores.size() != 4){						
					 modelo.setPrimaneta(fn.castBigDecimal(fn.castDouble(valores.get(0))));
				     modelo.setAjusteUno(fn.castBigDecimal(fn.castDouble(valores.get(1))));
					 modelo.setDerecho(fn.castBigDecimal(fn.castDouble(valores.get(5))));
					 modelo.setIva(fn.castBigDecimal(fn.castDouble(valores.get(6))));
					 modelo.setPrimaTotal(fn.castBigDecimal(fn.castDouble(valores.get(7))));
					}
				}
			}
	
	
			if(modelo.getFormaPago() == 1 && (fn.diferenciaDias(modelo.getVigenciaDe(), modelo.getVigenciaA()) <30)) {
				List<EstructuraRecibosModel> recibos = new ArrayList<>();
				EstructuraRecibosModel recibo = new EstructuraRecibosModel();
				recibo.setReciboId("");
				recibo.setSerie("1/1");
				recibo.setVigenciaDe(modelo.getVigenciaDe());
				recibo.setVigenciaA(modelo.getVigenciaA());				
			    recibo.setVencimiento(modelo.getVigenciaDe());				
				recibo.setPrimaneta(fn.castBigDecimal(modelo.getPrimaneta(), 2));
				recibo.setDerecho(fn.castBigDecimal(modelo.getDerecho(), 2));
				recibo.setRecargo(fn.castBigDecimal(modelo.getRecargo(), 2));
				recibo.setIva(fn.castBigDecimal(modelo.getDerecho(), 2));
				recibo.setPrimaTotal(fn.castBigDecimal(modelo.getPrimaTotal(), 2));
				recibo.setAjusteUno(fn.castBigDecimal(modelo.getAjusteUno(), 2));
				recibo.setAjusteDos(fn.castBigDecimal(modelo.getAjusteDos(), 2));
				recibo.setCargoExtra(fn.castBigDecimal(modelo.getCargoExtra(), 2));
				recibos.add(recibo);
				modelo.setRecibos(recibos);
			}
		
			if(modelo.getFormaPago() ==0){			
				modelo.setFormaPago(1);				
			}
			modelo.setMoneda(1);
	
			return modelo;
		} catch (Exception ex) {
			modelo.setError(HdiAutosBModel.this.getClass().getTypeName() + " | " + ex.getMessage() + " | " + ex.getCause());
			 return modelo;
		}

	}

}
