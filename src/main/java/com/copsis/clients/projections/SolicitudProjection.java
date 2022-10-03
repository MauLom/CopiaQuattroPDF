package com.copsis.clients.projections;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class SolicitudProjection {
	  private String parrafo1;
	  private String parrafo2;
	  private String parrafo2_1;
	  private String parrafo3;
	  private String parrafo3_1;
	  private boolean parrafo4;
	  private String parrafo5;
}
