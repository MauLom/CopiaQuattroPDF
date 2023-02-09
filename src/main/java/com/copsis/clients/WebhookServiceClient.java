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
	@Value("${gateway-url}")
	private String gatewayURL;
	
	public JSONObject send(JSONObject obj, WebhookType type) throws SException {
        try {
            OkHttpClient client = new OkHttpClient();
            String url = gatewayURL.concat("/crm/webhooks/send/".concat(type.value));
            System.out.println("url " + url);
            System.out.println("obj " + obj.toString());
            
            RequestBody body = null;
            if (obj != null) body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), obj.toString());
            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .addHeader("Cache-Control", "no-cache")
                    .build();

            Response responseOK = client.newCall(request).execute();
            String strResponse = responseOK.body().string();            
            
            return new JSONObject(strResponse);
            
        } catch(Exception e) {
            throw new SException(e.getMessage());
        }
    }
}
