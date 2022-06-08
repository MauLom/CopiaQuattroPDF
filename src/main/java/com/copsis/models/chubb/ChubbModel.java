package com.copsis.models.chubb;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import com.copsis.constants.ConstantsValue;
import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraJsonModel;
import com.copsis.models.chubb.autos.ChubbAutosModel;

import lombok.Data;

@Data
public class ChubbModel {
	// Variables
	PDFTextStripper pdfStripper;
	PDDocument pdDoc;
	String contenido;

	public EstructuraJsonModel procesa() {
		EstructuraJsonModel modelo = new EstructuraJsonModel();
		DataToolsModel fn = new DataToolsModel();
		try {
			int pagFin = 0;
			int pagIni = 0;
			

			String[] tipos = { "RESPONSABILIDAD CIVIL VIAJERO","TRANSPORTE DE CARGA","HOGAR","TRANSPORTE DE MERCANCIAS", "AUTOMÓVILES", "Placas:", "EMPRESARIAL", "PYME SEGURA", "TRANSPORTE",
					"SEGURO CONCRETA","TECHO","CONTRATISTA","Sótanos","EMBARCACIONES" };
			 boolean encontro = false;
			for (String tipo : tipos) {			
				if (contenido.contains(tipo) && !encontro) {
					switch (tipo) {
					case "RESPONSABILIDAD CIVIL VIAJERO":
					case "TRANSPORTE DE CARGA":
					case "EMPRESARIAL":
					case "HOGAR":
					case "SEGURO CONCRETA":
					case "PYME SEGURA":
					case "CONTRATISTA":
					case "Sótanos":
					case "EMBARCACIONES":
					case "TRANSPORTE DE MERCANCIAS":
						pagFin = fn.pagFinRango(pdfStripper, pdDoc, "Notas del riesgo");

						if (pagFin == 0) {
							pagFin = fn.pagFinRango(pdfStripper, pdDoc, "FACTURA");
						}
						if (pagFin == 0) {
							pagFin = fn.pagFinRango(pdfStripper, pdDoc, "Artículo 25");
						}
						
						if (pagFin == 0) {
							pagFin = fn.pagFinRango(pdfStripper, pdDoc, "ART. 25");
						}

				
						if (pagFin > 0) {
							contenido = "";
							modelo = new ChubbDiversosModel(fn.caratula(1, pagFin, pdfStripper, pdDoc),
									fn.textoBusqueda(pdfStripper, pdDoc, ConstantsValue.AVISO_COBRO, false)).procesar();

						}
						if(modelo.getVigenciaA().contains("-") && modelo.getVigenciaDe().contains("-") && modelo.getFormaPago() !=0 ) {
							encontro = true;
							break;
						}

						if(tipo.equals("EMBARCACIONES")) {
							encontro = true;
							break;
						}
						
					case "AUTOMÓVILES":
					case "TRANSPORTE":
					case "Placas:":
						pagIni = fn.pagFinRango(pdfStripper, pdDoc, "CARÁTULA");
						pagFin = fn.pagFinRango(pdfStripper, pdDoc, ConstantsValue.AVISO_COBRO);

						if (pagFin > 0 && pagIni > 0) {
							ChubbAutosModel chubbAutos = new ChubbAutosModel();
							chubbAutos.setContenido(fn.caratula(pagIni, pagFin, pdfStripper, pdDoc));
							chubbAutos.setRecibos(fn.textoBusqueda(pdfStripper, pdDoc, ConstantsValue.AVISO_COBRO, false));
							modelo = chubbAutos.procesar();
						} else {
							ChubbAutosModel chubbAutos = new ChubbAutosModel();
							chubbAutos.setContenido(fn.caratula(0, 3, pdfStripper, pdDoc));
							modelo = chubbAutos.procesar();
						}
						encontro = true;
						break;
					default:
						break;
					}
				}
			}
			return modelo;
		} catch (Exception ex) {
			modelo.setError(
					ChubbModel.this.getClass().getTypeName() + " - catch:" + ex.getMessage() + " | " + ex.getCause());
			return modelo;
		}
	}
}
