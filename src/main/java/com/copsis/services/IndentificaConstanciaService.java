package com.copsis.services;

import java.io.IOException;
import java.net.URL;

import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.copsis.clients.QuattroExternalApiClient;
import com.copsis.clients.QuattroUtileriasApiClient;
import com.copsis.controllers.forms.DatosSatForm;
import com.copsis.controllers.forms.PdfNegocioForm;
import com.copsis.controllers.forms.PdfForm;
import com.copsis.models.CardSettings;
import com.copsis.models.CopsisResponse;
import com.copsis.models.EstructuraConstanciaSatModel;
import com.copsis.models.constancia.ConstanciaModel;
import com.copsis.models.constancia.ConstanciaSatModel;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Service
@Slf4j
public class IndentificaConstanciaService {
	private final ConstanciaModel constanciaModel;
	private final WebhookService webhookService;
	private String errores = "";

	@Autowired
	private QuattroUtileriasApiClient quattroUtileriasApiClient;
	@Autowired
	private QuattroExternalApiClient quattroExternalApiClient;

	public EstructuraConstanciaSatModel indentificaConstancia(PdfForm pdfForm) throws IOException {

		EstructuraConstanciaSatModel constancia = new EstructuraConstanciaSatModel();
		PDDocument documentToBeParsed = null;
		COSDocument cosDoc = null;
		PDDocument pdDoc = null;

		try {

			final URL scalaByExampleUrl = new URL(pdfForm.getUrl());
			documentToBeParsed = PDDocument.load(scalaByExampleUrl.openStream());
			PDFTextStripper pdfStripper = new PDFTextStripper();
			cosDoc = documentToBeParsed.getDocument();
			pdDoc = new PDDocument(cosDoc);
			pdfStripper.setStartPage(1);
			pdfStripper.setEndPage(1);
			String contenido = pdfStripper.getText(pdDoc);
			if (contenido.contains("CONSTANCIA DE SITUACIÓN FISCAL") || contenido.contains("CÉDULA DE IDENTIFICACIÓN FISCAL ")) {
				constancia = constanciaModel.procesar(pdfStripper, pdDoc, contenido, pdfForm);
				return constancia;
			}

			String errorMessage = "Documento de tipo no reconocido.";

			sendWebhookMessage(pdfForm, errorMessage);

			constancia.setError(errorMessage);
			return constancia;

		} catch (IOException e) {
			sendWebhookMessage(pdfForm, e.getMessage());
			constancia.setError(IndentificaConstanciaService.this.getClass().getTypeName() + " | " + e.getMessage() + " | " + e.getCause());
			return constancia;
		} catch (Exception ex) {
			sendWebhookMessage(pdfForm, ex.getMessage());
			constancia.setError(IndentificaConstanciaService.this.getClass().getTypeName() + " | " + ex.getMessage() + " | " + ex.getCause());
			return constancia;
		} finally {
			if(documentToBeParsed != null) documentToBeParsed.close();
			if(cosDoc != null) cosDoc.close();
			if(pdDoc != null) pdDoc.close();

		}

	}

	private void sendWebhookMessage(PdfForm pdfForm, String errorMessage) {
		try {
			CardSettings cardSettings = CardSettings.builder()
					.fileUrl(pdfForm.getUrl())
					.sourceClass(IndentificaConstanciaService.class.getName())
					.exceptionMessage(errorMessage)
					.build();
			webhookService.send(cardSettings);
		} catch (Exception e) {
			// do nothing
		}
	}

