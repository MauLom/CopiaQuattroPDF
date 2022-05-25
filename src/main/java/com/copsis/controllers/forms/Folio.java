package com.copsis.controllers.forms;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
@Getter
public class Folio {
	@NotBlank(message = "¡El parametro 'folio' no puede ir vacio!")
	private	String folio;
}
