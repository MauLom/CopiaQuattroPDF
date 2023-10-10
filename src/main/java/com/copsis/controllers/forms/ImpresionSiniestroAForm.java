package com.copsis.controllers.forms;

import java.util.List;

import com.copsis.clients.projections.ContratanteCaraProjection;
import com.copsis.clients.projections.SocioDirecProjection;
import com.copsis.clients.projections.DocumentoSiniestroProjection;
import com.copsis.clients.projections.SiniestroAProjection;
import com.copsis.clients.projections.SiniestroContactoProjection;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ImpresionSiniestroAForm {    
     private ContratanteCaraProjection  contrantante;
     private SocioDirecProjection socio;
     private List<DocumentoSiniestroProjection> documentoSiniestro;
     private SiniestroAProjection siniestroAProjection;
     private SiniestroContactoProjection contacto;
}
