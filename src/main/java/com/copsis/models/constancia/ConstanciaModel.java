package com.copsis.models.constancia;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraConstanciaSatModel;

public class ConstanciaModel {

	private DataToolsModel dataToolsModel = new DataToolsModel();
	private EstructuraConstanciaSatModel constancia = new EstructuraConstanciaSatModel();

	// Variables
	private PDFTextStripper pdfstripper;
	private PDDocument pdDoc;
	private String contenido;

	public ConstanciaModel(PDFTextStripper pdfstripper, PDDocument pdDoc, String contenido) {
		super();
		this.pdfstripper = pdfstripper;
		this.pdDoc = pdDoc;
		this.contenido = contenido;
	}
	
	public   EstructuraConstanciaSatModel procesar() {
		try {
			
			constancia = new  ConstanciaSatModel(dataToolsModel.caratula(1, 4, pdfstripper, pdDoc)).procesar();
			
			return constancia;			
		} catch (Exception ex) {
			constancia.setError(ConstanciaModel.this.getClass().getTypeName() + " | " + ex.getMessage() + " | "
					+ ex.getCause());
			return constancia;
		}
	}
	

}
