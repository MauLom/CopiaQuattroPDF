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


			
			if ((contenido.contains("Datos del vehículo")) && !contenido.contains(" Vehicle description")) { // AUTOS
				AxaAutosModel datosAxaAutos = new AxaAutosModel(fn.caratula(1, 2, stripper, doc),
						fn.textoBusqueda(stripper, doc, "RECIBO PROVISIONAL DE", false));
				modelo = datosAxaAutos.procesar();
			}else if( contenido.contains("Datos del Vehículo") && contenido.contains("AUTOMÓVILES/AUTO  COLECTIVA")  && !contenido.contains(" Vehicle description")) {
				AxaAutos3Model datosAxaAutos = new AxaAutos3Model(fn.caratula(1, 4, stripper, doc));
				modelo = datosAxaAutos.procesar();
			}
			else if (contenido.indexOf("Recibo provisional para pago de primas") > -1) {
				AxaSaludModel datosAxaSalud = new AxaSaludModel(fn.caratula(1, 3, stripper, doc));
				modelo = datosAxaSalud.procesar();
		
			}

			else {
		
				String[] tipos = { "PAQUETE DE SEGURO EMPRESARIAL", "GASTOS M", "TRADICIONALES DE VIDA","VIDA PROTGT",
						"HOGAR INTEGRAL", "VEHICLE DESCRIPTION", "PROTECCIÓN A BIENES EMPRESARIALES",
						"PLANPROTEGE / COMERCIO",
						"RESPONSABILIDAD CIVIL, COMERCIO","DAÑOS","PLANPROTEGE / CONSTRUCTORES"};
				contenido = contenido.toUpperCase();
				for (String tipo : tipos) {
					if (contenido.contains(tipo)) {
						switch (tipo) {
						case "TRADICIONALES DE VIDA": case "VIDA PROTGT": // VIDA
							if(tipo.equals("VIDA PROTGT") ) {
								AxaVida2Model datosAxaVida = new AxaVida2Model(fn.caratula(1, 4, stripper, doc));
								modelo = datosAxaVida.procesar();
							}else {
								AxaVidaModel datosAxaVida = new AxaVidaModel(fn.caratula(1, 3, stripper, doc));
								modelo = datosAxaVida.procesar();	
							}											
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
						case "PLANPROTEGE / CONSTRUCTORES":
						// HOGAR
							 int pagFinal = doc.getNumberOfPages() > 4 ? doc.getNumberOfPages() :3;
							AxaDiversosModel datosAxaDiversos = new AxaDiversosModel(fn.caratula(1, pagFinal, stripper, doc));
							modelo = datosAxaDiversos.procesar();
						
							break;
						   case "RESPONSABILIDAD CIVIL, COMERCIO":// HOGAR
						   case "PROTECCIÓN A BIENES EMPRESARIALES":
							   int pagFin = doc.getNumberOfPages() > 3 ? 4 :3;
							   AxaDiversos2Model datosAxaDive = new AxaDiversos2Model(fn.caratula(1, pagFin, stripper, doc));
								modelo = datosAxaDive.procesar();								
								break;
							default: 
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
