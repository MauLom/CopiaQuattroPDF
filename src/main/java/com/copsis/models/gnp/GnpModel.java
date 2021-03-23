package com.copsis.models.gnp;

import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraJsonModel;
import com.copsis.models.gnp.autos.GnpAutos2Model;
import com.copsis.models.gnp.autos.GnpAutosModel;

public class GnpModel {
	// Clases
	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
	// Variables
	private PDFTextStripper stripper;
	private PDDocument doc;
	private String contenido;
	private int pagFin = 0;

	// Constructor
	public GnpModel(PDFTextStripper pdfStripper, PDDocument pdDoc, String contenido) {
		this.stripper = pdfStripper;
		this.doc = pdDoc;
		this.contenido = contenido;
	}

	public EstructuraJsonModel procesa() {

		try {

			if (contenido.contains("Motor") && contenido.contains("Placas")) {
				if (contenido.contains("DESGLOSE DE COBERTURAS Y SERVICIOS")) { // AUTOS NARANJA AZUL
					pagFin = fn.pagFinRango(stripper, doc, "AGENTE");
					if (pagFin > 0) {
						modelo = new GnpAutosModel(fn.caratula(1, pagFin, stripper, doc)).procesar();
					}
				} else {// AUTOS AZUL
					pagFin = fn.pagFinRango(stripper, doc, "Clave");
					if (pagFin > 0) {
						modelo = new GnpAutos2Model(fn.caratula(1, pagFin, stripper, doc)).procesar();
					}
				}
			} // termina el codigo de Autos
			else if (contenido.contains("Póliza de Seguro Gastos Médicos")) {
				pagFin = fn.pagFinRango(stripper, doc, "Clave");
				if (pagFin > 0) {
					modelo = new GnpSaludModel(fn.caratula(1, pagFin, stripper, doc),
							aseguradosCertificaSalud(stripper, doc, "CERTIFICADO DE COBERTURA POR ASEGURADO", false),
							aseguradosCertificaSalud(stripper, doc, "Asegurado (s)", true)).procesar();
				}
			} // termina el codigo de salud
			else if (contenido.contains("Póliza de Seguro de Vida")) {
				contenido = "";
				modelo = new GnpVidaModel(fn.caratula(1, 4, stripper, doc)).procesar();
			} // termina el codigo de vida
			 else if (contenido.contains("Coberturas Amparadas") && contenido.contains("Ubicación de los bienes asegurados")
	                    || contenido.contains("Secciones Contratadas") && contenido.contains("Póliza de Seguro de Daños ")
	                    || contenido.contains("secciones contratadas") && contenido.contains("Daños")) {
				 System.out.println("entro aqui");
				 pagFin = fn.pagFinRango(stripper, doc, "Clave");
	                if (pagFin > 0) {
	                	modelo = new GnpDiversosModel(fn.caratula(1, pagFin, stripper, doc), aseguradosCertificaSalud(stripper, doc, "Características del Riesgo",false),1).procesar();
	                }
			 }else if (contenido.contains("Póliza de Seguro de Daños")) {
				 modelo = new GnpDiversosModel(fn.caratula(1, 8, stripper, doc), aseguradosCertificaSalud(stripper, doc, "Características del Riesgo",false),2).procesar();
             
			 }
			return modelo;
		} catch (Exception ex) {
			modelo.setError(
					GnpModel.this.getClass().getTypeName() + " - catch:" + ex.getMessage() + " | " + ex.getCause());
			return modelo;
		}
	}

	public String aseguradosCertificaSalud(PDFTextStripper pdfStripper, PDDocument pdDoc, String buscar, Boolean tipo)
			throws IOException { // BUSCA UNA PAGINA QUE CONTENGA LO BUSCADO
		String x = "";
		int listado = 0;

		for (int i = 1; i <= pdDoc.getPages().getCount(); i++) {
			pdfStripper.setStartPage(i);
			pdfStripper.setEndPage(i);
			if (pdfStripper.getText(pdDoc).contains(buscar)) {
				if (tipo) {// asegurados
					listado++;
					if (listado == 2) {
						PDFTextStripper s = new PDFTextStripper();
						s.setParagraphStart("###");
						s.setSortByPosition(true);
						s = pdfStripper;
						x += s.getText(pdDoc);
						break;
					}
				} else {// certificado
					PDFTextStripper s = new PDFTextStripper();
					s.setParagraphStart("###");
					s.setSortByPosition(true);
					s = pdfStripper;
					x += s.getText(pdDoc);

				}
			}

		}
		return x;
	}

}
