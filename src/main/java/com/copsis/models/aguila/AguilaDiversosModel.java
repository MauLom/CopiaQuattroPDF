package com.copsis.models.aguila;

import java.util.ArrayList;
import java.util.List;

import org.aspectj.apache.bcel.classfile.ConstantValue;

import com.copsis.constants.ConstantsValue;
import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraCoberturasModel;
import com.copsis.models.EstructuraJsonModel;

public class AguilaDiversosModel {
    private DataToolsModel fn = new DataToolsModel();
    private EstructuraJsonModel modelo = new EstructuraJsonModel();

    public EstructuraJsonModel procesar(String contenido) {
        int inicio = 0;
        int fin = 0;
        StringBuilder newcontenido = new StringBuilder();
        contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());
        try {
            modelo.setTipo(1);
            modelo.setCia(33);

            inicio = contenido.indexOf("Domicilio del Asegurado");
            fin = contenido.indexOf(ConstantsValue.COBERTURAS_AMPARADAS2);

            newcontenido.append(fn.extracted(inicio, fin, contenido));
            for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {

                if (newcontenido.toString().split("\n")[i].contains("Domicilio del Asegurado")) {
                    modelo.setCteNombre(newcontenido.toString().split("\n")[i + 1].replace("###", "").trim());
                    modelo.setCteDireccion(
                            newcontenido.toString().split("\n")[i + 2].split(ConstantsValue.POLIZA_ACENT)[0]
                                    .replace("###", "").trim());
                }
                if (newcontenido.toString().split("\n")[i].contains(ConstantsValue.POLIZA_NUMERO3)) {
                    modelo.setPoliza(newcontenido.toString().split("\n")[i].split("número:")[1].trim());
                }
                if (newcontenido.toString().split("\n")[i].contains("C.P.")) {
                    modelo.setCp(newcontenido.toString().split("\n")[i].split("C.P.")[1].trim().substring(0, 5));
                }

                if (newcontenido.toString().split("\n")[i].contains("Agente:") &&
                        newcontenido.toString().split("\n")[i + 1].contains("R.F.C.")) {
                    modelo.setAgente(newcontenido.toString().split("\n")[i].split("Agente:")[1].trim() + " "
                            + newcontenido.toString().split("\n")[i + 1].split("###")[1].trim());
                    modelo.setRfc(newcontenido.toString().split("\n")[i + 1].split("R.F.C.")[1].split("###")[0].trim());
                }

                if (newcontenido.toString().split("\n")[i].contains("Vigencia") &&
                        newcontenido.toString().split("\n")[i].contains("Forma de pago") &&
                        newcontenido.toString().split("\n")[i].contains("Moneda")) {
                    List<String> valores = fn.obtenVigePoliza(newcontenido.toString().split("\n")[i + 1]);
                    if (valores.size() > 1) {
                        modelo.setVigenciaDe(fn.formatDateMonthCadena(valores.get(0)));
                        modelo.setVigenciaA(fn.formatDateMonthCadena(valores.get(1)));
                    }
                    modelo.setFormaPago(4);

                    modelo.setMoneda(fn.buscaMonedaEnTexto(newcontenido.toString().split("\n")[i + 1]));
                }
            }

            inicio = contenido.indexOf(ConstantsValue.COBERTURAS_AMPARADAS2);
            fin = contenido.indexOf(ConstantsValue.RECARGO_PAGO);
            List<EstructuraCoberturasModel> coberturas = new ArrayList<>();
            newcontenido = new StringBuilder();
            newcontenido.append(fn.extracted(inicio, fin, contenido));
            for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
                EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();
                if (!newcontenido.toString().split("\n")[i].contains("Coberturas") && 
                !newcontenido.toString().split("\n")[i].contains("Prima Neta")) {
                    if( newcontenido.toString().split("\n")[i].split("###").length == 4){
                     cobertura.setSeccion(newcontenido.toString().split("\n")[i].split("###")[0]);
                     cobertura.setNombre(newcontenido.toString().split("\n")[i].split("###")[1]);
                     cobertura.setSa(newcontenido.toString().split("\n")[i].split("###")[2]);
                     coberturas.add( cobertura);
                    }
                }
            }
            modelo.setCoberturas(coberturas);

            inicio = contenido.indexOf(ConstantsValue.RECARGO_PAGO);
            fin = contenido.indexOf("Primer Pago");
            newcontenido = new StringBuilder();
            newcontenido.append(fn.extracted(inicio, fin, contenido));
            for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
                if(newcontenido.toString().split("\n")[i].contains("Prima Según Vigencia")){
                    List<String> valores = fn.obtenerListNumeros(newcontenido.toString().split("\n")[i]);
                    if(!valores.isEmpty()){
						modelo.setPrimaneta(fn.castBigDecimal(fn.castDouble(valores.get(0))));
						modelo.setRecargo(fn.castBigDecimal(fn.castDouble(valores.get(1))));
                        modelo.setDerecho(fn.castBigDecimal(fn.castDouble(valores.get(2))));
						modelo.setIva(fn.castBigDecimal(fn.castDouble(valores.get(3))));
						modelo.setPrimaTotal(fn.castBigDecimal(fn.castDouble(valores.get(4))));
					}
                }
                
            
            }

            

            return modelo;
        } catch (Exception ex) {
            modelo.setError(AguilaDiversosModel.this.getClass().getTypeName() + " - catch:" + ex.getMessage() + " | "
                    + ex.getCause());
            return modelo;
        }

    }
}
