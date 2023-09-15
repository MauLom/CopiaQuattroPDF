package com.copsis.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MovimientosDTO {
	private String poliza;
	private String folio;
	private String endoso;
	private String tipo;
	private String solicitud;
	private String vigencia;
	private String estatus;
	private String fechaCreacion;
	private String usuario;
}