	public EstructuraConstanciaSatModel negocioValidaDatosFiscales(PdfNegocioForm pdfNegocioForm) {
		
		EstructuraConstanciaSatModel estructuraConstanciaSatModel = pdfNegocioForm.getEstructuraConstanciaSatModel();
		try {

			switch (pdfNegocioForm.getTipoValidacion()) {
			case 1: // Valida datos CFDI[Constancia Situacion Fiscal]
				if (!validaciones(estructuraConstanciaSatModel).equals("")) {
					// intentamos con convertir PDF a imagen JPG, leemos QR, leemos pagina del SAT,
					// llenamos modelo y validamos información nuevamente
					errores = "";
					//Llenamos Form
					DatosSatForm datosSatForm = new DatosSatForm();
					datosSatForm.setUrl(pdfNegocioForm.getUrl());
					CopsisResponse copsisResponse;
					try {
						// extrae url de QR que esta en la constancia
						copsisResponse = quattroUtileriasApiClient.getExtraeUrl(datosSatForm);
					} catch (Exception ex) {
						throw ex;
					}
					
					//Llenamos Form con la nueva info[URL SAT]
					datosSatForm.setUrl((String) copsisResponse.getResult());
					CopsisResponse copsisResponseExternal;
					
					try {
						// Va a formar estructura con datos de pagina
						copsisResponseExternal = quattroExternalApiClient.extraeDatosPaginaSat(datosSatForm);
					} catch (Exception ex) {
						throw ex;
					}
					estructuraConstanciaSatModel = (EstructuraConstanciaSatModel) copsisResponseExternal.getResult();

					// Valida estructura
					if (validaciones(estructuraConstanciaSatModel).equals("")) {
						// retornamos
						return estructuraConstanciaSatModel;
					} else {
						PdfForm pdfForm = new PdfForm();
						pdfForm.setUrl(pdfNegocioForm.getUrl());
						estructuraConstanciaSatModel.setError(errores);
						sendWebhookMessage(pdfForm, errores);
						return estructuraConstanciaSatModel;
					}
				}

			default:
				break;
			}
			return estructuraConstanciaSatModel;
		} catch (Exception ex) {
			PdfForm pdfForm = new PdfForm();
			pdfForm.setUrl(pdfNegocioForm.getUrl());
			sendWebhookMessage(pdfForm, ex.getMessage());
			throw ex;
		}
	}

	private String validaciones(EstructuraConstanciaSatModel estructuraConstanciaSatModel) {
		try {
			String respuesta = "";
			if (estructuraConstanciaSatModel.getTipoPersona().equals("")) {
				errores = "Tipo persona";
			} else {
				if (estructuraConstanciaSatModel.getTipoPersona().equals("Física")) {
					validacionPersonaFisica(estructuraConstanciaSatModel);
				}

				if (estructuraConstanciaSatModel.getTipoPersona().equals("Moral")) {
					validacionPersonaMoral(estructuraConstanciaSatModel);
				}
			}
			// datos adicionales
			if (estructuraConstanciaSatModel.getCp().equals("")) {
				capturaErrores("Código postal");
			}
			if (estructuraConstanciaSatModel.getRfc().equals("")) {
				capturaErrores("Rfc");
			}
			if (!errores.equals("")) {
				respuesta = "No fué posible leer alguno de los datos:".concat(errores);
			}
			return respuesta;
		} catch (Exception e) {
			throw e;
		}
	}

	private void validacionPersonaFisica(EstructuraConstanciaSatModel estructuraConstanciaSatModel) {
		if (estructuraConstanciaSatModel.getNombre().equals("")) {
			capturaErrores("nombre(s)");
		}
		if (estructuraConstanciaSatModel.getApellidoP().equals("")) {
			capturaErrores("apellído paterno");
		}
	}

	private void validacionPersonaMoral(EstructuraConstanciaSatModel estructuraConstanciaSatModel) {
		try {
			if (estructuraConstanciaSatModel.getRazonSocial().equals("")) {
				capturaErrores("Razón Social");
			}
			if (estructuraConstanciaSatModel.getRegimenDeCapital().equals("")) {
				capturaErrores("Régimen Capital");
			}
		} catch (Exception e) {
			throw e;
		}
	}

	private void capturaErrores(String error) {
		if (errores.equals("")) {
			errores.concat(error);
		} else {
			errores.concat(", ").concat(error);
		}
	}
}
