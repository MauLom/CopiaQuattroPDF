package com.copsis.models.impresionAxa;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.net.URL;

import javax.imageio.ImageIO;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import com.copsis.controllers.forms.ImpresionAxaForm;
import com.copsis.exceptions.GeneralServiceException;
import com.copsis.models.Tabla.BaseTable;
import com.copsis.models.Tabla.ImageUtils;
import com.copsis.models.Tabla.Row;
import com.copsis.models.Tabla.Sio4CommunsPdf;

public class ImpresionConstanciaAntiguedad {
	private Sio4CommunsPdf communsPdf = new Sio4CommunsPdf();
	private final Color bgColor = new Color(255, 255, 255, 0);
	private float margin = 10, yStartNewPage = 780, yStart = 780, bottomMargin = 130;
	private float fullWidth = 590;
	private Boolean acumula;

	public byte[] buildPDF(ImpresionAxaForm impresionAxa) {
		ByteArrayOutputStream output;
		try {
			try (PDDocument document = new PDDocument()) {
				try {

					PDPage page = new PDPage();
					document.addPage(page);
					BaseTable table;
					Row<PDPage> baseRow;

					setEncabezado(impresionAxa, document, page);
					
					table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, margin, document, page, false, true);
					baseRow = communsPdf.setRow(table);
			        communsPdf.setCell(baseRow, 100,"Carta Constancia de Antigüedad",Color.BLACK,false, "C", 16, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
			        baseRow = communsPdf.setRow(table);
			        communsPdf.setCell(baseRow, 100,"Seguro de Gastos Médico Mayores",Color.BLACK,false, "C", 12, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);            
			        table.draw();
			        
			        yStart -= (table.getHeaderAndDataHeight() + 10);
			        
			    	table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, margin, document, page, false, true);
					baseRow = communsPdf.setRow(table);
			        communsPdf.setCell(baseRow, 15,"CONTRATANTE",Color.BLACK,true, "L", 10, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
			        communsPdf.setCell(baseRow, 85,impresionAxa.getContrannte(),Color.BLACK,false, "L", 10, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
			    	baseRow = communsPdf.setRow(table);
			        communsPdf.setCell(baseRow, 15,"PÓLIZA",Color.BLACK,true, "L", 10, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
			        communsPdf.setCell(baseRow, 85,impresionAxa.getNoPoliza(),Color.BLACK,false, "L", 10, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
			    	baseRow = communsPdf.setRow(table);
			        communsPdf.setCell(baseRow, 15,"VIGENCIA",Color.BLACK,true, "L", 10, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
			        communsPdf.setCell(baseRow, 85,impresionAxa.getVigenciaDe(),Color.BLACK,false, "L", 10, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
			        table.draw();
			        
			        yStart -= (table.getHeaderAndDataHeight() + 10);
			        
			    	table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, margin, document, page, false, true);
					baseRow = communsPdf.setRow(table);
			        communsPdf.setCell(baseRow, 25,"A quien  Corresponda:",Color.BLACK,true, "L", 11, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
			        table.draw();
			        
			        yStart -= (table.getHeaderAndDataHeight() + 5);
			       	table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, margin, document, page, false, true);
					baseRow = communsPdf.setRow(table);
			        communsPdf.setCell(baseRow, 100,"Por medio de la presente se hace constar que las personas mencionadas a continuación se encuentran amparadas en la póliza arriba indicada",Color.BLACK,false, "L", 10, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
			        table.draw();
			        
			        yStart -= (table.getHeaderAndDataHeight() + 5);
			        table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, margin, document, page, false, true);
	                baseRow = communsPdf.setRow(table);
	                communsPdf.setCell(baseRow, 15,"Certificado",Color.BLACK,true, "L", 10, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,4f,5f),bgColor);
	                communsPdf.setCell(baseRow, 35,"Nombre",Color.BLACK,true, "L", 10, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,4f,5f),bgColor);	                
	                communsPdf.setCell(baseRow, 20,"Fecha Nacimiento",Color.BLACK,true, "C", 10, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,4f,5f),bgColor);
	                communsPdf.setCell(baseRow, 13,"Parentesco",Color.BLACK,true, "C", 10, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,4f,5f),bgColor);
	                communsPdf.setCell(baseRow, 13,"Antigüedad",Color.BLACK,true, "C", 10, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,4f,5f),bgColor);
	                table.draw();
                    yStart -= (table.getHeaderAndDataHeight());
                    if(impresionAxa.getAsegurados() !=null) {
                    int x=0;
                    
                    while(x< impresionAxa.getAsegurados().size()) {     
                    	acumula = true;
                        table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, margin, document, page, false, true);
    	                baseRow = communsPdf.setRow(table);
                    	communsPdf.setCell(baseRow, 15, impresionAxa.getAsegurados().get(x).getCertificado(),Color.BLACK,false, "C", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
    	                communsPdf.setCell(baseRow, 35,Sio4CommunsPdf.eliminaHtmlTags3(impresionAxa.getAsegurados().get(x).getNombre()),Color.BLACK,false, "L", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
    	                communsPdf.setCell(baseRow, 20,impresionAxa.getAsegurados().get(x).getFechNacimiento(),Color.BLACK,false, "C", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
    	                communsPdf.setCell(baseRow, 13,impresionAxa.getAsegurados().get(x).getParentesco(),Color.BLACK,false, "C", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
    	                communsPdf.setCell(baseRow, 13,impresionAxa.getAsegurados().get(x).getFechAntigueda(),Color.BLACK,false, "C", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
                    	
                    	 if (isEndOfPage(table)) {
                             table.getRows().remove(table.getRows().size() - 1);
                             table.draw();
                             page = new PDPage();
                             document.addPage(page);
                     		setEncabezado(impresionAxa, document, page);
                             acumula = false;
                         } else {
                             table.draw();
                             yStart -= table.getHeaderAndDataHeight();
                         }
                         if (acumula) {
                             x++;
                         }
                         if (x > 150) {
                             table.draw();
                             break;
                         }
                    }
			        
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
					"Ocurrio un error en el servicio ImpresionConstanciaAntiguedad: " + ex.getMessage());
		}

	}

	private void setEncabezado(ImpresionAxaForm impresionAxa, PDDocument document, PDPage page) {
		try (PDPageContentStream content = new PDPageContentStream(document, page)) {
			BaseTable table;
			Row<PDPage> baseRow;

			table = new BaseTable(yStart, yStartNewPage, bottomMargin, 295, margin, document, page, false, true);
			baseRow = communsPdf.setRow(table, 12);
			if(impresionAxa.getLogoSuperior() != null) {
				communsPdf.setCell(baseRow, 100, ImageUtils.readImage(impresionAxa.getLogoSuperior()));	
			}else {
				communsPdf.setCell(baseRow, 100,"",Color.BLACK,false, "L", 10, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
			}
			
			table.draw();
			yStart -= (table.getHeaderAndDataHeight() + 10);

			table = new BaseTable(290, 130, 9, fullWidth, margin, document, page, false, true);
			baseRow = communsPdf.setRow(table);
	        communsPdf.setCell(baseRow, 80,"Encontrándose el asegurado vigente a la fecha.",Color.BLACK,false, "L", 10, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
	    	baseRow = communsPdf.setRow(table);
	        communsPdf.setCell(baseRow, 80,"Se extiende la presente para los fines que se estimen convenientes.",Color.BLACK,false, "L", 10, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
	        baseRow = communsPdf.setRow(table,50);
	        baseRow = communsPdf.setRow(table,50);
	        communsPdf.setCell(baseRow, 70,"* Este documento es informativo y no forma parte del contrato del seguro, el cual se regirá por disposiciones contractuales y legales aplicables.",Color.BLACK,false, "L", 10, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
	        table.draw();
	        
	        
	        table = new BaseTable(90, 90, 9, fullWidth, margin, document, page, false, true);
            baseRow = communsPdf.setRow(table, 10);
            communsPdf.setCell(baseRow,100, "México D.F. a " + impresionAxa.getFecInicioAseg(),Color.BLACK,false, "C",10, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
            baseRow = communsPdf.setRow(table, 9);
            communsPdf.setCell(baseRow,22,"Félix Cuevas #366, Piso 3",Color.BLACK,false, "L",10, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
            baseRow = communsPdf.setRow(table, 9);
            communsPdf.setCell(baseRow,22, "Col. Tlacoquemécatl",Color.BLACK,false, "L",10, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
            baseRow = communsPdf.setRow(table, 9);
            communsPdf.setCell(baseRow,100, "Alcaldía Benito Juárez, C.P. 03200",Color.BLACK,false, "L",10, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
            baseRow = communsPdf.setRow(table, 9);
            communsPdf.setCell(baseRow,22, "Ciudad de México",Color.BLACK,false, "L",10, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);		             
            table.draw();
            
            
            table = new BaseTable(90, 90, 9, 150, 450, document, page, false, true);
            baseRow = communsPdf.setRow(table, 9);	          
            communsPdf.setCell(baseRow, 100, ImageUtils.readImage("https://storage.googleapis.com/quattrocrm-copsis/s32tkk/2207/Polizas/2207/4Avwv9KqPKZ31lIeYtlac4IcQfxQb9bn7ywY43F1ufipY7UgiHHJe4Y6TI4OL/firmaApoderado.jpg").scale(80, 80),  communsPdf.setLineStyle(Color.white), communsPdf.setPadding(0f,40f,-20f,40f), "L", "");
            baseRow = communsPdf.setRow(table, 9);	        
            communsPdf.setCell(baseRow,90, "Apoderado" ,Color.BLACK,false, "C",10, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
            table.draw();

       
            PDImageXObject pdImage2 = LosslessFactory.createFromImage(document, ImageIO.read(new URL ("https://storage.googleapis.com/quattrocrm-copsis/s32tkk/2207/Polizas/2207/4Avwv9KqPKZ31lIeYtlacxDUsyamerpi7ccyYDus88M3evIUZ2DWQYhyx9VfvnRx/marcaxa.png")));
            content.drawImage(pdImage2, 20, 30, 612, 792);

		} catch (Exception ex) {
			throw new GeneralServiceException("00001",
					"Ocurrio un error en el servicio ImpresionConstanciaAntiguedad: setEncabezado " + ex.getMessage());
		}

	}

	private boolean isEndOfPage(BaseTable table) {

		float currentY = yStart - table.getHeaderAndDataHeight();
		boolean isEndOfPage = currentY <= bottomMargin;
		return isEndOfPage;
	}
}
