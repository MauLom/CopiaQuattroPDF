package com.copsis.models.impresionCaratula;

import java.awt.Color;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;

import com.copsis.clients.projections.SocioDirecProjection;
import com.copsis.controllers.forms.ImpresionCaratulaForm;
import com.copsis.exceptions.GeneralServiceException;
import com.copsis.models.Tabla.BaseTable;
import com.copsis.models.Tabla.ImageUtils;
import com.copsis.models.Tabla.Row;
import com.copsis.models.Tabla.Sio4CommunsPdf;
import com.copsis.models.tabladi.tabla;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;

public class ImpresionCaratulaDiversos {

    tabla tabla = new tabla();
    public static BaseColor blanco = new BaseColor(255, 255, 255);
    private boolean drawLines = true;
    BaseColor gray = new BaseColor(148, 166, 187);
    BaseColor gray2 = new BaseColor(229, 234, 237);
    BaseColor black = new BaseColor(0, 0, 0);
    BaseColor blue = new BaseColor(40, 76, 113);
    float height = 0;
    

    public byte[] buildPDF(ImpresionCaratulaForm caratula) {
        try {
            ByteArrayOutputStream output = new ByteArrayOutputStream();

            Document document = new Document();

            PdfWriter writer = PdfWriter.getInstance(document, output);
            document.setMargins(15, 13, 10, 30);
            document.open();
            PdfPTable tablas;
            HeaderFooterPageEvent event = new HeaderFooterPageEvent();
            event.setDatos(caratula);
            event.onEndPage(writer, document);
            writer.setPageEvent(event);


            setencabezado(caratula, document, 0);
            
            tablas = tabla.setTable(2, 100, new float[]{60, 40}, "L");

            tabla.setTextCell("Movimiento", true, 10f, "L", gray2, "M", tablas, gray2, black, height, Font.BOLD);
            tabla.setTextCell("Contratante", true, 10f, "L", gray2, "M", tablas, gray2, black, height, Font.BOLD);
           
            tabla.setTextCell(caratula.getContrantante().getTipoPoliza() , true, 10f, "L", blanco, "M", tablas, blanco, blue, 15f, Font.BOLD);
            
            

            tabla.setTextCell(jdatos.getJSONObject("t1").has("c17") ? jdatos.getJSONObject("t1").getString("c17") : "", true, 10f, "L", blanco, "M", tablas, blanco, blue, 1f, Font.BOLD);
            document.add(tablas);

            tablas = tabla.setTable(3, 100, new float[]{20, 40, 40}, "L");
            tabla.setTextCell("Vigencia:", true, 10f, "L", blanco, "T", tablas, blanco, blue, 0f, Font.BOLD);
            tabla.setTextCell(jdatos.getJSONObject("t1").has("c7") ? jdatos.getJSONObject("t1").getString("c7") : "", true, 10f, "L", blanco, "M", tablas, blanco, blue, 0f, Font.NORMAL);
            tabla.setTextCell(jdatos.getJSONObject("t1").has("c13") ? jdatos.getJSONObject("t1").getString("c13") : "", true, 10f, "L", blanco, "M", tablas, blanco, blue, 0f, Font.NORMAL);

            tabla.setTextCell("Emisión:", true, 10f, "L", blanco, "T", tablas, blanco, blue, 0f, Font.BOLD);
            tabla.setTextCell(jdatos.getJSONObject("t1").has("c8") ? jdatos.getJSONObject("t1").getString("c8") : "", true, 10f, "L", blanco, "M", tablas, blanco, blue, 0f, Font.NORMAL);
            tabla.setTextCell(jdatos.getJSONObject("t1").has("c14") ? jdatos.getJSONObject("t1").getString("c14") : "", true, 10f, "L", blanco, "M", tablas, blanco, blue, 0f, Font.NORMAL);

            tabla.setTextCell("Subramo:", true, 10f, "L", blanco, "T", tablas, blanco, blue, 0f, Font.BOLD);
            tabla.setTextCell(jdatos.getJSONObject("t1").has("c9") ? jdatos.getJSONObject("t1").getString("c9") : "", true, 10f, "L", blanco, "M", tablas, blanco, blue, 0f, Font.NORMAL);
            tabla.setTextCell(jdatos.getJSONObject("t1").has("c15") ? jdatos.getJSONObject("t1").getString("c15") : "", true, 10f, "L", blanco, "M", tablas, blanco, blue, 0f, Font.NORMAL);
            document.add(tablas);

            tablas = tabla.setTable(4, 100, new float[]{20, 38, 9, 30}, "L");
            tabla.setTextCell("Forma de Pago:", true, 10f, "L", blanco, "T", tablas, blanco, blue, 0f, Font.BOLD);
            tabla.setTextCell(jdatos.getJSONObject("t1").has("c10") ? jdatos.getJSONObject("t1").getString("c10") : "", true, 10f, "L", blanco, "M", tablas, blanco, blue, 0f, Font.NORMAL);
            tabla.setTextCell("CURP:", true, 10f, "L", blanco, "M", tablas, blanco, blue, 0f, Font.BOLD);
            tabla.setTextCell((jdatos.getJSONObject("t1").has("curp") ? jdatos.getJSONObject("t1").getString("curp") : ""), true, 10f, "L", blanco, "M", tablas, blanco, blue, 0f, Font.NORMAL);

            tabla.setTextCell("Moneda:", true, 10f, "L", blanco, "T", tablas, blanco, blue, 0f, Font.BOLD);
            tabla.setTextCell(jdatos.getJSONObject("t1").has("c11") ? jdatos.getJSONObject("t1").getString("c11") : "", true, 10f, "L", blanco, "M", tablas, blanco, blue, 0f, Font.NORMAL);
            tabla.setTextCell("Email:", true, 10f, "L", blanco, "M", tablas, blanco, blue, 0f, Font.BOLD);
            tabla.setTextCell((jdatos.getJSONObject("t1").has("email") ? jdatos.getJSONObject("t1").getString("email") : ""), true, 10f, "L", blanco, "M", tablas, blanco, blue, 0f, Font.NORMAL).setLeft(-10);

            tabla.setTextCell("", true, 10f, "L", blanco, "T", tablas, blanco, blue, 0f, Font.BOLD);
            tabla.setTextCell("", true, 10f, "L", blanco, "M", tablas, blanco, blue, 0f, Font.NORMAL);
            tabla.setTextCell("Telefono:", true, 10f, "L", blanco, "M", tablas, blanco, blue, 0f, Font.BOLD);
            tabla.setTextCell((jdatos.getJSONObject("t1").has("telefono") ? jdatos.getJSONObject("t1").getString("telefono") : ""), true, 10f, "L", blanco, "M", tablas, blanco, blue, 0f, Font.NORMAL);

            document.add(tablas);

            tablas = tabla.setTable(3, 100, new float[]{20, 40, 40}, "L");
            tablas.setSpacingBefore(10);
            tabla.setTextCell("Agente:", true, 10f, "L", gray2, "M", tablas, gray2, black, 0f, Font.BOLD);
            tabla.setTextCell("", true, 10f, "L", gray2, "M", tablas, gray2, black, 0f, Font.BOLD);
            tabla.setTextCell("Grupo:", true, 10f, "L", gray2, "M", tablas, gray2, black, 0f, Font.BOLD);

            tabla.setTextCell("Aseguradora:", true, 10f, "L", blanco, "T", tablas, blanco, blue, 0f, Font.BOLD);
            tabla.setTextCell(jdatos.getJSONObject("t1").has("c16") ? jdatos.getJSONObject("t1").getString("c16") : "", true, 10f, "L", blanco, "M", tablas, blanco, blue, 0f, Font.NORMAL);
            tabla.setTextCell(jdatos.getJSONObject("g").has("c1") ? jdatos.getJSONObject("g").getString("c1") : "", true, 10f, "L", blanco, "M", tablas, blanco, blue, 0f, Font.NORMAL);

            tabla.setTextCell("Clave:", true, 10f, "L", blanco, "T", tablas, blanco, blue, 0f, Font.BOLD);
            tabla.setTextCell(jdatos.getJSONObject("t1").has("c19") ? jdatos.getJSONObject("t1").getString("c19") : "", true, 10f, "L", blanco, "M", tablas, blanco, blue, 0f, Font.NORMAL);
            tabla.setTextCell("", true, 10f, "L", blanco, "M", tablas, blanco, blue, 0f, Font.NORMAL);
            document.add(tablas);

            /*CARATCTERISICAS*/
            JSONArray jaDatosDireccion = jdatos.getJSONObject("t2").getJSONArray("c1");
            Boolean respuesta = (jaDatosDireccion.length() > 0);
            String dir1 = "";
            String dir2 = "";
            JSONObject joDatosDireccion = new JSONObject();
            if (jaDatosDireccion.length() > 0) {
                joDatosDireccion = jaDatosDireccion.getJSONObject(0);
                dir1 = joDatosDireccion.getString("c2") + " " + joDatosDireccion.getString("c3");
                dir1 += ((!"".equals(joDatosDireccion.getString("c4"))) ? " Int " + joDatosDireccion.getString("c4") : ", ") + joDatosDireccion.getString("c7") + ", CP: " + joDatosDireccion.getString("c6");
                dir2 = joDatosDireccion.getString("c8") + ", " + joDatosDireccion.getString("c9") + ", " + joDatosDireccion.getString("c10");

            }

            tablas = tabla.setTable(1, 100, new float[]{100}, "L");
            tablas.setSpacingBefore(10);
            tabla.setTextCell("Características: " + (joDatosDireccion.has("c1") ? joDatosDireccion.getString("c1") : " "), true, 10f, "L", gray2, "M", tablas, gray2, black, 0f, Font.BOLD);
            document.add(tablas);

            tablas = tabla.setTable(2, 100, new float[]{20, 80}, "L");
            tabla.setTextCell("Dirección:", true, 10f, "L", blanco, "T", tablas, blanco, blue, 30f, Font.BOLD);
            tabla.setTextCell(dir1 + "\n" + dir2, true, 10f, "L", blanco, "T", tablas, blanco, blue, 30f, Font.NORMAL);
            document.add(tablas);

            tablas = tabla.setTable(4, 100, new float[]{23, 37, 20, 20}, "L");
            tabla.setTextCell("Tipo Construcción:", true, 10f, "L", blanco, "L", tablas, blanco, blue, 0f, Font.BOLD);

            tabla.setTextCell((respuesta == true ? joDatosDireccion.getString("c13") : ""), true, 10f, "L", blanco, "T", tablas, blanco, blue, 0f, Font.NORMAL);
            tabla.setTextCell("Niveles:", true, 10f, "L", blanco, "M", tablas, blanco, blue, 0f, Font.BOLD);
            tabla.setTextCell((respuesta == true ? joDatosDireccion.getInt("c11") + "" : ""), true, 10f, "L", blanco, "T", tablas, blanco, blue, 0f, Font.NORMAL);

            tabla.setTextCell("Sotano:", true, 10f, "L", blanco, "L", tablas, blanco, blue, 0f, Font.BOLD);
            tabla.setTextCell((respuesta == true ? joDatosDireccion.getInt("c12") + "" : ""), true, 10f, "L", blanco, "T", tablas, blanco, blue, 0f, Font.NORMAL);
            tabla.setTextCell("Techo:", true, 10f, "L", blanco, "L", tablas, blanco, blue, 0f, Font.BOLD);
            tabla.setTextCell((respuesta == true ? joDatosDireccion.getString("c14") : ""), true, 10f, "L", blanco, "T", tablas, blanco, blue, 0f, Font.NORMAL);

            tabla.setTextCell("ZonaHidrometeorológica:", true, 10f, "L", blanco, "T", tablas, blanco, blue, 0f, Font.BOLD);
            tabla.setTextCell((respuesta == true ? joDatosDireccion.getString("c15") : ""), true, 10f, "L", blanco, "T", tablas, blanco, blue, 0f, Font.NORMAL);
            tabla.setTextCell("Zona Sísmica:", true, 10f, "L", blanco, "L", tablas, blanco, blue, 0f, Font.BOLD);
            tabla.setTextCell((respuesta == true ? joDatosDireccion.getString("c16") : ""), true, 10f, "L", blanco, "T", tablas, blanco, blue, 0f, Font.NORMAL);
            document.add(tablas);

            tablas = tabla.setTable(3, 100, new float[]{40, 30, 30}, "L");
            tablas.setSpacingBefore(5);

            tabla.setTextCell("Paquete", true, 10f, "L", gray2, "M", tablas, gray2, blue, 0f, Font.BOLD);
            tabla.setTextCell("", true, 10f, "L", gray2, "M", tablas, gray2, blue, 0f, Font.BOLD);
            tabla.setTextCell("", true, 10f, "L", gray2, "M", tablas, gray2, blue, 0f, Font.BOLD);

            tabla.setTextCell("Cobertura", true, 10f, "L", blanco, "M", tablas, gray2, blue, 0f, Font.BOLD);
            tabla.setTextCell("Suma Asegurada", true, 10f, "C", blanco, "M", tablas, gray2, blue, 0f, Font.BOLD);
            tabla.setTextCell("Deducible", true, 10f, "C", blanco, "M", tablas, gray2, blue, 0f, Font.BOLD);
            tablas.setHeaderRows(2);

            JSONArray jArray = jdatos.getJSONObject("c").getJSONArray("c2");
            for (int i = 0; i < jArray.length(); i++) {
                if (i % 2 == 0) {
                    drawLines = true;
                } else {
                    drawLines = false;
                }
                tabla.setTextCell((jArray.getJSONObject(i).has("c1") ? jArray.getJSONObject(i).getString("c1") : ""), true, 10f, "L", drawLines ? gray2 : blanco, "M", tablas, gray2, blue, 17f, Font.NORMAL);
                tabla.setTextCell((jArray.getJSONObject(i).has("c2") ? jArray.getJSONObject(i).getString("c2") : ""), true, 10f, "R", drawLines ? gray2 : blanco, "M", tablas, gray2, blue, 17f, Font.NORMAL);
                tabla.setTextCell((jArray.getJSONObject(i).has("c3") ? jArray.getJSONObject(i).getString("c3") : ""), true, 10f, "L", drawLines ? gray2 : blanco, "M", tablas, gray2, blue, 17f, Font.NORMAL);

            }
            document.add(tablas);
            if (jdatos.getJSONObject("t1").has("c26") && jdatos.getJSONObject("t1").getString("c26").length() > 0) {
                tablas = tabla.setTable(1, 100, new float[]{100}, "L");
                tablas.setSpacingBefore(10);
                tabla.setTextCell("Observaciones", true, 10f, "L", gray2, "L", tablas, blanco, blue, 0f, Font.BOLD);
                tabla.setTextCell(jdatos.getJSONObject("t1").has("c26") ? jdatos.getJSONObject("t1").getString("c26") : "HOLA", true, 10f, "L", blanco, "M", tablas, blanco, black, 1f, Font.NORMAL);
                document.add(tablas);
            }

            tablas = tabla.setTable(1, 100, new float[]{100}, "L");
            tablas.setSpacingBefore(5);
            tabla.setTextCell("Detalle del Seguro:", true, 10f, "L", gray2, "L", tablas, blanco, blue, 0f, Font.BOLD);
            document.add(tablas);
            StringBuilder htmlString = new StringBuilder();
            htmlString.append("<html><body>  ");
            htmlString.append(jdatos.getJSONObject("t2").has("c0") ? jdatos.getJSONObject("t2").getString("c0").replaceAll("<colgroup/>", "</colgroup>").replace("<br>", "<br/>") : "");
            htmlString.append("</body></html>");
            InputStream is = new ByteArrayInputStream(htmlString.toString().getBytes());
            XMLWorkerHelper.getInstance().parseXHtml(writer, document, is, Charset.forName("UTF-8"));

            document.close();

            return output.toByteArray();

        } catch (Exception ex) {
            throw new GeneralServiceException("00001",
                    "Ocurrio un error en el servicio ImpresionCaratulaDiversos: " + ex.getMessage());
        }

    }

