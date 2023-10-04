package com.copsis.models.impresionQuattrocrm;

import java.awt.Color;
import java.io.ByteArrayOutputStream;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;

import com.copsis.exceptions.GeneralServiceException;
import com.copsis.models.Tabla.BaseTable;
import com.copsis.models.Tabla.ImageUtils;
import com.copsis.models.Tabla.Row;
import com.copsis.models.Tabla.Sio4CommunsPdf;
import com.copsis.clients.projections.SocioDirecProjection;
import com.copsis.controllers.forms.ImpresionReclamacionForm;

public class ImpresionSiniestroAutos {

    private Color black = new Color(0, 0, 0);
    private Color gray = new Color(229, 234, 237);
    private Sio4CommunsPdf communsPdf = new Sio4CommunsPdf();
    private float yStart = 780, bottomMargin = 30, fullWidth = 590, yStartNewPage = 780, margin = 10;
    private Color grayCell = new Color(148, 166, 187);

    public byte[] buildPDF(ImpresionReclamacionForm reclamacion) {
        float yStarpageNew = 0;
        float ysposionfija;
        boolean acumula = false;
        boolean acumula2 = false;
        boolean yTexto = false;

        try {
            ByteArrayOutputStream output;
            try (PDDocument document = new PDDocument()) {
                try {
                         PDPage page = new PDPage();
                        document.addPage(page);
                        BaseTable table;
                        Row<PDPage> baseRow;
                       yStart = this.setEncabezado(document, page, reclamacion, false, false, false, false, false);

                    output = new ByteArrayOutputStream();
                    document.save(output);
                    //document.save(new File("/home/aalbanil/Vídeos/IMPRESIONCARACTULA/reclamncion.pdf"));
                    return output.toByteArray();
                } finally {
                    document.close();
                }
            }
        } catch (Exception ex) {
            throw new GeneralServiceException("00001",
                    "Ocurrio un error en el servicio ImpresioncaratulaAutos: " + ex.getMessage());
        }

    }


