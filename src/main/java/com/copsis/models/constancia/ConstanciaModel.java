package com.copsis.models.constancia;

import java.awt.Rectangle;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Component;

import com.copsis.controllers.forms.PdfForm;
import com.copsis.models.CardSettings;
import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraConstanciaSatModel;
import com.copsis.models.Tabla.PDFTableStripper;
import com.copsis.services.WebhookService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ConstanciaModel {
	private final ConstanciaSatModel constanciaSatModel;
	private DataToolsModel dataToolsModel = new DataToolsModel();
	private EstructuraConstanciaSatModel constancia = new EstructuraConstanciaSatModel();
	private final WebhookService webhookService;

	public EstructuraConstanciaSatModel procesar(PDFTextStripper pdfstripper, PDDocument pdDoc, String contenido,
			PdfForm pdfForm) {
		try {
			constancia = constanciaSatModel.procesar(dataToolsModel.caratula(1, 4, pdfstripper, pdDoc), pdfForm);
			if (constancia.getTipoPersona().contains("Moral")) {// otra forma extrar texto pdf.	
				final double res = 72;
				PDFTableStripper stripper = new PDFTableStripper();
				stripper.setSortByPosition(true);
				
				stripper.setRegion(new Rectangle(9, (int) Math.round(1 * res),(int) Math.round(250 * res), (int) Math.round(250.0 * res)));
					
				for (int page = 0; page < 1; ++page) {				
					PDPage pdPage = pdDoc.getPage(page);
					stripper.extractTable(pdPage);
					for(int c=0; c<stripper.getColumns(); ++c) {
					
						for(int r=0; r<stripper.getRows(); ++r) {	
						
							if(stripper.getText(r, c).contains("Razón Social")){    
								 int lineas = stripper.getText(r, c).replace("Denominación/Razón Social:", "").split("\n").length;
						
								if(constancia.getRazonSocial().equals(constancia.getNombreComercial())){                            
							    
								} else if(constancia.getRazonSocial().length() > 0 && constancia.getNombreComercial().length() == 0
								&&  lineas == 1){								
								}								
								else{								
									constancia.setRazonSocial(stripper.getText(r, c).replace("Denominación/Razón Social:", "").replace("<\br>", "\n").replace("\n", " ").trim());
								}
							 													    
							}
							if(stripper.getText(r, c).contains("Régimen") && stripper.getText(r, c).contains("Capital:")) {
							   constancia.setRegimenDeCapital(stripper.getText(r, c).replace("Régimen Capital:", "").replace("<\br>", "\n").replace("\n", " ").replace("  ", " ").trim());	
							 }						
						}
					}

				}

			}

			return constancia;
		} catch (Exception ex) {		
			try {
				CardSettings cardSettings = CardSettings.builder()
						.fileUrl(pdfForm.getUrl())
						.sourceClass(ConstanciaModel.class.getName())
						.exceptionMessage(ex.getMessage())
						.build();
				webhookService.send(cardSettings);
			} catch (Exception e) {
				// do nothing
			}
			constancia.setError(
					ConstanciaModel.this.getClass().getTypeName() + " | " + ex.getMessage() + " | " + ex.getCause());
			return constancia;
		}
	}

}
