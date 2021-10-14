package com.copsis.models.axa;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraJsonModel;
import com.copsis.models.axa.salud.AxaSaludModel;
import com.copsis.models.axa.salud.AxaSaludV2Model;

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

			if (contenido.contains("Datos del vehículo") && contenido.contains(" Vehicle description") == false) { // AUTOS
				AxaAutosModel datosAxaAutos = new AxaAutosModel(fn.caratula(1, 1, stripper, doc),
						fn.textoBusqueda(stripper, doc, "RECIBO PROVISIONAL DE", false));
				modelo = datosAxaAutos.procesar();
			} else if (contenido.indexOf("Recibo provisional para pago de primas") > 0) {
				AxaSaludModel datosAxaSalud = new AxaSaludModel(fn.caratula(1, 3, stripper, doc));
				modelo = datosAxaSalud.procesar();
		
			}

			else {
		
				String tipos[] = { "PAQUETE DE SEGURO EMPRESARIAL", "GASTOS M", "TRADICIONALES DE VIDA",
						"HOGAR INTEGRAL", "VEHICLE DESCRIPTION", "PROTECCIÓN A BIENES EMPRESARIALES",
						"PLANPROTEGE / COMERCIO",
						"RESPONSABILIDAD CIVIL, COMERCIO"};
				contenido = contenido.toUpperCase();
				for (String tipo : tipos) {
					if (contenido.contains(tipo)) {
						switch (tipo) {
						case "TRADICIONALES DE VIDA": // VIDA
							AxaVidaModel datosAxaVida = new AxaVidaModel(fn.caratula(1, 3, stripper, doc));
							modelo = datosAxaVida.procesar();
							break;
						case "VEHICLE DESCRIPTION":
							
							AxaAutos2Model datosAxaAutos = new AxaAutos2Model(fn.caratula(1, 4, stripper, doc));
							modelo = datosAxaAutos.procesar();
							break;
						case "GASTOS M": // GASTOS MEDICOS --/(Se usara identificar la version 2 del pdf)Datos del
											// contratante
							if (contenido.contains("DATOS DEL CONTRATANTE")) {
						
								AxaSaludV2Model datosAxa2Salud = new AxaSaludV2Model(fn.caratula(1, 3, stripper, doc));
								modelo = datosAxa2Salud.procesar();
							} else {
				
								AxaSaludModel datosAxaSalud = new AxaSaludModel(fn.caratula(1, 3, stripper, doc));
								modelo = datosAxaSalud.procesar();
							}
							break;
						case "HOGAR INTEGRAL":
						// HOGAR							
							AxaDiversosModel datosAxaDiversos = new AxaDiversosModel(fn.caratula(1, 3, stripper, doc));
							modelo = datosAxaDiversos.procesar();
						
							break;
						   case "RESPONSABILIDAD CIVIL, COMERCIO":// HOGAR							
							   AxaDiversos2Model datosAxaDive = new AxaDiversos2Model(fn.caratula(1, 3, stripper, doc));
								modelo = datosAxaDive.procesar();								
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
