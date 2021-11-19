package com.copsis.models.afirme;

import java.util.ArrayList;
import java.util.List;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraCoberturasModel;
import com.copsis.models.EstructuraJsonModel;


public class AfirmeAutosModel {
	// Clases
	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
	// Varaibles
	private String contenido = "";
	private static final String POLIZA ="Poliza:";
	private static final String PRIMANETA ="Prima Neta";
	private static final String INCISO ="Inciso:";
	private static final String CLAVE ="Clave:";
	private static final String CLAVECONDUCTO = "Clave y Nombre del Conducto:";

	public AfirmeAutosModel(String contenido) {
		this.contenido = contenido;
	}

	public EstructuraJsonModel procesar() {
		try {
			StringBuilder newdireccion = new StringBuilder();
			String newcontenido = "";
			int inicio = 0;
			int fin = 0;
			
			contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());
			contenido = contenido.replace("Prima neta", PRIMANETA)
					.replace("Póliza:", POLIZA);
			//tipo
            modelo.setTipo(1);
            //cia
            modelo.setCia(31);

   
            //Datos Generales
            inicio = contenido.indexOf("PÓLIZA DE SEGURO");
            fin = contenido.indexOf("DESGLOSE DE COBERTURAS");
          
            if (inicio > 0 && fin > 0 && inicio < fin) {
				newcontenido = contenido.substring(inicio, fin).replace("\r", "").replace("@@@", "");
				for (int i = 0; i < newcontenido.split("\n").length; i++) {			
					if(newcontenido.split("\n")[i].contains(POLIZA) && newcontenido.split("\n")[i].contains(INCISO)) {
						modelo.setPoliza(newcontenido.split("\n")[i].split(POLIZA)[1].split(INCISO)[0].replace("-", "").replace("###", "").trim());
						modelo.setPolizaGuion(newcontenido.split("\n")[i].split(POLIZA)[1].split(INCISO)[0].replace("###", "").trim());
					}
					if(newcontenido.split("\n")[i].contains("Desde:") && newcontenido.split("\n")[i].contains("Hasta:") && newcontenido.split("\n")[i+1].split("-").length > 2) {						
						modelo.setVigenciaDe(fn.formatDateMonthCadena(newcontenido.split("\n")[i+1].split("###")[1].replace("###", "").trim()));
						modelo.setFechaEmision(modelo.getVigenciaDe());
						modelo.setVigenciaA(fn.formatDateMonthCadena(newcontenido.split("\n")[i+1].split("###")[3].replace("###", "").trim()));
						
					}
					if(newcontenido.split("\n")[i].contains("Marca") && newcontenido.split("\n")[i].contains(CLAVE)) {
						modelo.setMarca( newcontenido.split("\n")[i].split(CLAVE)[1]);
						modelo.setClave( fn.numTx(newcontenido.split("\n")[i].split(CLAVE)[1]));
					}
					if(newcontenido.split("\n")[i].contains("ASEGURADO") && newcontenido.split("\n")[i+2].contains("Y-O")){
						modelo.setCteNombre(newcontenido.split("\n")[i+2].split("Y-O")[0].trim());							
					}
					
					if(modelo.getCteNombre().length() == 0 && newcontenido.split("\n")[i].contains("Tipo y Clase:")) {
						modelo.setCteNombre( newcontenido.split("\n")[i].split("tipo y Clase:")[0].replace("###", "").trim());
					}
					
					if(newcontenido.split("\n")[i].contains("Modelo")){
						modelo.setModelo(Integer.parseInt(newcontenido.split("\n")[i].split("Modelo:")[1].split("###")[1]));
						newdireccion.append(newcontenido.split("\n")[i].split("Modelo:")[0].replace("###", ""));
					}
					if(newcontenido.split("\n")[i].contains("Motor")){
						newdireccion.append(newcontenido.split("\n")[i].split("Motor")[0].replace("###", ""));
						modelo.setCteDireccion(newdireccion.toString());
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
   
            inicio = contenido.indexOf(CLAVECONDUCTO);
            fin = contenido.indexOf("Artículo 20 de");
       
            if (inicio > 0 && fin > 0 && inicio < fin) {
            	newcontenido = contenido.substring(inicio, fin).replace("\r", "").replace("@@@", "");
				for (int i = 0; i < newcontenido.split("\n").length; i++) {
					if (newcontenido.split("\n")[i].contains(CLAVECONDUCTO)) {			
						String x= newcontenido.split("\n")[i].split(CLAVECONDUCTO)[1].replace(" ", "###");
						modelo.setCveAgente(x.split("###")[1].trim());
						modelo.setAgente(x.split(modelo.getCveAgente())[1].replace("###", " "));
						
					}
				}
            	
            }
            

      
            //Primas
            inicio = contenido.indexOf(PRIMANETA);
            fin = contenido.indexOf("Artículo 25 de");
       
            if (inicio > 0 && fin > 0 && inicio < fin) {
				newcontenido = contenido.substring(inicio, fin).replace("\r", "").replace("@@@", "");
				for (int i = 0; i < newcontenido.split("\n").length; i++) {
					if (newcontenido.split("\n")[i].contains(PRIMANETA)) {
						modelo.setPrimaneta( fn.castBigDecimal(fn.castDouble(newcontenido.split("\n")[i].split(PRIMANETA)[1].split("###")[1])));
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
				}
            }
            
          
            //Coberturas
            inicio = contenido.indexOf("DESGLOSE DE COBERTURAS");
            fin = contenido.indexOf(PRIMANETA);
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
            
            
			return modelo;
		} catch (Exception ex) {
			modelo.setError(AfirmeAutosModel.this.getClass().getTypeName() + " - catch:" + ex.getMessage() + " | "
					+ ex.getCause());
			return modelo;
		}
	}

}
