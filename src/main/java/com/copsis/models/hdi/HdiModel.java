package com.copsis.models.hdi;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraJsonModel;

public class HdiModel {
	// Clases
	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
	// Variables
	private PDFTextStripper stripper;
	private PDDocument doc;
	private String contenido;

	public HdiModel(PDFTextStripper pdfStripper, PDDocument pdDoc, String contenido) {
		this.stripper = pdfStripper;
		this.doc = pdDoc;
		this.contenido = contenido;
	}

	public EstructuraJsonModel procesar() {
		try {
	

			int tipo = fn.tipoPoliza(fn.caratula(1, 2, stripper, doc));

			if(tipo==0&& fn.caratula(1, 2, stripper, doc).contains("AUTO TOURIST INSURANCE POLICY") 
			&& fn.caratula(1, 2, stripper, doc).contains("Plates:")){
                tipo=1;
			}
		
			if(contenido.contains("Giro:")) {
			    tipo=4;
			}
			
		
		
;
		
			switch (tipo) {
			case 1:
			
				if(contenido.contains("AUTOS TURISTAS") || contenido.contains("VEHICULOS RESIDENTES GLM PLUS")) {
				
				   modelo = new HdiAutosBModel().procesar(fn.caratula(1, 3, stripper, doc));

				} else if(fn.caratula(1, 2, stripper, doc).contains("AUTO TOURIST INSURANCE POLICY")){
                      modelo = new HdiAutosUsaModel().procesar(fn.caratula(1, 1, stripper, doc));
				}
				else {
					
					if(fn.caratula(1, 1, stripper, doc).contains("FORMATO DE PAGO")) {
						modelo = new HdiAutosModel(fn.caratula(2, 3, stripper, doc)).procesar();
					}else {
						modelo = new HdiAutosModel(fn.caratula(1, 3, stripper, doc)).procesar();
					}
										
				}				
				break;
			case 2:
			    modelo=  new HdiSaludModel().procesar(fn.caratula(1, 3, stripper, doc));
			break;					
			case 4:
				modelo = new HdiDiversosModel(fn.caratula(1, 6, stripper, doc)).procesar();
				break;	

			default:
				break;
			}
		
			return modelo;
		} catch (Exception ex) {
			modelo.setError(HdiModel.this.getClass().getTypeName() + " | " + ex.getMessage() + " | " + ex.getCause());
			return modelo;
		}

	}

}
