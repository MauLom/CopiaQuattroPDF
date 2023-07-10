package com.copsis.clients.projections;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
@Getter
@JsonInclude(value = Include.NON_NULL)
public class AseguradosProjection {
	private	String  nombre;
	private	String  vigencia;
	private String fechNacimiento;
	private String fechAntigueda;
	private String  parentesco;
	private String edad;
	private String certificado;
    private String fechaBaja;
    private String sa;
	private String prima;
	private String status;
	private String rfc;
	private String telefono;
	private String sexo;
	private String  nomina;

	
}
