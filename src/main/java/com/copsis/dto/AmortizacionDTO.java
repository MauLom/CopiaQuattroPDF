package com.copsis.dto;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class AmortizacionDTO {
	private Integer id;
	private BigDecimal seguroDanos;
	private BigDecimal aportacionCapital;
	private BigDecimal capital;
	private BigDecimal interes;
	private BigDecimal iva;
	private BigDecimal pago;
	private BigDecimal abonoCapital;
	private BigDecimal capitalNuevo;
}
