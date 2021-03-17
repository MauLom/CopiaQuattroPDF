package com.copsis.models;

import lombok.Data;

@Data
public class EstructuraRecibosModel {
	private String serie = "";
	private String vigencia_de = "";
	private String vigencia_a = "";
	private String vencimiento = "";
	private float prima_neta = 0;
	private float ajuste_uno = 0;
	private float ajuste_dos = 0;
	private float cargo_extra = 0;
	private float recargo = 0;
	private float derecho = 0;
	private float iva = 0;
	private float prima_total = 0;
	private String recibo_id = "";
}
