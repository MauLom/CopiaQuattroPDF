package com.copsis.models.constancia;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Component;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraConstanciaSatModel;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ConstanciaModel {
	private final ConstanciaSatModel constanciaSatModel;
	
	private DataToolsModel dataToolsModel = new DataToolsModel();
	private EstructuraConstanciaSatModel constancia = new EstructuraConstanciaSatModel();

	// Variables
	/*private PDFTextStripper pdfstripper;
	private PDDocument pdDoc;
	private String contenido;

	public ConstanciaModel(PDFTextStripper pdfstripper, PDDocument pdDoc, String contenido) {
		super();
		this.pdfstripper = pdfstripper;
		this.pdDoc = pdDoc;
		this.contenido = contenido;
	}*/
	
	public   EstructuraConstanciaSatModel procesar(PDFTextStripper pdfstripper, PDDocument pdDoc, String contenido) {
		try {
			
			constancia = constanciaSatModel.procesar(dataToolsModel.caratula(1, 4, pdfstripper, pdDoc));
			
			return constancia;			
		} catch (Exception ex) {
			constancia.setError(ConstanciaModel.this.getClass().getTypeName() + " | " + ex.getMessage() + " | "
					+ ex.getCause());
			return constancia;
		}
	}
	

}
