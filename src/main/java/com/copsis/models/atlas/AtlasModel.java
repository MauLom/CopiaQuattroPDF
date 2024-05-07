package com.copsis.models.atlas;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraJsonModel;

public class AtlasModel {
	// Clases
	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
	// Variables
	private PDFTextStripper stripper;
	private PDDocument doc;
	private String contenido;

	// Constructor
	public AtlasModel(PDFTextStripper pdfStripper, PDDocument pdDoc, String contenido) {
		this.stripper = pdfStripper;
		this.doc = pdDoc;
		this.contenido = contenido;
	}

	public EstructuraJsonModel procesar() {
		try {

			int tipo = fn.tipoPoliza(contenido);
			
			if(tipo == 2 && contenido.contains("SEGURO DE DAÑOS")) {
				tipo =4;
			}
			
			switch (tipo) {
			case 1:// Autos
				modelo = new AtlasAutosModel(fn.caratula(1, 2, stripper, doc),
						fn.textoBusqueda(stripper, doc, "RECIBO DE PAGO DE SEGURO DE AUTOS", false)).procesar();
				break;
			case 2:// Salud
			   if(fn.caratula(1, 3, stripper, doc).contains("Características###de###la###Póliza")){
				modelo = new AtlasSaludBModel().procesar(fn.caratula(1, 3, stripper, doc));
			   }else if(fn.caratula(4, 5, stripper, doc).contains("Características###de###la###Póliza")){
				modelo = new AtlasSaludBModel().procesar(fn.caratula(4, 4, stripper, doc));
			   } 
			   else{
				modelo = new AtlasSaludModel(fn.caratula(1, 3, stripper, doc)).procesar();
			   }
				
				break;
			case 4:// Diversos
				modelo = new AtlasDiversosModel(fn.caratula(1, 3, stripper, doc)).procesar();
				break;
			case 5:// Vida
		
				if(contenido.contains("No.Póliza/Endoso")){
					modelo = new AtlasVida2Model().procesar(fn.caratula(1, 3, stripper, doc));
				}else{
					modelo = new AtlasVidaModel(fn.caratula(1, 3, stripper, doc)).procesar();
				}
				
				break;
			default:
				break;

			}

			return modelo;
		} catch (Exception ex) {
			modelo.setError(AtlasModel.this.getClass().getTypeName() + " | " + ex.getMessage() + " | " + ex.getCause());
			return modelo;
		}

	}
}
