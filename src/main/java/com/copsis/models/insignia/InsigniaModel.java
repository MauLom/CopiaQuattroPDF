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

		
		    if (tipo == 5) {
				modelo = new InsigniaVidaModel(fn.caratula(1, 4, stripper, doc)).procesar();
			}

			if (tipo == 5 && fn.caratula(2, 2, stripper, doc).contains("PÓLIZA DE SEGURO DE VIDA INDIVIDUAL")) {
				modelo = new InsigniaVidaBModel().procesar(fn.caratula(2, 3, stripper, doc));
			}
			
			if (tipo == 5 && fn.caratula(2, 2, stripper, doc).contains("PÓLIZA DE SEGURO DE VIDA INDIVIDUAL") &&  fn.caratula(2, 2, stripper, doc).contains("Póliza:")) {
				modelo = new InsigniaVidaModel(fn.caratula(2, 3, stripper, doc)).procesar();
			}
			
			return modelo;
		} catch (Exception ex) {
			modelo.setError(
					InsigniaModel.this.getClass().getTypeName() + " - catch:" + ex.getMessage() + " | " + ex.getCause());
			return modelo;
		}
	}
	
}
