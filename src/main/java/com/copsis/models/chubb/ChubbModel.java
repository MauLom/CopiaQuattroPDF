package com.copsis.models.chubb;

import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import com.copsis.constants.ConstantsValue;
import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraJsonModel;
import com.copsis.models.chubb.autos.ChubbAutosBModel;
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

			
			
			if(fn.caratula(1, 1, pdfStripper, pdDoc).contains("CONTENIDO###NUMERACIÓN")) {
				contenido = fn.caratula(2, 4, pdfStripper, pdDoc);
			}

			if(fn.caratula(1, 1, pdfStripper, pdDoc).contains("accidente vial o robo ")) {
				contenido = fn.caratula(2, 4, pdfStripper, pdDoc);
			}



			String[] tipos = { "RESPONSABILIDAD CIVIL VIAJERO","TRANSPORTE DE CARGA",
			" AUTOMÓVILES Y CAMIONES RESIDENTES","HOGAR","TRANSPORTE DE MERCANCIAS", "SEGURO DE VIAJE DE PLACER CHUBB","AUTOMÓVILES", "Placas:", "EMPRESARIAL", "PYME SEGURA", "TRANSPORTE",
					"SEGURO CONCRETA","TECHO","CONTRATISTA","Sótanos",ConstantsValue.EMBARCACIONES,"Todo Riesgo Contratistas" ,"Profesional para Médicos","PÓLIZA DE SEGURO VIDA",ConstantsValue.SEGURO_DE_VIAJE};
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
					case ConstantsValue.EMBARCACIONES:
					case "SEGURO DE VIAJE DE PLACER CHUBB":
					case "TRANSPORTE DE MERCANCIAS":
					case "Todo Riesgo Contratistas":
						
					
							pagFin = getPageFin(fn);
						
						if (pagFin > 0) {
						
							contenido = "";
							if(!contenido.contains("SOBRE AUTOMÓVILES Y CAMIONES RESIDENTES")){
								
								modelo = new ChubbDiversosModel(fn.caratula(1, pagFin, pdfStripper, pdDoc),
									fn.textoBusqueda(pdfStripper, pdDoc, ConstantsValue.AVISO_COBRO, false),fn.caratula(1, 7, pdfStripper, pdDoc)).procesar();
								

							}
							
						}
						
						else if( modelo.getVigenciaA().contains("-") && modelo.getVigenciaDe().contains("-") && modelo.getFormaPago() !=0 ) {
							encontro = true;
							break;
						}

						if(tipo.equals(ConstantsValue.EMBARCACIONES)) {
							encontro = true;
							break;
						}
						
						break;
					case "AUTOMÓVILES":
					case "TRANSPORTE":
					case "Placas:":		
					case " AUTOMÓVILES Y CAMIONES RESIDENTES":
						pagIni = fn.pagFinRango(pdfStripper, pdDoc, "CARÁTULA");
						pagFin = fn.pagFinRango(pdfStripper, pdDoc, ConstantsValue.AVISO_COBRO);
						
						
						if(contenido.contains("AUTOMÓVILES TURISTAS ") && contenido.contains("AUTO SOUTHBOUND")) {
							modelo = new ChubbAutosBModel().procesar(fn.caratula(0, 4, pdfStripper, pdDoc));
						}else if( fn.caratula(1, 1, pdfStripper, pdDoc).contains("PÓLIZA DE SEGURO TRANSPORTE DE CARGA ESPECIFICO")){
							modelo = new ChubbDiversosModel(fn.caratula(1, 2, pdfStripper, pdDoc),
							fn.textoBusqueda(pdfStripper, pdDoc, ConstantsValue.AVISO_COBRO, false),fn.caratula(1, 7, pdfStripper, pdDoc)).procesar();
						}

						else {
                 			
						if (pagFin > 0 && pagIni > 0) {
							ChubbAutosModel chubbAutos = new ChubbAutosModel();
							if(fn.caratula(pagIni, pagFin, pdfStripper, pdDoc).contains("Motor:")){
                               chubbAutos.setContenido(fn.caratula(pagIni, pagFin, pdfStripper, pdDoc));
							}else{
								chubbAutos.setContenido(fn.caratula(1, 1, pdfStripper, pdDoc));
							}
							
							chubbAutos.setRecibos(fn.textoBusqueda(pdfStripper, pdDoc, ConstantsValue.AVISO_COBRO, false));
							modelo = chubbAutos.procesar();
						} else {
							ChubbAutosModel chubbAutos = new ChubbAutosModel();
							chubbAutos.setContenido(fn.caratula(0, 3, pdfStripper, pdDoc));
							modelo = chubbAutos.procesar();
						}
						
					   }
					encontro = true;
						break;
					case "Profesional para Médicos",ConstantsValue.SEGURO_DE_VIAJE:
				
                      if(fn.caratula(1, 1, pdfStripper, pdDoc).contains(ConstantsValue.SEGURO_DE_VIAJE)){
							modelo = new ChubbSalud2Model().procesar(fn.caratula(0, 4, pdfStripper, pdDoc));
					  }else{
                         modelo = new ChubbSaludModel().procesar(fn.caratula(0, 4, pdfStripper, pdDoc));
					  }
					
					encontro = true;
					break;	
					case "PÓLIZA DE SEGURO VIDA":
			
					modelo = new ChubbVidaModel().procesar(fn.caratula(0, 3, pdfStripper, pdDoc));
					

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

	private int getPageFin(DataToolsModel fn) throws IOException {
		int pagFin=0;
		String[] palabrasClave = {"Notas del riesgo", "FACTURA", "Artículo 25", "Artículo###25", "ART. 25"};

			for (String palabraClave : palabrasClave) {
				pagFin = fn.pagFinRango(pdfStripper, pdDoc, palabraClave);
				if (pagFin != 0) {
					break; // Salimos del bucle si encontramos una coincidencia
				}
			}
		return pagFin;
	}
}
