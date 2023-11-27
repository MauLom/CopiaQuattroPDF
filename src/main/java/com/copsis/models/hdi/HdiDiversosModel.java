package com.copsis.models.hdi;

import java.util.ArrayList;
import java.util.List;

import com.copsis.constants.ConstantsValue;
import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraCoberturasModel;
import com.copsis.models.EstructuraJsonModel;
import com.copsis.models.EstructuraUbicacionesModel;

public class HdiDiversosModel {
	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
	private String contenido = "";

	public HdiDiversosModel(String contenido) {
		this.contenido = contenido;
	}

	public EstructuraJsonModel procesar() {
		int inicio = 0;
		int fin = 0;
		String tipopolizatxt="";
		StringBuilder newcontenido = new StringBuilder();
		boolean cpvalid = true;
		contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales()).replace("pago", "Pago:").replace("agente",
				"Agente:");

		try {

			modelo.setTipo(7);
			modelo.setCia(14);

			inicio = contenido.indexOf("SEGURO DE DAÑOS");
			if (inicio == -1) {
				inicio = contenido.indexOf("Ramo: Daños");
			}
			fin = contenido.indexOf("El asegurado es:");

			if (fin == -1) {
				fin = contenido.indexOf("Datos de la empresa:");
			}

			newcontenido.append(fn.extracted(inicio, fin, contenido));

			for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {

				if (newcontenido.toString().split("\n")[i].contains(ConstantsValue.POLIZA_ACENT2) && newcontenido.toString().split("\n")[i].contains("Inciso")) {
					modelo.setPoliza(newcontenido.toString().split("\n")[i].split(ConstantsValue.POLIZA_ACENT2)[1].split("Inciso")[0].trim());
				}
				if (newcontenido.toString().split("\n")[i].contains(ConstantsValue.EMISION_MY_PT)) {
					modelo.setFechaEmision(fn.formatDateMonthCadena((newcontenido.toString().split("\n")[i].split(ConstantsValue.EMISION_MY_PT)[1].trim())));
				}
				if (newcontenido.toString().split("\n")[i].contains("Vigencia:")
						&& newcontenido.toString().split("\n")[i].contains("Desde")
						&& newcontenido.toString().split("\n")[i].contains("Hasta")
						&& fn.obtenVigePoliza2(newcontenido.toString().split("\n")[i]).size() == 2) {
					modelo.setVigenciaDe(fn
							.formatDateMonthCadena(fn.obtenVigePoliza2(newcontenido.toString().split("\n")[i]).get(0).trim()));
					modelo.setVigenciaA(fn
							.formatDateMonthCadena(fn.obtenVigePoliza2(newcontenido.toString().split("\n")[i]).get(1).trim()));
				}
				if (newcontenido.toString().split("\n")[i].contains("Pago:")
						&& newcontenido.toString().split("\n")[i].contains("Moneda:")) {
					modelo.setFormaPago(fn.formaPagoSring(newcontenido.toString().split("\n")[i]));
					modelo.setMoneda(fn.buscaMonedaEnTexto(newcontenido.toString().split("\n")[i]));
				}
				if (newcontenido.toString().split("\n")[i].contains("Agente:")
						&& newcontenido.toString().split("\n")[i].contains(ConstantsValue.NOMBRE2)) {
					modelo.setAgente(
							newcontenido.toString().split("\n")[i].split(ConstantsValue.NOMBRE2)[1].replace("###", "").trim());
				}
				if (newcontenido.toString().split("\n")[i].contains("Clave:")
						&& newcontenido.toString().split("\n")[i].contains("Oficina:")) {
					modelo.setCveAgente(newcontenido.toString().split("\n")[i].split("Clave:")[1].split("Oficina")[0]
							.replace("###", "").trim());
				}
				if (newcontenido.toString().split("\n")[i].contains(ConstantsValue.TIPO_DE_POLIZAPT)){
					tipopolizatxt = newcontenido.toString().split("\n")[i].split(ConstantsValue.TIPO_DE_POLIZAPT)[1];
				}
				
			}

			inicio = contenido.indexOf("El asegurado es:");
			fin = contenido.indexOf("Detalle de Cobertura:");	
			inicio = inicio == -1 ? contenido.indexOf("Datos de la empresa:"): inicio;					
			fin = fin ==-1 ? contenido.indexOf(ConstantsValue.SUMA_ASEGURADAMY):fin;					
			fin = fin == -1 ? contenido.indexOf("Datos de bienes"):fin;
			fin = fin == -1 ? contenido.indexOf("Detalle del Endoso:"):fin;


			newcontenido = new StringBuilder();
			newcontenido.append(fn.extracted(inicio, fin, contenido));
			for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
				if (newcontenido.toString().split("\n")[i].contains("RFC:")
						&& newcontenido.toString().split("\n")[i].contains("Cliente:")) {
					modelo.setRfc(newcontenido.toString().split("\n")[i].split("RFC:")[1].split("Cliente:")[0]
							.replace("###", "").trim());
				}
				if (newcontenido.toString().split("\n")[i].contains(ConstantsValue.NOMBRE2)) {
					modelo.setCteNombre(
							newcontenido.toString().split("\n")[i].split(ConstantsValue.NOMBRE2)[1].replace("###", "").trim());
				}
				if (newcontenido.toString().split("\n")[i].contains("Domicilio Fiscal:")) {
					modelo.setCteDireccion(
							newcontenido.toString().split("\n")[i].split("Fiscal:")[1].replace("###", "").trim());
				}
				if (newcontenido.toString().split("\n")[i].contains("C.P.") && cpvalid ) {
					modelo.setCp(newcontenido.toString().split("\n")[i].split("C.P.")[1].trim().substring(0, 5));
					cpvalid = false;
				}
			}

