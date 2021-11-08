package com.copsis.models.chubb;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraJsonModel;
import com.copsis.models.chubb.autos.ChubbAutosModel;
import com.copsis.models.mapfre.autos.MapfreAutosModel;

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
			System.out.println(contenido);
			String[] tipos = { "HOGAR", "AUTOMÓVILES", "Placas:", "EMPRESARIAL", "PYME SEGURA", "TRANSPORTE",
					"SEGURO CONCRETA" };
			for (String tipo : tipos) {
				System.out.println(contenido.contains(tipo) +"-->" + tipo);
				if (contenido.contains(tipo)) {
					switch (tipo) {
					          case "EMPRESARIAL":
		                        case "HOGAR":
		                        case "SEGURO CONCRETA":
		                            pagFin = fn.pagFinRango(pdfStripper, pdDoc, "Notas del riesgo");
		                     
		                            if (pagFin == 0) {
		                                pagFin = fn.pagFinRango(pdfStripper, pdDoc, "FACTURA");
		                            }                            
		                            if (pagFin > 0) {
		                                contenido = "";
		                         
		                             	modelo = new ChubbDiversosModel(fn.caratula(1, pagFin, pdfStripper, pdDoc),fn.textoBusqueda(pdfStripper, pdDoc, "AVISO DE COBRO",false)).procesar();
		                        	
		                            }
		                            break;
					case "AUTOMÓVILES":
					case "TRANSPORTE":
					case "Placas:":
						pagIni = fn.pagFinRango(pdfStripper, pdDoc, "CARÁTULA");
						pagFin = fn.pagFinRango(pdfStripper, pdDoc, "AVISO DE COBRO");

						if (pagFin > 0 && pagIni > 0) {
							ChubbAutosModel chubbAutos = new ChubbAutosModel();
							chubbAutos.setContenido(fn.caratula(pagIni, pagFin, pdfStripper, pdDoc));
							chubbAutos.setRecibos(fn.textoBusqueda(pdfStripper, pdDoc, "AVISO DE COBRO", false));
							modelo = chubbAutos.procesar();
						} else {
							ChubbAutosModel chubbAutos = new ChubbAutosModel();
							chubbAutos.setContenido(fn.caratula(0, 3, pdfStripper, pdDoc));
							modelo = chubbAutos.procesar();
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
