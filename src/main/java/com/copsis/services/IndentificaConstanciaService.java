package com.copsis.services;

import java.io.IOException;
import java.net.URL;

import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;

import com.copsis.controllers.forms.PdfForm;
import com.copsis.models.CardSettings;
import com.copsis.models.EstructuraConstanciaSatModel;
import com.copsis.models.constancia.ConstanciaModel;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Service
@Slf4j
public class IndentificaConstanciaService {
	private final ConstanciaModel constanciaModel;	
	private final WebhookService webhookService;
	
	public EstructuraConstanciaSatModel indentificaConstancia (PdfForm pdfForm) throws IOException {
		
		EstructuraConstanciaSatModel constancia = new EstructuraConstanciaSatModel();
		PDDocument documentToBeParsed = null;
		COSDocument cosDoc = null;
		PDDocument pdDoc = null;
		
		try {
			
			final URL scalaByExampleUrl = new URL(pdfForm.getUrl());
			documentToBeParsed = PDDocument.load(scalaByExampleUrl.openStream());
			PDFTextStripper pdfStripper = new PDFTextStripper();
			cosDoc = documentToBeParsed.getDocument();
			pdDoc = new PDDocument(cosDoc);
			pdfStripper.setStartPage(1);
			pdfStripper.setEndPage(1);
			String contenido = pdfStripper.getText(pdDoc);
			if (contenido.contains("CONSTANCIA DE SITUACIÓN FISCAL") || contenido.contains("CÉDULA DE IDENTIFICACIÓN FISCAL ")) {				
				constancia = constanciaModel.procesar(pdfStripper, pdDoc, contenido, pdfForm);
				return constancia;
			}

			String errorMessage = "Documento de tipo no reconocido.";
			
			try  {
				CardSettings cardSettings = CardSettings.builder()
						.fileUrl(pdfForm.getUrl())
						.sourceClass(IndentificaConstanciaService.class.getName())
						.exceptionMessage(errorMessage)
						.build();
				webhookService.send(cardSettings);
			} catch(Exception e) {
				// do nothing
			}
			constancia.setError(errorMessage);
			return constancia;
			
		} catch(IOException e) {
			constancia.setError(IndentificaConstanciaService.this.getClass().getTypeName() + " | " + e.getMessage() + " | "
					+ e.getCause());
			return constancia;
		} catch (Exception ex) {
			constancia.setError(IndentificaConstanciaService.this.getClass().getTypeName() + " | " + ex.getMessage() + " | "
					+ ex.getCause());
			return constancia;
		} finally {
			if(documentToBeParsed != null) documentToBeParsed.close();
			if(cosDoc != null) cosDoc.close();	
			if(pdDoc != null) pdDoc.close();
			
		}
		
	}
	
}
