package com.copsis.models.mapfre;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraJsonModel;
import com.copsis.models.mapfre.autos.MapfreAutosBModel;
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
			switch (fn.tipoPoliza(contenido)) {
			case 1:// Autos

				if (contenido.contains("CONDUCTOR HABITUAL")) {
					modelo = new MapfreAutosBModel(fn.caratula(1, 3, stripper, doc)).procesar();
				} else {
					pagFin = fn.pagFinRango(stripper, doc, "Coberturas Amparadas");
					modelo = new MapfreAutosModel(fn.caratula(1, pagFin, stripper, doc),
							fn.textoBusqueda(stripper, doc, "Serie de recibo:", false)).procesar();
				}
				break;
			}
			
			
			
			
//			if (contenido.contains("AUTOMOVILES") || contenido.contains(" MOTOCICLETAS")
//					|| contenido.contains("SEGURO AUTOPLUS")) {
//				if (contenido.contains("TIPO DE DOCUMENTO:") && contenido.contains("Nombre del agente:")) {
//					pagFin = fn.pagFinRango(stripper, doc, "Coberturas Amparadas");
//					if (pagFin > 0) {
//						
//						if (contenido.contains("CONDUCTOR HABITUAL")) {
//							System.out.println("paso 1");
//							modelo = new MapfreAutosBModel(fn.caratula(1, 3, stripper, doc)).procesar();
//						} else {
//							modelo = new MapfreAutosModel(fn.caratula(1, pagFin, stripper, doc),fn.textoBusqueda(stripper, doc, "Serie de recibo:", false)).procesar();
//						}
//
//					}
//				} else {
//
//					pagFin = fn.pagFinRango(stripper, doc, "INFORMACIÓN ADICIONAL");
//					if (pagFin > 0) {
//						modelo = new MapfreAutosModel(fn.caratula(1, pagFin, stripper, doc), "").procesar();
//					}
//				}
//			}
//			if (contenido.contains("END CAMBIO DE AGENT") || contenido.contains("VIDA INDIVIDUAL")
//					|| contenido.contains("PREVICANCER") || contenido.contains("FUNERARIOS")
//					|| contenido.contains("SEGURO DE VIDA")) { // VIDA
//				modelo = new MapfreVidaModel(fn.caratula(1, 5, stripper, doc)).procesar();
//
//			}
//
//			if (contenido.contains("GASTOS MÉDICOS") || contenido.contains("PROTECCION MEDICA A TU MEDIDA")) {
//				modelo = new MapfreSaludModel(fn.caratula(1, 5, stripper, doc)).procesar();
//			}
//
//			if (contenido.contains("MULTIPLE EMPRESARIAL") || contenido.contains("SEGUPYME")
//					|| contenido.contains("HOGAR")) { // EMPRESARIAL
//
//				modelo = new MapfreDiversosModel(fn.caratula(1, 2, stripper, doc),
//						fn.textoBusqueda(stripper, doc, "Serie de recibo:", false),
//						fn.textoBusqueda(stripper, doc, "ESPECIFICACION DE BIENES CUBIERTOS PARA", false)).procesar();
//			}

			return modelo;
		} catch (Exception ex) {
			modelo.setError(
					MapfreModel.this.getClass().getTypeName() + " - catch:" + ex.getMessage() + " | " + ex.getCause());

			return modelo;
		}
	}

}
