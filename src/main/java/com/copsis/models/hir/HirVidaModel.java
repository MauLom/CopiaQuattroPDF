package com.copsis.models.hir;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraCoberturasModel;
import com.copsis.models.EstructuraJsonModel;

public class HirVidaModel {
    private DataToolsModel fn = new DataToolsModel();
    private EstructuraJsonModel modelo = new EstructuraJsonModel();

    int inicio;
    int fin;
    StringBuilder newcontenido = new StringBuilder();
    StringBuilder cteDireccion = new StringBuilder();

    public EstructuraJsonModel procesar(String contenido) {
        try {
            contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());

            modelo.setTipo(5);
            modelo.setCia(19);

            inicio = contenido.indexOf("Razón Social");
            fin = contenido.lastIndexOf("Empleados Administrativos");
            getContratante(contenido);
            modelo.setCteDireccion(cteDireccion.toString());

          
            inicio = contenido.indexOf("Coberturas###Suma Asegurada");
            fin = contenido.indexOf("El###producto###ligado");
            newcontenido = new StringBuilder();
            getCoberturas(contenido);

            inicio = contenido.indexOf("Clave Agente");
            fin = contenido.lastIndexOf("Para###cualquier###aclaración");
            getAgente(contenido);

            return modelo;
        } catch (Exception ex) {
            modelo.setError(HirVidaModel.this.getClass().getTypeName() + " | " + ex.getMessage() + " | "
                    + ex.getCause());
            return modelo;
        }

    }

    private void getAgente(String contenido) {
        newcontenido = new StringBuilder();
        newcontenido.append(fn.extracted(inicio, fin, contenido));
        for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
         
            if(newcontenido.toString().split("\n")[i].contains("Clave Agente") && newcontenido.toString().split("\n")[i].contains("Nombre Agente")){
                List<String> valores = fn.obtenerListNumeros2(newcontenido.toString().split("\n")[i]);
                modelo.setCveAgente(valores.get(0));

                modelo.setAgente(newcontenido.toString().split("\n")[i].split("Nombre Agente")[1].replace("###", "") +" " + newcontenido.toString().split("\n")[i+1].replace("###", "").trim());
            }
        }
    }

    private void getCoberturas(String contenido) {
        newcontenido.append(fn.extracted(inicio, fin, contenido));
        List<EstructuraCoberturasModel> coberturas = new ArrayList<>();
        for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
            if (!newcontenido.toString().split("\n")[i].contains("Coberturas")
                    && !newcontenido.toString().split("\n")[i].contains("SECCION")) {
                EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();
                cobertura.setNombre(newcontenido.toString().split("\n")[i].split("###")[0]);
                if (newcontenido.toString().split("\n")[i].split("###").length > 1) {
                    cobertura.setSa(newcontenido.toString().split("\n")[i].split("###")[1]);

                }
                coberturas.add(cobertura);

            }
        }
        modelo.setCoberturas(coberturas);
    }

    private void getContratante(String contenido) {
        newcontenido.append(fn.extracted(inicio, fin, contenido));
        for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {

            if (newcontenido.toString().split("\n")[i].contains("Contratante")
                    && newcontenido.toString().split("\n")[i].contains("Póliza")) {
                modelo.setPoliza(newcontenido.toString().split("\n")[i].split("Póliza")[1].replace("###", "").trim());
                modelo.setCteNombre(newcontenido.toString().split("\n")[i + 1].split("###")[0]);
            }

            if (newcontenido.toString().split("\n")[i].contains("Registro Federal")
                    && newcontenido.toString().split("\n")[i].contains("Contribuyentes")) {
                modelo.setRfc(newcontenido.toString().split("\n")[i].split("Contribuyentes")[1].replace("###", ""));
            }

            if (newcontenido.toString().split("\n")[i].contains("Calle")
                    && newcontenido.toString().split("\n")[i].contains("Colonia")) {
                cteDireccion.append(newcontenido.toString().split("\n")[i].replace("Calle", " ").replace("Colonia", " ")
                        .replace("###", "").trim() + " ");
            }
            if (newcontenido.toString().split("\n")[i].contains("Alcaldía")
                    && newcontenido.toString().split("\n")[i].contains("Estado")) {
                cteDireccion
                        .append(newcontenido.toString().split("\n")[i].replace("Alcaldía", " ").replace("Estado", " ")
                                .replace("Municipio", " ")
                                .replace("Teléfono", "")
                                .replace("Código Postal", "")
                                .replace("###", " ").trim());
            }

            if (newcontenido.toString().split("\n")[i].contains("Código Postal")) {
                List<String> valores = fn.obtenerListNumeros2(newcontenido.toString().split("\n")[i]);
                if (!valores.isEmpty()) {
                    modelo.setCp(valores.stream()
                            .filter(numero -> String.valueOf(numero).length() >= 4)
                            .collect(Collectors.toList()).get(0));
                }
            }
            if (newcontenido.toString().split("\n")[i].contains("Prima Tarifa")
                    && newcontenido.toString().split("\n")[i].contains("Forma de Pago")) {
                List<String> valores = fn.obtenerListNumeros2(newcontenido.toString().split("\n")[i].replace(",", ""));
                if (!valores.isEmpty()) {
                    modelo.setPrimaneta(fn.castBigDecimal(fn.castDouble(valores.get(0))));

                }
                modelo.setFormaPago(fn.formaPagoSring(newcontenido.toString().split("\n")[i]));

            }
            if (newcontenido.toString().split("\n")[i].contains("Recargo por")
                    && newcontenido.toString().split("\n")[i].contains("Moneda")) {
                List<String> valores = fn.obtenerListNumeros2(newcontenido.toString().split("\n")[i].replace(",", ""));
                if (!valores.isEmpty()) {
                    modelo.setRecargo(fn.castBigDecimal(fn.castDouble(valores.get(0))));
                }
                modelo.setMoneda(fn.buscaMonedaEnTexto(newcontenido.toString().split("\n")[i]));

            }
            if (newcontenido.toString().split("\n")[i].contains("Gastos de Expedición#")) {
                List<String> valores = fn.obtenerListNumeros2(newcontenido.toString().split("\n")[i].replace(",", ""));
                if (!valores.isEmpty()) {
                    modelo.setDerecho(fn.castBigDecimal(fn.castDouble(valores.get(0))));
                }

            }
            if (newcontenido.toString().split("\n")[i].contains("IVA")) {
                List<String> valores = fn.obtenerListNumeros2(newcontenido.toString().split("\n")[i].replace(",", ""));
                if (!valores.isEmpty()) {
                    modelo.setIva(fn.castBigDecimal(fn.castDouble(valores.get(0))));
                }

            }
            if (newcontenido.toString().split("\n")[i].contains("Prima Total")) {
                List<String> valores = fn.obtenerListNumeros2(newcontenido.toString().split("\n")[i].replace(",", ""));
                if (!valores.isEmpty()) {
                    modelo.setPrimaTotal(fn.castBigDecimal(fn.castDouble(valores.get(0))));
                }

            }
            getVigencias(i);
        }
    }

    private void getVigencias(int i) {
        if (newcontenido.toString().split("\n")[i].contains("Giro")
                && newcontenido.toString().split("\n")[i].contains("Vigencia")) {
            List<String> valores = fn.obtenVigePoliza(newcontenido.toString().split("\n")[i]);
            modelo.setVigenciaDe(fn.formatDateMonthCadena(valores.get(0)));
            modelo.setVigenciaA(fn.formatDateMonthCadena(valores.get(1)));
            modelo.setFechaEmision(modelo.getVigenciaDe());
        }
    }
}
