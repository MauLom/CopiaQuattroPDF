package com.copsis.clients.projections;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class CoberturaProjection {
	private int index;
	private String nombres;
	private String sa;
	private String prima;
	private String coaseguro;
	private String deducible;
	private String topeCoaseguro;
	private String copago;
	
	
	public CoberturaProjection() {
		
	}
}
