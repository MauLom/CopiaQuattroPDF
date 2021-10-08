package com.copsis.models.Tabla;


import java.awt.Color;
import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.util.Matrix;


public class Sio4CommunsPdf {
	private final Color textColor = Color.black;
    private final Color bgColor = new Color(255, 255, 255, 0);
    private final Integer fontsize = 9;
    public  Integer impresiom = 0;
    

 
    private LineStyle lineGris = new LineStyle(new Color(196, 196, 196), 0);//para la cotizacion inter 37;
    private LineStyle lineAzul = new LineStyle(new Color(0, 0, 143), 0);//para la cotizacion inter 37;
    private LineStyle lineBlanco = new LineStyle(new Color(255, 255, 255), 0);//para la cotizacion inter 37;
 

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
    
    public final void drawText(PDPageContentStream content, Boolean bold, float x, float y, String texto,Color cltxt) throws IOException {
        content.beginText();
        content.newLineAtOffset(x, y);
        if (bold) {
            content.setFont(PDType1Font.HELVETICA_BOLD, fontsize);
        } else {
            content.setFont(PDType1Font.HELVETICA, fontsize);
        }
        content.setWordSpacing(0);
        content.setLeading(8.5f);
        content.setNonStrokingColor(cltxt);
        content.showText(texto);
        content.setTextMatrix(new Matrix(12, 0, 0, 12, 0, 10 * 1.5f));
        content.endText();

    }
    public final void drawText(PDPageContentStream contentStream, Boolean bold, float x, float y, String texto,Color cltxtm,Boolean rotate) throws IOException {
    	contentStream.saveGraphicsState();
    	contentStream.beginText();
    	// set font and font size
    	 if (bold) {
    		 contentStream.setFont(PDType1Font.HELVETICA_BOLD, fontsize);
         } else {
        	 contentStream.setFont(PDType1Font.HELVETICA, fontsize);
         }
    	// set text color to red
    	contentStream.setNonStrokingColor(cltxtm);
    	if (rotate) {
    	    // rotate the text according to the page rotation
    	
    	    contentStream.setTextRotation(-1.57f, 594, 130);
    	} else {
    	    contentStream.setTextTranslation(30,
    	            200);
    	}
    	contentStream.drawString(texto);
    	contentStream.endText();
    	contentStream.restoreGraphicsState();
    	contentStream.close();

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
	
	        cell.setTextColor(black);
	        cell.setFillColor(bgColor);
	       
	        if(impresiom == 102) {
	        	 cell.setFontSize(fontsize);
	        }else {
	        	 cell.setFontSize(fontsize2);
	        }
	       
	     
	       this.setAligment(cell, aligconte);
	    
	        return cell;
				
	}
    
    public Cell<PDPage> setCell(Row<PDPage> row, int width, String content, Color black, boolean bold, String aligconte,
			int fontsize2, List<LineStyle> line, String aligconte2, List<Float> padding,Color bgColor2) {
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
	
	        
	        cell.setTextColor(black);
	        cell.setFillColor(bgColor2);
	        cell.setFontSize(fontsize2);
	     
	       this.setAligment(cell, aligconte);
	    
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
        if( cotizacion == 103) {

            switch (tipo) {
            case 1:
            	   cell.setLeftBorderStyle(lineBlanco);
                   cell.setRightBorderStyle(lineAzul);
                   cell.setBottomBorderStyle(lineBlanco);
                   cell.setTopBorderStyle(lineBlanco);
            	break;
            case 2:
         	   cell.setLeftBorderStyle(lineBlanco);
                cell.setRightBorderStyle(lineBlanco);
                cell.setBottomBorderStyle(lineBlanco);
                cell.setTopBorderStyle(lineBlanco);
         	break;
            	default:
                    cell.setLeftBorderStyle(lineAzul);
                    cell.setRightBorderStyle(lineAzul);
                    cell.setBottomBorderStyle(lineAzul);
                    cell.setTopBorderStyle(lineAzul);
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
