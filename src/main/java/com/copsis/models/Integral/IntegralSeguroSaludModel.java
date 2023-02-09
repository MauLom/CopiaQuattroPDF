package com.copsis.models.Integral;

import java.util.ArrayList;
import java.util.List;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraAseguradosModel;
import com.copsis.models.EstructuraCoberturasModel;
import com.copsis.models.EstructuraJsonModel;

public class IntegralSeguroSaludModel {
    private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
    public EstructuraJsonModel procesar(String contenido) {
        int inicio = 0;
		int fin = 0;
		StringBuilder newcontenido = new StringBuilder();
        contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());

        try {
            modelo.setTipo(3);
			modelo.setCia(83);
         
			inicio = contenido.indexOf("PÓLIZA INDIVIDUAL");
			fin = contenido.indexOf("DATOS DE LOS ASEGURADOS");		
            newcontenido.append( fn.extracted(inicio, fin, contenido).replace("día-mes-año: ", ""));
			
			for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
             
                if(newcontenido.toString().split("\n")[i].contains("PÓLIZA NO.") && newcontenido.toString().split("\n")[i].contains("AGENTE")){
                    modelo.setPoliza(newcontenido.toString().split("\n")[i].split("PÓLIZA NO.")[1].split("AGENTE")[0].replace("###", "").trim());
                }
                if(newcontenido.toString().split("\n")[i].contains("NOMBRE:") && newcontenido.toString().split("\n")[i].contains("SOLICITUD NO.")){
                    modelo.setCteNombre(newcontenido.toString().split("\n")[i].split("NOMBRE:")[1].replace("###", "").trim());
                }
                if(newcontenido.toString().split("\n")[i].contains("DOMICILIO")){
                    modelo.setCteDireccion(newcontenido.toString().split("\n")[i+1]);
                    if(modelo.getCteDireccion().length() > 0){
                     modelo.setCp(modelo.getCteDireccion().split("CP")[1].trim().substring(0,5));
                    }
                } 
                if(newcontenido.toString().split("\n")[i].contains("RFC")){
                    modelo.setRfc(newcontenido.toString().split("\n")[i+1].split("###")[1]);
                }
            }

          


            inicio = contenido.indexOf("DATOS DE LOS ASEGURADOS");
			fin = contenido.indexOf("ESCRIPCIÓN DE LA PÓLIZA");	
       
            newcontenido = new StringBuilder();		
			newcontenido.append( fn.extracted(inicio, fin, contenido));
            List<EstructuraAseguradosModel> asegurados = new ArrayList<>();
            for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
                EstructuraAseguradosModel asegurado = new EstructuraAseguradosModel();
                if(newcontenido.toString().split("\n")[i].split("-").length > 3){                
                    asegurado.setNombre(newcontenido.toString().split("\n")[i].split("###")[1].trim());
                    asegurado.setSexo(fn.sexo(newcontenido.toString().split("\n")[i].split("###")[1]) ? 1:0);
                    asegurado.setNacimiento(fn.formatDateMonthCadena(fn.obtenVigePoliza( newcontenido.toString().split("\n")[i]).get(0)));
                    asegurado.setAntiguedad(fn.formatDateMonthCadena(fn.obtenVigePoliza( newcontenido.toString().split("\n")[i]).get(1)));
                    asegurados.add(asegurado);
                }
            }
            modelo.setAsegurados(asegurados);


            inicio = contenido.indexOf("DESCRIPCIÓN DE LA PÓLIZA");
			fin = contenido.indexOf("COBERTURAS INCLUIDAS");	


            newcontenido = new StringBuilder();		
			newcontenido.append( fn.extracted(inicio, fin, contenido));
            for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {               
                if(newcontenido.toString().split("\n")[i].contains("Desde") && newcontenido.toString().split("\n")[i].contains("Hasta")){
                 String x = newcontenido.toString().split("\n")[i].split("Desde")[1]
                 .replace("las 00:00 del día", "").replace("las 24:00 del día", "")
                 .replace("del", "###").replace("de", "###").replace(" ", "");
                 modelo.setVigenciaDe(fn.formatDateMonthCadena(x.split("Hasta")[0].replace("###", "-")));
                 modelo.setVigenciaA(fn.formatDateMonthCadena(x.split("Hasta")[1].replace("###", "-")));
                 modelo.setFechaEmision(modelo.getVigenciaDe());

                 
                }
            }
            


            inicio = contenido.indexOf("COBERTURAS INCLUIDAS");
			fin = contenido.indexOf("PRIMA NETA");	
       
            newcontenido = new StringBuilder();		
			newcontenido.append( fn.extracted(inicio, fin, contenido));
            List<EstructuraCoberturasModel> coberturas = new ArrayList<>();				
            for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {            
                EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();
                if(!newcontenido.toString().split("\n")[i].contains("SUMA ASEGURADA") && !newcontenido.toString().split("\n")[i].contains("GENERALES")
                 && !newcontenido.toString().split("\n")[i].contains("PRIMA")){                                    
                    if( newcontenido.toString().split("\n")[i].split("###").length == 3){
                        cobertura.setNombre(newcontenido.toString().split("\n")[i].split("###")[0]);
                        cobertura.setCopago(newcontenido.toString().split("\n")[i].split("###")[1]);
                        cobertura.setSa(newcontenido.toString().split("\n")[i].split("###")[2].replace("\r", ""));
                        coberturas.add(cobertura);
                    }
                }
            }
            modelo.setCoberturas(coberturas);


            inicio = contenido.indexOf("PRIMA NETA");
			fin = contenido.indexOf("MEDI ACCESS SEGUROS");	       
            newcontenido = new StringBuilder();		
			newcontenido.append( fn.extracted(inicio, fin, contenido));
            for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {          
            
                if(newcontenido.toString().split("\n")[i].contains("EXPEDICIÓN")){
                    List<String> valores = fn.obtenerListNumeros(newcontenido.toString().split("\n")[i+1]);
            
                    modelo.setPrimaneta(fn.castBigDecimal(fn.castDouble(valores.get(0))));
                    modelo.setRecargo(fn.castBigDecimal(fn.castDouble(valores.get(4))));
                     modelo.setDerecho(fn.castBigDecimal(fn.castDouble(valores.get(5))));
                    
                }
                
                if(newcontenido.toString().split("\n")[i].contains("IVA")){
                    List<String> valores = fn.obtenerListNumeros(newcontenido.toString().split("\n")[i+1]);
                    modelo.setIva(fn.castBigDecimal(fn.castDouble(valores.get(0))));                    
                    modelo.setPrimaTotal(fn.castBigDecimal(fn.castDouble(valores.get(2))));  
                }

            }

            newcontenido = new StringBuilder();		
            return modelo;
        } catch (Exception e) {    
            return modelo;
        }

    }
    
}
