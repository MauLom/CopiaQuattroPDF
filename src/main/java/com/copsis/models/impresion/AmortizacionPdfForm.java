package com.copsis.models.impresion;

import java.util.List;

import javax.validation.Valid;

import com.copsis.dto.AmortizacionDTO;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class AmortizacionPdfForm {
	
	private String producto;
	private Double monto;
	private Integer plazo;
	private Double tasa;
	private Integer modelo;
	private String descripcion;
	private String codigoPostal;
	@Valid
	private List<AmortizacionDTO> amortizacion;
}
