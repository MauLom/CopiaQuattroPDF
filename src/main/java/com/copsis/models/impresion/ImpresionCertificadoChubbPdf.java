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
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import com.copsis.clients.projections.CertificadoProjection;
import com.copsis.clients.projections.CoberturaProjection;
import com.copsis.exceptions.GeneralServiceException;
import com.copsis.models.Tabla.BaseTable;
import com.copsis.models.Tabla.Row;
import com.copsis.models.Tabla.Sio4CommunsPdf;
import com.fasterxml.jackson.databind.ObjectMapper;


public class ImpresionCertificadoChubbPdf {
    private final Color gray = new Color(236, 238, 238, 0);
    private float margin = 10, yStartNewPage = 780, yStart = 720, bottomMargin = 170, fullWidth = 590;
    private Sio4CommunsPdf communsPdf = new Sio4CommunsPdf();
    private float marginY = 0;
    private float marginX = 20;
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

                     texto.append("PÓLIZA DE SEGURO DE RESPONSABILIDAD CIVIL PARA AUTOS TURISTAS CON PLACAS MEXICANAS EN E.U.A. Y CANADÁ - AUTO");
                    // marginY = page.getBBox().getHeight() - yStart + 10;                
                    // this.parrafo(document, page, this.medidas(page.getMediaBox(), marginX, marginY), texto.toString(), 560, PDType1Font.HELVETICA_BOLD, 9.5f, (-1.2f * 9.5f), 0f);

                    texto = new StringBuilder();
                    texto.append("COBERTURA OTORGADA POR / COVERAGE PROVIDED BY: CHUBB SEGUROS MÉXICO, S.A.");
                    
