package com.copsis.models;

import org.json.JSONArray;

public class EstructuraJsonModel {

    private int tipo = 0;
    private int cia = 0;
    private String poliza = "";
    private String renovacion = "";
    private String endoso = "";
    private int inciso = 0;
    private String cte_nombre = "";
    private String cte_direccion = "";
    private String cp = "";
    private String rfc = "";
    private String curp = "";
    private String id_cliente = "";
    private String fecha_emision = "";
    private String vigencia_de = "";
    private String vigencia_a = "";
    private float prima_neta = 0;
    private float ajuste_uno = 0;
    private float ajuste_dos = 0;
    private float cargo_extra = 0;
    private float recargo = 0;
    private float derecho = 0;
    private float iva = 0;
    private float prima_total = 0;
    private int moneda = 0;
    private int forma_pago = 0;
    private String agente = "";
    private String cve_agente = "";
    private String descripcion = "";
    private String clave = "";
    private String marca = "";
    private int modelo = 0;
    private String serie = "";
    private String motor = "";
    private String conductor = "";
    private String placas = "";
    private float primer_prima_total = 0;
    private float sub_prima_total = 0;
    private String plan = "";
    private String sa = "";
    private String deducible = "";
    private String deducible_ext = "";
    private String coaseguro = "";
    private String coaseguro_tope = "";
    private String polizaGuion = "";
    private int plazo = 0;
    private int plazo_pago = 0;
    private int retiro = 0;
    private int tipovida = 0;
    private int aportacion = 0;
    private JSONArray coberturas = new JSONArray();
    private JSONArray recibos = new JSONArray();
    private JSONArray asegurados = new JSONArray();
    private JSONArray beneficiarios = new JSONArray();
    private JSONArray ubicaciones = new JSONArray();

    private String cte_nomina = "";
    private String contratante = "";
    private String nomina = "";
    private String ramo ="";
    private String subramo ="";

    public EstructuraJsonModel() {

    }

	public int getTipo() {
		return tipo;
	}

	public void setTipo(int tipo) {
		this.tipo = tipo;
	}

	public int getCia() {
		return cia;
	}

	public void setCia(int cia) {
		this.cia = cia;
	}

	public String getPoliza() {
		return poliza;
	}

	public void setPoliza(String poliza) {
		this.poliza = poliza;
	}

	public String getRenovacion() {
		return renovacion;
	}

	public void setRenovacion(String renovacion) {
		this.renovacion = renovacion;
	}

	public String getEndoso() {
		return endoso;
	}

	public void setEndoso(String endoso) {
		this.endoso = endoso;
	}

	public int getInciso() {
		return inciso;
	}

	public void setInciso(int inciso) {
		this.inciso = inciso;
	}

	public String getCte_nombre() {
		return cte_nombre;
	}

	public void setCte_nombre(String cte_nombre) {
		this.cte_nombre = cte_nombre;
	}

	public String getCte_direccion() {
		return cte_direccion;
	}

	public void setCte_direccion(String cte_direccion) {
		this.cte_direccion = cte_direccion;
	}

	public String getCp() {
		return cp;
	}

	public void setCp(String cp) {
		this.cp = cp;
	}

	public String getRfc() {
		return rfc;
	}

	public void setRfc(String rfc) {
		this.rfc = rfc;
	}

	public String getCurp() {
		return curp;
	}

	public void setCurp(String curp) {
		this.curp = curp;
	}

	public String getId_cliente() {
		return id_cliente;
	}

	public void setId_cliente(String id_cliente) {
		this.id_cliente = id_cliente;
	}

	public String getFecha_emision() {
		return fecha_emision;
	}

