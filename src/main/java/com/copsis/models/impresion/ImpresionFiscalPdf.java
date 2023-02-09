package com.copsis.models.impresion;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.net.URL;

import javax.imageio.ImageIO;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDPageContentStream.AppendMode;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.json.JSONArray;
import org.json.JSONObject;

import com.copsis.controllers.forms.ImpresionFiscalForm;
import com.copsis.exceptions.GeneralServiceException;
import com.copsis.models.Tabla.BaseTable;
import com.copsis.models.Tabla.Row;
import com.copsis.models.Tabla.Sio4CommunsPdf;
import com.copsis.models.Tabla.VerticalAlignment;

public class ImpresionFiscalPdf {
	
	private Sio4CommunsPdf communsPdf = new Sio4CommunsPdf();
	private Color black = new Color(0, 0, 0);
	private Color green = new Color(8, 109, 5,1);
	private Color gray = new Color(229, 229, 229,1);
	private Color graytb1 = new Color(220, 220, 220,1);
	private Color gray2 = new Color(102, 102, 102,1);
	private Color azul = new Color(41, 67, 191,1);
	private final Color bgColor = new Color(255, 255, 255, 0);
	private float yStartNewPage = 760, yStart = 760, bottomMargin = 26;

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
					communsPdf.setCell(baseRow, 100, "¡Gracias!", green,true, "C", 29, communsPdf.setLineStyle(Color.white,Color.white,Color.white,Color.white), "", communsPdf.setPadding(2f),bgColor);
					table.setRemBordes(true);
					table.draw();
					yStart -=table.getHeaderAndDataHeight()+10;
					
					table = new BaseTable(yStart, yStartNewPage, bottomMargin, 400, 100, document, page, false,true);
					baseRow = communsPdf.setRow(table, 20);
					communsPdf.setCell(baseRow, 100, "Tu número de folio es: ",gray2,true, "C", 17, communsPdf.setLineStyle(Color.white,Color.white,Color.white,Color.white), "", communsPdf.setPadding(2f),bgColor);
					baseRow = communsPdf.setRow(table, 20);
					communsPdf.setCell(baseRow, 100, impresionFiscalForm.getFolio(), black,true, "C", 17, communsPdf.setLineStyle(Color.white,Color.white,Color.white,Color.white), "", communsPdf.setPadding(2f),bgColor);
					table.setRemBordes(true);
					table.draw();
					
					yStart -=table.getHeaderAndDataHeight()+20;
//					
//					table2 = new BaseTable(yStart, yStartNewPage, bottomMargin, 470, 70, document, page, true,true);
//					
//					y =yStart;
					
					table = new BaseTable(yStart, yStartNewPage, bottomMargin, 470, 70, document, page, true,true);
					baseRow = communsPdf.setRow(table, 20);
					communsPdf.setCell(baseRow, 100, "Datos de identificación del Contribuyente:",Color.white,true, "L", 12, communsPdf.setLineStyle(gray), "", communsPdf.setPadding(2f),azul).setValign(VerticalAlignment.MIDDLE);;
					baseRow = communsPdf.setRow(table, 20);
					communsPdf.setCell(baseRow, 30,"RFC", black,false, "L", 12, communsPdf.setLineStyle(graytb1), "", communsPdf.setPadding(5f),bgColor,false).setValign(VerticalAlignment.MIDDLE);
					communsPdf.setCell(baseRow, 70,impresionFiscalForm.getRfc(), black,false, "L", 12, communsPdf.setLineStyle(Color.white,graytb1,graytb1,graytb1), "", communsPdf.setPadding(5f),bgColor,false).setValign(VerticalAlignment.MIDDLE);
				
