package com.copsis.models.atlas;

import java.util.ArrayList;
import java.util.List;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraAseguradosModel;
import com.copsis.models.EstructuraCoberturasModel;
import com.copsis.models.EstructuraJsonModel;

public class AtlasSaludBModel {
    private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();

    public EstructuraJsonModel procesar(String contenido) {
        int inicio = 0;
		int fin = 0;
        StringBuilder newcontenido = new StringBuilder();
		contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());
        try {
            modelo.setTipo(3);
			modelo.setCia(33);
          
            inicio = contenido.indexOf("Gastos###Médicos###Mayores");
            fin = contenido.lastIndexOf("Límites###de###Cobertura");
            newcontenido.append(fn.extracted(inicio, fin, contenido));
            getContratante(newcontenido);


            inicio = contenido.indexOf("Límites###de###Cobertura");
            fin = contenido.lastIndexOf("Lista###de###Asegurados");
            newcontenido = new StringBuilder();
            newcontenido.append(fn.extracted(inicio, fin, contenido));
            getCoberturas(newcontenido);

           
            inicio = contenido.indexOf("Lista###de###Asegurados");
            fin = contenido.lastIndexOf("Limitaciones###y###Exclusiones");
            newcontenido = new StringBuilder();
            newcontenido.append(fn.extracted(inicio, fin, contenido));
            getAsegurados(newcontenido);

            return modelo;
        } catch (Exception ex) {
            modelo.setError(AtlasSaludBModel.this.getClass().getTypeName() + " - catch:" + ex.getMessage() + " | "
            + ex.getCause());
    return modelo;
        }
        
    }

    private void getAsegurados(StringBuilder newcontenido) {
        List<EstructuraAseguradosModel> asegurados = new ArrayList<>();				
        for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
          
            EstructuraAseguradosModel asegurado = new EstructuraAseguradosModel();

            
            if(newcontenido.toString().split("\n")[i].contains("Titular")){
           
                List<String> valores = fn.obtenVigePoliza(newcontenido.toString().split("\n")[i]);   
                 asegurado.setNombre(newcontenido.toString().split("\n")[i-2].replace("###", " ")+" "+ newcontenido.toString().split("\n")[i-1].replace("###", " "));
                 if(!valores.isEmpty()){
                     asegurado.setParentesco(3);
                     asegurado.setNacimiento(valores.get(0));
                     asegurado.setAntiguedad(valores.get(1));
                     asegurado.setFechaAlta(valores.get(2));
                  }
                  asegurado.setSexo(fn.sexo(newcontenido.toString().split("\n")[i].split("###")[2]) ? 1:0);
                 asegurados.add(asegurado);
             }
            if(newcontenido.toString().split("\n")[i].contains("Dependiente")){
           
            List<String> valores = fn.obtenVigePoliza(newcontenido.toString().split("\n")[i]);   
             asegurado.setNombre(newcontenido.toString().split("\n")[i-2].replace("###", " ")+" "+ newcontenido.toString().split("\n")[i-1].replace("###", " "));
             if(!valores.isEmpty()){
                 asegurado.setParentesco(3);
                 asegurado.setNacimiento(valores.get(0));
                 asegurado.setAntiguedad(valores.get(1));
                 asegurado.setFechaAlta(valores.get(2));
              }
              asegurado.setSexo(fn.sexo(newcontenido.toString().split("\n")[i].split("###")[2]) ? 1:0);
             asegurados.add(asegurado);
          }
        }
        modelo.setAsegurados(asegurados);

    }


    private void getCoberturas(StringBuilder newcontenido) {
        List<EstructuraCoberturasModel> coberturas = new ArrayList<>();
        EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();
        for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
            
            
             if(newcontenido.toString().split("\n")[i].contains("Suma###Asegurada:")){
                cobertura.setSa(newcontenido.toString().split("\n")[i].split("Suma###Asegurada:")[1].replace("###", "").trim());
             }
             if(newcontenido.toString().split("\n")[i].contains("Deducible")){
               cobertura.setDeducible(newcontenido.toString().split("\n")[i].split("EE.UU:")[1].replace("###", "").trim());
             }
        }
        coberturas.add(cobertura);
        modelo.setCoberturas(coberturas);
    }

    private void getContratante(StringBuilder newcontenido) {
        for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
           
            if(newcontenido.toString().split("\n")[i].contains("Póliza:") &&
              newcontenido.toString().split("\n")[i].contains("Fecha")){
                 modelo.setPoliza( newcontenido.toString().split("\n")[i].split("Póliza:")[1].split("Fecha")[0].replace("###", "").trim());
              }

              if(newcontenido.toString().split("\n")[i].contains("Vigencia") &&
              newcontenido.toString().split("\n")[i].contains("Hasta:")){
               
                List<String> valores = fn.obtenVigePoliza(newcontenido.toString().split("\n")[i]);
                modelo.setVigenciaDe(fn.formatDateMonthCadena(valores.get(0)));
                modelo.setFechaEmision(fn.formatDateMonthCadena(valores.get(0)));
                modelo.setVigenciaA(fn.formatDateMonthCadena(valores.get(1)));
              
              }
              if(newcontenido.toString().split("\n")[i].contains("Contratante") &&
              newcontenido.toString().split("\n")[i].contains("RFC:")){
               
                modelo.setCteNombre(newcontenido.toString().split("\n")[i+1].replace("###", " ").trim());
                modelo.setRfc(newcontenido.toString().split("\n")[i].split("RFC:")[1].replace("###", "").trim());
                
              }
              if(newcontenido.toString().split("\n")[i].contains("Moneda") &&
              newcontenido.toString().split("\n")[i].contains("Prima###Neta:")){
              
                modelo.setMoneda(fn.buscaMonedaEnTexto(newcontenido.toString().split("\n")[i]));
                List<String> valores = fn.obtenerListNumeros2(newcontenido.toString().split("\n")[i].replace(",", ""));
                if(!valores.isEmpty()){
                modelo.setPrimaneta( fn.castBigDecimal(fn.castDouble(valores.get(0))));
                }
              }

              if(newcontenido.toString().split("\n")[i].contains("Forma###de###Pago") &&
              newcontenido.toString().split("\n")[i].contains("Recargo")){
                modelo.setFormaPago(fn.formaPagoSring(newcontenido.toString().split("\n")[i]));
                List<String> valores = fn.obtenerListNumeros2(newcontenido.toString().split("\n")[i].replace(",", ""));
                if(!valores.isEmpty()){
                modelo.setRecargo( fn.castBigDecimal(fn.castDouble(valores.get(0))));
                }
              
              }
              if(newcontenido.toString().split("\n")[i].contains("1er.###Recibo:") &&
              newcontenido.toString().split("\n")[i].contains("Expedición:")){
                List<String> valores = fn.obtenerListNumeros2(newcontenido.toString().split("\n")[i].replace(",", ""));              
                if(!valores.isEmpty()){
                    modelo.setPrimerPrimatotal(fn.castBigDecimal(fn.castDouble(valores.get(1))));    
                modelo.setDerecho( fn.castBigDecimal(fn.castDouble(valores.get(2))));
                }
                
              }
              if(newcontenido.toString().split("\n")[i].contains("Subsecuente:") &&
              newcontenido.toString().split("\n")[i].contains("IVA:")){
                List<String> valores = fn.obtenerListNumeros2(newcontenido.toString().split("\n")[i].replace(",", ""));
                if(!valores.isEmpty()){
                    modelo.setSubPrimatotal(fn.castBigDecimal(fn.castDouble(valores.get(0))));    
                    modelo.setIva( fn.castBigDecimal(fn.castDouble(valores.get(1))));
                }
                
              }
              if(newcontenido.toString().split("\n")[i].contains("PLAN:")){
                modelo.setPlan(newcontenido.toString().split("\n")[i].split("PLAN:")[1].replace("###", " ").trim());
              }
        }
    }
}