    public void setencabezado( ImpresionCaratulaForm caratula , Document document, int page) throws IOException, DocumentException {
        PdfPTable tablas;
        tablas = tabla.setTable(2, 40, new float[]{20, 10}, "L");
        tablas.setSpacingAfter(10);
        tabla.setTextCell("Póliza", true, 10f, "C", gray2, "M", tablas, gray2, black, 0f, Font.BOLD);
        tabla.setTextCell("OT", true, 10f, "C", gray2, "M", tablas, gray2, black, 0f, Font.BOLD);

        tabla.setTextCell(caratula.getContrantante().getNoPoliza(), true, 10f, "C", blanco, "M", tablas, gray2, blue, 0f, Font.NORMAL);
        tabla.setTextCell(caratula.getContrantante().getPolizaID(), true, 10f, "C", blanco, "M", tablas, gray2, blue, 0f, Font.NORMAL);

        document.add(tablas);

         SocioDirecProjection socio = caratula.getSocio();
        String direccion =  socio != null ? socio.getCalle() : ""+"\n"+ socio != null ? socio.getColonia() : ""
        +"\n"+ socio != null ? socio.getEstado() : "";
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.setWidths(new int[]{2, 1});
        table.addCell(createTextCell(direccion));
        table.addCell(createImageCell(socio.getAvatar()));

        document.add(table);
    }

