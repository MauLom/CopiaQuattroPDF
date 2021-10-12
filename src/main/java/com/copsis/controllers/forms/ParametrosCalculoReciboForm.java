package com.copsis.controllers.forms;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Data;
import lombok.ToString;

@Data
@JsonInclude(Include.NON_NULL)
@ToString
public class ParametrosCalculoReciboForm {
	private int formaPago;
	private Double primerRecibo;
	private Double subsecuentesRecibos;
	private Double primaNeta;
	private Double ajustePrincipal;
	private Double ajusteSecundario;
	private Double gastoExtra;
	private Double derecho;
	private Double financiamiento;
	private Double iva;
	private Double primaTotal;
	private String vigDe;
	private String vigA;
	private Integer aseguradoraId;
	private Integer ramoId;
	private String d;
}