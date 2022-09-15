package com.copsis.models.impresionAxa;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.font.PDType0Font;

import com.copsis.clients.projections.CotizacionProjection;
import com.copsis.exceptions.GeneralServiceException;
import com.copsis.models.TextoAxa;
import com.copsis.models.Tabla.BaseTable;
import com.copsis.models.Tabla.ImageUtils;
import com.copsis.models.Tabla.PDocumenteHW;
import com.copsis.models.Tabla.Row;
import com.copsis.models.Tabla.Sio4CommunsPdf;

public class ImpresionCotizacionVida {
	private final Color bgColor = new Color(255, 255, 255, 0);
	private final Color bgColorA = new Color(0, 44, 134, 0);
	private float margin = 10, yStartNewPage = 1150, yStart = 1150, bottomMargin = 130;
	private float fullWidth = 590;
	private Sio4CommunsPdf communsPdf = new Sio4CommunsPdf();

	public byte[] buildPDF(CotizacionProjection cotizacionProjection) {
		ByteArrayOutputStream output;
		try {
			try (PDDocument document = new PDDocument()) {
				try {
					PDPage page = new PDPage(PDocumenteHW.A3);
					
					document.addPage(page);
					BaseTable table;
					Row<PDPage> baseRow;

					table = new BaseTable(1170, 1170, bottomMargin, 607, 20, document, page, false, true);		           
			        baseRow = communsPdf.setRow(table, 18);
		            communsPdf.setCell(baseRow,60, "12/8/22, 12:13",Color.BLACK,false, "L",9, communsPdf.setLineStyle(Color.BLACK), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);	                 		    
		            communsPdf.setCell(baseRow,30, "Cotizador Vida - AXA",Color.BLACK,false, "L",9, communsPdf.setLineStyle(Color.BLACK), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor).setLeftPadding(25f);	                 		    				     
		            table.draw();
		             
                    PDType0Font lbsef = PDType0Font.load(document,  new File(getClass().getClassLoader().getResource("imgInter/sans.ttf").toURI()));
		
					table = new BaseTable(1160, 1160, bottomMargin, 120, 100, document, page, false, true);
					baseRow = communsPdf.setRow(table, 12);
					communsPdf.setCell(baseRow, 100, ImageUtils.readImage("https://storage.googleapis.com/quattrocrm/h31fila/2208/Polizas/2208/zeSWdRgKylw7QuyGNMQ9vc8HSPiToM8cVUnQd5e2VOIzWrci1IgN1QLcOhFEfhy/Axalogo.png"));
					table.draw();
					
					
					 table = new BaseTable(1160, 1160, bottomMargin, 200, 350, document, page, false, true);
			         baseRow = communsPdf.setRow(table, 12);
			         communsPdf.setCell(baseRow, 45, ImageUtils.readImage("https://storage.googleapis.com/quattrocrm-copsis/s32tkk/2209/Polizas/2209/vgXoyBQh6weQnOWoF1ap30noi64mtwZU5BN487XnlHdLiukVtfh295bEepZm0Kl/barra2.png"));
			         table.draw();
			         
			         table = new BaseTable(yStart, yStart, bottomMargin, 210, 510, document, page, false, true);
		             
			         baseRow = communsPdf.setRow(table, 15);
		             communsPdf.setCell(baseRow,100, "12 de Agosto de 2022 v2022",Color.BLACK,true, "C",9, communsPdf.setLineStyle(Color.BLACK), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
			         baseRow = communsPdf.setRow(table, 18);
		             communsPdf.setCell(baseRow,17, "ROL:",Color.BLACK,true, "L",9, communsPdf.setLineStyle(Color.BLACK), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
		             communsPdf.setCell(baseRow,83, "FUNCIONARIO COMERCIAL",Color.BLACK,true, "L",9, communsPdf.setLineStyle(Color.BLACK), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
		             baseRow = communsPdf.setRow(table, 18);
		             communsPdf.setCell(baseRow,26, "NOMBRE:",Color.BLACK,true, "L",9, communsPdf.setLineStyle(Color.BLACK), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
		             communsPdf.setCell(baseRow,74, "",Color.BLACK,true, "L",9, communsPdf.setLineStyle(Color.BLACK), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
		             baseRow = communsPdf.setRow(table, 18);
		             communsPdf.setCell(baseRow,17, "MX:",Color.BLACK,true, "L",9, communsPdf.setLineStyle(Color.BLACK), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
		             communsPdf.setCell(baseRow,83, "",Color.BLACK,true, "L",9, communsPdf.setLineStyle(Color.BLACK), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
		             baseRow = communsPdf.setRow(table, 18);
		             communsPdf.setCell(baseRow,21, "EMAIL:",Color.BLACK,true, "L",9, communsPdf.setLineStyle(Color.BLACK), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
		             communsPdf.setCell(baseRow,79, "Guillermo.Sanchez@axa.com.mx",Color.BLACK,true, "L",9, communsPdf.setLineStyle(Color.BLACK), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);		             
		             table.draw();
		             
					
		             
		             yStart -= table.getHeaderAndDataHeight()-10;
		             table = new BaseTable(yStart, yStart, bottomMargin, 150, 119, document, page,  false, true);
		             baseRow = communsPdf.setRow(table, 15);
		             communsPdf.setCell(baseRow,100, "Vida ProTGT 2022",Color.BLACK,true, "L",13, communsPdf.setLineStyle(Color.BLACK), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
		             table.draw();
		             yStart -= table.getHeaderAndDataHeight()+8;
		             
		             
			         table = new BaseTable(yStart, yStart, bottomMargin, 607, 116, document, page, true, true);
		             baseRow = communsPdf.setRow(table, 5);
		             communsPdf.setCell(baseRow,100, "",Color.BLACK,true, "L",10, communsPdf.setLineStyle(bgColorA), "", communsPdf.setPadding(5f,5f,5f,5f),bgColorA);
		             table.draw();
		             
		             yStart -= table.getHeaderAndDataHeight()+5;
		             table = new BaseTable(yStart, yStart, bottomMargin, 607, 119, document, page,  false, true);
		             baseRow = communsPdf.setRow(table, 15);
		             communsPdf.setCell(baseRow,100, "Datos del Prospecto:",Color.BLACK,true, "L",10, communsPdf.setLineStyle(Color.BLACK), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
		             table.draw();
		             
		             yStart -= table.getHeaderAndDataHeight()-10;
		            
		             if(cotizacionProjection.isVida()) {
		                 
		            	 table = new BaseTable(yStart, yStart, bottomMargin, 180, 270, document, page,  false, true);
		                 baseRow = communsPdf.setRow(table, 19);
			             communsPdf.setCell(baseRow,100, "Titular",Color.BLACK,true, "L",10, communsPdf.setLineStyle(Color.BLACK), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);			             
			             baseRow = communsPdf.setRow(table, 15);
			             communsPdf.setCell(baseRow,30, "Nombre:",Color.BLACK,true, "L",10, communsPdf.setLineStyle(Color.BLACK), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
			             communsPdf.setCell(baseRow,70, "Guillermo Sanchez",Color.BLACK,false, "L",10, communsPdf.setLineStyle(Color.BLACK), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
			             baseRow = communsPdf.setRow(table, 15);
			             communsPdf.setCell(baseRow,30, "Edad:",Color.BLACK,true, "L",10, communsPdf.setLineStyle(Color.BLACK),"", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
			             communsPdf.setCell(baseRow,70, "37 años",Color.BLACK,false, "L",10, communsPdf.setLineStyle(Color.BLACK), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);			   
			             baseRow = communsPdf.setRow(table, 15);
			             communsPdf.setCell(baseRow,30, "Género:",Color.BLACK,true, "L",10, communsPdf.setLineStyle(Color.BLACK), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
			             communsPdf.setCell(baseRow,70, "HOMBRE",Color.BLACK,false, "L",10, communsPdf.setLineStyle(Color.BLACK), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);			    
			             baseRow = communsPdf.setRow(table, 15);
			             communsPdf.setCell(baseRow,30, "Hábito:",Color.BLACK,true, "L",10, communsPdf.setLineStyle(Color.BLACK), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
			             communsPdf.setCell(baseRow,70, "NO FUMADOR",Color.BLACK,false, "L",10, communsPdf.setLineStyle(Color.BLACK), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
			             table.draw();

		            	 table = new BaseTable(yStart, yStart, bottomMargin, 180, 450, document, page,  false, true);
		                 baseRow = communsPdf.setRow(table, 19);
			             communsPdf.setCell(baseRow,100, "Menor",Color.BLACK,true, "L",10, communsPdf.setLineStyle(Color.BLACK), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);			             
			             baseRow = communsPdf.setRow(table, 15);
			             communsPdf.setCell(baseRow,30, "Nombre:",Color.BLACK,true, "L",10, communsPdf.setLineStyle(Color.BLACK), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
			             communsPdf.setCell(baseRow,70, "Guillermo Sanchez",Color.BLACK,false, "L",10, communsPdf.setLineStyle(Color.BLACK), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
			             baseRow = communsPdf.setRow(table, 15);
			             communsPdf.setCell(baseRow,30, "Edad:",Color.BLACK,true, "L",10, communsPdf.setLineStyle(Color.BLACK), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
			             communsPdf.setCell(baseRow,70, "37 años",Color.BLACK,false, "L",10, communsPdf.setLineStyle(Color.BLACK), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);			   
			             baseRow = communsPdf.setRow(table, 15);
			             communsPdf.setCell(baseRow,30, "Género:",Color.BLACK,true, "L",10, communsPdf.setLineStyle(Color.BLACK), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
			             communsPdf.setCell(baseRow,70, "HOMBRE",Color.BLACK,false, "L",10, communsPdf.setLineStyle(Color.BLACK), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);			    
			             table.draw();
		             }else {
		                 table = new BaseTable(yStart, yStart, bottomMargin, 300, 320, document, page,  false, true);
			             baseRow = communsPdf.setRow(table, 15);
			             communsPdf.setCell(baseRow,30, "Nombre:",Color.BLACK,true, "L",10, communsPdf.setLineStyle(Color.BLACK), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
			             communsPdf.setCell(baseRow,70, "Guillermo Sanchez",Color.BLACK,false, "L",10, communsPdf.setLineStyle(Color.BLACK), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
			             baseRow = communsPdf.setRow(table, 15);
			             communsPdf.setCell(baseRow,30, "Edad:",Color.BLACK,true, "L",10, communsPdf.setLineStyle(Color.BLACK), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
			             communsPdf.setCell(baseRow,70, "37 años",Color.BLACK,false, "L",10, communsPdf.setLineStyle(Color.BLACK), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
			    
			             baseRow = communsPdf.setRow(table, 15);
			             communsPdf.setCell(baseRow,30, "Género:",Color.BLACK,true, "L",10, communsPdf.setLineStyle(Color.BLACK), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
			             communsPdf.setCell(baseRow,70, "HOMBRE",Color.BLACK,false, "L",10, communsPdf.setLineStyle(Color.BLACK), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
			    
			             baseRow = communsPdf.setRow(table, 15);
			             communsPdf.setCell(baseRow,30, "Hábito:",Color.BLACK,true, "L",10, communsPdf.setLineStyle(Color.BLACK), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
			             communsPdf.setCell(baseRow,70, "NO FUMADOR",Color.BLACK,false, "L",10, communsPdf.setLineStyle(Color.BLACK), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
			             table.draw();
		             }
		       
		             
		             
		             yStart -= table.getHeaderAndDataHeight()+20;
		             
		             table = new BaseTable(yStart, yStart, bottomMargin, 607, 116, document, page, true, true);
		             baseRow = communsPdf.setRow(table, 5);
		             communsPdf.setCell(baseRow,100, "",Color.BLACK,true, "L",10, communsPdf.setLineStyle(bgColorA), "", communsPdf.setPadding(5f,5f,5f,5f),bgColorA);
		             table.draw();
		             
		             yStart -= table.getHeaderAndDataHeight()+5;
		             table = new BaseTable(yStart, yStart, bottomMargin, 607, 119, document, page,  false, true);
		             baseRow = communsPdf.setRow(table, 15);
		             communsPdf.setCell(baseRow,100, "Características del Plan:",Color.BLACK,true, "L",10, communsPdf.setLineStyle(Color.BLACK), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
		             table.draw();
		             
		             yStart -= table.getHeaderAndDataHeight()+5;
		             table = new BaseTable(yStart, yStart, bottomMargin, 400, 320, document, page,  false, true);
		             baseRow = communsPdf.setRow(table, 15);
		             communsPdf.setCell(baseRow,40, "Plan:",Color.BLACK,true, "L",10, communsPdf.setLineStyle(Color.BLACK), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
		             communsPdf.setCell(baseRow,60, "Aliados+",Color.BLACK,false, "L",10, communsPdf.setLineStyle(Color.BLACK), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
		             baseRow = communsPdf.setRow(table, 15);
		             communsPdf.setCell(baseRow,40, "Tipo de Moneda:",Color.BLACK,true, "L",10, communsPdf.setLineStyle(Color.BLACK), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
		             communsPdf.setCell(baseRow,60, "",Color.BLACK,false, "L",10, communsPdf.setLineStyle(Color.BLACK), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
		    
		             baseRow = communsPdf.setRow(table, 15);
		             communsPdf.setCell(baseRow,40, "Plazo de pagos:",Color.BLACK,true, "L",10, communsPdf.setLineStyle(Color.BLACK), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
		             communsPdf.setCell(baseRow,60, "",Color.BLACK,false, "L",10, communsPdf.setLineStyle(Color.BLACK), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
		    
		             baseRow = communsPdf.setRow(table, 15);
		             communsPdf.setCell(baseRow,40, "Prima anual:",Color.BLACK,true, "L",10, communsPdf.setLineStyle(Color.BLACK), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
		             communsPdf.setCell(baseRow,60, "",Color.BLACK,false, "L",10, communsPdf.setLineStyle(Color.BLACK), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
		    
		  
		             baseRow = communsPdf.setRow(table, 15);
		             communsPdf.setCell(baseRow,40, "Aportación:",Color.BLACK,true, "L",10, communsPdf.setLineStyle(Color.BLACK), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
		             communsPdf.setCell(baseRow,60, "",Color.BLACK,false, "L",10, communsPdf.setLineStyle(Color.BLACK), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);

		             baseRow = communsPdf.setRow(table, 15);
		             communsPdf.setCell(baseRow,40, "Prima mensual:",Color.BLACK,true, "L",10, communsPdf.setLineStyle(Color.BLACK), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
		             communsPdf.setCell(baseRow,60, "",Color.BLACK,false, "L",10, communsPdf.setLineStyle(Color.BLACK), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);

		             baseRow = communsPdf.setRow(table, 15);
		             communsPdf.setCell(baseRow,40, "Valor de la UDI:",Color.BLACK,true, "L",10, communsPdf.setLineStyle(Color.BLACK), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
		             communsPdf.setCell(baseRow,60, "",Color.BLACK,false, "L",10, communsPdf.setLineStyle(Color.BLACK), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);

		             
		             baseRow = communsPdf.setRow(table, 25);
		             communsPdf.setCell(baseRow,40, "",Color.BLACK,true, "L",10, communsPdf.setLineStyle(Color.BLACK), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
		             communsPdf.setCell(baseRow,60, "",Color.BLACK,false, "L",10, communsPdf.setLineStyle(Color.BLACK), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);

		             
		             baseRow = communsPdf.setRow(table, 15);
		             communsPdf.setCell(baseRow,40, "Prima Anual:",Color.BLACK,true, "L",10, communsPdf.setLineStyle(Color.BLACK), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
		             communsPdf.setCell(baseRow,60, "",Color.BLACK,false, "L",10, communsPdf.setLineStyle(Color.BLACK), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);

		             baseRow = communsPdf.setRow(table, 15);
		             communsPdf.setCell(baseRow,40, "Aportación:",Color.BLACK,true, "L",10, communsPdf.setLineStyle(Color.BLACK), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
		             communsPdf.setCell(baseRow,60, "",Color.BLACK,false, "L",10, communsPdf.setLineStyle(Color.BLACK), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);

		             baseRow = communsPdf.setRow(table, 15);
		             communsPdf.setCell(baseRow,40, "Prima mensual:",Color.BLACK,true, "L",10, communsPdf.setLineStyle(Color.BLACK), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
		             communsPdf.setCell(baseRow,60, "",Color.BLACK,false, "L",10, communsPdf.setLineStyle(Color.BLACK), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
		    
		             table.draw();

		             yStart -= table.getHeaderAndDataHeight()+15;
		             
		             if(cotizacionProjection.isVida()) {
		            	 ArrayList<TextoAxa> texto  = this.textolist2();		             		         		           
			             int i=0;		             
			             for (TextoAxa textoAxa : texto) {
			         
			            	  table = new BaseTable(yStart, yStart, bottomMargin, 607, 116, document, page, false, true);
					             baseRow = communsPdf.setRow(table, 5);
					             communsPdf.setCell(baseRow,100, "",Color.BLACK,true, "L",10, communsPdf.setLineStyle(bgColorA), "", communsPdf.setPadding(5f,5f,5f,5f),bgColorA);
					             
					             baseRow = communsPdf.setRow(table, 5);
					             communsPdf.setCell(baseRow,100, "",Color.BLACK,true, "L",10, communsPdf.setLineStyle(Color.white,Color.white,Color.white,Color.white), "", communsPdf.setPadding(5f,5f,5f,5f),bgColor);
					             table.draw();
					             
					             yStart -= table.getHeaderAndDataHeight();
					            
					             table = new BaseTable(yStart, yStart, bottomMargin, 500, 160, document, page, true, true);
					             System.out.println(i +"--> " + textoAxa.getNombre());
					             if(i == 2) {
					            	  baseRow.setLineSpacing(1.7f);
					            	 baseRow = communsPdf.setRow(table, 30);					            
						             communsPdf.setCell(baseRow,15,textoAxa.getNombre(),Color.BLACK,false, "L",10, communsPdf.setLineStyle(bgColor), "", communsPdf.setPadding(0f,0f,0f,0f),bgColor).setTopPadding(0);
						             communsPdf.setCell(baseRow,85, "",Color.BLACK,true, "L",10, communsPdf.setLineStyle(bgColor), "", communsPdf.setPadding(5f,5f,5f,5f),bgColor);
							       
					             }else {
					            	 baseRow = communsPdf.setRow(table, 15);
					           	 
						             communsPdf.setCell(baseRow,15,textoAxa.getNombre(),Color.BLACK,false, "L",10, communsPdf.setLineStyle(bgColor), "", communsPdf.setPadding(0f,0f,0f,0f),bgColor).setTopPadding(0);
						             communsPdf.setCell(baseRow,85, "",Color.BLACK,true, "L",10, communsPdf.setLineStyle(bgColor), "", communsPdf.setPadding(5f,5f,5f,5f),bgColor);
							      
					             }
					                 if(i == 2) {
						        	   baseRow = communsPdf.setRow(table, 50);  
						        	   baseRow.setLineSpacing(1.5f);
						           }else {
						        	   baseRow = communsPdf.setRow(table, 25);
						           }
					             
					             communsPdf.setCell(baseRow,2, "",Color.BLACK,false, "C",12, communsPdf.setLineStyle(bgColor), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
					             communsPdf.setCellImg(baseRow, 8,  ImageUtils.readImage(textoAxa.getLogo()), communsPdf.setLineStyle(bgColor), communsPdf.setPadding2(0f,0f,0f,0f), "C", "T");						 					            		             
					             communsPdf.setCell(baseRow,5, "",Color.BLACK,false, "C",12, communsPdf.setLineStyle(bgColor), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
					             communsPdf.setCell(baseRow,85, Sio4CommunsPdf.eliminaHtmlTags3(textoAxa.getTexto()),Color.BLACK,false, "C",12, communsPdf.setLineStyle(bgColor), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor).setTopPadding(-10f);		    				             
					             table.draw();
					             yStart -= table.getHeaderAndDataHeight();
					           	 i++;  
						}
			             
		             }
		                 
		             else {
		             ArrayList<TextoAxa> texto  = this.textolist();		             		         		           
		             int i=0;		             
		             for (TextoAxa textoAxa : texto) {
		         
		            	  table = new BaseTable(yStart, yStart, bottomMargin, 607, 116, document, page, true, true);
				             baseRow = communsPdf.setRow(table, 5);
				             communsPdf.setCell(baseRow,100, "",Color.BLACK,true, "L",10, communsPdf.setLineStyle(bgColorA), "", communsPdf.setPadding(5f,5f,5f,5f),bgColorA);
				             
				             baseRow = communsPdf.setRow(table, 5);
				             communsPdf.setCell(baseRow,100, "",Color.BLACK,true, "L",10, communsPdf.setLineStyle(Color.white,Color.white,Color.white,Color.white), "", communsPdf.setPadding(5f,5f,5f,5f),bgColor);
				             table.draw();
				             
				             yStart -= table.getHeaderAndDataHeight();
				            
				             table = new BaseTable(yStart, yStart, bottomMargin, 500, 160, document, page, true, true);
				             baseRow = communsPdf.setRow(table, 10);		
				             communsPdf.setCell(baseRow,15,textoAxa.getNombre(),Color.BLACK,false, "L",10, communsPdf.setLineStyle(bgColor), "", communsPdf.setPadding(0f,0f,0f,0f),bgColor).setTopPadding(0);
				             communsPdf.setCell(baseRow,85, "",Color.BLACK,true, "L",10, communsPdf.setLineStyle(bgColor), "", communsPdf.setPadding(5f,5f,5f,5f),bgColor);
					           if(i == 1) {
					        	   baseRow = communsPdf.setRow(table, 50);  
					        	   baseRow.setLineSpacing(1.5f);
					           }else {
					        	   baseRow = communsPdf.setRow(table, 25);
					           }
				             
				             communsPdf.setCell(baseRow,2, "",Color.BLACK,false, "C",12, communsPdf.setLineStyle(bgColor), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
				             communsPdf.setCellImg(baseRow, 8,  ImageUtils.readImage(textoAxa.getLogo()), communsPdf.setLineStyle(bgColor), communsPdf.setPadding2(0f,0f,0f,0f), "C", "T");						 					            		             
				             communsPdf.setCell(baseRow,5, "",Color.BLACK,false, "C",12, communsPdf.setLineStyle(bgColor), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
				             communsPdf.setCell(baseRow,85, Sio4CommunsPdf.eliminaHtmlTags3(textoAxa.getTexto()),Color.BLACK,false, "C",12, communsPdf.setLineStyle(bgColor), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor).setTopPadding(-10f);		    				             
				             table.draw();
				             yStart -= table.getHeaderAndDataHeight();
				           	 i++;  
					}
		          }   
		             
		             
		             
		             
		           StringBuilder txtexta = new StringBuilder();
		           txtexta.append("Esta cotización tiene una validez de 15 días naturales contados a partir de la fecha de su elaboración, por lo que no se garantiza la emisión del seguro. La misma es ilustrativa y de apoyo a"
		           		+ " la fuerza de ventas. No forma parte del contrato del seguro."
		           		+ "\nAXA Seguros se reserva el derecho de solicitar la respuesta a un cuestionario médico y de ocupación, así como estudios de laboratorio.");
		           yStart = yStart-10;
		           
		             table = new BaseTable(yStart, yStart, bottomMargin, 607, 116, document, page, true, true);
		             baseRow = communsPdf.setRow(table, 5);
		             communsPdf.setCell(baseRow,100, "",Color.BLACK,true, "L",10, communsPdf.setLineStyle(bgColorA), "", communsPdf.setPadding(5f,5f,5f,5f),bgColorA);		             
		             baseRow = communsPdf.setRow(table, 5);
		             communsPdf.setCell(baseRow,100, "",Color.BLACK,true, "L",10, communsPdf.setLineStyle(Color.white,Color.white,Color.white,Color.white), "", communsPdf.setPadding(5f,5f,5f,5f),bgColor);		   		             
		             table.draw();
		             
		             yStart -= table.getHeaderAndDataHeight();
		             table = new BaseTable(yStart, yStart, bottomMargin, 600, 130, document, page, true, true);
		             baseRow = communsPdf.setRow(table, 15);		
		             communsPdf.setCell(baseRow,100,  Sio4CommunsPdf.eliminaHtmlTags3(txtexta.toString()),Color.BLACK,false, "C",8, communsPdf.setLineStyle(bgColor), "", communsPdf.setPadding(0f,0f,0f,0f),bgColor).setFont(lbsef);
		  
		             table.draw();
		           
		             
		             
		             
		           
					
					output = new ByteArrayOutputStream();
					document.save(output);
					document.save(new File("/home/aalbanil/Documentos/AXA-SPRING-PF/Cotizacion/CotizadoCopsis.pdf"));

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
	
	public ArrayList<TextoAxa> textolist(){
		ArrayList<TextoAxa> texto  = new ArrayList<>();
		
		texto.add(new TextoAxa ("En caso de fallecimiento, tus beneficiarios recibirán la cantidad de 60,000 UDI\\nen una sola exhibición.","Fallecimiento","https://storage.googleapis.com/quattrocrm-copsis/s32tkk/2209/Polizas/2209/vgXoyBQh6weQnOWoF1ap3v03subQdoyiLTpAC23gTmZtumlkE1aOjuGc7iNrvJu/img_fallecimiento.png"));
		
		texto.add(new TextoAxa ("En caso de invalidez total y permanente, tu seguro quedará exento del pago de"
				+"las primas de la cobertura básica y continuará protegido por fallecimiento, y recibirás la cantidad de: 60,000 UDI ","Invalidez",
				"https://storage.googleapis.com/quattrocrm-copsis/s32tkk/2209/Polizas/2209/vgXoyBQh6weQnOWoF1ap3wYQXtgGX6UUCw3nwJjDgnDVbYwyI0yXefqCa1PljA2/img_invalidez.png"));
		texto.add(new TextoAxa ("Sin cobertura de desempleo.","Desempleo","https://storage.googleapis.com/quattrocrm-copsis/s32tkk/2209/Polizas/2209/vgXoyBQh6weQnOWoF1ap35CqK9bSQv9UAHn9peTdR9pG82SEWKl4yrl2K8HGCyz/img_desempleo.png"));
		

	
//		texto.add("Al final del plazo recibirás la cantidad de 25,000 UDI.");
//		texto.add("En caso de fallecimiento, tus beneficiarios recibirán la cantidad de 25,000 UDI.");
//		texto.add("En caso de fallecimiento o invalidez del asegurado titular, el seguro quedará\nexento de pago de primas y continuará vigente hasta el final del plazo.");
//		texto.add("Esta cotización tiene una validez de 15 días naturales contados a partir de la fecha de su elaboración, por lo que no se garantiza la emisión del seguro. La misma es ilustrativa y de apoyo a"
//				+ "");
		return texto;
		
	}
	
	public ArrayList<TextoAxa> textolist2(){
		ArrayList<TextoAxa> texto  = new ArrayList<>();
		
		texto.add(new TextoAxa ("Al final del plazo recibirás la cantidad de 25,000 UDI.","Supervivencia o fallecimiento del menor",
				"https://storage.googleapis.com/quattrocrm-copsis/s32tkk/2209/Polizas/2209/vgXoyBQh6weQnOWoF1ap3ycM3aqESEttRzcUQB4RU6yf28q8Kbuiju0TRNNoqjD/img_supervivencia.png"));
		
		texto.add(new TextoAxa ("En caso de fallecimiento, tus beneficiarios recibirán la cantidad de 25,000 UDI","Fallecimiento del titular",
				"https://storage.googleapis.com/quattrocrm-copsis/s32tkk/2209/Polizas/2209/vgXoyBQh6weQnOWoF1ap3v03subQdoyiLTpAC23gTmZtumlkE1aOjuGc7iNrvJu/img_fallecimiento.png"));
		texto.add(new TextoAxa ("En caso de fallecimiento o invalidez del asegurado titular, el seguro quedará\nexento de pago de primas y continuará vigente hasta el final del plazo.","Exención por fallecimiento o invalidez",
				"https://storage.googleapis.com/quattrocrm-copsis/s32tkk/2209/Polizas/2209/vgXoyBQh6weQnOWoF1ap30wRrI8t9rObuUQ39uigNZFZ0ZKRud6Q9F3zP8dMWf/img_excencion.png"));
		
		return texto;
		
	}
	

}
