package com.copsis.models.latino;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraJsonModel;

public class LatinoSeguroModel {

	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();

	private PDFTextStripper stripper;
	private PDDocument doc;
	private String contenido;

	public LatinoSeguroModel(PDFTextStripper stripper, PDDocument doc, String contenido) {
		this.stripper = stripper;
		this.doc = doc;
		this.contenido = contenido;
	}

	public EstructuraJsonModel procesar() {
		try {
	

			int tipo = fn.tipoPoliza(contenido);
			int salud=0;
			if (tipo == 0 && fn.caratula(2, 3, stripper, doc).contains("GM IND MEDICA")) {
				tipo = 2;
				salud=1;
			}
			if (tipo == 0 && fn.caratula(4, 5, stripper, doc).contains("GM IND MEDICA")) {
				tipo = 2;
			}
			System.out.println(fn.caratula(2, 3, stripper, doc));
			
			switch (tipo) {
				case 1:
					modelo = new LatinoSeguroAutoModel(fn.caratula(1, 2, stripper, doc)).procesar();
					break;

				case 2:
				System.out.println(salud);
				      if(salud==1){
						modelo = new LatinoSeguroSaludModel().procesar(fn.caratula(2,3, stripper, doc));
					  }else{
						modelo = new LatinoSeguroSaludModel().procesar(fn.caratula(5,7, stripper, doc));
					  }
					break;
				default:
					break;
			}
			return modelo;
		} catch (Exception ex) {
			modelo.setError(
					LatinoSeguroModel.this.getClass().getTypeName() + " | " + ex.getMessage() + " | " + ex.getCause());
			return modelo;
		}

	}

}
