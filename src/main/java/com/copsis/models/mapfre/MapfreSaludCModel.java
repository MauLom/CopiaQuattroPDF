package com.copsis.models.mapfre;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.copsis.constants.ConstantsValue;
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
        contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales())
        .replace("Prima Neta:", "Prima neta:")
        .replace("Póliza Número :", ConstantsValue.POLIZA_NUMERO);
        int inicio = 0;
        int fin = 0;
        try {
            modelo.setTipo(3);
            modelo.setCia(22);

            inicio = contenido.indexOf("Documento:");
            fin = contenido.indexOf("Mapfre México, S.A. denominada");
            fin = fin == -1 ?  contenido.indexOf("MAPFRE México, S.A."):fin;
          
            newcontenido.append( fn.extracted(inicio, fin, contenido));
            List<EstructuraAseguradosModel> asegurados = new ArrayList<>();
            EstructuraAseguradosModel asegurado = new EstructuraAseguradosModel();
            for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
             
                if(newcontenido.toString().split("\n")[i].contains(ConstantsValue.POLIZA_NUMERO)){
                   modelo.setPoliza(newcontenido.toString().split("\n")[i].split(ConstantsValue.POLIZA_NUMERO)[1].replace(":", "").replace("###", "").trim());
                }
                if(newcontenido.toString().split("\n")[i].contains(ConstantsValue.CONTRATANTE) && newcontenido.toString().split("\n")[i].contains(ConstantsValue.RFC)){
                    modelo.setCteNombre(newcontenido.toString().split("\n")[i].split(ConstantsValue.CONTRATANTE)[1].split(ConstantsValue.RFC)[0].replace("###", "").trim());
                    modelo.setRfc(newcontenido.toString().split("\n")[i].split(ConstantsValue.RFC)[1].replace("###", "").trim());
                }
                if(newcontenido.toString().split("\n")[i].contains("C.P:")){
                     List<String> valores = fn.obtenerListNumeros2(newcontenido.toString().split("\n")[i]);
                if(!valores.isEmpty()){
                     modelo.setCp(valores.stream()
                        .filter(numero -> String.valueOf(numero).length() >= 4)
                        .collect(Collectors.toList()).get(0));
                }
                  
                }
              
                if(newcontenido.toString().split("\n")[i].contains("Dom:") && newcontenido.toString().split("\n")[i].contains("Tel:")){
                    modelo.setCteDireccion(newcontenido.toString().split("\n")[i].split("Dom:")[1].split("Tel:")[0].replace("###", ""));
                }
                if(newcontenido.toString().split("\n")[i].contains("Domicilio:")){
                    modelo.setCteDireccion(newcontenido.toString().split("\n")[i].split("Domicilio:")[1].split("Tel:")[0].replace("###", "").trim());
                }
                if(newcontenido.toString().split("\n")[i].contains("Asegurado:") 
                && newcontenido.toString().split("\n")[i].contains(ConstantsValue.RFC)
                 &&  newcontenido.toString().split("\n")[i].contains("Fec.Nac:")){
                    asegurado.setNombre(newcontenido.toString().split("\n")[i].split("Asegurado:")[1].split(ConstantsValue.RFC)[0].trim());
                    asegurado.setNacimiento(fn.formatDateMonthCadena(fn.obtenVigePoliza(newcontenido.toString().split("\n")[i] ).get(0)));
                }
                if(newcontenido.toString().split("\n")[i].contains("Dom:") && newcontenido.toString().split("\n")[i].contains("Edad")){
                    asegurado.setEdad(fn.castInteger(fn.obtenerListNumeros2(newcontenido.toString().split("\n")[i].split("Edad")[1]).get(0)));
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
                
                    if(valores.size() > 5){
                        modelo.setPrimaneta(fn.castBigDecimal(fn.castDouble(valores.get(0))));
                        modelo.setDerecho(fn.castBigDecimal(fn.castDouble(valores.get(3))));
                        modelo.setRecargo(fn.castBigDecimal(fn.castDouble(valores.get(2))));
                        modelo.setIva(fn.castBigDecimal(fn.castDouble(valores.get(4))));                    
                        modelo.setPrimaTotal(fn.castBigDecimal(fn.castDouble(valores.get(5))));
                    }else{
                        modelo.setPrimaneta(fn.castBigDecimal(fn.castDouble(valores.get(0))));
                        modelo.setRecargo(fn.castBigDecimal(fn.castDouble(valores.get(1))));
                        modelo.setDerecho(fn.castBigDecimal(fn.castDouble(valores.get(2))));        
                        modelo.setIva(fn.castBigDecimal(fn.castDouble(valores.get(3))));                    
                        modelo.setPrimaTotal(fn.castBigDecimal(fn.castDouble(valores.get(4))));
                    }
                   
                    
                }
            }
           
            asegurados.add(asegurado);
            if(!asegurado.getNombre().isEmpty()){
                modelo.setAsegurados(asegurados);
            }
            

          
            inicio = contenido.indexOf("COBERTURAS NUMERO"); 
            fin = contenido.indexOf(ConstantsValue.BENEFICIARIOS);        
    

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
            .replace("###PAGO###DIRECTO###", "PAGO DIRECTO###")
            .replace("###REEMBOLSO###DE###GASTOS###MEDICOS######50,000.00###P-P###0.0###", "REEMBOLSO DE GASTOS MEDICOS######50,000.00###P-P###0.0")
            .replace("###CONTACT###CENTER###AMPARADA###", "CONTACT CENTER###AMPARADA")
            .replace("###ASISTENCIA###VISIÓN###AMPARADA###", "ASISTENCIA VISIÓN###AMPARAD")
            .replace("###ASISTENCIA###FUNERARIA###AMPARADA###", "ASISTENCIA FUNERARIA###AMPARADA")
    
            );
        
            List<EstructuraCoberturasModel> coberturas = new ArrayList<>();
            if(!newcontenido.isEmpty()){                     
                getCoberturas(newcontenido, coberturas);
                modelo.setCoberturas(coberturas);
             }

         if(newcontenido.isEmpty()){
          inicio = contenido.indexOf("COBERTURAS###SUMA ASEGURADA"); 
            fin = contenido.indexOf("ADMINISTRACIÓN:"); 

             newcontenido = new StringBuilder();
             newcontenido.append( fn.extracted(inicio, fin, contenido)
             .replace("###PAGO###DIRECTO###", "PAGO DIRECTO###")
             .replace("###REEMBOLSO###DE###GASTOS###MEDICOS######50,000.00###P-P###0.0###", "REEMBOLSO DE GASTOS MEDICOS######50,000.00###P-P###0.0")
             .replace("###CONTACT###CENTER###AMPARADA###", "CONTACT CENTER###AMPARADA")
             .replace("###ASISTENCIA###VISIÓN###AMPARADA###", "ASISTENCIA VISIÓN###AMPARAD")
             .replace("###ASISTENCIA###FUNERARIA###AMPARADA###", "ASISTENCIA FUNERARIA###AMPARADA"));

            getCoberturas2(newcontenido, coberturas);
            modelo.setCoberturas(coberturas);
        }
            
            inicio = contenido.indexOf(ConstantsValue.BENEFICIARIOS);
            fin = contenido.indexOf("LA DOCUMENTACION");
            getBeneficiarios(contenido, inicio, fin);

            return modelo;
        } catch (Exception ex) {
           
            modelo.setError(MapfreSaludCModel.this.getClass().getTypeName() + " - catch:" + ex.getMessage() + " | "
                    + ex.getCause());
            return modelo;
        }
    }

    private void getBeneficiarios(String contenido, int inicio, int fin) {
        StringBuilder newcontenido;
        newcontenido = new StringBuilder();
        newcontenido.append( fn.extracted(inicio, fin, contenido).replace("OTROS", "###OTROS###"));
        if(!newcontenido.isEmpty()){
        List<EstructuraBeneficiariosModel> beneficiarios = new ArrayList<>();
        for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {      
            EstructuraBeneficiariosModel beneficiario = new EstructuraBeneficiariosModel();            
            if(!newcontenido.toString().split("\n")[i].contains(ConstantsValue.BENEFICIARIOS) && !newcontenido.toString().split("\n")[i].contains("PORCENTAJE") ){                  
                beneficiario.setNombre(newcontenido.toString().split("\n")[i].split("###")[0].trim());
                beneficiario.setParentesco(fn.buscaParentesco(newcontenido.toString().split("\n")[i].split("###")[1].trim()));
                beneficiario.setPorcentaje(fn.castInteger(fn.obtenerListNumeros2(newcontenido.toString().split("\n")[i] ).get(0)));
                beneficiarios.add(beneficiario);
            }    
        }
        modelo.setBeneficiarios(beneficiarios);
         }
    }

    private void getCoberturas(StringBuilder newcontenido, List<EstructuraCoberturasModel> coberturas) {
        for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {    
            EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();     
          if(!newcontenido.toString().split("\n")[i].contains("COBERTURAS") 
          && !newcontenido.toString().split("\n")[i].contains("ENFERMEDAD")
          && !newcontenido.toString().split("\n")[i].contains("ASEGURADA")
         && !newcontenido.toString().split("\n")[i].contains("###TIPO###DE")
          && !newcontenido.toString().split("\n")[i].contains("SERVICIOS")
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
    }

    private void getCoberturas2(StringBuilder newcontenido, List<EstructuraCoberturasModel> coberturas) {
        for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
            EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();
            if (!newcontenido.toString().split("\n")[i].contains("COBERTURAS") &&
                !newcontenido.toString().split("\n")[i].contains("###TIPO###DE")
                    && !newcontenido.toString().split("\n")[i].contains("SERVICIOS")) {
                switch (newcontenido.toString().split("\n")[i].split("###").length) {
                    case 2:case 5:
                    cobertura.setNombre(newcontenido.toString().split("\n")[i].split("###")[0].trim());
                    cobertura.setSa(newcontenido.toString().split("\n")[i].split("###")[1].trim()); 
                    coberturas.add(cobertura) ;  
                        break;
               

                    default:
                        break;
                }

            }
        }
    }

}
