package com.copsis.models.sura;

import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import com.copsis.constants.ConstantsValue;
import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraJsonModel;

public class SuraModel {
	// Clases
	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
	// Variables
	private PDFTextStripper stripper;
	private PDDocument doc;
	private String contenido;

	// Constructor
	public SuraModel(PDFTextStripper pdfStripper, PDDocument pdDoc, String contenido) {
		this.stripper = pdfStripper;
		this.doc = pdDoc;
		this.contenido = contenido;
	}

	public EstructuraJsonModel procesar() {
		try {

			if (contenido.contains("RECORDATORIO DE PAGO")) {
				contenido = fn.caratula(2, 3, stripper, doc);
			}

			int tipo = fn.tipoPoliza(contenido);
			tipo = getTipoInic(tipo);		
			tipo= getTipo(fn.caratula(3, 3, stripper, doc), tipo);
			tipo = getTipou(tipo);

			switch ((tipo == 0 ? fn.tipoPoliza(contenido) : tipo)) {
				case 1:// Autos
					int pagFin = fn.pagFinRango(stripper, doc, "Nombre Agente");

					if (pagFin == 0) {
						pagFin = 3;
					}
					modelo = new SuraAutosModel(fn.caratula(1, pagFin, stripper, doc)).procesar();
					break;
				case 2:// Salud
					if (!fn.caratula(1, 1, stripper, doc).contains("Desde las")) {
						modelo = new SuraSaludBModel(fn.caratula(1, 3, stripper, doc)).procesar();
					}

					break;

				case 4:// Diversos
					if (contenido.contains("Hogar Máster Total") || contenido.contains(ConstantsValue.MUL_EMPRESARIA_DESCU)) {
						modelo = new SuraDiversosModel(fn.caratula(1, 3, stripper, doc)).procesar();
					} else if (contenido.contains("DAÑOS RESPONSABILIDAD")) {
						modelo = new SuraDiversos2Model().procesar(fn.caratula(3, 3, stripper, doc));
					} else {
						modelo = new SuraDiversosModel(fn.caratula(2, 3, stripper, doc)).procesar();
					}

					break;

				case 5:
					modelo = new SuraVidaModel().procesar(fn.caratula(2, 3, stripper, doc));
					break;
				default:
					break;

			}

			return modelo;
		} catch (Exception ex) {
			modelo.setError(
					SuraModel.this.getClass().getTypeName() + " | " + ex.getMessage() + " | " + ex.getCause());
			return modelo;
		}

	}

	private int getTipoInic(int tipo) {
		if (tipo == 5 && contenido.contains("Grupo Experiencia Global Sin Dividendos")) {
			tipo = 5;
		}

		if (tipo == 1 && contenido.contains("MEDIC ")) {
			tipo = 2;
		}
		return tipo;
	}

	private int getTipou(int tipo) throws IOException {
		if (tipo == 4 && fn.caratula(3, 3, stripper, doc).contains("Modelo")) {
			tipo = 1;
		}

		if (tipo == 0 && fn.caratula(3, 3, stripper, doc)
				.contains("Seguro de Automóviles Residentes de Uso y Servicio")) {
			tipo = 1;
		}
		return tipo;
	}

	public int getTipo(String conten,int tipo){
		int tipov= tipo;
		String[] searchTerms = {"Hogar Máster Total",ConstantsValue.MUL_EMPRESARIA_DESCU,ConstantsValue.MUL_EMPRESARIA_DESCU
	              ,"Seguro de Accidentes  Personales Colectivo","Seguro Múltiple Familiar Todo Riesgo Hogar"};
	       int inicio=-1;
	
		

		for (String term : searchTerms) {
			inicio =conten.indexOf(term);
			if (inicio != -1) {		
				if(tipov == 2 || tipov == 0 || tipov== 1){
			        tipov=4;
		         }
				break;
			}
		}
		return tipov;
	}

}
