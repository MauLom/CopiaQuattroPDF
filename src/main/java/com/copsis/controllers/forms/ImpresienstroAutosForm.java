package com.copsis.controllers.forms;

import java.util.List;

import com.copsis.clients.projections.ContratanteCaraProjection;
import com.copsis.clients.projections.SocioDirecProjection;
import com.copsis.clients.projections.DocumentoSiniestroProjection;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ImpresienstroAutosForm {    
     private ContratanteCaraProjection  contrantante;
     private SocioDirecProjection socio;
     private List<DocumentoSiniestroProjection> documentoSiniestro;
}
