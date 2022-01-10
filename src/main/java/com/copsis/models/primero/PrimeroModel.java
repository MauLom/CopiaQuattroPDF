package com.copsis.models.primero;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraJsonModel;

import lombok.Data;

 @Data
public class PrimeroModel {
	// Clases
	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
	// Variables
	private PDFTextStripper stripper;
	private PDDocument doc;
	private String contenido;

	// Constructor
	public PrimeroModel(PDFTextStripper pdfStripper, PDDocument pdDoc, String contenido) {
		this.stripper = pdfStripper;
		this.doc = pdDoc;
		this.contenido = contenido;

	}

	public EstructuraJsonModel procesar() {
		try {
			switch (fn.tipoPoliza(contenido)) {
			case 1:// Autos
	
				if(contenido.contains("Motor") || contenido.contains("Serie")) {
					modelo  = new primeroAutosModel(fn.caratula(1, 3, stripper, doc)).procesar();
				}else {
					modelo  = new PrimeroAutosBModel(fn.caratula(1, 3, stripper, doc)).procesar();	
				}
						
				break;
			case 4:// Diversos
				modelo  = new PrimeroDiversosModel(fn.caratula(1, 3, stripper, doc)).procesar();	
				break;
			}

			return modelo;
		} catch (Exception ex) {
			modelo.setError(
					PrimeroModel.this.getClass().getTypeName() + " - catch:" + ex.getMessage() + " | " + ex.getCause());

			return modelo;
		}
	}

}
