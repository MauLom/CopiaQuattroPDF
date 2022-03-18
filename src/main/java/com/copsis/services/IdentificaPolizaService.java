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
import com.copsis.models.afirme.AfirmeModel;
import com.copsis.models.aig.AigModel;
import com.copsis.models.allians.AlliansModel;
import com.copsis.models.ana.AnaModel;
import com.copsis.models.argos.ArgosModel;
import com.copsis.models.atlas.AtlasModel;
import com.copsis.models.axa.AxaModel;
import com.copsis.models.banorte.BanorteModel;
import com.copsis.models.bexmas.BexmasModel;
import com.copsis.models.chubb.ChubbModel;
import com.copsis.models.gmx.GmxModel;
import com.copsis.models.gnp.GnpModel;
import com.copsis.models.hdi.HdiModel;
import com.copsis.models.inbursa.InbursaModel;
import com.copsis.models.insignia.InsigniaModel;
import com.copsis.models.mapfre.MapfreModel;
import com.copsis.models.metlife.MetlifeModel;
import com.copsis.models.multiva.MultivaModels;
import com.copsis.models.planSeguro.PlanSeguroModel;
import com.copsis.models.potosi.PotosiModel;
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

	private static final String MONTERREY = "Monterrey";
	private static final String URL_ANASEGUROS = "www.anaseguros.com.mx";

	public EstructuraJsonModel queIndentificaPoliza(PdfForm pdfForm) throws IOException {

		EstructuraJsonModel modelo = new EstructuraJsonModel();
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
			String contenidoAux = "";		
			boolean encontro = false;
	      
			// CHUBB
			if (!encontro && contenido.contains("Chubb")) {
				ChubbModel datosChubb = new ChubbModel();
				datosChubb.setPdfStripper(pdfStripper);
				datosChubb.setPdDoc(pdDoc);
				datosChubb.setContenido(contenido);
				modelo = datosChubb.procesa();
				encontro = true;
			}

		
			// ENTRADA PARA QUALITAS
			if (!encontro && contenido.contains("qualitas")  || rangoSimple(5, 6, pdfStripper, pdDoc).contains("qualitas")) {
				if(!contenido.contains("qualitas")) {
					contenido = rangoSimple(5, 6, pdfStripper, pdDoc);
				}
				QualitasModel datosQualitas = new QualitasModel(pdfStripper, pdDoc, contenido);
				modelo = datosQualitas.procesa();
				encontro = true;
			}
			
			if (!encontro && !rangoSimple(2, 4, pdfStripper, pdDoc).contains("Seguros el Potosí S.A") && (contenido.contains("visite gnp.com.mx") || contenido.contains("GNP")
					|| contenido.contains("Grupo Nacional Provincial S.A.B")
					|| contenido.contains("Grupo Nacional Provincial")  || rangoSimple(2, 4, pdfStripper, pdDoc).contains("GNP") 					
					|| contenido.contains("gnp.com.mx")|| rangoSimple(2, 4, pdfStripper, pdDoc).contains("gnp.com.mx") )) {

				GnpModel datosGnp = new GnpModel(pdfStripper, pdDoc, contenido);
				modelo = datosGnp.procesa();
				encontro = true;
			}

			// ENTRADA PARA MAPFRE
			if (!encontro && contenido.length() > 502 && contenido.indexOf("MAPFRE") > -1
					|| contenido.contains("Mapfre Tepeyac")) {
				contenidoAux = rangoSimple(1, 2, pdfStripper, pdDoc);
				MapfreModel datosmapfre = new MapfreModel(pdfStripper, pdDoc, contenidoAux);
				modelo = datosmapfre.procesa();
				encontro = true;

			}

			// ENTRADA PARA SEGUROS MONTERREY
			if (!encontro) {
				
				if (contenido.contains("Seguros a\r\n" + MONTERREY) || contenido.contains("Seguros Monterrey")
						|| contenido.contains("Seguros a Monterrey") || contenido.contains("@@@Seguros a\n" + MONTERREY)
						|| contenido.contains("COLECTIVO EMPRESARIAL")) {

					SegurosMtyModel datosSegurosMty = new SegurosMtyModel(pdfStripper, pdDoc, contenido);
					modelo = datosSegurosMty.procesa();
					encontro = true;
				} else {
					contenidoAux = rangoSimple(2, 4, pdfStripper, pdDoc);
					

					if (contenidoAux.contains("Seguros a\r\n" + MONTERREY) || contenidoAux.contains("Seguros Monterrey")
							|| contenidoAux.contains("Seguros a Monterrey")
							|| contenidoAux.contains("@@@Seguros a\n" + MONTERREY)
							|| contenidoAux.contains("SEGUROS MONTERREY")) {
						SegurosMtyModel datosSegurosMty = new SegurosMtyModel(pdfStripper, pdDoc, contenido);
						modelo = datosSegurosMty.procesa();
						encontro = true;
					}
				}
			}

			// ENTRADA PARA AXA
			if (!encontro && contenido.contains("AXA Seguros, S.A. de C.V.")
					|| contenido.contains("AXA SEGUROS, S.A. DE C.V")
					|| contenido.contains("AXA Seguros, S.A de C.V.")) {
				AxaModel datosAxa = new AxaModel(pdfStripper, pdDoc, contenido);
				modelo = datosAxa.procesa();
				encontro = true;
			}

			// ENTRADA PARA ABA
			if (!encontro && (contenido.contains("ABA Seguros S.A.")
					|| contenido.contains("Datos del asegurado y/o propietario")
							&& contenido.contains("Domicilio del bien asegurado")
							&& contenido.contains("Descripcion del Riesgo")
					|| contenido.contains("Datos del asegurado y/o propietario")
							&& contenido.contains("Características del riesgo"))) {
				AbaModel datosAba = new AbaModel();
				datosAba.setPdfStripper(pdfStripper);
				datosAba.setPdDoc(pdDoc);
				datosAba.setContenido(contenido);
				modelo = datosAba.procesa();
				encontro = true;

			}

			// ENTRADA PARA AFIRME
			if (!encontro) {

				contenidoAux = rangoSimple(1, 3, pdfStripper, pdDoc);

				if (contenido.contains("AFIRME GRUPO FINANCIERO") || contenido.contains("Afirme Grupo Financiero")
						|| contenido.contains("www.afirme.com")) {
					AfirmeModel datosAfirme = new AfirmeModel(pdfStripper, pdDoc, contenido);
					modelo = datosAfirme.procesar();
					encontro = true;
				} else {
					if (contenidoAux.contains("AFIRME GRUPO FINANCIERO")
							|| contenidoAux.contains("Afirme Grupo Financiero") || contenidoAux.contains("www.afirme.com")) {					
						AfirmeModel datosAfirme = new AfirmeModel(pdfStripper, pdDoc, contenidoAux);
						modelo = datosAfirme.procesar();
						encontro = true;
					}
				}
			}

			// ENTRADA PARA BANORTE
			if (!encontro && contenido.contains("Banorte") && !contenido.contains("Servicios Integrales de Salud Nova")
						|| (contenido.contains("DATOS DEL CONTRATANTE (Sírvase escribir con letra de molde)")
								&& contenido.contains("Datos del asegurado titular (Solicitante)")
								&& contenido.contains("ASEGURADOS"))) {
					if (contenido.contains("Estimado(a)") ) {
							BanorteModel datosBanort;						
							if(caratula(3, 4, pdfStripper, pdDoc).contains("AVISO DE COBRO")){						
								 datosBanort = new BanorteModel(pdfStripper, pdDoc, caratula(5, 7, pdfStripper, pdDoc));
							}else {
								 datosBanort = new BanorteModel(pdfStripper, pdDoc, caratula(3, 4, pdfStripper, pdDoc));
							}					
						modelo = datosBanort.procesar();
						encontro = true;
					} else  if(contenido.contains("AVISO DE COBRO") || contenido.contains("CARGOS AUTOMÁTICOS")) {
						contenido = caratula(1, 6, pdfStripper, pdDoc);
						BanorteModel datosBanort = new BanorteModel(pdfStripper, pdDoc, contenido);
						modelo = datosBanort.procesar();
						encontro = true;
					}
					else {

						BanorteModel datosBanort = new BanorteModel(pdfStripper, pdDoc, contenido);
						modelo = datosBanort.procesar();
						encontro = true;
					}
			}
		
			// ENTRADA PARA INBURSA
			if (!encontro && contenido.contains("Inbursa") || contenido.contains("INBURSA")) {
				InbursaModel datosInbursa = new InbursaModel(pdfStripper, pdDoc, contenido);
				modelo = datosInbursa.procesar();
				encontro = true;

			}
			// ENTRADA PARA METLIFE
			if (!encontro && contenido.contains("MetLife México S.A.")
					|| contenido.contains("www.metlife.com.mx") || contenido.contains("MetLife México")) {
				MetlifeModel datosMetlife = new MetlifeModel(pdfStripper, pdDoc, contenido);
				modelo = datosMetlife.procesar();
				encontro = true;
			}

			// ENTRADA PARA ATLAS
			if (!encontro && contenido.contains("Atlas") || contenido.contains("Atlas, S.A.")
					|| contenido.contains("Seguros Atlas, S.A.")) {
				AtlasModel datosAtlas = new AtlasModel(pdfStripper, pdDoc, contenido);
				modelo = datosAtlas.procesar();
				encontro = true;
			}

			// ENTRADA PARA SISNOVA
			if (!encontro) {
				contenido = rangoSimple(1, 3, pdfStripper, pdDoc);
				if (contenido.contains("Servicios Integrales de Salud Nova")
						|| contenido.contains("www.sisnova.com.mx")) {
					SisnovaModel datosSisnova = new SisnovaModel(pdfStripper, pdDoc, contenido);
					modelo = datosSisnova.procesar();
					encontro = true;
				}
			}

			// ENTRADA PARA MULTIVA
			if (!encontro && contenido.indexOf("Seguros Multiva") > -1 || contenido.indexOf("Multiva Seguros") > -1) {
				MultivaModels datosMultiva = new MultivaModels(pdfStripper, pdDoc, contenido);
				modelo = datosMultiva.procesar();
				encontro = true;
			}

			// ENTRADA PARA ZURICH
			if (!encontro && contenido.contains("zurich") || contenido.contains("Zurich")) {
				ZurichModel datosZurich = new ZurichModel(pdfStripper, pdDoc, contenido);
				modelo = datosZurich.procesar();
				encontro = true;
			}

			// ENTRADA PARA GMX
			if (!encontro && contenido.contains("Grupo Mexicano de Seguros") || contenido.contains("GMX SEGUROS")) {
				GmxModel datosGmx = new GmxModel(pdfStripper, pdDoc, contenido);
				modelo = datosGmx.procesar();
				encontro = true;
			}

			// ENTRADA PARA AIG
			if (!encontro && contenido.contains("AIG Seguros") || contenido.contains("www.aig.com.mx")) {
				// www.AIG.com.mx
				AigModel datosAig = new AigModel(pdfStripper, pdDoc, contenido);
				modelo = datosAig.procesar();
				encontro = true;
			}

			// ENTRADA PARA HDI
			if (!encontro && contenido.contains("HDI Seguros, S.A. de C.V.")
					|| contenido.contains("HDI Seguros, S.A. de C.V.")
					|| contenido.indexOf("@@@HDI Seguros, S.A de C.V.") > -1
					|| contenido.contains("@@@HDI Seguros S.A. de C.V.,")) {
				HdiModel datosHdi = new HdiModel(pdfStripper, pdDoc, contenido);
				modelo = datosHdi.procesar();
				encontro = true;
			}


			// ENTRADA PARA SURA
			if (!encontro && contenido.contains("Seguros SURA S.A.")
					|| contenido.contains("Royal & SunAlliance Seguros")
					|| contenido.contains("Seguros SURA S.A. de C.V.")
					|| contenido.contains("Seguros SURA")
					|| contenido.contains("@@@Seguros SURA S.A. de C.V.") || contenido.contains("SURA S.A.")) {
				SuraModel datosSura = new SuraModel(pdfStripper, pdDoc, contenido);
				modelo = datosSura.procesar();
				encontro = true;
			}

			// ENTRADA PARA PRIMERO SEGUROS
			if (!encontro && contenido.contains("PRIMERO SEGUROS S.A. de C.V.")
					|| contenido.contains("Primero Seguros S.A. De C.V.")) {
				PrimeroModel datosPrimero = new PrimeroModel(pdfStripper, pdDoc, contenido);
				modelo = datosPrimero.procesar();
				encontro = true;
			}

			// ARGOS
			if (!encontro && contenido.contains("Seguros Argos, S.A. de C.V.")) {
				ArgosModel datosArgos = new ArgosModel(pdfStripper, pdDoc, contenido);
				modelo = datosArgos.procesar();
				encontro = true;
			}

			// ENTRADA PARA ANA
			if (!encontro && (contenido.contains("ANA COMPAÑÍA DE SEGUROS") && contenido.contains(URL_ANASEGUROS))
					|| (contenido.contains("A.N.A. COMPAÑÍA") && contenido.contains(URL_ANASEGUROS))
					|| (contenido.contains("A.N.A. Compañía de Seguros") && contenido.contains(URL_ANASEGUROS))
					|| (contenido.contains("ANA COMPAÑÍA DE SEGUROS"))) {
				AnaModel datosAna = new AnaModel(pdfStripper, pdDoc, contenido);
				modelo = datosAna.procesar();
				encontro = true;
			}
			
			
		
		    if (!encontro && contenido.contains("Seguros el Potosí S.A.")){		    			    		                
                	PotosiModel datospotosi = new PotosiModel(pdfStripper, pdDoc, contenido);
                	modelo = datospotosi.procesar();
                    encontro = true;                
            }
		    

			// ENTRADA PARA VEXMAS
            if (! encontro &&( contenido.contains("Seguros Ve Por")
                        || contenido.contains("Seguros Ve por Más, S.A.")
                        || contenido.contains("Seguros Ve por Más, S. A.")
                        || contenido.contains("Seguros Ve Por Más")
                        || contenido.contains("www.vepormas.com"))) {//Ve por Más 
            	BexmasModel datosVeporMas = new BexmasModel(pdfStripper, pdDoc, contenido);
                    modelo = datosVeporMas.procesar();
                    encontro = true;
                
            }
           
            // ENTRADA PARA ALLIANZ
            if (!encontro && (contenido.contains("Allianz México")
                        || contenido.contains("www.allianz.com.mx")
                        || contenido.contains("MERCADO MEDIANO") //Plan Daños
                        || (contenido.contains("En cumplimiento a lo dispuesto") && contenido.contains("Nombre del Asegurado"))
                        || (contenido.contains("COBERTURAS CONTRATADAS") && contenido.contains("APORTACIONES COMPROMETIDAS") && contenido.contains("En el caso de que se desee nombrar beneficiarios a menores de edad")))) {
                	AlliansModel datosAllianz = new AlliansModel(pdfStripper, pdDoc, contenido);
                	modelo = datosAllianz.procesar();
                    encontro = true;
                
            }
            
            

            //ENTRADA PARA INSIGNIA
            if(!encontro && (rangoSimple(1, 4, pdfStripper, pdDoc).contains("www.insignialife.com") 
            || rangoSimple(1, 4, pdfStripper, pdDoc).contains("Insignia Life"))) {
            
            	contenido =rangoSimple(1, 4, pdfStripper, pdDoc);
            	
            	InsigniaModel insigniaModel = new InsigniaModel(pdfStripper, pdDoc, contenido);
            	modelo = insigniaModel.procesar();
                encontro = true;
            
            }

            // ENTRADA PARA PLAN SEGURO
            
   
                if (!encontro && contenido.contains("Plan Seguro")) {
            
                	PlanSeguroModel datosPlan = new PlanSeguroModel(pdfStripper, pdDoc, contenido);
                    modelo = datosPlan.procesar();
                    encontro = true;
                }
            



			if (!encontro) {
				// VALIDACION AL NO RECONOCER DE QUE CIA SE TRATA EL PDF
				modelo.setError(IdentificaPolizaService.this.getClass().getTypeName() + " | "
						+ "No se logró identificar el PDF.");
			}
		
			pdDoc.close();

			documentToBeParsed.close();
			cosDoc.close();
			pdDoc.close();

			return modelo;
		} catch (Exception ex) {
			modelo.setError(IdentificaPolizaService.this.getClass().getTypeName() + " - catch:" + ex.getMessage()
					+ " | " + ex.getCause());
			return modelo;
		}
	}

	// Metodo agrega separaciones de texto y inicios de parrafo.
	public String caratula(int inicio, int fin, PDFTextStripper stripper, PDDocument doc) throws IOException { // DEVUELVE
																												// UN
																												// CONTENIDO
																												// DE UN
																												// RANGO
																												// DE
																												// PAGINAS
		stripper.setStartPage(inicio);
		stripper.setEndPage(fin);
		stripper.setParagraphStart("@@@");
		stripper.setWordSeparator("###");
		stripper.setSortByPosition(true);
		return stripper.getText(doc);
	}

	public String rangoSimple(int inicio, int fin, PDFTextStripper pdfStripper, PDDocument pdDoc) throws IOException { // DEVUELVE
																														// UN
																														// RANGO
																														// DE
																														// PAGINAS
		pdfStripper.setStartPage(inicio);
		pdfStripper.setEndPage(fin);
		return pdfStripper.getText(pdDoc);
	}
}
