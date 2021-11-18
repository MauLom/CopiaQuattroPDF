package com.copsis.models;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class EstructuraUbicacionesModel {
	private String nombre = "";
	private String calle = "";
	private String noExterno = "";
	private String noInterno = "";
	private String colonia = "";
	private int niveles = 0;
	private String giro = "";
	private String cp = "";
	private int sotanos = 0;
	private int muros = 0;
	private int techos = 0;
}
