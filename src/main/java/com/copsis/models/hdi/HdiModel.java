package com.copsis.models.hdi;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraJsonModel;

public class HdiModel {
	// Clases
	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
	// Variables
	private PDFTextStripper stripper;
	private PDDocument doc;
	private String contenido;

	public HdiModel(PDFTextStripper pdfStripper, PDDocument pdDoc, String contenido) {
		this.stripper = pdfStripper;
		this.doc = pdDoc;
		this.contenido = contenido;
	}

	public EstructuraJsonModel procesar() {
		try {
			if (fn.tipoPoliza(contenido) == 1) {
				modelo = new HdiAutosModel(fn.caratula(1, 3, stripper, doc)).procesar();
			}
			return modelo;
		} catch (Exception ex) {
			modelo.setError(HdiModel.this.getClass().getTypeName() + " | " + ex.getMessage() + " | " + ex.getCause());
			return modelo;
		}

	}

}
