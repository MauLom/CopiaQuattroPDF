package com.copsis.controllers.forms;
import java.util.List;

import com.copsis.clients.projections.BeneficiariosAxaProjection;
import com.copsis.clients.projections.CoberturaBasicaProjection;
import com.copsis.clients.projections.CoberturaBenificiosProjection;
import com.copsis.clients.projections.CoberturaProjection;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class ImpresionCertificadoAxa {
    private	String grupo;
	private	String contrantante;
	private	String noPoliza;
    private String vigenciaDe;
    private String vigenciaA;
	private	String moneda;	
    private	String nombAsegurado;
    private	String categoria;
    private	String noEmpleado;
    private	String fecNacimiento;
    private	String ocupacion;
    private	String regla;
    private List<CoberturaBenificiosProjection>  coberturas;
    private List<CoberturaBenificiosProjection>  beneficios;
    private List<BeneficiariosAxaProjection> beneficiarios;
}
