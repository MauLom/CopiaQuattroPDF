package com.copsis.models;

import lombok.Data;

@Data
public class EstructuraRecibosModel {
	private String serie = "";
	private String vigenciaDe = "";
	private String vigenciaA = "";
	private String vencimiento = "";
	private float primaneta = 0;
	private float ajusteUno = 0;
	private float ajusteDos = 0;
	private float cargoExtra = 0;
	private float recargo = 0;
	private float derecho = 0;
	private float iva = 0;
	private float primaTotal = 0;
	private String reciboId = "";
}
