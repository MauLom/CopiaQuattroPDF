package com.copsis.clients.projections;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
@Setter
@Getter
public class FacturaDetalleProjection {
    private String totalreclamado;
    private String totalprocedente;
    List<FacturasProjection> facturasListProjection;
}
