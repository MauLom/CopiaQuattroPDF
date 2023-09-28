package com.copsis.models.mapfre;

import java.util.ArrayList;
import java.util.List;

import com.copsis.constants.ConstantsValue;
import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraAseguradosModel;
import com.copsis.models.EstructuraBeneficiariosModel;
import com.copsis.models.EstructuraCoberturasModel;
import com.copsis.models.EstructuraJsonModel;
import com.copsis.models.EstructuraRecibosModel;


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
		.replace("P ól i za nú m ero :", ConstantsValue.POLIZA_NUMERO)
				.replace("Mapfre México, S.A.", "Mapfre Tepeyac, S.A.")
				.replace("Fecha de Emisión", "Fecha de Emisiòn:")
				.replace("Prima Neta:", ConstantsValue.PRIMA_NETA4).replace("Plan de Seguro:", ConstantsValue.PLAN_SEGURO)
                .replace("DESCRIPCIÓN DE COBERTURAS",ConstantsValue.DESCRIPCION_COBERTURAS)
                .replace("Póliza número :", "Póliza Número:").replace("C.P.", "C.P:")
                .replace("RFC :", "R.F.C:").replace("Vigencia desde", "Vigencia Desde").replace("Clave de agente:", "Clave de Agente:")
                .replace("hasta", "Hasta").replace("Fecha de emisión:", "Fecha de Emisiòn:").replace("Forma de pago:", "Forma de Pago:")
                .replace("Gastos de expedición", "Gastos de Expedición").replace("Prima total:", "Prima Total:")
                .replace("CURP:", "C.U.R.P:");

		String newcontenido = "";
		int inicio = 0;
		int fin = 0;
		String renglon = "";
		String[] arrNewContenido;
		try {
			modelo.setTipo(5);
			modelo.setCia(22);
			inicio = contenido.indexOf("SEGURO DE VIDA");
			if(inicio == -1) {
				inicio = contenido.indexOf("PLAN SERVICIOS");
			}
			fin = contenido.indexOf("Mapfre Tepeyac, S.A.");
			
			if(fin == -1) {
				fin = contenido.indexOf("MAPFRE México, S.A.");
			}
	

			
			if (inicio > -1 && fin > -1 && inicio < fin) {
				newcontenido = contenido.substring(inicio, fin).replace("@@@", "").replace("\r", "").replace("### 00.00", "### 00.00###");
			
				arrNewContenido = newcontenido.split("\n");
				for (int i = 0; i < arrNewContenido.length; i++) {
				
					renglon = arrNewContenido[i];
					if (renglon.contains(ConstantsValue.POLIZA_NUMERO)) {
						
						modelo.setPoliza(
								renglon.split(ConstantsValue.POLIZA_NUMERO)[1].replace("###", "").replace(":","").trim());
					}

			
					leerDatosContratante(renglon,arrNewContenido,i);
					if(modelo.getAsegurados().isEmpty()) {
						obtenerAsegurado(arrNewContenido,i);
					}
					leerDatosAgenteYVigencia(renglon, arrNewContenido, i);
					leerDatosDePago(arrNewContenido, i);
					leerPrimasDerechoYRecargo(arrNewContenido, i, renglon);
					


				}
			}

		
			inicio = contenido.indexOf(ConstantsValue.PLAN_SEGURO);
			fin = contenido.indexOf(ConstantsValue.DESCRIPCION_COBERTURAS);
			if(fin  ==  -1) {
				fin = contenido.indexOf("Asegurados que ampara");
			}

			if (inicio > -1 && fin > -1 && inicio < fin) {
				newcontenido = contenido.substring(inicio, fin).replace("@@@", "").replace("\r", "");
				arrNewContenido = newcontenido.split("\n");
				leerPlan(arrNewContenido);
			}

			inicio = contenido.indexOf(ConstantsValue.DESCRIPCION_COBERTURAS);
			fin = contenido.indexOf("EL PLAZO DE GRACIA");
			if(fin == -1) {
				fin = contenido.lastIndexOf(ConstantsValue.PRIMA_NETA4);
			}
			
			
			if(modelo.getMoneda() ==0){
				modelo.setMoneda(1);
			}
				
			if(fn.diferencia(modelo.getVigenciaDe(), modelo.getVigenciaA()) > 10){

				modelo.setVigenciaA(fn.calcvigenciaA(modelo.getVigenciaDe(), 12));
			  }
		
			leerCoberturas(inicio,fin);
			inicio = contenido.indexOf("DESIGNACION DE LOS BENEFICIARIOS");
			leerBeneficiarios(inicio);
			buildRecibos();
	
			if(modelo.getAsegurados().isEmpty()){
				inicio = contenido.indexOf("NOMBRE PARENTESCO FECHA NAC.");
				fin  = contenido.lastIndexOf("COBERTURAS ASEGURADA");			
				StringBuilder conteAsegurado = new StringBuilder();
				if(inicio > -1 && fin > -1 && inicio < fin){
				conteAsegurado.append(contenido.split("NOMBRE PARENTESCO FECHA NAC.")[1].split("SUMA PLAZO PRIMA")[0].replace("@@@", "").replace("TITULAR", "###TITULAR###"));
				}
			
				if(!conteAsegurado.isEmpty()){
				List<EstructuraAseguradosModel> listAsegurados = new ArrayList<>();
			
				for(	int x=0; x < conteAsegurado.toString().split("\n").length ; x++){
					EstructuraAseguradosModel asegurado = new EstructuraAseguradosModel();
					if(conteAsegurado.toString().split("\n")[x].contains("-") ){
					asegurado.setNombre( conteAsegurado.toString().split("\n")[x].split("###")[0]);
					List<String> valores = fn.obtenVigePoliza(conteAsegurado.toString().split("\n")[x]);
					asegurado.setNacimiento(fn.formatDateMonthCadena(valores.get(0)));
					asegurado.setParentesco(1);
				    listAsegurados.add(asegurado);
					}
					
				}
				modelo.setAsegurados(listAsegurados);
				}
			
				}

			return modelo;
		} catch (Exception ex) {
			modelo.setError(MapfreVidaBModel.this.getClass().getTypeName() + " - catch:" + ex.getMessage() + " | "
					+ ex.getCause());
			return modelo;
		}

	}
	
	private void leerDatosContratante(String renglon,String[] arrNewContenido,int i) {
		if (renglon.contains("Contratante:")&& renglon.contains(ConstantsValue.RFC)) {
			modelo.setCteNombre(renglon.split("Contratante:")[1].split(ConstantsValue.RFC)[0]
					.replace("###", "").replace("C.U.R.P:", "").trim());
			modelo.setRfc(renglon.split(ConstantsValue.RFC)[1].replace("###", "").trim());
			if (arrNewContenido[i+1].contains("C.P:")) {
				modelo.setCp(arrNewContenido[i+1].split("C.P:")[1].replace("###", "").trim().substring(0, 5).trim());
			}
			
		}
		if (renglon.contains("Domicilio:")&& renglon.contains("Tel:")) {
			modelo.setCteDireccion(renglon.split("Domicilio:")[1].split("Tel:")[0]
					.replace("###", "").trim());
		}
	}
	
	private void leerDatosAgenteYVigencia(String renglon, String[] arrNewContenido, int i) {
		if (renglon.contains("Desde") && renglon.contains("Clave de Agente:")) {
			modelo.setVigenciaDe(fn.formatDateMonthCadena(
					renglon.split("Desde")[1].split("Clave de Agente:")[0]
							.replace("###", "").trim()));
			
			modelo.setCveAgente(arrNewContenido[i + 1].split("###")[1].replace("###", "").replace(":","").trim());
			modelo.setAgente(arrNewContenido[i + 1].split("###")[2].replace("###", "").replace(":","").trim());
			
			if(arrNewContenido[i+1].split("-").length  == 3) {
				if(arrNewContenido[i + 1].split("###").length == 3){
                   modelo.setCveAgente(arrNewContenido[i + 1].split("###")[1].replace("###", "").replace(":","").trim());				
				   modelo.setAgente(arrNewContenido[i + 1].split("###")[2].replace("###", "").replace(":","").trim());	
				}else{
					modelo.setCveAgente(arrNewContenido[i + 1].split("###")[2].replace("###", "").replace(":","").trim());				
				    modelo.setAgente(arrNewContenido[i + 1].split("###")[3].replace("###", "").replace(":","").trim());	
				}
				
			}else {
				modelo.setCveAgente(arrNewContenido[i + 1].split("###")[1].replace("###", "").replace(":","").trim());
				modelo.setAgente(arrNewContenido[i + 1].split("###")[2].replace("###", "").replace(":","").trim());
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
		
		if ((arrNewContenido[i].contains("Fecha de Emisiòn:") || arrNewContenido[i].contains("Fecha de emisiòn:"))
				&& arrNewContenido[i].contains("Forma de Pago:")
				&& arrNewContenido[i].contains("Moneda")) {
						
			modelo.setFechaEmision(fn.formatDateMonthCadena(
					arrNewContenido[i + 1].split("###")[0].replace("###", "").replace(" ", ""))
					.trim());
			modelo.setFormaPago(fn
					.formaPagoSring(arrNewContenido[i + 1].split("###")[1].replace("###", "").trim()));
			modelo.setMoneda(fn.buscaMonedaEnTexto(arrNewContenido[i + 1]));
		}
	}
	
	private void leerPrimasDerechoYRecargo(String[] arrNewContenido, int i,String renglon) {
		if (renglon.contains(ConstantsValue.PRIMA_NETA4)
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
						
						arrNewContenido[i].split(ConstantsValue.PLAN_SEGURO)[1].replace("####", "").replace(":","").trim());
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
                    .replace("PAGO###ADICIONAL###DE###SUMA###ASEGURADA###POR###INVALIDEZ###TOTAL###Y###PERMANENTE","PAGO ADICIONAL DE SUMA ASEGURADA POR INVALIDEZ TOTAL Y PERMANENTE")
                    .replace("ENFERMEDADES###GRAVES", "ENFERMEDADES GRAVES")
                    .replace("MUERTE###ACCIDENTAL###Y###PÉRDIDAS###ORGÁNICAS###COLECTIVA", "MUERTE ACCIDENTAL Y PÉRDIDAS ORGÁNICAS COLECTIVA")
                    .replace("SERVICIOS###FUNERARIOS","SERVICIOS FUNERARIOS")
                    .replace("Pago###de###suma###asegurada###por###invalidez###total###y###permanente###BIPA", "Pago de suma asegurada por invalidez total y permanente BIPA")
                    .replace("Muerte###accidental###MA", "Muerte accidental MA")
					.replace("###PAGO###ADICIONAL###DE###SUMA###ASEGURADA###X###INVALIDEZ###TOTAL###Y###PERMANENTE", "###PAGO ADICIONAL DE SUMA ASEGURADA X INVALIDEZ TOTAL Y PERMANENTE")
					.replace("EXENCION###POR###FALLECIMIENTO###AMPARADA", "EXENCION POR FALLECIMIENTO###AMPARADA");
			
                    
			
		
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
                    .replace("MADRE", "###MADRE###")
			        .replace("HERMANO-A", "###HERMANO###");
			arrNewContenido = newcontenido.split("\n"); 
			List<EstructuraBeneficiariosModel> beneficiarios = new ArrayList<>();
			for (int i = 0; i < arrNewContenido.length; i++) {
				EstructuraBeneficiariosModel beneficiario = new EstructuraBeneficiariosModel();
				renglon = arrNewContenido[i];			
				if(!renglon.contains("PARENTESCO")) {
					int sp =renglon.split("###").length;
					if(sp == 3) {
						beneficiario.setNombre(renglon.split("###")[0].trim());
						beneficiario.setParentesco(fn.parentesco( renglon.split("###")[1]));
						beneficiario.setPorcentaje(Integer.parseInt(renglon.split("###")[2].trim()));
						beneficiarios.add(beneficiario);
					}
				}
			}
			modelo.setBeneficiarios(beneficiarios);				
		}
	}
	
	private void obtenerAsegurado(String[] arrContenido,int i) {
		List<EstructuraAseguradosModel> listAsegurados = new ArrayList<>();
	

		if(arrContenido[i].contains("Asegurado") && arrContenido[i].contains("R.F.C") && arrContenido[i].contains("Nacimiento")) {
			EstructuraAseguradosModel asegurado = new EstructuraAseguradosModel();
			asegurado.setNombre(arrContenido[i].split("Asegurado")[1].split("R.F.C")[0].replace("###","").replace(":", "").trim());
			asegurado.setNacimiento(fn.formatDate(arrContenido[i].split("Nacimiento")[1].replace(":", "").replace("###", ""),"dd-MM-yyyy"));
			asegurado.setParentesco(1);
			
			if(arrContenido[i+1].contains("Edad")) {
				asegurado.setEdad(fn.castInteger(arrContenido[i+1].split("Edad")[1].replace(":", "").replace("###", "")));
			}
			listAsegurados.add(asegurado);
			modelo.setAsegurados(listAsegurados);
			
		}
	}
	
	private void buildRecibos() {
		if (modelo.getFormaPago() == 1) {
			List<EstructuraRecibosModel> listRecibos = new ArrayList<>();
			EstructuraRecibosModel recibo = new EstructuraRecibosModel();

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
			listRecibos.add(recibo);
			modelo.setRecibos(listRecibos);
		}
	}
}
