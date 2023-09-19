package com.copsis.models.impresionCaratula;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.copsis.clients.projections.CoberturaProjection;
import com.copsis.clients.projections.SocioDirecProjection;
import com.copsis.clients.projections.UbicacionesProjection;
import com.copsis.controllers.forms.ImpresionCaratulaForm;
import com.copsis.exceptions.GeneralServiceException;
import com.copsis.models.tabladi.tabla;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import java.nio.charset.Charset;
import java.util.List;
import java.io.InputStream;

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
            
            

            tabla.setTextCell( caratula.getContrantante().getContrantante(), true, 10f, "L", blanco, "M", tablas, blanco, blue, 1f, Font.BOLD);
            document.add(tablas);

            tablas = tabla.setTable(3, 100, new float[]{20, 40, 40}, "L");
            tabla.setTextCell("Vigencia:", true, 10f, "L", blanco, "T", tablas, blanco, blue, 0f, Font.BOLD);
            tabla.setTextCell(caratula.getContrantante().getVigencia(), true, 10f, "L", blanco, "M", tablas, blanco, blue, 0f, Font.NORMAL);
            tabla.setTextCell(caratula.getContrantante().getRfc(), true, 10f, "L", blanco, "M", tablas, blanco, blue, 0f, Font.NORMAL);

            tabla.setTextCell("Emisión:", true, 10f, "L", blanco, "T", tablas, blanco, blue, 0f, Font.BOLD);
            tabla.setTextCell(caratula.getContrantante().getFechaEmision(), true, 10f, "L", blanco, "M", tablas, blanco, blue, 0f, Font.NORMAL);
            tabla.setTextCell(caratula.getContrantante().getCteCalle(), true, 10f, "L", blanco, "M", tablas, blanco, blue, 0f, Font.NORMAL);

            tabla.setTextCell("Subramo:", true, 10f, "L", blanco, "T", tablas, blanco, blue, 0f, Font.BOLD);
            tabla.setTextCell(caratula.getContrantante().getSubRamo(), true, 10f, "L", blanco, "M", tablas, blanco, blue, 0f, Font.NORMAL);
            tabla.setTextCell(caratula.getContrantante().getCteColonia(), true, 10f, "L", blanco, "M", tablas, blanco, blue, 0f, Font.NORMAL);
            document.add(tablas);

            tablas = tabla.setTable(4, 100, new float[]{20, 38, 9, 30}, "L");
            tabla.setTextCell("Forma de Pago:", true, 10f, "L", blanco, "T", tablas, blanco, blue, 0f, Font.BOLD);
            tabla.setTextCell(caratula.getContrantante().getFormaPago(), true, 10f, "L", blanco, "M", tablas, blanco, blue, 0f, Font.NORMAL);
            tabla.setTextCell("CURP:", true, 10f, "L", blanco, "M", tablas, blanco, blue, 0f, Font.BOLD);
            tabla.setTextCell(caratula.getClientExtra() != null ? caratula.getClientExtra().getCurp() : "", true, 10f, "L", blanco, "M", tablas, blanco, blue, 0f, Font.NORMAL);

            tabla.setTextCell("Moneda:", true, 10f, "L", blanco, "T", tablas, blanco, blue, 0f, Font.BOLD);
            tabla.setTextCell(caratula.getContrantante().getMoneda(), true, 10f, "L", blanco, "M", tablas, blanco, blue, 0f, Font.NORMAL);
            tabla.setTextCell("Email:", true, 10f, "L", blanco, "M", tablas, blanco, blue, 0f, Font.BOLD);
            tabla.setTextCell(caratula.getClientExtra().getEmail(), true, 10f, "L", blanco, "M", tablas, blanco, blue, 0f, Font.NORMAL).setLeft(-10);

            tabla.setTextCell("", true, 10f, "L", blanco, "T", tablas, blanco, blue, 0f, Font.BOLD);
            tabla.setTextCell("", true, 10f, "L", blanco, "M", tablas, blanco, blue, 0f, Font.NORMAL);
            tabla.setTextCell("Telefono:", true, 10f, "L", blanco, "M", tablas, blanco, blue, 0f, Font.BOLD);
            tabla.setTextCell(caratula.getClientExtra().getTelefono(), true, 10f, "L", blanco, "M", tablas, blanco, blue, 0f, Font.NORMAL);

            document.add(tablas);

            tablas = tabla.setTable(3, 100, new float[]{20, 40, 40}, "L");
            tablas.setSpacingBefore(10);
            tabla.setTextCell("Agente:", true, 10f, "L", gray2, "M", tablas, gray2, black, 0f, Font.BOLD);
            tabla.setTextCell("", true, 10f, "L", gray2, "M", tablas, gray2, black, 0f, Font.BOLD);
            tabla.setTextCell("Grupo:", true, 10f, "L", gray2, "M", tablas, gray2, black, 0f, Font.BOLD);

            tabla.setTextCell("Aseguradora:", true, 10f, "L", blanco, "T", tablas, blanco, blue, 0f, Font.BOLD);
            tabla.setTextCell(caratula.getContrantante().getAseguradora(), true, 10f, "L", blanco, "M", tablas, blanco, blue, 0f, Font.NORMAL);
            tabla.setTextCell(caratula.getContrantante().getGrupo(), true, 10f, "L", blanco, "M", tablas, blanco, blue, 0f, Font.NORMAL);

            tabla.setTextCell("Clave:", true, 10f, "L", blanco, "T", tablas, blanco, blue, 0f, Font.BOLD);
            tabla.setTextCell(caratula.getContrantante().getClaveAngente(), true, 10f, "L", blanco, "M", tablas, blanco, blue, 0f, Font.NORMAL);
            tabla.setTextCell("", true, 10f, "L", blanco, "M", tablas, blanco, blue, 0f, Font.NORMAL);
            document.add(tablas);

            /*CARATCTERISICAS*/

            List<UbicacionesProjection>  ubicaciones = caratula.getUbicaciones();
            

            tablas = tabla.setTable(1, 100, new float[]{100}, "L");
            tablas.setSpacingBefore(10);
            tabla.setTextCell("Características: " +  (ubicaciones == null ||  ubicaciones.isEmpty() ? "": ubicaciones.get(0).getNoUbicacion()), true, 10f, "L", gray2, "M", tablas, gray2, black, 0f, Font.BOLD);
            document.add(tablas);

            tablas = tabla.setTable(2, 100, new float[]{20, 80}, "L");
            tabla.setTextCell("Dirección:", true, 10f, "L", blanco, "T", tablas, blanco, blue, 30f, Font.BOLD);
            tabla.setTextCell((ubicaciones == null ||  ubicaciones.isEmpty() ? "": caratula.getUbicaciones().get(0).getDireccion()), true, 10f, "L", blanco, "T", tablas, blanco, blue, 30f, Font.NORMAL);
            document.add(tablas);

            tablas = tabla.setTable(4, 100, new float[]{23, 37, 20, 20}, "L");
            tabla.setTextCell("Tipo Construcción:", true, 10f, "L", blanco, "L", tablas, blanco, blue, 0f, Font.BOLD);

            tabla.setTextCell(ubicaciones == null || ubicaciones.isEmpty() ? "": ubicaciones.get(0).getTipoConstrucion(), true, 10f, "L", blanco, "T", tablas, blanco, blue, 0f, Font.NORMAL);
            tabla.setTextCell("Niveles:", true, 10f, "L", blanco, "M", tablas, blanco, blue, 0f, Font.BOLD);
            tabla.setTextCell(ubicaciones == null || ubicaciones.isEmpty() ? "": ubicaciones.get(0).getNiveles(), true, 10f, "L", blanco, "T", tablas, blanco, blue, 0f, Font.NORMAL);

            tabla.setTextCell("Sotano:", true, 10f, "L", blanco, "L", tablas, blanco, blue, 0f, Font.BOLD);
            tabla.setTextCell(ubicaciones == null || ubicaciones.isEmpty() ? "": ubicaciones.get(0).getSotano()+"", true, 10f, "L", blanco, "T", tablas, blanco, blue, 0f, Font.NORMAL);
            tabla.setTextCell("Techo:", true, 10f, "L", blanco, "L", tablas, blanco, blue, 0f, Font.BOLD);
            tabla.setTextCell(ubicaciones == null || ubicaciones.isEmpty() ? "": ubicaciones.get(0).getTechos()+"", true, 10f, "L", blanco, "T", tablas, blanco, blue, 0f, Font.NORMAL);

            tabla.setTextCell("ZonaHidrometeorológica:", true, 10f, "L", blanco, "T", tablas, blanco, blue, 0f, Font.BOLD);
            tabla.setTextCell(ubicaciones == null || ubicaciones.isEmpty() ? "": ubicaciones.get(0).getSismo()+"", true, 10f, "L", blanco, "T", tablas, blanco, blue, 0f, Font.NORMAL);
            tabla.setTextCell("Zona Sísmica:", true, 10f, "L", blanco, "L", tablas, blanco, blue, 0f, Font.BOLD);
            tabla.setTextCell(ubicaciones == null || ubicaciones.isEmpty() ? "": ubicaciones.get(0).getSismo()+"" , true, 10f, "L", blanco, "T", tablas, blanco, blue, 0f, Font.NORMAL);
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

            List<CoberturaProjection> coberturas= caratula.getCoberturas();
            if(!coberturas.isEmpty()){
            for (int i = 0; i < coberturas.size(); i++) {
                if (i % 2 == 0) {
                    drawLines = true;
                } else {
                    drawLines = false;
                }
                tabla.setTextCell( coberturas.get(i).getNombres(), true, 10f, "L", drawLines ? gray2 : blanco, "M", tablas, gray2, blue, 17f, Font.NORMAL);
                tabla.setTextCell( coberturas.get(i).getSa(), true, 10f, "R", drawLines ? gray2 : blanco, "M", tablas, gray2, blue, 17f, Font.NORMAL);
                tabla.setTextCell(coberturas.get(i).getDeducible(), true, 10f, "L", drawLines ? gray2 : blanco, "M", tablas, gray2, blue, 17f, Font.NORMAL);

            }}
            document.add(tablas);
            if (caratula.getContrantante() != null && caratula.getContrantante().getDescripcion().length() > 0) {
                tablas = tabla.setTable(1, 100, new float[]{100}, "L");
                tablas.setSpacingBefore(10);
                tabla.setTextCell("Observaciones", true, 10f, "L", gray2, "L", tablas, blanco, blue, 0f, Font.BOLD);
                tabla.setTextCell(caratula.getContrantante().getDescripcion(), true, 10f, "L", blanco, "M", tablas, blanco, black, 1f, Font.NORMAL);
                document.add(tablas);
            }

            tablas = tabla.setTable(1, 100, new float[]{100}, "L");
            tablas.setSpacingBefore(5);
            tabla.setTextCell("Detalle del Seguro:", true, 10f, "L", gray2, "L", tablas, blanco, blue, 0f, Font.BOLD);
            document.add(tablas);
            StringBuilder htmlString = new StringBuilder();
            htmlString.append("<html><body>  ");
            htmlString.append(caratula.getContrantante().getDetalleSeguro() == null || caratula.getContrantante().getDetalleSeguro().isEmpty() ? "": caratula.getContrantante().getDetalleSeguro().replaceAll("<colgroup/>", "</colgroup>").replace("<br>", "<br/>"));
            htmlString.append("</body></html>");
            InputStream is = new ByteArrayInputStream(htmlString.toString().getBytes());
            XMLWorkerHelper.getInstance().parseXHtml(writer, document, is, Charset.forName("UTF-8"));

            
            document.close();
             //FileOutputStream fos =  new FileOutputStream(new File("/home/aalbanil/Vídeos/IMPRESIONCARACTULA/diversos.pdf"));
              //output.writeTo(fos);
            return output.toByteArray();
               
            
           

        } catch (Exception ex) {
            ex.printStackTrace();
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
        String direccion =  (socio != null ? socio.getCalle() : "")+"\n"+ (socio != null ? socio.getColonia() : "")
        +"\n"+ (socio != null ? socio.getEstado() : "");
       ;
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

