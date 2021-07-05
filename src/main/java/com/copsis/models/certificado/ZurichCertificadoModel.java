package com.copsis.models.certificado;

import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraJsonModel;
import com.copsis.models.zurich.ZurichAutosModel;
import com.copsis.models.zurich.ZurichModel;

public class ZurichCertificadoModel {

	// Clases
	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
	// Variables
	private PDFTextStripper stripper;
	private PDDocument doc;
	private String contenido;
	private Integer pagIni = 0;
	private Integer pagFin = 0;

	public ZurichCertificadoModel(PDFTextStripper pdfStripper, PDDocument pdDoc, String contenido) {
		this.stripper = pdfStripper;
		this.doc = pdDoc;
		this.contenido = contenido;
	}

	public EstructuraJsonModel procesar() {
		try {
		

			if(contenido.contains("Detalle de Sumas Aseguradas")) {
				modelo = new ZurichAsegurados(textoBusqueda(stripper, doc, "Detalle de Sumas Aseguradas")).procesar();	
			}else {	
			
				modelo = new ZurichCertificadoGrupo(textoBusqueda(stripper, doc, "Certificado Individual")).procesar();
			}
			return modelo;
		} catch (Exception ex) {
			modelo.setError(ZurichCertificadoModel.this.getClass().getTypeName() + " | " + ex.getMessage() + " | "
					+ ex.getCause());
			return modelo;
		}

	}

	public String textoBusqueda(PDFTextStripper pdfStripper, PDDocument pdDoc, String buscar) throws IOException { //BUSCA UNA PAGINA QUE CONTENGA LO BUSCADO 
        String x = "";
        for (int i = 1; i <= pdDoc.getPages().getCount(); i++) {
            pdfStripper.setStartPage(i);
            pdfStripper.setEndPage(i);
	
			if (pdfStripper.getText(pdDoc).contains(buscar) ) {
				PDFTextStripper s = new PDFTextStripper();
				s = pdfStripper;
				s.setSortByPosition(true);
				s.setParagraphStart("@@@");
				  s.setWordSeparator("###");
				x += s.getText(pdDoc) ;
			}
		}
        return x;
    }

}
