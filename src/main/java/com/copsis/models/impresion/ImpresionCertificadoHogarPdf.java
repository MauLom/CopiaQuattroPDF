package com.copsis.models.impresion;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.File;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;

import com.copsis.controllers.forms.ImpresionForm;
import com.copsis.exceptions.GeneralServiceException;
import com.copsis.models.Tabla.BaseTable;
import com.copsis.models.Tabla.ImageUtils;
import com.copsis.models.Tabla.PDocumenteHW;
import com.copsis.models.Tabla.Row;
import com.copsis.models.Tabla.Sio4CommunsPdf;
import com.copsis.models.Tabla.VerticalAlignment;



public class ImpresionCertificadoHogarPdf {
	private Sio4CommunsPdf communsPdf = new Sio4CommunsPdf();
	private Color black = new Color(0, 0, 0);
	private final Color bgColor = new Color(255, 255, 255, 0);

	private float yStartNewPage = 770, yStart = 830, bottomMargin = 26;
	private float fullWidth = 542;
	private float margin = 40;
	private float ystarpos =0;
	private boolean acumula;
	
	public byte[] buildPDF(ImpresionForm impresionForm) {
		ByteArrayOutputStream output;
		try {
			try (PDDocument document = new PDDocument()) {
				try {
					
					PDPage page = new PDPage(PDocumenteHW.B0);
					
					document.addPage(page);
					BaseTable table;
					Row<PDPage> baseRow;
					BaseTable table2;
					Row<PDPage> baseRow2;
					
			
					StringBuilder contenido = new StringBuilder();
					
					table = new BaseTable(yStart, yStartNewPage, bottomMargin, 200, 385, document, page, false,true);				 
					baseRow = communsPdf.setRow(table, 20);
					communsPdf.setCell(baseRow, 100, "Certificado de Seguros", black, true, "R", 14, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor);
					baseRow = communsPdf.setRow(table, 20);
					communsPdf.setCell(baseRow, 100, "Daños Hogar", black, true, "R", 14, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor);
				    table.draw();
					
					
					table = new BaseTable(yStart, yStartNewPage, bottomMargin, 200, 30, document, page, false,true);				 
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 60, ImageUtils.readImage("https://storage.googleapis.com/quattrocrm-copsis/s32tkk/2202/Polizas/2202/0pMkKygY9dNFfOOz4qe6G5h1E2yMYCk8K4QTvoUllnNimx5QyJntaUOddqT5Wdn/sura.png"), 1, 1, bgColor).setValign(VerticalAlignment.MIDDLE);
				    table.draw();
				    
				    yStart -= table.getHeaderAndDataHeight()+7;
				    
				    
				    contenido.append("Seguros SURA,\t  S.A. de \tC.V. (Antes \tSeguros Royal \t& \tSunAlliance \tSeguros \t(México), S.A. \tde C.V.)");
				    table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, margin, document, page, false,true);
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 100, communsPdf.eliminaHtmlTags(contenido.toString()), black, true, "L", 11, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor);
					baseRow = communsPdf.setRow(table, 15);
					table.draw();
					
					 yStart -= table.getHeaderAndDataHeight()+2;
					    
					 table = new BaseTable(yStart, yStartNewPage, bottomMargin, 355, margin, document, page,false ,true);
						baseRow = communsPdf.setRow(table, 40);
						baseRow.setLineSpacing(1.2f);
						contenido = new StringBuilder();
						contenido.append("AL \tRECIBIR  \tESTA \tCARÁTULA \tDE  \tLA \tPÓLIZA \tSE \tENTREGAN \tTAMBIÉN LAS\n"
							+ "CONDICIONES GENERALES, ESPECIALES Y PARTICULARES QUE INTEGRAN SU\n"
							+ "CONTRACTO \tDE \tSEGURO.LAS \tCONDICIONES GENERALES PUEDEN TAMBIÉN\n"
							+ "SER CONSULTADAS EN LA PAGINA <b>www.segurossura.com.mx");
					communsPdf.setCell(baseRow, 100,communsPdf.eliminaHtmlTags(contenido.toString()) , black, false, "L", 9, communsPdf.setLineStyle(black), "", communsPdf.setPadding(1f),bgColor);

					table.draw();
					
						table = new BaseTable(yStart, yStartNewPage, bottomMargin, 187, 395, document, page, true,true);
						baseRow = communsPdf.setRow(table, 21);
						communsPdf.setCell(baseRow, 50, "Oficina:", black, false, "L", 9, communsPdf.setLineStyle(Color.white,Color.white,Color.black,Color.white), "", communsPdf.setPadding(2f),bgColor);					
						communsPdf.setCell(baseRow, 50, "Ramo:", black, false, "L", 9, communsPdf.setLineStyle(Color.white,Color.white,Color.black,Color.white), "", communsPdf.setPadding(2f),bgColor);					
						baseRow = communsPdf.setRow(table, 21);
						communsPdf.setCell(baseRow, 50, "Póliza:", black, false, "L", 10, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor).setValign(VerticalAlignment.MIDDLE);;
						
						communsPdf.setCell(baseRow, 50, "Inciso:", black, false, "L", 10, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor).setValign(VerticalAlignment.MIDDLE);;
						table.draw();
						
						yStart -= table.getHeaderAndDataHeight()+7;
						
						
						table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, margin, document, page, true,true);
						baseRow = communsPdf.setRow(table, 15);
						communsPdf.setCell(baseRow, 60, "Vigencia:", black, false, "L", 10, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor).setValign(VerticalAlignment.MIDDLE);
						communsPdf.setCell(baseRow, 20, "Endoso:", black, false, "L", 10, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor).setValign(VerticalAlignment.MIDDLE);
						communsPdf.setCell(baseRow, 20, "Asegurado:", black, false, "L", 10, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor).setValign(VerticalAlignment.MIDDLE);
						table.draw();
						
