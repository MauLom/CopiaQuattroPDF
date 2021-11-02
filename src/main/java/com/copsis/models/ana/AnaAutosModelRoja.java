package com.copsis.models.ana;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraCoberturasModel;
import com.copsis.models.EstructuraJsonModel;

public class AnaAutosModelRoja {

	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
	private String contenido = "";

	public AnaAutosModelRoja(String contenido) {
		this.contenido = contenido;
	}

	public EstructuraJsonModel procesar() {
		String newcontenido = "";
		String direccion = "";
		String vigencias = "";
		int inicio = 0;
		int fin = 0;

		contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());
		contenido = contenido.replace("Fecha de Exp.", "Fecha de Expedición")
				.replace("Pagos Subsec", "Pagos Subsecuentes")
				.replace("For. de Pago","Forma de pago")
				.replace("Subsec:","Subsecuente")
				.replace("Prima Net a :", "Prima Neta:")
				.replace("Prima Tota l:", "Prima Total:");
//		System.out.println(contenido);

		try {
			modelo.setTipo(1);
			// cia
			modelo.setCia(7);

			inicio = contenido.indexOf("PÓLIZA DE SEGURO AUTOMÓVILES");
			fin = contenido.indexOf("Coberturas Amparada");

			if (inicio > -1 && fin > -1 && inicio < fin) {
				newcontenido = contenido.substring(inicio, fin).replace("@@@", "").replace("\r", "");
				for (int i = 0; i < newcontenido.split("\n").length; i++) {
					System.out.println( newcontenido.split("\n")[i]);

					if (newcontenido.split("\n")[i].contains("Póliza") && newcontenido.split("\n")[i].contains("Inciso")
							&& newcontenido.split("\n")[i].contains("Endoso")) {
						modelo.setPoliza(newcontenido.split("\n")[i].split("Póliza:")[1].split("Inciso")[0]
								.replace("###", "").trim());
						modelo.setEndoso(newcontenido.split("\n")[i].split("Endoso:")[1].replace("###", "").trim());
					}
					if (newcontenido.split("\n")[i].contains("R.F.C:") && newcontenido.split("\n")[i].contains("No.Cliente:")&& newcontenido.split("\n")[i].contains("Pague")) {
						modelo.setRfc(newcontenido.split("\n")[i].split("R.F.C:")[1].split("No.Cliente")[0].replace("###", "").trim());
						modelo.setIdCliente(newcontenido.split("\n")[i].split("No.Cliente:")[1].split("Pague")[0].replace("###", "").trim());
					}
					
					if (newcontenido.split("\n")[i].contains("R.F.C:") && newcontenido.split("\n")[i].contains("No.Cliente:")&& newcontenido.split("\n")[i].contains("Clave Agente")) {
						modelo.setRfc(newcontenido.split("\n")[i].split("R.F.C:")[1].split("No.Cliente")[0].replace("###", "").trim());
						modelo.setIdCliente(newcontenido.split("\n")[i].split("No.Cliente:")[1].split("Clave Agente")[0].replace("###", "").trim());
					}
					
					if (newcontenido.split("\n")[i].contains("Nombre")
							&& newcontenido.split("\n")[i].contains("Contratante")
							&& newcontenido.split("\n")[i].contains("Dirección")) {
						modelo.setCteNombre(newcontenido.split("\n")[i + 1].replace("###", "").trim());

						if (newcontenido.split("\n")[i + 3].contains("Duración:")) {
							direccion = newcontenido.split("\n")[i + 4];

							if (newcontenido.split("\n")[i + 5].contains("Fecha de Expedición")) {
								direccion += newcontenido.split("\n")[i + 4].split("Fecha")[0];
							}
							modelo.setCteDireccion(direccion);
						}
					}else {
						if (newcontenido.split("\n")[i].contains("Nombre")
								&& newcontenido.split("\n")[i].contains("Dirección")) {
							modelo.setCteNombre(newcontenido.split("\n")[i + 1].replace("###", "").trim());

							if (newcontenido.split("\n")[i + 2].contains("Duración:")) {
								direccion = newcontenido.split("\n")[i + 3].split("Fecha")[0]  ;

								if (newcontenido.split("\n")[i + 4].contains("C.P.")) {
									direccion += " " + newcontenido.split("\n")[i + 4].split("###")[0] +"  " +newcontenido.split("\n")[i + 4].split("###")[1];
								}
								modelo.setCteDireccion(direccion.replace("###", ""));
							}
						}
						
					}
					if (newcontenido.split("\n")[i].contains("C.P.")) {
						modelo.setCp(newcontenido.split("C.P.")[1].substring(0, 5).trim());
					}
					if (newcontenido.split("\n")[i].contains("Expedición")
							&& newcontenido.split("\n")[i].contains("Desde")
							&& newcontenido.split("\n")[i].contains("Hasta")) {
						vigencias = fn.gatos(newcontenido.split("\n")[i + 1].replace("###", "").replace("D", "###")
								.replace("M", "###").replace("A", "###"));
						
					
						
						int sp = vigencias.split("###").length;
						if(sp == 12) {
							vigencias = fn.gatos(vigencias.split(vigencias.split("###")[2])[1]);
							 sp = vigencias.split("###").length;
						}
					
				
						switch (sp) {
						case 9:
							modelo.setVigenciaA(vigencias.split("###")[8] + "-" + vigencias.split("###")[7] + "-"
									+ vigencias.split("###")[6]);
							modelo.setVigenciaDe(vigencias.split("###")[5] + "-" + vigencias.split("###")[4] + "-"
									+ vigencias.split("###")[3]);
							modelo.setFechaEmision(vigencias.split("###")[2] + "-" + vigencias.split("###")[1] + "-"
									+ vigencias.split("###")[0]);
							break;

						}
					}
				
					if (newcontenido.split("\n")[i].contains("Cobertura:")
							&& newcontenido.split("\n")[i].contains("Pagos Subsecuentes")
							&& newcontenido.split("\n")[i].contains("Recargos")) {
						modelo.setPlan(newcontenido.split("\n")[i].split("Cobertura:")[1].split("Pagos")[0]
								.replace("###", "").trim());
						modelo.setRecargo(fn.castBigDecimal(fn
								.preparaPrimas(newcontenido.split("\n")[i].split("Recargos:")[1].replace("###", ""))));
					}
					if (newcontenido.split("\n")[i].contains("Moneda:")
							&& newcontenido.split("\n")[i].contains("Inicial:")
							&& newcontenido.split("\n")[i].contains("Gastos:")) {
						modelo.setDerecho(fn.castBigDecimal(
								fn.preparaPrimas(newcontenido.split("\n")[i].split("Gastos:")[1].replace("###", ""))));

					}
					if (newcontenido.split("\n")[i].contains("pago:")
							&& newcontenido.split("\n")[i].contains("Subsecuente")
							&& newcontenido.split("\n")[i].contains("I.V.A:")) {

						
						modelo.setFormaPago(fn.formaPago(
								newcontenido.split("\n")[i].split("Forma de pago:")[1].split("Subsecuente")[0]
										.replace("###", "").replace(".", "").trim()));
						modelo.setIva(fn.castBigDecimal(
								fn.preparaPrimas(newcontenido.split("\n")[i].split("I.V.A:")[1].replace("###", ""))));
					}

					if (newcontenido.split("\n")[i].contains("Prima Neta:")
							&& newcontenido.split("\n")[i].contains("Prima Total:")) {
						modelo.setPrimaneta(fn.castBigDecimal(fn.preparaPrimas(
								newcontenido.split("\n")[i].split("Prima Neta:")[1].split("Prima Total:")[0]
										.replace("###", ""))));
						modelo.setPrimaTotal(fn.castBigDecimal(fn.preparaPrimas(
								newcontenido.split("\n")[i].split("Prima Total:")[1].replace("###", ""))));
					}
					if (newcontenido.split("\n")[i].contains("Descripción:")) {
						modelo.setDescripcion(newcontenido.split("\n")[i].split("Descripción:")[1].replace("###", ""));
					}
					if (newcontenido.split("\n")[i].contains("No.Motor:")
							&& newcontenido.split("\n")[i].contains("Capacidad:")
							&& newcontenido.split("\n")[i].contains("Modelo:")) {
						modelo.setMotor(newcontenido.split("\n")[i].split("No.Motor:")[1].split("Capacidad")[0]
								.replace("###", ""));
						modelo.setModelo(Integer
								.parseInt(newcontenido.split("\n")[i].split("Modelo:")[1].replace("###", "").trim()));
					}

					if (newcontenido.split("\n")[i].contains("Serie:")
							&& newcontenido.split("\n")[i].contains("Placas:")) {
						modelo.setSerie(
								newcontenido.split("\n")[i].split("Serie:")[1].split("Placas:")[0].replace("###", ""));
						modelo.setPlacas(newcontenido.split("\n")[i].split("Placas:")[1].replace("###", "").trim());
					}

				}
			}

			
//			System.out.println(contenido);
			
             modelo.setMoneda(1);
             

 			inicio = contenido.indexOf("Canal de Venta, Agente:");
 			fin = contenido.indexOf("Tel");
 			System.out.println(inicio  +"---> " + fin);

 			if (inicio > -1 && fin > -1 && inicio < fin) {
 				newcontenido = contenido.substring(inicio, fin).replace("@@@", "").replace("\r", "");
				for (int i = 0; i < newcontenido.split("\n").length; i++) {
					modelo.setCveAgente(newcontenido.split("\n")[i].split("Agente:")[1].split("###")[0]);
				}
 			}

			inicio = contenido.indexOf("Coberturas Amparada");
			fin = contenido.indexOf("A.N.A. Compañía de Seguros");

			if (inicio > -1 && fin > -1 && inicio < fin) {
				List<EstructuraCoberturasModel> coberturas = new ArrayList<>();
				newcontenido = contenido.substring(inicio, fin).replace("@@@", "").replace("\r", "");
				for (int i = 0; i < newcontenido.split("\n").length; i++) {
					EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();

					if (newcontenido.split("\n")[i].contains("Unidad de Medida")
							|| newcontenido.split("\n")[i].contains("www.anaseguros")
							|| newcontenido.split("\n")[i].contains("Valor Comercial")
							|| newcontenido.split("\n")[i].contains("Las sumas aseguradas de RC")
							|| newcontenido.split("\n")[i].contains("Aplican condiciones generales")
							|| newcontenido.split("\n")[i].contains("Coberturas Amparadas")) {
					} else {
						int sp = newcontenido.split("\n")[i].split("###").length;
						switch (sp) {
						case 2:
						case 3:
							cobertura.setNombre(newcontenido.split("\n")[i].split("###")[0]);
							cobertura.setSa(newcontenido.split("\n")[i].split("###")[1]);
							coberturas.add(cobertura);					
							break;
						case 4:
							cobertura.setNombre(newcontenido.split("\n")[i].split("###")[0]);
							cobertura.setDeducible(newcontenido.split("\n")[i].split("###")[1].trim());
							cobertura.setSa(newcontenido.split("\n")[i].split("###")[2]);
							coberturas.add(cobertura);			
							break;
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
