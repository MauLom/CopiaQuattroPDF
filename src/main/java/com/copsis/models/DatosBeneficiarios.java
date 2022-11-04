package com.copsis.models;

import java.util.ArrayList;
import java.util.List;

import com.copsis.clients.projections.BeneficiariosAxaProjection;
import com.copsis.clients.projections.DomiciliosAxaProjection;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DatosBeneficiarios {
    private String infoAdicional="";
    private List<BeneficiariosAxaProjection> beneficiarios;
    private List<DomiciliosAxaProjection> domicilios;
}
