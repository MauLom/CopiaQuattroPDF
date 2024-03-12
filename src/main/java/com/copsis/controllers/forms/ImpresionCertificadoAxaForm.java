package com.copsis.controllers.forms;
import java.util.List;

import com.copsis.clients.projections.AseguradosProjection;
import com.copsis.clients.projections.BeneficiarioProjection;
import com.copsis.clients.projections.PaqueteCoberturaProjection;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class ImpresionCertificadoAxaForm {
    private	String grupo;
	private	String contrantante;
	private	String noPoliza;
    private String vigenciaDe;
    private String vigenciaA;
	private	String moneda;	
    private	String categoria;  
    private	String ocupacion;
    private	String regla; 
    private String titulo;
    private List<PaqueteCoberturaProjection>  coberturas;
    private List<PaqueteCoberturaProjection>  beneficios;
    private List<BeneficiarioProjection> beneficiarios;
    private AseguradosProjection asegurado;
}