						yStart -= table.getHeaderAndDataHeight()+7;
						
						table = new BaseTable(yStart, yStartNewPage, bottomMargin, 325, margin, document, page, true,true);					
					    baseRow = communsPdf.setRow(table, 15);
						communsPdf.setCell(baseRow, 100, "DATOS DEL ASEGURADO Y/O PROPIETARIO", black, true, "C", 10, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor).setValign(VerticalAlignment.MIDDLE);
					
						baseRow = communsPdf.setRow(table, 15);
						communsPdf.setCell(baseRow, 18, "Asegurado:", black, false, "L", 10, communsPdf.setLineStyle(Color.black,Color.white,Color.white,Color.black), "", communsPdf.setPadding(2f),bgColor);
						communsPdf.setCell(baseRow, 82, "", black, false, "L", 10, communsPdf.setLineStyle(Color.white,Color.black,Color.white,Color.black), "", communsPdf.setPadding(2f),bgColor);
						
						baseRow = communsPdf.setRow(table, 47);						
						communsPdf.setCell(baseRow, 18, "Domicilio:", black, false, "L", 10, communsPdf.setLineStyle(Color.black,Color.white,Color.white,Color.black), "", communsPdf.setPadding(2f),bgColor);
						communsPdf.setCell(baseRow, 82, "", black, false, "L", 10, communsPdf.setLineStyle(Color.white,Color.black,Color.white,Color.black), "", communsPdf.setPadding(2f),bgColor);
					   
						baseRow = communsPdf.setRow(table, 15);						
						communsPdf.setCell(baseRow, 18, "Telefono:", black, false, "L", 10, communsPdf.setLineStyle(Color.black,Color.white,Color.white,Color.black), "", communsPdf.setPadding(2f),bgColor);
						communsPdf.setCell(baseRow, 82, "", black, false, "L", 10, communsPdf.setLineStyle(Color.white,Color.black,Color.white,Color.black), "", communsPdf.setPadding(2f),bgColor);
					   
						baseRow = communsPdf.setRow(table, 15);						
						communsPdf.setCell(baseRow, 18, "RFC:", black, false, "L", 10, communsPdf.setLineStyle(Color.black,Color.white,Color.black,Color.black), "", communsPdf.setPadding(2f),bgColor);
						communsPdf.setCell(baseRow, 32, "", black, false, "L", 10, communsPdf.setLineStyle(Color.white,Color.black,Color.black,Color.black), "", communsPdf.setPadding(2f),bgColor);
					   
