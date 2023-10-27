package com.copsis.models.bupa;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraJsonModel;

public class BupaModel {

	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
	
	private PDFTextStripper stripper;
	private PDDocument doc;
	private String contenido;
	
	public BupaModel(PDFTextStripper pdfStripper, PDDocument pdDoc, String contenido) {
		this.stripper = pdfStripper;		
		this.doc = pdDoc;
		this.contenido = contenido;
	}
	
	public EstructuraJsonModel procesar() {
		int  pagIni=0;
		int  pagFin=0;
		try {

			int tipo = fn.tipoPoliza(contenido);
			String[] palabras ={"Contratante","Derecho"};
             if(tipo ==0  && fn.pagPalabras(stripper, doc,palabras) !=0){				
				 pagIni = fn.pagPalabras(stripper, doc,palabras) ;		
				 pagFin=pagIni;		 						     
			     tipo = fn.tipoPoliza(fn.caratula(pagIni, pagFin, stripper, doc));
			 }


		    if(tipo == 0 && pagIni ==0) {
			   pagIni = fn.pagFinRango(stripper, doc, "Contratante");
			   pagFin = fn.pagFinRango(stripper, doc, "Advertencia");
			   if(pagIni > 0 && pagFin > 0 && pagFin >= pagIni) {				
			     contenido = fn.caratula(pagIni, pagFin, stripper, doc);
			   }
			   tipo = fn.tipoPoliza(contenido);
			 }
			 

			
		     if(tipo == 2){
 				if(pagIni > 0 && pagFin > 0 && pagFin >= pagIni) {		
					
					 modelo =new  BupaSaludModel().procesar(fn.caratula(pagIni, pagIni+1, stripper, doc),fn.caratula(pagIni, pagIni+5, stripper, doc),fn.textoBusqueda(stripper, doc, "Recibo para Pago", false));
				 }else {
					 modelo =new  BupaSaludModel().procesar(fn.caratula(0, 3, stripper, doc),"","");
				 }
			 }
				
				
		
			
			return modelo;
		} catch (Exception ex) {
			modelo.setError(
					BupaModel.this.getClass().getTypeName() + " | " + ex.getMessage() + " | " + ex.getCause());
			return modelo;
		}
	}
}
