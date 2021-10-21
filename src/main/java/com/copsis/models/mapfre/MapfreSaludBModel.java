package com.copsis.models.mapfre;

import java.util.ArrayList;
import java.util.List;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraAseguradosModel;
import com.copsis.models.EstructuraCoberturasModel;
import com.copsis.models.EstructuraJsonModel;

public class MapfreSaludBModel {

	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
	private String contenido = "";

	public MapfreSaludBModel(String contenido) {
		this.contenido = contenido;
	}
	public EstructuraJsonModel procesar() {
		contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());
		contenido = contenido.replace("Póliza Número :", "Póliza Número:")
				.replace("las 12:00 hrs. de:", "")
				.replace("SIN LIMITE", "###SIN LIMITE###")
				.replace("AYUDA DE MATERNIDAD", "AYUDA DE MATERNIDAD###")
				.replace("AMPARADA", "###AMPARADA###")
				.replace("BÁSICO", "###BÁSICO###");
		String newcontenido = "";
		int inicio = 0;
		int fin = 0;
		try {
			modelo.setTipo(3);
			modelo.setCia(22);
			inicio = contenido.indexOf("GASTOS MÉDICOS MAYORES");
			fin = contenido.indexOf("COBERTURAS SUMA ASEGURADA");
			if (inicio > -1 & fin > -1 & inicio < fin) {
				newcontenido = contenido.substring(inicio, fin).replace("@@@", "").replace("\r", "");
				for (int i = 0; i < newcontenido.split("\n").length; i++) {
					if(newcontenido.split("\n")[i].contains("Póliza Número:")) {
						modelo.setPoliza(newcontenido.split("\n")[i].split("Póliza Número:")[1].replace("###", "").strip());
					}
					if(newcontenido.split("\n")[i].contains("Contratante:") && newcontenido.split("\n")[i].contains("R.F.C:")) {
						modelo.setCteNombre(newcontenido.split("\n")[i].split("Contratante:")[1].split("R.F.C:")[0].replace("###", "").strip());
						modelo.setRfc(newcontenido.split("\n")[i].split("R.F.C:")[1].replace("###", "").strip());
					}
					if(newcontenido.split("\n")[i].contains("Domicilio:") && newcontenido.split("\n")[i].contains("Tel:")) {
						modelo.setCteDireccion(newcontenido.split("\n")[i].split("Domicilio:")[1].split("Tel:")[0].replace("###", "").strip());
					}
					if(newcontenido.split("\n")[i].contains("Desde") && newcontenido.split("\n")[i].contains("Clave de Agente:")) {
						modelo.setVigenciaDe(fn.formatDate_MonthCadena( newcontenido.split("\n")[i].split("Desde")[1].split("Clave de Agente:")[0].replace("###", "").strip()));
						modelo.setCveAgente(newcontenido.split("\n")[i+1].split("###")[2].replace("###", "").strip());
						modelo.setAgente(newcontenido.split("\n")[i+1].split("###")[3].replace("###", "").strip());
					}
					if(newcontenido.split("\n")[i].contains("Hasta") ) {
						modelo.setVigenciaA(fn.formatDate_MonthCadena(newcontenido.split("\n")[i].split("Hasta")[1].split("###")[1].replace("###", "").strip()));
					}
					if(newcontenido.split("\n")[i].contains("Fecha de Emisión") && newcontenido.split("\n")[i].contains("Forma de Pago:") && newcontenido.split("\n")[i].contains("Moneda") ) {
						modelo.setFechaEmision(fn.formatDate_MonthCadena(newcontenido.split("\n")[i+1].split("###")[0].replace("###", "").replace(" ", "")).strip() );					
						modelo.setFormaPago(fn.formaPago( newcontenido.split("\n")[i+1].split("###")[2].replace("###", "").strip()));
						modelo.setMoneda(1);
						
					}
					if(newcontenido.split("\n")[i].contains("Prima Neta:") && newcontenido.split("\n")[i].contains("Expedición") && newcontenido.split("\n")[i].contains("Prima Total:") ) {
						int sp  = newcontenido.split("\n")[i+1].split("###").length;
						switch (sp) {
						case 7:
				               modelo.setPrimaneta(fn.castBigDecimal( fn.preparaPrimas(newcontenido.split("\n")[i + 1].split("###")[0])));
		                        modelo.setRecargo(fn.castBigDecimal(fn.preparaPrimas(newcontenido.split("\n")[i + 1].split("###")[2])));
		                        modelo.setDerecho(fn.castBigDecimal(fn.preparaPrimas(newcontenido.split("\n")[i + 1].split("###")[3])));
		                        modelo.setIva(fn.castBigDecimal(fn.preparaPrimas(newcontenido.split("\n")[i + 1].split("###")[5])));
		                        modelo.setPrimaTotal(fn.castBigDecimal(fn.preparaPrimas(newcontenido.split("\n")[i + 1].split("###")[6])));
							
							break;
						}						
					}
				}
			}
			

			
			inicio = contenido.indexOf("COBERTURAS SUMA ASEGURADA");
			fin = contenido.indexOf("LAS ANTERIORES COBERTURAS");	
			if (inicio > -1 & fin > -1 & inicio < fin) {
				 List<EstructuraCoberturasModel> coberturas = new ArrayList<>();
				newcontenido = contenido.substring(inicio, fin).replace("@@@", "").replace("\r", "")
						.replace("10%", "###10%###")
						.replace("Usd", "###Usd###")
						.replace("30%", "###30%###")
						.replace("VISIÓN", "VISIÓN###");
				for (int i = 0; i < newcontenido.split("\n").length; i++) {
					EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();

					if(newcontenido.split("\n")[i].contains("DEDUCIBLE")) {						
					}else {
						int sp =newcontenido.split("\n")[i].split("###").length;
						switch (sp) {
						case  3:
							   cobertura.setNombre(newcontenido.split("\n")[i].split("###")[0].strip());
	                              cobertura.setSa(newcontenido.split("\n")[i].split("###")[1].strip());	                           
	                              coberturas.add(cobertura);
							break;
						case  4: 	case  5:
							  cobertura.setNombre(newcontenido.split("\n")[i].split("###")[0].strip());
                              cobertura.setSa(newcontenido.split("\n")[i].split("###")[1].strip());
                              cobertura.setDeducible(newcontenido.split("\n")[i].split("###")[2].strip());
                              coberturas.add(cobertura);
							break;
					
							
						}
						
					}
				}
				modelo.setCoberturas(coberturas);
				inicio = contenido.indexOf("LISTA DE ASEGURADOS:");
				fin = contenido.indexOf("FECHAS DE ANTIGÜEDAD: ");	
				if (inicio > -1 & fin > -1 & inicio < fin) {
				       List<EstructuraAseguradosModel> asegurados = new ArrayList<>();
					newcontenido = contenido.substring(inicio, fin).replace("@@@", "").replace("\r", "")
							.replace(" F ", "###F###")
							.replace("TITULAR", "###TITULAR###")
							.replace("HIJO-A", "###HIJO-A###")
							.replace(" M ", "###M###")
							.replace("CONYUGE", "###CONYUGE###")
							
							;
					for (int i = 0; i < newcontenido.split("\n").length; i++) {
						EstructuraAseguradosModel asegurado = new EstructuraAseguradosModel();
						if(newcontenido.split("\n")[i].contains("PARENTESCO")  || newcontenido.split("\n")[i].contains("LISTA DE ASEGURADOS") ) {						
						}else {
							
							int sp =newcontenido.split("\n")[i].split("###").length;
							System.out.println(newcontenido.split("\n")[i] +" <===> " +sp);

							switch (sp) {
							case  5:
								asegurado.setNombre(newcontenido.split("\n")[i].split("###")[0].split(newcontenido.split("\n")[i].split("###")[0].split(" ")[1])[1].strip());
								asegurado.setSexo(fn.sexo(newcontenido.split("\n")[i].split("###")[1]) ? 1 : 0);
								asegurado.setEdad(Integer.parseInt(newcontenido.split("\n")[i].split("###")[2].strip()));
								asegurado.setParentesco(fn.parentesco(newcontenido.split("\n")[i].split("###")[3].strip()));
								asegurado.setAntiguedad(fn.formatDate_MonthCadena(newcontenido.split("\n")[i].split("###")[4].strip().split(" ")[1]));
								asegurados.add(asegurado);
								break;
							}
						}
					}
					modelo.setAsegurados(asegurados);
				}				

			}
			
			return modelo;
		} catch (Exception e) {
			return modelo;
		}
		
	}

}
