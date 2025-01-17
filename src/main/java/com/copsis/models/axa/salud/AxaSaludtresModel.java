package com.copsis.models.axa.salud;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.copsis.constants.ConstantsValue;
import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraAseguradosModel;
import com.copsis.models.EstructuraCoberturasModel;
import com.copsis.models.EstructuraJsonModel;

public class AxaSaludtresModel {
    private DataToolsModel fn = new DataToolsModel();
    private EstructuraJsonModel modelo = new EstructuraJsonModel();

    public EstructuraJsonModel procesar(String contenido) {
        int inicio = 0;
        int fin = 0;
        StringBuilder newcontenido = new StringBuilder();
        contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());
        try {

            modelo.setTipo(3);
            modelo.setCia(20);

            inicio = contenido.indexOf("Datos del contratante");
            fin = contenido.indexOf("Agente:");
            newcontenido.append(fn.extracted(inicio, fin, contenido));

            for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {

                if (newcontenido.toString().split("\n")[i].contains("contratante")
                        && newcontenido.toString().split("\n")[i].contains("Póliza")
                        && newcontenido.toString().split("\n")[i + 1].contains("Nombre:")) {
                    modelo.setCteNombre(newcontenido.toString().split("\n")[i + 1].split("###")[1]);
                    modelo.setPoliza(newcontenido.toString().split("\n")[i + 1].split("###")[2]);
                }
                if (newcontenido.toString().split("\n")[i].contains("Domicilio:")
                        && newcontenido.toString().split("\n")[i].contains("Tipo de plan")) {
                    modelo.setCteDireccion(
                            newcontenido.toString().split("\n")[i].split("Domicilio:")[1].split("Tipo")[0]
                                    .replace("###", "").trim());
                    modelo.setPlan(newcontenido.toString().split("\n")[i + 1].split("###")[0]);
                }
                if (newcontenido.toString().split("\n")[i].contains("inicio de vigencia")) {
                    List<String> valores = fn.obtenVigePoliza(newcontenido.toString().split("\n")[i]);
                    modelo.setVigenciaDe(fn.formatDateMonthCadena(valores.get(0)));

                }
                if (newcontenido.toString().split("\n")[i].contains("fin de vigencia")) {
                    List<String> valores = fn.obtenVigePoliza(newcontenido.toString().split("\n")[i]);
                    modelo.setVigenciaA(fn.formatDateMonthCadena(valores.get(0)));
                }
                if (newcontenido.toString().split("\n")[i].contains("Fecha de emisión")) {
                    List<String> valores = fn.obtenVigePoliza(newcontenido.toString().split("\n")[i]);
                    modelo.setFechaEmision(fn.formatDateMonthCadena(valores.get(0)));
                }
                if (newcontenido.toString().split("\n")[i].contains("Frecuencia de pago")) {
                    modelo.setFormaPago(fn.formaPagoSring(newcontenido.toString().split("\n")[i]));
                }
               
                if (newcontenido.toString().split("\n")[i].contains("R.F.C:")
                        && newcontenido.toString().split("\n")[i].contains(ConstantsValue.TELEFONO)) {
                    modelo.setRfc((newcontenido.toString().split("\n")[i].split("R.F.C:")[1].split(ConstantsValue.TELEFONO)[0]
                            .replace("###", "")).replace("\u00A0", "").trim());
                           
                }
                if (newcontenido.toString().split("\n")[i].contains("C.P.")){
                    List<String> valores = fn.obtenerListNumeros2(newcontenido.toString().split("\n")[i]);
                        if(!valores.isEmpty()){
                            modelo.setCp(valores.stream()
                                .filter(numero -> String.valueOf(numero).length() >= 4)
                                .collect(Collectors.toList()).get(0));
                        }
                }

            }
            inicio = contenido.indexOf("Condiciones Contratadas");
            fin = contenido.indexOf("Coberturas-Servicios ");
            boolean coaseguro =true;
            if (inicio > 0 && fin > 0 && inicio < fin) {
                newcontenido = new StringBuilder();
                newcontenido.append(fn.extracted(inicio, fin+100, contenido));
                for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
                    if (newcontenido.toString().split("\n")[i].contains("SumaAsegurada")) {
                        modelo.setSa(newcontenido.toString().split("\n")[i].split("SumaAsegurada")[1].replace("###", "")
                                .replace("\r", ""));
                    }
                    if (newcontenido.toString().split("\n")[i].contains(ConstantsValue.DEDUCIBLE)) {
                        modelo.setDeducible(newcontenido.toString().split("\n")[i].split(ConstantsValue.DEDUCIBLE)[1]
                                .replace("###", "").replace("\r", "").trim());
                    }
                    if (coaseguro &&  newcontenido.toString().split("\n")[i].contains(ConstantsValue.COASASEGURO)) {
                        modelo.setCoaseguro(
                                newcontenido.toString().split("\n")[i].split(ConstantsValue.COASASEGURO)[1].replace("###", "")
                                        .replace("\r", ""));
                            coaseguro=false;            
                    }
                    if (newcontenido.toString().split("\n")[i].contains("Tope de Coaseguro")) {                        
                        modelo.setCoaseguroTope(
                                (newcontenido.toString().split("\n")[i].split(ConstantsValue.COASASEGURO)[1].replace("###", "")
                                        .replace("\r", "")).trim());
                    }

                   
                }
            }

