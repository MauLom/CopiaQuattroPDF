package com.copsis.models.impresion;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;

import com.copsis.controllers.forms.ImpresionForm;
import com.copsis.exceptions.GeneralServiceException;
import com.copsis.models.NumeroALetraModel;
import com.copsis.models.Tabla.BaseTable;
import com.copsis.models.Tabla.ImageUtils;
import com.copsis.models.Tabla.LineStyle;
import com.copsis.models.Tabla.Row;
import com.copsis.models.Tabla.Sio4CommunsPdf;

public class ImpresionInter {
	private Color black = new Color(0, 0, 0);
	private Sio4CommunsPdf communsPdf = new Sio4CommunsPdf();
	private float  yStartNewPage = 760, yStart = 760, bottomMargin = 0;
	private float fullWidth = 551;
	List<LineStyle> lineBoders = new ArrayList<>();
	List<Float> padding = new ArrayList<>();
	NumeroALetraModel fnLetras = new NumeroALetraModel();

	public byte[] buildPDF(ImpresionForm impresionForm) {

		// Solo son 4 L,R,T,B
		lineBoders.add(new LineStyle(new Color(51, 153, 102), 0));
		lineBoders.add(new LineStyle(new Color(51, 153, 102), 0));
		lineBoders.add(new LineStyle(new Color(51, 153, 102), 0));
		lineBoders.add(new LineStyle(new Color(51, 153, 102), 0));

		// Paddings son 4 L,R,T,B
		padding.add(0f);
		padding.add(0f);
		padding.add(4f);
		padding.add(0f);


		try {
			ByteArrayOutputStream output;
			try (PDDocument document = new PDDocument()) {
				try {
					PDPage page = new PDPage();
					document.addPage(page);
					BaseTable table;
					Row<PDPage> baseRow;
					communsPdf.impresiom = 102;

					//File logo = new File("https://storage.googleapis.com/quattrocrm-copsis/h11fia/recursosInter/interLogo.png");
					table = new BaseTable(yStart, yStartNewPage, bottomMargin, 100, 250, document, page, false, true);
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 100, ImageUtils.readImage("https://storage.googleapis.com/quattrocrm-copsis/h11fia/recursosInter/LOGO-INTER-SINALOA.png"), 0, 0, black);
					table.draw();

					yStart -= table.getHeaderAndDataHeight() + 100;

					table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, 100, document, page, false,
							true);
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 100, "Culiacán, Sinaloa a " + impresionForm.getFecha(), black, false,
							"L", 13, lineBoders, "", padding);
					table.draw();

					yStart -= table.getHeaderAndDataHeight() + 50;

					table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, 100, document, page, false,
							true);
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 100, "MARTHA ELENA JACOBO MEDINA", black, false, "L", 13, lineBoders,
							"", padding);
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 100, "Presente.", black, false, "L", 13, lineBoders, "", padding);
					table.draw();

					yStart -= table.getHeaderAndDataHeight() + 20;

					table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, 100, document, page, false,
							true);
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 100,
							"Por medio de la presente se autoriza el pago de la reclamación de "
									+ communsPdf.eliminaHtmlTags("<b>" + impresionForm.getTipoSiniestro() + "</b>")
									+ " del siniestro a",
							black, false, "L", 13, lineBoders, "", padding);
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 100,
							"nombre de: " + communsPdf.eliminaHtmlTags("<b>" + impresionForm.getAsegurado() + "</b>")
									+ " cual será entregado a sus beneficiarios designados en la",
							black, false, "L", 13, lineBoders, "", padding);
					baseRow = communsPdf.setRow(table, 15);
					 DecimalFormat formateador = new DecimalFormat("#,##0.00");
					communsPdf.setCell(baseRow, 100, "póliza por la cantidad de: " + formateador.format(Double.parseDouble( impresionForm.getSaSiniestro()))
							+" (" + fnLetras.convertir(impresionForm.getSaSiniestro(),false).toUpperCase() +" ). ", black, false, "L", 13, lineBoders, "", padding);
					table.draw();

					yStart -= table.getHeaderAndDataHeight() + 20;

					table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, 100, document, page, false,
							true);
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 100,
							"Lo anterior con fundamento en póliza vigente de seguro de grupo vida COPPEL, S.A. DE C.V., la", black, false, "L",
							13, lineBoders, "", padding);
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 100, "cual se tiene contratada con " + impresionForm.getAseguradora() +".", black,
							false, "L", 13, lineBoders, "", padding);
					table.draw();

					yStart -= table.getHeaderAndDataHeight() + 25;

					table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, 100, document, page, false,
							true);
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 100, communsPdf.eliminaHtmlTags("ATENTAMENTE:"), black, true, "L",
							12, lineBoders, "", padding);
					baseRow = communsPdf.setRow(table, 15);
					//File firma = new File("https://storage.googleapis.com/quattrocrm-copsis/h11fia/recursosInter/imgF.png");
					communsPdf.setCell(baseRow, 40, ImageUtils.readImage("https://storage.googleapis.com/quattrocrm-copsis/h11fia/recursosInter/imgFirma.png"), 6, 37, black);
					table.draw();

					yStart -= table.getHeaderAndDataHeight() + 15;

					table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, 100, document, page, false,
							true);
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 100, "Lic. José María Espinosa de los Monteros Saldaña", black, false,
							"L", 13, lineBoders, "", padding);
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 100,
							"Interprotección Sinaloa, Agente de Seguros y de Fianzas, S.A. de C.V.", black, false, "L",
							13, lineBoders, "", padding);
					table.draw();

					yStart = 90;
					//File redes = new File("https://storage.googleapis.com/quattrocrm-copsis/h11fia/recursosInter/redes.png");
					table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, 50, document, page, false,
							true);
					baseRow = communsPdf.setRow(table, 12);
					communsPdf.setCell(baseRow, 100, "Interprotección Sinaloa, Agente de Seguros y de Fianzas SA de CV",
							black, true, "C", 13, lineBoders, "", padding);
					baseRow = communsPdf.setRow(table, 12);
					communsPdf.setCell(baseRow, 50, "667 752 0585 / 90|", black, true, "R", 13, lineBoders, "", padding);
					communsPdf.setCell(baseRow, 50, "800 000 312", black, false, "l", 13, lineBoders, "", padding);
					baseRow = communsPdf.setRow(table, 12);
					communsPdf.setCell(baseRow, 100,
							"Blvd. Paseo Niños Héroes #700 Int. 5, Colonia Centro, CP.80000, Culiacán, Sinaloa", black, false, "C",
							13, lineBoders, "", padding);
//					baseRow = communsPdf.setRow(table, 15);
//					communsPdf.setCell(baseRow, 45, "www.inter.mx", black, false, "R", 13, lineBoders, "", padding);
//					communsPdf.setCell(baseRow, 15, ImageUtils.readImage("https://storage.googleapis.com/quattrocrm-copsis/h11fia/recursosInter/redes.png"), 6, 37, black);
//					communsPdf.setCell(baseRow, 40, "interprotección", black, false, "L", 13, lineBoders, "", padding);
					table.draw();

					output = new ByteArrayOutputStream();
					document.save(output);
					return output.toByteArray();
				} finally {
					document.close();
				}

			}

		} catch (Exception ex) {
			
			throw new GeneralServiceException("00001", "Ocurrio un error en el servicio ImpresionInter: "+ ex.getMessage());
			
		}

	}

}
