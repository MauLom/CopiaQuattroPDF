package com.copsis.controllers;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.copsis.controllers.forms.ImpresionForm;
import com.copsis.models.CopsisResponse;
import com.copsis.services.ImpresionService;

@RestController
@RequestMapping("/pdf/impresion-pdf")
public class ImpresionePDFController {

	@Autowired
	private ImpresionService impresionService;
	
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CopsisResponse> impresionesMain (@RequestBody ImpresionForm impresionForm) throws  IOException {
		
		  return new CopsisResponse.Builder()
				.ok(true)
				.status(HttpStatus.OK)
				.result(impresionService.ImpresionServicePdf(impresionForm)).build();
	}
}
