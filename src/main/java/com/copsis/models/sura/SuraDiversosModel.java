
package com.copsis.models.sura;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.copsis.constants.ConstantsValue;
import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraCoberturasModel;
import com.copsis.models.EstructuraJsonModel;
import com.copsis.models.EstructuraRecibosModel;
import com.copsis.models.EstructuraUbicacionesModel;

public class SuraDiversosModel {

	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
	private String contenido;

	public SuraDiversosModel(String contenidox) {
		this.contenido = contenidox;
	}

	public EstructuraJsonModel procesar() {
		int inicio;
		int fin;
		StringBuilder newcontenido = new StringBuilder();
		contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());
		
		try {

			modelo.setTipo(7);
			modelo.setCia(88);

			inicio = contenido.indexOf("Seguro Múltiple Familiar");
			fin = contenido.indexOf("Ubicación de los bienes asegurados");

			if (inicio == -1) {
				inicio = contenido.indexOf("Múltiple Empresarial Riesgos");
			}

			if (inicio == -1) {
				inicio = contenido.indexOf("Seguros SURA, S.A. de C.V.");
				fin = contenido.indexOf("SEGÚN ESPECIFICACIÓN ANEXA");
			}

			
			newcontenido.append(fn.extracted(inicio, fin, contenido));

			String[] lineas = newcontenido.toString().split("\n");

			

			for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
				
				if (newcontenido.toString().split("\n")[i].contains("Datos del asegurado")) {
					modelo.setCteNombre(newcontenido.toString().split("\n")[i + 1].split("###")[0]);
					
				}
				
				if (newcontenido.toString().split("\n")[i].contains("Datos del contratante")) {
					modelo.setMoneda(fn.moneda(newcontenido.toString().split("\n")[i + 1]
							.split("###")[newcontenido.toString().split("\n")[i + 5].split("###").length - 1]));

					modelo.setCteNombre(newcontenido.toString().split("\n")[i + 1]
							.split("###")[newcontenido.toString().split("\n")[i + 4].split("###").length - 1]);

					modelo.setCteDireccion(newcontenido.toString().split("\n")[i - 4]
							.split("###")[newcontenido.toString().split("\n")[i].split("###").length - 1]);
				}
				
				if (newcontenido.toString().split("\n")[i].contains("Póliza no.")
						&& newcontenido.toString().split("\n")[i + 1].contains("C.P")) {
					modelo.setPoliza(newcontenido.toString().split("\n")[i + 1]
							.split("###")[newcontenido.toString().split("\n")[i + 1].split("###").length - 1]);
					modelo.setCp(newcontenido.toString().split("\n")[i + 1].split("C.P.")[1].trim().substring(0, 5));
				}
				
				if (newcontenido.toString().split("\n")[i].contains("Ramo")) {
					modelo.setRamo(newcontenido.toString().split("\n")[i + 1]
							.split("###")[newcontenido.toString().split("\n")[i + 1].split("###").length - 2]);
				}

				if (modelo.getPoliza().length() == 0 && newcontenido.toString().split("\n")[i].contains("PólizA no.")) {
					modelo.setPoliza(newcontenido.toString().split("\n")[i + 2]
							.split("###")[newcontenido.toString().split("\n")[i + 2].split("###").length - 1]);

					if (newcontenido.toString().split("\n")[i + 2].contains("C.P.")) {
						List<String> valores = fn
								.obtenerListNumeros2(newcontenido.toString().split("\n")[i + 2].split("C.P.")[1]);
						if (!valores.isEmpty()) {
							modelo.setCp(valores.stream()
									.filter(numero -> String.valueOf(numero).length() >= 4)
									.collect(Collectors.toList()).get(0));
							
						}
					}
				}

				if (newcontenido.toString().split("\n")[i].contains("Forma de pago")
						&& newcontenido.toString().split("\n")[i].contains("Fecha de emisión")) {
					modelo.setFormaPago(fn.formaPagoSring(newcontenido.toString().split("\n")[i + 1]));
					modelo.setFechaEmision(fn.formatDateMonthCadena(
							fn.obtenVigePoliza(newcontenido.toString().split("\n")[i + 1]).get(0)));
				}
				if (modelo.getFormaPago() == 0 && newcontenido.toString().split("\n")[i].contains("Forma de pago")
						) {
					modelo.setFormaPago(fn.formaPagoSring(newcontenido.toString().split("\n")[i + 1]));
					modelo.setFechaEmision(fn.formatDateMonthCadena(
							fn.obtenVigePoliza(newcontenido.toString().split("\n")[i + 1]).get(0)));
				}

				if (newcontenido.toString().split("\n")[i].contains("Vigencia desde")) {
					modelo.setVigenciaDe(fn
							.formatDateMonthCadena(fn.obtenVigePoliza(newcontenido.toString().split("\n")[i]).get(0)));
				}
				if (newcontenido.toString().split("\n")[i].contains("Hasta las")) {
					modelo.setVigenciaA(fn
							.formatDateMonthCadena(fn.obtenVigePoliza(newcontenido.toString().split("\n")[i]).get(0)));
				}


// Verificar que el índice `i` esté dentro de los límites del array
if (i >= 0 && i < lineas.length) {
    String lineaActual = lineas[i];
    
    // Manejar el caso de "R.F.C."
    if (lineaActual.contains("R.F.C.")) {
        // Tomar la parte de la línea después de "R.F.C."
        String[] parts = lineaActual.split("R.F.C.");
        if (parts.length > 1) {
            // Si hay al menos dos partes, tomar la primera y limpiarla
            String rfcPart = parts[1].split("###")[0].trim();
            modelo.setRfc(rfcPart);
        } 
    }

    // Manejar el caso de "R.F.C:" y "Vigencia"
    if (lineaActual.contains("R.F.C:") && lineaActual.contains("Vigencia")) {
        // Dividir por "R.F.C:" y luego por "Vigencia"
        String rfcPart = lineaActual.split("R.F.C:")[1].split("Vigencia")[0].replace("###", "").trim().split(" ")[0];
        modelo.setRfc(rfcPart);
    }
} 
			}
			