					if(impresionFiscalForm.getTipo().toUpperCase().equals("MORAL")) {
						baseRow = communsPdf.setRow(table, 20);
						communsPdf.setCell(baseRow, 30,"Razón Social", black,false, "L", 12, communsPdf.setLineStyle(graytb1), "", communsPdf.setPadding(5f),bgColor,false).setValign(VerticalAlignment.MIDDLE);
						communsPdf.setCell(baseRow, 70,impresionFiscalForm.getRazonSocial(), black,false, "L", 12, communsPdf.setLineStyle(Color.white,graytb1,graytb1,graytb1), "", communsPdf.setPadding(5f),bgColor,false).setValign(VerticalAlignment.MIDDLE);
						baseRow = communsPdf.setRow(table, 20);
						communsPdf.setCell(baseRow, 30,"Régimen Capital", black,false, "L", 12, communsPdf.setLineStyle(graytb1), "", communsPdf.setPadding(5f),bgColor,false).setValign(VerticalAlignment.MIDDLE);
						communsPdf.setCell(baseRow, 70,impresionFiscalForm.getRegimenDeCapital(), black,false, "L", 12, communsPdf.setLineStyle(Color.white,graytb1,graytb1,graytb1), "", communsPdf.setPadding(5f),bgColor,false).setValign(VerticalAlignment.MIDDLE);
						baseRow = communsPdf.setRow(table, 20);
						communsPdf.setCell(baseRow, 30,"Código postal:", black,false, "L", 12, communsPdf.setLineStyle(graytb1), "", communsPdf.setPadding(5f),bgColor,false).setValign(VerticalAlignment.MIDDLE);
						communsPdf.setCell(baseRow, 70,impresionFiscalForm.getCp(), black,false, "L", 12, communsPdf.setLineStyle(Color.white,graytb1,graytb1,graytb1), "", communsPdf.setPadding(5f),bgColor,false).setValign(VerticalAlignment.MIDDLE);
			
					}else {									
					baseRow = communsPdf.setRow(table, 20);
					communsPdf.setCell(baseRow, 30,"Nombre (s):", black,false, "L", 12, communsPdf.setLineStyle(graytb1), "", communsPdf.setPadding(5f),bgColor,false).setValign(VerticalAlignment.MIDDLE);
					communsPdf.setCell(baseRow, 70,impresionFiscalForm.getNombre(), black,false, "L", 12, communsPdf.setLineStyle(Color.white,graytb1,graytb1,graytb1), "", communsPdf.setPadding(5f),bgColor,false).setValign(VerticalAlignment.MIDDLE);
					baseRow = communsPdf.setRow(table, 20);
					communsPdf.setCell(baseRow, 30,"Primer apellido:", black,false, "L", 12, communsPdf.setLineStyle(graytb1), "", communsPdf.setPadding(5f),bgColor,false).setValign(VerticalAlignment.MIDDLE);
					communsPdf.setCell(baseRow, 70,impresionFiscalForm.getApellidoP(), black,false, "L", 12, communsPdf.setLineStyle(Color.white,graytb1,graytb1,graytb1), "", communsPdf.setPadding(5f),bgColor,false).setValign(VerticalAlignment.MIDDLE);
					baseRow = communsPdf.setRow(table, 20);
					communsPdf.setCell(baseRow, 30,"Segundo apellido:", black,false, "L", 12, communsPdf.setLineStyle(graytb1), "", communsPdf.setPadding(5f),bgColor,false).setValign(VerticalAlignment.MIDDLE);
					communsPdf.setCell(baseRow, 70,impresionFiscalForm.getApellidoM(), black,false, "L", 12, communsPdf.setLineStyle(Color.white,graytb1,graytb1,graytb1), "", communsPdf.setPadding(5f),bgColor,false).setValign(VerticalAlignment.MIDDLE);
					baseRow = communsPdf.setRow(table, 20);
					communsPdf.setCell(baseRow, 30,"Código postal:", black,false, "L", 12, communsPdf.setLineStyle(graytb1), "", communsPdf.setPadding(5f),bgColor,false).setValign(VerticalAlignment.MIDDLE);
					communsPdf.setCell(baseRow, 70,impresionFiscalForm.getCp(), black,false, "L", 12, communsPdf.setLineStyle(Color.white,graytb1,graytb1,graytb1), "", communsPdf.setPadding(5f),bgColor,false).setValign(VerticalAlignment.MIDDLE);
					}
					

				
//					baseRow2= communsPdf.setRow(table2, (y -(yStart -=table.getHeaderAndDataHeight())));
//					communsPdf.setCell(baseRow2, 100,"", black,false, "L", 12, communsPdf.setLineStyle(gray), "", communsPdf.setPadding(5f),bgColor).setValign(VerticalAlignment.MIDDLE);
//					table2.draw();
					table.draw();
					yStart -=(table.getHeaderAndDataHeight()+10);
		
					
					JSONArray datosfinal =  new JSONArray();
					if(impresionFiscalForm.getProductos().size() > 0) {
						
						
						JSONObject iniciox = new JSONObject();
						iniciox.put("nombre", "");
						iniciox.put("regimenNombre", "Régimen Fiscal");
						iniciox.put("claveNombre", "Clave de uso");
						
						datosfinal.put(iniciox);
					}
					
				
					
				
					for (int i = 0; i < impresionFiscalForm.getProductos().size(); i++) {
						JSONObject inicio = new JSONObject();
						inicio.put("nombre",impresionFiscalForm.getProductos().get(i).getNombre());
						inicio.put("regimenNombre", impresionFiscalForm.getProductos().get(i).getRegimenNombre());
						inicio.put("claveNombre", impresionFiscalForm.getProductos().get(i).getClaveNombre());
						datosfinal.put(inicio);
						
					}
					
		
					
			
					
