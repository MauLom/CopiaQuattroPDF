package com.copsis.models;

import java.math.BigDecimal;
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
    private String cteNombre = "";
    private String cteDireccion = "";
    private String cp = "";
    private String rfc = "";
    private String curp = "";
    private String idCliente = "";
    private String fechaEmision = "";
    private String vigenciaDe = "";
    private String vigenciaA = "";
    private BigDecimal primaneta = BigDecimal.ZERO;
    private BigDecimal ajusteUno = BigDecimal.ZERO;
    private BigDecimal ajusteDos = BigDecimal.ZERO;
    private BigDecimal cargoExtra = BigDecimal.ZERO;
    private BigDecimal recargo = BigDecimal.ZERO;
    private BigDecimal derecho = BigDecimal.ZERO;
    private BigDecimal iva = BigDecimal.ZERO;
    private BigDecimal primaTotal = BigDecimal.ZERO;
    private int moneda = 0;
    private int formaPago = 0;
    private String agente = "";
    private String cveAgente = "";
    private String descripcion = "";
    private String clave = "";
    private String marca = "";
    private int modelo = 0;
    private String serie = "";
    private String motor = "";
    private String conductor = "";
    private String placas = "";
    private float primerPrimatotal = 0;
    private float subPrimatotal = 0;
    private String plan = "";
    private String sa = "";
    private String deducible = "";
    private String deducibleExt = "";
    private String coaseguro = "";
    private String coaseguroTope = "";
    private String polizaGuion = "";
    private int plazo = 0;
    private int plazoPago = 0;
    private int retiro = 0;
    private int tipovida = 0;
    private int aportacion = 0;
    private String subgrupo = "";
    private String categoria= "";
  
    private List<EstructuraCoberturasModel> coberturas = new ArrayList<>();
    private List<EstructuraRecibosModel> recibos = new ArrayList<>();
    private List<EstructuraAseguradosModel> asegurados = new ArrayList<>();
    private List<EstructuraBeneficiariosModel> beneficiarios = new ArrayList<>();
    private List<EstructuraUbicacionesModel> ubicaciones = new ArrayList<>();
    
    private String cteNomina = "";
    private String contratante = "";
    private String nomina = "";
    private String ramo ="";
    private String subramo ="";
    private String error ="";

}
