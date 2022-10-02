package com.copsis.clients.projections;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class MenorProjection {
	  private float parentesco;
	  private String nombres;
	  private String apPaterno;
	  private String apMaterno;
	  private String fechaNacimiento;
	  private float edad;
	  private boolean sexo;
	  private boolean mismosDatos;
	  private String calle;
	  private String noExterior;
	  private String noInterior;
	  private String codigoPostal;
	  private String colonia;
	  private String telefono;
	  private String correo;
}
