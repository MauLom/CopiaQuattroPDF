package com.copsis.models.impresion;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;

import com.copsis.clients.projections.BeneficiariosAxaProjection;
import com.copsis.clients.projections.CoberturaBenificiosProjection;
import com.copsis.controllers.forms.ImpresionCertificadoAxa;
import com.copsis.exceptions.GeneralServiceException;
import com.copsis.models.Tabla.BaseTable;
import com.copsis.models.Tabla.ImageUtils;
import com.copsis.models.Tabla.Row;
import com.copsis.models.Tabla.Sio4CommunsPdf;
import com.copsis.models.Tabla.VerticalAlignment;

public class ImpresionCertificadoAxaPdf {
    private Sio4CommunsPdf communsPdf = new Sio4CommunsPdf();
    private float margin = 25, yStartNewPage = 780, yStart = 780, bottomMargin = 30;
    private final Color bgColorAb = new Color(203, 193, 230, 0);
    private final Color bgColorA = new Color(0, 0, 143, 0);

    private final Color bgColor = new Color(255, 255, 255, 0);

    private float fullWidth = 556;
   

    public byte[] buildPDF(ImpresionCertificadoAxa certificadoAxa) {
         float yStartPos = 0;
         float yStartPosTb2 = 0;
        ByteArrayOutputStream output;
        try {
            try (PDDocument document = new PDDocument()) {
                try {
                    PDPage page = new PDPage();
                    document.addPage(page);
                    BaseTable table;
                    Row<PDPage> baseRow;
                    BaseTable table2;
                    Row<PDPage> baseRow2;

                     InputStream arialNormal = new URL("https://storage.googleapis.com/quattrocrm-copsis/BiiBiiC/arial.ttf").openStream();
                    InputStream arialBold = new URL("https://storage.googleapis.com/quattrocrm-copsis/BiiBiiC/arialbd.ttf").openStream();                    
                    PDType0Font arialN =  PDType0Font.load(document, arialNormal);
                    PDType0Font arialB =  PDType0Font.load(document, arialBold);

                    this.setEncabezado(document, page);
                    yStartPos= yStart;
                    table = new BaseTable(yStart, yStartNewPage, bottomMargin, 260, 340, document, page, true, true);
                    baseRow = communsPdf.setRow(table, 15);
                    communsPdf.setCell(baseRow, 100, "Póliza", Color.white, true, "C", 10, communsPdf.setLineStyle(bgColorA), "", communsPdf.setPadding(4f, 4f, 4f, 4f), bgColorA);
                    baseRow = communsPdf.setRow(table, 15);
                    communsPdf.setCell(baseRow, 100, certificadoAxa.getNoPoliza(), Color.black, true, "C", 14, communsPdf.setLineStyle(bgColorA,bgColorA,Color.white,Color.white), "", communsPdf.setPadding(4f, 4f, 4f, 4f), bgColor).setValign(VerticalAlignment.MIDDLE);
                    baseRow = communsPdf.setRow(table, 15);
                    communsPdf.setCell(baseRow, 100, "Datos de la Póliza", Color.white, true, "C", 10, communsPdf.setLineStyle(bgColorA), "", communsPdf.setPadding(4f, 4f, 4f, 4f), bgColorA);
         
                    baseRow = communsPdf.setRow(table, 12);
                    communsPdf.setCell(baseRow, 57, "Fecha de inicio de vigencia", Color.black, true, "L", 10, communsPdf.setLineStyle(bgColorA,bgColorA,Color.white,Color.white), "", communsPdf.setPadding(0f, 0f, 4f, 4f), bgColor).setValign(VerticalAlignment.MIDDLE);
                    communsPdf.setCell(baseRow, 43, certificadoAxa.getVigenciaDe(), Color.black, false, "", 10, communsPdf.setLineStyle(bgColorA,bgColorA,Color.white,Color.white), "", communsPdf.setPadding(4f, 4f, 4f, 4f), bgColor).setValign(VerticalAlignment.MIDDLE);
                    baseRow = communsPdf.setRow(table, 12);
                    communsPdf.setCell(baseRow, 57, "Fecha de fin de vigencia", Color.black, true, "L", 10, communsPdf.setLineStyle(bgColorA,bgColorA,Color.white,Color.white), "", communsPdf.setPadding(0f, 0f, 4f, 4f), bgColor).setValign(VerticalAlignment.MIDDLE);
                    communsPdf.setCell(baseRow, 43, certificadoAxa.getVigenciaA(), Color.black, false, "R", 10, communsPdf.setLineStyle(bgColorA,bgColorA,Color.white,Color.white), "", communsPdf.setPadding(4f, 4f, 4f, 4f), bgColor).setValign(VerticalAlignment.MIDDLE);
        
                    baseRow = communsPdf.setRow(table, 12);
                    communsPdf.setCell(baseRow, 57, "Moneda", Color.black, true, "L", 10, communsPdf.setLineStyle(bgColorA,bgColorA,Color.white,Color.white), "", communsPdf.setPadding(0f, 0f, 4f, 4f), bgColor).setValign(VerticalAlignment.MIDDLE);
                    communsPdf.setCell(baseRow, 43, certificadoAxa.getMoneda(), Color.black, false, "R", 10, communsPdf.setLineStyle(bgColorA,bgColorA,Color.white,Color.white), "", communsPdf.setPadding(4f, 4f, 4f, 4f), bgColor).setValign(VerticalAlignment.MIDDLE);
        
                    baseRow = communsPdf.setRow(table, 12);
                    communsPdf.setCell(baseRow, 57, "Participación de la prima", Color.black, true, "L", 10, communsPdf.setLineStyle(bgColorA,Color.white,bgColorA,Color.white), "", communsPdf.setPadding(4f, 4f, 4f, 4f), bgColor).setValign(VerticalAlignment.MIDDLE);
                    communsPdf.setCell(baseRow, 43,"", Color.black, false, "L", 10, communsPdf.setLineStyle(bgColorA,bgColorA,bgColorA,Color.white), "", communsPdf.setPadding(4f, 4f, 4f, 4f), bgColor).setValign(VerticalAlignment.MIDDLE);                   
                    baseRow = communsPdf.setRow(table, 15);
                    communsPdf.setCell(baseRow, 100, "Certificado", Color.white, true, "C", 10, communsPdf.setLineStyle(bgColorA), "", communsPdf.setPadding(4f, 4f, 4f, 4f), bgColorA);
                    baseRow = communsPdf.setRow(table, 15);
                    communsPdf.setCell(baseRow, 100, certificadoAxa.getCertificado(), Color.black, false, "C", 10, communsPdf.setLineStyle(bgColorA,bgColorA,Color.white,Color.white), "", communsPdf.setPadding(4f, 4f, 4f, 4f), bgColor).setValign(VerticalAlignment.MIDDLE);
    
                    
                    baseRow = communsPdf.setRow(table, 15);
                    communsPdf.setCell(baseRow, 100, "Datos del Certificado", Color.white, true, "C", 10, communsPdf.setLineStyle(bgColorA), "", communsPdf.setPadding(4f, 4f, 4f, 4f), bgColorA);
                     baseRow = communsPdf.setRow(table, 12);
                    communsPdf.setCell(baseRow, 57, "Fecha de inicio de vigencia", Color.black, true, "L", 10, communsPdf.setLineStyle(bgColorA,bgColorA,Color.white,Color.white), "", communsPdf.setPadding(0f, 0f, 4f, 4f), bgColor).setValign(VerticalAlignment.MIDDLE);
                    communsPdf.setCell(baseRow, 43, certificadoAxa.getGrupo(), Color.black, false, "R", 10, communsPdf.setLineStyle(bgColorA,bgColorA,Color.white,Color.white), "", communsPdf.setPadding(4f, 4f, 4f, 4f), bgColor).setValign(VerticalAlignment.MIDDLE);
                    baseRow = communsPdf.setRow(table, 12);
                    communsPdf.setCell(baseRow, 57, "Fecha de fin de vigencia", Color.black, true, "L", 10, communsPdf.setLineStyle(bgColorA,bgColorA,Color.white,Color.white), "", communsPdf.setPadding(0f, 0f, 4f, 4f), bgColor).setValign(VerticalAlignment.MIDDLE);
                    communsPdf.setCell(baseRow, 43, certificadoAxa.getGrupo(), Color.black, false, "R", 10, communsPdf.setLineStyle(bgColorA,bgColorA,Color.white,Color.white), "", communsPdf.setPadding(4f, 4f, 4f, 4f), bgColor).setValign(VerticalAlignment.MIDDLE);
                    baseRow = communsPdf.setRow(table, 12);
                    communsPdf.setCell(baseRow, 57, "Fecha de Ingreso  a la Póliza", Color.black, true, "L", 10, communsPdf.setLineStyle(bgColorA,Color.white,bgColorA,Color.white), "", communsPdf.setPadding(4f, 4f, 4f, 4f), bgColor).setValign(VerticalAlignment.MIDDLE);
                    communsPdf.setCell(baseRow, 43,"0%", Color.black, false, "R", 10, communsPdf.setLineStyle(bgColorA,bgColorA,bgColorA,Color.white), "", communsPdf.setPadding(4f, 4f, 4f, 4f), bgColor).setValign(VerticalAlignment.MIDDLE);                   
         
                    table.draw();

                    yStartPos -= table.getHeaderAndDataHeight();



                    table = new BaseTable(yStart, yStartNewPage, bottomMargin, 300, margin, document, page, true, true);
                    baseRow = communsPdf.setRow(table, 15);
                    communsPdf.setCell(baseRow, 100, "Datos del Contratante", Color.white, true, "L", 10, communsPdf.setLineStyle(bgColorA), "", communsPdf.setPadding(4f, 4f, 4f, 4f), bgColorA);
                    
                    baseRow = communsPdf.setRow(table, 16);
                    communsPdf.setCell(baseRow, 30, "Grupo empresarial:", Color.black, false, "L", 10, communsPdf.setLineStyle(bgColorA,bgColorA,Color.white,Color.white), "", communsPdf.setPadding(0f, 0f, 4f, 4f), bgColor).setValign(VerticalAlignment.MIDDLE);
                    communsPdf.setCell(baseRow, 70, certificadoAxa.getGrupo(), Color.black, false, "L", 10, communsPdf.setLineStyle(Color.white,bgColorA,Color.white,Color.white), "", communsPdf.setPadding(4f, 4f, 4f, 4f), bgColor).setValign(VerticalAlignment.MIDDLE);
                    baseRow = communsPdf.setRow(table, 16);
                    communsPdf.setCell(baseRow, 30, "Contratante:", Color.black, false, "L", 10, communsPdf.setLineStyle(bgColorA,Color.white,bgColorA,Color.white), "", communsPdf.setPadding(4f, 4f, 4f, 4f), bgColor).setValign(VerticalAlignment.MIDDLE);
                    communsPdf.setCell(baseRow, 70,certificadoAxa.getContrantante(), Color.black, false, "L", 10, communsPdf.setLineStyle(Color.white,bgColorA,bgColorA,Color.white), "", communsPdf.setPadding(4f, 4f, 4f, 4f), bgColor).setValign(VerticalAlignment.MIDDLE);                   
                    table.draw();

                   
                    yStart -= table.getHeaderAndDataHeight()+10;
                    table = new BaseTable(yStart, yStartNewPage, bottomMargin, 300, margin, document, page, true, true);
                    baseRow = communsPdf.setRow(table, 15);
                    communsPdf.setCell(baseRow, 100, "Datos del Asegurado", Color.white, true, "L", 10, communsPdf.setLineStyle(bgColorA), "", communsPdf.setPadding(4f, 4f, 4f, 4f), bgColorA);
                    
                    baseRow = communsPdf.setRow(table, 12);
                    communsPdf.setCell(baseRow, 37, "Nombre:", Color.black, true, "L", 10, communsPdf.setLineStyle(bgColorA,bgColorA,Color.white,Color.white), "", communsPdf.setPadding(0f, 0f, 5f, 4f), bgColor).setValign(VerticalAlignment.MIDDLE);
                    communsPdf.setCell(baseRow, 63, certificadoAxa.getNombAsegurado(), Color.black, false, "L", 10, communsPdf.setLineStyle(Color.white,bgColorA,Color.white,Color.white), "", communsPdf.setPadding(4f, 4f, 5f, 4f), bgColor).setValign(VerticalAlignment.MIDDLE);
                    baseRow = communsPdf.setRow(table, 12);
                    communsPdf.setCell(baseRow, 37, "Categoria:", Color.black, true, "L", 10, communsPdf.setLineStyle(bgColorA,bgColorA,Color.white,Color.white), "", communsPdf.setPadding(0f, 0f, 4f, 4f), bgColor).setValign(VerticalAlignment.MIDDLE);
                    communsPdf.setCell(baseRow, 63, certificadoAxa.getCategoria(), Color.black, false, "L", 10, communsPdf.setLineStyle(Color.white,bgColorA,Color.white,Color.white), "", communsPdf.setPadding(4f, 4f, 4f, 4f), bgColor).setValign(VerticalAlignment.MIDDLE);
        
                    baseRow = communsPdf.setRow(table, 12);
                    communsPdf.setCell(baseRow, 37, "Numero de empleado:", Color.black, true, "L", 10, communsPdf.setLineStyle(bgColorA,bgColorA,Color.white,Color.white), "", communsPdf.setPadding(0f, 0f, 4f, 4f), bgColor).setValign(VerticalAlignment.MIDDLE);
                    communsPdf.setCell(baseRow, 63, certificadoAxa.getNoEmpleado(), Color.black, false, "L", 10, communsPdf.setLineStyle(Color.white,bgColorA,Color.white,Color.white), "", communsPdf.setPadding(4f, 4f, 4f, 4f), bgColor).setValign(VerticalAlignment.MIDDLE);
        
                    baseRow = communsPdf.setRow(table, 12);
                    communsPdf.setCell(baseRow, 37, "Fecha de Nacimiento:", Color.black, true, "L", 10, communsPdf.setLineStyle(bgColorA,bgColorA,Color.white,Color.white), "", communsPdf.setPadding(0f, 0f, 4f, 4f), bgColor).setValign(VerticalAlignment.MIDDLE);
                    communsPdf.setCell(baseRow, 63, certificadoAxa.getGrupo(), Color.black, false, "L", 10, communsPdf.setLineStyle(Color.white,bgColorA,Color.white,Color.white), "", communsPdf.setPadding(4f, 4f, 4f, 4f), bgColor).setValign(VerticalAlignment.MIDDLE);

                    baseRow = communsPdf.setRow(table, 12);
                    communsPdf.setCell(baseRow, 37, "Ocupación:", Color.black, true, "L", 10, communsPdf.setLineStyle(bgColorA,Color.white,bgColorA,Color.white), "", communsPdf.setPadding(4f, 4f, 4f, 4f), bgColor).setValign(VerticalAlignment.MIDDLE);
                    communsPdf.setCell(baseRow, 63,certificadoAxa.getOcupacion(), Color.black, false, "L", 10, communsPdf.setLineStyle(Color.white,bgColorA,bgColorA,Color.white), "", communsPdf.setPadding(4f, 4f, 4f, 4f), bgColor).setValign(VerticalAlignment.MIDDLE);                   
                    table.draw();

                 
                    yStart = yStartPos-20;
                    table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth+20, margin, document, page, true, true);
                    baseRow = communsPdf.setRow(table, 15);
                    communsPdf.setCell(baseRow, 100, "Regla para determinar a la Suma Asegurada por el Fallecimiento y Endosos adicionales en su caso", Color.white, true, "L", 10, communsPdf.setLineStyle(bgColorA), "", communsPdf.setPadding(4f, 4f, 4f, 4f), bgColorA);
                    baseRow = communsPdf.setRow(table, 15);
                    communsPdf.setCell(baseRow, 100, "", Color.white, true, "L", 10, communsPdf.setLineStyle(bgColorA), "", communsPdf.setPadding(4f, 4f, 4f, 4f), bgColor);
        
                    table.draw();

                    yStart -= table.getHeaderAndDataHeight()+10;
                    table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth+20, margin, document, page, true, true);
                    baseRow = communsPdf.setRow(table, 15);
                    communsPdf.setCell(baseRow, 100, "Coberturas", Color.white, true, "L", 10, communsPdf.setLineStyle(bgColorA), "", communsPdf.setPadding(4f, 4f, 4f, 4f), bgColorA);
                    baseRow = communsPdf.setRow(table, 15);
                    communsPdf.setCell(baseRow, 50, "Coberturas Amparadas", Color.black, true, "C", 10, communsPdf.setLineStyle(bgColorA), "", communsPdf.setPadding(4f, 4f, 4f, 4f), bgColor);
                    communsPdf.setCell(baseRow, 50, "Suma Asegurada", Color.black, true, "C", 10, communsPdf.setLineStyle(bgColorA), "", communsPdf.setPadding(4f, 4f, 4f, 4f), bgColor);               
                    table.draw();
                    yStart -= table.getHeaderAndDataHeight();

                    boolean acumula2 = true;
                    int  t=0;
                    List<CoberturaBenificiosProjection>  coberturaBasica = certificadoAxa.getCoberturas();
                    while (t < coberturaBasica.size()) {

                        table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth+20, margin, document, page, true, true);
                         baseRow = communsPdf.setRow(table, 15);
                        communsPdf.setCell(baseRow, 50,coberturaBasica.get(t).getNombres(), Color.black, false, "L", 10, communsPdf.setLineStyle(bgColorA,Color.white,bgColorA,t == 0 ? bgColorA : Color.white), "", communsPdf.setPadding(4f, 4f, 4f, 4f), bgColor);
                        communsPdf.setCell(baseRow, 50,coberturaBasica.get(t).getSa(), Color.black, false, "R", 10, communsPdf.setLineStyle(bgColorA,bgColorA,bgColorA,t == 0 ? bgColorA : Color.white), "", communsPdf.setPadding(4f, 4f, 4f, 4f), bgColor);               
                      

                        if (isEndOfPage(table)) {
                            table.getRows().remove(table.getRows().size() - 1);
                            table.draw();
                            page = new PDPage();
                            document.addPage(page);
                            this.setEncabezado(document, page);
                            acumula2 = false;

                        } else {
                            table.draw();
                            yStart -= table.getHeaderAndDataHeight();
                        }

                        if (acumula2) {
                            t++;
                        }
                        if (t > 150) {
                            table.draw();
                            break;
                        }

                    }


                    yStart -= table.getHeaderAndDataHeight();
                    table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth+20, margin, document, page, true, true);
                    baseRow = communsPdf.setRow(table, 15);
                    communsPdf.setCell(baseRow, 100, "Beneficios", Color.white, true, "L", 10, communsPdf.setLineStyle(bgColorA), "", communsPdf.setPadding(4f, 4f, 4f, 4f), bgColorA);
                    baseRow = communsPdf.setRow(table, 15);
                    communsPdf.setCell(baseRow, 50, "Beneficios incluidos", Color.black, true, "C", 10, communsPdf.setLineStyle(bgColorA), "", communsPdf.setPadding(4f, 4f, 4f, 4f), bgColor);
                    communsPdf.setCell(baseRow, 50, "Suma Asegurada", Color.black, true, "C", 10, communsPdf.setLineStyle(bgColorA), "", communsPdf.setPadding(4f, 4f, 4f, 4f), bgColor);               
                    table.draw();
                    yStart -= table.getHeaderAndDataHeight();


