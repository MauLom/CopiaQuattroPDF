package com.copsis.clients.projections;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class CotizacionProjection {
	private String nombre;
	private String sexo;
	private String edad;
	private String habito;
	private String plan;
	private String moneda;
	private String plazodepagos;
	private String primaAnual;
	private String aportacion;
	private String primaMensual;
	private String udi;
	private String primaAnualex;
	private String aportacionex;
	private String primaMensualex;
	private String mx;
	private String email; 
	private String rol; 
	private boolean vida;

}
