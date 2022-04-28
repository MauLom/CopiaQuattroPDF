package com.copsis.models.constancia;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraConstanciaSatModel;

public class constanciaModel {

	private DataToolsModel fn = new DataToolsModel();
	private EstructuraConstanciaSatModel constancia = new EstructuraConstanciaSatModel();

	// Variables
	private PDFTextStripper stripper;
	private PDDocument doc;
	private String contenido;

	public constanciaModel(PDFTextStripper stripper, PDDocument doc, String contenidopdf) {
		super();
		this.stripper = stripper;
		this.doc = doc;
		this.contenido = contenidopdf;
	}
	
	public   EstructuraConstanciaSatModel procesar() {
		try {
			
			constancia = new  constanciaSatModel(fn.caratula(1, 4, stripper, doc)).procesar();
			
			return constancia;			
		} catch (Exception ex) {
			constancia.setError(constanciaModel.this.getClass().getTypeName() + " | " + ex.getMessage() + " | "
					+ ex.getCause());
			return constancia;
		}
	}
	

}
