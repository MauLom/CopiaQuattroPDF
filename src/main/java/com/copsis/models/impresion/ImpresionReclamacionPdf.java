package com.copsis.models.impresion;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.URL;
import java.text.DecimalFormat;

import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;

import com.copsis.clients.projections.ImpresionReclamacionProjection;
import com.copsis.exceptions.GeneralServiceException;
import com.copsis.models.Tabla.BaseTable;
import com.copsis.models.Tabla.ImageUtils;
import com.copsis.models.Tabla.Row;
import com.copsis.models.Tabla.Sio4CommunsPdf;
import com.copsis.models.Tabla.VerticalAlignment;

public class ImpresionReclamacionPdf {
	private Sio4CommunsPdf communsPdf = new Sio4CommunsPdf();
	private Color black = new Color(0, 0, 0);
	private final Color bgColor = new Color(255, 255, 255, 0);

	private float yStartNewPage = 760, yStart = 760, bottomMargin = 26;
	private float fullWidth = 542;
	public byte[] buildPDF(ImpresionReclamacionProjection impresionReclamacionProjection) {
		ByteArrayOutputStream output;
		try {
			PDFMergerUtility pdfMerger = new PDFMergerUtility();
			try (PDDocument document = new PDDocument()) {
				try {
				
					Integer modelo =impresionReclamacionProjection.modelo;
					
			
					
					PDPage page = new PDPage();
					document.addPage(page);
					BaseTable table;
					Row<PDPage> baseRow;
					
					boolean acomula = false;
					boolean acomula2 = false;
					boolean acomula3 = false;
					boolean acomula4 = false;
			
					
					table = new BaseTable(yStart, yStartNewPage, bottomMargin, 200, 500, document, page, false,true);				 
					baseRow = communsPdf.setRow(table, 20);
					communsPdf.setCell(baseRow, 40, ImageUtils.readImage(impresionReclamacionProjection.getContratantes().getAsegurados().c6), 1, 1, bgColor).setValign(VerticalAlignment.MIDDLE);
				    table.draw();
					
					table = new BaseTable(yStart, yStartNewPage, bottomMargin, 200, 30, document, page, false,true);				 
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 80, ImageUtils.readImage(impresionReclamacionProjection.getImagenSocio()), 1, 1, bgColor).setValign(VerticalAlignment.MIDDLE);
				    table.draw();
					
					
					
					yStart -= table.getHeaderAndDataHeight()+3;
			
			
				
					table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, 30, document, page, false,true);
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 7, "Póliza:", black, false, "L", 10, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor);
					communsPdf.setCell(baseRow, 15,(impresionReclamacionProjection.getContratantes().getAsegurados().c1 !=null ? impresionReclamacionProjection.getContratantes().getAsegurados().c1 :"") , black, false, "L", 10, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor);
					communsPdf.setCell(baseRow, 7, "Inciso:", black, false, "L", 10, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor);
					communsPdf.setCell(baseRow, 6, (String.valueOf(impresionReclamacionProjection.getContratantes().getDatosGenerale().c3) !=null ? String.valueOf(impresionReclamacionProjection.getContratantes().getDatosGenerale().c3) :""), black, false, "L", 10, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor);
					communsPdf.setCell(baseRow, 18, "", black, false, "L", 10, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor);
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 12, "Contratante:", black, false, "L", 10, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor);
					communsPdf.setCell(baseRow, 60, (impresionReclamacionProjection.getContratantes().getAsegurados().c2 !=null ? impresionReclamacionProjection.getContratantes().getAsegurados().c2 :"") , black, false, "L", 10, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor);					
					communsPdf.setCell(baseRow, 12, "quattroCRM:", black, false, "L", 10, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor);
					communsPdf.setCell(baseRow, 5,(String.valueOf(impresionReclamacionProjection.getContratantes().getDatosGenerale().c14) !=null ? String.valueOf(impresionReclamacionProjection.getContratantes().getDatosGenerale().c14) :"") , black, false, "L", 10, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor);
					
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 12, "Concepto:", black, false, "L", 10, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor);
					communsPdf.setCell(baseRow, 60, (impresionReclamacionProjection.getContratantes().getAsegurados().c3 !=null ? impresionReclamacionProjection.getContratantes().getAsegurados().c3 :""), black, false, "L", 10, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor);					
					communsPdf.setCell(baseRow, 25, (impresionReclamacionProjection.getContratantes().Servicio.get(0).c1 !=null ? impresionReclamacionProjection.getContratantes().Servicio.get(0).c1 :""), black, false, "L", 10, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor);
					
					
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 12, "Vigencia:", black, false, "L", 10, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor);
					communsPdf.setCell(baseRow, 60, (impresionReclamacionProjection.getContratantes().getAsegurados().c4 !=null ? impresionReclamacionProjection.getContratantes().getAsegurados().c4 :""), black, false, "L", 10, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor);					
					communsPdf.setCell(baseRow, 15, (impresionReclamacionProjection.getContratantes().Servicio.get(0).c2 !=null ? impresionReclamacionProjection.getContratantes().Servicio.get(0).c2 :""), black, false, "L", 10, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor);
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 72, "", black, false, "L", 10, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor);
					communsPdf.setCell(baseRow, 28, (String.valueOf(impresionReclamacionProjection.getContratantes().getDatosGenerale().c6) !=null ? tipo(impresionReclamacionProjection.getContratantes().getDatosGenerale().c6) :""), black, false, "L", 9, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor);
				
					table.draw();
					
					yStart -= table.getHeaderAndDataHeight();
					
					table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, 30, document, page, true,true);
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 100, "", black, false, "L", 10, communsPdf.setLineStyle(Color.white,Color.white,Color.black,Color.white), "", communsPdf.setPadding(2f),bgColor);
					table.draw();
					
					
					yStart -= table.getHeaderAndDataHeight() +5;
					
					table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, 30, document, page, false,true);
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 65, (impresionReclamacionProjection.getContratantes().getDatosGenerale().c2 !=null ? impresionReclamacionProjection.getContratantes().getDatosGenerale().c2 :""), black, false, "L", 10, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor);
					communsPdf.setCell(baseRow, 14, "Captura:", black, false, "L", 10, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor);
					communsPdf.setCell(baseRow, 21, (impresionReclamacionProjection.getContratantes().getDatosGenerale().c9 !=null ? impresionReclamacionProjection.getContratantes().getDatosGenerale().c9 :""), black, false, "L", 10, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor);
				
					
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 15, (modelo == 1|| modelo == 2 ? "Conductor:" : modelo == 7 ? "Dirección" :"Contacto") , black, false, "L", 10, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor);
					communsPdf.setCell(baseRow, 50, (impresionReclamacionProjection.getContratantes().getDatosGenerale().c15 !=null ? impresionReclamacionProjection.getContratantes().getDatosGenerale().c15 :""), black, false, "L", 10, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor);
					communsPdf.setCell(baseRow, 14, "Siniestro:", black, false, "L", 10, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor);
					communsPdf.setCell(baseRow, 21,(impresionReclamacionProjection.getContratantes().getDatosGenerale().c10 !=null ? impresionReclamacionProjection.getContratantes().getDatosGenerale().c10 :""), black, false, "L", 10, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor);
					
					baseRow = communsPdf.setRow(table, 15);
					if(modelo == 1 || modelo == 2) {
						communsPdf.setCell(baseRow, 15, "Modelo:", black, false, "L", 10, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor);
						communsPdf.setCell(baseRow, 21, (String.valueOf(impresionReclamacionProjection.getContratantes().getDatosGenerale().c16) !=null ? String.valueOf(impresionReclamacionProjection.getContratantes().getDatosGenerale().c16 ):""), black, false, "L", 10, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor);
						communsPdf.setCell(baseRow, 10, "Nomina:", black, false, "L", 10, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor);
						communsPdf.setCell(baseRow, 19, (impresionReclamacionProjection.getContratantes().getDatosGenerale().c17 !=null ? impresionReclamacionProjection.getContratantes().getDatosGenerale().c17 :""), black, false, "L", 10, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor);
					
					}else {
						communsPdf.setCell(baseRow, 5, "CP:", black, false, "L", 10, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor);
						communsPdf.setCell(baseRow, 10, (String.valueOf(impresionReclamacionProjection.getContratantes().getDatosGenerale().c16) !=null ? String.valueOf(impresionReclamacionProjection.getContratantes().getDatosGenerale().c16 ):""), black, false, "L", 10, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor);
						communsPdf.setCell(baseRow, 10, "Colonia:", black, false, "L", 10, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor);
						communsPdf.setCell(baseRow, 40, (impresionReclamacionProjection.getContratantes().getDatosGenerale().c17 !=null ? impresionReclamacionProjection.getContratantes().getDatosGenerale().c17 :""), black, false, "L", 10, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor);
					
					}
				
					
					communsPdf.setCell(baseRow, 14, "Reporte:", black, false, "L", 10, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor);
					communsPdf.setCell(baseRow, 21,(impresionReclamacionProjection.getContratantes().getDatosGenerale().c11 !=null ? impresionReclamacionProjection.getContratantes().getDatosGenerale().c11 :""), black, false, "L", 10, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor);
				
					

					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 15, (modelo == 1 || modelo ==2 ? "No. de Serie:" : modelo == 7  ? "Municipio"  :"Celular:"), black, false, "L", 10, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor);
					if(modelo == 1 || modelo == 2 || modelo == 7) {
						communsPdf.setCell(baseRow, 50, (impresionReclamacionProjection.getContratantes().getDatosGenerale().c18 !=null ? impresionReclamacionProjection.getContratantes().getDatosGenerale().c18 :""), black, false, "L", 10, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor);	
					}else {
						communsPdf.setCell(baseRow, 50, (impresionReclamacionProjection.getContratantes().getDatosGenerale().c17 !=null ? impresionReclamacionProjection.getContratantes().getDatosGenerale().c17 :""), black, false, "L", 10, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor);
					}
					
					communsPdf.setCell(baseRow, 14, "Promesa Pago:", black, false, "L", 10, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor);
					communsPdf.setCell(baseRow, 21, (impresionReclamacionProjection.getContratantes().getDatosGenerale().c12 !=null ? impresionReclamacionProjection.getContratantes().getDatosGenerale().c12 :""), black, false, "L", 10, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor);
					
					if(modelo == 1 || modelo == 2) {
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 8, "Placas:", black, false, "L", 10, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor);
					communsPdf.setCell(baseRow, 12, (impresionReclamacionProjection.getContratantes().getDatosGenerale().c26 !=null ? impresionReclamacionProjection.getContratantes().getDatosGenerale().c26 :""), black, false, "L", 10, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor);
					communsPdf.setCell(baseRow, 8, "Clave:", black, false, "L", 10, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor);
					communsPdf.setCell(baseRow, 12, (impresionReclamacionProjection.getContratantes().getDatosGenerale().c27 !=null ? impresionReclamacionProjection.getContratantes().getDatosGenerale().c27 :""), black, false, "L", 10, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor);
					communsPdf.setCell(baseRow, 8, "Valor:", black, false, "L", 10, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor);
					communsPdf.setCell(baseRow, 17, (String.valueOf( impresionReclamacionProjection.getContratantes().getDatosGenerale().c28) !=null ? String.valueOf(impresionReclamacionProjection.getContratantes().getDatosGenerale().c28) :""), black, false, "L", 10, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor);
					communsPdf.setCell(baseRow, 14, "Pago Real:", black, false, "L", 10, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor);
					communsPdf.setCell(baseRow, 21, (impresionReclamacionProjection.getContratantes().getDatosGenerale().c13 !=null ? impresionReclamacionProjection.getContratantes().getDatosGenerale().c13 :""), black, false, "L", 10, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor);
					}
					
					table.draw();
					;
	                 yStart -= table.getHeaderAndDataHeight()+5;
					
					table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, 30, document, page, true,true);
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 100, "", black, false, "L", 10, communsPdf.setLineStyle(Color.white,Color.white,Color.black,Color.white), "", communsPdf.setPadding(2f),bgColor);
					table.draw();
					
				    yStart -= table.getHeaderAndDataHeight()+5;
				    
				    //JSONObject documentos = new JSONObject("{'c2':[{'c3':true,'c4':'Articulo 142','c5':false,'c6':false,'c7':'','c1':'qQ3WdYReTFgKnSHabEg1Tw==','c2':true},{'c3':false,'c4':'DOCUMENTOS PRUEBA...','c5':false,'c6':false,'c7':'','c1':'ukosJtaVNf5hcJP+Zrt5YA==','c2':false},{'c3':false,'c4':'DOCUMENTO PARA PRUEBA','c5':false,'c6':false,'c7':'','c1':'btFd6cFUhf/wR/Eq6P4L/A==','c2':false},{'c3':false,'c4':'PARTE DE ACCIDENTE','c5':false,'c6':false,'c7':'','c1':'zXQUkvKsL75HXYjLXZdG6Q==','c2':false}]}");
				  
				    
				    
				    
				    table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, 30, document, page, false,true);
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 100, "Documentos", black, true, "L", 10, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor);
					table.draw();
					yStart -= table.getHeaderAndDataHeight();
					
					table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, 30, document, page, false,true);
					baseRow = communsPdf.setRow(table, 15);
					
					
					int i =0;
					while(i < impresionReclamacionProjection.datos.size()) {
						acomula = true;
						table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, 30, document, page, false,true);
						baseRow = communsPdf.setRow(table, 15);
					    	if(impresionReclamacionProjection.datos.get(i).isC2()) {
					    		communsPdf.setCell(baseRow, 4, ImageUtils.readImage("https://storage.googleapis.com/quattrocrm-copsis/s32tkk/2202/Documento/2202/gee575tNCq8EBtcOzUVixlKPGSg4leUaxRy9ybfRGS6iPoX4xlHCpPUimgW7zIzu/done.png"), i, i, bgColor).setValign(VerticalAlignment.MIDDLE);
					    	}else {
						communsPdf.setCell(baseRow, 4, ImageUtils.readImage("https://storage.googleapis.com/quattrocrm-copsis/s32tkk/2201/Siniestro/2201/EbyNLChPxCfJOgraG8wVpAZX61q8AqZVGfcWXBQLkYVMSSUrPJvjYYBKTWUSEHiq/1.png"), i, i, bgColor).setValign(VerticalAlignment.MIDDLE);
					    	}
						communsPdf.setCell(baseRow, 40, impresionReclamacionProjection.datos.get(i).getC4(), black, false, "L", 10, communsPdf.setLineStyle(Color.white,Color.white,Color.black,Color.white), "", communsPdf.setPadding(2f),bgColor);
						if(modelo ==  7 && impresionReclamacionProjection.datos.get(i).isC5()) {
							communsPdf.setCell(baseRow, 40, "OBLIGATORIO", Color.red, false, "R", 10, communsPdf.setLineStyle(Color.white,Color.white,Color.black,Color.white), "", communsPdf.setPadding(2f),bgColor);
						}
						if(isEndOfPage(table)) {
							table.getRows().remove(table.getRows().size()-1);				
							yStart = 720;
							
							// CRACION NUEVA PAGINA
							page = new PDPage();
							document.addPage(page);
							acomula = false;
							
						}else {
							table.draw();
							yStart -= table.getHeaderAndDataHeight();
						}
						
						if(acomula) {								
							i++;
						} 
					
						
					}
					
					
					yStart -= table.getHeaderAndDataHeight()+5;
			
					table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, 30, document, page, true,true);
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 100, "", black, false, "L", 10, communsPdf.setLineStyle(Color.white,Color.white,Color.black,Color.white), "", communsPdf.setPadding(2f),bgColor);
					table.draw();
					
					yStart -= table.getHeaderAndDataHeight()+5;
					
					
					
					table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, 30, document, page, false,true);
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 100, "Facturas", black, true, "L", 10, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor);
					table.draw();
					yStart -= table.getHeaderAndDataHeight();
					
					table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, 30, document, page, false,true);
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 70, "FacturaEmisor/Concepto", black, true, "L", 10, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor);
					communsPdf.setCell(baseRow, 15, "Reclamado", black, true, "L", 10, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor);
					communsPdf.setCell(baseRow, 15, "Procedente", black, true, "L", 10, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor);
					table.draw();
	
					yStart -= table.getHeaderAndDataHeight();
					DecimalFormat formateador = new DecimalFormat("#,##0.00");
					Double reclamado = 0.00;
					Double totalreclamado = 0.00;
					
					Double procedente = 0.00;
					Double totalProcedente = 0.00;
					int t =0;
					int cf =0;
					while(t < impresionReclamacionProjection.getC5().getDocumentos().size()) {	
						cf++;
						acomula2= true;
						table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, 30, document, page, false,true);
						baseRow = communsPdf.setRow(table, 10);
						 reclamado = Double.parseDouble( impresionReclamacionProjection.getC5().getDocumentos().get(t).getC9());
						 procedente =Double.parseDouble( impresionReclamacionProjection.getC5().getDocumentos().get(t).getC13());
						communsPdf.setCell(baseRow, 70, impresionReclamacionProjection.getC5().getDocumentos().get(t).getC3() +"   " + impresionReclamacionProjection.getC5().getDocumentos().get(t).getC5(), black, false, "L", 10, communsPdf.setLineStyle(Color.white,Color.white,Color.black,Color.white), "", communsPdf.setPadding(2f),bgColor);
						communsPdf.setCell(baseRow, 15,formateador.format(reclamado) , black, false, "L", 10, communsPdf.setLineStyle(Color.white,Color.white,Color.black,Color.white), "", communsPdf.setPadding(2f),bgColor);
						communsPdf.setCell(baseRow, 15, impresionReclamacionProjection.getC5().getDocumentos().get(t).getC13(), black, false, "L", 10, communsPdf.setLineStyle(Color.white,Color.white,Color.black,Color.white), "", communsPdf.setPadding(2f),bgColor);
						baseRow = communsPdf.setRow(table, 10);
						communsPdf.setCell(baseRow, 90, impresionReclamacionProjection.getC5().getDocumentos().get(t).getC12(), black, false, "L", 10, communsPdf.setLineStyle(Color.white,Color.white,Color.black,Color.white), "", communsPdf.setPadding(2f),bgColor);
						
				
						totalreclamado  += reclamado;
						totalProcedente  += procedente;
						if(impresionReclamacionProjection.getC5().getDocumentos().size() == cf) {
							baseRow = communsPdf.setRow(table, 15);
							communsPdf.setCell(baseRow, 60,"" , black, false, "L", 10, communsPdf.setLineStyle(Color.white,Color.white,Color.black,Color.white), "", communsPdf.setPadding(2f),bgColor);
							communsPdf.setCell(baseRow, 10,"Total:" , black, true, "L", 10, communsPdf.setLineStyle(Color.white,Color.white,Color.black,Color.white), "", communsPdf.setPadding(2f),bgColor);
							communsPdf.setCell(baseRow, 15, formateador.format(totalreclamado) , black, false, "L", 10, communsPdf.setLineStyle(Color.white,Color.white,Color.black,Color.white), "", communsPdf.setPadding(2f),bgColor);
							communsPdf.setCell(baseRow, 15, formateador.format(totalProcedente) , black, true, "L", 10, communsPdf.setLineStyle(Color.white,Color.white,Color.black,Color.white), "", communsPdf.setPadding(2f),bgColor);
						
						}
						
					
						if(isEndOfPage(table)) {
							table.getRows().remove(table.getRows().size()-1);
							table.draw();
							yStart = 620;
							// CRACION NUEVA PAGINA
							page = new PDPage();
							document.addPage(page);
							acomula2 = false;
							
						}else {
							table.draw();
							yStart -= table.getHeaderAndDataHeight()+4;
						}
						
						if(acomula2) {								
							t++;
						} 
						
					}
					

				
				
		
					table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, 30, document, page, true,true);
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 100, "", black, false, "L", 10, communsPdf.setLineStyle(Color.white,Color.white,Color.black,Color.white), "", communsPdf.setPadding(2f),bgColor);
					table.draw();								
					yStart -= table.getHeaderAndDataHeight();
					
					
					
					
					
				
					table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, 30, document, page, false,true);
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 100, "Historial", black, true, "L", 10, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor);
					table.draw();
					yStart -= table.getHeaderAndDataHeight()+7;
					
					int x =0;
					while(x <  impresionReclamacionProjection.tramites.size() ) {
						acomula3 = true;
					
						table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, 50, document, page, false,true);
						baseRow = communsPdf.setRow(table, 15);
					    	
						communsPdf.setCell(baseRow, 10,  impresionReclamacionProjection.tramites.get(x).getC9(), black, false, "L", 10, communsPdf.setLineStyle(Color.white,Color.white,Color.black,Color.white), "", communsPdf.setPadding(2f),bgColor);
						communsPdf.setCell(baseRow, 40,   impresionReclamacionProjection.tramites.get(x).getC2(), black, false, "L", 10, communsPdf.setLineStyle(Color.white,Color.white,Color.black,Color.white), "", communsPdf.setPadding(2f),bgColor);
						communsPdf.setCell(baseRow, 40,  impresionReclamacionProjection.tramites.get(x).getC3(), black, false, "L", 10, communsPdf.setLineStyle(Color.white,Color.white,Color.black,Color.white), "", communsPdf.setPadding(2f),bgColor);
						baseRow = communsPdf.setRow(table, 15);
						communsPdf.setCell(baseRow, 15, "Observaciones:", black, true, "L", 10, communsPdf.setLineStyle(Color.white,Color.white,Color.black,Color.white), "", communsPdf.setPadding(2f),bgColor);
						communsPdf.setCell(baseRow, 40,   Sio4CommunsPdf.eliminaHtmlTags3( Sio4CommunsPdf.extractAllText(impresionReclamacionProjection.tramites.get(x).getC7())), black, false, "L", 10, communsPdf.setLineStyle(Color.white,Color.white,Color.black,Color.white), "", communsPdf.setPadding(2f),bgColor);
						baseRow = communsPdf.setRow(table, 12);
						communsPdf.setCell(baseRow, 6, "Folio:", black, true, "L", 10, communsPdf.setLineStyle(Color.white,Color.white,Color.black,Color.white), "", communsPdf.setPadding(2f),bgColor);
						communsPdf.setCell(baseRow, 40,   impresionReclamacionProjection.tramites.get(x).getC6(), black, false, "L", 10, communsPdf.setLineStyle(Color.white,Color.white,Color.black,Color.white), "", communsPdf.setPadding(2f),bgColor);											
						baseRow = communsPdf.setRow(table, 15);
						communsPdf.setCell(baseRow, 100, "___________________________________________________________", black, false, "L", 10, communsPdf.setLineStyle(Color.white,Color.white,Color.black,Color.white), "", communsPdf.setPadding(2f),bgColor);
								
						
						if(isEndOfPage(table)) {
							table.getRows().remove(true);					
							yStart = 720;
							
							page = new PDPage();
							document.addPage(page);
							acomula3 = false;
							
						}else {
							table.draw();
							yStart -= table.getHeaderAndDataHeight();
							
						}
						
						if(acomula3) {								
							x++;
						} 
					    if (i > 80) {
                            table.draw();
                            break;
                        }
						
					}
					
					
					
				
		
					table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, 30, document, page, true,true);
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 100, "", black, false, "L", 10, communsPdf.setLineStyle(Color.white,Color.white,Color.black,Color.white), "", communsPdf.setPadding(2f),bgColor);
					table.draw();
													
					yStart -= table.getHeaderAndDataHeight()+5;
			
					
				
					
					
					table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, 30, document, page, false,true);
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 100, "Bitacora", black, true, "L", 10, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor);
					table.draw();
					yStart -= table.getHeaderAndDataHeight();
				
					
					int n =0;
			
					while(n < impresionReclamacionProjection.primas.size()) {
				
						
						acomula4= true;
						table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, 30, document, page, false,true);
						baseRow = communsPdf.setRow(table, 10);											
						communsPdf.setCell(baseRow, 8, ImageUtils.readImage(impresionReclamacionProjection.primas.get(n).getC3()), i, i, bgColor).setValign(VerticalAlignment.MIDDLE);
						communsPdf.setCell(baseRow, 70, Sio4CommunsPdf.eliminaHtmlTags3( Sio4CommunsPdf.extractAllText( impresionReclamacionProjection.primas.get(n).getC1())), black, false, "L", 10, communsPdf.setLineStyle(Color.white,Color.white,Color.black,Color.white), "", communsPdf.setPadding(2f),bgColor);
						baseRow = communsPdf.setRow(table, 15);	
						communsPdf.setCell(baseRow, 7,  impresionReclamacionProjection.primas.get(n).getC4(), black, false, "R", 10, communsPdf.setLineStyle(Color.white,Color.white,Color.black,Color.white), "", communsPdf.setPadding(2f),bgColor);
						baseRow = communsPdf.setRow(table, 15);	
						communsPdf.setCell(baseRow, 60,  impresionReclamacionProjection.primas.get(n).getC9(), black, false, "R", 10, communsPdf.setLineStyle(Color.white,Color.white,Color.black,Color.white), "", communsPdf.setPadding(2f),bgColor);
						
						if(isEndOfPage(table)) {
			
							table.getRows().remove(true);					
							yStart = 720;
							// CRACION NUEVA PAGINA
						
							page = new PDPage();
							document.addPage(page);
							acomula4 = false;
							
						}else {
							table.draw();
							yStart -= table.getHeaderAndDataHeight()+5;
						}
						
						if(acomula4) {								
							n++;
						} 
						
					}
					
	
					
					table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, 30, document, page, true,true);
					baseRow = communsPdf.setRow(table, 2);
					communsPdf.setCell(baseRow, 100, "", black, false, "L", 10, communsPdf.setLineStyle(Color.white,Color.white,Color.black,Color.white), "", communsPdf.setPadding(2f),bgColor);
					table.draw();
					
					yStart -= table.getHeaderAndDataHeight()+5;
					table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, 30, document, page, false,true);
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 100, "Imagenes", black, true, "L", 10, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor);
					table.draw();
					yStart -= table.getHeaderAndDataHeight();
					
				
					String pahtpdf;
					try {
						if(!impresionReclamacionProjection.getImagenes().isEmpty()) {
						for (int j = 0; j < impresionReclamacionProjection.getImagenes().size(); j++) {
							if(impresionReclamacionProjection.getImagenes().get(j).getPath().indexOf(".pdf") > -1 
							   || impresionReclamacionProjection.getImagenes().get(j).getPath().indexOf(".PDF") > -1
							   || impresionReclamacionProjection.getImagenes().get(j).getPath().indexOf(".Pdf") > -1
									) {
								pahtpdf = impresionReclamacionProjection.getImagenes().get(j).getPath();
								if(pahtpdf.contains(" ")) {
									pahtpdf = pahtpdf.replace(" ", "%20");
								}
								
								URL scalaByExampleUrl = scalaByExampleUrl = new URL(pahtpdf);	
							
								final PDDocument documentToBeParsed = PDDocument.load(scalaByExampleUrl.openStream());							
								pdfMerger.appendDocument(document, documentToBeParsed);
							
							}else if(
									impresionReclamacionProjection.getImagenes().get(j).getPath().indexOf(".jpg") > -1
                                    || impresionReclamacionProjection.getImagenes().get(j).getPath().indexOf(".JPG") > -1
                                    || impresionReclamacionProjection.getImagenes().get(j).getPath().indexOf(".PNG") > -1
                                    || impresionReclamacionProjection.getImagenes().get(j).getPath().indexOf(".png") > -1
                                    || impresionReclamacionProjection.getImagenes().get(j).getPath().indexOf(".JPEG") > -1
                                    || impresionReclamacionProjection.getImagenes().get(j).getPath().indexOf(".jpeg") > -1
                                    || impresionReclamacionProjection.getImagenes().get(j).getPath().indexOf(".GIF") > -1
                                    || impresionReclamacionProjection.getImagenes().get(j).getPath().indexOf(".gif") > -1
									) {
								page = new PDPage();
								document.addPage(page);
								table = new BaseTable(yStart, yStartNewPage, bottomMargin, 200, 30, document, page, false,true);				 
								baseRow = communsPdf.setRow(table, 15);
								communsPdf.setCell(baseRow, 80, ImageUtils.readImage(impresionReclamacionProjection.getImagenes().get(j).getPath()), 1, 1, bgColor).setValign(VerticalAlignment.MIDDLE);
							    table.draw();
								
							}
						}
						pdfMerger.mergeDocuments();
						}
						
					} catch (Exception ex) {					
						throw new GeneralServiceException("00001",
								"Ocurrio un error en el servicio ImpresionInter: " + ex.getMessage());
					}
					
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
	public String tipo(int tipo) {
		String tipoSieniesto="";
		switch (tipo) {
		case 0:
			tipoSieniesto ="SELECCIONE TIPO";
			break;
		case 1:
			tipoSieniesto ="REMBOLSO";
			break;
		case 2:
			tipoSieniesto ="PROGRAMACIÓN";
			break;
		case 3:
			tipoSieniesto ="PAGO DIRECTO";
			break;
		case 10: case 51:
			tipoSieniesto ="COLISION/VUELCO";
			break;
		case 11: case 52:
			tipoSieniesto ="INCENDIO/RAYO/EXPLOSION";
			break;
		case 12: case 53:
			tipoSieniesto ="FHM";
			break;
		case 13:	case 54:
			tipoSieniesto ="TERREMOTO";
			break;
		case 14:	case 55:
			tipoSieniesto ="ROBO";
			break;
		case 15:	case 56:
			tipoSieniesto ="ASISTENCIA";
			break;
		case 17:	case 58:
			tipoSieniesto ="CRISTALES";
			break;
		case 18:
			tipoSieniesto ="ATROPELLAMIENTO";
			break;
		case 19:
			tipoSieniesto ="INTENTO DE ROBO";
			break;
		case 20:
			tipoSieniesto ="AJUSTE EXPRES";
			break;
		case 21:
			tipoSieniesto ="INUNDACIÓN";
			break;
		case 22:
			tipoSieniesto ="ROBO PARCIAL";
			break;
		case 23:
			tipoSieniesto ="CAIDA DE OBJETOS";
			break;
		case 24:
			tipoSieniesto ="ALCANCE (DAR)";
			break;
		case 25:
			tipoSieniesto ="VOLCADURA";
			break;
		case 26:
			tipoSieniesto ="AUTOPISTA";
			break;
		case 27:
			tipoSieniesto ="VUELTA INDEBIDA";
			break;
		case 28:
			tipoSieniesto ="FALTA DE PERICIA";
			break;
		case 29:
			tipoSieniesto ="COLISION CON OBJETO FIJO";
			break;
		case 50:
			tipoSieniesto ="ACCIDENTE";
			break;
		case 57:
			tipoSieniesto ="OTRO";
			break;
		case 59:
			tipoSieniesto ="EQUIPO ELECTRONICO";
			break;
		case 60:
			tipoSieniesto ="ROTURA DE MAQUINARIA";
			break;
		case 61:
			tipoSieniesto ="ANUNCIOS LUMINOSOS";
			break;
		case 62:
			tipoSieniesto ="DINERO Y VALORES";
			break;
			
		case 63:
			tipoSieniesto ="RESPONSABILIDAD CIVIL";
			break;
			
		case 81:
			tipoSieniesto ="MUERTE";
			break;
		case 82:
			tipoSieniesto ="MUERTE ACCIDENTAL'";
			break;
		case 83:
			tipoSieniesto ="INDEMNIZACIÓN";
			break;
	
		default:
			break;
		}
	 return tipoSieniesto;
	}

}
