package com.copsis.models.aguila;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraJsonModel;

public class AguilaModel {

	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
	private PDFTextStripper stripper;
	private PDDocument doc;
	private String contenido;
	
	public AguilaModel(PDFTextStripper stripper, PDDocument doc, String contenido) {
		this.stripper = stripper;
		this.doc = doc;
		this.contenido = contenido;
	}
	
	
	public EstructuraJsonModel procesar() {
		try {
        
		System.out.println(fn.tipoPoliza(contenido));
		System.out.println();
		if(fn.tipoPoliza(contenido) == 0 || contenido.contains("Incendio de los Contenidos") ||fn.tipoPoliza(contenido)  == 4) {
			
			modelo  = new AguilaDiversosModel().procesar(fn.caratula(1, 3, stripper, doc));
		}
		
		if(fn.tipoPoliza(contenido) == 1) {
			modelo  = new AguilaAutosModel(fn.caratula(1, 3, stripper, doc)).procesar();	
		}
		
			return modelo;
		} catch (Exception ex) {
			modelo.setError(
					AguilaModel.this.getClass().getTypeName() + " | " + ex.getMessage() + " | " + ex.getCause());
			return modelo;
		}
	}
	
	
}
