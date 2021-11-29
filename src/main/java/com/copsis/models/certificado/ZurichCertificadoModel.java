package com.copsis.models.certificado;

import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import com.copsis.models.EstructuraJsonModel;

public class ZurichCertificadoModel {

	// Clases

	private EstructuraJsonModel modelo = new EstructuraJsonModel();
	// Variables
	private PDFTextStripper stripper;
	private PDDocument doc;
	private String contenido;

	public ZurichCertificadoModel(PDFTextStripper pdfStripper, PDDocument pdDoc, String contenido) {
		this.stripper = pdfStripper;
		this.doc = pdDoc;
		this.contenido = contenido;
	}

	public EstructuraJsonModel procesar() {
		try {

			if (contenido.contains("Detalle de Sumas Aseguradas")) {
				modelo = new ZurichAsegurados(textoBusqueda(stripper, doc, "Detalle de Sumas Aseguradas")).procesar();
			} else {

				modelo = new ZurichCertificadoGrupo(textoBusqueda(stripper, doc, "Certificado Individual")).procesar();
			}
			return modelo;
		} catch (Exception ex) {
			modelo.setError(ZurichCertificadoModel.this.getClass().getTypeName() + " | " + ex.getMessage() + " | "
					+ ex.getCause());
			return modelo;
		}

	}

	public String textoBusqueda(PDFTextStripper pdfStripper, PDDocument pdDoc, String buscar) throws IOException { 
		// BUSCA UNA PAGINA QUE CONTENGA LO BUSCADO
		StringBuilder x = new StringBuilder();
		for (int i = 1; i <= pdDoc.getPages().getCount(); i++) {
			pdfStripper.setStartPage(i);
			pdfStripper.setEndPage(i);

			if (pdfStripper.getText(pdDoc).contains(buscar)) {
				PDFTextStripper s = new PDFTextStripper();
				s = pdfStripper;
				s.setSortByPosition(true);
				s.setParagraphStart("@@@");
				s.setWordSeparator("###");
				x.append(s.getText(pdDoc));
			}
		}
		return x.toString();
	}

}
