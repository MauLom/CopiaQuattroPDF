package com.copsis.services;

import java.io.IOException;
import java.net.URL;

import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;

import com.copsis.controllers.forms.PdfForm;
import com.copsis.models.EstructurarReciboModel;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class IdentificaReciboService {
    public EstructurarReciboModel identificaRecibo(PdfForm pdfForm) throws IOException {
        EstructurarReciboModel recibo = new EstructurarReciboModel();            
    
	
        try {
            final URL scalaByExampleUrl = new URL(pdfForm.getUrl());
            try (PDDocument documentToBeParsed = PDDocument.load(scalaByExampleUrl.openStream())) {
                PDFTextStripper pdfStripper = new PDFTextStripper();
                pdfStripper.setStartPage(1);
                pdfStripper.setEndPage(1);
                String contenido = pdfStripper.getText(documentToBeParsed);
                return recibo;
            } catch (Exception e) {
                recibo.setError(IdentificaReciboService.this.getClass().getTypeName() + " | " + e.getMessage() + " | " + e.getCause());
                return recibo;
            } 
        } catch (IOException e) {
            // Manejar la excepción de la URL aquí si es necesario.
            recibo.setError(IdentificaReciboService.this.getClass().getTypeName() + " | " + e.getMessage() + " | " + e.getCause());
            return recibo;
        }
    }
        
    
}
