/**
 * 
 */
package com.copsis.clients;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.copsis.enums.WebhookType;
import com.copsis.exceptions.SException;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Israel_work
 *
 */
@Component
@Slf4j
public class WebhookServiceClient {
	@Value("${serviceUrl.quattro-crm.baseURL}")
	private String serviceURL;
	
	public JSONObject send(String strObj, WebhookType type) throws SException {
        try {
            OkHttpClient client = new OkHttpClient();
            String url = serviceURL.concat("/crm/webhooks/send/".concat(type.value));
            log.info("URL: {}", url);
            RequestBody body = null;
            if (strObj != null) body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), strObj);
            log.info("body: {}", body);
            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .addHeader("Cache-Control", "no-cache")
                    .build();
            log.info("request: {}", request);
            Response responseOK = client.newCall(request).execute();
            String strResponse = responseOK.body().string();            
            log.info("strResponse: {}", strResponse);
            return new JSONObject(strResponse);
            
        } catch(Exception e) {
            throw new SException(e.getMessage());
        }
    }
}
