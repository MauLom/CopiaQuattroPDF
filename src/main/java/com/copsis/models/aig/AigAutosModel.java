package com.copsis.models.aig;

import java.util.ArrayList;
import java.util.List;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraCoberturasModel;
import com.copsis.models.EstructuraJsonModel;


public class AigAutosModel {

	// Clases
	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
	// Varaibles
	private String contenido = "";	
	private static final String RFC = "R.F.C:";
	private static final String MONEDA="Moneda";
	private static final String RIESGOSCUBIERTOS = "RIESGOS CUBIERTOS";

	public AigAutosModel(String contenido) {
		this.contenido = contenido;
	}
	
	public EstructuraJsonModel procesar() {
		StringBuilder resultado = new StringBuilder();
		StringBuilder newCobertura = new StringBuilder();
		StringBuilder newcontenido = new StringBuilder();
		int inicio = 0;
		int fin = 0;
		contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());
		contenido = contenido.replace("DE LAS 12:00 HORAS del ", "").replace("A LAS 12:00 HORAS del", "###")
				.replace(" de ", "-")
				.replace("DAÑO MATERIAL", "DAÑO MATERIAL###")
				.replace("ROBO TOTAL", "ROBO TOTAL###")
				.replace("RESPONSABILIDAD CIVIL L.U.C.", "RESPONSABILIDAD CIVIL L.U.C.###")
				.replace("GASTOS MEDICOS OCUPANTES ", "GASTOS MEDICOS OCUPANTES ###")
				.replace("ASISTENCIA VIAL Y EN VIAJES", "ASISTENCIA VIAL Y EN VIAJES###")
				.replace("S.O.S. DEFENSA LEGAL Y FIANZ", " S.O.S. DEFENSA LEGAL Y FIANZ###")
				.replace("AUTO SUSTITUTO POR PERDIDA T", " AUTO SUSTITUTO POR PERDIDA T###")
				.replace("ELIMINACION DE DEDUCIBLE POR", " ELIMINACION DE DEDUCIBLE POR###")
				.replace("DEVOLUCION DE PRIMA POR P.T.", " DEVOLUCION DE PRIMA POR P.T.###")
				;
	
		try {
			

			//tipo
			modelo.setTipo(1);

			//cia
			modelo.setCia(3);
			
			//Datos del contratante
			
			inicio  = contenido.indexOf("SEGURO DE AUTOMÓVILES");
			fin = contenido.indexOf("RIESGOS CUBIERTOS ");
			if(inicio >  0 && fin > 0 && inicio < fin) {
				newcontenido.append(contenido.substring( inicio, fin).replace("@@@", "").replace("\r", ""));
				for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
					if(newcontenido.toString().split("\n")[i].contains("PÓLIZA:")) {					
						modelo.setPoliza(newcontenido.toString().split("\n")[i].split("PÓLIZA")[1].split("-")[2].trim());
					}
					if(newcontenido.toString().split("\n")[i].contains("VIGENCIA DEL SEGURO")) {
						modelo.setVigenciaDe( fn.formatDateMonthCadena(newcontenido.toString().split("\n")[i+1].split("###")[0].trim()));
						modelo.setVigenciaA( fn.formatDateMonthCadena(newcontenido.toString().split("\n")[i+1].split("###")[1].trim()));
					}
					if(newcontenido.toString().split("\n")[i].contains("Nombre:") && newcontenido.toString().split("\n")[i].contains(RFC)) {
						modelo.setCteNombre(newcontenido.toString().split("\n")[i].split("Nombre:")[1].split(RFC)[0].trim());
						modelo.setRfc(newcontenido.toString().split("\n")[i].split(RFC)[1]);						
					}
					if(newcontenido.toString().split("\n")[i].contains("Código Postal:")) {
						modelo.setCp(newcontenido.toString().split("\n")[i].split("Código Postal:")[1].trim());
					}
					
					if(newcontenido.toString().split("\n")[i].contains("Calle:")) {
						resultado.append(newcontenido.toString().split("\n")[i].split("Calle:")[1]);
					}
					
					if(newcontenido.toString().split("\n")[i].contains("Colonia:")) {
						resultado.append(newcontenido.toString().split("\n")[i].split("Colonia:")[1]);						
					}
                     if(newcontenido.toString().split("\n")[i].contains("Estado-Municipio:")) {
						modelo.setCteDireccion((resultado.append(newcontenido.toString().split("\n")[i].split("Estado-Municipio:")[1]).toString().trim()));
					}
                     
                     if(newcontenido.toString().split("\n")[i].contains("Conductor Habitual:")) {
                    	 modelo.setConductor(newcontenido.toString().split("\n")[i].split("Habitual:")[1].trim());
                    	 
                     }
					
					if (newcontenido.toString().split("\n")[i].contains("Marca:")) {
						modelo.setMarca(newcontenido.toString().split("\n")[i].split("Marca:")[1]);

					}
					if (newcontenido.toString().split("\n")[i].contains("Modelo:") && newcontenido.toString().split("\n")[i].contains("Placa:")) {
						modelo.setModelo(fn.castInteger( newcontenido.toString().split("\n")[i].split("Modelo:")[1].split("Placa")[0].trim()));
						modelo.setPlacas(newcontenido.toString().split("\n")[i].split("Placa:")[1].trim());

					}
					
					if (newcontenido.toString().split("\n")[i].contains("Motor:")) {
                     modelo.setMotor(newcontenido.toString().split("\n")[i].split("Motor:")[1]);
					}
					if (newcontenido.toString().split("\n")[i].contains("Serie:")) {
				         modelo.setSerie(newcontenido.toString().split("\n")[i].split("Serie:")[1]);
					}					
				}				
			}
			
