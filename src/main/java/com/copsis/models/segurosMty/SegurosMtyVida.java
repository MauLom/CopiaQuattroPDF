package com.copsis.models.segurosMty;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraJsonModel;
import com.copsis.models.planSeguro.PlanSeguroSaludModel;

public class SegurosMtyVida {
	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();	
	private String contenido = "";
	
	public  SegurosMtyVida(String contenido){
    	this.contenido = contenido;
    }
	
	 public EstructuraJsonModel procesar() {
			int inicio = 0;
			int fin = 0;
			StringBuilder newcontenido = new StringBuilder();
			contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());
			
			try {
				modelo.setTipo(5);
				modelo.setCia(25);
				
			
				inicio = contenido.indexOf("CARÁTULA DE LA PÓLIZA");
				fin = contenido.indexOf("BENEFICIOS");				
				newcontenido.append( fn.extracted(inicio, fin, contenido));

				for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
					System.out.println(newcontenido.toString().split("\n")[i]);
					if(newcontenido.toString().split("\n")[i].contains("PLAN BÁSICO") &&  newcontenido.toString().split("\n")[i].contains("PÓLIZA")) {
						modelo.setPlan(newcontenido.toString().split("\n")[i].split("PLAN BÁSICO")[1].split("PÓLIZA")[0].replace("###", "").trim());
						modelo.setPoliza(newcontenido.toString().split("\n")[i].split("PÓLIZA")[1].replace("No.", "").replace("###", "").trim());
					}
					if(newcontenido.toString().split("\n")[i].contains("CONTRATANTE") && newcontenido.toString().split("\n")[i].contains("TIPO DE PÓLIZA") ) {
						modelo.setCteNombre(newcontenido.toString().split("\n")[i].split("CONTRATANTE")[1].split("TIPO DE PÓLIZA")[0].replace("###", "").trim());						
					}
					
					if(newcontenido.toString().split("\n")[i].contains("EMISIÓN")) {			
						modelo.setFechaEmision(fn.formatDateMonthCadena(fn.obtenerMes(newcontenido.toString().split("\n")[i].toUpperCase())));
						modelo.setVigenciaDe(modelo.getFechaEmision());
						
					}
					if(newcontenido.toString().split("\n")[i].contains("NACIMIENTO")) {	
						System.out.println(fn.obtenerMes(newcontenido.toString().split("\n")[i].toUpperCase()));
					}
					
					
				}

				
				return modelo;
			} catch (Exception ex) {
				modelo.setError(SegurosMtyVida.this.getClass().getTypeName() + " | " + ex.getMessage() + " | " + ex.getCause());
				 return modelo;
			}
		 
	 }

}
