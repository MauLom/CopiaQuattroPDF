package com.copsis.models;

import java.math.BigDecimal;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;




public class DataToolsModel {


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
	    
	    
	    
	    public Double castDouble(String texto) {
	        Double resultado = null;
	        try {
	            texto = texto.replace("(", "").replace(")", "").replace(",", "").replace("$", "").replace(" ", "")
	                    .replace("###", "").trim();

	            if (texto.contains("-")) {
	                resultado = -1 * Double.parseDouble(texto.replace("-", ""));
	            } else {
	                resultado = Double.parseDouble(texto);
	            }

	            return resultado;
	        } catch (Exception ex) {
	            return resultado;
	        }

	    }

	    public Float castFloat(String texto) {
	        Float resultado = null;
	        try {
	            texto = texto.replace("(", "").replace(")", "").replace(",", "").replace("$", "").replace(" ", "")
	                    .replace("###", "").trim();

	            if (texto.contains("-")) {
	                resultado = -1 * Float.parseFloat(texto.replace("-", ""));
	            } else {
	                resultado = Float.parseFloat(texto);
	            }

	            return resultado;
	        } catch (Exception ex) {
	            return resultado;
	        }

	    }
	    
		public BigDecimal castBigDecimal(String valor) {
			BigDecimal resulDecimal = null;
			valor = valor.replace("(", "").replace(")", "").replace(",", "").replace("$", "").replace(" ", "")
					.replace("###", "").trim();
			
				resulDecimal = new BigDecimal(valor);

			return resulDecimal;
		}


	  

