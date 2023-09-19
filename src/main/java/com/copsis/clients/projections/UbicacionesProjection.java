package com.copsis.clients.projections;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UbicacionesProjection {
    private String noUbicacion;
    private String direccion;
    private String tipoConstrucion;
    private String niveles;
    private int sotano;
    private int techos;
    private int hidro;
    private int sismo;
}
