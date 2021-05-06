package com.copsis.models.atlas;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraJsonModel;

public class AtlasModel {
	// Clases
		private DataToolsModel fn = new DataToolsModel();
		private EstructuraJsonModel modelo = new EstructuraJsonModel();
		// Variables
		private PDFTextStripper stripper;
		private PDDocument doc;
		private String contenido;

		// Constructor
		public AtlasModel(PDFTextStripper pdfStripper, PDDocument pdDoc, String contenido) {
			this.stripper = pdfStripper;
			this.doc = pdDoc;
			this.contenido = contenido;
		}
		
		public EstructuraJsonModel procesar() {
			try {
		
				
				switch (fn.tipoPoliza(contenido)) {

				case 1:// Autos
					modelo  = new AtlasAutosModel(fn.caratula(1, 2, stripper, doc),fn.textoBusqueda(stripper, doc, "RECIBO DE PAGO DE SEGURO DE AUTOS", false)).procesar();					
					break;
			
				}

				return modelo;
			} catch (Exception ex) {
				modelo.setError(
						AtlasModel.this.getClass().getTypeName() + " | " + ex.getMessage() + " | " + ex.getCause());
				return modelo;
			}
			
		}
}
