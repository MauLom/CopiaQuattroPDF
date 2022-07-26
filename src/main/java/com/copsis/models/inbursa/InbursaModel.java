package com.copsis.models.inbursa;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraJsonModel;

public class InbursaModel {
	// Clases
	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
	// Variables
	private PDFTextStripper stripper;
	private PDDocument doc;
	private String contenido;

	public InbursaModel(PDFTextStripper pdfStripper, PDDocument pdDoc, String contenido) {
		this.stripper = pdfStripper;
		this.doc = pdDoc;
		this.contenido = contenido;
	}

	public EstructuraJsonModel procesar() {
		try {
			
			Boolean tipof5 =false;
			int tipo =  fn.tipoPoliza(contenido);
			if(tipo == 0 &&  fn.caratula(1, 3, stripper, doc).contains("ENDOSO DE BENEFICIARIOS") ) {
				tipo =5;
				tipof5 = true;
			}

			
			switch (tipo) {
			case 1:// Autos
				int fin = doc.getNumberOfPages() == 5 ? 4 : 2;
				modelo = new InbursaAutosModel(fn.caratula(1, fin, stripper, doc),
						fn.textoBusqueda(stripper, doc, "DETALLE DE RECIBOS", false)).procesar();
				break;
			case 2:// Salud
				modelo = new InbursaSaludModel(fn.caratula(1, 3, stripper, doc)).procesar();
				break;
			case 4:// Diversos
				modelo = new inbursaDiversosModel(fn.caratula(1, 3, stripper, doc)).procesar();
				break;
			case 5:// Vida
				if(tipof5) {
					modelo = new InbursaVidaBModel().procesar(fn.caratula(1, 5, stripper, doc));
				}else {
				modelo = new InbursaVidaModel().procesar(fn.caratula(1, 5, stripper, doc));
				}
				break;	
			default:
				break;
			}

			return modelo;
		} catch (Exception ex) {
			modelo.setError(
					InbursaModel.this.getClass().getTypeName() + " | " + ex.getMessage() + " | " + ex.getCause());
			return modelo;
		}

	}

}
