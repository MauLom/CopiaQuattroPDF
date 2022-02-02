package com.copsis.services;

import java.util.Date;

import org.apache.commons.codec.binary.Base64;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import com.copsis.clients.QuattroUploadClient;
import com.copsis.clients.projections.ImpresionReclamacionProjection;
import com.copsis.controllers.forms.AdjuntoForm;
import com.copsis.controllers.forms.AmortizacionPdfForm;
import com.copsis.controllers.forms.ImpresionForm;
import com.copsis.encryptor.SiO4EncryptorAES;
import com.copsis.exceptions.GeneralServiceException;
import com.copsis.exceptions.ValidationServiceException;
import com.copsis.models.impresion.ImpresionAmortizacionesPdf;
import com.copsis.models.impresion.ImpresionReclamacionPdf;
import com.copsis.utils.ErrorCode;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ImpresionService {

	private final QuattroUploadClient quattroUploadClient;

	public ImpresionForm impresionServicePdf(ImpresionForm impresionForm, HttpHeaders headers) {
		ImpresioneTipoService impresioneTipoService = new ImpresioneTipoService(impresionForm);
		AdjuntoForm adjuntoForm = new AdjuntoForm();

		if (impresionForm.getTipoImpresion() == 100 && impresionForm.getSiniestroDocumentoID() > 0) {
			adjuntoForm.setEntidadID(impresionForm.getSiniestroDocumentoID());
			adjuntoForm.setEntidadTipo(21);
		}
		byte[] byteArrayPDF = impresioneTipoService.getByteArrayPDF();
		
		if(impresionForm.getTiporespuesta() == 1) {
			Date date = new Date();

			String nombrePdf = "";
			if(impresionForm.getNombreOriginal() != null && !impresionForm.getNombreOriginal().equals("")) {
				nombrePdf = impresionForm.getNombreOriginal();
			} else {
				nombrePdf = SiO4EncryptorAES.encrypt("Consolidado_" + date,com.copsis.encryptor.utils.Constants.ENCRYPTION_KEY);
			}
			adjuntoForm.setB64(Base64.encodeBase64String(byteArrayPDF));
			adjuntoForm.setFolder(impresionForm.getFolder());
			adjuntoForm.setBucket(impresionForm.getBucket());
			adjuntoForm.setNombreOriginal(nombrePdf.length() > 50? nombrePdf.substring(0,50):nombrePdf);
			adjuntoForm.setConcepto(nombrePdf.length() > 50? nombrePdf.substring(0,50):nombrePdf);
			adjuntoForm.setD(impresionForm.getD());
			quattroUploadClient.getUploadAndAdjuntoByteArray(adjuntoForm, headers).getResult();

		} else {
			impresionForm.setUrls(null);
			impresionForm.setByteArrayPDF(byteArrayPDF);
		}
		
		return impresionForm;
	}
	
	public byte[] impresionAmortizacion(AmortizacionPdfForm amortizacionForm) {
		try {
			ImpresionAmortizacionesPdf impresionAmortizacionesPdf = new ImpresionAmortizacionesPdf();
			return impresionAmortizacionesPdf.buildPDF(amortizacionForm);
		} catch(ValidationServiceException e) {
			throw e;
		} catch(Exception ex) {
			throw new GeneralServiceException(ErrorCode.MSJ_ERROR_00000, ex.getMessage());
		}
	}
	
	public byte[] impresionReclamacion(ImpresionReclamacionProjection impresionReclamacionProjection) {
		try {
			ImpresionReclamacionPdf impresionReclamacionPdf = new ImpresionReclamacionPdf();
			return impresionReclamacionPdf.buildPDF(impresionReclamacionProjection);
			
		} catch(ValidationServiceException e) {
			throw e;
		} catch(Exception ex) {
			throw new GeneralServiceException(ErrorCode.MSJ_ERROR_00000, ex.getMessage());
		}
	}


	
	
	
	
}
