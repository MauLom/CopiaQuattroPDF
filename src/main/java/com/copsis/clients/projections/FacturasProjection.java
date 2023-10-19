package com.copsis.clients.projections;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonInclude(Include.NON_NULL)
public class FacturasProjection {
    private  Long facturaID;
    private String emisiorNombreFolio;
    private String totalreclamado;
    private String procedente;
    private String concepto;
}
