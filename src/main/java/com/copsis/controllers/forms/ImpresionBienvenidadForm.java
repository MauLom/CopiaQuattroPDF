package com.copsis.controllers.forms;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class ImpresionBienvenidadForm {
    private String noPoliza;
    private String aseguradora;
    private String vigencide;
    private String vigencia;
    private String concepto;
    private String formaPago;
    private String avatarwks;
    private String avatarsocio;
    private String avataraseguradora;
    private String seccion1;
    private String seccion2;
    private String imagen1;
    private String imagen2;
    private String imagen3;
    private int  tipo;
    private String titulo;
    private String subtitulo;
    private String colorDpoliza;   
    private String webpath; 
    
}
