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
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import com.copsis.clients.projections.AseguradosProjection;
import com.copsis.clients.projections.CaractulaProjection;
import com.copsis.clients.projections.CoberturaProjection;
import com.copsis.exceptions.GeneralServiceException;
import com.copsis.models.Tabla.BaseTable;
import com.copsis.models.Tabla.ImageUtils;
import com.copsis.models.Tabla.Row;
import com.copsis.models.Tabla.Sio4CommunsPdf;
import com.copsis.models.Tabla.VerticalAlignment;

public class ImpresionCertificadoArgos {
   
    private float yStartNewPage = 780, yStart = 778, bottomMargin = 32, fullWidth = 570, margin = 20, marginx = 56,yStartY=0;
    private Sio4CommunsPdf communsPdf = new Sio4CommunsPdf();
    private final Color green = new Color(40, 134, 74, 0);
    private final Color gray = new Color(226, 228, 226, 0);

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

                        AseguradosProjection asegurado = datos.getAsegurado();
                        table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, margin, document, page, false, true);
                        baseRow = communsPdf.setRow(table, 40);
                        String logoAfirme = "https://storage.googleapis.com/quattrocrm/aseguradoras/39_280x140.png";
                        communsPdf.setCellImg(baseRow, 30, ImageUtils.readImage(logoAfirme).scale(90, 90), communsPdf.setLineStyle(Color.black, Color.black, Color.black, Color.black), communsPdf.setPadding2(2f, 0f, 0f, 0f), "", "T");
                        table.draw();
                        
                        yStart -= table.getHeaderAndDataHeight()+1; 

                        table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, margin , document, page, true, true);
                        baseRow = communsPdf.setRow(table, 16);
                        communsPdf.setCell(baseRow, 100, "CERTIFICADO", Color.white, true, "C", 10, communsPdf.setLineStyle(Color.black), "", communsPdf.setPadding2(4f, 0f, 2f, 0f), green).setValign(VerticalAlignment.MIDDLE);
                        table.draw();

                         yStart -= table.getHeaderAndDataHeight()+7; 
                        table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth/2+25, margin , document, page, true, true);
                        baseRow = communsPdf.setRow(table, 25);
                        communsPdf.setCell(baseRow, 30, "PÓLIZA", Color.black, true, "C", 10, communsPdf.setLineStyle(Color.black), "", communsPdf.setPadding2(4f, 0f, 2f, 0f), gray).setValign(VerticalAlignment.MIDDLE);
                        communsPdf.setCell(baseRow, 70, "MATRÍCULA DEL ASEGURADO", Color.black, true, "C", 10, communsPdf.setLineStyle(Color.black), "", communsPdf.setPadding2(4f, 0f, 2f, 0f), gray).setValign(VerticalAlignment.MIDDLE);
                        baseRow = communsPdf.setRow(table, 38);
                        communsPdf.setCell(baseRow, 30, datos.getNumeroPoliza(), Color.black, false, "L", 10, communsPdf.setLineStyle(Color.black), "", communsPdf.setPadding2(4f, 0f, 2f, 0f),Color.white).setValign(VerticalAlignment.MIDDLE);
                        communsPdf.setCell(baseRow, 70, asegurado.getNomina(), Color.black, false, "C", 10, communsPdf.setLineStyle(Color.black), "", communsPdf.setPadding2(4f, 0f, 2f, 0f),Color.white).setValign(VerticalAlignment.MIDDLE);                   
                        table.draw();

                         table = new BaseTable(yStart, yStartNewPage, bottomMargin, 253, margin+318 , document, page, true, true);
                        baseRow = communsPdf.setRow(table, 15);
                        communsPdf.setCell(baseRow, 100, "VIGENCIA", Color.black, true, "C", 10, communsPdf.setLineStyle(Color.black), "", communsPdf.setPadding2(4f, 0f, 2f, 0f), gray).setValign(VerticalAlignment.MIDDLE);
                        
                         baseRow = communsPdf.setRow(table, 20);
                        communsPdf.setCell(baseRow, 50, "DESDE", Color.black, true, "C", 10, communsPdf.setLineStyle(Color.black), "", communsPdf.setPadding2(4f, 0f, 2f, 0f), gray).setValign(VerticalAlignment.MIDDLE);
                        communsPdf.setCell(baseRow, 50, "HASTA", Color.black, true, "C", 10, communsPdf.setLineStyle(Color.black), "", communsPdf.setPadding2(4f, 0f, 2f, 0f), gray).setValign(VerticalAlignment.MIDDLE);
                        baseRow = communsPdf.setRow(table, 27);
                        communsPdf.setCell(baseRow, 50, datos.getFechaDesdeCert(), Color.black, false, "C", 10, communsPdf.setLineStyle(Color.black), "", communsPdf.setPadding2(4f, 0f, 2f, 0f),Color.white).setValign(VerticalAlignment.MIDDLE);
                        communsPdf.setCell(baseRow, 50, datos.getFechaHastaCent(), Color.black, false, "C", 10, communsPdf.setLineStyle(Color.black), "", communsPdf.setPadding2(4f, 0f, 2f, 0f),Color.white).setValign(VerticalAlignment.MIDDLE);                   
                        table.draw();

                        yStart -= table.getHeaderAndDataHeight()+9; 

                         table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth/2+25, margin , document, page, true, true);
                        baseRow = communsPdf.setRow(table, 25);
                        communsPdf.setCell(baseRow, 100, "NOMBRE DEL ASEGURADO Y/O CONTRATANTE", Color.black, true, "C", 10, communsPdf.setLineStyle(Color.black), "", communsPdf.setPadding2(4f, 0f, 2f, 0f), gray).setValign(VerticalAlignment.MIDDLE);
                        
                        baseRow = communsPdf.setRow(table, 38);
                        communsPdf.setCell(baseRow, 100, datos.getNombre(), Color.black, false, "L", 10, communsPdf.setLineStyle(Color.black), "", communsPdf.setPadding2(4f, 0f, 2f, 0f),Color.white).setValign(VerticalAlignment.MIDDLE);
                        
                        table.draw();

                        table = new BaseTable(yStart, yStartNewPage, bottomMargin, 253, margin+318 , document, page, true, true);                                                
                        baseRow = communsPdf.setRow(table, 25);
                        communsPdf.setCell(baseRow, 50, "FECHA DE NACIMIENTO", Color.black, true, "C", 10, communsPdf.setLineStyle(Color.black), "", communsPdf.setPadding2(4f, 0f, 2f, 0f), gray).setValign(VerticalAlignment.MIDDLE);
                        communsPdf.setCell(baseRow, 50, "SEXO", Color.black, true, "C", 10, communsPdf.setLineStyle(Color.black), "", communsPdf.setPadding2(4f, 0f, 2f, 0f), gray).setValign(VerticalAlignment.MIDDLE);
                        baseRow = communsPdf.setRow(table, 38);
                        communsPdf.setCell(baseRow, 50, asegurado.getFechNacimiento(), Color.black, false, "C", 10, communsPdf.setLineStyle(Color.black), "", communsPdf.setPadding2(4f, 0f, 2f, 0f),Color.white).setValign(VerticalAlignment.MIDDLE);
                        communsPdf.setCellImg(baseRow, 50, ImageUtils.readImage(this.urlMasFem(asegurado.getSexo())).scale(105, 50), communsPdf.setLineStyle(Color.black, Color.black, Color.black, Color.black), communsPdf.setPadding2(2f, 0f, 0f, 0f), "", "T");
                        table.draw();


                         yStart -= table.getHeaderAndDataHeight()+5; 

                        table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, margin , document, page, true, true);                                                
                        baseRow = communsPdf.setRow(table, 22);
                        communsPdf.setCell(baseRow, 100, "NOMBRE DEL ALUMNO", Color.black, true, "C", 10, communsPdf.setLineStyle(Color.black), "", communsPdf.setPadding2(4f, 0f, 2f, 0f), gray).setValign(VerticalAlignment.MIDDLE);                       
                        baseRow = communsPdf.setRow(table, 26);
                        communsPdf.setCell(baseRow, 100, asegurado.getNombre(), Color.black, false, "L", 10, communsPdf.setLineStyle(Color.black), "", communsPdf.setPadding2(4f, 0f, 2f, 0f),Color.white).setValign(VerticalAlignment.MIDDLE);                       
                        table.draw();

                        yStart -= table.getHeaderAndDataHeight()+5; 
                        table = new BaseTable(yStart, yStartNewPage, bottomMargin, 100, margin , document, page, true, true);                                                
                        baseRow = communsPdf.setRow(table, 18);
                        communsPdf.setCell(baseRow, 100, "NIVEL ESCOLAR:", Color.black, true, "C", 10, communsPdf.setLineStyle(Color.black), "", communsPdf.setPadding2(4f, 0f, 2f, 0f), gray).setValign(VerticalAlignment.MIDDLE);
                        table.draw();

                        table = new BaseTable(yStart, yStartNewPage, bottomMargin, 210, margin+103 , document, page, true, true);                                                
                        baseRow = communsPdf.setRow(table, 18);
                        communsPdf.setCell(baseRow, 100, datos.getNivelEscolar(), Color.black, false, "L", 10, communsPdf.setLineStyle(Color.black), "", communsPdf.setPadding2(4f, 0f, 2f, 0f), Color.white).setValign(VerticalAlignment.MIDDLE);                                                                       
                        table.draw();
                        
                        table = new BaseTable(yStart, yStartNewPage, bottomMargin, 110, margin+325 , document, page, true, true);                                                
                        baseRow = communsPdf.setRow(table, 18);
                        communsPdf.setCell(baseRow, 100, "PLAN DE PAGOS:", Color.black, true, "C", 10, communsPdf.setLineStyle(Color.black), "", communsPdf.setPadding2(4f, 0f, 2f, 0f), gray).setValign(VerticalAlignment.MIDDLE);
                        table.draw();

                        table = new BaseTable(yStart, yStartNewPage, bottomMargin, 132, margin+438 , document, page, true, true);                                                
                        baseRow = communsPdf.setRow(table, 18);
                        communsPdf.setCell(baseRow, 100, datos.getFormaPago(), Color.black, false, "C", 10, communsPdf.setLineStyle(Color.black), "", communsPdf.setPadding2(4f, 0f, 2f, 0f), Color.white).setValign(VerticalAlignment.MIDDLE);                                                                       
                        table.draw();

                         yStart -= table.getHeaderAndDataHeight(); 

                        table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, margin , document, page, false, true);                                                
                        baseRow = communsPdf.setRow(table, 14);
                        communsPdf.setCell(baseRow, 58, "(Bachillerato, Licenciatura, Licenciatura Ejecutiva, Posgrado)", Color.black, false, "L", 8, communsPdf.setLineStyle(Color.black), "", communsPdf.setPadding2(0f, 0f, 2f, 0f), gray).setValign(VerticalAlignment.MIDDLE);
                        communsPdf.setCell(baseRow, 42, "(4,6,10)", Color.black, false, "L", 8, communsPdf.setLineStyle(Color.black), "", communsPdf.setPadding2(4f, 0f, 2f, 0f), gray).setValign(VerticalAlignment.MIDDLE);
                        table.draw();

                        /**Tabla 2 */

                          yStart -= table.getHeaderAndDataHeight()+5; 
                           List<CoberturaProjection>  cbo = datos.getCoberturas();
                        table = new BaseTable(yStart, yStartNewPage, bottomMargin, 100, margin , document, page, true, true);                                                
                        baseRow = communsPdf.setRow(table, 22);
                        communsPdf.setCell(baseRow, 100, "DESCRIPCIÓN DE LA COBERTURA:", Color.black, true, "C", 10, communsPdf.setLineStyle(Color.black), "", communsPdf.setPadding2(4f, 0f, 2f, 0f), gray).setValign(VerticalAlignment.MIDDLE);
                        table.draw();

                        table = new BaseTable(yStart, yStartNewPage, bottomMargin, 170, margin+103 , document, page, true, true);                                                
                        baseRow = communsPdf.setRow(table, 23);
                        communsPdf.setCell(baseRow, 100, "", Color.black, true, "C", 10, communsPdf.setLineStyle(Color.black), "", communsPdf.setPadding2(4f, 0f, 2f, 0f), Color.white).setValign(VerticalAlignment.MIDDLE);                                                                       
                        table.draw();
                        
                        table = new BaseTable(yStart, yStartNewPage, bottomMargin, 150, margin+279 , document, page, true, true);                                                
                        baseRow = communsPdf.setRow(table, 18);
                        communsPdf.setCell(baseRow, 100, "SUMA ASEGURADA hasta:", Color.black, false, "C", 10, communsPdf.setLineStyle(Color.black), "", communsPdf.setPadding2(4f, 0f, 2f, 0f), gray).setValign(VerticalAlignment.MIDDLE);
                        table.draw();

                        table = new BaseTable(yStart, yStartNewPage, bottomMargin, 132, margin+438 , document, page, true, true);                                                
                        baseRow = communsPdf.setRow(table, 18);
                        communsPdf.setCell(baseRow, 100, cbo.get(0).getSa(), Color.black, false, "C", 10, communsPdf.setLineStyle(Color.black), "", communsPdf.setPadding2(4f, 0f, 2f, 0f), Color.white).setValign(VerticalAlignment.MIDDLE);                                                                       
                        table.draw();

                         yStart -= table.getHeaderAndDataHeight()+1; 

                        table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, margin , document, page, false, true);                                                
                        baseRow = communsPdf.setRow(table, 14);
                        communsPdf.setCell(baseRow, 100, "(Desempleo Involuntario / Invalidez Total Temporal)", Color.black, false, "L", 8, communsPdf.setLineStyle(Color.black), "", communsPdf.setPadding2(0f, 0f, 2f, 0f), gray).setValign(VerticalAlignment.MIDDLE);                        
                        table.draw();

                        yStart -= table.getHeaderAndDataHeight()+1; 

                       
                 



              
                        Float tb = page.getMediaBox().getHeight() - yStart + 9;   
                        texto = new StringBuilder();
                        texto.append("Edad máxima");                                                     
                        this.parrafo(document, page, this.medidas(page.getMediaBox(), margin, tb), Sio4CommunsPdf.eliminaHtmlTags3(texto.toString()), 547, PDType1Font.HELVETICA_BOLD, 8.8f, (-1.3f * 9f), 1f, 0.2f);                        
                        texto = new StringBuilder();
                        texto.append("de aceptación para el titular del pago 64 años con cancelación a los 65 años.");
                        this.parrafo(document, page, this.medidas(page.getMediaBox(), margin+60, tb), Sio4CommunsPdf.eliminaHtmlTags3(texto.toString()), 547, PDType1Font.HELVETICA, 8.8f, (-1.3f * 9f), 1f, 0.2f);
                       
                        /**Parro 2 */

                        tb = tb+15;
                        texto = new StringBuilder();
                        texto.append("Desempleo Involuntario:");
                        this.parrafo(document, page, this.medidas(page.getMediaBox(), margin, tb), Sio4CommunsPdf.eliminaHtmlTags3(texto.toString()), 547, PDType1Font.HELVETICA_BOLD, 8.8f, (-1.3f * 9f), 1f, 0.0f);                        
                        
                        texto = new StringBuilder();
                        texto.append("se indemnizará al colegio el número de parcialidades o hasta el monto máximo descrito en el apartado de Suma ");
                        this.parrafo(document, page, this.medidas(page.getMediaBox(), margin+110, tb), Sio4CommunsPdf.eliminaHtmlTags3(texto.toString()), 547, PDType1Font.HELVETICA, 8.9f, (-1.3f * 9f), 1f, 0.1f);
                        
                        tb = tb+12;
                        texto = new StringBuilder();
                        texto.append("Asegurada de este certificado, siempre y cuando el asegurado trabaje de forma independiente sin un contrato de empleo, sujeto a los términos y ");
                        
                        this.parrafo(document, page, this.medidas(page.getMediaBox(), margin, tb), Sio4CommunsPdf.eliminaHtmlTags3(texto.toString()), 570, arialN, 8.8f, (-1f * 9f), 1f, 0f);
                        
                        tb = tb+12;
                        texto = new StringBuilder();                        
                        texto.append("condiciones de Seguros Argos.");
                        this.parrafo(document, page, this.medidas(page.getMediaBox(), margin, tb), Sio4CommunsPdf.eliminaHtmlTags3(texto.toString()), 547, PDType1Font.HELVETICA, 9f, (-1.1f * 9f), 1f, 0f);
                        
                            /**Parro 3 */


                         tb = tb+15;
                        texto = new StringBuilder();
                        texto.append("Invalidez Total temporal:");
                        this.parrafo(document, page, this.medidas(page.getMediaBox(), margin, tb), Sio4CommunsPdf.eliminaHtmlTags3(texto.toString()), 547, PDType1Font.HELVETICA_BOLD, 8.8f, (-1.3f * 9f), 1f, 0.0f);                        
                        
                        texto = new StringBuilder();
                        texto.append("se indemnizará al colegio el número de parcialidades o hasta el monto máximo descrito en el apartado de Suma ");
                        this.parrafo(document, page, this.medidas(page.getMediaBox(), margin+110, tb), Sio4CommunsPdf.eliminaHtmlTags3(texto.toString()), 547, PDType1Font.HELVETICA, 8.9f, (-1.3f * 9f), 1f, 0.1f);
                        
                        tb = tb+12;
                        texto = new StringBuilder();
                        texto.append("Asegurada de este certificado, siempre y cuando el asegurado trabaje de forma independiente sin un contrato de empleo, sujeto a los términos y ");
                        
                        this.parrafo(document, page, this.medidas(page.getMediaBox(), margin, tb), Sio4CommunsPdf.eliminaHtmlTags3(texto.toString()), 570, arialN, 8.8f, (-1f * 9f), 1f, 0f);
                        
                        tb = tb+12;
                        texto = new StringBuilder();                        
                        texto.append("condiciones de Seguros Argos.");
                        this.parrafo(document, page, this.medidas(page.getMediaBox(), margin, tb), Sio4CommunsPdf.eliminaHtmlTags3(texto.toString()), 547, PDType1Font.HELVETICA, 9f, (-1.1f * 9f), 1f, 0f);
                       

                            /**Parro 4 */

                          tb = tb+15;
                             texto = new StringBuilder();
                        texto.append("Periodo de Espera:");
                        this.parrafo(document, page, this.medidas(page.getMediaBox(), margin, tb), Sio4CommunsPdf.eliminaHtmlTags3(texto.toString()), 547, PDType1Font.HELVETICA_BOLD, 8.8f, (-1.3f * 9f), 1f, 0.0f);                        
                        
                        texto = new StringBuilder();
                        texto.append("30 (treinta) días, se define como el periodo posterior inmediato al inicio del Desempleo Involuntario o que cause la Invalidez");
                        this.parrafo(document, page, this.medidas(page.getMediaBox(), margin+82, tb), Sio4CommunsPdf.eliminaHtmlTags3(texto.toString()), 547, PDType1Font.HELVETICA, 8.8f, (-1.3f * 9f), 1f, 0f);
                        
                        tb = tb+12;
                        texto = new StringBuilder();
                        texto.append("Total Temporal del Asegurado y estará estipulado en la carátula o certificado de la Póliza. Aplica demás términos y condiciones de Seguros ");
                        
                        this.parrafo(document, page, this.medidas(page.getMediaBox(), margin, tb), Sio4CommunsPdf.eliminaHtmlTags3(texto.toString()), 570, arialN, 8.9f, (-1f * 9f), 1f, 0.1f);
                          tb = tb+12;
                        texto = new StringBuilder();                        
                        texto.append("Argos.");
                        this.parrafo(document, page, this.medidas(page.getMediaBox(), margin, tb), Sio4CommunsPdf.eliminaHtmlTags3(texto.toString()), 547, PDType1Font.HELVETICA, 9f, (-1.1f * 9f), 1f, 0f);
                       
                        /*Parro 5 */
                          tb = tb+15;
                        texto = new StringBuilder();
                        texto.append("Información importante:");
                        this.parrafo(document, page, this.medidas(page.getMediaBox(), margin, tb), Sio4CommunsPdf.eliminaHtmlTags3(texto.toString()), 547, PDType1Font.HELVETICA_BOLD, 8.8f, (-1.3f * 9f), 1f, 0.1f);                        
                        
                        texto = new StringBuilder();
                        texto.append("Este documento tiene efectos informativos, por lo que no sustituye el Certificado Individual del Seguro de Vida Grupo");
                        this.parrafo(document, page, this.medidas(page.getMediaBox(), margin+110, tb), Sio4CommunsPdf.eliminaHtmlTags3(texto.toString()), 547, PDType1Font.HELVETICA, 8.8f, (-1.3f * 9f), 1f, 0f);
                        
  
                        tb = tb+10;
                        texto = new StringBuilder();
                        texto.append("que otorga la aseguradora, registrado ante la Comisión Nacional de Seguros y Fianzas, para efectos de lo dispuesto en el artículo 16 del Regla-");
                        texto.append("mento del Seguro de Grupo para la Operación de Vida y del Seguro Colectivo para la Operación de Accidentes y Enfermedades, el cual Usted ");
                        texto.append("debe solicitar a Seguros Argos, S.A. de C.V. al número telefónico 800.265.20.20, indicando el número de póliza que se indica aquí mismo.");
                         this.parrafo(document, page, this.medidas(page.getMediaBox(), margin, tb), Sio4CommunsPdf.eliminaHtmlTags3(texto.toString()), 565, arialN, 8.8f, (-1.3f * 9f), 1f, 0f);
                        



                          yStart -= table.getHeaderAndDataHeight()+180; 

                        table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, margin , document, page, true, true);                                                
                        baseRow = communsPdf.setRow(table, 12);
                        communsPdf.setCell(baseRow, 100, "AVISO DE PRIVACIDAD", Color.black, false, "c",9, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding2(0f, 0f, 2f, 0f), gray).setFont(arialN);
                        
                        table.draw();
                         yStart -= table.getHeaderAndDataHeight(); 

                        tb = page.getMediaBox().getHeight() - yStart+8;   
                        texto = new StringBuilder();
                        texto.append("Al aceptar ser Asegurado, Usted otorga su consentimiento para que Seguros Argos, S.A. de C.V. (en adelante “SEGUROS ARGOS”) realice el tratamiento de sus Datos Personales, de conformidad con lo previsto en la Ley Federal de");
                        texto.append("Protección de Datos Personales en Posesión de Particulares (LFPDPPP) y su Reglamento, considerando lo siguiente: 1.- ¿Quiénes somos? Seguros Argos, S.A. de C.V. (en lo sucesivo “SEGUROS ARGOS”) es una institución de seguros");
                        texto.append("autorizada por el Gobierno Federal a través de la Secretaría de Hacienda y Crédito Público, con domicilio en Tecoyotitla 412, Col. Ex-Hacienda de Guadalupe Chimalistac, C.P. 01050, Alcaldía Álvaro Obregón, Ciudad de México. 2.- ¿Qué");
                        texto.append("tipo de Datos Personales recabamos para el servicio de aseguramiento señalado en este documento? Datos de carácter general: Nombre Completo, Edad o Fecha de Nacimiento, País de Nacimiento, Nacionalidad, Ocupación, Profesión,");
                        texto.append("Actividad o Giro del Negocio, Domicilio Particular en su lugar de residencia, Teléfono, Correo Electrónico, CURP y RFC con Homoclave. 3.- ¿Para qué nos proporciona sus Datos Personales? Los Datos Personales se utilizan para: a) Fines");
                        texto.append("Primarios: 1. Evaluar y, en su caso, aceptar el riesgo que se propone para la celebración del contrato de seguro; 2. Realizar la cobranza de la(s) prima(s) correspondientes a la(s) póliza(s) de seguro que se contrate(n); 3. Llevar a cabo la emisión");
                        texto.append("y entrega de la póliza de seguro que corresponda, endosos y/o certificados individuales (según el tipo de seguro de que se trate), 4. Atender consultas y reclamaciones de pago de siniestros; y 6. Para todos los demás fines previstos en nuestro");
                        texto.append("Aviso de Privacidad Integral. b) Fines Secundarios: Su información general puede ser utilizada para fines publicitarios y promocionales, así como para los demás fines que se especifican en nuestro Aviso de Privacidad Integral. 4.- Medios para");
                        texto.append("el ejercicio de derechos del Titular de Datos Personales (Derechos ARCO). Usted puede ejercer los derechos de Acceso, Rectificación, Cancelación y Oposición previstos en la LFPDPPP (en adelante los “Derechos ARCO”), presentando ante");
                        texto.append("la Oficina de Privacidad de SEGUROS ARGOS una solicitud por escrito. Dicha petición deberá contener cuando menos lo siguiente: (i) Nombre del Titular de los Datos Personales; (ii) Domicilio para recibir comunicaciones de SEGUROS");
                        texto.append("ARGOS; (iii) Documentos que acrediten su identidad. En caso de ser Representante Legal, el instrumento del que se desprendan sus facultades de representación; (iv) Descripción clara y precisa de los datos personales respecto de los que");
                        texto.append("se busca ejercer los derechos; (v) Cualquier otro elemento o documento que facilite la localización de los datos personales, (vi) Cuando la solicitud corresponda a la rectificación de datos personales, el Titular debe indicar las modificaciones a");
                        texto.append("realizarse y aportar la documentación que sustente su petición y (vii) Cualquier otro elemento de conformidad con la legislación y con la última Política de Privacidad que se encuentren vigentes al momento de la presentación de su solicitud.");
                        texto.append("Tratándose de solicitudes de acceso a la información que resulten procedentes, SEGUROS ARGOS proporcionará la misma ya sea a través de copias simples o bien mediante copias simples digitalizadas. Los datos de contacto para la");
                        texto.append("presentación de las solicitudes referidas en el párrafo anterior son los siguientes: Oficina de Privacidad. Domicilio: Tecoyotitla No. 412, Colonia Ex-Hacienda De Guadalupe Chimalistac, C.P. 01050, Alcaldía Álvaro Obregón, Ciudad de México.");
                        texto.append("Correo Electrónico: oficinadeprivacidad@segurosargos.com. 5.- Transferencia de Datos. Los datos personales de Clientes y Usuarios se podrán transferir a terceros para (a) cumplir con las disposiciones legales vigentes; (b) en acatamiento");
                        texto.append("a mandamiento u orden judicial, (c) siempre que sea necesario para la operación y funcionamiento y/o para ejercer algún derecho o cumplir con alguna obligación de SEGUROS ARGOS y (d) para los demás efectos que se precisan en nuestro");
                        texto.append("Aviso de Privacidad Integral www.segurosargos.com. 6.- Aviso de Privacidad Integral. Nuestro aviso de privacidad completo y nuestra política de privacidad, los puede consultar en www.segurosargos.com. Fecha de Publicación: Octubre,");
                        texto.append("2010. 7.- Modificaciones al Aviso de Privacidad. SEGUROS ARGOS se reserva el derecho de modificar su Aviso y Política de Privacidad de vez en vez en su página web www.segurosargos.com. Última actualización: Mayo 14, 2021. 8.-");
                        texto.append("Quejas. En caso de diferencias o inconformidades que surjan respecto de los derechos de nuestros Clientes y Usuarios a la protección de sus datos personales, éstos pueden interponer queja correspondiente ante el Instituto Nacional de");
                        texto.append("Transparencia, Acceso a la Información y Protección de Datos Personales (INAI), en términos de lo previsto en la LFPDPPP. Para mayor información visite www.inai.org.mx.");                                            
                        this.parrafo(document, page, this.medidas(page.getMediaBox(), margin, tb), Sio4CommunsPdf.eliminaHtmlTags3(texto.toString()), 565, arialN, 6f, (-0.8f * 9f), 1f, 0f);
                        

                       

                        /**TABLA 1 */
                        
                        yStart = yStart-190;
                        table = new BaseTable(yStart, yStartNewPage, 0, 200, margin , document, page, true, true);                                                
                        baseRow = communsPdf.setRow(table, 5);
                        communsPdf.setCell(baseRow, 100, "", Color.black, true, "C", 10, communsPdf.setLineStyle(Color.white,Color.white,Color.black,Color.white), "", communsPdf.setPadding2(4f, 0f, 2f, 0f), Color.white).setValign(VerticalAlignment.MIDDLE);                    
                        table.draw();

                        table = new BaseTable(yStart, yStartNewPage, 0, 150, 250 , document, page, true, true);                                                
                        baseRow = communsPdf.setRow(table, 5);
                        communsPdf.setCell(baseRow, 100, "", Color.black, true, "C", 10, communsPdf.setLineStyle(Color.white,Color.white,Color.black,Color.white), "", communsPdf.setPadding2(4f, 0f, 2f, 0f), Color.white).setValign(VerticalAlignment.MIDDLE);                    
                        table.draw();
                        table = new BaseTable(yStart, yStartNewPage, 0, 150, 435 , document, page, true, true);                                                
                        baseRow = communsPdf.setRow(table, 5);
                        communsPdf.setCell(baseRow, 100, "", Color.black, true, "C", 10, communsPdf.setLineStyle(Color.white,Color.white,Color.black,Color.white), "", communsPdf.setPadding2(4f, 0f, 2f, 0f), Color.white).setValign(VerticalAlignment.MIDDLE);                    
                        table.draw();

                       
                        /**TABLA 2 */


                         yStart -= table.getHeaderAndDataHeight();
                         table = new BaseTable(yStart, yStartNewPage, 0, 200, margin , document, page, false, true);                                                
                        baseRow = communsPdf.setRow(table, 18);
                        communsPdf.setCell(baseRow, 100, "LUGAR Y FECHA DE EMISIÓN", Color.black, false, "C", 10, communsPdf.setLineStyle(Color.white,Color.white,Color.black,Color.white), "", communsPdf.setPadding2(4f, 0f, 2f, 0f), Color.white).setValign(VerticalAlignment.MIDDLE);                    
                        table.draw();
                    
                        table = new BaseTable(yStart, yStartNewPage, 0, 150, 250 , document, page, false, true);                                                
                        baseRow = communsPdf.setRow(table, 18);
                        communsPdf.setCell(baseRow, 100, "ASEGURADO", Color.black, false, "C", 10, communsPdf.setLineStyle(Color.white,Color.white,Color.black,Color.white), "", communsPdf.setPadding2(4f, 0f, 2f, 0f), Color.white).setValign(VerticalAlignment.MIDDLE);                    
                        table.draw();
                        table = new BaseTable(yStart, yStartNewPage, 0, 150, 435 , document, page, false, true);                                                
                        baseRow = communsPdf.setRow(table, 18);
                        communsPdf.setCell(baseRow, 100, "ASEGURADORA", Color.black, false, "C", 10, communsPdf.setLineStyle(Color.white,Color.white,Color.black,Color.white), "", communsPdf.setPadding2(4f, 0f, 2f, 0f), Color.white).setValign(VerticalAlignment.MIDDLE);                    
                        table.draw();

                        yStart -= table.getHeaderAndDataHeight()-5;
                        table = new BaseTable(yStart, yStartNewPage, 0, fullWidth-18, margin+10 , document, page, true, true);                                                
                        baseRow = communsPdf.setRow(table, 5);
                        communsPdf.setCell(baseRow, 100, "", Color.black, true, "C", 10, communsPdf.setLineStyle(Color.white,Color.white,Color.red,Color.white), "", communsPdf.setPadding2(4f, 0f, 2f, 0f), Color.white).setValign(VerticalAlignment.MIDDLE);                    
                        table.draw();

                        yStart -= table.getHeaderAndDataHeight()+5;
                         table = new BaseTable(yStart, yStartNewPage, 0, fullWidth, margin , document, page, false, true);                                                
                        baseRow = communsPdf.setRow(table,5);
                        communsPdf.setCell(baseRow, 100,"Tecoyotitla 412, Col. Ex-Hacienda de Guadalupe Chimalistac, Alcaldía Álvaro Obregón, C.P. 01050, Ciudad de México" , Color.black, false, "C", 7, communsPdf.setLineStyle(Color.white,Color.white,Color.red,Color.white), "", communsPdf.setPadding2(4f, 0f, 2f, 0f), Color.white).setValign(VerticalAlignment.MIDDLE);                    
                        baseRow = communsPdf.setRow(table, 5);
                        communsPdf.setCell(baseRow, 100, "Ciudad de México y Área Metropolitana 55 20001700 • Interior de la República 800 265 2020 • www.segurosargos.com", Color.black, true, "C", 7, communsPdf.setLineStyle(Color.white,Color.white,Color.red,Color.white), "", communsPdf.setPadding2(4f, 0f, 2f, 0f), Color.white).setValign(VerticalAlignment.MIDDLE);                    
                      
                        table.draw();



                        
                        

                        output = new ByteArrayOutputStream();
                        document.save(output);
                        // document.save(new File("/home/aalbanil/Vídeos/argos.pdf"));
                        return output.toByteArray();
                    } finally {
                        document.close();
                    }
                
                }            
        } catch (Exception ex) {
          throw new GeneralServiceException("00001",
                        "Ocurrio un error en el servicio ImpresionCertificadoAfirme: " + ex.getMessage());
        }
      
    }


        public String  urlMasFem(String sexo){
            String url="";
            switch(sexo){
                case "Femenino":
                url ="https://storage.googleapis.com/quattrocrm-prod/quattro-biibiic/2307/1N7rQflDvq65bN1u4E4VKEDZCqGCvkF8y0tq5vcoRVDgxY4xPcw5f7HAcl7VW6w/FEMAS.png";
                break;
                case "Masculino":
                url ="https://storage.googleapis.com/quattrocrm-prod/quattro-biibiic/2307/1N7rQflDvq65bN1u4E4VKFkEWmjWbzQzhNqSA00U4PaItUyz1XrYzMi1ifeCmP5/FEMAS2.png";
                break;
                default:
                url ="https://storage.googleapis.com/quattrocrm-prod/quattro-biibiic/2307/1N7rQflDvq65bN1u4E4VKHUMplDyPm1GYU86a2x89mrc2ckPmJy4sdQP3MhLp/FEM.png";
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
                addParagraph(contentStream, i, lines.get(1) + paddig, lines.get(2), tetxo, true, FONT, FONT_SIZE,LEADING, SPACING);
                contentStream.endText();
                contentStream.close();
                
             
                return contentStream;
            } catch (IOException e) {
                return null;
            }
        }

    
}
