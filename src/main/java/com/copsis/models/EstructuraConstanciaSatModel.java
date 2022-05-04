package com.copsis.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class EstructuraConstanciaSatModel {

	 private  String rfc="";
	 private  String curp="";
	 private  String nombre="";
	 private  String apellidoP="";
	 private  String apellidoM="";
	 private  String fechaOperaciones="";
	 private  String statusPadron="";
	 private  String fechaEstado="";
	 private  String nombreComercial="";
	 private  String cp="";	
	 private  String tipoVialidad="";
	 private  String nombreVialidad="";
	 private  String numeroExterior="";
	 private  String numeroInterior="";
	 private  String colonia="";
	 private  String localidad="";
	 private  String municipio="";
	 private  String estado="";
	 private  String entreCalle="";
	 private  String yCalle="";
	 private  String correo="";
	 private  String telefonoLada="";
	 private  String movilLada="";
	 private  String telefonoFijo="";
	 private  String telefonoMovil="";
	 private  String estadoDomicilio="";
	 private  String estadoContribuyente="";
	 private  String error="";
	 private List<String> regimenFiscal;
	 private String regimenDeCapital;
	 private String tipoPersona;
}
	