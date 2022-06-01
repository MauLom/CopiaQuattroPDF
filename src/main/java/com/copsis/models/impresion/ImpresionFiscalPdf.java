package com.copsis.models.impresion;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.URL;

import javax.imageio.ImageIO;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDPageContentStream.AppendMode;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import com.copsis.controllers.forms.ImpresionFiscalForm;
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
	private Color azul = new Color(41, 67, 191,1);
	private final Color bgColor = new Color(255, 255, 255, 0);
	private float yStartNewPage = 760, yStart = 760, bottomMargin = 26;
	private float fullWidth = 542;
	private float margin = 40;
	private boolean acumula;
	public byte[] buildPDF(ImpresionFiscalForm  impresionFiscalForm ) {
		ByteArrayOutputStream output;
		try {
			try (PDDocument document = new PDDocument()) {
				try {
					
					PDPage page = new PDPage();				
					document.addPage(page);
					
					
					this.getMarcaAgua(document, page);
//					BaseTable table0;
//					Row<PDPage> baseRow0;
//					
//					table0 = new BaseTable(750, yStartNewPage, 0, 630, -10, document, page, true,true);
//					baseRow0 = communsPdf.setRow(table0, 100);
//					communsPdf.setCell(baseRow0, 100, ImageUtils.readImage("https://storage.googleapis.com/quattrocrm-copsis/s32tkk/2206/Polizas/2206/2Ow0kGUtV0qNONDhCJi5GW4nZgNJYWys7HYKAs1WmhzvS8SQvnDIdIVuxQg85/bg_carta.png"), 1, 1, bgColor).setValign(VerticalAlignment.MIDDLE);
//					table0.draw();
					
					
					BaseTable table;
					Row<PDPage> baseRow;
					BaseTable table2;
					Row<PDPage> baseRow2;
					
					BaseTable table3;
					Row<PDPage> baseRow3;
					
					
//					table2 = new BaseTable(yStart, yStartNewPage, 0, 630, -10, document, page, true,true);
//					baseRow2 = communsPdf.setRow(table2, 799);
//					communsPdf.setCell(baseRow2, 100, "1212", Color.white, false, "C", 30, communsPdf.setLineStyle(Color.white,Color.white,Color.white,Color.white), "", communsPdf.setPadding(2f),gray);
//					table2.draw();
//					
//					table = new BaseTable(yStart, yStartNewPage, bottomMargin, 630, -10, document, page, true,true);
//					baseRow = communsPdf.setRow(table, 15);
//					communsPdf.setCell(baseRow, 100, ImageUtils.readImage("https://storage.googleapis.com/quattrocrm-copsis/s32tkk/2205/Polizas/2205/Wbid33C5rJAW6z0NIjt4VekDyDxj7mlnbnQa4OexZoy02VdX7PMbEzW1D91TW/Frame_1.png"), 1, 1, bgColor).setValign(VerticalAlignment.MIDDLE);
//					table.draw();
					
//					yStart -=table.getHeaderAndDataHeight()+100;
					
//					table3 = new BaseTable((yStart+10), yStartNewPage, bottomMargin, 300, 140, document, page, true,true);
//					baseRow3 = communsPdf.setRow(table3, 130);
//					communsPdf.setCell(baseRow3, 100, "", green,true, "C", 30, communsPdf.setLineStyle(Color.white,Color.white,Color.white,Color.white), "", communsPdf.setPadding(2f),bgColor);
//					table3.draw();
//					
					table = new BaseTable(yStart, yStartNewPage, bottomMargin, 300, 140, document, page, false,true);
					baseRow = communsPdf.setRow(table, 35);
					communsPdf.setCell(baseRow, 100, "¡Gracias!", green,true, "C", 25, communsPdf.setLineStyle(Color.white,Color.white,Color.white,Color.white), "", communsPdf.setPadding(2f),bgColor);
					table.setRemBordes(true);
					table.draw();
					yStart -=table.getHeaderAndDataHeight()+10;
					
					table = new BaseTable(yStart, yStartNewPage, bottomMargin, 300, 140, document, page, false,true);
					baseRow = communsPdf.setRow(table, 20);
					communsPdf.setCell(baseRow, 100, "Tu número de folio es: ",gray2,true, "C", 12, communsPdf.setLineStyle(Color.white,Color.white,Color.white,Color.white), "", communsPdf.setPadding(2f),bgColor);
					baseRow = communsPdf.setRow(table, 20);
					communsPdf.setCell(baseRow, 100, impresionFiscalForm.getFolio(), black,true, "C", 12, communsPdf.setLineStyle(Color.white,Color.white,Color.white,Color.white), "", communsPdf.setPadding(2f),bgColor);
					table.setRemBordes(true);
					table.draw();
					
					yStart -=table.getHeaderAndDataHeight()+20;
					table = new BaseTable(yStart, yStartNewPage, bottomMargin, 470, 70, document, page, true,true);
					baseRow = communsPdf.setRow(table, 20);
					communsPdf.setCell(baseRow, 100, "Datos de identificación del Contribuyente:",Color.white,true, "L", 12, communsPdf.setLineStyle(gray), "", communsPdf.setPadding(2f),azul);
					baseRow = communsPdf.setRow(table, 25);
					communsPdf.setCell(baseRow, 30,"RFC", black,false, "L", 12, communsPdf.setLineStyle(gray), "", communsPdf.setPadding(5f),bgColor).setValign(VerticalAlignment.MIDDLE);
					communsPdf.setCell(baseRow, 70,impresionFiscalForm.getRfc(), black,false, "L", 12, communsPdf.setLineStyle(Color.white,gray,gray,gray), "", communsPdf.setPadding(5f),bgColor).setValign(VerticalAlignment.MIDDLE);
				System.err.println(impresionFiscalForm.getTipo().toUpperCase());
					if(impresionFiscalForm.getTipo().toUpperCase().equals("MORAL")) {
						baseRow = communsPdf.setRow(table, 25);
						communsPdf.setCell(baseRow, 30,"Razón Social", black,false, "L", 12, communsPdf.setLineStyle(gray), "", communsPdf.setPadding(5f),bgColor).setValign(VerticalAlignment.MIDDLE);
						communsPdf.setCell(baseRow, 70,impresionFiscalForm.getNombre(), black,false, "L", 12, communsPdf.setLineStyle(Color.white,gray,gray,gray), "", communsPdf.setPadding(5f),bgColor).setValign(VerticalAlignment.MIDDLE);
						baseRow = communsPdf.setRow(table, 25);
						communsPdf.setCell(baseRow, 30,"Regimen Capital", black,false, "L", 12, communsPdf.setLineStyle(gray), "", communsPdf.setPadding(5f),bgColor).setValign(VerticalAlignment.MIDDLE);
						communsPdf.setCell(baseRow, 70,impresionFiscalForm.getApellidoP(), black,false, "L", 12, communsPdf.setLineStyle(Color.white,gray,gray,gray), "", communsPdf.setPadding(5f),bgColor).setValign(VerticalAlignment.MIDDLE);
						baseRow = communsPdf.setRow(table, 25);
						communsPdf.setCell(baseRow, 30,"Código postal:", black,false, "L", 12, communsPdf.setLineStyle(gray), "", communsPdf.setPadding(5f),bgColor).setValign(VerticalAlignment.MIDDLE);
						communsPdf.setCell(baseRow, 70,impresionFiscalForm.getCp(), black,false, "L", 12, communsPdf.setLineStyle(Color.white,gray,gray,gray), "", communsPdf.setPadding(5f),bgColor).setValign(VerticalAlignment.MIDDLE);
			
					}else {									
					baseRow = communsPdf.setRow(table, 25);
					communsPdf.setCell(baseRow, 30,"Nombre (s):", black,false, "L", 12, communsPdf.setLineStyle(gray), "", communsPdf.setPadding(5f),bgColor).setValign(VerticalAlignment.MIDDLE);
					communsPdf.setCell(baseRow, 70,impresionFiscalForm.getNombre(), black,false, "L", 12, communsPdf.setLineStyle(Color.white,gray,gray,gray), "", communsPdf.setPadding(5f),bgColor).setValign(VerticalAlignment.MIDDLE);
					baseRow = communsPdf.setRow(table, 25);
					communsPdf.setCell(baseRow, 30,"Primer apellido:", black,false, "L", 12, communsPdf.setLineStyle(gray), "", communsPdf.setPadding(5f),bgColor).setValign(VerticalAlignment.MIDDLE);
					communsPdf.setCell(baseRow, 70,impresionFiscalForm.getApellidoP(), black,false, "L", 12, communsPdf.setLineStyle(Color.white,gray,gray,gray), "", communsPdf.setPadding(5f),bgColor).setValign(VerticalAlignment.MIDDLE);
					baseRow = communsPdf.setRow(table, 25);
					communsPdf.setCell(baseRow, 30,"Segundo apellido:", black,false, "L", 12, communsPdf.setLineStyle(gray), "", communsPdf.setPadding(5f),bgColor).setValign(VerticalAlignment.MIDDLE);
					communsPdf.setCell(baseRow, 70,impresionFiscalForm.getApellidoM(), black,false, "L", 12, communsPdf.setLineStyle(Color.white,gray,gray,gray), "", communsPdf.setPadding(5f),bgColor).setValign(VerticalAlignment.MIDDLE);
					baseRow = communsPdf.setRow(table, 25);
					communsPdf.setCell(baseRow, 30,"Código postal:", black,false, "L", 12, communsPdf.setLineStyle(gray), "", communsPdf.setPadding(5f),bgColor).setValign(VerticalAlignment.MIDDLE);
					communsPdf.setCell(baseRow, 70,impresionFiscalForm.getCp(), black,false, "L", 12, communsPdf.setLineStyle(Color.white,gray,gray,gray), "", communsPdf.setPadding(5f),bgColor).setValign(VerticalAlignment.MIDDLE);
					}
					table.draw();
					if(impresionFiscalForm.getTipo().toUpperCase().equals("MORAL")) {
						yStart -=table.getHeaderAndDataHeight();
						table = new BaseTable(yStart, yStartNewPage, bottomMargin, 470, 70, document, page, true,true);
						baseRow = communsPdf.setRow(table, 20);
						communsPdf.setCell(baseRow, 100, "Regímenes:",Color.white,true, "L", 12, communsPdf.setLineStyle(gray), "", communsPdf.setPadding(2f),azul);
						baseRow = communsPdf.setRow(table, 20);
						communsPdf.setCell(baseRow, 30,"Régiminen", black,false, "L", 12, communsPdf.setLineStyle(gray), "", communsPdf.setPadding(5f),bgColor).setValign(VerticalAlignment.MIDDLE);
						communsPdf.setCell(baseRow, 70,impresionFiscalForm.getCp(), black,false, "L", 12, communsPdf.setLineStyle(Color.white,gray,gray,gray), "", communsPdf.setPadding(5f),bgColor).setValign(VerticalAlignment.MIDDLE);				
						table.draw();
					
					}
				
				

					output = new ByteArrayOutputStream();				
					document.save(output);
				document.save(new File("/home/aalbanil/Documentos/axa.pdf"));
				
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
	
	private void getMarcaAgua(PDDocument document, PDPage page) {
		try (PDPageContentStream content = new PDPageContentStream(document, page, AppendMode.APPEND,true,true )) {

			URL marcaAgua = new URL("https://storage.googleapis.com/quattrocrm-copsis/s32tkk/2206/Polizas/2206/2Ow0kGUtV0qNONDhCJi5GW4nZgNJYWys7HYKAs1WmhzvS8SQvnDIdIVuxQg85/bg_carta.png");
			BufferedImage imgMar = ImageIO.read(marcaAgua);
			PDImageXObject pdImage2 = LosslessFactory.createFromImage(document, imgMar);
			
			content.drawImage(pdImage2, 0, 0, 612, 792);
			
		} catch (Exception e) {
		throw new GeneralServiceException("Error:", e.getMessage());
		}
	}
	
	

}
