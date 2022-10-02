package com.copsis.controllers.forms;

import java.util.ArrayList;
import java.util.List;

import com.copsis.clients.projections.BeneficiariosAxaProjection;
import com.copsis.clients.projections.ContratanteProjection;
import com.copsis.clients.projections.MenorProjection;
import com.copsis.clients.projections.OcupacionProjection;
import com.copsis.clients.projections.SaludProjection;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(value = Include.NON_NULL)
public class ImpresionAxaVidaForm {
	private List<ContratanteProjection> Contratante = new ArrayList<>();
	private List<MenorProjection> menor = new ArrayList<>();
	private List<OcupacionProjection> ocupacion = new ArrayList<>();
	private List<SaludProjection> salud = new ArrayList<>();
	private List<BeneficiariosAxaProjection> benficiarios = new ArrayList<>();

}
