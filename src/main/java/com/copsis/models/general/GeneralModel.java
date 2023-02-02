package com.copsis.models.general;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraJsonModel;


public class GeneralModel {
	
	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
	private String contenido;
	
	public EstructuraJsonModel procesar(PDFTextStripper pdfStripper, PDDocument pdDoc, String contenido) {
		try {	

			int tipo = fn.tipoPoliza(contenido);
			if(contenido.contains("PÓLIZA INDIVIDUAL / FAMILIAR")) {
				tipo =2;
			}
			if(contenido.contains("MARÍTIMO Y TRANSPORTES-CARGA ESPECÍFICA")) {
				tipo =4;
			}

			if(contenido.contains("PAQUETE EMPRESARIAL")) {
				tipo =4;
			}
			
			 switch (tipo) {
			 case 1:
				 modelo = new GeneralAutosModel().procesar(fn.caratula(1, 3, pdfStripper, pdDoc));	
				 break;
			 case 2:
				 modelo = new GeneralSaludModel().procesar(fn.caratula(1, 3, pdfStripper, pdDoc));	
				 break;
		     case 4:
				if(contenido.contains("PAQUETE EMPRESARIAL")) {
					modelo = new GeneralDiversosAModel().procesar(fn.caratula(1, 3, pdfStripper, pdDoc));	
				}else{
					modelo = new GeneralDiversosModel().procesar(fn.caratula(1, 3, pdfStripper, pdDoc));	
				}
			
				 break;	 
			 }
					
			return modelo;
		} catch (Exception ex) {
			modelo.setError(GeneralModel.this.getClass().getTypeName() + " | " + ex.getMessage() + " | " + ex.getCause());
			return modelo;
		}
	}
}
