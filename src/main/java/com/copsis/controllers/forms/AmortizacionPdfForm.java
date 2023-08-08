package com.copsis.controllers.forms;

import java.util.List;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import com.copsis.dto.AmortizacionDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AmortizacionPdfForm {
	@NotBlank(message = "¡El Producto no puede ir vacio!")
	private String producto;
	@Min(value = 1, message = "El parametro monto debe ser mayor a cero")
	private Double monto;
	@Min(value = 12, message = "El parametro plazo debe ser mayor a once")
	private Integer plazo;
	@NotNull(message = "El parametro tasa no puede ir vacio")
	private Double tasa;
	@Min(value = 1, message = "El parametro modelo debe ser mayor a cero")
	private Integer modelo;
	@NotBlank(message = "El parametro descripcion no puede ir vacio")
	private String descripcion;
	@NotBlank(message = "El parametro codigoPostal no puede ir vacio")
	private String codigoPostal;
	@NotNull(message = "El parametro amortizacion no puede ir vacio")
	private Double comisionApertura;
	private Double certificadoRenovacion;
	private Double garantiaExtendida;
	private Double comisionAperturaIva;
	private String montoSeguroFinacimiento;
	private Double montoSeguroVida;
	private Double montoSeguroDesempleo;
	private Double derecho;
	private Double montoSeguroEngache;
	private Double montoSegurosDanos;
	private Double montoFinanciar;
	private Double valor;
	private Double coberturasAdicionales;
	private Double engache;
	private Double mensualidad;
	private String cliente;
	private List<AmortizacionDTO> amortizacion;
}
