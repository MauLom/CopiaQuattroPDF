package com.copsis.models.aig;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraJsonModel;

public class AigModel {
	// Clases
	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
	// Variables
	private PDFTextStripper stripper;
	private PDDocument doc;
	private String contenido;

	// Constructor
	public AigModel(PDFTextStripper pdfStripper, PDDocument pdDoc, String contenido) {
		this.stripper = pdfStripper;
		this.doc = pdDoc;
		this.contenido = contenido;
	}
	public EstructuraJsonModel procesar() {
		try {
			switch (fn.tipoPoliza(contenido)) {
			case 1:// Autos
				modelo  = new AigAutosModel(fn.caratula(1, 4, stripper, doc)).procesar();	
				break;			
			}					
			return modelo;
		} catch (Exception ex) {
			modelo.setError(
					AigModel.this.getClass().getTypeName() + " | " + ex.getMessage() + " | " + ex.getCause());
			return modelo;
		}
	}

}
