package com.copsis.models.impresionAxa;

import java.awt.Color;
import java.awt.image.BufferedImage;
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
import com.copsis.models.Tabla.PDocumenteHW;
import com.copsis.models.Tabla.Row;
import com.copsis.models.Tabla.Sio4CommunsPdf;

public class ImpresionCredencialPdf {

    
    private final Color bgColor = new Color(255, 255, 255, 0);

    private float margin = 2, yStartNewPage = 308, yStart = 308, bottomMargin = 30;
    private Sio4CommunsPdf communsPdf = new Sio4CommunsPdf();
    private float fullWidth = 240;
    private boolean acumula;

    public byte[] buildPDF(ImpresionAxaForm impresionAxa) {
        ByteArrayOutputStream output;
        try {
            try (PDDocument document = new PDDocument()) {
                try {
                    PDPage page = new PDPage(PDocumenteHW.A7);
                    document.addPage(page);
                    BaseTable table;
                    Row<PDPage> baseRow;
        
                    setEncabezado(impresionAxa, document, page);

                    switch (impresionAxa.getCoberturas().size()) {
                    case 3:
                          bottomMargin = 55;
                        break;
                    case 4:
                          bottomMargin = 45;
                        break;
                    case 5:
                           bottomMargin = 40;
                        break;
                    case 6:
                           bottomMargin = 35;
                        break;
                    case 7:
                           bottomMargin = 35;
                        break;

                    default:
                        break;
                    }
                    
                    
                    yStart = 75;
                    int i = 0;
                    while (i < impresionAxa.getAsegurados().size()) {
                           acumula = true;
                          
                           table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, margin, document, page, false, true);
                           baseRow = communsPdf.setRow(table, 7);
                           if (i % 5 == 0) {
                               communsPdf.setCell(baseRow, 15,"Asegurado(s):",Color.BLACK,true, "L", 5, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(0f,0f,3f,0f),bgColor);
                           }else {
                               communsPdf.setCell(baseRow, 15, "",Color.BLACK,true, "R", 5, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(0f,0f,5f,5f),bgColor);
                           }
                           communsPdf.setCell(baseRow, 50,impresionAxa.getAsegurados().get(i).getNombre(),Color.BLACK,false, "L", 5, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(0f,0f,3f,0f),bgColor);
                           communsPdf.setCell(baseRow, 35,impresionAxa.getAsegurados().get(i).getVigencia(),Color.BLACK,false, "R", 5, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,5f,5f),bgColor);

                            if (isEndOfPage(table)) {
                                table.getRows().remove(table.getRows().size() - 1);
                                table.draw();
                                page = new PDPage(PDocumenteHW.A7);
                                document.addPage(page);
                                setEncabezado(impresionAxa, document, page);
                                acumula = false;
                                yStart =75;

                            } else {
                                table.draw();
                                yStart -= table.getHeaderAndDataHeight();
                            }

                            if (acumula) {
                                i++;
                            }
                            if (i > 100) {
                                table.draw();
                                break;
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

    public void setEncabezado(ImpresionAxaForm impresionAxa, PDDocument document, PDPage page) {
        try (PDPageContentStream content = new PDPageContentStream(document, page)) {
            BaseTable table;
            Row<PDPage> baseRow;
   
            yStart = 160;

            if ( impresionAxa.getLogoCredencial() !=null &&impresionAxa.getLogoCredencial().length() > 0) {
                URL urllogo = new URL(impresionAxa.getLogoCredencial());
                BufferedImage imgbarra = ImageIO.read(urllogo);
                PDImageXObject pdImage2 = LosslessFactory.createFromImage(document, imgbarra);
                content.drawImage(pdImage2, 0, 165, 243, 149);
            }
            
                setEncabezado2(impresionAxa, document, page);
                        
             table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, margin, document, page, false, true);
             baseRow = communsPdf.setRow(table, 12);              
             if (impresionAxa.getLogoBarraAxa() !=null &&  impresionAxa.getLogoBarraAxa().length() > 0) {
                 communsPdf.setCell(baseRow, 100, ImageUtils.readImage(impresionAxa.getLogoBarraAxa()));
             }
             baseRow = communsPdf.setRow(table, 5);           
             communsPdf.setCell(baseRow, 100, impresionAxa.getContrannte(),Color.BLACK,false, "L", 5, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(3.5f),bgColor);
             if (isEndOfPage(table)) {
             } else {
                 table.draw();
                 yStart -= table.getHeaderAndDataHeight();
             }
             
             
             table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, margin, document, page, false, true);
             baseRow = communsPdf.setRow(table, 6);
             communsPdf.setCell(baseRow, 81, "Poliza:",Color.BLACK,true, "R", 5, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(0f,0f,5f,5f),bgColor);
             communsPdf.setCell(baseRow, 19, impresionAxa.getNoPoliza(),Color.BLACK,false, "R", 5, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(0f,5f,5f,5f),bgColor);
             if (isEndOfPage(table)) {

             } else {
                table.draw();
                yStart -= table.getHeaderAndDataHeight();
             }
             
             
          
                table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, margin, document, page, false, true);
                for (int i = 0; i < impresionAxa.getCoberturas().size(); i++) {
                    if(impresionAxa.getCoberturas().get(i).getIndex() > 0) {
                        switch (impresionAxa.getCoberturas().get(i).getIndex()) {
                        case 1:
                          
                           baseRow = communsPdf.setRow(table, 6);
                           communsPdf.setCell(baseRow, 35, impresionAxa.getCoberturas().get(i).getNombres(),Color.BLACK,true, "L", 5, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(0f,0f,5f,5f),bgColor);
                           communsPdf.setCell(baseRow, 39, impresionAxa.getCoberturas().get(i).getSa(),Color.BLACK,false, "L", 5, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(0f,0f,5f,5f),bgColor);
                           if (i == 0) {
                               communsPdf.setCell(baseRow, 12, "Certificado",Color.BLACK,true, "L", 5, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(0f,0f,5f,0f),bgColor);
                               communsPdf.setCell(baseRow, 17, impresionAxa.getNoCertificado(),Color.BLACK,false, "L", 5, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(0f,0f,5f,0f),bgColor);

                           }
                           if (i == 1) {
                               communsPdf.setCell(baseRow, 12,(impresionAxa.getEtiquetaPlan().length() > 0 ? "Plan:" :"" ) ,Color.BLACK,true, "L", 5, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(0f,0f,5f,0f),bgColor);
                               communsPdf.setCell(baseRow, 17,  impresionAxa.getEtiquetaPlan(),Color.BLACK,false, "L", 5, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(0f,0f,5f,0f),bgColor);

                           }
                  
                           
                           
                            break;
                        case 2:
                            //baseRow2 = communsPdf.setRow(table2, 6);
                               baseRow = communsPdf.setRow(table, 6);
                               communsPdf.setCell(baseRow, 35, impresionAxa.getCoberturas().get(i).getNombres(),Color.BLACK,true, "L", 5, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(0f,0f,5f,5f),bgColor);
                               communsPdf.setCell(baseRow, 39, impresionAxa.getCoberturas().get(i).getSa(),Color.BLACK,false, "L", 5, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(0f,5f,5f,5f),bgColor);

                               if (i == 0) {
                                   communsPdf.setCell(baseRow, 12, "Certificado",Color.BLACK,true, "L", 5, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(0f,0f,5f,0f),bgColor);
                                   communsPdf.setCell(baseRow, 17, impresionAxa.getNoCertificado(),Color.BLACK,false, "L", 5, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(0f,0f,5f,0f),bgColor);

                               }
                               if (i == 1) {
                                   communsPdf.setCell(baseRow, 12, (impresionAxa.getEtiquetaPlan().length() > 0 ? "Plan:" :"" ),Color.BLACK,true, "L", 5, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(0f,0f,5f,0f),bgColor);
                                   communsPdf.setCell(baseRow, 17, impresionAxa.getEtiquetaPlan(),Color.BLACK,false, "L", 5, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(0f,0f,5f,0f),bgColor);

                               }

                                break;
                        case 3:
                        case 4:
                           
                               baseRow = communsPdf.setRow(table, 6);
                               communsPdf.setCell(baseRow, 35, impresionAxa.getCoberturas().get(i).getNombres(),Color.BLACK,true, "L", 5, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(0f,0f,5f,5f),bgColor);
                               communsPdf.setCell(baseRow, 39, impresionAxa.getCoberturas().get(i).getSa(),Color.BLACK,false, "L", 5, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(0f,5f,5f,5f),bgColor);

                               if (i == 0) {
                                   communsPdf.setCell(baseRow, 12, "Certificado",Color.BLACK,true, "L", 5, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(0f,0f,5f,0f),bgColor);
                                   communsPdf.setCell(baseRow, 17, impresionAxa.getNoCertificado(),Color.BLACK,false, "L", 5, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(0f,0f,5f,0f),bgColor);

                               }
                               if (i == 1) {
                                   communsPdf.setCell(baseRow, 12, (impresionAxa.getEtiquetaPlan().length() > 0 ? "Plan:" :"" ),Color.BLACK,true, "L", 5, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(0f,0f,5f,0f),bgColor);
                                   communsPdf.setCell(baseRow, 17,  impresionAxa.getEtiquetaPlan(),Color.BLACK,false, "L", 5, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(0f,0f,5f,0f),bgColor);

                               }
                       
                               
                               
                                break;

                    
                        }
                        
                    }else {
                           baseRow = communsPdf.setRow(table, 6);
                           communsPdf.setCell(baseRow, 35, impresionAxa.getCoberturas().get(i).getNombres(),Color.BLACK,true, "L", 5, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(0f,0f,5f,5f),bgColor);
                           communsPdf.setCell(baseRow, 65, impresionAxa.getCoberturas().get(i).getSa(),Color.BLACK,false, "L", 5, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(0f,5f,5f,5f),bgColor);

                    }
                }
                if (isEndOfPage(table)) {

                } else {
           
                    table.draw();
                    yStart -= table.getHeaderAndDataHeight() + 1;
                }
                
                
                table = new BaseTable(80, yStartNewPage, bottomMargin, fullWidth, margin, document, page, false, true);
                baseRow = communsPdf.setRow(table, 6);  
                communsPdf.setCell(baseRow, 98, "Exclusivo Uso Interno",Color.BLACK,true, "R", 5, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(0f,0f,5f,5f),bgColor);
                if (isEndOfPage(table)) {

                } else {
                    table.draw();
                    yStart -= table.getHeaderAndDataHeight();
                }

                
              setFooter(impresionAxa, document, page);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new GeneralServiceException("00001",
                    "Ocurrio un error en el servicio ImpresionCredencialPdf: " + ex.getMessage());
        }
    }

    private void setEncabezado2(ImpresionAxaForm impresionAxa, PDDocument document, PDPage page) {
        try {
            yStart = 160;
            BaseTable table;
            Row<PDPage> baseRow;
            table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, margin, document, page, false, true);
            baseRow = communsPdf.setRow(table, 12);

            if (impresionAxa.getLogoBarraAxa() !=null && impresionAxa.getLogoBarraAxa().length() > 0) {
                communsPdf.setCell(baseRow, 100, ImageUtils.readImage((impresionAxa.getLogoBarraAxa())));
            }

            table.draw();

        } catch (Exception ex) {
            throw new GeneralServiceException("00001",
                    "Ocurrio un error en el servicio setEncabezado2: " + ex.getMessage());

        }
    }
    
    
    private void setFooter(ImpresionAxaForm impresionAxa, PDDocument document, PDPage page) {
        try {
            BaseTable table = new BaseTable(30, 30, 10, fullWidth, margin, document, page, false, true);
            Row<PDPage> baseRow;
            baseRow = communsPdf.setRow(table, 5);
            communsPdf.setCell(baseRow, 80, impresionAxa.getLeyenda(),Color.BLACK,false, "L", 5, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(0f,0f,3f,0f),bgColor);  
            table.draw();

            table = new BaseTable(20, 20, 10, fullWidth, margin, document, page, false, true);
            baseRow = communsPdf.setRow(table, 5);     
            communsPdf.setCell(baseRow, 80, "Agente:",Color.BLACK,true, "R", 5, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(0f,0f,5f,5f),bgColor);                       
            communsPdf.setCell(baseRow, 10,  impresionAxa.getCveAgente(),Color.BLACK,false, "R", 5, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(0f,0f,5f,5f),bgColor);
            communsPdf.setCell(baseRow, 10, "",Color.BLACK,true, "R", 5, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(0f,0f,5f,5f),bgColor);            
            table.draw();

        } catch (Exception ex) {
            throw new GeneralServiceException("00001",
                    "Ocurrio un error en el servicio setFooter: " + ex.getMessage());
        }
    }

    
    

    private boolean isEndOfPage(BaseTable table) {
        float currentY = yStart - table.getHeaderAndDataHeight();
        boolean isEndOfPage = currentY <= bottomMargin;
        return isEndOfPage;
    }


}
