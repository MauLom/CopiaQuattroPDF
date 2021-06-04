package com.copsis.controllers.forms;

import java.util.List;

import lombok.Data;

@Data
public class ImpresionForm {

	private Integer tiporespuesta;//tipo de respuesta
	private Integer tipoImpresion;//tipo de impresion
	private Integer siniestroId;// sinietro Id
    private List<UrlForm> urls;//Lista de urls de Pdf
	
	


}
