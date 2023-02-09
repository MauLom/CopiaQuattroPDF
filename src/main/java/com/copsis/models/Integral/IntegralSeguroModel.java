package com.copsis.models.Integral;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraJsonModel;

public class IntegralSeguroModel {
    private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
    public EstructuraJsonModel procesar(PDFTextStripper stripper, PDDocument doc, String contenido) {
        try {
            modelo = new IntegralSeguroSaludModel().procesar(fn.caratula(1, 1, stripper, doc));
            return modelo;
        } catch (Exception e) {
            return modelo;
        }
    }
}
