package com.copsis.clients.projections;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CoberturaBasicaProjection {
	private	String  paquete;
    private String sumaAsegurada;
	private String deducible;
	private String coaseguro;
	private String topeCoaseguro;
}
