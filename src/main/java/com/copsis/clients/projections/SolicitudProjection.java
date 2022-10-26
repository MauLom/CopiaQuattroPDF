package com.copsis.clients.projections;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class SolicitudProjection {
    private String pregunta1;
    private String pregunta2;
    private String pregunta2R1;
    private String pregunta3;
    private String pregunta3R1;
    private Boolean pregunta4;
    private String pregunta5;
}
