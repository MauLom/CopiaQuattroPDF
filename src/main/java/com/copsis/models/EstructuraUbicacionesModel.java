package com.copsis.models;

import lombok.Data;

@Data
public class EstructuraUbicacionesModel {
	private String nombre = "";
	private String calle = "";
	private String no_externo = "";
	private String no_interno = "";
	private String colonia = "";
	private int niveles = 0;
	private String giro = "";
	private String cp = "";
	private int sotanos = 0;
	private int muros = 0;
	private int techos = 0;
}
