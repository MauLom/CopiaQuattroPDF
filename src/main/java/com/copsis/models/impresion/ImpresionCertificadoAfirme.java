 package com.copsis.models.impresion ;

    import java.awt.Color ;
    import java.io.ByteArrayOutputStream ;
    import java.io.File ;
    import java.io.IOException ;
    import java.io.InputStream ;
    import java.net.URL ;
    import java.util.ArrayList ;
    import java.util.List ;

    import org.apache.pdfbox.multipdf.PDFMergerUtility ;
    import org.apache.pdfbox.pdmodel.PDDocument ;
    import org.apache.pdfbox.pdmodel.PDPage ;
    import org.apache.pdfbox.pdmodel.PDPageContentStream ;
    import org.apache.pdfbox.pdmodel.common.PDRectangle ;
    import org.apache.pdfbox.pdmodel.font.PDFont ;
    import org.apache.pdfbox.pdmodel.font.PDType0Font ;
    import org.apache.pdfbox.pdmodel.font.PDType1Font ;
    import org.apache.pdfbox.pdmodel.graphics.color.PDColor ;
    import org.apache.pdfbox.pdmodel.graphics.color.PDDeviceRGB ;

    import com.copsis.clients.projections.AseguradosProjection ;
    import com.copsis.clients.projections.BeneficiarioProjection ;
    import com.copsis.clients.projections.CaractulaProjection ;
