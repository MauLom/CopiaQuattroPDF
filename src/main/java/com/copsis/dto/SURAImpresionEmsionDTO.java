package com.copsis.dto;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class SURAImpresionEmsionDTO {
	
	private Long suraHogarCotizacion;
	private int paquete;
	private String noPoliza;
	private String inciso;
	private String vigenciaDe;
	private String vigenciaA;
	private String contratanteNombre;
	private String contratanteApPaterno;
	private String contratanteApMaterno;
	private String contratanteCalleNo;
	private String contratanteCP;
	private String contratanteColonia;
	private String contratanteNacimiento;
	private String contratanteRFC;
	private String claveAgente;
	private Integer formaPago;	
	private Long moneda;
	private String giro;
	private String contratoNo;
	private String ubicacionCalleNo;
	private String ubicacionCP;
	private String ubicacionColonia;
	private Double primaNeta;
	private Double derecho;
	private Double recargo;
	private Double iva;
	private Double primaTotal;
	private List<SURACoberturaDTO>coberturas;
	private String ramo;
	private String oficina;
	private String formaPagoEnum;
	private String monedaEnum;
	

}
