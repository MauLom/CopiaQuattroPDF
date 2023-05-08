package com.copsis.models.mediacces;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraJsonModel;

public class MediaccesModel {
    private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();



    public EstructuraJsonModel procesar(PDFTextStripper stripper, PDDocument doc, String contenido) {
            try {      
              
                   
                modelo = new MediaccesSaludModel().procesar(fn.caratula(1, 2, stripper, doc));
                return modelo;
            } catch (Exception ex) {
                modelo.setError(
					MediaccesModel.this.getClass().getTypeName() + " | " + ex.getMessage() + " | " + ex.getCause());
			return modelo;
            }
    }


}
