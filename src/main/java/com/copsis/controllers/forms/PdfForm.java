package com.copsis.controllers.forms;

import javax.validation.constraints.Min;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Data
public class PdfForm {

	private	String url;
}
