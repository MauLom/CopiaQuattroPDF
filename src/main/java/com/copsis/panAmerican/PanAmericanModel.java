package com.copsis.panAmerican;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraJsonModel;

public class PanAmericanModel {
	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
	
	private PDFTextStripper stripper;
	private PDDocument doc;
	private String contenido;
	
	public PanAmericanModel(PDFTextStripper pdfStripper, PDDocument pdDoc, String contenido) {
		this.stripper = pdfStripper;
		this.doc = pdDoc;
		this.contenido = contenido;
	}
	
	public EstructuraJsonModel procesar() {
		try {
			
			switch (fn.tipoPoliza(contenido)) {
			case 2:
				modelo =new  PanAmericanSaludModel().procesar(fn.caratula(0, 7, stripper, doc));
				break;
			}
			
			return modelo;
			} catch (Exception ex) {
				modelo.setError(
						PanAmericanModel.this.getClass().getTypeName() + " | " + ex.getMessage() + " | " + ex.getCause());
				return modelo;
			}
	}
	
}
