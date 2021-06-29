package com.copsis.models.certificado;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraJsonModel;
import com.copsis.models.zurich.ZurichAutosModel;
import com.copsis.models.zurich.ZurichModel;

public class ZurichCertificadoModel {
	
	// Clases
	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
	// Variables
	private PDFTextStripper stripper;
	private PDDocument doc;
	private String contenido;
	private Integer pagIni =0;
	private Integer pagFin =0;

	public ZurichCertificadoModel(PDFTextStripper pdfStripper, PDDocument pdDoc, String contenido) {
		this.stripper = pdfStripper;
		this.doc = pdDoc;
		this.contenido = contenido;
	}
	
	public EstructuraJsonModel procesar() {
		try {
			
			switch (fn.tipoPoliza(contenido)) {
			case 1:// Autos
				modelo  = new ZurichAutosModel(fn.caratula(1, 2, stripper, doc)).procesar();				
				break;
			
			}
			return modelo;
		} catch (Exception ex) {
			modelo.setError(
					ZurichCertificadoModel.this.getClass().getTypeName() + " | " + ex.getMessage() + " | " + ex.getCause());
			return modelo;
		}
		
	}

}
