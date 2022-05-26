/**
 * 
 */
package com.copsis.models;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Israel_work
 *
 */
@Getter
@Setter
@Builder
public class CardSettings {
	private Integer logID;
	private String fileUrl;
	private String sourceClass;
	private Exception exception;
}
