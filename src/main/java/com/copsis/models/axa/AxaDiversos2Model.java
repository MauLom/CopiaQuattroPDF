package com.copsis.models.axa;

import java.util.ArrayList;
import java.util.List;

import com.copsis.constants.ConstantsValue;
import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraCoberturasModel;
import com.copsis.models.EstructuraJsonModel;
import com.copsis.models.EstructuraUbicacionesModel;

public class AxaDiversos2Model {
    // Clases

    private DataToolsModel fn = new DataToolsModel();
    private EstructuraJsonModel modelo = new EstructuraJsonModel();
    private String contenido = "";
    private static final String UBICACION = "Ubicación###Contratante";

    public AxaDiversos2Model(String contenidox) {
        this.contenido = fn.remplazarMultiple(contenidox, fn.remplazosGenerales());
        contenido = contenido.replace("C o b e r t u r a s", "Coberturas")
                .replace(" C O B E R T U R A S SUMA ASEGURADA", " COBERTURAS SUMA###ASEGURADA")
                .replace("D ###omicilio:", "Domicilio:");
    }

    public EstructuraJsonModel procesar() {
        String newcontenido = "";
        StringBuilder newcon = new StringBuilder();
        int inicio = 0;
        int fin = 0;
        //Responsabilidad Civil, Comercio

        try {
            modelo.setTipo(7);
            modelo.setCia(20);


            inicio = contenido.indexOf("CARÁTULA DE PÓLIZA");
            fin = contenido.indexOf("Datos del Contratante");
     
            newcon.append(fn.extracted(inicio, fin, contenido));
            
            for (int i = 0; i < newcon.toString().split("\n").length; i++) {
                if(newcon.toString().split("\n")[i].contains("CARÁTULA DE PÓLIZA")){
                    modelo.setPlan(newcon.toString().split("\n")[i+1].split("###")[1]);

                }
               
            }

            if (contenido.contains("Anterior:")) {
                newcontenido = contenido.substring(contenido.indexOf("Anterior:") + 9, contenido.indexOf("Anterior:") + 100).trim();
                if (newcontenido.contains("Desde")) {
                    modelo.setRenovacion(newcontenido.split("Desde")[0].replace("###", "").trim().replaceAll("^\\s*", ""));
                }
            }

            inicio = contenido.indexOf("Datos de la Póliza");
            fin = contenido.indexOf("Datos Adicionales");
            if (inicio == -1) {
                inicio = contenido.indexOf("Datos del Contratante");
                fin = contenido.indexOf("AXA Seguros, S.A. de C.V.");
            }

            if (inicio > -1 && fin > -1 && inicio < fin) {
                newcontenido = contenido.substring(inicio, fin).replace("@@@", "").replace("\r", "");
         
                for (int i = 0; i < newcontenido.split("\n").length; i++) {
                  
                    if (newcontenido.split("\n")[i].contains("PÓLIZA") && newcontenido.split("\n")[i+1].contains("Nombre")) {
                        modelo.setPoliza(newcontenido.split("\n")[i+1].split("###")[newcontenido.split("\n")[i+1].split("###").length -1]);

                    }
                    
                    if (newcontenido.split("\n")[i].contains("Póliza No.") && newcontenido.split("\n")[i].contains("Ramo")) {
                        modelo.setPoliza(newcontenido.split("\n")[i].split("Póliza No.")[1].split("Ramo")[0].replace("###", "").trim());

                    }
                    if (newcontenido.split("\n")[i].contains("Fecha de Emisión") && newcontenido.split("\n")[i].contains("Expediente")) {
                        modelo.setFechaEmision(fn.formatDateMonthCadena(newcontenido.split("\n")[i].split("Emisión")[1].split("Expediente")[0].replace("###", "").trim()));
                    }
              
                    if (newcontenido.split("\n")[i].contains("Vigencia")) {
                        String x = newcontenido.split("\n")[i + 1].replace("a las 12 Hrs.", "");//			
                        if (x.split("###")[1].trim().contains("-") && x.split("###")[3].trim().contains("-")) {
                            modelo.setVigenciaDe(fn.formatDateMonthCadena(x.split("###")[1].trim()));
                            modelo.setVigenciaA(fn.formatDateMonthCadena(x.split("###")[3].trim()));
                        }
                        if (x.split("###").length == 5 && x.split("###")[4].trim().contains("-")) {
                            modelo.setVigenciaDe(fn.formatDateMonthCadena(x.split("###")[4].trim()));
                        }

                    }

                    if (modelo.getVigenciaDe().length() ==0 && newcontenido.split("\n")[i].contains("Desde")) {                      
                        modelo.setVigenciaDe(fn.formatDateMonthCadena(newcontenido.split("\n")[i].split("Desde")[1].replace("###", "").trim()));
                       
                    }

                    if (newcontenido.split("\n")[i].contains("Hasta") && newcontenido.split("\n")[i].split("Hast")[1].length()> 8) {                                          
                        modelo.setVigenciaA(fn.formatDateMonthCadena(newcontenido.split("\n")[i].split("Hasta")[1].replace("###", "").trim()));
                        modelo.setFechaEmision(modelo.getVigenciaDe());
                    }

                    if (newcontenido.split("\n")[i].contains(ConstantsValue.DATOS_GENERALES_ASEGURADO) && newcontenido.split("\n")[i + 1].contains("RFC:")) {
                        modelo.setCteNombre(newcontenido.split("\n")[i + 1].split("Nombre")[1].split("RFC:")[0].replace(":", "").replace("###", "").trim());
                        modelo.setRfc(newcontenido.split("\n")[i + 1].split("RFC:")[1].trim());
                    }
                  
                    if (newcontenido.split("\n")[i].contains(ConstantsValue.DOMICILIO2)) {
                        modelo.setCteDireccion(newcontenido.split("\n")[i].split(ConstantsValue.DOMICILIO2)[1].replace("###", "").replace(":", "").trim());
                        StringBuilder direccion = new StringBuilder();
                        direccion.append(newcontenido.split("\n")[i].split(ConstantsValue.DOMICILIO)[1].replace("###", "").trim());

                        if (newcontenido.split("\n")[i + 1].contains("C.P:")) {
                            direccion.append(" ").append(newcontenido.split("\n")[i + 1].split("C.P:")[0].replace("###", "").trim());
                        }
                        modelo.setCteDireccion(direccion.toString().replace("Vigencia a las 12 hrs.", ""));
                        modelo.setMoneda(1);
                    }
                    if (newcontenido.split("\n")[i].contains("C.P:") && newcontenido.split("\n")[i].contains("Tel:")) {
                        modelo.setCp(newcontenido.split("\n")[i].split("C.P:")[1].split("Tel:")[0].trim());
                    }
                    
                    if (newcontenido.split("\n")[i].contains("C.P:") && newcontenido.split("\n")[i].contains("Desde")) {
                        modelo.setCp(newcontenido.split("\n")[i].split("C.P:")[1].split("Desde")[0].replace("###", "").trim());
                    }

                    if(modelo.getCteNombre().length() == 0 && newcontenido.split("\n")[i].contains("Datos del Contratante")) {
                     modelo.setCteNombre(newcontenido.split("\n")[i+1].split("Nombre")[1].split("###")[1].trim());
                    }
                    if (newcontenido.split("\n")[i].contains("Moneda")){
                        modelo.setMoneda(fn.buscaMonedaEnTexto(newcontenido.split("\n")[i]));
                    }


                }
            }

            if (contenido.contains("C.P:") && modelo.getCp().isBlank()) {
                if (contenido.split("C.P:")[1].split("\n")[0].split("Tel:")[0].replace("###", "").trim().matches("\\d{5}")) {
                    modelo.setCp(contenido.split("C.P:")[1].split("\n")[0].split("Tel:")[0].replace("###", "").trim());
                }

            }
          

            inicio = contenido.indexOf("Datos Adicionales");
            fin = contenido.indexOf("Suma Asegurada Prima Neta");
            if (fin == -1) {
                fin = contenido.indexOf("AXA ###Seguros");
            }
            if (fin == -1) {
                fin = contenido.indexOf("3### - 6");
            }
            if (fin == -1) {
                fin = contenido.indexOf("3### - 8");
            }
            if (fin == -1) {
                inicio = contenido.indexOf("Datos Adicionales");
                fin = contenido.indexOf("Prima Total:")+100;
            }
         
        
         

            if (inicio > -1 && fin > -1 && inicio < fin) {

                newcontenido = contenido.substring(inicio, fin).replace("@@@", "").replace("\r", "").replace("\u00a0", "").replace("I.V.A:", "I.V.A.")
                       .replace("Prima Total:", "Prima Total").replace("Gastos de Expedición:", "Gastos por Expedición") ;
      
                for (int i = 0; i < newcontenido.split("\n").length; i++) {
          

                    if (newcontenido.split("\n")[i].contains("Moneda") && newcontenido.split("\n")[i].contains("Suma Asegurada")) {
                        modelo.setMoneda(fn.moneda(newcontenido.split("\n")[i].split("Moneda")[1].split("Suma Asegurada")[0].replace("###", "").trim()));
                    }
                    if (newcontenido.split("\n")[i].contains("Forma de Pago") && newcontenido.split("\n")[i].contains(ConstantsValue.PRIMA_NETA)) {
                        modelo.setFormaPago(fn.formaPago(newcontenido.split("\n")[i].split("Forma de Pago")[1].split(ConstantsValue.PRIMA_NETA)[0].replace("###", "").trim()));

                    }
                    if (newcontenido.split("\n")[i].contains("Forma de Pago") && newcontenido.split("\n")[i].contains("Subramo")) {                  
                        modelo.setFormaPago(fn.formaPagoSring(newcontenido.split("\n")[i+1].trim()));

                    }
                    if (newcontenido.split("\n")[i].contains("Nombre del Agente")) {
                     
                        modelo.setAgente(newcontenido.split("\n")[i].split("Nombre del Agente")[1].split("###")[0]
                        .replace("###", "").replace(":", "").trim());

                    }
                    if (newcontenido.split("\n")[i].contains("Número de Agente") && newcontenido.split("\n")[i].contains(ConstantsValue.GASTOS_POR_EXPEDICION)) {
                        modelo.setCveAgente(newcontenido.split("\n")[i].split("Número de Agente")[1].split(ConstantsValue.GASTOS_POR_EXPEDICION)[0].replace("###", "").trim());

                    }
                    if (newcontenido.split("\n")[i].contains("I.V.A.")) {
                        modelo.setIva(fn.castBigDecimal(fn.cleanString(newcontenido.split("\n")[i].split("I.V.A.")[1].split("###")[1].trim())));

                    }
                    if (newcontenido.split("\n")[i].contains("Prima Neta")) {
                        modelo.setPrimaneta(fn.castBigDecimal(fn.cleanString(newcontenido.split("\n")[i].split("Prima Neta")[1].split("###")[1].trim())));

                    }
                 
                    if (newcontenido.split("\n")[i].contains(ConstantsValue.GASTOS_POR_EXPEDICION)) {

                        modelo.setDerecho(fn.castBigDecimal(fn.cleanString(newcontenido.split("\n")[i].split(ConstantsValue.GASTOS_POR_EXPEDICION)[1].split("###")[1].trim())));

                    }
                    if (newcontenido.split("\n")[i].contains("Prima Total")) {
                        modelo.setPrimaTotal(fn.castBigDecimal(fn.cleanString(newcontenido.split("\n")[i].split("Prima Total")[1].replace("###", "").trim())));

                    }
                }
            }

            obtenerDatosubicacion(contenido, modelo);

            /*Proceoso para las  coberturas*/
            inicio = contenido.indexOf("Coberturas");
            fin = contenido.indexOf("Giro del Negocio");
         
            
             if(fin ==-1) { 
                 inicio =-1;
             }

            if (inicio == -1 || fin == -1) {
              
                for (int i = 0; i < contenido.split("COBERTURAS SUMA###ASEGURADA").length; i++) {
                  
                    if (i > 0) {
                        if (contenido.split("COBERTURAS SUMA###ASEGURADA")[i].contains("La Suma Asegurada opera")) {
                            newcon.append(contenido.split("COBERTURAS SUMA###ASEGURADA")[i].split("La Suma Asegurada opera")[0]);

                        }
                    }
                }
                
                if(newcon.length() == 0) {

                    for (int i = 0; i < contenido.split("Módulo - Cobertura").length; i++) {
                     
                        if (i > 0) {
                           
                            if (contenido.split("Módulo - Cobertura")[i].contains("Para las coberturas arriba")) {
                                newcon.append(contenido.split("Módulo - Cobertura")[i].split("Para las coberturas arriba")[0].replace("@@@", ""));

                            }
                        }
                    }
                }
                
            }


            
            if ((inicio > -1 && fin > -1 && inicio < fin) || newcon.length() > 100) {
                List<EstructuraCoberturasModel> coberturas = new ArrayList<>();

                if (newcon.length() > 100) {
                    newcontenido = newcon.toString();
                } else {
                    newcontenido = contenido.substring(inicio, fin);
                }

                for (int i = 0; i < newcontenido.split("\n").length; i++) {
                    EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();
                    int x = newcontenido.split("\n")[i].split("###").length;
                    if(!newcontenido.split("\n")[i].contains("Asegurada")) {
                    if (newcontenido.split("\n")[i].length() > 20) {
                        if (x == 2) {
                            cobertura.setNombre(newcontenido.split("\n")[i].split("###")[0].trim());
                            cobertura.setSa(newcontenido.split("\n")[i].split("###")[1].replace("\r", ""));
                            coberturas.add(cobertura);
                        }
                        if (x == 3) {
                            cobertura.setNombre(newcontenido.split("\n")[i].split("###")[0].trim());
                            cobertura.setSa(newcontenido.split("\n")[i].split("###")[1]);
                            coberturas.add(cobertura);
                        }
                        if (x == 4) {
                            cobertura.setNombre(newcontenido.split("\n")[i].split("###")[0].trim());
                            cobertura.setSa(newcontenido.split("\n")[i].split("###")[1]);
                            cobertura.setDeducible(newcontenido.split("\n")[i].split("###")[2].replace("\r", ""));
                            coberturas.add(cobertura);
                        }
                    }
                  }
                }
                modelo.setCoberturas(coberturas);
            }
            if (modelo.getCoberturas().isEmpty()) {
                inicio = contenido.indexOf("Sección Coberturas Suma Asegurada");
                fin = contenido.indexOf("En testimonio de lo cual la");

                if (inicio > -1 && inicio < fin) {
                    obtenerCoberturasSeccion(contenido, inicio, fin, modelo);
                }
            }

            return modelo;
        } catch (Exception ex) {
            ex.printStackTrace();
            modelo.setError(
                    AxaDiversos2Model.this.getClass().getTypeName() + " | " + ex.getMessage() + " | " + ex.getCause());
            return modelo;

        }

    }

