package com.copsis.models.Tabla;


import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import com.kenai.jffi.Array;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.pdfbox.pdmodel.font.PDType0Font;


public class Sio4CommunsPdf {
	private final Color textColor = Color.black;
    private final Color bgColor = new Color(255, 255, 255, 0);
    private final Integer fontsize = 9;
    private final float topPadding = 3.5f;
    private final boolean bold = false;
    private final String alignment = "L";
    private Color gray2 = new Color(239, 239, 239);
    private Color naranja = new Color(247, 150, 70);

    private LineStyle leftBorderStyle = new LineStyle(new Color(51, 153, 102), 0);
    private LineStyle rightBorderStyle = new LineStyle(new Color(51, 153, 102), 0);
    private LineStyle topBorderStyle = new LineStyle(new Color(51, 153, 102), 0);
    private LineStyle bottomBorderStyle = new LineStyle(new Color(51, 153, 102), 0);
    private LineStyle rightBorderStyle2 = new LineStyle(new Color(255, 255, 255), 0);
    private LineStyle leftBorderStyle2 = new LineStyle(new Color(255, 255, 255), 0);
    private LineStyle rightBorderStyle3 = new LineStyle(new Color(83, 183, 174), 0);
    private LineStyle leftBorderStyle3 = new LineStyle(new Color(83, 183, 174), 0);
    private LineStyle leftblack = new LineStyle(new Color(0, 0, 0), 0);
    private LineStyle lineBlack2 = new LineStyle(new Color(0, 0, 0), 0);
    private LineStyle lineBlack3 = new LineStyle(new Color(0, 0, 0), 1);

    private LineStyle lineGris = new LineStyle(new Color(196, 196, 196), 0);//para la cotizacion inter 37;
    private LineStyle lineAzulb = new LineStyle(new Color(35, 41, 88), 0);//para la cotizacion inter 37;
    private LineStyle linewhite = new LineStyle(new Color(255, 255, 255), 0);

    private LineStyle lineAzulC = new LineStyle(new Color(35, 81, 115), 0);//para la cotizacion inter 46;
    private LineStyle lineAzulv = new LineStyle(new Color(173, 216, 230), 0);//para la impresion 49

    private LineStyle lineNaranja = new LineStyle(new Color(247, 150, 70), 2);//para la impresion 49
    private LineStyle lineGraopc = new LineStyle(new Color(166, 166, 166), 0);//para la impresion 49

    private LineStyle lineNaranja2 = new LineStyle(new Color(131, 120, 111), 0);
     private LineStyle lineNaranja3 = new LineStyle(new Color(245, 145, 0), 0);

    public Sio4CommunsPdf() {
    }

    public final void drawText(PDPageContentStream content, Color txColor, Boolean bold, Integer size, float x, float y, String texto) throws IOException {
        content.beginText();
        content.newLineAtOffset(x, y);
        if (bold) {
            content.setFont(PDType1Font.HELVETICA_BOLD, size);
        } else {
            content.setFont(PDType1Font.HELVETICA, size);
        }
        content.setLeading(8.5f);
        content.setNonStrokingColor(txColor);
        content.showText(texto);
        content.endText();
    }

    public final void drawText(PDPageContentStream content, Boolean bold, float x, float y, String texto) throws IOException {
        content.beginText();
        content.newLineAtOffset(x, y);
        if (bold) {
            content.setFont(PDType1Font.HELVETICA_BOLD, fontsize);
        } else {
            content.setFont(PDType1Font.HELVETICA, fontsize);
        }
        content.setWordSpacing(0);
        content.setLeading(8.5f);
        content.setNonStrokingColor(textColor);
        content.showText(texto);
        content.endText();

    }

    public final void drawBox(PDPageContentStream content, Color backgroud, float x, float y, float xend, float yend) throws IOException {
        content.setNonStrokingColor(backgroud);
        content.addRect(x, y, xend, yend);
        content.fill();

    }

