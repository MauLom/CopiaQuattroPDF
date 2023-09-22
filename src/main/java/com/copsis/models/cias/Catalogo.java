package com.copsis.models.cias;

import java.util.Optional;

import com.copsis.models.cias.Aseguradoras.Aseguradora;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Catalogo {
    public String catalgo(int id) {
        String abrevia = "";
        try {
            String cias = "{\"cias\":[{\"id\":1,\"abrevia\":\"CHUBB\"},{\"id\":3,\"abrevia\":\"AIG\"},{\"id\":4,\"abrevia\":\"ALIANZ\"},{\"id\":7,\"abrevia\":\"ANA\"},{\"id\":14,\"abrevia\":\"HDI\"},{\"id\":18,\"abrevia\":\"GNP\"},{\"id\":20,\"abrevia\":\"AXA\"},{\"id\":22,\"abrevia\":\"MAPFRE\"},{\"id\":29,\"abrevia\":\"QUALITAS\"},{\"id\":31,\"abrevia\":\"AFIRME\"},{\"id\":35,\"abrevia\":\"BANORTE\"},{\"id\":44,\"abrevia\":\"ZURICH\"},{\"id\":33,\"abrevia\":\"ATLAS\"},{\"id\":49,\"abrevia\":\"PRIM\"}]}";
            Aseguradoras aseguradoras = new ObjectMapper().readValue(cias, Aseguradoras.class);

            Optional<Aseguradora> encontrado = aseguradoras.aseguradoras.stream().filter(cia-> cia.getId() == id ).findFirst();
			if(encontrado.isPresent()) {
				abrevia = encontrado.get().getAbrevia();
			}
            return abrevia;
        } catch (Exception e) {
            return abrevia;
        }

    }
}
