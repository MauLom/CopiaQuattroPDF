package com.copsis.models.mapfre;

import java.util.ArrayList;
import java.util.List;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraBeneficiariosModel;
import com.copsis.models.EstructuraCoberturasModel;
import com.copsis.models.EstructuraJsonModel;

public class MapfreVidaBModel {
	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
	private String contenido = "";

	public MapfreVidaBModel(String contenido) {
		this.contenido = contenido;
	}

	public EstructuraJsonModel procesar() {
		contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());
		contenido = contenido.replace("las 12:00 hrs. de:", "").replace("P ól i za Nú m er o :", "Póliza Número:");
		String newcontenido = "";
		int inicio = 0;
		int fin = 0;

		try {
			modelo.setTipo(5);
			modelo.setCia(22);
			inicio = contenido.indexOf("SEGURO DE VIDA ");
			fin = contenido.indexOf("Mapfre Tepeyac, S.A.");
			
			
			if (inicio > -1 & fin > -1 & inicio < fin) {
				newcontenido = contenido.substring(inicio, fin).replace("@@@", "").replace("\r", "")
						.replace("### 00.00", "### 00.00###");
				for (int i = 0; i < newcontenido.split("\n").length; i++) {
					if (newcontenido.split("\n")[i].contains("Póliza Número:")) {
						modelo.setPoliza(
								newcontenido.split("\n")[i].split("Póliza Número:")[1].replace("###", "").strip());
					}
					if (newcontenido.split("\n")[i].contains("Contratante:")
							&& newcontenido.split("\n")[i].contains("R.F.C:")) {
						modelo.setCteNombre(newcontenido.split("\n")[i].split("Contratante:")[1].split("R.F.C:")[0]
								.replace("###", "").strip());
						modelo.setRfc(newcontenido.split("\n")[i].split("R.F.C:")[1].replace("###", "").strip());
					}
					if (newcontenido.split("\n")[i].contains("Domicilio:")
							&& newcontenido.split("\n")[i].contains("Tel:")) {
						modelo.setCteDireccion(newcontenido.split("\n")[i].split("Domicilio:")[1].split("Tel:")[0]
								.replace("###", "").strip());
					}
					if (newcontenido.split("\n")[i].contains("Desde")
							&& newcontenido.split("\n")[i].contains("Clave de Agente:")) {
						modelo.setVigenciaDe(fn.formatDate_MonthCadena(
								newcontenido.split("\n")[i].split("Desde")[1].split("Clave de Agente:")[0]
										.replace("###", "").strip()));
						modelo.setCveAgente(newcontenido.split("\n")[i + 1].split("###")[1].replace("###", "").strip());
						modelo.setAgente(newcontenido.split("\n")[i + 1].split("###")[2].replace("###", "").strip());
					}
					if (newcontenido.split("\n")[i].contains("Hasta")) {
						modelo.setVigenciaA(
								fn.formatDate_MonthCadena(newcontenido.split("\n")[i].split("Hasta")[1].split("###")[0]
										.replace("###", "").strip()));
					}

					if (newcontenido.split("\n")[i].contains("Fecha de Emisiòn:")
							&& newcontenido.split("\n")[i].contains("Forma de Pago:")
							&& newcontenido.split("\n")[i].contains("Moneda")) {
						modelo.setFechaEmision(fn.formatDate_MonthCadena(
								newcontenido.split("\n")[i + 1].split("###")[0].replace("###", "").replace(" ", ""))
								.strip());
						modelo.setFormaPago(fn
								.formaPago(newcontenido.split("\n")[i + 1].split("###")[1].replace("###", "").strip()));
						modelo.setMoneda(1);
					}

					if (newcontenido.split("\n")[i].contains("Prima neta:")
							&& newcontenido.split("\n")[i].contains("Expedición")
							&& newcontenido.split("\n")[i].contains("Prima Total:")) {
						int sp = newcontenido.split("\n")[i + 1].split("###").length;
						switch (sp) {
						case 6:
							modelo.setPrimaneta(fn
									.castBigDecimal(fn.preparaPrimas(newcontenido.split("\n")[i + 1].split("###")[0])));
							modelo.setRecargo(fn
									.castBigDecimal(fn.preparaPrimas(newcontenido.split("\n")[i + 1].split("###")[2])));
							modelo.setDerecho(fn
									.castBigDecimal(fn.preparaPrimas(newcontenido.split("\n")[i + 1].split("###")[3])));
							modelo.setIva(fn.castBigDecimal(fn.preparaPrimas(
									newcontenido.split("\n")[i + 1].split("###")[4].replace("Exento", "").strip())));
							modelo.setPrimaTotal(fn
									.castBigDecimal(fn.preparaPrimas(newcontenido.split("\n")[i + 1].split("###")[5])));

							break;
						}
					}

				}
			}

			inicio = contenido.indexOf("PLAN DE SEGURO:");
			fin = contenido.indexOf("DESCRIPCION DE COBERTURAS");
			if (inicio > -1 & fin > -1 & inicio < fin) {
				newcontenido = contenido.substring(inicio, fin).replace("@@@", "").replace("\r", "");
				for (int i = 0; i < newcontenido.split("\n").length; i++) {

					if (newcontenido.split("\n")[i].contains("PLAN DE SEGURO:")) {
						modelo.setPlan(
								newcontenido.split("\n")[i].split("PLAN DE SEGURO:")[1].replace("####", "").strip());
					}
				}

			}

			inicio = contenido.indexOf("DESCRIPCION DE COBERTURAS");
			fin = contenido.indexOf("EL PLAZO DE GRACIA");
			if (inicio > -1 & fin > -1 & inicio < fin) {
				List<EstructuraCoberturasModel> coberturas = new ArrayList<>();
				newcontenido = contenido.substring(inicio, fin).replace("@@@", "").replace("\r", "").replace(" ", "###")
						.replace("###VIDA###", "VIDA###")
						.replace("###MUERTE###ACCIDENTAL", "MUERTE ACCIDENTAL")
						.replace("###SERVICIOS###FUNERARIOS", "SERVICIOS FUNERARIOS");
				for (int i = 0; i < newcontenido.split("\n").length; i++) {
					EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();

					if (newcontenido.split("\n")[i].contains("COBERTURAS")
							|| newcontenido.split("\n")[i].contains("Prima")) {

					} else {
						int sp = newcontenido.split("\n")[i].split("###").length;
						switch (sp) {
						case 5: case 6:
							   cobertura.setNombre(newcontenido.split("\n")[i].split("###")[0]);
		                	   cobertura.setSa(newcontenido.split("\n")[i].split("###")[1]);
		                	   coberturas.add(cobertura);	
							break;

						}
					}

				}
				modelo.setCoberturas(coberturas);
			}
			
			

			inicio = contenido.indexOf("DESIGNACION DE LOS BENEFICIARIOS");
			if(inicio > -1) {
		         newcontenido = contenido.split("DESIGNACION DE LOS BENEFICIARIOS")[1];
		         if(newcontenido.indexOf("En testimonio de lo") > 0) {
		        	 newcontenido = newcontenido.split("En testimonio de lo")[0];
		         }
			}
			
			if (inicio > -1 ) {
				newcontenido = newcontenido.replace("@@@", "").replace("\r", "")
						.replace("CONYUGE", "###CONYUGE###");
				List<EstructuraBeneficiariosModel> beneficiarios = new ArrayList<>();
				for (int i = 0; i < newcontenido.split("\n").length; i++) {
					EstructuraBeneficiariosModel beneficiario = new EstructuraBeneficiariosModel();
					if(newcontenido.split("\n")[i].contains("PARENTESCO")) {}else {
						int sp =newcontenido.split("\n")[i].split("###").length;
						if(sp == 3) {
							beneficiario.setNombre(newcontenido.split("\n")[i].split("###")[0].strip());
							beneficiario.setParentesco(fn.parentesco( newcontenido.split("\n")[i].split("###")[0]));
							beneficiario.setPorcentaje(Integer.parseInt(newcontenido.split("\n")[i].split("###")[2].strip()));
							beneficiarios.add(beneficiario);
						}
					}
				}
				modelo.setBeneficiarios(beneficiarios);				
			}
			

			return modelo;
		} catch (Exception e) {
			return modelo;
		}

	}
}