                    boolean acumula3 = true;
                    int  x=0;
                    List<CoberturaBenificiosProjection>  beneficios= certificadoAxa.getBeneficios();
                    while (x < beneficios.size()) {
                        table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth+20, margin, document, page, true, true);
                        baseRow = communsPdf.setRow(table, 15);
                        communsPdf.setCell(baseRow, 50,beneficios.get(x).getNombres(), Color.black, false, "L", 10, communsPdf.setLineStyle(bgColorA,bgColorA,bgColorA,x == 0 ? bgColorA : Color.white), "", communsPdf.setPadding(4f, 4f, 4f, 4f), bgColor);
                        communsPdf.setCell(baseRow, 50,beneficios.get(x).getSa(), Color.black, false, "R", 10, communsPdf.setLineStyle(bgColorA,bgColorA,bgColorA,x == 0 ? bgColorA : Color.white), "", communsPdf.setPadding(4f, 4f, 4f, 4f), bgColor);               
                      

                        if (isEndOfPage(table)) {
                            table.getRows().remove(table.getRows().size() - 1);
                            table.draw();
                            page = new PDPage();
                            document.addPage(page);
                            this.setEncabezado(document, page);
                            acumula3 = false;

                        } else {
                            table.draw();
                            yStart -= table.getHeaderAndDataHeight();
                        }

