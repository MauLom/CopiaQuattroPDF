package com.copsis.models.sspins;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraJsonModel;

public class SspInsModel {

    private DataToolsModel fn = new DataToolsModel();
    private EstructuraJsonModel modelo = new EstructuraJsonModel();

    public EstructuraJsonModel procesar(PDFTextStripper stripper, PDDocument doc, String contenido) {
        try {
            modelo = new SspInsDiversosModel().procesar(fn.caratula(1, 5, stripper, doc));
            return modelo;
        } catch (Exception ex) {
            modelo.setError(
                    SspInsModel.this.getClass().getTypeName() + " | " + ex.getMessage() + " | " + ex.getCause());
            return modelo;
        }

    }
}
