package com.copsis.clients.projections;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DomiciliosAxaProjection {
    private float noBeneficiario;
    private String calle;
    private String noExterior;
    private String noInterior;
    private String codigoPostal;
    private String ciudad;
    private String pais;
    private String domicilio;
}
