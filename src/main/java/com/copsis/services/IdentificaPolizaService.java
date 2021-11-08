package com.copsis.services;


import java.io.IOException;
import java.net.URL;

import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import com.copsis.controllers.forms.PdfForm;
import com.copsis.models.EstructuraJsonModel;
import com.copsis.models.ImportacionValidacionModel;
import com.copsis.models.aba.AbaModel;
import com.copsis.models.afirme.AfirmeModel;
import com.copsis.models.aig.AigModel;
import com.copsis.models.ana.AnaModel;
import com.copsis.models.argos.ArgosModel;
import com.copsis.models.atlas.AtlasModel;
import com.copsis.models.axa.AxaModel;
import com.copsis.models.banorte.BanorteModel;
import com.copsis.models.chubb.ChubbModel;
import com.copsis.models.gmx.GmxModel;
import com.copsis.models.gnp.GnpModel;
import com.copsis.models.hdi.HdiModel;
import com.copsis.models.inbursa.InbursaModel;
import com.copsis.models.mapfre.MapfreModel;
import com.copsis.models.metlife.MetlifeModel;
import com.copsis.models.multiva.MultivaModels;
import com.copsis.models.primero.PrimeroModel;
import com.copsis.models.qualitas.QualitasModel;
import com.copsis.models.segurosMty.SegurosMtyModel;
import com.copsis.models.sisnova.SisnovaModel;
import com.copsis.models.sura.SuraModel;
import com.copsis.models.zurich.ZurichModel;

import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
@Service
public class IdentificaPolizaService {


	private EstructuraJsonModel modelo = new EstructuraJsonModel();
	 
