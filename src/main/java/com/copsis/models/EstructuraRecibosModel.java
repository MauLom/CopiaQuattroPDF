package com.copsis.models;

public class EstructuraRecibosModel {
	private String serie = "";
	private String vigencia_de = "";
	private String vigencia_a = "";
	private String vencimiento = "";
	private float prima_neta = 0;
	private float ajuste_uno = 0;
	private float ajuste_dos = 0;
	private float cargo_extra = 0;
	private float recargo = 0;
	private float derecho = 0;
	private float iva = 0;
	private float prima_total = 0;
	private String recibo_id = "";

	public EstructuraRecibosModel(){

	}

	public String getSerie() {
		return serie;
	}

	public void setSerie(String serie) {
		this.serie = serie;
	}

	public String getVigencia_de() {
		return vigencia_de;
	}

	public void setVigencia_de(String vigencia_de) {
		this.vigencia_de = vigencia_de;
	}

	public String getVigencia_a() {
		return vigencia_a;
	}

	public void setVigencia_a(String vigencia_a) {
		this.vigencia_a = vigencia_a;
	}

	public String getVencimiento() {
		return vencimiento;
	}

	public void setVencimiento(String vencimiento) {
		this.vencimiento = vencimiento;
	}

	public float getPrima_neta() {
		return prima_neta;
	}

	public void setPrima_neta(float prima_neta) {
		this.prima_neta = prima_neta;
	}

	public float getAjuste_uno() {
		return ajuste_uno;
	}

	public void setAjuste_uno(float ajuste_uno) {
		this.ajuste_uno = ajuste_uno;
	}

	public float getAjuste_dos() {
		return ajuste_dos;
	}

	public void setAjuste_dos(float ajuste_dos) {
		this.ajuste_dos = ajuste_dos;
	}

	public float getCargo_extra() {
		return cargo_extra;
	}

	public void setCargo_extra(float cargo_extra) {
		this.cargo_extra = cargo_extra;
	}

	public float getRecargo() {
		return recargo;
	}

	public void setRecargo(float recargo) {
		this.recargo = recargo;
	}

	public float getDerecho() {
		return derecho;
	}

	public void setDerecho(float derecho) {
		this.derecho = derecho;
	}

	public float getIva() {
		return iva;
	}

	public void setIva(float iva) {
		this.iva = iva;
	}

	public float getPrima_total() {
		return prima_total;
	}

	public void setPrima_total(float prima_total) {
		this.prima_total = prima_total;
	}

	public String getRecibo_id() {
		return recibo_id;
	}

	public void setRecibo_id(String recibo_id) {
		this.recibo_id = recibo_id;
	}

}
