package com.copsis.models.allians;

import java.util.ArrayList;
import java.util.List;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraJsonModel;
import com.copsis.models.EstructuraUbicacionesModel;

public class AllianasDiversosBModel {
	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
	private String contenido = "";
	
	public  EstructuraJsonModel procesar(String contenido) {
		StringBuilder direccion = new StringBuilder();
		StringBuilder newcontenido = new StringBuilder();
		int inicio = 0;
		int fin = 0;		
		contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());
		try {
			modelo.setTipo(7);		
			modelo.setCia(4);

			
			inicio = contenido.indexOf("Razon Social Asegurado");
			fin = contenido.indexOf("Datos de la ubicación Asegurada");
			newcontenido.append( fn.extracted(inicio, fin, contenido));
			for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
			
				if(newcontenido.toString().split("\n")[i].contains("Razon Social")) {
					modelo.setCteNombre(newcontenido.toString().split("\n")[i+1].split("###")[0]);	
				}
				if(newcontenido.toString().split("\n")[i].split("-").length > 3) {
					modelo.setPoliza(newcontenido.toString().split("\n")[i].split("###")[0]);
					modelo.setMoneda(fn.buscaMonedaEnTexto(newcontenido.toString().split("\n")[i]));
					modelo.setFormaPago(fn.formaPagoSring(newcontenido.toString().split("\n")[i]));
					modelo.setFechaEmision(fn.formatDateMonthCadena(fn.obtenVigePoliza(newcontenido.toString().split("\n")[i]).get(0)));
					modelo.setVigenciaDe(fn.formatDateMonthCadena(fn.obtenVigePoliza(newcontenido.toString().split("\n")[i]).get(1)));
					modelo.setVigenciaA(fn.formatDateMonthCadena(fn.obtenVigePoliza(newcontenido.toString().split("\n")[i]).get(2)));					
				}
				if(newcontenido.toString().split("\n")[i].contains("Agente:")) {					
					modelo.setCveAgente(fn.numTx(newcontenido.toString().split("\n")[i].split("Agente:")[1].replace("###", "")));
					modelo.setAgente(newcontenido.toString().split("\n")[i].split("Agente:")[1].split(modelo.getCveAgente())[1].replace("###", ""));
				}
				if(newcontenido.toString().split("\n")[i].contains("RFC")) {
					modelo.setRfc(newcontenido.toString().split("\n")[i].split("RFC:")[1].replace("###", "").trim());
				}
				if(newcontenido.toString().split("\n")[i].contains("Calle")) {
					direccion.append(newcontenido.toString().split("\n")[i].split("Calle:")[1].replace("###", "").trim());
				}
				if(newcontenido.toString().split("\n")[i].contains("Colonia")) {
					direccion.append(newcontenido.toString().split("\n")[i].split("Colonia:")[1].replace("###", "").trim());
				}
				if(newcontenido.toString().split("\n")[i].contains("Población")) {
					direccion.append("  " +newcontenido.toString().split("\n")[i].split("Población:")[1].replace("###", "").trim() +" ");
				}				
				if(newcontenido.toString().split("\n")[i].contains("País")) {
	        		direccion.append(newcontenido.toString().split("\n")[i].split("País:")[1].replace("###", "").trim());
				}
				if(newcontenido.toString().split("\n")[i].contains("Estado")) {
					direccion.append(newcontenido.toString().split("\n")[i].split("Estado:")[1].replace("###", "").trim());				
				}				
				if(newcontenido.toString().split("\n")[i].contains("C.P:")) {
	        		modelo.setCp(newcontenido.toString().split("\n")[i].split("C.P:")[1].replace("###", "").trim());
				}				
			}			
			modelo.setCteDireccion(direccion.toString());
			
			
//			 System.out.println(contenido);
			inicio = contenido.indexOf("Datos de la ubicación Asegurada");
			fin = contenido.indexOf("Año Construcción");
			newcontenido = new StringBuilder();
			newcontenido.append( fn.extracted(inicio, fin, contenido));
			List<EstructuraUbicacionesModel> ubicaciones = new ArrayList<>();
			EstructuraUbicacionesModel ubicacion = new EstructuraUbicacionesModel();
			
			for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
				System.out.println(newcontenido.toString().split("\n")[i]);
				if(newcontenido.toString().split("\n")[i].contains("calle")) {
				ubicacion.setCalle(newcontenido.toString().split("\n")[i].split("calle")[1].replace("###", "").trim());	
				}
				
				if(newcontenido.toString().split("\n")[i].contains("Tipo Muros:") &&  newcontenido.toString().split("\n")[i].contains("Tipo Muros:")) {
					System.out.println("casasa==========>" +fn.material(newcontenido.toString().split("\n")[i].split("Muros:")[1].split("Tipo Techo:")[0].replace("###", "")));
				}
			}
		
			return modelo;
		} catch (Exception ex) {
			modelo.setError(AllianasDiversosBModel.this.getClass().getTypeName() + " | " + ex.getMessage() + " | "
					+ ex.getCause());
			return modelo;
		}
	}
}
