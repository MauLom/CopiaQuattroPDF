package com.copsis.clients;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.copsis.controllers.forms.DatosSatForm;
import com.copsis.exceptions.ValidationServiceException;
import com.copsis.models.CopsisResponse;
import com.copsis.utils.ErrorCode;

@Service
public class QuattroExternalApiClient {

	@Value("${serviceUrl.quattro-external-api.baseURL}")
	private String quattroExternalApiProxy;

	public CopsisResponse extraeDatosPaginaSat(DatosSatForm datosSatForm) {
		try {
			RestTemplate restTemplate = new RestTemplate();
			HttpEntity<DatosSatForm> httpEntity = new HttpEntity<>(datosSatForm);
			
			String url = quattroExternalApiProxy.concat("/external/linkCIF");
			return restTemplate.exchange(url, HttpMethod.POST, httpEntity, new ParameterizedTypeReference<CopsisResponse>() {}).getBody();
		} catch (Exception e) {
			throw new ValidationServiceException(ErrorCode.MSJ_ERROR_00000 + " | " + e.getMessage());
		}
	}
}
