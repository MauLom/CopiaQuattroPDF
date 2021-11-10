package com.copsis.models.impresion;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;

import com.copsis.controllers.forms.ImpresionForm;
import com.copsis.dto.AmortizacionDTO;
import com.copsis.exceptions.GeneralServiceException;
import com.copsis.models.Tabla.BaseTable;
import com.copsis.models.Tabla.LineStyle;
import com.copsis.models.Tabla.Row;
import com.copsis.models.Tabla.Sio4CommunsPdf;


//document.save(new File("/home/development/Documents"));

public class ImpresionAmortizacionesPdf {
	
	private  List<LineStyle> borderTable = new ArrayList<>();
	private  List<LineStyle> cellStyle = new ArrayList<>();
	

	private List<Float> paddingHead = new ArrayList<>();
	private List<Float> paddingHead2 = new ArrayList<>();
	
	private List<Float> paddingBody = new ArrayList<>();
	private List<Float> paddingBody2 = new ArrayList<>();
	private float yStartNewPage = 760;
	private float yStart = 770;
	private float bottomMargin = 26;
	private float fullWidth = 562;
	private final Color bgColor = new Color(255, 255, 255, 0);
	private final Color black = new Color(0, 0, 0, 1);
	private final LineStyle lineStyle = new LineStyle(black,0);
	private Sio4CommunsPdf communsPdf = new Sio4CommunsPdf();
	public byte[] buildPDF(ImpresionForm impresionForm) {
						
				//** BODY **// L,R,T,B
				
				cellStyle.add(lineStyle);
				cellStyle.add(lineStyle);
				cellStyle.add(lineStyle);
				cellStyle.add(lineStyle);
				
				borderTable.add(lineStyle);
				borderTable.add(lineStyle);
				borderTable.add(lineStyle);
				borderTable.add(lineStyle);
				
				

				//**ENCABEZADO **/
				// Paddings son 4 L,R,T,B
				paddingHead.add(4f);
				paddingHead.add(0f);
				paddingHead.add(4f);
				paddingHead.add(0f);

				paddingHead2.add(0f);
				paddingHead2.add(0f);
				paddingHead2.add(7f);
				paddingHead2.add(0f);
				
				/** CUERPO **/
				// Paddings son 4 L,R,T,B
				paddingBody.add(0f);
				paddingBody.add(7f);
				paddingBody.add(7f);
				paddingBody.add(0f);
				
				paddingBody2.add(0f);
				paddingBody2.add(0f);
				paddingBody2.add(7f);
				paddingBody2.add(0f);

		byte[] pdfArray = null;
			try {
				ByteArrayOutputStream output;
				try (PDDocument document = new PDDocument()) {
					try {
						float height = 0;
						output = new ByteArrayOutputStream();
						PDPage page = new PDPage();
						document.addPage(page);
						
						BaseTable header;
						Row<PDPage> baseHeaderRow;
						BaseTable table;
						Row<PDPage> baseRow;
						BaseTable table2;
						Row<PDPage> baseRow2;
						
						header = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, 20, document, page, true,true);
						baseHeaderRow = communsPdf.setRow(header, 20 );
						communsPdf.setCell(baseHeaderRow,100, "", black, false, "C", 8, borderTable, "", paddingHead2, bgColor);
						header.draw();
						
						table = headerTable(document, page);
						table.draw();

						yStart -= table.getHeaderAndDataHeight();
						height = yStart;
						
						table2 = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, 20, document, page, true,true);
						table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, 20, document, page, false,true);
						List<AmortizacionDTO> listAmortizacion = impresionForm.getAmortizacion(); 
						for (AmortizacionDTO amortizacionDTO : listAmortizacion) {
							
							baseRow = communsPdf.setRow(table, 20);
							communsPdf.setCell(baseRow,3, amortizacionDTO.getId().toString(), black, false, "C", 8, cellStyle, "", paddingHead2, bgColor);
							
							communsPdf.setCell(baseRow,1,"$", black, false, "C", 8, cellStyle, "", paddingBody2,bgColor);	
							communsPdf.setCell(baseRow,11,amortizacionDTO.getSeguroDanos().toString(), black, false, "R", 8, cellStyle, "", paddingBody,bgColor);
							
							communsPdf.setCell(baseRow,1,"$", black, false, "C", 8, cellStyle, "", paddingBody2,bgColor);
							communsPdf.setCell(baseRow,12,amortizacionDTO.getAportacionCapital().toString(), black, false, "R", 8, cellStyle, "",paddingBody, bgColor);
							
							communsPdf.setCell(baseRow,1,"$", black, false, "C", 8, cellStyle, "", paddingBody2,bgColor);
							communsPdf.setCell(baseRow,11,amortizacionDTO.getCapital().toString(), black, false, "R", 8, cellStyle, "", paddingBody,bgColor);
							
							communsPdf.setCell(baseRow,1,"$", black, false, "C", 8, cellStyle, "", paddingBody2,bgColor);
							communsPdf.setCell(baseRow,11,amortizacionDTO.getInteres().toString(), black, false, "R", 8, cellStyle, "", paddingBody,bgColor);
							
							communsPdf.setCell(baseRow,1,"$", black, false, "C", 8, cellStyle, "", paddingBody2,bgColor);
							communsPdf.setCell(baseRow,11,amortizacionDTO.getIva().toString(), black, false, "R", 8, cellStyle, "", paddingBody,bgColor);
							
							communsPdf.setCell(baseRow,1,"$", black, false, "C", 8, cellStyle, "", paddingBody2,bgColor);
							communsPdf.setCell(baseRow,11,amortizacionDTO.getPago().toString(), black, false, "R", 8, cellStyle, "", paddingBody,bgColor);
							
							communsPdf.setCell(baseRow,1,"$", black, false, "C", 8, cellStyle, "", paddingBody2,bgColor);
							communsPdf.setCell(baseRow,11,amortizacionDTO.getAbonoCapital().toString(), black, false, "R", 8, cellStyle, "", paddingBody, bgColor);
							
							communsPdf.setCell(baseRow,1,"$", black, false, "C", 8, cellStyle, "", paddingBody2,bgColor);
							communsPdf.setCell(baseRow,11,amortizacionDTO.getCapitalNuevo().toString() , black, false, "R", 8, cellStyle, "", paddingBody, bgColor);
							
						
						}

						yStart -= table.getHeaderAndDataHeight();						
						height = height - yStart;
						
						baseRow2 = communsPdf.setRow(table2, height+30);
						communsPdf.setCell(baseRow2,100, "", black, false, "C", 8, borderTable, "", paddingBody, bgColor);
						
						table2.draw();
						table.draw();
						
						document.save(new File("/home/development/Documents/prueba"));
						return pdfArray;
					} finally {
//						document.close();
					}
				}				
				
			}catch (Exception ex) {
				throw new GeneralServiceException("00001",
						"Ocurrio un error en el servicio ImpresionInter: " + ex.getMessage());
			}
		
	}
	
	private BaseTable headerTable(PDDocument document, PDPage page) throws IOException   {
		
		BaseTable table;
		Row<PDPage> baseRow;
			
		table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, 30, document, page, false,true);

		baseRow = communsPdf.setRow(table, 20);
		communsPdf.setCell(baseRow,3, "", black, false, "C", 8, cellStyle, "", paddingHead, bgColor);
		communsPdf.setCell(baseRow,12,"Seguro de daños - vida", black, true, "C", 8, cellStyle, "", paddingHead,bgColor);
		communsPdf.setCell(baseRow,13,"APORTACIÓN A CAPITAL", black, true, "C", 8, cellStyle, "",paddingHead, bgColor);
		communsPdf.setCell(baseRow,12,"CAPITAL", black, true, "C", 8, cellStyle, "", paddingHead2,bgColor);
		communsPdf.setCell(baseRow,12,"INTERÉS", black, true, "C", 8, cellStyle, "", paddingHead2,bgColor);
		communsPdf.setCell(baseRow,12,"IVA", black, true, "C", 8, cellStyle, "", paddingHead2,bgColor);
		communsPdf.setCell(baseRow,12,"PAGO", black, true, "C", 8, cellStyle, "", paddingHead2,bgColor);
		communsPdf.setCell(baseRow,12,"ABONO A CAPITAL", black, true, "C", 8, cellStyle, "", paddingHead, bgColor);
		communsPdf.setCell(baseRow,12,"CAPITAL NUEVO", black, true, "C", 8, cellStyle, "", paddingHead, bgColor);
				
		return table;
		
	}
	
	private boolean isEndOfPage(BaseTable table) {
        float currentY = yStart - table.getHeaderAndDataHeight();
        boolean isEndOfPage = currentY <= bottomMargin;

        return isEndOfPage;
    }
	
	
	

}
