package com.copsis.models;

import java.util.ArrayList;



import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import java.util.List;
import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
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
  
    private List<EstructuraCoberturasModel> coberturas = new ArrayList<>();
    private List<EstructuraRecibosModel> recibos = new ArrayList<>();
    private List<EstructuraAseguradosModel> asegurados = new ArrayList<>();
    private List<EstructuraBeneficiariosModel> beneficiarios = new ArrayList<>();
    private List<EstructuraUbicacionesModel> ubicaciones = new ArrayList<>();
    
    private String cte_nomina = "";
    private String contratante = "";
    private String nomina = "";
    private String ramo ="";
    private String subramo ="";
    private String error ="";

}
