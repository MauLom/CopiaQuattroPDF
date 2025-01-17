package com.copsis.clients.projections;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class ContratanteProjection {
    private String rfc;
    private String curp;
    private String estadoCivil;
    private String paisDeNacimiento;
    private String nacionalidad;
    private String calle;
    private String noExterior;
    private String noInterior;
    private String codigoPostal;
    private String colonia;
    private boolean facturar;
    private String regimenFiscal;
    private String noSerieFiel;
    private String claveUso;
    private String cpDomicilioFiscal;
    private String telefono;
    private String telefonoParticular;
    private String correo;
    private boolean extranjero;
    private boolean extranjeroFiscal;
    private String extranjeroCalle;
    private String extranjeroNoExterior;
    private String extranjeroNoInterior;
    private String extranjeroColonia;
    private String extranjeroCodigoPostal;
    private String extranjeroPoblacion;
    private String extranjeroEstado;
    private String extranjeroPais;
    private String extranjeroIdFiscal;
    private String extranjeroTelefono;
    private String nombreCompleto;
    private String fechaNacimiento;
    private String genero;
    private String ciudad;
    private String estado;
    private Double sumaAseguradaTitular;
    private Double sumaAseguradaMenor;
    private boolean esEfi;
    private String formaPago;
    private String edadAlcanzadaMenor;
    
}
