package com.copsis.models.banorte;

import java.util.ArrayList;
import java.util.List;

import com.copsis.constants.ConstantsValue;
import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraCoberturasModel;
import com.copsis.models.EstructuraJsonModel;
import com.copsis.models.EstructuraRecibosModel;
import com.copsis.models.EstructuraUbicacionesModel;

public class BanorteDiversos {
	// Clases
	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
	// Varaibles
	private String contenido = "";
	private String recibosText = "";
	

	public BanorteDiversos(String contenido, String recibos) {
		this.contenido = contenido;
		this.recibosText = recibos;
	}

	public EstructuraJsonModel procesar() {

		int inicio = 0;
		int fin = 0;
		String newcontenido = "";
		StringBuilder resultado = new StringBuilder();

		contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());
		contenido = contenido.replace("R ESPONSABILIDAD CIVIL GENERAL", " RESPONSABILIDAD CIVIL GENERAL")
				.replace("C RISTALES", "CRISTALES").replace("R OBO DE BIENES", "ROBO DE BIENES")
				.replace("D INERO Y VALORES", "DINERO Y VALORES").replace("EQ UIPO ELECTRONICO", "EQUIPO ELECTRONICO")
				.replace("SE RVICIOS DE ASISTENCIA", "SERVICIOS DE ASISTENCIA")
				.replace("R IESGOS HIDROMETEOROLOGICOS COASEGURO SEGUN ZONA",
						" RIESGOS HIDROMETEOROLOGICOS COASEGURO SEGUN ZONA")
				.replace("S.E.A ", "###S.E.A ").replace("Fraccionado", "fraccionado")
				.replace("hasta a las 12 hrs:", "Hasta las 12 hrs:").replace("Expreso", "###Expreso")
				.replace("Dentro", "Dentro###").replace("SUBLIMITE DEL ", "SUBLIMITE DEL###")
				.replace("Fuera SUBLIMITE", "Fuera SUBLIMITE###").replace("Asalto", "Asalto###")

		;
		recibosText = fn.remplazarMultiple(recibosText, fn.remplazosGenerales());

