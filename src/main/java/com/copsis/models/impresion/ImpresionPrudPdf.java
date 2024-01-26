package com.copsis.models.impresion;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import java.awt.Color ;
import com.copsis.clients.projections.CaractulaProjection;
import com.copsis.exceptions.GeneralServiceException;
import com.copsis.models.Tabla.BaseTable;
import com.copsis.models.Tabla.ImageUtils;
import com.copsis.models.Tabla.Row;
import com.copsis.models.Tabla.Sio4CommunsPdf;
import java.net.URL;

public class ImpresionPrudPdf {
     private final Color azul = new Color(68, 114, 196, 0);
        private final Color azulb = new Color(217, 226, 243, 0);
        private float yStartNewPage = 890, yStart = 792, bottomMargin = 32, fullWidth = 620, margin = 31, ytexto = 0;
        private Sio4CommunsPdf communsPdf = new Sio4CommunsPdf();
        private final Color colorLink = new Color(5, 99, 193, 0);

    public byte[] buildPDF(CaractulaProjection datos) {
        try {
            ByteArrayOutputStream output;
            try (PDDocument document = new PDDocument()) {
                try {
                      PDPage page = new PDPage();
                        document.addPage(page);
                        BaseTable table;
                        Row<PDPage> baseRow;
                        BaseTable table2;
                        Row<PDPage> baseRow2;
   
                       
                      table = new BaseTable(yStart, yStartNewPage, 0, fullWidth, 0, document, page, false, true);
                baseRow = communsPdf.setRow(table, 15);
                communsPdf.setCell(baseRow, 100,
                        ImageUtils.readImage("https://storage.googleapis.com/quattrocrm-prod/quattro-biibiic/2401/1N7rQflDvq65bN1u4E4VKMkiBzjJdcMHZX6ocgLIrBc7vz1H5gV6PXBNAlEmXhz/01.png"),
                        0, 0, Color.black);
                        table.remoBordes(true, bottomMargin);
                table.draw();

               
                getEncabezadoTexto(document, page);

                page = new PDPage();
				document.addPage(page);
                table = new BaseTable(yStart, yStartNewPage, 0, fullWidth, 0, document, page, false, true);
                baseRow = communsPdf.setRow(table, 15);
                communsPdf.setCell(baseRow, 100,
                        ImageUtils.readImage("https://storage.googleapis.com/quattrocrm-prod/quattro-biibiic/2401/1N7rQflDvq65bN1u4E4VKJeMr90uHUr8OGXqvpSEfCc7vz1H5gV6PXBNAlEmXhz/02.png"),
                        0, 0, Color.black);
                        table.remoBordes(true, bottomMargin);
                table.draw();
                getEncabezadoTexto(document, page);



                    output = new ByteArrayOutputStream();
                    document.save(output);
                    document.save(new File("/home/aalbanil/Vídeos/prudential" + datos.getTipo() +".pdf"));
                    return output.toByteArray();
                } finally {
                    document.close();
                }

            }
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new GeneralServiceException("00001",
                    "Ocurrio un error en el servicio ImpresionInter: " + ex.getMessage());

        }

    }

    private void getEncabezadoTexto(PDDocument document, PDPage page) throws IOException {
        BaseTable table2;
        Row<PDPage> baseRow2;
        table2 = new BaseTable(697, 780, bottomMargin, 116, 416, document, page, false, true);
        baseRow2 = communsPdf.setRow(table2);
        communsPdf.setCell(baseRow2, 100, "10000", Color.black, false, "L", 10,communsPdf.setLineStyle(Color.black),"", communsPdf.setPadding(3f),Color.white);
        table2.draw();
    }
}
