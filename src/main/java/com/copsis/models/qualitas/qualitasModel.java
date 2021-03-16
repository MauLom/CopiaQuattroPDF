package com.copsis.models.qualitas;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.json.JSONObject;

import com.copsis.services.IdentificaPolizaService;

public class qualitasModel {
	
	   private IdentificaPolizaService identifica = new IdentificaPolizaService();
	    private PDFTextStripper stripper;
	    private PDDocument doc;
	    private String contenido;

	    public qualitasModel(PDFTextStripper pdfStripper, PDDocument pdDoc, String contenido) {
	        this.stripper = pdfStripper;
	        this.doc = pdDoc;
	        this.contenido = contenido;
	    }
	    private int pagIni = 0;
	    private int pagFin = 0;

	    public JSONObject procesa() {
	        JSONObject jsonObject = new JSONObject();
	        try {

	            if (contenido.contains("SEGURO DE AUTOMÓVILES") || contenido.contains("POLIZA VEHICULOS ")) {
	            	 
	                pagIni = identifica.pagFinRango(stripper, doc, "OFICINA DE");
	                pagFin = identifica.pagFinRango(stripper, doc, "IMPORTE TOTAL");
	               
	                if (pagIni < pagFin) {
	       
	                   qualitasAutosModel datosQualitasAutos = new qualitasAutosModel(identifica.caratula(pagIni, pagFin, stripper, doc));
	                    jsonObject = datosQualitasAutos.procesar();
	                } else {
	               	 
	                	qualitasAutosModel datosQualitasAutos = new qualitasAutosModel(identifica.caratula(1, 2, stripper, doc));
	                    jsonObject = datosQualitasAutos.procesar();
	                }
	            }
	            return jsonObject;
	        } catch (Exception ex) {
	            jsonObject.put("error", "DatosQualitas.procesa: " + ex.getMessage());
	            return jsonObject;
	        }
	    }

}
