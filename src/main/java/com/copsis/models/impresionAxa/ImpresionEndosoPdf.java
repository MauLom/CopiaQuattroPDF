package com.copsis.models.impresionAxa;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.File;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;

import com.copsis.controllers.forms.ImpresionAxaForm;
import com.copsis.exceptions.GeneralServiceException;
import com.copsis.models.Tabla.BaseTable;
import com.copsis.models.Tabla.ImageUtils;
import com.copsis.models.Tabla.Row;
import com.copsis.models.Tabla.Sio4CommunsPdf;

public class ImpresionEndosoPdf {
	private final Color bgColor = new Color(255, 255, 255, 0);
	private Color black = new Color(0, 0, 0);
	private float margin = 10, yStartNewPage = 780, yStart = 780, bottomMargin = 130, yDespacho = 0, bottomMargin2 = 30;
	private float fullWidth =590;
	private Sio4CommunsPdf communsPdf = new Sio4CommunsPdf();

	public byte[] buildPDF(ImpresionAxaForm impresionAxa) {
		ByteArrayOutputStream output;
		try {
			try (PDDocument document = new PDDocument()) {
				try {
					PDPage page = new PDPage();
					document.addPage(page);
					BaseTable table;
					Row<PDPage> baseRow;

					setEncabezado(impresionAxa, document, page,false);

					output = new ByteArrayOutputStream();
					document.save(output);
					document.save(new File("/home/aalbanil/Documentos/AXA-SPRING-PF/endoso.pdf"));
					return output.toByteArray();
				} finally {
					document.close();
				}

			}

		} catch (Exception ex) {
			throw new GeneralServiceException("00001",
					"Ocurrio un error en el servicio ImpresionEndosoPdf: " + ex.getMessage());
		}
	}

	private void setEncabezado(ImpresionAxaForm impresionAxa, PDDocument document, PDPage page, Boolean asegurados) {
		try (PDPageContentStream content = new PDPageContentStream(document, page)) {
			yStart = 780;
			BaseTable table;
			Row<PDPage> baseRow;
	        table = new BaseTable(yStart, yStartNewPage, bottomMargin, 295, margin, document, page, false, true);
            baseRow = communsPdf.setRow(table, 12);
            communsPdf.setCell(baseRow, 100, ImageUtils.readImage(impresionAxa.getLogoSuperior()));
            table.draw();
            
            table = new BaseTable(yStart, yStartNewPage, bottomMargin, 200, (602 - 200), document, page, false, true);
            baseRow = communsPdf.setRow(table, 12);
            communsPdf.setCell(baseRow, 100,"Endoso",Color.BLACK,true, "R", 11, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
            baseRow = communsPdf.setRow(table, 12);
            communsPdf.setCell(baseRow, 100,"Gastos Médicos Mayores",Color.BLACK,true, "R", 11, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
            baseRow = communsPdf.setRow(table, 12);
            communsPdf.setCell(baseRow, 100,impresionAxa.getEtiquetaPlan(),Color.BLACK,true, "R", 11, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
            baseRow = communsPdf.setRow(table, 12);
            communsPdf.setCell(baseRow, 65,"Póliza",Color.BLACK,true, "R", 11, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
            communsPdf.setCell(baseRow, 35,impresionAxa.getNoPoliza(),Color.BLACK,true, "R", 11, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
            baseRow = communsPdf.setRow(table, 12);
        	communsPdf.setCell(baseRow, 100,"ORIGINAL",Color.BLACK,true, "R", 10, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
            baseRow = communsPdf.setRow(table, 12);
        	communsPdf.setCell(baseRow, 90,"Hoja:",Color.BLACK,true, "R", 10, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
            table.draw();
            
            yStart -= (table.getHeaderAndDataHeight() + 10);

            table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, margin, document, page, false, true);
            baseRow = communsPdf.setRow(table, 12);
            communsPdf.setCell(baseRow, 20,"Contratante",Color.BLACK,true, "L", 11, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,4f,5f),bgColor);
            baseRow = communsPdf.setRow(table);
            communsPdf.setCell(baseRow, 20,"Nombre",Color.BLACK,true, "L", 10, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,4f,5f),bgColor);	            
            communsPdf.setCell(baseRow, 80,impresionAxa.getContrannte(),Color.BLACK,false, "L", 10, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,4f,5f),bgColor);
            baseRow = communsPdf.setRow(table);
            communsPdf.setCell(baseRow, 20,"Fecha de Emisión:",Color.BLACK,true, "L", 10, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,4f,5f),bgColor);
            communsPdf.setCell(baseRow, 80,impresionAxa.getFechaEmision(),Color.BLACK,false, "L", 10, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,4f,5f),bgColor);
            baseRow = communsPdf.setRow(table);
            communsPdf.setCell(baseRow, 20,"Vigencia de:",Color.BLACK,true, "L", 10, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,4f,5f),bgColor);
            communsPdf.setCell(baseRow, 80,impresionAxa.getVigenciaDe(),Color.BLACK,false, "L", 10, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,4f,5f),bgColor);
            baseRow = communsPdf.setRow(table);
            communsPdf.setCell(baseRow, 20,"Duracion dias:",Color.BLACK,true, "L", 10, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,4f,5f),bgColor);
            communsPdf.setCell(baseRow, 80,impresionAxa.getDias(),Color.BLACK,false, "L", 10, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,4f,5f),bgColor);
            baseRow = communsPdf.setRow(table);
            communsPdf.setCell(baseRow, 20,"Forma de Pago:",Color.BLACK,true, "L", 10, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,4f,5f),bgColor);
            communsPdf.setCell(baseRow, 80,impresionAxa.getFormaPago(),Color.BLACK,false, "L", 10, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,4f,5f),bgColor);
            baseRow = communsPdf.setRow(table);
            communsPdf.setCell(baseRow, 20,"Moneda:",Color.BLACK,true, "L", 10, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,4f,5f),bgColor);
            communsPdf.setCell(baseRow, 80,impresionAxa.getMoneda(),Color.BLACK,false, "L", 10, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,4f,5f),bgColor);
            baseRow = communsPdf.setRow(table);
            communsPdf.setCell(baseRow, 20,"Numero de Endoso:",Color.BLACK,true, "L", 10, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,4f,5f),bgColor);
            communsPdf.setCell(baseRow, 30,"",Color.BLACK,true, "L", 10, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,4f,5f),bgColor);
         
            communsPdf.setCell(baseRow, 13,"Movimiento:",Color.BLACK,true, "L", 10, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(3f,5f,4f,5f),bgColor);
            communsPdf.setCell(baseRow, 9,impresionAxa.getEndosoId(),Color.BLACK,false, "L", 10, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,4f,5f),bgColor);
            baseRow = communsPdf.setRow(table);
            communsPdf.setCell(baseRow, 20,"Subgrupo:",Color.BLACK,true, "L", 10, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,4f,5f),bgColor);
            communsPdf.setCell(baseRow, 80,impresionAxa.getSubGrupo(),Color.BLACK,false, "L", 10, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,4f,5f),bgColor);    
            table.draw();
		
		} catch (Exception ex) {
			throw new GeneralServiceException("00001",
					"Ocurrio un error en el servicio ImpresionEndosoPdf: " + ex.getMessage());
		}
	}

}
