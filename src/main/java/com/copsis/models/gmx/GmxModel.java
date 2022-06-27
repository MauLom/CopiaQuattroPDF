package com.copsis.models.gmx;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraJsonModel;

public class GmxModel {
	// Clases
	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
	// Variables
	private PDFTextStripper stripper;
	private PDDocument doc;
	private String contenido;

	public GmxModel(PDFTextStripper pdfStripper, PDDocument pdDoc, String contenido) {
		this.stripper = pdfStripper;
		this.doc = pdDoc;
		this.contenido = contenido;
	}

	public EstructuraJsonModel procesar() {
		try {
           
              int tipo = 0;
	          if(contenido.contains("giro o actividad")) {
	        	  tipo = 4 ;
	          }
			switch (fn.tipoPoliza(contenido) ==  0 ? fn.tipoPoliza(contenido) : tipo ) {
			case 1:
				
				break;
			case 4:
				modelo = new GmxDiversosModel(fn.caratula(1, 4, stripper, doc)).procesar();
				break;
			default:
				modelo = new GmxDiversosModel(fn.caratula(1, 3, stripper, doc)).procesar();
				break;
			}

			return modelo;
		} catch (Exception ex) {
			modelo.setError(GmxModel.this.getClass().getTypeName() + " | " + ex.getMessage() + " | " + ex.getCause());
			return modelo;
		}
	}

}