			inicio = contenido.indexOf("Detalle de Cobertura:");
			fin = contenido.indexOf(ConstantsValue.PRIMA_NETA);
			newcontenido = new StringBuilder();
			newcontenido.append(fn.extracted(inicio, fin, contenido));

			List<EstructuraCoberturasModel> coberturas = new ArrayList<>();
			EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();
			for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
				if(tipopolizatxt.contains("RESPONSABILIDAD CIVIL PROFESIONAL AGENTES")) {
					if (newcontenido.toString().split("\n")[i].contains(ConstantsValue.SUMA_ASEGURADAPM )) {
						cobertura.setNombre("Responsabilidad Civil");
						cobertura.setSa(newcontenido.toString().split("\n")[i].split(ConstantsValue.SUMA_ASEGURADAPM)[1]);
					}
					
				}else {
					if (newcontenido.toString().split("\n")[i].contains(ConstantsValue.SUMA_ASEGURADAPM)) {
						cobertura.setSa(newcontenido.toString().split("\n")[i].split(ConstantsValue.SUMA_ASEGURADAPM)[1]);
					}
					if (newcontenido.toString().split("\n")[i].contains("deducibles:")) {
						cobertura.setDeducible(newcontenido.toString().split("\n")[i].split("deducibles:")[1]);
					}
				}
				
			}

			if (cobertura.getSa().length()> 0) {
				coberturas.add(cobertura);
				modelo.setCoberturas(coberturas);
			}

			if (modelo.getCoberturas().isEmpty()) {

				inicio = contenido.indexOf(ConstantsValue.SUMA_ASEGURADAMY);
				fin = contenido.indexOf("Atención a siniestros");

				newcontenido = new StringBuilder();
				newcontenido.append(fn.extracted(inicio, fin, contenido).trim());

				for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
					EstructuraCoberturasModel cobertu = new EstructuraCoberturasModel();
					if (newcontenido.toString().split("\n")[i].length() > 0 &&  (!newcontenido.toString().split("\n")[i].contains(ConstantsValue.SUMA_ASEGURADAMY)
								&& !newcontenido.toString().split("\n")[i].contains("Unidad Especializada")
								&& newcontenido.toString().split("\n")[i].split("###").length == 2)) {
							
							    cobertu.setNombre(newcontenido.toString().split("\n")[i].split("###")[0]);				
								cobertu.setSa(newcontenido.toString().split("\n")[i].split("###")[1]);	
								coberturas.add(cobertu);
														
							

						
					}

				}

				modelo.setCoberturas(coberturas);
			}


			if (modelo.getCoberturas().isEmpty()) {
				inicio = contenido.indexOf("Giro:");
				fin = contenido.indexOf("Atención a siniestros");
				newcontenido = new StringBuilder();
				newcontenido.append(fn.extracted(inicio, fin, contenido).trim());

				for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
					EstructuraCoberturasModel cobertu = new EstructuraCoberturasModel();
					if (!newcontenido.toString().split("\n")[i].contains("Giro:")) {			
						switch (newcontenido.toString().split("\n")[i].split("###").length) {
						case 2:
							cobertu.setNombre(newcontenido.toString().split("\n")[i].split("###")[0]);
							cobertu.setDeducible(newcontenido.toString().split("\n")[i].split("###")[1]);
							coberturas.add(cobertu);
							break;
						case 3:

							break;
						default:
							break;
						}
					}
				}
				modelo.setCoberturas(coberturas);
				
			}

			
			inicio = contenido.indexOf(ConstantsValue.PRIMA_NETA);
			fin = contenido.indexOf("Desglose de Pagos:");
			fin = fin ==-1? contenido.indexOf("Artículo"):fin;
			
			
			newcontenido = new StringBuilder();
			newcontenido.append(fn.extracted(inicio, fin, contenido));
			
			for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {				
				if (newcontenido.toString().split("\n")[i].contains(ConstantsValue.PRIMA_NETA)) {
					modelo.setPrimaneta(fn
							.castBigDecimal(fn.castDouble(newcontenido.toString().split("\n")[i + 1].split("###")[0])));
					modelo.setRecargo(fn
							.castBigDecimal(fn.castDouble(newcontenido.toString().split("\n")[i + 1].split("###")[2])));
					modelo.setDerecho(fn
							.castBigDecimal(fn.castDouble(newcontenido.toString().split("\n")[i + 1].split("###")[6])));
					modelo.setIva(fn
							.castBigDecimal(fn.castDouble(newcontenido.toString().split("\n")[i + 1].split("###")[7])));
					modelo.setPrimaTotal(fn
							.castBigDecimal(fn.castDouble(newcontenido.toString().split("\n")[i + 1].split("###")[8])));
				}
			}
			

		
			inicio = contenido.indexOf("Domicilio de la Casa:");
			fin = contenido.indexOf("SECCIÓN");
			List<EstructuraUbicacionesModel> ubicaciones = new ArrayList<>();
			EstructuraUbicacionesModel ubicacion = new EstructuraUbicacionesModel();
			newcontenido = new StringBuilder();
			newcontenido.append(fn.extracted(inicio, fin, contenido));
			for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
				if(newcontenido.toString().split("\n")[i].contains("Domicilio") && newcontenido.toString().split("\n")[i].contains("Casa:")) {
					ubicacion.setCalle(newcontenido.toString().split("\n")[i].split("Casa")[1].replace("###", "").trim());
				}
				if(newcontenido.toString().split("\n")[i].contains("Techos") && newcontenido.toString().split("\n")[i].contains("Porcentaje")) {
					ubicacion.setTechos(fn.material(newcontenido.toString().split("\n")[i].split("Techos")[1].split("Porcentaje")[0].replace("###", "").trim()));
				}
				if(newcontenido.toString().split("\n")[i].contains("Muros") && newcontenido.toString().split("\n")[i].contains("Entrepisos")) {
				  ubicacion.setMuros(fn.material( newcontenido.toString().split("\n")[i].split("Muros")[1].split("Entrepisos")[0].replace("###", "").trim()));
				}
				if(newcontenido.toString().split("\n")[i].contains("Pisos:")) {
					ubicacion.setNiveles(0);
				}
			}
			if(newcontenido.length() > 100) {
				ubicaciones.add(ubicacion);
				modelo.setUbicaciones(ubicaciones);	
			}
			
			
			return modelo;
		} catch (Exception ex) {
			modelo.setError(
					HdiDiversosModel.this.getClass().getTypeName() + " | " + ex.getMessage() + " | " + ex.getCause());
			return modelo;
		}

	}
}