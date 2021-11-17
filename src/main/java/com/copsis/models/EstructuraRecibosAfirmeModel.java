package com.copsis.models;

import java.math.BigDecimal;

import lombok.Data;
@Data
public class EstructuraRecibosAfirmeModel {

	private String vigenciaDe="";
	private String vigenciaA="";
	private String vence ="";
	private int serie ;
    private int totalSerie; 
    private BigDecimal primaNeta = BigDecimal.ZERO;
	private BigDecimal financiamiento = BigDecimal.ZERO;	
	private BigDecimal derecho =BigDecimal.ZERO;	
	private BigDecimal iva = BigDecimal.ZERO;
	private BigDecimal total =BigDecimal.ZERO;
    private BigDecimal comision = BigDecimal.ZERO; 
    private BigDecimal ajuste1 = BigDecimal.ZERO;
    private BigDecimal ajuste2 = BigDecimal.ZERO;
    private BigDecimal cargoExtra = BigDecimal.ZERO;
}
