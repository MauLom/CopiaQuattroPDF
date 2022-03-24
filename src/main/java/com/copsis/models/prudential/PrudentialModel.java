package com.copsis.models.prudential;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraJsonModel;

public class PrudentialModel {

	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();	

	private PDFTextStripper stripper;
	private PDDocument doc;
	private String contenido;
	
	
	public PrudentialModel(PDFTextStripper pdfStripper, PDDocument pdDoc, String contenido) {
		super();
		this.stripper = pdfStripper;
		this.doc = pdDoc;
		this.contenido = contenido;
	}
	
	public EstructuraJsonModel procesar() {
		try {
			
				if(fn.tipoPoliza(contenido) ==  5) {
					modelo = new PrudentialVidaModel(fn.caratula(1, 2, stripper, doc)).procesar();
				}
			
			return modelo;
		} catch (Exception ex) {
			modelo.setError(
					PrudentialModel.this.getClass().getTypeName() + " | " + ex.getMessage() + " | " + ex.getCause());
			return modelo;
		}
		
	}
	
	
	
}