    public final void draWCircle(PDPageContentStream content, Color backgroud, float cx, float cy, float r, float yend) throws IOException {
        final float k = 0.552284749831f;
        content.setNonStrokingColor(backgroud);
        content.moveTo(cx - r, cy);
        content.curveTo(cx - r, cy + k * r, cx - k * r, cy + r, cx, cy + r);
        content.curveTo(cx + k * r, cy + r, cx + r, cy + k * r, cx + r, cy);
        content.curveTo(cx + r, cy - k * r, cx + k * r, cy - r, cx, cy - r);
        content.curveTo(cx - k * r, cy - r, cx - r, cy - k * r, cx - r, cy);
        content.fill();

    }

    public Row<PDPage> setRow(BaseTable table, float height) {
        Row<PDPage> row = table.createRow(height);
        return row;
    }

    public Row<PDPage> setRow(BaseTable table) {
        Row<PDPage> row = table.createRow(15f);
        return row;
    }


    public Cell<PDPage> setCell(Row<PDPage> row, int width, String content, Color black, boolean bold, String aligconte,
			int fontsize2, List<LineStyle> line, String aligconte2, List<Float> padding) {
		  Cell<PDPage> cell = row.createCell(width, content);
	        if (bold) {
	            cell.setFont(PDType1Font.HELVETICA_BOLD);
	        } else {
	            cell.setFont(PDType1Font.HELVETICA);
	        }
	        
	        cell.setLeftBorderStyle(line.get(0));
	        cell.setRightBorderStyle(line.get(1));
	        cell.setBottomBorderStyle(line.get(2));
	        cell.setTopBorderStyle(line.get(3));
	        
	        cell.setLeftPadding(padding.get(0));
	        cell.setRightPadding(padding.get(1));
	        cell.setTopPadding(padding.get(2));
	        cell.setBottomPadding(padding.get(3));
	     
	        cell.setTextColor(textColor);
	        cell.setFillColor(bgColor);
	        cell.setFontSize(fontsize);
	        cell.setTopPadding(topPadding);
	       this.setAligment(cell, aligconte);
	      //  this.setAligment(cell, aligconte2);
	        return cell;
				
	}


 


 




    public Cell<PDPage> setCell(Row<PDPage> row, int width, Image img) {
        Cell<PDPage> cell = row.createImageCell(width, img);
        return cell;
    }

    public Cell<PDPage> setCell(Row<PDPage> row, float width, Image img, int tipo, int cotizacion, Color bgColor) {
        Cell<PDPage> cell = row.createImageCell(width, img);
        if (cotizacion == 37) {

            cell.setLeftBorderStyle(lineGris);
            cell.setRightBorderStyle(lineGris);
            cell.setBottomBorderStyle(lineGris);
            cell.setTopBorderStyle(lineGris);
            switch (tipo) {
                case 0:
                    cell.setValign(VerticalAlignment.MIDDLE);
                    break;
                case 1:
                    cell.setValign(VerticalAlignment.MIDDLE);
                    cell.setAlign(HorizontalAlignment.CENTER);
                    cell.setFillColor(bgColor);
                case 3:
                    cell.setValign(VerticalAlignment.MIDDLE);
                    cell.setTopPadding(0);
                    cell.setBottomPadding(0);
                    cell.setLeftPadding(0);
                    cell.setRightPadding(0);
                    break;
                case 4:
                    cell.setValign(VerticalAlignment.MIDDLE);
                    cell.setAlign(HorizontalAlignment.CENTER);
                    cell.setTopPadding(0);
                    cell.setBottomPadding(0);
                    cell.setLeftPadding(0);
                    cell.setRightPadding(0);
                    break;

                case 5:
                    cell.setValign(VerticalAlignment.MIDDLE);
                    cell.setAlign(HorizontalAlignment.LEFT);
                    cell.setTopPadding(0);
                    cell.setBottomPadding(0);
                    cell.setLeftPadding(0);
                    cell.setRightPadding(0);
                    break;
                case 6:
                    cell.setValign(VerticalAlignment.MIDDLE);
                    cell.setAlign(HorizontalAlignment.RIGHT);
                    cell.setTopPadding(0);
                    cell.setBottomPadding(0);
                    cell.setLeftPadding(0);
                    cell.setRightPadding(2);
                    break;

            }

        }
        return cell;
    }





   



  
    public final Cell<PDPage> setAligment(Cell<PDPage> cell, String alignment) {
        switch (alignment) {
            case "L":
            case "l":
                cell.setAlign(HorizontalAlignment.LEFT);
                break;
            case "R":
            case "r":
                cell.setAlign(HorizontalAlignment.RIGHT);
                break;
            case "J":
            case "j":
                cell.setAlign(HorizontalAlignment.JUSTIFY);
                break;
            case "C":
            case "c":
                cell.setAlign(HorizontalAlignment.CENTER);
                break;
            default:
                cell.setAlign(HorizontalAlignment.LEFT);
                break;
        }

        return cell;
    }
    public final Cell<PDPage> setVertical(Cell<PDPage> cell, String alignment) {
        switch (alignment) {
            case "vB":
            case "vb":
                cell.setValign(VerticalAlignment.BOTTOM);
                break;
            case "vM":
            case "vm":
                cell.setValign(VerticalAlignment.MIDDLE);
                break;
            case "vT":
            case "vt":
                cell.setValign(VerticalAlignment.TOP);
                break;
     
            default:
                cell.setValign(VerticalAlignment.BOTTOM);
                break;
        }

        return cell;
    }

