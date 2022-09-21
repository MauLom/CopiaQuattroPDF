package com.copsis.models.afirme;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraJsonModel;

public class AfirmeModel {
	// Clases
	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
	// Variables
	private PDFTextStripper stripper;
	private PDDocument doc;
	private String contenido;
	private int tipo  = 0;
	private int tipoV  = 0;

	public AfirmeModel(PDFTextStripper pdfStripper, PDDocument pdDoc, String contenido) {
		this.stripper = pdfStripper;
		this.doc = pdDoc;
		this.contenido = contenido;
	}
	public EstructuraJsonModel procesar() {	
		try {
			tipo =fn.tipoPoliza(contenido);
			Integer pagIni = 0;

				if( tipo == 1 && fn.caratula(1, 2, stripper, doc).contains("SEGURO TRANSPORTES CARGA")){
					tipo=4;
					tipoV =1;
				}
			
				switch (tipo == 0 ? fn.tipoPoliza(contenido) : tipo) {
				case 1:
					if(contenido.contains("AUTOMÓVILES RESIDENTES") ||contenido.contains("AUTOMÓVILES SERVICIO PUBLICO")) {
						pagIni = fn.pagFinRango(stripper, doc, "CARATULA");
						modelo  = new AfirmeAutosBModel(fn.caratula(pagIni, pagIni+2, stripper, doc),fn.recibos(stripper, doc, "RECIBO DE PRIMAS")).procesar();
					}else {
						pagIni = fn.pagFinRango(stripper, doc, "DESGLOSE DE COBERTURAS");		
						modelo  = new AfirmeAutosModel(fn.caratula(pagIni, pagIni+2, stripper, doc)).procesar();	
					}
		
					break;
		
				case 4:
					if(tipoV == 0) {
						modelo  = new AfirmeDiversosModel(fn.caratula(1, 4, stripper, doc)).procesar();
					}else {
						modelo  = new AfirmeDiversosBModel(fn.caratula(1, 5, stripper, doc)).procesar();
					}
		
					break;
		
				default:
					break;
				}

		

			
			return modelo;
		} catch (Exception ex) {
			modelo.setError(
					AfirmeModel.this.getClass().getTypeName() + " | " + ex.getMessage() + " | " + ex.getCause());
			return modelo;
		}
		
	}

}
