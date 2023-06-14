package com.copsis.models.impresion;
import java.io.ByteArrayOutputStream;
import java.io.File;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;

import java.awt.Color;
import com.copsis.clients.projections.CaractulaProjection;
import com.copsis.exceptions.GeneralServiceException;
import com.copsis.models.Tabla.BaseTable;
import com.copsis.models.Tabla.Row;
import com.copsis.models.Tabla.Sio4CommunsPdf;

public class ImpresionCaractulaPrudential {
    private final Color azul = new Color(68,114,196, 0);
    private final Color azulb = new Color(217,226,243, 0);
    private float yStartNewPage = 780, yStart = 760, bottomMargin = 32, fullWidth =549,margin =31 ;
    private Sio4CommunsPdf communsPdf = new Sio4CommunsPdf();
    public byte[] buildPDF(CaractulaProjection  caractulaProjection) {

		try {
			ByteArrayOutputStream output;
			try (PDDocument document = new PDDocument()) {
				try {
					PDPage page = new PDPage();
					document.addPage(page);
					BaseTable table;
					Row<PDPage> baseRow;

                        table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, margin, document, page, false, true);
                        baseRow = communsPdf.setRow(table);
                        communsPdf.setCell(baseRow, 100,"PÓLIZA",Color.BLACK,true, "C", 10, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding2(0f,0f,3f,5f),Color.white);
                        baseRow = communsPdf.setRow(table);
                        communsPdf.setCell(baseRow, 100,"Seguro individual – Hospitalización" ,Color.BLACK,true, "C", 10, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding2(0f,5f,3f,5f),Color.white);                   
                        table.draw();
                        yStart -= table.getHeaderAndDataHeight()+8;

                        table = new BaseTable( yStart, yStartNewPage, bottomMargin, 546, 34, document, page, true, true);
                        baseRow = communsPdf.setRow(table,2);
                        communsPdf.setCell(baseRow, 100,"",Color.BLACK,true, "L", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding2(0f,5f,3f,5f),azul);
                        table.draw();                        
                        yStart -= table.getHeaderAndDataHeight()+10;

                        table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, margin, document, page, true, true);
                        baseRow = communsPdf.setRow(table);
                        communsPdf.setCell(baseRow, 100,"DATOS DE LA PÓLIZA",Color.BLACK,true, "L", 10, communsPdf.setLineStyle(Color.white,Color.white,Color.black,Color.black), "", communsPdf.setPadding2(5f,0f,3f,0f),azulb);
                        baseRow = communsPdf.setRow(table);
                        communsPdf.setCell(baseRow, 33,"Número de póliza",Color.BLACK,true, "L", 10, communsPdf.setLineStyle(Color.white,Color.black,Color.white,Color.black), "", communsPdf.setPadding2(5f,0f,3f,0f),Color.white);
                        communsPdf.setCell(baseRow, 33,"Inicio de vigencia de la póliza",Color.BLACK,true, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.white,Color.black), "", communsPdf.setPadding2(5f,0f,3f,0f),Color.white);
                        communsPdf.setCell(baseRow, 34,"Fin de vigencia de la póliza",Color.BLACK,true, "L", 10, communsPdf.setLineStyle(Color.black,Color.white,Color.white,Color.black), "", communsPdf.setPadding2(5f,0f,3f,0f),Color.white);
                        baseRow = communsPdf.setRow(table);
                        communsPdf.setCell(baseRow, 33,"123",Color.BLACK,false, "L", 10, communsPdf.setLineStyle(Color.white,Color.black,Color.black,Color.black), "", communsPdf.setPadding2(5f,0f,3f,0f),Color.white);
                        communsPdf.setCell(baseRow, 33,"",Color.BLACK,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.black), "", communsPdf.setPadding2(5f,0f,3f,0f),Color.white);
                        communsPdf.setCell(baseRow, 34,"",Color.BLACK,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.white,Color.black,Color.black), "", communsPdf.setPadding2(5f,0f,3f,0f),Color.white);
                        baseRow = communsPdf.setRow(table);
                        communsPdf.setCell(baseRow, 33,"Fecha de emisión",Color.BLACK,true, "L", 10, communsPdf.setLineStyle(Color.white,Color.black,Color.white,Color.black), "", communsPdf.setPadding2(5f,0f,3f,0f),Color.white);
                        communsPdf.setCell(baseRow, 33,"Moneda",Color.BLACK,true, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.white,Color.black), "", communsPdf.setPadding2(5f,0f,3f,0f),Color.white);
                        communsPdf.setCell(baseRow, 34,"Forma de pago",Color.BLACK,true, "L", 10, communsPdf.setLineStyle(Color.black,Color.white,Color.white,Color.black), "", communsPdf.setPadding2(5f,0f,3f,0f),Color.white);
                        baseRow = communsPdf.setRow(table);
                        communsPdf.setCell(baseRow, 33,"123",Color.BLACK,false, "L", 10, communsPdf.setLineStyle(Color.white,Color.black,Color.black,Color.black), "", communsPdf.setPadding2(5f,0f,3f,0f),Color.white);
                        communsPdf.setCell(baseRow, 33,"",Color.BLACK,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.black), "", communsPdf.setPadding2(5f,0f,3f,0f),Color.white);
                        communsPdf.setCell(baseRow, 34,"",Color.BLACK,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.white,Color.black,Color.black), "", communsPdf.setPadding2(5f,0f,3f,0f),Color.white);                                                
                        table.setCellCallH(true);
                        table.draw();
                        yStart -= table.getHeaderAndDataHeight()+25;

                        table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, margin, document, page, true, true);
                        baseRow = communsPdf.setRow(table);
                        communsPdf.setCell(baseRow, 100,"DATOS DEL CONTRATANTE",Color.BLACK,true, "L", 10, communsPdf.setLineStyle(Color.white,Color.white,Color.black,Color.black), "", communsPdf.setPadding2(5f,0f,3f,0f),azulb);
                        baseRow = communsPdf.setRow(table);
                        communsPdf.setCell(baseRow, 50,"Nombre o Razón Social",Color.BLACK,true, "L", 10, communsPdf.setLineStyle(Color.white,Color.black,Color.white,Color.black), "", communsPdf.setPadding2(5f,0f,3f,0f),Color.white);
                        communsPdf.setCell(baseRow, 23,"R.F.C.",Color.BLACK,true, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.white,Color.black), "", communsPdf.setPadding2(5f,0f,3f,0f),Color.white);
                        communsPdf.setCell(baseRow, 27,"Teléfono (Incluyendo Lada)",Color.BLACK,true, "L", 10, communsPdf.setLineStyle(Color.black,Color.white,Color.white,Color.black), "", communsPdf.setPadding2(5f,0f,3f,0f),Color.white);
                        baseRow = communsPdf.setRow(table);
                        communsPdf.setCell(baseRow, 50,"123",Color.BLACK,false, "L", 10, communsPdf.setLineStyle(Color.white,Color.black,Color.black,Color.black), "", communsPdf.setPadding2(5f,0f,3f,0f),Color.white);
                        communsPdf.setCell(baseRow, 23,"",Color.BLACK,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.black), "", communsPdf.setPadding2(5f,0f,3f,0f),Color.white);
                        communsPdf.setCell(baseRow, 27,"",Color.BLACK,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.white,Color.black,Color.black), "", communsPdf.setPadding2(5f,0f,3f,0f),Color.white);                        
                        baseRow = communsPdf.setRow(table);
                        communsPdf.setCell(baseRow, 73,"Domicilio (Calle, Número)",Color.BLACK,true, "L", 10, communsPdf.setLineStyle(Color.white,Color.black,Color.white,Color.black), "", communsPdf.setPadding2(5f,0f,3f,0f),Color.white);                        
                        communsPdf.setCell(baseRow, 27,"Colonia",Color.BLACK,true, "L", 10, communsPdf.setLineStyle(Color.black,Color.white,Color.white,Color.black), "", communsPdf.setPadding2(5f,0f,3f,0f),Color.white);
                        baseRow = communsPdf.setRow(table);
                        communsPdf.setCell(baseRow, 73,"123",Color.BLACK,false, "L", 10, communsPdf.setLineStyle(Color.white,Color.black,Color.black,Color.black), "", communsPdf.setPadding2(5f,0f,3f,0f),Color.white);                                           communsPdf.setCell(baseRow, 34,"",Color.BLACK,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.white,Color.black,Color.black), "", communsPdf.setPadding2(5f,0f,3f,0f),Color.white);                                                                                                
                        communsPdf.setCell(baseRow, 27,"12",Color.BLACK,true, "L", 10, communsPdf.setLineStyle(Color.black,Color.white,Color.white,Color.black), "", communsPdf.setPadding2(5f,0f,3f,0f),Color.white);
                        baseRow = communsPdf.setRow(table);
                        communsPdf.setCell(baseRow, 38,"Población",Color.BLACK,true, "L", 10, communsPdf.setLineStyle(Color.white,Color.black,Color.white,Color.black), "", communsPdf.setPadding2(5f,0f,3f,0f),Color.white);
                        communsPdf.setCell(baseRow, 35,"Nacionalidad",Color.BLACK,true, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.white,Color.black), "", communsPdf.setPadding2(5f,0f,3f,0f),Color.white);
                        communsPdf.setCell(baseRow, 27,"Código Postal",Color.BLACK,true, "L", 10, communsPdf.setLineStyle(Color.black,Color.white,Color.white,Color.black), "", communsPdf.setPadding2(5f,0f,3f,0f),Color.white);
                        baseRow = communsPdf.setRow(table);
                        communsPdf.setCell(baseRow, 38,"123",Color.BLACK,false, "L", 10, communsPdf.setLineStyle(Color.white,Color.black,Color.black,Color.black), "", communsPdf.setPadding2(5f,0f,3f,0f),Color.white);
                        communsPdf.setCell(baseRow, 35,"",Color.BLACK,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.black), "", communsPdf.setPadding2(5f,0f,3f,0f),Color.white);
                        communsPdf.setCell(baseRow, 27,"",Color.BLACK,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.white,Color.black,Color.black), "", communsPdf.setPadding2(5f,0f,3f,0f),Color.white);                                                
                        table.setCellCallH(true);
                        table.draw();

                        yStart -= table.getHeaderAndDataHeight()+28;
                        table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, margin, document, page, true, true);
                        baseRow = communsPdf.setRow(table);
                        communsPdf.setCell(baseRow, 100,"COBERTURAS AMPARADAS",Color.BLACK,true, "L", 10, communsPdf.setLineStyle(Color.white,Color.white,Color.black,Color.black), "", communsPdf.setPadding2(5f,0f,3f,0f),azulb);
                        baseRow = communsPdf.setRow(table);
                        communsPdf.setCell(baseRow, 33,"Nombre o Razón Social",Color.BLACK,true, "L", 10, communsPdf.setLineStyle(Color.white,Color.black,Color.white,Color.black), "", communsPdf.setPadding2(5f,0f,3f,0f),Color.white);
                        communsPdf.setCell(baseRow, 33,"R.F.C.",Color.BLACK,true, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.white,Color.black), "", communsPdf.setPadding2(5f,0f,3f,0f),Color.white);
                        communsPdf.setCell(baseRow, 34,"Teléfono (Incluyendo Lada)a",Color.BLACK,true, "L", 10, communsPdf.setLineStyle(Color.black,Color.white,Color.white,Color.black), "", communsPdf.setPadding2(5f,0f,3f,0f),Color.white);
                        baseRow = communsPdf.setRow(table);
                        communsPdf.setCell(baseRow, 33,"123",Color.BLACK,false, "L", 10, communsPdf.setLineStyle(Color.white,Color.black,Color.black,Color.black), "", communsPdf.setPadding2(5f,0f,3f,0f),Color.white);
                        communsPdf.setCell(baseRow, 33,"",Color.BLACK,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.black), "", communsPdf.setPadding2(5f,0f,3f,0f),Color.white);
                        communsPdf.setCell(baseRow, 34,"",Color.BLACK,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.white,Color.black,Color.black), "", communsPdf.setPadding2(5f,0f,3f,0f),Color.white);
                        baseRow = communsPdf.setRow(table);
                        communsPdf.setCell(baseRow, 33,"Fecha de Nacimiento",Color.BLACK,true, "L", 10, communsPdf.setLineStyle(Color.white,Color.black,Color.white,Color.black), "", communsPdf.setPadding2(5f,0f,3f,0f),Color.white);
                        communsPdf.setCell(baseRow, 33,"Edad",Color.BLACK,true, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.white,Color.black), "", communsPdf.setPadding2(5f,0f,3f,0f),Color.white);
                        communsPdf.setCell(baseRow, 34,"Sexo",Color.BLACK,true, "L", 10, communsPdf.setLineStyle(Color.black,Color.white,Color.white,Color.black), "", communsPdf.setPadding2(5f,0f,3f,0f),Color.white);
                        baseRow = communsPdf.setRow(table);
                        communsPdf.setCell(baseRow, 33,"123",Color.BLACK,false, "L", 10, communsPdf.setLineStyle(Color.white,Color.black,Color.black,Color.black), "", communsPdf.setPadding2(5f,0f,3f,0f),Color.white);
                        communsPdf.setCell(baseRow, 33,"",Color.BLACK,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.black), "", communsPdf.setPadding2(5f,0f,3f,0f),Color.white);
                        communsPdf.setCell(baseRow, 34,"",Color.BLACK,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.white,Color.black,Color.black), "", communsPdf.setPadding2(5f,0f,3f,0f),Color.white);                                                
                     
                        table.draw();
					

			                    
					output = new ByteArrayOutputStream();
					document.save(output);
                    document.save(new File("/home/aalbanil/Vídeos/caractula.pdf"));
					return output.toByteArray();
				} finally {
					document.close();
				}

			}

		} catch (Exception ex) {
			
			throw new GeneralServiceException("00001", "Ocurrio un error en el servicio ImpresionInter: "+ ex.getMessage());
			
		}

	}
}
