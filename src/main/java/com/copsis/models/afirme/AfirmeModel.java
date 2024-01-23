package com.copsis.models.afirme;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import com.copsis.constants.ConstantsValue;
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
	

	public AfirmeModel(PDFTextStripper pdfStripper, PDDocument pdDoc, String contenido) {
		this.stripper = pdfStripper;
		this.doc = pdDoc;
		this.contenido = contenido;
	}
	public EstructuraJsonModel procesar() {	
		 int tipo  = 0;
	     int tipoV  = 0;
		try {
		
			tipo =fn.tipoPoliza(contenido);
			Integer pagIni = 0;
		
				if( tipo == 4 && fn.caratula(1, 2, stripper, doc).contains("SEGURO PAQUETE EMPRESARIAL")){
			
                    tipo=4;
                    tipoV =4;  
                }

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
				
			
		        if( tipo == 0 && fn.caratula(1, 2, stripper, doc).contains("Ubicación")){
                    tipo=4;
                    tipoV =0;  
                }

				
				if( tipo == 0 && fn.caratula(3, 4, stripper, doc).contains(ConstantsValue.AUTOMOVILES_INDIVIDUALES)){
                    tipo=1;                    
                }


				switch (tipo == 0 ? fn.tipoPoliza(contenido) : tipo) {
				case 1:				
					if(contenido.contains("AUTOMÓVILES RESIDENTES") ||contenido.contains("AUTOMÓVILES SERVICIO PUBLICO") 
					|| fn.caratula(1, 2, stripper, doc).contains(ConstantsValue.AUTOMOVILES_INDIVIDUALES)					
					|| fn.caratula(1, 2, stripper, doc).contains("MOTOCICLETAS INDIVIDUALES")
					|| (fn.caratula(1, 2, stripper, doc).contains("PICK UPS") && fn.caratula(1, 2, stripper, doc).contains("INDIVIDUAL"))) {
						pagIni = fn.pagFinRango(stripper, doc, "CARATULA");	
						
						pagIni = pagIni == 0 ? fn.pagFinRango(stripper, doc, "SEGURO PARA PICK UPS") :pagIni;
						pagIni = pagIni == 0 ? fn.pagFinRango(stripper, doc, ConstantsValue.AUTOMOVILES_INDIVIDUALES) :pagIni;
						pagIni = pagIni == 0 ? fn.pagFinRango(stripper, doc, "MOTOCICLETAS INDIVIDUALES") :pagIni;
						pagIni = pagIni == 0 ? fn.pagFinRango(stripper, doc, "PICK UPS") :pagIni;
						Integer cbo= fn.pagFinRango(stripper, doc, "LIMITE MAXIMO DE");
						
						if(cbo > pagIni){
							pagIni =  cbo;
						}
						
						modelo  = new AfirmeAutosBModel(fn.caratula(pagIni, pagIni+2, stripper, doc),fn.recibos(stripper, doc, "RECIBO DE PRIMAS")).procesar();
                         
					}else {
						pagIni = fn.pagFinRango(stripper, doc, "DATOS DEL VEHÍCULO");
					
						pagIni = pagIni == -1 ? fn.pagFinRango(stripper, doc, "DESGLOSE DE COBERTURAS"): pagIni;	
							
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
						 case 4:
						 modelo  = new AfirmeDiversosBModel(fn.caratula(1,4, stripper, doc)).procesar();	
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
