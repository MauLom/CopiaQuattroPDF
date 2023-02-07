package com.copsis.clients.projections;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;

@Getter
@JsonInclude(value = Include.NON_NULL)
public class ProspectoProjection {
	private String parentesco;
	private String nombre;
	private String edad;
	private String sexo;	
	private String habito;
}
