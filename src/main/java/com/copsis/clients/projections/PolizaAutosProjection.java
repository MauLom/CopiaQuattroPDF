package com.copsis.clients.projections;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(value = Include.NON_NULL)
public class PolizaAutosProjection {
    private String numeroPoliza;
	private String endoso;
	private String inciso;
    private String movimiento;
    private	String fechaDesdeCert;
    private	String fechaHastaCent;    
}
