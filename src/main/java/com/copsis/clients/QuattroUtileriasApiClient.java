package com.copsis.clients;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.copsis.clients.projections.QuattroUtileriasApiQrProjection;
import com.copsis.controllers.forms.DatosSatForm;
import com.copsis.exceptions.ValidationServiceException;
import com.copsis.utils.ErrorCode;

@Service
public class QuattroUtileriasApiClient {

	@Value("${serviceUrl.quattro-utilerias-api.baseURL}")
	private String quattroUtileriasApiProxy;

	public QuattroUtileriasApiQrProjection getExtraeUrl(DatosSatForm datosSatForm) {
		try {
			
			RestTemplate restTemplate = new RestTemplate();
			HttpHeaders httpHeaders = new HttpHeaders();
			HttpEntity<DatosSatForm> httpEntity = new HttpEntity<>(datosSatForm,httpHeaders);			
			String url = quattroUtileriasApiProxy.concat("/utilerias-api/lecturaQR");

			return restTemplate.exchange(url, HttpMethod.POST, httpEntity, QuattroUtileriasApiQrProjection.class).getBody();
			
		} catch (Exception e) {
			throw new ValidationServiceException(ErrorCode.MSJ_ERROR_00002, e.getMessage());
		}
	}
}
