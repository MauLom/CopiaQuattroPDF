package com.copsis.models.potosi;

import java.util.ArrayList;
import java.util.List;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraCoberturasModel;
import com.copsis.models.EstructuraJsonModel;
import com.copsis.models.planSeguro.PlanSeguroSaludModel;

public class PotosiDiversosModel {
	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();	
	private String contenido = "";
	private List<String> vigenciaspo = new ArrayList<>();
	public PotosiDiversosModel(String contenido) {
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
			
		
			inicio = contenido.indexOf("Número de Póliza");
			fin = contenido.indexOf("Información del bien");			
			newcontenido.append( fn.extracted(inicio, fin, contenido));
		
			for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
				
				if(newcontenido.toString().split("\n")[i].contains("Número de Póliza")) {
					modelo.setPoliza(newcontenido.toString().split("\n")[i].split("Póliza")[1].replace("###", ""));
				}
				if(newcontenido.toString().split("\n")[i].contains("Vigencia") && newcontenido.toString().split("\n")[i+1].split("-").length >2) {
					vigenciaspo = fn.obtenVigePoliza(newcontenido.toString().split("\n")[i+1]);
					if(vigenciaspo.size() > 1) {
						modelo.setVigenciaDe(fn.formatDateMonthCadena( vigenciaspo.get(0)));
						modelo.setVigenciaA(fn.formatDateMonthCadena( vigenciaspo.get(1)));
					}
					
				}
				if(newcontenido.toString().split("\n")[i].contains("Contratante") && newcontenido.toString().split("\n")[i+1].split("###")[0].contains("Razón Social")) {
					modelo.setCteNombre(newcontenido.toString().split("\n")[i+1].split("###")[1].trim());
				}
				if(newcontenido.toString().split("\n")[i].contains("R.F.C.") &&  newcontenido.toString().split("\n")[i].contains("Cliente")) {
					modelo.setRfc(newcontenido.toString().split("\n")[i].split("R.F.C.")[1].split("Cliente")[0].replace("###", "").replace("-", "").trim());
				}
				if(newcontenido.toString().split("\n")[i].contains("Domicilio") ) {
					modelo.setCteDireccion(newcontenido.toString().split("\n")[i].split("Domicilio")[1].replace("Fiscal", "").replace("###", "").trim());
					modelo.setCp(newcontenido.toString().split("\n")[i].split("CP")[1].trim().substring(0,5));
				}
				if(newcontenido.toString().split("\n")[i].contains("Plan de Pago")  && newcontenido.toString().split("\n")[i].contains("Moneda") ) {
					modelo.setMoneda(fn.buscaMonedaEnTexto(newcontenido.toString().split("\n")[i]));
					modelo.setFormaPago(fn.formaPagoSring(newcontenido.toString().split("\n")[i]));
				}
				if(newcontenido.toString().split("\n")[i].contains("Agente") && newcontenido.toString().split("\n")[i].contains("Clave del Agente") && newcontenido.toString().split("\n")[i].contains("Fec.Operación")) {
					modelo.setAgente(newcontenido.toString().split("\n")[i].split("Agente")[1].split("Clave")[0].replace("###", "").trim());
					modelo.setCveAgente(newcontenido.toString().split("\n")[i].split("Clave del Agente")[1].split("Fec.")[0].replace("###", "").trim());
				}
				
			}
			
			
			modelo.setFechaEmision(modelo.getVigenciaDe().length() >  0 ? modelo.getVigenciaDe() :"" );
			
		
			
			inicio = contenido.indexOf("Información del movimiento");
			fin = contenido.indexOf("Información de Prima");			
			newcontenido = new StringBuilder();
			newcontenido.append( fn.extracted(inicio, fin, contenido));
			List<EstructuraCoberturasModel> coberturas = new ArrayList<>();
			for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
				EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();
				if(!newcontenido.toString().split("\n")[i].contains("Información") && !newcontenido.toString().split("\n")[i].contains("Coberturas") && !newcontenido.toString().split("\n")[i].contains("SE EXCLUYEN")) {
					cobertura.setNombre(newcontenido.toString().split("\n")[i].split("###")[0]);
					cobertura.setSa(newcontenido.toString().split("\n")[i].split("###")[1]);
					coberturas.add(cobertura);
				}
			}
			modelo.setCoberturas(coberturas);
			
			
			inicio = contenido.indexOf("Tasa de Financiamiento");
			fin = contenido.indexOf("Seguros el Potosí, S.A.");			
			newcontenido = new StringBuilder();
			newcontenido.append( fn.extracted(inicio, fin, contenido));
			for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {	
				if(newcontenido.toString().split("\n")[i].contains("fraccionado") && newcontenido.toString().split("\n")[i+1].split("###").length == 8) {
					modelo.setPrimaneta(fn.castBigDecimal(fn.castFloat(newcontenido.toString().split("\n")[i+1].split("###")[0])));
					modelo.setRecargo(fn.castBigDecimal(fn.castFloat(newcontenido.toString().split("\n")[i+1].split("###")[1])));
					modelo.setDerecho(fn.castBigDecimal(fn.castFloat(newcontenido.toString().split("\n")[i+1].split("###")[2])));
					modelo.setIva(fn.castBigDecimal(fn.castFloat(newcontenido.toString().split("\n")[i+1].split("###")[4])));
					modelo.setPrimaTotal(fn.castBigDecimal(fn.castFloat(newcontenido.toString().split("\n")[i+1].split("###")[7])));								
				}
			}
			
			
			  return modelo;
		} catch (Exception ex) {
			modelo.setError(PotosiDiversosModel.this.getClass().getTypeName() + " | " + ex.getMessage() + " | " + ex.getCause());
			return modelo;
		}
	}
}
