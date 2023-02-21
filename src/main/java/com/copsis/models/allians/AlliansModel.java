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
		Boolean  vida =false;
			int op = fn.tipoPoliza(contenido);
			
			if(contenido.contains("TEMPORAL 1 AÑO RENOV") || contenido.contains("TEMPORAL 25")  ){
				op =5;
			}
			if(contenido.contains("Revaluables Renta Fija Especial")) {
				op=5;
				vida=true;
			}
			if(contenido.contains("BASICA POR FALLECIMIENTO")) {
				op=5;
			
			}
			if(contenido.contains("B E N E F I C I A R I O S")) {
				op=5;
				vida=true;
				}
				
	

			if(contenido.contains("Muerte Accidental")) {
				op=5;
				vida=true;
			}

			switch (op) {
				case 1:
                    modelo = new AlliansAutosModel().procesar(fn.caratula(0, 2, stripper, doc));
                    break;
			case 2:
				modelo = new AlliansSaludModel(fn.caratula(1, 2, stripper, doc)).procesar();
				break;
			case 5:
				if(vida ) {
					modelo = new AlliansVidaBModel().procesar(fn.caratula(0, 2, stripper, doc));
				}else {
					modelo = new AlliansVidaModel(fn.caratula(0, 2, stripper, doc)).procesar();	
				}
				
				break;
			case 4:
				
				if(fn.caratula(0, 4, stripper, doc).contains("Datos del Contratante")) {
					modelo = new AllianasDiversosBModel().procesar(fn.caratula(0, 7, stripper, doc));
				}else {
					modelo = new AlliansDiversosModel(fn.caratula(0, 4, stripper, doc)).procesar();
				}
			
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
