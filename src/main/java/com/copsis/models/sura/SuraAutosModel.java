package com.copsis.models.sura;

import java.util.ArrayList;
import java.util.List;

import com.copsis.constants.ConstantsValue;
import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraCoberturasModel;
import com.copsis.models.EstructuraJsonModel;

public class SuraAutosModel {

	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
	private String contenido;
	private String newcontenido;
	private int inicio;
	private int fin;

	public SuraAutosModel(String contenidox) {
		this.contenido = contenidox;
	}

	public EstructuraJsonModel procesar() {
		try {
			contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());
			contenido = contenido.replace("R. F. C:", ConstantsValue.RFC);
			modelo.setTipo(1);
			modelo.setCia(88);
		
			inicio = contenido.indexOf("Seguro de Automóviles");
			if (inicio == -1) {
				inicio = contenido.indexOf("SEGUROS AUTOS RESIDENTES");
			}
			if (inicio == -1) {
				inicio = contenido.indexOf("SEGUROS MOTOR TECHNICAL");
			}
			fin = contenido.indexOf("Coberturas contratadas");

			if (inicio > -1 && fin > -1 && inicio < fin) {
				newcontenido = contenido.substring(inicio, fin).replace("@@@", "").replace("\r", "");
				for (int i = 0; i < newcontenido.split("\n").length; i++) {
					if (newcontenido.split("\n")[i].contains("Póliza no.")) {
						if (newcontenido.split("\n")[i + 2].contains("C.P.")) {
							modelo.setPolizaGuion(newcontenido.split("\n")[i + 2].split("###")[3]);
							modelo.setPoliza(newcontenido.split("\n")[i + 2].split("###")[3].replace("-", "")
									.replace(" ", "").trim());
						}
						if (newcontenido.split("\n")[i + 1].contains("C.P.")) {
							modelo.setCp(newcontenido.split("\n")[i + 2].split("C.P.")[1].split("###")[0].trim());
						}
					}
					if (newcontenido.split("\n")[i].contains("Moneda")
							&& newcontenido.split("\n")[i].contains("Emisión")) {
						if (newcontenido.split("\n")[i + 1].contains("NACIONAL")
								|| newcontenido.split("\n")[i + 1].contains("Pesos")) {
							modelo.setMoneda(1);
						}
						int sp = newcontenido.split("\n")[i + 1].split("###").length;
						if (sp == 7) {
							modelo.setFormaPago(fn.formaPago(newcontenido.split("\n")[i + 1].split("###")[6]));
							modelo.setFechaEmision(
									fn.formatDateMonthCadena(newcontenido.split("\n")[i + 1].split("###")[5]));
						}
					}
					if (newcontenido.split("\n")[i].contains("Vigencia desde")) {
						modelo.setVigenciaDe(
								fn.formatDateMonthCadena(newcontenido.split("Vigencia desde")[1].split("###")[1]));
					}
					if (newcontenido.split("\n")[i].contains("Hasta las")
							&& newcontenido.split("\n")[i].contains("C.P.")) {
				
						modelo.setVigenciaA(
								fn.formatDateMonthCadena(newcontenido.split("Hasta las")[1].split("###")[1]));
						modelo.setCp(newcontenido.split("\n")[i].split("C.P.")[1].split("###")[0].trim());
					}
					

					if (newcontenido.split("\n")[i].contains(ConstantsValue.RFC)) {
						modelo.setRfc(newcontenido.split("\n")[i].split(ConstantsValue.RFC)[1].split("###")[0]);
					}
					if (newcontenido.split("\n")[i].contains("Datos del contratante")) {

						modelo.setCteNombre(newcontenido.split("\n")[i + 1].split("###")[0].replace("###", "").trim());
						String direccion = newcontenido.split("\n")[i + 2] + " "
								+ newcontenido.split("\n")[i + 3].split("C.P.")[0];
						modelo.setCteDireccion(direccion);
					}
					if (newcontenido.split("\n")[i].contains("Descripción")
							&& newcontenido.split("\n")[i].contains("Modelo")) {
						modelo.setDescripcion(newcontenido.split("\n")[i + 1].split("###")[0]);
						modelo.setModelo(fn.castInteger(newcontenido.split("\n")[i + 1].split("###")[1]));
					}

					if (newcontenido.split("\n")[i].contains("Motor") && newcontenido.split("\n")[i].contains("Serie")) {

						int sp = newcontenido.split("\n")[i + 1].split("###").length;
						if (sp == 3) {
							modelo.setSerie(newcontenido.split("\n")[i + 1].split("###")[0]);
						} else if (sp == 4) {
							modelo.setMotor(newcontenido.split("\n")[i + 1].split("###")[0]);
							modelo.setSerie(newcontenido.split("\n")[i + 1].split("###")[1]);
						}	
					}
					if (newcontenido.split("\n")[i].contains("Cve")) {
						modelo.setClave(newcontenido.split("\n")[i].split("Vehículo:")[1].split("###")[0].trim());
					}
				}
			}
			
			inicio = contenido.indexOf("Prima neta###Descuento");
			fin = contenido.indexOf("Pág. 1");
			if (inicio > -1 && fin > -1 && inicio < fin) {
				newcontenido = contenido.substring(inicio, fin).replace("@@@", "").replace("\r", "");
				for (int i = 0; i < newcontenido.split("\n").length; i++) {					
					if(newcontenido.split("\n")[i].contains("Prima neta")) {
						int sp = newcontenido.split("\n")[i+1].split("###").length;
						if(sp== 6) {
                            modelo.setPrimaneta(fn.castBigDecimal(fn.preparaPrimas( newcontenido.split("\n")[i+1].split("###")[0])));
                            modelo.setRecargo(fn.castBigDecimal(fn.preparaPrimas( newcontenido.split("\n")[i+1].split("###")[2])));
                            modelo.setDerecho(fn.castBigDecimal(fn.preparaPrimas( newcontenido.split("\n")[i+1].split("###")[3])));
                            modelo.setIva(fn.castBigDecimal(fn.preparaPrimas( newcontenido.split("\n")[i+1].split("###")[4])));
                            modelo.setPrimaTotal(fn.castBigDecimal(fn.preparaPrimas( newcontenido.split("\n")[i+1].split("###")[5])));
						}
					}
					
				}
				
			}

			
			inicio = contenido.indexOf("Coberturas contratadas");
			fin = contenido.indexOf("Prima neta###Descuento");
			if (inicio > -1 && fin > -1 && inicio < fin) {
				List<EstructuraCoberturasModel> coberturas = new ArrayList<>();
				newcontenido = contenido.substring(inicio, fin).replace("@@@", "").replace("\r", "");
				for (int i = 0; i < newcontenido.split("\n").length; i++) {
					EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();
					if(!newcontenido.split("\n")[i].contains("contratadas") || !newcontenido.split("\n")[i].contains("Prima neta")) {
						int sp = newcontenido.split("\n")[i].split("###").length;
					
						if(sp == 3 ) {
							cobertura.setNombre(newcontenido.split("\n")[i].split("###")[0]);
							cobertura.setSa(newcontenido.split("\n")[i].split("###")[1]);	
							coberturas.add(cobertura);
						}else if( sp == 4) {
							cobertura.setNombre(newcontenido.split("\n")[i].split("###")[0]);
							cobertura.setSa(newcontenido.split("\n")[i].split("###")[1]);
							cobertura.setDeducible(newcontenido.split("\n")[i].split("###")[3]);
							coberturas.add(cobertura);
						}						
					}
				}
				modelo.setCoberturas(coberturas);
			}
			
			
			return modelo;
		} catch (Exception e) {
			return modelo;
		}
	}

}
