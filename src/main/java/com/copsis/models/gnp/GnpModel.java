package com.copsis.models.gnp;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import com.copsis.models.EstructuraJsonModel;
import com.copsis.services.IdentificaPolizaService;

public class GnpModel {

	   private IdentificaPolizaService identifica = new IdentificaPolizaService();
	    private PDFTextStripper stripper;
	    private PDDocument doc;
	    private String contenido;
	    
	 	 private EstructuraJsonModel modelo = new EstructuraJsonModel();
}
