package com.copsis.models.impresionAxa;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.text.DateFormatSymbols;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;

import com.copsis.clients.projections.CoberturaProjection;
import com.copsis.controllers.forms.ImpresionAxaForm;
import com.copsis.exceptions.GeneralServiceException;
import com.copsis.models.Tabla.BaseTable;
import com.copsis.models.Tabla.ImageUtils;
import com.copsis.models.Tabla.Row;
import com.copsis.models.Tabla.Sio4CommunsPdf;

public class ImpresionCertificadoPdf {

	private final Color bgColor = new Color(255, 255, 255, 0);

	private Color black = new Color(0, 0, 0);

	private float margin = 10, yStartNewPage = 780, yStart = 780, bottomMargin = 170;
	private float fullWidth = 590;

	private boolean txtExtranjero = false;

	private Sio4CommunsPdf communsPdf = new Sio4CommunsPdf();

	public byte[] buildPDF(ImpresionAxaForm impresionAxa) {

	     boolean acumula;
		 boolean acumula2;
		 boolean drawLines = true;
		ByteArrayOutputStream output;
		try {
			try (PDDocument document = new PDDocument()) {
				try {
	                    PDPage page = new PDPage();
	                    document.addPage(page);
	                    BaseTable table;
	                    Row<PDPage> baseRow;
	                    
	                    setEncabezado(impresionAxa, document,  page,false ,false);
	                    
	                    
	                    if (impresionAxa.getRamo() == 10) {
	                    	bottomMargin=180;
	                    }
	                    if(impresionAxa.getCoberturas() !=null) {	                    		                    
	                    	for (CoberturaProjection cbo : impresionAxa.getCoberturas() ) {
	                    		if(cbo.getValExtranjero() != null && cbo.getValExtranjero().length() > 0) {
	                    			txtExtranjero = true;
	                    		}else {
	                    			 txtExtranjero = false;
	                    		}	                    	
	                    	}	                    		                 
	                    }
	                    
	                    table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, margin, document, page, false, true);	                   
	                    if (impresionAxa.getRamo() == 10) {
	                    	 baseRow = communsPdf.setRow(table, 10);
	                    	 communsPdf.setCell(baseRow, 2,"",Color.BLACK,true, "L", 11, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
	                    	 communsPdf.setCell(baseRow, 40,"Cobertura",Color.BLACK,true, "L", 11, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
	                         baseRow = communsPdf.setRow(table);
	                     	 communsPdf.setCell(baseRow, 100,Sio4CommunsPdf.eliminaHtmlTags3("<b>Regla de la Suma Asegurada</b>:  Global(Suma Asegurada Fija)"),Color.BLACK,true, "L", 11, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
	                     	 baseRow = communsPdf.setRow(table);
	                    }
	                    
	                    baseRow = communsPdf.setRow(table);
	                    if (impresionAxa.getRamo() == 10) {
	                    	communsPdf.setCell(baseRow, 50,Sio4CommunsPdf.eliminaHtmlTags3("Beneficios\nBásicos"),Color.BLACK,true, "L", 11, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
	                    	communsPdf.setCell(baseRow, 50,"Suma Asegurada",Color.BLACK,true, "L", 11, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
	                    
	                    }else {
	                    	communsPdf.setCell(baseRow, 40,"Cobertura",Color.BLACK,true, "L", 11, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
	                    	communsPdf.setCell(baseRow, 30,"Nacional",Color.BLACK,true, "C", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
	                    	 if (txtExtranjero) {
	                    		 communsPdf.setCell(baseRow, 30,"Extranjero",Color.BLACK,true, "C", 11, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
	                    	 }else {
	                    		 communsPdf.setCell(baseRow, 30,"",Color.BLACK,true, "C", 11, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
	                    	 }
	                    }

	                    table.draw();
	                    yStart -= (table.getHeaderAndDataHeight());
	                    
	                    int x=0;
	                    
	                    while(x < impresionAxa.getCoberturas().size()) {
	                        acumula = true;
	                        table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, margin, document, page, false, true);
	                        if (x % 2 == 0) {
	                            drawLines = true;
	                        } else {
	                            drawLines = false;
	                        }
	                        baseRow = communsPdf.setRow(table, 11);
	                        if (impresionAxa.getRamo() == 10) {
	                        	communsPdf.setCell(baseRow, 5,"",Color.BLACK,false, "L", 11, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
	                        	communsPdf.setCell(baseRow, 45,(impresionAxa.getCoberturas().get(x).getNombres() !=null  ?impresionAxa.getCoberturas().get(x).getNombres() :"" ),Color.BLACK,false, "L", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),(drawLines ? black : null));
	                        	communsPdf.setCell(baseRow, 50,(impresionAxa.getCoberturas().get(x).getSa() !=null  ?impresionAxa.getCoberturas().get(x).getSa() :"" ),Color.BLACK,false, "R", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),(drawLines ? black : null));
	                        }else {
	                        	communsPdf.setCell(baseRow, 40,(impresionAxa.getCoberturas().get(x).getNombres() !=null  ?impresionAxa.getCoberturas().get(x).getNombres() :"" ),Color.BLACK,false, "L", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),(drawLines ? black : null));
	                        	communsPdf.setCell(baseRow, 30,(impresionAxa.getCoberturas().get(x).getSa() !=null  ?impresionAxa.getCoberturas().get(x).getSa() :"" ),Color.BLACK,false, "L", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),(drawLines ? black : null));
	                        	communsPdf.setCell(baseRow, 30,(impresionAxa.getCoberturas().get(x).getValExtranjero() !=null  ?impresionAxa.getCoberturas().get(x).getValExtranjero() :"" ),Color.BLACK,false, "L", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),(drawLines ? black : null));	                     
	                        }
	                        
	                        if (isEndOfPage(table)) {
	                            table.getRows().remove(table.getRows().size() - 1);
	                            table.draw();
	                            page = new PDPage();
	                            document.addPage(page);
	                            setEncabezado(impresionAxa, document, page, true, false);
	                            acumula = false;
	                        } else {
	                            table.draw();
	                            yStart -= table.getHeaderAndDataHeight();
	                        }
	                        if (acumula) {
	                            x++;
	                        }
	                        if (x > 80) {
	                            table.draw();
	                            break;
	                        }
	                    }
	                    
	                    yStart -= (table.getHeaderAndDataHeight() + 2);
	                    
	                    if (impresionAxa.getRamo() == 10) {
	                    	  table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, margin, document, page, false, true);
	                    	  for (int i = 0; i < 10; i++) {
		                    	  if (i >= impresionAxa.getCoberturas().size()) {
		                    		  baseRow = communsPdf.setRow(table, 10);
		                    		  communsPdf.setCell(baseRow, 25,"",Color.BLACK,false, "L", 11, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
		                    	  }
	                    	  }
	                    	  baseRow = communsPdf.setRow(table, 5);
	                    	  communsPdf.setCell(baseRow, 25,"Beneficiarios",Color.BLACK,false, "L", 11, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
	                    	  baseRow = communsPdf.setRow(table, 10);
	                    	  communsPdf.setCell(baseRow, 25,"",Color.BLACK,false, "L", 11, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
	                     	  baseRow = communsPdf.setRow(table, 5);
	                    	  communsPdf.setCell(baseRow, 25,Sio4CommunsPdf.eliminaHtmlTags3(impresionAxa.getAsegurado()),Color.BLACK,false, "L", 11, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
	                    	  table.draw();
	                    }else {
	                  	 table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, margin, document, page, false, true);
	                  	 baseRow = communsPdf.setRow(table, 5);
	                  	 communsPdf.setCell(baseRow, 25,"Asegurados",Color.BLACK,false, "L", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
	                     baseRow = communsPdf.setRow(table);
	                  	 communsPdf.setCell(baseRow, 35,"Nombre",Color.BLACK,true, "C", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
	                  	 communsPdf.setCell(baseRow, 10,"Parentesco",Color.BLACK,true, "C", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
	                  	 communsPdf.setCell(baseRow, 20,"Fecha de Nacimiento",Color.BLACK,true, "C", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
	                	 communsPdf.setCell(baseRow, 8,"Edad",Color.BLACK,true, "C", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
	                	 communsPdf.setCell(baseRow, 8,"Estatus",Color.BLACK,true, "C", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
	                	 communsPdf.setCell(baseRow, 20,"Fecha de Ingreso",Color.BLACK,true, "C", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
	                	 if (isEndOfPage(table)) {
	                            table.getRows().remove(table.getRows().size() - 1);
	                            table.draw();
	                            page = new PDPage();
	                            document.addPage(page);
	                            setEncabezado(impresionAxa, document, page, false, true);
	                            acumula2 = false;
	                        } else {
	                            //table.remoBordes(true);
	                            table.draw();
	                        }
	                    }
	                    
	                    
	                    yStart -= table.getHeaderAndDataHeight();
	                    int t1 =0;
	                    
	                    if (impresionAxa.getAsegurados() != null) {
	                        while (t1 < impresionAxa.getAsegurados().size()) {
	                            acumula2 = true;
	                            table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, margin, document, page, false, true);
	                            baseRow = communsPdf.setRow(table, 11);
	                            communsPdf.setCell(baseRow, 35, Sio4CommunsPdf.eliminaHtmlTags3(impresionAxa.getAsegurados().get(t1).getNombre()), Color.BLACK, false, "L", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f, 5f, 3f, 5f), bgColor);
	                            communsPdf.setCell(baseRow, 10, impresionAxa.getAsegurados().get(t1).getParentesco(), Color.BLACK, false, "C", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f, 5f, 3f, 5f), bgColor);
	                            communsPdf.setCell(baseRow, 20, impresionAxa.getAsegurados().get(t1).getFechNacimiento(), Color.BLACK, false, "C", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f, 5f, 3f, 5f), bgColor);
	                            communsPdf.setCell(baseRow, 8, impresionAxa.getAsegurados().get(t1).getEdad(), Color.BLACK, false, "C", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f, 5f, 3f, 5f), bgColor);
	                            communsPdf.setCell(baseRow, 8, "", Color.BLACK, true, "C", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f, 5f, 3f, 5f), bgColor);
	                            communsPdf.setCell(baseRow, 20, impresionAxa.getAsegurados().get(t1).getFechAntigueda(), Color.BLACK, false, "C", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f, 5f, 3f, 5f), bgColor);
	                            if (isEndOfPage(table)) {
	                                table.getRows().remove(table.getRows().size() - 1);
	                                table.draw();
	                                page = new PDPage();
	                                document.addPage(page);
	                                setEncabezado(impresionAxa, document, page, false, true);
	                                acumula2 = false;

	                            } else {
	                                table.draw();
	                                yStart -= table.getHeaderAndDataHeight();
	                            }

	                            if (acumula2) {
	                                t1++;
	                            }
	                            if (t1 > 150) {
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
					"Ocurrio un error en el servicio ImpresionCredencialPdf: " + ex.getMessage());
		}
		
	}

	private void setEncabezado(ImpresionAxaForm impresionAxa, PDDocument document, PDPage page,Boolean conAsegurados, Boolean conCoberturas) {
		try (PDPageContentStream content = new PDPageContentStream(document, page)) {
			DateFormatSymbols sym = DateFormatSymbols.getInstance(new Locale("es", "MX"));
			sym.setMonths(new String[] { "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto","Septiembre", "Octubre", "Noviembre", "Diciembre" });
			sym.setAmPmStrings(new String[] { "AM", "PM" });
			SimpleDateFormat formatter = new SimpleDateFormat("dd 'de' MMMM 'del' yyyy", sym);
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
	            baseRow = communsPdf.setRow(table, 12);
	            communsPdf.setCell(baseRow, 10,"Dirección",Color.BLACK,true, "L", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);	            
	            communsPdf.setCell(baseRow, 70,impresionAxa.getCteDireccion(),Color.BLACK,false, "L", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
	            if (impresionAxa.getRamo() == 10) {
	            	communsPdf.setCell(baseRow, 20,"COPIA",Color.BLACK,false, "R", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
	            }else {
	            	communsPdf.setCell(baseRow, 20,"ORIGINAL",Color.BLACK,false, "R", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
	            }
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
	            
	            DecimalFormat formateador = new DecimalFormat("#,##0.00");
	            yStart -= (table.getHeaderAndDataHeight() + 10);
	            table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, margin, document, page, false, true);
	            baseRow = communsPdf.setRow(table, 12);
	            communsPdf.setCell(baseRow, 100,"Datos del Asegurado",Color.BLACK,true, "L", 11, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
	            baseRow = communsPdf.setRow(table, 12);
	            communsPdf.setCell(baseRow, 10,"Certificado",Color.BLACK,true, "L", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);	            
	            communsPdf.setCell(baseRow, 47,impresionAxa.getNoCertificado(),Color.BLACK,false, "L", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);	   
	            if(impresionAxa.getPrimas() !=null) {

	             communsPdf.setCell(baseRow, 10,"Prima Neta",Color.BLACK,true, "L", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
	  	         communsPdf.setCell(baseRow, 29,formateador.format(impresionAxa.getPrimas().getPrima()),Color.BLACK,false, "L", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
	  	         communsPdf.setCell(baseRow, 4,"",Color.BLACK,false, "L", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor); 
	            }
	            
	            baseRow = communsPdf.setRow(table, 12);
	            communsPdf.setCell(baseRow, 10,"Nombre",Color.BLACK,true, "L", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);	            
	            communsPdf.setCell(baseRow, 47,Sio4CommunsPdf.eliminaHtmlTags3( impresionAxa.getAsegurado()),Color.BLACK,false, "L", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);	   
	            
	            if(impresionAxa.getPrimas() !=null ) {
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
	                if(impresionAxa.getPrimas() !=null ) {
	   	             communsPdf.setCell(baseRow,29, "Recargo por Pago Fraccionado",Color.BLACK,true, "L", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
	   	  	         communsPdf.setCell(baseRow,10,formateador.format(impresionAxa.getPrimas().getRecargo()),Color.BLACK,false, "L", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
	   	  	         communsPdf.setCell(baseRow, 4,"",Color.BLACK,false, "L", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor); 
	   	            }
	            }
	            
	            baseRow = communsPdf.setRow(table, 12);
	            communsPdf.setCell(baseRow,30, "Fecha de Nacimiento",Color.BLACK,true, "L", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
	  	        communsPdf.setCell(baseRow,15,impresionAxa.getFecNacAseg(),Color.BLACK,false, "L", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
	  	        communsPdf.setCell(baseRow, 7,"Edad",Color.BLACK,true, "L", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor); 
	  	        communsPdf.setCell(baseRow, 4,impresionAxa.getEdadAseg(),Color.BLACK,false, "L", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
	  	        
	  	        if(impresionAxa.getPrimas() !=null) {
	   	             communsPdf.setCell(baseRow,29, "Derecho de Póliza",Color.BLACK,true, "L", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
	   	  	         communsPdf.setCell(baseRow,10,formateador.format(impresionAxa.getPrimas().getDerecho()),Color.BLACK,false, "L", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
	   	  	         communsPdf.setCell(baseRow, 4,"",Color.BLACK,false, "L", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor); 
	   	         } 
	  	        
	  	        baseRow = communsPdf.setRow(table, 12);
	            communsPdf.setCell(baseRow,30, "Fecha de Ingreso a la Póliza",Color.BLACK,true, "L", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
	  	        communsPdf.setCell(baseRow,27,impresionAxa.getFecAltAseg(),Color.BLACK,false, "L", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
	  	      	  	        
	  	        if(impresionAxa.getPrimas() !=null) {
	   	             communsPdf.setCell(baseRow,29, "I.V.A.",Color.BLACK,true, "L", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
	   	  	         communsPdf.setCell(baseRow,10,formateador.format(impresionAxa.getPrimas().getIva()),Color.BLACK,false, "L", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
	   	  	         communsPdf.setCell(baseRow, 4,"",Color.BLACK,false, "L", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor); 
	   	         } 
	  	        
		  	      if (impresionAxa.getRamo() == 10) {
		  	    	  
		  	      }else {
		  	    	 baseRow = communsPdf.setRow(table, 12);
		  	        communsPdf.setCell(baseRow,30, "Fecha de Vencimiento",Color.BLACK,true, "L", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
		  	        communsPdf.setCell(baseRow,27,impresionAxa.getVigenciaA(),Color.BLACK,false, "L", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
		  	      	  	    	    	  	        
		  	        if(impresionAxa.getPrimas() !=null) {
		   	             communsPdf.setCell(baseRow,29, "Prima Total",Color.BLACK,true, "L", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
		   	  	         communsPdf.setCell(baseRow,10,formateador.format(impresionAxa.getPrimas().getPrima()),Color.BLACK,false, "L", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
		   	  	         communsPdf.setCell(baseRow, 4,"",Color.BLACK,false, "L", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor); 
		   	         } 
		  	      }	            	           
	            table.draw();
	            
	            yStart -= (table.getHeaderAndDataHeight() + 10);
	            
	            if (conAsegurados) {
	            	table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, margin, document, page, false, true);
	            	baseRow = communsPdf.setRow(table, 15);
	            	communsPdf.setCell(baseRow,40, "Cobertura",Color.BLACK,true, "L",11, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
	            	communsPdf.setCell(baseRow,30, "Nacional",Color.BLACK,true, "C", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
	            	communsPdf.setCell(baseRow,30, "Extranjero",Color.BLACK,true, "C", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
	            	table.draw();
	                yStart -= (table.getHeaderAndDataHeight());
	            }
	            
	            if (conAsegurados) {
	            	table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, margin, document, page, false, true);
	            	baseRow = communsPdf.setRow(table, 15);
	            	communsPdf.setCell(baseRow,40, "Nombre",Color.BLACK,true, "L",11, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
	            	communsPdf.setCell(baseRow,30, "Parentesco",Color.BLACK,true, "C", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
	            	communsPdf.setCell(baseRow,30, "Nacimiento",Color.BLACK,true, "C", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
	            	communsPdf.setCell(baseRow,30, "Edad",Color.BLACK,true, "C", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
	            	communsPdf.setCell(baseRow,30, "Estatus",Color.BLACK,true, "C", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
	            	communsPdf.setCell(baseRow,30,"Fecha de Ingreso",Color.BLACK,true, "C", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
	            	table.draw();
	                yStart -= (table.getHeaderAndDataHeight());
	            }
	            
	            
	            if (impresionAxa.getRamo() == 10) {
	            	 table = new BaseTable(180, 180, 9, fullWidth, margin, document, page, false, true);
	            	 baseRow = communsPdf.setRow(table, 5);
		             communsPdf.setCell(baseRow,100, "En cumplimiento a lo dispuesto en el artículo 202 de la Ley de Instituciones de Seguros y de Fianzas y la Circular Única de Seguros y Fianzas,",Color.BLACK,true, "L",9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
		             baseRow = communsPdf.setRow(table, 5);
		             communsPdf.setCell(baseRow,100, Sio4CommunsPdf.eliminaHtmlTags3("se ha recibido de <b>AXA Seguros, S.A. de C.V.</b> el producto paquete de seguros denominado <b>PROTECCION IDEAL COLECTIVO</b>, el cual"),Color.BLACK,true, "L",9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
		             baseRow = communsPdf.setRow(table, 5);
		             communsPdf.setCell(baseRow,100,"queda inscrito a partir del día <b>26/02/2019</b> en el registro de esta Comisión, con el número <b>PPAQ-S0048-0075-2018</b>",Color.BLACK,true, "L",9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
		             baseRow = communsPdf.setRow(table, 5);
		             communsPdf.setCell(baseRow,100, "AXA Seguros S.A. de C.V. con domicilio en Av. Félix Cuevas #366 Col Tlacoquemécatl del Valle, Benito Juárez. C.P. 03200, CDMX. Le",Color.BLACK,true, "L",9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);		
		             baseRow = communsPdf.setRow(table, 5);
		             communsPdf.setCell(baseRow,100, "informa que sus datos serán tratados únicamente para los fines del contrato de seguro, Usted podrá conocer ampliamente el Aviso de",Color.BLACK,true, "L",9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
		             baseRow = communsPdf.setRow(table, 5);
		             communsPdf.setCell(baseRow,100, "informa que sus datos serán tratados únicamente para los fines del contrato de seguro, Usted podrá conocer ampliamente el Aviso de",Color.BLACK,true, "L",9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
		             baseRow = communsPdf.setRow(table, 5);
		             communsPdf.setCell(baseRow,100, "Privacidad en axa.mx",Color.BLACK,true, "L",9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);		
		             table.draw();	
		             
		             table = new BaseTable(110, 110, 9, fullWidth, margin, document, page, false, true);
		             baseRow = communsPdf.setRow(table);
		             baseRow = communsPdf.setRow(table, 15);
		             communsPdf.setCell(baseRow,100, "México D.F. a " + impresionAxa.getFecInicioAseg(),Color.BLACK,false, "L",10, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
		             baseRow = communsPdf.setRow(table, 9);
		             communsPdf.setCell(baseRow,100,"Félix Cuevas #366, Piso 3",Color.BLACK,false, "L",10, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
		             baseRow = communsPdf.setRow(table, 9);
		             communsPdf.setCell(baseRow,100, "Col. Tlacoquemécatl",Color.BLACK,false, "L",10, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
		             baseRow = communsPdf.setRow(table, 9);
		             communsPdf.setCell(baseRow,100, "Alcaldía Benito Juárez, C.P. 03200",Color.BLACK,false, "L",10, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
		             baseRow = communsPdf.setRow(table, 9);
		             communsPdf.setCell(baseRow,100, "Ciudad de México",Color.BLACK,false, "L",10, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);		             
		             table.draw();
	            }else {
	                table = new BaseTable(130, 130, 9, fullWidth, margin, document, page, false, true);
	                baseRow = communsPdf.setRow(table);
	                communsPdf.setCell(baseRow,85, "* G.U.A.: Hasta el gasto usual y acostumbrado en el lugar donde reciban los servicios. ** Ver Condiciones. *** Endoso. AXA Seguros, S.A. de C.V. cubre al asegurado por los beneficios contratados en los términos y condiciones de la póliza citada y en los endosos que formen parte de ella." ,Color.BLACK,false, "L",9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3.5f,5f),bgColor);
	                baseRow = communsPdf.setRow(table);
	                baseRow = communsPdf.setRow(table,15);
	                communsPdf.setCell(baseRow,100,"México D.F. a " +  formatter.format(new Date()) ,Color.BLACK,false, "C",10, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
	                baseRow = communsPdf.setRow(table,9);
	                communsPdf.setCell(baseRow,22, "Félix Cuevas #366, Piso 3" ,Color.BLACK,false, "L",10, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
	                baseRow = communsPdf.setRow(table,9);
	                communsPdf.setCell(baseRow,22, "Col. Tlacoquemécatl" ,Color.BLACK,false, "L",10, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
	                baseRow = communsPdf.setRow(table,9);
	                communsPdf.setCell(baseRow,100, "Alcaldía Benito Juárez, C.P. 03200" ,Color.BLACK,false, "L",10, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
	                baseRow = communsPdf.setRow(table,9);
	                communsPdf.setCell(baseRow,22, "Ciudad de México" ,Color.BLACK,false, "L",10, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
	                table.draw();
	            }
	            
	          
	            table = new BaseTable(90, 90, 9, 150, 450, document, page, false, true);
	            baseRow = communsPdf.setRow(table, 9);	          
	            communsPdf.setCell(baseRow, 100, ImageUtils.readImage("https://storage.googleapis.com/quattrocrm-copsis/s32tkk/2207/Polizas/2207/4Avwv9KqPKZ31lIeYtlac4IcQfxQb9bn7ywY43F1ufipY7UgiHHJe4Y6TI4OL/firmaApoderado.jpg").scale(80, 80),  communsPdf.setLineStyle(Color.white), communsPdf.setPadding(0f,40f,-20f,40f), "L", "");
	            baseRow = communsPdf.setRow(table, 9);	        
	            communsPdf.setCell(baseRow,90, "Apoderado" ,Color.BLACK,false, "C",10, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
	            table.draw();

		} catch (Exception ex) {
			ex.printStackTrace();
			throw new GeneralServiceException("00001",
					"Ocurrio un error en el servicio setEncabezado: " + ex.getMessage());
		}

	}
	
	 private boolean isEndOfPage(BaseTable table) {

	        float currentY = yStart - table.getHeaderAndDataHeight();
	        boolean isEndOfPage = currentY <= bottomMargin;
	        return isEndOfPage;
	    }

}
