package com.copsis.models.insignia;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraJsonModel;

public class InsigniaModel {


	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();

	private PDFTextStripper stripper;
	private PDDocument doc;
	private String contenido;
	private int tipo  = 0;
	
	public InsigniaModel(PDFTextStripper pdfStripper, PDDocument pdDoc, String contenido) {
		this.stripper = pdfStripper;
		this.doc = pdDoc;
		this.contenido = contenido;
	}
	
	public EstructuraJsonModel procesar() {
		try {
			
			tipo =fn.tipoPoliza(contenido);
			System.out.println(tipo);
			
		 if (tipo == 1) {
				modelo = new InsigniaVidaModel(fn.caratula(1, 2, stripper, doc)).procesar();
			}
			
			return modelo;
		} catch (Exception ex) {
			modelo.setError(
					InsigniaModel.this.getClass().getTypeName() + " - catch:" + ex.getMessage() + " | " + ex.getCause());
			return modelo;
		}
	}
	
}
