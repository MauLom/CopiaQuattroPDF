package com.copsis.models;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class EstructuraCoberturasModel  {
	    private int idx;
	    private String nombre = "";
	    private String sa = "";
	    private String deducible = "";
	    private String coaseguro = "";
	    private String copago = "";
	    private String seccion = "";
	    private String deducibleExt = "";
	    private String coaseguroTope = "";
}
