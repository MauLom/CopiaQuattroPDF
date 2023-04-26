package com.copsis.clients.projections;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
@Getter
@JsonInclude(value = Include.NON_NULL)
public class CertificadoProjection {
    private	String  nombre;
	private	String vigencia;
    
}