    public static PdfPCell createImageCell(String path) throws DocumentException, IOException {
        Image img = Image.getInstance(path);
        PdfPCell cell = new PdfPCell(img, true);
        cell.setBorder(Rectangle.NO_BORDER);
        return cell;
    }

    public static PdfPCell createTextCell(String text) throws DocumentException, IOException {
        PdfPCell cell = new PdfPCell();
        Paragraph p = new Paragraph(text);
        p.setAlignment(Element.ALIGN_LEFT);
        p.getFont().setColor(new BaseColor(40, 76, 113));
        p.getFont().setSize(10);
        cell.addElement(p);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);

        cell.setBorder(Rectangle.NO_BORDER);

        return cell;
    }

    
}

class HeaderFooterPageEvent extends PdfPageEventHelper {

   ImpresionCaratulaForm caratula = new ImpresionCaratulaForm();

    public void setDatos(ImpresionCaratulaForm caratula) {
        caratula = caratula;

    }

    @Override
    public void onStartPage(PdfWriter writer, Document document) {

        try {
            setencabezado(caratula, document, 0);

        } catch (Exception ex) {
            throw new GeneralServiceException("00001",
                    "Ocurrio un error en el servicio setencabezado: " + ex.getMessage());
        }
    }

    @Override
    public void onEndPage(PdfWriter writer, Document document) {
        try {
            BaseColor black = new BaseColor(0, 0, 0);
            BaseColor blue = new BaseColor(40, 76, 113);
            BaseColor blanco = new BaseColor(255, 255, 255);

            tabla tabla = new tabla();
            SocioDirecProjection socio = caratula.getSocio();
            PdfPTable tablas;
            tablas = tabla.setTable(4, 100, new float[]{300, 65, 70, 130}, "L", 567);
            tabla.setTextCell("Powered by", true, 10f, "R", blanco, "M", tablas, blanco, black, 0f, Font.NORMAL);
            tabla.setTextCell("quattroCRM", true, 10f, "L", blanco, "M", tablas, blanco, black, 0f, Font.BOLD);

            tabla.setTextCell("FIRMA:", true, 10f, "R", blanco, "M", tablas, blanco, blue, 0f, Font.BOLD);
            tabla.setTextCell(socio != null ? socio.getNombSocio():"" , true, 10f, "R", blanco, "M", tablas, blanco, blue, 0f, Font.NORMAL);

            tablas.writeSelectedRows(0, -1, 15, 30, writer.getDirectContent());

        } catch (Exception ex) {
          throw new GeneralServiceException("00001",
                    "Ocurrio un error en el servicio setencabezado: " + ex.getMessage());
        }
    }