    private void obtenerCoberturasSeccion(String contenido, int inicio, int fin, EstructuraJsonModel modelo) {
        String newcontenido = contenido.substring(inicio, fin).replace("@@@", "");
        String[] arrContenido = newcontenido.split("\n");
        List<EstructuraCoberturasModel> coberturas = new ArrayList<>();
        String seccion = "";

        for (int i = 0; i < arrContenido.length; i++) {
            int numValores = arrContenido[i].split("###").length;
            String[] valoresCoberura = arrContenido[i].split("###");
            if (fn.seccion(valoresCoberura[0].trim()).length() > 0) {
                seccion = valoresCoberura[0].trim();
            }
            if (numValores == 4) {
                EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();
                cobertura.setSeccion(seccion);
                cobertura.setNombre(valoresCoberura[1].trim());
                cobertura.setSa(valoresCoberura[2].trim());
                coberturas.add(cobertura);
            }
        }
        modelo.setCoberturas(coberturas);
    }

    private void obtenerDatosubicacion(String contenido, EstructuraJsonModel modelo) {
        String texto = contenido.replace("Ubicación  ###Contratante", UBICACION)
        .replace("Coberturas Contratadas", ConstantsValue.COBERTURAS_CONTRATADAS2);
        int inicio = texto.indexOf(UBICACION);

        if (inicio != texto.lastIndexOf(UBICACION)) {
            inicio = texto.lastIndexOf(UBICACION);
        }

        int fin = texto.indexOf(ConstantsValue.COBERTURAS_CONTRATADAS2);

        if (fin != texto.lastIndexOf(ConstantsValue.COBERTURAS_CONTRATADAS2)) {
            fin = texto.lastIndexOf(ConstantsValue.COBERTURAS_CONTRATADAS2);
        }

        if (inicio > -1 && inicio < fin) {
            String newContenido = texto.substring(inicio, fin).replace("@@@", "")
                    .replace("D ###omicilio", ConstantsValue.DOMICILIO2)
                    .replace("###F ###RANCISCO", "FRANCISCO")
                    .replace("C.P", "C/P");

            if (newContenido.contains(ConstantsValue.DATOS_GENERALES_ASEGURADO)) {
                newContenido = newContenido.split(ConstantsValue.DATOS_GENERALES_ASEGURADO)[1];
                String[] arrNewContenido = newContenido.split("\n");
                EstructuraUbicacionesModel ubicacion = new EstructuraUbicacionesModel();

                for (int i = 0; i < arrNewContenido.length; i++) {

                    if (arrNewContenido[i].contains(ConstantsValue.DOMICILIO) && (i + 1) < arrNewContenido.length) {
                        StringBuilder direccion = new StringBuilder();
                        direccion.append(arrNewContenido[i].split(ConstantsValue.DOMICILIO)[1].replace("###", "").trim());

                        if (arrNewContenido[i + 1].contains("C/P")) {
                            direccion.append(" ").append(arrNewContenido[i + 1].split("C/P")[0].replace("###", ""));
                        }
                        ubicacion.setCalle(direccion.toString().trim());
                    }
                    //CP
                    if (arrNewContenido[i].contains("C/P") && arrNewContenido[i].contains("Tel")) {
                        String aux = arrNewContenido[i].split("C/P")[1].split("Tel")[0].replace(":", "").replace(".", "").trim();
                        if (fn.isvalidCp(aux)) {
                            ubicacion.setCp(aux);
                        }
                    }
                    //
                    if (arrNewContenido[i].contains("Giro tarifa")) {
                        ubicacion.setGiro(arrNewContenido[i].split("Giro tarifa")[1].replace("###", "").replace(".-", "").trim());
                        ubicacion.setNombre(ubicacion.getNombre());
                    }
                }

                if (ubicacion.getCalle().length() > 0) {
                    List<EstructuraUbicacionesModel> ubicaciones = new ArrayList<>();
                    ubicaciones.add(ubicacion);
                    modelo.setUbicaciones(ubicaciones);
                }
            }
        }
    }
}