		try {
			// tipo
			modelo.setTipo(7);

			// cia
			modelo.setCia(35);

			// Poliza
			inicio = contenido.indexOf("PÓLIZA DE SEGURO");
			fin = contenido.indexOf("DATOS DE LAS COBERTURAS");
			if (inicio > -1 && fin > -1 && inicio < fin) {
				newcontenido = contenido.substring(inicio, fin).replace("\r", "").replace("@", "")
						.replace("A las 12 hrs desde:", "").replace("Hasta las 12 hrs:", "");
				for (int i = 0; i < newcontenido.split("\n").length; i++) {

					if (newcontenido.split("\n")[i].contains("PÓLIZA")
							&& newcontenido.split("\n")[i].contains("OFICINA")) {
						modelo.setPoliza(newcontenido.split("\n")[i + 1].split("###")[0].replace("-", ""));
						modelo.setPolizaGuion(newcontenido.split("\n")[i + 1].split("###")[0]);
					}
					if (newcontenido.split("\n")[i].contains("Nombre")
							&& newcontenido.split("\n")[i].contains(ConstantsValue.SOCIAL)
							&& newcontenido.split("\n")[i].contains("RFC:")) {
						modelo.setCteNombre(newcontenido.split("\n")[i].split(ConstantsValue.SOCIAL)[1].split("RFC:")[0]
								.replace("###", "").trim());
					} else if (newcontenido.split("\n")[i].contains("Nombre")
							&& newcontenido.split("\n")[i].contains(ConstantsValue.SOCIAL)) {
						modelo.setCteNombre(newcontenido.split("\n")[i].split(ConstantsValue.SOCIAL)[1].replace("###", "").trim());
					}
					if (newcontenido.split("\n")[i].contains("RFC:")) {
						modelo.setRfc(contenido.split("RFC:")[1].split("\n")[0].replace("\r", "").replace("###", "").trim());
					}

					if (newcontenido.split("\n")[i].contains("Calle")
							&& newcontenido.split("\n")[i].contains("Número:")) {
						resultado.append(newcontenido.split("\n")[i].split("Número:")[1].split("Código Postal")[0]);
						
					}
					if (newcontenido.split("\n")[i].contains("Población")
							&& newcontenido.split("\n")[i].contains("Municipio:")) {
						resultado.append(" ").append(newcontenido.split("\n")[i].split("Municipio:")[1]);

					}
					if (newcontenido.split("\n")[i].contains("Colonia")
							&& newcontenido.split("\n")[i].contains("RFC")) {
						resultado.append(" ").append(newcontenido.split("\n")[i].split("Colonia:")[1].split("RFC")[0]);
						
						modelo.setCteDireccion(fn.eliminaSpacios(resultado.toString().replace("Estado:", " ").replace(" Código Postal:", "")
								.replace("###", "").trim()));
					}
					if (newcontenido.split("\n")[i].contains("Emisión:")) {
						modelo.setFechaEmision(
								fn.formatDateMonthCadena(newcontenido.split("\n")[i].split("Emisión:")[1].replace("###", "").trim()));
					}
					if (newcontenido.split("\n")[i].contains(ConstantsValue.MONEDA2)
							&& newcontenido.split("\n")[i].contains("Vigencia")) {
						if(newcontenido.split("\n")[i].split(ConstantsValue.MONEDA2).length>1) {
							modelo.setMoneda(fn.moneda(newcontenido.split("\n")[i].split(ConstantsValue.MONEDA2)[1].trim()));
						}

						String x = fn.gatos(newcontenido.split("\n")[i + 1].replace("  ", "###").replace("######", "###"));

					    modelo.setVigenciaDe(fn.formatDateMonthCadena(x.split("###")[0].trim()));
						modelo.setVigenciaA(fn.formatDateMonthCadena(x.split("###")[1].trim()));
						if(modelo.getMoneda() == 0 && x.split("###").length > 1) {
							modelo.setMoneda(fn.moneda(x.split("###")[2].trim()));
						}

					}
					if (newcontenido.split("\n")[i].contains("Pago:")) {
						modelo.setFormaPago(fn.formaPago(newcontenido.split("\n")[i].split("Pago:")[1].replace("###", "").replace("PAGOS","").trim()));

					}
					if (newcontenido.split("\n")[i].contains("Código Postal:")) {
						modelo.setCp(newcontenido.split("\n")[i].split("Código Postal:")[1].replace("###", "").trim());
					}
					if (newcontenido.split("\n")[i].contains(ConstantsValue.PRIMA_NETA)
							&& newcontenido.split("\n")[i + 1].contains("fraccionado")) {

						String x = newcontenido.split("\n")[i + 2];
						modelo.setPrimaneta(fn.castBigDecimal(fn.castFloat(x.split("###")[0])));
						modelo.setDerecho(fn.castBigDecimal(fn.castFloat(x.split("###")[1])));
						modelo.setRecargo(fn.castBigDecimal(fn.castFloat(x.split("###")[2])));
						modelo.setIva(fn.castBigDecimal(fn.castFloat(x.split("###")[3])));
						modelo.setPrimaTotal(fn.castBigDecimal(fn.castFloat(x.split("###")[4])));

					}
				}
			}

			inicio = contenido.indexOf("nombre del Agente");
			fin = contenido.indexOf("En cumplimiento");

			if (inicio > 0 && fin > 0 && inicio < fin) {
				newcontenido = contenido.substring(inicio, fin).replace("\r", "").replace("@", "").trim();
				for (int i = 0; i < newcontenido.split("\n").length; i++) {
					if (newcontenido.split("\n")[i].contains(ConstantsValue.AGENTE)
							&& newcontenido.split("\n")[i].contains("-")) {

						modelo.setCveAgente(
								newcontenido.split("\n")[i].split(ConstantsValue.AGENTE)[1].split("-")[0].trim());
						modelo.setAgente(
								newcontenido.split("\n")[i].split(ConstantsValue.AGENTE)[1].split("-")[1].trim());

					}else if(newcontenido.split("\n")[i].contains(ConstantsValue.AGENTE)
							&& !newcontenido.split("\n")[i].contains("-") && (i+1)<newcontenido.split("\n").length) {
						String textoRenglonAgente = newcontenido.split("\n")[i].split(ConstantsValue.AGENTE)[1].trim();
						String textoOtroRenglon = newcontenido.split("\n")[i+1];
						String[] detalle = fn.gatos(textoRenglonAgente).split(" ");
						String agente = "";
						if(fn.isNumeric(detalle[0])) {
							modelo.setCveAgente(detalle[0]);
							agente = textoRenglonAgente.split(modelo.getCveAgente())[1];
						}
						if(!textoOtroRenglon.contains("-")) {
							agente = agente +" " + textoOtroRenglon.trim();
						}
						modelo.setAgente(agente);
					}

				}

			}
			
			if(contenido.contains("Fecha de Emisión")) {
				modelo.setFechaEmision(fn.formatDateMonthCadena(contenido.split("Fecha de Emisión")[1].replace(":", "").replace("###", "").split("\n")[0].trim()));
			}

			if (modelo.getVigenciaDe().length() > 0 && modelo.getFechaEmision().length() == 0) {
				modelo.setFechaEmision(modelo.getVigenciaDe());
			}

//	            /**/

			inicio = contenido.indexOf("DATOS DE LAS COBERTURAS");
			fin = contenido.indexOf("Seguros Banorte");
			if (inicio > fin) {
				fin = contenido.indexOf("contrato de Seguros:");
			}

			if (inicio > -1 && fin > -1 && inicio < fin) {
				List<EstructuraCoberturasModel> coberturas = new ArrayList<>();
				newcontenido = contenido.substring(inicio, fin).replace("\r", "").replace("@", "").trim()
						.replace("ED IFICIOS", "EDIFICIOS").replace("ED ###IFICIOS", "EDIFICIOS")
						.replace("CO NTENIDOS", "CONTENIDOS").replace("CO ###NTENIDOS", "CONTENIDOS")
						.replace("IN CE NDIO", ConstantsValue.INCENDIO).replace("IN C ENDIO", ConstantsValue.INCENDIO)
						.replace("AD ###JU ###NTA###", "ADJUNTA").replace("R IES ###GOS", "RIESGOS")
						.replace("SE ###G ###UN ZONA###", "SEGUN ZONA");

				for (int i = 0; i < newcontenido.split("\n").length; i++) {
					EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();
					if (newcontenido.split("\n")[i].split("###").length > 1
							&& !newcontenido.split("\n")[i].contains(ConstantsValue.INCENDIO)
							&& !newcontenido.split("\n")[i].contains("SECCIÓN")
							&& !newcontenido.split("\n")[i].contains("ADJUNTA")
							&& newcontenido.split("\n")[i].length() > 20) {

						cobertura.setNombre(newcontenido.split("\n")[i].split("###")[0]);
						cobertura.setSa(newcontenido.split("\n")[i].split("###")[1]);
						if (newcontenido.split("\n")[i].split("###").length > 2) {
							cobertura.setDeducible(newcontenido.split("\n")[i].split("###")[2]);
						}
						coberturas.add(cobertura);

					}
				}
				modelo.setCoberturas(coberturas);
			}
			
			if(modelo.getCoberturas().isEmpty()) {
				extraerCoberturas(contenido, modelo);
			}

			List<EstructuraRecibosModel> recibosList = new ArrayList<>();
			EstructuraRecibosModel recibo = new EstructuraRecibosModel();
			if (modelo.getFormaPago() == 1) {
				recibo.setReciboId("");
				recibo.setSerie("1/1");
				recibo.setVigenciaDe(modelo.getVigenciaDe());
				recibo.setVigenciaA(modelo.getVigenciaA());
				if (recibo.getVigenciaDe().length() > 0) {
					recibo.setVencimiento(fn.dateAdd(recibo.getVigenciaDe(), 30, 1));
				}
				recibo.setPrimaneta(modelo.getPrimaneta());
				recibo.setDerecho(modelo.getDerecho());
				recibo.setRecargo(modelo.getRecargo());
				recibo.setIva(modelo.getIva());
				recibo.setPrimaTotal(modelo.getPrimaTotal());
				recibo.setAjusteUno(modelo.getAjusteUno());
				recibo.setAjusteDos(modelo.getAjusteDos());
				recibo.setCargoExtra(modelo.getCargoExtra());
				recibosList.add(recibo);

				modelo.setRecibos(recibosList);

			}

			obtenerDatosUbicacion(contenido,modelo);
			return modelo;
		} catch (Exception ex) {
			modelo.setError(BanorteDiversos.this.getClass().getTypeName() + " - catch:" + ex.getMessage() + " | "
					+ ex.getCause());
			return modelo;
		}

	}


	private void extraerCoberturas(String texto, EstructuraJsonModel model) {
		StringBuilder contenidoCoberturas = new StringBuilder();
		for(int i =0; i<texto.split("DATOS DE LAS COBERTURAS").length;i++) {
			if(texto.split("DATOS DE LAS COBERTURAS")[i].contains("Seguros Banorte, S.A")) {
				contenidoCoberturas.append(" ").append(texto.split("DATOS DE LAS COBERTURAS")[i].split("Seguros Banorte, S.A")[0]);
			}
		}

		if(contenidoCoberturas.length() > 0 ) {
			String aux = contenidoCoberturas.toString().replace("@@@", "").replace("\r", "");
			contenidoCoberturas = new StringBuilder();
			contenidoCoberturas.append(aux);
			
			String[] arrContenido = contenidoCoberturas.toString().split("\n");
			for(int i=0; i< arrContenido.length;i++) {
				arrContenido[i] = completaTextoCobertura(arrContenido, i);
				arrContenido[i] = fn.gatos(arrContenido[i]);
			}
			
			List<EstructuraCoberturasModel> coberturas = new ArrayList<>();
			for (int i = 0; i < arrContenido.length; i++) {
				EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();
				if (arrContenido[i].split("###").length > 1
						&& !arrContenido[i].contains("SECCIÓN")
						&& arrContenido[i].length() > 20) {

					cobertura.setNombre(arrContenido[i].split("###")[0].trim());
					cobertura.setSa(arrContenido[i].split("###")[1].trim());
					if (arrContenido[i].split("###").length > 2) {
						cobertura.setDeducible(arrContenido[i].split("###")[2].trim());
					}
					coberturas.add(cobertura);

				}
			}
			model.setCoberturas(coberturas);
		}
	}
	
	private String completaTextoCobertura(String[] arrTexto,int i) {
		String texto = arrTexto[i];
		if(texto.contains("REDUC DE ING POR INTERRUPCION DE ACTIVIDAD")) {
			texto = completaTextoActualConLineaSiguiente(arrTexto, i, "REDUC DE ING POR INTERRUPCION DE ACTIVIDAD", "COM");
		}else if(texto.contains("* ROBO CON VIOLENCIA Y-O ASALTO FUERA EN") && (i+1) < arrTexto.length) {
			if(!texto.contains("PODER DE EMPLEADOS S") && arrTexto[i+1].contains("PODER DE EMPLEADOS S") && (i+2) < arrTexto.length) {
				String textoSiguiente = arrTexto[i+1];
				textoSiguiente = textoSiguiente.replace("PODER DE EMPLEADOS S###", "").replace("PODER DE EMPLEADOS S", "");

				if (textoSiguiente.split("###")[0].contains("S.E.A") && texto.split("###").length > 1) {
					String sumaAsegurada = texto.split("###")[1];
					texto = texto.replace(sumaAsegurada+"###", sumaAsegurada+"###S.E.A###");
					arrTexto[i+1] = textoSiguiente;
				}
				
				if(!textoSiguiente.contains("EQUIPO ELECTRONICO") && arrTexto[i+2].contains("EQUIPO ELECTRONICO")) {
					texto = texto.replace("* ROBO CON VIOLENCIA Y-O ASALTO FUERA EN", "*ROBO CON VIOLENCIA Y-O ASALTO FUERA EN PODER DE EMPLEADOS S EQUIPO ELECTRONICO");
					arrTexto[i+2] = arrTexto[i+2].replace("EQUIPO ELECTRONICO", "");
				}
				
				
			}
		}
		
		return texto;
	}
		
	private String completaTextoActualConLineaSiguiente(String[] arrTexto, int i, String textoActual, String textoSiguiente) {
		String texto = arrTexto[i];
		if(!texto.contains(textoSiguiente) && arrTexto[i+1].contains(textoSiguiente)) {
			texto = texto.replace(textoActual, textoActual + " " + textoSiguiente);
			arrTexto[i+1] = arrTexto[i+1].replace(textoSiguiente, "").replace(textoSiguiente+"###", "");
		}
		return texto;
	}
	
	private void obtenerDatosUbicacion(String textoContenido, EstructuraJsonModel model) {
		int inicio = textoContenido.indexOf("DATOS DEL BIEN ASEGURADO");
		int fin = textoContenido.indexOf("DATOS DE LA PÓLIZA");
		
		if(inicio > -1 && inicio < fin) {
			String texto = textoContenido.split("DATOS DEL BIEN ASEGURADO")[1].split("DATOS DE LA PÓLIZA")[0];
			if(texto.contains("Calle y número:") && texto.contains("Código postal")) {
				List<EstructuraUbicacionesModel> ubicaciones = new ArrayList<>();
				EstructuraUbicacionesModel ubicacion = new EstructuraUbicacionesModel();
				String calle = texto.split("Calle y número:")[1].split("Código postal")[0].replace("###","").trim();
				String numero = calle.split(" ")[calle.split(" ").length-1];
				ubicacion.setCalle(calle.trim());
				ubicacion.setNoExterno(numero);
				
				ubicacion.setCp(texto.split("Código postal:")[1].split("\n")[0].replace("###", "").trim());
				ubicaciones.add(ubicacion);
				model.setUbicaciones(ubicaciones);
			}
		}
	}
}
