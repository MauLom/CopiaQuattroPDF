package com.copsis.models.general;

import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraJsonModel;

public class GeneralModel {

	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
	private String contenido;

	public EstructuraJsonModel procesar(PDFTextStripper pdfStripper, PDDocument pdDoc, String contenido) {
		try {
			
			int tipo = fn.tipoPoliza(contenido);
			if (contenido.contains("PÓLIZA INDIVIDUAL / FAMILIAR")) {
				tipo = 2;
			}
			if (contenido.contains("MARÍTIMO Y TRANSPORTES-CARGA ESPECÍFICA")) {
				tipo = 4;
			}

			if (contenido.contains("PAQUETE EMPRESARIAL")
					|| fn.caratula(1, 2, pdfStripper, pdDoc).contains("Maquinaria Segura")) {
				tipo = 4;
			}
			if (fn.caratula(2, 2, pdfStripper, pdDoc).contains("PÓLIZA DE AUTOMÓVILES")) {
				tipo = 1;

			}

			switch (tipo) {
				case 1:
					modelo = new GeneralAutosModel().procesar(fn.caratula(1, 3, pdfStripper, pdDoc));
					break;
				case 2:
					modelo = new GeneralSaludModel().procesar(fn.caratula(1, 3, pdfStripper, pdDoc));
					break;
				case 4:
					getIndentifica(pdfStripper, pdDoc, contenido);

					break;
				default:	
			}

			return modelo;
		} catch (Exception ex) {
			modelo.setError(
					GeneralModel.this.getClass().getTypeName() + " | " + ex.getMessage() + " | " + ex.getCause());
			return modelo;
		}
	}

	private void getIndentifica(PDFTextStripper pdfStripper, PDDocument pdDoc, String contenido) throws IOException {
		if (contenido.contains("PAQUETE EMPRESARIAL")) {
			modelo = new GeneralDiversosAModel().procesar(fn.caratula(1, 3, pdfStripper, pdDoc));
		} else if (fn.caratula(1, 2, pdfStripper, pdDoc).contains("Maquinaria Segura")) {
			modelo = new GneralDiversosBModel().procesar(fn.caratula(1, 3, pdfStripper, pdDoc));
		} else {
			modelo = new GeneralDiversosModel().procesar(fn.caratula(1, 3, pdfStripper, pdDoc));
		}
	}
}
