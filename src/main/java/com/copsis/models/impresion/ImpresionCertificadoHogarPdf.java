package com.copsis.models.impresion;

import java.awt.Color;
import java.io.ByteArrayOutputStream;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;

import com.copsis.exceptions.GeneralServiceException;
import com.copsis.models.Tabla.BaseTable;
import com.copsis.models.Tabla.ImageUtils;
import com.copsis.models.Tabla.Row;
import com.copsis.models.Tabla.Sio4CommunsPdf;
import com.copsis.models.Tabla.VerticalAlignment;

public class ImpresionCertificadoHogarPdf {
	private Sio4CommunsPdf communsPdf = new Sio4CommunsPdf();
	private Color black = new Color(0, 0, 0);
	private final Color bgColor = new Color(255, 255, 255, 0);

	private float yStartNewPage = 760, yStart = 760, bottomMargin = 26,yStartnw =600;
	private float fullWidth = 542;
	
	public byte[] buildPDF() {
		ByteArrayOutputStream output;
		try {
			try (PDDocument document = new PDDocument()) {
				try {
					
					PDPage page = new PDPage();
					document.addPage(page);
					BaseTable table;
					Row<PDPage> baseRow;
					
					table = new BaseTable(yStart, yStartNewPage, bottomMargin, 200, 30, document, page, false,true);				 
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 80, ImageUtils.readImage(""), 1, 1, bgColor).setValign(VerticalAlignment.MIDDLE);
				    table.draw();
					
					
					output = new ByteArrayOutputStream();				
					document.save(output);
					return output.toByteArray();
				} finally {
					document.close();
				}
			}
			
		} catch (Exception ex) {
			throw new GeneralServiceException("00001",
					"Ocurrio un error en el servicio ImpresionInter: " + ex.getMessage());
		}
	}
}