	    public String eliminaSpacios(String texto) {
	        String result = "";
	        int counterspace = 0;
	        for (int i = 0; i < texto.length(); i++) {
	            if (texto.charAt(i) == ' ') {
	                if (counterspace < 1) {
	                	counterspace = 1;
	                    if (result.length() > 0 ) {
	                        result += Character.toString(texto.charAt(i));
	                    }
	                }
	            } else {
	            	counterspace = 0;
	                result += Character.toString(texto.charAt(i));
	            }
	        }
	        return result;
	    }

	   
	    public String eliminaSpacioCaracteres(String texto) {
	        String result = "";
	        int counterspace = 0;
	        for (int i = 0; i < texto.length(); i++) {
	            if (texto.charAt(i) == '_' ) {
	                if (counterspace < 1) {
	                	counterspace = 1;
	                    if (result.length() > 0 || result.length() > 0 && i < texto.length()) {
	                        result += Character.toString(texto.charAt(i));
	                    }
	                }
	            } else {
	            	counterspace = 0;
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
	    
		public String formatDate(String fecha, String format) { // RECIBE FORMATO 02/02/2018 RETORNA 2018-02-02

			String resul = "";
			try {
				;
				if(fecha.split("/")[1].length() > 2) {
					fecha = fecha.split("/")[0] +"/" +formatMonth(fecha.split("/")[1])  +"/"+ fecha.split("/")[2];
				}
				
				DateFormat formatter;
				Date date;
				formatter = new SimpleDateFormat(format);
				date = (Date) formatter.parse(fecha.replaceAll("/", "-"));
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
				resul = simpleDateFormat.format(date).toUpperCase();
	
				return resul;
			} catch (Exception ex) {
			       resul = ex.getMessage();
				return resul;

			}

		}
	    

	  
	    public String formatMonth(String mes) { // RECIBE Ene || ENE || ENERO 02
	        String dato = "";
	        switch (mes.toUpperCase()) {
	            case "ENE":
	            case "ENERO":
	                dato = "01";
	                break;
	            case "FEB":
	            case "FEBRERO":
	                dato = "02";
	                break;
	            case "MAR":
	            case "MARZO":
	                dato = "03";
	                break;
	            case "ABR":
	            case "ABRIL":
	                dato = "04";
	                break;
	            case "MAY":
	            case "MAYO":
	                dato = "05";
	                break;
	            case "JUN":
	            case "JUNIO":
	                dato = "06";
	                break;
	            case "JUL":
	            case "JULIO":
	                dato = "07";
	                break;
	            case "AGO":
	            case "AGOSTO":
	                dato = "08";
	                break;
	            case "SEP":
	            case "SEPTIEMBRE":
	                dato = "09";
	                break;
	            case "OCT":
	            case "OCTUBRE":
	                dato = "10";
	                break;
	            case "NOV":
	            case "NOVIEMBRE":
	                dato = "11";
	                break;
	            case "DIC":
	            case "DICIEMBRE":
	                dato = "12";
	                break;
	            default:
	                dato = "0";
	                break;
	        }
	        return dato;
	    }

	
	    public int moneda(String texto) {
	        int moneda = 0;
	        switch (texto.toUpperCase()) {
	            case "NACIONAL":
	            case "NAL.":
	            case "PESOS":
	            case "PESO MEXICANO":
	            case "M.N.":
	            case "PESOS CON REVALUACIÓN ANUAL":
	            case "PESOS SIN REVALUACIÓN":
	            case "M.NAC":
	            case "PRIMA EN MONEDA NACIONAL":
	            case "MXP":
	            case "PESO":
	                moneda = 1;
	                break;
	            case "DÓLARES":
	            case "DÓLAR AMERICANO":
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
	                    moneda = 5;	                
	                break;
	                
	        }

	        return moneda;
	    }

	

	    public int formaPago(String x) { // FORMA DE PAGO
	        int dato = 0;
	        switch (x.toUpperCase()) {
	            case "CONT":
	            case "CONTADO":
	            case "PRIMA UNICA":
	            case "PAGO ÚNICO":
	            case "PAGO UNICO":
	            case "ANUA":
	            case "ANUAL":
	            case "ÚNICA":
	            case "ANUALMENTE":
	            case "ANUAL EXTENDIDA":
	                dato = 1;
	                break;
	            case "SEME":
	            case "SEM.":
	            case "SEMESTRAL":
	            case "SEMESTR":
	            case "SEMESTRAL S/R":
	                dato = 2;
	                break;
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
	            case "MENS":
	            case "MEN.":
	            case "MENSUAL":
	            case "MENSUAL SIN RPF":
	            case "MENSUAL SR DERP":
	            case "MENSUAL SR":
	            case "MENSUAL S/R":
	            case "MENSUALS/R":
	            case "MEN.DER.PRORRATEA":
	            case "MENSUAL VITRO":
	            case "MENSUAL S/R DERP":
	            case "MEN.S/REC.":
	                dato = 4;
	                break;
	            case "QUIN":
	            case "QUICENAL":
	            case "QUINCENAL DXN VITRO":
	                dato = 5;
	                break;
	            case "SEMA":
	            case "SEMANAL":
	            case "SEMANAL VITRO":
	                dato = 6;
	                break;
	            default:
	                dato = 0;
	                break;
	        }
	        return dato;
	    }


	    public int getTotalRec(int formapago) { //se le pasa como parametro jsonObject.getInt("forma_pago")
	        int result = 0;
	        switch (formapago) {
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
	        int counterspace = 0;
	        for (int i = 0; i < contenido.length(); i++) {
	            if (contenido.charAt(i) == ' ') {
	                if (counterspace < 1) {
	                	counterspace = 1;
	                    if (result.length() > 0 ) {
	                        result += Character.toString(contenido.charAt(i));
	                    }
	                }
	            } else {
	            	counterspace = 0;
	                result += Character.toString(contenido.charAt(i));
	            }
	        }
	        return result;
	    }

	

	    public Boolean sexo(String texto) {
	        Boolean result = false;
	        switch (texto.toUpperCase()) {
	            case "MASCULINO":
	            case "HOMBRE":
	            case "H":
	            case "HIJO":
	            case "MLO":
	            case "MASC.":
	                result = true ;
	                break;
	            case "FEMENINO":
	            case "MUJER":
	            case "F":
	            case "HIJA":
	            case "FEM.":
	                result = false;
	                break;
	            default:
	                result = true;
	                break;
	        }
	        return result;
	    }

	    public int parentesco(String parentesco) {
	        int dato = 1;
	        switch (parentesco.toUpperCase()) {
	            case "TIT":
	            case "TITULAR":
	            case "ASEGURADO PRINCIPAL":
	                dato = 1;
	                break;
	            case "CONY.":
	            case "CONYUGE":
	                dato = 2;
	                break;
	            case "HIJO":
	            case "HIJO A":
	            case "HIJA":
	            case "HIJO/A":
	                dato = 3;
	                break;
	            case "PADRE":
	            case "NIETO":
	            case "MADRE":
	            case "ESPOSA":
	            case "ESPOSO":
	            case "ASEGURADO":
	            case "DEPENDIENTES":
	            case "OTRO":
	            case "HERMANO/A":
	            case "PARENTESCOS":
	            case "PERTENECIENTE":
	            case "HERMANA":
	            case "HERMANO":
	            case "SUEGRA":
	            case "ABUELO":
	                dato = 4;
	                break;
	            default:
	                dato = 1;
	                break;
	        }
	        return dato;
	    }

	    public String remplazaGrupoSpace(String dato) { // RETORNA UNA CADENA, EN DONDE TENGA MAS DE 2 ESPACIOS PONE ###
	        boolean encontrogrupo = false;
	        int par = 0;
	        String newdato = "";
	        for (int i = 0; i < dato.length(); i++) {
	            if (dato.charAt(i) == ' ') {
	                if (encontrogrupo == false) {
	                    par = par + 1;
	                    if (par == 2) {
	                    	encontrogrupo = true;
	                        newdato = newdato.trim();
	                        newdato += "###";
	                    } else {
	                        newdato += Character.toString(dato.charAt(i));
	                    }
	                }
	            } else {
	                par = 0;
	                encontrogrupo = false;
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
	        String contFix = contenido.replace("\n", "\r\n");
	        String texto = "";
	        if (contFix.contains("\r\r\n")) {
	            texto = contFix.replace("\r\r\n", "\r\n");
	        } else {
	            texto = contenido.replace("\n", "\r\n");
	        }
	        return texto;
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
                default:
                    result = 1;
                	break;
	        }
	        return result;
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
	            default:
	                fecha = "";
                	break;
	        }
	        return fecha;
	    }

	    public int monthAdd(int formapago) {
	        int meses = 0;
	        switch (formapago) {
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
	            default:
                    meses = 0;
                	break;
	        }
	        return meses;
	    }

	
	    public boolean isValidDate(String date) {//valida si es una fecha
	        String date1 = "^(0?[1-9]|[12][0-9]|3[01])/(0?[1-9]|1[012])/([12][0-9]{3})$";
	        return date.matches(date1);
	    }



}
