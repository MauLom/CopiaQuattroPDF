package com.copsis.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TextoAxa {

	private  String texto;
	private  String nombre;
	private  String logo;
	public TextoAxa(String texto, String nombre, String logo) {
		super();
		this.texto = texto;
		this.nombre = nombre;
		this.logo = logo;
	}
	
}
