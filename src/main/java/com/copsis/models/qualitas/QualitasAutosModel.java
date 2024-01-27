 package com.copsis.models.qualitas ;
import java.util.ArrayList ;
import java.util.List ;
import java.util.stream.Collectors;

import com.copsis.constants.ConstantsValue ;
import com.copsis.models.DataToolsModel ;
import com.copsis.models.EstructuraCoberturasModel ;
import com.copsis.models.EstructuraJsonModel ;
import com.copsis.models.EstructuraRecibosModel ;
	public class QualitasAutosModel {
        private DataToolsModel fn = new DataToolsModel();
        private EstructuraJsonModel modelo = new EstructuraJsonModel();
        private String contenido = "";
        private String cbo = "";
        private String cotxtra = "";

        public QualitasAutosModel(String contenido, String coberturas, String cotxtra) {
            this.contenido = contenido;
            this.cbo = coberturas;
            this.cotxtra = cotxtra;
        }

        public EstructuraJsonModel procesar() {

            int donde = 0;
            int index = 0;
            int inicio = 0;
            int inicioaux = 0;
            int fin = 0;
            StringBuilder texto = new StringBuilder();
            String subtxt = "";
            String newcontenido = "";
            String cboxt = "";
            String[] arrNewContenido;
            int inxt = 0;
            int inxty = 0;

            StringBuilder datosvehiculo = new StringBuilder();

            StringBuilder newcontenidotxt = new StringBuilder();
            StringBuilder newctx = new StringBuilder();

            contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());
            contenido = contenido.replace("IMPORTE TOTAL.", ConstantsValue.IMPORTE_TOTAL).replace("RREENNUUEEVVAA", ConstantsValue.RENUEVA)
                    .replace("MEsutnaidciop i:o:", ConstantsValue.MUNICIPIO).replace("Expedición.", "Expedición")
                    .replace("Servic i o :",ConstantsValue.SERVICIO)
                    .replace("Dom i c il i o ", "Domicilio")
                    .replace("MOTOR:", ConstantsValue.MOTOR);

            try {

                modelo.setCia(29);
                modelo.setTipo(1);
                modelo.setRamo("Autos");

                //fecha_emision
                inicio = contenido.indexOf(ConstantsValue.IMPORTE_TOTAL);
                fin = contenido.lastIndexOf("www.qualitas.com.mx");
                
            
                if (inicio > -1 && fin > inicio) {
                    newcontenido = contenido.substring(inicio, fin);
                   
                    for (String x : newcontenido.split("\r\n")) {

                        if (x.contains(" DE ") && x.split(" DE ").length == 3 && x.contains("A ")) {
                                    x = x.split("A ")[1];
                                    x = x.replace(" DE ", "-");                      
                                    if (x.split("-").length > 2) {
                                        modelo.setFechaEmision(fn.formatDate(x, ConstantsValue.FORMATO_FECHA));
                                    }

                             
                        }
                    }
                }

                if (modelo.getFechaEmision().length() == 0) {
                    inicio = contenido.indexOf(ConstantsValue.IMPORTE_TOTAL);
                    fin = contenido.indexOf("Funcionario Autorizado");

                    if (inicio > -1 && inicio < fin) {
                        newcontenido = contenido.substring(inicio, fin).replace("-DA", "").replace("@@@", "").replace("\r", "");
                        for (String textoRenglon : newcontenido.split("\n")) {
                            if (textoRenglon.split(" DE ").length == 3 && textoRenglon.split("A ").length > 1) {
                                String fecha = textoRenglon.split("A ")[1].replace(" DE ", "-").trim();
                                if (fecha.split("-").length > 2) {
                                    modelo.setFechaEmision(fn.formatDateMonthCadena(fecha));
                                }

                            }
                        }
                    }
                }

            
                inicio = contenido.lastIndexOf("ENDOSO###INCISO");
                fin = contenido.lastIndexOf("INFORMACIÓN DEL ASEGURADO");
                fin = fin == -1 ? contenido.lastIndexOf("DEL ASEGURADO") : fin;

                if (inicio > 0 && fin > 0 && inicio < fin) {
                    newcontenido = contenido.substring(inicio, fin).replace("@@@", "").replace("\r", "");

                    arrNewContenido = newcontenido.split("\n");
                    for (int i = 0; i < arrNewContenido.length; i++) {
                        if (newcontenido.split("\n")[i].contains("AUTOMÓVILES") && modelo.getPoliza().length() == 0) {
                            if ((i + 1) == arrNewContenido.length) {

                                modelo.setPoliza(arrNewContenido[i].split("###")[1]);
                                modelo.setEndoso(arrNewContenido[i].split("###")[2]);
                                if (fn.isNumeric(arrNewContenido[i].split("###")[3].trim())) {
                                    modelo.setInciso(Integer.parseInt(arrNewContenido[i].split("###")[3].trim()));
                                }

                            } else {
                                modelo.setPoliza(arrNewContenido[i + 1].split("###")[0]);
                                modelo.setEndoso(arrNewContenido[i + 1].split("###")[1]);
                                if (fn.isNumeric(arrNewContenido[i + 1].split("###")[2].trim())) {
                                    modelo.setInciso(
                                            Integer.parseInt(newcontenido.split("\n")[i + 1].split("###")[2].trim()));
                                }

                            }

                        } else {
                            if (newcontenido.split("\n")[i].contains("ENDOSO") && newcontenido.split("\n")[i].contains("INCISO") &&  (arrNewContenido[arrNewContenido.length - 1].split("###").length < 4)) {

                                    if (arrNewContenido[arrNewContenido.length - 1].split("###")[0].contains("AUTOMÓVILES")) {
                                        modelo.setPoliza(arrNewContenido[arrNewContenido.length - 1].split("###")[1]);
                                        modelo.setEndoso(arrNewContenido[arrNewContenido.length - 1].split("###")[2]);
                                        if (fn.isNumeric(arrNewContenido[arrNewContenido.length - 1].split("###")[3].trim())) {
                                            modelo.setInciso(
                                                    Integer.parseInt(arrNewContenido[arrNewContenido.length - 1].split("###")[2].trim()));
                                        }

                                    } else {

                                        if (arrNewContenido.length == 2 && arrNewContenido[arrNewContenido.length - 1].split("###").length > 3) {
                                            modelo.setPoliza(arrNewContenido[arrNewContenido.length - 1].split("###")[0]);
                                            modelo.setEndoso(arrNewContenido[arrNewContenido.length - 1].split("###")[1]);
                                            if (fn.isNumeric(arrNewContenido[arrNewContenido.length - 1].split("###")[2].trim())) {
                                                modelo.setInciso(
                                                        Integer.parseInt(arrNewContenido[arrNewContenido.length - 1].split("###")[2].trim()));
                                            }
                                        }

                                        if (arrNewContenido.length == 3 && arrNewContenido[arrNewContenido.length - 2].split("###").length < 4 && (arrNewContenido[arrNewContenido.length - 2].split("###").length > 1)){
                                            modelo.setPoliza(arrNewContenido[arrNewContenido.length - 2].split("###")[0]);
                                            modelo.setEndoso(arrNewContenido[arrNewContenido.length - 2].split("###")[1]);
                                            
                                            
                                            
                                        }

                                        if (arrNewContenido.length == 3 && arrNewContenido[arrNewContenido.length - 2].split("###").length < 4 && (arrNewContenido[arrNewContenido.length - 2].split("###").length > 1)){
                                            modelo.setPoliza(arrNewContenido[arrNewContenido.length - 2].split("###")[0]);
                                            modelo.setEndoso(arrNewContenido[arrNewContenido.length - 2].split("###")[1]);
                                            if (fn.isNumeric(arrNewContenido[arrNewContenido.length - 2].split("###")[2].trim())) {
                                                modelo.setInciso(Integer.parseInt(arrNewContenido[arrNewContenido.length - 2].split("###")[2].trim()));
                                            }
                                          
                                        }

                                        if (modelo.getPoliza().isEmpty() && arrNewContenido.length == 2 && arrNewContenido[arrNewContenido.length - 1].split("###").length < 4) {
                                            modelo.setPoliza(arrNewContenido[arrNewContenido.length - 1].split("###")[0]);

                                            modelo.setEndoso(arrNewContenido[arrNewContenido.length - 1].split("###")[1]);
                                            if (fn.isNumeric(arrNewContenido[arrNewContenido.length - 1].split("###")[2].trim())) {
                                                modelo.setInciso(
                                                        Integer.parseInt(arrNewContenido[arrNewContenido.length - 1].split("###")[2].trim()));
                                            }
                                        }

                                    }

                                

                            }

                        }

                    }
                }

                // cte_nombre
                inicio = contenido.lastIndexOf("DEL ASEGURADO");
                fin = contenido.lastIndexOf("Domicilio");

                if (inicio > -1 && fin > inicio) {
                    newcontenido = contenido.substring(inicio + 13, fin);
                    if (newcontenido.contains(ConstantsValue.RENUEVA)) {
                        modelo.setCteNombre(newcontenido.split(ConstantsValue.RENUEVA)[0].replace("@@@", "").replace("###", "").trim());
                    } else {
                        if (newcontenido.split("\r\n")[1].length() > 0) {
                            modelo.setCteNombre(newcontenido.split("\r\n")[1].replace("@@@", "").replace("###", "").trim());
                        }
                    }
                    if (newcontenido.contains(ConstantsValue.RENUEVA)) {
                        modelo.setCteNomina(newcontenido.split(ConstantsValue.RENUEVA)[0].replace("@@@", ""));
                    }
                }

                // cte_direccion
                inicio = contenido.lastIndexOf("Domicilio:");
                fin = contenido.lastIndexOf("DESCRIPCIÓN DEL VEHÍCULO");
              
                if (inicio > -1 && fin > inicio) {
                    newcontenido = contenido.substring(inicio, fin).trim();
                
                    // calle
                    inicio = newcontenido.indexOf("Domicilio:");
                    inicioaux = newcontenido.indexOf("Número:");
                    if (inicioaux == -1) {
                        inicioaux = newcontenido.indexOf("No. EXT");
                    }
                    if (inicio > -1 && inicioaux > -1) {
                       texto.append(newcontenido.substring(inicio + 10, inicioaux).replace("###", "").trim());
                        /**
                         * *********************************
                         */
                        // numero exterior
                        subtxt = newcontenido.split("\r\n")[0];
                        inicio = subtxt.indexOf("Número:");
                        index = 7;
                        if (inicio == -1) {
                            inicio = subtxt.indexOf("No. EXT.");
                            index = 8;
                        }
                        if (inicio == -1) {
                            inicio = subtxt.indexOf("No. EXT");
                            index = 7;
                        }
                        fin = subtxt.indexOf("Interior:");
                        if (fin == -1) {
                            fin = subtxt.indexOf("No. INT.");
                        }
                        if (fin == -1) {
                            fin = subtxt.indexOf(ConstantsValue.COLONIAPT);
                        }
                        if (fin == -1) {
                            fin = subtxt.indexOf(ConstantsValue.RFC4);
                        }
                        if (fin > -1) {
                            texto.append(", " + subtxt.substring(inicio + index, fin).replace("###", "").trim());
                        }

                        // numero interior
                        inicio = subtxt.indexOf("Interior:");
                        index = 9;
                        if (inicio == -1) {
                            inicio = subtxt.indexOf("No. INT.");
                            index = 8;
                        }
                        if (inicio == -1) {
                            inicio = subtxt.indexOf(ConstantsValue.COLONIAPT);
                            index = 8;
                        }
                        if (inicio == -1) {
                            inicio = subtxt.indexOf(ConstantsValue.RFC4);
                            index = 6;
                        }
                        fin = subtxt.indexOf(ConstantsValue.RFC4);
                        if (fin == -1) {
                            fin = subtxt.indexOf("COL.");
                        }
                        if (inicio > -1 && fin > inicio &&  (subtxt.substring(inicio + index, fin).replace("###", "").trim().length() > 0)) {
                               texto.append( ", "
                                        + subtxt.substring(inicio + index, fin).split("\r\n")[0].replace("###", "").trim());
                            
                        }

                        // colonia
                        inicio = newcontenido.indexOf("Colonia:");
                        index = 8;
                        if (inicio == -1) {
                            inicio = newcontenido.indexOf("COL.");
                            index = 4;
                        }
                        if (inicio > -1) {
                            texto.append( ", " + newcontenido.substring(inicio + index, newcontenido.indexOf("\r\n", inicio))
                                    .replace("###", "").trim());
                        }

                        for (String x : newcontenido.split("\r\n")) {
                            if (x.contains(ConstantsValue.MUNICIPIO)) {
                                inicio = x.indexOf(ConstantsValue.MUNICIPIO);
                                index = 10;
                                fin = x.indexOf(ConstantsValue.ESTADPT) > -1 ? x.indexOf(ConstantsValue.ESTADPT) : x.indexOf(ConstantsValue.COLONIA);
                                if (inicio > -1 && fin > inicio) {
                                    texto.append(", " + x.substring(inicio + index, fin).replace("###", "").trim());
                                }
                            }
                        }

                        // estado
                        inicio = newcontenido.indexOf(ConstantsValue.ESTADPT);
                        index = 7;
                        if (inicio > -1) {
                            subtxt = newcontenido.substring(inicio + index, newcontenido.indexOf("\r\n", inicio + index));
                            if (subtxt.contains(ConstantsValue.COLONIA)) {
                               texto.append(", " + subtxt.split(ConstantsValue.COLONIA)[0].replace("###", "").trim());
                            } else if (subtxt.contains("R.F.C.")) {
                                texto.append( ", " + subtxt.split("R.F.C.")[0].replace("###", "").trim());
                            } else {
                                texto.append(  ", " + subtxt.trim());
                            }
                        }

                        inicio = newcontenido.indexOf("C.P.");
                        fin = newcontenido.indexOf("RFC");
                        if (inicio > -1 && fin > inicio) {
                            subtxt = fn.gatos(newcontenido.substring(inicio + 4, fin));
                            if (subtxt.split("###").length == 2 && subtxt.split("\r\n").length == 1) {
                                texto.append(", " + subtxt.split("###")[1].trim());
                            }
                        }

                    }
                }
                modelo.setCteDireccion(texto.toString().trim());

                if(modelo.getCteDireccion().isEmpty()){
                     inicio = contenido.indexOf("Domicilio:");
                     fin = contenido.indexOf("DESCRIPCIÓN DEL VEHÍCULO");
                     
                     newcontenido = contenido.substring(inicio, fin).trim();

                    for(int i=0; i < newcontenido.split("\n").length; i++){
                     
                     if(newcontenido.split("\n")[i].contains("Domicilio:") && newcontenido.split("\n")[i].contains("R.F.C:")){
                        texto.append(newcontenido.split("\n")[i].split("Domicilio:")[1].split("R.F.C:")[0].replace("###", " "));

                     }
                     if(newcontenido.split("\n")[i].contains("C.P:") && newcontenido.split("\n")[i].contains("Colonia:")){
                        texto.append(newcontenido.split("\n")[i].split("C.P:")[1].replace("###", " ").replace("Municipio:", "")
                        .replace("Estado:", "").replace("Colonia:", ""));
                     }
                    }
                   modelo.setCteDireccion(texto.toString().trim());
                   List<String> valores = fn.obtenerListNumeros2(modelo.getCteDireccion());
                
                    if(!valores.isEmpty()){
           
                        modelo.setCp(valores.stream()
                            .filter(numero -> String.valueOf(numero).length() >= 4)
                            .collect(Collectors.toList()).get(0));
                    }
                }

                // rfc
                inicio = contenido.indexOf(ConstantsValue.RFC);
                index = 7;
                if (inicio == -1) {
                    inicio = contenido.lastIndexOf("RFC");
                    index = 3;
                }
                if (inicio == -1) {

               
                    inicio = contenido.lastIndexOf(ConstantsValue.RFC4);
                    index = 6;
                }

                if (inicio > -1) {
                    newcontenido = contenido.substring(inicio + index, contenido.indexOf("\r\n", inicio + index))
                            .replace("###", "").replace("-", "").trim();
                           
                    modelo.setRfc(newcontenido.replace("##", ""));
                }

                if (modelo.getRfc().length() == 0) {
                    inicio = contenido.indexOf(ConstantsValue.RFC);
                    fin = contenido.indexOf("VEHÍCULO ASEGURADO");
                    if (inicio > 0 && fin > 0 && inicio < fin) {
                        newcontenido = contenido.substring(inicio, fin).replace("@@@", "").replace("\r", "");

                        for (int i = 0; i < newcontenido.split("\n").length; i++) {
                            if (newcontenido.split("\n")[i].split(ConstantsValue.RFC).length > 1) {
                                modelo.setRfc(newcontenido.split("\n")[i].split(ConstantsValue.RFC)[1].replace("###", "").replace("-", "").trim());
                            }

                        }

                    }

                }

                // moneda
                inicio = contenido.indexOf(ConstantsValue.MONEDA_MAYUS);
                if (inicio > -1) {
                    newcontenido = contenido.substring(inicio + 6, contenido.indexOf("\r\n", inicio + 6)).replace("###", "")
                            .trim();
                    modelo.setMoneda(fn.moneda(newcontenido));
                }

                // prima_neta
                inicio = contenido.indexOf("Prima Neta");
                if (inicio > -1) {
                    newcontenido = contenido.substring(inicio + 10, contenido.indexOf("\r\n", inicio + 10))
                            .replace("###", "").replace(",", "").trim();
                    if (fn.isNumeric(newcontenido)) {
                        modelo.setPrimaneta(fn.castBigDecimal(newcontenido));
                    }
                }

   
                inicio = contenido.indexOf(ConstantsValue.TASA_FINACIMIENTO);
                if (inicio > -1) {
                    newcontenido = contenido.substring(inicio + 19, contenido.indexOf("\r\n", inicio + 19))
                            .replace("###", "").replace(",", "").trim();
                    if (fn.isNumeric(newcontenido)) {
                        modelo.setRecargo(fn.castBigDecimal(newcontenido));
                    }
                }

                
                inicio = contenido.indexOf("por Expedición");// Gastos por Expedición.
                if (inicio > -1) {
                    newcontenido = contenido.substring(inicio + 14, contenido.indexOf("\r\n", inicio + 14))
                            .replace("###", "").replace(",", "").trim();
                    if (newcontenido.contains("Pa")) {
                        newcontenido = newcontenido.split("Pa")[0].trim();
                    }
                    if (fn.isNumeric(newcontenido)) {
                        modelo.setDerecho(fn.castBigDecimal(newcontenido));
                    }
                }

                // iva
                inicio = contenido.indexOf("I.V.A.");
                if (inicio > -1) {
                    newcontenido = contenido.substring(inicio, contenido.indexOf("\r\n", inicio));
                    if (newcontenido.contains("%")) {
                        newcontenido = newcontenido.split("%")[1].replace("###", "").replace(",", "").trim();
                        if (newcontenido.contains("Bitli")) {
                            newcontenido = newcontenido.split("Bitli")[0].trim();
                        }
                        if (fn.isNumeric(newcontenido)) {
                            modelo.setIva(fn.castBigDecimal(newcontenido));
                        }
                    }
                }

                // prima_total
                inicio = contenido.indexOf(ConstantsValue.IMPORTE_TOTAL);
                if (inicio > -1) {
                    newcontenido = contenido.substring(inicio + 13, contenido.indexOf("\r\n", inicio + 13))
                            .replace("###", "").replace(",", "").trim();
                    if (fn.isNumeric(newcontenido)) {
                        modelo.setPrimaTotal(fn.castBigDecimal(newcontenido));
                    }
                }

            
         
                donde = fn.recorreContenido(contenido, ConstantsValue.AGENTE2);
                if (donde > 0) {
                    for (String dato : contenido.split("@@@")[donde].split("\r\n")) {
                        if (dato.contains(ConstantsValue.AGENTE2)) {
                            switch (dato.split("###").length) {
                                case 1:
                                    if (dato.contains(ConstantsValue.AGENTE)) {
                                        modelo.setCveAgente(dato.split("te:")[1].trim().split(" ")[0].trim());
                                    }
                                    if (modelo.getCveAgente().length() > 0) {
                                        modelo.setAgente(dato.split(modelo.getCveAgente())[1].trim());
                                    }
                                    break;
                                case 2:
                                    if (dato.split("###")[0].trim().equals("Agente:")) {
                                        modelo.setCveAgente(dato.split("###")[1].trim().split(" ")[0].trim());
                                        modelo.setAgente(dato.split("###")[1].trim().split(modelo.getCveAgente())[1].trim());
                                    }
                                    break;
                                case 3:
                                    modelo.setCveAgente(dato.split("###")[1].trim());
                                    modelo.setAgente(dato.split("###")[2].trim());
                                    break;
                                default:
                                    break;
                            }
                        }
                    }
                }
                if (modelo.getAgente().contains("ASEASOONR")) {
          
                    donde = fn.recorreContenido(contenido, ConstantsValue.IMPORTE_TOTAL);
                    if (donde > 0) {
                        for (String dato : contenido.split("@@@")[donde].split("\r\n")) {
                            if (dato.contains("Agente")) {
                                if (dato.split("###").length == 3 &&  (dato.split("###")[2].contains("IMPORTE"))) {
                                        modelo.setAgente(dato.split("###")[1].trim());
                                    
                                }
                            } else if (dato.contains("Clave") &&  (dato.split("###").length == 4 && dato.split("###")[2].contains("Teléfono:"))) {

                                    modelo.setCveAgente(dato.split("###")[1].trim());

                                
                            }
                        }
                    }
                }

                // plan
                inicio = contenido.indexOf("PLAN:");
                if (inicio > -1) {
                    newcontenido = contenido.substring(inicio + 5, contenido.indexOf("\r\n", inicio + 5)).replace("###", "")
                            .trim();
                    modelo.setPlan(newcontenido);
                }

                if (modelo.getPlan().length() == 0) {
                    inicio = cotxtra.lastIndexOf("PLAN:");
                    if (inicio > 0) {
                        inicio = inicio + 5;

                        modelo.setPlan(cotxtra.substring(inicio, inicio + 10).replace("###", "").replace("\n", ""));
                    }

                }

                // renovacion
                inicio = contenido.indexOf("RENUEVA A");
                if (inicio > -1) {

                    inxt = inicio + 20;
                    inxty = contenido.indexOf("\r\n", inicio + 10);
                    if (inxt > 0 && inxty > 0 && inxt < inxty) {
                        newcontenido = contenido.substring(inxt, contenido.indexOf("\r\n", inxty)).replace("###", "").replace("-", "").replace(":", "").trim();
                        modelo.setRenovacion(newcontenido);
                    }

                }

                // forma_pago
                inicio = contenido.indexOf(ConstantsValue.TASA_FINACIMIENTO);

                if (inicio > -1) {
                    newcontenido = contenido.substring(inicio, inicio + 200).split("\r\n")[1];

                    if (newcontenido.contains(ConstantsValue.GASTOS_POR)) {
                        newcontenido = newcontenido.split(ConstantsValue.GASTOS_POR)[0].replace("###", "").trim();
                        if (newcontenido.contains(ConstantsValue.FORMA_DEPT)) {
                            newcontenido = newcontenido.split(ConstantsValue.FORMA_DEPT)[1];
                        }
                        modelo.setFormaPago(fn.formaPago(newcontenido));
                    } else if (newcontenido.contains(ConstantsValue.FORMA_DEPT) && newcontenido.split("Forma d")[1].length() > 10) {
                        newcontenido = newcontenido.split(ConstantsValue.FORMA_DEPT)[1].replace("###", "").trim();

                        if (newcontenido.contains("Primer pago")) {
                            newcontenido = contenido.substring(inicio, inicio + 300).split("\r\n")[2];
                            if (newcontenido.contains("Gastos por Expedición")) {
                                modelo.setFormaPago(fn.formaPago(newcontenido.split("###")[0].trim()));
                            }

                        } else {
                            modelo.setFormaPago(fn.formaPago(newcontenido));
                        }

                    } else if (newcontenido.contains("Forma de Pago")) {

                        modelo.setFormaPago(fn.formaPago(newcontenido));

                    }
                }

                /**
                 * **********para cuando trae para primer recibo y
                 * subsecuente*************
                 */
                fin = contenido.indexOf("Pagos Subsecuentes");

                if (fin > -1 && modelo.getFormaPago() == 0) {
                    index = contenido.substring(fin - 150, fin).split("\r\n").length - 1;
                    newcontenido = contenido.substring(fin - 150, fin).split("\r\n")[index].trim().split(" ")[0]
                            .replace("###", "").replace("@@@", "").trim();

                    modelo.setFormaPago(fn.formaPago(newcontenido));
                }

                if (modelo.getFormaPago() == 0) {
                    inicio = contenido.lastIndexOf("Pago:");

                    if (inicio > -1) {
                        newcontenido = contenido.substring(inicio + 5, inicio + 150).split("\r\n")[0].replace("###", "")
                                .trim();

                        if (newcontenido.contains("12")) {
                            modelo.setFormaPago(fn.formaPago(newcontenido.split("12")[0].trim()));
                        } else {
                            if (newcontenido.contains("Pagos")) {
                                modelo.setFormaPago(fn.formaPago(newcontenido.split("Pagos")[0]));
                            } else {

                                if (newcontenido.contains("Gastos")) {
                                    modelo.setFormaPago(fn.formaPago(newcontenido.split("Gastos")[0]));
                                } else {
                                    modelo.setFormaPago(fn.formaPago(newcontenido));
                                }

                            }

                        }

                    }
                }
                if (modelo.getFormaPago() == 0) {
                    inicio = contenido.indexOf(ConstantsValue.MONEDA_MAYUS);
                    fin = contenido.indexOf(ConstantsValue.IMPORTE_TOTAL);
                    if (inicio > -1 && fin > -1) {
                        newcontenido = contenido.substring(inicio, fin).replace("@@@", "").replace("\r", "");

                        modelo.setFormaPago(fn.formaPagoSring(newcontenido));

                    }

                }

                // primer_prima_total
                inicio = contenido.indexOf("Pago Inicial:");
                index = 13;
                if (inicio == -1) {
                    inicio = contenido.indexOf("Primer pago");
                    index = 11;
                }
                if (inicio > -1) {
                    newcontenido = contenido.substring(inicio + index, contenido.indexOf("\r\n", inicio + index))
                            .replace("###", "");
                    if (newcontenido.contains(ConstantsValue.TASA_FINACIMIENTO)) {
                        newcontenido = newcontenido.split(ConstantsValue.TASA_FINACIMIENTO)[0].replace(",", "").trim();
                    }

                    if (fn.isNumeric(newcontenido)) {
                        modelo.setPrimerPrimatotal(fn.castBigDecimal(fn.castDouble(newcontenido)));
                    }
                }

                // sub_prima_total
                inicio = contenido.indexOf("Pagos Subsecuentes:");
                index = 19;
                if (inicio == -1) {
                    inicio = contenido.indexOf("Pago(s) Subsecuente(s)");
                    index = 22;
                }
                if (inicio > -1) {
                    newcontenido = contenido.substring(inicio + index, contenido.indexOf("\r\n", inicio + 19))
                            .replace("###", "");
                    if (newcontenido.contains("Gastos por")) {
                        newcontenido = newcontenido.split("Gastos por")[0].replace(",", "").trim();
                    }

                    if (fn.isNumeric(newcontenido)) {
                        modelo.setSubPrimatotal(fn.castBigDecimal(fn.castDouble(newcontenido)));
                    }
                }
              
                inicio = contenido.lastIndexOf(ConstantsValue.HASTA_LAS);
                if (inicio > -1) {
                    newcontenido = contenido.substring(inicio, contenido.indexOf("\r\n", inicio)).replace("del:", "del");

                    if (newcontenido.contains("Plazo")) {
                        newcontenido = newcontenido.split("Plazo")[0].split("del")[1].replace("###", "").trim();
                        if (newcontenido.length() == 11) {
                            modelo.setVigenciaA(fn.formatDate(newcontenido, ConstantsValue.FORMATO_FECHA));
                          
                        }
                    } else {
                        newcontenido = !fn.obtenVigePoliza2(newcontenido).isEmpty() ? fn.obtenVigePoliza2(newcontenido).get(0) : "";
                        if (newcontenido.length() > 0) {
                            modelo.setVigenciaA(fn.formatDateMonthCadena(newcontenido));
                        }

                    }
                }
          
                inicio = contenido.lastIndexOf("Desde las");
                if (inicio > -1) {
                    newcontenido = contenido.substring(inicio + 9, contenido.indexOf("\r\n", inicio + 9))
                            .replace("del:", "del").replace("Servic  i o  :", ConstantsValue.SERVICIO).replace("Servic ###io:",ConstantsValue.SERVICIO);

                    if (newcontenido.contains(ConstantsValue.SERVICIO)) {
                        newcontenido = fn.gatos(newcontenido.split(ConstantsValue.SERVICIO)[0].split("del")[1].trim());

                        if (newcontenido.split("###").length == 2 || (newcontenido.split("###").length == 1 && newcontenido.contains("-"))) {
                            newcontenido = fn.formatDate(newcontenido.split("###")[0].trim(), ConstantsValue.FORMATO_FECHA);

                            if (newcontenido.length() == 10) {
                                modelo.setVigenciaDe(newcontenido);
                            }
                        }
                    } else {
                        if (newcontenido.contains(ConstantsValue.HASTA_LAS)) {
                            newcontenido = newcontenido.split(ConstantsValue.HASTA_LAS)[0].split("del")[1].replace("###", "").trim();
                            modelo.setVigenciaDe(fn.formatDate(newcontenido, ConstantsValue.FORMATO_FECHA));

                            if (modelo.getVigenciaA().isEmpty()) {
                          

                                newcontenido = contenido.substring(inicio + 9, contenido.indexOf("\r\n", inicio + 9))
                                        .replace("del:", "del").replace("Servic  i o  :", "Servicio:");

                                modelo.setVigenciaA(
                                        fn.formatDate(newcontenido.split("\r\n")[0].split(ConstantsValue.HASTA_LAS)[1].split("del")[1]
                                                .replace("###", "").trim(), ConstantsValue.FORMATO_FECHA));
                            }
                        } else if (newcontenido.contains("del") && newcontenido.split("###").length > 1) {
                            modelo.setVigenciaDe(fn.formatDate(newcontenido.split("###")[1].trim(), ConstantsValue.FORMATO_FECHA));
                        }
                    }
                }

      
                            
                // cp
                inicio = contenido.lastIndexOf("C.P");
              
                index = 3;
                if (inicio > -1) {
                    newcontenido = contenido.substring(inicio + index, contenido.indexOf("\r\n", inicio + index))
                            .replace(".:", "").replace(".", "").replace(":", "");
                           

                    if (newcontenido.contains(ConstantsValue.MUNICIPIO)) {
                      
                        newcontenido = newcontenido.split(ConstantsValue.MUNICIPIO)[0].replace("###", "").trim();
                         List<String> valores = fn.obtenerListNumeros2(newcontenido);
                                                                       
                        if(!valores.isEmpty()){
                          
                            modelo.setCp(valores.stream()
                                .filter(numero -> String.valueOf(numero).length() >= 4)
                                .collect(Collectors.toList()).get(0));
                        }
                    } else if (newcontenido.split("###").length > 2) {
                        newcontenido = fn.gatos(newcontenido);
                        if (fn.isNumeric(newcontenido.split("###")[0].trim())) {
                            String cp = newcontenido.split("###")[0].trim();
                       List<String> valores = fn.obtenerListNumeros2(cp);                                                  
                        if(!valores.isEmpty()){
                            modelo.setCp(valores.stream()
                                .filter(numero -> String.valueOf(numero).length() >= 4)
                                .collect(Collectors.toList()).get(0));
                        }
                        }
                    } else {
                      List<String> valores = fn.obtenerListNumeros2(newcontenido);                                                  
                        if(!valores.isEmpty()){
                            modelo.setCp(valores.stream()
                                .filter(numero -> String.valueOf(numero).length() >= 4)
                                .collect(Collectors.toList()).get(0));
                        }
                    }

                    if (modelo.getCp().isEmpty()) {

                        List<String> valores = fn.obtenerListNumeros2(newcontenido);                                                  
                        if(!valores.isEmpty()){
                            modelo.setCp(valores.stream()
                                .filter(numero -> String.valueOf(numero).length() >= 4)
                                .collect(Collectors.toList()).get(0));
                        }

                    }
                }

                // clave
                inicio = contenido.indexOf("VEHÍCULO ASEGURADO\r\n");
                index = 20;
                if (inicio > -1) {
                    newcontenido = contenido.substring(inicio + index, contenido.indexOf("\r\n", inicio + index))
                            .replace("@@@", "");
                    if (newcontenido.contains("(")) {
                        newcontenido = newcontenido.replace("(", "&&&");
                        newcontenido = newcontenido.split("&&&")[0].replace("###", "").trim();
                        modelo.setClave(newcontenido);
                    } else if (newcontenido.split("###").length == 2) {
                        modelo.setClave(newcontenido.split("###")[0].trim());
                    }
                }

                // marca
                // descripcion
                inicio = contenido.indexOf("VEHÍCULO ASEGURADO");
                index = 20;

                if (inicio > -1) {
                    newcontenido = contenido.substring(inicio + index, contenido.indexOf("\r\n", inicio + index));

                    if (newcontenido.contains("(")) {
                        newcontenido = newcontenido.replace(")", "&&&");
                        newcontenido = newcontenido.split("&&&")[1].replace("###", "").trim();
                        modelo.setMarca(newcontenido.split(" ")[0].trim());
                        modelo.setDescripcion(newcontenido);
                    } else if (newcontenido.split("###").length == 2) {
                        newcontenido = newcontenido.split("###")[1].trim();
                        modelo.setMarca(newcontenido.split(" ")[0].trim());
                        modelo.setDescripcion(newcontenido);
                    } else if (!newcontenido.contains("#") && newcontenido.split(" ").length > 2) {
                        modelo.setMarca(newcontenido.split(" ")[1].trim());
                        modelo.setDescripcion(newcontenido.split(modelo.getMarca())[1].replace("\r", "").trim());
                    }
                }

                // modelo
                inicio = contenido.indexOf(ConstantsValue.MODELO);
                if (inicio > -1) {
                    newcontenido = contenido.substring(inicio + 7, contenido.indexOf("\r\n", inicio + 7));
                    if (newcontenido.contains(ConstantsValue.COLOR)) {
                        newcontenido = newcontenido.split(ConstantsValue.COLOR)[0].replace("###", "").trim();
                        if (fn.isNumeric(newcontenido)) {
                            modelo.setModelo(Integer.parseInt(newcontenido));
                        }
                    }
                }

                // serie
                inicio = contenido.indexOf(ConstantsValue.SERIE);
                fin = contenido.indexOf(ConstantsValue.MOTOR);

                if (inicio > 0 && fin > 0 && inicio < fin) {
                    newcontenido = contenido.substring(inicio, fin).split(ConstantsValue.SERIE)[1].replace("###", "").trim();
                    modelo.setSerie(newcontenido);

                }

                
                inicio = contenido.indexOf(ConstantsValue.MOTOR);
                if (inicio > -1) {
                    newcontenido = contenido.substring(inicio + 6, contenido.indexOf("\r\n", inicio + 6));
                    if (newcontenido.contains(ConstantsValue.PLACASPT)) {
                        if (newcontenido.contains("REPUVE")) {
                            newcontenido = newcontenido.split("REPUVE")[0];
                        }
                        newcontenido = newcontenido.split(ConstantsValue.PLACASPT)[0].replace("###", "").trim();
                        modelo.setMotor(newcontenido);
                    }
                }

                // placas
                inicio = contenido.indexOf(ConstantsValue.PLACAS);
                if (inicio > -1) {
                    newcontenido = contenido.substring(inicio + 7, contenido.indexOf("\r\n", inicio + 7)).replace("###", "")
                            .replace("-", "").replace(" ", "").trim();
                    modelo.setPlacas(newcontenido);
                }
               
                if (modelo.getCp().trim().length() < 4) {
                    modelo.setCp("");
                }

                boolean cp = false;
               

                inicio = cotxtra.indexOf("RESTO DE LA HOJA EN BLANCO");
                fin = cotxtra.indexOf("GESTIÓN  PRIMA");
                if (inicio > 0 && fin > 0 && inicio < fin) {

                    String contenidocp = cotxtra.substring(inicio, fin);

                    for (int i = 0; i < contenidocp.split("\n").length; i++) {

                        if (cp && contenidocp.split("\n")[i].contains("C.P.:")) {

                            modelo.setCp(contenidocp.split("\n")[i].split("C.P.:")[1].trim().substring(0, 5));
                            cp = true;
                        }
                    }
                }
                if (modelo.getDescripcion().length() == 0 && modelo.getSerie().length() == 0) {
                    boolean existe = false;
                    StringBuilder vehiculoDatos = new StringBuilder();
                    for (int i = 0; i < cotxtra.split(ConstantsValue.VEHICULO_ASEGURADO).length; i++) {
                        if (cotxtra.split(ConstantsValue.VEHICULO_ASEGURADO)[i].contains("Fecha Vencimiento del pago") &&  (cotxtra.split(ConstantsValue.VEHICULO_ASEGURADO)[i].split("Fecha Vencimiento")[0].contains("Modelo") && !existe)) {
                                vehiculoDatos.append(cotxtra.split(ConstantsValue.VEHICULO_ASEGURADO)[i].split("Fecha Vencimiento")[0].trim());
                                existe = true;
                            

                        }

                    }

                    for (int i = 0; i < vehiculoDatos.toString().split("\n").length; i++) {
                        if (i == 0 && vehiculoDatos.toString().split("\n")[0].length() > 2) {
                            modelo.setDescripcion(vehiculoDatos.toString().split("\n")[0].replace("()", "###").split("###")[1]);
                        }

                        if (vehiculoDatos.toString().split("\n")[i].contains("Modelo:") && vehiculoDatos.toString().split("\n")[i].contains("Color")) {
                            modelo.setModelo(fn.castInteger(fn.obtenerListNumeros2(vehiculoDatos.toString().split("\n")[i].split("Modelo:")[1]).get(0)));
                        }
                        if (vehiculoDatos.toString().split("\n")[i].contains(ConstantsValue.SERIE) && vehiculoDatos.toString().split("\n")[i].contains(ConstantsValue.MOTOR)
                                && vehiculoDatos.toString().split("\n")[i].contains(ConstantsValue.PLACAS)) {
                            modelo.setSerie(vehiculoDatos.toString().split("\n")[i].split(ConstantsValue.SERIE)[1].split(ConstantsValue.MOTOR)[0].trim());
                            modelo.setMotor(vehiculoDatos.toString().split("\n")[i].split(ConstantsValue.MOTOR)[1].split(ConstantsValue.PLACAS)[0].trim().replace("###", ""));
                            if (vehiculoDatos.toString().split("\n")[i].split("Moto")[1].length() > 6 && vehiculoDatos.toString().split("\n")[i].split(ConstantsValue.PLACASPT)[1].length() > 10) {
                                modelo.setPlacas(vehiculoDatos.toString().split("\n")[i].split(ConstantsValue.PLACAS)[1]);
                            }

                        }
                    }
                }

                // coberturas
                inicio = contenido.indexOf("PRIMAS");
                if (inicio == -1) {
                    inicio = contenido.indexOf("PRIMA");
                }
                fin = contenido.indexOf(ConstantsValue.MONEDA_MAYUS);

                if (inicio > -1 && fin > inicio) {
                    newcontenido = contenido.substring(inicio, fin).replace("@@@", "").trim();

                    if (newcontenido.contains("La Unidad de Medida")) {
                        newcontenido = newcontenido.split("La Unidad de Medida")[0].trim();
                    }
                    if (newcontenido.contains("Servicios de Asistencia Vial")) {
                        newcontenido = newcontenido.split("Servicios de Asistencia Vial")[0].trim();
                    }

                    List<EstructuraCoberturasModel> coberturas = new ArrayList<>();

                    for (String x : newcontenido.split("\r\n")) {
                        EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();
                        switch (x.split("###").length) {
                            case 4:
                                cobertura.setNombre(x.split("###")[0].trim());
                                cobertura.setSa(fn.eliminaSpacios(x.split("###")[1].trim(), ' ', ""));
                                cobertura.setDeducible(x.split("###")[2].trim());
                                coberturas.add(cobertura);
                                break;
                            case 3:
                                cobertura.setNombre(x.split("###")[0].trim());
                                cobertura.setSa(fn.eliminaSpacios(x.split("###")[1].trim(), ' ', ""));
                                coberturas.add(cobertura);
                                break;
                            default:
                                break;
                        }

                    }

                    modelo.setCoberturas(coberturas);
                }

                if (modelo.getCoberturas().isEmpty()) {
                    
                    inicio = cbo.indexOf("PRIMAS");
                    if (inicio == -1) {
                        inicio = cbo.indexOf("PRIMA");
                    }
                    fin = cbo.indexOf(ConstantsValue.MONEDA_MAYUS);

                    if (cbo.indexOf(ConstantsValue.COBERTURAS_CONTRATADAS) > -1 && cbo.indexOf("SUMA ASEGURADA") > -1 && cbo.indexOf("DEDUCIBLE") > -1) {
                        inicio = cbo.lastIndexOf(ConstantsValue.COBERTURAS_CONTRATADAS);
                        fin = cbo.indexOf("Para RC en el extranjero");
                    }

                    if (inicio > -1 && fin > inicio) {

                        newcontenido = cbo.substring(inicio, fin).replace("\u00A0", "").replace("@@@", "").trim();

                        if (newcontenido.contains(ConstantsValue.ASVI_QUALITAS)) {
                            newcontenido = newcontenido.split(ConstantsValue.ASVI_QUALITAS)[0].trim();
                        }
                        List<EstructuraCoberturasModel> coberturas = new ArrayList<>();

                        for (String x : newcontenido.split("\n")) {
                            EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();

                            switch (x.split("###").length) {
                                case 4:
                                    cobertura.setNombre(x.split("###")[0].trim());
                                    cobertura.setSa(fn.eliminaSpacios(x.split("###")[1].trim(), ' ', ""));
                                    cobertura.setDeducible(x.split("###")[2].trim());
                                    coberturas.add(cobertura);
                                    break;
                                case 3:
                                    cobertura.setNombre(x.split("###")[0].trim());
                                    cobertura.setSa(fn.eliminaSpacios(x.split("###")[1].trim(), ' ', ""));
                                    coberturas.add(cobertura);
                                    break;
                                default:
                                    break;
                            }

                        }

                        modelo.setCoberturas(coberturas);
                    }

                }

                if (modelo.getCoberturas().isEmpty() && cotxtra.length() > 0) {
                    for (int i = 0; i < cotxtra.split(ConstantsValue.COBERTURAS_CONTRATADAS).length; i++) {
                        if (i > 0) {
                            newctx.append(cotxtra.split(ConstantsValue.COBERTURAS_CONTRATADAS)[i].split("MONEDA")[0].replace("@@@", ""));
                        }
                    }
                    cboxt = fn.remplazarMultiple(newctx.toString(), fn.remplazosGenerales());
                    if (cboxt.length() > 0) {
                        List<EstructuraCoberturasModel> coberturas = new ArrayList<>();
                        for (int i = 0; i < cboxt.split("\n").length; i++) {
                            EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();
                            if (!cboxt.split("\n")[i].contains("DEDUCIBLE") && !cboxt.split("\n")[i].contains("deducible")
                                    && !cboxt.split("\n")[i].contains("Fronterizos") && !cboxt.split("\n")[i].contains("sistema")
                                    && !cboxt.split("\n")[i].contains("Servicios")) {

                                int sp = cboxt.split("\n")[i].split("###").length;

                                switch (sp) {
                                    case 4:
                                        cobertura.setNombre(cboxt.split("\n")[i].split("###")[0].trim());
                                        cobertura.setSa(fn.eliminaSpacios(cboxt.split("\n")[i].split("###")[1].trim(), ' ', ""));
                                        cobertura.setDeducible(cboxt.split("\n")[i].split("###")[2].trim());
                                        coberturas.add(cobertura);
                                        break;
                                    case 3:
                                        cobertura.setNombre(cboxt.split("\n")[i].split("###")[0].trim());
                                        cobertura.setSa(fn.eliminaSpacios(cboxt.split("\n")[i].split("###")[1].trim(), ' ', ""));
                                        coberturas.add(cobertura);
                                        break;
                                    default:
                                        break;
                                }

                            }
                        }
                        modelo.setCoberturas(coberturas);
                    }
                }

                if (cbo.length() > 0) {

                    for (int i = 0; i < cbo.split(ConstantsValue.VEHICULO_ASEGURADO).length; i++) {
                        if (i > 0 &&  (cbo.split(ConstantsValue.VEHICULO_ASEGURADO)[i].contains("Modelo") && cbo.split(ConstantsValue.VEHICULO_ASEGURADO)[i].contains("Placas"))) {

                                datosvehiculo.append(cbo.split(ConstantsValue.VEHICULO_ASEGURADO)[i].split("COBERTURAS CONTRATADA")[0]);

                            
                        }
                    }
                    if (datosvehiculo.toString().length() > 0) {
                        for (int i = 0; i < datosvehiculo.toString().split("\n").length; i++) {
                            if (datosvehiculo.toString().split("\n")[i].contains("Desde") && fn.obtenVigePoliza2(datosvehiculo.toString().split("\n")[i]).size() == 2) {
                                modelo.setFechaVence(fn.formatDateMonthCadena(fn.obtenVigePoliza2(datosvehiculo.toString().split("\n")[i]).get(1).trim().replace("/", "-")));
                            }
                        }
                    }
                }

                List<EstructuraRecibosModel> recibos = new ArrayList<>();

               

                 if(modelo.getFormaPago()==1){
                     EstructuraRecibosModel recibo = new EstructuraRecibosModel();
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
                        recibo.setIva(modelo.getIva());

                        recibo.setPrimaTotal(modelo.getPrimaTotal());
                        recibo.setAjusteUno(modelo.getAjusteUno());
                        recibo.setAjusteDos(modelo.getAjusteDos());
                        recibo.setCargoExtra(modelo.getCargoExtra());
                        recibos.add(recibo);
                 }
                       
               

                
                modelo.setRecibos(recibos);

                inicio = contenido.indexOf("MONEDA");
                fin = contenido.indexOf(ConstantsValue.IMPORTE_TOTAL);

                newcontenidotxt = new StringBuilder();
                newcontenidotxt.append(fn.extracted(inicio, fin, contenido));
                for (int i = 0; i < newcontenidotxt.toString().split("\n").length; i++) {
                    if (newcontenidotxt.toString().split("\n")[i].contains("DESCUENTOS") &&  (newcontenidotxt.toString().split("\n")[i].split("DESCUENT")[1].length() > 5)) {
                            modelo.setAjusteUno(fn.castBigDecimal(fn.preparaPrimas(newcontenidotxt.toString().split("\n")[i].split("DESCUENTOS")[1].replace("###", ""))));
                        
                    }
                }
                if (modelo.getFormaPago() == 0) {
                    modelo.setFormaPago(1);
                }

                if(modelo.getFechaEmision().isEmpty() && !modelo.getVigenciaDe().isEmpty()){
                    modelo.setFechaEmision(modelo.getVigenciaDe());
                }


                return modelo;
            } catch (Exception ex) {       
                modelo.setError(
                        QualitasAutosModel.this.getClass().getTypeName() + " | " + ex.getMessage() + " | " + ex.getCause());
                return modelo;
            }
        }

    }