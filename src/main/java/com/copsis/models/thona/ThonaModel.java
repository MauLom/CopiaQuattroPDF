package com.copsis.models.thona;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraJsonModel;

public class ThonaModel {
	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
	
	private PDFTextStripper stripper;
	private PDDocument doc;
	private String contenido;
	

	public ThonaModel(PDFTextStripper pdfStripper, PDDocument pdDoc, String contenido) {
		this.stripper = pdfStripper;
		this.doc = pdDoc;
		this.contenido = contenido;
	}
	
	public EstructuraJsonModel procesar() {
		try {
			switch (fn.tipoPoliza(contenido)) {
			case 2:
				modelo =new  ThonaSaludModel().procesar(fn.caratula(0, 7, stripper, doc));
			
				break;
			}
			return modelo;
		} catch (Exception ex) {
			modelo.setError(
					ThonaModel.this.getClass().getTypeName() + " | " + ex.getMessage() + " | " + ex.getCause());
			return modelo;
		}
		
	}
}
