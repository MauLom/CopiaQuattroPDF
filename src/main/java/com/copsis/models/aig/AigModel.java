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
			int tipo = fn.tipoPoliza(contenido);
			if(tipo == 0 && contenido.contains("TRA- TRANSPORTE COMBINADO")) {
				tipo = 4;
			}
			
			if(tipo == 5 && contenido.contains("NÚMERO DE UBICACIÓN")) {
				tipo = 4;
			}
			if(tipo == 0 && fn.caratula(1, 4, stripper, doc).contains("SEGURO DE AUTOMÓVILES RESIDENTES")) {
				tipo = 1;
			}

			if(tipo == 0 && fn.caratula(1, 4, stripper, doc).contains("NÚMERO DE UBICACIÓN")) {
				tipo = 4;
			}

		
			
			switch (tipo) {
			case 1:// Autos
				modelo  = new AigAutosModel().procesar(fn.caratula(1, 4, stripper, doc));
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
