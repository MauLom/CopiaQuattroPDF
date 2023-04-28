package com.copsis.models.mapfre;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraJsonModel;
import com.copsis.models.mapfre.autos.MapfreAutosBModel;
import com.copsis.models.mapfre.autos.MapfreAutosModel;

public class MapfreModel {

	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();

	private PDFTextStripper stripper;
	private PDDocument doc;
	private String contenido;
	private int pagFin = 0;
	private int tipo  = 0;


	public MapfreModel(PDFTextStripper pdfStripper, PDDocument pdDoc, String contenido) {
		this.stripper = pdfStripper;
		this.doc = pdDoc;
		this.contenido = contenido;
	}

	public EstructuraJsonModel procesa() {

		try {
			
			tipo =fn.tipoPoliza(contenido);

			if(fn.tipoPoliza(contenido) == 2 &&  contenido.contains("ACCIDENTES PERSONALES") || 	fn.caratula(2, 2, stripper, doc).contains("ACCIDENTES PERSONALES")) {
				tipo =5;
			}
			

			if(tipo == 0 &&  contenido.contains("ACCIDENTES PERSONALES") && fn.caratula(1, 3, stripper, doc).contains("SEGURVIAJE")) {
				tipo =5;
			}
	
			if(tipo == 0 &&  contenido.contains("Dental PLATA")) {
				tipo =2;
			}
			

			if((tipo == 5 && contenido.contains(" DENTAL ") && !contenido.contains("SEGURO DE VIDA")) || (tipo == 0 && contenido.contains("SEGURVIAJE"))) {
				tipo = 2;
			}
			
			if((tipo == 5 && contenido.contains("ACCIDENTES PERSONALES INDIVIDUAL")) || (tipo == 0 && contenido.contains("SEGURVIAJE"))) {
				tipo = 2;
			}
			
			if(tipo == 5 &&  contenido.contains("MULTIPLE EMPRESARIAL")) {
				tipo =4;
			}
			if(tipo == 2 &&  contenido.contains("HOGAR BIEN SEGURO")) {
				tipo =4;
			}

			if(tipo == 5 &&  (contenido.contains("UBICACIÓN DE RIESGOS")|| contenido.contains("RIESGOS CUBIERTOS"))) {
				tipo =4;
			}
			if( tipo == 0 &&contenido.contains("Gastos Hospitalarios")) {
			    tipo =2;
			}
			if( tipo == 1 &&(contenido.contains("UBICACION Y DESCRIPCION DEL BIEN ASEGURADO")|| contenido.contains("PÓLIZA ESPECIFICA POR VIAJE")
				|| contenido.contains("PRONOSTICO DE EMBARQUES") )) {
	                tipo =4;
	        }
            boolean saludFt2=false;
			if(tipo ==0 && fn.caratula(1, 1, stripper, doc).contains("RECUPERACION MEDICA")){
                tipo=2;
				saludFt2=true;
			}

System.out.println( contenido);
			//PÓLIZA ESPECIFICA POR VIAJE

			switch ((tipo == 0 ? fn.tipoPoliza(contenido) : tipo )) {
			
	
			case 1:// Autos
				if (contenido.contains("CONTRATANTE Y CONDUCTOR" ) || contenido.contains("INFORMACIÓN GENERAL")) {
					pagFin = fn.pagFinRango(stripper, doc, "Coberturas Amparadas");
		
					if (pagFin == 0) {
						pagFin = fn.pagFinRango(stripper, doc, "INFORMACIÓN ADICIONAL");
					}
					modelo = new MapfreAutosModel(fn.caratula(1, pagFin, stripper, doc),
							fn.textoBusqueda(stripper, doc, "Serie de recibo:", false)).procesar();
				} else {
					modelo = new MapfreAutosBModel(fn.caratula(1, 3, stripper, doc)).procesar();
				}
				break;
				
			case 2://Salud
			      if(saludFt2){
					modelo = new MapfreSaludCModel().procesar(fn.caratula(1, 3, stripper, doc));
				  }	
                  else {
					if(contenido.contains("INFORMACIÓN GENERAL") && (contenido.contains("COBERTURAS Y SERVICIOS") || contenido.contains("ASEGURADO TITULAR") )) {
						pagFin = contenido.contains("COLECTIVIDAD ASEGURABLE")? 6: 5;
						modelo = new MapfreSaludRojoModel(fn.caratula(1, pagFin, stripper, doc)).procesar();
					}else {
						modelo = new MapfreSaludBModel(fn.caratula(1, 5, stripper, doc)).procesar();
					}
				  }
				
				
				break;
			case 4://Diversos
				modelo = new MapfreDiversosModel(fn.caratula(1, 10, stripper, doc),"","").procesar();
				break;
			case 5://vida
				if(contenido.contains("ACCIDENTES PERSONALES")) {
					 modelo = new MapfreVidaCModel(fn.caratula(1, 5, stripper, doc)).procesar();
				}else {
					 modelo = new MapfreVidaBModel(fn.caratula(1, 5, stripper, doc)).procesar();
				}
				break;
			}
			
			
			
			


			return modelo;
		} catch (Exception ex) {
			modelo.setError(
					MapfreModel.this.getClass().getTypeName() + " - catch:" + ex.getMessage() + " | " + ex.getCause());

			return modelo;
		}
	}

}