				int margin = (datosfinal.length() > 4  ? 10 :0);
				int with = 20;
			
			
									

			
				
					table = new BaseTable(yStart, yStartNewPage, bottomMargin, 530, (70 -margin), document, page, false,true);
					baseRow = communsPdf.setRow(table, 20);
					for (int i = 0; i < datosfinal.length(); i++) {		
						 with = getwith(datosfinal, with, i);
						communsPdf.setCell(baseRow, with,datosfinal.getJSONObject(i).getString("nombre") ,Color.black,false, "C", 8, communsPdf.setLineStyle(gray), "", communsPdf.setPadding(2f),azul);
					}
					table.draw();
					
					yStart -=table.getHeaderAndDataHeight()+5;
					table = new BaseTable(yStart, yStartNewPage, bottomMargin, 530, (70 -margin), document, page, false,true);
					baseRow = communsPdf.setRow(table, 20);
					for (int i = 0; i <datosfinal.length(); i++) {	
						 with = getwith(datosfinal, with, i);
						communsPdf.setCell(baseRow, with,datosfinal.getJSONObject(i).getString("regimenNombre"),Color.black,false, "L", 8, communsPdf.setLineStyle(gray), "", communsPdf.setPadding(2f),azul);
					}
					table.draw();
					
					yStart -=table.getHeaderAndDataHeight()+10;
					table = new BaseTable(yStart, yStartNewPage, bottomMargin, 530, (70 -margin), document, page, false,true);
					baseRow = communsPdf.setRow(table, 20);
					for (int i = 0; i < datosfinal.length(); i++) {	
						 with = getwith(datosfinal, with, i);
						communsPdf.setCell(baseRow, with,datosfinal.getJSONObject(i).getString("claveNombre") ,Color.black,false, "L", 8, communsPdf.setLineStyle(gray), "", communsPdf.setPadding(2f),azul);
					}
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

	private int getwith(JSONArray datosfinal, int with, int i) {
		if(i == 0) {
			 with =10;
		 }else {
				if(datosfinal.length()  == 7) {
					with = 90/6;
				}
				if(datosfinal.length()  == 4) {
									
					with=90/4;			}
				if(datosfinal.length() < 4) {
					with=20;
				}
		 }
		return with;
	}
	
	private void getMarcaAgua(PDDocument document, PDPage page) {
		try (PDPageContentStream content = new PDPageContentStream(document, page, AppendMode.APPEND,true,true )) {

			URL marcaAgua = new URL("https://storage.googleapis.com/biibiic-axa/bg_carta_02.png");
			BufferedImage imgMar = ImageIO.read(marcaAgua);
			PDImageXObject pdImage2 = LosslessFactory.createFromImage(document, imgMar);
			
			content.drawImage(pdImage2, 0, 0, 612, 792);
			
		} catch (Exception e) {
		throw new GeneralServiceException("Error:", e.getMessage());
		}
	}
	
	

}
