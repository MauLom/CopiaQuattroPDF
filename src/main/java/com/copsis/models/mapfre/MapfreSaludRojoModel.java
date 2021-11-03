package com.copsis.models.mapfre;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraJsonModel;

public class MapfreSaludRojoModel {
	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
	private String contenido = "";

	public MapfreSaludRojoModel(String contenido) {
		this.contenido = contenido;
	}

}
