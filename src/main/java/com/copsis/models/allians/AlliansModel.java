package com.copsis.models.allians;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraJsonModel;

public class AlliansModel {
	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();

	private PDFTextStripper stripper;
	private PDDocument doc;
	private String contenido;
	public AlliansModel(PDFTextStripper stripper, PDDocument doc, String contenido) {
		super();
		this.stripper = stripper;
		this.doc = doc;
		this.contenido = contenido;
	}
	
	public EstructuraJsonModel procesar() {
		try {
		
			int op = fn.tipoPoliza(contenido);
			if(contenido.contains("TEMPORAL 1 AÑO RENOV")){
				op =5;
			}
			
			
			switch (op) {
			case 2:
				modelo = new AlliansSaludModel(fn.caratula(1, 2, stripper, doc)).procesar();
				break;
			case 5:
				modelo = new AlliansVidaModel(fn.caratula(0, 2, stripper, doc)).procesar();
				break;
			case 4:
				modelo = new AlliansDiversosModel(fn.caratula(0, 4, stripper, doc)).procesar();
				break;	
			default:
				break;
			}
			
			return modelo;
		} catch (Exception ex) {
			modelo.setError(AlliansModel.this.getClass().getTypeName() + " | " + ex.getMessage() + " | " + ex.getCause());
			return modelo;
		}
	}
	
	

}
