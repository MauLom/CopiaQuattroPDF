package com.copsis.clients;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import com.copsis.clients.projections.QuattroUploadAdjuntoProjection;
import com.copsis.controllers.forms.AdjuntoForm;
import com.copsis.exceptions.GeneralServiceException;

@Service
public class QuattroUploadClient {

	@Value("${serviceUrl.quattro-upload.baseURL}")
	private String QUATTRO_UPLOAD_PROXY;



	public QuattroUploadAdjuntoProjection getUploadAndAdjuntoByteArray(AdjuntoForm adjuntoForm, HttpHeaders headers) {
		try {
			headers.setContentType(MediaType.MULTIPART_FORM_DATA);
			MultiValueMap<String, String> fileMap = new LinkedMultiValueMap<>();
			byte[] someByteArray = Base64.getDecoder().decode(adjuntoForm.getB64());
			ContentDisposition contentDisposition = ContentDisposition.builder("form-data").name("file")
					.filename(adjuntoForm.getNombreOriginal() + ".pdf").build();
			fileMap.add(HttpHeaders.CONTENT_DISPOSITION, contentDisposition.toString());
			HttpEntity<byte[]> fileEntity = new HttpEntity<>(someByteArray, fileMap);

			MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
			body.add("file", fileEntity);
			body.add("folder", adjuntoForm.getFolder());
			body.add("bucket", adjuntoForm.getBucket());
			body.add("concepto", adjuntoForm.getConcepto());
			if(adjuntoForm.getAdjuntoID() > 0) {
				body.add("adjuntoID", adjuntoForm.getAdjuntoID());
			}
			if(adjuntoForm.getClienteID() > 0) {
				body.add("clienteID", adjuntoForm.getClienteID());
			}
			if(adjuntoForm.getPolizaID() > 0) {
				body.add("polizaID", adjuntoForm.getPolizaID());
			}
			if(adjuntoForm.getEndosoID() > 0) {
				body.add("endosoID", adjuntoForm.getEndosoID());
			}
			if(adjuntoForm.getReciboID() > 0) {
				body.add("reciboID", adjuntoForm.getReciboID());
			}
			if(adjuntoForm.getPagoID() > 0) {
				body.add("pagoID", adjuntoForm.getPagoID());
			}
			if(adjuntoForm.getEdoctaVendedorID() > 0) {
				body.add("edoctaVendedorID", adjuntoForm.getEdoctaVendedorID());
			}
			if(adjuntoForm.getAseguradoID() > 0) {
				body.add("aseguradoID", adjuntoForm.getAseguradoID());
			}
			if(adjuntoForm.getPolizaAseguradoID() > 0) {
				body.add("polizaAseguradoID", adjuntoForm.getPolizaAseguradoID());
			}
			if(adjuntoForm.getVehiculoID() > 0) {
				body.add("vehiculoID", adjuntoForm.getVehiculoID());
			}
			if(adjuntoForm.getPolizaVehiculoID() > 0) {
				body.add("polizaVehiculoID", adjuntoForm.getPolizaVehiculoID());
			}
			if(adjuntoForm.getCrmID() > 0) {
				body.add("crmID", adjuntoForm.getCrmID());
			}
			if(adjuntoForm.getCotizacionID() > 0) {
				body.add("cotizacionID", adjuntoForm.getCotizacionID());
			}
			if(adjuntoForm.getProspectID() > 0) {
				body.add("prospectoID", adjuntoForm.getProspectID());
			}
			if(adjuntoForm.getEmailID() > 0) {
				body.add("emailID", adjuntoForm.getEmailID());
			}
			if(adjuntoForm.getVendedorID() > 0) {
				body.add("vendedorID", adjuntoForm.getVendedorID());
			}
			if(adjuntoForm.getAseguradoraID() > 0) {
				body.add("aseguradoraID", adjuntoForm.getAseguradoraID());
			}
			if(adjuntoForm.getComisionID() > 0) {
				body.add("comisionID", adjuntoForm.getComisionID());
			}
			if(adjuntoForm.getConciliacionID() > 0) {
				body.add("conciliacionID", adjuntoForm.getConciliacionID());
			}
			if(adjuntoForm.getSiniestroID() > 0) {
				body.add("siniestroID", adjuntoForm.getSiniestroID());
			}
			if(adjuntoForm.getEntidadID() > 0) {
				body.add("entidadID", adjuntoForm.getEntidadID());
			}
			if(adjuntoForm.getEntidadTipo() > 0) {
				body.add("entidadTipo", adjuntoForm.getEntidadTipo());
			}
			if(adjuntoForm.getTipo() > 0) {
				body.add("tipo", adjuntoForm.getTipo());
			}
			if(adjuntoForm.getPrivado() > 0) {
				body.add("privado", adjuntoForm.getPrivado());
			}
			if(adjuntoForm.getWebpath() != null && !adjuntoForm.getWebpath().equals("")) {
				body.add("webpath", adjuntoForm.getWebpath());
			}
			if(adjuntoForm.getNombreOriginal() != null && !adjuntoForm.getNombreOriginal().equals("")) {
				body.add("nombreOriginal", adjuntoForm.getNombreOriginal());;
			}
			if(adjuntoForm.getD() != null && !adjuntoForm.getD().equals("")) {
				body.add("d", adjuntoForm.getD());	
			}
			System.out.println("body => " + body);
			HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
			RestTemplate restTemplate = new RestTemplate();
			ResponseEntity<QuattroUploadAdjuntoProjection> response = restTemplate
					.postForEntity(QUATTRO_UPLOAD_PROXY + "/upload/adjunto", requestEntity, QuattroUploadAdjuntoProjection.class);
			return response.getBody();
		} catch (Exception e) {
			throw new GeneralServiceException("00009", "Hubo un error en el proceso: " + e.getMessage());
		}
	}
}
