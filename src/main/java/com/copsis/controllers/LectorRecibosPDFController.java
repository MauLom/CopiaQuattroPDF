package com.copsis.controllers;

import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.copsis.controllers.forms.PdfForm;
import com.copsis.exceptions.GeneralServiceException;
import com.copsis.exceptions.ValidationServiceException;
import com.copsis.models.CopsisResponse;
import com.copsis.services.IdentificaReciboService;
import com.copsis.utils.ErrorCode;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/lector-recibos")
@RequiredArgsConstructor
public class LectorRecibosPDFController {
    
	@PostMapping(value = "/recibo", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CopsisResponse> lectorCaractulaAxa (@Valid @RequestBody PdfForm pdfForm, BindingResult bindingResult){
		try {
			if(bindingResult.hasErrors()) {
				String errors = bindingResult.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(", "));
				throw new ValidationServiceException(ErrorCode.MSJ_ERROR_00000, errors);
			}
			return new CopsisResponse.Builder().ok(true).status(HttpStatus.OK).result(IdentificaReciboService.identificaRecibo(pdfForm)).build();
		}catch(ValidationServiceException e) {
			throw e;
		}catch(Exception ex) {
			throw new GeneralServiceException(ErrorCode.MSJ_ERROR_00003, ex.getMessage());
		}
	}

}
