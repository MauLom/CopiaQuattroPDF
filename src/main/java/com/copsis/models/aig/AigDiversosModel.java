package com.copsis.models.aig;

import java.util.ArrayList;
import java.util.List;

import com.copsis.constants.ConstantsValue;
import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraCoberturasModel;
import com.copsis.models.EstructuraJsonModel;

public class AigDiversosModel {

	// Clases
	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
	// Varaibles
	private String contenido = "";
	

	public AigDiversosModel(String contenido) {
		this.contenido = contenido;
	}

	public EstructuraJsonModel procesar() {
		int inicio = 0;
		int fin = 0;
		String newcontenido = "";
		String newresultado = "";
		StringBuilder newcoberturas = new StringBuilder();
		
		contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());
		contenido = contenido.replace("MENSUAL", "###MENSUAL###").replace("EDIFICIO", "EDIFICIO###")
				.replace("CONTENIDOS", "CONTENIDOS###").replace("REMOCION DE ESCOMBROS", "REMOCION DE ESCOMBROS###")
				.replace("GASTOS EXTRAORDINARIOS", "GASTOS EXTRAORDINARIOS###")
				.replace("RESPONSABILIDAD CIVIL", "RESPONSABILIDAD CIVIL###").replace("CRISTALES", "CRISTALES###")
				.replace("EQUIPO ELECTRONICO Y-O ELECTRODOMESTICO", "EQUIPO ELECTRONICO Y-O ELECTRODOMESTICO###")
				.replace("ROBO CON VIOLENCIA Y-O ASALTO", "ROBO CON VIOLENCIA Y-O ASALTO###")
				.replace("SERVICIOS ADICIONALES", "SERVICIOS ADICIONALES###")
				.replace("I.V.A","IVA");
		try {
			// tipo
			modelo.setTipo(7);
			// cia
			modelo.setCia(3);
			// Datos del Contractante
			inicio = contenido.indexOf("PAQUETE");
			fin = contenido.indexOf(ConstantsValue.MONEDA_MAYUS);
			if (inicio > 0 && fin > 0 && inicio < fin) {
				newcontenido = contenido.substring(inicio, fin).replace("@@@", "").replace("\r", "").replace("  ",
						"###");
				for (int i = 0; i < newcontenido.split("\n").length; i++) {
					if ((newcontenido.split("\n")[i].contains("NÚMERO DE PÓLIZA") || newcontenido.split("\n")[i].contains("PÓLIZA") ) && modelo.getPoliza().length() == 0 ) {
						String separador = " ";
						if(newcontenido.split("\n")[i + 1].split(separador).length < 2 && newcontenido.split("\n")[i + 1].split("###").length > 2) {
							separador = "###";
						}
						modelo.setPoliza(newcontenido.split("\n")[i + 1].split(separador)[3].replace("###", "").trim());
					}
					if (newcontenido.split("\n")[i].contains("NOMBRE:")
							&& newcontenido.split("\n")[i].contains("R.F.C:")) {
						modelo.setCteNombre(newcontenido.split("\n")[i].split("NOMBRE:")[1].split("R.F.C:")[0].replace("###", "").trim());
					}
					if (newcontenido.split("\n")[i].contains("DIRECCIÓN:")) {
						newresultado = newcontenido.split("\n")[i].split("DIRECCIÓN:")[1] + " "
								+ newcontenido.split("\n")[i + 1];
						modelo.setCteDireccion(fn.eliminaSpacios(newresultado.replace("###", " ")).trim());
					}
					if (newcontenido.split("\n")[i].contains("CÓDIGO POSTAL:")) {
						modelo.setCp(newcontenido.split("\n")[i].split("CÓDIGO POSTAL:")[1].replace("###", "").trim());
					}
					if (newcontenido.split("\n")[i].contains("DE PAGO")) {
						int indexRenglonVigencia = i + 2;
						if(modelo.getFormaPago() == 0 && newcontenido.split("\n")[i + 1].contains("###")) {
							modelo.setFormaPago(fn.formaPago(newcontenido.split("\n")[i + 1].split("###")[1]));
						}else if(modelo.getFormaPago() == 0 && (i+3)<newcontenido.split("\n").length) {
							if(newcontenido.split("\n")[i+3].split("###").length>1) {
								String[] valores = newcontenido.split("\n")[i+3].split("###");
								modelo.setFormaPago(fn.formaPago(valores[valores.length-2].trim()));
								indexRenglonVigencia = i +4;
							}
						}
						newresultado = newcontenido.split("\n")[indexRenglonVigencia].replace("DÍAS", "").trim().replace(" ", "/");
						if(newresultado.split("/").length > 1) {
							String a = newresultado.split("/")[0] + "-" + newresultado.split("/")[1] + "-"
									+ newresultado.split("/")[2].split("###")[0].trim();
							
							modelo.setVigenciaDe(fn.formatDateMonthCadena(a));
							modelo.setFechaEmision(modelo.getVigenciaDe());
						}						
						if(newresultado.split("###").length> 1) {
							String b = newresultado.split("###")[1];
							if(b.split("/").length == 3) {
								b = b.replace("/", "-");
								modelo.setVigenciaA(fn.formatDateMonthCadena(b));
							}
						}

					}
				}
			}

			inicio = contenido.indexOf(ConstantsValue.MONEDA_MAYUS);
			fin = contenido.indexOf("FECHA DE EXPEDICIÓN:");
			if (inicio > 0 && fin > 0 && inicio < fin) {
				newcontenido = contenido.substring(inicio, fin).replace("@@@", "").replace("\r", "").replace(" ",
						"###");
				for (int i = 0; i < newcontenido.split("\n").length; i++) {
					if (newcontenido.split("\n")[i].contains(ConstantsValue.MONEDA_MAYUS)
							&& newcontenido.split("\n")[i].contains("PRIMA")) {
						modelo.setMoneda(fn.moneda(newcontenido.split("\n")[i + 1].split("###")[0].trim()));
						modelo.setPrimaneta(fn.castBigDecimal(fn.castDouble(newcontenido.split("\n")[i + 1]
								.split("###")[newcontenido.split("\n")[i + 1].split("###").length - 1])));
					}
					if (newcontenido.split("\n")[i].contains("FRACCIONADO")) {
						modelo.setRecargo(fn.castBigDecimal(fn.castDouble(newcontenido.split("\n")[i]
								.split("###")[newcontenido.split("\n")[i].split("###").length - 1])));
					}
					if (newcontenido.split("\n")[i].contains("EXPEDICIÓN")) {
						modelo.setDerecho(fn.castBigDecimal(fn.castDouble(newcontenido.split("\n")[i]
								.split("###")[newcontenido.split("\n")[i].split("###").length - 1])));
					}
					if (newcontenido.split("\n")[i].contains("IVA")) {
						modelo.setIva(fn.castBigDecimal(fn.castDouble(newcontenido.split("\n")[i]
								.split("###")[newcontenido.split("\n")[i].split("###").length - 1])));
					}
					if (newcontenido.split("\n")[i].contains("TOTAL")
							&& newcontenido.split("\n")[i].contains("PAGAR")) {
						modelo.setPrimaTotal(fn.castBigDecimal(fn.castDouble(newcontenido.split("\n")[i]
								.split("###")[newcontenido.split("\n")[i].split("###").length - 1])));
					}

				}
			}
			boolean cbo = false;
			for (int i = 0; i < contenido.split(ConstantsValue.SECCIONES_COBERTURAS).length; i++) {
				if (contenido.split(ConstantsValue.SECCIONES_COBERTURAS)[i].contains(ConstantsValue.MONEDA_MAYUS)) {
					newcoberturas.append(contenido.split(ConstantsValue.SECCIONES_COBERTURAS)[i].split(ConstantsValue.MONEDA_MAYUS)[0]);
				} else if (contenido.split(ConstantsValue.SECCIONES_COBERTURAS)[i].contains("En cumplimiento") && !cbo) {
					newcoberturas.append(contenido.split(ConstantsValue.SECCIONES_COBERTURAS)[i].split("En cumplimiento")[0]);
					cbo = true;
				}
			}
			if(!contenido.contains(ConstantsValue.SECCIONES_COBERTURAS) && contenido.contains("RIESGOS CUBIERTOS")) {
				String textoCoberturas = contenido.split("RIESGOS CUBIERTOS")[1];
				if(textoCoberturas.contains("En cumplimiento")) {
					textoCoberturas = textoCoberturas.split("En cumplimiento")[0].replace("@@@", "").replace("\r", "");
					newcoberturas = new StringBuilder();
					String[] arrContenido = textoCoberturas.split("\n");
					List<EstructuraCoberturasModel> coberturas = new ArrayList<>();

					for (int i = 0; i < arrContenido.length; i++) {
						EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();
						arrContenido[i] = arrContenido[i].trim();
						if (!arrContenido[i].contains("INCISO: 1") && arrContenido[i].length() > 0) {
							cobertura.setNombre(arrContenido[i].trim());
							coberturas.add(cobertura);
						}
					}
					modelo.setCoberturas(coberturas);
				}
			}
			
			if (newcoberturas.length() > 0) {
				List<EstructuraCoberturasModel> coberturas = new ArrayList<>();
				String auxStr = newcoberturas.toString();
				newcoberturas = new StringBuilder();
				newcoberturas.append(auxStr.replace("@@@", "").replace("\r", "").trim());
				for (int i = 0; i < newcoberturas.toString().split("\n").length; i++) {
					EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();
					if (newcoberturas.toString().split("\n")[i].split("###").length > 1) {
						cobertura.setNombre(newcoberturas.toString().split("\n")[i].split("###")[0].trim());
						cobertura.setSa(newcoberturas.toString().split("\n")[i].split("###")[1].trim());
						coberturas.add(cobertura);
					}
				}
				modelo.setCoberturas(coberturas);
			}
			
			obtenerDatosAgente(contenido,modelo);

			return modelo;
		} catch (Exception ex) {
			modelo.setError(AigDiversosModel.this.getClass().getTypeName() + " - catch:" + ex.getMessage() + " | "
					+ ex.getCause());
			return modelo;
		}

	}
	
	private void obtenerDatosAgente(String textoContenido, EstructuraJsonModel model) {
		if(textoContenido.contains("ORDEN DE TRABAJO DEL AGENTE")) {
			String texto = textoContenido.split("ORDEN DE TRABAJO DEL AGENTE")[1].replace("AGENTE ###", "AGENTE###");
			if(texto.contains("AGENTE###") && texto.contains("PROMOTOR")) {
				texto = texto.split("AGENTE###")[1].split("PROMOTOR")[0].replace("\n", "").replace("@@@", "").replace(":###","").replace(": ###", "");
				texto = fn.eliminaSpacios(texto).trim();
				String agente = "";
				if(texto.split(" ").length>0 && fn.isNumeric(texto.split(" ")[0])) {
					model.setCveAgente(texto.split(" ")[0]);
					agente =  texto.split(model.getCveAgente())[1];
				}
				
				if(agente.length()>0) {
					model.setAgente(agente.replace("\r", "").trim());
				}
			}
		}
	}

}
