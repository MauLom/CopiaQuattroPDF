package com.copsis.models.qualitas;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import com.copsis.constants.ConstantsValue;
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



	public EstructuraJsonModel procesa() {
	 int pagIni = 0;
	 int pagFin = 0;
		try {
			
			
			if(!contenido.contains("SEGURO DE AUTOMÓVILES") || !contenido.contains("POLIZA VEHICULOS ")) {
				contenido = fn.textoBusqueda(stripper, doc, "Placas", false);
			}
			
		
			if (contenido.contains("SEGURO DE AUTOMÓVILES") || contenido.contains("POLIZA VEHICULOS ")
			|| contenido.contains(ConstantsValue.TOURIST_VEHICLE_POLICY) ||  contenido.contains("Placas:")) {
				pagIni = fn.pagFinRango(stripper, doc, "OFICINA DE");
				pagFin = fn.pagFinRango(stripper, doc, "IMPORTE TOTAL");
				if(fn.caratula(1, 2, stripper, doc).contains("Motocicletas")) {
					if(doc.getPages().getCount() >= 3) {

						if(fn.caratula(6, 7, stripper, doc).contains("Motocicletas")){
							qualitasAutosMotosModel datosQualitasMotosAutos = new qualitasAutosMotosModel(
								fn.caratula(6, 7, stripper, doc),fn.caratula(1, 2, stripper, doc));
						modelo = datosQualitasMotosAutos.procesar();
						} else if(fn.caratula(1, 1, stripper, doc).contains(ConstantsValue.COBERTURAS_CONTRATADAS)) {
							qualitasAutosMotosModel datosQualitasMotosAutos = new qualitasAutosMotosModel(fn.caratula(1, 1, stripper, doc),fn.caratula(1, 2, stripper, doc));
							modelo = datosQualitasMotosAutos.procesar();	
						}
						else{
							
							qualitasAutosMotosModel datosQualitasMotosAutos = new qualitasAutosMotosModel(fn.caratula(2, 3, stripper, doc),fn.caratula(1, 2, stripper, doc));
						    modelo = datosQualitasMotosAutos.procesar();
						}
					
					}else {
						
						if(fn.caratula(1, 1, stripper, doc).contains(ConstantsValue.COBERTURAS_CONTRATADAS)) {
							qualitasAutosMotosModel datosQualitasMotosAutos = new qualitasAutosMotosModel(fn.caratula(1, 1, stripper, doc),fn.caratula(1, 2, stripper, doc));
							modelo = datosQualitasMotosAutos.procesar();	
						}else {
							qualitasAutosMotosModel datosQualitasMotosAutos = new qualitasAutosMotosModel(fn.caratula(2, 2, stripper, doc),fn.caratula(2, 2, stripper, doc));
							modelo = datosQualitasMotosAutos.procesar();
						}
						
					}
					
				}else {				
					if(fn.caratula(1, 1, stripper, doc).contains(ConstantsValue.MODELO2) && fn.caratula(1, 1, stripper, doc).contains(ConstantsValue.TOURIST_VEHICLE_POLICY)){
						pagIni = fn.pagFinRango(stripper, doc, "PLAN:");
						pagFin = fn.pagFinRango(stripper, doc, "PLAN:")+1;
					}
					
					if (pagIni < pagFin) {
					
						if(fn.caratula(2, 2, stripper, doc).contains(ConstantsValue.MODELO2) && fn.caratula(1, 1, stripper, doc).contains(ConstantsValue.TOURIST_VEHICLE_POLICY)){
							QualitasAutosUsaModel datosQualitasAutos = new 	QualitasAutosUsaModel();
								modelo = datosQualitasAutos.procesar(fn.caratula(2, 3, stripper, doc));
						} else if(fn.caratula(1, 1, stripper, doc).contains(ConstantsValue.MODELO2) && fn.caratula(1, 1, stripper, doc).contains(ConstantsValue.TOURIST_VEHICLE_POLICY)){
							QualitasAutosUsaModel datosQualitasAutos = new 	QualitasAutosUsaModel();
								modelo = datosQualitasAutos.procesar(fn.caratula(1, 2, stripper, doc));
						}
						else{
							
							if(fn.caratula(3, 4, stripper, doc).contains("DESCRIPCIÓN DEL VEHÍCULO ASEGURADO")) {
						
								QualitasAutosModel datosQualitasAutos = new QualitasAutosModel(
										fn.caratula(pagIni, pagFin, stripper, doc),fn.caratula(3, 4, stripper, doc),fn.caratula(1, 8, stripper, doc));
								modelo = datosQualitasAutos.procesar();
							} 
							else if(!fn.caratula(1, 1, stripper, doc).contains(ConstantsValue.DANOS_MATERIALES)){
								pagIni = fn.pagFinRango(stripper, doc, ConstantsValue.DANOS_MATERIALES);							
								pagFin = fn.pagFinRango(stripper, doc, ConstantsValue.DANOS_MATERIALES)+1;
								if(fn.caratula(2, 2, stripper, doc).contains(ConstantsValue.DANOS_MATERIALES)){
									pagIni=2;
									pagFin=3;
								}

								if(fn.caratula(2, 2, stripper, doc).contains("Proteccion para Danos sin Responsabilida")){
									pagIni=2;
									pagFin=3;
								}
								
								
							
								QualitasAutosModel datosQualitasAutos = new QualitasAutosModel(
										fn.caratula(pagIni, pagFin, stripper, doc),fn.caratula(5, 6, stripper, doc),fn.caratula(1, 8, stripper, doc));
								modelo = datosQualitasAutos.procesar();
							}
							else {
								
								
								QualitasAutosModel datosQualitasAutos = new QualitasAutosModel(
										fn.caratula(pagIni, pagFin, stripper, doc),fn.caratula(5, 6, stripper, doc),fn.caratula(1, 8, stripper, doc));
								modelo = datosQualitasAutos.procesar();
							}
							
						}         
					} else {
					
				
						if(fn.caratula(1, 1, stripper, doc).contains("ACUSE DE ENTREGA DE DOCUMENTACIÓN CONTRACTUAL")
						|| fn.caratula(1, 1, stripper, doc).contains("ADVERTENCIA! POLIZA DE USO TURISTA")
						|| fn.caratula(1, 1, stripper, doc).contains("¡ADVERTENCIA! POLIZA PARA USO DE VIAJE TURISTA")){					
					     	if(fn.caratula(5, 5, stripper, doc).contains(ConstantsValue.COBERTURAS_CONTRATADAS)){
								pagFin=5;
							}

							if(fn.caratula(4, 5, stripper, doc).contains(ConstantsValue.MOTORNM) && pagFin == 0){
								pagFin=4;
							}
							if(fn.caratula(3, 3, stripper, doc).contains(ConstantsValue.MOTORNM) && pagFin == 0){
								pagFin=3;
							}
							if(fn.caratula(18, 19, stripper, doc).contains(ConstantsValue.MOTORNM) && pagFin == 0){
								pagFin=3;
							}											
						 QualitasAutosModel datosQualitasAutos = new QualitasAutosModel(fn.caratula(pagFin, pagFin+1, stripper, doc),"","");
						 modelo = datosQualitasAutos.procesar();
						}else if (fn.caratula(1, 1, stripper, doc).contains("ASEGURAMOS AUTOS | CUIDAMOS PERSONAS")){
							if(fn.caratula(8, 8, stripper, doc).contains("COBERTURAS CONTRATADAS")){
								 pagFin=8;
							 }
							  QualitasAutosModel datosQualitasAutos = new QualitasAutosModel(fn.caratula(pagFin, pagFin+1, stripper, doc),"","");
							  modelo = datosQualitasAutos.procesar();
						 }
						else{
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
