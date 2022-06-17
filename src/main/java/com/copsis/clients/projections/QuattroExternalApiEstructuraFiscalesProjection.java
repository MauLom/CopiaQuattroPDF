package com.copsis.clients.projections;

import org.springframework.http.HttpStatus;

import com.copsis.models.EstructuraConstanciaSatModel;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuattroExternalApiEstructuraFiscalesProjection {
	private Boolean ok;
	private HttpStatus status;
	private EstructuraConstanciaSatModel result;
	private String message;
	private String cause;
}
