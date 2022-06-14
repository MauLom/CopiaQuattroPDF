package com.copsis.models.general;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraJsonModel;


public class GeneralModel {
	
	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
	private String contenido;
	
	public EstructuraJsonModel procesar(PDFTextStripper pdfStripper, PDDocument pdDoc, String contenido) {
		try {			
			modelo = new GeneralSaludModel().procesar(fn.caratula(1, 3, pdfStripper, pdDoc));			
			return modelo;
		} catch (Exception ex) {
			modelo.setError(GeneralModel.this.getClass().getTypeName() + " | " + ex.getMessage() + " | " + ex.getCause());
			return modelo;
		}
	}
}