			inicio = contenido.indexOf("Ubicación de los bienes asegurados");
			fin = contenido.indexOf("Secciones contratadas");
			if (fin == -1) {
				fin = contenido.indexOf("Coberturas contratadas");
			}

			List<EstructuraUbicacionesModel> ubicaciones = new ArrayList<>();

			EstructuraUbicacionesModel ubicacion = new EstructuraUbicacionesModel();
			newcontenido = new StringBuilder();
			newcontenido.append(fn.extracted(inicio, fin, contenido));
			for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {

				if (newcontenido.toString().split("\n")[i].contains("Ubicación de los bienes")) {
					ubicacion.setNombre(newcontenido.toString().split("\n")[i + 1].split("###")[0]);
				}
				if (newcontenido.toString().split("\n")[i].contains("C.P.")) {
					List<String> valores = fn.obtenerListNumeros2(newcontenido.toString().split("\n")[i]);
					if (valores.isEmpty()) {
						valores = fn.obtenerListNumeros2(newcontenido.toString().split("\n")[i + 1]);
					}
					if (!valores.isEmpty()) {
						modelo.setCp(valores.get(0));
					}

				}
				if (newcontenido.toString().split("\n")[i].contains("CONSTRUCCIÓN")) {
					ubicacion.setMuros(1);
				}
				if (newcontenido.toString().split("\n")[i].contains("Pisos:")
						&& fn.isNumeric(newcontenido.toString().split("\n")[i].split("Pisos:")[1].split("###")[1])) {

					ubicacion.setNiveles(
							fn.castInteger(newcontenido.toString().split("\n")[i].split("Pisos:")[1].split("###")[1]));
				}
			}
			ubicaciones.add(ubicacion);
			modelo.setUbicaciones(ubicaciones);

			List<EstructuraCoberturasModel> coberturas = new ArrayList<>();

			inicio = contenido.indexOf("Coberturas contratadas");
			if (inicio == -1) {
				inicio = contenido.indexOf("Coberturas contratadas");
			}
			fin = contenido.indexOf("Costo del seguro");

			if (fin < inicio) {
				fin = contenido.lastIndexOf("Costo del seguro");
			}

