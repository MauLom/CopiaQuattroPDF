package com.copsis.controllers.forms;
import java.util.List;

import com.copsis.clients.projections.DocumentoSiniestroProjection;
import com.copsis.clients.projections.SiniestroProjecion;
import com.copsis.clients.projections.ConceptoSiniestrosProjection;
import com.copsis.clients.projections.SocioDirecProjection;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class ImpresionReclamacionForm { 
 private SiniestroProjecion siniestro;
 private SocioDirecProjection socio;
 private List<DocumentoSiniestroProjection> documentoSiniestro;
 private List<ConceptoSiniestrosProjection> conceptoSiniestro;    
}
