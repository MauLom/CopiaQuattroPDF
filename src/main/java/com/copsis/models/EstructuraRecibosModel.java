package com.copsis.models;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EstructuraRecibosModel {
    private String serie = "";
    private String vigenciaDe = "";
    private String vigenciaA = "";
    private String vencimiento = "";
    private BigDecimal primaneta = BigDecimal.ZERO;
    private BigDecimal ajusteUno = BigDecimal.ZERO;
    private BigDecimal ajusteDos = BigDecimal.ZERO;
    private BigDecimal cargoExtra = BigDecimal.ZERO;
    private BigDecimal recargo = BigDecimal.ZERO;
    private BigDecimal derecho = BigDecimal.ZERO;
    private BigDecimal iva = BigDecimal.ZERO;
    private BigDecimal primaTotal =  BigDecimal.ZERO;
    private String reciboId = "";
}
