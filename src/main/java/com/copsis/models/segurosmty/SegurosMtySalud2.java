package com.copsis.models.segurosmty;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.copsis.constants.ConstantsValue;
import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraCoberturasModel;
import com.copsis.models.EstructuraJsonModel;

public class SegurosMtySalud2 {
    private DataToolsModel fn = new DataToolsModel();
    private EstructuraJsonModel modelo = new EstructuraJsonModel();

    StringBuilder newcontenido = new StringBuilder();

    public EstructuraJsonModel procesar(String contenido) {
        int inicio = 0;
        int fin = 0;
        contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());
        try {
            modelo.setCia(39);
            modelo.setTipo(3);

            inicio = contenido.indexOf("ACCIDENTES PERSONALES");
            fin = contenido.indexOf("INTEGRANTES");

            newcontenido.append(fn.extracted(inicio, fin, contenido));
            for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
                if (newcontenido.toString().split("\n")[i].contains("CONTRATANTE")) {
                    modelo.setCteNombre(
                            newcontenido.toString().split("\n")[i].split("CONTRATANTE")[1].replace("###", "").trim());
                }

                if (newcontenido.toString().split("\n")[i].contains("PÓLIZA ###NÚMERO")
                        && newcontenido.toString().split("\n")[i].contains("ENOMINADA ###EN")) {
                    modelo.setPoliza(
                            newcontenido.toString().split("\n")[i + 1].split("###")[1].replace("###", "").trim());
                    modelo.setMoneda(fn.buscaMonedaEnTexto(newcontenido.toString().split("\n")[i + 1]));
                }
                if (newcontenido.toString().split("\n")[i].contains("C.P.")) {
                    modelo.setCteDireccion(
                            newcontenido.toString().split("\n")[i].split("C.P.")[1].replace("###", "").trim());
                    List<String> valores = fn.obtenerListNumeros2(modelo.getCteDireccion());
                    if (!valores.isEmpty()) {
                        modelo.setCp(valores.stream()
                                .filter(numero -> String.valueOf(numero).length() >= 4)
                                .collect(Collectors.toList()).get(0));
                    }
                }
                getVigencias(i);
            }

         
            inicio = contenido.indexOf("PLAZO DE");
            fin = contenido.indexOf(ConstantsValue.SUMAS_ASEGURAS);
            newcontenido = new StringBuilder();
            newcontenido.append(fn.extracted(inicio, fin, contenido));
            for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
           
