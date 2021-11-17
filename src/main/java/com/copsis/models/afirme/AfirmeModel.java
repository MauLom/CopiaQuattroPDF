package com.copsis.models.afirme;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraJsonModel;

public class AfirmeModel {
	// Clases
	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
	// Variables
	private PDFTextStripper stripper;
	private PDDocument doc;
	private String contenido;
	private Integer pagIni =0;

	
	public AfirmeModel(PDFTextStripper pdfStripper, PDDocument pdDoc, String contenido) {
		this.stripper = pdfStripper;
		this.doc = pdDoc;
		this.contenido = contenido;
	}
	public EstructuraJsonModel procesar() {
	
		try {
			if(fn.tipoPoliza(contenido) == 1) { // autos
				if(contenido.contains("AUTOMÓVILES RESIDENTES")) {
					
					modelo  = new AfirmeAutosBModel(fn.caratula(1, 2, stripper, doc),fn.recibos(stripper, doc, "RECIBO DE PRIMAS")).procesar();	
				}else {
					pagIni = fn.pagFinRango(stripper, doc, "DESGLOSE DE COBERTURAS");		
					modelo  = new AfirmeAutosModel(fn.caratula(pagIni, pagIni+2, stripper, doc)).procesar();	
				}
			}
			
			return modelo;
		} catch (Exception ex) {
			modelo.setError(
					AfirmeModel.this.getClass().getTypeName() + " | " + ex.getMessage() + " | " + ex.getCause());
			return modelo;
		}
		
	}

}
