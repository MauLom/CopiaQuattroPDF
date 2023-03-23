package com.copsis.controllers.forms;



import java.util.List;

import com.copsis.models.ProductosModel;

import lombok.Getter;
@Getter
public class ImpresionFiscalForm {
	private	String folio;
	private	String rfc;
	private	String nombre;
	private	String apellidoP;
	private	String apellidoM;
	private	String cp;	
	private	String tipo;
	private	String razonSocial;	
	private	String regimenDeCapital;
	private List<ProductosModel> productos;
	private	String fechaRegistro;
}
