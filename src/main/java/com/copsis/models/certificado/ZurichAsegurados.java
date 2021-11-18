package com.copsis.models.certificado;

import java.util.ArrayList;
import java.util.List;

import com.copsis.exceptions.GeneralServiceException;
import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraAseguradosModel;
import com.copsis.models.EstructuraJsonModel;

public class ZurichAsegurados {
	   // Clases
		private DataToolsModel fn = new DataToolsModel();
		private EstructuraJsonModel modelo = new EstructuraJsonModel();
		// variables
		private String contenido = "";
		private String newcontenido = "";
		private String recibosText = "";
		private String resultado = "";
		private String resultadoName = "";
		private String subgrupo= "";
		private String categoria = "";
		private int inicio = 0;
		private int fin = 0;

		public ZurichAsegurados(String contenido) {
			this.contenido = contenido;
			// this.recibosText = recibos;
		}
		
		public EstructuraJsonModel procesar() {
			contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());
			try {
				inicio = contenido.indexOf("Contratante");
				fin = contenido.indexOf("Certificado");
				
				if(inicio > 0 && fin >  0 && inicio < fin) {
					newcontenido = contenido.substring(inicio, fin).replace("@@@","").replace("\r", "").trim();
					
					for (int i = 0; i < newcontenido.split("\n").length; i++) {				
						if(newcontenido.split("\n")[i].contains("Contratante") && newcontenido.split("\n")[i].contains("Subgrupo")  && newcontenido.split("\n")[i].contains("Categoría"))
						{
							if(newcontenido.split("\n")[i+1].length() > 5) {
								modelo.setContratante(newcontenido.split("\n")[i+1].split("###")[0].trim());
							}else {
								modelo.setContratante(newcontenido.split("\n")[i+2].split("###")[0].trim());
							}				
						}
					}
				}
		
				
				newcontenido ="";
				for (int i = 0; i < contenido.split("Contratante").length; i++) {

					if (contenido.split("Contratante")[i].contains("Total de Asegurados")) {
						newcontenido += contenido.split("Contratante")[i].replace("@@@", "").replace("\r", "").split("Total de Asegurados")[0].replace("TITU", "TITULAR")
								.replace("CONY", "CONYUGUE").replace("Alta-Baja", "");
					}
				}
		
				List<EstructuraAseguradosModel> asegurados = new ArrayList<>();
				for (int i = 0; i < newcontenido.split("\n").length; i++) {

			
				if(newcontenido.split("\n")[i].contains("Subgrupo")) {				
					subgrupo = newcontenido.split("\n")[i+1].split("###")[1];
					categoria = newcontenido.split("\n")[i+1].split("###")[2];
				}
					EstructuraAseguradosModel asegurado = new EstructuraAseguradosModel();					
					if(newcontenido.split("\n")[i].split("-").length > 3) {											
						asegurado.setPrimaneta(fn.castBigDecimal(fn.preparaPrimas( newcontenido.split("\n")[i].split("###")[newcontenido.split("\n")[i].split("###").length -1 ].replace("Pesos", "").trim())));
						asegurado.setSa(newcontenido.split("\n")[i].split("###")[newcontenido.split("\n")[i].split("###").length -2 ]);
						asegurado.setCobertura(newcontenido.split("\n")[i].split("###")[newcontenido.split("\n")[i].split("###").length -3 ]);						
						asegurado.setFechaAlta(fn.formatDateMonthCadena( newcontenido.split("\n")[i].split("###")[newcontenido.split("\n")[i].split("###").length -4 ]));
						asegurado.setSexo(fn.sexo( newcontenido.split("\n")[i].split("###")[newcontenido.split("\n")[i].split("###").length -5 ]) ? 1 : 0 );
						asegurado.setEdad(Integer.parseInt(newcontenido.split("\n")[i].split("###")[newcontenido.split("\n")[i].split("###").length -6 ]));
						asegurado.setAntiguedad(fn.formatDateMonthCadena( newcontenido.split("\n")[i].split("###")[newcontenido.split("\n")[i].split("###").length -7 ]));

						if(newcontenido.split("\n")[i+1].split("-").length == 1) {
							asegurado.setNombre((newcontenido.split("\n")[i].split("###")[newcontenido.split("\n")[i].split("###").length -8] +" " + newcontenido.split("\n")[i+1]).replace("+", "").replace("  ", " ").trim());
						}else {
							asegurado.setNombre(newcontenido.split("\n")[i].split("###")[newcontenido.split("\n")[i].split("###").length -8].replace("+","").replace("  ", " ").trim());
						}

						asegurado.setParentesco(fn.parentesco(newcontenido.split("\n")[i].split("###")[newcontenido.split("\n")[i].split("###").length -9].trim()));
						asegurado.setCertificado(newcontenido.split("\n")[i].split("###")[newcontenido.split("\n")[i].split("###").length -10].trim());
						
						asegurado.setSubgrupo(subgrupo.trim());
						asegurado.setCategoria(categoria.trim());
						asegurados.add(asegurado);
					}
					
				}
				modelo.setAsegurados(asegurados);
				return modelo;
			} catch (Exception ex) {
				throw new GeneralServiceException("00001", "Ocurrio un error en el servicio ImpresionInter: "+ ex.getMessage());
			}
		}
}
