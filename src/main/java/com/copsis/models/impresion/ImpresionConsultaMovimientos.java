package com.copsis.models.impresion;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDPageContentStream.AppendMode;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import com.copsis.controllers.forms.MovimientosForm;
import com.copsis.dto.AmortizacionDTO;
import com.copsis.dto.MovimientosDTO;
import com.copsis.exceptions.GeneralServiceException;
import com.copsis.models.Tabla.BaseTable;
import com.copsis.models.Tabla.ImageUtils;
import com.copsis.models.Tabla.LineStyle;
import com.copsis.models.Tabla.Row;
import com.copsis.models.Tabla.Sio4CommunsPdf;
import com.copsis.utils.ErrorCode;

public class ImpresionConsultaMovimientos {
	
	private  List<LineStyle> borderWhite = new ArrayList<>();
	private  List<LineStyle> cellStyle = new ArrayList<>();
	private  List<LineStyle> cellStyle2 = new ArrayList<>();
	
	
	private List<Float> paddingHeadData = new ArrayList<>();
	private List<Float> paddingHeadData2 = new ArrayList<>();

	private List<Float> paddingHead = new ArrayList<>();
	private List<Float> paddingHead2 = new ArrayList<>();
	
	private List<Float> paddingBody = new ArrayList<>();
	private List<Float> paddingBody2 = new ArrayList<>();
	private float yStartNewPage = 720;
	private float yStart = 730;
	private float bottomMargin = 26;
	private float fullWidth = 564;
	float height = 0;
	float heightBorder = 0;
	private final Color bgColor = new Color(255, 255, 255, 0 );
	private final Color bgRow = new Color(246, 248, 253, 1);
	
