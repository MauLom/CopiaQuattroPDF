package com.copsis.models.mapfre;

import java.util.ArrayList;
import java.util.List;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraAseguradosModel;
import com.copsis.models.EstructuraBeneficiariosModel;
import com.copsis.models.EstructuraCoberturasModel;
import com.copsis.models.EstructuraJsonModel;

public class MapfreSaludCModel {
    private DataToolsModel fn = new DataToolsModel();
    private EstructuraJsonModel modelo = new EstructuraJsonModel();

    public EstructuraJsonModel procesar(String contenido) {
        StringBuilder newcontenido = new StringBuilder();
        contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());
        int inicio = 0;
        int fin = 0;
        try {
            modelo.setTipo(3);
            modelo.setCia(22);

            inicio = contenido.indexOf("Documento:");
            fin = contenido.indexOf("Mapfre México, S.A. denominada");
            newcontenido.append( fn.extracted(inicio, fin, contenido));
            List<EstructuraAseguradosModel> asegurados = new ArrayList<>();
            EstructuraAseguradosModel asegurado = new EstructuraAseguradosModel();
            for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
               
                if(newcontenido.toString().split("\n")[i].contains("Póliza Número")){
                   modelo.setPoliza(newcontenido.toString().split("\n")[i].split("Número")[1].replace(":", "").trim());
                }
                if(newcontenido.toString().split("\n")[i].contains("Contratante:") && newcontenido.toString().split("\n")[i].contains("R.F.C:")){
                    modelo.setCteNombre(newcontenido.toString().split("\n")[i].split("Contratante:")[1].split("R.F.C:")[0].replace("###", ""));
                    modelo.setRfc(newcontenido.toString().split("\n")[i].split("R.F.C:")[1].replace("###", ""));
                }
                if(newcontenido.toString().split("\n")[i].contains("C.P:")){
                    modelo.setCp(newcontenido.toString().split("\n")[i].split("C.P:")[1].trim().substring(0,5));
                }
                if(newcontenido.toString().split("\n")[i].contains("Dom:") && newcontenido.toString().split("\n")[i].contains("Tel:")){
                    modelo.setCteDireccion(newcontenido.toString().split("\n")[i].split("Dom:")[1].split("Tel:")[0].replace("###", ""));
                }
                if(newcontenido.toString().split("\n")[i].contains("Asegurado:") 
                && newcontenido.toString().split("\n")[i].contains("R.F.C:")
                 &&  newcontenido.toString().split("\n")[i].contains("Fec.Nac:")){
                    asegurado.setNombre(newcontenido.toString().split("\n")[i].split("Asegurado:")[1].split("R.F.C:")[0].trim());
                    asegurado.setNacimiento(fn.formatDateMonthCadena(fn.obtenVigePoliza(newcontenido.toString().split("\n")[i] ).get(0)));
                }
                if(newcontenido.toString().split("\n")[i].contains("Dom:") && newcontenido.toString().split("\n")[i].contains("Edad")){
                    asegurado.setEdad(fn.castInteger(fn.obtenerListNumeros2(newcontenido.toString().split("\n")[i].split("Edad")[1]).get(0).toString()));
                }
                if(newcontenido.toString().split("\n")[i].contains("Vigencia") 
                && newcontenido.toString().split("\n")[i].contains("Desde") && newcontenido.toString().split("\n")[i].contains("Clave")){
                    modelo.setVigenciaDe(fn.formatDateMonthCadena(fn.obtenVigePoliza(newcontenido.toString().split("\n")[i].split("Clave")[0]).get(0)));
                    modelo.setVigenciaA(fn.formatDateMonthCadena(fn.obtenVigePoliza(newcontenido.toString().split("\n")[i+1]).get(0)));
                    modelo.setFechaEmision(modelo.getVigenciaDe());                  
                }
                if(newcontenido.toString().split("\n")[i].contains("Pago") && newcontenido.toString().split("\n")[i].contains("Moneda") ){
                    modelo.setFormaPago(fn.formaPagoSring(newcontenido.toString().split("\n")[i+1]));
                    modelo.setMoneda(fn.buscaMonedaEnTexto(newcontenido.toString().split("\n")[i+1] ));

                }

                if(newcontenido.toString().split("\n")[i].contains("Prima neta:")) {
                    List<String> valores = fn.obtenerListNumeros(newcontenido.toString().split("\n")[i+1]);
                    modelo.setPrimaneta(fn.castBigDecimal(fn.castDouble(valores.get(0))));
                    modelo.setDerecho(fn.castBigDecimal(fn.castDouble(valores.get(3))));
                    modelo.setRecargo(fn.castBigDecimal(fn.castDouble(valores.get(2))));
                    modelo.setIva(fn.castBigDecimal(fn.castDouble(valores.get(4))));                    
                    modelo.setPrimaTotal(fn.castBigDecimal(fn.castDouble(valores.get(5))));
                }
            }
            asegurados.add(asegurado);
            modelo.setAsegurados(asegurados);

