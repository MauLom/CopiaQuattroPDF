package com.copsis.models.sura;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraJsonModel;

public class SuraModel {
	// Clases
	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
	// Variables
	private PDFTextStripper stripper;
	private PDDocument doc;
	private String contenido;
	
	// Constructor
	public SuraModel(PDFTextStripper pdfStripper, PDDocument pdDoc, String contenido) {
		this.stripper = pdfStripper;
		this.doc = pdDoc;
		this.contenido = contenido;
	}
	
	public EstructuraJsonModel procesar() {
		try {		

			switch (fn.tipoPoliza(contenido)) {				
			case 1://Autos
					modelo  = new SuraAutosModel(fn.caratula(1, 3, stripper, doc)).procesar();					
				break;
			case 2://Salud
				modelo  = new SuraSaludModel(fn.caratula(1, 3, stripper, doc)).procesar();					
			break;


			}

			return modelo;
		} catch (Exception ex) {
			modelo.setError(
					SuraModel.this.getClass().getTypeName() + " | " + ex.getMessage() + " | " + ex.getCause());
			return modelo;
		}
		
	}
	

}
