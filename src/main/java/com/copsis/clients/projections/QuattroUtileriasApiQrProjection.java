package com.copsis.clients.projections;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuattroUtileriasApiQrProjection {
	private Boolean ok;
	private HttpStatus status;
	private String result;
	private String message;
	private String cause;
}
