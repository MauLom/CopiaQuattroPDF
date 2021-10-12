package com.copsis.clients.projections;
import java.math.BigDecimal;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class CalculaRecibosProjection {
	public Integer serie;
	public Integer totalSerie;
	public String vigenciaDe;
	public String vigenciaA;
	public String vence;
	public BigDecimal primaNeta;
	public BigDecimal ajuste1;
	public BigDecimal ajuste2;
	public BigDecimal cargoExtra;
	public BigDecimal derecho;
	public BigDecimal financiamiento;
	public BigDecimal iva;
	public BigDecimal total;
}