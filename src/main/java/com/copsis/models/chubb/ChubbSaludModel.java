package com.copsis.models.chubb;

import java.util.ArrayList;
import java.util.List;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraCoberturasModel;
import com.copsis.models.EstructuraJsonModel;

public class ChubbSaludModel {
    DataToolsModel fn = new DataToolsModel();
    EstructuraJsonModel modelo = new EstructuraJsonModel();

    public EstructuraJsonModel procesar(String contenido) {
       
        int inicio = 0;
        int fin = 0;
        StringBuilder newcontenido = new StringBuilder();
        StringBuilder direccion = new StringBuilder();
        contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales())
        .replace("Artículo###25", "Artículo 25");

        try {


          
            modelo.setTipo(3);
			modelo.setCia(1);
            inicio = contenido.indexOf("Póliza:");
			fin = contenido.indexOf("Suma asegurada");			
			newcontenido.append( fn.extracted(inicio, fin, contenido));
            for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {  
          
                if(newcontenido.toString().split("\n")[i].contains("Póliza:") && newcontenido.toString().split("\n")[i].contains("Vigencia:")){
                  modelo.setPoliza(newcontenido.toString().split("\n")[i].split("Póliza:")[1].split("Vigencia")[0].replace("###", "").trim());
                  modelo.setVigenciaDe(fn.formatDateMonthCadena(fn.obtenVigePoliza2(newcontenido.toString().split("\n")[i]).get(0)));
                  modelo.setVigenciaA(fn.formatDateMonthCadena(fn.obtenVigePoliza2(newcontenido.toString().split("\n")[i]).get(1)));
                  modelo.setFechaEmision(modelo.getVigenciaDe());
                
                }
                if(newcontenido.toString().split("\n")[i].contains("Asegurado:") && newcontenido.toString().split("\n")[i].contains("C.P:")){
                    modelo.setCteNombre(newcontenido.toString().split("\n")[i].split("Asegurado:")[1].split("C.P:")[0].replace("###", "").trim());
                    modelo.setCp(newcontenido.toString().split("\n")[i].split("C.P:")[1].replace("###","").trim());
                }
                if(newcontenido.toString().split("\n")[i].contains("Moneda:") && newcontenido.toString().split("\n")[i].contains("Forma de pago")){
                    
                    modelo.setFormaPago(fn.formaPagoSring(newcontenido.toString().split("\n")[i]));
                    modelo.setMoneda(fn.buscaMonedaEnTexto(newcontenido.toString().split("\n")[i]));
                }

                if(newcontenido.toString().split("\n")[i].contains("Domicilio:") && newcontenido.toString().split("\n")[i].contains("Teléfono")){
                    direccion.append(newcontenido.toString().split("\n")[i].split("Domicilio:")[1].split("Teléfono")[0].replace("###", "").trim());     
                    direccion.append(newcontenido.toString().split("\n")[i+1].split("RFC:")[0].replace("###", "").trim());  
                                 
                }
                if(newcontenido.toString().split("\n")[i].contains("RFC:") ){
                  modelo.setRfc(newcontenido.toString().split("\n")[i].split("RFC:")[1].replace("###", "").trim());
                }
                if(newcontenido.toString().split("\n")[i].contains("Clave interna") && newcontenido.toString().split("\n")[i].contains("agente:")){
                 modelo.setAgente(newcontenido.toString().split("\n")[i].split("agente:")[1].replace("###", "").trim());
                }
            }

            modelo.setCteDireccion(direccion.toString());

           
            inicio = contenido.indexOf("Coberturas");
			fin = contenido.indexOf("Prima Neta");
			newcontenido = new StringBuilder();
			newcontenido.append(fn.extracted(inicio, fin, contenido));
			List<EstructuraCoberturasModel> coberturas = new ArrayList<>();
			for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
				EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();                
                if(!newcontenido.toString().split("\n")[i].contains("Coberturas") && (newcontenido.toString().split("\n")[i].split("###").length == 4)) {
						cobertura.setNombre(newcontenido.toString().split("\n")[i].split("###")[0].trim());
						cobertura.setSa(newcontenido.toString().split("\n")[i].split("###")[1].trim());
                        cobertura.setDeducible(newcontenido.toString().split("\n")[i].split("###")[2].trim());
                        cobertura.setCoaseguro(newcontenido.toString().split("\n")[i].split("###")[3].trim());
						coberturas.add(cobertura);
					
                } 
            }
            modelo.setCoberturas(coberturas);

            inicio = contenido.indexOf("Prima Neta");
            fin = contenido.indexOf("Artículo 25");
           
            newcontenido = new StringBuilder();
            newcontenido.append(fn.extracted(inicio, fin, contenido));
            if(modelo.getFormaPago() == 0){
                modelo.setFormaPago(fn.formaPagoSring(newcontenido.toString()));
            }  
         
            
            for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {              
                if(newcontenido.toString().split("\n")[i].contains("Prima Neta")) {
                    List<String> valores = fn.obtenerListNumeros(newcontenido.toString().split("\n")[i]);
                    modelo.setPrimaneta(fn.castBigDecimal(fn.castDouble(valores.get(0))));
                }
                if(newcontenido.toString().split("\n")[i].contains("fraccionado")) {
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
                if(newcontenido.toString().split("\n")[i].contains("Prima Total:")) {
                    List<String> valores = fn.obtenerListNumeros(newcontenido.toString().split("\n")[i]);
                    modelo.setPrimaTotal(fn.castBigDecimal(fn.castDouble(valores.get(0))));
                }
            }

            return modelo;
        } catch (Exception ex) {
            modelo.setError(
                    ChubbSaludModel.this.getClass().getTypeName() + " | " + ex.getMessage() + " | " + ex.getCause());
            return modelo;
        }
    }

}
