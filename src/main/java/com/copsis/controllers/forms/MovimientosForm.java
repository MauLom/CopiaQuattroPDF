package com.copsis.controllers.forms;

import java.util.List;

import com.copsis.dto.MovimientosDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MovimientosForm {
	private List<MovimientosDTO> movimientos;
}
