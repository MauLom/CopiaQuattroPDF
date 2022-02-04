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
			
			int tipo = fn.tipoPoliza(contenido);

			if(tipo == 4 ) {
				contenido = fn.caratula(1, doc.getNumberOfPages(), stripper, doc);
				tipo = fn.tipoPoliza(contenido);
			}
			
			switch (tipo) {
			case 1://Autos

				pagIni = fn.pagFinRango(stripper, doc, "PÓLIZA DE SEGURO DE");
				 if(pagIni == 0) pagIni = fn.pagFinRango(stripper, doc, "DE SEGURO DE AUTOMÓVILES");
				pagFin = fn.pagFinRango(stripper, doc, "DETALLES DE COBERTURAS");
				  if(pagFin == 0)   pagFin = fn.pagFinRango(stripper, doc, "DETALLE COBERTURAS");
					if(pagIni > 0 && pagFin > 0 && pagFin >= pagIni) {
						modelo  = new BanorteAutosModel(fn.caratula(pagIni, pagFin, stripper, doc),fn.textoBusqueda(stripper, doc, ConstantsValue.AVISO_COBRO, false)).procesar();
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
