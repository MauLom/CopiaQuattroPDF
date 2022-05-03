package com.copsis.models.sura;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraJsonModel;

public class SuraModel {
	// Clases
	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
	// Variables
	private PDFTextStripper stripper;
	private PDDocument doc;
	private String contenido;
	
	// Constructor
	public SuraModel(PDFTextStripper pdfStripper, PDDocument pdDoc, String contenido) {
		this.stripper = pdfStripper;
		this.doc = pdDoc;
		this.contenido = contenido;
	}
	
	public EstructuraJsonModel procesar() {
		try {
		 
			if(contenido.contains("RECORDATORIO DE PAGO")) {
				contenido = fn.caratula(2, 3, stripper, doc);
			}
			
			int tipo = fn.tipoPoliza(contenido);
			
		
		
			if(tipo == 1 &&  contenido.contains("MEDIC ")) {
				tipo =2;
			}
			


			switch ((tipo == 0 ? fn.tipoPoliza(contenido) : tipo )) {
			case 1://Autos
					int pagFin = fn.pagFinRango(stripper, doc, "Nombre Agente");
					
					if(pagFin == 0) {
						pagFin = 3;
					}
					modelo  = new SuraAutosModel(fn.caratula(1, pagFin, stripper, doc)).procesar();					
				break;
			case 2://Salud
				 if(fn.caratula(1, 1, stripper, doc).contains("Desde las")) {
					 
				 }else {
						modelo  = new SuraSaludBModel(fn.caratula(1, 3, stripper, doc)).procesar();
				 }
							
			break;
			
			case 4://Diversos
				modelo  = new SuraDiversosModel(fn.caratula(2, 3, stripper, doc)).procesar();
				break;
			default:
				break;

			}

			return modelo;
		} catch (Exception ex) {
			modelo.setError(
					SuraModel.this.getClass().getTypeName() + " | " + ex.getMessage() + " | " + ex.getCause());
			return modelo;
		}
		
	}
	

}
