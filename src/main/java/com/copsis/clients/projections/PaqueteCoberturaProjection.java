package com.copsis.clients.projections;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class PaqueteCoberturaProjection {
	private String nombres;
	private Integer incluido;
	private String coberturaValor;
	private String letra;
	
	
	public PaqueteCoberturaProjection() {
		
	}

	public PaqueteCoberturaProjection(String nombres, Integer incluido, String coberturaValor) {
		super();
		this.nombres = nombres;
		this.incluido = incluido;
		this.coberturaValor = coberturaValor;
	
		
	}
	
	public PaqueteCoberturaProjection(String nombres, Integer incluido, String coberturaValor,String letra) {
		super();
		this.nombres = nombres;
		this.incluido = incluido;
		this.coberturaValor = coberturaValor;
	    this.letra = letra;
		
	}
	

}
