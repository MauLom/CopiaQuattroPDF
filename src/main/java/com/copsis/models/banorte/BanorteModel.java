package com.copsis.models.banorte;

import java.math.BigDecimal;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraJsonModel;
import com.copsis.models.axa.AxaAutosModel;

public class BanorteModel {
	// Clases
	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
	// Variables
	private PDFTextStripper stripper;
	private PDDocument doc;
	private String contenido;
	private Integer pagIni =0;
	private Integer pagFin =0;


	
	
	public BanorteModel(PDFTextStripper pdfStripper, PDDocument pdDoc, String contenido) {
		this.stripper = pdfStripper;
		this.doc = pdDoc;
		this.contenido = contenido.replace("GASTOS MEDICOS MAYORES", "GASTOS MÉDICOS MAYORES");
	}
	public EstructuraJsonModel procesar() {
	
		try {

			switch (fn.tipoPoliza(contenido)) {
			case 1://Autos

				pagIni = fn.pagFinRango(stripper, doc, "PÓLIZA DE SEGURO DE");
				 if(pagIni == 0) pagIni = fn.pagFinRango(stripper, doc, "DE SEGURO DE AUTOMÓVILES");
				pagFin = fn.pagFinRango(stripper, doc, "DETALLES DE COBERTURAS");
				  if(pagFin == 0)   pagFin = fn.pagFinRango(stripper, doc, "DETALLE COBERTURAS");
					if(pagIni > 0 && pagFin > 0 && pagFin >= pagIni) {
						modelo  = new BanorteAutosModel(fn.caratula(pagIni, pagFin, stripper, doc),fn.textoBusqueda(stripper, doc, "AVISO DE COBRO", false)).procesar();
					}					   
				break;
			case 2://Salud 
				pagIni = fn.pagFinRango(stripper, doc, "LIZA INDIVIDUAL/FAMILIAR");
				if(pagIni > 0) pagFin = fn.pagFinRango(stripper, doc, "La legislación citada y las abreviaturas que aparecen");
				if(pagFin == 0) pagFin = fn.pagFinRango(stripper, doc, "En cumplimiento a lo dispuesto");
				if(pagFin <= pagIni) {
			     pagFin = fn.pagFinRango(stripper, doc, "NOMBRE DEL ASEGURADO");
				}
		
				if(pagIni > 0 && pagFin > 0 && pagFin >= pagIni) {
				
					modelo  = new BanorteSaludModel(fn.caratula(pagIni, pagFin, stripper, doc),fn.textoBusqueda(stripper, doc, "AVISO DE COBRO", false)).procesar();
				}
				break;
			case 3://Vida

				break;
			case 4://Diversos/Empresarial
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
