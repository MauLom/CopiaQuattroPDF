package com.copsis.clients.projections;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UbicacionesProjection {
    private String noUbicacion;
    private String direccion;    
    private int niviles;
    private int sotano;
    private String tipoConstrucion;
    private String techos;
    private String hidro;
    private String sismo;
}
