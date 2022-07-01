package com.copsis.clients.projections;

import java.util.List;

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
	
}