    private float setEncabezado(PDDocument document, PDPage page, ImpresionReclamacionForm reclamacion,boolean headAdjuntos,
        boolean headerContiFacturas,boolean headeFactura,boolean headresult,boolean headerObservaciones ) {
            try ( PDPageContentStream conten = new PDPageContentStream(document, page)) {
                yStart = 780;
                BaseTable table;
                Row<PDPage> baseRow;
                SocioDirecProjection socio = reclamacion.getSocio();
                if (socio != null) {

                    table = new BaseTable(775, 775, bottomMargin, 150, 10, document, page, false, true);
                    baseRow = communsPdf.setRow(table, 12);
                    communsPdf.setCell(baseRow, 100, ImageUtils.readImage(socio.getAvatar()).scale(130, 130));
                    table.draw();
                }
    
                String direccion =socio !=null ?  socio.getNombSocio() +" "+ socio.getCalle()+" "+ socio.getColonia() +" "+ socio.getEstado():"";

                table = new BaseTable(yStart, yStart, bottomMargin, 245, 180, document, page, false, true);
                baseRow = communsPdf.setRow(table);
                communsPdf.setCell(baseRow, 100, direccion, black, false, "C", 9, communsPdf.setLineStyle(gray), "", communsPdf.setPadding(4f), gray).setLineSpacing(1.2f);;
                table.draw();
               

                table = new BaseTable(yStart, yStart, bottomMargin, 155, 443, document, page, true, true);
                baseRow = communsPdf.setRow(table,10);
                communsPdf.setCell(baseRow, 60, "No.Siniestro", black, false, "L", 8, communsPdf.setLineStyle(grayCell), "", communsPdf.setPadding(3f), grayCell).setLeftPadding(10);                
                communsPdf.setCell(baseRow, 40, reclamacion.getSiniestro().getFechaCaptura(), black, false, "C", 8, communsPdf.setLineStyle(grayCell), "", communsPdf.setPadding(3f), grayCell);
                baseRow = communsPdf.setRow(table,10);
                communsPdf.setCell(baseRow, 60, "Orden", black, false, "L", 8, communsPdf.setLineStyle(grayCell), "", communsPdf.setPadding(3f), grayCell).setLeftPadding(10);                
                communsPdf.setCell(baseRow, 40, reclamacion.getSiniestro().getFechaCaptura(), black, false, "C", 8, communsPdf.setLineStyle(grayCell), "", communsPdf.setPadding(3f), grayCell);
                table.draw();

               

                 yStart -= table.getHeaderAndDataHeight()+15;

                 table = new BaseTable(yStart, yStart, bottomMargin, fullWidth, margin, document, page, true, true);
                 baseRow = communsPdf.setRow(table);
                 communsPdf.setCell(baseRow, 58, "Contratante", black, true, "L", 9,communsPdf.setLineStyle(gray),"", communsPdf.setPadding(3f), gray);
                 communsPdf.setCell(baseRow, 42, "Movimiento", black, true, "L", 9,communsPdf.setLineStyle(gray),"", communsPdf.setPadding(3f),gray);                                
                 baseRow = communsPdf.setRow(table,15);
                 communsPdf.setCell(baseRow, 58,"",black, true, "L", 13,communsPdf.setLineStyle(gray),"", communsPdf.setPadding(3f), Color.white);
                 communsPdf.setCell(baseRow, 42,"",black, true, "L", 9,communsPdf.setLineStyle(gray),"", communsPdf.setPadding(3f), Color.white); 
                 baseRow = communsPdf.setRow(table,15);
                 communsPdf.setCell(baseRow, 14,"", black, true, "L", 9,communsPdf.setLineStyle(gray),"", communsPdf.setPadding(3f), Color.white); 
                 communsPdf.setCell(baseRow, 44,"Póliza", black, false, "L", 9,communsPdf.setLineStyle(gray),"", communsPdf.setPadding(3f), Color.white); 
                 communsPdf.setCell(baseRow, 42,"", black, true, "L", 9,communsPdf.setLineStyle(gray),"", communsPdf.setPadding(3f), Color.white);
                 baseRow = communsPdf.setRow(table,15);
                 communsPdf.setCell(baseRow, 14,"Emisión:", black, true, "L", 9,communsPdf.setLineStyle(gray),"", communsPdf.setPadding(3f), Color.white); 
                 communsPdf.setCell(baseRow, 44,"", black, false, "L", 9,communsPdf.setLineStyle(gray),"", communsPdf.setPadding(3f), Color.white); 
                 communsPdf.setCell(baseRow, 42,"", black, true, "L", 9,communsPdf.setLineStyle(gray),"", communsPdf.setPadding(3f), Color.white);
                 baseRow = communsPdf.setRow(table,15);
                 communsPdf.setCell(baseRow, 14,"Subramo::", black, true, "L", 9,communsPdf.setLineStyle(gray),"", communsPdf.setPadding(3f), Color.white); 
                 communsPdf.setCell(baseRow, 44,"", black, false, "L", 9,communsPdf.setLineStyle(gray),"", communsPdf.setPadding(3f), Color.white); 
                 communsPdf.setCell(baseRow, 42,"", black, true, "L", 9,communsPdf.setLineStyle(gray),"", communsPdf.setPadding(3f), Color.white);
                 baseRow = communsPdf.setRow(table,15);
                 communsPdf.setCell(baseRow, 14,"Forma de Pago:", black, true, "L", 9,communsPdf.setLineStyle(gray),"", communsPdf.setPadding(3f), Color.white); 
                 communsPdf.setCell(baseRow, 44,"", black, false, "L", 9,communsPdf.setLineStyle(gray),"", communsPdf.setPadding(3f), Color.white); 
                 communsPdf.setCell(baseRow, 9,"CURP:", black, true, "L", 9,communsPdf.setLineStyle(gray),"", communsPdf.setPadding(3f), Color.white);
                 communsPdf.setCell(baseRow, 30,"" , black, true, "L", 9,communsPdf.setLineStyle(gray),"", communsPdf.setPadding(3f), Color.white);
                 baseRow = communsPdf.setRow(table,15);
                 communsPdf.setCell(baseRow, 14,"Moneda:", black, true, "L", 9,communsPdf.setLineStyle(gray),"", communsPdf.setPadding(3f), Color.white); 
                 communsPdf.setCell(baseRow, 44,"", black, false, "L", 9,communsPdf.setLineStyle(gray),"", communsPdf.setPadding(3f), Color.white); 
                 communsPdf.setCell(baseRow, 7,"Email:", black, true, "L", 9,communsPdf.setLineStyle(gray),"", communsPdf.setPadding(3f), Color.white); 
                 communsPdf.setCell(baseRow, 30, "", black, false, "L", 9,communsPdf.setLineStyle(gray),"", communsPdf.setPadding(3f), Color.white);
                 baseRow = communsPdf.setRow(table,15);
                 communsPdf.setCell(baseRow, 58,"", black, true, "L", 9,communsPdf.setLineStyle(gray),"", communsPdf.setPadding(3f), Color.white); 
                 communsPdf.setCell(baseRow, 10,"Teléfono:", black, true, "L", 9,communsPdf.setLineStyle(gray),"", communsPdf.setPadding(3f), Color.white); 
                 communsPdf.setCell(baseRow, 30,"", black, false, "L", 9,communsPdf.setLineStyle(gray),"", communsPdf.setPadding(3f), Color.white);                
                 table.remoBordes(true, 1);
                 table.draw();

                 yStart -=table.getHeaderAndDataHeight()+10;
                 table = new BaseTable(yStart, yStart, bottomMargin, fullWidth, margin, document, page, true, true);
                 baseRow = communsPdf.setRow(table);
                 communsPdf.setCell(baseRow, 58, "Agente:", black, true, "L", 9,communsPdf.setLineStyle(gray),"", communsPdf.setPadding(3f), gray);
                 communsPdf.setCell(baseRow, 42, "Grupo:", black, true, "L", 9,communsPdf.setLineStyle(gray),"", communsPdf.setPadding(3f),gray);                                
                 baseRow = communsPdf.setRow(table,15);
                 communsPdf.setCell(baseRow, 14,"Aseguradora:", black, true, "L", 9,communsPdf.setLineStyle(gray),"", communsPdf.setPadding(3f), Color.white); 
                 communsPdf.setCell(baseRow, 44,"", black, false, "L", 9,communsPdf.setLineStyle(gray),"", communsPdf.setPadding(3f), Color.white); 
                 communsPdf.setCell(baseRow, 42,"", black, true, "L", 9,communsPdf.setLineStyle(gray),"", communsPdf.setPadding(3f), Color.white);
                 baseRow = communsPdf.setRow(table,15);
                 communsPdf.setCell(baseRow, 14,"Clave:", black, true, "L", 9,communsPdf.setLineStyle(gray),"", communsPdf.setPadding(3f), Color.white); 
                 communsPdf.setCell(baseRow, 44,"", black, false, "L", 9,communsPdf.setLineStyle(gray),"", communsPdf.setPadding(3f), Color.white);                                   
                 table.remoBordes(true, 1);
                 table.draw();
             return yStart;
            } catch (Exception ex) {
               
                throw new GeneralServiceException("00001",
                        "Ocurrio un error en el servicio setEncabezado: " + ex.getMessage());
            }
        }


    private boolean isEndOfPage(BaseTable table) {
      
        float currentY = yStart - table.getHeaderAndDataHeight();   
     
        return  currentY <= bottomMargin;
    }


}