            inicio = contenido.indexOf("Descuento familiar");
            fin = contenido.indexOf("Prima anual");
            if (fin != -1) {
                fin = fin + 100;
            }
            newcontenido = new StringBuilder();
            newcontenido.append(fn.extracted(inicio, fin, contenido));

            for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {

                if (newcontenido.toString().split("\n")[i].contains("Prima Neta")) {
                    List<String> valores = fn
                            .obtenerListNumeros2(newcontenido.toString().split("\n")[i].replace(",", ""));
                    if (!valores.isEmpty()) {
                        modelo.setPrimaneta(fn.castBigDecimal(fn.castDouble(valores.get(0))));
                    }

                }
                if (newcontenido.toString().split("\n")[i].contains("Recargo")) {
                    List<String> valores = fn
                            .obtenerListNumeros2(newcontenido.toString().split("\n")[i].replace(",", ""));
                    if (!valores.isEmpty()) {
                        modelo.setRecargo(fn.castBigDecimal(fn.castDouble(valores.get(0))));
                    }

                }
                if (newcontenido.toString().split("\n")[i].contains("Derecho")) {
                    List<String> valores = fn
                            .obtenerListNumeros2(newcontenido.toString().split("\n")[i].replace(",", ""));
                    if (!valores.isEmpty()) {
                        modelo.setDerecho(fn.castBigDecimal(fn.castDouble(valores.get(0))));
                    }

                }
                if (newcontenido.toString().split("\n")[i].contains("I.V.A.")) {
                    List<String> valores = fn
                            .obtenerListNumeros2(newcontenido.toString().split("\n")[i].replace(",", ""));
                    if (!valores.isEmpty()) {
                        modelo.setIva(fn.castBigDecimal(fn.castDouble(valores.get(0))));
                    }

                }
                if (newcontenido.toString().split("\n")[i].contains("Prima anual")) {
                    List<String> valores = fn
                            .obtenerListNumeros2(newcontenido.toString().split("\n")[i].replace(",", ""));
                    if (!valores.isEmpty()) {
                        modelo.setPrimaTotal(fn.castBigDecimal(fn.castDouble(valores.get(0))));
                    }

                }
            }

            inicio = contenido.indexOf("Relación de Asegurados");
            fin = contenido.indexOf("AXA Seguros, S.A. de C.V.");

            List<EstructuraAseguradosModel> asegurados = new ArrayList<>();

