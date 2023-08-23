package com.copsis.controllers;

import java.util.stream.Collectors;

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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.copsis.clients.projections.CaractulaProjection;
import com.copsis.clients.projections.CertificadoProjection;
import com.copsis.clients.projections.CotizacionProjection;
import com.copsis.clients.projections.ImpresionReclamacionProjection;
import com.copsis.clients.projections.PolizaAutosProjection;
import com.copsis.controllers.forms.AmortizacionPdfForm;
import com.copsis.controllers.forms.ImpresionAxaForm;
import com.copsis.controllers.forms.ImpresionAxaVidaForm;
import com.copsis.controllers.forms.ImpresionCaractulaForm;
import com.copsis.controllers.forms.ImpresionFiscalForm;
import com.copsis.controllers.forms.ImpresionForm;
import com.copsis.dto.SURAImpresionEmsionDTO;
import com.copsis.exceptions.GeneralServiceException;
import com.copsis.exceptions.ValidationServiceException;
import com.copsis.models.CopsisResponse;
import com.copsis.services.ImpresionService;
import com.copsis.utils.ErrorCode;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/impresion-pdf")
@RequiredArgsConstructor
public class ImpresionePDFController {

	private final ImpresionService impresionService;
	
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CopsisResponse> impresionesMain (@RequestBody ImpresionForm impresionForm,@RequestHeader HttpHeaders headers) {
		try {
		
			return new CopsisResponse.Builder().ok(true).status(HttpStatus.OK).result(impresionService.impresionServicePdf(impresionForm,headers)).build();
		}catch(ValidationServiceException ex) {
			throw ex;
		}catch(Exception ex) {
	
			throw new GeneralServiceException(ErrorCode.MSJ_ERROR_00000, ex.getMessage());
		}
		  
	}