                if (newcontenido.toString().split("\n")[i].contains("DE PAGO:")){
                    modelo.setFormaPago(fn.formaPagoSring(newcontenido.toString().split("\n")[i]));
                }
            }

            inicio = contenido.indexOf(ConstantsValue.SUMAS_ASEGURAS);
            fin = contenido.indexOf("CIUDAD ###DE MÉXICO:");
            getCoberturas(contenido, inicio, fin);

            inicio = contenido.lastIndexOf("PRIMA ANUAL");
            fin = contenido.indexOf("SEGUROS ###MONTERREY");
            getPrimas(contenido, inicio, fin);

            return modelo;
        } catch (Exception ex) {
            modelo.setError(
                    SegurosMtySalud2.this.getClass().getTypeName() + " - catch:" + ex.getMessage() + " | "
                            + ex.getCause());
            return modelo;
        }
    }

    private void getPrimas(String contenido, int inicio, int fin) {
        newcontenido = new StringBuilder();
        newcontenido.append(fn.extracted(inicio, fin, contenido));
        for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {

            if (newcontenido.toString().split("\n")[i].contains("RECARGO")) {
                List<String> valores = fn.obtenerListNumeros2(newcontenido.toString().split("\n")[i].replace(",", ""));
                if (!valores.isEmpty()) {
                    modelo.setRecargo(fn.castBigDecimal(fn.castDouble(valores.get(0))));
                }
            }
            if (newcontenido.toString().split("\n")[i].contains("I.V.A")) {
                List<String> valores = fn.obtenerListNumeros2(newcontenido.toString().split("\n")[i].replace(",", ""));
                if (!valores.isEmpty()) {
                    modelo.setIva(fn.castBigDecimal(fn.castDouble(valores.get(0))));
                }
            }

            getSub(i);
        }
    }

    private void getSub(int i) {
        if (newcontenido.toString().split("\n")[i].contains("PRIMA ANUAL")) {
            List<String> valores = fn.obtenerListNumeros2(newcontenido.toString().split("\n")[i].replace(",", ""));
            if (!valores.isEmpty()) {
                modelo.setPrimaTotal(fn.castBigDecimal(fn.castDouble(valores.get(0))));
            }
        }
        if (newcontenido.toString().split("\n")[i].contains("PRIMA SEGÚN")) {
            List<String> valores = fn.obtenerListNumeros2(newcontenido.toString().split("\n")[i].replace(",", ""));
            if (!valores.isEmpty()) {
                modelo.setPrimaneta(fn.castBigDecimal(fn.castDouble(valores.get(0))));
            }
        }
        if (newcontenido.toString().split("\n")[i].contains("PRIMER ###RECIBO")) {
            List<String> valores = fn.obtenerListNumeros2(newcontenido.toString().split("\n")[i].replace(",", ""));
            if (!valores.isEmpty()) {
                modelo.setPrimerPrimatotal(fn.castBigDecimal(fn.castDouble(valores.get(0))));
            }
        }

        if (newcontenido.toString().split("\n")[i].contains("SUBSECUENTES")) {
            List<String> valores = fn.obtenerListNumeros2(newcontenido.toString().split("\n")[i].replace(",", ""));
            if (!valores.isEmpty()) {
                modelo.setSubPrimatotal(fn.castBigDecimal(fn.castDouble(valores.get(0))));
            }
        }
    }

    private void getCoberturas(String contenido, int inicio, int fin) {
        newcontenido = new StringBuilder();
        newcontenido.append(fn.extracted(inicio, fin, contenido));
        List<EstructuraCoberturasModel> coberturas = new ArrayList<>();
        for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
            EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();
            if (!newcontenido.toString().split("\n")[i].contains(ConstantsValue.SUMAS_ASEGURAS)
                    && !newcontenido.toString().split("\n")[i].contains("COBERTURA")
                    && !newcontenido.toString().split("\n")[i].contains("ASEGURADOS")
                    && !newcontenido.toString().split("\n")[i].contains("ANEXOS")
                    && !newcontenido.toString().split("\n")[i].contains("BENEFICIO")
                    && !newcontenido.toString().split("\n")[i].contains("TITULAR")
                    && !newcontenido.toString().split("\n")[i].contains("DEPENDIENTES")
                    && newcontenido.toString().split("\n")[i].length() > 10) {

                int sp = newcontenido.toString().split("\n")[i].split("###").length;
                switch (sp) {
                    case 1:
                        cobertura.setNombre(newcontenido.toString().split("\n")[i].split("###")[0].trim());

                        coberturas.add(cobertura);
                        break;
                    case 2:
                    case 4:
                    case 6:
                        cobertura.setNombre(newcontenido.toString().split("\n")[i].split("###")[0].trim());
                        cobertura.setSa(newcontenido.toString().split("\n")[i].split("###")[1].trim());
                        coberturas.add(cobertura);
                        break;
                    default:
                }
            }
        }
        modelo.setCoberturas(coberturas);
    }

    private void getVigencias(int i) {
        if (newcontenido.toString().split("\n")[i].contains("HRS. DEL DÍA")) {
            List<String> valores = fn.obtenVigePoliza(newcontenido.toString().split("\n")[i + 1]);
            if (valores.size() == 2) {
                modelo.setVigenciaDe(fn.formatDateMonthCadena(valores.get(0)));
                modelo.setVigenciaA(fn.formatDateMonthCadena(valores.get(1)));
                modelo.setFechaEmision(modelo.getVigenciaDe());
            }

        }
    }
}
