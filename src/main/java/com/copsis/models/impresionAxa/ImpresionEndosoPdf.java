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

public class ImpresionEndosoPdf {
	private final Color bgColor = new Color(255, 255, 255, 0);
	private float margin = 10, yStartNewPage = 780, yStart = 780, bottomMargin = 130;
	private float fullWidth =590;
	private Sio4CommunsPdf communsPdf = new Sio4CommunsPdf();
	public float pPaginacion;
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

					setEncabezado(impresionAxa, document, page,false);
					pPaginacion = 780 - 15;
					table = new BaseTable(yStart, yStartNewPage, bottomMargin, 400, margin, document, page, false, true);
	                baseRow = communsPdf.setRow(table, 15);
	                communsPdf.setCell(baseRow,100, "Por medio de este documento se hace constar que:",Color.BLACK,true, "L",10, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
	                baseRow = communsPdf.setRow(table, 15);
	                if(impresionAxa.getTxtMovimiento() !=null) {
	                	communsPdf.setCell(baseRow,100, impresionAxa.getTxtMovimiento()  +" y obligaciones estipulados en la póliza a las personas anotadas a continuación: " ,Color.BLACK,false, "L",10, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
	                }
	                table.draw();
	                yStart -= (table.getHeaderAndDataHeight() + 25);
	                
	               	table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, margin, document, page, false, true);
	                baseRow = communsPdf.setRow(table);
	                communsPdf.setCell(baseRow, 12,"Certificado",Color.BLACK,true, "C", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
	                communsPdf.setCell(baseRow, 27,"Nombre",Color.BLACK,true, "C", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
	                communsPdf.setCell(baseRow, 4,"Edad",Color.BLACK,true, "C", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(0f,0f,3f,0f),bgColor);
	                communsPdf.setCell(baseRow, 10,"Fecha Nacimiento",Color.BLACK,true, "C", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
	                communsPdf.setCell(baseRow, 12,"Parentesco",Color.BLACK,true, "C", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
	                communsPdf.setCell(baseRow, 10,"Suma Asgurada",Color.BLACK,true, "C", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
	                if(impresionAxa.getTipoEndoso() == 4 || impresionAxa.getTipoEndoso() == 2 || impresionAxa.getTipoEndoso() == 14) {
	                	 communsPdf.setCell(baseRow, 13,"Fecha Alta",Color.BLACK,true, "C", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
	                }else {
	                	 communsPdf.setCell(baseRow, 13,"Fecha Baja",Color.BLACK,true, "C", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
	                }
	                communsPdf.setCell(baseRow, 10,"Prima",Color.BLACK,true, "C", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
	                if (isEndOfPage(table)) {
                        table.getRows().remove(table.getRows().size() - 1);
                        table.draw();
                        page = new PDPage();
                        document.addPage(page);
                        setEncabezado(impresionAxa, document, page, false);
                       
                    } else {
                        table.draw();
                    }
	                yStart -= (table.getHeaderAndDataHeight() + 5);
                    PDPageContentStream content01 = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, true);
                    communsPdf.drawBox(content01, Color.black, 10, yStart - 2, 585, 0.5f);
                    content01.close();
                    
                    
                    int x=0;
                    while(x< impresionAxa.getAsegurados().size()) {
                    	acumula = true;
                    	table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, margin, document, page, false, true);
                        baseRow = communsPdf.setRow(table);                        
                        communsPdf.setCell(baseRow, 12, impresionAxa.getAsegurados().get(x).getCertificado(),Color.BLACK,false, "L", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
    	                communsPdf.setCell(baseRow, 27,Sio4CommunsPdf.eliminaHtmlTags3(impresionAxa.getAsegurados().get(x).getNombre()),Color.BLACK,false, "L", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
    	                communsPdf.setCell(baseRow, 4,impresionAxa.getAsegurados().get(x).getEdad(),Color.BLACK,false, "C", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(0f,0f,3f,0f),bgColor);
    	                communsPdf.setCell(baseRow, 10,impresionAxa.getAsegurados().get(x).getFechNacimiento(),Color.BLACK,false, "C", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
    	                communsPdf.setCell(baseRow, 12,impresionAxa.getAsegurados().get(x).getParentesco(),Color.BLACK,false, "C", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
    	                communsPdf.setCell(baseRow, 10,impresionAxa.getAsegurados().get(x).getSa(),Color.BLACK,false, "C", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);    	              
    	                communsPdf.setCell(baseRow, 13,impresionAxa.getAsegurados().get(x).getFechaBaja(),Color.BLACK,false, "C", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);   	          
    	                communsPdf.setCell(baseRow, 10,impresionAxa.getAsegurados().get(x).getPrima(),Color.BLACK,false, "C", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
                        if (isEndOfPage(table)) {
                            table.getRows().remove(table.getRows().size() - 1);
                            table.draw();
                            page = new PDPage();
                            document.addPage(page);
                            setEncabezado(impresionAxa, document, page, true);
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
                    
                    yStart -= table.getHeaderAndDataHeight();
                    table = new BaseTable(yStart, yStartNewPage, bottomMargin, 400, 50, document, page, false, true);
                    baseRow = communsPdf.setRow(table, 15);
                    communsPdf.setCell(baseRow, 100,"Total por Subgrupo "+ impresionAxa.getSubGrupo() ,Color.BLACK,false, "L", 10, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
                    baseRow = communsPdf.setRow(table, 15);
                    baseRow = communsPdf.setRow(table, 15);
                    communsPdf.setCell(baseRow, 30,"TOTAL DE ENDOSO" ,Color.BLACK,false, "L", 10, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
                    communsPdf.setCell(baseRow, 20,"0000" ,Color.BLACK,false, "L", 10, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
                    communsPdf.setCell(baseRow, 20,"MOVIMIENTO" ,Color.BLACK,false, "L", 10, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
                    communsPdf.setCell(baseRow, 12,impresionAxa.getNoCertificado() ,Color.BLACK,false, "L", 10, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(0f,0f,3f,0f),bgColor);
                    table.draw();
                    
                    
                    
                    Float poPaginacion = pPaginacion - 60;
                    int nume = document.getNumberOfPages();                                 
                    for (int v = 0; v < nume; v++) {
                        PDPage page2 = document.getPage(v);
                        try (PDPageContentStream content = new PDPageContentStream(document, page2, PDPageContentStream.AppendMode.PREPEND, true, true)) {
                            int u = 1;
                            u += v;
                            String numeF = Integer.toString(u);
                            communsPdf.drawText(content, true, 580, poPaginacion, numeF);
                        }
                    }
                    
                    
                    
					output = new ByteArrayOutputStream();
					document.save(output);
	                document.save(new File("/home/aalbanil/Vídeos/cerficadoAxa.pdf"));
					return output.toByteArray();
				} finally {
					document.close();
				}

			}

		} catch (Exception ex) {

			throw new GeneralServiceException("00001",
					"Ocurrio un error en el servicio ImpresionEndosoPdf: " + ex.getMessage());
		}
	}

	private void setEncabezado(ImpresionAxaForm impresionAxa, PDDocument document, PDPage page, Boolean asegurados) {
		try (PDPageContentStream content = new PDPageContentStream(document, page)) {
			yStart = 780;
			BaseTable table;
			Row<PDPage> baseRow;
	        table = new BaseTable(yStart, yStartNewPage, bottomMargin, 295, margin, document, page, false, true);
            baseRow = communsPdf.setRow(table, 12);
            communsPdf.setCell(baseRow, 100, ImageUtils.readImage(impresionAxa.getLogoSuperior()));
            table.draw();
            
            table = new BaseTable(yStart, yStartNewPage, bottomMargin, 200, (602 - 200), document, page, false, true);
            baseRow = communsPdf.setRow(table, 12);
            communsPdf.setCell(baseRow, 100,"Endoso",Color.BLACK,true, "R", 11, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
            baseRow = communsPdf.setRow(table, 12);
            communsPdf.setCell(baseRow, 100,"Gastos Médicos Mayores",Color.BLACK,true, "R", 11, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
            baseRow = communsPdf.setRow(table, 12);
            communsPdf.setCell(baseRow, 100,impresionAxa.getEtiquetaPlan(),Color.BLACK,false, "R", 11, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
            baseRow = communsPdf.setRow(table, 12);
            communsPdf.setCell(baseRow, 65,"Póliza",Color.BLACK,true, "R", 10, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
            communsPdf.setCell(baseRow, 35,impresionAxa.getNoPoliza(),Color.BLACK,false, "R", 10, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
            baseRow = communsPdf.setRow(table, 12);
        	communsPdf.setCell(baseRow, 100,"ORIGINAL",Color.BLACK,true, "R", 10, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
            baseRow = communsPdf.setRow(table, 12);
        	communsPdf.setCell(baseRow, 90,"Hoja:",Color.BLACK,true, "R", 10, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
            table.draw();
            
            yStart -= (table.getHeaderAndDataHeight() + 10);

            table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, margin, document, page, false, true);
            baseRow = communsPdf.setRow(table, 12);
            communsPdf.setCell(baseRow, 20,"CONTRATANTE",Color.BLACK,true, "L", 11, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,4f,5f),bgColor);
            baseRow = communsPdf.setRow(table);
            communsPdf.setCell(baseRow, 20,"Nombre",Color.BLACK,true, "L", 10, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,4f,5f),bgColor);	            
            communsPdf.setCell(baseRow, 80,impresionAxa.getContrannte(),Color.BLACK,false, "L", 10, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,4f,5f),bgColor);
            baseRow = communsPdf.setRow(table);
            communsPdf.setCell(baseRow, 20,"Fecha de Emisión:",Color.BLACK,true, "L", 10, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,4f,5f),bgColor);
            communsPdf.setCell(baseRow, 80,impresionAxa.getFechaEmision(),Color.BLACK,false, "L", 10, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,4f,5f),bgColor);
            baseRow = communsPdf.setRow(table);
            communsPdf.setCell(baseRow, 20,"Vigencia de:",Color.BLACK,true, "L", 10, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,4f,5f),bgColor);
            communsPdf.setCell(baseRow, 80,impresionAxa.getVigenciaDe(),Color.BLACK,false, "L", 10, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,4f,5f),bgColor);
            baseRow = communsPdf.setRow(table);
            communsPdf.setCell(baseRow, 20,"Duracion dias:",Color.BLACK,true, "L", 10, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,4f,5f),bgColor);
            communsPdf.setCell(baseRow, 80,"360",Color.BLACK,false, "L", 10, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,4f,5f),bgColor);
            baseRow = communsPdf.setRow(table);
            communsPdf.setCell(baseRow, 20,"Forma de Pago:",Color.BLACK,true, "L", 10, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,4f,5f),bgColor);
            communsPdf.setCell(baseRow, 80,impresionAxa.getFormaPago(),Color.BLACK,false, "L", 10, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,4f,5f),bgColor);
            baseRow = communsPdf.setRow(table);
            communsPdf.setCell(baseRow, 20,"Moneda:",Color.BLACK,true, "L", 10, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,4f,5f),bgColor);
            communsPdf.setCell(baseRow, 80,impresionAxa.getMoneda(),Color.BLACK,false, "L", 10, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,4f,5f),bgColor);
            baseRow = communsPdf.setRow(table);
            communsPdf.setCell(baseRow, 20,"Numero de Endoso:",Color.BLACK,true, "L", 10, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,4f,5f),bgColor);
            communsPdf.setCell(baseRow, 30,"",Color.BLACK,true, "L", 10, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,4f,5f),bgColor);
         
            communsPdf.setCell(baseRow, 13,"Movimiento:",Color.BLACK,true, "L", 10, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(3f,5f,4f,5f),bgColor);
            communsPdf.setCell(baseRow, 9,impresionAxa.getEndosoId(),Color.BLACK,false, "L", 10, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,4f,5f),bgColor);
            baseRow = communsPdf.setRow(table);
            communsPdf.setCell(baseRow, 20,"Subgrupo:",Color.BLACK,true, "L", 10, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,4f,5f),bgColor);
            communsPdf.setCell(baseRow, 80,impresionAxa.getSubGrupo(),Color.BLACK,false, "L", 10, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,4f,5f),bgColor);    
            table.draw();
            
            yStart -= (table.getHeaderAndDataHeight() + 10);
            
            if(asegurados) {
            	table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, margin, document, page, false, true);
                baseRow = communsPdf.setRow(table);
                communsPdf.setCell(baseRow, 12,"Certificado",Color.BLACK,true, "C", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,4f,5f),bgColor);
                communsPdf.setCell(baseRow, 27,"Nombre",Color.BLACK,true, "C", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,4f,5f),bgColor);
                communsPdf.setCell(baseRow, 4,"Edad",Color.BLACK,true, "C", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,4f,5f),bgColor);
                communsPdf.setCell(baseRow, 10,"Fecha Nacimiento",Color.BLACK,true, "C", 11, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,4f,5f),bgColor);
                communsPdf.setCell(baseRow, 12,"Parentesco",Color.BLACK,true, "C", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,4f,5f),bgColor);
                communsPdf.setCell(baseRow, 10,"Suma Asgurada",Color.BLACK,true, "C", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,4f,5f),bgColor);
                if(impresionAxa.getTipoEndoso() == 4) {
                	 communsPdf.setCell(baseRow, 13,"Fecha Alta",Color.BLACK,true, "C", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,4f,5f),bgColor);
                }else {
                	 communsPdf.setCell(baseRow, 13,"Fecha Baja",Color.BLACK,true, "C", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,4f,5f),bgColor);
                }
                communsPdf.setCell(baseRow, 10,"Prima",Color.BLACK,true, "C", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,4f,5f),bgColor);
                yStart -= (table.getHeaderAndDataHeight());
            }
            
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
            
            
		
		} catch (Exception ex) {	
			throw new GeneralServiceException("00001",
					"Ocurrio un error en el servicio ImpresionEndosoPdf: " + ex.getMessage());
		}
	}
	
    private boolean isEndOfPage(BaseTable table) {
        float currentY = yStart - table.getHeaderAndDataHeight();
        boolean isEndOfPage = currentY <= bottomMargin;
        return isEndOfPage;
    }


}