		@PostMapping(value = "amortizacion")
	public ResponseEntity<CopsisResponse> impresionScotia (@Valid @RequestBody AmortizacionPdfForm amortizacionPdfForm, BindingResult bindingResult) {
		try {
			if(bindingResult.hasErrors()) {
				String errors = bindingResult.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(", "));
				throw new ValidationServiceException(ErrorCode.MSJ_ERROR_00000,errors);
			}
			return new CopsisResponse.Builder().ok(true).status(HttpStatus.OK).result(impresionService.impresionAmortizacion(amortizacionPdfForm)).build();
		}catch(ValidationServiceException ex) {
			throw ex;
		}catch(Exception ex) {
			throw new GeneralServiceException(ErrorCode.MSJ_ERROR_00000, ex.getMessage());
		}		  
	}
	@PostMapping(value = "reclamacion")
	public ResponseEntity<CopsisResponse> impresionReclmacion (@Valid @RequestBody ImpresionReclamacionProjection impresionReclamacionProjection, BindingResult bindingResult) {
		try {
			  
			if(bindingResult.hasErrors()) {
				String errors = bindingResult.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(", "));
				throw new ValidationServiceException(ErrorCode.MSJ_ERROR_00000,errors);
			}
			return new CopsisResponse.Builder().ok(true).status(HttpStatus.OK).result(impresionService.impresionReclamacion(impresionReclamacionProjection)).build();
		}catch(ValidationServiceException ex) {
			throw ex;
		}catch(Exception ex) {
			
			throw new GeneralServiceException(ErrorCode.MSJ_ERROR_00000, ex.getMessage());
		}		  
	}
	
	@PostMapping(value = "certificado")
	public ResponseEntity<CopsisResponse> impresionCertificado ( @RequestBody SURAImpresionEmsionDTO suraImpresionEmsionDTO, BindingResult bindingResult) {
		try {
			  
			if(bindingResult.hasErrors()) {
				String errors = bindingResult.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(", "));
				throw new ValidationServiceException(ErrorCode.MSJ_ERROR_00000,errors);
			}
			return new CopsisResponse.Builder().ok(true).status(HttpStatus.OK).result(impresionService.impresionCertificado(suraImpresionEmsionDTO)).build();
		}catch(ValidationServiceException ex) {
			throw ex;
		}catch(Exception ex) {
			throw new GeneralServiceException(ErrorCode.MSJ_ERROR_00000, ex.getMessage());
		}		  
	}
	@PostMapping(value = "constaciaFiscal")
	public ResponseEntity<CopsisResponse> impresionFiscal ( @Valid @RequestBody ImpresionFiscalForm  impresionFiscalForm, BindingResult bindingResult,@RequestHeader HttpHeaders headers) {
		try {
			boolean texto = false;
		
			if( ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString().contains("quattro-pdf-jazkd5ckiq-uc.a.run.app")){
				texto=true;
			}
			
			
			if(bindingResult.hasErrors()) {
				String errors = bindingResult.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(", "));
				throw new ValidationServiceException(ErrorCode.MSJ_ERROR_00000,errors);
			}
			
			return new CopsisResponse.Builder().ok(true).status(HttpStatus.OK).result(impresionService.impresionFiscal(impresionFiscalForm,texto)).build();
		}catch(ValidationServiceException ex) {
			throw ex;
		}catch(Exception ex) {
			throw new GeneralServiceException(ErrorCode.MSJ_ERROR_00000, ex.getMessage());
		}		  
	}
	@PostMapping(value = "axa")
	public ResponseEntity<CopsisResponse> impresionAxa ( @Valid @RequestBody ImpresionAxaForm  impresionAxa, BindingResult bindingResult) {
		try {
			  
			if(bindingResult.hasErrors()) {
				String errors = bindingResult.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(", "));
				throw new ValidationServiceException(ErrorCode.MSJ_ERROR_00000,errors);
			}
			return new CopsisResponse.Builder().ok(true).status(HttpStatus.OK).result(impresionService.impresionAxa(impresionAxa)).build();
		}catch(ValidationServiceException ex) {
			throw ex;
		}catch(Exception ex) {
			throw new GeneralServiceException(ErrorCode.MSJ_ERROR_00000, ex.getMessage());
		}		  
	}
	
	@PostMapping(value = "axaVida", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CopsisResponse> impresionAxaVida ( @Valid @RequestBody ImpresionAxaVidaForm  impresionAxaVidaForm, BindingResult bindingResult) {
		try {
			  
			if(bindingResult.hasErrors()) {
				String errors = bindingResult.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(", "));
				throw new ValidationServiceException(ErrorCode.MSJ_ERROR_00000,errors);
			}
			return new CopsisResponse.Builder().ok(true).status(HttpStatus.OK).result(impresionService.impresionAxaVida(impresionAxaVidaForm)).build();
		}catch(ValidationServiceException ex) {
			throw ex;
		}catch(Exception ex) {
			throw new GeneralServiceException(ErrorCode.MSJ_ERROR_00000, ex.getMessage());
		}		  
	}
	
	
	@PostMapping(value = "cotizacionVida")
	public ResponseEntity<CopsisResponse> impresionCotizacion ( @Valid @RequestBody CotizacionProjection  cotizacionProjection, BindingResult bindingResult) {
		try {
			  
			if(bindingResult.hasErrors()) {
				String errors = bindingResult.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(", "));
				throw new ValidationServiceException(ErrorCode.MSJ_ERROR_00000,errors);
			}
			return new CopsisResponse.Builder().ok(true).status(HttpStatus.OK).result(impresionService.impresionCotizacion(cotizacionProjection)).build();
		}catch(ValidationServiceException ex) {
			throw ex;
		}catch(Exception ex) {
			throw new GeneralServiceException(ErrorCode.MSJ_ERROR_00000, ex.getMessage());
		}		  
	}

	@PostMapping(value = "certificadoAutos")
	public ResponseEntity<CopsisResponse> impresionCertificadoAutos( @Valid @RequestBody CertificadoProjection  certificadoProjection, BindingResult bindingResult) {
		try {
			  
			if(bindingResult.hasErrors()) {
				String errors = bindingResult.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(", "));
				throw new ValidationServiceException(ErrorCode.MSJ_ERROR_00000,errors);
			}
			return new CopsisResponse.Builder().ok(true).status(HttpStatus.OK).result(impresionService.ImpresionCertificadoAutos(certificadoProjection)).build();
		}catch(ValidationServiceException ex) {
			throw ex;
		}catch(Exception ex) {
			throw new GeneralServiceException(ErrorCode.MSJ_ERROR_00000, ex.getMessage());
		}		  
	}

	@PostMapping(value = "caractulaPrudential")
	public ResponseEntity<CopsisResponse> impresionCaractulaPrudential( @Valid @RequestBody CaractulaProjection  caractulaProjection, BindingResult bindingResult) {
		try {
			  
			if(bindingResult.hasErrors()) {
				String errors = bindingResult.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(", "));
				throw new ValidationServiceException(ErrorCode.MSJ_ERROR_00000,errors);
			}
			return new CopsisResponse.Builder().ok(true).status(HttpStatus.OK).result(impresionService.impresionCaractulaPrudential(caractulaProjection)).build();
		}catch(ValidationServiceException ex) {
			throw ex;
		}catch(Exception ex) {
			throw new GeneralServiceException(ErrorCode.MSJ_ERROR_00000, ex.getMessage());
		}		  
	}


	@PostMapping(value = "certificadoVida")
	public ResponseEntity<CopsisResponse> impresionCertificadoVida( @Valid @RequestBody CaractulaProjection  caractulaProjection, BindingResult bindingResult) {
		try {
			  
			if(bindingResult.hasErrors()) {
				String errors = bindingResult.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(", "));
				throw new ValidationServiceException(ErrorCode.MSJ_ERROR_00000,errors);
			}
			return new CopsisResponse.Builder().ok(true).status(HttpStatus.OK).result(impresionService.impresionCertificadoVida(caractulaProjection)).build();
		}catch(ValidationServiceException ex) {
			throw ex;
		}catch(Exception ex) {
			throw new GeneralServiceException(ErrorCode.MSJ_ERROR_00000, ex.getMessage());
		}		  
	}

	@PostMapping(value = "certificadoArgos")
	public ResponseEntity<CopsisResponse> impresionCertificadoArgos( @Valid @RequestBody CaractulaProjection  caractulaProjection, BindingResult bindingResult) {
		try {
			  
			if(bindingResult.hasErrors()) {
				String errors = bindingResult.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(", "));
				throw new ValidationServiceException(ErrorCode.MSJ_ERROR_00000,errors);
			}
			return new CopsisResponse.Builder().ok(true).status(HttpStatus.OK).result(impresionService.impresionCertificadoArgos(caractulaProjection)).build();
		}catch(ValidationServiceException ex) {
			throw ex;
		}catch(Exception ex) {
			throw new GeneralServiceException(ErrorCode.MSJ_ERROR_00000, ex.getMessage());
		}		  
	}


	@PostMapping(value = "polizaAutos")
	public ResponseEntity<CopsisResponse> impresionPolizAutos(@Valid @RequestBody PolizaAutosProjection  polizaAutosProjection, BindingResult bindingResult) {
		try {
			  
			if(bindingResult.hasErrors()) {
				String errors = bindingResult.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(", "));
				throw new ValidationServiceException(ErrorCode.MSJ_ERROR_00000,errors);
			}
			return new CopsisResponse.Builder().ok(true).status(HttpStatus.OK).result(impresionService.impresionPolizAutos(polizaAutosProjection)).build();
		}catch(ValidationServiceException ex) {
			throw ex;
		}catch(Exception ex) {
			throw new GeneralServiceException(ErrorCode.MSJ_ERROR_00000, ex.getMessage());
		}		  
	}
	

	@PostMapping(value = "caractulaAutos")
	public ResponseEntity<CopsisResponse> impresionCaractulaAutos(@Valid @RequestBody ImpresionCaractulaForm  impresionCaractulaForm, BindingResult bindingResult) {
		try {
			  
			if(bindingResult.hasErrors()) {
				String errors = bindingResult.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(", "));
				throw new ValidationServiceException(ErrorCode.MSJ_ERROR_00000,errors);
			}
			return new CopsisResponse.Builder().ok(true).status(HttpStatus.OK).result(impresionService.impresionCaractulaAutos(impresionCaractulaForm)).build();
		}catch(ValidationServiceException ex) {
			throw ex;
		}catch(Exception ex) {
			throw new GeneralServiceException(ErrorCode.MSJ_ERROR_00000, ex.getMessage());
		}		  
	}
	
	
}
