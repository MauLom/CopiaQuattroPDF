package com.copsis.clients.projections;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
@Getter
@JsonInclude(value = Include.NON_NULL)
public class CertificadoProjection {
    private	String  numeroPoliza;
	private	String fechaEmision;
    private	String fechaDesde;
    private	String fechaHasta;
    private	String nombre;
    private	String app;
    private	String apm;
    private	String telefono;
    private	String domicilio;
    private	String ciudad;
    private	String cp;
    private	String estado;
    private	String formaPago;
    private	String modelo;
    private	String marca;
    private	String descripcion;
    private	String serie;
    private	String placas;
    private	String uso;
    
}
