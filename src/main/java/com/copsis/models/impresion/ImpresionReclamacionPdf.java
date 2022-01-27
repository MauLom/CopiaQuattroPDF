package com.copsis.models.impresion;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.File;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;

import com.copsis.controllers.forms.ImpresionForm;
import com.copsis.exceptions.GeneralServiceException;
import com.copsis.models.Tabla.BaseTable;
import com.copsis.models.Tabla.Row;
import com.copsis.models.Tabla.Sio4CommunsPdf;

public class ImpresionReclamacionPdf {
	private Sio4CommunsPdf communsPdf = new Sio4CommunsPdf();
	private Color black = new Color(0, 0, 0);
	private final Color bgColor = new Color(255, 255, 255, 0);
	private final Color bgColorA = new Color(0, 0, 143);
	private float yStartNewPage = 760, yStart = 760, bottomMargin = 26;
	private float fullWidth = 542;
	public byte[] buildPDF(ImpresionForm impresionForm) {
		ByteArrayOutputStream output;
		try {
			try (PDDocument document = new PDDocument()) {
				try {
				
					PDPage page = new PDPage();
					document.addPage(page);
					BaseTable table;
					Row<PDPage> baseRow;
				
					table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, 30, document, page, false,true);
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 7, "Póliza:", black, false, "L", 10, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor);
					communsPdf.setCell(baseRow, 15, "0245784512", black, false, "L", 10, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor);
					communsPdf.setCell(baseRow, 6, "Inciso:", black, false, "L", 10, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor);
					communsPdf.setCell(baseRow, 6, "0001", black, false, "L", 10, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor);
					communsPdf.setCell(baseRow, 18, "3", black, false, "L", 10, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor);
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 12, "Contratante:", black, false, "L", 10, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor);
					communsPdf.setCell(baseRow, 69, "(71) Pedro Montalvo Veloz", black, false, "L", 10, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor);					
					communsPdf.setCell(baseRow, 12, "quattroCRM:", black, false, "L", 10, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor);
					communsPdf.setCell(baseRow, 5, "23", black, false, "L", 10, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor);
					
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 12, "Concepto:", black, false, "L", 10, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor);
					communsPdf.setCell(baseRow, 70, "KIA FORTE LX MT 4P 4CIL 2021", black, false, "L", 10, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor);					
					communsPdf.setCell(baseRow, 15, "Administrador QuattroCRM", black, false, "L", 10, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor);
					
					
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 12, "Vigencia:", black, false, "L", 10, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor);
					communsPdf.setCell(baseRow, 70, "25 feb 2021 - 25 feb 2022", black, false, "L", 10, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor);					
					communsPdf.setCell(baseRow, 15, "RESPONSABLE", black, false, "L", 10, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor);
				
					table.draw();
					
					yStart -= table.getHeaderAndDataHeight();
					
					table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, 30, document, page, true,true);
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 100, "", black, false, "L", 10, communsPdf.setLineStyle(Color.white,Color.white,Color.black,Color.white), "", communsPdf.setPadding(2f),bgColor);
					table.draw();
					
					
					yStart -= table.getHeaderAndDataHeight() +5;
					
					table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, 30, document, page, false,true);
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 65, "(713) (204-1-1) KIA FORTE LX MT 4P 4CIL", black, false, "L", 10, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor);
					communsPdf.setCell(baseRow, 14, "Captura:", black, false, "L", 10, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor);
					communsPdf.setCell(baseRow, 21, "09 dic 2021 09:40 AM", black, false, "L", 10, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor);
				
					
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 15, "Conductor:", black, false, "L", 10, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor);
					communsPdf.setCell(baseRow, 50, "", black, false, "L", 10, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor);
					communsPdf.setCell(baseRow, 14, "Siniestro:", black, false, "L", 10, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor);
					communsPdf.setCell(baseRow, 21, "S/A", black, false, "L", 10, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor);
					
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 15, "Modelo:", black, false, "L", 10, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor);
					communsPdf.setCell(baseRow, 21, "", black, false, "L", 10, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor);
					communsPdf.setCell(baseRow, 10, "Nomina:", black, false, "L", 10, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor);
					communsPdf.setCell(baseRow, 19, "", black, false, "L", 10, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor);
					communsPdf.setCell(baseRow, 14, "Reporte:", black, false, "L", 10, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor);
					communsPdf.setCell(baseRow, 21, "S/A", black, false, "L", 10, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor);
				
					

					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 15, "No. de Serie:", black, false, "L", 10, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor);
					communsPdf.setCell(baseRow, 50, "", black, false, "L", 10, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor);
					communsPdf.setCell(baseRow, 14, "Promesa Pago:", black, false, "L", 10, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor);
					communsPdf.setCell(baseRow, 21, "S/A", black, false, "L", 10, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor);
					
					
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 8, "Placas:", black, false, "L", 10, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor);
					communsPdf.setCell(baseRow, 12, "", black, false, "L", 10, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor);
					communsPdf.setCell(baseRow, 8, "Clave:", black, false, "L", 10, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor);
					communsPdf.setCell(baseRow, 12, "", black, false, "L", 10, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor);
					communsPdf.setCell(baseRow, 8, "Valor:", black, false, "L", 10, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor);
					communsPdf.setCell(baseRow, 17, "", black, false, "L", 10, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor);
					communsPdf.setCell(baseRow, 14, "Pago Real:", black, false, "L", 10, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor);
					communsPdf.setCell(baseRow, 21, "S/A", black, false, "L", 10, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor);
				
					
					table.draw();
					
	                 yStart -= table.getHeaderAndDataHeight();
					
					table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, 30, document, page, true,true);
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 100, "", black, false, "L", 10, communsPdf.setLineStyle(Color.white,Color.white,Color.black,Color.white), "", communsPdf.setPadding(2f),bgColor);
					table.draw();
					
				    yStart -= table.getHeaderAndDataHeight()+5;
				    
				    table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, 30, document, page, false,true);
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 100, "Documentos", black, false, "L", 10, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor);
					table.draw();
					yStart -= table.getHeaderAndDataHeight();
				    table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, 30, document, page, false,true);
					
					
					output = new ByteArrayOutputStream();
					document.save(output);
					document.save(new File("/home/desarrollo8/Pictures/prueba.pdf"));
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
