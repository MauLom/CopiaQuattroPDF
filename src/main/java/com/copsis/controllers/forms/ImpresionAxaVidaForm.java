package com.copsis.controllers.forms;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(value = Include.NON_NULL)
public class ImpresionAxaVidaForm {
	private String folder ="";
    private String   bucket;
    private LlenadoSolicitudForm llenadoSolicitud ;

}
