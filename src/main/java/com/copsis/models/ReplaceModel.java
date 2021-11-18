package com.copsis.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReplaceModel {
	private String remplazaDe = "";
	private String remplazaA = "";

	public ReplaceModel(String remplazaDe, String remplazaA) {
		this.remplazaDe = remplazaDe;
		this.remplazaA = remplazaA;
	}

}
