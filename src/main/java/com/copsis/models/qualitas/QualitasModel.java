package com.copsis.models.qualitas;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraJsonModel;

public class QualitasModel {
	
	//Clases
	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();	
	//Variables
	private PDFTextStripper stripper;
	private PDDocument doc;
	private String contenido;
    //Constructor
	public QualitasModel(PDFTextStripper pdfStripper, PDDocument pdDoc, String contenido) {
		this.stripper = pdfStripper;
		this.doc = pdDoc;
		this.contenido = contenido;
	}

	private int pagIni = 0;
	private int pagFin = 0;

	public EstructuraJsonModel procesa() {

		try {
			
			
			if(!contenido.contains("SEGURO DE AUTOMÓVILES") || !contenido.contains("POLIZA VEHICULOS ")) {
				contenido = fn.textoBusqueda(stripper, doc, "Placas", false);
			}
			
		
			if (contenido.contains("SEGURO DE AUTOMÓVILES") || contenido.contains("POLIZA VEHICULOS ")
			|| contenido.contains("TOURIST VEHICLE POLICY") ||  contenido.contains("Placas:")) {
				pagIni = fn.pagFinRango(stripper, doc, "OFICINA DE");
				pagFin = fn.pagFinRango(stripper, doc, "IMPORTE TOTAL");
	
	
				if(fn.caratula(1, 2, stripper, doc).contains("Motocicletas")) {
					if(doc.getPages().getCount() >= 3) {
						if(fn.caratula(6, 7, stripper, doc).contains("Motocicletas")){
							qualitasAutosMotosModel datosQualitasMotosAutos = new qualitasAutosMotosModel(
								fn.caratula(6, 7, stripper, doc),fn.caratula(1, 2, stripper, doc));
						modelo = datosQualitasMotosAutos.procesar();
						}else{
							qualitasAutosMotosModel datosQualitasMotosAutos = new qualitasAutosMotosModel(
								fn.caratula(2, 3, stripper, doc),fn.caratula(1, 2, stripper, doc));
						modelo = datosQualitasMotosAutos.procesar();
						}
					
					}else {						
						if(fn.caratula(1, 1, stripper, doc).contains("COBERTURAS CONTRATADAS")) {
							qualitasAutosMotosModel datosQualitasMotosAutos = new qualitasAutosMotosModel(fn.caratula(1, 1, stripper, doc),fn.caratula(1, 2, stripper, doc));
							modelo = datosQualitasMotosAutos.procesar();	
						}else {
							qualitasAutosMotosModel datosQualitasMotosAutos = new qualitasAutosMotosModel(fn.caratula(2, 2, stripper, doc),fn.caratula(2, 2, stripper, doc));
							modelo = datosQualitasMotosAutos.procesar();
						}
						
					}
					
				}else {
							
					if (pagIni < pagFin) {
					
						if(fn.caratula(2, 2, stripper, doc).contains("Modelo") && fn.caratula(1, 1, stripper, doc).contains("TOURIST VEHICLE POLICY")){
							QualitasAutosUsaModel datosQualitasAutos = new 	QualitasAutosUsaModel();
								modelo = datosQualitasAutos.procesar(fn.caratula(2, 3, stripper, doc));
						} else if(fn.caratula(1, 1, stripper, doc).contains("Modelo") && fn.caratula(1, 1, stripper, doc).contains("TOURIST VEHICLE POLICY")){
							QualitasAutosUsaModel datosQualitasAutos = new 	QualitasAutosUsaModel();
								modelo = datosQualitasAutos.procesar(fn.caratula(1, 2, stripper, doc));
						}
						else{
							if(fn.caratula(3, 4, stripper, doc).contains("DESCRIPCIÓN DEL VEHÍCULO ASEGURADO")) {
								QualitasAutosModel datosQualitasAutos = new QualitasAutosModel(
										fn.caratula(pagIni, pagFin, stripper, doc),fn.caratula(3, 4, stripper, doc),fn.caratula(1, 8, stripper, doc));
								modelo = datosQualitasAutos.procesar();
							}else {
								// QualitasAutosModel datosQualitasAutos = new QualitasAutosModel(
								// 		fn.caratula(pagIni, pagFin, stripper, doc),fn.caratula(5, 6, stripper, doc),fn.caratula(1, 8, stripper, doc));
								// modelo = datosQualitasAutos.procesar();
							}
							
						}         
					} else {
				
						if(fn.caratula(1, 1, stripper, doc).contains("ACUSE DE ENTREGA DE DOCUMENTACIÓN CONTRACTUAL")
						|| fn.caratula(1, 1, stripper, doc).contains("ADVERTENCIA! POLIZA DE USO TURISTA")){					
					     	if(fn.caratula(5, 5, stripper, doc).contains("COBERTURAS CONTRATADAS")){
								pagFin=5;
							}
							if(fn.caratula(4, 5, stripper, doc).contains("Motor") && pagFin == 0){
								pagFin=4;
							}
							if(fn.caratula(3, 3, stripper, doc).contains("Motor") && pagFin == 0){
								pagFin=3;
							}											
						 QualitasAutosModel datosQualitasAutos = new QualitasAutosModel(fn.caratula(pagFin, pagFin+1, stripper, doc),"","");
						 modelo = datosQualitasAutos.procesar();
						}else{
						 QualitasAutosModel datosQualitasAutos = new QualitasAutosModel(fn.caratula(1, 2, stripper, doc),"","");
						modelo = datosQualitasAutos.procesar();
						}
						
					}
				}
				
				

			} else {
				modelo.setError(QualitasModel.this.getClass().getTypeName() + " | " + "No se pudo leer");

			}
			return modelo;
		} catch (Exception ex) {
			modelo.setError(QualitasModel.this.getClass().getTypeName() + " - catch:" + ex.getMessage() + " | "
					+ ex.getCause());
			
			return modelo;
		}
	}

}
