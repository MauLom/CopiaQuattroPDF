package com.copsis.models.ana;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraJsonModel;

public class AnaModel {

	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();

	private PDFTextStripper stripper;
	private PDDocument doc;
	private String contenido;

	public AnaModel(PDFTextStripper stripper, PDDocument doc, String contenido) {
		super();
		this.stripper = stripper;
		this.doc = doc;
		this.contenido = contenido;
	}

	public EstructuraJsonModel procesar() {
		try {
			if (fn.tipoPoliza(contenido) == 1) {
				contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());
				if (!contenido.split("\r\n")[5].contains("ANA COMPAÑÍA")
						&& !contenido.split("\r\n")[5].contains("A.N.A. COMPAÑÍA")
						&& !contenido.split("\r\n")[4].contains("ANA COMPAÑÍA")) {
					modelo = new AnaAutosModelRoja(fn.caratula(1, 5, stripper, doc)).procesar();
				}
			}

			return modelo;
		} catch (Exception ex) {
			modelo.setError(AnaModel.this.getClass().getTypeName() + " | " + ex.getMessage() + " | " + ex.getCause());
			return modelo;
		}
	}

}