    public EstructuraJsonModel queIndentificaPoliza( PdfForm pdfForm,HttpHeaders headers) throws  IOException {

        try {

         	 final URL scalaByExampleUrl = new URL(pdfForm.getUrl());
             final PDDocument documentToBeParsed = PDDocument.load(scalaByExampleUrl.openStream());
             PDFTextStripper pdfStripper = new PDFTextStripper();
             COSDocument cosDoc = documentToBeParsed.getDocument();
             PDDocument pdDoc = new PDDocument(cosDoc);
             pdfStripper.setStartPage(1);
             pdfStripper.setEndPage(1);
             pdfStripper.setParagraphStart("@@@");
             String contenido = pdfStripper.getText(pdDoc);
             String contenidoAux="";

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
                    	 contenidoAux = rangoSimple(1, 2, pdfStripper, pdDoc);
                    	 
                    	 MapfreModel datosmapfre = new MapfreModel(pdfStripper, pdDoc, contenidoAux);
                    	 modelo = datosmapfre.procesa();
                         encontro = true;
                     }
                 }
             }
             
             // ENTRADA PARA SEGUROS MONTERREY
             if (encontro == false) {
                 if (contenido.contains("Seguros a\r\n" + "Monterrey")
                         || contenido.contains("Seguros Monterrey")
                         || contenido.contains("Seguros a Monterrey")
                         || contenido.contains("@@@Seguros a\n" + "Monterrey")) {
                	 SegurosMtyModel datosSegurosMty = new SegurosMtyModel(pdfStripper, pdDoc, contenido);
                	 modelo = datosSegurosMty.procesa();
                     encontro = true;
                 } else if (contenido.contains("COLECTIVO EMPRESARIAL")) {
                	 SegurosMtyModel datosSegurosMty = new SegurosMtyModel(pdfStripper, pdDoc, contenido);
                	 modelo = datosSegurosMty.procesa();
                     encontro = true;
                 } else {

                     contenidoAux = rangoSimple(2, 4, pdfStripper, pdDoc);

                     if (contenidoAux.contains("Seguros a\r\n" + "Monterrey")
                             || contenidoAux.contains("Seguros Monterrey")
                             || contenidoAux.contains("Seguros a Monterrey")
                             || contenidoAux.contains("@@@Seguros a\n" + "Monterrey")
                             || contenidoAux.contains("SEGUROS MONTERREY")) {
                    	 SegurosMtyModel datosSegurosMty = new SegurosMtyModel(pdfStripper, pdDoc, contenido);
                    	 modelo = datosSegurosMty.procesa();
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
             
             // ENTRADA PARA AFIRME
             if (encontro == false) {
            	 
                 contenidoAux = rangoSimple(1, 2, pdfStripper, pdDoc);

                 if (contenido.contains("AFIRME GRUPO FINANCIERO") || contenido.contains("Afirme Grupo Financiero") || contenido.contains("www.afirme.com")) {
                	 AfirmeModel datosAfirme = new AfirmeModel(pdfStripper, pdDoc, contenido);
                	 modelo = datosAfirme.procesar();
                     encontro = true;
                 } else {
                     if (contenidoAux.contains("AFIRME GRUPO FINANCIERO") || contenidoAux.contains("Afirme Grupo Financiero")) {
                    	 AfirmeModel datosAfirme = new AfirmeModel(pdfStripper, pdDoc, contenido);
                    	 modelo = datosAfirme.procesar();
                         encontro = true;
                     }
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

	            
	            // ENTRADA PARA ZURICH
	            if (encontro == false) {
	            	
	                if (contenido.contains("zurich") || contenido.contains("Zurich")) {
	                	ZurichModel datosZurich = new ZurichModel(pdfStripper, pdDoc, contenido);
	                    modelo = datosZurich.procesar();
	                    encontro = true;
	                }
	            }
             

	            // ENTRADA PARA GMX
	            if (encontro == false) {
	                if (contenido.contains("Grupo Mexicano de Seguros") || contenido.contains("GMX SEGUROS")) {
	                	GmxModel datosGmx = new GmxModel(pdfStripper, pdDoc, contenido);
	                    modelo = datosGmx.procesar();
	                    encontro = true;
	                }
	            }
	            
	            // ENTRADA PARA AIG
	            if (encontro == false) {
	                if (contenido.contains("AIG Seguros") || contenido.contains("www.aig.com.mx")) { //www.AIG.com.mx
	                	AigModel datosAig = new AigModel(pdfStripper, pdDoc, contenido);
	                    modelo = datosAig.procesar();
	                    encontro = true;
	                }
	            }
	            
	            // ENTRADA PARA HDI
	            if (encontro == false) {
	                if (contenido.split("@@@")[1].contains("HDI Seguros, S.A. de C.V.")
	                        || contenido.split("@@@")[2].contains("HDI Seguros, S.A. de C.V.")
	                        || contenido.indexOf("@@@HDI Seguros, S.A de C.V.") > 0
	                        || contenido.contains("@@@HDI Seguros S.A. de C.V.,")) {
	                	HdiModel datosHdi = new HdiModel(pdfStripper, pdDoc, contenido);
	                	modelo = datosHdi.procesar();
	                    encontro = true;
	                }
	            }
	            
	            //ENTRADA PARA SURA
	            if (encontro == false) {
	                if (contenido.split("@@@")[3].contains("Seguros SURA S.A.")
	                        || contenido.contains("Royal & SunAlliance Seguros")
	                        || contenido.contains("Seguros SURA S.A. de C.V.")
	                        || contenido.contains("Seguros SURA S.A. de C.V.")
	                        || contenido.contains("@@@Seguros SURA S.A. de C.V.")) {
	                	SuraModel datosSura = new SuraModel(pdfStripper, pdDoc, contenido);
	                    modelo = datosSura.procesar();
	                    encontro = true;
	                }
	            }
	            
	            // ENTRADA PARA PRIMERO SEGUROS
	            if (encontro == false) {
	                if (contenido.contains("PRIMERO SEGUROS S.A. de C.V.")
	                        || contenido.contains("Primero Seguros S.A. De C.V.")) {
	                	PrimeroModel datosPrimero = new PrimeroModel(pdfStripper, pdDoc, contenido);
	                	modelo = datosPrimero.procesar();
	                    encontro = true;
	                }
	            }
	            
	            
	            // ARGOS
	            if (encontro == false) {
	                if (contenido.contains("Seguros Argos, S.A. de C.V.")) {
	                    ArgosModel datosArgos = new ArgosModel(pdfStripper, pdDoc, contenido);
	                    modelo = datosArgos.procesar();
	                    encontro = true;
	                }
	            }
	            
	            // ENTRADA PARA ANA
	            if (encontro == false) {
	                if ((contenido.contains("ANA COMPAÑÍA DE SEGUROS") && contenido.contains("www.anaseguros.com.mx"))
	                        || (contenido.contains("A.N.A. COMPAÑÍA") && contenido.contains("www.anaseguros.com.mx"))
	                        || (contenido.contains("A.N.A. Compañía de Seguros") && contenido.contains("www.anaseguros.com.mx")) || (contenido.split("@@@")[2].contains("ANA COMPAÑÍA DE SEGUROS"))) {
	                	AnaModel datosAna = new AnaModel(pdfStripper, pdDoc, contenido);
	                	modelo = datosAna.procesar();
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