            inicio = contenido.indexOf("COBERTURAS NUMERO");
            fin = contenido.indexOf("BENEFICIARIOS");
            newcontenido = new StringBuilder();
            newcontenido.append( fn.extracted(inicio, fin, contenido)
            .replace(" ", "###")
            .replace("INFARTO###AL###MIOCARDIO", "INFARTO AL MIOCARDIO")
            .replace("DIVERTICULOS###DEL###COLON", "DIVERTICULOS DEL COLON")
            .replace("LITIASIS###VESICULAR", "LITIASIS VESICULAR")
            .replace("EXTRIPACION###QUIRURGICA###DE###OVARIOS", "EXTRIPACION QUIRURGICA DE OVARIOS")
            .replace("PANCREATITIS###AGUDA", "PANCREATITIS AGUDA")
            .replace("HERNIA###ABDOMINAL###E###INGUINAL", "HERNIA ABDOMINAL E INGUINAL")
            .replace("TUMORES###BENIGNOS", "TUMORES BENIGNOS")
            .replace("HIPERPLASIA###PROSTATICA###BENIGNA", "HIPERPLASIA PROSTATICA BENIGNA")
            .replace("ULCERA###DUODENAL###Y###GASTRICA", "ULCERA DUODENAL Y GASTRICA")
    
            );
            List<EstructuraCoberturasModel> coberturas = new ArrayList<>();
            for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {    
                EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();     
              if(!newcontenido.toString().split("\n")[i].contains("COBERTURAS") 
              && !newcontenido.toString().split("\n")[i].contains("ENFERMEDAD")
              && !newcontenido.toString().split("\n")[i].contains("ASEGURADA")
              ){ 
             
                cobertura.setNombre(newcontenido.toString().split("\n")[i].split("###")[1].trim());
                cobertura.setSa(newcontenido.toString().split("\n")[i].split("###")[2].trim());  
                coberturas.add(cobertura) ;      
                if(newcontenido.toString().split("\n")[i].split("###").length > 3){
                    cobertura.setNombre(newcontenido.toString().split("\n")[i].split("###")[3].trim());
                    cobertura.setSa(newcontenido.toString().split("\n")[i].split("###")[4].trim());  
                    coberturas.add(cobertura) ; 
                }
              }
            }
            modelo.setCoberturas(coberturas);
            
            inicio = contenido.indexOf("BENEFICIARIOS");
            fin = contenido.indexOf("LA DOCUMENTACION");
            newcontenido = new StringBuilder();
            newcontenido.append( fn.extracted(inicio, fin, contenido).replace("OTROS", "###OTROS###"));
            List<EstructuraBeneficiariosModel> beneficiarios = new ArrayList<>();
            for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {      
                EstructuraBeneficiariosModel beneficiario = new EstructuraBeneficiariosModel();            
                if(!newcontenido.toString().split("\n")[i].contains("BENEFICIARIOS") && !newcontenido.toString().split("\n")[i].contains("PORCENTAJE") ){                  
                    beneficiario.setNombre(newcontenido.toString().split("\n")[i].split("###")[0].trim());
                    beneficiario.setParentesco(fn.buscaParentesco(newcontenido.toString().split("\n")[i].split("###")[1].trim()));
                    beneficiario.setPorcentaje(fn.castInteger(fn.obtenerListNumeros2(newcontenido.toString().split("\n")[i] ).get(0)));
                    beneficiarios.add(beneficiario);
                }    
            }
            modelo.setBeneficiarios(beneficiarios);

            return modelo;
        } catch (Exception ex) {
            ex.printStackTrace();
            modelo.setError(MapfreSaludCModel.this.getClass().getTypeName() + " - catch:" + ex.getMessage() + " | "
                    + ex.getCause());
            return modelo;
        }
    }

}
