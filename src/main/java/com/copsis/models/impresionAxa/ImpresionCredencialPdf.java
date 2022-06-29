package com.copsis.models.impresionAxa;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.URL;

import javax.imageio.ImageIO;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import com.copsis.controllers.forms.ImpresionAxaForm;
import com.copsis.exceptions.GeneralServiceException;
import com.copsis.models.Tabla.BaseTable;
import com.copsis.models.Tabla.ImageUtils;
import com.copsis.models.Tabla.PDocumenteHW;
import com.copsis.models.Tabla.Row;
import com.copsis.models.Tabla.Sio4CommunsPdf;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ImpresionCredencialPdf {

	private Color blue = new Color(40, 76, 113);
	private Color black = new Color(0, 0, 0);
	private Color gray = new Color(229, 234, 237);
	private final Color bgColor = new Color(255, 255, 255, 0);

	private float margin = 2, yStartNewPage = 308, yStart = 308, bottomMargin = 30;
	private Sio4CommunsPdf communsPdf = new Sio4CommunsPdf();
	private float fullWidth = 240;
	private float fullWidth2 = 240;
	private Boolean acumula;

	public byte[] buildPDF(ImpresionAxaForm impresionAxa) {
		ByteArrayOutputStream output;
		try {
			try (PDDocument document = new PDDocument()) {
				try {
					PDPage page = new PDPage(PDocumenteHW.A7);
					document.addPage(page);
					BaseTable table;
					Row<PDPage> baseRow;
					setEncabezado(impresionAxa, document, page);

					output = new ByteArrayOutputStream();
					document.save(output);
					document.save(new File("/home/aalbanil/Documentos/AXA-SPRING-PF/credencial.pdf"));
					return output.toByteArray();
				} finally {
					document.close();
				}

			}

		} catch (Exception ex) {
			throw new GeneralServiceException("00001",
					"Ocurrio un error en el servicio ImpresionCredencialPdf: " + ex.getMessage());
		}

	}

	public void setEncabezado(ImpresionAxaForm impresionAxa, PDDocument document, PDPage page) {
		try (PDPageContentStream content = new PDPageContentStream(document, page)) {
			BaseTable table;
			Row<PDPage> baseRow;
			yStart = 160;

			if (impresionAxa.getLogoCredencial().length() > 0) {
				URL urllogo = new URL(impresionAxa.getLogoCredencial());
				BufferedImage imgbarra = ImageIO.read(urllogo);
				PDImageXObject pdImage2 = LosslessFactory.createFromImage(document, imgbarra);
				content.drawImage(pdImage2, 0, 165, 243, 149);
			}
			
				setEncabezado2(impresionAxa, document, page);
						
		     table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, margin, document, page, false, true);
	         baseRow = communsPdf.setRow(table, 12);	          
	         if (impresionAxa.getLogoBarraAxa().length() > 0) {
	        	 communsPdf.setCell(baseRow, 100, ImageUtils.readImage(impresionAxa.getLogoBarraAxa()));
	         }
	         baseRow = communsPdf.setRow(table, 5);	          
	         communsPdf.setCell(baseRow, 100, impresionAxa.getContrannte(),Color.BLACK,false, "L", 5, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(3.5f),bgColor);
	         if (isEndOfPage(table)) {
	         } else {
	        	 table.draw();
	        	 yStart -= table.getHeaderAndDataHeight();
	         }
	         
	         
	         table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, margin, document, page, false, true);
	         baseRow = communsPdf.setRow(table, 6);
	         communsPdf.setCell(baseRow, 81, "Poliza:",Color.BLACK,true, "R", 5, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(0f,0f,5f,5f),bgColor);
	         communsPdf.setCell(baseRow, 19, impresionAxa.getNoPoliza(),Color.BLACK,false, "R", 5, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(0f,5f,5f,5f),bgColor);
			 if (isEndOfPage(table)) {

			 } else {
				table.draw();
				yStart -= table.getHeaderAndDataHeight();
			 }
			 
			 System.out.println("asegurado => " + new ObjectMapper().writeValueAsString(impresionAxa.getCoberturas()));
			 
			    table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, margin, document, page, false, true);
			    for (int i = 0; i < impresionAxa.getCoberturas().size(); i++) {
			    	if(impresionAxa.getCoberturas().get(i).getIndex() > 0) {
			    		switch (impresionAxa.getCoberturas().get(i).getIndex()) {
						case 1:
						   baseRow = communsPdf.setRow(table, 6);
						   communsPdf.setCell(baseRow, 35, impresionAxa.getCoberturas().get(i).getNombres(),Color.BLACK,true, "L", 5, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(0f,0f,5f,5f),bgColor);
						   communsPdf.setCell(baseRow, 39, impresionAxa.getCoberturas().get(i).getCoaseguro(),Color.BLACK,false, "L", 5, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(0f,5f,5f,5f),bgColor);

						   if (i == 0) {
							   communsPdf.setCell(baseRow, 12, "Certificado:",Color.BLACK,true, "L", 5, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(0f,0f,5f,5f),bgColor);
							   communsPdf.setCell(baseRow, 17, impresionAxa.getNoCertificado(),Color.BLACK,false, "L", 5, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(0f,0f,5f,5f),bgColor);

						   }
						   if (i == 1) {
							   communsPdf.setCell(baseRow, 12, "Plan:",Color.BLACK,true, "L", 5, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(0f,0f,5f,5f),bgColor);
							   communsPdf.setCell(baseRow, 17, impresionAxa.getEtiquetaPlan(),Color.BLACK,false, "L", 5, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(0f,0f,5f,5f),bgColor);

						   }
						   
							break;
						case 2:
							   baseRow = communsPdf.setRow(table, 6);
							   communsPdf.setCell(baseRow, 35, impresionAxa.getCoberturas().get(i).getNombres(),Color.BLACK,true, "L", 5, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(0f,0f,5f,5f),bgColor);
							   communsPdf.setCell(baseRow, 39, impresionAxa.getCoberturas().get(i).getCoaseguro(),Color.BLACK,false, "L", 5, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(0f,5f,5f,5f),bgColor);

							   if (i == 0) {
								   communsPdf.setCell(baseRow, 12, "Certificado:",Color.BLACK,true, "L", 5, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(0f,0f,5f,5f),bgColor);
								   communsPdf.setCell(baseRow, 17, impresionAxa.getNoCertificado(),Color.BLACK,false, "L", 5, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(0f,5f,5f,5f),bgColor);

							   }
							   if (i == 1) {
								   communsPdf.setCell(baseRow, 12, "Plan:",Color.BLACK,true, "L", 5, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(0f,0f,5f,5f),bgColor);
								   communsPdf.setCell(baseRow, 17, impresionAxa.getEtiquetaPlan(),Color.BLACK,false, "L", 5, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(0f,5f,5f,5f),bgColor);

							   }
							   
								break;
						case 3:
						case 4:
							   baseRow = communsPdf.setRow(table, 6);
							   communsPdf.setCell(baseRow, 35, impresionAxa.getCoberturas().get(i).getNombres(),Color.BLACK,true, "L", 5, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(0f,0f,5f,5f),bgColor);
							   communsPdf.setCell(baseRow, 39, impresionAxa.getCoberturas().get(i).getCoaseguro(),Color.BLACK,false, "L", 5, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(0f,5f,5f,5f),bgColor);

							   if (i == 0) {
								   communsPdf.setCell(baseRow, 12, "Certificado:",Color.BLACK,true, "L", 5, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(0f,0f,5f,5f),bgColor);
								   communsPdf.setCell(baseRow, 17, impresionAxa.getNoCertificado(),Color.BLACK,false, "L", 5, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(0f,5f,5f,5f),bgColor);

							   }
							   if (i == 1) {
								   communsPdf.setCell(baseRow, 12, "Plan:",Color.BLACK,true, "L", 5, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(0f,0f,5f,5f),bgColor);
								   communsPdf.setCell(baseRow, 17, impresionAxa.getEtiquetaPlan(),Color.BLACK,false, "L", 5, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(0f,5f,5f,5f),bgColor);

							   }
							   
								break;

					
						}
			    		
			    	}else {
			    		   baseRow = communsPdf.setRow(table, 6);
			    		   communsPdf.setCell(baseRow, 35, impresionAxa.getCoberturas().get(i).getNombres(),Color.BLACK,true, "L", 5, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(0f,0f,5f,5f),bgColor);
						   communsPdf.setCell(baseRow, 65, impresionAxa.getCoberturas().get(i).getSa(),Color.BLACK,false, "L", 5, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(0f,5f,5f,5f),bgColor);

			    	}
			    }
			    if (isEndOfPage(table)) {

	            } else {
	                table.draw();
	                yStart -= table.getHeaderAndDataHeight() + 1;
	            }
			    

		} catch (Exception ex) {
			throw new GeneralServiceException("00001",
					"Ocurrio un error en el servicio ImpresionCredencialPdf: " + ex.getMessage());
		}
	}

	private void setEncabezado2(ImpresionAxaForm impresionAxa, PDDocument document, PDPage page) {
		try {
			yStart = 160;
			BaseTable table;
			Row<PDPage> baseRow;
			table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, margin, document, page, false, true);
			baseRow = communsPdf.setRow(table, 12);

			if (impresionAxa.getLogoBarraAxa().length() > 0) {
				communsPdf.setCell(baseRow, 100, ImageUtils.readImage((impresionAxa.getLogoBarraAxa())));
			}

			table.draw();

		} catch (Exception ex) {
			throw new GeneralServiceException("00001",
					"Ocurrio un error en el servicio ImpresionCredencialPdf: " + ex.getMessage());

		}
	}
	

    private boolean isEndOfPage(BaseTable table) {
        float currentY = yStart - table.getHeaderAndDataHeight();
        boolean isEndOfPage = currentY <= bottomMargin;
        return isEndOfPage;
    }


}
