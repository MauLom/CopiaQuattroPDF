package com.copsis.models.aba;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraJsonModel;
import com.copsis.models.axa.AxaAutosModel;
import com.copsis.models.chubb.ChubbModel;

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
        		AbaDiversosModel datosAbaDiversos = new AbaDiversosModel(fn.caratula(1, 1, pdfStripper, pdDoc));
        		modelo = datosAbaDiversos.procesar();
        	}
        	
        	
        	
        	
//        	if(contenido.contains("Motor")) {
//    			jsonObject = new DatosAbaAutos(identifica.caratula(1, 1, stripper, doc), identifica.recibos(stripper, doc, "AVISO DE COBRO")).procesar();
//    		}else if(contenido.contains("EL SEGURO DE CASA") || contenido.contains("Flexible Premium") 
//    		|| contenido.contains("Flexible óptimo") || contenido.contains("Vivienda:") || contenido.contains("EMPRESARIAL")) {
//    			pagFin = identifica.pagFinRango(stripper, doc, "Conoce más sobre");
//    			if(pagFin > 0) jsonObject = new DatosAbaDanos(identifica.caratula(1, pagFin, stripper, doc), identifica.recibos(stripper, doc, "AVISO DE COBRO")).procesar();
//    		}
//        			
        	return modelo;
		} catch (Exception ex) {
			modelo.setError(
					AbaModel.this.getClass().getTypeName() + " - catch:" + ex.getMessage() + " | " + ex.getCause());
			return modelo;
		}
		
	
	}
}
