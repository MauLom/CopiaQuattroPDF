package com.copsis.clients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.copsis.clients.projections.CalculoRecibosCopsisResponse;
import com.copsis.controllers.forms.ParametrosCalculoReciboForm;
import com.copsis.exceptions.GeneralServiceException;

@Service
public class QuattroPolizaClient {
	
	@Value("${serviceUrl.quattro-poliza.baseURL}")
	private String QUATTRO_POLIZA_PROXY;
	private Object OBJECT = new Object();
	

	
	public CalculoRecibosCopsisResponse getRecibos(ParametrosCalculoReciboForm parametros,HttpHeaders headers) {
		try {
			RestTemplate restTemplate = new RestTemplate();
			HttpEntity<Object> httpEntity = new HttpEntity<>(parametros,headers);
			String url = QUATTRO_POLIZA_PROXY + "/poliza/recibo/calculaRecibos";
			return restTemplate.exchange(url, HttpMethod.POST, httpEntity, CalculoRecibosCopsisResponse.class).getBody();
			
		} catch (Exception e) {
			throw new GeneralServiceException("000001", e.getMessage());
		}
	}
	
	
	
}