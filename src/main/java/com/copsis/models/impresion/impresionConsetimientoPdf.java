package com.copsis.models.impresion;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;

import com.copsis.controllers.forms.ImpresionForm;
import com.copsis.exceptions.GeneralServiceException;
import com.copsis.models.Tabla.BaseTable;
import com.copsis.models.Tabla.ImageUtils;
import com.copsis.models.Tabla.LineStyle;
import com.copsis.models.Tabla.Row;
import com.copsis.models.Tabla.Sio4CommunsPdf;

public class impresionConsetimientoPdf {
	private Color black = new Color(0, 0, 0);
	private Color azul = new Color(0, 0, 143);
    private final Color bgColor = new Color(255, 255, 255, 0);
    private final Color bgColorA = new Color(0, 0, 143);
	private Sio4CommunsPdf communsPdf = new Sio4CommunsPdf();
	private float  yStartNewPage = 760, yStart = 760, bottomMargin = 30;
	private float fullWidth = 551;
	List<LineStyle> lineBoders = new ArrayList<>();
	List<LineStyle> lineBoders2 = new ArrayList<>();
	
	List<Float> padding = new ArrayList<>();
	List<Float> padding2 = new ArrayList<>();
	List<Float> padding3 = new ArrayList<>();

	public byte[] buildPDF(ImpresionForm impresionForm) {
		byte[] pdfArray = null;
		// Solo son 4 L,R,T,B
		lineBoders.add(new LineStyle(new Color(0, 0, 143), 0));
		lineBoders.add(new LineStyle(new Color(0, 0, 143), 0));
		lineBoders.add(new LineStyle(new Color(0, 0, 143), 0));
		lineBoders.add(new LineStyle(new Color(0, 0, 143), 0));
		
		// Solo son 4 L,R,T,B
		lineBoders2.add(new LineStyle(new Color(0, 0, 143), 0));
		lineBoders2.add(new LineStyle(new Color(0, 0, 143), 0));
		lineBoders2.add(new LineStyle(new Color(255, 255, 255), 0));
		lineBoders2.add(new LineStyle(new Color(0, 0, 143), 0));

		// Paddings son 4 L,R,T,B
		padding.add(0f);
		padding.add(0f);
		padding.add(4f);
		padding.add(0f);
		/**/
		padding2.add(2f);
		padding2.add(0f);
		padding2.add(4f);
		padding2.add(0f);
		/**/
		padding3.add(4f);
		padding3.add(0f);
		padding3.add(4f);
		padding3.add(0f);
		
		
		
		
		try {
			ByteArrayOutputStream output;
			try (PDDocument document = new PDDocument()) {
				try {
					output = new ByteArrayOutputStream();
					PDPage page = new PDPage();
					document.addPage(page);
					BaseTable table;
					Row<PDPage> baseRow;
					BaseTable table2;
					Row<PDPage> baseRow2;

					
		
					setEncabezado(impresionForm, document, page);
			
					table2 = new BaseTable((yStart-2), yStartNewPage, bottomMargin, fullWidth, 30, document, page, false,true);
					table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, 30, document, page, false,true);
					baseRow2 = communsPdf.setRow(table2, 15);
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow,20, "Contratante", azul, false,"L", 10, lineBoders, "", padding,bgColor);
					communsPdf.setCell(baseRow, 30, "", azul, false,"L", 13, lineBoders, "", padding,bgColor);
					communsPdf.setCell(baseRow, 20, "Categoría", azul, false,"L", 10, lineBoders, "", padding,bgColor);
					communsPdf.setCell(baseRow, 30, "", azul, false,"L", 13, lineBoders, "", padding,bgColor);
					/***/
					communsPdf.setCell(baseRow2,10, "", azul, false,"L", 13, lineBoders, "", padding,bgColor);
					communsPdf.setCell(baseRow2, 40, "_________________________", azul, false,"L", 13, lineBoders, "", padding,bgColor);
					communsPdf.setCell(baseRow2, 10, "", azul, false,"L", 12, lineBoders, "", padding,bgColor);
					communsPdf.setCell(baseRow2, 40, "____________________________", azul, false,"L", 13, lineBoders, "", padding,bgColor);
					table2.draw();
					table.draw();
					
					yStart -=table.getHeaderAndDataHeight()+20;
					/*Pagina 1*/
					table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, 30, document, page, true,true);
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow,100, "Datos del Integrante", bgColor, true,"L", 10, lineBoders, "", padding2,bgColorA);
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow,75, "Nombre(s), apellido paterno, apellido materno", azul, false,"L", 10, lineBoders, "", padding3,bgColor);
					communsPdf.setCell(baseRow,25, "Empleado No.", azul, false,"L", 10, lineBoders, "", padding3,bgColor);
					
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow,50, "RFC", azul, false,"L", 10, lineBoders, "", padding3,bgColor);
					communsPdf.setCell(baseRow,50, "Género", azul, false,"L", 10, lineBoders, "", padding3,bgColor);
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow,50, "", azul, false,"L", 10, lineBoders, "", padding3,bgColor);
					communsPdf.setCell(baseRow,10, "Masculino", azul, false,"L", 10, lineBoders, "", padding3,bgColor);
					communsPdf.setCell(baseRow,40, "Femenino", azul, false,"L", 10, lineBoders, "", padding3,bgColor);
					
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow,50, "Fecha de nacimiento", azul, false,"L", 10, lineBoders2, "", padding3,bgColor);
					communsPdf.setCell(baseRow,50, "Ocupación o profesión", azul, false,"L", 10, lineBoders2, "", padding3,bgColor);
					
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow,50, "", azul, false,"L", 10, lineBoders, "", padding3,bgColor);
					communsPdf.setCell(baseRow,50, "", azul, false,"L", 10, lineBoders, "", padding3,bgColor);
					
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow,75, "Domicilio", azul, true,"L", 10, lineBoders2, "", padding3,bgColor);
					communsPdf.setCell(baseRow,12, "No. exterior", azul, false,"L", 10, lineBoders2, "", padding3,bgColor);
					communsPdf.setCell(baseRow,13, "No. interior", azul, false,"L", 10, lineBoders2, "", padding3,bgColor);
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow,75, "Calle", azul, false,"L", 10, lineBoders, "", padding3,bgColor);
					communsPdf.setCell(baseRow,12, "", azul, false,"L", 10, lineBoders, "", padding3,bgColor);
					communsPdf.setCell(baseRow,13, "", azul, false,"L", 10, lineBoders, "", padding3,bgColor);
					
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow,33, "Colonia", azul, false,"L", 10, lineBoders2, "", padding3,bgColor);
					communsPdf.setCell(baseRow,33, "Población (delegación o municipio)", azul, false,"L", 10, lineBoders2, "", padding3,bgColor);
					communsPdf.setCell(baseRow,34, "Estado", azul, false,"L", 10, lineBoders2, "", padding3,bgColor);
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow,33, "", azul, false,"L", 10, lineBoders, "", padding3,bgColor);
					communsPdf.setCell(baseRow,33, "", azul, false,"L", 10, lineBoders, "", padding3,bgColor);
					communsPdf.setCell(baseRow,34, "", azul, false,"L", 10, lineBoders, "", padding3,bgColor);
					
					
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow,40, "Código postal", azul, false,"L", 10, lineBoders2, "", padding3,bgColor);
					communsPdf.setCell(baseRow,60, "Teléfono (con clave de ciudad)", azul, false,"L", 10, lineBoders2, "", padding3,bgColor);
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow,40, "", azul, false,"L", 10, lineBoders, "", padding3,bgColor);
					communsPdf.setCell(baseRow,60, "", azul, false,"L", 10, lineBoders, "", padding3,bgColor);
					
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow,100, "Llenar estos datos en caso de ser extranjero", bgColor, true,"L", 10, lineBoders, "", padding2,bgColorA);
					
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow,30, "Domicilio en su país de origen", azul, false,"L", 10, lineBoders2, "", padding3,bgColor);
					communsPdf.setCell(baseRow,10, "No.", azul, false,"L", 10, lineBoders2, "", padding3,bgColor);
					communsPdf.setCell(baseRow,20, "Colonia", azul, false,"L", 10, lineBoders2, "", padding3,bgColor);
					communsPdf.setCell(baseRow,15, "Código postal", azul, false,"L", 10, lineBoders2, "", padding3,bgColor);
					communsPdf.setCell(baseRow,25, "Delegación o municipio", azul, false,"L", 10, lineBoders2, "", padding3,bgColor);
			
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow,30, "Calle", azul, false,"L", 10, lineBoders, "", padding3,bgColor);
					communsPdf.setCell(baseRow,10, "", azul, false,"L", 10, lineBoders, "", padding3,bgColor);
					communsPdf.setCell(baseRow,20, "", azul, false,"L", 10, lineBoders, "", padding3,bgColor);
					communsPdf.setCell(baseRow,15, "", azul, false,"L", 10, lineBoders, "", padding3,bgColor);
					communsPdf.setCell(baseRow,25, "", azul, false,"L", 10, lineBoders, "", padding3,bgColor);
			
					
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow,30, "Ciudad", azul, false,"L", 10, lineBoders2, "", padding3,bgColor);
					communsPdf.setCell(baseRow,30, "Estado", azul, false,"L", 10, lineBoders2, "", padding3,bgColor);
					communsPdf.setCell(baseRow,15, "País", azul, false,"L", 10, lineBoders2, "", padding3,bgColor);
					communsPdf.setCell(baseRow,25, "Teléfono", azul, false,"L", 10, lineBoders2, "", padding3,bgColor);
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow,30, "Calle", azul, false,"L", 10, lineBoders, "", padding3,bgColor);
					communsPdf.setCell(baseRow,30, "", azul, false,"L", 10, lineBoders, "", padding3,bgColor);
					communsPdf.setCell(baseRow,15, "", azul, false,"L", 10, lineBoders, "", padding3,bgColor);					
					communsPdf.setCell(baseRow,25, "", azul, false,"L", 10, lineBoders, "", padding3,bgColor);
			
					
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow,40, "Fecha de nacimiento:", azul, false,"L", 10, lineBoders2, "", padding3,bgColor);
					communsPdf.setCell(baseRow,20, "Día", azul, false,"L", 10, lineBoders2, "", padding3,bgColor);
					communsPdf.setCell(baseRow,20, "Mes", azul, false,"L", 10, lineBoders2, "", padding3,bgColor);
					communsPdf.setCell(baseRow,20, "Mes", azul, false,"L", 10, lineBoders2, "", padding3,bgColor);
					
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow,40, "Fecha de inicio de vigencia del consentimiento:", azul, false,"L", 10, lineBoders2, "", padding3,bgColor);
					communsPdf.setCell(baseRow,20, "Día", azul, false,"L", 10, lineBoders2, "", padding3,bgColor);
					communsPdf.setCell(baseRow,20, "Mes", azul, false,"L", 10, lineBoders2, "", padding3,bgColor);
					communsPdf.setCell(baseRow,20, "Mes", azul, false,"L", 10, lineBoders2, "", padding3,bgColor);
					
					
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow,40, "Fecha de término de vigencia del consentimiento:", azul, false,"L", 10, lineBoders2, "", padding3,bgColor);
					communsPdf.setCell(baseRow,20, "Día", azul, false,"L", 10, lineBoders2, "", padding3,bgColor);
					communsPdf.setCell(baseRow,20, "Mes", azul, false,"L", 10, lineBoders2, "", padding3,bgColor);
					communsPdf.setCell(baseRow,20, "Mes", azul, false,"L", 10, lineBoders2, "", padding3,bgColor);
					table.draw();
					
					
					output = new ByteArrayOutputStream();
					document.save(output);
					document.save(new File("/home/desarrollo8/Documentos/impresion.pdf"));
					return output.toByteArray();
				}finally {
					document.close();
				} 
				
			}
			
		} catch (Exception ex) {
			throw new GeneralServiceException("00001", "Ocurrio un error en el servicio ImpresionInter: "+ ex.getMessage());
		}
		
	}
	
	 public void setEncabezado(ImpresionForm impresionForm , PDDocument document, PDPage page) {
	        try (PDPageContentStream content = new PDPageContentStream(document, page)) {
	            BaseTable table;
	            Row<PDPage> baseRow;
	            yStart = 762;
	            table = new BaseTable(yStart, yStartNewPage, bottomMargin, 100, 30, document, page, false, true);
				baseRow = communsPdf.setRow(table, 15);
				communsPdf.setCell(baseRow, 100, ImageUtils.readImage("https://storage.googleapis.com/quattrocrm-copsis/h11fia/recursosInter/LOGO-INTER-SINALOA.png"), 0, 0, black);
				table.draw();
				
				table = new BaseTable((yStart-20), yStartNewPage, bottomMargin, 300, 280, document, page, true,true);
				baseRow = communsPdf.setRow(table, 15);
				communsPdf.setCell(baseRow, 100, "Consentimiento Individual / Seguro de grupo", azul, true,"r", 11, lineBoders, "", padding);
				baseRow = communsPdf.setRow(table, 15);
				communsPdf.setCell(baseRow, 100, "Respaldo Empresarial ", azul, true,"r", 11, lineBoders, "", padding);
				table.draw();
				
				yStart -= table.getHeaderAndDataHeight()+40;

	        } catch (Exception ex) {
	            System.out.println("Error  en encabezado Sio4ConstaciaPdf +" + ex.getMessage());

	        }

	    
	 }
}
