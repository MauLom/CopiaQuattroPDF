package com.copsis.models.impresion;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;

import com.copsis.controllers.forms.AmortizacionPdfForm;
import com.copsis.dto.AmortizacionDTO;
import com.copsis.exceptions.GeneralServiceException;
import com.copsis.models.Tabla.BaseTable;
import com.copsis.models.Tabla.LineStyle;
import com.copsis.models.Tabla.Row;
import com.copsis.models.Tabla.Sio4CommunsPdf;


public class ImpresionAmortizacionesPdf {
	
	private  List<LineStyle> borderTable = new ArrayList<>();
	private  List<LineStyle> cellStyle = new ArrayList<>();
	
	
	private List<Float> paddingHeadData = new ArrayList<>();
	private List<Float> paddingHeadData2 = new ArrayList<>();

	private List<Float> paddingHead = new ArrayList<>();
	private List<Float> paddingHead2 = new ArrayList<>();
	
	private List<Float> paddingBody = new ArrayList<>();
	private List<Float> paddingBody2 = new ArrayList<>();
	private float yStartNewPage = 760;
	private float yStart = 770;
	private float bottomMargin = 26;
	private float fullWidth = 562;
	float height = 0;
	float heightBorder = 0;
	private final Color bgColor = new Color(255, 255, 255, 0 );
	private final Color black = new Color(0, 0, 0, 1);
	private final LineStyle lineStyle = new LineStyle(black,0);
	private Sio4CommunsPdf communsPdf = new Sio4CommunsPdf();
	
