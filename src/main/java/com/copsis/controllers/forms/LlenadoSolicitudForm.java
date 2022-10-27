package com.copsis.controllers.forms;

import java.util.ArrayList;
import java.util.List;

import com.copsis.clients.projections.ContratanteProjection;
import com.copsis.clients.projections.MenorProjection;
import com.copsis.clients.projections.OcupacionProjection;
import com.copsis.clients.projections.SaludProjection;
import com.copsis.clients.projections.SolicitudProjection;
import com.copsis.models.DatosBeneficiarios;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class LlenadoSolicitudForm {
	private List<ContratanteProjection> contratante;
	private List<MenorProjection> menor;
	private List<OcupacionProjection> ocupacion;
	private List<SaludProjection> salud;
    private List<DatosBeneficiarios>  beneficiarios ;
	private List<SolicitudProjection> llenado ;
}
