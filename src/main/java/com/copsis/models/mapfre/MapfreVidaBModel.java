package com.copsis.models.mapfre;

import java.util.ArrayList;
import java.util.List;

import com.copsis.constants.ConstantsValue;
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
		contenido = contenido.replace("las 12:00 hrs. de:", "").replace("P ól i za Nú m er o :", ConstantsValue.POLIZA_NUMERO)
				.replace("Mapfre México, S.A.", "Mapfre Tepeyac, S.A.")
				.replace("Fecha de Emisión", "Fecha de Emisiòn:")
				.replace("Prima Neta:", "Prima neta:").replace("Plan de Seguro:", ConstantsValue.PLAN_SEGURO)
                .replace("DESCRIPCIÓN DE COBERTURAS","DESCRIPCION DE COBERTURAS");;

		String newcontenido = "";
		int inicio = 0;
		int fin = 0;
		String renglon = "";
		String[] arrNewContenido;
		try {
			modelo.setTipo(5);
			modelo.setCia(22);
			inicio = contenido.indexOf("SEGURO DE VIDA ");
			if(inicio == -1) {
				inicio = contenido.indexOf("PLAN SERVICIOS");
			}
			fin = contenido.indexOf("Mapfre Tepeyac, S.A.");
	

			
			
			if (inicio > -1 && fin > -1 && inicio < fin) {
				newcontenido = contenido.substring(inicio, fin).replace("@@@", "").replace("\r", "")
						.replace("### 00.00", "### 00.00###");
				arrNewContenido = newcontenido.split("\n");
				for (int i = 0; i < arrNewContenido.length; i++) {
				
					renglon = arrNewContenido[i];
					if (renglon.contains(ConstantsValue.POLIZA_NUMERO)) {
						
						modelo.setPoliza(
								renglon.split(ConstantsValue.POLIZA_NUMERO)[1].replace("###", "").trim());
					}

			
					leerDatosContratante(renglon);
					leerDatosAgenteYVigencia(renglon, arrNewContenido, i);
					leerDatosDePago(arrNewContenido, i);
					leerPrimasDerechoYRecargo(arrNewContenido, i, renglon);
					


				}
			}

		
			inicio = contenido.indexOf(ConstantsValue.PLAN_SEGURO);
			fin = contenido.indexOf("DESCRIPCION DE COBERTURAS");
			if(fin  ==  -1) {
				fin = contenido.indexOf("Asegurados que ampara");
			}

			if (inicio > -1 && fin > -1 && inicio < fin) {
				newcontenido = contenido.substring(inicio, fin).replace("@@@", "").replace("\r", "");
				arrNewContenido = newcontenido.split("\n");
				leerPlan(arrNewContenido);
			}

			inicio = contenido.indexOf("DESCRIPCION DE COBERTURAS");
			fin = contenido.indexOf("EL PLAZO DE GRACIA");
			if(fin == -1) {
				fin = contenido.lastIndexOf("Prima neta:");
			}
			
		
			leerCoberturas(inicio,fin);
			inicio = contenido.indexOf("DESIGNACION DE LOS BENEFICIARIOS");
			leerBeneficiarios(inicio);
			return modelo;
		} catch (Exception ex) {
			modelo.setError(MapfreVidaBModel.this.getClass().getTypeName() + " - catch:" + ex.getMessage() + " | "
					+ ex.getCause());
			return modelo;
		}

	}
	
	private void leerDatosContratante(String renglon) {
		if (renglon.contains("Contratante:")
				&& renglon.contains(ConstantsValue.RFC)) {
			modelo.setCteNombre(renglon.split("Contratante:")[1].split(ConstantsValue.RFC)[0]
					.replace("###", "").replace("C.U.R.P:", "").trim());
			modelo.setRfc(renglon.split(ConstantsValue.RFC)[1].replace("###", "").trim());
			
		}
		if (renglon.contains("Domicilio:")
				&& renglon.contains("Tel:")) {
			modelo.setCteDireccion(renglon.split("Domicilio:")[1].split("Tel:")[0]
					.replace("###", "").trim());
		}
	}
	
	private void leerDatosAgenteYVigencia(String renglon, String[] arrNewContenido, int i) {
		if (renglon.contains("Desde") && renglon.contains("Clave de Agente:")) {
			modelo.setVigenciaDe(fn.formatDateMonthCadena(
					renglon.split("Desde")[1].split("Clave de Agente:")[0]
							.replace("###", "").trim()));
			
			modelo.setCveAgente(arrNewContenido[i + 1].split("###")[1].replace("###", "").trim());
			modelo.setAgente(arrNewContenido[i + 1].split("###")[2].replace("###", "").trim());
			
			if(arrNewContenido[i+1].split("-").length  == 3) {
				modelo.setCveAgente(arrNewContenido[i + 1].split("###")[2].replace("###", "").trim());
				modelo.setAgente(arrNewContenido[i + 1].split("###")[3].replace("###", "").trim());	
			}else {
				modelo.setCveAgente(arrNewContenido[i + 1].split("###")[1].replace("###", "").trim());
				modelo.setAgente(arrNewContenido[i + 1].split("###")[2].replace("###", "").trim());
			}
		}
		if (renglon.contains(ConstantsValue.HASTA2)) {
			if(renglon.split(ConstantsValue.HASTA2)[1].split("###")[0].contains("-")) {
				modelo.setVigenciaA(
						fn.formatDateMonthCadena(arrNewContenido[i].split(ConstantsValue.HASTA2)[1].split("###")[0]
								.replace("###", "").trim()));
			}else {
				modelo.setVigenciaA(
						fn.formatDateMonthCadena(arrNewContenido[i].split(ConstantsValue.HASTA2)[1].split("###")[1]
								.replace("###", "").trim()));
			}
			
		}
	}
	
	private void leerDatosDePago(String[] arrNewContenido, int i) {
		if (arrNewContenido[i].contains("Fecha de Emisiòn:")
				&& arrNewContenido[i].contains("Forma de Pago:")
				&& arrNewContenido[i].contains("Moneda")) {
			modelo.setFechaEmision(fn.formatDateMonthCadena(
					arrNewContenido[i + 1].split("###")[0].replace("###", "").replace(" ", ""))
					.trim());
			modelo.setFormaPago(fn
					.formaPago(arrNewContenido[i + 1].split("###")[1].replace("###", "").trim()));
			modelo.setMoneda(1);
		}
	}
	
	private void leerPrimasDerechoYRecargo(String[] arrNewContenido, int i,String renglon) {
		if (renglon.contains("Prima neta:")
				&& renglon.contains("Expedición")
				&& renglon.contains("Prima Total:")) {
			int sp = arrNewContenido[i + 1].split("###").length;

			switch (sp) {
			case 6:
				modelo.setPrimaneta(fn
						.castBigDecimal(fn.preparaPrimas(arrNewContenido[i + 1].split("###")[0])));
				modelo.setRecargo(fn
						.castBigDecimal(fn.preparaPrimas(arrNewContenido[i + 1].split("###")[2])));
				modelo.setDerecho(fn
						.castBigDecimal(fn.preparaPrimas(arrNewContenido[i + 1].split("###")[3])));
				modelo.setIva(fn.castBigDecimal(fn.preparaPrimas(
						arrNewContenido[i + 1].split("###")[4].replace("Exento", "").trim())));
				modelo.setPrimaTotal(fn
						.castBigDecimal(fn.preparaPrimas(arrNewContenido[i + 1].split("###")[5])));

				break;
				
			case 7:
				modelo.setPrimaneta(fn
						.castBigDecimal(fn.preparaPrimas(arrNewContenido[i + 1].split("###")[0])));
				modelo.setRecargo(fn
						.castBigDecimal(fn.preparaPrimas(arrNewContenido[i + 1].split("###")[2])));
				modelo.setDerecho(fn
						.castBigDecimal(fn.preparaPrimas(arrNewContenido[i + 1].split("###")[3])));
				modelo.setIva(fn.castBigDecimal(fn.preparaPrimas(
						arrNewContenido[i + 1].split("###")[5].replace("Exento", "").trim())));
				modelo.setPrimaTotal(fn
						.castBigDecimal(fn.preparaPrimas(arrNewContenido[i + 1].split("###")[6])));

				break;
			default:
				break;
			}
		}
	}
	
	private void leerPlan(String[] arrNewContenido) {
		for (int i = 0; i < arrNewContenido.length; i++) {

			if (arrNewContenido[i].contains(ConstantsValue.PLAN_SEGURO)) {
				modelo.setPlan(
						
						arrNewContenido[i].split(ConstantsValue.PLAN_SEGURO)[1].replace("####", "").trim());
			}
		}
	}
	
	private void leerCoberturas(int inicio, int fin) {
		String newcontenido = "";
		String[] arrNewContenido;
		String renglon = "";
		if (inicio > -1 && fin > -1 && inicio < fin) {
			List<EstructuraCoberturasModel> coberturas = new ArrayList<>();
			newcontenido = contenido.substring(inicio, fin).replace("@@@", "").replace("\r", "").replace(" ", "###")
					.replace("###VIDA###", "VIDA###")
					.replace("###MUERTE###ACCIDENTAL", "MUERTE ACCIDENTAL")
					.replace("###SERVICIOS###FUNERARIOS", "SERVICIOS FUNERARIOS")
                    .replace("EXENCIÓN###DE###PAGO###DE###PRIMAS###POR###INVALIDEZ###TOTAL###Y###PERMANENTE", "EXENCIÓN DE PAGO DE PRIMAS POR INVALIDEZ TOTAL Y PERMANENTE")
                    .replace("PAGO###ADICIONAL###DE###SUMA###ASEGURADA###POR###INVALIDEZ###TOTAL###Y###PERMANENTE","PAGO ADICIONAL DE SUMA ASEGURADA POR INVALIDEZ TOTAL Y PERMANENTE");
    
			arrNewContenido = newcontenido.split("\n");
			for (int i = 0; i < arrNewContenido.length; i++) {
				EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();
				renglon = arrNewContenido[i];
				if (!(renglon.contains("COBERTURAS")
						|| renglon.contains("Prima"))) {
					int sp = renglon.split("###").length;
					if (sp == 5 || sp == 6) {
						cobertura.setNombre(renglon.split("###")[0]);
						cobertura.setSa(renglon.split("###")[1]);
						coberturas.add(cobertura);
					}
				}

			}
			modelo.setCoberturas(coberturas);
		}
	}
	
	private void leerBeneficiarios(int inicio) {
		String newcontenido = "";
		String[] arrNewContenido;
		String renglon = "";
		if(inicio > -1) {
	         newcontenido = contenido.split("DESIGNACION DE LOS BENEFICIARIOS")[1];
	         if(newcontenido.indexOf("En testimonio de lo") >= 0) {
	        	 newcontenido = newcontenido.split("En testimonio de lo")[0];
	         }
		}
		
		if (inicio > -1 ) {
			newcontenido = newcontenido.replace("@@@", "").replace("\r", "")
					.replace("CONYUGE", "###CONYUGE###")
                    .replace("MADRE", "###MADRE###");;
			arrNewContenido = newcontenido.split("\n"); 
			List<EstructuraBeneficiariosModel> beneficiarios = new ArrayList<>();
			for (int i = 0; i < arrNewContenido.length; i++) {
				EstructuraBeneficiariosModel beneficiario = new EstructuraBeneficiariosModel();
				renglon = arrNewContenido[i];
				if(!renglon.contains("PARENTESCO")) {
					int sp =renglon.split("###").length;
					if(sp == 3) {
						beneficiario.setNombre(renglon.split("###")[0].trim());
						beneficiario.setParentesco(fn.parentesco( renglon.split("###")[0]));
						beneficiario.setPorcentaje(Integer.parseInt(renglon.split("###")[2].trim()));
						beneficiarios.add(beneficiario);
					}
				}
			}
			modelo.setBeneficiarios(beneficiarios);				
		}
	}
}
