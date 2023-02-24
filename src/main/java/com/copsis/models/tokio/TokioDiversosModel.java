package com.copsis.models.tokio;

import java.util.ArrayList;
import java.util.List;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraJsonModel;
import com.copsis.models.EstructuraUbicacionesModel;

public class TokioDiversosModel {
    private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
	
	public EstructuraJsonModel procesar(String contenido) {
    
        int inicio = 0;
		int fin = 0;
        String vigA="";
        String vigD="";
		StringBuilder newcontenido = new StringBuilder();	
		contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());

        try {
            modelo.setTipo(7);
            modelo.setCia(43);
            
            inicio = contenido.indexOf("PAQUETE");
			fin = contenido.indexOf("Agente");			
			newcontenido.append( fn.extracted(inicio, fin, contenido).replace("día-mes-año: ", ""));			
			for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
              
                if(newcontenido.toString().split("\n")[i].contains("PAQUETE")){
                  modelo.setPlan(newcontenido.toString().split("\n")[i].split("PAQUETE")[1].trim());
                }
                if(newcontenido.toString().split("\n")[i].contains("póliza") && newcontenido.toString().split("\n")[i+1].contains("Desde")){

                modelo.setPoliza(newcontenido.toString().split("\n")[i+2].split("###")[0]);
                }
                if(newcontenido.toString().split("\n")[i].contains("Desde")){
                    vigD = newcontenido.toString().split("\n")[i].split("Desde")[1]
                    .split("del")[1].replace("de", "-").replace(" ", "").trim();
                     modelo.setVigenciaDe( fn.formatDateMonthCadena(vigD));
                }
                if(newcontenido.toString().split("\n")[i].contains("Hasta")){
                    vigA = newcontenido.toString().split("\n")[i].split("Hasta")[1]
                    .split("del")[1].split("###")[0].replace("de", "-").replace(" ", "").trim();
                     modelo.setVigenciaA( fn.formatDateMonthCadena(vigA));
                }
                if(newcontenido.toString().split("\n")[i].contains("Asegurado")){
                    modelo.setCteNombre(newcontenido.toString().split("\n")[i].split("Asegurado")[1].replace(":", "").trim());
                }
                if(newcontenido.toString().split("\n")[i].contains("Domicilio")){
                    modelo.setCteDireccion(newcontenido.toString().split("\n")[i].split("Domicilio")[1].replace(":", "").trim());
                }
                if(newcontenido.toString().split("\n")[i].contains("RFC:")){
                    modelo.setRfc(newcontenido.toString().split("\n")[i].split("RFC:")[1].trim());
                }
                if(newcontenido.toString().split("\n")[i].contains("RFC:")){
                    modelo.setRfc(newcontenido.toString().split("\n")[i].split("RFC:")[1].trim());
                }
                if(newcontenido.toString().split("\n")[i].contains("Prima neta")) {
                    List<String> valores = fn.obtenerListNumeros(newcontenido.toString().split("\n")[i]);
                    modelo.setPrimaneta(fn.castBigDecimal(fn.castDouble(valores.get(0))));
                }
                if(newcontenido.toString().split("\n")[i].contains("Recargo")) {
                    List<String> valores = fn.obtenerListNumeros(newcontenido.toString().split("\n")[i]);
                    modelo.setRecargo(fn.castBigDecimal(fn.castDouble(valores.get(0))));
                }
                if(newcontenido.toString().split("\n")[i].contains("expedición")) {
                    List<String> valores = fn.obtenerListNumeros(newcontenido.toString().split("\n")[i]);
                    modelo.setDerecho(fn.castBigDecimal(fn.castDouble(valores.get(0))));
                }
                if(newcontenido.toString().split("\n")[i].contains("I.V.A.")) {
                    List<String> valores = fn.obtenerListNumeros(newcontenido.toString().split("\n")[i]);
                    modelo.setIva(fn.castBigDecimal(fn.castDouble(valores.get(0))));
                }
                if(newcontenido.toString().split("\n")[i].contains("Prima total")) {
                    List<String> valores = fn.obtenerListNumeros(newcontenido.toString().split("\n")[i]);
                    modelo.setPrimaTotal(fn.castBigDecimal(fn.castDouble(valores.get(0))));
                }

            }

            modelo.setMoneda(1);
            modelo.setFormaPago(1);
            modelo.setAgente(contenido.toString().split("Agente")[1].split("\r\n")[0].replace(":", "").trim());
                
            List<EstructuraUbicacionesModel> ubicaciones = new ArrayList<>();
            EstructuraUbicacionesModel ubicacion = new EstructuraUbicacionesModel();

            inicio = contenido.indexOf("ESPECIFICACION DE LA POLIZA ");
			fin = contenido.indexOf("SECCION");	
            newcontenido = new StringBuilder();		
            newcontenido.append(fn.extracted(inicio, fin, contenido));
            for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {              
                if(newcontenido.toString().split("\n")[i].contains("Ubicación")){
                    ubicacion.setCalle(newcontenido.toString().split("\n")[i].split("Ubicación")[1].replace("###", "").trim());
                }
                if(newcontenido.toString().split("\n")[i].contains("Niveles:")){
                    List<String> valores = fn.obtenerListNumeros2(newcontenido.toString().split("\n")[i]);            
                    ubicacion.setNiveles(fn.castInteger(valores.get(0)));
                }
                if(newcontenido.toString().split("\n")[i].contains("Giro:")){
                   ubicacion.setGiro(newcontenido.toString().split("\n")[i].split("Giro:")[1].replace("###", ""));
                }
                if(newcontenido.toString().split("\n")[i].contains("Tipos Constructivos:")){
                   ubicacion.setMuros(1);
                }
            }
            ubicaciones.add(ubicacion);            
            modelo.setUbicaciones(ubicaciones);	


            return modelo;
        } catch (Exception ex) {
   
            modelo.setError(TokioDiversosModel.this.getClass().getTypeName() + " | " + ex.getMessage() + " | " + ex.getCause());
			return modelo;
        }
    }
}
