package com.copsis.clients.projections;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SiniestroDireccionProjection {
    private String nombre;
    private String direccion;
    private String cp;
    private String colonia;
    private String municipio;
}
