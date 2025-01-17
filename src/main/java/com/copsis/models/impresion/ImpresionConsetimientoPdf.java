package com.copsis.models.impresion;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;

import com.copsis.clients.projections.BeneficiarioProjection;
import com.copsis.clients.projections.PaqueteCoberturaProjection;
import com.copsis.controllers.forms.ImpresionForm;
import com.copsis.exceptions.GeneralServiceException;
import com.copsis.models.Tabla.BaseTable;
import com.copsis.models.Tabla.ImageUtils;
import com.copsis.models.Tabla.LineStyle;
import com.copsis.models.Tabla.Row;
import com.copsis.models.Tabla.Sio4CommunsPdf;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ImpresionConsetimientoPdf {
	private Color black = new Color(0, 0, 0);
	private Color azul = new Color(0, 0, 143);
	private final Color bgColor = new Color(255, 255, 255, 0);
	private final Color bgColorA = new Color(0, 0, 143);
	private final Color bgColorAb = new Color(203, 203, 228, 0);
	private final Color bgColorCl = new Color(135, 129, 189, 0);
	private Sio4CommunsPdf communsPdf = new Sio4CommunsPdf();
	private float yStartNewPage = 760, yStart = 760, bottomMargin = 26;
	private float yStartStar = 0;
	private float fullWidth = 562;
	private  List<LineStyle> lineBoders = new ArrayList<>();
	private List<LineStyle> lineBoders1 = new ArrayList<>();
	private  List<LineStyle> lineBoders11 = new ArrayList<>();
	private List<LineStyle> lineBoders112 = new ArrayList<>();
	private List<LineStyle> lineBoders2 = new ArrayList<>();
	private List<LineStyle> lineBoders3 = new ArrayList<>();
	private List<LineStyle> lineBoders31 = new ArrayList<>();
	private List<LineStyle> lineBoders310 = new ArrayList<>();
	private List<LineStyle> lineBoders3101 = new ArrayList<>();

	private List<LineStyle> lineBoders32 = new ArrayList<>();
	private List<LineStyle> lineBoders33 = new ArrayList<>();
	private List<LineStyle> lineBoders40 = new ArrayList<>();
	private List<LineStyle> lineBoders4 = new ArrayList<>();
	private List<LineStyle> lineBoders41 = new ArrayList<>();
	private List<LineStyle> lineBoders5 = new ArrayList<>();
	private List<LineStyle> lineBoders6 = new ArrayList<>();
	private List<LineStyle> lineBoders61 = new ArrayList<>();
	private List<LineStyle> lineBoders62 = new ArrayList<>();
	private List<LineStyle> lineBoders63 = new ArrayList<>();
	private List<LineStyle> lineBoders64 = new ArrayList<>();
	private List<LineStyle> lineBoders65 = new ArrayList<>();

	private List<Float> padding = new ArrayList<>();
	private List<Float> padding2 = new ArrayList<>();
	private List<Float> padding3 = new ArrayList<>();
	private List<Float> padding4 = new ArrayList<>();

	public byte[] buildPDF(ImpresionForm impresionForm) {
		DateFormatSymbols sym = DateFormatSymbols.getInstance(new Locale("es", "MX"));
		sym.setMonths(new String[] { "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto","Septiembre", "Octubre", "Noviembre", "Diciembre" });
		sym.setAmPmStrings(new String[] { "AM", "PM" });
		SimpleDateFormat formatter = new SimpleDateFormat("dd 'de' MMMM 'del' yyyy hh:mm a", sym);

		
		// Solo son 4 L,R,T,B
		lineBoders.add(new LineStyle(new Color(0, 0, 143), 0));
		lineBoders.add(new LineStyle(new Color(0, 0, 143), 0));
		lineBoders.add(new LineStyle(new Color(0, 0, 143), 0));
		lineBoders.add(new LineStyle(new Color(0, 0, 143), 0));
		// Solo son 4 L,R,T,B
		lineBoders1.add(new LineStyle(new Color(0, 0, 143), 0));
		lineBoders1.add(new LineStyle(new Color(0, 0, 143), 0));
		lineBoders1.add(new LineStyle(Color.red, 0.5f));
		lineBoders1.add(new LineStyle(new Color(0, 0, 143), 0));

		// Solo son 4 L,R,T,B
		lineBoders11.add(new LineStyle(new Color(255, 255, 255), 0));
		lineBoders11.add(new LineStyle(new Color(255, 255, 255), 0));
		lineBoders11.add(new LineStyle(new Color(0, 0, 143), 0));
		lineBoders11.add(new LineStyle(new Color(0, 0, 143), 0));

		// Solo son 4 L,R,T,B
		lineBoders112.add(new LineStyle(new Color(0, 0, 143), 0));
		lineBoders112.add(new LineStyle(new Color(255, 255, 255), 0));
		lineBoders112.add(new LineStyle(new Color(0, 0, 143), 0));
		lineBoders112.add(new LineStyle(new Color(0, 0, 143), 0));

		// Solo son 4 L,R,T,B
		lineBoders2.add(new LineStyle(new Color(0, 0, 143), 0));
		lineBoders2.add(new LineStyle(new Color(0, 0, 143), 0));
		lineBoders2.add(new LineStyle(new Color(255, 255, 255), 0));
		lineBoders2.add(new LineStyle(new Color(0, 0, 143), 0));

		// Solo son 4 L,R,T,B
		lineBoders3.add(new LineStyle(new Color(0, 0, 143), 0));
		lineBoders3.add(new LineStyle(new Color(255, 255, 255), 0));
		lineBoders3.add(new LineStyle(new Color(0, 0, 143), 0));
		lineBoders3.add(new LineStyle(new Color(255, 255, 255), 0));

		lineBoders310.add(new LineStyle(new Color(255, 255, 255), 0));
		lineBoders310.add(new LineStyle(new Color(0, 0, 143), 0));
		lineBoders310.add(new LineStyle(new Color(255, 255, 255), 0));
		lineBoders310.add(new LineStyle(new Color(0, 0, 143), 0));

		// Solo son 4 L,R,T,B
		lineBoders3101.add(new LineStyle(new Color(0, 0, 143), 0));
		lineBoders3101.add(new LineStyle(new Color(255, 255, 255), 0));
		lineBoders3101.add(new LineStyle(new Color(255, 255, 255), 0));
		lineBoders3101.add(new LineStyle(new Color(0, 0, 143), 0));

		// Solo son 4 L,R,T,B
		lineBoders31.add(new LineStyle(new Color(255, 255, 255), 0));
		lineBoders31.add(new LineStyle(new Color(255, 255, 255), 0));
		lineBoders31.add(new LineStyle(new Color(255, 255, 255), 0));
		lineBoders31.add(new LineStyle(new Color(0, 0, 143), 0));

		// Solo son 4 L,R,T,B
		lineBoders32.add(new LineStyle(new Color(255, 255, 255), 0));
		lineBoders32.add(new LineStyle(new Color(0, 0, 143), 0));
		lineBoders32.add(new LineStyle(new Color(255, 255, 255), 0));
		lineBoders32.add(new LineStyle(new Color(0, 0, 143), 0));

		lineBoders33.add(new LineStyle(new Color(255, 255, 255), 0));
		lineBoders33.add(new LineStyle(new Color(0, 0, 143), 0));
		lineBoders33.add(new LineStyle(new Color(0, 0, 143), 0));
		lineBoders33.add(new LineStyle(new Color(0, 0, 143), 0));

		// Solo son 4 L,R,T,B
		lineBoders40.add(new LineStyle(new Color(0, 0, 143), 0));
		lineBoders40.add(new LineStyle(new Color(255, 255, 255), 0));
		lineBoders40.add(new LineStyle(new Color(255, 255, 255), 0));
		lineBoders40.add(new LineStyle(new Color(0, 0, 143), 0));

		// Solo son 4 L,R,T,B
		lineBoders4.add(new LineStyle(new Color(255, 255, 255), 0));
		lineBoders4.add(new LineStyle(new Color(0, 0, 143), 0));
		lineBoders4.add(new LineStyle(new Color(255, 255, 255), 0));
		lineBoders4.add(new LineStyle(new Color(0, 0, 143), 0));

		// Solo son 4 L,R,T,B
		lineBoders41.add(new LineStyle(new Color(255, 255, 255), 0));
		lineBoders41.add(new LineStyle(new Color(255, 255, 255), 0));
		lineBoders41.add(new LineStyle(new Color(0, 0, 143), 0));
		lineBoders41.add(new LineStyle(new Color(255, 255, 255), 0));

		// Solo son 4 L,R,T,B
		lineBoders5.add(new LineStyle(new Color(0, 0, 143), 0));
		lineBoders5.add(new LineStyle(new Color(55, 255, 255), 0));
		lineBoders5.add(new LineStyle(new Color(0, 0, 143), 0));
		lineBoders5.add(new LineStyle(new Color(255, 255, 255), 0));

		// Solo son 4 L,R,T,B
		lineBoders6.add(new LineStyle(new Color(255, 255, 255), 0));
		lineBoders6.add(new LineStyle(new Color(255, 255, 255), 0));
		lineBoders6.add(new LineStyle(new Color(255, 255, 255), 0));
		lineBoders6.add(new LineStyle(new Color(255, 255, 255), 0));

		// Solo son 4 L,R,T,B
		lineBoders61.add(new LineStyle(new Color(255, 255, 255), 0));
		lineBoders61.add(new LineStyle(new Color(255, 255, 255), 0));
		lineBoders61.add(new LineStyle(new Color(0, 0, 143), 0));
		lineBoders61.add(new LineStyle(new Color(255, 255, 255), 0));

		// Solo son 4 L,R,T,B
		lineBoders62.add(new LineStyle(new Color(0, 0, 143), 0));
		lineBoders62.add(new LineStyle(new Color(255, 255, 255), 0));
		lineBoders62.add(new LineStyle(new Color(255, 255, 255), 0));
		lineBoders62.add(new LineStyle(new Color(0, 0, 143), 0));

		// Solo son 4 L,R,T,B
		lineBoders63.add(new LineStyle(new Color(255, 255, 255), 0));
		lineBoders63.add(new LineStyle(new Color(0, 0, 143), 0));
		lineBoders63.add(new LineStyle(new Color(0, 0, 143), 0));
		lineBoders63.add(new LineStyle(new Color(0, 0, 143), 0));

		// Solo son 4 L,R,T,B
		lineBoders64.add(new LineStyle(new Color(255, 255, 255), 0));
		lineBoders64.add(new LineStyle(new Color(0, 0, 143), 0));
		lineBoders64.add(new LineStyle(new Color(255, 255, 255), 0));
		lineBoders64.add(new LineStyle(new Color(255, 255, 255), 0));

		// Solo son 4 L,R,T,B
		lineBoders65.add(new LineStyle(new Color(0, 0, 143), 0));
		lineBoders65.add(new LineStyle(new Color(255, 255, 255), 0));
		lineBoders65.add(new LineStyle(new Color(255, 255, 255), 0));
		lineBoders65.add(new LineStyle(new Color(255, 255, 255), 0));

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

		padding4.add(4f);
		padding4.add(4f);
		padding4.add(4f);
		padding4.add(4f);

		try {
			ByteArrayOutputStream output;
			try (PDDocument document = new PDDocument()) {
				try {
			
					
					if(impresionForm.isErrorPDF()) {
						output = new ByteArrayOutputStream();
						PDPage page = new PDPage();
						document.addPage(page);
						BaseTable table;
						Row<PDPage> baseRow;	
						
						table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, 30, document, page, false,true);
						baseRow = communsPdf.setRow(table, 15);
						communsPdf.setCell(baseRow, 100, "Se detectó un error en la creación del consentimiento, favor de contactar a AXA Seguros.", Color.black, false, "C", 12, lineBoders1, "", padding2,bgColorA);
						table.draw();
						
						
						

						output = new ByteArrayOutputStream();
						document.save(output);
												
						return output.toByteArray();
						
					}else {
						
						output = new ByteArrayOutputStream();
						PDPage page = new PDPage();
					
					
					document.addPage(page);
					BaseTable table;
					Row<PDPage> baseRow;
					setEncabezado(impresionForm, document, page);
					Date fecha = new Date();
					

					setContenido(impresionForm, document, page);

					/** Pagina 2 */

					page = new PDPage();
					document.addPage(page);
					setEncabezado(impresionForm, document, page);

					String newcontenido = "Advertencia:en el caso de que se desee  nombrar beneficiarios a menores de edad, no se debe señalar a un mayor de edad\n"
							+ "como representante de los menores para efecto de que, en su representación, cobre la \tindemnización. Lo anterior \tporque\n"
							+ "las legislaciones civiles previenen la forma en que deben designarse tutores, albaceas, representantes de herederos u otros\n"
							+ "cargos similares y no consideran al contrato de seguro como instrumento adecuado para tales designaciones. La designación\n"
							+ "que se hiciera de un mayor de edad como \trepresentante de menores \tbeneficiarios, \tdurante la minoría de edad de ellos,\n"
							+ "legalmente puede \timplicar que se nombra \tbeneficiario al mayor de edad, quien en \ttodo caso solo tendría una obligación\n"
							+ "moral, pues la designación \tque se hace de beneficiarios en un contrato de seguro le concede el derecho incondicionado de\n"
							+ "disponer de la Suma Asegurada. Para \tcoberturas de Pérdidas Orgánicas por Accidente e Invalidez Total y Permanente, en\n"
							+ "caso de haberse contratado y si el Integrante o Asegurado fallece sin haber recibido el pago de las coberturas, el importe de\n"
							+ "la Suma Asegurada contratada se pagará a su sucesión.";

					table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, 30, document, page, true,
							true);
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 100, "Beneficiarios", bgColor, true, "L", 10, lineBoders1, "", padding2,
							bgColorA);
					baseRow = communsPdf.setRow(table, 115);
					baseRow.setLineSpacing(1.2f);
					communsPdf.setCell(baseRow, 100, Sio4CommunsPdf.eliminaHtmlTags3(newcontenido), azul, false, "L",
							10, lineBoders, "", padding2);

					baseRow = communsPdf.setRow(table, 20);
					communsPdf.setCell(baseRow, 5, "", azul, false, "L", 10, lineBoders, "", padding3, bgColor);
					communsPdf.setCell(baseRow, 30, "Nombre", azul, true, "C", 10, lineBoders112, "", padding3,
							bgColor);
					communsPdf.setCell(baseRow, 30, "Fecha de nacimiento", azul, true, "L", 10, lineBoders11, "",
							padding3, bgColor);
					communsPdf.setCell(baseRow, 17, "Parentesco", azul, true, "L", 10, lineBoders11, "", padding3,
							bgColor);
					communsPdf.setCell(baseRow, 18, "% de participación", azul, true, "L", 10, lineBoders, "", padding3,
							bgColor);
					table.remoBordes(false, 1);
					table.draw();

					yStart -= table.getHeaderAndDataHeight();
					List<BeneficiarioProjection> beneficarios = impresionForm.getBeneficiarios();
					if (beneficarios.size() > 0) {
						int total = beneficarios.size();
						table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, 30, document, page, true,
								true);
						int x = 1;
						for (int i = 0; i < 10; i++) {
							if (i < 5) {
								if (i < 5) {

									baseRow = communsPdf.setRow(table, 20);
									communsPdf.setCell(baseRow, 5, x + "°", azul, false, "C", 10, lineBoders, "",
											padding3, bgColor);
									communsPdf.setCell(baseRow, 30,
									beneficarios.get(i).getNombres(), azul, false, "L", 10,
											lineBoders112, "", padding3, bgColor);
									communsPdf.setCell(baseRow, 30,
											formarDate(beneficarios.get(i).getFecNacimiento(),
													"dd-MM-yyyy"),
											azul, false, "L", 10, lineBoders11, "", padding3, bgColor);
									communsPdf.setCell(baseRow, 17,
									beneficarios.get(i).getParentesco(), azul, false, "L",
											10, lineBoders11, "", padding3, bgColor);
									communsPdf.setCell(baseRow, 18,
									beneficarios.get(i).getPorcentaje() + " %", azul, false,
											"C", 10, lineBoders, "", padding3, bgColor);

								} else {
									baseRow = communsPdf.setRow(table, 20);
									communsPdf.setCell(baseRow, 5, x + "°", azul, false, "C", 10, lineBoders, "",
											padding3, bgColor);
									communsPdf.setCell(baseRow, 30, "", azul, true, "C", 10, lineBoders112, "",
											padding3, bgColor);
									communsPdf.setCell(baseRow, 30, "", azul, true, "L", 10, lineBoders11, "", padding3,
											bgColor);
									communsPdf.setCell(baseRow, 17, "", azul, true, "L", 10, lineBoders11, "", padding3,
											bgColor);
									communsPdf.setCell(baseRow, 18, "", azul, true, "L", 10, lineBoders, "", padding3,
											bgColor);
								}
								if (x == total) {
									break;
								}

							} else {
								if (total > 5) {
									if (i < total) {
										if (i == total) {
											baseRow = communsPdf.setRow(table, 25);
										} else {
											baseRow = communsPdf.setRow(table, 20);
										}
										communsPdf.setCell(baseRow, 5, x + "°", azul, false, "C", 10, lineBoders, "",
												padding3, bgColor);
										communsPdf.setCell(baseRow, 30,
										beneficarios.get(i).getNombres(), azul, false, "L",
												10, lineBoders112, "", padding3, bgColor);
										communsPdf.setCell(baseRow, 30,
												formarDate(beneficarios.get(i).getFecNacimiento(),
														"dd-MM-yyyy"),
												azul, false, "L", 10, lineBoders11, "", padding3, bgColor);
										communsPdf.setCell(baseRow, 17,
										beneficarios.get(i).getParentesco(), azul, false,
												"L", 10, lineBoders11, "", padding3, bgColor);
										communsPdf.setCell(baseRow, 18,
										beneficarios.get(i).getPorcentaje() + " %", azul,
												false, "C", 10, lineBoders, "", padding3, bgColor);
									}

									if (i == total) {
										break;
									}

								}

							}

							x++;
						}
						table.remoBordes(false, 1);
						table.draw();

					} else {
						table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, 30, document, page, true,
								true);
						int x = 1;
						for (int i = 0; i < 5; i++) {
							baseRow = communsPdf.setRow(table, 20);
							communsPdf.setCell(baseRow, 5, x + "°", azul, false, "C", 10, lineBoders, "", padding3,
									bgColor);
							communsPdf.setCell(baseRow, 30, "", azul, true, "C", 10, lineBoders112, "", padding3,
									bgColor);
							communsPdf.setCell(baseRow, 30, "", azul, true, "L", 10, lineBoders11, "", padding3,
									bgColor);
							communsPdf.setCell(baseRow, 17, "", azul, true, "L", 10, lineBoders11, "", padding3,
									bgColor);
							communsPdf.setCell(baseRow, 18, "", azul, true, "L", 10, lineBoders, "", padding3, bgColor);
							x++;
						}

						table.remoBordes(false, 1);
						table.draw();
					}
					yStart -= table.getHeaderAndDataHeight() + 2;

					table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, 30, document, page, true,
							true);
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 100, "Datos de los beneficiarios", bgColor, true, "L", 10, lineBoders1,
							"", padding2, bgColorA);

					baseRow = communsPdf.setRow(table, 20);
					communsPdf.setCell(baseRow, 5, "", azul, false, "L", 10, lineBoders, "", padding3, bgColor);
					communsPdf.setCell(baseRow, 95,
							"Calle, número interior, número exterior, colonia, delegación o municipio, población o ciudad, estado, código postal",
							azul, true, "L", 9, lineBoders, "", padding, bgColor).setLeftPadding(10);
							table.draw();
					yStart -= table.getHeaderAndDataHeight();

					if (impresionForm.getBeneficiarios().size() > 0) {

					 	List<BeneficiarioProjection> beneficariox = new ArrayList<BeneficiarioProjection>();
						 beneficariox =  impresionForm.getBeneficiarios();
						int total =0;
						if(beneficariox.size() == 5){
							total =beneficariox.size();
						}else{
							total =5- beneficariox.size();
						}
						
						for (int i = 0; i < total; i++) {
							if(beneficariox.size()  < 5){
								BeneficiarioProjection ben = new BeneficiarioProjection();
								ben.setNombreEspanol("");														
								beneficariox.add( ben);
							}
						
						}
						
						int x = 1;
						table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, 30, document, page, true,true);
						for (int i = 0; i < beneficariox.size(); i++) {
				
							baseRow = communsPdf.setRow(table, 20);
									communsPdf.setCell(baseRow, 5, x + "°", azul, false, "C", 10, lineBoders, "", padding3, bgColor);
									communsPdf.setCell(baseRow, 95, beneficarios.get(i).getNombreEspanol(), azul, false, "L", 10, lineBoders, "", padding3,bgColor);

								
									x++;
						}
						table.draw();		
					yStart -= table.getHeaderAndDataHeight() ;
					}else{
						table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, 30, document, page, true,true);
						baseRow = communsPdf.setRow(table, 20);
						communsPdf.setCell(baseRow, 5, "1°", azul, false, "C", 10, lineBoders, "", padding3, bgColor);
						communsPdf.setCell(baseRow, 95, "", azul, true, "C", 10, lineBoders, "", padding3, bgColor);
						baseRow = communsPdf.setRow(table, 20);
						communsPdf.setCell(baseRow, 5, "2°", azul, false, "C", 10, lineBoders, "", padding3, bgColor);
						communsPdf.setCell(baseRow, 95, "", azul, true, "C", 10, lineBoders, "", padding3, bgColor);
						baseRow = communsPdf.setRow(table, 20);
						communsPdf.setCell(baseRow, 5, "3°", azul, false, "C", 10, lineBoders, "", padding3, bgColor);
						communsPdf.setCell(baseRow, 95, "", azul, true, "C", 10, lineBoders, "", padding3, bgColor);
						baseRow = communsPdf.setRow(table, 20);
						communsPdf.setCell(baseRow, 5, "4°", azul, false, "C", 10, lineBoders, "", padding3, bgColor);
						communsPdf.setCell(baseRow, 95, "", azul, true, "C", 10, lineBoders, "", padding3, bgColor);
						baseRow = communsPdf.setRow(table, 20);
						communsPdf.setCell(baseRow, 5, "5°", azul, false, "C", 10, lineBoders, "", padding3, bgColor);
						communsPdf.setCell(baseRow, 95, "", azul, true, "C", 10, lineBoders, "", padding3, bgColor);
						table.draw();		
						yStart -= table.getHeaderAndDataHeight() ;
					}
		
					
					table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, 30, document, page, true,
					true);
					newcontenido = "Otorgo mi \tconsentimiento  \tpara ser \tasegurado en la Póliza de Seguro de Grupo que el Contratante ha solicitado a AXA\n"
							+ "SEGUROS S.A. de C.V. de acuerdo a las Condiciones Generales de la Póliza. Tengo conocimiento de que para ingresar al\n"
							+ "Seguro debo estar en servicio activo, y adquirir las características de asegurabilidad para tomar parte del mismo.\n"
							+ "\nMis datos serán tratados de \tconformidad con lo \testablecido en el Aviso de Privacidad Integral ubicado en axa.mx, y para\n"
							+ "todos los fines del contrato de seguro.\n\n" + "Cruzar casilla que corresponda";
					baseRow = communsPdf.setRow(table, 115);
					baseRow.setLineSpacing(1.2f);
					
					communsPdf.setCell(baseRow, 100,Sio4CommunsPdf.eliminaHtmlTags3(newcontenido),azul,false, "L", 10, communsPdf.setLineStyle(bgColorA,bgColorA,Color.white,bgColorA), "", padding2,bgColor);	
					baseRow = communsPdf.setRow(table, 15);					
	               communsPdf.setCellImg(baseRow, 100, ImageUtils.readImage("https://storage.googleapis.com/quattrocrm-copsis/recursos-pdf/SINO.png").scale(70,40), communsPdf.setLineStyle(bgColorA,bgColorA,bgColorA,bgColorA), communsPdf.setPadding2(2f,0f,-22f,0f), "L", "T"); 

					table.remoBordes(false, 1);

					table.draw();

					/** Pagina 3 */

					StringBuilder conte;
					String url ="https://storage.googleapis.com/quattrocrm-copsis/recursos-pdf/Original_A.png";
					 setPage3(impresionForm, formatter, document, fecha, url);
					
					/***********Contenido duplicado**************/
					
					page = new PDPage();
					document.addPage(page);
					setEncabezado(impresionForm, document, page);
					setContenido(impresionForm, document, page);
					
					page = new PDPage();
					document.addPage(page);
					setEncabezado(impresionForm, document, page);

		

					table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, 30, document, page, true,
							true);
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 100, "Beneficiarios", bgColor, true, "L", 10, lineBoders1, "", padding2,
							bgColorA);
					baseRow = communsPdf.setRow(table, 115);
					baseRow.setLineSpacing(1.2f);
					communsPdf.setCell(baseRow, 100, Sio4CommunsPdf.eliminaHtmlTags3(newcontenido), azul, false, "L",
							10, lineBoders, "", padding2);

					baseRow = communsPdf.setRow(table, 20);
					communsPdf.setCell(baseRow, 5, "", azul, false, "L", 10, lineBoders, "", padding3, bgColor);
					communsPdf.setCell(baseRow, 30, "Nombre", azul, true, "C", 10, lineBoders112, "", padding3,
							bgColor);
					communsPdf.setCell(baseRow, 30, "Fecha de nacimiento", azul, true, "L", 10, lineBoders11, "",
							padding3, bgColor);
					communsPdf.setCell(baseRow, 17, "Parentesco", azul, true, "L", 10, lineBoders11, "", padding3,
							bgColor);
					communsPdf.setCell(baseRow, 18, "% de participación", azul, true, "L", 10, lineBoders, "", padding3,
							bgColor);
					table.remoBordes(false, 1);
					table.draw();

					yStart -= table.getHeaderAndDataHeight();
				
					if (beneficarios.size() > 0) {
						int total = beneficarios.size();
						table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, 30, document, page, true,
								true);
						int x = 1;
						for (int i = 0; i < 10; i++) {
							if (i < 5) {
								if (i < 5) {
                                     if(beneficarios.get(i).getNombres() != null){
									
									
									baseRow = communsPdf.setRow(table, 20);
									communsPdf.setCell(baseRow, 5, x + "°", azul, false, "C", 10, lineBoders, "",
											padding3, bgColor);
									communsPdf.setCell(baseRow, 30,
									beneficarios.get(i).getNombres(), azul, false, "L", 10,
											lineBoders112, "", padding3, bgColor);
									communsPdf.setCell(baseRow, 30,
											formarDate(beneficarios.get(i).getFecNacimiento(),
													"dd-MM-yyyy"),
											azul, false, "L", 10, lineBoders11, "", padding3, bgColor);
									communsPdf.setCell(baseRow, 17,
									beneficarios.get(i).getParentesco(), azul, false, "L",
											10, lineBoders11, "", padding3, bgColor);
									communsPdf.setCell(baseRow, 18,
									beneficarios.get(i).getPorcentaje() + " %", azul, false,
											"C", 10, lineBoders, "", padding3, bgColor);
									 }

								} else {
									baseRow = communsPdf.setRow(table, 20);
									communsPdf.setCell(baseRow, 5, x + "°", azul, false, "C", 10, lineBoders, "",
											padding3, bgColor);
									communsPdf.setCell(baseRow, 30, "", azul, true, "C", 10, lineBoders112, "",
											padding3, bgColor);
									communsPdf.setCell(baseRow, 30, "", azul, true, "L", 10, lineBoders11, "", padding3,
											bgColor);
									communsPdf.setCell(baseRow, 17, "", azul, true, "L", 10, lineBoders11, "", padding3,
											bgColor);
									communsPdf.setCell(baseRow, 18, "", azul, true, "L", 10, lineBoders, "", padding3,
											bgColor);
								}
								if (x == total) {
									break;
								}

							} else {
								if (total > 5) {
									if (i < total) {
										if(beneficarios.get(i).getNombres() != null){
										if (i == total) {
											baseRow = communsPdf.setRow(table, 25);
										} else {
											baseRow = communsPdf.setRow(table, 20);
										}
										communsPdf.setCell(baseRow, 5, x + "°", azul, false, "C", 10, lineBoders, "",
												padding3, bgColor);
										communsPdf.setCell(baseRow, 30,
										beneficarios.get(i).getNombres(), azul, false, "L",
												10, lineBoders112, "", padding3, bgColor);
										communsPdf.setCell(baseRow, 30,
												formarDate(beneficarios.get(i).getFecNacimiento(),
														"dd-MM-yyyy"),
												azul, false, "L", 10, lineBoders11, "", padding3, bgColor);
										communsPdf.setCell(baseRow, 17,
										beneficarios.get(i).getParentesco(), azul, false,
												"L", 10, lineBoders11, "", padding3, bgColor);
										communsPdf.setCell(baseRow, 18,
										beneficarios.get(i).getPorcentaje() + " %", azul,
												false, "C", 10, lineBoders, "", padding3, bgColor);
									}
									}

									if (i == total) {
										break;
									}

								}

							}

							x++;
						}
						table.remoBordes(false, 1);
						table.draw();

					} else {
						table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, 30, document, page, true,
								true);
						int x = 1;
						for (int i = 0; i < 5; i++) {
							baseRow = communsPdf.setRow(table, 20);
							communsPdf.setCell(baseRow, 5, x + "°", azul, false, "C", 10, lineBoders, "", padding3,
									bgColor);
							communsPdf.setCell(baseRow, 30, "", azul, true, "C", 10, lineBoders112, "", padding3,
									bgColor);
							communsPdf.setCell(baseRow, 30, "", azul, true, "L", 10, lineBoders11, "", padding3,
									bgColor);
							communsPdf.setCell(baseRow, 17, "", azul, true, "L", 10, lineBoders11, "", padding3,
									bgColor);
							communsPdf.setCell(baseRow, 18, "", azul, true, "L", 10, lineBoders, "", padding3, bgColor);
							x++;
						}

						table.remoBordes(false, 1);
						table.draw();
					}
					yStart -= table.getHeaderAndDataHeight() + 2;

					table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, 30, document, page, true,
							true);
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 100, "Datos de los beneficiarios", bgColor, true, "L", 10, lineBoders1,
							"", padding2, bgColorA);

					baseRow = communsPdf.setRow(table, 20);
					communsPdf.setCell(baseRow, 5, "", azul, false, "L", 10, lineBoders, "", padding3, bgColor);
					communsPdf.setCell(baseRow, 95,
							"Calle, número interior, número exterior, colonia, delegación o municipio, población o ciudad, estado, código postal",
							azul, true, "L", 9, lineBoders, "", padding, bgColor).setLeftPadding(10);
					table.draw();
					yStart -= table.getHeaderAndDataHeight() ;		


					if (impresionForm.getBeneficiarios().size() > 0) {

						List<BeneficiarioProjection> beneficariox = new ArrayList<BeneficiarioProjection>();
						beneficariox =  impresionForm.getBeneficiarios();
					   int total =0;
					   if(beneficariox.size() == 5){
						   total =beneficariox.size();
					   }else{
						   total =5- beneficariox.size();
					   }
					   
					   for (int i = 0; i < total; i++) {
						   if(beneficariox.size()  < 5){
							   BeneficiarioProjection ben = new BeneficiarioProjection();
							   ben.setNombreEspanol("");														
							   beneficariox.add( ben);
						   }
					   
					   }
					   
					   int x = 1;
					   table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, 30, document, page, true,true);
					   for (int i = 0; i < beneficariox.size(); i++) {
			   
						   baseRow = communsPdf.setRow(table, 20);
								   communsPdf.setCell(baseRow, 5, x + "°", azul, false, "C", 10, lineBoders, "", padding3, bgColor);
								   communsPdf.setCell(baseRow, 95, beneficarios.get(i).getNombreEspanol(), azul, false, "L", 10, lineBoders, "", padding3,bgColor);

							   
								   x++;
					   }
					   table.draw();		
				   yStart -= table.getHeaderAndDataHeight() ;
				   }else{
					   table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, 30, document, page, true,true);
					   baseRow = communsPdf.setRow(table, 20);
					   communsPdf.setCell(baseRow, 5, "1°", azul, false, "C", 10, lineBoders, "", padding3, bgColor);
					   communsPdf.setCell(baseRow, 95, "", azul, true, "C", 10, lineBoders, "", padding3, bgColor);
					   baseRow = communsPdf.setRow(table, 20);
					   communsPdf.setCell(baseRow, 5, "2°", azul, false, "C", 10, lineBoders, "", padding3, bgColor);
					   communsPdf.setCell(baseRow, 95, "", azul, true, "C", 10, lineBoders, "", padding3, bgColor);
					   baseRow = communsPdf.setRow(table, 20);
					   communsPdf.setCell(baseRow, 5, "3°", azul, false, "C", 10, lineBoders, "", padding3, bgColor);
					   communsPdf.setCell(baseRow, 95, "", azul, true, "C", 10, lineBoders, "", padding3, bgColor);
					   baseRow = communsPdf.setRow(table, 20);
					   communsPdf.setCell(baseRow, 5, "4°", azul, false, "C", 10, lineBoders, "", padding3, bgColor);
					   communsPdf.setCell(baseRow, 95, "", azul, true, "C", 10, lineBoders, "", padding3, bgColor);
					   baseRow = communsPdf.setRow(table, 20);
					   communsPdf.setCell(baseRow, 5, "5°", azul, false, "C", 10, lineBoders, "", padding3, bgColor);
					   communsPdf.setCell(baseRow, 95, "", azul, true, "C", 10, lineBoders, "", padding3, bgColor);
					   table.draw();		
					   yStart -= table.getHeaderAndDataHeight() ;
				   }
	   

					
				
					table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, 30, document, page, true,
					true);
					newcontenido = "Otorgo mi \tconsentimiento  \tpara ser \tasegurado en la Póliza de Seguro de Grupo que el Contratante ha solicitado a AXA\n"
							+ "SEGUROS S.A. de C.V. de acuerdo a las Condiciones Generales de la Póliza. Tengo conocimiento de que para ingresar al\n"
							+ "Seguro debo estar en servicio activo, y adquirir las características de asegurabilidad para tomar parte del mismo.\n"
							+ "\nMis datos serán tratados de \tconformidad con lo \testablecido en el Aviso de Privacidad Integral ubicado en axa.mx, y para\n"
							+ "todos los fines del contrato de seguro.\n\n" + "Cruzar casilla que corresponda";
					baseRow = communsPdf.setRow(table, 115);
					baseRow.setLineSpacing(1.2f);
				       communsPdf.setCell(baseRow, 100,Sio4CommunsPdf.eliminaHtmlTags3(newcontenido),azul,false, "L", 10, communsPdf.setLineStyle(bgColorA,bgColorA,Color.white,bgColorA), "", padding2,bgColor);  
	                    baseRow = communsPdf.setRow(table, 15);                 
	                   communsPdf.setCellImg(baseRow, 100, ImageUtils.readImage("https://storage.googleapis.com/quattrocrm-copsis/recursos-pdf/SINO.png").scale(70,40), communsPdf.setLineStyle(bgColorA,bgColorA,bgColorA,bgColorA), communsPdf.setPadding2(2f,0f,-22f,0f), "L", "T"); 

					table.remoBordes(false, 1);

					table.draw();
					String url2 ="https://storage.googleapis.com/quattrocrm-copsis/recursos-pdf/Original_B.png";
					setPage3(impresionForm, formatter, document, fecha ,url2);


					
					
					
				

					int nume2 = document.getNumberOfPages();
					
					for (int v = 0; v < nume2; v++) {
						PDPage page3 = document.getPage(v);
						try (PDPageContentStream content = new PDPageContentStream(document, page3,
								PDPageContentStream.AppendMode.PREPEND, true, true)) {
								
							communsPdf.drawText(content, false, 500, 754, "VI-557• ENER0 2019", azul, true);
						}
					}
					
					

					output = new ByteArrayOutputStream();
					document.save(output);
					
			
			
					conte =null;								
					return output.toByteArray();
					
					}
					
				} finally {
					document.close();
				}

			}

		} catch (Exception ex) {			
			throw new GeneralServiceException("00001",
					"Ocurrio un error en el servicio ImpresionInter: " + ex.getMessage());
		}

	}


	private String setPage3(ImpresionForm impresionForm, SimpleDateFormat formatter, PDDocument document, Date fecha,String url)
			throws IOException, MalformedURLException {
		PDPage page;
		BaseTable table;
		Row<PDPage> baseRow;
		String newcontenido;
		page = new PDPage();
		document.addPage(page);
		setEncabezado(impresionForm, document, page);

		newcontenido = "Otorgo mi consentimiento expreso para que AXA transfiera con mi agente o intermediario de seguros, la siniestralidad de mi\n"
				+ "Póliza. En este \tsentido, el agente \ttendrá carácter de Responsable en términos de la Ley Federal de Datos Personales en\n"
				+ "Posesión de los Particulares de los datos personales y datos personales sensbibles que AXA le transfiera.\n"
				+ "\nEn caso de haber \tproporcionado \tinformación \tpersonal de otros \ttitulares (solicitantes), \tacepto la \tresponsabilidad y mi\n"
				+ "obligación de informarles de esta entrega, así como los lugares en los que se encuentra \tdisponible el Aviso de Privacidad,\n"
				+ "para su consulta";

		
		table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, 30, document, page, true,
				true);
		
		baseRow = communsPdf.setRow(table, 90);
		baseRow.setLineSpacing(1.2f);
		communsPdf.setCell(baseRow, 100, Sio4CommunsPdf.eliminaHtmlTags3(newcontenido), azul, false, "L",
				10, lineBoders2, "", padding2);
		baseRow = communsPdf.setRow(table, 15);

		
		communsPdf.setCell(baseRow, 13, "Lugar y fecha", azul, true, "L", 10, lineBoders62, "", padding3,
				bgColor);
		communsPdf.setCell(baseRow, 87,"México a " +formatter.format(fecha), azul, false, "L", 10, lineBoders63, "", padding3, bgColor);

		newcontenido = "Para cualquier \taclaración o duda no \tresuelta en \trelación con su seguro, \tcontacte a la \tUnidad Especializada de nuestra\n"
				+ "compañía en la \tdirección indicada al pie de \tpágina. Tel. 01 800 737 76 63 (opción 1) y desde la Cd. de México 5169 2746\n"
				+ "(opción 1) de lunes a jueves de 8:00 a 17:30 horas y viernes de 8:00 a 16:00 horas, o escríbanos a axasoluciones@axa.com.\n"
				+ "mx; o bien, comunicarse a Condusef: Av. Insurgentes Sur #762 Col. Del Valle Cd. de México C.P 03100 - Tel. (55)5340 0999\n"
				+ "y (01 800) 999 80 80, asesoria@condusef.gob.mx";
		baseRow = communsPdf.setRow(table, 65);
		baseRow.setLineSpacing(1.2f);
		communsPdf.setCell(baseRow, 100, Sio4CommunsPdf.eliminaHtmlTags3(newcontenido), azul, false, "L",
				10, lineBoders2, "", padding2);
		
		StringBuilder conte= new StringBuilder();

		conte.append("En \tcumplimiento a lo \tdispuesto en el \tartículo 202 de la Ley de \tInstituciones de \tSeguros y\n");
				conte.append( "de \tFianzas, la \tdocumentación contractual y la nota \ttécnica que \tintegran este \tproducto de\n");
						conte.append( "seguro, \tquedaron \tregistradas ante la \tComisión \tNacional de Seguros y Fianzas,");
				
				if(impresionForm.getTextoConsentimiento().length() > 0) {
					conte.append( impresionForm.getTextoConsentimiento() );	
				}else {
					conte.append( "a partir del\n"
							+ impresionForm.getNotaTecnica());
				}
				
				
		baseRow = communsPdf.setRow(table, 60);
		baseRow.setLineSpacing(1.2f);
		communsPdf.setCell(baseRow, 100, Sio4CommunsPdf.eliminaHtmlTags3(conte.toString()), azul, false, "L",
				13, lineBoders, "", padding2);

		baseRow = communsPdf.setRow(table, 15);
		communsPdf.setCell(baseRow, 50, "Firma y sello del Contratante", azul, true, "L", 10, lineBoders,
				"", padding2, bgColorAb);
		communsPdf.setCell(baseRow, 50, "Firma del Integrante del Grupo asegurado", azul, true, "L", 10,
				lineBoders, "", padding2, bgColorAb);

		baseRow = communsPdf.setRow(table, 80);
		communsPdf.setCell(baseRow, 50, "", azul, false, "L", 10, lineBoders, "", padding3, bgColor);
		communsPdf.setCell(baseRow, 50, impresionForm.getHashCode(), azul, false, "L", 10, lineBoders, "",
				padding3, bgColor);

		baseRow = communsPdf.setRow(table, 15);
		communsPdf.setCell(baseRow, 100, "Comentarios", azul, true, "L", 10, lineBoders, "", padding2,
				bgColorAb);

		if (impresionForm.getComentario() != null &&  impresionForm.getComentario().length() >= 123) {
			baseRow = communsPdf.setRow(table, 24);
		} else {
			baseRow = communsPdf.setRow(table, 15);
		}
		communsPdf.setCell(baseRow, 100, impresionForm.getComentario(), azul, false, "L", 10, lineBoders,
				"", padding3, bgColor);
		table.remoBordes(false, 1);

		table.draw();

		table = new BaseTable(35, yStartNewPage, 0, 500, 503, document, page, true, true);
		baseRow = communsPdf.setRow(table, 15);
		communsPdf.setCell(baseRow, 20,ImageUtils.readImage(url).scale(80, 50),2, 103, black)
				.setTopPadding(-22);

		table.draw();
		return newcontenido;
	}


	private void setContenido(ImpresionForm impresionForm, PDDocument document, PDPage page)
			throws IOException, MalformedURLException {
		BaseTable table;
		Row<PDPage> baseRow;
		BaseTable table2;
		Row<PDPage> baseRow2;
		BaseTable table3;
		Row<PDPage> baseRow3;
		BaseTable table4;
		Row<PDPage> baseRow4;
		table2 = new BaseTable((yStart - 2), yStartNewPage, bottomMargin, fullWidth, 30, document, page,
				true, true);
		table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, 30, document, page, false,
				true);
		baseRow2 = communsPdf.setRow(table2, 12);
		baseRow = communsPdf.setRow(table, 15);

		communsPdf.setCell(baseRow, 10, "Contratante", azul, false, "L", 10, lineBoders, "", padding,
				bgColor);
		if(impresionForm.getContrannte().length() > 48) {
			communsPdf.setCell(baseRow, 50, impresionForm.getContrannte(), azul, false, "L", 9, lineBoders, "",
					padding, bgColor).setTopPadding(-2f);
		}else {
			communsPdf.setCell(baseRow, 50, impresionForm.getContrannte(), azul, false, "L", 9, lineBoders, "",
					padding, bgColor);
		}

		communsPdf.setCell(baseRow, 10, "Categoría", azul, false, "L", 10, lineBoders, "", padding, bgColor)
				.setLeftPadding(6f);
		communsPdf.setCell(baseRow, 30, impresionForm.getCategoria(), azul, false, "L", 10, lineBoders, "",
				padding, bgColor);
		/***/
		communsPdf.setCell(baseRow2, 10, "", azul, false, "L", 13, lineBoders6, "", padding, bgColor);
		communsPdf.setCell(baseRow2, 50, "", azul, false, "L", 13, lineBoders61, "", padding, bgColor);
		communsPdf.setCell(baseRow2, 10, "", azul, false, "L", 12, lineBoders6, "", padding, bgColor);
		communsPdf.setCell(baseRow2, 30, "", azul, false, "L", 13, lineBoders61, "", padding, bgColor);

		baseRow = communsPdf.setRow(table, 10);
		baseRow = communsPdf.setRow(table, 15);
		baseRow2 = communsPdf.setRow(table2, 11);
		baseRow2 = communsPdf.setRow(table2, 15);
		communsPdf.setCell(baseRow, 10, "No. Póliza", azul, false, "L", 10, lineBoders, "", padding,
				bgColor);
		communsPdf.setCell(baseRow, 40, impresionForm.getNopoliza(), azul, false, "L", 10, lineBoders, "",
				padding, bgColor);

		communsPdf.setCell(baseRow2, 10, "", azul, false, "L", 13, lineBoders6, "", padding, bgColor);
		communsPdf.setCell(baseRow2, 40, "", azul, false, "L", 13, lineBoders61, "", padding, bgColor);
		table2.draw();
		table.draw();

		yStart -= table.getHeaderAndDataHeight() + 20;
		/* Pagina 1 */
		table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, 30, document, page, true,
				true);
		baseRow = communsPdf.setRow(table, 15);
		communsPdf.setCell(baseRow, 100, "Datos del Integrante", bgColor, true, "L", 10, lineBoders1, "",
				padding2, bgColorA);

		baseRow = communsPdf.setRow(table, 15);
		baseRow.setLineSpacing(-0.9f);
		communsPdf.setCell(baseRow, 75, "Nombre(s), apellido paterno, apellido materno", azul, false, "L",
				10, lineBoders2, "", padding3, bgColor);
		communsPdf.setCell(baseRow, 25, "Empleado No.", azul, false, "L", 10, lineBoders2, "", padding3,
				bgColor);
		baseRow = communsPdf.setRow(table, 15);

		communsPdf.setCell(baseRow, 75, impresionForm.getAsegurado(), azul, false, "L", 9, lineBoders, "",
				padding3, bgColor);
		communsPdf.setCell(baseRow, 25, impresionForm.getNomina(), azul, false, "L", 10, lineBoders, "",
				padding3, bgColor);

		baseRow = communsPdf.setRow(table, 15);
		communsPdf.setCell(baseRow, 50, "RFC", azul, false, "L", 10, lineBoders2, "", padding3, bgColor);
		communsPdf.setCell(baseRow, 50, "Género", azul, false, "L", 10, lineBoders2, "", padding3, bgColor);
		baseRow = communsPdf.setRow(table, 18);
		communsPdf.setCell(baseRow, 50, impresionForm.getRfc(), azul, false, "L", 10, lineBoders, "",
				padding3, bgColor);
		if (impresionForm.isSexo()) {
			communsPdf.setCell(baseRow, 50,
					ImageUtils.readImage(
							"https://storage.googleapis.com/quattrocrm-copsis/recursos-pdf/log-mm.png")
							.scale(130, 120),
					0, 103, black);
		} else {
			communsPdf.setCell(baseRow, 50,
					ImageUtils.readImage(
							"https://storage.googleapis.com/quattrocrm-copsis/recursos-pdf/log-ff.png")
							.scale(130, 120),
					0, 103, black);
		}

		baseRow = communsPdf.setRow(table, 15);
		communsPdf.setCell(baseRow, 50,
				"Fecha de nacimiento " + Sio4CommunsPdf.eliminaHtmlTags3(
						"\n                                                          Dia   Mes   Año"),
				azul, false, "L", 10, lineBoders2, "", padding3, bgColor);
		communsPdf.setCell(baseRow, 50, "Ocupación o profesión", azul, false, "L", 10, lineBoders2, "",
				padding3, bgColor);

		baseRow = communsPdf.setRow(table, 15);
		communsPdf.setCell(baseRow, 50,
				impresionForm.getNacimiento().split("-")[2] + "     "
						+ impresionForm.getNacimiento().split("-")[1] + "     "
						+ impresionForm.getNacimiento().split("-")[0],
				azul, false, "C", 10, lineBoders, "", padding3, bgColor).setLeftPadding(120);
		communsPdf.setCell(baseRow, 50, impresionForm.getOcupacion(), azul, false, "L", 10, lineBoders, "",
				padding3, bgColor);

		baseRow = communsPdf.setRow(table, 15);
		communsPdf.setCell(baseRow, 50, "Correo electrónico", azul, false, "L", 10, lineBoders2, "",
				padding3, bgColor);
		communsPdf.setCell(baseRow, 50, "País de nacimiento", azul, false, "L", 10, lineBoders2, "",
				padding3, bgColor);

		baseRow = communsPdf.setRow(table, 15);
		communsPdf.setCell(baseRow, 50, impresionForm.getCorreo(), azul, false, "L", 10, lineBoders, "",
				padding3, bgColor);
		communsPdf.setCell(baseRow, 50, impresionForm.getPaisNacimiento(), azul, false, "L", 10, lineBoders,
				"", padding3, bgColor);

		baseRow = communsPdf.setRow(table, 15);
		communsPdf.setCell(baseRow, 75, "Domicilio", azul, true, "L", 10, lineBoders2, "", padding3,
				bgColor);
		communsPdf.setCell(baseRow, 12, "No. exterior", azul, false, "L", 10, lineBoders2, "", padding3,
				bgColor);
		communsPdf.setCell(baseRow, 13, "No. interior", azul, false, "L", 10, lineBoders2, "", padding3,
				bgColor);
		baseRow = communsPdf.setRow(table, 15);
		communsPdf.setCell(baseRow, 75, impresionForm.getCalle(), azul, false, "L", 10, lineBoders, "",
				padding3, bgColor);
		communsPdf.setCell(baseRow, 12, impresionForm.getNoExterior(), azul, false, "L", 10, lineBoders, "",
				padding3, bgColor);
		communsPdf.setCell(baseRow, 13, impresionForm.getNoInterior(), azul, false, "L", 10, lineBoders, "",
				padding3, bgColor);

		baseRow = communsPdf.setRow(table, 15);
		communsPdf.setCell(baseRow, 33, "Colonia", azul, false, "L", 10, lineBoders2, "", padding3,
				bgColor);
		communsPdf.setCell(baseRow, 33, "Población (delegación o municipio)", azul, false, "L", 10,
				lineBoders2, "", padding3, bgColor);
		communsPdf.setCell(baseRow, 34, "Estado", azul, false, "L", 10, lineBoders2, "", padding3, bgColor);
		baseRow = communsPdf.setRow(table, 15);
		communsPdf.setCell(baseRow, 33, impresionForm.getColonia(), azul, false, "L", 10, lineBoders, "",
				padding3, bgColor);
		communsPdf.setCell(baseRow, 33, impresionForm.getMunicipo(), azul, false, "L", 10, lineBoders, "",
				padding3, bgColor);
		communsPdf.setCell(baseRow, 34, impresionForm.getEstado(), azul, false, "L", 10, lineBoders, "",
				padding3, bgColor);

		baseRow = communsPdf.setRow(table, 15);
		communsPdf.setCell(baseRow, 40, "Código postal", azul, false, "L", 10, lineBoders2, "", padding3,
				bgColor);
		communsPdf.setCell(baseRow, 60, "Teléfono (con clave de ciudad)", azul, false, "L", 10, lineBoders2,
				"", padding3, bgColor);
		baseRow = communsPdf.setRow(table, 15);
		communsPdf.setCell(baseRow, 40, impresionForm.getCp(), azul, false, "L", 10, lineBoders, "",
				padding3, bgColor);
		communsPdf.setCell(baseRow, 60, impresionForm.getTelefono(), azul, false, "L", 10, lineBoders, "",
				padding3, bgColor);

		baseRow = communsPdf.setRow(table, 15);
		communsPdf.setCell(baseRow, 100, "Llenar estos datos en caso de ser extranjero", bgColor, true, "L",
				10, lineBoders1, "", padding2, bgColorA);

		baseRow = communsPdf.setRow(table, 15);
		communsPdf.setCell(baseRow, 30, "Domicilio en su país de origen", azul, true, "L", 10, lineBoders2,
				"", padding3, bgColor);
		communsPdf.setCell(baseRow, 10, "No.", azul, false, "L", 10, lineBoders2, "", padding3, bgColor);
		communsPdf.setCell(baseRow, 20, "Colonia", azul, false, "L", 10, lineBoders2, "", padding3,
				bgColor);
		communsPdf.setCell(baseRow, 15, "Código postal", azul, false, "L", 10, lineBoders2, "", padding3,
				bgColor);
		communsPdf.setCell(baseRow, 25, "Delegación o municipio", azul, false, "L", 10, lineBoders2, "",
				padding3, bgColor);

		baseRow = communsPdf.setRow(table,25);				
		communsPdf.setCell(baseRow, 30, "Calle " + impresionForm.getCalleExt(), azul, false, "L", 10, lineBoders, "", padding3, bgColor);
		communsPdf.setCell(baseRow, 10, impresionForm.getNumeroExt(), azul, false, "L", 10, lineBoders, "", padding3, bgColor);
		communsPdf.setCell(baseRow, 20, impresionForm.getColoniaExt(), azul, false, "L", 10, lineBoders, "", padding3, bgColor);
		communsPdf.setCell(baseRow, 15, impresionForm.getCpExt(), azul, false, "L", 10, lineBoders, "", padding3, bgColor);
		communsPdf.setCell(baseRow, 25, impresionForm.getMunicipioExt(), azul, false, "L", 10, lineBoders, "", padding3, bgColor);

		baseRow = communsPdf.setRow(table, 20);
		communsPdf.setCell(baseRow, 30, "Ciudad", azul, false, "L", 10, lineBoders2, "", padding3, bgColor);
		communsPdf.setCell(baseRow, 30, "Estado", azul, false, "L", 10, lineBoders2, "", padding3, bgColor);
		communsPdf.setCell(baseRow, 15, "País", azul, false, "L", 10, lineBoders2, "", padding3, bgColor);
		communsPdf.setCell(baseRow, 25, "Teléfono", azul, false, "L", 10, lineBoders2, "", padding3,
				bgColor);

		baseRow = communsPdf.setRow(table, 15);
		communsPdf.setCell(baseRow, 30, impresionForm.getCiudadExt(), azul, false, "L", 10, lineBoders2, "", padding4, bgColor);
		communsPdf.setCell(baseRow, 30, impresionForm.getEstadoExt(), azul, false, "L", 10, lineBoders2, "", padding4, bgColor);
		communsPdf.setCell(baseRow, 15, impresionForm.getPaisExt(), azul, false, "L", 10, lineBoders2, "", padding4, bgColor);
		communsPdf.setCell(baseRow, 25, impresionForm.getTelefonoExt(), azul, false, "L", 10, lineBoders2, "", padding4, bgColor);

		table.remoBordes(false, 1);
		table.draw();

		yStart -= table.getHeaderAndDataHeight() + 20;
		yStartStar = yStart;
		table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, 30, document, page, true,
				true);
		baseRow = communsPdf.setRow(table, 15);
		communsPdf.setCell(baseRow, 100, "Vigencia", bgColor, true, "L",
				10, lineBoders1, "", padding2, bgColorA);


		baseRow = communsPdf.setRow(table, 20);
		communsPdf.setCell(baseRow, 40, "Fecha de inicio de vigencia del consentimiento:", azul, false, "L",
				10, lineBoders3101, "", padding3, bgColor);
		communsPdf.setCell(baseRow, 20, "Día     ", azul, false, "L", 10, lineBoders31, "", padding3,
				bgColor);
		communsPdf.setCell(baseRow, 20, "Mes     ", azul, false, "L", 10, lineBoders31, "", padding3,
				bgColor);
		communsPdf.setCell(baseRow, 20, "Año     ", azul, false, "L", 10, lineBoders32, "", padding3,
				bgColor);

		baseRow = communsPdf.setRow(table, 20);
		communsPdf.setCell(baseRow, 40, "Fecha de término de vigencia del consentimiento:", azul, false,
				"L", 10, lineBoders3101, "", padding3, bgColor);
		communsPdf.setCell(baseRow, 20, "Día     ", azul, false, "L", 10, lineBoders31, "", padding3,
				bgColor);
		communsPdf.setCell(baseRow, 20, "Mes     ", azul, false, "L", 10, lineBoders31, "", padding3,
				bgColor);
		communsPdf.setCell(baseRow, 20, "Año     ", azul, false, "L", 10, lineBoders32, "", padding3,
				bgColor);
		table.remoBordes(false, 1);
		table.draw();

		yStart -= table.getHeaderAndDataHeight();

		table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, 30, document, page, true,
				true);
		baseRow = communsPdf.setRow(table, 25);
		communsPdf.setCell(baseRow, 16, "Tipo de afiliación:", azul, true, "L", 10, lineBoders3101, "",
				padding3, bgColor);
		communsPdf.setCell(baseRow, 84, "prestación laboral", azul, false, "L", 10, lineBoders32, "",
				padding3, bgColor);


		baseRow = communsPdf.setRow(table, 15);
		communsPdf.setCell(baseRow, 100, "Coberturas amparadas", bgColor, true, "L", 10, communsPdf.setLineStyle(azul, azul, Color.gray, Color.green), "",
				padding2, bgColorA);

		baseRow = communsPdf.setRow(table, 15);
		communsPdf.setCell(baseRow, 82, "Regla para determinar la Suma Asegurada", azul, false, "R", 10,
				communsPdf.setLineStyle(azul, azul,azul, azul), "", padding3, bgColor);
		communsPdf
				.setCell(baseRow, 18, "Incluido", azul, true, "c", 10, lineBoders33, "", padding3, bgColor)
				.setLeftPadding(16);
	
		table.remoBordes(false, 1);
		table.draw();
		yStart -= table.getHeaderAndDataHeight()+3;

		table4 = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, 30, document, page, false,
				true);
		table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, 30, document, page, true,
				true);
		
		List<String> meses = Arrays.asList("a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z");
		ArrayList<PaqueteCoberturaProjection> coberturas = new ArrayList<PaqueteCoberturaProjection>();

			for (int i = 0; i < impresionForm.getCoberturas().size(); i++) {
			PaqueteCoberturaProjection cob = new PaqueteCoberturaProjection();

			cob.setLetra(meses.get(i));
			cob.setNombres( impresionForm.getCoberturas().get(i).getNombres());
			cob.setIncluido( impresionForm.getCoberturas().get(i).getIncluido());
			cob.setCoberturaValor( impresionForm.getCoberturas().get(i).getCoberturaValor());
			coberturas.add(cob);
				}

		if (coberturas.size() > 0) {
			for (int i = 0; i < coberturas.size(); i++) {
				baseRow = communsPdf.setRow(table, 17);
				baseRow4 = communsPdf.setRow(table4, 17);				
				communsPdf.setCell(baseRow4, 47, "", azul, false, "L", 10, lineBoders65, "", padding3,bgColor);
				communsPdf.setCell(baseRow, 47 ,coberturas.get(i).getLetra() +")"+ coberturas.get(i).getNombres(), azul, false, "L", 10,lineBoders65, "", padding3, bgColor);
				communsPdf.setCell(baseRow, 38, (coberturas.get(i).getCoberturaValor().length() > 0 ? coberturas.get(i).getCoberturaValor() :""), azul, false, "L", 10, lineBoders41, "",padding3, bgColor);
				 if (coberturas.get(i).getIncluido() == 1) {
						communsPdf.setCell(baseRow, 15, "Incluido", azul, false, "C", 10, lineBoders64, "",
								padding3, bgColor);
					}else {
						communsPdf.setCell(baseRow, 15, "", azul, false, "C", 10, lineBoders64, "",
								padding3, bgColor);
					}

				}
			}						 
						 
	
		if (coberturas.size() == 0) {
			baseRow = communsPdf.setRow(table, 125);
			communsPdf.setCell(baseRow, 100, "", azul, false, "L", 10, lineBoders, "", padding3, bgColor);

		}else {
		baseRow = communsPdf.setRow(table, 5);
		communsPdf.setCell(baseRow, 100, "", azul, false, "L", 10, lineBoders, "", padding3, bgColor);

		}
		baseRow4 = communsPdf.setRow(table4, 5);
		communsPdf.setCell(baseRow4, 100, "", azul, false, "L", 10, lineBoders, "", padding3, bgColor);

		table.remoBordes(false, 1);
		table.draw();

		table4.remoBordes(false, 1);
		table4.draw();

		PDPageContentStream content0 = new PDPageContentStream(document, page,
				PDPageContentStream.AppendMode.APPEND, true);
		table3 = new BaseTable(yStartStar, yStartNewPage, bottomMargin, fullWidth, 30, document, page,
				false, true);
		table3.setTableContentStream(content0);
		baseRow3 = communsPdf.setRow(table3, 8);
		communsPdf.setCell(baseRow3, 43, "", azul, false, "L", 10, lineBoders, "", padding3, bgColor);
		communsPdf.setCell(baseRow3, 21, "_______________", azul, false, "l", 10, lineBoders, "", padding3,
				bgColor);
		communsPdf.setCell(baseRow3, 20, "_______________", azul, false, "l", 10, lineBoders, "", padding3,
				bgColor);
		communsPdf.setCell(baseRow3, 20, "_____________", azul, false, "l", 10, lineBoders, "", padding3,
				bgColor);
		baseRow3 = communsPdf.setRow(table3, 5);

		baseRow3 = communsPdf.setRow(table3, 15);
		communsPdf.setCell(baseRow3, 43, "", azul, false, "L", 10, lineBoders, "", padding3, bgColor);
		communsPdf.setCell(baseRow3, 21, "_______________", azul, false, "l", 10, lineBoders, "", padding3,
				bgColor);
		communsPdf.setCell(baseRow3, 20, "_______________", azul, false, "l", 10, lineBoders, "", padding3,
				bgColor);
		communsPdf.setCell(baseRow3, 20, "_____________", azul, false, "l", 10, lineBoders, "", padding3,
				bgColor);

		baseRow3 = communsPdf.setRow(table3, 5);

		baseRow3 = communsPdf.setRow(table3, 15);
		communsPdf.setCell(baseRow3, 43, "", azul, false, "L", 10, lineBoders, "", padding3, bgColor);
		communsPdf.setCell(baseRow3, 21, "_______________", azul, false, "l", 10, lineBoders, "", padding3,
				bgColor);
		communsPdf.setCell(baseRow3, 20, "_______________", azul, false, "l", 10, lineBoders, "", padding3,
				bgColor);
		communsPdf.setCell(baseRow3, 20, "_____________", azul, false, "l", 10, lineBoders, "", padding3,
				bgColor);
		table3.draw();
	}


	private void setEncabezado(ImpresionForm impresionForm, PDDocument document, PDPage page) {
		try (PDPageContentStream content = new PDPageContentStream(document, page)) {
			BaseTable table;
			Row<PDPage> baseRow;
			yStart = 762;
			table = new BaseTable(yStart, yStartNewPage, bottomMargin, 100, 30, document, page, false, true);
			baseRow = communsPdf.setRow(table, 15);
			communsPdf.setCell(baseRow, 100,
					ImageUtils.readImage("https://storage.googleapis.com/quattrocrm-copsis/recursos-pdf/logo-AXA.png"),
					0, 0, black);
			table.draw();

			table = new BaseTable((yStart - 20), yStartNewPage, bottomMargin, 300, 290, document, page, false, true);
			baseRow = communsPdf.setRow(table, 15);
			communsPdf.setCell(baseRow, 100, "Vida", bgColorCl, true, "r", 11, lineBoders, "", padding);
			baseRow = communsPdf.setRow(table, 15);
			communsPdf.setCell(baseRow, 100, "Consentimiento Individual / Seguro de grupo", azul, true, "r", 11,lineBoders, "", padding);
			table.draw();
			yStart -= table.getHeaderAndDataHeight() + 40;

			table = new BaseTable(30, yStartNewPage, 0, fullWidth + 30, 10, document, page, false, true);
			baseRow = communsPdf.setRow(table, 15);
			communsPdf.setCell(baseRow, 100,
					"AXA Seguros, S.A. de C.V. Félix Cuevas 366, Piso 6, Col. Tlacoquemécatl, Del. Benito Juárez, 03200, CDMX, México • Tels. 5169 1000 • 01 800 900 1292 • axa.mx",
					azul, false, "r", 8, lineBoders, "", padding);
			table.draw();

		} catch (Exception ex) {

		}

	}

	private static String formarDate(String dateD, String format) {
		SimpleDateFormat formatter = null;
		Date date = null;
		try {
			formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			date = formatter.parse(dateD);
		} catch (ParseException e) {
			throw new GeneralServiceException("00001", "Fallo en el fomateo de datos.");
		}

		DateFormatSymbols sym = DateFormatSymbols.getInstance(new Locale("es", "MX"));
		sym.setMonths(new String[] { "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto",
				"Septiembre", "Octubre", "Noviembre", "Diciembre" });

		sym.setShortMonths(
				new String[] { "Ene", "Feb", "Mar", "Abr", "May", "Jun", "Jul", "Ago", "Sep", "Oct", "Nov", "Dic" });

		if (format.equals("")) {
			formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss", sym);
		} else {
			formatter = new SimpleDateFormat(format, sym);
		}

		return formatter.format(date);
	}
	


}
