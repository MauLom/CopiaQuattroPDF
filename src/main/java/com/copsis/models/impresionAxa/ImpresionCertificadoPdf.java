package com.copsis.models.impresionAxa;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.File;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;

import com.copsis.controllers.forms.ImpresionAxaForm;
import com.copsis.exceptions.GeneralServiceException;
import com.copsis.models.Tabla.BaseTable;
import com.copsis.models.Tabla.ImageUtils;
import com.copsis.models.Tabla.Row;
import com.copsis.models.Tabla.Sio4CommunsPdf;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ImpresionCertificadoPdf {
	private byte[] byteArrayPDF;
	private final Color bgColor = new Color(255, 255, 255, 0);
	private Color blue = new Color(40, 76, 113);
	private Color black = new Color(0, 0, 0);
	private Color gray = new Color(229, 234, 237);
	private Color gray2 = new Color(239, 239, 239);
	private float margin = 10, yStartNewPage = 780, yStart = 780, bottomMargin = 170, yDespacho = 0, bottomMargin2 = 30;
	private float fullWidth = 590;
	private Boolean acumula;
	private Boolean acumula2;
	private Boolean acumula3;
	private float yStart2;
	private boolean drawLines = true;
	private boolean txtExtranjero = false;

	private Sio4CommunsPdf communsPdf = new Sio4CommunsPdf();

	public byte[] buildPDF(ImpresionAxaForm impresionAxa) {
		ByteArrayOutputStream output;
		try {
			try (PDDocument document = new PDDocument()) {
				try {
					System.out.println("asegurado => " + new ObjectMapper().writeValueAsString(impresionAxa));
	                    PDPage page = new PDPage();
	                    document.addPage(page);
	                    BaseTable table;
	                    setEncabezado(impresionAxa, document,  page);
					
	                    output = new ByteArrayOutputStream();
						document.save(output);
						document.save(new File("/home/aalbanil/Documentos/AXA-SPRING-PF/certificado.pdf"));
						return output.toByteArray();
				} finally {
					document.close();
				}
				
			}
			
		} catch (Exception ex) {
			throw new GeneralServiceException("00001",
					"Ocurrio un error en el servicio ImpresionCredencialPdf: " + ex.getMessage());
		}
		
	}

	private void setEncabezado(ImpresionAxaForm impresionAxa, PDDocument document, PDPage page) {
		try (PDPageContentStream content = new PDPageContentStream(document, page)) {
	          yStart = 780;
	            BaseTable table;
	            Row<PDPage> baseRow;
	            
	            table = new BaseTable(yStart, yStartNewPage, bottomMargin, 295, margin, document, page, false, true);
	            baseRow = communsPdf.setRow(table, 12);
	            communsPdf.setCell(baseRow, 100, ImageUtils.readImage(impresionAxa.getLogoSuperior()));
	            table.draw();
	            String valor = "";

	            int with = 150;
	         
	            switch (impresionAxa.getRamo()) {
	                case 4:
	                    valor = "Gastos Médicos Mayores";
	                    with = 150;

	                    break;
	                case 10:
	                    valor = "GASTOS MÉDICOS / ACCIDENTES PERSONALES";
	                    with = 250;

	                    break;
	                default:
	                    valor = "Gastos Médicos Mayores";
	                    with = 150;

	                    break;

	            }
	            
	            table = new BaseTable(yStart, yStartNewPage, bottomMargin, with, (602 - with), document, page, false, true);
	            baseRow = communsPdf.setRow(table, 12);	          	          
	            communsPdf.setCell(baseRow, 100,"Certificado",Color.BLACK,true, "R", 11, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
	            baseRow = communsPdf.setRow(table, 12);	          	          
	            communsPdf.setCell(baseRow, 100,valor,Color.BLACK,true, "R", 10, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);	        
	            baseRow = communsPdf.setRow(table, 12);
	            
	            if (impresionAxa.getRamo() == 10) {
	                //communsPdf.setCell(baseRow, 100, "Protección Ideal Colectivo", black, null, true, 9, "R", 3f);//cCAMBIO DE VALOR DE SUBRAMO
	            	communsPdf.setCell(baseRow, 100,"Protección Ideal Colectivo",Color.BLACK,false, "R", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
	            } else {	                
	            	communsPdf.setCell(baseRow, 100, impresionAxa.getSubGrupo(),Color.BLACK,false, "R", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);	            	               
	            }
	            table.draw();
	            yStart -= (table.getHeaderAndDataHeight() + 10);
	            
	            table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, margin, document, page, false, true);
	            baseRow = communsPdf.setRow(table, 12);
	            communsPdf.setCell(baseRow, 20,"Contratante",Color.BLACK,true, "L", 11, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
	            baseRow = communsPdf.setRow(table, 12);
	            communsPdf.setCell(baseRow, 10,"Nombre",Color.BLACK,true, "L", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);	            
	            communsPdf.setCell(baseRow, 70,impresionAxa.getContrannte(),Color.BLACK,false, "L", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
	            communsPdf.setCell(baseRow, 8,"Póliza:",Color.BLACK,true, "L", 11, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
	            communsPdf.setCell(baseRow, 12,impresionAxa.getNoPoliza(),Color.BLACK,true, "L", 11, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
	            table.draw();
	            
	            yStart -= (table.getHeaderAndDataHeight() + 10);
	            table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, margin, document, page, false, true);
	            baseRow = communsPdf.setRow(table, 12);
	            communsPdf.setCell(baseRow, 20,"Datos de la Póliza",Color.BLACK,true, "L", 11, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
	            baseRow = communsPdf.setRow(table, 12);
	            communsPdf.setCell(baseRow, 20,"Fecha de Emisión",Color.BLACK,true, "L", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);	            
	            communsPdf.setCell(baseRow, 80,impresionAxa.getFechaEmision(),Color.BLACK,false, "L", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
	            baseRow = communsPdf.setRow(table, 12);
	            communsPdf.setCell(baseRow, 20,"Vigencia de",Color.BLACK,true, "L", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
	            communsPdf.setCell(baseRow, 80,impresionAxa.getVigenciaDe(),Color.BLACK,false, "L", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
	            baseRow = communsPdf.setRow(table, 12);
	            if (impresionAxa.getRamo() == 10) {
	            	communsPdf.setCell(baseRow, 20,"Mondeda:",Color.BLACK,false, "L", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
	            }else {
	            	communsPdf.setCell(baseRow, 20,"Endoso:",Color.BLACK,false, "L", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
	            	communsPdf.setCell(baseRow, 20,"",Color.BLACK,true, "L", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
	            }
	            
	            table.draw();
	            
	            
	            yStart -= (table.getHeaderAndDataHeight() + 10);
	            table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, margin, document, page, false, true);
	            baseRow = communsPdf.setRow(table, 12);
	            communsPdf.setCell(baseRow, 100,"Datos del Asegurado",Color.BLACK,true, "L", 11, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
	            baseRow = communsPdf.setRow(table, 12);
	            communsPdf.setCell(baseRow, 10,"Certificado",Color.BLACK,true, "L", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);	            
	            communsPdf.setCell(baseRow, 47,impresionAxa.getNoCertificado(),Color.BLACK,false, "L", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);	   
	            if(impresionAxa.getPrimas() !=null && impresionAxa.getPrimas().size() >  0) {

	             communsPdf.setCell(baseRow, 10,"Prima Neta",Color.BLACK,true, "L", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
	  	         communsPdf.setCell(baseRow, 29,impresionAxa.getPrimas().get(0).getPrima(),Color.BLACK,false, "L", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
	  	         communsPdf.setCell(baseRow, 4,"",Color.BLACK,false, "L", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor); 
	            }
	            
	            baseRow = communsPdf.setRow(table, 12);
	            communsPdf.setCell(baseRow, 10,"Nombre",Color.BLACK,true, "L", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);	            
	            communsPdf.setCell(baseRow, 47,Sio4CommunsPdf.eliminaHtmlTags3( impresionAxa.getAsegurado()),Color.BLACK,false, "L", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);	   
	            
	            if(impresionAxa.getPrimas() !=null && impresionAxa.getPrimas().size() >  0) {
	             communsPdf.setCell(baseRow,29, "R.A. ó Cesión de Comisión",Color.BLACK,true, "L", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
	  	         communsPdf.setCell(baseRow,10,"0.00",Color.BLACK,false, "L", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
	  	         communsPdf.setCell(baseRow, 4,"",Color.BLACK,false, "L", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor); 
	            }
	            baseRow = communsPdf.setRow(table, 12);
	            if (impresionAxa.getRamo() == 10) {
	            	communsPdf.setCell(baseRow, 10,"Subgrupo",Color.BLACK,true, "L", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
	            	if(impresionAxa.getSubGrupo() !=null) {
	            		communsPdf.setCell(baseRow, 90, impresionAxa.getSubGrupo()+ " de vigencia contados a partir de la fecha de ingreso del asegurado en la póliza.",Color.BLACK,false, "R", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);	
	            	}	            	
	            }else {
	            	if(impresionAxa.getSubGrupo() !=null) {
	            		communsPdf.setCell(baseRow, 10,"Subgrupo",Color.BLACK,true, "L", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
	            		communsPdf.setCell(baseRow, 47, impresionAxa.getSubGrupo(),Color.BLACK,false, "L", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
	            	}
	                if(impresionAxa.getPrimas() !=null && impresionAxa.getPrimas().size() >  0) {
	   	             communsPdf.setCell(baseRow,29, "Recargo por Pago Fraccionado",Color.BLACK,true, "L", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
	   	  	         communsPdf.setCell(baseRow,10,impresionAxa.getPrimas().get(0).getRecargo(),Color.BLACK,false, "L", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
	   	  	         communsPdf.setCell(baseRow, 4,"",Color.BLACK,false, "L", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor); 
	   	            }
	            }
	            
	            baseRow = communsPdf.setRow(table, 12);
	            communsPdf.setCell(baseRow,30, "Fecha de Nacimiento",Color.BLACK,true, "L", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
	  	        communsPdf.setCell(baseRow,15,impresionAxa.getFecNacAseg(),Color.BLACK,false, "L", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
	  	        communsPdf.setCell(baseRow, 7,"Edad",Color.BLACK,true, "L", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor); 
	  	        communsPdf.setCell(baseRow, 4,impresionAxa.getEdadAseg(),Color.BLACK,false, "L", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
	  	        
	  	        if(impresionAxa.getPrimas() !=null && impresionAxa.getPrimas().size() > 0) {
	   	             communsPdf.setCell(baseRow,29, "Derecho de Póliza",Color.BLACK,true, "L", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
	   	  	         communsPdf.setCell(baseRow,10,impresionAxa.getPrimas().get(0).getDerecho(),Color.BLACK,false, "L", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
	   	  	         communsPdf.setCell(baseRow, 4,"",Color.BLACK,false, "L", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor); 
	   	         } 
	  	        
	  	        baseRow = communsPdf.setRow(table, 12);
	            communsPdf.setCell(baseRow,30, "Fecha de Ingreso a la Póliza",Color.BLACK,true, "L", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
	  	        communsPdf.setCell(baseRow,27,impresionAxa.getFecAltAseg(),Color.BLACK,false, "L", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
	  	      	  	        
	  	        if(impresionAxa.getPrimas() !=null && impresionAxa.getPrimas().size() > 0) {
	   	             communsPdf.setCell(baseRow,29, "I.V.A.",Color.BLACK,true, "L", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
	   	  	         communsPdf.setCell(baseRow,10,impresionAxa.getPrimas().get(0).getIva(),Color.BLACK,false, "L", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
	   	  	         communsPdf.setCell(baseRow, 4,"",Color.BLACK,false, "L", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor); 
	   	         } 
	  	        
		  	      if (impresionAxa.getRamo() == 10) {
		  	    	  
		  	      }else {
		  	    	 baseRow = communsPdf.setRow(table, 12);
		  	        communsPdf.setCell(baseRow,30, "Fecha de Vencimiento",Color.BLACK,true, "L", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
		  	        communsPdf.setCell(baseRow,27,impresionAxa.getVigenciaA(),Color.BLACK,false, "L", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
		  	      	  	    
		  	      }
	            
	            
	            table.draw();
	            

		} catch (Exception ex) {
			ex.printStackTrace();
			throw new GeneralServiceException("00001",
					"Ocurrio un error en el servicio setEncabezado: " + ex.getMessage());
		}

	}

}
