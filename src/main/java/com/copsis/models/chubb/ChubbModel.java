package com.copsis.models.chubb;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

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
			String[] tipos = { "HOGAR", "AUTOMÓVILES", "Placas:", "EMPRESARIAL", "PYME SEGURA", "TRANSPORTE",
					"SEGURO CONCRETA" };
			for (String tipo : tipos) {
				if (contenido.contains(tipo)) {
					switch (tipo) {
//		                        case "HOGAR":
//		                        case "SEGURO CONCRETA":
//		                            pagFin = pagFinRango(stripper, doc, "Notas del riesgo");
//		                     
//		                            if (pagFin == 0) {
//		                                pagFin = pagFinRango(stripper, doc, "FACTURA");
//		                            }                            
//		                         
//		                            if (pagFin > 0) {
//		                                contenido = "";
//		                                DatosChubbHogar datosChubbHogar = new DatosChubbHogar(identifica.caratula(1, pagFin, stripper, doc), identifica.recibos(stripper, doc, "AVISO DE COBRO"));
//		                                jsonObject = datosChubbHogar.procesar();
//		                            }
//		                            break;
					case "AUTOMÓVILES":
					case "TRANSPORTE":
						pagIni = fn.pagFinRango(pdfStripper, pdDoc, "CARÁTULA");
						pagFin = fn.pagFinRango(pdfStripper, pdDoc, "AVISO DE COBRO");

						if (pagFin > 0 && pagIni > 0) {
							ChubbAutosModel chubbAutos = new ChubbAutosModel();
							chubbAutos.setContenido(fn.caratula(pagIni, pagFin, pdfStripper, pdDoc));
							chubbAutos.setRecibos(fn.textoBusqueda(pdfStripper, pdDoc, "AVISO DE COBRO", false));
							modelo = chubbAutos.procesar();
						} else {
							// DatosChubbAutos datosChubbAutos = new DatosChubbAutos(identifica.caratula(1,
							// 6, pdfStripper, pdDoc), identifica.recibos(pdfStripper, pdDoc, "VISO DE
							// COBRO"));
							// jsonObject = datosChubbAutos.procesarV2();
						}
						break;
//		                        case "EMPRESARIAL":
//		                        case "PYME SEGURA":
//		                            pagIni = pagFinRango(stripper, doc, "CARÁTULA");
//		                            pagFin = pagFinRango(stripper, doc, "AVISO DE COBRO");
//		                            DatosChubbEmpresarial datosChubbEmpresarial = new DatosChubbEmpresarial(identifica.caratula(pagIni, pagFin, stripper, doc), identifica.recibos(stripper, doc, "VISO DE COBRO"));
//		                            jsonObject = datosChubbEmpresarial.procesar();
//		                            break;
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
