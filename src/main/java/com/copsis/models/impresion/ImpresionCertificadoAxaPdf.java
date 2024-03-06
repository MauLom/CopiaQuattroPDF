package com.copsis.models.impresion;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;

import com.copsis.clients.projections.BeneficiariosAxaProjection;
import com.copsis.clients.projections.CoberturaBasicaProjection;
import com.copsis.clients.projections.CoberturaBenificiosProjection;
import com.copsis.clients.projections.CoberturaProjection;
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
    private final Color bgColorex = new Color(46, 49, 146, 0);
    private final Color bgColor = new Color(255, 255, 255, 0);

    private float fullWidth = 556;
    private float yStartPos = 0;

    public byte[] buildPDF(ImpresionCertificadoAxa certificadoAxa) {
        ByteArrayOutputStream output;
        try {
            try (PDDocument document = new PDDocument()) {
                try {
                    PDPage page = new PDPage();
                    document.addPage(page);
                    BaseTable table;
                    Row<PDPage> baseRow;

                    this.setEncabezado(document, page);
                    yStartPos= yStart;
                    table = new BaseTable(yStart, yStartNewPage, bottomMargin, 260, 340, document, page, true, true);
                    baseRow = communsPdf.setRow(table, 15);
                    communsPdf.setCell(baseRow, 100, "Póliza", Color.white, true, "C", 10, communsPdf.setLineStyle(bgColorA), "", communsPdf.setPadding(4f, 4f, 4f, 4f), bgColorA);
                    baseRow = communsPdf.setRow(table, 15);
                    communsPdf.setCell(baseRow, 100, certificadoAxa.getGrupo(), Color.black, false, "C", 10, communsPdf.setLineStyle(bgColorA,bgColorA,Color.white,Color.white), "", communsPdf.setPadding(4f, 4f, 4f, 4f), bgColor).setValign(VerticalAlignment.MIDDLE);
                    baseRow = communsPdf.setRow(table, 15);
                    communsPdf.setCell(baseRow, 100, "Datos de la Póliza", Color.white, true, "C", 10, communsPdf.setLineStyle(bgColorA), "", communsPdf.setPadding(4f, 4f, 4f, 4f), bgColorA);
         
                    baseRow = communsPdf.setRow(table, 16);
                    communsPdf.setCell(baseRow, 57, "Fecha de inicio de vigencia", Color.black, true, "L", 10, communsPdf.setLineStyle(bgColorA,bgColorA,Color.white,Color.white), "", communsPdf.setPadding(0f, 0f, 4f, 4f), bgColor).setValign(VerticalAlignment.MIDDLE);
                    communsPdf.setCell(baseRow, 43, certificadoAxa.getGrupo(), Color.black, false, "L", 10, communsPdf.setLineStyle(bgColorA,bgColorA,Color.white,Color.white), "", communsPdf.setPadding(4f, 4f, 4f, 4f), bgColor).setValign(VerticalAlignment.MIDDLE);
                    baseRow = communsPdf.setRow(table, 16);
                    communsPdf.setCell(baseRow, 57, "Fecha de fin de vigencia", Color.black, true, "L", 10, communsPdf.setLineStyle(bgColorA,bgColorA,Color.white,Color.white), "", communsPdf.setPadding(0f, 0f, 4f, 4f), bgColor).setValign(VerticalAlignment.MIDDLE);
                    communsPdf.setCell(baseRow, 43, certificadoAxa.getGrupo(), Color.black, false, "L", 10, communsPdf.setLineStyle(bgColorA,bgColorA,Color.white,Color.white), "", communsPdf.setPadding(4f, 4f, 4f, 4f), bgColor).setValign(VerticalAlignment.MIDDLE);
        
                    baseRow = communsPdf.setRow(table, 16);
                    communsPdf.setCell(baseRow, 57, "Moneda", Color.black, true, "L", 10, communsPdf.setLineStyle(bgColorA,bgColorA,Color.white,Color.white), "", communsPdf.setPadding(0f, 0f, 4f, 4f), bgColor).setValign(VerticalAlignment.MIDDLE);
                    communsPdf.setCell(baseRow, 43, certificadoAxa.getGrupo(), Color.black, false, "L", 10, communsPdf.setLineStyle(bgColorA,bgColorA,Color.white,Color.white), "", communsPdf.setPadding(4f, 4f, 4f, 4f), bgColor).setValign(VerticalAlignment.MIDDLE);
        
                    baseRow = communsPdf.setRow(table, 16);
                    communsPdf.setCell(baseRow, 57, "Participación de la prima", Color.black, true, "L", 10, communsPdf.setLineStyle(bgColorA,Color.white,bgColorA,Color.white), "", communsPdf.setPadding(4f, 4f, 4f, 4f), bgColor).setValign(VerticalAlignment.MIDDLE);
                    communsPdf.setCell(baseRow, 43,certificadoAxa.getContrantante(), Color.black, false, "L", 10, communsPdf.setLineStyle(bgColorA,bgColorA,bgColorA,Color.white), "", communsPdf.setPadding(4f, 4f, 4f, 4f), bgColor).setValign(VerticalAlignment.MIDDLE);                   
                    baseRow = communsPdf.setRow(table, 15);
                    communsPdf.setCell(baseRow, 100, "Datos del Certificado", Color.white, true, "C", 10, communsPdf.setLineStyle(bgColorA), "", communsPdf.setPadding(4f, 4f, 4f, 4f), bgColorA);
                     baseRow = communsPdf.setRow(table, 16);
                    communsPdf.setCell(baseRow, 57, "Fecha de inicio de vigencia", Color.black, true, "L", 10, communsPdf.setLineStyle(bgColorA,bgColorA,Color.white,Color.white), "", communsPdf.setPadding(0f, 0f, 4f, 4f), bgColor).setValign(VerticalAlignment.MIDDLE);
                    communsPdf.setCell(baseRow, 43, certificadoAxa.getGrupo(), Color.black, false, "L", 10, communsPdf.setLineStyle(bgColorA,bgColorA,Color.white,Color.white), "", communsPdf.setPadding(4f, 4f, 4f, 4f), bgColor).setValign(VerticalAlignment.MIDDLE);
                    baseRow = communsPdf.setRow(table, 16);
                    communsPdf.setCell(baseRow, 57, "Fecha de fin de vigencia", Color.black, true, "L", 10, communsPdf.setLineStyle(bgColorA,bgColorA,Color.white,Color.white), "", communsPdf.setPadding(0f, 0f, 4f, 4f), bgColor).setValign(VerticalAlignment.MIDDLE);
                    communsPdf.setCell(baseRow, 43, certificadoAxa.getGrupo(), Color.black, false, "L", 10, communsPdf.setLineStyle(bgColorA,bgColorA,Color.white,Color.white), "", communsPdf.setPadding(4f, 4f, 4f, 4f), bgColor).setValign(VerticalAlignment.MIDDLE);
                    baseRow = communsPdf.setRow(table, 16);
                    communsPdf.setCell(baseRow, 57, "Fecha de Ingreso  a la Póliza", Color.black, true, "L", 10, communsPdf.setLineStyle(bgColorA,Color.white,bgColorA,Color.white), "", communsPdf.setPadding(4f, 4f, 4f, 4f), bgColor).setValign(VerticalAlignment.MIDDLE);
                    communsPdf.setCell(baseRow, 43,certificadoAxa.getContrantante(), Color.black, false, "L", 10, communsPdf.setLineStyle(bgColorA,bgColorA,bgColorA,Color.white), "", communsPdf.setPadding(4f, 4f, 4f, 4f), bgColor).setValign(VerticalAlignment.MIDDLE);                   
         
                    table.draw();

                    yStartPos -= table.getHeaderAndDataHeight();



                    table = new BaseTable(yStart, yStartNewPage, bottomMargin, 300, margin, document, page, true, true);
                    baseRow = communsPdf.setRow(table, 15);
                    communsPdf.setCell(baseRow, 100, "Datos del Contratante", Color.white, true, "L", 10, communsPdf.setLineStyle(bgColorA), "", communsPdf.setPadding(4f, 4f, 4f, 4f), bgColorA);
                    
                    baseRow = communsPdf.setRow(table, 16);
                    communsPdf.setCell(baseRow, 54, "Grupo Empresarelia:", Color.black, false, "L", 10, communsPdf.setLineStyle(bgColorA,bgColorA,Color.white,Color.white), "", communsPdf.setPadding(0f, 0f, 4f, 4f), bgColor).setValign(VerticalAlignment.MIDDLE);
                    communsPdf.setCell(baseRow, 46, certificadoAxa.getGrupo(), Color.black, false, "L", 10, communsPdf.setLineStyle(Color.white,bgColorA,Color.white,Color.white), "", communsPdf.setPadding(4f, 4f, 4f, 4f), bgColor).setValign(VerticalAlignment.MIDDLE);
                    baseRow = communsPdf.setRow(table, 16);
                    communsPdf.setCell(baseRow, 54, "Contratante:", Color.black, false, "L", 10, communsPdf.setLineStyle(bgColorA,Color.white,bgColorA,Color.white), "", communsPdf.setPadding(4f, 4f, 4f, 4f), bgColor).setValign(VerticalAlignment.MIDDLE);
                    communsPdf.setCell(baseRow, 46,certificadoAxa.getContrantante(), Color.black, false, "L", 10, communsPdf.setLineStyle(Color.white,bgColorA,bgColorA,Color.white), "", communsPdf.setPadding(4f, 4f, 4f, 4f), bgColor).setValign(VerticalAlignment.MIDDLE);                   
                    table.draw();

                   
                    yStart -= table.getHeaderAndDataHeight()+10;
                    table = new BaseTable(yStart, yStartNewPage, bottomMargin, 300, margin, document, page, true, true);
                    baseRow = communsPdf.setRow(table, 15);
                    communsPdf.setCell(baseRow, 100, "Datos del Asegurado", Color.white, true, "L", 10, communsPdf.setLineStyle(bgColorA), "", communsPdf.setPadding(4f, 4f, 4f, 4f), bgColorA);
                    
                    baseRow = communsPdf.setRow(table, 16);
                    communsPdf.setCell(baseRow, 37, "Nombre:", Color.black, true, "L", 10, communsPdf.setLineStyle(bgColorA,bgColorA,Color.white,Color.white), "", communsPdf.setPadding(0f, 0f, 4f, 4f), bgColor).setValign(VerticalAlignment.MIDDLE);
                    communsPdf.setCell(baseRow, 63, certificadoAxa.getGrupo(), Color.black, false, "L", 10, communsPdf.setLineStyle(Color.white,bgColorA,Color.white,Color.white), "", communsPdf.setPadding(4f, 4f, 4f, 4f), bgColor).setValign(VerticalAlignment.MIDDLE);
                    baseRow = communsPdf.setRow(table, 16);
                    communsPdf.setCell(baseRow, 37, "Categoria:", Color.black, true, "L", 10, communsPdf.setLineStyle(bgColorA,bgColorA,Color.white,Color.white), "", communsPdf.setPadding(0f, 0f, 4f, 4f), bgColor).setValign(VerticalAlignment.MIDDLE);
                    communsPdf.setCell(baseRow, 63, certificadoAxa.getGrupo(), Color.black, false, "L", 10, communsPdf.setLineStyle(Color.white,bgColorA,Color.white,Color.white), "", communsPdf.setPadding(4f, 4f, 4f, 4f), bgColor).setValign(VerticalAlignment.MIDDLE);
        
                    baseRow = communsPdf.setRow(table, 16);
                    communsPdf.setCell(baseRow, 37, "Numero de empleado:", Color.black, true, "L", 10, communsPdf.setLineStyle(bgColorA,bgColorA,Color.white,Color.white), "", communsPdf.setPadding(0f, 0f, 4f, 4f), bgColor).setValign(VerticalAlignment.MIDDLE);
                    communsPdf.setCell(baseRow, 63, certificadoAxa.getGrupo(), Color.black, false, "L", 10, communsPdf.setLineStyle(Color.white,bgColorA,Color.white,Color.white), "", communsPdf.setPadding(4f, 4f, 4f, 4f), bgColor).setValign(VerticalAlignment.MIDDLE);
        
                    baseRow = communsPdf.setRow(table, 16);
                    communsPdf.setCell(baseRow, 37, "Fecha de Nacimiento:", Color.black, true, "L", 10, communsPdf.setLineStyle(bgColorA,bgColorA,Color.white,Color.white), "", communsPdf.setPadding(0f, 0f, 4f, 4f), bgColor).setValign(VerticalAlignment.MIDDLE);
                    communsPdf.setCell(baseRow, 63, certificadoAxa.getGrupo(), Color.black, false, "L", 10, communsPdf.setLineStyle(Color.white,bgColorA,Color.white,Color.white), "", communsPdf.setPadding(4f, 4f, 4f, 4f), bgColor).setValign(VerticalAlignment.MIDDLE);

                    baseRow = communsPdf.setRow(table, 16);
                    communsPdf.setCell(baseRow, 37, "Ocupación:", Color.black, true, "L", 10, communsPdf.setLineStyle(bgColorA,Color.white,bgColorA,Color.white), "", communsPdf.setPadding(4f, 4f, 4f, 4f), bgColor).setValign(VerticalAlignment.MIDDLE);
                    communsPdf.setCell(baseRow, 63,certificadoAxa.getContrantante(), Color.black, false, "L", 10, communsPdf.setLineStyle(Color.white,bgColorA,bgColorA,Color.white), "", communsPdf.setPadding(4f, 4f, 4f, 4f), bgColor).setValign(VerticalAlignment.MIDDLE);                   
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

                    while (c < beneficiarios.size()) {
                        table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth+20, margin, document, page, true, true);
                        baseRow = communsPdf.setRow(table, 15);
                        communsPdf.setCell(baseRow, 15,beneficiarios.get(x).getNombres(), Color.black, false, "L", 10, communsPdf.setLineStyle(bgColorA,bgColorA,bgColorA,x == 0 ? bgColorA : Color.white), "", communsPdf.setPadding(4f, 4f, 4f, 4f), bgColor);
                        communsPdf.setCell(baseRow, 15,beneficiarios.get(x).getFechaNacimiento().toString(), Color.black, false, "R", 10, communsPdf.setLineStyle(bgColorA,bgColorA,bgColorA,x == 0 ? bgColorA : Color.white), "", communsPdf.setPadding(4f, 4f, 4f, 4f), bgColor);               
                        communsPdf.setCell(baseRow, 15,beneficiarios.get(x).getPorcentaje(), Color.black, false, "R", 10, communsPdf.setLineStyle(bgColorA,bgColorA,bgColorA,x == 0 ? bgColorA : Color.white), "", communsPdf.setPadding(4f, 4f, 4f, 4f), bgColor); 

                        if (isEndOfPage(table)) {
                            table.getRows().remove(table.getRows().size() - 1);
                            table.draw();
                            page = new PDPage();
                            document.addPage(page);
                            this.setEncabezado(document, page);
                            acumula4 = false;

                        } else {
                            table.draw();
                            yStart -= table.getHeaderAndDataHeight();
                        }

                        if (acumula3) {
                            c++;
                        }
                        if (c > 150) {
                            table.draw();
                            break;
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
            communsPdf.setCell(baseRow, 100, "", bgColorA, false, "R", 11, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f, 5f, 3f, 5f), bgColor);
            table.draw();

            table = new BaseTable(30, 30, 5, fullWidth + 100, 5, document, page, false, true);
            baseRow = communsPdf.setRow(table, 12);
            communsPdf.setCell(baseRow, 100, "AXA Seguros, S.A. de C.V. Félix Cuevas 366, piso 3, col. Tlacoquemécatl, alcaldía Benito Juárez, 03200, CDMX, México • Tels. 55 5169 1000 • 800 900 1292 • axa.mx", bgColorA, false, "L", 8, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f, 5f, 3f, 5f), bgColor);
            table.draw();

            table = new BaseTable(770, 770, bottomMargin, 120, margin - 27, document, page, false, true);
            baseRow = communsPdf.setRow(table, 12);
            communsPdf.setCell(baseRow, 100, ImageUtils.readImage("https://storage.googleapis.com/quattrocrm/h31fila/2208/Polizas/2208/zeSWdRgKylw7QuyGNMQ9vc8HSPiToM8cVUnQd5e2VOIzWrci1IgN1QLcOhFEfhy/Axalogo.png"));
            table.draw();
            yStart -= table.getHeaderAndDataHeight() + 15;

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
}
