package com.copsis.controllers.forms;

import java.util.List;

import com.copsis.clients.projections.AseguradosProjection;
import com.copsis.clients.projections.CoberturaProjection;
import com.copsis.clients.projections.PrimasProjection;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;

@Getter
@JsonInclude(value = Include.NON_NULL)
public class ImpresionAxaForm {
	private	int  tipoImpresion;
	private	String logoCredencial;
	private	String logoBarraAxa;
	private String contrannte;
	private String noPoliza;
	private String noCertificado;
	private List<CoberturaProjection> coberturas;
	private String leyenda;
	private String cveAgente;
	private String etiquetaPlan;
	private List<AseguradosProjection> asegurados;
	private	String  logoSuperior;
	private	int ramo;
	private	String subGrupo;
	private String vigenciaDe;
	private String vigenciaA;
	private String fechaEmision;
	private List<PrimasProjection> primas;
	private	String asegurado;
	private	String fecNacAseg;
	private String edadAseg;
	private	String fecAltAseg;
	private	String fecInicioAseg;
	private String cteDireccion;	
	private String formaPago;
	private String moneda;
	private String endosoId;
	private String dias;
	
	
	
}
