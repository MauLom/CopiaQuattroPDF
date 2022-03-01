package com.copsis.models.aig;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraJsonModel;

public class AigModel {
	// Clases
	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
	// Variables
	private PDFTextStripper stripper;
	private PDDocument doc;
	private String contenido;

	// Constructor
	public AigModel(PDFTextStripper pdfStripper, PDDocument pdDoc, String contenido) {
		this.stripper = pdfStripper;
		this.doc = pdDoc;
		this.contenido = contenido;
	}
	public EstructuraJsonModel procesar() {
		try {
			System.out.println("Aqui ando ");
			int tipo = fn.tipoPoliza(contenido);
			System.out.println("Tipo?"+tipo);
			if(tipo == 0 && contenido.contains("TRA- TRANSPORTE COMBINADO")) {
				tipo = 4;
			}
			switch (tipo) {
			case 1:// Autos
				modelo  = new AigAutosModel(fn.caratula(1, 4, stripper, doc)).procesar();	
				break;
			case 2:// salud
			
				if(contenido.contains("Seguro de Accidentes Personales")) {
					modelo  = new AigSaludBModel(fn.caratula(1, 4, stripper, doc)).procesar();
				}else {
					modelo  = new AigSaludModel(fn.caratula(1, 4, stripper, doc)).procesar();
				}
				
				break;
			case 4:// diversos
				modelo  = new AigDiversosModel(fn.caratula(1, 4, stripper, doc)).procesar();	
				break;	
				default:
					break;
			}					
			return modelo;
		} catch (Exception ex) {
			modelo.setError(
					AigModel.this.getClass().getTypeName() + " | " + ex.getMessage() + " | " + ex.getCause());
			return modelo;
		}
	}

}
