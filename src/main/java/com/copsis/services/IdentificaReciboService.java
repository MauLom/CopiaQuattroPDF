package com.copsis.services;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;
import com.copsis.controllers.forms.PdfForm;
import com.copsis.models.EstructurarReciboModel;
import com.copsis.models.recibos.ReciboCfeModel;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class IdentificaReciboService {
    public EstructurarReciboModel identificaRecibo(PdfForm pdfForm) throws IOException {
        EstructurarReciboModel recibo = new EstructurarReciboModel();

            final URL scalaByExampleUrl = new URL(pdfForm.getUrl());
            try (PDDocument documentToBeParsed = PDDocument.load(scalaByExampleUrl.openStream())) {
                PDFTextStripper pdfStripper = new PDFTextStripper();
                pdfStripper.setStartPage(1);
                pdfStripper.setEndPage(1);
                String contenido = pdfStripper.getText(documentToBeParsed);
               
                if(this.getIndentificaReciboLuz(contenido)){
                   recibo = new ReciboCfeModel().procesar(this.recibo(1,2,pdfStripper,documentToBeParsed));
                }
                if(this.getIndentificaReciboTotalPlay(contenido)){
                   
                }

                return recibo;
            } catch (Exception e) {
                recibo.setError(IdentificaReciboService.this.getClass().getTypeName() + " | " + e.getMessage() + " | "
                        + e.getCause());
                return recibo;
            }
    
    }

    public boolean getIndentificaReciboLuz(String contenido) {     
        boolean encotro= false;
        Set<String> diccionario = new HashSet<>(Arrays.asList("Energía", "CFE", "TARIFA", "MEDIDOR"));
        encotro = getContadorCoincidencias(contenido, encotro, diccionario);
        return encotro;
    }

     public boolean getIndentificaReciboTotalPlay(String contenido) {     
        boolean encotro= false;
        Set<String> diccionario = new HashSet<>(Arrays.asList("TOTAL PLAY", "mitotalplay", "TELECOMUNICACIONES", "telefónico"));
        encotro = getContadorCoincidencias(contenido, encotro, diccionario);
        return encotro;
    }

    private boolean getContadorCoincidencias(String contenido, boolean encotro, Set<String> diccionario) {
        String[] lineas = contenido.split("\n");
        int contadorCoincidencias = 0;

        for (String linea : lineas) {
            long coincidencias = diccionario.stream()
                    .filter(linea::contains)
                    .count();

            if (coincidencias > 0) {             
                contadorCoincidencias += coincidencias;
            }

            if (contadorCoincidencias > 3) {
                break;
            }
        }

        if (contadorCoincidencias > 3) {
          encotro = true;
        }
        return encotro;
    }

    public String recibo(int inicio, int fin, PDFTextStripper stripper, PDDocument doc) throws IOException {
        stripper.setStartPage(inicio);
        stripper.setEndPage(fin);
        stripper.setParagraphStart("@@@");
        stripper.setWordSeparator("###");
        stripper.setSortByPosition(true);
        return stripper.getText(doc);
     }
    

}
