package com.copsis.models.qualitas;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;


import com.copsis.models.EstructuraJsonModel;
import com.copsis.services.IdentificaPolizaService;

public class QualitasModel {
	
	   private IdentificaPolizaService identifica = new IdentificaPolizaService();
	    private PDFTextStripper stripper;
	    private PDDocument doc;
	    private String contenido;
	 	 private EstructuraJsonModel modelo = new EstructuraJsonModel();
	    public QualitasModel(PDFTextStripper pdfStripper, PDDocument pdDoc, String contenido) {
	        this.stripper = pdfStripper;
	        this.doc = pdDoc;
	        this.contenido = contenido;
	    }
	    private int pagIni = 0;
	    private int pagFin = 0;

	    public EstructuraJsonModel procesa() {
	   
	        try {
	            if (contenido.contains("SEGURO DE AUTOMÓVILES") || contenido.contains("POLIZA VEHICULOS ")) {
	                pagIni = identifica.pagFinRango(stripper, doc, "OFICINA DE");
	                pagFin = identifica.pagFinRango(stripper, doc, "IMPORTE TOTAL");
	               
	                if (pagIni < pagFin) {
	                   qualitasAutosModel datosQualitasAutos = new qualitasAutosModel(identifica.caratula(pagIni, pagFin, stripper, doc));
	                   modelo = datosQualitasAutos.procesar();
	                } else {
	                	qualitasAutosModel datosQualitasAutos = new qualitasAutosModel(identifica.caratula(1, 2, stripper, doc));
	                	modelo = datosQualitasAutos.procesar();
	                }
	           
	        }else {
            	modelo.setError(QualitasModel.this.getClass().getTypeName() +" | "+ "No se pudo leer");
            	
            }
	            return modelo;
	        } catch (Exception ex) {
	        	modelo.setError(QualitasModel.this.getClass().getTypeName() +" - catch:" + ex.getMessage() + " | " + ex.getCause());;
	           return modelo;
	        }
	    }

}
