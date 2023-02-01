package com.copsis.models.general;

import java.util.ArrayList;
import java.util.List;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraCoberturasModel;
import com.copsis.models.EstructuraJsonModel;
import com.copsis.models.EstructuraRecibosModel;
import com.copsis.models.EstructuraUbicacionesModel;

public class GeneralDiversosModel {
    private DataToolsModel fn = new DataToolsModel();
    private EstructuraJsonModel modelo = new EstructuraJsonModel();

    public EstructuraJsonModel procesar(String contenido) {
        int inicio = 0;
        int fin = 0;
        StringBuilder newcontenido = new StringBuilder();
        StringBuilder direccion = new StringBuilder();

        contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());
        try {
            modelo.setTipo(7);
            modelo.setCia(16);

            inicio = contenido.indexOf("PÓLIZA DEL SEGURO");
            fin = contenido.indexOf("DESCRIPCIÓN DE LAS COBERTURAS");
            newcontenido.append(fn.extracted(inicio, fin, contenido));

            for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
                if (newcontenido.toString().split("\n")[i].contains("C.P.")){
                    modelo.setCp(newcontenido.toString().split("\n")[i].split("C.P.")[1].trim().substring(0,5));
                  }
                if (newcontenido.toString().split("\n")[i].contains("RAMO")
                        && newcontenido.toString().split("\n")[i].contains("DESDE")) {
                    modelo.setVigenciaDe(fn
                            .formatDateMonthCadena(fn.obtenVigePoliza(newcontenido.toString().split("\n")[i]).get(0)));
                }
                if (newcontenido.toString().split("\n")[i].contains("PÓLIZA")
                        && newcontenido.toString().split("\n")[i].contains("SUBRAMO")
                        && newcontenido.toString().split("\n")[i].contains("HASTA LAS")) {
                    modelo.setPoliza(newcontenido.toString().split("\n")[i].split("PÓLIZA")[1].split("SUBRAMO")[0]
                            .replace("###", "").trim());
                    modelo.setVigenciaA(fn
                            .formatDateMonthCadena(fn.obtenVigePoliza(newcontenido.toString().split("\n")[i]).get(0)));

                }

                if (newcontenido.toString().split("\n")[i].contains("R.F.C.")) {
                    modelo.setRfc(newcontenido.toString().split("\n")[i].split("R.F.C.")[1].replace("###", ""));
                }
                if (newcontenido.toString().split("\n")[i].contains("Nombre:")) {
                    modelo.setCteNombre(newcontenido.toString().split("\n")[i].split("Nombre:")[1].replace("###", ""));
                }
                if (newcontenido.toString().split("\n")[i].contains("Calle:")) {
                    direccion.append(newcontenido.toString().split("\n")[i].replace("Calle:", "")
                    .replace("Colonia:", "").replace("###", " ")
                    );
                }
                if (newcontenido.toString().split("\n")[i].contains("No. Ext.")) {
                    direccion.append(newcontenido.toString().split("\n")[i].replace("No. Ext.", "")
                    .replace("Municipio:", "").replace("Estado:", "").replace("###", " "));
                    modelo.setCteDireccion(direccion.toString().trim());
                }
                if (newcontenido.toString().split("\n")[i].contains("EMISIÓN")) {
                    modelo.setFechaEmision(fn
                    .formatDateMonthCadena(fn.obtenVigePoliza(newcontenido.toString().split("\n")[i]).get(0)));

                }
                if (newcontenido.toString().split("\n")[i].contains("MONEDA") 
                && newcontenido.toString().split("\n")[i].contains("FORMA DE PAGO")
                && newcontenido.toString().split("\n")[i].contains("PRIMA BRUTA")
                && newcontenido.toString().split("\n")[i].contains("DESCUENTO")
                && newcontenido.toString().split("\n")[i].contains("I.V.A")) {
                    modelo.setFormaPago(fn.formaPagoSring(newcontenido.toString().split("\n")[i+1]));
                    modelo.setMoneda(fn.buscaMonedaEnTexto(newcontenido.toString().split("\n")[i+1]));
                    List<String> valores = fn.obtenerListNumeros(newcontenido.toString().split("\n")[i+1]);
                    modelo.setPrimaneta(fn.castBigDecimal(fn.castDouble(valores.get(0))));
                    modelo.setIva(fn.castBigDecimal(fn.castDouble(valores.get(3))));
                  
                }
              
                if (newcontenido.toString().split("\n")[i].contains("RECARGO")
                 && newcontenido.toString().split("\n")[i].contains("EXPEDICIÓN")){
                    List<String> valores = fn.obtenerListNumeros(newcontenido.toString().split("\n")[i+1]);
                    modelo.setPrimaTotal(fn.castBigDecimal(fn.castDouble(valores.get(4))));
                    modelo.setDerecho(fn.castBigDecimal(fn.castDouble(valores.get(3))));
                    modelo.setRecargo(fn.castBigDecimal(fn.castDouble(valores.get(2))));
                    modelo.setPrimerPrimatotal(fn.castBigDecimal(fn.castDouble(valores.get(0))));
                } 

            }

            inicio = contenido.indexOf("DESCRIPCIÓN DE LAS COBERTURAS");
            fin = contenido.indexOf("BIENES ASEGURADOS");

            newcontenido = new StringBuilder();
            newcontenido.append(fn.extracted(inicio, fin, contenido));
            List<EstructuraCoberturasModel> coberturas = new ArrayList<>();
            for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
                EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();
                
                if(!newcontenido.toString().split("\n")[i].contains("DESCRIPCIÓN")              
                && !newcontenido.toString().split("\n")[i].contains("Según Anexo")
                && !newcontenido.toString().split("\n")[i].contains("Nomenclatura:")){
                   

                    int sp  = newcontenido.toString().split("\n")[i].split("###").length;
                    
                    switch (sp) {
              
                 case 4:
                     cobertura.setNombre(newcontenido.toString().split("\n")[i].split("###")[0]);
                     cobertura.setSa(newcontenido.toString().split("\n")[i].split("###")[1]);
                     cobertura.setDeducible(newcontenido.toString().split("\n")[i].split("###")[2]);
                     cobertura.setCoaseguro(newcontenido.toString().split("\n")[i].split("###")[3].replace("\n", ""));
                     coberturas.add(cobertura);
                     break;    
                 default:
                     break;
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

            if(fn.diferenciaDias(modelo.getVigenciaDe(), modelo.getVigenciaA()) < 30){
                modelo.setVigenciaA(fn.calcvigenciaA(modelo.getVigenciaDe(), 12));
             }
            
            return modelo;
        } catch (Exception ex) {
            modelo.setError(GeneralDiversosModel.this.getClass().getTypeName() + " - catch:" + ex.getMessage() + " | "
                    + ex.getCause());
            return modelo;
        }
    }
}
