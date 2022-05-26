/**
 * 
 */
package com.copsis.enums;

/**
 * @author Israel_work
 *
 */
public enum WebhookType {
	TEXT("text"),
    CARD("card");
    
    public final String value;
    
    private WebhookType(String value) {
        this.value = value;
    }
}
