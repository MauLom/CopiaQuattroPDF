package com.copsis.clients.projections;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;
@Data
@JsonInclude(Include.NON_NULL)
public class BeneficiarioProjection {

	private String nombres;
	private String fecNacimiento;
	private String parentesco;
	private int porcentaje;
	public BeneficiarioProjection() {

	}
	
	public BeneficiarioProjection( String nombres,
			Date fecNacimiento, String parentesco, int porcentaje) {
		super();
		this.nombres = nombres;
		this.fecNacimiento = fecNacimiento.toString();
		this.parentesco = parentesco;
		this.porcentaje = porcentaje;
	}

}
