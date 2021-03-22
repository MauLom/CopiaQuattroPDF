package com.copsis.models;

import lombok.Data;

@Data
public class ReplaceModel {
	private String remplazaDe = "";
	private String remplazaA = "";

	public ReplaceModel(String remplazaDe, String remplazaA) {
		this.remplazaDe = remplazaDe;
		this.remplazaA = remplazaA;
	}

}