						communsPdf.setCell(baseRow, 18, "Contracto:", black, false, "L", 10, communsPdf.setLineStyle(Color.white,Color.white,Color.black,Color.black), "", communsPdf.setPadding(2f),bgColor);
						communsPdf.setCell(baseRow, 32, "", black, false, "L", 10, communsPdf.setLineStyle(Color.white,Color.black,Color.black,Color.black), "", communsPdf.setPadding(2f),bgColor);
						
						table.draw();
					
						
						table = new BaseTable(yStart, yStartNewPage, bottomMargin, 217, 365, document, page, true,true);
						baseRow = communsPdf.setRow(table, 15);
						communsPdf.setCell(baseRow, 100, "DATOS GENERALES DEL PÓLIZA", black, true, "L", 10, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor).setValign(VerticalAlignment.MIDDLE);						
						baseRow = communsPdf.setRow(table, 15);
						communsPdf.setCell(baseRow, 30, "Asegurado:", black, false, "L", 10, communsPdf.setLineStyle(Color.black,Color.white,Color.white,Color.black), "", communsPdf.setPadding(2f),bgColor);
						communsPdf.setCell(baseRow, 70, "", black, false, "L", 10, communsPdf.setLineStyle(Color.white,Color.black,Color.white,Color.black), "", communsPdf.setPadding(2f),bgColor);
						baseRow = communsPdf.setRow(table, 15);
						communsPdf.setCell(baseRow, 45, "Fecha de Emisión:", black, false, "L", 10, communsPdf.setLineStyle(Color.black,Color.white,Color.white,Color.black), "", communsPdf.setPadding(2f),bgColor);
						communsPdf.setCell(baseRow, 55, "", black, false, "L", 10, communsPdf.setLineStyle(Color.white,Color.black,Color.white,Color.black), "", communsPdf.setPadding(2f),bgColor);
						baseRow = communsPdf.setRow(table, 15);
						communsPdf.setCell(baseRow, 40, "Fecha de pago:", black, false, "L", 10, communsPdf.setLineStyle(Color.black,Color.white,Color.white,Color.black), "", communsPdf.setPadding(2f),bgColor);
						communsPdf.setCell(baseRow, 60, "", black, false, "L", 10, communsPdf.setLineStyle(Color.white,Color.black,Color.white,Color.black), "", communsPdf.setPadding(2f),bgColor);
						baseRow = communsPdf.setRow(table, 15);
						communsPdf.setCell(baseRow, 30, "Descuento:", black, false, "L", 10, communsPdf.setLineStyle(Color.black,Color.white,Color.white,Color.black), "", communsPdf.setPadding(2f),bgColor);
						communsPdf.setCell(baseRow, 70, "", black, false, "L", 10, communsPdf.setLineStyle(Color.white,Color.black,Color.white,Color.black), "", communsPdf.setPadding(2f),bgColor);
						baseRow = communsPdf.setRow(table, 15);
						communsPdf.setCell(baseRow, 25, "Moneda:", black, false, "L", 10, communsPdf.setLineStyle(Color.black,Color.white,Color.white,Color.black), "", communsPdf.setPadding(2f),bgColor);
						communsPdf.setCell(baseRow, 75, "", black, false, "L", 10, communsPdf.setLineStyle(Color.white,Color.black,Color.white,Color.black), "", communsPdf.setPadding(2f),bgColor);
						baseRow = communsPdf.setRow(table, 15);
						communsPdf.setCell(baseRow, 20, "Giro:", black, false, "L", 10, communsPdf.setLineStyle(Color.black,Color.white,Color.black,Color.black), "", communsPdf.setPadding(2f),bgColor);
						communsPdf.setCell(baseRow, 80, "", black, false, "L", 10, communsPdf.setLineStyle(Color.white,Color.black,Color.black,Color.black), "", communsPdf.setPadding(2f),bgColor);
			
						table.draw();
						
