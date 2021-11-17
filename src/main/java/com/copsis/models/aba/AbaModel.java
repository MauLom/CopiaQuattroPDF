package com.copsis.models.aba;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraJsonModel;

import lombok.Data;

@Data
public class AbaModel {
	// Variables
	PDFTextStripper pdfStripper;
	PDDocument pdDoc;
	String contenido;
	


	public EstructuraJsonModel procesa() {
		EstructuraJsonModel modelo = new EstructuraJsonModel();
		DataToolsModel fn = new DataToolsModel();
        try {
        	
        	if(contenido.contains("Motor")) {
        		AbaAutosModel datosAbaAutos = new AbaAutosModel(fn.caratula(1, 1, pdfStripper, pdDoc));
        		modelo = datosAbaAutos.procesar();
        	}
        
        	if(contenido.contains("Flexible óptimo") || contenido.contains("Vivienda") || contenido.contains("EMPRESARIAL") || contenido.contains("SEGURO DE CASA") ) {
        		AbaDiversosModel datosAbaDiversos = new AbaDiversosModel(fn.caratula(1, 8, pdfStripper, pdDoc));
        		modelo = datosAbaDiversos.procesar();
        	}
        	
        	return modelo;
		} catch (Exception ex) {
			modelo.setError(
					AbaModel.this.getClass().getTypeName() + " - catch:" + ex.getMessage() + " | " + ex.getCause());
			return modelo;
		}
		
	
	}
}
