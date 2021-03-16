package com.copsis.models;

import java.math.BigDecimal;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class DatosToolsModel {
	  public boolean isNumeric(String value) {// validacion de si es numero
	        try {
	            Double.parseDouble(value);
	            return true;
	        } catch (Exception ex) {
	            return false;
	        }
	    }

	    public String cleanString(String texto) {// limpiar de signos los datos antes de convertir a numeros
	        texto = texto.replace("(", "").replace(")", "").replace(",", "").replace("$", "").replace("MXP", "").replace("MXN", "").trim();
	        return texto;
	    }

	    public float floatPrimas(String texto) {
	        Float dato;
	        texto = texto.replace("(", "").replace(")", "").replace(",", "").replace("$", "").replace("MXP", "").replace("MXN", "").trim();
	        dato = Float.parseFloat(texto);
	        return dato;
	    }

	    public Double cleanStringv2(String texto) {// limpiar de signos los datos antes de convertir a numeros
	        //ELIMINAR ESTA FUNCION ,POR QUE YA NO SE UTILIZARA,YA QUE LOS DATOS VAN SER DE TIPO FLOAT
	        texto = texto.replace("(", "").replace(")", "").replace(",", "").replace("$", "").trim();
	        Double result = new Double(texto);

	        return result;
	    }

	    public float cStrF(String texto) {// limpiar de signos los datos antes de convertir a numeros
	        texto = texto.replace("(", "").replace(")", "").replace(",", "").replace("$", "").trim();
	        Float result = Float.parseFloat(texto);
	        return result;
	    }

	    public String elimina_spacios(String texto) {
	        String result = "";
	        int counter_space = 0;
	        for (int i = 0; i < texto.length(); i++) {
	            if (texto.charAt(i) == ' ') {
	                if (counter_space < 1) {
	                    counter_space = 1;
	                    if (result.length() > 0 || result.length() > 0 && i < texto.length()) {
	                        result += Character.toString(texto.charAt(i));
	                    }
	                }
	            } else {
	                counter_space = 0;
	                result += Character.toString(texto.charAt(i));
	            }
	        }
	        return result;
	    }

	    public String elimina_spacios2(String sTexto) {
	        String sCadenaSinBlancos = "";
	        for (int x = 0; x < sTexto.length(); x++) {
	            if (sTexto.charAt(x) != ' ') {
	                sCadenaSinBlancos += sTexto.charAt(x);
	            }

	        }

	        return sCadenaSinBlancos;
	    }

	    public String elimina_spacioCaracteres(String texto) {
	        String result = "";
	        int counter_space = 0;
	        for (int i = 0; i < texto.length(); i++) {
	            if (texto.charAt(i) == '_' || texto.charAt(i) == '_') {
	                if (counter_space < 1) {
	                    counter_space = 1;
	                    if (result.length() > 0 || result.length() > 0 && i < texto.length()) {
	                        result += Character.toString(texto.charAt(i));
	                    }
	                }
	            } else {
	                counter_space = 0;
	                result += Character.toString(texto.charAt(i));
	            }
	        }
	        return result;
	    }

	    public String gatos(String texto) {// QUITA ### AL INICIO Y FINAL
	        String newtexto = "";
	        int longText = 0;
	        texto = texto.trim();
	        longText = texto.length();

	        if (longText >= 3) {
	            if (texto.substring(longText - 3, longText).equals("###")) {
	                newtexto = texto.substring(0, longText - 3);
	            } else {
	                newtexto = texto;
	            }
	            longText = newtexto.length();
	            if (newtexto.length() >= 3) {
	                if (newtexto.substring(0, 3).equals("###")) {
	                    newtexto = newtexto.substring(3, longText);
	                }
	            }
	        } else {
	            newtexto = texto;
	        }
	        return newtexto;
	    }

	    public double firstNum(String texto) {// empieza a validar si es numero por index
	        double result = 0;
	        if (texto.contains(".")) {
	            try {
	                result = new Double(texto.substring(0, texto.indexOf(".") + 3));
	            } catch (Exception e) {

	            }
	        }
	        return result;
	    }

	    public String extraigoText(String texto) {
	        String newtext = "";
	        for (int i = 0; i < texto.length(); i++) {
	            try {
	                if (Double.parseDouble(Character.toString(texto.charAt(i))) >= 0) {
	                }
	            } catch (java.lang.NumberFormatException e) {
	                newtext += texto.charAt(i);
	            }
	        }
	        return newtext;
	    }

	    public int recorreContenido(String texto, String search) {
	        int result = 0;
	        for (int i = 1; i < texto.trim().split("@@@").length; i++) {
	            if (texto.trim().split("@@@")[i].contains(search)) {
	                result = i;
	                break;
	            }
	        }
	        return result;
	    }

	    public int searchTwoTexts(String texto, String textOne, String textTwo) {
	        int result = 0;
	        for (int i = 1; i < texto.trim().split("@@@").length; i++) {
	            if (texto.trim().split("@@@")[i].contains(textOne) && texto.trim().split("@@@")[i].contains(textTwo)) {
	                result = i;
	                break;
	            }
	        }
	        return result;
	    }

	    public String formatDate(String formatear) { // RECIBE FORMATO 02/02/2018 RETORNA 2018-02-02

	        String result = "";
	        String day = formatear.split("/")[0];
	        String month = formatear.split("/")[1];
	        String year = formatear.split("/")[2];
	        result = year + "-" + month + "-" + day;
	        return result;
	    }

	    public String formatDate_MonthCadena(String formatear) { // RECIBE 02/FEBRERO/2018 || 02/FEB/2018 || 02/Feb/2018												// RETORNA 2018-02-02
	        String result = "";
	        String day = "";
	        if (formatear.split("/")[0].length() == 1) {
	            day = "0" + formatear.split("/")[0];
	        } else {
	            day = formatear.split("/")[0];
	        }
	        String month = formatMonth(formatear.split("/")[1]);
	        String year = formatear.split("/")[2];
	        result = year + "-" + month + "-" + day;
	        return result;
	    }

	    public String formatDate_MonthCadena2(String formatear) {
	        {
	            String str_date = formatear.replaceAll("/", "-");
	            String resul = "";
	            String rtfecha = "";
	            String day = "";
	            try {
	                DateFormat formatter;
	                Date date;
	                formatter = new SimpleDateFormat("dd-MMM-yy");
	                date = (Date) formatter.parse(str_date);
	                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
	                resul = simpleDateFormat.format(date).toUpperCase();
	                return resul.toString();
	            } catch (Exception ex) {
	                return resul.toString();
	            }
	        }

	    }

	    public String formatMonth(String mes) { // RECIBE Ene || ENE || ENERO 02
	        String dato = "";
	        switch (mes) {
	            case "Ene":
	            case "ENE":
	            case "ENERO":
	            case "Enero":
	            case "enero":
	                dato = "01";
	                break;
	            case "Feb":
	            case "FEB":
	            case "FEBRERO":
	            case "Febrero":
	            case "febrero":
	                dato = "02";
	                break;
	            case "Mar":
	            case "MAR":
	            case "MARZO":
	            case "Marzo":
	            case "marzo":
	            case "mar":
	                dato = "03";
	                break;
	            case "Abr":
	            case "ABR":
	            case "ABRIL":
	            case "Abril":
	            case "abril":
	            case "abr":
	                dato = "04";
	                break;
	            case "May":
	            case "MAY":
	            case "MAYO":
	            case "Mayo":
	            case "mayo":
	                dato = "05";
	                break;
	            case "Jun":
	            case "JUN":
	            case "JUNIO":
	            case "Junio":
	            case "junio":
	            case "jun":
	                dato = "06";
	                break;
	            case "Jul":
	            case "JUL":
	            case "JULIO":
	            case "Julio":
	            case "julio":
	                dato = "07";
	                break;
	            case "Ago":
	            case "AGO":
	            case "AGOSTO":
	            case "Agosto":
	            case "agosto":
	                dato = "08";
	                break;
	            case "Sep":
	            case "SEP":
	            case "SEPTIEMBRE":
	            case "Septiembre":
	            case "septiembre":
	                dato = "09";
	                break;
	            case "Oct":
	            case "OCT":
	            case "OCTUBRE":
	            case "Octubre":
	            case "octubre":
	                dato = "10";
	                break;
	            case "Nov":
	            case "NOV":
	            case "NOVIEMBRE":
	            case "Noviembre":
	            case "noviembre":
	            case "nov":
	                dato = "11";
	                break;
	            case "Dic":
	            case "DIC":
	            case "DICIEMBRE":
	            case "Diciembre":
	            case "diciembre":
	                dato = "12";
	                break;
	        }
	        return dato;
	    }

	    public String cambio(String valor) {//borrar funcion al cambiar todo a moneda
	        String dato = "";
	        switch (valor) {
	            case "NACIONAL":
	            case "NAL.":
	            case "Nacional":
	            case "PESOS":
	            case "Pesos":
	            case "M.N.":
	            case "nacional":
	            case "PESOS CON REVALUACIÓN ANUAL":
	            case "PESOS SIN REVALUACIÓN":
	                dato = "Pesos";
	                break;
	            case "M.NAC":
	            case "Prima en moneda nacional":
	            case "MXP":
	                dato = "Pesos";
	                break;
	            case "DOLARES":
	            case "Dólar Americano":
	            case "Dolares":
	            case "Dólares":
	            case "DOLARES US":
	            case "USD":
	            case "DÓLARES SIN REVALUACIÓN":
	            case "U.S.DOLLAR":
	                dato = "Dólares";
	                break;
	            case "UDIS":
	                dato = "UDIS";
	                break;
	            case "UVACS":
	                dato = "UVACS";
	                break;
	            default:
	                dato = "Otro";
	                break;
	        }
	        return dato;
	    }

	    public int moneda(String texto) {
	        int moneda = 0;
	        switch (texto) {
	            case "NACIONAL":
	            case "NAL.":
	            case "Nacional":
	            case "PESOS":
	            case "Pesos":
	            case "Peso Mexicano":
	            case "M.N.":
	            case "nacional":
	            case "PESOS CON REVALUACIÓN ANUAL":
	            case "PESOS SIN REVALUACIÓN":
	            case "M.NAC":
	            case "Prima en moneda nacional":
	            case "MXP":
	            case "peso":
	                moneda = 1;
	                break;
	            case "DOLARES":
	            case "Dólar Americano":
	            case "Dolares":
	            case "Dólares":
	            case "DOLARES US":
	            case "USD":
	            case "DÓLARES SIN REVALUACIÓN":
	            case "U.S.DOLLAR":
	                moneda = 2;
	                break;
	            case "UDIS":
	            case "UDI":
	                moneda = 3;
	                break;
	            case "UVACS":
	                moneda = 4;
	                break;
	            default:
	                if (texto.length() == 0) {
	                    moneda = 5;
	                } else {
	                    moneda = 5;
	                }
	                break;
	        }

	        return moneda;
	    }

	    public String for_pago(String texto) { // FORMA DE PAGO
	        String dato = "";
	        switch (texto) {
	            case "Sema":
	            case "SEMA":
	            case "SEMANAL":
	            case "Semanal":
	                dato = "Semanal";
	                break;
	            case "Anua":
	            case "ANUA":
	            case "ANUAL":
	            case "Anual":
	            case "Única":
	            case "anual":
	            case "Anualmente":
	                dato = "Contado";
	                break;
	            case "Quin":
	            case "QUIN":
	            case "Quincenal":
	            case "QUICENAL":
	                dato = "Quincenal";
	                break;
	            case "Trim":
	            case "TRIM":
	            case "TRIMESTR":
	            case "TRIMESTRAL":
	            case "Trimestral":
	            case "TRIMESTRAL S/R":
	            case "TRIMESTRAL SR DERP":
	                dato = "Trimestral";
	                break;
	            case "Seme":
	            case "SEME":
	            case "Sem.":
	            case "SEM.":
	            case "Semestral":
	            case "SEMESTRAL":
	            case "SEMESTR":
	            case "SEMESTR S/R":
	                dato = "Semestral";
	                break;
	            case "Mens":
	            case "Mens.":
	            case "MEN.":
	            case "Mensual":
	            case "MENSUAL":
	            case "MENSUAL SIN RPF":
	            case "MENSUAL SR DERP":
	            case "MENSUAL SR":
	            case "mensual":
	            case "MENSUAL S/R":
	            case "MEN.DER.PRORRATEA":
	            case "MENSUAL VITRO":
	                dato = "Mensual";
	                break;
	            case "Contado":
	            case "cont":
	            case "CONT.":
	            case "CONTADO":
	            case "Prima Unica":
	            case "PAGO ÚNICO":
	            case "Pago Unico":
	            case "PAGO UNICO":
	                dato = "Contado";
	                break;
	        }
	        return dato;
	    }

	    public int fPago(String x) { // FORMA DE PAGO
	        int dato = 0;
	        switch (x) {
	            case "Contado":
	            case "cont":
	            case "CONT.":
	            case "CONTADO":
	            case "Prima Unica":
	            case "PAGO ÚNICO":
	            case "Pago Unico":
	            case "PAGO UNICO":
	            case "Anua":
	            case "ANUA":
	            case "ANUAL":
	            case "Anual":
	            case "Única":
	            case "anual":
	            case "Anualmente":
	            case "ANUAL EXTENDIDA":
	                dato = 1;
	                break;
	            case "Seme":
	            case "SEME":
	            case "Sem.":
	            case "SEM.":
	            case "Semestral":
	            case "SEMESTRAL":
	            case "SEMESTR":
	            case "SEMESTRAL S/R":

	                dato = 2;
	                break;
	            case "Trim":
	            case "TRIM":
	            case "TRIMESTR":
	            case "TRIMESTRAL":
	            case "Trimestral":
	            case "TRIMESTRAL S/R":
	            case "TRIMESTRAL SR DERP":
	            case "TRIMESTRAL 12 MESES":
	            case "TRIM.S/REC":
	                dato = 3;
	                break;
	            case "Mens":
	            case "Mens.":
	            case "MEN.":
	            case "Mensual":
	            case "MENSUAL":
	            case "MENSUAL SIN RPF":
	            case "MENSUAL SR DERP":
	            case "MENSUAL SR":
	            case "mensual":
	            case "MENSUAL S/R":
	            case "MENSUALS/R":
	            case "MEN.DER.PRORRATEA":
	            case "MENSUAL VITRO":
	            case "MENSUAL S/R DERP":
	            case "MEN.S/REC.":
	                dato = 4;
	                break;
	            case "Quin":
	            case "QUIN":
	            case "Quincenal":
	            case "QUICENAL":
	            case "QUINCENAL DXN VITRO":
	                dato = 5;
	                break;
	            case "Sema":
	            case "SEMA":
	            case "SEMANAL":
	            case "Semanal":
	            case "SEMANAL VITRO":
	                dato = 6;
	                break;
	        }
	        return dato;
	    }

	    public int numero_recibos(String texto) { //FUNCION SE VA A ELIMIONAR
	        int dato = 0;
	        switch (texto) {
	            // case "Sema": case "SEMA": case "SEMANAL": case "Semanal":
	            // dato =2;
	            // break;
	            case "Anua":
	            case "ANUA":
	            case "ANUAL":
	            case "Anual":
	            case "Única":
	            case "anual":
	            case "Anualmente":
	                dato = 1;
	                break;
	            // case "Quin": case "QUIN": case"Quincenal": case "QUICENAL":
	            // dato = 15;
	            // break;
	            case "Trim":
	            case "TRIM":
	            case "TRIMESTR":
	            case "TRIMESTRAL":
	            case "Trimestral":
	            case "TRIMESTRAL S/R":
	            case "TRIMESTRAL SR DERP":
	                dato = 4;
	                break;
	            case "Seme":
	            case "SEME":
	            case "Sem.":
	            case "SEM.":
	            case "Semestral":
	            case "SEMESTRAL":
	            case "SEMESTR":
	                dato = 2;
	                break;
	            case "Mens":
	            case "Mens.":
	            case "MEN.":
	            case "Mensual":
	            case "MENSUAL":
	            case "MENSUAL SIN RPF":
	            case "MENSUAL SR DERP":
	            case "MENSUAL SR":
	            case "mensual":
	            case "MENSUAL S/R":
	            case "MEN.DER.PRORRATEA":
	                dato = 12;
	                break;
	            case "Contado":
	            case "cont":
	            case "CONT.":
	            case "CONTADO":
	            case "Prima Unica":
	            case "PAGO ÚNICO":
	            case "Pago Unico":
	            case "PAGO UNICO":
	                dato = 1;
	                break;
	        }
	        return dato;
	    }

	    public int getTotalRec(int forma_pago) { //se le pasa como parametro jsonObject.getInt("forma_pago")
	        int result = 0;
	        switch (forma_pago) {
	            case 1:
	                result = 1;
	                break;
	            case 2:
	                result = 2;
	                break;
	            case 3:
	                result = 4;
	                break;
	            case 4:
	                result = 12;
	                break;
	            case 5:
	                result = 24; //REVISAR ESTE CASO
	                break;
	            case 6:
	                result = 52; //REVISAR ESTE CASO
	                break;
	        }
	        return result;
	    }

	    public String filtroPorRango(String texto, int rango) {
	        String textonuevo = "";
	        for (int i = 0; i < texto.split("@@@").length; i++) {
	            if (i < rango) {
	                if (i == 0) {
	                    textonuevo = texto.split("@@@")[i].trim();
	                } else {
	                    textonuevo += "\r\n@@@" + texto.split("@@@")[i].trim();
	                }
	            } else {
	                break;
	            }
	        }
	        return textonuevo;
	    }

	    public String formatoTexto(String contenido) { // REMPLAZA LOS DOBLES O MAS ESPACIOS JUNTOS ESPACIOS POR 1
	        String result = "";
	        int counter_space = 0;
	        for (int i = 0; i < contenido.length(); i++) {
	            if (contenido.charAt(i) == ' ') {
	                if (counter_space < 1) {
	                    counter_space = 1;
	                    if (result.length() > 0 || result.length() > 0 && i < contenido.length()) {
	                        result += Character.toString(contenido.charAt(i));
	                    }
	                }
	            } else {
	                counter_space = 0;
	                result += Character.toString(contenido.charAt(i));
	            }
	        }
	        return result;
	    }

	    public String sexo_format(String texto) {
	        String result = "";
	        switch (texto) {
	            case "MASCULINO":
	            case "Masculino":
	            case "HOMBRE":
	            case "M":
	            case "H":
	                result = "M";
	                break;
	            case "FEMENINO":
	            case "Femenino":
	            case "MUJER":
	            case "F":
	                result = "F";
	                break;
	        }
	        return result;
	    }

	    public int sexo(String texto) {
	        int result = 0;
	        switch (texto) {
	            case "masculino":
	            case "hombre":
	            case "m":
	            case "H":
	            case "masc.":
	            case "hijo":
	            case "mlo":
	            case "MASC.":
	                result = 1;
	                break;
	            case "femenino":
	            case "mujer":
	            case "f":
	            case "fem.":
	            case "hija":
	            case "FEM.":
	                result = 0;
	                break;
	            default:
	                result = 1;
	                break;
	        }
	        return result;
	    }

	    public int parent(String parentesco) {
	        int dato = 1;
	        switch (parentesco) {
	            case "tit.":
	            case "tit":
	            case "titular":
	            case "asegurado principal":
	                dato = 1;
	                break;
	            case "cony.":
	            case "conyuge":
	            case "cónyuge":
	                dato = 2;
	                break;
	            case "hijo":
	            case "hijo a":
	            case "hija":
	            case "hijo/a":
	                dato = 3;
	                break;
	            case "padre":
	            case "nieto":
	            case "madre":
	            case "esposa":
	            case "esposo":
	            case "asegurado":
	            case "dependientes":
	            case "otro":
	            case "hermano/a":
	            case "parentescos":
	            case "perteneciente":
	            case "hermana":
	            case "hermano":
	            case "suegra":
	            case "abuelo":
	                dato = 4;
	                break;
	            default:
	                dato = 1;
	                break;
	        }
	        return dato;
	    }

	    public String RemplazaGrupoSpace(String dato) { // RETORNA UNA CADENA, EN DONDE TENGA MAS DE 2 ESPACIOS PONE ###
	        boolean encontro_grupo = false;
	        int par = 0;
	        String newdato = "";
	        for (int i = 0; i < dato.length(); i++) {
	            if (dato.charAt(i) == ' ') {
	                if (encontro_grupo == false) {
	                    par = par + 1;
	                    if (par == 2) {
	                        encontro_grupo = true;
	                        newdato = newdato.trim();
	                        newdato += "###";
	                    } else {
	                        newdato += Character.toString(dato.charAt(i));
	                    }
	                }
	            } else {
	                par = 0;
	                encontro_grupo = false;
	                newdato += Character.toString(dato.charAt(i));
	            }
	        }
	        return newdato;
	    }

	    public int equals(String contenido, String texto) {
	        int aqui = 0;
	        for (int i = 0; i < contenido.trim().split("@@@").length; i++) {
	            if (contenido.trim().split("@@@")[i].trim().equals(texto)) {
	                aqui = i;
	                break;
	            }
	        }
	        return aqui;
	    }

	    public String fixContenido(String contenido) {
	        String cont_Fix = contenido.replace("\n", "\r\n");
	        String texto = "";
	        if (cont_Fix.contains("\r\r\n")) {
	            texto = cont_Fix.replace("\r\r\n", "\r\n");
	        } else {
	            texto = contenido.replace("\n", "\r\n");
	        }
	        return texto;
	    }

	    public Double fDecimal(Double texto) {
	        Double m;
	        String d = String.valueOf(texto).trim();
	        String str = d.replace(".", "###");
	        if (str.split("###")[1].length() > 1) {
	            m = Double.parseDouble(str.split("###")[0] + "." + str.split("###")[1].substring(0, 2));
	        } else {
	            m = Double.parseDouble(str.split("###")[0] + "." + str.split("###")[1].substring(0, 1));
	        }
	        return m;
	    }

	    public int material(String material) {
	        int result = 0;
	        switch (material) {
	            case "BLOCK":
	            case "BLOCK DE CONCRETO":
	            case "MACIZOS":
	            case "CONSTRUCCIÓN MACIZA":
	            case "MAZISOS(PRECOLADOS, CONCRETO, ARMADO)":
	            case "MACIZOS (PRECOLADOS, CONCRETO ARMADO ETC.)":
	            case "MACIZOS (PRECOLADOS, CONCRETO CARTERA MANUAL ARMADO ETC.)":
	            case "TECHOS DE CONCRETO AL 100%":
	            case "MAZISOS":
	            case "DE CONCRETO":
	            case "CONCRETO":
	            case "Construcción Maciza":
	                result = 1;
	                break;
	            case "CONCRETO ARMADO":
	            case "LAMINAS METALICAS":
	            case "LAMINA METALICA":
	            case "LAM.METAL ASBESTO O TEJA":
	            case "LAMINA DE ASBESTO, METALICA, ETC.":
	            case "LAMINA METALICA SOBRE EST. METALICA":
	                result = 2;
	                break;
	            case "LADRILLO":
	            case "OTRO":
	                result = 3;
	                break;
	            case "TABIQUE, TABICON, PIEDRA":
	            case "TABIQUE":
	            case "TABIQUE DE CARGA":
	                result = 4;
	                break;
	        }
	        return result;
	    }

	    public BigDecimal decimalS(String texto) {
	        BigDecimal formateado = new BigDecimal(texto);
	        return formateado;
	    }

	    public BigDecimal decimalN(double numero) {
	        BigDecimal formateado = new BigDecimal(numero);
	        return formateado;
	    }

	    public BigDecimal decimalF(float numero) {
	        BigDecimal formateado = new BigDecimal(numero);
	        return formateado;
	    }

	    public String dateAdd(String fecha, int cuantos, int caso) {
	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	        LocalDate date = LocalDate.parse(fecha);
	        switch (caso) {
	            case 1://DIAS
	                LocalDate dateNew = date.plusDays(cuantos);
	                fecha = dateNew.format(formatter);
	                break;
	            case 2://MESES
	                LocalDate dateNew1 = date.plusMonths(cuantos);
	                fecha = dateNew1.format(formatter);
	                break;
	        }
	        return fecha;
	    }

	    public int monthAdd(int forma_pago) {
	        int meses = 0;
	        switch (forma_pago) {
	            case 2:
	                meses = 6;
	                break;
	            case 3:
	                meses = 3;
	                break;
	            case 4:
	                meses = 1;
	                break;
	            case 5:
	            case 6:
	                meses = 0;
	                break;
	        }
	        return meses;
	    }

	    /*
		public Date dateAdd(String sdate) throws ParseException {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Date fecha = simpleDateFormat.parse(sdate);
			return fecha;
		}*/
	    public boolean isDate(String date) {
	        String date1 = "^(0?[1-9]|[12][0-9]|3[01])/(0?[1-9]|1[012])/([12][0-9]{3})$";
	        return date.matches(date1);
	    }

	    public static String eliminaHtmlTags2(String value) {
	        value = value.replaceAll("(\\r\\n|\\n|\\n\\n)", "")
	                .replaceAll("\n\n|\n", "")
	                .replaceAll("&quot;", "")
	                .replaceAll("\t|\\t", " ")
	                .replaceAll("\u200B", "")
	                .replaceAll("0x002D", "")
	                .replaceAll("\u2010", "")
	                .replaceAll("<div>", "")
	                .replaceAll("</div>", "")
	                .replaceAll("\u0009", " ");
	        String pattern = "<([a-z]+)([^<]*)>|</([a-z]+)>";
	        Pattern r = Pattern.compile(pattern);
	        Matcher m = r.matcher(value);

	        return value.replaceAll("&nbsp;", " ");
	    }

	    public Double numeros(String texto) {
	        Double resultado = null;
	        try {
	            texto = texto.replace("(", "").replace(")", "").replace(",", "").replace("$", "").replace(" ", "")
	                    .replace("###", "").trim();

	            if (texto.contains("-")) {
	                resultado = -1 * Double.parseDouble(texto.replaceAll("-", ""));
	            } else {
	                resultado = Double.parseDouble(texto);
	            }

	            return resultado;
	        } catch (Exception ex) {
	            return resultado;
	        }

	    }

	    public Float numerosFl(String texto) {
	        Float resultado = null;
	        try {
	            texto = texto.replace("(", "").replace(")", "").replace(",", "").replace("$", "").replace(" ", "")
	                    .replace("###", "").trim();

	            if (texto.contains("-")) {
	                resultado = -1 * Float.parseFloat(texto.replaceAll("-", ""));
	            } else {
	                resultado = Float.parseFloat(texto);
	            }

	            return resultado;
	        } catch (Exception ex) {
	            return resultado;
	        }

	    }

}
