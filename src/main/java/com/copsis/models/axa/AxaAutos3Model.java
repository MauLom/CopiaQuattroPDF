package com.copsis.models.axa;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraJsonModel;

public class AxaAutos3Model {
	// Clases
	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();

	private String contenido;

	public AxaAutos3Model(String contenido) {
		this.contenido = contenido;
	}

	public EstructuraJsonModel procesar() {
		int inicio;
		int fin;
		String newcontenido;
		contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());
		try {
			// tipo
			modelo.setTipo(1);
			// cia
			modelo.setCia(20);

			inicio = contenido.indexOf("CARÁTULA DE PÓLIZA");
			fin = contenido.indexOf("Datos del Vehículo");

			if (inicio > 0 && fin > 0 && inicio < fin) {
				newcontenido = contenido.substring(inicio, fin).replace("@@@", "").replace("\r", "");
				for (int i = 0; i < newcontenido.split("\n").length; i++) {

					if (newcontenido.split("\n")[i].contains("Moneda:")
							&& newcontenido.split("\n")[i].contains("Póliza:")) {
						modelo.setMoneda(fn.moneda(newcontenido.split("\n")[i].split("Moneda:")[1].split("Póliza:")[0]
								.replace("###", "").strip()));
						modelo.setPoliza(newcontenido.split("\n")[i].split("Póliza:")[1].replace("###", "").strip());
					}
					if (newcontenido.split("\n")[i].contains("Nombre:") && newcontenido.split("\n")[i].contains("Edad:")
							&& newcontenido.split("\n")[i].contains("R.F.C:")) {
						modelo.setCteNombre(newcontenido.split("\n")[i].split("Nombre:")[1].split("Edad:")[0]
								.replace("###", "").strip());
						modelo.setRfc(newcontenido.split("\n")[i].split("R.F.C:")[1].replace("###", "").strip());
					}
					if (newcontenido.split("\n")[i].contains("Domicilio:")) {
						modelo.setCteDireccion(
								newcontenido.split("\n")[i].split("Domicilio:")[1].replace("###", "").strip() + " "
										+ newcontenido.split("\n")[i + 1].replace("###", "").strip());
					}
				}
			}
			inicio = contenido.indexOf("Datos del Vehículo");
			fin = contenido.indexOf("Datos de la Póliza");

			if (inicio > 0 && fin > 0 && inicio < fin) {
				newcontenido = contenido.substring(inicio, fin).replace("@@@", "").replace("\r", "");
				for (int i = 0; i < newcontenido.split("\n").length; i++) {				
					if (newcontenido.split("\n")[i].contains("Vehículo:")) {
                     modelo.setDescripcion(newcontenido.split("\n")[i].split("Vehículo:")[1].replace("###", "").strip());
					}
					if (newcontenido.split("\n")[i].contains("Motor:") && newcontenido.split("\n")[i].contains("Modelo:")) {
						modelo.setMotor(newcontenido.split("\n")[i].split("Motor:")[1].split("Modelo:")[0].replace("###", "").strip());
						modelo.setModelo(Integer.parseInt(newcontenido.split("\n")[i].split("Modelo:")[1].replace("###", "").strip()));
					}
					if (newcontenido.split("\n")[i].contains("Serie:") && newcontenido.split("\n")[i].contains("Capacidad:")) {
						modelo.setSerie(newcontenido.split("\n")[i].split("Serie:")[1].split("Capacidad:")[0].replace("###", "").strip());
					}
					if (newcontenido.split("\n")[i].contains("Placas:") && newcontenido.split("\n")[i].contains("Carga:")) {
						modelo.setPlacas(newcontenido.split("\n")[i].split("Placas:")[1].split("Carga:")[0].replace("###", "").strip());
					}

				}
			}
			
			
			inicio = contenido.indexOf("Datos de la Póliza");
			fin = contenido.indexOf("Datos Adicionales ");
//			System.out.println(contenido);

			if (inicio > 0 && fin > 0 && inicio < fin) {
				newcontenido = contenido.substring(inicio, fin).replace("@@@", "").replace("\r", "").replace("a las 12 hrs. del", "").replace("Vigencia :", "Vigencia:");
				for (int i = 0; i < newcontenido.split("\n").length; i++) {
					System.out.println("===> " + newcontenido.split("\n")[i]);
					if(newcontenido.split("\n")[i].contains("Vigencia:") && newcontenido.split("\n")[i].contains("al:")) {
						modelo.setVigenciaDe(fn.formatDate_MonthCadena( newcontenido.split("\n")[i].split("Vigencia:")[1].split("al:")[0].replaceAll("###", "").replace("  ", "").strip()));
						modelo.setVigenciaA(fn.formatDate_MonthCadena(newcontenido.split("\n")[i].split("Vigencia:")[1].split("al:")[1].replaceAll("###", "").replace("  ", "").strip()));
					}
					if(newcontenido.split("\n")[i].contains("Forma de Pago:")){
						modelo.setFormaPago(fn.formaPago(newcontenido.split("\n")[i].split("Forma de Pago:")[1].replace("###", "").strip()));
					}
					if(newcontenido.split("\n")[i].contains("Fo")){
						
					}
				}
			}
			
			return modelo;
		} catch (Exception e) {
			return modelo;
		}
	}

}
