package com.copsis.models.impresion;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.encryption.AccessPermission;
import org.apache.pdfbox.pdmodel.encryption.StandardProtectionPolicy;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import com.copsis.clients.projections.CertificadoProjection;
import com.copsis.exceptions.GeneralServiceException;
import com.copsis.models.Tabla.BaseTable;
import com.copsis.models.Tabla.ImageUtils;
import com.copsis.models.Tabla.Row;
import com.copsis.models.Tabla.Sio4CommunsPdf;

public class ImpresionCertificadoChubbPdf {
    private final Color gray = new Color(236, 238, 238, 0);
    private final Color gray2 = new Color(193,197, 199, 0);
    private float yStartNewPage = 780, yStart = 690, bottomMargin = 170, fullWidth = 590;
    private Sio4CommunsPdf communsPdf = new Sio4CommunsPdf();
    public byte[] buildPDF(CertificadoProjection certificadoProjection) {
        ByteArrayOutputStream output;
        try {
            try (PDDocument document = new PDDocument()) {
                try {
                    StringBuilder texto = new StringBuilder();
                    PDPage page = new PDPage();
                    document.addPage(page);
                    BaseTable table;
                    Row<PDPage> baseRow;

                    // InputStream input = new URL("https://storage.googleapis.com/quattrocrm-importaciones-prod/poliza-pdf/R0dVcf+2JE1uasiEni&fraslWJ2hCWbfnaGBS9&fraslbOFkXeNZ4=.ttf").openStream();
                    
                    // PDType0Font arial =  PDType0Font.load(document, input);
                    
                 
                   table = new BaseTable(750, yStartNewPage, bottomMargin, fullWidth, 20, document, page, false,true);
                   baseRow = communsPdf.setRow(table, 100);
                   communsPdf.setCell(baseRow,96, ImageUtils.readImage("https://storage.googleapis.com/quattrocrm-copsis/s32tkk/2304/Polizas/2304/7UEtSpacvIfhtgaJEaC7QHVWU4roWaw8a3gVGhmLtHQ9poX7Diywh2hkKKgea/texto.png"));
                   table.draw();


                    texto = new StringBuilder();
                    texto.append("COBERTURA OTORGADA POR / COVERAGE PROVIDED BY: CHUBB SEGUROS MÉXICO, S.A.");
                    PDPageContentStream content01 = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, true);
                    communsPdf.drawBox(content01, Color.black, 25, yStart - 13, 449, 1.6f);
                    content01.close();


                    PDPageContentStream content02 = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, true);
                    communsPdf.drawBox(content02, Color.black, 30, yStart - 23, 550,0.1f);
                    content02.close();
                    
                    
                    table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, 25, document, page, false, true);
					baseRow = communsPdf.setRow(table);
			        communsPdf.setCell(baseRow, 100,texto.toString(),Color.BLACK,true, "L", 10, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding2(0f,5f,3f,5f),Color.white);
                    table.draw();

                    yStart -= (table.getHeaderAndDataHeight() + 7);

                    table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, 25, document, page, false, true);
					baseRow = communsPdf.setRow(table);
			        communsPdf.setCell(baseRow, 19,"No. de Póliza / Policy No.",Color.BLACK,true, "L", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding2(0f,0f,3f,5f),Color.red);
                    communsPdf.setCell(baseRow, 31,certificadoProjection.getNumeroPoliza() ,Color.BLACK,false, "L", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding2(0f,5f,3f,5f),Color.red);
                    communsPdf.setCell(baseRow, 26,"Fecha de emisión / Issuance date:",Color.BLACK,true, "L", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding2(5f,0f,3f,5f),Color.white);
                    communsPdf.setCell(baseRow, 24, certificadoProjection.getFechaEmision(),Color.BLACK,false, "L", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding2(0f,5f,3f,5f),Color.white);
                    baseRow = communsPdf.setRow(table);
			        communsPdf.setCell(baseRow, 27,"Vigencia de la Póliza / Policy Period:",Color.BLACK,true, "L", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding2(0f,0f,3f,5f),Color.red);
                    communsPdf.setCell(baseRow, 73,certificadoProjection.getFechaDesde() +" a "+certificadoProjection.getFechaHasta(),Color.BLACK,false, "L", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding2(0f,0f,3f,5f),Color.red);                
                    table.draw();
                  
