package com.copsis.models.latino;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraJsonModel;

public class LatinoSeguroModel {

	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();

	private PDFTextStripper stripper;
	private PDDocument doc;
	private String contenido;
	public LatinoSeguroModel(PDFTextStripper stripper, PDDocument doc, String contenido) {
		this.stripper = stripper;
		this.doc = doc;
		this.contenido = contenido;
	}
	
	
	public EstructuraJsonModel procesar() {
		try {

			 switch (fn.tipoPoliza(contenido)) {
			   case 1:
				   modelo = new LatinoSeguroAutoModel(fn.caratula(1, 2, stripper, doc)).procesar();
				break;

			default:
				break;
			}
			 return modelo;
		} catch (Exception ex) {
			modelo.setError(LatinoSeguroModel.this.getClass().getTypeName() + " | " + ex.getMessage() + " | " + ex.getCause());
			return modelo;
		}
	
	}
	
	
}
