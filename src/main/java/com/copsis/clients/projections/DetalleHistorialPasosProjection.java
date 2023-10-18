package com.copsis.clients.projections;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DetalleHistorialPasosProjection {
    private Long dias;
    private String iniciales;
    private String trammite;
    private String observaciones;
}
