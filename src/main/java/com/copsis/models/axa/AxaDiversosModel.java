package com.copsis.models.axa;

import java.util.ArrayList;
import java.util.List;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraCoberturasModel;
import com.copsis.models.EstructuraJsonModel;
import com.copsis.models.EstructuraRecibosModel;
import com.copsis.models.EstructuraUbicacionesModel;

import net.bytebuddy.agent.builder.AgentBuilder.InitializationStrategy.SelfInjection.Split;

public class AxaDiversosModel {
	// Clases
	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
	// Varaibles
	public String inicontenido = "";
	private String contenido = "";
	public String newcontenido = "";
	private String resultado = "";
	private String textbusq = "";
	private int inicio = 0;
	private int fin = 0;
	private int donde = 0;
	
	public AxaDiversosModel(String contenido) {
		this.contenido = contenido;
	}

	public EstructuraJsonModel procesar() {
		contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());
		contenido = contenido.replace("D omicilio:", "Domicilio:").replace("F RESNOS", "FRESNOS")
				.replace("P r i m a T o t a l", "Prima Total").replace("Prima###neta:", "Prima Neta:").replace("Prima neta:", "Prima Neta:")
				.replace("expedición", "Expedición").replace("C .P:", "C.P:").replace("R .F.C: ", "R.F.C: ")
				.replace("@@@I.V.A:", "I.V.A.").replace("I.V.A", "I.V.A.").replace("I.V.A..", "I.V.A.").replace("Prima###Total", "Prima Total").replace("financiamiento", "Financiamiento");
		try {
			modelo.setTipo(7);
			modelo.setCia(20);

			textbusq="Datos del contratante";
			inicio = contenido.indexOf("Datos del contratante");
			if(inicio == -1) {
				textbusq="Datos###del###contratante";
				inicio = contenido.indexOf("Datos###del###contratante");
				if(inicio == -1) {
					textbusq="Datos del asegurado###Póliza";
					inicio = contenido.indexOf("Datos del asegurado###Póliza");//se usa para version 2
				}
			}
			fin  = contenido.indexOf("Costo del seguro");
			if(fin == -1) {
				fin  = contenido.indexOf("Costo###del###seguro"); 
				if(fin == -1) {
					fin  = contenido.indexOf("Paquete contratado:");//se usa para version 2
				}
			}
			
			
			if(inicio > 0 & fin > 0 & inicio < fin) {
				newcontenido = contenido.substring(inicio,fin);
			
				for (int i = 0; i < newcontenido.split("\n").length; i++) {
					if(newcontenido.split("\n")[i].contains("Póliza") && newcontenido.split("\n")[i].contains(textbusq)) {
				
						if(newcontenido.split("\n")[i+1].split("###").length > 1) {
							String x = newcontenido.split("\n")[i+1].replace("Nombre:", "");
							modelo.setPoliza(x.split("###")[x.split("###").length -1].replace("\r", ""));
							modelo.setCteNombre((x.split(modelo.getPoliza())[0].replace("###", " ")).trim());							
						}else {							
							String x = newcontenido.split("\n")[i+1].replace(" ", "###").replace("Nombre:", "");
						
							modelo.setPoliza(x.split("###")[x.split("###").length -1].replace("\r", ""));
							modelo.setCteNombre((x.split(modelo.getPoliza())[0].replace("###", " ")).trim());
						}									
					}
					if (newcontenido.split("\n")[i].contains("Domicilio:")) {
						String x = "";
						if (newcontenido.split("\n")[i + 1].contains("C.P:")) {
							x = (newcontenido.split("\n")[i + 1].split("C.P:")[0]).replace("\r", "");
						}
						if (newcontenido.split("\n")[i + 1].contains("Vigencia")) {
							x = (newcontenido.split("\n")[i + 1].split("Vigencia")[0]).replace("\r", "");
						}
						if (newcontenido.split("\n")[i + 1].contains("Vigencia a las")) {
							x = (newcontenido.split("\n")[i + 1].split("Vigencia a las")[1]).replace("\r", "").replace("1 2 hrs", "");
						}


						modelo.setCteDireccion((newcontenido.split("\n")[i].split("Domicilio:")[1] + x).replace("\r", "").replace("###", "").replace("@@@", "").trim());
					}
					if(newcontenido.split("\n")[i].contains("C.P:")) {
						if(newcontenido.split("\n")[i].split("C.P:")[1].contains("Vigencia")) {
							modelo.setCp(newcontenido.split("\n")[i].split("C.P:")[1].split("Vigencia")[0].trim());
						}
						if(newcontenido.split("\n")[i].split("C.P:")[1].contains("Desde")) {
							modelo.setCp(newcontenido.split("\n")[i].split("C.P:")[1].split("Desde")[0].replace("###", "").trim());
						}						
					}
					if(newcontenido.split("\n")[i].contains("R.F.C:")) {
						if(newcontenido.split("\n")[i].split("R.F.C:")[1].contains("Desde")) {
							modelo.setRfc(newcontenido.split("\n")[i].split("R.F.C:")[1].split("Desde")[0].replace("###", "").trim());
						}if(newcontenido.split("\n")[i].split("R.F.C:")[1].contains("Hasta")) {
							modelo.setRfc(newcontenido.split("\n")[i].split("R.F.C:")[1].split("Hasta")[0].replace("###", "").trim());
						}
						else {
							modelo.setRfc(newcontenido.split("\n")[i].split("R.F.C:")[1].replace("\r", "").replace("###", ""));
						}
						
					}
					if(newcontenido.split("\n")[i].contains("Desde")) {
						modelo.setVigenciaDe(fn.formatDate_MonthCadena(newcontenido.split("\n")[i].split("Desde")[1].replace("\r", "").replace(" ", "").replace("###", "")));
					}
					if(newcontenido.split("\n")[i].contains("Hasta")) {
						modelo.setVigenciaA(fn.formatDate_MonthCadena(newcontenido.split("\n")[i].split("Hasta")[1].replace("\r", "").replace(" ", "").replace("###", "")));
					}
					if(newcontenido.split("\n")[i].contains("Emisión")) {
						modelo.setFechaEmision(fn.formatDate_MonthCadena(newcontenido.split("\n")[i].split("Emisión")[1].replace("\r", "").replace(" ", "").replace("###", "")));
					}
					
					if(newcontenido.split("\n")[i].contains("Moneda")) {
						modelo.setMoneda(fn.moneda(newcontenido.split("\n")[i].split("Moneda")[1].replace("\r", "").replace(" ", "").replace("###", "")));
					}
					
					if(newcontenido.split("\n")[i].contains("Nombre") && newcontenido.split("\n")[i].contains("Agente:")) {
						
						modelo.setAgente((newcontenido.split("\n")[i].split("Agente:")[1].split("###")[newcontenido.split("\n")[i].split("Agente:")[1].split("###").length -2]).trim().replace("###", " "));
					}
				
					if(newcontenido.split("\n")[i].contains("Número") && newcontenido.split("\n")[i].contains("Agente:")) {
						modelo.setCveAgente((newcontenido.split("\n")[i].split("Agente:")[1].split("No.")[0]).trim().replace("###", ""));
					}
					if(newcontenido.split("\n")[i].contains("Subramo") && newcontenido.split("\n")[i].contains("pago")) {
						modelo.setFormaPago(fn.formaPago(newcontenido.split("\n")[i+1].split("Folio:")[1].replace("###", "").replace("\r", "").trim()));
					}
					if(newcontenido.split("\n")[i].contains("Uso:") && newcontenido.split("\n")[i].contains("pago")) {
						modelo.setFormaPago(fn.formaPago(newcontenido.split("\n")[i+1].split("Régimen:")[1].replace("Vivienda Sola", "").replace("###", "").replace("\r", "").trim()));
					}
				
				}
			}
			
			
			//Primas de la poliza
			inicio = contenido.indexOf("Prima Neta");
			fin = contenido.indexOf("Relación de bienes opcionales");

			if( fin == -1) {
				inicio = contenido.indexOf("Costo###del###seguro");
				fin = contenido.indexOf("AXA###Seguros");
				if(inicio ==-1 && fin == -1) {
					inicio = contenido.indexOf("Costo del seguro");
					fin = contenido.indexOf("AXA Seguros, S.A.");
				}
			}
				
			if(inicio > 0 & fin > 0 & inicio < fin) {
				newcontenido = contenido.substring(inicio,fin);				
				for (int i = 0; i < newcontenido.split("\n").length; i++) {		
					if(newcontenido.split("\n")[i].contains("Prima Neta")) {
						modelo.setPrimaneta(fn.castBigDecimal( fn.castDouble(newcontenido.split("\n")[i].split("Prima Neta")[1].replace(":", "").replace("###", "").replace("\r", ""))));
					}
					if(newcontenido.split("\n")[i].contains("Financiamiento")) {
						if(newcontenido.split("\n")[i].contains("Financiamiento 0.0%")) {
							modelo.setDerecho(fn.castBigDecimal( fn.castDouble(newcontenido.split("\n")[i].split("Financiamiento 0.0%")[1].replace("###", "").replace("\r", ""))));	
						}else {							
							modelo.setDerecho(fn.castBigDecimal( fn.castDouble(newcontenido.split("\n")[i].split("Financiamiento")[1].split("###")[1].replace("###", "").replace("\r", ""))));
						}						
					}					
					if(newcontenido.split("\n")[i].contains("Expedición")) {
						modelo.setRecargo(fn.castBigDecimal( fn.castDouble(newcontenido.split("\n")[i].split("Expedición")[1].replace(":", "").replace("###", "").replace("\r", ""))));
					}					
					if(newcontenido.split("\n")[i].contains("I.V.A.")) {
						if(newcontenido.split("\n")[i].split("I.V.A.")[1].replace(":", "").split("###")[1].contains("%")) {
							modelo.setIva(fn.castBigDecimal( fn.castDouble(newcontenido.split("\n")[i].split("I.V.A.")[1].replace(":", "").split("###")[0].replace("###", "").replace("\r", ""))));
						}else {
							modelo.setIva(fn.castBigDecimal( fn.castDouble(newcontenido.split("\n")[i].split("I.V.A.")[1].replace(":", "").split("###")[1].replace("###", "").replace("\r", ""))));
						}													
					}					
					if(newcontenido.split("\n")[i].contains("Prima Total")) {
						modelo.setPrimaTotal(fn.castBigDecimal( fn.castDouble(newcontenido.split("\n")[i].split("Prima Total")[1].replace(":", "").replace("###", "").replace("\r", ""))));
					}
				}				
			}
			

			
			//Proceso para ubicaciones
			inicio = contenido.indexOf("Descripción de la ");
			fin = contenido.indexOf("Paquete contratado:");
			
			if(inicio > 0 & fin > 0 & inicio < fin) {
				List<EstructuraUbicacionesModel> ubicaciones = new ArrayList<>();
				EstructuraUbicacionesModel ubicacion = new EstructuraUbicacionesModel();
				newcontenido = contenido.substring(inicio,fin);				
				for (int i = 0; i < newcontenido.split("\n").length; i++) {
					if(newcontenido.split("\n")[i].contains("Muros:")) {
						ubicacion.setMuros(fn.material(newcontenido.split("\n")[i]));
					}
					if(newcontenido.split("\n")[i].contains("Niveles:")) {
						ubicacion.setNiveles(fn.castInteger(newcontenido.split("\n")[i].split("Niveles:")[1].replace("\r", "").trim()).intValue());
					}					
				}
				ubicaciones.add(ubicacion);
				modelo.setUbicaciones(ubicaciones);
			}
			
			/*Proceoso para las  coberturas*/
			inicio = contenido.indexOf("Paquete contratado");
			fin = contenido.indexOf("Prima Neta");
			if(inicio > 0 & fin > 0 & inicio < fin) {
				List<EstructuraCoberturasModel> coberturas = new ArrayList<>();				
				newcontenido = contenido.substring(inicio,fin);				
				for (int i = 0; i < newcontenido.split("\n").length; i++) {
					EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();
				    int x = newcontenido.split("\n")[i].split("###").length;
				    if(x == 2) {
				    	cobertura.setNombre(newcontenido.split("\n")[i].split("###")[0]);
				    	cobertura.setSa(newcontenido.split("\n")[i].split("###")[1].replace("\r", ""));
				    	coberturas.add(cobertura);				
				    }
				    if(x == 3) {
				    	cobertura.setNombre(newcontenido.split("\n")[i].split("###")[0]);
				    	cobertura.setSa(newcontenido.split("\n")[i].split("###")[1]);
				    	cobertura.setDeducible(newcontenido.split("\n")[i].split("###")[2].replace("\r", ""));
				    	coberturas.add(cobertura);
				    }
				    if(x == 4) {
				    	cobertura.setNombre(newcontenido.split("\n")[i].split("###")[0]);
				    	cobertura.setSa(newcontenido.split("\n")[i].split("###")[1]);
				    	cobertura.setDeducible(newcontenido.split("\n")[i].split("###")[2].replace("\r", ""));
				    	coberturas.add(cobertura);
				    }
				}
				modelo.setCoberturas(coberturas);
			}
		
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
				recibo.setPrimaneta(modelo.getPrimaneta());
				recibo.setDerecho(modelo.getDerecho());
				recibo.setRecargo(modelo.getRecargo());
				recibo.setIva(modelo.getDerecho());
				recibo.setPrimaTotal(modelo.getPrimaTotal());
				recibo.setAjusteUno(modelo.getAjusteUno());
				recibo.setAjusteDos(modelo.getAjusteDos());
				recibo.setCargoExtra(modelo.getCargoExtra());
				recibos.add(recibo);
				break;
			}
			modelo.setRecibos(recibos);
			
			
			
			return modelo;
		} catch (Exception ex) {
			modelo.setError(
					AxaDiversosModel.this.getClass().getTypeName() + " | " + ex.getMessage() + " | " + ex.getCause());
			return modelo;
		}
		
	}

}
