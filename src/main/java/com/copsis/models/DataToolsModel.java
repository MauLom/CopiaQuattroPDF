package com.copsis.models;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import com.copsis.constants.ConstantsValue;

public class DataToolsModel {
	DateTimeFormatter formatter;
	SimpleDateFormat simpleDateFormat;
	private static final  String RPL ="\r\r\n";
	private static final String FORMATDATE="yyyy-MM-dd";
	
	private static final String CONTADO="CONTADO";
	private static final String SEMESTRAL="SEMESTRAL";
	private static final String TRIMESTRAL="TRIMESTRAL";
	private static final String MENSUAL="MENSUAL";
	private static final String SEMANAL="SEMANAL";
	private static final String QUINCENAL="QUINCENAL";
	

	public boolean isNumeric(String value) {// validacion de si es numero
		try {
			Double.parseDouble(value);
			return true;
		} catch (Exception ex) {
			return false;
		}
	}

	public String cleanString(String texto) {// limpiar de signos los datos antes de convertir a numeros
		return texto.replace("(", "").replace(")", "").replace(",", "").replace("$", "").replace("MXP", "")
				.replace("MXN", "").trim();
	}

	public String preparaPrimas(String texto) {// limpiar de signos los datos antes de convertir a numeros
		return texto.replace(",", "").replace("MXP", "").replace("MXN", "").trim();
	}

	public List<ReplaceModel> remplazosGenerales() {
		List<ReplaceModel> remplazoDeA = new ArrayList<>();
		try {
			remplazoDeA.add(new ReplaceModel(RPL, "\r\n"));
			remplazoDeA.add(new ReplaceModel("\n", "\r\n"));
			remplazoDeA.add(new ReplaceModel(" :", ":"));
			remplazoDeA.add(new ReplaceModel("$", ""));
			remplazoDeA.add(new ReplaceModel("", ""));
			remplazoDeA.add(new ReplaceModel("MXP", ""));
			remplazoDeA.add(new ReplaceModel("/", "-"));
			remplazoDeA.add(new ReplaceModel("(", ""));
			remplazoDeA.add(new ReplaceModel(")", ""));
			remplazoDeA.add(new ReplaceModel(".:", ":"));
			remplazoDeA.add(new ReplaceModel("..:", ":"));
			remplazoDeA.add(new ReplaceModel("...:", ":"));
			remplazoDeA.add(new ReplaceModel("....:", ":"));
			remplazoDeA.add(new ReplaceModel("             ", " "));
			remplazoDeA.add(new ReplaceModel("            ", " "));
			remplazoDeA.add(new ReplaceModel("           ", " "));
			remplazoDeA.add(new ReplaceModel("          ", " "));
			remplazoDeA.add(new ReplaceModel("         ", " "));
			remplazoDeA.add(new ReplaceModel("        ", " "));
			remplazoDeA.add(new ReplaceModel("       ", " "));
			remplazoDeA.add(new ReplaceModel("      ", " "));
			remplazoDeA.add(new ReplaceModel("     ", " "));
			remplazoDeA.add(new ReplaceModel("    ", " "));
			remplazoDeA.add(new ReplaceModel("   ", " "));
			remplazoDeA.add(new ReplaceModel("  ", " "));
			remplazoDeA.add(new ReplaceModel("############", "###"));
			remplazoDeA.add(new ReplaceModel("#########", "###"));
			remplazoDeA.add(new ReplaceModel("######", "###"));
			remplazoDeA.add(new ReplaceModel("### ### ### ### ### ### ### ###", "###"));
			remplazoDeA.add(new ReplaceModel("### ### ### ### ### ### ###", "###"));	
			remplazoDeA.add(new ReplaceModel("### ### ### ### ### ###", "###"));
			remplazoDeA.add(new ReplaceModel("### ### ### ### ### ", "###"));
			remplazoDeA.add(new ReplaceModel("### ### ###","###"));
			remplazoDeA.add(new ReplaceModel("### ###","###"));

		
		
			
			return remplazoDeA;
		} catch (Exception e) {
			return remplazoDeA;
		}
	}
	
