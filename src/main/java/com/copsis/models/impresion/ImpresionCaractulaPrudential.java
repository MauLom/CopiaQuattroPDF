package com.copsis.models.impresion ;
import java.awt.Color ;
import java.io.ByteArrayOutputStream ;
import java.io.File ;
import java.io.IOException ;
import java.util.ArrayList ;
import java.util.List ;

import org.apache.pdfbox.pdmodel.PDDocument ;
import org.apache.pdfbox.pdmodel.PDPage ;
import org.apache.pdfbox.pdmodel.PDPageContentStream ;
import org.apache.pdfbox.pdmodel.common.PDRectangle ;
import org.apache.pdfbox.pdmodel.font.PDFont ;
import org.apache.pdfbox.pdmodel.font.PDType1Font ;
import org.apache.pdfbox.pdmodel.graphics.color.PDColor;
import org.apache.pdfbox.pdmodel.graphics.color.PDDeviceRGB;

import com.copsis.clients.projections.AseguradosProjection;
import com.copsis.clients.projections.BeneficiarioProjection;
import com.copsis.clients.projections.CaractulaProjection ;
import com.copsis.clients.projections.CoberturaProjection;
import com.copsis.exceptions.GeneralServiceException ;
import com.copsis.models.Tabla.BaseTable ;
import com.copsis.models.Tabla.ImageUtils ;
import com.copsis.models.Tabla.Row ;
import com.copsis.models.Tabla.Sio4CommunsPdf ;
import com.copsis.models.Tabla.VerticalAlignment ;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ImpresionCaractulaPrudential {

    private final Color azul = new Color(68, 114, 196, 0);
    private final Color azulb = new Color(217, 226, 243, 0);
    private float yStartNewPage = 780, yStart = 760, bottomMargin = 32, fullWidth = 549, margin = 31, ytexto = 0;
    private Sio4CommunsPdf communsPdf = new Sio4CommunsPdf();
    private final Color colorLink = new Color(5, 99, 193, 0);
    public byte[] buildPDF(CaractulaProjection datos) {

        try {
            ByteArrayOutputStream output;
            try ( PDDocument document = new PDDocument()) {
                try {
                    PDPage page = new PDPage();
                    document.addPage(page);
                    BaseTable table;
                    Row<PDPage> baseRow;
                    StringBuilder texto = new StringBuilder();
                    this.setFooter(document, page);

                    table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, margin, document, page, true, true);
                    baseRow = communsPdf.setRow(table);
                    communsPdf.setCell(baseRow, 100, "DATOS DE LA PÓLIZA", Color.BLACK, true, "L", 10, communsPdf.setLineStyle(Color.white, Color.white, Color.black, Color.black), "", communsPdf.setPadding2(5f, 0f, 3f, 0f), azulb);
                    baseRow = communsPdf.setRow(table);
                    communsPdf.setCell(baseRow, 33, "Número de póliza", Color.BLACK, true, "L", 10, communsPdf.setLineStyle(Color.white, Color.black, Color.white, Color.black), "", communsPdf.setPadding2(5f, 0f, 3f, 0f), Color.white);
                    communsPdf.setCell(baseRow, 33, "Inicio de vigencia de la póliza", Color.BLACK, true, "L", 10, communsPdf.setLineStyle(Color.black, Color.black, Color.white, Color.black), "", communsPdf.setPadding2(5f, 0f, 3f, 0f), Color.white);
                    communsPdf.setCell(baseRow, 34, "Fin de vigencia de la póliza", Color.BLACK, true, "L", 10, communsPdf.setLineStyle(Color.black, Color.white, Color.white, Color.black), "", communsPdf.setPadding2(5f, 0f, 3f, 0f), Color.white);
                    baseRow = communsPdf.setRow(table);
                    communsPdf.setCell(baseRow, 33, datos.getNumeroPoliza(), Color.BLACK, false, "L", 10, communsPdf.setLineStyle(Color.white, Color.black, Color.black, Color.black), "", communsPdf.setPadding2(5f, 0f, 3f, 0f), Color.white);
                    communsPdf.setCell(baseRow, 33, datos.getFechaDesde(), Color.BLACK, false, "L", 10, communsPdf.setLineStyle(Color.black, Color.black, Color.black, Color.black), "", communsPdf.setPadding2(5f, 0f, 3f, 0f), Color.white);
                    communsPdf.setCell(baseRow, 34, datos.getFechaHasta(), Color.BLACK, false, "L", 10, communsPdf.setLineStyle(Color.black, Color.white, Color.black, Color.black), "", communsPdf.setPadding2(5f, 0f, 3f, 0f), Color.white);
                    baseRow = communsPdf.setRow(table);
                    communsPdf.setCell(baseRow, 33, "Fecha de emisión", Color.BLACK, true, "L", 10, communsPdf.setLineStyle(Color.white, Color.black, Color.white, Color.black), "", communsPdf.setPadding2(5f, 0f, 3f, 0f), Color.white);
                    communsPdf.setCell(baseRow, 33, "Moneda", Color.BLACK, true, "L", 10, communsPdf.setLineStyle(Color.black, Color.black, Color.white, Color.black), "", communsPdf.setPadding2(5f, 0f, 3f, 0f), Color.white);
                    communsPdf.setCell(baseRow, 34, "Forma de pago", Color.BLACK, true, "L", 10, communsPdf.setLineStyle(Color.black, Color.white, Color.white, Color.black), "", communsPdf.setPadding2(5f, 0f, 3f, 0f), Color.white);
                    baseRow = communsPdf.setRow(table);
                    communsPdf.setCell(baseRow, 33, datos.getFechaEmision(), Color.BLACK, false, "L", 10, communsPdf.setLineStyle(Color.white, Color.black, Color.black, Color.black), "", communsPdf.setPadding2(5f, 0f, 3f, 0f), Color.white);
                    communsPdf.setCell(baseRow, 33, datos.getMmoneda(), Color.BLACK, false, "L", 10, communsPdf.setLineStyle(Color.black, Color.black, Color.black, Color.black), "", communsPdf.setPadding2(5f, 0f, 3f, 0f), Color.white);
                    communsPdf.setCell(baseRow, 34, datos.getFormaPago(), Color.BLACK, false, "L", 10, communsPdf.setLineStyle(Color.black, Color.white, Color.black, Color.black), "", communsPdf.setPadding2(5f, 0f, 3f, 0f), Color.white);
                    table.setCellCallH(true);
                    table.draw();
                    yStart -= table.getHeaderAndDataHeight() + 25;

                    table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, margin, document, page, true, true);
                    baseRow = communsPdf.setRow(table);
                    communsPdf.setCell(baseRow, 100, "DATOS DEL CONTRATANTE", Color.BLACK, true, "L", 10, communsPdf.setLineStyle(Color.white, Color.white, Color.black, Color.black), "", communsPdf.setPadding2(5f, 0f, 3f, 0f), azulb);
                    baseRow = communsPdf.setRow(table);
                    communsPdf.setCell(baseRow, 50, "Nombre o Razón Social", Color.BLACK, true, "L", 10, communsPdf.setLineStyle(Color.white, Color.black, Color.white, Color.black), "", communsPdf.setPadding2(5f, 0f, 3f, 0f), Color.white);
                    communsPdf.setCell(baseRow, 23, "R.F.C.", Color.BLACK, true, "L", 10, communsPdf.setLineStyle(Color.black, Color.black, Color.white, Color.black), "", communsPdf.setPadding2(5f, 0f, 3f, 0f), Color.white);
                    communsPdf.setCell(baseRow, 27, "Teléfono (Incluyendo Lada)", Color.BLACK, true, "L", 10, communsPdf.setLineStyle(Color.black, Color.white, Color.white, Color.black), "", communsPdf.setPadding2(5f, 0f, 3f, 0f), Color.white);
                    baseRow = communsPdf.setRow(table);
                    communsPdf.setCell(baseRow, 50, datos.getNombre(), Color.BLACK, false, "L", 10, communsPdf.setLineStyle(Color.white, Color.black, Color.black, Color.black), "", communsPdf.setPadding2(5f, 0f, 3f, 0f), Color.white);
                    communsPdf.setCell(baseRow, 23, datos.getRfc(), Color.BLACK, false, "L", 10, communsPdf.setLineStyle(Color.black, Color.black, Color.black, Color.black), "", communsPdf.setPadding2(5f, 0f, 3f, 0f), Color.white);
                    communsPdf.setCell(baseRow, 27, datos.getTelefono(), Color.BLACK, false, "L", 10, communsPdf.setLineStyle(Color.black, Color.white, Color.black, Color.black), "", communsPdf.setPadding2(5f, 0f, 3f, 0f), Color.white);
                    baseRow = communsPdf.setRow(table);
                    communsPdf.setCell(baseRow, 73, "Domicilio (Calle, Número)", Color.BLACK, true, "L", 10, communsPdf.setLineStyle(Color.white, Color.black, Color.white, Color.black), "", communsPdf.setPadding2(5f, 0f, 3f, 0f), Color.white);
                    communsPdf.setCell(baseRow, 27, "Colonia", Color.BLACK, true, "L", 10, communsPdf.setLineStyle(Color.black, Color.white, Color.white, Color.black), "", communsPdf.setPadding2(5f, 0f, 3f, 0f), Color.white);
                    baseRow = communsPdf.setRow(table);
                    communsPdf.setCell(baseRow, 73, datos.getDireccion(), Color.BLACK, false, "L", 10, communsPdf.setLineStyle(Color.white, Color.black, Color.black, Color.black), "", communsPdf.setPadding2(5f, 0f, 3f, 0f), Color.white);
                    communsPdf.setCell(baseRow, 27, datos.getColonia(), Color.BLACK, false, "L", 10, communsPdf.setLineStyle(Color.black, Color.white, Color.black, Color.black), "", communsPdf.setPadding2(5f, 0f, 3f, 0f), Color.white);

                    baseRow = communsPdf.setRow(table);
                    communsPdf.setCell(baseRow, 38, "Población", Color.BLACK, true, "L", 10, communsPdf.setLineStyle(Color.white, Color.black, Color.white, Color.black), "", communsPdf.setPadding2(5f, 0f, 3f, 0f), Color.white);
                    communsPdf.setCell(baseRow, 35, "Nacionalidad", Color.BLACK, true, "L", 10, communsPdf.setLineStyle(Color.black, Color.black, Color.white, Color.black), "", communsPdf.setPadding2(5f, 0f, 3f, 0f), Color.white);
                    communsPdf.setCell(baseRow, 27, "Código Postal", Color.BLACK, true, "L", 10, communsPdf.setLineStyle(Color.black, Color.white, Color.white, Color.black), "", communsPdf.setPadding2(5f, 0f, 3f, 0f), Color.white);
                    baseRow = communsPdf.setRow(table);
                    communsPdf.setCell(baseRow, 38, datos.getPoblación(), Color.BLACK, false, "L", 10, communsPdf.setLineStyle(Color.white, Color.black, Color.black, Color.black), "", communsPdf.setPadding2(5f, 0f, 3f, 0f), Color.white);
                    communsPdf.setCell(baseRow, 35, datos.getNacionalidad(), Color.BLACK, false, "L", 10, communsPdf.setLineStyle(Color.black, Color.black, Color.black, Color.black), "", communsPdf.setPadding2(5f, 0f, 3f, 0f), Color.white);
                    communsPdf.setCell(baseRow, 27, datos.getCp(), Color.BLACK, false, "L", 10, communsPdf.setLineStyle(Color.black, Color.white, Color.black, Color.black), "", communsPdf.setPadding2(5f, 0f, 3f, 0f), Color.white);
                    table.setCellCallH(true);
                    table.draw();

                    yStart -= table.getHeaderAndDataHeight() + 28;

                    AseguradosProjection asegurado = datos.getAsegurado();

                    table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, margin, document, page, true, true);
                    baseRow = communsPdf.setRow(table);
                    communsPdf.setCell(baseRow, 100, "DATOS DEL ASEGURADO", Color.BLACK, true, "L", 10, communsPdf.setLineStyle(Color.white, Color.white, Color.black, Color.black), "", communsPdf.setPadding2(5f, 0f, 3f, 0f), azulb);
                    baseRow = communsPdf.setRow(table);
                    communsPdf.setCell(baseRow, 50, "Nombre o Razón Social", Color.BLACK, true, "L", 10, communsPdf.setLineStyle(Color.white, Color.black, Color.white, Color.black), "", communsPdf.setPadding2(5f, 0f, 3f, 0f), Color.white);
                    communsPdf.setCell(baseRow, 23, "R.F.C.", Color.BLACK, true, "L", 10, communsPdf.setLineStyle(Color.black, Color.black, Color.white, Color.black), "", communsPdf.setPadding2(5f, 0f, 3f, 0f), Color.white);
                    communsPdf.setCell(baseRow, 27, "Teléfono (Incluyendo Lada)a", Color.BLACK, true, "L", 10, communsPdf.setLineStyle(Color.black, Color.white, Color.white, Color.black), "", communsPdf.setPadding2(5f, 0f, 3f, 0f), Color.white);
                    baseRow = communsPdf.setRow(table);
                    communsPdf.setCell(baseRow, 50, asegurado.getNombre(), Color.BLACK, false, "L", 10, communsPdf.setLineStyle(Color.white, Color.black, Color.black, Color.black), "", communsPdf.setPadding2(5f, 0f, 3f, 0f), Color.white);
                    communsPdf.setCell(baseRow, 23, asegurado.getRfc(), Color.BLACK, false, "L", 10, communsPdf.setLineStyle(Color.black, Color.black, Color.black, Color.black), "", communsPdf.setPadding2(5f, 0f, 3f, 0f), Color.white);
                    communsPdf.setCell(baseRow, 27, asegurado.getTelefono(), Color.BLACK, false, "L", 10, communsPdf.setLineStyle(Color.black, Color.white, Color.black, Color.black), "", communsPdf.setPadding2(5f, 0f, 3f, 0f), Color.white);
                    baseRow = communsPdf.setRow(table);
                    communsPdf.setCell(baseRow, 38, "Fecha de Nacimiento", Color.BLACK, true, "L", 10, communsPdf.setLineStyle(Color.white, Color.black, Color.white, Color.black), "", communsPdf.setPadding2(5f, 0f, 3f, 0f), Color.white);
                    communsPdf.setCell(baseRow, 35, "Edad", Color.BLACK, true, "L", 10, communsPdf.setLineStyle(Color.black, Color.black, Color.white, Color.black), "", communsPdf.setPadding2(5f, 0f, 3f, 0f), Color.white);
                    communsPdf.setCell(baseRow, 27, "Sexo", Color.BLACK, true, "L", 10, communsPdf.setLineStyle(Color.black, Color.white, Color.white, Color.black), "", communsPdf.setPadding2(5f, 0f, 3f, 0f), Color.white);
                    baseRow = communsPdf.setRow(table);
                    communsPdf.setCell(baseRow, 38, asegurado.getFechNacimiento(), Color.BLACK, false, "L", 10, communsPdf.setLineStyle(Color.white, Color.black, Color.black, Color.black), "", communsPdf.setPadding2(5f, 0f, 3f, 0f), Color.white);
                    communsPdf.setCell(baseRow, 35, asegurado.getEdad(), Color.BLACK, false, "L", 10, communsPdf.setLineStyle(Color.black, Color.black, Color.black, Color.black), "", communsPdf.setPadding2(5f, 0f, 3f, 0f), Color.white);
                    communsPdf.setCell(baseRow, 27, asegurado.getSexo(), Color.BLACK, false, "L", 10, communsPdf.setLineStyle(Color.black, Color.white, Color.black, Color.black), "", communsPdf.setPadding2(5f, 0f, 3f, 0f), Color.white);
                    table.draw();

                    yStart -= table.getHeaderAndDataHeight() + 28;
                    table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, margin, document, page, true, true);
                    baseRow = communsPdf.setRow(table);
                    communsPdf.setCell(baseRow, 100, "COBERTURAS AMPARADAS", Color.BLACK, true, "L", 10, communsPdf.setLineStyle(Color.white, Color.white, Color.black, Color.black), "", communsPdf.setPadding2(5f, 0f, 3f, 0f), azulb);
                    
                    baseRow = communsPdf.setRow(table);
                    communsPdf.setCell(baseRow, 37, "Nombre de la Cobertura", Color.BLACK, true, "C", 10, communsPdf.setLineStyle(Color.white, Color.black, Color.black, Color.black), "", communsPdf.setPadding2(5f, 0f, 3f, 0f), Color.white);
                    communsPdf.setCell(baseRow, 63, "Suma Asegurada", Color.BLACK, true, "C", 10, communsPdf.setLineStyle(Color.black, Color.white, Color.black, Color.black), "", communsPdf.setPadding2(5f, 0f, 3f, 0f), Color.white);
                    table.draw();
                    List<CoberturaProjection>  cbo = datos.getCoberturas();
                    if(!cbo.isEmpty()){                    
                    yStart -= table.getHeaderAndDataHeight();
                    table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, margin, document, page, true, true);
                    int ccb=0;
                    for (int i = 0; i < cbo.size();i++){ 
                        ccb++;
                         if(ccb == cbo.size()) {
                            baseRow = communsPdf.setRow(table);
                            communsPdf.setCell(baseRow, 37, cbo.get(i).getNombres(), Color.BLACK, false, "L", 10, communsPdf.setLineStyle(Color.white, Color.black, Color.black, Color.black), "", communsPdf.setPadding2(5f, 0f, 3f, 0f), Color.white);
                            communsPdf.setCell(baseRow, 63, cbo.get(i).getSa(), Color.BLACK, false, "C", 10, communsPdf.setLineStyle(Color.black, Color.white, Color.black, Color.black), "", communsPdf.setPadding2(5f, 0f, 3f, 0f), Color.white);                       
                         }else{
                            baseRow = communsPdf.setRow(table);
                            communsPdf.setCell(baseRow, 37,cbo.get(i).getNombres() , Color.BLACK, false, "L", 10, communsPdf.setLineStyle(Color.white, Color.black, Color.white, Color.black), "", communsPdf.setPadding2(5f, 0f, 3f, 0f), Color.white);
                            communsPdf.setCell(baseRow, 63, cbo.get(i).getSa(), Color.BLACK, false, "C", 10, communsPdf.setLineStyle(Color.black, Color.white, Color.white, Color.black), "", communsPdf.setPadding2(5f, 0f, 3f, 0f), Color.white);
                       
                         }                 
                          
                    }
                    table.setCellCallH(true);
                    table.draw();
                }
                    

                    yStart -= table.getHeaderAndDataHeight() + 28;
                    table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, margin, document, page, true, true);
                    baseRow = communsPdf.setRow(table, 25);
                    communsPdf.setCell(baseRow, 20, "Prima Neta Anual", Color.BLACK, true, "C", 10, communsPdf.setLineStyle(Color.white, Color.black, Color.black, Color.black), "", communsPdf.setPadding2(5f, 0f, 3f, 0f), Color.white);
                    communsPdf.setCell(baseRow, 20, "I.V.A", Color.BLACK, true, "C", 10, communsPdf.setLineStyle(Color.black, Color.black, Color.black, Color.black), "", communsPdf.setPadding2(5f, 0f, 3f, 0f), Color.white);
                    communsPdf.setCell(baseRow, 20, "Derecho de Póliza", Color.BLACK, true, "C", 10, communsPdf.setLineStyle(Color.black, Color.black, Color.black, Color.black), "", communsPdf.setPadding2(5f, 0f, 3f, 0f), Color.white);
                    communsPdf.setCell(baseRow, 20, "Recargo por pago fraccionado", Color.BLACK, true, "C", 10, communsPdf.setLineStyle(Color.black, Color.black, Color.black, Color.black), "", communsPdf.setPadding2(5f, 0f, 3f, 0f), Color.white);
                    communsPdf.setCell(baseRow, 20, "Prima Total", Color.BLACK, true, "C", 10, communsPdf.setLineStyle(Color.black, Color.white, Color.black, Color.black), "", communsPdf.setPadding2(5f, 0f, 3f, 0f), Color.white);
                    baseRow = communsPdf.setRow(table, 25);
                    communsPdf.setCell(baseRow, 20, datos.getPrimaNeta(), Color.BLACK, false, "L", 10, communsPdf.setLineStyle(Color.white, Color.black, Color.black, Color.black), "", communsPdf.setPadding2(5f, 0f, 3f, 0f), Color.white);
                    communsPdf.setCell(baseRow, 20, datos.getIva(), Color.BLACK, false, "L", 10, communsPdf.setLineStyle(Color.black, Color.black, Color.black, Color.black), "", communsPdf.setPadding2(5f, 0f, 3f, 0f), Color.white);
                    communsPdf.setCell(baseRow, 20, datos.getDerecho(), Color.BLACK, false, "L", 10, communsPdf.setLineStyle(Color.black, Color.black, Color.black, Color.black), "", communsPdf.setPadding2(5f, 0f, 3f, 0f), Color.white);
                    communsPdf.setCell(baseRow, 20, datos.getRecargo(), Color.BLACK, false, "L", 10, communsPdf.setLineStyle(Color.black, Color.black, Color.black, Color.black), "", communsPdf.setPadding2(5f, 0f, 3f, 0f), Color.white);
                    communsPdf.setCell(baseRow, 20, datos.getPrimaTotal(), Color.BLACK, false, "L", 10, communsPdf.setLineStyle(Color.black, Color.white, Color.black, Color.black), "", communsPdf.setPadding2(5f, 0f, 3f, 0f), Color.white);
                    table.draw();

                    yStart -= table.getHeaderAndDataHeight() + 60;
                    PDPageContentStream con1 = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, true);
                    communsPdf.drawBox(con1, Color.black, 25, yStart-40, 552, 0.5f);
                    con1.close();

                //     ///DESARRROLO DE PAGINA NUMERO 2
                    page = new PDPage();
                    document.addPage(page);
                    this.setFooter(document, page);

                    table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, margin, document, page, true, true);
                    baseRow = communsPdf.setRow(table);
                    communsPdf.setCell(baseRow, 100, "BENEFICIARIOS", Color.BLACK, true, "L", 10, communsPdf.setLineStyle(Color.white, Color.white, Color.black, Color.black), "", communsPdf.setPadding2(5f, 0f, 3f, 0f), azulb);
                    table.draw();
                    yStart -= table.getHeaderAndDataHeight();

                    Float tb = page.getMediaBox().getHeight() - yStart + 12;

                    texto = new StringBuilder();
                    texto.append("ADVERTENCIA:");
                    this.parrafo(document, page, this.medidas(page.getMediaBox(), 32f, tb), Sio4CommunsPdf.eliminaHtmlTags3(texto.toString()), 547, PDType1Font.HELVETICA_BOLD, 12f, (-1.6f * 9f), 1f, 0.2f);
                    texto = new StringBuilder();
                    texto.append("En el caso de que se desee nombrar beneficiarios a menores de edad, no se");
                    this.parrafo(document, page, this.medidas(page.getMediaBox(), 129f, tb), Sio4CommunsPdf.eliminaHtmlTags3(texto.toString()), 547, PDType1Font.HELVETICA_BOLD, 12f, (-1.6f * 9f), 1f, 0.2f);

                    tb = tb + 14;

                    texto = new StringBuilder();
                    texto.append("debe señalar a un mayor de edad como representante de los menores para efecto de que, en ");
                    texto.append("su representación, cobre la indemnización. Lo anterior porque las legislaciones civiles");
                    texto.append("previenen la forma en que deben designarse tutores, albaceas, representantes de herederos u");
                    texto.append("otros cargos similares y no consideran al contrato de seguro como el instrumento adecuado");
                    texto.append("para tales designaciones. La designación que se hiciera de un mayor de edad como");
                    texto.append("representante de menores beneficiarios, durante la minoría de edad de ellos, legalmente puede");
                    texto.append("implicar que se nombre beneficiario al mayor de edad, quien en todo caso sólo tendría una");
                    texto.append("obligación moral, pues la designación que se hace de beneficiarios en un contrato de seguro");
                    texto.append("le concede el derecho incondicionado de disponer de la suma asegurada.");
                    this.parrafo(document, page, this.medidas(page.getMediaBox(), 32f, tb), Sio4CommunsPdf.eliminaHtmlTags3(texto.toString()), 547, PDType1Font.HELVETICA_BOLD, 12f, (-1.6f * 9f), 1f, 0.2f);
                    tb = tb + 110;

                    yStart = page.getMediaBox().getHeight() - tb;

                    yStart -= table.getHeaderAndDataHeight();
                    table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, margin, document, page, true, true);
                    baseRow = communsPdf.setRow(table, 32);
                    communsPdf.setCell(baseRow, 13, "Número de Beneficiario", Color.BLACK, true, "L", 10, communsPdf.setLineStyle(Color.black, Color.black, Color.black, Color.black), "", communsPdf.setPadding2(5f, 0f, 3f, 0f), azulb).setValign(VerticalAlignment.MIDDLE);
                    communsPdf.setCell(baseRow, 31, "Nombre del Beneficiario", Color.BLACK, true, "C", 10, communsPdf.setLineStyle(Color.black, Color.black, Color.black, Color.black), "", communsPdf.setPadding2(5f, 0f, 3f, 0f), azulb).setValign(VerticalAlignment.MIDDLE);
                    communsPdf.setCell(baseRow, 18, "Fecha de nacimiento del Beneficiario", Color.BLACK, true, "C", 10, communsPdf.setLineStyle(Color.black, Color.black, Color.black, Color.black), "", communsPdf.setPadding2(5f, 0f, 3f, 0f), azulb).setValign(VerticalAlignment.MIDDLE);
                    communsPdf.setCell(baseRow, 19, "Parentesco o relación con el solicitante", Color.BLACK, true, "C", 10, communsPdf.setLineStyle(Color.black, Color.black, Color.black, Color.black), "", communsPdf.setPadding2(5f, 0f, 3f, 0f), azulb).setValign(VerticalAlignment.MIDDLE);
                    communsPdf.setCell(baseRow, 19, "Porcentaje", Color.BLACK, true, "C", 10, communsPdf.setLineStyle(Color.black, Color.black, Color.black, Color.black), "", communsPdf.setPadding2(5f, 0f, 3f, 0f), azulb).setValign(VerticalAlignment.MIDDLE);
                    table.draw();

                    yStart -= table.getHeaderAndDataHeight();
                    
                    List<BeneficiarioProjection> bene =datos.getBeneficiarios();
                    if(!bene.isEmpty()){
                    table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, margin, document, page, true, true);
                    int c = 0;
                    for (int i = 0; i < bene.size(); i++) {
                        c++;
                        baseRow = communsPdf.setRow(table, 20);
                        communsPdf.setCell(baseRow, 13, c + "", Color.BLACK, false, "C", 10, communsPdf.setLineStyle(Color.black, Color.black, Color.black, Color.black), "", communsPdf.setPadding2(5f, 0f, 3f, 0f), Color.white).setValign(VerticalAlignment.MIDDLE);
                        communsPdf.setCell(baseRow, 31, bene.get(i).getNombres() !=null ?  bene.get(i).getNombres():"", Color.BLACK, false, "L", 10, communsPdf.setLineStyle(Color.black, Color.black, Color.black, Color.black), "", communsPdf.setPadding2(5f, 0f, 3f, 0f), Color.white).setValign(VerticalAlignment.MIDDLE);
                        communsPdf.setCell(baseRow, 18, bene.get(i).getFecNacimiento()!=null ? bene.get(i).getFecNacimiento():"", Color.BLACK, false, "C", 10, communsPdf.setLineStyle(Color.black, Color.black, Color.black, Color.black), "", communsPdf.setPadding2(5f, 0f, 3f, 0f), Color.white).setValign(VerticalAlignment.MIDDLE);
                        communsPdf.setCell(baseRow, 19, bene.get(i).getParentesco()!=null? bene.get(i).getParentesco():"" , Color.BLACK, false, "C", 10, communsPdf.setLineStyle(Color.black, Color.black, Color.black, Color.black), "", communsPdf.setPadding2(5f, 0f, 3f, 0f), Color.white).setValign(VerticalAlignment.MIDDLE);
                        communsPdf.setCell(baseRow, 19, bene.get(i).getPorcentaje().toString()!=null? bene.get(i).getPorcentaje().toString():"" , Color.BLACK, false, "C", 10, communsPdf.setLineStyle(Color.black, Color.black, Color.black, Color.black), "", communsPdf.setPadding2(5f, 0f, 3f, 0f), Color.white).setValign(VerticalAlignment.MIDDLE);
                    }
                    table.draw();
                  }

                    yStart -= table.getHeaderAndDataHeight() + 30;

                    table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, margin, document, page, true, true);
                    baseRow = communsPdf.setRow(table);
                    communsPdf.setCell(baseRow, 100, "AVISO DE PRIVACIDAD CORTO", Color.BLACK, true, "L", 10, communsPdf.setLineStyle(Color.white, Color.white, Color.black, Color.black), "", communsPdf.setPadding2(5f, 0f, 3f, 0f), azulb);
                    table.draw();

                    tb = page.getMediaBox().getHeight() - yStart + 25;
                    texto = new StringBuilder();
                    texto.append("Los Datos Personales que nos proporcione serán tratados por Prudential Seguros México, S.A. de C.V., con domicilio en ");
                    texto.append("Av. Santa Fe 428, piso 7, DownTown Torre II, Col. Santa Fe Cuajimalpa, Cuajimalpa de Morelos, Ciudad de México, C.P. ");
                    texto.append("05348, con la finalidad primaria y necesaria de contactarle y/o dar seguimiento a sus solicitudes respecto de los productos,");
                    texto.append("servicios y/o actividad comercial de Prudential. Para conocer nuestros Aviso de Privacidad integral visite");
                    this.parrafo(document, page, this.medidas(page.getMediaBox(), 35f, tb), Sio4CommunsPdf.eliminaHtmlTags3(texto.toString()), 547, PDType1Font.HELVETICA, 10f, (-1.3f * 9f), 1f, 0.2f);
                    tb = tb + 53;

                    yStart = page.getMediaBox().getHeight() - tb;
                    try (PDPageContentStream contentStream = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.PREPEND, true, true)) {                
                       float[] components = new float[] {
                        colorLink.getRed() / 255f, colorLink.getGreen() / 255f, colorLink.getBlue() / 255f };
                        PDColor color = new PDColor(components, PDDeviceRGB.INSTANCE);
                        String txtlink="https://www.prudentialseguros.com.mx/PrudentialMexico/aviso-de-privacidad";
                        communsPdf.getTextlink(contentStream, page,35f ,(yStart+25), color, 10, txtlink, true, txtlink, txtlink, true);    
                    }

                    
                   


                    table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, margin, document, page, true, true);
                    baseRow = communsPdf.setRow(table);
                    communsPdf.setCell(baseRow, 100, "DE INTERÉS PARA EL CONTRATANTE", Color.BLACK, true, "L", 10, communsPdf.setLineStyle(Color.white, Color.white, Color.black, Color.black), "", communsPdf.setPadding2(5f, 0f, 3f, 0f), azulb);
                    table.draw();
                    yStart -= table.getHeaderAndDataHeight();
                   

                    tb = page.getMediaBox().getHeight() - yStart + 10;
                    texto = new StringBuilder();
                    texto.append("Si el contenido de la póliza o sus modificaciones no concordaren con la oferta, el asegurado podrá pedir la ");                    
                    texto.append("rectificación correspondiente dentro de los treinta días que sigan al día en que reciba la póliza. Transcurrido este ");                    
                    texto.append("plazo, se considerarán aceptadas las estipulaciones de la póliza o de sus modificaciones (Art.25 Ley Sobre el");
                    texto.append("Contrato de Seguro).");
                    this.parrafo(document, page, this.medidas(page.getMediaBox(), 35f, tb), Sio4CommunsPdf.eliminaHtmlTags3(texto.toString()), 547, PDType1Font.HELVETICA_BOLD, 10f, (-1.3f * 9f), 1f, 0.2f);
                  

                    tb = tb+60;
                    texto = new StringBuilder();
                    texto.append("Le recordamos que el producto contiene exclusiones y/o limitantes de cobertura, los cuales ");                    
                    texto.append("puede consultar en las respectivas condiciones generales, disponibles en nuestra página web: ");                    
                    texto.append("www.prudentialseguros.com.mx");
                    
                    this.parrafo(document, page, this.medidas(page.getMediaBox(), 35f, tb), Sio4CommunsPdf.eliminaHtmlTags3(texto.toString()), 547, PDType1Font.HELVETICA_BOLD, 12f, (-1.5f * 9f), 1f, 0.2f);
                  

                    tb = tb + 30;
                    yStart = page.getMediaBox().getHeight() - tb;

                    table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, margin, document, page, true, true);
                    baseRow = communsPdf.setRow(table);
                    communsPdf.setCell(baseRow, 100, "UNIDAD ESPECIALIZADA DE ATENCIÓN DE CONSULTAS Y RECLAMACIONES (UNE)", Color.BLACK, true, "L", 10, communsPdf.setLineStyle(Color.white, Color.white, Color.black, Color.black), "", communsPdf.setPadding2(5f, 0f, 3f, 0f), azulb);
                    table.draw();
                    yStart -= table.getHeaderAndDataHeight();


                    tb = page.getMediaBox().getHeight() - yStart + 10;
                    texto = new StringBuilder();
                    texto.append("Para cualquier aclaración o duda no resueltas en relación con su seguro, contacte a la Unidad Especializada de nuestra ");                    
                    texto.append("Compañía a los teléfonos 55 1103 7000 y en el interior de la república al 800-000-54-33, y/o al correo electrónico ");                    
                    this.parrafo(document, page, this.medidas(page.getMediaBox(), 35f, tb), Sio4CommunsPdf.eliminaHtmlTags3(texto.toString()), 547, PDType1Font.HELVETICA, 10f, (-1.3f * 9f), 1f, 0.3f);
                  
                    
                    tb = tb + 27;
                    yStart = page.getMediaBox().getHeight() - tb;
                    try (PDPageContentStream contentStream1 = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.PREPEND, true, true)) {                
                        float[] components = new float[] {
                         colorLink.getRed() / 255f, colorLink.getGreen() / 255f, colorLink.getBlue() / 255f };
                         PDColor color = new PDColor(components, PDDeviceRGB.INSTANCE);
                         String txtlink="quejasyconsultas@prudential.com";
                         communsPdf.getTextlink(contentStream1, page,35f ,(yStart+25), color, 10, txtlink, true, txtlink, txtlink, true);    
                     }

                     try (PDPageContentStream contentStream2 = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.PREPEND, true, true)) {                
                        float[] components = new float[] {
                         colorLink.getRed() / 255f, colorLink.getGreen() / 255f, colorLink.getBlue() / 255f };
                         PDColor color = new PDColor(components, PDDeviceRGB.INSTANCE);
                         String txtlink="www.prudentialseguros.com.mx";
                         communsPdf.getTextlink(contentStream2, page,230f ,(yStart+25), color, 10, txtlink, true, txtlink, txtlink, true);    
                     }
                     texto = new StringBuilder();
                     texto.append("o visite:");                                        
                     this.parrafo(document, page, this.medidas(page.getMediaBox(), 190f, tb-5), Sio4CommunsPdf.eliminaHtmlTags3(texto.toString()), 547, PDType1Font.HELVETICA, 10.2f, (-1.3f * 9f), 1f, 0.3f);
                   
                     

                    table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, margin, document, page, true, true);
                    baseRow = communsPdf.setRow(table);
                    communsPdf.setCell(baseRow, 100, "DATOS DE LA CONDUSEF", Color.BLACK, true, "L", 10, communsPdf.setLineStyle(Color.white, Color.white, Color.black, Color.black), "", communsPdf.setPadding2(5f, 0f, 3f, 0f), azulb);
                    table.draw();
                    yStart -= table.getHeaderAndDataHeight();

                    tb = page.getMediaBox().getHeight() - yStart + 10;
                    texto = new StringBuilder();
                    texto.append("Av. Insurgentes Sur # 762, planta baja, Col. Del Valle, Alc. Benito Juárez, C.P.03100, México, Cd. de México");                                        
                    this.parrafo(document, page, this.medidas(page.getMediaBox(), 43f, tb), Sio4CommunsPdf.eliminaHtmlTags3(texto.toString()), 547, PDType1Font.HELVETICA, 10f, (-1.3f * 9f), 1f, 0.3f);
                  

                    tb = tb+16;
                    yStart = page.getMediaBox().getHeight() - tb;
                    texto = new StringBuilder();
                    texto.append("Correo Electrónico:");                                        
                    this.parrafo(document, page, this.medidas(page.getMediaBox(), 43f, tb-5), Sio4CommunsPdf.eliminaHtmlTags3(texto.toString()), 547, PDType1Font.HELVETICA, 10f, (-1.3f * 9f), 1f, 0.3f);
                    try (PDPageContentStream contentStream3 = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.PREPEND, true, true)) {                
                        float[] components = new float[] {
                         colorLink.getRed() / 255f, colorLink.getGreen() / 255f, colorLink.getBlue() / 255f };
                         PDColor color = new PDColor(components, PDDeviceRGB.INSTANCE);
                         String txtlink="asesoria@condusef.gob.mx,";
                         communsPdf.getTextlink(contentStream3, page,138f ,(yStart+25), color, 10, txtlink, true, txtlink, txtlink, true);    
                     }

                    tb = tb+10;
                    yStart = page.getMediaBox().getHeight() - tb;
                    texto = new StringBuilder();
                    texto.append("Página web:");
                    this.parrafo(document, page, this.medidas(page.getMediaBox(), 43f, tb-5), Sio4CommunsPdf.eliminaHtmlTags3(texto.toString()), 547, PDType1Font.HELVETICA, 10f, (-1.3f * 9f), 1f, 0.3f);        
                    
                    tb = page.getMediaBox().getHeight() - yStart + 8;
                    try (PDPageContentStream contentStream3 = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.PREPEND, true, true)) {                
                        float[] components = new float[] {
                         colorLink.getRed() / 255f, colorLink.getGreen() / 255f, colorLink.getBlue() / 255f };
                         PDColor color = new PDColor(components, PDDeviceRGB.INSTANCE);
                         String txtlink="http://www.condusef.gob.mx";
                         communsPdf.getTextlink(contentStream3, page,107f ,(yStart+25), color, 10, txtlink, true, txtlink, txtlink, true);    
                     }

                     tb = tb+1;
                    texto = new StringBuilder();
                    texto.append("Teléfonos: 800 999 8080 o 55 5340 0999");                                        
                    this.parrafo(document, page, this.medidas(page.getMediaBox(), 43f, tb), Sio4CommunsPdf.eliminaHtmlTags3(texto.toString()), 547, PDType1Font.HELVETICA, 10f, (-1.3f * 9f), 1f, 0.3f);
                    
                    /////////////////////////////////////////////////////PAGINA 3
                    page = new PDPage();
                    document.addPage(page);
                    this.setFooter(document, page);

                    PDPageContentStream content01 = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, true);
                    communsPdf.drawBox(content01, Color.black, 25, yStart-2, 552, 0.5f);
                    content01.close();



                    PDPageContentStream content02 = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, true);
                    communsPdf.drawBox(content02, Color.black, 25, yStart-40, 552, 0.5f);
                    content02.close();

                    //LINEA 
                    // table = new BaseTable(yStart, yStartNewPage, bottomMargin, 552, 28, document, page, true, true);
                    // baseRow = communsPdf.setRow(table, 2);
                    // communsPdf.setCell(baseRow, 100, "", Color.BLACK, false, "L", 1, communsPdf.setLineStyle(Color.white, Color.white, Color.white, Color.black), "", communsPdf.setPadding2(0f, 5f, 0f, 0f), Color.black);
                    // table.setCellCallH(true);
                    // table.draw();
                    yStart -= table.getHeaderAndDataHeight() + 38;

                    table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, margin, document, page, true, true);
                    baseRow = communsPdf.setRow(table, 13);
                    communsPdf.setCell(baseRow, 50, "Fecha de Emisión", Color.BLACK, false, "C", 10, communsPdf.setLineStyle(Color.black, Color.black, Color.black, Color.black), "", communsPdf.setPadding2(5f, 0f, 3f, 0f), Color.white).setValign(VerticalAlignment.MIDDLE);
                    communsPdf.setCell(baseRow, 50, "Firma de Funcionario", Color.BLACK, false, "C", 10, communsPdf.setLineStyle(Color.black, Color.black, Color.black, Color.black), "", communsPdf.setPadding2(5f, 0f, 3f, 0f), Color.white).setValign(VerticalAlignment.MIDDLE);
                    baseRow = communsPdf.setRow(table, 40);
                    communsPdf.setCell(baseRow, 50, "", Color.BLACK, false, "C", 10, communsPdf.setLineStyle(Color.black, Color.black, Color.black, Color.black), "", communsPdf.setPadding2(5f, 0f, 3f, 0f), Color.white).setValign(VerticalAlignment.MIDDLE);
                    communsPdf.setCell(baseRow, 50, "", Color.BLACK, false, "C", 10, communsPdf.setLineStyle(Color.black, Color.black, Color.black, Color.black), "", communsPdf.setPadding2(5f, 0f, 3f, 0f), Color.white).setValign(VerticalAlignment.MIDDLE);                                    
                    table.draw();

                    yStart -= table.getHeaderAndDataHeight();

                    tb = page.getMediaBox().getHeight() - yStart + 30;
                    texto = new StringBuilder();
                    texto.append("“En cumplimiento a lo dispuesto en el artículo 202 de la Ley de Instituciones de Seguros y de Fianzas,");
                    texto.append("la documentación contractual y la nota técnica que integran este producto de seguro, quedaron");
                    texto.append("registradas ante la Comisión Nacional de Seguros y Fianzas, a partir del día xx de xxxx de xxxx, con el");
                    texto.append("número xxxxxxx/xxxxxxx“.");
                    this.parrafo(document, page, this.medidas(page.getMediaBox(), 32f, tb), Sio4CommunsPdf.eliminaHtmlTags3(texto.toString()), 547, PDType1Font.HELVETICA, 12f, (-1.5f * 9f), 1f, 0.2f);
                  
                   
                     
                    
                    


                    output = new ByteArrayOutputStream();
                    document.save(output);
                    //document.save(new File("/home/aalbanil/Vídeos/caractula.pdf"));
                    return output.toByteArray();
                } finally {
                    document.close();
                }

            }

        } catch (Exception ex) {
  ex.printStackTrace();
            throw new GeneralServiceException("00001", "Ocurrio un error en el servicio ImpresionInter: " + ex.getMessage());

        }
    }

    private void setFooter(PDDocument document, PDPage page) {
        try ( PDPageContentStream content = new PDPageContentStream(document, page)) {
            BaseTable table;
            Row<PDPage> baseRow;
            yStart = 760;
            table = new BaseTable((yStart + 10), yStartNewPage, bottomMargin, 120, 25, document, page, false, true);
            baseRow = communsPdf.setRow(table, 15);
            communsPdf.setCell(baseRow, 100,
                    ImageUtils.readImage("https://storage.googleapis.com/quattrocrm-prod/quattro-biibiic/2306/1N7rQflDvq65bN1u4E4VKKEBwkpnGdMv3RzssHtKGc7vz1H5gV6PXBNAlEmXhz/prudential.png"),
                    0, 0, Color.black);
            table.draw();

            table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, margin, document, page, false, true);
            baseRow = communsPdf.setRow(table);
            communsPdf.setCell(baseRow, 100, "PÓLIZA", Color.BLACK, true, "C", 10, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding2(0f, 0f, 3f, 5f), Color.white);
            baseRow = communsPdf.setRow(table);
            communsPdf.setCell(baseRow, 100, "Seguro individual – Hospitalización", Color.BLACK, true, "C", 10, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding2(0f, 5f, 3f, 5f), Color.white);
            table.draw();
            yStart -= table.getHeaderAndDataHeight() + 8;

            table = new BaseTable(yStart, yStartNewPage, bottomMargin, 546, 34, document, page, true, true);
            baseRow = communsPdf.setRow(table, 2);
            communsPdf.setCell(baseRow, 100, "", Color.BLACK, true, "L", 9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding2(0f, 5f, 3f, 5f), azul);
            table.draw();
            yStart -= table.getHeaderAndDataHeight() + 10;

            table = new BaseTable(70, 30, 0, fullWidth, margin+6, document, page, false, true);
            baseRow = communsPdf.setRow(table, 5);
            communsPdf.setCell(baseRow, 100, "Prudential Seguros México, S.A. de C.V | Av. Santa Fe 428 | Piso 7 | DownTown Torre II | Col. Santa Fe Cuajimalpa", Color.BLACK, false, "L", 10, communsPdf.setLineStyle(Color.white, Color.black, Color.white, Color.black), "", communsPdf.setPadding2(5f, 0f, 3f, 0f), Color.white);
            baseRow = communsPdf.setRow(table, 5);
            communsPdf.setCell(baseRow, 100, "| Cuajimalpa de Morelos | Ciudad de México | C.P.05348 | Tel.: (55) 1103-7000 | www.prudentialseguros.com.mx", Color.BLACK, false, "L", 10, communsPdf.setLineStyle(Color.white, Color.black, Color.white, Color.black), "", communsPdf.setPadding2(9f, 0f, 3f, 0f), Color.white);
            table.draw();

        } catch (Exception e) {
            throw new GeneralServiceException("Error=>", e.getMessage());
        }

    }

    private static void addParagraph(PDPageContentStream contentStream, float width, float sx,
            float sy, String text, boolean justify, PDFont FONT, Float FONT_SIZE, Float LEADING, Float SPACING) throws IOException {
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
            PDFont FONT, Float FONT_SIZE, Float LEADING, Float paddig, float SPACING) {
        try {
            PDPageContentStream contentStream = new PDPageContentStream(document, page,
                    PDPageContentStream.AppendMode.APPEND, true);
            contentStream.beginText();
            addParagraph(contentStream, i, lines.get(1) + paddig, lines.get(2), tetxo, true, FONT, FONT_SIZE,
                    LEADING, SPACING);
            contentStream.endText();
            contentStream.close();

            return contentStream;
        } catch (IOException e) {
            return null;
        }
    }

}