            newcontenido = new StringBuilder();
            newcontenido.append(fn.extracted(inicio, fin, contenido)
                    .replace("H i j o", "###Hijo###")
                    .replace("H i j a ", "###Hija##")
                    .replace(" T it u l ar", "###Titular###")
                    .replace("C ó n y uge ", "###Cónyuge###")
                    .replace("### ###Hija", "###Hija")
                    .replace("###M", "###M###")
                    .replace("###F", "###F###")
                    .replace("0 ###1", "###01")
                    .replace("1 2 ###-", "12-")
                    .replace("2 6 -", "26-")
                    .replace("2 5 -", "25-")
                    .replace("0 2 -", "02-")
                    .replace("0 3 -", "03-")
                    .replace("0 3-", "03-")
                    .replace("2 5-", "25-")
                    .replace("2 1-", "21-")
                    .replace("1 8-", "18-")                    
                    .replace("0 ###9-01", "###0 ###9-01")
                    .replace("0 ###9", " 0 9")
                    .replace("26- 08-", "26-08-")
                    .replace("1 3-01-", "###13-01-")
                    .replace("1 ###3-01-", "###13-01-")
                    .replace("3 1-03-2015", "###31-03-2015")
                    .replace("3 ###1-03-", "###31-03-")
                    .replace("1 1 ###-01-1997", "11-01-1997###")
                    .replace("04-2020", "04-2020###")
                    .replace("1 ###3-04-2022", "13-04-2022###")
                    .replace("0 8-06-2018", "###08-06-2018")
                    .replace("2 1 ###-03", "21-03")
                    .replace("L ###ozano", "Lozano")
                    .replace("S ###anchez", "Sanchez")
                    .replace("C ###antu Rivera", "Cantu Rivera")
                    .replace("######Titular###", "###Titular###")
                    .replace("2022 13-04", "2022###13-04")
                    .replace("0 4 ###-08-1984", "04-08-1984")
                    .replace("1 0-10-2019", "###10-10-2019")  
                    .replace(" 09-01-2024 09-01-2024", "###09-01-2024###09-01-2024")
                    .replace("Hija##1 8 -11-2021", "Hija###18 -11-2021###")

                    );
         

            if (modelo.getAsegurados().isEmpty() && (inicio > 0 && fin > 0 && inicio < fin)) {

                for (int i = 0; i < newcontenido.toString().split("\n").length; i++) { 
                                  
                    EstructuraAseguradosModel asegurado = new EstructuraAseguradosModel();
                    if (newcontenido.toString().split("\n")[i].split("-").length > 5) {
                     
                        String x = newcontenido.toString().split("\n")[i].split("###")[0].replace("@@@", "").trim();
                        if(x.contains(",")){
                            asegurado.setNombre((x.split(",")[1] + " " + x.split(",")[0]).trim());
                        }                    
                        List<String> valores = fn.obtenVigePoliza(newcontenido.toString().split("\n")[i]); 
                                        
                        if (!valores.isEmpty() && valores.size() == 5) {

                            asegurado.setNacimiento(
                                    fn.formatDateMonthCadena(valores.get(0)));
                            asegurado.setAntiguedad(
                                    fn.formatDateMonthCadena(valores.get(1)));
                                    asegurado.setFechaAlta(
                                        fn.formatDateMonthCadena(valores.get(4)));
                        }
                        if (!valores.isEmpty() && valores.size() == 4) {

                            asegurado.setNacimiento(
                                    fn.formatDateMonthCadena(valores.get(0)));
                            asegurado.setAntiguedad(
                                    fn.formatDateMonthCadena(valores.get(1)));
                                    asegurado.setFechaAlta(
                                        fn.formatDateMonthCadena(valores.get(3)));
                        }
                        if (!valores.isEmpty() && valores.size() == 3) {

                            asegurado.setNacimiento(
                                    fn.formatDateMonthCadena(valores.get(0)));
                            asegurado.setAntiguedad(
                                    fn.formatDateMonthCadena(valores.get(1)));
                                    
                        }

                      
                        asegurado.setParentesco(fn.parentesco(newcontenido.toString().split("\n")[i].split("###")[3]));
                        asegurado.setSexo(fn.sexo(newcontenido.toString().split("\n")[i].split("###")[1]).booleanValue() ? 1 : 0);
                        asegurados.add(asegurado);
                    }

                }
                modelo.setAsegurados(asegurados);

            }
        