	public List<ReplaceModel> remplazosGeneralesV2() {
		List<ReplaceModel> remplazoDeA = new ArrayList<>();
		try {
			remplazoDeA.add(new ReplaceModel(RPL, "\r\n"));
			remplazoDeA.add(new ReplaceModel("\n", "\r\n"));
			remplazoDeA.add(new ReplaceModel(" :", ":"));
			remplazoDeA.add(new ReplaceModel("$", ""));
			remplazoDeA.add(new ReplaceModel("", ""));
			remplazoDeA.add(new ReplaceModel("MXP", ""));
			remplazoDeA.add(new ReplaceModel("/", "-"));
			remplazoDeA.add(new ReplaceModel(".:", ":"));
			remplazoDeA.add(new ReplaceModel("..:", ":"));
			remplazoDeA.add(new ReplaceModel("...:", ":"));
			remplazoDeA.add(new ReplaceModel("....:", ":"));
			remplazoDeA.add(new ReplaceModel("             ", " "));
			remplazoDeA.add(new ReplaceModel("            ", " "));
			remplazoDeA.add(new ReplaceModel("           ", " "));
			remplazoDeA.add(new ReplaceModel("          ", " "));
			remplazoDeA.add(new ReplaceModel("         ", " "));
			remplazoDeA.add(new ReplaceModel("        ", " "));
			remplazoDeA.add(new ReplaceModel("       ", " "));
			remplazoDeA.add(new ReplaceModel("      ", " "));
			remplazoDeA.add(new ReplaceModel("     ", " "));
			remplazoDeA.add(new ReplaceModel("    ", " "));
			remplazoDeA.add(new ReplaceModel("   ", " "));
			remplazoDeA.add(new ReplaceModel("  ", " "));
			remplazoDeA.add(new ReplaceModel("############", "###"));
			remplazoDeA.add(new ReplaceModel("#########", "###"));
			remplazoDeA.add(new ReplaceModel("######", "###"));
			remplazoDeA.add(new ReplaceModel("### ### ### ### ### ### ### ###", "###"));
			remplazoDeA.add(new ReplaceModel("### ### ### ### ### ### ###", "###"));	
			remplazoDeA.add(new ReplaceModel("### ### ### ### ### ###", "###"));
			remplazoDeA.add(new ReplaceModel("### ### ### ### ### ", "###"));
			remplazoDeA.add(new ReplaceModel("### ### ###","###"));
			remplazoDeA.add(new ReplaceModel("### ###","###"));
						
			return remplazoDeA;
		} catch (Exception e) {
			return remplazoDeA;
		}
	}

	public String remplazarMultiple(String texto, List<ReplaceModel> datos) {
		try {
			for (ReplaceModel r : datos) {

			
				texto = texto.replace(r.getRemplazaDe(), r.getRemplazaA());

			}
			return texto;
		} catch (Exception e) {
			return texto;

		}

	}

	public Integer castInteger(String texto) {
		Integer resultado = null;
		try {
			return Integer.parseInt(texto.replace(",", ""));
		} catch (Exception ex) {
			return resultado;
		}

	}

	public Double castDouble(String texto) {
		Double resultado = null;
		try {
			return Double.parseDouble(texto.replace(",", ""));
		} catch (Exception ex) {
			return resultado;
		}

	}

	public Float castFloat(String texto) {
		Float resultado = null;
		try {
			return Float.parseFloat(texto.replace(",", ""));
		} catch (Exception ex) {
			return resultado;
		}

	}

	public BigDecimal castBigDecimal(Object valueObj, Integer rango) {
		if (valueObj instanceof BigDecimal) {
			return BigDecimal.valueOf(((BigDecimal) valueObj).longValue()).setScale(rango, RoundingMode.HALF_EVEN);
		} else if (valueObj instanceof Long) {
			return BigDecimal.valueOf(((Long) valueObj).longValue()).setScale(rango, RoundingMode.HALF_EVEN);
		} else if (valueObj instanceof Short) {
			return BigDecimal.valueOf(((Short) valueObj).longValue()).setScale(rango, RoundingMode.HALF_EVEN);
		} else if (valueObj instanceof Integer) {
			return BigDecimal.valueOf(((Integer) valueObj).longValue()).setScale(rango, RoundingMode.HALF_EVEN);
		} else if (valueObj instanceof Double) {
			return BigDecimal.valueOf((Double) valueObj).setScale(rango, RoundingMode.HALF_EVEN);
		} else if (valueObj instanceof Float) {
			return BigDecimal.valueOf((Float) valueObj).setScale(rango, RoundingMode.HALF_EVEN);
		} else if (valueObj instanceof String) {
			return new BigDecimal(((String) valueObj)).setScale(rango, RoundingMode.HALF_EVEN);
		} else if (valueObj instanceof BigInteger) {
			return new BigDecimal(((BigInteger) valueObj)).setScale(rango, RoundingMode.HALF_EVEN);
		} else if (valueObj instanceof Number) {
			return BigDecimal.valueOf(((Number) valueObj).longValue()).setScale(rango, RoundingMode.HALF_EVEN);
		} else {
			return null;
		}
	}

	public BigDecimal castBigDecimal(Object valueObj) {
		if (valueObj instanceof BigDecimal) {
			return BigDecimal.valueOf(((BigDecimal) valueObj).longValue()).setScale(2, RoundingMode.HALF_EVEN);
		} else if (valueObj instanceof Long) {
			return BigDecimal.valueOf(((Long) valueObj).longValue()).setScale(2, RoundingMode.HALF_EVEN);
		} else if (valueObj instanceof Short) {
			return BigDecimal.valueOf(((Short) valueObj).longValue()).setScale(2, RoundingMode.HALF_EVEN);
		} else if (valueObj instanceof Integer) {
			return BigDecimal.valueOf(((Integer) valueObj).longValue()).setScale(2, RoundingMode.HALF_EVEN);
		} else if (valueObj instanceof Double) {
			return BigDecimal.valueOf((Double) valueObj).setScale(2, RoundingMode.HALF_EVEN);
		} else if (valueObj instanceof Float) {
			return BigDecimal.valueOf((Float) valueObj).setScale(2, RoundingMode.HALF_EVEN);
		} else if (valueObj instanceof String) {
			return new BigDecimal(((String) valueObj)).setScale(2, RoundingMode.HALF_EVEN);
		} else if (valueObj instanceof BigInteger) {
			return new BigDecimal(((BigInteger) valueObj)).setScale(2, RoundingMode.HALF_EVEN);
		} else if (valueObj instanceof Number) {
			return BigDecimal.valueOf(((Number) valueObj).longValue()).setScale(2, RoundingMode.HALF_EVEN);
		} else {
			return null;
		}
	}

