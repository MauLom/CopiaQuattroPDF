package com.copsis.models.gnp;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraJsonModel;
import com.copsis.models.gnp.autos.GnpAutos2Model;
import com.copsis.models.gnp.autos.GnpAutosModel;

public class GnpModel {
	//Clases
	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
	// Variables
	private PDFTextStripper stripper;
	private PDDocument doc;
	private String contenido;
	private int pagFin = 0;
	//Constructor
	public GnpModel(PDFTextStripper pdfStripper, PDDocument pdDoc, String contenido) {
		this.stripper = pdfStripper;
		this.doc = pdDoc;
		this.contenido = contenido;
	}

	public EstructuraJsonModel procesa() {

		try {

			if (contenido.contains("Motor") && contenido.contains("Placas")) {
				if (contenido.contains("DESGLOSE DE COBERTURAS Y SERVICIOS")) { // AUTOS NARANJA AZUL

					pagFin = fn.pagFinRango(stripper, doc, "AGENTE");
					if (pagFin > 0) {
						modelo = new GnpAutosModel(fn.caratula(1, pagFin, stripper, doc)).procesar();
					}
				} else {// AUTOS AZUL
					pagFin = fn.pagFinRango(stripper, doc, "Clave");
					System.out.println(pagFin);
					if (pagFin > 0) {
						 modelo = new GnpAutos2Model(fn.caratula(1, pagFin, stripper, doc)).procesar();
					}
				}
			}
			return modelo;
		} catch (Exception ex) {
			modelo.setError(
					GnpModel.this.getClass().getTypeName() + " - catch:" + ex.getMessage() + " | " + ex.getCause());
			;
			return modelo;
		}
	}
}
