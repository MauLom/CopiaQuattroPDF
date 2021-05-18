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
	private String newcontenido = "";
	private String newCobertura = "";
	private String resultado = "";
	private int inicio = 0;
	private int fin = 0;

	
	public AigAutosModel(String contenido) {
		this.contenido = contenido;
	}
	
	public EstructuraJsonModel procesar() {
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
			
			System.out.println("---> " + contenido);
			//tipo
			modelo.setTipo(1);

			//cia
			modelo.setCia(3);
			
			//Datos del contratante
			
			inicio  = contenido.indexOf("SEGURO DE AUTOMÓVILES");
			fin = contenido.indexOf("RIESGOS CUBIERTOS ");
			if(inicio >  0 && fin > 0 && inicio < fin) {
				newcontenido = contenido.substring( inicio, fin).replace("@@@", "").replace("\r", "");
				for (int i = 0; i < newcontenido.split("\n").length; i++) {
					if(newcontenido.split("\n")[i].contains("PÓLIZA:")) {					
						modelo.setPoliza(newcontenido.split("\n")[i].split("PÓLIZA")[1].split("-")[2].trim());
					}
					if(newcontenido.split("\n")[i].contains("VIGENCIA DEL SEGURO")) {
						modelo.setVigenciaDe( fn.formatDate_MonthCadena(newcontenido.split("\n")[i+1].split("###")[0].trim()));
						modelo.setVigenciaA( fn.formatDate_MonthCadena(newcontenido.split("\n")[i+1].split("###")[1].trim()));
					}
					if(newcontenido.split("\n")[i].contains("Nombre:") && newcontenido.split("\n")[i].contains("R.F.C:")) {
						modelo.setCteNombre(newcontenido.split("\n")[i].split("Nombre:")[1].split("R.F.C:")[0].trim());
						modelo.setRfc(newcontenido.split("\n")[i].split("R.F.C:")[1]);						
					}
					if(newcontenido.split("\n")[i].contains("Código Postal:")) {
						modelo.setCp(newcontenido.split("\n")[i].split("Código Postal:")[1].trim());
					}
					
					if(newcontenido.split("\n")[i].contains("Calle:")) {
						resultado = newcontenido.split("\n")[i].split("Calle:")[1];
					}
					
					if(newcontenido.split("\n")[i].contains("Colonia:")) {
						resultado += newcontenido.split("\n")[i].split("Colonia:")[1];						
					}
                     if(newcontenido.split("\n")[i].contains("Estado-Municipio:")) {
						modelo.setCteDireccion(( resultado += newcontenido.split("\n")[i].split("Estado-Municipio:")[1]).trim());
					}
                     
                     if(newcontenido.split("\n")[i].contains("Conductor Habitual:")) {
                    	 modelo.setConductor(newcontenido.split("\n")[i].split("Habitual:")[1].trim());
                    	 
                     }
					
					if (newcontenido.split("\n")[i].contains("Marca:")) {
						modelo.setMarca(newcontenido.split("\n")[i].split("Marca:")[1]);

					}
					if (newcontenido.split("\n")[i].contains("Modelo:") && newcontenido.split("\n")[i].contains("Placa:")) {
						modelo.setModelo(fn.castInteger( newcontenido.split("\n")[i].split("Modelo:")[1].split("Placa")[0].trim()));
						modelo.setPlacas(newcontenido.split("\n")[i].split("Placa:")[1].trim());

					}
					
					if (newcontenido.split("\n")[i].contains("Motor:")) {
                     modelo.setMotor(newcontenido.split("\n")[i].split("Motor:")[1]);
					}
					if (newcontenido.split("\n")[i].contains("Serie:")) {
				         modelo.setSerie(newcontenido.split("\n")[i].split("Serie:")[1]);
					}					
				}				
			}
			
			inicio  = contenido.indexOf("Moneda");
			fin = contenido.indexOf("Si su vehículo" );
			if(inicio >  0 && fin > 0 && inicio < fin) {
				newcontenido = contenido.substring( inicio, fin).replace("@@@", "").replace("\r", "");
				for (int i = 0; i < newcontenido.split("\n").length; i++) {
					System.out.println("=====>"+newcontenido.split("\n")[i]);
					if(newcontenido.split("\n")[i].contains("Moneda:") && newcontenido.split("\n")[i].contains("Prima Neta:")) {
						modelo.setMoneda(fn.moneda(newcontenido.split("\n")[i].split("Moneda:")[1].split("Total")[0].trim()));
						modelo.setPrimaneta(fn.castBigDecimal(fn.castDouble(newcontenido.split("\n")[i].split("Neta:")[1])));
					}
					if(newcontenido.split("\n")[i].contains("Fraccionado")) {
						modelo.setRecargo(fn.castBigDecimal(fn.castDouble(newcontenido.split("\n")[i].split("Fraccionado")[1].split(":")[1])));
					}
					if(newcontenido.split("\n")[i].contains("Pago:") && newcontenido.split("\n")[i].contains("Expedición:")) {
						modelo.setFormaPago( fn.formaPago(newcontenido.split("\n")[i].split("Pago:")[1].split("Gastos")[0].trim()));
						modelo.setDerecho(fn.castBigDecimal(fn.castDouble(newcontenido.split("\n")[i].split("Expedición:")[1])));
						
					}
					if (newcontenido.split("\n")[i].contains("IVA")) {
						modelo.setIva(fn.castBigDecimal(fn.castDouble(newcontenido.split("\n")[i].split("IVA")[1].split(":")[1])));
					}

					if (newcontenido.split("\n")[i].contains("Prima Total:")) {
						modelo.setPrimaTotal(fn.castBigDecimal(fn.castDouble(newcontenido.split("\n")[i].split("Prima Total:")[1])));
					}
				}
			}
			
			//PROCESO PARA LAS COBERTURAS
			for (int i = 0; i < contenido.split("RIESGOS CUBIERTOS").length; i++) {		
				if(contenido.split("RIESGOS CUBIERTOS")[i].contains("Moneda")) {
					newCobertura = contenido.split("RIESGOS CUBIERTOS")[i].split("Moneda")[0];
				}else
               if(contenido.split("RIESGOS CUBIERTOS")[i].contains("En cumplimiento")) {
            	   newCobertura += contenido.split("RIESGOS CUBIERTOS")[i].split("En cumplimiento")[0];
				}
			}

			newCobertura = newCobertura.replace("@@@", "").replace("\r", "").trim()
					.replace("VALOR GUIA EBC", "VALOR GUIA EBC###")
					.replace("AMPARADA", "AMPARADA###")
					.replace(".00", ".00###")
					.replace("0.0", "0.0###")
					.replace(" % ", " %###")
					;
			if(newCobertura.length() > 0)
			{
				List<EstructuraCoberturasModel> coberturas = new ArrayList<>();
				for (int i = 0; i < newCobertura.split("\n").length; i++) {
					EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();
					int sp = newCobertura.split("\n")[i].split("###").length;
					if(sp > 2) {
						cobertura.setNombre(newCobertura.split("\n")[i].split("###")[0]);
						cobertura.setSa(newCobertura.split("\n")[i].split("###")[1]);
						cobertura.setDeducible(newCobertura.split("\n")[i].split("###")[2]);
						coberturas.add(cobertura);
					}
	
				}
				modelo.setCoberturas(coberturas);
				
			}

			return modelo;
		} catch (Exception ex) {
			modelo.setError(
					AigAutosModel.this.getClass().getTypeName() + " - catch:" + ex.getMessage() + " | " + ex.getCause());
			return modelo;
		}
		
	}
}