    public void setencabezado(ImpresionCaratulaForm caractula, Document document, int page) throws IOException, DocumentException {
        BaseColor gray2 = new BaseColor(229, 234, 237);
        BaseColor black = new BaseColor(0, 0, 0);
        BaseColor blue = new BaseColor(40, 76, 113);
        BaseColor blanco = new BaseColor(255, 255, 255);

        tabla tabla = new tabla();
        PdfPTable tablas;
        tablas = tabla.setTable(2, 40, new float[]{20, 10}, "L");
        tabla.setTextCell("Póliza", true, 10f, "C", gray2, "M", tablas, gray2, black, 0f, Font.BOLD);
        tabla.setTextCell("OT", true, 10f, "C", gray2, "M", tablas, gray2, black, 0f, Font.BOLD);

        tabla.setTextCell(caractula.getContrantante().getNoPoliza(), true, 10f, "C", blanco, "M", tablas, gray2, blue, 0f, Font.NORMAL);
        tabla.setTextCell(caractula.getContrantante().getPolizaID(), true, 10f, "C", blanco, "M", tablas, gray2, blue, 0f, Font.NORMAL);

        document.add(tablas);

        SocioDirecProjection socio = caratula.getSocio();
        String direccion =  socio != null ? socio.getCalle() : ""+"\n"+ socio != null ? socio.getColonia() : ""
        +"\n"+ socio != null ? socio.getEstado() : "";
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.setWidths(new int[]{2, 1});
        table.addCell(createTextCell(direccion));
        table.addCell(createImageCell(socio.getAvatar()));

        document.add(table);
    }

    public static PdfPCell createImageCell(String path) throws DocumentException, IOException {
        Image img = Image.getInstance(path);
        PdfPCell cell = new PdfPCell(img, true);
        cell.setBorder(Rectangle.NO_BORDER);
        return cell;
    }

    public static PdfPCell createTextCell(String text) throws DocumentException, IOException {
        PdfPCell cell = new PdfPCell();
        Paragraph p = new Paragraph(text);
        p.setAlignment(Element.ALIGN_LEFT);
        p.getFont().setColor(new BaseColor(40, 76, 113));
        p.getFont().setSize(10);
        cell.addElement(p);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);

        cell.setBorder(Rectangle.NO_BORDER);

        return cell;
    }

}

