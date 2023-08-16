package com.copsis.controllers.forms;

import java.util.List;

import com.copsis.clients.projections.CoberturaProjection;
import com.copsis.clients.projections.InvolucradosProjection;
import com.copsis.clients.projections.VehiculoProjection;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class ImpresionCaractulaForm {
    private String noPoliza;
    private String polizaID;
    private String calleSo;
    private String coloniaSo;
    private String estadoSo;
    private String vigencia;
    private String fechaEmision;
    private String subramo;
    private String formaPago;
    private String moneda;
    private String contrantante;
    private String rfc;
    private String cteCalle;
    private String cteColonia;
    private String curp;
    private String correo;
    private String telefono;
    private String descripcion;
    private String aseguradora;
    private String grupo;
    private String claveAngente;    
    private String urlLogo;
    private String firma;
    private List<InvolucradosProjection> involucrados;
    private List<CoberturaProjection> coberturas;
    private VehiculoProjection vehiculo;
    

}