	public byte[] buildPDF(AmortizacionPdfForm impresionForm) {
						
				//** BODY **// L,R,T,B
				
				cellStyle.add(lineStyle);
				cellStyle.add(lineStyle);
				cellStyle.add(lineStyle);
				cellStyle.add(lineStyle);
				
				borderTable.add(lineStyle);
				borderTable.add(lineStyle);
				borderTable.add(lineStyle);
				borderTable.add(lineStyle);
				
				
				//**ENCABEZADO Data**/
				// Paddings son 4 L,R,T,B
				paddingHeadData.add(4f);
				paddingHeadData.add(0f);
				paddingHeadData.add(4f);
				paddingHeadData.add(0f);
				
				//**ENCABEZADO Data**/
				// Paddings son 4 L,R,T,B
				paddingHeadData2.add(15f);
				paddingHeadData2.add(0f);
				paddingHeadData2.add(4f);
				paddingHeadData2.add(0f);
				

				//**ENCABEZADO **/
				// Paddings son 4 L,R,T,B
				paddingHead.add(4f);
				paddingHead.add(0f);
				paddingHead.add(2f);
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

			try {
				ByteArrayOutputStream output;
				try (PDDocument document = new PDDocument()) {
					try{
						
						output = new ByteArrayOutputStream();
						PDPage page = new PDPage();
						document.addPage(page);
						
						BaseTable table;
						Row<PDPage> baseRow;
						BaseTable table2;
						Row<PDPage> baseRow2;

						// PRODUCTO
						getEncabezado(document, page , impresionForm);
						// ENCABEZADO TABLA//
						headerTable(document, page);
						
						boolean isBold = false;
						
						Integer x = 0;
						boolean acomula = false;
						List<AmortizacionDTO> listAmortizacion = impresionForm.getAmortizacion();
						
						// CREACION DE CUERPO // 
						while(x < listAmortizacion.size()) {
							
							isBold = (x==0);
							acomula = true;
							AmortizacionDTO amortizacionDTO = listAmortizacion.get(x);
							
							table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, 20, document, page, false,true);
							baseRow = communsPdf.setRow(table, 20);
							communsPdf.setCell(baseRow,3, amortizacionDTO.getId().toString(), black, isBold, "C", 8, cellStyle, "", paddingHead2, bgColor);
							
							communsPdf.setCell(baseRow,1,"$", black, true, "C", 8, cellStyle, "", paddingBody2,bgColor);	
							communsPdf.setCell(baseRow,11,amortizacionDTO.getSeguroDanos().toString().equals("0.0")? "" : amortizacionDTO.getSeguroDanos().toString()  , black, true, "R", 8, cellStyle, "", paddingBody,bgColor);
							
							communsPdf.setCell(baseRow,1,"$", black, true, "C", 8, cellStyle, "", paddingBody2,bgColor);
							communsPdf.setCell(baseRow,12,amortizacionDTO.getAportacionCapital().toString().equals("0.0")? "-" : amortizacionDTO.getAportacionCapital().toString() , black, isBold, "R", 8, cellStyle, "",paddingBody, bgColor);
							
							communsPdf.setCell(baseRow,1,"$", black, isBold, "C", 8, cellStyle, "", paddingBody2,bgColor);
							communsPdf.setCell(baseRow,11,amortizacionDTO.getCapital().toString().equals("0.0")? "-": amortizacionDTO.getCapital().toString() , black, isBold, "R", 8, cellStyle, "", paddingBody,bgColor);
							
							communsPdf.setCell(baseRow,1,"$", black, isBold, "C", 8, cellStyle, "", paddingBody2,bgColor);
							communsPdf.setCell(baseRow,11,amortizacionDTO.getInteres().toString().equals("0.0")? "-" : amortizacionDTO.getInteres().toString(), black, isBold, "R", 8, cellStyle, "", paddingBody,bgColor);
							
							communsPdf.setCell(baseRow,1,"$", black, isBold, "C", 8, cellStyle, "", paddingBody2,bgColor);
							communsPdf.setCell(baseRow,11,amortizacionDTO.getIva().toString().equals("0.0")? "-":amortizacionDTO.getIva().toString(), black, isBold, "R", 8, cellStyle, "", paddingBody,bgColor);
							
							communsPdf.setCell(baseRow,1,"$", black, true, "C", 8, cellStyle, "", paddingBody2,bgColor);
							communsPdf.setCell(baseRow,11,amortizacionDTO.getPago().toString().equals("0.0")?"-":amortizacionDTO.getPago().toString(), black, true, "R", 8, cellStyle, "", paddingBody,bgColor);
							
							communsPdf.setCell(baseRow,1,"$", black, isBold, "C", 8, cellStyle, "", paddingBody2,bgColor);
							communsPdf.setCell(baseRow,11,amortizacionDTO.getAbonoCapital().toString().equals("0.0")?"-":amortizacionDTO.getAbonoCapital().toString(), black, isBold, "R", 8, cellStyle, "", paddingBody, bgColor);
							
							communsPdf.setCell(baseRow,1,"$", black, isBold, "C", 8, cellStyle, "", paddingBody2,bgColor);
							communsPdf.setCell(baseRow,11,amortizacionDTO.getCapitalNuevo().toString().equals("0.0")?"-":amortizacionDTO.getCapitalNuevo().toString() , black, isBold, "R", 8, cellStyle, "", paddingBody, bgColor);
							
							// SALTO DE PAGINA
							if(isEndOfPage(table)) {
								
								table.getRows().remove(table.getRows().size()-1);
								table.draw();
								// CRACION NUEVA PAGINA
								page = new PDPage();
								document.addPage(page);
																
								// PRODUCTO
								getEncabezado(document, page, impresionForm);
								// ENCABEZADO TABLA//
								headerTable(document, page);
							
								// BORDE DEL CUERPO DE LA TABLA EN NUEVA PAGINA
								table2 = new BaseTable(650, yStartNewPage, bottomMargin, fullWidth, 20, document, page, true,true);
								baseRow2 = communsPdf.setRow(table2, 620);
								communsPdf.setCell(baseRow2,100, "", black, false, "C", 8, borderTable, "", paddingHead2, bgColor);
								table2.draw();	
								
								acomula = false;
								
							} else {
								if(x == 0) {
									// BORDE DEL CUERPO DE TABLA PRIMER PAGINA
									table2 = new BaseTable(650, yStartNewPage, bottomMargin, fullWidth, 20, document, page, true,true);
									baseRow2 = communsPdf.setRow(table2, 620 );
									communsPdf.setCell(baseRow2,100, "", black, false, "C", 8, borderTable, "", paddingHead2, bgColor);
									table2.draw();	
								}
								// PINTA LA DATA DE LA TABLA
								table.draw();
								yStart -= table.getHeaderAndDataHeight();
								heightBorder = height -yStart;
							}
							if(acomula) {								
								x++;
							} 
						}
						document.save(new File("/home/development/Documents/prueba"));

						output = new ByteArrayOutputStream();
						document.save(output);
						return output.toByteArray();
					} finally {
						document.close();
					}
				}				
				
			}catch (Exception ex) {
				throw new GeneralServiceException("00001","Ocurrio un error en el servicio ImpresionInter: " + ex.getMessage());
			}
		
	}
	
	private void headerTable(PDDocument document, PDPage page) throws IOException   {
		
		BaseTable table;
		Row<PDPage> baseRow;
		yStart = 670;
		height = yStart;
		
		
		table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, 20, document, page, true,true);
		baseRow = communsPdf.setRow(table, 20 );
		communsPdf.setCell(baseRow,100, "", black, false, "C", 8, borderTable, "", paddingHead2, bgColor);
		table.draw();
		
		table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, 20, document, page, false,true);
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
		yStart -= table.getHeaderAndDataHeight();
		table.draw();
		
	}
	
	
	private void getEncabezado(PDDocument document, PDPage page,AmortizacionPdfForm impresionForm) throws IOException   {
		
		BaseTable table;
		Row<PDPage> baseRow;
		yStart = 770;
		table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, 20, document, page, false,true);

		baseRow = communsPdf.setRow(table, 20);
		communsPdf.setCell(baseRow,10, "Producto: ", black, true, "L", 10, cellStyle, "", paddingHeadData, bgColor);
		communsPdf.setCell(baseRow,85, impresionForm.getProducto(), black, false, "L", 10, cellStyle, "", paddingHeadData, bgColor);
		yStart -= table.getHeaderAndDataHeight();
		table.draw();
		
		
		table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, 20, document, page, true,true);
		baseRow = communsPdf.setRow(table, 60 );
		communsPdf.setCell(baseRow,41, "", black, false, "C", 8, borderTable, "", paddingHead2, bgColor);
		table.draw();
		
		
		table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, 20, document, page, false,true);
		baseRow = communsPdf.setRow(table, 20);
		communsPdf.setCell(baseRow,25, "MONTO A FINANCIAR", black, true, "L", 10, cellStyle, "", paddingHeadData, bgColor);
		communsPdf.setCell(baseRow,15,"$ "+impresionForm.getMonto().toString(), black, false, "R", 10, cellStyle, "", paddingHeadData,bgColor);
		communsPdf.setCell(baseRow,60,"DATOS CONSIDERADOS PARA EL SEGURO", black, true, "L", 10, cellStyle, "", paddingHeadData2,bgColor);
		
		baseRow = communsPdf.setRow(table, 20);
		communsPdf.setCell(baseRow,25, "PLAZO (meses)", black, true, "L", 10, cellStyle, "", paddingHeadData, bgColor);
		communsPdf.setCell(baseRow,15, impresionForm.getPlazo().toString(), black, false, "R", 10, cellStyle, "", paddingHeadData, bgColor);
		communsPdf.setCell(baseRow,16, "Descripción:", black, true, "L", 10, cellStyle, "", paddingHeadData2, bgColor);
		communsPdf.setCell(baseRow,46, impresionForm.getDescripcion()+" "+impresionForm.getModelo(), black, false, "L", 10, cellStyle, "", paddingHeadData, bgColor);
		
		baseRow = communsPdf.setRow(table, 20);
		communsPdf.setCell(baseRow,25, "TASA DE INTERÉS ANUAL", black, true, "L", 10, cellStyle, "", paddingHeadData, bgColor);
		communsPdf.setCell(baseRow,15, impresionForm.getTasa().toString()+"%", black, false, "R", 10, cellStyle, "", paddingHeadData, bgColor);
		communsPdf.setCell(baseRow,16, "Código Postal:", black, true, "L", 10, cellStyle, "", paddingHeadData2, bgColor);
		communsPdf.setCell(baseRow,50, impresionForm.getCodigoPostal(), black, false, "L", 10, cellStyle, "", paddingHeadData, bgColor);
		
		table.draw();
		yStart -= table.getHeaderAndDataHeight() + 20;
		
	}

	
	private boolean isEndOfPage(BaseTable table) {
        float currentY = yStart - table.getHeaderAndDataHeight();
        return currentY <= bottomMargin;
    }
	
	
	

}