                        if (acumula3) {
                            x++;
                        }
                        if (x > 150) {
                            table.draw();
                            break;
                        }

                    }

                    boolean acumula4 = true;
                    int  c=0;
                    List<BeneficiariosAxaProjection>  beneficiarios= certificadoAxa.getBeneficiarios();
                    yStart -= table.getHeaderAndDataHeight();
                    table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth+20, margin, document, page, true, true);
                    baseRow = communsPdf.setRow(table, 15);
                    communsPdf.setCell(baseRow, 100, "Beneficiarios (nombre y apellidos, parentesco, fecha de nacimiento y porcentaje de participación)", Color.white, true, "L", 10, communsPdf.setLineStyle(bgColorA), "", communsPdf.setPadding(4f, 4f, 4f, 4f), bgColorA);
                    table.draw();
                    yStart -= table.getHeaderAndDataHeight();

                    yStartPosTb2 = yStart;
                    while (c < beneficiarios.size()) {
                        table2 = new BaseTable(yStartPosTb2, yStartNewPage, bottomMargin, fullWidth+20, margin, document, page, true, true);
                        baseRow2 = communsPdf.setRow(table2, 15);
                        communsPdf.setCell(baseRow2, 100,"", Color.black, false, "L", 10, communsPdf.setLineStyle(bgColorA,bgColorA,bgColorA,x == 0 ? bgColorA : Color.white), "", communsPdf.setPadding(4f, 4f, 4f, 4f), bgColor);
                        table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth+20, margin, document, page, false, true);
                        baseRow = communsPdf.setRow(table, 15);
                        communsPdf.setCell(baseRow, 34,beneficiarios.get(c).getNombres(), Color.black, false, "L", 10, communsPdf.setLineStyle(bgColorA,bgColorA,bgColorA,x == 0 ? bgColorA : Color.white), "", communsPdf.setPadding(4f, 4f, 4f, 4f), bgColor);
                        communsPdf.setCell(baseRow, 12,beneficiarios.get(c).getFechaNacimiento(), Color.black, false, "R", 10, communsPdf.setLineStyle(bgColorA,bgColorA,bgColorA,x == 0 ? bgColorA : Color.white), "", communsPdf.setPadding(4f, 4f, 4f, 4f), bgColor);               
                        communsPdf.setCell(baseRow, 10,beneficiarios.get(c).getPorcentaje(), Color.black, false, "R", 10, communsPdf.setLineStyle(bgColorA,bgColorA,bgColorA,x == 0 ? bgColorA : Color.white), "", communsPdf.setPadding(4f, 4f, 4f, 4f), bgColor); 

                     
                        if (isEndOfPage(table)) {
                            table.getRows().remove(table.getRows().size() - 1);
                            table.draw();
                            table2.getRows().remove(table2.getRows().size() - 1);
                            table2.draw();
                            page = new PDPage();
                            document.addPage(page);
                            this.setEncabezado(document, page);
                            acumula4 = false;

                        } else {
                            table2.draw();
                            table.draw();
                         
                            yStart -= table.getHeaderAndDataHeight();
                            yStartPosTb2 -= table2.getHeaderAndDataHeight();
                        }

                        if (acumula3) {
                            c++;
                        }
                        if (c > 150) {
                           
                            table2.draw();
                            table.draw();
                            break;
                        }

                    }

                    page = new PDPage();
                    document.addPage(page);
                    this.setEncabezado(document, page);
                    StringBuilder texto =  new StringBuilder();

                    Float  tb=150f;
                    texto = new StringBuilder();
                    texto.append("AXA Seguros S.A. de C.V. cubre al Asegurado por las coberturas contratadas en los términos y condiciones de la Póliza");       
                    this.parrafo(document, page, this.medidas(page.getMediaBox(), 32f, tb), Sio4CommunsPdf.eliminaHtmlTags3(texto.toString()), 537, arialN, 9.5f, (-1.2f * 9f), 1f,0.3f);

                    tb = tb+30;
                    texto = new StringBuilder();
                    texto.append("Artículo 17 del Reglamento del Seguro de Grupo para la Operación de Vida y del Seguro Colectivo para la Operación de Accidentes y Enfermedades.");
       
                    this.parrafo(document, page, this.medidas(page.getMediaBox(), 32f, tb), Sio4CommunsPdf.eliminaHtmlTags3(texto.toString()), 540, arialB, 9.5f, (-1.2f * 9f), 1f,0.2f);
          
                    tb = tb+12;
                    texto = new StringBuilder();
                    texto.append("Las personas que ingresen al Grupo o Colectividad asegurado con posterioridad a la");       
                    this.parrafo(document, page, this.medidas(page.getMediaBox(), 183f, tb), Sio4CommunsPdf.eliminaHtmlTags3(texto.toString()), 540, arialN, 9.5f, (-1.2f * 9f), 1f,0.4f);
                    tb = tb+12;
                    texto = new StringBuilder();
                    texto.append("celebración  del contrato y que  hayan dado su  consentimiento para ser asegurados dentro de los 30 días  naturales siguientes ");    
                    texto.append("a su ingreso, quedarán  aseguradas con las mismas  condiciones en que fue contratada la Póliza, desde el  momento que "); 
                    texto.append("adquirieron las características para formar parte del Grupo o Colectividad de que se trate.");  
                     
                    this.parrafo(document, page, this.medidas(page.getMediaBox(), 32f, tb), Sio4CommunsPdf.eliminaHtmlTags3(texto.toString()), 540, arialN, 9.5f, (-1.2f * 9f), 1f,0.3f);

                    tb = tb+42;
                    texto = new StringBuilder();
                    texto.append("Con independencia de lo previsto en el párrafo anterior, tratándose de personas que soliciten su ingreso al Grupo o ");    
                    texto.append("Colectividad asegurado con posterioridad a la celebración del contrato y que hayan dado su consentimiento después de los "); 
                    texto.append("30 días naturales siguientes a la fecha en que hubieran adquirido el derecho de formar parte del mismo, la  Compañía, dentro ");  
                     
                    this.parrafo(document, page, this.medidas(page.getMediaBox(), 32f, tb), Sio4CommunsPdf.eliminaHtmlTags3(texto.toString()), 540, arialN, 9.5f, (-1.2f * 9f), 1f,0.2f);

                    tb = tb+50;
                    texto = new StringBuilder();
                    texto.append("Cuando la aseguradora exija requisitos médicos u otros para asegurar a las personas a que se refiere el párrafo anterior, ");    
                    texto.append("contará con un plazo de 30 días naturales, contado a partir de la fecha en que se hayan cumplido dichos requisitos para "); 
                    texto.append("resolver sobre la aceptación o no de asegurar a la persona, de no hacerlo se entenderá que la acepta con las mismas ");  
                    texto.append("condiciones en que fue contratada la Póliza.");
                     
                    this.parrafo(document, page, this.medidas(page.getMediaBox(), 32f, tb), Sio4CommunsPdf.eliminaHtmlTags3(texto.toString()), 540, arialN, 9.5f, (-1.2f * 9f), 1f,0.3f);

                    tb = tb+50;
                    texto = new StringBuilder();          
                    texto.append("Artículo 18 del Reglamento del Seguro de Grupo para la Operación de Vida y del Seguro Colectivo para la Operación de Accidentes y Enfermedades.");       
                    this.parrafo(document, page, this.medidas(page.getMediaBox(), 32f, tb), Sio4CommunsPdf.eliminaHtmlTags3(texto.toString()), 540, arialB, 9.5f, (-1.2f * 9f), 1f,0.2f);
                    tb = tb+12;
                    texto = new StringBuilder();
                    texto.append("Las personas que se separen definitivamente del Grupo o  Colectividad asegurado,");       
                    this.parrafo(document, page, this.medidas(page.getMediaBox(), 183f, tb), Sio4CommunsPdf.eliminaHtmlTags3(texto.toString()), 540, arialN, 9.5f, (-1.2f * 9f), 1f,0.5f);
         
                    tb = tb+10;
                    texto = new StringBuilder();
                    texto.append("dejarán.Cuando la aseguradora exija requisitos médicos u otros para asegurar a las personas a que se refiere el párrafo anterior,");    
                    texto.append("contará con un plazo de 30 días naturales, contado a partir de la fecha en que se hayan cumplido dichos requisitos para "); 
                    texto.append("resolver sobre la aceptación o no de asegurar a la persona, de no hacerlo se entenderá que la acepta con las mismas ");  
                    texto.append("condiciones en que fue contratada la Póliza.");
                    this.parrafo(document, page, this.medidas(page.getMediaBox(), 32f, tb), Sio4CommunsPdf.eliminaHtmlTags3(texto.toString()), 540, arialN, 9.5f, (-1.4f * 9f), 3f,0.3f);


                    tb = tb+60;
                    texto = new StringBuilder();          
                    texto.append("Artículo 19 del Reglamento del Seguro de Grupo para la Operación de Vida y del Seguro Colectivo para la Operación de Accidentes y Enfermedades.");       
                    this.parrafo(document, page, this.medidas(page.getMediaBox(), 32f, tb), Sio4CommunsPdf.eliminaHtmlTags3(texto.toString()), 540, arialB, 9.5f, (-1.2f * 9f), 1f,0.2f);
        
                    tb = tb+12;
                    texto = new StringBuilder();
                    texto.append("En los Seguros de Grupo y en los Seguros Colectivos cuyo objeto sea otorgar una ");       
                    this.parrafo(document, page, this.medidas(page.getMediaBox(), 183f, tb), Sio4CommunsPdf.eliminaHtmlTags3(texto.toString()), 540, arialN, 9.5f, (-1.4f * 9f), 1f,0.5f);
                    tb = tb+10;
                    texto = new StringBuilder();
                    texto.append("prestación laboral, se deberá cumplir con lo siguiente:");               
                    this.parrafo(document, page, this.medidas(page.getMediaBox(), 32f, tb), Sio4CommunsPdf.eliminaHtmlTags3(texto.toString()), 540, arialN, 9.5f, (-1.4f * 9f), 1f,0.3f);


                    tb = tb+20;
                    texto = new StringBuilder();
                    texto.append("I. Para la operación de vida, la Aseguradora tendrá la obligación de asegurar, por una sola vez y sin requisitos médicos, al ");    
                    texto.append("Integrante del Grupo o Colectividad que se separe definitivamente del mismo, en cualquiera de los planes individuales de la "); 
                    texto.append("operación de vida que esta comercialice, con excepción del seguro temporal y sin incluir beneficio adicional alguno, simpre ");  
                    texto.append("que su edad esté comprendida dentro de los límites de admisión de la Aseguradora. Pare ejercer este derecho, la persona ");
                    texto.append("separada del Grupo o Colectividad deberá presentar su solicitud a la Aseguradora, dentro del plazo de treinta días naturales ");
                    texto.append("a partir de su separación. La Suma Asegurada será la que resulte menor entre la que se encontraba en vigor al momento de ");
                    texto.append("la separación y la máxima Suma Asegurada sin pruebas médicas de la cartera individual de la Aseguradora, considerando la ");
                    texto.append("edad alcanzada del asegurado al momento de separarse. La prima será determinada de acuerdo a los procedimientos ");
                    texto.append("establecidos en las notas técnicas registradas ante la Comisión. El solicitante deberá pagar a la Aseguradora la prima que");
                    texto.append("corresponda a la edad alcanzada y ocupación, en su caso, en la fecha de solicitud, según la tarifa en vigor. Las ");
                    texto.append("Aseguradoras que practiquen el Seguro de Grupo en la operación de vida deberán operar, cuando menos, un plan ordinario de vida.");               
                    texto.append("AXA Seguros S.A. de C.V. cubre al Integrante por las coberturas contratadas en los términos y condiciones de la Póliza");
                    this.parrafo(document, page, this.medidas(page.getMediaBox(), 32f, tb), Sio4CommunsPdf.eliminaHtmlTags3(texto.toString()), 540, arialN, 9.5f, (-1.4f * 9f), 3f,0.3f);


                    int nume = document.getNumberOfPages();
                                        
                    for (int v = 0; v < nume; v++) {
                        PDPage page2 = document.getPage(v);
                        try (PDPageContentStream content = new PDPageContentStream(document, page2, PDPageContentStream.AppendMode.PREPEND, true, true)) {
                            int u = 1;
                            u += v;
                            String numeF = Integer.toString(u);
                            communsPdf.drawText(content, false, 563, 770, numeF+"/"+ nume);
                        }
                    }

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
                    "Ocurrio un error en el servicio ImpresionCertificadoAxaPdf: " + ex.getMessage());
        }

    }
    private float setEncabezado(PDDocument document, PDPage page) {
        try ( PDPageContentStream content = new PDPageContentStream(document, page)) {
            BaseTable table;
            Row<PDPage> baseRow;
            yStart = 770;

            table = new BaseTable(770, 770, bottomMargin, 300, 280, document, page, true, true);
            baseRow = communsPdf.setRow(table, 10);
            communsPdf.setCell(baseRow, 100, "Vida", bgColorAb, true, "R", 11, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f, 5f, 3f, 5f), bgColor);
            baseRow = communsPdf.setRow(table, 10);
            communsPdf.setCell(baseRow, 100, "Certificado Individual", bgColorA, true, "R", 11, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f, 5f, 3f, 5f), bgColor);
            baseRow = communsPdf.setRow(table, 10);
            communsPdf.setCell(baseRow, 100, "Respaldo Empresarial EXPERIENCIA PROPIA Nacional", bgColorA, false, "R", 11, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f, 5f, 3f, 5f), bgColor);
            table.draw();

            table = new BaseTable(90, 90, 5, fullWidth, 20, document, page, true, true);
            baseRow = communsPdf.setRow(table, 12);
            communsPdf.setCell(baseRow, 100, "AXA Seguros S.A. de C.V.", Color.black, true, "L", 10, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f, 5f, 3f, 5f), bgColor);
            baseRow = communsPdf.setRow(table, 12);
            communsPdf.setCell(baseRow, 100, "Felix Cuevas 366 Piso 3, Col. Tlacoquemecatl Alcaldia Benito Juarez, Del. Benito Juárez, C.P.,", Color.black, false, "L", 10, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f, 5f, 3f, 5f), bgColor);
            baseRow = communsPdf.setRow(table, 12);
            communsPdf.setCell(baseRow, 100, "03200, Ciudad de Mexico", Color.black, false, "L", 10, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f, 5f, 3f, 5f), bgColor);
            baseRow = communsPdf.setRow(table, 12);
            communsPdf.setCell(baseRow, 100, "Tels. 55 5169 1000 - 800 900 1292 • • www.axa.mx", Color.black, false, "L", 10, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f, 5f, 3f, 5f), bgColor);
            baseRow = communsPdf.setRow(table, 12);
            communsPdf.setCell(baseRow, 100, "Este documento no es valido como recibo de pago.", Color.black, true, "L", 10, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f, 5f, 3f, 5f), bgColor);
    
            table.draw();

            table = new BaseTable(770, 770, bottomMargin, 120, margin - 27, document, page, false, true);
            baseRow = communsPdf.setRow(table, 12);
            communsPdf.setCell(baseRow, 100, ImageUtils.readImage("https://storage.googleapis.com/quattrocrm/h31fila/2208/Polizas/2208/zeSWdRgKylw7QuyGNMQ9vc8HSPiToM8cVUnQd5e2VOIzWrci1IgN1QLcOhFEfhy/Axalogo.png"));
            table.draw();
            yStart -= table.getHeaderAndDataHeight() + 38;

            return yStart;

        } catch (Exception ex) {
            throw new GeneralServiceException("00001",
                    "Ocurrio un error en el servicio ImpresionConstanciaAntiguedad: setEncabezado " + ex.getMessage());
        }

    }

    private boolean isEndOfPage(BaseTable table) {

        float currentY = yStart - table.getHeaderAndDataHeight();
        boolean isEndOfPage = currentY <= bottomMargin;
        return isEndOfPage;
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


}
