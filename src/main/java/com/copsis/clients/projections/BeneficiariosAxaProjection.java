package com.copsis.clients.projections;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BeneficiariosAxaProjection {
	private String nombres = "";
	private String apPaterno = "";
	private String apMaterno = "";
	private boolean sexo;
	private String fechaNacimiento = "";
	private float edad;
	private String parentesco = "";
	private String porcentaje = "";
	private String pregunta1="";
	private String infoAdicional="";
	
}
