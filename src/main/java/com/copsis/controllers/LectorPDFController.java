package com.copsis.controllers;
import java.util.stream.Collectors;

import jakarta.validation.Valid;

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
import com.copsis.controllers.forms.PdfNegocioForm;
import com.copsis.exceptions.GeneralServiceException;
import com.copsis.exceptions.ValidationServiceException;
import com.copsis.models.CopsisResponse;
import com.copsis.models.ImportacionValidacionModel;
import com.copsis.services.IdentificaCertificadoService;
import com.copsis.services.IdentificaPolizaService;
import com.copsis.services.IndentificaConstanciaService;
import com.copsis.utils.ErrorCode;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/lector-pdf")
@RequiredArgsConstructor
public class LectorPDFController   {
	
	private final IdentificaPolizaService identificaPolizaService;
	private final IdentificaCertificadoService identificaCertificadoService;
	private final IndentificaConstanciaService  indentificaConstanciaService;

	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CopsisResponse> lectorpdf (@Valid @RequestBody PdfForm pdfForm ,BindingResult bindingResult){
		try {
			if(bindingResult.hasErrors()) {
				String errors = bindingResult.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(", "));
				throw new ValidationServiceException(ErrorCode.MSJ_ERROR_00000, errors);
			}
		
			return new CopsisResponse.Builder().ok(true).status(HttpStatus.OK).result(identificaPolizaService.queIndentificaPoliza(pdfForm)).build();
		}catch(ValidationServiceException e) {
			throw e;
		}catch(Exception ex) {
			throw new GeneralServiceException(ErrorCode.MSJ_ERROR_00000, ex.getMessage());
		}
	}
	@PostMapping(value = "/lectormasi", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CopsisResponse> lectorpdfs (@Valid @RequestBody PdfForm pdfForm ,BindingResult bindingResult){
		try {
			if(bindingResult.hasErrors()) {
				String errors = bindingResult.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(", "));
				throw new ValidationServiceException(ErrorCode.MSJ_ERROR_00000, errors);
			}
			
			ImportacionValidacionModel  importacionValidacionModel = new ImportacionValidacionModel();			
			return new CopsisResponse.Builder().ok(true).status(HttpStatus.OK).result(importacionValidacionModel.isValidImportacion(identificaPolizaService.queIndentificaPoliza(pdfForm))).build();
		}catch(ValidationServiceException e) {
			throw e;
		}catch(Exception ex) {
			throw new GeneralServiceException(ErrorCode.MSJ_ERROR_00000, ex.getMessage());
		}
	}
	
	
	@PostMapping(value = "/lectorCertificados", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CopsisResponse> lectorCertificadospdf (@Valid @RequestBody PdfForm pdfForm, BindingResult bindingResult){
		try {
			if(bindingResult.hasErrors()) {
				String errors = bindingResult.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(", "));
				throw new ValidationServiceException(ErrorCode.MSJ_ERROR_00000, errors);
			}
			return new CopsisResponse.Builder().ok(true).status(HttpStatus.OK).result(identificaCertificadoService.queIndentificaCertificado(pdfForm)).build();
		}catch(ValidationServiceException e) {
			throw e;
		}catch(Exception ex) {
			throw new GeneralServiceException(ErrorCode.MSJ_ERROR_00000, ex.getMessage());
		}
	}
	
	@PostMapping(value = "/lectorConstancia", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CopsisResponse> lectorConstanciapdf (@Valid @RequestBody PdfForm pdfForm, BindingResult bindingResult){
		try {
			if(bindingResult.hasErrors()) {
				String errors = bindingResult.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(", "));
				throw new ValidationServiceException(ErrorCode.MSJ_ERROR_00000, errors);
			}
			return new CopsisResponse.Builder().ok(true).status(HttpStatus.OK).result(indentificaConstanciaService.indentificaConstancia(pdfForm)).build();
		}catch(ValidationServiceException e) {
			throw e;
		}catch(Exception ex) {
			throw new GeneralServiceException(ErrorCode.MSJ_ERROR_00000, ex.getMessage());
		}
	}


	@PostMapping(value = "/negocio", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CopsisResponse> negocioValidacionJson (@Valid @RequestBody PdfNegocioForm pdfNegocioForm, BindingResult bindingResult){
		try {
			if(bindingResult.hasErrors()) {
				String errors = bindingResult.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(", "));
				throw new ValidationServiceException(ErrorCode.MSJ_ERROR_00000, errors);
			}
			return new CopsisResponse.Builder().ok(true).status(HttpStatus.OK).result(indentificaConstanciaService.negocioValidaDatosFiscales(pdfNegocioForm)).build();
		}catch(ValidationServiceException e) {
			throw e;
		}catch(Exception ex) {
			throw new GeneralServiceException(ErrorCode.MSJ_ERROR_00003, ex.getMessage());
		}
	}
}
