package com.copsis.services;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;

import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.json.JSONObject;

import com.copsis.models.qualitas.qualitasModel;

public class IdentificaPolizaService {

	public IdentificaPolizaService() {

	}
	
    public JSONObject QueCIA(String pathFile) throws FileNotFoundException, IOException {
        JSONObject jsonObject = new JSONObject();
        try {
         	 final URL scalaByExampleUrl = new URL(pathFile);
             final PDDocument documentToBeParsed = PDDocument.load(scalaByExampleUrl.openStream());
             final PDFTextStripper pdfStripper = new PDFTextStripper();
             COSDocument cosDoc = documentToBeParsed.getDocument();
             PDDocument pdDoc = new PDDocument(cosDoc);
             pdfStripper.setStartPage(1);
             pdfStripper.setEndPage(1);
             pdfStripper.setParagraphStart("@@@");
             String contenido = pdfStripper.getText(pdDoc);
             
             boolean encontro = false;
             System.out.println("entro");
             // ENTRADA PARA QUALITAS
             if (encontro == false) {
              
                 if (contenido.contains("qualitas")) {
                	
                	 qualitasModel datosQualitas = new qualitasModel(pdfStripper, pdDoc, contenido);
                     jsonObject = datosQualitas.procesa();
                     encontro = true;
                 }
             }
             
        	
        	
            pdDoc.close();
            return jsonObject;
		} catch (Exception ex) {
			  jsonObject.put("error", "IdentificaPoliza.QueCIA - pathFile=" + pathFile + " - catch:" + ex.getMessage() + " | " + ex.getCause());
	            return jsonObject;
		}
   
       
    }
    
    //Metodo agrega  separaciones de texto y inicios de parrafo.
    public String caratula(int inicio, int fin, PDFTextStripper stripper, PDDocument doc) throws IOException { //DEVUELVE UN CONTENIDO DE UN RANGO DE PAGINAS
        stripper.setStartPage(inicio);
        stripper.setEndPage(fin);
        stripper.setParagraphStart("@@@");
        stripper.setWordSeparator("###");
        stripper.setSortByPosition(true);
        return stripper.getText(doc);
    }

    //Meodo que retorna la numero de pagina donde se encuentra ,el string a buscar
    public int pagFinRango(PDFTextStripper pdfStripper, PDDocument pdDoc, String buscar) throws IOException {
        int valor = 0;
        for (int i = 1; i <= pdDoc.getPages().getCount(); i++) {
            pdfStripper.setStartPage(i);
            pdfStripper.setEndPage(i);
            if (pdfStripper.getText(pdDoc).contains(buscar)) {
                valor = i;
                break;
            }
        }
        return valor;
    }
        

}
