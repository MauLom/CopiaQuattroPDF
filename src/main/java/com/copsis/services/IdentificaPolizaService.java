package com.copsis.services;


import java.io.IOException;

import java.net.URL;

import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;

import com.copsis.controllers.forms.PdfForm;
import com.copsis.models.EstructuraJsonModel;
import com.copsis.models.aba.AbaModel;
import com.copsis.models.atlas.AtlasModel;
import com.copsis.models.axa.AxaModel;
import com.copsis.models.banorte.BanorteModel;
import com.copsis.models.chubb.ChubbModel;
import com.copsis.models.gnp.GnpModel;
import com.copsis.models.inbursa.InbursaModel;
import com.copsis.models.mapfre.MapfreModel;
import com.copsis.models.metlife.MetlifeModel;
import com.copsis.models.multiva.MultivaModels;
import com.copsis.models.qualitas.QualitasModel;
import com.copsis.models.sisnova.SisnovaModel;
import com.copsis.models.sisnova.SisnovaSaludModel;

import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

@RequiredArgsConstructor
@Service
public class IdentificaPolizaService {

	private EstructuraJsonModel modelo = new EstructuraJsonModel();
	 
    public EstructuraJsonModel queIndentificaPoliza( PdfForm pdfForm) throws  IOException {

        try {

         	 final URL scalaByExampleUrl = new URL(pdfForm.getUrl());
             final PDDocument documentToBeParsed = PDDocument.load(scalaByExampleUrl.openStream());
             final PDFTextStripper pdfStripper = new PDFTextStripper();
             COSDocument cosDoc = documentToBeParsed.getDocument();
             PDDocument pdDoc = new PDDocument(cosDoc);
             pdfStripper.setStartPage(1);
             pdfStripper.setEndPage(1);
             pdfStripper.setParagraphStart("@@@");
             String contenido = pdfStripper.getText(pdDoc);

             boolean encontro = false;
             
             //CHUBB
             if (encontro == false) {
                 if (contenido.contains("Chubb")) {
                     ChubbModel datosChubb = new ChubbModel();
                     datosChubb.setPdfStripper(pdfStripper);
                     datosChubb.setPdDoc(pdDoc);
                     datosChubb.setContenido(contenido);
                     modelo = datosChubb.procesa();
                     encontro = true;
                 }
             }
      
             // ENTRADA PARA QUALITAS
             if (encontro == false) {
                 if (contenido.contains("qualitas")) {
                	 QualitasModel datosQualitas = new QualitasModel(pdfStripper, pdDoc, contenido);
                	 modelo = datosQualitas.procesa();
                     encontro = true;
                 }
             }
             
             if (encontro == false) {
                 if (contenido.contains("visite gnp.com.mx") || contenido.contains("GNP") || contenido.contains("Grupo Nacional Provincial S.A.B") || contenido.contains("Grupo Nacional Provincial")) {
                	 GnpModel datosGnp = new GnpModel(pdfStripper, pdDoc, contenido);
                	 modelo = datosGnp.procesa();
                     encontro = true;
                 }
             }
             
             // ENTRADA PARA MAPFRE
             if (encontro == false) {
                 if (contenido.length() > 502) {
                     if (contenido.indexOf("MAPFRE") > -1 || contenido.contains("Mapfre Tepeyac")) {                        
                    	 MapfreModel datosmapfre = new MapfreModel(pdfStripper, pdDoc, contenido);
                    	 modelo = datosmapfre.procesa();
                         encontro = true;
                     }
                 }
             }
             
             // ENTRADA PARA AXA
             if (encontro == false) {
                 if (contenido.contains("AXA Seguros, S.A. de C.V.") || contenido.contains("AXA SEGUROS, S.A. DE C.V") || contenido.contains("AXA Seguros, S.A de C.V.")) {
                	 AxaModel datosAxa = new AxaModel(pdfStripper, pdDoc, contenido);
                	 modelo = datosAxa.procesa();
                     encontro = true;
                 }
             }
             
             // ENTRADA PARA ABA
             if (encontro == false) {
                 if (contenido.contains("ABA Seguros S.A.")
                         || contenido.contains("Datos del asegurado y/o propietario") && contenido.contains("Domicilio del bien asegurado") && contenido.contains("Descripcion del Riesgo")
                         || contenido.contains("Datos del asegurado y/o propietario") && contenido.contains("Características del riesgo")) {
                	 AbaModel datosAba = new AbaModel();
                	 datosAba.setPdfStripper(pdfStripper);
                	 datosAba.setPdDoc(pdDoc);
                	 datosAba.setContenido(contenido);
                	 modelo = datosAba.procesa();
                     encontro = true;
                 }
             }
             
             // ENTRADA PARA BANORTE
             if (encontro == false) {
                 if (contenido.contains("Banorte")
                         || (contenido.contains("DATOS DEL CONTRATANTE (Sírvase escribir con letra de molde)")
                         && contenido.contains("Datos del asegurado titular (Solicitante)")
                         && contenido.contains("ASEGURADOS"))) {
                	 if(contenido.contains("Estimado(a)")) {
                		 contenido = caratula(3, 4, pdfStripper, pdDoc);
                		 BanorteModel datosBanort = new BanorteModel(pdfStripper, pdDoc, contenido);
                         modelo = datosBanort.procesar();
                         encontro = true;
                	 }else {
                		 BanorteModel datosBanort = new BanorteModel(pdfStripper, pdDoc, contenido);
                         modelo = datosBanort.procesar();
                         encontro = true;
                	 }
                
                 }
             }
             // ENTRADA PARA INBURSA
             if (encontro == false) {
                 if (contenido.contains("Inbursa") || contenido.contains("INBURSA")) {
                	 InbursaModel datosInbursa = new InbursaModel(pdfStripper, pdDoc, contenido);
                	 modelo = datosInbursa.procesar();
                     encontro = true;
                 }
             }
             
             // ENTRADA PARA METLIFE
             if (encontro == false) {
                 if (contenido.split("@@@")[1].contains("MetLife México S.A.")
                         || contenido.contains("www.metlife.com.mx")
                         || contenido.contains("MetLife México")) {
                	 MetlifeModel datosMetlife = new MetlifeModel(pdfStripper, pdDoc, contenido);
                	 modelo = datosMetlife.procesar();
                     encontro = true;
                 }
             }
             
             // ENTRADA PARA ATLAS
             if (encontro == false) {
                 if (contenido.contains("Atlas") || contenido.contains("Atlas, S.A.") || contenido.contains("Seguros Atlas, S.A.")) {
                	 AtlasModel datosAtlas = new AtlasModel(pdfStripper, pdDoc, contenido);
                     modelo = datosAtlas.procesar();
                     encontro = true;
                 }
             }

			 // ENTRADA PARA SISNOVA
			 if (encontro == false) {
			  contenido = rangoSimple(1, 3, pdfStripper, pdDoc);              
			  if (contenido.contains("Servicios Integrales de Salud Nova") || contenido.contains("www.sisnova.com.mx")) {
			    	  SisnovaModel datosSisnova = new SisnovaModel(pdfStripper, pdDoc, contenido);
			    	  modelo = datosSisnova.procesar();
			          encontro = true;
			      }
			  }
			 
			 // ENTRADA PARA MULTIVA
	            if (encontro == false) {
	                if (contenido.indexOf("Seguros Multiva") > -1 || contenido.indexOf("Multiva Seguros") > -1) {
	                	MultivaModels datosMultiva = new MultivaModels(pdfStripper, pdDoc, contenido);
	                	modelo = datosMultiva.procesar();
	                    encontro = true;
	                }
	            }
             

             if (encontro == false) {
                 // VALIDACION AL NO RECONOCER DE QUE CIA SE TRATA EL PDF					
            		modelo.setError(IdentificaPolizaService.this.getClass().getTypeName() +" | " +"No se logró identificar el PDF.");;
             }
        	
        	
            pdDoc.close();
     
            return modelo;
		} catch (Exception ex) {
			modelo.setError(IdentificaPolizaService.this.getClass().getTypeName() +" - catch:" + ex.getMessage() + " | " + ex.getCause());;
	       return modelo;
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
    public String rangoSimple(int inicio, int fin, PDFTextStripper pdfStripper, PDDocument pdDoc) throws IOException { //DEVUELVE UN RANGO DE PAGINAS
        pdfStripper.setStartPage(inicio);
        pdfStripper.setEndPage(fin);
        return pdfStripper.getText(pdDoc);
    }
}
