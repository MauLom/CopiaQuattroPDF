package com.copsis.models.sisnova;

import java.util.ArrayList;
import java.util.List;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraAseguradosModel;
import com.copsis.models.EstructuraCoberturasModel;
import com.copsis.models.EstructuraJsonModel;
import com.copsis.models.atlas.AtlasSaludModel;

public class SisnovaSaludModel {
	
	// Clases
	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
	// Variables
	private String contenido = "";
	private String newcontenido = "";
	private String resultado = "";
	private int donde = 0;
	private int inicio = 0;
	private int fin = 0;

	
	public SisnovaSaludModel(String contenido) {
		this.contenido = contenido;
		
	}
	
	public EstructuraJsonModel procesar() {
		contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());
		
//		System.out.println(contenido);
		try {
		    //mocia
            modelo.setCia(11);
            //cia
            modelo.setTipo(3);
            //

            
            //Datos Generales
            inicio = contenido.indexOf("PÓLIZA NO.");
            fin = contenido.indexOf("Datos de los asegurados");
            if (inicio > 0 && fin > 0 && inicio < fin) {
				newcontenido = contenido.substring(inicio, fin).replace("\r", "").replace("@", "").replace("   ", " ");
				for (int i = 0; i < newcontenido.split("\n").length; i++) {
					System.out.println("=======> " + newcontenido.split("\n")[i]);
					if(newcontenido.split("\n")[i].contains("PÓLIZA NO.")) {
						modelo.setPoliza(newcontenido.split("\n")[i].split("PÓLIZA NO.")[1].trim());
					}
					if(newcontenido.split("\n")[i].contains("Fecha de emisión")) {
						modelo.setFechaEmision(newcontenido.split("\n")[i].split("Fecha de emisión")[1].replace("###", ""));
					}
				}
            }

         

			
			
			
			return modelo;
		} catch (Exception ex) {
			modelo.setError(
					SisnovaSaludModel.this.getClass().getTypeName() + " - catch:" + ex.getMessage() + " | " + ex.getCause());
			return modelo;
		}
	}
	

}
