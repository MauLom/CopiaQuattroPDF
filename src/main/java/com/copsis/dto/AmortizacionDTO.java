package com.copsis.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class AmortizacionDTO {
	private Integer id;
	private Double seguroDanos;
	private Double aportacionCapital;
	private Double capital;
	private Double interes;
	private Double iva;
	private Double pago;
	private Double abonoCapital;
	private Double capitalNuevo;
}
