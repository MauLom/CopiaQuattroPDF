package com.copsis.controllers.forms;

import java.util.List;

import com.copsis.clients.projections.BeneficiarioProjection;
import com.copsis.clients.projections.PaqueteCoberturaProjection;
import com.copsis.dto.AmortizacionDTO;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Data
@JsonInclude(value = Include.NON_NULL)
public class ImpresionForm {
	private Integer tiporespuesta;//tipo de respuesta
	private Integer tipoImpresion;//tipo de impresion
	private Integer siniestroDocumentoID;// sinietro Id
    private List<UrlForm> urls;//Lista de urls de Pdf
    private String d;
    private String nombreOriginal;
    private String bucket;
    private String folder;
	private byte[] byteArrayPDF;
	private String fecha;
	private String tipoSiniestro;
	private String asegurado;
	private String saSiniestro;
	private String aseguradora;
	private String contrannte;
	private String categoria;
	private String nopoliza;
	private String rfc;
	private boolean sexo;
	private String calle;
	private String noExterior;
	private String noInterior;
	private String colonia;
	private String municipo;
	private String estado;
	private String cp;	
	private String nacimiento;
	private String vigenciaDe;
	private String vigenciaA;
	private String nomina;
	private String correo;
	private String hashCode;
	private String comentario;
	private String ocupacion;
	private String paisNacimiento;
	private String telefono;
	private String textoConsentimiento;
	private List<BeneficiarioProjection> Beneficiarios;
	private List<PaqueteCoberturaProjection> Coberturas;
}