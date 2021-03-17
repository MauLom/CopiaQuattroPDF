package com.copsis.controllers;


import java.io.FileNotFoundException;
import java.io.IOException;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.copsis.controllers.forms.PdfForm;
import com.copsis.models.CopsisResponse;

import com.copsis.services.IdentificaPolizaService;


import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/pdf/lector-pdf")
@RequiredArgsConstructor
public class LectorPDFController   {
	
	@Autowired
	private IdentificaPolizaService textPdf;


	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CopsisResponse> lectorpdf (@RequestBody PdfForm pdfForm,BindingResult bindingResult,@RequestHeader HttpHeaders headers) throws FileNotFoundException, IOException {
		if(bindingResult.hasErrors()) {
			
			return new CopsisResponse.Builder().ok(false).status(HttpStatus.BAD_REQUEST).message("No cuenta con los parametros suficientes").build();
		}

//		if(pdfForm.getUrl() == null) {
//			return new CopsisResponse.Builder().ok(false).status(HttpStatus.BAD_REQUEST).message("No cuenta con los parametros suficientes").build();
//		}
		  return new CopsisResponse.Builder()
				.ok(true)
				.status(HttpStatus.OK)
				.result(textPdf.QueCIA(pdfForm)).build();
	}

}
