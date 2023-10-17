package com.copsis.clients.projections;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class VehiculoProjection {

    private String modelo;
    private String clave;
    private String circulacion;
    private String descripcion;
    private String placas;
    private String nomina;
    private String serie;
    private String motor;
    private String color;    
    private String conductor;
    private String sexo;
    private String valorUnidad;
    private String adaptacion;
    private String monto;
    private String especial;
    private String montoextra;
    private Long vehiculoID;
    private int inciso; 
}
