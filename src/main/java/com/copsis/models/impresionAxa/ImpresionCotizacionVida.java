package com.copsis.models.impresionAxa ;

import java.awt.Color ;
import java.io.ByteArrayOutputStream ;
import java.io.Console ;
import java.io.File ;
import java.text.DateFormatSymbols ;
import java.text.SimpleDateFormat ;
import java.util.ArrayList ;
import java.util.Date ;
import java.util.List ;
import java.util.Locale ;

import org.apache.pdfbox.pdmodel.PDDocument ;
import org.apache.pdfbox.pdmodel.PDPage ;

import com.copsis.clients.projections.CotizacionProjection ;
import com.copsis.clients.projections.ProspectoProjection ;
import com.copsis.exceptions.GeneralServiceException ;
import com.copsis.models.DataToolsModel ;
import com.copsis.models.TextoAxa ;
import com.copsis.models.Tabla.BaseTable ;
import com.copsis.models.Tabla.ImageUtils ;
import com.copsis.models.Tabla.PDocumenteHW ;
import com.copsis.models.Tabla.Row ;
import com.copsis.models.Tabla.Sio4CommunsPdf ;
import com.copsis.models.Tabla.VerticalAlignment;

public class ImpresionCotizacionVida {

	private final Color bgColor = new Color(255, 255, 255, 0);
	private final Color bgColorgris = new Color(109, 120, 136, 0);
	private final Color bgColorA = new Color(0, 44, 134, 0);
	private float margin = 10, yStartNewPage = 1150, yStart = 1150, bottomMargin = 130;
	private float fullWidth = 590;
	private Sio4CommunsPdf communsPdf = new Sio4CommunsPdf();
	private DataToolsModel fn = new DataToolsModel();

