package com.copsis.services;

import java.io.IOException;
import java.net.URL;

import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;

import com.copsis.controllers.forms.PdfForm;
import com.copsis.models.EstructuraJsonModel;
import com.copsis.models.certificado.ZurichCertificadoModel;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class IdentificaCertificadoService {
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
	 public EstructuraJsonModel queIndentificaCertificado( PdfForm pdfForm) throws  IOException {
		 try {
	    	 final URL scalaByExampleUrl = new URL(pdfForm.getUrl());
             final PDDocument documentToBeParsed = PDDocument.load(scalaByExampleUrl.openStream());
             PDFTextStripper pdfStripper = new PDFTextStripper();
             COSDocument cosDoc = documentToBeParsed.getDocument();
             PDDocument pdDoc = new PDDocument(cosDoc);
             pdfStripper.setStartPage(1);
             pdfStripper.setEndPage(1);
             pdfStripper.setParagraphStart("@@@");
             String contenido = pdfStripper.getText(pdDoc);
             boolean encontro = false;
             
             if (encontro == false) {
            	 if (contenido.contains("zurich") || contenido.contains("Zurich")) {
            		 ZurichCertificadoModel datosZurich = new ZurichCertificadoModel(pdfStripper, pdDoc, contenido);
	                    modelo = datosZurich.procesar();
	                    encontro = true;
	                }
            	 
             }
             
             return modelo;
			
		} catch (Exception ex) {
			modelo.setError(IdentificaCertificadoService.this.getClass().getTypeName() +" - catch:" + ex.getMessage() + " | " + ex.getCause());;
		       return modelo;
		}
		 
	 }
	
}
