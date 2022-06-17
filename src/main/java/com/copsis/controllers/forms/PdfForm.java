package com.copsis.controllers.forms;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PdfForm {
	@NotBlank(message = "¡El url no puede ir vacio!")
	private	String url;
}
