package com.copsis.controllers.forms;

import java.util.List;

import com.copsis.clients.projections.AseguradosProjection;
import com.copsis.clients.projections.CoberturaProjection;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;

@Getter
@JsonInclude(value = Include.NON_NULL)
public class ImpresionAxaForm {
	private	String  logoCredencial;
	private	String  logoBarraAxa;
	private String contrannte;
	private String noPoliza;
	private String noCertificado;
	private List<CoberturaProjection> coberturas;
	private String leyenda;
	private String cveAgente;
	private String etiquetaPlan;
	private List<AseguradosProjection> asegurados;
	
	
}