	public void setFecha_emision(String fecha_emision) {
		this.fecha_emision = fecha_emision;
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

	public int getMoneda() {
		return moneda;
	}

	public void setMoneda(int moneda) {
		this.moneda = moneda;
	}

	public int getForma_pago() {
		return forma_pago;
	}

	public void setForma_pago(int forma_pago) {
		this.forma_pago = forma_pago;
	}

	public String getAgente() {
		return agente;
	}

	public void setAgente(String agente) {
		this.agente = agente;
	}

	public String getCve_agente() {
		return cve_agente;
	}

	public void setCve_agente(String cve_agente) {
		this.cve_agente = cve_agente;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getClave() {
		return clave;
	}

	public void setClave(String clave) {
		this.clave = clave;
	}

	public String getMarca() {
		return marca;
	}

	public void setMarca(String marca) {
		this.marca = marca;
	}

	public int getModelo() {
		return modelo;
	}

	public void setModelo(int modelo) {
		this.modelo = modelo;
	}

	public String getSerie() {
		return serie;
	}

	public void setSerie(String serie) {
		this.serie = serie;
	}

	public String getMotor() {
		return motor;
	}

	public void setMotor(String motor) {
		this.motor = motor;
	}

	public String getConductor() {
		return conductor;
	}

	public void setConductor(String conductor) {
		this.conductor = conductor;
	}

	public String getPlacas() {
		return placas;
	}

	public void setPlacas(String placas) {
		this.placas = placas;
	}

	public float getPrimer_prima_total() {
		return primer_prima_total;
	}

	public void setPrimer_prima_total(float primer_prima_total) {
		this.primer_prima_total = primer_prima_total;
	}

	public float getSub_prima_total() {
		return sub_prima_total;
	}

	public void setSub_prima_total(float sub_prima_total) {
		this.sub_prima_total = sub_prima_total;
	}

	public String getPlan() {
		return plan;
	}

	public void setPlan(String plan) {
		this.plan = plan;
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

	public String getDeducible_ext() {
		return deducible_ext;
	}

	public void setDeducible_ext(String deducible_ext) {
		this.deducible_ext = deducible_ext;
	}

	public String getCoaseguro() {
		return coaseguro;
	}

	public void setCoaseguro(String coaseguro) {
		this.coaseguro = coaseguro;
	}

	public String getCoaseguro_tope() {
		return coaseguro_tope;
	}

	public void setCoaseguro_tope(String coaseguro_tope) {
		this.coaseguro_tope = coaseguro_tope;
	}

	public String getPolizaGuion() {
		return polizaGuion;
	}

	public void setPolizaGuion(String polizaGuion) {
		this.polizaGuion = polizaGuion;
	}

	public int getPlazo() {
		return plazo;
	}

	public void setPlazo(int plazo) {
		this.plazo = plazo;
	}

	public int getPlazo_pago() {
		return plazo_pago;
	}

	public void setPlazo_pago(int plazo_pago) {
		this.plazo_pago = plazo_pago;
	}

	public int getRetiro() {
		return retiro;
	}

	public void setRetiro(int retiro) {
		this.retiro = retiro;
	}

	public int getTipovida() {
		return tipovida;
	}

	public void setTipovida(int tipovida) {
		this.tipovida = tipovida;
	}

	public int getAportacion() {
		return aportacion;
	}

	public void setAportacion(int aportacion) {
		this.aportacion = aportacion;
	}

	public JSONArray getCoberturas() {
		return coberturas;
	}

	public void setCoberturas(JSONArray coberturas) {
		this.coberturas = coberturas;
	}

	public JSONArray getRecibos() {
		return recibos;
	}

	public void setRecibos(JSONArray recibos) {
		this.recibos = recibos;
	}

	public JSONArray getAsegurados() {
		return asegurados;
	}

	public void setAsegurados(JSONArray asegurados) {
		this.asegurados = asegurados;
	}

	public JSONArray getBeneficiarios() {
		return beneficiarios;
	}

	public void setBeneficiarios(JSONArray beneficiarios) {
		this.beneficiarios = beneficiarios;
	}

	public JSONArray getUbicaciones() {
		return ubicaciones;
	}

	public void setUbicaciones(JSONArray ubicaciones) {
		this.ubicaciones = ubicaciones;
	}

	public String getCte_nomina() {
		return cte_nomina;
	}

	public void setCte_nomina(String cte_nomina) {
		this.cte_nomina = cte_nomina;
	}

	public String getContratante() {
		return contratante;
	}

	public void setContratante(String contratante) {
		this.contratante = contratante;
	}

	public String getNomina() {
		return nomina;
	}

	public void setNomina(String nomina) {
		this.nomina = nomina;
	}

	public String getRamo() {
		return ramo;
	}

	public void setRamo(String ramo) {
		this.ramo = ramo;
	}

	public String getSubramo() {
		return subramo;
	}

	public void setSubramo(String subramo) {
		this.subramo = subramo;
	}
    

}
