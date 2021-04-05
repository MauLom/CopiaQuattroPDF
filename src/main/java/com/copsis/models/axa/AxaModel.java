package com.copsis.models.axa;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraJsonModel;
import com.copsis.models.gnp.GnpModel;

public class AxaModel {
	// Clases
	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
	// Variables
	private PDFTextStripper stripper;
	private PDDocument doc;
	private String contenido;
	private int pagFin = 0;

	// Constructor
	public AxaModel(PDFTextStripper pdfStripper, PDDocument pdDoc, String contenido) {
		this.stripper = pdfStripper;
		this.doc = pdDoc;
		this.contenido = contenido;
	}
	
	public EstructuraJsonModel procesa() {
		try {
			if (contenido.contains("Datos del vehículo") && contenido.contains(" Vehicle description") == false) {	//AUTOS
				AxaAutosModel datosAxaAutos = new AxaAutosModel(fn.caratula(1, 1, stripper, doc), fn.textoBusqueda(stripper, doc, "RECIBO PROVISIONAL DE",false));
               modelo = datosAxaAutos.procesar();
            }
			
			return modelo;
		} catch (Exception ex) {
			modelo.setError(
					AxaModel.this.getClass().getTypeName() + " - catch:" + ex.getMessage() + " | " + ex.getCause());
			return modelo;
		}
		
	}

}
