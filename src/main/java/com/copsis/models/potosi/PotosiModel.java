package com.copsis.models.potosi;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraJsonModel;

public class PotosiModel {

	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();

	private PDFTextStripper stripper;
	private PDDocument doc;
	private String contenido;

	public PotosiModel(PDFTextStripper stripper, PDDocument doc, String contenido) {
		super();
		this.stripper = stripper;
		this.doc = doc;
		this.contenido = contenido;
	}

	// SE LE MODIFICO EL TIPO SE AÑADIO UN TIPO IGUALADO A 7
	public EstructuraJsonModel procesar() {
		try {

			int tipo = fn.tipoPoliza(contenido);
			if (tipo == 4 && (contenido.contains("SEGURO DE VIDA"))) {
				tipo = 5;
			}
			if (tipo == 2 && contenido.contains("VIDA INDIVIDUAL")) {
				tipo = 5;
			}
			if (tipo == 1 && fn.caratula(1, 2, stripper, doc).contains("RANSPORTE  DE CARGA")) {
				tipo = 4;
			}
			if (tipo == 4 && fn.caratula(0, 1, stripper, doc)
					.contains("SEGUROS DE RESPONSABILIDAD CIVIL GENERAL")) {
				tipo = 7;
			}
			// AGREGUE UN CASE 7 POR QUE ANTES NO LO TENIA
			switch (tipo == 0 ? fn.tipoPoliza(contenido) : tipo) {
				case 1:
					modelo = new PotosiAutosModel(fn.caratula(1, 2, stripper, doc)).procesar();
					break;
				case 4:
					if (fn.caratula(1, 2, stripper, doc).contains("Número de Póliza")) {
						if (fn.caratula(1, 2, stripper, doc).contains("SEGURO DE EMPRESA")) {
							modelo = new PotosiDiversosDModel().procesar(fn.caratula(1, 2, stripper, doc));
						} else {
							modelo = new PotosiDiversosModel(fn.caratula(1, 2, stripper, doc)).procesar();
						}
					} else if (fn.caratula(1, 2, stripper, doc).contains("SEGURO DE CASA")) {
						modelo = new PotosiDiversosCModel(fn.caratula(1, 2, stripper, doc)).procesar();
					} else {
						modelo = new PotosiDiversosBModel(fn.caratula(1, 2, stripper, doc)).procesar();
					}

					break;
				case 5:
					modelo = new PotosiVidaModel().procesar(fn.caratula(1, 3, stripper, doc));
					break;
				case 7:
					modelo = new PotosiDiversosDModel().procesar(fn.caratula(0, 1, stripper, doc));
				default:
					break;
			}
			return modelo;

		} catch (Exception ex) {
			modelo.setError(
					PotosiModel.this.getClass().getTypeName() + " | " + ex.getMessage() + " | " + ex.getCause());
			return modelo;
		}
	}

}
