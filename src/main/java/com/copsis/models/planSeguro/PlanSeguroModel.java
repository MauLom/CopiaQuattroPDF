package com.copsis.models.planSeguro;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraJsonModel;

public class PlanSeguroModel {

	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();

	private PDFTextStripper stripper;
	private PDDocument doc;
	private String contenido;

	public PlanSeguroModel(PDFTextStripper stripper, PDDocument doc, String contenido) {
		super();
		this.stripper = stripper;
		this.doc = doc;
		this.contenido = contenido;
	}

	public EstructuraJsonModel procesar() {
		try {
			
			boolean modelo2 = false;
			System.out.println(contenido);

			int tipo = fn.tipoPoliza(contenido);
			if (tipo == 0 && contenido.contains("Ambulancia")) {
				tipo = 2;
				modelo2 =true;
			}
			if (tipo == 0 && contenido.contains("PÓLIZA DE SALUD OPTIMA")) {
				tipo = 2;	
			}
	
			switch (tipo) {
			case 2:
				if(modelo2) {
					modelo = new PlanSeguroSaludBModel().procesar(fn.caratula(1, 2, stripper, doc));
				}else {
					modelo = new PlanSeguroSaludModel(fn.caratula(1, 2, stripper, doc)).procesar();
					
				}
				
				break;

			default:
				break;
			}
			return modelo;
		} catch (Exception ex) {
			modelo.setError(
					PlanSeguroModel.this.getClass().getTypeName() + " | " + ex.getMessage() + " | " + ex.getCause());
			return modelo;
		}

	}

}
