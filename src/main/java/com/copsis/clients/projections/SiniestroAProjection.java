package com.copsis.clients.projections;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SiniestroAProjection {
    private String descripcion;
    private String reclamado;
    private String procendente;
    private String coaseguro;
    private String deducible;
    private String pagado;
    private String observacion;
    private String noSiniestro;
    private String fechSiniestro;
    private String fechReporte;
    private String fechRegitro;
    private int tipo;
    private String fechaPromesa; 
    private String fechaCierre;
    private int siniestroID;     
}
