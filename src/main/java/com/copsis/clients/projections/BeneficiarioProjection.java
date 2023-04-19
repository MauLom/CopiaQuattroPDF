package com.copsis.clients.projections;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class BeneficiarioProjection {

	private String nombres;
	private String fecNacimiento;
	private String parentesco;
	private BigDecimal porcentaje;
	private String solicitante;
	private String nombreEspanol;
	public BeneficiarioProjection() {

	}
	
	public BeneficiarioProjection( String nombres,Date fecNacimiento, String parentesco, BigDecimal porcentaje) {
		super();
		this.nombres = nombres;
		this.fecNacimiento = fecNacimiento.toString();
		this.parentesco = parentesco;
		this.porcentaje = porcentaje;
	}

}
