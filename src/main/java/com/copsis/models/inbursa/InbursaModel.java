package com.copsis.models.inbursa;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraJsonModel;

public class InbursaModel {
	// Clases
	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
	// Variables
	private PDFTextStripper stripper;
	private PDDocument doc;
	private String contenido;
	private Integer pagIni =0;
	private Integer pagFin =0;


	
	
	public InbursaModel(PDFTextStripper pdfStripper, PDDocument pdDoc, String contenido) {
		this.stripper = pdfStripper;
		this.doc = pdDoc;
		this.contenido = contenido;
	}
	public EstructuraJsonModel procesar() {
		try {
			switch (fn.tipoPoliza(contenido)) {

			case 1:// Autos
				modelo  = new InbursaAutosModel(fn.caratula(1, 2, stripper, doc),fn.textoBusqueda(stripper, doc, "DETALLE DE RECIBOS", false)).procesar();
				
				break;
			}

			return modelo;
		} catch (Exception ex) {
			modelo.setError(
					InbursaModel.this.getClass().getTypeName() + " | " + ex.getMessage() + " | " + ex.getCause());
			return modelo;
		}
		
	}

}