	                    yStart -= table.getHeaderAndDataHeight()+6;	
	                    table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, margin, document, page, true,true);					
					    baseRow = communsPdf.setRow(table, 15);
						communsPdf.setCell(baseRow, 100, "DESGLOSE  DE COBERTURAS", black, true, "C", 10, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor).setValign(VerticalAlignment.MIDDLE);
						table.draw();
						yStart -= table.getHeaderAndDataHeight();
	                    contenido = new StringBuilder();
	                    contenido.append("Seguro SURA, S.A de C.V (Antes Seguros Royal & SunAlliance Seguros(México), S.A de C.V.) (Que en lo sucesivo se denominará la \"Compañía\"), asegurada a favor de la persona de arriba citada (que  en lo sucesivo se"
	                    		+ "denominará el \"Asegurado\"), los bienes y riesgos más adelante  detallados, de acuerdo a las condiciones generales  y particulares de la póliza durante la vigencia establecida");
	                    table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, margin, document, page, true,true);					
					    baseRow = communsPdf.setRow(table, 20);
						communsPdf.setCell(baseRow, 100, communsPdf.eliminaHtmlTags(contenido.toString()), black, false, "L", 6, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor).setValign(VerticalAlignment.MIDDLE);
						table.draw();                  
						 yStart -= table.getHeaderAndDataHeight();
						 
							
						
						table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, margin, document, page, true,true);					
					    baseRow = communsPdf.setRow(table, 15);
						communsPdf.setCell(baseRow, 60, "Secciones Amparadas", black, true, "C", 10, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor).setValign(VerticalAlignment.MIDDLE);
						communsPdf.setCell(baseRow, 20, "Suma Asegurada", black, true, "C", 10, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor).setValign(VerticalAlignment.MIDDLE);
						communsPdf.setCell(baseRow, 20, "Prima", black, true, "C", 10, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor).setValign(VerticalAlignment.MIDDLE);
						table.draw();
						
									yStart -= table.getHeaderAndDataHeight();	
						int x=0;
						int c=0;

						while(x < impresionForm.getCoberturasLista().size()) {
							acumula  =true;
							c++;
							
							

							table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, margin, document, page, true,true);					
						    baseRow = communsPdf.setRow(table, 18);
						    if(c == impresionForm.getCoberturasLista().size()) {
						    	communsPdf.setCell(baseRow, 60,impresionForm.getCoberturasLista().get(x).getNombres() , black, false, "L", 9, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.white), "", communsPdf.setPadding(2f),bgColor).setValign(VerticalAlignment.MIDDLE); 	
						    	communsPdf.setCell(baseRow, 20, impresionForm.getCoberturasLista().get(x).getSa(), black, false, "L", 9, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.white), "", communsPdf.setPadding(2f),bgColor).setValign(VerticalAlignment.MIDDLE);
								communsPdf.setCell(baseRow, 20, impresionForm.getCoberturasLista().get(x).getPrima(), black, false, "L", 9, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.white), "", communsPdf.setPadding(2f),bgColor).setValign(VerticalAlignment.MIDDLE);
					
						    }else {
						    	communsPdf.setCell(baseRow, 60,impresionForm.getCoberturasLista().get(x).getNombres() , black, false, "L", 9, communsPdf.setLineStyle(Color.black,Color.black,Color.white,Color.white), "", communsPdf.setPadding(2f),bgColor).setValign(VerticalAlignment.MIDDLE);
						    	communsPdf.setCell(baseRow, 20, impresionForm.getCoberturasLista().get(x).getSa(), black, false, "L", 9, communsPdf.setLineStyle(Color.black,Color.black,Color.white,Color.white), "", communsPdf.setPadding(2f),bgColor).setValign(VerticalAlignment.MIDDLE);
								communsPdf.setCell(baseRow, 20, impresionForm.getCoberturasLista().get(x).getPrima(), black, false, "L", 9, communsPdf.setLineStyle(Color.black,Color.black,Color.white,Color.white), "", communsPdf.setPadding(2f),bgColor).setValign(VerticalAlignment.MIDDLE);
					
						    }
							
						
							if(isEndOfPage(table)) {

								table.getRows().remove(table.getRows().size()-1);
								table.draw();
								page = new PDPage();
								document.addPage(page);
								acumula = false;
								
							}else {
								table.draw();
								yStart -= table.getHeaderAndDataHeight();
								

							}
							if(acumula) {								
								x++;
							} 
							
						}
						
						
				
						
			
			
						table2 = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, margin, document, page, true,true);
					    baseRow2 = communsPdf.setRow(table2, 120);
					    communsPdf.setCell(baseRow2, 100, "", black, false, "L", 9, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor).setValign(VerticalAlignment.MIDDLE);
					    table2.draw();
						
						
						
						table = new BaseTable((yStart-10), yStartNewPage, bottomMargin, 314, margin+10, document, page, true,true);					
					    baseRow = communsPdf.setRow(table, 15);
					    communsPdf.setCell(baseRow, 32, "Beneficiario Preferente:", black, false, "L", 9, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor).setValign(VerticalAlignment.MIDDLE);
						communsPdf.setCell(baseRow, 67, "Autofinanciamiento de Automóviles Monterrey, S.A. de C.V.", black, false, "L", 8, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor).setValign(VerticalAlignment.MIDDLE);
					    
						 baseRow = communsPdf.setRow(table, 5);
						 communsPdf.setCell(baseRow, 100, "", black, false, "L", 9, communsPdf.setLineStyle(Color.white,Color.white,Color.white,Color.white), "", communsPdf.setPadding(2f),bgColor).setValign(VerticalAlignment.MIDDLE);

						 baseRow = communsPdf.setRow(table, 30);
						 communsPdf.setCell(baseRow, 31, "Ubicacion:", black, false, "L", 9, communsPdf.setLineStyle(Color.white,Color.white,Color.white,Color.white), "", communsPdf.setPadding(2f),bgColor).setValign(VerticalAlignment.MIDDLE);
						 communsPdf.setCell(baseRow, 69, "Autofinanciamiento de Automóviles Monterrey, S.A. de C.V.", black, false, "L", 9, communsPdf.setLineStyle(Color.white,Color.white,Color.white,Color.white), "", communsPdf.setPadding(2f),bgColor);
						 
						
						table.draw();
					    
					    table = new BaseTable(yStart, yStartNewPage, bottomMargin, 217, 365, document, page, false,true);					
					    baseRow = communsPdf.setRow(table, 12);
					    communsPdf.setCell(baseRow, 50, "Prima Neta", black, false, "L", 9, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor).setValign(VerticalAlignment.MIDDLE);
						communsPdf.setCell(baseRow, 50, "", black, false, "L", 8, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor).setValign(VerticalAlignment.MIDDLE);
						
						baseRow = communsPdf.setRow(table, 12);
						communsPdf.setCell(baseRow, 50, "Otros Descuentos", black, false, "L", 9, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor).setValign(VerticalAlignment.MIDDLE);
					    communsPdf.setCell(baseRow, 50, "", black, false, "L", 8, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor).setValign(VerticalAlignment.MIDDLE);
						
					    baseRow = communsPdf.setRow(table, 15);
						communsPdf.setCell(baseRow, 50, "Finacimiento por Pago Fracionado", black, false, "L", 9, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor).setValign(VerticalAlignment.MIDDLE);
					    communsPdf.setCell(baseRow, 50, "", black, false, "L", 8, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor).setValign(VerticalAlignment.MIDDLE);
					    
					    baseRow = communsPdf.setRow(table, 12);
						communsPdf.setCell(baseRow, 50, "Gastos de Expedición", black, false, "L", 9, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor).setValign(VerticalAlignment.MIDDLE);
					    communsPdf.setCell(baseRow, 50, "", black, false, "L", 8, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor).setValign(VerticalAlignment.MIDDLE);
					    
					    baseRow = communsPdf.setRow(table, 12);
						communsPdf.setCell(baseRow, 50, "IVA", black, false, "L", 9, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor).setValign(VerticalAlignment.MIDDLE);
					    communsPdf.setCell(baseRow, 50, "", black, false, "L", 8, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor).setValign(VerticalAlignment.MIDDLE);
					    
					    baseRow = communsPdf.setRow(table, 12);
						communsPdf.setCell(baseRow, 50, "Prima Total", black, true, "L", 9, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor).setValign(VerticalAlignment.MIDDLE);
					    communsPdf.setCell(baseRow, 50, "", black, true, "L", 8, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor).setValign(VerticalAlignment.MIDDLE);
						table.draw();
						
						
						yStart -= table.getHeaderAndDataHeight()+5;
						
						contenido = new StringBuilder();
						contenido.append("En cumplimiento a lo dispuesto en el artiuculo 202 de la Ley de Instituciones de  Seguros y de Fianzas,la documentacion contractual ");
						contenido.append("y a la nota  tecnica que integran este producto de seguro,quedaron registradas  ante la Comision Nacional de Seguros y Fianzas, a ");
						contenido.append("partir del dia 29 de noviembre de 2005,con el numero CNSF-S0010-0535-2005");
						table = new BaseTable(yStart, yStartNewPage, bottomMargin,fullWidth, margin, document, page, false,true);					
					    baseRow = communsPdf.setRow(table, 15);
					    baseRow.setLineSpacing(1.2f);
					    communsPdf.setCell(baseRow, 100, communsPdf.eliminaHtmlTags(contenido.toString()), black, false, "L", 9, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor).setValign(VerticalAlignment.MIDDLE);
					    table.draw();
					    
					    yStart -= table.getHeaderAndDataHeight()+14;
				    
					     
					    
					
					
					    
					    
                        					
						contenido = new StringBuilder();
						contenido.append("Para cualquier \taclaración o \tduda no resueltas \ten la relación \tcon su seguro, \tcontacte a la  \tUnidad \tEspecializada de \tnuestra \tCompañía \ta \tlos \tteléfonos 57-23-79-99 en el ");
						contenido.append("Distrito federal y la lada sin costo al 01-800-72-37-900 o al correo una.clientes@segurossura.com.mx ,visite www.segurossura.com.mx; o  bien comunicarse a ");
						contenido.append("Condusef al teléfono (55) 5448 7000 en el D.F. y del interior de la República al 01 800 999 8080 o  visite la página www.condusef.gob.mx ");
						table = new BaseTable(yStart, yStartNewPage, bottomMargin,fullWidth, margin, document, page, false,true);
						baseRow = communsPdf.setRow(table, 15);						
						communsPdf.setCell(baseRow, 100, "Documentos con Validez Oficial", black, true, "C", 14, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor).setValign(VerticalAlignment.MIDDLE);
					    baseRow = communsPdf.setRow(table, 15);
					    baseRow.setLineSpacing(1.2f);
					    communsPdf.setCell(baseRow, 100, communsPdf.eliminaHtmlTags(contenido.toString()), black, false, "L", 7, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor).setValign(VerticalAlignment.MIDDLE);
					   
						contenido = new StringBuilder();
					    contenido.append("Seguros SURA,  S.A. de tC.V. (Antes tSeguros Royal & SunAlliance Seguros (México), S.A. de C.V.)");
						baseRow = communsPdf.setRow(table, 10);
						communsPdf.setCell(baseRow, 100, communsPdf.eliminaHtmlTags(contenido.toString()), black, true, "C", 11, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor);
			
						baseRow = communsPdf.setRow(table, 10);
						communsPdf.setCell(baseRow, 100, communsPdf.eliminaHtmlTags("Blvd. Adolfo López Mateos No.2448,Col. Altravista Delegación Alvaro Obegrón,C.P. 01060 México, D.F. Tels. 57-23-79-99, 01800-723-79-00"), black, false, "C", 8, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor);
			
					    table.draw();

					    
					    
					    
					 
	
					output = new ByteArrayOutputStream();				
					document.save(output);
					document.save(new File("/home/desarrollo8/Pictures/certificado.pdf"));
					return output.toByteArray();
				} finally {
				
					document.close();
				}
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new GeneralServiceException("00001",
					"Ocurrio un error en el servicio ImpresionInter: " + ex.getMessage());
		}
	}
	
	private boolean isEndOfPage(BaseTable table) {
        float currentY = yStart - table.getHeaderAndDataHeight();
        return currentY <= bottomMargin;
    }
}
