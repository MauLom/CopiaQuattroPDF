package com.copsis.clients.projections;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class CaractulaProjection {
    private	String  numeroPoliza;
	private	String fechaEmision;
    private	String fechaDesde;
    private	String fechaHasta;
    private	String mmoneda;
    private	String formaPago;
    private	String nombre;
    private	String rfc;
    private	String telefono;
    private	String direccion;
    private	String colonia;
    private	String población;
    private	String Nacionalidad;        
    private	String cp;
    private AseguradosProjection asegurado;
    private List<CoberturaProjection> coberturas;
    private	String  primaNeta;
    private	String  iva;
    private	String  derecho;
    private	String  recargo;
    private	String  primaTotal;
    private List<BeneficiarioProjection> beneficiarios;
    private int tipo=0;
    private String nivelEscolar;
}
