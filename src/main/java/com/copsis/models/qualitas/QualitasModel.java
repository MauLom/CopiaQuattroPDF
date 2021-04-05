package com.copsis.models.qualitas;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraJsonModel;

public class QualitasModel {
	
	//Clases
	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();	
	//Variables
	private PDFTextStripper stripper;
	private PDDocument doc;
	private String contenido;
    //Constructor
	public QualitasModel(PDFTextStripper pdfStripper, PDDocument pdDoc, String contenido) {
		this.stripper = pdfStripper;
		this.doc = pdDoc;
		this.contenido = contenido;
	}

	private int pagIni = 0;
	private int pagFin = 0;

	public EstructuraJsonModel procesa() {

		try {
			if (contenido.contains("SEGURO DE AUTOMÓVILES") || contenido.contains("POLIZA VEHICULOS ")) {
				pagIni = fn.pagFinRango(stripper, doc, "OFICINA DE");
				pagFin = fn.pagFinRango(stripper, doc, "IMPORTE TOTAL");

				if (pagIni < pagFin) {
					qualitasAutosModel datosQualitasAutos = new qualitasAutosModel(
							fn.caratula(pagIni, pagFin, stripper, doc));
					modelo = datosQualitasAutos.procesar();
				} else {
					qualitasAutosModel datosQualitasAutos = new qualitasAutosModel(fn.caratula(1, 2, stripper, doc));
					modelo = datosQualitasAutos.procesar();
				}

			} else {
				modelo.setError(QualitasModel.this.getClass().getTypeName() + " | " + "No se pudo leer");

			}
			return modelo;
		} catch (Exception ex) {
			modelo.setError(QualitasModel.this.getClass().getTypeName() + " - catch:" + ex.getMessage() + " | "
					+ ex.getCause());
			;
			return modelo;
		}
	}

}
