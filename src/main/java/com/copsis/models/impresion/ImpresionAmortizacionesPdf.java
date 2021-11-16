package com.copsis.models.impresion;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
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
import com.copsis.utils.ErrorCode;
import com.copsis.utils.FormatoFecha;

public class ImpresionAmortizacionesPdf {
	
	private  List<LineStyle> borderWhite = new ArrayList<>();
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
	private float fullWidth = 564;
	float height = 0;
	float heightBorder = 0;
	private final Color bgColor = new Color(255, 255, 255, 0 );
	private final Color black = new Color(0, 0, 0, 1);
	private final LineStyle lineStyle = new LineStyle(black,0);
	private final LineStyle lineStyleWhite = new LineStyle(bgColor,0);
	private Sio4CommunsPdf communsPdf = new Sio4CommunsPdf();
	
	public byte[] buildPDF(AmortizacionPdfForm impresionForm) {
						
				//** BODY **// L,R,T,B
				
				cellStyle.add(lineStyle);
				cellStyle.add(lineStyle);
				cellStyle.add(lineStyle);
				cellStyle.add(lineStyle);
				
				borderWhite.add(lineStyleWhite);
				borderWhite.add(lineStyleWhite);
				borderWhite.add(lineStyleWhite);
				borderWhite.add(lineStyleWhite);
				
				
				//**ENCABEZADO Data**/
				// Paddings son 4 L,R,T,B
				paddingHeadData.add(4f);
				paddingHeadData.add(0f);
				paddingHeadData.add(4f);
				paddingHeadData.add(0f);
				
				//**ENCABEZADO Data**/
				// Paddings son 4 L,R,T,B
				paddingHeadData2.add(10f);
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
							communsPdf.setCell(baseRow,11,amortizacionDTO.getSeguroDanos().toString().equals("0.0")? "" : formatoDinero(amortizacionDTO.getSeguroDanos())  , black, true, "R", 8, cellStyle, "", paddingBody,bgColor);
							
							communsPdf.setCell(baseRow,1,"$", black, true, "C", 8, cellStyle, "", paddingBody2,bgColor);
							communsPdf.setCell(baseRow,12,amortizacionDTO.getAportacionCapital().toString().equals("0.0")? "-" : formatoDinero(amortizacionDTO.getAportacionCapital()) , black, isBold, "R", 8, cellStyle, "",paddingBody, bgColor);
							
							communsPdf.setCell(baseRow,1,"$", black, isBold, "C", 8, cellStyle, "", paddingBody2,bgColor);
							communsPdf.setCell(baseRow,11,amortizacionDTO.getCapital().toString().equals("0.0")? "-": formatoDinero(amortizacionDTO.getCapital()) , black, isBold, "R", 8, cellStyle, "", paddingBody,bgColor);
							
							communsPdf.setCell(baseRow,1,"$", black, isBold, "C", 8, cellStyle, "", paddingBody2,bgColor);
							communsPdf.setCell(baseRow,11,amortizacionDTO.getInteres().toString().equals("0.0")? "-" : formatoDinero(amortizacionDTO.getInteres()), black, isBold, "R", 8, cellStyle, "", paddingBody,bgColor);
							
							communsPdf.setCell(baseRow,1,"$", black, isBold, "C", 8, cellStyle, "", paddingBody2,bgColor);
							communsPdf.setCell(baseRow,11,amortizacionDTO.getIva().toString().equals("0.0")? "-":formatoDinero(amortizacionDTO.getIva()), black, isBold, "R", 8, cellStyle, "", paddingBody,bgColor);
							
							communsPdf.setCell(baseRow,1,"$", black, true, "C", 8, cellStyle, "", paddingBody2,bgColor);
							communsPdf.setCell(baseRow,11,amortizacionDTO.getPago().toString().equals("0.0")?"-":formatoDinero(amortizacionDTO.getPago()), black, true, "R", 8, cellStyle, "", paddingBody,bgColor);
							
							communsPdf.setCell(baseRow,1,"$", black, isBold, "C", 8, cellStyle, "", paddingBody2,bgColor);
							communsPdf.setCell(baseRow,11,amortizacionDTO.getAbonoCapital().toString().equals("0.0")?"-":formatoDinero(amortizacionDTO.getAbonoCapital()), black, isBold, "R", 8, cellStyle, "", paddingBody, bgColor);
							
							communsPdf.setCell(baseRow,1,"$", black, isBold, "C", 8, cellStyle, "", paddingBody2,bgColor);
							communsPdf.setCell(baseRow,11,amortizacionDTO.getCapitalNuevo().toString().equals("0.0")?"-":formatoDinero(amortizacionDTO.getCapitalNuevo()) , black, isBold, "R", 8, cellStyle, "", paddingBody, bgColor);
							
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
								communsPdf.setCell(baseRow2,100, "", black, false, "C", 8, cellStyle, "", paddingHead2, bgColor);
								table2.draw();	
								
								acomula = false;
								
							} else {
								if(x == 0) {
									// BORDE DEL CUERPO DE TABLA PRIMER PAGINA
									table2 = new BaseTable(650, yStartNewPage, bottomMargin, fullWidth, 20, document, page, true,true);
									baseRow2 = communsPdf.setRow(table2, 620 );
									communsPdf.setCell(baseRow2,100, "", black, false, "C", 8, cellStyle, "", paddingHead2, bgColor);
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
						output = new ByteArrayOutputStream();
						document.save(output);
						return output.toByteArray();
				}				
				
			}catch (Exception ex) {
				throw new GeneralServiceException(ErrorCode.MSJ_ERROR_00000,ex.getMessage());
			}
		
	}
	
	private void headerTable(PDDocument document, PDPage page) throws IOException   {
		
		BaseTable table;
		Row<PDPage> baseRow;
		yStart = 670;
		height = yStart;
		
		
		table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, 20, document, page, true,true);
		baseRow = communsPdf.setRow(table, 20 );
		communsPdf.setCell(baseRow,100, "", black, false, "C", 8, cellStyle, "", paddingHead2, bgColor);
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
		String dateString = new FormatoFecha().getStringFormat(new Date(), "dd MMMM yyyy");
  
		table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, 20, document, page, true,true);
		baseRow = communsPdf.setRow(table, 87);
		communsPdf.setCell(baseRow,43, "", black, false, "C", 8, cellStyle, "", paddingHead2, bgColor);
		communsPdf.setCell(baseRow,57, "", black, false, "C", 8, cellStyle, "", paddingHead2, bgColor);
		table.draw();
		
		table = new BaseTable(yStart-2, yStartNewPage, bottomMargin, fullWidth, 20, document, page, false,true);
		baseRow = communsPdf.setRow(table, 15);
		communsPdf.setCell(baseRow,10, "Producto: ", black, true, "L", 10, cellStyle, "", paddingHeadData, bgColor);
		communsPdf.setCell(baseRow,32, impresionForm.getProducto(), black, false, "L", 10, cellStyle, "", paddingHeadData, bgColor);
		communsPdf.setCell(baseRow,58,"DATOS CONSIDERADOS PARA EL SEGURO", black, true, "L", 10, cellStyle, "", paddingHeadData2,bgColor);

		baseRow = communsPdf.setRow(table, 15);
		communsPdf.setCell(baseRow,20, "MONTO A FINANCIAR", black, true, "L", 10, cellStyle, "", paddingHeadData, bgColor);
		communsPdf.setCell(baseRow,22,"$ "+formatoDinero(impresionForm.getMonto()), black, false, "R", 10, cellStyle, "", paddingHeadData,bgColor);
		communsPdf.setCell(baseRow,15, "Descripción:", black, true, "L", 10, cellStyle, "", paddingHeadData2, bgColor);
		communsPdf.setCell(baseRow,43,  ellipsize(impresionForm.getDescripcion()+" "+impresionForm.getModelo(),85), black, false, "L", 10, cellStyle, "", paddingHeadData, bgColor);
		
		
		baseRow = communsPdf.setRow(table, 15);
		communsPdf.setCell(baseRow,27, "PLAZO (meses)", black, true, "L", 10, cellStyle, "", paddingHeadData, bgColor);
		communsPdf.setCell(baseRow,15, impresionForm.getPlazo().toString(), black, false, "R", 10, cellStyle, "", paddingHeadData, bgColor);
		communsPdf.setCell(baseRow,15, "Código Postal:", black, true, "L", 10, cellStyle, "", paddingHeadData2, bgColor);
		communsPdf.setCell(baseRow,43, impresionForm.getCodigoPostal(), black, false, "L", 10, cellStyle, "", paddingHeadData, bgColor);
		
		
		baseRow = communsPdf.setRow(table, 15);
		communsPdf.setCell(baseRow,27, "TASA DE INTERÉS ANUAL", black, true, "L", 10, cellStyle, "", paddingHeadData, bgColor);
		communsPdf.setCell(baseRow,15, impresionForm.getTasa().toString()+"%", black, false, "R", 10, cellStyle, "", paddingHeadData, bgColor);
		
		baseRow = communsPdf.setRow(table, 15);
		communsPdf.setCell(baseRow,99,dateString +" Válido 15 días apartir de su impresión.", black, false, "R", 8, cellStyle, "", paddingHeadData, bgColor);
		
		table.draw();
		yStart -= table.getHeaderAndDataHeight() + 20;
		
	}
	
	private String formatoDinero(Double valor) {
		return String.format("%,.2f", valor);
	}
	

	private static int textWidth(String str) {
		String mask = "[^iIl1\\.,']";
	    return (str.length() - str.replaceAll(mask, "").length() / 2);
	}

	public static String ellipsize(String text, int max) {
	    if (textWidth(text) <= max)
	        return text;
	    int end = text.lastIndexOf(' ', max - 3);
	    if (end == -1)
	        return text.substring(0, max-3) + "...";
	    int newEnd = end;
	    do {
	        end = newEnd;
	        newEnd = text.indexOf(' ', end + 1);
	        if (newEnd == -1)
	            newEnd = text.length();
	    } while (textWidth(text.substring(0, newEnd) + "...") < max);

	    return text.substring(0, end) + "...";
	}
	
	private boolean isEndOfPage(BaseTable table) {
        float currentY = yStart - table.getHeaderAndDataHeight();
        return currentY <= bottomMargin;
    }
	
	
	

}