    public String eliminaHtmlTags(String value) {
        value = value.replaceAll("(\\r\\n|\\n|\\n\\n)", "<br/>")
                .replaceAll("\n\n|\n", "\\n")
                .replaceAll("&quot;", "\"")
                .replaceAll("\t|\\t", " ")
                .replaceAll("\u200B", "")
                .replaceAll("0x002D", "-")
                .replaceAll("\u2010", "-")
                .replaceAll("\uFF91", "")
                .replaceAll("<div>", "<br/>")
                .replaceAll("</div>", "")
                .replaceAll("\u2028", " ")
                .replaceAll("\u202c", " ")
                .replace("\u00A0", " ")
                .replaceAll("\u0009", " ");
        String pattern = "<([a-z]+)([^<]*)>|</([a-z]+)>";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(value);

        while (m.find()) {
            if (m.group().startsWith("</p")) {
                value = value.replace(m.group(), "<br/>");
            } else if (!m.group().startsWith("<b") && !m.group().startsWith("</b") && !m.group().startsWith("<br")) {
                value = value.replace(m.group(), "");
            }
        }

        return value.replaceAll("&nbsp;", " ");
    }

    public  String eliminaHtmlTags2(String value) {
        value = value.replaceAll("(\\r\\n|\\n|\\n\\n)", "<br/>")
                .replaceAll("\n\n|\n", "\\n")
                .replaceAll("&quot;", "\"")
                .replaceAll("\t|\\t", " ")
                .replaceAll("\u200B", "")
                .replaceAll("0x002D", "-")
                .replaceAll("\u2010", "-")
                .replaceAll("<div>", "<br/>")
                .replaceAll("</div>", "")
                .replaceAll("\u0009", " ");
        String pattern = "<([a-z]+)([^<]*)>|</([a-z]+)>";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(value);

        return value.replaceAll("&nbsp;", " ");
    }

    public static String replaceDatos(String texto) {
        texto = texto.replaceAll("(\\r\\n|\\n|\\n\\n)", "<br/>")
                .replaceAll("\n\n|\n", "\\n").replaceAll("\n", "\\n");
        return texto;
    }

    public static String eliminaHtmlTags3(String value) {
        value = value.replaceAll("(\\r\\n|\\n|\\n\\n)", "<br/>")
                .replaceAll("\n\n|\n", "\\n")
                .replaceAll("\t|\\t", "  ")
                .replaceAll("\u200B", "  ")
                .replaceAll("\u0009", "  ");

        return value.replaceAll("&nbsp;", " ");
    }

    public static String elimiDeducible(String deducible) {

        String valor = "";
        switch (deducible) {

            case "0":
            case "UMA":
            case "DSMGVDF":
            case "0 DSMGVDF":
            case "0%":
            case "SCGP":
                valor = "";
                break;
            default:
                valor = deducible;
                break;

        }

        return valor;

    }
    
    public boolean validar(String cadena) {
        String regex = "^\\d*\\.\\d+|\\d+\\.\\d*$";
		if (cadena.matches(regex)) {
			return true;
		} else {
			return false;
		}
	}

	
}
