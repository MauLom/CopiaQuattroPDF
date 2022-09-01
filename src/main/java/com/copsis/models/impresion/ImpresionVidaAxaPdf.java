package com.copsis.models.impresion;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.File;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;

import com.copsis.controllers.forms.ImpresionAxaForm;
import com.copsis.controllers.forms.ImpresionAxaVidaForm;
import com.copsis.exceptions.GeneralServiceException;
import com.copsis.models.Tabla.BaseTable;
import com.copsis.models.Tabla.ImageUtils;
import com.copsis.models.Tabla.Row;
import com.copsis.models.Tabla.Sio4CommunsPdf;

public class ImpresionVidaAxaPdf {
	
	private Sio4CommunsPdf communsPdf = new Sio4CommunsPdf();
	private final Color bgColor = new Color(255, 255, 255, 0);
	private final Color bgColorAb = new Color(116, 111, 159, 0);
	private final Color bgColorA = new Color(36, 44, 106, 0);
	private float margin = 10, yStartNewPage = 780, yStart = 780, bottomMargin = 130;
	private float fullWidth = 566;
	private Boolean acumula;

	public byte[] buildPDF(ImpresionAxaVidaForm  impresionAxaVidaForm) {
		ByteArrayOutputStream output;
		try {
			
			try (PDDocument document = new PDDocument()) {
				try {
					PDPage page = new PDPage();
					document.addPage(page);
					BaseTable table;
					Row<PDPage> baseRow;
					
					this.setEncabezado(document, page);
					
					table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, margin, document, page, false, true);	    
					baseRow = communsPdf.setRow(table, 10);
					communsPdf.setCell(baseRow, 100,"Datos del Contratante (Es la persona que se compromete a realizar el pago de la prima)",Color.BLACK,true, "L", 1, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
					baseRow = communsPdf.setRow(table, 10);
					communsPdf.setCell(baseRow, 100,"El nombre completo, el RFC con homoclave y la CURP son datos necesarios para la emisión de las constancias y CFDI para la deducción de impuestos y, en su caso, para la recuperación de estos.",Color.BLACK,true, "L", 1, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
					table.draw();
					
					
					output = new ByteArrayOutputStream();
					document.save(output);									
					document.save(new File("/home/aalbanil/Documentos/AXA-SPRING-PF/AxaVida.pdf"));
					return output.toByteArray();
					
				}finally {
					document.close();
				}
				
			}
			
		} catch (Exception ex) {
			throw new GeneralServiceException("00001",
					"Ocurrio un error en el servicio ImpresionVidaAxaPdf: " + ex.getMessage());
		}
	}
	private void setEncabezado( PDDocument document, PDPage page) {
		try (PDPageContentStream content = new PDPageContentStream(document, page)) {
			BaseTable table;
			Row<PDPage> baseRow;
			
			table = new BaseTable(770, 770, bottomMargin, 120, margin, document, page, false, true);
			baseRow = communsPdf.setRow(table, 12);
			communsPdf.setCell(baseRow, 100, ImageUtils.readImage("https://storage.googleapis.com/quattrocrm/h31fila/2208/Polizas/2208/zeSWdRgKylw7QuyGNMQ9vc8HSPiToM8cVUnQd5e2VOIzWrci1IgN1QLcOhFEfhy/Axalogo.png"));
			table.draw();
			
			  table = new BaseTable(770, 770, bottomMargin, 300, 280, document, page, true, true);	    
			  baseRow = communsPdf.setRow(table, 10);
			  baseRow = communsPdf.setRow(table, 10);
			  communsPdf.setCell(baseRow, 100,"Solicitud de Seguro de Vida Individual",bgColorA,true, "R", 11, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
			  baseRow = communsPdf.setRow(table, 10);
			  communsPdf.setCell(baseRow, 100,"Personas Físicas",bgColorA,false, "R", 11, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);	
			  table.draw();
			
			
			
		} catch (Exception ex) {
			throw new GeneralServiceException("00001",
					"Ocurrio un error en el servicio ImpresionConstanciaAntiguedad: setEncabezado " + ex.getMessage());
		}

	}
}
