package com.copsis.clients.projections;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContratanteProjection {
	private String rfc = "";
	private String curp = "";
	private String noSerieFiel = "";
	private float estadoCivil;
	private String paisDeNacimiento = "";
	private String estado = "";
	private String ciudad = "";
	private String calle = "";
	private String noExterior = "";
	private String noInterior = "";
	private String codigoPostal = "";
	private String colonia = "";
	private boolean domicilioExtranjero;
	private String telefono = "";
	private String correo = "";
}
