package com.copsis.controllers;

import java.io.IOException;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.copsis.controllers.forms.AmortizacionPdfForm;
import com.copsis.controllers.forms.ImpresionForm;
import com.copsis.exceptions.GeneralServiceException;
import com.copsis.exceptions.ValidationServiceException;
import com.copsis.models.CopsisResponse;
import com.copsis.services.ImpresionService;
import com.copsis.utils.ErrorCode;

@RestController
@RequestMapping("/pdf/impresion-pdf")
public class ImpresionePDFController {

	@Autowired
	private ImpresionService impresionService;
	
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CopsisResponse> impresionesMain (@RequestBody ImpresionForm impresionForm,	@RequestHeader HttpHeaders headers) throws  IOException {
		
		  return new CopsisResponse.Builder()
				.ok(true)
				.status(HttpStatus.OK)
				.result(impresionService.impresionServicePdf(impresionForm,headers)).build();
	}
	
	
	@PostMapping(value = "amortizacion")
	public ResponseEntity<CopsisResponse> impresionScotia (@Valid @RequestBody AmortizacionPdfForm impresionForm, BindingResult bindingResult) {
		try {
	
			if(bindingResult.hasErrors()) {
				String errors = bindingResult.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(", "));
				throw new ValidationServiceException(ErrorCode.MSJ_ERROR_00000,errors);
			}
			return new CopsisResponse.Builder().ok(true).status(HttpStatus.OK).result(impresionService.impresionAmortizacion(impresionForm)).build();
		}catch(ValidationServiceException ex) {
			throw ex;
		}
		catch(Exception ex) {
			throw new GeneralServiceException(ErrorCode.MSJ_ERROR_00000, ex.getMessage());
		}
		  
	}
}
