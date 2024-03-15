package com.copsis.models.segurosmty;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraJsonModel;

	
public class SegurosMtyModel {
	// Clases
	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
	// Variables
	private PDFTextStripper stripper;
	private PDDocument doc;
	private String contenido;
	

	public SegurosMtyModel(PDFTextStripper pdfStripper, PDDocument pdDoc, String contenido) {
		this.stripper = pdfStripper;
		this.doc = pdDoc;
		this.contenido = contenido;
	}
	
	public EstructuraJsonModel procesa() {
		try {
		
			int tipo =0;
			int tipoSalud =0;
			if(fn.caratula(1, 2, stripper, doc).contains("ALFA MEDICAL FLEX")) {
			    tipo = 2;
			}
			if(tipo == 0 && fn.caratula(1, 2, stripper, doc).contains("ALFA MEDICAL") 
			&&( fn.caratula(1, 2, stripper, doc).contains("GASTOS MÉDICOS")  
			|| fn.caratula(1, 2, stripper, doc).contains( "GASTOS ###MÉDICOS"))
			|| fn.caratula(3, 3, stripper, doc).contains( "GASTOS MÉDICOS")
			|| fn.caratula(3, 3, stripper, doc).contains("GASTOS ###MÉDICOS")
			) {
			    tipo = 2;
			}
			if(tipo == 0 && fn.caratula(1, 3, stripper, doc).toUpperCase().contains("ALFA MEDICAL") 
			&& fn.caratula(1, 3, stripper, doc).toUpperCase().contains("GASTOS MÉDICOS")  ){
			tipo = 2;
			}
			if(tipo == 0 && (fn.caratula(1, 2, stripper, doc).contains("VIDA INDIVIDUAL") || fn.caratula(1, 3, stripper, doc).contains("PÓLIZA DE SEGURO DE VIDA"))) {
			    tipo = 5;
			}
		
			if(tipo == 0 && fn.caratula(1, 2, stripper, doc).contains("ACCIDENTES PERSONALES")){
				tipo=2;
				tipoSalud=1;

			}
			
			
	
		switch (tipo) {
			case 2:// Salud
			    if(tipoSalud== 1){
					modelo  = new SegurosMtySalud2().procesar(fn.caratula(1, 5, stripper, doc));	
				}else{
					modelo  = new SegurosMtySalud(fn.caratula(1, 5, stripper, doc)).procesar();	
				}
				
				break;
			case 5:// Vida
				modelo  = new SegurosMtyVida(fn.caratula(1, 6, stripper, doc)).procesar();				
				break;	
				default:
				
			}
			return modelo;
		} catch (Exception ex) {
			modelo.setError(
					SegurosMtyModel.this.getClass().getTypeName() + " | " + ex.getMessage() + " | " + ex.getCause());
			return modelo;
		}
		
	}

	
}
