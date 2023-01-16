package com.copsis.models.Tabla;

import java.util.List;

import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import java.awt.Color;

public class setCellRow {

    public Cell<PDPage> setCell(Row<PDPage> row, int width, String content, Color black, boolean bold, String aligconte,
            int fontsize2, List<LineStyle> line, String aligconte2, List<Float> padding, Color bgColor2
           ) {
        Cell<PDPage> cell = row.createCell(width, content);
        if (bold) {
            cell.setFont(PDType1Font.HELVETICA_BOLD);
        } else {
            cell.setFont(PDType1Font.HELVETICA);
        }

        cell.setLeftBorderStyle(line.get(0));
        cell.setRightBorderStyle(line.get(1));
        cell.setTopBorderStyle(line.get(2));
        cell.setBottomBorderStyle(line.get(3));

        cell.setLeftPadding(padding.get(0));
        cell.setRightPadding(padding.get(1));
        cell.setTopPadding(padding.get(2));
        cell.setBottomPadding(padding.get(3));

        cell.setTextColor(black);
     

        cell.setFontSize(fontsize2);

        this.setAligment(cell, aligconte);

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
}
