package com.copsis.models.berkley;

import java.util.ArrayList;
import java.util.List;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraCoberturasModel;
import com.copsis.models.EstructuraJsonModel;

public class BerkleyDiversosModel {
    private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();

    public EstructuraJsonModel procesar(String contenido) {
        int inicio = 0;
		int fin = 0;
		
		StringBuilder newcontenido = new StringBuilder();
		contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales())
        .replace("NO. POLIZA", "NO.POLIZA");
        
        try {
           
			modelo.setTipo(7);
			modelo.setCia(106);

            inicio = contenido.indexOf("OFICINA");
			fin  = contenido.indexOf("Prima Neta");	
			newcontenido = new StringBuilder();
			newcontenido.append(fn.extracted(inicio, fin, contenido));

            for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
            
                if(newcontenido.toString().split("\n")[i].contains("NO.POLIZA") && newcontenido.toString().split("\n")[i].contains("ENDOSO")
                 &&  newcontenido.toString().split("\n")[i+1].split("###").length == 7){
                    modelo.setPoliza(newcontenido.toString().split("\n")[i+1].split("###")[4]);
                   
                }
                if(newcontenido.toString().split("\n")[i].contains("Contratante") ){
                  modelo.setCteNombre(newcontenido.toString().split("\n")[i].split("Contratante")[1].replace("###", "").trim());
                }
                if(newcontenido.toString().split("\n")[i].contains("Domicilio de Cobro") ){
                  modelo.setCteDireccion(newcontenido.toString().split("\n")[i].split("Cobro")[1].replace("###", "").trim());
                }
               
                if(newcontenido.toString().split("\n")[i].contains("Entidad-C.P.") ){
                    List<String> valores = fn.obtenerListNumeros2(newcontenido.toString().split("\n")[i]);
                    modelo.setCp(valores.get(0));
                  }
                if(newcontenido.toString().split("\n")[i].contains("RFC:") ){
                    modelo.setRfc(newcontenido.toString().split("\n")[i].split("RFC:")[1].replace("###", "").trim());
                }
                if(newcontenido.toString().split("\n")[i].contains("Desde") && newcontenido.toString().split("\n")[i].contains("12:00")){
                 modelo.setVigenciaDe(fn.formatDateMonthCadena(newcontenido.toString().split("\n")[i].split("Desde")[1].split("12")[0].replace("###", "").trim().replace(" ", "-")));
                }
                if(newcontenido.toString().split("\n")[i].contains("Hasta") && newcontenido.toString().split("\n")[i].contains("12:00")){
                    modelo.setVigenciaA(fn.formatDateMonthCadena(newcontenido.toString().split("\n")[i].split("Hasta")[1].split("12")[0].replace("###", "").trim().replace(" ", "-")));
           
                }
              
                if(newcontenido.toString().split("\n")[i].contains("Moneda") && newcontenido.toString().split("\n")[i].contains("Forma de Pago:")){
                  modelo.setMoneda(fn.buscaMonedaEnTexto(newcontenido.toString().split("\n")[i]));
                  modelo.setFormaPago(fn.formaPagoSring(newcontenido.toString().split("\n")[i]));
                }
                

            }

            if(modelo.getVigenciaDe().length() >0){
              modelo.setFechaEmision(modelo.getVigenciaDe());
            }

            if (fn.diferencia(modelo.getVigenciaDe(), modelo.getVigenciaA()) <  1){

              modelo.setVigenciaA(fn.calcvigenciaA(modelo.getVigenciaDe(), 12));
            }

            inicio = contenido.indexOf("Prima Neta");
			fin  = contenido.indexOf("El Asegurado y Berkley");	
			newcontenido = new StringBuilder();
			newcontenido.append(fn.extracted(inicio, fin, contenido));

            for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
                if(newcontenido.toString().split("\n")[i].contains("Prima Neta")){
                    List<String> valores = fn.obtenerListNumeros(newcontenido.toString().split("\n")[i+1]);
                    modelo.setPrimaneta(fn.castBigDecimal(fn.castDouble(valores.get(0))));
                    modelo.setDerecho(fn.castBigDecimal(fn.castDouble(valores.get(2))));
                    modelo.setRecargo(fn.castBigDecimal(fn.castDouble(valores.get(1))));
                    modelo.setIva(fn.castBigDecimal(fn.castDouble(valores.get(3))));                    
                    modelo.setPrimaTotal(fn.castBigDecimal(fn.castDouble(valores.get(4))));
                }
            }

          
            inicio = contenido.indexOf("SECCIONES Y COBERTURAS AMPARADAS");
			fin  = contenido.indexOf("DESGLOSE DE RIESGOS");	
			newcontenido = new StringBuilder();
			newcontenido.append(fn.extracted(inicio, fin, contenido));
            List<EstructuraCoberturasModel> coberturas = new ArrayList<>();
            for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
            
             if(!newcontenido.toString().split("\n")[i].contains("SECCIONES")
             && !newcontenido.toString().split("\n")[i].contains("SECCION")){
                
                EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();
                cobertura.setNombre(newcontenido.toString().split("\n")[i].split("###")[0]);
                if(newcontenido.toString().split("\n")[i].split("###").length >1){
                    cobertura.setSa(newcontenido.toString().split("\n")[i].split("###")[1]);
                
                }
                    coberturas.add(cobertura); 
                
                
                
             }
            }
            modelo.setCoberturas(coberturas);
            
            return modelo;
        } catch (Exception ex) {
            modelo.setError(BerkleyDiversosModel.this.getClass().getTypeName() + " | " + ex.getMessage() + " | " + ex.getCause());
			return modelo;
        }
    }
    
}
