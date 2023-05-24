package com.copsis.models.mapfre;

import java.util.ArrayList;
import java.util.List;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraCoberturasModel;
import com.copsis.models.EstructuraJsonModel;
import com.copsis.models.EstructuraRecibosModel;

public class MapfreVidaDModel {
    private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
    public EstructuraJsonModel procesar(String contenido) {
        int inicio = 0;
		int fin = 0;
        contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales())
        .replace("FORMA###DE###PAGO:", "FORMA DE PAGO:")
        .replace("PRIMA###NETA:", "PRIMA NETA:")
        .replace("PRIMA###TOTAL:", "PRIMA TOTAL:");
        StringBuilder newcontenido = new StringBuilder();
        try {
            modelo.setTipo(5);
			modelo.setCia(22);

             
            inicio = contenido.indexOf("VIDA INDIVIDUAL TRADICIONAL");
			fin = contenido.indexOf("MAPFRE###MÉXICO,###S.A");
			newcontenido.append(fn.extracted(inicio, fin, contenido).replace("las###12:00 P.M.", ""));
            for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
         
               if(newcontenido.toString().split("\n")[i].contains("PÓLIZA-ENDOSO")){
                 modelo.setPoliza(newcontenido.toString().split("\n")[i].split("PÓLIZA-ENDOSO")[1].replace("###", ""));
               }
               if(newcontenido.toString().split("\n")[i].contains("EMISIÓN")&&
                newcontenido.toString().split("\n")[i+1].contains("AGENTE:")){
                    String x =newcontenido.toString().split("\n")[i+1].split("AGENTE:")[1].replace("###", "");
                    if(x.split(",").length >1){
                      x = x.split(",")[1] +""+ x.split(",")[0];
                    }
                modelo.setAgente(x.trim());
               }
               if(newcontenido.toString().split("\n")[i].contains("DOMICILIO:")){
                modelo.setCteDireccion(newcontenido.toString().split("\n")[i].split("DOMICILIO:")[1].replace("###", "").trim());
               }
               if(newcontenido.toString().split("\n")[i].contains("CLAVE DE AGENTE:")){
                modelo.setCveAgente(newcontenido.toString().split("\n")[i].split("AGENTE:")[1].replace("###", ""));
               }
               if(newcontenido.toString().split("\n")[i].contains("VIGENCIA") && newcontenido.toString().split("\n")[i].contains("DESDE")){
                 List<String> valores = fn.obtenVigePoliza(newcontenido.toString().split("\n")[i]);
                 modelo.setVigenciaDe(fn.formatDateMonthCadena(valores.get(0)));
                 modelo.setFechaEmision(modelo.getVigenciaDe());
               }
               if(newcontenido.toString().split("\n")[i].contains("HASTA")){
                List<String> valores = fn.obtenVigePoliza(newcontenido.toString().split("\n")[i]);
                modelo.setVigenciaA(fn.formatDateMonthCadena(valores.get(0)));
               }
               if(newcontenido.toString().split("\n")[i].contains("NOMBRE") && newcontenido.toString().split("\n")[i].contains("RAZÓN")
               && newcontenido.toString().split("\n")[i].contains("C.P:")){
                  modelo.setCteNombre( newcontenido.toString().split("\n")[i].split("RAZÓN")[1].split("C.P:")[0].replace("###", "").trim());
                  List<String> valores = fn.obtenerListNumeros2(newcontenido.toString().split("\n")[i]);
                  modelo.setCp(valores.get(0));
                }

               if(newcontenido.toString().split("\n")[i].contains("R.F.C:")){
                     modelo.setRfc(newcontenido.toString().split("\n")[i].split("R.F.C:")[1].replace("###", "").trim());
               }

               if(newcontenido.toString().split("\n")[i].contains("FORMA DE PAGO:") && newcontenido.toString().split("\n")[i].contains("PRIMA NETA:") ){
                List<String> valores = fn.obtenerListNumeros2(newcontenido.toString().split("\n")[i].replace(",", ""));
                modelo.setPrimaneta(fn.castBigDecimal(fn.castDouble(valores.get(0))));
                modelo.setFormaPago(fn.formaPagoSring(newcontenido.toString().split("\n")[i]));
               }
              
               if(newcontenido.toString().split("\n")[i].contains("MONEDA:") && newcontenido.toString().split("\n")[i].contains("EXPEDICIÓN")){
                 modelo.setMoneda(fn.buscaMonedaEnTexto(newcontenido.toString().split("\n")[i]));
                 List<String> valores = fn.obtenerListNumeros2(newcontenido.toString().split("\n")[i]);
                 modelo.setDerecho(fn.castBigDecimal(fn.castDouble(valores.get(0))));
               }
               if(newcontenido.toString().split("\n")[i].contains("RECARGO") ){
                List<String> valores = fn.obtenerListNumeros2(newcontenido.toString().split("\n")[i]);
                 modelo.setRecargo(fn.castBigDecimal(fn.castDouble(valores.get(0))));

               }
               if(newcontenido.toString().split("\n")[i].contains("I.V.A:") ){
                List<String> valores = fn.obtenerListNumeros2(newcontenido.toString().split("\n")[i]);
                modelo.setIva(fn.castBigDecimal(fn.castDouble(valores.get(0))));
               }
               if(newcontenido.toString().split("\n")[i].contains("PRIMA TOTAL:") ){
                List<String> valores = fn.obtenerListNumeros2(newcontenido.toString().split("\n")[i].replace(",", ""));
                modelo.setPrimaTotal(fn.castBigDecimal(fn.castDouble(valores.get(0))));
               }
              
            }

           
            inicio = contenido.indexOf("COBERTURAS###AMPARADAS");
			fin = contenido.indexOf("INFORMACIÓN ADICIONAL");
			newcontenido = new StringBuilder();
            newcontenido.append(fn.extracted(inicio, fin, contenido).replace("las###12:00 P.M.", ""));
            List<EstructuraCoberturasModel> coberturas = new ArrayList<>();
            for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
                EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();
                if(!newcontenido.toString().split("\n")[i].contains("COBERTURAS")
                && !newcontenido.toString().split("\n")[i].contains("PLAN DE SEGURO")
                && !newcontenido.toString().split("\n")[i].contains("PLAZO")
                && !newcontenido.toString().split("\n")[i].contains("COBERTURA")){
                 
                    if(newcontenido.toString().split("\n")[i].split("###").length == 5){
                        if(newcontenido.toString().split("\n")[i].split("###")[0].contains("EXENCIÓN DEL PAGO DE")){
                            cobertura.setNombre(newcontenido.toString().split("\n")[i].split("###")[0] +" PRIMAS POR INVALIDEZ TOTAL Y PERMANENTE BIT" );
                        }else{
                            cobertura.setNombre(newcontenido.toString().split("\n")[i].split("###")[0]);
                        }
                        
                        cobertura.setSa(newcontenido.toString().split("\n")[i].split("###")[1]);
                        coberturas.add(cobertura);
                    }
                }
            }
            modelo.setCoberturas(coberturas);

            List<EstructuraRecibosModel> recibos = new ArrayList<>();
            EstructuraRecibosModel recibo = new EstructuraRecibosModel();

            if (modelo.getFormaPago() == 1) {
                recibo.setReciboId("");
                recibo.setSerie("1/1");
                recibo.setVigenciaDe(modelo.getVigenciaDe());
                recibo.setVigenciaA(modelo.getVigenciaA());
                if (recibo.getVigenciaDe().length() > 0) {
                    recibo.setVencimiento(fn.dateAdd(recibo.getVigenciaDe(), 30, 1));
                }
                recibo.setPrimaneta(modelo.getPrimaneta());
                recibo.setDerecho(modelo.getDerecho());
                recibo.setRecargo(modelo.getRecargo());
                recibo.setIva(modelo.getDerecho());

                recibo.setPrimaTotal(modelo.getPrimaTotal());
                recibo.setAjusteUno(modelo.getAjusteUno());
                recibo.setAjusteDos(modelo.getAjusteDos());
                recibo.setCargoExtra(modelo.getCargoExtra());
                recibos.add(recibo);
            }

            modelo.setRecibos(recibos);
            
            
            return modelo;
        } catch (Exception ex) {
            modelo.setError(MapfreVidaDModel.this.getClass().getTypeName() + " - catch:" + ex.getMessage() + " | " + ex.getCause());
             return modelo;
        }
    }
}