	private final Color black = new Color(0, 0, 0, 1);
	private final LineStyle lineStyle2 = new LineStyle(bgRow,0);
	private final LineStyle lineStyle = new LineStyle(black,0);
	private final LineStyle lineStyleWhite = new LineStyle(bgColor,0);
	private Sio4CommunsPdf communsPdf = new Sio4CommunsPdf();
	
	
	public byte[] buildPDF(MovimientosForm movimientosForm) {
		
		//** BODY **// L,R,T,B
		
		cellStyle.add(lineStyle);
		cellStyle.add(lineStyle);
		cellStyle.add(lineStyle);
		cellStyle.add(lineStyle);
		
		cellStyle2.add(lineStyle2);
		cellStyle2.add(lineStyle2);
		cellStyle2.add(lineStyle2);
		cellStyle2.add(lineStyle2);
		
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
				
				// ENCABEZADO TABLA//
				yStart = getEncabezadoTabla(document, page);
				
				Integer x = 0;
				boolean acomula = false;
				List<MovimientosDTO> listadoMovimientos = movimientosForm.getMovimientos();
				
				//Creacion del cuerpo
				while (x < listadoMovimientos.size()) {
					
					MovimientosDTO movimientoDTO = listadoMovimientos.get(x);
					
					acomula = true;
					// CREACION CUERPO DE PDF
					table = getCuerpoPdf(document, page, movimientoDTO, x);
					
					// SALTO DE PAGINA
					if(isEndOfPage(table)) {
						table.getRows().remove(table.getRows().size()-1);
						table.draw();
						// CRACION NUEVA PAGINA
						page = new PDPage();
						document.addPage(page);
						yStart = 730;
						// ENCABEZADO TABLA//
						getEncabezadoTabla(document, page);
						//marcaAgua
						getMarcaAgua(document, page);
						acomula = false;
						
					}else {
						if(x == 0) {
							//marcaAgua
							getMarcaAgua(document, page);
						}
						table.draw();
						yStart -= table.getHeaderAndDataHeight();
					}
					
					if(acomula) {								
						x++;
					} 
				}
				
				output = new ByteArrayOutputStream();
				document.save(output);
				//document.save(new File("/home/mduque/Documentos/archivo.pdf"));
				return output.toByteArray();
			}
			
		}catch (Exception ex) {
			throw new GeneralServiceException(ErrorCode.MSJ_ERROR_00001,ex.getMessage());
		}
	}
	
	
	private float getEncabezadoTabla(PDDocument document, PDPage page)   {
		try {
			
			BaseTable table;
			Row<PDPage> baseRow;
			
			table = new BaseTable((yStart-20), yStartNewPage, bottomMargin, fullWidth, 20, document, page, false,true);
			baseRow = communsPdf.setRow(table, 20);
			communsPdf.setCell(baseRow,12,"Póliza", black, true, "C", 8, cellStyle, "", paddingHead2,bgColor);
			communsPdf.setCell(baseRow,12,"Folio", black, true, "C", 8, cellStyle, "",paddingHead2, bgColor);
			communsPdf.setCell(baseRow,12,"Endoso", black, true, "C", 8, cellStyle, "", paddingHead2,bgColor);
			communsPdf.setCell(baseRow,18,"Tipo", black, true, "C", 8, cellStyle, "", paddingHead2,bgColor);
			communsPdf.setCell(baseRow,14,"Solicitud", black, true, "C", 8, cellStyle, "", paddingHead2,bgColor);
			communsPdf.setCell(baseRow,17,"Vigencia", black, true, "C", 8, cellStyle, "", paddingHead2,bgColor);
			communsPdf.setCell(baseRow,15,"Estatus", black, true, "C", 8, cellStyle, "", paddingHead2, bgColor);
			
			table.draw();
			
			yStart -= table.getHeaderAndDataHeight()+25;
			
			return yStart;
		} catch (Exception e) {
			throw new GeneralServiceException("Error=>", e.getMessage());
		}
	}
	
	
	private BaseTable getCuerpoPdf(PDDocument document, PDPage page, MovimientosDTO movimientosDTO, int loop) {
		try {
			
			BaseTable table;
			Row<PDPage> baseRow;
		
			Color bgColorRow = new Color(255, 255, 255, 0 );
			if (loop % 2 == 0) {
				bgColorRow = new Color(246, 248, 253, 1);
		    }
			
		
			table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, 20, document, page, false,true);
			baseRow = communsPdf.setRow(table, 25);
			communsPdf.setCell(baseRow,12, movimientosDTO.getPoliza().toString(), black, false, "C", 8, cellStyle2, "", paddingBody, bgColor);
			communsPdf.setCell(baseRow,12, movimientosDTO.getFolio().toString(), black, false, "C", 8, cellStyle2, "", paddingBody, bgColor);
			communsPdf.setCell(baseRow,12, movimientosDTO.getEndoso().toString(), black, false, "C", 8, cellStyle2, "", paddingBody, bgColor);
			communsPdf.setCell(baseRow,18, movimientosDTO.getTipo().toString(), black, false, "C", 8, cellStyle2, "", paddingBody, bgColor);
			communsPdf.setCell(baseRow,14, movimientosDTO.getSolicitud().toString(), black, false, "C", 8, cellStyle2, "", paddingBody, bgColor);
			communsPdf.setCell(baseRow,17, movimientosDTO.getVigencia().toString(), black, false, "C", 8, cellStyle2, "", paddingBody, bgColor);
			communsPdf.setCell(baseRow,15, movimientosDTO.getEstatus().toString(), black, false, "C", 8, cellStyle2, "", paddingBody, bgColor);
			
			return table;
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new GeneralServiceException("Error=>", e.getMessage());
		}
	}
	
	private BaseTable getMarcaAgua2(PDDocument document, PDPage page) {
		try {
			
			BaseTable table;
			Row<PDPage> baseRow;

			table = new BaseTable(775, 775, bottomMargin, 460, 10, document, page, true, true);
			baseRow = communsPdf.setRow(table, 600);
			communsPdf.setCell(baseRow, 100, ImageUtils.readImage("https://storage.googleapis.com/quattrocrm-copsis/biibiic/axa/fondo_axa_carta.png"));
			table.draw();
			
			return table;
		}catch (Exception e) {
			throw new GeneralServiceException("Error=>", e.getMessage());
		}
	}
	
	private void getMarcaAgua(PDDocument document, PDPage page) {
		try (PDPageContentStream content = new PDPageContentStream(document, page, AppendMode.APPEND,false,false)) {

			URL marcaAgua = new URL("https://storage.googleapis.com/quattrocrm-copsis/biibiic/axa/fondo_axa_carta.png");
			BufferedImage imgMar = ImageIO.read(marcaAgua);
			PDImageXObject pdImage2 = LosslessFactory.createFromImage(document, imgMar);
			
			content.drawImage(pdImage2, 0, 0, 612, 792);
			
		} catch (Exception e) {
		throw new GeneralServiceException("Error:", e.getMessage());
		}
	}
	
	private boolean isEndOfPage(BaseTable table) {
        float currentY = yStart - table.getHeaderAndDataHeight();
        return currentY <= bottomMargin;
    }
	
	
}
