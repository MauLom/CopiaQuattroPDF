package com.copsis.models.sura;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraCoberturasModel;
import com.copsis.models.EstructuraJsonModel;

public class SuraDiversos3Model {

    private DataToolsModel fn = new DataToolsModel();
    private EstructuraJsonModel modelo = new EstructuraJsonModel();

    public EstructuraJsonModel procesar(String contenido) {
        int inicio;
        int fin;
        StringBuilder newcontenido = new StringBuilder();
        StringBuilder newDireecion = new StringBuilder();
        contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());
        try {
            modelo.setTipo(7);
            modelo.setCia(88);

         

            inicio = contenido.indexOf("Póliza No.");
            fin = contenido.indexOf("Datos del asegurado");
            newcontenido.append(fn.extracted(inicio, fin, contenido));
            getContratante(newcontenido, newDireecion);

            inicio = contenido.indexOf("Datos del asegurado");
            fin = contenido.indexOf("Beneficiario preferente");
          
            getVigencia(contenido, inicio, fin);

            inicio = contenido.indexOf("Suma asegurada");
            fin = contenido.lastIndexOf("Oficina");
          
            getCoberturas(contenido, inicio, fin);

            inicio = contenido.indexOf("Prima neta");
            fin = contenido.lastIndexOf("Pag. 1 de");
         
           
            getPrimas(contenido, inicio, fin);

            inicio = contenido.indexOf("Agente:");
            fin = contenido.lastIndexOf("En cumplimiento");
          
            
            getAgente(contenido, inicio, fin);

            return modelo;
        } catch (Exception e) {
            modelo.setError(
                    SuraDiversos3Model.this.getClass().getTypeName() + " | " + e.getMessage() + " | " + e.getCause());
            return modelo;
        }
    }

    private void getAgente(String contenido, int inicio, int fin) {
        StringBuilder newcontenido = new StringBuilder();
        newcontenido.append(fn.extracted(inicio, fin, contenido));
        for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {              
           if(newcontenido.toString().split("\n")[i].contains("Agente:")){
              List<String> valores = fn.obtenerListSimple(newcontenido.toString().split("\n")[i]);
              
            if (!valores.isEmpty()) {
                modelo.setCveAgente(valores.get(0));
                modelo.setAgente(newcontenido.toString().split("\n")[i].split(modelo.getCveAgente())[1].replace("###", "").trim());
            }
           }
        }
    }

    private void getPrimas(String contenido, int inicio, int fin) {
       StringBuilder newcontenido = new StringBuilder();
         newcontenido.append(fn.extracted(inicio, fin, contenido));
     
        for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {

            if (newcontenido.toString().split("\n")[i].contains("Prima neta")) {
                               
                List<String> valores = fn.obtenerListSimple(newcontenido.toString().split("\n")[i+1]);
                if (!valores.isEmpty()) {
                    modelo.setPrimaneta(fn.castBigDecimal(fn.castDouble(valores.get(0))));
                    modelo.setDerecho(fn.castBigDecimal(fn.castDouble(valores.get(2))));
                    modelo.setRecargo(fn.castBigDecimal(fn.castDouble(valores.get(1))));
                    modelo.setIva(fn.castBigDecimal(fn.castDouble(valores.get(4))));
                    modelo.setPrimaTotal(fn.castBigDecimal(fn.castDouble(valores.get(5))));
                }

            }
        }
    }

    private void getCoberturas(String contenido, int inicio, int fin) {
        StringBuilder newcontenido = new StringBuilder();
        newcontenido.append(fn.extracted(inicio, fin, contenido));
        List<EstructuraCoberturasModel> coberturas = new ArrayList<>();
        for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
            EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();
            if (!newcontenido.toString().split("\n")[i].contains("Pag.")
                    && !newcontenido.toString().split("\n")[i].contains("Deducible")) {

                int sp = newcontenido.toString().split("\n")[i].split("###").length;
                switch (sp) {
                    case 2:
                        cobertura.setNombre(newcontenido.toString().split("\n")[i].split("###")[0]);
                        cobertura.setSa(newcontenido.toString().split("\n")[i].split("###")[1]);
                        coberturas.add(cobertura);
                        break;
                    case 3:
                        cobertura.setNombre(newcontenido.toString().split("\n")[i].split("###")[0]);
                        cobertura.setSa(newcontenido.toString().split("\n")[i].split("###")[1]);
                        cobertura.setDeducible(newcontenido.toString().split("\n")[i].split("###")[2]);
                        coberturas.add(cobertura);
                        break;

                    default:
                        break;
                }

            }
        }

        modelo.setCoberturas(coberturas);
    }

    private void getVigencia(String contenido, int inicio, int fin) {
        StringBuilder newcontenido = new StringBuilder();
        newcontenido.append(fn.extracted(inicio, fin, contenido));
        for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
            if (newcontenido.toString().split("\n")[i].contains("Hasta las")) {
                List<String> valores = fn.obtenVigePoliza(newcontenido.toString().split("\n")[i]);
                modelo.setVigenciaA(fn.formatDateMonthCadena(valores.get(0)));
            }
            if (newcontenido.toString().split("\n")[i].contains("R.F.C.")) {
                modelo.setRfc(newcontenido.toString().split("\n")[i].split("R.F.C.")[1].replace("###", "").trim());
            }
        }
    }

    private void getContratante(StringBuilder newcontenido, StringBuilder newDireecion) {
        for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {

            if (newcontenido.toString().split("\n")[i].contains("Póliza No.")) {
                modelo.setPoliza(newcontenido.toString().split("\n")[i + 1]
                        .split("###")[newcontenido.toString().split("\n")[i + 1].split("###").length - 1]
                        .replace("###", ""));
            }
            if (newcontenido.toString().split("\n")[i].contains("contratante")) {
                modelo.setCteNombre(newcontenido.toString().split("\n")[i + 1].split("###")[0].replace("###", ""));
                modelo.setMoneda(fn.buscaMonedaEnTexto(newcontenido.toString().split("\n")[i + 1]));
                newDireecion.append(newcontenido.toString().split("\n")[i + 2].split("###")[0]);
                newDireecion.append(" " + newcontenido.toString().split("\n")[i + 3].split("###")[0]);
                newDireecion.append(" " + newcontenido.toString().split("\n")[i + 4].split("###")[0]);
            }
            if (newcontenido.toString().split("\n")[i].contains("Forma de pago") &&
                    newcontenido.toString().split("\n")[i].contains("emisión")) {
                modelo.setFormaPago(fn.formaPagoSring(newcontenido.toString().split("\n")[i + 1]));
            }
            if (newcontenido.toString().split("\n")[i].contains("Vigencia desde")) {
                List<String> valores = fn.obtenVigePoliza(newcontenido.toString().split("\n")[i]);
                modelo.setVigenciaDe(fn.formatDateMonthCadena(valores.get(0)));
            }
        }
        if (!modelo.getVigenciaDe().isEmpty()) {
            modelo.setFechaEmision(modelo.getVigenciaDe());
        }
        modelo.setCteDireccion(newDireecion.toString());

        if (!modelo.getCteDireccion().isEmpty()) {
            List<String> valores = fn.obtenerListNumeros2(modelo.getCteDireccion());
            if (!valores.isEmpty()) {
                modelo.setCp(valores.stream()
                        .filter(numero -> String.valueOf(numero).length() >= 4)
                        .collect(Collectors.toList()).get(0));
            }
        }

    }
}
