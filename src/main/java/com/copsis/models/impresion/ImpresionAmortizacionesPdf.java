package com.copsis.models.impresion;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDPageContentStream.AppendMode;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import com.copsis.controllers.forms.AmortizacionPdfForm;
import com.copsis.dto.AmortizacionDTO;
import com.copsis.exceptions.GeneralServiceException;
import com.copsis.models.Tabla.BaseTable;
import com.copsis.models.Tabla.ImageUtils;
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
	private float yStart = 670;
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
						BaseTable table2;
						Row<PDPage> baseRow2;

						// Logo
						getLogo(document, page);
						// PRODUCTO
						getEncabezadoPdf(document, page , impresionForm);
						// ENCABEZADO TABLA//
						getEncabezadoTabla(document, page);
						
						
						
						Integer x = 0;
						boolean acomula = false;
						List<AmortizacionDTO> listAmortizacion = impresionForm.getAmortizacion();
						
						// CREACION DE CUERPO // 
						while(x < listAmortizacion.size()) {
							
							AmortizacionDTO amortizacionDTO = listAmortizacion.get(x);

							acomula = true;
							// CREACION CUERPO DE PDF
							table = getCuerpoPdf(document, page, amortizacionDTO, x);

							// SALTO DE PAGINA
							if(isEndOfPage(table)) {

								table.getRows().remove(table.getRows().size()-1);
								table.draw();
								// CRACION NUEVA PAGINA
								page = new PDPage();
								document.addPage(page);
								// Logo
								
								getLogo(document, page);
								// PRODUCTO
								getEncabezadoPdf(document, page, impresionForm);
								// ENCABEZADO TABLA//
								getEncabezadoTabla(document, page);
							
								// BORDE DEL CUERPO DE LA TABLA EN NUEVA PAGINA
								table2 = new BaseTable(610, yStartNewPage, bottomMargin, fullWidth, 20, document, page, true,true);
								baseRow2 = communsPdf.setRow(table2, 575);
								communsPdf.setCell(baseRow2,100, "", black, false, "C", 8, cellStyle, "", paddingHead2, bgColor);
								table2.draw();
								//marcaAgua
								getMarcaAgua(document, page);
								
								acomula = false;
								
							} else {
								if(x == 0) {
									// BORDE DEL CUERPO DE TABLA PRIMER PAGINA
									table2 = new BaseTable(610, yStartNewPage, bottomMargin, fullWidth, 20, document, page, true,true);
									baseRow2 = communsPdf.setRow(table2, 575);
									communsPdf.setCell(baseRow2,100, "", black, false, "C", 8, cellStyle, "", paddingHead2, bgColor);
									table2.draw();
									//marcaAgua
									getMarcaAgua(document, page);
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
						
						if(x == listAmortizacion.size() ) { // insertar informacion
							if(yStart <= 110.0) { // para plazo de 24 meses
								page = new PDPage();
								document.addPage(page);
								// Logo
								getLogo(document, page);
								// PRODUCTO
								getEncabezadoPdf(document, page, impresionForm);
								// BORDE DEL CUERPO DE TABLA
								table2 = new BaseTable(610, yStartNewPage, bottomMargin, fullWidth, 20, document, page, true,true);
								baseRow2 = communsPdf.setRow(table2, 575);
								communsPdf.setCell(baseRow2,100, "", black, false, "C", 8, cellStyle, "", paddingHead2, bgColor);
								table2.draw();
								//marcaAgua
								getMarcaAgua(document, page);
								getInfo(document, page, 620);
							} else {
								getInfo(document, page, yStart);
							}
						}
						output = new ByteArrayOutputStream();
						document.save(output);
						return output.toByteArray();
				}				
				
			}catch (Exception ex) {
				throw new GeneralServiceException(ErrorCode.MSJ_ERROR_00001,ex.getMessage());
			}
		
	}
	
	
	private BaseTable getCuerpoPdf(PDDocument document, PDPage page, AmortizacionDTO amortizacionDTO, int loop) {
		try {
			
			boolean isBold = (loop==0);
			BaseTable table;
			Row<PDPage> baseRow;
			
			table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, 20, document, page, false,true);
			baseRow = communsPdf.setRow(table, 20);
			communsPdf.setCell(baseRow,3, String.valueOf(amortizacionDTO.getId()), black, isBold, "C", 8, cellStyle, "", paddingHead2, bgColor);
			
			communsPdf.setCell(baseRow,1,"$", black, true, "C", 8, cellStyle, "", paddingBody2,bgColor);	
			communsPdf.setCell(baseRow,11,amortizacionDTO.getSeguroDanos().toString().equals("0.0")? "" : formatoDinero(amortizacionDTO.getSeguroDanos()) , black, true, "R", 8, cellStyle, "", paddingBody,bgColor);
			
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
			
			return table;
			
		} catch (Exception e) {
			throw new GeneralServiceException("", e.getMessage());
		}
	}
	
	private void getMarcaAgua(PDDocument document, PDPage page) {
		try (PDPageContentStream content = new PDPageContentStream(document, page, AppendMode.APPEND,true,true )) {

			URL marcaAgua = new URL("https://storage.googleapis.com/sio4/BiiBiiC/MarcaAguaScotia/img_informativo2.png");
			BufferedImage imgMar = ImageIO.read(marcaAgua);
			PDImageXObject pdImage2 = LosslessFactory.createFromImage(document, imgMar);
			
			content.drawImage(pdImage2, 20, 30, 612, 792);
			
		} catch (Exception e) {
		throw new GeneralServiceException("Error:", e.getMessage());
		}
	}
	
	
	private void getEncabezadoTabla(PDDocument document, PDPage page)   {
		try {
			
			BaseTable table;
			Row<PDPage> baseRow;
			yStart = 630;
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
			//getFooter(document, page);
			
		} catch (Exception e) {
			throw new GeneralServiceException("Error=>", e.getMessage());
		}
				
	}
	
	
	private void getEncabezadoPdf(PDDocument document, PDPage page,AmortizacionPdfForm impresionForm) {
		
		try {
			BaseTable table;
			Row<PDPage> baseRow;
			yStart = 730;
			String dateString = new FormatoFecha().getStringFormat(new Date(), "dd MMMM yyyy");
			String[] dateNew = dateString.split("\\s+");
			
			dateString = dateNew[0] + " de " + dateNew[1] + " del " + dateNew[2];
	  
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
			communsPdf.setCell(baseRow,20, "MONTO A FINANCIAR:", black, true, "L", 10, cellStyle, "", paddingHeadData, bgColor);
			communsPdf.setCell(baseRow,22,"$ "+formatoDinero(impresionForm.getMonto()), black, false, "R", 10, cellStyle, "", paddingHeadData,bgColor);
			communsPdf.setCell(baseRow,15, "Descripción:", black, true, "L", 10, cellStyle, "", paddingHeadData2, bgColor);
			communsPdf.setCell(baseRow,43,  ellipsize(impresionForm.getDescripcion()+" "+impresionForm.getModelo(),85), black, false, "L", 10, cellStyle, "", paddingHeadData, bgColor);
			
			
			baseRow = communsPdf.setRow(table, 15);
			communsPdf.setCell(baseRow,27, "PLAZO (meses):", black, true, "L", 10, cellStyle, "", paddingHeadData, bgColor);
			communsPdf.setCell(baseRow,15, impresionForm.getPlazo().toString(), black, false, "R", 10, cellStyle, "", paddingHeadData, bgColor);
			communsPdf.setCell(baseRow,15, "Código Postal:", black, true, "L", 10, cellStyle, "", paddingHeadData2, bgColor);
			communsPdf.setCell(baseRow,43, impresionForm.getCodigoPostal(), black, false, "L", 10, cellStyle, "", paddingHeadData, bgColor);
			
			
			baseRow = communsPdf.setRow(table, 15);
			communsPdf.setCell(baseRow,27, "TASA DE INTERÉS ANUAL:", black, true, "L", 10, cellStyle, "", paddingHeadData, bgColor);
			communsPdf.setCell(baseRow,15, impresionForm.getTasa().toString()+"%", black, false, "R", 10, cellStyle, "", paddingHeadData, bgColor);
			
			baseRow = communsPdf.setRow(table, 15);
			communsPdf.setCell(baseRow,99,dateString, black, false, "R", 8, cellStyle, "", paddingHeadData, bgColor);
			
			table.draw();
			yStart -= table.getHeaderAndDataHeight() + 20;
			
		} catch (Exception e) {
			throw new GeneralServiceException("Error => ", e.getMessage() );
		}
	}
	
	private void getFooter(PDDocument document, PDPage page) {
        try {
        	
        	StringBuilder txt = new StringBuilder();
        	
        	txt.append("* Esta tabla de amortización, es tan sólo una estimación de cómo se comportarían los pagos. \n");
        	txt.append("* Esta cotización es de carácter informativo y sujeto a cambio sin previo aviso. \n");
        	txt.append("* Este documento no tiene ninguna validez oficial y sujeto a autorización de crédito, aplican restricciones. ");
        	
            BaseTable table = new BaseTable(35, 26, 10, fullWidth, 20, document, page, false, true);
            Row<PDPage> baseRow = communsPdf.setRow(table, 15);
            communsPdf.setCell(baseRow,100, Sio4CommunsPdf.eliminaHtmlTags3(txt.toString()), black, false, "L", 8, cellStyle, "", paddingHeadData, bgColor);
            table.draw();

        } catch (Exception ex) {
        	throw new GeneralServiceException("Error => ", ex.getMessage() );
        }
    }
	
	private void getLogo(PDDocument document, PDPage page)   {
		try {
			BaseTable table;
			Row<PDPage> baseRow;
			yStart = 780;
			height = yStart;
			
			table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, 20, document, page, false,true);
			baseRow = communsPdf.setRow(table, 35);
			communsPdf.setCell(baseRow,30, ImageUtils.readImage("https://storage.googleapis.com/sio4/BiiBiiC/LogoScotiaBank.png"));
			table.draw();
			
		} catch (Exception e) {
			throw new GeneralServiceException("Error=>", e.getMessage());
		}
	}
	
	private void getInfo(PDDocument document, PDPage page, float inicio) {
		try {
			StringBuilder txt = new StringBuilder();
			txt.append("<br>");
			txt.append("<br>");
        	txt.append("• Las mensualidades incluyen intereses, IVA ,seguro de daños con cobertura amplia y seguro de vida. \n");
        	txt.append("• En la primera mensualidad se cobra solamente comisión por apertura más intereses e IVA \n");
        	txt.append("• Cotizaciones de carácter informativo y sujetas a cambio sin previo aviso. \n");
        	txt.append("• Este documento no tiene ninguna validez oficial. \n");
        	txt.append("• El monto del seguro comprende 12 meses. Al terminar este periodo se renueva en forma automática. \n");
        	txt.append("• Sujeto a autorización de crédito. \n");
        	txt.append("• Se solicitará la contratación de los seguros correspondientes. Scotiabank \n");
        	txt.append("   reconoce el derecho innegable que tiene el Cliente de contratar los \n");
        	txt.append("   productos y/o servicios adicionales o ligados a la operación o servicio a \n");
        	txt.append("   través de un tercero independiente. \n ");        	
        	txt.append("<b>Documentación requerida**: </b> \n");
        	txt.append("- Identificación oficial \n");
        	txt.append("- Comprobante de domicilio \n");
        	txt.append("- Comprobante de ingresos \n");
        	txt.append("<i>**Scotiabank Inverlat se reserva el derecho de solicitar información adicional. <i>" );
            BaseTable table = new BaseTable(inicio, 26, 10, fullWidth, 20, document, page, false, true);
            Row<PDPage> baseRow = communsPdf.setRow(table, 15);
            communsPdf.setCell(baseRow,100, Sio4CommunsPdf.eliminaHtmlTags3(txt.toString()), black, false, "L", 9, cellStyle, "", paddingHeadData, bgColor);
            table.draw();
			
		}catch (Exception e) {
			throw new GeneralServiceException("Error=>", e.getMessage());
		}
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