	public String eliminaSpacios(String texto) {
		 StringBuilder result = new  StringBuilder();
		int counterSpace = 0;
		for (int i = 0; i < texto.length(); i++) {
			if (texto.charAt(i) == ' ') {
				if (counterSpace < 1) {
					counterSpace = 1;
					if (result.length() > 0 || result.length() > 0 && i < texto.length()) {
						result.append(Character.toString(texto.charAt(i)));
					}
				}
			} else {
				counterSpace = 0;
				result.append(Character.toString(texto.charAt(i)));
			}
		}
		return result.toString();
	}

	public String remplazaGrupoSpace(String dato) { // RETORNA UNA CADENA, EN DONDE TENGA MAS DE 2 ESPACIOS PONE ###
		boolean encontroGrupo = false;
		int par = 0;
		StringBuilder newdato = new StringBuilder();
		for (int i = 0; i < dato.length(); i++) {
			if (dato.charAt(i) == ' ') {
				if (!encontroGrupo) {
					par = par + 1;
					if (par == 2) {
						encontroGrupo = true;
						newdato.append(newdato.toString().trim());
						newdato.append("###");
					} else {
						newdato.append(Character.toString(dato.charAt(i)));
					}
				}
			} else {
				par = 0;
				encontroGrupo = false;
				newdato.append(Character.toString(dato.charAt(i)));
			}
		}
		return newdato.toString();
	}

