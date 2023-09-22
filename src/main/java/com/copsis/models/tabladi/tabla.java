package com.copsis.models.tabladi;

import java.io.IOException;
import java.net.MalformedURLException;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;

public class tabla {
        public PdfPTable setTable(int row, int withporcentaje, float[] wintrow, String textalig) throws DocumentException, MalformedURLException, IOException {

        PdfPTable tabla = new PdfPTable(row);
        tabla.setWidthPercentage(withporcentaje);
        tabla.setWidths(wintrow);

        setTableAlign(textalig, tabla);

        return tabla;
    }

    public PdfPTable setTable(int row, int withporcentaje, float[] wintrow, String textalig, int withtb) throws DocumentException, MalformedURLException, IOException {

        PdfPTable tabla = new PdfPTable(row);
        tabla.setWidthPercentage(withporcentaje);
        tabla.setTotalWidth(wintrow);
        

//        tabla.setWidths(widths);

        setTableAlign(textalig, tabla);

        return tabla;
    }

    private void setTableAlign(String textalig, PdfPTable tabla) {
        switch (textalig) {
            case "L":
            case "l":
                tabla.setHorizontalAlignment(Element.ALIGN_LEFT);
                break;
            case "R":
            case "r":
                tabla.setHorizontalAlignment(Element.ALIGN_RIGHT);
                break;
            case "J":
            case "j":
                tabla.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
                break;
            case "C":
            case "c":
                tabla.setHorizontalAlignment(Element.ALIGN_CENTER);
                break;
            default:
                tabla.setHorizontalAlignment(Element.ALIGN_LEFT);
                break;
        }
    }

    public PdfPCell setTextCell(String text, Boolean fonbol, Float fonsize, String textalig, BaseColor color, String textvetralig, PdfPTable pdftable, BaseColor colorbor, BaseColor colortexto, Float pdfcelheigInt, int fonsty) throws DocumentException, IOException {

        PdfPCell cell = new PdfPCell();

        cell.setUseAscender(true);
        Paragraph p = new Paragraph(text);
        p.getFont().setStyle(fonsty);
        p.getFont().setSize(fonsize);
        p.getFont().setColor(colortexto);

        setTextAlign(p, textalig);

        cell.addElement(p);
        cell.setBackgroundColor(color);
        cell.setBorderColor(colorbor);

        if (pdfcelheigInt == 0) {
            pdfcelheigInt = 15f;
        }
        
        if (pdfcelheigInt == 1) {
            pdfcelheigInt =  cell.getMaxHeight();
        }

        if (pdfcelheigInt > 0) {
            cell.setFixedHeight(pdfcelheigInt);
        }
        pdftable.addCell(cell);
        return cell;
    }

    private static void setverticalAlig(PdfPCell cell, String textvetralig) {
        switch (textvetralig) {
            case "M":
            case "m":
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                break;
            case "B":
            case "b":
                cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
                break;
            case "T":
            case "t":
                cell.setVerticalAlignment(Element.ALIGN_TOP);
                break;

        }
    }

    private static void setTextAlign(Paragraph p, String textalig) {
        switch (textalig) {
            case "L":
            case "l":
                p.setAlignment(Element.ALIGN_LEFT);
                break;
            case "R":
            case "r":
                p.setAlignment(Element.ALIGN_RIGHT);
                break;
            case "J":
            case "j":
                p.setAlignment(Element.ALIGN_JUSTIFIED);
                break;
            case "C":
            case "c":
                p.setAlignment(Element.ALIGN_CENTER);
                break;
            case "M":
            case "m":
                p.setAlignment(Element.ALIGN_MIDDLE);
                break;
            default:
                p.setAlignment(Element.ALIGN_LEFT);
                break;
        }
    }
}
