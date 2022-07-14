package com.copsis.clients.projections;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class PrimasAxaProjection {
	private double prima;
	private double derecho;
	private double recargo;
	private double iva;
	private double total;
}
