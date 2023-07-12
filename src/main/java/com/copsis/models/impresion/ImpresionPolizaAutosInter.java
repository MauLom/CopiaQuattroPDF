package com.copsis.models.impresion ;

    import java.awt.Color ;
    import java.io.ByteArrayOutputStream ;
import java.io.File;

import org.apache.pdfbox.pdmodel.PDDocument ;
    import org.apache.pdfbox.pdmodel.PDPage ;

    import com.copsis.clients.projections.CaractulaProjection ;
import com.copsis.clients.projections.PolizaAutosProjection;
import com.copsis.exceptions.GeneralServiceException ;
    import com.copsis.models.Tabla.BaseTable ;
    import com.copsis.models.Tabla.Row ;
    import com.copsis.models.Tabla.Sio4CommunsPdf ;
import com.copsis.models.Tabla.VerticalAlignment;

    public class ImpresionPolizaAutosInter {

        private float yStartNewPage = 780, yStart = 765, bottomMargin = 32, fullWidth = 570, margin =17;
                
        private Sio4CommunsPdf communsPdf = new Sio4CommunsPdf();      
        private final Color gray = new Color(217, 217, 217, 0);
      
        public byte[] buildPDF(PolizaAutosProjection  poliza) {
            try {
                ByteArrayOutputStream output;
                try ( PDDocument document = new PDDocument()) {

                    try {
                        PDPage page = new PDPage();
                        document.addPage(page);
                        BaseTable table;
                        Row<PDPage> baseRow;

                        table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth+5, margin, document, page, true, true);
                        baseRow = communsPdf.setRow(table, 40);
                        communsPdf.setCell(baseRow, 100, "                PÓLIZA DE SEGUROS DE AUTOMÓVILES", Color.BLACK, false, "L", 12, communsPdf.setLineStyle(Color.black), "", communsPdf.setPadding2(4f, 0f, 0f, 0f), Color.white).setValign(VerticalAlignment.MIDDLE);                        
                        table.draw();

                       table = new BaseTable(yStart-5, yStartNewPage, bottomMargin, 230,350, document, page, true, true);
                        baseRow = communsPdf.setRow(table, 29);
                        communsPdf.setCell(baseRow, 100, "", Color.BLACK, false, "L", 12, communsPdf.setLineStyle(Color.black), "", communsPdf.setPadding2(4f, 0f, 0f, 0f), Color.white).setValign(VerticalAlignment.MIDDLE);                        
                        table.draw();

                        table = new BaseTable(yStart-4, yStartNewPage, bottomMargin, 230,350, document, page, false, true);
                        baseRow = communsPdf.setRow(table, 19);
                        communsPdf.setCell(baseRow, 34, "PÓLIZA", Color.BLACK, false, "C", 8, communsPdf.setLineStyle(Color.black), "", communsPdf.setPadding2(4f, 0f, 0f, 0f), Color.white).setValign(VerticalAlignment.MIDDLE);
                        communsPdf.setCell(baseRow, 33, "ENDOSO", Color.BLACK, false, "C", 8, communsPdf.setLineStyle(Color.black), "", communsPdf.setPadding2(4f, 0f, 0f, 0f), Color.white).setValign(VerticalAlignment.MIDDLE);
                        communsPdf.setCell(baseRow, 33, "INCISO", Color.BLACK, false, "C", 8, communsPdf.setLineStyle(Color.black), "", communsPdf.setPadding2(4f, 0f, 0f, 0f), Color.white).setValign(VerticalAlignment.MIDDLE);
                        baseRow = communsPdf.setRow(table, 10);
                        communsPdf.setCell(baseRow, 34, poliza.getNumeroPoliza(), Color.BLACK, false, "C", 8, communsPdf.setLineStyle(Color.black), "", communsPdf.setPadding2(4f, 0f, 0f, 0f), Color.white).setValign(VerticalAlignment.MIDDLE);
                        communsPdf.setCell(baseRow, 33, poliza.getEndoso(), Color.BLACK, false, "C", 8, communsPdf.setLineStyle(Color.black), "", communsPdf.setPadding2(4f, 0f, 0f, 0f), Color.white).setValign(VerticalAlignment.MIDDLE);
                        communsPdf.setCell(baseRow, 33, poliza.getInciso(), Color.BLACK, false, "C", 8, communsPdf.setLineStyle(Color.black), "", communsPdf.setPadding2(4f, 0f, 0f, 0f), Color.white).setValign(VerticalAlignment.MIDDLE);                     
                        table.draw();

                        yStart -=table.getHeaderAndDataHeight()+20;


                        table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth+5,margin, document, page, true, true);
                        baseRow = communsPdf.setRow(table, 19);
                        communsPdf.setCell(baseRow, 100, "DESCRIPCIÓN", Color.BLACK, false, "C", 12, communsPdf.setLineStyle(Color.black), "", communsPdf.setPadding2(4f, 0f, 4f, 0f), Color.white).setValign(VerticalAlignment.MIDDLE);
                        table.draw();

                        yStart -=table.getHeaderAndDataHeight();

                        


                        table = new BaseTable(yStart, yStartNewPage, 0, fullWidth+5,margin, document, page, true, true);
                        baseRow = communsPdf.setRow(table, 660);
                        communsPdf.setCell(baseRow, 100, "", Color.BLACK, false, "C", 12, communsPdf.setLineStyle(Color.black), "", communsPdf.setPadding2(4f, 0f, 4f, 0f), Color.white).setValign(VerticalAlignment.MIDDLE);
                        table.draw();

                        table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth+5,margin, document, page, false, true);
                        baseRow = communsPdf.setRow(table, 19);
                        communsPdf.setCell(baseRow, 100, "POR MEDIO DEL PRESENTE SE HACE CONSTAR QUE SE MODIFICARA "+poliza.getMovimiento(), Color.BLACK, false, "L", 8, communsPdf.setLineStyle(Color.black), "", communsPdf.setPadding2(4f, 0f, 4f, 0f), Color.white).setValign(VerticalAlignment.MIDDLE);
                        table.draw();


                        output = new ByteArrayOutputStream();
                        document.save(output);
                        //document.save(new File("/home/aalbanil/V\u00EDdeos/certificado.pdf"));
                        return output.toByteArray();

                    } finally {
                        document.close();
                    }
                }

            } catch (Exception ex) {
                throw new GeneralServiceException("00001",
                        "Ocurrio un error en el servicio ImpresionInter: " + ex.getMessage());
            }

        }

    }