	public byte[] buildPDF(CotizacionProjection cotizacionProjection) {
		DateFormatSymbols sym = DateFormatSymbols.getInstance(new Locale("es", "MX"));
		sym.setMonths(new String[]{"Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"});
		sym.setAmPmStrings(new String[]{"AM", "PM"});
		SimpleDateFormat formatter = new SimpleDateFormat("dd 'de' MMMM 'de' yyyy", sym);
		ByteArrayOutputStream output;
		try {
			try ( PDDocument document = new PDDocument()) {
				try {
					PDPage page = new PDPage(PDocumenteHW.A3);

					document.addPage(page);
					BaseTable table;
					Row<PDPage> baseRow;
					BaseTable table2;
					Row<PDPage> baseRow2;
					BaseTable table3;
					Row<PDPage> baseRow3;
					BaseTable table4;
					Row<PDPage> baseRow4;

					table = new BaseTable(1170, 1170, bottomMargin, 607, 20, document, page, false, true);
					baseRow = communsPdf.setRow(table, 18);
					communsPdf.setCell(baseRow, 60, "", Color.BLACK, false, "L", 9, communsPdf.setLineStyle(Color.BLACK), "", communsPdf.setPadding(5f, 5f, 3f, 5f), bgColor);
					communsPdf.setCell(baseRow, 30, "Cotizador Vida - AXA", Color.BLACK, false, "L", 9, communsPdf.setLineStyle(Color.BLACK), "", communsPdf.setPadding(5f, 5f, 3f, 5f), bgColor).setLeftPadding(25f);
					table.draw();

					table = new BaseTable(1160, 1160, bottomMargin, 120, 100, document, page, false, true);
					baseRow = communsPdf.setRow(table, 12);
					communsPdf.setCell(baseRow, 100, ImageUtils.readImage("https://storage.googleapis.com/quattrocrm-prod/quattro-biibiic/2209/1N7rQflDvq65bN1u4E4VKMx8EV7QvJ0rhjgJUTSkhuC1dzj1prD49eGE73D87jH5/Axalogo.png"));
					table.draw();

					table = new BaseTable(1160, 1160, bottomMargin, 200, 350, document, page, false, true);
					baseRow = communsPdf.setRow(table, 12);
					communsPdf.setCell(baseRow, 45, ImageUtils.readImage("https://storage.googleapis.com/quattrocrm-copsis/s32tkk/2209/Polizas/2209/vgXoyBQh6weQnOWoF1ap30noi64mtwZU5BN487XnlHdLiukVtfh295bEepZm0Kl/barra2.png"));
					table.draw();

					table = new BaseTable(yStart, yStart, bottomMargin, 210, 510, document, page, false, true);
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 60, formatter.format(new Date()), Color.BLACK, false, "R", 9, communsPdf.setLineStyle(Color.BLACK), "", communsPdf.setPadding(5f, 5f, 3f, 5f), bgColor);
					communsPdf.setCell(baseRow, 40, "v2022.8", Color.red, false, "L", 7, communsPdf.setLineStyle(Color.BLACK), "", communsPdf.setPadding(0f, 0f, 5f, 0f), bgColor);
					baseRow = communsPdf.setRow(table, 18);
					communsPdf.setCell(baseRow, 17, "ROL:", bgColorgris, true, "L", 9, communsPdf.setLineStyle(Color.BLACK), "", communsPdf.setPadding(5f, 5f, 3f, 5f), bgColor);
					communsPdf.setCell(baseRow, 83, "-", Color.BLACK, false, "L", 9, communsPdf.setLineStyle(Color.BLACK), "", communsPdf.setPadding(5f, 5f, 3f, 5f), bgColor);
					baseRow = communsPdf.setRow(table, 18);
					communsPdf.setCell(baseRow, 26, "NOMBRE:", bgColorgris, true, "L", 9, communsPdf.setLineStyle(Color.BLACK), "", communsPdf.setPadding(5f, 5f, 3f, 5f), bgColor);
					communsPdf.setCell(baseRow, 74, "-", Color.BLACK, false, "L", 9, communsPdf.setLineStyle(Color.BLACK), "", communsPdf.setPadding(5f, 5f, 3f, 5f), bgColor);
					baseRow = communsPdf.setRow(table, 18);
					communsPdf.setCell(baseRow, 17, "MX:", bgColorgris, true, "L", 9, communsPdf.setLineStyle(Color.BLACK), "", communsPdf.setPadding(5f, 5f, 3f, 5f), bgColor);
					communsPdf.setCell(baseRow, 83, "-", Color.BLACK, false, "L", 9, communsPdf.setLineStyle(Color.BLACK), "", communsPdf.setPadding(5f, 5f, 3f, 5f), bgColor);
					baseRow = communsPdf.setRow(table, 18);
					communsPdf.setCell(baseRow, 21, "EMAIL:", bgColorgris, true, "L", 9, communsPdf.setLineStyle(Color.BLACK), "", communsPdf.setPadding(5f, 5f, 3f, 5f), bgColor);
					communsPdf.setCell(baseRow, 79, "-", Color.BLACK, false, "L", 9, communsPdf.setLineStyle(Color.BLACK), "", communsPdf.setPadding(5f, 5f, 3f, 5f), bgColor);
					table.draw();

					yStart -= table.getHeaderAndDataHeight() - 10;
					table = new BaseTable(yStart, yStart, bottomMargin, 150, 119, document, page, false, true);
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 100, cotizacionProjection.getProducto(), Color.BLACK, true, "L", 13, communsPdf.setLineStyle(Color.BLACK), "", communsPdf.setPadding(5f, 5f, 3f, 5f), bgColor);
					table.draw();
					yStart -= table.getHeaderAndDataHeight() + 8;

					table = new BaseTable(yStart, yStart, bottomMargin, 607, 116, document, page, true, true);
					baseRow = communsPdf.setRow(table, 5);
					communsPdf.setCell(baseRow, 100, "", Color.BLACK, true, "L", 10, communsPdf.setLineStyle(bgColorA), "", communsPdf.setPadding(5f, 5f, 5f, 5f), bgColorA);
					table.draw();

					yStart -= table.getHeaderAndDataHeight() + 5;
					table = new BaseTable(yStart, yStart, bottomMargin, 607, 119, document, page, false, true);
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 100, "Datos del Prospecto:", Color.BLACK, true, "L", 10, communsPdf.setLineStyle(Color.BLACK), "", communsPdf.setPadding(5f, 5f, 3f, 5f), bgColor);
					table.draw();

					yStart -= table.getHeaderAndDataHeight() - 10;
					if (cotizacionProjection.getProspecto() != null) {
						List<ProspectoProjection> propecto = cotizacionProjection.getProspecto();
						if (cotizacionProjection.isVida()) {

							for (int i = 0; i < propecto.size(); i++) {

								if (i % 2 == 0) {
									table = new BaseTable(yStart, yStart, bottomMargin, 180, 270, document, page, false, true);
									baseRow = communsPdf.setRow(table, 19);
									communsPdf.setCell(baseRow, 100, propecto.get(i).getParentesco(), Color.BLACK, true, "L", 10, communsPdf.setLineStyle(Color.BLACK), "", communsPdf.setPadding(5f, 5f, 3f, 5f), bgColor);
									baseRow = communsPdf.setRow(table, 15);
									communsPdf.setCell(baseRow, 30, "Nombre:", Color.BLACK, true, "L", 10, communsPdf.setLineStyle(Color.BLACK), "", communsPdf.setPadding(5f, 5f, 3f, 5f), bgColor);
									communsPdf.setCell(baseRow, 70, propecto.get(i).getNombre(), Color.BLACK, false, "L", 10, communsPdf.setLineStyle(Color.BLACK), "", communsPdf.setPadding(5f, 5f, 3f, 5f), bgColor);
									baseRow = communsPdf.setRow(table, 15);
									communsPdf.setCell(baseRow, 30, "Edad:", Color.BLACK, true, "L", 10, communsPdf.setLineStyle(Color.BLACK), "", communsPdf.setPadding(5f, 5f, 3f, 5f), bgColor);
									communsPdf.setCell(baseRow, 70, propecto.get(i).getEdad(), Color.BLACK, false, "L", 10, communsPdf.setLineStyle(Color.BLACK), "", communsPdf.setPadding(5f, 5f, 3f, 5f), bgColor);
									baseRow = communsPdf.setRow(table, 15);
									communsPdf.setCell(baseRow, 30, "Género:", Color.BLACK, true, "L", 10, communsPdf.setLineStyle(Color.BLACK), "", communsPdf.setPadding(5f, 5f, 3f, 5f), bgColor);
									communsPdf.setCell(baseRow, 70, propecto.get(i).getSexo(), Color.BLACK, false, "L", 10, communsPdf.setLineStyle(Color.BLACK), "", communsPdf.setPadding(5f, 5f, 3f, 5f), bgColor);
									baseRow = communsPdf.setRow(table, 15);
									communsPdf.setCell(baseRow, 30, "Hábito:", Color.BLACK, true, "L", 10, communsPdf.setLineStyle(Color.BLACK), "", communsPdf.setPadding(5f, 5f, 3f, 5f), bgColor);
									communsPdf.setCell(baseRow, 70, propecto.get(i).getHabito(), Color.BLACK, false, "L", 10, communsPdf.setLineStyle(Color.BLACK), "", communsPdf.setPadding(5f, 5f, 3f, 5f), bgColor);
									table.draw();
								} else {
									table = new BaseTable(yStart, yStart, bottomMargin, 180, 450, document, page, false, true);
									baseRow = communsPdf.setRow(table, 19);
									communsPdf.setCell(baseRow, 100, propecto.get(i).getParentesco(), Color.BLACK, true, "L", 10, communsPdf.setLineStyle(Color.BLACK), "", communsPdf.setPadding(5f, 5f, 3f, 5f), bgColor);
									baseRow = communsPdf.setRow(table, 15);
									communsPdf.setCell(baseRow, 30, "Nombre:", Color.BLACK, true, "L", 10, communsPdf.setLineStyle(Color.BLACK), "", communsPdf.setPadding(5f, 5f, 3f, 5f), bgColor);
									communsPdf.setCell(baseRow, 70, propecto.get(i).getNombre(), Color.BLACK, false, "L", 10, communsPdf.setLineStyle(Color.BLACK), "", communsPdf.setPadding(5f, 5f, 3f, 5f), bgColor);
									baseRow = communsPdf.setRow(table, 15);
									communsPdf.setCell(baseRow, 30, "Edad:", Color.BLACK, true, "L", 10, communsPdf.setLineStyle(Color.BLACK), "", communsPdf.setPadding(5f, 5f, 3f, 5f), bgColor);
									communsPdf.setCell(baseRow, 70, propecto.get(i).getEdad(), Color.BLACK, false, "L", 10, communsPdf.setLineStyle(Color.BLACK), "", communsPdf.setPadding(5f, 5f, 3f, 5f), bgColor);
									baseRow = communsPdf.setRow(table, 15);
									communsPdf.setCell(baseRow, 30, "Género:", Color.BLACK, true, "L", 10, communsPdf.setLineStyle(Color.BLACK), "", communsPdf.setPadding(5f, 5f, 3f, 5f), bgColor);
									communsPdf.setCell(baseRow, 70, propecto.get(i).getSexo(), Color.BLACK, false, "L", 10, communsPdf.setLineStyle(Color.BLACK), "", communsPdf.setPadding(5f, 5f, 3f, 5f), bgColor);
									table.draw();

								}

							}

						} else {
							for (int i = 0; i < propecto.size(); i++) {
								table = new BaseTable(yStart, yStart, bottomMargin, 300, 320, document, page, false, true);
								baseRow = communsPdf.setRow(table, 15);
								communsPdf.setCell(baseRow, 30, "Nombre:", Color.BLACK, true, "L", 10, communsPdf.setLineStyle(Color.BLACK), "", communsPdf.setPadding(5f, 5f, 3f, 5f), bgColor);
								communsPdf.setCell(baseRow, 70, propecto.get(i).getNombre(), Color.BLACK, false, "L", 10, communsPdf.setLineStyle(Color.BLACK), "", communsPdf.setPadding(5f, 5f, 3f, 5f), bgColor);
								baseRow = communsPdf.setRow(table, 15);
								communsPdf.setCell(baseRow, 30, "Edad:", Color.BLACK, true, "L", 10, communsPdf.setLineStyle(Color.BLACK), "", communsPdf.setPadding(5f, 5f, 3f, 5f), bgColor);
								communsPdf.setCell(baseRow, 70, propecto.get(i).getEdad(), Color.BLACK, false, "L", 10, communsPdf.setLineStyle(Color.BLACK), "", communsPdf.setPadding(5f, 5f, 3f, 5f), bgColor);

								baseRow = communsPdf.setRow(table, 15);
								communsPdf.setCell(baseRow, 30, "Género:", Color.BLACK, true, "L", 10, communsPdf.setLineStyle(Color.BLACK), "", communsPdf.setPadding(5f, 5f, 3f, 5f), bgColor);
								communsPdf.setCell(baseRow, 70, propecto.get(i).getSexo(), Color.BLACK, false, "L", 10, communsPdf.setLineStyle(Color.BLACK), "", communsPdf.setPadding(5f, 5f, 3f, 5f), bgColor);

								baseRow = communsPdf.setRow(table, 15);
								communsPdf.setCell(baseRow, 30, "Hábito:", Color.BLACK, true, "L", 10, communsPdf.setLineStyle(Color.BLACK), "", communsPdf.setPadding(5f, 5f, 3f, 5f), bgColor);
								communsPdf.setCell(baseRow, 70, propecto.get(i).getHabito(), Color.BLACK, false, "L", 10, communsPdf.setLineStyle(Color.BLACK), "", communsPdf.setPadding(5f, 5f, 3f, 5f), bgColor);
								table.draw();
							}
						}
					}

					yStart -= table.getHeaderAndDataHeight() + 20;

					table = new BaseTable(yStart, yStart, bottomMargin, 607, 116, document, page, true, true);
					baseRow = communsPdf.setRow(table, 5);
					communsPdf.setCell(baseRow, 100, "", Color.BLACK, true, "L", 10, communsPdf.setLineStyle(bgColorA), "", communsPdf.setPadding(5f, 5f, 5f, 5f), bgColorA);
					table.draw();

					yStart -= table.getHeaderAndDataHeight() + 5;
					table = new BaseTable(yStart, yStart, bottomMargin, 607, 119, document, page, false, true);
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 100, "Características del Plan:", Color.BLACK, true, "L", 10, communsPdf.setLineStyle(Color.BLACK), "", communsPdf.setPadding(5f, 5f, 3f, 5f), bgColor);
					table.draw();

