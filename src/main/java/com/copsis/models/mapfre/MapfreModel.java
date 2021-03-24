package com.copsis.models.mapfre;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraJsonModel;
import com.copsis.models.mapfre.autos.MapfreAutosModel;

public class MapfreModel {
	// Clases
	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
	// Variables
	private PDFTextStripper stripper;
	private PDDocument doc;
	private String contenido;
	private int pagFin = 0;

	// Constructor
	public MapfreModel(PDFTextStripper pdfStripper, PDDocument pdDoc, String contenido) {
		this.stripper = pdfStripper;
		this.doc = pdDoc;
		this.contenido = contenido;
	}
	
	public EstructuraJsonModel procesa() {

		try {

			  if (contenido.contains("AUTOMOVILES") || contenido.contains(" MOTOCICLETAS") || contenido.contains("SEGURO AUTOPLUS")) {	              
	                if (contenido.contains("TIPO DE DOCUMENTO:") && contenido.contains("Nombre del agente:")) {
	                    pagFin = fn.pagFinRango(stripper, doc, "Coberturas Amparadas");
	                    if (pagFin > 0) {	                    
	                       // model = new DatosMapfreAutos2(identifica.caratula(1, pagFin, stripper, doc), identifica.recibos(stripper, doc, "Serie de recibo:")).procesar();
	                   
	                    	modelo = new MapfreAutosModel(fn.caratula(1, pagFin, stripper, doc),fn.textoBusqueda(stripper, doc, "Serie de recibo:", false) ).procesar();
	                    }
	                } else {
	                    pagFin = fn.pagFinRango(stripper, doc, "INFORMACIÓN ADICIONAL");
	                    if (pagFin > 0) {
	                    	modelo = new MapfreAutosModel(fn.caratula(1, pagFin, stripper, doc),"").procesar();
	                    }
	                }
	            }

			return modelo;
		} catch (Exception ex) {
			modelo.setError(
					MapfreModel.this.getClass().getTypeName() + " - catch:" + ex.getMessage() + " | " + ex.getCause());
			
			return modelo;
		}
	}

}
