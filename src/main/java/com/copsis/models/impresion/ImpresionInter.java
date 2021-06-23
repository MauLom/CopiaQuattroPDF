package com.copsis.models.impresion;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;

import com.copsis.controllers.forms.ImpresionForm;
import com.copsis.models.Tabla.BaseTable;
import com.copsis.models.Tabla.ImageUtils;
import com.copsis.models.Tabla.LineStyle;
import com.copsis.models.Tabla.Row;
import com.copsis.models.Tabla.Sio4CommunsPdf;

public class ImpresionInter {
	private Color black = new Color(0, 0, 0);
	private Sio4CommunsPdf communsPdf = new Sio4CommunsPdf();
	private float margin = 36, yStartNewPage = 760, yStart = 760, bottomMargin = 0;
    private float fullWidth = 551;
	List<LineStyle>  lineBoders = new ArrayList<>();
	List<Float>  padding = new ArrayList<>();

	public byte[] buildPDF(ImpresionForm impresionForm) {

		//Solo son 4 L,R,T,B
		lineBoders.add( new LineStyle(new Color(51, 153, 102), 0));
		lineBoders.add( new LineStyle(new Color(51, 153, 102), 0));
		lineBoders.add( new LineStyle(new Color(51, 153, 102), 0));
		lineBoders.add( new LineStyle(new Color(51, 153, 102), 0));
	
		//Paddings  son 4 L,R,T,B
		padding.add(0f);
		padding.add(0f);
		padding.add(4f);
		padding.add(0f);
		
	
		
		byte[] pdfArray = null;
		try {
			ByteArrayOutputStream output;
			try (PDDocument document = new PDDocument()) {
				try {
					PDPage page = new PDPage();
					document.addPage(page);
					BaseTable table;
					Row<PDPage> baseRow;
					System.out.println("----> llego");
					
	                    File logo = new File(getClass().getClassLoader().getResource("imgInter/interImg.png").toURI());					
						table = new BaseTable(yStart, yStartNewPage, bottomMargin, 200, 200, document, page, false,true);
						baseRow = communsPdf.setRow(table, 15);
						communsPdf.setCell(baseRow, 100, ImageUtils.readImage(logo), 0, 0, black);
						table.draw();

						yStart -= table.getHeaderAndDataHeight() + 100;

						table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, 100, document, page,false, true);
						baseRow = communsPdf.setRow(table, 15);
						communsPdf.setCell(baseRow, 100, "Culiacán, Sinaloa a"  + impresionForm.getFecha(), black, false, "L",12, lineBoders, "", padding);
						table.draw();
						
						yStart -= table.getHeaderAndDataHeight() + 50;

						table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, 100, document, page,false, true);
						baseRow = communsPdf.setRow(table, 15);
						communsPdf.setCell(baseRow, 100, "MARTHA ELENA JACOBO MEDINA", black, true, "L",12, lineBoders, "", padding);
						baseRow = communsPdf.setRow(table, 15);
						communsPdf.setCell(baseRow, 100, "Coppel, S.A. de C.V.", black, false, "L",12, lineBoders, "", padding);
						table.draw();
						

						yStart -= table.getHeaderAndDataHeight() + 20;

						table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, 100, document, page,false, true);
						baseRow = communsPdf.setRow(table, 15);
						communsPdf.setCell(baseRow, 100, "Por medio de la presente se autoriza el pago de la reclamación de " +communsPdf.eliminaHtmlTags("<b>"+impresionForm.getTipoSiniestro() +"</b>") +" del siniestro a", black, false, "L",12, lineBoders, "", padding);
						baseRow = communsPdf.setRow(table, 15);
						communsPdf.setCell(baseRow, 100, "nombre de: " +communsPdf.eliminaHtmlTags( "<b>"+impresionForm.getAsegurado() +"</b>")+   " cual será entregado a sus beneficiarios designados en la", black, false, "L",12, lineBoders, "", padding);
						baseRow = communsPdf.setRow(table, 15);
						communsPdf.setCell(baseRow, 100, "póliza por la cantidad de: "+ impresionForm.getSaSiniestro()+ " (diez mil pesos 00/100 m.n.)", black, false, "L",12, lineBoders, "", padding);
						table.draw();
                     
						
						yStart -= table.getHeaderAndDataHeight() + 20;

						table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, 100, document, page,false, true);
						baseRow = communsPdf.setRow(table, 15);
						communsPdf.setCell(baseRow, 100, "Lo anterior con fundamento en póliza vigente de seguro de grupo vida COPPEL, S.A. DE C.V. la cual", black, false, "L",12, lineBoders, "", padding);
						baseRow = communsPdf.setRow(table, 15);
						communsPdf.setCell(baseRow, 100, "se tiene contratada con MAPFRE México, S.A.", black, false, "L",12, lineBoders, "", padding);
						table.draw();
                     
						yStart -= table.getHeaderAndDataHeight() + 25;

						table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, 100, document, page,false, true);
						baseRow = communsPdf.setRow(table, 15);
						communsPdf.setCell(baseRow, 100, communsPdf.eliminaHtmlTags("<b>ATENTAMENTE:<b>"), black, true, "L",12, lineBoders, "", padding);											
						table.draw();
						
						yStart -= table.getHeaderAndDataHeight() + 25;
	                    File log2 = new File(getClass().getClassLoader().getResource("imgInter/firma.png").toURI());					
						table = new BaseTable(yStart, yStartNewPage, bottomMargin, 250, 90, document, page, false,true);
						baseRow = communsPdf.setRow(table, 15);
						communsPdf.setCell(baseRow, 100, ImageUtils.readImage(log2), 0, 0, black);
						table.draw();
						
						
						yStart =100;
						table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, 50, document, page,false, true);
						baseRow = communsPdf.setRow(table, 15);
						communsPdf.setCell(baseRow, 100,"Interprotección,Agente de Seguros y de Fianzas,S.A de C.V.", black, false, "C",12, lineBoders, "", padding);
						baseRow = communsPdf.setRow(table, 15);
						communsPdf.setCell(baseRow, 100,"(667) 752 0585", black, false, "C",12, lineBoders, "", padding);
						baseRow = communsPdf.setRow(table, 15);
						communsPdf.setCell(baseRow, 100,"Paseo Niños Héroes No.700-5 Pte., Centro Culiacán,80000,Culiacán, Sin.", black, false, "C",12, lineBoders, "", padding);
						baseRow = communsPdf.setRow(table, 15);
						communsPdf.setCell(baseRow, 100,"www.inter.mx", black, false, "C",12, lineBoders, "", padding);
						table.draw();
                     
                     

					output = new ByteArrayOutputStream();
					document.save(output);
					document.save(new File("/home/development/Music/pdfInter/pbrueba.pdf"));
					return output.toByteArray();
				} finally {
					document.close();
				}

			}

		} catch (Exception ex) {
			System.out.println("Error en la impresion de Sio4CertificadoInterPdf  ==> " + ex.getMessage());
			return pdfArray;
		}

	}

}
