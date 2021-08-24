package com.copsis.models.segurosMty;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraJsonModel;

	
public class SegurosMtyModel {
	// Clases
	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
	// Variables
	private PDFTextStripper stripper;
	private PDDocument doc;
	private String contenido;
	private Integer pagIni =0;
	private Integer pagFin =0;

	public SegurosMtyModel(PDFTextStripper pdfStripper, PDDocument pdDoc, String contenido) {
		this.stripper = pdfStripper;
		this.doc = pdDoc;
		this.contenido = contenido;
	}
	
	public EstructuraJsonModel procesa() {
		try {
			switch (fn.tipoPoliza(contenido)) {
			case 2:// Salud
				modelo  = new SegurosMtySalud(fn.caratula(1, 4, stripper, doc)).procesar();				
				break;
			}
			return modelo;
		} catch (Exception ex) {
			modelo.setError(
					SegurosMtyModel.this.getClass().getTypeName() + " | " + ex.getMessage() + " | " + ex.getCause());
			return modelo;
		}
		
	}
}
