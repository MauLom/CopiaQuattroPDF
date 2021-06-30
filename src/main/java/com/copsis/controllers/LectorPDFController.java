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

import com.copsis.controllers.forms.PdfForm;
import com.copsis.models.CopsisResponse;
import com.copsis.services.IdentificaCertificadoService;
import com.copsis.services.IdentificaPolizaService;






@RestController
@RequestMapping("/pdf/lector-pdf")
public class LectorPDFController   {
	
	@Autowired
	private IdentificaPolizaService identificaPolizaService;
	
	@Autowired
	private IdentificaCertificadoService identificaCertificadoService;


	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CopsisResponse> lectorpdf (@RequestBody PdfForm pdfForm) throws  IOException {
		
		  return new CopsisResponse.Builder()
				.ok(true)
				.status(HttpStatus.OK)
				.result(identificaPolizaService.queIndentificaPoliza(pdfForm)).build();
	}
	
	@PostMapping(value = "/lectorCertificados", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CopsisResponse> lectorCertificadospdf (@RequestBody PdfForm pdfForm) throws  IOException {
		
		  return new CopsisResponse.Builder()
				.ok(true)
				.status(HttpStatus.OK)
				.result(identificaCertificadoService.queIndentificaCertificado(pdfForm)).build();
	}


}
