package com.copsis.models;

import lombok.Data;

@Data
public class EstructuraBeneficiariosModel {
	private String nombre = "";
	private int parentesco = 0;
	private int porcentaje = 0;
	private String nacimiento = "";
	private int tipo = 0;
}
