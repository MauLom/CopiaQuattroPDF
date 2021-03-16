package com.copsis.models;

public class EstructuraCoberturasModel {
	 private int idx;
	    private String nombre = "";
	    private String sa = "";
	    private String deducible = "";
	    private String coaseguro = "";
	    private String copago = "";
	    private String seccion = "";

	    public EstructuraCoberturasModel() {

	    }

	    public String getNombre() {
	        return nombre;
	    }

	    public void setNombre(String nombre) {
	        this.nombre = nombre;
	    }

	    public String getSa() {
	        return sa;
	    }

	    public void setSa(String sa) {
	        this.sa = sa;
	    }

	    public String getDeducible() {
	        return deducible;
	    }

	    public void setDeducible(String deducible) {
	        this.deducible = deducible;
	    }

	    public String getCoaseguro() {
	        return coaseguro;
	    }

	    public void setCoaseguro(String coaseguro) {
	        this.coaseguro = coaseguro;
	    }

	    public String getCopago() {
	        return copago;
	    }

	    public void setCopago(String copago) {
	        this.copago = copago;
	    }

	    public String getSeccion() {
	        return seccion;
	    }

	    public void setSeccion(String seccion) {
	        this.seccion = seccion;
	    }

	    public int getIdx() {
	        return idx;
	    }

	    public void setIdx(int idx) {
	        this.idx = idx;
	    }

}
