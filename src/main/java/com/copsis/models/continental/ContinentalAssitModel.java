package com.copsis.models.continental;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraJsonModel;

public class ContinentalAssitModel {
    
    private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();

    public EstructuraJsonModel procesar(PDFTextStripper pdfStripper, PDDocument pdDoc, String contenido) {
        try {

             modelo = new ContinentalAssitSalud().procesar(fn.caratula(1, 2, pdfStripper, pdDoc));	
            
             return modelo;
        } catch (Exception e) {
            return modelo;
        }
    
    }

}
