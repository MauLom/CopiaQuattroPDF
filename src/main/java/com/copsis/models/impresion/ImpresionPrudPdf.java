package com.copsis.models.impresion ;

import java.awt.Color ;
import java.io.ByteArrayOutputStream ;
import java.io.File;
import java.io.IOException ;

import org.apache.pdfbox.pdmodel.PDDocument ;
import org.apache.pdfbox.pdmodel.PDPage ;

import com.copsis.clients.projections.CaractulaPrudentialProjection;
import com.copsis.exceptions.GeneralServiceException ;
import com.copsis.models.Tabla.BaseTable ;
import com.copsis.models.Tabla.ImageUtils ;
import com.copsis.models.Tabla.Row ;
import com.copsis.models.Tabla.Sio4CommunsPdf ;

public class ImpresionPrudPdf {


    private float yStartNewPage = 890, yStart = 792, bottomMargin = 32, fullWidth = 620;
    private Sio4CommunsPdf communsPdf = new Sio4CommunsPdf();


    public byte[] buildPDF(CaractulaPrudentialProjection datos) {
        try {
            ByteArrayOutputStream output;
            try ( PDDocument document = new PDDocument()) {
                try {
                    PDPage page = new PDPage();
                    document.addPage(page);
                 
                    
                    if(datos.getPaquete() ==1){
                        getAccidentesPersonales(document, page,datos);
                    }
                   

                    if(datos.getPaquete() ==2){
                    getHospitalizacion(document, page,datos);
                     }

                     if(datos.getPaquete() ==3){
                        getGastosMedicos(document, page,datos);
                     }

                     if(datos.getPaquete() ==4){
                     getSeguroHospitalizacion(document, page,datos);
                    }

                    output = new ByteArrayOutputStream();
                    document.save(output);
                    document.save(new File("/home/aalbanil/Vídeos/prud" + datos.getPaquete() + ".pdf"));
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

    private void getSeguroHospitalizacion(PDDocument document, PDPage page,CaractulaPrudentialProjection datos) throws IOException {
        BaseTable table;
        Row<PDPage> baseRow;
        table = new BaseTable(yStart, yStartNewPage, 0, fullWidth, 0, document, page, false, true);
         baseRow = communsPdf.setRow(table, 15);
         communsPdf.setCell(baseRow, 100,
                 ImageUtils.readImage("https://storage.googleapis.com/quattrocrm-prod/quattro-biibiic/2401/1N7rQflDvq65bN1u4E4VKPxA3pMtBaoanSGU6i6KfY9hgInFpTMZZy7OjLhpWsT/01.png"),
                 0, 0, Color.black);
         table.remoBordes(true, bottomMargin);
         table.draw();
             
         getEncabezadoTexto(document, page, 510f,133f, datos);
         String[] imagenes = {
        "https://storage.googleapis.com/quattrocrm-prod/quattro-biibiic/2401/1N7rQflDvq65bN1u4E4VKA4eUSpuyEvmTj7jpeR5y2mlPvg8f9ZCxJelFzfXAwx/02.png",
        "https://storage.googleapis.com/quattrocrm-prod/quattro-biibiic/2401/1N7rQflDvq65bN1u4E4VKH60mAXoYt9rgqmsyiQUEvCNroeczBxGOLxYAtZ3sUfo/03.png",
        "https://storage.googleapis.com/quattrocrm-prod/quattro-biibiic/2401/1N7rQflDvq65bN1u4E4VKEMN0MhVHS9ruxmFxfDMiPqMMlzC3lodetZJQxcxCB57/04.png",
        "https://storage.googleapis.com/quattrocrm-prod/quattro-biibiic/2401/1N7rQflDvq65bN1u4E4VKMXeoIekoA0qUokTY0qC2XOgybH2wWtTZB8GTKAjt1/05.png",
        "https://storage.googleapis.com/quattrocrm-prod/quattro-biibiic/2401/1N7rQflDvq65bN1u4E4VKGiH6ijKgA7GrukZtpSIYdr8HNBdwNfZlbcqG2havy4r/06.png",
        "https://storage.googleapis.com/quattrocrm-prod/quattro-biibiic/2401/1N7rQflDvq65bN1u4E4VKC3XlFKsqXBpXrMx1Lt3wAYmBxh2p04DUth0RnFmjoEm/07.png",
        "https://storage.googleapis.com/quattrocrm-prod/quattro-biibiic/2401/1N7rQflDvq65bN1u4E4VKCRnYn98GCfyAhcnnhqbFNVnZC9MfKxQk8CPuQv8C/08.png",
        "https://storage.googleapis.com/quattrocrm-prod/quattro-biibiic/2401/1N7rQflDvq65bN1u4E4VKAr1N3sXp5Y5kp6aHpZyKAmBxh2p04DUth0RnFmjoEm/09.png"                 
        };

        for (String url : imagenes) {   
        
            page = new PDPage();
            document.addPage(page);
            table = new BaseTable(yStart, yStartNewPage, 0, fullWidth, 0, document, page, false, true);
            baseRow = communsPdf.setRow(table, 15);
            communsPdf.setCell(baseRow, 100,
                    ImageUtils.readImage(url),
                    0, 0, Color.black);
            table.remoBordes(true, bottomMargin);
            table.draw();
            getEncabezadoTexto(document, page, 742f,468f,datos);
            }
    }

    private void getGastosMedicos(PDDocument document, PDPage page,CaractulaPrudentialProjection datos) throws IOException {
        BaseTable table;
        Row<PDPage> baseRow;
        table = new BaseTable(yStart, yStartNewPage, 0, fullWidth, 0, document, page, false, true);
        baseRow = communsPdf.setRow(table, 15);
        communsPdf.setCell(baseRow, 100,
                ImageUtils.readImage("https://storage.googleapis.com/quattrocrm-prod/quattro-biibiic/2401/1N7rQflDvq65bN1u4E4VKEOKUPtmHVhtSG0joqAk5WmmLoeWXc0ccnlcAGHX1U/01.png"),
                0, 0, Color.black);
        table.remoBordes(true, bottomMargin);
        table.draw();
            
        getEncabezadoTexto(document, page, 691f,416f,datos);

        String[] imagenes = {
            "https://storage.googleapis.com/quattrocrm-prod/quattro-biibiic/2401/1N7rQflDvq65bN1u4E4VKGwKt8hUKUMAHQN0Bf0CjmvQirOLH25pL1zKJAXj3T/02.png",
            "https://storage.googleapis.com/quattrocrm-prod/quattro-biibiic/2401/1N7rQflDvq65bN1u4E4VKO03Walqw0329RC24QvoGtwf9BN9np8vrrC6GjXN2fNK/03.png",
            "https://storage.googleapis.com/quattrocrm-prod/quattro-biibiic/2401/1N7rQflDvq65bN1u4E4VKHIFtGbOPazQwiZBS7qrQVyKj77QG05HNcr5T4IhmwEr/04.png"
        };
            
        int i=0;
        for (String url : imagenes) {   
          
            page = new PDPage();
            document.addPage(page);
            table = new BaseTable(yStart, yStartNewPage, 0, fullWidth, 0, document, page, false, true);
            baseRow = communsPdf.setRow(table, 15);
            communsPdf.setCell(baseRow, 100,
                    ImageUtils.readImage(url),
                    0, 0, Color.black);
            table.remoBordes(true, bottomMargin);
            table.draw();
            getEncabezadoTexto(document, page, 691f,i ==0 ? 416f :412f,datos);
            i++;
            }
    }

    private void getHospitalizacion(PDDocument document, PDPage page,CaractulaPrudentialProjection datos) throws IOException {
        BaseTable table;
        Row<PDPage> baseRow;
        table = new BaseTable(yStart, yStartNewPage, 0, fullWidth, 0, document, page, false, true);
        baseRow = communsPdf.setRow(table, 15);
        communsPdf.setCell(baseRow, 100,
                ImageUtils.readImage("https://storage.googleapis.com/quattrocrm-prod/quattro-biibiic/2401/1N7rQflDvq65bN1u4E4VKJTetcstexw1pEUbr5UCy5RT4EFN0czC3L7ck53tsxyS/01.png"),
                0, 0, Color.black);
        table.remoBordes(true, bottomMargin);
        table.draw();
         
        getEncabezadoTexto(document, page, 510f,135f,datos);

        String[] imagenes = {
            
             "https://storage.googleapis.com/quattrocrm-prod/quattro-biibiic/2401/1N7rQflDvq65bN1u4E4VKAWeKLbO7tZecMSpvUZzZsezoCUijshOblqoddYkMF/02.png",
             "https://storage.googleapis.com/quattrocrm-prod/quattro-biibiic/2401/1N7rQflDvq65bN1u4E4VKLW32In9zM8MR3SHa99JB1cNGeICVwQXmA4k6YCaS0/03.png",
             "https://storage.googleapis.com/quattrocrm-prod/quattro-biibiic/2401/1N7rQflDvq65bN1u4E4VKJq2pq39l4f4gsX3k401raItUyz1XrYzMi1ifeCmP5/04.png",
             "https://storage.googleapis.com/quattrocrm-prod/quattro-biibiic/2401/1N7rQflDvq65bN1u4E4VKGEJPS3ldoPKZgAsaSRt0SpVnZC9MfKxQk8CPuQv8C/05.png",
             "https://storage.googleapis.com/quattrocrm-prod/quattro-biibiic/2401/1N7rQflDvq65bN1u4E4VKPkBDyDEHaaewBTqyr9b21dzj1prD49eGE73D87jH5/06.png",
             "https://storage.googleapis.com/quattrocrm-prod/quattro-biibiic/2401/1N7rQflDvq65bN1u4E4VKHR4AqwU8gRyb7WkTIMbwVAf9BN9np8vrrC6GjXN2fNK/07.png",
             "https://storage.googleapis.com/quattrocrm-prod/quattro-biibiic/2401/1N7rQflDvq65bN1u4E4VKDts5uHuJs9O9HnOq13lb6c7vz1H5gV6PXBNAlEmXhz/08.png",
             "https://storage.googleapis.com/quattrocrm-prod/quattro-biibiic/2401/1N7rQflDvq65bN1u4E4VKMphoBMld2m4OUZKIwyErfMPGA9bhiXJiuifooWS99L7/09.png",
             "https://storage.googleapis.com/quattrocrm-prod/quattro-biibiic/2401/1N7rQflDvq65bN1u4E4VKMEnFhGNG6MZbR7w1WUdHaItUyz1XrYzMi1ifeCmP5/10.png"
        };
         
        // Acceder a las URLs de las imágenes en el arreglo
        
          for (String url : imagenes) {   
          
        page = new PDPage();
        document.addPage(page);
        table = new BaseTable(yStart, yStartNewPage, 0, fullWidth, 0, document, page, false, true);
        baseRow = communsPdf.setRow(table, 15);
        communsPdf.setCell(baseRow, 100,
                ImageUtils.readImage(url),
                0, 0, Color.black);
        table.remoBordes(true, bottomMargin);
        table.draw();
        getEncabezadoTexto(document, page, 742f,466f,datos);
        }
    }

    private void getAccidentesPersonales(PDDocument document, PDPage page,CaractulaPrudentialProjection datos) throws IOException {
        
        BaseTable table;
        Row<PDPage> baseRow;
        table = new BaseTable(yStart, yStartNewPage, 0, fullWidth, 0, document, page, false, true);
        baseRow = communsPdf.setRow(table, 15);
        communsPdf.setCell(baseRow, 100,
                ImageUtils.readImage("https://storage.googleapis.com/quattrocrm-prod/quattro-biibiic/2401/1N7rQflDvq65bN1u4E4VKIJMM2X1opIC1j178nHSJNbc5bDwxp7b5kpNlunxjRl0/01.png"),
                0, 0, Color.black);
        table.remoBordes(true, bottomMargin);
        table.draw();

        getEncabezadoTexto(document, page, 681f,412f,datos);

        page = new PDPage();
        document.addPage(page);
        table = new BaseTable(yStart, yStartNewPage, 0, fullWidth, 0, document, page, false, true);
        baseRow = communsPdf.setRow(table, 15);
        communsPdf.setCell(baseRow, 100,
                ImageUtils.readImage("https://storage.googleapis.com/quattrocrm-prod/quattro-biibiic/2401/1N7rQflDvq65bN1u4E4VKMXzDPSsvzORZ6aa5evAFfBet2aMC1bKeugA2jtgz/02.png"),
                0, 0, Color.black);
        table.remoBordes(true, bottomMargin);
        table.draw();
        getEncabezadoTexto(document, page, 691f,412f,datos);

        page = new PDPage();
        document.addPage(page);
        table = new BaseTable(yStart, yStartNewPage, 0, fullWidth, 0, document, page, false, true);
        baseRow = communsPdf.setRow(table, 15);
        communsPdf.setCell(baseRow, 100,
                ImageUtils.readImage("https://storage.googleapis.com/quattrocrm-prod/quattro-biibiic/2401/1N7rQflDvq65bN1u4E4VKBiuI1GFqblahm43iftmZwMf9BN9np8vrrC6GjXN2fNK/03.png"),
                0, 0, Color.black);
        table.remoBordes(true, bottomMargin);
        table.draw();
        getEncabezadoTexto(document, page, 691f,412f,datos);
    }

    private void getEncabezadoTexto(PDDocument document, PDPage page, Float yStart,Float marginx,CaractulaPrudentialProjection datos) throws IOException {
        BaseTable table2;
        Row<PDPage> baseRow2;
        table2 = new BaseTable(yStart, 780, bottomMargin, 116, marginx, document, page, false, true);
        baseRow2 = communsPdf.setRow(table2);
        communsPdf.setCell(baseRow2, 100, datos.getNoSolicitud(), Color.black, false, "L", 10, communsPdf.setLineStyle(Color.black), "", communsPdf.setPadding(3f), Color.white);
        table2.draw();
    }
}