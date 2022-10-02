package com.copsis.clients.projections;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class SaludProjection {
	  private String fumas;
	  private String fumasDesde = null;
	  private String bebidas;
	  private String ingerias;
	  private float hasta;
	  private String drogas;
	  private String consumias = null;
	  private String estatura;
	  private String peso;
	  private String pregunta1;
	  private String pregunta2;
	  private String pregunta3;
	  private String pregunta4;
	  private String pregunta5;
	  private String pregunta6;
	  private String pregunta6_1;
	  private String pregunta6_2;
	  private String pregunta6_3 = null;
	  private String pregunta6_4;
	  private String pregunta7;
	  private String pregunta8;
	  private String pregunta9;
	  private String pregunta10;
	  private String pregunta11;
	  private String pregunta12;
	  private String pregunta13;
	  private String pregunta14;
	  private String pregunta15;
	  private float parentesco;
	  private String detallarEnfermedad;

}