                    PDPageContentStream content03 = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, true);
                    communsPdf.drawBox(content03, Color.black, 35, yStart - 15, 548,0.2f);
                    content03.close();

                    yStart -= (table.getHeaderAndDataHeight() );

                    PDPageContentStream content04 = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, true);
                    communsPdf.drawBox(content04, Color.black, 30, yStart+1, 550,0.2f);
                    content04.close();

                    table = new BaseTable(yStart, yStartNewPage, bottomMargin, 550, 32, document, page, true, true);
					baseRow = communsPdf.setRow(table,13);
			        communsPdf.setCell(baseRow, 100,"Datos del Asegurado y datos generales de la póliza / Insured Information & policy general data",Color.BLACK,true, "L", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding2(0f,5f,3f,5f),gray);
                    table.draw();

                    yStart -= (table.getHeaderAndDataHeight() );
                    table = new BaseTable(yStart, yStartNewPage, bottomMargin, 550, 32, document, page, false, true);
                    baseRow = communsPdf.setRow(table,11);
			        communsPdf.setCell(baseRow, 15,"Nombre / Name:",Color.BLACK,true, "L", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding2(0f,5f,3f,5f),Color.white);
                    communsPdf.setCell(baseRow, 35,certificadoProjection.getNombreCompleto(),Color.BLACK,false, "L", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding2(0f,5f,3f,5f),Color.white);
                    communsPdf.setCell(baseRow, 15,"Teléfono / Phone:",Color.BLACK,true, "L", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding2(0f,5f,3f,5f),Color.white);
                    communsPdf.setCell(baseRow, 35,certificadoProjection.getTelefono(),Color.BLACK,false, "L", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding2(0f,5f,3f,5f),Color.white);
                    baseRow = communsPdf.setRow(table,11);
			        communsPdf.setCell(baseRow, 50,"Fecha de Nac / DOB:",Color.BLACK,true, "L", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding2(0f,5f,3f,5f),Color.white);
                    communsPdf.setCell(baseRow, 50,"Sexo / Gender:",Color.BLACK,true, "L", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding2(0f,5f,3f,5f),Color.white);
                 
                    baseRow = communsPdf.setRow(table,11);
			        communsPdf.setCell(baseRow, 17,"Domicilio / Address:",Color.BLACK,true, "L", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding2(0f,0f,3f,5f),Color.white);
                    communsPdf.setCell(baseRow, 83,certificadoProjection.getDomicilio(),Color.BLACK,false, "L", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding2(0f,5f,3f,5f),Color.white);
                 
                    baseRow = communsPdf.setRow(table,11);
			        communsPdf.setCell(baseRow, 50,"Ciudad / City:",Color.BLACK,true, "L", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding2(0f,5f,3f,5f),Color.white);
                    communsPdf.setCell(baseRow, 12,"Estado / State:",Color.BLACK,true, "L", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding2(0f,0f,3f,5f),Color.white);
                    communsPdf.setCell(baseRow, 38,certificadoProjection.getEstado(),Color.BLACK,false, "L", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding2(0f,5f,3f,5f),Color.white);
                
                    baseRow = communsPdf.setRow(table,11);
			        communsPdf.setCell(baseRow, 13,"C.P. / Zip Code:",Color.BLACK,true, "L", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding2(0f,0f,3f,5f),Color.white);
                    communsPdf.setCell(baseRow, 37,certificadoProjection.getCp(),Color.BLACK,false, "L", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding2(0f,5f,3f,5f),Color.white);
                    communsPdf.setCell(baseRow, 50,"R.F.C. / Tax Payer ID:",Color.BLACK,true, "L", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding2(0f,5f,3f,5f),Color.white);
                 
                    baseRow = communsPdf.setRow(table,11);
			        communsPdf.setCell(baseRow, 50,"E-mail:",Color.BLACK,true, "L", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding2(0f,5f,3f,5f),Color.white);
                    communsPdf.setCell(baseRow, 31,"Forma de pago / Payment installments:",Color.BLACK,true, "L", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding2(0f,0f,3f,5f),Color.white);
                    communsPdf.setCell(baseRow,19,certificadoProjection.getFormaPago(),Color.BLACK,false, "L", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding2(0f,5f,3f,5f),Color.white);
                 
                    baseRow = communsPdf.setRow(table,11);
			        communsPdf.setCell(baseRow, 50,"Moneda / Currency:",Color.BLACK,true, "L", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding2(0f,5f,3f,5f),Color.white);
                    communsPdf.setCell(baseRow, 50,"Clave del Agente / Agent code:",Color.BLACK,true, "L", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding2(0f,5f,3f,5f),Color.white);
                    table.draw();
                    yStart -= (table.getHeaderAndDataHeight()+20 );

                    table = new BaseTable(yStart, yStartNewPage, bottomMargin, 550, 32, document, page, true, true);
					baseRow = communsPdf.setRow(table,13);
			        communsPdf.setCell(baseRow, 100,"Descripción del Vehículo / Vehicle Description",Color.BLACK,true, "L", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding2(0f,5f,3f,5f),gray);
                    table.draw();

                    yStart -= (table.getHeaderAndDataHeight()  );
                    table = new BaseTable(yStart, yStartNewPage, bottomMargin, 550, 32, document, page, false, true);
					       
                    baseRow = communsPdf.setRow(table,12);
			        communsPdf.setCell(baseRow, 9,"Año / Year:",Color.BLACK,true, "L", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding2(0f,0f,3f,5f),Color.white);
                    communsPdf.setCell(baseRow, 20,certificadoProjection.getModelo(),Color.BLACK,false, "L", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding2(0f,0f,3f,5f),Color.white);
                    communsPdf.setCell(baseRow, 11,"Marca / Make:",Color.BLACK,true, "L", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding2(0f,0f,3f,5f),Color.white);
                    communsPdf.setCell(baseRow, 25,certificadoProjection.getMarca(),Color.BLACK,false, "L", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding2(0f,5f,3f,5f),Color.white);
                    communsPdf.setCell(baseRow, 13,"Modelo / Model:",Color.BLACK,true, "L", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding2(0f,0f,3f,5f),Color.white);
                    communsPdf.setCell(baseRow, 23,certificadoProjection.getDescripcion(),Color.BLACK,false, "L", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding2(0f,5f,3f,5f),Color.white);
                                        
                    baseRow = communsPdf.setRow(table,12);
			        communsPdf.setCell(baseRow, 9,"Serie / VIN:",Color.BLACK,true, "L", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding2(0f,0f,3f,5f),Color.white);
                    communsPdf.setCell(baseRow, 20,certificadoProjection.getSerie(),Color.BLACK,false, "L", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding2(0f,0f,3f,5f),Color.white);
                    communsPdf.setCell(baseRow, 12,"Placas / Plates:",Color.BLACK,true, "L", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding2(0f,0f,3f,5f),Color.white);
                    communsPdf.setCell(baseRow, 24,certificadoProjection.getPlacas(),Color.BLACK,false, "L", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding2(0f,0f,3f,5f),Color.white);
                    communsPdf.setCell(baseRow, 9,"Uso / Use:",Color.BLACK,true, "L", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding2(0f,0f,3f,5f),Color.white);
                    communsPdf.setCell(baseRow, 20,certificadoProjection.getUso(),Color.BLACK,false, "L", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding2(0f,0f,3f,5f),Color.white);
                    table.draw();
                    yStart -= (table.getHeaderAndDataHeight() +10 );
                    
                    table = new BaseTable(yStart, yStartNewPage, bottomMargin, 550, 32, document, page, true, true);
					baseRow = communsPdf.setRow(table,12);
			        communsPdf.setCell(baseRow, 52,"Coberturas sin deducible / Coverages without deductible",Color.BLACK,true, "L", 9, communsPdf.setLineStyle(gray), "", communsPdf.setPadding2(0f,5f,3f,5f),gray);
                    communsPdf.setCell(baseRow, 33,"Suma Asegurada /Insured Amount",Color.BLACK,true, "C", 9, communsPdf.setLineStyle(gray), "", communsPdf.setPadding2(0f,5f,3f,5f),gray);
                    communsPdf.setCell(baseRow, 15,"Prima / Premium",Color.BLACK,true, "L", 9, communsPdf.setLineStyle(gray), "", communsPdf.setPadding2(0f,5f,3f,5f),gray);
                    table.draw();
                    yStart -= (table.getHeaderAndDataHeight() +2 );

                 
                     


                    table = new BaseTable(yStart, yStartNewPage, bottomMargin, 550, 32, document, page, true, true);
                    baseRow = communsPdf.setRow(table);
			        communsPdf.setCell(baseRow, 52,"RC Daños a Terceros / T.P.L. Bodily Injury & Property Damage",Color.BLACK,false, "L", 9, communsPdf.setLineStyle(gray2), "", communsPdf.setPadding2(0f,0f,3f,5f),Color.white);
                    communsPdf.setCell(baseRow, 33,"$150,000 USD LUC/CSL",Color.BLACK,false, "C", 9, communsPdf.setLineStyle(gray2), "", communsPdf.setPadding2(0f,5f,7f,5f),Color.white);
                    communsPdf.setCell(baseRow, 15,"",Color.BLACK,false, "L", 9, communsPdf.setLineStyle(gray2), "", communsPdf.setPadding2(0f,5f,3f,5f),Color.white);
                    baseRow = communsPdf.setRow(table,22);
                    communsPdf.setCell(baseRow, 52,"Gastos Médicos Ocupantes por persona / por evento Medical Expenses Occupants per person / per event",Color.BLACK,false, "L", 9, communsPdf.setLineStyle(gray2), "", communsPdf.setPadding2(0f,0f,3f,5f),Color.white);
                    communsPdf.setCell(baseRow, 33,"$5,000 USD / $25,000 USD",Color.BLACK,false, "C", 9, communsPdf.setLineStyle(gray2), "", communsPdf.setPadding2(0f,5f,9f,5f),Color.white);
                    communsPdf.setCell(baseRow, 15,"",Color.BLACK,false, "L", 9, communsPdf.setLineStyle(gray2), "", communsPdf.setPadding2(0f,5f,3f,5f),Color.white);
                    baseRow = communsPdf.setRow(table,12);
                    communsPdf.setCell(baseRow, 52,"Extensión RC y G.M.O. / T.P.L. & M.E. Extension",Color.BLACK,false, "L", 9, communsPdf.setLineStyle(gray2), "", communsPdf.setPadding2(0f,0f,3f,5f),Color.white);
                    communsPdf.setCell(baseRow, 33,"AMPARADO/COVERED",Color.BLACK,false, "C", 9, communsPdf.setLineStyle(gray2), "", communsPdf.setPadding2(0f,5f,3f,5f),Color.white);
                    communsPdf.setCell(baseRow, 15,"",Color.BLACK,false, "L", 9, communsPdf.setLineStyle(gray2), "", communsPdf.setPadding2(0f,5f,3f,5f),Color.white);
   
                       baseRow = communsPdf.setRow(table,12);
                    communsPdf.setCell(baseRow, 52,"Indemnización por Muerte al Titular / Death Compensation",Color.BLACK,false, "L", 9, communsPdf.setLineStyle(gray2), "", communsPdf.setPadding2(0f,0f,3f,5f),Color.white);
                    communsPdf.setCell(baseRow, 33,"$5,000 USD",Color.BLACK,false, "C", 9, communsPdf.setLineStyle(gray2), "", communsPdf.setPadding2(5f,5f,3f,5f),Color.white);
                    communsPdf.setCell(baseRow, 15,"",Color.BLACK,false, "L", 9, communsPdf.setLineStyle(gray2), "", communsPdf.setPadding2(5f,5f,3f,5f),Color.white);
                    
                    baseRow = communsPdf.setRow(table,10);
                    communsPdf.setCell(baseRow, 52,"Servicios de Asistencia / Roadside Assistance",Color.BLACK,false, "L", 9, communsPdf.setLineStyle(gray2), "", communsPdf.setPadding2(0f,0f,3f,5f),Color.white);
                    communsPdf.setCell(baseRow, 33,"INCLUIDO/INCLUDED",Color.BLACK,false, "C", 9, communsPdf.setLineStyle(gray2), "", communsPdf.setPadding2(5f,5f,3f,5f),Color.white);
                    communsPdf.setCell(baseRow, 15,"",Color.BLACK,false, "L", 9, communsPdf.setLineStyle(gray2), "", communsPdf.setPadding2(5f,5f,3f,5f),Color.white);
                    
                    baseRow = communsPdf.setRow(table,10);
                    communsPdf.setCell(baseRow, 52,"Asistencia Legal / Legal Assistance",Color.BLACK,false, "L", 9, communsPdf.setLineStyle(gray2), "", communsPdf.setPadding2(0f,0f,3f,5f),Color.white);
                    communsPdf.setCell(baseRow, 33,"INCLUIDO/INCLUDED",Color.BLACK,false, "C", 9, communsPdf.setLineStyle(gray2), "", communsPdf.setPadding2(5f,5f,3f,5f),Color.white);
                    communsPdf.setCell(baseRow, 15,"",Color.BLACK,false, "L", 9, communsPdf.setLineStyle(gray2), "", communsPdf.setPadding2(5f,5f,3f,5f),Color.white);
                    
                    baseRow = communsPdf.setRow(table,10);
                    communsPdf.setCell(baseRow, 52,"Beneficios en Viaje / Travel Benefits",Color.BLACK,false, "L", 9, communsPdf.setLineStyle(gray2), "", communsPdf.setPadding2(0f,0f,3f,5f),Color.white);
                    communsPdf.setCell(baseRow, 33,"INCLUIDO/INCLUDED",Color.BLACK,false, "C", 9, communsPdf.setLineStyle(gray2), "", communsPdf.setPadding2(5f,5f,3f,5f),Color.white);
                    communsPdf.setCell(baseRow, 15,"",Color.BLACK,false, "L", 9, communsPdf.setLineStyle(gray2), "", communsPdf.setPadding2(5f,5f,3f,5f),Color.white);
                    


                    table.draw();

                    yStart -= (table.getHeaderAndDataHeight()+7 );
                    table = new BaseTable(yStart, yStartNewPage, bottomMargin,286, 32, document, page, true, true);
					baseRow = communsPdf.setRow(table,12);
			        communsPdf.setCell(baseRow, 100,"Conductores Adicionales / Aditional Drivers",Color.BLACK,true, "L", 10, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding2(0f,0f,2f,5f),gray);
                    table.draw();
                    yStart -= (table.getHeaderAndDataHeight()+1 );                    
             
       
                    
                    table = new BaseTable(yStart, yStartNewPage, bottomMargin, 285, 32, document, page, true, true);
                    baseRow = communsPdf.setRow(table,51);
			        communsPdf.setCell(baseRow, 100,"",Color.BLACK,false, "L", 10, communsPdf.setLineStyle(gray2), "", communsPdf.setPadding2(0f,0f,3f,5f),Color.white);
                    table.draw();

                    table = new BaseTable(yStart, yStartNewPage, bottomMargin, 266, 317, document, page, true, true);
                    baseRow = communsPdf.setRow(table,12);
			        communsPdf.setCell(baseRow, 69,"Prima Neta / Net Premium:",Color.BLACK,false, "L", 10, communsPdf.setLineStyle(gray2), "", communsPdf.setPadding2(0f,0f,3f,5f),Color.white).setLeftPadding(25);
                    communsPdf.setCell(baseRow, 31,"",Color.BLACK,false, "C", 10, communsPdf.setLineStyle(gray2), "", communsPdf.setPadding2(5f,0f,3f,5f),Color.white);               
                    baseRow = communsPdf.setRow(table,12);
			        communsPdf.setCell(baseRow, 69,"Gastos de expedición / Policy Fee:",Color.BLACK,false, "L", 10, communsPdf.setLineStyle(gray2), "", communsPdf.setPadding2(0f,0f,3f,5f),Color.white).setLeftPadding(25);
                    communsPdf.setCell(baseRow, 31,"",Color.BLACK,false, "C", 10, communsPdf.setLineStyle(gray2), "", communsPdf.setPadding2(5f,0f,3f,5f),Color.white);               
                    baseRow = communsPdf.setRow(table,12);
			        communsPdf.setCell(baseRow, 69,"I.V.A. / Mex. Tax",Color.BLACK,false, "L", 10, communsPdf.setLineStyle(gray2), "", communsPdf.setPadding2(0f,0f,3f,5f),Color.white).setLeftPadding(25);
                    communsPdf.setCell(baseRow, 31,"",Color.BLACK,false, "C", 10, communsPdf.setLineStyle(gray2), "", communsPdf.setPadding2(5f,0f,3f,5f),Color.white);               
                    baseRow = communsPdf.setRow(table,12);
			        communsPdf.setCell(baseRow, 69,"Prima Total /Total Premium:",Color.BLACK,false, "L", 10, communsPdf.setLineStyle(gray2), "", communsPdf.setPadding2(0f,0f,3f,5f),Color.white).setLeftPadding(25);
                    communsPdf.setCell(baseRow, 31,"",Color.BLACK,false, "C", 10, communsPdf.setLineStyle(gray2), "", communsPdf.setPadding2(0f,0f,3f,5f),Color.white);                               
                    table.draw();
                    
                    Float  tb=450f;
                    texto = new StringBuilder();
                    texto.append("Cualquier conductor mayor de 18 años con licencia vigente y");
       
                    this.parrafo(document, page, this.medidas(page.getMediaBox(), 32f, tb), Sio4CommunsPdf.eliminaHtmlTags3(texto.toString()), 270, PDType1Font.HELVETICA, 9.5f, (-1.2f * 9f), 1f,0.4f);
                    texto = new StringBuilder();
                    texto.append("autorizado por el asegurado está amparado en esta póliza / Any");
                    tb =tb+12;
                    this.parrafo(document, page, this.medidas(page.getMediaBox(), 32f, tb), Sio4CommunsPdf.eliminaHtmlTags3(texto.toString()), 270, PDType1Font.HELVETICA, 9.5f, (-1.2f * 9f), 1f,0.2f);
                    tb =tb+12;
                    texto = new StringBuilder();
                    texto.append("driver over 18 years old, with valid driver license and authorized");
                    this.parrafo(document, page, this.medidas(page.getMediaBox(), 32f, tb), Sio4CommunsPdf.eliminaHtmlTags3(texto.toString()), 270, PDType1Font.HELVETICA, 9.5f, (-1.2f * 9f), 1f,0.2f);                   
                    tb =tb+12;
                    texto = new StringBuilder();
                    texto.append("by the insured is covered by this Policy.");
                    this.parrafo(document, page, this.medidas(page.getMediaBox(), 32f, tb), Sio4CommunsPdf.eliminaHtmlTags3(texto.toString()), 270, PDType1Font.HELVETICA, 9.5f, (-1.2f * 9f), 1f,0.2f);

                    tb =tb+16;
                    texto = new StringBuilder();
                    texto.append("La presente póliza de Auto de Responsabilidad Civil en E.U.A. y Canadá tendrá validez si la póliza de");
                    this.parrafo(document, page, this.medidas(page.getMediaBox(), 32f, tb), Sio4CommunsPdf.eliminaHtmlTags3(texto.toString()), 550, PDType1Font.HELVETICA, 9.5f, (-1.2f * 9f), 1f,0.2f);
                    texto = new StringBuilder();
                    texto.append("AXA Seguros S.A. de");
                    this.parrafo(document, page, this.medidas(page.getMediaBox(), 482f, tb), Sio4CommunsPdf.eliminaHtmlTags3(texto.toString()), 550, PDType1Font.HELVETICA_BOLD, 9.5f, (-1.2f * 9f), 1f,0.2f);

                    tb =tb+12;
                    texto = new StringBuilder();
                    texto.append("C.V.");
                    this.parrafo(document, page, this.medidas(page.getMediaBox(), 32f, tb), Sio4CommunsPdf.eliminaHtmlTags3(texto.toString()), 550, PDType1Font.HELVETICA_BOLD, 9.5f, (-1.2f * 9f), 1f,0.2f);
                    texto = new StringBuilder();
                    texto.append("de auto residente ligada a ésta se encuentra vigente, cubre sólo vehículos registrados en México y garantiza cubrir los");
                    this.parrafo(document, page, this.medidas(page.getMediaBox(), 58f, tb), Sio4CommunsPdf.eliminaHtmlTags3(texto.toString()), 550, PDType1Font.HELVETICA, 9.5f, (-1.2f * 9f), 1f,0.2f);
                     
                    tb =tb+12;
                    texto = new StringBuilder();
                    texto.append("límites mínimos de Responsabilidad Civil por Daños a Terceros en sus Bienes y en sus Personas requeridos en los E.U.A y ");
                    texto.append("Canadá. / This Auto Third Party Liability policy will be in force if the related");
                    this.parrafo(document, page, this.medidas(page.getMediaBox(), 32f, tb), Sio4CommunsPdf.eliminaHtmlTags3(texto.toString()), 550, PDType1Font.HELVETICA, 9.5f, (-1.3f * 9f), 1f,0.5f);
                    tb =tb+12;
                    texto = new StringBuilder();
                    texto.append("AXA Seguros, S.A. de C.V.");                    
                    this.parrafo(document, page, this.medidas(page.getMediaBox(), 386f, tb), Sio4CommunsPdf.eliminaHtmlTags3(texto.toString()), 550, PDType1Font.HELVETICA_BOLD, 9.5f, (-1.4f * 9f), 1f,0.5f);
                    texto = new StringBuilder();
                    texto.append("mexican auto");                    
                    this.parrafo(document, page, this.medidas(page.getMediaBox(), 522f, tb), Sio4CommunsPdf.eliminaHtmlTags3(texto.toString()), 550, PDType1Font.HELVETICA, 9.5f, (-1.3f * 9f), 1f,0.2f);
                
                    tb =tb+12;
                    texto = new StringBuilder();
                    texto.append("insurance policy is in force, it covers only Mexican plated and registered vehicles and guarantees to cover the Minimum limits ");
                    texto.append("for Bodily Injury and Property Damage Liability, required in the USA and Canada.");
                    this.parrafo(document, page, this.medidas(page.getMediaBox(), 32f, tb), Sio4CommunsPdf.eliminaHtmlTags3(texto.toString()), 550, PDType1Font.HELVETICA, 9.5f, (-1.3f * 9f), 1f,0.2f);
                     
                    tb =tb+29;
                    texto = new StringBuilder();
                    texto.append("Chubb Seguros México, S.A. está autorizado y reconocido en Estados Unidos de Norteamérica por la National ");
                    texto.append("Association of Insurance Commissioners (NAIC:AA2730007) y en Canadá por el Canadian Council of Insurance ");
                    texto.append("Regulators (CCIR)/Chubb Seguros México, S.A. is authorized and recognized in the United States of America by the ");
                    texto.append("National Association of Insurance Commissioners (NAIC:AA2730007) and in Canada by the Canadian Council of ");
                    texto.append("Insurance Regulators (CCIR).");
                    this.parrafo(document, page, this.medidas(page.getMediaBox(), 32f, tb), Sio4CommunsPdf.eliminaHtmlTags3(texto.toString()), 550, PDType1Font.HELVETICA_BOLD, 9.5f, (-1.3f * 9f), 1f,0.2f);

                    tb =tb+65;
                    PDPageContentStream content05 = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, true);
                    communsPdf.drawBox(content05, Color.black, 33, 158, 545,0.2f);
                    content05.close();
                    texto = new StringBuilder();
                    texto.append("En cumplimiento a lo dispuesto en el artículo 202 de la Ley de Instituciones de Seguros y de Fianzas, la ");
                    texto.append("documentación contractual y la nota técnica que integran este producto de seguro, quedaron registradas ");
                    texto.append("ante la Comisión Nacional de Seguros y Fianzas, a partir del día 1 de Octubre de 2018, con el número ");
                    texto.append("CNSF-S0039-0490-2018 / CONDUSEF-003931-03.");
                    this.parrafo(document, page, this.medidas(page.getMediaBox(), 32f, tb), Sio4CommunsPdf.eliminaHtmlTags3(texto.toString()), 547, PDType1Font.HELVETICA, 11.2f, (-1.6f * 9f),1f,0.2f);


                  
                    table = new BaseTable(70, yStartNewPage, 0, 550, 32, document, page, true, true);
                    baseRow = communsPdf.setRow(table,12);
			        communsPdf.setCell(baseRow, 33,"Reporte de siniestro:",Color.BLACK,true, "L", 9, communsPdf.setLineStyle(gray2), "", communsPdf.setPadding2(2f,0f,3f,5f),gray);
                    communsPdf.setCell(baseRow, 33,"Solicitar una grúa o asistencia:",Color.BLACK,true, "L", 9, communsPdf.setLineStyle(gray2), "", communsPdf.setPadding2(2f,5f,3f,5f),gray);
                    communsPdf.setCell(baseRow, 34,"Solicitar ID Card para Canadá:",Color.BLACK,true, "L", 9, communsPdf.setLineStyle(gray2), "", communsPdf.setPadding2(2f,5f,3f,5f),gray);
                    texto = new StringBuilder();
                    texto.append("Llamando desde USA: 1-855-278-4082\n");
                    texto.append("Llamando desde México:\n");
                    texto.append("001-503-747-1842");

                    baseRow = communsPdf.setRow(table,40);
                    baseRow.setLineSpacing(1.5f);
			        communsPdf.setCell(baseRow, 33,Sio4CommunsPdf.eliminaHtmlTags3(texto.toString()),Color.BLACK,false, "L", 10, communsPdf.setLineStyle(gray2), "", communsPdf.setPadding2(2f,0f,3f,5f),Color.white);
                    texto = new StringBuilder();
                    texto.append("Llamando desde USA: 1-855-278-4825\n");
                    texto.append("Llamando desde México:\n");
                    texto.append("001-882-730-8622");
                    communsPdf.setCell(baseRow, 33,Sio4CommunsPdf.eliminaHtmlTags3(texto.toString()),Color.BLACK,false, "L", 10, communsPdf.setLineStyle(gray2), "", communsPdf.setPadding2(2f,5f,3f,5f),Color.white);
                    texto = new StringBuilder();
                    texto.append("Llamando desde USA: 1-877-730-8622\n");
                    texto.append("Llamando desde México:\n");
                    texto.append("800-467-3031");
                    communsPdf.setCell(baseRow, 34,Sio4CommunsPdf.eliminaHtmlTags3(texto.toString()),Color.BLACK,false, "L", 10, communsPdf.setLineStyle(gray2), "", communsPdf.setPadding2(2f,5f,3f,5f),Color.white);                
                    table.draw();

                    output = new ByteArrayOutputStream();
                     AccessPermission accessPermission = new AccessPermission();
                    
                   
                        if(!certificadoProjection.getSerie().isEmpty() && certificadoProjection.getPdfEncrypted() == null){
                            System.out.println( certificadoProjection.getPdfEncrypted());
                            String password =certificadoProjection.getSerie().substring(certificadoProjection.getSerie().length()-4, certificadoProjection.getSerie().length());                   
                            StandardProtectionPolicy spp = new StandardProtectionPolicy(password,password,accessPermission);
                            spp.setEncryptionKeyLength(128);
                            spp.setPermissions(accessPermission);
                            document.protect(spp); 
                         }
                     
                    
                                 
                    document.save(output);

                    return output.toByteArray();
                } finally {
                    document.close();
                }

            }
        } catch (Exception ex) {
            throw new GeneralServiceException("00001",
                    "Ocurrio un error en el servicio ImpresionCertificadoChubbPdf: " + ex.getMessage());
        }
    }


    private static void addParagraph(PDPageContentStream contentStream, float width, float sx,
            float sy, String text, boolean justify, PDFont FONT, Float FONT_SIZE, Float LEADING,Float SPACING) throws IOException {
        List<String> lines = parseLines(text, width, FONT, FONT_SIZE, LEADING);
        contentStream.setFont(FONT, FONT_SIZE);
        contentStream.newLineAtOffset(sx, sy);
        for (String line : lines) {
            float charSpacing = SPACING;
            if (justify) {
                if (line.length() > 1) {
                    float size = FONT_SIZE * FONT.getStringWidth(line) / 1000;
                    float free = width - size;
                    if (free > 0 && !lines.get(lines.size() - 1).equals(line)) {
                        charSpacing = free / (line.length() - 1);
                    }
                }
            }
            
            contentStream.setCharacterSpacing(charSpacing);
            contentStream.showText(line);
            contentStream.newLineAtOffset(0, LEADING);
            
        }
    }

    private static List<String> parseLines(String text, float width, PDFont FONT, Float FONT_SIZE, Float LEADING)
            throws IOException {
        List<String> lines = new ArrayList<String>();

        int lastSpace = -1;
        while (text.length() > 0) {
            int spaceIndex = text.indexOf(' ', lastSpace + 1);
            if (spaceIndex < 0) {
                spaceIndex = text.length();
            }
            String subString = text.substring(0, spaceIndex);
            float size = FONT_SIZE * FONT.getStringWidth(subString) / 1000;
            if (size > width) {
                if (lastSpace < 0) {
                    lastSpace = spaceIndex;
                }
                subString = text.substring(0, lastSpace);
                lines.add(subString);
                text = text.substring(lastSpace).trim();
                lastSpace = -1;
            } else if (spaceIndex == text.length()) {
                lines.add(text);
                text = "";
            } else {
                lastSpace = spaceIndex;
            }
        }
        return lines;
    }

    public List<Float> medidas(PDRectangle mediaBox, Float marginX, Float marginY) {
        List<Float> lines = new ArrayList<Float>();

        lines.add(mediaBox.getWidth() - 2 * marginX);// with
        lines.add(mediaBox.getLowerLeftX() + marginX);// x
        lines.add(mediaBox.getUpperRightY() - marginY); // y

        return lines;
    }

    public PDPageContentStream parrafo(PDDocument document, PDPage page, List<Float> lines, String tetxo, int i,
            PDFont FONT, Float FONT_SIZE, Float LEADING, Float paddig,float SPACING) {
        try {
            PDPageContentStream contentStream = new PDPageContentStream(document, page,
                    PDPageContentStream.AppendMode.APPEND, true);
            contentStream.beginText();
            addParagraph(contentStream, i, lines.get(1) + paddig, lines.get(2), tetxo, true, FONT, FONT_SIZE,
                    LEADING,SPACING);
            contentStream.endText();
            contentStream.close();

            return contentStream;
        } catch (IOException e) {
            return null;
        }
    }

}