			newcontenido = new StringBuilder();
			newcontenido.append(fn.extracted(inicio, fin, contenido));
			for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
				EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();
				if (!newcontenido.toString().split("\n")[i].contains("Suma asegurada")
						&& !newcontenido.toString().split("\n")[i].contains("SECCIÓN")
						&& !newcontenido.toString().split("\n")[i].contains("excluida")
						&& !newcontenido.toString().split("\n")[i].contains("asegurada")) {

					switch (newcontenido.toString().split("\n")[i].split("###").length) {
						case 2:
							cobertura.setNombre(newcontenido.toString().split("\n")[i].split("###")[0]);

							coberturas.add(cobertura);
							break;
						case 3:
							if (newcontenido.toString().split("\n")[i].split("###")[0].length() < 5) {
								cobertura.setNombre(newcontenido.toString().split("\n")[i].split("###")[1
								
								]);
							} else {
								cobertura.setNombre(newcontenido.toString().split("\n")[i].split("###")[0]);
							}
							cobertura.setSa(newcontenido.toString().split("\n")[i].split("###")[1]);
							coberturas.add(cobertura);
							break;
						case 4:
							cobertura.setNombre(newcontenido.toString().split("\n")[i].split("###")[0] + " "
									+ newcontenido.toString().split("\n")[i].split("###")[1]);
							cobertura.setSa(newcontenido.toString().split("\n")[i].split("###")[2]);
							coberturas.add(cobertura);
							break;
						default:
							break;
					}

				}

			}
			modelo.setCoberturas(coberturas);

			inicio = contenido.indexOf("Prima neta");
			fin = contenido.indexOf("Pág. 2");
			newcontenido = new StringBuilder();
			newcontenido.append(fn.extracted(inicio, fin, contenido));
			for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {

				if (newcontenido.toString().split("\n")[i].contains("Prima neta")
						&& newcontenido.toString().split("\n")[i].contains("financiamiento")) {
					List<String> valores = fn.obtenerListNumeros(newcontenido.toString().split("\n")[i + 1]);
					modelo.setPrimaneta(fn.castBigDecimal(fn.castDouble(valores.get(0))));
					modelo.setDerecho(fn.castBigDecimal(fn.castDouble(valores.get(2))));
					modelo.setRecargo(fn.castBigDecimal(fn.castDouble(valores.get(1))));
					modelo.setIva(fn.castBigDecimal(fn.castDouble(valores.get(4))));
					modelo.setPrimaTotal(fn.castBigDecimal(fn.castDouble(valores.get(5))));

				}
			}
			    modelo.setFormaPago(1);
            
            List<EstructuraRecibosModel> recibos = new ArrayList<>();
            EstructuraRecibosModel recibo = new EstructuraRecibosModel();

            if (modelo.getFormaPago() == 1) {
                recibo.setReciboId("");
                recibo.setSerie("1/1");
                recibo.setVigenciaDe(modelo.getVigenciaDe());
                recibo.setVigenciaA(modelo.getVigenciaA());
                if (recibo.getVigenciaDe().length() > 0) {
                    recibo.setVencimiento(fn.dateAdd(recibo.getVigenciaDe(), 30, 1));
                }
                recibo.setPrimaneta(fn.castBigDecimal(modelo.getPrimaneta(), 2));
                recibo.setDerecho(fn.castBigDecimal(modelo.getDerecho(), 2));
                recibo.setRecargo(fn.castBigDecimal(modelo.getRecargo(), 2));
                recibo.setIva(fn.castBigDecimal(modelo.getIva(), 2));

                recibo.setPrimaTotal(fn.castBigDecimal(modelo.getPrimaTotal(), 2));
                recibo.setAjusteUno(fn.castBigDecimal(modelo.getAjusteUno(), 2));
                recibo.setAjusteDos(fn.castBigDecimal(modelo.getAjusteDos(), 2));
                recibo.setCargoExtra(fn.castBigDecimal(modelo.getCargoExtra(), 2));
                recibos.add(recibo);

            }

            modelo.setRecibos(recibos);
            

			
			newcontenido = new StringBuilder();
			newcontenido.append(fn.extracted(inicio, fin, contenido));
			for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
			if (newcontenido.toString().split("\n")[i].contains("Agente:")) {
			
			modelo.setCveAgente(newcontenido.toString().split("\n")[i].split("Agente:")[1].trim().split(" ")[0].replace("###", ""));
			
			modelo.setAgente(newcontenido.toString().split("\n")[i].split(modelo.getCveAgente())[1].trim());
			
			}
			}

			return modelo;
		} catch (Exception ex) {
			ex.printStackTrace();
			modelo.setError(
					SuraDiversosModel.this.getClass().getTypeName() + " | " + ex.getMessage() + " | " + ex.getCause());
			return modelo;
		}
	}
}