import com.copsis.clients.projections.CoberturaProjection;
import com.copsis.exceptions.GeneralServiceException ;
    import com.copsis.models.Tabla.BaseTable ;
    import com.copsis.models.Tabla.ImageUtils ;
    import com.copsis.models.Tabla.Row ;
    import com.copsis.models.Tabla.Sio4CommunsPdf ;
    import com.copsis.models.Tabla.VerticalAlignment ;

    public class ImpresionCertificadoAfirme {

        private float yStartNewPage = 780, yStart = 765, bottomMargin = 32, fullWidth = 533, margin = 53, marginx = 56, yStartY = 0;
        private Sio4CommunsPdf communsPdf = new Sio4CommunsPdf();
        private final Color colorLink = new Color(6, 6, 255, 0);
        private final Color gray = new Color(217, 217, 217, 0);

        private boolean pageBreak = false;
        private boolean pageBreak2 = false;

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

                        InputStream arialNormal = new URL("https://storage.googleapis.com/quattrocrm-copsis/BiiBiiC/arial.ttf").openStream();
                        InputStream arialBold = new URL("https://storage.googleapis.com/quattrocrm-copsis/BiiBiiC/arialbd.ttf").openStream();
                        PDType0Font arialN = PDType0Font.load(document, arialNormal);
                        PDType0Font arialB = PDType0Font.load(document, arialBold);

                        String lineatxt1 = "SEGURO DE VIDA GRUPO ESCOLAR";
                        String lineatxt2 = "UNIVERSIDAD AUTÓNOMA DE VERACRUZ, S.A. Y/O FILIALES";
                        if (datos.getTipo() == 2) {
                            lineatxt1 = "SEGURO DE VIDA GRUPO ORFANDAD";
                            lineatxt2 = "UNIVERSIDAD TECNOLÓGICA DE MÉXICO (UNITEC) Y FILIALES";
                        }

                        table = new BaseTable(745, yStartNewPage, bottomMargin, fullWidth / 2, margin + 210, document, page, false, true);
                        baseRow = communsPdf.setRow(table, 11);
                        communsPdf.setCell(baseRow, 100, "CONSENTIMIENTO-CERTIFICADO INDIVIDUAL", Color.BLACK, true, "C", 10, communsPdf.setLineStyle(Color.black), "", communsPdf.setPadding2(4f, 0f, 0f, 0f), gray).setValign(VerticalAlignment.MIDDLE);
                        baseRow = communsPdf.setRow(table, 11);
                        communsPdf.setCell(baseRow, 100, lineatxt1, Color.BLACK, true, "C", 10, communsPdf.setLineStyle(Color.black), "", communsPdf.setPadding2(4f, 0f, 0f, 0f), gray).setValign(VerticalAlignment.MIDDLE);
                        baseRow = communsPdf.setRow(table, 19);
                        baseRow.setLineSpacing(1f);
                        communsPdf.setCell(baseRow, 100, lineatxt2, Color.BLACK, true, "C", 10, communsPdf.setLineStyle(Color.black), "", communsPdf.setPadding2(4f, 0f, 0f, 0f), gray).setValign(VerticalAlignment.MIDDLE);

                        table.draw();

                        table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, margin, document, page, false, true);
                        baseRow = communsPdf.setRow(table, 24);
                        String logoAfirme = "https://storage.googleapis.com/quattrocrm-prod/quattro-biibiic/2306/1N7rQflDvq65bN1u4E4VKLeAGZK1fFh6IdNKDbiGQTo07CL0a2eWI1y6bgGxqbT1/logo.png";
                        communsPdf.setCellImg(baseRow, 33, ImageUtils.readImage(logoAfirme).scale(300, 300), communsPdf.setLineStyle(Color.black, Color.black, Color.black, Color.black), communsPdf.setPadding2(2f, 0f, 0f, 0f), "", "T");
                        table.draw();

                        yStart -= table.getHeaderAndDataHeight() + 24;

                        table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, margin, document, page, true, true);
                        baseRow = communsPdf.setRow(table, 24);
                        communsPdf.setCell(baseRow, 100, "", Color.BLACK, false, "L", 10, communsPdf.setLineStyle(Color.black), "", communsPdf.setPadding2(4f, 0f, 3f, 0f), Color.white);
                        table.draw();

                        Float tb = page.getMediaBox().getHeight() - yStart + 9;
                        texto = new StringBuilder();
                        texto.append("Consentimiento- Certificado individual para formar parte del Seguro de Vida Grupo temporal a un año solicitado a");
                        this.parrafo(document, page, this.medidas(page.getMediaBox(), margin, tb), Sio4CommunsPdf.eliminaHtmlTags3(texto.toString()), 547, PDType1Font.HELVETICA, 10f, (-1.3f * 9f), 1f, 0.2f);

                        tb = tb + 10;
                        texto = new StringBuilder();
                        texto.append("Seguros Afirme, S.A. de C.V., Afirme Grupo Financiero, por el grupo al que pertenezco.");
                        this.parrafo(document, page, this.medidas(page.getMediaBox(), margin, tb), Sio4CommunsPdf.eliminaHtmlTags3(texto.toString()), 547, PDType1Font.HELVETICA, 10f, (-1.3f * 9f), 1f, 0.2f);

                        yStart -= table.getHeaderAndDataHeight() - 1;
                        AseguradosProjection asegurado = datos.getAsegurado();
                        table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, margin, document, page, true, true);
                        baseRow = communsPdf.setRow(table, 12);
                        communsPdf.setCell(baseRow, 100, "Datos Generales", Color.BLACK, true, "L", 10, communsPdf.setLineStyle(Color.black), "", communsPdf.setPadding2(4f, 0f, 0f, 0f), gray).setValign(VerticalAlignment.MIDDLE);
                        baseRow = communsPdf.setRow(table, 11);
                        communsPdf.setCell(baseRow, 50, "Número de Póliza", Color.BLACK, false, "L", 10, communsPdf.setLineStyle(Color.black), "", communsPdf.setPadding2(4f, 0f, 0f, 0f), Color.white);
                        communsPdf.setCell(baseRow, 50, "Vigencias Póliza", Color.BLACK, false, "L", 10, communsPdf.setLineStyle(Color.black), "", communsPdf.setPadding2(4f, 0f, 0f, 0f), Color.white);
                        baseRow = communsPdf.setRow(table, 11);
                        communsPdf.setCell(baseRow, 50, datos.getNumeroPoliza(), Color.BLACK, false, "L", 10, communsPdf.setLineStyle(Color.black), "", communsPdf.setPadding2(4f, 0f, 0f, 0f), Color.white);
                        communsPdf.setCell(baseRow, 50, datos.getFechaDesde() + "  - " + datos.getFechaHasta(), Color.BLACK, false, "L", 10, communsPdf.setLineStyle(Color.black), "", communsPdf.setPadding2(4f, 0f, 0f, 0f), Color.white);
                        baseRow = communsPdf.setRow(table, 11);
                        communsPdf.setCell(baseRow, 50, "Número de Certificado", Color.BLACK, false, "L", 10, communsPdf.setLineStyle(Color.black), "", communsPdf.setPadding2(4f, 0f, 0f, 0f), Color.white);
                        communsPdf.setCell(baseRow, 50, "Vigencia del Certificado", Color.BLACK, false, "L", 10, communsPdf.setLineStyle(Color.black), "", communsPdf.setPadding2(4f, 0f, 0f, 0f), Color.white);

                        baseRow = communsPdf.setRow(table, 11);
                        communsPdf.setCell(baseRow, 50,asegurado.getCertificado(), Color.BLACK, false, "L", 10, communsPdf.setLineStyle(Color.black), "", communsPdf.setPadding2(4f, 0f, 0f, 0f), Color.white);
                        communsPdf.setCell(baseRow, 50,( datos.getFechaDesdeCert() != null ?  datos.getFechaDesdeCert():" " ) +"  - "+(datos.getFechaHastaCert() !=null ? datos.getFechaHastaCert():""), Color.BLACK, false, "L", 10, communsPdf.setLineStyle(Color.black), "", communsPdf.setPadding2(4f, 0f, 0f, 0f), Color.white);

                        baseRow = communsPdf.setRow(table, 11);
                        communsPdf.setCell(baseRow, 100, "Contratante o Razón Social", Color.BLACK, false, "L", 10, communsPdf.setLineStyle(Color.black, Color.black, Color.white, Color.black), "", communsPdf.setPadding2(4f, 0f, 1f, 0f), Color.white);
                        baseRow = communsPdf.setRow(table, 11);
                        communsPdf.setCell(baseRow, 100, datos.getNombre(), Color.BLACK, false, "L", 10, communsPdf.setLineStyle(Color.black, Color.black, Color.black, Color.black), "", communsPdf.setPadding2(4f, 0f, 0f, 0f), Color.white);
                        baseRow = communsPdf.setRow(table, 11);
                        communsPdf.setCell(baseRow, 100, "Nombre(s) del Asegurado, Apellido Paterno, Materno", Color.BLACK, false, "L", 10, communsPdf.setLineStyle(Color.black, Color.black, Color.white, Color.black), "", communsPdf.setPadding2(4f, 0f, 1f, 0f), Color.white);
                        baseRow = communsPdf.setRow(table, 10);
                        communsPdf.setCell(baseRow, 100, asegurado.getNombre(), Color.BLACK, false, "L", 10, communsPdf.setLineStyle(Color.black, Color.black, Color.black, Color.black), "", communsPdf.setPadding2(4f, 0f, 0f, 0f), Color.white);

                        baseRow = communsPdf.setRow(table, 23);
                        communsPdf.setCell(baseRow, 33, "Fecha de nacimiento (día,mes.año)", Color.BLACK, false, "L", 10, communsPdf.setLineStyle(Color.black, Color.black, Color.black, Color.black), "", communsPdf.setPadding2(4f, 0f, 3f, 0f), Color.white);
                        communsPdf.setCell(baseRow, 34, "Fecha de alta al Seguro (día,mes.año)", Color.BLACK, false, "L", 10, communsPdf.setLineStyle(Color.black, Color.black, Color.black, Color.black), "", communsPdf.setPadding2(4f, 0f, 3f, 0f), Color.white);
                        communsPdf.setCell(baseRow, 33, "Sexo", Color.BLACK, false, "L", 10, communsPdf.setLineStyle(Color.black, Color.black, Color.black, Color.black), "", communsPdf.setPadding2(4f, 0f, 3f, 0f), Color.white);
                        baseRow = communsPdf.setRow(table, 21);
                        communsPdf.setCell(baseRow, 33, asegurado.getFechNacimiento(), Color.BLACK, false, "L", 10, communsPdf.setLineStyle(Color.black, Color.black, Color.black, Color.black), "", communsPdf.setPadding2(4f, 0f, 3f, 0f), Color.white);
                        communsPdf.setCell(baseRow, 34, datos.getFechaDesdeCert(), Color.BLACK, false, "L", 10, communsPdf.setLineStyle(Color.black, Color.black, Color.black, Color.black), "", communsPdf.setPadding2(4f, 0f, 3f, 0f), Color.white);
                        communsPdf.setCellImg(baseRow, 33, ImageUtils.readImage(this.urlMascu(asegurado.getSexo())).scale(300, 300), communsPdf.setLineStyle(Color.black, Color.black, Color.black, Color.black), communsPdf.setPadding2(2f, 0f, 0f, 0f), "", "T");
                        baseRow = communsPdf.setRow(table, 13);
                        communsPdf.setCell(baseRow, 100, "Ocupación del Asegurado", Color.BLACK, false, "L", 10, communsPdf.setLineStyle(Color.black, Color.black, Color.white, Color.black), "", communsPdf.setPadding2(4f, 0f, 3f, 0f), Color.white);
                        baseRow = communsPdf.setRow(table, 12);
                        communsPdf.setCell(baseRow, 100, "", Color.BLACK, false, "L", 10, communsPdf.setLineStyle(Color.black, Color.black, Color.black, Color.black), "", communsPdf.setPadding2(4f, 0f, 3f, 0f), Color.white);
                        table.draw();
                        yStart -= table.getHeaderAndDataHeight() + 10;
                        table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, margin, document, page, true, true);
                        baseRow = communsPdf.setRow(table, 11);
                        communsPdf.setCell(baseRow, 100, "Detalle de Coberturas", Color.BLACK, true, "L", 10, communsPdf.setLineStyle(Color.black), "", communsPdf.setPadding2(4f, 0f, 1f, 0f), gray).setValign(VerticalAlignment.MIDDLE);
                        baseRow = communsPdf.setRow(table, 13);
                        communsPdf.setCell(baseRow, 34, "Coberturas(s)", Color.BLACK, false, "L", 10, communsPdf.setLineStyle(Color.black), "", communsPdf.setPadding2(4f, 0f, 0f, 0f), Color.white);
                        communsPdf.setCell(baseRow, 66, "Suma Asegurada o regla para establecerla", Color.BLACK, false, "L", 10, communsPdf.setLineStyle(Color.black), "", communsPdf.setPadding2(4f, 0f, 0f, 0f), Color.white);
                        table.draw();

                        yStart -= table.getHeaderAndDataHeight();
                         List<CoberturaProjection>  cbo = datos.getCoberturas();
                         if(!cbo.isEmpty()){
                        table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, margin, document, page, true, true);
                        for (int i = 0; i < 3; i++) {
                            baseRow = communsPdf.setRow(table, 11);
                            communsPdf.setCell(baseRow, 34, (i == 0 && datos.getTipo() ==2 ? "Fallecimiento" :"")  , Color.BLACK, false, "L", 10, communsPdf.setLineStyle(Color.black), "", communsPdf.setPadding2(4f, 0f, 2f, 0f), Color.white);
                            communsPdf.setCell(baseRow, 66,  (i== 0? cbo.get(0).getSa() :""), Color.BLACK, false, "L", 10, communsPdf.setLineStyle(Color.black), "", communsPdf.setPadding2(4f, 0f, 2f, 0f), Color.white);

                        }
                  
                        table.draw();
                       }
                       
                        yStart -= table.getHeaderAndDataHeight() + 2;
                        tb = page.getMediaBox().getHeight() - yStart + 21;
                        table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, margin, document, page, true, true);
                        baseRow = communsPdf.setRow(table, 46);
                        communsPdf.setCell(baseRow, 100, "", Color.BLACK, false, "L", 10, communsPdf.setLineStyle(Color.black), "", communsPdf.setPadding2(4f, 0f, 2f, 0f), Color.white);
                        table.draw();
                        yStart -= table.getHeaderAndDataHeight();

                        texto = new StringBuilder();

                        if (datos.getTipo() == 1) {
                            texto.append("*Periodo de Espera: Se define como el periodo de 30 (treinta) días posterior inmediato al inicio del Desempleo o el");
                            texto.append("Accidente o la Enfermedad que cause la Invalidez Total Temporal del Asegurado para que comience el beneficio");
                            texto.append("estipulado en la carátula de la Póliza.");
                        }
                        if (datos.getTipo() == 2) {
                            texto.append("Periodo de Espera: Se define como el periodo de 3 meses posterior inmediato al inicio del accidente o la enfermedad ");
                            texto.append("que cause la Invalidez Total y Permanente o la Incapacidad Parcial Permanente del Asegurado para que comience el ");
                            texto.append("beneficio estipulado en la carátula o certificado de la Póliza.");
                        }

                        this.parrafo(document, page, this.medidas(page.getMediaBox(), margin, tb), Sio4CommunsPdf.eliminaHtmlTags3(texto.toString()), 530, PDType1Font.HELVETICA, 10f, (-1.3f * 9f), 1f, 0.1f);

                        table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, margin, document, page, true, true);
                        baseRow = communsPdf.setRow(table, 13);
                        communsPdf.setCell(baseRow, 17, "NIVEL ESCOLAR", Color.BLACK, true, "L", 10, communsPdf.setLineStyle(Color.black,Color.white,Color.black,Color.black), "", communsPdf.setPadding2(4f, 0f, 2f, 0f), Color.white);
                        communsPdf.setCell(baseRow, 83, datos.getNivelEscolar(), Color.BLACK, false, "L", 10, communsPdf.setLineStyle(Color.white,Color.black,Color.black,Color.black), "", communsPdf.setPadding2(4f, 0f, 2f, 0f), Color.white);
                        baseRow = communsPdf.setRow(table, 13);
                        communsPdf.setCell(baseRow, 17, "PLAN DE PAGOS", Color.BLACK, true, "L", 10, communsPdf.setLineStyle(Color.black), "", communsPdf.setPadding2(4f, 0f, 2f, 0f), Color.white);
                        communsPdf.setCell(baseRow, 83, datos.getFormaPago(), Color.BLACK, false, "L", 10, communsPdf.setLineStyle(Color.white,Color.black,Color.black,Color.black), "", communsPdf.setPadding2(4f, 0f, 2f, 0f), Color.white);
                        baseRow = communsPdf.setRow(table, 13);
                        communsPdf.setCell(baseRow, 100, "", Color.BLACK, false, "L", 10, communsPdf.setLineStyle(Color.black), "", communsPdf.setPadding2(4f, 0f, 2f, 0f), Color.white);
                        table.draw();

                        yStart -= table.getHeaderAndDataHeight();
                        table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, margin, document, page, true, true);
                        baseRow = communsPdf.setRow(table, 13);
                        communsPdf.setCell(baseRow, 100, "Designación de Beneficiarios", Color.BLACK, true, "L", 10, communsPdf.setLineStyle(Color.black), "", communsPdf.setPadding2(4f, 0f, 2f, 0f), gray).setValign(VerticalAlignment.MIDDLE);
                        baseRow = communsPdf.setRow(table, 13);
                        communsPdf.setCell(baseRow, 60, "Nombre(s), Apellido Paterno, Materno", Color.BLACK, false, "L", 10, communsPdf.setLineStyle(Color.black), "", communsPdf.setPadding2(4f, 0f, 2f, 0f), Color.white);
                        communsPdf.setCell(baseRow, 10, "%", Color.BLACK, false, "L", 10, communsPdf.setLineStyle(Color.black), "", communsPdf.setPadding2(4f, 0f, 2f, 0f), Color.white);
                        communsPdf.setCell(baseRow, 30, "Designación Irrevocable", Color.BLACK, false, "L", 10, communsPdf.setLineStyle(Color.black), "", communsPdf.setPadding2(4f, 0f, 2f, 0f), Color.white);
                        table.draw();

                        yStart -= table.getHeaderAndDataHeight() + 1;
                        List<BeneficiarioProjection> bene = datos.getBeneficiarios();
                        if(!bene.isEmpty()){

                  
                            table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, margin, document, page, true, true);
                            
                            for (int i = 0; i <3; i++) {
                                baseRow = communsPdf.setRow(table, 13);
                                communsPdf.setCell(baseRow, 60, i== 0 ? bene.get(0).getNombres():"", Color.BLACK, false, "L", 10, communsPdf.setLineStyle(Color.black), "", communsPdf.setPadding2(4f, 0f, 2f, 0f), Color.white).setValign(VerticalAlignment.MIDDLE);
                                communsPdf.setCell(baseRow, 10, i== 0 ? (bene.get(0).getPorcentaje() + "%"):"", Color.BLACK, false, "L", 10, communsPdf.setLineStyle(Color.black), "", communsPdf.setPadding2(4f, 0f, 2f, 0f), Color.white).setValign(VerticalAlignment.MIDDLE);
                                communsPdf.setCellImg(baseRow,30,i== 0 ? ImageUtils.readImage(this.urlsino(1)).scale(120, 100):ImageUtils.readImage(this.urlsino(0)).scale(120, 100) , communsPdf.setLineStyle(Color.black, Color.black, Color.black, Color.black), communsPdf.setPadding2(2f, 0f, 0f, 0f), "", "M");
                            }
                            table.draw();
                        }

                        yStart -= table.getHeaderAndDataHeight() + 2;
                        tb = page.getMediaBox().getHeight() - yStart + 23;
                        table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, margin, document, page, true, true);
                        baseRow = communsPdf.setRow(table, 13);
                        communsPdf.setCell(baseRow, 100, "Advertencias", Color.BLACK, true, "L", 10, communsPdf.setLineStyle(Color.black), "", communsPdf.setPadding2(4f, 0f, 2f, 0f), gray).setValign(VerticalAlignment.MIDDLE);
                        table.draw();

                        yStart -= table.getHeaderAndDataHeight();
                   
                        Float tbHeighty = 145f;
                        if (yStart < 230) {
                            if (pageBreak2) {
                                tbHeighty = 100f;
                            }
                            pageBreak = true;
                        }
                        table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, margin, document, page, true, true);
                        baseRow = communsPdf.setRow(table, tbHeighty);
                        communsPdf.setCell(baseRow, 100, "", Color.BLACK, true, "L", 10, communsPdf.setLineStyle(Color.black), "", communsPdf.setPadding2(4f, 0f, 2f, 0f), Color.white);
                        table.draw();

                        texto = new StringBuilder();
                        texto.append("En el caso de que el asegurado desee nombrar Beneficiarios a menores de edad, no se debe señalar a un mayor de ");
                        texto.append("edad como representante de los menores para efecto de que, en su representación cobre la indemnización.");
                        this.parrafo(document, page, this.medidas(page.getMediaBox(), margin, tb), Sio4CommunsPdf.eliminaHtmlTags3(texto.toString()), 530, arialN, 10f, (-1.3f * 9f), 0.1f, 0.1f);

                        tb = tb + 25;
                        texto = new StringBuilder();
                        texto.append("Lo anterior porque las legislaciones civiles previenen la forma de que debe designarse tutores, albaceas, ");
                        texto.append("representantes de herederos u otros cargos similares y no consideran al contrato de seguro como el instrumento ");
                        texto.append("adecuado para tales designaciones.");
                        this.parrafo(document, page, this.medidas(page.getMediaBox(), margin, tb), Sio4CommunsPdf.eliminaHtmlTags3(texto.toString()), 530, PDType1Font.HELVETICA, 10f, (-1.3f * 9f), 1f, 0.1f);
                        tb = tb + 35;
                        texto = new StringBuilder();
                        texto.append("Lo anterior porque las legislaciones civiles previenen la forma de que debe designarse tutores, albaceas, ");
                        texto.append("representantes de herederos u otros cargos similares y no consideran al contrato de seguro como el instrumento ");
                        texto.append("adecuado para tales designaciones.");

                        this.parrafo(document, page, this.medidas(page.getMediaBox(), margin, tb), Sio4CommunsPdf.eliminaHtmlTags3(texto.toString()), 530, PDType1Font.HELVETICA, 10f, (-1.3f * 9f), 1f, 0.1f);

                        tb = tb + 45;
                        if (pageBreak2) {
                            page = new PDPage();
                            document.addPage(page);
                            yStart = 760;
                            tb = page.getMediaBox().getHeight() - yStart + 10;
                            table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, margin, document, page, true, true);
                            baseRow = communsPdf.setRow(table, 70);
                            communsPdf.setCell(baseRow, 100, "", Color.BLACK, true, "L", 10, communsPdf.setLineStyle(Color.black), "", communsPdf.setPadding2(4f, 0f, 2f, 0f), Color.white);
                            table.draw();
                        }
                        texto = new StringBuilder();
                        texto.append("Si en el presente o en el futuro el Asegurado realiza o se relaciona en actividades ilícitas, se considerará una ");
                        texto.append("agravación del riesgo y cesarán las obligaciones de la Institución derivadas del contrato de seguro.");
                        this.parrafo(document, page, this.medidas(page.getMediaBox(), margin, tb), Sio4CommunsPdf.eliminaHtmlTags3(texto.toString()), 530, PDType1Font.HELVETICA, 10f, (-1.3f * 9f), 1f, 0.1f);

                        if (pageBreak && pageBreak2 == false) {
                            page = new PDPage();
                            document.addPage(page);
                            yStart = 760;
                            tb = page.getMediaBox().getHeight() - yStart + 10;
                            table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, margin, document, page, true, true);
                            baseRow = communsPdf.setRow(table, 70);
                            communsPdf.setCell(baseRow, 100, "", Color.BLACK, true, "L", 10, communsPdf.setLineStyle(Color.black), "", communsPdf.setPadding2(4f, 0f, 2f, 0f), Color.white);
                            table.draw();

                        } else {

                            yStart -= table.getHeaderAndDataHeight();
                            tb = page.getMediaBox().getHeight() - yStart;
                            table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, margin, document, page, true, true);
                            baseRow = communsPdf.setRow(table, 55);
                            communsPdf.setCell(baseRow, 100, "", Color.BLACK, true, "L", 10, communsPdf.setLineStyle(Color.black), "", communsPdf.setPadding2(4f, 0f, 2f, 0f), Color.white);
                            table.draw();
                        }

                        tb = tb + 10;
                        texto = new StringBuilder();
                        texto.append("Transcripción artículos 17 y 18 del Reglamento del Seguro de Grupo para la Operación de Vida y del Seguro");
                        this.parrafo(document, page, this.medidas(page.getMediaBox(), margin + 5, tb), Sio4CommunsPdf.eliminaHtmlTags3(texto.toString()), 530, arialB, 10f, (-1.3f * 10f), 1f, 0.1f);

                        tb = tb + 12;
                        texto = new StringBuilder();

                        texto.append("Colectivo para la Operación de Accidentes y Enfermedades.");
                        this.parrafo(document, page, this.medidas(page.getMediaBox(), margin + 5, tb), Sio4CommunsPdf.eliminaHtmlTags3(texto.toString()), 530, arialB, 10f, (-1.1f * 9f), 1f, 0f);

                        tb = tb + 12;
                        texto = new StringBuilder();
                        texto.append("Artículo 17.");
                        this.parrafo(document, page, this.medidas(page.getMediaBox(), margin + 5, tb), Sio4CommunsPdf.eliminaHtmlTags3(texto.toString()), 530, arialB, 10f, (-1.1f * 9f), 1f, 0f);

                        texto = new StringBuilder();
                        texto.append("Las personas que ingresen al Grupo o Colectividad asegurado con posterioridad a la celebración del");
                        this.parrafo(document, page, this.medidas(page.getMediaBox(), margin + 60, tb), Sio4CommunsPdf.eliminaHtmlTags3(texto.toString()), 530, arialN, 10f, (-1.1f * 9f), 1f, 0.2f);

                        tb = tb + 12;
                        tb = tb + 1;
                        texto = new StringBuilder();
                        texto.append("contrato y que hayan dado su consentimiento para ser asegurados dentro de los treinta días naturales siguientes a su");
                        this.parrafo(document, page, this.medidas(page.getMediaBox(), margin + 6, tb), Sio4CommunsPdf.eliminaHtmlTags3(texto.toString()), 530, arialN, 10f, (-1.3f * 10f), 0f, 0f);

                        page = new PDPage();
                        document.addPage(page);

                        yStart = 750;

                        tb = page.getMediaBox().getHeight() - yStart + 10;
                        table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth - 2, marginx, document, page, true, true);
                        baseRow = communsPdf.setRow(table, 185);
                        communsPdf.setCell(baseRow, 100, "", Color.BLACK, true, "L", 10, communsPdf.setLineStyle(Color.black), "", communsPdf.setPadding2(4f, 0f, 2f, 0f), Color.white);
                        table.draw();

                        texto = new StringBuilder();
                        texto.append("ingreso, quedarán aseguradas con las mismas condiciones con que fue contratada la póliza, desde el momento en ");
                        texto.append("que adquirieron las características para formar parte del Grupo o Colectividad de que se trate.                                     ");
                        texto.append("Con independencia de lo previsto en el párrafo anterior, tratándose de personas que soliciten su ingreso al Grupo o ");
                        texto.append("Colectividad asegurado con posterioridad a la celebración del contrato y que hayan dado su consentimiento después ");
                        texto.append("de los treinta días naturales siguientes a la fecha en que hubieran adquirido el derecho de formar parte del mismo, la ");
                        texto.append("Aseguradora, dentro de los treinta días naturales siguientes a la fecha en que se le haya comunicado esa situación, ");
                        texto.append("podrá exigir requisitos médicos u otros para asegurarlas, si no lo hace quedarán aseguradas con las mismas ");
                        texto.append("condiciones en que fue contratada la póliza.                                                                                                                                          ");
                        texto.append("Cuando la Aseguradora exija requisitos médicos u otros para asegurar a las personas a que se refiere el párrafo ");
                        texto.append("anterior, contará con un plazo de treinta días naturales, contado a partir de la fecha en que se hayan cumplido dichos ");
                        texto.append("requisitos para resolver sobre la aceptación o no de asegurar a la persona, de no hacerlo se entenderá que la acepta ");
                        texto.append("con las mismas condiciones en que fue contratada la póliza.");
                        this.parrafo(document, page, this.medidas(page.getMediaBox(), marginx + 5, tb), Sio4CommunsPdf.eliminaHtmlTags3(texto.toString()), 522, arialN, 10f, (-1.2f * 10f), 0f, 0.2f);

                        tb = tb + 142;
                        texto = new StringBuilder();
                        texto.append("Artículo 18.");
                        this.parrafo(document, page, this.medidas(page.getMediaBox(), margin + 5, tb), Sio4CommunsPdf.eliminaHtmlTags3(texto.toString()), 530, arialB, 10f, (-1.1f * 9f), 1f, 0f);

                        texto = new StringBuilder();
                        texto.append("Las personas que se separen definitivamente del Grupo o Colectividad asegurado, dejarán de estar");
                        this.parrafo(document, page, this.medidas(page.getMediaBox(), margin + 60, tb), Sio4CommunsPdf.eliminaHtmlTags3(texto.toString()), 530, arialN, 10f, (-1.1f * 9f), 1f, 0.3f);

                        tb = tb + 12;
                        texto = new StringBuilder();
                        texto.append("aseguradas desde el momento de su separación, quedando sin validez alguna el Certificado Individual expedido. En ");
                        texto.append("este caso, la Institución restituirá la parte de la prima neta no devengada de dichos integrantes calculada en días ");
                        texto.append("exactos, a quienes la hayan aportado, en la proporción correspondiente.");
                        this.parrafo(document, page, this.medidas(page.getMediaBox(), margin + 5, tb), Sio4CommunsPdf.eliminaHtmlTags3(texto.toString()), 525, arialN, 10f, (-1.1f * 9f), 1f, 0f);

                        yStart -= table.getHeaderAndDataHeight();
                        tb = page.getMediaBox().getHeight() - yStart + 10;

                        table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth - 2, marginx, document, page, true, true);
                        baseRow = communsPdf.setRow(table, 185);
                        communsPdf.setCell(baseRow, 100, "", Color.BLACK, true, "L", 10, communsPdf.setLineStyle(Color.black), "", communsPdf.setPadding2(4f, 0f, 2f, 0f), Color.white);
                        table.draw();

                        texto = new StringBuilder();
                        texto.append("OTORGO MI CONSENTIMIENTO PARA SER ASEGURADO EN LA POLIZA DE SEGURO DE GRUPO DE VIDA ");
                        texto.append("SOLICITADA A SEGUROS AFIRME, S. A. DE C. V. AFIRME GRUPO FINANCIERO, DE ACUERDO A LAS ");
                        texto.append("CONDICIONES GENERALES DE LA POLIZA.                                                                                                                                        ");
                        texto.append("PARA TODOS LOS EFECTOS QUE PUEDA TENER ESTE CONSENTIMIENTO, HAGO CONSTAR QUE LAS ");
                        texto.append("DECLARACIONES CONTENIDAS EN EL MISMO LAS HE HECHO PERSONALMENTE, SON VERIDICAS Y ESTAN ");
                        texto.append("COMPLETAS.");
                        this.parrafo(document, page, this.medidas(page.getMediaBox(), margin + 5, tb), Sio4CommunsPdf.eliminaHtmlTags3(texto.toString()), 526, arialN, 10f, (-1.3f * 9f), 1f, 0f);

                        table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, margin + 3, document, page, false, true);
                        baseRow = communsPdf.setRow(table, 100);
                        communsPdf.setCell(baseRow, 100, "", Color.BLACK, false, "L", 10, communsPdf.setLineStyle(Color.white, Color.white, Color.black, Color.white), "", communsPdf.setPadding2(4f, 0f, 2f, 0f), Color.white);
                        table.draw();

                        yStart -= table.getHeaderAndDataHeight() - 9;

                        table = new BaseTable(yStart, yStartNewPage, bottomMargin, 100, 320 , document, page, false, true);
                        baseRow = communsPdf.setRow(table, 15);
                        communsPdf.setCell(baseRow, 100,datos.getFechaDesdeCert().split("/")[0], Color.BLACK, false, "L", 10, communsPdf.setLineStyle(Color.white, Color.white, Color.black, Color.white), "", communsPdf.setPadding2(4f, 0f, 2f, 0f), Color.white);
                        table.draw();

                         table = new BaseTable(yStart, yStartNewPage, bottomMargin, 100, 420 , document, page, false, true);
                        baseRow = communsPdf.setRow(table, 15);
                        communsPdf.setCell(baseRow, 100,datos.getFechaDesdeCert().split("/")[1], Color.BLACK, false, "L", 10, communsPdf.setLineStyle(Color.white, Color.white, Color.black, Color.white), "", communsPdf.setPadding2(4f, 0f, 2f, 0f), Color.white);
                        table.draw();

                         table = new BaseTable(yStart, yStartNewPage, bottomMargin, 100, 490 , document, page, false, true);
                        baseRow = communsPdf.setRow(table, 15);
                        communsPdf.setCell(baseRow, 100,datos.getFechaDesdeCert().split("/")[2], Color.BLACK, false, "L", 10, communsPdf.setLineStyle(Color.white, Color.white, Color.black, Color.white), "", communsPdf.setPadding2(4f, 0f, 2f, 0f), Color.white);
                        table.draw();

                        table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, margin + 3, document, page, false, true);
                        baseRow = communsPdf.setRow(table, 15);
                        communsPdf.setCell(baseRow, 100, "Lugar____________________________________ el dia ______ de ___________________ de ______", Color.BLACK, false, "L", 10, communsPdf.setLineStyle(Color.white, Color.white, Color.black, Color.white), "", communsPdf.setPadding2(4f, 0f, 2f, 0f), Color.white);
                        table.draw();

                        yStart -= table.getHeaderAndDataHeight() + 55;

                        table = new BaseTable(yStart + 10, yStartNewPage, bottomMargin, fullWidth, margin + 3, document, page, false, true);
                        baseRow = communsPdf.setRow(table, 5);
                        communsPdf.setCell(baseRow, 50, "________________________", Color.BLACK, false, "L", 10, communsPdf.setLineStyle(Color.white, Color.white, Color.black, Color.white), "", communsPdf.setPadding2(4f, 0f, 0f, 0f), Color.white);
                        communsPdf.setCell(baseRow, 50, "___________________________________", Color.BLACK, false, "C", 10, communsPdf.setLineStyle(Color.white, Color.white, Color.black, Color.white), "", communsPdf.setPadding2(4f, 0f, 2f, 0f), Color.white);
                        table.draw();

                        table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, margin + 3, document, page, false, true);
                        baseRow = communsPdf.setRow(table, 15);
                        communsPdf.setCell(baseRow, 50, "FIRMA DEL ASEGURADO", Color.BLACK, false, "L", 10, communsPdf.setLineStyle(Color.white, Color.white, Color.black, Color.white), "", communsPdf.setPadding2(4f, 0f, 2f, 0f), Color.white);
                        communsPdf.setCell(baseRow, 50, "FIRMA DEL FUNCIONARIO AUTORIZADO", Color.BLACK, false, "C", 10, communsPdf.setLineStyle(Color.white, Color.white, Color.black, Color.white), "", communsPdf.setPadding2(4f, 0f, 2f, 0f), Color.white);
                        table.draw();

                        yStart -= table.getHeaderAndDataHeight();

                        table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth - 2, marginx, document, page, true, true);
                        baseRow = communsPdf.setRow(table, 25);
                        communsPdf.setCell(baseRow, 100, "Este consentimiento/certificado será entregado al Asegurado por el Contratante una vez que sea firmado tanto por el propio Asegurado como por el funcionario de la Institución que lo suscriben.", Color.BLACK, true, "L", 10, communsPdf.setLineStyle(Color.black), "", communsPdf.setPadding2(2f, 0f, 2f, 0f), Color.white);
                        table.draw();

                        yStart -= table.getHeaderAndDataHeight();
                        table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth - 2, marginx, document, page, true, true);
                        baseRow = communsPdf.setRow(table, 255);
                        communsPdf.setCell(baseRow, 100, "", Color.BLACK, true, "L", 10, communsPdf.setLineStyle(Color.black), "", communsPdf.setPadding2(2f, 0f, 2f, 0f), Color.white);
                        table.draw();
                        tb = page.getMediaBox().getHeight() - yStart + 10;

                        texto = new StringBuilder();
                        texto.append("Unidad Especializada de Atención a Usuarios (UNE):");
                        this.parrafo(document, page, this.medidas(page.getMediaBox(), margin + 5, tb), Sio4CommunsPdf.eliminaHtmlTags3(texto.toString()), 530, arialB, 10f, (-1.1f * 9f), 1f, 0f);

                        texto = new StringBuilder();
                        texto.append("Av. Juárez 800 Sur, Colonia Centro, Monterrey, Nuevo León,");
                        this.parrafo(document, page, this.medidas(page.getMediaBox(), margin + 257, tb), Sio4CommunsPdf.eliminaHtmlTags3(texto.toString()), 530, arialN, 10f, (-1.1f * 9f), 1f, 0f);

                        tb = tb + 12;

                        texto = new StringBuilder();
                        texto.append("C.P. 64000, entre José María Morelos y Padre Mier. Tel: (81) 8318 3900 ext. 27419 y 24206, correo");
                        this.parrafo(document, page, this.medidas(page.getMediaBox(), margin + 5, tb), Sio4CommunsPdf.eliminaHtmlTags3(texto.toString()), 530, arialN, 10f, (-1.1f * 9f), 1f, 0f);

                        tb = tb + 12;
                        texto = new StringBuilder();
                        texto.append("electrónico:");
                        this.parrafo(document, page, this.medidas(page.getMediaBox(), margin + 5, tb), Sio4CommunsPdf.eliminaHtmlTags3(texto.toString()), 530, arialN, 10f, (-1.1f * 9f), 1f, 0f);

                        yStart = page.getMediaBox().getHeight() - tb;
                        try ( PDPageContentStream con1 = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, true, true)) {
                            float[] components = new float[]{
                                colorLink.getRed() / 255f, colorLink.getGreen() / 255f, colorLink.getBlue() / 255f};
                            PDColor color = new PDColor(components, PDDeviceRGB.INSTANCE);
                            String txtlink = "soluciones@afirme.com";
                            communsPdf.getTextlink(con1, page, margin + 60, (yStart + 20), color, 10, txtlink, true, txtlink, txtlink, true, new Color(6, 6, 255, 0));
                        }

                        tb = tb + 25;
                        texto = new StringBuilder();
                        texto.append("Comisión Nacional de Protección y Defensa al Usuario de los Servicios Financieros (CONDUSEF):");
                        this.parrafo(document, page, this.medidas(page.getMediaBox(), margin + 5, tb), Sio4CommunsPdf.eliminaHtmlTags3(texto.toString()), 530, arialB, 10f, (-1.1f * 9f), 1f, 0.5f);

                        tb = tb + 12;
                        texto = new StringBuilder();
                        texto.append("Avenida Insurgentes Sur 762, Colonia del Valle, Delegación Benito Juárez, Código Postal 03100, Ciudad de");
                        this.parrafo(document, page, this.medidas(page.getMediaBox(), margin + 5, tb), Sio4CommunsPdf.eliminaHtmlTags3(texto.toString()), 530, arialN, 10f, (-1.1f * 9f), 1f, 0.3f);

                        tb = tb + 12;
                        texto = new StringBuilder();
                        texto.append("México, tel. (55) 53.40.09.99, Correo");
                        this.parrafo(document, page, this.medidas(page.getMediaBox(), margin + 5, tb), Sio4CommunsPdf.eliminaHtmlTags3(texto.toString()), 530, arialN, 10f, (-1.1f * 9f), 1f, 0.3f);

                        yStart = page.getMediaBox().getHeight() - tb;
                        try ( PDPageContentStream con2 = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, true, true)) {
                            float[] components = new float[]{
                                colorLink.getRed() / 255f, colorLink.getGreen() / 255f, colorLink.getBlue() / 255f};
                            PDColor color = new PDColor(components, PDDeviceRGB.INSTANCE);
                            String txtlink = "asesoria@condusef.gob.mx,";
                            communsPdf.getTextlink(con2, page, margin + 185, (yStart + 20), color, 10, txtlink, true, txtlink, txtlink, true, new Color(6, 6, 255, 0));
                        }

                        try ( PDPageContentStream con3 = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, true, true)) {
                            float[] components = new float[]{
                                colorLink.getRed() / 255f, colorLink.getGreen() / 255f, colorLink.getBlue() / 255f};
                            PDColor color = new PDColor(components, PDDeviceRGB.INSTANCE);
                            String txtlink = "www.condusef.gob.mx";
                            communsPdf.getTextlink(con3, page, margin + 352, (yStart + 20), color, 10, txtlink, true, txtlink, txtlink, true, new Color(6, 6, 255, 0));
                        }

                        texto = new StringBuilder();
                        texto.append("pagina");
                        this.parrafo(document, page, this.medidas(page.getMediaBox(), margin + 315, tb), Sio4CommunsPdf.eliminaHtmlTags3(texto.toString()), 530, arialN, 10f, (-1.1f * 9f), 1f, 0.3f);

                        tb = tb + 25;
                        texto = new StringBuilder();
                        texto.append("En cumplimiento a lo dispuesto en el artículo 202 de la Ley de Instituciones de Seguros y ");
                        texto.append("de Fianzas, la documentación contractual y la nota técnica que integran este producto de ");
                        texto.append("seguro, quedaron registradas ante la Comisión Nacional de Seguros y Fianzas (CNSF), a ");
                        texto.append("partir del día CNSF-S0094-0126-2023 con el número 07 de marzo de 2023.");
                        this.parrafo(document, page, this.medidas(page.getMediaBox(), margin + 5, tb), Sio4CommunsPdf.eliminaHtmlTags3(texto.toString()), 524, arialB, 12f, (-1.3f * 12f), 1f, 0f);

                        table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth - 2, marginx, document, page, false, true);
                        baseRow = communsPdf.setRow(table, 105);
                        communsPdf.setCell(baseRow, 100, "", Color.BLACK, true, "L", 10, communsPdf.setLineStyle(Color.black), "", communsPdf.setPadding2(2f, 0f, 2f, 0f), Color.white);
                        table.draw();
                        yStart -= table.getHeaderAndDataHeight();

                        table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth - 2, marginx, document, page, false, true);
                        baseRow = communsPdf.setRow(table, 12);
                        communsPdf.setCell(baseRow, 100, "SEGUROS AFIRME S.A. DE C.V., AFIRME GRUPO FINANCIERO", Color.BLACK, true, "C", 10, communsPdf.setLineStyle(Color.black), "", communsPdf.setPadding2(2f, 0f, 2f, 0f), Color.white).setFont(arialB);;
                        baseRow = communsPdf.setRow(table, 12);
                        communsPdf.setCell(baseRow, 100, "Av. Hidalgo 234 Poniente, Colonia Centro, C.P. 64000, Monterrey, Nuevo León, México", Color.BLACK, true, "C", 10, communsPdf.setLineStyle(Color.black), "", communsPdf.setPadding2(2f, 0f, 2f, 0f), Color.white).setFont(arialN);;
                        baseRow = communsPdf.setRow(table, 12);
                        communsPdf.setCell(baseRow, 100, "Teléfono: (81) 8318-3800 | Lunes a Jueves de 8:30 a 18:00 horas, Viernes de 8:30 a 16:00 horas |", Color.BLACK, true, "C", 10, communsPdf.setLineStyle(Color.black), "", communsPdf.setPadding2(2f, 0f, 2f, 0f), Color.white).setFont(arialN);;

                        table.draw();

                        yStart -= table.getHeaderAndDataHeight() + 13;

                        try ( PDPageContentStream con4 = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, true, true)) {
                            float[] components = new float[]{
                                colorLink.getRed() / 255f, colorLink.getGreen() / 255f, colorLink.getBlue() / 255f};
                            PDColor color = new PDColor(components, PDDeviceRGB.INSTANCE);
                            String txtlink = "www.afirmeseguros.com";
                            communsPdf.getTextlink(con4, page, margin + 185, (yStart + 20), color, 10, txtlink, true, txtlink, txtlink, true, new Color(6, 6, 255, 0));
                        }

                        PDFMergerUtility pdfMerger = new PDFMergerUtility();

                        String urladjunto = "https://storage.googleapis.com/quattrocrm-prod/quattro-biibiic/2306/1N7rQflDvq65bN1u4E4VKJcz53MZGzcrcmN0YYlnpdVnZC9MfKxQk8CPuQv8C/certiCompl.pdf";
                        URL scalaByExampleUrl = new URL(urladjunto);
                        final PDDocument documentToBeParsed = PDDocument.load(scalaByExampleUrl.openStream());
                        pdfMerger.appendDocument(document, documentToBeParsed);
                        pdfMerger.mergeDocuments();

                        output = new ByteArrayOutputStream();
                        document.save(output);
                      //  document.save(new File("/home/aalbanil/Vídeos/certificado.pdf"));
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

        private String urlMascu(String tipo) {
            String url = "";
            switch (tipo) {
                  case "Femenino":
                    url = "https://storage.googleapis.com/quattrocrm-prod/quattro-biibiic/2306/1N7rQflDvq65bN1u4E4VKKw5ex35QQWiSbHKpAGIMldT4EFN0czC3L7ck53tsxyS/Fem05.png";
                    break;
               case "Masculino":
                    url = "https://storage.googleapis.com/quattrocrm-prod/quattro-biibiic/2306/1N7rQflDvq65bN1u4E4VKEo0UnOetTtvexg0vQfH4gFmQVNCX7KlJLx5wrxwN/Masc050.png";
                    break;
                default:
                    url = "https://storage.googleapis.com/quattrocrm-prod/quattro-biibiic/2306/1N7rQflDvq65bN1u4E4VKPCR6Bj3pzHFIHNv4py6gV1hw4nL747RpnfsHsnwpEa/Fmv.png";

            }
            return url;
        }

        private String urlsino(int tipo) {
            String url = "";
            switch (tipo) {
                case 1:
                    url = "https://storage.googleapis.com/quattrocrm-prod/quattro-biibiic/2306/1N7rQflDvq65bN1u4E4VKEW9uJOVzWt2ENujU3dpEKc7vz1H5gV6PXBNAlEmXhz/sis.png";
                    break;

                case 2:
                    url = "https://storage.googleapis.com/quattrocrm-prod/quattro-biibiic/2306/1N7rQflDvq65bN1u4E4VKGeTsxOXcVMjQYst6XgPGoIFmQVNCX7KlJLx5wrxwN/nos.png";
                    break;
                default:
                    url = "https://storage.googleapis.com/quattrocrm-prod/quattro-biibiic/2306/1N7rQflDvq65bN1u4E4VKHzWNReWT90aUeZbhAHeYqrmk2yhx00TLfhpFbUBcSR/sino.png";

            }
            return url;
        }

        private static void addParagraph(PDPageContentStream contentStream, float width, float sx,
                float sy, String text, boolean justify, PDFont FONT, Float FONT_SIZE, Float LEADING, Float SPACING) throws IOException {
            List<String> lines = parseLines(text, width, FONT, FONT_SIZE, LEADING);

            contentStream.setNonStrokingColor(Color.black);
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

        public PDPageContentStream parrafo(PDDocument document, PDPage page, List<Float> lines, String tetxo, int i,PDFont FONT, Float FONT_SIZE, Float LEADING, Float paddig, float SPACING) {
            try {
                PDPageContentStream contentStream = new PDPageContentStream(document, page,PDPageContentStream.AppendMode.APPEND, true);
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