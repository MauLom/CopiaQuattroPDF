package com.copsis.controllers.forms;

import java.util.List;

import javax.validation.constraints.NotBlank;

import com.copsis.dto.AmortizacionDTO;

import lombok.Getter;

@Getter
public class AmortizacionPdfForm {
	@NotBlank(message = "¡El Producto no puede ir vacio!")
	private String producto;
	private Double monto;
	private Integer plazo;
	private Double tasa;
	private Integer modelo;
	private String descripcion;
	private String codigoPostal;
	private List<AmortizacionDTO> amortizacion;
}
