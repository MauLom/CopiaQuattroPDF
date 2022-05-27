package com.copsis.models.constancia;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.copsis.controllers.forms.PdfForm;
import com.copsis.models.CardSettings;
import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraConstanciaSatModel;
import com.copsis.services.WebhookService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ConstanciaModel {
	private final ConstanciaSatModel constanciaSatModel;	
	private DataToolsModel dataToolsModel = new DataToolsModel();
	private EstructuraConstanciaSatModel constancia = new EstructuraConstanciaSatModel();
	private final WebhookService webhookService;
	
	public   EstructuraConstanciaSatModel procesar(PDFTextStripper pdfstripper, PDDocument pdDoc, String contenido, PdfForm pdfForm) {
		try {			
			constancia = constanciaSatModel.procesar(dataToolsModel.caratula(1, 4, pdfstripper, pdDoc), pdfForm);
			
			return constancia;			
		} catch (Exception ex) {
			try  {
				CardSettings cardSettings = CardSettings.builder()
						.fileUrl(pdfForm.getUrl())
						.sourceClass(ConstanciaModel.class.getName())
						.exceptionMessage(ex.getMessage())
						.build();
				webhookService.send(cardSettings);
			} catch(Exception e) {
				// do nothing
			}
			constancia.setError(ConstanciaModel.this.getClass().getTypeName() + " | " + ex.getMessage() + " | " + ex.getCause());
			return constancia;
		}
	}
	

}
