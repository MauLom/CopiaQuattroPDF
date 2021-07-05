package com.copsis.models;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class EstructuraAseguradosModel {
	private String nombre = "";
	private String nacimiento = "";
	private String antiguedad = "";
	private int sexo = 0;
	private int parentesco = 0;
	private String certificado = "";
	private String fechaAlta = "";
	private Integer edad = 0;
    private BigDecimal primaneta = BigDecimal.ZERO;
	private String sa = "";
	private String cobertura = "";
	private String subgrupo = "";
	private String categoria = "";
	

}
