package com.copsis.controllers.forms;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;

@Getter
@JsonInclude(value = Include.NON_NULL)
public class ImpresionAxaVidaForm {
	private String contrannte;
	private String sexo;
	private String rfc;
	private String curp;
	private String serie;
	private String fechNacimiento;
	private String direccion;
	private String nacionalidad;
	private String calle;
	private String noInterior;
	private String noExterior;
	private String colonia;
	private String cp;
	private String municipio;
	private String ciudad;
	private String estado;
	private String regimen;
	private String clave;
	private String cpfiscal;
	private String celular;
	private String correo;
	private String actividad;
	private String ingreso;
	private String calleExt;
	private String noInteriorExt;
	private String noExteriorExt;
	private String cpExt;
	private String municipioExt;
	private String ciudadExt;
	private String plan;
	private String palzo;
	private String plazoPago;
	private String primaExcendente;
	
}
