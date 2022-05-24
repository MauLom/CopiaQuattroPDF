package com.copsis.models.impresion;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.File;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;

import com.copsis.dto.SURAImpresionEmsionDTO;
import com.copsis.exceptions.GeneralServiceException;
import com.copsis.models.Tabla.BaseTable;
import com.copsis.models.Tabla.ImageUtils;
import com.copsis.models.Tabla.Row;
import com.copsis.models.Tabla.Sio4CommunsPdf;
import com.copsis.models.Tabla.VerticalAlignment;

public class ImpresionFiscalPdf {
	
	private Sio4CommunsPdf communsPdf = new Sio4CommunsPdf();
	private Color black = new Color(0, 0, 0);
	private Color green = new Color(8, 109, 5,1);
	private Color gray = new Color(229, 229, 229,1);
	private Color gray2 = new Color(102, 102, 102,1);
	private final Color bgColor = new Color(255, 255, 255, 0);
	private float yStartNewPage = 800, yStart = 800, bottomMargin = 26;
	private float fullWidth = 542;
	private float margin = 40;
	private boolean acumula;
	public byte[] buildPDF(String folio) {
		ByteArrayOutputStream output;
		try {
			try (PDDocument document = new PDDocument()) {
				try {
					
					PDPage page = new PDPage();				
					document.addPage(page);
					BaseTable table;
					Row<PDPage> baseRow;
					BaseTable table2;
					Row<PDPage> baseRow2;
					
					BaseTable table3;
					Row<PDPage> baseRow3;
					
					
					table2 = new BaseTable(yStart, yStartNewPage, 0, 630, -10, document, page, true,true);
					baseRow2 = communsPdf.setRow(table2, 799);
					communsPdf.setCell(baseRow2, 100, "1212", Color.white, false, "C", 30, communsPdf.setLineStyle(Color.white,Color.white,Color.white,Color.white), "", communsPdf.setPadding(2f),gray);
					table2.draw();
					
					table = new BaseTable(yStart, yStartNewPage, bottomMargin, 630, -10, document, page, true,true);
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 100, ImageUtils.readImage("https://storage.googleapis.com/quattrocrm-copsis/s32tkk/2205/Polizas/2205/Wbid33C5rJAW6z0NIjt4VekDyDxj7mlnbnQa4OexZoy02VdX7PMbEzW1D91TW/Frame_1.png"), 1, 1, bgColor).setValign(VerticalAlignment.MIDDLE);
					table.draw();
					
					yStart -=table.getHeaderAndDataHeight()+100;
					
					table3 = new BaseTable((yStart+10), yStartNewPage, bottomMargin, 300, 140, document, page, true,true);
					baseRow3 = communsPdf.setRow(table3, 130);
					communsPdf.setCell(baseRow3, 100, "", green,true, "C", 30, communsPdf.setLineStyle(Color.white,Color.white,Color.white,Color.white), "", communsPdf.setPadding(2f),bgColor);
					table3.draw();
					
					table = new BaseTable(yStart, yStartNewPage, bottomMargin, 300, 140, document, page, false,true);
					baseRow = communsPdf.setRow(table, 35);
					communsPdf.setCell(baseRow, 100, "¡Gracias!", green,true, "C", 25, communsPdf.setLineStyle(Color.white,Color.white,Color.white,Color.white), "", communsPdf.setPadding(2f),bgColor);
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 100, "Tus datos fiscales han sido registrados correctamente", black,false, "C", 6, communsPdf.setLineStyle(Color.white,Color.white,Color.white,Color.white), "", communsPdf.setPadding(2f),bgColor);
					baseRow = communsPdf.setRow(table, 20);
					communsPdf.setCell(baseRow, 60, "Tu número de folio es: ",gray2,true, "R", 12, communsPdf.setLineStyle(Color.white,Color.white,Color.white,Color.white), "", communsPdf.setPadding(2f),bgColor);
					communsPdf.setCell(baseRow, 40, folio, black,true, "L", 12, communsPdf.setLineStyle(Color.white,Color.white,Color.white,Color.white), "", communsPdf.setPadding(2f),bgColor);
				    StringBuilder contenido = new StringBuilder();
				    contenido.append("En caso de requerir alguna modificación acude con tu\n"
				    		+ "Intermediario para la solicitud de los cambios de facturación y estos aplicarán\n"
				    		+ "únicamente para recibos posteriores a la solicitud.");
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 100, Sio4CommunsPdf.eliminaHtmlTags3(contenido.toString()), black,false, "C", 6, communsPdf.setLineStyle(Color.white,Color.white,Color.white,Color.white), "", communsPdf.setPadding(2f),bgColor);
				
					table.setRemBordes(true);
					table.draw();
					
					table = new BaseTable(90, yStartNewPage, bottomMargin, 560, 25, document, page, true,true);
					baseRow = communsPdf.setRow(table, 35);
									communsPdf.setCell(baseRow, 100, ImageUtils.readImage("https://storage.googleapis.com/quattrocrm-copsis/s32tkk/2205/Polizas/2205/Wbid33C5rJAW6z0NIjt1gmfBF8GbsTEIGoTe4bHVxyNnk625bM6XxIRyoYyD/image_2.png"), 1, 1, bgColor).setValign(VerticalAlignment.MIDDLE);
				
					table.setRemBordes(true);
				    table.draw();
				
				
			
					table = new BaseTable(43, yStartNewPage, 0, 630, -10, document, page, true,true);
					baseRow = communsPdf.setRow(table, 20);
					communsPdf.setCell(baseRow, 100, ImageUtils.readImage("https://storage.googleapis.com/quattrocrm-copsis/s32tkk/2205/Polizas/2205/Wbid33C5rJAW6z0NIjt6lwa76KrAt5M08wIRYCvEUl1jMtSBgDEjOSxLUzkgQQ/barra.png"), 1, 1, bgColor).setBottomPadding(0f);
					
					table.setRemBordes(true);
				    table.draw();
						
					output = new ByteArrayOutputStream();				
					document.save(output);
				
					return output.toByteArray();
				}finally {
					document.close();
				}
				
			}
			
		} catch (Exception ex) {
			throw new GeneralServiceException("00001",
					"Ocurrio un error en el servicio ImpresionInter: " + ex.getMessage());
		}
		
	}
	

}
