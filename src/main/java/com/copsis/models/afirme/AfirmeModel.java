package com.copsis.models.afirme;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraJsonModel;

public class AfirmeModel {
	// Clases
	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
	// Variables
	private PDFTextStripper stripper;
	private PDDocument doc;
	private String contenido;
	private int tipo  = 0;
	private int tipoV  = 0;

	public AfirmeModel(PDFTextStripper pdfStripper, PDDocument pdDoc, String contenido) {
		this.stripper = pdfStripper;
		this.doc = pdDoc;
		this.contenido = contenido;
	}
	public EstructuraJsonModel procesar() {	
		try {
		
			tipo =fn.tipoPoliza(contenido);
			Integer pagIni = 0;

				if( tipo == 1 && fn.caratula(1, 2, stripper, doc).contains("SEGURO TRANSPORTES CARGA")){
					tipo=4;
					tipoV =1;
				}
		        if( tipo == 0 && fn.caratula(1, 2, stripper, doc).contains("UBICACIÓN")){
                    tipo=4;
                    tipoV =2;
                }
		        if( tipo == 4 && fn.caratula(1, 2, stripper, doc).contains("DAÑOS MATERIALES AL INMUEBLE")){
                    tipo=4;
                    tipoV =3;  
		        }

		        System.out.println(tipo +"-- " + tipoV);
			
				switch (tipo == 0 ? fn.tipoPoliza(contenido) : tipo) {
				case 1:
					if(contenido.contains("AUTOMÓVILES RESIDENTES") ||contenido.contains("AUTOMÓVILES SERVICIO PUBLICO")) {
						pagIni = fn.pagFinRango(stripper, doc, "CARATULA");
						modelo  = new AfirmeAutosBModel(fn.caratula(pagIni, pagIni+2, stripper, doc),fn.recibos(stripper, doc, "RECIBO DE PRIMAS")).procesar();
					}else {
						pagIni = fn.pagFinRango(stripper, doc, "DESGLOSE DE COBERTURAS");		
						modelo  = new AfirmeAutosModel(fn.caratula(pagIni, pagIni+2, stripper, doc)).procesar();	
					}
		
					break;
		
				case 4:
				    
				    switch (tipoV) {
                        case 0:
                            modelo  = new AfirmeDiversosModel(fn.caratula(1, 4, stripper, doc)).procesar();
                            break;
                         case 2:
                             modelo  = new AfirmeDiversosCModel().procesar(fn.caratula(1, 4, stripper, doc));
                            break;
                         case 3:
                             modelo  = new AfirmeDiversosDModel().procesar(fn.caratula(1, 4, stripper, doc));
                            break;    

                        default:
                             modelo  = new AfirmeDiversosCModel().procesar(fn.caratula(1, 4, stripper, doc));
                            break;
                    }
				    
				
		
					break;
		
				default:
					break;
				}

		

			
			return modelo;
		} catch (Exception ex) {
			modelo.setError(
					AfirmeModel.this.getClass().getTypeName() + " | " + ex.getMessage() + " | " + ex.getCause());
			return modelo;
		}
		
	}

}
