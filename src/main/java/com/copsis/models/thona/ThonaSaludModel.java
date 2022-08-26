package com.copsis.models.thona;

import java.util.ArrayList;
import java.util.List;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraAseguradosModel;
import com.copsis.models.EstructuraBeneficiariosModel;
import com.copsis.models.EstructuraCoberturasModel;
import com.copsis.models.EstructuraJsonModel;
import com.copsis.models.EstructuraRecibosModel;

public class ThonaSaludModel {
	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
	
	public EstructuraJsonModel procesar(String contenido) {
		int inicio = 0;
		int fin = 0;
		StringBuilder newcontenido = new StringBuilder();	
		contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());
		try {
			modelo.setTipo(3);
			modelo.setCia(102);
	
			inicio = contenido.indexOf("ACCIDENTES PERSONALES INDIVIDUAL");
			fin = contenido.indexOf("NOMBRE DEL ASEGURADO");			
			newcontenido.append( fn.extracted(inicio, fin, contenido).replace("día-mes-año: ", ""));			
			for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
				
				if(newcontenido.toString().split("\n")[i].contains("PÓLIZA:") && newcontenido.toString().split("\n")[i].contains("CONSECUTIVO") 
				 && newcontenido.toString().split("\n")[i].contains("AGENTE:") && newcontenido.toString().split("\n")[i].contains("OFICINA")		) {
					modelo.setPoliza(newcontenido.toString().split("\n")[i].split("PÓLIZA:")[1].split("CONSECUTIVO")[0].replace("###", ""));
					modelo.setCveAgente(newcontenido.toString().split("\n")[i].split("AGENTE:")[1].split("OFICINA")[0].replace("###", ""));
				}
				if(newcontenido.toString().split("\n")[i].contains("MONEDA:") && newcontenido.toString().split("\n")[i].contains("FORMA PAGO:") ) {
					modelo.setMoneda(fn.buscaMonedaEnTexto(newcontenido.toString().split("\n")[i]));
					modelo.setFormaPago(fn.formaPagoSring(newcontenido.toString().split("\n")[i]));
				}
				if(newcontenido.toString().split("\n")[i].contains("DESDE") && newcontenido.toString().split("\n")[i].contains("HASTA")  && newcontenido.toString().split("\n")[i].contains("PLAN") ) {				 
					if(newcontenido.toString().split("\n")[i].split("###").length == 6) {
					 modelo.setPlan(newcontenido.toString().split("\n")[i+1].split("###")[5]);
			 	    }					
					modelo.setVigenciaDe(fn.formatDateMonthCadena(fn.obtenVigePoliza(newcontenido.toString().split("\n")[i+1]).get(0)));
					modelo.setVigenciaA(fn.formatDateMonthCadena(fn.obtenVigePoliza(newcontenido.toString().split("\n")[i+1]).get(1)));
					modelo.setFechaEmision(fn.formatDateMonthCadena(fn.obtenVigePoliza(newcontenido.toString().split("\n")[i+1]).get(2)));				
				}
				
				if( newcontenido.toString().split("\n")[i].contains("NOMBRE") && newcontenido.toString().split("\n")[i].contains("CONTRATANTE") ) {
					modelo.setCteNombre(newcontenido.toString().split("\n")[i+1]);
				}
				if( newcontenido.toString().split("\n")[i].contains("DOMICILIO") && newcontenido.toString().split("\n")[i].contains("CONTRATANTE")
						&& newcontenido.toString().split("\n")[i].contains("RFC")) {
					modelo.setRfc(newcontenido.toString().split("\n")[i].split("RFC")[1].replace("###", "").trim());
					modelo.setCteDireccion(newcontenido.toString().split("\n")[i+1]);
					modelo.setCp(fn.obtenerCPRegex2(newcontenido.toString().split("\n")[i+1]));
				}
			}
			
		
			
			inicio = contenido.indexOf("NOMBRE DEL ASEGURADO");
			fin = contenido.indexOf("BENEFICIARIOS");
			newcontenido = new StringBuilder();
			newcontenido.append( fn.extracted(inicio, fin, contenido).replace("día-mes-año: ", ""));
			
