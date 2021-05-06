package com.copsis.models.sisnova;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraJsonModel;

public class SisnovaModel {
			// Clases
			private DataToolsModel fn = new DataToolsModel();
			private EstructuraJsonModel modelo = new EstructuraJsonModel();
			// Variables
			private PDFTextStripper stripper;
			private PDDocument doc;
			private String contenido;

			// Constructor
			public SisnovaModel(PDFTextStripper pdfStripper, PDDocument pdDoc, String contenido) {
				this.stripper = pdfStripper;
				this.doc = pdDoc;
				this.contenido = contenido;
			}
			
			public EstructuraJsonModel procesar() {
				try {		
					switch (fn.tipoPoliza(contenido)) {				
					case 2:// Salud
						modelo  = new SisnovaSaludModel(fn.caratula(1, 3, stripper, doc)).procesar();					
						break;

					}

					return modelo;
				} catch (Exception ex) {
					modelo.setError(
							SisnovaModel.this.getClass().getTypeName() + " | " + ex.getMessage() + " | " + ex.getCause());
					return modelo;
				}
				
			}
			
}
