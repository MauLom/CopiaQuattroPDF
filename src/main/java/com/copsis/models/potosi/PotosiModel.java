package com.copsis.models.potosi;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraJsonModel;
import com.copsis.models.ana.AnaModel;

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
	public EstructuraJsonModel procesar() {
		try {
			
			System.out.println(fn.caratula(1, 2, stripper, doc));

		      switch (fn.tipoPoliza(contenido)) {
			case 1:
				modelo = new PotosiAutosModel(fn.caratula(1, 2, stripper, doc)).procesar();
				break;
			case 4:
				if(fn.caratula(1, 2, stripper, doc).contains("Número de Póliza")) {
					modelo = new PotosiDiversosModel(fn.caratula(1, 2, stripper, doc)).procesar();
				}else if(fn.caratula(1, 2, stripper, doc).contains("Número de Póliza")) {
					
				}
				else {
					modelo = new PotosiDiversosBModel(fn.caratula(1, 2, stripper, doc)).procesar();
				}
				
				break;	
			default:
				break;
			}
			return modelo;
		} catch (Exception ex) {
			modelo.setError(PotosiModel.this.getClass().getTypeName() + " | " + ex.getMessage() + " | " + ex.getCause());
			return modelo;
		}
		
	}

}
