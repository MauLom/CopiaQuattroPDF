package com.copsis.controllers.forms;

import java.util.List;

import com.copsis.clients.projections.AseguradoDetlleSiniestroProjection;
import com.copsis.clients.projections.ContratanteCaraProjection;
import com.copsis.clients.projections.DetalleHistorialPasosProjection;
import com.copsis.clients.projections.DocumentoSiniestroProjection;
import com.copsis.clients.projections.FacturasProjection;
import com.copsis.clients.projections.ImagenesAdjuntosDetalleProjection;
import com.copsis.clients.projections.SiniestroAProjection;
import com.copsis.clients.projections.SiniestroDireccionProjection;
import com.copsis.clients.projections.SocioDirecProjection;
import com.copsis.clients.projections.VehiculoProjection;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ImpresionDetalleReclamacionForm {
     private ContratanteCaraProjection contrantante;
     private SocioDirecProjection socio;
     private VehiculoProjection vehiculo;
     private List<DocumentoSiniestroProjection> documentoSiniestro;
     private SiniestroAProjection siniestroAProjection; 
     private List<FacturasProjection> facturasProjection;
     private List<DetalleHistorialPasosProjection> historial;
     private List<ImagenesAdjuntosDetalleProjection>  imagenes;
     private  SiniestroDireccionProjection direccion;     
     private AseguradoDetlleSiniestroProjection aseguradoDetalle; 
}
