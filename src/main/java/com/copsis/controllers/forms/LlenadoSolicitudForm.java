package com.copsis.controllers.forms;

import java.util.ArrayList;
import java.util.List;

import com.copsis.clients.projections.BeneficiariosAxaProjection;
import com.copsis.clients.projections.ContratanteProjection;
import com.copsis.clients.projections.MenorProjection;
import com.copsis.clients.projections.OcupacionProjection;
import com.copsis.clients.projections.SaludProjection;
import com.copsis.clients.projections.SolicitudProjection;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class LlenadoSolicitudForm {
	private List<ContratanteProjection> contratante = new ArrayList<>();
	private List<MenorProjection> menor = new ArrayList<>();
	private List<OcupacionProjection> ocupacion = new ArrayList<>();
	private List<SaludProjection> salud = new ArrayList<>();
	private List<BeneficiariosAxaProjection> beneficiarios = new ArrayList<>();
	private List<SolicitudProjection> solicitud = new ArrayList<>();
}
