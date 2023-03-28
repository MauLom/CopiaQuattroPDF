package com.copsis.controllers.forms;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import com.copsis.models.EstructuraConstanciaSatModel;

import lombok.Getter;

@Getter
public class PdfNegocioForm {
	@NotNull(message = "!El parametro tipo no puede ser nulo!")
	Integer tipoValidacion; 
	@NotNull(message = "!El parametro url no puede ser nulo!")
	@NotBlank(message = "!El parametro url no puede ser nulo!")
	String url;
	@NotNull(message = "!El parametro estructuraConstanciaSatModel no puede ser nulo!")
	EstructuraConstanciaSatModel estructuraConstanciaSatModel;
}
