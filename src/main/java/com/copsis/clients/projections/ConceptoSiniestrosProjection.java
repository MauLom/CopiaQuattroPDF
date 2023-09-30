package com.copsis.clients.projections;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ConceptoSiniestrosProjection {
    private String  folio;
    private String  concepto;
    private String  sumImporte;
    private String  fechFactura;
    private String  rfc;
    private String  emisor;
    private String  procedente;
}
