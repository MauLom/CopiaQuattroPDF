package com.copsis.controllers.forms;

import java.util.List;

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
}