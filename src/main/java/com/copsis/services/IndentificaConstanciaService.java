package com.copsis.services;

import java.io.IOException;
import java.net.URL;

import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;

import com.copsis.controllers.forms.PdfForm;
import com.copsis.models.EstructuraConstanciaSatModel;
import com.copsis.models.constancia.ConstanciaModel;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class IndentificaConstanciaService {
	private final ConstanciaModel constanciaModel;
	
	
	public EstructuraConstanciaSatModel indentificaConstancia (PdfForm pdfForm) throws IOException {
		
		EstructuraConstanciaSatModel constancia = new EstructuraConstanciaSatModel();
		try {
			
			final URL scalaByExampleUrl = new URL(pdfForm.getUrl());
			final PDDocument documentToBeParsed = PDDocument.load(scalaByExampleUrl.openStream());
			PDFTextStripper pdfStripper = new PDFTextStripper();
			COSDocument cosDoc = documentToBeParsed.getDocument();
			PDDocument pdDoc = new PDDocument(cosDoc);
			pdfStripper.setStartPage(1);
			pdfStripper.setEndPage(1);

			String contenido = pdfStripper.getText(pdDoc);
			
			if (contenido.contains("CONSTANCIA DE SITUACIÓN FISCAL") || contenido.contains("CÉDULA DE IDENTIFICACIÓN FISCAL ")) {				
				constancia = constanciaModel.procesar(pdfStripper, pdDoc, contenido);
				
				documentToBeParsed.close();
				cosDoc.close();
				pdDoc.close();
			}

			return constancia;
			
		} catch (Exception ex) {
			constancia.setError(IndentificaConstanciaService.this.getClass().getTypeName() + " | " + ex.getMessage() + " | "
					+ ex.getCause());
			return constancia;
		}
		
	}
	
}
