package com.copsis.models.bexmas;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraJsonModel;

public class BexmasModel {
	// Clases
	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
	// Variables
	private PDFTextStripper stripper;
	private PDDocument doc;
	private String contenido;


	public BexmasModel(PDFTextStripper pdfStripper, PDDocument pdDoc, String contenido) {
		this.stripper = pdfStripper;
		this.doc = pdDoc;
		this.contenido = contenido.replace("MÉDICOS MAYORES", "GASTOS MÉDICOS MAYORES");
	}
	public EstructuraJsonModel procesar() {
		try {

			switch (fn.tipoPoliza(contenido)) {
			case 1:
				 modelo =  new BexmasAutosModel(fn.caratula(0, 3, stripper, doc)).procesar();
				break;
			case 2:
				if(fn.caratula(0, 3, stripper, doc).contains("Individual/Familiar") && fn.caratula(0, 3, stripper, doc).contains("Plan:")) {
					 modelo =  new BexmasSaludBModel(fn.caratula(0, 3, stripper, doc)).procesar();
				}else{
					 modelo =  new BexmasSaludModel(fn.caratula(0, 3, stripper, doc)).procesar();
				}
				
				break;
			case 4:
				modelo =  new BexmasDiversosModel().procesar(fn.caratula(0, 3, stripper, doc));
				break;
			case 5:
				modelo = new BexmasVidaModel().procesar(fn.caratula(0, 3, stripper, doc));
				break;
			default:
				break;
			}
			
		
			
			
			return modelo;
		} catch (Exception ex) {
			modelo.setError(
					BexmasModel.this.getClass().getTypeName() + " | " + ex.getMessage() + " | " + ex.getCause());
			return modelo;
		}
	}
}
