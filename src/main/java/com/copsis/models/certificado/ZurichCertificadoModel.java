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
	private Integer pagIni =0;
	private Integer pagFin =0;

	public ZurichCertificadoModel(PDFTextStripper pdfStripper, PDDocument pdDoc, String contenido) {
		this.stripper = pdfStripper;
		this.doc = pdDoc;
		this.contenido = contenido;
	}
	
	public EstructuraJsonModel procesar() {
		try {
		
			
		
		
				modelo  = new ZurichCertificadoGrupo(textoBusqueda(stripper, doc,"Certificado Individual")).procesar();				

			
			
			return modelo;
		} catch (Exception ex) {
			modelo.setError(
					ZurichCertificadoModel.this.getClass().getTypeName() + " | " + ex.getMessage() + " | " + ex.getCause());
			return modelo;
		}
		
	}
	
	
	public String textoBusqueda(PDFTextStripper pdfStripper, PDDocument pdDoc, String buscar)
			throws IOException { // BUSCA UNA PAGINA QUE CONTENGA LO BUSCADO
		StringBuilder x = new StringBuilder();
		int listado = 0;

		for (int i = 1; i <= pdDoc.getPages().getCount(); i++) {
			pdfStripper.setStartPage(i);
			pdfStripper.setEndPage(i);
			if (pdfStripper.getText(pdDoc).contains(buscar)) {

				// certificado|busca paginas necesarias
					PDFTextStripper s = new PDFTextStripper();
					//s.setParagraphStart("###");
					 s.setWordSeparator("###");
					s.setParagraphStart("@@@");
			
					s.setSortByPosition(true);			
					s = pdfStripper;
					x.append(s.getText(pdDoc));
				
			}
		}
		return x.toString();
	}
	
	

}
