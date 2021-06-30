package com.copsis.models.certificado;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraJsonModel;
import com.copsis.models.banorte.BanorteAutosModel;

public class ZurichCertificadoGrupo {

	// Clases
	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
    //variables
	private String contenido = "";
	private String newcontenido = "";
	private String recibosText = "";
	private String resultado = "";
	private int inicio = 0;
	private int fin = 0;
	
	public ZurichCertificadoGrupo(String contenido) {
		this.contenido = contenido;
		//this.recibosText = recibos;
	}
	
	public EstructuraJsonModel procesar() {
		contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());
		try {
			
			
			
			return modelo;
		} catch (Exception ex) {

			modelo.setError(ZurichCertificadoGrupo.this.getClass().getTypeName() + " | " + ex.getMessage() + " | "
					+ ex.getCause());
			return modelo;
		}
		
		
	}
	
	
}
