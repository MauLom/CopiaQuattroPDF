package com.copsis.models.sura;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import com.copsis.constants.ConstantsValue;
import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraJsonModel;

public class SuraModel {
	// Clases
	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
	// Variables
	private PDFTextStripper stripper;
	private PDDocument doc;
	private String contenido;
	
	// Constructor
	public SuraModel(PDFTextStripper pdfStripper, PDDocument pdDoc, String contenido) {
		this.stripper = pdfStripper;
		this.doc = pdDoc;
		this.contenido = contenido;
	}
	
	public EstructuraJsonModel procesar() {
		try {
		 
				if(contenido.contains("RECORDATORIO DE PAGO")) {
					contenido = fn.caratula(2, 3, stripper, doc);
				}
				
				int tipo = fn.tipoPoliza(contenido);
			

				if(tipo == 5 &&  contenido.contains("Grupo Experiencia Global Sin Dividendos")) {
					tipo =5;
				}
				
				if(tipo == 1 &&  contenido.contains("MEDIC ")) {
					tipo =2;
				}
				if(tipo == 2 && contenido.contains("Hogar Máster Total")) {
					tipo =4;
				}
				if(tipo == 2 && contenido.contains(ConstantsValue.MUL_EMPRESARIA_DESCU )) {
					tipo =4;
				}

				
				if(tipo == 0 && fn.caratula(2, 3, stripper, doc).contains(ConstantsValue.MUL_EMPRESARIA_DESCU)) {
					tipo =4;
				}
				if(tipo == 0 && fn.caratula(2, 3, stripper, doc).contains("Seguro de Accidentes  Personales Colectivo")) {
					tipo =2;
				}
				if(tipo == 1 && fn.caratula(2, 3, stripper, doc).contains("Seguro Múltiple Familiar Todo Riesgo Hogar ")) {
					tipo =4;
				}

				if(tipo == 4 && fn.caratula(3, 3, stripper, doc).contains("Modelo")){
                  tipo=1;
				}

				if(tipo == 0 && fn.caratula(3, 3, stripper, doc).contains("Seguro de Automóviles Residentes de Uso y Servicio")
				) {
					tipo =1;
				}

				
			

         
			switch ((tipo == 0 ? fn.tipoPoliza(contenido) : tipo )) {
			case 1://Autos
					int pagFin = fn.pagFinRango(stripper, doc, "Nombre Agente");
					
					if(pagFin == 0) {
						pagFin = 3;
					}
					modelo  = new SuraAutosModel(fn.caratula(1, pagFin, stripper, doc)).procesar();					
				break;
			case 2://Salud
				 if(!fn.caratula(1, 1, stripper, doc).contains("Desde las")) {
						modelo  = new SuraSaludBModel(fn.caratula(1, 3, stripper, doc)).procesar();
				 }
							
			break;
			
			case 4://Diversos

			   
				if( contenido.contains("Hogar Máster Total")) {
					modelo  = new SuraDiversosModel(fn.caratula(1, 3, stripper, doc)).procesar();
				}else 	if( contenido.contains(ConstantsValue.MUL_EMPRESARIA_DESCU)) {
					modelo  = new SuraDiversosModel(fn.caratula(1, 3, stripper, doc)).procesar();
				}
				else if( contenido.contains("DAÑOS RESPONSABILIDAD")) {
					modelo  = new SuraDiversos2Model().procesar(fn.caratula(3, 3, stripper, doc));
				}
				else {
					modelo  = new SuraDiversosModel(fn.caratula(2, 3, stripper, doc)).procesar();
				}
			
				break;
		
				
				case 5:
				modelo = new SuraVidaModel().procesar(fn.caratula(2, 3, stripper, doc));
				break;
			default:
				break;

			}

			return modelo;
		} catch (Exception ex) {
			modelo.setError(
					SuraModel.this.getClass().getTypeName() + " | " + ex.getMessage() + " | " + ex.getCause());
			return modelo;
		}
		
	}
	

}
