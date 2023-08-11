package com.copsis.models.banorte;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import com.copsis.constants.ConstantsValue;
import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraJsonModel;

public class BanorteModel {
	// Clases
	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
	// Variables
	private PDFTextStripper stripper;
	private PDDocument doc;
	private String contenido;
	private Integer pagFin =0;


	
	
	public BanorteModel(PDFTextStripper pdfStripper, PDDocument pdDoc, String contenido) {

		this.stripper = pdfStripper;
		
		this.doc = pdDoc;
		this.contenido = contenido.replace("GASTOS MEDICOS MAYORES", "GASTOS MÉDICOS MAYORES");
	}
	public EstructuraJsonModel procesar() {
	
		try {
			Integer pagIni =0;
			Boolean autos =false;
			int tipo = fn.tipoPoliza(contenido);

			if(tipo == 4 ) {
				contenido = fn.caratula(1, doc.getNumberOfPages(), stripper, doc);
				tipo = fn.tipoPoliza(contenido);
			}
			
			if(tipo == 1 && contenido.contains("DATOS DEL BIEN ASEGURADO")) {
				tipo = 4;
			}
			if(tipo == 1 && fn.caratula(2, 2, stripper, doc).contains("SEGURO DE DAÑOS")) {
				tipo = 4;
			}
	
			switch (tipo) {
			case 1://Autos

					pagIni = fn.pagFinRango(stripper, doc, "PÓLIZA DE SEGURO DE");
					if(pagIni == 0) pagIni = fn.pagFinRango(stripper, doc, "DE SEGURO DE AUTOMÓVILES");
					pagFin = fn.pagFinRango(stripper, doc, "DETALLES DE COBERTURAS");
					if(pagFin == 0)   pagFin = fn.pagFinRango(stripper, doc, "DETALLE COBERTURAS");
					pagFin = pagFin == 0? fn.pagFinRango(stripper, doc, "DETALLE DE COBERTURAS") : pagFin;
				  
					if(pagIni > 0 && pagFin > 0 && pagFin >= pagIni) {
						modelo  = new BanorteAutosModel(fn.caratula(pagIni, pagFin, stripper, doc),fn.textoBusqueda(stripper, doc, ConstantsValue.AVISO_COBRO, false)).procesar();
				      	autos = true;
					}
					pagIni = fn.pagFinRango(stripper, doc, "AUTO COMPLEMENTARIO BANORTE");
					if(autos ==false && pagIni >-1){
						modelo  = new BanorteAutosBModel().procesar(fn.caratula(1, 4, stripper, doc));
					}					   
				break;
			case 2://Salud 

				pagIni = fn.pagFinRango(stripper, doc, "LIZA INDIVIDUAL/FAMILIAR");
				if(pagIni > 0) pagFin = fn.pagFinRango(stripper, doc, "La legislación citada y las abreviaturas que aparecen");
				if(pagFin == 0) pagFin = fn.pagFinRango(stripper, doc, "En cumplimiento a lo dispuesto");
				if(pagFin == 0) pagFin = doc.getNumberOfPages();
				if(pagFin <= pagIni) {
			     pagFin = fn.pagFinRango(stripper, doc, "NOMBRE DEL ASEGURADO");
				}
			
				if(pagIni > 0 && pagFin > 0 && pagFin >= pagIni) {
					pagFin = pagFin == 1 && doc.getNumberOfPages() > 5 ? doc.getNumberOfPages() : pagFin;
					
					modelo  = new BanorteSaludModel(fn.caratula(pagIni, pagFin, stripper, doc),fn.textoBusqueda(stripper, doc, ConstantsValue.AVISO_COBRO, false),fn.coberturas(stripper, doc, "COBERTURAS###OPC###IONALES")).procesar();
				}
				break;
			case 5://Vida
				modelo  = new BanorteVidaModel(fn.caratula(2, 3, stripper, doc),fn.textoBusqueda(stripper, doc,ConstantsValue.AVISO_COBRO, false)).procesar();

				break;
			case 4://Diversos/Empresarial
				pagIni = fn.pagFinRango(stripper, doc, "DATOS DEL CONTRATANTE");
				pagFin = fn.pagFinRango(stripper, doc, "nombre del Agente:");
				if(pagIni > 0 && pagFin > 0 && pagFin >= pagIni) {					
					if(fn.caratula(pagIni, pagFin, stripper, doc).contains("Prima Neta")) {
						pagIni = fn.pagFinRango(stripper, doc, "Prima Neta");
					}
				
					modelo  = new BanorteDiversos(fn.caratula(pagIni, pagFin, stripper, doc),fn.textoBusqueda(stripper, doc, ConstantsValue.AVISO_COBRO, false)).procesar();
				}				
				break;

			default:
				break;
			}
			
			return modelo;
		} catch (Exception ex) {
			modelo.setError(
					BanorteModel.this.getClass().getTypeName() + " | " + ex.getMessage() + " | " + ex.getCause());
			return modelo;
		}
		
	}
	

}
