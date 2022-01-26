package com.copsis.models.gnp;

import java.util.ArrayList;
import java.util.List;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraCoberturasModel;
import com.copsis.models.EstructuraJsonModel;

public class GnpVidaModel3 {
	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
	private String contenido = "";
	
	public GnpVidaModel3(String contenido) {
		this.contenido = contenido;
	}

	public EstructuraJsonModel procesar() {
		StringBuilder newcont = new StringBuilder();
		int inicio = 0;
		int fin = 0;
		contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());
		contenido = contenido.replace("P¾liza", "Poliza").replace("CaracterÝsticas", "Caracteristicas")
.replace("Raz¾n", "Razon");
		
		try {
			modelo.setTipo(5);
			modelo.setCia(18);
			
			
			inicio = contenido.indexOf("Seguro###de###Vida");
			fin = contenido.indexOf("Caracteristicas###del###grupo");
			
			if(inicio > -1 && fin > -1 && inicio < fin) {
				newcont.append(contenido.substring(inicio,fin).replace("@@@", "").replace("\r", ""));
				for (int i = 0; i < newcont.toString().split("\n").length; i++) {
			
					if(newcont.toString().split("\n")[i].contains("Poliza") && newcont.toString().split("\n")[i].contains("Vida")) {
						modelo.setPoliza(newcont.toString().split("\n")[i].split("Poliza")[1].replace("###", "").replace("No.", "").trim() );
					}
					if(newcont.toString().split("\n")[i].contains("Razon")) {
						modelo.setCteNombre(newcont.toString().split("\n")[i+2].replace("###", " "));
					}
					if(newcont.toString().split("\n")[i].contains("C.P.")) {
						modelo.setCp(newcont.toString().split("\n")[i].split("C.P.")[1].replace("###", " ").trim());
						modelo.setCteDireccion(newcont.toString().split("\n")[i].replace("###", " "));
					}
					if(newcont.toString().split("\n")[i].contains("R.F.C.") && newcont.toString().split("\n")[i].contains("Desde")) {
						modelo.setRfc(newcont.toString().split("\n")[i].split("R.F.C")[1].split("Desde")[0].replace("###", "").replace(".:", "").trim());					
						if(newcont.toString().split("\n")[i].split("Desde")[1].split("hrs.")[1].split("###").length == 4) {
							modelo.setVigenciaDe(fn.formatDateMonthCadena(fn.elimgatos(newcont.toString().split("\n")[i].split("Desde")[1].split("hrs.")[1]).replace("###", "-")));
							modelo.setFechaEmision(modelo.getVigenciaDe());
						}
					}
					if( newcont.toString().split("\n")[i].contains("Hasta") && newcont.toString().split("\n")[i].split("Hasta")[1].split("hrs.")[1].split("###").length == 4) {
						
							modelo.setVigenciaA(fn.formatDateMonthCadena(fn.elimgatos(newcont.toString().split("\n")[i].split("Hasta")[1].split("hrs.")[1]).replace("###", "-")));
						
					}
					if( newcont.toString().split("\n")[i].contains("Moneda")) {
						modelo.setMoneda(fn.buscaMonedaEnTexto(newcont.toString().split("\n")[i]));
					}
					if( newcont.toString().split("\n")[i].contains("Forma") && newcont.toString().split("\n")[i].contains("pago")) {
						modelo.setFormaPago(fn.formaPagoSring(newcont.toString().split("\n")[i]));
					}
					if( newcont.toString().split("\n")[i].contains("Prima") && newcont.toString().split("\n")[i].contains("Neta")) {
						modelo.setPrimaneta(fn.castBigDecimal(fn.castDouble(newcont.toString().split("\n")[i].split("Neta")[1].replace("###", "").trim())));
					}
					if( newcont.toString().split("\n")[i].contains("Importe") && newcont.toString().split("\n")[i].contains("total") &&  newcont.toString().split("\n")[i+1].contains("pagar")) {
						modelo.setPrimaTotal(fn.castBigDecimal(fn.castDouble(newcont.toString().split("\n")[i+1].split("pagar")[1].replace("###", "").trim())));
					}
				}
			}
			
		
			inicio = contenido.indexOf("Coberturas");
			fin = contenido.indexOf("La###descripci");
			if(inicio > -1 && fin > -1 && inicio < fin) {
				List<EstructuraCoberturasModel> coberturas = new ArrayList<>();
				newcont = new StringBuilder();
				newcont.append(contenido.substring(inicio,fin).replace("@@@", "").replace("\r", "")
				.replace("Cobertura###Bßsica###de###fallecimiento", "Cobertura Basica de fallecimiento")
				.replace("Pago###Parcial###de###Fallecimiento", "Pago Parcial de Fallecimiento")
						);
				for (int i = 0; i < newcont.toString().split("\n").length; i++) {
                 	EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();
					if(!newcont.toString().split("\n")[i].contains("Contratadas") && !newcont.toString().split("\n")[i].contains("Mes")
					&& !newcont.toString().split("\n")[i].contains("Duraci¾n") && !newcont.toString().split("\n")[i].contains("Importe")
					&& !newcont.toString().split("\n")[i].contains("utilidades")) {
					
						int x =newcont.toString().split("\n")[i].split("###").length; 
						if(x == 10  || x == 11) {
							cobertura.setNombre( newcont.toString().split("\n")[i].split("###")[0]);
							cobertura.setSa( newcont.toString().split("\n")[i].split("###")[1]);
							coberturas.add(cobertura);
						}
					}
				}
				modelo.setCoberturas(coberturas);
			}
			inicio = contenido.indexOf("Agente");
			fin = contenido.indexOf("Para###mayor");
			
			if(inicio > -1 && fin > -1 && inicio < fin) {
				newcont = new StringBuilder();
				newcont.append(contenido.substring(inicio,fin).replace("@@@", "").replace("\r", ""));
				for (int i = 0; i < newcont.toString().split("\n").length; i++) {

					if( newcont.toString().split("\n")[i].contains("Agente") && newcont.toString().split("\n")[i].contains("Clave")) {
						modelo.setAgente(newcont.toString().split("\n")[i].split("Agente")[1].split("Clave")[0].replace("###", " ").trim());
						modelo.setCveAgente(newcont.toString().split("\n")[i].split("Clave")[1].split("###")[1].replace("###", " ").trim());
					}
				}
			}
			
		
			return modelo;
		} catch (Exception ex) {
			
			modelo.setError(
					GnpVidaModel3.this.getClass().getTypeName() + " | " + ex.getMessage() + " | " + ex.getCause());
			return modelo;
		}
	}

}