					yStart -= table.getHeaderAndDataHeight() + 5;
					table = new BaseTable(yStart, yStart, bottomMargin, 400, 320, document, page, false, true);
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 40, "Plan:", Color.BLACK, true, "L", 10, communsPdf.setLineStyle(Color.BLACK), "", communsPdf.setPadding(5f, 5f, 3f, 5f), bgColor);
					communsPdf.setCell(baseRow, 60, cotizacionProjection.getPlan(), Color.BLACK, false, "L", 10, communsPdf.setLineStyle(Color.BLACK), "", communsPdf.setPadding(5f, 5f, 3f, 5f), bgColor);
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 40, "Tipo de Moneda:", Color.BLACK, true, "L", 10, communsPdf.setLineStyle(Color.BLACK), "", communsPdf.setPadding(5f, 5f, 3f, 5f), bgColor);
					communsPdf.setCell(baseRow, 60, cotizacionProjection.getMoneda(), Color.BLACK, false, "L", 10, communsPdf.setLineStyle(Color.BLACK), "", communsPdf.setPadding(5f, 5f, 3f, 5f), bgColor);

					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 40, "Plazo de pagos:", Color.BLACK, true, "L", 10, communsPdf.setLineStyle(Color.BLACK), "", communsPdf.setPadding(5f, 5f, 3f, 5f), bgColor);
					communsPdf.setCell(baseRow, 60, (cotizacionProjection.getPlazoPagos() != "" ? "Edad alcanzada " + cotizacionProjection.getPlazoPagos() + " años" : ""), Color.BLACK, false, "L", 10, communsPdf.setLineStyle(Color.BLACK), "", communsPdf.setPadding(5f, 5f, 3f, 5f), bgColor);

					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 40, "Prima anual:", Color.BLACK, true, "L", 10, communsPdf.setLineStyle(Color.BLACK), "", communsPdf.setPadding(5f, 5f, 3f, 5f), bgColor);
					communsPdf.setCell(baseRow, 60, cotizacionProjection.getPrimaAnual() + "                        " + (cotizacionProjection.getPrimaAnualex().length() > 0 ? cotizacionProjection.getPrimaAnualex().replace("UDI", "Pesos") : ""), Color.BLACK, false, "L", 10, communsPdf.setLineStyle(Color.BLACK), "", communsPdf.setPadding(5f, 5f, 3f, 5f), bgColor);

					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 40, "Aportación:", Color.BLACK, true, "L", 10, communsPdf.setLineStyle(Color.BLACK), "", communsPdf.setPadding(5f, 5f, 3f, 5f), bgColor);
					communsPdf.setCell(baseRow, 60, cotizacionProjection.getAportacion(), Color.BLACK, false, "L", 10, communsPdf.setLineStyle(Color.BLACK), "", communsPdf.setPadding(5f, 5f, 3f, 5f), bgColor);

					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 40, "Prima mensual:", Color.BLACK, true, "L", 10, communsPdf.setLineStyle(Color.BLACK), "", communsPdf.setPadding(5f, 5f, 3f, 5f), bgColor);
					communsPdf.setCell(baseRow, 60, cotizacionProjection.getPrimaMensual(), Color.BLACK, false, "L", 10, communsPdf.setLineStyle(Color.BLACK), "", communsPdf.setPadding(5f, 5f, 3f, 5f), bgColor);

					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 40, "Valor de la UDI:", Color.BLACK, true, "L", 10, communsPdf.setLineStyle(Color.BLACK), "", communsPdf.setPadding(5f, 5f, 3f, 5f), bgColor);
					communsPdf.setCell(baseRow, 60, cotizacionProjection.getValorUdi(), Color.BLACK, false, "L", 10, communsPdf.setLineStyle(Color.BLACK), "", communsPdf.setPadding(5f, 5f, 3f, 5f), bgColor);

					baseRow = communsPdf.setRow(table, 25);
					communsPdf.setCell(baseRow, 40, "", Color.BLACK, true, "L", 10, communsPdf.setLineStyle(Color.BLACK), "", communsPdf.setPadding(5f, 5f, 3f, 5f), bgColor);
					communsPdf.setCell(baseRow, 60, "", Color.BLACK, false, "L", 10, communsPdf.setLineStyle(Color.BLACK), "", communsPdf.setPadding(5f, 5f, 3f, 5f), bgColor);

					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 40, "Prima Anual:", Color.BLACK, true, "L", 10, communsPdf.setLineStyle(Color.BLACK), "", communsPdf.setPadding(5f, 5f, 3f, 5f), bgColor);
					communsPdf.setCell(baseRow, 60, cotizacionProjection.getPrimaAnualex().replace("UDI", "Pesos"), Color.BLACK, false, "L", 10, communsPdf.setLineStyle(Color.BLACK), "", communsPdf.setPadding(5f, 5f, 3f, 5f), bgColor);

					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 40, "Aportación:", Color.BLACK, true, "L", 10, communsPdf.setLineStyle(Color.BLACK), "", communsPdf.setPadding(5f, 5f, 3f, 5f), bgColor);
					communsPdf.setCell(baseRow, 60, cotizacionProjection.getAportacionex(), Color.BLACK, false, "L", 10, communsPdf.setLineStyle(Color.BLACK), "", communsPdf.setPadding(5f, 5f, 3f, 5f), bgColor);

					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 40, "Prima mensual:", Color.BLACK, true, "L", 10, communsPdf.setLineStyle(Color.BLACK), "", communsPdf.setPadding(5f, 5f, 3f, 5f), bgColor);
					communsPdf.setCell(baseRow, 60, cotizacionProjection.getPrimaMensualex(), Color.BLACK, false, "L", 10, communsPdf.setLineStyle(Color.BLACK), "", communsPdf.setPadding(5f, 5f, 3f, 5f), bgColor);

					table.draw();

					yStart -= table.getHeaderAndDataHeight() + 15;
		
					if (cotizacionProjection.isVida()) {
						ArrayList<TextoAxa> texto = this.textolist2(cotizacionProjection.getPrimaUdiMenor(), cotizacionProjection.getPrimaUdiTitular());
						int i = 0;
						for (TextoAxa textoAxa : texto) {
							table = new BaseTable(yStart, yStart, bottomMargin, 607, 116, document, page, true, true);
							baseRow = communsPdf.setRow(table, 5);
							communsPdf.setCell(baseRow, 100, "", Color.BLACK, true, "L", 10, communsPdf.setLineStyle(bgColorA), "", communsPdf.setPadding(5f, 5f, 5f, 5f), bgColorA);

							baseRow = communsPdf.setRow(table, 5);
							communsPdf.setCell(baseRow, 100, "", Color.BLACK, true, "L", 10, communsPdf.setLineStyle(Color.white, Color.white, Color.white, Color.white), "", communsPdf.setPadding(5f, 5f, 5f, 5f), bgColor);
							table.draw();

							yStart -= table.getHeaderAndDataHeight();
							table2 = new BaseTable(yStart, yStart, bottomMargin, 200, 160, document, page, false, true);
							table3 = new BaseTable(yStart - 10, yStart, bottomMargin, 510, 250, document, page, false, true);
							table4 = new BaseTable(yStart , yStart, bottomMargin, 120, 615, document, page, false, true);

							if (i == 2) {

								baseRow2 = communsPdf.setRow(table2, 15);
								baseRow2.setLineSpacing(1.6f);
								communsPdf.setCell(baseRow2, 50, textoAxa.getNombre(), Color.BLACK, false, "c", 10, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(0f, 0f, 0f, 0f), bgColor).setTopPadding(0);

								baseRow = communsPdf.setRow(table, 40);
							} else {

								baseRow2 = communsPdf.setRow(table2, 15);
								baseRow.setLineSpacing(1.6f);
								communsPdf.setCell(baseRow2, 50, textoAxa.getNombre(), Color.BLACK, false, "c", 10, communsPdf.setLineStyle(Color.black), "", communsPdf.setPadding(0f, 0f, 0f, 0f), bgColor).setTopPadding(0);

							}

							if (i == 2) {

								baseRow2 = communsPdf.setRow(table2, 50);
								baseRow2.setLineSpacing(1.5f);

							} else {

								baseRow2 = communsPdf.setRow(table2, 25);

							}
							communsPdf.setCell(baseRow2, 15, "", Color.BLACK, false, "C", 12, communsPdf.setLineStyle(Color.black), "", communsPdf.setPadding(5f, 5f, 3f, 5f), bgColor);
							communsPdf.setCellImg(baseRow2, 20, ImageUtils.readImage(textoAxa.getLogo()), communsPdf.setLineStyle(Color.black), communsPdf.setPadding2(0f, 0f, 0f, 0f), "C", "T");
							communsPdf.setCell(baseRow2, 15, "", Color.BLACK, false, "C", 12, communsPdf.setLineStyle(Color.black), "", communsPdf.setPadding(5f, 5f, 3f, 5f), bgColor);

							if (i == 2) {
								baseRow3 = communsPdf.setRow(table3, 50);
								communsPdf.setCell(baseRow3, 85, Sio4CommunsPdf.eliminaHtmlTags3(textoAxa.getTexto()), Color.BLACK, false, "C", 12, communsPdf.setLineStyle(Color.BLACK), "", communsPdf.setPadding(5f, 5f, 3f, 5f), bgColor);
							} else {
								baseRow3 = communsPdf.setRow(table3, 25);
								communsPdf.setCell(baseRow3, 85, Sio4CommunsPdf.eliminaHtmlTags3(textoAxa.getTexto()), Color.BLACK, false, "C", 12, communsPdf.setLineStyle(Color.BLACK), "", communsPdf.setPadding(5f, 5f, 3f, 5f), bgColor);
							}



							if (i == 0) {
								baseRow4 = communsPdf.setRow(table4, 25);
								communsPdf.setCell(baseRow4,100, cotizacionProjection.getPrimaUdiMenor() +"UDI",Color.BLACK,false, "C",12, communsPdf.setLineStyle(Color.BLACK), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor).setValign(VerticalAlignment.MIDDLE);
					
								baseRow4 = communsPdf.setRow(table4, 25);
								communsPdf.setCell(baseRow4,100, cotizacionProjection.getPrimaPesosMenor() +" Pesos",Color.BLACK,false, "C",12, communsPdf.setLineStyle(Color.BLACK), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor).setValign(VerticalAlignment.MIDDLE);
							} else  if(i == 1){
								baseRow4 = communsPdf.setRow(table4, 25);
								communsPdf.setCell(baseRow4,100,cotizacionProjection.getPrimaUdiTitular() +" UDI",Color.BLACK,false, "C",12, communsPdf.setLineStyle(Color.BLACK), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor).setValign(VerticalAlignment.MIDDLE);
						
								baseRow4 = communsPdf.setRow(table4, 25);
								communsPdf.setCell(baseRow4,100, cotizacionProjection.getPrimaPesosTitular() +" Pesos",Color.BLACK,false, "C",12, communsPdf.setLineStyle(Color.BLACK), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor).setValign(VerticalAlignment.MIDDLE);
							}


							table2.draw();
							yStart -= table2.getHeaderAndDataHeight();
							table3.draw();
							table4.draw();

							if (i == 2) {
								yStart = yStart + 20;
							}
							i++;
						}

					} else {
						ArrayList<TextoAxa> texto = this.textolist(cotizacionProjection.getPrimaUdiMenor(), cotizacionProjection.getPrimaUdiTitular());
						int i = 0;
						for (TextoAxa textoAxa : texto) {

							table = new BaseTable(yStart, yStart, bottomMargin, 607, 116, document, page, true, true);
							baseRow = communsPdf.setRow(table, 5);
							communsPdf.setCell(baseRow, 100, "", Color.BLACK, true, "L", 10, communsPdf.setLineStyle(bgColorA), "", communsPdf.setPadding(5f, 5f, 5f, 5f), bgColorA);

							baseRow = communsPdf.setRow(table, 5);
							communsPdf.setCell(baseRow, 100, "", Color.BLACK, true, "L", 10, communsPdf.setLineStyle(Color.white, Color.white, Color.white, Color.white), "", communsPdf.setPadding(5f, 5f, 5f, 5f), bgColor);
							table.draw();

							yStart -= table.getHeaderAndDataHeight();
							table2 = new BaseTable(yStart, yStart, bottomMargin, 200, 130, document, page, false, true);
							table3 = new BaseTable(yStart - 10, yStart, bottomMargin, 510, 210, document, page, false, true);
							table4 = new BaseTable(yStart , yStart, bottomMargin, 120, 615, document, page, false, true);

							if (i == 1) {

								baseRow2 = communsPdf.setRow(table2, 15);
								baseRow2.setLineSpacing(1.6f);
								communsPdf.setCell(baseRow2, 50, textoAxa.getNombre(), Color.BLACK, false, "c", 10, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(0f, 0f, 0f, 0f), bgColor).setTopPadding(0);

								baseRow = communsPdf.setRow(table, 40);
							} else {

								baseRow2 = communsPdf.setRow(table2, 15);
								baseRow.setLineSpacing(1.6f);
								communsPdf.setCell(baseRow2, 50, textoAxa.getNombre(), Color.BLACK, false, "c", 10, communsPdf.setLineStyle(Color.black), "", communsPdf.setPadding(0f, 0f, 0f, 0f), bgColor).setTopPadding(0);

							}

							if (i == 1) {

								baseRow2 = communsPdf.setRow(table2, 50);
								baseRow2.setLineSpacing(1.5f);

							} else {

								baseRow2 = communsPdf.setRow(table2, 50);

							}
							communsPdf.setCell(baseRow2, 15, "", Color.BLACK, false, "C", 12, communsPdf.setLineStyle(Color.black), "", communsPdf.setPadding(5f, 5f, 3f, 5f), bgColor);
							communsPdf.setCellImg(baseRow2, 20, ImageUtils.readImage(textoAxa.getLogo()), communsPdf.setLineStyle(Color.black), communsPdf.setPadding2(0f, 0f, 0f, 0f), "C", "T");
							communsPdf.setCell(baseRow2, 15, "", Color.BLACK, false, "C", 12, communsPdf.setLineStyle(Color.black), "", communsPdf.setPadding(5f, 5f, 3f, 5f), bgColor);

							if (i == 1) {
								baseRow3 = communsPdf.setRow(table3, 50);
								communsPdf.setCell(baseRow3, 85, Sio4CommunsPdf.eliminaHtmlTags3(textoAxa.getTexto()), Color.BLACK, false, "C", 12, communsPdf.setLineStyle(Color.BLACK), "", communsPdf.setPadding(5f, 5f, 3f, 5f), bgColor);
							} else {
								baseRow3 = communsPdf.setRow(table3, 25);
								communsPdf.setCell(baseRow3, 85, Sio4CommunsPdf.eliminaHtmlTags3(textoAxa.getTexto()), Color.BLACK, false, "C", 12, communsPdf.setLineStyle(Color.BLACK), "", communsPdf.setPadding(5f, 5f, 3f, 5f), bgColor);
							}



							if (i == 0) {
								baseRow4 = communsPdf.setRow(table4, 25);
								communsPdf.setCell(baseRow4,100, cotizacionProjection.getPrimaUdiMenor() +"UDI",Color.BLACK,false, "C",12, communsPdf.setLineStyle(Color.BLACK), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor).setValign(VerticalAlignment.MIDDLE);
					
								baseRow4 = communsPdf.setRow(table4, 25);
								communsPdf.setCell(baseRow4,100, cotizacionProjection.getPrimaPesosMenor() +" Pesos",Color.BLACK,false, "C",12, communsPdf.setLineStyle(Color.BLACK), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor).setValign(VerticalAlignment.MIDDLE);
							} else  if(i == 1){
								baseRow4 = communsPdf.setRow(table4, 25);
								communsPdf.setCell(baseRow4,100,cotizacionProjection.getPrimaUdiTitular() +" UDI",Color.BLACK,false, "C",12, communsPdf.setLineStyle(Color.BLACK), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor).setValign(VerticalAlignment.MIDDLE);
						
								baseRow4 = communsPdf.setRow(table4, 25);
								communsPdf.setCell(baseRow4,100, cotizacionProjection.getPrimaPesosTitular() +" Pesos",Color.BLACK,false, "C",12, communsPdf.setLineStyle(Color.BLACK), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor).setValign(VerticalAlignment.MIDDLE);
							}


							table2.draw();
							yStart -= table2.getHeaderAndDataHeight();
							table3.draw();
							table4.draw();

							if (i == 2) {
								yStart = yStart + 20;
							}
							i++;
						}
					}

					StringBuilder txtexta = new StringBuilder();
					txtexta.append("Esta cotización tiene una validez de 15 días naturales contados a partir de la fecha de su elaboración, por lo que no se garantiza la emisión del seguro. La misma es ilustrativa y de apoyo a"
							+ " la fuerza de ventas. No forma parte del contrato del seguro."
							+ "\nAXA Seguros se reserva el derecho de solicitar la respuesta a un cuestionario médico y de ocupación, así como estudios de laboratorio.");
					yStart = yStart - 10;

					table = new BaseTable(yStart, yStart, bottomMargin, 607, 116, document, page, true, true);
					baseRow = communsPdf.setRow(table, 5);
					communsPdf.setCell(baseRow, 100, "", Color.BLACK, true, "L", 10, communsPdf.setLineStyle(bgColorA), "", communsPdf.setPadding(5f, 5f, 5f, 5f), bgColorA);
					baseRow = communsPdf.setRow(table, 5);
					communsPdf.setCell(baseRow, 100, "", Color.BLACK, true, "L", 10, communsPdf.setLineStyle(Color.white, Color.white, Color.white, Color.white), "", communsPdf.setPadding(5f, 5f, 5f, 5f), bgColor);
					table.draw();

					yStart -= table.getHeaderAndDataHeight();
					table = new BaseTable(yStart, yStart, bottomMargin, 600, 130, document, page, true, true);
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 100, Sio4CommunsPdf.eliminaHtmlTags3(txtexta.toString()), Color.BLACK, false, "C", 8, communsPdf.setLineStyle(bgColor), "", communsPdf.setPadding(0f, 0f, 0f, 0f), bgColor);

					table.draw();

					output = new ByteArrayOutputStream();
					document.save(output);
					//document.save(new File("/home/aalbanil/Vídeos/cotizacion4new.pdf"));
					return output.toByteArray();
				} finally {
					document.close();
				}

			}

		} catch (Exception ex) {
			throw new GeneralServiceException("00001",
					"Ocurrio un error en el servicio ImpresionCotizacionVida: " + ex.getMessage());
		}

	}

	public ArrayList<TextoAxa> textolist(String primaUdiTitular, String primaUdiMenor) {
		ArrayList<TextoAxa> texto = new ArrayList<>();

		texto.add(new TextoAxa("En caso de fallecimiento, tus beneficiarios recibirán la cantidad de " + " \nen una sola exhibición.", "Fallecimiento", "https://storage.googleapis.com/quattrocrm-prod/quattro-biibiic/2209/1N7rQflDvq65bN1u4E4VKPjFgtyzgy7JEIcHrDRLPgxY4xPcw5f7HAcl7VW6w/img_fallecimiento.png"));

		texto.add(new TextoAxa("En caso de invalidez total y permanente, tu seguro quedará exento del pago de"
				+ "las primas de la cobertura básica y continuará protegido por fallecimiento, y recibirás la cantidad de: "  , "Invalidez",
				"https://storage.googleapis.com/quattrocrm-prod/quattro-biibiic/2209/1N7rQflDvq65bN1u4E4VKOqd1O9liJdorrWLzgXQg2OgybH2wWtTZB8GTKAjt1/img_invalidez.png"));
		texto.add(new TextoAxa("Sin cobertura de desempleo.", "Desempleo", "https://storage.googleapis.com/quattrocrm-prod/quattro-biibiic/2209/1N7rQflDvq65bN1u4E4VKGce8Pt8szzzCnS3X8PzdEyUU70DygI1YSX6iQeJM0y/img_desempleo.png"));

		return texto;

	}

	public ArrayList<TextoAxa> textolist2(String primaUdiTitular, String primaUdiMenor) {
		ArrayList<TextoAxa> texto = new ArrayList<>();

		texto.add(new TextoAxa("Al final del plazo recibirás la cantidad de ", "Meta de Ahorro",
				"https://storage.googleapis.com/quattrocrm-prod/quattro-biibiic/2209/1N7rQflDvq65bN1u4E4VKOWa39jvjMQDMhW3XUUVXXY3oR2Ib70UaEoEob3GL5w/img_supervivencia.png"));

		texto.add(new TextoAxa("En caso de fallecimiento, tus beneficiarios recibirán la cantidad de ", "Fallecimiento del titular",
				"https://storage.googleapis.com/quattrocrm-prod/quattro-biibiic/2209/1N7rQflDvq65bN1u4E4VKPjFgtyzgy7JEIcHrDRLPgxY4xPcw5f7HAcl7VW6w/img_fallecimiento.png"));
		texto.add(new TextoAxa("En caso de fallecimiento o invalidez del asegurado titular, el seguro quedará\nexento de pago de primas y continuará vigente hasta el final del plazo.", "Exención por fallecimiento o invalidez",
				"https://storage.googleapis.com/quattrocrm-prod/quattro-biibiic/2209/1N7rQflDvq65bN1u4E4VKPuzfyj18mhRXZJSO3QPvIn8TNqsoEiuGboZt0Sdk/img_excencion.png"));

		return texto;

	}

}
