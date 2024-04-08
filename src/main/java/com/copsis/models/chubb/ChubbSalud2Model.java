package com.copsis.models.chubb;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.copsis.constants.ConstantsValue;
import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraAseguradosModel;
import com.copsis.models.EstructuraCoberturasModel;
import com.copsis.models.EstructuraJsonModel;

public class ChubbSalud2Model {
    DataToolsModel fn = new DataToolsModel();
    EstructuraJsonModel modelo = new EstructuraJsonModel();

    public EstructuraJsonModel procesar(String contenido) {
        int inicio = 0;
        int fin = 0;

        contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());
        try {

            modelo.setTipo(3);
            modelo.setCia(1);
            inicio = contenido.indexOf("PÓLIZA DE SEGURO");
            fin = contenido.indexOf("Datos del Asegurado");
            String[] newcontenido = fn.extracted(inicio, fin, contenido).split("\n");

            for (String line : newcontenido) {
                getPoliza(line);              
                getMonedaPago(line);               
                getDireccion(line);                               
            }
            
            inicio = contenido.indexOf("Datos del Asegurado");
            fin = contenido.indexOf("Características del Riesg");
            newcontenido = fn.extracted(inicio, fin, contenido).split("\n");
            List<EstructuraAseguradosModel> asegurados = new ArrayList<>();
             for (String line : newcontenido) {
               
                EstructuraAseguradosModel  asegurado= new EstructuraAseguradosModel();
              if(line.split("-").length > 2){
                 asegurado.setNombre(line.split("###")[0]);
                  List<String> valores = fn.obtenVigePoliza(line);
                  asegurado.setNacimiento(fn.formatDateMonthCadena(valores.get(0)));
                 asegurados.add(asegurado);
                }                
             }
             modelo.setAsegurados(asegurados);
            inicio = contenido.indexOf("Prima Neta:");
            fin = contenido.indexOf("Beneficiarios designados");
            newcontenido = fn.extracted(inicio, fin, contenido).split("\n");
            
            for (String line : newcontenido) {                 
              
                if(line.contains("Prima Neta:")){
                 List<String> valores = fn.obtenerListNumeros(line);
                 modelo.setPrimaneta(fn.castBigDecimal(fn.castDouble(valores.get(0))));
                }
                 if(line.contains("Prima Total:") && line.contains("MXN")){
                List<String> valores = fn.obtenerListNumeros(line);
                 modelo.setPrimaTotal(fn.castBigDecimal(fn.castDouble(valores.get(0))));
                }
                
                if(line.contains("I.V.A:") && line.contains("MXN")){
                    List<String> valores = fn.obtenerListNumeros(line);
                     modelo.setIva(fn.castBigDecimal(fn.castDouble(valores.get(0))));
                    }
            }


            inicio = contenido.indexOf("Secciones###Suma asegurada");
            fin = contenido.indexOf("Servicios de Asistencia");
            String contenidomo=  fn.extracted(inicio, fin, contenido)
            .replace("Sección I. Muerte Accidental###10,000###N-A###N-A", "Muerte Accidental###10,000###N-A###N-A")
            .replace("Sección XVII. Traslados Médicos de Emergencia y Traslados Sanitarios ###7,500###N-A###N-A ", "Traslados Médicos de Emergencia y Traslados Sanitarios ###7,500###N-A###N-A ")           
            .replace("Sección III. Invalidez Total y", "Invalidez Total y Permanente por Accidente###20,000###N-A###N-A")
            .replace("Sección VII. Gastos Médicos por", "Gastos Médicos por Accidente y-o Enfermedad###7,500###N-A###N-A")
            .replace("Sección XIV. Gastos por", "Gastos por Emergencia Odontológica###1,000###N-A###N-A ")
            .replace("Sección XVII. Traslados Médicos###7,500###N-A###N-A", "Traslados Médicos de Emergencia y Traslados Sanitarios ###7,500###N-A###N-A ")
            .replace("Sección XVIII. Gastos Funerarios", "Gastos Funerarios o Traslado de restos mortales###7,500###N-A###N-A")
            .replace("Sección XIX. Traslado de un", "Traslado de un Familiar Directo en caso de Accidente o Emergencia Médica del Asegurado###300###N-A###N-A")
            .replace("Sección XXV. Demora de Viaje###500###N-A###N-A", "Demora de Viaje###500###N-A###N-A")
            .replace("Sección XXX. Pérdida de", "Pérdida de Equipaje###500###N-A###N-A")
            .replace("Sección XXXI. Daño de Equipaje###500###N-A###N-A", "Daño de Equipaje###500###N-A###N-A")
            .replace("Sección XXXIII. Responsabilidad", "Responsabilidad Civil###15,000###N-A###N-A");
          
            newcontenido =contenidomo.split("\n");
            getCoberturas(newcontenido);

            return modelo;
        } catch (Exception ex) {
            modelo.setError(
                    ChubbSalud2Model.this.getClass().getTypeName() + " | " + ex.getMessage() + " | " + ex.getCause());
            return modelo;
        }
    }

    private void getCoberturas(String[] newcontenido) {
        List<EstructuraCoberturasModel> coberturas = new ArrayList<>();
        for(int i=0;i< newcontenido.length;i++){
            EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();     
           if(!newcontenido[i].contains("Secciones")
           && !newcontenido[i].contains("horas cubiertas")
           && !newcontenido[i].contains("Muerte e Ivalidez")        
           && !newcontenido[i].contains("Inconvenientes de Viaje")
           && !newcontenido[i].contains("Otros")
           && !newcontenido[i].contains("Propiedades Personales")
           && !newcontenido[i].contains("PÓLIZA DE SEGURO")
           && !newcontenido[i].contains("CARÁTULA")
           && (newcontenido[i].split("###").length > 3)){                 
                cobertura.setNombre(newcontenido[i].split("###")[0].trim());
        		cobertura.setSa(newcontenido[i].split("###")[1].trim());                       
        		coberturas.add(cobertura);
                             
           }
        }
        modelo.setCoberturas(coberturas);
    }

    private void getPoliza(String line) {
        if (line.contains(ConstantsValue.POLIZA_ACENT) && line.contains("Vigencia:")) {
           modelo.setPoliza(line.split(ConstantsValue.POLIZA_ACENT)[1].split("Vigencia:")[0].replace("###", "").trim());
            List<String> valores = fn.obtenVigePoliza(line);
            modelo.setVigenciaDe(fn.formatDateMonthCadena(valores.get(0)));
            modelo.setVigenciaA(fn.formatDateMonthCadena(valores.get(1)));
        }
         if (line.contains("emisión:") && line.contains("Renovación:")) {
                    List<String> valores = fn.obtenVigePoliza(line);
                    modelo.setFechaEmision(fn.formatDateMonthCadena(valores.get(0)));
         }
         if (line.contains("Contratante:")) {
                    modelo.setCteNombre(line.split("Contratante:")[1].replace("###", "").trim());
         }
    }

    private void getDireccion(String line) {
        if (line.contains("Domicilio:")) {
            modelo.setCteDireccion(line.split("Domicilio:")[1].replace("###", "").trim());
        }
        if (line.contains("C.P:")) {
            getCP(line);
        }
    }

    private void getMonedaPago(String line) {
        if (line.contains("Moneda") && line.contains("Plan contratado:")) {
            modelo.setMoneda(fn.buscaMonedaEnTexto(line));
            modelo.setPlan(line.split("contratado:")[1].replace("###", "").trim());
        }
        if (line.contains("Forma de pago:")) {
            modelo.setFormaPago(fn.formaPagoSring(line));
        }
    }

   
    private void getCP(String line) {
        List<String> valores = fn.obtenerListNumeros2(line);
        if (!valores.isEmpty()) {
            modelo.setCp(valores.stream()
                    .filter(numero -> String.valueOf(numero).length() >= 4)
                    .collect(Collectors.toList()).get(0));
        }
    }
}
