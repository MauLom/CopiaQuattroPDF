package com.copsis.models.hdi;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraJsonModel;
import com.copsis.models.banorte.BanorteAutosModel;
import com.copsis.models.banorte.BanorteDiversos;
import com.copsis.models.banorte.BanorteModel;
import com.copsis.models.banorte.BanorteSaludModel;
import com.copsis.models.banorte.BanorteVidaModel;
import com.copsis.models.sisnova.SisnovaSaludModel;

public class HdiModel {
	// Clases
		private DataToolsModel fn = new DataToolsModel();
		private EstructuraJsonModel modelo = new EstructuraJsonModel();
		// Variables
		private PDFTextStripper stripper;
		private PDDocument doc;
		private String contenido;
		private Integer pagIni =0;
		private Integer pagFin =0;
		
		public HdiModel(PDFTextStripper pdfStripper, PDDocument pdDoc, String contenido) {
			this.stripper = pdfStripper;
			this.doc = pdDoc;
			this.contenido = contenido;
		}
		public EstructuraJsonModel procesar() {
		
			try {

				switch (fn.tipoPoliza(contenido)) {
				case 1://Autos
					modelo  = new HdiAutosModel(fn.caratula(1, 3, stripper, doc)).procesar();	
								   
					break;
			

				default:
					break;
				}
				
				return modelo;
			} catch (Exception ex) {
				modelo.setError(
						HdiModel.this.getClass().getTypeName() + " | " + ex.getMessage() + " | " + ex.getCause());
				return modelo;
			}
			
		}
		
}