			inicio  = contenido.indexOf(MONEDA);
			fin = contenido.indexOf("Si su vehículo" );
			
			if(inicio >  0 && fin > 0 && inicio < fin) {
				newcontenido = new StringBuilder();
				newcontenido.append(contenido.substring( inicio, fin).replace("@@@", "").replace("\r", ""));
				for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
					if(newcontenido.toString().split("\n")[i].contains("Moneda:") && newcontenido.toString().split("\n")[i].contains("Prima Neta:")) {
						modelo.setMoneda(fn.moneda(newcontenido.toString().split("\n")[i].split("Moneda:")[1].split("Total")[0].trim()));
						modelo.setPrimaneta(fn.castBigDecimal(fn.castDouble(newcontenido.toString().split("\n")[i].split("Neta:")[1])));
					}
					if(newcontenido.toString().split("\n")[i].contains("Fraccionado")) {
						modelo.setRecargo(fn.castBigDecimal(fn.castDouble(newcontenido.toString().split("\n")[i].split("Fraccionado")[1].split(":")[1])));
					}
					if(newcontenido.toString().split("\n")[i].contains("Pago:") && newcontenido.toString().split("\n")[i].contains("Expedición:")) {
						modelo.setFormaPago( fn.formaPago(newcontenido.toString().split("\n")[i].split("Pago:")[1].split("Gastos")[0].trim()));
						modelo.setDerecho(fn.castBigDecimal(fn.castDouble(newcontenido.toString().split("\n")[i].split("Expedición:")[1])));
						
					}
					if (newcontenido.toString().split("\n")[i].contains("IVA")) {
						modelo.setIva(fn.castBigDecimal(fn.castDouble(newcontenido.toString().split("\n")[i].split("IVA")[1].split(":")[1])));
					}

					if (newcontenido.toString().split("\n")[i].contains("Prima Total:")) {
						modelo.setPrimaTotal(fn.castBigDecimal(fn.castDouble(newcontenido.toString().split("\n")[i].split("Prima Total:")[1])));
					}
				}
			}
			
			//PROCESO PARA LAS COBERTURAS
			for (int i = 0; i < contenido.split(RIESGOSCUBIERTOS).length; i++) {		
				if(contenido.split(RIESGOSCUBIERTOS)[i].contains(MONEDA)) {
					newCobertura.append(contenido.split(RIESGOSCUBIERTOS)[i].split(MONEDA)[0]);
				}else
					if(contenido.split(RIESGOSCUBIERTOS)[i].contains("En cumplimiento")) {
            	   newCobertura.append(contenido.split(RIESGOSCUBIERTOS)[i].split("En cumplimiento")[0]);
				}
			}
			
			String auxStr = newCobertura.toString();
			newCobertura = new StringBuilder();
			newCobertura.append(auxStr.replace("@@@", "").replace("\r", "").trim()
					.replace("VALOR GUIA EBC", "VALOR GUIA EBC###")
					.replace("AMPARADA", "AMPARADA###")
					.replace(".00", ".00###")
					.replace("0.0", "0.0###")
					.replace(" % ", " %###"));
			if(newCobertura.length() > 0)
			{
				List<EstructuraCoberturasModel> coberturas = new ArrayList<>();
				for (int i = 0; i < newCobertura.toString().split("\n").length; i++) {
					EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();
					int sp = newCobertura.toString().split("\n")[i].split("###").length;
					if(sp > 2) {
						cobertura.setNombre(newCobertura.toString().split("\n")[i].split("###")[0]);
						cobertura.setSa(newCobertura.toString().split("\n")[i].split("###")[1]);
						cobertura.setDeducible(newCobertura.toString().split("\n")[i].split("###")[2]);
						coberturas.add(cobertura);
					}
				}
				modelo.setCoberturas(coberturas);
				
			}
			
			newCobertura = null;
			newcontenido = null;
			resultado = null;

			return modelo;
		} catch (Exception ex) {
			modelo.setError(
					AigAutosModel.this.getClass().getTypeName() + " - catch:" + ex.getMessage() + " | " + ex.getCause());
			return modelo;
		}
		
	}
}
