package com.copsis.models.impresion;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.text.DateFormatSymbols;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;

import com.copsis.dto.SURAImpresionEmsionDTO;
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

	private boolean acumula;
	
	public byte[] buildPDF(SURAImpresionEmsionDTO suraImpresionEmsionDTO) {
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
					String direccion = "";
					String ubicacion = "";
					
					table = new BaseTable(yStart, yStartNewPage, bottomMargin, 200, 385, document, page, false,true);				 
					baseRow = communsPdf.setRow(table, 20);
					communsPdf.setCell(baseRow, 100, "Certificado de Seguros", black, true, "R", 14, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor);
					baseRow = communsPdf.setRow(table, 20);
					communsPdf.setCell(baseRow, 100, "Daños Hogar", black, true, "R", 14, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor);
				    table.draw();
					
					
					table = new BaseTable(yStart, yStartNewPage, bottomMargin, 200, 30, document, page, false,true);				 
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 60, ImageUtils.readImage("https://storage.googleapis.com/quattrocrm-copsis/recursos-pdf/sura.png"), 1, 1, bgColor).setValign(VerticalAlignment.MIDDLE);
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
							+ "CONTRATO \tDE \tSEGURO.LAS \tCONDICIONES GENERALES PUEDEN TAMBIÉN\n"
							+ "SER CONSULTADAS EN LA PAGINA <b>www.segurossura.com.mx");
					communsPdf.setCell(baseRow, 100,communsPdf.eliminaHtmlTags(contenido.toString()) , black, false, "L", 9, communsPdf.setLineStyle(black), "", communsPdf.setPadding(1f),bgColor);

					table.draw();
					
						table = new BaseTable(yStart, yStartNewPage, bottomMargin, 187, 395, document, page, true,true);
						baseRow = communsPdf.setRow(table, 21);
						communsPdf.setCell(baseRow, 50, "Oficina:"  + (suraImpresionEmsionDTO.getOficina() != null ?  suraImpresionEmsionDTO.getOficina() :""), black, false, "L", 9, communsPdf.setLineStyle(Color.white,Color.white,Color.black,Color.white), "", communsPdf.setPadding(2f),bgColor);					
						communsPdf.setCell(baseRow, 50, "Ramo:"  + (suraImpresionEmsionDTO.getRamo() != null ?  suraImpresionEmsionDTO.getRamo() :""), black, false, "L", 9, communsPdf.setLineStyle(Color.white,Color.white,Color.black,Color.white), "", communsPdf.setPadding(2f),bgColor);					
						baseRow = communsPdf.setRow(table, 21);
						communsPdf.setCell(baseRow, 50, "Póliza:" + (suraImpresionEmsionDTO.getNoPoliza() != null ?  suraImpresionEmsionDTO.getNoPoliza() :"") , black, false, "L", 10, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor).setValign(VerticalAlignment.MIDDLE);;						
						communsPdf.setCell(baseRow, 50, "Inciso:"  + (suraImpresionEmsionDTO.getInciso() != null ?  suraImpresionEmsionDTO.getInciso() :""), black, false, "L", 10, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor).setValign(VerticalAlignment.MIDDLE);;
						table.draw();
						
						yStart -= table.getHeaderAndDataHeight()+7;
						
						
						table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, margin, document, page, true,true);
						baseRow = communsPdf.setRow(table, 15);
						communsPdf.setCell(baseRow, 60, "Vigencia: "+ "Del " + (suraImpresionEmsionDTO.getVigenciaDe() != null ? formarDate( suraImpresionEmsionDTO.getVigenciaDe(),"") :"") +" 12:00 Hrs. Al "+(suraImpresionEmsionDTO.getVigenciaA() != null ?  formarDate(suraImpresionEmsionDTO.getVigenciaA(),"") :"") +" 12:00 Hrs."  , black, false, "L", 10, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor).setValign(VerticalAlignment.MIDDLE);
						communsPdf.setCell(baseRow, 20, "Endoso: " , black, false, "L", 10, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor).setValign(VerticalAlignment.MIDDLE);
						communsPdf.setCell(baseRow, 20, "Asegurado:", black, false, "L", 10, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor).setValign(VerticalAlignment.MIDDLE);
						table.draw();
						
						yStart -= table.getHeaderAndDataHeight()+7;
						
						table = new BaseTable(yStart, yStartNewPage, bottomMargin, 325, margin, document, page, true,true);					
					    baseRow = communsPdf.setRow(table, 15);
						communsPdf.setCell(baseRow, 100, "DATOS DEL ASEGURADO Y/O PROPIETARIO", black, true, "C", 10, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor).setValign(VerticalAlignment.MIDDLE);
					
						baseRow = communsPdf.setRow(table, 15);
						communsPdf.setCell(baseRow, 18, "Asegurado:", black, false, "L", 10, communsPdf.setLineStyle(Color.black,Color.white,Color.white,Color.black), "", communsPdf.setPadding(2f),bgColor);
						communsPdf.setCell(baseRow, 82,(suraImpresionEmsionDTO.getContratanteNombre() != null ?  suraImpresionEmsionDTO.getContratanteNombre() :"") +" "+
								(suraImpresionEmsionDTO.getContratanteApPaterno() != null ?  suraImpresionEmsionDTO.getContratanteApPaterno() :"") +" "+
								(suraImpresionEmsionDTO.getContratanteApMaterno() != null ?  suraImpresionEmsionDTO.getContratanteApMaterno() :"")
								, black, true, "L", 10, communsPdf.setLineStyle(Color.white,Color.black,Color.white,Color.black), "", communsPdf.setPadding(2f),bgColor);
						
						direccion =(suraImpresionEmsionDTO.getContratanteCalleNo() != null ?  suraImpresionEmsionDTO.getContratanteCalleNo() :"") +"\n";
						direccion +=(suraImpresionEmsionDTO.getContratanteColonia() != null ?  suraImpresionEmsionDTO.getContratanteColonia() :"" ) +"\n";
						direccion +=(suraImpresionEmsionDTO.getContratanteMunicipio() != null ?  suraImpresionEmsionDTO.getContratanteMunicipio() :"");
						direccion +=(suraImpresionEmsionDTO.getContratanteCP() != null ?   " C.P."+suraImpresionEmsionDTO.getContratanteCP() :"");
						
						baseRow = communsPdf.setRow(table, 47);						
						communsPdf.setCell(baseRow, 18, "Domicilio:", black, false, "L", 10, communsPdf.setLineStyle(Color.black,Color.white,Color.white,Color.black), "", communsPdf.setPadding(2f),bgColor);
						communsPdf.setCell(baseRow, 82,Sio4CommunsPdf.eliminaHtmlTags3( direccion.toString()), black, false, "L", 10, communsPdf.setLineStyle(Color.white,Color.black,Color.white,Color.black), "", communsPdf.setPadding(2f),bgColor);
					   
						baseRow = communsPdf.setRow(table, 15);						
						communsPdf.setCell(baseRow, 18, "Teléfono:", black, false, "L", 10, communsPdf.setLineStyle(Color.black,Color.white,Color.white,Color.black), "", communsPdf.setPadding(2f),bgColor);
						communsPdf.setCell(baseRow, 82, "", black, false, "L", 10, communsPdf.setLineStyle(Color.white,Color.black,Color.white,Color.black), "", communsPdf.setPadding(2f),bgColor);
					   
						baseRow = communsPdf.setRow(table, 15);						
						communsPdf.setCell(baseRow, 18, "RFC:", black, false, "L", 10, communsPdf.setLineStyle(Color.black,Color.white,Color.black,Color.black), "", communsPdf.setPadding(2f),bgColor);
						communsPdf.setCell(baseRow, 32,(suraImpresionEmsionDTO.getContratanteColonia() != null ?  suraImpresionEmsionDTO.getContratanteRFC() :""), black, false, "L", 10, communsPdf.setLineStyle(Color.white,Color.white,Color.black,Color.black), "", communsPdf.setPadding(2f),bgColor);
					   
						communsPdf.setCell(baseRow, 18, "Contrato:", black, false, "L", 10, communsPdf.setLineStyle(Color.white,Color.white,Color.black,Color.black), "", communsPdf.setPadding(2f),bgColor);
						communsPdf.setCell(baseRow, 32, (suraImpresionEmsionDTO.getContratoNo() != null ?  suraImpresionEmsionDTO.getContratoNo() :""), black, false, "L", 10, communsPdf.setLineStyle(Color.white,Color.black,Color.black,Color.black), "", communsPdf.setPadding(2f),bgColor);
						
						table.draw();
						SimpleDateFormat formatter= new SimpleDateFormat("d/MM/yyyy");
						Date fecha = new Date();					
						
						table = new BaseTable(yStart, yStartNewPage, bottomMargin, 217, 365, document, page, true,true);
						baseRow = communsPdf.setRow(table, 15);
						communsPdf.setCell(baseRow, 100, "DATOS GENERALES DE LA PÓLIZA", black, true, "L", 10, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor).setValign(VerticalAlignment.MIDDLE);						
						baseRow = communsPdf.setRow(table, 15);
						communsPdf.setCell(baseRow, 20, "Agente:", black, false, "L", 10, communsPdf.setLineStyle(Color.black,Color.white,Color.white,Color.black), "", communsPdf.setPadding(2f),bgColor);
						communsPdf.setCell(baseRow, 80, (suraImpresionEmsionDTO.getClaveAgente() != null ?  suraImpresionEmsionDTO.getClaveAgente() :""), black, false, "L", 10, communsPdf.setLineStyle(Color.white,Color.black,Color.white,Color.black), "", communsPdf.setPadding(2f),bgColor);
						baseRow = communsPdf.setRow(table, 15);
						communsPdf.setCell(baseRow, 45, "Fecha de Emisión:", black, false, "L", 10, communsPdf.setLineStyle(Color.black,Color.white,Color.white,Color.black), "", communsPdf.setPadding(2f),bgColor);
						communsPdf.setCell(baseRow, 55, formatter.format(fecha), black, false, "L", 10, communsPdf.setLineStyle(Color.white,Color.black,Color.white,Color.black), "", communsPdf.setPadding(2f),bgColor);
						baseRow = communsPdf.setRow(table, 15);
						communsPdf.setCell(baseRow, 40, "Fecha de pago:", black, false, "L", 10, communsPdf.setLineStyle(Color.black,Color.white,Color.white,Color.black), "", communsPdf.setPadding(2f),bgColor);
						communsPdf.setCell(baseRow, 60, (suraImpresionEmsionDTO.getFormaPagoEnum() != null ?  suraImpresionEmsionDTO.getFormaPagoEnum() :"") , black, false, "L", 10, communsPdf.setLineStyle(Color.white,Color.black,Color.white,Color.black), "", communsPdf.setPadding(2f),bgColor);
						baseRow = communsPdf.setRow(table, 15);
						communsPdf.setCell(baseRow, 30, "Descuento:", black, false, "L", 10, communsPdf.setLineStyle(Color.black,Color.white,Color.white,Color.black), "", communsPdf.setPadding(2f),bgColor);
						communsPdf.setCell(baseRow, 70, "", black, false, "L", 10, communsPdf.setLineStyle(Color.white,Color.black,Color.white,Color.black), "", communsPdf.setPadding(2f),bgColor);
						baseRow = communsPdf.setRow(table, 15);
						communsPdf.setCell(baseRow, 25, "Moneda:", black, false, "L", 10, communsPdf.setLineStyle(Color.black,Color.white,Color.white,Color.black), "", communsPdf.setPadding(2f),bgColor);
						communsPdf.setCell(baseRow, 75,(suraImpresionEmsionDTO.getMonedaEnum() != null ?  suraImpresionEmsionDTO.getMonedaEnum() :""), black, false, "L", 10, communsPdf.setLineStyle(Color.white,Color.black,Color.white,Color.black), "", communsPdf.setPadding(2f),bgColor);
						baseRow = communsPdf.setRow(table, 15);
						communsPdf.setCell(baseRow, 20, "Giro:", black, false, "L", 10, communsPdf.setLineStyle(Color.black,Color.white,Color.black,Color.black), "", communsPdf.setPadding(2f),bgColor);
						communsPdf.setCell(baseRow, 80,(suraImpresionEmsionDTO.getGiro() != null ?  suraImpresionEmsionDTO.getGiro() :""), black, false, "L", 10, communsPdf.setLineStyle(Color.white,Color.black,Color.black,Color.black), "", communsPdf.setPadding(2f),bgColor);			
						table.draw();
						
	                    yStart -= table.getHeaderAndDataHeight()+6;	
	                    table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, margin, document, page, true,true);					
					    baseRow = communsPdf.setRow(table, 15);
						communsPdf.setCell(baseRow, 100, "DESGLOSE  DE COBERTURAS", black, true, "C", 10, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor).setValign(VerticalAlignment.MIDDLE);
						table.draw();
						yStart -= table.getHeaderAndDataHeight();
	                    contenido = new StringBuilder();
	                    contenido.append("Seguro SURA, S.A de C.V (Antes Seguros Royal & SunAlliance Seguros(México), S.A de C.V.) (Que en lo sucesivo se denominará la \"Compañía\"), asegurada a favor de la persona de arriba citada (que  en lo sucesivo se"
	                    		+ " denominará el \"Asegurado\"), los bienes y riesgos más adelante  detallados, de acuerdo a las condiciones generales  y particulares de la póliza durante la vigencia establecida");
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
						
						DecimalFormat formateador = new DecimalFormat("#,##0.00");

						while(x < suraImpresionEmsionDTO.getCoberturas().size()) {
							acumula  =true;
							c++;
							
							

							table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, margin, document, page, true,true);					
						    baseRow = communsPdf.setRow(table, 15);
						    if(c == suraImpresionEmsionDTO.getCoberturas().size()) {
						    	communsPdf.setCell(baseRow, 60," "+suraImpresionEmsionDTO.getCoberturas().get(x).getNombre().toUpperCase() , black, false, "L", 9, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.white), "", communsPdf.setPadding(4f),bgColor).setValign(VerticalAlignment.MIDDLE); 	
						    	communsPdf.setCell(baseRow, 20,"  $"+formateador.format( suraImpresionEmsionDTO.getCoberturas().get(x).getSa()) , black, false, "L", 9, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.white), "", communsPdf.setPadding(4f),bgColor).setValign(VerticalAlignment.MIDDLE);
								communsPdf.setCell(baseRow, 20,"", black, false, "L", 9, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.white), "", communsPdf.setPadding(2f),bgColor).setValign(VerticalAlignment.MIDDLE);
					
						    }else {
						    	communsPdf.setCell(baseRow, 60," "+suraImpresionEmsionDTO.getCoberturas().get(x).getNombre().toUpperCase() , black, false, "L", 9, communsPdf.setLineStyle(Color.black,Color.black,Color.white,Color.white), "", communsPdf.setPadding(2f),bgColor).setValign(VerticalAlignment.MIDDLE);
						    	communsPdf.setCell(baseRow, 20,"  $"+ formateador.format( suraImpresionEmsionDTO.getCoberturas().get(x).getSa()), black, false, "L", 9, communsPdf.setLineStyle(Color.black,Color.black,Color.white,Color.white), "", communsPdf.setPadding(2f),bgColor).setValign(VerticalAlignment.MIDDLE);
								communsPdf.setCell(baseRow, 20, "", black, false, "L", 9, communsPdf.setLineStyle(Color.black,Color.black,Color.white,Color.white), "", communsPdf.setPadding(2f),bgColor).setValign(VerticalAlignment.MIDDLE);
					
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
						communsPdf.setCell(baseRow, 67,(suraImpresionEmsionDTO.getBeneficiario() != null ?  suraImpresionEmsionDTO.getBeneficiario() :""), black, false, "L", 8, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor).setValign(VerticalAlignment.MIDDLE);
					    
						 baseRow = communsPdf.setRow(table, 5);
						 communsPdf.setCell(baseRow, 100, "", black, false, "L", 9, communsPdf.setLineStyle(Color.white,Color.white,Color.white,Color.white), "", communsPdf.setPadding(2f),bgColor).setValign(VerticalAlignment.MIDDLE);
					
						 
						 ubicacion =(suraImpresionEmsionDTO.getUbicacionCalleNo() != null ?  suraImpresionEmsionDTO.getUbicacionCalleNo() :"") +"\n";
							ubicacion +=(suraImpresionEmsionDTO.getUbicacionColonia() != null ?  suraImpresionEmsionDTO.getUbicacionColonia() :"" ) +"\n";
							ubicacion +=(suraImpresionEmsionDTO.getUbicacionMunicipio() != null ?  suraImpresionEmsionDTO.getUbicacionMunicipio() :"");
							ubicacion +=(suraImpresionEmsionDTO.getUbicacionCP() != null ?   " C.P."+suraImpresionEmsionDTO.getUbicacionCP() :"");
							
						 

					
						 
						 baseRow = communsPdf.setRow(table, 30);
						 communsPdf.setCell(baseRow, 31, "Ubicación:", black, false, "L", 9, communsPdf.setLineStyle(Color.white,Color.white,Color.white,Color.white), "", communsPdf.setPadding(2f),bgColor).setValign(VerticalAlignment.MIDDLE);
						 communsPdf.setCell(baseRow, 69,Sio4CommunsPdf.eliminaHtmlTags3( ubicacion), black, false, "L", 9, communsPdf.setLineStyle(Color.white,Color.white,Color.white,Color.white), "", communsPdf.setPadding(2f),bgColor);
						 
						
						table.draw();
					    
					    table = new BaseTable(yStart, yStartNewPage, bottomMargin, 217, 365, document, page, false,true);					
					   
					    baseRow = communsPdf.setRow(table, 7);
					    communsPdf.setCell(baseRow, 100, "", black, false, "L", 9, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor).setValign(VerticalAlignment.MIDDLE);
					
					    baseRow = communsPdf.setRow(table, 12);
					    communsPdf.setCell(baseRow, 50, "Prima Neta", black, false, "L", 10, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor).setValign(VerticalAlignment.MIDDLE);
						communsPdf.setCell(baseRow, 50, formateador.format(suraImpresionEmsionDTO.getPrimaNeta()), black, false, "L", 10, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor).setValign(VerticalAlignment.MIDDLE);
						
						baseRow = communsPdf.setRow(table, 12);
						communsPdf.setCell(baseRow, 50, "Otros Descuentos", black, false, "L", 10, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor).setValign(VerticalAlignment.MIDDLE);
					    communsPdf.setCell(baseRow, 50,  formateador.format(0.00), black, false, "L", 10, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor).setValign(VerticalAlignment.MIDDLE);
						
					    baseRow = communsPdf.setRow(table, 15);
						communsPdf.setCell(baseRow, 50, "Finacimiento por Pago Fracionado", black, false, "L", 10, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor).setValign(VerticalAlignment.MIDDLE);
					    communsPdf.setCell(baseRow, 50,  formateador.format(suraImpresionEmsionDTO.getRecargo()), black, false, "L", 10, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor).setValign(VerticalAlignment.MIDDLE);
					    
					    baseRow = communsPdf.setRow(table, 12);
						communsPdf.setCell(baseRow, 50, "Gastos de Expedición", black, false, "L", 10, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor).setValign(VerticalAlignment.MIDDLE);
					    communsPdf.setCell(baseRow, 50,  formateador.format(suraImpresionEmsionDTO.getDerecho()), black, false, "L", 10, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor).setValign(VerticalAlignment.MIDDLE);
					    
					    baseRow = communsPdf.setRow(table, 12);
						communsPdf.setCell(baseRow, 50, "IVA", black, false, "L", 10, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor).setValign(VerticalAlignment.MIDDLE);
					    communsPdf.setCell(baseRow, 50,  formateador.format(suraImpresionEmsionDTO.getIva()), black, false, "L", 10, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor).setValign(VerticalAlignment.MIDDLE);
					    
					    baseRow = communsPdf.setRow(table, 12);
						communsPdf.setCell(baseRow, 50, "Prima Total", black, true, "L", 10, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor).setValign(VerticalAlignment.MIDDLE);
					    communsPdf.setCell(baseRow, 50,  formateador.format(suraImpresionEmsionDTO.getPrimaTotal()), black, true, "L", 9, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor).setValign(VerticalAlignment.MIDDLE);
						table.draw();
						
						
						yStart -= table.getHeaderAndDataHeight()+5;
						
						contenido = new StringBuilder();
						contenido.append("En cumplimiento a lo dispuesto en el artiuculo 202 de la Ley de Instituciones de  Seguros y de Fianzas,la documentación contractual ");
						contenido.append("y a la nota  tecnica que integran este producto de seguro,quedaron registradas  ante la Comision Nacional de Seguros y Fianzas, a ");
						contenido.append("partir del dia 29 de noviembre de 2005,con el numero CNSF-S0010-0535-2005");
						table = new BaseTable(yStart, yStartNewPage, bottomMargin,fullWidth, margin, document, page, false,true);					
					    baseRow = communsPdf.setRow(table, 15);
					    baseRow.setLineSpacing(1.2f);
					    communsPdf.setCell(baseRow, 100, communsPdf.eliminaHtmlTags(contenido.toString()), black, false, "L", 9, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor).setValign(VerticalAlignment.MIDDLE);
					    table.draw();
					    
					    yStart -= table.getHeaderAndDataHeight()+16;
				    
					     
					    
					
					
					    
					    
                        					
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
					return output.toByteArray();
				} finally {
				
					document.close();
				}
			}
			
		} catch (Exception ex) {
			throw new GeneralServiceException("00001",
					"Ocurrio un error en el servicio ImpresionInter: " + ex.getMessage());
		}
	}
	
	private boolean isEndOfPage(BaseTable table) {
        float currentY = yStart - table.getHeaderAndDataHeight();
        return currentY <= bottomMargin;
    }
	
	private static String formarDate(String dateD, String format) {		
		SimpleDateFormat formatter = null;
		Date date = null;
		try {
			formatter = new SimpleDateFormat("yyyy-MM-dd");
			date = formatter.parse(dateD);
		} catch (ParseException e) {
			throw new GeneralServiceException("00001", "Fallo en el fomateo de datos.");
		}

		DateFormatSymbols sym = DateFormatSymbols.getInstance(new Locale("es", "MX"));

		if (format.equals("")) {
			formatter = new SimpleDateFormat("dd/MM/yyyy", sym);
		} else {
			formatter = new SimpleDateFormat(format, sym);
		}

		return formatter.format(date);
	}
}
