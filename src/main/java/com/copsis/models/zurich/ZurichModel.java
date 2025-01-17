package com.copsis.models.zurich;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraJsonModel;
import com.copsis.models.certificado.ZurichCertificadoGrupo;

public class ZurichModel {

	// Clases
	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
	// Variables
	private PDFTextStripper stripper;
	private PDDocument doc;
	private String contenido;
	

	public ZurichModel(PDFTextStripper pdfStripper, PDDocument pdDoc, String contenido) {
		this.stripper = pdfStripper;
		this.doc = pdDoc;
		this.contenido = contenido;
	}
	
	public EstructuraJsonModel procesar() {
		try {

			int tipo = fn.tipoPoliza(contenido);
			if(tipo == 0 && contenido.contains("Ubicación del riesgo:")) {
				tipo =4;
			}
			if(tipo == 5 && contenido.contains("Incendio")) {
				tipo = 4;
			}
			if(tipo == 5 && contenido.contains("Recolección de Residuos")) {
				tipo = 4;
			}
			if(tipo == 0 && (contenido.contains("Incendio Todo Riesgo Casa")|| contenido.contains("Transportes Mercancía:")
			|| contenido.contains("TRANSPORTES DE MERCANCIA:")
			|| contenido.contains("Trabajos eléctricos - Electricistas")
			|| fn.caratula(2, 2, stripper, doc).contains("Terremoto y Erupción Volcánica"))) {
				tipo =4;
			}
		

			switch ((tipo == 0 ? fn.tipoPoliza(contenido ): tipo)) {
			case 1:// Autos
				modelo  = new ZurichAutosModel(fn.caratula(1, 2, stripper, doc)).procesar();				
				break;
			case 2:// SALUD
				modelo  = new ZurichCertificadoGrupo(fn.caratula(1, 2, stripper, doc)).procesar();				
				break;
				
			case 4:// 
				String textoFormaPago = fn.textoBusqueda(stripper, doc, "Forma de pago", false);
				modelo  = new ZurichDiversosModel(fn.caratula(1, 8, stripper, doc) + textoFormaPago).procesar();				
				break;
			default:
				break;			
			}
			return modelo;
		} catch (Exception ex) {
			modelo.setError(
					ZurichModel.this.getClass().getTypeName() + " | " + ex.getMessage() + " | " + ex.getCause());
			return modelo;
		}
		
	}
	
}
