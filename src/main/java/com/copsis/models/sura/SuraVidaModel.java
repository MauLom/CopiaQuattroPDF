package com.copsis.models.sura;

import java.util.ArrayList;
import java.util.List;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraCoberturasModel;
import com.copsis.models.EstructuraJsonModel;

public class SuraVidaModel {
    private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
    public EstructuraJsonModel procesar(String contenido) {
		int inicio;
		int fin;
		StringBuilder newcontenido = new StringBuilder();
		contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());
      
        try {
            modelo.setTipo(5);
			modelo.setCia(88);
			
		
			inicio = contenido.indexOf("Oficina###Ramo###Póliza");
			fin = contenido.indexOf("Regla para determinar la suma asegurada por cobertura");			
			newcontenido.append( fn.extracted(inicio, fin, contenido));
		
			for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {                
                if(newcontenido.toString().split("\n")[i].contains("Oficina") && newcontenido.toString().split("\n")[i].contains("Póliza")){
                    modelo.setPoliza(newcontenido.toString().split("\n")[i+1].split("###") [newcontenido.toString().split("\n")[i+1].split("###").length-1]);
                }
                if(newcontenido.toString().split("\n")[i].contains("Moneda") ){
                    modelo.setMoneda(fn.buscaMonedaEnTexto(newcontenido.toString().split("\n")[i+1]));
                }

                if(newcontenido.toString().split("\n")[i].contains("Datos del contratante") ){
                    modelo.setCteNombre(newcontenido.toString().split("\n")[i+1]);
                    modelo.setCteDireccion(newcontenido.toString().split("\n")[i+2]
                    +" " + newcontenido.toString().split("\n")[i+3].split("###")[0]);
                }

                if(newcontenido.toString().split("\n")[i].contains("Fecha Emisión") && newcontenido.toString().split("\n")[i].contains("Forma de pago") ){
                  modelo.setFormaPago(fn.formaPagoSring(newcontenido.toString().split("\n")[i+1]));
                  List<String> valores = fn.obtenVigePoliza(newcontenido.toString().split("\n")[i+1]);
                  modelo.setFechaEmision(fn.formatDateMonthCadena(valores.get(0)));
                 
                }
                if(newcontenido.toString().split("\n")[i].contains("Vigencia desde") ){
                    List<String> valores = fn.obtenVigePoliza(newcontenido.toString().split("\n")[i]);
                    modelo.setVigenciaDe(fn.formatDateMonthCadena(valores.get(0)));
                }
                if(newcontenido.toString().split("\n")[i].contains("Hasta las") && newcontenido.toString().split("\n")[i].contains("REC.") ){
                    List<String> valores = fn.obtenVigePoliza(newcontenido.toString().split("\n")[i]);
                    modelo.setVigenciaA(fn.formatDateMonthCadena(valores.get(0)));
                }
                if(newcontenido.toString().split("\n")[i].contains("C.P.") ){
                    modelo.setCp(newcontenido.toString().split("\n")[i].split("C.P.")[1].trim().substring(0, 5));
                }
            }


            inicio = contenido.indexOf("Regla para determinar la suma asegurada por cobertura");
			fin = contenido.indexOf("Costo del seguro");	
            newcontenido = new StringBuilder();		
			newcontenido.append( fn.extracted(inicio, fin, contenido));
            List<EstructuraCoberturasModel> coberturas = new ArrayList<>();
            for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {                        
                if( !newcontenido.toString().split("\n")[i].contains("Sumas aseguradas") && 
                !newcontenido.toString().split("\n")[i].contains("Regla para determinar")){
                    EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();
                    if(newcontenido.toString().split("\n")[i].split("###").length == 3 && newcontenido.toString().split("\n")[i].split("###")[1].length() >20){
                        cobertura.setNombre(newcontenido.toString().split("\n")[i].split("###")[1]);
                        coberturas.add(cobertura);	 
                    }                    
                }
            }
            modelo.setCoberturas(coberturas);


            inicio = contenido.indexOf("Recargo por pago fracc.");
			fin = contenido.indexOf("Pág. 1 de 2");	
            newcontenido = new StringBuilder();			
			newcontenido.append( fn.extracted(inicio, fin, contenido));
      	
		
			for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {                
                if(!newcontenido.toString().split("\n")[i].contains("Recargo")){
                    List<String> valores = fn.obtenerListNumeros(newcontenido.toString().split("\n")[i]);
                    modelo.setPrimaneta(fn.castBigDecimal(fn.castDouble(valores.get(0))));
                    modelo.setDerecho(fn.castBigDecimal(fn.castDouble(valores.get(2))));
                    modelo.setRecargo(fn.castBigDecimal(fn.castDouble(valores.get(1))));
                    modelo.setIva(fn.castBigDecimal(fn.castDouble(valores.get(4))));                    
                    modelo.setPrimaTotal(fn.castBigDecimal(fn.castDouble(valores.get(5))));
                }
            }
          
            
            return modelo;
        } catch (Exception ex) {
            modelo.setError(
                SuraVidaModel.this.getClass().getTypeName() + " | " + ex.getMessage() + " | " + ex.getCause());
            return modelo;
        }
    }
}
