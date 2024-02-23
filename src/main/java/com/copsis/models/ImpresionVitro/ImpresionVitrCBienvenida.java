package com.copsis.models.ImpresionVitro;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.URL;

import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;

import com.copsis.controllers.forms.ImpresionBienvenidadForm;
import com.copsis.exceptions.GeneralServiceException;
import com.copsis.models.Tabla.BaseTable;
import com.copsis.models.Tabla.ImageUtils;
import com.copsis.models.Tabla.Row;
import com.copsis.models.Tabla.Sio4CommunsPdf;

public class ImpresionVitrCBienvenida {
    private byte[] byteArrayPDF;
    private Color blue = new Color(40, 76, 113);
    private Color black = new Color(0, 0, 0);
    private Color gray = new Color(229, 234, 237);

    private float margin = 10, yStartNewPage = 780, yStart = 780, bottomMargin = 400, yDespacho = 0, bottomMargin2 = 30;

    private Sio4CommunsPdf communsPdf = new Sio4CommunsPdf();
    private float fullWidth = 590;
    private Boolean acumula;
    private Boolean acumula2;
    private float pivoY;

    private float pivoyinicial;
    private float pivofinal;
    private float pivoresultado1;
    private float pivoresultado2;
    private float pivoresultado3;

    public byte[] buildPDF(ImpresionBienvenidadForm impresion) {
        float yStarpageNew = 0;
        float ysposionfija;
        boolean acumula = false;
        try {
            ByteArrayOutputStream output;
            try (PDDocument document = new PDDocument()) {
                PDFMergerUtility PDFmerger = new PDFMergerUtility();
                try {
                    PDPage page = new PDPage();
                    document.addPage(page);
                    BaseTable table;
                    Row<PDPage> baseRow;
                    this.setEncabezado(impresion, document, page, acumula2);
                    pivoY = yStart;
                    pivoyinicial = yStart;
                    table = new BaseTable(yStart, yStartNewPage, bottomMargin, 295, margin, document, page, false, true);
                    baseRow = communsPdf.setRow(table, 5);

                    
                    communsPdf.setCell(baseRow, 100,  impresion.getTitulo(), Color.black, true, "L", 14, communsPdf.setLineStyle(Color.black), "", communsPdf.setPadding(3f), Color.white);
                    baseRow = communsPdf.setRow(table, 15);
                    
                    communsPdf.setCell(baseRow, 100,  impresion.getSubtitulo(), Color.black, false, "L", 14, communsPdf.setLineStyle(Color.black), "", communsPdf.setPadding(3f), Color.white);

                    baseRow = communsPdf.setRow(table, 18);
                    if (impresion.getAvataraseguradora() != null && !impresion.getAvataraseguradora().isEmpty()) {
                        communsPdf.setCell(baseRow, 50, ImageUtils.readImage(impresion.getAvataraseguradora()));
                    }
                   
                    table.draw();



                    table = new BaseTable(yStart, yStartNewPage, bottomMargin, 295, 295, document, page, false, true);
                    baseRow = communsPdf.setRow(table, 5);
                    communsPdf.setCell(baseRow, 100, "", Color.white, false, "L", 14, communsPdf.setLineStyle(Color.black), "", communsPdf.setPadding(3f), Color.white);
                    baseRow = communsPdf.setRow(table, 15);
                    communsPdf.setCell(baseRow, 37, "Número de póliza:", Color.white, false, "L", 11, communsPdf.setLineStyle(Color.black), "", communsPdf.setPadding(3.5f), Color.white);
                    communsPdf.setCell(baseRow, 63, impresion.getNoPoliza(), Color.white, true, "L", 11, communsPdf.setLineStyle(Color.black), "", communsPdf.setPadding(3.5f), Color.white);
                    baseRow = communsPdf.setRow(table, 15);
                    communsPdf.setCell(baseRow, 37, "Aseguradora:", Color.white, false, "L", 11, communsPdf.setLineStyle(Color.black), "", communsPdf.setPadding(3.5f), Color.white);
                    communsPdf.setCell(baseRow, 63, impresion.getAseguradora(), Color.white, true, "L", 11, communsPdf.setLineStyle(Color.black), "", communsPdf.setPadding(3.5f), Color.white);
                    baseRow = communsPdf.setRow(table, 15);
                    communsPdf.setCell(baseRow, 37, "Inicio de vigencia:", Color.white, false, "L", 11, communsPdf.setLineStyle(Color.black), "", communsPdf.setPadding(3.5f), Color.white);
                    communsPdf.setCell(baseRow, 63, impresion.getVigencide(), Color.white, true, "L", 11, communsPdf.setLineStyle(Color.black), "", communsPdf.setPadding(3.5f), Color.white);
                    baseRow = communsPdf.setRow(table, 15);
                    communsPdf.setCell(baseRow, 37, "Término de vigencia:", Color.white, false, "L", 11, communsPdf.setLineStyle(Color.black), "", communsPdf.setPadding(3.5f), Color.white);
                    communsPdf.setCell(baseRow, 63, impresion.getVigencia(), Color.white, true, "L", 11, communsPdf.setLineStyle(Color.black), "", communsPdf.setPadding(3.5f), Color.white);
                    baseRow = communsPdf.setRow(table, 15);
                    communsPdf.setCell(baseRow, 37, "Concepto:", Color.white, false, "L", 11, communsPdf.setLineStyle(Color.black), "", communsPdf.setPadding(3.5f), Color.white);
                    communsPdf.setCell(baseRow, 63, impresion.getConcepto(), Color.white, true, "L", 11, communsPdf.setLineStyle(Color.black), "", communsPdf.setPadding(3.5f), Color.white);
                    baseRow = communsPdf.setRow(table, 15);
                    communsPdf.setCell(baseRow, 37, "Forma de Pago:", Color.white, false, "L", 11, communsPdf.setLineStyle(Color.black), "", communsPdf.setPadding(3.5f), Color.white);
                    communsPdf.setCell(baseRow, 63,impresion.getFormaPago(), Color.white, true, "L", 11, communsPdf.setLineStyle(Color.black), "", communsPdf.setPadding(3.5f), Color.white);
                    baseRow = communsPdf.setRow(table, 5);
                    table.draw();
                    yStart -= table.getHeaderAndDataHeight();
                    pivoY = yStart;

                    PDPageContentStream content02 = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.PREPEND, true);
                    content02.setNonStrokingColor(Color.decode(impresion.getColorDpoliza()));
                    content02.addRect(295, pivoY, 295, ((pivoyinicial - 10) - pivoY) + 10);
                    content02.fill();
                    content02.close();
                    yStart = pivoY - 10;

                    pivofinal = yStart;

                    pivoresultado1 = yStart;


                    table = new BaseTable(yStart, yStartNewPage, bottomMargin, 580, margin, document, page, false, true);
                    baseRow = communsPdf.setRow(table, 5);
                    baseRow = communsPdf.setRow(table, 15);
                    communsPdf.setCell(baseRow, 25, "Recomendaciones",  Color.decode("#444444"), true, "L", 14, communsPdf.setLineStyle(Color.black), "", communsPdf.setPadding(3.5f), Color.white);
                    communsPdf.setCell(baseRow, 75,"Te damos algunos tips para el uso de tu seguro.",  Color.decode("#444444"), false, "L", 12, communsPdf.setLineStyle(Color.black), "", communsPdf.setPadding(3.5f), Color.white);
                    baseRow = communsPdf.setRow(table, 14);
                    table.draw();


                    yStart -= table.getHeaderAndDataHeight();

                  if(impresion.getTipo() ==1){
                            int t = 0;
                            int c = 0;
                            String dato = communsPdf.eliminaHtmlTags(impresion.getSeccion1());
                            String datos[] = dato.split("<br>|<br/>|</br>");

                            while (t < datos.length) {
                                acumula2 = true;

                                table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, 20, document, page, false, true);
                                baseRow = communsPdf.setRow(table, 12f);

                                
                                communsPdf.setCell(baseRow, 100,communsPdf.eliminaHtmlTags(datos[t]),  Color.decode("#444444"), false, "L", 11, communsPdf.setLineStyle(Color.black), "", communsPdf.setPadding(3.5f),gray);

                                if (isEndOfPage(table)) {
                                    table.getRows().remove(table.getRows().size() - 1);
                                    table.draw();

                                    page = new PDPage();
                                    document.addPage(page);
                                    setEncabezado(impresion, document, page, false);
                                    acumula2 = false;

                                } else {
                                    table.draw();
                                    yStart -= table.getHeaderAndDataHeight();
                                }
                                c++;

                                if (acumula2) {
                                    t++;
                                }
                                if (c > 100) {
                                    table.draw();
                                    break;

                                }

                            }

                            pivoresultado2 = yStart;
                            pivoresultado3 = pivoresultado1 - 400;
                            PDPageContentStream contentgris = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.PREPEND, true);
                            communsPdf.drawBox(contentgris, Color.decode("#F8F8F8"), 0, 400, 700, (pivoresultado3));

                            contentgris.close();

                        }

                    yStart = 395;
                    table = new BaseTable(400, yStartNewPage, 0, 622, 0, document, page, false, true);
                    baseRow = communsPdf.setRow(table, 190);
                    communsPdf.setCell(baseRow, 100, ImageUtils.readImage(impresion.getImagen1()));
                    table.draw();

                    int t1 = 0;
                    int c1 = 0;
                    String dato1 = communsPdf.eliminaHtmlTags(impresion.getSeccion2());
                    String datos1[] = dato1.split("<br>|<br/>|</br>");

                    while (t1 < datos1.length) {
                        acumula = true;

                        table = new BaseTable(yStart, yStartNewPage, bottomMargin2, fullWidth, margin, document, page, false, true);

                        baseRow = communsPdf.setRow(table, 13f);
                        if (datos1[t1].contains("Números de atención") || datos1[t1].contains("Contacto directo con nosotros:")) {                                                  
                            communsPdf.setCell(baseRow, 100,communsPdf.eliminaHtmlTags(datos1[t1]), black, false, "L", 12, communsPdf.setLineStyle(Color.black), "", communsPdf.setPadding(3.5f),gray);
                        } else {                           
                            communsPdf.setCell(baseRow, 100,communsPdf.eliminaHtmlTags(datos1[t1]), black, false, "L", 10, communsPdf.setLineStyle(Color.black), "", communsPdf.setPadding(0f),gray).setLeftPadding(30f);
                        }

                        if (isEndOfPage2(table)) {
                            table.getRows().remove(table.getRows().size() - 1);
                            table.draw();

                            page = new PDPage();
                            document.addPage(page);
                            setEncabezado(impresion, document, page, false);
                            acumula = false;

                        } else {

                            table.draw();
                            yStart -= table.getHeaderAndDataHeight();
                        }
                        c1++;

                        if (acumula) {
                            t1++;
                        }
                        if (c1 > 100) {
                            table.draw();
                            break;

                        }

                    }

                   if (impresion.getImagen2() != null && !impresion.getImagen2().isEmpty()) {
                    table = new BaseTable(700, yStartNewPage, 0, 612, 0, document, page, true, true);
                    baseRow = communsPdf.setRow(table, 800);                    
                    communsPdf.setCellImg(baseRow, 100, ImageUtils.readImage(impresion.getImagen3()), communsPdf.setLineStyle(Color.white), communsPdf.setPadding2(0f, 0f, 0f, 0f), "L", "");                    
                    table.draw();

                    }

                    if (impresion.getWebpath() != null && !impresion.getWebpath().isEmpty()) {
                        final URL scalaByExampleUrl = new URL(impresion.getWebpath());
                    final PDDocument documentToBeParsed = PDDocument.load(scalaByExampleUrl.openStream());
                    PDFmerger.appendDocument(document, documentToBeParsed);
                    PDFmerger.mergeDocuments();
                    }



                    output = new ByteArrayOutputStream();
                    document.save(output);
                    // document.save(new
                    // File("/home/aalbanil/Documentos/caractula.pdf"));
                    return output.toByteArray();
                } finally {
                    document.close();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new GeneralServiceException("00001",
                    "Ocurrio un error en el servicio ImpresioncaratulaAutos: " + ex.getMessage());
        }
    }

    private void setEncabezado(ImpresionBienvenidadForm impresion, PDDocument document, PDPage page,
            Boolean setDetalle) {
        try (PDPageContentStream content = new PDPageContentStream(document, page)) {
            yStart = 780;
            yDespacho = yStart;

            BaseTable table;
            Row<PDPage> baseRow;

            if (impresion.getAvatarwks() != null && !impresion.getAvatarwks().isEmpty()) {
                table = new BaseTable(776, yStartNewPage, 0, 88, 16, document, page, false, true);
                baseRow = communsPdf.setRow(table, 40);
                communsPdf.setCell(baseRow, 100, ImageUtils.readImage(impresion.getAvatarwks()));
                table.remoBordes(true, 0);
                table.draw();
            }

            if (impresion.getAvatarsocio() != null && !impresion.getAvatarsocio().isEmpty()) {
                table = new BaseTable(776, yStartNewPage, 0, 137, 459, document, page, false, true);
                baseRow = communsPdf.setRow(table, 40);
                communsPdf.setCell(baseRow, 100, ImageUtils.readImage(impresion.getAvatarsocio()));
                table.remoBordes(true, 0);
                table.draw();
            }

            yStart = 720;
            PDPageContentStream contentgris = new PDPageContentStream(document, page,
                    PDPageContentStream.AppendMode.PREPEND, true);
            communsPdf.drawBox(contentgris, Color.decode("#F0EFF5"), 0, yStart, 700, 0.5f);
            contentgris.close();
            yStart = 717;
            if (impresion.getImagen2() != null && !impresion.getImagen2().isEmpty()) {
                table = new BaseTable(204, yStartNewPage, 0, 622, 0, document, page, false, true);
                baseRow = communsPdf.setRow(table, 194);
                communsPdf.setCell(baseRow, 100, ImageUtils.readImage(impresion.getImagen2()));
                table.remoBordes(true, 0);
                table.draw();
            }

        } catch (Exception ex) {
            System.out.println("error setEncabezado endoso -> " + ex.getMessage());

        }
    
    }
    private boolean isEndOfPage(BaseTable table) {
        float currentY = yStart - table.getHeaderAndDataHeight();
        boolean isEndOfPage = currentY <= bottomMargin;

        return isEndOfPage;
    }

    private boolean isEndOfPage2(BaseTable table) {
        float currentY = yStart - table.getHeaderAndDataHeight();
        boolean isEndOfPage = currentY <= bottomMargin2;

        return isEndOfPage;
    }
}