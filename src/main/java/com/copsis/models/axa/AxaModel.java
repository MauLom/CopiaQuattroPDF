package com.copsis.models.axa;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraJsonModel;


public class AxaModel {
	// Clases
	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
	// Variables
	private PDFTextStripper stripper;
	private PDDocument doc;
	private String contenido;

	// Constructor
	public AxaModel(PDFTextStripper pdfStripper, PDDocument pdDoc, String contenido) {
		this.stripper = pdfStripper;
		this.doc = pdDoc;
		this.contenido = contenido;
	}
	
	public EstructuraJsonModel procesa() {
		try {
			if (contenido.contains("Datos del vehículo") && contenido.contains(" Vehicle description") == false) {	//AUTOS
				AxaAutosModel datosAxaAutos = new AxaAutosModel(fn.caratula(1, 1, stripper, doc), fn.textoBusqueda(stripper, doc, "RECIBO PROVISIONAL DE",false));
               modelo = datosAxaAutos.procesar();
            }else if (contenido.indexOf("Recibo provisional para pago de primas") > 0) {
            	AxaSaludModel datosAxaSalud = new AxaSaludModel(fn.caratula(1, 3, stripper, doc));
            	  modelo = datosAxaSalud.procesar();
            }
			
			else {
                String tipos[] = {"PAQUETE DE SEGURO EMPRESARIAL", "GASTOS M", "TRADICIONALES DE VIDA", "HOGAR INTEGRAL", " VEHICLE DESCRIPTION", "PROTECCIÓN A BIENES EMPRESARIALES", "PLANPROTEGE / COMERCIO"};
                contenido = contenido.toUpperCase();
                for (String tipo : tipos) {
                    if (contenido.contains(tipo)) {
                	  switch (tipo) {
                	  case "TRADICIONALES DE VIDA":	//VIDA
                		  AxaVidaModel datosAxaVida = new AxaVidaModel(fn.caratula(1, 3, stripper, doc));
                		  modelo = datosAxaVida.procesar();
                		  break;
                	   case "GASTOS M": //GASTOS MEDICOS
                          	AxaSaludModel datosAxaSalud = new AxaSaludModel(fn.caratula(1, 3, stripper, doc));
                          	 modelo = datosAxaSalud.procesar();
                		   break;
                	  
                	  }
                    }
                	
                }
            	
            }
			
			return modelo;
		} catch (Exception ex) {
			modelo.setError(
					AxaModel.this.getClass().getTypeName() + " - catch:" + ex.getMessage() + " | " + ex.getCause());
			return modelo;
		}
		
	}

}
