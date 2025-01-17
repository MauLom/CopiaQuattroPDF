package com.copsis.clients.projections;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class SiniestroProjecion {
    private String fechaCaptura;
    private String folio;
    private int tramite;
    private String aseguradora;
    private String  cveAgente;
    private String  asegurado;
    private String  fechaNacimiento;
    private String  edad;
    private String  noPoliza;
    private String  certificado;
    private String  parentesco;
    private String  nomTitular;
    private String  nomCliente;
    private String  cteDirecion;
    private String  rfc;
    private String  padecimiento;
    private String  aseguradoraCuenta;
    private String  totalReclamado;  
    private String  usuario;
    private String  usuarioFecha;
}
