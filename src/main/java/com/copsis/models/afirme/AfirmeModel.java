package com.copsis.models.afirme;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraJsonModel;
import com.copsis.models.hdi.HdiAutosModel;

public class AfirmeModel {
	// Clases
	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
	// Variables
	private PDFTextStripper stripper;
	private PDDocument doc;
	private String contenido;
	private Integer pagIni =0;
	private Integer pagFin =0;
	
	public AfirmeModel(PDFTextStripper pdfStripper, PDDocument pdDoc, String contenido) {
		this.stripper = pdfStripper;
		this.doc = pdDoc;
		this.contenido = contenido;
	}
	public EstructuraJsonModel procesar() {
	
		try {
System.out.println(fn.tipoPoliza(contenido));
			switch (fn.tipoPoliza(contenido)) {
			case 1://Autos
				modelo  = new AfirmeAutosModel(fn.caratula(1, 3, stripper, doc)).procesar();								  
				break;		
			default:
				break;
			}
			
			return modelo;
		} catch (Exception ex) {
			modelo.setError(
					AfirmeModel.this.getClass().getTypeName() + " | " + ex.getMessage() + " | " + ex.getCause());
			return modelo;
		}
		
	}

}
