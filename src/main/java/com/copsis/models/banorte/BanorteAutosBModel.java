package com.copsis.models.banorte;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraJsonModel;

public class BanorteAutosBModel {
    private DataToolsModel fn = new DataToolsModel();
    private EstructuraJsonModel modelo = new EstructuraJsonModel();

    public EstructuraJsonModel procesar(String contenido) {
        int inicio = 0;
        int fin = 0;
        StringBuilder newcontenido = new StringBuilder();
        String direccion = "";
        contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());

        try {
            modelo.setTipo(1);
            modelo.setCia(35);

            return modelo;
        } catch (Exception ex) {
            modelo.setError(
                    BanorteAutosBModel.this.getClass().getTypeName() + " | " + ex.getMessage() + " | " + ex.getCause());
            return modelo;
        }

    }

}
