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
			
			int modeloTipo = 0;

			int tipo = fn.tipoPoliza(contenido);
			if (tipo == 0 && contenido.contains("Ambulancia")) {
				tipo = 2;
				modeloTipo = 1;
			}
			if( contenido.contains("Póliza de Golden Salud")){
				tipo = 2;
				modeloTipo = 3;
			}
			if (tipo == 0 && contenido.contains("PÓLIZA DE SALUD OPTIMA")) {
				tipo = 2;
			}

			if (tipo == 0 &&  contenido.contains("PLAN SEGURO ÓPTIMO")) {
				tipo = 2;
				modeloTipo = 0;
			}
		
			if (tipo == 0 &&  fn.caratula(3, 3, stripper, doc).contains("PLAN SEGURO ÓPTIMO")) {
				tipo = 2;
				modeloTipo = 2;
			}

			if (tipo == 0 &&  contenido.contains("Póliza de Plan Seguro Esencial")) {
				tipo = 2;
				modeloTipo = 3;
			}
		

			if (tipo == 0 && fn.caratula(3, 3, stripper, doc).contains("Póliza de Salud Avanzado") ) {
				tipo = 2;
				modeloTipo = 2;
			}
			if (tipo == 0 && fn.caratula(1, 4, stripper, doc).contains("SALUD OPTIMA / INDIVIDUAL") ) {
				tipo = 2;
				modeloTipo = 2;
			}
			if (tipo == 0 && fn.caratula(1, 2, stripper, doc).contains("Póliza de Plan Seguro Intermedio")) {
				tipo = 2;
				modeloTipo = 3;
			}

			if (tipo == 0 && fn.caratula(1, 2, stripper, doc).contains("Póliza de Plan Seguro Óptimo")) {
				tipo = 2;
				modeloTipo = 3;
			}
			if (tipo == 0 &&  fn.caratula(1, 4, stripper, doc).contains("PÓLIZA DE GOLDEN SALUD INDIVIDUAL")) {
				tipo = 2;
				modeloTipo = 0;
			}
			if (tipo == 0 &&  fn.caratula(1, 2, stripper, doc).contains("Póliza de Salud Avanzado")) {
				tipo = 2;
				modeloTipo = 3;
			}
	



			switch (tipo) {
				case 2:
					switch (modeloTipo) {
						case 0:
							modelo = new PlanSeguroSaludModel(fn.caratula(1, 2, stripper, doc)).procesar();
							break;
						case 1:
							modelo = new PlanSeguroSaludBModel().procesar(fn.caratula(1, 2, stripper, doc));
							break;
						case 2:
							modelo = new PlanSeguroSaludBModel().procesar(fn.caratula(3, 4, stripper, doc));
							break;
						case 3:
							modelo = new PlanSeguroSaludCModel().procesar(fn.caratula(1, 2, stripper, doc));
							break;
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
