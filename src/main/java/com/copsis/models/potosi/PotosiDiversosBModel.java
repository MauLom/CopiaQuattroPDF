package com.copsis.models.potosi;

import java.util.ArrayList;
import java.util.List;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraCoberturasModel;
import com.copsis.models.EstructuraJsonModel;

public class PotosiDiversosBModel {
	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();	
	private String contenido = "";
	private List<String> vigenciaspo = new ArrayList<>();
	
	public PotosiDiversosBModel(String contenido) {
		this.contenido = contenido;
	}
	
	public EstructuraJsonModel procesar() {
		int inicio = 0;
		int fin = 0;
		StringBuilder newcontenido = new StringBuilder();
		contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());
		try {
			modelo.setTipo(7);
			modelo.setCia(37);
			
   
			inicio = contenido.indexOf("POLIZA DE SEGURO DE R ESPONSABILIDAD ");
			inicio = inicio  == -1  ? contenido.indexOf("POLIZA DE SEGURO DE"):inicio;
			fin = contenido.indexOf("DETALLE DEL MOVIMIENTO");
			

			newcontenido.append( fn.extracted(inicio, fin, contenido));
			List<EstructuraCoberturasModel> coberturas = new ArrayList<>();
			for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
			
				if(newcontenido.toString().split("\n")[i].contains("Póliza") && newcontenido.toString().split("\n")[i].contains("Renov") 
						&& newcontenido.toString().split("\n")[i].contains("Desde") && newcontenido.toString().split("\n")[i].contains("Hasta")) {
					modelo.setPoliza(newcontenido.toString().split("\n")[i].split("Póliza")[1].split("Renov")[0].replace("###", "").trim());
					vigenciaspo = fn.obtenVigePoliza(newcontenido.toString().split("\n")[i]);
					if(vigenciaspo.size() > 1) {
						modelo.setVigenciaDe(fn.formatDateMonthCadena( vigenciaspo.get(0)));
						modelo.setVigenciaA(fn.formatDateMonthCadena( vigenciaspo.get(1)));
						modelo.setFechaEmision(modelo.getVigenciaDe());
					}
				}
				if(newcontenido.toString().split("\n")[i].contains("Plan de Pago") && newcontenido.toString().split("\n")[i].contains("Moneda")) {
					modelo.setMoneda(fn.buscaMonedaEnTexto(newcontenido.toString().split("\n")[i]));
					modelo.setFormaPago(fn.formaPagoSring(newcontenido.toString().split("\n")[i]));
				} 
				if(newcontenido.toString().split("\n")[i].contains("CONTRATANTE") && newcontenido.toString().split("\n")[i+1].contains("Razón Social")
						&& newcontenido.toString().split("\n")[i+1].contains("RFC")	) {
					modelo.setCteNombre( newcontenido.toString().split("\n")[i+1].split("Razón Social")[1].split("RFC")[0].replace("###", "").trim());
					modelo.setRfc(newcontenido.toString().split("\n")[i+1].split("RFC")[1].replace("###", "").replace("-", "").trim());
					
				}
				if(newcontenido.toString().split("\n")[i].contains("Domicilio del Contratante") ) {
					modelo.setCteDireccion(newcontenido.toString().split("\n")[i].split("Domicilio del Contratante")[1].replace("###", "").trim()
							 +" " + newcontenido.toString().split("\n")[i+1].replace("###", "").trim());
				}
				
			
				if(newcontenido.toString().split("\n")[i].contains("CP") ) {
					modelo.setCp(newcontenido.toString().split("\n")[i].split("CP")[1].trim().substring(0, 5));
				}

				if(newcontenido.toString().split("\n")[i].contains("Ramo") ) {
					EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();
					switch (newcontenido.toString().split("\n")[i+1].split("###").length ) {
						case 3:
							 cobertura.setNombre(newcontenido.toString().split("\n")[i+1].split("###")[0]);
					        cobertura.setSa(newcontenido.toString().split("\n")[i+1].split("###")[1]);
							 coberturas.add(cobertura);
							break;
					   case 4:
							 cobertura.setNombre(newcontenido.toString().split("\n")[i+1].split("###")[0]);
					        cobertura.setSa(newcontenido.toString().split("\n")[i+1].split("###")[1]);
							cobertura.setDeducible(newcontenido.toString().split("\n")[i+1].split("###")[2].trim());
							coberturas.add(cobertura);
							break;		
					
						default:
							break;
					}
										
					 modelo.setCoberturas(coberturas);
				}
			  }
				inicio = contenido.indexOf("DETALLE DEL MOVIMIENTO");
				fin = contenido.indexOf("Seguros el Potosí, S.A.");	
				fin = fin == fin ? contenido.indexOf("Seguros El Potosí, S.A."):fin;
			
		
				newcontenido = new StringBuilder();
				newcontenido.append( fn.extracted(inicio, fin, contenido));				
				for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {					
					if(newcontenido.toString().split("\n")[i].contains("FRACCIONADO")) {
						modelo.setPrimaneta(fn.castBigDecimal(fn.castFloat(newcontenido.toString().split("\n")[i+1].split("###")[0])));
						modelo.setRecargo(fn.castBigDecimal(fn.castFloat(newcontenido.toString().split("\n")[i+1].split("###")[1])));
						modelo.setDerecho(fn.castBigDecimal(fn.castFloat(newcontenido.toString().split("\n")[i+1].split("###")[2])));
						modelo.setIva(fn.castBigDecimal(fn.castFloat(newcontenido.toString().split("\n")[i+1].split("###")[4])));
						modelo.setPrimaTotal(fn.castBigDecimal(fn.castFloat(newcontenido.toString().split("\n")[i+1].split("###")[7])));
					}
				}

				inicio = contenido.indexOf("Intermediario");
				fin = contenido.indexOf("Registro:");	
			
				newcontenido = new StringBuilder();
				newcontenido.append( fn.extracted(inicio, fin, contenido));	
					
				for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {					
					if(newcontenido.toString().split("\n")[i].contains("Intermediario")){
						List<String> valores = fn.obtenerListNumeros2(newcontenido.toString().split("\n")[i]);
						modelo.setCveAgente(valores.get(0));
						if(!modelo.getCveAgente().isEmpty()){
							modelo.setAgente(newcontenido.toString().split("\n")[i].split(modelo.getCveAgente())[1].replace("###", "").trim());
						}						
					}
				}
			
			return modelo;
		} catch (Exception ex) {	
			modelo.setError(PotosiDiversosBModel.this.getClass().getTypeName() + " | " + ex.getMessage() + " | " + ex.getCause());
			return modelo;
		}
		
	}

}
