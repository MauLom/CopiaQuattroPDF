package com.copsis.models.berkley;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraJsonModel;

public class BerkleyModel {

	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
    
    public EstructuraJsonModel procesar(PDFTextStripper pdfStripper, PDDocument pdDoc, String contenido) {
        try {
            modelo = new BerkleyDiversosModel().procesar(fn.caratula(1, 5, pdfStripper, pdDoc));            
            return modelo;
        } catch (Exception ex) {
            modelo.setError(
                BerkleyModel.this.getClass().getTypeName() + " - catch:" + ex.getMessage() + " | " + ex.getCause());
        return modelo;
        }
    }
}