			EstructuraAseguradosModel asegurado = new EstructuraAseguradosModel();
			List<EstructuraAseguradosModel> asegurados = new ArrayList<>();
			for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {			
				if(newcontenido.toString().split("\n")[i].contains("ASEGURADO")){
					asegurado.setNombre( newcontenido.toString().split("\n")[i+1]);
				}
				if(newcontenido.toString().split("\n")[i].contains("NACIMIENTO")){
					asegurado.setNacimiento(fn.formatDateMonthCadena(fn.obtenVigePoliza(newcontenido.toString().split("\n")[i]).get(0)));
				}
				if(newcontenido.toString().split("\n")[i].contains("SEXO") && newcontenido.toString().split("\n")[i].contains("NACIONALIDAD")){
					asegurado.setSexo(fn.sexo(newcontenido.toString().split("\n")[i].split("SEXO")[1].split("NACIONALIDAD")[0]) ? 1:0);
				}
			}
			asegurados.add(asegurado);
			modelo.setAsegurados(asegurados);
			
			inicio = contenido.indexOf("BENEFICIARIOS");
			fin = contenido.indexOf("PRIMA NETA");
			newcontenido = new StringBuilder();
			newcontenido.append( fn.extracted(inicio, fin, contenido));
			List<EstructuraBeneficiariosModel> beneficiarios = new ArrayList<>();			
			for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
				EstructuraBeneficiariosModel beneficiario = new EstructuraBeneficiariosModel();
				if(!newcontenido.toString().split("\n")[i].contains("BENEFICIARIOS") && !newcontenido.toString().split("\n")[i].contains("PORCENTAJE")) {
					beneficiario.setNombre(newcontenido.toString().split("\n")[i].split("###")[0]);
					beneficiario.setParentesco(fn.buscaParentesco(newcontenido.toString().split("\n")[i].split("###")[1]));
					beneficiario.setPorcentaje(fn.castInteger(newcontenido.toString().split("\n")[i].split("###")[2].replace("%", "").trim()));
					beneficiarios.add(beneficiario);
					
				}
			}
			
		
			
			inicio = contenido.indexOf("PRIMA NETA");
			fin = contenido.indexOf("ADVERTENCIA");
			newcontenido = new StringBuilder();
			newcontenido.append( fn.extracted(inicio, fin, contenido));
			
			for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
			
				if(newcontenido.toString().split("\n")[i].contains("PRIMA NETA")) {
					 List<String> valores = fn.obtenerListNumeros(newcontenido.toString().split("\n")[i+1]);
					 modelo.setPrimaneta(fn.castBigDecimal(fn.castDouble(valores.get(0))));
					 modelo.setDerecho(fn.castBigDecimal(fn.castDouble(valores.get(1))));
					 modelo.setIva(fn.castBigDecimal(fn.castDouble(valores.get(2))));			
					 modelo.setPrimaTotal(fn.castBigDecimal(fn.castDouble(valores.get(3))));
				}
			}

			inicio = contenido.indexOf("SUMAS ASEGURADAS");
			fin = contenido.indexOf("ESTA PÓLIZA QUEDA SUJETA");
			newcontenido = new StringBuilder();
			newcontenido.append( fn.extracted(inicio, fin, contenido));
			List<EstructuraCoberturasModel> coberturas = new ArrayList<>();		
			for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
				EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();
				if(!newcontenido.toString().split("\n")[i].contains("SUMAS ASEGURADAS ")) {
					cobertura.setNombre(newcontenido.toString().split("\n")[i].split("###")[0]);
					cobertura.setSa(newcontenido.toString().split("\n")[i].split("###")[1]);
					coberturas.add(cobertura);
				}
			}
			modelo.setCoberturas(coberturas);		
			
			List<EstructuraRecibosModel> recibos = new ArrayList<>();
			EstructuraRecibosModel recibo = new EstructuraRecibosModel();
			switch (modelo.getFormaPago()) {
			case 1:

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

			break;
			}
			modelo.setRecibos(recibos);
			
			return modelo;
		} catch (Exception ex) {
			modelo.setError(ThonaSaludModel.this.getClass().getTypeName() + " | " + ex.getMessage() + " | " + ex.getCause());
			return modelo;
		}
	}
}
