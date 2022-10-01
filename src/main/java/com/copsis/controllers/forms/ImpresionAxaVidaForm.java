package com.copsis.controllers.forms;

import java.util.ArrayList;
import java.util.List;

import com.copsis.models.ClasesAxa.beneficiarios;
import com.copsis.models.ClasesAxa.contratante;
import com.copsis.models.ClasesAxa.menor;
import com.copsis.models.ClasesAxa.ocupacion;
import com.copsis.models.ClasesAxa.salud;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(value = Include.NON_NULL)
public class ImpresionAxaVidaForm {
	private List<contratante> Contratante = new ArrayList<>();
	private List<menor> menor = new ArrayList<>();
	private List<ocupacion> ocupacion = new ArrayList<>();
	private List<salud> salud = new ArrayList<>();
	private List<beneficiarios> benficiarios = new ArrayList<>();

}
