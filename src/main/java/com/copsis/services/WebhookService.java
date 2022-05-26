/**
 * 
 */
package com.copsis.services;

import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import com.copsis.clients.WebhookServiceClient;
import com.copsis.encryptor.SiO4EncryptorAES;
import com.copsis.enums.WebhookType;
import com.copsis.models.CardSettings;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Israel_work
 *
 */
@Service
@Slf4j
public class WebhookService {
	@Value("${webhook-id}")
	private int webhookID;
	
	@Autowired
	private WebhookServiceClient webhookServiceClient;
	
	private static final String WIDGETS = "widgets";
	private static final String TEXT_PARAGRAPH = "textParagraph";
	private static final String TEXT = "text";
	private static final String SECTIONS = "sections";
	private static final String CARDS = "cards";
	
	public void send(CardSettings cardSettings) {
		try {
			webhookServiceClient.send(getCardConfig(cardSettings), WebhookType.CARD);            
		} catch(Exception e) {
			// do nothing
			log.info("WebhookService.catch: {}", e.getMessage());
		}
	}
	
	private JSONObject getCardConfig(CardSettings cardSettings) {
		JSONObject config = new JSONObject();
		
		config.put("webhookIdEnc", SiO4EncryptorAES.encrypt(String.valueOf(webhookID), com.copsis.encryptor.utils.Constants.ENCRYPTION_KEY));
        
        JSONArray cards = new JSONArray();
        JSONObject card = new JSONObject();
                
        JSONObject header = new JSONObject();
        //header.put("title", "<b>Socio: " + getNombreSocio(cardSettings.getEmail().getSocio()) + "</b>");
        header.put("title", "<b>Service: quattro-pdf</b>");
        header.put("subtitle", "");
        header.put("imageUrl", "https://storage.googleapis.com/quattrocrm/avatar/photo.jpg");
        header.put("imageStyle", "AVATAR");
        
        card.put("header", header);
        
        // secciones
        JSONArray sections = new JSONArray();
        
        // exception
        JSONObject text = new JSONObject();
        StringBuilder sbText = new StringBuilder();
        //log.info("cardSettings.getFileUrl(): {}", cardSettings.getFileUrl());
        sbText.append("<font color=\"#a5a5a5\">PDF</font><br>");
        sbText.append("<a href=" + cardSettings.getFileUrl() + ">Open in browser</a>");
        
        sbText.append("<br><br><font color=\"#a5a5a5\">Source class</font><br>");
        sbText.append(cardSettings.getSourceClass());
        
        sbText.append("<br><br><font color=\"#a5a5a5\">Exception message</font><br>");
        String exceptionMessage = cardSettings.getException().getMessage().length() > 500 ? cardSettings.getException().getMessage().substring(0, 500) + "..." : cardSettings.getException().getMessage();
        sbText.append(exceptionMessage);        
        
        text.put(TEXT, sbText.toString());
        
        JSONObject textParagraph = new JSONObject();
        textParagraph.put(TEXT_PARAGRAPH, text);
        
        JSONArray widgets = new JSONArray();
        widgets.put(textParagraph);
        
        JSONObject section = new JSONObject();
        section.put(WIDGETS, widgets);
        
        sections.put(section);
        
        // secciones
        card.put(SECTIONS, sections);
        
        cards.put(card);            
        
        config.put(CARDS, cards);
        
        return config;
	}		
}
