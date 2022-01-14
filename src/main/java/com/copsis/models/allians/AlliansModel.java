package com.copsis.models.allians;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraJsonModel;

public class AlliansModel {
	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();

	private PDFTextStripper stripper;
	private PDDocument doc;
	private String contenido;
	public AlliansModel(PDFTextStripper stripper, PDDocument doc, String contenido) {
		super();
		this.stripper = stripper;
		this.doc = doc;
		this.contenido = contenido;
	}
	
	public EstructuraJsonModel procesar() {
		try {
			if (fn.tipoPoliza(contenido) == 2) {				
				modelo = new AlliansSaludModel(fn.caratula(1, 2, stripper, doc)).procesar();
			}
			return modelo;
		} catch (Exception ex) {
			modelo.setError(AlliansModel.this.getClass().getTypeName() + " | " + ex.getMessage() + " | " + ex.getCause());
			return modelo;
		}
	}
	
	

}