                    table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, margin+5, document, page, false, true);
					baseRow = communsPdf.setRow(table);
			        communsPdf.setCell(baseRow, 100,texto.toString(),Color.BLACK,true, "L", 10, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),Color.white);
                    table.draw();

                    yStart -= (table.getHeaderAndDataHeight() + 10);

                    table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, margin+5, document, page, false, true);
					baseRow = communsPdf.setRow(table);
			        communsPdf.setCell(baseRow, 50,"No. de Póliza / Policy No.",Color.BLACK,true, "L", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),Color.white);
                    communsPdf.setCell(baseRow, 50,"Fecha de emisión / Issuance date:",Color.BLACK,true, "L", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),Color.white);
                    baseRow = communsPdf.setRow(table);
			        communsPdf.setCell(baseRow, 28,"Vigencia de la Póliza / Policy Period:",Color.BLACK,true, "L", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(0f,0f,3f,5f),Color.red);
                    communsPdf.setCell(baseRow, 72,"MMM/DD/YYYY 00:00hrs a MMM/DD/YYYY 00:00hrs",Color.BLACK,true, "L", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(0f,0f,3f,5f),Color.red);
                
                    table.draw();

                    yStart -= (table.getHeaderAndDataHeight() );

                    table = new BaseTable(yStart, yStartNewPage, bottomMargin, 550, margin+15, document, page, true, true);
					baseRow = communsPdf.setRow(table);
			        communsPdf.setCell(baseRow, 100,"Datos del Asegurado y datos generales de la póliza / Insured Information & policy general data",Color.BLACK,true, "L", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),gray);
                    table.draw();
                    yStart -= (table.getHeaderAndDataHeight() );
                    table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, margin+15, document, page, false, true);
                    baseRow = communsPdf.setRow(table);
			        communsPdf.setCell(baseRow, 50,"Nombre / Name:",Color.BLACK,true, "L", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),Color.white);
                    communsPdf.setCell(baseRow, 50,"Teléfono / Phone:",Color.BLACK,true, "L", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),Color.white);
                    baseRow = communsPdf.setRow(table);
			        communsPdf.setCell(baseRow, 50,"Fecha de Nac / DOB:",Color.BLACK,true, "L", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),Color.white);
                    communsPdf.setCell(baseRow, 50,"Sexo / Gender:",Color.BLACK,true, "L", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),Color.white);
                 
                    baseRow = communsPdf.setRow(table);
			        communsPdf.setCell(baseRow, 50,"Domicilio / Address:",Color.BLACK,true, "L", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),Color.white);
                    communsPdf.setCell(baseRow, 50,"",Color.BLACK,true, "L", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),Color.white);
                 
                    baseRow = communsPdf.setRow(table);
			        communsPdf.setCell(baseRow, 50,"Nombre / Name:",Color.BLACK,true, "L", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),Color.white);
                    communsPdf.setCell(baseRow, 50,"Teléfono / Phone:",Color.BLACK,true, "L", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),Color.white);
                 
                    baseRow = communsPdf.setRow(table);
			        communsPdf.setCell(baseRow, 50,"Ciudad / City:",Color.BLACK,true, "L", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),Color.white);
                    communsPdf.setCell(baseRow, 50,"Estado / State:",Color.BLACK,true, "L", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),Color.white);
                 
                    baseRow = communsPdf.setRow(table);
			        communsPdf.setCell(baseRow, 50,"C.P. / Zip Code:",Color.BLACK,true, "L", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),Color.white);
                    communsPdf.setCell(baseRow, 50,"R.F.C. / Tax Payer ID:",Color.BLACK,true, "L", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),Color.white);
                 
                    baseRow = communsPdf.setRow(table);
			        communsPdf.setCell(baseRow, 50,"E-mail:",Color.BLACK,true, "L", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),Color.white);
                    communsPdf.setCell(baseRow, 50,"Forma de pago / Payment installments:",Color.BLACK,true, "L", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),Color.white);
                 
                    baseRow = communsPdf.setRow(table);
			        communsPdf.setCell(baseRow, 50,"Moneda / Currency:",Color.BLACK,true, "L", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),Color.white);
                    communsPdf.setCell(baseRow, 50,"Clave del Agente / Agent code:",Color.BLACK,true, "L", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),Color.white);
                    table.draw();
                    yStart -= (table.getHeaderAndDataHeight()+20 );

                    table = new BaseTable(yStart, yStartNewPage, bottomMargin, 550, margin+15, document, page, true, true);
					baseRow = communsPdf.setRow(table);
			        communsPdf.setCell(baseRow, 100,"Descripción del Vehículo / Vehicle Description",Color.BLACK,true, "L", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),gray);
                    table.draw();

                    yStart -= (table.getHeaderAndDataHeight() +5 );
                    table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, margin+15, document, page, false, true);
					       
                    baseRow = communsPdf.setRow(table);
			        communsPdf.setCell(baseRow, 28,"Año / Year:",Color.BLACK,true, "L", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),Color.white);
                    communsPdf.setCell(baseRow, 36,"Marca / Make:",Color.BLACK,true, "L", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),Color.white);
                    communsPdf.setCell(baseRow, 30,"Modelo / Model:",Color.BLACK,true, "L", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),Color.white);
                    
                                        
                    baseRow = communsPdf.setRow(table);
			        communsPdf.setCell(baseRow, 28,"Serie / VIN:",Color.BLACK,true, "L", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),Color.white);
                    communsPdf.setCell(baseRow, 36,"Placas / Plates:",Color.BLACK,true, "L", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),Color.white);
                    communsPdf.setCell(baseRow, 30,"Uso / Use:",Color.BLACK,true, "L", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),Color.white);
			        table.draw();
                    yStart -= (table.getHeaderAndDataHeight() +5 );
                    table = new BaseTable(yStart, yStartNewPage, bottomMargin, 550, margin+15, document, page, true, true);
					baseRow = communsPdf.setRow(table);
			        communsPdf.setCell(baseRow, 100,"Coberturas sin deducible / Coverages without deductible Suma Asegurada/Insured Amount",Color.BLACK,true, "L", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),gray);
                    table.draw();


                 
                     


                    table = new BaseTable(yStart, yStartNewPage, bottomMargin, 550, margin+15, document, page, true, true);
                    baseRow = communsPdf.setRow(table);
			        communsPdf.setCell(baseRow, 50,"RC Daños a Terceros / T.P.L. Bodily Injury & Property Damage",Color.BLACK,false, "L", 9, communsPdf.setLineStyle(Color.gray), "", communsPdf.setPadding(5f,0f,3f,5f),Color.white);
                    communsPdf.setCell(baseRow, 30,"$150,000 USD LUC/CSL:",Color.BLACK,false, "L", 9, communsPdf.setLineStyle(Color.gray), "", communsPdf.setPadding(5f,5f,3f,5f),Color.white);
                    communsPdf.setCell(baseRow, 20,"",Color.BLACK,false, "L", 9, communsPdf.setLineStyle(Color.gray), "", communsPdf.setPadding(5f,5f,3f,5f),Color.white);
                    baseRow = communsPdf.setRow(table,22);
                    communsPdf.setCell(baseRow, 50,"Gastos Médicos Ocupantes por persona / por evento Medical Expenses Occupants per person / per event",Color.BLACK,false, "L", 9, communsPdf.setLineStyle(Color.gray), "", communsPdf.setPadding(5f,0f,3f,5f),Color.white);
                    communsPdf.setCell(baseRow, 30,"$5,000 USD / $25,000 USD",Color.BLACK,false, "L", 9, communsPdf.setLineStyle(Color.gray), "", communsPdf.setPadding(5f,5f,3f,5f),Color.white);
                    communsPdf.setCell(baseRow, 20,"",Color.BLACK,false, "L", 9, communsPdf.setLineStyle(Color.gray), "", communsPdf.setPadding(5f,5f,3f,5f),Color.white);
                    baseRow = communsPdf.setRow(table,22);
                    communsPdf.setCell(baseRow, 50,"Extensión RC y G.M.O. / T.P.L. & M.E. Extension",Color.BLACK,false, "L", 9, communsPdf.setLineStyle(Color.gray), "", communsPdf.setPadding(5f,0f,3f,5f),Color.white);
                    communsPdf.setCell(baseRow, 30,"AMPARADO/COVERED",Color.BLACK,false, "L", 9, communsPdf.setLineStyle(Color.gray), "", communsPdf.setPadding(5f,5f,3f,5f),Color.white);
                    communsPdf.setCell(baseRow, 20,"",Color.BLACK,false, "L", 9, communsPdf.setLineStyle(Color.gray), "", communsPdf.setPadding(5f,5f,3f,5f),Color.white);
   
                       baseRow = communsPdf.setRow(table,22);
                    communsPdf.setCell(baseRow, 50,"Indemnización por Muerte al Titular / Death Compensation",Color.BLACK,false, "L", 9, communsPdf.setLineStyle(Color.gray), "", communsPdf.setPadding(5f,0f,3f,5f),Color.white);
                    communsPdf.setCell(baseRow, 30,"$5,000 USD",Color.BLACK,false, "L", 9, communsPdf.setLineStyle(Color.gray), "", communsPdf.setPadding(5f,5f,3f,5f),Color.white);
                    communsPdf.setCell(baseRow, 20,"",Color.BLACK,false, "L", 9, communsPdf.setLineStyle(Color.gray), "", communsPdf.setPadding(5f,5f,3f,5f),Color.white);
                    
                    baseRow = communsPdf.setRow(table,22);
                    communsPdf.setCell(baseRow, 50,"Servicios de Asistencia / Roadside Assistance",Color.BLACK,false, "L", 9, communsPdf.setLineStyle(Color.gray), "", communsPdf.setPadding(5f,0f,3f,5f),Color.white);
                    communsPdf.setCell(baseRow, 30,"INCLUIDO/INCLUDED",Color.BLACK,false, "L", 9, communsPdf.setLineStyle(Color.gray), "", communsPdf.setPadding(5f,5f,3f,5f),Color.white);
                    communsPdf.setCell(baseRow, 20,"",Color.BLACK,false, "L", 9, communsPdf.setLineStyle(Color.gray), "", communsPdf.setPadding(5f,5f,3f,5f),Color.white);
                    
                    baseRow = communsPdf.setRow(table,22);
                    communsPdf.setCell(baseRow, 50,"Asistencia Legal / Legal Assistance",Color.BLACK,false, "L", 9, communsPdf.setLineStyle(Color.gray), "", communsPdf.setPadding(5f,0f,3f,5f),Color.white);
                    communsPdf.setCell(baseRow, 30,"INCLUIDO/INCLUDED",Color.BLACK,false, "L", 9, communsPdf.setLineStyle(Color.gray), "", communsPdf.setPadding(5f,5f,3f,5f),Color.white);
                    communsPdf.setCell(baseRow, 20,"",Color.BLACK,false, "L", 9, communsPdf.setLineStyle(Color.gray), "", communsPdf.setPadding(5f,5f,3f,5f),Color.white);
                    
                    baseRow = communsPdf.setRow(table,22);
                    communsPdf.setCell(baseRow, 50,"Beneficios en Viaje / Travel Benefits",Color.BLACK,false, "L", 9, communsPdf.setLineStyle(Color.gray), "", communsPdf.setPadding(5f,0f,3f,5f),Color.white);
                    communsPdf.setCell(baseRow, 30,"INCLUIDO/INCLUDED",Color.BLACK,false, "L", 9, communsPdf.setLineStyle(Color.gray), "", communsPdf.setPadding(5f,5f,3f,5f),Color.white);
                    communsPdf.setCell(baseRow, 20,"",Color.BLACK,false, "L", 9, communsPdf.setLineStyle(Color.gray), "", communsPdf.setPadding(5f,5f,3f,5f),Color.white);
                    


                    table.draw();

                    yStart -= (table.getHeaderAndDataHeight() );
                    table = new BaseTable(yStart, yStartNewPage, bottomMargin, 550, margin+15, document, page, true, true);
					baseRow = communsPdf.setRow(table);
			        communsPdf.setCell(baseRow, 100,"Conductores Adicionales / Aditional Drivers",Color.BLACK,true, "L", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),gray);
                    table.draw();
                    yStart -= (table.getHeaderAndDataHeight() );

                    texto = new StringBuilder();
                    texto.append("Cualquier conductor mayor de 18 años con licencia vigente y");
                    texto.append("autorizado por el asegurado está amparado en esta póliza / Any");
                    texto.append("driver over 18 years old, with valid driver license and authorized");
                    texto.append("by the insured is covered by this Policy.");
                    

                    table = new BaseTable(yStart, yStartNewPage, bottomMargin, 550/2, margin+15, document, page, true, true);
                    baseRow = communsPdf.setRow(table);
			        communsPdf.setCell(baseRow, 100,texto.toString(),Color.BLACK,false, "L", 9, communsPdf.setLineStyle(Color.gray), "", communsPdf.setPadding(0f,0f,3f,5f),Color.white);
                    table.draw();

                    output = new ByteArrayOutputStream();
                    document.save(output);
                    document.save(new File("/home/aalbanil/Vídeos/certificado.pdf"));
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
            float sy, String text, boolean justify, PDFont FONT, Float FONT_SIZE, Float LEADING) throws IOException {
        List<String> lines = parseLines(text, width, FONT, FONT_SIZE, LEADING);
        contentStream.setFont(FONT, FONT_SIZE);
        contentStream.newLineAtOffset(sx, sy);
        for (String line : lines) {
            float charSpacing = 0;
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
            PDFont FONT, Float FONT_SIZE, Float LEADING, Float paddig) {
        try {
            PDPageContentStream contentStream = new PDPageContentStream(document, page,
                    PDPageContentStream.AppendMode.APPEND, true);
            contentStream.beginText();
            addParagraph(contentStream, i, lines.get(1) + paddig, lines.get(2), tetxo, true, FONT, FONT_SIZE,
                    LEADING);
            contentStream.endText();
            contentStream.close();

            return contentStream;
        } catch (IOException e) {
            return null;
        }
    }

}