	public String eliminaSpacios(String texto,char delimiter ,String valor) {
		boolean encontroGrupo = false;
		int counterspace = 0;
		StringBuilder result = new StringBuilder();
		for (int i = 0; i < texto.length(); i++) {
			if (texto.charAt(i) == ' ') {

				if (valor.length() > 0) {
					if (!encontroGrupo) {
						counterspace = counterspace + 1;
						if (counterspace == 2) {
							encontroGrupo = true;
							result.append(result.toString().trim());
							result.append("###");
						} else {
							result.append(Character.toString(texto.charAt(i)));
						}
					}

				} else {
					if (counterspace < 1) {
						counterspace = 1;

						if (result.length() > 0) {
							result.append(Character.toString(texto.charAt(i)));
						}
					}
				}

			} else {
				encontroGrupo = false;
				counterspace = 0;
				result.append(Character.toString(texto.charAt(i)));
			}
		}
		return result.toString();
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
			if (newtexto.length() >= 3 && newtexto.substring(0, 3).equals("###")) {
				newtexto = newtexto.substring(3, longText);
			}
		} else {
			newtexto = texto;
		}
		return newtexto;
	}

	public int searchTwoTexts(String texto, String textOne, String textTwo) {
		int result = 0;
		boolean toBreak = false;
		for (int i = 1; i < texto.trim().split("@@@").length; i++) {
			if (texto.length() > 0) {
				if (texto.trim().split("@@@")[i].contains(textOne) && texto.trim().split("@@@")[i].contains(textTwo)) {
					result = i;
					toBreak = true;
				}
			} else {
				if (texto.trim().split("@@@")[i].contains(textOne)) {
					result = i;
					toBreak = true;
				}
			}
			if(toBreak) {
				break;
			}

		}
		return result;
	}

	public String formatDateMonthCadena(String formatear) { /** RECIBE 02/FEBRERO/2018 || 02/FEB/2018 || 02/Feb/2018  // RETORNA 2018-02-02; **/
		String result = "";
		String day = "";
		if (formatear.split("-")[0].length() == 1) {
			day = "0" + formatear.split("-")[0];
		} else {
			day = formatear.split("-")[0];
		}
		String month = "";
		if(formatear.split("-")[1].length()  <= 2) {
			month = formatear.split("-")[1];	
		}else {
			 month = mes(formatear.split("-")[1]);			
		}
		
	
		String year = formatear.split("-")[2];
		result = year + "-" + month + "-" + day;
		return result;
	}

	public String formatDate(String fecha, String format) { // RECIBE FORMATO 02/02/2018 RETORNA 2018-02-02
		String resul = "";
		try {
			if (fecha.split("-")[1].length() > 2) {
				fecha = fecha.split("-")[0] + "-" + mes(fecha.split("-")[1]) + "/" + fecha.split("-")[2];
			}
			DateFormat formater;
			Date date;
			formater = new SimpleDateFormat(format);
			date = formater.parse(fecha.replace("/", "-"));
			simpleDateFormat = new SimpleDateFormat(FORMATDATE);
			return simpleDateFormat.format(date);
		} catch (Exception ex) {
			resul = ex.getMessage();
			return resul;

		}

	}

	public String mes(String mes) { /** RECIBE Ene || ENE || ENERO 02 */
		mes = mes.toUpperCase();
		List<String> meses = Arrays.asList("ENERO", "FEBRERO", "MARZO", "ABRIL", "MAYO", "JUNIO", "JULIO", "AGOSTO",
				"SEPTIEMBRE", "OCTUBRE", "NOVIEMBRE", "DICIEMBRE");
		if (mes.length() == 3) {
			meses = Arrays.asList("ENE", "FEB", "MAR", "ABR", "MAY", "JUN", "JUL", "AGO", "SEP", "OCT", "NOV", "DIC");
		}
		return (meses.indexOf(mes) + 1) > 9 ? "" + (meses.indexOf(mes) + 1)
				: "0" + (meses.indexOf(mes.toUpperCase()) + 1);
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
		case "DOLARES":
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
		case CONTADO:
		case "PRIMA UNICA":
		case "PAGO ÚNICO":
		case "PAGO UNICO":
		case "ANUA":
		case "ANUAL":
		case "ÚNICA":
		case "UNICA":
		case "ANUALMENTE":
		case "ANUAL EXTENDIDA":
			dato = 1;
			break;
		case "SEME":
		case "SEM.":
		case SEMESTRAL:
		case "SEMESTR":
		case "SEMESTRAL S/R":
		case "SEMESTRAL S-R":
			dato = 2;
			break;
		case "TRIM":
		case "TRIMESTR":
		case TRIMESTRAL:
		case "Trimestral":
		case "TRIMESTRAL S/R":
		case "TRIMESTRAL S-R":
		case "TRIMESTRAL SR DERP":
		case "TRIMESTRAL 12 MESES":
		case "TRIM.S/REC":
		case "TRIM.S-REC":
		case "TRIM.C-REC":
		case "TRIM.C/REC":	
			dato = 3;
			break;
		case "MENS":
		case "MEN.":
		case MENSUAL:
		case "MENSUAL SIN RPF":
		case "MENSUAL SR DERP":
		case "MENSUAL SR":
		case "MENSUAL S/R":
		case "MENSUALS/R":
		case "MEN.DER.PRORRATEA":
		case "MENSUAL VITRO":
		case "MENSUAL S/R DERP":
		case "MEN.S/REC.":
		case "MENSUAL S-R":
			dato = 4;
			break;
		case "QUIN":
		case "QUICENAL":
		case "QUINCENAL DXN VITRO":
			dato = 5;
			break;
		case "SEMA":
		case SEMANAL:
		case "SEMANAL VITRO":
			dato = 6;
			break;
		default:
			dato = 0;
			break;
		}
		return dato;
	}

	public int getTotalRec(int formapago) { // se le pasa como parametro jsonObject.getInt("forma_pago")
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
			result = 24; // REVISAR ESTE CASO
			break;
		case 6:
			result = 52; // REVISAR ESTE CASO
			break;
		default:
			break;
		}
		return result;
	}

	public String filtroPorRango(String texto, int rango) {
		StringBuilder textonuevo = new StringBuilder();
		for (int i = 0; i < texto.split("@@@").length; i++) {
			if (i < rango) {
				if (i == 0) {
					textonuevo.append(texto.split("@@@")[i].trim());
				} else {
					textonuevo.append("\r\n@@@" + texto.split("@@@")[i].trim());
				}
			} else {
				break;
			}
		}
		return textonuevo.toString();
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

	public Boolean sexo(String texto) {
		Boolean result = false;
		switch (texto.toUpperCase()) {
		case "MASCULINO":
		case "HOMBRE":
		case "H":
		case "HIJO":
		case "MLO":
		case "MASC.":
		case "MAS":
		case "M":
			result = true;
			break;
		case "FEMENINO":
		case "MUJER":
		case "F":
		case "HIJA":
		case "FEM.":
			result = false;
			break;
		default:
			result = false;
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
		case "CÓNYUGE":
			dato = 2;
			break;
		case "HIJO":
		case "HIJO A":
		case "HIJA":
		case "HIJO/A":
		case "HIJO-A":
		case "HIJO M":
		case "HIJA F":
		case "MENOR":
			dato = 3;
			break;
		case "PADRE":
		case "NIETO":
		case "MADRE":
		case "ESPOSA":
		case "ESPOSO":
		case "ESPOSA F":
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
		case "PADRE-MADRE":
			dato = 4;
			break;
		default:
			dato = 1;
			break;
		}
		return dato;
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

	public String recibo2(String fecha, int cuantos, int caso) {

		formatter = DateTimeFormatter.ofPattern(FORMATDATE);
		LocalDate date = LocalDate.parse(fecha);
		switch (caso) {
		case 1:// DIAS
			LocalDate dateNew = date.plusDays(cuantos);
			fecha = dateNew.format(formatter);
			break;
		case 2:// MESES
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


	public String textoBusqueda(PDFTextStripper pdfStripper, PDDocument pdDoc, String buscar, boolean tipo)
			throws IOException { // BUSCA UNA PAGINA QUE CONTENGA LO BUSCADO
		StringBuilder x = new StringBuilder();
		int listado = 0;

		for (int i = 1; i <= pdDoc.getPages().getCount(); i++) {
			pdfStripper.setStartPage(i);
			pdfStripper.setEndPage(i);
			if (pdfStripper.getText(pdDoc).contains(buscar)) {
				if (tipo) {// asegurados
					listado++;
					if (listado == 2) {
						PDFTextStripper s = new PDFTextStripper();
						s.setParagraphStart("###");
						s.setSortByPosition(true);
						s = pdfStripper;
						x.append(s.getText(pdDoc));
						break;
					}
				} else {// certificado|busca paginas necesarias
					PDFTextStripper s = new PDFTextStripper();
					s.setParagraphStart("###");
					s.setSortByPosition(true);
					s = pdfStripper;
					x.append(s.getText(pdDoc));
				}
			}
		}
		return x.toString();
	}

	public String dateAdd(String fecha, int cuantos, int caso) {
		formatter = DateTimeFormatter.ofPattern(FORMATDATE);
		LocalDate date = LocalDate.parse(fecha);
		if(caso ==1) {
			LocalDate dateNew = date.plusDays(cuantos);
			fecha = dateNew.format(formatter);
		}else {
			LocalDate dateNew1 = date.plusMonths(cuantos);
			fecha = dateNew1.format(formatter);
		}
	
		return fecha;
	}

	// Meodo que retorna la numero de pagina donde se encuentra ,el string a buscar
	public int pagFinRango(PDFTextStripper pdfStripper, PDDocument pdDoc, String buscar) throws IOException {
		int valor = 0;
		for (int i = 1; i <= pdDoc.getPages().getCount(); i++) {
			pdfStripper.setStartPage(i);
			pdfStripper.setEndPage(i);
			if (pdfStripper.getText(pdDoc).contains(buscar)) {
				valor = i;
				break;
			}
		}
		return valor;
	}

	public String fixContenido(String contenido) {
		String contFix = contenido.replace("\n", "\r\n");
		String texto = "";
		if (contFix.contains(RPL)) {
			texto = contFix.replace(RPL, "\r\n");
		} else {
			texto = contenido.replace("\n", "\r\n");
		}
		return texto;
	}

	public Boolean isvalidCp(String codigoPostal) {
		Boolean result = false;
		 result = Pattern.matches("[0-9]{5}", codigoPostal);
		return result;
	}

	/* DEVUELVE UN CONTENIDO DE UN RANGO DE PAGINAS */
	public String caratula(int inicio, int fin, PDFTextStripper stripper, PDDocument doc) throws IOException {
		stripper.setStartPage(inicio);
		stripper.setEndPage(fin);
	
		stripper.setParagraphStart("@@@");
		stripper.setWordSeparator("###");
		stripper.setSortByPosition(true);
		
		return stripper.getText(doc);
	}
	
	 public String coberturas(PDFTextStripper pdfStripper, PDDocument pdDoc, String buscar) throws IOException { //BUSCA UNA PAGINA QUE CONTENGA LO BUSCADO 
	        
	        StringBuilder x = new StringBuilder(); 
		      
     
	        for (int i = 1; i <= pdDoc.getPages().getCount(); i++) {

	            pdfStripper.setStartPage(i);
	            pdfStripper.setEndPage(i);


	            if (pdfStripper.getText(pdDoc).contains(buscar)) {
	                PDFTextStripper s = new PDFTextStripper();
	                s.setSortByPosition(true);
	                s = pdfStripper;
	                x.append(s.getText(pdDoc));
	            }
	        }

	        return x.toString();
	    }
	
	
	  public String recibos(PDFTextStripper pdfStripper, PDDocument pdDoc, String buscar) throws IOException { //BUSCA UNA PAGINA QUE CONTENGA LO BUSCADO 
	        StringBuilder x = new StringBuilder();
	        for (int i = 1; i <= pdDoc.getPages().getCount(); i++) {

	            pdfStripper.setStartPage(i);
	            pdfStripper.setEndPage(i);

	            if (pdfStripper.getText(pdDoc).contains(buscar)) {
	                PDFTextStripper s = new PDFTextStripper();
	                s.setParagraphStart("###");
	                s.setSortByPosition(true);
	                s = pdfStripper;
	                x.append(s.getText(pdDoc));
	            }
	        }

	        return x.toString();
	    }
	
	
	public int tipoPoliza(String contenido) {
		int dato = 0;
		// Autos 1 Salud 2 Vida 3 Empresarial 4

		String[] tipos = { "SEGURO DE AUTOMÓVILES", "AUTOMÓVILES", "DATOS DEL VEH", "PAQUETE DE SEGURO EMPRESARIAL","PLACAS",
				"AUTOS",
				"AUTO INDIVIDUAL",
				"VEHÍCULO",
				"SEGURO DE AUTOS",
				"CONTRATANTE Y CONDUCTOR",
				"CUATRIMOTOS",
				"ACCIDENTES PERSONALES",
				"GASTOS M", "TRADICIONALES DE VIDA","GASTOS MÉDICOS MAYORES",
				"GASTOS MÉDICOS","TECHOS",
				"DAÑOS",
				"FUNERARIOS",
				"VIDA","VIDA INDIVIDUAL",
				"HOGAR INTEGRAL", " VEHICLE DESCRIPTION",
				"PROTECCIÓN A BIENES EMPRESARIALES", "PLANPROTEGE / COMERCIO","CASA HABITACIÓN",
				"EMPRESARIAL"};

		 boolean encontro = false;
		for (String tipo : tipos) {	
			if (contenido.toUpperCase().contains(tipo) && !encontro) {
				switch (tipo) {
				case "DATOS DEL VEH":
				case "AUTOMÓVILES":
				case "SEGURO DE AUTOMÓVILES":
				case "SEGURO DE AUTOS":
				case "PLACAS":
				case "AUTO INDIVIDUAL":
				case "AUTOS":
				case "CONTRATANTE Y CONDUCTOR":
				case "VEHÍCULO":
				case "CUATRIMOTOS":
					dato = 1;
					encontro = true;
					break;
				case "GASTOS MÉDICOS":
				case "GMM":
				case "INDIVIDUAL/FAMILIAR":
				case "GASTOS MÉDICOS MAYORES":
				case "GASTOS M":
				case "ACCIDENTES":
					dato = 2;
					encontro = true;
					break;
				case "EMPRESARIAL":
				case "CASA HABITACIÓN":
				case "TECHOS":
				case "DAÑOS":
					dato = 4;
					encontro = true;
					break;
				case "VIDA":
				case "VIDA INDIVIDUAL":
				case "FUNERARIOS":
					dato = 5;
					encontro = true;
					break;
				default:
					dato = 0;
					encontro = false;
					break;
				}
			}
		}
		return dato;
	}
	
	public String extraerNumeros(String cadena) {
		String resultado = "";
		Matcher m = Pattern.compile("-?\\d+(,\\d+)*?\\.[0-9]?\\d+?").matcher(cadena);
		while (m.find()) {
			resultado = m.group();
		}
		return resultado;
	}	
	
	public String numTx (String cadena) {
		 Pattern p = Pattern.compile("-?\\d+(,\\d+)*?\\.?\\d+?");
		 String resultado = "";
	        Matcher m = p.matcher(cadena);
	        while (m.find()) {  
	        	resultado = m.group();
	        }
	        return resultado;
	}
	
	public String elimgatos(String texto) {// QUITA ### AL INICIO Y FINAL
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
	
	
	public String seccion(String res ) {
		String number;
		switch (res) {
        case "I":
        	number ="I";
        	break;
        case "II":
        	number ="I";
        	break;
        case "III":
        	number ="III";
        	break;
        case "IV":
        	number ="IV";
        	break;
        case "V":
        	number ="V";
        	break;
        case "VI":
        	number ="VI";
        	break;
        	
        case "VII":
        	number ="VII";
        	break;
        case "VIII":
        	number ="VIII";
        	break;
        case "IX":
        	number ="IX";
        	break;
        case "X":
        	number ="X";
        	break;
        case "XI":
        	number ="XI";
        	break;
        case "XII":
        	number ="XII";
        	break;
        case "XIII":
        	number ="XIII";
        	break;
        case "XIV":
        	number ="XIV";
        	break;
        case "XV":
        	number ="XV";
        	break;
         default:
        	 number ="";
         break;
		}
		return number;
	}
	
	public int formaPagoSring(String x) { /** FORMA DE PAGO **/
		int dato = 0;
		String[] tiposP = { "CONT", CONTADO, "PRIMAUNICA", "PAGOÚNICO", "PAGOUNICO", "ANUA", "ANUAL", "ÚNICA",
				"ANUALMENTE", "ANUALEXTENDIDA", "SEME", "SEM.", SEMESTRAL, "SEMESTR", "SEMESTRALS/R", "SEMESTRALS-R",
				"TRIM ", "TRIMESTR", TRIMESTRAL, "Trimestral", "TRIMESTRALS/R", "TRIMESTRALS-R", "TRIMESTRALSRDERP",
				"TRIMESTRAL12MESES", "TRIM.S/REC","TRIM.S-REC","TRIM.C-REC","TRIM.C/REC", "MENS", "MEN.", MENSUAL, "MENSUALSINRPF", "MENSUALSRDERP",
				"MENSUALSR", "MENSUALS/R", "MENSUALS/R", "MEN.DER.PRORRATEA", "MENSUALVITRO", "MENSUALS/RDERP",
				"MEN.S/REC.", "MENSUALS-R", "QUIN ", "QUICENAL", "QUINCENALDXNVITRO","QUINCENAL DXN", "SEMA", SEMANAL, "SEMANALVITRO","" };
		for (String tipo : tiposP) {
			if (x.toUpperCase().contains(tipo)) {
				switch (tipo) {
				case "CONT":
				case CONTADO:
				case "PRIMA UNICA":
				case "PAGO ÚNICO":
				case "PAGO UNICO":
				case "ANUA":
				case "ANUAL":
				case "ÚNICA":
				case "UNICA":
				case "ANUALMENTE":
				case "ANUAL EXTENDIDA":
					dato = 1;
					break;
				case "SEME":
				case "SEM.":
				case SEMESTRAL:
				case "SEMESTR":
				case "SEMESTRAL S/R":
				case "SEMESTRAL S-R":
					dato = 2;
					break;
				case "TRIM":
				case "TRIMESTR":
				case TRIMESTRAL:
				case "Trimestral":
				case "TRIMESTRAL S/R":
				case "TRIMESTRAL S-R":
				case "TRIMESTRAL SR DERP":
				case "TRIMESTRAL 12 MESES":
				case "TRIM.S/REC":
				case "TRIM.S-REC":
				case "TRIM.C-REC":
				case "TRIM.C/REC":	
					dato = 3;
					break;
				case "MENS":
				case "MEN.":
				case MENSUAL:
				case "MENSUAL SIN RPF":
				case "MENSUAL SR DERP":
				case "MENSUAL SR":
				case "MENSUAL S/R":
				case "MENSUALS/R":
				case "MEN.DER.PRORRATEA":
				case "MENSUAL VITRO":
				case "MENSUAL S/R DERP":
				case "MEN.S/REC.":
				case "MENSUAL S-R":
					dato = 4;
					break;
				case "QUIN":
				case "QUICENAL":
				case "QUINCENAL DXN VITRO":
				case "QUINCENAL DXN":
					dato = 5;
					break;
				case "SEMA":
				case SEMANAL:
				case "SEMANAL VITRO":
					dato = 6;
					break;
				}			
			}				
		}

		return dato;
	}
	
	public int buscaMonedaEnTexto(String texto) {
		int resultado = 0;
		List<String> listMonedas = Arrays.asList("NACIONAL", "NAL.", "PESOS", "PESO MEXICANO", "M.N.", "PESOS CON REVALUACIÓN ANUAL", "PESOS SIN REVALUACIÓN", "M.NAC", "PRIMA EN MONEDA NACIONAL", "MXP", "PESO","DÓLAR" ,"DÓLARES","DOLARES","DÓLAR AMERICANO","DOLARES US","USD","DÓLARES SIN REVALUACIÓN","U.S.DOLLAR","UDIS","UDI","UVACS");
		
		for(String moneda: listMonedas) {
			if(texto.toUpperCase().contains(moneda)) {
				switch (moneda) {
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
					resultado = 1;
					break;
				case "DÓLAR":	
				case "DÓLARES":
				case "DOLARES":
				case "DÓLAR AMERICANO":
				case "DOLARES US":
				case "USD":
				case "DÓLARES SIN REVALUACIÓN":
				case "U.S.DOLLAR":
					resultado = 2;
					break;
				case "UDIS":
				case "UDI":
					resultado = 3;
					break;
				case "UVACS":
					resultado = 4;
					break;
				default:
					resultado = 5;
					break;

				}
			}
			
		}
	return resultado;
		
	}
	public String obtenerFecha(String texto) {
		Pattern pattern = Pattern.compile(ConstantsValue.REGEX_FECHA);
		Matcher matcher = pattern.matcher(texto);
		return matcher.find() ? matcher.group() : "";
	}
	
	public String obtenerPolizaRegex(String texto,int lengthNumPoliza) {
		Pattern pattern = Pattern.compile(ConstantsValue.REGEX_POLIZA);
		Matcher matcher = pattern.matcher(texto);
		String result = matcher.find() ? matcher.group(3).trim():"";
	
		if (result.length() < lengthNumPoliza) {
			int inicio = matcher.start();
			result = texto.substring(inicio + 14, inicio + 60).split("\r\n")[0].replace(":", "").replace(" ", "")
					.trim();
		}
		return result;
	}
	
	public String obtenerCPRegex(String texto) {
		Pattern pattern = Pattern.compile(ConstantsValue.REGEX_CP);
		Matcher matcher = pattern.matcher(texto);
		String result = matcher.find() ? matcher.group(3).trim() : "";
		if(result.length()> 0 && result.length() < 5 ) {
			int inicio = matcher.start();
			result = texto.substring(inicio, inicio + 30).split("\r\n")[0].split(":")[1].replace(" ", "")
					.trim();
		}
		return result;
	}
	public String obtenerCPRegex2(String texto) {
		Pattern pattern = Pattern.compile(ConstantsValue.REGEX_CP2);
		Matcher matcher = pattern.matcher(texto);
		return matcher.find() ? matcher.group() : "";
	}
	
	
	public List<String> obtenerListNumeros(String cadena) {
		List<String> resultado = new ArrayList<>();
		Matcher m = Pattern.compile("-?\\d+(,\\d+)*?\\.[0-9]?\\d+?").matcher(cadena);
		while (m.find()) {
			resultado.add(m.group());
		}
		return resultado;
	}
	public List<String> obtenerListNumeros2(String cadena) {
		List<String> resultado = new ArrayList<>();
		Matcher m = Pattern.compile("[0-9]+(\\.[0-9][0-9]?)?").matcher(cadena);
		while (m.find()) {
			resultado.add(m.group());
		}
		return resultado;
	}
	
	
	  public String formatDate(String formatear) {
	        {
	            String str_date = formatear.replaceAll("/", "-");
	            String resul = "";
	            String rtfecha = "";
	            String day = "";
	            try {
	                DateFormat formatter;
	                Date date;
	                formatter = new SimpleDateFormat("yy-MM-dd");
	                date = formatter.parse(str_date);
	                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
	                resul = simpleDateFormat.format(date).toUpperCase();
	                return resul.toString();
	            } catch (Exception ex) {
	                return resul.toString();
	            }
	        }

	    }

		public int buscaParentesco(String texto) {
			int tipoParentesco = 0;
	
			List<String> listParentescos = Arrays.asList("TIT","TITULAR","ASEGURADO PRINCIPAL","CONY.","CONYUGE","CÓNYUGE","HIJO",
	        "HIJO","HIJO A","HIJA","HIJO/A","HIJO-A","HIJO M","HIJA F"
	        ,"PADRE","NIETO","MADRE","ESPOSA","ESPOSO","","ESPOSA F","ASEGURADO","DEPENDIENTES"
	        ,"OTRO","HERMANO/A","PARENTESCOS","PERTENECIENTE","HERMANA","HERMANO","ABUELO","PADRE-MADRE"
	        );
		
			for(String parentesco: listParentescos) {

				if(texto.toUpperCase().contains(parentesco)) {

				switch (parentesco) {
	                case "TIT":
	                case "TITULAR":
	                case "ASEGURADO PRINCIPAL":
	                tipoParentesco = 1;
	                    break;
	                case "CONY.":
	                case "CONYUGE":
	                case "CÓNYUGE":
	                tipoParentesco = 2;
	                    break;
	                case "HIJO":
	                case "HIJO A":
	                case "HIJA":
	                case "HIJO/A":
	                case "HIJO-A":
	                case "HIJO M":
	                case "HIJA F":
	                tipoParentesco = 3;
	                    break;
	                case "PADRE":
	                case "NIETO":
	                case "MADRE":
	                case "ESPOSA":
	                case "ESPOSO":
	                case "ESPOSA F":
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
	                case "PADRE-MADRE":
	                tipoParentesco = 4;
	                    break;
	           
	                }
	               
				}
			}
	
		return tipoParentesco;
			
		}
		
		public String extracted(int inicio, int fin, String contenido) {
			String texto = "";
			if(inicio > 0 && fin > 0 && inicio < fin ) {
				texto = contenido.substring(inicio,fin).replace("@@@", "").replace("\r", "").trim();
			}			
		  return texto;	
		}
		
		public List<String> obtenVigePoliza(String cadena) {
			List<String> resultado = new ArrayList<>();
			Matcher m = Pattern.compile(ConstantsValue.REGEX_FECHA).matcher(cadena);
			while (m.find()) {
				resultado.add(m.group());
			}
			return resultado;
		}
		
		public List<String> obtenVigePoliza2(String cadena) {
			List<String> resultado = new ArrayList<>();
			Matcher m = Pattern.compile(ConstantsValue.REGFEHCA_MES).matcher(cadena);
			while (m.find()) {
				resultado.add(m.group());
			}
			return resultado;
		}
	
		
		public String obtenerMes(String texto) {
			Pattern pattern = Pattern.compile(ConstantsValue.REGFEHCA_MES);
			Matcher matcher = pattern.matcher(texto);
			return matcher.find() ? matcher.group() : "";
		}
		
		public String calcvigenciaA(String fecha, int meses) {

			try {
				SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");		
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(formato.parse(fecha));
				calendar.add(Calendar.MONTH, meses);
				return formato.format(calendar.getTime());
			} catch (Exception e) {
			  return e.getMessage();
			}

		}
		
		public String calcfechavence(String fecha, int dias) {

			try {
				SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");		
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(formato.parse(fecha));
				calendar.add(Calendar.DAY_OF_MONTH, dias);
				return formato.format(calendar.getTime());
			} catch (Exception e) {
			  return e.getMessage();
			}

		}
		
		
		public String sumar(Date fecha,int meses) {

			Calendar calendar = Calendar.getInstance();
			calendar.setTime(fecha);
			calendar.add(Calendar.MONTH,meses);
			
			 SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd"); 
			return formato.format( calendar.getTime());
		}
		
		public int diferenciaDias(String vigD,String vigA ) {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");			 
			Date fechaInicial = null;
			Date fechaFinal = null;
			int dias=0;
			try {
				fechaInicial = dateFormat.parse(vigD);
				fechaFinal=dateFormat.parse(vigA);
				
				dias=(int) ((fechaFinal.getTime()-fechaInicial.getTime())/86400000);
				return dias;
			} catch (ParseException e) {
				return dias;

			}

		}

		public int diferencia(String vigD, String vigA) {
			int dias = 0;
			LocalDate localDate1 = LocalDate.parse(vigD, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
			LocalDate localDate2 = LocalDate.parse(vigA, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

			Period period = Period.between(localDate1, localDate2);
			dias = Math.abs(period.getYears());
			return dias;

		}
		

}
