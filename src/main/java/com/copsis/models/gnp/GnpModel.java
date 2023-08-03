package com.copsis.models.gnp;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import com.copsis.constants.ConstantsValue;
import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraJsonModel;
import com.copsis.models.general.GeneralDiversosAModel;
import com.copsis.models.gnp.autos.GnpAutos2Model;
import com.copsis.models.gnp.autos.GnpAutos3Model;
import com.copsis.models.gnp.autos.GnpAutosModel;


public class GnpModel {
	// Clases
	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
	// Variables
	private PDFTextStripper stripper;
	private PDDocument doc;
	private String contenido;

	// Constructor
	public GnpModel(PDFTextStripper pdfStripper, PDDocument pdDoc, String contenido) {
		this.stripper = pdfStripper;
		this.doc = pdDoc;
		this.contenido = contenido;
	}

	public EstructuraJsonModel procesa() {
		int pagFin = 0;

		try {

			if (contenido.contains("Motor") && contenido.contains("Placas") || fn.caratula(1, 1, stripper, doc).contains("#MOTOR INSURANCE") && contenido.contains("License Plate")) {
		
				if (contenido.contains("DESGLOSE DE COBERTURAS Y SERVICIOS")) { // AUTOS NARANJA AZUL
					
					pagFin = fn.pagFinRango(stripper, doc, "Artículo 25");

					pagFin = pagFin == -1 ? pagFin = fn.pagFinRango(stripper, doc, "AGENTE"):pagFin;
			       
					if (pagFin > 0) {
						modelo = new GnpAutosModel(fn.caratula(1, pagFin, stripper, doc)).procesar();
					}

				}else if( fn.caratula(1, 1, stripper, doc).contains("MOTOR INSURANCE") && contenido.contains("License Plate")) {//Caractula en Ingles
					modelo = new GnpAutos3Model(fn.caratula(1, 4, stripper, doc)).procesar();
				}				
				else {// AUTOS AZUL

					pagFin = fn.pagFinRango(stripper, doc, ConstantsValue.CLAVE2);
					if (pagFin > 0) {
						modelo = new GnpAutos2Model(fn.caratula(1, pagFin, stripper, doc)).procesar();
					}
				}
			} // termina el codigo de Autos
			else if (contenido.contains("Póliza de Seguro Gastos Médicos") || contenido.contains("Seguro Médico GNP Indemniza")) {
				pagFin = fn.pagFinRango(stripper, doc, ConstantsValue.CLAVE2);
				if( contenido.contains("Seguro Médico GNP Indemniza")){
					modelo = new GnpSaludBModel().procesar(fn.caratula(1, pagFin, stripper, doc));
				}else if (pagFin > 0) {
					
					String x = fn.textoBusqueda(stripper, doc, "Asegurado (s)", true);
					x = x.length() > 0 ? x :fn.textoBusqueda(stripper, doc, "Asegurado (s)", false);
					System.out.println(x);
					modelo = new GnpSaludModel(fn.caratula(1, pagFin, stripper, doc),
							fn.textoBusqueda(stripper, doc, "CERTIFICADO DE COBERTURA POR ASEGURADO", false),
							x).procesar();
				}
			} // termina el codigo de salud
			else if (contenido.contains("Póliza de Seguro de Vida") || contenido.contains("Seguro de Vida") || contenido.contains("Seguro de Vida") ) {

			 if (!contenido.contains("Póliza de Seguro de Daños") || !contenido.contains("Daños")) {
				if(contenido.contains("Seguro de Vida")) {
					modelo = new GnpVIdaModel2(fn.caratula(1, 5, stripper, doc)).procesar();	
				}else {
					modelo = new GnpVidaModel3(fn.caratula(1, 5, stripper, doc)).procesar();
				}
			 }
			} // termina el codigo de vida
			else if (contenido.contains("Coberturas Amparadas") && contenido.contains("Ubicación de los bienes asegurados") || contenido.contains("Secciones Contratadas") && contenido.contains("Póliza de Seguro de Daños ")
					|| contenido.contains("secciones contratadas") && contenido.contains("Daños")) {
				pagFin = fn.pagFinRango(stripper, doc, ConstantsValue.CLAVE2);
				if (pagFin > 0) {
					modelo = new GnpDiversosModel(fn.caratula(1, pagFin, stripper, doc),
							fn.textoBusqueda(stripper, doc, "Características del Riesgo", false), 1).procesar();
				}
			} else if (contenido.contains("Póliza de Seguro de Daños") || contenido.contains("Daños")
			) {

				if(contenido.contains("Póliza Multiple")){
					modelo = new GnpDiversosBModelo(fn.caratula(1, 12, stripper, doc),
							fn.textoBusqueda(stripper, doc, "Características del Riesgo", false)														
							).procesar();

				}else {
					modelo = new GnpDiversosModel(fn.caratula(1, 8, stripper, doc),
							fn.textoBusqueda(stripper, doc, "Características del Riesgo", false), 2).procesar();

				}
				
			}
	
			//|| contenido.contains("ESPECIFICACIÓN DEL GIRO")
			//proceso para cuando no se pudo  indentificar ,aplica para pdfs de Gnp SEGURO HOGAR VERSATIL
			if(modelo.getTipo() == 0 && fn.caratula(1, 5, stripper, doc).contains("CARACTERÍSTICAS###DEL###RIESGO")
			|| modelo.getTipo() == 0 && fn.caratula(1, 5, stripper, doc).contains("Especificaciones###del###Giro")
			|| modelo.getTipo() == 0 && contenido.contains("ESPECIFICACIÓN DEL GIRO")) {	
				modelo = new GnpDiversosCModelo(fn.caratula(1, 12, stripper, doc)).procesar();			
			}
			if(fn.caratula(3,3, stripper, doc).contains("CARACTERÍSTICAS###DE###LA###MASCOTA")){
				modelo = new GnpDiversosDModelo().procesar(fn.caratula(3,4, stripper, doc));
			}
			
	
			
			if(modelo.getTipo() == 0 && (fn.caratula(1, 2, stripper, doc).contains("SEGURO DE VIDA") || fn.caratula(1, 2, stripper, doc).contains("SEGURIDAD EN VIDA"))) {
				modelo = new GnpVidaModel4().procesar(fn.caratula(1, 12, stripper, doc));
			}
			
			
			return modelo;
		} catch (Exception ex) {
		
			modelo.setError(
					GnpModel.this.getClass().getTypeName() + " - catch:" + ex.getMessage() + " | " + ex.getCause());
			return modelo;
		}
	}

}
