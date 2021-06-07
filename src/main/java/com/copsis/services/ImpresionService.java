package com.copsis.services;

import java.util.Date;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import com.copsis.clients.QuattroUploadClient;
import com.copsis.controllers.forms.AdjuntoForm;
import com.copsis.controllers.forms.ImpresionForm;
import com.copsis.dto.AdjuntoDTO;
import com.copsis.encryptor.SiO4EncryptorAES;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ImpresionService {

	@Autowired
	private QuattroUploadClient quattroUploadClient;

	public AdjuntoDTO ImpresionServicePdf(ImpresionForm impresionForm, HttpHeaders headers) {
		ImpresioneTipoService impresioneTipoService = new ImpresioneTipoService(impresionForm);
		AdjuntoForm adjuntoForm = new AdjuntoForm();
		String folder = "h11fia";
		String bucket = "quattrocrm-copsis";
		AdjuntoDTO adjuntoDTO = new AdjuntoDTO();

		if (impresionForm.getTipoImpresion() == 100 && impresionForm.getSiniestroDocumentoID() > 0) {// Esto solo
																										// aplicara para
																										// impresion 100
			adjuntoForm.setEntidadID(impresionForm.getSiniestroDocumentoID());
			adjuntoForm.setEntidadTipo(21);
		}

		byte[] byteArrayPDF = impresioneTipoService.getByteArrayPDF();

		switch (impresionForm.getTiporespuesta()) {
		case 1:
			Date date = new Date();
			Integer year = date.getYear() + 1900;
			String mes = (date.getMonth() + 1) + "";
			if (mes.length() == 1) {
				mes = "0" + mes;
			}
			String fecha = year.toString().substring(2, 4) + mes;
			String nombrePdf = "";
			if(impresionForm.getNombreOriginal() != null && !impresionForm.getNombreOriginal().equals("")) {
				nombrePdf = impresionForm.getNombreOriginal();
			} else {
				nombrePdf = SiO4EncryptorAES.encrypt("Consolidado_" + date,
						com.copsis.encryptor.utils.Constants.ENCRYPTION_KEY);
			}
			adjuntoForm.setB64(Base64.encodeBase64String(byteArrayPDF));
			adjuntoForm.setFolder(folder);
			adjuntoForm.setBucket(bucket);
			adjuntoForm.setNombreOriginal(nombrePdf.length() > 50? nombrePdf.substring(0,50):nombrePdf);
			adjuntoForm.setConcepto(nombrePdf.length() > 50? nombrePdf.substring(0,50):nombrePdf);
			adjuntoForm.setD(impresionForm.getD());
			adjuntoDTO = quattroUploadClient.getUploadAndAdjuntoByteArray(adjuntoForm, headers).getResult();

			break;

		default:

			break;
		}

		return adjuntoDTO;

	}

}
