package com.copsis.models.zurich;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraJsonModel;
import com.copsis.models.certificado.ZurichCertificadoGrupo;

public class ZurichModel {

	// Clases
	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
	// Variables
	private PDFTextStripper stripper;
	private PDDocument doc;
	private String contenido;
	private Integer pagIni =0;
	private Integer pagFin =0;

	public ZurichModel(PDFTextStripper pdfStripper, PDDocument pdDoc, String contenido) {
		this.stripper = pdfStripper;
		this.doc = pdDoc;
		this.contenido = contenido;
	}
	
	public EstructuraJsonModel procesar() {
		try {
			
			switch (fn.tipoPoliza(contenido)) {
			case 2:// Autos
				modelo  = new ZurichCertificadoGrupo(fn.caratula(1, 2, stripper, doc)).procesar();				
				break;
			
			}
			return modelo;
		} catch (Exception ex) {
			modelo.setError(
					ZurichModel.this.getClass().getTypeName() + " | " + ex.getMessage() + " | " + ex.getCause());
			return modelo;
		}
		
	}
	
}
