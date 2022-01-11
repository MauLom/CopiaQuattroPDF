package com.copsis.models.potosi;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraJsonModel;
import com.copsis.models.ana.AnaModel;

public class PotosiModel {

	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();

	private PDFTextStripper stripper;
	private PDDocument doc;
	private String contenido;
	
	

	public PotosiModel(PDFTextStripper stripper, PDDocument doc, String contenido) {
		super();
		this.stripper = stripper;
		this.doc = doc;
		this.contenido = contenido;
	}
	public EstructuraJsonModel procesar() {
		try {
		
			if(fn.tipoPoliza(contenido) == 1) {			
				modelo = new PotosiAutosModel(fn.caratula(1, 2, stripper, doc)).procesar();
			}
			
			return modelo;
		} catch (Exception ex) {
			modelo.setError(PotosiModel.this.getClass().getTypeName() + " | " + ex.getMessage() + " | " + ex.getCause());
			return modelo;
		}
		
	}

}
