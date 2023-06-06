package com.copsis.models.axa.salud;

import java.util.ArrayList;
import java.util.List;

import com.copsis.constants.ConstantsValue;
import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraAseguradosModel;
import com.copsis.models.EstructuraCoberturasModel;
import com.copsis.models.EstructuraJsonModel;
import com.copsis.models.EstructuraRecibosModel;

public class AxaSaludV2Model {
    // Clases
    private DataToolsModel fn = new DataToolsModel();
    private EstructuraJsonModel modelo = new EstructuraJsonModel();
    // Varaibles
    private String contenido = "";

    private boolean cp = false;
    private boolean vigenciaD = false;

    public AxaSaludV2Model(String contenido) {
        this.contenido = contenido;
    }

    public EstructuraJsonModel procesar() {
        String inicontenido = "";
        String newcontenido = "";
        String resultado = "";
        int inicio = 0;
        int fin = 0;

        inicontenido = fn.fixContenido(contenido);
        contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales())
                .replace("Costo###por###Servicio", "Costo por Servicio")
                .replace("P ###rotección Dental", "Protección Dental")
                .replace("T ###u Médico 24 Hrs", "Tu Médico 24 Hrs###")
                .replace("B ###eneficio de Atn Médica", "Beneficio de Atn Médica###")
                .replace("C ###omplicaciones de GMM no cubiertos", "Complicaciones de GMM no cubiertos###")
                .replace("D ###e acuerdo a Condiciones Generales", " De acuerdo a Condiciones Generales")
                .replace("D ###educible Cero por Accidente", "Deducible Cero por Accidente###")
                .replace("N ###-A", "###N-A").replace("C ###osto Preferencial", "Costo Preferencial ")
                .replace("C ###obertura Nacional", "Cobertura Nacional").replace("###N ###o Aplica", "###No Aplica")
                .replace("N ###o Aplica ", "No Aplica ");
        try {
            modelo.setTipo(3);
            modelo.setCia(20);

            inicio = contenido.indexOf("Datos###del###contratante###Póliza");
            if (inicio == -1) {
                inicio = contenido.indexOf("Datos del contratante###Póliza");
            }
            fin = contenido.indexOf("Coberturas-Servicios");

            if (inicio > 0 && fin > 0 && inicio < fin) {
                newcontenido = contenido.substring(inicio, fin);
                for (int i = 0; i < newcontenido.split("\n").length; i++) {
                    if (newcontenido.split("\n")[i].contains("contratante")
                            || newcontenido.split("\n")[i].contains("Póliza")) {
                        if (newcontenido.split("\n")[i + 1].contains(ConstantsValue.NOMBRE)) {
                            String contratante = newcontenido.split("\n")[i + 1].split(ConstantsValue.NOMBRE)[1]
                                    .split("###")[newcontenido.split("\n")[i + 1].split("###").length - 2].replace(":",
                                            "");
                            if (contratante.contains(",")) {
                                modelo.setCteNombre(
                                        (contratante.split(",")[1] + " " + contratante.split(",")[0]).trim());
                            } else {
                                modelo.setCteNombre(contratante);
                            }
                        }
                        modelo.setPoliza(newcontenido.split("\n")[i + 1].split("Nombre")[1]
                                .split("###")[newcontenido.split("\n")[i + 1].split("###").length - 1].replace("\r",
                                        ""));
                    }
                    if (newcontenido.split("\n")[i].contains("plan")
                            && newcontenido.split("\n")[i].contains(ConstantsValue.SOLICITUD_DOMICILIO)) {
                        modelo.setPlan(newcontenido.split("\n")[i].split(ConstantsValue.SOLICITUD_DOMICILIO)[1]
                                .split("###")[newcontenido.split("\n")[i].split(ConstantsValue.SOLICITUD_DOMICILIO)[1]
                                        .split("###").length - 3]);
                    } else if (newcontenido.split("\n")[i].contains("plan")
                            && newcontenido.split("\n")[i].contains("Solicitud")) {

                        if (newcontenido.split("\n")[i + 1].split("###").length == 2) {
                            modelo.setPlan(newcontenido.split("\n")[i + 1].split("###")[0].replace("@@@", ""));
                        } else {
                            modelo.setPlan(newcontenido.split("\n")[i + 1].split("###")[1].replace("@@@", ""));
                        }

                    } else if (newcontenido.split("\n")[i].contains("plan")) {
                        modelo.setPlan(newcontenido.split("\n")[i + 1].split("###")[0].replace("@@@", ""));
                    }

                    if (newcontenido.split("\n")[i].contains("C.P.") && newcontenido.split("\n")[i].contains("Fecha")
                            && newcontenido.split("\n")[i].contains(ConstantsValue.VIGENCIA3)) {// C.P./fecha/vigencia

                        modelo.setCp(newcontenido.split("\n")[i].split("C.P.")[1].split("Ciudad")[0].replace("###", "")
                                .trim());
                        if (modelo.getCp().length() > 5) {
                            int sp = newcontenido.split("\n")[i].split("C.P.")[1].split("###").length;
                            if (sp > 0) {
                                
                                modelo.setCp(newcontenido.split("\n")[i].split("C.P.")[1].split("###")[0]);
                            }
                        }
                        
                        resultado = newcontenido.split("\n")[i].split("Fecha")[0].replace(modelo.getCp(), "")
                                .replace("C.P", "").replace("###", " ".replace("\r", ""));
                        if(resultado.contains("Ciudad")) {
                            resultado = resultado.split("Ciudad")[0].trim();
                        }

                        // vigencias
                        modelo.setVigenciaDe(
                                fn.formatDateMonthCadena(newcontenido.split("\n")[i].split(ConstantsValue.VIGENCIA3)[1]
                                        .replace("###", "").replace("\r", "")));
                        if (newcontenido.split("\n")[i + 1].contains("-")) {
                            modelo.setVigenciaA(fn
                                    .formatDateMonthCadena(newcontenido.split("\n")[i + 1].replace("@@@", "").trim()));
                        }

                        cp = true;
                        vigenciaD = true;
                    } else if (newcontenido.split("\n")[i].contains("C.P.")
                            && newcontenido.split("\n")[i].contains(ConstantsValue.VIGENCIA3)) {
                        modelo.setVigenciaDe(
                                fn.formatDateMonthCadena(newcontenido.split("\n")[i].split(ConstantsValue.VIGENCIA3)[1]
                                        .replace("###", "").trim().replace("\r", "")));
                        vigenciaD = true;
                    }

                    if (newcontenido.split("\n")[i].contains("C.P.") && !cp) {
                        modelo.setCp(newcontenido.split("\n")[i].split("C.P.")[1].split("Ciudad")[0].replace("###", "")
                                .trim());
                        if (modelo.getCp().length() > 5) {
                            int sp = newcontenido.split("\n")[i].split("C.P.")[1].split("###").length;
                            if (sp > 0) {
                                modelo.setCp(newcontenido.split("\n")[i].split("C.P.")[1].split("###")[0]);
                            }
                        }

                    }
                    if (newcontenido.split("\n")[i].contains("inicio de vigencia") && !vigenciaD) {
                        modelo.setVigenciaDe(
                                fn.formatDateMonthCadena(newcontenido.split("\n")[i].split("inicio de vigencia")[1]
                                        .replace("###", "").trim().replace("\r", "")));

                    }
                    if (newcontenido.split("\n")[i].contains("R.F.C:")
                            && newcontenido.split("\n")[i].contains("Teléfono")) {
                        modelo.setRfc(newcontenido.split("\n")[i].split("R.F.C:")[1].split("Teléfono")[0]
                                .replace("###", "").replace("\u00A0", "").trim());
                    }

                    if (newcontenido.split("\n")[i].contains("R.F.C.")
                            && newcontenido.split("\n")[i].contains(ConstantsValue.VIGENCIA3)
                            && newcontenido.split("\n")[i].split(ConstantsValue.VIGENCIA3)[1].length() > 7) {

                        modelo.setVigenciaA(
                                fn.formatDateMonthCadena(newcontenido.split("\n")[i].split(ConstantsValue.VIGENCIA3)[1]
                                        .replace("###", "").trim().replace("\r", "")));
                    } else if (newcontenido.split("\n")[i].contains("Fecha de fin de vigencia")) {

                        modelo.setVigenciaA(fn
                                .formatDateMonthCadena(newcontenido.split("\n")[i].split("Fecha de fin de vigencia")[1]
                                        .replace("###", "").trim().replace("\r", "")));

                    }

                    if (newcontenido.split("\n")[i].contains("emisión")) {
                        modelo.setFechaEmision(fn.formatDateMonthCadena(newcontenido.split("\n")[i].split("emisión")[1]
                                .replace("###", "").trim().replace("\r", "")));
                    }
                    if (newcontenido.split("\n")[i].contains("Domicilio")) {
                        modelo.setCteDireccion(newcontenido.split("\n")[i].split("Domicilio")[1].replace("###", "")
                                .replace(":", "").replace(",", "").replace("\r", "") + " " + resultado);
                    }
                    if (newcontenido.split("\n")[i].contains("Agente:")) {
                        modelo.setCveAgente(newcontenido.split("\n")[i].split("Agente:")[1].split("###")[1]);
                        if (newcontenido.split("\n")[i + 1].contains(ConstantsValue.PERIODO)) {
                            modelo.setAgente(
                                    (newcontenido.split("\n")[i].split(modelo.getCveAgente())[1].replace("###", " ")
                                            + " "
                                            + newcontenido.split("\n")[i + 1].split(ConstantsValue.PERIODO)[0].trim())
                                                    .trim().replace("@@@", "").replace("\r", "").replace("###", "")
                                                    .trim());
                        } else {
                            modelo.setAgente((newcontenido.split("\n")[i].split(modelo.getCveAgente())[1]
                                    .split(ConstantsValue.PERIODO)[0].replace("###", " ")).replace("\r", "").trim());
                        }
                    }
                    if (newcontenido.split("\n")[i].contains("pago")
                            && newcontenido.split("\n")[i].contains("Frecuencia")) {
                        modelo.setFormaPago(fn.formaPago(newcontenido.split("\n")[i].split("pago")[1].replace("###", "")
                                .replace("\r", "").trim()));
                    }

                }
            }
            
            //codigo postal en una linea
            if(modelo.getCp().length() != 5 && newcontenido.contains("Domicilio") && newcontenido.contains("@@@Fecha de fin")) {
                if(newcontenido.split("Domicilio")[1].split("@@@Fecha de fin")[0].split("\n").length == 4) {
                    modelo.setCp(newcontenido.split("Domicilio")[1].split("@@@Fecha de fin")[0].split("\n")[3].trim());
                }
            }

            modelo.setMoneda(1);
            inicio = contenido.indexOf("Prima###Neta");
            if (inicio == -1) {
                inicio = contenido.indexOf("Prima Neta");
            }
            fin = contenido.indexOf("HOJA 1 ");
            if (fin == -1) {
                fin = contenido.indexOf("Este Documento No");
            }
    
            if (inicio > 0 && fin > 0 && inicio < fin) {
                newcontenido = contenido.substring(inicio, fin);
                for (int i = 0; i < newcontenido.split("\n").length; i++) {
                    if (newcontenido.split("\n")[i].contains("Neta")) {
                        modelo.setPrimaneta(fn.castBigDecimal(fn.castDouble(
                                newcontenido.split("\n")[i].split("Neta")[1].replace(",", "").replace("###", ""))));

                    }
                    if (newcontenido.split("\n")[i].contains("Recargo")) {
                        modelo.setRecargo(fn.castBigDecimal(
                                fn.castDouble(newcontenido.split("\n")[i].split("fraccionado")[1].replace("###", ""))));

                    }

                    if (newcontenido.split("\n")[i].contains("Derecho")) {
                        modelo.setDerecho(fn.castBigDecimal(
                                fn.castDouble(newcontenido.split("\n")[i].split("póliza")[1].replace("###", ""))));

                    }

                    if (newcontenido.split("\n")[i].contains("I.V.A.")) {
                        modelo.setIva(fn.castBigDecimal(
                                fn.castDouble(newcontenido.split("\n")[i].split("I.V.A.")[1].replace("###", ""))));

                    }
                    if (newcontenido.split("\n")[i].contains("Prima")
                            && newcontenido.split("\n")[i].contains("total")) {
                        modelo.setPrimaTotal(fn.castBigDecimal(
                                fn.castDouble(newcontenido.split("\n")[i].split("total")[1].replace("###", ""))));
                    }
                }
            }

            // Sa,deducibel,coaseguro

            inicio = contenido.indexOf("Condiciones Contratadas");
            fin = contenido.indexOf("Coberturas-Servicios ");
            if (inicio > 0 && fin > 0 && inicio < fin) {
                newcontenido = contenido.substring(inicio, fin);
                for (int i = 0; i < newcontenido.split("\n").length; i++) {
                    if (newcontenido.split("\n")[i].contains("SumaAsegurada")) {
                        modelo.setSa(newcontenido.split("\n")[i].split("SumaAsegurada")[1].replace("###", "")
                                .replace("\r", ""));
                    }
                    if (newcontenido.split("\n")[i].contains(ConstantsValue.DEDUCIBLE)) {
                        modelo.setDeducible(newcontenido.split("\n")[i].split(ConstantsValue.DEDUCIBLE)[1]
                                .replace("###", "").replace("\r", ""));
                    }
                    if (newcontenido.split("\n")[i].contains("Coaseguro")) {
                        modelo.setCoaseguro(
                                newcontenido.split("\n")[i].split("Coaseguro")[1].replace("###", "").replace("\r", ""));
                    }
                }
            }

            // proceso Asegurados
            List<EstructuraAseguradosModel> asegurados = new ArrayList<>();
            inicio = contenido.indexOf("Relación###de###Asegurados");
            fin = contenido.indexOf("AXA###Seguros,###S.A.");
            if (modelo.getAsegurados().isEmpty()) {
            if (inicio > 0 && fin > 0 && inicio < fin) {
                newcontenido = contenido.substring(inicio, fin);
                for (int i = 0; i < newcontenido.split("\n").length; i++) {
                  
                    EstructuraAseguradosModel asegurado = new EstructuraAseguradosModel();
                    if (newcontenido.split("\n")[i].split("-").length > 5) {
                        String x = newcontenido.split("\n")[i].split("###")[0].replace("@@@", "").trim();
                        asegurado.setNombre(x.split(",")[1] + " " + x.split(",")[0]);
                        asegurado.setNacimiento(
                                fn.formatDateMonthCadena(newcontenido.split("\n")[i].split("###")[4]).trim());
                        asegurado.setAntiguedad(fn.formatDateMonthCadena(newcontenido.split("\n")[i].split("###")[5]));
                        asegurado.setParentesco(fn.parentesco(newcontenido.split("\n")[i].split("###")[3]));
                        asegurado.setSexo(fn.sexo(newcontenido.split("\n")[i].split("###")[3]).booleanValue() ? 1 : 0);
                        asegurados.add(asegurado);
                    }

                }
                modelo.setAsegurados(asegurados);
            }
        }

            if (modelo.getAsegurados().isEmpty()) {
                String nombre = "";
                String fechaN = "";
                String fechaA = "";

                inicio = inicontenido.indexOf("Relación de Asegurados");
                fin = inicontenido.indexOf("AXA Seguros, S.A. de C.V.");

                
                if (inicio > 0 && fin > 0 && inicio < fin) {
                    
                    newcontenido = inicontenido.substring(inicio, fin)
                            .replace("M   e  d  i a    Herm 1a4n/a0 6/2005", "###Media Hermana###14/06/2005###")
                            .replaceAll("  +", "###")                           
                            .replace("/", "-")
                            .replace("######", "###").replace("######", "###").replace("### ###", "###")
                            .replace("T   it u  l ar", "Titular").replace("T###i t u###l ar", "Titular")
                            .replace("T###it u###l ar", "Titular")
                            .replace("C   ó  n  y  uge", "Conyuge").replace("C###ó###n###y###u ###ge", "Conyuge")
                            .replace("C###ó###n###y###uge", "Conyuge")
                            .replace("H   i j o", "Hijo").replace("H###i j o", "Hijo").replace("H###i j a", "Hija")
                            .replace("R ###odriguez", "Rodriguez")
                            .replace("T###i tular", "Titular###")
                          
                            .replace("H###e###r m###ano", "Hermano")
                            .replace("H ###ijo", "###Hijo###")
                            .replace("N###i e###to#", "Nieto")
                            .replace(" ###", "")
                            .replace("###0###", "###0")
                            .replace("###-", "-");
                            System.out.println(newcontenido);
                    
                    //begin fix parrafo listado de asegurados
                    List<String> palabras = this.palabraInicioyFinMayuscula(newcontenido);
                    for(String x: palabras) {
                        newcontenido = newcontenido.replace(x, x.substring(0, (x.length()-1)) + "###" + x.substring((x.length()-1), x.length()));
                    }
   
                    palabras = this.aseguradosEdadDividida(newcontenido.replace("Ma. De", "Ma. De Las M"));
                   
                    for(String x:palabras) {
                        newcontenido = newcontenido.replace(x, x.substring(0, 5) + x.substring((x.length()-1), x.length()));
                    }
                    
                    newcontenido = newcontenido.replace("S###o###b###r ina", "Sobrina")
                    .replace("S###o###b###r ino#","Sobrino")
                    .replace("H###e###r m###ana", "Hermana")
                    .replace("50 Titular", "50###Titular");

                    //end fix parrafo listado de asegurados
                    for (int i = 0; i < newcontenido.split("\n").length; i++) {
                        EstructuraAseguradosModel asegurado = new EstructuraAseguradosModel();
                        int x = newcontenido.split("\n")[i].split("###").length;
                     
                        if (newcontenido.split("\n")[i].split("-").length > 5) {
                          
                           
                            if (x == 9) {
                                asegurado.setNombre(newcontenido.split("\n")[i].split("###")[0]);   
                                asegurado.setSexo(
                                        fn.sexo(newcontenido.split("\n")[i].split("###")[1]).booleanValue() ? 1 : 0);
                                asegurado.setEdad(fn.castInteger(newcontenido.split("\n")[i].split("###")[2]));
                                asegurado.setParentesco(fn.buscaParentesco(newcontenido.split("\n")[i]));                               
                                if(fn.obtenVigePoliza(newcontenido.split("\n")[i]).size() == 5){
                                    String vi= fn.obtenVigePoliza(newcontenido.split("\n")[i]).get(4);
                                
                                    asegurado.setNacimiento(fn.formatDateMonthCadena(fn.obtenVigePoliza(newcontenido.split("\n")[i]).get(0)));
                                    asegurado.setPrimaneta(fn.castBigDecimal(fn.castDouble(newcontenido.split("\n")[i].split("###")[7].split(vi)[1])));
                                }
                                asegurados.add(asegurado);
                            }
                            if (x == 11) {
                                if(newcontenido.split("\n")[i+1].length() > 5){
                                    nombre =(newcontenido.split("\n")[i].split("###")[0] +" " +newcontenido.split("\n")[i+1]).replace("@@@", "").replace("###", "").trim();
                              
                                }else{
                                    nombre = newcontenido.split("\n")[i].split("###")[0].replace("@@@", "").trim();
                                
                                }
                                if (nombre.contains(", ")) {
                                    asegurado.setNombre((nombre.split(",")[1]  +" "+ nombre.split(",")[0]).trim());
                                } else {
                                    asegurado.setNombre(nombre.trim());
                                }
                       
                            
                                asegurado.setSexo(
                                        fn.sexo(newcontenido.split("\n")[i].split("###")[1]).booleanValue() ? 1 : 0);
                                asegurado.setEdad(fn.castInteger(newcontenido.split("\n")[i].split("###")[2]));
                                asegurado.setParentesco(fn.buscaParentesco(newcontenido.split("\n")[i]));

                                fechaN = newcontenido.split("\n")[i].split("###")[3] + ""+ newcontenido.split("\n")[i].split("###")[4].replace(" ", "");
                                
                                
                                fechaN = (fechaN.length() > 10
                                        ? newcontenido.split("\n")[i].split("###")[4].replace(" ", "")
                                        : fechaN);

                                asegurado.setNacimiento(fn.formatDateMonthCadena(fechaN).trim());
                                asegurado.setAntiguedad(
                                        fn.formatDateMonthCadena(newcontenido.split("\n")[i].split("###")[5].replace(" ", "")));
                                asegurado.setFechaAlta(fn.formatDateMonthCadena(newcontenido.split("\n")[i].split("###")[8]));
                                
                                asegurado.setPrimaneta(fn.castBigDecimal(fn.castDouble(newcontenido.split("\n")[i].split("###")[9])));

                                asegurados.add(asegurado);
                            }

                            if (x == 12) {
                                if(newcontenido.split("\n")[i+1].length() > 5){
                                    nombre =(newcontenido.split("\n")[i].split("###")[0] +" " +newcontenido.split("\n")[i+1]).replace("@@@", "").replace("###", "").trim();
                              
                                }else{
                                    nombre = newcontenido.split("\n")[i].split("###")[0].replace("@@@", "").trim();
                                
                                }
                                if (nombre.contains(", ")) {
                                    asegurado.setNombre(((nombre.split(",")[1]  +" "+ nombre.split(",")[0]).trim()).replace("  ", " "));
                                } else {
                                    asegurado.setNombre(nombre.trim());
                                }
                       

                                fechaN = newcontenido.split("\n")[i].split("###")[4] + ""
                                        + newcontenido.split("\n")[i].split("###")[5].replace(" ", "");

                            

                                fechaN = (fechaN.length() > 10? newcontenido.split("\n")[i].split("###")[5].replace(" ", ""): fechaN);
                              
                                if(fechaN.length() >10) {
                                asegurado.setNacimiento(fn.formatDateMonthCadena(fechaN).trim());
                                }else {
                                    asegurado.setNacimiento(fn.formatDateMonthCadena(fn.obtenVigePoliza(fechaN).get(0)));
                                }
                                fechaA = newcontenido.split("\n")[i].split("###")[6].replace(" ", "");
                                if(fechaA.length() >10) {
                                    asegurado.setAntiguedad(fn.formatDateMonthCadena(fechaA));  
                                }else {
                                    asegurado.setAntiguedad(fn.formatDateMonthCadena(fn.obtenVigePoliza(fechaA).get(0)));
                                }
                                

                                asegurado.setParentesco(fn.buscaParentesco(newcontenido.split("\n")[i]));
                                asegurado.setSexo(
                                        fn.sexo(newcontenido.split("\n")[i].split("###")[1]).booleanValue() ? 1 : 0);
                                asegurados.add(asegurado);
                            }

                            if (x == 13) {
                               
                                nombre = newcontenido.split("\n")[i].split("###")[0].replace("@@@", "").trim();
                                if(nombre.contains(",") && nombre.split(",").length > 1) {
                        
                                    asegurado.setNombre(nombre.split(",")[1] + " "+ newcontenido.split("\n")[i + 1].split("###")[0] + " " + nombre.split(",")[0]);    
                                }else {
                                    if(nombre.contains(",")){
                                        asegurado.setNombre((newcontenido.split("\n")[i].split("###")[1] +" " + nombre.split(",")[0]).trim());
                                    }
                                
                                }
                                
                                
                                
                                fechaN = newcontenido.split("\n")[i].split("###")[5] + ""
                                        + newcontenido.split("\n")[i].split("###")[6].replace(" ", "");
                                if(fechaN.length() == 9 && fechaN.split("-")[0].length() == 1 ) {
                                    fechaN = newcontenido.split("\n")[i].split("###")[4] +
                                            newcontenido.split("\n")[i].split("###")[5] + ""
                                            + newcontenido.split("\n")[i].split("###")[6].replace(" ", "");
                                }
                                
                                asegurado.setNacimiento(fn.formatDateMonthCadena(fechaN).trim());
                                fechaA = newcontenido.split("\n")[i].split("###")[7].replace(" ", "");
                                asegurado.setAntiguedad(fn.formatDateMonthCadena(fechaA));
                                asegurado.setParentesco(fn.parentesco(newcontenido.split("\n")[i].split("###")[4]));
                                asegurado.setSexo(
                                        fn.sexo(newcontenido.split("\n")[i].split("###")[1]).booleanValue() ? 1 : 0);
                                asegurados.add(asegurado);
                            }
                            if (x == 14) {
                                nombre = newcontenido.split("\n")[i].split("###")[0].replace("@@@", "").trim();
                                if (nombre.contains(", ")) {
                                    asegurado.setNombre(nombre.split(",")[1] + " " + nombre.split(",")[0]);
                                } else {
                                    asegurado.setNombre(nombre);
                                }

                                fechaN = newcontenido.split("\n")[i].split("###")[5] + ""
                                        + newcontenido.split("\n")[i].split("###")[6] + ""
                                        + newcontenido.split("\n")[i].split("###")[7].replace(" ", "");
                                asegurado.setNacimiento(fn.formatDateMonthCadena(fechaN.replace("ta", "")).trim());
                                fechaA = newcontenido.split("\n")[i].split("###")[8].replace(" ", "").contains("-")
                                        ? newcontenido.split("\n")[i].split("###")[8].replace(" ", "")
                                        : newcontenido.split("\n")[i].split("###")[9];
                                asegurado.setAntiguedad(fn.formatDateMonthCadena(fechaA));

                                asegurado.setParentesco(fn.parentesco(newcontenido.split("\n")[i].split("###")[3]));
                                asegurado.setSexo(
                                        fn.sexo(newcontenido.split("\n")[i].split("###")[1]).booleanValue() ? 1 : 0);
                                asegurado.setEdad(fn.castInteger(newcontenido.split("\n")[i]
                                        .split(newcontenido.split("\n")[i].split("###")[1])[1]
                                                .split(newcontenido.split("\n")[i].split("###")[3])[0]
                                                        .replace("###", "").trim()));
                                asegurado.setFechaAlta(
                                        fn.formatDateMonthCadena(newcontenido.split("\n")[i].split("###")[10]));
                                asegurados.add(asegurado);
                            }

                            if (x == 15) {

                                nombre = newcontenido.split("\n")[i].replace("A ###", "A").split("###")[0]
                                        .replace("@@@", "").trim();
                                if (nombre.contains(", ")) {
                                    asegurado.setNombre((nombre.split(",")[1] + " " + nombre.split(",")[0]).trim());
                                } else {
                                    asegurado.setNombre(nombre);
                                }

                                fechaN = newcontenido.split("\n")[i].split("###")[5] + ""
                                        + newcontenido.split("\n")[i].split("###")[6] + ""
                                        + newcontenido.split("\n")[i].split("###")[7].replace(" ", "");  
                                
                                asegurado.setNacimiento(fn.formatDateMonthCadena(fechaN).trim());
                                fechaA = newcontenido.split("\n")[i].split("###")[8].replace(" ", "");
                                asegurado.setAntiguedad(fn.formatDateMonthCadena(fechaA));
                                asegurado.setParentesco(fn.parentesco(newcontenido.split("\n")[i].split("###")[4]));
                                if (newcontenido.split("\n")[i].split("###")[1].length() > 3) {
                                    asegurado.setSexo(
                                            fn.sexo(newcontenido.split("\n")[i].split("###")[2]).booleanValue() ? 1
                                                    : 0);
                                } else {
                                    asegurado.setSexo(
                                            fn.sexo(newcontenido.split("\n")[i].split("###")[1]).booleanValue() ? 1
                                                    : 0);
                                }

                                asegurado.setFechaAlta(
                                        fn.formatDateMonthCadena(newcontenido.split("\n")[i].split("###")[11]));

                                asegurado.setEdad(fn.castInteger(newcontenido.split("\n")[i]
                                        .split(newcontenido.split("\n")[i].split("###")[1])[1]
                                                .split(newcontenido.split("\n")[i].split("###")[4])[0]
                                                        .replace("###", "").trim()));

                                asegurados.add(asegurado);
                            }
                        }

                    }
                    modelo.setAsegurados(asegurados);
                }
            }
            
            if (modelo.getAsegurados().isEmpty()) {
                inicio = inicontenido.indexOf("Relación de Asegurados");
                fin = inicontenido.indexOf("AXA Seguros, S.A. de C.V.");
                String nombre = "";
                String fechaN = "";
                String fechaA = "";
                if (inicio > 0 && fin > 0 && inicio < fin) {    
                    
                 newcontenido =   fn.remplazarMultiple(inicontenido.substring(inicio, fin).replaceAll("  +", "###").replace("/", "-"),fn.remplazosGenerales())
                    .replace("T###i t u###l ar", "Titular")
                    .replace("###C###ó###n###y###u ###ge", "###Conyuge")
                    .replace("H###i j o", "Hijo")
                    .replace("R ###odriguez", "Rodriguez")
                    .replace("N###i e###to###0", "Nieto");

                    for (int i = 0; i < newcontenido.split("\n").length; i++) {
                        EstructuraAseguradosModel asegurado = new EstructuraAseguradosModel();
                        int x = newcontenido.split("\n")[i].split("###").length;
                      
                        
                        if (newcontenido.split("\n")[i].split("-").length > 5) {
                           
                            
                            if (x == 11) {
                                nombre = newcontenido.split("\n")[i].split("###")[0].replace("@@@", "").trim();
                                if (nombre.contains(", ")) {
                                    asegurado.setNombre(nombre.split(",")[1] + " " + nombre.split(",")[0]);
                                } else {
                                    asegurado.setNombre(nombre);
                                }
                                asegurado.setSexo(
                                        fn.sexo(newcontenido.split("\n")[i].split("###")[1]).booleanValue() ? 1 : 0);
                                asegurado.setEdad(fn.castInteger(newcontenido.split("\n")[i].split("###")[2]));
                                asegurado.setParentesco(fn.buscaParentesco(newcontenido.split("\n")[i]));

                                fechaN = newcontenido.split("\n")[i].split("###")[3] + ""
                                        + newcontenido.split("\n")[i].split("###")[4].replace(" ", "");
                                fechaN = (fechaN.length() > 10
                                        ? newcontenido.split("\n")[i].split("###")[4].replace(" ", "")
                                        : fechaN);

                                asegurado.setNacimiento(fn.formatDateMonthCadena(fechaN).trim());
                                asegurado.setAntiguedad(
                                        fn.formatDateMonthCadena(newcontenido.split("\n")[i].split("###")[5]));
                                asegurado.setFechaAlta( fn.formatDateMonthCadena(newcontenido.split("\n")[i].split("###")[newcontenido.split("\n")[i].split("###").length -3].trim()));
                                asegurado.setPrimaneta( fn.castBigDecimal(fn.castDouble(newcontenido.split("\n")[i].split("###")[newcontenido.split("\n")[i].split("###").length -2])));
                                asegurados.add(asegurado);
                            }

                            if (x == 12) {
                                nombre = newcontenido.split("\n")[i].split("###")[0].replace("@@@", "").trim();
                                if (nombre.contains(", ")) {
                                    asegurado.setNombre(nombre.split(",")[1] + " " + nombre.split(",")[0]);
                                } else {
                                    if(nombre.length()  < 20) {
                                        asegurado.setNombre((newcontenido.split("\n")[i].split("###")[0] +"" +newcontenido.split("\n")[i].split("###")[1]).replace("###", ""));
                                    }else {
                                        asegurado.setNombre(nombre);
                                    }
                                    
                                }

                                fechaN = newcontenido.split("\n")[i].split("###")[4] + ""
                                        + newcontenido.split("\n")[i].split("###")[5].replace(" ", "");

                                fechaN = (fechaN.length() > 10
                                        ? newcontenido.split("\n")[i].split("###")[5].replace(" ", "")
                                        : fechaN);
                                asegurado.setNacimiento(fn.formatDateMonthCadena(fechaN).trim());

                                fechaA = newcontenido.split("\n")[i].split("###")[6].replace(" ", "");
                                asegurado.setAntiguedad(fn.formatDateMonthCadena(fechaA));

                                asegurado.setParentesco(fn.buscaParentesco(newcontenido.split("\n")[i]));
                                asegurado.setSexo(
                                        fn.sexo(newcontenido.split("\n")[i].split("###")[1]).booleanValue() ? 1 : 0);
                                asegurado.setFechaAlta( fn.formatDateMonthCadena(newcontenido.split("\n")[i].split("###")[newcontenido.split("\n")[i].split("###").length -3].trim()));
                                asegurado.setPrimaneta( fn.castBigDecimal(fn.castDouble(newcontenido.split("\n")[i].split("###")[newcontenido.split("\n")[i].split("###").length -2])));
                                asegurados.add(asegurado);
                            }

                        }
                    }
                    modelo.setAsegurados(asegurados);
                }
                
            }

            // proceso de coberturas

            List<EstructuraCoberturasModel> coberturas = new ArrayList<>();

            inicio = contenido.indexOf("Coberturas-Servicios");
            fin = contenido.indexOf("Costo por Servicio");

            if (inicio > 0 && fin > 0 && inicio < fin) {

                
                newcontenido = contenido.substring(inicio, fin).replace("@@@", "").replace("\r", "")
                        .replace("######", "###").replace("###1 ###0 % ", "###10 %")
                        .replace("M ###aternidad", "Maternidad")
                        .replace("P ###reexistencias","Preexistencias")
                        .replace("M ###edicamentos fuera del hospital Básica", "Medicamentos fuera del hospital Básica")
                        .replace("Complicaciones de GMM no cubiertos De acuerdo a Condiciones Generales", "Complicaciones de GMM no cubiertos###De acuerdo a Condiciones Generales")
                        .replace("E ###nfermedades cubiertas", "Enfermedades cubiertas")
                        .replace("A ###tención en el Extranjero", "Atención en el Extranjero")
                        .replace("###D ###e Acuerdo a Condiciones", "###De Acuerdo a Condiciones")
                        .replace("M ###edicamentos fuera del hospital B ###ásica", "Medicamentos fuera del hospital###Básica")
                        .replace("C ###onversión Garantizada B ###ásica", "Conversión Garantizada Básica")
                        .replace("### ###","###");
                

                //fix coberturas
                if(!newcontenido.contains("Medicamentos fuera del hospital #")) {
                    newcontenido = newcontenido.replace("Medicamentos fuera del hospital ", "Medicamentos fuera del hospital###");
                }
                if(!newcontenido.contains("Complicaciones de GMM no cubiertos #")) {
                    newcontenido = newcontenido.replace("Complicaciones de GMM no cubiertos ", "Complicaciones de GMM no cubiertos###");
                }
                if(!newcontenido.contains("Deducible Cero por Accidente #")) {
                    newcontenido = newcontenido.replace("Deducible Cero por Accidente ", "Deducible Cero por Accidente###");
                }
                if(!newcontenido.contains("Cobertura Nacional #")) {
                    newcontenido = newcontenido.replace("Cobertura Nacional ", "Cobertura Nacional ###");
                }

                
                for (int i = 0; i < newcontenido.split("\n").length; i++) {
                    EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();
                    if (!newcontenido.split("\n")[i].contains("Coberturas-Servicios")
                            && !newcontenido.split("\n")[i].contains("Tope de Coaseguro")
                            && !newcontenido.split("\n")[i].contains("Tope de coaseguro")
                            && !newcontenido.split("\n")[i].contains("Coberturas adicionales con costo")
                            && !newcontenido.split("\n")[i].contains("Servicios con costo")
                            && !newcontenido.split("\n")[i].contains("Servicio###")
                            && !newcontenido.split("\n")[i].contains("###Suma")
                            && !newcontenido.split("\n")[i].contains("Tabulador Médico")) {

                        switch (newcontenido.split("\n")[i].split("###").length) {
                        case 3:
                        case 6:
                            cobertura.setNombre(newcontenido.split("\n")[i].split("###")[0].trim());
                            cobertura.setSa(newcontenido.split("\n")[i].split("###")[1].trim());
                            cobertura.setDeducible(newcontenido.split("\n")[i].split("###")[2].trim());
                            if(newcontenido.split("\n")[i].split("###").length  == 6) {
                                cobertura.setCoaseguro(newcontenido.split("\n")[i].split("###")[3].trim());
                            }
                            coberturas.add(cobertura);
                            break;
                        case 4:
                        case 5:
                            cobertura.setNombre(newcontenido.split("\n")[i].split("###")[0].trim());
                            cobertura.setSa(newcontenido.split("\n")[i].split("###")[1].trim());
                            cobertura.setDeducible(newcontenido.split("\n")[i].split("###")[2].trim());
                            cobertura
                                    .setCoaseguro(newcontenido.split("\n")[i].split("###")[3].replace("\r", "").trim());
                            coberturas.add(cobertura);
                            break;

                        default:
                            break;
                        }

                    }
                }
                modelo.setCoberturas(coberturas);
            }

            List<EstructuraRecibosModel> recibos = new ArrayList<>();
            EstructuraRecibosModel recibo = new EstructuraRecibosModel();
            if (modelo.getFormaPago() == 1) {

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
                recibo.setIva(modelo.getDerecho());

                recibo.setPrimaTotal(modelo.getPrimaTotal());
                recibo.setAjusteUno(modelo.getAjusteUno());
                recibo.setAjusteDos(modelo.getAjusteDos());
                recibo.setCargoExtra(modelo.getCargoExtra());
                recibos.add(recibo);
            }
            modelo.setRecibos(recibos);

            return modelo;
        } catch (Exception ex) {     
            ex.printStackTrace();
            modelo.setError(
                    AxaSaludV2Model.this.getClass().getTypeName() + " | " + ex.getMessage() + " | " + ex.getCause());
            return modelo;
        }
    }

    private List<String> palabraInicioyFinMayuscula(String parrafo) {
        //metodo para buscar palabras ejemplo
        //ArturoM### problema
        //Arturo##M### para posteriomente corregir y quedar
        List<String> palabras = new ArrayList<>();
        for(int i= 0; i < parrafo.split("\n").length; i++) {
            for(int j= 0; j < parrafo.split("\n")[i].split("###").length;j++) {
                for(int k= 0; k < parrafo.split("\n")[i].split("###")[j].split(" ").length;k++) {
                    if(parrafo.split("\n")[i].split("###")[j].split(" ")[k].matches("^[A-Z][a-z]+[A-Z]")) {
                        palabras.add(parrafo.split("\n")[i].split("###")[j].split(" ")[k]);
                    }
                }
            }
        }
        return palabras;
    }
    
    private List<String> aseguradosEdadDividida(String parrafo){
        //Metodo para buscar la edad separada por ### 
        //SEXO EDAD
        //F###3###0
        //para ajustarlos posteriormente a F###30 
        List<String> ajuntar = new ArrayList<>();
        String [] posibles = {"M###", "F###"};
        for(String posible : posibles) {
            for(String x : parrafo.split("\n")) {
             
                if( x.length() >15 && x.contains(posible)) {                   
                    String cadena = x.substring(x.indexOf(posible), (x.indexOf(posible)+9));                  
                    if(fn.isNumeric(cadena.substring(8)) && cadena.substring(4, 9).contains("###")) {
                        ajuntar.add(x.substring(x.indexOf(posible), (x.indexOf(posible)+9)));                       
                    }
                }
            }
        }
        return ajuntar;
    }
}