            List<EstructuraCoberturasModel> coberturas = new ArrayList<>();

            inicio = contenido.indexOf("Coberturas-Servicios");
            fin = contenido.indexOf("Costo por Servicio");

            if (inicio > 0 && fin > 0 && inicio < fin) {

                newcontenido = new StringBuilder();
                newcontenido.append(fn.extracted(inicio, fin, contenido).replace("@@@", "").replace("\r", "")
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
                        .replace("P ###rotección Dental", "Protección Dental")
                        .replace("C ###obertura Nacional", "Cobertura Nacional")
                        .replace("###C ###osto Preferencial ", "###Costo Preferencial")
                        .replace("Tu Médico 24 Hrs N-A", "Tu Médico 24 Hrs###N-A")
                        .replace("Beneficio de Atn Médica N-A ", "Beneficio de Atn Médica###N-A")
                        .replace("D ###educible Cero por Accidente N ###o Aplica", 
                        "Deducible Cero por Accidente###No Aplica")
                        .replace("Complicaciones de GMM no cubiertos D ###e acuerdo", "Complicaciones de GMM no cubiertos###De acuerdo")
                        .replace("###N ###-A", "###N-A")
                        .replace("### ###","###"));
                

                   modelo.setMoneda(1);

                for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
                   
                    EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();
                    if (!newcontenido.toString().split("\n")[i].contains("Coberturas-Servicios")
                            && !newcontenido.toString().split("\n")[i].contains("Tope de Coaseguro")
                            && !newcontenido.toString().split("\n")[i].contains("Tope de coaseguro")
                            && !newcontenido.toString().split("\n")[i].contains("Coberturas adicionales con costo")
                            && !newcontenido.toString().split("\n")[i].contains("Servicios con costo")
                            && !newcontenido.toString().split("\n")[i].contains("Servicio###")
                            && !newcontenido.toString().split("\n")[i].contains("###Suma")
                            && !newcontenido.toString().split("\n")[i].contains("Tabulador Médico")) {

                        switch (newcontenido.toString().split("\n")[i].split("###").length) {
                        case 3:
                        case 6:
                            cobertura.setNombre(newcontenido.toString().split("\n")[i].split("###")[0].trim());
                            cobertura.setSa(newcontenido.toString().split("\n")[i].split("###")[1].trim());
                            cobertura.setDeducible(newcontenido.toString().split("\n")[i].split("###")[2].trim());
                            if(newcontenido.toString().split("\n")[i].split("###").length  == 6) {
                                cobertura.setCoaseguro(newcontenido.toString().split("\n")[i].split("###")[3].trim());
                            }
                            coberturas.add(cobertura);
                            break;
                        case 4:
                        case 5:
                            cobertura.setNombre(newcontenido.toString().split("\n")[i].split("###")[0].trim());
                            cobertura.setSa(newcontenido.toString().split("\n")[i].split("###")[1].trim());
                            cobertura.setDeducible(newcontenido.toString().split("\n")[i].split("###")[2].trim());
                            cobertura
                                    .setCoaseguro(newcontenido.toString().split("\n")[i].split("###")[3].replace("\r", "").trim());
                            coberturas.add(cobertura);
                            break;

                        default:
                            break;
                        }

                    }
                }
                modelo.setCoberturas(coberturas);
            }

            return modelo;
        } catch (Exception ex) {
            modelo.setError(

                    AxaSaludtresModel.this.getClass().getTypeName() + " | " + ex.getMessage() + " | " + ex.getCause());
        
            return modelo;
        }
    }
}
