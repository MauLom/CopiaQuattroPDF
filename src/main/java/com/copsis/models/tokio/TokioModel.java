package com.copsis.models.tokio;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraJsonModel;

public class TokioModel {
    private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
	
	
	
	public EstructuraJsonModel procesar(PDFTextStripper pdfStripper, PDDocument pdDoc, String contenido) {
		try {		
         
				modelo =new  TokioDiversosModel().procesar(fn.caratula(0, 7, pdfStripper, pdDoc));			
			return modelo;
		} catch (Exception ex) {
			modelo.setError(
                TokioModel.this.getClass().getTypeName() + " | " + ex.getMessage() + " | " + ex.getCause());
			return modelo;
		}
		
	}
}
