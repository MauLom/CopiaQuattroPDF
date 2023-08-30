package com.copsis.models.prevem;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraJsonModel;

public class PrevemModel {
    private EstructuraJsonModel modelo = new EstructuraJsonModel();
    private DataToolsModel fn = new DataToolsModel();

    public EstructuraJsonModel procesar(PDFTextStripper pdfStripper, PDDocument pdDoc, String contenido) {
    	try {
						
            modelo =new  PrevemSaludModel().procesar(fn.caratula(0, 7, pdfStripper, pdDoc));
		
			return modelo;
		} catch (Exception ex) {
			modelo.setError(
                PrevemModel.this.getClass().getTypeName() + " | " + ex.getMessage() + " | " + ex.getCause());
			return modelo;
		}
    }
}
